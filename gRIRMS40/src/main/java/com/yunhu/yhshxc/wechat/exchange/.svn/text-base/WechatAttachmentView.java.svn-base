package com.yunhu.yhshxc.wechat.exchange;

import java.io.File;

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
import com.yunhu.yhshxc.list.activity.RecordAuditionActivity;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.HttpHelper;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.wechat.bo.WechatAttachment;

public class WechatAttachmentView {
	private View view = null;
	private WechatAttachment wechatAttachment;
	private TextView tv_wechat_attachment;
	private Context context;
	private String url;
	private String fileName;
	private View showVoiceReferView;
	private ImageView iv_wechat;
	public WechatAttachmentView(Context mContext) {
		this.context = mContext;
		view = View.inflate(mContext, R.layout.wechat_attachment_view, null);
		iv_wechat = (ImageView)view.findViewById(R.id.iv_wechat);
		tv_wechat_attachment = (TextView)view.findViewById(R.id.tv_wechat_attachment);
		view.setOnClickListener(listener);
	}
	
	private void setImg(String name){
		if (name.endsWith(".doc") || url.endsWith(".docx")) {//word文档
			iv_wechat.setBackgroundResource(R.drawable.pic_content_small_word);
		}else if(name.endsWith(".xls") || url.endsWith(".xlsx")){//excel
			iv_wechat.setBackgroundResource(R.drawable.pic_content_small_excel);
		}else if(name.endsWith(".ppt") || url.endsWith(".pptx")){//ppt
			iv_wechat.setBackgroundResource(R.drawable.pic_content_small_ppt);
		}else if(name.endsWith(".pdf")){//pdf
			iv_wechat.setBackgroundResource(R.drawable.pic_content_small_pdf);
		}else if(name.endsWith(".txt")){//txt 
			iv_wechat.setBackgroundResource(R.drawable.pic_content_small_txt);
		}else if(name.endsWith(".3gp")){
			iv_wechat.setBackgroundResource(R.drawable.pic_content_small_3gp);
//			tv_wechat_attachment.setText("录音文件");
		}else if(name.endsWith(".mp4")){
			iv_wechat.setBackgroundResource(R.drawable.pic_content_small_mp4);
//			tv_wechat_attachment.setText("视频文件");
		}else if(name.endsWith(".png") || name.endsWith(".PNG") || name.endsWith(".jpg") || name.endsWith(".JPG") 
				|| name.endsWith(".GIF") || name.endsWith(".gif") || 
				name.endsWith(".JPEG") || name.endsWith(".jpeg")|| 
				name.endsWith(".BMP") || name.endsWith(".bmp") ){
			iv_wechat.setBackgroundResource(R.drawable.pic_content_small_jpg);
		}
	}
	
	public void setData(WechatAttachment data){
		if (data!=null) {
			this.wechatAttachment = data;
			String name = data.getName();
			tv_wechat_attachment.setText(name);
			url = data.getUrl();
			int index = url.lastIndexOf("/");
			fileName = url.substring(index + 1);
			setImg(fileName);
		}
		
	}
	
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			readAttachment();
		}
	};
	
	public View getView(){
		return view;
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
			}else if(url.endsWith(".3gp")){//录音文件
				if (url.startsWith("http")) {
					openFile(fileName);
				}else{
					File file = new File(Constants.WECHAT_PATH_LOAD+fileName);
					if (file.exists()) {
						Intent intent = new Intent(context, RecordAuditionActivity.class);
						intent.putExtra("url", Constants.WECHAT_PATH_LOAD+fileName);
						intent.putExtra("isLoc", true);
						context.startActivity(intent);
					}else{
						File file_1 = new File(Constants.RECORD_PATH+fileName);
						if (file_1.exists()) {
							Intent intent = new Intent(context, RecordAuditionActivity.class);
							intent.putExtra("url", Constants.RECORD_PATH+fileName);
							intent.putExtra("isLoc", true);
							context.startActivity(intent);
						}else{
							Toast.makeText(context, R.string.wechat_content48, Toast.LENGTH_SHORT).show();
						}
					}
				}
			}else if(url.endsWith(".mp4")){
				if (url.startsWith("http")) {
					openFile(fileName);
				}else{
					File file = new File(Constants.WECHAT_PATH_LOAD+fileName);
					if (file.exists()) {
						 Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
						 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						 intent.putExtra("oneshot", 0);
						  intent.putExtra("configchange", 0);
						  Uri uri = Uri.fromFile(file);
						 intent.setDataAndType(uri, "video/*");
					     context.startActivity(intent);
					}
				}
			}else if(url.endsWith(".zip")){
				Toast.makeText(context, R.string.un_support_zip, Toast.LENGTH_SHORT).show();
			}else if(url.endsWith(".rar")){
				Toast.makeText(context, R.string.un_support_rar, Toast.LENGTH_SHORT).show();
			}else{//默认是图片
			
				int index = url.lastIndexOf("/");
				String name  = url.substring(index + 1);
				File file = new File(Constants.WECHAT_PATH_LOAD+name);
				Intent intent = new Intent(context, PhotoPriviewActivity.class);	
				if (file.exists()) {
					intent.putExtra("isLocal", true);
					intent.putExtra("url", file.getAbsolutePath());
				}else{
					intent.putExtra("url", url);
				}
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
		String path = Constants.WECHAT_PATH_LOAD + name;
		File file = new File(path);
		if (file == null || !file.exists()){
			downAttachment(name);
		}else{
			if (path.endsWith(".3gp")) {
				Intent intent = new Intent(context, RecordAuditionActivity.class);
				intent.putExtra("url", Constants.WECHAT_PATH_LOAD+fileName);
				intent.putExtra("isLoc", true);
				context.startActivity(intent);
			}else if(path.endsWith(".mp4")){
				Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
				 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				 intent.putExtra("oneshot", 0);
				  intent.putExtra("configchange", 0);
				  Uri uri = Uri.fromFile(file);
				 intent.setDataAndType(uri, "video/*");
			     context.startActivity(intent);
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
			int result = new HttpHelper(context).downloadNotifyAttachment(url, Constants.WECHAT_PATH_LOAD, fileName);
			isContinue(String.valueOf(result));
			return String.valueOf(result);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			dialog.dismiss();
			if (!TextUtils.isEmpty(result) && "0".equals(result)) {//下载成功
				File file = new File(Constants.WECHAT_PATH_LOAD+fileName);
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
					File file = new File(Constants.WECHAT_PATH_LOAD+fileName);
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
		File file = new File(Constants.WECHAT_PATH_LOAD+fileName);
		if (file!=null && file.exists()) {
			file.delete();
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.tip);
		builder.setMessage(R.string.down_failure_retry);
		builder.setPositiveButton(R.string.submit_btn_again, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				downAttachment(fileName);
			}
		});
		builder.setNegativeButton(R.string.Cancle, null);
		builder.setCancelable(false);
		builder.create().show();
	}

	public void setShowVoiceReferView(View showVoiceReferView) {
		this.showVoiceReferView = showVoiceReferView;
	}


	
	
}

