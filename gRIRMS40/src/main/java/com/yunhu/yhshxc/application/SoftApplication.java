package com.yunhu.yhshxc.application;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.yunhu.yhshxc.MeetingAgenda.notification.Event;
import com.yunhu.yhshxc.MeetingAgenda.notification.NotificationServices;
import com.yunhu.yhshxc.activity.HomeMenuFragment;
import com.yunhu.yhshxc.activity.HomePageActivity;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;
import com.zhy.http.okhttp.https.HttpsUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import gcg.org.debug.JLog;
import im.fir.sdk.FIR;
import okhttp3.OkHttpClient;

//import im.fir.sdk.FIR;

/**
 * 应用运行崩溃后，由此捕获异常，上报Log
 * 
 * @author houyu
 * @version 2013.5.22
 */
public class SoftApplication extends MultiDexApplication {

	public boolean isActive = false; // 应用是否在后台
	public boolean isSubmitActive = true;
	public List<Activity> heapList = new ArrayList<Activity>();
	// public boolean isRegisterScreenStateReceiver = false;//是否注册屏幕状态改变广播
	public boolean isRegisterExitReceiver = false;// 是否注册了超时不操作退出应用广播

	private HomePageActivity homePageActivity;
	private HomeMenuFragment homeMenuFragment;

	private List<Event> eventsList;

	/**
	 * 分割 Dex 支持
	 * @param base
	 */
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}
	public static Context context;



	public Handler getMeetHandler() {
		return meetHandler;
	}

	public void setMeetHandler(Handler meetHandler) {
		this.meetHandler = meetHandler;
	}
	//会议日程首页handler
	private Handler meetHandler;
	private Handler stationHandler;

	public Handler getStationHandler() {
		return stationHandler;
	}

	public void setStationHandler(Handler stationHandler) {
		this.stationHandler = stationHandler;
	}

	public void onCreate() {
		super.onCreate();
		context = this;
		try{
			FIR.init(this);//bugHD 初始化
//			UEHandler ueHandler = new UEHandler();
			// 设置异常处理实例
//			Thread.setDefaultUncaughtExceptionHandler(ueHandler);
			initImageLoader(this.getApplicationContext());
			SDKInitializer.initialize(getApplicationContext());
		}catch(Exception e){
			JLog.e(e);
		}

		//配置Cookie(包含Session)
		CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(getApplicationContext()));
		//设置可访问所有的https网站
		HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);

		OkHttpClient okHttpClient = new OkHttpClient.Builder()
				.connectTimeout(10000L, TimeUnit.MILLISECONDS)
				.readTimeout(10000L, TimeUnit.MILLISECONDS)
				//其他配置
				.cookieJar(cookieJar)
				//配置Log
				.addInterceptor(new LoggerInterceptor("TAG"))
				//https
				.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
				.build();

		OkHttpUtils.initClient(okHttpClient);

		//开启一个服务，用于会议提醒
		eventsList = new ArrayList<>();
		startService(new Intent(this, NotificationServices.class));
	}


	public void addActivityToHeap(Activity activity) {
		heapList.add(activity);
	}

	public void removeActivityFromHeap(Activity activity) {
		if (heapList.contains(activity)) {
			heapList.remove(activity);
		}
	}

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)
		// .threadPriority(Thread.NORM_PRIORITY - 2) //设置线程的优先级
				/**
				 * 当同一个Uri获取不同大小的图片，缓存到内存时，只缓存一个。默认会缓存多个不同的大小的相同图片
				 */
				.denyCacheImageMultipleSizesInMemory() // 拒绝内存的缓存多种尺寸图片
				.discCacheFileNameGenerator(new Md5FileNameGenerator()) // 图片名字用URI
																		// MD5命名
				.tasksProcessingOrder(QueueProcessingType.LIFO) // 设置图片下载和显示的工作队列排序
				.threadPoolSize(4).writeDebugLogs() // Not necessary in common
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	/**
	 * 捕获系统异常
	 * 
	 * @author houyu
	 * 
	 */
	private class UEHandler implements Thread.UncaughtExceptionHandler {

		@Override
		public void uncaughtException(Thread thread, Throwable ex) {

			/** 以下为将捕获的异常信息，格式化输出到文件中 */

			// create file
			String info = null;
			ByteArrayOutputStream baos = null;
			PrintStream printStream = null;
			try {
				baos = new ByteArrayOutputStream();
				printStream = new PrintStream(baos);
				ex.printStackTrace(printStream);
				byte[] data = baos.toByteArray();
				info = new String(data);
				data = null;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (printStream != null) {
						printStream.close();
					}
					if (baos != null) {
						baos.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// print info
			long threadId = thread.getId();
			Log.e("ANDROID_LAB", "Thread.getName()=" + thread.getName()
					+ " id=" + threadId + " state=" + thread.getState());
			Log.e("ANDROID_LAB", "Error[" + info + "]");
			Log.e("ANDROID_LAB",
					"sdcard =>" + Environment.getExternalStorageState());
			JLog.d(info);
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) { // sd卡是否挂载
				// 打印log
				PublicUtils.writeErrorLog(SoftApplication.this, info,
						"SoftApplication");
				// softApp.startService(new Intent(softApp,
				// UploadService.class));
				// 上传log
				PublicUtils.uploadLog(SoftApplication.this, "log");
			}
			// kill App Progress
			android.os.Process.killProcess(android.os.Process.myPid());

		}
	}

	public static SoftApplication getInstance(){
		return (SoftApplication)context;
	}


	public HomePageActivity getHomePageActivity() {
		return homePageActivity;
	}

	public void setHomePageActivity(HomePageActivity homePageActivity) {
		this.homePageActivity = homePageActivity;
	}

	public HomeMenuFragment getHomeMenuFragment() {
		return homeMenuFragment;
	}

	public void setHomeMenuFragment(HomeMenuFragment homeMenuFragment) {
		this.homeMenuFragment = homeMenuFragment;
	}

	public List<Event> getEventsList() {
		return eventsList;
	}


	public void setEventsToList(Event event) {
		for (Event e : eventsList) {
			if (e.getMeetingID() == event.getMeetingID()){
				break;
			}
		}
		this.eventsList.add(event);
	}
}