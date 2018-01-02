package com.yunhu.yhshxc.http;

import gcg.org.debug.JLog;

import org.apache.http.Header;

import android.content.Context;
import android.text.TextUtils;

import cn.jpush.android.api.JPushInterface;

import com.yunhu.yhshxc.utility.ApplicationHelper;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

public class GcgHttpClient {

    private static AsyncHttpClient client = null;
    
    private static GcgHttpClient gcgHttpClient = null;
    
    private static Context mContext = null;
    
    
    public static int TIME_OUT_LONG = 120000;
    public static int TIME_OUT_SHORT = 15000;
    
//    private HttpResponseListener listener = null;
    
    private GcgHttpClient() {
    	if(client == null){
        	client = new AsyncHttpClient();
        	//client.setThreadPool(Executors.newSingleThreadExecutor());
//        	client.setTimeout(120000);
    	}
	}
    
    public static GcgHttpClient getInstance(Context context){
    	mContext = context;
    	if(gcgHttpClient == null){
    		gcgHttpClient = new GcgHttpClient();
    	}
    	client.setTimeout(TIME_OUT_LONG);
    	return gcgHttpClient;
    }
    
    public static GcgHttpClient getInstance(Context context,int timeOut){
    	mContext = context;
    	if(gcgHttpClient == null){
    		gcgHttpClient = new GcgHttpClient();
    	}
    	client.setTimeout(timeOut);
    	return gcgHttpClient;
    }
    
    private void addHeader(){
    	String subscriberId = PublicUtils.getSubscriberId(mContext.getApplicationContext());
		String phoneNo = PublicUtils.receivePhoneNO(mContext.getApplicationContext());
		String IMEI = PublicUtils.getDeviceId(mContext.getApplicationContext());
		String userCode = PublicUtils.getRandomCode(mContext.getApplicationContext());
		String version = ApplicationHelper.getApplicationName(mContext).appVersionName;
		String versionCode = ApplicationHelper.getApplicationName(mContext).appVersionCode;
		String grirms_user_is_code_one = SharedPreferencesUtil.getInstance(mContext).getGrirmsUserIsCodeOne();

		//String log = new StringBuffer().append("HTTP Thread(").append(Thread.currentThread().getName()).append(") phoneNo =").append(phoneNo)
		//		.append("IMSI=").append(subscriberId).append("IMEI=").append(IMEI).append("version=").append(version).toString();
		//JLog.d(log);
		if(TextUtils.isEmpty(subscriberId)){
			JLog.d("未获取到手机卡串码");
		}
		
		client.addHeader("GRIRMS_REQ_DEVICE",subscriberId);
		client.addHeader("GRIRMS_PROJ_NAME", mContext.getPackageName());
		client.addHeader("grirms_user_mmdn", phoneNo);
		
		if(PublicUtils.ISDEMO){
			client.addHeader("GRIRMS_USER_IMEI", "99000536259515");//860636000362764  IMEI     99000536259515
			client.addHeader("GRIRMS_USER_IMSI", "460031266314817");//      				460031266314817
			client.addHeader("GRIRMS_USER_CODE", "2c83a198d1e94b81a98209448ce675c5");//b3ae0958dcba4c6e9c11fdaf2f45d1da  userCode   2c83a198d1e94b81a98209448ce675c5
			
		}else{
			client.addHeader("GRIRMS_USER_IMEI", IMEI);//860636000362764  IMEI     99000536259515
			client.addHeader("GRIRMS_USER_IMSI", subscriberId);//      				460031266314817
			client.addHeader("GRIRMS_USER_CODE", userCode);//b3ae0958dcba4c6e9c11fdaf2f45d1da  userCode   2c83a198d1e94b81a98209448ce675c5
			
		}
		

		
		
		
		
		client.addHeader("grirms_user_is_code_one", grirms_user_is_code_one);
		client.addHeader("GRIRMS_VERSION",version);
		client.addHeader("GRIRMS_VERSION_CODE",versionCode);
		client.addHeader("GRIRMS_MODEL",android.os.Build.MODEL);//手机型号
		client.addHeader("GRIRMS_RELEASE",android.os.Build.VERSION.RELEASE);//手机系统版本号
		client.addHeader("GRIRMS_USER_REG",JPushInterface.getRegistrationID(mContext));//Jpush的注册ID
	}
    
  

	public void get(String url, RequestParams params, final HttpResponseListener listener) {
		addHeader();
		JLog.d(url);
        if(listener == null){
    		throw new NullPointerException("HttpResponseListener not null");
    	}
//		this.listener = listener;
		client.get(url, params, new TextHttpResponseHandler() {
			
			@Override
			public void onStart() {
				listener.onStart();
			}

			@Override
			public void onFinish() {
				listener.onFinish();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				listener.onFailure(throwable, responseString);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, String responseString) {
				listener.onSuccess(statusCode, responseString);
			}
		});
    }
	

	public void getNoHeader(String url, RequestParams params, final HttpResponseListener listener) {

		JLog.d(url);
        if(listener == null){
    		throw new NullPointerException("HttpResponseListener not null");
    	}
//		this.listener = listener;
		client.get(url, params, new TextHttpResponseHandler() {
			
			@Override
			public void onStart() {
				listener.onStart();
			}

			@Override
			public void onFinish() {
				listener.onFinish();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				listener.onFailure(throwable, responseString);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, String responseString) {
				listener.onSuccess(statusCode, responseString);
			}
		});
    }
	
	
    public void post(String url, RequestParams params, final HttpResponseListener listener) {
    	addHeader();
    	JLog.d(url);
        if(listener == null){
    		throw new NullPointerException("HttpResponseListener not null");
    	}
//    	this.listener = listener;
		client.post(url, params, new TextHttpResponseHandler() {
			
			@Override
			public void onStart() {
				listener.onStart();
			}

			@Override
			public void onFinish() {
				listener.onFinish();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				listener.onFailure(throwable, responseString);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, String responseString) {
				listener.onSuccess(statusCode, responseString);
			}
		});

    }
    
    public void post(String url, RequestParams params, TextHttpResponseHandler hander) {
    	addHeader();
    	JLog.d(url);
        if(hander == null){
    		throw new NullPointerException("TextHttpResponseHandler not null");
    	}
		client.post(url, params, hander);
    }
    
    public void get(String url, RequestParams params, TextHttpResponseHandler hander) {
    	addHeader();
		JLog.d(url);
		if(hander == null){
    		throw new NullPointerException("TextHttpResponseHandler not null");
    	}
		client.get(url, params, hander);
    }
    
    
//    private AsyncHttpResponseHandler resHander =  new TextHttpResponseHandler(){
//    	
//
//		@Override
//		public void onStart() {
//			listener.onStart();
//		}
//
//		@Override
//		public void onFinish() {
//			listener.onFinish();
//		}
//
//		@Override
//		public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//			listener.onFailure(throwable, responseString);
//		}
//
//		@Override
//		public void onSuccess(int statusCode, Header[] headers, String responseString) {
//			listener.onSuccess(statusCode, responseString);
//		}
//    };
    
    
}
