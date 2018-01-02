package com.yunhu.yhshxc.listener;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;

/**
 * Gsm信号强度监听
 */
public class SignalCheckListener extends PhoneStateListener {
	private static final int BEST = 0; //信号最好
	private static final int GOOD = 1;//信号好
	private static final int BAD = 2;//信号坏
	private static final int WORST = 3;//信号最坏
	private static final int DIE = 4;//接近无信号
	private int flag = 8;//信号状态
	private Context context;// 上下文
	private String TAG = "SignalCheckListener";

	/**
	 * 构造方法
	 * @param context
	 * @param ivSignal
	 */
	public SignalCheckListener(Context context) {
		super();
		this.context = context;
	}

	/**
	 * 王建雨 2012.6.13 
	 * 信号方式改变时,回调此方法
	 * @param signalStrength 当前信号强度
	 */
	
	public boolean signalStrength(){
		boolean signal=true;
		switch (flag) {
		case BEST:
			signal=true;
			break;
		case GOOD:
			signal=true;
			break;
		case BAD:
			signal=true;
			break;
		case WORST:
			signal=false;
			break;
		case DIE:
			signal=false;
			break;
		default:
			signal=false;
			break;
		}
		return signal;
	}
	
	@Override
	public void onSignalStrengthsChanged(SignalStrength signalStrength) {
		super.onSignalStrengthsChanged(signalStrength);
		//Evdo信号的dbm值,越接近0越好
		int signalStrengthInt = signalStrength.getEvdoDbm();
		//初始化资源文件
		//给当前信号状态赋值
		if (signalStrengthInt >= -70) {
			flag = BEST;
		} else if (signalStrengthInt > -80 && signalStrengthInt < -70) {
			flag = GOOD;
		} else if (signalStrengthInt > -90 && signalStrengthInt <= -80) {
			flag = BAD;
		} else if (signalStrengthInt > -95 && signalStrengthInt <= -90) {
			flag = WORST;
		} else if (signalStrengthInt < -95) {
			flag = DIE;
		}
	}
}
