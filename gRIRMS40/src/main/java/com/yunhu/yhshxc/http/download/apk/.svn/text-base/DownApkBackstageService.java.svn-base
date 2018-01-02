package com.yunhu.yhshxc.http.download.apk;

import gcg.org.debug.JLog;

import java.io.File;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.utility.ApplicationHelper;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.MD5Helper;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;

/**
 * 后台下载APK的服务
 * @author jishen
 *
 */
public class DownApkBackstageService extends Service {

	private String TAG = "DownApkService";
	/**
	 * 下载的url
	 */
	private String urlstr = null;
	/**
	 * 正下载的apk的MD5
	 */
	private String md5 = null;
	/**
	 * 最新版本号
	 */
	private String newVersion = null;


	private boolean isFromHome = false;//从home页面开启的服务才提示安装
	@Override
	public void onCreate() {

		super.onCreate();
//		writeDownInfo("下载服务开启        "+DateUtil.getCurDateTime()+"\n");
		init();
	}
	
	/**
	 * 获取下载url MD5 最新版本号和本地版本号
	 */
	private void init(){
		urlstr = SharedPreferencesUtil.getInstance(this).getDownUrl().trim();
		md5 = SharedPreferencesUtil.getInstance(this).getMD5Code().trim();
		newVersion = SharedPreferencesUtil.getInstance(this).getNewVersion().trim();
		oldVersion = ApplicationHelper.getApplicationName(this).appVersionName;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent!=null) {
			isFromHome = intent.getBooleanExtra("isFromHome", false);
		}
		JLog.d(TAG, "下载服务开启:isFromHome:"+isFromHome);
		
		if(!TextUtils.isEmpty(urlstr) && !TextUtils.isEmpty(md5) && isNeedUpdate(oldVersion, newVersion)){//验证是否需要下载
			if (new File(Constants.DOWN_NEWAPK_PATH).exists() && isPassMd5(Constants.DOWN_NEWAPK_PATH)) {// 判断是否需要下载 true 表示需要下载 文件不存在或者MD5比较不相同
				installApp();//弹出安装对话框
			} else {
				init();
				downLoadApplication();//开始下载
			}
		}else{
			stopSelf();//如果不需要下载关闭服务
		}
		
