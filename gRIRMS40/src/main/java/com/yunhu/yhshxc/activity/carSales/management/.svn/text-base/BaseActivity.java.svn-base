package com.yunhu.yhshxc.activity.carSales.management;

import com.yunhu.yhshxc.utility.PhoneModelManager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import tf.test.SerialPort;
import tf.test.SpecialKey;

public class BaseActivity extends Activity {

	protected SerialPort mSerialPort;
	protected SpecialKey mSpecialKey;
	protected int fdUart = -1;
	protected Context mContext;

	protected static final String TAG = "ScankeyListenerService";
	protected static final String ACTION_SPECIALKEY = "tf.test.SpecialKeyPressed";
	protected static final String SCANNER_DEV = "/dev/ttyMT0";
	protected static final String ACTION_SCANNER = "tf.test.ScannerData";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = getApplicationContext();
		if (PhoneModelManager.isScanS200Phone(mContext)) {
			mSerialPort = new SerialPort();
			mSpecialKey = new SpecialKey();
			mSpecialKey.startListenthread();
			mSpecialKey.SendContext(mContext);
		}
	

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

	
	}

	protected void SerialPortClose() {

		try {
			SerialPort.close(fdUart);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(ex.getMessage());
		}
		fdUart = -1;
	}

	protected void SerialPortOpen() {
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

}
