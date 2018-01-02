package com.yunhu.yhshxc.order;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.database.OrderSaleDB;
import com.yunhu.yhshxc.order.bo.OrderItem;
import com.yunhu.yhshxc.order.listener.OnProductChoosedListener;
import com.yunhu.yhshxc.order.view.ProductLayout;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.widget.ToastOrder;

public class OrderSaleActivity extends Activity {
	private OrderItem data;

	private TextView txtTitle;
	private TextView txtProduct;
	private EditText edtPrice;
	private EditText edtQuantity;
	private Button btn1;
	private Button btn2;
	private LinearLayout container;
	private TextView txtName;
	private ProductLayout productLayout;
	
	private OrderConfirmDialog exitDialog;

	private OrderSaleDB db;

	private boolean isEdit;

	private String storeId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_sale_create);

		db = new OrderSaleDB(this);
		// db.deleteAll();

		txtTitle = (TextView) findViewById(R.id.title);
		txtProduct = (TextView)findViewById(R.id.product);
		container = (LinearLayout) findViewById(R.id.container);
		edtPrice = (EditText) findViewById(R.id.price);
		edtPrice.setOnKeyListener(keyListener);
		edtQuantity = (EditText) findViewById(R.id.quantity);
		edtQuantity.setOnKeyListener(keyListener);
		btn1 = (Button) findViewById(R.id.btn1);
		btn1.setOnClickListener(btnListener);
		btn2 = (Button) findViewById(R.id.btn2);
		btn2.setOnClickListener(btnListener);
		
		exitDialog = new OrderConfirmDialog(this);
		exitDialog.getMessageText().setText("退出以后未保存数据将会被清除，是否确定退出？ ");
		exitDialog.getOkButton().setOnClickListener(btnListener);
		exitDialog.getCancelButton().setOnClickListener(btnListener);

		initialize();
	}

	private void initialize() {
		data = new OrderItem();

		btn1.setText(R.string.order_sale_save_and_preview);

		Intent intent = getIntent();
		if (intent.hasExtra(Constants.ORDER_BOUNDLE_PRODUCT_ID_KEY)) {
			isEdit = true;
			data.setId(intent.getIntExtra(Constants.ORDER_BOUNDLE_ID_KEY, 0));
			data.setProductId(intent.getIntExtra(Constants.ORDER_BOUNDLE_PRODUCT_ID_KEY, 0));
			data.setName(intent.getStringExtra(Constants.ORDER_BOUNDLE_PRODUCT_NAME_KEY));
			data.setPrice(intent.getDoubleExtra(Constants.ORDER_BOUNDLE_PRODUCT_PRICE_KEY, 0));
			data.setSales(intent.getLongExtra(Constants.ORDER_BOUNDLE_PRODUCT_QUANTITY_KEY, 0));

			txtName = new TextView(this);
			txtName.setTextSize(getResources().getDimensionPixelSize(R.dimen.order_default_text_size));
			txtName.setTextColor(Color.BLACK);
			txtName.setText(data.getName());
			container.addView(txtName);

			btn2.setText(R.string.delete);
		}
		else {
			isEdit = false;
			productLayout = new ProductLayout(this);
			productLayout.setOnProductChoosedListener(chooseListener);
			container.addView(productLayout, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			
			txtProduct.setVisibility(View.GONE);

			btn2.setText(R.string.order_sale_create);
		}

		edtPrice.setText(data.getPrice() > 0 ? String.valueOf(data.getPrice()) : "");
		edtQuantity.setText(data.getSales() > 0 ? String.valueOf(data.getSales()) : "");

		storeId = getIntent().getStringExtra(Constants.ORDER_BOUNDLE_STORE_ID_KEY);
		txtTitle.setText(getIntent().getStringExtra(Constants.ORDER_BOUNDLE_TITLE_KEY));

		// test
		// data.setProductId(1);
		// data.setName("大众CC");
		// txtTitle.setText("高森明晨东大桥店");
	}

	@Override
	public void onBackPressed() {
		boolean noInput = true;
		if (!edtPrice.getText().toString().trim().equals("")) {
			noInput = false;
		}
		if (!edtQuantity.getText().toString().trim().equals("")) {
			noInput = false;
		}
		if (!isEdit && !TextUtils.isEmpty(data.getName())) {
			noInput = false;
		}
		
		if (noInput) {
			super.onBackPressed();
		}
		else {
			exitDialog.show();
		}
	}

	private boolean save() {
		if (TextUtils.isEmpty(data.getName())) {
			ToastOrder.makeText(getApplicationContext(), "请选择产品", ToastOrder.LENGTH_SHORT).show();
			return false;
		}
		
		String input = edtPrice.getText().toString();
		if (input.matches("^[0-9.]+$")) {
			double value = Double.parseDouble(input);
			if (value > 0) {
				data.setPrice(value);
				edtPrice.setTextColor(Color.BLACK);
			}
			else {
				edtPrice.setTextColor(Color.RED);
				return false;
			}
		}
		else {
			edtPrice.setTextColor(Color.RED);
			return false;
		}

		input = edtQuantity.getText().toString();
		if (input.matches("^[0-9]+$")) {
			Long value = Long.parseLong(input);
			if (value > 0) {
				data.setSales(value);
				edtQuantity.setTextColor(Color.BLACK);
			}
			else {
				edtQuantity.setTextColor(Color.RED);
				return false;
			}
		}
		else {
			edtQuantity.setTextColor(Color.RED);
			return false;
		}

		db.save(data, storeId);
		return true;
	}

	private void delete() {
		db.delete(data.getId());
	}

	private final OnProductChoosedListener chooseListener = new OnProductChoosedListener() {

		@Override
		public void onOptionsItemChoosed(Dictionary obj) {
			if (obj != null) {
				data.setProductId(Integer.parseInt(obj.getDid()));
				data.setName(obj.getCtrlCol());
			}
		}
	};

	private final View.OnKeyListener keyListener = new View.OnKeyListener() {

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (v == edtPrice)
				edtPrice.setTextColor(Color.BLACK);
			else if (v == edtQuantity)
				edtQuantity.setTextColor(Color.BLACK);
			return false;
		}
	};

	private final View.OnClickListener btnListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v == btn1) {
				if (save()) {
					finish();
				}
			}
			else if (v == btn2) {
				if (isEdit) {
					// 如果是编辑状态，则该按钮执行删除功能
					delete();
					finish();
				}
				else {
					// 如果是创建状态，则该按钮执行继续创建功能
					if (save()) {
						Intent target = new Intent(getApplicationContext(), OrderSaleActivity.class);
						target.putExtra(Constants.ORDER_BOUNDLE_STORE_ID_KEY, storeId);
						target.putExtra(Constants.ORDER_BOUNDLE_TITLE_KEY, txtTitle.getText().toString());
						target.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(target);
						finish();
					}
				}
			}
			else if (v == exitDialog.getOkButton()) {
				exitDialog.dismiss();
				finish();
			}
			else if (v == exitDialog.getCancelButton()) {
				exitDialog.dismiss();
			}
		}
	};
}
