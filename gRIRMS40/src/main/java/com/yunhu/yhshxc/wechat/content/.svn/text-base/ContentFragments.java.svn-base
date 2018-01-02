package com.yunhu.yhshxc.wechat.content;

import gcg.org.debug.JLog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.FileHelper;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.wechat.Util.SharedPrefrencesForWechatUtil;
import com.yunhu.yhshxc.wechat.exchange.ExchangeActivity;
import com.yunhu.yhshxc.widget.ToastOrder;

@SuppressLint("NewApi")
public class ContentFragments extends DialogFragment implements ContentControlListener,OnClickListener{

	private String TAG = "ContentFragments";
	public final static int CONTENTFRAGMENTS_FLAG=1100;
	private ImageView iv_wechat_add_photo;
	private ImageView iv_wechat_add_photos;
	private ImageView iv_content_add_voice;
	private ImageView iv_content_add_file;
	private ImageView iv_content_add_video;
	private LinearLayout ll_wechat_photo;
	private LinearLayout ll_content_add_voice;
	private LinearLayout ll_content_add_file;
	private LinearLayout ll_content_add_video;
	
	private LinearLayout ll_cancel,ll_confirm;
	private ExchangeActivity exchangeActivity;
	public LinearLayout ll_b;
	
	public List<ContentPhotoView> photoViewList = null;
	public List<VoiceView> voiceViewList = null;
	public List<FileView> fileViewList = null;
	public List<VideoView> videoViewList = null;

	private List<String> photoPathList = null;
	private List<String> voicePathList = null;//录音路径集合
	private List<String> filePathList = null;//附件路径集合
	private List<String> videoPathList = null;//视频路径集合
	
	public static final int REQUEST_CODE_SELECT_VIDEO = 23;

