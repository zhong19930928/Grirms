package com.yunhu.yhshxc.attendance.leave;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.yunhu.yhshxc.utility.Constants;

public class ImageViewUtils {
	public static final int INTENT_CAPTURE = 10;// 相机请求码
	public static final int INTENT_PICTURE = 11;// 相册请求码
//	public static final int INTENT_CROP = 12;// 裁剪
	public static Uri imageUri;
	public static final String TEMP_IMAGE_DIR = Constants.LEAVE_PAHT;
	// =================================================================
	public static final String TEMPIMAGE =TEMP_IMAGE_DIR; // 临时存储图片路径

	/**
	 * 跳转到系统相机 并将拍摄照片（原图）存储到指定位置 系统默认不返回缩略图
	 * 
	 * @param context
	 */
	public static void GoCaptureAndSavePicture(Context context,String name) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri(name)); // 拍照图片保存地址
		((Activity) context).startActivityForResult(intent, INTENT_CAPTURE);

	}

	/**
	 * 跳转到相册
	 *
	 * @param context
	 */
	public static void GoAlbum(Context context) {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
			// 4.4
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);// ACTION_OPEN_DOCUMENT
			intent.addCategory(Intent.CATEGORY_OPENABLE);
			intent.setType("image/*");
			((Activity) context).startActivityForResult(intent, INTENT_PICTURE);
		} else {
			Intent intent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			intent.setType("image/*");
			((Activity) context).startActivityForResult(intent, INTENT_PICTURE);
		}

	}


	/**
	 * 图片裁剪
	 *
	 * @param context
	 * @param file
	 *            将裁剪后的图片存储到
	 */
//	public static void GoCropAndSave(Context context, File file, String name) {
//		Intent intent = new Intent("com.android.camera.action.CROP");
//		Uri uri = Uri.fromFile(file);
//		intent.setDataAndType(uri, "image/*");
//
//		intent.putExtra("crop", "true");// 发送裁剪信号
//		intent.putExtra("aspectX", 1);// X方向上的比例
//		intent.putExtra("aspectY", 1);
//		intent.putExtra("outputX", 500);// 裁剪区的宽
//		intent.putExtra("outputY", 500);
//		intent.putExtra("return-data", false);// 是否将数据保留在Bitmap中返回
//		intent.putExtra("noFaceDetection", true);// 是否无人脸识别
//		intent.putExtra("scale", true);// 是否保留比例
//		intent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri(name));
//		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());// 将URI指向相应的file//图片格式
//		((Activity) context).startActivityForResult(intent, INTENT_CROP);
//	}

	private static Uri getTempUri(String name) {
		File file = createFile(TEMPIMAGE + name, FileType.FILE);
		imageUri = Uri.fromFile(file);
		return imageUri;
	}

	public enum FileType {
		FILE, DIRECTORY
	}

	public static File createFile(String filePath, FileType type) {
		File file = new File(filePath);
		switch (type) {
		case FILE:
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			break;
		case DIRECTORY:
			if (!file.exists()) {
				file.mkdirs();
			}
			break;
		}
		return file;
	}

	/**
	 * 判断sdcard是否被挂载
	 */
	public static boolean hasSdcard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 从uri中加载图片
	 * 
	 * @param uri
	 * @param context
	 * @return
	 */
	public static Bitmap getBitmapFromUri(Uri uri, Context context) {
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(context.getContentResolver()
					.openInputStream(uri));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}

		return bitmap;

	}
	
	/**
	 *从文件path加载图片
	 */
	public static Bitmap getBitmapFromFilePath(String filePath,Context context){
		 File f = new File(filePath);  
	        if (f.exists()) { /* 产生Bitmap对象，并放入mImageView中 */  
	            Bitmap bm = BitmapFactory.decodeFile(filePath);  
	            return bm; 
	        } else {  
	            return null;
	        }  
	        
	}

	/**
	 * 图库 获取图片地址 4.4以下
	 *
	 * @param context
	 * @param data
	 * @return
	 */
	public static String getPathLow(Context context, Intent data) {
		Uri selectedImage = data.getData();
		if (selectedImage != null) {
			String uriStr = selectedImage.toString();
			String path = uriStr.substring(10, uriStr.length());
			if (path.startsWith("com.sec.android.gallery3d")) {
				return null;
			}
		}
		String[] filePathColumn = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(selectedImage,
				filePathColumn, null, null, null);
		cursor.moveToFirst();
		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		String picturePath = cursor.getString(columnIndex);
		cursor.close();
		return picturePath;
	}

	/**
	 * 图库 获取图片地址4.4
	 *
	 * @param context
	 * @param data
	 * @return getPath(context, data.getData())
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	@SuppressLint("NewApi")
	public static String getPathHigh(final Context context, Intent data) {
		Uri uri = data.getData();
		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}

			} else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {

			// Return the remote address
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();

			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	private static String getDataColumn(Context context, Uri uri,
			String selection, String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	private static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	private static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	private static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	private static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri
				.getAuthority());
	}
	public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) width / w);
		float scaleHeight = ((float) height / h);
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		return newbmp;
	}
	/**
	 * Save image to the SD card 
	 * @param photoBitmap
	 * @param photoName
	 * @param path
	 */
	public static void savePhotoToSDCard(Bitmap photoBitmap,String path,String photoName){
		if (checkSDCardAvailable()) {
			File dir = new File(path);
			if (!dir.exists()){
				dir.mkdirs();
			}
			
			File photoFile = new File(path , photoName + ".jpg");
			FileOutputStream fileOutputStream = null;
			try {
				fileOutputStream = new FileOutputStream(photoFile);
				if (photoBitmap != null) {
					if (photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)) {
						fileOutputStream.flush();
//						fileOutputStream.close();
					}
				}
			} catch (FileNotFoundException e) {
				photoFile.delete();
				e.printStackTrace();
			} catch (IOException e) {
				photoFile.delete();
				e.printStackTrace();
			} finally{
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} 
	}
	/**
	 * Check the SD card 
	 * @return
	 */
	public static boolean checkSDCardAvailable(){
		return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	}

}
