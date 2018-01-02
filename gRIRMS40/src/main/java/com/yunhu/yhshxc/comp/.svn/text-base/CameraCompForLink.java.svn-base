package com.yunhu.yhshxc.comp;

import java.util.Date;
import java.util.List;

import android.content.Context;

import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.Submit;
import com.yunhu.yhshxc.bo.SubmitItem;
import com.yunhu.yhshxc.database.SubmitDB;
import com.yunhu.yhshxc.database.SubmitItemTempDB;
import com.yunhu.yhshxc.utility.Constants;

/**
 * 超链接模块中的拍照控件
 * @author gcg_jishen
 *
 */
public class CameraCompForLink extends CameraComp{
	
	public CameraCompForLink(Context context,Func func) {
		super(context, func);
	}
	
	@Override
	public void resultPhoto(String fileName,Date date) {
		((AbsBaseActivity) context).application.isSubmitActive = true;
		int submitId = save(fileName);
		linkLocation(submitId);
		linkStore(submitId);
		settingPhoto(fileName,date);
	}
	
	/**
	 * 添加店面水印
	 */
	private void linkStore(int submitId){
		if (this.compFunc.getIsImgStore() == 1) {
			SubmitItem submitItem = new SubmitItemTempDB(context).findSubmitItemByNote(submitId, SubmitItem.NOTE_STORE);
			if(submitItem == null){
				List<Submit> submit = new SubmitDB(context).findSubmitByParentId(0);
				super.setStoreWater(submit.get(0).getId());
			}else{
				setWaterStroe(getOrgStore(submitItem.getParamValue()));
			}
		}
	}
	
	/**
	 * 添加定位水印
	 * @param submitId
	 */
	private void linkLocation(int submitId){
		if(this.compFunc.getPhotoLocationType() > -1){
			List<SubmitItem> list = new SubmitItemTempDB(context).findSubmitItemBySubmitIdAndType(submitId, Func.TYPE_LOCATION);
			if(!list.isEmpty()){ //超链接本身的定位
				SubmitItem item = list.get(0);
				forC(item.getParamValue());
			}else{ //最外层的定位
				List<Submit> submit = new SubmitDB(context).findSubmitByParentId(0);
				setLocation(submit.get(0).getId());
			}
		}
	}
	
	
	@Override
	protected int save(String fileName) {
		SubmitDB submitDB = new SubmitDB(context);
		SubmitItemTempDB linkSubmitItemTempDB = new SubmitItemTempDB(activity);
		int submitId = submitDB.selectSubmitIdNotCheckOut(activity.planId, activity.wayId, activity.storeId, activity.targetId, activity.menuType, Submit.STATE_NO_SUBMIT);
		if (submitId != Constants.DEFAULTINT) {// 首先查找有没有该条数据对应的Submit
			SubmitItem submitItem = new SubmitItem();
			submitItem.setSubmitId(submitId);
			submitItem.setParamName(String.valueOf(compFunc.getFuncId()));
			submitItem.setParamValue(fileName);
			submitItem.setType(compFunc.getType());
			submitItem.setIsCacheFun(compFunc.getIsCacheFun());
			boolean isHas = linkSubmitItemTempDB.findIsHaveParamName(submitItem.getParamName(), submitId);// 首先判断数据库中有没有该项操作
			if (isHas) {// 如果有就更新没有就插入 此时不能删除原来的图片，因为用户可能不保存
				linkSubmitItemTempDB.updateSubmitItem(submitItem);
			}else {
				linkSubmitItemTempDB.insertSubmitItem(submitItem);
			}
		}else {// 没有该条数据对应的Submit 在submitDB中先插入一条数据，与该条操作对应。
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
			linkSubmitItemTempDB.insertSubmitItem(submitItem);// 插入该submitItem
		}
		return submitId;
	}
	
}
