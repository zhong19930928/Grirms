package com.yunhu.yhshxc.wechat.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;

/**
 * zjl 2:21:20
 * 
 * @author long
 *
 */
public class MyExpandableListView extends ExpandableListView {

	private static final String TAG = "MyExpandableListView";
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

	boolean isChild;

	private LayoutInflater mInflater;

	private ViewGroup itemLayout;

	private SlideView mFocusedItemView;

	/**
	 * 为删除按钮提供一个回调接口
	 */
	private ButtonClickListener mListener;
	/**
	 * 当前手指触摸的View
	 */
	/**
	 * 当前手指触摸的位置
	 */
	private int mCurrentViewPos = -1;
	@Override
	/**
	 * 重写该方法，达到使ListView适应ScrollView的效果
	 */
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
	public MyExpandableListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		initData(context);
	}

	public MyExpandableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initData(context);
	}

	public MyExpandableListView(Context context) {
		super(context);
		initData(context);
	}

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
			isBack = false;
			yDown = y;
			int position = pointToPosition(xDown, yDown);
			// 只要是显示 就复位 复位则 所有事件ListViewItem不响应
			if (isSliding) {
				isSliding = false;
				if (mFocusedItemView != null) {
					isBack = true;
					mFocusedItemView.reset();
				}
			}
			mCurrentViewPos = position;
			itemLayout = (ViewGroup) getChildAt(mCurrentViewPos
					- getFirstVisiblePosition());
			if (itemLayout instanceof RelativeLayout) {
				isChild = false;
			} else {
				isChild = true;
			}

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
			 *判断是否是从右到左的滑动
			 */
			if (xMove < xDown && Math.abs(dx) > touchSlop
					&& Math.abs(dy) < touchSlop && isChild) {
				isSliding = true;
			}
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		int x = (int) ev.getX();
		int y = (int) ev.getY();
		/**
		 * 如果是从右到左的滑动才响应
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
					if (mFocusedItemView != null) {
						mFocusedItemView.adjust(xDown - x > 0);
						return true;
					}
				}
			}
			//相应滑动期间屏幕itemClick事件，避免发生冲突
			return true;
		} else {
			if (isBack) {
				// 如果此次为复位 item不响应任何事件
				return true;
			}
		}
		return super.onTouchEvent(ev);
	}

	public void setButtonClickListener(ButtonClickListener listener) {
		mListener = listener;
	}

	interface ButtonClickListener {
		public void clickHappend(int position);
	}
}
