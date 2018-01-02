package com.yunhu.yhshxc.http.mdn;

import encoding.Base64;
import gcg.org.debug.JLog;

import java.io.IOException;
import java.util.Calendar;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.exception.HTTPResponseResultException;
import com.yunhu.yhshxc.http.CoreHttpAbstractChances;
import com.yunhu.yhshxc.http.listener.ResponseListener;
import com.yunhu.yhshxc.utility.ApplicationHelper;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.widget.ToastOrder;

/**
 * 获取电话号码,
 * 电信、联通根据wap网络来获取电话好码
 * 移动通过输入电话号码
 * 
 * @author jishen
 *
 */
public class CoreHttpMDN extends CoreHttpAbstractChances{

	private final String TAG ="CoreHttpMDN";
	private final String a = "90004"; //GET参数中缺失IMSI参数
	private final String b = "400"; //CTWAP返回的手机号码有误
	private final String c = "80002"; //MSI/IMEI码格式错误或⽆无效
	private final String d = "80003"; // CTWAP的头文件无效，通常意味着⼿手机没有通过CTWAP访问此接口
	private final String e = "网络异常"; // 网络异常
	private ResponseListener listener = null; //网络请求响应监听
	private Context context = null;
	private boolean isRun = true; //是否执行获取电话号，true：执行，false:不执行
	private int mode = 0; //哪家运营商
	private int attemptRequest = 0; //尝试请求次数
	private int callbackCount = 0; //CoreHttpAbstractChances 回调次数

