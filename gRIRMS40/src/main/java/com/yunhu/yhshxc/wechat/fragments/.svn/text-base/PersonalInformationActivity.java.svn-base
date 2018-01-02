package com.yunhu.yhshxc.wechat.fragments;

import gcg.org.debug.JLog;

import java.io.File;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.submitManager.bo.PendingRequestVO;
import com.yunhu.yhshxc.submitManager.bo.TablePending;
import com.yunhu.yhshxc.submitManager.core.SubmitWorkManager;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.FileHelper;
import com.yunhu.yhshxc.utility.MD5Helper;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.wechat.Util.ImageViewUtils;
import com.yunhu.yhshxc.wechat.Util.ImageViewUtils.FileType;
import com.yunhu.yhshxc.wechat.Util.SharedPrefrencesForWechatUtil;
import com.yunhu.yhshxc.wechat.Util.WechatUtil;
import com.yunhu.yhshxc.wechat.bo.UserPerson;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class PersonalInformationActivity extends AbsBaseActivity {
	private TextView tv_zw;
	private TextView tv_personal_return;
	private TextView tv_personal_xiugai;
	private TextView tv_gxqm;
	private LinearLayout ll_zw;
	private LinearLayout ll_gxqm;
	private LinearLayout ll_header;
	private ImageView iv_header;
	private EditNCDialog ncDialog;
	private Bitmap bitmap ;
	private UserPerson user;
	private WechatUtil util;
	/* 头像名称 */
//	private static final String PHOTO_FILE_NAME = SystemClock.currentThreadTimeMillis()+".png";
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;
	private boolean isLocalImg = false;//是否是本地图片
	private MyProgressDialog progressDialog;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wechat_personal_edit);
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.people)
		.showImageForEmptyUri(R.drawable.people)
		.showImageOnFail(R.drawable.people)
		.cacheInMemory()
		.cacheOnDisc().displayer(new RoundedBitmapDisplayer(10))
		.build();
		user = new UserPerson();
		util = new WechatUtil(this);
		initViews();
		initDataInfo();
		SharedPrefrencesForWechatUtil.getInstance(PersonalInformationActivity.this).setIsChangedReader("0");
	}
	
	private void initViews() {
		tv_zw = (TextView) findViewById(R.id.tv_zw);
		tv_personal_xiugai = (TextView) findViewById(R.id.tv_personal_xiugai);
		tv_personal_return = (TextView) findViewById(R.id.tv_personal_return);
		tv_gxqm = (TextView) findViewById(R.id.tv_gxqm);
		iv_header = (ImageView) findViewById(R.id.iv_header);
		iv_header.setOnClickListener(listener);
		ll_gxqm = (LinearLayout) findViewById(R.id.ll_gxqm);
		ll_zw = (LinearLayout) findViewById(R.id.ll_zw);
		ll_header = (LinearLayout) findViewById(R.id.ll_header);
		ll_zw.setOnClickListener(listener);
		ll_gxqm.setOnClickListener(listener);
		tv_personal_return.setOnClickListener(listener);
		tv_personal_xiugai.setOnClickListener(listener);
		ll_header.setOnClickListener(listener);
	}
	private void initDataInfo() {
		String nicheng = SharedPrefrencesForWechatUtil.getInstance(this).getNicheng();
		tv_zw.setText(nicheng);
		String qianming = SharedPrefrencesForWechatUtil.getInstance(this).getQianMing();
		tv_gxqm.setText(qianming);
		File file = ImageViewUtils.createFile(ImageViewUtils.TEMP_IMAGE_DIR, FileType.DIRECTORY);
		File file1 = ImageViewUtils.createFile(Constants.WECHAT_PATH_HEADER_LOAD, FileType.DIRECTORY);
		String url = SharedPrefrencesForWechatUtil.getInstance(this).getHeaderName();
		if(!TextUtils.isEmpty(url)){
			int index = url.lastIndexOf("/");
 			String fileName = url.substring(index + 1);
 			String[] str = fileName.split("@");
 			if (str.length>1) {
 				fileName = str[1];
			}
			File f = new File(Constants.WECHAT_PATH_HEADER_LOAD+fileName);
//			if (f.exists()) {
//				JLog.d("alin", "path= "+"file://"+Constants.WECHAT_PATH_HEADER_LOAD+fileName);
//				imageLoader.displayImage("file://"+Constants.WECHAT_PATH_HEADER_LOAD+fileName, iv_header, options, null);
//			}else{
				imageLoader.displayImage(url, iv_header, options, null);
//			}
		}
		
	}
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ll_header://设置头像
			case R.id.iv_header://设置头像
				setHearder();
				break;
			case R.id.ll_zw://设置昵称
				setNiCheng();
				break;
			case R.id.ll_gxqm://设置个性签名
				setGXQM();
				break;
			case R.id.tv_personal_xiugai:
				submit();
				break;
			case R.id.tv_personal_return:
				finish();
				break;
			default:
				break;
			}
		}

	};
	
	private void setGXQM() {
		ncDialog = new EditNCDialog(PersonalInformationActivity.this,R.style.CustomProgressDialog,tv_gxqm,false);
		ncDialog.show();
	}
	private void setNiCheng() {
		ncDialog = new EditNCDialog(PersonalInformationActivity.this,R.style.CustomProgressDialog,tv_zw,true);
		ncDialog.show();
	}
	
	/**
	 *头像设置
	 */
	private void setHearder() {
		View contentView = View.inflate(PersonalInformationActivity.this, R.layout.wechat_touxiangxuanze, null);
		Button quertBtn = (Button)contentView.findViewById(R.id.btn_nearby_visit_store_info_check);
		Button updateBtn = (Button)contentView.findViewById(R.id.btn_nearby_visit_store_info_update);
		Button cancelBtn = (Button)contentView.findViewById(R.id.btn_nearby_visit_store_info_cancel);
		
		final PopupWindow popupWindow = new PopupWindow(contentView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, true);
		popupWindow.setAnimationStyle(R.style.popupwindow);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.update();//更新弹出窗口的状态
		popupWindow.setBackgroundDrawable(new BitmapDrawable());//设置一个默认的背景
		popupWindow.showAtLocation(findViewById(R.id.ll_nearby_bottom), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); 
		quertBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				camera(v);
				popupWindow.dismiss();
			}
		});
		updateBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				gallery(v);
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
	/*
	 * 从相册获取
	 */
	public void gallery(View view) {
		ImageViewUtils.GoAlbum(this);
	}

	/*
	 * 从相机获取
	 */
	public void camera(View view) {
		ImageViewUtils.GoCaptureAndSavePicture(this,"aa.png");
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		String url = SharedPrefrencesForWechatUtil.getInstance(this).getHeaderName();
		if (requestCode == ImageViewUtils.INTENT_PICTURE
				&& resultCode == RESULT_OK) {
			String name = "";
			if(!TextUtils.isEmpty(url)){
				Pattern pattern = Pattern.compile("[\\d]+.jpg");
				Matcher matcher = pattern.matcher(url);
				if (matcher.find()) {
					name=matcher.group(0);
				}
			}else{
				 name = System.currentTimeMillis()+".jpg";
			}
			
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
				
			    ImageViewUtils.GoCropAndSave(this,
						new File(ImageViewUtils.getPathHigh(this, data)), name);
			}else{
				ImageViewUtils.GoCropAndSave(this,
						new File(ImageViewUtils.getPathLow(this, data)), name);
			}
			user.setTouxiang(name);

		}
		if (requestCode == ImageViewUtils.INTENT_CAPTURE
				&& resultCode == RESULT_OK) {
			String name = "";
			if(!TextUtils.isEmpty(url)){
				Pattern pattern = Pattern.compile("[\\d]+.jpg");
				Matcher matcher = pattern.matcher(url);
				if (matcher.find()) {
					name=matcher.group(0);
				}
			}else{
				 name = System.currentTimeMillis()+".jpg";
			}
			ImageViewUtils.GoCropAndSave(this,
					new File(ImageViewUtils.imageUri.getPath()), name);
			user.setTouxiang(name);

		}
		if (requestCode == ImageViewUtils.INTENT_CROP
				&& resultCode == RESULT_OK) {
			iv_header.setImageBitmap(ImageViewUtils.getBitmapFromUri(
					ImageViewUtils.imageUri, this));
			
			File file = new File(Constants.WECHAT_PATH+user.getTouxiang());
	        if (file == null || !file.exists()) {
	            return;
	        }
	        fileResult(file);
//			SharedPrefrencesForWechatUtil.getInstance(PersonalInformationActivity.this).setIsChangedReader("1");
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	private void fileResult(final File srcFile){
		new Thread(){
			public void run() {
	        	   try {
	        		   String filePath = srcFile.getAbsolutePath();
		        		if (!TextUtils.isEmpty(filePath)) {
						int index = filePath.lastIndexOf("/");
						String name = filePath.substring(index + 1);
						FileHelper helper = new FileHelper();
						helper.copyFile(filePath, Constants.WECHAT_PATH_HEADER_LOAD+name);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}
	private void submit() {
		progressDialog = new MyProgressDialog(PersonalInformationActivity.this,R.style.CustomProgressDialog,
				getResources().getString(R.string.str_wechat_person_in));
		progressDialog.show();
		String str = tv_zw.getText().toString().trim();
		if(!TextUtils.isEmpty(str)){
			String sCon = str.replaceAll("\r|\n", "");
			user.setNicheng(sCon);
		}
		String sQm = tv_gxqm.getText().toString().trim();
		if(!TextUtils.isEmpty(sQm)){
			String sCon = sQm.replaceAll("\r|\n", "");
			user.setQianming(sCon);
		}
		
		String url = UrlInfo.doWeChatUserInfo(this);
		GcgHttpClient.getInstance(this).post(url, params(user),
				new HttpResponseListener() {

					@Override
					public void onSuccess(int statusCode, String content) {
						JLog.d("alin", "content:" + content);
						try {
							JSONObject obj = new JSONObject(content);
							String resultcode = obj.getString("resultcode");
							if ("0000".equals(resultcode)) {
								Toast.makeText(PersonalInformationActivity.this, R.string.wechat_content31, Toast.LENGTH_LONG).show();
								if(!TextUtils.isEmpty(user.getTouxiang())){
									SharedPrefrencesForWechatUtil.getInstance(PersonalInformationActivity.this).setIsChangedReader("1");
								}
								submitPhotoBackground(user.getTouxiang());
								SubmitWorkManager.getInstance(PersonalInformationActivity.this).commit();
								
								SharedPrefrencesForWechatUtil.getInstance(PersonalInformationActivity.this).setNiCheng(user.getNicheng());
								SharedPrefrencesForWechatUtil.getInstance(PersonalInformationActivity.this).setQianMing(user.getQianming());
								
								if (progressDialog != null && progressDialog.isShowing()) {
									progressDialog.dismiss();
								}
								PersonalInformationActivity.this.finish();
							} else {
								throw new Exception();
							}
						} catch (Exception e) {
//							searchHandler.sendEmptyMessage(3);
							if (progressDialog != null && progressDialog.isShowing()) {
								progressDialog.dismiss();
							}
							Toast.makeText(PersonalInformationActivity.this, R.string.wechat_content30, Toast.LENGTH_LONG).show();
						}
					}

					@Override
					public void onStart() {
					}

					@Override
					public void onFinish() {
						if (progressDialog != null && progressDialog.isShowing()) {
							progressDialog.dismiss();
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						JLog.d("alin", "failure");
						if (progressDialog != null && progressDialog.isShowing()) {
							progressDialog.dismiss();
						}
						Toast.makeText(PersonalInformationActivity.this, R.string.wechat_content30, Toast.LENGTH_LONG).show();
					}
				});
	}

	private RequestParams params(UserPerson user2) {
		RequestParams params = new RequestParams();
		params.put("phoneno", PublicUtils.receivePhoneNO(this));
		try {
			String data = util.submitPersonal(user2);
			params.put("data", data);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JLog.d("alin", "params:"+params.toString());
		return params;
	}
	/**
	 * 提交群头像图片
	 * @param name
	 */
	private void submitPhotoBackground(String name){
		PendingRequestVO vo = new PendingRequestVO();
		vo.setContent(PublicUtils.getResourceString(this,R.string.wechat_content29)+":");
		vo.setTitle(PublicUtils.getResourceString(this,R.string.wechat_content28));
		vo.setMethodType(SubmitWorkManager.HTTP_METHOD_POST);
		vo.setType(TablePending.TYPE_IMAGE);
		vo.setUrl(UrlInfo.getUrlPhoto(PersonalInformationActivity.this));
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("name", name);
		params.put("companyid", String.valueOf(SharedPreferencesUtil.getInstance(PersonalInformationActivity.this).getCompanyId()));
		params.put("md5Code", MD5Helper.getMD5Checksum2(Constants.WECHAT_PATH + name));
		vo.setParams(params);
		vo.setImagePath(Constants.WECHAT_PATH + name);
		SubmitWorkManager.getInstance(PersonalInformationActivity.this).performImageUpload(vo);
	}
	
}
