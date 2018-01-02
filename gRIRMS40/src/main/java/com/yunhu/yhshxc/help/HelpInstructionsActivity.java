package com.yunhu.yhshxc.help;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.FileHelper;
import com.yunhu.yhshxc.utility.HttpHelper;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;

import org.apache.http.HttpEntity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import gcg.org.debug.JLog;

/**
 * 操作说明模块 通过将相关html页面下载到本地，并由WebView显示。如果是视频资料，则调用Android系统播放器播放
 * 
 * @version 2013.5.23
 * @author wangchao
 * 
 */
public class HelpInstructionsActivity extends AbsBaseActivity {
	private final String TAG = "HelpInstructionsActivity";

	/**
	 * 进度对话框，用于通过HTTP加载操作说明时显示进度
	 */
	private ProgressDialog mProgressDialog = null;

	/**
	 * 浏览器View
	 */
	private WebView wv = null;

	/**
	 * 下载操作说明的url
	 */
	private String downLoad_url = null;

	/**
	 * 保存操作说明的本地目录
	 */
	private String PATH_HELP = Constants.SDCARD_PATH + "help/";

	/**
	 * 操作说明文件的大小
	 */
	private Long totalSize = 0L;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.help_instructions);
		findViewById(R.id.help_instruction_back).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				HelpInstructionsActivity.this.finish();
				
			}
		});
		wv = (WebView) this.findViewById(R.id.wv);// 获取浏览器View
		webSettings();// 设置浏览器相关属性
		wv.setWebViewClient(webViewClient());// 设置浏览器监听器
		this.showProgressDialog(this.getResources().getString(
				R.string.initTable));// 显示对话框
		compareVersion1();
		// requestVersion().start(); // 请求最新版本
		// loadUrl();
		// String url = "http://219.148.162.99/help/ylly/help.zip";
	}

	/**
	 * 设置浏览器相关属性
	 */
	private void webSettings() {
		WebSettings mWebSettings = wv.getSettings();
		mWebSettings.setBuiltInZoomControls(false);// 不显示缩放控件
		mWebSettings.setSupportZoom(false); // 不支持缩放
		// mWebSettings.setUseWideViewPort(true) ;//将图片调整到适合webview的大小
		// mWebSettings.setLoadWithOverviewMode(true);

		mWebSettings.setSupportMultipleWindows(false);// 不支持多窗口显示
		mWebSettings.setDefaultZoom(ZoomDensity.MEDIUM);// 设置默认大小
		mWebSettings.setJavaScriptEnabled(true);// 支持Javascript
	}

	/**
	 * 转换文件大小为可读性强的文字（按照MB、KB显示）
	 * 
	 * @param filesize
	 *            文件大小
	 * @return 转换后的文字
	 */
	public String convertFileSize(long filesize) {
		String strUnit = "Bytes";
		String strAfterComma = "";
		int intDivisor = 1;
		if (filesize >= 1024 * 1024) {
			strUnit = "MB";
			intDivisor = 1024 * 1024;

		} else if (filesize >= 1024) {
			strUnit = "KB";
			intDivisor = 1024;
		}
		if (intDivisor == 1)
			return filesize + " " + strUnit;
		strAfterComma = "" + 100 * (filesize % intDivisor) / intDivisor;
		if (strAfterComma == "")
			strAfterComma = ".0";
		return filesize / intDivisor + "." + strAfterComma + " " + strUnit;
	}

	/**
	 * 配合线程显示结果的Handler
	 */
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			closeDialog();// 关闭进度对话框
			switch (msg.what) {
			// 如果有新版本，则询问用户是否下载
			case 1:
				totalSize = Long.valueOf((String) msg.obj);
				showUpdateDialog(getResources().getString(R.string.helpinstruc_message01)
						+ convertFileSize(totalSize) + ")");
				break;

			// 如果没有新版本，则加载已有的版本
			case 2:
				loadUrl();
				break;
			}
		}

	};

	/**
	 * 请求最新版本
	 * 
	 * @return 返回执行操作的线程对象
	 */
	private Thread requestVersion() {
		this.showProgressDialog(this.getResources().getString(
				R.string.initTable));// 显示对话框
		return new Thread() {
			@Override
			public void run() {
				compareVersion1();
			}

		};
	}

	private void compareVersion1() {
		String loginId = SharedPreferencesHelp.getInstance(this).getLoginId();// 取出公司ID
		String verTemp = SharedPreferencesHelp.getInstance(this)
				.getHelpVersion();// 取出当前版本
		String url = UrlInfo.urlDownloadHelp(this) + "?en=" + loginId + "&vs="
				+ verTemp + "&md=2f630ff9d99eb67a2c76665be8e1bb06";
		GcgHttpClient.getInstance(this).get(url, null,
				new HttpResponseListener() {

					@Override
					public void onFailure(Throwable error, String content) {
						closeDialog();// 关闭进度对话框
						loadUrl();// 认为没有新版本
					}

					@Override
					public void onStart() {

					}

					@Override
					public void onFinish() {

					}

					@Override
					public void onSuccess(int statusCode, String content) {
						closeDialog();// 关闭进度对话框
						if (!TextUtils.isEmpty(content)) {
							// 解析数据，因为是在非主线程中操作的，所以结果需要发送给Handler来显示
							String[] arr = content.split(",");
							if (!TextUtils.isEmpty(arr[0])
									&& arr[0].equalsIgnoreCase("y")
									&& arr.length == 4) {// 操作说明有新版本
								SharedPreferencesHelp.getInstance(
										HelpInstructionsActivity.this
												.getApplicationContext())
										.saveHelpVersionTemp(arr[1]);
								downLoad_url = arr[3];
								totalSize = Long.parseLong(arr[2]);
								showUpdateDialog(getResources().getString(R.string.helpinstruc_message01)
										+ convertFileSize(totalSize) + ")");
							} else {
								loadUrl();// 操作说明没有新版本
							}
						} else {
							loadUrl();// 操作说明没有新版本
						}
					}

				});
	}

	// /**
	// * 将公司ID和当前版本发送给服务器，由服务器来判断是否需要更新版本 这个方法属于耗时操作，应该放在非主线程中进行
	// */
	// private void compareVersion() {
	// String loginId = SharedPreferencesHelp.getInstance(this).getLoginId();//
	// 取出公司ID
	// String verTemp =
	// SharedPreferencesHelp.getInstance(this).getHelpVersion();// 取出当前版本
	// String url = UrlInfo.urlDownloadHelp(this) + "?en=" + loginId + "&vs=" +
	// verTemp + "&md=2f630ff9d99eb67a2c76665be8e1bb06";
	// // String url =
	// "http://219.148.162.99:8080/grirms4_help_upload/GetHelpFileUrl?en=text&vs="+verTemp+"&md=2f630ff9d99eb67a2c76665be8e1bb06";
	// JLog.d(TAG, "compareVersion url =>" + url);
	// String result = new
	// HttpHelper(HelpInstructionsActivity.this).connectGet(url);//
	// 通过HTTP加载（耗时操作，加载完成前会阻塞）
	// JLog.d(TAG, "compareVersion result =>" + String.valueOf(result));
	// if (!TextUtils.isEmpty(result)) {
	// // 解析数据，因为是在非主线程中操作的，所以结果需要发送给Handler来显示
	// String[] arr = result.split(",");
	// if (!TextUtils.isEmpty(arr[0]) && arr[0].equalsIgnoreCase("y") &&
	// arr.length == 4) {//操作说明有新版本
	// SharedPreferencesHelp.getInstance(HelpInstructionsActivity.this.getApplicationContext()).saveHelpVersionTemp(arr[1]);
	// downLoad_url = arr[3];
	// Message msg = mHandler.obtainMessage();
	// msg.obj = arr[2];
	// msg.what = 1;
	// mHandler.sendMessage(msg);
	// }
	// else {
	// mHandler.sendEmptyMessage(2);//操作说明没有新版本
	// }
	// }
	// else {
	// mHandler.sendEmptyMessage(2);//操作说明没有新版本
	// }
	// }

	/**
	 * 在浏览器中加载html
	 */
	public void loadUrl() {
		wv.loadUrl("file://" + Constants.SDCARD_PATH + "help/index.html");
		// wv.loadUrl("http://219.148.162.99/wordpress/");
	}

	/**
	 * 获取用于监听浏览器加载过程的监听器
	 * 
	 * @return 返回监听器
	 */
	private WebViewClient webViewClient() {
		return new WebViewClient() {

			/**
			 * 当页面开始加载时显示进度对话框(详细信息参考Android相关API)
			 */
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				showProgressDialog(getResources().getString(R.string.helpinstruc_message04));
				super.onPageStarted(view, url, favicon);

			}

			/**
			 * 如果是视频就调用系统播放器来播放，否则就用当前浏览器显示
			 */
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.endsWith(".mp4")
						|| url.endsWith("some other supported type")) {
					// Intent intent = new Intent(HelpInstructionsActivity.this,
					// VideoPlayer.class);
					// intent.putExtra("url", url);
					// startActivity(intent);

					Intent it = new Intent(Intent.ACTION_VIEW);
					Uri uri = Uri.parse(url);
					it.setDataAndType(uri, "video/mp4");
					startActivity(it);

					return true;
				} else {
					view.loadUrl(url);
				}
				return super.shouldOverrideUrlLoading(view, url);
			}

			/**
			 * 加载完成后关闭进度对话框
			 */
			@Override
			public void onPageFinished(WebView view, String url) {
				closeDialog();
				super.onPageFinished(view, url);
			}

		};
	}

	/**
	 * 通过AsyncTask下载需要更新的数据并写到本地文件中。
	 * 
	 * @see AsyncTask
	 */
	private class RequestPatch extends AsyncTask<String, Integer, Boolean> {
		private final int DOWNLOAD_IN_PROGRESS = 1; // 下载中
		private final int UNZIP_START = 2; // 开始解压
		private final int CLOSE_DIALOG = 3; // 关闭

		/**
		 * 封装了HTTP相关的操作
		 */
		private HttpHelper httpHelper = null;

		/**
		 * 显示进度对话框，并初始化httpHelper对象
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showUpdateProgressDialog();
			httpHelper = new HttpHelper(HelpInstructionsActivity.this);
		}

		/**
		 * 在线程中执行下载任务
		 */
		@Override
		protected Boolean doInBackground(String... params) {
			if (TextUtils.isEmpty(downLoad_url)){// 如果url为空，则直接返回
				isContinue(false);
				return false;
			}

				
			try {
				long flow = PublicUtils.totalFlow();
				JLog.d(TAG, "downLoad url =>" + downLoad_url);
				HttpEntity entity = httpHelper
						.connectGetReturnEntity(downLoad_url);// 耗时操作
				if (entity != null) {
					mProgressDialog.setMax(totalSize.intValue());
					if (downFile(entity.getContent()) == 0) {
						isContinue(true);
						return true;
					}
				}
				JLog.d("HttpFlow==>help(" + Thread.currentThread().getName()
						+ ")" + (PublicUtils.totalFlow() - flow));
			} catch (Exception e) {
				e.printStackTrace();
			}
			isContinue(false);
			return false;
		}

		/**
		 * 线程执行完毕，通过Toast给用户提示是否下载成功，并关闭进度对话框、在浏览器中加载页面
		 */
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			closeDialog();// 关闭进度对话框
			if (result) {// 下载成功
				String verTemp = SharedPreferencesHelp.getInstance(
						HelpInstructionsActivity.this.getApplicationContext())
						.getHelpVersionTemp();
				SharedPreferencesHelp.getInstance(
						HelpInstructionsActivity.this.getApplicationContext())
						.saveHelpVersion(verTemp);
				ToastOrder.makeText(
						HelpInstructionsActivity.this,
						HelpInstructionsActivity.this.getResources().getString(
								R.string.download_finish), ToastOrder.LENGTH_LONG)
						.show();
			} else {// 下载失败
				ToastOrder.makeText(
						HelpInstructionsActivity.this,
						HelpInstructionsActivity.this.getResources().getString(
								R.string.ERROR_NETWORK), ToastOrder.LENGTH_LONG)
						.show();
			}
			loadUrl();// 在浏览器中加载页面
		}

		/**
		 * 更新进度
		 */
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			switch (values[0]) {
			case DOWNLOAD_IN_PROGRESS:
				mProgressDialog.setProgress(values[1]);
				break;
			case UNZIP_START:
				showProgressDialog(getResources().getString(R.string.helpinstruc_message02));
				break;
			case CLOSE_DIALOG:
				closeDialog();
				break;
			}
		}

		private void isContinue(boolean result) {
			// 4.1版本以上
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
			} else {
				Message message = mHandler.obtainMessage();
				message.obj = result;
				message.sendToTarget();
			}
		}
		
		public void initPro(Integer... values){
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
				publishProgress(values);
			} else {
				Message message = progressHandler.obtainMessage();
				message.obj = values;
				message.sendToTarget();
			}
		}
		
		private Handler progressHandler = new Handler(){
			public void handleMessage(Message msg) {
				Integer[] values = (Integer[]) msg.obj;
				switch (values[0]) {
				case DOWNLOAD_IN_PROGRESS:
					mProgressDialog.setProgress(values[1]);
					
					break;
				case UNZIP_START:
					showProgressDialog(getResources().getString(R.string.helpinstruc_message02));
					break;
				case CLOSE_DIALOG:
					closeDialog();
					break;
				}
			};
		};

		private Handler mHandler = new Handler() {
			public void handleMessage(Message msg) {
				boolean result = (Boolean) msg.obj;
				closeDialog();// 关闭进度对话框
				if (result) {// 下载成功
					String verTemp = SharedPreferencesHelp.getInstance(
							HelpInstructionsActivity.this.getApplicationContext())
							.getHelpVersionTemp();
					SharedPreferencesHelp.getInstance(
							HelpInstructionsActivity.this.getApplicationContext())
							.saveHelpVersion(verTemp);
					ToastOrder.makeText(
							HelpInstructionsActivity.this,
							HelpInstructionsActivity.this.getResources().getString(
									R.string.download_finish), ToastOrder.LENGTH_LONG)
							.show();
				} else {// 下载失败
					ToastOrder.makeText(
							HelpInstructionsActivity.this,
							HelpInstructionsActivity.this.getResources().getString(
									R.string.ERROR_NETWORK), ToastOrder.LENGTH_LONG)
							.show();
				}
				loadUrl();// 在浏览器中加载页面
			};
		};

		/**
		 * 将文件从HTTP流中读取出来，并写到本地文件中
		 * 
		 * @param inputStream
		 *            读取文件的流
		 * @return -1:文件下载出错 0:文件下载成功
		 */
		private int downFile(InputStream inputStream) {
			if (inputStream == null) {
				return -1;
			}
			try {
				File file = new File(Constants.PATH_TEMP);// 创建一个临时目录
				if (!file.exists()) {
					file.mkdirs();
				}
				String filePath = Constants.PATH_TEMP + "help.zip";
				File resultFile = write2SDFromInput(filePath, inputStream);// 将文件写到SD卡中
				// File resultFile = new File(filePath);
				// 如果文件长度不符，则说明下载的有问题
				if (resultFile == null || resultFile.length() != totalSize) {
					return -1;
				}
//				this.publishProgress(UNZIP_START);// 更新进度对话框
				initPro(UNZIP_START);
				new FileHelper().delFolder(PATH_HELP);// 清空目录
				File newFile = new File(PATH_HELP);
				if (!newFile.exists()) {// 如果文件不存在就创建
					newFile.mkdirs();
				}
				new FileHelper().unzip(filePath, PATH_HELP);
				resultFile.delete();
				diff();
			} catch (Exception e) {
				e.printStackTrace();
				return -1;
			} finally {
				// 释放相关资源
				try {
					httpHelper.disconnect();
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return 0;
		}

		/**
		 * 将一个InputStream里面的数据写入到SD卡中
		 * 
		 * @param path
		 *            文件路径
		 * @param input
		 *            输入流
		 * @return 返回目标文件
		 */
		private File write2SDFromInput(String path, InputStream input) {
			File file = null;
			OutputStream output = null;
			try {
				file = new File(path);
				if (file.exists()) {
					file.delete();
				}
				output = new FileOutputStream(file);
				byte[] buffer = new byte[4 * 1024];
				Long downloadedSize = 0L;
				int bufferLength = -1;
				while ((bufferLength = input.read(buffer)) != -1) {
					output.write(buffer, 0, bufferLength);
					downloadedSize += bufferLength;
//					this.publishProgress(DOWNLOAD_IN_PROGRESS,
//							downloadedSize.intValue());
					initPro(DOWNLOAD_IN_PROGRESS,downloadedSize.intValue());
				}
				output.flush();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return file;
		}

		/**
	     * 
	     */
		private void diff() {
			// File file = new File();
		}
	}

	/**
	 * 显示更新提示对话框，请用户确定是否更新
	 * 
	 * @param message
	 *            提示文字
	 */
	private void showUpdateDialog(String message) {
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage(message);
		builder.setTitle(getResources().getString(R.string.update_hints));
		// 确定按钮
		builder.setPositiveButton(R.string.confirm, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				new RequestPatch().execute();
			}
		});
		// 取消按钮
		builder.setNegativeButton(R.string.cancel, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				loadUrl();
			}
		});

		builder.create().show();
	}

	/**
	 * 显示文字为"正在更新，请稍后..."的模态进度对话框
	 */
	private void showUpdateProgressDialog() {
		closeDialog();
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setMessage(getResources().getString(R.string.helpinstruc_message03));
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mProgressDialog.setCancelable(false);
		mProgressDialog.show();
	}

	/**
	 * 根据指定文字显示模态进度对话框
	 * 
	 * @param message
	 *            对话框中要显示的文字
	 */
	private void showProgressDialog(String message) {
		closeDialog();
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setMessage(message);
		mProgressDialog.setCancelable(false);
		mProgressDialog.show();
	}

	/**
	 * 关闭进度对话框
	 */
	private void closeDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
	}

	@Override
	protected void onDestroy() {
		wv.destroy();// 销毁当前Activity前先销毁浏览器
		super.onDestroy();
	}

}
