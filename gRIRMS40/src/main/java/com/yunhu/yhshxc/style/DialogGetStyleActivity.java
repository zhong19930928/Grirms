package com.yunhu.yhshxc.style;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.HttpHelper;
import com.yunhu.yhshxc.utility.MD5Helper;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.widget.ToastOrder;

/**
 * 下载UI样式dialog
 * @author gcg_jishen
 *
 */
public class DialogGetStyleActivity extends Activity{
	
	private ProgressBar progressBar;
	private LinearLayout ll_confirm,ll_cancel;
	private TextView tv_dialog_get_style_tip_msg;
	private String currentUrl;
	private int currentIndex;
	private List<Style> urlList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		    this.setContentView(R.layout.dialog_get_style);	
		    urlList = StyleUtil.findImageUrlForStyle(this);
		progressBar = (ProgressBar)findViewById(R.id.bar_dialog_style);
		ll_confirm = (LinearLayout)findViewById(R.id.btn_confirm);
		ll_cancel = (LinearLayout)findViewById(R.id.btn_cancel);
		tv_dialog_get_style_tip_msg = (TextView)findViewById(R.id.tv_dialog_get_style_tip_msg);
		String tip = PublicUtils.getResourceString(this,R.string.style_string3);
		tv_dialog_get_style_tip_msg.setText(tip);
		
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		start(currentIndex);
	}

	public void onClick(View view){
		
		switch(view.getId()){
			case R.id.btn_confirm:
				start(currentIndex);
				break;
			case R.id.btn_cancel:
				this.finish();
				break;
			default:
		}
	}
	
	/**
	 * 开始下载
	 * @param indext 要下载图片在集合中的下标
	 */
	private void start(int indext){
		if (urlList != null && !urlList.isEmpty()) {
			ll_confirm.setBackgroundResource(R.color.darkGray);
			ll_cancel.setBackgroundResource(R.color.darkGray);
			ll_confirm.setEnabled(false);
			ll_cancel.setEnabled(false);
			progressBar.setMax(urlList.size());
			if (indext < urlList.size()) {
				currentUrl = urlList.get(indext).getImgUrl();
				if (!TextUtils.isEmpty(currentUrl)) {
					currentUrl = currentUrl.trim();
					new StyleTask().execute();
				}else{
					currentIndex++;
					start(currentIndex);
				}
			}else{
				Intent intent = new Intent();
				intent.setAction(Constants.BROADCAST_ACTION_STYLE);
				sendBroadcast(intent);
				ToastOrder.makeText(this, R.string.style_string2, ToastOrder.LENGTH_SHORT).show();
				this.finish();
			}
		}else{
			ToastOrder.makeText(this, R.string.style_string1, ToastOrder.LENGTH_SHORT).show();
			this.finish();
		}
	}

	private class StyleTask extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			int i = new HttpHelper(DialogGetStyleActivity.this).downloadStyleFile(currentUrl, Constants.COMPANY_STYLE_PATH,urlList.get(currentIndex).getImgName());
			isContinue(String.valueOf(i));
			return String.valueOf(i);//1是下载失败 0是下载成功
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (!TextUtils.isEmpty(result) && "0".equals(result)) {//下载成功
				currentIndex++;
				progressBar.setProgress(currentIndex);
				start(currentIndex);
			}else{//下载失败
				loadFail();
			}
		}
		
		private void isContinue(String result){
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
				String result =  (String) msg.obj;
				if (!TextUtils.isEmpty(result) && "0".equals(result)) {//下载成功
					currentIndex++;
					progressBar.setProgress(currentIndex);
					start(currentIndex);
				}else{//下载失败
					loadFail();
				}
			};
		};

	}
	
	/**
	 * 加载失败
	 */
	private void loadFail(){
		try {
			File file = new File(Constants.COMPANY_STYLE_PATH,MD5Helper.EncoderByMd5(currentUrl));
			if (file.exists()) {
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ll_confirm.setBackgroundResource(R.drawable.func_detail_submit_btn);
		ll_cancel.setBackgroundResource(R.drawable.func_detail_submit_btn);
		ll_confirm.setEnabled(true);
		ll_cancel.setEnabled(true);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.tip);
		builder.setMessage(R.string.BBS_LOAD_FAIL);
		builder.setPositiveButton(R.string.submit_btn_again, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				urlList = StyleUtil.findImageUrlForStyle(DialogGetStyleActivity.this);
				start(currentIndex);
			}
		});
		builder.setNegativeButton(R.string.Cancle, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				DialogGetStyleActivity.this.finish();
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
