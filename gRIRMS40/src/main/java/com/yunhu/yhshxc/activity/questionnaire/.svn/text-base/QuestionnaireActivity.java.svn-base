package com.yunhu.yhshxc.activity.questionnaire;

import gcg.org.debug.JLog;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.activity.questionnaire.Util.QuestionnaireUtil;
import com.yunhu.yhshxc.activity.questionnaire.bo.FindIngDetail;
import com.yunhu.yhshxc.activity.questionnaire.bo.Findings;
import com.yunhu.yhshxc.activity.questionnaire.bo.Questionnaire;
import com.yunhu.yhshxc.activity.questionnaire.db.FindIngDetailDB;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.yunhu.android.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.RequestParams;

public class QuestionnaireActivity extends AbsBaseActivity {

	private PullToRefreshListView lv_questionnaire;
	private QuestionAdapter questionAdapter;
	private List<Questionnaire> questionnaireList = new ArrayList<Questionnaire>();
	private List<Questionnaire> questionnaireResultList = new ArrayList<Questionnaire>();
	
	private Button btn_question_my_question;
	private Button btn_question_result;
	private Boolean ifResult = false;// false为调查问卷，true为调查结果
	private Boolean ifHave = false;//是否存在adapter
	private Boolean ifHaveFindsAdapter = false;
	private FindIngDetailDB findIngDetailDB;
	private List<Findings> findingsList = new ArrayList<Findings>();
	private List<FindIngDetail> findIngDetailsList = new ArrayList<FindIngDetail>();
	private FindingsAdapter findingsAdapter;

	private Dialog resultDialog = null;
//	private boolean isResult;

