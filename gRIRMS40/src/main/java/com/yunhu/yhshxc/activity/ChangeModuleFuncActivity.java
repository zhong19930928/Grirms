package com.yunhu.yhshxc.activity;



import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.LocationResult;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.bo.Module;
import com.yunhu.yhshxc.bo.OrgStore;
import com.yunhu.yhshxc.bo.Submit;
import com.yunhu.yhshxc.bo.SubmitItem;
import com.yunhu.yhshxc.bo.VisitStore;
import com.yunhu.yhshxc.comp.location.LocationControlListener;
import com.yunhu.yhshxc.comp.location.LocationForCheckOut;
import com.yunhu.yhshxc.database.DictDB;
import com.yunhu.yhshxc.database.DoubleDB;
import com.yunhu.yhshxc.database.FuncControlDB;
import com.yunhu.yhshxc.database.FuncDB;
import com.yunhu.yhshxc.database.MainMenuDB;
import com.yunhu.yhshxc.database.ModuleDB;
import com.yunhu.yhshxc.database.SubmitDB;
import com.yunhu.yhshxc.database.SubmitItemDB;
import com.yunhu.yhshxc.database.SubmitItemTempDB;
import com.yunhu.yhshxc.database.VisitFuncDB;
import com.yunhu.yhshxc.database.VisitStoreDB;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.doubletask.DoubleTaskManager;
import com.yunhu.yhshxc.doubletask.DoubleTaskManagerDB;
import com.yunhu.yhshxc.doubletask2.NewDoubleDetailActivity;
import com.yunhu.yhshxc.exception.DataComputeException;
import com.yunhu.yhshxc.func.AbsFuncActivity;
import com.yunhu.yhshxc.func.FuncTree;
import com.yunhu.yhshxc.func.HistorySearchPopupWindow;
import com.yunhu.yhshxc.http.submit.SubmitManager;
import com.yunhu.yhshxc.list.activity.TableListActivity;
import com.yunhu.yhshxc.list.activity.TableListActivityNew;
import com.yunhu.yhshxc.listener.SubmitDataListener;
import com.yunhu.yhshxc.location.LocationMaster;
import com.yunhu.yhshxc.order2.Order2WidgetMainActivity;
import com.yunhu.yhshxc.order3.Order3MainActivity;
import com.yunhu.yhshxc.order3.send.Order3SendActivity;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.CustomizeUtil;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.widget.ToastOrder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gcg.org.debug.ELog;
import gcg.org.debug.JLog;


/**
 * 审核 修改 改派 avtivity
 * 继承AbsFuncActivity页面操作都在父类中实现，数据获取在本类中获取
 * 跳转到该页面的时候不清楚原有数据,退出的时候清除
 * @author jishen
 */
public class ChangeModuleFuncActivity extends AbsFuncActivity  implements SubmitDataListener{
	
	private String status; //二级菜单类型所获取的控件状态
	private Integer linkKey;//超链接控件的控件ID
	private Module module;//自定义模块的实例
	private String sqlLinkJson;//是否是sql超链接 不为空说明是sql超链接跳转过来的
	public SubmitDB submitDB;//提交表单数据库
	public SubmitItemDB submitItemDB;//提交表单的值数据库
	private VisitFuncDB visitFuncDB;//访店控件数据库
	private FuncDB funcDB;//自定义模块控件数据库
	private FuncControlDB funcControlDB;//模块在规定的时间内控件同一个值提交的次数控制
	private DictDB dicDB;//字典数据库
//	private int submitId ;
	private Submit submit;
	private boolean isFromLinkPriview = false;//不是超链接预览界面
	private int isCheckout = 0;
	private String doubleMasterTaskNo;//新双向主任务数据id
	private String doubleButtonType;//新双向button类型 
	private String menuType2;//针对是否是拜访模块跳转进来
	@Override
	public void onCreate(Bundle savedInstanceState) {
		init();//获取传过来的值
		super.onCreate(savedInstanceState);
		isLinkActivity=false;//不是超链接
		submitDB = new SubmitDB(this);
		submitItemDB = new SubmitItemDB(this);
		visitFuncDB = new VisitFuncDB(this);
		funcControlDB = new FuncControlDB(this);
		funcDB = new FuncDB(this);
		dicDB = new DictDB(this);
	}
	
	/**
	 * 获取传过来的值
	 */
	private void init(){
		menuType2 = getIntent().getStringExtra("menuType2");
		bundle = getIntent().getBundleExtra("bundle");
		targetId = bundle.getInt("targetId");//数据ID
		taskId = bundle.getInt("taskId");//任务ID
		modType = bundle.getInt("modType");//模块类型
		status=bundle.getString("status");//二级菜单类型所获取的控件状态
		sqlLinkJson=bundle.getString("sqlLinkJson");//获取是否是sql超链接 
		module=(Module) bundle.getSerializable("module");//自定义模块的实例
//		menuType=Menu.TYPE_MODULE;//设置模块类型为自定义模块
		menuType=bundle.getInt("menuType");//设置模块类型为自定义模块
		bundle.putInt("targetType", menuType);
	}
	

