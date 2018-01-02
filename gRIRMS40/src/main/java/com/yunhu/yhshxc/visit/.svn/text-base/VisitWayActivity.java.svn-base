package com.yunhu.yhshxc.visit;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.bo.VisitStore;
import com.yunhu.yhshxc.bo.VisitWay;
import com.yunhu.yhshxc.comp.TitleBar;
import com.yunhu.yhshxc.database.VisitStoreDB;
import com.yunhu.yhshxc.database.VisitWayDB;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.PublicUtils;

/**
 * 拜访线路activity  以listview的形式展示要拜访的线路
 * @author jishen
 * @version 2012-05-22
 */
public class VisitWayActivity extends AbsBaseActivity {
	
	/**
	 * TitleBar的view id
	 */
	private final int ID_BAR = 10001;
	/**
	 * 左部文本view的ID
	 */
	private final int ID_LEFT = 10002;
	/**
	 * 剩余可拜访店面的View的ID
	 */
	private final int ID_NUMBER = 10003;
	/**
	 * 右边图标的view的ID
	 */
	private final int ID_RIGHT = 10004;
	
	/**
	 * 设置一个单线程的线程池 加载页面的时候使用
	 */
	private final ExecutorService thread = Executors.newSingleThreadExecutor();
	
	/**
	 * 页面顶部的电池电量，信号状态 定位 显示
	 */
	private TitleBar titlebar;

	/**
	 * 所有可以访问的线路的集合
	 */
	private List<VisitWay> items = new ArrayList<VisitWay>();

	/**
	 * 线路布局的ListView
	 */
	private ListView lst;

	/**
	 * 页面布局ListView的adapter
	 */
	private final BaseAdapter adapter = new BaseAdapter() {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			//根据位置获取view
			ListItem item = convertView == null ? new ListItem(getApplicationContext()) : (ListItem)convertView;
			item.setData(items.get(position));//设置数据
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
	};
	
