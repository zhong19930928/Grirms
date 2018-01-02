package com.yunhu.yhshxc.order2;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.order.bo.ProductConf;
import com.yunhu.yhshxc.order2.bo.Order2;
import com.yunhu.yhshxc.order2.bo.OrderContacts;
import com.yunhu.yhshxc.order2.bo.OrderDetail;
import com.yunhu.yhshxc.order2.bo.OrderSpec;

public class Order2Data {
	
	//detail
	private final static String DETAIL_PRODUCT_ID = "order_product_id"; //产品ID
	private final static String DETAIL_PRODUCT_UNIT_ID = "product_standard_id"; //产品单位
	private final static String DETAIL_PRODUCT_UNIT = "product_standard"; //产品单位
	private final static String DETAIL_ORDER_PRICE = "order_price"; //单价
	private final static String DETAIL_ORDER_STATUS = "order_status"; //订单状态
	private final static String DETAIL_ORDER_AMOUNT = "order_amount";  //金额
//	private final static String DETAIL_ACTUAL_AMOUNT = "actual_amount"; //实际金额
	private final static String DETAIL_ORDER_COUNT = "order_count"; //数量
//	private final static String DETAIL_ACTUAL_COUNT = "actual_count";  //实际数量
	private final static String DETAIL_ORDER_REMARK = "order_remark";  // 备份
	
	//List
	private final static String LIST_ORDER_TIME = "order_time";  //创建订单时间
	private final static String LIST_ORDER_NO= "order_num";  //订单号
	private final static String LIST_AUDIT_STATUS = "audit_status";  //审核状态
	private final static String LIST_STORE_NAME = "store_name";  //店面
	private final static String LIST_ACTUAL_AMOUNT = "actual_amount";  //金额

	//SPEC
	private final static String SPEC_PAY_AMOUNT = "pay_amount";  //已支付
	private final static String SPEC_ORDER_DISCOUNT = "order_discount";  //折扣
	private final static String SPEC_PLACE_USER = "place_user";  //订货联系人
	private final static String SPEC_RECEIVE_USER = "receive_user"; //收货联系人
	private final static String SPEC_ORDER_REMARK = "order_remark";  //备份
	private final static String SPEC_ORDER_UN_PAY = "unpaid_amount";  //备份

	
	//联系人
	private final static String CONTACTS_USER_ADDRESS = "user_address"; //收货联系人
	private final static String CONTACTS_ID = "id";
	private final static String CONTACTS_USER_NAME = "user_name";
	private final static String CONTACTS_USER_PHONE_1 = "user_phone_1";
	private final static String CONTACTS_USER_PHONE_2 = "user_phone_2";
	private final static String CONTACTS_USER_PHONE_3 = "user_phone_3";
	
	private final static String ORDER_DETAIL = "detail";  //产品
	private final static String ORDER_SPEC = "spec";  //金额
	private final static String ORDER_NO = "order_num";  //订单编号
	private final static String ORDER_DATE = "order_time";  //订单日期
	private final static String ORDER_AMOUNT = "order_amount";  //订单金额
	private final static String ORDER_STOREID = "order_store_id";  //店面
	private final static String ORDER_STORE_NAME = "store_name";  //店面
	private final static String AUDIT_STATUS = "audit_status";//订单状态


		
	/**
	 * 订单提交时，转成JSON
	 * 
	 * @param detail
	 * @param spec
	 * @return
	 * @throws JSONException
	 */
	public static String order2Json(Order2 order)
			throws JSONException {
		if(order == null){
			return null;
		}
		String detailStr = orderDetailList2Json(order.getOrderDetailList());
		String specStr = orderSpec2Json(order.getOrderSpec());
		if (detailStr == null && specStr == null) {
			return null;
		}
		JSONObject obj = new JSONObject();
		if (detailStr != null) {
			obj.put(ORDER_DETAIL, detailStr);
		}
		if (specStr != null) {
			obj.put(ORDER_SPEC, specStr);
		}
		
		String orderNo = order.getOrderNo();
		obj.put(ORDER_NO, orderNo);
		
		String orderDate = order.getOrderDate();
		obj.put(ORDER_DATE, orderDate);
		
		String amount = order.getAmount();
		obj.put(ORDER_AMOUNT, amount);
		
		String storeId = order.getStoreId();
		obj.put(ORDER_STOREID, storeId);
		
		String storeName = order.getStoreName();
		obj.put(ORDER_STORE_NAME, storeName);
		
		String status = order.getStatus();
		obj.put(AUDIT_STATUS, status);
		
		return obj.toString();
	}

