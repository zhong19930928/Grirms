package com.yunhu.yhshxc.print;

import android.bluetooth.BluetoothDevice;

public interface BluetoothPrinter {
	public static final int STATUS_OTHER = 0;
	public static final int STATUS_CONNECTED = 1;
	public static final int STATUS_DISCONNECTED = 2;
	public static final int STATUS_CONNECTING = 3;
	public static final int STATUS_DISCONNECTING = 4;
	
	public void init(Object... params);
	
	public void connect(BluetoothDevice device);
	
	public void disconnect();
	
	public void release();
	
	public void printText(String text, int x, int scaleWidth, int scaleHeight, int fontType, boolean isBold);
	
	public void printDivider();
	
	public void printNewLine();
	
	public void printBarCode(String code, int x);
	
	public int getDeviceWidth();
	
	public int getStatus();
}
