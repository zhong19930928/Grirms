package com.yunhu.yhshxc.activity.synchrodata;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.utility.PublicUtils;

public class SyncDataActivity extends AbsBaseActivity {

	private PinnedHeaderExpandableListView elistview;
	private PinnedHeaderExpandableAdapter adapter;
	private SyncDataUtil syncDataUtil;

	private List<List<SyncItem>> childrenData = new ArrayList<List<SyncItem>>();
	private List<String> groupData = new ArrayList<String>();
	private List<SyncItem> list1 = new ArrayList<SyncItem>();
	private List<SyncItem> menuConfigSyncDataItem;
	private List<SyncItem> menuDataSyncDataItem;
	private int expandFlag = -1;// 控制列表的展开

	TextView tv_sync_all;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.syncdata_view);
		
		
		initData();
		this.elistview = (PinnedHeaderExpandableListView) super
				.findViewById(R.id.elistview); // 取得组件
		
		// 设置悬浮头部VIEW
		this.elistview.setHeaderView(getLayoutInflater().inflate(
				R.layout.syncdata_item_view_group_head, elistview, false));
		this.adapter = new PinnedHeaderExpandableAdapter(childrenData,
				groupData, this, elistview); // 实例化适配器
		this.elistview.setAdapter(this.adapter); // 设置适配器

		tv_sync_all = (TextView) findViewById(R.id.tv_sync_all);
		tv_sync_all.setOnClickListener(listener);
	}

	class GroupClickListener implements OnGroupClickListener {

		@Override
		public boolean onGroupClick(ExpandableListView parent, View v,
				int groupPosition, long id) {
			// TODO Auto-generated method stub
			if (expandFlag == 1) {
				// 展开被选的group
				elistview.expandGroup(groupPosition);
				// 设置被选中的group置于顶端
				elistview.setSelectedGroup(groupPosition);
				expandFlag = groupPosition;
			} else if (expandFlag == groupPosition) {
				elistview.collapseGroup(expandFlag);
				expandFlag = -1;
			} else {
				elistview.collapseGroup(expandFlag);
				// 展开被选的group
				elistview.expandGroup(groupPosition);
				// 设置被选中的group置于顶端
				elistview.setSelectedGroup(groupPosition);
				expandFlag = groupPosition;
			}
			return true;
		}

	}

	private void initData() {

		groupData.add(PublicUtils.getResourceString(this,R.string.visit_task));
		groupData.add(PublicUtils.getResourceString(this,R.string.visit_task1));
		groupData.add(PublicUtils.getResourceString(this,R.string.visit_data));
		syncDataUtil = new SyncDataUtil(this);
		menuConfigSyncDataItem = syncDataUtil.menuConfigSyncDataItem();
		menuDataSyncDataItem = syncDataUtil.menuDataSyncDataItem();

		childrenData.add(list1);
		childrenData.add(menuConfigSyncDataItem);
		childrenData.add(menuDataSyncDataItem);
	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_sync_all:
				new SyncInitData(SyncDataActivity.this).syncAll();
				break;
			default:
				break;
			}
		}
	};

}
