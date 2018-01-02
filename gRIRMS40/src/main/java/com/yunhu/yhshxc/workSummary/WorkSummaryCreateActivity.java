package com.yunhu.yhshxc.workSummary;

import gcg.org.debug.JLog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.CoreHttpHelper;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.submitManager.bo.PendingRequestVO;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.FileHelper;
import com.yunhu.yhshxc.utility.MD5Helper;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.wechat.Util.SharedPrefrencesForWechatUtil;
import com.yunhu.yhshxc.wechat.content.Photo;
import com.yunhu.yhshxc.wechat.content.Voice;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.yunhu.yhshxc.workSummary.view.SummaryPhotoView;
import com.yunhu.yhshxc.workSummary.view.SummaryPhotoViewPreview;
import com.yunhu.yhshxc.workSummary.view.SummaryVoiceView;
import com.loopj.android.http.RequestParams;

import static com.yunhu.yhshxc.application.SoftApplication.context;

public class WorkSummaryCreateActivity extends Activity implements SummaryControlListener{
	private EditText et_summary_content;
	private ImageView btn_picture;
	private ImageView btn_photo;
	private ImageView worksummary_back;
	private Button newplan_submit;
	private List<String> photoPathList = new ArrayList<>();
	private List<String> voicePathList = new ArrayList<>();//录音路径集合
	private List<SummaryPhotoView> photoViewList = null;
	private List<SummaryVoiceView> voiceViewList = null;
	private LinearLayout ll_summary_photo;
	private LinearLayout ll_summay_voice;
	private String submitContent;
	private String CurrentDate;
	private String UserId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_work_summary_new_create);
		CurrentDate =getIntent().getStringExtra("date");
		UserId = getIntent().getStringExtra("userId");
		photoViewList = new ArrayList<SummaryPhotoView>();
		voiceViewList = new ArrayList<SummaryVoiceView>();
		photoPathList = new ArrayList<String>();//照片路径集合
		voicePathList = new ArrayList<String>();//录音路径集合
		initView();
		initData();
	}
	
	private void initView() {
		et_summary_content = (EditText) findViewById(R.id.et_summary_content);
		btn_picture = (ImageView) findViewById(R.id.btn_picture);
		btn_photo = (ImageView) findViewById(R.id.btn_photo);
		worksummary_back = (ImageView) findViewById(R.id.worksummary_back);
		newplan_submit = (Button) findViewById(R.id.newplan_submit);
		ll_summary_photo = (LinearLayout) findViewById(R.id.ll_summary_photo);
		ll_summay_voice = (LinearLayout) findViewById(R.id.ll_summay_voice);
		btn_picture.setOnClickListener(listener);
		btn_photo.setOnClickListener(listener);
		newplan_submit.setOnClickListener(listener);
		worksummary_back.setOnClickListener(listener);
		 mCameraDialog = new Dialog(this, R.style.my_dialog);  
	        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(  
	                R.layout.layout_camera_control, null);  
	        root.findViewById(R.id.btn_open_camera).setOnClickListener(listener);  
	        root.findViewById(R.id.btn_choose_img).setOnClickListener(listener);  
	        root.findViewById(R.id.btn_cancel).setOnClickListener(listener);  
	        mCameraDialog.setContentView(root);  
	        Window dialogWindow = mCameraDialog.getWindow();  
	        dialogWindow.setGravity(Gravity.BOTTOM);  
	        dialogWindow.setWindowAnimations(R.style.dialogstyle); // 添加动画  
	        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值  
	        lp.x = 0; // 新位置X坐标  
	        lp.y = -20; // 新位置Y坐标  
	        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度  
//	      lp.height = WindowManager.LayoutParams.WRAP_CONTENT; // 高度  
//	      lp.alpha = 9f; // 透明度  
	        root.measure(0, 0);  
	        lp.height = root.getMeasuredHeight();  
	        lp.alpha = 9f; // 透明度  
	        dialogWindow.setAttributes(lp);  
		
	}
	private void initData() {
		ll_summary_photo.removeAllViews();
		if (!photoViewList.isEmpty()) {
			for (int i = 0; i < photoViewList.size(); i++) {
				SummaryPhotoView photoView = photoViewList.get(i);
				photoView.setContentControlListener(this);
				ViewGroup v = (ViewGroup) photoView.getView().getParent();
				if (v!=null) {
					v.removeAllViewsInLayout();
				}
				ll_summary_photo.addView(photoView.getView());
			}
		}
		
		ll_summay_voice.removeAllViews();
		if (!voiceViewList.isEmpty()) {
			for (int i = 0; i < voiceViewList.size(); i++) {
				SummaryVoiceView voiceView = voiceViewList.get(i);
				voiceView.setContentControlListener(this);
				ViewGroup v = (ViewGroup) voiceView.getView().getParent();
				if (v!=null) {
					v.removeAllViewsInLayout();
				}
				ll_summay_voice.addView(voiceView.getView());
			}
		}
	}
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_picture://拍照、相册
				takePhotoPictrue();
				break;
			case R.id.btn_photo://录音
				voice();
				break;
			case R.id.newplan_submit://提交
				submitContent = et_summary_content.getText().toString();
				if(!TextUtils.isEmpty(submitContent)){
					showSubmitDialog();
				}else{
					Toast.makeText(WorkSummaryCreateActivity.this, PublicUtils.getResourceString(context,R.string.content_not_null), Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.worksummary_back://返回
				showCloseDialog();
				break;
			case R.id.btn_open_camera: // 打开照相机  
                if (photoPathList.size()>=9) {
    				ToastOrder.makeText(WorkSummaryCreateActivity.this, PublicUtils.getResourceString(context,R.string.no_morethan_nine_photo), ToastOrder.LENGTH_SHORT).show();
    			}else{
    				PS();
    				if (mCameraDialog != null) {  
                        mCameraDialog.dismiss();  
                    } 
    			}
                break;  
           
            case R.id.btn_choose_img:   // 打开相册  
            	if (photoPathList.size()>=9) {
    				ToastOrder.makeText(WorkSummaryCreateActivity.this, PublicUtils.getResourceString(context,R.string.no_morethan_nine_photo), ToastOrder.LENGTH_SHORT).show();
    			}else{
    				XC();
    				if (mCameraDialog != null) {  
                        mCameraDialog.dismiss();  
                    } 
    			}
                break;  
            
            case R.id.btn_cancel:  // 取消  
                if (mCameraDialog != null) {  
                    mCameraDialog.dismiss();  
                }  
                break;  
			default:
				break;
			}
			
		}

	};
	/**
	 * 直接拍照
	 */
	private String photoName;
	private void PS(){
		Intent intent = new Intent();
		Intent intent_camera = getPackageManager().getLaunchIntentForPackage("com.android.camera");
		if (intent_camera != null) {
		    intent.setPackage("com.android.camera");
		}
		intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
		File tmpFile = new File(Constants.SUMMARY_PATH_LOAD);
		if (!tmpFile.exists()) {
			tmpFile.mkdirs();
		}
		photoName = System.currentTimeMillis()+".jpg";
		File file = new File(Constants.SUMMARY_PATH_LOAD, photoName);
		if (!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Uri originalUri = Uri.fromFile(file);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, originalUri);
		startActivityForResult(intent, 101);
	}
	/*
	 * 从相册中选择
	 */
	private void XC(){
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
		startActivityForResult(intent, 100);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 100) {//相册返回
			if (data!=null) {
				resultForPhotoAlbum(data);
			}
		}else if(requestCode == 101){//拍照返回
			photoResut(Constants.SUMMARY_PATH_LOAD+photoName,true);
		}
	};
	/**
	 * 通过相册选择图片
	 */
	private Dialog loadPhotoPic = null;
	private File file;
	private void photoResut(String str ,final boolean isPZ){
		if (loadPhotoPic == null) {
			loadPhotoPic = new MyProgressDialog(WorkSummaryCreateActivity.this,R.style.CustomProgressDialog,PublicUtils.getResourceString(WorkSummaryCreateActivity.this,R.string.initTable));
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
							JLog.d("alin", "\n"+path);
							file = new File(path);
							JLog.d("alin", "\n"+file.getAbsolutePath());

							if (file.exists()) {
								String newName = System.currentTimeMillis()+".jpg";
								 sb.append(",").append(newName);
								 byte[]  b = 	helper.readImageByte(path);
								 helper.saveWeReply(b, newName,Constants.SUMMARY_PATH_LOAD);//查看用
								 helper.saveWeReply(b, newName,Constants.SUMMARY_PATH);//提交用
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
				SummaryPhotoView photoView = new SummaryPhotoView(WorkSummaryCreateActivity.this);
				Photo photo = new Photo();
				photo.setName(name);
				photo.setPhotoPath(name);
				photoView.setPhotoView(photo);
				photoView.setContentControlListener(WorkSummaryCreateActivity.this);
				ll_summary_photo.addView(photoView.getView());
				photoViewList.add(photoView);
				photoView.setFilePath(Constants.SUMMARY_PATH+name);
				photoPathList.add(Constants.SUMMARY_PATH+name);
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
					ToastOrder.makeText(WorkSummaryCreateActivity.this, R.string.work_summary_c6, ToastOrder.LENGTH_SHORT).show();
				}
			} else {
				ToastOrder.makeText(WorkSummaryCreateActivity.this, R.string.work_summary_c6, ToastOrder.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
			ToastOrder.makeText(WorkSummaryCreateActivity.this,R.string.work_summary_c6, ToastOrder.LENGTH_SHORT).show();
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
	Dialog mCameraDialog;
	private void takePhotoPictrue() {
		
        mCameraDialog.show();  
		
	}
	/**
	 * 提示是否提交对话框
	 */
	private void showSubmitDialog() {
		AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.NewAlertDialogStyle).setTitle(R.string.tip)
				.setMessage(R.string.work_summary_c4).setPositiveButton(R.string.Confirm, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
							submit();
							submitPhotos();
							submitVoices();
					}

					
				}).setNegativeButton(R.string.Cancle, null).setCancelable(false).create();
		alertDialog.show();

	}
	private MyProgressDialog mDialog = null;
	private void submit() {
		submitContent = et_summary_content.getText().toString();
		mDialog = new MyProgressDialog(WorkSummaryCreateActivity.this, R.style.CustomProgressDialog, PublicUtils.getResourceString(WorkSummaryCreateActivity.this,R.string.submint_loding));
		String url = UrlInfo.saveWorkSum(this);
		GcgHttpClient.getInstance(this).post(url, replyParams(), new HttpResponseListener() {
			@Override
			public void onSuccess(int statusCode, String content) {
				JLog.d("alin", "content:" + content);
				try {
					JSONObject obj = new JSONObject(content);
					String resultcode = obj.getString("resultcode");
					if ("0000".equals(resultcode)) {
						 Toast.makeText(getApplicationContext(),PublicUtils.getResourceString(getApplicationContext(),R.string.toast_one6),Toast.LENGTH_SHORT).show();
						 setResult(1);
						 WorkSummaryCreateActivity.this.finish();
					} else {
						throw new Exception();
					}
				} catch (Exception e) {

				}
			}

			@Override
			public void onStart() {
				mDialog.show();
			}

			@Override
			public void onFinish() {
				if(mDialog!=null&&mDialog.isShowing()){
					mDialog.dismiss();
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				Toast.makeText(WorkSummaryCreateActivity.this, PublicUtils.getResourceString(WorkSummaryCreateActivity.this,R.string.toast_one7), Toast.LENGTH_SHORT).show();
			}
		});
	}
	private RequestParams replyParams() {
		RequestParams params = new RequestParams();
		JSONObject obj = new JSONObject();
		 String key = PublicUtils.receivePhoneNO(this) + System.currentTimeMillis();
		try {
			obj.put("msg_key", key);
//			obj.put("plan_id", planId);//计划ID
			obj.put("sum_date", CurrentDate);//计划日期
			obj.put("sub_time",DateUtil.getCurDateTime());//提交时间
			obj.put("sum_marks",submitContent );
			JSONArray array = new JSONArray();
			for (int i = 0; i < photoPathList.size(); i++) {
				JSONObject object = new JSONObject();
				String url = photoPathList.get(i);
				if (url.endsWith(".jpg")) {
					int index = url.lastIndexOf("/");
					String fileName = url.substring(index + 1);
					object.put("fileType", 2);//文件类型（1：文件、2：图片、3：语音）
					object.put("name", fileName);
					object.put("showName", fileName);
				}
				File file = new File(url);
				if (file.exists()) {
					object.put("size", file.length());
				}
				array.put(object);

			}
			for (int i = 0; i < voicePathList.size(); i++) {
				JSONObject object = new JSONObject();
				String url = voicePathList.get(i);
				if (url.endsWith(".3gp")) {
					int index = url.lastIndexOf("/");
					String fileName = url.substring(index + 1);
					object.put("fileType", 3);//文件类型（1：文件、2：图片、3：语音）
					object.put("name", fileName);
					object.put("showName", fileName);
				}
				File file = new File(url);
				if (file.exists()) {
					object.put("size", file.length());
				}
				array.put(object);
			}
			;
			obj.put("annexFile", array);
			params.put("work", obj.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return params;
	}
	/**
	 * 提交图片
	 */
	private void submitPhotos() {
		for (int i = 0; i < photoPathList.size(); i++) {
//			String name = s[i];
			String url = photoPathList.get(i);
			String fileName = "";
			if (url.endsWith(".jpg")) {
				int index = url.lastIndexOf("/");
				 fileName = url.substring(index + 1);
			}
			PendingRequestVO vo = new PendingRequestVO();
			vo.setContent(PublicUtils.getResourceString(context,R.string.work_summary_c2));
			vo.setTitle(PublicUtils.getResourceString(context,R.string.work_summary_c3));
			vo.addFiles("name", url);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("cid", String.valueOf(SharedPreferencesUtil.getInstance(this).getCompanyId()));
			params.put("name", fileName);
			params.put("fip", "help.gcgcloud.com");
			params.put("md5Code", MD5Helper.getMD5Checksum2(url));
			vo.setParams(params);
			new CoreHttpHelper().performWehatUpload(this, vo);
		}
	}
	/**
	 * 提交录音
	 */
	private void submitVoices() {
		try {
			for(int i = 0; i<voicePathList.size();i++){
				String url = voicePathList.get(i);
				String fileName  ="";
				if (url.endsWith(".3gp")) {
					int index = url.lastIndexOf("/");
					 fileName = url.substring(index + 1);
				}
				if (!TextUtils.isEmpty(fileName)) {

						PendingRequestVO vo = new PendingRequestVO();
						vo.setTitle(PublicUtils.getResourceString(context,R.string.work_summary_c2));
						vo.setContent(PublicUtils.getResourceString(context,R.string.work_summary_c1));
						vo.addFiles("name", url);
						HashMap<String, String> params = new HashMap<String, String>();
						params.put("cid", String.valueOf(SharedPreferencesUtil.getInstance(this).getCompanyId()));
						params.put("name", fileName);
						params.put("fip", "help.gcgcloud.com");
						params.put("md5Code", MD5Helper.getMD5Checksum2(url));
						vo.setParams(params);
						new CoreHttpHelper().performWehatUpload(this, vo);
				}
			}
				
		} catch (Exception e) {
		}
	}

	/**
	 * 关闭提示对话框
	 */
	private void showCloseDialog() {
		AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.NewAlertDialogStyle).setTitle(R.string.Confirm)
				.setMessage(R.string.work_summary_c5).setPositiveButton(R.string.Confirm, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						clearSubmitDatas();
						WorkSummaryCreateActivity.this.finish();

					}

					
				}).setNegativeButton(R.string.Cancle, null).setCancelable(false).create();
		alertDialog.show();

	}
	/**
	 * 清楚未提交数据，图片以及录音文件
	 */
	private void clearSubmitDatas() {
		if (!photoPathList.isEmpty()) {// 如果有照片的话删除照片
			for (int k = 0; k < photoPathList.size(); k++) {
				String tempPath = photoPathList.get(k);
				File tempFile = new File(tempPath);
				if (tempFile.exists()) {
					tempFile.delete();
				}
			}
		}
		if(!voicePathList.isEmpty()){//如果有录音文件的话删除录音
			for (int i = 0; i < voicePathList.size(); i++) {
				String tempPString = voicePathList.get(i);
				File tempFile = new File(tempPString);
				if (tempFile.exists()) {
					tempFile.delete();
				}
			}
		}
	}
	@Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			showCloseDialog();
		}
		return super.onKeyDown(keyCode, event);
    }

	/**
	 * 删除照片
	 */
	@Override
	public void contentControl(SummaryPhotoView photoView) {
		ll_summary_photo.removeView(photoView.getView());
		JLog.d("alin", ll_summary_photo.toString()+"------------------:"+ll_summary_photo.getChildCount());
		photoViewList.remove(photoView);
		String filePath = photoView.getFilePath();
		photoPathList.remove(filePath);
		
	}

	/**
	 * 删除录音文件
	 */
	@Override
	public void contentVoiceControl(SummaryVoiceView voiceView) {
		ll_summay_voice.removeView(voiceView.getView());
		voiceViewList.remove(voiceView);
		String filePath = voiceView.getFilePath();
		voicePathList.remove(filePath);
		
	}
	
	/**
	 * 录音
	 */
	private SummaryVoicePopupWindow recordPopupWindow;

	public void voice() {
		recordPopupWindow = new SummaryVoicePopupWindow(this);
		recordPopupWindow.setFileName(voiceFileName);
		recordPopupWindow.show(newplan_submit);
	}
	/**
	 * 保存录音文件
	 */

	private String voiceFileName;

	public void saveVoice(String name) {
		voiceResult(name);
	}

	private void voiceResult(String name) {
		voiceForResult(Constants.RECORD_PATH+name);
	}
	
	private Dialog voiceDialog;
	public void voiceForResult(final String path){
		if (voiceDialog == null) {
			voiceDialog = new MyProgressDialog(WorkSummaryCreateActivity.this,R.style.CustomProgressDialog,PublicUtils.getResourceString(context,R.string.init));
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
						SharedPrefrencesForWechatUtil.getInstance(WorkSummaryCreateActivity.this).setFileName(name,name);
							File destDir = new File(Constants.SUMMARY_PATH_LOAD);
							if (!destDir.exists()) {
								destDir.mkdirs();
							}
						helper.copyFile(path, Constants.SUMMARY_PATH_LOAD+name);
//						helper.copyFile(path, Constants.SUMMARY_PATH+name);
						 
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
				SummaryVoiceView voiceView = new SummaryVoiceView(WorkSummaryCreateActivity.this);
				Voice voice = new Voice();
				voice.setName(name);
				voiceView.setVoiceView(voice);
				voiceView.setContentControlListener(WorkSummaryCreateActivity.this);
				ll_summay_voice.addView(voiceView.getView());
				voiceView.setFilePath(Constants.SUMMARY_PATH_LOAD+name);
				voiceViewList.add(voiceView);
				voicePathList.add(Constants.SUMMARY_PATH_LOAD+name);
				
				break;
			case 2:
				ToastOrder.makeText(WorkSummaryCreateActivity.this, PublicUtils.getResourceString(context,R.string.audio_load_failure), ToastOrder.LENGTH_SHORT).show();
				break;
			case 3:
	        	  ToastOrder.makeText(WorkSummaryCreateActivity.this, PublicUtils.getResourceString(context,R.string.un_support_type), ToastOrder.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		};
	};

	@Override
	public void contentControl(SummaryPhotoViewPreview photoView) {
		// TODO Auto-generated method stub
		
	}
	
}
