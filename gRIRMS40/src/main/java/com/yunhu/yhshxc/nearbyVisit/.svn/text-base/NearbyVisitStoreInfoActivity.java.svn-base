package com.yunhu.yhshxc.nearbyVisit;

import android.os.Bundle;
import android.text.TextUtils;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.visit.InfoDetailActivity;

public class NearbyVisitStoreInfoActivity extends InfoDetailActivity{
	
	private String storeInfo;
	private String storeName;
	@Override
	public int setStoreType() {
		storeInfo = String.valueOf(getIntent().getBundleExtra("bundle").getInt("storeInfo"));
		if("1".equals(storeInfo)){
			return Func.IS_STORE_VIEW;//查看
		}else if("2".equals(storeInfo)){
			return Func.IS_STORE_UPDATE;//修改
		}else{
			return super.setStoreType();
		}
	}
	
	
	@Override
	public String titleName() {
		if("1".equals(storeInfo)){
			return getResources().getString(R.string.nearby_visit_19);//查看
		}else if("2".equals(storeInfo)){
			return getResources().getString(R.string.nearby_visit_20);//修改
		}else{
			return super.titleName();
		}
	}
	
	@Override
	public String storeName() {
		storeName = getIntent().getBundleExtra("bundle").getString("storeName");
		if (!TextUtils.isEmpty(storeName)) {
			return storeName;
		}else{
			return super.storeName();
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("storeInfo", storeInfo);
		outState.putString("storeName", storeName);
	}
	
	@Override
	protected void restoreConstants(Bundle savedInstanceState) {
		super.restoreConstants(savedInstanceState);
		storeInfo = savedInstanceState.getString("storeInfo");
		storeName = savedInstanceState.getString("storeName");
	}


}
