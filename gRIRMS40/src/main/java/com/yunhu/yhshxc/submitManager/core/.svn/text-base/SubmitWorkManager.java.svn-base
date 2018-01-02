package com.yunhu.yhshxc.submitManager.core;

import gcg.org.debug.JLog;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.database.LocationPendingDB;
import com.yunhu.yhshxc.database.TablePendingDB;
import com.yunhu.yhshxc.location.bo.LocationPending;
import com.yunhu.yhshxc.submitManager.bo.CoreHttpPendingRequest;
import com.yunhu.yhshxc.submitManager.bo.PendingRequestVO;
import com.yunhu.yhshxc.submitManager.bo.TablePending;
import com.yunhu.yhshxc.utility.ApplicationHelper;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.UrlInfo;

/**
 * SubmitInBackstageService 服务管理类
 * 此类为单例
 * @author david.hou
 */
public class SubmitWorkManager{
	
	/**
	 * 系统提交Unicode时采用的编码为UTF-8
	 */
	protected final static String SYSTEM_ENCODING = "UTF-8";
	
	/**
	 * HTTP GET方法
	 */
	public static final int HTTP_METHOD_GET = 12001;
	/**
	 * HTTP POST方法
	 */
	public static final int HTTP_METHOD_POST = 16001;
	
	private Context context = null;
	
	private static SubmitWorkManager mInstance = null;
	
	public final static String FILE_KEY = "IMAGE";

	private TablePendingDB tpDB;
	
	private SubmitWorkManager(Context context) {
		this.context = context;
		tpDB = new TablePendingDB(context);
	}
	
