package com.yunhu.yhshxc.utility;

import gcg.org.debug.JLog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import cn.jpush.android.api.JPushInterface;

import android.content.Context;

public class HttpHelper {

	private DefaultHttpClient mClient;
	private HttpRequestBase httpMethod;
	/** Timer对象 **/  
    private Timer timer = null;  
    /** TimerTask对象 **/  
    private TimerTask timerTask = null;
    
    private HttpURLConnection con;
    
    private Context context=null;
    
    public static final int TIMEOUT_SHORT = 1;
    public static final int TIMEOUT_LONG = 2;
    
    private int timeout = 120000;
    
	public HttpHelper(Context context) {
		this.context = context;
		addHead(TIMEOUT_LONG);
	}
	
	public HttpHelper(Context context,int duration) {
		this.context = context;
		addHead(duration);
	}
	
	private void closeTimer() {  
        if (timer != null) {  
            timer.cancel();  
            timer = null;  
        }  
        if (timerTask != null) {  
            timerTask = null;  
        }  
	}  
		
	private void addHead(int duration){
		if(TIMEOUT_SHORT == duration){
			timeout = 40000;
		}
		HttpParams params = new BasicHttpParams();
		setHeader(params);
		mClient = new DefaultHttpClient(params);
		addInterceptor();
	}
	
	private void setHeader(HttpParams params){
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
		HttpProtocolParams.setUserAgent(params, "Apache-HttpClient/Android");
		HttpConnectionParams.setConnectionTimeout(params, timeout);
		HttpConnectionParams.setSoTimeout(params, timeout);
		HttpConnectionParams.setStaleCheckingEnabled(params, false);
		HttpClientParams.setRedirecting(params, true);
		
		//4G对应 防止获取代理时异常退出
//		if(!NetSettingUtil.isOpenWifi(context)){
//			String proxyHost = Proxy.getDefaultHost();
//			if(!TextUtils.isEmpty(proxyHost)){
//				params.setParameter(ConnRoutePNames.DEFAULT_PROXY, new HttpHost(proxyHost, 80));
//			}else{
//				proxyHost = PublicUtils.checkIsWapForProxy(context);
//				if(!TextUtils.isEmpty(proxyHost)){
//					params.setParameter(ConnRoutePNames.DEFAULT_PROXY, new HttpHost(proxyHost, 80));
//				}
//			}
//		}
	}
	
	private void setGCGHeader(HttpRequestBase method){
		String subscriberId = PublicUtils.getSubscriberId(context.getApplicationContext());
		String phoneNo = PublicUtils.receivePhoneNO(context.getApplicationContext());
		String IMEI = PublicUtils.getDeviceId(context.getApplicationContext());
		String userCode = PublicUtils.getRandomCode(context.getApplicationContext());
		String version = ApplicationHelper.getApplicationName(context).appVersionName;
		String versionCode = ApplicationHelper.getApplicationName(context).appVersionCode;
		String grirms_user_is_code_one = SharedPreferencesUtil.getInstance(context).getGrirmsUserIsCodeOne();

		//String log = new StringBuffer().append("HTTP Thread(").append(Thread.currentThread().getName()).append(") phoneNo =").append(phoneNo)
		//		.append("IMSI=").append(subscriberId).append("IMEI=").append(IMEI).append("version=").append(version).toString();
		//JLog.d(log);
		method.setHeader("GRIRMS_REQ_DEVICE",subscriberId);
		method.setHeader("GRIRMS_PROJ_NAME", this.context.getPackageName());
		method.setHeader("grirms_user_mmdn", phoneNo);
		
		if(PublicUtils.ISDEMO){
			
			method.addHeader("GRIRMS_USER_IMEI", "99000536259515");//860636000362764  IMEI     99000536259515
			method.addHeader("GRIRMS_USER_IMSI", "460031266314817");//      				460031266314817
			method.addHeader("GRIRMS_USER_CODE", "2c83a198d1e94b81a98209448ce675c5");//b3ae0958dcba4c6e9c11fdaf2f45d1da  userCode   2c83a198d1e94b81a98209448ce675c5
				
		}else{
			
			method.setHeader("GRIRMS_USER_IMEI", IMEI);////860636000362764  IMEI
			method.setHeader("GRIRMS_USER_IMSI", subscriberId);
			method.setHeader("GRIRMS_USER_CODE", userCode);//b3ae0958dcba4c6e9c11fdaf2f45d1da userCode
			
		}

		
		
		method.setHeader("grirms_user_is_code_one", grirms_user_is_code_one);
		method.setHeader("GRIRMS_VERSION",version);
		method.setHeader("GRIRMS_VERSION_CODE",versionCode);
		method.setHeader("GRIRMS_MODEL",android.os.Build.MODEL);//手机型号
		method.setHeader("GRIRMS_RELEASE",android.os.Build.VERSION.RELEASE);//手机系统版本号
		method.addHeader("GRIRMS_USER_REG",JPushInterface.getRegistrationID(context));//Jpush的注册ID

	}

