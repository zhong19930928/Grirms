package com.yunhu.yhshxc.activity.onlineExamination.view;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;

public class OnlinePreviewItemView {
	private View view;
	private Context context;
	private TextView tv_online_preview_title;
	private LinearLayout ll_add_online_question_preview_item;
	public OnlinePreviewItemView(Context context){
		this.context = context;
		view = View.inflate(context, R.layout.online_question_preview_item_view, null);
		ll_add_online_question_preview_item = (LinearLayout) view.findViewById(R.id.ll_add_online_question_preview_item);
		tv_online_preview_title = (TextView) view.findViewById(R.id.tv_online_preview_title);
	}
	public View getView(){
		return view;
	}
	
	public void setPreviewView(){
		for(int i = 0; i<3; i++){
			OnlineExamPreviewItemSeledtedView previewView = new OnlineExamPreviewItemSeledtedView(context);
			ll_add_online_question_preview_item.addView(previewView.getView());
		}
	}
}
