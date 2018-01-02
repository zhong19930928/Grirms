package com.yunhu.yhshxc.activity;

import gcg.org.debug.JLog;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.adapter.TaskListAdapter;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.bo.Task;
import com.yunhu.yhshxc.database.MainMenuDB;
import com.yunhu.yhshxc.database.TaskDB;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.widget.ToastOrder;

/**
 * 任务列表
 * 
 */
public class TaskListActivity extends AbsBaseActivity implements OnScrollListener {
	private ListView taskListView;
	private View loadingView;// 分页加载视图View
	private List<Task> taskList;
	public TaskListAdapter taskListAdapter;
	private int lastItem = 0;// listView分页
	private int moduleId;
	private String taskName;
	private TaskDB taskDB;
	private TextView taskNameTV;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_list);
		initBase();
	}

	
	@Override
	protected void onStart() {
		super.onStart();
		init();
	}
	private void init() {
		taskDB=new TaskDB(this);
		moduleId=getIntent().getIntExtra("moduleId", Constants.DEFAULTINT);
		Menu currentMenu=new MainMenuDB(this).findMenuListByMenuId(moduleId);
//		taskName=getIntent().getStringExtra("taskName");
		taskNameTV=(TextView)findViewById(R.id.tv_taskName);
		taskNameTV.setText(currentMenu.getName());
		loadingView = LayoutInflater.from(this).inflate(R.layout.notify_list_loading, null);
		taskListView = (ListView) findViewById(R.id.task_list_listView);
		taskList = taskDB.findAllTaskByModuleid(moduleId);
//		if (taskList.isEmpty()) {
//			Toast.makeText(this, getResources().getString(R.string.noTask),Toast.LENGTH_SHORT).show();
//			this.finish();
//		} else {
			if (taskListView.getFooterViewsCount()==0 && taskList.size()>=20) {
				taskListView.addFooterView(loadingView);// 添加加载视图
			}
//		}
		taskListAdapter = new TaskListAdapter(this, taskList);
		taskListAdapter.setModuleId(moduleId);
		taskListView.setAdapter(taskListAdapter);
		taskListView.setOnScrollListener(this);
//		httpHelper=new CoreHttpHelper(this,resultHandler);
	}

	/**
	 * 分页
	 */

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		List<Task> tempList = taskListAdapter.getTaskList();
		if (lastItem == tempList.size()&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
			try {
				Task item = tempList.get(tempList.size() - 1);
				String date=item.getCreateTime();
				int moduleid=item.getModuleid();
				getTask(date,moduleid);
				JLog.d(TAG, "date===>"+date);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 分页
	 */
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		lastItem = firstVisibleItem + visibleItemCount - 1;
		if (totalItemCount < 21 && totalItemCount > 1) {
			taskListView.removeFooterView(loadingView);
		}
	}

	
	
	
	/**
	 * 获取心的人物
	 * @param date
	 * @param moduleid
	 */
	private void getTask(final String date, final int moduleid) {
		List<Task> newList = taskDB.findTaskByDateAndModuleid(date, moduleid);// 解析拿到的公告
		if (!newList.isEmpty()) {
			List<Task> itemList = taskListAdapter.getTaskList();
			itemList.addAll(newList);
			taskListAdapter.setTaskList(itemList);
			JLog.d(TAG, "itemListSize==> "+taskListAdapter.getTaskList().size());
			if (itemList.size() < 20) {
				taskListView.removeFooterView(loadingView);
			}
			taskListAdapter.notifyDataSetChanged();
		} else {
			taskListView.removeFooterView(loadingView);
			taskListAdapter.notifyDataSetChanged();
			ToastOrder.makeText(TaskListActivity.this, R.string.un_search_data4, 1).show();
		}
		JLog.d(TAG, "getTaskThreadStart");
	}
	
	
	
	/*
	 * 返回按钮点击事件
	 */
	@Override
	public void onClick(View v) {
		super.onClick(v);
		TaskListActivity.this.finish();
	}

	
	public void showDeleteDialog(final int pos,final Task task){
		JLog.d(TAG, "DELETEPOS==> "+pos);
		JLog.d(TAG, "taskListSize==> "+taskList.size());
		View contentView = View.inflate(this, R.layout.delete_prompt, null);
		final PopupWindow popupWindow = new PopupWindow(contentView,WindowManager.LayoutParams.FILL_PARENT,WindowManager.LayoutParams.FILL_PARENT,true);
		popupWindow.setAnimationStyle(R.style.popupwindow);
		int high=getWindowManager().getDefaultDisplay().getHeight();
//		popupWindow.showAsDropDown(taskListView,0,-(Constants.SCREEN_HEIGHT/5+15));
		popupWindow.showAsDropDown(taskListView,0,-(high/5+15));
		contentView.findViewById(R.id.btn_confirm).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
				taskList.remove(pos);
				taskDB.deleteTaskById(task.getId());
				taskListAdapter.notifyDataSetChanged();
			}
		});
		contentView.findViewById(R.id.btn_canceled).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});
	}
	
	
	@Override
	protected void onStop() {
		super.onStop();
		if (taskListView.getFooterViewsCount() == 1) {
			taskListView.removeFooterView(loadingView);
		}
	}
}
