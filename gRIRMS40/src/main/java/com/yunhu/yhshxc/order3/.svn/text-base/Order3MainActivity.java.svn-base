package com.yunhu.yhshxc.order3;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.bo.Module;
import com.yunhu.yhshxc.order3.send.Order3SendActivity;
import com.yunhu.yhshxc.utility.Constants;

public class Order3MainActivity extends AbsBaseActivity {
	private LinearLayout ll_order3_layout;
	private String storeId;
	private int menuType;
	private int targetId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order3_main_activity);
		menuType = getIntent().getExtras().getInt("menuType", 0);
		targetId = getIntent().getExtras().getInt("targetId", 0);
		ll_order3_layout = (LinearLayout) this.findViewById(R.id.ll_order3_layout);
		List<Module> list = new ArrayList<Module>();
		Module create = new Module();
		create.setName("创建订单");
		create.setType(Constants.MODULE_TYPE_REPORT);
		list.add(create);
		Module search = new Module();
		search.setName("查询订单");
		search.setType(Constants.MODULE_TYPE_QUERY);
		list.add(search);
		storeId = getIntent().getStringExtra("storeId");
//		Module send = new Module();
//		send.setName("订单派送");
//		send.setType(Constants.MODULE_TYPE_EXECUTE);
//		list.add(send);
		
		initLayout(list);
findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	private void initLayout(List<Module> moduleList) {
		// 往这个布局里面添加
				float density = getResources().getDisplayMetrics().density;
				if (moduleList != null) {
					int len = moduleList.size();
					for (int i = 0; i < len; i++) { 
						//自定义模块的view
						final View item = View.inflate(getApplicationContext(), R.layout.module_item, null);
//						item.setBackgroundResource(R.color.visit_item_disorder);
						item.setTag(moduleList.get(i));
						//设置view的内容
						ImageView iconLeft = (ImageView)item.findViewById(R.id.leftIcon);
//						ImageView iconRight = (ImageView)item.findViewById(R.id.rightIcon);
						TextView label = (TextView)item.findViewById(R.id.label);
						label.setText(moduleList.get(i).getName());
						
						switch (moduleList.get(i).getType()) {
						
							case Constants.MODULE_TYPE_QUERY:// 查询
								iconLeft.setImageResource(R.drawable.icon_order);//
								break;
							case Constants.MODULE_TYPE_REPORT:// 上报
								iconLeft.setImageResource(R.drawable.icon_upload);
								break;
							case Constants.MODULE_TYPE_EXECUTE://执行
								iconLeft.setImageResource(R.drawable.icon_unupload);
							
						}
						//给自定义的view添加单击事件
						item.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								onClickForModule((Module) v.getTag());
							}
						});
						//给自定义的view添加触摸事件 改变背景颜色
						item.setOnTouchListener(new View.OnTouchListener() {
							
							@Override
							public boolean onTouch(View v, MotionEvent event) {
								switch (event.getAction()) {
									case MotionEvent.ACTION_DOWN:
//										item.setBackgroundResource(R.color.home_menu_pressed);
										break;
										
									case MotionEvent.ACTION_UP:
									case MotionEvent.ACTION_CANCEL:
									case MotionEvent.ACTION_OUTSIDE:
//										item.setBackgroundResource(R.color.visit_item_disorder);
										break;
								}
								return false;
							}
						});
//						LinearLayout.LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1);
//						lp.bottomMargin = (int)(5 * density);
//						ll_order3_layout.addView(item, lp);//将自定义的view添加到父view中
						ll_order3_layout.addView(item);
					}
				}
	}
	/**
	 * 根据模块类型跳转到不同的页面
	 * @param module 模块的实例
	 */
	public void onClickForModule(Module module) {

		switch (module.getType()) {
	
			case Constants.MODULE_TYPE_QUERY:// 查询
				search();
				break;
			case Constants.MODULE_TYPE_REPORT:// 上报
				create();
				break;
			case Constants.MODULE_TYPE_EXECUTE:
				send();
				break;
			default:
				break;
		}

	}
	private void send() {
		Intent intent = new Intent(Order3MainActivity.this,Order3SendActivity.class);
		if (!TextUtils.isEmpty(storeId)) {
			intent.putExtra("storeId", storeId);
		}
		startActivity(intent);
	}
	private void create() {
		Intent intent = new Intent(Order3MainActivity.this,Order3HomeActivity.class);
		if (!TextUtils.isEmpty(storeId)) {
			intent.putExtra("storeId", storeId);
		}
		intent.putExtra("menuType",menuType);
		intent.putExtra("targetId", targetId);
		startActivity(intent);
	}
	private void search() {
		Intent intent = new Intent(Order3MainActivity.this,Order3SearchActivity.class);
		if (!TextUtils.isEmpty(storeId)) {
			intent.putExtra("storeId", storeId);
		}
		startActivity(intent);
	}
}
