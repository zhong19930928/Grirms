package com.yunhu.yhshxc.activity.synchrodata;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.todo.Todo;
import com.yunhu.yhshxc.activity.todo.TodoListActivity;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.database.MainMenuDB;
import com.yunhu.yhshxc.database.TablePendingDB;
import com.yunhu.yhshxc.database.TodoDB;
import com.yunhu.yhshxc.help.HelpPopupWindow;
import com.yunhu.yhshxc.submitManager.SubmitManagerActivity;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.SlidingMenu.OnOpenListener;

public class NavigationMenu implements OnClickListener{
	/**
	 * 侧滑菜单
	 */
	
	private SlidingMenu slidingMenu;
	private LinearLayout slidingItem;
	private Context context;
	
	public NavigationMenu(Context context) {
		this.context = context;
	}
	
	public void initNavigationMenu() {
		slidingMenu = new SlidingMenu(context);
		slidingMenu.setMode(SlidingMenu.LEFT);
		slidingMenu.setBehindWidthRes(R.dimen.slidingmenu_offset);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		slidingMenu.setFadeDegree(0.35f);
		slidingMenu.attachToActivity((Activity)context, SlidingMenu.SLIDING_CONTENT);
		slidingMenu.setMenu(R.layout.slidingmenu_view);
		slidingMenu.toggle();
		slidingMenu.showMenu();
		slidingMenu.showContent();
		slidingMenu.setOnOpenListener(new OnOpenListener() {
			
			@Override
			public void onOpen() {
				initMenuView();
			}
		});
		
	}
	
	private void initMenuView() {
		List<NavigationItem> list = new ArrayList<NavigationItem>();
		NavigationItem syncItem = new NavigationItem();
		syncItem.setName(PublicUtils.getResourceString(context,R.string.asyncdata));
		syncItem.setType(NAVIGATION_TYPE_SYNC);
		list.add(syncItem);
		
		NavigationItem managerItem = new NavigationItem();
		managerItem.setName(PublicUtils.getResourceString(context,R.string.person_info_unsubmit));
		managerItem.setType(NAVIGATION_TYPE_MANAGER);
		int unSubmitNumbers = unSubmitNumbers();
		if (unSubmitNumbers>0) {
			managerItem.setNumber(unSubmitNumbers);
		}
		list.add(managerItem);
		
		if (isHasMenu(Menu.TYPE_TODO)) {
			NavigationItem todoItem = new NavigationItem();
			todoItem.setName(PublicUtils.getResourceString(context,R.string.person_info_approve));
			int toDoNumbers = toDoNumbers();
			if (toDoNumbers>0) {
				todoItem.setNumber(toDoNumbers);
			}
			todoItem.setType(NAVIGATION_TYPE_TODO);
			list.add(todoItem);
		}
		
		
		if (isHasMenu(Menu.TYPE_HELP)) {
			NavigationItem helpItem = new NavigationItem();
			helpItem.setName(PublicUtils.getResourceString(context,R.string.center_po7));
			helpItem.setType(NAVIGATION_TYPE_HELP);
			list.add(helpItem);
		}
		addViews(list);
	}
	
	/**
	 * 判断是否有type类型的menu
	 * @return
	 */
	private boolean isHasMenu(int type){
		Menu menu = new MainMenuDB(context).findMenuListByMenuType(type);
		boolean flag = menu == null ? false:true;
		return flag;
	}
	
	
	/**
	 * 未提交的数据条数
	 */
	private int unSubmitNumbers(){
		int number = new TablePendingDB(context).tablePendingCount();
		return number;
	}
	
	/**
	 * 待办事项条数
	 */
	private int toDoNumbers(){
		int number = 0;
		List<Todo> list = new TodoDB(context).findAllTodo();
		for (int i = 0; i < list.size(); i++) {
			Todo todo = list.get(i);
			number += todo.getTodoNum();
		}
		return number;
	}
	
	private void addViews(List<NavigationItem> itemList) {
		slidingItem = (LinearLayout) ((Activity) context).findViewById(R.id.lin);
		slidingItem.removeAllViews();
		for (int i = 0; i < itemList.size(); i++) {
			View v = LayoutInflater.from(context).inflate(R.layout.slidingmenu_item_view, null);
			TextView tv1 = (TextView) v.findViewById(R.id.tv1);
			TextView tv2 = (TextView) v.findViewById(R.id.tv2);
			NavigationItem item = itemList.get(i);
			if(item!=null){
				int number = item.getNumber();
				if(number>0){
					tv2.setVisibility(View.VISIBLE);
					tv2.setText(String.valueOf(number));
				}else{
					tv2.setVisibility(View.INVISIBLE);
				}
			}
			tv1.setText(item.getName());
			v.setTag(item.getType());
			v.setOnClickListener(this);
			slidingItem.addView(v);
		}
		
	}

	@Override
	public void onClick(View v){
		int i = (Integer) v.getTag();
		if (slidingMenu!=null) {
			slidingMenu.showContent();
		}
		switch (i) {
		case NAVIGATION_TYPE_SYNC://同步数据
//			Intent intent = new Intent(context,SyncDataActivity.class);
//			context.startActivity(intent);
			syncAll();
			break;
		case NAVIGATION_TYPE_MANAGER://未提交数据管理
			Intent intent1 = new Intent(context, SubmitManagerActivity.class);
			context.startActivity(intent1);
			break;
		case NAVIGATION_TYPE_TODO://待审批事项
			Intent intent2 = new Intent(context, TodoListActivity.class);
			context.startActivity(intent2);
			break;
		case NAVIGATION_TYPE_HELP://帮助
			new HelpPopupWindow(context).show(null);//基于home页的GridView组件显示弹出窗口
			break;

		}
	}
	
	private void syncAll(){
		int unSumitCount = new TablePendingDB(context).tablePendingCountNotErrorServer();
		if (unSumitCount>0) {
			ToastOrder.makeText(context, R.string.un_submit_data, ToastOrder.LENGTH_SHORT).show();
		}else{
			new SyncInitData(context).syncAll();
		}
	}
	
	
	private final int NAVIGATION_TYPE_SYNC = 1;//同步数据
	private final int NAVIGATION_TYPE_MANAGER = 2;//未提交数据
	private final int NAVIGATION_TYPE_TODO = 3;//待审批
	private final int NAVIGATION_TYPE_HELP = 4;//帮助
	class NavigationItem{
		private String name;
		private int type;
		private int number;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getType() {
			return type;
		}
		public void setType(int type) {
			this.type = type;
		}
		public int getNumber() {
			return number;
		}
		public void setNumber(int number) {
			this.number = number;
		}
		
		
		
	}
}
