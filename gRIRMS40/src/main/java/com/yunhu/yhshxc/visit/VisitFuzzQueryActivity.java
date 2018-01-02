package com.yunhu.yhshxc.visit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.bo.VisitStore;
import com.yunhu.yhshxc.bo.VisitWay;
import com.yunhu.yhshxc.customMade.xiaoyuan.VisitForXiaoYuan;
import com.yunhu.yhshxc.customMade.xiaoyuan.VisitForXiaoYuanListener;
import com.yunhu.yhshxc.database.VisitStoreDB;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.widget.ToastOrder;

public class VisitFuzzQueryActivity extends Activity {
	private final List<DataWrapper> data = new ArrayList<DataWrapper>();

	private AdapterImpl adapter;

	private AutoCompleteTextView edtInput;

	private int orderType;
	private int wayId;
	private int planId;
	private int intervalType;
	private String wayName;
	private int cycleCount;//拜访周期数
	private int visitCount;//拜访次数
	private String startDate;//开始日期
	/**
	 * 有序的情况下标识第几个店面可以访问
	 */
	private int firstEnableOrder;
	/**
	 * 有序的情况需要找到第一个可以操作的店面，
	 * 找到后，其他店面设为不能点击
	 */
	private boolean isFindEnable = false;
	
	/**
	 * tagFinished已完成的标识图片 tagRightOrder访店view右侧箭头（有序） tagRightDisorder访店view的右侧箭头（无序）
	 */
	private Drawable tagFinished, tagRightOrder, tagRightDisorder;

	private LinearLayout ll_bg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.visit_fuzz_query);
		ll_bg = (LinearLayout)findViewById(R.id.ll_bg);
