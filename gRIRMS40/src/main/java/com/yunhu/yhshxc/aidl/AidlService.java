package com.yunhu.yhshxc.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.yunhu.yhshxc.http.download.apk.Dao;
import com.yunhu.yhshxc.http.download.apk.DownloadInfo;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;

/**
 * 企业应用中心的服务端远程调用获取当前的apk的版本信息 获取当前是否正在下载，如果正在下载就把已经下载的apk的大小返回 返回当前要升级
 * 的apk文件的MD5码
 * @author jishen
 */
public class AidlService extends Service {
	private GrirmsDataImpl.Stub grirmsData;//数据源
	@Override
	public IBinder onBind(Intent intent) {
		return grirmsData;
	}
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		grirmsData = new GrirmsDataImpl(){
			String md5Code=SharedPreferencesUtil.getInstance(AidlService.this).getMD5Code();
			Dao dao=new Dao(AidlService.this);
			boolean isDowning=!dao.isHasInfors(md5Code);//返回true表示数据库中没有记录，返回false表示数据库中有记录
			@Override
			public String getMd5Code() throws RemoteException {
				return md5Code;//返回当前最新版本apk的MD5
			}

			@Override
			public String getCompeleteSize() throws RemoteException {
				if(isDowning){//如果正在下载返回已经下载的文件的大小否则返回空
					DownloadInfo info=dao.getInfo(md5Code);
					return String.valueOf(info.getCompeleteSize());
				}else{
					return "";
				}
			}

			@Override
			public boolean isDowning() throws RemoteException {
				if(isDowning){//返回是否正在下载 true表示正在下载 false表示没有在下载
					return true;
				}else{
					return false;
				}
			}
			
		};
	}

}
