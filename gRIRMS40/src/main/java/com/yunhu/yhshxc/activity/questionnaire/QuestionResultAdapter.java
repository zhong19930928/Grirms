package com.yunhu.yhshxc.activity.questionnaire;

import java.util.ArrayList;
import java.util.List;

import com.yunhu.yhshxc.activity.questionnaire.bo.Findings;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class QuestionResultAdapter extends BaseAdapter{
	
	private Context mContext = null;
	private List<Findings> findingList = new ArrayList<Findings>();
	
	
	public QuestionResultAdapter(Context mContext, List<Findings> findingList) {
		this.mContext = mContext;
		this.findingList = findingList;
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return findingList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return findingList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		Findings findings = findingList.get(position);
		QuestionResultView questionView = new QuestionResultView(mContext);
		questionView.setQuestion(findings);
		convertView = questionView.getView();
		
		
		return convertView;
	}

	
	
}
