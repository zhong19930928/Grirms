package com.yunhu.yhshxc.wechat.content;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.list.activity.RecordAuditionActivity;
import com.yunhu.yhshxc.wechat.Util.SharedPrefrencesForWechatUtil;

public class VoiceView implements OnClickListener{
	
	private Context context;
	private View view;
	private ContentControlListener contentControlListener;
	private ImageView iv_voice_items;
	private Boolean isHave = false;//判断删除按钮的显示、隐藏
	private ImageButton ib_delete_voice_items;
	private TextView tv_voice_name;
	private String filePath;
	private LinearLayout ll_voice;

	public VoiceView(Context context) {
		this.context = context;
		view = View.inflate(context, R.layout.activity_wechat_content_voiceview, null);
		tv_voice_name = (TextView)view.findViewById(R.id.tv_voice_name);
		iv_voice_items = (ImageView) view.findViewById(R.id.iv_voice_items);
		ib_delete_voice_items = (ImageButton) view.findViewById(R.id.ib_delete_voice_items);
		ll_voice = (LinearLayout)view.findViewById(R.id.ll_voice);
		ll_voice.setOnClickListener(this);
		ib_delete_voice_items.setOnClickListener(this);
	}
	
	
	public String getFilePath() {
		return filePath;
	}


	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}


	public void setVoiceView(Voice voice) {
		String name = SharedPrefrencesForWechatUtil.getInstance(context).getGetFileName(voice.getName());
		tv_voice_name.setText(TextUtils.isEmpty(name)?voice.getName():name);
		iv_voice_items.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				if (isHave) {
					ib_delete_voice_items.setVisibility(View.GONE);
					isHave = false;
				} else {
					ib_delete_voice_items.setVisibility(View.VISIBLE);
					isHave = true;
				}
				return false;
			}
		});
		
	}
	
	public View getView() {
		return view;
	}
	
	public void setContentControlListener(
			ContentControlListener contentControlListener) {
//		this.contentControlListener = contentControlListener;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_delete_voice_items:
			showDialog();
			contentControlListener.contentVoiceControl(this);
			break;
		case R.id.ll_voice:
			priview();
			break;

		default:
			break;
		}
	}
	
	
	private void showDialog(){
		new AlertDialog.Builder(context)
		.setMessage(R.string.is_confirm_delete)
		.setPositiveButton(R.string.Confirm,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						contentControlListener.contentVoiceControl(VoiceView.this);
						dialog.dismiss();
					}
				})
		.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						dialog.dismiss();
					}
				}).setCancelable(false).show();
	}
	

	private void priview(){
		Intent intent = new Intent(context, RecordAuditionActivity.class);
		intent.putExtra("url", filePath);
		intent.putExtra("isLoc", true);
		context.startActivity(intent);
	}
}
