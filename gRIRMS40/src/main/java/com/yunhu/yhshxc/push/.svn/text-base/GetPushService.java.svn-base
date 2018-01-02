package com.yunhu.yhshxc.push;

import encoding.Base64;
import gcg.org.debug.JLog;

import org.json.JSONArray;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;

import com.yunhu.yhshxc.bo.Push;
import com.yunhu.yhshxc.bo.PushItem;
import com.yunhu.yhshxc.database.PushDB;
import com.yunhu.yhshxc.database.PushItemDB;
import com.yunhu.yhshxc.dialog.CloseAppDialogActivity;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;

/**
 * 此服务用来接收消息服务器中的msg,接收后发送广播，由PushBroadcastReceiver接收处理
 * 800：没有消息
 * 90002:json格式错误
 * 90003：授权码认证错误
 * 90004：请求的格式或方法错误或参数丢失
 * 80001：手机号码格式不正确或缺失
 * 900：强制初始化
 * 
 * @author david.hou
 * @version 2013-05-30
 */
public class GetPushService extends Service {
	private final String TAG = "GetPushService";
	private boolean isRunning = false; //线程是否正在执行
	private AlarmManager am = null;  //定时器
	private PendingIntent pi = null; //定时执行意图
	private Context context ;

	/**
	 * 向服务器发送请求
	 */
	public int onStartCommand(Intent intent, int flags, int startId) {
		context = this;
		startAlarm(); //开始设置下次定时启动时间（防止服务以外终止push停止）
		/*
		 * 向消息服务器发起请求
		 * isRunnging :如何push正在执行，则不在进行
		 * Constants.PUSH_IS_RUNNING ： PushBroadcastReceiver是否在执行
		 */
		if(!isRunning && !Constants.PUSH_IS_RUNNING){
			isRunning = true;
			JLog.d(TAG,"执行一次push请求");
			String url = Constants.URL_PUSH_GET+"&mdn="+PublicUtils.receivePhoneNO(this);
			request(url);
//			Toast.makeText(this, "发起PUSH请求", Toast.LENGTH_LONG).show();
//			try {
//				String txt = FileHelper.inputStream2String(this.getAssets().open("push.txt"));
//				result(txt);
//			} catch (IOException e) {
//				
//				e.printStackTrace();
//			}
			
		}else{
			JLog.d(TAG,"be running");
		}
		return Service.START_STICKY;
	}
	
	/**
	 * 获取push msg后返回到此方法中
	 * 并根据"@"取出队列中qid
	 */
	public void request(final String url) {

		JLog.d(TAG,url);
		GcgHttpClient.getInstance(this).get(url, null, new HttpResponseListener(){
			
			@Override
			public void onFailure(Throwable error, String content) {
				JLog.d("GetPushService => "+error.getMessage());
				bak(url);
				
			}
			
			@Override
			public void onSuccess(int statusCode, String content) {
				String r = PublicUtils.verifyReturnValue(content);
				if (!TextUtils.isEmpty(r)) {
					result(r);
				}else{
					bak(url);	
				}
			}

			@Override
			public void onStart() {
				
			}

			@Override
			public void onFinish() {
				
			}
			
		} );
	}
	
	private void bak(String url){
		if(url.contains(Constants.URL_PUSH_GET_BAK)){
			finish();
		}else{
			request(Constants.URL_PUSH_GET_BAK+"&mdn="+PublicUtils.receivePhoneNO(GetPushService.this));
		}
	}
	
	private void result(String content){
		if (!TextUtils.isEmpty(content)) {
			JLog.d(TAG,content);
			int index = content.indexOf("@");
			if(index != -1){ //存在新消息
				String qid = content.substring(0,index); //取出qid
				save(qid,content.substring(index+1)).start(); //解析
			}else if(!TextUtils.isEmpty(content) && String.valueOf(Constants.PUSH_CODE_INIT).equals(content)){
				//消息服务器要求用户重新初始化数据
				SharedPreferencesUtil.getInstance(GetPushService.this).setIsInit(false);
				//弹出对话框要求用户确认退出
				Intent intent = new Intent(GetPushService.this,CloseAppDialogActivity.class); 
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
			}else{
				finish();
			}
		}
	}
	
