package com.yunhu.yhshxc.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.carSales.util.SharedPreferencesForCarSalesUtil;
import com.yunhu.yhshxc.attendance.SharedPrefsAttendanceUtil;
import com.yunhu.yhshxc.attendance.util.SharedPreferencesForLeaveUtil;
import com.yunhu.yhshxc.customMade.xiaoyuan.SharedPreferencesUtilForXiaoYuan;
import com.yunhu.yhshxc.database.DatabaseHelper;
import com.yunhu.yhshxc.order2.SharedPreferencesForOrder2Util;
import com.yunhu.yhshxc.order3.util.SharedPreferencesForOrder3Util;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil2;
import com.yunhu.yhshxc.utility.SharedPreferencesUtilForNearby;
import com.yunhu.yhshxc.utility.SharedPreferencesUtilForTable;
import com.yunhu.yhshxc.wechat.Util.SharedPrefrencesForWechatUtil;

import cn.jpush.android.api.JPushInterface;

public class SplashNewActivity extends AbsBaseActivity implements OnClickListener {
    private Button logIn;// 登录
    private Button experience;// 体验
    private Button register;// 注册新企业
    private boolean isFirst = true;
    private TextView splash_title1, splash_center_title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        if (!PublicUtils.ISCOMBINE) {
            startActivity(new Intent(this, SplashActivity2.class));
            this.finish();
        }
        //如果已经登录过或体验过,则不再显示此页面
        String phoneno = getApplicationContext().getSharedPreferences(PublicUtils.PREFERENCE_NAME, Context.MODE_PRIVATE).getString(PublicUtils.PREFERENCE_NAME_PHONE, "");
        if (!TextUtils.isEmpty(phoneno) && !checkPhone(phoneno)) {
            startActivity(new Intent(this, SplashActivity2.class));
            finish();
        }

        setContentView(R.layout.activity_enter_layout);
        logIn = (Button) findViewById(R.id.btn_enter_login);
        experience = (Button) findViewById(R.id.btn_experience);
        register = (Button) findViewById(R.id.btn_register);
        splash_title1 = (TextView) findViewById(R.id.splash_title1);
        splash_center_title = (TextView) findViewById(R.id.splash_center_title);
        splash_title1.setText(getResources().getString(R.string.app_normalname));
        splash_center_title.setText(getResources().getString(R.string.init_intro));
        logIn.setOnClickListener(this);
        experience.setOnClickListener(this);
        register.setOnClickListener(this);
    }
    private boolean checkPhone(String phone){
        Boolean flag = false;
        for (String s:PublicUtils.phoneArr
             ) {
            if (s.equals(phone)){
                flag = true;
                break;
            }
        }
        return flag;

    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_enter_login:// 登录
                if (isFirst) {
                    intentToLogIn();
                    isFirst = false;
                }
                break;
            case R.id.btn_experience:// 体验
                if (isFirst) {
                    isFirst = false;
                    intentToExperience();
                }
                break;
            case R.id.btn_register:// 注册新企业
                intentToRegister();
                break;

            default:
                break;
        }
    }

    //企业注册
    private void intentToRegister() {
        clearAllDate();
        startActivity(new Intent(this, RegistExplainActivity.class));
    }

    // 体验Demo版
    private void intentToExperience() {
        clearAllDate();
        PublicUtils.ISDEMO = true;
        startActivity(new Intent(this, SplashActivity2.class));
        this.finish();

    }

    //登录界面
    private void intentToLogIn() {
        clearAllDate();
        PublicUtils.ISDEMO = false;
        startActivity(new Intent(this, SplashActivity2.class));
        this.finish();
    }


    //清除可能进入DemoApp留下的表的数据
    private void clearAllDate() {

        try {
            this.getSharedPreferences(PublicUtils.PREFERENCE_NAME, Context.MODE_PRIVATE).edit().clear().commit();
            SharedPreferencesUtil.getInstance(this).clearAll();
            SharedPreferencesUtil2.getInstance(this).clearAll();
            SharedPreferencesUtilForNearby.getInstance(this).clearAll();
            SharedPreferencesUtilForTable.getInstance(this).clearAll();
            SharedPreferencesUtilForXiaoYuan.getInstance(this).clearAll();
            SharedPreferencesForCarSalesUtil.getInstance(this).clearAll();
            SharedPreferencesForLeaveUtil.getInstance(this).clearAll();
            SharedPrefrencesForWechatUtil.getInstance(this).clearAll();
            SharedPreferencesForOrder2Util.getInstance(this).clearAll();
            SharedPreferencesForOrder3Util.getInstance(this).clearAll();
            SharedPrefsAttendanceUtil.getInstance(this).clearAll();
            DatabaseHelper.getInstance(this).deteleAllTable();

//		startActivity(new Intent(context,LoginForFreeActivity.class));
//     	String cachePath = getActivity().getApplicationContext().getCacheDir().toString();
//     	
//     	String path=cachePath.substring(0, cachePath.lastIndexOf("/"));
//     	FileHelper.deleteAllFile(path+"/cache");//清除缓存文件
//     	FileHelper.deleteAllFile(path+"/shared_prefs");//清除配置文件
//     	FileHelper.deleteAllFile(path+"/databases");//清除数据库文件
//     	FileHelper.deleteAllFile(Constants.SDCARD_PATH);//清除外部存储数据

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        JPushInterface.onResume(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        JPushInterface.onPause(this);
        super.onPause();
    }


}