	@Override
	public void initLayout() {
		TextView title=(TextView)findViewById(R.id.tv_titleName);
		title.setText(bundle.getString("buttonName"));
		if (modType==Constants.MODULE_TYPE_REASSIGN) {//改派和下发两种类型的显示用户
			final Func userFunc=new Func();
			userFunc.setName(PublicUtils.getResourceString(this,R.string.func_name));
			userFunc.setFuncId(Func.ISSUE_USER);//随便赋值与CompDialog查询的时候对应
			userFunc.setTargetid(targetId);//就是menuID
			userFunc.setType(Func.TYPE_MULTI_CHOICE_SPINNER_COMP);
			LinearLayout issued=(LinearLayout)findViewById(R.id.ll_visit_checkIn);
			issued.setVisibility(View.VISIBLE);//改派按钮设置可见
			title_finish_iv=(ImageView)findViewById(R.id.title_finish_iv);
			ImageView title_iv=(ImageView) findViewById(R.id.title_iv);
			title_iv.setBackgroundResource(R.drawable.issued);
			issued.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					openCompDialog(userFunc, v,0);//弹出操作dialog
				}
			});
		}
		super.initLayout();
	}
	
	/**
	 * 整个布局中所用到的控件
	 * @return
	 */
	@Override
	public List<Func> getFuncList() {
		List<Func> funcList=null;
		FuncDB funcDB=new FuncDB(this);
		if(!TextUtils.isEmpty(sqlLinkJson)){//是sql超链接跳转过来的
			funcList = funcDB.findFuncListByTargetidReplash(targetId,status,true);
		}else{
			funcList = funcDB.findFuncListByTargetidReplash(targetId,status,false);
		}
		return funcList;
	}

	/*
	 * (non-Javadoc)
	 * @see com.gcg.grirms.func.AbsFuncActivity#getFuncByFuncId(int)
	 */
	@Override
	public Func getFuncByFuncId(int funcId) {
		Func func =new FuncDB(this).findFuncByFuncId(funcId);
		return func;
	}

	/*
	 * (non-Javadoc)
	 * @see com.gcg.grirms.func.AbsFuncActivity#getOrderFuncList(java.lang.Integer, int)
	 */
	@Override
	public ArrayList<Func> getOrderFuncList(Integer funcId, int inputOrder) {
		ArrayList<Func> orderList = new FuncDB(this).findFuncListByInputOrder(targetId, funcId, inputOrder);
		return orderList;
	}

	/*
	 * (non-Javadoc)
	 * @see com.gcg.grirms.func.AbsFuncActivity#getButtonFuncList()
	 */
	@Override
	public List<Func> getButtonFuncList() {
		List<Func> buttonFuncList=new FuncDB(this).findButtonFuncListReplenish(targetId,status);
		return buttonFuncList;
	}

	/*
	 * (non-Javadoc)
	 * @see com.gcg.grirms.func.AbsFuncActivity#findFuncListByInputOrder()
	 */
	@Override
	public Integer[] findFuncListByInputOrder() {
		Integer[] inputOrderArr =new FuncDB(this).findFuncListByInputOrder(targetId);
		return inputOrderArr;
	}

	/*
	 * (non-Javadoc)
	 * @see com.gcg.grirms.func.AbsFuncActivity#intoLink(android.os.Bundle)
	 */
	@Override
	protected void intoLink(Bundle linkBundle) {
		Intent linkIntent=new Intent(this,LinkFuncActivity.class);
		linkBundle.putInt("modType", modType);
		linkIntent.putExtra("bundle", linkBundle);
		startActivity(linkIntent);
	}
	
	@Override
	protected void newOrder(Func func) {
		Func storeFunc = newOrderStoreFunc();
		if (storeFunc !=null) {
			OrgStore store = chosedNewOrderStore(storeFunc);
			if (store!=null) {
				Intent intent = new Intent(this, Order2WidgetMainActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("storeId", String.valueOf(store.getStoreId()));
				bundle.putString("storeName", store.getStoreName());
				bundle.putInt("funcId", func.getFuncId());
				bundle.putInt("orderId",func.getId());
				bundle.putInt("submitId", submitId());
				intent.putExtra("newOrderBundle", bundle);
				startActivity(intent);
			}else{
				ToastOrder.makeText(this, PublicUtils.getResourceString(this,R.string.toast_one3) + storeFunc.getName(), ToastOrder.LENGTH_SHORT).show();
			}
		}else{
			ToastOrder.makeText(this, PublicUtils.getResourceString(this,R.string.toast_one4), ToastOrder.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void threeOrder(Func func) {
		super.threeOrder(func);
		Intent intent = new Intent(this,Order3MainActivity.class);
		startActivity(intent);

	}
	
	@Override
	protected void threeOrderSend(Func func) {
		super.threeOrderSend(func);
		Intent intent = new Intent(this,Order3SendActivity.class);
		startActivity(intent);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.gcg.grirms.func.AbsFuncActivity#showPreview()
	 */
	@Override
	public void showPreview() {
		SubmitDB submitDB=new SubmitDB(this);
		int submitId = submitDB.selectSubmitId(planId, wayId, storeId,targetId, menuType);
		Submit submit=submitDB.findSubmitById(submitId);
		if(submit!=null && modType==Constants.MODULE_TYPE_REASSIGN && TextUtils.isEmpty(submit.getSendUserId())){//改派的时候如果改派的用户ID为空说明没选择要改派给谁，此时提示用户
			ToastOrder.makeText(this, getResources().getString(R.string.reassign_user),ToastOrder.LENGTH_LONG).show();
		}else{
			if (submit==null) {//等于空的时候要插入一条submit
				submit = new Submit();
				submit.setPlanId(planId);
				submit.setState(Submit.STATE_NO_SUBMIT);
				submit.setStoreId(storeId);
				submit.setWayId(wayId);
				submit.setTargetid(targetId);
				submit.setTargetType(menuType);
				if(modType!=0){
					submit.setModType(modType);
				}
				submit.setMenuId(menuId);
				submit.setMenuType(menuType);
				submit.setMenuName(menuName);
				submitDB.insertSubmit(submit);
				submitId = submitDB.selectSubmitIdNotCheckOut(planId, wayId,storeId, targetId, menuType,Submit.STATE_NO_SUBMIT);
			}
			
			if(targetId == 2008455){//格力特殊处理
				CustomizeUtil util = new CustomizeUtil(this);
				util.setModule(module);
				util.setSubmit(submit);
				if (util.checkForGL()) {
					intentToDetail(submitId);
				}
			}else{
				intentToDetail(submitId);
			}
		}
		
	}
	/**
	 * 控制隐藏域
	 * @throws DataComputeException 
	 * @throws NumberFormatException 
	 */
	private void controlHidden(int submitId) throws NumberFormatException, DataComputeException{
		// 存储隐藏类型的
		List<Func> hiddenList = new FuncDB(this).findFuncListByType(targetId,Func.TYPE_HIDDEN,status);
		List<Func> hiddenSql=new ArrayList<Func>();
		if (hiddenList != null) {
			for (int i = 0; i < hiddenList.size(); i++) {
				Func func = hiddenList.get(i);
				if(func.getDefaultType()!=null && func.getDefaultType()==Func.DEFAULT_TYPE_COMPUTER){//计算类型的隐藏域先存储 最后在计算值 否则计算的值可能不正确
					hiddenSql.add(func);
				}else{//保存隐藏域的值
					saveHideFunc(func,submitId);
				}
			}
		}
		
		if(!hiddenSql.isEmpty()){
			for (int i = 0; i < hiddenSql.size(); i++) {//存储计算隐藏域
				Func func = hiddenSql.get(i);
				saveHideFunc(func,submitId);
			}
		}
		
		SubmitItemDB sItemDb = new SubmitItemDB(this);
		List<Func> list=getFuncList();
		for (int i = 0; i < list.size(); i++) {
			Func textFunc=list.get(i);
			if(textFunc.getType()==Func.TYPE_TEXTCOMP){
				SubmitItem item=sItemDb.findSubmitItemBySubmitIdAndFuncId(submitId, textFunc.getFuncId());
				if(item!=null){
					String value=new FuncTree(this,textFunc,submitId).result;
					item.setParamValue(value);
					sItemDb.updateSubmitItem(item,false);
				}
			}
		}
		
	}	
	private int currentTargetId;
	/**
	 * 跳转到详细预览页面
	 * @param submitId 提交表单的表单ID
	 */
	private void intentToDetail(int submitId){
//		Intent intent = new Intent(this, FuncDetailActivity.class);
//		intent.putExtra("submitId", submitId);
//		intent.putExtra("linkKey", linkKey);//超链接控件的控件ID
//		intent.putExtra("linkId", linkId);// 超链接的时候把上级SubimtID传过去
//		intent.putExtra("taskId", taskId);// 双向模块的时候要传到服务端
//		intent.putExtra("targetId", targetId);//数据ID
//		intent.putExtra("ableStatus", status);//二级菜单类型所获取的控件状态
//		intent.putExtra("modType", modType);//模块类型
//		intent.putExtra("sqlLinkJson", sqlLinkJson);//是否是超链接外部数据
//		intent.putExtra("menuType", menuType);
//		intent.putExtra("priviewBundle", bundle);
//		intent.putExtra("codeSubmitControl", codeSubmitControl);//串码提交控制
//		intent.putExtra("isNoWait", isNoWait);//是否提交无等待
//		intent.putExtra("bundle", bundle);
		try {
			controlHidden(submitId);//存储隐藏域
//			startActivityForResult(intent,R.id.submit_succeeded);
			
			
			
			
		} catch (NumberFormatException e) {//下拉框其他计算格式出错异常
			ToastOrder.makeText(this,PublicUtils.getResourceString(this,R.string.toast_one1), ToastOrder.LENGTH_SHORT).show();
			JLog.e(e);
		} catch (DataComputeException e) {
			ToastOrder.makeText(getApplicationContext(), e.getMessage(), ToastOrder.LENGTH_LONG).show();
			JLog.e(e);
		}
		submitDataLayout.setEnabled(false);
		if (bundle!=null) {
			doubleMasterTaskNo = bundle.getString(NewDoubleDetailActivity.DOUBLE_MASTER_TASK_NO);
			doubleButtonType = bundle.getString(NewDoubleDetailActivity.DOUBLE_BTN_TYPE);
		}
		submit = submitDB.findSubmitById(submitId);// 首先查询Submit
		currentTargetId = submit.getTargetid();//当前数据ID
		
		if (linkId != Constants.DEFAULTINT) {//超链接预览
			if (codeSubmitControl() && isCompletion(submitId)) {
				controlLink(submitId);
			}
		}else{
			if (menuType == Menu.TYPE_VISIT) {// 首先检测完整性控制 访店提交
				if (usableControl() && codeSubmitControl() && isCompletion(submitId)) {
					submitVisit();
				}
			}else if (menuType == Menu.TYPE_NEW_TARGET){//新双向
				if (usableControl() && codeSubmitControl() && submitForBaseTime(submitId) && isCompletion(submitId)) {
					if (modType == Constants.MODULE_TYPE_EXECUTE_NEW && infoType == 0) {
						submitNewDouble(submitId);
					}
				}
			}else if(menuType == Menu.TYPE_NEARBY && isCompletion(submitId)){//就近拜访
				submitNearbyVisit(submitId);
			} else if (menuType == Menu.TYPE_MODULE) {// 自定义情况下只改变提交状态和存入时间戳
				if (usableControl() && codeSubmitControl() && submitForBaseTime(submitId) && isCompletion(submitId)) {
					//自定义 执行 上报  店面信息修改
					if ((modType == Constants.MODULE_TYPE_EXECUTE || modType == Constants.MODULE_TYPE_REPORT  || modType == Constants.DEFAULTINT) && infoType == 0) {
						submitModuleOrDouble(submitId);
					}else {
						submitReplenish(submitId);//补报提交
					}
				}
			}
		}
	}
	/**
	 * 手机可用时间段控制
	 * @param
	 * @return
	 */
	public boolean usableControl(){
		boolean flag = true;
		String time = null;
		Menu menu = null;
		if (menuType == Menu.TYPE_VISIT || menuType == Menu.TYPE_ATTENDANCE || menuType == Menu.TYPE_NEW_ATTENDANCE) {
			menu = new MainMenuDB(this).findMenuListByMenuType(menuType);
		}else{
			menu = new MainMenuDB(this).findMenuListByMenuId(targetId);
		}
//		time = "12:00|0.5,14:55|0.5";
		time = menu!=null?menu.getPhoneUsableTime():"";
		MenuUsableControl  control = new MenuUsableControl();
		flag = control.isCanUse(time);
		if (menuType == Menu.TYPE_ORDER3) {
			if (flag) {//时间过了判断天
				flag = control.isOrder3CanUse(this);
				if (!flag) {
					showTip(PublicUtils.getResourceString(this,R.string.toast_one8));
					submitDataLayout.setEnabled(true);
				}
			}else{
				showTip(control.tipInfo(time));
				submitDataLayout.setEnabled(true);
			}
		}else{
			if (!flag) {
				showTip(control.tipInfo(time));
				submitDataLayout.setEnabled(true);
			}
		}
		return flag;
	}
	/**
	 * 用串码的时候控制是否提交
	 * @return
	 */
	private boolean codeSubmitControl(){
		boolean flag = true;
		if (codeFunc !=null) {
			String codeControl = codeFunc.getCodeControl();
			if ("4".equals(codeControl) || "3".equals(codeControl)) {//控制
				flag = codeSubmitControl;
			}
			if (!flag) {
				ToastOrder.makeText(this, R.string.toast_one, ToastOrder.LENGTH_SHORT).show();
				submitDataLayout.setEnabled(true);
			}
		}
		return flag;
	}
	/*
	 * 完整性控制
	 */
	public boolean isCompletion(int submitId) {
		boolean isCompletion = true; // 默认操作完整
		Submit isCompletionSubmit = submitDB.findSubmitById(submitId); // 首先查询要提交的Submit
		if (isCompletionSubmit!=null) {
			int currentSubmitId = isCompletionSubmit.getId();
			int currentTargetId = isCompletionSubmit.getTargetid();
			// 根据Submitid找到该ID对应的应经保存的一组数据
			List<Func> isEmptyList=new ArrayList<Func>(); // 查找所有必须操作的List集合
			if (menuType == Menu.TYPE_VISIT) { // 访店
				isEmptyList = visitFuncDB.findFuncListByIsEmpty(1, currentTargetId);// 查找所有不能为空的选项
			} else if(menuType == Menu.TYPE_VISIT){
				isEmptyList = funcDB.findNearbyFuncListIsEmpty(1, currentTargetId);// 查找所有不能为空的选项
			} else {//自定义模块
				switch (modType) {
				case Constants.MODULE_TYPE_EXECUTE:
					isEmptyList = funcDB.findFuncListByIsEmpty(1, currentTargetId,"1"); // 查找双向所有不能为空的选项
					break;
				case Constants.MODULE_TYPE_QUERY://查询
				case Constants.MODULE_TYPE_VERIFY://审核
				case Constants.MODULE_TYPE_REASSIGN://改派
					isEmptyList = funcDB.findFuncListByIsEmpty(1, currentTargetId,status);
					break;
				case Constants.MODULE_TYPE_ISSUED://下发
				case Constants.MODULE_TYPE_REPORT://上报
					isEmptyList = funcDB.findFuncListByIsEmpty(1,currentTargetId, "0"); // 上报和下发的情况
					break;
				default:
					isEmptyList = funcDB.findFuncListByIsEmpty(1, currentTargetId,status);
					break;
				}
			}
			int isEmptyListLen = isEmptyList.size();
			if (isEmptyListLen != 0) { // 说明有选项必须要操作
				for (int i = 0; i < isEmptyListLen; i++) { // 2012-10-29 houyu修改 for EnterMustList
					Func isEmptyFunc = isEmptyList.get(i); // 获取必须要操作的Func
					SubmitItem item=null;
					if(linkId!=0 && !isFromLinkPriview){//超链接查找临时表
						item = new SubmitItemTempDB(this).findSubmitItemByParamNameAndSubmitId(String.valueOf(isEmptyFunc.getFuncId()), currentSubmitId);
					}else{
						item = submitItemDB.findSubmitItemByParamNameAndSubmitId(String.valueOf(isEmptyFunc.getFuncId()), currentSubmitId);
					}
					
					if (item == null || TextUtils.isEmpty(item.getParamValue())) {
						if (!TextUtils.isEmpty(isEmptyFunc.getEnterMustList())) { // 如果此处有值，要判断条件是否满足，满足时，才是必填
							boolean isEmptyByEnterMustList=false;
							try {
								if(linkId!=0 && !isFromLinkPriview){
									isEmptyByEnterMustList = new SubmitItemTempDB(this).isEmptyByEnterMustList(currentSubmitId,isEmptyFunc.getEnterMustList(),menuType);
								}else{
									isEmptyByEnterMustList = submitItemDB.isEmptyByEnterMustList(currentSubmitId,isEmptyFunc.getEnterMustList(),menuType);
								}
							} catch (NumberFormatException e) {
								ToastOrder.makeText(this,R.string.toast_one1, ToastOrder.LENGTH_SHORT).show();
								JLog.e(e);
								break;
							} catch (DataComputeException e) {
								ToastOrder.makeText(ChangeModuleFuncActivity.this,e.getMessage(),ToastOrder.LENGTH_SHORT).show();
								JLog.e(e);
								break;
							}
							if (isEmptyByEnterMustList) { // 如果不满足，为不必填
								isCompletion = false;
								ToastOrder.makeText(ChangeModuleFuncActivity.this,PublicUtils.getResourceString(ChangeModuleFuncActivity.this,R.string.toast_one2)+"\n\"" + isEmptyFunc.getName() + "\"",ToastOrder.LENGTH_SHORT).show();
								break;
							} else {
								continue;
							}
						} else { // 此处无值为必填栏位
							isCompletion = false;
							ToastOrder.makeText(ChangeModuleFuncActivity.this,PublicUtils.getResourceString(ChangeModuleFuncActivity.this,R.string.toast_one2)+"\"" + isEmptyFunc.getName() + "\"",ToastOrder.LENGTH_SHORT).show();
							break;
						}
					}
				}
			}
		}
		if (!isCompletion) {
			submitDataLayout.setEnabled(true);
		}
		return isCompletion;
	}
	/**
	 * 超链接点击保存
	 */
	private ArrayList<String> orgMapValueList;//超链接预览编辑跳转到上报页面的时候把机构关系传过去
	private void controlLink(int submitId){
		if(isFromLinkPriview){
			Intent linkIntent=new Intent(this,LinkFuncActivity.class);
			Bundle priviewBundle=new Bundle();
			priviewBundle.putInt("modType", modType);
			priviewBundle.putInt("targetId", targetId);
			priviewBundle.putInt("linkId", linkId); //下级连接用来查找上级submitid,以便保存patchId
			priviewBundle.putInt("taskId", taskId);// 双向模块的时候要传到服务端
			priviewBundle.putInt("linkKey", linkKey);//当前func的id提交的时候存储超链接的值使用
			priviewBundle.putInt("storeId", submit.getStoreId());
			priviewBundle.putInt("menuType", Menu.TYPE_MODULE);
			if(orgMapValueList!=null && !orgMapValueList.isEmpty()){
				priviewBundle.putStringArrayList("map", orgMapValueList);
			}
			linkIntent.putExtra("bundle", priviewBundle);
			startActivity(linkIntent);
			this.finish();
		}else{
			SubmitItemTempDB submitItemTempDB = new SubmitItemTempDB(this);
			SubmitItem item = new SubmitItem(); // 上一级item
			item.setTargetId(targetId+"");
			item.setSubmitId(linkId);
			item.setParamName(linkKey + "");
			if ((TextUtils.isEmpty(submit.getTimestamp()))) {
				submit.setTimestamp(String.valueOf(System.currentTimeMillis()));
			}
			item.setParamValue(submit.getTimestamp());
			item.setType(Func.TYPE_LINK);
			boolean isHas = submitItemDB.findIsHaveParamName(
					item.getParamName(), linkId);
			if(isHas){
				submitItemDB.updateSubmitItem(item,false);
			}else{
				submitItemDB.insertSubmitItem(item,false);
			}
			
			/**
			 * 临时表中也添加数据添加，否则保存外层预览的时候查询不到该链接
			 */
			boolean isHasTemp = submitItemTempDB.findIsHaveParamName(
					item.getParamName(), linkId);
			if(isHasTemp){
				submitItemTempDB.updateSubmitItem(item);
			}else{
				submitItemTempDB.insertSubmitItem(item);
			}
			
			
			
			Func linkFunc = new VisitFuncDB(this).findFuncByFuncId(linkKey);
			if (linkFunc == null) {//无法区分超链接Func是拜访中的还是自定义中的所以两个表都查
				linkFunc = new FuncDB(this).findFuncByFuncId(linkKey);
			}
			if (linkFunc!=null) {
				submit.setContentDescription(linkFunc.getName());
			}
			
			submit.setParentId(linkId);
			submitDB.updateSubmit(submit);
			
			//jishen2013-02-25保存要提交的临时表中的数据
			List<SubmitItem> submitItemTempList = new SubmitItemTempDB(this).findSubmitItemBySubmitId(submitId);// 根据Submitid找到该ID对应的一组数据
			new SubmitItemDB(this).deleteSubmitItemBySubmitId(submitId);// 删除原来表中的所有数据
			for (int i = 0; i < submitItemTempList.size(); i++) {//将临时表中的数据添加到提交表中
				SubmitItem tempItem=submitItemTempList.get(i);
				tempItem.setTargetId(targetId+"");
//				boolean flag = submitItemDB.findIsHaveParamName(tempItem.getParamName(), submitId);
//				if(flag){
//					submitItemDB.updateSubmitItem(tempItem);
//				}else{
//					submitItemDB.insertSubmitItem(tempItem);
//				}
				submitItemDB.insertSubmitItem(tempItem,false);
				new SubmitItemTempDB(this).deleteSubmitItem(tempItem);
			}
			
//			thisFinish(R.id.submit_succeeded,null);
			onActivityResultAbs(R.id.submit_succeeded, R.id.submit_succeeded, null);
			JLog.d(TAG, "超链接添加Timestamp ===>" + submit.getTimestamp());
		}
	}
	/**
	 * 访店数据提交
	 */
	
	private void submitVisit() {
		if (isCheckout == 1) {
			checkOutLocation();
		} else {
			sureCheckOut(null);
		}
	}
	/**
	 * 确认checkOUT
	 */
	private void sureCheckOut(LocationResult locationResult) {
		if (isCheckout == 1) {//插入离店定位内容
			if(locationResult!=null){
				if (this.getResources().getString(R.string.location_success_no_addr).equals(locationResult.getAddress())||PublicUtils.checkAddress(locationResult.getAddress())) {
					locationResult.setAddress("");
				}
				String location = new StringBuffer()
				.append(locationResult.getLongitude())
				.append("$").append(locationResult.getLatitude())
				.append("$").append(locationResult.getAddress())
				.append("$").append(locationResult.getLocType())
				.append("$").append(locationResult.getRadius())
				.append("$").append(locationResult.getPosType())
				.append("$").append(LocationMaster.ACTION)
				.append("$").append(LocationMaster.VERSION).toString();
				submit.setCheckoutGis(location);
			}
			submit.setCheckoutTime(DateUtil.getCurDateTime());
		}
		submit.setState(Submit.STATE_SUBMITED);//改变数据状态为提交状态
		Constants.ISCHECKOUT = true;//离店定位以后修改访店的访问时间和访店状态
		VisitStoreDB visitStoreDB = new VisitStoreDB(this);
		VisitStore visitStore = visitStoreDB.findVisitStore(submit.getWayId(),submit.getTargetid(), submit.getStoreId(), submit.getPlanId());
		if (visitStore != null) {
			submit.setVisitNumbers(visitStore.getSubmitNum()+1);
		}
		if (TextUtils.isEmpty(submit.getTimestamp())) {
			submit.setTimestamp(String.valueOf(System.currentTimeMillis()));
		}
		submitDB.updateSubmit(submit);
		startSubmitService(submitId());//开始提交
	}
	/**
	 * checkout定位
	 */
	private LocationForCheckOut locationForCheckOut;
	private void checkOutLocation(){
		Bundle lBundle = new Bundle();
		lBundle.putString("storelon", bundle.getString("storelon"));
		lBundle.putString("storelat", bundle.getString("storelat"));
		lBundle.putInt("isCheck", bundle.getInt("isCheck"));
		lBundle.putInt("storeDistance", bundle.getInt("storeDistance"));
		lBundle.putString("is_address", bundle.getString("is_address"));//默认1显示
		lBundle.putString("is_anew_loc", bundle.getString("is_anew_loc"));//默认0不重新定位
		lBundle.putString("loc_type",bundle.getString("loc_type") );//默认2选择所有定位方式
		Dialog dialog = new MyProgressDialog(this,R.style.CustomProgressDialog,getResources().getString(R.string.waitForAMoment));
		locationForCheckOut = new LocationForCheckOut(this,new LocationControlListener() {
			
			@Override
			public void confirmLocation(LocationResult result) {
				sureCheckOut(result);// 确认提交
			}
			
			@Override
			public void areaSearchLocation(LocationResult result) {
				
			}
		});
		locationForCheckOut.setLoadDialog(dialog);
		locationForCheckOut.setBundle(lBundle);
		locationForCheckOut.startLocation();
	}
	/**
	 * 自定义上报判断基于时间的提交
	 * @return true允许提交 false 不允许提交
	 */
	private boolean submitForBaseTime(int submitId){
		boolean flag=true;
		
		if (linkId != Constants.DEFAULTINT) {//超链接直接通过
			return flag;
		}
		Menu currentMenu=new MainMenuDB(this).findMenuListByMenuId(targetId);
		Integer moduleType=null;
		if(currentMenu !=null){
			moduleType=currentMenu.getModuleType();
		}else{
			return flag;
		}
		Date date=new Date();
		int currentTime=Integer.parseInt((DateUtil.getHour(date)+100+"").substring(1)+(DateUtil.getMin(date)+100+"").substring(1));
		int baseTime=Integer.parseInt(TextUtils.isEmpty(currentMenu.getBaseTime())?currentTime+"" :currentMenu.getBaseTime().replace(":", ""));//超过时间不让提交或者让提交但是日期改为明天的
		if(moduleType!=null){
			if(moduleType==Menu.MENU_MODULE_TYPE_2){//超时后不能再提交
				if(currentTime>baseTime){
					flag=false;
					ToastOrder.makeText(this, PublicUtils.getResourceString(this,R.string.toast_one5), ToastOrder.LENGTH_LONG).show();
				}
			}else if(moduleType==Menu.MENU_MODULE_TYPE_3){// 把提交时间改成明天
				if(currentTime>baseTime){
					Func hiddenFunc=new FuncDB(this).findFuncListByTargetIdAndDefaultType(targetId, Func.DEFAULT_TYPE_BASE_TIEM);//基于时间的隐藏域
					if(hiddenFunc!=null){
						SubmitItemDB submitItemDB=new SubmitItemDB(this);
						SubmitItem item =submitItemDB.findSubmitItemBySubmitIdAndFuncId(submitId, hiddenFunc.getFuncId());
						if(item!=null){
							item.setParamValue(DateUtil.getTomorrowDate());
							submitItemDB.updateSubmitItem(item,false);
						}
					}
				}
			}
		}
		return flag;
	}
	
	/**
	 * 双向或者自定义提交
	 */
	private void submitNewDouble(int submitId) {
		submit.setState(Submit.STATE_SUBMITED);
		if ("1".equals(doubleButtonType) || "-99".equals(doubleButtonType)) {//接受  和动态按钮 的时候是修改主数据 传doubleMasterTaskNo
			submit.setTimestamp(doubleMasterTaskNo);
		}else if("2".equals(doubleButtonType)){//上报的时候自己生成时间戳
			submit.setTimestamp(String.valueOf(System.currentTimeMillis()));
		}else{//修改数据的时候时间戳是服务器传过来的值
			if ((TextUtils.isEmpty(submit.getTimestamp()))) {
				submit.setTimestamp(String.valueOf(System.currentTimeMillis()));
			}
		}
		//新双向
		if (modType == Constants.MODULE_TYPE_EXECUTE_NEW) {
			submit.setDoubleBtnType(doubleButtonType);
			submit.setDoubleMasterTaskNo(doubleMasterTaskNo);
		}
		submitDB.updateSubmit(submit);
//		Constants.ISDOUBLERETURN = true;
		Constants.ISCHECKOUTMODUL = true;
		startSubmitService(submitId);
	}
	
	/**
	 * 双向或者自定义提交
	 */
	private void submitModuleOrDouble(int submitId) {
		submit.setState(Submit.STATE_SUBMITED);
		if ((TextUtils.isEmpty(submit.getTimestamp()))) {
			submit.setTimestamp(String.valueOf(System.currentTimeMillis()));
		}
		// 旧双向如果是双向提交完以后删除该条记录
		if (modType == Constants.MODULE_TYPE_EXECUTE) {
			if (linkId == Constants.DEFAULTINT && TextUtils.isEmpty(sqlLinkJson)) {// 双向如果是超链接过来就不删除 超链接外部数据不删除
				removeDoubleTask(taskId, currentTargetId);
				
				DoubleTaskManagerDB dbs = new DoubleTaskManagerDB(this);
				boolean isHas = dbs.isHasDoubleTaskManager(currentTargetId, taskId);
				if (!isHas) {
					DoubleTaskManager m = new DoubleTaskManager();
					m.setDataId(taskId);
					m.setMenuId(currentTargetId);
					dbs.insert(m);
					dbs.controlDoubleTaskManagerNum();
				}
		}
		
		}
		submitDB.updateSubmit(submit);
		Constants.ISCHECKOUTMODUL = true;
		startSubmitService(submitId);
	}
	/**ƒ
	 * 双向模块提交以后删除该条记录
	 * 
	 * @param taskId 任务ID 
	 * @param targetId 数据ID
	 */
	private void removeDoubleTask(int taskId, int targetId) {
		DoubleDB doubleDB = new DoubleDB(this);
		doubleDB.removeDoubleTask(String.valueOf(taskId),String.valueOf(targetId));
	}
	
	/**
	 * 就近拜访
	 */
	private void submitNearbyVisit(int submitId){
		submit.setState(Submit.STATE_SUBMITED);
		if ((TextUtils.isEmpty(submit.getTimestamp()))) {
			submit.setTimestamp(String.valueOf(System.currentTimeMillis()));
		}
		submitDB.updateSubmit(submit);
		Constants.ISCHECKOUTMODUL = true;
		startSubmitService(submitId);
	}

	/**
	 * 开启提交服务
	 */
	private void startSubmitService(int submitId) {
		List<SubmitItem> submitItemList = submitItemDB.findSubmitItemBySubmitId(submitId);// 根据Submitid找到该ID对应的一组数据
		for (int i = 0; i < submitItemList.size(); i++) {
			funcControlDB.updateFuncControlSubmitState(submitItemList.get(i).getParamName(), targetId);
		}

		funcControlDB.removeFuncControlByState(0);// 删除没提交的
		updateLinkSubmitState(submitId);//修改超链接提交表单数据状态
		submit();
	}
	/**
	 * 提交数据
	 * 
	 */
	public void submit(){
//		isNoWait = false;
		if (submit!=null) {
			submit.setContentDescription(contentDescription());
			submitDB.updateSubmit(submit);
		}
		SubmitManager.getInstance(this).submitData(isNoWait, this);
	}
	public String contentDescription(){
		String contentDescription = "";
		if (menuType == Menu.TYPE_VISIT) {
			contentDescription = submit.getWayName()+"-"+submit.getStoreName();
		}else if (menuType == Menu.TYPE_NEW_TARGET){//新双向
			int targetid = submit.getTargetid();
			Module doubleModule = new ModuleDB(this).findModuleByTargetId(targetid, Constants.MODULE_TYPE_EXECUTE_NEW);
			if (doubleModule!=null) {
				contentDescription = doubleModule.getName();
			}
		}else if(menuType == Menu.TYPE_NEARBY){
			int targetid = submit.getTargetid();
			Menu menu = new MainMenuDB(this).findMenuListByMenuId(targetid);
			if (menu!=null) {
				contentDescription = menu.getName();
			}
		} else if (menuType == Menu.TYPE_MODULE) {// 自定义
			if (submit.getModType()!=null) {
				int targetid = submit.getTargetid();
				Module module = new ModuleDB(this).findModuleByTargetId(targetid, submit.getModType());
				if (module!=null) {
					contentDescription = module.getName();
				}
			}
		}
		return contentDescription;
	}
	/**
	 * 更改超链接提交状态
	 * @param parentId 上层的表单ID
	 */
	private void updateLinkSubmitState(int parentId){
		//超链接的情况更改子链接的submit的提交状态
		List<Submit> linkSubmitList=submitDB.findSubmitByParentId(parentId);
		if(!linkSubmitList.isEmpty()){
			submitDB.updateSubmitStateByParentId(parentId);
			for (int i = 0; i < linkSubmitList.size(); i++) {
				Submit linkSubmit=linkSubmitList.get(i);
				updateLinkSubmitState(linkSubmit.getId());
			}
		}
	}
	/**
	 * 补报数据提交
	 */
	private void submitReplenish(int submitId) {
		submit.setState(Submit.STATE_SUBMITED);
		if ((TextUtils.isEmpty(submit.getTimestamp()))) {
			submit.setTimestamp(String.valueOf(System.currentTimeMillis()));
		}
		submitDB.updateSubmit(submit);
		Constants.ISCHECKOUTMODUL = true;
		startSubmitService(submitId);
	}
	/**
	 * 手机可使用时间段控制提醒
	 */
	private LinearLayout ll_tip;
	private void showTip(String info){
		ll_tip = (LinearLayout)findViewById(R.id.ll_menu_usable_tip);
		TextView tv_tip = (TextView)findViewById(R.id.tv_menu_usable_tip);
		ll_tip.setVisibility(View.VISIBLE);
		tv_tip.setText(info);
		ll_tip.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ll_tip.setVisibility(View.GONE);
			}
		});
		
	}
	/*
	 * 退出的时候清除数据
	 * (non-Javadoc)
	 * @see com.gcg.grirms.func.AbsFuncActivity#onClickBackBtn()
	 */
	@Override
	protected void onClickBackBtn() {
		
		if(modType==Constants.MODULE_TYPE_VERIFY || modType==Constants.MODULE_TYPE_UPDATE || modType==Constants.MODULE_TYPE_REASSIGN){//审核情况(补报过来)
			ELog.d("Open TableListActivity");
			Intent intent=new Intent(this,TableListActivityNew.class);
			Bundle bundle = new Bundle();
			bundle.putInt("targetId", module!=null?module.getMenuId():targetId);
			bundle.putInt("modType",  module!=null?module.getType():modType);
			bundle.putInt("menuType", menuType);
			bundle.putSerializable("module", module);
			if (!TextUtils.isEmpty(sqlLinkJson)) {
				bundle.putBoolean(TableListActivity.TAG_IS_LINK, true);
			}
			intent.putExtra("sqlLinkJson", sqlLinkJson);
			bundle.putInt("menuId", menuId);
			bundle.putString("menuName", menuName);
			intent.putExtra("bundle", bundle);
			startActivity(intent);
		}
		cleanCurrentNoSubmit(targetId);//清除未提交的数据
		if(bundle.getBoolean("isLinkHistory")){//如果是超链接历史信息查询返回的时候不关闭查询页面
			this.setResult(R.id.link_history_return);
		}
		this.finish();
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
//		JLog.d(TAG, " Activity 正在被非正常关闭 ! ");
		outState.putString("status", status);
		outState.putInt("linkKey",linkKey!=null?linkKey:0);
		super.onSaveInstanceState(outState);
	}
	
	/**
	 * 读取数据-->非正常状态下使用
	 */
	@Override
	protected void restoreConstants(Bundle savedInstanceState) {
		super.restoreConstants(savedInstanceState);
		linkKey=savedInstanceState.getInt("linkKey");
		status =savedInstanceState.getString("status");
	}

	@Override
	public void submitReceive(boolean isSuccess) {
		submitDataLayout.setEnabled(true);
		if (isSuccess) {
			if (menuType == Menu.TYPE_VISIT) {//拜访店面提交成功的时候店面提交个数要加1
				VisitStoreDB visitStoreDB = new VisitStoreDB(this);
				VisitStore visitStore = visitStoreDB.findVisitStore(submit.getWayId(),submit.getTargetid(), submit.getStoreId(), submit.getPlanId());
				if (visitStore != null) {
					visitStore.setSubmitNum(visitStore.getSubmitNum()+1);
					visitStore.setSubmitDate(DateUtil.getCurDate());
					visitStoreDB.updateVisitStoreById(visitStore);
				}
			}
			Intent intent = null;
			if (menuType == Menu.TYPE_MODULE) {// 自定义情况
				if (modType == Constants.MODULE_TYPE_EXECUTE) {//双向提交
					intent = new Intent();
					intent.putExtra("doubleSubmit", true);
				}
			}
//			thisFinish(R.id.submit_succeeded,intent);
			onActivityResultAbs(R.id.submit_succeeded, R.id.submit_succeeded, intent);
			Toast.makeText(ChangeModuleFuncActivity.this, PublicUtils.getResourceString(ChangeModuleFuncActivity.this,R.string.toast_one6), Toast.LENGTH_SHORT).show();
			if (!TextUtils.isEmpty(menuType2)&&menuType2.equals(Menu.TYPE_VISIT+"")) {//如果是拜访,提交成功后关闭本页面以及上一级页面
				Intent closeIntent = new Intent();
				closeIntent.setAction(HistorySearchPopupWindow.CLOSE_ACTION);
				sendBroadcast(closeIntent);
			}
		}else{
			if (submit != null) {
				submit.setState(Submit.STATE_NO_SUBMIT);
				submitDB.updateSubmit(submit);
				Toast.makeText(ChangeModuleFuncActivity.this, PublicUtils.getResourceString(ChangeModuleFuncActivity.this,R.string.toast_one7), Toast.LENGTH_SHORT).show();;
			}
		}
		
	}
	
}
