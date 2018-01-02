package com.yunhu.yhshxc.comp;

import gcg.org.debug.JLog;

import java.util.Calendar;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.Submit;
import com.yunhu.yhshxc.bo.SubmitItem;
import com.yunhu.yhshxc.database.SubmitItemDB;
import com.yunhu.yhshxc.database.SubmitItemTempDB;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;

/**
 * 输入框类型的控件
 * @author jishen
 *
 */
public class EditComp extends Component {
	private String TAG = "EditComp";
	private Context context;
	/**
	 * 当前输入框
	 */
	private EditText currentEditText;
	/**
	 * 当前func
	 */
	private Func func;
	private View view;
	private CompDialog compDialog;
	/**
	 * sql查询出来的结果
	 */
	private String defaultSql;
	/**
	 * 携带查询条件的bundle
	 */
	private Bundle replenishBundle = null;
       private LinearLayout layout=null;
	public EditComp(Context context, Func func,
			CompDialog compDialog) {
		this.context = context;
		this.compDialog = compDialog;
		this.func = func;
		this.type = func.getType();
		this.param = func.getFuncId();
		this.compFunc=func;
		if(compDialog!=null){//compDialog是空是表示是非表格中的
			this.putData(compDialog.type, compDialog.wayId, compDialog.planId, compDialog.storeId, compDialog.targetid, compDialog.targetType);
		}
		view = View.inflate(context, R.layout.edit_comp, null);
		currentEditText = (EditText) view.findViewById(R.id.textEditTextComp);

        
	}
	public EditText getEditText(){
		return currentEditText;
	}

	/**
	 * 获取当前func
	 * @return
	 */
	public Func getFunc() {
		return func;
	}
	/**
	 * 设置默认值
	 * @param value 默认值
	 */
	public void setValue(String value) {
		if (!TextUtils.isEmpty(value)) {
			currentEditText.setText(value);
		}
		this.value = value;
	}
	
	@Override
	public View getObject() {
		//DataType 控制键盘  checkType控制验证
		if(func.getDataType() != null){
			switch(func.getDataType()){
			case Func.DATATYPE_BIG_INTEGER:  //整数（4到10位）
				currentEditText.setSingleLine(true);
				currentEditText.setInputType(InputType.TYPE_CLASS_PHONE);
				setMaxLength(10);
				break;
			case Func.DATATYPE_EDITCOMP_NUMERIC:  //整数（4到10位）
				currentEditText.setSingleLine(true);
				currentEditText.setInputType(InputType.TYPE_CLASS_PHONE);
				setMaxLength(10);
				break;
			case Func.DATATYPE_SMALL_INTEGER:  // 小整数（<=4位）
				currentEditText.setInputType(InputType.TYPE_CLASS_PHONE);
				currentEditText.setSingleLine(true);
				setMaxLength(4);
				break;
			case Func.DATATYPE_DECIMAL:  // 小数类型
				currentEditText.setInputType(InputType.TYPE_CLASS_PHONE);
				currentEditText.setSingleLine(true);
				if(func.getLength() != null){
					setMaxLength(func.getLength());
				}
				break;
			case Func.DATATYPE_PWD:  // 密码类型
				currentEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
				if(func.getLength() != null){
					setMaxLength(func.getLength());
				}
				break;
			case Func.DATATYPE_AREATEXT:  // 文本域类型
				currentEditText.setSingleLine(false);
				currentEditText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
				if(func.getLength() != null){
					setMaxLength(func.getLength());
				}
				break;
			default:
				currentEditText.setSingleLine(false);
				currentEditText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
				if(func.getLength() != null){
					setMaxLength(func.getLength());
				}
				break;
			}
		}else{
			currentEditText.setSingleLine(false);
			currentEditText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
			if(func.getLength() != null){
				setMaxLength(func.getLength());
			}
		}
        
		initData();// 初始化数据
		currentEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				EditComp.this.value = s.toString();
				Log.d(TAG, EditComp.this.value);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		return view;
	}

