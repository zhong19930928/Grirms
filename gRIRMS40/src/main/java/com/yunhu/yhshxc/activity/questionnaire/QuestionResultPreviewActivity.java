package com.yunhu.yhshxc.activity.questionnaire;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import gcg.org.debug.JLog;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.activity.questionnaire.Util.QuestionnaireUtil;
import com.yunhu.yhshxc.activity.questionnaire.bo.Findings;
import com.yunhu.yhshxc.activity.questionnaire.bo.Question;
import com.yunhu.yhshxc.activity.questionnaire.bo.Questionnaire;
import com.yunhu.yhshxc.activity.questionnaire.bo.SurveyAdress;
import com.yunhu.yhshxc.activity.questionnaire.db.FindingsDB;
import com.yunhu.yhshxc.activity.questionnaire.db.QuestionDB;
import com.yunhu.yhshxc.activity.questionnaire.db.QuestionnaireDB;
import com.yunhu.yhshxc.activity.questionnaire.db.SurveyAdressDB;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.loopj.android.http.RequestParams;

public class QuestionResultPreviewActivity extends AbsBaseActivity{
	
	/**
	 * 调查结果预览页面
	 */
	
	private Button btn_question_preview_back;
	private ArrayAdapter<String> adapter;    
	private LinearLayout ll_question_preview_item_title;
	private Button btn_preview_to_explain;
	private Button btn_question_result_search;
	private Button btn_question_result_before;
	private Button btn_question_result_next;
	private Button btn_question_result_delete;
	private Button btn_question_result_time;
	private List<Question> questionList = new ArrayList<Question>();
	private SurveyAdress surveyAdress = new SurveyAdress();
	private SurveyAdressDB surveyAdressDB;//调查地址
	private FindingsDB findingsDB;//调查结果
	private List<Findings> findingsList = new ArrayList<Findings>();
	private Findings findings = new Findings();
	private int qId;//问卷调查Id
	private int rId;
	private TextView tv_name;
	private TextView tv_qusetion_preview;
	private QuestionDB questionDB;//问题数据库
	private TextView tv_nums;
	private int total = 0;
	private int Count;
	private int  currentPage = 1;
	private QuestionnaireDB questionnaireDB;//问卷的整体定义数据库
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_question_result_preview);
		questionnaireDB= new QuestionnaireDB(this);
		init();
	}
	
	private void init(){
		surveyAdressDB = new SurveyAdressDB(this);
		questionDB = new QuestionDB(this);
		findingsDB = new FindingsDB(this);
		Intent intent = getIntent();
		qId = Integer.parseInt(intent.getStringExtra("qId"));
		rId = intent.getIntExtra("rId", 0);
		Count = intent.getIntExtra("Count", 0);
//		findingsList = findingsDB.findAllFindIng();
		
		
		tv_nums = (TextView) findViewById(R.id.tv_nums);
		btn_question_preview_back = (Button) findViewById(R.id.btn_question_preview_back);
		ll_question_preview_item_title = (LinearLayout) findViewById(R.id.ll_question_preview_item_title);
		btn_preview_to_explain = (Button) findViewById(R.id.btn_preview_to_explain);
		btn_question_result_search = (Button) findViewById(R.id.btn_question_result_search);
		btn_question_result_before = (Button) findViewById(R.id.btn_question_result_before);
		btn_question_result_before.setVisibility(View.GONE);
		btn_question_result_next = (Button) findViewById(R.id.btn_question_result_next);
		if(Count==1){
			btn_question_result_next.setVisibility(View.GONE);
		}
		btn_question_result_delete = (Button) findViewById(R.id.btn_question_result_delete);
		btn_question_result_time = (Button) findViewById(R.id.btn_question_result_time);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_qusetion_preview = (TextView) findViewById(R.id.tv_qusetion_preview);
		
//		for(int i = 0;i < findingsList.size();i++){
//			if(findingsList.get(i).getResultId() == rId){
//				total = i + 1;
//			}
//		}
		tv_nums.setText(currentPage + "/" + Count);

		btn_question_preview_back.setOnClickListener(this);
		btn_preview_to_explain.setOnClickListener(this);
		btn_question_result_search.setOnClickListener(this);
		btn_question_result_before.setOnClickListener(this);
		btn_question_result_next.setOnClickListener(this);
		btn_question_result_delete.setOnClickListener(this);
		btn_question_result_time.setOnClickListener(this);
		
		searchResultData(qId,0,true);
		
		
		

	}
	
	private void initData(){
		findings = findingsDB.findFindingsById(rId);
		if(findings != null){
			tv_name.setText(findings.getInvestigatorName());
			tv_qusetion_preview.setText(findings.getAdress());
			if(!TextUtils.isEmpty(findings.getEndDate())){	
//				if(findings.getEndDate().length() > 10){
//					btn_question_result_time.setText(findings.getEndDate().substring(0, 10));
//				}else{
					btn_question_result_time.setText(findings.getEndDate());
//				}
			
			
			}
		}
		
		
		
	}
	
	/**
	 * 添加子View，根据数据添加
	 */
	private void addView(){
		ll_question_preview_item_title.removeAllViews();
		questionList = getQuestionList();
		for(int i = 0;i < questionList.size();i++){
			QuestionPreviewView questionPreviewView = new QuestionPreviewView(getApplicationContext(),false);
			questionPreviewView.setID(qId, rId);
			questionPreviewView.setPreviewView(questionList.get(i) ,  i);
			ll_question_preview_item_title.addView(questionPreviewView.getView());
			
		}
		
	}
	
	private List<Question> getQuestionList(){
		
		questionList = questionDB.findQuestionListById(qId,1);//查询章节
		return questionList;
		
	}
	
	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_question_preview_back:
			this.finish();
			break;
		case R.id.btn_preview_to_explain:
			Questionnaire questionnaire = questionnaireDB.findQuestionnaireById(qId);
			if(questionnaire!=null){
				Intent intent_content = new Intent(getApplicationContext(), QuestionExplainActivity.class);
				intent_content.putExtra("qId", qId);
				startActivity(intent_content);
			}else{
				Toast.makeText(QuestionResultPreviewActivity.this, R.string.qustion_no1,Toast.LENGTH_LONG).show();
			}
			
			break;
		case R.id.btn_question_result_search:
			//查询按钮点击事件
			break;
		case R.id.btn_question_result_before:
			//上一份
