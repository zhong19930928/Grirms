package com.yunhu.yhshxc.func;

import android.content.Context;
import android.text.TextUtils;

import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.database.FuncDB;
import com.yunhu.yhshxc.database.SubmitItemTempDB;
import com.yunhu.yhshxc.exception.DataComputeException;
/**
 * 超链接模块中的计算控件
 * @author gcg_jishen
 *
 */
public class FuncTreeForLink extends FuncTree{

	public FuncTreeForLink(Context context, Func func, int submitId) throws DataComputeException {
		super(context, func, submitId);
	}

	@Override
	protected void setValueForNoChlidNote() {
		if(func.getType() == Func.TYPE_HIDDEN){
			result = ((AbsFuncActivity)this.context).getDefaultVaiueByType(func);
		}else{
			submitItem = new SubmitItemTempDB(context).findSubmitItem(submitId, func.getFuncId()+"");
			if(submitItem != null && !TextUtils.isEmpty(submitItem.getParamValue())){
				result = submitItem.getParamValue();
			}
		}
	}

	@Override
	protected FuncTree insert(String funcId) throws DataComputeException {
		Func childFunc = new FuncDB(context).findFuncListByFuncIdAndTargetId(funcId,func.getTargetid());
		FuncTree tree = new FuncTreeForLink(context,childFunc,submitId);
		return tree;
	}

	
}
