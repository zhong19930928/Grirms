package com.yunhu.yhshxc.expand;

import android.content.Context;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.LocationResult;
import com.yunhu.yhshxc.comp.location.LocationControlListener;
import com.yunhu.yhshxc.func.StoreExpandUtil;

import java.util.ArrayList;
import java.util.List;

public class StoreModuleExpandUtil  extends StoreExpandUtil implements LocationControlListener{
	
	public static final int TYPE_STOREMODULE_NAME = -1001;//名称
	public static final int TYPE_STOREMODULE_NUM = -1002;//编号
	public static final int TYPE_STOREMODULE_LOCATION = -1003;//定位
	public static final int TYPE_STOREMODULE_ADRESS = -1004;//定位
	public static final int TYPE_STOREMODULE_LON = -1005;//经度
	public static final int TYPE_STOREMODULE_LAT = -1006;//纬度
	
	
	public StoreModuleExpandUtil(Context context) {
		super(context);
		
	}

	@Override
	public void confirmLocation(LocationResult result) {
		super.confirmLocation(result);
	}

	@Override
	public void areaSearchLocation(LocationResult result) {
		
	}

	public List<Func> getFuncs( int  targetId) {
		List<Func> gdList = new ArrayList<Func>();
		context.getResources().getString(R.string.other);
		Func func1 = func(TYPE_STOREMODULE_NAME,context.getResources().getString(R.string.storemodule_name), Func.TYPE_EDITCOMP, 1,targetId);// 输入框类型
		Func func2 = func(TYPE_STOREMODULE_NUM, context.getResources().getString(R.string.storemodule_number), Func.TYPE_EDITCOMP, 1,targetId);//输入
		locationFunc = func(TYPE_STOREMODULE_LOCATION,  context.getResources().getString(R.string.storemodule_location), Func.TYPE_LOCATION, 1,targetId);// 定位类型
		adressFunc = func(TYPE_STOREMODULE_ADRESS,  context.getResources().getString(R.string.storemodule_address), Func.TYPE_EDITCOMP, 1,targetId);// 文本类型

		gdList.add(func1);
		gdList.add(func2);
		gdList.add(locationFunc);
		gdList.add(adressFunc);

		lonFunc = func(TYPE_STOREMODULE_LON,context.getResources().getString(R.string.storemodule_location), Func.TYPE_EDITCOMP, 1,targetId);// 输入框类型
		latFunc = func(TYPE_STOREMODULE_LAT,context.getResources().getString(R.string.storemodule_latitude), Func.TYPE_EDITCOMP, 1,targetId);// 输入框类型
		return gdList;
	}

	private Func func(int i, String name, int type, int edit,int targetId) {
		Func f1 = new Func();
		f1.setFuncId(i);
		f1.setId(i);
		f1.setTargetid(targetId);
		f1.setName(name);
		f1.setType(type);
		f1.setIsEdit(edit);

		return f1;
	}
	

	
}
