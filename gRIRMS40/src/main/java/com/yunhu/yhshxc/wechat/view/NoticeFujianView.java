package com.yunhu.yhshxc.wechat.view;

import java.io.File;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.dragimage.PhotoPriviewActivity;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.HttpHelper;
import com.yunhu.yhshxc.utility.PublicUtils;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class NoticeFujianView {
	private View view;
	private Context context;
	private ImageView iv_fujian;
	private TextView tv_fujian;
	private String url;//附件图片下载URL
	private String fileName;
	public NoticeFujianView(Context context){
		this.context = context;
		view = View.inflate(context, R.layout.wechat_notice_item_fujian, null);
		tv_fujian = (TextView) view.findViewById(R.id.tv_fujian);
		iv_fujian = (ImageView) view.findViewById(R.id.iv_fujian);
		tv_fujian.getPaint().setUnderlineText(true);
	}
	public void setAttachmentData(String name,String url){
		this.url = url;
		int index = url.lastIndexOf("/");
		fileName = url.substring(index + 1);
		tv_fujian.setText(name);
		setIvAttachment();

	}
	
	public View getView(){
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				readAttachment();
			}
		});
		return view;
	}
	
	/**
	 * 设置图标
	 */
	private void setIvAttachment(){
		if (!TextUtils.isEmpty(url)) {
			iv_fujian.setBackgroundResource(R.drawable.wechat_fujian);
		}
	}
	
	/**
	 * 查看附件内容
	 */
	private void readAttachment(){
		if (!TextUtils.isEmpty(url)) {
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
			}else if(url.endsWith(".zip")){
				Toast.makeText(context, R.string.un_support_zip, Toast.LENGTH_SHORT).show();
			}else if(url.endsWith(".rar")){
				Toast.makeText(context,R.string.un_support_rar, Toast.LENGTH_SHORT).show();
			}else{//默认是图片
				Intent intent = new Intent(context, PhotoPriviewActivity.class);	
				intent.putExtra("url", url);
				context.startActivity(intent);
			}
		}
	}
	
	/**
	 * 打开附件
	 * @param name
	 * @return
	 */
	private void openFile(String name) {
		String path = Constants.WECHAT_PATH + name;
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
				Toast.makeText(context, R.string.install_wps, Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	private void downAttachment(final String fileName){
		new DownAttachment().execute(fileName);
	}
	
	private class DownAttachment extends AsyncTask<String, String, String>{
		
		private Dialog dialog = null;
		public DownAttachment() {
			dialog = new MyProgressDialog(context,R.style.CustomProgressDialog, PublicUtils.getResourceString(context,R.string.down_load_accessory));
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String fileName = params[0];
			int result = new HttpHelper(context).downloadNotifyAttachment(url, Constants.WECHAT_PATH, fileName);
			isContinue(String.valueOf(result));
			return String.valueOf(result);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			dialog.dismiss();
			if (!TextUtils.isEmpty(result) && "0".equals(result)) {//下载成功
				openFile(fileName);
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
					openFile(fileName);
				}else{
					downFail();
				}
			};
		};
	}
	
	private void downFail(){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.tip);
		builder.setMessage(R.string.down_failure_retry);
		builder.setPositiveButton(R.string.submit_btn_again, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				downAttachment(url);
			}
		});
		builder.setNegativeButton(R.string.Cancle, null);
		builder.setCancelable(false);
		builder.create().show();
	}
}
