package com.yunhu.yhshxc.activity.carSales.scene.sellingGoods;

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
import com.yunhu.yhshxc.activity.carSales.bo.CarSales;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesContact;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesPromotionInfo;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesShoppingCart;
import com.yunhu.yhshxc.activity.carSales.db.CarSalesShoppingCartDB;
import com.yunhu.yhshxc.activity.carSales.scene.view.DingdanMXView;
import com.yunhu.yhshxc.activity.carSales.scene.view.ZengpingZKView;
import com.yunhu.yhshxc.activity.carSales.util.SharedPreferencesForCarSalesUtil;
import com.yunhu.yhshxc.utility.PublicUtils;

public class CarSaleYulanFragment extends Fragment {
	
	private TextView tv_preview;
	private TextView tv_time;
	private TextView tv_user;
	private TextView tv_tel_num;
	private TextView tv_contact;
	private TextView tv_price;
	private LinearLayout order_detail_ll;
	private LinearLayout zengpin_ll;
	private LinearLayout ll_yulan_lxr;

	private TextView tv_sk;
	private TextView tv_jm;
	private TextView tv_ly;
	private TextView tv_wsk;
	
	private List<CarSalesShoppingCart> order3Shoppingcars;
	private CarSalesShoppingCartDB order3ShoppingCartDB;
	private CarSaleShoppingCartActivity carAcrtivity;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.order3_da_yin_ding_dan, container, false);
		carAcrtivity = (CarSaleShoppingCartActivity) this.getActivity();
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
		int isContact = SharedPreferencesForCarSalesUtil.getInstance(carAcrtivity).getIsCarSalesUser();
		if (isContact == 1) {
			ll_yulan_lxr.setVisibility(View.GONE);
		}else{
			ll_yulan_lxr.setVisibility(View.VISIBLE);
		}
	}
	
	private void initView(View v) {
		order3ShoppingCartDB = new CarSalesShoppingCartDB(carAcrtivity);
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
		tv_wsk = (TextView) v.findViewById(R.id.tv_wsk);
		tv_contact = (TextView)v.findViewById(R.id.tv_contact);
	}
	List<CarSalesShoppingCart> shoppingcarts = new ArrayList<CarSalesShoppingCart>();
	List<CarSalesPromotionInfo> infos ;
	
	
	public void initData(){
		tv_sk.setText(SharedPreferencesForCarSalesUtil.getInstance(carAcrtivity).getShouKuan()+" 元");
		tv_jm.setText(SharedPreferencesForCarSalesUtil.getInstance(carAcrtivity).getJianMian()+" 元");
		tv_ly.setText(SharedPreferencesForCarSalesUtil.getInstance(carAcrtivity).getLeaveMessage());
		
		CarSales order3 =((CarSaleShoppingCartActivity) carAcrtivity).getCarSales();
		
		tv_user.setText(SharedPreferencesForCarSalesUtil.getInstance(carAcrtivity).getCarSalesStoreName());
		tv_preview.setText(order3.getCarSalesNo());//订单编号
		tv_time.setText(order3.getCarSalesTime());//下单时间
		tv_price.setText(PublicUtils.formatDouble(order3.getActualAmount())+" 元");//订单金额
		double wsk = order3.getActualAmount()-Double.parseDouble(SharedPreferencesForCarSalesUtil.getInstance(carAcrtivity).getShouKuan());
		tv_wsk.setText(PublicUtils.formatDouble(wsk)+" 元");
		setDingDanMingXi();//订单明细
		setZengPingZheKou();//赠品折扣
		CarSalesContact contact = order3.getContact();
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
		
		infos = ((CarSaleShoppingCartActivity) carAcrtivity).getProInfo();
		zengpin_ll.removeAllViews();
		for (int i = 0; i < infos.size(); i++) {
			ZengpingZKView item = new ZengpingZKView(getActivity());
			item.initData(i+1, infos.get(i));
			zengpin_ll.addView(item.getView());
		}
	}

	private void setDingDanMingXi() {
		order3Shoppingcars = order3ShoppingCartDB.findAllList();
		shoppingcarts.clear();
		for(int i = 0; i<order3Shoppingcars.size();i++){
			CarSalesShoppingCart cart = order3Shoppingcars.get(i);
			if(cart.getPitcOn()==1){
				shoppingcarts.add(cart);
			}
		}
		order_detail_ll.removeAllViews();
		for(int i = 0;i<shoppingcarts.size();i++){
			DingdanMXView item = new DingdanMXView(carAcrtivity);
			item.initData(i+1, shoppingcarts.get(i));
			order_detail_ll.addView(item.getView());
		}
	}
}
