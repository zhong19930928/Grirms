package com.yunhu.yhshxc.order;

import gcg.org.debug.JLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.order.OrderSingleReturnedDialog.AddReturnedCallback;
import com.yunhu.yhshxc.order.bo.Order;
import com.yunhu.yhshxc.order.bo.OrderItem;
import com.yunhu.yhshxc.order.bo.Returned;
import com.yunhu.yhshxc.parser.ParsePSS;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.loopj.android.http.RequestParams;

public class OrderPurchaseActivity extends Activity {
	private final String TAG = "OrderPurchaseActivity";
	
	public static final String BUNDLE_FINISH_KEY = "finish";
	public static final int FINISH_TYPE_OK = 0;
	public static final int FINISH_TYPE_BACK = 1;
	public static final int FINISH_TYPE_CANCEL = 2;

	private TextView txtTitle;
	private Spinner spiOrder;
	private Button btnReturned;
	private Button btnPreview;

	private TextView txtPurchaseTitle, txtReturnedTitle;
	private LinearLayout purchaseContainer, returnedContainer;

	private MyProgressDialog progressDialog;
	private OrderSingleReturnedDialog returnedDialog;
	private OrderConfirmDialog exitDialog;

	private final List<Order> data = new ArrayList<Order>();
	private ArrayAdapter adapter;

	private Order currentOrder;
	private String selectOrderId = null;

	private int defaultTextSize;
	private int padding;
	private int columnWidth1, columnWidth2, columnWidth3, columnWidth4;

	private ParsePSS parser;

