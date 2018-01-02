package com.yunhu.yhshxc.func;

import android.content.Context;

import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.database.VisitFuncDB;
import com.yunhu.yhshxc.exception.DataComputeException;

/**
 * 拜访中的计算控件
 * @author gcg_jishen
 *
 */
public class FuncTreeForVisit extends FuncTree{

	public FuncTreeForVisit(Context context, Func func, int submitId) throws DataComputeException {
		super(context, func, submitId);
	}

	@Override
	protected FuncTree insert(String funcId) throws DataComputeException{
		Func childFunc = new VisitFuncDB(context).findFuncListByFuncIdAndTargetId(funcId,func.getTargetid());
		FuncTree tree = new FuncTreeForVisit(context,childFunc,submitId);
		return tree;
	}
}