	/**
	 * 处理完接收的消息后关闭此服务
	 */
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			finish();
		}
		
	};
	
	/**
	 * 关闭服务，并发送Broadcast到PushBroadcastReceiver
	 * 
	 */
	private void finish(){
		Intent i = new Intent(Constants.BROADCAST_ACTION_PUSH);
		i.putExtra(Constants.BROADCAST_KEY_PUSH, true);
		this.sendBroadcast(i);
		this.stopSelf();
		
	}
	
	/**
	 * 将队列中的msg解析成数组
	 * 根据"@"取出队列中msgId
	 * 
	 * @param qid //队列ID
	 * @param result //队列内容
	 * @return 线程对象
	 */
	private Thread save(final String qid,final String result){

		return new Thread(){

			@Override
			public void run() {
				try {
									
					JSONArray resArr = new JSONArray(result);
					int len = resArr.length();
					if(len == 0)return;
					String []contentArr = null;
					String msg = null;
					for(int i=0; i < len; i++){
						msg = resArr.getString(i);
						if(TextUtils.isEmpty(msg)) continue;
						contentArr = msg.split("@"); //差分msgId
						savePush(qid,contentArr[0],contentArr[1]);
			
					}
					JLog.d(TAG,"接收"+len+"条");
				} catch (Exception e) {
					JLog.e(e);
				}
				handler.sendEmptyMessage(0);
			}
			
		};
	}
	
	/**
	 * 保存消息队列到Push中
	 * 
	 * @param qid  队列id
	 * @param msgId  消息ID
	 * @param msgContent 消息内容
	 */
	private void savePush(String qid,String msgId,String msgContent){
		try {
			msgContent = new String(Base64.decode(msgContent)).trim(); //将Base64编码的消息解码
		} catch (Exception e) {
			msgContent = "";
		}
		
		//打印log
		StringBuffer log = new StringBuffer();
		log.append("save=").append("(Thread:").append(Thread.currentThread().getName()).append(") qid:").append(qid).append(" msgId:").append(msgId);
		JLog.d(TAG,log.toString());
		
		/* 是否此条push已经存在 ，如果不存在则保存至DB，待后续处理*/
		Push push = new PushDB(this).findPushByMsgId(msgId);
		if(push == null){
			push = new Push();
			push.setQueueId(qid);
			push.setMsgId(msgId);
			push.setCreateDate(DateUtil.getCurDateTime());
			push.setIsFinish(0);
			new PushDB(this).insertPush(push);
			
			
			//每条消息中存在定义的若干子消息，同过 “ $& ” 解析
			if(TextUtils.isEmpty(msgContent) || msgContent.indexOf("$&") == -1){
				savePushItem(msgContent,msgId);
			}else{
				String []contentArr = msgContent.split("\\$&");
				for(String str : contentArr){
					savePushItem(str,msgId);
				}
			}
		}else{
			JLog.d(TAG,"已存在msgId="+msgId);
		}
		
		
	}
	
	/**
	 * 保存每条消息到PushItem
	 * @param msgContent  消息内容
	 * @param msgId 消息ID
	 */
	private void savePushItem(String msgContent,String msgId){
		JLog.d(TAG, "push:"+msgContent);
		int type = 0;  //消息类型
		String status= ""; //消息操作符
		String content = ""; //消息内容
		if(!TextUtils.isEmpty(msgContent)){
			
			/*根据规则解析消息中真正的数据，例如：c|98|log*/
			int first = msgContent.indexOf("|");
			status = msgContent.substring(0,first);
			msgContent = msgContent.substring(first+1);
			if(status.equals("a")){ //此类型为APK下载
				content = msgContent;
				type = -1; 
			}else{
				int second = msgContent.indexOf("|");
				type = Integer.valueOf(msgContent.substring(0,second));
				content = msgContent.substring(second+1);
			}
		}
		PushItem pushItem = new PushItem();
		pushItem.setContent(content);
		pushItem.setMsgId(msgId);
		pushItem.setStatus(status);
		pushItem.setType(type);
		new PushItemDB(this).insert(pushItem);
		if (pushItem.getType() == Constants.PUSH_NOTIFY
				|| pushItem.getType() == Constants.PUSH_TASK
				|| pushItem.getType() == Constants.PUSH_TODO
				|| pushItem.getType() == Constants.PUSH_DOUBLE
				|| pushItem.getType() == Constants.PUSH_PLAN) {
			Intent intent = new Intent("new_message");
			
			context.sendStickyBroadcast(intent);
			context.sendBroadcast(new Intent("new_wechat_message"));
		}
		
		
	}

	/**
	 * 
	 */
	public IBinder onBind(Intent arg0) {
		return null;
	}

	/**
	 * 定时启动服务
	 */
	private void startAlarm() {
		int intervalMillis = 2*60*1000;
		if(am == null){
			am = ((AlarmManager) this.getSystemService(Context.ALARM_SERVICE));
			pi = PendingIntent.getService(this, 0, new Intent(this, this.getClass()), PendingIntent.FLAG_UPDATE_CURRENT);
		}
		am.cancel(pi);
		am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + intervalMillis,intervalMillis, pi);
	}
	
	/**
	 * 销毁服务，并定时下次启动时间
	 */
	public void onDestroy() {
		JLog.d(TAG,"stopSelf");
		startAlarm();
		super.onDestroy();
	}
}