	private String wechatMsg;//发送的消息
	private EditText et_wechat_msg;
	private ContentFragmentSelectCheck selectCheckListener;
	public ContentFragments() {
//		setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
		this.setCancelable(false);// 设置点击屏幕Dialog不消失  
		photoViewList = new ArrayList<ContentPhotoView>();
		voiceViewList = new ArrayList<VoiceView>();
		fileViewList = new ArrayList<FileView>();
		videoViewList = new ArrayList<VideoView>();
		photoPathList = new ArrayList<String>();//照片路径集合
		voicePathList = new ArrayList<String>();//录音路径集合
		filePathList = new ArrayList<String>();//附件路径集合
		videoPathList = new ArrayList<String>();//视频路径集合
	}
	//接口用于获取审核人信息
	public interface ContentFragmentSelectCheck{
		void toGroupUserActivity();
		void setEditTextContent(EditText et);
	}
	//设置接口的实现子类
	public void setContentFragmentSelectCheck(ContentFragmentSelectCheck selectCheckListener){
		this.selectCheckListener=selectCheckListener;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_wechat_submit_content, null);
		ll_b = (LinearLayout)v.findViewById(R.id.ll_b);
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		init(v);
		initDate();
		return v;
	}
		

	private void init(View v) {
		iv_wechat_add_photo = (ImageView) v.findViewById(R.id.iv_wechat_add_photo);
		iv_wechat_add_photos = (ImageView)v.findViewById(R.id.iv_wechat_add_photos);
		iv_content_add_voice = (ImageView) v.findViewById(R.id.iv_content_add_voice2);
		iv_content_add_file = (ImageView) v.findViewById(R.id.iv_content_add_file);
		iv_content_add_video = (ImageView) v.findViewById(R.id.iv_content_add_video);
		ll_wechat_photo = (LinearLayout) v.findViewById(R.id.ll_wechat_photo);
		ll_content_add_voice = (LinearLayout) v.findViewById(R.id.ll_content_add_voice);
		ll_content_add_file = (LinearLayout) v.findViewById(R.id.ll_content_add_file);
		ll_content_add_video = (LinearLayout) v.findViewById(R.id.ll_content_add_video);
		et_wechat_msg = (EditText)v.findViewById(R.id.et_wechat_msg);
		et_wechat_msg.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (count != 0) {// 输入框有内容
					
					String result = s.toString();
					char[] array = result.toCharArray();
					String re = array[0] + "";
					if (re.equals("@") && array.length < 2 ) {
						if (selectCheckListener!=null) {
							
							selectCheckListener.toGroupUserActivity();
							selectCheckListener.setEditTextContent(et_wechat_msg);
						}else{
							throw new NullPointerException();
						}
					}
				}
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		ll_cancel = (LinearLayout)v.findViewById(R.id.ll_cancel);
		ll_confirm = (LinearLayout)v.findViewById(R.id.ll_confirm);
		ll_confirm.setOnClickListener(this);
		ll_cancel.setOnClickListener(this);
		iv_wechat_add_photo.setOnClickListener(this);
		iv_wechat_add_photos.setOnClickListener(this);
		iv_content_add_voice.setOnClickListener(this);
		iv_content_add_file.setOnClickListener(this);
		iv_content_add_video.setOnClickListener(this);
		
	}
	
	private void initDate(){
		ll_wechat_photo.removeAllViews();
		if (!photoViewList.isEmpty()) {
			for (int i = 0; i < photoViewList.size(); i++) {
				ContentPhotoView photoView = photoViewList.get(i);
				photoView.setContentControlListener(this);
				ViewGroup v = (ViewGroup) photoView.getView().getParent();
				if (v!=null) {
					v.removeAllViewsInLayout();
				}
				ll_wechat_photo.addView(photoView.getView());
			}
		}
		
		ll_content_add_voice.removeAllViews();
		if (!voiceViewList.isEmpty()) {
			for (int i = 0; i < voiceViewList.size(); i++) {
				VoiceView voiceView = voiceViewList.get(i);
				voiceView.setContentControlListener(this);
				ViewGroup v = (ViewGroup) voiceView.getView().getParent();
				if (v!=null) {
					v.removeAllViewsInLayout();
				}
				ll_content_add_voice.addView(voiceView.getView());
			}
		}
		
		ll_content_add_file.removeAllViews();
		if (!fileViewList.isEmpty()) {
			ll_content_add_file.removeAllViews();
			for (int i = 0; i < fileViewList.size(); i++) {
				FileView fileView = fileViewList.get(i);
				fileView.setContentControlListener(this);
				ViewGroup v = (ViewGroup) fileView.getView().getParent();
				if (v!=null) {
					v.removeAllViewsInLayout();
				}
				ll_content_add_file.addView(fileView.getView());
			}
		}
		
		ll_content_add_video.removeAllViews();
		if (!videoViewList.isEmpty()) {
			for (int i = 0; i < videoViewList.size(); i++) {
				VideoView videoView = videoViewList.get(i);
				videoView.setContentControlListener(this);
				ViewGroup v = (ViewGroup) videoView.getView().getParent();
				if (v!=null) {
					v.removeAllViewsInLayout();
				}
				ll_content_add_video.addView(videoView.getView());
			}
		}
		
//		wechatMsg = exchangeActivity.ed_exchange_comment_2.getText().toString();
		if (!TextUtils.isEmpty(wechatMsg)) {
			et_wechat_msg.setText(wechatMsg);
		}else{
			et_wechat_msg.setText("");
//			et_wechat_msg.setText("测试数据");

		}
	}

	public void setExchangeActivity(ExchangeActivity exchangeActivity) {
		this.exchangeActivity = exchangeActivity;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.iv_wechat_add_photo://添加照片按钮
			if (photoPathList.size()>=9) {
				ToastOrder.makeText(exchangeActivity, R.string.no_morethan_nine_photo, ToastOrder.LENGTH_SHORT).show();
			}else{
				PS();
//				showPup();
			}
			break;
		case R.id.iv_content_add_voice2:
			String msg = et_wechat_msg.getText().toString();
			if (!TextUtils.isEmpty(msg)) {
				exchangeActivity.ed_exchange_comment.setText(msg);
			}else{
				exchangeActivity.ed_exchange_comment.setText("");
			}
			exchangeActivity.voice();
			break;
		case R.id.iv_content_add_file:
			selectFileFromLocal();
			break;
         case R.id.iv_content_add_video:
			Intent intent_video = new Intent(getActivity(), ImageGridActivity.class);
            startActivityForResult(intent_video, REQUEST_CODE_SELECT_VIDEO);
//        	 video();
			break;
		case R.id.ll_cancel:
			//提交事件
//			if (exchangeActivity!=null) {
//				exchangeActivity.dismisContentFragment();
//			}
			
			if (exchangeActivity!=null) {
				exchangeActivity.confrimMessage(photoPathList, voicePathList, filePathList, videoPathList,et_wechat_msg.getText().toString());
//				exchangeActivity.test(photoViewList, voiceViewList, fileViewList, videoViewList, et_wechat_msg.getText().toString());
				exchangeActivity.dismisContentFragment();
			}
			
			break;
		case R.id.ll_confirm:
			//提交事件
			if (exchangeActivity!=null) {
				exchangeActivity.confrimMessage(photoPathList, voicePathList, filePathList, videoPathList,et_wechat_msg.getText().toString());
//				exchangeActivity.test(photoViewList, voiceViewList, fileViewList, videoViewList, et_wechat_msg.getText().toString());
				exchangeActivity.dismisContentFragment();
				exchangeActivity.submitReply();
			}
			
			
			break;
		case R.id.iv_wechat_add_photos:
			if (photoPathList.size()>=9) {
				ToastOrder.makeText(exchangeActivity, R.string.no_morethan_nine_photo, ToastOrder.LENGTH_SHORT).show();
			}else{
				XC();
//				showPup();
			}
			
			break;
		default:
			break;
		}
	}
	
	private Dialog voiceDialog;
	public void voiceForResult(final String path){
		if (voiceDialog == null) {
			voiceDialog = new MyProgressDialog(exchangeActivity,R.style.CustomProgressDialog, PublicUtils.getResourceString(exchangeActivity,R.string.wechat_content95));
		}
		if (voiceDialog!=null && !voiceDialog.isShowing()) {
			voiceDialog.show();
		}
		new Thread(){
			public void run() {
	        	   try {
		        		if (!TextUtils.isEmpty(path)) {
						int index = path.lastIndexOf("/");
						String name = path.substring(index + 1);
						FileHelper helper = new FileHelper();
//						String[] fileStr = name.split("\\.");
//						String newName = String.valueOf(System.currentTimeMillis())+"."+fileStr[1];
						SharedPrefrencesForWechatUtil.getInstance(getActivity()).setFileName(name,name);
						
						helper.copyFile(path, Constants.WECHAT_PATH_LOAD+name);
//						helper.copyFile(path, Constants.WECHAT_PATH+name);
						 
						 Message msg = voiceHander.obtainMessage();
						 msg.what =1;
						 msg.obj = name;
						 voiceHander.sendMessage(msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
					voiceHander.sendEmptyMessage(2);
				}
			};
		}.start();
	}
	
	private Handler voiceHander = new Handler(){
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
			if (voiceDialog !=null && voiceDialog.isShowing()) {
				voiceDialog.dismiss();
			} 
			switch (what) {
			case 1:
				String name = (String) msg.obj;
				VoiceView voiceView = new VoiceView(exchangeActivity);
				Voice voice = new Voice();
				voice.setName(name);
				voiceView.setVoiceView(voice);
				voiceView.setContentControlListener(ContentFragments.this);
				ll_content_add_voice.addView(voiceView.getView());
				voiceView.setFilePath(Constants.WECHAT_PATH_LOAD+name);
				voiceViewList.add(voiceView);
				voicePathList.add(Constants.WECHAT_PATH_LOAD+name);
				
				break;
			case 2:
				ToastOrder.makeText(getActivity(), R.string.audio_load_failure, ToastOrder.LENGTH_SHORT).show();
				break;
			case 3:
	        	  ToastOrder.makeText(getActivity(), R.string.un_support_type, ToastOrder.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		};
	};
	
	
	

	/*
	 * 从相册中选择
	 */
	private void XC(){
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
		Intent intent_camera = exchangeActivity.getPackageManager().getLaunchIntentForPackage("com.android.camera");
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
     * 选择文件
     */
    public static final int REQUEST_CODE_SELECT_FILE = 24;
    private void selectFileFromLocal() {
        Intent intent = null;
        if (Build.VERSION.SDK_INT < 20) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        } else {
            intent = new Intent( Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
    }
	
	@Override
	public void contentControl(ContentPhotoView photoView) {
		ll_wechat_photo.removeView(photoView.getView());
		JLog.d("jishen", ll_wechat_photo.toString()+"------------------:"+ll_wechat_photo.getChildCount());
		photoViewList.remove(photoView);
		String filePath = photoView.getFilePath();
		photoPathList.remove(filePath);
	}

	@Override
	public void contentVoiceControl(VoiceView voiceView) {
		ll_content_add_voice.removeView(voiceView.getView());
		voiceViewList.remove(voiceView);
		String filePath = voiceView.getFilePath();
		voicePathList.remove(filePath);
	}

	@Override
	public void contentFileControl(FileView fileView) {
		ll_content_add_file.removeView(fileView.getView());
		fileViewList.remove(fileView);
		String filePath = fileView.getFilePaht();
		filePathList.remove(filePath);
	}

	@Override
	public void contentVideoControl(VideoView videoView) {
		ll_content_add_video.removeView(videoView.getView());
		videoViewList.remove(videoView);
		String filePath = videoView.getFilePath();
		videoPathList.remove(filePath);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 100) {
			if (data!=null) {
				resultForPhotoAlbum(data);
			}
		}else if(requestCode == REQUEST_CODE_SELECT_FILE){
			 if (data != null) {
                 Uri uri = data.getData();
                 if (uri != null) {
                	 	resultForFile(uri);
                 }
             }
		}else if(requestCode == 101){//拍照返回
			photoResut(Constants.WECHAT_PATH_LOAD+photoName,true);
		}else if(requestCode == 110){//录制视频返回
			if(data!=null){
			    Uri uri = data.getData();
			    if (uri != null) {
            	 		resultForFile(uri);
			    }
			 }
			
		}else if(requestCode == REQUEST_CODE_SELECT_VIDEO){
			
			if (data != null) {
				String videoPath = data.getStringExtra("path");
				if(!TextUtils.isEmpty(videoPath)){
//					addVideoView(videoPath);
					videoForResult(videoPath);
				}
			}
		}
	}
	
	
	private Dialog videoDialog;
	private void videoForResult(final String path){
		if (videoDialog == null) {
			videoDialog = new MyProgressDialog(getActivity(),R.style.CustomProgressDialog,PublicUtils.getResourceString(getActivity(),R.string.wechat_content96));
		}
		if (videoDialog!=null && !videoDialog.isShowing()) {
			videoDialog.show();
		}
		new Thread(){
			public void run() {
	        	   try {
		        		if (!TextUtils.isEmpty(path)) {
						int index = path.lastIndexOf("/");
						String name = path.substring(index + 1);
						FileHelper helper = new FileHelper();
//						String[] fileStr = name.split("\\.");
//						String newName = String.valueOf(System.currentTimeMillis())+"."+fileStr[1];
						SharedPrefrencesForWechatUtil.getInstance(getActivity()).setFileName(name,name);
						
//						helper.copyFile(path, Constants.WECHAT_PATH_LOAD+newName);
						helper.copyFile(path, Constants.WECHAT_PATH+name);
						 
						 Message msg = videoHander.obtainMessage();
						 msg.what =1;
						 msg.obj = name;
						 videoHander.sendMessage(msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
					videoHander.sendEmptyMessage(2);
				}
			};
		}.start();
	
	}
	/**
	 * 加载是视频view
	 */
	
	private void addVideoView(String videoPath){
		int index = videoPath.lastIndexOf("/");
		String name = videoPath.substring(index + 1);
		VideoView videoView = new VideoView(getActivity());
		Video video = new Video();
		video.setName(name);
		videoView.setVideoView(video);
		videoView.setFilePath(videoPath);
		videoView.setContentControlListener(ContentFragments.this);
		ll_content_add_video.addView(videoView.getView());
		videoViewList.add(videoView);
		videoPathList.add(videoPath);
	}
	
	/**
	 *选择照片方式
	 */
	private void showPup() {
		View contentView = View.inflate(exchangeActivity, R.layout.wechat_touxiangxuanze, null);
		Button quertBtn = (Button)contentView.findViewById(R.id.btn_nearby_visit_store_info_check);
		Button updateBtn = (Button)contentView.findViewById(R.id.btn_nearby_visit_store_info_update);
		Button cancelBtn = (Button)contentView.findViewById(R.id.btn_nearby_visit_store_info_cancel);
		
		final PopupWindow popupWindow = new PopupWindow(contentView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, true);
		popupWindow.setAnimationStyle(R.style.popupwindow);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.update();//更新弹出窗口的状态
		popupWindow.setBackgroundDrawable(new BitmapDrawable());//设置一个默认的背景
		popupWindow.showAtLocation(ll_b, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); 
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
	
	
	
	
	/**
	 * 通过相册选择图片
	 */
	private Dialog loadPhotoPic = null;
	private File file;
	private void photoResut(String str ,final boolean isPZ){
		if (loadPhotoPic == null) {
			loadPhotoPic = new MyProgressDialog(getActivity(),R.style.CustomProgressDialog,PublicUtils.getResourceString(getActivity(),R.string.wechat_content97));
		}
		if (!TextUtils.isEmpty(str)) {
			if (loadPhotoPic!=null && !loadPhotoPic.isShowing()) {
				loadPhotoPic.show();
			}
			final FileHelper helper = new FileHelper();
			final StringBuffer sb = new StringBuffer();
			final String[] image = str.split(",");
			new Thread(){
				public void run() {
					if (image!=null) {
						for (int i = 0; i < image.length; i++) {
							
							String path = image[i];
							JLog.d("jishen", "\n"+path);
							file = new File(path);
							JLog.d("jishen", "\n"+file.getAbsolutePath());

							if (file.exists()) {
								String newName = System.currentTimeMillis()+".jpg";
								 sb.append(",").append(newName);
								 byte[]  b = 	helper.readImageByte(path);
								 helper.saveWeReply(b, newName,Constants.WECHAT_PATH_LOAD);//查看用
								 helper.saveWeReply(b, newName,Constants.WECHAT_PATH);//提交用
								 Message msg = photoHander.obtainMessage();
								 msg.what = 2;
								 msg.obj = newName;
								 photoHander.sendMessage(msg);
							}
	
						}
						photoHander.sendEmptyMessage(1);
					}
				};
			}.start();
		}
	}
	
	/**
	 * 
	 * @param uri
	 */
	  public void resultForFile(Uri uri) {
	        String filePath = null;
//	        if ("content".equalsIgnoreCase(uri.getScheme())) {
//	        	
//	            String[] projection = { "_data" };
//	            Cursor cursor = null;
//
//	            try {
//	                cursor = getActivity().getContentResolver().query(uri, projection, null,
//	                        null, null);
//	                int column_index = cursor.getColumnIndexOrThrow("_data");
//	                if (cursor.moveToFirst()) {
//	                    filePath = cursor.getString(column_index);
//	                }
//	            } catch (Exception e) {
//	                e.printStackTrace();
//	            }
//	        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
//	            filePath = uri.getPath();
//	        }
	        filePath = getPath(getActivity(), uri);
	        File file = new File(filePath);
	        if (file == null || !file.exists()) {
	            ToastOrder.makeText(getActivity(), R.string.work_summary_c7, ToastOrder.LENGTH_SHORT).show();
	            return;
	        }
	        if (file.length() > 10 * 1024 * 1024) {
	            ToastOrder.makeText(getActivity(), R.string.wechat_content39, ToastOrder.LENGTH_SHORT).show();
	            return;
	        }
	        if(filePath.endsWith(".doc") || filePath.endsWith(".docx") || 
	        		filePath.endsWith(".xls") || filePath.endsWith(".xlsx")|| 
	        		filePath.endsWith(".ppt") || filePath.endsWith(".pptx")|| 
	        		filePath.endsWith(".pdf") || filePath.endsWith(".txt")|| 
	        		filePath.endsWith(".png") || filePath.endsWith(".PNG")|| 
	        		filePath.endsWith(".jpg") || filePath.endsWith(".JPG")|| 
	        		filePath.endsWith(".GIF") || filePath.endsWith(".gif")|| 
	        		filePath.endsWith(".JPEG") || filePath.endsWith(".jpeg")|| 
	        		filePath.endsWith(".BMP") || filePath.endsWith(".bmp")|| 
	        		filePath.endsWith(".3gp") || filePath.endsWith(".3GP") ||
	        		filePath.endsWith(".mp4") || filePath.endsWith(".MP4")){
	        		fileResult(file);
	        }else{
	        		fileHander.sendEmptyMessage(3);
		        return;
	        }
	    }
	  
	  /**
	   * 根据Uri获取路径
	   * @param context
	   * @param uri
	   * @return
	   */
	  
	  public static String getPath(final Context context, final Uri uri) {  
		  
		    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;  
		  
		    // DocumentProvider  
		    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {  
		        // ExternalStorageProvider  
		        if (isExternalStorageDocument(uri)) {  
		            final String docId = DocumentsContract.getDocumentId(uri);  
		            final String[] split = docId.split(":");  
		            final String type = split[0];  
		  
		            if ("primary".equalsIgnoreCase(type)) {  
		                return Environment.getExternalStorageDirectory() + "/" + split[1];  
		            }  
		  
		            // TODO handle non-primary volumes  
		        }  
		        // DownloadsProvider  
		        else if (isDownloadsDocument(uri)) {  
		  
		            final String id = DocumentsContract.getDocumentId(uri);  
		            final Uri contentUri = ContentUris.withAppendedId(  
		                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));  
		  
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
		            final String[] selectionArgs = new String[] {  
		                    split[1]  
		            };  
		  
		            return getDataColumn(context, contentUri, selection, selectionArgs);  
		        }  
		    }  
		    // MediaStore (and general)  
		    else if ("content".equalsIgnoreCase(uri.getScheme())) {  
		        return getDataColumn(context, uri, null, null);  
		    }  
		    // File  
		    else if ("file".equalsIgnoreCase(uri.getScheme())) {  
		        return uri.getPath();  
		    }  
		  
		    return null;  
		}  
		  
		/** 
		 * Get the value of the data column for this Uri. This is useful for 
		 * MediaStore Uris, and other file-based ContentProviders. 
		 * 
		 * @param context The context. 
		 * @param uri The Uri to query. 
		 * @param selection (Optional) Filter used in the query. 
		 * @param selectionArgs (Optional) Selection arguments used in the query. 
		 * @return The value of the _data column, which is typically a file path. 
		 */  
		public static String getDataColumn(Context context, Uri uri, String selection,  
		        String[] selectionArgs) {  
		  
		    Cursor cursor = null;  
		    final String column = "_data";  
		    final String[] projection = {  
		            column  
		    };  
		  
		    try {  
		        cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,  
		                null);  
		        if (cursor != null && cursor.moveToFirst()) {  
		            final int column_index = cursor.getColumnIndexOrThrow(column);  
		            return cursor.getString(column_index);  
		        }  
		    } finally {  
		        if (cursor != null)  
		            cursor.close();  
		    }  
		    return null;  
		}  
		  
		  
		/** 
		 * @param uri The Uri to check. 
		 * @return Whether the Uri authority is ExternalStorageProvider. 
		 */  
		public static boolean isExternalStorageDocument(Uri uri) {  
		    return "com.android.externalstorage.documents".equals(uri.getAuthority());  
		}  
		  
		/** 
		 * @param uri The Uri to check. 
		 * @return Whether the Uri authority is DownloadsProvider. 
		 */  
		public static boolean isDownloadsDocument(Uri uri) {  
		    return "com.android.providers.downloads.documents".equals(uri.getAuthority());  
		}  
		  
		/** 
		 * @param uri The Uri to check. 
		 * @return Whether the Uri authority is MediaProvider. 
		 */  
		public static boolean isMediaDocument(Uri uri) {  
		    return "com.android.providers.media.documents".equals(uri.getAuthority());  
		}  
	  

		/**
		 * 附件选择完毕
		 */
		
		private Dialog fileDialog;
		private void fileResult(final File srcFile){
			if (fileDialog == null) {
				fileDialog = new MyProgressDialog(getActivity(),R.style.CustomProgressDialog,PublicUtils.getResourceString(getActivity(),R.string.wechat_content79));
			}
			if (fileDialog!=null && !fileDialog.isShowing()) {
				fileDialog.show();
			}
			new Thread(){
				public void run() {
		        	   try {
		        		   String filePath = srcFile.getAbsolutePath();
			        		if (!TextUtils.isEmpty(filePath)) {
							int index = filePath.lastIndexOf("/");
							String name = filePath.substring(index + 1);
							FileHelper helper = new FileHelper();
							String[] fileStr = name.split("\\.");
							String newName = String.valueOf(System.currentTimeMillis())+"."+fileStr[fileStr.length-1];
							SharedPrefrencesForWechatUtil.getInstance(getActivity()).setFileName(newName,name);
							
							helper.copyFile(filePath, Constants.WECHAT_PATH_LOAD+newName);
							helper.copyFile(filePath, Constants.WECHAT_PATH+newName);
							 
							 Message msg = fileHander.obtainMessage();
							 msg.what =1;
							 msg.obj = newName;
							fileHander.sendMessage(msg);
						}
					} catch (Exception e) {
						e.printStackTrace();
						fileHander.sendEmptyMessage(2);
					}
				};
			}.start();
		}
		
		
		private Handler videoHander = new Handler(){
			public void handleMessage(android.os.Message msg) {
				int what = msg.what;
				if (videoDialog !=null && videoDialog.isShowing()) {
					videoDialog.dismiss();
				} 
				switch (what) {
				case 1:
					String name = (String) msg.obj;
					VideoView videoView = new VideoView(exchangeActivity);
					Video video = new Video();
					video.setName(name);
					videoView.setVideoView(video);
					videoView.setFilePath(Constants.WECHAT_PATH+name);
					videoView.setContentControlListener(ContentFragments.this);
					ll_content_add_video.addView(videoView.getView());
					videoViewList.add(videoView);
					videoPathList.add(Constants.WECHAT_PATH+name);
					break;
				case 2:
					ToastOrder.makeText(getActivity(), R.string.wechat_content94, ToastOrder.LENGTH_SHORT).show();
					break;
				case 3:
		        	  ToastOrder.makeText(getActivity(), R.string.un_support_type, ToastOrder.LENGTH_SHORT).show();
					break;

				default:
					break;
				}
			};
		};

		
		
		
		private Handler fileHander = new Handler(){
			public void handleMessage(android.os.Message msg) {
				int what = msg.what;
				if (fileDialog !=null && fileDialog.isShowing()) {
					fileDialog.dismiss();
				} 
				switch (what) {
				case 1:
					String name = (String) msg.obj;
					FileView fileView = new FileView(exchangeActivity);
					ContentFile file = new ContentFile();
					file.setName(name);
					file.setFilePath(name);
					fileView.setFileView(file);
					fileView.setContentControlListener(ContentFragments.this);
					fileView.setFilePaht(Constants.WECHAT_PATH+name);
					ll_content_add_file.addView(fileView.getView());
					fileViewList.add(fileView);
			        filePathList.add(Constants.WECHAT_PATH+name);
					break;
				case 2:
					ToastOrder.makeText(getActivity(), R.string.wechat_content70, ToastOrder.LENGTH_SHORT).show();
					break;
				case 3:
		        	  ToastOrder.makeText(getActivity(), R.string.un_support_type, ToastOrder.LENGTH_SHORT).show();
					break;

				default:
					break;
				}
			};
		};
		
		
	
	
	private Handler photoHander = new Handler(){
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
			switch (what) {
			case 1:
				if (loadPhotoPic!=null && loadPhotoPic.isShowing()){
					loadPhotoPic.dismiss();
				}
				break;
			case 2:
				String name = (String) msg.obj;
				ContentPhotoView photoView = new ContentPhotoView(exchangeActivity);
				Photo photo = new Photo();
				photo.setName(name);
				photo.setPhotoPath(name);
				photoView.setPhotoView(photo);
				photoView.setContentControlListener(ContentFragments.this);
				ll_wechat_photo.addView(photoView.getView());
				photoViewList.add(photoView);
				photoView.setFilePath(Constants.WECHAT_PATH+name);
				photoPathList.add(Constants.WECHAT_PATH+name);
				break;

			default:
				break;
			}
		};
	};
	

	
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
					String path = photoAlbumPath(originalUri);
					photoResut(path,false);
				} else {
					ToastOrder.makeText(exchangeActivity, R.string.work_summary_c6, ToastOrder.LENGTH_SHORT).show();
				}
			} else {
				ToastOrder.makeText(exchangeActivity, R.string.work_summary_c6, ToastOrder.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
			ToastOrder.makeText(exchangeActivity, R.string.work_summary_c6, ToastOrder.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * 从相册选择图片的路径
	 * @param uri
	 * @return
	 */
	private String photoAlbumPath(Uri uri){
		String filePath = "";
		if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;
            try {
                cursor = exchangeActivity.getContentResolver().query(uri, projection, null,
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
            ToastOrder.makeText(exchangeActivity.getApplicationContext(), R.string.work_summary_c7, ToastOrder.LENGTH_SHORT).show();
        }
        return filePath;
	}

	public List<VoiceView> getVoiceViewList() {
		return voiceViewList;
	}

	public void setVoiceViewList(List<VoiceView> voiceViewList) {
		this.voiceViewList = voiceViewList;
	}

	public List<VideoView> getVideoViewList() {
		return videoViewList;
	}

	public void setVideoViewList(List<VideoView> videoViewList) {
		this.videoViewList = videoViewList;
	}
	

	public List<ContentPhotoView> getPhotoViewList() {
		return photoViewList;
	}

	public void setPhotoViewList(List<ContentPhotoView> photoViewList) {
		this.photoViewList = photoViewList;
	}

	public List<FileView> getFileViewList() {
		return fileViewList;
	}

	public void setFileViewList(List<FileView> fileViewList) {
		this.fileViewList = fileViewList;
	}

	public void setWechatMsg(String wechatMsg) {
		this.wechatMsg = wechatMsg;
	}

	public List<String> getPhotoPathList() {
		return photoPathList;
	}

	public void setPhotoPathList(List<String> photoPathList) {
		this.photoPathList = photoPathList;
	}

	public List<String> getVoicePathList() {
		return voicePathList;
	}

	public void setVoicePathList(List<String> voicePathList) {
		this.voicePathList = voicePathList;
	}

	public List<String> getFilePathList() {
		return filePathList;
	}

	public void setFilePathList(List<String> filePathList) {
		this.filePathList = filePathList;
	}

	public List<String> getVideoPathList() {
		return videoPathList;
	}

	public void setVideoPathList(List<String> videoPathList) {
		this.videoPathList = videoPathList;
	}
	

	
	
}
