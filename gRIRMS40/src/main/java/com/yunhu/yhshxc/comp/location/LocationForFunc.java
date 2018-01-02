package com.yunhu.yhshxc.comp.location;

import android.app.Dialog;
import android.content.Context;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.LocationResult;
import com.yunhu.yhshxc.dialog.LocationDialogForFunc;
import com.yunhu.yhshxc.func.AbsFuncActivity;

import gcg.org.debug.JLog;

/**
 * 定位控件定位
 * @author gcg_jishen
 *
 */
public class LocationForFunc extends AbsLocationComp{

	private LocationDialogForFunc locationDialogForFunc = null;

	public LocationForFunc(Context context,LocationControlListener listener) {
		super(context,listener);
	}
	
	
	@Override
	public void onReceiveResult(LocationResult result) {
		showLoadDialog(false);
		this.locationResult = result;
		if (isAreaSearchLocation) {
			locationControlListener.areaSearchLocation(result);
		}else{
			try {
				
				locationDialogForFunc = new LocationDialogForFunc(mContext,result,this);
				int isCheck = bundle!=null?bundle.getInt("isCheck"):0;
				if (isCheck == 0) {
					locationDialogForFunc.initDialogView();
				}else{
					boolean isInDistance = storeDistance(result);
					locationDialogForFunc.initCheckDialogView(isInDistance, bundle);
				}
				locationDialogForFunc.showLocationDialog();
			} catch (Exception e) {
				JLog.e(e);
				AbsFuncActivity activity = (AbsFuncActivity) mContext;
				activity.isCanClick = true;
				if(mContext!=null){
					Toast.makeText(mContext, mContext.getResources().getString(R.string.locations_exception), Toast.LENGTH_LONG).show();
				}
			}
			
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
		AbsFuncActivity activity = (AbsFuncActivity) mContext;
		activity.isCanClick = true;
		dialog.dismiss();
	}

	@Override
	public void onAcivityResume() {
		if (locationDialogForFunc!=null && locationDialogForFunc.isShowOnResume) {
			locationDialogForFunc.showLocationDialog();
		}
	}

}
