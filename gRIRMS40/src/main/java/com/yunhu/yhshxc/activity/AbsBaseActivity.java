package com.yunhu.yhshxc.activity;

import gcg.org.debug.JLog;

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import cn.jpush.android.api.JPushInterface;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.login.LoginActivity;
import com.yunhu.yhshxc.activity.login.LoginFor4GActivity;
import com.yunhu.yhshxc.application.SoftApplication;
import com.yunhu.yhshxc.http.CoreHttpHelper;
import com.yunhu.yhshxc.service.CheckExitAppService;
import com.yunhu.yhshxc.submitManager.bo.PendingRequestVO;
import com.yunhu.yhshxc.submitManager.bo.TablePending;
import com.yunhu.yhshxc.submitManager.core.SubmitWorkManager;
import com.yunhu.yhshxc.utility.ApplicationHelper;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.CroutonUtil;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;

/**
 * 所有页面activity的基类 主要是保存和都去一些公用的常量 以及判断应用程序是否在前台执行
 * 
 * @author gcg_jishen
 *
 *
 */
public abstract class AbsBaseActivity extends Activity implements OnClickListener {


	public final String TAG = this.getClass().getSimpleName();

	protected boolean isNormal = true; // 适配特殊机型所用-是否正常进入 true表示是正常进入
										// false表示非正常进入
	public SoftApplication application;//
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		application = (SoftApplication) this.getApplication();
		if (savedInstanceState != null) {// 非正常关闭
			restoreConstants(savedInstanceState);
			isNormal = false;
		}
		registScreenStateReceiver();
		application.addActivityToHeap(this);

