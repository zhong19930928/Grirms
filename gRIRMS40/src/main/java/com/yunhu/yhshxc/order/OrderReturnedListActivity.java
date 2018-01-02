package com.yunhu.yhshxc.order;

import gcg.org.debug.JLog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.apache.http.Header;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.order.bo.Order;
import com.yunhu.yhshxc.order.bo.OrderItem;
import com.yunhu.yhshxc.order.bo.Returned;
import com.yunhu.yhshxc.parser.ParsePSS;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

public class OrderReturnedListActivity extends Activity {
	private final String TAG = "OrderReturnedListActivity";
	
	private final List<Order> data = new ArrayList<Order>();

	private LinearLayout container;
	private TextView txtTitle;
	private Button btnMore, btnUpdate, btnCreate;

	private MyProgressDialog progressDialog;
	private OrderConfirmDialog confirmDialog;

	private int defaultTextSize;
	private int padding;
	private int colorStateRight, colorStateWrong;

	private ParsePSS parser;

	private String storeId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_returned_list);

		parser = new ParsePSS(this);

		txtTitle = (TextView) findViewById(R.id.title);
		container = (LinearLayout) findViewById(R.id.container);
		btnMore = (Button) findViewById(R.id.more);
		btnMore.setOnClickListener(btnListener);
		btnUpdate = (Button) findViewById(R.id.update);
		btnUpdate.setOnClickListener(btnListener);
		btnCreate = (Button) findViewById(R.id.create);
		btnCreate.setOnClickListener(btnListener);

		defaultTextSize = getResources().getDimensionPixelSize(R.dimen.order_default_text_size);
		padding = getResources().getDimensionPixelSize(R.dimen.order_padding_large);

		colorStateRight = getResources().getColor(R.color.order_state_right);
		colorStateWrong = getResources().getColor(R.color.order_state_wrong);

		confirmDialog = new OrderConfirmDialog(this);
		confirmDialog.getMessageText().setText("您确定要取消吗？");
		confirmDialog.getOkButton().setOnClickListener(btnListener);
		confirmDialog.getCancelButton().setOnClickListener(btnListener);

		storeId = getIntent().getStringExtra(Constants.ORDER_BOUNDLE_STORE_ID_KEY);
		txtTitle.setText(getIntent().getStringExtra(Constants.ORDER_BOUNDLE_TITLE_KEY));

