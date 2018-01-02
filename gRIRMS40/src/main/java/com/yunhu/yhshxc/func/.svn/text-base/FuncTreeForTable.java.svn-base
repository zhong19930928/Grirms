package com.yunhu.yhshxc.func;

import gcg.org.debug.JLog;

import java.util.List;

import android.content.Context;
import android.util.Log;

import com.yunhu.yhshxc.activity.TableCompActivity;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.comp.Component;
import com.yunhu.yhshxc.database.FuncDB;
import com.yunhu.yhshxc.exception.DataComputeException;

/**
 * 表格中的计算控件
 * @author gcg_jishen
 *
 */
public class FuncTreeForTable extends FuncTree{

	private List<Component> comList;
	
	public FuncTreeForTable(Context context, Func func,List<Component> list) throws DataComputeException {
		super(context, func);
		this.comList = list;
		setTree();
		result();
		String logInfo = "("+func.getName()+"/"+func.getFuncId()+"/"+func.getDefaultValue()+")";
		JLog.d(TAG, logInfo+"="+result);
	}
	
	@Override
	protected void setValueForNoChlidNote() {
		
		if(func!=null){
			if(func.getType() == Func.TYPE_HIDDEN){
				result = ((TableCompActivity)this.context).getHiddenValue(func);
			}else{
				for(Component com :comList){
					Log.e(TAG, func.getFuncId()+"=>"+com.compFunc.getFuncId());
					if(com.compFunc.getFuncId().intValue() == func.getFuncId()){
						this.result = com != null ? com.value : null;
						break;
					}
				}
				
			}
		}
	}
	
	@Override
	protected FuncTree insert(String funcId) throws DataComputeException {
		Func childFunc = new FuncDB(context).findFuncListByFuncIdAndTargetId(funcId,func.getTargetid());
		FuncTreeForTable tree = new FuncTreeForTable(context,childFunc,comList);
		return tree;
	}
}
