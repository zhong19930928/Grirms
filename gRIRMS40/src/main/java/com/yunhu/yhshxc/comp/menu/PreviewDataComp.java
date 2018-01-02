package com.yunhu.yhshxc.comp.menu;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;

/**
 * 预览view
 * @author jishen
 */
public class PreviewDataComp extends Menu {
	private TextView showDataTitle;
	private TextView showDataContent;
	private ImageView showDataIcon;
	private ImageView iv_up_down;
	private View view;
	private LinearLayout ll_content;
	private LinearLayout ll_bar;
	public PreviewDataComp(Context context) {
		view = View.inflate(context, R.layout.priview_data_comp, null);
		showDataTitle = (TextView) view.findViewById(R.id.tv_priview_title);
		showDataContent = (TextView) view.findViewById(R.id.tv_priview_content);
		showDataIcon=(ImageView)view.findViewById(R.id.iv_priview_icon);
		iv_up_down=(ImageView)view.findViewById(R.id.iv_priview_up_down);
		ll_bar=(LinearLayout)view.findViewById(R.id.ll_priview_bar);
		ll_content=(LinearLayout)view.findViewById(R.id.ll_priview_content);
		ll_content.setVisibility(View.VISIBLE);
		ll_bar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(ll_content.isShown()){
					setVisible(false);
				}else{
					setVisible(true);
				}
			}
		});
	}

	/**
	 * 控件名称
	 * @param title
	 */
	public void setShowDataTitle(String title) {
		showDataTitle.setText(title);
	}

	/**
	 * 控件内容
	 * @param content
	 */
	public void setShowDataContent(String content) {
		showDataContent.setText(content);
	}
	public void addShowDataContent(View content) {
		showDataContent.setVisibility(View.GONE);
		ll_content.addView(content);
	}

	public void setShowDataIcon(boolean flag,int resid){
		if(flag){
			showDataIcon.setVisibility(View.VISIBLE);
			showDataIcon.setBackgroundResource(resid);
		}
	}
	
	/**
	 * 设置下箭头是否可见
	 * @param isVisible
	 */
	public void setVisible(boolean isVisible){
		if(isVisible){
			ll_content.setVisibility(View.VISIBLE);
			iv_up_down.setBackgroundResource(R.drawable.p_dowm_icon);
		}else{
			ll_content.setVisibility(View.GONE);
			iv_up_down.setBackgroundResource(R.drawable.p_up_icon);
		}
	}
	
	/**
	 * 设置上箭头是否可见
	 * @param flag
	 */
	public void setIsShowUp(boolean flag){
		if(flag){
			iv_up_down.setVisibility(View.VISIBLE);
		}else{
			iv_up_down.setVisibility(View.GONE);
		}
			
	}
	
	@Override
	public View getView() {
		return view;
	}

	@Override
	public void setBackgroundResource(int resid) {
		ll_bar.setBackgroundResource(resid);
	}

}
