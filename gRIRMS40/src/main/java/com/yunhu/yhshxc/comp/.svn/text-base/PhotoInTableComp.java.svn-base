package com.yunhu.yhshxc.comp;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.Submit;
import com.yunhu.yhshxc.bo.SubmitItem;
import com.yunhu.yhshxc.database.SubmitDB;
import com.yunhu.yhshxc.database.SubmitItemTempDB;

/**
 * 表格中的拍照封装
 * 
 * @author jishen
 * 
 */
public class PhotoInTableComp extends CameraComp {
	private String TAG = "PhotoInTableComp";
	private Button currentButton;
	private ImageView currentImageView;
	private View view;
	private int submitId;
	private boolean isLink = false;

	public PhotoInTableComp(Context context, Func func) {
		super(context, func);
		view = View.inflate(context, R.layout.photo_in_table_comp, null);
		currentButton = (Button) view.findViewById(R.id.photo_in_table_Comp);
		currentImageView=(ImageView) view.findViewById(R.id.photo_in_table_image);
	}

	@Override
	public void resultPhoto(String fileName,Date date) {
		((AbsBaseActivity) context).application.isSubmitActive = true;
		if (!isLink) {
			super.resultPhoto(fileName,date);
		} else {
			int submitId = save(fileName);
			if (this.compFunc.getPhotoLocationType() > -1) {
				List<SubmitItem> list = new SubmitItemTempDB(context).findSubmitItemBySubmitIdAndType(submitId,Func.TYPE_LOCATION);
				if (!list.isEmpty()) { // 超链接本身的定位
					SubmitItem item = list.get(0);
					forC(item.getParamValue());
				} else { // 最外层的定位
					List<Submit> submit = new SubmitDB(context).findSubmitByParentId(0);
					submitId = submit.get(0).getId();
					setLocation(submitId);
				}
			}
			
			/**
			 * 添加店面水印
			 */
			if (this.compFunc.getIsImgStore() == 1) {
				SubmitItem submitItem = new SubmitItemTempDB(context).findSubmitItemByNote(submitId, SubmitItem.NOTE_STORE);
				if(submitItem == null){
					List<Submit> submit = new SubmitDB(context).findSubmitByParentId(0);
					super.setStoreWater(submit.get(0).getId());
				}else{
					setWaterStroe(getOrgStore(submitItem.getParamValue()));
				}
			}
			
			settingPhoto(fileName,date);
		}
	}

	public Func getFunc() {
		return compFunc;
	}

	public void setIsLink(boolean isLink) {
		this.isLink = isLink;
	}

	@Override
	protected int save(String fileName) {
		return submitId;
	}

	public void setSubmitId(int submitId) {
		this.submitId = submitId;
	}

	/**
	 * 设置当前控件的值
	 * 
	 * @param value
	 *            值
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * 给button设置显示名称
	 * 
	 * @param name
	 */
	public void setName(String name) {
		if (!TextUtils.isEmpty(name)) {
			currentButton.setText(name);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gcg.grirms.comp.Component#getObject()
	 */
	@Override
	public View getObject() {
		return view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gcg.grirms.comp.Component#setIsEnable(boolean)
	 */
	@Override
	public void setIsEnable(boolean isEnable) {
		currentButton.setEnabled(isEnable);
	}

	/**
	 * 
	 * @return 当前button
	 */
	public Button getCurrentButton() {
		return currentButton;
	}

	/**
	 * 
	 * @return 当前ImageView
	 */
	public ImageView getCurrentImageView() {
		return currentImageView;
	}
}
