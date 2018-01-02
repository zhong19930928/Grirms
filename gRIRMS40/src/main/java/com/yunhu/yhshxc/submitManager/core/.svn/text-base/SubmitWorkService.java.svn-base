package com.yunhu.yhshxc.submitManager.core;

import gcg.org.debug.JDebugOptions;
import gcg.org.debug.JLog;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;

import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.submitManager.bo.CoreHttpPendingRequest;
import com.yunhu.yhshxc.submitManager.bo.TablePending;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.NetSettingUtil;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.loopj.android.http.RequestParams;

public class SubmitWorkService extends Service{
	
	private final String TAG = this.getClass().getSimpleName();
	
	private boolean isRunning = false; //是否正在提交表单
	private static SubmitWorkManager manager = null; //
	
	private AlarmManager am = null;  //定时器
	private PendingIntent pi = null; //定时执行意图

	private int runningTime = 1*60*1000; //防止提交执行中，服务异常回收
	private int intervalTime = 3*60*1000; //定时检查，因网络原因，设置成 未提交成功 的数据
	
	/**
	 * 定时启动服务
	 */
	private void startAlarm(int intervalMillis) {
		if(am == null){
			am = ((AlarmManager) this.getSystemService(Context.ALARM_SERVICE));
		}
		Intent intent = new Intent(this, this.getClass());
		if(intervalMillis == intervalTime){ //只在定时检查存在网络异常数据时（intervalTime），更新网络异常成ready
			intent.putExtra("updateAllNetWorkErrorToReady", true);
		}
		pi = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		//am.cancel(pi);
		am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+intervalMillis, intervalMillis,pi);
		JLog.d(TAG, "设置定时："+intervalMillis);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		startAlarm(runningTime);//此处设置定时，防止服务异常回收
		isRunning = false;
		manager = SubmitWorkManager.getInstance(this.getApplicationContext());
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(NetSettingUtil.isConnectMobileNetwork(getApplicationContext()) || NetSettingUtil.isConnectWifiNetwork(getApplicationContext())){
			if(!isRunning){ //如果没有数据提交，则执行提交
				isRunning=true;
				if(intent != null && intent.getBooleanExtra("updateAllNetWorkErrorToReady", false)){
					//只在定时检查存在网络异常数据时（intervalTime），更新网络异常成ready
					//避免防止服务回收（runningTime）时与一般提交时多次更新此处
					int num = manager.updateAllNetWorkErrorToReady();
					if(num<1){
						JLog.d(TAG, "没有提交数据！");
						closeService();
						return Service.START_STICKY;
					}
				}
				printLog("-------start-------");
				submitting();
			}else{
				printLog("后台提交正在运行...");
			}
		}else{
			try {
				manager.updateAllReadyToNetWorkError();
				printLog("网络已关闭...");
			} catch (Exception e) {
				JLog.e(e);
			}
			closeService();
		}
		return Service.START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	/**
	 * 将状态为正在提交的数据，提交到服务器
	 * 如果提交失败：
	 * 1.因网络原因，数据numberOfTimes（numberOfTimes<3）加1，进入下次提交提交
	 * 2.因服务器返回非0000，更改数据状态
	 */
	private void submitting(){
		TablePending submittingPending = manager.getSubmitting() ;
		if(submittingPending != null){
			if(submittingPending.getNumberOfTimes() < TablePending.TABLE_PENDING_MAX_NUMBER){
				try {
					performPendingRequest(submittingPending);
				} catch (Exception e) {
					JLog.e(e);
				}
				return;
			}else{
				//三次提交后，设置成网络异常，不再提交
				printLog("提交次数：（"+submittingPending.getRequest().getRequestID()+"）"+TablePending.TABLE_PENDING_MAX_NUMBER);
				manager.updatePendingForNetWorkError(submittingPending);
			}
		}
		ready2Submitting();
		
	}
	
