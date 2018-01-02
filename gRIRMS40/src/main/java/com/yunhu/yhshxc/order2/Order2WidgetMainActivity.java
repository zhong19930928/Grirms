package com.yunhu.yhshxc.order2;

import gcg.org.debug.JLog;
import android.content.Intent;
import android.os.Bundle;

public class Order2WidgetMainActivity extends Order2MainActivity {
	protected Bundle newOrderBundle;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {// 非正常关闭
			JLog.d(TAG, "<--Activity非正常关闭过!!-->");
			restoreConstants(savedInstanceState);
		}else{
			newOrderBundle  = getIntent().getBundleExtra("newOrderBundle");
		}
	}
	

	/**
	 * 创建订单
	 */
	@Override
	protected void create() {
		Intent intent = new Intent(Order2WidgetMainActivity.this, OrderWidgetCreateActivity.class);
		intent.putExtra("newOrderBundle", newOrderBundle);
		startActivity(intent);
	}
	
	/**
	 * 查询订单
	 */
	@Override
	protected void search() {
		Intent intent = new Intent(Order2WidgetMainActivity.this, OrderWidgetSearchListActivity.class);
		intent.putExtra("newOrderBundle", newOrderBundle);
		startActivity(intent);
	}
	
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBundle("newOrderBundle", newOrderBundle);
		super.onSaveInstanceState(outState);
	}

	/**
	 * 读取数据-->非正常状态下使用
	 */
	@Override
	protected void restoreConstants(Bundle savedInstanceState) {
		super.restoreConstants(savedInstanceState);
		newOrderBundle = savedInstanceState.getBundle("newOrderBundle");
	}
}
