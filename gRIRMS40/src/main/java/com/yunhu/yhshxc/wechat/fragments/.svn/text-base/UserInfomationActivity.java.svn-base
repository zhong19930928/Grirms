package com.yunhu.yhshxc.wechat.fragments;

import gcg.org.debug.JLog;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.bo.OrgUser;
import com.yunhu.yhshxc.database.OrgUserDB;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class UserInfomationActivity extends AbsBaseActivity{
	private ImageView iv_header;
	private TextView tv_personal_name;
	private TextView tv_zw;
	private TextView tv_bm;
	private TextView tv_gxqm;
	private TextView tv_personal_nicheng;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;
	private OrgUserDB orgUserDB;
	private boolean isLocalImg = false;//是否是本地图片
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		String userIds = intent.getStringExtra("userId");
		int userId = 0;
		if(!TextUtils.isEmpty(userIds)){
			userId = Integer.parseInt(userIds);
		}
		setContentView(R.layout.wechat_personal);
		orgUserDB = new OrgUserDB(this);
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.people)
		.showImageForEmptyUri(R.drawable.people)
		.showImageOnFail(R.drawable.people)
		.cacheInMemory()
		.cacheOnDisc().displayer(new RoundedBitmapDisplayer(10))
		.build();
		initView(userId);
		initInfo(userId);
	}
	private void initInfo(int userId) {
		String url = UrlInfo.weChatUserInfo(UserInfomationActivity.this);
		GcgHttpClient.getInstance(UserInfomationActivity.this).post(url, params(userId),
				new HttpResponseListener() {

					@Override
					public void onSuccess(int statusCode, String content) {
						JLog.d("alin", "content:" + content);
						try {
							JSONObject obj = new JSONObject(content);
							String resultcode = obj.getString("resultcode");
							String weChat_user = obj.getString("weChat_user");
							if ("0000".equals(resultcode)) {
								JSONObject objData = new JSONObject(weChat_user);
								if (PublicUtils.isValid(objData, "headImg")) {
									String imageUrl = objData.getString("headImg");
									if (!TextUtils.isEmpty(imageUrl)) {
//											DiskCacheUtils.removeFromCache(imageUrl, imageLoader.getDiskCache());
//											MemoryCacheUtils.removeFromCache(imageUrl, imageLoader.getMemoryCache());
										if (isLocalImg) {
											imageLoader.displayImage("file://"+imageUrl, iv_header, options, null);
										}else{
											if (imageUrl.startsWith("http")) {
												imageLoader.displayImage(imageUrl, iv_header, options, null);
											}
										}
									}
								}
								
								if(PublicUtils.isValid(objData, "nickName")){
									String nicheng = objData.getString("nickName");
									if(!TextUtils.isEmpty(nicheng)){
										tv_personal_nicheng.setText(nicheng);
									}else{
										tv_personal_nicheng.setText("");
									}
								}else{
									tv_personal_nicheng.setText("");
								}
								if(PublicUtils.isValid(objData, "signature")){
									String gexingqianm = objData.getString("signature");
									tv_gxqm.setText(gexingqianm);
								}else{
									tv_gxqm.setText("");
								}
								if(PublicUtils.isValid(objData, "orgName")){
									String bumen = objData.getString("orgName");
									tv_bm.setText(bumen);
								}else{
									tv_bm.setText("");
								}
								
								if(PublicUtils.isValid(objData, "roleName")){
									String zhiwei = objData.getString("roleName");
									tv_zw.setText(zhiwei);
								}else{
									tv_zw.setText("");
								}
								
							} else {
								throw new Exception();
							}
						} catch (Exception e) {
//							searchHandler.sendEmptyMessage(3);
							Toast.makeText(UserInfomationActivity.this, R.string.reload_failure, Toast.LENGTH_LONG).show();
						}
					}

					@Override
					public void onStart() {
					}

					@Override
					public void onFinish() {

					}

					@Override
					public void onFailure(Throwable error, String content) {
						JLog.d("alin", "failure");
						Toast.makeText(UserInfomationActivity.this, R.string.reload_failure, Toast.LENGTH_LONG).show();
					}
				});
	}
	private RequestParams params(int userId) {
		RequestParams params = new RequestParams();
//		params.put("phoneno", PublicUtils.receivePhoneNO(getActivity()));
		params.put("userId", userId);
		JLog.d("alin", "params:"+params.toString());
		return params;
	}
	private void initView(int userId) {
		iv_header = (ImageView)findViewById(R.id.iv_header);
		tv_personal_name = (TextView) findViewById(R.id.tv_personal_name);
		tv_personal_nicheng = (TextView) findViewById(R.id.tv_personal_nicheng);
		tv_zw = (TextView) findViewById(R.id.tv_zw);
		tv_bm = (TextView) findViewById(R.id.tv_bm);
		tv_gxqm = (TextView) findViewById(R.id.tv_gxqm);
		OrgUser user = orgUserDB.findUserByUserId(userId);
		if(user!=null){
			tv_personal_name.setText(user.getUserName());
		}else{
			tv_personal_name.setText("");
		}
//		
	}
}