	/**
	 * 获取单例对象
	 * @param context
	 * @return
	 */
	public static SubmitWorkManager getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new SubmitWorkManager(context);
		}
		return mInstance;
	}
	
	
	/**
	 * 获取正在提交的数据
	 * @return
	 */
	protected TablePending getSubmitting() {
		return tpDB.findSubmitingTablePending();
	}
	
	/**
	 * 是否存在准备提交的数据，
	 * 如果存在将准备数据改为正在提交状态
	 * 
	 * @return
	 */
	protected boolean isEmptyReady() {
	
		createLocationTablePending();//首先创建被动定位的TablePending
		
		TablePending tp = tpDB.findReadyTablePending();
		if(tp != null){
			tpDB.updateTablePendingStatusById(tp.getId(), TablePending.STATUS_SUBIMTTING);
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 被动定位提交数据在“isEmptyReady”中处理；
	 * 在每次调用“isEmptyReady”时，都要查看否有被动定位数据提交；
	 * 如果有就处理数据，准备提交；
	 * TablePending表中当且仅当只有一条记录，为的是定位数据提交的顺序不能变
	 */
	private void createLocationTablePending(){
		//如果TablePending中有被动定位提交数据，那么就等待提交;
		//否则，创建被动位提交数据
		if(tpDB.findReadyTablePending(TablePending.TYPE_LOATION) == null){
			createPendingRequestForBackstageLocation();
		}
		//如果TablePending中有考勤被动定位提交数据，那么就等待提交;
		//否则，创建被动位提交数据
		//if(tpDB.findReadyTablePending(TablePending.TYPE_ATTENDANCE_LOATION) == null){
			createPendingRequestForAttendanceLocation();
		//}
		
	}
	
	/**
	 * 创建被动定位的TablePending
	 * 如果LocationPending没有数据，则不需要创建
	 */
	private void createPendingRequestForBackstageLocation(){
		List<LocationPending> pendingList = new LocationPendingDB(context).findLocationByType(TablePending.TYPE_LOATION);
		if(pendingList.isEmpty()){
			return;
		}
		PendingRequestVO vo = new PendingRequestVO();
		vo.setType(TablePending.TYPE_LOATION);
		vo.setMethodType(SubmitWorkManager.HTTP_METHOD_POST);
		vo.setTitle(PublicUtils.getResourceString(context, R.string.submit_string2));
		vo.setContent(PublicUtils.getResourceString(context, R.string.submit_string1));
		vo.addParams("data", location2JSONArray(pendingList));
		vo.setUrl(UrlInfo.getUrlLocationInfo2(context));
		performJustSubmit(vo);
		removeLocationPending(pendingList);
	}
	
	/**
	 * 创建考勤被动定位的TablePending
	 * 如果LocationPending没有数据，则不需要创建
	 */
	private void createPendingRequestForAttendanceLocation(){
		//List<LocationPending> pendingList = new LocationPendingDB(context).findLocationByType(TablePending.TYPE_ATTENDANCE_LOATION);
		List<LocationPending> pendingList = new LocationPendingDB(context).findLocationByTypeAttendance(); //临时方法（仅查一条"limit 1")
		if(pendingList.isEmpty()){
			return;
		}
		PendingRequestVO vo = new PendingRequestVO();
		vo.setType(TablePending.TYPE_ATTENDANCE_LOATION);
		vo.setMethodType(SubmitWorkManager.HTTP_METHOD_POST);
		vo.setTitle(PublicUtils.getResourceString(context, R.string.submit_string2));
		vo.setContent(PublicUtils.getResourceString(context, R.string.submit_string1));
		vo.addParams("data", location2JSONArray(pendingList));
		vo.setUrl(UrlInfo.getUrlAttendanceLocationInfo(context));
		performJustSubmit(vo);
		removeLocationPending(pendingList);
	}
	
	private String location2JSONArray(List<LocationPending> pendingList){
		JSONArray jsonArr = new JSONArray();
		for(LocationPending pending : pendingList){
			try {
				jsonArr.put(new JSONObject(pending.getValue()));
			} catch (JSONException e) {
				JLog.e(e);
			}
		}
		return jsonArr.toString();
	}
	
	private void removeLocationPending(List<LocationPending> pendingList){
		StringBuffer sb = new StringBuffer();
		for(LocationPending pending : pendingList){
			sb.append(",").append(pending.getId());
		}
		if(sb.length()>0){
			new LocationPendingDB(context).deleteLocationById(sb.substring(1));
		}
	}

	/**
	 * 删除该条记录
	 * @param
	 * @throws Exception 
	 */
	protected void killPending(TablePending tp) throws Exception {
		tpDB.removeTablePendingById(tp.getId());
		CoreHttpPendingRequest request = tp.getRequest();
		if(request.getFiles() != null){ //删除本地图片
			for (Map.Entry<String, String> entry : request.getFiles().entrySet()) {
				File file =  new File(entry.getValue());
				if (file.exists()) {
					file.delete();
				}
				

//				String path = file.getAbsolutePath();
//				if (file.exists() && path.endsWith("jpg")){//企业微信的图片不删除
//					file.delete();
//				}
			}
		}
	}
	
	/**
	 * 删除该条记录并发送广播
	 * @param
	 * @throws Exception 
	 */
	protected void killPendingAndSendBroadcast(TablePending tp) throws Exception {
		killPending(tp);
		sendBroadcast(true);
	}
	
	/**
	 * 接收PUSH，删除所有错误提交数据
	 */
	public void removeByFail(){
		//删除0001和0002数据
		tpDB.removeFailTablePending();
	}
	
	/**
	 * 接收PUSH，将失败提交改为准备提交
	 */
	public void updatePendingInfoByFail(){
		//将所有数据恢复准备提交状态，除正在提交的
		tpDB.updateAllTablePendingToReady();
	}
	
	/**
	 * 将正在等待的都变成网络异常状态
	 */
	public void updateAllReadyToNetWorkError(){
		tpDB.updateAllReadyToNetWorkError();
		sendBroadcast(false);
	}
	
	/**
	 * 将网络异常的都变成正在等待状态
	 */
	public int updateAllNetWorkErrorToReady(){
		int num = tpDB.updateAllNetWorkErrorToReady();
		sendBroadcast(true);
		return num;
	}
	
	/**
	 * 因网络错误，更新pending次数
	 * @param
	 */
	protected void updatePendingForNumberOfTimes(TablePending pendingInfo){
		int n = 0;
		if(pendingInfo.getType() == TablePending.TYPE_LARGE_IMAGE){ //如果是大图片，只提交一次，因此直接更新成最大次数，不再提交
			n = TablePending.TABLE_PENDING_MAX_NUMBER;
		}else{
			n = pendingInfo.getNumberOfTimes() + 1;
		}
		
		tpDB.updateTablePendingNumberOfTimesById(pendingInfo.getId(), n);
		sendBroadcast(false);
	}
	
	/**
	 * 更新pending为网络错误
	 * @param
	 */
	protected void updatePendingForNetWorkError(TablePending pendingInfo){
		if(pendingInfo.getType() == TablePending.TYPE_LARGE_IMAGE){//如果是大图片更状态
			tpDB.updateTablePendingStatusById(pendingInfo.getId(),TablePending.STATUS_ERROR_BIG_SIZE);
		}else{
			tpDB.updateTablePendingStatusById(pendingInfo.getId(),TablePending.STATUS_ERROR_NETWORK);
		}
		sendBroadcast(false);
	}
	
	/**
	 * 更新pending为服务器返回0001
	 * @param
	 */
	protected void updatePendingForServerError(TablePending pendingInfo){
		tpDB.updateTablePendingStatusById(pendingInfo.getId(),TablePending.STATUS_ERROR_SERVER);
		sendBroadcast(false);
	}
	
	/**
	 * 更新pending为服务器返回0002
	 * @param
	 */
	protected void updatePendingForUserError(TablePending pendingInfo){
		tpDB.updateTablePendingStatusById(pendingInfo.getId(),TablePending.STATUS_ERROR_USER);
		sendBroadcast(false);
	}
	
	private void sendBroadcast(boolean isSuccess){
		Intent intent = new Intent(Constants.BROADCAST_SUBMMIT_FINISH);
		intent.putExtra("isSuccess", isSuccess);
		context.sendBroadcast(intent);
	}
	
	public void putPendingRequest(PendingRequestVO vo,CoreHttpPendingRequest request){
		
		TablePending tablePending = new TablePending();
		tablePending.setCreateDate(DateUtil.getCurDateTime());
		tablePending.setRequest(request);
		tablePending.setTitle(vo.getTitle());
		tablePending.setContent(vo.getContent());
		tablePending.setType(vo.getType());
		tablePending.setStatus(TablePending.STATUS_READY);
//		String url = vo.getUrl();
//		if (url.equals("")) {
//			tablePending.setNote("car_sales");
//		}
		tpDB.insertNewOrder(tablePending);
	}
	
	/**
	 * 将要提交的数据存到转化成json存到putPendingRequest中

	 * @return
	 */
	public int performJustSubmit(PendingRequestVO vo) {
		int REQUEST_ID = this.generateReqID();
		JLog.d("HTTP","ready:attempt to execute request:" + REQUEST_ID);
		if(vo.getParams() != null){
			String apkversion = ApplicationHelper.getApplicationName(context).appVersionCode;
			vo.getParams().put("apkversion", apkversion);
			Log.d("888", "apkversion="+ apkversion);
		}
		this.putPendingRequest(vo,new CoreHttpPendingRequest(REQUEST_ID, vo.getMethodType(),false, vo.getUrl(), vo.getParams(),null));
		return REQUEST_ID;
	}
	
	/**
	 * 将要上传的图片信息存储到提交队列中

	 * @return
	 */
	public int performImageUpload(PendingRequestVO vo) {
		int REQUEST_ID = this.generateReqID();
		
		HashMap<String,String> files = new HashMap<String,String>();
		files.put(FILE_KEY, renameTo(vo.getImagePath()));
		this.putPendingRequest(vo,new CoreHttpPendingRequest(REQUEST_ID,HTTP_METHOD_POST,true, vo.getUrl(), vo.getParams(), files));
		return REQUEST_ID;
	}
	
	/**
	 * 将要上传的文件信息存储到提交队列中

	 * @return
	 */
	public int performFileUpload(PendingRequestVO vo) {
		int REQUEST_ID = this.generateReqID();
		this.putPendingRequest(vo,new CoreHttpPendingRequest(REQUEST_ID,HTTP_METHOD_POST,true, vo.getUrl(), vo.getParams(), vo.getFiles()));
		return REQUEST_ID;
	}
	
	/**
	 * 点击提交的时候将照片换一个存放路径 防止清除缓存数据的时候清除掉
	 * @param imagePath 照片路径改变前的存放位置
	 * @return
	 */
	private String renameTo(String imagePath){
		File file = new File(imagePath);
		if(file.exists()){
			File dir = new File(Constants.SDCARD_PATH+"bak");
			if(!dir.exists()){
				dir.mkdirs();
			}
			String newFilePath = dir.getPath()+"/"+file.getName();
			File newFile = new File(newFilePath);
			if(newFile.exists()){
				newFile.delete();
			}
			try {
				newFile.createNewFile();
			} catch (IOException e) {
				newFilePath = imagePath;
			}
			file.renameTo(newFile);
			file.delete();
			return newFilePath;
		}else{
			return imagePath;
		}
	}
	
	/**
	 * 生成请求ID
	 * @return
	 */
	private int generateReqID() {
		Calendar now = Calendar.getInstance();
		Random rnd = new Random(now.getTimeInMillis());
		int id = rnd.nextInt(1000000);
		return id;
	}
	
	/**
	 * 开启后台提交服务
	 */
	public void commit(){
		if(!isStartService()){
			context.startService(new Intent(context,SubmitWorkService.class));
		}
	}

	/**
	 * 通过Service的类名来判断是否启动service服务
	 * 
	 * @return
	 */
	private boolean isStartService() {
		ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);  
		List<RunningServiceInfo> serviceList = activityManager.getRunningServices(30);  //先获得运行着sevice的列表
		for (int i = 0; i < serviceList.size(); i++) {
			if (SubmitWorkService.class.getName().equals(serviceList.get(i).service.getClassName())) {
				return true;
			}
		}
		return false;
	}

}
