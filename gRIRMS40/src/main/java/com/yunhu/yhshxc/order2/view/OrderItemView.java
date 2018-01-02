package com.yunhu.yhshxc.order2.view;

import java.util.List;

import org.json.JSONException;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.order2.OrderDetailFragment;

public class OrderItemView {
	private View view;
	private boolean isTitle;
	private LinearLayout ll_order2_item_view_main;
	private ImageView iv_order2_item_view_mark;
	private LinearLayout ll_order2_item_view_main_content;
	private LinearLayout ll_order2_item_view_sub;
	private TextView tv_order2_item_view_comment;
	private LinearLayout ll_order2_item_view_update;
	private LinearLayout ll_order2_item_view_delete;
	private boolean isOpean = false;
	private OrderDetailFragment orderDetailFragment;
	private Context context;
	public OrderItemView(Context context,boolean isTitle) {
		this.isTitle = isTitle;
		this.context = context;
		view = View.inflate(context, R.layout.order_item_view, null);
		ll_order2_item_view_main = (LinearLayout)view.findViewById(R.id.ll_order2_item_view_main);
		iv_order2_item_view_mark = (ImageView)view.findViewById(R.id.iv_order2_item_view_mark);
		ll_order2_item_view_main_content = (LinearLayout)view.findViewById(R.id.ll_order2_item_view_main_content);
		ll_order2_item_view_sub = (LinearLayout)view.findViewById(R.id.ll_order2_item_view_sub);
		tv_order2_item_view_comment = (TextView)view.findViewById(R.id.tv_order2_item_view_comment);
		ll_order2_item_view_update = (LinearLayout)view.findViewById(R.id.ll_order2_item_view_update);
		ll_order2_item_view_delete = (LinearLayout)view.findViewById(R.id.ll_order2_item_view_delete);
		ll_order2_item_view_update.setOnClickListener(listener);
		ll_order2_item_view_delete.setOnClickListener(listener);
//		initData();
	}
	
	
	
	public void setOrderDetailFragment(OrderDetailFragment orderDetailFragment) {
		this.orderDetailFragment = orderDetailFragment;
	}



	public View getView(){
		return view;
	}
	
	public void setIsExamine(boolean isExamine){
		if (isExamine) {
			ll_order2_item_view_update.setVisibility(View.GONE);
			ll_order2_item_view_delete.setVisibility(View.GONE);
		}
	}
	
	public void initTitleData(List<String> data){
		iv_order2_item_view_mark.setVisibility(View.INVISIBLE);
		ll_order2_item_view_sub.setVisibility(View.GONE);
		ll_order2_item_view_main.setBackgroundResource(R.color.order_item_title_bg);
		ll_order2_item_view_main_content.setVisibility(View.GONE);
		if (data!=null && data.size()>0) {
			for (int i = 0; i < data.size(); i++) {
				TextView tv_title = (TextView) View.inflate(context, R.layout.table_list_item_unit, null);
				tv_title.setGravity(Gravity.CENTER);//标题设置居中
				DisplayMetrics dm = new DisplayMetrics();
				dm = context.getResources().getDisplayMetrics(); 
				if (dm.density<=1.5){
					tv_title.setLayoutParams(new LayoutParams(150, LayoutParams.WRAP_CONTENT));
				}else if(dm.density<=3){
					tv_title.setLayoutParams(new LayoutParams(250, LayoutParams.WRAP_CONTENT));
				}
				
				tv_title.setText(data.get(i));
				tv_title.setTextColor(Color.rgb(0, 0, 0));
				tv_title.setTextSize(16);
				ll_order2_item_view_main.addView(tv_title);
			}
		}
	}
	
	public void initContentData(List<String> data){
		ll_order2_item_view_sub.setVisibility(View.GONE);
		ll_order2_item_view_main.setBackgroundResource(R.drawable.order2_item_view);
		ll_order2_item_view_main.setOnClickListener(listener);
		if (data!=null && data.size()>0) {
			for (int i = 0; i < data.size() - 1; i++) {
				TextView tv_title = (TextView) View.inflate(context, R.layout.table_list_item_unit, null);
				tv_title.setGravity(Gravity.CENTER);//标题设置居中
				DisplayMetrics dm = new DisplayMetrics();
				dm = context.getResources().getDisplayMetrics(); 
				if (dm.density<=1.5){
					tv_title.setLayoutParams(new LayoutParams(150, LayoutParams.WRAP_CONTENT));
				}else if(dm.density<=3){
					tv_title.setLayoutParams(new LayoutParams(250, LayoutParams.WRAP_CONTENT));
				}
				tv_title.setText(data.get(i));
				tv_title.setTextColor(Color.rgb(0, 0, 0));
				tv_title.setTextSize(16);
				ll_order2_item_view_main_content.addView(tv_title);
			}
			tv_order2_item_view_comment.setText("备注:"+data.get(data.size() - 1));
		}
	}
	
	public View getMain(){
		return ll_order2_item_view_main;
	}
	
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ll_order2_item_view_main:
				mainClick();
				break;
			case R.id.ll_order2_item_view_update:
				update();
				break;
			case R.id.ll_order2_item_view_delete:
				try {
					delete();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			default:
				break;
			}
			
		}
	};
	
	private void delete() throws JSONException{
		if (orderDetailFragment!=null) {
			orderDetailFragment.delete(Integer.parseInt((String) getView().getTag()));
		}
	}
	
	private void update(){
//		Intent intent = new Intent(context, OrderCreateEditActivity.class);
//		String index = (String) getView().getTag();
//		intent.putExtra("updateIndex", index);
//		context.startActivity(intent);
		
		if (orderDetailFragment!=null) {
			orderDetailFragment.update(Integer.parseInt((String) getView().getTag()));
		}
	}
	
	private void mainClick(){
		int show = isOpean ? View.GONE:View.VISIBLE;
		ll_order2_item_view_sub.setVisibility(show);
		isOpean = !isOpean;
		int backgroundColor = isOpean ? R.color.order_item_main_bg_open : R.drawable.order2_item_view;
		ll_order2_item_view_main.setBackgroundResource(backgroundColor);
		int mark = isOpean ? R.drawable.order2_mark2 : R.drawable.order2_mark1;
		iv_order2_item_view_mark.setBackgroundResource(mark);
	}
	
}