	/*
	 * 初始化数据
	 */
	private void initData() {
		
		if(func.getDefaultType() != null && func.getDefaultType()==Func.DEFAULT_TYPE_SQL){//sql类型的获取sql传过来的值
			String itemValue = getDefaultSql();// 查询数据库中当前对应的值
			currentEditText.setText(itemValue);
			value = itemValue;
		}else{
			SubmitItem item=null;
			if(isLink){//超链接 查询临时表
				item=new SubmitItemTempDB(context).findSubmitItem(null, Submit.STATE_NO_SUBMIT, planId, wayId, storeId, targetid, targetType, String.valueOf(func.getFuncId()));
			}else{
				item=new SubmitItemDB(context).findSubmitItem(null, Submit.STATE_NO_SUBMIT, planId,wayId, storeId, targetid, targetType, String.valueOf(func.getFuncId()));
			}
			if(item!=null && replenishBundle==null){//不是查询条件，查询数据库中的值
				String itemValue = item.getParamValue();// 查询数据库中当前对应的值
				currentEditText.setText(itemValue);
				value = itemValue;
				JLog.d(TAG, "itemValue==>" + itemValue);
							
			}else{//如果数据库中没有该控件的值那么就获取默认值
				String defaultVaule=getDefaultVaiueByType(func.getDefaultType());
				if(!TextUtils.isEmpty(defaultVaule)){
					currentEditText.setText(defaultVaule);//设置默认值
					value=defaultVaule;
					JLog.d(TAG, "defaultVaule==>"+defaultVaule);
				}else{
					if(!TextUtils.isEmpty(func.getDefaultValue())){//如果没有默认值 就获取传过来的默认值
						currentEditText.setText(func.getDefaultValue());
						value=func.getDefaultValue();
						JLog.d(TAG, "func.getDefaultValue()==>"+func.getDefaultValue());
					}
				}
				if(replenishBundle!=null && replenishBundle.containsKey(func.getFuncId()+"")){//如果是查询条件从bundle中获取值
					currentEditText.setText(replenishBundle.getString(func.getFuncId()+""));
					value = replenishBundle.getString(func.getFuncId()+"");
					JLog.d(TAG, "itemValue==>" + value);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.gcg.grirms.comp.Component#setIsEnable(boolean)
	 */
	@Override
	public void setIsEnable(boolean isEnable) {
		currentEditText.setEnabled(isEnable);
	}
	
	/**
	 * 设置最大输入文本个数
	 * @param maxLength 个数
	 */
	private void setMaxLength(int maxLength) {
		currentEditText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(maxLength) });
	}
	
	
	/**
	 * 根据传入的默认类型设置默认值
	 */
	private String getDefaultVaiueByType(Integer type){
		if(type != null){
			Calendar c = Calendar.getInstance();
			switch (type) {
			case Func.DEFAULT_TYPE_YEAR://返回当前年
				return c.get(Calendar.YEAR)+"";
			case Func.DEFAULT_TYPE_MONTH://返回当前月
				return (c.get(Calendar.MONTH)+1)+"";
			case Func.DEFAULT_TYPE_DAY://返回当前日
				return (c.get(Calendar.DAY_OF_MONTH))+"";
			case Func.DEFAULT_TYPE_DATE://返回当前日期包含时间
				return DateUtil.getCurDateTime();
			case Func.DEFAULT_TYPE_DATE_NO_TIME://返回当前日期不包含时间
				return DateUtil.getCurDate();
			case Func.DEFAULT_TYPE_TIME://返回当前时间
				return c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND);
			case Func.DEFAULT_TYPE_HOURS://返回当前小时
				return c.get(Calendar.HOUR_OF_DAY)+"";
			case Func.DEFAULT_TYPE_MINUTE://返回当前分钟
				return c.get(Calendar.MINUTE)+"";
			case Func.DEFAULT_TYPE_USER://返回当前用户
				return SharedPreferencesUtil.getInstance(context.getApplicationContext()).getUserName();
			case Func.DEFAULT_TYPE_JOB_NUMBER://唯一工号
				return System.currentTimeMillis()+"";
			}
		}
		return "";
	}
	
	/**
	 * 获取携带查询条件的bundle
	 * @return
	 */
	public Bundle getReplenishBundle() {
		return replenishBundle;
	}
	/**
	 * 设置查询条件的bundle
	 * @param replenishBundle bundle
	 */
	public void setReplenishBundle(Bundle replenishBundle) {
		this.replenishBundle = replenishBundle;
	}
	
	/**
	 * @return sql查询出的值
	 */
	public String getDefaultSql() {
		return defaultSql;
	}
	/**
	 * @param defaultSql sql查询的值
	 */
	public void setDefaultSql(String defaultSql) {
		this.defaultSql = defaultSql;
	}
	/**
	 * 
	 * @return 当前输入框的值
	 */
	public EditText getCurrentEditText() {
		return currentEditText;
	}
}