//		if (!TextUtils.isEmpty(StyleUtil.homeStyleImg(this))) {
//			Drawable drawable = Drawable.createFromPath(Constants.COMPANY_STYLE_PATH+StyleUtil.homeStyleImg(this));
//			if (drawable!=null) {
//				ll_bg.setBackgroundDrawable(drawable);
//			}
//		}
		tagFinished = getResources().getDrawable(R.drawable.visitstore_finished);
		tagRightOrder = getResources().getDrawable(R.drawable.visit_right_order);
		tagRightDisorder = getResources().getDrawable(R.drawable.visit_right_disorder);

		orderType = getIntent().getIntExtra("isOrder", 0);// 获取排序类型
		wayId = getIntent().getIntExtra("wayId", 0);// 获取线路ID
		planId = getIntent().getIntExtra("planId", 0);// 获取计划ID
		wayName = getIntent().getStringExtra("wayName");// 获取线路名称
		intervalType = getIntent().getIntExtra("intervalType", 0);// 获取店面执行类型
		visitCount = getIntent().getIntExtra("visitCount", 0);//店面可拜访的次数
		cycleCount = getIntent().getIntExtra("cycleCount", 0);
		startDate = getIntent().getStringExtra("startDate");
		
		
		adapter = new AdapterImpl();
		
		TextView txtTitle = (TextView)findViewById(R.id.title);
		txtTitle.setText(wayName);
		
		ImageView imgTag = (ImageView)findViewById(R.id.imgTag);
		switch (orderType) {
			case VisitWay.ORDER_YES:
				imgTag.setImageResource(R.drawable.order_tag_left);
				break;
				
			case VisitWay.ORDER_NO:
				imgTag.setImageResource(R.drawable.disorder_tag_left);
				break;
		}

		edtInput = (AutoCompleteTextView) findViewById(R.id.input);
		edtInput.setAdapter(adapter);
		edtInput.setDropDownBackgroundResource(android.R.color.white);
		edtInput.setDropDownVerticalOffset(0);
		edtInput.setVerticalFadingEdgeEnabled(false);
		edtInput.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ListItem item = (ListItem)view;

				if (isDeleteStore(item.wrapper.store)){
					ToastOrder.makeText(getApplicationContext(),R.string.visit_store7, ToastOrder.LENGTH_LONG).show();
				}
				else if (item.wrapper.isFinished) {
					ToastOrder.makeText(getApplicationContext(), R.string.visit_store6, ToastOrder.LENGTH_LONG).show();
				}
				else {
					if (orderType == VisitWay.ORDER_YES) {
						int index = data.indexOf(item.wrapper);
						DataWrapper prev = index > 0 ? data.get(index - 1) : null;
						if (prev == null) {//有序的情况，如果是第一个直接跳转
							onClickForStore(item.wrapper.store);
						}
						else if ((prev.isFinished || isDeleteStore(prev.store)) && index == firstEnableOrder) {//判断如果上一个已经删除或者已经完成并且下标为当前可执行的那么就跳转
							onClickForStore(item.wrapper.store);
						}
						else {//不能访问的请款提示用用户该店不能访问
							ToastOrder.makeText(getApplicationContext(), R.string.visit_store5, ToastOrder.LENGTH_LONG).show();
						}
					}
					else {
						onClickForStore(item.wrapper.store);//页面跳转
					}
				}
			}
		});
	}
	
	/**
	 * 判断店面是否完全删除
	 * @param store
	 * @return true表示完全删除 false 没有完全删除
	 */
	public boolean isDeleteStore(VisitStore store){
		boolean flag = false;
		String deleteDate = store.getDeleteDate();
		if (!TextUtils.isEmpty(deleteDate) && store.getSubmitNum() >= visitCount) {
			flag = true;
		}
		return flag;
	}
	

	@Override
	protected void onStart() {
		super.onStart();
		resetData();
		adapter.notifyDataSetChanged();//改变UI
				
	}
	
	private void resetData(){
		//每次返回的时候先清除所有数据再重新添加
		data.clear();
		//查询所有店面
		List<VisitStore> stores = new VisitStoreDB(this).findAllVisitStoreList(wayId, planId);
		for (int i = 0; i < stores.size(); i++) {
			VisitStore store = stores.get(i);
			DataWrapper wrapper = new DataWrapper(store);
			data.add(wrapper);
		}
	}

	private class DataWrapper {
		VisitStore store;// 店面的实例
		boolean isFinished;// 标识该店面是否完成 true标识已经完成拜访 false表示未拜访
		boolean isCancel;// 标识该店面是否取消 true表示已经删除 false表示未删除

		DataWrapper(VisitStore store) {
			this.store = store;
			init();
		}

		/**
		 * 初始化店面信息
		 */
		void init() {
			VisitCycleUtil visitCycleUtil = new VisitCycleUtil(VisitFuzzQueryActivity.this);
			visitCycleUtil.setCycleCount(cycleCount);
			visitCycleUtil.setVisitCount(visitCount);
			visitCycleUtil.setStartDate(startDate);
			try {
				
//				intervalType = Constants.VISITWAY_TYPE_EVERY_MONTH;
				
				switch(intervalType){
				case Constants.VISITWAY_TYPE_EVERY_DAY:// 每天类型
					isFinished = visitCycleUtil.dayVisitType(store);
					break;
				case Constants.VISITWAY_TYPE_EVERY_MONTH:// 每月类型
					isFinished = visitCycleUtil.monthVisitType(store);
					break;
				case Constants.VISITWAY_TYPE_EVERY_WEEK: // 每周类型
					isFinished = visitCycleUtil.weekVisitType(store);
					break;
				case Constants.VISITWAY_TYPE_MOUDLE:// 自定义类型
					isFinished = visitCycleUtil.moduleVisitType(store);  //如果有值代表他执行了一次
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			updateStoreDeleteState(visitCycleUtil,store);
			
		}
	}
	
	/**
	 * 如果删除的时候和当前周期不在同一周期的话更改店面的删除状态
	 * @param store
	 */
	private void updateStoreDeleteState(VisitCycleUtil visitCycleUtil,VisitStore store){
		String deleteDate = store.getDeleteDate();
		String nowDate = DateUtil.getCurDate();
		boolean isSameCycle = false;
		if(!TextUtils.isEmpty(deleteDate)){
			switch (intervalType) {
			case Constants.VISITWAY_TYPE_EVERY_DAY:// 每天类型
				if (visitCycleUtil.cycleNumber(deleteDate,cycleCount) == visitCycleUtil.cycleNumber(nowDate, cycleCount)) {
					isSameCycle = true;
				}
				break;
			case Constants.VISITWAY_TYPE_EVERY_MONTH:// 每月类型
				if (visitCycleUtil.monthTypeCycleNumber(deleteDate,cycleCount) == visitCycleUtil.monthTypeCycleNumber(nowDate, cycleCount)) {
					isSameCycle = true;
				}
				break;
			case Constants.VISITWAY_TYPE_EVERY_WEEK: // 每周类型
				if (visitCycleUtil.cycleNumber(deleteDate,cycleCount*7) == visitCycleUtil.cycleNumber(nowDate, cycleCount*7)) {
					isSameCycle = true;
				}
				break;
			case Constants.VISITWAY_TYPE_MOUDLE:// 自定义类型
				isSameCycle = true;//自定义类型肯定是在同一周期内
				break;
			}
			if (!isSameCycle) {
				store.setDeleteDate("");
				new VisitStoreDB(getApplicationContext()).updateVisitStoreById(store);//取消店面后，一个周期后要重新允许提交
			}
		}
	}
	private class ListItem extends RelativeLayout {
		/**
		 * 左侧图标view设置ID
		 */
		static final int ID_LEFT_ICON = 10001;
		/**
		 * 封装的数据
		 */
		DataWrapper wrapper;
		/**
		 * 当前数据的下标
		 */
		int index;
		/**
		 * 左侧图标 右侧图标 删除图标
		 */
		ImageView iconLeft, iconRight, iconCancel;
		/**
		 * 店面名称
		 */
		TextView txtContent,txtNumber;
		
		ListItem(Context context) {
			super(context);
			
			float density = context.getResources().getDisplayMetrics().density;
			
			setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, (int)(60 * density)));

			//根据店面是否有序设置左侧部分的颜色
			LayoutParams lp = new LayoutParams((int)(60 * density), LayoutParams.MATCH_PARENT);
			FrameLayout iconLeftBg = new FrameLayout(context);
			iconLeftBg.setId(ID_LEFT_ICON);
			switch (orderType) {
				case VisitWay.ORDER_YES:
					iconLeftBg.setBackgroundResource(R.color.visit_store_item_left_icon_bg_order);
					break;
				
				case VisitWay.ORDER_NO:
					iconLeftBg.setBackgroundResource(R.color.visit_store_item_left_icon_bg_disorder);
					break;
			}
			addView(iconLeftBg, lp);

			//设置view左侧部分图标
			FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			flp.gravity = Gravity.CENTER;
			iconLeft = new ImageView(context);
			iconLeft.setImageResource(R.drawable.visitstore_lefticon);
			iconLeftBg.addView(iconLeft, flp);
			
			//设置view的删除图标
			lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp.rightMargin = (int)(10 * density);
			lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			lp.addRule(RelativeLayout.CENTER_VERTICAL);
			iconCancel = new ImageView(context);
			iconCancel.setImageResource(R.drawable.visitstore_cancel);
			addView(iconCancel, lp);
			iconCancel.setVisibility(View.GONE);
			
			//设置view的右侧图标
			lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp.rightMargin = (int)(10 * density);
			lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			lp.addRule(RelativeLayout.CENTER_VERTICAL);
			iconRight = new ImageView(context);
			addView(iconRight, lp);
			
			//设置view中的店面的名称
			lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp.topMargin = (int)(5 * density);
			lp.leftMargin = (int)(10 * density);
			lp.rightMargin = (int)(70 * density);
			lp.addRule(RelativeLayout.RIGHT_OF, ID_LEFT_ICON);
			txtContent = new TextView(context);
			txtContent.setGravity(Gravity.CENTER_VERTICAL);
			txtContent.setEllipsize(TruncateAt.END);
			txtContent.setTextColor(Color.WHITE);
			txtContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
			txtContent.setLines(2);
			addView(txtContent, lp);
			
			//添加该店面下可访问店面的数目
			lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.CENTER_VERTICAL);
			lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			lp.rightMargin = (int)(30 * density);
			txtNumber = new TextView(context);
//			txtNumber.setId(ID_NUMBER);
			txtNumber.setTextColor(Color.WHITE);
			txtNumber.setGravity(Gravity.CENTER);
			txtNumber.setBackgroundResource(R.drawable.order_num_bg);
			txtNumber.setVisibility(View.GONE);
			addView(txtNumber, lp);
			
			
		}
		
		/**
		 * 设置数据源
		 * @param index 当前店面的下标
		 */
		void setData(int index) {
			this.wrapper = adapter.stores.get(index);
			this.index = index;
			//设置店面名称
			txtContent.setText(wrapper.store.getName());
			//根据数据更新UI
			update();
		}
		
		/*
		 * 根据数据更新UI
		 */
		void update() {
			if (wrapper.isCancel) {//已经删除
				iconRight.setVisibility(View.GONE);
				iconCancel.setVisibility(View.VISIBLE);
			}
			else {
				iconRight.setVisibility(View.VISIBLE);
				iconCancel.setVisibility(View.GONE);
			}
			
			switch (orderType) {
			case VisitWay.ORDER_YES://有序
				if (wrapper.isFinished || isDeleteStore(wrapper.store)) {//已完成已删除
					setBackgroundResource(R.color.visit_item_order);
					iconRight.setImageDrawable(tagFinished);
				}else if (!isFindEnable) {//有序可操作, 就把当firstEnableOrder的值置为当前位置
					firstEnableOrder = index;
					setBackgroundResource(R.color.visit_item_order);
					iconRight.setImageDrawable(tagRightOrder);
					isFindEnable = true;
				}else{
					setBackgroundResource(R.color.visit_item_order_disable);
					iconRight.setImageDrawable(tagRightOrder);
				}
			
				break;
				
			case VisitWay.ORDER_NO://无序
				setBackgroundResource(R.color.visit_item_disorder);
				iconRight.setImageDrawable(wrapper.isFinished || isDeleteStore(wrapper.store) ? tagFinished : tagRightDisorder);
				break;
		}
			
			//如果未访问的线路的数目大于0就显示出来数目
			if (!isDeleteStore(wrapper.store) && !wrapper.isFinished && (visitCount - wrapper.store.getSubmitNum()) > 0) {
				txtNumber.setVisibility(View.VISIBLE);//设置数目可见
				txtNumber.setText(String.valueOf(visitCount - wrapper.store.getSubmitNum()));//给数目赋值
			}
			else {//如果没有可执行的店面则把数目隐藏
				txtNumber.setVisibility(View.GONE);
			}
			
		}
		
	}

	private class AdapterImpl extends BaseAdapter implements Filterable {
		private final List<DataWrapper> stores = new ArrayList<DataWrapper>();

		private Filter filter;

		@Override
		public int getCount() {
			return stores.size();
		}

		@Override
		public Object getItem(int position) {
			return stores.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ListItem item = convertView == null ? new ListItem(getApplicationContext()) : (ListItem)convertView;
			item.setData(position);
			if (orderType == VisitWay.ORDER_YES && position == 0)//如果是有序的情况第一个置为可用 其他的置灰（不可用状态）
				item.setBackgroundResource(R.color.visit_item_order);
			return item;
		}

		@Override
		public Filter getFilter() {
			if (filter == null)
				filter = new QueryFilter();
			return filter;
		}

		private class QueryFilter extends Filter {

			@Override
			protected FilterResults performFiltering(CharSequence keyword) {
				FilterResults results = new FilterResults();
				resetData();
				LinkedList<DataWrapper> tmp = new LinkedList<DataWrapper>();
				if (keyword == null || keyword.length() == 0) {
					tmp.addAll(data);
				}else {
					for (DataWrapper i : data) {
						if (i.store.getName().contains(keyword)) {
							tmp.add(i);
						}
					}
				}

				results.values = tmp;
				results.count = tmp.size();

				return results;
			}

			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
				synchronized (stores) {
					stores.clear();
					stores.addAll((Collection<DataWrapper>) results.values);
					data.clear();
					data.addAll(stores);
				}
				notifyDataSetChanged();
			}

			@Override
			public CharSequence convertResultToString(Object resultValue) {
				if (resultValue == null)
					return "";

				if (resultValue instanceof DataWrapper) {
					DataWrapper wrapper = (DataWrapper) resultValue;
					return wrapper.store.getName();
				}

				return super.convertResultToString(resultValue);
			}
		}
		
		@Override
		public void notifyDataSetChanged() {
			isFindEnable = false;
			super.notifyDataSetChanged();
		}
		
	}

	
	/**
	 * 跳转到控件页面
	 * storeId 店面ID
	 * targetId 数据ID
	 * isCheckin 是否进入定位
	 * isCheckout 提交时候是否定位
	 * planId 计划ID
	 * storeName店名
	 * menuType menu类型
	 * @param store 当前店面
	 */
	private void onClickForStore(final VisitStore store){
		if (VisitForXiaoYuan.getInstance(this).isUse()) {//如果是校园项目先联网获取店面属性然后再跳转
			Dialog dialog = new MyProgressDialog(this,R.style.CustomProgressDialog, PublicUtils.getResourceString(this,R.string.init));
			VisitForXiaoYuan.getInstance(this).search(dialog,store.getStoreId(), new VisitForXiaoYuanListener() {
				
				@Override
				public void searchResult() {
					intentToFunc(store);
				}
			});
		}else{//不是校园项目直接跳转
			intentToFunc(store);
		}
	}
	
	private void intentToFunc(VisitStore store){
		Intent intent = new Intent(VisitFuzzQueryActivity.this,VisitFuncActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("wayId", wayId);
		bundle.putInt("storeId", store.getStoreId());
		bundle.putInt("targetId",store.getTargetid());
		bundle.putInt("isCheckin", store.getIsCheckin());
		bundle.putInt("isCheckout", store.getIsCheckout());
		bundle.putInt("planId", store.getPlanId());
		bundle.putString("storeName", store.getName());
		bundle.putString("wayName", wayName);
		bundle.putString("storelon", store.getLon());
		bundle.putString("storelat", store.getLat());
		bundle.putInt("isCheck", store.getIsCheck());
		bundle.putInt("storeDistance", store.getStoreDistance());
		bundle.putString("is_address", TextUtils.isEmpty(store.getIsAddress())?"1":store.getIsAddress());//默认1显示
		bundle.putString("is_anew_loc", TextUtils.isEmpty(store.getIsNewLoc())?"0":store.getIsNewLoc());//默认0不重新定位
		bundle.putString("loc_type", TextUtils.isEmpty(store.getLocType())?"2":store.getLocType());//默认2选择所有定位方式
		bundle.putInt("menuType", Menu.TYPE_VISIT);
		intent.putExtra("isNoWait", store.getIsNoWait()==1?true:false);
		intent.putExtra("bundle", bundle);
		startActivity(intent);
		this.finish();
	}
	
}
