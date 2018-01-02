package com.yunhu.yhshxc.utility;

import android.content.Context;
import android.widget.Toast;

/**
 * 弹出提示工具类
 * 避免多次点击连续提示的不友好显示
 * @author qingli
 *
 */
public class ToastUtil {

	private static Toast toast;
	
	public static void showText(Context context,String text){
		if (toast==null) {
			
			toast=Toast.makeText(context, text, Toast.LENGTH_SHORT);
		}else{
			toast.setText(text);
		}
		toast.show();
	}
	
}
