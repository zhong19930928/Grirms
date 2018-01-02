package com.yunhu.yhshxc.activity.questionnaire;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.questionnaire.bo.Findings;

public class QuestionResultView {
	
	private View view;
	private Context context;
	private Findings findings;
	
	
	private TextView tv_question_item_title;
	private TextView tv_question_item_time;
	private TextView tv_question_item_explain;
	
	public QuestionResultView(Context context) {
		this.context = context;
		view = View.inflate(context, R.layout.actiity_question_item, null);
		
		tv_question_item_title = (TextView) view.findViewById(R.id.tv_question_item_title);
		tv_question_item_time = (TextView) view.findViewById(R.id.tv_question_item_time);
		tv_question_item_explain = (TextView) view.findViewById(R.id.tv_question_item_explain);

	}
	
	/**
	 * 设置调差问卷信息
	 */
	public void setQuestion(Findings findings){
		this.findings = findings;
		
		if(!TextUtils.isEmpty(findings.getAdress())){
			tv_question_item_title.setText(findings.getAdress());
		}
		if(!TextUtils.isEmpty(findings.getEndDate())){
			tv_question_item_time.setText(findings.getEndDate());
		}
		
	}
	
	public View getView() {
		return view;
	}

}
