package com.yunhu.yhshxc.wechat.content;

import java.io.File;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.wechat.Util.SharedPrefrencesForWechatUtil;

public class VideoView implements OnClickListener{
	
	private Context context;
	private View view;
	private ContentControlListener contentControlListener;
	private ImageView iv_delete_video_items;
	private ImageButton ib_delete_video_items;
	private TextView tv_delete_video_items;
	private Boolean isHave = false;//判断删除按钮的显示、隐藏
	private String filePath;
	private LinearLayout ll_video;
	public VideoView(Context context) {
		this.context = context;
		view = View.inflate(context, R.layout.activity_wechat_content_videoview, null);
		iv_delete_video_items = (ImageView) view.findViewById(R.id.iv_delete_video_items);
		ib_delete_video_items = (ImageButton) view.findViewById(R.id.ib_delete_video_items);
		tv_delete_video_items = (TextView) view.findViewById(R.id.tv_delete_video_items);
		ll_video = (LinearLayout)view.findViewById(R.id.ll_video);
		iv_delete_video_items.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				if (isHave) {
					ib_delete_video_items.setVisibility(View.GONE);
					isHave = false;
				} else {
					ib_delete_video_items.setVisibility(View.VISIBLE);
					isHave = true;
				}
				
				return false;
			}
		});
		
		ll_video.setOnClickListener(this);
	}
	
	
	public String getFilePath() {
		return filePath;
	}


	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}


	public void setVideoView(Video video) {
		String name = SharedPrefrencesForWechatUtil.getInstance(context).getGetFileName(video.getName());
		tv_delete_video_items.setText(TextUtils.isEmpty(name)?video.getName():name);
		ib_delete_video_items.setOnClickListener(this);
		
	}
	
	public View getView() {
		return view;
	}
	
	public void setContentControlListener(
			ContentControlListener contentControlListener) {
		this.contentControlListener = contentControlListener;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_delete_video_items:
			showDialog();		
//			contentControlListener.contentVideoControl(this);
			break;
		case R.id.ll_video:
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
						contentControlListener.contentVideoControl(VideoView.this);
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
		File file = new File(filePath);
		if (file.exists()) {
			 Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
			 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			 intent.putExtra("oneshot", 0);
			  intent.putExtra("configchange", 0);
			  Uri uri = Uri.fromFile(file);
			 intent.setDataAndType(uri, "video/*");
		     context.startActivity(intent);
		}
	}
}
