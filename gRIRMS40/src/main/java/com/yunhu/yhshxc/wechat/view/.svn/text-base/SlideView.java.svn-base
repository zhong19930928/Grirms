package com.yunhu.yhshxc.wechat.view;

import com.yunhu.yhshxc.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

/**
 * 
 * 
 * @author long
 * 
 */
public class SlideView extends LinearLayout implements OnClickListener {
	public static final String TAG = "SlideView";

	private static final int TAN = 2;
	private int mHolderWidth = 80;
	private float mLastX = 0;
	private float mLastY = 0;
	private LinearLayout mViewContent;
	private Scroller mScroller;
	private Context mContext;
	private Resources mResources;
	private boolean count;
//	public boolean isShow = false;
	// 修改================================================================================
	int groupPosition;
	int childPosition;
	private ExpendListViewOnlick mOnClick;

	// private TextView shenhe;

	// public TextView getShenhe() {
	// return shenhe;
	// }

	public TextView getBack() {
		return back;
	}

	private TextView back;

	public SlideView(Context context, Resources resources, View content,
			boolean count, ExpendListViewOnlick mOnClick) {
		super(context);

		this.count = count;
		this.mOnClick = mOnClick;
		initView(context, resources, content);
	}

	public SlideView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context, context.getResources(), null);
	}

	public SlideView(Context context) {
		super(context);
		initView(context, context.getResources(), null);
	}

	@SuppressLint("NewApi")
	private void initView(Context context, Resources resources, View content) {
		setOrientation(LinearLayout.HORIZONTAL);
		this.mContext = context;
		this.mResources = resources;
		mScroller = new Scroller(context);
		View view = LayoutInflater.from(context).inflate(
				resources.getLayout(R.layout.slide_view_merge), this);
		// view.findViewById(R.id.holder).setBackground(
		// resources.getDrawable(R.drawable.selector_slider_holder));
		// shenhe = (TextView) view.findViewById(R.id.shenhe);
		back = (TextView) findViewById(R.id.back);
		// shenhe.setCompoundDrawablesWithIntrinsicBounds(null,
		// resources.getDrawable(R.drawable.pen), null, null);
		// back.setCompoundDrawablesWithIntrinsicBounds(null,
		// resources.getDrawable(R.drawable.back), null, null);
		back.setOnClickListener(this);
		// shenhe.setOnClickListener(this);
		mViewContent = (LinearLayout) view.findViewById(R.id.view_content);
		mHolderWidth = Math.round(TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, mHolderWidth, getResources()
						.getDisplayMetrics()));
		if (content != null) {
			LinearLayout.LayoutParams params = new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			content.setLayoutParams(params);
			mViewContent.addView(content);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (count) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_MOVE:
				float x = event.getX();
				float y = event.getY();
				float deltaX = x - mLastX;
				float deltaY = y - mLastY;
				mLastX = x;
				mLastY = y;
				if (Math.abs(deltaX) < Math.abs(deltaY) * TAN) {
					break;
				}
				if (deltaX != 0) {
					float newScrollX = getScrollX() - deltaX;
					if (newScrollX < 0) {
						newScrollX = 0;
					} else if (newScrollX > mHolderWidth) {
						newScrollX = mHolderWidth;
					}
					this.scrollTo((int) newScrollX, 0);
//					isShow = true;
				}
				break;
			}
		}

		return super.onTouchEvent(event);
	}

	private void smoothScrollTo(int destX, int destY) {
		int scrollX = getScrollX();
		int delta = destX - scrollX;
		mScroller.startScroll(scrollX, 0, delta, 0, Math.abs(delta) * 3);
		invalidate();
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
	}

	/**
	 * 获取view是需要重置缓存状态״̬
	 */
	public void shrink() {
		int offset = getScrollX();
		if (offset == 0) {
			return;
		}
		scrollTo(0, 0);
	}

	public void setContentView(View view) {
		if (mViewContent != null) {
			mViewContent.addView(view);
		}
	}

	public void reset() {
		int offset = getScrollX();
		if (offset == 0) {
			return;
		}
		smoothScrollTo(0, 0);

	}

	public void adjust(boolean left) {
		int offset = getScrollX();
		if (offset == 0) {
			return;
		}
		if (offset < 20) {
			this.smoothScrollTo(0, 0);
		} else if (offset < mHolderWidth - 20) {
			if (left) {
				this.smoothScrollTo(mHolderWidth, 0);
			} else {
				this.smoothScrollTo(0, 0);
			}
		} else {
			this.smoothScrollTo(mHolderWidth, 0);
		}
	}

	@Override
	public void onClick(View v) {
		// if (v == shenhe) {
		// Toast.makeText(mContext, "点击1", 0).show();
		// } else if (v == back) {
		// Toast.makeText(mContext, "点击2", 0).show();
		// }
		if (mOnClick != null) {
			mOnClick.expendOnclick(groupPosition, childPosition);
		}
	}

	public interface ExpendListViewOnlick {
		public void expendOnclick(int groupPosition, int childPosition);
	}

	public void setPostion(int grouppostion, int childposition) {
		this.childPosition = childposition;
		this.groupPosition = grouppostion;
	}
}
