package com.yunhu.yhshxc.activity;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.utility.PublicUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 注册说明
 *
 */
public class RegistExplainActivity extends AbsBaseActivity implements OnClickListener {

	private ImageView registBack;// 返回
	private TextView registToRegist,text1,text2,text3;// 注册新企业

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist_explain);
		registBack = (ImageView) findViewById(R.id.regist_explain_back);
		registToRegist = (TextView) findViewById(R.id.regist_explain_toregist);
		text1 = (TextView) findViewById(R.id.regist_explain_text1);
		text2 = (TextView) findViewById(R.id.regist_explain_text2);
		text3 = (TextView) findViewById(R.id.regist_explain_text3);
		String tips = getResources().getString(R.string.app_normalname);
		text1.setText(PublicUtils.getResourceString(this,R.string.welcom)+tips+"!");
		text2.setText("       "+tips+PublicUtils.getResourceString(this,R.string.welcom_info));
		text3.setText("       "+tips+PublicUtils.getResourceString(this,R.string.welcom_info1));
		
		registBack.setOnClickListener(this);
		registToRegist.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.regist_explain_back:
			this.finish();
			break;
		case R.id.regist_explain_toregist:
			startActivity(new Intent(this, RegisterCompanyActivity.class));
			this.finish();
			break;

		default:
			break;
		}
 	}

}
