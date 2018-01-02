package com.yunhu.yhshxc.comp.spinner;

import gcg.org.debug.JLog;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.Submit;
import com.yunhu.yhshxc.bo.SubmitItem;
import com.yunhu.yhshxc.comp.CompDialog;
import com.yunhu.yhshxc.database.DictDB;
import com.yunhu.yhshxc.database.SubmitItemDB;
import com.yunhu.yhshxc.database.SubmitItemTempDB;

/**
 * 下拉框
 * @author jishen
 */
public class SingleSpinnerComp extends AbsSelectComp implements OnSingleSpinnerSeclectLisner {
	private String TAG = "SpinnerComp";
	public SingleSpinner spinner;
//	private ArrayAdapter<String> adapter;
	public String selected; //选择ITEM的值
	private Bundle replenishBundle = null;//携带查询条件的bundle
	private OnMuktiChiceSelecteLister lisner;
	private CompDialog compDialog;
	public Bundle getReplenishBundle() {
		return replenishBundle;
	}



	public void setReplenishBundle(Bundle replenishBundle) {
		this.replenishBundle = replenishBundle;
	}


	/**
	 * 控件不是在表格中
	 * @param context
	 * @param func
	 * @param queryWhere 层级关系
	 * @param orgBundle 机构关系
	 * @param compDialog
	 */
	public SingleSpinnerComp(Context context, Func func,String queryWhere,Bundle orgBundle,CompDialog compDialog,OnMuktiChiceSelecteLister lisner) {
		super(context, func,queryWhere,orgBundle);
		this.compFunc=func;
		this.context=context;
		this.lisner = lisner;
		this.func=func;
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
	public SingleSpinnerComp(Context context, Func func,String queryWhere,Bundle orgBundle) {
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
		view = View.inflate(context, R.layout.select_comp_single, null);
		spinner= (SingleSpinner) view.findViewById(R.id.selectComp);
		spinner.setOnMultiChoiceConfirmListener(this);
		//查询已设置得值,传入进去
		SubmitItem item = null;
		if (isLink){
			item = new SubmitItemTempDB(context).findSubmitItemByParamName(func.getFuncId()+"");
		}else{
			item = new SubmitItemDB(context).findSubmitItemByFuncId(func.getFuncId());
		}
		if (item!=null){
			spinner.setSelected(item.getParamValue());
		}
	}
	

	
	/*
	 * (non-Javadoc)
	 * @see com.gcg.grirms.comp.spinner.AbsSelectComp#notifyDataSetChanged()
	 */
	@Override
	public void notifyDataSetChanged() {
		JLog.d(TAG, "queryWhere"+queryWhere);
		getDataSrcList(null,queryWhereForDid);//通过父类获取值
		if(dataSrcList!=null){
			setDataForSelectComp(dataSrcList);
		}
	}
	

	/**
	 * 
	 * @param
	 */
	public void setDataForSelectComp(List<Dictionary> srcDictList){
//		adapter = bindData();//改变资源列获取新的Adapter
//		if(adapter!=null){
//			spinner.setAdapter(adapter);
//			adapter.notifyDataSetChanged();
//		}
		String[] str=getDateSrc();
		spinner.setDataSrc(str);
		if(srcDictList == null || srcDictList.isEmpty()){
			onSingleChoice(null,null,-1,-100);
		}else{
			String selectStr = spinner.getSelected();
			if (!TextUtils.isEmpty(selectStr)){
				for (Dictionary dict : dataSrcList) {
				      if (selectStr.equals(dict.getDid())){
						  spinner.setSelected(dict.getCtrlCol());
						  break;
					  }
				 }
			}
		}
	}
	
	/**
	 * @return绑定数据使用的Adapter
	 */
	
//	private ArrayAdapter<String> bindData() {
//		String[] str=getDateSrc();
//		if(str!=null){
//			ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
//					android.R.layout.simple_dropdown_item_1line, str);
//			adapter.setDropDownViewResource(R.layout.sprinner_check_item);
////			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//			return adapter;
//		}
//		return null;
//	}


	/**
	 * 操作选中那一项
	 */
	@Override
	public void setSelected(String did) {
		if(TextUtils.isEmpty(did)){//did为空的情况
			notifyDataSetChanged();
			onSingleChoice(null,null,-1,-100);
		}else{
			value = did;
			if(dataSrcList != null && !dataSrcList.isEmpty() && !TextUtils.isEmpty(did)){
				JLog.d(TAG, "did==>"+did);
				int i= 0;
				for(Dictionary dict:dataSrcList){//设置当前显示为传过来的did的值
					JLog.d(TAG,dataSrcList.get(i).getCtrlCol()+"");
					if(dict.getDid().equals(did)){
						selectedValue  = dict.getNote();
						JLog.d(TAG, func.getName()+"=>selece" +
								"d:"+i);
						spinner.setSelection(i);
						break;
					}
					i++;
				}
			}else{
				value = "";
			}
		}
	}


	/**
	 * 设置初始化数据
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
			//sql类型的下拉框 sql查询到的结果不一定是最终的value
//			setSelected(itemValue);//设置选择的值
//			value = itemValue;
			JLog.d(TAG, "itemValue==>" + itemValue);
		}else{
			notifyDataSetChanged();
			SubmitItem item=null;
			if(isLink){//超链接的情况下查询临时表中的数据
				item=new SubmitItemTempDB(context).findSubmitItem(null, Submit.STATE_NO_SUBMIT, planId, 
						wayId, storeId, targetid, targetType, String.valueOf(func.getFuncId()));
			}else{
				item=new SubmitItemDB(context).findSubmitItem(null, Submit.STATE_NO_SUBMIT, planId, 
						wayId, storeId, targetid, targetType, String.valueOf(func.getFuncId()));
			}
			
			if(item!=null && replenishBundle==null){//如果不是查询条件 查找数据库中的值
				String itemValue = item.getParamValue();// 查询数据库中当前对应的值
				setSelected(itemValue);
				value = itemValue;
				JLog.d(TAG, "itemValue==>" + itemValue);
			}else{//查询条件 查找replenishBundle中的值
				if(replenishBundle!=null && replenishBundle.containsKey(func.getFuncId()+"")){
					setSelected(replenishBundle.getString(func.getFuncId()+""));
					value = replenishBundle.getString(func.getFuncId()+"");
					JLog.d(TAG, "itemValue==>" + value);
				}
				
			}
		}
		
	}
	/*
	 * (non-Javadoc)
	 * @see com.gcg.grirms.comp.spinner.AbsSelectComp#setIsEnable(boolean)
	 */
	@Override
	public void setIsEnable(boolean isEnable) {
		spinner.setEnabled(isEnable);
	}



	@Override
	public void onSingleChoice(AdapterView<?> parent, View view, int position,
			long id) {
		if(dataSrcList!=null && !dataSrcList.isEmpty()){//查询到的数据不为空
			if(id != -100){//选中的不为空
				selected = dataSrcList.get(position).getCtrlCol();

				value = dataSrcList.get(position).getDid();
				selectedValue  = dataSrcList.get(position).getCtrlCol();
				if (nextComp != null && isInTable) {//下级不为空并且是在表格里 拼接层级关系
					if(queryWhere==null){
						nextComp.queryWhere=value;
					}else{
						nextComp.queryWhere=queryWhere+"@"+value;
					}
					oneToMany();//设置下级联动
				}
			}else{//选中的数据位空
				value="";
				selectedValue  = "";
				if (nextComp != null) {
					if(isInTable){//如果控件在表格中要设置下级联动
						if(queryWhere==null){
							nextComp.queryWhere="-2";
						}else{
							nextComp.queryWhere=queryWhere+"@"+"-2";
						}
						oneToMany();
					}else{
						nextComp.notifyDataSetChanged();
					}
				}
			}
		}else{
			value="";
			selectedValue  = "";
			if (nextComp != null) {//当前数据位空
				if(isInTable){//在表格中
					if(queryWhere==null){//设置下级级联条件
						nextComp.queryWhere="-2";//拼接-2这样就查询不到值
					}else{
						nextComp.queryWhere=queryWhere+"@"+"-2";
					}
					oneToMany();
				}else{
					nextComp.notifyDataSetChanged();
				}
			}
		}
		
		if (isInTable && "1".equals(func.getShowColor())) {
			if (!TextUtils.isEmpty(value) && position!=0) {
				
				int count = dataSrcList.get(position).getSelectCount()+1;
				new DictDB(context).updateDictSelectCount(func.getDictTable(), value, count);
			}
		}
		//如果是缓存控件,把名称存值到缓存表中
	     dataName = selected;
		//单选如果选择的值和数据库(超链接,非超链接区分)已存在的值相同,认为是取消值,删除数据库中的值,并将当前选择值置空,
		  if (!isLink){
			  SubmitItemDB itemDB = new SubmitItemDB(context);
			  SubmitItem sub1 =itemDB.findSubmitItemByFuncId(func.getFuncId());
			  if (sub1!=null){
				  //判断是否已经选择过此选项
				  if (!TextUtils.isEmpty(sub1.getParamValue())){
					  if (value.equals(sub1.getParamValue())){
						   sub1.setParamValue("");
						   itemDB.updateSubmitItem(sub1,false);
//					        itemDB.deleteSubmitItem(sub1);
						  if (compDialog!=null){
							  compDialog.isHaveValue=false;
						  }
					  }

				  }
			  }
		  }else{
             SubmitItemTempDB tempDB = new SubmitItemTempDB(context);
			  SubmitItem sub2 = tempDB.findSubmitItemByParamName(func.getFuncId()+"");
			  if (sub2!=null){
				  //判断是否已经选择过此选项
				  if (!TextUtils.isEmpty(sub2.getParamValue())){
					  if (value.equals(sub2.getParamValue())){
						  sub2.setParamValue("");
						  tempDB.updateSubmitItem(sub2);
//					        itemDB.deleteSubmitItem(sub1);
						  if (compDialog!=null){
							  compDialog.isHaveValue=false;
						  }
					  }

				  }
//				  if (value.equals(sub2.getParamValue())){
//					  sub2.setParamValue("");
//					  tempDB.updateSubmitItem(sub2);
////					  tempDB.deleteSubmitItem(sub2);
//					  if (compDialog!=null){
//						  compDialog.isHaveValue=func.getFuncId();
//					  }
//				  }
			  }

		  }

		if(lisner!=null){
			lisner.onMuktiConfirm();
		}
		
	}

	public void setCompDialog(CompDialog cp){
		this.compDialog = cp;
	}



}
