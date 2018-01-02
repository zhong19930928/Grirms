package com.yunhu.yhshxc.comp.menu;

import gcg.org.debug.JLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.utility.Constants;

/**
 * 预览view
 * @author jishen
 *
 */
public class RecordPreviewDataComp extends Menu implements OnClickListener{
	private final String TAG="RecordPreviewDataComp";
	private TextView showDataTitle;
	private TextView tv_time;
	private View view;
	private Button playBtn;//播放 暂停按钮
	private LinearLayout ll_bar;
	private final int STATE_PLAY=1;//播放状态
	private final int STATE_PAUSE=0;//暂停状态
	private final int STATE_OVER=2;//播放完毕
	private int CURRENTSTATE;//当前状态
	private MediaPlayer mediaPlayer=null;//播放器
	private File audioFile;//录音文件
	/** Timer对象 **/
	private Timer timer = null; // 录音计时
	private int time;// 录音时间
	private Context mContext;
	
	public RecordPreviewDataComp(Context context,String value) {
		this.mContext=context;
		view = View.inflate(context, R.layout.record_priview_data_comp, null);
		showDataTitle = (TextView) view.findViewById(R.id.tv_record_priview_title);
		tv_time = (TextView) view.findViewById(R.id.tv_record_priview_time);
		playBtn=(Button)view.findViewById(R.id.btn_record_play);
		playBtn.setOnClickListener(this);
		ll_bar=(LinearLayout)view.findViewById(R.id.ll__record_priview_bar);
		if(!TextUtils.isEmpty(value)){
			audioFile=new File(Constants.RECORD_PATH+value);
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
		try {
			if(CURRENTSTATE==STATE_PLAY){
				playBtn.setText(mContext.getResources().getString(R.string.play1));
				pause();
			}else{
				play();
				playBtn.setText(mContext.getResources().getString(R.string.suspend));
			}
		} catch (Exception e) {
			JLog.d("jishen", e.getMessage());
			stopPlay();
		}
	}
	
	/**
	 * 播放录音
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	private void play() throws IllegalArgumentException, IllegalStateException, IOException{
		if (audioFile != null) {
			CURRENTSTATE=STATE_PLAY;
			if(mediaPlayer==null){
				mediaPlayer = new MediaPlayer();
				FileInputStream fis = new FileInputStream(audioFile); 
				mediaPlayer.setDataSource(fis.getFD());
				mediaPlayer.prepare();
			}
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					CURRENTSTATE=STATE_OVER;
					time=0;
					playBtn.setText(mContext.getResources().getString(R.string.play1));
					tv_time.setText("");
				}
			});
			mediaPlayer.start();
			startTimer(100);
			mediaPlayer.setOnErrorListener(new OnErrorListener() {
				
				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					JLog.d(TAG, "播放失败 setOnErrorListener");
					Toast.makeText(mContext, mContext.getResources().getString(R.string.play_fails), Toast.LENGTH_SHORT).show();
					stopPlay();
					return false;
				}
			});
			
		}
	}
	
	
	/**
	 * 停止播放
	 */
	private void stopPlay(){
		CURRENTSTATE=STATE_OVER;
		mediaPlayer.pause();
		playBtn.setText(mContext.getResources().getString(R.string.play1));
		tv_time.setText("");
		if (mediaPlayer != null) {// 释放播放器
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}
	
	
	/**
	 * 暂停录音
	 */
	private void pause(){
		if(mediaPlayer!=null && mediaPlayer.isPlaying()){
			CURRENTSTATE=STATE_PAUSE;
			mediaPlayer.pause();
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
				time=mediaPlayer.getCurrentPosition()/1000;
				tv_time.post(new Runnable() {
					@Override
					public void run() {
						tv_time.setText(time + "  ＂");//设置播放时间显示
						if (CURRENTSTATE==STATE_OVER||CURRENTSTATE==STATE_PAUSE) {//播放完暂停的时候停止计时器
							cancelTimer();
							if(CURRENTSTATE==STATE_OVER){//播放完以后
								time=0;
								tv_time.setText("");
							}
						}
					}
				});
			}
		};
	}
}
