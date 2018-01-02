package com.yunhu.yhshxc.order3;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.order3.bo.Order3;
import com.yunhu.yhshxc.order3.bo.Order3Contact;
import com.yunhu.yhshxc.order3.bo.Order3ProductData;
import com.yunhu.yhshxc.order3.db.Order3ContactsDB;
import com.yunhu.yhshxc.order3.db.Order3DB;
import com.yunhu.yhshxc.order3.db.Order3ProductDataDB;
import com.yunhu.yhshxc.order3.print.Order3PrintForSearch;
import com.yunhu.yhshxc.order3.util.SharedPreferencesForOrder3Util;
import com.yunhu.yhshxc.order3.view.Order3DtailItem;
import com.yunhu.yhshxc.order3.view.Order3FreeProductIem;
import com.yunhu.yhshxc.print.templet.Element;
import com.yunhu.yhshxc.print.templet.Templet.DataSource;
import com.yunhu.yhshxc.utility.FileHelper;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.widget.ToastOrder;

public class Order3SearDetailActivity extends AbsBaseActivity implements DataSource {
	private TextView tv_preview;
	private TextView tv_contact;
	private TextView tv_user;
	private TextView tv_tel_num;
	private TextView tv_yulan_price;
	private TextView tv_yulan_all;
	private TextView tv_sk;
	private TextView tv_jm;
	private TextView tv_ly;

