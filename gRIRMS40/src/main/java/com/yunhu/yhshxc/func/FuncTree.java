package com.yunhu.yhshxc.func;

import android.content.Context;
import android.text.TextUtils;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.SubmitItem;
import com.yunhu.yhshxc.database.DictDB;
import com.yunhu.yhshxc.database.FuncDB;
import com.yunhu.yhshxc.database.SubmitItemDB;
import com.yunhu.yhshxc.database.SubmitItemTempDB;
import com.yunhu.yhshxc.exception.DataComputeException;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.RegExpvalidateUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

import gcg.org.debug.JLog;

public class FuncTree {
	
	protected final String TAG = "FuncTree";
	protected final int OPT_ADD = 1; //加法
	protected final int OPT_SUB = 2; //减法
	protected final int OPT_MUL = 3; //乘法
	protected final int OPT_DIV = 4; //除法
	protected final int OPT_MOD = 5; //取余
	protected final int OPT_MAX = 6; //最大值
	protected final int OPT_MIN = 7; //最小值
	protected final int OPT_ABS = 8; //绝对值
	
	protected final int TYPE_TWO = 100; //正常两项运算
	protected final int TYPE_ONE = 101; //单项运算
	protected final int TYPE_TWO_FIXED = 102; //两项运算，right是固定值
	
	public Context context;
	
	//当前控件
	public Func func = null;
	//当前控件操作后的值
	public String result;
	
	public Integer submitId;
	
	protected SubmitItem submitItem;
	
	public int option; //运算符号
	public Double fixedValue;
	
	protected int type; //运算类型
	
	//左子树
	protected FuncTree left = null;
	//右子树
	protected FuncTree right = null;
	//单叶子数
	protected FuncTree single  = null;
	
	public FuncTree(Context context,Func func) throws DataComputeException{
		if(func == null){
			throw new DataComputeException(context.getResources().getString(R.string.type_is_not_supported));
		}
		this.context = context;
		this.func = func;
	}

	public FuncTree(Context context,Func func,int submitId) throws DataComputeException {
		if(func == null){
			throw new DataComputeException(context.getResources().getString(R.string.type_is_not_supported));
		}
		this.context = context;
		this.func = func;
		this.submitId = submitId;
		setTree();
		result();
		String logInfo = "("+func.getName()+"/"+func.getFuncId()+"/"+func.getDefaultValue()+")";
		JLog.d(TAG, logInfo+"="+result);
	}
	
	/**
	 *设置无子叶节点的值
	 */
	protected void setValueForNoChlidNote(){
		//主要是为了获取无子叶节点的值
		if(func.getType() == Func.TYPE_HIDDEN){
			result = ((AbsFuncActivity)this.context).getDefaultVaiueByType(func);
		}else{
			submitItem = new SubmitItemDB(context).findSubmitItem(submitId, func.getFuncId()+"");
			if(submitItem != null && !TextUtils.isEmpty(submitItem.getParamValue())){
				result = submitItem.getParamValue();
			}
		}
	}
	
	protected void setTree() throws DataComputeException{
		//String logInfo = "("+func.getName()+"/"+func.getFuncId()+"/"+func.getDefaultValue()+")";
		//Log.i(TAG, logInfo);


		if(func.getDefaultType() == null || func.getDefaultType() != Func.DEFAULT_TYPE_COMPUTER){ //如果此控件没有公式，则代表为最底层叶节点
			setValueForNoChlidNote();
			return;
		}
		String rule = func.getDefaultValue();
		JLog.d(TAG, "funcName:"+func.getName());
		JLog.d(TAG, "rule:"+rule);
		String []rules = rule.split(",");
		option = Integer.valueOf(rules[1]);
		if(option == OPT_ABS){
			single = insert(rules[0]);
			type = TYPE_ONE;
		}else if("-1".equals(rules[2])){
			left = insert(rules[0]);
			fixedValue = Double.valueOf(rules[3]);
			type = TYPE_TWO_FIXED;
		}else{
			left = insert(rules[0]);
			right = insert(rules[2]);
			type = TYPE_TWO;
		}
	}
	
	protected FuncTree insert(String funcId) throws DataComputeException{
		Func childFunc = new FuncDB(context).findFuncListByFuncIdAndTargetId(funcId,func.getTargetid());
		FuncTree tree = new FuncTree(context,childFunc,submitId);
		return tree;
	}
	
