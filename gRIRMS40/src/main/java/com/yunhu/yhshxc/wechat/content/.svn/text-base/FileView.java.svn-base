package com.yunhu.yhshxc.wechat.content;

import java.io.File;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.dragimage.PhotoPriviewActivity;
import com.yunhu.yhshxc.list.activity.RecordAuditionActivity;
import com.yunhu.yhshxc.wechat.Util.SharedPrefrencesForWechatUtil;

public class FileView implements OnClickListener{
	
	private Context context;
	private View view;
	private ContentControlListener contentControlListener;
	private ImageView iv_file_items;
	private Boolean isHave = false;//判断删除按钮的显示、隐藏
	private ImageButton ib_delete_file_items;
	private TextView tv_file_items;
	private String filePaht;
	private LinearLayout ll_file;
	public FileView(Context context) {
		this.context = context;
		view = View.inflate(context, R.layout.activity_wechat_content_fileview, null);
		ll_file = (LinearLayout)view.findViewById(R.id.ll_file);
		iv_file_items = (ImageView) view.findViewById(R.id.iv_file_items);
		tv_file_items = (TextView) view.findViewById(R.id.tv_file_items);
		ib_delete_file_items = (ImageButton) view.findViewById(R.id.ib_delete_file_items);
		
		iv_file_items.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				if (isHave) {
					ib_delete_file_items.setVisibility(View.GONE);
					isHave = false;
				} else {
					ib_delete_file_items.setVisibility(View.VISIBLE);
					isHave = true;
				}
				
				return false;
			}
		});
		
		ll_file.setOnClickListener(this);
	}
	
	
	public String getFilePaht() {
		return filePaht;
	}


	public void setFilePaht(String filePaht) {
		this.filePaht = filePaht;
	}


	public void setFileView(ContentFile file) {
		ib_delete_file_items.setOnClickListener(this);
		String name = SharedPrefrencesForWechatUtil.getInstance(context).getGetFileName(file.getName());
		tv_file_items.setText(TextUtils.isEmpty(name)?file.getName():name);
		String filePath = file.getFilePath();
		
		if(filePath.endsWith(".doc") || filePath.endsWith(".docx")){
			iv_file_items.setBackgroundResource(R.drawable.pic_content_word);
		}else if(filePath.endsWith(".xls") || filePath.endsWith(".xlsx")){
			iv_file_items.setBackgroundResource(R.drawable.pic_content_excel);
		}else if(filePath.endsWith(".ppt") || filePath.endsWith(".pptx")){
			iv_file_items.setBackgroundResource(R.drawable.pic_content_ppt);
		}else if(filePath.endsWith(".pdf")){
			iv_file_items.setBackgroundResource(R.drawable.pic_content_pdf);
		}else if(filePath.endsWith(".txt")){
			iv_file_items.setBackgroundResource(R.drawable.pic_content_txt);
		}else if(filePath.endsWith(".png") || filePath.endsWith(".PNG") || filePath.endsWith(".jpg") || filePath.endsWith(".JPG") 
				|| filePath.endsWith(".GIF") || filePath.endsWith(".gif") || 
        		filePath.endsWith(".JPEG") || filePath.endsWith(".jpeg")|| 
        		filePath.endsWith(".BMP") || filePath.endsWith(".bmp") ){
			iv_file_items.setBackgroundResource(R.drawable.pic_content_jpg);
		}else if(filePath.endsWith(".3gp") || filePath.endsWith(".3GP")){
			iv_file_items.setBackgroundResource(R.drawable.pic_content_3gp);
		}else if(filePath.endsWith(".mp4") || filePath.endsWith(".MP4")){
			iv_file_items.setBackgroundResource(R.drawable.pic_content_mp4);
		}
        	
        	
	}
	
	public View getView() {
		return view;
	}
	
	public void setContentControlListener(
			ContentControlListener contentControlListener) {
		this.contentControlListener = contentControlListener;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_delete_file_items:
			showDialog();
//			contentControlListener.contentFileControl(this);
			break;
		case R.id.ll_file:
			pirview();
			break;

		default:
			break;
		}
	}
	
	
	private void showDialog(){
		new AlertDialog.Builder(context)
		.setMessage(R.string.is_confirm_delete)
		.setPositiveButton(R.string.Confirm,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						contentControlListener.contentFileControl(FileView.this);
						dialog.dismiss();
					}
				})
		.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						dialog.dismiss();
					}
				}).setCancelable(false).show();
	}
	
	private void pirview(){
		if (!TextUtils.isEmpty(filePaht)) {
			if (filePaht.endsWith(".doc") || filePaht.endsWith(".docx")) {//word文档
				openFile();
			}else if(filePaht.endsWith(".xls") || filePaht.endsWith(".xlsx")){//excel
				openFile();
			}else if(filePaht.endsWith(".ppt") || filePaht.endsWith(".pptx")){//ppt
				openFile();
			}else if(filePaht.endsWith(".pdf")){//pdf
				openFile();
			}else if(filePaht.endsWith(".txt")){//txt 
				openFile();
			}else if(filePaht.endsWith(".3gp")){
				openFile();
			}else if(filePaht.endsWith(".mp4")){
				openFile();
			}else if(filePaht.endsWith(".zip")){
				Toast.makeText(context, R.string.un_support_zip, Toast.LENGTH_SHORT).show();
			}else if(filePaht.endsWith(".rar")){
				Toast.makeText(context, R.string.un_support_rar, Toast.LENGTH_SHORT).show();
			}else{//默认是图片
				File file = new File(filePaht);
				Intent intent = new Intent(context, PhotoPriviewActivity.class);	
				intent.putExtra("isLocal", true);
				intent.putExtra("url", file.getAbsolutePath());
				context.startActivity(intent);

			}
		}
	
	}
	
	private void openFile(){
		File file = new File(filePaht);
		if (file == null || !file.exists()){
			Toast.makeText(context, R.string.work_summary_c7, Toast.LENGTH_SHORT).show();
		}else{
			if (filePaht.endsWith(".3gp")) {
				Intent intent = new Intent(context, RecordAuditionActivity.class);
				intent.putExtra("url", filePaht);
				intent.putExtra("isLoc", true);
				context.startActivity(intent);
			}else if(filePaht.endsWith(".mp4")){
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

}
