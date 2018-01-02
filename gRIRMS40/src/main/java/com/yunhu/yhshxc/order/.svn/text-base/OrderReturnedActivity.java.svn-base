package com.yunhu.yhshxc.order;

import gcg.org.debug.JLog;

import java.util.ArrayList;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.order.bo.Returned;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.loopj.android.http.RequestParams;

public class OrderReturnedActivity extends Activity {
	private final String TAG = "OrderReturnedActivity";
	
	private final ArrayList<Returned> returneds = new ArrayList<Returned>();

	private LinearLayout container;
	private TextView txtTitle;
	private TextView txtOrder;
	private Button btnSubmit, btnReturned;

	private OrderMultiProductReturnedDialog returnedDialog;
	private MyProgressDialog progressDialog;
	private OrderConfirmDialog confirmDialog;
	private OrderConfirmDialog exitDialog;

	private int defaultTextSize;
	private int padding;

	private String orderId;
	private String storeId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_returned);

		orderId = OrderCalculate.getOrderNumber();

		txtTitle = (TextView) findViewById(R.id.title);
		container = (LinearLayout) findViewById(R.id.container);

		txtOrder = (TextView) findViewById(R.id.order);
		txtOrder.setText("退货单号 " + orderId);

		btnReturned = (Button) findViewById(R.id.returned);
		btnReturned.setOnClickListener(btnListener);

		btnSubmit = (Button) findViewById(R.id.submit);
		btnSubmit.setOnClickListener(btnListener);

		progressDialog = new MyProgressDialog(this, R.style.CustomProgressDialog, "正在提交...");

		confirmDialog = new OrderConfirmDialog(this);
		confirmDialog.getMessageText().setText("您确定要提交吗？");
		confirmDialog.getOkButton().setOnClickListener(btnListener);
		confirmDialog.getCancelButton().setOnClickListener(btnListener);
		
		exitDialog = new OrderConfirmDialog(this);
		exitDialog.getMessageText().setText(R.string.BACKDIALOG);
		exitDialog.getOkButton().setOnClickListener(btnListener);
		exitDialog.getCancelButton().setOnClickListener(btnListener);

		defaultTextSize = getResources().getDimensionPixelSize(R.dimen.order_default_text_size);
		padding = getResources().getDimensionPixelSize(R.dimen.order_padding);

		storeId = getIntent().getStringExtra(Constants.ORDER_BOUNDLE_STORE_ID_KEY);
		txtTitle.setText(getIntent().getStringExtra(Constants.ORDER_BOUNDLE_TITLE_KEY));

