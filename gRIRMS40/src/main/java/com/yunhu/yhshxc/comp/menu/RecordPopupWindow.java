package com.yunhu.yhshxc.comp.menu;

import gcg.org.debug.JLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.Module;
import com.yunhu.yhshxc.bo.Submit;
import com.yunhu.yhshxc.bo.SubmitItem;
import com.yunhu.yhshxc.database.SubmitDB;
import com.yunhu.yhshxc.database.SubmitItemDB;
import com.yunhu.yhshxc.database.SubmitItemTempDB;
import com.yunhu.yhshxc.func.AbsFuncActivity;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.widget.ToastOrder;

/**
 * 录音页面
 * @author gcg_jishen
 */
public class RecordPopupWindow implements OnClickListener,OnTouchListener{
	private final String TAG = "RecordPopupWindow";
	private View recordView;// 录音页面view
	public ImageView record_view;// 正在录音显示view 播放录音view
	public TextView record_time;// 录音时间
	private ImageView record_back;// 返回按钮
	private ImageView record_btn;// 录音触发按钮
	private ImageView record_save;// 保存录音
	private MediaPlayer mediaPlayer;// 录音播放器
	private MediaRecorder mediaRecorder;// 录音
	public File audioFile;// 录音问及那
	private int MAX_TIME;// 最大录音时间
	private Context context;
	public int CURRENT_RECORD_STATE;// 当前录音的状态 默认0未录音
	private final int RECORDIND = 1;// 正在录音
	public final int RECORD_STOP = 2;// 录音完毕
	private final int RECORD_PLAY = 3;// 播放录音
	private final int RECORD_PAUSE = 4;// 暂停播放录音
	
	public LinearLayout ll_recod;
	
	/**
	 * 用于显示帮助的弹出窗口
	 */
	public PopupWindow popupWindow = null;
	private AbsFuncActivity absFuncActivity;
	private Bundle bundle;
	private Func func;
	private Module module;
	/** Timer对象 **/
	private Timer timer = null; // 录音计时
	private int time;// 录音时间
	public String fileName;
	private Integer wayId;
	private Integer planId;
	private Integer storeId;
	private Integer targetid;
	private Integer targetType;
	private String storeName;
	private String wayName;
	private boolean isLink;
	public LinearLayout ll_say;
	private OnRefreshListner lisner ;

	public RecordPopupWindow(Context mContext, Bundle bundle, Func func,OnRefreshListner lisner) {
		this.context = mContext;
		this.bundle = bundle;
		this.func = func;
		this.lisner = lisner;
		absFuncActivity = (AbsFuncActivity) context;
		module = (Module) bundle.getSerializable("module");// 模块实例
		wayId = bundle.getInt("wayId");// 线路ID
		planId = bundle.getInt("planId");// 计划ID
		storeId = bundle.getInt("storeId");// 店面ID
		storeName = bundle.getString("storeName");// 店面名称
		wayName = bundle.getString("wayName");// 线路名称
		// 赋值
		targetid = func.getTargetid();// 数据ID
		targetType = bundle.getInt("targetType");// 模块类型
		isLink = bundle.getBoolean("isLink");// 是否是超链接 true 是 false否
		String voiceTime=SharedPreferencesUtil.getInstance(mContext).getVoiceTime();
		MAX_TIME=Integer.parseInt(voiceTime)*60*1000;
		initView(mContext);
	}
	
	public RecordPopupWindow(Context mContext) {
		this.context = mContext;
		initView(mContext);
	}
	
	private void initView(Context mContext){
		recordView = View.inflate(mContext, R.layout.record_view, null);
		record_view = (ImageView) recordView.findViewById(R.id.iv_record_view);
		record_time = (TextView) recordView.findViewById(R.id.tv_record_time);
		record_back = (ImageView) recordView.findViewById(R.id.iv_record_back);
		record_btn = (ImageView) recordView.findViewById(R.id.iv_record_btn);
		record_save = (ImageView) recordView.findViewById(R.id.iv_record_save);
		ll_say = (LinearLayout)recordView.findViewById(R.id.ll_say);
		ll_recod = (LinearLayout)recordView.findViewById(R.id.ll_recod);
		record_view.setOnClickListener(this);
		record_back.setOnClickListener(this);
		record_save.setOnClickListener(this);
		record_btn.setOnTouchListener(this);
	}
	
