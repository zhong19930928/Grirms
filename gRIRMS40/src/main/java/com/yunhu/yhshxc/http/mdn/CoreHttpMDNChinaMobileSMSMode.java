package com.yunhu.yhshxc.http.mdn;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.telephony.SmsManager;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.utility.PublicUtils;

import gcg.org.debug.JLog;

/**
 * 移动手机号操作
 * @author jishen
 *
 */
public class CoreHttpMDNChinaMobileSMSMode {

	private final String TAG ="HTTP";
	private Context context;
	private Handler callerHandler;
	private String MDN = null;
	
	private final String SOURCE_MDN_CT = "10086";
	private final String MDN_QUERY_CODE = "本机号码";
	
	protected CoreHttpMDNChinaMobileSMSMode(Context cont, Handler mHandler) {
		this.context = cont;
		this.callerHandler = mHandler;
	}
	
	/**
	 * 获取mdn
	 */
	@Deprecated
	public void obtainMDN() {
		this.sendSMS();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				readMDN();
				JLog.d("HTTP","read with result:" + MDN);
				if(!TextUtils.isEmpty(MDN)){
					context.getApplicationContext().getSharedPreferences(PublicUtils.PREFERENCE_NAME, Context.MODE_PRIVATE).edit().putString(PublicUtils.PREFERENCE_NAME_PHONE, MDN).commit();
					deleteMDN();
					callerHandler.obtainMessage(1, MDN).sendToTarget();
				}else{
					callerHandler.sendEmptyMessage(2);
				}
			}
		}, 15000);
	}
	
	/**
	 * 弹出输入手机号dialog
	 */
	public void showDialog(){
		LinearLayout ll = new LinearLayout(context);
		ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		ll.setPadding(30, 0, 30, 0);
		final EditText edit = new EditText(context);
		edit.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		edit.setHint(context.getResources().getString(R.string.please_input_right_phone_number));
		edit.setSingleLine(true);
		edit.setInputType(InputType.TYPE_CLASS_PHONE);
		edit.setFilters(new InputFilter[] { new InputFilter.LengthFilter(11) });
		ll.addView(edit);
		AlertDialog dialog = new AlertDialog.Builder(context)
			.setIcon(android.R.drawable.btn_star)
			.setTitle(context.getResources().getString(R.string.tip))
			.setView(ll)
			.setPositiveButton(context.getResources().getString(R.string.Confirm), new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					MDN = edit.getText().toString();
					if(!TextUtils.isEmpty(MDN) && PublicUtils.isMobileTelephone(MDN)){
						context.getApplicationContext().getSharedPreferences(PublicUtils.PREFERENCE_NAME, Context.MODE_PRIVATE).edit().putString(PublicUtils.PREFERENCE_NAME_PHONE, MDN).commit();
						callerHandler.obtainMessage(1, MDN).sendToTarget();
					}else{
						showDialog();
					}
				}
				
			}).create();
		dialog.setCancelable(false);
		dialog.show();
	}
	
	/**
	 * 发送短信
	 */
	@Deprecated
	private void sendSMS() {
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(SOURCE_MDN_CT, null, MDN_QUERY_CODE, null,null);
		JLog.d(TAG,"移动SMS一次-----------------------");
	}
	
	/**
	 * 删除短信
	 */
	@Deprecated
	private void deleteMDN() {
		String[] args = new String[] { SOURCE_MDN_CT };
		this.context.getContentResolver().delete(Uri.parse("content://sms"),
				"address=?", args);
	}
	/**
	 * 读取短信
	 */
	@Deprecated
	private void readMDN() {
		JLog.d(TAG,"TIMER:try to read 短信息"+Thread.currentThread().getName());
		Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms"), null, null, null, "_id DESC");
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			int count = 0;
			while (!cursor.isAfterLast() && count < 4) {
				String currMDN = cursor.getString(cursor.getColumnIndex("address"));
				if (currMDN.equals(SOURCE_MDN_CT)) {
					String currContent = cursor.getString(cursor.getColumnIndex("body")).trim();
					if(currContent != null && currContent.length() >= 11){
						currContent = currContent.substring(0, 11);
						if(PublicUtils.isMobileTelephone(currContent)){
							this.MDN = currContent;
							JLog.d("HTTP","isMobileTelephone = true");
						}
					}
					break;
				}
				count++;
				cursor.moveToNext();
			}
			if (!cursor.isClosed()){
				cursor.close();
			}
		}
	}
}
