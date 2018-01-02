package com.yunhu.yhshxc.wechat.exchange;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import com.yunhu.yhshxc.R;

public class MyViewFilpper extends ViewFlipper implements OnGestureListener {

	Animation leftInAnimation;
	Animation leftOutAnimation;
	Animation rightInAnimation;
	Animation rightOutAnimation;
	Animation topInAnimation;

	private Context context;
	private GestureDetector detector; // 手势检测

	public MyViewFilpper(Context context) {
		super(context);
		// TODO Auto-generated constructor stub

		this.context = context;
		leftInAnimation = AnimationUtils.loadAnimation(context, R.anim.left_in);
		leftOutAnimation = AnimationUtils.loadAnimation(context,
				R.anim.left_out);
		rightInAnimation = AnimationUtils.loadAnimation(context,
				R.anim.right_in);
		rightOutAnimation = AnimationUtils.loadAnimation(context,
				R.anim.right_out);
		detector = new GestureDetector(this);
	}

	public MyViewFilpper(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
		leftInAnimation = AnimationUtils.loadAnimation(context, R.anim.left_in);
		leftOutAnimation = AnimationUtils.loadAnimation(context,
				R.anim.left_out);
		rightInAnimation = AnimationUtils.loadAnimation(context,
				R.anim.right_in);
		rightOutAnimation = AnimationUtils.loadAnimation(context,
				R.anim.right_out);
		detector = new GestureDetector(this);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return this.detector.onTouchEvent(event); // touch事件交给手势处理。
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		Log.d("abby", "执行onFling" + (e1.getX() - e2.getX()));
		if (e1.getX() - e2.getX() > 120) {
			setInAnimation(leftInAnimation);
			setOutAnimation(leftOutAnimation);
			showNext();// 向右滑动
			return true;
		} else if (e1.getX() - e2.getX() < -120) {
			Log.d("abby", "执行onFling向左滑动开始");
			setInAnimation(rightInAnimation);
			setOutAnimation(rightOutAnimation);
			showPrevious();// 向左滑动
			Log.d("abby", "执行onFling向左滑动开始");
			return true;
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
}
