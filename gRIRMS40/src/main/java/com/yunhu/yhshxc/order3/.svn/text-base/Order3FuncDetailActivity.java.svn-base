package com.yunhu.yhshxc.order3;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.FuncDetailActivity;
import com.yunhu.yhshxc.order3.util.SharedPreferencesForOrder3Util;
import com.yunhu.yhshxc.utility.PublicUtils;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Order3FuncDetailActivity extends FuncDetailActivity{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
	}
	
	@Override
	public void onClick(View v) {
		submitDataLayout.setEnabled(false);
		switch (v.getId()) {
		case R.id.ll_func_show_detail_data: // 提交数据
			ableStatus = "0";
			setOrder3Time();
			
			break;
		case R.id.editPhoto_btn:
			editPhoto();//照片编辑
			break;

		default:
			break;
		}
	}
	
	private void submitOrder3Func(){
		if (TextUtils.isEmpty(submit.getTimestamp())) {
			submit.setTimestamp(String.valueOf(System.currentTimeMillis()));
		}
		submitDB.updateSubmit(submit);
		SharedPreferencesForOrder3Util.getInstance(this).setOrderTimestamp(submit.getTimestamp());
		thisFinish(R.id.submit_succeeded,null);
	}
	
	
	private  void setOrder3Time(){
		flagOrder3Time = true;
		timer = new Timer(true);
		if(task.cancel()){
			timer.schedule(task,3000, 100000000); //延时3000ms后执行，1000ms执行一次
		}
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Date date = PublicUtils.getNetDate();;
				  Message msg = Message.obtain();
				  msg.obj = date;
				  mHanlder.sendMessage(msg);
			}
		}).start();
	}
	 Handler mHanlder = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(flagOrder3Time){
				flagOrder3Time = false;
				if (task != null){
					 task.cancel();
				 }
				Date date = (Date) msg.obj;
				if(null==date){
					Toast.makeText(Order3FuncDetailActivity.this, "请检查当前网络是否可用", Toast.LENGTH_SHORT).show();
				}else{
					if (usableControlOrder3(date) && isCompletion()) {
						submitOrder3Func();
					}
				}
			}
		};
	};
	private  boolean flagOrder3Time = true;
	 Timer timer;
	 TimerTask task = new TimerTask(){  
        public void run() {  
        	mHanlder.sendEmptyMessage(1);   
        }  
	};
}
