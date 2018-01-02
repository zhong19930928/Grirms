package com.yunhu.yhshxc.activity;

import org.json.JSONException;
import org.json.JSONObject;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.login.LoginActivity;
import com.yunhu.yhshxc.dialog.DialogLawActivity;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.parser.CacheData;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.loopj.android.http.RequestParams;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import gcg.org.debug.JLog;

public class LogInNewActivity extends AbsBaseActivity implements OnClickListener {
	
	private EditText newlogin_username;// 用户名
	private EditText newlogin_userphone;// 用户手机号
	private EditText newlogin_userpwd;// 用户密码
	private Button btn_login;// 登录
	private ImageView newlogin_agreement_selectimg;// 同意协议的勾选
	private TextView newlogin_agreement_text;// 协议展示按钮
	private boolean isAcceptAgreement = false;// 是否同意协议
	private String TAG = "LogInNewActivity";
    private TextView login_icontitle;
    private String tips="";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newlogin_layout);
		tips =PublicUtils.getResourceString(LogInNewActivity.this,R.string.app_normalname);
		initView();

	}

	private void initView() {
		newlogin_username = (EditText) findViewById(R.id.newlogin_username);
		newlogin_userphone = (EditText) findViewById(R.id.newlogin_userphone);
		newlogin_userpwd = (EditText) findViewById(R.id.newlogin_userpwd);
		btn_login = (Button) findViewById(R.id.btn_login);
		newlogin_agreement_selectimg = (ImageView) findViewById(R.id.newlogin_agreement_selectimg);
		newlogin_agreement_text = (TextView) findViewById(R.id.newlogin_agreement_text);
		login_icontitle = (TextView) findViewById(R.id.login_icontitle);
		btn_login.setOnClickListener(this);
		newlogin_agreement_selectimg.setOnClickListener(this);
		newlogin_agreement_text.setOnClickListener(this);
		isAcceptAgreement = true;//默认为选中
		newlogin_agreement_selectimg.setImageResource(R.drawable.selected1_icon);
			login_icontitle.setText(getResources().getString(R.string.app_normalname));
		    tips = getResources().getString(R.string.app_normalname);
		newlogin_agreement_text.setText(PublicUtils.getResourceString(this,R.string.already_agree)+"<<"+tips+PublicUtils.getResourceString(this,R.string.already_agree1)+">>");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			if (isAcceptAgreement) {
				login();
			} else {
				Toast.makeText(this, PublicUtils.getResourceString(this,R.string.already_agree2)+tips+PublicUtils.getResourceString(this,R.string.already_agree1), Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.newlogin_agreement_selectimg:
			isAcceptAgreement = !isAcceptAgreement;
			if (isAcceptAgreement) {
				newlogin_agreement_selectimg.setImageResource(R.drawable.selected1_icon);
			} else {
				newlogin_agreement_selectimg.setImageResource(R.drawable.select1_icon);
			}
			break;
		case R.id.newlogin_agreement_text:
			// 提示法律条文
			Intent intent = new Intent(this, DialogLawActivity.class);
			startActivity(intent);

			break;

		default:
			break;
		}

	}

	private void login() {
		if (isComplete()) {
			final Dialog dialog = new MyProgressDialog(this, R.style.CustomProgressDialog, PublicUtils.getResourceString(this,R.string.acitvation));
			GcgHttpClient.getInstance(this, GcgHttpClient.TIME_OUT_SHORT).post(loginUrl(), loginParams(),
					new HttpResponseListener() {

						@Override
						public void onSuccess(int statusCode, String content) {
							try {
								loginSuccess(content);
							} catch (Exception e) {
								try {
									JSONObject jsb = new JSONObject(content);
									if (jsb.has("resultcode")) {
										Toast.makeText(LogInNewActivity.this, PublicUtils.getResourceString(LogInNewActivity.this,R.string.tip_confirm),
												Toast.LENGTH_SHORT).show();
									} else {
									
									}

								} catch (JSONException e1) {
									Toast.makeText(LogInNewActivity.this, PublicUtils.getResourceString(LogInNewActivity.this,R.string.bad_network), Toast.LENGTH_SHORT).show();
									e1.printStackTrace();
								}

							}
						}

						@Override
						public void onStart() {
							if (dialog != null && !dialog.isShowing()) {
								dialog.show();
							}
						}

						@Override
						public void onFinish() {
							if (dialog != null && dialog.isShowing()) {
								dialog.dismiss();
							}
						}

						@Override
						public void onFailure(Throwable error, String content) {
							Toast.makeText(LogInNewActivity.this, PublicUtils.getResourceString(LogInNewActivity.this,R.string.re_net_error), Toast.LENGTH_SHORT).show();

						}
					});
		}
	}

	private void loginSuccess(String json) throws Exception {
		JLog.d(TAG, "version==>" + json);
		JSONObject jsonObject = new JSONObject(json);
		String resultcode = jsonObject.getString("resultcode");
		if ("0000".equals(resultcode)) {
			if (jsonObject.has("version")) {
				JSONObject versionObj = jsonObject.getJSONObject("version");
				// 服务端最新APK安装包的MD5
				if (versionObj.has("md5code")) {
					String md5code = versionObj.getString("md5code");
					SharedPreferencesUtil.getInstance(getApplicationContext()).saveMD5Code(md5code);
				}
				// 服务端最新APK安装包的下载URL
				if (versionObj.has("url")) {
					String url = versionObj.getString("url");
					SharedPreferencesUtil.getInstance(getApplicationContext()).saveDownUrl(url);
				}
				// 服务端最新APK安装包的版本
				if (versionObj.has("version")) {
					String version = versionObj.getString("version");
					SharedPreferencesUtil.getInstance(getApplicationContext()).saveNewVersion(version);
				}
				// 服务端最新APK安装包的大小 KB为单位
				if (versionObj.has("size")) {
					String size = versionObj.getString("size");
					SharedPreferencesUtil.getInstance(getApplicationContext()).saveApkSize(size);
				}
				// 用户的有效期的开始日期
				if (versionObj.has(CacheData.AVAIL_START)) {
					SharedPreferencesUtil.getInstance(getApplicationContext())
							.setUsefulStartDate(versionObj.getString(CacheData.AVAIL_START));
				}
				// 用户的有效期的结束日期
				if (versionObj.has(CacheData.AVAIL_END)) {
					SharedPreferencesUtil.getInstance(getApplicationContext())
							.setUsefulEndDate(versionObj.getString(CacheData.AVAIL_END));
				}
				// 存储用户手机号码
				String phoneno = newlogin_userphone.getText().toString();
				getApplicationContext().getSharedPreferences(PublicUtils.PREFERENCE_NAME, Context.MODE_PRIVATE).edit()
						.putString(PublicUtils.PREFERENCE_NAME_PHONE, phoneno).commit();
				intentToLogin();
			} else {
				throw new Exception();
			}
		} else if ("0002".equals(resultcode)) {// 三码验证不通过手机号是有效的
			codeNotPass();
		} else {// "0003"用户手机号是无效用户
			if (jsonObject.has("resultcode")) {

				invalidUser();
			} else {
				Toast.makeText(LogInNewActivity.this, PublicUtils.getResourceString(this,R.string.bad_network), Toast.LENGTH_SHORT).show();
			}
		}
	}

	/**
	 * 用户三码鉴权不通过
	 */
	private void codeNotPass() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(PublicUtils.getResourceString(this,R.string.tip));
		builder.setMessage(PublicUtils.getResourceString(this,R.string.changed_pho));
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
	private void invalidUser() {

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

	private void intentToLogin() {
//		Intent intent = new Intent(LogInNewActivity.this, LoginActivity.class);
//		intent.putExtra("isResetPass", true);
//		startActivity(intent);
//		LogInNewActivity.this.finish();
		SharedPreferencesUtil prefs = SharedPreferencesUtil.getInstance(getApplicationContext());

		String locVersion = SharedPreferencesUtil.getInstance(this).getLocationVersion();
		if (!TextUtils.isEmpty(locVersion) && (Integer.valueOf(this.getString(R.string.IS_NEED_INIT_VERSION)) > Integer.valueOf(locVersion))) {
			// 如果本地版本小于等于在需要重新初始化的历史本版时
			SharedPreferencesUtil.getInstance(LogInNewActivity.this).setIsInit(false);
		}
		// 获取当前版本号
		String curVersion = PublicUtils.getCurVersion(this);
		// 存放当前版本号到本地
		SharedPreferencesUtil.getInstance(this).setLocationVersion(curVersion);

		//如果已经初始化，则跳转到主界面，否则跳转到初始化界面
		Intent intent = new Intent(getApplicationContext(), prefs.getIsInit() ? HomePageActivity.class : InitActivity.class);
		startActivity(intent);
		LogInNewActivity.this.finish();
	}

	private RequestParams loginParams() {
		String userName = newlogin_username.getText().toString();
		String phoneno = newlogin_userphone.getText().toString();
		String psw = newlogin_userpwd.getText().toString();
		RequestParams params = new RequestParams();
		params.put("un", userName);
		params.put("pn", phoneno);
		params.put("pwd", psw);
		JLog.d(TAG, "激活账号params:" + params.toString());
		return params;
	}

	private String loginUrl() {
		String url = UrlInfo.queryFreeCheckInfo(this);
		JLog.d(TAG, "激活账号url:" + url);
		return url;
	}

	private boolean isComplete() {
		boolean flag = true;
		String userName = newlogin_username.getText().toString();
		String phoneno = newlogin_userphone.getText().toString();
		String psw = newlogin_userpwd.getText().toString();
		if (TextUtils.isEmpty(userName)) {
			flag = false;
			Toast.makeText(this, PublicUtils.getResourceString(this,R.string.tip_input_username), Toast.LENGTH_SHORT).show();
		} else if (TextUtils.isEmpty(phoneno)) {
			flag = false;
			Toast.makeText(this, PublicUtils.getResourceString(this,R.string.login_hint_phone), Toast.LENGTH_SHORT).show();
		} else if (!PublicUtils.isMobileTelephone(phoneno)) {
			flag = false;
			Toast.makeText(this, PublicUtils.getResourceString(this,R.string.input_right_pho), Toast.LENGTH_SHORT).show();
		} else if (TextUtils.isEmpty(psw)) {
			flag = false;
			Toast.makeText(this,PublicUtils.getResourceString(this,R.string.tip_input_password) , Toast.LENGTH_SHORT).show();
		}
		return flag;
	}
}
