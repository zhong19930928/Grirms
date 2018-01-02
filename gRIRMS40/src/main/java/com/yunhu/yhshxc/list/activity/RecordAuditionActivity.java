package com.yunhu.yhshxc.list.activity;

import gcg.org.debug.JLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.widget.ToastOrder;

/**
 * 查询的时候在线听录音
 * @author gcg_jishen
 */
public class RecordAuditionActivity extends Activity {
	private final String TAG = "RecordAuditionActivity";
	private String url;// 播放地址
	private ImageView iv_record_audition_view;
	private TextView tv_record_auditio_title;
	private int CURRENTRECORDSTATE;// 当前播放状态
	private final int CURRENTRECORDSTATE_STATE_PLAY = 1;// 当前播放状态 播放
	private final int CURRENTRECORDSTATE_STATE_PAUSE = 2;// 当前播放状态 暂停
	private boolean isLoc;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record_audition);
		url = getIntent().getStringExtra("url");
		isLoc = getIntent().getBooleanExtra("isLoc", false);
		iv_record_audition_view = (ImageView) findViewById(R.id.iv_record_audition_view);
		tv_record_auditio_title = (TextView) findViewById(R.id.tv_record_auditio_title);
		iv_record_audition_view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					if (isLoc) {
						audioFile = new File(url);
						playLocRecord();
					}else{
						playFromNetwork();
					}
				} catch (Exception e) {
					e.printStackTrace();
					if (audioFile!=null) {
						audioFile.delete();
						RecordAuditionActivity.this.finish();
						ToastOrder.makeText(RecordAuditionActivity.this, setString(R.string.activity_after_09), ToastOrder.LENGTH_SHORT).show();
					}
					
				}
			}
		});
	}

	/**
	 * url 录音资源地址
	 */
	private MediaPlayer playerNet = null;

	private void playFromNetwork() {
		try {
			if (CURRENTRECORDSTATE == 0) {// 初始状态
				playRecord(url);
			} else if (CURRENTRECORDSTATE == CURRENTRECORDSTATE_STATE_PAUSE) {// 暂停状态
				playFromPause();
			} else if (CURRENTRECORDSTATE == CURRENTRECORDSTATE_STATE_PLAY) {// 播放状态
				pause();
			}
		} catch (Exception e) {
			JLog.d(TAG, "playFromNetwork");
			stopPlay();
		}
	}

	/**
	 * 播放音频件
	 * 
	 * @param url网络地址
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	private void playRecord(String url) throws IllegalArgumentException,IllegalStateException, IOException {
		if (playerNet == null) {
			playerNet = new MediaPlayer();
			playerNet.setDataSource(url);
			tv_record_auditio_title.post(new Runnable() {
				
				@Override
				public void run() {
					tv_record_auditio_title.setText(setString(R.string.activity_after_04));
					
				}
			});
			playerNet.prepareAsync();
			playerNet.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
					public void onPrepared(MediaPlayer player) {
						playerNet.start();
						CURRENTRECORDSTATE = CURRENTRECORDSTATE_STATE_PLAY;
						tv_record_auditio_title.setText(setString(R.string.activity_after_12));
						iv_record_audition_view.setBackgroundResource(R.drawable.record_pause_btn);
					}
			});
		}

		playerNet.setOnErrorListener(new OnErrorListener() {
			
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				JLog.d(TAG, "播放失败 setOnErrorListener what:"+what+"extra:"+extra);
				ToastOrder.makeText(RecordAuditionActivity.this, setString(R.string.activity_after_09), ToastOrder.LENGTH_SHORT).show();
				stopPlay();
				return false;
			}
		});
		
		playerNet.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				playerNet.reset();
				playerNet=null;
				CURRENTRECORDSTATE = 0;
				tv_record_auditio_title.setText(setString(R.string.activity_after_08));
				iv_record_audition_view.setBackgroundResource(R.drawable.record_play_btn);
			}
		});
	}
	
	
	/**
	 * 播放录音
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	private File audioFile;//录音文件
	private final int STATE_PLAY=1;//播放状态
	private final int STATE_PAUSE=0;//暂停状态
	private final int STATE_OVER=2;//播放完毕
	private int CURRENTSTATE;//当前状态
	private MediaPlayer mediaPlayer=null;//播放器
	private Timer timer = null; // 录音计时
	private int time;// 录音时间
	
//	private void playLoc() throws IllegalArgumentException, IllegalStateException, IOException{
//		if (audioFile != null) {
//			CURRENTSTATE=STATE_PLAY;
//			if(mediaPlayer==null){
//				mediaPlayer = new MediaPlayer();
//				FileInputStream fis = new FileInputStream(audioFile); 
//				mediaPlayer.setDataSource(fis.getFD());
//				mediaPlayer.prepare();
////				mediaPlayer.prepareAsync();
//			}
//			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
//				@Override
//				public void onCompletion(MediaPlayer mp) {
//					CURRENTSTATE=STATE_OVER;
//					time=0;
////					playBtn.setText("播放");
//					tv_record_auditio_title.setText("点击播放");
//				}
//			});
//			mediaPlayer.start();
//			startTimer(100);
//			mediaPlayer.setOnErrorListener(new OnErrorListener() {
//				
//				@Override
//				public boolean onError(MediaPlayer mp, int what, int extra) {
//					JLog.d(TAG, "播放失败 setOnErrorListener");
//					Toast.makeText(RecordAuditionActivity.this, "播放失败", Toast.LENGTH_SHORT).show();
//					stopPlay();
//					return false;
//				}
//			});
//			
//		}
//	}
	

	public int CURRENT_RECORD_STATE;// 当前录音的状态 默认0未录音
	private final int RECORDIND = 1;// 正在录音
	public final int RECORD_STOP = 2;// 录音完毕
	private final int RECORD_PLAY = 3;// 播放录音
	private final int RECORD_PAUSE = 4;// 暂停播放录音
	
	private void playLocRecord() throws Exception, SecurityException,IllegalStateException, IOException {
		if (audioFile != null) {
			CURRENTSTATE=STATE_PLAY;
			tv_record_auditio_title.setText(setString(R.string.activity_after_13));
			iv_record_audition_view.setBackgroundResource(R.drawable.record_play_btn);
			if(mediaPlayer==null){
				mediaPlayer = new MediaPlayer();
				FileInputStream fis = new FileInputStream(audioFile); 
				mediaPlayer.setDataSource(fis.getFD());
				JLog.d(TAG, "path:"+audioFile.getAbsolutePath());
				mediaPlayer.prepare();
			}
			
			mediaPlayer.start();
			
			mediaPlayer.setOnErrorListener(new OnErrorListener() {
				
				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					JLog.d(TAG, "播放失败 setOnErrorListener what:"+what+"extra:"+extra);
					playExection();
					return false;
				}
			});
			
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					JLog.d(TAG, "播放完毕 setOnCompletionListener");
					CURRENT_RECORD_STATE=RECORD_STOP;
					iv_record_audition_view.setBackgroundResource(R.drawable.record_play_btn);
					tv_record_auditio_title.setText(setString(R.string.activity_after_14));
				}
			});

		}
	}
	
	/**
	 * 停止播放
	 */
	private void playExection(){
		CURRENT_RECORD_STATE = RECORD_STOP;// 停止录音
		iv_record_audition_view.setBackgroundResource(R.drawable.record_play_btn);
		tv_record_auditio_title.setText(setString(R.string.activity_after_10));
		ToastOrder.makeText(this, setString(R.string.activity_after_11), ToastOrder.LENGTH_SHORT).show();
		if (mediaPlayer != null) {// 释放播放器
//			mediaPlayer.reset();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}
	 
	/**
	 * 开始计时器
	 * 
	 * @param intervalTime间隔时间
	 */
	private void startTimer(long intervalTime) {
		cancelTimer();
		timer = new Timer();
		timer.schedule(getTask(), 0, intervalTime);
	}

	/**
	 * 关闭计时器
	 */
	private void cancelTimer() {
		if (timer != null) {
			timer.cancel();
		}
	}

	/**
	 * 录音执行任务
	 * 
	 * @return
	 */
	public TimerTask getTask() {
		return new TimerTask() {
			@Override
			public void run() {
//				time++;
				if (mediaPlayer!=null) {
					time=mediaPlayer.getCurrentPosition()/1000;
				}
				tv_record_auditio_title.post(new Runnable() {
					@Override
					public void run() {
						tv_record_auditio_title.setText(time + "  ＂");//设置播放时间显示
						if (CURRENTSTATE==STATE_OVER||CURRENTSTATE==STATE_PAUSE) {//播放完暂停的时候停止计时器
							cancelTimer();
							if(CURRENTSTATE==STATE_OVER){//播放完以后
								time=0;
								tv_record_auditio_title.setText(setString(R.string.activity_after_08));
							}
						}
					}
				});
			}
		};
	}
	
	
	/**
	 * 停止播放
	 */
	private void stopPlay(){
		if (playerNet != null) {// 释放播放器
			playerNet.release();
			playerNet=null;
			CURRENTRECORDSTATE = 0;
			tv_record_auditio_title.setText(setString(R.string.activity_after_08));
			iv_record_audition_view.setBackgroundResource(R.drawable.record_play_btn);
		}
	}

	/**
	 * 暂停后再播放
	 */
	private void playFromPause(){
		if(CURRENTRECORDSTATE==CURRENTRECORDSTATE_STATE_PAUSE){
			playerNet.start();
			CURRENTRECORDSTATE = CURRENTRECORDSTATE_STATE_PLAY;
			tv_record_auditio_title.setText(setString(R.string.activity_after_12));
			iv_record_audition_view.setBackgroundResource(R.drawable.record_pause_btn);
		}
	}
	/**
	 * 暂停
	 */
	private void pause() {
		if (playerNet != null && playerNet.isPlaying()) {
			CURRENTRECORDSTATE = CURRENTRECORDSTATE_STATE_PAUSE;
			playerNet.pause();
			tv_record_auditio_title.post(new Runnable() {
				
				@Override
				public void run() {
					tv_record_auditio_title.setText(setString(R.string.activity_after_08));
				}
			});
			iv_record_audition_view.setBackgroundResource(R.drawable.record_play_btn);
		}
	}
	
	/**
	 * 暂停
	 */
	private void pauseLoc() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			CURRENTSTATE=STATE_PAUSE;
			mediaPlayer.pause();
			tv_record_auditio_title.post(new Runnable() {
				
				@Override
				public void run() {
					tv_record_auditio_title.setText(setString(R.string.activity_after_08));
				}
			});
			iv_record_audition_view.setBackgroundResource(R.drawable.record_play_btn);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (playerNet != null) {// 释放播放器
			pause();
			playerNet.release();
			playerNet = null;
		}
		if (mediaPlayer != null) {// 释放播放器
			pauseLoc();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		pause();	
	}

	private String setString(int stringId){
		return getResources().getString(stringId);
	}
}