	/**
	 * 单击事件
	 */
	@Override
	public void onClick(View v) {
		try {
			switch (v.getId()) {
			case R.id.iv_record_view:
				if (CURRENT_RECORD_STATE == RECORD_STOP || CURRENT_RECORD_STATE==RECORD_PAUSE) {
					playRecord();
				}else if(CURRENT_RECORD_STATE == RECORD_PLAY){
					pausePlay();
				}
				break;
			case R.id.iv_record_back:
				back();
				break;
			case R.id.iv_record_save:
				if(!TextUtils.isEmpty(fileName) && CURRENT_RECORD_STATE!=0){
					save(fileName);
				}else{
					ToastOrder.makeText(context, context.getResources().getString(R.string.you_not_have_recordings), ToastOrder.LENGTH_SHORT).show();
				}
				break;

			default:
				break;
			}
		} catch (Exception e) {
			playExection();
			JLog.d(TAG, "e:"+e.getMessage());
		}
	}

	/**
	 * 触摸事件
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		try {
			switch (event.getAction()) {
			case MotionEvent.ACTION_UP://手指松开的时候停止录音
				stopRecord(false);
				if(time<=1000){//如果录音时间小于1秒则认为录音无效
					CURRENT_RECORD_STATE = 0;
					record_view.setBackgroundResource(R.drawable.record_voice);
					record_save.setBackgroundResource(R.drawable.record_confirm2);
					record_btn.setBackgroundResource(R.drawable.record_btn1);
					record_time.post(new Runnable() {
						
						@Override
						public void run() {
							record_time.setText("");
						}
					});
					ToastOrder.makeText(context, context.getResources().getString(R.string.recordings_is_too_short), ToastOrder.LENGTH_SHORT).show();
				}
				break;
			case MotionEvent.ACTION_DOWN://手指按下的时候开始录音
				if(CURRENT_RECORD_STATE==RECORD_PLAY){
					pausePlay();
				}
				startRecord();
				break;

			default:
				break;
			}
		} catch (Exception e) {
//			JLog.d(TAG, e.getMessage());
		}
		return false;
	}

	/**
	 * 开始录音
	 * 
	 * @throws Exception
	 */
	private void startRecord() throws Exception {
		File file = new File(Constants.RECORD_PATH);
		if (!file.exists()) {
			file.mkdirs();
		}
		mediaRecorder = new MediaRecorder();
		if(mediaPlayer!=null){
			mediaPlayer.release();
			mediaPlayer=null;
		}
		// 设置音频来源(一般为麦克风)
		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		// 设置音频输出格式（默认的输出格式）
		mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		// 设置音频编码方式（默认的编码方式）
		mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		// 设置最大录音时间
		mediaRecorder.setMaxDuration(MAX_TIME);
		fileName = System.currentTimeMillis() + ".3gp";
		String path = Constants.RECORD_PATH + fileName;
		Log.d(TAG, "path:" + path);
		audioFile = new File(path);
		if (audioFile.exists()) {
			audioFile.delete();
		}
		audioFile.createNewFile();
		mediaRecorder.setOutputFile(audioFile.getAbsolutePath());
		mediaRecorder.prepare();
		mediaRecorder.start();
		startTimer(100);
		CURRENT_RECORD_STATE = RECORDIND;// 正在录音
		record_view.setBackgroundResource(R.drawable.record_voice);
		record_save.setBackgroundResource(R.drawable.record_confirm2);
		record_btn.setBackgroundResource(R.drawable.record_btn2);
		record_time.setText("");
		updateMicStatus();
	}

	/**
	 * 停止录音
	 */
	public void stopRecord(boolean isOnPause) {
		if (audioFile != null && mediaRecorder!=null&&CURRENT_RECORD_STATE==RECORDIND) {
			CURRENT_RECORD_STATE = RECORD_STOP;// 停止录音
			record_view.setBackgroundResource(R.drawable.record_play_btn);
			record_save.setBackgroundResource(R.drawable.record_confirm1);
			record_btn.setBackgroundResource(R.drawable.record_btn1);       
			mediaRecorder.stop();
			mediaRecorder.release();
			mediaRecorder = null;
			cancelTimer();
		}
	}

