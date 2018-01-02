package com.yunhu.yhshxc.attendance.leave;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.attendance.util.SharedPreferencesForLeaveUtil;
import com.yunhu.yhshxc.bo.Module;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.android.pulltorefresh.library.PullToRefreshListView;

/**
 * 我发起的
 * @author xuelinlin
 *
 */
public class InitiateLeaveActivity extends AbsBaseActivity {
	private LinearLayout ll_order3_layout;
	private TextView tv_attend_leave;
	private AskForLeaveInfoDB leaveDB;
	private PullToRefreshListView leave_list;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.attend_for_leave);
		leaveDB = new AskForLeaveInfoDB(this);
		leaveDB.delete();
		ll_order3_layout = (LinearLayout) this.findViewById(R.id.ll_order3_layout);
		tv_attend_leave = (TextView) findViewById(R.id.tv_attend_leave);
		tv_attend_leave.setText(PublicUtils.getResourceString(this,R.string.attendance_leave));
		leave_list = (PullToRefreshListView) findViewById(R.id.leave_list);
		leave_list.setVisibility(View.GONE);
		List<Module> list = new ArrayList<Module>();
		if("1".equals(SharedPreferencesForLeaveUtil.getInstance(this).getIS_INSERT())){//是否有上报
			Module create = new Module();
			create.setName(PublicUtils.getResourceString(this,R.string.attendance_title));
			create.setType(Constants.MODULE_TYPE_REPORT);
			list.add(create);
		}
		if("1".equals(SharedPreferencesForLeaveUtil.getInstance(this).getIS_UPDATE())){//是否修改
			Module search = new Module();
			search.setName(PublicUtils.getResourceString(this,R.string.leave_m1));
			search.setType(Constants.MODULE_TYPE_QUERY);
			list.add(search);
		}
		if("1".equals(SharedPreferencesForLeaveUtil.getInstance(this).getIS_QUERY())){//是否查询
			Module though = new Module();
			though.setName(PublicUtils.getResourceString(this,R.string.attendance_search));
			though.setType(Constants.MODULE_TYPE_EXECUTE);
			list.add(though);
		}
		
		if("1".equals(SharedPreferencesForLeaveUtil.getInstance(this).getIS_AUDIT())){//是否审核
			Module retur = new Module();
			retur.setName(PublicUtils.getResourceString(this,R.string.leave_m13));
			retur.setType(Constants.MODULE_TYPE_PAY);
			list.add(retur);
		}
		
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
						
							case Constants.MODULE_TYPE_QUERY:// 待审批
								iconLeft.setImageResource(R.drawable.leave_wait);//
								break;
							case Constants.MODULE_TYPE_REPORT:// 请假上报
								iconLeft.setImageResource(R.drawable.leave_shangbao);
								break;
							case Constants.MODULE_TYPE_EXECUTE:// 查询
								iconLeft.setImageResource(R.drawable.leave_chaxun);
								break;
							case Constants.MODULE_TYPE_PAY:// 请审批
								iconLeft.setImageResource(R.drawable.leave_shenpi);
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
	
			case Constants.MODULE_TYPE_QUERY:// 待审批
				search();
				break;
			case Constants.MODULE_TYPE_REPORT:// 请假上报
				create();
				break;
			case Constants.MODULE_TYPE_EXECUTE:// 查询
				through();
				break;
			case Constants.MODULE_TYPE_PAY:// 请审批
				retur();
				break;
			default:
				break;
		}

	}
	/**
	 * 请审批
	 */
	private void retur() {
		Intent intent = new Intent(InitiateLeaveActivity.this,DoApproLeaveActivity.class);
		
		startActivity(intent);
	}
	/**
	 * 查询
	 */
	private void through() {
		Intent intent = new Intent(InitiateLeaveActivity.this,ThroughLeaveActivity.class);
		
		startActivity(intent);
	}
	private void create() {
		Intent intent = new Intent(InitiateLeaveActivity.this,ReportLeaveActivity.class);
		
		startActivity(intent);
	}
	private void search() {
		Intent intent = new Intent(InitiateLeaveActivity.this,WaitForApproLeaveActivity.class);
		
		startActivity(intent);
	}
	@Override
	protected void onStart() {
		if(leaveDB!=null){
			leaveDB.delete();
		}
		super.onStart();
	}
	@Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
    }  
}
