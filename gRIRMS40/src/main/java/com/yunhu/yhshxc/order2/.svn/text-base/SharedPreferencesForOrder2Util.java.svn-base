package com.yunhu.yhshxc.order2;

import gcg.org.debug.JLog;

import java.util.List;

import org.json.JSONException;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.yunhu.yhshxc.order.bo.ProductConf;

public class SharedPreferencesForOrder2Util {

	private static Editor saveEditor;
	private static SharedPreferences saveInfo;
	private static SharedPreferencesForOrder2Util spUtil = new SharedPreferencesForOrder2Util();
	private static Context mContext;
	
	private final String ORDER_DETAIL = "order_detail"; //缓存订单明细
	private final String ORDER_SPEC = "order_spec"; //缓存订单说明
	
	private final String ENABLE_TIME = "enable_time"; //下订单可用时间, 
	private final String IS_PHONE_PRICE = "is_phone_price"; // 手机端价格的显示控制（1、显示、不可修改；2、显示、可修改；3、不显示；默认：2）
	private final String IS_PHONE_PRINT = "is_phone_print"; //是否需要手机打印（1、不要；2、要；默认：1）
	private final String IS_PRICE = "is_price"; //是否配送单上显示价格（1、不要；2、要；默认：2）
	private final String IS_STOCKS = "is_stocks"; //下订单时库存参考（1、不需要；2、需要；默认：1）
	private final String IS_PAY = "is_pay"; //是否需要支付（1、不要；2、要；默认：2）
	//private final String IS_AMOUNT = "is_amount"; //是否配送单上显示金额（1、不要；2、要；默认：2）
	private final String IS_PLACE_USER = "is_place_user"; //是否需要订货联系人（1、不要；2、要；默认：2）
	private final String IS_RECEIVE_USER = "is_receive_user"; //是否需要收货联系人（1、不要；2、要；默认：2）
	private final String PRICE_CTRL = "price_ctrl"; //价格控件ID（格式：模块ID|控件ID）
	private final String CODE_CTRL = "code_ctrl"; //二维码控件ID（格式：模块ID|控件ID）
	private final String UNIT_CTRL = "unit_ctrl"; //计量单位控件（格式：模块ID|控件ID）
	private final String PRODUCT_CTRL = "product_ctrl";// 产品名称控件（格式：第1级模块ID|控件ID,第2级模块ID|控件ID,第3级模块ID|控件ID）

	private final String ORDER2 = "order_2";
	
	private SharedPreferencesForOrder2Util() {
		
	}

	public static SharedPreferencesForOrder2Util getInstance(Context context) {
		if (saveInfo == null && context != null) {
			mContext = context.getApplicationContext();
			saveInfo = mContext.getSharedPreferences("order2", Context.MODE_PRIVATE);
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
	
//	/**
//	 * 保存订单明细JSON
//	 */
//	public void setOrderDetail(String json){
//		saveEditor.putString(ORDER_DETAIL,json);
//		saveEditor.commit();
//	}
//	
//	/**
//	 * 得到订单明细JSON
//	 */
//	public String getOrderDetail(){
//		return saveInfo.getString(ORDER_DETAIL, "");
//	}
	
//	/**
//	 * 保存订单说明
//	 */
//	public void setOrderSpec(String json){
//		saveEditor.putString(ORDER_SPEC,json);
//		saveEditor.commit();
//	}
//	
//	/**
//	 * 得到订单说明
//	 */
//	public String getOrderSpec(){
//		return saveInfo.getString(ORDER_SPEC, "");
//	}

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
	 * 是否需要手机打印（1、不要；2、要；默认：1）
	 */
	public void setIsPrice(int flag){
		saveEditor.putInt(IS_PRICE,flag);
		saveEditor.commit();
	}
	
	/**
	 * 是否需要手机打印（1、不要；2、要；默认：1）
	 */
	public int getIsPrice(){
		return saveInfo.getInt(IS_PRICE, 1);
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
	 * 是否需要支付（1、不要；2、要；默认：2）
	 */
	public void setIsPay(int flag){
		saveEditor.putInt(IS_PAY,flag);
		saveEditor.commit();
	}
	
	/**
	 * 是否需要支付（1、不要；2、要；默认：2）
	 */
	public int getIsPay(){
		return saveInfo.getInt(IS_PAY, 1);
	}
	
	/**
	 * 是否需要订货联系人（1、不要；2、要；默认：2）
	 */
	public void setIsPlaceUser(int flag){
		saveEditor.putInt(IS_PLACE_USER,flag);
		saveEditor.commit();
	}
	
	/**
	 * 是否需要订货联系人（1、不要；2、要；默认：2）
	 */
	public int getIsPlaceUser(){
		return saveInfo.getInt(IS_PLACE_USER, 2);
	}
	
	/**
	 * 是否需要收货联系人（1、不要；2、要；默认：2）
	 */
	public void setIsReceiveUser(int flag){
		saveEditor.putInt(IS_RECEIVE_USER,flag);
		saveEditor.commit();
	}
	
	/**
	 * 是否需要收货联系人（1、不要；2、要；默认：2）
	 */
	public int getIsReceiveUser(){
		return saveInfo.getInt(IS_RECEIVE_USER, 2);
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
	
	/**
	 * 计量单位控件（格式：模块ID|控件ID）
	 */
	public ProductConf getUnitCtrl(){
		String json = saveInfo.getString(UNIT_CTRL, "");
		ProductConf conf = null;
		try {
			conf = Order2Data.unitCtrlJson2Obj(json);
		} catch (JSONException e) {
			JLog.e(e);
		}
		return conf;
	}

	/**
	 * 产品名称控件（格式：第1级模块ID|控件ID,第2级模块ID|控件ID,第3级模块ID|控件ID）
	 */
	public void setProductCtrl(String productCtrl){
		saveEditor.putString(PRODUCT_CTRL,productCtrl);
		saveEditor.commit();
	}
	
	/**
	 * 产品名称控件（格式：第1级模块ID|控件ID,第2级模块ID|控件ID,第3级模块ID|控件ID）
	 */
	public List<ProductConf> getProductCtrl(){
		
		String json = saveInfo.getString(PRODUCT_CTRL, "");
		List<ProductConf> list = null;
		if(!TextUtils.isEmpty(json)){
			try {
				list = Order2Data.productCtrlJson2Obj(json);
			} catch (JSONException e) {
				JLog.e(e);
			}
		}
		
		return list;
	}
	
//	public void setOrder2(String json){
//		saveEditor.putString(ORDER2, json);
//		saveEditor.commit();
//	}
//	
//	public String getOrder2(){
//		return saveInfo.getString(ORDER2, "");
//	}
}
