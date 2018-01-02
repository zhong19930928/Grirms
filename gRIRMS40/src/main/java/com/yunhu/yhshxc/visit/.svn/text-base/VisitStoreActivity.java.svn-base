package com.yunhu.yhshxc.visit;

import gcg.org.debug.JLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.bo.VisitStore;
import com.yunhu.yhshxc.bo.VisitWay;
import com.yunhu.yhshxc.customMade.xiaoyuan.VisitForXiaoYuan;
import com.yunhu.yhshxc.customMade.xiaoyuan.VisitForXiaoYuanListener;
import com.yunhu.yhshxc.database.VisitStoreDB;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.CoreHttpHelper;
import com.yunhu.yhshxc.submitManager.bo.PendingRequestVO;
import com.yunhu.yhshxc.submitManager.bo.TablePending;
import com.yunhu.yhshxc.submitManager.core.SubmitWorkManager;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;

/**
 * 拜访店面activity  以listview的形式展示要拜访的店面 分为有序和无序两种情况
 * 有序的店面：访问的时候必须一个一个的访问，只有提交过一个店面，才能访问另一个(不能访问的店面显示灰色的)
 * 无序的店面：不必按顺序访问
 * 店面拜访完以后更改店面的显示状态，并改变UI体现已拜访
 * 滑动的时候会出现删除按钮，点击删除该店，删除以后店面不能再访问，变为灰色
 * @author jishen
 * @version 2012-05-22
 */
public class VisitStoreActivity extends AbsBaseActivity{
	
	/**
	 * 访店的数据库实例
	 */
	private VisitStoreDB visitStoreDB;
	
	/**
	 * 店面的访问类型 有“每周” “每日” “每月” “自定义” 
	 */
	private int intervalType;
	/**
	 * 线路的名字
	 */
	private String wayName;
	/**
	 * 是否有序 0表示无序 1表示有序
	 */
	private int orderType;
	/**
	 * 线路ID 计划ID
	 */
	private int wayId,planId;
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
	 * 当前线路下所有店面的数据的封装类的集合
	 */
	private ArrayList<DataWrapper> items = new ArrayList<DataWrapper>();

	/**
	 * 拜访店面的listview
	 */
	private ListView lst;
	
	private int cycleCount;//拜访周期数
	private int visitCount;//拜访次数
	private String startDate;//开始日期

	/**
	 * 拜访店面listview的adapter
	 */
	private final BaseAdapter adapter = new BaseAdapter() {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			//根据位置返回一个访店view
			ListItem item = convertView == null ? new ListItem(getApplicationContext()) : (ListItem)convertView;
			item.setData(position);
			if (orderType == VisitWay.ORDER_YES && position == 0)//如果是有序的情况第一个置为可用 其他的置灰（不可用状态）
				item.setBackgroundResource(R.color.visit_item_order);
			return item;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public Object getItem(int position) {
			return items.get(position);
		}

		@Override
		public int getCount() {
			return items.size();
		}

		@Override
		public void notifyDataSetChanged() {
			isFindEnable = false;
			super.notifyDataSetChanged();
		}
	};
	
	/**
	 * 数据改变后更新UI
	 */
	private final Handler handler = new Handler(new Handler.Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			adapter.notifyDataSetChanged();
			return true;
		}
	});
	
	/**
	 * tagFinished已完成的标识图片 tagRightOrder访店view右侧箭头（有序） tagRightDisorder访店view的右侧箭头（无序）
	 */
	private Drawable tagFinished, tagRightOrder, tagRightDisorder;
	private RelativeLayout ll_bg;
	private String menuName;
	private int menuType;
	private int menuId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.visit_store);
		initBase();
		ll_bg = (RelativeLayout)findViewById(R.id.ll_store_bg);
