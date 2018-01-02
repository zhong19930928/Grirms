package com.yunhu.yhshxc.activity.addressBook;

import java.util.List;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.addressBook.bo.AdressBookUser;
import com.yunhu.yhshxc.activity.addressBook.db.AdressBookUserDB;
import com.yunhu.yhshxc.activity.synchrodata.PinnedHeaderExpandableListView;
import com.yunhu.yhshxc.activity.synchrodata.PinnedHeaderExpandableListView.HeaderAdapter;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AddressPinnedHeaderExpandableAdapter extends
		BaseExpandableListAdapter implements HeaderAdapter{
	private List<List<AdressBookUser>> childrenData;
	private List<AdressBookUser> groupData;
	private Context context;
	private PinnedHeaderExpandableListView listView;
	private LayoutInflater inflater;
	private AdressBookUserDB userDB;

	public AddressPinnedHeaderExpandableAdapter(List<List<AdressBookUser>> childrenData,
			List<AdressBookUser> groupData, Context context,
			PinnedHeaderExpandableListView listView) {
		this.context = context;
		this.groupData = groupData;
		this.childrenData = childrenData;
		this.context = context;
		this.listView = listView;
		userDB = new AdressBookUserDB(context);
		inflater = LayoutInflater.from(this.context);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return childrenData.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView != null) {
			view = convertView;
		} else {
			view = createChildrenView();
		}
		TextView child_text = (TextView) view.findViewById(R.id.title);
		TextView child_text_num = (TextView) view.findViewById(R.id.tv_telephone_number);
		AdressBookUser u = childrenData.get(groupPosition).get(childPosition);
		if(u!=null){
			final String syncDataItem = u.getUn();
			child_text.setText(syncDataItem);
			child_text_num.setText(u.getPn());
		}else{
			child_text.setText("");
			child_text_num.setText("");
		}
		return view;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return childrenData.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groupData.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return groupData.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		View view = null;
		if (convertView != null) {
			view = convertView;
		} else {
			view = createGroupView();
		}

		TextView title_name = (TextView) view.findViewById(R.id.title_name);//组名
		TextView tv_person_number = (TextView) view.findViewById(R.id.tv_person_number);//组人数
		AdressBookUser u = groupData.get(groupPosition);
		title_name.setText(u.getRn());
		int count = userDB.findUserNumberByRoleId(u.getrId(),SharedPreferencesUtil.getInstance(context).getUserId());
		tv_person_number.setText(String.valueOf(count)+PublicUtils.getResourceString(context,R.string.address_book_person));
		ImageView iv = (ImageView)view.findViewById(R.id.iv_group_address);
		if (isExpanded) {
			iv.setImageResource(R.drawable.address_up);
			title_name.setTextColor(Color.parseColor("#009688"));
		}
		else{
			iv.setImageResource(R.drawable.address_down);
			title_name.setTextColor(Color.parseColor("#000000"));
		}
		return view;
	}
	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	private View createChildrenView() {
		return inflater.inflate(R.layout.address_view, null);
	}

	private View createGroupView() {
		return inflater.inflate(R.layout.address_group_view, null);
	}

	@Override
	public int getHeaderState(int groupPosition, int childPosition) {
		final int childCount = getChildrenCount(groupPosition);
		
		if (childCount == 0) {//如果没有子view就不设置头VIEW
			return PINNED_HEADER_GONE;
		}
		
		if (childPosition == childCount - 1) {
			return PINNED_HEADER_PUSHED_UP;
		} else if (childPosition == -1
				&& !listView.isGroupExpanded(groupPosition)) {
			return PINNED_HEADER_GONE;
		} else {
			return PINNED_HEADER_VISIBLE;
		}
	}

	@Override
	public void configureHeader(View header, int groupPosition,
			int childPosition, int alpha) {
		String groupData = this.groupData.get(groupPosition).getRn();
		int count = userDB.findUserNumberByRoleId(this.groupData.get(groupPosition).getrId(),SharedPreferencesUtil.getInstance(context).getUserId());
		((TextView) header.findViewById(R.id.title_name)).setText(groupData);
		((TextView) header.findViewById(R.id.tv_person_number)).setText(count+ PublicUtils.getResourceString(context,R.string.address_book_person));

	}

	private SparseIntArray groupStatusMap = new SparseIntArray();

	@Override
	public void setGroupClickStatus(int groupPosition, int status) {
		groupStatusMap.put(groupPosition, status);
	}

	@Override
	public int getGroupClickStatus(int groupPosition) {
		if (groupStatusMap.keyAt(groupPosition) >= 0) {
			return groupStatusMap.get(groupPosition);
		} else {
			return 0;
		}
	}
	

}
