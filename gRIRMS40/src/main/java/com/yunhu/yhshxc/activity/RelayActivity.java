package com.yunhu.yhshxc.activity;

import gcg.org.debug.JLog;
import android.content.Intent;
import android.os.Bundle;

import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.bo.Module;
import com.yunhu.yhshxc.database.MainMenuDB;
import com.yunhu.yhshxc.database.ModuleDB;
import com.yunhu.yhshxc.doubletask.DoubleWayActivity;
import com.yunhu.yhshxc.doubletask2.NewDoubleListActivity;
import com.yunhu.yhshxc.notify.NotifyListActivity;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.visit.VisitWayActivity;
/**
 * push收到，计划 公告 任务 双向
 * @author gcg_jishen
 *
 */
public class RelayActivity extends AbsBaseActivity{

	Intent relayIntent = null;
			
	@Override
	public void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		relayIntent = this.getIntent();
		JLog.d("RelayActivity", "进入onCreate");
	}

	@Override
	protected void onResume() {

		super.onResume();
		int state = relayIntent.getIntExtra(Constants.RELAY_STATE, 0);
		JLog.d("RelayActivity", "进入onResume"+state);
		into(state);
	}

	private void into(int state){
		switch (state) {
		case Constants.RELAY_NOTICE:
			intoNotice();
			Constants.NOTICENUMBER=0;
			this.finish();
			break;
		case Constants.RELAY_TASK:
			intoTask();
			Constants.TASKNUMBER=0;
			this.finish();
			break;
		case Constants.RELAY_PLAN:
			intoPlan();
			this.finish();
			break;
		case Constants.RELAY_DOUBLE:
			intoDouble();
			Constants.DOUBLENUMBER=0;
			this.finish();
			break;
		default:
			this.finish();
			break;
		}
	}
	
	/**
	 * 公告
	 */
	private void intoNotice(){
		Intent intent = new Intent(this,NotifyListActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(intent);
		this.finish();
	}
	/**
	 * 人物
	 */
	private void intoTask(){
		Integer moduleId=relayIntent.getIntExtra("moduleId", Constants.DEFAULTINT);
		Intent intent = new Intent(this,TaskListActivity.class);
		intent.putExtra("moduleId", moduleId);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(intent);
		this.finish();
	}
	/**
	 * 计划
	 */
	private void intoPlan(){
		Intent intent= new Intent(this,VisitWayActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(intent);
		this.finish();
	}
	/**
	 * 双向
	 */
	private void intoDouble(){
		int targetId=relayIntent.getIntExtra("targetId", Constants.DEFAULTINT);
		Menu currentMenu=new MainMenuDB(this).findMenuListByMenuId(targetId);
		if (currentMenu == null) {
			return;
		}
		Intent intent = null;
		Module module = null;
		if (currentMenu.getType() == Menu.TYPE_NEW_TARGET) {
			intent= new Intent(this,NewDoubleListActivity.class);
			module=new ModuleDB(this).findModuleByTargetId(targetId,Constants.MODULE_TYPE_EXECUTE_NEW);
		}else{
			intent= new Intent(this,DoubleWayActivity.class);
			module=new ModuleDB(this).findModuleByTargetId(targetId,Constants.MODULE_TYPE_EXECUTE);
		}
		Bundle bundle=new Bundle();
		bundle.putSerializable("module", module);
		intent.putExtra("bundle", bundle);
		intent.putExtra("targetId", targetId);
		intent.putExtra("doubleHead", module.getName());
		bundle.putBoolean("isNoWait",currentMenu.getIsNoWait() == 1 ? true:false);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		SharedPreferencesUtil.getInstance(this).setKey(String.valueOf(currentMenu.getMenuId()), "0");
		this.startActivity(intent);
		this.finish();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		relayIntent = this.getIntent();
		JLog.d("RelayActivity", "进入onNewIntent");
		super.onNewIntent(intent);
	}
	
	
}