	@Override                                                                                                           
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_questionnaire);
		init();
	}

	private void init() {
		findIngDetailDB = new FindIngDetailDB(this);
		findIngDetailDB.clearFindIngDetail();
		lv_questionnaire = (PullToRefreshListView) findViewById(R.id.lv_questionnaire);
		lv_questionnaire.setPullToRefreshEnabled(false);
		lv_questionnaire.setPullToRefreshOverScrollEnabled(false);
		btn_question_my_question = (Button) findViewById(R.id.btn_question_my_question);
		btn_question_result = (Button) findViewById(R.id.btn_question_result);
		
		btn_question_result.setBackgroundResource(R.drawable.btn_selector_white);
		btn_question_result.setTextColor(Color.parseColor("#4BB8C2"));
		
//		String label = DateUtils.formatDateTime(this.getApplicationContext(),
//				System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
//						| DateUtils.FORMAT_SHOW_DATE
//						| DateUtils.FORMAT_ABBREV_ALL);
//		lv_questionnaire.getLoadingLayoutProxy().setLastUpdatedLabel(label);
//		lv_questionnaire.setMode(Mode.BOTH);
//		lv_questionnaire
//				.setOnRefreshListener(new OnRefreshListener<ListView>() {
//					@Override
//					public void onRefresh(
//							PullToRefreshBase<ListView> refreshView) {
//						// 向下滑动
//						if (refreshView.getCurrentMode() == Mode.PULL_FROM_END) {
//							search();
//							lv_questionnaire.onRefreshComplete();
//						}
//						// 向上加载
//						if (refreshView.getCurrentMode() == Mode.PULL_FROM_START) { 
//							search();
//							lv_questionnaire.onRefreshComplete();
//						}
//
//					}
//				});
		
	
		
	     btn_question_my_question.setOnClickListener(this);
	     btn_question_result.setOnClickListener(this);
		 lv_questionnaire.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
					if(ifResult){
						if(questionnaireResultList.size()>0){
							Intent intent_content = new Intent(QuestionnaireActivity.this, QuestionResultPreviewActivity.class);
							intent_content.putExtra("qId", questionnaireResultList.get(position-1).getQuestionId() + "");
							intent_content.putExtra("Count", questionnaireResultList.get(position-1).getCount());
							startActivity(intent_content);
						}
//						intent_content.putExtra("rId", questionnaireResultList.get(position - 1).getResultId());
						
					}else{
//						if (Integer.parseInt(SharedPreferencesUtil.getInstance(QuestionnaireActivity.this).getFindingNums(questionnaireList.get(position - 1).getQuestionId() + "nums"))>=questionnaireList.get(position - 1).getNumbers()) {//如果已提交份数等于发放份数 则不能跳转进入答题界面
//							Toast.makeText(QuestionnaireActivity.this, "该问卷已答够指定份数,无法继续作答", Toast.LENGTH_SHORT).show();
//						}else{
						Questionnaire question = questionnaireList.get(position-1);
						if(questionnaireList.size()>0){
							
							if(question.getUpCopies()<question.getNumbers()||question.getNumbers()==0){
								
								Intent intent_content = new Intent(QuestionnaireActivity.this, QuestionnaireContentActivity.class);
								intent_content.putExtra("qId", questionnaireList.get(position - 1).getQuestionId() + "");
								startActivity(intent_content);
							}else{
								switch (question.getCycle()) { //1：每天、2：每周、3：每月、4：每季度、5：每半年、6：每年 
								case 1:
									Toast.makeText(QuestionnaireActivity.this, R.string.celling, Toast.LENGTH_SHORT).show();
									break;
								case 2:
									Toast.makeText(QuestionnaireActivity.this, R.string.celling1, Toast.LENGTH_SHORT).show();
									break;
								case 3:
									Toast.makeText(QuestionnaireActivity.this, R.string.celling2, Toast.LENGTH_SHORT).show();
									break;
								case 4:
									Toast.makeText(QuestionnaireActivity.this, R.string.celling3, Toast.LENGTH_SHORT).show();
									break;
								case 5:
									Toast.makeText(QuestionnaireActivity.this, R.string.celling4, Toast.LENGTH_SHORT).show();
									break;
								case 6:
									Toast.makeText(QuestionnaireActivity.this, R.string.celling5, Toast.LENGTH_SHORT).show();
									break;

								default:
									break;
								}
							}
						}
						
//						}
					}
				
				
			}
		});
		getData();

	}

	
	private RequestParams searchParams() {

		RequestParams param = new RequestParams();
		param.put("phoneno",PublicUtils.receivePhoneNO(getApplicationContext()));
//		param.put("phoneno","18910901869");
		JLog.d(TAG, param.toString());
		return param;
	}
	
	/**
	 * 查询调查问卷信息
	 */
	private Dialog dialog = null;
	private void search(){
		dialog = new MyProgressDialog(this,R.style.CustomProgressDialog,PublicUtils.getResourceString(this,R.string.init));
		String url = UrlInfo.doQueryQuestionInfo(this);
		RequestParams param = searchParams();
		GcgHttpClient.getInstance(this.getApplicationContext()).post(url,
				param, new HttpResponseListener() {

					@Override
					public void onSuccess(int statusCode, String content) {
						JLog.d(TAG, "onSuccess:" + content);
						try {
							JSONObject obj = new JSONObject(content);
							String resultCode = obj.getString("resultcode");
							if ("0000".equals(resultCode)) {
								
								QuestionnaireUtil questionnaireUtil = new QuestionnaireUtil(getApplicationContext());
								JSONArray questionArray = new JSONArray();
								JSONArray addressArray = new JSONArray();
								JSONArray problemArray = new JSONArray();
								JSONArray optionArray = new JSONArray();
								questionnaireList.clear();
								
								if(PublicUtils.isValid(obj, "question")){
									questionArray = obj.getJSONArray("question");
									if(!TextUtils.isEmpty(questionArray.toString())){
										questionnaireList = questionnaireUtil.parserQuestionListItem(questionArray);
									}
								}
								
								if(PublicUtils.isValid(obj, "address")){
									addressArray = obj.getJSONArray("address");
									if(!TextUtils.isEmpty(addressArray.toString())){
										questionnaireUtil.parserAddressListItem(addressArray);
									}
								}
								
								if(PublicUtils.isValid(obj, "problem")){
									problemArray = obj.getJSONArray("problem");
									if(!TextUtils.isEmpty(problemArray.toString())){
										questionnaireUtil.parserProblemListItem(problemArray);
									}
								}
								
								
								if(PublicUtils.isValid(obj, "option")){
									optionArray = obj.getJSONArray("option");
									if(!TextUtils.isEmpty(optionArray.toString())){
										questionnaireUtil.parserOptionListItem(optionArray);
									}
								}
								
							
								ifHaveFindsAdapter = false;
								/**
								 * 是否存在adapter
								 */
								if(ifHave){
									questionAdapter.refresh(questionnaireList);
								}else{
									ifHave = true; 
									questionAdapter = new QuestionAdapter(getApplicationContext(), questionnaireList);
									lv_questionnaire.setAdapter(questionAdapter);
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
					public void onStart() {
						dialog.show();
					}
					
					@Override
					public void onFinish() {
						dialog.dismiss();
					}

				});
	
	}
	
	
	private void getData() {
		search();

	}
	
	private void setResultData(){
//		searchResultData();
		searchResultListData();
	}
	
	private void searchResultListData() {
		// TODO Auto-generated method stub
		

		resultDialog = new MyProgressDialog(this,R.style.CustomProgressDialog,PublicUtils.getResourceString(this,R.string.init));
		String url = UrlInfo.doQuestionListInfo(this);
		RequestParams param = searchParams();
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
//								JSONArray findingsArray = new JSONArray();
//								JSONArray findingDetailArray = new JSONArray();
								QuestionnaireUtil questionnaireUtil = new QuestionnaireUtil(getApplicationContext());
								questionnaireResultList.clear();
								if(PublicUtils.isValid(obj, "qa")){								
									questionnaireArray = obj.getJSONArray("qa");
									if(!TextUtils.isEmpty(questionnaireArray.toString())){
										questionnaireResultList = questionnaireUtil.parserQuestionItem(questionnaireArray);
									}
								}
							    ifHave = false;
							    if(ifHaveFindsAdapter){
							    	findingsAdapter.refresh(questionnaireResultList);
							    }else{
							    	findingsAdapter = new FindingsAdapter(getApplicationContext(), questionnaireResultList);
									lv_questionnaire.setAdapter(findingsAdapter);
									ifHaveFindsAdapter = true;
							    }
								
								
								
								
							
								
//								
//								if(PublicUtils.isValid(obj, "qa")){
//									questionnaireArray = obj.getJSONArray("qa");
//									if(!TextUtils.isEmpty(questionnaireArray.toString())){
//										questionnaireList = questionnaireUtil.parserSearchListItem(questionnaireArray);
//									}
//									
//								}
//								
//								for(int i = 0;i < questionnaireList.size();i++){
//									Questionnaire questionnaire = questionnaireList.get(i);
//									JLog.d(questionnaire.getName());
//								}
//								if(PublicUtils.isValid(obj, "ra")){
//									findingsArray = obj.getJSONArray("ra");
//									if(!TextUtils.isEmpty(findingsArray.toString())){
//										findingsList = questionnaireUtil.parserFindingsListItem(findingsArray);
//									}
//								}
//								
//								if(PublicUtils.isValid(obj, "oa")){
//									findingDetailArray = obj.getJSONArray("oa");
//									if(!TextUtils.isEmpty(findingDetailArray.toString())){
//										findIngDetailsList = questionnaireUtil.parserFindIngDetailListItem(findingDetailArray);
//									}
//									
//								}
//								
//							
//	
//							    ifHave = false;
//							    if(ifHaveFindsAdapter){
//							    	findingsAdapter.refresh(findingsList);
//							    }else{
//							    	findingsAdapter = new FindingsAdapter(getApplicationContext(), findingsList);
//									lv_questionnaire.setAdapter(findingsAdapter);
//									ifHaveFindsAdapter = true;
//							    }
//								
////								questionAdapter.refresh(questionnaireList);
//	
//							
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





	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_question_my_question:
			findIngDetailDB.clearFindIngDetail();
			getData();
			//btn_selector_white
			btn_question_my_question.setBackgroundResource(R.drawable.btn_selector_blue1);
			btn_question_my_question.setTextColor(Color.WHITE);
			btn_question_result.setTextColor(Color.parseColor("#4BB8C2"));
			btn_question_result.setBackgroundResource(R.drawable.btn_selector_white);
			ifResult = false;
			break;
		case R.id.btn_question_result:
			setResultData();
			btn_question_result.setTextColor(Color.WHITE);
			
			btn_question_result.setBackgroundResource(R.drawable.btn_selector_blue1);
			btn_question_my_question.setBackgroundResource(R.drawable.btn_selector_white);
			btn_question_my_question.setTextColor(Color.parseColor("#4BB8C2"));
			ifResult = true;
			break;
		default:
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			finish();
			
		}
		
		return true;
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(ifResult){
		}else{
			search();
		}
	}
}
