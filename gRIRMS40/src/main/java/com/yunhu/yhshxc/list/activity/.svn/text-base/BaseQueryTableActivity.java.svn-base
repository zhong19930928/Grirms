package com.yunhu.yhshxc.list.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.Module;

abstract public class BaseQueryTableActivity extends Activity {
	/**
	 * 数据ID
	 */
	protected int targetId;
	/**
	 * 自定义模块中二级菜单item类型
	 * 参考Constants.MODULE_TYPE_*常量组
	 */
	protected int modType;

	/**
	 * 菜单类型
	 * 参考Menu.TYPE_*
	 */
	protected int menuType;

	/**
	 * Bundle
	 */
	protected Bundle bundle;

	/**
	 * @see Module
	 */
	protected Module module = null;

	/**
	 * 是否需要搜索
	 */
	protected boolean isNeedSearch = true;
	protected boolean isSqlLink = false;
	
	protected int tableHeaderHeight;
	protected int tableItemHeight;
	
	protected final List<GroupItemData> data = new ArrayList<GroupItemData>();
	
	protected ExpandableListView expListView;
	protected BaseExpandableListAdapterImpl adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		tableHeaderHeight = getResources().getDimensionPixelSize(R.dimen.table_header_height);
		tableItemHeight = getResources().getDimensionPixelSize(R.dimen.table_row_height);
		
		initParams();
		initViews();
	}

	protected void initParams() {
		// 获取传进来的数据
		bundle = getIntent().getBundleExtra("bundle");
		if (bundle != null && !bundle.isEmpty()) {
			targetId = bundle.getInt("targetId");
			menuType = bundle.getInt("menuType");
			module = (Module) bundle.getSerializable("module");
			if (module != null) {
				modType = module.getType();
			}
			else {
				modType = bundle.getInt("modType");
			}
		}
	}
	
	abstract protected void initViews();
	
	private class BaseExpandableListAdapterImpl extends BaseExpandableListAdapter {

		@Override
		public int getGroupCount() {
			
			return 0;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
		
			return 0;
		}

		@Override
		public Object getGroup(int groupPosition) {

			return null;
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			
			return null;
		}

		@Override
		public long getGroupId(int groupPosition) {
			
			return 0;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			
			return 0;
		}

		@Override
		public boolean hasStableIds() {
			
			return false;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			
			return null;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
			CellData cd = data.get(groupPosition).cells[childPosition];
			
			return null;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			
			return false;
		}
	}
	
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		CellData cd = data.get(groupPosition).cells[childPosition];
		
		TextView txt = null;
		if (convertView == null) {
			txt = new TextView(this);
			txt.setGravity(Gravity.CENTER);
		}
		else {
			
		}
		return null;
	}
	
	protected class GroupItemData {
		CellData[] cells;
	}
	
	protected class CellData {
		Func func;
		String label;
		int width;
		
		protected CellData(Func func, String label, int width) {
			this.func = func;
			this.label = label;
			this.width = width;
		}
	}

	@Override
	protected void onDestroy() {
		// 关闭加载对话框
//		if (dialog != null && dialog.isShowing()) {
//			dialog.dismiss();
//		}
		super.onDestroy();
	}
}
