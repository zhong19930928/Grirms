package com.yunhu.yhshxc.order;

import gcg.org.debug.JLog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.database.PSSConfDB;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.order.bo.Order;
import com.yunhu.yhshxc.order.bo.OrderItem;
import com.yunhu.yhshxc.order.bo.PSSConf;
import com.yunhu.yhshxc.order.bo.Returned;
import com.yunhu.yhshxc.parser.ParsePSS;
import com.yunhu.yhshxc.print.templet.Element;
import com.yunhu.yhshxc.print.templet.Templet.DataSource;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

public class OrderQueryActivity extends Activity implements DataSource {
	private final String TAG = "OrderQueryActivity";
	
	private TextView txtTitle;
	private ExpandableListView lst;
	private Button btnUpdate, btnMore;

	private MyProgressDialog progressDialog;
	private OrderConfirmDialog confirmDialog;

	private final ArrayList<Order> data = new ArrayList<Order>();
	private final BaseAdapterImpl adapter = new BaseAdapterImpl();

	private final DecimalFormat priceFormat = new DecimalFormat("#.00");

	private int defaultTextSize;
	private int colorStateRight, colorStateWrong;
	private int padding;

	private int widthItemName, widthItemCount, widthItemState, groupItemStateWidth;

	private ParsePSS parser;

	private String storeId;
	
	private Order printData;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_query);

		parser = new ParsePSS(this);

		defaultTextSize = getResources().getDimensionPixelSize(R.dimen.order_default_text_size);
		padding = getResources().getDimensionPixelSize(R.dimen.order_padding);

		colorStateRight = getResources().getColor(R.color.order_state_right);
		colorStateWrong = getResources().getColor(R.color.order_state_wrong);

		txtTitle = (TextView) findViewById(R.id.title);
		lst = (ExpandableListView) findViewById(R.id.list);
		btnUpdate = (Button) findViewById(R.id.update);
		btnMore = (Button) findViewById(R.id.more);

		confirmDialog = new OrderConfirmDialog(this);
		confirmDialog.getMessageText().setText("您确定要取消吗？");
		confirmDialog.getOkButton().setOnClickListener(btnListener);
		confirmDialog.getCancelButton().setOnClickListener(btnListener);

		storeId = getIntent().getStringExtra(Constants.ORDER_BOUNDLE_STORE_ID_KEY);
		txtTitle.setText(getIntent().getStringExtra(Constants.ORDER_BOUNDLE_TITLE_KEY));

