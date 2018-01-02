package com.yunhu.yhshxc.order3.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.order3.db.Order3ShoppingCartDB;

public class Order3ShoppingCar {
	private View view;
	private TextView tv_data;
	private Context context;
	public Order3ShoppingCar(Context context){
		this.context  = context;
		view = View.inflate(context, R.layout.order3_shopping_car_data, null);
		tv_data = (TextView) view.findViewById(R.id.tv_data);
		
	}
	public View getView(){
		return view;
	}
	public void initShoppingCartCount(){
		int count = new Order3ShoppingCartDB(context).count();
		if (count > 0 ) {
			tv_data.setText("购物车"+count);

		}
	}
}
