package com.yunhu.yhshxc.order;

import gcg.org.debug.JLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.database.PSSConfDB;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.order.bo.Order;
import com.yunhu.yhshxc.order.bo.OrderItem;
import com.yunhu.yhshxc.order.bo.PSSConf;
import com.yunhu.yhshxc.order.bo.Returned;
import com.yunhu.yhshxc.print.templet.Element;
import com.yunhu.yhshxc.print.templet.Templet.DataSource;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.loopj.android.http.RequestParams;

public class OrderPurchasePreviewActivity extends Activity implements DataSource {
	private final String TAG = "OrderPurchasePreviewActivity";
	
	public static Order data;
	
	private final ArrayList<OrderItem> receiveds = new ArrayList<OrderItem>();
	private final HashMap<Integer, Returned> returneds = new HashMap<Integer, Returned>();
	
	private TextView txtTitle;
	
	private TextView txtPurchaseTitle, txtReturnedTitle, txtRealTitle;
	private LinearLayout purchaseContainer, returnedContainer, realContainer;
	private Button btnDelete;
	private Button btnSubmit;
	private Button btnPrint;

	private MyProgressDialog progressDialog;
	private OrderConfirmDialog confirmDialog;
	
	private int padding;
	private int columnWidthLeft, columnWidthCenter, columnWidthRight;
	
	private String storeId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_purchase_preview);
		
		txtTitle = (TextView)findViewById(R.id.title);

		txtPurchaseTitle = (TextView)findViewById(R.id.purchaseTitle);
		txtPurchaseTitle.setOnClickListener(btnListener);
		txtReturnedTitle = (TextView)findViewById(R.id.returnedTitle);
		txtReturnedTitle.setOnClickListener(btnListener);
		txtRealTitle = (TextView)findViewById(R.id.realTitle);
		txtRealTitle.setOnClickListener(btnListener);
		
		purchaseContainer = (LinearLayout)findViewById(R.id.purchaseContainer);
		returnedContainer = (LinearLayout)findViewById(R.id.returnedContainer);
		realContainer = (LinearLayout)findViewById(R.id.realContainer);
		
		btnDelete = (Button)findViewById(R.id.delete);
		btnDelete.setOnClickListener(btnListener);
		btnSubmit = (Button)findViewById(R.id.submit);
		btnSubmit.setOnClickListener(btnListener);
		btnPrint = (Button)findViewById(R.id.print);
		btnPrint.setOnClickListener(btnListener);
		
		confirmDialog = new OrderConfirmDialog(this);
		confirmDialog.getMessageText().setText("您确定要提交吗？");
		confirmDialog.getOkButton().setOnClickListener(btnListener);
		confirmDialog.getCancelButton().setOnClickListener(btnListener);

	    padding = getResources().getDimensionPixelSize(R.dimen.order_padding);
		
		storeId = getIntent().getStringExtra(Constants.ORDER_BOUNDLE_STORE_ID_KEY);
		txtTitle.setText(getIntent().getStringExtra(Constants.ORDER_BOUNDLE_TITLE_KEY));

		PSSConfDB pssConfDB = new PSSConfDB(this);
		PSSConf pssConf = pssConfDB.findPSSConf();
		if (!TextUtils.isEmpty(pssConf.getStockPrintStyle())) {
			btnPrint.setVisibility(View.VISIBLE);
		}

