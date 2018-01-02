package com.yunhu.android.view.loopview;

import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by zhonghuibin
 * on 2017/09/01.
 */
final class LoopViewGestureListener extends android.view.GestureDetector.SimpleOnGestureListener {

    final LoopView loopView;

    LoopViewGestureListener(LoopView loopview) {
        loopView = loopview;
    }

    /**
     *@date2017/9/1
     *@author zhonghuibin
     *@description 自由滚动时才执行此方法
     */
    @Override
    public final boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.e("LoopViewGestureListener", "velocityY== "+velocityY );
        loopView.scrollBy(velocityY);
        return true;
    }
}
