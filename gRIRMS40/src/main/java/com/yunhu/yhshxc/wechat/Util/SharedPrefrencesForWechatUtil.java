package com.yunhu.yhshxc.wechat.Util;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class SharedPrefrencesForWechatUtil {
	private static Editor saveEditor;
	private static SharedPreferences saveInfo;
	private static SharedPrefrencesForWechatUtil spUtil = new SharedPrefrencesForWechatUtil();
	private static Context mContext;
	private final String NI_CHENG= "ni_cheng";//个人昵称
	private final String QIAN_MING = "qian_ming";//个人签名
	private final String HEARDER_NAME ="header_name";//头像名
	private final String BUM_MEN="bu_men";//部门名称
	private final String ZHI_WEI ="zhi_wei";//职位名称
	private final String SRC_NAME ="src_name";//
	private final String ISCHANEDHEARDER = "isChangedHeader";//1更换，0未更换
	private SharedPrefrencesForWechatUtil() {}
	
	private final String IS_PHONE = "isPhone";// 手机端权限（0：无、1：有）
	private final String IS_NOTICE  = "isNotice";// 是否可发布通知（0：不可、1：可以
	private final String IS_GROUP  = "isGroup";// 是否可创建群（0：不可、1：可以）
	private final String IS_TOPIC  = "isTopic";// 是否可创话题（0：不可、1：可以）
	private final String IS_PRIVATE  = "isPrivate";// 是否可私聊（0：不可、1：可以）
	private final String GROUP_AUTH  = "groupAuth";// 群成员权限（1：全部、2：本部门（包含下级部门）、3：本部门（不含下级部门））
	private final String NOTICE_RANGE  = "noticeRange";// 通知范围（1：全部、2：本部门（包含下级部门）、3：本部门（不含下级部门））
	private final String TOPIC_RANGE  = "topicRange";// 话题权限（1：全部、2：本部门（包含下级部门）、3：本部门（不含下级部门））
	private final String PRIVATE_RANGE  = "privateRange";// 私聊范围（1：全部、2：本部门（包含下级部门）、3：本部门（不含下级部门））
	
	public void setBuMen(String values){
		saveEditor.putString(BUM_MEN, values);
		saveEditor.commit();
	}
	
	public String getBuMen(){
		return saveInfo.getString(BUM_MEN, "");
	}
	
	public void setZhiWei(String values){
		saveEditor.putString(ZHI_WEI, values);
		saveEditor.commit();
	}
	
	public String getZhiWei(){
		return saveInfo.getString(ZHI_WEI, "");
	}
	
	public void setIsChangedReader(String values){
		saveEditor.putString("ISCHANEDHEARDER", values);
		saveEditor.commit();
	}
	
	public String getIsChangedReader(){
		return saveInfo.getString("ISCHANEDHEARDER", "1");
	}
	
	public void setPrivateRange(String value){
		saveEditor.putString(PRIVATE_RANGE, value);
		saveEditor.commit();
	}
	
	public String getPrivateRange(){
		return saveInfo.getString(PRIVATE_RANGE, "1");
	}
	
	
	public void setTopicRange(String value){
		saveEditor.putString(TOPIC_RANGE, value);
		saveEditor.commit();
	}
	
	public String getTopicRange(){
		return saveInfo.getString(TOPIC_RANGE, "1");
	}
	
	
	public void setNoticeRange(String value){
		saveEditor.putString(NOTICE_RANGE, value);
		saveEditor.commit();
	}
	
	public String getNoticeRange(){
		return saveInfo.getString(NOTICE_RANGE, "1");
	}
	
	
	
	public void setGroupAuth(String value){
		saveEditor.putString(GROUP_AUTH, value);
		saveEditor.commit();
	}
	
	public String getGroupAuth(){
		return saveInfo.getString(GROUP_AUTH, "1");
	}
	
	
	public void setIsPrivate(String value){
		saveEditor.putString(IS_PRIVATE, value);
		saveEditor.commit();
	}
	
	public String getIsPrivate(){
		return saveInfo.getString(IS_PRIVATE, "1");
	}
	
	
	
	
	public void setIsTopic(String value){
		saveEditor.putString(IS_TOPIC, value);
		saveEditor.commit();
	}
	
	public String getIsTopic(){
		return saveInfo.getString(IS_TOPIC, "1");
	}
	
	
	public void setUserHedaImg(String userId,String url){
		saveEditor.putString(userId, url);
		saveEditor.commit();
	}
	
	public String getUserHeadImg(String userId){
		return saveInfo.getString(userId, "");
	}

	public void setIsGroup(String value){
		saveEditor.putString(IS_GROUP, value);
		saveEditor.commit();
	}
	
	public String getIsGroup(){
		return saveInfo.getString(IS_GROUP, "1");
	}
	
	
	public void setisNotice(String value){
		saveEditor.putString(IS_NOTICE, value);
		saveEditor.commit();
	}
	
	public String getIsNotice(){
		return saveInfo.getString(IS_NOTICE, "1");
	}
	
	
	public void setIsPhone(String value){
		saveEditor.putString(IS_PHONE, value);
		saveEditor.commit();
	}
	
	public String getIsPhone(){
		return saveInfo.getString(IS_PHONE, "0");
	}
	

	public static SharedPrefrencesForWechatUtil getInstance(Context context) {
		if (saveInfo == null && context != null) {
			mContext = context.getApplicationContext();
			saveInfo = mContext.getSharedPreferences("wechat", Context.MODE_PRIVATE);
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
	public void setNiCheng(String value){
		saveEditor.putString(NI_CHENG, value);
		saveEditor.commit();
	}
	
	public String getNicheng(){
		return saveInfo.getString(NI_CHENG, "");
	}
	
	public void setQianMing(String value){
		saveEditor.putString(QIAN_MING, value);
		saveEditor.commit();
	}
	
	public String getQianMing(){
		return saveInfo.getString(QIAN_MING, "");
	}
	
	public void setHeaderName(String value){
		saveEditor.putString(HEARDER_NAME, value);
		saveEditor.commit();
	}
	
	public String getHeaderName(){
		return saveInfo.getString(HEARDER_NAME, "");
	}
	
	public void setFileName(String key,String value){
		saveEditor.putString(key, value);
		saveEditor.commit();
	}
	
	public String getGetFileName(String key){
		return saveInfo.getString(key, "");
	}
	
	
}
