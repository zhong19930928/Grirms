package com.yunhu.yhshxc.s200;

import com.yunhu.yhshxc.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import tf.test.SerialPort;
import tf.test.SpecialKey;


public class ScanS200Activity extends Activity {
	private TextView scans200_tip;
	private TextView scans200_resultcode;
	private Button scans200_rescan;
	private SerialPort mSerialPort = new SerialPort();
	private SpecialKey mSpecialKey = new SpecialKey();
	private int fdUart = -1;
	private Context mContext;
	private static final String TAG = "ScanS200Activity";
	private static final String ACTION_SPECIALKEY = "tf.test.SpecialKeyPressed";
	private static final String SCANNER_DEV = "/dev/ttyMT0";
	private static final String ACTION_SCANNER = "tf.test.ScannerData";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scans200);
		scans200_tip = (TextView) findViewById(R.id.scans200_tip);
		scans200_resultcode = (TextView) findViewById(R.id.scans200_resultcode);
		scans200_rescan = (Button) findViewById(R.id.scans200_rescan);
		scans200_rescan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ScanS200Activity.this.finish();
				
			}
		});
		mContext = this;
		mSpecialKey.startListenthread();
		mSpecialKey.SendContext(mContext);
		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_SPECIALKEY);
		mContext.registerReceiver(keyPressReceiver, filter);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mSpecialKey.stopListenthread();
		mContext.unregisterReceiver(keyPressReceiver);
	}
   
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}
	private void SerialPortClose() {
		Log.i(TAG, "SerialPortClose");
		try {
			SerialPort.close(fdUart);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(ex.getMessage());
		}
		fdUart = -1;
	}

	private void SerialPortOpen() {
		Log.i(TAG, "SerialPortOpen");
		try {
			fdUart = SerialPort.open(SCANNER_DEV);
			if (fdUart <= 0) {
				Log.e(TAG, "SerialPortOpen open Uart fail!");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(ex.getMessage());
		}
	}

	private BroadcastReceiver keyPressReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {

//			Log.e("XXXXXX",
//					"ScankeyListenerService onReceive intent =++++++++++++++++++++++++++++++" + (intent.getAction()));
			if (ACTION_SPECIALKEY.equals(intent.getAction())) {
				SerialPortOpen();

				if (fdUart <= 0) {
					Log.e(TAG, "onReceive Uart not open");
					return;
				}

				/* scan data */
				String str_read = "";
				try {
					str_read = SerialPort.receiveData(fdUart);
                    if (!TextUtils.isEmpty(str_read)) {
                    	Toast.makeText(mContext, R.string.toast_string5, Toast.LENGTH_SHORT).show();
//   					 Intent i = ScanS200Activity.this.getIntent();
                    	scans200_resultcode.setText(str_read);
   					 Intent i = new Intent();
   					 i.putExtra("SCAN_RESULT",str_read);
   					 ScanS200Activity.this.setResult(R.id.scan_succeeded,i);
   					 ScanS200Activity.this.finish();
   					 SerialPortClose();
					}else{
						Toast.makeText(mContext, R.string.toast_string4, Toast.LENGTH_SHORT).show();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					System.out.println(ex.getMessage());
				}

				
				
				

			}

		
			
		}
	};

}
