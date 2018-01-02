package com.yunhu.yhshxc.kt40;

import java.util.TimerTask;

import com.yunhu.yhshxc.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class KT40ScanActivity extends Activity implements OnClickListener{
	private TextView textDecode;

	//接受广播
	private String RECE_DATA_ACTION = "com.se4500.onDecodeComplete";
	//调用扫描广播
	private String START_SCAN_ACTION = "com.geomobile.se4500barcode";
	
	private String STOP_SCAN="com.geomobile.se4500barcode.poweroff";
	
	private Button buttonscan;

	
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		public void onReceive(android.content.Context context,
				android.content.Intent intent) {
			String action = intent.getAction();
			if (!TextUtils.isEmpty(action)&&action.equals(RECE_DATA_ACTION)) {
				String data = intent.getStringExtra("se4500");
			 Intent i = KT40ScanActivity.this.getIntent();

			 textDecode.setText(data);

			 i.putExtra("SCAN_RESULT",data);
			 KT40ScanActivity.this.setResult(R.id.scan_succeeded,i);
			 KT40ScanActivity.this.finish();
			
			}
		}

	};
      
	

	
	public class MyTask extends TimerTask {
		@Override
		public void run() {
			startScan();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kt40_scaning);
		buttonscan = (Button) findViewById(R.id.buttonDec);
		textDecode = (TextView) findViewById(R.id.textDecode);
		buttonscan.setOnClickListener(this);



		IntentFilter iFilter = new IntentFilter();
		
		//注册系统广播  接受扫描到的数据
		iFilter.addAction(RECE_DATA_ACTION);
		registerReceiver(receiver, iFilter);
	}



	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Intent intent =new Intent();
		intent.setAction(STOP_SCAN);
		sendBroadcast(intent);
		unregisterReceiver(receiver);
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub

		super.onPause();
	
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
	     startScan();
		super.onResume();
	}



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		
		case R.id.buttonDec:
			startScan();
			break;
		default:
			break;
		}
	}

	/**
	 * 发送广播  调用系统扫描
	 */
	private void startScan() {
		Intent intent = new Intent();
		intent.setAction(START_SCAN_ACTION);
		sendBroadcast(intent, null);
	}
	
}
