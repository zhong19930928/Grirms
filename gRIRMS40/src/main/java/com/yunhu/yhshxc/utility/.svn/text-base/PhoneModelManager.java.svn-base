package com.yunhu.yhshxc.utility;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.kt40.KT40ScanActivity;
import com.yunhu.yhshxc.s200.ScanS200Activity;
import com.google.zxing.activity.CaptureActivity;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.widget.Toast;

/**
 * 手机型号管理适配类,根据不同的手机型号进行相应的硬件功能调用,比如扫描枪,普通手机摄像头
 * 
 * @author qingli
 *
 */
public class PhoneModelManager {
	public static final String ACTION_SPECIALKEY = "tf.test.SpecialKeyPressed";//S200手机的按键action
	
	
	
	/**
	 * 根据设备型号返回不同的扫描调用意图,
	 * @param context
	 * @param isOpen 是否打开一个界面进行扫描,请在开启意图时进行非空判断
	 */
	public static Intent getIntent(Context context,boolean isOpen){
		String model=android.os.Build.MODEL;//型号
		Intent intent = new Intent();
		if ("KT40".equals(model)) {//KT40扫描枪系列
			intent.setClass(context, KT40ScanActivity.class);
		}else if ("S200".equals(model)) {//S200扫描枪系列
			if (isOpen) {				
				intent.setClass(context, ScanS200Activity.class);
			}else{
				Toast.makeText(context, R.string.utility_string31, Toast.LENGTH_SHORT).show();
				return null;
			}
			
		}else{//默认普通安卓设备
			intent.setClass(context, CaptureActivity.class);			
		}
		return intent;
	}

	  
	public static boolean isScanS200Phone(Context context){
		String model=android.os.Build.MODEL;//型号
		if ("S200".equals(model)) {
			return true;
		}
		return false;
		
		
	}
	
	
	
/**
 * 根据手机的型号,判断是否在当前页面进行按键调用扫描功能
 * 适配于手机硬件按钮的直接扫描
 * @param context 
 * @param keyCode 按键码
 * @param event   事件码
 */
    public static boolean isStartScan(Context context,int keyCode,KeyEvent event){
    	String model=android.os.Build.MODEL;//型号
    	boolean isScanBy=false;
    	if ("KT40".equals(model)||"KT40Q".equals(model)) {//KT40扫描枪系列
    		if (keyCode==event.KEYCODE_F5||keyCode==event.KEYCODE_F6) {//在当前页面按下手机硬件扫描按钮也跳转至扫码页面
    			isScanBy=true;
    		}
		}else if ("S200".equals(model)) { //S200天津云迈科技手机,按键是发送的广播,无法进行keydown
			
		}
		return isScanBy;
    }
    
    
    
}
