package com.yunhu.yhshxc.activity.questionnaire;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.activity.questionnaire.bo.FindIngDetail;
import com.yunhu.yhshxc.activity.questionnaire.bo.Question;
import com.yunhu.yhshxc.activity.questionnaire.bo.Questionnaire;
import com.yunhu.yhshxc.activity.questionnaire.bo.SurveyAdress;
import com.yunhu.yhshxc.activity.questionnaire.db.FindIngDetailDB;
import com.yunhu.yhshxc.activity.questionnaire.db.QuestionDB;
import com.yunhu.yhshxc.activity.questionnaire.db.QuestionnaireDB;
import com.yunhu.yhshxc.activity.questionnaire.db.SurveyAdressDB;
import com.yunhu.yhshxc.utility.PublicUtils;

public class QuestionPreviewActivity extends AbsBaseActivity{
	
	/**
	 * 调查结果预览页面
	 */
	
	private Button btn_question_preview_back;
	private Spinner sp_qusetion_preview;
	private List<String> list = new ArrayList<String>();  
	private ArrayAdapter<String> adapter;    
	private LinearLayout ll_question_preview_item_title;
	private Button btn_preview_to_explain;
	private SurveyAdressDB surveyAdressDB;
	private List<QuestionnaireContentView> questionnaireContentViews = new ArrayList<QuestionnaireContentView>();
	private List<Question> questionList = new ArrayList<Question>();//章节
	private List<Question> problemList = new ArrayList<Question>();//问题
	private List<FindIngDetail> findIngDetailList = new ArrayList<FindIngDetail>();
	private QuestionDB questionDB;
	private int qId;
	private TextView tv_nums; 
	private TextView tv_surplus_nums;
	private FindIngDetailDB findIngDetailDB;
	private String addressSelected = "";
	private int intSelected;
	private QuestionnaireDB questionnaireDB;
	private Button btn_question_preview_submit;
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_question_preview);
		questionnaireDB = new QuestionnaireDB(this);
		init();
	}
	
	private void init(){
		questionDB = new QuestionDB(this);
		surveyAdressDB = new SurveyAdressDB(this);
		findIngDetailDB = new FindIngDetailDB(this);
		btn_question_preview_back = (Button) findViewById(R.id.btn_question_preview_back);
		sp_qusetion_preview = (Spinner) findViewById(R.id.sp_qusetion_preview);
		sp_qusetion_preview.setEnabled(false);
		ll_question_preview_item_title = (LinearLayout) findViewById(R.id.ll_question_preview_item_title);
		btn_preview_to_explain = (Button) findViewById(R.id.btn_preview_to_explain);
		tv_nums = (TextView) findViewById(R.id.tv_nums);
		tv_surplus_nums = (TextView) findViewById(R.id.tv_surplus_nums);

		btn_question_preview_back.setOnClickListener(this);
		btn_preview_to_explain.setOnClickListener(this);
		btn_question_preview_submit= (Button) findViewById(R.id.btn_question_preview_submit);
		btn_question_preview_submit.setVisibility(View.GONE);
		sp_qusetion_preview.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				addressSelected = list.get(position);
				TextView tv = (TextView) view;
				tv.setTextSize(14);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		initData();
		addView();
	}
	
	private void initData(){
		String strs = TextUtils.isEmpty(getIntent().getStringExtra("sId"))?"0":getIntent().getStringExtra("sId");
		 int sId = Integer.parseInt(strs);
		 qId = Integer.parseInt(getIntent().getStringExtra("qId"));
		 addressSelected = getIntent().getStringExtra("addressSelected");
		 SurveyAdress surveyAdress = surveyAdressDB.findSurveyAdressById(sId);
		 if(surveyAdress != null){
			 if(!TextUtils.isEmpty(surveyAdress.getAdress())){
				 String str = surveyAdress.getAdress();
				 String d =  str.replace(";", "；");
				 String[] sStr = d.split("；");
				 for(int i = 0;i < sStr.length; i ++){
					 list.add(sStr[i]);
					 if(sStr[i].equals(addressSelected)){
						 intSelected = i;
					 }
				 }
			 }
		 }

	     adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);    
	     //第三步：为适配器设置下拉列表下拉时的菜单样式。    
	     adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
         //第四步：将适配器添加到下拉列表上    
	     sp_qusetion_preview.setAdapter(adapter); 
	     sp_qusetion_preview.setSelection(intSelected, true);
	}
	
	public void addView(){
    	questionList = questionDB.findQuestionListById(qId,1);//查询章节
    	int size = 0;
    	int qSize = 0;
    	for(int i = 0;i < questionList.size();i++){
    		QuestionnaireContentView questionnaireContentView = new QuestionnaireContentView(this,false); 
    		Question question = questionList.get(i);
    		problemList = questionDB.findQuestionListByCId(qId, 2 ,question.getQuestionId());//查询问题
    		findIngDetailList = findIngDetailDB.findAllFindIngDetail();
    		for(int j = 0; j < problemList.size();j++){
    			for(int f = 0;f < findIngDetailList.size();f++){
    				int aId = problemList.get(j).getQuestionId();
    				int bId = findIngDetailList.get(f).getQuestionId();
    				if(aId == bId){
    					qSize++;
    				}
    			}
    			
    		}
    		size += problemList.size();
    		questionnaireContentView.setQuestion(question);
        	questionnaireContentView.setQuestionContent(problemList,i);
        	ll_question_preview_item_title.addView(questionnaireContentView.getView());
        	questionnaireContentViews.add(questionnaireContentView);
    	}
    	tv_nums.setText(PublicUtils.getResourceString(this,R.string.question_num) + size + PublicUtils.getResourceString(this,R.string.question_ti));
    	int sSize = size - qSize;
    	tv_surplus_nums.setText(sSize + PublicUtils.getResourceString(this,R.string.question_ti));
    	
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
				Toast.makeText(QuestionPreviewActivity.this,R.string.qustion_no1,Toast.LENGTH_LONG).show();
			}
			
			break;
		case R.id.btn_question_preview_submit://预览界面的提交按钮
			
		default:
			break;
		}

	}
}
