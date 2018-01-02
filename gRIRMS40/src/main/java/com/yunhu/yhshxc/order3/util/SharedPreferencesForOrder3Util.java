package com.yunhu.yhshxc.order3.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesForOrder3Util {

	private static Editor saveEditor;
	private static SharedPreferences saveInfo;
	private static SharedPreferencesForOrder3Util spUtil = new SharedPreferencesForOrder3Util();
	private static Context mContext;
	
	private final String IS_PHONE_PRICE = "is_phone_price"; // 手机端价格的显示控制（1、显示、不可修改；2、显示、可修改；3、不显示；默认：2）
	private final String IS_PHONE_PRINT = "is_phone_print"; //是否需要手机打印（1、不要；2、要；默认：1）
	private final String IS_STOCKS = "is_stocks"; //下订单时库存参考（1、不需要；2、需要；默认：1）
	private final String PRICE_CTRL = "price_ctrl"; //价格控件ID（格式：模块ID|控件ID）
	private final String CODE_CTRL = "code_ctrl"; //二维码控件ID（格式：模块ID|控件ID）
	private final String UNIT_CTRL = "unit_ctrl"; //计量单位控件（格式：模块ID|控件ID）
	private final String IS_DATE_TYPE = "is_date_type";//下订单可用日期（1.固定时间(周一 ~ 周日 可选) 2.自定义 默认是1）
	private final String IS_ORDER_USER = "is_order_user";//是否需要订货联系人 1不要 2要 默认是2
	private final String IS_AMOUNT = "is_amount";//配送单上是否显示金额 1不要 2要 默认是2
	private final String IS_AUDIT_UPDATE = "is_audit_update";//审批后是否可修改 1不可 2 可 默认是2
	private final String IS_PROMOTION = "is_promotion";//是否需要促销模块 1不要 2要 默认2
	private final String IS_DIST_SALES = "is_dist_sales";//是否需要分销管理 1不要 2要 默认2
	
	private final String ORDER3_ORG_ID = "order3_org_id";//当前选择的客户的机构ID	
	private final String ORDER3_STORE_NAME = "order3_store_name";//当前选择的客户名称
	private final String ORDER3_STORE_LEVEL = "order3_store_level";//当前选择的客户层级
	private final String ORDER3_STORE_ID = "order3_store_id";//当前选择的客户
	private final String ORDER3_COUNT = "order3_count";//某天提交的订单数 日期_数 格式保存
	private final String CONTACT_ID = "contact_id";//当前选择的联系人ID
	private final String PHOTONAMEONE = "photo_name_one";//照片名字1
	private final String PHOTONAMETOW = "photo_name_two";//照片名字2
	
	private final String ORDER_PRINT_COUNT = "order_print_count";//订单打印次数
	private final String ORDER_SEND_PRINT_COUNT = "order_send_print_count";//送货订单打印次数
	
	private final String MIN_NUM = "min_num";//下单每个单品数量的最小值
	private final String MAX_NUM = "max_num";//下单每个单品数量的最大值
	
	public final String LINK_MOD = "link_mod";//拓展模块数据Id
	public final String LINK_NAME = "link_name";//拓展模块名称

	private final String ENABLE_TIME = "unable_time"; //下订单可用时间, 
	private final String SEND_ENABLE_TIME = "send_enable_time"; //送货可用时间, 

	private final String TIMESTAMP = "timestamp";//订单拓展模块submit的时间戳
	
	private final String UNABLE_WEEK = "unable_week";//每周可用 1可用0不可用
	private final String UNABLE_DATE = "unable_date";//每月可用日期
	private final String IS_INPUT = "is_input"; // 是否必须填报，0：不必须（默认）、1：必须
	
	private SharedPreferencesForOrder3Util() {}

	public static SharedPreferencesForOrder3Util getInstance(Context context) {
		if (saveInfo == null && context != null) {
			mContext = context.getApplicationContext();
			saveInfo = mContext.getSharedPreferences("order_3", Context.MODE_PRIVATE);
			saveEditor = saveInfo.edit();
		}
		return spUtil;
	}
	
	public void clearAll() {
		saveEditor.clear();
		saveEditor.commit();
	}

	public void clear(String key) {
		saveEditor.remove(key);
		saveEditor.commit();
	}
	
	
	public void setIsDateType(int dateType){
		saveEditor.putInt(IS_DATE_TYPE, dateType);
		saveEditor.commit();
	}
	
	public int getIsDateType(){
		return saveInfo.getInt(IS_DATE_TYPE, 1);
	}
	
	public void setIsOrderUser(int value){
		saveEditor.putInt(IS_ORDER_USER, value);
		saveEditor.commit();
	}
	
	public int getIsOrderUser(){
		return saveInfo.getInt(IS_ORDER_USER, 2);
	}
	
	public void setIsAmount(int value){
		saveEditor.putInt(IS_AMOUNT, value);
		saveEditor.commit();
	}
	
	public int getIsAmount(){
		return saveInfo.getInt(IS_AMOUNT, 2);
	}
	
	public void setIsAuditUpdate(int value){
		saveEditor.putInt(IS_AUDIT_UPDATE, value);
		saveEditor.commit();
	}
	
	public int getIsAuditUpdate(){
		return saveInfo.getInt(IS_AUDIT_UPDATE, 2);
	}
	
	public void setIsPromotion(int value){
		saveEditor.putInt(IS_PROMOTION, value);
		saveEditor.commit();
	}
	
	public int getIsPromotion(){
		return saveInfo.getInt(IS_PROMOTION, 2);
	}
	
	public void setIsdistSales(int value){
		saveEditor.putInt(IS_DIST_SALES, value);
		saveEditor.commit();
	}
	
	public int getIsdistSales(){
		return saveInfo.getInt(IS_DIST_SALES, 2);
	}
	
	public void setShouKuan(String value){
		saveEditor.putString("shoukuan", value);
		saveEditor.commit();
	}
	
	public String getShouKuan(){
		return saveInfo.getString("shoukuan", "0");
	}
	public void setLeaveMessage(String value){
		saveEditor.putString("leavemessage", value);
		saveEditor.commit();
	}
	
	public String getLeaveMessage(){
		return saveInfo.getString("leavemessage", "");
	}
	public void setJianMian(String value){
		saveEditor.putString("jianmian", value);
		saveEditor.commit();
	}
	
	public String getJianMian(){
		return saveInfo.getString("jianmian", "0");
	}
	public void setOrder3StoreLevel(int level){
		saveEditor.putInt(ORDER3_STORE_LEVEL, level);
		saveEditor.commit();
	}
	public int getOrder3StoreLevel(){
		return saveInfo.getInt(ORDER3_STORE_LEVEL, 0);
	}
	public void setStoreId(String storeId){
		saveEditor.putString(ORDER3_STORE_ID, storeId);
		saveEditor.commit();
	}
	public String getStoreId(){
		return saveInfo.getString(ORDER3_STORE_ID, "");
	}
	public void setPhotoNameOne(String name){
		saveEditor.putString(PHOTONAMEONE, name);
		saveEditor.commit();
	}
	public String getPhotoNameOne(){
		return saveInfo.getString(PHOTONAMEONE, "");
	}
	public void setPhotoNameTwo(String name){
		saveEditor.putString(PHOTONAMETOW, name);
		saveEditor.commit();
	}
	public String getPhotoNameTwo(){
		return saveInfo.getString(PHOTONAMETOW, "");
	}
	public void clearLeaveMeassage(){
		clear("shoukuan");
		clear("leavemessage");
		clear("jianmian");
		clear(ORDER3_STORE_ID);
		clear(ORDER3_ORG_ID);
		clear(ORDER3_STORE_NAME);
		clear(ORDER3_STORE_LEVEL);
		clear(CONTACT_ID);
		clear(ORDER_PRINT_COUNT);
		clear(ORDER_SEND_PRINT_COUNT);
		clear(PHOTONAMEONE);
		clear(PHOTONAMETOW);
		clear(TIMESTAMP);
	}
	
	public void clearSubmitMeassage(){
		clear("shoukuan");
		clear("leavemessage");
		clear("jianmian");
		clear(CONTACT_ID);
		clear(ORDER_SEND_PRINT_COUNT);
		clear(PHOTONAMEONE);
		clear(PHOTONAMETOW);
		clear(TIMESTAMP);
	}

	public void setOrder3OrgId(String orgId){
		saveEditor.putString(ORDER3_ORG_ID, orgId);
		saveEditor.commit();
	}
	
	public String getOrder3OrgId(){
		return saveInfo.getString(ORDER3_ORG_ID, "");
	}
	
	/**
	 * 手机端价格的显示控制（1、显示、不可修改；2、显示、可修改；3、不显示；默认：2）
	 */
	public void setIsPhonePrice(int flag){
		saveEditor.putInt(IS_PHONE_PRICE,flag);
		saveEditor.commit();
	}
	
	/**
	 * 手机端价格的显示控制（1、显示、不可修改；2、显示、可修改；3、不显示；默认：2）
	 */
	public int getIsPhonePrice(){
		return saveInfo.getInt(IS_PHONE_PRICE, 2);
	}
	
	/**
	 * 是否需要手机打印（1、不要；2、要；默认：1）
	 */
	public void setIsPhonePrint(int flag){
		saveEditor.putInt(IS_PHONE_PRINT,flag);
		saveEditor.commit();
	}
	
	/**
	 * 是否需要手机打印（1、不要；2、要；默认：1）
	 */
	public int getIsPhonePrint(){
		return saveInfo.getInt(IS_PHONE_PRINT, 1);
	}

	
	/**
	 * 下订单时库存参考（1、不需要；2、需要；默认：1）
	 */
	public void setIsStocks(int flag){
		saveEditor.putInt(IS_STOCKS,flag);
		saveEditor.commit();
	}
	
	/**
	 * 下订单时库存参考（1、不需要；2、需要；默认：1）
	 */
	public int getIsStocks(){
		return saveInfo.getInt(IS_STOCKS, 1);
	}


	/**
	 * 价格控件ID（格式：模块ID|控件ID）
	 */
	public void setPriceCtrl(String priceCtrl){
		saveEditor.putString(PRICE_CTRL,priceCtrl);
		saveEditor.commit();
	}
	
	/**
	 * 价格控件ID（格式：模块ID|控件ID）
	 */
	public String getPriceCtrl(){
		return saveInfo.getString(PRICE_CTRL, "");
	}
	
	/**
	 * 二维码控件ID（格式：模块ID|控件ID）
	 */
	public void setCodeCtrl(String codeCtrl){
		saveEditor.putString(CODE_CTRL,codeCtrl);
		saveEditor.commit();
	}
	
	/**
	 * 二维码控件ID（格式：模块ID|控件ID）
	 */
	public String getCodeCtrl(){
		return saveInfo.getString(CODE_CTRL, "");
	}
	
	/**
	 * 计量单位控件（格式：模块ID|控件ID）
	 */
	public void setUnitCtrl(String unitCtrl){
		saveEditor.putString(UNIT_CTRL,unitCtrl);
		saveEditor.commit();
	}
	
	public String getOrder3Count(){
		return saveInfo.getString(ORDER3_COUNT,"");
	}
	
	public void setOrder3Count(String str){
		saveEditor.putString(ORDER3_COUNT, str);
		saveEditor.commit();
	}
	public String getOrder3NumberCount(){
		return saveInfo.getString("order3_number_count", "");
	}
	public void setOrder3NumberCount(String str){
		saveEditor.putString("order3_number_count", str);
		saveEditor.commit();
	}
	public int getOrder3ContactId(){
		return saveInfo.getInt(CONTACT_ID, 0);
	}
	
	public void setOrder3ContactId(int str){
		saveEditor.putInt(CONTACT_ID, str);
		saveEditor.commit();
	}
	public int getOrderPrintCount(){
		return saveInfo.getInt(ORDER_PRINT_COUNT, 0);
	}
	
	public void setOrderPrintCount(int count){
		saveEditor.putInt(ORDER_PRINT_COUNT, count);
		saveEditor.commit();
	}
	public int getOrderSendPrintCount(){
		return saveInfo.getInt(ORDER_SEND_PRINT_COUNT, 0);
	}
	
	public void setOrderSendPrintCount(int count){
		saveEditor.putInt(ORDER_SEND_PRINT_COUNT, count);
		saveEditor.commit();
	}
	public String getOrder3StoreName(){
		return saveInfo.getString(ORDER3_STORE_NAME, "");
	}
	
	public void setOrder3StoreName(String name){
		saveEditor.putString(ORDER3_STORE_NAME, name);
		saveEditor.commit();
	}
	
	public void setMaxNum(String max){
		saveEditor.putString(MAX_NUM, max);
		saveEditor.commit();
	}
	
	public String getMaxNum(){
		return saveInfo.getString(MAX_NUM, "0");
	}
	
	public void setMinNum(String min){
		saveEditor.putString(MIN_NUM, min);
		saveEditor.commit();
	}
	
	public String getMinNum(){
		return saveInfo.getString(MIN_NUM, "0");
	}
	
	public void setLinkMod(int linkMod){
		saveEditor.putInt(LINK_MOD, linkMod);
		saveEditor.commit();
	}
	
	public int getLinkMod(){
		return saveInfo.getInt(LINK_MOD, 0);
	}
	
	public void setLinkName(String linkName){
		saveEditor.putString(LINK_NAME, linkName);
		saveEditor.commit();
	}
	
	public String getLinkName(){
		return saveInfo.getString(LINK_NAME, "订单拓展");
	}
	
	/**
	 * 下订单可用时间, 
	 */
	public void setEnableTime(String enableTime){
		saveEditor.putString(ENABLE_TIME,enableTime);
		saveEditor.commit();
	}
	
	/**
	 * 下订单可用时间
	 */
	public String getEnableTime(){
		return saveInfo.getString(ENABLE_TIME, "");
	}
	
	/**
	 * 送货可用时间, 
	 */
	public void setSendEnableTime(String enableTime){
		saveEditor.putString(SEND_ENABLE_TIME,enableTime);
		saveEditor.commit();
	}
	
	/**
	 * 送货可用时间
	 */
	public String getSendEnableTime(){
		return saveInfo.getString(SEND_ENABLE_TIME, "");
	}
	
	public void setOrderTimestamp(String timestamp){
		saveEditor.putString(TIMESTAMP, timestamp);
		saveEditor.commit();
	}
	
	public String getOrderTimestamp(){
		return saveInfo.getString(TIMESTAMP, "");
	}
	
	/**
	 * 每周可用时间
	 * @param week
	 */
	public void setUnableWeek(String week){
		saveEditor.putString(UNABLE_WEEK, week);
		saveEditor.commit();
	}
	
	public String getUnableWeek(){
		return saveInfo.getString(UNABLE_WEEK, "");
	}
	
	public void setUnablDate(String date){
		saveEditor.putString(UNABLE_DATE, date);
		saveEditor.commit();
	}
	
	public String getUnableDate(){
		return saveInfo.getString(UNABLE_DATE, "");
	}
	
	public void setIsInput(String value){
		saveEditor.putString(IS_INPUT, value);
		saveEditor.commit();
	}
	
	public String getIsInput(){
		return saveInfo.getString(IS_INPUT, "0");
	}
}
