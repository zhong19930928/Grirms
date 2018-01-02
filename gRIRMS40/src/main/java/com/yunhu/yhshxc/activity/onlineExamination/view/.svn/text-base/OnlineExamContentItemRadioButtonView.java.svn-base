package com.yunhu.yhshxc.activity.onlineExamination.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.onlineExamination.bo.ExamQuestion;

public class OnlineExamContentItemRadioButtonView {
	private View view;
	private Context  context;
	
	private LinearLayout ll_online_content_exam;
	private ImageView rb_online_exam_comment_item;
	private TextView tv_content_xuanx;
	private ExamQuestion question;
	private Boolean isSelected = false;//判断是否被选中
	
	public OnlineExamContentItemRadioButtonView(Context  context){
		this.context = context;
		view = View.inflate(context, R.layout.online_exam_content_item_radio_button, null);
		tv_content_xuanx= (TextView) view.findViewById(R.id.tv_content_xuanx);
		rb_online_exam_comment_item= (ImageView) view.findViewById(R.id.rb_online_exam_comment_item);
		ll_online_content_exam = (LinearLayout) view.findViewById(R.id.ll_online_content_exam);
//		ll_online_content_exam.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if(isSelected){
//					isSelected = false;
//					rb_online_exam_comment_item.setBackgroundResource(R.drawable.pic_question_radio);
//				}else{
//					isSelected = true;
//					rb_online_exam_comment_item.setBackgroundResource(R.drawable.pic_question_radio_selected);
//				}
//			}
//		});
	}
	public View getView(){
		return view;
	}
	public void setQuestion(ExamQuestion question){
		this.question = question;
	}
	public boolean getSelected(){
		return isSelected;
	}
	public void setSelected(boolean isSelect){
		this.isSelected = isSelect;
	}
	public ImageView getImageView(){
		return rb_online_exam_comment_item;
	}
	public LinearLayout getLinearLayout(){
		return ll_online_content_exam;
	}
}
