package com.yunhu.yhshxc.module.bbs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.BbsCommentItem;
import com.yunhu.yhshxc.bo.BbsInformationDetail;
import com.yunhu.yhshxc.bo.BbsInformationItem;
import com.yunhu.yhshxc.bo.BbsUserInfo;
import com.yunhu.yhshxc.bo.PhotoInfo;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.FileHelper;
import com.yunhu.yhshxc.utility.HttpHelper;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.apache.http.HttpEntity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import gcg.org.debug.JLog;

public class BBSMessageAdapter extends BaseAdapter{
	
	private String TAG="BBSMessageAdapter";
	private Context context;
	private List<BbsInformationItem> informationList;
//	private ExecutorService exec;
	private HttpHelper httpHelper;
//	private HashMap<Integer, ImageView> cacheImage;
//	private ProgressBar downPB;
	private AsyncImageLoader asyncImageLoader;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	private DisplayImageOptions options;
	private ListView listView;
	
	public BBSMessageAdapter(Context context) {
		this.context=context;
		httpHelper=new HttpHelper(context);
//		exec = Executors.newFixedThreadPool(3);
//		cacheImage = new HashMap<Integer, ImageView>();
		asyncImageLoader = new AsyncImageLoader();
		
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.empty_photo)
		.showImageForEmptyUri(R.drawable.empty_photo)
		.showImageOnFail(R.drawable.empty_photo)
		.cacheInMemory()
		.cacheOnDisc()
		.build();
		
	}
	@Override
	public int getCount() {
		if(informationList == null||informationList.isEmpty()){
			return 0;
		}else{
			return informationList.size();
		}
	}

	
	@Override
	public Object getItem(int position) {
		return 0;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		JLog.d(TAG, position+"/"+convertView);
		final BbsInformationItem informationItem=informationList.get(position);
		View view=null;
		try {
			BbsUserInfo userInfo=informationItem.getUserInfo();
			List<BbsCommentItem> commentList=informationItem.getBbsCommentItemList();
			final BbsInformationDetail informationDetail=informationItem.getBbsInfoDetail();
			BBSMessageItem messageItem = null;
			if(convertView == null){
				messageItem = new BBSMessageItem(context);
				view=messageItem.getView();
				view.setTag(messageItem);
			}else{
				view=convertView;
				messageItem = (BBSMessageItem)convertView.getTag();
			}

			messageItem.setMainUserName(PublicUtils.clearNumber(userInfo.getName()));//主贴姓名
			messageItem.setMainUserTime(PublicUtils.compareDate(informationDetail.getCreateTime()));//主贴日期
			
			// 主贴用户等级 && 积分
			if(!TextUtils.isEmpty(userInfo.getScore())){
				if(PublicUtils.getBBSLeve(userInfo.getScore()) == -1){
					messageItem.getMainUserLeve().setVisibility(View.GONE);
				}else{
					messageItem.getMainUserLeve().setVisibility(View.VISIBLE);
					messageItem.getMainUserLeve().setImageResource(PublicUtils.getBBSLeveIcon(userInfo.getScore()));
				}
				messageItem.getMainUserScore().setText(userInfo.getScore());
			}else{
				messageItem.getMainUserLeve().setVisibility(View.GONE);
				messageItem.getMainUserScore().setText("0");
			}
			
//			messageItem.setMainUserContent(informationDetail.getContent());//主贴内容
			//主贴内容
			if(TextUtils.isEmpty(informationDetail.getContent())){
				messageItem.getMainUserContent().setVisibility(View.GONE);
				messageItem.setMainUserContent("");//主贴内容
			}else{
				messageItem.getMainUserContent().setVisibility(View.VISIBLE);
				messageItem.setMainUserContent(informationDetail.getContent().length()>200?informationDetail.getContent().substring(0, 190)+"......":informationDetail.getContent());//主贴内容
			}
			//主贴定位地址
			if(TextUtils.isEmpty(informationDetail.getAddress())){//如果没有定位地址就不显示定位图标
				messageItem.getLocationIv().setVisibility(View.GONE);
				messageItem.setUserLocation("");
			}else{
				messageItem.getLocationIv().setVisibility(View.VISIBLE);
				messageItem.setUserLocation(informationDetail.getAddress());//主贴发帖地址
			}
			messageItem.setReviewNumber(informationDetail.getCommentNum());//主贴评论数
			messageItem.setUserDate(informationDetail.getCreateTime());//主贴日期
			
			LinearLayout contentIvLayout=messageItem.getContentIvLayout();
			//主贴内容图片
			PhotoInfo photoInfo=informationDetail.getPhotoInfo();
			if(!TextUtils.isEmpty(photoInfo.getPhotoUrl())){
				contentIvLayout.setVisibility(View.VISIBLE);
				String imageUrl=photoInfo.getPhotoUrl();
				ImageView imageView = messageItem.getMainContentIv();
		        imageView.setTag(imageUrl);
		        imageLoader.displayImage(imageUrl, imageView, options, animateFirstListener);;
			}else{
				contentIvLayout.setVisibility(View.GONE);
			}
			
			//主贴头像
			PhotoInfo userPhoto=userInfo.getPhotoInfo();
			if(userPhoto != null&&!TextUtils.isEmpty(userPhoto.getPhotoUrl())){
				Drawable userDrawable=Drawable.createFromPath(Constants.USERIV_PATH+userPhoto.getPhotoName());//添加内容图片
				if(userDrawable!=null){
					messageItem.setMainUserIv(userDrawable);
				}else{
					String imageUrl=userPhoto.getPhotoUrl();
					ImageView imageView = messageItem.getMainContentIv();
			        imageView.setTag(imageUrl);
			        imageLoader.displayImage(imageUrl, imageView, options, animateFirstListener);;
				}
			}
			
			contentIvLayout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					downPhoto(informationDetail);
				}
			});
			
			//添加评论
			if(commentList!=null){
				int commentSize=commentList.size();
				if(commentSize==0){
					messageItem.setFirstReviewLayoutShow(false);
					messageItem.setSecondReviewLayoutShow(false);
				}
				
				if(commentSize==1){
					messageItem.setFirstReviewLayoutShow(true);
					messageItem.setSecondReviewLayoutShow(false);
					BbsCommentItem commentItem=commentList.get(0);
					messageItem.setReviewUserNameFirst(PublicUtils.clearNumber(commentItem.getCreateuser()));//评论名字
					messageItem.setReviewUserContentFirst(commentItem.getContent());//评论内容
					messageItem.setReviewUserTimeFirst(PublicUtils.compareDate(commentItem.getCreateTime()));//评论日期
					
					// 用户等级
					if(!TextUtils.isEmpty(commentItem.getScore())){
						if(PublicUtils.getBBSLeve(commentItem.getScore()) == -1){
							messageItem.getReviewLeveFirst().setVisibility(View.GONE);
						}else{
							messageItem.getReviewLeveFirst().setVisibility(View.VISIBLE);
							messageItem.getReviewLeveFirst().setImageResource(PublicUtils.getBBSLeveIconSmall(commentItem.getScore()));
						}
					}else{
						messageItem.getReviewLeveFirst().setVisibility(View.GONE);
					}
				}
				
				if(commentSize==2){
					messageItem.setFirstReviewLayoutShow(true);
					messageItem.setSecondReviewLayoutShow(true);
					
					BbsCommentItem commentItem=commentList.get(0);
					messageItem.setReviewUserNameFirst(PublicUtils.clearNumber(commentItem.getCreateuser()));
					messageItem.setReviewUserContentFirst(commentItem.getContent());
					messageItem.setReviewUserTimeFirst(PublicUtils.compareDate(commentItem.getCreateTime()));
					// 用户等级
					if(!TextUtils.isEmpty(commentItem.getScore())){
						if(PublicUtils.getBBSLeve(commentItem.getScore()) == -1){
							messageItem.getReviewLeveFirst().setVisibility(View.GONE);
						}else{
							messageItem.getReviewLeveFirst().setVisibility(View.VISIBLE);
							messageItem.getReviewLeveFirst().setImageResource(PublicUtils.getBBSLeveIconSmall(commentItem.getScore()));
						}
					}else{
						messageItem.getReviewLeveFirst().setVisibility(View.GONE); 
					}
					
					BbsCommentItem commentItem2=commentList.get(1);
					messageItem.setReviewUserNameSecond(PublicUtils.clearNumber(commentItem2.getCreateuser()));
					messageItem.setReviewUserContentSecond(commentItem2.getContent());
					messageItem.setReviewUserTimeSecond(PublicUtils.compareDate(commentItem2.getCreateTime()));
					
					// 用户等级
					if(!TextUtils.isEmpty(commentItem2.getScore())){
						if(PublicUtils.getBBSLeve(commentItem2.getScore()) == -1){
							messageItem.getReviewLeveSecond().setVisibility(View.GONE);
						}else{
							messageItem.getReviewLeveSecond().setVisibility(View.VISIBLE);
							messageItem.getReviewLeveSecond().setImageResource(PublicUtils.getBBSLeveIconSmall(commentItem2.getScore()));
						}
					}else{
						messageItem.getReviewLeveSecond().setVisibility(View.GONE); 
					}
				}
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent=new Intent(context,BBSContentDetailActivity.class);
				intent.putExtra("bbsinformationitem", informationItem);
				context.startActivity(intent);
				//((Activity)context).overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
			}
		});
		
		return view;
	}
	
	private void downPhoto(BbsInformationDetail informationDetail){
		PhotoInfo photoInfo=informationDetail.getPhotoInfo();
		if(photoInfo!=null){
			String[] str=photoInfo.getPhotoUrl().split("/");
			Drawable contentDrawable=Drawable.createFromPath(Constants.CONTENTIV_PATH+str[str.length-1]);//添加内容图片
			JLog.d(TAG, "photoPath==>"+Constants.CONTENTIV_PATH+str[str.length-1]);
//  		Drawable contentDrawable=Drawable.createFromPath("/sdcard/aaa.jpg");//添加内容图片
			if(contentDrawable!=null){
				Dialog showPhoto=showPhotoDialog(Constants.CONTENTIV_PATH+str[str.length-1]);
				showPhoto.show();
				
			}else{
				new DownPhoto().execute(Constants.CONTENTIV_PATH+str[str.length-1],photoInfo.getPhotoUrl());
			}
		}
	}
	
	public List<BbsInformationItem> getInformationList() {
		return informationList;
	}
	public void setInformationList(List<BbsInformationItem> informationList) {
		this.informationList = informationList;
	}
	
    
    class DownPhoto extends AsyncTask<String, Integer, String>{
    	String path=null;
    	@Override
    	protected void onPreExecute() {
    		super.onPreExecute();
    	}
		@Override
		protected String doInBackground(String... params) {
			String locationPath=params[0];
			path=locationPath;
			String url=params[1];
			File file = new File(locationPath);
			if (!file.exists() || !file.isFile()) {
				HttpEntity entity = httpHelper
						.connectGetReturnEntity(url);
				try {

					file.createNewFile();
					InputStream is = entity.getContent();
					if (is != null) {
						FileOutputStream fos = new FileOutputStream(file);
						byte[] bt = new byte[1024];
						int i = 0;
						while ((i = is.read(bt)) != -1) {
							fos.write(bt, 0, i);
						}
						fos.flush();
						fos.close();
						is.close();
					}

				} catch (Exception e) {
					new FileHelper().deleteFile(locationPath);
					isContinue("1");
					return "1";//表示有异常下载失败
				}
			}
			isContinue("100");
			return "100";//下载完成
		}
    	
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if(result.equals("1")){//下载失败
				ToastOrder.makeText(context, context.getResources().getString(R.string.bbs_info_11), ToastOrder.LENGTH_LONG).show();
			}else{//下载成功！
				Dialog showPhoto=showPhotoDialog(path);
				showPhoto.show();
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
				if(result.equals("1")){//下载失败
					ToastOrder.makeText(context, context.getResources().getString(R.string.bbs_info_11), ToastOrder.LENGTH_LONG).show();
				}else{//下载成功！
					Dialog showPhoto=showPhotoDialog(path);
					showPhoto.show();
				}
			};
		};
    }
    
    private Dialog showPhotoDialog(String path) {
    	JLog.d(TAG, "photoPath==>"+path);
    	Dialog photoDialog = new Dialog(context, R.style.transparentDialog1);
    	View view=View.inflate(context, R.layout.bbs_show_photo, null);
    	LinearLayout iv=(LinearLayout)view.findViewById(R.id.bbs_showPhoto);
    	Drawable contentDrawable=Drawable.createFromPath(path);//添加内容图片
    	iv.setBackgroundDrawable(contentDrawable);
    	photoDialog.setContentView(view);
    	return photoDialog;
    }
    
    public void setListView(ListView listView){
    	this.listView=listView;
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
