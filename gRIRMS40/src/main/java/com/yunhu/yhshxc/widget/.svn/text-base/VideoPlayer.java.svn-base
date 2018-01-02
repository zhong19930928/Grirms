package com.yunhu.yhshxc.widget;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.utility.PublicUtils;

public class VideoPlayer extends Activity {

	private Uri uri;
	private VideoView mVideoView;
	//private int mPositionWhenPaused = 0;
	private ProgressDialog pd;

	public void onCreate(Bundle icicle) {

		super.onCreate(icicle);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		setContentView(R.layout.videoplay);
		uri = Uri.parse(this.getIntent().getStringExtra("url"));
		pd = new ProgressDialog(this);
		pd.setMessage(PublicUtils.getResourceString(this,R.string.reading_now));
    	pd.show();
		mVideoView = (VideoView) findViewById(R.id.video);
		mVideoView.setVideoURI(uri);
		MediaController controller = new MediaController(this);
		mVideoView.setMediaController(controller);
		mVideoView.requestFocus();
		
		mVideoView.setOnPreparedListener(new OnPreparedListener() {
			// @Override
			public void onPrepared(MediaPlayer mp) {
				pd.dismiss();
			}
		});

		mVideoView.setOnCompletionListener(new OnCompletionListener() {
			// @Override
			public void onCompletion(MediaPlayer mp) {
				
				//Toast.makeText(VideoPlayer.this, "video play finished!",
				//		Toast.LENGTH_LONG)
				//.show();
			}
		});

		controller.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
	}
	
	public void onStart() {
		super.onStart();
		mVideoView.start();
	}

	@Override
	protected void onResume() {
		super.onResume();
		//mVideoView.seekTo(mPositionWhenPaused);
	}
	
	public void onPause() {
		super.onPause();
		//mPositionWhenPaused = mVideoView.getCurrentPosition();
		mVideoView.pause();
		//mVideoView.stopPlayback();
	}

	@Override
	protected void onStop() {
		super.onStop();
		//mVideoView.stopPlayback();
		//mPositionWhenPaused = mVideoView.getCurrentPosition();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
			return true;
		}
		return false;
	}
}
