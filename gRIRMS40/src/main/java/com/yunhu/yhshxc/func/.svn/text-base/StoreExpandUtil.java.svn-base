package com.yunhu.yhshxc.func;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.LocationResult;
import com.yunhu.yhshxc.bo.Submit;
import com.yunhu.yhshxc.bo.SubmitItem;
import com.yunhu.yhshxc.comp.location.LocationControlListener;
import com.yunhu.yhshxc.comp.location.LocationForStoreExpand;
import com.yunhu.yhshxc.database.SubmitDB;
import com.yunhu.yhshxc.database.SubmitItemDB;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.utility.Constants;

import java.util.List;



public class StoreExpandUtil implements LocationControlListener{
	public Context context;
	private Dialog dialog = null;
	public static final int STORE_EXPAND_LOCATION_ID = -1234;
	private AbsFuncActivity absFuncActivity;
	

	public StoreExpandUtil(Context context){
		this.context = context;
		absFuncActivity = (AbsFuncActivity) context;
	}


	public Func lonFunc;//经度控件
	public Func latFunc;//纬度控件
	public Func adressFunc;//位置控件
	public Func locationFunc;//定位控件

	
	public  List<Func> getFuncs(List<Func> funcList){
		if (funcList!=null && funcList.size()>4) {
			lonFunc = funcList.get(3);
			latFunc = funcList.get(4);
			adressFunc = funcList.get(2);
			funcList.remove(lonFunc);
			funcList.remove(latFunc);
			locationFunc = new Func();//定位控件
			locationFunc.setName(context.getResources().getString(R.string.location_title));
			locationFunc.setType(Func.TYPE_LOCATION);
			locationFunc.setFuncId(STORE_EXPAND_LOCATION_ID);
			funcList.add(2,locationFunc);
		}
		return funcList;
	}

	private LocationForStoreExpand locationForStoreExpand;
	public void initLocation(boolean isAreaSearchLocation){
		dialog = new MyProgressDialog(context,R.style.CustomProgressDialog,context.getResources().getString(R.string.waitForAMoment));

		locationForStoreExpand = new LocationForStoreExpand(context, this);
		locationForStoreExpand.setLoadDialog(dialog);
		locationForStoreExpand.setAreaSearchLocation(isAreaSearchLocation);//设置是否是店面距离搜索定位
		locationForStoreExpand.startLocation();

	}

	@Override
	public void confirmLocation(LocationResult result) {
		save(result);
	}

	@Override
	public void areaSearchLocation(LocationResult result) {}
	
	private void save(LocationResult result) {
		if (result!=null) {
			double lon = result.getLongitude();
			double lat = result.getLatitude();
			String adress = result.getAddress();
			
			if (lon>0) {
				saveLonFunc(String.valueOf(lon));
			}
			if (lat>0) {
				saveLatFunc(String.valueOf(lat));
			}
			if (!TextUtils.isEmpty(adress) && !isHasAdressFunc()) {
				saveAdressFunc(adress);
			}
						
			absFuncActivity.saveLocationContent(result, locationFunc);
		}else{
			Toast.makeText(context, context.getResources().getString(R.string.locations_exception), Toast.LENGTH_SHORT).show();
		}
		absFuncActivity.setShowHook();
	}
	
	private void saveLonFunc(String value){
		saveSubmitItem(lonFunc, value);
	}
	
	private void saveLatFunc(String value){
		saveSubmitItem(latFunc, value);
	}
	private void saveAdressFunc(String value){
		saveSubmitItem(adressFunc, value);
	}
	
	
	private boolean isHasAdressFunc(){
		int submitId = new SubmitDB(context).selectSubmitIdNotCheckOut(absFuncActivity.planId, absFuncActivity.wayId, absFuncActivity.storeId,absFuncActivity.targetId, absFuncActivity.menuType,Submit.STATE_NO_SUBMIT);
		boolean  isHas = new SubmitItemDB(context).findIsHaveParamName(String.valueOf(adressFunc.getFuncId()), submitId);
		return isHas;
	}
	
	private void saveSubmitItem(Func func,String value){
		SubmitDB submitDB = new SubmitDB(context);
		SubmitItemDB submitItemDB = new SubmitItemDB(context);
		SubmitItem submitItem = new SubmitItem();
		submitItem.setTargetId(func.getTargetid()+"");
		int submitId = submitDB.selectSubmitIdNotCheckOut(absFuncActivity.planId, absFuncActivity.wayId, absFuncActivity.storeId,absFuncActivity.targetId, absFuncActivity.menuType,Submit.STATE_NO_SUBMIT);
		if (submitId != Constants.DEFAULTINT) {
			submitItem.setParamName(String.valueOf(func.getFuncId()));
			submitItem.setParamValue(value);
			submitItem.setSubmitId(submitId);
			submitItem.setType(func.getType());
			submitItem.setOrderId(func.getId());
			submitItem.setIsCacheFun(func.getIsCacheFun());
			boolean  isHas = submitItemDB.findIsHaveParamName(submitItem.getParamName(), submitId);
			if (isHas) {// true表示已经操作过，此时更新数据库
				if (TextUtils.isEmpty(value)) {// 如果值是空就修改为空 修改的时候如果值取消就传给服务器一个空值，否则服务器不处理
					submitItem.setParamValue("");
				}
				submitItemDB.updateSubmitItem(submitItem,false);
			} else {
				if (!TextUtils.isEmpty(value)) {// 没有该项，并且值不为空就插入
					submitItemDB.insertSubmitItem(submitItem,false);
				}
			}
		} else {
			if(!TextUtils.isEmpty(value)){
				Submit submit = new Submit();
				submit.setPlanId(absFuncActivity.planId);
				submit.setState(Submit.STATE_NO_SUBMIT);
				submit.setStoreId(absFuncActivity.storeId);
				submit.setStoreName(absFuncActivity.storeName);
				submit.setWayName(absFuncActivity.wayName);
				submit.setWayId(absFuncActivity.wayId);
				submit.setTargetid(absFuncActivity.targetId);
				submit.setTargetType(absFuncActivity.menuType);
				submit.setMenuId(absFuncActivity.menuId);
				submit.setMenuType(absFuncActivity.menuType);
				submit.setMenuName(absFuncActivity.menuName);
				submitDB.insertSubmit(submit);
				int currentsubmitId = submitDB.selectSubmitIdNotCheckOut(absFuncActivity.planId, absFuncActivity.wayId,absFuncActivity.storeId, absFuncActivity.targetId, absFuncActivity.menuType,Submit.STATE_NO_SUBMIT);
				submitItem.setParamName(String.valueOf(func.getFuncId()));
				submitItem.setType(func.getType());
				submitItem.setParamValue(value);
				submitItem.setSubmitId(currentsubmitId);
				submitItem.setOrderId(func.getId());
				submitItem.setIsCacheFun(func.getIsCacheFun());
				submitItemDB.insertSubmitItem(submitItem,false);

			}
			
		}
	}
	
}
