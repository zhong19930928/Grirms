package com.yunhu.yhshxc.activity.login;

import gcg.org.debug.JLog;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.activity.HomePageActivity;
import com.yunhu.yhshxc.activity.InitActivity;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.parser.CacheData;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.loopj.android.http.RequestParams;

public class LoginForFreeActivity extends AbsBaseActivity{
	
	private Button loginBtn;//激活按钮
	private EditText et_username;//用户名
	private EditText et_phoneno;//手机号
	private EditText et_password;//密码
	private boolean onceLogin=false;//限制快速多次点击开启多个密码
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_for_free);
		initWidget();
		testLogin();
	}
	
	private void initWidget(){
		loginBtn = (Button)findViewById(R.id.btn_login);
		loginBtn.setOnClickListener(listener);
		et_username = (EditText)findViewById(R.id.et_username);
		et_phoneno = (EditText)findViewById(R.id.et_phoneno);
		et_password = (EditText)findViewById(R.id.et_password);
	}
	
	/**
	 * 测试用户
	 */
	private void testLogin(){
//		et_username.setText("pla");
//		et_phoneno.setText("17701050109");
//		et_password.setText("123");
		
//		et_username.setText("weidong");
//		et_phoneno.setText("18910858903");
//		et_password.setText("123");
		
//		et_username.setText("1892");
//		et_phoneno.setText("18910901892");
//		et_password.setText("123");
		
//		et_username.setText("1862");
//		et_phoneno.setText("18910901862");
//		et_password.setText("123");
	}
	
	
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_login:
//				if (!onceLogin) {
					onceLogin=true;
					login();
