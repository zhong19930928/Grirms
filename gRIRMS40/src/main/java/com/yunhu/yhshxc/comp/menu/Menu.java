package com.yunhu.yhshxc.comp.menu;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class Menu {
	
	protected ImageView iv_icon;
	
	protected TextView tv_cnName;
	
	protected TextView tv_enName;
	
	public abstract View getView();
	
	public void setIcon(int resId){
		if(iv_icon != null){
			iv_icon.setImageResource(resId);
		}
	}

	public void setCnName(String cnName){
		if(tv_cnName != null){
			tv_cnName.setText(cnName);
		}
	}
	public void setEnName(String enName){
		if(tv_enName != null){
			tv_enName.setText(enName);
		}
	}
	
	public abstract void setBackgroundResource(int resid);


}
