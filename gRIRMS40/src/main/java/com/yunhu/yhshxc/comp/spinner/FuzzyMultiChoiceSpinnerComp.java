package com.yunhu.yhshxc.comp.spinner;


import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.Org;
import com.yunhu.yhshxc.bo.OrgStore;
import com.yunhu.yhshxc.bo.OrgUser;
import com.yunhu.yhshxc.bo.Submit;
import com.yunhu.yhshxc.bo.SubmitItem;
import com.yunhu.yhshxc.comp.CompDialog;
import com.yunhu.yhshxc.database.DictDB;
import com.yunhu.yhshxc.database.OrgDB;
import com.yunhu.yhshxc.database.OrgStoreDB;
import com.yunhu.yhshxc.database.OrgUserDB;
import com.yunhu.yhshxc.database.RoleDB;
import com.yunhu.yhshxc.database.SubmitDB;
import com.yunhu.yhshxc.database.SubmitItemDB;
import com.yunhu.yhshxc.database.SubmitItemTempDB;
import com.yunhu.yhshxc.listener.OnMultiChoiceConfirmListener;
import com.yunhu.yhshxc.report.ReportActivity;
import com.yunhu.yhshxc.widget.MultiChoiceFuzzyQuerySpinner;

import java.util.List;

import gcg.org.debug.JLog;

/**
 * 多选模糊搜索下拉框
 * @author jishen
 */
public class FuzzyMultiChoiceSpinnerComp extends AbsSelectComp implements OnMultiChoiceConfirmListener{
	private String TAG="FuzzyMultiChoiceSpinnerComp";
	public MultiChoiceFuzzyQuerySpinner multiChoiceFuzzyQuerySpinner = null;
	private Bundle replenishBundle = null;//携带查询条件的bundle
	private OnMuktiChiceSelecteLister lister;
	/**
	 * 控件不是在表格中
	 * @param context
	 * @param func
	 * @param queryWhere 层级关系
	 * @param orgBundle 机构关系
	 * @param compDialog
	 */
	public FuzzyMultiChoiceSpinnerComp(Context context, Func func,String queryWhere,Bundle orgBundle,CompDialog compDialog,OnMuktiChiceSelecteLister listerg) {
		super(context, func,queryWhere,orgBundle);
		this.context=context;
		this.lister = lister;
		this.func=func;
		this.compFunc=func;
		this.putData(compDialog.type, compDialog.wayId, compDialog.planId, compDialog.storeId, compDialog.targetid, compDialog.targetType);
		this.setReplenishBundle(compDialog.replenish);
		this.setOrgLevelMap(compDialog.getOrgMap());
		init();
	}
	
	/**
	 * 控件在表格中
	 * @param context
	 * @param func
	 * @param queryWhere 层级关系
	 * @param orgBundle 机构关系
	 */
	public FuzzyMultiChoiceSpinnerComp(Context context, Func func,String queryWhere,Bundle orgBundle) {
		super(context, func,queryWhere,orgBundle);
		this.context=context;
		this.func=func;
		this.compFunc=func;
		init();
	}

	/**
	 * 初始化实例
	 */
	public void init(){
		view = View.inflate(context, R.layout.comp_fuzzy_multi_choice_spinner, null);
		multiChoiceFuzzyQuerySpinner = (MultiChoiceFuzzyQuerySpinner)view.findViewById(R.id.multiChoiceFuzzyQuerySpinner);
		multiChoiceFuzzyQuerySpinner.setOnMultiChoiceConfirmListener(this);
	}
	
