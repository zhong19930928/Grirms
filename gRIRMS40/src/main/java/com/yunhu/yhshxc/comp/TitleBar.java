package com.yunhu.yhshxc.comp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemClock;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ImageView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.listener.SignalStrengthListener;
import com.yunhu.yhshxc.utility.Constants;

/**
 * 
 * 显示电量 信号状态的view
 * @author jishen
 *
 */
public class TitleBar {

	private Context context = null;
	private TelephonyManager telManger = null;
	private SignalStrengthListener signalStrengthListener = null;
	private BroadcastReceiver receiver = null;
	private ImageView iv_signal = null;//信号显示view
	private ImageView iv_location = null;//定位view
	private ImageView iv_power= null;//电量view
	
	public TitleBar(Context context,View ll_titlebar) {
		this.context = context;
		// 获取显示信号强度的ImageView
		iv_signal = (ImageView)ll_titlebar.findViewById(R.id.iv_signal);
		// 获取显示定位的ImageView
		iv_location = (ImageView)ll_titlebar.findViewById(R.id.iv_location);
		// 获取显示电量的ImageView
		iv_power = (ImageView)ll_titlebar.findViewById(R.id.iv_power);
	}

	/**
	 * 根据状态设置图片
	 * @param power 状态
	 * @return
	 */
	private int getPowerImageRes(int power){
		int imageRes = -1;
		if(power < 5){
			imageRes = R.drawable.titlebar_power_0;
		}else if(power < 10){
			imageRes = R.drawable.titlebar_power_1;
		}else if(power < 50){
			imageRes = R.drawable.titlebar_power_2;
		}else if(power < 90){
			imageRes = R.drawable.titlebar_power_3;
		}else{
			imageRes = R.drawable.titlebar_power_4;
		}
		return imageRes;
	}
	
	/**
	 * 注册监听
	 */
	public void register(){
		// 得到TelephonyManager
		telManger = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		// 信号强度监听
		signalStrengthListener = new SignalStrengthListener(context, iv_signal);
		// 注册信号强度监听
		telManger.listen(signalStrengthListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

		receiver = new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				//获取电量广播
				if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
					int level = intent.getIntExtra("level", 0); //获得当前电量 
					int scale = intent.getIntExtra("scale", 100); //获得总电量
					// "电池电量：" + (level * 100 / scale) + "%";
					int p = level * 100 / scale;
//					Log.d("888", "level:"+level+",scale:"+scale+",电池电量:"+p);
					iv_power.setImageResource(getPowerImageRes(p));
				}else if (Constants.BROADCAST_LOCATION.equals(intent.getAction())) { //获取定位广播
					iv_location.setVisibility(View.VISIBLE);
					SystemClock.sleep(1000);
					iv_location.setVisibility(View.GONE);
				}
			}
		};
		//注册广播
		IntentFilter filter=new IntentFilter();
		filter.addAction(Intent.ACTION_BATTERY_CHANGED);
		//filter.addAction(Constants.BROADCAST_LOCATION);
		context.registerReceiver(receiver, filter);
	}
	
	/**
	 * 取消监听
	 */
	public void unregister(){
		if(telManger != null){
			telManger.listen(signalStrengthListener, PhoneStateListener.LISTEN_NONE);
		}
		if(receiver != null){
			context.unregisterReceiver(receiver);
		}
	}
}
