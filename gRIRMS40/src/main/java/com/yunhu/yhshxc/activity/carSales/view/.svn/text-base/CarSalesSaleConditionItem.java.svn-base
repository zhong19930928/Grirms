package com.yunhu.yhshxc.activity.carSales.view;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.carSales.bo.CarSaleSalesVolume;

public class CarSalesSaleConditionItem {

	private View view;

	private LinearLayout llSalesStatistics;
	private Context context;
	private boolean isTitle;

	public CarSalesSaleConditionItem(Context context, boolean isTitle) {
		this.context = context;
		this.isTitle = isTitle;
		view = View.inflate(context, R.layout.carsales_sale_condition_item,
				null);

		llSalesStatistics = (LinearLayout) view
				.findViewById(R.id.ll_sales_statistics);

	}

	public View getView() {
		return view;
	}

	public void initData(CarSaleSalesVolume carSaleSalesVolume) {

		if (carSaleSalesVolume != null) {

			for (int i = 0; i < carSaleSalesVolume.getProductClass().size(); i++) {

				TextView test = new TextView(llSalesStatistics.getContext());
				test.setText(carSaleSalesVolume.getProductClass().get(i));
				if (i == carSaleSalesVolume.getProductClass().size() - 1) {
					if (!"价格".equals(carSaleSalesVolume.getProductClass()
							.get(i))) {
						test.setText(carSaleSalesVolume.getProductClass()
								.get(i) + "元");
					}

				}
				test.setTextColor(context.getResources()
						.getColor(R.color.black));
				test.setLayoutParams(new LayoutParams(250,
						LayoutParams.WRAP_CONTENT));
				test.setLines(2);
				test.setGravity(Gravity.CENTER_VERTICAL);
				test.setTextSize(15);
				test.setPadding(20, 0, 0, 0);
				llSalesStatistics.addView(test);

			}

		}

	}

}
