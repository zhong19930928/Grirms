package com.yunhu.yhshxc.module.bbs;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.comp.menu.Menu;

public class BBSSmallLayout extends Menu {

	private Context mContext;
	private View view;	
	private LinearLayout ll_small;

	public BBSSmallLayout(Context context) {

		mContext = context;
		view = View.inflate(mContext, R.layout.layout_small_bbs, null);				
		view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT, 1.0f));
		iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
		tv_cnName = (TextView) view.findViewById(R.id.tv_cnName);
		ll_small = (LinearLayout) view.findViewById(R.id.ll_small);
	}

	@Override
	public View getView() {
		return view;
	}

	@Override
	public void setBackgroundResource(int resid) {
		ll_small.setBackgroundResource(resid);
	}
	
	public LinearLayout getLayout(){
		return ll_small;
	}
}