//		test();
		query("");

		dataChanged();
	}

	private void dataChanged() {
		container.removeAllViews();

		for (Order i : data) {
			Group group = new Group(this);
			group.update(i);
			container.addView(group);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {//只有OrderReturnedActivity提交数据后关闭才会到这里
			query("");
		}
	}

	private void test() {
		txtTitle.setText("高森明晨东大桥店");

		Random r = new Random(System.currentTimeMillis());

		int id = 20130702;
		for (int i = 0; i < 10; i++) {
			Order order = new Order();
			order.setOrderNo(String.valueOf(id++));
			order.setDate(String.valueOf(id));
			order.setReturneds(new ArrayList<Returned>());
			order.setItems(new ArrayList<OrderItem>());

			int n = r.nextInt(5) + 1;
			for (int j = 0; j < n; j++) {
				Returned rd = new Returned();
				rd.setName(String.valueOf(j + 1));
				Dictionary reason = new Dictionary();
				reason.setDid(String.valueOf(1));
				reason.setCtrlCol("AAA");
				rd.setReason(reason);
				order.getReturneds().add(rd);
			}

			data.add(order);
		}
	}

	private final View.OnClickListener btnListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v == btnMore) {
				query(data.isEmpty() ? "" : data.get(data.size() - 1).getDate());
			}
			else if (v == btnUpdate) {
				query("");
			}
			else if (v == btnCreate) {
				Intent target = new Intent(getApplicationContext(), OrderReturnedActivity.class);
				target.putExtra(Constants.ORDER_BOUNDLE_STORE_ID_KEY, storeId);
				target.putExtra(Constants.ORDER_BOUNDLE_TITLE_KEY, txtTitle.getText());
				startActivityForResult(target, 0);
			}
			else if (v == confirmDialog.getOkButton()) {
				confirmDialog.dismiss();

				Returned returned = (Returned) confirmDialog.getData();
				delete(returned);
			}
			else if (v == confirmDialog.getCancelButton()) {
				confirmDialog.dismiss();
			}
			else {
				if (v instanceof Group) {
					Group group = (Group)v;
					if (group.container.getVisibility() == View.VISIBLE) {
						group.imgIcon.setImageResource(R.drawable.order_icon_expand);
						group.container.setVisibility(View.GONE);
					}
					else {
						group.imgIcon.setImageResource(R.drawable.order_icon_shrink);
						group.container.setVisibility(View.VISIBLE);
					}
				}
			}
		}
	};

	private final View.OnLongClickListener itemLongClickListener = new View.OnLongClickListener() {

		@Override
		public boolean onLongClick(View v) {

			GroupItem item = (GroupItem) v;
			if (item.data.getState() == Constants.ORDER_PRODUCT_STATE_UNIMPLEMENTED) {
				confirmDialog.setData(item.data);
				confirmDialog.show();
			}
			return false;
		}
	};
	
	private void query(String time) {
		progressDialog = new MyProgressDialog(OrderReturnedListActivity.this, R.style.CustomProgressDialog, getResources().getString(R.string.initTable));
		progressDialog.show();
		
		RequestParams params = new RequestParams();
		params.put("storeid", storeId);
		params.put("createtime", time);
		JLog.d(TAG, "Params:" + params.toString());
		
		String url = UrlInfo.UrlPSSReturnSearch(getApplicationContext());
		JLog.d(TAG, "Url:" + url);
		
		QueryHttpResponseHandler handler = new QueryHttpResponseHandler(time);
		GcgHttpClient.getInstance(this).post(url, params, handler);
	}
	
	private class QueryHttpResponseHandler extends TextHttpResponseHandler {
		String time;//查询时用的时间，不能为null，如果没有则设置为空字符串
		
		QueryHttpResponseHandler(String time) {
			this.time = time;
		}


		@Override
		public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) {
			Message msg = handler.obtainMessage();
			msg.arg1 = -5;
			handler.sendMessage(msg);
			
		}

		@Override
		public void onSuccess(int statusCode, Header[] headers, String content) {
			JLog.d(TAG, "Result:" + content);
			
			Message msg = handler.obtainMessage(HANDLER_TYPE_QUERY);
			
			if (TextUtils.isEmpty(content)) {
				msg.arg1 = -3;
				handler.sendMessage(msg);
				return;
			}

			Collection<Order> result = null;
			try {
				JSONObject jData = new JSONObject(content);
				String code = jData.getString("resultcode");
				if ("0000".equals(code)) {
					result = parser.getReturnedList(content);
					msg.obj = result;
					msg.arg1 = 0;
					msg.arg2 = TextUtils.isEmpty(time) ? 0 : 1;
				}
				else {
					msg.arg1 = -1;
				}
			}
			catch (JSONException e) {
				e.printStackTrace();
				msg.arg1 = -2;
			}
			handler.sendMessage(msg);
			
		}
	}
	
	private final int HANDLER_TYPE_QUERY = 0;
	private final int HANDLER_TYPE_DELETE = 1;
	
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			progressDialog.dismiss();

			if (msg.what == HANDLER_TYPE_QUERY) {
				if (msg.arg1 != 0) {
					// 解析异常
					ToastOrder.makeText(getApplicationContext(), "获取数据失败", ToastOrder.LENGTH_SHORT).show();
					return;
				}

				// 如果没有时间值，说明是刷新操作，而刷新操作需要清空之前的数据
				if (msg.arg2 == 0) {
					data.clear();
				}
				List<Order> result = (List<Order>)msg.obj;
				data.addAll(result);
				dataChanged();
			}
			else {
				if (msg.arg1 != 0) {
					// 解析异常
					ToastOrder.makeText(getApplicationContext(), "取消失败", ToastOrder.LENGTH_SHORT).show();
					return;
				}

				//  提交成功
				Returned returned = (Returned)msg.obj;
				returned.setState(Constants.ORDER_PRODUCT_STATE_CANCELED);
				dataChanged();
				ToastOrder.makeText(getApplicationContext(), "取消成功", ToastOrder.LENGTH_SHORT).show();
			}
		}
	};
	
	private void delete(Returned returned) {
		progressDialog = new MyProgressDialog(OrderReturnedListActivity.this, R.style.CustomProgressDialog, "正在提交...");
		progressDialog.show();
		
		String url = UrlInfo.UrlPSSOrderReturnedCancel(getApplicationContext());
		JLog.d(TAG, "Url:" + url);

		RequestParams params = new RequestParams("id", returned.getId());
		JLog.d(TAG, "Params:" + params.toString());

		DeleteHttpResponseHandler handler = new DeleteHttpResponseHandler(returned);
		GcgHttpClient.getInstance(this).post(url, params, handler);
	}
	
	private class DeleteHttpResponseHandler extends TextHttpResponseHandler {
		Returned returned;
		
		DeleteHttpResponseHandler(Returned returned) {
			this.returned = returned;
		}


		@Override
		public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
			Message msg = handler.obtainMessage();
			msg.arg1 = -5;
			handler.sendMessage(msg);
		}

		@Override
		public void onSuccess(int statusCode, Header[] headers, String content) {
			JLog.d(TAG, "Result:" + content);
			
			Message msg = handler.obtainMessage(HANDLER_TYPE_DELETE);
			
			if (TextUtils.isEmpty(content)) {
				msg.arg1 = -3;
				handler.sendMessage(msg);
				return;
			}

			try {
				JSONObject jData = new JSONObject(content);
				String code = jData.getString("resultcode");
				msg.arg1 = "0000".equals(code) ? 0 : -1;
				msg.obj = returned;
			}
			catch (JSONException e) {
				e.printStackTrace();
				msg.arg1 = -2;
			}
			handler.sendMessage(msg);
			
		}
	}

	private class Group extends LinearLayout {
		static final int ID_NUMBER = 10001;

		Order data;

		TextView txtNumber;
		TextView txtDate;
		ImageView imgIcon;
		
		LinearLayout container;

		Group(Context context) {
			this(context, null);
		}

		Group(Context context, AttributeSet attrs) {
			super(context, attrs);
			preInitialize(context);
		}

		void preInitialize(Context context) {
			setOrientation(LinearLayout.VERTICAL);
			setOnClickListener(btnListener);

			RelativeLayout titlebar = new RelativeLayout(context);
			titlebar.setBackgroundResource(R.color.order_list_title_bg);
			titlebar.setPadding(padding, padding, padding, padding);
			addView(titlebar, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

			txtNumber = new TextView(context);
			txtNumber.setId(ID_NUMBER);
			txtNumber.setTextColor(Color.BLACK);
			txtNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize);
			titlebar.addView(txtNumber);

			RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			relativeParams.addRule(RelativeLayout.BELOW, ID_NUMBER);
			relativeParams.topMargin = context.getResources().getDimensionPixelSize(R.dimen.order_padding);
			txtDate = new TextView(context);
			txtDate.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize);
			txtDate.setTextColor(Color.BLACK);
			titlebar.addView(txtDate, relativeParams);

			relativeParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			relativeParams.addRule(RelativeLayout.CENTER_VERTICAL);
			relativeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			imgIcon = new ImageView(context);
			imgIcon.setImageResource(R.drawable.order_icon_shrink);
			titlebar.addView(imgIcon, relativeParams);
			
			container = new LinearLayout(getApplicationContext());
			container.setOrientation(LinearLayout.VERTICAL);
			addView(container);
		}

		void update(Order data) {
			txtNumber.setText("退货单号 " + data.getOrderNo());
			txtDate.setText(data.getDate());
			for (Returned i : data.getReturneds()) {
				GroupItem gi = new GroupItem(getContext());
				gi.update(i);
				container.addView(gi);
			}
		}
	}

	private class GroupItem extends LinearLayout {
		Returned data;

		TextView txtName;
		TextView txtQuantity;
		TextView txtState;

		GroupItem(Context context) {
			this(context, null);
		}

		GroupItem(Context context, AttributeSet attrs) {
			super(context, attrs);
			preInitialize(context);
		}

		void preInitialize(Context context) {
			setOrientation(LinearLayout.HORIZONTAL);
			setPadding(padding, padding, padding, padding);
			setOnLongClickListener(itemLongClickListener);

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1);
			params.gravity = Gravity.CENTER_VERTICAL;
			txtName = new TextView(context);
			txtName.setTextColor(Color.BLACK);
			txtName.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize);
			txtName.setEllipsize(TruncateAt.END);
			addView(txtName, params);

			params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1);
			params.gravity = Gravity.CENTER_VERTICAL;
			txtQuantity = new TextView(context);
			txtQuantity.setTextColor(Color.BLACK);
			txtQuantity.getPaint().setFakeBoldText(true);
			txtQuantity.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize);
			txtQuantity.setEllipsize(TruncateAt.END);
			txtQuantity.setGravity(Gravity.CENTER_HORIZONTAL);
			addView(txtQuantity, params);

			params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1);
			params.gravity = Gravity.CENTER_VERTICAL;
			txtState = new TextView(context);
			txtState.setTextColor(Color.BLACK);
			txtState.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize);
			txtState.setEllipsize(TruncateAt.END);
			txtState.setGravity(Gravity.RIGHT);
			addView(txtState, params);

