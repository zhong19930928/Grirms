package com.yunhu.yhshxc.order3;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.order3.bo.Order3;
import com.yunhu.yhshxc.order3.bo.Order3Contact;
import com.yunhu.yhshxc.order3.bo.Order3PromotionInfo;
import com.yunhu.yhshxc.order3.bo.Order3ShoppingCart;
import com.yunhu.yhshxc.order3.db.Order3ShoppingCartDB;
import com.yunhu.yhshxc.order3.util.SharedPreferencesForOrder3Util;
import com.yunhu.yhshxc.order3.view.Order3DiscountItem;
import com.yunhu.yhshxc.order3.view.Order3DtailItem;
import com.yunhu.yhshxc.utility.PublicUtils;

public class YulanFragment extends Fragment {
	private TextView tv_preview;
	private TextView tv_time;
	private TextView tv_user;
	private TextView tv_tel_num;
	private TextView tv_contact;
	private TextView tv_price;
	private TextView tv_wsk;
	private LinearLayout order_detail_ll;
	private LinearLayout zengpin_ll;
	private LinearLayout ll_yulan_lxr;

	private TextView tv_sk;
	private TextView tv_jm;
	private TextView tv_ly;
	
	private List<Order3ShoppingCart> order3Shoppingcars;
	private Order3ShoppingCartDB order3ShoppingCartDB;
	private ShoppingCarActivity carAcrtivity;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.order3_da_yin_ding_dan, null);
		carAcrtivity = (ShoppingCarActivity) this.getActivity();
		initView(v);
		controlContact();
		return v;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initData();
	}
	
	//是否需要订货联系人 1不要 2要 默认是2
	private void controlContact(){
		int isContact = SharedPreferencesForOrder3Util.getInstance(carAcrtivity).getIsOrderUser();
		if (isContact == 1) {
			ll_yulan_lxr.setVisibility(View.GONE);
		}else{
			ll_yulan_lxr.setVisibility(View.VISIBLE);
		}
	}
	
	private void initView(View v) {
		order3ShoppingCartDB = new Order3ShoppingCartDB(carAcrtivity);
		tv_preview = (TextView) v.findViewById(R.id.tv_preview);
		tv_time = (TextView) v.findViewById(R.id.tv_time);
		tv_user = (TextView) v.findViewById(R.id.tv_user);
		tv_tel_num = (TextView) v.findViewById(R.id.tv_tel_num);
		tv_price = (TextView) v.findViewById(R.id.tv_price);
		order_detail_ll = (LinearLayout) v.findViewById(R.id.order_detail_ll);
		ll_yulan_lxr = (LinearLayout) v.findViewById(R.id.ll_yulan_lxr);
		zengpin_ll = (LinearLayout) v.findViewById(R.id.zengpin_ll);
		tv_sk = (TextView)v.findViewById(R.id.tv_sk);
		tv_jm = (TextView)v.findViewById(R.id.tv_jm);
		tv_ly = (TextView)v.findViewById(R.id.tv_ly);
		tv_wsk = (TextView)v.findViewById(R.id.tv_wsk);
		tv_contact = (TextView)v.findViewById(R.id.tv_contact);
	}
	List<Order3ShoppingCart> shoppingcarts = new ArrayList<Order3ShoppingCart>();
	List<Order3PromotionInfo> infos ;
	
	
	public void initData(){
		tv_sk.setText(SharedPreferencesForOrder3Util.getInstance(carAcrtivity).getShouKuan()+" 元");
		tv_jm.setText(SharedPreferencesForOrder3Util.getInstance(carAcrtivity).getJianMian()+" 元");
		tv_ly.setText(SharedPreferencesForOrder3Util.getInstance(carAcrtivity).getLeaveMessage());
		
		Order3 order3 =((ShoppingCarActivity) carAcrtivity).getOrder3(null);
		
		tv_user.setText(SharedPreferencesForOrder3Util.getInstance(carAcrtivity).getOrder3StoreName());
		tv_preview.setText(order3.getOrderNo());//订单编号
		tv_time.setText(order3.getOrderTime());//下单时间
		tv_price.setText(PublicUtils.formatDouble(order3.getActualAmount())+" 元");//订单金额
		double wsk = order3.getActualAmount()-Double.parseDouble(SharedPreferencesForOrder3Util.getInstance(carAcrtivity).getShouKuan());
		tv_wsk.setText(PublicUtils.formatDouble(wsk)+" 元");
		setDingDanMingXi();//订单明细
		setZengPingZheKou();//赠品折扣
		Order3Contact contact = order3.getContact();
		if (contact!=null) {
			tv_contact.setText(contact.getUserName());
			String phone_1 = contact.getUserPhone1();
			String phone_2 = contact.getUserPhone2();
			String phone_3 = contact.getUserPhone3();
			StringBuffer phone = new StringBuffer();
			if (!TextUtils.isEmpty(phone_1)) {
				phone.append(phone_1).append("\n");
			}
			if (!TextUtils.isEmpty(phone_2)) {
				phone.append(phone_2).append("\n");
			}
			if (!TextUtils.isEmpty(phone_3)) {
				phone.append(phone_3);
			}
			tv_tel_num.setText(phone.toString());
		}else{
			tv_contact.setText("");
			tv_tel_num.setText("");
		}
	}

	private void setZengPingZheKou() {
		
		infos = ((ShoppingCarActivity) carAcrtivity).getProInfo();
		zengpin_ll.removeAllViews();
		for (int i = 0; i < infos.size(); i++) {
			Order3DiscountItem item = new Order3DiscountItem(getActivity());
			item.initData(i+1, infos.get(i));
			zengpin_ll.addView(item.getView());
		}
	}

	private void setDingDanMingXi() {
		order3Shoppingcars = order3ShoppingCartDB.findAllList();
		shoppingcarts.clear();
		for(int i = 0; i<order3Shoppingcars.size();i++){
			Order3ShoppingCart cart = order3Shoppingcars.get(i);
			if(cart.getPitcOn()==1){
				shoppingcarts.add(cart);
			}
		}
		order_detail_ll.removeAllViews();
		for(int i = 0;i<shoppingcarts.size();i++){
			Order3DtailItem item = new Order3DtailItem(carAcrtivity);
			item.initData(i+1, shoppingcarts.get(i));
			order_detail_ll.addView(item.getView());
		}
	}

}
