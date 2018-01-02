package com.yunhu.yhshxc.comp.location;

import android.app.Dialog;
import android.content.Context;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.LocationResult;
import com.yunhu.yhshxc.dialog.LocationDialogForCheckOut;
import com.yunhu.yhshxc.func.AbsFuncActivity;

import gcg.org.debug.JLog;

/**
 * 访店离店定位
 * @author gcg_jishen
 *
 */
public class LocationForCheckOut extends AbsLocationComp{

	private AbsFuncActivity mFuncDetailActivity;
	private LocationDialogForCheckOut locationDialogForCheckOut;
	public LocationForCheckOut(Context context,LocationControlListener listener) {
		super(context,listener);
		
		mFuncDetailActivity = (AbsFuncActivity)context;
		
	}
	
	
	@Override
	public void onReceiveResult(LocationResult result) {
		this.locationResult = result;
		showLoadDialog(false);
		try {
			locationDialogForCheckOut = new LocationDialogForCheckOut(mContext, result, this);
			int isCheck = bundle!=null?bundle.getInt("isCheck"):0;
//			locationDialogForCheckOut=null;
			if (isCheck == 0) {//不需要检测距离
				locationDialogForCheckOut.initDialogView();
			}else{//需要检测距离
				boolean isInDistance = storeDistance(result);
				locationDialogForCheckOut.initCheckDialogView(isInDistance,bundle);
			}		
			locationDialogForCheckOut.showLocationDialog();
		} catch (Exception e) {
			JLog.e(e);
			if(mContext!=null){
				Toast.makeText(mContext, mContext.getResources().getString(R.string.locations_exception), Toast.LENGTH_LONG).show();
			}
			if(mFuncDetailActivity!=null)
			mFuncDetailActivity.submitDataLayout.setEnabled(true);
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
		dialog.dismiss();
	}
	
	@Override
	public void onAcivityResume() {
		if (locationDialogForCheckOut!=null && locationDialogForCheckOut.isShowOnResume) {
			locationDialogForCheckOut.showLocationDialog();
		}
	}
	

}
