package com.yunhu.yhshxc.dialog;

import android.content.Context;
import android.os.Bundle;

import com.yunhu.yhshxc.bo.LocationResult;

public abstract class AbsLocationDialog {
	public boolean isShowOnResume = false;//在onResume方法中是不是要弹出dialog true弹出，false不弹出
	protected Context mContext;
	protected LocationDialogListener locationDialogListener;
	protected LocationResult locationResult;
	public boolean isNeedShowDialog = true;//定位成功以后是否在弹出dialog
	
	public AbsLocationDialog(Context context,LocationResult locationResult,LocationDialogListener listener) {
		this.mContext = context;
		this.locationDialogListener = listener;
		this.locationResult = locationResult;
	}
	/**
	 * 初始化dialog页面
	 */
	public abstract void initDialogView();
	/**
	 * 初始化检测类型的定位页面
	 * @param isInDistance 是否在规定范围内 true是 false不是
	 * @param bundle 传递验证参数
	 */
	public abstract void initCheckDialogView(boolean isInDistance,Bundle bundle);
	public abstract void showLocationDialog();

	
}
