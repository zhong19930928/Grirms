package com.yunhu.yhshxc.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;

/**
 * 法律声明dialog
 * @author gcg_jishen
 *
 */

public class DialogLawActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_law);
	}

	public void onClick(View view){
		
		switch(view.getId()){
			case R.id.btn_confirm:
				SharedPreferencesUtil.getInstance(this.getApplicationContext()).saveIsReadedLaw(true);
				this.finish();
				break;
			
			case R.id.btn_cancel:
				this.finish();
				break;
			default:
		}
	}

	@Override
	protected void onDestroy() {
		
		super.onDestroy();
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
