package com.yunhu.yhshxc.wechat.exchange;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

	

public class WechatPhotoSelectPriviewActivity extends AbsBaseActivity {
	
	private ImageView iv_review;
	private LinearLayout ll_chat_del;
	private boolean isDelete = false;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	private DisplayImageOptions options;
	private String path;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_photo_review);
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ic_empty)
		.showImageForEmptyUri(R.drawable.ic_empty)
		.showImageOnFail(R.drawable.ic_empty)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.build();
		iv_review = (ImageView)findViewById(R.id.iv_review);
		ll_chat_del = (LinearLayout)findViewById(R.id.ll_chat_del);
		ll_chat_del.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				delete();
			}
		});
		
		path = getIntent().getStringExtra("path");
		imageLoader.displayImage("file://"+path, iv_review, options, animateFirstListener);

	}
	
	
	private void delete(){
		isDelete  =true;
		Intent intent = new Intent();
		intent.putExtra("isDelete", isDelete);
		intent.putExtra("path", path);
		setResult(300, intent);
		this.finish();
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
