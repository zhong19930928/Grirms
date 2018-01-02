package com.yunhu.yhshxc.wechat.exchange;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yunhu.yhshxc.wechat.bo.GroupUser;

public class GroupUserAdapter extends BaseAdapter {

	private Context mContext = null;
	private List<GroupUser> groupList;


	public GroupUserAdapter(Context mContext, List<GroupUser> groupList) {
		this.mContext = mContext;
		this.groupList = groupList;
	}

	/**
	 * 获取菜单项总数，即菜单项List的总数
	 * 
	 * @return 返回菜单项总数
	 */
	@Override
	public int getCount() {
		return groupList.size();
	}

	/**
	 * 获取指定位置的菜单项数据
	 * 
	 * @param position
	 *            菜单项在List中的位置
	 * @return 返回指定位置的菜单项数据
	 */
	@Override
	public GroupUser getItem(int position) {
		return groupList.get(position);
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
		GroupUser groupUser = groupList.get(position);
		GroupUserView groupUserView = new GroupUserView(mContext);
		groupUserView.setGroupUserView(groupUser);
		convertView = groupUserView.getView();
		return convertView;
	}

}
