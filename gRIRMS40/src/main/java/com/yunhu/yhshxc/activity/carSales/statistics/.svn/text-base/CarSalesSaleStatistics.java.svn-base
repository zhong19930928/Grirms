package com.yunhu.yhshxc.activity.carSales.statistics;

import android.content.Context;
import android.content.Intent;

public class CarSalesSaleStatistics {
	/**
	 * 销售统计管理页面
	 * @author abby.zhao
	 */
	private Context context;

	public CarSalesSaleStatistics(Context context) {
		this.context = context;
	}

	/**
	 * 库存统计
	 */

	public void stockStatistics() {
		Intent intent = new Intent(context,
				CarSalesStockNewStatisticsActivity.class);
		intent.putExtra("stock", "in");
		context.startActivity(intent);
	}

	/**
	 * 销售统计
	 */
	public void salesStatistics() {
		Intent intent = new Intent(context,
				CarSalesSaleStatisticsActivity.class);
		context.startActivity(intent);
	}

	/**
	 * 缺货统计
	 */
	public void stockOutStatistics() {
		Intent intent = new Intent(context,
				CarSalesStockNewStatisticsActivity.class);
		intent.putExtra("stock", "out");
		context.startActivity(intent);
	}

	/**
	 * 欠款统计
	 */
	public void debtStatistics() {
		Intent intent = new Intent(context,
				CarSalesDebtStatisticsActivity.class);
		context.startActivity(intent);
	}
}
