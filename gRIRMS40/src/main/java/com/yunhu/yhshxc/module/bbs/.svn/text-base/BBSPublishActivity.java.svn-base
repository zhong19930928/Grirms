package com.yunhu.yhshxc.module.bbs;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.bo.LocationResult;
import com.yunhu.yhshxc.comp.location.LocationControlListener;
import com.yunhu.yhshxc.comp.location.LocationForBBS;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.FileHelper;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.loopj.android.http.RequestParams;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import gcg.org.debug.JLog;

public class BBSPublishActivity extends AbsBaseActivity implements LocationControlListener{
	private final String TAG = "BBSPublishActivity";
	
	private EditText input; 
	private ImageView location; 
	private ImageView photo; 
//	private CoreHttpHelper httpHelper;
	private String locationContent = "";
	private Dialog dialog;
	private boolean location_flag = true;
	private boolean isFinishData = true;
	private boolean isFinishFhoto = true;

	private LinearLayout llPublish; 
	
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			/*if(dialog!=null&&dialog.isShowing()){
				dialog.dismiss();
			}*/
			switch (msg.what) {
			case 18001:
				JLog.d(TAG, "InitActivity => 返回成功");
				break;
			default:
				break;
			}
		}
		
	};

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bbs_publish);
		initBase();
		init();
		//initGPSOne();
	}
	
	private void init() {
		
		input = (EditText) findViewById(R.id.et_input);
		location = (ImageView) findViewById(R.id.iv_location);
		photo = (ImageView) findViewById(R.id.iv_photo);

		llPublish = (LinearLayout) findViewById(R.id.ll_tv_bbs_publish);
		
		llPublish.setOnClickListener(this);
		dialog = initDialog(getResources().getString(R.string.waitForAMoment));

	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_location:   
			if (!location_flag){
				ToastOrder.makeText(BBSPublishActivity.this, setString(R.string.bbs_info_14), ToastOrder.LENGTH_LONG).show();
				return;
			}
//			dialog.show();
//			initGPSOne();
			initLocation(location_flag);

			break;
		case R.id.iv_camera:
			camera();
			break;
			
		case R.id.ll_tv_bbs_publish:
			publish();
			break;
		default:
			break;  
		}
	}
	
	/*
	 * 拍照
	 */
	String photoFileName; // 照片的名字

	private void camera() {
		Intent mIntent = new Intent("android.media.action.IMAGE_CAPTURE");
		photoFileName = System.currentTimeMillis() + ".jpg";
		String tempPath = Constants.SDCARD_PATH + "/temp/";
		File tmpFile = new File(tempPath);
		if (!tmpFile.exists()) {
			tmpFile.mkdir();
		}
		File file = new File(tempPath, photoFileName);
		Uri originalUri = Uri.fromFile(file);
		mIntent.putExtra(MediaStore.EXTRA_OUTPUT, originalUri);
		startActivityForResult(mIntent, 100);
	}
	
	/*
	 * 拍照返回
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
//		if (Activity.RESULT_OK == resultCode) {
//			
//		} else {
			
			String tempPath = Constants.SDCARD_PATH + "/temp/" + photoFileName;
			File tempFile = new File(tempPath);
			if (!tempFile.exists()) {
				ToastOrder.makeText(BBSPublishActivity.this, setString(R.string.bbs_info_13), ToastOrder.LENGTH_LONG).show();
				photoFileName = "";
//				camera.setImageResource(R.drawable.bbs_camera);
				return;
			}
			FileHelper fileHelper = new FileHelper();
			Bitmap bitmap = fileHelper.readImageFile(tempPath, 320);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			saveFile(baos.toByteArray(), photoFileName);
			
			photo.setImageBitmap(bitmap);//照片
			
			if (tempFile.exists()) {
				tempFile.delete();
			}
//		}
	}
	
	private void publish() {
		final String content = input.getText().toString().trim();
		if (TextUtils.isEmpty(content) && TextUtils.isEmpty(photoFileName)) {
			ToastOrder.makeText(BBSPublishActivity.this, setString(R.string.bbs_info_15), ToastOrder.LENGTH_LONG).show();
			return;
		}
		input.setText("");
		submitData(content);
	}
	
	private void submitData(String content){
		dialog = initDialog(setString(R.string.bbs_info_16));
		RequestParams rp = new RequestParams();
		rp.put("partakecontent",content);
		if(!TextUtils.isEmpty(photoFileName)){
			rp.put("photo", photoFileName);
		}
		if(!TextUtils.isEmpty(locationContent)){
			rp.put("address", locationContent);
		}
		
		GcgHttpClient.getInstance(this).post(UrlInfo.getUrlBbsSubmitInfo(BBSPublishActivity.this), rp, new HttpResponseListener() {
			
			@Override
			public void onSuccess(int statusCode, String content) {
				if (TextUtils.isEmpty(photoFileName)) {
					BBSPublishActivity.this.finish();
				}else{
					submitPhoto();
				}
			}
			
			@Override
			public void onStart() {
				dialog.show();
			}
			
			@Override
			public void onFinish() {
				dialog.dismiss();
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				ToastOrder.makeText(BBSPublishActivity.this, setString(R.string.bbs_info_17), ToastOrder.LENGTH_SHORT).show();
			}
		});
		
		
	}
	
	private void submitPhoto(){
		try {
			dialog = initDialog(setString(R.string.bbs_info_18));
			RequestParams p = new RequestParams();
			p.put("name", photoFileName);
			p.put("photo", new File(Constants.CONTENTIV_PATH + photoFileName));
			p.put("companyid", String.valueOf(SharedPreferencesUtil.getInstance(this).getCompanyId()));
			GcgHttpClient.getInstance(this).post(UrlInfo.getUrlBbsSubmitImg(BBSPublishActivity.this), p,new HttpResponseListener() {

					@Override
					public void onSuccess(int statusCode, String content) {
						BBSPublishActivity.this.finish();
					}

					@Override
					public void onStart() {
						dialog.show();
					}

					@Override
					public void onFinish() {
						dialog.dismiss();
					}

					@Override
					public void onFailure(Throwable error, String content) {
						ToastOrder.makeText(BBSPublishActivity.this,setString(R.string.bbs_info_18), ToastOrder.LENGTH_SHORT).show();
					}
				});
			
//			Map<String, String> params = new HashMap<String, String>();
//			params.put("companyid", String.valueOf(SharedPreferencesUtil.getInstance(this).getCompanyId()));
//			Map<String, String> paramsFile = new HashMap<String, String>();
//			paramsFile.put("name", Constants.CONTENTIV_PATH + photoFileName);
//			new HttpHelper(this).uploadFile(UrlInfo.getUrlBbsSubmitImg(BBSPublishActivity.this), params, paramsFile);
//			
			} catch (Exception e) {
				ToastOrder.makeText(this, setString(R.string.bbs_info_20), ToastOrder.LENGTH_SHORT).show();
		}
		
		
		
		
	}
	
	private Dialog initDialog(String content) {
		// new一个对话框
		final Dialog initDialog = new Dialog(this, R.style.transparentDialog);
		// 屏蔽返回按钮
		initDialog.setCancelable(false);
		// 加载对话框的布局
		View view = View.inflate(this, R.layout.bbs_location_dialog, null);
		// 查找对话框显示的textview
		TextView initContent = (TextView) view.findViewById(R.id.tv_init_content);
		// 设置对话框显示的内容
		initContent.setText(content);
		// 在对话框中添加要显示的内容
		initDialog.setContentView(view);
		return initDialog;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
//		registerReceiver(LocationReceiver, new IntentFilter(Constants.BROADCAST_LOCATION_FINISH),null,locationHandler);
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	/**
	 * 保存指定的data内容到指定的文件中
	 * @param data
	 * @param fileName
	 */
	private  void saveFile(byte[] data,String fileName){
		OutputStream os=null;
		try {
			File dir = new File(Constants.CONTENTIV_PATH);
			if(!dir.exists()){
				dir.mkdirs();
			}
			File file = new File(Constants.CONTENTIV_PATH+fileName);
			if(!file.exists()){
				file.createNewFile();
			}
			
			os = new FileOutputStream(file);
			os.write(data, 0, data.length);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(os!=null){
					os.close();
				}
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}
	private LocationForBBS locationForBBS;
	private void initLocation(boolean isAreaSearchLocation){
		dialog = new MyProgressDialog(this,R.style.CustomProgressDialog,getResources().getString(R.string.waitForAMoment));
		locationForBBS = new LocationForBBS(this,this);
		locationForBBS.setLoadDialog(dialog);
		locationForBBS.setAreaSearchLocation(isAreaSearchLocation);//设置是否是店面距离搜索定位
		locationForBBS.startLocation();
	}

	@Override
	public void confirmLocation(LocationResult result) {
		
	}

	@Override
	public void areaSearchLocation(LocationResult result) {
		location_flag = false;
		ToastOrder.makeText(this, setString(R.string.bbs_info_12), ToastOrder.LENGTH_LONG).show();
		location.setImageResource(R.drawable.bbs_location);
		locationContent = result.getAddress();
		
		
	}

	private String setString(int stringId){
		return getResources().getString(stringId);
	}
}
