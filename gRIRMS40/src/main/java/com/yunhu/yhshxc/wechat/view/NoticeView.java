package com.yunhu.yhshxc.wechat.view;

import gcg.org.debug.JLog;

import java.text.ParseException;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.wechat.bo.Notification;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class NoticeView {
	private View view;
	private Context context;
	private ImageView iv_weidu;
//	private ImageView iv_notice_fujian;
	private TextView tv_notice_content;
	private TextView tv_notice_bumen;
	private TextView tv_notice_time;
	private TextView tv_notice_content_jieshao;
	public NoticeView(Context context){
		this.context = context;
		view = View.inflate(context, R.layout.wechat_chat_notice, null);
		iv_weidu = (ImageView) view.findViewById(R.id.iv_weidu);
//		iv_notice_fujian = (ImageView) view.findViewById(R.id.iv_notice_fujian);
		tv_notice_content= (TextView) view.findViewById(R.id.tv_notice_content);
		tv_notice_bumen= (TextView) view.findViewById(R.id.tv_notice_bumen);
		tv_notice_time= (TextView) view.findViewById(R.id.tv_notice_time);
		tv_notice_content_jieshao= (TextView) view.findViewById(R.id.tv_notice_content_jieshao);
	}
	public View getView(){
		return view;
	}
	public void initData(Notification data) throws ParseException{
		//判断是否未读，如果未读消息则设置红点
		
		if(data!=null){
			if("0".equals(data.getIsRead())){
				iv_weidu.setVisibility(View.VISIBLE);
			}else{
				iv_weidu.setVisibility(View.INVISIBLE);
			}
			tv_notice_content.setText(data.getTitle());
			tv_notice_bumen.setText(data.getCreater());
//			tv_notice_bumen.setText("总经理");
			String dataTime = PublicUtils.compareDate(data.getCreateDate());
			JLog.d("aaa", "time  = "+data.getCreateDate());
//			tv_notice_time.setText(data.getCreateDate());
			tv_notice_time.setText(dataTime);
			if("1".equals(data.getIsAttach())){//是否有附件
//				iv_notice_fujian.setVisibility(View.VISIBLE);
				Drawable drawable = context.getResources().getDrawable(R.drawable.wechat_fujiangren);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				tv_notice_content.setCompoundDrawables(null, null, drawable, null);
			}else{
//				iv_notice_fujian.setVisibility(View.INVISIBLE);
			}
			tv_notice_content_jieshao.setText(data.getContent());
		}
		
	}
}