	/**
	 * 更新listview页面
	 */
	private final Handler handler = new Handler(new Handler.Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			adapter.notifyDataSetChanged();
			return true;
		}
	});
	
	/**
	 * 更新没访问的店面的数量 线程
	 */
	private final Runnable updateUnVisitCount = new Runnable() {
		
		@Override
		public void run() {
			updateUnVisitCount();
			handler.sendEmptyMessage(0);//更新完数据以后更改页面
		}
	};
	
	/**
	 * 线路页面实例
	 *
	 */
	private class ListItem extends RelativeLayout {
		/**
		 * 线路右侧的图标
		 */
		ImageView iconRight;
		/**
		 * 线路左侧文本 ，线路内容，未拜访的店面的数目
		 */
		TextView txtLeft, txtContent, txtNumber;
		/**
		 * 该条线路的实例
		 */
		VisitWay data;

		ListItem(Context context) {
			super(context);
			
			float density = context.getResources().getDisplayMetrics().density;
			
			setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, (int)(60 * density)));
			
			//添加左侧文本内容
			LayoutParams lp = new LayoutParams((int)(40 * density), (int)(40 * density));
			lp.addRule(RelativeLayout.CENTER_VERTICAL);
			lp.leftMargin=(int) (10 * density);
			txtLeft = new TextView(context);
			txtLeft.setId(ID_LEFT);
			txtLeft.setGravity(Gravity.CENTER);
			txtLeft.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
			addView(txtLeft, lp);
			
			//添加线路名称
			lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			lp.addRule(RelativeLayout.CENTER_VERTICAL);
			lp.addRule(RelativeLayout.RIGHT_OF, ID_LEFT);
			lp.addRule(RelativeLayout.LEFT_OF, ID_NUMBER);
			lp.leftMargin = (int)(10 * density);
			lp.rightMargin = (int)(10 * density);
			txtContent = new TextView(context);
			txtContent.setGravity(Gravity.CENTER_VERTICAL);
			txtContent.setEllipsize(TruncateAt.END);
			txtContent.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
			txtContent.setTextColor(Color.BLACK);
			txtContent.setLines(2);
			txtContent.setPadding((int)(10 * density), 0, 0, 0);
			addView(txtContent, lp);
			
			//添加该线路下可访问店面的数目
			lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.CENTER_VERTICAL);
			lp.addRule(RelativeLayout.LEFT_OF, ID_RIGHT);
			lp.rightMargin = (int)(10 * density);
			txtNumber = new TextView(context);
			txtNumber.setId(ID_NUMBER);
			txtNumber.setTextColor(Color.WHITE);
			txtNumber.setGravity(Gravity.CENTER);
			txtNumber.setBackgroundResource(R.drawable.order_num_bg);
			txtNumber.setVisibility(View.GONE);
			addView(txtNumber, lp);
			
			//添加右侧图标
			lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.CENTER_VERTICAL);
			lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			lp.rightMargin = (int)(10 * density);
			iconRight = new ImageView(context);
			iconRight.setId(ID_RIGHT);
			addView(iconRight, lp);			
			/**
			 * 给线路布局View设置单击事件，单击跳转到店面activity并传入一系列数据
			 */
			setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getApplicationContext(), VisitStoreActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("menuName", menuName);
					bundle.putInt("menuId", menuId);
					bundle.putInt("menuType", menuType);
					bundle.putString("wayName", data.getName());//传递线路名字
					bundle.putInt("isOrder", data.getIsOrder());//是否排序
					bundle.putInt("wayId", data.getWayId());//线路ID
					bundle.putInt("planId", data.getPlanId());//计划ID
					bundle.putInt("awokeType", data.getAwokeType());//提醒类型
					bundle.putInt("intervalType", data.getIntervalType());//执行类型
					bundle.putString("startDate", data.getStartdate());//店面拜访的开始日期
					bundle.putInt("cycle_count", data.getCycleCount());//拜访周期数
					bundle.putInt("visit_count", data.getVisitCount());//拜访次数
					intent.putExtra("visitWay", bundle);
					startActivity(intent);
				}
			});
			
			/**
			 * 给线路布局View设置触摸事件
			 */
			setOnTouchListener(new View.OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN://按下的时候改变背景颜色
							setBackgroundResource(R.color.home_menu_pressed);
							break;
							
						case MotionEvent.ACTION_UP:
						case MotionEvent.ACTION_CANCEL:
						case MotionEvent.ACTION_OUTSIDE://更改页面布局
							update();
							break;
					}
					return false;
				}
			});
		}
		
		/**
		 * 根据数据源设置页面显示
		 * @param data 当前线路的数据源
		 */
		void setData(VisitWay data) {
			this.data = data;
			update();
		}
		
		/**
		 * 更改页面显示
		 */
		void update() {
			txtContent.setText(data.getName());//设置线路的名称
			switch (data.getIsOrder()) {
				case VisitWay.ORDER_YES://有序的线路
					setBackgroundResource(R.color.white);
					txtLeft.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_text_square));
					txtLeft.setTextColor(getApplicationContext().getResources().getColor(R.color.app_color));
//					txtLeft.setText("有\n序");
					txtLeft.setText(intervalType2Str(data.getIntervalType()));
					iconRight.setImageResource(R.drawable.visit_right_order);
					break;
					
				case VisitWay.ORDER_NO://无序的线路
					setBackgroundResource(R.color.white);
					txtLeft.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_text_square));
					txtLeft.setTextColor(getApplicationContext().getResources().getColor(R.color.app_color));
