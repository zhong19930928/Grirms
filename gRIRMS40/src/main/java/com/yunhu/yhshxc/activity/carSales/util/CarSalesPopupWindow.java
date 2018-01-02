package com.yunhu.yhshxc.activity.carSales.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.dragimage.PhotoPriviewActivity;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.FileHelper;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.widget.ToastOrder;

public class CarSalesPopupWindow {
	/**
	 * 用于显示拍照的弹出窗口
	 */
	private PopupWindow popupWindow = null;
	private Context context = null;

	/**
	 * 构造方法
	 */
	public CarSalesPopupWindow(Context context) {
		this.context = context;
	}
	/**
	 * @param anchor 弹出窗口所基于的view。如果anchor为null，则基于R.id.ll_btn_layout组件的位置显示，
	 * 否则就基于anchor组件的位置显示
	 */
	public  ImageView iv1;
	public  ImageView iv2;
	public  TextView tv1;
	public  TextView tv2;
	public Button btn_cancel_one;
	public Button btn_cancel_two;
	public void setImageViewBitmap(Bitmap bit,int order){
		if(order == 0){
			iv1.setVisibility(View.VISIBLE);
			iv1.setImageBitmap(bit);
			btn_cancel_one.setVisibility(View.VISIBLE);
			tv1.setVisibility(View.GONE);
		}else if(order ==1){
			iv2.setVisibility(View.VISIBLE);
			iv2.setImageBitmap(bit);
			btn_cancel_two.setVisibility(View.VISIBLE);
			tv2.setVisibility(View.GONE);
		}
		
	}
	public Bitmap getBitmap(String url){
		 Bitmap bitmap = BitmapFactory.decodeFile(url);  
		return bitmap;
		
	}
	public void show(View anchor,String url1,String url2) {
		View contentView = View.inflate(context, R.layout.order3_take_photo_popupwindow, null);
		contentView.findViewById(R.id.ll_photo_album).setOnClickListener(clickListener);//选择拍照
		
		contentView.findViewById(R.id.ll_photo).setOnClickListener(clickListener);//选择拍照
		iv1 = (ImageView) contentView.findViewById(R.id.iv_take_photo_one);
		iv2 = (ImageView) contentView.findViewById(R.id.iv_take_photo_two);
		tv1 = (TextView) contentView.findViewById(R.id.tv_take_photo_one);
		tv2 = (TextView) contentView.findViewById(R.id.tv_take_photo_two);
		btn_cancel_one = (Button) contentView.findViewById(R.id.btn_cancel_one);
		btn_cancel_one.setOnClickListener(clickListener);
		btn_cancel_two = (Button) contentView.findViewById(R.id.btn_cancel_two);
		btn_cancel_two.setOnClickListener(clickListener);
		if(!TextUtils.isEmpty(url1)){
			iv1.setVisibility(View.VISIBLE);
			tv1.setVisibility(View.GONE);
			iv1.setImageBitmap(getBitmap(Constants.CARSALES_PATH+url1));
			btn_cancel_one.setVisibility(View.VISIBLE);
		}
		if(!TextUtils.isEmpty(url2)){
			iv2.setVisibility(View.VISIBLE);
			tv2.setVisibility(View.GONE);
			iv2.setImageBitmap(getBitmap(Constants.CARSALES_PATH+url2));
			btn_cancel_two.setVisibility(View.VISIBLE);
		}
		btn_cancel_one = (Button) contentView.findViewById(R.id.btn_cancel_one);
		btn_cancel_two = (Button) contentView.findViewById(R.id.btn_cancel_two);
		btn_cancel_one.setOnClickListener(clickListener);
		btn_cancel_two.setOnClickListener(clickListener);

		//根据屏幕高度/2.5的值来设置弹出窗口的高度
		int pwHeight = PublicUtils.convertPX2DIP(context, (Double.valueOf(Math.floor(Constants.SCREEN_HEIGHT / 2.5))).intValue());
		popupWindow = new PopupWindow(contentView, WindowManager.LayoutParams.FILL_PARENT, pwHeight, true);
		popupWindow.setAnimationStyle(R.style.popupwindow);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.update();//更新弹出窗口的状态
		popupWindow.setBackgroundDrawable(new BitmapDrawable());//设置一个默认的背景
		
		//设置弹出窗口的位置（基于哪个背景层组件显示）
		if (anchor == null) {
			// 设置弹出窗口的位置为水平居中且对齐R.id.gridview_home组件的底部
			popupWindow.showAtLocation(((Activity) context).findViewById(R.id.ll_home), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); 
		}
		else {
			//根据设置弹出窗口的位置
			int y = Constants.SCREEN_HEIGHT - anchor.getHeight() - pwHeight;
			popupWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, 0, y);
		}

	}

	/**
	 * 拍照和删除按钮的处理事件处理
	 */
	private final View.OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				//直接拍照
				case R.id.ll_photo:
					
				if (!TextUtils.isEmpty(SharedPreferencesForCarSalesUtil.getInstance(context).getPhotoNameOne())) {
					priviewPhoto(Constants.CARSALES_PATH+SharedPreferencesForCarSalesUtil.getInstance(context).getPhotoNameOne());
				} else {
					photoOrder = 0 ;
					camera();
				}
					break;
					
				//选择照片
				case R.id.ll_photo_album:
					
					if(!TextUtils.isEmpty(SharedPreferencesForCarSalesUtil.getInstance(context).getPhotoNameTwo())){
						priviewPhoto(Constants.CARSALES_PATH+SharedPreferencesForCarSalesUtil.getInstance(context).getPhotoNameTwo());
					}else{
						photoOrder = 1;
						camera();
					}
					break;
				case R.id.btn_cancel_one:
					btn_cancel_one.setVisibility(View.GONE);
					iv1.setVisibility(View.GONE);
					tv1.setVisibility(View.VISIBLE);
					SharedPreferencesForCarSalesUtil.getInstance(context).setPhotoNameOne(null);
					break;
				case R.id.btn_cancel_two:
					btn_cancel_two.setVisibility(View.GONE);
					iv2.setVisibility(View.GONE);
					tv2.setVisibility(View.VISIBLE);
					SharedPreferencesForCarSalesUtil.getInstance(context).setPhotoNameTwo(null);
					break;
			}
