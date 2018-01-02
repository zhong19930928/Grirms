package com.yunhu.yhshxc.list.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.HttpHelper;

import java.io.File;

public class VideoReview {
	private Context context;
	private String url;
	private String fileName;
	public VideoReview(Context mContext) {
		this.context = mContext;
	}
	
	/**
	 * 查看附件内容
	 */
	public void readAttachment(String url){
		this.url = url;
		if (!TextUtils.isEmpty(url)) {
			if (url.endsWith(".mp4")) {//视频
				int index = url.lastIndexOf("/");
				fileName = url.substring(index + 1);
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
		String path = Constants.VIDEO_PATH + name;
		File file = new File(path);
		if (file == null || !file.exists()){
			downAttachment(name);
		}else{
			 if(path.endsWith(".mp4")){
					Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
					 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					 intent.putExtra("oneshot", 0);
					  intent.putExtra("configchange", 0);
					  Uri uri = Uri.fromFile(file);
					 intent.setDataAndType(uri, "video/*");
				     context.startActivity(intent);
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
			int result = new HttpHelper(context).downloadNotifyAttachment(url, Constants.VIDEO_PATH, fileName);
			isContinue(String.valueOf(result));
			return String.valueOf(result);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			dialog.dismiss();
			if (!TextUtils.isEmpty(result) && "0".equals(result)) {//下载成功
				File file = new File(Constants.VIDEO_PATH+fileName);
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
					File file = new File(Constants.VIDEO_PATH+fileName);
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
		File file = new File(Constants.VIDEO_PATH+fileName);
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
	private String setString(int stringId){
		return context.getResources().getString(stringId);
	}
	
}

