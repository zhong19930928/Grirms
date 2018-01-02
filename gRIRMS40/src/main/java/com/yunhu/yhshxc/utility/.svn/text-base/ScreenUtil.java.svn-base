package com.yunhu.yhshxc.utility;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

public class ScreenUtil {
	private DisplayMetrics dm;
	private int screenWidth, screenHeight;

	public ScreenUtil(Context context) {
		dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);

		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;

		Log.d("宽==》", String.valueOf(screenWidth));
		Log.d("高==》", String.valueOf(screenHeight));

	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public int getWidth(float ratio, int currentScreenWidth) {
		int width = 0;
		
		width = (int) (ratio * currentScreenWidth);
		Log.d("比率==》", String.valueOf(ratio));
		Log.d("转换后的宽==》", String.valueOf(width));
		return width;
	}
	public int getHeight(float ratio, int currentScreenHeight) {
		int heigh = 0;
		
		heigh = (int) (ratio * currentScreenHeight);
		Log.d("比率==》", String.valueOf(ratio));
		Log.d("转换后的高==》", String.valueOf(heigh));
		return heigh;
	}
}
