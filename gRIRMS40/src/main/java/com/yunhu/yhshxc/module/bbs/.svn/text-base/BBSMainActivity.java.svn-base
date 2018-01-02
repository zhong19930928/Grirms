package com.yunhu.yhshxc.module.bbs;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.utility.Constants;
/**
 * 
 * @author dalvik.peng 
 * 2012.06.26 
 * BBS主界面
 * 
 */
public class BBSMainActivity extends AbsBaseActivity {

	private LinearLayout bbsView;
	private LinearLayout ll_two;
	private BBSSmallLayout infoLayout;
	private BBSSmallLayout groupLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	
		setContentView(R.layout.bbs_main);
		initBase();
		bbsView = (LinearLayout) this.findViewById(R.id.bbscontainer);
		
		initBBSUI();
	}

	private void initBBSUI() {
		int width = 0;
		
		ll_two = new LinearLayout(this);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(5, 7, 5, 5);
		ll_two.setLayoutParams(params);		
		ll_two.setOrientation(LinearLayout.HORIZONTAL);		
		
		infoLayout = new BBSSmallLayout(this);
		width = infoLayout.getLayout().getWidth();
		infoLayout.setBackgroundResource(R.color.bbs_menu_blue);
		infoLayout.setIcon(R.drawable.bbs_menu_xinxiliu);
		infoLayout.setCnName(getResources().getString(R.string.bbs_info_message));
		infoLayout.getView().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				Toast.makeText(getApplicationContext(), "信息流", 0).show();
				Intent infoIntent = new Intent(BBSMainActivity.this,BBSMessageListActivity.class);
				startActivity(infoIntent);
			}
		});
		
		
		ll_two.addView(infoLayout.getView());		
		
		groupLayout = new BBSSmallLayout(this);
		groupLayout.setBackgroundResource(R.color.white);
		groupLayout.setIcon(R.drawable.bbs_menu_quanzi);
		((TextView)groupLayout.getView().findViewById(R.id.tv_cnName)).setTextColor(0xff3ecfff);
		groupLayout.setCnName(getResources().getString(R.string.bbs_info_circle));
		
		groupLayout.getView().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				Toast.makeText(getApplicationContext(), "圈子", 0).show();
				Intent groupIntent = new Intent(BBSMainActivity.this,BBSGroupActivity.class);
				startActivity(groupIntent);
			}
		});

		ll_two.addView(groupLayout.getView());		
		
		bbsView.addView(ll_two);
		
		initResolution();
		bbsView.requestLayout();
		bbsView.invalidate();
	}
	
	public void initResolution(){
		TextView infoText = (TextView) infoLayout.getView().findViewById(R.id.tv_cnName);
		TextView groupText = (TextView) groupLayout.getView().findViewById(R.id.tv_cnName);
		int resolution = getResolution();
		switch (resolution) {
		case Constants.RESOLUTION_320_240:
			infoText.setTextSize(22);
			groupText.setTextSize(22);
			break;
		case Constants.RESOLUTION_480_320:
			infoText.setTextSize(22);
			groupText.setTextSize(22);
			break;
		case Constants.RESOLUTION_800_480:
			infoText.setTextSize(22);
			groupText.setTextSize(22);
			break;
		case Constants.RESOLUTION_854_480:
			infoText.setTextSize(22);
			groupText.setTextSize(22);
			break;
		default:
			infoText.setTextSize(22);
			groupText.setTextSize(22);
			break;
		}
		
		LinearLayout info = infoLayout.getLayout();
		LinearLayout group = groupLayout.getLayout();
		LinearLayout.LayoutParams infoParams = (LinearLayout.LayoutParams)info.getLayoutParams();
		LinearLayout.LayoutParams groupParams = (LinearLayout.LayoutParams)group.getLayoutParams();
		int width = this.getWindowManager().getDefaultDisplay().getWidth();
		infoParams.height = (width - 30)/2;
		groupParams.height = (width - 30)/2;		
		info.setLayoutParams(infoParams);
		group.setLayoutParams(groupParams);	
		
	}	
	
	public int getResolution(){
		DisplayMetrics disMetrics = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(disMetrics);
		int widthPixels=disMetrics.widthPixels;
		int heightPixels=disMetrics.heightPixels;		
		
		if(heightPixels==320 && widthPixels==240){
			return Constants.RESOLUTION_320_240;
		}else if(heightPixels==480 && widthPixels==320){
			return Constants.RESOLUTION_480_320;
		}else if(heightPixels==800 && widthPixels==480){
			return Constants.RESOLUTION_800_480;
		}else if(heightPixels==854 && widthPixels==480){
			return Constants.RESOLUTION_854_480;
		}else{
			return Constants.RESOLUTION_480_320;
		}
	}
}