	/**
	 * 请求发起机制是一次获取用户信息的访问
	 */		
	private final int REQUEST_TYPE_INITIAL_AUTH=1000;
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 1){ //成功获取电话号
				stopSignalLevelListener();
				isRun = true;
				String mdn = (String)msg.obj;
				if(mdn.equals(a)){
					ToastOrder.makeText(context, a+context.getResources().getString(R.string.donot_get_phone_number), ToastOrder.LENGTH_SHORT).show();
				}else if(mdn.equals(b)){
					ToastOrder.makeText(context, b+context.getResources().getString(R.string.donot_get_phone_number), ToastOrder.LENGTH_SHORT).show();
				}else if(mdn.equals(c)){
					ToastOrder.makeText(context, c+context.getResources().getString(R.string.donot_get_phone_number), ToastOrder.LENGTH_SHORT).show();
				}else if(mdn.equals(d)){
					ToastOrder.makeText(context, d+context.getResources().getString(R.string.donot_get_phone_number), ToastOrder.LENGTH_SHORT).show();
				}else{
					if(listener != null){
						listener.receive(0, mdn); //返回结果
					}
				}
			}else if(msg.what == 2){//没有取到电话号码
				if(mode == PublicUtils.MODE_CHINA_MOBILE && attemptRequest > 2){ //如果是移动的话，只执行3次
					stopSignalLevelListener();
					ToastOrder.makeText(context, e, ToastOrder.LENGTH_SHORT).show();
				}
				isRun = true;
				if(attemptRequest % 2 == 0){ //每隔2次提交是一次
					ToastOrder.makeText(context, e, ToastOrder.LENGTH_SHORT).show();
				}
				JLog.d(TAG,"网络异常 attemptRequest"+attemptRequest);
			}else if(msg.what == 3){
				new CoreHttpMDNChinaMobileSMSMode(context,handler).showDialog();
			}
			
			super.handleMessage(msg);
		}
		
	};
	
	public CoreHttpMDN(Context context) {
		super(context);
		this.context = context;
		mode = PublicUtils.receiveOperator(context);//获取当前手机卡运营商
	}
	
	/**
	 * 获取电话号码的方法
	 */
	public void getMDN(){
		startSignalLevelListener();
	}
	
	/**
	 * 停止继续获取电话号码
	 */
	public void releaseHttpClient() {
		stopSignalLevelListener();
	}
	
	/**
	 * 当获取电话号码会回调此方法
	 * @param listener
	 */
	public void setResponseListener(ResponseListener listener) {
		this.listener = listener;
	}

	/**
	 * CoreHttpAbstractChances 回调方法
	 */
	@Override
	public void ifOppotunity() {
		if(isRun){ //是否已经执行
			callbackCount++;
			if(this.isOppotunity()){ //判断此时网络是否适合发起请求
				isRun = false;
				attemptRequest++;
				//mode = PublicUtils.MODE_CHINA_MOBILE;
				if(mode == PublicUtils.MODE_CHINA_MOBILE){ //如果是中国移动
					//new CoreHttpMDNChinaMobileSMSMode(context,handler).obtainMDN();
					new CoreHttpMDNChinaMobileSMSMode(context,handler).showDialog();
				}else{
					request().start(); //开始获取电话号
				}
			}else{
				if(callbackCount % 3 == 0){
					//Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
				}
			}
		}
		
	}
	
	/**
	 * 请求获取电话好码
	 * @return
	 */
	private Thread request(){
		return new Thread(){
			@Override
			public void run() {
				JLog.d(TAG,"------MDN startting----"+this.currentThread().getName());
				String mdn = getCacheMDN();
				if(TextUtils.isEmpty(mdn)){ //如果存在电话号码，就不在获取，否则开始获取
					mdn = getMDNfromProxy();
				}
				JLog.d(TAG,"MDN return with value:"+mdn);
				if(TextUtils.isEmpty(mdn) || mdn.length() != 11){ //验证电话
					String projectVersion=context.getResources().getString(R.string.PROJECT_VERSIONS);
					if (Constants.APP_VERSION_4_5.equalsIgnoreCase(projectVersion)) {
						if(mdn.equals(d)){
							handler.sendEmptyMessage(3);
						}else{
							SystemClock.sleep(30000);
							handler.sendEmptyMessage(2);
						}
					}else{
						if(mode == PublicUtils.MODE_CHINA_UNICOM && mdn.equals(d)){
							handler.sendEmptyMessage(3);
						}else{
							SystemClock.sleep(30000);
							handler.sendEmptyMessage(2);
						}
					}
				}else{
					context.getApplicationContext().getSharedPreferences(PublicUtils.PREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE).edit().putString(PublicUtils.PREFERENCE_NAME_PHONE, mdn).commit();
					handler.obtainMessage(1, mdn).sendToTarget();
				}
				JLog.d(TAG,"------MDN end----"+this.currentThread().getName());
			}
		};
	}
	
	/**
	 * 缓存电话
	 * @return
	 */
	private String getCacheMDN(){
		String mdn = null;
		SharedPreferences MDNPref = context.getApplicationContext().getSharedPreferences(PublicUtils.PREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
		mdn = MDNPref.getString(PublicUtils.PREFERENCE_NAME_PHONE, null);
		if(!TextUtils.isEmpty(mdn) && (mdn.equals(a) || mdn.equals(b) || mdn.equals(c) || mdn.equals(d))){
			mdn = null;
		}
		return mdn;
	}
	
	/**
	 * 通过网络来获取电话好码
	 * @return
	 */
	private String getMDNfromProxy() {
		AuthHttpHelper request = new AuthHttpHelper(context,mode);
		int reqID = generateReqID();
		String mdn = null;
		try {
//			String url = Constants.URL_GET_MDN+PublicUtils.getSubscriberId(context); //获取电话号码的接口
			String url = Constants.URL_GET_MDN+PublicUtils.getIMSI(context); //获取电话号码的接口
			JLog.d(TAG,"MDN url:"+url);
			mdn = request.authGetMethod(url, true, reqID,generateAuthValue(reqID)); //网络请求
		} catch (IOException e) {
			request = new AuthHttpHelper(context,mode);
//			String url = Constants.URL_GET_MDN_BAK+PublicUtils.getSubscriberId(context);//获取电话号码的备用接口
			String url = Constants.URL_GET_MDN_BAK+PublicUtils.getIMSI(context);//获取电话号码的备用接口
			JLog.d(TAG,"MDN url:"+url);
			JLog.e(e);
			try {
				mdn = request.authGetMethod(url, true, reqID,generateAuthValue(reqID)); //网络请求
			} catch (IOException e1) {
				mdn = null;
				e1.printStackTrace();
			}
		}
		return mdn;
	}
	
	/**
	 * 关闭信号回掉
	 */
	private void stopSignalLevelListener(){
		JLog.d(TAG,"stopSignalLevelListener");
		this.stopListen();
	}
	
	/**
	 * 开启信号回掉
	 */
	private void startSignalLevelListener() {
		JLog.d(TAG,"startSignalLevelListener");
		this.startListen();
	}
	
	/**
	 * 通过wap网络根据哪个运营商来获取电话好码
	 * 访问获取电话号码的接口，服务器会返回电话号码
	 * 
	 * @author david.hou
	 *
	 */
	class AuthHttpHelper {

		private DefaultHttpClient mClient;
		private HttpRequestBase httpMethod;
		private Context context;
		private Integer mode;
		
		/**
		 * 
		 * @param con
		 * @param mode 哪个运营商
		 * @author houyu
		 */
		protected AuthHttpHelper(Context con,Integer mode) {
			this.context=con;
			this.mode = mode; //运营商
			addHead();
		}
		
		/**
		 * 添加的请求参数
		 */
		private void addHead() {
			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
			HttpConnectionParams.setStaleCheckingEnabled(params, false);
			HttpClientParams.setRedirecting(params, true);
			mClient = new DefaultHttpClient(params);
			
			//根据运营商添加代理
			HttpHost proxy = null;
			if(mode == PublicUtils.MODE_CHINA_UNICOM){ 
				proxy = new HttpHost("10.0.0.172", 80);
				mClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
			}else if(mode == PublicUtils.MODE_CHINA_TELECOM){
				proxy = new HttpHost("10.0.0.200", 80);
				mClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
				mClient.getCredentialsProvider().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("ctwap@mycdma.cn", "vnet.mobi"));
			}else{
				JLog.d("这是什么运营商？");
			}
		}

		/**
		 * 添加的头文件
		 */
		protected String authGetMethod(String url, boolean isAuth, int reqID,
				String authCode) throws IOException {
			Calendar now = Calendar.getInstance();
			httpMethod = new HttpGet(url);
			if (isAuth) {
				httpMethod.addHeader("GRIRMS_HTTP_TYPE", Integer.toString(REQUEST_TYPE_INITIAL_AUTH));
				httpMethod.addHeader("GRIRMS_PASS", authCode);
			}
			httpMethod.addHeader("GRIRMS_PROJ_NAME", this.context.getPackageName());
			httpMethod.addHeader("GRIRMS_REQ_ID", Integer.toString(reqID));
			httpMethod.addHeader("GRIRMS_REQ_TIME", Long.toString(now.getTimeInMillis()));
			
			httpMethod.addHeader("CLIENT_VERSION", ApplicationHelper.getApplicationName(context).appVersionName);
			httpMethod.addHeader("MODEL", android.os.Build.MODEL);
			httpMethod.addHeader("OS_VERSION", android.os.Build.VERSION.RELEASE);
			httpMethod.addHeader("BRAND", android.os.Build.BRAND);
			httpMethod.addHeader("BOARD", android.os.Build.MANUFACTURER);
			httpMethod.addHeader("PRODUCT", android.os.Build.PRODUCT);
			httpMethod.addHeader("ROM_VERSION", android.os.Build.FINGERPRINT);
			httpMethod.addHeader("BUILD", android.os.Build.DISPLAY);
			if(PublicUtils.ISDEMO){
				
				httpMethod.addHeader("IMEI", "99000536259515");
				httpMethod.addHeader("IMSI", "460031266314817");
			}else{
				httpMethod.addHeader("IMEI", PublicUtils.getDeviceId(context.getApplicationContext()));  
//				httpMethod.addHeader("IMSI", PublicUtils.getSubscriberId(context.getApplicationContext()));	
				httpMethod.addHeader("IMSI", PublicUtils.getIMSI(context.getApplicationContext()));	
				
			}
			
			
			//Header arr[] = httpMethod.getAllHeaders();
			//for (int i=0; i<arr.length; i++) {
			//	JLog.d(TAG, arr[i].getName()+":"+arr[i].getValue());
			//}
			String result = execute(httpMethod);
			return result;
		}

		/**
		 * 执行请求
		 * @param method
		 * @return
		 * @throws IOException
		 */
		private String execute(final HttpRequestBase method) throws IOException {
			String resultValue = null;
			try {
				HttpResponse response = mClient.execute(method);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { // OK
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						String r = EntityUtils.toString(entity, "UTF-8");
						resultValue = PublicUtils.verifyReturnValue(r);
						if(TextUtils.isEmpty(resultValue)){
							throw new HTTPResponseResultException(r);
						}
					}
				}
			} catch (IOException e) {
				resultValue = null;
				throw e;
			} finally {
				if (mClient != null) {
					mClient.getConnectionManager().shutdown();
				}
			}
			return resultValue;
		}

	}
	
	private int generateReqID() {
		Calendar now = Calendar.getInstance();
		Random rnd = new Random(now.getTimeInMillis());
		int id = rnd.nextInt(1000000);
		return id;
	}
	
	private String generateAuthValue(int reqID) {
		Calendar now = Calendar.getInstance();
		int day = now.get(Calendar.DAY_OF_MONTH);
		int mth = now.get(Calendar.MONTH) + 1;
		int mixed = (mth + day) * day + reqID;
		String mixedHex = Integer.toHexString(mixed).toUpperCase();
		return Base64.encodeBytes(mixedHex.getBytes());
	}
}
