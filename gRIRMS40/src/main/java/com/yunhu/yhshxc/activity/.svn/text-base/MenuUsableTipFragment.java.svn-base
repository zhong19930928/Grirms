package com.yunhu.yhshxc.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;

/**
 * 手机段可使用时间段提示
 * @author gcg_jishen
 *
 */
public class MenuUsableTipFragment extends Fragment {
	private LinearLayout ll=null;
	
	private View menuView;
	private TextView tipTv;
	private String tipInfo;//提示内容
	private boolean isLeft;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = View.inflate(this.getActivity(),R.layout.menu_usable_tip_fragment, null);
		ll = (LinearLayout)view.findViewById(R.id.ll_menu_usable_tip);
		tipTv = (TextView)view.findViewById(R.id.tv_menu_usable_tip);
		tipTv.setText(tipInfo);
		return view;
	}
	
	public void setTipInfo(String info){
		this.tipInfo = info;
	}
//	@Override
//	public void onResume() {
//		super.onResume();
//		Rect globeRect = new Rect(); 
//		menuView.getGlobalVisibleRect(globeRect);
//		setTipFragment(isLeft, globeRect.left, globeRect.top);
//		
//	}
	
//	public void setTipFragment(boolean isLeft,int left,final int top){
//		if (isLeft) {
//			tipTv.setBackgroundResource(R.drawable.chatto_bg_focused);
//		}else{
//			tipTv.setBackgroundResource(R.drawable.chatto_bg_focused);
//		}
//		tipTv = (TextView)this.getView().findViewById(R.id.tv_menu_usable_tip);
//		tipTv.setText(tipInfo);
//		ll = (LinearLayout)this.getView().findViewById(R.id.ll_menu_usable_tip);
//		WindowManager wm = (WindowManager) this.getActivity().getSystemService(Context.WINDOW_SERVICE);
//		int height = wm.getDefaultDisplay().getHeight();//屏幕高度
//		ll.setPadding(30, 0, 30, height-top);
//	}
	
	
//	public void setMenuView(View view,String tipInfo,boolean isLeft){
//		this.menuView = view;
//		this.tipInfo = tipInfo;
//		this.isLeft = isLeft;
//	}
//	
//	public int high(){
//		int widthSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
//		int heightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
//		ll.measure(widthSpec, heightSpec);
//		int height = ll.getMeasuredHeight();
//		return height;
//	}
}
