package com.yunhu.yhshxc.comp.location;

import android.app.Dialog;
import android.content.Context;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.LocationResult;
import com.yunhu.yhshxc.dialog.LocationDialogForCheckIn;
import com.yunhu.yhshxc.visit.VisitFuncActivity;

import gcg.org.debug.JLog;

/**
 * 访店进店定位
 * @author gcg_jishen
 */
public class LocationForCheckIn extends AbsLocationComp{

	private LocationDialogForCheckIn locationDialogForCheckIn;
	private VisitFuncActivity visitFuncActivity;
	public LocationForCheckIn(Context context,LocationControlListener listener) {
		super(context,listener);
		visitFuncActivity = (VisitFuncActivity) context;
	}
	
	@Override
	public void onReceiveResult(LocationResult result) {
		this.locationResult = result;
		showLoadDialog(false);
		try {
			locationDialogForCheckIn = new LocationDialogForCheckIn(mContext, result, this);
			int isCheck = bundle!=null?bundle.getInt("isCheck"):0;
			if (isCheck == 0) {//不需要检测距离
				locationDialogForCheckIn.initDialogView();
			}else{//需要检测距离
				boolean isInDistance = storeDistance(result);
//				locationDialogForCheckIn.initVisitCheckInDialogView(isInDistance);
				locationDialogForCheckIn.initCheckDialogView(isInDistance,bundle);
			}
			locationDialogForCheckIn.showLocationDialog();
		} catch (Exception e) {
			JLog.e(e);
			if(mContext!=null){
				Toast.makeText(mContext, mContext.getResources().getString(R.string.locations_exception),Toast.LENGTH_LONG).show();
			}
		}
		
		if (visitFuncActivity.checkIn!=null) {
			visitFuncActivity.checkIn.setEnabled(true);
		}
		super.onReceiveResult(result);
	}

	@Override
	public void confirm(Dialog dialog) {
		dialog.dismiss();
		locationControlListener.confirmLocation(locationResult);
	}

	@Override
	public void newLocation(Dialog dialog) {
		dialog.dismiss();
		startLocation();
	}

	@Override
	public void cancel(Dialog dialog) {
		visitFuncActivity.checkLocation = false;
		dialog.dismiss();
	}

	
	@Override
	public void startLocation() {
		visitFuncActivity.checkLocation = true;//状态置为true表示该次定位为chickin定位
		super.startLocation();

	}

	@Override
	public void onAcivityResume() {
		if (locationDialogForCheckIn!=null && locationDialogForCheckIn.isShowOnResume) {
			locationDialogForCheckIn.showLocationDialog();
		}		
	}
	
}
