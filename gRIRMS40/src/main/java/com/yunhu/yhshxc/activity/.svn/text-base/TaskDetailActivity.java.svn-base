package com.yunhu.yhshxc.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.bo.Task;
import com.yunhu.yhshxc.database.MainMenuDB;
import com.yunhu.yhshxc.database.TaskDB;
import com.yunhu.yhshxc.utility.Constants;

/**
 * 
 * @author jishen 任务列表的详细信息
 * 
 */
public class TaskDetailActivity extends AbsBaseActivity{
	private TextView taskDetailTitle, taskDetailContent,taskDetailData;
	private ImageView taskDetailDelete;
	private int currentTaskId;
	private TaskDB taskDB;
	private PopupWindow popupWindow;
	private TextView headTV;
	private int moduleId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notify_list);
		initBase();
		taskDB=new TaskDB(this);
//		taskDetailTitle = (TextView) findViewById(R.id.task_detail_title);
//		taskDetailContent = (TextView) findViewById(R.id.task_detail_content);
//		taskDetailData = (TextView) findViewById(R.id.task_list_detail_data);
//		taskDetailDelete = (ImageView) findViewById(R.id.task_detail_delete);
//		headTV=(TextView)findViewById(R.id.taskDetail_head);
		taskDetailDelete.setOnClickListener(this);
		bindData();
	}

	/**
	 * 给详细信息组件绑定数据
	 */
	private void bindData() {
		Bundle bundle = getIntent().getBundleExtra("currentTask");
		taskDetailTitle.setText(bundle.getString("currentTaskTitle"));
		taskDetailContent.setText(bundle.getString("currentTaskDetail"));
		taskDetailData.setText(bundle.getString("currentTaskData"));
		currentTaskId = bundle.getInt("currentTaskId");
		taskDB.updateTaskReadStateById(currentTaskId, Task.ISREAD_Y);
		moduleId=bundle.getInt("moduleId");
		Menu currentMenu=new MainMenuDB(this).findMenuListByMenuId(moduleId);
		headTV.setText(currentMenu.getName());
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 * 点击删除按钮删除该条信息
	 */

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.notify_detail_delete:
			v.setEnabled(false);
			showDeleteDialog();
			v.setEnabled(true);
			break;
		default:
			break;
		}

	}

	/*
	 * 删除该条公告
	 */
	private void deleteTask(int id) {
		if(id!=-1){
			taskDB.deleteTaskById(id);
		}
		TaskDetailActivity.this.finish();
	}
	
	/**
	 * 显示删除dialog
	 */
	private void showDeleteDialog(){
		View contentView = View.inflate(this, R.layout.delete_prompt, null);
		popupWindow = new PopupWindow(contentView,WindowManager.LayoutParams.FILL_PARENT,WindowManager.LayoutParams.FILL_PARENT,true);
		popupWindow.setAnimationStyle(R.style.popupwindow);
		popupWindow.showAsDropDown(taskDetailDelete,0,-(Constants.SCREEN_HEIGHT/5));
		contentView.findViewById(R.id.btn_confirm).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
				deleteTask(currentTaskId);
			}
		});
		contentView.findViewById(R.id.btn_canceled).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});
	}
}
