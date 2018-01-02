package com.yunhu.yhshxc.activity.carSales.view;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesDebtListItem;
import com.yunhu.yhshxc.utility.PublicUtils;

public class CarSalesDebtSearchResultItem {

	private View view;
	private Context context;
	private CarSalesDebtListItem item;
	private LinearLayout llDebtResultItem;
	private TextView tv_result_item_number;
	private TextView tv_result_item;
	private TextView tv_debt_item_name;
	private TextView tv_debt_totalprice;

	public CarSalesDebtSearchResultItem(Context mContext,
			CarSalesDebtListItem data) {
		this.context = mContext;
		view = View.inflate(mContext,
				R.layout.carsales_debt_search_result_item, null);

		llDebtResultItem = (LinearLayout) view
				.findViewById(R.id.ll_debt_result_item);

		tv_debt_item_name = (TextView) view
				.findViewById(R.id.tv_debt_item_name);

		tv_debt_totalprice = (TextView) view
				.findViewById(R.id.tv_debt_totalprice);

		String total_price = "0.00";
		for (int i = 0; i < data.getData().size(); i++) {
			RelativeLayout rl_item = new RelativeLayout(context);
			tv_result_item_number = new TextView(context);
			tv_result_item_number.setText(data.getData().get(i).getTime());
			float dimen = context.getResources().getDimension(
					R.dimen.car_sales_debt_data);
			tv_result_item_number.setTextSize((int) dimen);
			tv_result_item_number.setPadding(20, 0, 0, 0);
			tv_result_item_number.setTextColor(context.getResources().getColor(
					R.color.nearby_text_2));
			rl_item.addView(tv_result_item_number);

			tv_result_item = new TextView(context);
			String price = PublicUtils.formatDouble(Math.abs(Double.parseDouble(data
					.getData().get(i).getPrice()))) + "元";

			tv_result_item.setText(price);
			tv_result_item.setTextSize((int) dimen);

			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,
					RelativeLayout.TRUE);
			tv_result_item.setLayoutParams(params);
			tv_result_item.setPadding(0, 0, 20, 0);
			tv_result_item.setTextColor(context.getResources().getColor(
					R.color.nearby_text_2));

			rl_item.addView(tv_result_item);

			llDebtResultItem.addView(rl_item);
		}
		String totalPrice = "";
		totalPrice = PublicUtils.formatDouble(Math.abs(Double.parseDouble(data.getResultTotalPrice()))) + "元";
		tv_debt_totalprice.setText(totalPrice);

	}

	public void setData(CarSalesDebtListItem data) {
		this.item = data;

		tv_debt_item_name.setText(data.getResultName());

	}

	public View getView() {
		// view.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// Toast.makeText(context, item.getTag_1(), Toast.LENGTH_SHORT).show();
		// }
		// });
		return view;
	}
}
