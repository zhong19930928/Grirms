package com.yunhu.yhshxc.activity.questionnaire;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.activity.questionnaire.bo.Questionnaire;
import com.yunhu.yhshxc.activity.questionnaire.db.QuestionnaireDB;

public class QuestionExplainActivity extends AbsBaseActivity{

	private Button btn_question_explain_back;
	private Questionnaire questionnaire = new Questionnaire();
	private QuestionnaireDB questionnaireDB;
	private TextView tv_explain_title;
	private TextView tv_explain_explain;
	private TextView tv_explain_time;
	private TextView tv_explain_copies1;
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_question_explain);
		questionnaireDB = new QuestionnaireDB(this);
		init();
	}
	
	private void init(){
		Intent intent = getIntent();
		int qId = intent.getIntExtra("qId", 0);
		questionnaire = questionnaireDB.findQuestionnaireById(qId);
		
		btn_question_explain_back = (Button) findViewById(R.id.btn_question_explain_back);
		tv_explain_title = (TextView) findViewById(R.id.tv_explain_title);
		tv_explain_explain = (TextView) findViewById(R.id.tv_explain_explain);
		tv_explain_time = (TextView) findViewById(R.id.tv_explain_time);
		tv_explain_copies1 = (TextView) findViewById(R.id.tv_explain_copies1);
		tv_explain_title.setText(questionnaire.getName());
		tv_explain_explain.setText(questionnaire.getExplain());
		String startTime = "";
		String endTime = "";
		if(!TextUtils.isEmpty(questionnaire.getStartDate())&&questionnaire.getStartDate().length() > 10){
			startTime = questionnaire.getStartDate().substring(0, 10);
		}else{
			startTime = questionnaire.getStartDate();
		}
		if(!TextUtils.isEmpty(questionnaire.getEndDate())&&questionnaire.getEndDate().length() > 10){
			endTime = questionnaire.getEndDate().substring(0,10);
		}else{
			endTime = questionnaire.getEndDate();
		}
		
		tv_explain_time.setText(!TextUtils.isEmpty(startTime)?startTime:"" + " ~ " + endTime != null?endTime:"");
		if(questionnaire.getNumbers()==0){
			tv_explain_copies1.setText("*");
		}else{
			tv_explain_copies1.setText(questionnaire.getNumbers()+"");
		}
		
		btn_question_explain_back.setOnClickListener(this);
	}
	
	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_question_explain_back:
			this.finish();
			break;
	
		default:
			break;
		}

	}
}