	private void addInterceptor(){
		mClient.addRequestInterceptor(new HttpRequestInterceptor() {

	        public void process(
	                final HttpRequest request,
	                final HttpContext context) throws HttpException, IOException {
	            if (!request.containsHeader("Accept-Encoding")) {
	                request.addHeader("Accept-Encoding", "gzip,deflat");
	            }
	        }

	    });
		
		mClient.addResponseInterceptor(new HttpResponseInterceptor() {

	        public void process(
	                final HttpResponse response,
	                final HttpContext context) throws HttpException, IOException {
	            HttpEntity entity = response.getEntity();
	            //Log.d("888", "response=>"+entity.getContentLength());
	            Header ceheader = entity.getContentEncoding();
	            if (ceheader != null) {
	                HeaderElement[] codecs = ceheader.getElements();
	                for (int i = 0; i < codecs.length; i++) {
	                    if (codecs[i].getName().equalsIgnoreCase("gzip")) {
	                        response.setEntity(new GzipDecompressingEntity(response.getEntity()));
	                        return;
	                    }
	                }
	            }
	        }

	    });
	}
	
	class GzipDecompressingEntity extends HttpEntityWrapper {
	
	    public GzipDecompressingEntity(final HttpEntity entity) {
	        super(entity);
	    }
	
	    public InputStream getContent()
	        throws IOException, IllegalStateException {
	
	        // the wrapped entity's getContent() decides about repeatability
	        InputStream wrappedin = wrappedEntity.getContent();
	
	        return new GZIPInputStream(wrappedin);
	    }
	
	    public long getContentLength() {
	        // length of ungzipped content is not known
	        return -1;
	    }
	
	}
	
	public String connectGet(String url){
		httpMethod = new HttpGet(url);
	    return execute(httpMethod);
	}
	
	public HttpEntity connectGetReturnEntity(String url){
		httpMethod = new HttpGet(url);
		
	    return executeReturnEntity(httpMethod);
	}
	
	public String connectPost(String url,Map<String, String> params){
		httpMethod = new HttpPost(url);
         // data - name/value params
         List<NameValuePair> nvps = null;
         if ((params != null) && (params.size() > 0)) {
            nvps = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
               nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
         }
         if (nvps != null) {
            try {
               HttpPost methodPost = (HttpPost) httpMethod;
               methodPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            } catch (UnsupportedEncodingException e) {
            	JLog.d(String.valueOf(httpMethod.getURI())+e.getMessage());
            }
         }
         return execute(httpMethod);
	}
    
