package com.yunhu.yhshxc.http.download.apk;


import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.utility.Constants;


/**
 * 下载完成以后弹出对话框提示安装
 * @author jishen
 *
 */
public class DialogInstallActivity extends Activity{
	
	/**
	 * 安装包的路径
	 */
	private String path=null;
	/**
	 * 提示的内容
	 */
	private TextView context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_download);
		context=(TextView) findViewById(R.id.downTv);
		String projectVersion=this.getResources().getString(R.string.PROJECT_VERSIONS);
		if(projectVersion.equals(Constants.APP_VERSION_4_5)){//4.5
			context.setText(this.getResources().getString(R.string.UPDATE4_5));
		}else{//其他类型 目前包括伊利和4.0
			context.setText(this.getResources().getString(R.string.UPDATE4_0));
		}
		path=getIntent().getStringExtra("path");//获取apk路径
	}

	public void onClick(View view){
		
		switch(view.getId()){
			case R.id.btn_confirm:
				showDialogToInstallApk(path);
				this.finish();
				break;
			
			case R.id.btn_cancel:
				this.finish();
				break;
			default:
		}
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

	@Override
	protected void onDestroy() {
		
		super.onDestroy();
	}
	
	// 显示安装管理软件apk对话框
	private void showDialogToInstallApk(String path) {
		if (new File(path).exists()) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.parse("file://" + path),
					"application/vnd.android.package-archive");
			// 返回
			startActivity(intent);
		}
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
