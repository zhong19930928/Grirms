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
import com.yunhu.yhshxc.widget.MultiChoiceSpinner;

import java.util.List;

import gcg.org.debug.JLog;

/**
 * 多选下拉框
 * @author jishen
 */
public class MultiChoiceSpinnerComp extends AbsSelectComp implements OnMultiChoiceConfirmListener{
	private String TAG="MultiChoiceSpinnerComp";
	public MultiChoiceSpinner multiChoiceSpinner;
	private Bundle replenishBundle = null;//携带查询条件的bundle
	private Bundle chooseMapBundle = null;//非机构的联动关系
	private OnMuktiChiceSelecteLister onMuktiChiceSelecteLister;

	/**
	 *	控件不在表格中使用
	 * @param context
	 * @param func
	 * @param queryWhere 上级级联关系
	 * @param orgBundle 机构级联关系
	 * @param compDialog
	 */
	public MultiChoiceSpinnerComp(Context context, Func func,String queryWhere,Bundle orgBundle,CompDialog compDialog,OnMuktiChiceSelecteLister onMuktiChiceSelecteLister) {
		super(context, func,queryWhere,orgBundle);
		this.context=context;
		this.onMuktiChiceSelecteLister = onMuktiChiceSelecteLister;
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
	 * @param queryWhere 上级级联关系
	 * @param orgBundle 机构级联关系
	 */
	public MultiChoiceSpinnerComp(Context context, Func func,String queryWhere,Bundle orgBundle) {
		super(context, func,queryWhere,orgBundle);
		this.context=context;
		this.func=func;
		this.compFunc=func;
		init();
	}
	
	/**
	 * 初始化实例
	 */
	private void init(){
		view = View.inflate(context, R.layout.comp_multi_choice_spinner, null);
		multiChoiceSpinner = (MultiChoiceSpinner)view.findViewById(R.id.multiChoiceSpinner);
		multiChoiceSpinner.setOnMultiChoiceConfirmListener(this);
	}
	
	/**
	 * 创建下拉框列表（展开）
	 */
	public void create(){
		if(dataSrcList!=null && !dataSrcList.isEmpty()){//数据不为空
			int size = dataSrcList.size();
			String []dataSrc = new String[size];//设置数据数组的大小
			boolean []chooseStatus = new boolean[size];//选中状态
			int i=0;
			if(TextUtils.isEmpty(this.value)){
				for (Dictionary dict : dataSrcList) {
					chooseStatus[i] = false;
					dataSrc[i] = dict.getCtrlCol()==null?"":dict.getCtrlCol();//获取资源列所有数据
					i++;
				}
			}else{
				String []values = value.split(",");
				for (Dictionary dict : dataSrcList) {
					for(int j = 0; j<values.length; j++){
						if(values[j].equals(dict.getDid())){//如果有值就置为true
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
			multiChoiceSpinner.setDataSrc(dataSrc);//设置数据源
			multiChoiceSpinner.setChooseStatus(chooseStatus);//设置选中状态数组			
		}else{
			multiChoiceSpinner.setDataSrc( new String[0]);
			multiChoiceSpinner.setChooseStatus(new boolean[0]);	
		}
	}
	

	/*
	 * (non-Javadoc)
	 * @see com.gcg.grirms.comp.spinner.AbsSelectComp#setIsEnable(boolean)
	 */
	@Override
	public void setIsEnable(boolean isEnable) {
		multiChoiceSpinner.setEnabled(isEnable);
		
	}

	/*
	 * (non-Javadoc)
	 * @see com.gcg.grirms.listener.OnMultiChoiceConfirmListener#OnConfirm(boolean[])
	 */
	@Override
	public void OnConfirm(boolean[] chooseStatus) {
		
		if(chooseStatus!=null){
			StringBuilder sb = new StringBuilder();
			StringBuilder sb2 = new StringBuilder();
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
				if(bf.length()>0){//将选择的值放到存放报表的查询条件中
					reportActivity.valueBundle.putString(reportActivity.getCurrentReportWhere().getColumnNumber()+"", bf.substring(1));
				}
				
			}else{//非报表的情况
				for(int i=0;i<chooseStatus.length;i++){
					if(chooseStatus[i]){//拼接选择的值
						sb.append(",").append(dataSrcList.get(i).getDid());
						sb2.append(",").append(dataSrcList.get(i).getCtrlCol());
					}
				}
			}
			
			if(sb.length()>0){//设置值
				this.value = sb.substring(1);
				this.dataName=sb2.substring(1);
	
			}else{
				this.value=null;
				
			}
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
		if(onMuktiChiceSelecteLister!=null){
			onMuktiChiceSelecteLister.onMuktiConfirm();
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
		if(chooseMapBundle!=null && chooseMapBundle.containsKey(func.getFuncId()+"")){
			queryWhere=chooseMapBundle.getString(func.getFuncId()+"");
		}
		JLog.d(TAG, "queryWhere"+queryWhere);
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
		multiChoiceSpinner.setText(rs);
		multiChoiceSpinner.create();
		create();
		if (nextComp!=null && isInTable) {//多选的时候清空下级
			oneToMany();
		}
		
	}
	/**
	 * 初始化数据
	 */
	public void setInitData() {
		if(func.getDefaultType() != null && func.getDefaultType()==Func.DEFAULT_TYPE_SQL){//sql情况
			String itemValue = getDefaultSql();// 查询数据库中当前对应的值
			if(!TextUtils.isEmpty(itemValue)){
				queryWhereForDid=itemValue;
			}else{
				queryWhereForDid="-99";
			}
			notifyDataSetChanged();
			//sql类型的下拉框 sql查询到的结果不一定是最终的value
//			setSelected(itemValue);//设置显示为当前选中的值
//			value = itemValue;
			JLog.d(TAG, "itemValue==>" + itemValue);
		}else{
			notifyDataSetChanged();
			if(isMultichoiceUser){//多选的用户
				if(func.getFuncId()==-5||func.getFuncId()==-8968){//用户
					SubmitDB submitDB=new SubmitDB(context);
					int submitId = submitDB.selectSubmitIdNotCheckOut(planId, wayId, storeId,
							targetid, targetType,Submit.STATE_NO_SUBMIT);
					Submit item=submitDB.findSubmitById(submitId);
					if(item != null && replenishBundle==null){//查找表单中存放的用户ID
						this.value = item.getSendUserId();
					}
					if(replenishBundle!=null && replenishBundle.containsKey(func.getFuncId()+"")){//查询条件
						value = replenishBundle.getString(func.getFuncId()+"");
					}
					
					String s=new OrgUserDB(context).findDictMultiChoiceValueStr(value, null);//查询当前的值
					multiChoiceSpinner.setText(s);
					
					JLog.d(TAG, "itemValue==>" + value);
				}else if(func.getFuncId()==-4){//角色
					SubmitItem item=null;
					if(isLink){//超链接查询临时表
						item=new SubmitItemTempDB(context).findSubmitItem(null, Submit.STATE_NO_SUBMIT, planId, 
								wayId, storeId, targetid, targetType, String.valueOf(func.getFuncId()));
					}else{
						item=new SubmitItemDB(context).findSubmitItem(null, Submit.STATE_NO_SUBMIT, planId, 
								wayId, storeId, targetid, targetType, String.valueOf(func.getFuncId()));
					}
					
					if(item != null && replenishBundle==null){
						this.value = item.getParamValue();//设置默认值
					}
					if(replenishBundle!=null && replenishBundle.containsKey(func.getFuncId()+"")){//如果是查询条件设置默认值
						value = replenishBundle.getString(func.getFuncId()+"");
					}
					JLog.d(TAG, "value==>"+value);
					String rs = new RoleDB(context).findDictMultiChoiceValueStr(value,null);//查询值
					multiChoiceSpinner.setText(rs);
				}else if(func.getFuncId()==-2907){//表示是报表中的多选
					ReportActivity reportActivity = getReportActivity();
					if(reportActivity == null || reportActivity.reportValue == null){
						return;
					}
					//查询值
					value=reportActivity.reportValue.getString(reportActivity.getCurrentReportWhere().getColumnNumber());
					//如果有值就显示当前选择的值否则就显示请选择
					if(!TextUtils.isEmpty(value)){
						multiChoiceSpinner.setText(reportActivity.valueBundle.getString(reportActivity.getCurrentReportWhere().getColumnNumber()+""));
					}else{
						multiChoiceSpinner.setText(context.getResources().getString(R.string.please_select));
					}
					JLog.d(TAG, "value==>"+value);
				}
			}else{//普通的多选下拉框
				SubmitItem item=null;
				if(isLink){//超链接的请款查询临时表中的数据
					item=new SubmitItemTempDB(context).findSubmitItem(null, Submit.STATE_NO_SUBMIT, planId, 
							wayId, storeId, targetid, targetType, String.valueOf(func.getFuncId()));
				}else{
					item=new SubmitItemDB(context).findSubmitItem(null, Submit.STATE_NO_SUBMIT, planId, 
							wayId, storeId, targetid, targetType, String.valueOf(func.getFuncId()));
				}
				
				if(item != null && replenishBundle==null){//replenishBundle==null说明不是查询和修改，不是审核
					this.value = item.getParamValue();
					JLog.d(TAG, "value==>"+value);
				}
				if(replenishBundle!=null && replenishBundle.containsKey(func.getFuncId()+"")){//查询条件
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
						multiChoiceSpinner.setText(sb.length()>0 ? sb.substring(1).toString():value);
					}else if(func.getOrgOption()==Func.ORG_STORE){//店面
						List<OrgStore> orgStoreList = new OrgStoreDB(context).findOrgListByStoreId(value);
						StringBuffer sb=new StringBuffer();
						for (int i = 0; i < orgStoreList.size(); i++) {
							OrgStore orgStore = orgStoreList.get(i);
							sb.append(",").append(orgStore.getStoreName());
						}
						multiChoiceSpinner.setText(sb.length()>0 ? sb.substring(1).toString():value);
					}else{//用户
						List<OrgUser> orgUserList = new OrgUserDB(context).findOrgUserByUserId(value);
						StringBuffer sb=new StringBuffer();
						for (int i = 0; i < orgUserList.size(); i++) {
							OrgUser orgUser = orgUserList.get(i);
							sb.append(",").append(orgUser.getUserName());
						}
						multiChoiceSpinner.setText(sb.length()>0 ? sb.substring(1).toString():value);
					}
				}else{
					String rs =  new DictDB(context).findDictMultiChoiceValueStr(value, func.getDictDataId(), func.getDictTable());
					multiChoiceSpinner.setText(rs);
				}
			}
			multiChoiceSpinner.create();
			create();//创建下拉框
		}
	}
	
	/**
	 * 设置当前多选下拉框是否是用户
	 * @param isMultichoiceUser true是 false 不是
	 */
	public void setMultichoiceUser(boolean isMultichoiceUser) {
		this.isMultichoiceUser = isMultichoiceUser;
	}
	
	public void setReplenishBundle(Bundle bundle){
		this.replenishBundle =  bundle;
	}

	public Bundle getReplenishBundle() {
		return replenishBundle;
	}
	public Bundle getChooseMapBundle() {
		return chooseMapBundle;
	}

	public void setChooseMapBundle(Bundle chooseMapBundle) {
		this.chooseMapBundle = chooseMapBundle;
	}
	
	
}
