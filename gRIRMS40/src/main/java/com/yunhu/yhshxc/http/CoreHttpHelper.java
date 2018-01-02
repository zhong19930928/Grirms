package com.yunhu.yhshxc.http;

import gcg.org.debug.JLog;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.yunhu.yhshxc.database.LocationPendingDB;
import com.yunhu.yhshxc.http.listener.ResponseListener;
import com.yunhu.yhshxc.location.bo.LocationInfo;
import com.yunhu.yhshxc.location.bo.LocationPending;
import com.yunhu.yhshxc.submitManager.bo.PendingRequestVO;
import com.yunhu.yhshxc.submitManager.bo.TablePending;
import com.yunhu.yhshxc.submitManager.core.SubmitWorkManager;
import com.yunhu.yhshxc.utility.UrlInfo;

/**
 * http请求封装
 * @author jishen
 *
 */
public class CoreHttpHelper {

	private CoreHttpQueryRequest coreHttpQueryRequest = null;//返回结果
	
	public CoreHttpHelper() {
	}
	
	public CoreHttpHelper(Context context) {
		coreHttpQueryRequest = new CoreHttpQueryRequest(context);
	}
	
	/**
	 * 设置监听
	 * @param listener  网络返回监听
	 */
	public void setResponseListener(ResponseListener listener) {
		coreHttpQueryRequest.setResponseListener(listener);
	}
	
	/**
	 * 查询
	 * @param url 请求url
	 * @return
	 */
	public int performQueryRequest(String url){
		int reqId = coreHttpQueryRequest.performQueryRequest(url);
		return reqId;
	}
	
	/**
	 * 取消监听
	 */
	public void releaseHttp(){
		coreHttpQueryRequest.releaseHttp();
	}
	
	/**
	 * 获取当前的监听状态是否开启
	 * @return true标识当前监听是打开的 false 标识已经关闭监听
	 */
	public boolean isOpenedHttp(){
		return coreHttpQueryRequest.isOpenedListen();
	}
	
	/**
	 * 后台提交数据
	 * @return
	 */
	public int performJustSubmit(Context context,PendingRequestVO vo) {
		SubmitWorkManager manager = SubmitWorkManager.getInstance(context.getApplicationContext());
		int reqId = manager.performJustSubmit(vo);
		manager.commit();
		return reqId;
	}
	
	/**
	 * 后台定位数据
	 * @return
	 */
	public void performLocationSubmit(Context context,LocationInfo location) {
		LocationPending loc = new LocationPending();
		loc.setType(TablePending.TYPE_LOATION);
		loc.setValue(toJson(location));
		new LocationPendingDB(context).insertLocation(loc);
		SubmitWorkManager.getInstance(context.getApplicationContext()).commit();
	}
	
	/**
	 * 守店定位数据
	 * @return
	 */
	public void performAttendanceLocationSubmit(Context context,LocationInfo location) {
		LocationPending loc = new LocationPending();
		loc.setType(TablePending.TYPE_ATTENDANCE_LOATION);
		loc.setValue(toJsonForAtte(location));
		new LocationPendingDB(context).insertLocation(loc);
		SubmitWorkManager.getInstance(context.getApplicationContext()).commit();
	}
	
	
	/**
	 * 将被动定位数据转成JSON数据
	 * @param list
	 * @return
	 */
	public String toJson(LocationInfo info){
		
		String json = null;
			try {
				if(info != null){
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("gisX", info.getLon());
					jsonObject.put("gisY", info.getLat());
					jsonObject.put("address", info.getAddr());
					jsonObject.put("locType", info.getLocationType());
					jsonObject.put("locationTime", info.getLocationTime());
					jsonObject.put("version", info.getVersion());
					jsonObject.put("action", info.getAction());
					jsonObject.put("status", info.getPosType());
					jsonObject.put("acc", info.getAcc());
					json = jsonObject.toString();
				}
			} catch (JSONException e) {
				json = null;
				JLog.e(e);
			}
		return json;
	}
	
	/**
	 * 将被动定位数据转成JSON数据
	 * @param list
	 * @return
	 */
	public String toJsonForAtte(LocationInfo info){
		
		String json = null;
			try {
				if(info != null){
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("lon", info.getLon());
					jsonObject.put("lat", info.getLat());
					jsonObject.put("address", info.getAddr());
					jsonObject.put("locType", info.getLocationType());
					jsonObject.put("checkTime", info.getLocationTime());
					jsonObject.put("version", info.getVersion());
					jsonObject.put("action", info.getAction());
					jsonObject.put("status", info.getPosType());
					jsonObject.put("acc", info.getAcc());
					jsonObject.put("isExp", info.getIsExp());
					jsonObject.put("remk", info.getRemark());
					json = jsonObject.toString();
				}
			} catch (JSONException e) {
				json = null;
				JLog.e(e);
			}
		return json;
	}
	
	/**
	 * 后台提交图片类型数据
	 * @return
	 */
	public int performImageUpload(Context context,PendingRequestVO vo) {
		SubmitWorkManager manager = SubmitWorkManager.getInstance(context.getApplicationContext());
		vo.setType(TablePending.TYPE_IMAGE);
		vo.setMethodType(SubmitWorkManager.HTTP_METHOD_POST);
		vo.setUrl(UrlInfo.urlUploadPhoto(context));
		int reqId = manager.performImageUpload(vo);
		manager.commit();
		return reqId;
	}
	
	/**
	 * 后台提交Log文件类型数据
	 * @return
	 */
	public int performLogUpload(Context context,PendingRequestVO vo) {
		SubmitWorkManager manager = SubmitWorkManager.getInstance(context.getApplicationContext());
		vo.setType(TablePending.TYPE_FILE);
		vo.setMethodType(SubmitWorkManager.HTTP_METHOD_POST);
		vo.setUrl(UrlInfo.urlUploadLog(context));
		int reqId = manager.performFileUpload(vo);
		manager.commit();
		return reqId;
	}
	
	/**
	 * 后台提交微信文件类型数据
	 * @return
	 */
	public int performWehatUpload(Context context,PendingRequestVO vo) {
		SubmitWorkManager manager = SubmitWorkManager.getInstance(context.getApplicationContext());
		vo.setType(TablePending.TYPE_FILE);
		vo.setMethodType(SubmitWorkManager.HTTP_METHOD_POST);
		vo.setUrl(UrlInfo.uploadHelpFileInfo(context));
		int reqId = manager.performFileUpload(vo);
		manager.commit();
		return reqId;
	}
}
