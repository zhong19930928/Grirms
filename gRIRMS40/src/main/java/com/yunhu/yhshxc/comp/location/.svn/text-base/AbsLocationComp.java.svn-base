package com.yunhu.yhshxc.comp.location;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.yunhu.yhshxc.bo.LocationResult;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.dialog.LocationDialogListener;
//import com.gcg.grirms.location.LocationFactory;
import com.yunhu.yhshxc.location.ReceiveLocationListener;
import com.yunhu.yhshxc.location2.LocationFactoy;
import com.yunhu.yhshxc.utility.MapDistance;

/**
 * 定位控件基类
 * @author gcg_jishen
 *
 */
public abstract class AbsLocationComp implements ReceiveLocationListener ,LocationDialogListener{

	public Context mContext;
	public Menu menu;
//	public LocationFactory locationFactory;
	public LocationControlListener locationControlListener;
	public LocationResult locationResult;//定位结果
	public boolean isAreaSearchLocation;//是否是店面距离筛选定位
	public Dialog loadDialog;
	public Bundle bundle;
	public Bundle bundleType;
	
	public LocationFactoy factory;

	public AbsLocationComp(Context context,LocationControlListener listener) {
		this.mContext = context;
		this.locationControlListener = listener;
//		locationFactory = new LocationFactory(context);
		
		factory = new LocationFactoy(context,this);

	}
	
	
	/**
	 * 开始定位
	 */
	public void startLocation(){
		Log.e("aaa", "------------------");
		
//		int isCheck = bundle!=null?bundle.getInt("isCheck"):0;
//		if (isCheck == 1) {
//			String locType = bundle!=null?bundle.getString("loc_type"):"2";//定位方式 1仅GPS 2所有 默认为2
//			String isAddress = bundle!=null?bundle.getString("is_address"):"1";//显示地址详细 0不显示 1显示 默认为1
//			boolean showAdress = ("1").equals(isAddress)?true:false;
//			if ("1".equals(locType)) {
//				locationFactory.startOnlyGPSLocation(this,showAdress);
//			}else{
//				locationFactory.startNewLocation(this,showAdress);
//			}
//		}else{
//			locationFactory.startNewLocation(this,true);
//		}
	
		String lev = bundleType!=null?bundleType.getString("loc_lev"):"";
		if(lev.equalsIgnoreCase("1")){//GPS优先
			factory.startLocationG();
		}else if(lev.equalsIgnoreCase("2")){
			factory.startLocationWF();
		}else{
			factory.startLocationHH();
		}
		showLoadDialog(true);
	}
	public abstract void onAcivityResume();

	
	@Override
	public void onReceiveResult(LocationResult result) {}

	public Dialog getLoadDialog() {
		return loadDialog;
	}

	public void setLoadDialog(Dialog loadDialog) {
		this.loadDialog = loadDialog;
	}
	
	public Bundle getBundle() {
		return bundle;
	}

	public void setBundle(Bundle bundle) {
		this.bundle = bundle;
	}
	
	public Bundle getBunType(){
		return bundleType;
	}
	public void setBunType(Bundle bundle){
		this.bundleType = bundle;
	}

	public boolean isAreaSearchLocation() {
		return isAreaSearchLocation;
	}

	public void setAreaSearchLocation(boolean isAreaSearchLocation) {
		this.isAreaSearchLocation = isAreaSearchLocation;
	}

	/**
	 * 正在定位加载框
	 * @param isShow true要显示 false关闭显示
	 */
	public void showLoadDialog(boolean isShow){
		if (isShow) {
			if (loadDialog!=null) {
				loadDialog.show();
			}
		}else{
			if (loadDialog!=null && loadDialog.isShowing()) {
				loadDialog.dismiss();
			}
		}
	}
	
//比较两经纬度之间的距离是否在有效范围内
	public boolean storeDistance(LocationResult result) {
		String storeLon=bundle.getString("storelon");
		String storeLat=bundle.getString("storelat");
		int storeDistance=bundle.getInt("storeDistance");
		boolean flag = true;
		if (result == null) {
			flag = false;
		}else if(!result.isStatus() || result.getLongitude() == null || result.getLatitude() == null){
			flag = false;
		}else{
			if (TextUtils.isEmpty(storeLat) || TextUtils.isEmpty(storeLon) || storeDistance==0) {//有空值就不比较
				flag = true;
			}else{
				double lon1 = Double.valueOf(storeLon);
				double lat1 = Double.valueOf(storeLat);
				double lon2 = result.getLongitude();
				double lat2 = result.getLatitude();
				double distance = MapDistance.getDistance(lat1, lon1, lat2, lon2);
				if (distance>storeDistance) {
					flag = false;
				}else{
					flag = true;
				}
			}
		}
		return flag;
	}

}
