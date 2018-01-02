package com.yunhu.yhshxc.activity;

import java.io.IOException;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.utility.PublicUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NfcFuncActivity extends Activity {
	private RelativeLayout nfc_anima;// 动画布局
	private ImageView img_tag;// 动画标签
	private TextView show_content;// 读取内容
	private TextView nfc_title;
	private NfcAdapter nfcAdapter;
	private static final int DIALOG_NO_NFC = 1;// 1:不支持NFC功能
	private static final int DIALOG_OPEN_NFC = 2;// 2:未开启NFC功能
	private AlertDialog alertDialog;
	private int flag = 0;// 11读取 22写入
	private PendingIntent pendingIntent;
	private IntentFilter[] mFilters;
	private String[][] mTechLists;
	private boolean isFirst = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 检查设备是否支持NFC功能
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		checkNFC();
		setContentView(R.layout.nfc_func);
		Intent intent = getIntent();
		flag = intent.getIntExtra("nfctag", 0);
		initView();
		// initData();

	}

	private void initData() {

	}

	private void initView() {
		nfc_title = (TextView) findViewById(R.id.nfc_title);
		if (flag == 11) {
			nfc_title.setText(PublicUtils.getResourceString(this,R.string.nfc_info1));
		} else if (flag == 22) {
			nfc_title.setText(PublicUtils.getResourceString(this,R.string.nfc_info2));
		}
		nfc_anima = (RelativeLayout) findViewById(R.id.nfc_anima);
		img_tag = (ImageView) findViewById(R.id.img_tag);
		show_content = (TextView) findViewById(R.id.show_content);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		int pxWidth = displayMetrics.widthPixels;
		int transX = pxWidth / 2 - 50;
		TranslateAnimation translate = new TranslateAnimation(0, -transX, 0, 0);
		translate.setDuration(3000);
		translate.setRepeatCount(1000);
		img_tag.startAnimation(translate);

		pendingIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
		ndef.addCategory("*/*");
		mFilters = new IntentFilter[] { ndef };// 过滤器
		mTechLists = new String[][] { new String[] { MifareClassic.class.getName() },
				new String[] { NfcA.class.getName() } };// 允许扫描的标签类型

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		checkNFC();
		if (nfcAdapter != null && flag == 11) {
			nfcAdapter.enableForegroundDispatch(this, pendingIntent, mFilters, mTechLists);
			if (isFirst) {
				if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(getIntent().getAction())) {
					String result = processIntent(getIntent());
					if (!TextUtils.isEmpty(result)) {
						nfc_anima.setVisibility(View.GONE);
						show_content.setText(result);
						 Intent i = new Intent();
	   					 i.putExtra("SCAN_RESULT",result);
	   					 NfcFuncActivity.this.setResult(R.id.scan_succeeded,i);
	   					 NfcFuncActivity.this.finish();
				
					} else {
						Toast.makeText(this, R.string.nfc_info3, Toast.LENGTH_SHORT).show();
					}

				}
				isFirst = false;
			}
		} else {
			nfcAdapter.enableForegroundDispatch(this, pendingIntent, mFilters, mTechLists);
		}

	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		if (nfcAdapter != null && flag == 11) {
			if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
				String result = processIntent(intent);
				if (!TextUtils.isEmpty(result)) {
					nfc_anima.setVisibility(View.GONE);
					show_content.setText(result);
					 Intent i = new Intent();
   					 i.putExtra("SCAN_RESULT",result);
   					 NfcFuncActivity.this.setResult(R.id.scan_succeeded,i);
   					 NfcFuncActivity.this.finish();
				} else {
					Toast.makeText(this, R.string.nfc_info3, Toast.LENGTH_SHORT).show();
				}
			}
		} else if (flag == 22 && NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
			Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			NdefMessage ndefMessage = getNoteAsNdef();
			if (ndefMessage != null) {
				writeTag(getNoteAsNdef(), tag);
			} else {
				showToast(PublicUtils.getResourceString(this,R.string.nfc_info4));
			}
		}

	}
	// 根据文本生成一个NdefRecord
	private NdefMessage getNoteAsNdef() {
		String text = PublicUtils.getResourceString(this,R.string.nfc_info);
		if (text.equals("")) {
			return null;
		} else {
			byte[] textBytes = text.getBytes();
			// image/jpeg text/plain
			NdefRecord textRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA,
					"image/jpeg".getBytes(), new byte[] {}, textBytes);
			return new NdefMessage(new NdefRecord[] { textRecord });
		}

	}

	// 写入tag
	boolean writeTag(NdefMessage message, Tag tag) {

		int size = message.toByteArray().length;

		try {
			Ndef ndef = Ndef.get(tag);
			if (ndef != null) {
				ndef.connect();

				if (!ndef.isWritable()) {
					showToast(PublicUtils.getResourceString(NfcFuncActivity.this,R.string.nfc_info7));
					return false;
				}
				if (ndef.getMaxSize() < size) {
					showToast(PublicUtils.getResourceString(this,R.string.nfc_info8));
					return false;
				}

				ndef.writeNdefMessage(message);
				showToast(PublicUtils.getResourceString(this,R.string.nfc_info9));
				return true;
			} else {
				NdefFormatable format = NdefFormatable.get(tag);
				if (format != null) {
					try {
						format.connect();
						format.format(message);
						showToast(PublicUtils.getResourceString(this,R.string.nfc_info10));
						return true;
					} catch (IOException e) {
						showToast(PublicUtils.getResourceString(this,R.string.nfc_info11));
						return false;
					}
				} else {
					showToast(PublicUtils.getResourceString(this,R.string.nfc_info12));
					return false;
				}
			}
		} catch (Exception e) {
			showToast(PublicUtils.getResourceString(this,R.string.nfc_info13));
		}

		return false;
	}
	private void showToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}
	private String processIntent(Intent intent) {
		try{
		Parcelable[] rawmsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
		NdefMessage msg = (NdefMessage) rawmsgs[0];
		NdefRecord[] records = msg.getRecords();
		String resultStr = new String(records[0].getPayload());
		return resultStr;
		}catch(Exception e){			
			return null;
		}
		
	}

	private void showDialogMessage(int type) {
		switch (type) {
		case 1:
			alertDialog = new AlertDialog.Builder(this).setTitle(PublicUtils.getResourceString(this,R.string.tip)).setMessage(PublicUtils.getResourceString(this,R.string.nfc_info5))
					.setPositiveButton(PublicUtils.getResourceString(this,R.string.Confirm), new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							NfcFuncActivity.this.finish();
						}
					}).create();
			break;
		case 2:
			alertDialog = new AlertDialog.Builder(this).setTitle(PublicUtils.getResourceString(this,R.string.tip)).setMessage(PublicUtils.getResourceString(this,R.string.nfc_info6))
					.setPositiveButton(PublicUtils.getResourceString(this,R.string.Confirm), new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);

							NfcFuncActivity.this.startActivity(intent);
						}
					}).create();

			break;

		default:
			break;

		}
		alertDialog.show();

	}

	/**
	 * 检查NFC功能
	 */
	private void checkNFC() {
		if (nfcAdapter == null) {// 不支持
			showDialogMessage(DIALOG_NO_NFC);
		} else {
			if (nfcAdapter != null && !nfcAdapter.isEnabled()) {// 系统设置开启NFC功能
				showDialogMessage(DIALOG_OPEN_NFC);
			}
		}
	}

}
