package com.yunhu.yhshxc.wechat.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ListView;

public class WeChatListView extends ListView{

	public WeChatListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initData(context);
	}

	public WeChatListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initData(context);
	}

	public WeChatListView(Context context) {
		super(context);
		initData(context);
	}

	/**
	 * 用户滑动最小距离
	 */
	private int touchSlop;
	/**
	 * 是否相应滑动
	 */
	private boolean isSliding;
	/**
	 * 手指按下时x坐标
	 */
	private int xDown;
	/**
	 * 手指按下时的y坐标
	 */
	private int yDown;
	/**
	 * 手指移动时的x坐标
	 */
	private int xMove;
	/**
	 * 手指移动时的y坐标
	 */
	private int yMove;

	private LayoutInflater mInflater;

	private ViewGroup itemLayout;

	private SlideView mFocusedItemView;

	/**
	 * 当前手指触摸的View
	 */

	/**
	 * 当前手指触摸的位置
	 */
	private int mCurrentViewPos = -1;

	private void initData(Context context) {
		mInflater = LayoutInflater.from(context);
		touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
	}
	private boolean isBack = false;
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		int x = (int) ev.getX();
		int y = (int) ev.getY();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			xDown = x;
			yDown = y;
			// 获得当前手指按下时的item的位置
						isBack = false;
			int position = pointToPosition(xDown, yDown);
			if (isSliding) {
				isSliding = false;
				if (mFocusedItemView != null) {
					isBack = true;
					mFocusedItemView.reset();
				}
			}
			mCurrentViewPos = position;
			// 获得当前手指按下时的item
			break;
		case MotionEvent.ACTION_MOVE:
			if (isBack) {
				return true;
			}
			xMove = x;
			yMove = y;
			int dx = xMove - xDown;
			int dy = yMove - yDown;
			/**
			 * 判断是否是从右到左的滑动
			 */
			if (xMove < xDown && Math.abs(dx) > touchSlop
					&& Math.abs(dy) < touchSlop) {
				isSliding = true;
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		int x = (int) ev.getX();
		int y = (int) ev.getY();
		/**
		 * 如果是从右到左的滑动才相应
		 */
		if (isSliding) {
			switch (action) {
			case MotionEvent.ACTION_MOVE:
				if (mCurrentViewPos != -1) {
					if (Math.abs(yDown - y) < 30 && Math.abs(xDown - x) > 20) {
						int first = this.getFirstVisiblePosition();
						int index = mCurrentViewPos - first;
						mFocusedItemView = (SlideView) getChildAt(index);
						mFocusedItemView.onTouchEvent(ev);
						return true;
					}
				}

				break;
			case MotionEvent.ACTION_UP:
				if (isSliding) {
					// isSliding = false;
					if (mFocusedItemView != null) {
						mFocusedItemView.adjust(xDown - x > 0);
						return true;
					}
				}
			}
			// 相应滑动期间屏幕itemClick事件，避免发生冲突
			return true;
		}else{
			if (isBack) {
				// 如果此次为复位 item不响应任何事件
				return true;
			}
		}
		// 如果item正在显示 则取消显示
//		if (mFocusedItemView != null && mFocusedItemView.isShow) {
//			switch (action) {
//			case MotionEvent.ACTION_UP:
//				mFocusedItemView.isShow = false;
//				return true;
//			}
//		}
		return super.onTouchEvent(ev);
	}

}
