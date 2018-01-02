package com.yunhu.yhshxc.activity.onlineExamination.view;

import android.content.Context;
import android.view.View;

import com.yunhu.yhshxc.R;

public class OnlineExamPreviewItemSeledtedView {

	
	private View view;
	private Context context;


	public OnlineExamPreviewItemSeledtedView(Context context) {
		this.context = context;
		view = View.inflate(context, R.layout.online_exam_question_preview_item_view, null);
	
	}
	
	
	public void setQuestionPreviewItemSelectedView(){
		
	}


	public View getView() {
		return view;
	}
}
