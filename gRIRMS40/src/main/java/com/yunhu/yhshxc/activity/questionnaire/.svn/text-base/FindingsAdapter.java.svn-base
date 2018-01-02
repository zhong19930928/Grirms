package com.yunhu.yhshxc.activity.questionnaire;

import java.util.ArrayList;
import java.util.List;

import com.yunhu.yhshxc.activity.questionnaire.bo.Questionnaire;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class FindingsAdapter extends BaseAdapter{
	
	private Context mContext = null;
	private List<Questionnaire> findings = new ArrayList<Questionnaire>();
	
	public FindingsAdapter(Context mContext, List<Questionnaire> findings) {
		this.mContext = mContext;
		this.findings = findings;
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return findings.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return findings.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Questionnaire finding = findings.get(position);

		FindingsView findingsView = null;
		findingsView = new FindingsView(mContext);
		convertView = findingsView.getView();
		convertView.setTag(findingsView);
		if(finding !=null){
			findingsView.setFindingsNew(finding);
		}
		return convertView;
	}

	public void refresh(List<Questionnaire> findingsList) {
		this.findings = findingsList;
		notifyDataSetChanged();
	}
	
}