//				}
				break;

			default:
				break;
			}
		}
	};
	
	private void login(){
		if (isComplete()) {
			final Dialog dialog = new MyProgressDialog(this,R.style.CustomProgressDialog,PublicUtils.getResourceString(this,R.string.acitvation));
			GcgHttpClient.getInstance(this,GcgHttpClient.TIME_OUT_SHORT).post(loginUrl(), loginParams(), new HttpResponseListener() {
				
				@Override
				public void onSuccess(int statusCode, String content) {
					try {
						loginSuccess(content);
					} catch (Exception e) {
						Toast.makeText(LoginForFreeActivity.this, R.string.tip_confirm, Toast.LENGTH_SHORT).show();
						onceLogin=false;
					}
				}
				
				@Override
				public void onStart() {
					if (dialog!=null && !dialog.isShowing()) {
						dialog.show();
					}
				}
				
				@Override
				public void onFinish() {
					if (dialog!=null && dialog.isShowing()) {
						dialog.dismiss();
					}
				}
				
				@Override
				public void onFailure(Throwable error, String content) {
					Toast.makeText(LoginForFreeActivity.this, R.string.re_net_error, Toast.LENGTH_SHORT).show();
					onceLogin=false;
				}
			});
		}
	}
	
	
	private void loginSuccess(String json) throws Exception{
		JLog.d(TAG, "version==>" + json);
		JSONObject jsonObject = new JSONObject(json);
		String resultcode = jsonObject.getString("resultcode");
		if ("0000".equals(resultcode)) {
			if (jsonObject.has("version")) {
				JSONObject versionObj = jsonObject.getJSONObject("version");
				//服务端最新APK安装包的MD5
				if (versionObj.has("md5code")) {
					String md5code = versionObj.getString("md5code");
					SharedPreferencesUtil.getInstance(getApplicationContext()).saveMD5Code(md5code);
				}
				//服务端最新APK安装包的下载URL
				if (versionObj.has("url")) {
					String url = versionObj.getString("url");
					SharedPreferencesUtil.getInstance(getApplicationContext()).saveDownUrl(url);
				}
				//服务端最新APK安装包的版本
				if (versionObj.has("version")) {
					String version = versionObj.getString("version");
					SharedPreferencesUtil.getInstance(getApplicationContext()).saveNewVersion(version);
				}
				//服务端最新APK安装包的大小 KB为单位
				if (versionObj.has("size")) {
					String size = versionObj.getString("size");
					SharedPreferencesUtil.getInstance(getApplicationContext()).saveApkSize(size);
				}
				//用户的有效期的开始日期
				if (versionObj.has(CacheData.AVAIL_START)) {
					SharedPreferencesUtil.getInstance(getApplicationContext()).setUsefulStartDate(versionObj.getString(CacheData.AVAIL_START));
				}
				//用户的有效期的结束日期
				if (versionObj.has(CacheData.AVAIL_END)) {
					SharedPreferencesUtil.getInstance(getApplicationContext()).setUsefulEndDate(versionObj.getString(CacheData.AVAIL_END));
				}
				//存储用户手机号码
				String phoneno = et_phoneno.getText().toString();
				getApplicationContext().getSharedPreferences(PublicUtils.PREFERENCE_NAME, Context.MODE_PRIVATE).edit().putString(PublicUtils.PREFERENCE_NAME_PHONE, phoneno).commit();
				intentToLogin();
			}else{
				throw new Exception();
			}
		}else if("0002".equals(resultcode)){//三码验证不通过手机号是有效的
			codeNotPass();
		}else{//"0003"用户手机号是无效用户
			invalidUser();
		}
	}
	
	/**
	 * 用户三码鉴权不通过
	 */
	private void codeNotPass(){
		onceLogin=false;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(PublicUtils.getResourceString(this,R.string.tip));
		builder.setMessage(PublicUtils.getResourceString(this,R.string.login_1));
		builder.setPositiveButton(PublicUtils.getResourceString(this,R.string.Confirm), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.setCancelable(false);
		builder.create().show();
	}
	
	/**
	 * 无效用户
	 */
	private void invalidUser(){
		onceLogin=false;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(PublicUtils.getResourceString(this,R.string.tip));
		builder.setMessage(PublicUtils.getResourceString(this,R.string.tip_confirm));
		builder.setPositiveButton(PublicUtils.getResourceString(this,R.string.Confirm), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.setCancelable(false);
		builder.create().show();
	}
	
	private void intentToLogin(){
//		Intent intent = new Intent(LoginForFreeActivity.this, LoginActivity.class);
//		startActivity(intent);
//		LoginForFreeActivity.this.finish();
		SharedPreferencesUtil prefs = SharedPreferencesUtil.getInstance(getApplicationContext());

		String locVersion = SharedPreferencesUtil.getInstance(this).getLocationVersion();
		if (!TextUtils.isEmpty(locVersion) && (Integer.valueOf(this.getString(R.string.IS_NEED_INIT_VERSION)) > Integer.valueOf(locVersion))) {
			// 如果本地版本小于等于在需要重新初始化的历史本版时
			SharedPreferencesUtil.getInstance(LoginForFreeActivity.this).setIsInit(false);
		}
		// 获取当前版本号
		String curVersion = PublicUtils.getCurVersion(this);
		// 存放当前版本号到本地
		SharedPreferencesUtil.getInstance(this).setLocationVersion(curVersion);

		//如果已经初始化，则跳转到主界面，否则跳转到初始化界面
		Intent intent = new Intent(getApplicationContext(), prefs.getIsInit() ? HomePageActivity.class : InitActivity.class);
		startActivity(intent);
	}
	
	
	private RequestParams loginParams(){
		String userName = et_username.getText().toString();
		String phoneno = et_phoneno.getText().toString();
		String psw = et_password.getText().toString();
		RequestParams params = new RequestParams();
		params.put("un", userName);
		params.put("pn", phoneno);
		params.put("pwd", psw);
		JLog.d(TAG, "激活账号params:"+params.toString());
		return params;
	}
	
	private String loginUrl(){
		String url = UrlInfo.queryFreeCheckInfo(this);
		JLog.d(TAG, "激活账号url:"+url);
		return url;
	}
	
	private boolean isComplete(){
		boolean flag = true;
		String userName = et_username.getText().toString();
		String phoneno = et_phoneno.getText().toString();
		String psw = et_password.getText().toString();
		if(TextUtils.isEmpty(userName)){
			flag = false;
			Toast.makeText(this, R.string.tip_input_username, Toast.LENGTH_SHORT).show();
		}else if (TextUtils.isEmpty(phoneno)) {
			flag = false;
			Toast.makeText(this, R.string.login_hint_phone, Toast.LENGTH_SHORT).show();
		}else if(!PublicUtils.isMobileTelephone(phoneno)){
			flag = false;
			Toast.makeText(this, R.string.input_right_pho, Toast.LENGTH_SHORT).show();
		}else if(TextUtils.isEmpty(psw)){
			flag = false;
			Toast.makeText(this, R.string.tip_input_password, Toast.LENGTH_SHORT).show();
		}
		return flag;
	}
}
