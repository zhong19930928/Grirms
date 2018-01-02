package com.yunhu.yhshxc.order2;

import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.order2.bo.Order2;

public class OrderSearchDetailActivity extends IndicatorFragmentActivity {

	private static final int ORDER_DETAIL = 0;// 订单明细
	private static final int ORDER_EXPLAIN = 1;// 订单说明
	private TextView tv_order_client;//当前查看的订单的店面名称
	public Order2 order;//当前查看的订单
	private String orderJson;//当前查看的订单的json数据

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tv_order_client = (TextView) findViewById(R.id.tv_order_client);
		try {
			orderJson = getIntent().getStringExtra("orderJson");
			order = Order2Data.json2Order(orderJson);
			if (order!=null && !TextUtils.isEmpty(order.getStoreName())) {
				tv_order_client.setText(order.getStoreName());
			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, R.string.ERROR_DATA, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected int supplyTabs(List<TabInfo> tabs) {
		tabs.add(new TabInfo(ORDER_DETAIL, "订单明细", OrderSearchDetailFragment.class));
		tabs.add(new TabInfo(ORDER_EXPLAIN, "订单说明", OrderSearchExplainFragment.class));
		return ORDER_DETAIL;
	}

	protected int getMainViewResId() {
		return R.layout.order_detail_activity;
	}

	@Override
	public void onTextChanged(CharSequence s) {

	}

}
