package com.yunhu.yhshxc.wechat.exchange;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.wechat.survey.MyGridView;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class WechatPhotoSelectActivity extends AbsBaseActivity {
	
	private LinearLayout ll_chat_sure,ll_chat_cancel;
	private List<String> imageSrcList = new ArrayList<String>();
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	private DisplayImageOptions options;
	private boolean isFisrt;
	private MyGridView gv_chat_view;
	private String add = "add";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wechat_photo);
		ll_chat_sure = (LinearLayout)findViewById(R.id.ll_chat_sure);
		ll_chat_cancel = (LinearLayout)findViewById(R.id.ll_chat_cancel);
		ll_chat_sure.setOnClickListener(listener);
		ll_chat_cancel.setOnClickListener(listener);
		isFisrt = getIntent().getBooleanExtra("isFirst", false);
		gv_chat_view = (MyGridView)findViewById(R.id.gv_chat_view);
		imageSrcList.remove(add);
		if (isFisrt) {
			PS();
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean("isFirst", isFisrt);
		outState.putString("photoName", photoName);
	}
	
	@Override
	protected void restoreConstants(Bundle savedInstanceState) {
		super.restoreConstants(savedInstanceState);
		isFisrt = savedInstanceState.getBoolean("isFisrt");
		photoName = savedInstanceState.getString("photoName");
	}
	
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ll_chat_sure:
				sure();
				break;
			case R.id.ll_chat_cancel:
				WechatPhotoSelectActivity.this.finish();
				break;

			default:
				break;
			}
		}
	};
	
	/**
	 * 确定选中图片
	 */
	private void sure(){
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i <(imageSrcList.size()-1); i++) {//最后一个是add
			String s = imageSrcList.get(i);
//			int index = s.lastIndexOf("/");
//			String name = s.substring(index + 1);
//			sb.append(",");
//			sb.append(name);
			sb.append(",");
			sb.append(s);
		}
		Intent intent = new Intent();
		if (sb.length()>0) {
			intent.putExtra("data", sb.substring(1));
		}
		setResult(500, intent);
		this.finish();
	}
	
	/*
	 * 从相册中选择
	 */
	private void XC(){
		photoName = System.currentTimeMillis() + ".jpg";
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
		startActivityForResult(intent, 100);
	}
	
	/**
	 * 直接拍照
	 */
	private String photoName;
	private void PS(){
		Intent intent = new Intent();
		Intent intent_camera = this.getPackageManager().getLaunchIntentForPackage("com.android.camera");
		if (intent_camera != null) {
		    intent.setPackage("com.android.camera");
		}
		intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
		File tmpFile = new File(Constants.WECHAT_PATH_LOAD);
		if (!tmpFile.exists()) {
			tmpFile.mkdir();
		}
		photoName = System.currentTimeMillis()+".jpg";
		File file = new File(Constants.WECHAT_PATH_LOAD, photoName);
		Uri originalUri = Uri.fromFile(file);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, originalUri);
		startActivityForResult(intent, 101);
	}
	
	/**
	 * 从相册选择照片后返回
	 * 
	 * @param data
	 */
	private void resultForPhotoAlbum(Intent data) {
		try {
			if (data != null) {
				Uri originalUri = data.getData(); // 获得图片的uri
				if (originalUri != null) {
//					String path = Constants.WECHAT_PATH_LOAD + photoName;
//					Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), originalUri); // 显得到bitmap图片
//					FileHelper.saveBitmapToFile(bitmap, path);
					String path = path(originalUri);
					imageSrcList.add(path);
				} else {
					ToastOrder.makeText(this, R.string.work_summary_c6, ToastOrder.LENGTH_SHORT).show();
				}
			} else {
				ToastOrder.makeText(this, R.string.work_summary_c6, ToastOrder.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
			ToastOrder.makeText(this, R.string.work_summary_c6, ToastOrder.LENGTH_SHORT).show();
		}
	}
	
	
	private String path(Uri uri){
		String filePath = "";
		if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;
            try {
                cursor = getContentResolver().query(uri, projection, null,
                        null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(column_index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            filePath = uri.getPath();
        }
        File file = new File(filePath);
        if (file == null || !file.exists()) {
            ToastOrder.makeText(getApplicationContext(), R.string.work_summary_c7, ToastOrder.LENGTH_SHORT).show();
        }
        return filePath;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (Activity.RESULT_OK == resultCode) {
			isFisrt = false;
			if (requestCode == 100) {//从相册返回
				resultForPhotoAlbum(data);
			}else if(requestCode == 101){//拍照返回
				imageSrcList.add(Constants.WECHAT_PATH_LOAD+photoName);
			}
			refreshView();
		}else if(resultCode == 300){//预览图片返回
			if (data!=null) {
				boolean isDel = data.getBooleanExtra("isDelete", false);
				if (isDel) {
					String path = data.getStringExtra("path");
					imageSrcList.remove(path);
				}
			}
		}else{
			refreshView();
		}
	}
	
	private void refreshView(){
		imageSrcList.remove(add);
		imageSrcList.add(add);
		PhotoSelectAdapter adapter = new PhotoSelectAdapter();
		gv_chat_view.setAdapter(adapter);
	}
	
	class PhotoSelectAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return imageSrcList.size();
		}

		@Override
		public Object getItem(int position) {
			return imageSrcList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			 final ImageView imageView; 
			 String url = imageSrcList.get(position);
	         if (convertView == null) { 
	             imageView = new ImageView(WechatPhotoSelectActivity.this); 
	             int w = Constants.SCREEN_WIDTH/5;
	             imageView.setLayoutParams(new GridView.LayoutParams(w, w));//设置ImageView对象布局 
	             imageView.setAdjustViewBounds(false);//设置边界对齐 
	             imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);//设置刻度的类型 
	             imageView.setPadding(8, 8, 8, 8);//设置间距 
	         }else { 
	             imageView = (ImageView) convertView; 
	         } 
	         
	         if (position == imageSrcList.size()-1) {
	        	 	imageView.setBackgroundResource(R.drawable.add_photo);
			}else{
  				imageLoader.displayImage("file://"+url, imageView, options, animateFirstListener);
			}
	         imageView.setTag(url);
	         imageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String tag = (String) v.getTag();
					if (add.equals(tag)) {
						int size = imageSrcList.size();
						if (size<10) {
							showPup();
						}else{
							ToastOrder.makeText(WechatPhotoSelectActivity.this, R.string.no_morethan_nine_photo, ToastOrder.LENGTH_SHORT).show();
						}
					}else{
						Intent intent = new Intent(WechatPhotoSelectActivity.this, WechatPhotoSelectPriviewActivity.class);
						intent.putExtra("path", tag);
						WechatPhotoSelectActivity.this.startActivityForResult(intent, 300);
					}
				}
			});
	         return imageView; 
		}
		
		
	}
	
	
	/**
	 *选择照片方式
	 */
	private void showPup() {
		View contentView = View.inflate(WechatPhotoSelectActivity.this, R.layout.wechat_touxiangxuanze, null);
		Button quertBtn = (Button)contentView.findViewById(R.id.btn_nearby_visit_store_info_check);
		Button updateBtn = (Button)contentView.findViewById(R.id.btn_nearby_visit_store_info_update);
		Button cancelBtn = (Button)contentView.findViewById(R.id.btn_nearby_visit_store_info_cancel);
		
		final PopupWindow popupWindow = new PopupWindow(contentView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, true);
		popupWindow.setAnimationStyle(R.style.popupwindow);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.update();//更新弹出窗口的状态
		popupWindow.setBackgroundDrawable(new BitmapDrawable());//设置一个默认的背景
		popupWindow.showAtLocation(findViewById(R.id.ll_b), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); 
		quertBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				PS();
				popupWindow.dismiss();
			}
		});
		updateBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				XC();
				popupWindow.dismiss();
			}
		});
		cancelBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});
	
	}
	
	
	
	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500); //设置动画淡入
					displayedImages.add(imageUri);
				}
			}
		}
	}
	
}