	/**
	 * 将订单详细，转成JSON
	 * @param detailList
	 * @return
	 * @throws JSONException
	 */
	public static String orderDetailList2Json(List<OrderDetail> detailList) throws JSONException {
		
		if(detailList == null){return null;};

		JSONArray jsonArray = new JSONArray();
		for(OrderDetail detail : detailList){
			JSONObject obj = new JSONObject(orderDetail2Json(detail));
			jsonArray.put(obj);
		}
		return jsonArray.toString();
	}
	
	/**
	 * 将订单详细中的一个，转成JSON
	 * @param detail
	 */
	public static String orderDetail2Json(OrderDetail detail) throws JSONException {
		if(detail == null){
			return null;
		}
		
		JSONObject JSONObj = new JSONObject();
		Dictionary product = detail.getProduct();
		if (product!=null) {
			JSONObj.put(DETAIL_PRODUCT_ID, product.getDid());
		}
		if(detail.getProductUnit() != null){
			JSONObj.put(DETAIL_PRODUCT_UNIT_ID, detail.getProductUnit().getDid());
			JSONObj.put(DETAIL_PRODUCT_UNIT, detail.getProductUnit().getCtrlCol());
		}
		JSONObj.put(DETAIL_ORDER_PRICE, detail.getPrice());
		if(detail.getStatus() != null){
			JSONObj.put(DETAIL_ORDER_STATUS,detail.getStatus());	
		}
		JSONObj.put(DETAIL_ORDER_AMOUNT, detail.getAmount());
//		JSONObj.put(DETAIL_ACTUAL_AMOUNT, detail.getActualAmount());
		JSONObj.put(DETAIL_ORDER_COUNT, detail.getQuantity());
//		JSONObj.put(DETAIL_ACTUAL_COUNT, detail.getActualQuantity());
		if(detail.getRemark() != null){
			JSONObj.put(DETAIL_ORDER_REMARK, detail.getRemark());
		}
		return JSONObj.toString();
	}

	/**
	 * 将订单说名，转成JSON
	 * @param spec
	 * @return
	 * @throws JSONException
	 */
	public static String orderSpec2Json(OrderSpec spec) throws JSONException {
		if(spec == null){return null;};
		JSONObject jsonObj = new JSONObject();
		jsonObj.put(SPEC_PAY_AMOUNT, spec.getPay());
		jsonObj.put(SPEC_ORDER_DISCOUNT, spec.getDiscount());
		jsonObj.put(SPEC_PLACE_USER, contacts2Json(spec.getPlaceUser()));
		jsonObj.put(SPEC_RECEIVE_USER, contacts2Json(spec.getReceiveUser()));
		jsonObj.put(SPEC_ORDER_REMARK, spec.getSpec());
		jsonObj.put(SPEC_ORDER_UN_PAY, spec.getUnPay());
		
		return jsonObj.toString();
	}
	