//			post(initView);
		}

		void update(Returned data) {
			this.data = data;

			txtName.setText(data.getName());
			txtQuantity.setText(String.valueOf(data.getReturnedQuantity()));
			switch (data.getState()) {
				case Constants.ORDER_PRODUCT_STATE_UNIMPLEMENTED:
					txtState.setText("未生效");
					txtState.setTextColor(colorStateRight);
					break;

				case Constants.ORDER_PRODUCT_STATE_COMPLETED:
					txtState.setText("已完成");
					txtState.setTextColor(colorStateRight);
					break;

				case Constants.ORDER_PRODUCT_STATE_COMPLETING:
					txtState.setText("完成中");
					txtState.setTextColor(colorStateRight);
					break;

				case Constants.ORDER_PRODUCT_STATE_EXAMINE_OK:
					txtState.setText("审批通过");
					txtState.setTextColor(colorStateRight);
					break;

				case Constants.ORDER_PRODUCT_STATE_EXAMINE_REFUSE:
					txtState.setText("审批驳回");
					txtState.setTextColor(colorStateWrong);
					break;

				case Constants.ORDER_PRODUCT_STATE_CANCELED:
					txtState.setText("被取消");
					txtState.setTextColor(colorStateWrong);
					break;
			}
		}
	}
}
