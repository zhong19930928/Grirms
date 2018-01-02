package com.yunhu.yhshxc.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.parser.GetOrg;
import com.yunhu.yhshxc.parser.GetOrg.ResponseListener;
import com.yunhu.yhshxc.parser.OrgParser;

/**
 * 下载组织结构店面dialog
 * @author gcg_jishen
 */
public class DialogGetOrgActivity extends Activity implements ResponseListener{
	
	private ProgressBar progressBar;
	private LinearLayout ll_confirm,ll_cancel;
	private GetOrg getOrg;
	private TextView tv_dialog_get_org_tip_msg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_get_org);
		progressBar = (ProgressBar)findViewById(R.id.bar_dialog_org);
		ll_confirm = (LinearLayout)findViewById(R.id.btn_confirm);
		ll_cancel = (LinearLayout)findViewById(R.id.btn_cancel);
		tv_dialog_get_org_tip_msg = (TextView)findViewById(R.id.tv_dialog_get_org_tip_msg);
		getOrg = new GetOrg(this, this);
		StringBuffer buffer = new StringBuffer();
		if (getOrg.getOrgOperation()) {
			buffer.append(getResources().getString(R.string.institution));
		}
		if (getOrg.getUserOperation()) {
			buffer.append(getResources().getString(R.string.user));
		}
		if (getOrg.getStoreOperation()) {
			buffer.append(getResources().getString(R.string.store));
		}
		if (buffer.length() > 1) {
			String tip = getResources().getString(R.string.has)+buffer.substring(0, buffer.length()-1)+getResources().getString(R.string.data_update);
			tv_dialog_get_org_tip_msg.setText(tip);
		}else{
			finish();
		}
	}

	public void onClick(View view){
		
		switch(view.getId()){
			case R.id.btn_confirm:
				start(Func.ORG_OPTION);
				break;
			case R.id.btn_cancel:
				this.finish();
				break;
			default:
		}
	}

	/**
	 * 开始下载
	 */
	private void start(int type){
		ll_confirm.setBackgroundResource(R.color.darkGray);
		ll_cancel.setBackgroundResource(R.color.darkGray);
		ll_confirm.setEnabled(false);
		ll_cancel.setEnabled(false);
		if (type == Func.ORG_OPTION && getOrg.getOrgOperation()) {
			getOrg.getOrg();
			progressBar.setProgress(0);
		}else if (type == Func.ORG_USER && getOrg.getUserOperation()) {
			getOrg.getUser();
			progressBar.setProgress(30);
		}else if (type == Func.ORG_STORE && getOrg.getStoreOperation()) {
			getOrg.getStore();
			progressBar.setProgress(60);
		}else if(type > Func.ORG_STORE){
			progressBar.setProgress(100);
			SystemClock.sleep(2000);
			finish();
		}else{
			orgType = type;
			start(orgType + 1);
		}
	}
	
	@Override
	public void onSuccess(int type, String result) {
		orgType = type;
		new initORGAnsyncTask().execute(result);
	}
	
	/**
	 * 解析机构、店面、用户
	 * @author houyu
	 *
	 */
	private int orgType = 1;
	private class initORGAnsyncTask extends AsyncTask<String,Integer, Boolean> {
		
		@Override
		protected Boolean doInBackground(String... params) {
			boolean flag = true;
			String json = params[0];//获取要解析的json数据
			try {
				//开始解析
				new OrgParser(DialogGetOrgActivity.this).batchParseByType(json,orgType);
			} catch (Exception e) {
				//如果有异常该变标识状态
				e.printStackTrace();
			}
			isContinue(flag);
			return flag;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			start(orgType + 1);
		}
		private void isContinue(Boolean result){
			//4.1版本以上
			if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN){
				
			}else{
					Message message = mHandler.obtainMessage();
					message.obj = result;
					message.sendToTarget();
			}
		}
		
		private Handler mHandler = new  Handler(){
			public void handleMessage(Message msg) {
				boolean flag = (Boolean) msg.obj;
				start(orgType + 1);		
				};
		};
	}
	

	@Override
	public void onFailure(int type) {
		ll_confirm.setBackgroundResource(R.drawable.func_detail_submit_btn);
		ll_cancel.setBackgroundResource(R.drawable.func_detail_submit_btn);
		ll_confirm.setEnabled(true);
		ll_cancel.setEnabled(true);
		loadFail(type);
	}

	private void loadFail(final int type){
		String tipMsg = null;
		if (type == Func.ORG_OPTION) {
			tipMsg = getResources().getString(R.string.institution_loading_fails_please_tryagain);
		}else if (type == Func.ORG_USER) {
			tipMsg = getResources().getString(R.string.user_loading_fails_please_tryagain);
		}else if (type == Func.ORG_STORE) {
			tipMsg = getResources().getString(R.string.store_loading_fails_please_tryagain);
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getResources().getString(R.string.tip));
		builder.setMessage(tipMsg);
		builder.setPositiveButton(getResources().getString(R.string.submit_btn_again), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				start(type);
			}
		});
		builder.setNegativeButton(getResources().getString(R.string.Cancle), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				DialogGetOrgActivity.this.finish();
			}
		});
		builder.setCancelable(false);
		builder.create().show();
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    switch (keyCode) {
	        case KeyEvent.KEYCODE_BACK:
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * 处理  dialog样式的activity点击空白处自动关闭
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN && isOutOfBounds(this, event)) {
			return true;
		}
		return super.onTouchEvent(event);
	}

	private boolean isOutOfBounds(Activity context, MotionEvent event) {
		final int x = (int) event.getX();
		final int y = (int) event.getY();
		final int slop = ViewConfiguration.get(context).getScaledWindowTouchSlop();
		final View decorView = context.getWindow().getDecorView();
		return (x < -slop) || (y < -slop)|| (x > (decorView.getWidth() + slop))|| (y > (decorView.getHeight() + slop));
	}
	
}
