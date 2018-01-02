package com.yunhu.yhshxc.comp.location;

import android.app.Dialog;
import android.content.Context;

import com.yunhu.yhshxc.bo.LocationResult;
import com.yunhu.yhshxc.dialog.LocationDialogForFunc;

public class LocationForBBS extends AbsLocationComp {
	private LocationDialogForFunc locationDialogForFunc = null;
	
	public LocationForBBS(Context context, LocationControlListener listener) {
		super(context, listener);
	}

	@Override
	public void onReceiveResult(LocationResult result) {
		showLoadDialog(false);
		this.locationResult = result;
		if (isAreaSearchLocation) {
			locationControlListener.areaSearchLocation(result);
		}else{
			locationDialogForFunc = new LocationDialogForFunc(mContext,result,this);
			int isCheck = bundle!=null?bundle.getInt("isCheck"):0;
			if (isCheck == 0) {
				locationDialogForFunc.initDialogView();
			}else{
				boolean isInDistance = storeDistance(result);
				locationDialogForFunc.initCheckDialogView(isInDistance, bundle);
			}
			locationDialogForFunc.showLocationDialog();
		}
		
		super.onReceiveResult(result);
		
	}

	@Override
	public void confirm(Dialog dialog) {
		
	}

	@Override
	public void newLocation(Dialog dialog) {
		
	}

	@Override
	public void cancel(Dialog dialog) {
		
	}

	@Override
	public void onAcivityResume() {
		
	}
	
}
