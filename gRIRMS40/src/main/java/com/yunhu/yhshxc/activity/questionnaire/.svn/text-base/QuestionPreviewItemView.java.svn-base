package com.yunhu.yhshxc.activity.questionnaire;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.yunhu.yhshxc.R;

public class QuestionPreviewItemView {

	
	private View view;
	private Context context;
	private LinearLayout ll_add_question_preview_item;


	public QuestionPreviewItemView(Context context) {
		this.context = context;
		view = View.inflate(context, R.layout.activity_question_preview_item_view, null);
		ll_add_question_preview_item = (LinearLayout) view.findViewById(R.id.ll_add_question_preview_item);
	}
	
	
	public void setQuestionPreviewItemView(){
		QuestionPreviewItemSeledtedView questionPreviewItemSeledtedView = new QuestionPreviewItemSeledtedView(context);
		ll_add_question_preview_item.addView(questionPreviewItemSeledtedView.getView());
		
		QuestionPreviewItemSelectView questionPreviewItemSelectView = new QuestionPreviewItemSelectView(context);
		ll_add_question_preview_item.addView(questionPreviewItemSelectView.getView());
		
	}


	public View getView() {
		return view;
	}





}
