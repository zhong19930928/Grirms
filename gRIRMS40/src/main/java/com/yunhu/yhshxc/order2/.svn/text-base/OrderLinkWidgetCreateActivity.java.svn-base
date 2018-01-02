package com.yunhu.yhshxc.order2;

import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.SubmitItem;
import com.yunhu.yhshxc.database.SubmitItemTempDB;

public class OrderLinkWidgetCreateActivity extends OrderWidgetCreateActivity {
	
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
		SubmitItemTempDB submitItemTempDB = new SubmitItemTempDB(this);
		boolean isHas = submitItemTempDB.findIsHaveParamName(item.getParamName(), submitId);
		if (isHas) {
			submitItemTempDB.updateSubmitItem(item);
		}else{
			submitItemTempDB.insertSubmitItem(item);
		}
	}
}
