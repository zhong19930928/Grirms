package com.yunhu.yhshxc.module.replenish;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.attendance.AttendanceListActivity;
import com.yunhu.yhshxc.attendance.AttendanceNewListActivity;
import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.bo.Module;
import com.yunhu.yhshxc.bo.Org;
import com.yunhu.yhshxc.bo.OrgStore;
import com.yunhu.yhshxc.bo.OrgUser;
import com.yunhu.yhshxc.bo.Query;
import com.yunhu.yhshxc.comp.menu.PreviewDataComp;
import com.yunhu.yhshxc.database.DictDB;
import com.yunhu.yhshxc.database.FuncDB;
import com.yunhu.yhshxc.database.OrgDB;
import com.yunhu.yhshxc.database.OrgStoreDB;
import com.yunhu.yhshxc.database.OrgUserDB;
import com.yunhu.yhshxc.database.QueryDB;
import com.yunhu.yhshxc.database.RoleDB;
import com.yunhu.yhshxc.doubletask2.ChildrenListActivity;
import com.yunhu.yhshxc.list.activity.TableListActivity;
import com.yunhu.yhshxc.list.activity.TableListActivityNew;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gcg.org.debug.ELog;
import gcg.org.debug.JLog;

/**
 * 查询条件的预览
 * @author gcg_jishen
 *
 */
