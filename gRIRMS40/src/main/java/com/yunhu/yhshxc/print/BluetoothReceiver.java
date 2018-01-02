package com.yunhu.yhshxc.print;

import gcg.org.debug.ELog;

import java.util.concurrent.TimeUnit;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BluetoothReceiver extends BroadcastReceiver {
	private PrintHelper helper;
	private BluetoothStatusListener statusListener;
	
	public BluetoothReceiver(PrintHelper helper) {
		this.helper = helper;
	}
	
	public void setStatusListener(BluetoothStatusListener statusListener) {
		this.statusListener = statusListener;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
		if (action.equals(BluetoothDevice.ACTION_FOUND)) {
			if (device == null)
				return;
			final String address = device.getAddress();
			String name = device.getName();
			ELog.i("发现设备:" + name + "/" + device.getAddress());
			
			helper.addItemView(device);
			
			if (statusListener != null)
				statusListener.statusChanged(action, device);
		}
		else if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
			// 绑定状态改变了，有3中可能
			// 1,从未绑定到正在绑定
			// 2,从正在绑定到绑定成功或失败
			// 3,从已绑定到未绑定
			// 最重要的是逻辑
			ELog.i("状态改变:" + device.getBondState());
			
			if (statusListener != null)
				statusListener.statusChanged(action, device);
		}
		else if (action.equals(BluetoothDevice.ACTION_ACL_CONNECTED)) {
			ELog.i("连接成功");
			helper.connectedDevice = device;
			helper.dismissDialog();
			try {
				TimeUnit.SECONDS.sleep(1);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
//			helper.print();
			
			if (statusListener != null)
				statusListener.statusChanged(action, device);
		}
		else if (action.equals(BluetoothDevice.ACTION_ACL_DISCONNECTED)) {
			ELog.i("断开连接");
			
			if (statusListener != null)
				statusListener.statusChanged(action, device);
		}
		else if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
			switch (helper.adapter.getState()) {
				case BluetoothAdapter.STATE_TURNING_ON:
					ELog.i("正在开启蓝牙...");
					break;
				case BluetoothAdapter.STATE_ON:
					helper.adapter.startDiscovery();
					ELog.i("蓝牙已开");
					break;
				case BluetoothAdapter.STATE_TURNING_OFF:
					ELog.i("正在关闭蓝牙...");
					break;
				case BluetoothAdapter.STATE_OFF:
					ELog.i("蓝牙已关");
					break;
			}
			
			if (statusListener != null)
				statusListener.statusChanged(action, device);
		}
		else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)) {
			ELog.i("正在搜索");
			
			helper.setProgressVisibility(true);
			
			if (statusListener != null)
				statusListener.statusChanged(action, device);
		}
		else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
			ELog.i("搜索完毕");
			
			helper.setProgressVisibility(false);
			
			if (statusListener != null)
				statusListener.statusChanged(action, device);
		}
		else {
			ELog.i(action);
			
			if (statusListener != null)
				statusListener.statusChanged(action, device);
		}
	}

}
