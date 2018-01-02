package com.yunhu.yhshxc.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class GCGScrollView extends ScrollView{

	   //mViewTouchMode表示ViewPager是否全权控制滑动事件，默认为false，即不控制
    private boolean mViewTouchMode = false;
    
    public void setViewTouchMode(boolean b) {
        mViewTouchMode = b;
    }
    
	public GCGScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public GCGScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public GCGScrollView(Context context) {
		super(context);
	}

	  /**
     * 在mViewTouchMode为true的时候，ViewPager不拦截点击事件，点击事件将由子View处理
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (mViewTouchMode) {
            return false;
        }
        return super.onInterceptTouchEvent(event);
    }
	
	
}
