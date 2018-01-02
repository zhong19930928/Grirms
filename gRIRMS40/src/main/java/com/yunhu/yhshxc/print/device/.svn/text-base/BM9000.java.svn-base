package com.yunhu.yhshxc.print.device;

import gcg.org.debug.ELog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import btmanager.ClsUtils;
import btmanager.Pos;

import com.yunhu.yhshxc.print.BluetoothPrinter;

public class BM9000 implements BluetoothPrinter {
	private static final String TAG = "PrintHelper(BM9000)";
	
	public static final int WIDTH = 364;
	public static final String DIVIDER = "--------------------------------";
	
	protected Context context;

	@Override
	public void init(Object... params) {
		context = (Context)params[0];
		Pos.APP_Init(context);
	}

	@Override
	public void connect(BluetoothDevice device) {
		if (device.getBondState() == BluetoothDevice.BOND_NONE) {
			try {
				ClsUtils.setPin(device.getClass(), device, "0000");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			ELog.i("绑定");
		}
		
		ELog.i("连接状态:" + Pos.POS_isConnected() + "/" + Pos.POS_isConnecting() + " 绑定状态:" + device.getBondState());
		
		if (!Pos.POS_isConnected() && !Pos.POS_isConnecting()) {
			Pos.POS_Open(device.getAddress());
			ELog.i("连接设备:" + device.getName() + "/" + device.getAddress());
		}
		ELog.i("连接状态:" + Pos.POS_isConnected() + "/" + Pos.POS_isConnecting() + " 绑定状态:" + device.getBondState());
	}

	@Override
	public void disconnect() {
		if (Pos.POS_isConnected() && Pos.POS_isConnecting()) {
			Pos.POS_Close();
			ELog.i("断开连接");
		}
	}
	
	@Override
	public void release() {
		ELog.i("");
		disconnect();
		Pos.APP_UnInit();
	}

	@Override
	public void printText(String text, int x, int scaleWidth, int scaleHeight, int fontType, boolean isBold) {
		Pos.POS_S_TextOut(text, x, scaleWidth, scaleHeight, fontType, isBold ? 0x08 : 0x00);
	}

	@Override
	public void printDivider() {
		Pos.POS_S_TextOut(DIVIDER, 0, 0, 0, 0, 0);
	}

	@Override
	public void printNewLine() {
		Pos.POS_FeedLine();
	}

	@Override
	public void printBarCode(String code, int x) {
		Pos.POS_S_SetBarcode(code, x, 0, 0, 0, 0, 0);
	}

	@Override
	public int getDeviceWidth() {
		return WIDTH;
	}

	@Override
	public int getStatus() {
		if (Pos.POS_isConnected())
			return STATUS_CONNECTED;
		else if (Pos.POS_isConnecting())
			return STATUS_CONNECTING;
		else
			return STATUS_DISCONNECTED;
	}
}
