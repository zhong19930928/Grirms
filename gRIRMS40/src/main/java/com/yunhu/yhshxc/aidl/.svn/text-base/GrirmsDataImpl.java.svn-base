package com.yunhu.yhshxc.aidl;


import android.os.RemoteException;

public class GrirmsDataImpl extends GrirmsData.Stub{

	private String md5Code;//最新版本apk的MD5
	private boolean isDowning=false;//是否正在下载 true表示正在下载 false表示没有下载
	private String compeleteSize;//已经下载的大小
	@Override
	public String getMd5Code() throws RemoteException {
		return md5Code;
	}

	@Override
	public String getCompeleteSize() throws RemoteException {
		return compeleteSize;
	}


	@Override
	public boolean isDowning() throws RemoteException {
		return isDowning;
	}


}