//		test();

		dataChanged();
	}

	private void test() {
		txtTitle.setText("高森明晨东大桥店");

		Random r = new Random(System.currentTimeMillis());

		int n = r.nextInt(5) + 1;
		for (int j = 0; j < n; j++) {
			Returned rd = new Returned();
			rd.setName(String.valueOf(j + 1));
			Dictionary reason = new Dictionary();
			reason.setDid(String.valueOf(1));
			reason.setCtrlCol("AAA");
			rd.setReason(reason);
			returneds.add(rd);
		}
	}

	private void dataChanged() {
		if (!returneds.isEmpty()) {
			container.removeViews(1, container.getChildCount() - 1);
		}

		for (int i = 0; i < returneds.size(); i++) {
			ReturnedItem item = new ReturnedItem(getApplicationContext());
			item.update(returneds.get(i));
			container.addView(item);
		}
	}

	private void showReturnedDialog() {
		int width = getWindow().getDecorView().getWidth() - 20;
		returnedDialog = new OrderMultiProductReturnedDialog(this);
		returnedDialog.initialize(width, "选择产品和退货原因", addReturnedCallback);
		returnedDialog.show();
	}

	@Override
	public void onBackPressed() {
		if (returneds.isEmpty()) {
			setResult(RESULT_CANCELED);
			finish();
		}
		else {
			exitDialog.show();
		}
	}

	private final View.OnClickListener btnListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v == btnReturned) {
				showReturnedDialog();
			}
			else if (v == btnSubmit) {
				if (returneds.isEmpty()) {
					ToastOrder.makeText(getApplicationContext(), "请添加退货商品", ToastOrder.LENGTH_SHORT).show();
				}
				else {
					confirmDialog.show();
				}
			}
			else if (v == confirmDialog.getOkButton()) {
				confirmDialog.dismiss();

				submit();
			}
			else if (v == confirmDialog.getCancelButton()) {
				confirmDialog.dismiss();
			}
			else if (v == exitDialog.getOkButton()) {
				exitDialog.dismiss();
				setResult(RESULT_CANCELED);
				finish();
			}
			else if (v == exitDialog.getCancelButton()) {
				exitDialog.dismiss();
			}
		}
	};

	private OrderMultiProductReturnedDialog.AddReturnedCallback addReturnedCallback = new OrderMultiProductReturnedDialog.AddReturnedCallback() {

		@Override
		public void add(Returned returned) {
			returneds.add(returned);
			dataChanged();
		}
	};
	
	private void submit() {
		progressDialog.show();
		
		RequestParams params = new RequestParams();
		params.put("orderid", orderId);
		params.put("storeid", String.valueOf(storeId));
		JSONArray json = new JSONArray();
		try {
			for (Returned i : returneds) {
				JSONObject item = new JSONObject();
				item.put("proid", String.valueOf(i.getProductId()));
				item.put("productname", i.getName());
				item.put("returnedcount", String.valueOf(i.getReturnedQuantity()));
				item.put("reasonid", i.getReason().getDid());
				json.put(item);
			}
			params.put("proinfo", json.toString());
		}
		catch (Exception e) {
			e.printStackTrace();
			Message msg = handler.obtainMessage();
			msg.arg1 = -1;
			handler.sendMessage(msg);
			return;
		}
		
		JLog.d(TAG, "Params:" + params.toString());
		
		GcgHttpClient.getInstance(this).post(UrlInfo.UrlPSSOrderReturnedSave(getApplicationContext()), params, httpHandler);
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
			setResult(RESULT_OK);
			finish();
		}
	};

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

			post(new Runnable() {

				@Override
				public void run() {
					int width = getWidth() - padding * 2;
					float unit = width / 11;

					ViewGroup.LayoutParams childrenParams = txtName.getLayoutParams();
					childrenParams.width = (int) (4 * unit);
					txtName.setLayoutParams(childrenParams);

					childrenParams = txtReason.getLayoutParams();
					childrenParams.width = (int) (4 * unit);
					txtReason.setLayoutParams(childrenParams);

					childrenParams = txtNumber.getLayoutParams();
					childrenParams.width = (int) (2 * unit);
					txtNumber.setLayoutParams(childrenParams);

					childrenParams = btnReduce.getLayoutParams();
					childrenParams.width = (int) (1 * unit);
					btnReduce.setLayoutParams(childrenParams);
				}
			});

			LinearLayout.LayoutParams childrenParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			childrenParams.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
			childrenParams.leftMargin = padding;
			txtName = new TextView(context);
			txtName.setTextColor(Color.BLACK);
			txtName.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize);
			addView(txtName, childrenParams);

			childrenParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			childrenParams.gravity = Gravity.CENTER_VERTICAL;
			txtReason = new TextView(context);
			txtReason.setTextColor(Color.BLACK);
			txtReason.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize);
			txtReason.setPadding(5, 0, 5, 0);
			addView(txtReason, childrenParams);

			childrenParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			childrenParams.gravity = Gravity.CENTER;
			txtNumber = new TextView(context);
			txtNumber.setTextColor(Color.BLACK);
			txtNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize);
			txtNumber.getPaint().setFakeBoldText(true);
			txtNumber.setEllipsize(TextUtils.TruncateAt.END);
			txtNumber.setLines(1);
			txtNumber.setGravity(Gravity.CENTER);
			addView(txtNumber, childrenParams);

			childrenParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			childrenParams.gravity = Gravity.CENTER_VERTICAL;
			btnReduce = new ImageButton(context);
			btnReduce.setTag(this);
			btnReduce.setBackgroundColor(Color.TRANSPARENT);
			btnReduce.setImageResource(R.drawable.order_icon_reduce);
			btnReduce.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					ReturnedItem item = (ReturnedItem) v.getTag();
					container.removeView(item);
					boolean remove = returneds.remove(item.data);

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
}
