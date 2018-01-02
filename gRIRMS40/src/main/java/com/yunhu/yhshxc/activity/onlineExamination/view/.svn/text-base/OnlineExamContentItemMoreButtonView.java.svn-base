package com.yunhu.yhshxc.activity.onlineExamination.view;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.onlineExamination.bo.ExamQuestion;

public class OnlineExamContentItemMoreButtonView {
	private View view;
	private Context  context;
	
	private LinearLayout ll_exam_more;
	private ImageView iv_pic_online_more;
	private TextView tv_exam_more;
	private ExamQuestion question;
	private Boolean isSelected = false;//判断是否被选中
	
	public OnlineExamContentItemMoreButtonView(Context  context){
		this.context = context;
		view = View.inflate(context, R.layout.online_exam_content_item_more_button, null);
		tv_exam_more= (TextView) view.findViewById(R.id.tv_exam_more);
		iv_pic_online_more= (ImageView) view.findViewById(R.id.iv_pic_online_more);
		ll_exam_more = (LinearLayout) view.findViewById(R.id.ll_exam_more);
		ll_exam_more.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isSelected){
					isSelected = false;
					iv_pic_online_more.setBackgroundResource(R.drawable.pic_question_more);
				}else{
					isSelected = true;
					iv_pic_online_more.setBackgroundResource(R.drawable.pic_question_more_selected);
				}
			}
		});
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
		return iv_pic_online_more;
	}
	public LinearLayout getLinearLayout(){
		return ll_exam_more;
	}
}