	/**
	 * 计算当前结果
	 * @return
	 * @throws DataComputeException
	 */
	protected void result() throws DataComputeException{
		//只考虑当前控件计算出的结果
		if(type == TYPE_ONE){
			if(option == OPT_ABS && !TextUtils.isEmpty(single.result)){
				result = Math.abs(Double.valueOf(single.result))+"";
			}
		}else if(type == TYPE_TWO && left != null && right != null){
			TypeValue v1 = getValueBytype(left);
			TypeValue v2 = getValueBytype(right);
			result = compute(v1,v2);
		}else if(type == TYPE_TWO_FIXED){
			TypeValue v1 = getValueBytype(left);
			TypeValue v2 = new TypeValue();
			v2.type = TypeValue.TYPE_NUM;
			v2.value = String.valueOf(this.fixedValue);
			result = compute(v1,v2);
		}
	}
	
	protected String compute(TypeValue v1,TypeValue v2){
		String logInfo = "("+func.getName()+"/"+func.getFuncId()+")";
		JLog.d(TAG, logInfo+" DefaultValue"+func.getDefaultValue());
		String result = "";
		if(v1.type == TypeValue.TYPE_NUM && v2.type == TypeValue.TYPE_NUM){
			double num1 = TextUtils.isEmpty(v1.value)?0:Double.valueOf(v1.value);
			double num2 = TextUtils.isEmpty(v2.value)?0:Double.valueOf(v2.value);
			switch(option){
			case OPT_ADD:
				result = doubleTrans(num1 + num2);
				break;
			case OPT_SUB:
				result = doubleTrans(num1 - num2);
				break;
			case OPT_MUL:
				result = doubleTrans(num1 * num2);
				break;
			case OPT_DIV:
				double s = num2 == 0 ? 0 : (num1 / num2);
				result =  doubleTrans(s);
				break;
			case OPT_MOD:
				result =  doubleTrans(num2 == 0 ? 0 : Math.abs((num1 % num2)));
				break;
			case OPT_MAX:
				result =  String.valueOf(num1 > num2 ? num1 : num2);
				break;
			case OPT_MIN:
				result =  String.valueOf(num1 < num2 ? num1 : num2);
				break;
			}
		}else if(v1.type == TypeValue.TYPE_DATE && v2.type == TypeValue.TYPE_DATE){
			if(!TextUtils.isEmpty(v1.value) && !TextUtils.isEmpty(v2.value)){
				String timeStart = v1.value+" 00:00:00";
				String timeEnd = v2.value+" 00:00:00";
				switch(option){
					case OPT_SUB:
						result = String.valueOf(DateUtil.compareDateStr(timeEnd, timeStart)/(1000*60*60*24));
						break;
					case OPT_MAX:
						result =  String.valueOf(timeStart.compareTo(timeEnd) >= 0  ? timeStart : timeEnd);
						break;
					case OPT_MIN:
						result =  String.valueOf(timeStart.compareTo(timeEnd) <= 0 ? timeStart : timeEnd);
						break;
				}
			}
		}else if(v1.type == TypeValue.TYPE_TIME && v2.type == TypeValue.TYPE_TIME){
			if(!TextUtils.isEmpty(v1.value) && !TextUtils.isEmpty(v2.value)){
				String timeStart = v1.value;
				String timeEnd = v2.value;
				switch(option){
					case OPT_SUB:
						result = String.valueOf(DateUtil.compareDateStr(timeEnd, timeStart)/(1000*60));
						break;
					case OPT_MAX:
						result =  String.valueOf(timeStart.compareTo(timeEnd) >= 0  ? timeStart : timeEnd);
						break;
					case OPT_MIN:
						result =  String.valueOf(timeStart.compareTo(timeEnd) <= 0 ? timeStart : timeEnd);
						break;
				}
			}
		}else if(v1.type == TypeValue.TYPE_TIME && v2.type == TypeValue.TYPE_NUM){
			if(TextUtils.isEmpty(v1.value)){
				result = "";
			}else{
				Date time1 = DateUtil.getDate(v1.value);
				int num2 = (int) (TextUtils.isEmpty(v2.value)?0:Double.valueOf(v2.value));
				switch(option){
					case OPT_ADD:
						result = DateUtil.addDate(time1,Calendar.MINUTE, num2);
						break;
					case OPT_SUB:
						result = DateUtil.addDate(time1,Calendar.MINUTE, -num2);
						break;
				}
				
			}
		}else if(v1.type == TypeValue.TYPE_NUM && v2.type == TypeValue.TYPE_TIME){
			if(TextUtils.isEmpty(v1.value)){
				result = "";
			}else{
				int num1 = TextUtils.isEmpty(v1.value)?0:Integer.valueOf(v1.value);
				Date time2 = DateUtil.getDate(v2.value);
				switch(option){
					case OPT_ADD:
						result = DateUtil.addDate(time2,Calendar.MINUTE, num1);
						break;
					case OPT_SUB:
						result = DateUtil.addDate(time2,Calendar.MINUTE, -num1);
						break;
				}
				
			}
		}else if(v1.type == TypeValue.TYPE_DATE && v2.type == TypeValue.TYPE_NUM){
			if(TextUtils.isEmpty(v1.value)){
				result = "";
			}else{
				Date time1 = DateUtil.getDate(v1.value,DateUtil.DATAFORMAT_STR);
				int num2 = (int) (TextUtils.isEmpty(v2.value)?0:Double.valueOf(v2.value));
				switch(option){
					case OPT_ADD:
						result = DateUtil.addDate(time1,Calendar.DATE, num2);
						break;
					case OPT_SUB:
						result = DateUtil.addDate(time1,Calendar.DATE, -num2);
						break;
				}
				result = DateUtil.dateToDateString(DateUtil.getDate(result),DateUtil.DATAFORMAT_STR);
			}
		}else if(v1.type == TypeValue.TYPE_NUM && v2.type == TypeValue.TYPE_DATE){
			if(TextUtils.isEmpty(v2.value)){
				result = "";
			}else{
				int num1 = TextUtils.isEmpty(v1.value)?0:Integer.valueOf(v1.value);
				Date time2 = DateUtil.getDate(v2.value,DateUtil.DATAFORMAT_STR);
				switch(option){
					case OPT_ADD:
						result = DateUtil.addDate(time2,Calendar.DATE, num1);
						break;
					case OPT_SUB:
						result = DateUtil.addDate(time2,Calendar.DATE, -num1);
						break;
				}
				result = DateUtil.dateToDateString(DateUtil.getDate(result),DateUtil.DATAFORMAT_STR);
			}
		}else if(v1.type == TypeValue.TYPE_TIME && v2.type == TypeValue.TYPE_DATE){
			if (TextUtils.isEmpty(v1.value) || TextUtils.isEmpty(v2.value)) {
				result = "";
			}else{
				String timeStart = v1.value;
				String timeEnd = v2.value+" 00:00:00";
				switch(option){
				case OPT_SUB:
					result = String.valueOf(DateUtil.compareDateStr(timeEnd, timeStart)/(1000*60*60*24));
					break;
				case OPT_MAX:
					result =  String.valueOf(timeStart.compareTo(timeEnd) >= 0  ? timeStart : timeEnd);
					break;
				case OPT_MIN:
					result =  String.valueOf(timeStart.compareTo(timeEnd) <= 0 ? timeStart : timeEnd);
					break;
				}	
			}
		}else if(v1.type == TypeValue.TYPE_DATE && v2.type == TypeValue.TYPE_TIME){
			if (TextUtils.isEmpty(v1.value) || TextUtils.isEmpty(v2.value)) {
				result = "";
			}else{
				String timeStart = v1.value+" 00:00:00";
				String timeEnd = v2.value;
				switch(option){
				case OPT_SUB:
					result = String.valueOf(DateUtil.compareDateStr(timeEnd, timeStart)/(1000*60*60*24));
					break;
				case OPT_MAX:
					result =  String.valueOf(timeStart.compareTo(timeEnd) >= 0  ? timeStart : timeEnd);
					break;
				case OPT_MIN:
					result =  String.valueOf(timeStart.compareTo(timeEnd) <= 0 ? timeStart : timeEnd);
					break;
				}
			}
		}
		return result;
	}

