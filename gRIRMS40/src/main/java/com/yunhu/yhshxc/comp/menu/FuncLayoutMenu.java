package com.yunhu.yhshxc.comp.menu;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.utility.Constants;

/**
 * 报表控件页面布局
 * @author gcg_jishen
 *
 */
public class FuncLayoutMenu{

	private Context mContext;
	private View view;
	private ImageView iv_check_mark;
	private ImageView iv_icon;
	private TextView tv_cnName;
	private RelativeLayout ll_func_menu;
	private Func func;
	private boolean isReport=false;//是否是报表查询控件
	public FuncLayoutMenu(Context context,Func func,boolean isReport) {
		this.func=func;
		this.isReport=isReport;
		mContext = context;
		view = View.inflate(mContext, R.layout.func_layout_menu, null);
		view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1.0f));
		iv_check_mark = (ImageView) view.findViewById(R.id.iv_func_check_mark);
		iv_icon = (ImageView) view.findViewById(R.id.iv_func_icon);
		tv_cnName = (TextView) view.findViewById(R.id.tv_func_cnName);
		ll_func_menu = (RelativeLayout) view.findViewById(R.id.ll_func_menu);
	}

	public void setVisibilityForCheckMark(boolean isShow) {
		if (isShow) {
			iv_check_mark.setVisibility(View.VISIBLE); // 显示
		} else {
			iv_check_mark.setVisibility(View.INVISIBLE); // 占位隐藏
		}
	}

	public void setFuncName(String name){
		tv_cnName.setText(name);
	}

	public View getView() {

		if(func!=null || isReport){
			ll_func_menu.setOnTouchListener(new OnTouchListener() {
 
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if(event.getAction()==MotionEvent.ACTION_UP || event.getAction()==MotionEvent.ACTION_CANCEL){
						if(isReport){
							iv_icon.setBackgroundResource(setReportIconForTypeNew(func.getType(),false));
						}
						tv_cnName.setTextColor(mContext.getResources().getColor(R.color.func_menu_button_click));
					}else{
						if(isReport){
							iv_icon.setBackgroundResource(setReportIconForTypeNew(func.getType(),true));
						}
						tv_cnName.setTextColor(mContext.getResources().getColor(R.color.white));
					}
					return false;
				}
			});
		}
		return view;
	}

	public void setBackgroundResource(int resid) {
		ll_func_menu.setBackgroundResource(resid);
	}
	
	public void setIcon(int resid){
		iv_icon.setBackgroundResource(resid);
	}
	
	/*
	 * 获取小图标背景图片
	 */
	private int setReportIconForTypeNew(int type,boolean isPress) {
		switch (type) {
		case Constants.FUNC_TYPE_DATE:
		case Constants.FUNC_TYPE_TIME:
		case Constants.FUNC_TYPE_RANGE_TIME:
		case Constants.FUNC_TYPE_RANGE_DATE:
			return R.drawable.f_date;
		case Constants.FUNC_TYPE_TEXT:
//			return R.drawable.f_default;
			return R.drawable.icon_upload;
		default:
			break;
		}
		return R.drawable.f_default;
	}
	
}
