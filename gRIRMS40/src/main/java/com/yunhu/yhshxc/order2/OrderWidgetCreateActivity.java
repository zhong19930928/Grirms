package com.yunhu.yhshxc.order2;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.SubmitItem;
import com.yunhu.yhshxc.database.SubmitItemDB;
import com.yunhu.yhshxc.widget.ToastOrder;

public class OrderWidgetCreateActivity extends OrderCreateActivity {
	
	protected Bundle newOrderBundle;
	@Override
	protected void initStoreData(String fuzzy) {
		newOrderBundle = getIntent().getBundleExtra("newOrderBundle");
		storeId = newOrderBundle.getString("storeId");
		String storeName = newOrderBundle.getString("storeName");
		if (!TextUtils.isEmpty(storeId)) {
			tv_order_client.setText("客户:");
			tv_order_store.setVisibility(View.VISIBLE);
			order2_spinner_store.setVisibility(View.GONE);
			tv_order_store.setText(storeName);
			Dictionary dic = new Dictionary();
			dic.setDid(storeId);
			dic.setCtrlCol(storeName);
			initOrder(dic);
			initFragmen();
		}else{
			ToastOrder.makeText(this, "店面数据异常", ToastOrder.LENGTH_SHORT).show();
		}
	}
	
	@Override
	protected void submitOrderSuccessFinish() {
		int funcId = newOrderBundle.getInt("funcId");
		int submitId = newOrderBundle.getInt("submitId");
		int orderid = newOrderBundle.getInt("orderId");
		SubmitItem item = new SubmitItem();
		item.setSubmitId(submitId);
		item.setOrderId(orderid);
		item.setParamName(String.valueOf(funcId));
		item.setParamValue(orderNumber);
		item.setType(Func.TYPE_ORDER2);
		SubmitItemDB submitItemDB = new SubmitItemDB(this);
		boolean isHas = submitItemDB.findIsHaveParamName(item.getParamName(), submitId);
		if (isHas) {
			submitItemDB.updateSubmitItem(item,false);
		}else{
			submitItemDB.insertSubmitItem(item,false);
		}
	}
}
