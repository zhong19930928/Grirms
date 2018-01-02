package com.yunhu.yhshxc.comp;

import android.content.Context;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.view.View;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Func;

public class DoubleDetailItem {

	private Context mContext;
	private View view;

	private TextView tv_title;
	private TextView tv_content;
	private SpannableString msp = null;
	private String title, content;
	private Float titeSize;
	private StringBuffer sb;
	private Func func;
	public DoubleDetailItem(Context context) {
		mContext = context;
		view = View.inflate(context, R.layout.double_detail_item, null);
		tv_title = (TextView) view.findViewById(R.id.double_detail_item_title);
		tv_content = (TextView) view.findViewById(R.id.double_detail_item_content);
		sb=new StringBuffer();
	}


	public View getView() {
		return view;
	}

	/**
	 * 设置title
	 * @param title title显示的内容
	 */
	public void setTitle(String title) {
		this.title=title;
		this.tv_title.setText(title);
	}

	/**
	 * 设置内容
	 * @param content 要显示的内容
	 * @param func 当前控件
	 */
	public void setContent(String content,Func func) {
		this.func=func;
		this.content=content;
		if(func!=null && func.getCheckType()!=null && func.getCheckType()==Func.CHECK_TYPE_MOBILE_TELEPHONE){
			tv_content.setAutoLinkMask(Linkify.PHONE_NUMBERS);//自动转手机号码点击它可进入系统拨号界面
		}
		this.tv_content.setText(content);
		
	}
	
	/**
	 * 设置title的大小
	 * @param size 大小
	 * @return
	 */
	public Float setTitleSize(Float size){
		this.titeSize=size;
		return size;
	}
	
}
