package com.yunhu.yhshxc.order2;

import gcg.org.debug.JLog;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.database.NewOrderDB;
import com.yunhu.yhshxc.database.OrderContactsDB;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.order2.bo.NewOrder;
import com.yunhu.yhshxc.order2.bo.OrderContacts;
import com.yunhu.yhshxc.order2.bo.OrderSpec;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.DropDown;
import com.yunhu.yhshxc.widget.DropDown.OnResultListener;

public class OrderExplainFragment extends Fragment {
	private final String TAG = "OrderExplainFragment";
	private LinearLayout ll_order2_edit;
	private LinearLayout ll_order2_syn;//同步联系人
	private boolean isEdit = false;//是否是编辑状态
	private EditText et_order_explain_already_pay;//已支付
	private EditText et_order_explain_discount;//折扣
//	private EditText et_order_explain_unpay;//未付金额
	private DropDown sp_order_explain_order_pepople;//订货联系人
	private DropDown sp_order_explain_consignee;//收货联系人
	private EditText et_order_explain_order_supplement;//订单说明
	
	private TextView tv_order_explain_already_pay;//已支付
	private TextView tv_order_explain_discount;//折扣
	private TextView tv_order_explain_unpay;//未付金额
	private TextView tv_order_explain_order_pepople;//订货联系人
	private TextView tv_order_explain_consignee;//收货联系人
	private TextView tv_order_explain_order_supplement;//订单说明
	
	private LinearLayout ll_order_explain_already_pay;//已支付
	private LinearLayout ll_order_explain_discount;//折扣
//	private LinearLayout ll_order_explain_unpay;//未付金额
	private LinearLayout ll_order_explain_order_supplement;//订单说明
	
	private TextView tv_order_edit_title;//按钮标题
	
	private LinearLayout ll_order_explain_already_pay_main;//已支付
	private LinearLayout ll_order_explain_unpay_main;//未付金额
//	private LinearLayout ll_order_explain_discount_main;//折扣
	private LinearLayout ll_place_user;//订货联系人
	private LinearLayout ll_receive_user;//收货联系人
	
	private TextView tv_order_explain_already_pay_title;//已支付

	private OrderContacts placeUser;//订货联系人
	private OrderContacts receiveUser;//收货联系人
	
	private String storeId;//店面ID
	
	private OrderContactsDB orderContactsDB = null;
	private List<OrderContacts> orderContactsList = null;
	
