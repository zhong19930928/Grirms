package com.yunhu.yhshxc.http;

import gcg.org.debug.JDebugOptions;
import gcg.org.debug.JLog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;


/**
 * 电话状态监听
 * @author jishen
 *
 */
public abstract class CoreHttpAbstractChances extends PhoneStateListener {
	
	private TelephonyManager tm = null;
	private CoreHttpOppotunity oppotunity = null;
	private Context context = null;
	private boolean isOpenedListen = false; //当前监听是否打开 true已经打开 false 没有打开
	//定义要监听属性状态
	private final int events = PhoneStateListener.LISTEN_SIGNAL_STRENGTH //网络信号强度
			| PhoneStateListener.LISTEN_DATA_ACTIVITY//监听数据流量移动方向的变化
			| PhoneStateListener.LISTEN_CALL_STATE//监听通话状态
			| PhoneStateListener.LISTEN_DATA_CONNECTION_STATE//监听数据更改连接状态
			| PhoneStateListener.LISTEN_SERVICE_STATE;//监听网络修改状态
	
	public CoreHttpAbstractChances(Context con){
		this.context=con;
		this.tm=(TelephonyManager) this.context
				.getSystemService(Context.TELEPHONY_SERVICE);
		this.oppotunity=new CoreHttpOppotunity();		
	}
	
	/**
	 * 开启监听
	 */
	public void startListen(){
		this.tm.listen(this, events);
		IntentFilter filter=new IntentFilter();
		filter.addAction(WifiManager.RSSI_CHANGED_ACTION);
		filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		context.registerReceiver(wifiStateReceiver, filter);
		isOpenedListen = true;
	}
	
	/**
	 * 取消监听
	 */
	public void stopListen(){
		this.tm.listen(this, PhoneStateListener.LISTEN_NONE);
		context.unregisterReceiver(wifiStateReceiver);
		isOpenedListen = false;
	}
	
	/**
	 * 获取当前的监听状态
	 * @return true标识当前监听是打开的 false 标识已经关闭监听
	 */
	public boolean isOpenedListen(){
		return isOpenedListen;
	}
	
	public abstract void ifOppotunity();
	
	/**
	 * 当前网络状态是否通过
	 * @return true标识通过 false标识不通过
	 */
	public boolean isOppotunity(){
		return oppotunity.isOppotunity();
	}
	
	/**
	 * 打印线程信息
	 * @param message 标识
	 */
	private void log(String message){
		StringBuffer sb = new StringBuffer();
		sb.append(message);
		sb.append(",(");
		sb.append("thread:").append(Thread.currentThread().getName());
		sb.append(",pid:").append(android.os.Process.myPid());
		sb.append(",tid:").append(android.os.Process.myTid());
		sb.append(",uid:").append(android.os.Process.myUid());
		sb.append(")");
		JLog.d(JDebugOptions.TAG_NET_STATE,sb.toString());
	}
	
	/**
	 * 范围当前wify信号强度
	 * @return
	 */
	public int getStrength(){
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifiManager.getConnectionInfo();
		int strength = 0;
		if (info != null && info.getBSSID() != null) {
			strength = 2;
			//strength = WifiManager.calculateSignalLevel(info.getRssi(), 5);
			//int strength = info.getRssi();
			// 链接速度
//			int speed = info.getLinkSpeed();
//			// 链接速度单位
//			String units = WifiInfo.LINK_SPEED_UNITS;
//			// Wifi源名称
//			String ssid = info.getSSID();
			
		}
		
		return strength;
	}
	
