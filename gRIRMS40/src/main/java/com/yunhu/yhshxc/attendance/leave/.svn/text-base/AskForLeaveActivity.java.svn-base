package com.yunhu.yhshxc.attendance.leave;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.bo.Module;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.PublicUtils;

/**
 * 请假入口
 * @author xuelinlin
 *
 */
public class AskForLeaveActivity extends AbsBaseActivity {
	private LinearLayout ll_order3_layout;
	private String storeId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.attend_for_leave);
		ll_order3_layout = (LinearLayout) this.findViewById(R.id.ll_order3_layout);
		List<Module> list = new ArrayList<Module>();
		Module create = new Module();
		create.setName(PublicUtils.getResourceString(this,R.string.leave_m3));
		create.setType(Constants.MODULE_TYPE_REPORT);
		list.add(create);
		Module search = new Module();
		search.setName(PublicUtils.getResourceString(this,R.string.leave_m));
		search.setType(Constants.MODULE_TYPE_QUERY);
		list.add(search);

		
		initLayout(list);
		findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
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
						
							case Constants.MODULE_TYPE_QUERY:// 查询  我审批的
								iconLeft.setImageResource(R.drawable.icon_order);//
								break;
							case Constants.MODULE_TYPE_REPORT:// 上报   我发起的
								iconLeft.setImageResource(R.drawable.icon_upload);
								break;
							
							
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
	
			case Constants.MODULE_TYPE_QUERY:// 查询  审批
				search();
				break;
			case Constants.MODULE_TYPE_REPORT:// 上报  发起
				create();
				break;
		
			default:
				break;
		}

	}
	
	private void create() {
		Intent intent = new Intent(AskForLeaveActivity.this,InitiateLeaveActivity.class);
		
		startActivity(intent);
	}
	private void search() {
		Intent intent = new Intent(AskForLeaveActivity.this,ApprovalLeaveActivity.class);
		
		startActivity(intent);
	}
}
