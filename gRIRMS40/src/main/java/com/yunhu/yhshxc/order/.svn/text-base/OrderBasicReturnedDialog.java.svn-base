package com.yunhu.yhshxc.order;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.order.bo.OrderItem;

public abstract class OrderBasicReturnedDialog extends Dialog {
	public static final int UNSET = -1;
	
	protected TextView txtTitle;
	protected Spinner spiReason;
	protected EditText edtNumber;
	protected Button btnAdd;
	protected Button btnCancel;
	
	protected List<OrderItem> products;
	protected List<Dictionary> reasons;
	
	protected ArrayAdapter<Dictionary> reasonAdapter;
	
	protected int max = UNSET;
	
	protected int padding;

	public OrderBasicReturnedDialog(Context context) {
		super(context);
		preInitialize(context);
	}

	public OrderBasicReturnedDialog(Context context, int theme) {
		super(context, theme);
		preInitialize(context);
	}

	public OrderBasicReturnedDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		preInitialize(context);
	}

	abstract protected void preInitialize(Context context);

//	protected class ReasonAdapter extends BaseAdapter {
//
//		@Override
//		public int getCount() {
//			return reasons == null ? 0 : reasons.size();
//		}
//
//		@Override
//		public Object getItem(int position) {
//			return reasons == null ? null : reasons.get(position);
//		}
//
//		@Override
//		public long getItemId(int position) {
//			return position;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			TextView item = null;
//			if (convertView == null) {
//				item = new TextView(getContext());
//				item.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
//				item.setTextColor(Color.BLACK);
//				item.setPadding(padding, padding, (int)(40 * getContext().getResources().getDisplayMetrics().density), padding);
//				item.setLines(1);
//				item.setEllipsize(TruncateAt.END);
//			}
//			else {
//				item = (TextView)convertView;
//			}
//			item.setText(reasons.get(position).getCtrlCol());
//			return item;
//		}
//	}
	
	public void setMax(int max) {
		this.max = max;
	}
}