	private LinearLayout order_detail_ll;
	private LinearLayout zengpin_ll;
	private LinearLayout discount_ll;
	private LinearLayout yulan_dayin_ll;
	private Order3DB order3DB;
	private Order3 order3;
	private String orderNo;
	private Order3ProductDataDB order3ProductDataDB;
	private Order3ContactsDB order3ContactsDB ;
	private LinearLayout ll_yl_lxr;
	private LinearLayout ll_yl_phone;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order3_yulan);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		 orderNo = bundle.getString("orderNo");
		order3DB = new Order3DB(Order3SearDetailActivity.this);
		order3ProductDataDB = new Order3ProductDataDB(Order3SearDetailActivity.this);
		order3ContactsDB = new Order3ContactsDB(Order3SearDetailActivity.this);
		order3 = order3DB.findOrder3ByOrderNoAndStoreId(orderNo);
		initView();
		if(order3!=null){
			initData();
		}
		
	}
	
	private void initView() {
		tv_preview = (TextView) this.findViewById(R.id.tv_preview);
		tv_contact = (TextView) this.findViewById(R.id.tv_contact);
		tv_user = (TextView) this.findViewById(R.id.tv_user);
		tv_tel_num = (TextView) this.findViewById(R.id.tv_contract_num);
		tv_yulan_price = (TextView) this.findViewById(R.id.tv_yulan_price);
		tv_yulan_all = (TextView) this.findViewById(R.id.tv_yulan_all);
		tv_sk = (TextView) this.findViewById(R.id.tv_sk);
		tv_jm = (TextView) this.findViewById(R.id.tv_jm);
		tv_ly = (TextView) this.findViewById(R.id.tv_ly);
		order_detail_ll = (LinearLayout) this.findViewById(R.id.order_detail_ll);
		zengpin_ll = (LinearLayout) this.findViewById(R.id.zengpin_ll);
		discount_ll = (LinearLayout) this.findViewById(R.id.discount_ll);
		yulan_dayin_ll = (LinearLayout) this.findViewById(R.id.yulan_dayin_ll);
//		yulan_dayin_ll.setLayoutParams(new LayoutParams(getWindowWith()/4, LayoutParams.WRAP_CONTENT));
		yulan_dayin_ll.setOnClickListener(listner);
		ll_yl_lxr = (LinearLayout)findViewById(R.id.ll_yl_lxr);
		ll_yl_phone = (LinearLayout)findViewById(R.id.ll_yl_phone);
		controlPrint();
		controlContact();
	}
	
	//是否需要手机打印（1、不要；2、要；默认：1）
	private void controlPrint(){
		int isPrint = SharedPreferencesForOrder3Util.getInstance(this).getIsPhonePrint();
		if (isPrint == 1) {
			yulan_dayin_ll.setVisibility(View.GONE);
		}else{
			yulan_dayin_ll.setVisibility(View.VISIBLE);
		}
	}
	//是否需要订货联系人 1不要 2要 默认是2
	private void controlContact(){
		int isContact = SharedPreferencesForOrder3Util.getInstance(this).getIsOrderUser();
		if (isContact == 1) {
			ll_yl_lxr.setVisibility(View.GONE);
			ll_yl_phone.setVisibility(View.GONE);
		}else{
			ll_yl_lxr.setVisibility(View.VISIBLE);
			ll_yl_phone.setVisibility(View.VISIBLE);
		}
	}
	double discountAll = 0;
	Order3Contact contract;
	private void initData() {
		tv_preview.setText(order3.getOrderNo());
		contract = order3ContactsDB.findOrderContactsByContactsIdAndStoreId(order3.getContactId(), order3.getStoreId());
		if(contract!=null){
			tv_contact.setText(contract.getUserName());
			StringBuffer sb = new StringBuffer();
			if(!TextUtils.isEmpty(contract.getUserPhone1())){
				sb.append(contract.getUserPhone1());
			}
			if(!TextUtils.isEmpty(contract.getUserPhone2())){
				sb.append("\n").append(contract.getUserPhone2());
			}
			if(!TextUtils.isEmpty(contract.getUserPhone3())){
				sb.append("\n").append(contract.getUserPhone3());
			}
			tv_tel_num.setText(sb);
		}
		
		tv_user.setText(order3.getStoreName());
		tv_yulan_price.setText(PublicUtils.formatDouble(order3.getActualAmount()) +" 元");
		List<Order3ProductData> order3ProductDatas = order3ProductDataDB.findOrder3ProductDataByOrderNoContainPromotion(orderNo);
		discountAll = 0;
		if(order3ProductDatas!=null){
			for(int i = 0; i<order3ProductDatas.size(); i++){
				Order3ProductData data = order3ProductDatas.get(i);
				if(data.getIsOrderMain()==1){
					Order3DtailItem detailItem = new Order3DtailItem(Order3SearDetailActivity.this);
					detailItem.initData2(i+1, data);
					order_detail_ll.addView(detailItem.getView());
					mainData.add(data);
					
				}else if(data.getIsOrderMain() == 2){
					if(data.getProductId()==0){//打折减免  总和
						Order3FreeProductIem item = new Order3FreeProductIem(Order3SearDetailActivity.this, false);
						item.initDiscountData(data);
						discount_ll.addView(item.getView());
//						double a = data.getOrderAmount() - data.getActualAmount();
						discountAll+=data.getActualAmount();
						zhekouData.add(data);
					}else{//赠品情况
						if(data.getActualAmount()!=0||data.getOrderAmount()!=0){//有打折减免情况单品
							Order3FreeProductIem item = new Order3FreeProductIem(Order3SearDetailActivity.this, false);
							item.initDiscountData2(data);
							discount_ll.addView(item.getView());
							discountAll+=data.getActualAmount();
							zhekouData.add(data);
						}else{
							Order3FreeProductIem freeItem = new Order3FreeProductIem(Order3SearDetailActivity.this, true);
							freeItem.initFreeProductData(data);
							zengpin_ll.addView(freeItem.getView());
							zengpinData.add(data);
						}
					}
					
				}
			}
		}
		tv_sk.setText(PublicUtils.formatDouble(order3.getPayAmount())+" 元");
		tv_jm.setText(PublicUtils.formatDouble(order3.getOrderDiscount())+" 元");
		tv_ly.setText(order3.getNote());
		tv_yulan_all.setText("折扣 共"+PublicUtils.formatDouble(discountAll)+" 元");
	}
	private List<Order3ProductData> mainData = new ArrayList<Order3ProductData>();
	private List<Order3ProductData> zhekouData = new ArrayList<Order3ProductData>();
	private List<Order3ProductData> zengpinData = new ArrayList<Order3ProductData>();
	
	private OnClickListener listner = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.yulan_dayin_ll:
				print();
				break;

			default:
				break;
			}
		}
	};
	private Order3PrintForSearch orderPrint;
	private void print() {
		FileHelper help = new FileHelper();
		String json = help.readJsonString(Order3SearDetailActivity.this, "order3_print_search.txt");
		try {
			orderPrint = new Order3PrintForSearch(Order3SearDetailActivity.this);
			orderPrint.setOrder(order3);
			orderPrint.setMainData(mainData);
			orderPrint.setZengPinData(zengpinData);
			orderPrint.setZheKouData(zhekouData);
			orderPrint.setDiscount(PublicUtils.formatDouble(discountAll));
			orderPrint.setContact(contract);
			orderPrint.print(Order3SearDetailActivity.this, new JSONArray(json), Order3SearDetailActivity.this);
		}catch (Exception e) {
			e.printStackTrace();
			ToastOrder.makeText(this, "打印数据异常", ToastOrder.LENGTH_SHORT).show();
		}
		
	}

	@Override
	public void printLoop(List<Element> columns) {
		orderPrint.printLoop(columns);
		
	}

	@Override
	public void printRow(List<Element> columns) {
		orderPrint.printRow(columns);
	}

	@Override
	public void printPromotion(List<Element> columns) {
		orderPrint.printPromotion(columns);
	}

	@Override
	public void printZhekou(List<Element> columns) {
		orderPrint.printZhekou(columns);
	}
	
	@Override
	public void printDingdanbianhao(List<Element> columns) {
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (orderPrint!=null) {
			orderPrint.release();
		}
	}

	
}
