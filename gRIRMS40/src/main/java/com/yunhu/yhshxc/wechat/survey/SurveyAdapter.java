package com.yunhu.yhshxc.wechat.survey;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yunhu.yhshxc.wechat.bo.Reply;

public class SurveyAdapter extends BaseAdapter {

	private Context mContext = null;
	/**
	 * 所有菜单项数据的引用，这个List是从HomeMenuActivity传进来的
	 */
	private List<Reply> replyList = new ArrayList<Reply>();

	public SurveyAdapter(Context mContext, List<Reply> replyList) {
		this.mContext = mContext;
		this.replyList = replyList;
	}

	/**
	 * 获取菜单项总数，即菜单项List的总数
	 * 
	 * @return 返回菜单项总数
	 */
	@Override
	public int getCount() {
		return replyList.size();
	}

	/**
	 * 获取指定位置的菜单项数据
	 * 
	 * @param position
	 *            菜单项在List中的位置
	 * @return 返回指定位置的菜单项数据
	 */
	@Override
	public Object getItem(int position) {
		return replyList.get(position);
	}

	/**
	 * 获取指定位置菜单项的id，通常情况下这个ID就是指定位置本身
	 * 
	 * @param position
	 *            在菜单项List中的位置
	 * @return 直接返回position值
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 系统接口，系统会自己调用
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Reply reply = new Reply();
		reply = replyList.get(position);
		SurveyView surveyView = new SurveyView(mContext);
		surveyView.setSurveyReply(reply);
		convertView = surveyView.getView();
		return convertView;
	}
	
	public void setReplyList(List<Reply> replyList) {
		this.replyList = replyList;
	}

	public void refresh(List<Reply> replyList) {
		this.replyList = replyList;
		notifyDataSetChanged();

	}
}
