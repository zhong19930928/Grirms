package com.yunhu.yhshxc.adapter;


import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.TaskDetailActivity;
import com.yunhu.yhshxc.activity.TaskListActivity;
import com.yunhu.yhshxc.bo.Task;
import com.yunhu.yhshxc.comp.TaskListItem;
import com.yunhu.yhshxc.utility.PublicUtils;

/**
 * 
 * @author jishen 任务列表适配器
 */
public class TaskListAdapter extends BaseAdapter {
	private Context context;
	private List<Task> taskList;
	private int currentPos;
	private int moduleId;

	public void setModuleId(int moduleId) {
		this.moduleId = moduleId;
	}

	public TaskListAdapter(Context context, List<Task> taskList) {
		this.context = context;
		this.taskList = taskList;
	}

	public int getCount() {
		return taskList.size();
	}

	public Object getItem(int pos) {
		return 0;
	}

	public long getItemId(int pos) {
		return pos;
	}

	public View getView(final int pos, View v, ViewGroup p) {
		final Task currentTask = taskList.get(pos);
		TaskListItem item=new TaskListItem(context);
		View currentView=item.getView();
		item.setTitle(currentTask.getTaskTitle());
		
		if((!TextUtils.isEmpty(currentTask.getDetailTask()))&&(currentTask.getDetailTask().length()>20)){
			item.setContent(currentTask.getDetailTask().substring(0,20)+"...");
		}else{
			item.setContent(currentTask.getDetailTask());
		}
//		item.setContent(currentTask.getDetailTask());
		item.setDate(currentTask.getCreateTime());
		if(currentTask.getIsread()==Task.ISREAD_N){
			item.setIsRead(PublicUtils.getResourceString(context, R.string.un_read));
		}
		item.getIv_del().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				v.setEnabled(false);
				((TaskListActivity)context).showDeleteDialog(pos,currentTask);
				v.setEnabled(true);
			}
		});
		//跳转到双向详细页面
		currentView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				v.setEnabled(false);
				currentPos=pos;
				Task task = taskList.get(pos);
				Bundle bundle = new Bundle();
				if (task.getId() != null) {
					bundle.putInt("currentTaskId", task.getId());
				}else{
					bundle.putInt("currentTaskId", -1);
				}
				
				bundle.putString("currentTaskTitle", task.getTaskTitle());
				bundle.putString("currentTaskDetail", task.getDetailTask());
				bundle.putString("currentTaskData", task.getCreateTime());
				bundle.putInt("moduleId", moduleId);
				Intent intent = new Intent(context,TaskDetailActivity.class);
				intent.putExtra("currentTask", bundle);
				context.startActivity(intent);
				v.setEnabled(true);
			}
		});
		return currentView;
	}

	
	
	
	public List<Task> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<Task> taskList) {
		this.taskList = taskList;
	}
	
	public int getPos(){
		return currentPos;
	}
}
