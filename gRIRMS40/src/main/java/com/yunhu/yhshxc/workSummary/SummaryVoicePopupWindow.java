package com.yunhu.yhshxc.workSummary;

import java.io.File;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.comp.menu.RecordPopupWindow;
import com.yunhu.yhshxc.utility.Constants;

public class SummaryVoicePopupWindow extends RecordPopupWindow {

	
	private  WorkSummaryCreateActivity exchangeActivity;
	private String fileName;//录音文件名称
	public SummaryVoicePopupWindow(Context mContext) {
		super(mContext);
		exchangeActivity = (WorkSummaryCreateActivity) mContext;
//		isWechatRecord  = true;
//		ll_recod.setBackgroundColor(exchangeActivity.getResources().getColor(R.color.white));
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	/**
	 * 播放本地文件
	 * @param fileName
	 */
	public void playRecod(String fileName){
		ll_say.setVisibility(View.INVISIBLE);
		setFileName(fileName);
	}
	public void setSta(){

	}

	@Override
	public void save(String value) {
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
		}
		exchangeActivity.saveVoice(value);
		pausePlay();
	}

	
	@Override
	public void back() {
		super.back();
	}
	
	
	@Override
	public void init() {
		if(!TextUtils.isEmpty(fileName)){
			audioFile=new File(Constants.RECORD_PATH+fileName);
			if(audioFile.exists()){//如果文件存在
				record_view.setBackgroundResource(R.drawable.record_play_btn);
				record_time.setText(R.string.play);
				CURRENT_RECORD_STATE = RECORD_STOP;
			}
		}
	}
}
