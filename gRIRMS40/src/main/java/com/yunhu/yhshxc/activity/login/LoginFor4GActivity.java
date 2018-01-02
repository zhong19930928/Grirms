package com.yunhu.yhshxc.activity.login;

import gcg.org.debug.JLog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.activity.HomePageActivity;
import com.yunhu.yhshxc.activity.InitActivity;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.widget.ToastOrder;

public class LoginFor4GActivity extends AbsBaseActivity {
	
	private EditText et_login_4g_phoneno,et_login_4g_code;
	private Button btn_login_4g_msg,btn_login_4g_msg_code;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_4g_activity);
		et_login_4g_phoneno = (EditText)findViewById(R.id.et_login_4g_phoneno);
		et_login_4g_code = (EditText)findViewById(R.id.et_login_4g_code);
		btn_login_4g_msg = (Button)findViewById(R.id.btn_login_4g_msg);
		btn_login_4g_msg_code = (Button)findViewById(R.id.btn_login_4g_msg_code);
		btn_login_4g_msg.setOnClickListener(listener);
		btn_login_4g_msg_code.setOnClickListener(listener);
	}
	
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_login_4g_msg:
				sendMsg();
				break;
			case R.id.btn_login_4g_msg_code:
				checkCode();
				break;

			default:
				break;
			}
		}
	};
	
	private void sendMsg(){
		String phoneno = et_login_4g_phoneno.getText().toString();
		if(checkPhoneno(phoneno)){
			final Dialog dialog = new MyProgressDialog(this,R.style.CustomProgressDialog,PublicUtils.getResourceString(LoginFor4GActivity.this,R.string.input_password10));
//			String url = "http://bnet.gcgchina.com/opsys/auth/index.php?action=getveri&MDN="+phoneno+"&DID="+PublicUtils.getSubscriberId(this);
			String url = "http://bnet.gcgchina.com/opsys/auth/index.php?action=getveri&MDN="+phoneno+"&DID="+PublicUtils.getIMSI(this);
			JLog.d(TAG, "获取验证码url："+url);
			GcgHttpClient.getInstance(this,GcgHttpClient.TIME_OUT_SHORT).get(url, null, new HttpResponseListener(){
				@Override
				public void onStart() {
					dialog.show();
				}
				
				@Override
				public void onSuccess(int statusCode, String content) {
					JLog.d(TAG, "获取验证码content："+content);
					if ("200".equals(content)) {
						ToastOrder.makeText(LoginFor4GActivity.this, R.string.input_password11, ToastOrder.LENGTH_SHORT).show();
					}else{
						ToastOrder.makeText(LoginFor4GActivity.this, R.string.request_failure_checknet, ToastOrder.LENGTH_SHORT).show();
					}
				}
				
				@Override
				public void onFailure(Throwable error, String content) {
					ToastOrder.makeText(LoginFor4GActivity.this, R.string.request_failure_checknet, ToastOrder.LENGTH_SHORT).show();
				}
				
				
				@Override
				public void onFinish() {
					dialog.dismiss();
				}
				
			});
		}
	}
	
	public boolean checkPhoneno(String phoneno){
		if (TextUtils.isEmpty(phoneno)) {
			ToastOrder.makeText(this, R.string.input_password9, ToastOrder.LENGTH_SHORT).show();
			return false;
		}else if (!PublicUtils.isMobileTelephone(phoneno)) {
			ToastOrder.makeText(this, R.string.input_right_pho, ToastOrder.LENGTH_SHORT).show();
			return false;
		}else{
			return true;
		}
	}
	
	private void checkCode(){
		final String phoneno = et_login_4g_phoneno.getText().toString();
		if (checkPhoneno(phoneno)) {
			String code = et_login_4g_code.getText().toString();
			if (TextUtils.isEmpty(code)) {
				ToastOrder.makeText(this, R.string.input_password7, ToastOrder.LENGTH_SHORT).show();
				return;
			}else{
				final Dialog dialog = new MyProgressDialog(this,R.style.CustomProgressDialog,PublicUtils.getResourceString(LoginFor4GActivity.this,R.string.acitvation));
				String url = "http://bnet.gcgchina.com/opsys/auth/index.php?action=checkveri&MDN="+phoneno+"&DID="+PublicUtils.getSubscriberId(this)+"&VERI="+code;
				JLog.d(TAG, "激活账号url："+url);
				GcgHttpClient.getInstance(this,GcgHttpClient.TIME_OUT_SHORT).get(url, null, new HttpResponseListener(){
					@Override
					public void onStart() {
						dialog.show();
					}
					
					@Override
					public void onSuccess(int statusCode, String content) {
						JLog.d(TAG, "激活账号content："+content);
						if ("200".equals(content)) {
							getApplicationContext().getSharedPreferences(PublicUtils.PREFERENCE_NAME, Context.MODE_PRIVATE).edit().putString(PublicUtils.PREFERENCE_NAME_PHONE, phoneno).commit();
//							Intent intent = new Intent(LoginFor4GActivity.this, LoginActivity.class);
//							startActivity(intent);
//							LoginFor4GActivity.this.finish();
							SharedPreferencesUtil prefs = SharedPreferencesUtil.getInstance(getApplicationContext());

							String locVersion = SharedPreferencesUtil.getInstance(LoginFor4GActivity.this).getLocationVersion();
							if (!TextUtils.isEmpty(locVersion) && (Integer.valueOf(LoginFor4GActivity.this.getString(R.string.IS_NEED_INIT_VERSION)) > Integer.valueOf(locVersion))) {
								// 如果本地版本小于等于在需要重新初始化的历史本版时
								SharedPreferencesUtil.getInstance(LoginFor4GActivity.this).setIsInit(false);
							}
							// 获取当前版本号
							String curVersion = PublicUtils.getCurVersion(LoginFor4GActivity.this);
							// 存放当前版本号到本地
							SharedPreferencesUtil.getInstance(LoginFor4GActivity.this).setLocationVersion(curVersion);

							//如果已经初始化，则跳转到主界面，否则跳转到初始化界面
							Intent intent = new Intent(getApplicationContext(), prefs.getIsInit() ? HomePageActivity.class : InitActivity.class);
							startActivity(intent);
						}else{
							ToastOrder.makeText(LoginFor4GActivity.this, R.string.input_password8, ToastOrder.LENGTH_SHORT).show();
						}
					}
					
					@Override
					public void onFailure(Throwable error, String content) {
						ToastOrder.makeText(LoginFor4GActivity.this, R.string.input_password8, ToastOrder.LENGTH_SHORT).show();
					}
					
					
					@Override
					public void onFinish() {
						dialog.dismiss();
					}
					
				});
			}
		}
	}
	
}
