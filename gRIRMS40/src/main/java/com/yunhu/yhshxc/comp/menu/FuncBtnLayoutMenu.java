package com.yunhu.yhshxc.comp.menu;

import java.util.List;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Func;

/**
 * 按钮类型的控件
 * @author jishen
 *
 */
public class FuncBtnLayoutMenu {
	private Context mContext;
	private OnClickListener listener;
	private View view;
	/*
	 * 底部显现的五个button布局 
	 */
	private LinearLayout ll_btn_first;
	private LinearLayout ll_btn_second;
	private LinearLayout ll_btn_third;
	private LinearLayout ll_btn_four;
	private LinearLayout ll_btn_five;

	/*
	 * 底部显示的五个BUTTON图片
	 */
	private ImageView iv_btn_first;
	private ImageView iv_btn_second;
	private ImageView iv_btn_third;
	private ImageView iv_btn_four;
	private ImageView iv_btn_five;

	/*
	 * 底部显示的五个button 文本
	 */
	private TextView tv_btn_first;
	private TextView tv_btn_second;
	private TextView tv_btn_third;
	private TextView tv_btn_four;
	private TextView tv_btn_five;
	public static final int SHOWPRIVIEW_ID=-100;//预览button的ID

	public FuncBtnLayoutMenu(Context context, OnClickListener listener) {
		this.mContext = context;
		this.listener = listener;

		
		view = View.inflate(mContext, R.layout.func_btn_menu, null);
		ll_btn_first = (LinearLayout) view.findViewById(R.id.ll_btn_first);
		ll_btn_second = (LinearLayout) view.findViewById(R.id.ll_btn_second);
		ll_btn_third = (LinearLayout) view.findViewById(R.id.ll_btn_third);
		ll_btn_four = (LinearLayout) view.findViewById(R.id.ll_btn_four);
		ll_btn_five = (LinearLayout) view.findViewById(R.id.ll_btn_five);

		iv_btn_first = (ImageView) view.findViewById(R.id.iv_btn_first);
		iv_btn_second = (ImageView) view.findViewById(R.id.iv_btn_second);
		iv_btn_third = (ImageView) view.findViewById(R.id.iv_btn_third);
		iv_btn_four = (ImageView) view.findViewById(R.id.iv_btn_four);
		iv_btn_five = (ImageView) view.findViewById(R.id.iv_btn_five);

		iv_btn_first.setVisibility(View.GONE);
		iv_btn_second.setVisibility(View.GONE);
		iv_btn_third.setVisibility(View.GONE);
		iv_btn_five.setVisibility(View.GONE);
		iv_btn_four.setVisibility(View.GONE);
		
		tv_btn_first = (TextView) view.findViewById(R.id.tv_btn_first);
		tv_btn_second = (TextView) view.findViewById(R.id.tv_btn_second);
		tv_btn_third = (TextView) view.findViewById(R.id.tv_btn_third);
		tv_btn_four = (TextView) view.findViewById(R.id.tv_btn_four);
		tv_btn_five = (TextView) view.findViewById(R.id.tv_btn_five);

	}
     
	/**
	 * 根据button的个数控制button view的显示最多显示5个
	 * @param buttonFuncList button的控件的集合
	 * @return
	 */
	public View getView(List<Func> buttonFuncList) {
		if (buttonFuncList != null && !buttonFuncList.isEmpty()) {
			switch (buttonFuncList.size()) {
			case 1://显示最后一个

				showFive(buttonFuncList.get(0),0);
				ll_btn_first.setVisibility(View.GONE);
				ll_btn_second.setVisibility(View.GONE);
				ll_btn_third.setVisibility(View.GONE);
				ll_btn_four.setVisibility(View.GONE);
				ViewGroup.LayoutParams params = ll_btn_five.getLayoutParams();
				params.width = ViewGroup.LayoutParams.MATCH_PARENT;
				params.height = params.height/2;
				ll_btn_five.setBackground(mContext.getResources().getDrawable(R.drawable.btn_selector_blue1));

				break;
			case 2:
				showFour(buttonFuncList.get(0),0);
				showFive(buttonFuncList.get(1),1);
				ll_btn_first.setVisibility(View.GONE);
				ll_btn_second.setVisibility(View.GONE);
				ll_btn_third.setVisibility(View.GONE);
				break;
			case 3:
				showThird(buttonFuncList.get(0),0);
				showFour(buttonFuncList.get(1),1);
				showFive(buttonFuncList.get(2),2);
				ll_btn_first.setVisibility(View.GONE);
				ll_btn_second.setVisibility(View.GONE);
				
				break;
			case 4:
				showSecond(buttonFuncList.get(0),0);
				showThird(buttonFuncList.get(1),1);
				showFour(buttonFuncList.get(2),2);
				showFive(buttonFuncList.get(3),3);
				ll_btn_first.setVisibility(View.GONE);
				
				break;
			case 5://显示前四个和更多
				showFirst(buttonFuncList.get(0),0);
				showSecond(buttonFuncList.get(1),1);
				showThird(buttonFuncList.get(2),2);
				showFour(buttonFuncList.get(3),3);
				showMore();
				break;

			default:
				break;
			}
		} else {// 没有Btn类型显示预览
			showPriview();
		}
		return view;
	}

