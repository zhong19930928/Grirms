package com.yunhu.yhshxc.func;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.LocationResult;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.location.LocationTipActivity;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil2;
import com.yunhu.yhshxc.utility.SharedPreferencesUtilForNearby;

public class DialogInFuncManager {
	
	private AbsFuncActivity activity = null;

	public DialogInFuncManager(AbsFuncActivity activity) {
		this.activity = activity;
		if (activity.dialog != null && activity.dialog.isShowing()) {
			activity.dialog.dismiss();
		}
	}

	
	/*
	 * 定位dialog
	 * content 定位内容
	 * isInAcc 是否超出范围 true 没有超出范围 false已经超出范围
	 * isLocationSuccess 是否定位成功 true成功 false 失败
	 */
	public void showLocationDialog(LocationResult locationResult,boolean isInAcc,boolean isLocationSuccess) {
		activity.dialog = new Dialog(activity, R.style.transparentDialog);
		View view = View.inflate(activity, R.layout.location_dialog, null);
		LinearLayout confirm = (LinearLayout) view.findViewById(R.id.ll_location_dialog_confirm);
		LinearLayout newLocation = (LinearLayout) view.findViewById(R.id.ll_location_dialog_newlocation);
		LinearLayout cancel = (LinearLayout) view .findViewById(R.id.iv_location_dialog_cancle);
		TextView locationContent = (TextView) view.findViewById(R.id.tv_location_dialog_content);
		LinearLayout ll_Acc = (LinearLayout)view.findViewById(R.id.ll_location_acc);
		String content="";
//		String lon=SharedPreferencesUtil.getInstance(activity.getApplicationContext()).getLon();//经度
//		String lat=SharedPreferencesUtil.getInstance(activity.getApplicationContext()).getLat();//纬度
//		String loctype = SharedPreferencesUtil.getInstance(activity.getApplicationContext()).getLocationType();//定位方式
		StringBuffer buffer=new StringBuffer();
//		buffer.append("\n").append("经度：").append(lon).append("\n").append("纬度：").append(lat);
		if(locationResult!=null){
			buffer.append("\n").append(activity.getResources().getString(R.string.location_type)).append(locationResult.getLocType());
			content = locationResult.getAddress();
		}else{
			content = activity.getResources().getString(R.string.location_fail);
		}
		if(isLocationSuccess){//如果定位成功
			locationContent.setText(content+buffer.toString());
		}else{
			locationContent.setText(activity.getResources().getString(R.string.location_fail));
			confirm.setEnabled(false);
			confirm.setBackgroundResource(R.color.func_menu_unenable);
		}
		 
		
		if(!isInAcc){//如果没在范围内
			confirm.setEnabled(false);
			confirm.setBackgroundResource(R.color.func_menu_unenable);
			ll_Acc.setVisibility(View.VISIBLE);
			ll_Acc.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent=new Intent(activity, LocationTipActivity.class);
					activity.startActivity(intent);
				}
			});
		}
		cancel.setOnClickListener(activity);
		confirm.setOnClickListener(activity);
		newLocation.setOnClickListener(activity);
		activity.dialog.setContentView(view);
		activity.dialog.setCancelable(false);
		if(!activity.isOnPause){
			activity.dialog.show();
		}
	}
	
	
	
	public void loadingDialog(String content) {
//		activity.dialog = new Dialog(activity, R.style.transparentDialog);
//		activity.dialog.setCancelable(false);
//		View view = View.inflate(activity, R.layout.init_dialog, null);
//		Button cancelBtn = (Button) view.findViewById(R.id.btn_init_cancel);
//		TextView initContent = (TextView) view
//				.findViewById(R.id.tv_init_content);
//		initContent.setText(content);
//		cancelBtn.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				activity.dialog.dismiss();
//			}
//		});
//		activity.dialog.setContentView(view);
//		activity.dialog.show();
		activity.dialog = new MyProgressDialog(activity,R.style.CustomProgressDialog,content);
		activity.dialog.setCancelable(false);
		activity.dialog.show();
	}
	
	
	/**
	 * 
	 * @param isDelete true表示要删除数据
	 */
	public void backDialog(final boolean isLink) {
		AlertDialog.Builder alBuilder = new AlertDialog.Builder(this.activity,R.style.NewAlertDialogStyle);
		alBuilder.setTitle(activity.getResources().getString(R.string.WXTIP));
		alBuilder.setMessage(activity.getResources().getString(R.string.BACKDIALOG));
		if (isLink) {
			alBuilder.setMessage(activity.getResources().getString(R.string.LINK_BACKDIALOG));
		}else{
			alBuilder.setMessage(activity.getResources().getString(R.string.BACKDIALOG));
		}
		alBuilder.setPositiveButton(activity.getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				activity.alDialog.dismiss();
				SharedPreferencesUtil2.getInstance(activity).saveIsAnomaly(false);
				SharedPreferencesUtil2.getInstance(activity).saveMenuId(-1);
				if (isLink) {					
					activity.cleanCurrentNoSubmit(activity.targetId);
				}else{
					activity.cleanAllNoSubmit();
				}
				SharedPreferencesUtilForNearby.getInstance(activity).saveStoreInfoClear();
				activity.onClickBackBtn();

				
			}
		});
		alBuilder.setNegativeButton(activity.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				activity.alDialog.dismiss();
				
			}
		});
		activity.alDialog = alBuilder.create();
		activity.alDialog.setCancelable(false);
		activity.alDialog.show();
	}
	
}
