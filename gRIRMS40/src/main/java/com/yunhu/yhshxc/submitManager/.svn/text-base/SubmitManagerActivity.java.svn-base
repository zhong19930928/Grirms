package com.yunhu.yhshxc.submitManager;

import gcg.org.debug.JLog;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.database.TablePendingDB;
import com.yunhu.yhshxc.submitManager.bo.TablePending;
import com.yunhu.yhshxc.submitManager.core.SubmitWorkManager;
import com.yunhu.yhshxc.submitManager.view.TablePendingItem;
import com.yunhu.yhshxc.utility.Constants;

public class SubmitManagerActivity extends AbsBaseActivity{

	private ListView lv_submit_manager;
	public TablePendingDB tablePendingDB;
	private List<TablePending> tablePendingList = null;
	private TablePendingAdapter adapter;
	private ImageView iv_submit_manager_all;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.submit_manager_activity);
		tablePendingDB = new TablePendingDB(this);
		lv_submit_manager = (ListView)findViewById(R.id.lv_submit_manager);
		iv_submit_manager_all = (ImageView)findViewById(R.id.iv_submit_manager_all);
		iv_submit_manager_all.setOnClickListener(listener);
		registerReceiver(receive, new IntentFilter(Constants.BROADCAST_SUBMMIT_FINISH));
		refrshTablePendingList();
		
		
		findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
	/**
	 * 刷新列表
	 */
	public void refrshTablePendingList(){
		tablePendingList = tablePending();
		if (adapter == null) {
			adapter = new TablePendingAdapter();
			lv_submit_manager.setAdapter(adapter);
		}
		adapter.notifyDataSetChanged();
	}
	
	public List<TablePending> tablePending(){
		List<TablePending> tablePendingList = tablePendingDB.findAllTablePending();
		return tablePendingList;
	}

	private class TablePendingAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return tablePendingList.size();
		}

		@Override
		public Object getItem(int position) {
			return tablePendingList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
//		@Override
//		public boolean areAllItemsEnabled() {
//			return false;
//		}
		
		@Override
		public boolean isEnabled(int position) {
			return false;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TablePending tablePending = tablePendingList.get(position);
			TablePendingItem item = null;
			if (convertView == null) {
				item = new TablePendingItem(SubmitManagerActivity.this);
				convertView = item.getView();
				convertView.setTag(item);
			}else{
				item = (TablePendingItem) convertView.getTag();
			}
			item.setData(tablePending);
			return convertView;
		}
		
	}
	
	private BroadcastReceiver receive = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			JLog.d(TAG, "刷新列表");
			refrshTablePendingList();
		}
	};
	
	/**
	 * 提交所有
	 */
	protected void submitAll(){
		List<TablePending> failTablePendingList = tablePendingDB.findAllFailTablePending();
		if (!failTablePendingList.isEmpty()) {
			JLog.d("----------------提交所有未提交数据--------------");
			SubmitWorkManager.getInstance(this).updatePendingInfoByFail();
			SubmitWorkManager.getInstance(this).commit();
			refrshTablePendingList();
		}
	}
	
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.iv_submit_manager_all:
				submitAll();
				break;

			default:
				break;
			}
		}
	};
	
	
	
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receive);
	};
	
}
