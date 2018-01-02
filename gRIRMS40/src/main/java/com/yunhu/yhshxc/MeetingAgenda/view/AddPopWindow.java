package com.yunhu.yhshxc.MeetingAgenda.view;

import android.app.Activity;
import android.view.View;
import android.widget.PopupWindow;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.utility.PublicUtils;

/**
 * Created by YH on 2017/8/31.
 */

public class AddPopWindow extends PopupWindow{
    public AddPopWindow(final Activity context, View conentView,int height){
//        int h = context.getWindowManager().getDefaultDisplay().getHeight();
//        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        int px = PublicUtils.convertDIP2PX(context, 150);
//        int h=PublicUtils.convertDIP2PX(context, 240);
        this.setWidth(px);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(height);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.index_bgpop));
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimationPreview);
    }



    /**
     * 显示popupWindow
     *
     * @param
     */
    public void showPopupWindow(View addview) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAsDropDown(addview, 175, 42);
        } else {
            this.dismiss();
        }
    }
}
