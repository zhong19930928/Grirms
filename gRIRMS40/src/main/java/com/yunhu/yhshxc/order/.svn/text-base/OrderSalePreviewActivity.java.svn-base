package com.yunhu.yhshxc.order;

import gcg.org.debug.JLog;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.database.OrderSaleDB;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.order.bo.OrderItem;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.loopj.android.http.RequestParams;

public class OrderSalePreviewActivity extends Activity {
	private final String TAG = "OrderSalePreviewActivity";
	
	private final ArrayList<OrderItem> data = new ArrayList<OrderItem>();
	
	private TextView txtTitle;
	private ListView lst;
	private Button btnSubmit;
	private Button btnCreate;
	
	private MyProgressDialog progressDialog;
	private OrderConfirmDialog confirmDialog;
	
	private OrderSaleDB db;
	
	private final DecimalFormat priceFormat = new DecimalFormat("#.00");

	private int defaultTextSize;
	private int padding;
	private int widthItemName, widthItemCount, widthItemState;

	private String storeId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_sale_preview);
		
		db = new OrderSaleDB(this);
		
		txtTitle = (TextView)findViewById(R.id.title);
		lst = (ListView)findViewById(R.id.lst);
		btnSubmit = (Button)findViewById(R.id.submit);
		btnSubmit.setOnClickListener(btnListener);
		btnCreate = (Button)findViewById(R.id.create);
		btnCreate.setOnClickListener(btnListener);

		progressDialog = new MyProgressDialog(this, R.style.CustomProgressDialog, getResources().getString(R.string.initTable));

		confirmDialog = new OrderConfirmDialog(this);
		confirmDialog.getMessageText().setText("您确定要提交吗？");
		confirmDialog.getOkButton().setOnClickListener(btnListener);
		confirmDialog.getCancelButton().setOnClickListener(btnListener);

		defaultTextSize = getResources().getDimensionPixelSize(R.dimen.order_default_text_size);
		padding = getResources().getDimensionPixelSize(R.dimen.order_padding);

		lst.post(new Runnable() {

			@Override
			public void run() {
				int width = lst.getWidth();
				int w = (lst.getWidth() - padding * 2) / 12;
				widthItemName = w * 4;
				widthItemCount = w * 6;
				widthItemState = w * 2;

				lst.setAdapter(adapter);
				adapter.notifyDataSetChanged();
			}
		});

		storeId = getIntent().getStringExtra(Constants.ORDER_BOUNDLE_STORE_ID_KEY);
		txtTitle.setText(getIntent().getStringExtra(Constants.ORDER_BOUNDLE_TITLE_KEY));
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		data.clear();
		data.addAll(db.getAll(storeId));
		adapter.notifyDataSetChanged();
	}

	private final View.OnClickListener btnListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (v instanceof Button) {
				if (v == btnSubmit) {
					if (data.isEmpty()) {
						ToastOrder.makeText(getApplicationContext(), "没有数据可以提交", ToastOrder.LENGTH_SHORT).show();
						return;
					}
					confirmDialog.show();
				}
				else if (v == btnCreate) {
					Intent target = new Intent(getApplicationContext(), OrderSaleActivity.class);
					target.putExtra(Constants.ORDER_BOUNDLE_STORE_ID_KEY, storeId);
					target.putExtra(Constants.ORDER_BOUNDLE_TITLE_KEY, txtTitle.getText());
					startActivity(target);
				}
				else if (v == confirmDialog.getOkButton()) {
					confirmDialog.dismiss();
					
					submit();
				}
				else if (v == confirmDialog.getCancelButton()) {
					confirmDialog.dismiss();
				}
			}
			else if (v instanceof ListItem) {

				ListItem item = (ListItem)v;
				Intent target = new Intent(getApplicationContext(), OrderSaleActivity.class);
				target.putExtra(Constants.ORDER_BOUNDLE_STORE_ID_KEY, storeId);
				target.putExtra(Constants.ORDER_BOUNDLE_TITLE_KEY, txtTitle.getText());
				target.putExtra(Constants.ORDER_BOUNDLE_ID_KEY, item.data.getId());
				target.putExtra(Constants.ORDER_BOUNDLE_PRODUCT_ID_KEY, item.data.getProductId());
				target.putExtra(Constants.ORDER_BOUNDLE_PRODUCT_NAME_KEY, item.data.getName());
				target.putExtra(Constants.ORDER_BOUNDLE_PRODUCT_PRICE_KEY, item.data.getPrice());
				target.putExtra(Constants.ORDER_BOUNDLE_PRODUCT_QUANTITY_KEY, item.data.getSales());
				startActivity(target);
			}
		}
	};

	private final BaseAdapter adapter = new BaseAdapter() {
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ListItem item = convertView == null ? new ListItem(getApplicationContext()) : (ListItem)convertView;
			item.update(data.get(position));
			return item;
		}
		
		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public Object getItem(int position) {
			return data.get(position);
		}
		
		@Override
		public int getCount() {
			return data.size();
		}
	};

	private class ListItem extends FrameLayout {
		private OrderItem data;
		private TextView txtName;
		private TextView txtSales;
		private TextView txtPrice;
		private TextView txtTotal;

		private ListItem(Context context) {
			this(context, null, 0);
		}

		private ListItem(Context context, AttributeSet attrs) {
			this(context, attrs, 0);
		}

		public ListItem(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			preInitialize(context);
		}

		private void preInitialize(Context context) {
			setOnClickListener(btnListener);
			setPadding(padding, padding, padding, padding);

			txtName = new TextView(context);
			txtName.setTextColor(Color.BLACK);
			txtName.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize);
			txtName.setGravity(Gravity.CENTER_VERTICAL);
			txtName.setLines(2);
			txtName.setEllipsize(TruncateAt.END);
			addView(txtName, new FrameLayout.LayoutParams(widthItemName, LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL | Gravity.LEFT));

			txtSales = new TextView(context);
			txtSales.setTextColor(Color.BLACK);
			txtSales.getPaint().setFakeBoldText(true);
			txtSales.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize);
			txtSales.setGravity(Gravity.CENTER_VERTICAL);
			txtSales.setLines(2);
			txtSales.setEllipsize(TruncateAt.END);
			addView(txtSales, new FrameLayout.LayoutParams(widthItemState, LayoutParams.WRAP_CONTENT, Gravity.CENTER));

			final LinearLayout amountContainer = new LinearLayout(context);
			amountContainer.setOrientation(LinearLayout.VERTICAL);
			amountContainer.setGravity(Gravity.RIGHT);
			addView(amountContainer, new FrameLayout.LayoutParams(widthItemCount, LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL | Gravity.RIGHT));

			txtPrice = new TextView(context);
			txtPrice.setTextColor(getResources().getColor(R.color.blue));
			txtPrice.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize);
			txtPrice.setGravity(Gravity.RIGHT);
			amountContainer.addView(txtPrice, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

			txtTotal = new TextView(context);
			txtTotal.setTextColor(getResources().getColor(R.color.blue));
			txtTotal.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize);
			txtTotal.setGravity(Gravity.RIGHT);
			amountContainer.addView(txtTotal, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		}

		private void update(OrderItem data) {
			this.data = data;

			txtName.setText(data.getName());

			txtSales.setText(String.valueOf(data.getSales()));

			String value = priceFormat.format(data.getPrice());
			if (value.charAt(0) == '.')
				value = "0" + value;
			txtPrice.setText("单价：" + value);

			value = OrderCalculate.computeMultiply(String.valueOf(data.getSales()), String.valueOf(data.getPrice()));
			if (value.charAt(0) == '.')
				value = "0" + value;
			txtTotal.setText("小计：" + value);
		}
	}
	
	private void submit() {
		progressDialog.show();
		
		RequestParams params = new RequestParams();
		params.put("storeid", storeId);
		params.put("storename", txtTitle.getText().toString());
		try {
			JSONArray json = new JSONArray();
			for (OrderItem i : data) {
				JSONObject item = new JSONObject();
				item.put("proid", String.valueOf(i.getProductId()));
				item.put("productname", i.getName());
				item.put("salesvolume", String.valueOf(i.getSales()));
				item.put("salesprice", String.valueOf(i.getPrice()));
				json.put(item);
			}
			params.put("proinfo", json.toString());
		}
		catch (Exception e) {
			e.printStackTrace();
			ToastOrder.makeText(getApplicationContext(), "提交数据失败", ToastOrder.LENGTH_SHORT).show();
			return;
		}
		
		JLog.d(TAG, "Params:" + params.toString());
		
		GcgHttpClient.getInstance(this).post(UrlInfo.UrlPSSOrderSalesSave(getApplicationContext()), params, httpHandler);
	}
	
	private final HttpResponseListener httpHandler = new HttpResponseListener() {

		
		@Override
		public void onFailure(Throwable error, String content) {
			Message msg = handler.obtainMessage();
			msg.arg1 = -5;
			handler.sendMessage(msg);
		}

		@Override
		public void onStart() {
			
		}

		@Override
		public void onFinish() {
			
		}

		@Override
		public void onSuccess(int statusCode, String content) {
			JLog.d(TAG, "Result:" + content);
			
			Message msg = handler.obtainMessage();
			
			if (TextUtils.isEmpty(content)) {
				msg.arg1 = -4;
				handler.sendMessage(msg);
				return;
			}

			try {
				JSONObject jData = new JSONObject(content);
				String code = jData.getString("resultcode");
				if ("0000".equals(code)) {
					db.deleteAll(storeId);
					msg.arg1 = 0;
				}
				else {
					msg.arg1 = -3;
				}
			}
			catch (JSONException e) {
				e.printStackTrace();
				msg.arg1 = -2;
			}
			handler.sendMessage(msg);
		}
	};
	
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			progressDialog.dismiss();

			if (msg.arg1 != 0) {
				// 解析异常
				ToastOrder.makeText(getApplicationContext(), "提交数据失败", ToastOrder.LENGTH_SHORT).show();
				return;
			}

			//  提交成功
			ToastOrder.makeText(getApplicationContext(), R.string.submit_ok, ToastOrder.LENGTH_SHORT).show();
			finish();
		}
	};
}
