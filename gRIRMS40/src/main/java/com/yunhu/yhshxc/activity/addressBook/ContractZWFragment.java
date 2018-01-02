package com.yunhu.yhshxc.activity.addressBook;


import java.util.ArrayList;
import java.util.List;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.addressBook.PinnedHeaderExpandableListView.OnHeaderUpdateListener;
import com.yunhu.yhshxc.activity.addressBook.bo.AdressBookUser;
import com.yunhu.yhshxc.activity.addressBook.db.AdressBookUserDB;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.AbsListView.LayoutParams;
import android.widget.TextView;

public class ContractZWFragment extends Fragment implements
		ExpandableListView.OnChildClickListener,
		ExpandableListView.OnGroupClickListener, OnHeaderUpdateListener {
	private AdressBookUserDB userDB;
	private PinnedHeaderExpandableListView elistview;
	private MyexpandableListAdapter pinnAdapter;
	private List<List<AdressBookUser>> childrenData = new ArrayList<List<AdressBookUser>>();
	private List<AdressBookUser> groupData = new ArrayList<AdressBookUser>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.address_book_zhiwei, container,
				false);
		userDB = new AdressBookUserDB(getActivity());
		initData();
		initView(view);
		elistview.setOnHeaderUpdateListener(this);
		elistview.setOnChildClickListener(this);
		elistview.setOnGroupClickListener(this);
		return view;
	}

	private void initData() {
		List<AdressBookUser> listA = userDB.findAllRoleList();// 所有角色
		groupData.clear();
		childrenData.clear();
		for (int i = 0; i < listA.size(); i++) {
			AdressBookUser user = listA.get(i);
			if (user != null) {
				groupData.add(user);
				List<AdressBookUser> listU = userDB.findUserByRoleId(user
						.getrId());
				childrenData.add(listU);
			}
		}
	}

	private void initView(View view) {
		this.elistview = (PinnedHeaderExpandableListView) view
				.findViewById(R.id.elistview); // 取得组件
		// 设置悬浮头部VIEW
		pinnAdapter = new MyexpandableListAdapter(getActivity());
		this.elistview.setAdapter(this.pinnAdapter); // 设置适配器
	}

	/***
	 * 数据源
	 * 
	 * @author Administrator
	 * 
	 */
	class MyexpandableListAdapter extends BaseExpandableListAdapter {
		private Context context;
		private LayoutInflater inflater;

		public MyexpandableListAdapter(Context context) {
			this.context = context;
			inflater = LayoutInflater.from(context);
		}

		// 返回父列表个数
		@Override
		public int getGroupCount() {
			return childrenData.size();
		}

		// 返回子列表个数
		@Override
		public int getChildrenCount(int groupPosition) {
			return childrenData.get(groupPosition).size();
		}

		@Override
		public Object getGroup(int groupPosition) {

			return childrenData.get(groupPosition);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return childrenData.get(groupPosition).get(childPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {

			return true;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			GroupHolder groupHolder = null;
			if (convertView == null) {
				groupHolder = new GroupHolder();
				convertView = inflater.inflate(R.layout.address_group_view,
						null);
				groupHolder.title_name = (TextView) convertView
						.findViewById(R.id.title_name);// 组名
				groupHolder.tv_person_number = (TextView) convertView
						.findViewById(R.id.tv_person_number);// 组人数
				groupHolder.iv = (ImageView) convertView
						.findViewById(R.id.iv_group_address);
				convertView.setTag(groupHolder);
			} else {
				groupHolder = (GroupHolder) convertView.getTag();
			}

			AdressBookUser u = groupData.get(groupPosition);
			groupHolder.title_name.setText(u.getRn());
			int count = userDB.findUserNumberByRoleId(u.getrId(),
					SharedPreferencesUtil.getInstance(context).getUserId());
			groupHolder.tv_person_number.setText(String.valueOf(count) + PublicUtils.getResourceString(context,R.string.address_book_person));
			if (isExpanded) {
				groupHolder.iv.setImageResource(R.drawable.address_up);
				groupHolder.title_name
						.setTextColor(Color.parseColor("#009688"));
			} else {
				groupHolder.iv.setImageResource(R.drawable.address_down);
				groupHolder.title_name
						.setTextColor(Color.parseColor("#000000"));
			}
			return convertView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			ChildHolder childHolder = null;
			if (convertView == null) {
				childHolder = new ChildHolder();
				convertView = inflater.inflate(R.layout.address_view, null);
				childHolder.child_text = (TextView) convertView
						.findViewById(R.id.title);
				childHolder.child_text_num = (TextView) convertView
						.findViewById(R.id.tv_telephone_number);
				convertView.setTag(childHolder);
			} else {
				childHolder = (ChildHolder) convertView.getTag();
			}
			AdressBookUser u = childrenData.get(groupPosition).get(
					childPosition);
			if (u != null) {
				final String syncDataItem = u.getUn();
				childHolder.child_text.setText(syncDataItem);
				childHolder.child_text_num.setText(u.getPn());
			} else {
				childHolder.child_text.setText("");
				childHolder.child_text_num.setText("");
			}

			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
		
	}

	@Override
	public boolean onGroupClick(final ExpandableListView parent, final View v,
			int groupPosition, final long id) {

		return false;
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		AdressBookUser user = childrenData.get(groupPosition).get(childPosition);
		 if(user!=null){
			 Intent intent = new Intent(getActivity(),AddressBookDetailActivity.class);
			 intent.putExtra("userId", user.getuId());
			 startActivity(intent);
		 }
		return false;
	}

	class GroupHolder {
		TextView title_name;
		TextView tv_person_number;
		ImageView iv;
	}

	class ChildHolder {
		TextView child_text;
		TextView child_text_num;
	}

	@Override
	public View getPinnedHeader() {
		View headerView = (ViewGroup) getLayoutInflater(getArguments())
				.inflate(R.layout.address_group_view, null);
		headerView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));

		return headerView;
	}

	@Override
	public void updatePinnedHeader(View headerView, int firstVisibleGroupPos) {
		String groupData = "";
		if(this.groupData.size()>0){
			 groupData = this.groupData.get(firstVisibleGroupPos).getRn();
				int count = userDB.findUserNumberByRoleId(
						this.groupData.get(firstVisibleGroupPos).getrId(),
						SharedPreferencesUtil.getInstance(getActivity()).getUserId());
				TextView tv = (TextView) headerView.findViewById(R.id.title_name);
				
				tv.setText(groupData);
						((TextView) headerView.findViewById(R.id.tv_person_number))
						.setText(count + PublicUtils.getResourceString(getActivity(),R.string.address_book_person));
				ImageView iv = (ImageView) headerView
						.findViewById(R.id.iv_group_address);
				if(elistview.isGroupExpanded(firstVisibleGroupPos)){
					tv.setTextColor(Color.parseColor("#009688"));
					iv.setImageResource(R.drawable.address_up);
				}else{
					tv
					.setTextColor(Color.parseColor("#000000"));
					iv.setImageResource(R.drawable.address_down);
				}
		}
			
			
			
	}
	public void updata(){
		initData();
		pinnAdapter.notifyDataSetChanged();
	}

}