//		if (!TextUtils.isEmpty(StyleUtil.homeStyleImg(this))) {
//			Drawable drawable = Drawable.createFromPath(Constants.COMPANY_STYLE_PATH+StyleUtil.homeStyleImg(this));
//			if (drawable!=null) {
//				ll_bg.setBackgroundDrawable(drawable);
//			}
//		}
		tagFinished = getResources().getDrawable(R.drawable.icon_finish);
		tagRightOrder = getResources().getDrawable(R.drawable.visit_right_order);
		tagRightDisorder = getResources().getDrawable(R.drawable.visit_right_disorder);
		
		animOut = new TranslateAnimation(Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 120, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0);
		animOut.setDuration(200);
		animOut.setAnimationListener(animListener);
		
		animIn = new TranslateAnimation(Animation.ABSOLUTE, 120, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0);
		animIn.setDuration(200);
		animIn.setAnimationListener(animListener);

		visitStoreDB = new VisitStoreDB(this);
		Bundle bundle = getIntent().getBundleExtra("visitWay");
		menuName=bundle.getString("menuName");
		menuId=bundle.getInt("menuId");
		menuType=bundle.getInt("menuType");
		
		orderType = bundle.getInt("isOrder", 0);//获取排序类型
		wayId = bundle.getInt("wayId", 0);//获取线路ID
		planId = bundle.getInt("planId", 0);//获取计划ID
		wayName = bundle.getString("wayName");//获取线路名称
		
		intervalType = bundle.getInt("intervalType");//获取店面执行类型
		cycleCount = bundle.getInt("cycle_count", 1);//默认是1
		visitCount = bundle.getInt("visit_count", 1);//默认也是1
		startDate = bundle.getString("startDate");//开始日期
		
		lst = (ListView)findViewById(R.id.list);
		lst.setAdapter(adapter);
		
		//给title上设置线路名称
		TextView title = (TextView)findViewById(R.id.title);
		title.setLines(1);
		title.setEllipsize(TruncateAt.END);
		title.setText(wayName);
		
		//根据排序情况改变图标
		ImageView tag = (ImageView)findViewById(R.id.imgTag);
		switch (orderType) {
			case VisitWay.ORDER_YES:
				tag.setImageResource(R.drawable.order_tag_left);
				break;
				
			case VisitWay.ORDER_NO:
				tag.setImageResource(R.drawable.disorder_tag_left);
				break;
		}
		
		ImageButton btnQuery = (ImageButton)findViewById(R.id.query);
		btnQuery.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), VisitFuzzQueryActivity.class);
				intent.putExtra("isOrder", orderType);
				intent.putExtra("wayName", wayName);
				intent.putExtra("wayId", wayId);
				intent.putExtra("planId", planId);
				intent.putExtra("intervalType", intervalType);
				intent.putExtra("visitCount", visitCount);
				intent.putExtra("cycleCount", cycleCount);
				intent.putExtra("startDate", startDate);
				startActivity(intent);
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
		//每次返回的时候先清除所有数据再重新添加
