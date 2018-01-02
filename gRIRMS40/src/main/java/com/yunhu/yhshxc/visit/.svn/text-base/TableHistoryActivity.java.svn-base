package com.yunhu.yhshxc.visit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.ReplenishSearchResult;
import com.yunhu.yhshxc.bo.Submit;
import com.yunhu.yhshxc.bo.SubmitItem;
import com.yunhu.yhshxc.comp.CompDialog;
import com.yunhu.yhshxc.database.SubmitDB;
import com.yunhu.yhshxc.database.SubmitItemDB;
import com.yunhu.yhshxc.database.VisitFuncDB;
import com.yunhu.yhshxc.list.activity.AbsSearchListActivity;
import com.yunhu.yhshxc.list.activity.ShowTableActivity;
import com.yunhu.yhshxc.parser.ReplenishParse;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;

public class TableHistoryActivity extends AbsSearchListActivity {

	/**
	 * 历史信息查询表格控件的控件ID
	 */
	private int funcId;
	/**
	 * 店面ID
	 */
	private int storeId;
	private Func func;
	private int submitId;
	private int menuId;
	private String menuName;
	private String menuType2;//只针对拜访中的自由访店

	@Override
	protected void init() {
		super.init();
		ImageView imgQuery = (ImageView) findViewById(R.id.img_query);
		ViewGroup queryIconContainer = (ViewGroup) imgQuery.getParent();
		queryIconContainer.setVisibility(View.GONE);
		queryIconContainer.setOnClickListener(null);
	}

	@Override
	public void initBase() {
		//
		super.initBase();
		addPaging();
	}

	@Override
	protected void initTitle(List<Func> viewCoum, LinearLayout ll_title) {
		TextView tv_title_1 = (TextView) View.inflate(this, R.layout.table_list_item_unit, null);
		tv_title_1.setLayoutParams(
				new LayoutParams(PublicUtils.convertDIP2PX(TableHistoryActivity.this, 120), LayoutParams.WRAP_CONTENT));
		tv_title_1.setText(R.string.date);
		tv_title_1.setTextColor(Color.rgb(255, 255, 255));
		tv_title_1.setTextSize(18);
		ll_title.addView(tv_title_1);

		TextView tv_title_2 = (TextView) View.inflate(this, R.layout.table_list_item_unit, null);
		tv_title_2.setLayoutParams(
				new LayoutParams(PublicUtils.convertDIP2PX(TableHistoryActivity.this, 180), LayoutParams.WRAP_CONTENT));

		tv_title_2.setText(R.string.visit_store10);
		tv_title_2.setTextColor(Color.rgb(255, 255, 255));
		tv_title_2.setTextSize(18);
		ll_title.addView(tv_title_2);

		if (bundle != null && !bundle.isEmpty()) {
			funcId = bundle.getInt("funcId");
			storeId = bundle.getInt("storeId");
			menuId = bundle.getInt("menuId");
			menuName = bundle.getString("menuName");
			menuType2 = bundle.getString("menuType2");
			func = new VisitFuncDB(TableHistoryActivity.this).findFuncByFuncId(funcId);
		}
		menuType = 3;
	}

	@Override
	protected List getDataListOnThread(String json) throws Exception {
		// 解析JSON
		ReplenishSearchResult searchResult = new ReplenishParse().parseStoreTableResult(json);
		// 表格数据
		List<Map<String, String>> resultList = searchResult.getResultList();
		return resultList;
	}

	@Override
	protected String getSearchUrl() {
		return UrlInfo.getUrlTableOldDataInfo(TableHistoryActivity.this);
	}

	@Override
	protected HashMap<String, String> getSearchParams() {
		HashMap<String, String> searchParams = new HashMap<String, String>();
		searchParams.put(Constants.TASK_ID, String.valueOf(targetId));
		searchParams.put("ctrlid", String.valueOf(funcId));
		searchParams.put("storeid", String.valueOf(storeId));
		return searchParams;
	}

	@Override
	protected int getCurChildrenCount(int groupPosition) {
		if (func != null && func.getIsSearchModify() != null) {
			if (func.getIsSearchModify() == 2) {
				return 1;
			} else if (func.getIsSearchModify() == 1) {
				return 0;
			} else if (func.getIsSearchModify() == 0) {
				ToastOrder.makeText(TableHistoryActivity.this, R.string.visit_store13, ToastOrder.LENGTH_LONG).show();
				this.finish();
			}
		}
		return 0;
	}

	@Override
	protected View getCurGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		HashMap itemData = (HashMap) dataList.get(groupPosition);
		GroupViewHodler holder = null;
		if (convertView == null) {
			convertView = (LinearLayout) View.inflate(TableHistoryActivity.this, R.layout.table_list_item, null);
			holder = new GroupViewHodler();
			// 是否展开
			holder.iv_isExpanded = (ImageView) convertView.findViewById(R.id.iv_table_list_item_isExpanded);

			holder.tv_time = (TextView) View.inflate(TableHistoryActivity.this, R.layout.replenish_list_item_unit,
					null);
			holder.tv_time.setLayoutParams(new LayoutParams(PublicUtils.convertDIP2PX(TableHistoryActivity.this, 120),
					LayoutParams.WRAP_CONTENT));
			// 添加textview到item view
			((ViewGroup) convertView).addView(holder.tv_time);

			holder.tv_data = (TextView) View.inflate(TableHistoryActivity.this, R.layout.replenish_list_item_unit,
					null);
			holder.tv_data.setLayoutParams(new LayoutParams(PublicUtils.convertDIP2PX(TableHistoryActivity.this, 180),
					LayoutParams.WRAP_CONTENT));
			// 添加textview到item view
			((ViewGroup) convertView).addView(holder.tv_data);
			convertView.setTag(holder);
		}
		holder = (GroupViewHodler) convertView.getTag();
		holder.tv_time.setText((String) itemData.get("date"));
		holder.tv_data.setText(R.string.visit_store11);
		holder.tv_data.setOnClickListener(this);
		holder.tv_data.setTextSize(16);
		holder.tv_data.setTextColor(Color.rgb(0, 0, 255));
		holder.tv_data.getPaint().setUnderlineText(true);
		holder.tv_data.setTag((String) itemData.get("tab"));

