package com.yunhu.yhshxc.list.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.HttpHelper;

import java.io.File;

public class AttachmentReview {
	private Context context;
	private String url;
	private String fileName;
	public AttachmentReview(Context mContext) {
		this.context = mContext;
	}
	
	/**
	 * 查看附件内容
	 */
	public void readAttachment(String url){
		this.url = url;
		if (!TextUtils.isEmpty(url)) {
			int index = url.lastIndexOf("/");
			fileName = url.substring(index + 1);
			if (url.endsWith(".doc") || url.endsWith(".docx")) {//word文档
				openFile(fileName);
			}else if(url.endsWith(".xls") || url.endsWith(".xlsx")){//excel
				openFile(fileName);
			}else if(url.endsWith(".ppt") || url.endsWith(".pptx")){//ppt
				openFile(fileName);
			}else if(url.endsWith(".pdf")){//pdf
				openFile(fileName);
			}else if(url.endsWith(".txt")){//txt 
				openFile(fileName);
			}
		}
	}
	
	/**
	 * 打开附件
	 * @param name
	 * @return
	 */
	private void openFile(String name) {
		String path = Constants.FUNC_ATTACHMENT_PATH + name;
		File file = new File(path);
		if (file == null || !file.exists()){
			downAttachment(name);
		}else{
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
		//		bundle.putString(OPEN_MODE, READ_ONLY);       
		//		 //打开模式
		//		bundle.putBoolean(SEND_CLOSE_BROAD, true);         
		//		 //关闭时是否发送广播   
		//		bundle.putString(THIRD_PACKAGE, selfPackageName); 
		//          //第三方应用的包名，用于对改应用合法性的验证
				//清除打开记录
				bundle.putBoolean("ClearTrace", true);
				//关闭后删除打开文件
				intent.putExtras(bundle);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setAction(android.content.Intent.ACTION_VIEW);
				intent.setClassName("cn.wps.moffice_eng", "cn.wps.moffice.documentmanager.PreStartActivity2");
				Uri uri = Uri.fromFile(file);
				intent.setData(uri);
				try {
					context.startActivity(intent);
				}catch (ActivityNotFoundException e){
					e.printStackTrace();
					Toast.makeText(context, setString(R.string.activity_after_07), Toast.LENGTH_SHORT).show();
				}
			}
	}
	
	private void downAttachment(final String fileName){
		new DownAttachment().execute(fileName);
	}
	
	private class DownAttachment extends AsyncTask<String, String, String>{
		
		private Dialog dialog = null;
		public DownAttachment() {
			dialog = new MyProgressDialog(context,R.style.CustomProgressDialog,setString(R.string.activity_after_05));
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String fileName = params[0];
			int result = new HttpHelper(context).downloadNotifyAttachment(url, Constants.FUNC_ATTACHMENT_PATH, fileName);
			isContinue(String.valueOf(result));
			return String.valueOf(result);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			dialog.dismiss();
			if (!TextUtils.isEmpty(result) && "0".equals(result)) {//下载成功
				File file = new File(Constants.FUNC_ATTACHMENT_PATH+fileName);
				if (file==null || !file.exists() || file.length()==0) {
					downFail();
				}else{
					openFile(fileName);
				}
			}else{
				downFail();
			}
		}
		
		private void isContinue(String result){
			//4.1版本以上
			if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN){
			}else{
					Message message = mHandler.obtainMessage();
					message.obj = result;
					message.sendToTarget();
			}
		}
		
		private Handler mHandler = new  Handler(){
			public void handleMessage(Message msg) {
				String result =  (String) msg.obj;
				dialog.dismiss();
				if (!TextUtils.isEmpty(result) && "0".equals(result)) {//下载成功
					File file = new File(Constants.FUNC_ATTACHMENT_PATH+fileName);
					if (file==null || !file.exists() || file.length()==0) {
						downFail();
					}else{
						openFile(fileName);
					}
				}else{
					downFail();
				}
			};
		};

	}
	
	private void downFail(){
		File file = new File(Constants.FUNC_ATTACHMENT_PATH+fileName);
		if (file!=null && file.exists()) {
			file.delete();
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(setString(R.string.activity_after_prompt));
		builder.setMessage(setString(R.string.activity_after_06));
		builder.setPositiveButton(setString(R.string.activity_after_retry), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				downAttachment(fileName);
			}
		});
		builder.setNegativeButton(setString(R.string.activity_after_cancel), null);
		builder.setCancelable(false);
		builder.create().show();
	}


	private String setString(int stringID){
		return context.getResources().getString(stringID);
	}
	
}

