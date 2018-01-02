package com.yunhu.yhshxc.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.LocationResult;
import com.yunhu.yhshxc.func.AbsFuncActivity;
import com.yunhu.yhshxc.location.LocationTipActivity;

/**
 * checkout定位提示框
 * @author gcg_jishen
 * 
 *
 */
public class LocationDialogForCheckOut extends AbsLocationDialog{

	private Dialog locationDialog;
	private AbsFuncActivity myActivity;
	
	public LocationDialogForCheckOut(Context context,LocationResult locationResult, LocationDialogListener listener) {
		super(context, locationResult, listener);
		myActivity = (AbsFuncActivity) context;
	}

	@Override
	public void initDialogView() {
		locationDialog = new Dialog(mContext,R.style.transparentDialog);// 初始化dialog
		View view = View.inflate(mContext, R.layout.location_checkin_dialog,null);
		TextView checkInContent = (TextView) view.findViewById(R.id.tv_needcheckin_dialog_content);
		TextView title = (TextView) view.findViewById(R.id.location_title);
		LinearLayout ll_Acc = (LinearLayout) view.findViewById(R.id.ll_location_acc);
		LinearLayout confirm = (LinearLayout) view.findViewById(R.id.ll_needcheckin_dialog_confirm);
		LinearLayout newLocation = (LinearLayout) view.findViewById(R.id.ll_needcheckin_dialog_newlocation);
		LinearLayout cancel = (LinearLayout) view.findViewById(R.id.iv_needcheckin_dialog_cancle);
		//设置标题
		title.setText(mContext.getResources().getString(R.string.checkout_title));
		StringBuffer buffer = new StringBuffer();// 拼接定位信息内容
		if (locationResult != null) {
			buffer.append("\n").append(mContext.getResources().getString(R.string.location_type)).append(locationResult.getLocType());
		}
		//判断是否定位成功
		if (locationResult.isStatus()) {
			String adress = locationResult.getAddress();
			if (TextUtils.isEmpty(adress)) {//判断地址是否为空
				checkInContent.setText(mContext.getResources().getString(R.string.location_success_no_addr) + buffer.toString());
			}else{
				checkInContent.setText(adress + buffer.toString());
			}
		} else {
			confirm.setEnabled(false);
			checkInContent.setText(mContext.getResources().getString(R.string.location_fail));
			confirm.setBackgroundResource(R.color.func_menu_unenable);
		}

		//判断是否在精度范围内
		if (!locationResult.isInAcc()) {// 如果没在范围内
			confirm.setEnabled(false);
			confirm.setBackgroundResource(R.color.func_menu_unenable);
			ll_Acc.setVisibility(View.VISIBLE);
			ll_Acc.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext,LocationTipActivity.class);
					mContext.startActivity(intent);
				}
			});
		}
		
		newLocation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				locationDialogListener.newLocation(locationDialog);
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				locationDialogListener.cancel(locationDialog);
			}
		});
		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				locationDialogListener.confirm(locationDialog);
			}
		});
		locationDialog.setContentView(view);
		locationDialog.setCancelable(false);
	}
	
	public void initVisitCheckOutDialogView(boolean isInDistance){
		locationDialog = new Dialog(mContext,R.style.transparentDialog);// 初始化dialog
		View view = View.inflate(mContext, R.layout.location_checkin_dialog,null);
		TextView checkContent = (TextView) view.findViewById(R.id.tv_needcheckin_dialog_content);
		TextView tv_location_acc = (TextView)view.findViewById(R.id.tv_location_acc);
		TextView title = (TextView) view.findViewById(R.id.location_title);
		LinearLayout ll_Acc = (LinearLayout) view.findViewById(R.id.ll_location_acc);
		LinearLayout confirm = (LinearLayout) view.findViewById(R.id.ll_needcheckin_dialog_confirm);
		LinearLayout newLocation = (LinearLayout) view.findViewById(R.id.ll_needcheckin_dialog_newlocation);
		LinearLayout cancel = (LinearLayout) view.findViewById(R.id.iv_needcheckin_dialog_cancle);
		//设置标题
		title.setText(mContext.getResources().getString(R.string.checkout_title));
		StringBuffer buffer = new StringBuffer();// 拼接定位信息内容
		if (locationResult != null) {
			buffer.append("\n").append(mContext.getResources().getString(R.string.location_type)).append(locationResult.getLocType());
		}
		//判断是否定位成功
		if (locationResult.isStatus()) {
			String adress = locationResult.getAddress();
			if (TextUtils.isEmpty(adress)) {//判断地址是否为空
				checkContent.setText(mContext.getResources().getString(R.string.location_success_no_addr) + buffer.toString());
			}else{
				checkContent.setText(locationResult.getAddress()+ buffer.toString());
			}
		} else {
			confirm.setEnabled(false);
			checkContent.setText(mContext.getResources().getString(R.string.location_fail));
			confirm.setBackgroundResource(R.color.func_menu_unenable);
		}

		// 如果没在范围内
		if (!isInDistance) {
			confirm.setEnabled(false);
			confirm.setBackgroundResource(R.color.func_menu_unenable);
			ll_Acc.setVisibility(View.VISIBLE);
			tv_location_acc.setText(mContext.getResources().getString(R.string.location_no_in_distance));
		}
		
		newLocation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				locationDialogListener.newLocation(locationDialog);
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				locationDialogListener.cancel(locationDialog);
			}
		});
		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				locationDialogListener.confirm(locationDialog);
			}
		});
		locationDialog.setContentView(view);
		locationDialog.setCancelable(false);
	}
	
	@Override
	public void initCheckDialogView(boolean isInDistance, Bundle bundle) {
		locationDialog = new Dialog(mContext,R.style.transparentDialog);// 初始化dialog
		View view = View.inflate(mContext, R.layout.location_checkin_dialog,null);
		TextView checkContent = (TextView) view.findViewById(R.id.tv_needcheckin_dialog_content);
		TextView tv_location_acc = (TextView)view.findViewById(R.id.tv_location_acc);
		TextView title = (TextView) view.findViewById(R.id.location_title);
		LinearLayout ll_Acc = (LinearLayout) view.findViewById(R.id.ll_location_acc);
		LinearLayout confirm = (LinearLayout) view.findViewById(R.id.ll_needcheckin_dialog_confirm);
		LinearLayout newLocation = (LinearLayout) view.findViewById(R.id.ll_needcheckin_dialog_newlocation);
		LinearLayout cancel = (LinearLayout) view.findViewById(R.id.iv_needcheckin_dialog_cancle);
		TextView tv_out_confirm = (TextView)view.findViewById(R.id.tv_checkin_confirm);

		//设置标题
		title.setText(mContext.getResources().getString(R.string.checkinName));
		StringBuffer buffer = new StringBuffer();// 拼接定位信息内容
		if (locationResult != null) {
			buffer.append("\n").append(mContext.getResources().getString(R.string.location_type)).append(locationResult.getLocType());
		}
		//判断是否定位成功
		if (locationResult.isStatus()) {
			String isAddress = bundle!=null?bundle.getString("is_address"):"1";//显示地址详细 0不显示 1显示 默认为1
			String isNewLoc = bundle!=null?bundle.getString("is_anew_loc"):"0";//超出范围要重新定位 0不需要重新定位 1需要重新定位 默认为0

			if ("0".equals(isAddress)) {//不需要显示详细地址
				// 如果没在范围内
				if (isInDistance) {//没有超出距离
					//不显示地址并且在范围内 直接到上报页面
					isNeedShowDialog  = false;//不用弹出dialog
				}else{//超出距离
					if ("1".equals(isNewLoc)) {//超出距离需要重新定位
						confirm.setEnabled(false);
						confirm.setBackgroundResource(R.color.func_menu_unenable);
					}else{
						tv_out_confirm.setText(mContext.getResources().getString(R.string.accept1));
					}
					ll_Acc.setVisibility(View.VISIBLE);
					tv_location_acc.setText(mContext.getResources().getString(R.string.location_no_in_distance));
					checkContent.setVisibility(View.GONE);
				}
			}else{//需要显示详细地址
				String adress = locationResult.getAddress();
				if (TextUtils.isEmpty(adress)) {//判断地址是否为空
					checkContent.setText(mContext.getResources().getString(R.string.location_success_no_addr) + buffer.toString());
				}else{
					checkContent.setText(locationResult.getAddress()+ buffer.toString());
				}
				// 判断是否在范围内
				if (isInDistance) {//没有超出距离
					//显示地址并且在范围内
				}else{//超出距离
					if ("1".equals(isNewLoc)) {//超出距离需要重新定位
						confirm.setEnabled(false);
						confirm.setBackgroundResource(R.color.func_menu_unenable);
					}else{
						tv_out_confirm.setText(mContext.getResources().getString(R.string.accept1));
					}
					ll_Acc.setVisibility(View.VISIBLE);
					tv_location_acc.setText(mContext.getResources().getString(R.string.location_no_in_distance));
				}
			}
			
		} else {
			confirm.setEnabled(false);
			checkContent.setText(mContext.getResources().getString(R.string.location_fail));
			confirm.setBackgroundResource(R.color.func_menu_unenable);
		}
		
		newLocation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				locationDialogListener.newLocation(locationDialog);
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				locationDialogListener.cancel(locationDialog);
			}
		});
		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				locationDialogListener.confirm(locationDialog);
			}
		});
		locationDialog.setContentView(view);
		locationDialog.setCancelable(false);
		
	}
	

	@Override
	public void showLocationDialog() {
		if (isNeedShowDialog) {//需要弹出dialog
			if (myActivity.isOnPause) {//程序在后台
				isShowOnResume = true;
			}else{
				locationDialog.show();
				isShowOnResume = false;
			}
		}else{//不显示地址不需要弹出dialog的直接调确认
			locationDialogListener.confirm(locationDialog);
		}
	}

}