	private OrderCreateActivity orderCreateActivity;
    public OrderExplainFragment() {
        super();
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	super.onCreateView(inflater, container, savedInstanceState);
    	orderContactsDB = new OrderContactsDB(getActivity());
    	View view = View.inflate(this.getActivity(), R.layout.order_explain_fragmen, null);
    	tv_order_edit_title = (TextView)view.findViewById(R.id.tv_order_edit_title);
    	tv_order_explain_already_pay_title = (TextView)view.findViewById(R.id.tv_order_explain_already_pay_title);
    	ll_order2_syn = (LinearLayout)view.findViewById(R.id.ll_order2_syn);
    	ll_order2_syn.setOnClickListener(listener);
    	ll_order2_edit = (LinearLayout)view.findViewById(R.id.ll_order2_edit);
    	ll_order2_edit.setOnClickListener(listener);
    	et_order_explain_already_pay = (EditText)view.findViewById(R.id.et_order_explain_already_pay);
    	et_order_explain_discount = (EditText)view.findViewById(R.id.et_order_explain_discount);
//    	et_order_explain_unpay = (EditText)view.findViewById(R.id.et_order_explain_unpay);
//    	et_order_explain_unpay.addTextChangedListener(textWatcher);
    	sp_order_explain_order_pepople = (DropDown)view.findViewById(R.id.sp_order_explain_order_pepople);
    	sp_order_explain_order_pepople.setOnResultListener(placeUserListener);
    	
    	sp_order_explain_consignee = (DropDown)view.findViewById(R.id.sp_order_explain_consignee);
    	sp_order_explain_consignee.setOnResultListener(receiveUserListener);
    	
    	et_order_explain_order_supplement = (EditText)view.findViewById(R.id.et_order_explain_order_supplement);
    	tv_order_explain_already_pay = (TextView)view.findViewById(R.id.tv_order_explain_already_pay);
    	tv_order_explain_discount = (TextView)view.findViewById(R.id.tv_order_explain_discount);
    	tv_order_explain_unpay = (TextView)view.findViewById(R.id.tv_order_explain_unpay);
    	tv_order_explain_order_pepople = (TextView)view.findViewById(R.id.tv_order_explain_order_pepople);
    	tv_order_explain_consignee = (TextView)view.findViewById(R.id.tv_order_explain_consignee);
    	tv_order_explain_order_supplement = (TextView)view.findViewById(R.id.tv_order_explain_order_supplement);
    	ll_order_explain_already_pay = (LinearLayout)view.findViewById(R.id.ll_order_explain_already_pay);
    	ll_order_explain_discount = (LinearLayout)view.findViewById(R.id.ll_order_explain_discount);
//    	ll_order_explain_unpay = (LinearLayout)view.findViewById(R.id.ll_order_explain_unpay);
    	ll_order_explain_order_supplement = (LinearLayout)view.findViewById(R.id.ll_order_explain_order_supplement);
    	
    	ll_order_explain_already_pay_main = (LinearLayout)view.findViewById(R.id.ll_order_explain_already_pay_main);
    	ll_order_explain_unpay_main = (LinearLayout)view.findViewById(R.id.ll_order_explain_unpay_main);
//    	ll_order_explain_discount_main = (LinearLayout)view.findViewById(R.id.ll_order_explain_discount_main);
    	ll_place_user = (LinearLayout)view.findViewById(R.id.ll_place_user);
    	ll_receive_user = (LinearLayout)view.findViewById(R.id.ll_receive_user);
    	
    	et_order_explain_already_pay.addTextChangedListener(alreadyPayTextWatcher);
    	et_order_explain_discount.addTextChangedListener(discountTextWatcher);

    	try {
        	initData();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	setEditState(isEdit);
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
//			ll_order_explain_discount_main.setVisibility(View.GONE);
			ll_order_explain_unpay_main.setVisibility(View.GONE);
			break;
		default:
			ll_order_explain_already_pay_main.setVisibility(View.VISIBLE);
//			ll_order_explain_discount_main.setVisibility(View.VISIBLE);
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
     * 初始化订单附加说明
     * @throws JSONException
     */
    public void initData() throws JSONException{
    	orderCreateActivity = (OrderCreateActivity) this.getActivity();
		if (orderCreateActivity.newOrder!=null && orderCreateActivity.newOrder.getOrder()!=null) {
			orderCreateActivity.order = orderCreateActivity.newOrder.getOrder();
		}
		if (orderCreateActivity.order==null || orderCreateActivity.order.getOrderSpec()==null) {
			orderCreateActivity.orderSpec = new OrderSpec();
		}else{
			orderCreateActivity.orderSpec = orderCreateActivity.order.getOrderSpec();
		}
    	if (orderCreateActivity.orderSpec !=null) {
    		tv_order_explain_already_pay.setText(PublicUtils.formatDouble(orderCreateActivity.orderSpec.getPay()));
        	tv_order_explain_discount.setText(PublicUtils.formatDouble(orderCreateActivity.orderSpec.getDiscount()));
        	tv_order_explain_unpay.setText(PublicUtils.formatDouble(orderCreateActivity.orderSpec.getUnPay()));
        	tv_order_explain_order_pepople.setText(orderCreateActivity.orderSpec.getPlaceUser()!=null?orderCreateActivity.orderSpec.getPlaceUser().getUserName():"");
        	tv_order_explain_consignee.setText(orderCreateActivity.orderSpec.getReceiveUser()!=null?orderCreateActivity.orderSpec.getReceiveUser().getUserName():"");
        	tv_order_explain_order_supplement.setText(TextUtils.isEmpty(orderCreateActivity.orderSpec.getSpec())?"":orderCreateActivity.orderSpec.getSpec().trim());
        	
        	et_order_explain_already_pay.setText(PublicUtils.formatDouble(orderCreateActivity.orderSpec.getPay()));
        	et_order_explain_discount.setText(PublicUtils.formatDouble(orderCreateActivity.orderSpec.getDiscount()));
//        	et_order_explain_unpay.setText(PublicUtils.formatDouble(orderCreateActivity.orderSpec.getUnPay()));
        	et_order_explain_order_supplement.setText(TextUtils.isEmpty(orderCreateActivity.orderSpec.getSpec())?"":orderCreateActivity.orderSpec.getSpec().trim());
		}
    	
    	payControl();
    	placeUserControl();
    	receiveUserControl();
    }
    
    private void synBtnControl(){//同步联系人按钮控制显示隐藏
    	if (ll_place_user.isShown() || ll_receive_user.isShown()) {
    		ll_order2_syn.setVisibility(View.VISIBLE);
		}else{
			ll_order2_syn.setVisibility(View.GONE);
		}
    }
    
    /**
     * 初始化订单联系人
     */
    private void initOrderContacts(){
    	orderContactsList = orderContactsDB.findAllOrderContactsByStoreId(storeId);
    	List<Dictionary> contactsList = new ArrayList<Dictionary>();
		OrderContacts orderContacts = null;
    	for (int i = 0; i < orderContactsList.size(); i++) {
    		orderContacts = orderContactsList.get(i);
    		Dictionary dic = new Dictionary();
    		dic.setDid(String.valueOf(orderContacts.getOrderContactsId()));
    		dic.setCtrlCol(orderContacts.getUserName());
    		contactsList.add(dic);
		}
    	sp_order_explain_order_pepople.setSrcDictList(contactsList);
    	sp_order_explain_consignee.setSrcDictList(contactsList);
    	
    	OrderContacts placeUser = orderCreateActivity.orderSpec.getPlaceUser();
    	OrderContacts recceiveUser = orderCreateActivity.orderSpec.getReceiveUser();

    	if (placeUser!=null) {//如果有订货联系人设置已经选中的订货联系人
    		for (int i = 0; i < contactsList.size(); i++) {
    			Dictionary dic = contactsList.get(i);
    			if (placeUser.getOrderContactsId() == Integer.parseInt(dic.getDid())) {
    				sp_order_explain_order_pepople.setSelected(dic);
    				break;
				}
    			
    		}
		}
    	
    	if (recceiveUser!=null) {//如果有收获联系人设置已经选中的收获联系人
    		for (int i = 0; i < contactsList.size(); i++) {
    			Dictionary dic = contactsList.get(i);
    			if (recceiveUser.getOrderContactsId() == Integer.parseInt(dic.getDid())) {
    				sp_order_explain_consignee.setSelected(dic);
    				break;
				}
    			
    		}
		}
    	

    }
    
    private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ll_order2_edit:
				edit();
				break;
			case R.id.ll_order2_syn:
				synContact();
				break;
			default:
				break;
			}
		}
	};
	
	/**
	 * 编辑订单附加说明
	 */
	private void edit(){
		if (orderCreateActivity.order==null || TextUtils.isEmpty(orderCreateActivity.order.getStoreId())) {
			Toast.makeText(getActivity(), "请选择客户", Toast.LENGTH_SHORT).show();
			return;
		}
		storeId = orderCreateActivity.storeId;
		if (isEdit) {//当前输入框状态是可编辑状态 此时保存
			tv_order_edit_title.setText("编辑");
			save();
			isEdit = false;
			ll_order2_syn.setVisibility(View.GONE);
			tv_order_explain_already_pay_title.setText("已支付");
			ll_order_explain_unpay_main.setVisibility(View.VISIBLE);
		}else{//当前状态是不可编辑状态 此时编辑
	    	initOrderContacts();
			isEdit = true;
			tv_order_edit_title.setText("保存");
			synBtnControl();
			tv_order_explain_already_pay_title.setText("支付金额");
			ll_order_explain_unpay_main.setVisibility(View.GONE);
		}
    	setEditState(isEdit);
	}
	
	/**
	 * 联网同步联系人
	 */
	private Dialog synDialog = null;
	private void synContact(){
		storeId = orderCreateActivity.storeId;
		if (!TextUtils.isEmpty(storeId)) {

			synDialog =  new MyProgressDialog(this.getActivity(),R.style.CustomProgressDialog,"正在同步...");
			String url = UrlInfo.queryOrderNewUserInfo(getActivity(), storeId);
			GcgHttpClient.getInstance(this.getActivity()).post(url, null,new HttpResponseListener() {
				@Override
				public void onStart() {
					synDialog.show();
				}


				@Override
				public void onFailure(Throwable error, String content) {
					Toast.makeText(getActivity(), "联系人同步失败", Toast.LENGTH_SHORT).show();

				}

				@Override
				public void onFinish() {
					synDialog.dismiss();
				}


				@Override
				public void onSuccess(int statusCode, String content) {

					JLog.d(TAG, content);
					try {
						JSONObject obj = new JSONObject(content);
						String resultcode = obj.getString("resultcode");
						if ("0000".equals(resultcode)) {
							if (obj.has("orderUser") && !TextUtils.isEmpty(obj.getString("orderUser"))) {
								JSONArray arr = obj.getJSONArray("orderUser");
								JSONObject userObj = null;
								OrderContacts orderContacts = null;
								orderContactsList.clear();
								orderContactsDB.clearOrderContacts();
								for (int i = 0; i < arr.length(); i++) {
									userObj = arr.getJSONObject(i);
									orderContacts = new OrderContacts();
									if (userObj.has("id")) {
										orderContacts.setOrderContactsId(userObj.getInt("id"));
									}
									if (userObj.has("user_address")) {
										orderContacts.setUserAddress(userObj.getString("user_address"));
									}
									if (userObj.has("user_name")) {
										orderContacts.setUserName(userObj.getString("user_name"));
									}
									if (userObj.has("user_phone1")) {
										orderContacts.setUserPhone1(userObj.getString("user_phone1"));
									}
									if (userObj.has("user_phone2")) {
										orderContacts.setUserPhone2(userObj.getString("user_phone2"));
									}
									if (userObj.has("user_phone3")) {
										orderContacts.setUserPhone3(userObj.getString("user_phone3"));
									}
									orderContacts.setStoreId(storeId);
									orderContactsDB.insertOrderContants(orderContacts);
									orderContactsList.add(orderContacts);
								}
								initOrderContacts();
							}else{
								Toast.makeText(getActivity(), "没有联系人信息", Toast.LENGTH_SHORT).show();
							}
						}else{
							Toast.makeText(getActivity(), "联系人同步失败", Toast.LENGTH_SHORT).show();
						}
						
					} catch (Exception e) {
						e.printStackTrace();
						Toast.makeText(getActivity(), "联系人同步失败", Toast.LENGTH_SHORT).show();
					}
				
				}

			});
		
		}else{
			Toast.makeText(getActivity(), "请选择客户", Toast.LENGTH_SHORT).show();
		}
	}
	
	//保存
	private void save(){
		try {
			String payStr = et_order_explain_already_pay.getText().toString().trim();
			double pay = Double.parseDouble(TextUtils.isEmpty(payStr)?"0.0": payStr);
			orderCreateActivity.orderSpec.setPay(pay);
			String discountStr = et_order_explain_discount.getText().toString().trim();
			double discount = Double.parseDouble(TextUtils.isEmpty(discountStr)?"0.0":discountStr);
			orderCreateActivity.orderSpec.setDiscount(discount);
			orderCreateActivity.orderSpec.setPlaceUser(placeUser);
			orderCreateActivity.orderSpec.setReceiveUser(receiveUser);
			String spec = et_order_explain_order_supplement.getText().toString().trim();
			orderCreateActivity.orderSpec.setSpec(spec);
			double unPay = Double.parseDouble(TextUtils.isEmpty(orderCreateActivity.order.getAmount())?"0.0":orderCreateActivity.order.getAmount()) - orderCreateActivity.orderSpec.getPay() - orderCreateActivity.orderSpec.getDiscount();
			orderCreateActivity.orderSpec.setUnPay(unPay);
			orderCreateActivity.order.setOrderSpec(orderCreateActivity.orderSpec);
			
			NewOrderDB newOrderDB = new NewOrderDB(getActivity());
			NewOrder newOrder = newOrderDB.findNewOrderByStoreId(orderCreateActivity.storeId);
			newOrder.setOrder(orderCreateActivity.order);
			newOrderDB.updateNewOrder(newOrder);
			
        	tv_order_explain_already_pay.setText(String.valueOf(orderCreateActivity.orderSpec.getPay()));
        	tv_order_explain_discount.setText(String.valueOf(orderCreateActivity.orderSpec.getDiscount()));
        	tv_order_explain_unpay.setText(PublicUtils.formatDouble(orderCreateActivity.orderSpec.getUnPay()));
        	tv_order_explain_order_pepople.setText(orderCreateActivity.orderSpec.getPlaceUser()!=null?orderCreateActivity.orderSpec.getPlaceUser().getUserName():"");
        	tv_order_explain_consignee.setText(orderCreateActivity.orderSpec.getReceiveUser()!=null?orderCreateActivity.orderSpec.getReceiveUser().getUserName():"");
        	tv_order_explain_order_supplement.setText(TextUtils.isEmpty(orderCreateActivity.orderSpec.getSpec())?"":orderCreateActivity.orderSpec.getSpec().trim());
        	
        	et_order_explain_already_pay.setText(String.valueOf(orderCreateActivity.orderSpec.getPay()));
        	et_order_explain_discount.setText(String.valueOf(orderCreateActivity.orderSpec.getDiscount()));
//        	et_order_explain_unpay.setText(PublicUtils.formatDouble(orderCreateActivity.orderSpec.getUnPay()));
        	et_order_explain_order_supplement.setText(TextUtils.isEmpty(orderCreateActivity.orderSpec.getSpec())?"":orderCreateActivity.orderSpec.getSpec().trim());
        	
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getActivity(), R.string.ERROR_DATA, Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * 根据“编辑”和“保存”的状态判断隐藏显示
	 * @param flag 保存的时候true 编辑的时候是false
	 */
	private void setEditState(boolean flag){
		int isShowEdit = flag ? View.VISIBLE : View.GONE;
		ll_order_explain_already_pay.setVisibility(isShowEdit);
		ll_order_explain_discount.setVisibility(isShowEdit);
//		ll_order_explain_unpay.setVisibility(isShowEdit);
		sp_order_explain_order_pepople.setVisibility(isShowEdit);
		sp_order_explain_consignee.setVisibility(isShowEdit);
		ll_order_explain_order_supplement.setVisibility(isShowEdit);
		int isShowText = flag ? View.GONE : View.VISIBLE;
		tv_order_explain_already_pay.setVisibility(isShowText);
		tv_order_explain_discount.setVisibility(isShowText);
//		tv_order_explain_unpay.setVisibility(isShowText);
		tv_order_explain_order_pepople.setVisibility(isShowText);
		tv_order_explain_consignee.setVisibility(isShowText);
		tv_order_explain_order_supplement.setVisibility(isShowText);
		payControl();
    	placeUserControl();
    	receiveUserControl();
	}
	
	/**
	 * 选择订货联系人监听
	 */
	private OnResultListener placeUserListener = new OnResultListener() {
		
		@Override
		public void onResult(List<Dictionary> result) {
			if (result!=null && !result.isEmpty()&&result.get(0)!=null) {
				Dictionary dic = result.get(0);
				placeUser = new OrderContactsDB(getActivity()).findOrderContactsByContactsIdAndStoreId(Integer.parseInt(dic.getDid()), storeId);
			}else{
				placeUser = null;
			}
			
		}
	};
	/**
	 * 选择收货联系人监听
	 */
	private OnResultListener receiveUserListener = new OnResultListener() {
		
		@Override
		public void onResult(List<Dictionary> result) {
			if (result!=null && !result.isEmpty()&&result.get(0)!=null) {
				Dictionary dic = result.get(0);
				receiveUser = new OrderContactsDB(getActivity()).findOrderContactsByContactsIdAndStoreId(Integer.parseInt(dic.getDid()), storeId);
			}else{
				receiveUser = null;
			}
			
		}
	};
	
	
	/**
	 * 已支付文本框的控制和监听值改变  控制只能收入小数点后两位 值变化的时候重新计算未支付的值
	 */
	private TextWatcher alreadyPayTextWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			double alreadPay = Double.parseDouble(TextUtils.isEmpty(s.toString())?"0.0":s.toString());
			double discount = Double.parseDouble(TextUtils.isEmpty(et_order_explain_discount.getText().toString())?"0.0":et_order_explain_discount.getText().toString());
			double unPay = Double.parseDouble(TextUtils.isEmpty(orderCreateActivity.order.getAmount())?"0.0":orderCreateActivity.order.getAmount()) - alreadPay - discount;
			tv_order_explain_unpay.setText(PublicUtils.formatDouble(unPay));
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}
		
		@Override
		public void afterTextChanged(Editable s) {
			String temp = s.toString();
			int posDot = temp.indexOf(".");
			if (posDot <= 0) {
				return;
			}
			if (temp.length() - posDot - 1 > 2) {
				s.delete(posDot+3, posDot+4);
			}
		}
	};
	/**
	 * 折扣输入框值的控制和值变化监听  控制只能收入小数点后两位 值变化的时候重新计算未支付的值
	 */
	private TextWatcher discountTextWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			double discount = Double.parseDouble(TextUtils.isEmpty(s.toString())?"0.0":s.toString());
			double alreadPay = Double.parseDouble(TextUtils.isEmpty(et_order_explain_already_pay.getText().toString())?"0.0":et_order_explain_already_pay.getText().toString());
			double unPay = Double.parseDouble(TextUtils.isEmpty(orderCreateActivity.order.getAmount())?"0.0":orderCreateActivity.order.getAmount()) - alreadPay - discount;
			tv_order_explain_unpay.setText(PublicUtils.formatDouble(unPay));
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}
		
		@Override
		public void afterTextChanged(Editable s) {
			String temp = s.toString();
			int posDot = temp.indexOf(".");
			if (posDot <= 0) {
				return;
			}
			if (temp.length() - posDot - 1 > 2) {
				s.delete(posDot+3, posDot+4);
			}
		}
	};
}
