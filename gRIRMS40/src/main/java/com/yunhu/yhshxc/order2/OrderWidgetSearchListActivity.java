package com.yunhu.yhshxc.order2;

import com.loopj.android.http.RequestParams;

public class OrderWidgetSearchListActivity extends OrderSearchListActivity {

	@Override
	protected RequestParams orderSearchRequestParams() {
		String storeId = getIntent().getBundleExtra("newOrderBundle").getString("storeId");
		RequestParams param = super.orderSearchRequestParams();
		param.put("storeId", storeId);
		return param;
	}
}
