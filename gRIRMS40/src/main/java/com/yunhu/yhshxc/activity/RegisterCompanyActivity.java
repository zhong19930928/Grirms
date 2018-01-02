package com.yunhu.yhshxc.activity;

import gcg.org.debug.JLog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.loopj.android.http.RequestParams;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterCompanyActivity extends AbsBaseActivity implements OnClickListener {
	private EditText company_name;// 企业名称
	private EditText company_intro;// 企业简介
	private EditText company_phone;// 企业电话
	private EditText company_username;// 用户姓名
	private EditText company_userphone;// 手机号
	private EditText company_usermail;// 邮箱
	private Button btn_register;// 注册按钮
	private TextView company_phone_number,login_icontitle;// 客服电话
	private Context mContext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist_company);
		mContext = this;
		initView();
	}

	private void initView() {
		company_name = (EditText) findViewById(R.id.company_name);
		company_intro = (EditText) findViewById(R.id.company_intro);
		company_phone = (EditText) findViewById(R.id.company_phone);
		company_username = (EditText) findViewById(R.id.company_username);
		company_userphone = (EditText) findViewById(R.id.company_userphone);
		company_usermail = (EditText) findViewById(R.id.company_usermail);
		btn_register = (Button) findViewById(R.id.btn_register);
		company_phone_number = (TextView) findViewById(R.id.company_phone_number);
		login_icontitle = (TextView) findViewById(R.id.login_icontitle);
		login_icontitle.setText(getResources().getString(R.string.app_normalname));	
		btn_register.setOnClickListener(this);
		company_phone_number.setOnClickListener(this);
	}

	private boolean isEnable = true;

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_register:// 注册
			isEnable = false;
			submitCompanyInfo();
	
			break;
		case R.id.company_phone_number:// 拨打客服电话
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:4008188809"));
			startActivity(intent);
			break;

		default:
			break;
		}

	}

	private Dialog searchDialog;
	String companyName;
	String companyIntro;
	String companyPhone;
	String companyUserName;
	String companyUserPhone;
	String companyUserMail;


	private void submitCompanyInfo() {
		companyName = company_name.getText().toString().trim();
		companyIntro = company_intro.getText().toString().trim();
		companyPhone = company_phone.getText().toString().trim();
		companyUserName = company_username.getText().toString().trim();
		companyUserPhone = company_userphone.getText().toString().trim();
		companyUserMail = company_usermail.getText().toString().trim();
		if (TextUtils.isEmpty(companyName)) {
			Toast.makeText(mContext, R.string.login_hint_co, Toast.LENGTH_SHORT).show();
			isEnable = true;
			return;
		}
		if (TextUtils.isEmpty(companyIntro)) {
			Toast.makeText(mContext, R.string.login_hint_co1, Toast.LENGTH_SHORT).show();
			isEnable = true;
			return;
		}
		if (TextUtils.isEmpty(companyPhone)) {
			Toast.makeText(mContext, R.string.login_hint_co2, Toast.LENGTH_SHORT).show();
			isEnable = true;
			return;
		}
		if (TextUtils.isEmpty(companyUserName)) {
			Toast.makeText(mContext, R.string.login_hint_co3, Toast.LENGTH_SHORT).show();
			isEnable = true;
			return;
		}
		if (TextUtils.isEmpty(companyUserPhone)) {
			Toast.makeText(mContext, R.string.login_hint_co4, Toast.LENGTH_SHORT).show();
			isEnable = true;
			return;
		}
		if (TextUtils.isEmpty(companyUserMail)) {
			Toast.makeText(mContext, R.string.login_hint_co5, Toast.LENGTH_SHORT).show();
			isEnable = true;
			return;
		}

		// 验证手机号和邮箱是否符合格式
		String telRegex = "[1][23456789]\\d{9}";
		Pattern pattern1 = Pattern.compile(telRegex);
		Matcher mPhone = pattern1.matcher(companyUserPhone);

		if (!mPhone.matches()) {
			Toast.makeText(mContext, R.string.input_right_pho, Toast.LENGTH_SHORT).show();
			return;
		}
		String mailRegex = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";
		Pattern pattern2 = Pattern.compile(mailRegex);
		Matcher mMail = pattern2.matcher(companyUserMail);
		if (!mMail.matches()) {
			Toast.makeText(mContext, R.string.reset_email, Toast.LENGTH_SHORT).show();
			return;
		}

		// 信息格式匹配,开始联网提交
		searchDialog = new MyProgressDialog(this, R.style.CustomProgressDialog, PublicUtils.getResourceString(this,R.string.regisnting));
		String url = UrlInfo.phoneRegister(this);// 注册企业的接口
		RequestParams param = searchParams();
		GcgHttpClient.getInstance(this.getApplicationContext()).post(url, param, new HttpResponseListener() {

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				searchDialog.show();
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				if (searchDialog != null && searchDialog.isShowing()) {
					searchDialog.dismiss();
				}
				isEnable = true;
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				JLog.d(TAG, "onSuccess:" + content);

				JSONObject obj;
				try {
					obj = new JSONObject(content);
					String resultCode = obj.getString("resultcode");
					if ("0000".equals(resultCode)) {
						// Toast.makeText(RegisterCompanyActivity.this,
						// "注册后请登录所填邮箱查收邮件并进行激活,", Toast.LENGTH_SHORT).show();
						// 弹出提示框,告知用户要去查收邮件
						showDialogTip();

//						RegisterCompanyActivity.this.finish();
					} else if ("0001".equals(resultCode)) {
						Toast.makeText(RegisterCompanyActivity.this, R.string.regisnting1, Toast.LENGTH_SHORT).show();
					} else if ("0002".equals(resultCode)) {
						Toast.makeText(RegisterCompanyActivity.this, R.string.regisnting2, Toast.LENGTH_SHORT).show();
					} else if ("0003".equals(resultCode)) {
						Toast.makeText(RegisterCompanyActivity.this, R.string.regisnting3, Toast.LENGTH_SHORT).show();
					} else if ("0004".equals(resultCode)) {
						Toast.makeText(RegisterCompanyActivity.this, R.string.regisnting4, Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					ToastOrder.makeText(getApplicationContext(), R.string.ERROR_DATA, ToastOrder.LENGTH_SHORT).show();
				}

			}

			@Override
			public void onFailure(Throwable error, String content) {
				JLog.d(TAG, "onFailure:" + content);
				ToastOrder.makeText(getApplicationContext(), R.string.retry_net_exception, ToastOrder.LENGTH_SHORT).show();

			}
		});

	}

	private void showDialogTip() {
		View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_new_view, null);
		Button btn1 = (Button) view.findViewById(R.id.dialog_new_confirm);
		btn1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//跳转到登录界面
				RegisterCompanyActivity.this.startActivity(new Intent(RegisterCompanyActivity.this,LogInNewActivity.class));
				RegisterCompanyActivity.this.finish();
			}
		});
     AlertDialog dialog = new AlertDialog.Builder(mContext,R.style.NewAlertDialogStyle2)
    		 .setView(view)
    		 .create();
     dialog.setCancelable(false);
     dialog.show();
		
		
	}

	/**
	 * 提交参数
	 * 
	 * @returnString companyName; String companyIntro; String companyUserName;
	 *               String companyUserPhone; String companyUserMail;
	 */
	private RequestParams searchParams() {
		RequestParams param = new RequestParams();
		JSONObject obj = new JSONObject();
		try {
			obj.put("comName", companyName);// 公司名称
			obj.put("comJc", companyIntro);// 公司简介
			obj.put("userName", companyUserName);// 用户姓名
			obj.put("phoneNo", companyUserPhone);// 手机号
			obj.put("email", companyUserMail);// 邮箱地址
			obj.put("keyCom", PublicUtils.COMBINE_TYPE);//版本类别:
			obj.put("phoneNo2", companyPhone);//客服联系电话

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		param.put("info", obj.toString());

		return param;
	}
}
