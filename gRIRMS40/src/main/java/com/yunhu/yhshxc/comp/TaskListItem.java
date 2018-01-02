package com.yunhu.yhshxc.comp;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;

public class TaskListItem implements OnGestureListener,OnTouchListener {

	private Context mContext;
	private View view;
	private ImageView iv_del;
	private LinearLayout tasklistItemLayout;
	
	public ImageView getIv_del() {
		return iv_del;
	}

	private TextView tv_title;
	private TextView tv_content;
	private TextView tv_data;
	private TextView tv_isRead;

	public void setIsRead(String tv_isRead) {
		this.tv_isRead.setText(tv_isRead);
	}

	private GestureDetector detector;

	public TaskListItem(Context context) {
		mContext = context;
		detector = new GestureDetector(this);
		view.setOnTouchListener(this);
		view.setLongClickable(true);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		setTaskListItemBackground(R.color.orange);
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		setTaskListItemBackground(R.color.task_list);
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		setTaskListItemBackground(R.color.task_list);
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		setTaskListItemBackground(R.color.task_list);
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		setTaskListItemBackground(R.color.orange);

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		setTaskListItemBackground(R.color.task_list);
		if (e1.getX() - e2.getX() > 5 || e1.getX() - e2.getX() < -5) {
			isShowDelete();
			return true;
		}
		return false;
	}

	public View getView() {
		return view;
	}

	public void setTitle(String title) {
		this.tv_title.setText(title);
	}

	public void setContent(String content) {
		this.tv_content.setText(content);
	}
	
	public void setDate(String tv_data) {
		this.tv_data.setText(tv_data);
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		setTaskListItemBackground(R.color.task_list);
		return detector.onTouchEvent(event);
	}

	private void isShowDelete(){
		if(this.iv_del.isShown()){
			this.iv_del.setVisibility(View.INVISIBLE);
		}else{
			this.iv_del.setVisibility(View.VISIBLE);
			setTaskListItemBackground(R.color.task_list);
		}
	}
	
	public void setTaskListItemBackground(int id){
		tasklistItemLayout.setBackgroundResource(id);
	}
}
