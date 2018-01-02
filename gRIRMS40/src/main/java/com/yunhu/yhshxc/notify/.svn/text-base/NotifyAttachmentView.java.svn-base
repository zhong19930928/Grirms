package com.yunhu.yhshxc.notify;

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

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.dragimage.PhotoPriviewActivity;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.HttpHelper;

import java.io.File;

/**
 * 公告附件
 * @author gcg_jishen
 *
 */
public class NotifyAttachmentView {
	
	private Context context;
	private View view;
	private TextView tv_attachment;//附件名称
	private ImageView iv_attachment;//图标
	private String url;//附件图片下载URL
	private String fileName;
	public NotifyAttachmentView(Context context) {
		this.context = context;
		view = View.inflate(context, R.layout.notify_attachment, null);
		tv_attachment = (TextView)view.findViewById(R.id.tv_attachment_name);
		tv_attachment.getPaint().setUnderlineText(true);
		iv_attachment = (ImageView)view.findViewById(R.id.iv_attachment);
	}
	
	public void setAttachmentData(String name,String url){
		this.url = url;
		int index = url.lastIndexOf("/");
		fileName = url.substring(index + 1);
		tv_attachment.setText(name);
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
			if (url.endsWith(".doc") || url.endsWith(".docx")) {//word文档
				iv_attachment.setBackgroundResource(R.drawable.word);
			}else if(url.endsWith(".xls") || url.endsWith(".xlsx")){//excel
				iv_attachment.setBackgroundResource(R.drawable.excel);
			}else if(url.endsWith(".ppt") || url.endsWith(".pptx")){//ppt
				iv_attachment.setBackgroundResource(R.drawable.ppt);
			}else if(url.endsWith(".pdf")){//pdf
				iv_attachment.setBackgroundResource(R.drawable.pdf);
			}else{
				iv_attachment.setBackgroundResource(R.drawable.picture);
			}
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
				Toast.makeText(context, setString(R.string.notify_activity_07), Toast.LENGTH_SHORT).show();
			}else if(url.endsWith(".rar")){
				Toast.makeText(context, setString(R.string.notify_activity_08), Toast.LENGTH_SHORT).show();
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
		String path = Constants.ATTACHMENT_PATH + name;
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
				Toast.makeText(context, setString(R.string.notify_activity_06), Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	private void downAttachment(final String fileName){
		new DownAttachment().execute(fileName);
	}
	
	private class DownAttachment extends AsyncTask<String, String, String>{
		
		private Dialog dialog = null;
		public DownAttachment() {
			dialog = new MyProgressDialog(context,R.style.CustomProgressDialog,setString(R.string.notify_activity_01));
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String fileName = params[0];
			int result = new HttpHelper(context).downloadNotifyAttachment(url, Constants.ATTACHMENT_PATH, fileName);
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
		builder.setTitle(setString(R.string.notify_activity_02));
		builder.setMessage(setString(R.string.notify_activity_03));
		builder.setPositiveButton(setString(R.string.notify_activity_04), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				downAttachment(url);
			}
		});
		builder.setNegativeButton(setString(R.string.notify_activity_05), null);
		builder.setCancelable(false);
		builder.create().show();
	}

	private String setString(int stringId){
		return context.getResources().getString(stringId);
	}
}