	/**
	 * 播放录音
	 * 
	 * @throws Exception
	 * @throws SecurityException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	private void playRecord() throws Exception, SecurityException,
			IllegalStateException, IOException {
		if (audioFile != null) {
			CURRENT_RECORD_STATE = RECORD_PLAY;// 播放录音
			record_time.setText("正在播放");
			record_view.setBackgroundResource(R.drawable.record_play_btn);
			if(mediaPlayer==null){
				mediaPlayer = new MediaPlayer();
				FileInputStream fis = new FileInputStream(audioFile); 
				mediaPlayer.setDataSource(fis.getFD());
				JLog.d(TAG, "path:"+audioFile.getAbsolutePath());
				mediaPlayer.prepare();
			}
			
			mediaPlayer.start();
			
			mediaPlayer.setOnErrorListener(new OnErrorListener() {
				
				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					JLog.d(TAG, "播放失败 setOnErrorListener what:"+what+"extra:"+extra);
					playExection();
					return false;
				}
			});
			
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					JLog.d(TAG, "播放完毕 setOnCompletionListener");
					CURRENT_RECORD_STATE=RECORD_STOP;
					record_view.setBackgroundResource(R.drawable.record_play_btn);
					record_time.setText(context.getResources().getString(R.string.play_finifh));
				}
			});

		}
	}
	
	/**
	 * 退出
	 */
	public void back(){
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
		}
		pausePlay();
	}
	
	/**
	 * 暂停播放
	 */
	public void pausePlay(){
		if(mediaPlayer!=null && mediaPlayer.isPlaying()){
			CURRENT_RECORD_STATE=RECORD_PAUSE;
			mediaPlayer.pause();
			record_view.setBackgroundResource(R.drawable.record_pause_btn);
			record_time.setText(context.getResources().getString(R.string.suspend));
		}
	} 

	/**
	 * 停止播放
	 */
	private void playExection(){
		CURRENT_RECORD_STATE = RECORD_STOP;// 停止录音
		record_view.setBackgroundResource(R.drawable.record_play_btn);
		record_btn.setBackgroundResource(R.drawable.record_btn1);
		record_time.setText(context.getResources().getString(R.string.play_exceptions));
		ToastOrder.makeText(context, context.getResources().getString(R.string.this_recording_is_error), ToastOrder.LENGTH_SHORT).show();
		if (mediaPlayer != null) {// 释放播放器
//			mediaPlayer.reset();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}
	/**
	 * 开始计时器
	 * 
	 * @param
	 */
	public void startTimer(long intervalTime) {
		time = 0;
		cancelTimer();
		timer = new Timer();
		timer.schedule(getTask(), 0, intervalTime);
	}

	/**
	 * 关闭计时器
	 */
	public void cancelTimer() {
		if (timer != null) {
			timer.cancel();
		}
	}

	/**
	 * 录音执行任务
	 * 
	 * @return
	 */
	public TimerTask getTask() {
		return new TimerTask() {
			@Override
			public void run() {
				time+=100;
				if (time >= Integer.parseInt(SharedPreferencesUtil.getInstance(context).getVoiceTime())*60*1000) {
					timeHandler.sendEmptyMessage(0);
				} else {
					timeHandler.sendEmptyMessage(1);
				}
			}
		};
	}

	/**
	 * 录音时间改变timeHandler
	 */
	private Handler timeHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			record_time.setText(time/1000 + "  ＂");
			if (msg.what == 0) {
				stopRecord(false);
			}
		};
	};

	/**
	 * 显示录音的弹出窗口，基于R.layout.record_view来构造界面
	 * 
	 * @param anchor
	 *            弹出窗口所基于的view。如果anchor为null，则基于R.id.ll_btn_layout组件的位置显示，
	 *            否则就基于anchor组件的位置显示
	 */
	public void show(View anchor) {
		// 根据屏幕高度/2.5的值来设置弹出窗口的高度
		int pwHeight = PublicUtils.convertPX2DIP(context, (Double.valueOf(Math
				.floor(Constants.SCREEN_HEIGHT / 2.5))).intValue());
		popupWindow = new PopupWindow(recordView,
				WindowManager.LayoutParams.FILL_PARENT,
				WindowManager.LayoutParams.FILL_PARENT, true);
//		popupWindow.setAnimationStyle(R.style.popupwindow);
		popupWindow.setFocusable(true);
		// popupWindow.setOutsideTouchable(true);
		popupWindow.update();// 更新弹出窗口的状态
		popupWindow.setBackgroundDrawable(new BitmapDrawable());// 设置一个默认的背景
		// 设置弹出窗口的位置（基于哪个背景层组件显示）
		if (anchor == null) {
			// 设置弹出窗口的位置为水平居中且对齐R.id.gridview_home组件的底部
			popupWindow.showAtLocation(((Activity) context).findViewById(R.id.ll_btn_layout),
					Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
		} else {
			// 根据设置弹出窗口的位置
			int nativeBar = getNavigationBarHeight(context);
			LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ll_say.getLayoutParams();
			params.setMargins(0,0,0,nativeBar);
			ll_say.setLayoutParams(params);
			int y = Constants.SCREEN_HEIGHT - anchor.getHeight() - pwHeight-nativeBar;
			popupWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, 0, y);
		}
		((Activity) context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//保持屏幕
		init();

	}
	//获取虚拟按键的高度
	public  int getNavigationBarHeight(Context context) {
		int result = 0;
		WindowManager manager = ((WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE));
		if (hasSoftKeys(manager)) {
			Resources res = context.getResources();
			int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
			if (resourceId > 0) {
				result = res.getDimensionPixelSize(resourceId);
			}
		}
		return result;
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	private boolean hasSoftKeys(WindowManager windowManager){
		Display d = windowManager.getDefaultDisplay();


		DisplayMetrics realDisplayMetrics = new DisplayMetrics();
		d.getRealMetrics(realDisplayMetrics);


		int realHeight = realDisplayMetrics.heightPixels;
		int realWidth = realDisplayMetrics.widthPixels;


		DisplayMetrics displayMetrics = new DisplayMetrics();
		d.getMetrics(displayMetrics);


		int displayHeight = displayMetrics.heightPixels;
		int displayWidth = displayMetrics.widthPixels;


		return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
	}

	/**
	 * 将数据保存到数据库
	 */
	public void save(String value) {
		SubmitDB submitDB = new SubmitDB(context);
		SubmitItemDB submitItemDB = new SubmitItemDB(context);
		SubmitItemTempDB linkSubmitItemTempDB = new SubmitItemTempDB(context);
		SubmitItem submitItem = new SubmitItem();
		int submitId = submitDB.selectSubmitIdNotCheckOut(
				absFuncActivity.planId, absFuncActivity.wayId,
				absFuncActivity.storeId, targetid, targetType,
				Submit.STATE_NO_SUBMIT);
		if (submitId != Constants.DEFAULTINT) {
			submitItem.setParamName(String.valueOf(func.getFuncId()));
			submitItem.setParamValue(value);
			submitItem.setSubmitId(submitId);
			submitItem.setType(func.getType());
			submitItem.setOrderId(func.getId());
			submitItem.setIsCacheFun(func.getIsCacheFun());
			boolean isHas = false;
			if (isLink) {// 超链接的时候操作临时表中的数据
				isHas = linkSubmitItemTempDB.findIsHaveParamName(
						submitItem.getParamName(), submitId);
			} else {
				isHas = submitItemDB.findIsHaveParamName(
						submitItem.getParamName(), submitId);
			}
			if (isHas) {// true表示已经操作过，此时更新数据库
				if (TextUtils.isEmpty(value)) {// 如果值是空就修改为空
					submitItem.setParamValue("");
				}

				if (isLink) {// 超链接
					linkSubmitItemTempDB.updateSubmitItem(submitItem);
				} else {
					submitItemDB.updateSubmitItem(submitItem,false);
				}
			} else {
				if (!TextUtils.isEmpty(value)) {// 没有该项，并且值不为空就插入
					if (isLink) {
						linkSubmitItemTempDB.insertSubmitItem(submitItem);
					} else {
						submitItemDB.insertSubmitItem(submitItem,false);
					}
				}
			}

		} else {
			if (!TextUtils.isEmpty(value)) {
				Submit submit = new Submit();
				submit.setPlanId(planId);
				submit.setState(Submit.STATE_NO_SUBMIT);
				submit.setStoreId(storeId);
				submit.setStoreName(storeName);
				submit.setWayName(wayName);
				submit.setWayId(wayId);
				submit.setTargetid(targetid);
				submit.setTargetType(targetType);
				if (module != null) {
					submit.setModType(module.getType());
				}
				submit.setMenuId(menuId);
				submit.setMenuType(menuType);
				submit.setMenuName(menuName);
				submitDB.insertSubmit(submit);
				int currentsubmitId = submitDB.selectSubmitIdNotCheckOut(
						planId, wayId, storeId, targetid, targetType,
						Submit.STATE_NO_SUBMIT);
				submitItem.setParamName(String.valueOf(func.getFuncId()));
				submitItem.setType(func.getType());
				submitItem.setParamValue(value);
				submitItem.setSubmitId(currentsubmitId);
				submitItem.setOrderId(func.getId());
				submitItem.setIsCacheFun(func.getIsCacheFun());
				if (isLink) {
					linkSubmitItemTempDB.insertSubmitItem(submitItem);
				} else {
					submitItemDB.insertSubmitItem(submitItem,false);
				}
			}

		}
		if (TextUtils.isEmpty(value) && absFuncActivity != null) {
			absFuncActivity.inputUnOperatedMap(func);
//			absFuncActivity.setShowHook(false, absFuncActivity.hookView);
		} else {
			absFuncActivity.inputOperatedMap(func);
//			absFuncActivity.setShowHook(true, absFuncActivity.hookView);
		}
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
		}
		lisner.onRefreshRecore();
		pausePlay();
	}
	
	public interface OnRefreshListner{
		public void onRefreshRecore();
	}
	
	/**
	 * 初始化
	 */
	public void init(){
		SubmitItem item=null;
		if(isLink){//超链接模块中的控件 查询临时表
			item=new SubmitItemTempDB(context).findSubmitItem(null, Submit.STATE_NO_SUBMIT, planId, 
					wayId, storeId, targetid, targetType, String.valueOf(func.getFuncId()));
		}else{
			item=new SubmitItemDB(context).findSubmitItem(null, Submit.STATE_NO_SUBMIT, planId, 
					wayId, storeId, targetid, targetType, String.valueOf(func.getFuncId()));
		}
		if(item!=null ){//不是查询条件，从数据库中读取数据
			String itemValue = item.getParamValue();// 将数据库中查出的值赋给当前组件
			if(!TextUtils.isEmpty(itemValue)){
				audioFile=new File(Constants.RECORD_PATH+itemValue);
				if(audioFile.exists()){//如果文件存在
					record_view.setBackgroundResource(R.drawable.record_play_btn);
					record_time.setText(context.getResources().getString(R.string.play1));
					CURRENT_RECORD_STATE = RECORD_STOP;
				}
			}
		}
	}

	/**
	 * 更新话筒状态 分贝是也就是相对响度 分贝的计算公式K=20lg(Vo/Vi) Vo当前振幅值 Vi基准值为600：我是怎么制定基准值的呢？ 当20
	 * Math.log10(mMediaRecorder.getMaxAmplitude() / Vi)==0的时候vi就是我所需要的基准值
	 * 当我不对着麦克风说任何话的时候，测试获得的mMediaRecorder.getMaxAmplitude()值即为基准值。
	 * Log.i("mic_", "麦克风的基准值：" + mMediaRecorder.getMaxAmplitude());前提时不对麦克风说任何话
	 */
	private int BASE = 600;
	private int SPACE = 300;// 间隔取样时间

	private void updateMicStatus() {
		if (mediaRecorder != null) {
			// int vuSize = 10 * mMediaRecorder.getMaxAmplitude() / 32768;
			/*
			 * if (db > 1) { vuSize = (int) (20 * Math.log10(db)); Log.i("mic_",
			 * "麦克风的音量的大小：" + vuSize); } else Log.i("mic_", "麦克风的音量的大小：" + 0);
			 */
			int ratio = mediaRecorder.getMaxAmplitude() / BASE;
			int db = 0;// 分贝
			if (ratio > 1) {
				db = (int) (20 * Math.log10(ratio));
			}
			JLog.d(TAG, "db:" + db);
			switch (db / 4) {
			case 0:
				record_view.setBackgroundResource(R.drawable.record_voice_0);
				break;
			case 1:
				record_view.setBackgroundResource(R.drawable.record_voice_1);
				break;
			case 2:
				record_view.setBackgroundResource(R.drawable.record_voice_2);
				break;
			case 3:
				record_view.setBackgroundResource(R.drawable.record_voice_1);
				record_view.setBackgroundResource(R.drawable.record_voice_3);
				break;
			case 4:
				record_view.setBackgroundResource(R.drawable.record_voice_1);
				record_view.setBackgroundResource(R.drawable.record_voice_4);
				break;
			case 5:
				record_view.setBackgroundResource(R.drawable.record_voice_2);
				record_view.setBackgroundResource(R.drawable.record_voice_5);
				break;
			default:
				record_view.setBackgroundResource(R.drawable.record_voice_3);
				record_view.setBackgroundResource(R.drawable.record_voice_6);
				break;
			}
			 mHandler.postDelayed(mUpdateMicStatusTimer, SPACE);
		}
	}
	
	/**
	 * 捕获当前录音音量大小
	 */
	 private final Handler mHandler = new Handler();
	 private Runnable mUpdateMicStatusTimer = new Runnable() {
	 public void run() {
		 updateMicStatus();
		}
	 };

	/**
	 * 来电话的时候调用
	 */
	public void pauseRecord(){
		stopRecord(true);
	}
	private int menuId;
	private int menuType;
	private String menuName;

	public int getMenuId() {
		return menuId;
	}
	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}
	public int getMenuType() {
		return menuType;
	}
	public void setMenuType(int menuType) {
		this.menuType = menuType;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
}
