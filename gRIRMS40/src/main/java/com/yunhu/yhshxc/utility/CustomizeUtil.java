package com.yunhu.yhshxc.utility;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.application.SoftApplication;
import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.bo.Module;
import com.yunhu.yhshxc.bo.Submit;
import com.yunhu.yhshxc.bo.SubmitItem;
import com.yunhu.yhshxc.database.DictDB;
import com.yunhu.yhshxc.database.SubmitItemDB;

public class CustomizeUtil {
	
	private Module module;
	private Context context;
	private Submit submit;
	private SubmitItemDB submitItemDB;
	public CustomizeUtil(Context context) {
		this.context = context;
		submitItemDB = new SubmitItemDB(context);
	}
	
	public boolean checkForGL(){
		if (module!=null && submit!=null) {
			int menuid = module.getMenuId();
			if (2008455==menuid) {
				//外机条形码扫描
				SubmitItem item1 = submitItemDB.findSubmitItem(submit.getId(), "2018925");
				if (item1!=null) {
					String value = item1.getParamValue();
					if (!TextUtils.isEmpty(value)) {
						if (value.length()!=13) {
							Toast.makeText(context, R.string.utility_string38, Toast.LENGTH_SHORT).show();
							return false;
						}else{
							Dictionary dict = new DictDB(context).findDictByValueCheckForGL(PublicUtils.getResourceString(context,R.string.utility_string39), value.substring(0,5));
							if (dict==null) {
								Toast.makeText(context, R.string.utility_string38, Toast.LENGTH_SHORT).show();
								return false;
							}
						}
					}
				}
				//外机条形码输入
				SubmitItem item2 = submitItemDB.findSubmitItem(submit.getId(), "2018926");
				if (item2!=null) {
					String value = item2.getParamValue();
					if (!TextUtils.isEmpty(value)) {
						if (value.length()!=13) {
							Toast.makeText(context, R.string.utility_string37, Toast.LENGTH_SHORT).show();
							return false;
						}else{
							Dictionary dict = new DictDB(context).findDictByValueCheckForGL(PublicUtils.getResourceString(SoftApplication.context,R.string.utility_string39), value.substring(0,5));
							if (dict==null) {
								Toast.makeText(context, R.string.utility_string37, Toast.LENGTH_SHORT).show();
								return false;
							}
						}
					}
				}
				//内机条形码扫描
				SubmitItem item3 = submitItemDB.findSubmitItem(submit.getId(), "2018931");
				if (item3!=null) {
					String value = item3.getParamValue();
					if (!TextUtils.isEmpty(value)) {
						if (value.length()!=13) {
							Toast.makeText(context, R.string.utility_string36, Toast.LENGTH_SHORT).show();
							return false;
						}else{
							Dictionary dict = new DictDB(context).findDictByValueCheckForGL(PublicUtils.getResourceString(SoftApplication.context,R.string.utility_string35), value.substring(0,5));
							if (dict==null) {
								Toast.makeText(context, R.string.utility_string36, Toast.LENGTH_SHORT).show();
								return false;
							}
						}
					}
				}
				//内机条形码输入
				SubmitItem item4 = submitItemDB.findSubmitItem(submit.getId(), "2018932");
				if (item4!=null) {
					String value = item4.getParamValue();
					if (!TextUtils.isEmpty(value)) {
						if (value.length()!=13) {
							Toast.makeText(context, R.string.utility_string34, Toast.LENGTH_SHORT).show();
							return false;
						}else{
							Dictionary dict = new DictDB(context).findDictByValueCheckForGL(PublicUtils.getResourceString(SoftApplication.context,R.string.utility_string35), value.substring(0,5));
							if (dict==null) {
								Toast.makeText(context, R.string.utility_string34, Toast.LENGTH_SHORT).show();
								return false;
							}
						}
					}
				}
				
				String reInside = "";    // 内机条码第一位
				String serInside = "";   // 内机条码
				String reOutside = "";   // 外机条码第一位
				String serOutside = "";  // 外机条码
				if (item1 !=null && !TextUtils.isEmpty(item1.getParamValue())) {//外机扫码值
					serOutside = item1.getParamValue();
					reOutside  = serOutside.substring(0, 1);
				}else if(item2 !=null && !TextUtils.isEmpty(item2.getParamValue())){//外机输入值
					serOutside = item2.getParamValue();
					reOutside  = serOutside.substring(0, 1);
				}
				if (item3 !=null && !TextUtils.isEmpty(item3.getParamValue())) {//内机扫码值
					serInside = item3.getParamValue();
					reInside  = serOutside.substring(0, 1);
				}else if(item4 !=null && !TextUtils.isEmpty(item4.getParamValue())){//内机输入值
					serInside = item4.getParamValue();
					reInside  = serOutside.substring(0, 1);
				}
				
				if (!TextUtils.isEmpty(reInside) && !TextUtils.isEmpty(reOutside)) {
					if (reInside.equals(reOutside)) {
						String inF = reInside  = serInside.substring(0, 5);//内机条码的前五位
						String outF = reInside  = serOutside.substring(0, 5);//外机条码的前五位
						DictDB db = new DictDB(context);
						Dictionary inDescription = db.findDictDescriptionCheckForGL(inF);
						Dictionary outDescription = db.findDictDescriptionCheckForGL(outF);
						if (inDescription!=null && outDescription!=null && !TextUtils.isEmpty(inDescription.getCtrlCol())&& !TextUtils.isEmpty(outDescription.getCtrlCol())) {
							String inStr = inDescription.getCtrlCol();//内机描述
							String outStr = outDescription.getCtrlCol();//外机描述
							if ("4".equals(reInside) || "6".equals(reInside)) {//比较机型描述的前6位
								if (!inStr.substring(0, 6).equals(outStr.substring(0, 6))) {
									Toast.makeText(context, R.string.utility_string33, Toast.LENGTH_SHORT).show();
									return false;
								}
							}else{//比较机型描述的前五位
								if (!inStr.substring(0, 5).equals(outStr.substring(0, 5))) {
									Toast.makeText(context, R.string.utility_string33, Toast.LENGTH_SHORT).show();
									return false;
								}
							}
							
						}else{
							Toast.makeText(context, R.string.utility_string33, Toast.LENGTH_SHORT).show();
							return false;
						}
						
					}else{
						Toast.makeText(context, R.string.utility_string33, Toast.LENGTH_SHORT).show();
						return false;
					}
				}
			}
		}
		return true;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	public void setSubmit(Submit submit) {
		this.submit = submit;
	}
	
}
