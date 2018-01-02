package com.yunhu.yhshxc.comp;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.LocationResult;
import com.yunhu.yhshxc.bo.OrgStore;
import com.yunhu.yhshxc.bo.Submit;
import com.yunhu.yhshxc.bo.SubmitItem;
import com.yunhu.yhshxc.database.OrgStoreDB;
import com.yunhu.yhshxc.database.SubmitDB;
import com.yunhu.yhshxc.database.SubmitItemDB;
import com.yunhu.yhshxc.func.AbsFuncActivity;
import com.yunhu.yhshxc.utility.Constants;

/**
 * 拍照控件
 * @author gcg_jishen
 *
 */
public class CameraComp extends AbsCameraComp {

	protected AbsFuncActivity activity;

	public CameraComp(Context context, Func func) {
		super(context, func);
		//func.setPhotoLocationType(3);
		//func.setPhotoTimeType(2);
		if(context instanceof AbsFuncActivity){
			activity = (AbsFuncActivity) context;
		}
	}

	/**
	 * 拍照返回
	 */
	@Override
	public void resultPhoto(String fileName,Date date) {
		((AbsBaseActivity) context).application.isSubmitActive = true;
		int submitId = save(fileName);
		setStoreWater(submitId);
		setLocation(submitId);
		settingPhoto(fileName,date);
	}
	
	/**
	 * 设置定位地址
	 * @param submitId 该条提交数据的表单id
	 */
	public void setLocation(int submitId) {
		if(this.compFunc.getPhotoLocationType() > -1){//需要添加定位水印
			List<SubmitItem> list = new SubmitItemDB(context).findSubmitItemBySubmitIdAndType(submitId, Func.TYPE_LOCATION);
			if(list.isEmpty()){
				Submit submit = new SubmitDB(context).findSubmitById(submitId);
				if(submit != null){
					forV(submit.getCheckinGis());
				}
			}else{
				SubmitItem item = list.get(0);
				forC(item.getParamValue());
			}
		}
	}
	
	/**
	 * 设置店面水印
	 * @param submitId
	 */
	public void setStoreWater(int submitId){
		if (this.compFunc.getIsImgStore() == 1) {
			SubmitItem submitItem = new SubmitItemDB(context).findSubmitItemByNote(submitId, SubmitItem.NOTE_STORE);
			if(submitItem == null){
				Submit submit = new SubmitDB(context).findSubmitById(submitId);
				if(submit != null){
					setWaterStroe(submit.getStoreName());
				}
			}else{
				setWaterStroe(getOrgStore(submitItem.getParamValue()));
			}
		}
	}
	
	/**
	 * 返回店面数据
	 * @param value 店面的did
	 * @return 店面的名称
	 */
	public String getOrgStore(String value) {
		List<OrgStore> orgStoreList = new OrgStoreDB(context).findOrgListByStoreId(value);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < orgStoreList.size(); i++) {
			OrgStore orgStore = orgStoreList.get(i);
			sb.append(",").append(orgStore.getStoreName());
		}
		return sb.length() > 0 ? sb.substring(1).toString() : value;
	}
	
	/**
	 * 普通定位控件拆分定位信息
	 * @param value
	 */
	protected void forC(String value){
		if(TextUtils.isEmpty(value)) return;
		String str[] = value.split("\\$");
		if (str.length>=3) {
			setLocationResult(Double.valueOf(str[2]),Double.valueOf(str[1]),str[0]);
		}
	}
	
	/**
	 * 访店checkin拆分定位信息
	 * @param value
	 */
	protected void forV(String value){
		if(TextUtils.isEmpty(value)) return;
		String str[] = value.split("\\$");
		if (str.length>=3) {
			setLocationResult(Double.valueOf(str[1]),Double.valueOf(str[0]),str[2]);
		}
	}
	
	/**
	 * 设置定位信息
	 * @param lat
	 * @param lon
	 * @param addr
	 */
	private void setLocationResult(double lat,double lon,String addr){
		LocationResult locationResult = new LocationResult();
		locationResult.setAddress(addr);
		locationResult.setLongitude(lon);
		locationResult.setLatitude(lat);
		this.setLocationResult(locationResult);
	}

	/**
	 * 存储拍照信息
	 * @param fileName 照片名称
	 * @return
	 */
	protected int save(String fileName) {
		SubmitDB submitDB = new SubmitDB(context);
		SubmitItemDB submitItemDB = new SubmitItemDB(context);
		int submitId = submitDB.selectSubmitIdNotCheckOut(activity.planId,
				activity.wayId, activity.storeId, activity.targetId,
				activity.menuType, Submit.STATE_NO_SUBMIT);
		if (submitId != Constants.DEFAULTINT) {// 首先查找有没有该条数据对应的Submit
			SubmitItem submitItem = new SubmitItem();
			submitItem.setSubmitId(submitId);
			submitItem.setParamName(String.valueOf(compFunc.getFuncId()));
			submitItem.setParamValue(fileName);
			submitItem.setType(compFunc.getType());
			submitItem.setIsCacheFun(compFunc.getIsCacheFun());
			boolean isHas = submitItemDB.findIsHaveParamName(
					submitItem.getParamName(), submitId);// 首先判断数据库中有没有该项操作
			if (isHas) {// 如果有就更新没有就插入 此时不能删除原来的图片，因为用户可能不保存
				submitItemDB.updateSubmitItem(submitItem,false);
			} else {
				submitItemDB.insertSubmitItem(submitItem,false);
			}
		} else {// 没有该条数据对应的Submit 在submitDB中先插入一条数据，与该条操作对应。
			Submit submit = new Submit();
			submit.setPlanId(activity.planId);
			submit.setState(Submit.STATE_NO_SUBMIT);
			submit.setStoreId(activity.storeId);
			submit.setWayId(activity.wayId);
			submit.setTargetid(activity.targetId);
			submit.setTargetType(activity.menuType);
			if (activity.modType!=0) {
				submit.setModType(activity.modType);
			}
			submit.setMenuId(menuId);
			submit.setMenuType(menuType);
			submit.setMenuName(menuName);
			long submitid = submitDB.insertSubmit(submit);
			SubmitItem submitItem = new SubmitItem();
			submitItem.setSubmitId(Integer.valueOf(submitid + ""));
			submitItem.setParamName(String.valueOf(compFunc.getFuncId()));
			submitItem.setParamValue(fileName);
			submitItem.setType(compFunc.getType());
			submitItem.setIsCacheFun(compFunc.getIsCacheFun());
			submitItemDB.insertSubmitItem(submitItem,false);// 插入该submitItem
		}
		
		return submitId;
	}

	@Override
	public View getObject() {
		return null;
	}

	@Override
	public void setIsEnable(boolean isEnable) {

	}
	
}
