package com.yunhu.yhshxc.help;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.PublicUtils;

/**
 * 显示帮助用的弹出窗口
 * 此类是基于android.widget.PopupWindow构造的，封装了帮助窗口所需要的相关组件
 * 
 * @version 2013.5.22
 */
public class HelpPopupWindow {
	/**
	 * 用于显示帮助的弹出窗口
	 */
	private PopupWindow popupWindow = null;
	private Context context = null;

	/**
	 * 构造方法
	 */
	public HelpPopupWindow(Context context) {
		this.context = context;
	}

	/**
	 * 显示帮助的弹出窗口，基于R.layout.help_popupwindow来构造界面
	 * 
	 * @param anchor 弹出窗口所基于的view。如果anchor为null，则基于R.id.gridview_home组件的位置显示，
	 * 否则就基于anchor组件的位置显示
	 */
	public void show(View anchor) {
		View contentView = View.inflate(context, R.layout.help_popupwindow, null);
		contentView.findViewById(R.id.ll_help_instruction).setOnClickListener(clickListener);//操作说明
		contentView.findViewById(R.id.ll_help_question).setOnClickListener(clickListener);//常见问题

		//根据屏幕高度/2.5的值来设置弹出窗口的高度
		int pwHeight = PublicUtils.convertPX2DIP(context, (Double.valueOf(Math.floor(Constants.SCREEN_HEIGHT / 2.5))).intValue());
		popupWindow = new PopupWindow(contentView, WindowManager.LayoutParams.FILL_PARENT, pwHeight, true);
		popupWindow.setAnimationStyle(R.style.popupwindow);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.update();//更新弹出窗口的状态
		popupWindow.setBackgroundDrawable(new BitmapDrawable());//设置一个默认的背景
		
		//设置弹出窗口的位置（基于哪个背景层组件显示）
		if (anchor == null) {
			// 设置弹出窗口的位置为水平居中且对齐R.id.gridview_home组件的底部
			popupWindow.showAtLocation(((Activity) context).findViewById(R.id.gridview_home), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); 
		}
		else {
			//根据设置弹出窗口的位置
//			int y = Constants.SCREEN_HEIGHT - anchor.getHeight() - pwHeight;
			int y=anchor.getHeight();//改为从底部弹出
			popupWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, 0, y);
		}

	}

	/**
	 * 操作说明和常见问题按钮的事件处理
	 */
	private final View.OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = null;
			switch (v.getId()) {
				//操作说明
				case R.id.ll_help_instruction:
					//打开操作说明的Activity
					intent = new Intent(context, HelpInstructionsActivity.class);
					context.startActivity(intent);
					break;
					
				//常见问题
				case R.id.ll_help_question:
					//打开常见问题Activity
					intent = new Intent(context, QuestionActivity.class);
					context.startActivity(intent);
					break;
			}
			close();//关闭弹出窗口
		}
	};

	/**
	 * 关闭弹出窗口
	 */
	public void close() {
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
		}
	}
}
