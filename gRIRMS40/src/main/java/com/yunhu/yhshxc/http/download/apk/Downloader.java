package com.yunhu.yhshxc.http.download.apk;

import gcg.org.debug.JLog;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;

/**
 * 下载器
 * @author jishen
 *
 */
public class Downloader {
	private String urlString;// 下载的地址
	private String md5;// 下载的MD5
	private String localfile;// 保存路径
	private int threadcount;// 线程数
	private Handler mHandler;// 消息处理器
	private Dao dao;// 工具类
	private int fileSize;// 所要下载的文件的大小
	private Context context;

	public Downloader(String urlString, String md5, String localfile, int threadcount, Context context, Handler mHandler) {
		this.urlString = urlString;
		this.md5 = md5;
		this.localfile = localfile;
		this.threadcount = threadcount;
		this.mHandler = mHandler;
		this.context = context;
		dao = new Dao(context);
	}

	/**
	 * 得到downloader里的信息 首先进行判断是否是第一次下载，如果是第一次就要进行初始化，并将下载器的信息保存到数据库中 如果不是第一次下载，那就要从数据库中读出之前下载的信息（起始位置，结束为止，文件大小等），并将下载信息返回给下载器
	 */
	public DownloadInfo getDownloaderInfo() {
		String apkSize = SharedPreferencesUtil.getInstance(context).getApkSize();
		fileSize = Integer.valueOf(apkSize.trim());
		JLog.d("jishen", "fileSize==>" + fileSize);
		if (isFirst(md5)) {
			JLog.d("DownApkService", "isFirst");
			init();
			int range = fileSize / threadcount;

			DownloadInfo info = new DownloadInfo(threadcount - 1, (threadcount - 1) * range, fileSize - 1, 0, urlString, md5);
			// 保存infos中的数据到数据库
			dao.saveInfo(info);
			// 创建一个LoadInfo对象记载下载器的具体信息
			return info;
		}
		else {
			// 得到数据库中已有的md5的下载器的具体信息
			DownloadInfo info = dao.getInfo(md5);
			return info;
		}
	}

	/**
	 * 初始化
	 */
	private void init() {
		try {
			// URL url = new URL(urlstr);
			// HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			// connection.setConnectTimeout(3000);
			// connection.setRequestMethod("GET");
			// fileSize = connection.getContentLength();
			// String apkSize=new HttpHelper().connectGet(Constants.URL_APK_SIZE);
			// String apkSize=SharedPreferencesUtil.getInstance(context).getApkSize();
			// fileSize = Integer.valueOf(apkSize.trim());
			// JLog.d("jishen", "fileSize==>"+fileSize);
			File file = new File(localfile);
			if (!file.exists()) {
				file.createNewFile();
			}
			// 本地访问文件
			RandomAccessFile accessFile = new RandomAccessFile(file, "rwd");
			accessFile.setLength(fileSize);
			accessFile.close();
			// connection.disconnect();
		}
		catch (Exception e) {//如果有异常就通知服务
			Message message = Message.obtain();
			message.what = 2;
			message.obj = md5;
			mHandler.sendMessage(message);
		}
	}

	/**
	 * 判断是否是第一次 下载
	 */
	private boolean isFirst(String md5) {
		return dao.isHasInfors(md5);
	}

	/**
	 * 利用线程开始下载数据
	 */
	public void download(DownloadInfo info) {
		if (info != null) {
			Thread currentThread = new MyThread(info.getThreadId(), info.getStartPos(), info.getEndPos(), info.getCompeleteSize(), info.getUrl());
			DownloadInfo.NEEDDOWNTHREADID = currentThread.getId() + "";
			currentThread.start();
		}
	}

	/**
	 * 下载的线程
	 * @author jishen
	 *
	 */
	public class MyThread extends Thread {
		private int threadId;//线程ID
		private int startPos; //下载开始点
		private int endPos;//下载的结束点
		private int compeleteSize;//已经完成的大小
		private String urlstr;//下载的URL

		public MyThread(int threadId, int startPos, int endPos, int compeleteSize, String urlstr) {
			this.threadId = threadId;
			this.startPos = startPos;
			this.endPos = endPos;
			this.compeleteSize = compeleteSize;
			this.urlstr = urlstr;
		}

		@Override
		public void run() {
			HttpURLConnection connection = null;
			RandomAccessFile randomAccessFile = null;
			InputStream is = null;
			try {
				URL url = new URL(urlstr);
				connection = (HttpURLConnection) url.openConnection();
				connection.setConnectTimeout(5000);
				connection.setRequestMethod("GET");

				// 设置范围，格式为Range：bytes x-y;
				connection.setRequestProperty("Range", "bytes=" + (startPos + compeleteSize) + "-");
				int surplusSize = connection.getContentLength();// 剩余大小
				JLog.d("DownApkService", "线程：" + this.currentThread().getName() + "剩余大小：" + surplusSize + "  合计总大小：" + (surplusSize + compeleteSize));

				if (surplusSize + compeleteSize <= fileSize) {// 如果已完成的加上已下载的大于原文件，说明下载有异常，停止下载
					randomAccessFile = new RandomAccessFile(localfile, "rwd");
					randomAccessFile.seek(startPos + compeleteSize);
					// 将要下载的文件写到保存在保存路径下的文件中
					is = connection.getInputStream();
					JLog.d("DownApkService", is.available() + " 线程  id = " + this.currentThread().getId());

					byte[] buffer = new byte[4096];
					int length = -1;
					while ((length = is.read(buffer)) != -1) {
						randomAccessFile.write(buffer, 0, length);
						compeleteSize += length;
						// 更新数据库中的下载信息
						dao.updataInfos(threadId, compeleteSize, md5);
						// 用消息将下载信息传给进度条，对进度条进行更新
						Message message = Message.obtain();
						message.what = 1;
						message.obj = md5;
						message.arg1 = compeleteSize;
						mHandler.sendMessage(message);

						if (compeleteSize > fileSize) {// 如果已经下载的文件的大小大于实际文件的大小，跳出，不在下载
							Message message1 = Message.obtain();
							message1.arg2 = -1;
							message1.what = 1;
							message1.obj = md5;
							message1.arg1 = compeleteSize;
							mHandler.sendMessage(message1);
							break;
						}
						// 如果这个线程不是最新的线程那么该线程停止
						if (!TextUtils.isEmpty(DownloadInfo.NEEDDOWNTHREADID) && !DownloadInfo.NEEDDOWNTHREADID.equals(this.currentThread().getId() + "")) {
							JLog.d("DownApkService", "线程  id = " + this.currentThread().getId() + " 停止！");
							try {
								PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Constants.SDCARD_PATH + "downApkInfo.txt", true)));
								out.write("线程  id = " + this.currentThread().getId() + " 停止, 线程ID ：" + DownloadInfo.NEEDDOWNTHREADID + "正在下载！");
								out.close();
							}
							catch (Exception e) {
							}
							
							break;
						}
					}

				}
				else {
					Message message = Message.obtain();
					message.arg2 = -1;
					message.what = 1;
					message.obj = md5;
					message.arg1 = compeleteSize;
					mHandler.sendMessage(message);
				}

			}
			catch (Exception e) {
				Message message = Message.obtain();
				message.what = 2;
				message.obj = md5;
				mHandler.sendMessage(message);
			}
			finally {
				try {
					is.close();
					randomAccessFile.close();
					connection.disconnect();
					// dao.closeDb();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}

	// 删除数据库中urlstr对应的下载器信息
	public void delete(String md5) {
		dao.delete(md5);
	}
}