package com.yunhu.yhshxc.comp;

import android.content.Context;
import android.graphics.Color;
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
import com.yunhu.yhshxc.activity.ChangeModuleFuncActivity;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.FuncControl;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.bo.Module;
import com.yunhu.yhshxc.bo.ReportWhere;
import com.yunhu.yhshxc.bo.Submit;
import com.yunhu.yhshxc.bo.SubmitItem;
import com.yunhu.yhshxc.database.FuncControlDB;
import com.yunhu.yhshxc.database.FuncDB;
import com.yunhu.yhshxc.database.SubmitDB;
import com.yunhu.yhshxc.database.SubmitItemDB;
import com.yunhu.yhshxc.database.SubmitItemTempDB;
import com.yunhu.yhshxc.database.VisitFuncDB;
import com.yunhu.yhshxc.exception.DataComputeException;
import com.yunhu.yhshxc.func.AbsFuncActivity;
import com.yunhu.yhshxc.module.replenish.QueryFuncActivity;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.ToastUtil;
import com.yunhu.yhshxc.widget.ToastOrder;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gcg.org.debug.JLog;

/**
 * 输入框类型的控件
 * @author jishen
 *
 */
public class EditCompFunc extends Component{
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
//       TextInputLayout textInputLayout;
       
       private SubmitItemDB submitItemDB;
   	private SubmitItemTempDB linkSubmitItemTempDB;
   	private SubmitDB submitDB;
   	public Module module;//模块实例
   	public Bundle replenish;//查询条件
	private boolean isReplenish=false;//是否是查询 true是 false不是
	private boolean isReport=false;//是否是报表 true是 false不是
	public ReportWhere reportWhere;//报表查询条件实例
	private boolean isTableHistory=false;//是否是表格历史数据
	private AbsFuncActivity absFuncActivity;
	private int menuId;
	private int menuType;
	private String menuName;
    private boolean isChangeActivity=true;
	public int getMenuId() {
		return menuId;
	}
	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}
	public int getMenuType() {
		return menuType;
	}
	public void setMenuType(int menuType) {
		this.menuType = menuType;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	List<String> contrlEnterList = null;
	public EditCompFunc(Context context, Func func,Bundle bundle,List<String> contrlEnterList) {
		this.context = context;
		this.func = func;
		this.type = func.getType();
		this.param = func.getFuncId();
		this.compFunc=func;
		this.contrlEnterList=contrlEnterList;
		if(compDialog!=null){//compDialog是空是表示是非表格中的
			this.putData(compDialog.type, compDialog.wayId, compDialog.planId, compDialog.storeId, compDialog.targetid, compDialog.targetType);
		}
		view = View.inflate(context, R.layout.edit_comp, null);
		currentEditText = (EditText) view.findViewById(R.id.textEditTextComp);
//		textInputLayout = (TextInputLayout) view.findViewById(R.id.usernameWrapper);
//		currentEditText.addTextChangedListener(watcher);
		submitItemDB = new SubmitItemDB(context);
		submitDB = new SubmitDB(context);
		linkSubmitItemTempDB=new SubmitItemTempDB(context);
		isTableHistory=bundle.getBoolean("isTableHistory");
		isReport=bundle.getBoolean("isReport");
		if(!isTableHistory && !isReport){//不是表格历史查询    不是报表的话强转 
			absFuncActivity=(AbsFuncActivity)context;
			isChangeActivity = !(absFuncActivity instanceof ChangeModuleFuncActivity);
			isReplenish=absFuncActivity instanceof QueryFuncActivity;//是否是查询
			if(isReplenish){//如果是查询的情况
				QueryFuncActivity queryFuncActivity=(QueryFuncActivity)context;
				replenish=queryFuncActivity.replenish;
			}
		}
		init(bundle);
	}
	public EditText getEditText(){
		return currentEditText;
	}
	public void setTargetType(int targetType){
		this.targetType = targetType;
	}
	private void init(Bundle bundle) {
		wayId = bundle.getInt("wayId");//线路ID
		planId = bundle.getInt("planId");//计划ID
		storeId = bundle.getInt("storeId");//店面ID
		storeName = bundle.getString("storeName");//店面名称
		wayName = bundle.getString("wayName");//线路名称
		// 赋值
		param = func.getFuncId();//控件ID
		targetid = func.getTargetid();//数据ID
		targetType=bundle.getInt("targetType");//模块类型
		menuType = bundle.getInt("menuType");
		module=(Module) bundle.getSerializable("module");//模块实例
		
		//超链接临时存储表添加 jishen 2013/2/25
		isLink=bundle.getBoolean("isLink");//是否是超链接 true 是 false否
		
	}
//	private TextWatcher watcher = new TextWatcher() {
//		
//		@Override
//		public void onTextChanged(CharSequence s, int start, int before, int count) {
//			
//		}
//		
//		@Override
//		public void beforeTextChanged(CharSequence s, int start, int count,
//				int after) {
//			
//		}
//		
//		@Override
//		public void afterTextChanged(Editable s) {
//			completeConfrim(s.toString());
//		}
//	};
	public void completeConfrim(String value){
		if(func!=null){
			
			func.setvalue(value);
			
				if (checkType() && checkSubmit(func,value)) {//先验证类型
					
						if(!isReplenish){
							save();//将值存进数据库
							setContrlViewFunc(value);
						}else{
							saveReplenish(func,value);//数据查询的时候保存查询不保存数据库
						}
					controlOrgAndNext();//控制联动					
				}
				
		}
		
	}



	private void setContrlViewFunc(String value){

		if(contrlEnterList!=null){
			if(contrlEnterList.contains(String.valueOf(func.getFuncId()))){
				PublicUtils.TypeValue typeValue = null;
				try {
					typeValue = PublicUtils.getValueBytype(context,func,value,2);
				} catch (DataComputeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if(null!=typeValue)
					absFuncActivity.setContrlView(typeValue, String.valueOf(func.getFuncId()));
			}
		}
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
//			currentEditText.setText(value);
		if(!TextUtils.isEmpty(value)){
			currentEditText.setText(value);
		}else{
			currentEditText.setHint(context.getResources().getString(R.string.input_content));
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
//				currentEditText.setSingleLine(false);
//				currentEditText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
				if(func.getLength() != null){
					setMaxLength(func.getLength());
				}
				break;
			default:
//				currentEditText.setSingleLine(false);
//				currentEditText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
				if(func.getLength() != null){
					setMaxLength(func.getLength());
				}
				break;
			}
		}else{
//			currentEditText.setSingleLine(false);
//			currentEditText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
			if(func.getLength() != null){
				setMaxLength(func.getLength());
			}
		}
        
		initData();// 初始化数据
		currentEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				EditCompFunc.this.value = s.toString();
				Log.d(TAG, EditCompFunc.this.value);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				completeConfrim(s.toString());
			}
		});
		currentEditText.setSelection(currentEditText.getText().toString().length());
		return view;
	}

	/*
	 * 初始化数据
	 */
	private void initData() {
		
		if(func.getDefaultType() != null && func.getDefaultType()==Func.DEFAULT_TYPE_SQL){//sql类型的获取sql传过来的值
			String itemValue = getDefaultSql();// 查询数据库中当前对应的值
//			currentEditText.setText(itemValue);
			if(!TextUtils.isEmpty(itemValue)){
				currentEditText.setText(itemValue);
			}else{
				currentEditText.setHint(context.getResources().getString(R.string.input_content));
			}
			
			
			
			value = itemValue;
		}else{
			SubmitItem item=null;
			if(absFuncActivity.isLinkActivity){//超链接 查询临时表
				item=new SubmitItemTempDB(context).findSubmitItem(null, Submit.STATE_NO_SUBMIT, planId, wayId, storeId, targetid, targetType, String.valueOf(func.getFuncId()));
			}else{
				item=new SubmitItemDB(context).findSubmitItem(null, Submit.STATE_NO_SUBMIT, planId,wayId, storeId, targetid, targetType, String.valueOf(func.getFuncId()));
			}
			if(item!=null && replenishBundle==null){//不是查询条件，查询数据库中的值
				String itemValue = item.getParamValue();// 查询数据库中当前对应的值
//				currentEditText.setText(itemValue);
				if(!TextUtils.isEmpty(itemValue)){
					currentEditText.setText(itemValue);
				}else{
					currentEditText.setHint(context.getResources().getString(R.string.input_content));
				}
				
				value = itemValue;
				JLog.d(TAG, "itemValue==>" + itemValue);
							
			}else{//如果数据库中没有该控件的值那么就获取默认值
				String defaultVaule=getDefaultVaiueByType(func.getDefaultType());
				if(!TextUtils.isEmpty(defaultVaule)){
//					currentEditText.setText(defaultVaule);//设置默认值
					currentEditText.setHint(defaultVaule);//设置默认值
					
					value=defaultVaule;
					JLog.d(TAG, "defaultVaule==>"+defaultVaule);
				}else{
					if(!TextUtils.isEmpty(func.getDefaultValue())){//如果没有默认值 就获取传过来的默认值
//						currentEditText.setText(func.getDefaultValue());
						currentEditText.setHint(func.getDefaultValue());
						
						value=func.getDefaultValue();
						JLog.d(TAG, "func.getDefaultValue()==>"+func.getDefaultValue());
					}
				}
				if(replenishBundle!=null && replenishBundle.containsKey(func.getFuncId()+"")){//如果是查询条件从bundle中获取值
					currentEditText.setText(replenishBundle.getString(func.getFuncId()+""));
//					currentEditText.setHint(replenishBundle.getString(func.getFuncId()+""));
					
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
	
	/**
	 * 输入框类型检测输入的内容是否匹配
	 * DataType 控制键盘  checkType控制验证
	 * @return 匹配返回true 不匹配返回false
	 */
	public boolean checkType(){
		boolean flag = true;
		if(!TextUtils.isEmpty(value) && func.getCheckType() != null){
			
			switch(func.getCheckType()){
			case Func.CHECK_TYPE_FIXED_TELEPHONE:
				flag = isFixedTelephone();
				if (!flag) {
					ToastUtil.showText(context, context.getResources().getString(R.string.compdialog_message1));
				}
				break;
			case Func.CHECK_TYPE_IDNUMBER:
				flag = isIDNumber();
				if (!flag) {
					ToastUtil.showText(context, context.getResources().getString(R.string.compdialog_message2));
				}
				break;
			case Func.CHECK_TYPE_MOBILE_TELEPHONE:
				flag = isMobileTelephone();
				if (!flag) {
					ToastUtil.showText(context, context.getResources().getString(R.string.compdialog_message1));
				}
				break;
			case Func.CHECK_TYPE_MAIL:
				flag = isMail();
				if (!flag) {
					ToastUtil.showText(context, context.getResources().getString(R.string.compdialog_message3));
				}
				break;
			case Func.CHECK_TYPE_ZIPCODE:
				flag = isZipCode();
				if (!flag) {
					ToastUtil.showText(context, context.getResources().getString(R.string.compdialog_message4));
				}
				break;
			case Func.CHECK_TYPE_NUMERIC:
				JLog.d(TAG, "checkType<---->"+value);
				if(isReplenish){
					if(func.getDataType()==Func.DATATYPE_DECIMAL){//如果是小数
						flag = isNumeric(value);
						if (!flag) {
							ToastUtil.showText(context, context.getResources().getString(R.string.compdialog_message5));
						}
					} else {
						flag = isInteger(value);
						if (!flag) {
							ToastUtil.showText(context, context.getResources().getString(R.string.compdialog_message6));
						}
					}
				}else{
					if(func.getDataType()==Func.DATATYPE_DECIMAL){//如果是小数
						flag = isNumeric();
						if (!flag) {
//							ToastUtil.showText(context, "请输入正确数字!");
							currentEditText.setTextColor(Color.RED);
						}else{
							currentEditText.setTextColor(Color.parseColor("#808080"));
						}
					} else {
						flag = isInteger();
						if (!flag) {
							ToastUtil.showText(context, context.getResources().getString(R.string.compdialog_message6));
						}
					}
				}
				
				
				break;
			case Func.CHECK_TYPE_NET:
				
				break;
			}
		}
		return flag;
	}
	
	/*
	 * 验证数字
	 */
	public boolean isNumeric(String value) {
//		Pattern pattern = Pattern.compile("\\d+");
		Pattern pattern = Pattern.compile("[0-9]+\\.[0-9]+~@@[0-9]+\\.[0-9]+");
		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
	}
	/*
	 * 验证整数
	 */
	public boolean isInteger(String value) {
		Pattern pattern = Pattern.compile("\\d+~@@\\d+");
		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
	}
	
	/**
	 * 验证提交的次数 一定时间内 一个控件不能反复提交同一个值
	 * @param func 当前控件
	 * @param value 当前控件的值
	 * @return 是否可以提交
	 */
	private boolean checkSubmit(Func func,String value){
		boolean flag=true;
		if(func.getDayNumber()!=null&&func.getOperateNumber()!=null){
			FuncControlDB funcControlDB=new FuncControlDB(context);
			funcControlDB.removeFuncControlByDate(func.getDayNumber(),func.getTargetid(),func.getFuncId());//删除范围以外的
			flag=funcControlDB.isCanSubmit(func.getTargetid(), func.getFuncId(), func.getOperateNumber(), value);
			if(flag){
				if(func.getType()!=Func.TYPE_BUTTON && 
				   func.getType()!=Func.TYPE_CAMERA &&
				   func.getType()!=Func.TYPE_CAMERA_HEIGHT &&
				   func.getType()!=Func.TYPE_CAMERA_MIDDLE &&
				   func.getType()!=Func.TYPE_CAMERA_CUSTOM &&
				   func.getType()!=Func.TYPE_CHECKIN&&
				   func.getType()!=Func.TYPE_LINK &&
				   func.getType()!=Func.TYPE_LOCATION &&
				   func.getType()!=Func.TYPE_TABLECOMP &&
				   func.getType()!=Func.TYPE_SINGLE_CHOICE_FUZZY_QUERY_COMP &&
				   func.getType()!=Func.TYPE_MULTI_CHOICE_FUZZY_QUERY_COMP &&
				   func.getType()!=Func.TYPE_RANGEDATE &&
				   func.getType()!=Func.TYPE_RANGEEDIT &&
				   func.getType()!=Func.TYPE_HIDDEN){
					
					FuncControl control=new FuncControl();
					control.setFuncId(func.getFuncId());
					control.setTargetId(func.getTargetid());
					control.setUpdateDate(DateUtil.getCurDate());
					control.setValues(value);
					control.setSubmitState(0);//0表示未提交
					if(funcControlDB.isHas(control)){//如果已有
						if(TextUtils.isEmpty(value)){
							funcControlDB.removeFuncControlByFuncId(func.getFuncId());
						}else{
							funcControlDB.updateFuncControlValue(control.getFuncId(), control.getTargetId(), value);
						}
					}else{
						funcControlDB.insertFuncControl(control);
					}
				}
				
			}else{
				ToastOrder.makeText(context, context.getResources().getString(R.string.not_promite_commit), ToastOrder.LENGTH_LONG).show();
			}
		}
		return flag;
	}
	

	/*
	 * 保存操作数据
	 */
	public void save() {
		SubmitItem submitItem = new SubmitItem();
		submitItem.setTargetId(targetid+"");
		int submitId = 0;
		if(menuType == Menu.TYPE_NEARBY){
			 submitId = submitDB.selectSubmitIdNotCheckOut(planId, wayId, storeId,
					targetid, menuType,Submit.STATE_NO_SUBMIT);
		}else{
			 submitId = submitDB.selectSubmitIdNotCheckOut(planId, wayId, storeId,
					targetid, targetType,Submit.STATE_NO_SUBMIT);
		}
		if (submitId != Constants.DEFAULTINT) {
			submitItem.setParamName(String.valueOf(func.getFuncId()));
			submitItem.setParamValue(value);
			
			submitItem.setSubmitId(submitId);
			submitItem.setType(func.getType());
			submitItem.setOrderId(func.getId());
			submitItem.setIsCacheFun(func.getIsCacheFun());
			
			if (func.getOrgOption()!=null && func.getOrgOption()== Func.ORG_STORE) {
				submitItem.setNote(SubmitItem.NOTE_STORE);
			}
			boolean isHas=false;
			if(absFuncActivity.isLinkActivity){//超链接的时候操作临时表中的数据
				isHas = linkSubmitItemTempDB.findIsHaveParamName(submitItem.getParamName(), submitId);
			}else{
				isHas = submitItemDB.findIsHaveParamName(submitItem.getParamName(), submitId);
			}
			if (isHas) {// true表示已经操作过，此时更新数据库
				
				if (TextUtils.isEmpty(value)) {// 如果值是空就修改为空 修改的时候如果值取消就传给服务器一个空值，否则服务器不处理
					submitItem.setParamValue("");
				}
				
				if(absFuncActivity.isLinkActivity){//超链接
					linkSubmitItemTempDB.updateSubmitItem(submitItem);
				}else{
					submitItemDB.updateSubmitItem(submitItem,isChangeActivity);
				}
			} else {
				if (!TextUtils.isEmpty(value)) {// 没有该项，并且值不为空就插入
					if(absFuncActivity.isLinkActivity){
						linkSubmitItemTempDB.insertSubmitItem(submitItem);
					}else{
						submitItemDB.insertSubmitItem(submitItem,isChangeActivity);
					}
				}
			}

		} else {
			if(!TextUtils.isEmpty(value)){
				Submit submit = new Submit();
				submit.setPlanId(planId);
				submit.setState(Submit.STATE_NO_SUBMIT);
				submit.setStoreId(storeId);
				submit.setStoreName(storeName);
				submit.setWayName(wayName);
				submit.setWayId(wayId);
				submit.setTargetid(targetid);
				submit.setTargetType(targetType);
				if(module!=null){
					submit.setModType(module.getType());
				}
				submit.setMenuId(menuId);
				submit.setMenuType(menuType);
				submit.setMenuName(menuName);
				submitDB.insertSubmit(submit);
				int currentsubmitId = submitDB.selectSubmitIdNotCheckOut(planId, wayId,storeId, targetid, targetType,Submit.STATE_NO_SUBMIT);
				submitItem.setParamName(String.valueOf(func.getFuncId()));
				submitItem.setType(func.getType());
				submitItem.setParamValue(value);
				submitItem.setSubmitId(currentsubmitId);
				submitItem.setOrderId(func.getId());
				submitItem.setIsCacheFun(func.getIsCacheFun());
				if (func.getOrgOption()!=null && func.getOrgOption()== Func.ORG_STORE) {
					submitItem.setNote(SubmitItem.NOTE_STORE);
				}
				if(absFuncActivity.isLinkActivity){
					linkSubmitItemTempDB.insertSubmitItem(submitItem);
				}else{
					submitItemDB.insertSubmitItem(submitItem,isChangeActivity);
				}
			}
			
		}
		if(!isTableHistory && !isReport){//不是表格历史信息查询 不是报表查询
			if (TextUtils.isEmpty(value) && absFuncActivity!=null) {
				absFuncActivity.inputUnOperatedMap(func);
				absFuncActivity.setShowHook(false, absFuncActivity.hookView);
			} else {
				absFuncActivity.inputOperatedMap(func);
				absFuncActivity.setShowHook(false, absFuncActivity.hookView);
				if(selectedValue!=null&&!selectedValue.equals("")){
//					absFuncActivity.setValue(selectedValue, absFuncActivity.TView);
				}else{
				
//				absFuncActivity.setValue(value, absFuncActivity.TView);
				}
				
				
			}
		}
	}
	/*
	 * 验证其他下拉框
	 * 如果是数字返回true否则返回false
	 */
	public boolean checkOther(String value) {
		if(TextUtils.isEmpty(value)){
			return false;
		}
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
	}
	/**
	 * 存储查询条件的值
	 * @param func 当前控件
	 * @param value 当前控件的值
	 */
	private void saveReplenish(Func func,String value){
		JLog.d(TAG, "----->"+value);
		QueryFuncActivity queryFuncActivity=(QueryFuncActivity)context;
		
		if((func.getType()==Func.TYPE_SELECT_OTHER || func.getType()==Func.TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY_COMP) && !TextUtils.isEmpty(value) && !checkOther(value)){//值不是整数说明输入的是其他值
			queryFuncActivity.replenish.putString(func.getFuncId()+"_other", value);
			queryFuncActivity.replenish.putString(func.getFuncId()+"", "-1");
		}else{
			queryFuncActivity.replenish.putString(func.getFuncId()+"", value);
			if(queryFuncActivity.replenish.containsKey(func.getFuncId()+"_other")){
				queryFuncActivity.replenish.remove(func.getFuncId()+"_other");
			}
		}
		queryFuncActivity.inputOperatedMap(func);
		if(!TextUtils.isEmpty(replenish.getString(func.getFuncId()+""))){
			queryFuncActivity.setShowHook(true,absFuncActivity.hookView);
		}else{
			replenish.remove(func.getFuncId()+"");
			if(replenish.containsKey(func.getFuncId()+"_other")){
				replenish.remove(func.getFuncId()+"_other");
			}
			queryFuncActivity.inputUnOperatedMap(func);
			queryFuncActivity.setShowHook(false,absFuncActivity.hookView);
		}
	}
	
	/**
	 * 控制机构联动和外层控件联动
	 */
	private void controlOrgAndNext(){
		if(func.getType()==Func.TYPE_SELECTCOMP 
				|| func.getType()==Func.TYPE_SINGLE_CHOICE_FUZZY_QUERY_COMP 
				|| func.getType()==Func.TYPE_MULTI_CHOICE_FUZZY_QUERY_COMP 
				|| func.getType()==Func.TYPE_MULTI_CHOICE_SPINNER_COMP 
				|| func.getType()==Func.TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY_COMP 
				|| func.getType()==Func.TYPE_SELECT_OTHER){
			
			if(!TextUtils.isEmpty(value)){//值不是空的情况
				if(func.getOrgOption()!=null && func.getOrgOption()==Func.ORG_OPTION){//机构
					if(!checkOther(value) && func.getType()!=Func.TYPE_MULTI_CHOICE_FUZZY_QUERY_COMP && func.getType()!=Func.TYPE_MULTI_CHOICE_SPINNER_COMP){//如果选择了其他值
						getOrgMap().put(func.getOrgLevel(), "-1");
					}else{//没有选择其他值
						getOrgMap().put(func.getOrgLevel(),value);
					}
				
				}else{
					if(func.getNextDropdown() !=null && func.getNextDropdown() !=0){//有下级级联的时候
						Map<Integer,String> chooseMap = getChooseMap();
						cleanNextChoose(func.getNextDropdown());//操作上级的时候把下级的值清空
						//选择其他的时候要存储-1判断是否是其他是根据数值是否是数字，如果是数字则说明不是选择的其他，多选的时候不是数字，但是要存储多选条件
						if(chooseMap.get(func.getFuncId()) != null ){
							if(!checkOther(value) && func.getType()!=Func.TYPE_MULTI_CHOICE_FUZZY_QUERY_COMP && func.getType()!=Func.TYPE_MULTI_CHOICE_SPINNER_COMP){
								saveChooseMap(func.getNextDropdown(),chooseMap.get(func.getFuncId())+"@"+"-1");
							}else{
								saveChooseMap(func.getNextDropdown(),chooseMap.get(func.getFuncId())+"@"+value);
							}
						}else{
							if(!checkOther(value) && func.getType()!=Func.TYPE_MULTI_CHOICE_FUZZY_QUERY_COMP && func.getType()!=Func.TYPE_MULTI_CHOICE_SPINNER_COMP){
								saveChooseMap(func.getNextDropdown(),"-1");
							}else{
								saveChooseMap(func.getNextDropdown(),value);
							}
						}
					}
					if(func.getOrgOption() != null && func.getOrgOption() == Func.ORG_STORE){
						cleanOptionLocation(func);//清除联动的定位
					}
					updateChooseHook();
				}
				if(absFuncActivity!=null){
					absFuncActivity.inputOperatedMap(func);
				}
			}else{//值为空
				if(func.getOrgLevel()!=null){
					getOrgMap().put(func.getOrgLevel(), null);
				}
			}
			if(func.getOrgLevel()!=null && func.getOrgOption() != null && func.getOrgOption() == Func.ORG_OPTION){
				cleanOrgNext(func.getOrgLevel());//清除下层选择
			}
		}
	}
	private  void cleanOptionLocation(Func func){
		int submitId = new SubmitDB(context).selectSubmitIdNotCheckOut(planId, wayId, storeId,targetid, targetType,Submit.STATE_NO_SUBMIT);
		if(absFuncActivity.isLinkActivity){
			new SubmitItemTempDB(context).updateSubmitBySubmitIdAndNote(submitId, String.valueOf(func.getFuncId()));
		}else{
			new SubmitItemDB(context).updateSubmitBySubmitIdAndNote(submitId, String.valueOf(func.getFuncId()));
		}
	}
	/**
	 * 级联存map数据
	 * @param
	 * @param curNextFuncId
	 * @param value
	 */
	public void saveChooseMap(Integer curNextFuncId, String value) {
		setChooseMap(curNextFuncId, value);
		Func funcNext = null;
		if(targetType==Menu.TYPE_VISIT){
			funcNext = new VisitFuncDB(context).findFuncByFuncId(curNextFuncId);
		}else{
			funcNext = new FuncDB(context).findFuncByFuncId(curNextFuncId);
		}
		if(funcNext !=null && funcNext.getNextDropdown()!=null &&funcNext.getNextDropdown() !=0 ){
			saveChooseMap(funcNext.getNextDropdown(), value);
		}
	}
	/**
	 * 操作上级的时候把下级的值清除,一定要在设置值之前调用
	 * @param curNextFuncId
	 * @param
	 * @param
	 */
	public void cleanNextChoose(int curNextFuncId){
		
		
		Func func = null;
		if(targetType==Menu.TYPE_VISIT){
			func = new VisitFuncDB(context).findFuncByFuncId(curNextFuncId);
		}else{
			func = new FuncDB(context).findFuncByFuncId(curNextFuncId);
		}
		if(func !=null){
			getChooseMap().remove(func.getFuncId());
			if(absFuncActivity!=null){
				absFuncActivity.inputUnOperatedMap(func);
				if(absFuncActivity.isLinkActivity){
					SubmitItemTempDB submitItemTempDB=new SubmitItemTempDB(context);
//					submitItemTempDB.deleteSubmitItemByParamName(func.getFuncId()+"");
					submitItemTempDB.updateSubmitItemValue(String.valueOf(func.getFuncId()), "");
					submitItemTempDB.deleteSubmitItemByParamName(func.getFuncId()+"_other");
				}else{
//					submitItemDB.deleteSubmitItemByParamName(func.getFuncId()+"");
					submitItemDB.updateSubmitItemValue(String.valueOf(func.getFuncId()), "");
					submitItemDB.deleteSubmitItemByParamName(func.getFuncId()+"_other");
				}
			}
			if(isReplenish){
				QueryFuncActivity queryFuncActivity=(QueryFuncActivity)context;
				queryFuncActivity.inputUnOperatedMap(func);
				replenish.remove(func.getFuncId()+"");
				if(replenish.containsKey(func.getFuncId()+"_other")){
					replenish.remove(func.getFuncId()+"_other");
				}
			}
			if(func.getNextDropdown()!=null &&func.getNextDropdown() !=0 ){
				cleanNextChoose(func.getNextDropdown());
			}
		}
	}
	/**
	 * 
	 * @param currentLevel 当前操作的层级
	 *  操作上级的时候把下级的值清除
	 */
	public void cleanOrgNext(int currentLevel){
		Map<Integer, String> orgValue=getOrgMap();
		for (Iterator<Entry<Integer, String>> iterator = orgValue.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry<Integer, String> entry = (Map.Entry<Integer, String>) iterator.next();
			int key=entry.getKey();
			if(key>currentLevel){
				orgValue.put(key, null);
			}
			
		}
		if(absFuncActivity!=null){
			absFuncActivity.orgSetHook(isReplenish,replenish);
		}
	}
	
	/**
	 * 
	 * @return 机构层级关系的map 联动时候使用
	 */
	public Map<Integer, String> getOrgMap() {
		if(!isTableHistory && !isReport){
			return absFuncActivity.orgMap;
		}else{
			return new HashMap<Integer, String>();
		}
	}
	
	/**
	 * 
	 * @return 非机构的联动控件的层级关系map
 	 */
	public Map<Integer, String> getChooseMap() {
		if(!isTableHistory && !isReport){
			return absFuncActivity.chooseMap;
		}else{
			return new HashMap<Integer, String>();
		}
	}
	
	/**
	 * 保存非机构联动级联控件的关系
	 * @param key 下级的控件ID
	 * @param value 当前层级的值
	 */
	private void setChooseMap(Integer key,String value) {
		if(!isTableHistory && !isReport){
			absFuncActivity.saveChooseMap(key, value);
		}
	}
	
	/**
	 * 更新对勾显示
	 */
	private void updateChooseHook() {
		if(!isTableHistory && !isReport){
			absFuncActivity.setShowHook();
		}
		if (isReplenish) {
			QueryFuncActivity queryFuncActivity=(QueryFuncActivity)context;
			queryFuncActivity.setShowHook();
		}
	}

	/**
	 * 
	 * @return 将非机构级联控件的层级关系存到bundle里
	 */
	private Bundle getChooseMapBundle(){
		Bundle bundle=new Bundle();
		for (Map.Entry<Integer, String> m : getChooseMap().entrySet()) {
			bundle.putString(m.getKey()+"", m.getValue());
		}
		return bundle;
	}
	/**
	 * 
	 * @return  将机构级联控件的层级关系存到bundle里
	 */
	private Bundle getOrgMapBundle(){
		Bundle bundle=new Bundle();
		for (Map.Entry<Integer, String> m : getOrgMap().entrySet()) {
			bundle.putString(m.getKey()+"", m.getValue());
		}
		return bundle;
	}

}
