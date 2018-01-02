package com.yunhu.yhshxc.module.replenish;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.attendance.AttendanceNewListActivity;
import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.bo.Module;
import com.yunhu.yhshxc.bo.Org;
import com.yunhu.yhshxc.bo.OrgStore;
import com.yunhu.yhshxc.bo.OrgUser;
import com.yunhu.yhshxc.bo.Query;
import com.yunhu.yhshxc.bo.SubmitItem;
import com.yunhu.yhshxc.database.DictDB;
import com.yunhu.yhshxc.database.FuncDB;
import com.yunhu.yhshxc.database.OrgDB;
import com.yunhu.yhshxc.database.OrgStoreDB;
import com.yunhu.yhshxc.database.OrgUserDB;
import com.yunhu.yhshxc.database.QueryDB;
import com.yunhu.yhshxc.database.RoleDB;
import com.yunhu.yhshxc.database.SubmitDB;
import com.yunhu.yhshxc.database.SubmitItemDB;
import com.yunhu.yhshxc.func.AbsFuncActivity;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 考勤查询模块
 * 继承自AbsFuncActivity类，大部分界面工作由父类完成，通过实现getFuncList()查询出
 * 父类所需的与考勤查询相关模块。
 * 
 * @version 2013.5.22
 * @author wangchao
 * 
 */
