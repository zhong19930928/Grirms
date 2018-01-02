package com.yunhu.yhshxc.activity.carSales.dialog;

import gcg.org.debug.JLog;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.carSales.CarSalesMainActivity;
import com.yunhu.yhshxc.activity.carSales.util.SharedPreferencesForCarSalesUtil;
import com.yunhu.yhshxc.widget.DropDown;

public class DiaLogAddStore extends Dialog {

	/**
	 * 新增店铺
	 * 
	 * @author abby_zhao
	 */

	public Context context;// 上下文
	private CarSalesMainActivity activity;
//	public DiaLogAddStore(Context context) {
//		super(context);
//		this.context = context;
//		activity = (CarSalesMainActivity) context;
//	}
//
//	public DiaLogAddStore(Context context, boolean cancelable,
//			OnCancelListener cancelListener) {
//		super(context, cancelable, cancelListener);
//		this.context = context;
//		activity = (CarSalesMainActivity) context;
//	}

	public DiaLogAddStore(Context context, int theme, String content,
			final DropDown drop) {
		super(context, theme);
		this.context = context;
		activity = (CarSalesMainActivity) context;
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_add_store, null); // 加载自己定义的布局

		setContentView(view);// 为Dialoge设置自己定义的布局

		LinearLayout btn_add_store_confirm = (LinearLayout) view
				.findViewById(R.id.btn_add_store_confirm);

		final EditText ed_store_name = (EditText) view
				.findViewById(R.id.ed_store_name);
		if ("-1".equals(SharedPreferencesForCarSalesUtil.getInstance(
				getContext()).getStoreId())) {
			ed_store_name.setText(SharedPreferencesForCarSalesUtil.getInstance(
					getContext()).getCarSalesStoreName());
		}

		btn_add_store_confirm.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (ed_store_name.getText().toString().equals("")) {
					JLog.d("abby", "name" + ed_store_name.getText().toString());

					Toast.makeText(getContext(), "请输入店面信息！", Toast.LENGTH_SHORT)
							.show();

				} else {

					SharedPreferencesForCarSalesUtil.getInstance(getContext())
							.getCarSalesStoreName();

					SharedPreferencesForCarSalesUtil.getInstance(getContext())
							.setStoreId("-1");
					SharedPreferencesForCarSalesUtil.getInstance(getContext())
							.setCarSalesStoreName(
									ed_store_name.getText().toString());
					SharedPreferencesForCarSalesUtil.getInstance(getContext())
							.setCarSalesStoreLevel(-1);
					SharedPreferencesForCarSalesUtil.getInstance(getContext())
							.setCarSalesOrgId("-1");

					if (drop != null) {
						drop.refreshComp(ed_store_name.getText().toString());
					}
					dismiss();
				}

			}
		});

		LinearLayout btn_add_store_cancel = (LinearLayout) view
				.findViewById(R.id.btn_add_store_cancel);

		btn_add_store_cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (TextUtils.isEmpty(SharedPreferencesForCarSalesUtil.getInstance(
						getContext()).getCarSalesStoreName())) {

					if (drop != null) {
						drop.refreshComp(ed_store_name.getText().toString());
					}
					dismiss();

				} else {
					if (drop != null) {
						drop.refreshComp(SharedPreferencesForCarSalesUtil
								.getInstance(getContext())
								.getCarSalesStoreName());
					}
					if (activity!=null) {
						activity.initStoreSelect(activity.srcStoreList);
					}
					dismiss();
				}

			}
		});
		this.setCancelable(false);

	}

}