//		items.clear();
		items = new ArrayList<DataWrapper>();
		//查询所有店面
		List<VisitStore> stores = visitStoreDB.findAllVisitStoreList(wayId, planId);
		for (int i = 0; i < stores.size(); i++) {
			VisitStore store = stores.get(i);
			DataWrapper data = new DataWrapper(store);
			items.add(data);
		}
		adapter.notifyDataSetChanged();//改变UI
	}
	
	/**
	 * 店面的数据封装类 包含店面的实例 店面是否完成的标识 店面是否取消的标识
	 * @author jishen
	 *
	 */
	private class DataWrapper {
		VisitStore store;//店面的实例
		boolean isFinished;//标识该店面是否完成 true标识已经完成拜访 false表示未拜访
		boolean isCancel;//标识该店面是否取消 true表示已经删除 false表示未删除
		
		DataWrapper(VisitStore store) {
			this.store = store;
			init();
		}
		
		/**
		 * 初始化店面信息
		 */
		void init() {
			VisitCycleUtil visitCycleUtil = new VisitCycleUtil(VisitStoreActivity.this);
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
				JLog.e(e);
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
	
	/**
	 * isMoving标识手指是否在当前view上移动 true 表示正在移动 false表示没有移动
	 */
	private volatile boolean isMoving, isProcessed;
	/**
	 * 当前手指按下的x轴的位置
	 */
	private volatile float downX;
	/**
	 * 手指在当前view上连续移动的数
	 */
	private volatile int movingCount;
	/**
	 * 访店的view实例
	 *
	 */
	private class ListItem extends RelativeLayout {
		/**
		 * 左侧图标view设置ID
		 */
		static final int ID_LEFT_ICON = 10001;
		
		/**
		 * 剩余可拜访店面的View的ID
		 */
		private final int ID_NUMBER = 10003;
		/**
		 * 右边图标的view的ID
		 */
		private final int ID_RIGHT = 10004;
		
		
		/**
		 * 封装的数据
		 */
		DataWrapper data;
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
			RelativeLayout iconLeftBg = new RelativeLayout(context);
			iconLeftBg.setId(ID_LEFT_ICON);
		
			switch (orderType) {
				case VisitWay.ORDER_YES:
					iconLeftBg.setBackgroundResource(R.color.visit_store_item_left_icon_bg_order);
					break;
				
				case VisitWay.ORDER_NO:
					iconLeftBg.setBackgroundResource(R.color.white);
					break;
			}
			addView(iconLeftBg, lp);

			//设置view左侧部分图标
			LayoutParams flp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			flp.addRule(RelativeLayout.CENTER_VERTICAL);
			flp.topMargin = (int)(5 * density*2);	
			flp.leftMargin = (int)(10 * density);
			iconLeft = new ImageView(context);			
			iconLeft.setImageResource(R.drawable.visitstore_lefticon1);
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
			
			
			//添加该店面下可访问店面的数目
			lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.CENTER_VERTICAL);
			lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			lp.rightMargin = (int)(30 * density);
			txtNumber = new TextView(context);
			txtNumber.setId(ID_NUMBER);
			txtNumber.setTextColor(Color.WHITE);
			txtNumber.setGravity(Gravity.CENTER);
			txtNumber.setBackgroundResource(R.drawable.order_num_bg);
			txtNumber.setVisibility(View.GONE);
			addView(txtNumber, lp);
			
			
			
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
			lp.addRule(RelativeLayout.CENTER_VERTICAL);
			txtContent = new TextView(context);
			txtContent.setGravity(Gravity.CENTER_VERTICAL);
			txtContent.setEllipsize(TruncateAt.END);
			txtContent.setTextColor(Color.BLACK);		
			txtContent.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
			txtContent.setLines(2);
			addView(txtContent, lp);
			/**
			 * 给删除图标添加单击事件
			 */
			iconCancel.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//如果店面已经删除或已经完成则不处理
					if (isDeleteStore(data.store) || data.isFinished)
						return;
					//如果店面是有序的要确定当前店面是可以操作并且是可以删除的才能删除
					if (orderType == VisitWay.ORDER_YES) {
						int index = items.indexOf(data);
						DataWrapper prev = index > 0 ? items.get(index - 1) : null;
						if (prev == null) {
							showDeleteDialog(ListItem.this);
							isProcessed = true;
						}
						else if ((prev.isFinished || isDeleteStore(data.store)) && index == firstEnableOrder) {
							showDeleteDialog(ListItem.this);
							isProcessed = true;
						}
					}
					else {//如果是无序的店面则可以直接删除
						showDeleteDialog(ListItem.this);
						isProcessed = true;
					}
				}
			});
			/**
			 * 给view添加长点击事件
			 */
			setOnLongClickListener(new View.OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					//如果店面已经摆放完毕 或正在移动则不处理
					if (isMoving || isProcessed || isPlaying || isDeleteStore(data.store) || data.isFinished)
						return false;
					//如果店面是有序的要确定当前店面是可以操作并且是可以删除的才能删除
					if (orderType == VisitWay.ORDER_YES) {
						int index = items.indexOf(data);
						DataWrapper prev = index > 0 ? items.get(index - 1) : null;
						if (prev == null) {
							showDeleteDialog(ListItem.this);
							isProcessed = true;
						}
						else if ((prev.isFinished || isDeleteStore(data.store)) && index == firstEnableOrder) {
							showDeleteDialog(ListItem.this);
							isProcessed = true;
						}
					}
					else {//如果是无序的店面则可以直接删除
						showDeleteDialog(ListItem.this);
						isProcessed = true;
					}
					return true;
				}
			});
			
			/*
			 * 给view添加触摸事件
			 */
			setOnTouchListener(new View.OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN://按下
							movingCount = 0;
							downX = event.getX();
							setBackgroundResource(R.color.home_menu_pressed);//改变背景色
							break;
							
						case MotionEvent.ACTION_UP://抬起手指
							if (!isMoving && !isProcessed && !isPlaying) {
								if (isDeleteStore(data.store)){
//									Toast.makeText(getApplicationContext(), "该店已删除！", Toast.LENGTH_LONG).show();
								}
								else if (data.isFinished) {
//									Toast.makeText(getApplicationContext(), "该店已访问！", Toast.LENGTH_LONG).show();
								}
								else {
									if (orderType == VisitWay.ORDER_YES) {
										int index = items.indexOf(data);
										DataWrapper prev = index > 0 ? items.get(index - 1) : null;
										if (prev == null) {//有序的情况，如果是第一个直接跳转
											onClickForStore(data.store);
										}
										else if ((prev.isFinished || isDeleteStore(data.store)) && index == firstEnableOrder) {//判断如果上一个已经删除或者已经完成并且下标为当前可执行的那么就跳转
											onClickForStore(data.store);
										}
										else {//不能访问的请款提示用用户该店不能访问
											ToastOrder.makeText(getApplicationContext(), R.string.visit_store5, ToastOrder.LENGTH_LONG).show();
										}
									}
									else {
										onClickForStore(data.store);//页面跳转
									}
								}
							}
						case MotionEvent.ACTION_CANCEL:
						case MotionEvent.ACTION_OUTSIDE://不移动和超出范围的时候初始化信息
							downX = 0;
							isMoving = isProcessed = false;
							
							switch (orderType) {
								case VisitWay.ORDER_YES:
									setBackgroundResource(data.isFinished || isDeleteStore(data.store) || index == firstEnableOrder ? R.color.white : R.color.white);
									break;
									
								case VisitWay.ORDER_NO:
									setBackgroundResource(R.color.white);
									break;
							}
							break;
							
						case MotionEvent.ACTION_MOVE://移动的情况
							if (movingCount++ < 2 || isProcessed || data.isFinished || isDeleteStore(data.store))//移动距离不够 没有获取焦点 已经完成的情况不处理
								return true;

							System.out.println(downX + "/" + event.getX());
							if (!isMoving && Math.abs(event.getX() - downX) > 10)
								isMoving = true;
							
							float offset = event.getX() - downX;
							if (Math.abs(offset) > 50) {//左右移动范围大于50就显示删除按钮
								if (orderType == VisitWay.ORDER_YES) {
									int index = items.indexOf(data);
									DataWrapper prev = index > 0 ? items.get(index - 1) : null;
									if (prev == null) {
										playAnimation(ListItem.this, data.isFinished);
									}
									else if ((prev.isFinished || isDeleteStore(prev.store)) && index == firstEnableOrder) {
										playAnimation(ListItem.this, data.isFinished);
									}
								}
								else {
									playAnimation(ListItem.this, data.isFinished);
								}
							}
							return true;
					}
					return false;
				}
			});
		}
		
		/**
		 * 设置数据源
		 * @param index 当前店面的下标
		 */
		void setData(int index) {
			this.data = items.get(index);
			this.index = index;
			//设置店面名称
			txtContent.setText(data.store.getName());
			//根据数据更新UI
			update();
		}
		
		/*
		 * 根据数据更新UI
		 */
		void update() {
			if (data.isCancel) {//已经删除
				iconRight.setVisibility(View.GONE);
				iconCancel.setVisibility(View.VISIBLE);
			}
			else {
				iconRight.setVisibility(View.VISIBLE);
				iconCancel.setVisibility(View.GONE);
			}
			
			switch (orderType) {
				case VisitWay.ORDER_YES://有序
					if (data.isFinished || isDeleteStore(data.store)) {//已完成已删除
						setBackgroundResource(R.color.white);
						iconRight.setImageDrawable(tagFinished);
					}else if (!isFindEnable) {//有序可操作, 就把当firstEnableOrder的值置为当前位置
						firstEnableOrder = index;
						setBackgroundResource(R.color.white);
						iconRight.setImageDrawable(tagRightOrder);
						isFindEnable = true;
					}else{
						setBackgroundResource(R.color.white);
						iconRight.setImageDrawable(tagRightOrder);
					}
				
					break;
					
				case VisitWay.ORDER_NO://无序
					setBackgroundResource(R.color.white);
					iconRight.setImageDrawable(data.isFinished || isDeleteStore(data.store) ? tagFinished : tagRightDisorder);
					break;
			}
			
			//如果未访问的线路的数目大于0就显示出来数目
			if (!isDeleteStore(data.store) && !data.isFinished && (visitCount - data.store.getSubmitNum()) > 0) {
				txtNumber.setVisibility(View.VISIBLE);//设置数目可见
				txtNumber.setText(String.valueOf(visitCount - data.store.getSubmitNum()));//给数目赋值
			}
			else {//如果没有可执行的店面则把数目隐藏
				txtNumber.setVisibility(View.GONE);
			}
		}
		
		
	}
	
	
	/*
	 * 动画是否正在运行
	 */
	private volatile boolean isPlaying;
	/*
	 * animOut 出的动画 animIn进入的动画
	 */
	private TranslateAnimation animOut, animIn;
	private final AnimationListenerImpl animListener = new AnimationListenerImpl();
	/**
	 *设置动画 
	 *右侧删除图标出现和隐藏的时候的动画效果
	 */
	private class AnimationListenerImpl implements AnimationListener {
		/*
		 * 当前店面view
		 */
		ListItem host;
		/*
		 * 是否删除
		 */
		boolean isFinished;

		/*
		 * 开始动画
		 * (non-Javadoc)
		 * @see android.view.animation.Animation.AnimationListener#onAnimationStart(android.view.animation.Animation)
		 */
		@Override
		public void onAnimationStart(Animation animation) {
			if (!host.data.isCancel) {
				host.iconRight.setVisibility(View.GONE);
				host.iconCancel.setVisibility(View.VISIBLE);
				host.txtNumber.setVisibility(View.GONE);

			}
		}

		/*
		 * 结束动画
		 * (non-Javadoc)
		 * @see android.view.animation.Animation.AnimationListener#onAnimationEnd(android.view.animation.Animation)
		 */
		@Override
		public void onAnimationEnd(Animation animation) {
			if (host.data.isCancel) {
				host.iconRight.setVisibility(View.VISIBLE);
				host.iconCancel.setVisibility(View.GONE);
				host.txtNumber.setVisibility(View.VISIBLE);

				//根据是否有序设置不同的值
				switch (orderType) {
					case VisitWay.ORDER_YES:
						host.setBackgroundResource(host.data.isFinished || isDeleteStore(host.data.store) || host.index == firstEnableOrder ? R.color.visit_item_order : R.color.visit_item_order_disable);
						host.iconRight.setImageDrawable(isFinished ? tagFinished : tagRightOrder);
						break;
						
					case VisitWay.ORDER_NO:
						host.setBackgroundResource(R.color.visit_item_disorder);
						host.iconRight.setImageDrawable(isFinished ? tagFinished : tagRightDisorder);
						break;
				}
			}
			host.data.isCancel = !host.data.isCancel;
			isPlaying = false;
		}

		@Override
		public void onAnimationRepeat(Animation animation) {}
		
	}
	
	/**
	 * 开始动画效果
	 * @param host 当前操作的view
	 * @param isFinished 是否已经访问
	 * 
	 */
	private void playAnimation(ListItem host, boolean isFinished) {
		if (isPlaying) return;//如果动画正在运行不处理
		
		isPlaying = true;
		
		animListener.host = host;
		animListener.isFinished = isFinished;
		host.iconCancel.startAnimation(host.data.isCancel ? animOut : animIn);//开始动画
	}
	
	/*
	 *删除店面的原因的输入框 
	 */
	private EditText edtDialog;
	
	/**
	 * 弹出删除原因对话框
	 * @param item 当前要删除的view
	 */
	private void showDeleteDialog(final ListItem item){
		final Dialog dialog = new Dialog(VisitStoreActivity.this, R.style.transparentDialog);
		View view = View.inflate(this, R.layout.dialog_delte_tempstore, null);
		
		//删除提示内容TextView 
		TextView  tvContent = (TextView) view.findViewById(R.id.tv_back_content);
		//设置删除提示内容TextView不显示
		tvContent.setVisibility(View.GONE);
		//店面取消的理由EditText 
		edtDialog = (EditText) view.findViewById(R.id.et_delete_reason);
		//设置店面取消的理由EditText显示 
		edtDialog.setVisibility(View.VISIBLE);

		Button confirm=(Button)view.findViewById(R.id.btn_delete_tempstore_confirm);
		Button cancel=(Button)view.findViewById(R.id.btn_delete_tempstore_cancel);
		confirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//获取删除理由 
				String deleteReason = edtDialog.getText().toString();
				if(TextUtils.isEmpty(deleteReason)){//删除理由为空 
					//提示用户输入删除理由
					 ToastOrder.makeText(VisitStoreActivity.this, R.string.visit_store4, ToastOrder.LENGTH_SHORT).show();
					 //结束本次点击事件
					 return;
				}
				if (deleteReason.length() > 500) {//删除原因不能超过500
					ToastOrder.makeText(VisitStoreActivity.this, R.string.visit_store3, ToastOrder.LENGTH_SHORT).show();
					return;
				}
				item.data.store.setDeleteDate(DateUtil.getCurDate());
				item.data.store.setSubmitDate(DateUtil.getCurDateTime());
				item.data.store.setSubmitNum(item.data.store.getSubmitNum() + 1);
				visitStoreDB.updateVisitStoreById(item.data.store);
				item.data.isCancel = false;
				item.data.isFinished = isDeleteStore(item.data.store);
				
				dialog.dismiss();
				
				isProcessed = false;

				adapter.notifyDataSetChanged();


				//删除店面传给后台的数据
				HashMap<String, String> params=new HashMap<String, String>();
				params.put("storeId", String.valueOf(item.data.store.getStoreId()));//当前的店面的ID
				params.put("planId", String.valueOf(planId));//当前的计划ID
				params.put("routeId", String.valueOf(wayId));//当前的线路ID
				params.put("operateTime", String.valueOf(System.currentTimeMillis()));//删除时间
				params.put("taskId", String.valueOf(item.data.store.getTargetid()));//数据ID
				params.put("submit_count", String.valueOf(item.data.store.getSubmitNum()));//店面已经拜访的次数

				// 店面删除理由 
				params.put("msg", deleteReason);
				//提交给后台
				
				PendingRequestVO vo = new PendingRequestVO();
	        	vo.setContent(wayName+"-"+item.data.store.getName()+"("+params.get("operateTime")+")");
	        	vo.setTitle(PublicUtils.getResourceString(VisitStoreActivity.this,R.string.visit_store2));
	        	vo.setMethodType(SubmitWorkManager.HTTP_METHOD_POST);
	        	vo.setParams(params);
	        	vo.setType(TablePending.TYPE_DATA);
	        	vo.setUrl(UrlInfo.getUrlDeleteStore(VisitStoreActivity.this));
	    		new CoreHttpHelper().performJustSubmit(VisitStoreActivity.this,vo);
//				new CoreHttpHelper(VisitStoreActivity.this).performJustSubmit(SubmitInBackstageManager.HTTP_METHOD_POST, UrlInfo.getUrlDeleteStore(VisitStoreActivity.this), params);
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.setContentView(view);
		dialog.setCancelable(false);
		dialog.show();
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
			Dialog dialog = new MyProgressDialog(this,R.style.CustomProgressDialog,PublicUtils.getResourceString(this,R.string.init));
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
		Intent intent = new Intent(VisitStoreActivity.this,VisitFuncActivity.class);
		Bundle bundle = getIntent().getBundleExtra("visitWay");
		bundle.putString("menuName", menuName);
		bundle.putInt("menuId", menuId);
		bundle.putInt("menuType", menuType);
		bundle.putInt("storeId", store.getStoreId());
		bundle.putInt("targetId",store.getTargetid());
		bundle.putInt("isCheckin", store.getIsCheckin());
		bundle.putInt("isCheckout", store.getIsCheckout());
		bundle.putInt("planId", store.getPlanId());
		bundle.putString("storeName", store.getName());
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
	}
}