//		test();

		btnUpdate.setOnClickListener(btnListener);
		btnMore.setOnClickListener(btnListener);

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
				
				query("");
			}
		});
	}

	private void test() {
		txtTitle.setText("高森明晨东大桥店");

		int[] states = new int[] {
				Constants.ORDER_PRODUCT_STATE_CANCELED, Constants.ORDER_PRODUCT_STATE_COMPLETED, Constants.ORDER_PRODUCT_STATE_EXAMINE_OK, Constants.ORDER_PRODUCT_STATE_UNIMPLEMENTED
		};

		Random r = new Random(System.currentTimeMillis());

		int id = 20130702;
		for (int i = 0; i < 10; i++) {
			Order order = new Order();
			order.setItems(new ArrayList<OrderItem>());
			order.setReturneds(new ArrayList<Returned>());
			order.setOrderNo(String.valueOf(id++));
			order.setDate("2013.1.2");

			int n = r.nextInt(5) + 1;
			for (int j = 0; j < n; j++) {
				int total = r.nextInt(10000);

				OrderItem oi = new OrderItem();
				oi.setState(states[r.nextInt(states.length)]);
				oi.setTotal(total);
				oi.setArrivalQuantity(r.nextInt(total));
				oi.setName(String.valueOf(j));
				oi.setPrice(r.nextInt(200));

				order.getItems().add(oi);
			}

			System.out.println("【" + order.getOrderNo() + "】" + order.getItems().size());

			order.getItems().add(new OrderItem());

			data.add(order);
		}
	}

	public SpannableStringBuilder setTextColor(SpannableStringBuilder text, int color, int start, int end) {
		text.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		return text;
	}

	private final View.OnClickListener btnListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.update:
					query("");
					return;

				case R.id.more:
					query(data.isEmpty() ? "" : data.get(data.size() - 1).getDate());
					return;
					
				case R.id.print:
					printData = (Order)v.getTag();
					print();
					break;
			}

			if (v == confirmDialog.getOkButton()) {
				confirmDialog.dismiss();

				OrderItem oi = (OrderItem) confirmDialog.getData();
				delete(oi);
			}
			else if (v == confirmDialog.getCancelButton()) {
				confirmDialog.dismiss();
			}
		}
	};
	
	private void print() {
		PSSConfDB pssConfDB = new PSSConfDB(this);
		PSSConf pssConf = pssConfDB.findPSSConf();
		try {
			MenuOrderActivity.print(this, new JSONArray(pssConf.getOrderPrintStyle()), this);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void printLoop(List<Element> columns) {
		SharedPreferencesUtil prefs = SharedPreferencesUtil.getInstance(this);
		String[] total = computeTotal();
		
		for (OrderItem row : printData.getItems()) {
			if (isIgnoreByOrderState(row.getState()))
				continue;
			
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
							e.setContent(printData.getOrderNo());
							break;
						
						case 4://产品
							e.setContent(row.getName());
							break;
							
						case 5://数量
							e.setContent(String.valueOf(row.getTotal()));
							break;
							
						case 6://单价
							tmp = priceFormat.format(row.getPrice());
							if (row.getPrice() < 1)
								tmp = "0" + tmp;
							e.setContent(tmp);
							break;
							
						case 7://合计
							tmp = OrderCalculate.computeMultiply(String.valueOf(row.getTotal()), String.valueOf(row.getPrice()));
							if (tmp.charAt(0) == '.')
								tmp = "0" + tmp;
							e.setContent(tmp);
							break;
							
						case 8://总计
							e.setContent(total[0]);
							break;
							
						case 9://订单时间
							e.setContent(DateUtil.getDate());
							break;
							
						case 11://状态
							switch (row.getState()) {
								case Constants.ORDER_PRODUCT_STATE_UNIMPLEMENTED:
									tmp = "未生效";
									break;

								case Constants.ORDER_PRODUCT_STATE_COMPLETED:
									tmp = "已完成";
									break;

								case Constants.ORDER_PRODUCT_STATE_COMPLETING:
									tmp = "完成中";
									break;

								case Constants.ORDER_PRODUCT_STATE_EXAMINE_OK:
									tmp = "审批通过";
									break;

								case Constants.ORDER_PRODUCT_STATE_EXAMINE_REFUSE:
									tmp = "审批驳回";
									break;

								case Constants.ORDER_PRODUCT_STATE_CANCELED:
									tmp = "被取消";
									break;
									
								default:
									tmp = "";
							}
							e.setContent(tmp);
							break;
							
						case 12://配货完成
							e.setContent(total[1]);
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
			MenuOrderActivity.printNewLine();
		}
	}


	@Override
	public void printRow(List<Element> columns) {
		SharedPreferencesUtil prefs = SharedPreferencesUtil.getInstance(this);
		String[] total = computeTotal();
		
		StringBuilder status = new StringBuilder();
		for (OrderItem i : printData.getItems()) {
			switch (i.getState()) {
				case Constants.ORDER_PRODUCT_STATE_UNIMPLEMENTED:
					status.append(" 未生效");
					break;

				case Constants.ORDER_PRODUCT_STATE_COMPLETED:
					status.append(" 已完成");
					break;

				case Constants.ORDER_PRODUCT_STATE_COMPLETING:
					status.append(" 完成中");
					break;

				case Constants.ORDER_PRODUCT_STATE_EXAMINE_OK:
					status.append(" 审批通过");
					break;

				case Constants.ORDER_PRODUCT_STATE_EXAMINE_REFUSE:
					status.append(" 审批驳回");
					break;

				case Constants.ORDER_PRODUCT_STATE_CANCELED:
					status.append(" 被取消");
					break;
			}
		}
		status.trimToSize();
		
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
						e.setContent(printData.getOrderNo());
						break;
						
					case 8://总计
						e.setContent(total[0]);
						break;
						
					case 9://订单时间
						e.setContent(DateUtil.getDate());
						break;
						
					case 11://状态
						e.setContent(status.toString());
						break;
						
					case 12://配货完成
						e.setContent(total[1]);
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
	
	private String[] computeTotal() {
		long current = 0, total = 0;
		String amount = "0";

		for (OrderItem i : printData.getItems()) {
			if (isIgnoreByOrderState(i.getState()))
				continue;
			
			current += i.getArrivalQuantity();
			total += i.getTotal();
//			String n = OrderCalculate.computeMultiply(String.valueOf(i.getArrivalQuantity()), String.valueOf(i.getPrice()));
			String n = OrderCalculate.computeMultiply(String.valueOf(i.getTotal()), String.valueOf(i.getPrice()));
			amount = OrderCalculate.computeAdd(amount, n);
		}

		if (amount.charAt(0) == '.') {
			amount = "0" + amount;
		}
		
		return new String[] {amount, (current * 100 / total) + "%"};
	}
	
	public boolean isIgnoreByOrderState(int state) {
		switch (state) {
			case Constants.ORDER_PRODUCT_STATE_COMPLETED:
			case Constants.ORDER_PRODUCT_STATE_EXAMINE_REFUSE:
			case Constants.ORDER_PRODUCT_STATE_CANCELED:
			case Constants.ORDER_PRODUCT_STATE_UNIMPLEMENTED:
				return true;
		}
		return false;
	}

	// ------------------------------------------------以下更新界面-------------------------------------------------

	private final View.OnLongClickListener itemLongClickListener = new View.OnLongClickListener() {

		@Override
		public boolean onLongClick(View v) {
			SubListItem item = (SubListItem) v;
			if (item.data.getState() == Constants.ORDER_PRODUCT_STATE_UNIMPLEMENTED) {// 只有未生效的才能删除
				confirmDialog.setData(item.data);
				confirmDialog.show();
			}
			return false;
		}
	};

	private class BaseAdapterImpl extends BaseExpandableListAdapter {

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return data.get(groupPosition).getItems().get(childPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
			if (isLastChild) {
				if (convertView instanceof SubListItem) {
					SubListItemLast item = new SubListItemLast(getApplicationContext());
					item.update(data.get(groupPosition));

					convertView = item;
				}
				else {
					SubListItemLast item = (convertView == null ? new SubListItemLast(getApplicationContext()) : (SubListItemLast) convertView);
					item.update(data.get(groupPosition));

					convertView = item;
				}
			}
			else {
				if (convertView instanceof SubListItemLast) {
					SubListItem item = new SubListItem(getApplicationContext());
					item.update(data.get(groupPosition).getItems().get(childPosition));
					item.setOnLongClickListener(itemLongClickListener);

					convertView = item;
				}
				else {
					SubListItem item = (convertView == null ? new SubListItem(getApplicationContext()) : (SubListItem) convertView);
					item.update(data.get(groupPosition).getItems().get(childPosition));
					item.setOnLongClickListener(itemLongClickListener);

					convertView = item;
				}
			}

			return convertView;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return data.get(groupPosition).getItems().size() + 1;
		}

		@Override
		public Object getGroup(int groupPosition) {
			return data.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return data.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			ListItem item = (convertView == null ? new ListItem(getApplicationContext()) : (ListItem) convertView);
			item.update(data.get(groupPosition));
			return item;
		}

		/**
		 * 组和子元素是否持有稳定的ID,也就是底层数据的改变不会影响到它们。
		 * 
		 * @return 返回一个Boolean类型的值，如果为TRUE，意味着相同的ID永远引用相同的对象。
		 */
		@Override
		public boolean hasStableIds() {
			return true;
		}

		/**
		 * 是否选中指定位置上的子元素。
		 * 
		 * @param groupPosition 组位置（该组内部含有这个子元素）
		 * @param childPosition 子元素位置
		 * @return 是否选中子元素
		 */
		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return false;
		}
	}

	private class ListItem extends LinearLayout {
		private Order data;

		private TextView txtOrderNumber;
		private TextView txtDate;
		private TextView txtState;

		private ListItem(Context context) {
			this(context, null);
		}

		private ListItem(Context context, AttributeSet attrs) {
			super(context, attrs);
			preInitialize(context);
		}

		private void preInitialize(Context context) {
			setOrientation(LinearLayout.HORIZONTAL);
			setPadding(padding, padding, padding, padding);

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1);
			params.gravity = Gravity.CENTER_VERTICAL;
			LinearLayout leftContainer = new LinearLayout(context);
			leftContainer.setOrientation(LinearLayout.VERTICAL);
			addView(leftContainer, params);

			LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			params2.bottomMargin = (int)(2 * context.getResources().getDisplayMetrics().density);
			txtOrderNumber = new TextView(context);
			txtOrderNumber.setTextColor(Color.BLACK);
			txtOrderNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize);
			txtOrderNumber.getPaint().setFakeBoldText(true);
			leftContainer.addView(txtOrderNumber, params2);

			txtDate = new TextView(context);
			txtDate.setTextColor(Color.BLACK);
			txtDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
			leftContainer.addView(txtDate, new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			
			params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 3.2f);
			params.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
			txtState = new TextView(context);
			txtState.setTextColor(Color.BLACK);
			txtState.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
			txtState.setEllipsize(TruncateAt.END);
			txtState.setLines(2);
			txtState.setGravity(Gravity.CENTER_VERTICAL);
			addView(txtState, params);
		}

		private void update(Order data) {
			this.data = data;

			txtOrderNumber.setText("订单号：" + data.getOrderNo());

			txtDate.setText("订单日期：" + data.getDate());

			SpannableStringBuilder states = new SpannableStringBuilder();

			for (OrderItem i : data.getItems()) {
				String text;
				switch (i.getState()) {
					case Constants.ORDER_PRODUCT_STATE_UNIMPLEMENTED:
						text = " 未生效";
						states.append(text);
						setTextColor(states, colorStateRight, states.length() - text.length() + 1, states.length());
						break;

					case Constants.ORDER_PRODUCT_STATE_COMPLETED:
						text = " 已完成";
						states.append(text);
						setTextColor(states, colorStateRight, states.length() - text.length() + 1, states.length());
						break;

					case Constants.ORDER_PRODUCT_STATE_COMPLETING:
						text = " 完成中";
						states.append(text);
						setTextColor(states, colorStateRight, states.length() - text.length() + 1, states.length());
						break;

					case Constants.ORDER_PRODUCT_STATE_EXAMINE_OK:
						text = " 审批通过";
						states.append(text);
						setTextColor(states, colorStateRight, states.length() - text.length() + 1, states.length());
						break;

					case Constants.ORDER_PRODUCT_STATE_EXAMINE_REFUSE:
						text = " 审批驳回";
						states.append(text);
						setTextColor(states, colorStateWrong, states.length() - text.length() + 1, states.length());
						break;

					case Constants.ORDER_PRODUCT_STATE_CANCELED:
						text = " 被取消";
						states.append(text);
						setTextColor(states, colorStateWrong, states.length() - text.length() + 1, states.length());
						break;
				}
			}

			if (states.length() > 0)
				states.delete(0, 1);// 删除最开始的空格
			txtState.setText(states);
		}
	}

	private class SubListItem extends FrameLayout {
		private OrderItem data;
		private TextView txtName;
		private TextView txtCount;
		private TextView txtPrice;
		private TextView txtTotal;
		private TextView txtState;

		private SubListItem(Context context) {
			this(context, null, 0);
		}

		private SubListItem(Context context, AttributeSet attrs) {
			this(context, attrs, 0);
		}

		public SubListItem(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			preInitialize(context);
		}

		private void preInitialize(Context context) {
			int countColor = getResources().getColor(R.color.blue);
			
			setPadding(padding, padding, padding, padding);

			txtName = new TextView(context);
			txtName.setTextColor(Color.BLACK);
			txtName.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize);
			txtName.setGravity(Gravity.CENTER_VERTICAL);
			txtName.setLines(3);
			txtName.setEllipsize(TruncateAt.END);
			// txtName.setBackgroundColor(Color.RED);
			addView(txtName, new FrameLayout.LayoutParams(widthItemName, LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL | Gravity.LEFT));

			final LinearLayout amountContainer = new LinearLayout(context);
			amountContainer.setOrientation(LinearLayout.VERTICAL);
			amountContainer.setGravity(Gravity.RIGHT);
			// amountContainer.setBackgroundColor(Color.GREEN);
			addView(amountContainer, new FrameLayout.LayoutParams(widthItemCount, LayoutParams.WRAP_CONTENT, Gravity.CENTER));

			txtCount = new TextView(context);
			txtCount.setTextColor(countColor);
			txtCount.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize);
			txtCount.setGravity(Gravity.RIGHT);
			amountContainer.addView(txtCount, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

			txtPrice = new TextView(context);
			txtPrice.setTextColor(countColor);
			txtPrice.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize);
			txtPrice.setGravity(Gravity.RIGHT);
			amountContainer.addView(txtPrice, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

			txtTotal = new TextView(context);
			txtTotal.setTextColor(countColor);
			txtTotal.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize);
			txtTotal.setGravity(Gravity.RIGHT);
			amountContainer.addView(txtTotal, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

			txtState = new TextView(context);
			txtState.setTextColor(Color.BLACK);
			txtState.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize);
			txtState.setGravity(Gravity.CENTER_VERTICAL);
			txtState.setLines(3);
			txtState.setEllipsize(TruncateAt.END);
			// txtState.setBackgroundColor(Color.BLUE);
			addView(txtState, new FrameLayout.LayoutParams(widthItemState, LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL | Gravity.RIGHT));
		}

		private void update(OrderItem data) {
			this.data = data;

			txtName.setText(data.getName());

			txtCount.setText(data.getArrivalQuantity() + "/" + data.getTotal());

			double n = data.getPrice();
			String value = priceFormat.format(n);
			if (n < 1)
				value = "0" + value;
			txtPrice.setText("订货单价：" + value);

			value = OrderCalculate.computeMultiply(String.valueOf(data.getArrivalQuantity()), String.valueOf(data.getPrice()));
			if (value.charAt(0) == '.')
				value = "0" + value;
			txtTotal.setText("小计：" + value);

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

	private class SubListItemLast extends LinearLayout {
		private Order data;
		private TextView txtTotal;
		private TextView txtInfo;
		private Button btnPrint;

		private SubListItemLast(Context context) {
			this(context, null);
		}

		private SubListItemLast(Context context, AttributeSet attrs) {
			super(context, attrs);
			preInitialize(context);
		}

		private void preInitialize(Context context) {
			float density = context.getResources().getDisplayMetrics().density;
			
			setOrientation(LinearLayout.VERTICAL);
			setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

			FrameLayout rowContainer = new FrameLayout(context);
			rowContainer.setPadding(padding, padding, padding, padding);
			addView(rowContainer, LayoutParams.MATCH_PARENT, (int) (60 * density));

			txtInfo = new TextView(context);
			txtInfo.setTextColor(Color.BLACK);
			txtInfo.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize);
			txtInfo.setGravity(Gravity.RIGHT);
			rowContainer.addView(txtInfo, new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL | Gravity.LEFT));

			txtTotal = new TextView(context);
			txtTotal.setTextColor(Color.BLACK);
			txtTotal.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize);
			txtTotal.setGravity(Gravity.RIGHT);
			rowContainer.addView(txtTotal, new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL | Gravity.RIGHT));
			

			PSSConfDB pssConfDB = new PSSConfDB(context);
			PSSConf pssConf = pssConfDB.findPSSConf();
			
			if (!TextUtils.isEmpty(pssConf.getOrderPrintStyle())) {
				View divider = new View(context);
				divider.setBackgroundColor(Color.BLACK);
				addView(divider, LayoutParams.MATCH_PARENT, 1);
				
				rowContainer = new FrameLayout(context);
				rowContainer.setPadding(padding, padding, padding, padding);
				addView(rowContainer, LayoutParams.MATCH_PARENT, (int) (60 * density));
				
				btnPrint = new Button(context);
				btnPrint.setId(R.id.print);
				btnPrint.setText(R.string.order_print);
				btnPrint.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimensionPixelSize(R.dimen.order_default_text_size));
				btnPrint.setTextColor(Color.WHITE);
				btnPrint.setBackgroundResource(R.drawable.order_menu_click);
				btnPrint.setOnClickListener(btnListener);
				btnPrint.setGravity(Gravity.RIGHT);
				FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				frameParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
				rowContainer.addView(btnPrint, frameParams);
			}
		}

		private void update(Order data) {
			this.data = data;

			long current = 0, total = 0;
			String amount = "0";

			for (OrderItem i : data.getItems()) {
				current += i.getArrivalQuantity();
				total += i.getTotal();
				String n = OrderCalculate.computeMultiply(String.valueOf(i.getArrivalQuantity()), String.valueOf(i.getPrice()));
				amount = OrderCalculate.computeAdd(amount, n);
			}

			if (amount.charAt(0) == '.') {
				amount = "0" + amount;
			}
			SpannableStringBuilder text = new SpannableStringBuilder("合计:" + amount);
			setTextColor(text, colorStateRight, text.length() - amount.length(), text.length());
			txtTotal.setText(text);
			txtInfo.setText("配货完成：" + (current * 100 / total) + "%");
			
			if (btnPrint != null) {
				btnPrint.setTag(data);
			}
		}
	}

	// ------------------------------------------------以下HTTP操作-------------------------------------------------
	
	private void query(String time) {
		progressDialog = new MyProgressDialog(OrderQueryActivity.this, R.style.CustomProgressDialog, getResources().getString(R.string.initTable));
		progressDialog.show();
		
		RequestParams params = new RequestParams();
		params.put("storeid", storeId);
		params.put("createtime", time);
		JLog.d(TAG, "Params:" + params.toString());
		
		String url = UrlInfo.UrlPSSSearchOrder(getApplicationContext());
		JLog.d(TAG, "Url:" + url);
		
		QueryHttpResponseHandler handler = new QueryHttpResponseHandler(time);
		GcgHttpClient.getInstance(this).post(url, params, handler);
	}
	
	private class QueryHttpResponseHandler extends TextHttpResponseHandler{
		String time;//查询时用的时间，不能为null，如果没有则设置为空字符串
		
		QueryHttpResponseHandler(String time) {
			this.time = time;
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
					result = parser.getOrderList(content);
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

		@Override
		public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
			Message msg = handler.obtainMessage();
			msg.arg1 = -5;
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
				adapter.notifyDataSetChanged();
			}
			else {
				if (msg.arg1 != 0) {
					// 解析异常
					ToastOrder.makeText(getApplicationContext(), "取消失败", ToastOrder.LENGTH_SHORT).show();
					return;
				}

				// 提交成功
				OrderItem item = (OrderItem)msg.obj;
				ToastOrder.makeText(getApplicationContext(), "取消成功", ToastOrder.LENGTH_SHORT).show();
				item.setState(Constants.ORDER_PRODUCT_STATE_CANCELED);
				adapter.notifyDataSetChanged();
			}
		}
	};
	
	private void delete(OrderItem item) {
		progressDialog = new MyProgressDialog(OrderQueryActivity.this, R.style.CustomProgressDialog, "正在提交...");
		progressDialog.show();
		
		Order order = null;
		for (Order i : data) {
			if (i.getItems() != null && i.getItems().contains(item)) {// 如果确实删除了数据，才有必要刷新列表
				order = i;
				break;
			}
		}

		if (order == null) {
			ToastOrder.makeText(getApplicationContext(), "取消失败", ToastOrder.LENGTH_SHORT).show();
			return;
		}

		RequestParams params = new RequestParams();
		params.put("orderid", order.getOrderNo());// 订单ID
		params.put("storeid", String.valueOf(storeId));// 店面ID
		params.put("proid", String.valueOf(item.getProductId()));// 产品ID
		params.put("id", String.valueOf(item.getId()));
		
		JLog.d(TAG, "Params:" + params.toString());

		DeleteHttpResponseHandler handler = new DeleteHttpResponseHandler(order, item);
		GcgHttpClient.getInstance(this).post(UrlInfo.UrlPSSOrderCancel(getApplicationContext()), params, handler);
	}
	
	private class DeleteHttpResponseHandler extends TextHttpResponseHandler {
		Order order;
		OrderItem item;
		
		DeleteHttpResponseHandler(Order order, OrderItem item) {
			this.order = order;
			this.item = item;
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
				msg.obj = item;
			}
			catch (JSONException e) {
				e.printStackTrace();
				msg.arg1 = -2;
			}
			handler.sendMessage(msg);
		}
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
