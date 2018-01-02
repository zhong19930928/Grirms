package com.yunhu.yhshxc.activity.questionnaire;

import java.util.ArrayList;
import java.util.List;

import com.yunhu.yhshxc.activity.questionnaire.bo.Question;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class QuestionContentItemAdapter extends BaseAdapter{
	
	private Context mContext = null;
	private List<Question> questionList  = new ArrayList<Question>();
	private QuestionnaireContentActivity questionnaireContentActivity;
	private int pId;
	private int rId;
	private boolean isPreview;
	private boolean isClearOPtions;
	public QuestionContentItemAdapter(Context mContext, List<Question> questionList,boolean isPreview,boolean isClearOPtions) {
		this.mContext = mContext;
		this.questionList = questionList;
		this.isPreview = isPreview;
		this.isClearOPtions=isClearOPtions;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return questionList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return questionList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	@SuppressWarnings("unused")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Question question = questionList.get(position);
		QuestionContentItemView questionContentItemView = null;
		if(questionContentItemView ==null){
			questionContentItemView = new QuestionContentItemView(mContext,isPreview);
			convertView = questionContentItemView.getView();
			convertView.setTag(questionContentItemView);
		}else{
			questionContentItemView = (QuestionContentItemView) convertView.getTag();
		}
		if(question!=null){
				questionContentItemView.setQActivity3(questionnaireContentActivity);
				questionContentItemView.setId(question.getQuestionId(), rId);
				questionContentItemView.setQuestionContentItemView(question,isClearOPtions);
			
		}
		
		return convertView;
	}
	
//	public QuestionContentItemView getViews(){
//		return questionContentItemView;
//		
//	}
	
	public void setId(int pId, int rId){
		this.pId = pId;
		this.rId = rId;
	}

	
	public void setQActivity2(Context context){
		questionnaireContentActivity = (QuestionnaireContentActivity)context;
	}
	
}
