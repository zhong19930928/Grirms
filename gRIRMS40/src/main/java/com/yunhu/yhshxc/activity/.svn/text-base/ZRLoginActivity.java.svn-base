package com.yunhu.yhshxc.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.core.ApiUrl;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.style.StatusBarUtil;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class ZRLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText newlogin_userphone;
    private EditText newlogin_userpwd;
    private Button zr_login_btn_code;
    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StatusBarUtil.setColor(this, Color.WHITE);
        }
        setContentView(R.layout.activity_zrlogin);
        initView();
    }

    private void initView() {
        newlogin_userphone = (EditText) findViewById(R.id.newlogin_userphone);
        newlogin_userpwd = (EditText) findViewById(R.id.newlogin_userpwd);
        zr_login_btn_code = (Button) findViewById(R.id.zr_login_btn_code);
        btn_login = (Button) findViewById(R.id.btn_login);

        zr_login_btn_code.setOnClickListener(this);
        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zr_login_btn_code://获取验证码
                zr_login_btn_code.setClickable(false);
                getCode();

                break;
            case R.id.btn_login://登录
                login();
                break;
        }
    }

    //登录
    private void login() {
        final String phone = newlogin_userphone.getText().toString().trim();
        String code = newlogin_userpwd.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(code)) {
            Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        //进行校验
        String url = PublicUtils.getBaseUrl(this) + "doSMSLoginCheckInfo.do";
        String time = DateUtil.dateToDateString(Calendar.getInstance().getTime());
        RequestParams params = new RequestParams();
        params.put("phoneno", phone);
        params.put("smsCode", code);
        params.put("time", time);
        GcgHttpClient.getInstance(this).get(url, params, new HttpResponseListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onSuccess(int statusCode, String content) {
                if (!TextUtils.isEmpty(content)) {
                    try {
                        JSONObject jsonObject = new JSONObject(content);
                        if (jsonObject.has("resultcode")) {
                            String resultCode = jsonObject.getString("resultcode");
                            if ("0000".equals(resultCode)) {
                                Toast.makeText(ZRLoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                //存储手机号进入加载页
                                ZRLoginActivity.this.getSharedPreferences(PublicUtils.PREFERENCE_NAME, Context.MODE_PRIVATE).edit()
                                        .putString(PublicUtils.PREFERENCE_NAME_PHONE, phone).apply();
                                intentToLogin();

                            } else if ("0001".equals(resultCode)) {
                                Toast.makeText(ZRLoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                            } else if ("0002".equals(resultCode)) {
                                Toast.makeText(ZRLoginActivity.this, "该手机号未注册", Toast.LENGTH_SHORT).show();
                            } else if ("0006".equals(resultCode)) {
                                Toast.makeText(ZRLoginActivity.this, "验证码输入错误", Toast.LENGTH_SHORT).show();
                            } else if ("0007".equals(resultCode)) {
                                Toast.makeText(ZRLoginActivity.this, "验证码输入超时", Toast.LENGTH_SHORT).show();
                            } else if ("0009".equals(resultCode)) {
                                Toast.makeText(ZRLoginActivity.this, "账号被使用,请重新登录", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ZRLoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                            }
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ZRLoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ZRLoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Throwable error, String content) {
                Toast.makeText(ZRLoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
            }
        });


    }

    //根据手机号获取验证码
    private void getCode() {
        String phone = newlogin_userphone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, PublicUtils.getResourceString(this, R.string.login_hint_phone), Toast.LENGTH_SHORT).show();
            return;
        }
        String url = PublicUtils.getBaseUrl(this) + "doSMSLoginCreateInfo.do";
        RequestParams params = new RequestParams();
        params.put("phoneno", phone);
        GcgHttpClient.getInstance(this).get(url, params, new HttpResponseListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onSuccess(int statusCode, String content) {
                if (!TextUtils.isEmpty(content)) {
                    try {
                        JSONObject jsonObject = new JSONObject(content);
                        if (jsonObject.has("resultcode")) {

                            if ("0000".equals(jsonObject.getString("resultcode"))) {
                                Toast.makeText(ZRLoginActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                                //进入60秒计时
                                zr_login_btn_code.setText("重新获取(60s)");
                                beginTimeTask();

                            } else {
                                Toast.makeText(ZRLoginActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                            }
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ZRLoginActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Throwable error, String content) {
                Toast.makeText(ZRLoginActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //60秒倒计时
    private void beginTimeTask() {
        handler.sendEmptyMessageDelayed(1, 1000);
    }

    private int time = 60;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (time <= 0) {
                        restCodeBtn();
                        break;
                    }
                    time--;
                    zr_login_btn_code.setText("重新获取(" + time + "s)");
                    handler.sendEmptyMessageDelayed(1, 1000);
                    break;

            }
        }
    };

    private void restCodeBtn() {
        zr_login_btn_code.setText("获取验证码");
        zr_login_btn_code.setClickable(true);
        time=60;
    }

    private void intentToLogin() {
        SharedPreferencesUtil prefs = SharedPreferencesUtil.getInstance(getApplicationContext());
        //如果已经初始化，则跳转到主界面，否则跳转到初始化界面
        Intent intent = new Intent(getApplicationContext(), prefs.getIsInit() ? HomePageActivity.class : InitActivity.class);
        startActivity(intent);
        finish();
    }

}