	private String doubleTrans(double d){
//		  if(Math.round(d)-d==0){
//			  return String.valueOf((long)d);
//		  }else{
//			  return new DecimalFormat("0.00").format(d);
//		  }
		return new DecimalFormat("#.###").format(new BigDecimal(String.valueOf(d)).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue());
	}
	
	class TypeValue{
		static final int TYPE_NUM = 1;
		static final int TYPE_TIME = 2;
		static final int TYPE_DATE = 3;
		
		Integer type = null;
		String value = null;
	}
	
	/**
	 * 返回计算项的类型数值
	 * @param funcId
	 * @param value
	 * @param submitId
	 * @param menuType
	 * @return
	 * @throws DataComputeException
	 */
	protected TypeValue getValueBytype(FuncTree tree) throws DataComputeException{
		String value = tree.result;
		TypeValue typeValue = new TypeValue();
		Func func = tree.func;
		if(func.getType() == Func.TYPE_DATEPICKERCOMP ){
			
			typeValue.type = TypeValue.TYPE_DATE;
			typeValue.value = value;
			
		}else if(func.getType() == Func.TYPE_TEXTCOMP){
			typeValue.type = TypeValue.TYPE_NUM;
			if(!RegExpvalidateUtils.isNumeric(value.trim())){
				throw new DataComputeException("\""+func.getName()+"\""+context.getResources().getString(R.string.only_support_figure_type)); //由于输入错误，导致计算错误，抛出异常
			}
			typeValue.value = value;
		}else if (func.getType() == Func.TYPE_EDITCOMP && (func.getCheckType() != null && func.getCheckType() == Func.CHECK_TYPE_NUMERIC)) {
			
			typeValue.type = TypeValue.TYPE_NUM;
			typeValue.value = value;
			
		} else if(func.getType() == Func.TYPE_EDITCOMP){
			
			typeValue.value = value;
			
			if(!TextUtils.isEmpty(typeValue.value) && !RegExpvalidateUtils.isNumeric(typeValue.value.trim())){
				throw new DataComputeException("\""+func.getName()+"\""+context.getResources().getString(R.string.only_support_figure_type)); //由于输入错误，导致计算错误，抛出异常
			}
			typeValue.type = TypeValue.TYPE_NUM;
			
		}else if(func.getType() == Func.TYPE_SELECTCOMP){
			
			typeValue.type = TypeValue.TYPE_NUM;
			typeValue.value = new DictDB(context).findDictMultiChoiceValueStr(value, func.getDictDataId(), func.getDictTable());
			
		}else if(func.getType() == Func.TYPE_SELECT_OTHER){
			if(TextUtils.isEmpty(value)){
				typeValue.value = "0";
			}else if(!RegExpvalidateUtils.isInteger(value)){
				typeValue.value = value;
			}else if(!"-1".equals(value)){
				typeValue.value = new DictDB(context).findDictMultiChoiceValueStr(value, func.getDictDataId(), func.getDictTable());
			}else if(submitId != null){
				SubmitItem item = null;
				if (this instanceof FuncTreeForLink) {
					item = new SubmitItemTempDB(context).findSubmitItem(submitId,func.getFuncId()+"_other");
				}else{
					item = new SubmitItemDB(context).findSubmitItem(submitId,func.getFuncId()+"_other");
				}
				if (item !=null) {
					typeValue.value = item.getParamValue();
				}
			}else{
				typeValue.value = value;
			}
			if(!RegExpvalidateUtils.isNumeric(typeValue.value.trim())){
				throw new DataComputeException("\""+func.getName()+"\""+context.getResources().getString(R.string.only_support_figure_type)); //由于输入错误，导致计算错误，抛出异常
			}
			typeValue.type = TypeValue.TYPE_NUM;
			
		}else if(func.getType() == Func.TYPE_HIDDEN){
			
			if(func.getDefaultType() == Func.DEFAULT_TYPE_OTHER){
				typeValue.type = TypeValue.TYPE_NUM;
				typeValue.value = value;
			}else if(func.getDefaultType() == Func.DEFAULT_TYPE_DATE_NO_TIME){
				typeValue.type = TypeValue.TYPE_DATE;
				typeValue.value = value;
			}else if(func.getDefaultType() == Func.DEFAULT_TYPE_DATE || func.getDefaultType() == Func.DEFAULT_TYPE_STARTDATE){
				typeValue.type = TypeValue.TYPE_TIME;
				typeValue.value = value;
			}else{
				typeValue.value = value;
				if(!TextUtils.isEmpty(typeValue.value) && !RegExpvalidateUtils.isNumeric(typeValue.value.trim())){
					typeValue.type = TypeValue.TYPE_TIME;
				}else{
					typeValue.type = TypeValue.TYPE_NUM;
				}
			}
			
		}
		 
		return typeValue;
	}

}
