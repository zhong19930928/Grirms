package com.yunhu.yhshxc.nearbyVisit;

import gcg.org.debug.JLog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.LinkFuncActivity;
import com.yunhu.yhshxc.activity.MenuUsableControl;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.LocationResult;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.bo.Module;
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
import com.yunhu.yhshxc.func.FuncTreeForVisit;
import com.yunhu.yhshxc.http.submit.SubmitManager;
import com.yunhu.yhshxc.listener.SubmitDataListener;
import com.yunhu.yhshxc.location.LocationMaster;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtilForNearby;
import com.yunhu.yhshxc.widget.ToastOrder;

public class NearbyVisitFuncActivity extends AbsFuncActivity implements SubmitDataListener{
	
	/**
	 * 二级菜单类型所获取的控件
	 */
	private String storeName;
	private int storeId;
	private String patchId;
	
	public SubmitDB submitDB;//提交表单数据库
	public SubmitItemDB submitItemDB;//提交表单的值数据库
	private VisitFuncDB visitFuncDB;//访店控件数据库
	private FuncDB funcDB;//自定义模块控件数据库
	private FuncControlDB funcControlDB;//模块在规定的时间内控件同一个值提交的次数控制
	private DictDB dicDB;//字典数据库
	private boolean isFromLinkPriview = false;//不是超链接预览界面
	private Submit submit;
	private String doubleMasterTaskNo;//新双向主任务数据id
	private String doubleButtonType;//新双向button类型 
	private int  submitId;
	public String ableStatus;//结点数据状态
	private int isCheckout;//是否离店定位 0不要离店定位 1需要离店定位
	private String sqlLinkJson;//是否是sql超链接 不为空说明是sql超链接跳转过来的
	private ArrayList<String> orgMapValueList;//超链接预览编辑跳转到上报页面的时候把机构关系传过去
	private Integer linkKey;//超链接控件的控件ID
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		submitDB = new SubmitDB(this);
		submitItemDB = new SubmitItemDB(this);
		visitFuncDB = new VisitFuncDB(this);
		funcControlDB = new FuncControlDB(this);
		funcDB = new FuncDB(this);
		dicDB = new DictDB(this);
		init();
		setTitleName();
	}
	
	private void setTitleName(){
		//设置顶部标题
		TextView title=(TextView)findViewById(R.id.tv_titleName);
		title.setText(storeName);
	}
	/**
	 * 获取传过来的数据
	 */
	private void init(){
		bundle = getIntent().getBundleExtra("bundle");
		targetId = bundle.getInt("targetId");//数据ID
		storeId = bundle.getInt("storeId");
		storeName = bundle.getString("storeName");
		patchId = bundle.getString("patchId");
		menuType=Menu.TYPE_NEARBY;//设置为自定义类型
		bundle.putInt("targetType", menuType);//把类型存入bundle 储存提交数据和查询提交数据的时候使用
	}
	

	@Override
	protected void intoLink(Bundle linkBundle) {
		Intent linkIntent=new Intent(this,LinkFuncActivity.class);
		linkIntent.putExtra("bundle", linkBundle);
		startActivity(linkIntent);
	}

	@Override
	public List<Func> getFuncList() {
		FuncDB funcDB=new FuncDB(this);
		List<Func> funcList = funcDB.findNearbyFuncList(targetId,SharedPreferencesUtilForNearby.getInstance(this).getNearbyDataStatus(String.valueOf(targetId)));
		return funcList;
	}

	@Override
	public List<Func> getButtonFuncList() {
		List<Func> buttonFuncList=new FuncDB(this).findButtonFuncListReplenish(targetId,SharedPreferencesUtilForNearby.getInstance(this).getNearbyDataStatus(String.valueOf(targetId)));
		return buttonFuncList;
	}

	@Override
	public Func getFuncByFuncId(int funcId) {
		Func func =new FuncDB(this).findFuncByFuncId(funcId);
		return func;
	}

	@Override
	public Integer[] findFuncListByInputOrder() {
		Integer[] inputOrderArr =new FuncDB(this).findFuncListByInputOrder(targetId);
		return inputOrderArr;
	}

	@Override
	public ArrayList<Func> getOrderFuncList(Integer funcId, int inputOrder) {
		ArrayList<Func> orderList = new FuncDB(this).findFuncListByInputOrder(targetId, funcId, inputOrder);
		return orderList;
	}

	@Override
	public void showPreview() {
		SubmitDB submitDB=new SubmitDB(this);
		 submitId = submitDB.selectSubmitId(planId, wayId, storeId,targetId, menuType);
		if (submitId != Constants.DEFAULTINT) {// 等于0表示没有操作任何选项 提示用户不能预览否则跳转到预览
			intentToDetail(submitId);
		}else {
			ToastOrder.makeText(this, getResources().getString(R.string.priview),ToastOrder.LENGTH_LONG).show();
		}
	}

	/**
	 * 控制隐藏域  存储隐藏域的值，计算隐藏域的值
	 * @throws DataComputeException 
	 * @throws NumberFormatException 
	 */
	private void controlHidden(int submitId) throws NumberFormatException, DataComputeException{
		//查找所有的隐藏域类型的控件
		List<Func> hiddenList = new FuncDB(this).findFuncListByType(targetId,Func.TYPE_HIDDEN,SharedPreferencesUtilForNearby.getInstance(this).getNearbyDataStatus(String.valueOf(targetId)));
		List<Func> hiddenSql=new ArrayList<Func>();//存储计算类型的隐藏域，要后存储，否则数据有可能不正确
		if (hiddenList != null) {//如果有隐藏域的话
			for (int i = 0; i < hiddenList.size(); i++) {
				Func func = hiddenList.get(i);
				if(func.getDefaultType()!=null && func.getDefaultType()==Func.DEFAULT_TYPE_COMPUTER){
					hiddenSql.add(func);
				}else{
					saveHideFunc(func,submitId);//存数隐藏域的数据
				}
			}
		}
		//存储计算类型的隐藏域
		if(!hiddenSql.isEmpty()){
			for (int i = 0; i < hiddenSql.size(); i++) {//存储计算隐藏域
				Func func = hiddenSql.get(i);
				saveHideFunc(func,submitId);
			}
		}
		//文本类型的存储计算的值
		SubmitItemDB sItemDb = new SubmitItemDB(this);
		for (int i = 0; i < getFuncList().size(); i++) {
			Func textFunc=getFuncList().get(i);
			if(textFunc.getType()==Func.TYPE_TEXTCOMP){
				SubmitItem item=sItemDb.findSubmitItemBySubmitIdAndFuncId(submitId, textFunc.getFuncId());
				if(item!=null){
					String value= new FuncTreeForVisit(this,textFunc,submitId).result;
					item.setParamValue(value);
					sItemDb.updateSubmitItem(item,true);
				}
			}
		}
		
	}	
	
	/**
	 * 跳转到预览页面
	 * @param submitId 当前提交表单的 表单ID
	 */
	private void intentToDetail(int submitId){
//		Intent intent = new Intent(this, FuncDetailActivity.class);
//		intent.putExtra("submitId", submitId);
//		intent.putExtra("targetId", targetId);//数据ID
//		intent.putExtra("modType", modType);//自定义模块类型
//		intent.putExtra("menuType", menuType);//菜单类型
		try {
			controlHidden(submitId);
			setStoreParam(submitId);
			setSubmitTimestamp(submitId);
//			startActivityForResult(intent,R.id.submit_succeeded);
			
			
			submitDataLayout.setEnabled(false);
			if (bundle!=null) {
				doubleMasterTaskNo = bundle.getString(NewDoubleDetailActivity.DOUBLE_MASTER_TASK_NO);
				doubleButtonType = bundle.getString(NewDoubleDetailActivity.DOUBLE_BTN_TYPE);
			}
			submit = submitDB.findSubmitById(submitId);// 首先查询Submit
			currentTargetId = submit.getTargetid();//当前数据ID
			submitDataLayout.setEnabled(false);
			if (linkId != Constants.DEFAULTINT) {//超链接预览
				if (codeSubmitControl() && isCompletion()) {
					controlLink();
				}
			}else{
				if (menuType == Menu.TYPE_VISIT) {// 首先检测完整性控制 访店提交
					if (usableControl() && codeSubmitControl() && isCompletion()) {
						submitVisit();
					}
				}else if (menuType == Menu.TYPE_NEW_TARGET){//新双向
					if (usableControl() && codeSubmitControl() && submitForBaseTime() && isCompletion()) {
						if (modType == Constants.MODULE_TYPE_EXECUTE_NEW && infoType == 0) {
							submitNewDouble();
						}
					}
				}else if(menuType == Menu.TYPE_NEARBY && isCompletion()){//就近拜访
					submitNearbyVisit();
				} else if (menuType == Menu.TYPE_MODULE) {// 自定义情况下只改变提交状态和存入时间戳
					if (usableControl() && codeSubmitControl() && submitForBaseTime() && isCompletion()) {
						//自定义 执行 上报  店面信息修改
						if ((modType == Constants.MODULE_TYPE_EXECUTE || modType == Constants.MODULE_TYPE_REPORT  || modType == Constants.DEFAULTINT) && infoType == 0) {
							submitModuleOrDouble();
						}else {
							submitReplenish();//补报提交
						}
					}
				}
			}
			
		} catch (NumberFormatException e) {
			ToastOrder.makeText(this,R.string.toast_one1, ToastOrder.LENGTH_SHORT).show();
		} catch (DataComputeException e) {
			ToastOrder.makeText(this,e.getMessage(), ToastOrder.LENGTH_SHORT).show();
			JLog.e(e);
		}
	}
	
	/**
	 * 上报的时候把店面ID传给服务端
	 * @param submitId
	 */
	private void setStoreParam(int submitId){
		String status = SharedPreferencesUtilForNearby.getInstance(this).getNearbyDataStatus(String.valueOf(targetId));
		Func storeFunc = new FuncDB(this).findNearbyStoreFunc(targetId,SharedPreferencesUtilForNearby.getInstance(this).getNearbyDataStatus(String.valueOf(targetId)));
		if (storeFunc!=null && "0".equals(status)) {
			SubmitItemDB submitItemDB = new SubmitItemDB(this);
			SubmitItem item = new SubmitItem();
			item.setParamName(String.valueOf(storeFunc.getFuncId()));
			item.setParamValue(String.valueOf(storeId));
			item.setSubmitId(submitId);
			item.setType(storeFunc.getType());
			item.setOrderId(storeFunc.getId());
			item.setIsCacheFun(storeFunc.getIsCacheFun());
			item.setTargetId(storeFunc.getTargetid()+"");
			if (submitItemDB.findIsHaveParamName(String.valueOf(storeFunc.getFuncId()), submitId)) {
				submitItemDB.updateSubmitItem(item,true);
			}else{
				submitItemDB.insertSubmitItem(item,true);
			}
		}
	}

	/**
	 * 如果是status>0的时候服务端是更新数据，所以此时要把服务端传过来的patchid作为此次提交表单的patchid
	 * @param submitId
	 */
	private void setSubmitTimestamp(int submitId){
		String status = SharedPreferencesUtilForNearby.getInstance(this).getNearbyDataStatus(String.valueOf(targetId));
		if (!TextUtils.isEmpty(patchId) && !"0".equals(status)) {
			SubmitDB submitDB = new SubmitDB(this);
			Submit submit = submitDB.findSubmitById(submitId);
			if (submit!=null) {
				submit.setTimestamp(patchId);
				submitDB.updateSubmit(submit);
			}
		}
	}
	
	/**
	 * 点击回退按钮
	 */
	@Override
	protected void onClickBackBtn() {
		//清楚未提交的数据 关闭当前页面
		cleanCurrentNoSubmit(targetId);
		this.finish();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("storeName", storeName);
		outState.putInt("storeId", storeId);
		outState.putString("patchId", patchId);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		storeName = savedInstanceState.getString("storeName");
		patchId = savedInstanceState.getString("patchId");
		storeId = savedInstanceState.getInt("storeId");
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
				ToastOrder.makeText(this, getResources().getString(R.string.nearby_visit_01), ToastOrder.LENGTH_SHORT).show();
				submitDataLayout.setEnabled(true);
			}
		}
		return flag;
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
					showTip(getResources().getString(R.string.nearby_visit_03));
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
	 * 手机可用时间段控制,新考勤模块
	 * @param menu
	 * @return
	 */
	public  boolean usableControl(Date date){
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
		flag = control.isCanUse(time,date);
		if (menuType == Menu.TYPE_ORDER3) {
			if (flag) {//时间过了判断天
				flag = control.isOrder3CanUse(this);
				if (!flag) {
					showTip(getResources().getString(R.string.nearby_visit_03));
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
	 * 完整性控制
	 */
	public boolean isCompletion() {
		boolean isCompletion = true; // 默认操作完整
		Submit isCompletionSubmit = submitDB.findSubmitById(submitId); // 首先查询要提交的Submit
		if (isCompletionSubmit!=null) {
			int currentSubmitId = isCompletionSubmit.getId();
			int currentTargetId = isCompletionSubmit.getTargetid();
			// 根据Submitid找到该ID对应的应经保存的一组数据
			List<Func> isEmptyList=new ArrayList<Func>(); // 查找所有必须操作的List集合
			if (menuType == Menu.TYPE_VISIT) { // 访店
				isEmptyList = visitFuncDB.findFuncListByIsEmpty(1, currentTargetId);// 查找所有不能为空的选项
			} else if(menuType == Menu.TYPE_NEARBY){
				isEmptyList = funcDB.findNearbyFuncListIsEmpty(1, currentTargetId);// 查找所有不能为空的选项
			} else { //自定义模块
				switch (modType) {
				case Constants.MODULE_TYPE_EXECUTE:
					isEmptyList = funcDB.findFuncListByIsEmpty(1, currentTargetId,"1"); // 查找双向所有不能为空的选项
					break;
				case Constants.MODULE_TYPE_QUERY://查询
				case Constants.MODULE_TYPE_VERIFY://审核
				case Constants.MODULE_TYPE_REASSIGN://改派
					isEmptyList = funcDB.findFuncListByIsEmpty(1, currentTargetId,ableStatus);
					break;
				case Constants.MODULE_TYPE_ISSUED://下发
				case Constants.MODULE_TYPE_REPORT://上报
					isEmptyList = funcDB.findFuncListByIsEmpty(1,currentTargetId, "0"); // 上报和下发的情况
					break;
				default:
					isEmptyList = funcDB.findFuncListByIsEmpty(1, currentTargetId,ableStatus);
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
								ToastOrder.makeText(NearbyVisitFuncActivity.this,e.getMessage(),ToastOrder.LENGTH_SHORT).show();
								JLog.e(e);
								break;
							}
							if (isEmptyByEnterMustList) { // 如果不满足，为不必填
								isCompletion = false;
								ToastOrder.makeText(NearbyVisitFuncActivity.this,PublicUtils.getResourceString(NearbyVisitFuncActivity.this,R.string.toast_one2)+"\n\"" + isEmptyFunc.getName() + "\"",ToastOrder.LENGTH_SHORT).show();
								break;
							} else {
								continue;
							}
						} else { // 此处无值为必填栏位
							isCompletion = false;
							ToastOrder.makeText(NearbyVisitFuncActivity.this,PublicUtils.getResourceString(NearbyVisitFuncActivity.this,R.string.toast_one2)+"\"" + isEmptyFunc.getName() + "\"",ToastOrder.LENGTH_SHORT).show();
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
	 * 补报数据提交
	 */
	private void submitReplenish() {
		submit.setState(Submit.STATE_SUBMITED);
		if ((TextUtils.isEmpty(submit.getTimestamp()))) {
			submit.setTimestamp(String.valueOf(System.currentTimeMillis()));
		}
		submitDB.updateSubmit(submit);
		Constants.ISCHECKOUTMODUL = true;
		startSubmitService();
	}

	/**
	 * 双向或者自定义提交
	 */
	private void submitNewDouble() {
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
		startSubmitService();
	}
	
	/**
	 * 双向或者自定义提交
	 */
	private void submitModuleOrDouble() {
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
		startSubmitService();
	}
	
	/**
	 * 就近拜访
	 */
	private void submitNearbyVisit(){
		submit.setState(Submit.STATE_SUBMITED);
		if ((TextUtils.isEmpty(submit.getTimestamp()))) {
			submit.setTimestamp(String.valueOf(System.currentTimeMillis()));
		}
		submitDB.updateSubmit(submit);
		Constants.ISCHECKOUTMODUL = true;
		startSubmitService();
	}

	/**
	 * 开启提交服务
	 */
	private void startSubmitService() {
		List<SubmitItem> submitItemList = submitItemDB.findSubmitItemBySubmitId(submitId);// 根据Submitid找到该ID对应的一组数据
		for (int i = 0; i < submitItemList.size(); i++) {
			funcControlDB.updateFuncControlSubmitState(submitItemList.get(i).getParamName(), targetId);
		}

		funcControlDB.removeFuncControlByState(0);// 删除没提交的
		updateLinkSubmitState(submitId);//修改超链接提交表单数据状态
		submit();
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
	
	private int currentTargetId;

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
	 * 自定义上报判断基于时间的提交
	 * @return true允许提交 false 不允许提交
	 */
	private boolean submitForBaseTime(){
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
					ToastOrder.makeText(this, getResources().getString(R.string.nearby_visit_02), ToastOrder.LENGTH_LONG).show();
				}
			}else if(moduleType==Menu.MENU_MODULE_TYPE_3){// 把提交时间改成明天
				if(currentTime>baseTime){
					Func hiddenFunc=new FuncDB(this).findFuncListByTargetIdAndDefaultType(targetId, Func.DEFAULT_TYPE_BASE_TIEM);//基于时间的隐藏域
					if(hiddenFunc!=null){
						SubmitItemDB submitItemDB=new SubmitItemDB(this);
						SubmitItem item =submitItemDB.findSubmitItemBySubmitIdAndFuncId(submitId, hiddenFunc.getFuncId());
						if(item!=null){
							item.setParamValue(DateUtil.getTomorrowDate());
							submitItemDB.updateSubmitItem(item,true);
						}
					}
				}
			}
		}
		return flag;
	}
	
	/**
	 * 超链接点击保存
	 */
	private void controlLink(){
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
			item.setSubmitId(linkId);
			item.setParamName(linkKey + "");
			if ((TextUtils.isEmpty(submit.getTimestamp()))) {
				submit.setTimestamp(String.valueOf(System.currentTimeMillis()));
			}
			item.setParamValue(submit.getTimestamp());
			
			item.setIsCacheFun(currentFunc.getIsCacheFun());
			item.setTargetId(currentFunc.getTargetid()+"");
			item.setType(Func.TYPE_LINK);
			boolean isHas = submitItemDB.findIsHaveParamName(
					item.getParamName(), linkId);
			if(isHas){
				submitItemDB.updateSubmitItem(item,true);
			}else{
				submitItemDB.insertSubmitItem(item,true);
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
				tempItem.setTargetId(linkFunc.getTargetid()+"");
//				boolean flag = submitItemDB.findIsHaveParamName(tempItem.getParamName(), submitId);
//				if(flag){
//					submitItemDB.updateSubmitItem(tempItem);
//				}else{
//					submitItemDB.insertSubmitItem(tempItem);
//				}
				submitItemDB.insertSubmitItem(tempItem,true);
				new SubmitItemTempDB(this).deleteSubmitItem(tempItem);
			}
			
//			thisFinish(R.id.submit_succeeded,null);
			onActivityResultAbs(R.id.submit_succeeded, R.id.submit_succeeded, null);
			JLog.d(TAG, "超链接添加Timestamp ===>" + submit.getTimestamp());
		}
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
			Toast.makeText(NearbyVisitFuncActivity.this, R.string.submit_ok, Toast.LENGTH_SHORT).show();;
		}else{
			if (submit != null) {
				submit.setState(Submit.STATE_NO_SUBMIT);
				submitDB.updateSubmit(submit);
				Toast.makeText(NearbyVisitFuncActivity.this, R.string.toast_one7, Toast.LENGTH_SHORT).show();;
			}
		}
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
		startSubmitService();//开始提交
	}

}