//					txtLeft.setText("无\n序");
					txtLeft.setText(intervalType2Str(data.getIntervalType()));
					iconRight.setImageResource(R.drawable.visit_right_disorder);
					break;
			}
			//如果未访问的线路的数目大于0就显示出来数目
			if (data.getUnVisitCount() > 0) {
				txtNumber.setVisibility(View.VISIBLE);//设置数目可见
				txtNumber.setText(String.valueOf(data.getUnVisitCount()));//给数目赋值
			}
			else {//如果没有可执行的店面则把数目隐藏
				txtNumber.setVisibility(View.GONE);
			}
		}
	}
	
	private String intervalType2Str(int intervalType){
		String str = "";
		if (intervalType == Constants.VISITWAY_TYPE_EVERY_DAY) {
			str = PublicUtils.getResourceString(this,R.string.day);
		}else if (intervalType == Constants.VISITWAY_TYPE_EVERY_WEEK) {
			str = PublicUtils.getResourceString(this,R.string.attendance_shedul_week);
		}else if (intervalType == Constants.VISITWAY_TYPE_EVERY_MONTH) {
			str = PublicUtils.getResourceString(this,R.string.month);
		}else if (intervalType == Constants.VISITWAY_TYPE_MOUDLE) {
			str = PublicUtils.getResourceString(this,R.string.visit_store1);
		}
		return str;
	}
	
	private String menuName;
	private int menuType;
	private int menuId;

	/**
	 * 初始化实例
	 */
	private RelativeLayout ll_bg;
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.visit_way);
		Intent intent = getIntent();
		menuName = intent.getStringExtra("menuName");
		menuType = intent.getIntExtra("menuType", 0);
		menuId= intent.getIntExtra("menuId", 0);
		float density = getResources().getDisplayMetrics().density;
		ll_bg = (RelativeLayout)findViewById(R.id.ll_way_bg);
//		if (!TextUtils.isEmpty(StyleUtil.homeStyleImg(this))) {
//			Drawable  drawable = Drawable.createFromPath(Constants.COMPANY_STYLE_PATH+StyleUtil.homeStyleImg(this));
//			if (drawable!=null) {
//				ll_bg.setBackgroundDrawable(drawable);
//			}
//		}
		lst = (ListView) findViewById(R.id.list);
		lst.setAdapter(adapter);
//		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)lst.getLayoutParams();
//		lp.addRule(RelativeLayout.BELOW, ID_BAR);
		
