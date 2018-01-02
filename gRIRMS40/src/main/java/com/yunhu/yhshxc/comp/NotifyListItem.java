package com.yunhu.yhshxc.comp;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yunhu.yhshxc.R;

/**
 * 封装的每一条公告的view
 * @author jishen
 *
 */
public class NotifyListItem implements OnGestureListener,OnTouchListener {

	private Context mContext;
	private View view;
	private Button iv_del;//删除按钮
	private LinearLayout noticelistItemLayout;
	private PopupWindow popupWindow; //删除popupWindow
	
	public Button getIv_del() {
		return iv_del;
	}

	private TextView tv_title;//标题
	private TextView tv_content;//内容
	private TextView tv_data;//日期
	private TextView tv_isRead;//已读未读状态
	private TextView tv_createUser;//下发人

	public void setIsRead(boolean flag) {
		if(!flag){//未读
			this.tv_title.getPaint().setFakeBoldText(true);
			this.tv_data.getPaint().setFakeBoldText(true);
			this.tv_createUser.getPaint().setFakeBoldText(true);
//			this.tv_content.setTextColor(mContext.getResources().getColor(R.color.notice_detail_title));
			tv_content.setVisibility(View.GONE);
			
		}
	}

	private GestureDetector detector;

	/**
	 * 初始化
	 * @param context
	 */
	public NotifyListItem(Context context) {
		mContext = context;
		detector = new GestureDetector(this);
		view = View.inflate(context, R.layout.notify_list_item, null);
		noticelistItemLayout=(LinearLayout)view.findViewById(R.id.notify_list_item);
		iv_del = (Button) view.findViewById(R.id.notify_list_item_delete);
		tv_title = (TextView) view.findViewById(R.id.notify_list_item_title);
		tv_content = (TextView) view.findViewById(R.id.notify_list_item_content);
		tv_data = (TextView) view.findViewById(R.id.notify_list_item_data);
		tv_isRead = (TextView) view.findViewById(R.id.notify_list_item_isRead);
		tv_createUser = (TextView) view.findViewById(R.id.notify_list_item_creater);
		view.setOnTouchListener(this);
		view.setLongClickable(true);
	}

	//按下手指
	@Override
	public boolean onDown(MotionEvent e) {
		setNoticeListItemBackground(R.color.notice_item_bg_press);
		return false;
	}

	//按下手指
	@Override
	public void onShowPress(MotionEvent e) {
		setNoticeListItemBackground(R.color.notice_item_bg_press);
	}

	//抬起手指
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		setNoticeListItemBackground(R.color.white);
		return false;
	}

	//上下滑动
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		if(e2.getAction()==MotionEvent.ACTION_CANCEL || e2.getAction()==MotionEvent.ACTION_UP){
			setNoticeListItemBackground(R.color.white);
		}else{
			setNoticeListItemBackground(R.color.notice_item_bg_press);
		}
		return false;
	}

	/**
	 * 长按
	 */
	@Override
	public void onLongPress(MotionEvent e) {
		setNoticeListItemBackground(R.color.notice_item_bg_press);

	}

	/**
	 * 左右滑动
	 */
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if(e2.getAction()==MotionEvent.ACTION_CANCEL || e2.getAction()==MotionEvent.ACTION_UP){
			setNoticeListItemBackground(R.color.white);
		}else{
			setNoticeListItemBackground(R.color.notice_item_bg_press);
		}
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
	
	public void setCreater(String name){
		this.tv_createUser.setText(name);
	}
	
	/**
	 * 触摸
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(event.getAction()==MotionEvent.ACTION_CANCEL || event.getAction()==MotionEvent.ACTION_UP){
			setNoticeListItemBackground(R.color.white);
		}else{
			setNoticeListItemBackground(R.color.notice_item_bg_press);
			
		}
		return detector.onTouchEvent(event);
	}

	/**
	 * 删除按钮出现和消失的动画
	 */
	private void isShowDelete(){
		if(this.iv_del.isShown()){
			this.iv_del.setVisibility(View.INVISIBLE);
			Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_right);
			this.iv_del.setAnimation(animation);
		}else{
			this.iv_del.setVisibility(View.VISIBLE);
			Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_left);
			this.iv_del.setAnimation(animation);
			setNoticeListItemBackground(R.color.white);
		}
	}
	
	public void setNoticeListItemBackground(int id){
		noticelistItemLayout.setBackgroundResource(id);
	}
}
