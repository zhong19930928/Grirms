package com.yunhu.yhshxc.activity.carSales.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesForCarSalesUtil {

	private static Editor saveEditor;
	private static SharedPreferences saveInfo;
	private static SharedPreferencesForCarSalesUtil spUtil = new SharedPreferencesForCarSalesUtil();
	private static Context mContext;
	
	private final String IS_PHONE_PRICE = "is_phone_price"; // 手机端价格的显示控制（1、显示、不可修改；2、显示、可修改；3、不显示；默认：2）
	private final String IS_PHONE_PRINT = "is_phone_print"; //是否需要手机打印（1、不要；2、要；默认：1）
	private final String PRICE_CTRL = "price_ctrl"; //价格控件ID（格式：模块ID|控件ID）
	private final String CODE_CTRL = "code_ctrl"; //二维码控件ID（格式：模块ID|控件ID）
	private final String UNIT_CTRL = "unit_ctrl"; //计量单位控件（格式：模块ID|控件ID）
	private final String IS_CarSales_USER = "is_carSales_user";//是否需要订货联系人 1不要 2要 默认是2
	private final String IS_PROMOTION = "is_promotion";//是否需要促销模块 1不要 2要 默认2
	
	private final String MIN_NUM = "min_num";//下单每个单品数量的最小值
	private final String MAX_NUM = "max_num";//下单每个单品数量的最大值
	
	private final String CARSALES_ORG_ID = "CarSales_org_id";//当前选择的客户的机构ID	
	private final String CARSALES_STORE_NAME = "CarSales_store_name";//当前选择的客户名称
	private final String CARSALES_STORE_LEVEL = "CarSales_store_level";//当前选择的客户层级
	private final String CARSALES_STORE_ID = "CarSales_store_id";//当前选择的客户
	private final String CARSALES_COUNT = "CarSales_count";//某天提交的订单数 日期_数 格式保存
	private final String CONTACT_ID = "contact_id";//当前选择的联系人ID
	private final String PHOTONAMEONE = "photo_name_one";//照片名字1
	private final String PHOTONAMETOW = "photo_name_two";//照片名字2
	
	private final String CARSALES_PRINT_COUNT = "CarSales_print_count";//订单打印次数
	private final String CARSALES_SEND_PRINT_COUNT = "CarSales_send_print_count";//送货订单打印次数
	private final String CARSALES_NO = "CarSales_send_print_count";//订单号
	private final String CARSALES_CHECK_COUNT = "ctrlCount";//修改是首次默认数量
	
	public final String LINK_MOD = "link_mod";//拓展模块数据Id
	public final String LINK_NAME = "link_name";//拓展模块名称

	private final String ENABLE_TIME = "enable_time"; //下订单可用时间, 

	private final String TIMESTAMP = "timestamp";//订单拓展模块submit的时间戳
	
	private final String IS_SALES = "is_sales";//是否需要打印销售单，1不需要 2需要 默认是2
	private final String IS_LODING = "is_loding";//是否需要打印装车单，1不需要 2需要 默认是2
	private final String IS_TRUCK = "is_truck";//是否需要打印卸车单，1不需要 2需要 默认是2
	private final String IS_RECEIVABLES = "is_receivables";//是否需要打印收款单，1不需要 2需要 默认是2
	private final String IS_RETURN = "is_return";//是否需要打印退货单，1不需要 2需要 默认是2
	private final String IS_SALES_PHOTO = "is_sales_photo";//是否需要销售拍照，1不需要 2需要 默认是2
	private final String IS_LODING_PHOTO = "is_loding_photo";//是否需要装车拍照，1不需要 2需要 默认是2
	private final String IS_TRUCK_PHOTO = "is_truck_photo";//是否需要卸车拍照，1不需要 2需要 默认是2
	private final String IS_RECEIVABLES_PHOTO = "is_receivables_photo";//是否需要收款拍照，1不需要 2需要 默认是2
	private final String IS_RETURN_PHOTO = "is_return_photo";//是否需要退货拍照，1不需要 2需要 默认是2
	private final String CAR_ID = "car_id";
	private final String CAR_NO = "car_no";//车牌号
	private final String CAR_DPM = "car_dpm";//车辆所属机构
	private final String STORC_CTRL = "stock_ctrl";//库存控制
	
	
	private final String IS_HAS_COST = "is_has_cost";//是否需要费用报销，0不需要 1需要 默认是0
	private final String IS_HAS_STOCK = "is_has_stock";//是否需要库存盘点，0不需要 1需要 默认是0
	private final String IS_HAS_SALES = "is_has_sales";//是否需要卖货和收款，0不需要 1需要 默认是0
	private final String IS_HAS_FILL = "is_has_fill";//是否需要补货申请，0不需要 1需要 默认是0
	private final String IS_HAS_LODING = "is_has_loding";//是否需要装车和装车记录，0不需要 1需要 默认是0
	private final String IS_HAS_TRUCK = "is_has_truck";//是否需要卸车和卸车记录，0不需要 1需要 默认是0
	private final String IS_HAS_RETURN = "is_has_return";//是否需要退货，0不需要 1需要 默认是0
	
	private SharedPreferencesForCarSalesUtil() {}

	public static SharedPreferencesForCarSalesUtil getInstance(Context context) {
		if (saveInfo == null && context != null) {
			mContext = context.getApplicationContext();
			saveInfo = mContext.getSharedPreferences("car_sales", Context.MODE_PRIVATE);
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
	
	
	
	public void setIsCarSalesUser(int value){
		saveEditor.putInt(IS_CarSales_USER, value);
		saveEditor.commit();
	}
	
	public int getIsCarSalesUser(){
		return saveInfo.getInt(IS_CarSales_USER, 2);
	}
	
	public void setIsPromotion(int value){
		saveEditor.putInt(IS_PROMOTION, value);
		saveEditor.commit();
	}
	
	public int getIsPromotion(){
		return saveInfo.getInt(IS_PROMOTION, 2);
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
	public void setTuiKuan(String value){
		saveEditor.putString("tuikuan",value);
		saveEditor.commit();
	}
	public String getTuiKuan(){
		return saveInfo.getString("tuikuan", "0");
	}
	public void setCarSalesNo(String value){
		saveEditor.putString(CARSALES_NO, value);
		saveEditor.commit();
	}
	public String getCarSalesNo(){
		return saveInfo.getString(CARSALES_NO, "");
	}
	public void setCarSalesStoreLevel(int level){
		saveEditor.putInt(CARSALES_STORE_LEVEL, level);
		saveEditor.commit();
	}
	public int getCarSalesStoreLevel(){
		return saveInfo.getInt(CARSALES_STORE_LEVEL, 0);
	}
	public void setStoreId(String storeId){
		saveEditor.putString(CARSALES_STORE_ID, storeId);
		saveEditor.commit();
	}
	public String getStoreId(){
		return saveInfo.getString(CARSALES_STORE_ID, "");
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
	public  void setIsStatus(String status){
		saveEditor.putString("status", status);
		saveEditor.commit();
	}
	public String getStatus(){
		return saveInfo.getString("status", "0");
	}
	public void clearLeaveMeassage(){
		clear("shoukuan");
		clear("leavemessage");
		clear("jianmian");
		clear("status");
		clear(CARSALES_STORE_ID);
		clear(CARSALES_ORG_ID);
		clear(CARSALES_STORE_NAME);
		clear(CARSALES_STORE_LEVEL);
		clear(CONTACT_ID);
		clear(CARSALES_PRINT_COUNT);
		clear(CARSALES_SEND_PRINT_COUNT);
		clear(PHOTONAMEONE);
		clear(PHOTONAMETOW);
		clear(TIMESTAMP);
	}
	
	public void clearSubmitMeassage(){
		clear("shoukuan");
		clear("leavemessage");
		clear("jianmian");
		clear("status");
		clear(CONTACT_ID);
		clear(CARSALES_SEND_PRINT_COUNT);
		clear(PHOTONAMEONE);
		clear(PHOTONAMETOW);
		clear(TIMESTAMP);
	}
	public void clearReturnSubmitMeassage(){
		clear("tuikuan");
		clear("leavemessage");
		clear(CONTACT_ID);
		clear(CARSALES_SEND_PRINT_COUNT);
		clear(PHOTONAMEONE);
		clear(PHOTONAMETOW);
		clear(TIMESTAMP);
		clear(CARSALES_NO);
	}
	public void clearPhotoName(){
		clear(CARSALES_PRINT_COUNT);
		clear(PHOTONAMEONE);
		clear(PHOTONAMETOW);
	}
	public void clearStockCtrl(){
		clear(STORC_CTRL);
	}
	public void clearCarSalesCheckCount(){
		clear(CARSALES_CHECK_COUNT);
	}
	public void setCarSalesOrgId(String orgId){
		saveEditor.putString(CARSALES_ORG_ID, orgId);
		saveEditor.commit();
	}
	
	public String getCarSalesOrgId(){
		return saveInfo.getString(CARSALES_ORG_ID, "");
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
	
	public String getCarSalesCount(){
		return saveInfo.getString(CARSALES_COUNT,"");
	}
	
	public void setCarSalesCount(String str){
		saveEditor.putString(CARSALES_COUNT, str);
		saveEditor.commit();
	}
	public int getCarSalesContactId(){
		return saveInfo.getInt(CONTACT_ID, 0);
	}
	
	public void setCarSalesContactId(int str){
		saveEditor.putInt(CONTACT_ID, str);
		saveEditor.commit();
	}
	public int getCarSalesPrintCount(){
		return saveInfo.getInt(CARSALES_PRINT_COUNT, 0);
	}
	
	public void setCarSalesPrintCount(int count){
		saveEditor.putInt(CARSALES_PRINT_COUNT, count);
		saveEditor.commit();
	}
	public int getCarSalesSendPrintCount(){
		return saveInfo.getInt(CARSALES_SEND_PRINT_COUNT, 0);
	}
	
	public void setCarSalesSendPrintCount(int count){
		saveEditor.putInt(CARSALES_SEND_PRINT_COUNT, count);
		saveEditor.commit();
	}
	public void setListCtrlCount(String value){
		saveEditor.putString(CARSALES_CHECK_COUNT, value);
		saveEditor.commit();
	}
	public String getListCtrlCount(){
		return saveInfo.getString(CARSALES_CHECK_COUNT, "");
	}
	public String getCarSalesStoreName(){
		return saveInfo.getString(CARSALES_STORE_NAME, "");
	}
	
	public void setCarSalesStoreName(String name){
		saveEditor.putString(CARSALES_STORE_NAME, name);
		saveEditor.commit();
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
	
	public void setCarSalesTimestamp(String timestamp){
		saveEditor.putString(TIMESTAMP, timestamp);
		saveEditor.commit();
	}
	
	public String getCarSalesTimestamp(){
		return saveInfo.getString(TIMESTAMP, "");
	}
	
	public void setIsSales(String timestamp){
		saveEditor.putString(IS_SALES, timestamp);
		saveEditor.commit();
	}
	
	public String getIsSales(){
		return saveInfo.getString(IS_SALES, "2");
	}
	
	public void setIsLoding(String timestamp){
		saveEditor.putString(IS_LODING, timestamp);
		saveEditor.commit();
	}
	
	public String getIsLoding(){
		return saveInfo.getString(IS_LODING, "2");
	}
	
	public void setIsTruck(String timestamp){
		saveEditor.putString(IS_TRUCK, timestamp);
		saveEditor.commit();
	}
	
	public String getIsTruck(){
		return saveInfo.getString(IS_TRUCK, "2");
	}
	public void setIsReceivables(String timestamp){
		saveEditor.putString(IS_RECEIVABLES, timestamp);
		saveEditor.commit();
	}
	
	public String getIsReceivables(){
		return saveInfo.getString(IS_RECEIVABLES, "2");
	}
	
	public void setIsReturn(String timestamp){
		saveEditor.putString(IS_RETURN, timestamp);
		saveEditor.commit();
	}
	
	public String getIsReturn(){
		return saveInfo.getString(IS_RETURN, "2");
	}
	
	public void setIsSalesPhoto(String timestamp){
		saveEditor.putString(IS_SALES_PHOTO, timestamp);
		saveEditor.commit();
	}
	
	public String getIsSalesPhoto(){
		return saveInfo.getString(IS_SALES_PHOTO, "2");
	}
	
	public void setIsLodingPhoto(String timestamp){
		saveEditor.putString(IS_LODING_PHOTO, timestamp);
		saveEditor.commit();
	}
	
	public String getIsLodingPhoto(){
		return saveInfo.getString(IS_LODING_PHOTO, "2");
	}
	
	public void setIsTruckPhoto(String timestamp){
		saveEditor.putString(IS_TRUCK_PHOTO, timestamp);
		saveEditor.commit();
	}
	
	public String getIsTruckPhoto(){
		return saveInfo.getString(IS_TRUCK_PHOTO, "2");
	}
	
	public void setIsReceivablesPhoto(String timestamp){
		saveEditor.putString(IS_RECEIVABLES_PHOTO, timestamp);
		saveEditor.commit();
	}
	
	public String getIsReceivablesPhoto(){
		return saveInfo.getString(IS_RECEIVABLES_PHOTO, "2");
	}
	
	public void setIsReturnPhoto(String timestamp){
		saveEditor.putString(IS_RETURN_PHOTO, timestamp);
		saveEditor.commit();
	}
	
	public String getIsReturnPhoto(){
		return saveInfo.getString(IS_RETURN_PHOTO, "2");
	}
	
	public void setCarId(int  carId){
		saveEditor.putInt(CAR_ID, carId);
		saveEditor.commit();
	}
	
	public int getCarId(){
		return saveInfo.getInt(CAR_ID, 0);
	}
	
	public void setCarNo(String timestamp){
		saveEditor.putString(CAR_NO, timestamp);
		saveEditor.commit();
	}
	
	public String getCarNo(){
		return saveInfo.getString(CAR_NO, "");
	}
	
	public void setCarDpm(String timestamp){
		saveEditor.putString(CAR_DPM, timestamp);
		saveEditor.commit();
	}
	
	public String getCarDpm(){
		return saveInfo.getString(CAR_DPM, "");
	}
	
	public void setStorcCtrl(String timestamp){
		saveEditor.putString(STORC_CTRL, timestamp);
		saveEditor.commit();
	}
	
	public String getStorcCtrl(){
		return saveInfo.getString(STORC_CTRL, "");
	}
	
	public void setIsHasCost(String value){
		saveEditor.putString(IS_HAS_COST, value);
		saveEditor.commit();
	}
	
	public String getIsHasCost(){
		return saveInfo.getString(IS_HAS_COST, "0");
	}
	
	public void setIsHasStock(String value){
		saveEditor.putString(IS_HAS_STOCK, value);
		saveEditor.commit();
	}
	
	public String getIsHasStock(){
		return saveInfo.getString(IS_HAS_STOCK, "0");
	}
	public void setIsHasSales(String value){
		saveEditor.putString(IS_HAS_SALES, value);
		saveEditor.commit();
	}
	
	public String getIsHasSales(){
		return saveInfo.getString(IS_HAS_SALES, "0");
	}
	public void setIsHasFill(String value){
		saveEditor.putString(IS_HAS_FILL, value);
		saveEditor.commit();
	}
	
	public String getIsHasFill(){
		return saveInfo.getString(IS_HAS_FILL, "0");
	}
	public void setIsHasLoding(String value){
		saveEditor.putString(IS_HAS_LODING, value);
		saveEditor.commit();
	}
	
	public String getIsHasLoding(){
		return saveInfo.getString(IS_HAS_LODING, "0");
	}
	public void setIsHasTruck(String value){
		saveEditor.putString(IS_HAS_TRUCK, value);
		saveEditor.commit();
	}
	
	public String getIsHasTruck(){
		return saveInfo.getString(IS_HAS_TRUCK, "0");
	}
	public void setIsHasReturn(String value){
		saveEditor.putString(IS_HAS_RETURN, value);
		saveEditor.commit();
	}
	
	public String getIsHasReturn(){
		return saveInfo.getString(IS_HAS_RETURN, "0");
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
	
}
