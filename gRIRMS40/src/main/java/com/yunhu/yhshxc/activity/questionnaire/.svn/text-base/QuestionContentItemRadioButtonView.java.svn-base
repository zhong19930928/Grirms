package com.yunhu.yhshxc.activity.questionnaire;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.questionnaire.bo.AnswerOptions;
import com.yunhu.yhshxc.activity.questionnaire.bo.FindIngDetail;
import com.yunhu.yhshxc.activity.questionnaire.db.AnswerOptionsDB;
import com.yunhu.yhshxc.activity.questionnaire.db.FindIngDetailDB;
import com.yunhu.yhshxc.application.SoftApplication;
import com.yunhu.yhshxc.utility.PublicUtils;

public class QuestionContentItemRadioButtonView{
	
	private View view;
	private  Context context;
	private AnswerOptions answerOptions;
 	private ImageView rb_question_comment_item;
	private Boolean isSelected = false;//判断是否被选中
	private QuestionContentItemView questionContentItemView;
	private LinearLayout ll_question_comment_item;
	private TextView tv_option_content;
	private EditText ed_option_content;
	private LinearLayout ll_option_content;
	private FindIngDetailDB findIngDetailDB;
	private AnswerOptionsDB answerOptionsDB;
	private QuestionnaireContentActivity questionnaireContentActivity;
	private Boolean ifEdit;
	private TextView tv_option_choice;
	private Boolean isResult = false;
	private boolean isPreview;
	private int qId;
	private List<AnswerOptions> answerOptionsList = new ArrayList<AnswerOptions>();
	

	public QuestionContentItemRadioButtonView(Context context,final QuestionContentItemView questionContentItemView,boolean isPreview) {
		this.context = context;
		this.questionContentItemView = questionContentItemView;
		this.isPreview = isPreview;
		findIngDetailDB = new FindIngDetailDB(context);
		answerOptionsDB = new AnswerOptionsDB(context);
		view = View.inflate(context, R.layout.activity_qusetion_content_item_radio_button, null);
		rb_question_comment_item = (ImageView) view.findViewById(R.id.rb_question_comment_item);
		ll_question_comment_item = (LinearLayout) view.findViewById(R.id.ll_question_comment_item);
		tv_option_content = (TextView) view.findViewById(R.id.tv_option_content);
		ed_option_content = (EditText) view.findViewById(R.id.ed_option_content);
		ll_option_content = (LinearLayout) view.findViewById(R.id.ll_option_content);
		tv_option_choice = (TextView) view.findViewById(R.id.tv_option_choice);
		
		ll_question_comment_item.setEnabled(isPreview);
		ed_option_content.setEnabled(isPreview);
		
		ll_question_comment_item.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isSelected){
					isSelected = false;
					rb_question_comment_item.setBackgroundResource(R.drawable.pic_question_radio);
				}else{
					/**
					 * 将list储存中其他选中项设置为未选中状态
					 */
					for(int i = 0;i < questionContentItemView.questionContentItemRadioButtonViews.size(); i++){
						QuestionContentItemRadioButtonView questionContentItemRadioButtonView = questionContentItemView.questionContentItemRadioButtonViews.get(i);
						if(questionContentItemRadioButtonView.getSelected()){
							questionContentItemRadioButtonView.setSelected(false);
							questionContentItemRadioButtonView.answerOptions.setIsSave(0);
							answerOptionsDB.updateOption(questionContentItemRadioButtonView.answerOptions);
						}
					}
					
					answerOptionsList = answerOptionsDB.findAnswerOptionById(qId, answerOptions.getProblemId());
					for(int i = 0;i < answerOptionsList.size();i++){
						AnswerOptions answerOptions = answerOptionsList.get(i);
						answerOptions.setIsSave(0);
						answerOptionsDB.updateOption(answerOptions);
					}
					isSelected = true;
					rb_question_comment_item.setBackgroundResource(R.drawable.pic_question_radio_selected);
					
					answerOptions.setIsSave(1);
					if(answerOptions.getOptions().equals(PublicUtils.getResourceString(SoftApplication.context,R.string.others))){
						answerOptions.setOptionsRemarks(getContent());
					}
					answerOptionsDB.updateOption(answerOptions);
				}
			}
		});
		
	}
	
	
	public void setQuestionContentItemRadioButtonView( AnswerOptions answerOptions,Boolean ifEdit) {
		this.answerOptions = answerOptions;
		this.ifEdit = ifEdit;
	
		
		FindIngDetail f = findIngDetailDB.findFindIngDetailById(answerOptions.getOptionsId());
	
		if(ifEdit){
			tv_option_content.setVisibility(View.GONE);
			tv_option_choice.setVisibility(View.GONE);
			ll_option_content.setVisibility(View.VISIBLE);
			ed_option_content.setHint(answerOptions.getOptions());
		}else{
			ll_option_content.setVisibility(View.GONE);
			tv_option_content.setVisibility(View.VISIBLE);
			tv_option_content.setText(answerOptions.getOptionsRemarks());
			tv_option_choice.setText(answerOptions.getOptions());
		}
		if(questionnaireContentActivity == null){//数据库中存在并且为预览界面时
			if(f != null){
				isSelected = true;
				rb_question_comment_item.setBackgroundResource(R.drawable.pic_question_radio_selected);
				if(ifEdit){
					ed_option_content.setText(f.getFillOptions());
				}
			}
			
		}
		
	
	
		
	}
	 
	public void setText(String t){
		if(ifEdit){
			ed_option_content.setText(t);
		}
	}
	
	public void setIsResult(){
		isResult = true;
	}
	
	public void setQActivity4(Context context){
		questionnaireContentActivity = (QuestionnaireContentActivity)context;
	}

	public View getView() {
		return view;
	}
	
	public void setSelected(Boolean ifSelected){
		isSelected = ifSelected;
		if(ifSelected){
			rb_question_comment_item.setBackgroundResource(R.drawable.pic_question_radio_selected);
		}else{
			rb_question_comment_item.setBackgroundResource(R.drawable.pic_question_radio);
		}
	}
	
	public void setText(FindIngDetail ff){
		ed_option_content.setText(ff.getFillOptions());
	}
	

	public Boolean getSelected(){
		return isSelected;
	}
	/**
	 * 获得问题Id
	 * @return
	 */
	public int getQId(){
		return answerOptions.getProblemId();
	}
	/**
	 * 获得选项id
	 * @return
	 */
	public int getAId() {
//		return answerOptions.getOptionsId();
		return answerOptions.getOptionsId();
	}
	
	/**
	 * 获取是否为其他 true为是，false为否
	 * @return
	 */
	public Boolean getIfEdit(){
		return ifEdit;
	}
	
	/**
	 * 获得单选其他
	 */
	public String getContent(){
		return ed_option_content.getText().toString();
	}
	
	
}