//		ViewGroup root = (ViewGroup)lst.getParent();
//		View bar = View.inflate(this, R.layout.titlebar, null);
//		bar.setId(ID_BAR);
//		bar.setPadding((int)(5 * density), 0, (int)(5 * density), 0);
//		titlebar = new TitleBar(this, bar);//初始化 电量 是否在被动定位 信号状态 view
//		titlebar.register();//注册
//		root.addView(bar, LayoutParams.MATCH_PARENT, (int)(20 * density));//将TitleBar添加到父View中

		initBase();
		
		
		
		findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	/**
	 * 返回的时候要重新刷新页面布局
	 */
	@Override
	protected void onStart() {
		super.onStart();
		//先移除原来的旧数据
//		items.clear();
		items = new ArrayList<VisitWay>();
		//根据当前的日期查询可以操作的线路
		List<VisitWay> visitWayList = new VisitWayDB(this).findWayListByStartdate(DateUtil.getCurDateTime());
		//添加数据源
		getWayList(visitWayList);
		
		//如果线路是空，页面显示 "没有可访问线路!" 并把listview隐藏掉
		if (items.isEmpty()) {
			TextView txtEmpty = (TextView) findViewById(R.id.txtEmpty);
			txtEmpty.setVisibility(View.VISIBLE);
			lst.setVisibility(View.GONE);
			//把背景置为黑色
			ViewGroup root = (ViewGroup)lst.getParent();
			root.setBackgroundColor(Color.BLACK);
		}
		else {
			//添加页面布局
			thread.execute(updateUnVisitCount);
		}
	}

	/**
	 * 设置拜访线路数据源
	 * @param list 所有可执行的线路集合
	 */
	private void getWayList(List<VisitWay> list) {
		for (VisitWay way : list) {
			if (isShow(way)) {//如果当前线路可以拜访就添加到要显示的数据集合中
				items.add(way);
			}
		}
	}

	/**
	 * 根据线路的访问类型判断是否显示
	 * 1:表示每天类型
	 * 2:表示每周类型
	 * 3：表示每月类型
	 * 4：表示自定义类型  自定义类型获取开始日期和结束日期
	 * @param way线路的实例
	 * @return 该条线路是否可以显示 true表示可以显示  false表示不可以显示
	 */
	private boolean isShow(VisitWay way) {
		boolean flag = false;
		switch (way.getIntervalType()) {
			case Constants.VISITWAY_TYPE_EVERY_DAY://每天类型
				flag = true;
				break;
			case Constants.VISITWAY_TYPE_EVERY_MONTH://每月类型
				flag = true;
				break;
			case Constants.VISITWAY_TYPE_EVERY_WEEK://每周类型
				Calendar c = Calendar.getInstance();
				// c.setTime(DateUtil.getDate("2012-11-04 00:00:00"));
				int day = c.get(Calendar.DAY_OF_WEEK) - 1;// 获取今天是本周的第几天 从星期一开始
				day = day == 0 ? 7 : day;
				int n = Integer.valueOf(way.getWeekly(), 2) >> (7 - day);
				if (n % 2 == 1) { // 判断今天执行不 1 的话就表示今天执行，0 表示今天不执行
					flag = true;
				}
				else {
					flag = false;
				}
				break;
			case Constants.VISITWAY_TYPE_MOUDLE://自定义类型
				Date start = DateUtil.getDate(way.getFromDate());//自定义类型的开始执行日期
				Date stop = DateUtil.getDate(way.getToDate());//自定义类型的结束执行日期
				Date nowDate = DateUtil.getDate(DateUtil.getCurDate(), DateUtil.DATAFORMAT_STR);
				//获取今天的日期并和开始日期结束日期进行比较，判断今天是否要执行
				if (nowDate.before(start) || nowDate.after(stop)) {
					flag = false;
				}
				else {
					flag = true;
				}
				break;
		}

		return flag;
	}
	
	/**
	 * 更新可以访问的店面的数量
	 */
	private void updateUnVisitCount() {
		int number;
		VisitCycleUtil visitCycleUtil = null;
		for (VisitWay item : items) {
			//默认有0个没有访问的店面
			number = 0;
			//根据线路ID和计划ID查询出该线路下所有店面
			List<VisitStore> visitStoreList = new VisitStoreDB(this).findAllVisitStoreList(item.getWayId(), item.getPlanId());
			//如果集合为空则说明该线路下没有店面，则跳过这次循环
			if (visitStoreList.isEmpty())
				continue;
			//如果集合中有店面 则循环店面集合
			for (int i = 0; i < visitStoreList.size(); i++) {
				VisitStore visitStore = visitStoreList.get(i);
				//查看当前这个店面提交的日期
				String submitDate = visitStore.getSubmitDate();
				if (TextUtils.isEmpty(submitDate)) {//如果日期为空，则说明该店没有提交过访问
					number += item.getVisitCount();
				}else {//否则的话就根据店面的访问类型判断今天能不能访问该店
					visitCycleUtil = new VisitCycleUtil(VisitWayActivity.this);
					visitCycleUtil.setCycleCount(item.getCycleCount());
					visitCycleUtil.setVisitCount(item.getVisitCount());
					visitCycleUtil.setStartDate(item.getStartdate());
					try {
						switch(item.getIntervalType()){
						case Constants.VISITWAY_TYPE_EVERY_DAY:// 每天类型
							if (!visitCycleUtil.dayVisitType(visitStore)) {
								number += (item.getVisitCount() - visitStore.getSubmitNum());
							}
							break;
						case Constants.VISITWAY_TYPE_EVERY_MONTH:// 每月类型
							if (!visitCycleUtil.monthVisitType(visitStore)) {
								number += (item.getVisitCount() - visitStore.getSubmitNum());
							}
							break;
						case Constants.VISITWAY_TYPE_EVERY_WEEK: // 每周类型
							if (!visitCycleUtil.weekVisitType(visitStore)) {
								number += (item.getVisitCount() - visitStore.getSubmitNum());
							}
							break;
						case Constants.VISITWAY_TYPE_MOUDLE:// 自定义类型
							if (!visitCycleUtil.moduleVisitType(visitStore)) {
								number += (item.getVisitCount() - visitStore.getSubmitNum());
							}
							break;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			item.setUnVisitCount(number);//给当前线路设置今天可以访问的店面的数目
		}
	}

	/**
	 * 页面关闭的时候关闭线程池 取消titleBar的注册
	 */
	@Override
	protected void onDestroy() {
		thread.shutdown();//关闭线程池
		if (titlebar != null)//取消titlebar的注册
			titlebar.unregister();
		super.onDestroy();
	}
}
