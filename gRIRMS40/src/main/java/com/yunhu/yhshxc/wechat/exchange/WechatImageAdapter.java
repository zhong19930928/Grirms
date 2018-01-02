package com.yunhu.yhshxc.wechat.exchange;

import gcg.org.debug.JLog;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.dragimage.PhotoPriviewActivity;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.wechat.bo.Reply;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class WechatImageAdapter  extends BaseAdapter{

	private Context context;
	private List<String> imageSrcList = new ArrayList<String>();
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private ImageLoadingListener animateFirstListener = null;
	private DisplayImageOptions options;
	private Reply reply;
	
	public WechatImageAdapter(Context context) {
		this.context = context;
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ic_empty)
		.showImageForEmptyUri(R.drawable.ic_empty)
		.showImageOnFail(R.drawable.ic_empty)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.build();
		animateFirstListener = new AnimateFirstDisplayListener(this);
	}
	
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
             imageView = new ImageView(context); 
             imageView.setLayoutParams(new GridView.LayoutParams(100, 100));//设置ImageView对象布局 
             imageView.setAdjustViewBounds(false);//设置边界对齐 
             imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);//设置刻度的类型 
             imageView.setPadding(8, 8, 8, 8);//设置间距 
         }  
         else { 
             imageView = (ImageView) convertView; 
         } 
//        imageView.setImageResource(imgs[position]);//为ImageView设置图片资源 
         if (reply!=null) {
        	 if (!TextUtils.isEmpty(url)) {
        		 	int index = url.lastIndexOf("/");
     			String fileName = url.substring(index + 1);
     			String[] str = fileName.split("@");
     			if (str.length>1) {
     				fileName = str[1];
				}
     			
     			File f = new File(Constants.WECHAT_PATH_LOAD+fileName);
				if (f.exists()) {
					imageLoader.displayImage("file://"+Constants.WECHAT_PATH_LOAD+fileName, imageView, options, animateFirstListener);
	 				imageView.setTag(Constants.WECHAT_PATH_LOAD+fileName);

				}else{
     				imageLoader.displayImage(url, imageView, options, animateFirstListener);
     				imageView.setTag(url);

				}
 				
     			
//     			int replyUserId = reply.getUserId();
//     			if (replyUserId == SharedPreferencesUtil.getInstance(context).getUserId()) {
//					File f = new File(Constants.WECHAT_PATH+fileName);
//					if (f.exists()) {
//						imageLoader.displayImage("file://"+Constants.WECHAT_PATH+fileName, imageView, options, animateFirstListener);
//					}else{
//	     				imageLoader.displayImage(url, imageView, options, animateFirstListener);
//					}
//	 				imageView.setTag(Constants.WECHAT_PATH+fileName);
//				}else{
//					if (url.startsWith("http")) {
//	     				imageLoader.displayImage(url, imageView, options, animateFirstListener);
//	     			}else{
//	     				imageLoader.displayImage("file://"+Constants.WECHAT_PATH+url, imageView, options, animateFirstListener);
//	     			}
//	 				imageView.setTag(url);
//				}
        		 
//        		 		if (url.startsWith("http")) {
//	     				imageLoader.displayImage(url, imageView, options, animateFirstListener);
//	     			}else{
//	     				imageLoader.displayImage("file://"+Constants.WECHAT_PATH+url, imageView, options, animateFirstListener);
//	     			}
//	 				imageView.setTag(url);
	 				
 				imageView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						String tag = (String) imageView.getTag();
						if (!TextUtils.isEmpty(tag)) {
							Intent intent = new Intent(context, PhotoPriviewActivity.class);
							if (tag.startsWith("http")) {
								intent.putExtra("url", tag);
							}else{
								intent.putExtra("url", tag);
								intent.putExtra("isLocal", true);
							}
							context.startActivity(intent);
						}
					}
				});
     		}
        	 
		}
         return imageView; 
	}

	public List<String> getImageSrcList() {
		return imageSrcList;
	}

	public void setImageSrcList(List<String> imageSrcList) {
		this.imageSrcList = imageSrcList;
	}
	
	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		
		private WechatImageAdapter adapter;
		public AnimateFirstDisplayListener(WechatImageAdapter adapter) {
			this.adapter = adapter;
		}
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
		@Override
		public void onLoadingFailed(String imageUri, View view,FailReason failReason) {
			super.onLoadingFailed(imageUri, view, failReason);
			JLog.d("jishen", "111111111111111111");
//			if (adapter!=null) {
//				adapter.notifyDataSetChanged();
//			}
			
		}
	}

	public Reply getReply() {
		return reply;
	}

	public void setReply(Reply reply) {
		this.reply = reply;
	}
	
	

}
