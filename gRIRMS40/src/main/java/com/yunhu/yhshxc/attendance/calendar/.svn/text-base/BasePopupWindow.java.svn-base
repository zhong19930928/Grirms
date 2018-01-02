package com.yunhu.yhshxc.attendance.calendar;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.utility.PublicUtils;

public abstract class BasePopupWindow extends PopupWindow {
	
	private View contentView;
	private Context context;

	public BasePopupWindow(Context context) {
		super(context);
		this.context = context;
		init();
	}

	private void init(){

		contentView = createView();
        this.setContentView(contentView);  
        this.setWidth(LayoutParams.MATCH_PARENT);  
        this.setHeight(LayoutParams.WRAP_CONTENT);  
        //设置PopupWindow弹出窗体可点击  
        this.setFocusable(true);  
        //设置弹出窗体动画效果  
        this.setAnimationStyle(R.style.PopupWindowAnimation);
        //实例化一个ColorDrawable颜色为半透明  
        ColorDrawable dw = new ColorDrawable(0xb0000000);  
        //设置SelectPicPopupWindow弹出窗体的背景  
        this.setBackgroundDrawable(dw);
        this.setOutsideTouchable(true);
	}
	
	public abstract View createView();
	
	/**
	 * 显示PopupWindow
	 * 注意，使用此方法，根布局中的必须设置R.id.main。
	 */
	public void show(){
		View view = ((Activity)getContext()).findViewById(R.id.main);
		if(view == null){
			throw new NullPointerException(PublicUtils.getResourceString(context,R.string.end_service2)+"R.id.main");
		}
		showAtLocation(view,Gravity.BOTTOM,0,0);  
	}

	public Context getContext() {
		return context;
	}
	
	public View getContentView() {
		return contentView;
	}
}
