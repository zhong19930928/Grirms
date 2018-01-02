package com.yunhu.yhshxc.comp.menu;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.utility.Constants;

/**
 * 预览view
 * @author jishen
 *
 */
public class VideoPreviewDataComp extends Menu implements OnClickListener{
	private final String TAG="VideoPreviewDataComp";
	private TextView showDataTitle;
	private View view;
	private Button playBtn;//播放 暂停按钮
	private LinearLayout ll_bar;
	
	private File audioFile;//录音文件
	private Context mContext;
	
	public VideoPreviewDataComp(Context context,String value) {
		this.mContext=context;
		view = View.inflate(context, R.layout.video_priview_data_comp, null);
		showDataTitle = (TextView) view.findViewById(R.id.tv_record_priview_title);
		playBtn=(Button)view.findViewById(R.id.btn_record_play);
		playBtn.setOnClickListener(this);
		ll_bar=(LinearLayout)view.findViewById(R.id.ll__record_priview_bar);
		if(!TextUtils.isEmpty(value)){
			audioFile=new File(Constants.VIDEO_PATH+value);
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
		if (audioFile.exists()) {
			 Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
			 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			 intent.putExtra("oneshot", 0);
			  intent.putExtra("configchange", 0);
			  Uri uri = Uri.fromFile(audioFile);
			 intent.setDataAndType(uri, "video/*");
		     mContext.startActivity(intent);
		}
	}
	
}
