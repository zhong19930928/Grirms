package com.yunhu.yhshxc.listener;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.widget.ImageView;

import com.yunhu.yhshxc.R;

/**
 * @author JieRain.Wang 2012.6.13 
 * Gsm信号强度监听
 */
public class SignalStrengthListener extends PhoneStateListener {
	private static final int BEST = 0; //信号最好
	private static final int GOOD = 1;//信号好
	private static final int BAD = 2;//信号坏
	private static final int WORST = 3;//信号最坏
	private static final int DIE = 4;//接近无信号
	private int flag = 8;//信号状态
	private int preFlag = 9;//改变前信号状态
	private Context context;// 上下文
	private ImageView ivSignal;// 显示信号的ImageView
	private String TAG = "SignalStrengthListener";

	/**
	 * 构造方法
	 * @param context
	 * @param ivSignal
	 */
	public SignalStrengthListener(Context context, ImageView ivSignal) {
		super();
		this.context = context;
		this.ivSignal = ivSignal;
	}
	/**
	 * 构造方法
	 * @param context
	 * @param ivSignal
	 */
	public SignalStrengthListener(Context context) {
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
			signal=true;
			break;
		}
		return signal;
	}
	
	@Override
	public void onSignalStrengthsChanged(SignalStrength signalStrength) {
		super.onSignalStrengthsChanged(signalStrength);
		//将信号改变前状态付给preFlag
		preFlag = flag; 
		//Evdo信号的dbm值,越接近0越好
		int signalStrengthInt = signalStrength.getEvdoDbm();
		//初始化资源文件
		int imageRes = 0;

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

		//如果信号状态为改变,结束本事件
		if (flag == preFlag) {
			return;
		}

		//根据信号状态设置信号图标
		switch (flag) {
			case BEST:
				imageRes = R.drawable.titlebar_signal_3;
				break;
			case GOOD:
				imageRes = R.drawable.titlebar_signal_2;
				break;
			case BAD:
				imageRes = R.drawable.titlebar_signal_1;
				break;
			case WORST:
				imageRes = R.drawable.titlebar_signal_0;
				break;
			case DIE:
				imageRes = R.drawable.titlebar_signal_0;
				break;
		}
		
		//设置图片资源
		ivSignal.setImageResource(imageRes);
		
//		JLog.d(TAG, "信号强度为" + signalStrength.getEvdoDbm());
	}
}