		return Service.START_STICKY;
		
	}

	/**
	 * 利用消息处理机制适时更新进度条
	 */
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			int fileSize=Integer.parseInt(SharedPreferencesUtil.getInstance(DownApkBackstageService.this).getApkSize().trim());
			if (msg.what == 1) {
				int compeleteSize = msg.arg1;
				JLog.d(TAG, "已下载的文件大小==>" + compeleteSize+"  文件大小" + fileSize);
//				String downInfo="已下载大小==>"+compeleteSize+"  "+"时间==>"+DateUtil.getCurDateTime()+"\n";
//                writeDownInfo(downInfo);
				// 设置进度条按读取的length长度更新
				if (compeleteSize == fileSize || msg.arg2==-1) {
					JLog.d(TAG, "下载完成");
					String hash;//下载的文件的MD5
					try {
						hash = MD5Helper.getMD5Checksum(Constants.DOWN_NEWAPK_PATH);
						// 下载完成
						if (hash.toUpperCase().equals(md5.toUpperCase())) {//MD5验证通过
							JLog.d(TAG, "MD5验证通过");
//							String s1="下载完成，MD5验证通过"+DateUtil.getCurDateTime()+"\n";
//							writeDownInfo(s1);
//							installApp();
						} else {
							
							// 下载完成后清除进度条并将map中的数据清空
							JLog.d(TAG, "MD5验证不通过");
//							String s2="下载完成，MD5验证不通过"+DateUtil.getCurDateTime()+"\n";
//							writeDownInfo(s2);
							String localfile = Constants.DOWN_NEWAPK_PATH;
							File file = new File(localfile);
							if (file.exists()) {
								file.delete();
							}
							final String info="######Start###### \n"+"phoneVersion:"+oldVersion+"\n"+"newVersion:"+newVersion+"\n"+"newMD5:"+md5+"\n"+"downMD5:"+hash+"\n"+
										"newFileSize:"+fileSize+"\n"+"compeleteSize:"+compeleteSize+"\n"+"date:"+DateUtil.getCurDateTime()+"\n"+"######End##### \n";
							JLog.d(TAG, "下载失败信息:"+info);
//							writeDownInfo(info);
							stopSelf();//MD5验证不通过停止服务下次登录的时候重新开始下载
						}
						
						new Dao(DownApkBackstageService.this).delete();//删除表中的信息
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			} else {
				JLog.d(TAG, "网络异常，下载中断...");
//				String s2="网络异常，下载终止"+DateUtil.getCurDateTime()+"\n";
//				writeDownInfo(s2);
				//msg.what =2表示有异常，网络可能中断，此时每隔30秒尝试重新连接
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						JLog.d(TAG, "重新下载...");
//						String s2="网络异常，重新下载"+DateUtil.getCurDateTime()+"\n";
//						writeDownInfo(s2);
						downLoadApplication();
					}
				}, 30000);
			}
		}
	};
	private String oldVersion;
	/**
	 * 下载Apk的方法 里面是一个线程
	 * 
	 */
	private void downLoadApplication() {
//		String s1="开始下载       "+DateUtil.getCurDateTime()+"\n";
//		writeDownInfo(s1);
		// 得到下载文件的路径
		String localfile = Constants.DOWN_NEWAPK_PATH;
		// 初始化一个downloader下载器
		Downloader downloader = new Downloader(urlstr, md5, localfile, 1, this, mHandler);
		DownloadInfo info=downloader.getDownloaderInfo();//拿到当前最新MD5的下载器
		downloader.download(info);
	}

	/**
	 * 如果新版本号比就版本号大就返回true 否则返回false
	 * @param oldVersion 旧版本号
	 * @param newVersion 新版本号
	 * @return true 表示需要下载 false 表示不需要下载
	 */
	private boolean isNeedUpdate(String oldVersion, String newVersion) {
		if (!TextUtils.isEmpty(oldVersion) && !TextUtils.isEmpty(newVersion)) {	
			JLog.d("oldVersion==>", oldVersion);
			JLog.d("newVersion==>", newVersion);
			String projectVersion=this.getResources().getString(R.string.PROJECT_VERSIONS);
			String projectname=null;
			if(projectVersion.equals(Constants.APP_VERSION_4_5)){
				projectname = this.getResources().getString(R.string.project_name4_5); //本4.5防止下载成4.0的包
			}else{
				projectname = this.getResources().getString(R.string.project_name4_0); //本4.0防止下载成4.5的包
			}
			JLog.d("projectname==>", projectname);
			if(urlstr.toUpperCase().contains(projectname.toUpperCase()) && newVersion.compareTo(oldVersion) != 0){
				return true;
			}
		}
		return false;
	}

	/**
	 * 安装新版本APK提示
	 */
	private void installApp() {
		if (isFromHome) {
			Intent intent = new Intent(DownApkBackstageService.this,DialogInstallActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("path", Constants.DOWN_NEWAPK_PATH);
			DownApkBackstageService.this.startActivity(intent);
		}
		stopSelf();
	}
		
	
	/**
	 * 验证下载的文件的MD5和传过来的MD5是否一样
	 * @param filePath  apk文件路径
	 * @return true 表示服务端传过来的MD5和本地存的APK文件MD5相同 
	 */
	private boolean isPassMd5(String filePath) {
		boolean pass = true;
		try {
			String hash = MD5Helper.getMD5Checksum(filePath);
			if (!hash.toUpperCase().equals(md5.toUpperCase())) {
				pass = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pass;
	}
	

	@Override
	public void onDestroy() {
		super.onDestroy();
		JLog.d(TAG, "下载服务关闭");
//		writeDownInfo("下载服务关闭      "+DateUtil.getCurDateTime()+"\n");
	}
	
//	/**
//	 * 将下载的信息写在sd卡的文件上
//	 * @param info 要写入的信息
//	 */
//	 private void writeDownInfo(String info){
//	 		try {
//	 			PrintWriter out= new PrintWriter(new BufferedWriter(new FileWriter( Constants.SDCARD_PATH+"downApkInfo.txt",true))); 
//	 			out.write(info); 
//	 			out.close();
//	 		} catch (Exception e) {
//	 		}
//	 }

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
