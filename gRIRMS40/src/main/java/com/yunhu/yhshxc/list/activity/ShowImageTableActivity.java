package com.yunhu.yhshxc.list.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.http.HttpEntity;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.bo.SubmitItem;
import com.yunhu.yhshxc.database.SubmitItemDB;
import com.yunhu.yhshxc.database.SubmitItemTempDB;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.FileHelper;
import com.yunhu.yhshxc.utility.HttpHelper;
import com.yunhu.yhshxc.widget.ToastOrder;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import gcg.org.debug.JLog;

public class ShowImageTableActivity extends AbsBaseActivity {

	// 图片下载完毕
	protected static final int LOAD_IMAGE_OVER = 300;
	protected static final int LOAD_IMAGE_FAIL = 404;
	private static final String TAG = "ShowImageActivity";
	private ImageView iv_image;
	private Button btn_del;
	private TextView tv_name;
	private Dialog dialog;
	private boolean isTableImage;
	private PopupWindow popupWindow;
	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			closeDialog();
			switch (msg.what) {
			case LOAD_IMAGE_OVER:
				// 获取图片路径
				String imagePathName = (String) msg.obj;
				showImage(imagePathName);
				break;

			case LOAD_IMAGE_FAIL:
				ToastOrder.makeText(ShowImageTableActivity.this, setString(R.string.activity_after_16), 1).show();
				ShowImageTableActivity.this.finish();
				break;
			}
		
			
		};
	};
	private String imageUrl;//照片下载URL
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_image);
		DisplayMetrics  metric = new  DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		screenW = metric.widthPixels;
		screenH = metric.heightPixels;
		initBase();
		imageUrl = getIntent().getStringExtra("imageUrl");
		String imageName = getIntent().getStringExtra("imageName");
		isTableImage=getIntent().getBooleanExtra("PreviewTable", false);
		iv_image = (ImageView) findViewById(R.id.iv_show_image_img);
		tv_name = (TextView) findViewById(R.id.tv_show_image_name);
		btn_del=(Button)findViewById(R.id.btn_show_image_delete);
		tv_name.setText(imageName);
		String imagePathName=null;
		if(isTableImage){
			if(imageUrl.startsWith("http:") && (imageUrl.endsWith(".jpg") || imageUrl.endsWith(".png") || imageUrl.endsWith(".JPG") || imageUrl.endsWith(".PNG") || imageUrl.endsWith(".bmp") || imageUrl.endsWith(".BMP") ||  imageUrl.endsWith(".gif") || imageUrl.endsWith(".GIF") || imageUrl.endsWith(".jpeg") ||  imageUrl.endsWith(".JPEG"))){
				String[] splitName = imageUrl.split("/");
				// 图片路径
				imagePathName = Constants.TEMP_IMAGE_PATH+splitName[splitName.length-1];
			}else{
				imagePathName = Constants.SDCARD_PATH+imageUrl;
			}
			btn_del.setVisibility(View.VISIBLE);
		}else{
			btn_del.setVisibility(View.GONE);
			// 从图片地址上获取图片名称
			String[] splitName = imageUrl.split("/");
			// 图片路径
			imagePathName = Constants.TEMP_IMAGE_PATH+splitName[splitName.length-1];
		}
		final File imageFile = new File(imagePathName);
		if(imageFile.exists()&&!(imageFile.isDirectory())){
//			Bitmap bitmap = BitmapFactory.decodeFile(imagePathName);
			Bitmap bitmap = FileHelper.compressImagePriview(imagePathName, screenW, screenH);
			if(bitmap != null && bitmap.getWidth() > 1){
				showImage(imagePathName);
			}else{
				if(isTableImage){
					if(imageUrl.startsWith("http:") && (imageUrl.endsWith(".jpg") || imageUrl.endsWith(".png") || imageUrl.endsWith(".JPG") || imageUrl.endsWith(".PNG") || imageUrl.endsWith(".bmp") || imageUrl.endsWith(".BMP") ||  imageUrl.endsWith(".gif") || imageUrl.endsWith(".jpeg") || imageUrl.endsWith(".GIF") || imageUrl.endsWith(".JPEG"))){
						LoadImageFromUrl(imageFile, imageUrl);
					}else{
						ToastOrder.makeText(getApplicationContext(), setString(R.string.activity_after_15), ToastOrder.LENGTH_LONG).show();
						finish();
					}
				}else{
					LoadImageFromUrl(imageFile, imageUrl);
				}
			}
		}else{
			if(isTableImage){
				if(imageUrl.startsWith("http:") &&  (imageUrl.endsWith(".jpg") || imageUrl.endsWith(".png") || imageUrl.endsWith(".JPG") || imageUrl.endsWith(".PNG") || imageUrl.endsWith(".bmp") || imageUrl.endsWith(".BMP") ||  imageUrl.endsWith(".gif") || imageUrl.endsWith(".jpeg") || imageUrl.endsWith(".GIF") || imageUrl.endsWith(".JPEG"))){
					LoadImageFromUrl(imageFile, imageUrl);
				}else{
					ToastOrder.makeText(getApplicationContext(), setString(R.string.activity_after_15), ToastOrder.LENGTH_LONG).show();
					finish();
				}
			}else{
				LoadImageFromUrl(imageFile, imageUrl);
			}
		}
		
		
		btn_del.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDeleteDialog(imageFile.getName(),v);
			}
		});
		
		JLog.d(TAG, "imageUrl===>"+imageUrl);
		JLog.d(TAG, "imageName===>"+imageName);
		JLog.d(TAG, "imagePathName===>"+imagePathName);
	}
	
	
	private void showDeleteDialog(final String name,View view){
		View contentView = View.inflate(this, R.layout.delete_prompt, null);
		popupWindow = new PopupWindow(contentView,WindowManager.LayoutParams.FILL_PARENT,WindowManager.LayoutParams.FILL_PARENT,true);
		popupWindow.setAnimationStyle(R.style.popupwindow);
		int high=getWindowManager().getDefaultDisplay().getHeight();
		popupWindow.showAsDropDown(view,0,-(high/4+15));
		contentView.findViewById(R.id.btn_confirm).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
				int submitItemId=getIntent().getIntExtra("submitItemId", 0);
				boolean isLink=getIntent().getBooleanExtra("isLink", false);
				if(isLink){
					SubmitItemTempDB db=new SubmitItemTempDB(getApplicationContext());
					SubmitItem item=db.findSubmitItemBySubmitItemId(submitItemId);
					item.setNote(imageUrl);
					String newValue=item.getParamValue().replaceAll(name, "");
					item.setParamValue(newValue);
					String newNote=item.getNote().replaceAll(name, "");
					item.setNote(newNote);
					db.updateSubmitItem(item);
				}else{
					SubmitItemDB db=new SubmitItemDB(getApplicationContext());
					SubmitItem item=db.findSubmitItemBySubmitItemId(submitItemId);
					item.setNote(imageUrl);
					String newValue=item.getParamValue().replaceAll(name, "");
					item.setParamValue(newValue);
					String newNote=item.getNote().replaceAll(name, "");
					item.setNote(newNote);
					db.updateSubmitItem(item,false);
				}
				new FileHelper().deleteFile(Constants.SDCARD_PATH + name);
				ShowImageTableActivity.this.finish();
			}
		});
		contentView.findViewById(R.id.btn_canceled).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});
	}
	
	private int screenW,screenH;
	private void showImage(	String imagePathName){
		Bitmap image = FileHelper.compressImagePriview(imagePathName, screenW, screenH);
		// 图片bitmap
//		Bitmap image = BitmapFactory.decodeFile(imagePathName);
		// 设置图片
		iv_image.setImageBitmap(image);
	}
	
	/**
	 * 异步下载图片
	 * @param url 下载图片url
	 * @param locationPath图片存放路径
	 */
	private void LoadImageFromUrl(final File file,final String url) {
//		dialog = initDialog("图片加载中,请稍后...");
		dialog = new MyProgressDialog(this,R.style.CustomProgressDialog,setString(R.string.activity_after_04));
		dialog.show();
		new Thread(){
			public void run() {
				try {
					if(file.exists()){
						file.delete();
					}
					if(!(new File(Constants.TEMP_IMAGE_PATH).exists()) || !(new File(Constants.TEMP_IMAGE_PATH).isDirectory())){
						new File(Constants.TEMP_IMAGE_PATH).mkdir();
					}
					HttpHelper httpHelper = new HttpHelper(ShowImageTableActivity.this);
					// 获取entity
					HttpEntity entity = httpHelper.connectGetReturnEntity(url);
					// 获取输入流
					InputStream is = entity.getContent();
					if (is != null) {
						// 设置输出流
						FileOutputStream fos = new FileOutputStream(file);
						// 缓存
						byte[] bt = new byte[1024];
						// 标志
						int i = 0;
						// 流的对考
						while ((i = is.read(bt)) != -1) {
							// 写入输出流缓冲区
							fos.write(bt, 0, i);
						}
						// 刷新输出流
						fos.flush();
						fos.close();
						is.close();
						
						// message
						Message msg = new Message();
					
						msg.what = LOAD_IMAGE_OVER;
						// 将图片路径设置给msg
						msg.obj = file.getPath();
						// 通知图片下载完毕
						mHandler.sendMessage(msg);
					}else{
						// message
						Message msg = new Message();
					
						msg.what = LOAD_IMAGE_FAIL;
					
					
						mHandler.sendMessage(msg);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
					// message
					Message msg = new Message();
					msg.what = LOAD_IMAGE_FAIL;
					mHandler.sendMessage(msg);
					// 删除不完整图片
					new FileHelper().deleteFile(file.getPath());	
				}
			};
		}.start();
	}
	
	@Override
	protected void onDestroy() {
		closeDialog();
		super.onDestroy();
	}
	
	/**
	 * 关闭对话框
	 */
	private void closeDialog(){
		if(dialog != null && dialog.isShowing()){
			dialog.dismiss();
		}
	}

	private String setString(int stringId){
		return getResources().getString(stringId);
	}

}