//			if(total > 1){
//				Intent intentDown = new Intent(this, QuestionResultPreviewActivity.class);
//				intentDown.putExtra("qId",  qId + "");
//				intentDown.putExtra("rId", rId - 1);
//				startActivity(intentDown);
//				finish();
//			}
			
			if(currentPage>1){
				currentPage = currentPage-1;
				tv_nums.setText(currentPage + "/" + Count);
				rId = findingsList.get(currentPage-1).getResultId();
				initData();
				addView();
				btn_question_result_next.setVisibility(View.VISIBLE);
			
				if(currentPage==1){
					btn_question_result_before.setVisibility(View.GONE);
				}
			}else{
				btn_question_result_before.setVisibility(View.GONE);
			}
			break;
		case R.id.btn_question_result_next:
//			//下一份
//			if(total < findingsList.size()){
//				Intent intentUp = new Intent(this, QuestionResultPreviewActivity.class);
//				intentUp.putExtra("qId",  qId + "");
//				intentUp.putExtra("rId", rId + 1);
//				startActivity(intentUp);
//				finish();
//			}
			
			if(currentPage>=findingsList.size()){
				
			}else{
				currentPage = currentPage+1;
				tv_nums.setText(currentPage + "/" + Count);
				if(currentPage<findingsList.size()){
					
					rId = findingsList.get(currentPage-1).getResultId();
					initData();
					addView();
					btn_question_result_before.setVisibility(View.VISIBLE);
				}else{
					
					if(currentPage%10==0){
						
						rId = findingsList.get(currentPage-1).getResultId();
//						ToastOrder.makeText(QuestionResultPreviewActivity.this, "二次"+rId, Toast.LENGTH_LONG).show();
						searchResultData(qId,rId,false);
					}else{
						btn_question_result_next.setVisibility(View.GONE);
					}
					if(currentPage>1){
						btn_question_result_before.setVisibility(View.VISIBLE);
					}
				}
			}
			break;
		case R.id.btn_question_result_delete:
			//删除
			break;
		case R.id.btn_question_result_time:
			//时间选择控件
			break;
		default:
			break;
		}

	}
	private RequestParams searchParams(int qId,int rid) {

		RequestParams param = new RequestParams();
		param.put("phoneno",PublicUtils.receivePhoneNO(getApplicationContext()));
		if(qId!=0){
			param.put("questionId",qId+"");	
		}
		if(rid!=0){
			param.put("resultId",rid+"");	

		}
		
		JLog.d(TAG, param.toString());
		return param;
	}
	/**
	 * 查询问卷调查结果信息
	 */
	private Dialog resultDialog = null;
	private void searchResultData(int qId,int rid,final boolean isClearDb){
		resultDialog = new MyProgressDialog(this,R.style.CustomProgressDialog,PublicUtils.getResourceString(this,R.string.dialog_one2));
		String url = UrlInfo.doQuestionResultInfo(this);
		RequestParams param = searchParams(qId,rid);
		
		GcgHttpClient.getInstance(this.getApplicationContext()).post(url,
				param, new HttpResponseListener() {
					@Override
					public void onStart() {
						resultDialog.show();
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						JLog.d(TAG, "onSuccess:" + content);
						try {
							JSONObject obj = new JSONObject(content);
							String resultCode = obj.getString("resultcode");
							if ("0000".equals(resultCode)) {
								JSONArray questionnaireArray = new JSONArray();
								JSONArray findingsArray = new JSONArray();
								JSONArray findingDetailArray = new JSONArray();
								QuestionnaireUtil questionnaireUtil = new QuestionnaireUtil(getApplicationContext());
//								questionnaireList.clear();
								
								if(PublicUtils.isValid(obj, "qa")){
									questionnaireArray = obj.getJSONArray("qa");
									if(!TextUtils.isEmpty(questionnaireArray.toString())){
//										questionnaireList =
												questionnaireUtil.parserSearchListItem(questionnaireArray);
									}
									
								}
								
//								for(int i = 0;i < questionnaireList.size();i++){
//									Questionnaire questionnaire = questionnaireList.get(i);
//									JLog.d(questionnaire.getName());
//								}
								if(PublicUtils.isValid(obj, "ra")){
									findingsArray = obj.getJSONArray("ra");
									if(!TextUtils.isEmpty(findingsArray.toString())){
										List<Findings> findingsListTemp = questionnaireUtil.parserFindingsListItem(findingsArray);
										findingsList.addAll(findingsListTemp);
									}
								}
								
								if(PublicUtils.isValid(obj, "oa")){
									findingDetailArray = obj.getJSONArray("oa");
									if(!TextUtils.isEmpty(findingDetailArray.toString())){
//										findIngDetailsList =
												questionnaireUtil.parserFindIngDetailListItem(findingDetailArray,isClearDb);
									}
									
								}
								
								int size = findingsList.size();
								int postion = 0;
								if(size>0){
									if(size>10){
										postion = size-10;
									}
									
									rId = findingsList.get(postion).getResultId();
									
									initData();
									addView();
//								
									
								}else{
									btn_question_result_next.setVisibility(View.GONE);
								}
								
								
	
	
	
							} else {
								throw new Exception();
							}
						} catch (Exception e) {
							e.printStackTrace();
							ToastOrder.makeText(getApplicationContext(), R.string.ERROR_DATA,ToastOrder.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						JLog.d(TAG, "onFailure:" + content);
						ToastOrder.makeText(getApplicationContext(), R.string.retry_net_exception,
								ToastOrder.LENGTH_SHORT).show();
					}

					@Override
					public void onFinish() {
						resultDialog.dismiss();
					}

				});
	
	
	}
}
