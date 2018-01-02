package com.yunhu.yhshxc.activity.onlineExamination.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.onlineExamination.bo.ExamQuestion;

public class ExamContentItemView {
	private View view;
	private Context context;
	private TextView tv_conline_content_item_question;
	private TextView tv_score_item;
	private LinearLayout ll_online_add_item_view;
	private ExamQuestion question;
	
	private List<OnlineExamContentItemRadioButtonView> radioViews = new ArrayList<OnlineExamContentItemRadioButtonView>();//放单选View的list
	private List<OnlineExamContentItemMoreButtonView> moreViews = new ArrayList<OnlineExamContentItemMoreButtonView>();//放多选View的list
	public ExamContentItemView(Context context){
		this.context = context;
		view = View.inflate(context, R.layout.online_exam_content_item_view_one, null);
		tv_conline_content_item_question = (TextView) view.findViewById(R.id.tv_conline_content_item_question);
		tv_score_item = (TextView) view.findViewById(R.id.tv_score_item);
		ll_online_add_item_view = (LinearLayout) view.findViewById(R.id.ll_online_add_item_view);
	}
	
	private void init() {
		for(int i = 0; i<radioViews.size(); i++){
			
		}
	}

	public View getView(){
		return view;
	}
	public void setQuestion(ExamQuestion question){
		this.question = question;
		//问题区分 1单选 2单选其他 3多选 4多选其他 5填空
		ll_online_add_item_view.removeAllViews();
				if(question.getQuestionsDif() == 1 ){
			    //1单选
					for(int i= 0;i < 3;i ++){
						final int position = i;
						final OnlineExamContentItemRadioButtonView questionContentItemRadioButtonView = new OnlineExamContentItemRadioButtonView(context);
						questionContentItemRadioButtonView.setQuestion(question);
						ll_online_add_item_view.addView(questionContentItemRadioButtonView.getView());
						radioViews.add(questionContentItemRadioButtonView);
						questionContentItemRadioButtonView.getLinearLayout().setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								if(questionContentItemRadioButtonView.getSelected()){
									questionContentItemRadioButtonView.setSelected(false);
									questionContentItemRadioButtonView.getImageView().setBackgroundResource(R.drawable.pic_question_radio);
								}else{
									questionContentItemRadioButtonView.setSelected(true);
									questionContentItemRadioButtonView.getImageView().setBackgroundResource(R.drawable.pic_question_radio_selected);
									for(int j = 0; j<3;j++){
										if(j != position){
											radioViews.get(j).setSelected(false);
											radioViews.get(j).getImageView().setBackgroundResource(R.drawable.pic_question_radio);
										}
									}
								}
							}
						});
					}
				}else if(question.getQuestionsDif() == 2){
				//2单选其他
				}else if(question.getQuestionsDif() == 3){
				//3多选
					for(int i= 0;i < 3;i ++){
						OnlineExamContentItemMoreButtonView questionContentItemMoreButtonView = new OnlineExamContentItemMoreButtonView(context);
						questionContentItemMoreButtonView.setQuestion(question);
						ll_online_add_item_view.addView(questionContentItemMoreButtonView.getView());
						moreViews.add(questionContentItemMoreButtonView);
					}
				}else if(question.getQuestionsDif() == 4){
				//4多选其他
					
				}else if(question.getQuestionsDif() == 5){
				//5填空
					
				}
		      
				
				

				
				//输入框
//					QuestionContentItemEditTextView questionContentItemEditTextView = new QuestionContentItemEditTextView(context);
//					questionContentItemEditTextView.setQuestionContentItemRadioButtonView(question);
////					ll_add_item_view.addView(questionContentItemEditTextView.getView());
////					questionContentItemEditTextViews.add(questionContentItemEditTextView);

	}
}