	/**
	 * 创建下拉列表
	 */
	public void create(){
		if(dataSrcList!=null && !dataSrcList.isEmpty()){//数据源不为空的情况
			int size = dataSrcList.size();
			String []dataSrc = new String[size];
			boolean []chooseStatus = new boolean[size];
			int i=0;
			if(TextUtils.isEmpty(value)){
				for (Dictionary dict : dataSrcList) {
					chooseStatus[i] = false;
					dataSrc[i] = dict.getCtrlCol()==null?"":dict.getCtrlCol();//获取资源列所有数据
					i++;
				}
			}else{
				String []values = value.split(",");
				for (Dictionary dict : dataSrcList) {
					for(int j = 0; j<values.length; j++){
						if(values[j].equals(dict.getDid())){
							chooseStatus[i] = true;
							break;
						}else{
							chooseStatus[i] = false;
						}
					}
					dataSrc[i] = dict.getCtrlCol()==null?"":dict.getCtrlCol();//获取资源列所有数据;//获取资源列所有数据
					i++;
				}
			}
			multiChoiceFuzzyQuerySpinner.setDataSrc(dataSrc);
			multiChoiceFuzzyQuerySpinner.setChooseStatus(chooseStatus);	
		}else{
			multiChoiceFuzzyQuerySpinner.setDataSrc( new String[0]);
			multiChoiceFuzzyQuerySpinner.setChooseStatus(new boolean[0]);	
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.gcg.grirms.comp.spinner.AbsSelectComp#setIsEnable(boolean)
	 */
	@Override
	public void setIsEnable(boolean isEnable) {
		multiChoiceFuzzyQuerySpinner.setEnabled(isEnable);
		
	}

	/*
	 * (non-Javadoc)
	 * @see com.gcg.grirms.listener.OnMultiChoiceConfirmListener#OnConfirm(boolean[])
	 */
	@Override
	public void OnConfirm(boolean[] chooseStatus) {
		
		if(chooseStatus!=null){
			StringBuilder sb = new StringBuilder();
			if(func.getFuncId()==-2907){//报表中的多选
				ReportActivity reportActivity = getReportActivity();
				if(reportActivity == null){
					return;
				}
				StringBuffer bf=new StringBuffer();
				for(int i=0;i<chooseStatus.length;i++){
					if(chooseStatus[i]){
						sb.append(",").append(dataSrcList.get(i).getDid());
						bf.append(",").append(dataSrcList.get(i).getCtrlCol());
					}
				}
				
				if(bf.length()>0){//将值添加到报表查询条件bundle中
					reportActivity.valueBundle.putString(reportActivity.getCurrentReportWhere().getColumnNumber()+"", bf.substring(1));
				}
				
			}else{
				for(int i=0;i<chooseStatus.length;i++){
					if(chooseStatus[i]){
						sb.append(",").append(dataSrcList.get(i).getDid());
					}
				}
			}
			
			if(sb.length()>0){//设置值
				this.value = sb.substring(1);
			}else{
				this.value=null;
			}
//			if (compFunc.getIsCacheFun()==1) {
//				FuncCacheDB fcDb = new FuncCacheDB(context);
//				fcDb.updateValueToCache(compFunc, value);
//			}
			dataName = value;
			
			JLog.d(TAG, "value==>"+value);
			
			
			if (nextComp != null && isInTable) {//下级不为空并且是在表格里
				if(queryWhere==null){
					nextComp.queryWhere=value;
				}else{
					nextComp.queryWhere=queryWhere+"@"+value;
				}
				oneToMany();
			}
		}
		if(lister!=null){
			lister.onMuktiConfirm();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.gcg.grirms.listener.OnMultiChoiceConfirmListener#fuzzySearch(java.lang.String)
	 */
	@Override
	public void fuzzySearch(String fuzzyContent) {
		getDataSrcList(fuzzyContent,queryWhereForDid);
		create();
	}

	/*
	 * (non-Javadoc)
	 * @see com.gcg.grirms.comp.spinner.AbsSelectComp#notifyDataSetChanged()
	 */
	@Override
	public void notifyDataSetChanged() {
		getDataSrcList(null,queryWhereForDid);
	}

	/*
	 * (non-Javadoc)
	 * @see com.gcg.grirms.comp.spinner.AbsSelectComp#setSelected(java.lang.String)
	 */
	@Override
	public void setSelected(String did) {
		this.value = did;
		String rs = "";
		if (func.getOrgOption()!=null && func.getOrgOption() == Func.ORG_STORE) {
			List<OrgStore> orgStoreList = new OrgStoreDB(context).findOrgListByStoreId(value);
			StringBuffer sb=new StringBuffer();
			for (int i = 0; i < orgStoreList.size(); i++) {
				OrgStore orgStore = orgStoreList.get(i);
				sb.append(",").append(orgStore.getStoreName());
			}
			rs = sb.length()>0 ? sb.substring(1).toString():value;
		}else if (func.getOrgOption()!=null && func.getOrgOption() == Func.ORG_USER) {
			List<OrgUser> orgUserList = new OrgUserDB(context).findOrgUserByUserId(value);
			StringBuffer sb=new StringBuffer();
			for (int i = 0; i < orgUserList.size(); i++) {
				OrgUser orgUser = orgUserList.get(i);
				sb.append(",").append(orgUser.getUserName());
			}
			rs = sb.length()>0 ? sb.substring(1).toString():value;
		}else{
			rs =  new DictDB(context).findDictMultiChoiceValueStr(did, func.getDictDataId(), func.getDictTable());
		}
		multiChoiceFuzzyQuerySpinner.setText(rs);
		multiChoiceFuzzyQuerySpinner.create();
		create();
		if (nextComp!=null && isInTable) {//多选模糊搜索的时候清空下级
			oneToMany();
		}
	}
	
	/**
	 * 设置初始值
	 */
	public void setInitData() {
		if(func.getDefaultType() != null && func.getDefaultType()==Func.DEFAULT_TYPE_SQL){//sql类型
			String itemValue = getDefaultSql();// 查询数据库中当前对应的值
			if(!TextUtils.isEmpty(itemValue)){
				queryWhereForDid=itemValue;
			}else{
				queryWhereForDid="-99";//没有上级值
			}
			notifyDataSetChanged();
//			setSelected(itemValue);//设置选择的值
//			value = itemValue;
			JLog.d(TAG, "itemValue==>" + itemValue);
		}else{
			notifyDataSetChanged();
			if(isMultichoiceUser){
				if(func.getFuncId()==-5||func.getFuncId()==-8968){//用户
					SubmitDB submitDB=new SubmitDB(context);
					int submitId = submitDB.selectSubmitIdNotCheckOut(planId, wayId, storeId,
							targetid, targetType,Submit.STATE_NO_SUBMIT);
					Submit item=submitDB.findSubmitById(submitId);
					if(item != null && replenishBundle==null){
						this.value = item.getSendUserId();
					}
					if(replenishBundle!=null && replenishBundle.containsKey(func.getFuncId()+"")){
						value = replenishBundle.getString(func.getFuncId()+"");
					}
					JLog.d(TAG, "itemValue==>" + value);
				}else if(func.getFuncId()==-4){//角色
					SubmitItem item=null;
					if(isLink){
						item=new SubmitItemTempDB(context).findSubmitItem(null, Submit.STATE_NO_SUBMIT, planId, 
								wayId, storeId, targetid, targetType, String.valueOf(func.getFuncId()));
					}else{
						item=new SubmitItemDB(context).findSubmitItem(null, Submit.STATE_NO_SUBMIT, planId, 
								wayId, storeId, targetid, targetType, String.valueOf(func.getFuncId()));
					}
					
					if(item != null){//不是查询条件
						this.value = item.getParamValue();
					}
					if(replenishBundle!=null && replenishBundle.containsKey(func.getFuncId()+"")){//查询条件
						value = replenishBundle.getString(func.getFuncId()+"");
					}
					JLog.d(TAG, "value==>"+value);
					String rs = new RoleDB(context).findDictMultiChoiceValueStr(value,null);
					multiChoiceFuzzyQuerySpinner.setText(rs);
				}else if(func.getFuncId()==-2907){//表示是报表中的多选
					ReportActivity reportActivity = getReportActivity();
					if(reportActivity == null || reportActivity.reportValue == null){
						return;
					}
					value=reportActivity.reportValue.getString(reportActivity.getCurrentReportWhere().getColumnNumber());
					if(!TextUtils.isEmpty(value)){
						multiChoiceFuzzyQuerySpinner.setText(reportActivity.valueBundle.getString(reportActivity.getCurrentReportWhere().getColumnNumber()+""));
					}else{
						multiChoiceFuzzyQuerySpinner.setText(context.getResources().getString(R.string.please_select));
					}
				}
			}else{
				SubmitItem item=null;
				if(isLink){//超链接
					item=new SubmitItemTempDB(context).findSubmitItem(null, Submit.STATE_NO_SUBMIT, planId, 
							wayId, storeId, targetid, targetType, String.valueOf(func.getFuncId()));
				}else{
					item=new SubmitItemDB(context).findSubmitItem(null, Submit.STATE_NO_SUBMIT, planId, 
							wayId, storeId, targetid, targetType, String.valueOf(func.getFuncId()));
				}
				if(item != null && replenishBundle==null){
					this.value = item.getParamValue();
					JLog.d(TAG, "value==>"+value);
				}
				if(replenishBundle!=null && replenishBundle.containsKey(func.getFuncId()+"")){
					value = replenishBundle.getString(func.getFuncId()+"");
					JLog.d(TAG, "value==>"+value);
				}
				if(func.getOrgOption()!=null && func.getOrgOption()!=Func.OPTION_LOCATION){//机构
					if(func.getOrgOption()==Func.ORG_OPTION){
						List<Org> orgList = new OrgDB(context).findOrgByOrgId(value);
						StringBuffer sb=new StringBuffer();
						for (int i = 0; i < orgList.size(); i++) {
							Org org = orgList.get(i);
							sb.append(",").append(org.getOrgName());
						}
						multiChoiceFuzzyQuerySpinner.setText(sb.length()>0 ? sb.substring(1).toString():value);
					}else if(func.getOrgOption()==Func.ORG_STORE){//店面
						List<OrgStore> orgStoreList = new OrgStoreDB(context).findOrgListByStoreId(value);
						StringBuffer sb=new StringBuffer();
						for (int i = 0; i < orgStoreList.size(); i++) {
							OrgStore orgStore = orgStoreList.get(i);
							sb.append(",").append(orgStore.getStoreName());
						}
						multiChoiceFuzzyQuerySpinner.setText(sb.length()>0 ? sb.substring(1).toString():value);
					}else{//用户
						List<OrgUser> orgUserList = new OrgUserDB(context).findOrgUserByUserId(value);
						StringBuffer sb=new StringBuffer();
						for (int i = 0; i < orgUserList.size(); i++) {
							OrgUser orgUser = orgUserList.get(i);
							sb.append(",").append(orgUser.getUserName());
						}
						multiChoiceFuzzyQuerySpinner.setText(sb.length()>0 ? sb.substring(1).toString():value);
					}
				}else{
					String rs =  new DictDB(context).findDictMultiChoiceValueStr(value, func.getDictDataId(), func.getDictTable());
					multiChoiceFuzzyQuerySpinner.setText(rs);
				}
			}
		}
		multiChoiceFuzzyQuerySpinner.create();//创建下拉框
//		create();
	}
	
	public void setMultichoiceUser(boolean isMultichoiceUser) {
		this.isMultichoiceUser = isMultichoiceUser;
	}
	
	
	public void setReplenishBundle(Bundle bundle){
		this.replenishBundle =  bundle;
	}

	public Bundle getReplenishBundle() {
		return replenishBundle;
	}
}
