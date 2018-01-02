package com.yunhu.yhshxc.activity.questionnaire;

import java.util.ArrayList;
import java.util.List;

import com.yunhu.yhshxc.activity.questionnaire.bo.Questionnaire;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class QuestionAdapter extends BaseAdapter{
	
	private Context mContext = null;
	private List<Questionnaire> questionList = new ArrayList<Questionnaire>();
	
	public QuestionAdapter(Context mContext, List<Questionnaire> questionList) {
		this.mContext = mContext;
		this.questionList = questionList;
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return questionList.size();
	}

	@Override
	public Questionnaire getItem(int position) {
		// TODO Auto-generated method stub
		return questionList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		Questionnaire questionnaire = questionList.get(position);
		QuestionView questionView = new QuestionView(mContext);
		questionView.setQuestion(questionnaire);
		convertView = questionView.getView();
		return convertView;
	}

	public void refresh(List<Questionnaire> questionList) {
		this.questionList = questionList;
		notifyDataSetChanged();
	}
	
}
