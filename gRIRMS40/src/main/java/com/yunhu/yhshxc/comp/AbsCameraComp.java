package com.yunhu.yhshxc.comp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.LocationResult;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.FileHelper;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gcg.org.debug.JLog;

/**
 * 拍照控件基类
 * @author gcg_jishen
 *
 */
public abstract class AbsCameraComp extends Component {

	protected Context context;
	private LocationResult locationResult;//定位结果
	private String waterMark;//自定义水印内容
	private String waterStroe;//店面水印
	public int menuId;
	public int menuType;
	public String menuName;

	public int getMenuId() {
		return menuId;
	}
	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}
	public int getMenuType() {
		return menuType;
	}
	public void setMenuType(int menuType) {
		this.menuType = menuType;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	
	public AbsCameraComp(Context context, Func func) {
		this.context = context;
		compFunc = func;
		this.type = func.getType();
		this.param = func.getFuncId();
	}
	
	
	/**
	 * 开始拍照
	 * @param fileName 照片名称
	 */
	public void startPhoto(String fileName) {
		JLog.d("开始拍照  照片名称："+fileName);
		String mtype = android.os.Build.MODEL;
		if(!TextUtils.isEmpty(mtype) && mtype.matches(".*vivo.*")){
			((AbsBaseActivity) context).application.isSubmitActive = false;
			Intent mIntent = new Intent("android.media.action.IMAGE_CAPTURE");
			String tempPath = Constants.SDCARD_PATH + "/temp/";
			File tmpFile = new File(tempPath);
			if (!tmpFile.exists()) {
				tmpFile.mkdir();
			}
			File file = new File(tempPath, fileName);
			Uri originalUri = Uri.fromFile(file);
			mIntent.putExtra(MediaStore.EXTRA_OUTPUT, originalUri);
			((Activity) context).startActivityForResult(mIntent, 100);
		}else{
			((AbsBaseActivity) context).application.isSubmitActive = false;
			Intent intent = new Intent();
			Intent intent_camera = context.getPackageManager().getLaunchIntentForPackage("com.android.camera");
			if (intent_camera != null) {
			    intent.setPackage("com.android.camera");
			}
			intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
			String tempPath = Constants.SDCARD_PATH + "/temp/";
			File tmpFile = new File(tempPath);
			if (!tmpFile.exists()) {
				tmpFile.mkdir();
			}
			File file = new File(tempPath, fileName);
			Uri originalUri = Uri.fromFile(file);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, originalUri);
			((Activity) context).startActivityForResult(intent, 100);
		}
		
		
	}
	/**
	 * 只针对表格控件拍照
	 * @param fileName
	 */
	public void startPhotoTable(String fileName) {
		String mtype = android.os.Build.MODEL;
		if(!TextUtils.isEmpty(mtype) && mtype.matches(".*vivo.*")){
			((AbsBaseActivity) context).application.isSubmitActive = false;
			Intent mIntent = new Intent("android.media.action.IMAGE_CAPTURE");
			String tempPath = Constants.SDCARD_PATH + "/temp/";
			File tmpFile = new File(tempPath);
			if (!tmpFile.exists()) {
				tmpFile.mkdir();
			}
			File file = new File(tempPath, fileName);
			Uri originalUri = Uri.fromFile(file);
			mIntent.putExtra(MediaStore.EXTRA_OUTPUT, originalUri);
			((Activity) context).startActivityForResult(mIntent, 109);
		}else{
			((AbsBaseActivity) context).application.isSubmitActive = false;
			Intent intent = new Intent();
			Intent intent_camera = context.getPackageManager().getLaunchIntentForPackage("com.android.camera");
			if (intent_camera != null) {
			    intent.setPackage("com.android.camera");
			}
			intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
			String tempPath = Constants.SDCARD_PATH + "/temp/";
			File tmpFile = new File(tempPath);
			if (!tmpFile.exists()) {
				tmpFile.mkdir();
			}
			File file = new File(tempPath, fileName);
			Uri originalUri = Uri.fromFile(file);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, originalUri);
			((Activity) context).startActivityForResult(intent, 109);
		}
		
		
	}
	/**
	 * 从相册中选择照片
	 */
	public void startPhotoAlbum(){
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
		((Activity) context).startActivityForResult(intent, 104);
	}

	/**
	 * 从相册中选择照片,针对表格里的操作
	 */
	public void startPhotoAlbumTable(){
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
		((Activity) context).startActivityForResult(intent, 110);
	}
	public abstract void resultPhoto(String fileName,Date date);

	/**
	 * 设置照片
	 * @param fileName 照片名称
	 */
	public void settingPhoto(String fileName,Date date) {
		if (compFunc.getType() == Func.TYPE_CAMERA_CUSTOM) {
			setCustomPhoto(fileName,date);
		}else{
			String tempPath = Constants.PATH_TEMP + fileName;
			Bitmap bitmap = compressImage(tempPath);
			Bitmap bitmapAddWater = settingWatermark(bitmap,date);
			if(bitmapAddWater!=null){
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				bitmapAddWater.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				FileHelper.saveFile(baos.toByteArray(), fileName);
				File tempFile = new File(tempPath);
				if (tempFile.exists()) {
					tempFile.delete();
				}
				bitmapAddWater.recycle();
				System.gc();
			}
		}
	}
	
	public void setCustomPhoto(String fileName,Date date){
		String tempPath = Constants.PATH_TEMP + fileName;
		Bitmap bitmap = FileHelper.compressImage(tempPath,compFunc.getLength()==null?1:100/compFunc.getLength());
		Bitmap bitmapAddWater = settingWatermark(bitmap,date);
		if(bitmapAddWater!=null){
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmapAddWater.compress(Bitmap.CompressFormat.JPEG, 90, baos);
			FileHelper.saveFile(baos.toByteArray(), fileName);
			File tempFile = new File(tempPath);
			if (tempFile.exists()) {
				tempFile.delete();
			}
			bitmap.recycle();
			System.gc();
		}
		
	}
	/**
	 * 压缩照片
	 * @param photoPath 照片路径
	 * @return
	 */
	public Bitmap compressImage(String photoPath){
		Bitmap  bitmap = null;
		int type = compFunc.getType();
		switch (type) {
		case Func.TYPE_CAMERA:
			bitmap = FileHelper.compressImage(photoPath, 240,320);
			break;
		case Func.TYPE_CAMERA_MIDDLE:
			bitmap = FileHelper.compressImage(photoPath, 320,480);
			break;
		case Func.TYPE_CAMERA_HEIGHT:
			bitmap = FileHelper.compressImage(photoPath, 480,800);
			break;
		default:
			bitmap = FileHelper.compressImage(photoPath, 240,320);
			break;
		}
		return bitmap;
	}
	
	/**
	 * 设置照片水印
	 * @param bitmap
	 * @return
	 */
	private Bitmap settingWatermark(Bitmap bitmap,Date date) {
		List<String> water = new ArrayList<String>();
		
		/**
		 * 自定义的水印内容
		 */
		if (!TextUtils.isEmpty(waterMark)) {
			water.add(waterMark);
		}
		
		//位置信息
		if (locationResult != null) {
			switch (compFunc.getPhotoLocationType()) {
			case 1:// 显示经纬度
				water.add(String.valueOf(locationResult.getLongitude()));
				water.add(String.valueOf(locationResult.getLatitude()));
				break;
			case 2:// 显示地址
				water.add(TextUtils.isEmpty(locationResult.getAddress()) || locationResult.getAddress().equals("null") ? "未获取到地址"
						: locationResult.getAddress());
				break;
			case 3:// 显示经纬度和地址
				water.add(TextUtils.isEmpty(locationResult.getAddress()) || locationResult.getAddress().equals("null") ? "未获取到地址"
						: locationResult.getAddress());
				water.add(String.valueOf(locationResult.getLongitude()));
				water.add(String.valueOf(locationResult.getLatitude()));
				break;
			default:
				break;
			}
		}
		
		
		//日期时间信息
		switch (compFunc.getPhotoTimeType()) {
		case 1:// 带日期
//			water.add(DateUtil.getCurDate());
			if(null!=date){
				water.add(DateUtil.dateToDateString(date,DateUtil.DATAFORMAT_STR));
			}else{
				water.add(context.getResources().getString(R.string.no_net_time));
			}
			break;
		case 2:// 带日期 带时间
//			water.add(DateUtil.getCurDateTime());
			if(null!=date){
				water.add(DateUtil.dateToDateString(date,DateUtil.DATATIMEF_STR));
			}else{
				water.add(context.getResources().getString(R.string.no_net_time));
			}
			break;
		default:
			break;
		}
		
		//店面
		if (!TextUtils.isEmpty(waterStroe)) {
			water.add(waterStroe);
		}
		
		//添加用户水印
		if (compFunc.getIsImgUser() == 1) {
			water.add(SharedPreferencesUtil.getInstance(context).getUserName());
		}


		if (water.size() > 0) {
			bitmap = FileHelper.createWatermark(bitmap, water);
		}

		return bitmap;
	}
	
	public LocationResult getLocationResult() {
		return locationResult;
	}

	public void setLocationResult(LocationResult locationResult) {
		this.locationResult = locationResult;
	}

	public String getWaterMark() {
		return waterMark;
	}

	public void setWaterMark(String waterMark) {
		this.waterMark = waterMark;
	}

	public String getWaterStroe() {
		return waterStroe;
	}

	public void setWaterStroe(String waterStroe) {
		this.waterStroe = waterStroe;
	}
	
	
}