public class QueryFuncActivity extends AbsFuncActivity {
	/**
	 * 存储查询条件的值
	 */
	public Bundle replenish;
	private Module module;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		init();
		super.onCreate(savedInstanceState);
		dicDB = new DictDB(this);
		LinearLayout clear=(LinearLayout)findViewById(R.id.ll_visit_checkIn);
		TextView clearTv = (TextView) findViewById(R.id.tv_query_clear);
		ImageView title_iv = (ImageView) findViewById(R.id.title_iv);
		clear.setVisibility(View.VISIBLE);
		title_iv.setVisibility(View.GONE);
//		clearTv.setVisibility(View.VISIBLE);
		clear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				clear();//清除查询条件
			}
		});
		TextView title = (TextView) findViewById(R.id.tv_titleName);
		title.setText(titleName());
	}

	public String titleName(){
		if (module!=null) {
			return module.getName();
		}else{
			return getResources().getString(R.string.replenish_activity_query);
		}
	}
	/**
	 * 初始化，获取通过Intent传进来的参数
	 */
	private void init() {
		replenish = new Bundle();
		bundle = getIntent().getBundleExtra("bundle");//bundle定义在AbsFuncActivity中
		targetId = bundle.getInt("targetId");
		menuType = bundle.getInt("menuType");
		module = (Module) bundle.getSerializable("module");
		if (menuType == Menu.TYPE_ATTENDANCE || menuType == Menu.TYPE_NEW_ATTENDANCE) {// 考勤类型查询
			bundle.putInt("targetType", menuType);
			module = new Module();
			module.setName(getResources().getString(R.string.replenish_activity_query));
		}
		else {
			bundle.putInt("targetType", Menu.TYPE_MODULE);
		}
		initQueryData();
	}

	private void initQueryData(){
		List<Query> list = new QueryDB(this).findQueryByMid(targetId);
		for (int i = 0; i < list.size(); i++) {
			Query query = list.get(i);
			replenish.putString(query.getFuncId(), query.getValue());
		}
	}
	/**
	 * 获取整个布局中所用到的控件数据
	 * 
	 * @return 获取控件数据的List
	 */
	@Override
	public List<Func> getFuncList() {
		List<Func> funcList = null;
		FuncDB funcDB = new FuncDB(this);
		if (menuType == Menu.TYPE_ATTENDANCE || menuType == Menu.TYPE_NEW_ATTENDANCE) {// 考勤类型
			funcList = funcDB.findFuncListByTargetid(targetId, false);
			bundle.putInt("targetType", menuType);
		}
		else {
			funcList = funcDB.findFuncListReplenish(bundle.getInt("targetId"), null, Func.IS_Y, null);
			bundle.putInt("targetType", Menu.TYPE_MODULE);
		}
		return funcList;
	}
	
	private void clear(){
		clearFuncShowText();
		replenishValue = "";
		if (replenish!=null && replenish.size()>0) {
			new QueryDB(this).removeQueryByMid(targetId);
			replenish.clear();
			setShowHook();			
		}
	}

	/**
	 * 根据FuncId获取Func对象
	 * 
	 * @param funcId 目标Func的ID
	 * @return 返回funcId对应的Func对象
	 */
	@Override
	public Func getFuncByFuncId(int funcId) {
		Func func = new FuncDB(this).findFuncByFuncId(funcId);
		return func;
	}
	
	/**
	 * 按输入顺序获取对象
	 * @param funcId 对应Func.funcId属性
	 * @param inputOrder 对应Func.inputOrder属性
	 * @return 返回Func对象List
	 */
	@Override
	public ArrayList<Func> getOrderFuncList(Integer funcId, int inputOrder) {
		ArrayList<Func> orderList = new FuncDB(this).findFuncListByInputOrder(targetId, funcId, inputOrder);
		return orderList;
	}

	/**
	 * 父类中必须实现的abstract方法
	 */
	@Override
	public List<Func> getButtonFuncList() {
		return null;
	}

	/**
	 * 父类中必须实现的abstract方法
	 */
	@Override
	public Integer[] findFuncListByInputOrder() {
		// Integer[] inputOrderArr =new FuncDB(this).findFuncListByInputOrder(targetId);
		return null;
	}

	/**
	 * 父类中必须实现的abstract方法
	 */
	@Override
	protected void intoLink(Bundle linkBundle) {

	}
	private DictDB dicDB;
	/**
	 * 返回的时候重新查询数据库看是否显示对勾
	 */
	@Override
	public void setShowHook() {
		for (Func func : getFuncList()) {
			View view = null;
			ImageView hookView = null;//对勾组件
			TextView textView = null;
			if (unOperateMap.containsKey(func.getFuncId())) {//如果未处理的控件中包含当前的控件
				view = this.unOperateMap.get(func.getFuncId());
				hookView = (ImageView) view.findViewById(R.id.iv_func_check_mark);
				hookView.setImageBitmap(null);
				textView = (TextView) view.findViewById(R.id.tv_func_content);
			}
			else if (operatedMap.containsKey(func.getFuncId())) {//如果已处理的控件中包含当前控件
				view = this.operatedMap.get(func.getFuncId());
				hookView = (ImageView) view.findViewById(R.id.iv_func_check_mark);
				hookView.setImageBitmap(null);
				textView = (TextView) view.findViewById(R.id.tv_func_content);
			}
			
			if (replenish != null) {//如果查询条件中包含当前的控件
				String value = replenish.getString(String.valueOf(func.getFuncId()));
				if (!TextUtils.isEmpty(value)) {
					if (value.contains("~@@")) {
						value = value.replace("~@@", "-");
					}
					if (func.getOrgOption() != null && func.getOrgOption() == Func.ORG_OPTION) {// 维护机构级联关系（串码平铺属性使用）
						orgMap.put(func.getOrgLevel(), value);
					}
					inputOperatedMap(func);//向已处理的map中添加当前的
					initInputChooseMap(func, value);// 修改的时候外部级联Map条件 维护
//					setShowHook(true, hookView);//显示对勾
					if (textView!=null) {
//							textView.setText(value);
						String key=func.getFuncId()+"";
							if(func.getType()==Func.TYPE_SELECTCOMP 
									|| func.getType()==Func.TYPE_SINGLE_CHOICE_FUZZY_QUERY_COMP 
									|| func.getType()==Func.TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY_COMP
									|| func.getType()==Func.TYPE_SELECT_OTHER){
								if (func.getOrgOption()!=null && func.getOrgOption()!=Func.OPTION_LOCATION) {
									setOrgOption(func, textView, value);
								} else {
									
									if((func.getType()==Func.TYPE_SELECT_OTHER || func.getType()==Func.TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY_COMP) && !isOther(value)){//选择其他值的时候 即不是数字的时候
										textView.setText(replenish.getString(key+"_other"));
									}else{
										String tableName = func.getDictTable(); // 查询的表
										String dataId = func.getDictDataId(); // 查询表中的列
										Dictionary dic = dicDB.findDictListByTable(tableName, dataId,value);
										if(dic==null){
											textView.setText(value);
										}else{
											textView.setText(dic.getCtrlCol());
										}
									}
								}
								
							}else if((func.getType()==Func.TYPE_MULTI_CHOICE_SPINNER_COMP || func.getType()==Func.TYPE_MULTI_CHOICE_FUZZY_QUERY_COMP) && !TextUtils.isEmpty(value)){
								
									if (func.getOrgOption()!=null && func.getOrgOption()!=Func.OPTION_LOCATION) {
										setOrgOption(func, textView, value);
									} else {
										if(func.getFuncId()==-4){//角色
											String rs = new RoleDB(this).findDictMultiChoiceValueStr(value,null);
											textView.setText(rs);
										}else if(func.getFuncId()==-5){//用户
											String rs = new OrgUserDB(this).findDictMultiChoiceValueStr(value,null);
											textView.setText(rs);
										}else{
											String rs = dicDB.findDictMultiChoiceValueStr(value, func.getDictDataId(),func.getDictTable());
											textView.setText(rs);
										}
								}
							}else if(func.getType()==Func.TYPE_HIDDEN){
									Integer defaultType=func.getDefaultType();
									if(defaultType!=null){
										switch (defaultType) {
										case Func.DEFAULT_TYPE_SELECT:
										case Func.TYPE_SINGLE_CHOICE_FUZZY_QUERY_COMP:
											String tableName = func.getDictTable(); // 查询的表
											String dataId = func.getDictDataId(); // 查询表中的列
											Dictionary dic = dicDB.findDictListByTable(tableName, dataId,value);
											if(dic==null){
												textView.setText(value);
											}else{
												textView.setText(dic.getCtrlCol());
											}
											break;
										case Func.DEFAULT_TYPE_MULTI_SELECT:
										case Func.TYPE_MULTI_CHOICE_FUZZY_QUERY_COMP:
											String rs = dicDB.findDictMultiChoiceValueStr(value, func.getDictDataId(),func.getDictTable());
											textView.setText(rs);
											break;
										default:
											textView.setText(value);
											break;
										}
								}else{
									textView.setText(value);
								}
								
							}else if(func.getType() == Func.TYPE_SQL_BIG_DATA){
								textView.setText(replenish.getString(key+"_value"));
							}else{
								textView.setText(value);
							}
					}
					

				}else {
					inputUnOperatedMap(func);//向未处理的map中添加当前的
//					setShowHook(false, hookView);//隐藏对勾
					if (textView!=null) {
//							textView.setText(value);
						String key=func.getFuncId()+"";
						if(func.getType()==Func.TYPE_SELECTCOMP 
								|| func.getType()==Func.TYPE_SINGLE_CHOICE_FUZZY_QUERY_COMP 
								|| func.getType()==Func.TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY_COMP
								|| func.getType()==Func.TYPE_SELECT_OTHER){
							if (func.getOrgOption()!=null && func.getOrgOption()!=Func.OPTION_LOCATION) {
								setOrgOption(func, textView, value);
							} else {
								
								if((func.getType()==Func.TYPE_SELECT_OTHER || func.getType()==Func.TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY_COMP) && !isOther(value)){//选择其他值的时候 即不是数字的时候
									textView.setText(replenish.getString(key+"_other"));
								}else{
									String tableName = func.getDictTable(); // 查询的表
									String dataId = func.getDictDataId(); // 查询表中的列
									Dictionary dic = dicDB.findDictListByTable(tableName, dataId,value);
									if(dic==null){
										textView.setText(value);
									}else{
										textView.setText(dic.getCtrlCol());
									}
								}
							}
							
						}else if((func.getType()==Func.TYPE_MULTI_CHOICE_SPINNER_COMP || func.getType()==Func.TYPE_MULTI_CHOICE_FUZZY_QUERY_COMP) && !TextUtils.isEmpty(value)){
							
								if (func.getOrgOption()!=null && func.getOrgOption()!=Func.OPTION_LOCATION) {
									setOrgOption(func, textView, value);
								} else {
									if(func.getFuncId()==-4){//角色
										String rs = new RoleDB(this).findDictMultiChoiceValueStr(value,null);
										textView.setText(rs);
									}else if(func.getFuncId()==-5){//用户
										String rs = new OrgUserDB(this).findDictMultiChoiceValueStr(value,null);
										textView.setText(rs);
									}else{
										String rs = dicDB.findDictMultiChoiceValueStr(value, func.getDictDataId(),func.getDictTable());
										textView.setText(rs);
									}
							}
						}else if(func.getType()==Func.TYPE_HIDDEN){
								Integer defaultType=func.getDefaultType();
								if(defaultType!=null){
									switch (defaultType) {
									case Func.DEFAULT_TYPE_SELECT:
									case Func.TYPE_SINGLE_CHOICE_FUZZY_QUERY_COMP:
										String tableName = func.getDictTable(); // 查询的表
										String dataId = func.getDictDataId(); // 查询表中的列
										Dictionary dic = dicDB.findDictListByTable(tableName, dataId,value);
										if(dic==null){
											textView.setText(value);
										}else{
											textView.setText(dic.getCtrlCol());
										}
										break;
									case Func.DEFAULT_TYPE_MULTI_SELECT:
									case Func.TYPE_MULTI_CHOICE_FUZZY_QUERY_COMP:
										String rs = dicDB.findDictMultiChoiceValueStr(value, func.getDictDataId(),func.getDictTable());
										textView.setText(rs);
										break;
									default:
										textView.setText(value);
										break;
									}
							}else{
								textView.setText(value);
							}
							
						}else if(func.getType() == Func.TYPE_SQL_BIG_DATA){
							textView.setText(replenish.getString(key+"_value"));
						}else{
							textView.setText(value);
						}
					}
				}
			}

		}
	}
	/**
	 * 清除查询页面的控件展示内容
	 */
   private void clearFuncShowText(){
	   
	   for (int i = 0; i < funcLayout.getChildCount(); i++) {
		   View view = funcLayout.getChildAt(i);
		   TextView tv = (TextView) view.findViewById(R.id.tv_func_content);
		   EditText et = (EditText)view.findViewById(R.id.textEditTextComp);
           if (tv!=null) {
			tv.setText("");
		}
           if (et!=null) {
			et.setText("");
			et.setHint(getResources().getString(R.string.replenish_activity_01));

		}
	}
	   
   }
   
   
   

	/**
	 * 打开显示采集信息的PreviewActivity
	 */
	@Override
	public void showPreview() {
		
		if (menuType==Menu.TYPE_NEW_ATTENDANCE) {
		  try{
				addData();
				SharedPreferencesUtil.getInstance(this).setReplenish(replenishValue==null ? "":replenishValue);
				Intent intent1=new Intent(this,AttendanceNewListActivity.class);
				Bundle bundle1 = new Bundle();
				bundle1.putInt("targetId", targetId);
				bundle1.putInt("modType", modType);
				bundle1.putInt("menuType", menuType);
				intent1.putExtra("bundle", bundle1);
				startActivity(intent1);  
			  
		  }catch(Exception e){
			  e.printStackTrace();
			  intentToPreviewActivity();
		  }
		}else{
			intentToPreviewActivity();
		}
		
		
	}
    private void intentToPreviewActivity(){
    	Intent intent = new Intent(this, PreviewActivity.class);
		if (module != null) {
			intent.putExtra("modType", module.getType());
			intent.putExtra("module", bundle.getSerializable("module"));
		}
		intent.putExtra("taskid", bundle.getInt("targetId"));
		intent.putExtra("menuType", menuType);
		intent.putExtra("targetId", targetId);
		intent.putExtra("replenish", replenish);
		bundle.putInt("menuId", menuId);
		bundle.putString("nemuName", menuName);
		intent.putExtra("bundle", bundle);
		startActivity(intent);
    } 
    private String replenishValue;
	private void addData() {
		FuncDB funcDB = new FuncDB(this);
//		PreviewDataComp previewDataComp;
		StringBuffer buffer=new StringBuffer();
		List<Func> funcList=funcDB.findFuncListReplenish(bundle.getInt("targetId"),null,Func.IS_Y,null);
			for (int i = 0; i < funcList.size(); i++) { // 循环添加操作项
				Func func=funcList.get(i);
				String key=func.getFuncId()+"";
				String value=null;
				if(replenish.containsKey(key)){

					value=replenish.getString(key);				
					buffer.append("$@@").append(key).append(",").append(value);				
				}
               try{
    	           if (func.getType()==Func.TYPE_DATEPICKERCOMP) {
    		   			
   					int subId= new SubmitDB(QueryFuncActivity.this).findSubmitByTargetId(targetId, 0).getId();
   					if (subId!=0) {			
   						SubmitItem sItem = new SubmitItemDB(QueryFuncActivity.this).findSubmitItemBySubmitItemId(subId);
   						buffer.append("$@@").append(sItem.getParamName()).append(",").append(sItem.getParamValue());	
   					}
   			}
               }catch(Exception e){
            	   e.printStackTrace();
               }

			if(buffer.length()>0){
				replenishValue = buffer.substring(3).toString();
			}
	}
	}
	/**
	 * 点击返回对话框的确定按钮时，清除查询条件
	 */
    
	@Override
	protected void onClickBackBtn() {
		replenish.clear();
		//清除查询条件
		try{
			QueryDB qb = new QueryDB(this);
			qb.removeQueryByMid(targetId);
		}catch(Exception e){
			e.printStackTrace();
		}

		this.finish();
	}
	/**
	 * 设置机构店面
	 * 
	 * @param func
	 * @param currentValue
	 */
	private void setOrgOption(Func func, TextView tv,
			 String currentValue) {
		if (func.getOrgOption() != null&& func.getOrgOption() == Func.ORG_STORE) {
			tv.setText(getOrgStore(currentValue));
		} else if (func.getOrgOption() != null && func.getOrgOption() == Func.ORG_USER) {
			tv.setText(getOrgUser(currentValue));
		} else if (func.getOrgOption() != null && func.getOrgOption() == Func.ORG_OPTION) {
			tv.setText(getOrg(currentValue));
		}
	}
	/**
	 * 返回机构数据
	 * @param value
	 * @return
	 */
	private String getOrg(String value) {
		List<Org> orgList = new OrgDB(this).findOrgByOrgId(value);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < orgList.size(); i++) {
			Org org = orgList.get(i);
			sb.append(",").append(org.getOrgName());
		}
		return sb.length() > 0 ? sb.substring(1).toString() : value;
	}

	/**
	 * 返回店面数据
	 * @param value
	 * @return
	 */
	private String getOrgStore(String value) {
		List<OrgStore> orgStoreList = new OrgStoreDB(this)
				.findOrgListByStoreId(value);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < orgStoreList.size(); i++) {
			OrgStore orgStore = orgStoreList.get(i);
			sb.append(",").append(orgStore.getStoreName());
		}
		return sb.length() > 0 ? sb.substring(1).toString() : value;
	}

	/**
	 * 返回用户数据
	 * @param value
	 * @return
	 */
	private String getOrgUser(String value) {
		List<OrgUser> orgUserList = new OrgUserDB(this)
				.findOrgUserByUserId(value);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < orgUserList.size(); i++) {
			OrgUser orgUser = orgUserList.get(i);
			sb.append(",").append(orgUser.getUserName());
		}
		return sb.length() > 0 ? sb.substring(1).toString() : value;
	}
	/*
	 * 验证其他下拉框
	 */
	public boolean isOther(String value) {
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
	}
}
