package com.yunhu.yhshxc.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;

/**
 * @author XZQ
 * @version
 */
public class MyProgressDialog extends Dialog {

    public Context context;// 上下文
    private TextView tv_msg;
    public MyProgressDialog(Context context) {
        super(context);
        this.context = context;
    }

    public MyProgressDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    public MyProgressDialog(final Context context, int theme,String content) {
        super(context, theme);
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.load, null); // 加载自己定义的布局
        if(!TextUtils.isEmpty(content)){
        	 tv_msg=(TextView)view.findViewById(R.id.tv_msg);
        	 tv_msg.setText(content);
        }
        final ImageView img_loading = (ImageView) view.findViewById(R.id.img_load);
        RelativeLayout img_close = (RelativeLayout) view.findViewById(R.id.img_cancel);
        RotateAnimation rotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(context, R.anim.refresh); // 加载XML文件中定义的动画
        img_loading.setAnimation(rotateAnimation);// 开始动画
        setContentView(view);// 为Dialoge设置自己定义的布局
        //对关闭按钮添加点击事件
        img_close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });
        this.setOnShowListener(new OnShowListener() {
			
			@Override
			public void onShow(DialogInterface dialog) {
			     RotateAnimation rotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(context, R.anim.refresh); // 加载XML文件中定义的动画
			        img_loading.setAnimation(rotateAnimation);// 开始动画
			}
		});
        setCancelable(false);
    }
}