	/**
	 * wifi信号强度广播接收
	 */
	private BroadcastReceiver wifiStateReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(WifiManager.RSSI_CHANGED_ACTION)){
				int strength=getStrength();
				log("RSSI_CHANGED_ACTION,RE-EVAL=>wifi信号强度"+strength);
				oppotunity.updateWifiState(strength);
				ifOppotunity();
			}
		}
		
	};
	
	@Override
	public void onCallStateChanged(int state, String incomingNumber) { //对设备呼叫状态的变化监听。
		log("CALL STATE CHANGED,RE-EVAL=>呼叫状态的变化");
		oppotunity.updateCallState(state);
		this.ifOppotunity();
		super.onCallStateChanged(state, incomingNumber);
	}

	@Override
	public void onDataActivity(int direction) { //对数据流量移动方向的变化监听
		log("ACTV STATE CHANGED, RE-EVAL=>数据流量移动方向的变化"+direction);
		oppotunity.updateDircState(direction);
		this.ifOppotunity();
		super.onDataActivity(direction);
	}

	@Override
	public void onDataConnectionStateChanged(int state) { //对数据连接状态的变化监听
		log("DATA STATE CHANGED, RE-EVAL=>数据连接状态的变化"+state);
		oppotunity.updateDataState(state);
		this.ifOppotunity();
		super.onDataConnectionStateChanged(state);
	}

	@Override
	public void onServiceStateChanged(ServiceState serviceState) { //对网络服务状态监听
		log("SEVC STATE CHANGED, RE-EVAL=>网络服务状态");
		oppotunity.updateSevcState(serviceState);
		this.ifOppotunity();
		super.onServiceStateChanged(serviceState);
	}

	@Override
	public void onSignalStrengthsChanged(SignalStrength signalStrength) { //对网络信号强度的变化监听
		log("SIGL STATE CHANGED, RE-EVAL =>网络信号强度的变化:"+ signalStrength.getEvdoSnr());
		oppotunity.updateSignState(signalStrength.getEvdoSnr());
		this.ifOppotunity();
		super.onSignalStrengthsChanged(signalStrength);
	}
	
	private class CoreHttpOppotunity {
		private static final int FAIL_CRETERIA = 1000;
		private static final int PASS_CRETERIA = 1001;
		private static final int UNKN_CRETERIA = 1002;

		private int callStateFlag = UNKN_CRETERIA;
		private int dataStateFlag = UNKN_CRETERIA;
		private int sevcStateFlag = UNKN_CRETERIA;
		private int signStateFlag = UNKN_CRETERIA;
		private int dircStateFlag = UNKN_CRETERIA;
		private int wifiStateFlag = UNKN_CRETERIA;

		private void updateUnknownState() {
			if (this.callStateFlag == UNKN_CRETERIA)
				this.updateCallState(tm.getCallState());
			//if (this.dataStateFlag == UNKN_CRETERIA)
				this.updateDataState(tm.getDataState());
			if (this.dircStateFlag == UNKN_CRETERIA)
				this.updateDircState(tm.getDataActivity());
			if (this.sevcStateFlag == UNKN_CRETERIA)
				this.updateSevcState(new ServiceState());
			if (this.signStateFlag == UNKN_CRETERIA)
				this.updateSignState(100);
			if (this.wifiStateFlag == UNKN_CRETERIA)
				this.updateWifiState(getStrength());
		}

		/**
		 * 打印当前的信号状态和线程信息
		 */
		private void log(){
			StringBuffer sb = new StringBuffer();
			sb.append("CALL STATE:").append(this.callStateFlag == PASS_CRETERIA);
			sb.append(",DATA STATE:").append(this.dataStateFlag == PASS_CRETERIA);
			sb.append(",DIRC STATE:").append(this.dircStateFlag == PASS_CRETERIA);
			sb.append(",ACTV STATE:").append(this.sevcStateFlag == PASS_CRETERIA);
			sb.append(",SIGL STATE:").append(this.signStateFlag == PASS_CRETERIA);
			sb.append(",wifi STATE:").append(this.wifiStateFlag == PASS_CRETERIA);
			sb.append(",(");
			sb.append("thread:").append(Thread.currentThread().getName());
			sb.append(",pid:").append(android.os.Process.myPid());
			sb.append(",tid:").append(android.os.Process.myTid());
			sb.append(",uid:").append(android.os.Process.myUid());
			sb.append(")");
			JLog.d(JDebugOptions.TAG_NET_STATE,sb.toString());
		}
		
		/**
		 * 当前网络状态是否通过
		 * @return true标识通过 false标识不通过
		 */
		protected boolean isOppotunity() {
			this.updateUnknownState();
			log();
			return (this.wifiStateFlag == PASS_CRETERIA) 
					|| (this.callStateFlag == PASS_CRETERIA)
					&& (this.dataStateFlag == PASS_CRETERIA)
					&& (this.dircStateFlag == PASS_CRETERIA)
					&& (this.sevcStateFlag == PASS_CRETERIA)
					&& (this.signStateFlag == PASS_CRETERIA);
		}
		
		/**
		 * 更改wifi状态
		 * @param state
		 */
		protected void updateWifiState(int state) {
			if (state > 1 )
				this.wifiStateFlag = PASS_CRETERIA;
			else
				this.wifiStateFlag = FAIL_CRETERIA;
		}

		/**
		 * 设备呼叫时更改状态
		 * @param state
		 */
		protected void updateCallState(int state) {
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:
				this.callStateFlag = PASS_CRETERIA;
				break;
			case TelephonyManager.CALL_STATE_RINGING:
				this.callStateFlag = FAIL_CRETERIA;
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				this.callStateFlag = FAIL_CRETERIA;
				break;
			default:
				this.callStateFlag = PASS_CRETERIA;
				break;
			}
		}

		/**
		 * 对数据流量移动方向的变化监听改变状态
		 * @param direction
		 */
		protected void updateDircState(int direction) {
			switch (direction) {
			case TelephonyManager.DATA_ACTIVITY_DORMANT:
				this.dircStateFlag = PASS_CRETERIA;
				break;
			default:
				this.dircStateFlag = PASS_CRETERIA;
				break;
			}
		}

		/**
		 * 数据连接状态的变化监听更改状态
		 * @param state
		 */
		protected void updateDataState(int state) {
			switch (state) {
			case TelephonyManager.DATA_CONNECTED: //网络连接上
				this.dataStateFlag = PASS_CRETERIA;
				break;
			case TelephonyManager.DATA_CONNECTING: //网络正在连接
				this.dataStateFlag = FAIL_CRETERIA;
				break;
			case TelephonyManager.DATA_DISCONNECTED: //网络断开
				this.dataStateFlag = FAIL_CRETERIA;
				break;
			case TelephonyManager.DATA_SUSPENDED:
				this.dataStateFlag = FAIL_CRETERIA;
				break;
			default:
				this.dataStateFlag = PASS_CRETERIA;
				break;
			}
		}

		/**
		 * 对网络服务状态监听更改状态
		 * @param serviceState
		 */
		protected void updateSevcState(ServiceState serviceState) {
			switch (serviceState.getState()) {
			case ServiceState.STATE_IN_SERVICE:
				this.sevcStateFlag = PASS_CRETERIA;
				break;
			case ServiceState.STATE_EMERGENCY_ONLY:
				this.sevcStateFlag = PASS_CRETERIA;
				break;
			case ServiceState.STATE_OUT_OF_SERVICE:
				this.sevcStateFlag = PASS_CRETERIA;
				break;
			case ServiceState.STATE_POWER_OFF:
				this.sevcStateFlag = PASS_CRETERIA;
				break;
			default:
				this.sevcStateFlag = PASS_CRETERIA;
				break;

			}
		}

		/**
		 * 根据信号强度监听更改数据状态
		 * @param asu
		 */
		protected void updateSignState(int asu) {
			if (goodSignalLevel(asu))
				this.signStateFlag = PASS_CRETERIA;
			else
				this.signStateFlag = FAIL_CRETERIA;
		}

		private boolean goodSignalLevel(int level) {
			return level >= 4;
		}
	}
}



