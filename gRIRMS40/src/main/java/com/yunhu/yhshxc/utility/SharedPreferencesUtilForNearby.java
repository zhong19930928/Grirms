package com.yunhu.yhshxc.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.yunhu.yhshxc.R;

public class SharedPreferencesUtilForNearby {

	private static Editor saveEditor;

	private static SharedPreferences saveInfo;

	public final String NEARBY_MODULE_ID = "nearby_module_id";//模块ID
	public final String NEARBY_STORE_LIST_SQL = "nearby_store_list_sql";//取店面列表用SQL ID
	public final String NEARBY_STORE_DETAIL_SQL = "nearby_store_detail_sql";//取店面详细用SQL ID
	public final String NEARBY_DATA_STATUS = "nearby_data_status";//数据状态
	public final String NEARBY_IS_MDL = "nearby_is_mdl";//是否需要模板
	public final String NEARBY_IS_HISTRY_QRY = "nearby_is_histry_qry";//是否有历史查看
	public final String NEARBY_IS_HISTRY_UPD = "nearby_is_histry_upd";//是否有历史数据修改
	public final String NEARBY_IS_DATA_CPY = "nearby_is_data_cpy";//是否有数据复制
	public final String NEARBY_STORE_INFO = "nearby_store_info";//店面属性信息（store_info，1：查看、2：修改）
	public final String NEARBY_BTN_VISIT = "nearby_btn_visit";//拜访按钮名称
	public final String NEARBY_BTN_MDL = "nearby_btn_mdl";//数据模版按钮
	public final String NEARBY_BTN_HISTRY = "nearby_btn_histry";//历史数据按钮
	public final String NEARBY_BTN_STORE = "nearby_btn_store";//店面属性按钮
	public final String NEARBY_STYLE = "style";
	public final String NEARBY_SEARCH_INFO = "nearby_search_info";//最后一次查询条件
	public final String STORE_MODULE_ORGID="stroe_module_orgid";//机构店面ID
	public final String STORE_MENUID = "store_menuid";//新店上报menuId
	public final String STORE_MENU_TPYE ="store_menu_type";//新店上报menuType
	public final String STORE_MENU_NAME="store_menu_name";//新店上报menuName
	private static SharedPreferencesUtilForNearby spUtil = new SharedPreferencesUtilForNearby();
	private static Context mContext;

	public static SharedPreferencesUtilForNearby getInstance(Context context) {
		mContext = context;
		if (saveInfo == null && mContext != null) {
			saveInfo = mContext.getSharedPreferences("nearby", Context.MODE_PRIVATE);
			saveEditor = saveInfo.edit();
		}
		return spUtil;
	}

	private SharedPreferencesUtilForNearby() {
		if (mContext != null) {
			saveInfo = mContext.getSharedPreferences("nearby", Context.MODE_PRIVATE);
			saveEditor = saveInfo.edit();
		}
	}

	public void clearAll(){
		saveEditor.clear();
		saveEditor.commit();
	}
	
	
	public void saveNearbyBtnStore(String menuId,String value) {
		saveEditor.putString(NEARBY_BTN_STORE+"_"+menuId, value);
		saveEditor.commit();
	}
	
	public String getNearbyBtnStore(String moduleId){
		return saveInfo.getString(NEARBY_BTN_STORE+"_"+moduleId, PublicUtils.getResourceString(mContext,R.string.store_property));
	}
	
	
	public void saveNearbyBtnHistry(String menuId,String value) {
		saveEditor.putString(NEARBY_BTN_HISTRY+"_"+menuId, value);
		saveEditor.commit();
	}
	
	public String getNearbyBtnHistry(String menuId){
		return saveInfo.getString(NEARBY_BTN_HISTRY+"_"+menuId, PublicUtils.getResourceString(mContext,R.string.history_data));
	}
	
	
	public void saveNearbyBtnMdl(String menuId,String value) {
		saveEditor.putString(NEARBY_BTN_MDL+"_"+menuId, value);
		saveEditor.commit();
	}
	
	public String getNearbyBtnMdl(String menuId){
		return saveInfo.getString(NEARBY_BTN_MDL+"_"+menuId, PublicUtils.getResourceString(mContext,R.string.data_template));
	}
	
	public void saveNearbyBtnVisit(String menuId,String value) {
		saveEditor.putString(NEARBY_BTN_VISIT+"_"+menuId, value);
		saveEditor.commit();
	}
	
