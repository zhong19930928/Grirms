package com.yunhu.yhshxc.activity.popup;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.comp.PhotoInTableComp;
import com.yunhu.yhshxc.func.AbsFuncActivity;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.PublicUtils;

/**
 * 此类是基于android.widget.PopupWindow构造的，封装了帮助窗口所需要的相关组件
 */
public class PhotoPopupWindow {
	/**
	 * 用于显示帮助的弹出窗口
	 */
	private PopupWindow popupWindow = null;
	private Context context = null;
	private AbsFuncActivity absFuncActivity;
	private PhotoInTableComp photoInTableComp;
	private boolean isTablePhoto;
	private String photoName;

	/**
	 * 构造方法
	 */
	public PhotoPopupWindow(Context context) {
		this.context = context;
		absFuncActivity = (AbsFuncActivity)context;
	}
	
	/**
	 * 构造方法
	 */
	public PhotoPopupWindow(Context context,PhotoInTableComp photoInTableComp,String photoName) {
		this.context = context;
		this.photoInTableComp = photoInTableComp;
		isTablePhoto = true;
		this.photoName = photoName;
	}
	

	/**
	 * @param anchor 弹出窗口所基于的view。如果anchor为null，则基于R.id.ll_btn_layout组件的位置显示，
	 * 否则就基于anchor组件的位置显示
	 */
	public void show(View anchor) {
		View contentView = View.inflate(context, R.layout.photo_popupwindow, null);
		contentView.findViewById(R.id.ll_photo_album).setOnClickListener(clickListener);//选择相册
		contentView.findViewById(R.id.ll_photo).setOnClickListener(clickListener);//直接拍照

		//根据屏幕高度/2.5的值来设置弹出窗口的高度
		int pwHeight = PublicUtils.convertPX2DIP(context, (Double.valueOf(Math.floor(Constants.SCREEN_HEIGHT / 2))).intValue());
		popupWindow = new PopupWindow(contentView, WindowManager.LayoutParams.FILL_PARENT, pwHeight, true);
		popupWindow.setAnimationStyle(R.style.popupwindow);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.update();//更新弹出窗口的状态
		popupWindow.setBackgroundDrawable(new BitmapDrawable());//设置一个默认的背景
		
		//设置弹出窗口的位置（基于哪个背景层组件显示）
		if (anchor == null) {
			// 设置弹出窗口的位置为水平居中且对齐R.id.gridview_home组件的底部
			popupWindow.showAtLocation(((Activity) context).findViewById(R.id.ll_btn_layout), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); 
		}
		else {
			//根据设置弹出窗口的位置
			int y = Constants.SCREEN_HEIGHT - anchor.getHeight() - pwHeight;
			popupWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, 0, y);
		}
		popupWindow.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
				if (absFuncActivity!=null) {//设置拍照按钮可点击
					absFuncActivity.isCanClick = true;
				}
			}
		});

	}

	/**
	 * 操作说明和常见问题按钮的事件处理
	 */
	private final View.OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				//直接拍照
				case R.id.ll_photo:
					if (isTablePhoto) {
						photoInTableComp.startPhotoTable(photoName);
					}else{
						absFuncActivity.takePhoto(1);
					}
					break;
					
				//选择照片
				case R.id.ll_photo_album:
					if (isTablePhoto) {
						photoInTableComp.startPhotoAlbumTable();
					}else{
						absFuncActivity.takePhoto(2);
					}
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
