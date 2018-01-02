package com.yunhu.yhshxc.activity;

import gcg.org.debug.JLog;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.bo.OrgStore;
import com.yunhu.yhshxc.bo.Submit;
import com.yunhu.yhshxc.bo.SubmitItem;
import com.yunhu.yhshxc.database.FuncDB;
import com.yunhu.yhshxc.database.OrgStoreDB;
import com.yunhu.yhshxc.database.SubmitDB;
import com.yunhu.yhshxc.database.SubmitItemDB;
import com.yunhu.yhshxc.database.SubmitItemTempDB;
import com.yunhu.yhshxc.database.VisitFuncDB;
import com.yunhu.yhshxc.exception.DataComputeException;
import com.yunhu.yhshxc.func.AbsFuncActivity;
import com.yunhu.yhshxc.func.FuncTreeForLink;
import com.yunhu.yhshxc.order2.Order2LinkWidgetMainActivity;
import com.yunhu.yhshxc.order3.Order3MainActivity;
import com.yunhu.yhshxc.order3.send.Order3SendActivity;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.widget.ToastOrder;

/**
 * 超链接模块页面 继承AbsFuncActivity页面操作都在父类中实现，数据获取在本类中获取
 * 
 * @author jishen
 *
 */
public class LinkFuncActivity extends AbsFuncActivity {
	private Integer linkKey;// 超链接控件的控件ID
	private String status; // 二级菜单类型所获取的控件
	private int modType;// 模块ID
	private boolean isVisit;// 是否是访店
	private String menuType2 = "";//只针对拜访

	@Override
	public void onCreate(Bundle savedInstanceState) {
		init();
		super.onCreate(savedInstanceState);
		initTempSubmit();// 初始化临时表
		Func link = null;
		if (isVisit) {// 访店查询VisitFuncDB
			link = new VisitFuncDB(this).findFuncByFuncId(linkKey);
		} else {
			link = new FuncDB(this).findFuncByFuncId(linkKey);
		}
		TextView title = (TextView) findViewById(R.id.tv_titleName);
		if (link != null) {// 设置标题
			title.setText(link.getName());
		} else {
			title.setText(PublicUtils.getResourceString(LinkFuncActivity.this,R.string.report));
		}
		isLinkActivity = true;
	}

	/**
	 * 初始化数据，获取传过来的数据
	 */
	private void init() {
		menuType2 = getIntent().getStringExtra("menuType2");
		bundle = getIntent().getBundleExtra("bundle");
		targetId = bundle.getInt("targetId");// 数据ID
		status = bundle.getString("status");// 获取二级菜单数据状态
		linkId = bundle.getInt("linkId", 0);// 上层的表单ID
		linkKey = bundle.getInt("linkKey", 0);// 超链接控件的控件ID
		taskId = bundle.getInt("taskId", 0);// 任务ID
		sqlStoreId = bundle.getInt("sqlStoreId", 0);// 访店中的店面ID
		linkFuncId = bundle.getInt("funcId", 0);// 超链接控件的控件ID
		modType = bundle.getInt("modType", 0);// 模块类型
		menuType = Menu.TYPE_MODULE;// 设置菜单类型为自定义模块
		bundle.putInt("targetType", menuType);
		bundle.putBoolean("isLink", true);// 说明是超链接模块
		isVisit = bundle.getBoolean("isVisit");// 获取是否是访店
	}

	/**
	 * 初始化临时表 将数据表中已经保存的数据存储到临时表中
	 */
	private void initTempSubmit() {

		int submitId = new SubmitDB(this).selectSubmitId(planId, wayId, storeId, targetId, menuType);
		if (submitId != 0) {
			List<SubmitItem> list = new SubmitItemDB(this).findSubmitItemBySubmitId(submitId);
			SubmitItemTempDB tempDB = new SubmitItemTempDB(this);
			if (!list.isEmpty()) {
				for (int i = 0; i < list.size(); i++) {
					SubmitItem item = list.get(i);
					item.setIsSave(1);// 1 表示已经保存过该条数据
					boolean isHas = tempDB.findIsHaveParamName(item.getParamName(), item.getSubmitId());
					if (isHas) {
						// 拍照和表格的情况，只要临时表中有数据则是最新数据不用修改，否则表格修改再跳转回来的时候数据还是原来数据
						if (item.getType() != Func.TYPE_TABLECOMP && item.getType() != Func.TYPE_CAMERA
								&& item.getType() != Func.TYPE_CAMERA_HEIGHT
								&& item.getType() != Func.TYPE_CAMERA_MIDDLE
								&& item.getType() != Func.TYPE_CAMERA_CUSTOM) {
							tempDB.updateSubmitItem(item);
						}
					} else {
						tempDB.insertSubmitItem(item);
					}

				}
			}
		}
	}

