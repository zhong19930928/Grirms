package com.yunhu.yhshxc.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.utility.UrlInfo;

public class ApkVersionActivity extends AbsBaseActivity {
	private ImageView apk_version_back;
	private TextView apk_version_info;
	private TextView apk_version_check;// 版本号
	private ImageView app_icon;// 应用的图标
	private TextView app_name;// 应用的名字
	private RelativeLayout btn_service_phone;// 客服电话
	private RelativeLayout btn_function_introduction;// 功能介绍
	private Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.apk_version);
		initView();
	}

	private void initView() {

		apk_version_back = (ImageView) findViewById(R.id.apk_version_back);
		apk_version_info = (TextView) findViewById(R.id.apk_version_info);
		app_icon = (ImageView) findViewById(R.id.image_icon_blue);
		app_name = (TextView) findViewById(R.id.id_appname);
		apk_version_check = (TextView) findViewById(R.id.apk_version_check);
		btn_service_phone = (RelativeLayout) findViewById(R.id.btn_service_phone);
		btn_function_introduction = (RelativeLayout) findViewById(R.id.btn_function_introduction);
		btn_function_introduction.setVisibility(View.GONE);
		app_icon.setImageResource(R.drawable.icon_main);
		app_name.setText(getResources().getString(R.string.app_normalname));

		try { 
			String	versionName = this.getPackageManager().getPackageInfo(getResources().getString(R.string.app_package_name),
					0).versionName;
			apk_version_info.setText("V  " + versionName + "\n" + UrlInfo.getVersionInfo(context));
			apk_version_check.setText("V  " + versionName);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 返回按钮
		apk_version_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ApkVersionActivity.this.finish();

			}
		});
		// 拨打客服
		btn_service_phone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:4008188809"));
				startActivity(intent);

			}
		});
		// 功能介绍
		btn_function_introduction.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ApkVersionActivity.this, FunctionDetailActivity.class);
				startActivity(intent);

			}
		});
	}
}
