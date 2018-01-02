package com.yunhu.yhshxc.activity.carSales.view;

import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.carSales.util.CarSalesUtil;

public class CarSalesStockSearchResultItem {
	/**
	 * 库存统计查询结果展示界面
	 * 
	 * @author abby_zhao
	 */

	private View view;

	private Context context;
	private boolean isTitle;
	private LinearLayout ll_Stock;

	public CarSalesStockSearchResultItem(Context context, boolean isTitle) {
		this.context = context;
		this.isTitle = isTitle;
		view = View.inflate(context, R.layout.activity_carsales_stock_item,
				null);

		ll_Stock = (LinearLayout) view.findViewById(R.id.ll_stock_item);

	}

	public View getView() {
		return view;
	}

	public void initData(List<String> itemlist, Boolean stock) {

		if (isTitle) {
			List<String> list;
			if (stock) {
				list = new CarSalesUtil(context).carStockTitle();
			} else {
				list = new CarSalesUtil(context).carStockOTitle();
			}

			view.setBackgroundColor(context.getResources().getColor(
					R.color.order3_search_yulan_bg));

			for (int i = 0; i < list.size(); i++) {

				TextView test = new TextView(ll_Stock.getContext());
				test.setText(list.get(i));
				test.setTextColor(context.getResources()
						.getColor(R.color.black));

				test.setLayoutParams(new LayoutParams(250,
						LayoutParams.WRAP_CONTENT));
				test.setLines(2);
				test.setGravity(Gravity.CENTER_VERTICAL);
				test.setTextSize(15);
				test.setPadding(20, 0, 0, 0);
				ll_Stock.addView(test);

			}

		} else {
			int size;
			if (stock) {
				size = itemlist.size();
			} else {
				size = itemlist.size() - 1;
			}

			for (int i = 0; i < size; i++) {

				TextView test = new TextView(ll_Stock.getContext());
				test.setText(itemlist.get(i));
				test.setTextColor(context.getResources()
						.getColor(R.color.black));

				test.setLayoutParams(new LayoutParams(250,
						LayoutParams.WRAP_CONTENT));
				test.setLines(2);
				test.setGravity(Gravity.CENTER_VERTICAL);
				test.setTextSize(15);
				test.setPadding(20, 0, 0, 0);
				ll_Stock.addView(test);

			}

		}

	}

}