	/**
	 * 将pending通过Http提交到服务器
	 * 其中分为Get，Post,
	 * 提交失败pengding+1;
	 * 提交成功调用returnSuccess
	 */
	private void performPendingRequest(final TablePending pendingInfo) throws Exception {
		CoreHttpPendingRequest request = pendingInfo.getRequest(); // 提交数据
		printLog("(" + request.getRequestID() + ")"+pendingInfo.getTitle()+"&"+pendingInfo.getContent());
		String url = request.getUrl();
		//提交参数
		RequestParams reqParams = new RequestParams();
		if (request.getParams() != null && !request.getParams().isEmpty()) {
			for (Map.Entry<String, String> entry : request.getParams().entrySet()) {
				reqParams.put(entry.getKey(), entry.getValue());
				 JLog.d(JDebugOptions.TAG_SUBMIT,"(request:"+request.getRequestID()+")key="+entry.getKey()+"/value="+entry.getValue());
			}
		}
		printLog("(" + request.getRequestID() + ")"+url);
		
		//判断数据类型
		if (request.getExecType() == SubmitWorkManager.HTTP_METHOD_GET) {

			GcgHttpClient.getInstance(this).get(url, reqParams,
					new HttpResponseListener() {

						TablePending pending = pendingInfo;
						@Override
						public void onStart() {
						}

						@Override
						public void onFailure(Throwable error, String content) {
						 //网络异常提交失败，pending+1
							returnFailure(content,pending);
						}

						@Override
						public void onSuccess(int statusCode, String content) {
							//返回成功
							returnSuccess(content, pending);
						}

						@Override
						public void onFinish() {
						}

					}
			);

		} else {
			//存储文件参数
			HashMap<String, String> files = request.getFiles();
			if (files != null && !files.isEmpty()) {
				for (Map.Entry<String, String> entry : files.entrySet()) {
					File file = new File(entry.getValue());
					if (file.exists()){
						reqParams.put(entry.getKey(), file);
					}else{
						// 文件丢失后，为被特殊情况删除
						removeLostImage(pendingInfo,entry.getValue());
						return;
					}
					
				}
			}
			GcgHttpClient.getInstance(this).post(url, reqParams,
					new HttpResponseListener() {

					TablePending pending = pendingInfo;
						@Override
						public void onStart() {
						}

						@Override
						public void onFailure(Throwable error, String content) {
							
							//网络异常提交失败，pending+1
							returnFailure(content,pending);
						}

						@Override
						public void onSuccess(int statusCode, String content) {
							returnSuccess(content, pending);
						}

						@Override
						public void onFinish() {
						}
				}
			);
		}

	}
	
	/**
	 * 提交后返回失败， 
	 * 提交失败后numberOfTimes+1
	 * 
	 * @param result
	 * @param TablePending
	 */
	private void returnFailure(String content, TablePending pendingInfo) {
		CoreHttpPendingRequest request = pendingInfo.getRequest();
		printLog(request.getRequestID() + "/onFailure:" + content);
		manager.updatePendingForNumberOfTimes(pendingInfo);
		//进入下一次提交
		submitting();
	}
	
	/**
	 * 提交后返回的值的处理， 
	 * 提交成功返回0000后删除缓存数据，如果是图片，提交后删除缓存图片 
	 * 提交失败后等待下次提交
	 * 
	 * @param result
	 * @param TablePending
	 */
	private void returnSuccess(String result, TablePending pendingInfo) {
		CoreHttpPendingRequest request = pendingInfo.getRequest();
		printLog(request.getRequestID() + "/onSuccess:" + result);
		result = PublicUtils.verifyReturnValue(result); //ctwap返回，有可能会存在HTML
		if (TextUtils.isEmpty(result)) {
			manager.updatePendingForServerError(pendingInfo);
			
		}else{
			//后台提交返回值处理方法
			try {
				if (result.equals("200")) { // 返回200的数据为金博需要的活跃情况
					 // 清除记录
					manager.killPendingAndSendBroadcast(pendingInfo);
				} else {
					//数据提交后，返回值==0000 ? 删除 : 将pending状态改变
					JSONObject json = new JSONObject(result);
					if (PublicUtils.isValid(json, Constants.RESULT_CODE)) {
						String code = json.getString(Constants.RESULT_CODE);
						if (code.equals(Constants.RESULT_CODE_SUCCESS)) {
							// 清除记录
							manager.killPendingAndSendBroadcast(pendingInfo);
						}else if (code.equals(Constants.RESULT_CODE_FAILURE)){
							manager.updatePendingForServerError(pendingInfo);
						}else if(code.equals(Constants.RESULT_CODE_NO_REGISTER)){
							manager.updatePendingForUserError(pendingInfo);					
						}
					}else{
						throw new Exception();
					}
				}
			} catch (Exception e) {
				JLog.e(e);
			}
		}
		
		//此时提交成功，证明此时网络正常
		manager.updateAllNetWorkErrorToReady();
		//进入下一次提交
		submitting();
	}
	
	/**
	 * 如果图片文件丢失，则图片删除图片数据，
	 * 并提交Log
	 * 
	 * @param pendingInfo
	 * @throws Exception
	 */
	private void removeLostImage(TablePending pendingInfo,String fileName) throws Exception{
		CoreHttpPendingRequest request = pendingInfo.getRequest();
		// 文件丢失后，为被特殊情况删除
		manager.killPendingAndSendBroadcast(pendingInfo);
		printLog(request.getRequestID()+ ": Photo has been lost："+fileName);
		//进入下一次提交
		submitting();
	}

	/**
	 * 将准备提交的数据改变为正在提交的数据
	 * 如果没有准备提交的数据，关闭服务
	 */
	private void ready2Submitting(){
		if(manager.isEmptyReady()){
			this.submitting();
		}else{
			closeService();
			printLog("------end-------");
		}
	}
	
	/**
	 * 关闭服务
	 */
	private void closeService(){
		this.stopSelf();
	}
	
	/**
	 * 打印Log
	 * @param log
	 */
	private void printLog(String log){
//		JLog.d(TAG, log);
		JLog.d(JDebugOptions.TAG_SUBMIT,log);//需要上传服务器的log
	}

	@Override
	public void onDestroy() {
		//此处定时，为了检查因网络原因没有成功提交的数据
		startAlarm(intervalTime);
		super.onDestroy();
	}
	
	
}