//			close();//关闭弹出窗口
			
		}
	};
	
	/**
	 * 照片预览
	 * @param path 照片路径
	 */
	private void priviewPhoto(String path){
		Intent intent = new Intent(context,PhotoPriviewActivity.class);
		intent.putExtra("url", path);
		intent.putExtra("isLocal", true);
		context.startActivity(intent);
	}

	/**
	 * 关闭弹出窗口
	 */
	public void close() {
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
		}
	}
	String photoFileName1 = null;
	String photoFileName2 = null;
	private int photoOrder  = 0;
	public int getPhotoOrder(){
		
		return photoOrder;
	}
	
	public void camera() {
		Intent intentPhote = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intentPhote.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		String pathUrl = Constants.SDCARD_PATH + "/temp/";
		File filePreview = new File(pathUrl);
		if (!filePreview.exists()) {
			filePreview.mkdir();
		}
		File file = null;
		if(photoOrder == 0){
			photoFileName1 = System.currentTimeMillis() + ".jpg";
			SharedPreferencesForCarSalesUtil.getInstance(context).setPhotoNameOne(photoFileName1);
			file = new File(filePreview, photoFileName1);
		}else if(photoOrder == 1){
			photoFileName2 = System.currentTimeMillis() + ".jpg";
			SharedPreferencesForCarSalesUtil.getInstance(context).setPhotoNameTwo(photoFileName2);
			file = new File(filePreview, photoFileName2);
		}
		Uri uri = Uri.fromFile(file);
		// 获取拍照后未压缩的原图片，并保存在uri路径中
		intentPhote.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		((Activity) context).startActivityForResult(intentPhote, 2000);
	}
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		if (Activity.RESULT_OK == resultCode && requestCode == 2000) {
			String tempPath = null;
			if(photoOrder == 0){
				tempPath = Constants.SDCARD_PATH + "/temp/"+ photoFileName1;
			}else if(photoOrder == 1){
				tempPath = Constants.SDCARD_PATH + "/temp/" +photoFileName2;
			}
			File tempFile = new File(tempPath);
			if (!tempFile.exists()) {
				ToastOrder.makeText(context, "图片丢失", ToastOrder.LENGTH_SHORT).show();
			}else{
				Bitmap bitmap = null;
				if(photoOrder == 0){
					bitmap = setPhoto(photoFileName1);
				}else if(photoOrder == 1){
					bitmap = setPhoto(photoFileName2);
				}
				setImageViewBitmap(bitmap,photoOrder);// 照片
			}
		}else{
			if(photoOrder == 0){
				photoFileName1 = "";
				SharedPreferencesForCarSalesUtil.getInstance(context).setPhotoNameOne(photoFileName1);
			}else if(photoOrder == 1){
				photoFileName2 = "";
				SharedPreferencesForCarSalesUtil.getInstance(context).setPhotoNameTwo(photoFileName2);
			}
		}
	}
	
	public Bitmap setPhoto(String fileName){
		String tempPath = Constants.PATH_TEMP + fileName;
		Bitmap bitmap = FileHelper.compressImage(tempPath,100/50);
//		Bitmap bitmapAddWater = settingWatermark(bitmap);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
		saveFile(baos.toByteArray(), fileName);
		File tempFile = new File(tempPath);
		if (tempFile.exists()) {
			tempFile.delete();
		}
		return bitmap;
	}

	private void saveFile(byte[] data, String fileName) {
		OutputStream os = null;
		try {
			File dir = new File(Constants.CARSALES_PATH);
			if(!dir.exists()){
				dir.mkdirs();
			}
			File file = new File(Constants.CARSALES_PATH + fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			os = new FileOutputStream(file);
			os.write(data, 0, data.length);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

	}
}