	private String execute(final HttpRequestBase method) {
		setGCGHeader(method);
		String resultValue = null;
		long flow = PublicUtils.totalFlow();
		try{
		    HttpResponse response = mClient.execute(method);
		    if(method.isAborted()){
		    }
		    JLog.d("HttpStatus ==>("+Thread.currentThread().getName()+")"+response.getStatusLine().getStatusCode());
		    if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){  // OK
		    	 HttpEntity entity = response.getEntity();
		    	 if (entity != null) {
			         resultValue = EntityUtils.toString(entity,"UTF-8");
			     }
		    }
		}catch(Exception e){
			if(httpMethod !=null){
				JLog.d(String.valueOf(httpMethod.getURI())+e.getMessage());
			}else{
				JLog.d(e.getMessage());
			}
			resultValue = null;
		}finally{
			// 释放资源
			closeTimer();
			if(mClient != null){
				mClient.getConnectionManager().shutdown();
			}
		}
		resultValue = PublicUtils.verifyReturnValue(resultValue);
		JLog.d("HttpFlow==>("+Thread.currentThread().getName()+")"+(PublicUtils.totalFlow()-flow));
	    return resultValue;
	 }
	
	 private HttpEntity executeReturnEntity(final HttpRequestBase method) {
		 HttpEntity entity = null;
		try{
		    HttpResponse response = mClient.execute(method);
		    if(method.isAborted()){
		    	System.out.println("the request execution has been aborted");
		    }
		    System.out.println("HttpStatus ==>"+response.getStatusLine().getStatusCode());
		    if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){  // OK
		    	 entity = response.getEntity();
		    }
		}catch(Exception e){
			JLog.d(String.valueOf(httpMethod.getURI())+e.getMessage());
			entity = null;
		}finally{
			// 释放资源
			closeTimer();
			if(mClient != null){
				//mClient.getConnectionManager().shutdown();
			}
		}
	    return entity;
	 }
	
	public void disconnect(){
		
		System.out.println("HttpHelper ==>disconnect");
		if(httpMethod != null){
			//httpMethod.abort();		//socket 数据不完整异常
			mClient.getConnectionManager().shutdown();	//socket timeout exception
		}
		httpMethod = null;
		mClient = null;
	}
	
	/**
	 * HttpClient 上传信息和图片
	 * @param url
	 * @param params<key,value> 信息(可以为null)
	 * @param files<key,filePath> 图片(可以为null)
	 * @return
	 * @throws Exception
	 */
	public String uploadFile(String url, Map<String, String> params,Map<String, String> files) throws Exception{
		// 封装请求参数
		MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		if (params != null && !params.isEmpty()) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				StringBody par = new StringBody(entry.getValue());
				mpEntity.addPart(entry.getKey(), par);
			}
		}
		// 图片
		if (files != null && !files.isEmpty()) {
			for (Map.Entry<String, String> entry : files.entrySet()) {
				File file = new File(entry.getValue());
				if(file.exists()){
					mpEntity.addPart(entry.getKey(), new FileBody(file));
				}
			}
		}
		// 使用HttpPost对象设置发送的URL路径
		HttpPost post = new HttpPost(url);
		// 发送请求体
		post.setEntity(mpEntity);

		return execute(post);
	}
	
	/**
	 * 下载文件
	 * @param outPath
	 * @param outPath  存储路径
	 * @return
	 */
	public int downloadFile(String urlPath, String outPath) {
		int result = 0;
		try {
			
			File f=new File(outPath);
			if(f.exists()){
				File[] ff=f.listFiles();
				for (int i = 0; i < ff.length; i++) {
					ff[i].delete();
				}
			}
			
			int index = urlPath.lastIndexOf("/");
			String fileName = urlPath.substring(index + 1);
			String path=outPath+fileName;
			File dirFile=new File(outPath);
			if(!dirFile.exists()){
				dirFile.mkdir();
			}
			File outputFile=new File(path);
			outputFile.createNewFile();
			URL url = new URL(urlPath);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.connect();
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStream is = conn.getInputStream();
				FileOutputStream fos = new FileOutputStream(outputFile);
				byte[] bt = new byte[1024];
				int i = 0;
				while ((i = is.read(bt)) > 0) {
					fos.write(bt, 0, i);
				}
				fos.flush();
				fos.close();
				is.close();

			} else {
				result = 1;
			}

		} catch (FileNotFoundException e) {
			result = 1;
		} catch (IOException e) {
			result = 1;
		}
		return result;
	}
	
	/**
	 * 下载文件
	 * @param urlPsth	
	 * @param outPath  存储路径
	 * @return
	 */
	public int downloadStyleFile(String urlPath, String outPath,String fileName) {
		int result = 0;
		try {
			String path=outPath+fileName;
			File dirFile=new File(outPath);
			if(!dirFile.exists()){
				dirFile.mkdir();
			}
			File outputFile=new File(path);
			outputFile.createNewFile();
			URL url = new URL(urlPath);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.connect();
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStream is = conn.getInputStream();
				FileOutputStream fos = new FileOutputStream(outputFile);
				byte[] bt = new byte[1024];
				int i = 0;
				while ((i = is.read(bt)) > 0) {
					fos.write(bt, 0, i);
				}
				fos.flush();
				fos.close();
				is.close();

			} else {
				result = 1;
			}
		} catch (Exception e) {
			result = 1;
		}
		return result;
	}

	/**
	 * 下载考勤附件
	 * @param urlPath 下载的URL
	 * @param outPath 附件存储位置
	 * @param fileName 附件存储名字
	 * @return
	 */
	public int downloadNotifyAttachment(String urlPath, String outPath,String fileName){
		int result = 0;
		try {
			String path=outPath+fileName;
			File dirFile=new File(outPath);
			if(!dirFile.exists()){
				dirFile.mkdir();
			}
			File outputFile=new File(path);
			if (outputFile.exists()) {
				outputFile.delete();
			}
			outputFile.createNewFile();
			URL url = new URL(urlPath);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.connect();
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStream is = conn.getInputStream();
				FileOutputStream fos = new FileOutputStream(outputFile);
				byte[] bt = new byte[1024];
				int i = 0;
				while ((i = is.read(bt)) > 0) {
					fos.write(bt, 0, i);
				}
				fos.flush();
				fos.close();
				is.close();

			} else {
				result = 1;
			}
		} catch (Exception e) {
			result = 1;
		}
		return result;
	}
}
