package com.yunhu.yhshxc.func;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.bo.Module;
import com.yunhu.yhshxc.comp.CompDialog;
import com.yunhu.yhshxc.comp.SqlInComp;
import com.yunhu.yhshxc.list.activity.TableListActivity;
import com.yunhu.yhshxc.list.activity.TableListActivityNew;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.visit.TableHistoryActivity;

import java.util.ArrayList;

import gcg.org.debug.ELog;

/**
 * 历史信息查询和本次上报PopupWindow
 * 
 * @author jishen
 *
 */
public class HistorySearchPopupWindow {
	public static final String CLOSE_ACTION = "shut_down_popupwindow";
	private Context context = null;
	/**
	 * 当前func
	 */
	private Func currentFunc = null;
	private PopupWindow popupWindow = null;
	/**
	 * 弹出的PopupWindow上要条件的模块
	 */
	private ArrayList<Module> moduleList = null;

	// private TitleBar titlebar;
	private boolean isLink = false;// 是否是超链接模块里
	private int menuId;
	private String menuName;

	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public HistorySearchPopupWindow(Context context, Func currentFunc, boolean isLink) {
		this.context = context;
		this.isLink = isLink;
		this.currentFunc = currentFunc;
		init();
	}

	/**
	 * 初始化数据，目前只添加 “历史信息” 和 “立即上报” 这两个模块
	 */
	private void init() {
		moduleList = new ArrayList<Module>();
		Module m1 = new Module();
		Module m2 = new Module();
		m1.setMenuId(currentFunc.getTargetid());
		m2.setMenuId(currentFunc.getTargetid());
		m1.setType(Constants.MODULE_TYPE_REPORT);
		m2.setType(Constants.MODULE_TYPE_QUERY);
		m1.setName(context.getResources().getString(R.string.Report_immediately));
		m2.setName(context.getResources().getString(R.string.historical_information));
		moduleList.add(m1);
		moduleList.add(m2);

		View contentView = View.inflate(context, R.layout.module_layout_history, null);
		// contentView.findViewById(R.id.back).setOnClickListener(new
		// View.OnClickListener(){
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// close();
		// }
		//
		// });
		// titlebar = new TitleBar(context,
		// contentView.findViewById(R.id.rl_titlebar));
		// titlebar.register();
		LinearLayout moduleLayout = (LinearLayout) contentView.findViewById(R.id.ll_module_layout);
		popupWindow = new PopupWindow(contentView, WindowManager.LayoutParams.FILL_PARENT,
				WindowManager.LayoutParams.FILL_PARENT, true);
		// popupWindow.setAnimationStyle(R.style.popupwindow);
		popupWindow.setBackgroundDrawable(context.getResources().getDrawable(R.color.black));
		initLayout(moduleLayout, currentFunc, popupWindow);
		contentView.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// theOperatedFinish();
				// onClickKeyCodeBack();
				// finish();
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
			}
		});
	}

	/**
	 * 弹出PopupWindow
	 * 
	 * @param anchor
	 *            参照的view
	 */
	public void showAsDropDown(View anchor) {
		int high = ((AbsFuncActivity) context).getWindowManager().getDefaultDisplay().getHeight();
		// popupWindow.showAsDropDown(anchor, 0,
		// -high+PublicUtils.convertDIP2PX(context, 40));
		popupWindow.showAtLocation(anchor, Gravity.BOTTOM, 0, 0);
		popupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// titlebar.unregister();
			}
		});
		AbsFuncActivity act = (AbsFuncActivity) context;
		if (act != null && act.menuType == Menu.TYPE_VISIT) {// 如果是来自拜访,则注册监听,当收到监听广播后关闭此popwindow
			IntentFilter filter = new IntentFilter();
			filter.addAction(CLOSE_ACTION);
			receiver = new MyCloseReceiver();
			context.registerReceiver(receiver, filter);
		}

	}
	//处理特殊的拜访中提交数据关闭此PopUpWindow事件
    private MyCloseReceiver receiver;
	class MyCloseReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(CLOSE_ACTION)) {
				AbsFuncActivity act = (AbsFuncActivity) context;
				if (act.TView!=null) {
					act.setValue(context.getResources().getString(R.string.have_finished), act.TView);
				}
				close();
			}
			
		}
		
	}

	// 初始化本界面
	private void initLayout(LinearLayout moduleLayout, final Func func, final PopupWindow popupWindow) {
		// 往这个布局里面添加
		float density = context.getResources().getDisplayMetrics().density;
		moduleLayout = (LinearLayout) moduleLayout.findViewById(R.id.ll_module_layout);
		if (moduleList != null) {
			int len = moduleList.size();
			for (int i = 0; i < len; i++) { //
				final View item = View.inflate(context, R.layout.module_item, null);
				item.setBackgroundResource(R.color.visit_item_disorder);
				item.setTag(moduleList.get(i));

				ImageView iconLeft = (ImageView) item.findViewById(R.id.leftIcon),
						iconRight = (ImageView) item.findViewById(R.id.rightIcon);
				TextView label = (TextView) item.findViewById(R.id.label);
				label.setText(moduleList.get(i).getName());

				switch (moduleList.get(i).getType()) {
				case Constants.MODULE_TYPE_EXECUTE:// 双向执行
				case Constants.MODULE_TYPE_EXECUTE_NEW:// 双向执行
					iconLeft.setImageResource(R.drawable.zhixing);
					break;
				case Constants.MODULE_TYPE_QUERY:// 查询
					iconLeft.setImageResource(R.drawable.chaxun);
					break;
				case Constants.MODULE_TYPE_REPORT:// 上报
					iconLeft.setImageResource(R.drawable.shangbao);
					break;
				case Constants.MODULE_TYPE_ISSUED:// 下发
					iconLeft.setImageResource(R.drawable.xiafa);
					break;
				case Constants.MODULE_TYPE_VERIFY:// 审核
					iconLeft.setImageResource(R.drawable.shenhe);
					break;
				case Constants.MODULE_TYPE_REASSIGN:// 改派
					iconLeft.setImageResource(R.drawable.gaipai);
					break;
				case Constants.MODULE_TYPE_UPDATE:// 修改
					iconLeft.setImageResource(R.drawable.xiugai);
					break;
				case Constants.MODULE_TYPE_PAY:// 支付
					iconLeft.setImageResource(R.drawable.zhixing);
					break;
				}
				item.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						onClickForModule((Module) v.getTag(), func, popupWindow);
					}
				});
				item.setOnTouchListener(new View.OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							item.setBackgroundResource(R.color.home_menu_pressed);
							break;

						case MotionEvent.ACTION_UP:
						case MotionEvent.ACTION_CANCEL:
						case MotionEvent.ACTION_OUTSIDE:
							item.setBackgroundResource(R.color.visit_item_disorder);
							break;
						}
						return false;
					}
				});
				LinearLayout.LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
						1);
				lp.bottomMargin = (int) (5 * density);
				moduleLayout.addView(item, lp);
			}
		}
	}

	public void onClickForModule(Module module, Func func, PopupWindow popupWindow) {

		switch (module.getType()) {
		case Constants.MODULE_TYPE_QUERY:// 查询
			intentQuery(module);
			break;
		case Constants.MODULE_TYPE_REPORT:// 本次上报
			intentOther();
			break;

		default:
			break;
		}

	}

	/**
	 * 历史信息查询
	 * 
	 * @param module
	 */
	private void intentQuery(Module module) {
		AbsFuncActivity act = (AbsFuncActivity) context;
		act.bundle.putInt("funcId", currentFunc.getFuncId());
		act.bundle.putBoolean(TableListActivity.TAG_IS_HISTORY, true);
		switch (currentFunc.getType()) {
		case Func.TYPE_LINK:// 超链接
			ELog.d("Open TableListActivity");
			Intent linkIntent = new Intent(act, TableListActivityNew.class);
			act.bundle.putInt("menuId", act.menuId);
			act.bundle.putString("menuName", act.menuName);
			//向下传递类型
			act.bundle.putString("menuType2", act.menuType+"");
			linkIntent.putExtra("bundle", act.bundle);
			act.startActivity(linkIntent);
			break;
		case Func.TYPE_TABLECOMP:// 表格
			Intent tableIntent = new Intent(act, TableHistoryActivity.class);
			act.bundle.putInt("menuId", act.menuId);
			act.bundle.putString("menuName", act.menuName);
			//向下传递类型
			act.bundle.putString("menuType2", act.menuType+"");
			tableIntent.putExtra("bundle", act.bundle);
			
			act.startActivity(tableIntent);
			break;

		default:
			break;
		}

	}

	/**
	 * 立即上报
	 */
	private void intentOther() {

		AbsFuncActivity act = (AbsFuncActivity) context;

		switch (currentFunc.getType()) {
		case Func.TYPE_LINK:// 超链接
			if (currentFunc.getDefaultType() != null && currentFunc.getDefaultType() == Func.DEFAULT_TYPE_SQL) {// 超链接sql
				new SqlInComp(act, 0, isLink, menuId, menuName).getSearchRequirement2(currentFunc);
			} else {
				act.linkFunc(currentFunc);
			}
			break;
		case Func.TYPE_TABLECOMP:// 表格
			if (currentFunc.getDefaultType() != null && currentFunc.getDefaultType() == Func.DEFAULT_TYPE_SQL) {// 表格sql
				new SqlInComp(act, 0, isLink, menuId, menuName).getSearchRequirement2(currentFunc);
			} else {
				CompDialog comp = new CompDialog(act, currentFunc, act.bundle);
				comp.getObject();

			}
			break;
		default:
			break;
		}

	}

	public void close() {
		if (receiver!=null) {
			context.unregisterReceiver(receiver);
			receiver=null;
		}
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
		}

	}
}
