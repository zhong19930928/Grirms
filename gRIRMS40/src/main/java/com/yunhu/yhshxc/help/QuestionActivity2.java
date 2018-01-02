package com.yunhu.yhshxc.help;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.activity.LogInNewActivity;

public class QuestionActivity2 extends AbsBaseActivity {
	private TextView tv_reset_password;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment_questions);
		tv_reset_password  = (TextView) findViewById(R.id.tv_reset_password);
		tv_reset_password.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(QuestionActivity2.this,LogInNewActivity.class);
				startActivity(intent);
				QuestionActivity2.this.finish();
			}
		});
		findViewById(R.id.regist_explain_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				QuestionActivity2.this.finish();
				
			}
		});
	}
	/**
	 * 用户按下返回键时的事件处理（也可以通过覆盖Activity.onBackPressed()方法来响应返回键的事件）
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) { // 点击返回按钮
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
