package com.yunhu.yhshxc.activity.questionnaire;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.questionnaire.bo.FindIngDetail;
import com.yunhu.yhshxc.activity.questionnaire.bo.Question;
import com.yunhu.yhshxc.activity.questionnaire.db.FindIngDetailDB;

public class QuestionContentItemEditTextView{
	
	private View view;
	private Context context;
	private Question question;
	private EditText ed_question_edit_content;
	private Boolean ifEdit = false;
	private FindIngDetailDB findIngDetailDB;
	private boolean isPreview;;
	private int rId;


	public QuestionContentItemEditTextView(Context context,boolean isPreview) {
		this.context = context;
		this.isPreview = isPreview;
		findIngDetailDB = new FindIngDetailDB(context);
		view = View.inflate(context, R.layout.activity_question_content_item_edit_text, null);
		ed_question_edit_content = (EditText) view.findViewById(R.id.ed_question_edit_content);
		ed_question_edit_content.setEnabled(isPreview);
	 
	}
	public QuestionContentItemEditTextView(Context context,boolean isPreview,int rId) {
		this.context = context;
		this.isPreview = isPreview;
		this.rId = rId;
		findIngDetailDB = new FindIngDetailDB(context);
		view = View.inflate(context, R.layout.activity_question_content_item_edit_text, null);
		ed_question_edit_content = (EditText) view.findViewById(R.id.ed_question_edit_content);
		ed_question_edit_content.setEnabled(isPreview);
	 
	}
	
	
	public void setQuestionContentItemRadioButtonView(Question question) {
		this.question = question;
		
//		FindIngDetail f = findIngDetailDB.findFindIngDetailByQId(getQId());
		FindIngDetail f = findIngDetailDB.findFindIngDetailByQId(getQId(),rId);
		
		
		
		if(f != null){
			if(!TextUtils.isEmpty(f.getFillOptions())){
				ed_question_edit_content.setText(f.getFillOptions());
			}else{
				ed_question_edit_content.setText("");
				
			}
		}else{
			ed_question_edit_content.setText("");
			
		}
		if(isPreview){
			ed_question_edit_content.setText("");
		}
	}

	public View getView() {
		return view;
	}
	
	public Boolean ifEdit(){
		if(!TextUtils.isEmpty(ed_question_edit_content.getText())){
			ifEdit = true;
		}
		return ifEdit;
	}
	
	public int getQId(){
		return question.getQuestionId();
	}

	public String getContent(){
		return ed_question_edit_content.getText().toString();
	}
}
