package com.yunhu.yhshxc.doubletask2;

import android.os.Bundle;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.list.activity.TableListActivity;

import java.util.HashMap;

public class ChildrenListActivity extends TableListActivity {
	
	private String doubleBtnType;
	private String doubleMasterTaskNo;

	@Override
	protected void init() {
		super.init();
		doubleBtnType = bundle.getString(NewDoubleDetailActivity.DOUBLE_BTN_TYPE);
		doubleMasterTaskNo = bundle.getString(NewDoubleDetailActivity.DOUBLE_MASTER_TASK_NO);
	}

	/**
	 * 查询参数
	 */
	@Override
	protected HashMap<String, String> getSearchParams() {
		if (bundle != null && !bundle.isEmpty()) {}
		HashMap<String, String> searchParams = super.getSearchParams();
		searchParams.put("doubleBtnType", doubleBtnType);
		searchParams.put("doubleMasterTaskNo", doubleMasterTaskNo);
		return searchParams;
	}

	/**
	 * 按钮的名字
	 */
	@Override
	public String getButtonName() {
		return getResources().getString(R.string.into);
	}

	@Override
	protected Bundle setQueryBundle() {
		Bundle b = super.setQueryBundle();
		b.putString(NewDoubleDetailActivity.DOUBLE_BTN_TYPE, doubleBtnType);
		b.putString(NewDoubleDetailActivity.DOUBLE_MASTER_TASK_NO, doubleMasterTaskNo);
		return b; 
	}

	
}
