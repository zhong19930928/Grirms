package com.yunhu.yhshxc.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.yunhu.yhshxc.attendance.AttendanceUtil;
import com.yunhu.yhshxc.attendance.backstage.AttendanceLocationService;
import com.yunhu.yhshxc.location.backstage.BackstageLocationManager;
import com.yunhu.yhshxc.location.backstage.BackstageLocationService;
import com.yunhu.yhshxc.push.GetPushService;
import com.yunhu.yhshxc.submitManager.core.SubmitWorkService;
import com.yunhu.yhshxc.utility.PublicUtils;

/**
 * 当手机重启时会发出广播，此类会拦截此广播， 启动一些service
 * 
 */
public class BootBroadcastReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(final Context context, Intent intent) {
		
		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

			if (!TextUtils.isEmpty(PublicUtils.receivePhoneNO(context))) {
				// 开启push Service
				context.startService(new Intent(context, GetPushService.class));
				// 开启被动定位service
				new BackstageLocationManager(context).updateLocationRuleForInit(false); // 开机后会弹出提示框
				context.startService(new Intent(context,BackstageLocationService.class));//被动定位服务
				// 后台提交
				context.startService(new Intent(context,SubmitWorkService.class));
				// 守店
				AttendanceUtil.startAttendanceLocationService(context,AttendanceLocationService.WORK_PHONE_RESTART);
				// 上传Log
				PublicUtils.uploadLog(context, "log");
			}

		}

	}

}
