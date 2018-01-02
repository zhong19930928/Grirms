package com.yunhu.yhshxc.wechat.exchange;


import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yunhu.yhshxc.wechat.bo.GroupUser;

/**
 * 
 * @author jishen 任务列表适配器
 */
public class WechatGrridViewAdapter extends BaseAdapter {
	private Context context;
	private List<GroupUser> groupUserList;
	private int currentPos;
	private int moduleId;

	public void setModuleId(int moduleId) {
		this.moduleId = moduleId;
	}

	public WechatGrridViewAdapter(Context context, List<GroupUser> groupUserList) {
		this.context = context;
		this.groupUserList = groupUserList;
	}

	public int getCount() {
		return groupUserList.size();
	}

	public Object getItem(int pos) {
		return 0;
	}

	public long getItemId(int pos) {
		return pos;
	}

	public View getView(final int pos, View v, ViewGroup p) {
		
		GroupUser groupUser = groupUserList.get(pos);
		ExchangePhotoView exchangePhotoView = null;
		if (v == null) {
			exchangePhotoView = new ExchangePhotoView(context);
			v = exchangePhotoView.getView();
			v.setTag(exchangePhotoView);
		}else{
			exchangePhotoView = (ExchangePhotoView) v.getTag();
		}
		exchangePhotoView.setExchangePhotoView(groupUser);
		return v;
	}


}