		 initJPush();
	}

	// 注册屏幕改变广播
	private void registScreenStateReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_USER_PRESENT);
		registerReceiver(screenStatusReceiver, filter);
	}

	/**
	 * 初始化极光推送
	 */
	private void initJPush() {
		JPushInterface.init(getApplicationContext());
		JPushInterface.setLatestNotificationNumber(this, 1);// 保留最新的一条通知
		if (JPushInterface.isPushStopped(getApplicationContext())) {// 判断如果推送停止了，就恢复
			JPushInterface.resumePush(getApplicationContext());
		}
	}

	/**
	 * 注册屏幕变化广播
	 */
	private void registerExiteReceiver() {
		// 注册退出应用广播
		IntentFilter filter1 = new IntentFilter();
		filter1.addAction(CheckExitAppService.GCG_ANDROID_GRIRMS_INIT_ACTION);
		registerReceiver(exitAppReceiver, filter1);
		application.isRegisterExitReceiver = true;
	}

	/**
	 * 取消退出广播
	 */
	private void unregisterExiteReceiver() {
		if (application.isRegisterExitReceiver) {
			try {
				application.isRegisterExitReceiver = false;
				SharedPreferencesUtil.getInstance(this).setLastRunTime("");// 进入前台运行的时候把最后运行时间清空
				unregisterReceiver(exitAppReceiver);// 程序在前端运行的时候取消广播注册
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 暂时不使用
	 */
	public void initBase() {

	}

	/**
	 * 应用异常返回的时候都去存储的常量
	 * 
	 * @param savedInstanceState
	 */
	protected void restoreConstants(Bundle savedInstanceState) {
		Constants.CURRENT_VERSION = savedInstanceState.getString("CURRENT_VERSION");
		Constants.SCREEN_WIDTH = savedInstanceState.getInt("SCREEN_WIDTH");
		Constants.SCREEN_HEIGHT = savedInstanceState.getInt("SCREEN_HEIGHT");
		Constants.PLANNUMBER = savedInstanceState.getInt("PLANNUMBER");
		Constants.TASKNUMBER = savedInstanceState.getInt("TASKNUMBER");
		Constants.NOTICENUMBER = savedInstanceState.getInt("NOTICENUMBER");
		Constants.DOUBLENUMBER = savedInstanceState.getInt("DOUBLENUMBER");

		Constants.hasReplenish = savedInstanceState.getBoolean("hasReplenish");
		Constants.ISCHECKOUT = savedInstanceState.getBoolean("ISCHECKOUT");
		Constants.ISCHECKOUTMODUL = savedInstanceState.getBoolean("ISCHECKOUTMODUL");

		application.isActive = savedInstanceState.getBoolean("base_isActive");
		application.isSubmitActive = savedInstanceState.getBoolean("base_isSubmitActive");

	}

	@Override
	protected void onStart() {

		super.onStart();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {

		if (application.isSubmitActive && !application.isActive) {
			// JLog.d("888", "进入前台运行...");
			application.isActive = true;
			// submitActiveData(application.isActive);
			unregisterExiteReceiver();
		}
		registerReceiver();// 注册待办事项广播

		super.onResume();
	}

	@Override
	protected void onPause() {
		unregisterReceiver(receiver);// 取消注册待办事项广播
		super.onPause();
	}

	@Override
	protected void onStop() {
		if (application.isSubmitActive && !isAppOnForeground()) {
			// JLog.d("888", "进入后台运行...");
			application.isActive = false;
			// submitActiveData(application.isActive);
			registerExiteReceiver();
			startCheckInitService();
		}

		super.onStop();
	}

	/**
	 * 开启后台检测是否要初始化服务
	 */
	private void startCheckInitService() {
		SharedPreferencesUtil.getInstance(this).setLastRunTime(DateUtil.getCurDateTime());
		Intent intent = new Intent(this, CheckExitAppService.class);
		startService(intent);
	}

	/**
	 * 退出系统广播
	 */
	private BroadcastReceiver exitAppReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			JLog.d("*******************EXIT*********************" + AbsBaseActivity.this.getClass().getName());
			for (int i = 0; i < application.heapList.size(); i++) {
				Activity activity = application.heapList.get(i);
				if (activity != null) {
					activity.finish();
				}
			}
		}
	};

	@Override
	protected void onDestroy() {
		application.removeActivityFromHeap(this);
		try {
			if (application.heapList.isEmpty() && application.isRegisterExitReceiver) {
				unregisterExiteReceiver();
			}
			unregisterReceiver(screenStatusReceiver);// 关闭页面的时候就取消注册
		} catch (Exception e) {
			e.printStackTrace();
		}

		super.onDestroy();

	}

	@Override
	public void onClick(View v) {

	}

	public void refreshUI() {

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 存储部分公用常量 拍照异常返回的时候要读取
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("CURRENT_VERSION",
				ApplicationHelper.getApplicationName(getApplicationContext()).appVersionName);
		outState.putInt("SCREEN_WIDTH", Constants.SCREEN_WIDTH);
		outState.putInt("SCREEN_HEIGHT", Constants.SCREEN_HEIGHT);
		outState.putInt("PLANNUMBER", Constants.PLANNUMBER);
		outState.putInt("TASKNUMBER", Constants.TASKNUMBER);
		outState.putInt("NOTICENUMBER", Constants.NOTICENUMBER);
		outState.putInt("DOUBLENUMBER", Constants.DOUBLENUMBER);

		outState.putBoolean("hasReplenish", Constants.hasReplenish);
		outState.putBoolean("ISCHECKOUT", Constants.ISCHECKOUT);
		outState.putBoolean("ISCHECKOUTMODUL", Constants.ISCHECKOUTMODUL);
		outState.putBoolean("base_isActive", application.isActive);
		outState.putBoolean("base_isSubmitActive", application.isSubmitActive);
	}

	/**
	 * 程序是否在后台运行
	 * 
	 * @return true表示程序正在运行 false表示没有运行
	 */
	public boolean isAppOnForeground() {
		ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(ACTIVITY_SERVICE);
		String packageName = getApplicationContext().getPackageName();

		List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		if (appProcesses == null)
			return false;

		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 当应用进入前台和退到后台的时候把状态提交给服务器
	 * 
	 * @param isActive
	 *            true表示在前台运行 false表示应用在后台
	 */
	private void submitActiveData1(boolean isActive) {
		if (!(this instanceof SplashActivity2) && !(this instanceof LoginFor4GActivity)
				&& !(this instanceof LoginActivity) && !(this instanceof InitActivity)) {
			try {
				HashMap<String, String> actionMap = new HashMap<String, String>();
				String type = isActive ? "8001" : "8002";
				String time = DateUtil.getCurDateTime();
				String phoneno = PublicUtils.receivePhoneNO(this);
				JSONArray actionArray = new JSONArray();
				JSONObject actionObj = new JSONObject();
				actionObj.put("type", type);
				actionObj.put("time", time);
				actionArray.put(actionObj);
				actionMap.put("mobile", phoneno);
				actionMap.put("action", actionArray.toString());

				PendingRequestVO vo = new PendingRequestVO();
				vo.setContent("当前状态");
				vo.setTitle("状态");
				vo.setMethodType(SubmitWorkManager.HTTP_METHOD_POST);
				vo.setParams(actionMap);
				vo.setType(TablePending.TYPE_ACTIVE);
				vo.setUrl(getResources().getString(R.string.URL_PHONE_ACTION));
				new CoreHttpHelper().performJustSubmit(this, vo);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	/***
	 * 待办事项广播注册
	 */
	private void registerReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.BROADCAST_ACTION_WAITING_PROCESS);
		registerReceiver(receiver, filter);
	}

	/**
	 * 代办事项提醒广播
	 */
	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String message = null;
			if (intent != null) {
				message = intent.getStringExtra(Constants.BROADCAST_ACTION_WAITING_PROCESS);
			}
			if (!TextUtils.isEmpty(message)) {
				CroutonUtil.showCrouton(AbsBaseActivity.this, message);
				refreshUI();
			}
		}
	};

	/**
	 * 屏幕关闭和打开广播监听
	 */
	private BroadcastReceiver screenStatusReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction()) && isAppOnForeground()) {// 黑屏的时候并且应用处于打开状态开启时间检测
				registerExiteReceiver();
				startCheckInitService();
			} else if (Intent.ACTION_USER_PRESENT.equals(intent.getAction()) && isAppOnForeground()) {// 解锁了
				unregisterExiteReceiver();
			}
		}

	};

	


}