	public String getNearbyBtnVisit(String menuId){
		return saveInfo.getString(NEARBY_BTN_VISIT+"_"+menuId, PublicUtils.getResourceString(mContext, R.string.visit_store));
	}
	
	
	public void saveNearbyStoreInfo(String menuId,String value) {
		saveEditor.putString(NEARBY_STORE_INFO+"_"+menuId, value);
		saveEditor.commit();
	}
	
	public String getNearbyStoreInfo(String menuId){
		return saveInfo.getString(NEARBY_STORE_INFO+"_"+menuId, null);
	}
	
	
	public void saveNearbyIsDataCpy(String menuId,String value) {
		saveEditor.putString(NEARBY_IS_DATA_CPY+"_"+menuId, value);
		saveEditor.commit();
	}
	
	public String getNearbyIsDataCpy(String menuId){
		return saveInfo.getString(NEARBY_IS_DATA_CPY+"_"+menuId, null);
	}
	
	public void saveNearbyIsMdl(String menuId,String value) {
		saveEditor.putString(NEARBY_IS_MDL+"_"+menuId, value);
		saveEditor.commit();
	}
	
	public String getNearbyIsMdl(String menuId){
		return saveInfo.getString(NEARBY_IS_MDL+"_"+menuId, null);
	}
	
	
	public void saveNearbyDataStatus(String menuId,String value) {
		saveEditor.putString(NEARBY_DATA_STATUS+"_"+menuId, value);
		saveEditor.commit();
	}
	
	public String getNearbyDataStatus(String menuId){
		return saveInfo.getString(NEARBY_DATA_STATUS+"_"+menuId, "0");
	}
	
	
	public void saveNearbyStoreDetailSql(String menuId,String value) {
		saveEditor.putString(NEARBY_STORE_DETAIL_SQL+"_"+menuId, value);
		saveEditor.commit();
	}
	
	public String getNearbyStoreDetailSql(String menuId){
		return saveInfo.getString(NEARBY_STORE_DETAIL_SQL+"_"+menuId, null);
	}
	
	
	public void saveNearbyStoreListSql(String menuId,String value) {
		saveEditor.putString(NEARBY_STORE_LIST_SQL+"_"+menuId, value);
		saveEditor.commit();
	}
	
	public String getNearbyStoreListSql(String menuId){
		return saveInfo.getString(NEARBY_STORE_LIST_SQL+"_"+menuId, null);
	}
	
	
//	public void saveNearbyModuleId(String where) {
//		saveEditor.putString(NEARBY_MODULE_ID, where);
//		saveEditor.commit();
//	}
//	
//	public String getNearbyModuleId(){
//		return saveInfo.getString(NEARBY_MODULE_ID, null);
//	}
	public void saveNearbyStyle(String menuId,String value) {
		saveEditor.putString(NEARBY_STYLE+"_"+menuId, value);
		saveEditor.commit();
	}
	
	public String getNearbyStyle(String menuId){
		return saveInfo.getString(NEARBY_STYLE+"_"+menuId, null);
	}
	
//	public void saveNearbySearchInfo(String info){
//		saveEditor.putString(NEARBY_SEARCH_INFO, info);
//		saveEditor.commit();
//	}
//	public String getNearbySearchInfo(){
//		return saveInfo.getString(NEARBY_SEARCH_INFO, "");
//	}
	
	public void saveStoreOrgId(String value){
		saveEditor.putString(STORE_MODULE_ORGID, value);
		saveEditor.commit();
	}
	public String getStoreOrgId(){
		return saveInfo.getString(STORE_MODULE_ORGID, "");
	}
	public void saveStoreMenuName(String value){
		saveEditor.putString(STORE_MENU_NAME, value);
		saveEditor.commit();
	}
	public String getStoreMenuName(){
		return saveInfo.getString(STORE_MENU_NAME, "");
	}
	public void saveStoreMenuId(int value){
		saveEditor.putInt(STORE_MENUID, value);
		saveEditor.commit();
	}
	public int getStoreMenuId(){
		return saveInfo.getInt(STORE_MENUID, 0);
	}
	public void saveStoreMenuType(int value){
		saveEditor.putInt(STORE_MENU_TPYE, value);
		saveEditor.commit();
	}
	public int getStoreMenuType(){
		return saveInfo.getInt(STORE_MENU_TPYE, 0);
	}
	
	public void saveStoreInfoClear(){
		saveStoreMenuName("");
		saveStoreOrgId("");
		saveStoreMenuId(-1);
		saveStoreMenuType(-1);
	}
	
	
}
