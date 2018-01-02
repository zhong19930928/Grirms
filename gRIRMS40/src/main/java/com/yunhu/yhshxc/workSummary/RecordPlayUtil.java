package com.yunhu.yhshxc.workSummary;

import java.io.IOException;

import android.media.AudioManager;
import android.media.MediaPlayer;

/**
 * 播放录音的工具类
 * 
 * @author qingli
 *
 */
public class RecordPlayUtil {

	private static MediaPlayer mMediaPlayer;

	private static boolean isPause;

	/**
	 * 设置放的路径以及播放完毕的监听
	 * 
	 * @param filePath
	 * @param onCompletionListener
	 */
	public static void playSound(String filePath, MediaPlayer.OnCompletionListener onCompletionListener) {
		if (mMediaPlayer == null) {
			mMediaPlayer = new MediaPlayer();
			mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					mMediaPlayer.reset();
					return false;
				}
			});
		} else {
			mMediaPlayer.reset();
		}
		mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		// 播放完毕
		mMediaPlayer.setOnCompletionListener(onCompletionListener);
		try {

			mMediaPlayer.setDataSource(filePath);
			mMediaPlayer.prepare();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mMediaPlayer.start();
	}

	/**
	 * 暂停
	 */
	public static void pause() {
		if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
			mMediaPlayer.pause();
			isPause = true;
		}
	}

	/**
	 * 由暂停到播放
	 */
	public static void resume() {
		if (mMediaPlayer != null && isPause) {
			mMediaPlayer.start();
			isPause = false;
		}
	}

	/**
	 * 释放资源
	 */
	public static void release() {
		if (mMediaPlayer != null) {
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}

}