	/**
	 * 将json转成OrderDetail
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static List<OrderDetail> json2OrderDetail(String json) throws JSONException {
		List<OrderDetail> detailList = new ArrayList<OrderDetail>();
		if (!TextUtils.isEmpty(json)) {
			JSONArray detailArr = new JSONArray(json);
			JSONObject detailObj = null;
			for (int i = 0; i < detailArr.length(); i++) {
//				detailArr.get(i);
				detailObj = detailArr.getJSONObject(i);
				detailList.add(json2OrderDetail(detailObj));
			}
		}
		return detailList;
	}
	
	/**
	 * 将json转成 OrderSpec
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static OrderSpec json2OrderSpace(String json)  throws JSONException{
		
		OrderSpec orderSpect = new OrderSpec();
		if (!TextUtils.isEmpty(json)) {
			JSONObject obj = new JSONObject(json);
			if (obj.has(SPEC_PAY_AMOUNT)) {
				orderSpect.setPay(obj.getDouble(SPEC_PAY_AMOUNT));
			}
			if (obj.has(SPEC_ORDER_DISCOUNT)) {
				orderSpect.setDiscount(obj.getDouble(SPEC_ORDER_DISCOUNT));
			}
			if (obj.has(SPEC_PLACE_USER)) {
				orderSpect.setPlaceUser(json2Contacts(obj.getString(SPEC_PLACE_USER)));
			}
			if (obj.has(SPEC_RECEIVE_USER)) {
				orderSpect.setReceiveUser(json2Contacts(obj.getString(SPEC_RECEIVE_USER)));
			}
			if (obj.has(SPEC_ORDER_REMARK)) {
				orderSpect.setSpec(obj.getString(SPEC_ORDER_REMARK));
			}
			if (obj.has(SPEC_ORDER_UN_PAY)) {
				orderSpect.setUnPay(obj.getDouble(SPEC_ORDER_UN_PAY));
			}
		}
		return orderSpect;
	}
	
	public static Order2 json2Order(String json)  throws JSONException{
		Order2 order = new Order2();
		if (!TextUtils.isEmpty(json)) {
			JSONObject obj = new JSONObject(json);
			if (obj.has(ORDER_DETAIL)) {
				String detailStr = obj.getString(ORDER_DETAIL);
				List<OrderDetail> list = json2OrderDetail(detailStr);
				order.setOrderDetailList(list);
			}
			if (obj.has(ORDER_SPEC)) {
				String specStr = obj.getString(ORDER_SPEC);
				OrderSpec orderSpec = json2OrderSpace(specStr);
				order.setOrderSpec(orderSpec);
			}
			if (obj.has(ORDER_NO)) {
				String orderNo = obj.getString(ORDER_NO);
				order.setOrderNo(orderNo);
			}
			if (obj.has(ORDER_DATE)) {
				String date = obj.getString(ORDER_DATE);
				order.setOrderDate(date);
			}
			if (obj.has(ORDER_AMOUNT)) {
				String amount = obj.getString(ORDER_AMOUNT);
				order.setAmount(amount);
			}
			if (obj.has(ORDER_STOREID)) {
				String storeId = obj.getString(ORDER_STOREID);
				order.setStoreId(storeId);
			}
			
			if (obj.has(ORDER_STORE_NAME)) {
				String name = obj.getString(ORDER_STORE_NAME);
				order.setStoreName(name);
			}
			
			if (obj.has(AUDIT_STATUS)) {
				String status = obj.getString(AUDIT_STATUS);
				order.setStatus(status);
			}
			
		}
		return order;
	}
	
	public static OrderDetail json2OrderDetail(JSONObject json)  throws JSONException{
		OrderDetail detail = null;
		if (json!=null) {
			detail = new OrderDetail();
//			if (json.has(DETAIL_ACTUAL_AMOUNT)) {
//				detail.setActualAmount(json.getDouble(DETAIL_ACTUAL_AMOUNT));
//			}
//			if (json.has(DETAIL_ORDER_COUNT)) {
//				detail.setActualQuantity(json.getDouble(DETAIL_ORDER_COUNT));
//			}
			if (json.has(DETAIL_ORDER_AMOUNT)) {
				detail.setAmount(json.getDouble(DETAIL_ORDER_AMOUNT));
			}
			if (json.has(DETAIL_ORDER_PRICE)) {
				detail.setPrice(json.getDouble(DETAIL_ORDER_PRICE));
			}
			if (json.has(DETAIL_ORDER_COUNT)) {
				detail.setQuantity(json.getDouble(DETAIL_ORDER_COUNT));
			}
			if (json.has(DETAIL_ORDER_REMARK)) {
				detail.setRemark(json.getString(DETAIL_ORDER_REMARK));
			}
			if (json.has(DETAIL_ORDER_STATUS)) {
				detail.setStatus(json.getInt(DETAIL_ORDER_STATUS));
			}
			if (json.has(DETAIL_PRODUCT_UNIT_ID) || json.has(DETAIL_PRODUCT_UNIT)) {
				String unitId = json.has(DETAIL_PRODUCT_UNIT_ID)?json.getString(DETAIL_PRODUCT_UNIT_ID):"";
				String unitName = json.has(DETAIL_PRODUCT_UNIT)?json.getString(DETAIL_PRODUCT_UNIT):"";
				Dictionary productUnit = new Dictionary();
				productUnit.setDid(unitId);
				productUnit.setCtrlCol(unitName);
				detail.setProductUnit(productUnit);
			}
			if (json.has(DETAIL_PRODUCT_ID)) {
				String productId = json.getString(DETAIL_PRODUCT_ID);
				Dictionary product = new Dictionary();
				product.setDid(productId);
				detail.setProduct(product);
			}
		}
		
		return detail;
	}
	
	public static String contacts2Json(OrderContacts contacts) throws JSONException {
		if (contacts==null) {
			return null;
		}
		JSONObject obj = new JSONObject();
		obj.put(CONTACTS_ID, contacts.getOrderContactsId());
		if(contacts.getUserName() != null){
			obj.put(CONTACTS_USER_NAME, contacts.getUserName());
		}
		if(contacts.getUserAddress() != null){
			obj.put(CONTACTS_USER_ADDRESS, contacts.getUserAddress());
		}
		if(contacts.getUserPhone1() != null){
			obj.put(CONTACTS_USER_PHONE_1, contacts.getUserPhone1());
		}
		if(contacts.getUserPhone2() != null){
			obj.put(CONTACTS_USER_PHONE_2, contacts.getUserPhone2());
		}
		if(contacts.getUserPhone3() != null){
			obj.put(CONTACTS_USER_PHONE_3, contacts.getUserPhone3());
		}
		return obj.toString();
	}
	
	public static OrderContacts json2Contacts(String json) throws JSONException {
		if (json==null) {
			return null;
		}
		OrderContacts contacts= new OrderContacts();
		JSONObject obj = new JSONObject(json);
		if(obj.has(CONTACTS_ID)){
			contacts.setOrderContactsId(obj.getInt(CONTACTS_ID));;
		}
		if(obj.has(CONTACTS_USER_NAME)){
			contacts.setUserName(obj.getString(CONTACTS_USER_NAME));
		}
		if(obj.has(CONTACTS_USER_ADDRESS)){
			contacts.setUserAddress(obj.getString(CONTACTS_USER_ADDRESS));
		}
		if(obj.has(CONTACTS_USER_PHONE_1)){
			contacts.setUserPhone1(obj.getString(CONTACTS_USER_PHONE_1));
		}
		if(obj.has(CONTACTS_USER_PHONE_2)){
			contacts.setUserPhone2(obj.getString(CONTACTS_USER_PHONE_2));
		}
		if(obj.has(CONTACTS_USER_PHONE_3)){
			contacts.setUserPhone3(obj.getString(CONTACTS_USER_PHONE_3));
		}
		return contacts;
	}
	
	/**
	 * 解析productCtrl
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static List<ProductConf> productCtrlJson2Obj(String json) throws JSONException{
		List<ProductConf> list = null;
		JSONArray jsonArr = new JSONArray(json);
		int len = jsonArr.length();
		if(len > 0){
			list = new ArrayList<ProductConf>(len);
			ProductConf conf = null;
			for(int i=0; i<len; i++){
				conf = dictJson2Obj(jsonArr.getJSONObject(i));
				list.add(conf);
				
				//将本级table赋值给上级控件，表示级联关系
				if(i>0){
					list.get(i-1).setNext(conf.getDictTable());
				}
			}
		}
		
		return list;
	}
	
	/**
	 * 解析unitCtrl
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static ProductConf unitCtrlJson2Obj(String json) throws JSONException{
		JSONObject jsonObject = new JSONObject(json);
		return dictJson2Obj(jsonObject);
	}
	
	private static ProductConf dictJson2Obj(JSONObject jsonObj) throws JSONException{
		
		ProductConf conf = new ProductConf();
		if(jsonObj.has("tab")){
			conf.setDictTable(jsonObj.getString("tab"));
		}
		if(jsonObj.has("ctrl")){
			conf.setDictDataId(jsonObj.getString("ctrl"));
		}
		if(jsonObj.has("name")){
			conf.setName(jsonObj.getString("name"));
		}
		if(jsonObj.has("cols")){
			conf.setDictCols(jsonObj.getString("cols"));
		}
		if(jsonObj.has("orderby")){
			conf.setDictOrderBy(jsonObj.getString("orderby"));
		}
		if(jsonObj.has("asc")){
			conf.setDictIsAsc(jsonObj.getString("asc"));
		}
		
		return conf;
	}
	
}
