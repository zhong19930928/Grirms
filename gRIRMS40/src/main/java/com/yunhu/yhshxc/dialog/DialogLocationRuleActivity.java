package com.yunhu.yhshxc.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.location.backstage.BackstageLocationManager;
import com.yunhu.yhshxc.location.backstage.SharedPrefsBackstageLocation;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;


/**
 * 提示被动定位规则
 * @author gcg_jishen
 *
 */
public class DialogLocationRuleActivity extends Activity{

	private TextView tv_content;
	private Button btn_cancel;
	private BackstageLocationManager manager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_location_rule);
		tv_content = (TextView)this.findViewById(R.id.tv_content);
		btn_cancel = (Button)this.findViewById(R.id.btn_cancel);
		manager = new BackstageLocationManager(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		final int interval = SharedPrefsBackstageLocation.getInstance(this.getApplicationContext()).getLocationEachInterval();
		btn_cancel.setText(getResources().getString(R.string.writ));
		tv_content.setText(getMessage());
		
	}

	public void onClick(View view){
		
		switch(view.getId()){
			case R.id.btn_confirm:
				SharedPrefsBackstageLocation prefs = SharedPrefsBackstageLocation.getInstance(this);
				prefs.setIsConfirmLocationRule(true);
				SharedPreferencesUtil.getInstance(this).setIsTipUser(true);
				finish();
				break;
			
			case R.id.btn_cancel:
				manager.updateLocationRulePromptDateByDelay();
				finish();
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
	
	/**
	 * 返回被动定位规则的提示信息
	 * @return
	 */
	public String getMessage() {
		String begin = SharedPrefsBackstageLocation.getInstance(this.getApplicationContext()).getLocationStartTime();
		String end = SharedPrefsBackstageLocation.getInstance(this.getApplicationContext()).getLocationStopTime();
		StringBuffer sb = new StringBuffer();
//		sb.append(this.getResources().getString(R.string.TIP_LOCATION_BEGIN)).append(" 在").append(begin).append("时~").append(end);
//		sb.append("时期间，每隔").append(interval).append("分钟对您定位一次");
		sb.append(getResources().getString(R.string.locationrule_message01)).append(begin).append(" ~ ").append(end).append(getResources().getString(R.string.locationrule_message02));
		return sb.toString();
	}

//	@Override
//	public void onBackPressed() {
//		manager.updateLocationRulePromptDate(true);
//		manager.popup = null;
//		
//		SharedPrefsBackstageLocation prefs = SharedPrefsBackstageLocation.getInstance(this.getApplicationContext());
//
//		//将拒绝定位字段设置为明日
//		Calendar current = Calendar.getInstance();
//		if (current.get(Calendar.HOUR_OF_DAY) >= 23)
//			current.add(Calendar.DAY_OF_MONTH, 1);
//		int unavailableTime = current.get(Calendar.YEAR) * 10000 + (current.get(Calendar.MONTH) + 1) * 100 + current.get(Calendar.DAY_OF_MONTH);
//		prefs.setLocationUnavailableTime(unavailableTime);
//		JLog.d("wangchao", "不定位时间:" + prefs.getLocationUnavailableTime());
//		
//		finish();
//	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
}