	/**
	 * 显示第五个按钮
	 * @param func
	 * @param index
	 */
	private void showFive(Func func,int index) {

		
		
		ll_btn_five.setVisibility(View.VISIBLE);
		tv_btn_five.setText(func.getName());
		ll_btn_five.setId(func.getType());
//		iv_btn_five.setBackgroundResource(R.drawable.func_btn_custom1);
		ll_btn_five.setOnClickListener(listener);
		ll_btn_five.setTag(index);
		iv_btn_five.setVisibility(View.GONE);
//		ll_btn_five.setOnTouchListener(new OnTouchListener() {
//
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				if (event.getAction() == MotionEvent.ACTION_UP
//						|| event.getAction() == MotionEvent.ACTION_CANCEL) {
//					tv_btn_five.setTextColor(mContext.getResources().getColor(
//							R.color.func_menu_button_click));
//					iv_btn_five
//							.setBackgroundResource(R.drawable.func_btn_custom1);
//				} else {
//					tv_btn_five.setTextColor(mContext.getResources().getColor(
//							R.color.func_menu_press));
//					iv_btn_five
//							.setBackgroundResource(R.drawable.func_btn_custom2);
//				}
//				return false;
//			}
//		});
	}

	/**
	 * 显示第四个按钮
	 * @param func
	 * @param index
	 */
	private void showFour(Func func,int index) {
		ll_btn_four.setVisibility(View.VISIBLE);
		tv_btn_four.setText(func.getName());
		ll_btn_four.setId(func.getType());
		ll_btn_four.setOnClickListener(listener);
		ll_btn_four.setTag(index);
		iv_btn_four.setVisibility(View.GONE);
//		ll_btn_four.setOnTouchListener(new OnTouchListener() {
//
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				if (event.getAction() == MotionEvent.ACTION_UP
//						|| event.getAction() == MotionEvent.ACTION_CANCEL) {
//					tv_btn_four.setTextColor(mContext.getResources().getColor(
//							R.color.func_menu_button_click));
//					iv_btn_four
//							.setBackgroundResource(R.drawable.func_btn_custom1);
//				} else {
//					tv_btn_four.setTextColor(mContext.getResources().getColor(
//							R.color.func_menu_press));
//					iv_btn_four
//							.setBackgroundResource(R.drawable.func_btn_custom2);
//				}
//				return false;
//			}
//		});
	}

	/**
	 * 显示第三个按钮
	 * @param func
	 * @param index
	 */
	private void showThird(Func func,int index) {
		ll_btn_third.setVisibility(View.VISIBLE);
		tv_btn_third.setText(func.getName());
		ll_btn_third.setId(func.getType());
		ll_btn_third.setOnClickListener(listener);
		ll_btn_third.setTag(index);
//		ll_btn_third.setOnTouchListener(new OnTouchListener() {
//
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				if (event.getAction() == MotionEvent.ACTION_UP
//						|| event.getAction() == MotionEvent.ACTION_CANCEL) {
//					tv_btn_third.setTextColor(mContext.getResources().getColor(
//							R.color.func_menu_button_click));
//					iv_btn_third
//							.setBackgroundResource(R.drawable.func_btn_custom1);
//				} else {
//					tv_btn_third.setTextColor(mContext.getResources().getColor(
//							R.color.func_menu_press));
//					iv_btn_third
//							.setBackgroundResource(R.drawable.func_btn_custom2);
//				}
//				return false;
//			}
//		});
	}

	/**
	 * 显示第二个按钮
	 * @param func
	 * @param index
	 */
	private void showSecond(Func func,int index) {
		ll_btn_second.setVisibility(View.VISIBLE);
		tv_btn_second.setText(func.getName());
		ll_btn_second.setId(func.getType());
		ll_btn_second.setOnClickListener(listener);
		ll_btn_second.setTag(index);
//		ll_btn_second.setOnTouchListener(new OnTouchListener() {
//
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				if (event.getAction() == MotionEvent.ACTION_UP
//						|| event.getAction() == MotionEvent.ACTION_CANCEL) {
//					tv_btn_second.setTextColor(mContext.getResources().getColor(
//							R.color.func_menu_button_click));
//					iv_btn_second
//							.setBackgroundResource(R.drawable.func_btn_custom1);
//				} else {
//					tv_btn_second.setTextColor(mContext.getResources().getColor(
//							R.color.func_menu_press));
//					iv_btn_second
//							.setBackgroundResource(R.drawable.func_btn_custom2);
//				}
//				return false;
//			}
//		});
	}

	/**
	 * 显示第一个按钮
	 * @param func
	 * @param index
	 */
	private void showFirst(Func func,int index) {
		ll_btn_first.setVisibility(View.VISIBLE);
		tv_btn_first.setText(func.getName());
		ll_btn_first.setId(func.getType());
		ll_btn_first.setOnClickListener(listener);
		ll_btn_first.setTag(index);
//		ll_btn_first.setOnTouchListener(new OnTouchListener() {
//
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				if (event.getAction() == MotionEvent.ACTION_UP
//						|| event.getAction() == MotionEvent.ACTION_CANCEL) {
//					tv_btn_first.setTextColor(mContext.getResources().getColor(
//							R.color.func_menu_button_click));
//					iv_btn_first
//							.setBackgroundResource(R.drawable.func_btn_custom1);
//				} else {
//					tv_btn_first.setTextColor(mContext.getResources().getColor(
//							R.color.func_menu_press));
//					iv_btn_first
//							.setBackgroundResource(R.drawable.func_btn_custom2);
//				}
//				return false;
//			}
//		});
	}

	/**
	 * 显示更多
	 */
	private void showMore() {
		ll_btn_five.setVisibility(View.VISIBLE);
		tv_btn_five.setText(mContext.getResources().getString(R.string.more));
		iv_btn_five.setBackgroundResource(R.drawable.func_btn_priview1);
		ll_btn_five.setOnClickListener(listener);
//		ll_btn_five.setOnTouchListener(new OnTouchListener() {
//
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				if (event.getAction() == MotionEvent.ACTION_UP
//						|| event.getAction() == MotionEvent.ACTION_CANCEL) {
//					tv_btn_five.setTextColor(mContext.getResources().getColor(
//							R.color.func_menu_button_click));
//					iv_btn_five
//							.setBackgroundResource(R.drawable.func_btn_more1);
//				} else {
//					tv_btn_five.setTextColor(mContext.getResources().getColor(
//							R.color.func_menu_press));
//					iv_btn_five
//							.setBackgroundResource(R.drawable.func_btn_more2);
//				}
//				return false;
//			}
//		});
	}

	/**
	 * 显示预览
	 */
	private void showPriview() {
		
		ll_btn_first.setVisibility(View.GONE);
		ll_btn_second.setVisibility(View.GONE);
		ll_btn_third.setVisibility(View.GONE);
		ll_btn_four.setVisibility(View.GONE);
		
		
		ll_btn_five.setVisibility(View.VISIBLE);
		ViewGroup.LayoutParams params = ll_btn_five.getLayoutParams();
		params.width = ViewGroup.LayoutParams.MATCH_PARENT;
		params.height = params.height/2;
		ll_btn_five.setBackground(mContext.getResources().getDrawable(R.drawable.btn_selector_blue1));
		ll_btn_five.setId(SHOWPRIVIEW_ID);//表示预览ID
		ll_btn_five.setOnClickListener(listener);
		tv_btn_five.setText(mContext.getResources().getString(R.string.order_submit));
		iv_btn_five.setVisibility(View.GONE);
		iv_btn_five.setBackgroundResource(R.drawable.func_btn_priview1);
		ll_btn_five.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP
						|| event.getAction() == MotionEvent.ACTION_CANCEL) {
//					tv_btn_five.setTextColor(mContext.getResources().getColor(
//							R.color.func_menu_button_click));
//					iv_btn_five.setBackgroundResource(R.drawable.func_btn_priview1);
				} else {
//					tv_btn_five.setTextColor(mContext.getResources().getColor(
//							R.color.func_menu_press));
//					iv_btn_five.setBackgroundResource(R.drawable.func_btn_priview2);
				}
				return false;
			}
		});
	}
}
