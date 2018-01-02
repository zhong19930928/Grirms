package com.yunhu.yhshxc.order2;

import android.content.Intent;

public class Order2LinkWidgetMainActivity extends Order2WidgetMainActivity {
	
	@Override
	protected void create() {
		Intent intent = new Intent(Order2LinkWidgetMainActivity.this, OrderLinkWidgetCreateActivity.class);
		intent.putExtra("newOrderBundle", newOrderBundle);
		startActivity(intent);
	}
}