		if (isExpanded) {
			holder.iv_isExpanded.setImageResource(R.drawable.icon_reduce);
		} else {
			holder.iv_isExpanded.setImageResource(R.drawable.icon_plus);
		}
		return convertView;
	}

	@Override
	protected View getCurChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {
		// 显示的item view
		RelativeLayout view = (RelativeLayout) View.inflate(TableHistoryActivity.this,
				R.layout.table_list_item_children, null);
		view.findViewById(R.id.ll_table_list_item_children).setVisibility(View.GONE);
		// 点击操作
		Button bt_do = (Button) view.findViewById(R.id.bt_table_list_item_children_do);
		bt_do.setVisibility(View.VISIBLE);
		bt_do.setText(R.string.visit_store12);
		bt_do.setOnClickListener(this);
		bt_do.setId(groupPosition);

		return view;
	}

	/**
	 * 缓存listview中item view-->listview的优化
	 */
	public class GroupViewHodler {
		public ImageView iv_isExpanded;
		public TextView tv_time;
		public TextView tv_data;
	}

	@Override
	public void onClick(View v) {
		if (TextUtils.isEmpty((String) v.getTag())) {// 修改
			if (func != null && func.getIsSearchModify() != null && func.getIsSearchModify() != 2) {
				ToastOrder.makeText(TableHistoryActivity.this, R.string.visit_store9, ToastOrder.LENGTH_LONG).show();
				this.finish();
			}

			HashMap<String, String> itemData = (HashMap<String, String>) dataList.get(v.getId());
			String tableJson = itemData.get("tab");
			String patchId = itemData.get(Constants.PATCH_ID);

			saveTableData(tableJson, patchId);

			bundle.putInt("targetType", menuType);
			bundle.putBoolean("isTableHistory", true);
			// 显示要修改的表格
			// CompDialog comp=new CompDialog(TableHistoryActivity.this, func,
			// bundle);
			// comp.setTableHistory(true);
			// comp.createDialog().show();

			CompDialog comp = new CompDialog(this, func, bundle);
			comp.getObject();
			this.finish();

		} else {// 查看表格
			Intent intent = new Intent(TableHistoryActivity.this, ShowTableActivity.class);
			intent.putExtra("menuType", menuType);
			intent.putExtra("tableId", func.getTableId());
			intent.putExtra("funcName", func.getName());
			intent.putExtra("tableJson", (String) v.getTag());
			startActivity(intent);
		}
		super.onClick(v);
	}

	/**
	 * 保存table数据(修改时使用)
	 * 
	 * @param tableJson
	 * @param patchId
	 */
	private void saveTableData(String tableJson, String patchId) {
		submitId = new SubmitDB(TableHistoryActivity.this).selectSubmitIdNotCheckOut(null, null, null, targetId,
				menuType, Submit.STATE_NO_SUBMIT);
		cleanDataMethodByNoSubmit();
		Submit submit = new Submit();

		submit.setTargetid(targetId);
		submit.setTargetType(menuType);
		submit.setStoreId(storeId);
		submit.setPlanId(bundle.getInt("planId"));
		submit.setWayId(bundle.getInt("wayId"));
		submit.setTimestamp(patchId);
		submit.setState(Submit.STATE_NO_SUBMIT);
		if (modType != 0) {
			submit.setModType(modType);
		}
		submit.setMenuId(menuId);
		submit.setMenuType(menuType);
		submit.setMenuName(menuName);
		new SubmitDB(TableHistoryActivity.this).insertSubmit(submit);

		int newSubmitId = new SubmitDB(TableHistoryActivity.this).selectSubmitIdNotCheckOut(null, null, null, targetId,
				menuType, Submit.STATE_NO_SUBMIT);

		SubmitItem submitItem = new SubmitItem();
		submitItem.setSubmitId(newSubmitId);
		submitItem.setType(Func.TYPE_TABLECOMP);
		submitItem.setTargetId(func.getTargetid() + "");
		submitItem.setParamName(func.getFuncId().toString());
		submitItem.setParamValue(tableJson);
		submitItem.setIsCacheFun(func.getIsCacheFun());
		new SubmitItemDB(TableHistoryActivity.this).insertSubmitItem(submitItem, false);
	}

	/**
	 * 删除未提交的数据
	 * 
	 * @param targetId
	 */
	private void cleanDataMethodByNoSubmit() {
		new SubmitDB(TableHistoryActivity.this).deleteSubmitById(submitId);
		new SubmitItemDB(TableHistoryActivity.this).deleteSubmitItemBySubmitId(submitId);
	}

	/**
	 * 按键按下事件-屏蔽返回按钮
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:// 返回按钮
			cleanDataMethodByNoSubmit();
			TableHistoryActivity.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
