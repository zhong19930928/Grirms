package com.yunhu.yhshxc.order2.view;

import java.util.List;

import android.content.Context;

import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.order.bo.ProductConf;
import com.yunhu.yhshxc.order.view.ProItemLayout;
import com.yunhu.yhshxc.order.view.ProductLayout;
import com.yunhu.yhshxc.order2.SharedPreferencesForOrder2Util;
import com.yunhu.yhshxc.order2.bo.OrderDetail;

/**
 *订单产品布局，插入此布局，所有级联便会显示。
 *通过监听返回它最后一级列表的值 
 * @author houyu 
 *
 */
public class ProductLayoutForOrder2 extends ProductLayout{
	
	public ProductLayoutForOrder2(Context context, int lableColor) {
		super(context, lableColor);
	}

	public ProductLayoutForOrder2(Context context) {
		super(context);
	}

	@Override
	public List<ProductConf> getAllSpinnerDataConfInfo() {
		return SharedPreferencesForOrder2Util.getInstance(getContext()).getProductCtrl();
	}

	@Override
	public void setProductId(String productId) {
		List<Dictionary> selectedList = OrderDetail.getProductRelatedList(productId, this.getContext());
		int i=0;
		boolean isRefreshed = false;
		ProItemLayout layout = null;
		Dictionary temp;
		for(Dictionary dict : selectedList){
			layout = this.getItemLayoutMap().get(this.getProductConfList().get(i).getDictTable());
			temp = layout.getSeleced();
			layout.setSelected(dict);
			if(!isRefreshed && !dict.equals(temp)){
				layout.setSelected();
				isRefreshed = true;
			}
			i++;
		}
		
	}
	
	
}
