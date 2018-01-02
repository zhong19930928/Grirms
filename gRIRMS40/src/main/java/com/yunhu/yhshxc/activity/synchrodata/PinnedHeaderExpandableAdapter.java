package com.yunhu.yhshxc.activity.synchrodata;

import java.util.List;

import android.content.Context;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.synchrodata.PinnedHeaderExpandableListView.HeaderAdapter;

public class PinnedHeaderExpandableAdapter extends BaseExpandableListAdapter
		implements HeaderAdapter {
	private List<List<SyncItem>> childrenData;
	private List<String> groupData;
	private Context context;
	private PinnedHeaderExpandableListView listView;
	private LayoutInflater inflater;

	public PinnedHeaderExpandableAdapter(List<List<SyncItem>> childrenData,
			List<String> groupData, Context context,
			PinnedHeaderExpandableListView listView) {
		this.groupData = groupData;
		this.childrenData = childrenData;
		this.context = context;
		this.listView = listView;
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
		final SyncItem syncDataItem = childrenData.get(groupPosition).get(childPosition);
		TextView child_text = (TextView) view.findViewById(R.id.textView);
		child_text.setText(syncDataItem.getName());
		Button child_btn = (Button) view.findViewById(R.id.id_btn);
		child_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String url = syncDataItem.getSyncUrl();
				new SyncDataItem(context).syncData(url);
			}
		});
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
	

		TextView group_text = (TextView) view.findViewById(R.id.group_textView);
		group_text.setText(groupData.get(groupPosition));
		
		Button group_btn = (Button) view.findViewById(R.id.group_id_btn);
		group_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SyncDataUrl synchroadataUrl = new SyncDataUrl(context);
				if(groupPosition == 0){
					new SyncDataVisit(context).syncData(synchroadataUrl.visitInfo());
				}else if(groupPosition == 1){
					new SyncDataModule(context).syncData(synchroadataUrl.moduleInfo());
				}else{
					SyncDataAll syncDataAll = new SyncDataAll(context);
					syncDataAll.setSyncItemList(childrenData.get(groupPosition));
					syncDataAll.syncData();
				}
				
			}
		});
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
		return inflater.inflate(R.layout.syncdata_item_view, null);
	}

	private View createGroupView() {
		return inflater.inflate(R.layout.syncdata_item_view_group, null);
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
		String groupData = this.groupData.get(groupPosition);
		((TextView) header.findViewById(R.id.group_textView)).setText(groupData);

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
