package com.yunhu.yhshxc.order;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.database.PSSConfDB;
import com.yunhu.yhshxc.order.bo.OrderItem;

public class OrderSingleReturnedDialog extends OrderBasicReturnedDialog {
	private LinearLayout container;
	private Spinner spiProduct;
	
	private final List<OrderItem> products = new ArrayList<OrderItem>();
	
	private PSSConfDB pssDB;

	private ArrayAdapter<OrderItem> productAdapter;
	
	private AddReturnedCallback callback;

	public OrderSingleReturnedDialog(Context context) {
		super(context);
	}

	public OrderSingleReturnedDialog(Context context, int theme) {
		super(context, theme);
	}

	public OrderSingleReturnedDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	@Override
	protected void preInitialize(Context context) {
		pssDB = new PSSConfDB(context);
		
		float density = context.getResources().getDisplayMetrics().density;
		
	    padding = context.getResources().getDimensionPixelSize(R.dimen.order_padding);
	    
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    
		int cPadding = context.getResources().getDimensionPixelSize(R.dimen.order_padding_large);
		container = new LinearLayout(context);
		container.setBackgroundResource(R.color.home_menu_fix);
		container.setOrientation(LinearLayout.VERTICAL);
		container.setPadding(cPadding, cPadding, cPadding, cPadding);
		
		LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		txtTitle = new TextView(context);
		txtTitle.setTextColor(Color.WHITE);
		txtTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
		txtTitle.setPadding(0, cPadding, 0, cPadding);
		container.addView(txtTitle, params);

		params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		spiProduct = new Spinner(context);
		container.addView(spiProduct, params);
		
		params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.topMargin = padding;
		spiReason = new Spinner(context);
		container.addView(spiReason, params);

		int paddingHorizontal = (int)(8 * density), paddingBottom = (int)(4 * density);
		params = new LayoutParams((int)(100 * density), context.getResources().getDimensionPixelSize(R.dimen.order_input_quantity_height));
		params.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
		params.bottomMargin = padding;
		edtNumber = new EditText(context);
		edtNumber.setTextColor(Color.WHITE);
		edtNumber.setBackgroundResource(R.drawable.order_input);
		edtNumber.setPadding(paddingHorizontal, 0, paddingHorizontal, paddingBottom);
		edtNumber.setTextSize(context.getResources().getDimensionPixelSize(R.dimen.order_default_text_size));
		edtNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
		edtNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
		edtNumber.setGravity(Gravity.CENTER);
		edtNumber.setHint(R.string.order_quantity);
		edtNumber.setOnKeyListener(new View.OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				edtNumber.setTextColor(Color.WHITE);
				return false;
			}
		});
		container.addView(edtNumber, params);
		
		params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.topMargin = 20;
		LinearLayout buttonContainer = new LinearLayout(context);
		buttonContainer.setOrientation(LinearLayout.HORIZONTAL);
		container.addView(buttonContainer, params);

		params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1);
		params.rightMargin = context.getResources().getDimensionPixelSize(R.dimen.order_padding);
		btnAdd = new Button(context);
		btnAdd.setTextColor(Color.WHITE);
		btnAdd.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		btnAdd.setText("加入");
		btnAdd.setBackgroundResource(R.drawable.func_detail_submit_btn);
		buttonContainer.addView(btnAdd, params);

		params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1);
		btnCancel = new Button(context);
		btnCancel.setTextColor(Color.WHITE);
		btnCancel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		btnCancel.setText("取消");
		btnCancel.setBackgroundResource(R.drawable.func_detail_submit_btn);
		buttonContainer.addView(btnCancel, params);
	}
	
	public void initialize(int width, String title, List<OrderItem> products, AddReturnedCallback callback) {
		this.products.addAll(products);
		this.callback = callback;
		
		txtTitle.setText(title);
		spiProduct.setAdapter(productAdapter);
		spiReason.setAdapter(reasonAdapter);
		btnAdd.setOnClickListener(btnListener);
		btnCancel.setOnClickListener(btnListener);
		setContentView(container, new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT));
		
		View root = container.getRootView();
		root.setBackgroundColor(Color.TRANSPARENT);
		
		if (reasons == null)
			reasons = new ArrayList<Dictionary>();
		else
			reasons.clear();
		
		//test
//		for (int i = 0; i < 10; i++) {
//			Dictionary reason = new Dictionary();
//			reason.setDid(String.valueOf(i));
//			reason.setCtrlCol(reason.getDid());
//			reasons.add(reason);
//		}
		reasons.addAll(pssDB.findReturnedReasonList());
		
		productAdapter = new ArrayAdapter<OrderItem>(getContext(), android.R.layout.simple_spinner_item, products);
		productAdapter.setDropDownViewResource(R.layout.sprinner_check_item2);
		reasonAdapter = new ArrayAdapter<Dictionary>(getContext(), android.R.layout.simple_spinner_item, reasons);
		reasonAdapter.setDropDownViewResource(R.layout.sprinner_check_item2);
		
		spiProduct.setAdapter(productAdapter);
		productAdapter.notifyDataSetChanged();
		spiReason.setAdapter(reasonAdapter);
		reasonAdapter.notifyDataSetChanged();
	}
	
	private final View.OnClickListener btnListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			String input = edtNumber.getText().toString();
			if (v == btnAdd && callback != null && !TextUtils.isEmpty(input)) {
				long quantity = Long.parseLong(input);
				OrderItem product = products.get(spiProduct.getSelectedItemPosition());
				if (quantity > 0 && quantity <= product.getNewArrivalQuantity()) {
					callback.add(product, reasons.get(spiReason.getSelectedItemPosition()), quantity);
					dismiss();
				}
				else {
					//输入错误
					edtNumber.setTextColor(Color.RED);
					return;
				}
			}
			else if (v == btnCancel) {
				dismiss();
			}
		}
	};

	private class ProductAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return products == null ? 0 : products.size();
		}

		@Override
		public Object getItem(int position) {
			return products == null ? null : products.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView item = null;
			if (convertView == null) {
				item = new TextView(getContext());
				item.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
				item.setTextColor(Color.BLACK);
				item.setPadding(padding, padding, (int)(40 * getContext().getResources().getDisplayMetrics().density), padding);
				item.setLines(1);
				item.setEllipsize(TruncateAt.END);
			}
			else {
				item = (TextView)convertView;
			}
			item.setText(products.get(position).getName());
			return item;
		}
	}
	
	interface AddReturnedCallback {
		void add(OrderItem product, Dictionary reason, long quantity);
	}
}