//		test();
	    
	    for (OrderItem i : data.getItems()) {
	    	if (i.getNewArrivalQuantity() > 0) {
	    		receiveds.add(i);
	    	}
	    }
	    
	    for (Returned i : data.getReturneds()) {
	    	returneds.put(i.getId(), i);
	    }
	    
	    purchaseContainer.post(new Runnable() {
			
			@Override
			public void run() {
				int w = (purchaseContainer.getWidth() - padding * 5) / 10;
				columnWidthLeft = w * 4;
				columnWidthCenter = w * 3;
				columnWidthRight = w * 3;
				
				createItems();
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		data = null;
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		exit(OrderPurchaseActivity.FINISH_TYPE_BACK);
	}

	private void createItems() {
		returneds.clear();
		
		for (int i = 0; i < data.getReturneds().size(); i++) {
			Returned r = data.getReturneds().get(i);
			
			returneds.put(r.getId(), r);
			
			ReturnedItem item = new ReturnedItem(this);
			item.update(r);
			
			returnedContainer.addView(item);
		}
		
		for (int i = 0; i < receiveds.size(); i++) {
			OrderItem oi = receiveds.get(i);
			
			ReceivedItem receivedItem = new ReceivedItem(this);
			receivedItem.update(oi);
			purchaseContainer.addView(receivedItem);
			
			Long reduce = returneds.containsKey(oi.getId()) ? returneds.get(oi.getId()).getReturnedQuantity() : 0;
			long real = oi.getNewArrivalQuantity() - reduce;
			RealItem realItem = new RealItem(this);
			realItem.update(oi, real);
			realContainer.addView(realItem);
		}
		
		if (receiveds.isEmpty()) {
			purchaseContainer.setVisibility(View.GONE);
			realContainer.setVisibility(View.GONE);
		}
		if (returneds.isEmpty()) {
			returnedContainer.setVisibility(View.GONE);
		}
	}
	
	public void exit(int type) {
		Intent data = new Intent();
		data.putExtra(OrderPurchaseActivity.BUNDLE_FINISH_KEY, type);
		setResult(RESULT_OK, data);
		finish();
	}
	
	private View.OnClickListener btnListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (v == btnDelete) {
				exit(OrderPurchaseActivity.FINISH_TYPE_CANCEL);
			}
			else if (v == btnSubmit) {
				confirmDialog.show();
			}
			else if (v == btnPrint) {
				print();
			}
			else if (v == confirmDialog.getOkButton()) {
				confirmDialog.dismiss();
				
				submit();
			}
			else if (v == confirmDialog.getCancelButton()) {
				confirmDialog.dismiss();
			}
			else if (v == txtPurchaseTitle) {
				if (purchaseContainer.getVisibility() == View.VISIBLE) {
					txtPurchaseTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.order_icon_expand, 0);
					purchaseContainer.setVisibility(View.GONE);
				}
				else {
					txtPurchaseTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.order_icon_shrink, 0);
					purchaseContainer.setVisibility(View.VISIBLE);
				}
			}
			else if (v == txtReturnedTitle) {
				if (returnedContainer.getVisibility() == View.VISIBLE) {
					txtReturnedTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.order_icon_expand, 0);
					returnedContainer.setVisibility(View.GONE);
				}
				else if (returneds.size() > 0) {
					txtReturnedTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.order_icon_shrink, 0);
					returnedContainer.setVisibility(View.VISIBLE);
				}
			}
			else if (v == txtRealTitle) {
				if (realContainer.getVisibility() == View.VISIBLE) {
					txtRealTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.order_icon_expand, 0);
					realContainer.setVisibility(View.GONE);
				}
				else {
					txtRealTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.order_icon_shrink, 0);
					realContainer.setVisibility(View.VISIBLE);
				}
			}
		}
	};
	
	private void submit() {
		progressDialog = new MyProgressDialog(OrderPurchasePreviewActivity.this, R.style.CustomProgressDialog, "正在提交...");
		progressDialog.show();
		
		RequestParams params = new RequestParams();
		params.put("orderid", data.getOrderNo());
		params.put("storeid", String.valueOf(storeId));
		try {
			JSONArray json = new JSONArray();
			for (OrderItem i : receiveds) {
				Returned r = returneds.get(i.getId());
				
				JSONObject item = new JSONObject();
				item.put("proid", String.valueOf(i.getProductId()));
				item.put("productname", i.getName());
				item.put("stockcount", String.valueOf(i.getNewArrivalQuantity()));
				item.put("id", String.valueOf(i.getId()));
				if (r != null) {
					item.put("returnedcount", String.valueOf(r.getReturnedQuantity()));
					item.put("returnedid", r.getReason().getDid());
				}
				json.put(item);
			}
			params.put("proinfo", json.toString());
		}
		catch (Exception e) {
			e.printStackTrace();
			Message msg = handler.obtainMessage();
			msg.arg1 = -1;
			handler.sendMessage(msg);
		}
		
		JLog.d(TAG, "Params:" + params.toString());

		GcgHttpClient.getInstance(this).post(UrlInfo.UrlPSSOrderStockSave(getApplicationContext()), params, httpHandler);
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
				msg.arg1 = "0000".equals(code) ? 0 : -3;
			}
			catch (JSONException e) {
				e.printStackTrace();
				msg.arg1 = -2;
			}
			handler.sendMessage(msg);
			return;			
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

			// 提交成功
			ToastOrder.makeText(getApplicationContext(), R.string.submit_ok, ToastOrder.LENGTH_SHORT).show();
			exit(OrderPurchaseActivity.FINISH_TYPE_OK);
		}
	};

	private class ReceivedItem extends FrameLayout {
		OrderItem data;
		
		TextView txtName;
		TextView txtNumber;

		ReceivedItem(Context context) {
			this(context, null, 0);
		}

		ReceivedItem(Context context, AttributeSet attrs) {
			this(context, attrs, 0);
		}

		ReceivedItem(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			preInitialize(context);
		}
		
		void preInitialize(Context context) {
			setPadding(padding, padding, padding, padding);
			
			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(columnWidthLeft, LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL | Gravity.LEFT);
			txtName = new TextView(context);
			txtName.setTextColor(Color.BLACK);
			txtName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
			txtName.setGravity(Gravity.LEFT);
			addView(txtName, params);
			
			txtNumber = new TextView(context);
			txtNumber.setTextColor(getResources().getColor(R.color.blue));
			txtNumber.getPaint().setFakeBoldText(true);
			txtNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
			txtNumber.setGravity(Gravity.LEFT);
			addView(txtNumber, new FrameLayout.LayoutParams(columnWidthRight, LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL | Gravity.RIGHT));
		}
		
		void update(OrderItem data) {
			this.data = data;
			
			txtName.setText(data.getName());
			txtNumber.setText(String.valueOf(data.getNewArrivalQuantity()));
		}
	}
	
	private class ReturnedItem extends FrameLayout {
		Returned data;
		
		TextView txtName;
		TextView txtReason;
		TextView txtNumber;

		ReturnedItem(Context context) {
			this(context, null, 0);
		}

		ReturnedItem(Context context, AttributeSet attrs) {
			this(context, attrs, 0);
		}

		ReturnedItem(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			preInitialize(context);
		}
		
		void preInitialize(Context context) {
			setPadding(padding, padding, padding, padding);
			
			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(columnWidthLeft, LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL | Gravity.LEFT);
			txtName = new TextView(context);
			txtName.setTextColor(Color.BLACK);
			txtName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
			txtName.setGravity(Gravity.LEFT);
			addView(txtName, params);
			
			params = new FrameLayout.LayoutParams(columnWidthCenter, LayoutParams.WRAP_CONTENT, Gravity.CENTER);
			txtReason = new TextView(context);
			txtReason.setTextColor(Color.BLACK);
			txtReason.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
			txtReason.setGravity(Gravity.LEFT);
			addView(txtReason, params);
			
			params = new FrameLayout.LayoutParams(columnWidthRight, LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL | Gravity.RIGHT);
			txtNumber = new TextView(context);
			txtNumber.setTextColor(Color.BLACK);
			txtNumber.setTextColor(getResources().getColor(R.color.blue));
			txtNumber.getPaint().setFakeBoldText(true);
			txtNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
			txtNumber.setGravity(Gravity.LEFT);
			addView(txtNumber, params);
		}
		
		void update(Returned data) {
			this.data = data;
			
			txtName.setText(data.getName());
			txtReason.setText(data.getReason().getCtrlCol());
			txtNumber.setText(String.valueOf(data.getReturnedQuantity()));
		}
	}
	
	private class RealItem extends FrameLayout {
		OrderItem data;
		long realQuantity;
		
		TextView txtName;
		TextView txtNumber;
		TextView txtNew;

		RealItem(Context context) {
			this(context, null, 0);
		}

		RealItem(Context context, AttributeSet attrs) {
			this(context, attrs, 0);
		}

		RealItem(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			preInitialize(context);
		}
		
		void preInitialize(Context context) {
			setPadding(padding, padding, padding, padding);
			
			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(columnWidthLeft, LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL | Gravity.LEFT);
			txtName = new TextView(context);
			txtName.setTextColor(Color.BLACK);
			txtName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
			txtName.setGravity(Gravity.LEFT);
			addView(txtName, params);
			
			params = new FrameLayout.LayoutParams(columnWidthCenter, LayoutParams.WRAP_CONTENT, Gravity.CENTER);
			txtNumber = new TextView(context);
			txtNumber.setTextColor(Color.BLACK);
			txtNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
			txtNumber.getPaint().setFakeBoldText(true);
			txtName.setGravity(Gravity.LEFT);
			addView(txtNumber, params);
			
			params = new FrameLayout.LayoutParams(columnWidthRight, LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL | Gravity.RIGHT);
			txtNew = new TextView(context);
			txtNew.setTextColor(getResources().getColor(R.color.blue));
			txtNew.getPaint().setFakeBoldText(true);
			txtNew.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
			txtNew.setGravity(Gravity.LEFT);
			addView(txtNew, params);
		}
		
		void update(OrderItem data, long realQuantity) {
			this.data = data;
			this.realQuantity = realQuantity;
			
			txtName.setText(data.getName());
			txtNumber.setText(String.valueOf(data.getTotal() - data.getArrivalQuantity()));
			txtNew.setText(String.valueOf(realQuantity));
		}
	}
	
	private void print() {
		PSSConfDB pssConfDB = new PSSConfDB(this);
		PSSConf pssConf = pssConfDB.findPSSConf();
		try {
			MenuOrderActivity.print(this, new JSONArray(pssConf.getStockPrintStyle()), this);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void printLoop(List<Element> columns) {
		SharedPreferencesUtil prefs = SharedPreferencesUtil.getInstance(this);
		
		for (int i = 0; i < receiveds.size(); i++) {
			OrderItem oi = receiveds.get(i);
			Long reduce = returneds.containsKey(oi.getId()) ? returneds.get(oi.getId()).getReturnedQuantity() : 0;
			long real = oi.getNewArrivalQuantity() - reduce;

			for (Element e : columns) {
				if (e.getType() == Element.TYPE_TEXT) {
					switch (e.getVariable()) {
						case 1://公司
							String tmp = prefs.getCompanyName();
							e.setContent(tmp);
							break;
							
						case 2://用户
							tmp = prefs.getUserName();
							e.setContent(tmp);
							break;
							
						case 3://订单号
							e.setContent(data.getOrderNo());
							break;
						
						case 4://产品
							e.setContent(oi.getName());
							break;
							
						case 5://数量
							e.setContent(String.valueOf(real));
							break;
							
						case 6://单价
							e.setContent(String.valueOf(oi.getPrice()));
							break;
							
						case 7://合计
							e.setContent(OrderCalculate.computeMultiply(String.valueOf(real), String.valueOf(oi.getPrice())));
							break;
							
						case 8://总计
							e.setContent(String.valueOf(computeTotal()));
							break;
							
						case 9://订单时间
							e.setContent(data.getDate());
							break;
							
						case 11:
						case 12:
							continue;
							
						case 13://店面
							e.setContent(txtTitle.getText().toString());
							break;
							
						case 14://电话
							e.setContent(PublicUtils.receivePhoneNO(getApplicationContext()));
							break;
					}
				}
				MenuOrderActivity.printElement(e);
			}
			MenuOrderActivity.printNewLine();
		}
	}

	@Override
	public void printRow(List<Element> columns) {
		SharedPreferencesUtil prefs = SharedPreferencesUtil.getInstance(this);
		
		for (Element e : columns) {
			if (e.getType() == Element.TYPE_TEXT) {
				switch (e.getVariable()) {
					case 1://公司
						String tmp = prefs.getCompanyName();
						e.setContent(tmp);
						break;
						
					case 2://用户
						tmp = prefs.getUserName();
						e.setContent(tmp);
						break;
						
					case 3://订单号
						e.setContent(data.getOrderNo());
						break;
						
					case 8://总计
						e.setContent(String.valueOf(computeTotal()));
						break;
						
					case 9://订单时间
						e.setContent(data.getDate());
						break;
						
					case 13://店面
						e.setContent(txtTitle.getText().toString());
						break;
						
					case 14://电话
						e.setContent(PublicUtils.receivePhoneNO(getApplicationContext()));
						break;
				}
			}
			MenuOrderActivity.printElement(e);
		}
	}
	
	private double computeTotal() {
		double total = 0;
		for (int i = 0; i < receiveds.size(); i++) {
			OrderItem oi = receiveds.get(i);
			Long reduce = returneds.containsKey(oi.getId()) ? returneds.get(oi.getId()).getReturnedQuantity() : 0;
			long real = oi.getNewArrivalQuantity() - reduce;
			total += Double.parseDouble(OrderCalculate.computeMultiply(String.valueOf(real), String.valueOf(oi.getPrice())));
		}
		return total;
	}

	@Override
	public void printPromotion(List<Element> columns) {
		
	}

	@Override
	public void printZhekou(List<Element> columns) {
		
	}

	@Override
	public void printDingdanbianhao(List<Element> columns) {
		
	}
}