public class PreviewActivity extends AbsBaseActivity {
	private String TAG = "PreviewActivity";
	private LinearLayout showDataLayout;
	private LinearLayout submitDataLayout;
	private FuncDB funcDB;
	private DictDB dicDB;
	private Bundle replenish;
	private String replenishValue;
	private TextView noReplenish;
	private ScrollView repleish_sv;
	private QueryDB queryDB;
	private int modType,menuType,targetId;
	private Module module;
	private Bundle bundle=null;
	private int menuId;
	private String menuName;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.replenish_detail);
		initBase();
		modType=getIntent().getIntExtra("modType", 0);
		menuType=getIntent().getIntExtra("menuType", 0);
		targetId=getIntent().getIntExtra("targetId", 0);
		
		bundle = getIntent().getBundleExtra("bundle");
		if(bundle!=null){
			menuId = bundle.getInt("menuId");
			menuName = bundle.getString("menuName");
		}
		init();
		addData();
		if (showDataLayout.getChildCount() == 0) {
			noReplenish.setVisibility(View.VISIBLE);
			repleish_sv.setVisibility(View.GONE);
		}
	}

	/*
	 * 初始化数据
	 */
	private void init() {
		queryDB = new QueryDB(this);
		replenish=getIntent().getBundleExtra("replenish");
		module=(Module) getIntent().getSerializableExtra("module");
		funcDB = new FuncDB(this);
		dicDB = new DictDB(this);
		showDataLayout = (LinearLayout) findViewById(R.id.ll_func_detail_add_data);
		submitDataLayout = (LinearLayout) findViewById(R.id.ll_func_show_detail_data);
		noReplenish=(TextView)findViewById(R.id.tv_noReplenish);
		repleish_sv=(ScrollView)findViewById(R.id.repleish_sv);
		submitDataLayout.setOnClickListener(this);
	}

	private void insertQuery(){
		queryDB.removeQueryByMid(targetId);
		if (replenish!=null && replenish.size()>0) {
			Query query = null;
			for (String keyStr:replenish.keySet()) {
				query = new Query();
				query.setFuncId(keyStr);
				query.setValue(replenish.getString(keyStr));
				query.setMid(targetId);
				queryDB.insertQuery(query);
				JLog.d(TAG, "key:"+keyStr);
				JLog.d(TAG, "value:"+query.getValue());

			}
		}
	}
	
	private void addData() {
		PreviewDataComp previewDataComp;
		StringBuffer buffer=new StringBuffer();
		List<Func> funcList=funcDB.findFuncListReplenish(getIntent().getIntExtra("taskid", 0),null,Func.IS_Y,null);
			for (int i = 0; i < funcList.size(); i++) { // 循环添加操作项
				Func func=funcList.get(i);
				String key=func.getFuncId()+"";
				String value=null;
				if(replenish.containsKey(key)){
					
					if((func.getType()==Func.TYPE_SELECTCOMP 
							|| func.getType()==Func.TYPE_MULTI_CHOICE_SPINNER_COMP 
							|| func.getType()==Func.TYPE_MULTI_CHOICE_FUZZY_QUERY_COMP
							|| func.getType()==Func.TYPE_SELECT_OTHER 
							|| func.getType()==Func.TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY_COMP 
							|| func.getType()==Func.TYPE_SINGLE_CHOICE_FUZZY_QUERY_COMP) && func.getTargetid()!=-2){//func.getTargetid()!=-2是考勤的查询条件，不进行转型
						func.setType(changeTypeForIsSearchMulAndIsFuzzy(func));//单选作为查询条件转为多选的时候使用
					}
					
					previewDataComp = new PreviewDataComp(this);
					value=replenish.getString(key);
					previewDataComp.setShowDataTitle(func.getName());
					previewDataComp.setShowDataContent(value.replace("~@@", "~"));
					
					buffer.append("$@@").append(key).append(",").append(value);
					
					if(func.getType()==Func.TYPE_SELECTCOMP 
							|| func.getType()==Func.TYPE_SINGLE_CHOICE_FUZZY_QUERY_COMP 
							|| func.getType()==Func.TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY_COMP
							|| func.getType()==Func.TYPE_SELECT_OTHER){
						if (func.getOrgOption()!=null && func.getOrgOption()!=Func.OPTION_LOCATION) {
							setOrgOption(func, showDataLayout,previewDataComp, value);
						} else {
							
							if((func.getType()==Func.TYPE_SELECT_OTHER || func.getType()==Func.TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY_COMP) && !isOther(value)){//选择其他值的时候 即不是数字的时候
								previewDataComp.setShowDataContent(replenish.getString(key+"_other"));
							}else{
								String tableName = func.getDictTable(); // 查询的表
								String dataId = func.getDictDataId(); // 查询表中的列
								Dictionary dic = dicDB.findDictListByTable(tableName, dataId,value);
								if(dic==null){
									previewDataComp.setShowDataContent(value);
								}else{
									previewDataComp.setShowDataContent(dic.getCtrlCol());
								}
							}
							
							showDataLayout.addView(previewDataComp.getView());
						}
						
					}else if((func.getType()==Func.TYPE_MULTI_CHOICE_SPINNER_COMP || func.getType()==Func.TYPE_MULTI_CHOICE_FUZZY_QUERY_COMP) && !TextUtils.isEmpty(value)){
						
							if (func.getOrgOption()!=null && func.getOrgOption()!=Func.OPTION_LOCATION) {
								setOrgOption(func, showDataLayout,previewDataComp, value);
							} else {
								if(func.getFuncId()==-4){//角色
									String rs = new RoleDB(this).findDictMultiChoiceValueStr(value,null);
									previewDataComp.setShowDataContent(rs);
									previewDataComp.setShowDataTitle(func.getName());
								}else if(func.getFuncId()==-5){//用户
									String rs = new OrgUserDB(this).findDictMultiChoiceValueStr(value,null);
									previewDataComp.setShowDataContent(rs);
									previewDataComp.setShowDataTitle(func.getName());
								}else{
									String rs = dicDB.findDictMultiChoiceValueStr(value, func.getDictDataId(),func.getDictTable());
									previewDataComp.setShowDataContent(rs);
								}
							showDataLayout.addView(previewDataComp.getView());
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
										previewDataComp.setShowDataContent(value);
									}else{
										previewDataComp.setShowDataContent(dic.getCtrlCol());
									}
									showDataLayout.addView(previewDataComp.getView());
									break;
								case Func.DEFAULT_TYPE_MULTI_SELECT:
								case Func.TYPE_MULTI_CHOICE_FUZZY_QUERY_COMP:
									String rs = dicDB.findDictMultiChoiceValueStr(value, func.getDictDataId(),func.getDictTable());
									previewDataComp.setShowDataContent(rs);
									showDataLayout.addView(previewDataComp.getView());
									break;
								default:
									showDataLayout.addView(previewDataComp.getView());
									break;
								}
						}else{
							showDataLayout.addView(previewDataComp.getView());
						}
						
					}else if(func.getType() == Func.TYPE_SQL_BIG_DATA){
						previewDataComp.setShowDataContent(replenish.getString(key+"_value"));
						showDataLayout.addView(previewDataComp.getView());
					}else{
						showDataLayout.addView(previewDataComp.getView());
					}
				}
				
			}
			if(modType==Constants.MODULE_TYPE_REASSIGN){//下发和改派的情况添加预览显示要下发和显示的用户
				if(!TextUtils.isEmpty(replenish.getString("-8968"))){
					previewDataComp = new PreviewDataComp(this);
					String rs = new OrgUserDB(this).findDictMultiChoiceValueStr(replenish.getString("-8968"),null);
					previewDataComp.setShowDataContent(rs);
					previewDataComp.setShowDataTitle(getResources().getString(R.string.replenish_activity_user));
					showDataLayout.addView(previewDataComp.getView());
					buffer.append("$@@").append("-8968").append(",").append(replenish.getString("-8968"));
				}
			}
			if(buffer.length()>0){
				replenishValue = buffer.substring(3).toString();
			}
	}

	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ll_func_show_detail_data: // 提交数据
			if(menuType==Menu.TYPE_ATTENDANCE){//考勤
				intentAttend();
			}else if(menuType==Menu.TYPE_NEW_ATTENDANCE){//新考勤
				intentNewAttend();
			}else if(menuType==Menu.TYPE_NEW_TARGET){
				intentChildrenListActivity();
			}else {
				intentReplenishListActivity();
			}
			break;
		default:
			break;
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

	/**
	 * 设置机构店面
	 * 
	 * @param func
	 * @param showDataLayout
	 * @param previewDataComp
	 * @param currentValue
	 */
	private void setOrgOption(Func func, LinearLayout showDataLayout,
			PreviewDataComp previewDataComp, String currentValue) {
		if (func.getOrgOption() != null&& func.getOrgOption() == Func.ORG_STORE) {
			previewDataComp.setShowDataContent(getOrgStore(currentValue));
			showDataLayout.addView(previewDataComp.getView());
		} else if (func.getOrgOption() != null && func.getOrgOption() == Func.ORG_USER) {
			previewDataComp.setShowDataContent(getOrgUser(currentValue));
			showDataLayout.addView(previewDataComp.getView());
		} else if (func.getOrgOption() != null && func.getOrgOption() == Func.ORG_OPTION) {
			previewDataComp.setShowDataContent(getOrg(currentValue));
			showDataLayout.addView(previewDataComp.getView());
		}
	}

	
	private void intentReplenishListActivity(){
		ELog.d("Open TableListActivity");
//		SharedPreferencesUtil.getInstance(this).setReplenish(replenishValue==null ? "":replenishValue);
		insertQuery();
		Intent intent=new Intent(this,TableListActivityNew.class);
		Bundle bundle = new Bundle();
		bundle.putInt("targetId", targetId);
		bundle.putInt("modType", modType);
		bundle.putInt("menuType", menuType);
		bundle.putSerializable("module", module);
		bundle.putInt("menuId", menuId);
		bundle.putString("menuName", menuName);
		intent.putExtra("bundle", bundle);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		Constants.hasReplenish=true;
		this.finish();
	}
	
	private void intentChildrenListActivity(){
		insertQuery();
		Intent intent=new Intent(this,ChildrenListActivity.class);
		intent.putExtra("bundle", bundle);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		Constants.hasReplenish=true;
		this.finish();
	}
	
	
	private void intentAttend(){
		SharedPreferencesUtil.getInstance(this).setReplenish(replenishValue==null ? "":replenishValue);
		Intent intent=new Intent(this,AttendanceListActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("targetId", targetId);
		bundle.putInt("modType", modType);
		bundle.putInt("menuType", menuType);
		intent.putExtra("bundle", bundle);
		startActivity(intent);
//		Constants.hasReplenish=true;
		this.finish();
	}

	private void intentNewAttend(){
		SharedPreferencesUtil.getInstance(this).setReplenish(replenishValue==null ? "":replenishValue);
		Intent intent=new Intent(this,AttendanceNewListActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("targetId", targetId);
		bundle.putInt("modType", modType);
		bundle.putInt("menuType", menuType);
		intent.putExtra("bundle", bundle);
		startActivity(intent);
//		Constants.hasReplenish=true;
		this.finish();
	}

	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	/*
	 * 验证其他下拉框
	 */
	public boolean isOther(String value) {
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
	}

	/** 
	 * 隐藏域下拉框查询条件的时候要根据IsSearchMul和IsFuzzy的值进行判断该下拉框的类型
	 * @param func
	 * @return
	 */
	private Integer changeTypeForIsSearchMulAndIsFuzzy(Func func){
		//将类型改变为多选查询条件 如果是模糊搜索的话设置为模糊多选搜索
		if(func.getIsSearchMul()!=null && func.getIsSearchMul()==Func.IS_Y){
			if(func.getIsFuzzy()!=null && func.getIsFuzzy()==Func.IS_Y){
				func.setType(Func.TYPE_MULTI_CHOICE_FUZZY_QUERY_COMP);
			}else{
				func.setType(Func.TYPE_MULTI_CHOICE_SPINNER_COMP);
			}
		}else{
			if(func.getIsFuzzy()!=null && func.getIsFuzzy()==Func.IS_Y){
				func.setType(Func.TYPE_SINGLE_CHOICE_FUZZY_QUERY_COMP);
			}else{
				func.setType(Func.TYPE_SELECTCOMP);
			}
		}
		return func.getType();
	}
}
