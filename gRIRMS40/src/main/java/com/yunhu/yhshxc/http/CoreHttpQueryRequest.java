package com.yunhu.yhshxc.http;

import gcg.org.debug.JLog;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.yunhu.yhshxc.http.listener.ResponseListener;
import com.yunhu.yhshxc.http.mdn.CoreHttpMDN;
import com.yunhu.yhshxc.utility.HttpHelper;
import com.yunhu.yhshxc.utility.PublicUtils;

/**
 * 网络请求返回
 * @author jishen
 *
 */
public class CoreHttpQueryRequest extends CoreHttpAbstractChances{

	private final String TAG ="HTTP";
	private ResponseListener listener = null;
	private Context context = null;
	private int requestCode = 0;
	private int attemptRequest = 0;//尝试请求次数
	private int callbackCount = 0;//返回请求次数
	private String mdn = null;//当前手机号
	private boolean isReleaseHttp = false;//是否取消请求 false没有 true是
	private boolean isRun = true;//是否有线程正在运行 true是 false否
	private CoreHttpMDN coreHttpMDN = null;//获取电话号码
	private HashMap<Integer,String> requestMap = null;//?
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {

			if(msg.what == 1){ //成功获取
				String result = (String)msg.obj;
				if(requestMap.containsKey(msg.arg1)){
					requestMap.remove(msg.arg1);
				}
				if(requestMap.isEmpty()){
					stopSignalLevelListener();
				}
				isRun = true;
				if(listener != null){
					listener.receive(msg.arg1, result);//回调
					JLog.d(TAG,"CoreHttpQueryRequest:("+msg.arg1+")"+result);
				}
			}else if(msg.what == 2){//没有取到
				isRun = true;
				if(attemptRequest % 2 == 0){
					//Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
				}
				JLog.d(TAG,"网络异常 attemptRequest"+attemptRequest);
			}
			
			super.handleMessage(msg);
		}
		
	};
	
	public CoreHttpQueryRequest(Context context) {
		super(context);
		this.context = context;
		requestMap = new HashMap<Integer,String>(); 
		mdn = PublicUtils.receivePhoneNO(this.context);
		coreHttpMDN = new CoreHttpMDN(this.context);
		coreHttpMDN.setResponseListener(new ResponseListener() {
			
			@Override
			public void receive(int requestCode, Object dataObj) {
				mdn = (String)dataObj;
				startSignalLevelListener();//开启监听获取手机号码
			}
		});
	}
	
	@Override
	public void ifOppotunity() {
		if(isRun){
			Iterator<Entry<Integer, String>> iter = requestMap.entrySet().iterator();
			if(iter.hasNext()){
				Entry<Integer, String> entry= iter.next();
				int reqId = entry.getKey();
				String requestUrl = entry.getValue();
				callbackCount++;
				if(this.isOppotunity()){//网络状态满足的情况
					isRun = false;
					attemptRequest++;//尝试请求次数加1
					request(reqId,requestUrl).start();
				}else{
					if(callbackCount % 3 == 0){
						//Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
					}
				}
			}
		}
	}

	/**
	 * 开启线程请求
	 * @param reqId
	 * @param url
	 * @return
	 */
	private Thread request(final Integer reqId,final String url){
		return new Thread(){
			@Override
			public void run() {
				JLog.d(TAG,"------QueryRequest startting----"+this.currentThread().getName());
				
				JLog.d(TAG, "REQID:"+reqId+url);
				if(!isReleaseHttp) {//没有取消请求的话
					String result = new HttpHelper(context).connectGet(url);
					if(!isReleaseHttp){
						if(!TextUtils.isEmpty(result)){
							handler.obtainMessage(1, reqId, 0, result).sendToTarget();
						}else{
							handler.sendEmptyMessage(2);
						}
					}
				}
				
				JLog.d(TAG,"------QueryRequest end----"+this.currentThread().getName());
			}
		};
	}
	
	/**
	 * 
	 * @param url
	 * @return
	 */
	protected int performQueryRequest(String url){
		int REQUEST_ID = this.generateReqID();
		requestMap.put(REQUEST_ID, url);
		if(TextUtils.isEmpty(mdn)){//如果手机号为空 开启监听开始获取mdn
			coreHttpMDN.getMDN();
		}else{
			startSignalLevelListener();//开启监听获取监听信号状态
		}
		return REQUEST_ID;
	}
	
	/**
	 * 取消监听
	 */
	protected void releaseHttp() {
		isReleaseHttp = true;
		if(TextUtils.isEmpty(mdn)){//取消获取手机号码监听
			coreHttpMDN.releaseHttpClient();
		}
		stopSignalLevelListener();//取消信号状态监听
		if(requestMap != null){
			requestMap.clear();
		}
	}
	
	/**
	 * 设置监听
	 * @param listener
	 */
	public void setResponseListener(ResponseListener listener) {
		this.listener = listener;
	}

	
	/**
	 * 停止信号状态监听
	 */
	private void stopSignalLevelListener(){
		JLog.d(TAG,"stopSignalLevelListener");
		this.stopListen();
	}
	/**
	 * 开启信号状态监听
	 */
	private void startSignalLevelListener() {
		JLog.d(TAG,"startSignalLevelListener");
		this.startListen();
	}

	/**
	 * @return 返回请求code
	 */
	public int getRequestCode() {
		return requestCode;
	}

	private int generateReqID() {
		Calendar now = Calendar.getInstance();
		Random rnd = new Random(now.getTimeInMillis());
		int id = rnd.nextInt(1000000);
		return id;
	}
}