	/**
	 * 整个布局中所用到的控件
	 * 
	 * @return
	 */
	@Override
	public List<Func> getFuncList() {
		List<Func> funcList = null;
		if (isSqlLink) {// 链接sql
			funcList = new FuncDB(this).findFuncListByTargetid(targetId, true);
		} else {
			funcList = new FuncDB(this).findFuncListByTargetid(targetId, false);
		}
		return funcList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gcg.grirms.func.AbsFuncActivity#getFuncByFuncId(int)
	 */
	@Override
	public Func getFuncByFuncId(int funcId) {
		Func func = new FuncDB(this).findFuncByFuncId(funcId);
		return func;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gcg.grirms.func.AbsFuncActivity#getOrderFuncList(java.lang.
	 * Integer, int)
	 */
	@Override
	public ArrayList<Func> getOrderFuncList(Integer funcId, int inputOrder) {
		ArrayList<Func> orderList = new FuncDB(this).findFuncListByInputOrder(targetId, funcId, inputOrder);
		return orderList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gcg.grirms.func.AbsFuncActivity#getButtonFuncList()
	 */
	@Override
	public List<Func> getButtonFuncList() {
		List<Func> buttonFuncList = new FuncDB(this).findButtonFuncListReplenish(targetId, status);
		return buttonFuncList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gcg.grirms.func.AbsFuncActivity#findFuncListByInputOrder()
	 */
	@Override
	public Integer[] findFuncListByInputOrder() {
		Integer[] inputOrderArr = new FuncDB(this).findFuncListByInputOrder(targetId);
		return inputOrderArr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gcg.grirms.func.AbsFuncActivity#intoLink(android.os.Bundle)
	 */
	@Override
	protected void intoLink(Bundle linkBundle) {
		Intent linkIntent = new Intent(this, LinkFuncActivity.class);
		linkBundle.putInt("modType", modType);
		linkBundle.putInt("sqlStoreId", sqlStoreId);
		linkIntent.putExtra("bundle", linkBundle);
		startActivity(linkIntent);
	}

	@Override
	protected void newOrder(Func func) {
		Func storeFunc = newOrderStoreFunc();
		if (storeFunc != null) {
			OrgStore store = chosedNewOrderStore(storeFunc);
			if (store != null) {
				Intent intent = new Intent(this, Order2LinkWidgetMainActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("storeId", String.valueOf(store.getStoreId()));
				bundle.putString("storeName", store.getStoreName());
				bundle.putInt("funcId", func.getFuncId());
				bundle.putInt("orderId", func.getId());
				bundle.putInt("submitId", submitId());
				intent.putExtra("newOrderBundle", bundle);
				startActivity(intent);
			} else {
				ToastOrder.makeText(this, PublicUtils.getResourceString(LinkFuncActivity.this,R.string.toast_one3) + storeFunc.getName(), ToastOrder.LENGTH_SHORT).show();
			}
		} else {
			ToastOrder.makeText(this, PublicUtils.getResourceString(LinkFuncActivity.this,R.string.toast_one4), ToastOrder.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void threeOrder(Func func) {
		super.threeOrder(func);
		Intent intent = new Intent(this, Order3MainActivity.class);
		intent.putExtra("storeId", String.valueOf(sqlStoreId));
		startActivity(intent);

	}

	@Override
	protected void threeOrderSend(Func func) {
		super.threeOrderSend(func);
		Intent intent = new Intent(this, Order3SendActivity.class);
		intent.putExtra("storeId", String.valueOf(sqlStoreId));
		startActivity(intent);
	}

	@Override
	protected OrgStore chosedNewOrderStore(Func storeFunc) {
		OrgStore store = null;
		if (storeFunc != null) {
			SubmitItem item = new SubmitItemTempDB(this)
					.findSubmitItemByParamName(String.valueOf(storeFunc.getFuncId()));
			if (item != null && !TextUtils.isEmpty(item.getParamValue())) {// 没输入店面信息
				store = new OrgStoreDB(this).findOrgStoreByStoreId(item.getParamValue());// 根据店面ID查找店面
			}
		}
		return store;
	}

	@Override
	public boolean shouldShowRequestPermissionRationale(@NonNull String permission) {
		return super.shouldShowRequestPermissionRationale(permission);
	}

	/*
         * (non-Javadoc)
         *
         * @see com.gcg.grirms.func.AbsFuncActivity#showPreview()
         */
	@Override
	public void showPreview() {
		try {
			SubmitDB submitDB = new SubmitDB(this);
			int submitId = submitDB.selectSubmitId(planId, wayId, storeId, targetId, menuType);
			if (submitId == Constants.DEFAULTINT) {
				Submit submit = new Submit();
				submit.setPlanId(planId);
				submit.setState(Submit.STATE_NO_SUBMIT);
				submit.setStoreId(storeId);
				submit.setWayId(wayId);
				submit.setTargetid(targetId);
				submit.setTargetType(menuType);
				if (modType != 0) {
					submit.setModType(modType);
				}
				submit.setMenuId(menuId);
				submit.setMenuType(menuType);
				submit.setMenuName(menuName);
				submitDB.insertSubmit(submit);
				submitId = submitDB.selectSubmitIdNotCheckOut(planId, wayId, storeId, targetId, menuType,
						Submit.STATE_NO_SUBMIT);
			} // 等于0表示没有操作任何选项

			Intent intent = new Intent(this, FuncDetailActivity.class);
			intent.putExtra("submitId", submitId);
			intent.putExtra("funcId", linkFuncId);
			intent.putExtra("linkKey", linkKey);
			intent.putExtra("linkId", linkId);// 超链接的时候把上级SubimtID传过去
			intent.putExtra("taskId", taskId);// 双向模块的时候要传到服务端
			intent.putExtra("targetId", targetId);
			intent.putExtra("ableStatus", status);
			intent.putExtra("menuType", menuType);
			intent.putExtra("menuType2", menuType2);
			
			intent.putExtra("codeSubmitControl", codeSubmitControl);// 串码提交控制
			intent.putExtra("bundle", bundle);
			List<Func> hiddenList = null;

			if (menuType == Menu.TYPE_VISIT) {
				hiddenList = new VisitFuncDB(this).findFuncListByType(targetId, Func.TYPE_HIDDEN);
			} else {
				if (modType == Constants.MODULE_TYPE_EXECUTE) {// 双向隐藏取为1的
					hiddenList = new FuncDB(this).findFuncListByType(targetId, Func.TYPE_HIDDEN, "1");
				} else {
					hiddenList = new FuncDB(this).findFuncListByType(targetId, Func.TYPE_HIDDEN, status);
				}
			}

			List<Func> hiddenSql = new ArrayList<Func>();
			if (hiddenList != null) {
				for (int i = 0; i < hiddenList.size(); i++) {
					Func func = hiddenList.get(i);
					if (func.getDefaultType() != null && func.getDefaultType() == Func.DEFAULT_TYPE_COMPUTER) {
						hiddenSql.add(func);
					} else {
						saveHideFunc(func, submitId);
					}
				}
			}

			if (!hiddenSql.isEmpty()) {
				for (int i = 0; i < hiddenSql.size(); i++) {// 存储计算隐藏域
					Func func = hiddenSql.get(i);
					saveHideFunc(func, submitId);
				}
			}

			SubmitItemTempDB sItemDb = new SubmitItemTempDB(this);
			List<Func> list = getFuncList();
			for (int i = 0; i < list.size(); i++) {
				Func textFunc = list.get(i);
				if (textFunc.getType() == Func.TYPE_TEXTCOMP && (textFunc.getDefaultType() != null
						&& textFunc.getDefaultType() == Func.DEFAULT_TYPE_COMPUTER)) {
					SubmitItem item = sItemDb.findSubmitItemBySubmitIdAndFuncId(submitId, textFunc.getFuncId());
					if (item != null) {
						String value = new FuncTreeForLink(this, textFunc, submitId).result;
						item.setParamValue(value);
						sItemDb.updateSubmitItem(item);
					}
				}
			}
			startActivityForResult(intent, R.id.submit_succeeded);
		} catch (NumberFormatException e) {
			ToastOrder.makeText(this, R.string.toast_one1, ToastOrder.LENGTH_SHORT).show();
			JLog.e(e);
		} catch (DataComputeException e) {
			ToastOrder.makeText(getApplicationContext(), e.getMessage(), ToastOrder.LENGTH_SHORT).show();
			JLog.e(e);
		}
	}

	/**
	 * 点击回退按钮清空临时表
	 */
	@Override
	protected void onClickBackBtn() {
		int submitId = new SubmitDB(this).selectSubmitId(planId, wayId, storeId, targetId, menuType);
		new SubmitItemTempDB(this).deleteTempSubmitItem(submitId);// 退出的时候清空临时表
		this.finish();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gcg.grirms.func.AbsFuncActivity#onSaveInstanceState(android.
	 * os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString("status", status);
		outState.putInt("linkKey", linkKey != null ? linkKey : 0);
		outState.putInt("modType", modType);
		super.onSaveInstanceState(outState);
	}

	/**
	 * 读取数据-->非正常状态下使用
	 */
	@Override
	protected void restoreConstants(Bundle savedInstanceState) {
		super.restoreConstants(savedInstanceState);
		modType = savedInstanceState.getInt("modType");
		linkKey = savedInstanceState.getInt("linkKey");
		status = savedInstanceState.getString("status");
	}

	/**
	 * 存储视频信息
	 * 
	 * @param fileName
	 *            照片名称
	 * @return
	 */
	protected int saveVideo(String fileName, Func compFunc) {
		SubmitDB submitDB = new SubmitDB(this);
		SubmitItemTempDB submitItemDB = new SubmitItemTempDB(this);
		int submitId = submitDB.selectSubmitIdNotCheckOut(planId, wayId, storeId, targetId, menuType,
				Submit.STATE_NO_SUBMIT);
		if (submitId != Constants.DEFAULTINT) {// 首先查找有没有该条数据对应的Submit
			SubmitItem submitItem = new SubmitItem();
			submitItem.setSubmitId(submitId);
			submitItem.setParamName(String.valueOf(compFunc.getFuncId()));
			submitItem.setParamValue(fileName);
			submitItem.setType(compFunc.getType());
			submitItem.setIsCacheFun(compFunc.getIsCacheFun());
			boolean isHas = submitItemDB.findIsHaveParamName(submitItem.getParamName(), submitId);// 首先判断数据库中有没有该项操作
			if (isHas) {// 如果有就更新没有就插入 此时不能删除原来的图片，因为用户可能不保存
				submitItemDB.updateSubmitItem(submitItem);
			} else {
				submitItemDB.insertSubmitItem(submitItem);
			}
		} else {// 没有该条数据对应的Submit 在submitDB中先插入一条数据，与该条操作对应。
			Submit submit = new Submit();
			submit.setPlanId(planId);
			submit.setState(Submit.STATE_NO_SUBMIT);
			submit.setStoreId(storeId);
			submit.setWayId(wayId);
			submit.setTargetid(targetId);
			submit.setTargetType(menuType);
			if (modType != 0) {
				submit.setModType(modType);
			}
			submit.setMenuId(menuId);
			submit.setMenuType(menuType);
			submit.setMenuName(menuName);
			long submitid = submitDB.insertSubmit(submit);
			SubmitItem submitItem = new SubmitItem();
			submitItem.setSubmitId(Integer.valueOf(submitid + ""));
			submitItem.setParamName(String.valueOf(compFunc.getFuncId()));
			submitItem.setParamValue(fileName);
			submitItem.setType(compFunc.getType());
			submitItem.setIsCacheFun(compFunc.getIsCacheFun());
			submitItemDB.insertSubmitItem(submitItem);// 插入该submitItem
		}

		return submitId;
	}

}
