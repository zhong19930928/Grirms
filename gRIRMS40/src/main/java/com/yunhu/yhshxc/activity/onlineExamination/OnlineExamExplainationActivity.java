package com.yunhu.yhshxc.activity.onlineExamination;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;

public class OnlineExamExplainationActivity extends AbsBaseActivity {
	private TextView tv_online_title;
	private TextView tv_online_explain;
	private TextView tv_online_date;
	private LinearLayout ll_online_back;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.online_exam_expalination);
		init();
	}
	private void init() {
		tv_online_title = (TextView) this.findViewById(R.id.tv_online_title);
		tv_online_explain = (TextView) this.findViewById(R.id.tv_online_explain);
		tv_online_date = (TextView) this.findViewById(R.id.tv_online_date);
		ll_online_back = (LinearLayout) this.findViewById(R.id.ll_online_back);
		ll_online_back.setOnClickListener(listener);
	}
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			finish();
		}
	};
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
