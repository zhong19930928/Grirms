package com.yunhu.yhshxc.order2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.order2.bo.Order2;
import com.yunhu.yhshxc.order2.bo.OrderSpec;
import com.yunhu.yhshxc.utility.PublicUtils;

public class OrderSearchExplainFragment extends Fragment {

	
	private TextView tv_order_explain_already_pay;//已支付
	private TextView tv_order_explain_discount;//折扣
	private TextView tv_order_explain_unpay;//未付金额
	private TextView tv_order_explain_order_pepople;//订货联系人
	private TextView tv_order_explain_consignee;//收货联系人
	private TextView tv_order_explain_order_supplement;//订单说明
	
	private LinearLayout ll_order_explain_already_pay_main;//已支付
	private LinearLayout ll_order_explain_unpay_main;//未付金额
	private LinearLayout ll_order_explain_discount_main;//折扣
	private LinearLayout ll_place_user;//订货联系人
	private LinearLayout ll_receive_user;//收货联系人
	

	private OrderSpec orderSpec = null;
	private Order2 order = null;
	private OrderSearchDetailActivity orderSearchDetailActivity;

    public OrderSearchExplainFragment() {
        super();
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	super.onCreateView(inflater, container, savedInstanceState);
    	View view = View.inflate(this.getActivity(), R.layout.order_search_explain_fragmen, null);
    	tv_order_explain_already_pay = (TextView)view.findViewById(R.id.tv_order_explain_already_pay);
    	tv_order_explain_discount = (TextView)view.findViewById(R.id.tv_order_explain_discount);
    	tv_order_explain_unpay = (TextView)view.findViewById(R.id.tv_order_explain_unpay);
    	tv_order_explain_order_pepople = (TextView)view.findViewById(R.id.tv_order_explain_order_pepople);
    	tv_order_explain_consignee = (TextView)view.findViewById(R.id.tv_order_explain_consignee);
    	tv_order_explain_order_supplement = (TextView)view.findViewById(R.id.tv_order_explain_order_supplement);
    	
    	ll_order_explain_already_pay_main = (LinearLayout)view.findViewById(R.id.ll_order_explain_already_pay_main);
    	ll_order_explain_unpay_main = (LinearLayout)view.findViewById(R.id.ll_order_explain_unpay_main);
    	ll_order_explain_discount_main = (LinearLayout)view.findViewById(R.id.ll_order_explain_discount_main);
    	ll_place_user = (LinearLayout)view.findViewById(R.id.ll_place_user);
    	ll_receive_user = (LinearLayout)view.findViewById(R.id.ll_receive_user);
    	
    	initData();
        return view;
    }

    /**
     * 是否需要支付（1、不要；2、要；默认：2）
     */
    private void payControl(){
    	int is_pay = SharedPreferencesForOrder2Util.getInstance(this.getActivity()).getIsPay();
    	switch (is_pay) {
		case 1:
			ll_order_explain_already_pay_main.setVisibility(View.GONE);
			ll_order_explain_discount_main.setVisibility(View.GONE);
			ll_order_explain_unpay_main.setVisibility(View.GONE);
			break;
		default:
			ll_order_explain_already_pay_main.setVisibility(View.VISIBLE);
			ll_order_explain_discount_main.setVisibility(View.VISIBLE);
			ll_order_explain_unpay_main.setVisibility(View.VISIBLE);
			break;

		}
    	
    }
    
    /**
     * 订货联系人控制
     * 是否需要订货联系人（1、不要；2、要；默认：2）
     */
    private void placeUserControl(){
    	int place_user = SharedPreferencesForOrder2Util.getInstance(getActivity()).getIsPlaceUser();
    	switch (place_user) {
		case 1:
			ll_place_user.setVisibility(View.GONE);
			break;
		default:
			ll_place_user.setVisibility(View.VISIBLE);
			break;
		}
    }
    
    /**
     * 订货联系人控制
     * 是否需要订货联系人（1、不要；2、要；默认：2）
     */
    private void receiveUserControl(){
    	int receive_user = SharedPreferencesForOrder2Util.getInstance(getActivity()).getIsReceiveUser();
    	switch (receive_user) {
		case 1:
			ll_receive_user.setVisibility(View.GONE);
			break;
		default:
			ll_receive_user.setVisibility(View.VISIBLE);
			break;
		}
    }
    
    /**
     * 初始化数据
     */
    public void initData(){
    	orderSearchDetailActivity = (OrderSearchDetailActivity) this.getActivity();
    	order = orderSearchDetailActivity.order;
    	if (order!=null) {
    		orderSpec = order.getOrderSpec();
		}
    	if (orderSpec !=null) {
    		tv_order_explain_already_pay.setText(PublicUtils.formatDouble(orderSpec.getPay()));
        	tv_order_explain_discount.setText(PublicUtils.formatDouble(orderSpec.getDiscount()));
//    		double unPay = Double.parseDouble(TextUtils.isEmpty(order.getAmount())?"0":order.getAmount()) - orderSpec.getPay() - orderSpec.getDiscount();
        	tv_order_explain_unpay.setText(PublicUtils.formatDouble(orderSpec.getUnPay()));
        	tv_order_explain_order_pepople.setText(orderSpec.getPlaceUser()!=null?orderSpec.getPlaceUser().getUserName():"");
        	tv_order_explain_consignee.setText(orderSpec.getReceiveUser()!=null?orderSpec.getReceiveUser().getUserName():"");
        	tv_order_explain_order_supplement.setText(TextUtils.isEmpty(orderSpec.getSpec())?"":orderSpec.getSpec().trim());
		}
    	payControl();
    	placeUserControl();
    	receiveUserControl();
    }
	
	
}
