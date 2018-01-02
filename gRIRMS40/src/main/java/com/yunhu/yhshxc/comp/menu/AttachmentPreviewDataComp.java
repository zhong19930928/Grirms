package com.yunhu.yhshxc.comp.menu;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.utility.Constants;

import java.io.File;

/**
 * 预览view
 * @author jishen
 *
 */
public class AttachmentPreviewDataComp extends Menu implements OnClickListener{
	private final String TAG="AttachmentPreviewDataComp";
	private TextView showDataTitle;
	private View view;
	private Button playBtn;//播放 暂停按钮
	private LinearLayout ll_bar;
	
	private File audioFile;//录音文件
	private Context mContext;
	
	public AttachmentPreviewDataComp(Context context,String value) {
		this.mContext=context;
		view = View.inflate(context, R.layout.attachment_priview_data_comp, null);
		showDataTitle = (TextView) view.findViewById(R.id.tv_record_priview_title);
		playBtn=(Button)view.findViewById(R.id.btn_record_play);
		playBtn.setOnClickListener(this);
		ll_bar=(LinearLayout)view.findViewById(R.id.ll__record_priview_bar);
		if(!TextUtils.isEmpty(value)){
			audioFile=new File(Constants.FUNC_ATTACHMENT_PATH+value);
		}
	}

	public void setShowDataTitle(String title) {
		showDataTitle.setText(title);
	}

	@Override
	public View getView() {
		return view;
	}

	@Override
	public void setBackgroundResource(int resid) {
		ll_bar.setBackgroundResource(resid);
	}

	@Override
	public void onClick(View v) {
		if (audioFile == null || !audioFile.exists()){
			Toast.makeText(mContext, mContext.getResources().getString(R.string.no_file), Toast.LENGTH_SHORT).show();
		}else{
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
		//		bundle.putString(OPEN_MODE, READ_ONLY);       
		//		 //打开模式
		//		bundle.putBoolean(SEND_CLOSE_BROAD, true);         
		//		 //关闭时是否发送广播   
		//		bundle.putString(THIRD_PACKAGE, selfPackageName); 
		//          //第三方应用的包名，用于对改应用合法性的验证
				//清除打开记录
				bundle.putBoolean("ClearTrace", true);
				//关闭后删除打开文件
				intent.putExtras(bundle);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setAction(android.content.Intent.ACTION_VIEW);
				intent.setClassName("cn.wps.moffice_eng", "cn.wps.moffice.documentmanager.PreStartActivity2");
				Uri uri = Uri.fromFile(audioFile);
				intent.setData(uri);
				try {
					mContext.startActivity(intent);
				}catch (ActivityNotFoundException e){
					e.printStackTrace();
					Toast.makeText(mContext, mContext.getResources().getString(R.string.install_wpsoffice), Toast.LENGTH_SHORT).show();
				}
			}
	}
	
}