	private String storeId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_purchase);

		parser = new ParsePSS(this);

		txtTitle = (TextView) findViewById(R.id.title);

		txtPurchaseTitle = (TextView) findViewById(R.id.purchaseTitle);
		txtPurchaseTitle.setOnClickListener(btnListener);
		txtReturnedTitle = (TextView) findViewById(R.id.returnedTitle);
		txtReturnedTitle.setOnClickListener(btnListener);

		purchaseContainer = (LinearLayout) findViewById(R.id.purchaseContainer);
		returnedContainer = (LinearLayout) findViewById(R.id.returnedContainer);

		spiOrder = (Spinner) findViewById(R.id.selectOrder);
		btnReturned = (Button) findViewById(R.id.returned);
		btnPreview = (Button) findViewById(R.id.preview);

		btnReturned.setOnClickListener(btnListener);
		btnPreview.setOnClickListener(btnListener);

		progressDialog = new MyProgressDialog(this, R.style.CustomProgressDialog, getResources().getString(R.string.initTable));
		
		exitDialog = new OrderConfirmDialog(this);
		exitDialog.getMessageText().setText(R.string.BACKDIALOG);
		exitDialog.getOkButton().setOnClickListener(btnListener);
		exitDialog.getCancelButton().setOnClickListener(btnListener);

		defaultTextSize = getResources().getDimensionPixelSize(R.dimen.order_default_text_size);
		padding = getResources().getDimensionPixelSize(R.dimen.order_padding);

		storeId = getIntent().getStringExtra(Constants.ORDER_BOUNDLE_STORE_ID_KEY);
		txtTitle.setText(getIntent().getStringExtra(Constants.ORDER_BOUNDLE_TITLE_KEY));

		adapter = new ArrayAdapter<Order>(this, android.R.layout.simple_spinner_item, data);
		adapter.setDropDownViewResource(R.layout.sprinner_check_item2);
		spiOrder.setAdapter(adapter);
		spiOrder.setOnItemSelectedListener(spinnerItemListener);

		// test();

		purchaseContainer.post(new Runnable() {

			@Override
			public void run() {
				int width = purchaseContainer.getWidth() - padding * 2;
				float unit = width / 11;

				columnWidth1 = (int) (4 * unit);
				columnWidth2 = (int) (4 * unit);
				columnWidth3 = (int) (2 * unit);
				columnWidth4 = (int) (1 * unit);

				query();
			}
		});
	}

	private void test() {
		txtTitle.setText("高森明晨东大桥店");

		Random r = new Random(System.currentTimeMillis());

		int id = 20130702;
		for (int i = 0; i < 10; i++) {
			Order order = new Order();
			order.setOrderNo(String.valueOf(id++));
			order.setItems(new ArrayList<OrderItem>());
			order.setReturneds(new ArrayList<Returned>());

			int n = r.nextInt(5) + 1;
			for (int j = 0; j < n; j++) {
				OrderItem rc = new OrderItem();
				rc.setName(String.valueOf(j + 1));
				rc.setArrivalQuantity(r.nextInt(10000) + 1);
				order.getItems().add(rc);

				// Returned rd = new Returned();
				// rd.setName(rc.getName());
				// Dictionary reason = new Dictionary();
				// reason.setDid(String.valueOf(1));
				// reason.setCtrlCol("AAA");
				// rd.setReason(reason);
				// order.getReturneds().add(rd);
			}

			data.add(order);
		}
	}

	private void dataChanged(Order selected) {
		for (int i = 0; i < selected.getItems().size(); i++) {
			ReceivedItem item = new ReceivedItem(getApplicationContext());
			item.update(selected.getItems().get(i));
			purchaseContainer.addView(item);
		}

		for (int i = 0; i < selected.getReturneds().size(); i++) {
			ReturnedItem item = new ReturnedItem(getApplicationContext());
			item.update(selected.getReturneds().get(i));
			returnedContainer.addView(item);
		}

		currentOrder = selected;
		selectOrderId = selected.getOrderNo();
	}

	private void showReturnedDialog() {
		ArrayList<OrderItem> valids = new ArrayList<OrderItem>();
		ArrayList<Returned> returneds = new ArrayList<Returned>();
		int returnedCount = 0;//当前退货列表中已有的退货数
		for (OrderItem i : currentOrder.getItems()) {
			// 只有用户输入的进货数大于0，且退货列表中不存在该产品时，该产品才会显示在弹出对话框中
			if (i.getNewArrivalQuantity() > 0) {
				boolean isReturned = false;
				for (Returned r : currentOrder.getReturneds()) {
					if (r.getId() == i.getId()) {
						isReturned = true;
						break;
					}
				}
				if (!isReturned) {
					valids.add(i);
				}
			}
		}

		int width = getWindow().getDecorView().getWidth() - 20;
		returnedDialog = new OrderSingleReturnedDialog(this);
		returnedDialog.initialize(width, "选择产品和到退原因", valids, addReturnedCallback);
		returnedDialog.show();
	}

	private void initViews() {
		purchaseContainer.removeAllViews();
		returnedContainer.removeViews(1, returnedContainer.getChildCount() - 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		int type = data.getIntExtra(BUNDLE_FINISH_KEY, FINISH_TYPE_OK);
		switch (type) {
			case FINISH_TYPE_OK:
				initViews();
				query();
				break;

			case FINISH_TYPE_CANCEL:
				for (OrderItem i : currentOrder.getItems()) {
					i.setNewArrivalQuantity(0);
				}
				currentOrder.getReturneds().clear();
				initViews();
				dataChanged(currentOrder);
				break;

			case FINISH_TYPE_BACK:
				break;
		}
	}

	@Override
	public void onBackPressed() {
		if (hasInput())//如果有输入就询问用户是否退出
			exitDialog.show();
		else
			super.onBackPressed();
	}

	/**
	 * 将用户输入的数据存入相关JavaBean中
	 */
	private boolean saveInput() {
		boolean error = false;
		
		if (currentOrder == null)
			return error;
		
		for (OrderItem i : currentOrder.getItems()) {
			i.setNewArrivalQuantity(0);
		}
		
		for (int i = 0; i < currentOrder.getItems().size(); i++) {
			ReceivedItem item = (ReceivedItem) purchaseContainer.getChildAt(i);
			String str = item.edtNumber.getText().toString();
			if (str.matches("^[0-9]+$")) {
				long quantity = Long.parseLong(str);
				if (quantity > 0) {
					long max = item.data.getTotal() - item.data.getArrivalQuantity();
					long min = 0;
					for (Returned r : currentOrder.getReturneds()) {
						if (item.data.getId() == r.getId()) {//如果退货列表中已有当前产品，则还需要考虑退货数量，即用户输入的进货量不能大于退货量
							if (r.getReturnedQuantity() < max) {
								min = r.getReturnedQuantity();
							}
							break;
						}
					}
					
					if (quantity >= min && quantity <= max) {
						item.data.setNewArrivalQuantity(quantity);
					}
					else {
						item.edtNumber.setTextColor(Color.RED);
						error = true;
					}
				}
			}
		}
		
		return error;
	}
	
	private boolean hasInput() {
		boolean has = false;
		
		for (int i = 0; currentOrder != null && i < currentOrder.getItems().size(); i++) {
			ReceivedItem item = (ReceivedItem) purchaseContainer.getChildAt(i);
			String str = item.edtNumber.getText().toString();
			if (str.matches("^[0-9]+$")) {
				long quantity = Long.parseLong(str);
				if (quantity > 0) {
					has = true;
					break;
				}
			}
		}
		return has;
	}

	private AddReturnedCallback addReturnedCallback = new OrderSingleReturnedDialog.AddReturnedCallback() {

		@Override
		public void add(OrderItem product, Dictionary reason, long quantity) {
			if (product == null || reason == null || quantity <= 0)
				return;

			System.out.println("【Select】Product:" + product.getName() + " Reason:" + reason.getCtrlCol() + " Quantity:" + quantity);
			Returned rd = null;
			boolean has = false;
			for (Returned r : currentOrder.getReturneds()) {
				if (r.getId() == product.getId()) {
					rd = r;
					has = true;
				}
			}
			
			if (!has) {
				rd = new Returned();
			}
			rd.setId(product.getId());
			rd.setName(product.getName());
			rd.setReason(reason);
			rd.setReturnedQuantity(quantity);

			OrderItem selected = null;
			for (OrderItem i : currentOrder.getItems()) {
				if (i.equals(rd.getName())) {
					selected = i;
					break;
				}
			}

			if (!has) {
				currentOrder.getReturneds().add(rd);
			}
			initViews();
			dataChanged(currentOrder);
		}
	};

	private final View.OnClickListener btnListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v == btnReturned) {
				if (data.isEmpty()) {
					ToastOrder.makeText(getApplicationContext(), "没有订单", ToastOrder.LENGTH_SHORT).show();
				}
				else {
					boolean ret = hasInput();
					if (ret) {
						ret = saveInput();
						if (!ret) {
							showReturnedDialog();
						}
					}
					else {
						ToastOrder.makeText(getApplicationContext(), "请填写到货情况", ToastOrder.LENGTH_SHORT).show();
					}
				}
			}
			else if (v == btnPreview) {
				boolean ret = hasInput();
				if (ret) {
					ret = saveInput();
					if (!ret) {
						OrderPurchasePreviewActivity.data = currentOrder;
						Intent target = new Intent(getApplicationContext(), OrderPurchasePreviewActivity.class);
						target.putExtra(Constants.ORDER_BOUNDLE_STORE_ID_KEY, storeId);
						target.putExtra(Constants.ORDER_BOUNDLE_TITLE_KEY, txtTitle.getText());
						startActivityForResult(target, 0);
					}
				}
				else {
					ToastOrder.makeText(getApplicationContext(), "请填写进货情况", ToastOrder.LENGTH_SHORT).show();
				}
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
				else {
					txtReturnedTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.order_icon_shrink, 0);
					returnedContainer.setVisibility(View.VISIBLE);
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

	private final AdapterView.OnItemSelectedListener spinnerItemListener = new AdapterView.OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			Order order = data.get(position);
			System.out.println("【Select】" + order.getOrderNo());
			if (currentOrder == order)
				return;

			initViews();
			dataChanged(order);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			System.out.println("【Select】Null");
		}
	};

	private class ReceivedListAdapter extends BaseAdapter {
		private List<OrderItem> data;

		@Override
		public int getCount() {
			return data == null ? 0 : data.size();
		}

		@Override
		public Object getItem(int position) {
			return data == null ? null : data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (data == null)
				return null;

			ReceivedItem item = (convertView == null ? new ReceivedItem(getApplicationContext()) : (ReceivedItem) convertView);
			item.update(data.get(position));
			return item;
		}
	}

	private class ReceivedItem extends LinearLayout {
		OrderItem data;

		TextView txtName;
		TextView txtNumber;
		EditText edtNumber;

		ReceivedItem(Context context) {
			this(context, null);
		}

		ReceivedItem(Context context, AttributeSet attrs) {
			super(context, attrs);
			preInitialize(context);
		}

		void preInitialize(Context context) {
			setOrientation(LinearLayout.HORIZONTAL);
			float density = context.getResources().getDisplayMetrics().density;

			int paddingHorizontal = (int) (8 * density), paddingBottom = (int) (4 * density);

			ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			setLayoutParams(params);

			LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1);
			linearParams.leftMargin = padding;
			linearParams.gravity = Gravity.CENTER_VERTICAL;
			txtName = new TextView(context);
			txtName.setTextColor(Color.BLACK);
			txtName.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize);
			addView(txtName, linearParams);

			linearParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1);
			linearParams.gravity = Gravity.CENTER_VERTICAL;
			txtNumber = new TextView(context);
			txtNumber.setTextColor(Color.BLACK);
			txtNumber.getPaint().setFakeBoldText(true);
			txtNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize);
			addView(txtNumber, linearParams);

			linearParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, context.getResources().getDimensionPixelSize(R.dimen.order_input_quantity_height), 1);
			linearParams.leftMargin = padding;
			linearParams.rightMargin = padding;
			linearParams.bottomMargin = 5;
			linearParams.gravity = Gravity.CENTER_VERTICAL;
			edtNumber = new EditText(context);
			edtNumber.setBackgroundResource(R.drawable.order_input);
			edtNumber.setPadding(paddingHorizontal, 0, paddingHorizontal, paddingBottom);
			edtNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize);
			edtNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
			edtNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
			edtNumber.setGravity(Gravity.CENTER);
			edtNumber.setHint(R.string.order_quantity);
			edtNumber.setOnKeyListener(new View.OnKeyListener() {
				
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					edtNumber.setTextColor(Color.BLACK);
					return false;
				}
			});
			addView(edtNumber, linearParams);
		}

		void update(OrderItem data) {
			this.data = data;

			txtName.setText(data.getName());
			txtNumber.setText(String.valueOf(data.getTotal() - data.getArrivalQuantity()));
			edtNumber.setText(data.getNewArrivalQuantity() > 0 ? String.valueOf(data.getNewArrivalQuantity()) : "");
		}
	}

	private class ReturnedListAdapter extends BaseAdapter {
		private List<Returned> data;

		@Override
		public int getCount() {
			return data == null ? 0 : data.size();
		}

		@Override
		public Object getItem(int position) {
			return data == null ? null : data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (data == null)
				return null;

			ReturnedItem item = (convertView == null ? new ReturnedItem(getApplicationContext()) : (ReturnedItem) convertView);
			item.update(data.get(position));
			return item;
		}
	}

	private class ReturnedItem extends LinearLayout {
		Returned data;

		TextView txtName;
		TextView txtReason;
		TextView txtNumber;
		ImageButton btnReduce;

		ReturnedItem(Context context) {
			this(context, null);
		}

		ReturnedItem(Context context, AttributeSet attrs) {
			super(context, attrs);
			preInitialize(context);
		}

		void preInitialize(Context context) {
			ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			setLayoutParams(params);
			setGravity(Gravity.CENTER_VERTICAL);

			setOrientation(LinearLayout.HORIZONTAL);

			LinearLayout.LayoutParams childrenParams = new LinearLayout.LayoutParams(columnWidth1, LayoutParams.WRAP_CONTENT);
			childrenParams.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
			childrenParams.leftMargin = padding;
			txtName = new TextView(context);
			txtName.setTextColor(Color.BLACK);
			txtName.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize);
			addView(txtName, childrenParams);

			childrenParams = new LinearLayout.LayoutParams(columnWidth2, LayoutParams.WRAP_CONTENT);
			childrenParams.gravity = Gravity.CENTER_VERTICAL;
			txtReason = new TextView(context);
			txtReason.setTextColor(Color.BLACK);
			txtReason.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize);
			txtReason.setPadding(5, 0, 5, 0);
			txtReason.setGravity(Gravity.CENTER_VERTICAL);
			addView(txtReason, childrenParams);

			childrenParams = new LinearLayout.LayoutParams(columnWidth3, LayoutParams.WRAP_CONTENT);
			childrenParams.gravity = Gravity.CENTER_VERTICAL;
			txtNumber = new TextView(context);
			txtNumber.setTextColor(Color.BLACK);
			txtNumber.getPaint().setFakeBoldText(true);
			txtNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize);
			txtNumber.setEllipsize(TextUtils.TruncateAt.END);
			txtNumber.setLines(1);
			txtNumber.setGravity(Gravity.CENTER);
			addView(txtNumber, childrenParams);

			childrenParams = new LinearLayout.LayoutParams(columnWidth4, LayoutParams.WRAP_CONTENT);
			childrenParams.gravity = Gravity.CENTER_VERTICAL;
			btnReduce = new ImageButton(context);
			btnReduce.setTag(this);
			btnReduce.setBackgroundColor(Color.TRANSPARENT);
			btnReduce.setImageResource(R.drawable.order_icon_reduce);
			btnReduce.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					ReturnedItem item = (ReturnedItem) v.getTag();
					returnedContainer.removeView(item);
					boolean remove = currentOrder.getReturneds().remove(item.data);

					System.out.println("【Remove】" + remove);
				}
			});
			addView(btnReduce, childrenParams);
		}

		void update(Returned data) {
			this.data = data;

			txtName.setText(data.getName());
			txtReason.setText(data.getReason().getCtrlCol());
			txtNumber.setText(String.valueOf(data.getReturnedQuantity()));
		}
	}

	private class SpinnerAdapterImpl extends BaseAdapter {

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView item = null;
			if (convertView == null) {
				item = new TextView(getApplicationContext());
				item.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
				item.setTextColor(Color.BLACK);
				item.setPadding(padding, padding, padding, padding);
			}
			else {
				item = (TextView) convertView;
			}
			item.setText("订单：" + data.get(position).getOrderNo());
			return item;
		}
	}
	
	private void query() {
		progressDialog.show();

		String url = UrlInfo.UrlPSSStockInfo(getApplicationContext());
		JLog.d(TAG, "Url:" + url);
		
		RequestParams params = new RequestParams();
		params.put("storeid", storeId);
		JLog.d(TAG, "Params:" + params.toString());
		
		GcgHttpClient.getInstance(this).get(url, params, httpHandler);
	}
	
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			progressDialog.dismiss();

			if (msg.arg1 != 0) {
				// 解析异常
				ToastOrder.makeText(getApplicationContext(), "获取数据失败", ToastOrder.LENGTH_SHORT).show();
				return;
			}
			
			List<Order> result = (List<Order>)msg.obj;

			int selectIndex = -1;
			if (selectOrderId != null) {
				for (int i = 0; i < result.size(); i++) {
					Order order = result.get(i);
					if (order.getOrderNo().equals(selectOrderId)) {
						currentOrder = order;
						selectIndex = i;
						break;
					}
				}
			}

			data.clear();
			data.addAll(result);
			adapter.notifyDataSetChanged();
			if (selectIndex == -1) {
				if (!data.isEmpty()) {
					spiOrder.setSelection(0);
					dataChanged(data.get(0));// 刷新界面
				}
			}
			else {
				spiOrder.setSelection(selectIndex);
				dataChanged(data.get(selectIndex));// 刷新界面
			}
		}
	};
	
	private final HttpResponseListener httpHandler = new HttpResponseListener() {


		@Override
		public void onFailure(Throwable error, String content) {
			Message msg = handler.obtainMessage();
			msg.arg1 = -4;
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
				msg.arg1 = -1;
				handler.sendMessage(msg);
				return;
			}


			List<Order> result = null;
			try {
				JSONObject jData = new JSONObject(content);
				String code = jData.getString("resultcode");
				if ("0000".equals(code)) {
					result = parser.getOrderList(content);
					msg.arg1 = 0;
					msg.obj = result;
				}
				else {
					msg.arg1 = -3;
				}
			}
			catch (JSONException e) {
				e.printStackTrace();
				msg.arg1 = -2;
				handler.sendMessage(msg);
				return;
			}
			handler.sendMessage(msg);
		}
	};
}
