package com.yunhu.yhshxc.comp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.ChangeModuleFuncActivity;
import com.yunhu.yhshxc.activity.TableCompActivity;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.FuncControl;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.bo.Module;
import com.yunhu.yhshxc.bo.ReportWhere;
import com.yunhu.yhshxc.bo.Submit;
import com.yunhu.yhshxc.bo.SubmitItem;
import com.yunhu.yhshxc.comp.spinner.FuzzyMultiChoiceSpinnerComp;
import com.yunhu.yhshxc.comp.spinner.FuzzySingleChoiceSpinnerComp;
import com.yunhu.yhshxc.comp.spinner.MultiChoiceSpinnerComp;
import com.yunhu.yhshxc.comp.spinner.OnMuktiChiceSelecteLister;
import com.yunhu.yhshxc.comp.spinner.SingleChoiceSpinnerComp;
import com.yunhu.yhshxc.comp.spinner.SingleSpinnerComp;
import com.yunhu.yhshxc.database.FuncCacheDB;
import com.yunhu.yhshxc.database.FuncControlDB;
import com.yunhu.yhshxc.database.FuncDB;
import com.yunhu.yhshxc.database.SubmitDB;
import com.yunhu.yhshxc.database.SubmitItemDB;
import com.yunhu.yhshxc.database.SubmitItemTempDB;
import com.yunhu.yhshxc.database.VisitFuncDB;
import com.yunhu.yhshxc.exception.DataComputeException;
import com.yunhu.yhshxc.func.AbsFuncActivity;
import com.yunhu.yhshxc.listener.ProductCodeListener;
import com.yunhu.yhshxc.module.replenish.QueryFuncActivity;
import com.yunhu.yhshxc.report.ReportActivity;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.PublicUtils.TypeValue;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gcg.org.debug.JLog;

/**
 * 操作控件弹出的dialog
 * 
 * @author jishen
 *
 */
public class CompDialog extends Component implements OnClickListener {

	private String TAG = "CompDialog";
	private Context context;
	private View view;
	private Button confirmBtn, cancelBtn;// 确定按钮 取消按钮
	private LinearLayout linearLayout;
	private Func func;// 当前func
	public Dialog dialog;// 当前dialog
	private Component currentComponent;// 当前组件
	private SubmitItemDB submitItemDB;
	private SubmitItemTempDB linkSubmitItemTempDB;
	private SubmitDB submitDB;
	public ReportActivity reportActivity;// 报表
	public Bundle replenish;// 查询条件
	private boolean isReplenish = false;// 是否是查询 true是 false不是
	private boolean isReport = false;// 是否是报表 true是 false不是
	public Module module;// 模块实例
	public ReportWhere reportWhere;// 报表查询条件实例
	private boolean isTableHistory = false;// 是否是表格历史数据
	private String defaultSql;// 分销标准SQL数据
	private Bundle bundle;
	private AbsFuncActivity absFuncActivity;
	private ProductCode productCode;// 串码
	private ScanInputCode scanInputCode;// 可输入类型的扫描控件
	private ProductCodeListener productCodeListener;
	public Double c_lon, c_lat;// 当前位置经纬度，机构店面距离筛选使用
	private int menuId;
	private int menuType;
	private String menuName;
	private boolean isChangeActivity = true;//默认属于修改,不往FunCacheDb插入
    public boolean isHaveValue = true;//用于判断单选是否有值
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

	private View v;

	public CompDialog(Context context, Func func, Bundle bundle) {
		this.context = context;
		this.func = func;
		this.bundle = bundle;
		isTableHistory = bundle.getBoolean("isTableHistory");
		isReport = bundle.getBoolean("isReport");
		if (!isTableHistory && !isReport) {// 不是表格历史查询 不是报表的话强转
			absFuncActivity = (AbsFuncActivity) context;
			isChangeActivity= !(absFuncActivity instanceof ChangeModuleFuncActivity);//是否是修改
			isReplenish = absFuncActivity instanceof QueryFuncActivity;// 是否是查询
			if (isReplenish) {// 如果是查询的情况
				QueryFuncActivity queryFuncActivity = (QueryFuncActivity) context;
				replenish = queryFuncActivity.replenish;
			}
		}
		init(bundle);
	}

	public CompDialog(Context context, Func func, View v, Bundle bundle,List<String> controlEnterList) {
		this.context = context;
		this.func = func;
		this.bundle = bundle;
		this.v = v;
		this.controlEnterList = controlEnterList;
		isTableHistory = bundle.getBoolean("isTableHistory");
		isReport = bundle.getBoolean("isReport");
		if (!isTableHistory && !isReport) {// 不是表格历史查询 不是报表的话强转
			absFuncActivity = (AbsFuncActivity) context;
			isChangeActivity= !(absFuncActivity instanceof ChangeModuleFuncActivity);//是否是修改
			isReplenish = absFuncActivity instanceof QueryFuncActivity;// 是否是查询
			if (isReplenish) {// 如果是查询的情况
				QueryFuncActivity queryFuncActivity = (QueryFuncActivity) context;
				replenish = queryFuncActivity.replenish;
			}
		}
		init(bundle);
	}

List<String> controlEnterList = null;
	public CompDialog(Context context, Func func, Bundle bundle,List<String> controlEnterList) {
		this.context = context;
		this.func = func;
		this.bundle=bundle;
		this.controlEnterList=controlEnterList;
		isTableHistory=bundle.getBoolean("isTableHistory");
		isReport=bundle.getBoolean("isReport");
		if(!isTableHistory && !isReport){//不是表格历史查询    不是报表的话强转 
			absFuncActivity=(AbsFuncActivity)context;
			isReplenish=absFuncActivity instanceof QueryFuncActivity;//是否是查询
			if(isReplenish){//如果是查询的情况
				QueryFuncActivity queryFuncActivity=(QueryFuncActivity)context;
				replenish=queryFuncActivity.replenish;
			}
		}
		init(bundle);
	}




							

	/*
	 * 初始化
	 */
	private void init(Bundle bundle) {
		submitItemDB = new SubmitItemDB(context);
		submitDB = new SubmitDB(context);
		view = View.inflate(context, R.layout.comp_dialog, null);
		linearLayout = (LinearLayout) view.findViewById(R.id.ll_compDialog);
		confirmBtn = (Button) view.findViewById(R.id.comp_dialog_confirmBtn);// 保存按钮
		cancelBtn = (Button) view.findViewById(R.id.comp_dialog_cancelBtn);// 取消按钮
		confirmBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
		CompDialog.this.wayId = bundle.getInt("wayId");// 线路ID
		CompDialog.this.planId = bundle.getInt("planId");// 计划ID
		CompDialog.this.storeId = bundle.getInt("storeId");// 店面ID
		CompDialog.this.storeName = bundle.getString("storeName");// 店面名称
		CompDialog.this.wayName = bundle.getString("wayName");// 线路名称
		// 赋值
		CompDialog.this.param = func.getFuncId();// 控件ID
		CompDialog.this.targetid = func.getTargetid();// 数据ID
		CompDialog.this.targetType = bundle.getInt("targetType");// 模块类型
		module = (Module) bundle.getSerializable("module");// 模块实例

		// 超链接临时存储表添加 jishen 2013/2/25
		isLink = bundle.getBoolean("isLink");// 是否是超链接 true 是 false否
		linkSubmitItemTempDB = new SubmitItemTempDB(context);
	}

	@Override
	public View getObject() {
		switch (func.getType()) {
		case Func.TYPE_EDITCOMP:// 输入框
			if (absFuncActivity instanceof QueryFuncActivity
					&& (func.getDataType() == Func.DATATYPE_BIG_INTEGER || func.getDataType() == Func.DATATYPE_DECIMAL
							|| func.getDataType() == Func.DATATYPE_SMALL_INTEGER)) {// 如果是查询条件就弹出有范围的输入框
				EditRangeComp editRangeComp = new EditRangeComp(context, func);
				editRangeComp.isLink = isLink;
				editRangeComp.setReplenishBundle(replenish);
				currentComponent = editRangeComp;
				linearLayout.addView(currentComponent.getObject());
			} else {// 普通输入框
				EditComp editComp = new EditComp(context, func, CompDialog.this);
				editComp.isLink = isLink;
				editComp.setReplenishBundle(replenish);
				editComp.setDefaultSql(defaultSql);
				View view = editComp.getObject();
				currentComponent = editComp;
				linearLayout.addView(view);

			}
			break;
		case Func.TYPE_SELECTCOMP:// 下拉框
			final SingleSpinnerComp spinnerComp = new SingleSpinnerComp(context, func, getChooseMap().get(func.getFuncId()),
					getOrgMapBundle(), this, new OnMuktiChiceSelecteLister() {

						@Override
						public void onMuktiConfirm() {

							completeConfrim();
							if (!isHaveValue&&func.getType()==Func.TYPE_SELECTCOMP){//没有选中值
								if (!isLink){
									SubmitItemDB itemDB = new SubmitItemDB(context);
									SubmitItem sub1 =itemDB.findSubmitItemByFuncId(func.getFuncId());
									if (sub1!=null){
										itemDB.deleteSubmitItem(sub1);
									}
								} else{
									SubmitItemTempDB tempDB = new SubmitItemTempDB(context);
									SubmitItem sub2 = tempDB.findSubmitItemByParamName(func.getFuncId()+"");
									if (sub2!=null) {
										tempDB.deleteSubmitItem(sub2);
									}
								}
								  if (currentComponent!=null){
									  currentComponent.value="";
									  currentComponent.dataName="";
									  currentComponent.selectedValue="";
								  }

									if (absFuncActivity!=null){
										clearNextDropDown(func.getFuncId());
//										func.setvalue("");
//										HashMap<Integer, View> allFuncLayout = absFuncActivity.getAllFuncLayout();
//										 View view1 = allFuncLayout.get(func.getFuncId());
//										if (view1!=null){
//											TextView tvContent = (TextView) view1.findViewById(R.id.tv_func_content);
//											 if (tvContent!=null){
//												 tvContent.setText("");
//											 }
//										}

									}
								isHaveValue=true;
								updateChooseHook();
							}



						}
					});
			spinnerComp.setCompDialog(this);
			spinnerComp.currentLat = c_lat;
			spinnerComp.currentLon = c_lon;
			spinnerComp.isLink = isLink;
			spinnerComp.setOrgLevelMap(getOrgMap());// 设置层级map
			spinnerComp.setDefaultSql(defaultSql);// 设置sql初始化数据
			spinnerComp.setInitData();// 设置初始化数据
			currentComponent = spinnerComp;
			linearLayout.addView(currentComponent.getObject());
			spinnerComp.spinner.performClick();
			break;
		case Func.TYPE_MULTI_CHOICE_SPINNER_COMP:// 多选下拉框
			MultiChoiceSpinnerComp multiChoiceSpinnerComp = new MultiChoiceSpinnerComp(context, func,
					getChooseMap().get(func.getFuncId()), getOrgMapBundle(), this, new OnMuktiChiceSelecteLister() {

						@Override
						public void onMuktiConfirm() {
							completeConfrim();
						}
					});
			multiChoiceSpinnerComp.currentLat = c_lat;
			multiChoiceSpinnerComp.currentLon = c_lon;
			multiChoiceSpinnerComp.isLink = isLink;
			if (func.getFuncId() == -8968 || func.getTargetid() == -2 || func.getFuncId() == -2907) {// 考勤查询的时候func.getTargetid()==-2//func.getFuncId()==-2907表示是报表中的多选
				multiChoiceSpinnerComp.setMenuType(this.targetType);
				multiChoiceSpinnerComp.setMultichoiceUser(true);
				multiChoiceSpinnerComp.setModule(module);
			}
			multiChoiceSpinnerComp.setDefaultSql(defaultSql);
			multiChoiceSpinnerComp.setOrgLevelMap(getOrgMap());
			multiChoiceSpinnerComp.setInitData();
			currentComponent = multiChoiceSpinnerComp;
			linearLayout.addView(currentComponent.getObject());
			multiChoiceSpinnerComp.multiChoiceSpinner.performClick();
			break;
		case Func.TYPE_SINGLE_CHOICE_FUZZY_QUERY_COMP:// 单选模糊搜索下拉框
			FuzzySingleChoiceSpinnerComp fuzzySingleChoiceSpinnerComp = new FuzzySingleChoiceSpinnerComp(context, func,
					getChooseMap().get(func.getFuncId()), getOrgMapBundle(), this, new OnMuktiChiceSelecteLister() {

						@Override
						public void onMuktiConfirm() {
							// TODO Auto-generated method stub
							completeConfrim();

						}
					});
			fuzzySingleChoiceSpinnerComp.currentLat = c_lat;
			fuzzySingleChoiceSpinnerComp.currentLon = c_lon;
			fuzzySingleChoiceSpinnerComp.isLink = isLink;
			fuzzySingleChoiceSpinnerComp.setOrgLevelMap(getOrgMap());
			fuzzySingleChoiceSpinnerComp.setDefaultSql(defaultSql);
			fuzzySingleChoiceSpinnerComp.setInitData();
			currentComponent = fuzzySingleChoiceSpinnerComp;
			linearLayout.addView(currentComponent.getObject());
			fuzzySingleChoiceSpinnerComp.singleChoiceFuzzyQuerySpinner.performClick();
			break;
		case Func.TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY_COMP:// 单选模糊搜索其他类型下拉框
			FuzzySingleChoiceSpinnerComp fuzzySingleChoiceOtherSpinnerComp = new FuzzySingleChoiceSpinnerComp(context,
					func, getChooseMap().get(func.getFuncId()), getOrgMapBundle(), this,
					new OnMuktiChiceSelecteLister() {

						@Override
						public void onMuktiConfirm() {
							// TODO Auto-generated method stub
							completeConfrim();
						}
					});
			fuzzySingleChoiceOtherSpinnerComp.currentLat = c_lat;
			fuzzySingleChoiceOtherSpinnerComp.currentLon = c_lon;
			fuzzySingleChoiceOtherSpinnerComp.isLink = isLink;
			fuzzySingleChoiceOtherSpinnerComp.setOrgLevelMap(getOrgMap());
			fuzzySingleChoiceOtherSpinnerComp.setDefaultSql(defaultSql);
			fuzzySingleChoiceOtherSpinnerComp.setInitData();
			currentComponent = fuzzySingleChoiceOtherSpinnerComp;
			linearLayout.addView(currentComponent.getObject());
			fuzzySingleChoiceOtherSpinnerComp.singleChoiceFuzzyQuerySpinner.performClick();
			break;
		case Func.TYPE_MULTI_CHOICE_FUZZY_QUERY_COMP:// 多选模糊搜索下拉框
			FuzzyMultiChoiceSpinnerComp fuzzyMultiChoiceSpinnerComp = new FuzzyMultiChoiceSpinnerComp(context, func,
					getChooseMap().get(func.getFuncId()), getOrgMapBundle(), this, new OnMuktiChiceSelecteLister() {

						@Override
						public void onMuktiConfirm() {
							// TODO Auto-generated method stub
							completeConfrim();
						}
					});
			fuzzyMultiChoiceSpinnerComp.currentLat = c_lat;
			fuzzyMultiChoiceSpinnerComp.currentLon = c_lon;
			fuzzyMultiChoiceSpinnerComp.isLink = isLink;
			if (func.getFuncId() == -8968 || func.getTargetid() == -2 || func.getFuncId() == -2907) {// 考勤查询的时候func.getTargetid()==-2//func.getFuncId()==-2907表示是报表中的多选
				fuzzyMultiChoiceSpinnerComp.setMultichoiceUser(true);
				fuzzyMultiChoiceSpinnerComp.setModule(module);
			}
			fuzzyMultiChoiceSpinnerComp.setDefaultSql(defaultSql);
			fuzzyMultiChoiceSpinnerComp.setOrgLevelMap(getOrgMap());
			fuzzyMultiChoiceSpinnerComp.setInitData();
			currentComponent = fuzzyMultiChoiceSpinnerComp;
			linearLayout.addView(currentComponent.getObject());
			fuzzyMultiChoiceSpinnerComp.multiChoiceFuzzyQuerySpinner.performClick();
			break;
		case Func.TYPE_DATEPICKERCOMP:// 日期
			if (isReplenish) {// 如果是查询
				currentComponent = new DatePickerRangeComp(context, func, CompDialog.this);
				linearLayout.addView(currentComponent.getObject());
			} else {
				currentComponent = new DatePickerComp(context, func, CompDialog.this);
				linearLayout.addView(currentComponent.getObject());
			}

			break;
		case Func.TYPE_TIMEPICKERCOMP:// 时间
			if (isReplenish) {
				currentComponent = new TimeRangeComp(context, func, CompDialog.this);
				linearLayout.addView(currentComponent.getObject());
			} else {
				currentComponent = new TimePickerComp(context, func, CompDialog.this);
				linearLayout.addView(currentComponent.getObject());
			}
			break;
		case Func.TYPE_TEXTCOMP:// 文本标签
			TextComp textComp = new TextComp(context, func, CompDialog.this);
			textComp.isLink = isLink;
			textComp.setDefaultSql(defaultSql);
			currentComponent = textComp;
			linearLayout.addView(currentComponent.getObject());
			break;
		case Func.TYPE_PRODUCT_CODE:// 串码
			productCode = new ProductCode(context, func, this);
			productCode.isLink = isLink;
			if (targetType == com.yunhu.yhshxc.bo.Menu.TYPE_VISIT) {
				productCode.setProductTableType(ProductCode.TABLE_TYPE_VISIT);
			} else {
				productCode.setProductTableType(ProductCode.TABLE_TYPE_MODULE);
			}
			productCode.setProductCodeListener(productCodeListener);
			currentComponent = productCode;
			linearLayout.addView(currentComponent.getObject());
			break;
		case Func.TYPE_SCAN_INPUT:// 扫描
			scanInputCode = new ScanInputCode(context, func, this);
			scanInputCode.isLink = isLink;
			if (targetType == com.yunhu.yhshxc.bo.Menu.TYPE_VISIT) {
				scanInputCode.setProductTableType(ProductCode.TABLE_TYPE_VISIT);
			} else {
				scanInputCode.setProductTableType(ProductCode.TABLE_TYPE_MODULE);
			}
			scanInputCode.setProductCodeListener(productCodeListener);
			currentComponent = scanInputCode;
			linearLayout.addView(currentComponent.getObject());
			break;
		case Func.TYPE_TABLECOMP:// 表格
			Intent intent = new Intent(context, TableCompActivity.class);
			if (isTableHistory) {// 表格历史信息查询
				bundle.putBoolean("isTableHistory", true);
			} else {
				bundle.putBoolean("isTableHistory", false);
				if (func.getDefaultType() != null && func.getDefaultType() == Func.DEFAULT_TYPE_SQL) {// 表格sql获取
					bundle.putString("defaultSql", defaultSql);
				} else {
					bundle.putString("defaultSql", "");
				}
			}
			bundle.putSerializable("func", func);
			if (module != null) {
				bundle.putInt("modType", module.getType());
			}
			bundle.putInt("menuId", menuId);
			// bundle.putInt("menuType", menuType);
			bundle.putString("menuName", menuName);
			intent.putExtra("tableBundle", bundle);
			intent.putExtra("chooseMapBundle", getChooseMapBundle());
			intent.putExtra("orgMapBundle", getOrgMapBundle());
			intent.putExtra("isNoWait", bundle.getBoolean("bundle"));
			context.startActivity(intent);

			break;
		case Func.TYPE_CHECKBOXCOMP:
			currentComponent = new CheckboxComp(context, func, CompDialog.this);
			linearLayout.addView(currentComponent.getObject());
			break;
		case Func.TYPE_RADIOBTNCOMP:
			currentComponent = new RadiobtnComp(context, func);
			linearLayout.addView(currentComponent.getObject());
			break;
		case Func.TYPE_SELECT_OTHER:// 其他类型的下拉框
			SingleChoiceSpinnerComp singleChoiceSpinnerComp = new SingleChoiceSpinnerComp(context, func,
					getChooseMap().get(func.getFuncId()), getOrgMapBundle(), this, new OnMuktiChiceSelecteLister() {

						@Override
						public void onMuktiConfirm() {
							// TODO Auto-generated method stub
							completeConfrim();
						}
					});
			singleChoiceSpinnerComp.currentLat = c_lat;
			singleChoiceSpinnerComp.currentLon = c_lon;
			singleChoiceSpinnerComp.isLink = isLink;
			singleChoiceSpinnerComp.setOrgLevelMap(getOrgMap());
			singleChoiceSpinnerComp.setDefaultSql(defaultSql);
			singleChoiceSpinnerComp.setInitData();
			currentComponent = singleChoiceSpinnerComp;
			linearLayout.addView(currentComponent.getObject());
			singleChoiceSpinnerComp.singleChoiceSpinner.performClick();
			break;
		case Func.TYPE_HIDDEN:// 隐藏域下拉选择
			Integer defaultType = func.getDefaultType();
			if (defaultType != null) {
				switch (defaultType) {
				case Func.DEFAULT_TYPE_SELECT:// 隐藏域单选下拉框
					SingleSpinnerComp hiddenSpinnerComp = new SingleSpinnerComp(context, func,
							getChooseMap().get(func.getFuncId()), getOrgMapBundle(), this,
							new OnMuktiChiceSelecteLister() {

								@Override
								public void onMuktiConfirm() {
									completeConfrim();
								}
							});
					hiddenSpinnerComp.isLink = isLink;
					hiddenSpinnerComp.setDefaultSql(defaultSql);
					hiddenSpinnerComp.setOrgLevelMap(getOrgMap());
					hiddenSpinnerComp.setInitData();
					currentComponent = hiddenSpinnerComp;
					linearLayout.addView(currentComponent.getObject());
					hiddenSpinnerComp.spinner.performClick();
					break;
				case Func.DEFAULT_TYPE_MULTI_SELECT:// 隐藏域多选下拉框
					MultiChoiceSpinnerComp multiHiddenChoiceSpinnerComp = new MultiChoiceSpinnerComp(context, func,
							getChooseMap().get(func.getFuncId()), getOrgMapBundle(), this,
							new OnMuktiChiceSelecteLister() {

								@Override
								public void onMuktiConfirm() {
									completeConfrim();
								}
							});
					multiHiddenChoiceSpinnerComp.isLink = isLink;
					multiHiddenChoiceSpinnerComp.setOrgLevelMap(getOrgMap());
					multiHiddenChoiceSpinnerComp.setDefaultSql(defaultSql);
					multiHiddenChoiceSpinnerComp.setInitData();
					currentComponent = multiHiddenChoiceSpinnerComp;
					linearLayout.addView(currentComponent.getObject());
					break;
				case Func.TYPE_MULTI_CHOICE_FUZZY_QUERY_COMP:// 多选模糊搜索
					FuzzyMultiChoiceSpinnerComp fuzzyMultiHiddenChoiceSpinnerComp = new FuzzyMultiChoiceSpinnerComp(
							context, func, getChooseMap().get(func.getFuncId()), getOrgMapBundle(), this,
							new OnMuktiChiceSelecteLister() {

								@Override
								public void onMuktiConfirm() {
									// TODO Auto-generated method stub
									completeConfrim();
								}
							});
					fuzzyMultiHiddenChoiceSpinnerComp.isLink = isLink;
					fuzzyMultiHiddenChoiceSpinnerComp.setOrgLevelMap(getOrgMap());
					fuzzyMultiHiddenChoiceSpinnerComp.setDefaultSql(defaultSql);
					fuzzyMultiHiddenChoiceSpinnerComp.setInitData();
					currentComponent = fuzzyMultiHiddenChoiceSpinnerComp;
					linearLayout.addView(currentComponent.getObject());
					fuzzyMultiHiddenChoiceSpinnerComp.multiChoiceFuzzyQuerySpinner.performClick();
					break;
				case Func.TYPE_SINGLE_CHOICE_FUZZY_QUERY_COMP:// 单选模糊搜索
					FuzzySingleChoiceSpinnerComp fuzzySingleHiddenChoiceSpinnerComp = new FuzzySingleChoiceSpinnerComp(
							context, func, getChooseMap().get(func.getFuncId()), getOrgMapBundle(), this,
							new OnMuktiChiceSelecteLister() {

								@Override
								public void onMuktiConfirm() {
									// TODO Auto-generated method stub
									completeConfrim();
								}
							});
					fuzzySingleHiddenChoiceSpinnerComp.isLink = isLink;
					fuzzySingleHiddenChoiceSpinnerComp.setOrgLevelMap(getOrgMap());
					fuzzySingleHiddenChoiceSpinnerComp.setDefaultSql(defaultSql);
					fuzzySingleHiddenChoiceSpinnerComp.setInitData();
					currentComponent = fuzzySingleHiddenChoiceSpinnerComp;
					linearLayout.addView(currentComponent.getObject());
					fuzzySingleHiddenChoiceSpinnerComp.singleChoiceFuzzyQuerySpinner.performClick();
					break;

				default:
					break;
				}
			}
			break;
		default:
			break;
		}

		return view;
	}

	/**
	 * @return dialog
	 */
	public Dialog createDialog() {
		dialog = new Dialog(context, R.style.transparentDialog);
		dialog.setContentView(getObject());
		dialog.setCancelable(false);
		return dialog;
	}

	@Override
	public void setIsEnable(boolean isEnable) {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.comp_dialog_confirmBtn:// 确定按钮
			func.setvalue(currentComponent.value);

			// currentComponent.
			if (func.getType() == Func.TYPE_PRODUCT_CODE) {
				saveProductCode();
			} else if (func.getType() == Func.TYPE_SCAN_INPUT) {
				saveScanInputCode();
			} else {
				if (checkType() && checkSubmit(func, currentComponent.value)) {// 先验证类型
					if ((func.getType() == Func.TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY_COMP
							|| func.getType() == Func.TYPE_SELECT_OTHER) && !isReplenish) {// 存储其他类型的控件值并且不是查询的时候
						saveOther();// 保存其他类型的控件的值
					} else if (func.getType() == Func.TYPE_MULTI_CHOICE_SPINNER_COMP && func.getFuncId() == -8968
							&& !isReplenish) {// 存储用户
						saveUser();// 存储用户
					} else if (isReport && func.getFuncId() == -2907) {// 报表查询条件保存
						reportActivity.saveReport(reportWhere, currentComponent.value);
					} else {
						if (!isReplenish) {
							save();// 将值存进数据库
							setContrlViewFunc(currentComponent.value);
						} else {
							saveReplenish(func, currentComponent.value);// 数据查询的时候保存查询不保存数据库
							// 刷新当前页面
							if (context instanceof QueryFuncActivity) {
								QueryFuncActivity activity = (QueryFuncActivity) context;
								activity.setShowHook();
							}
						}
					}
					controlOrgAndNext();// 控制联动
				}
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
			}
			if (compFunc != null && compFunc.getIsCacheFun() == 1) {
				FuncCacheDB fcDb = new FuncCacheDB(context);
				fcDb.updateValueToCache(compFunc, dataName);

			}
			break;
		case R.id.comp_dialog_cancelBtn:
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}
			break;

		default:
			break;
		}

	}

	private void saveProductCode() {
		// 串码
		if (productCode != null) {
			if (TextUtils.isEmpty(currentComponent.value)) {
				ToastOrder.makeText(context, context.getResources().getString(R.string.please_imput_chuancode), ToastOrder.LENGTH_SHORT).show();
			} else {
				if (productCode.isNeedNet()) {
					productCode.getProductCodeInfo(func, currentComponent.value);
				} else {
					if (productCode.isCodeUpdate() && "1".equals(func.getCodeControl())) {
						SharedPreferencesUtil.getInstance(context).setCompareContent("content_" + func.getTargetid(),
								"");
					}
					save();
				}
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
			}
		}

	}

	/**
	 * 保存可输入类型的扫描控件
	 */
	private void saveScanInputCode() {
		if (scanInputCode != null) {
			if (TextUtils.isEmpty(currentComponent.value)) {
				ToastOrder.makeText(context, context.getResources().getString(R.string.please_imput_saocode), ToastOrder.LENGTH_SHORT).show();
			} else {
				save();
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
			}
		}
	}

	/**
	 * 存储查询条件的值
	 * 
	 * @param func
	 *            当前控件
	 * @param value
	 *            当前控件的值
	 */
	private void saveReplenish(Func func, String value) {
		JLog.d(TAG, "----->" + currentComponent.value);
		QueryFuncActivity queryFuncActivity = (QueryFuncActivity) context;

		if ((func.getType() == Func.TYPE_SELECT_OTHER
				|| func.getType() == Func.TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY_COMP) && !TextUtils.isEmpty(value)
				&& !checkOther(value)) {// 值不是整数说明输入的是其他值
			queryFuncActivity.replenish.putString(func.getFuncId() + "_other", currentComponent.value);
			queryFuncActivity.replenish.putString(func.getFuncId() + "", "-1");
		} else {
			queryFuncActivity.replenish.putString(func.getFuncId() + "", currentComponent.value);
			if (queryFuncActivity.replenish.containsKey(func.getFuncId() + "_other")) {
				queryFuncActivity.replenish.remove(func.getFuncId() + "_other");
			}
		}
		queryFuncActivity.inputOperatedMap(func);
		if (!TextUtils.isEmpty(replenish.getString(func.getFuncId() + ""))) {
			queryFuncActivity.setShowHook(true, absFuncActivity.hookView);
		} else {
			replenish.remove(func.getFuncId() + "");
			if (replenish.containsKey(func.getFuncId() + "_other")) {
				replenish.remove(func.getFuncId() + "_other");
			}
			queryFuncActivity.inputUnOperatedMap(func);
			queryFuncActivity.setShowHook(false, absFuncActivity.hookView);
		}
	}

	/**
	 * 控制机构联动和外层控件联动
	 */
	private void controlOrgAndNext() {
		if (func.getType() == Func.TYPE_SELECTCOMP || func.getType() == Func.TYPE_SINGLE_CHOICE_FUZZY_QUERY_COMP
				|| func.getType() == Func.TYPE_MULTI_CHOICE_FUZZY_QUERY_COMP
				|| func.getType() == Func.TYPE_MULTI_CHOICE_SPINNER_COMP
				|| func.getType() == Func.TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY_COMP
				|| func.getType() == Func.TYPE_SELECT_OTHER) {

			if (!TextUtils.isEmpty(currentComponent.value)) {// 值不是空的情况
				if (func.getOrgOption() != null && func.getOrgOption() == Func.ORG_OPTION) {// 机构
					if (!checkOther(currentComponent.value) && func.getType() != Func.TYPE_MULTI_CHOICE_FUZZY_QUERY_COMP
							&& func.getType() != Func.TYPE_MULTI_CHOICE_SPINNER_COMP) {// 如果选择了其他值
						getOrgMap().put(func.getOrgLevel(), "-1");
					} else {// 没有选择其他值
						getOrgMap().put(func.getOrgLevel(), currentComponent.value);
					}

				} else {
					if (func.getNextDropdown() != null && func.getNextDropdown() != 0) {// 有下级级联的时候
						Map<Integer, String> chooseMap = getChooseMap();
						cleanNextChoose(func.getNextDropdown());// 操作上级的时候把下级的值清空
						// 选择其他的时候要存储-1判断是否是其他是根据数值是否是数字，如果是数字则说明不是选择的其他，多选的时候不是数字，但是要存储多选条件
						if (chooseMap.get(func.getFuncId()) != null) {
							if (!checkOther(currentComponent.value)
									&& func.getType() != Func.TYPE_MULTI_CHOICE_FUZZY_QUERY_COMP
									&& func.getType() != Func.TYPE_MULTI_CHOICE_SPINNER_COMP) {
								saveChooseMap(func.getNextDropdown(), chooseMap.get(func.getFuncId()) + "@" + "-1");
							} else {
								saveChooseMap(func.getNextDropdown(),
										chooseMap.get(func.getFuncId()) + "@" + currentComponent.value);
							}
						} else {
							if (!checkOther(currentComponent.value)
									&& func.getType() != Func.TYPE_MULTI_CHOICE_FUZZY_QUERY_COMP
									&& func.getType() != Func.TYPE_MULTI_CHOICE_SPINNER_COMP) {
								saveChooseMap(func.getNextDropdown(), "-1");
							} else {
								saveChooseMap(func.getNextDropdown(), currentComponent.value);
							}
						}
					}
					if (func.getOrgOption() != null && func.getOrgOption() == Func.ORG_STORE) {
						cleanOptionLocation(func);// 清除联动的定位
					}
					updateChooseHook();
				}
				if (absFuncActivity != null) {
					absFuncActivity.inputOperatedMap(func);
				}
			} else {// 值为空
				if (func.getOrgLevel() != null) {
					getOrgMap().put(func.getOrgLevel(), null);
				}else if(func.getNextDropdown() != null && func.getNextDropdown() != 0){
					Map<Integer, String> chooseMap = getChooseMap();
					cleanNextChoose(func.getNextDropdown());// 操作上级的时候把下级的值清空
					// 选择其他的时候要存储-1判断是否是其他是根据数值是否是数字，如果是数字则说明不是选择的其他，多选的时候不是数字，但是要存储多选条件
					if (chooseMap.get(func.getFuncId()) != null) {
						if (!checkOther(currentComponent.value)
								&& func.getType() != Func.TYPE_MULTI_CHOICE_FUZZY_QUERY_COMP
								&& func.getType() != Func.TYPE_MULTI_CHOICE_SPINNER_COMP) {
							saveChooseMap(func.getNextDropdown(), chooseMap.get(func.getFuncId()) + "@" + "-1");
						} else {
							saveChooseMap(func.getNextDropdown(),
									chooseMap.get(func.getFuncId()) + "@" + currentComponent.value);
						}
					} else {
						if (!checkOther(currentComponent.value)
								&& func.getType() != Func.TYPE_MULTI_CHOICE_FUZZY_QUERY_COMP
								&& func.getType() != Func.TYPE_MULTI_CHOICE_SPINNER_COMP) {
							saveChooseMap(func.getNextDropdown(), "-1");
						} else {
							saveChooseMap(func.getNextDropdown(), currentComponent.value);
						}
					}
					updateChooseHook();
				}
			}
			if (func.getOrgLevel() != null && func.getOrgOption() != null && func.getOrgOption() == Func.ORG_OPTION) {
				cleanOrgNext(func.getOrgLevel());// 清除下层选择
				updateChooseHook();
			}

		}
	}

	private void cleanOptionLocation(Func func) {
		int submitId = new SubmitDB(context).selectSubmitIdNotCheckOut(planId, wayId, storeId, targetid, targetType,
				Submit.STATE_NO_SUBMIT);
		if (isLink) {
			new SubmitItemTempDB(context).updateSubmitBySubmitIdAndNote(submitId, String.valueOf(func.getFuncId()));
		} else {
			new SubmitItemDB(context).updateSubmitBySubmitIdAndNote(submitId, String.valueOf(func.getFuncId()));
		}
	}

	/**
	 * 输入框类型检测输入的内容是否匹配 DataType 控制键盘 checkType控制验证
	 * 
	 * @return 匹配返回true 不匹配返回false
	 */
	public boolean checkType() {
		boolean flag = true;
		if (!TextUtils.isEmpty(currentComponent.value) && func.getCheckType() != null) {

			switch (func.getCheckType()) {
			case Func.CHECK_TYPE_FIXED_TELEPHONE:
				flag = currentComponent.isFixedTelephone();
				if (!flag) {
					ToastOrder.makeText(context, context.getResources().getString(R.string.compdialog_message1), ToastOrder.LENGTH_LONG).show();
				}
				break;
			case Func.CHECK_TYPE_IDNUMBER:
				flag = currentComponent.isIDNumber();
				if (!flag) {
					ToastOrder.makeText(context, context.getResources().getString(R.string.compdialog_message2), ToastOrder.LENGTH_LONG).show();
				}
				break;
			case Func.CHECK_TYPE_MOBILE_TELEPHONE:
				flag = currentComponent.isMobileTelephone();
				if (!flag) {
					ToastOrder.makeText(context, context.getResources().getString(R.string.compdialog_message1), ToastOrder.LENGTH_LONG).show();
				}
				break;
			case Func.CHECK_TYPE_MAIL:
				flag = currentComponent.isMail();
				if (!flag) {
					ToastOrder.makeText(context, context.getResources().getString(R.string.compdialog_message3), ToastOrder.LENGTH_LONG).show();
				}
				break;
			case Func.CHECK_TYPE_ZIPCODE:
				flag = currentComponent.isZipCode();
				if (!flag) {
					ToastOrder.makeText(context, context.getResources().getString(R.string.compdialog_message4), ToastOrder.LENGTH_LONG).show();
				}
				break;
			case Func.CHECK_TYPE_NUMERIC:
				JLog.d(TAG, "checkType<---->" + currentComponent.value);
				if (isReplenish) {
					if (func.getDataType() == Func.DATATYPE_DECIMAL) {// 如果是小数
						flag = isNumeric(currentComponent.value);
						if (!flag) {
							ToastOrder.makeText(context, context.getResources().getString(R.string.compdialog_message5), ToastOrder.LENGTH_LONG).show();
						}
					} else {
						flag = isInteger(currentComponent.value);
						if (!flag) {
							ToastOrder.makeText(context, context.getResources().getString(R.string.compdialog_message6), ToastOrder.LENGTH_LONG).show();
						}
					}
				} else {
					if (func.getDataType() == Func.DATATYPE_DECIMAL) {// 如果是小数
						flag = currentComponent.isNumeric();
						if (!flag) {
							ToastOrder.makeText(context, context.getResources().getString(R.string.compdialog_message7), ToastOrder.LENGTH_LONG).show();
						}
					} else {
						flag = currentComponent.isInteger();
						if (!flag) {
							ToastOrder.makeText(context, context.getResources().getString(R.string.compdialog_message6), ToastOrder.LENGTH_LONG).show();
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
		// Pattern pattern = Pattern.compile("\\d+");
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

	/*
	 * 保存操作数据
	 */
	public void save() {
		SubmitItem submitItem = new SubmitItem();

		int submitId = submitDB.selectSubmitIdNotCheckOut(planId, wayId, storeId, targetid, targetType,
				Submit.STATE_NO_SUBMIT);
		submitItem.setTargetId(targetid + "");
		submitItem.setParamStr(currentComponent.dataName);

		if (submitId != Constants.DEFAULTINT) {
			submitItem.setParamName(String.valueOf(func.getFuncId()));
			submitItem.setParamValue(currentComponent.value);
			submitItem.setSubmitId(submitId);
			submitItem.setType(func.getType());
			submitItem.setOrderId(func.getId());
			submitItem.setIsCacheFun(func.getIsCacheFun());
			Submit sub = submitDB.findSubmitById(submitId);
			sub.setMenuId(menuId);
			sub.setMenuType(menuType);
			sub.setMenuName(menuName);
			submitDB.updateSubmit(sub);
			if (func.getOrgOption() != null && func.getOrgOption() == Func.ORG_STORE) {
				submitItem.setNote(SubmitItem.NOTE_STORE);
			}
			boolean isHas = false;
			if (isLink) {// 超链接的时候操作临时表中的数据
				isHas = linkSubmitItemTempDB.findIsHaveParamName(submitItem.getParamName(), submitId);
			} else {
				isHas = submitItemDB.findIsHaveParamName(submitItem.getParamName(), submitId);
			}
			if (isHas) {// true表示已经操作过，此时更新数据库

				if (TextUtils.isEmpty(currentComponent.value)) {// 如果值是空就修改为空
																// 修改的时候如果值取消就传给服务器一个空值，否则服务器不处理
					submitItem.setParamValue("");
				}

				if (isLink) {// 超链接
					linkSubmitItemTempDB.updateSubmitItem(submitItem);
				} else {
					submitItemDB.updateSubmitItem(submitItem,isChangeActivity);
				}
			} else {
				if (!TextUtils.isEmpty(currentComponent.value)) {// 没有该项，并且值不为空就插入
					if (isLink) {
						linkSubmitItemTempDB.insertSubmitItem(submitItem);
					} else {
						submitItemDB.insertSubmitItem(submitItem,isChangeActivity);
					}
				}
			}

		} else {
			if (!TextUtils.isEmpty(currentComponent.value)) {
				Submit submit = new Submit();
				submit.setPlanId(planId);
				submit.setState(Submit.STATE_NO_SUBMIT);
				submit.setStoreId(storeId);
				submit.setStoreName(storeName);
				submit.setWayName(wayName);
				submit.setWayId(wayId);
				submit.setTargetid(targetid);
				submit.setTargetType(targetType);
				if (module != null) {
					submit.setModType(module.getType());
				}
				submit.setMenuId(menuId);
				submit.setMenuType(menuType);
				submit.setMenuName(menuName);
				submitDB.insertSubmit(submit);
				int currentsubmitId = submitDB.selectSubmitIdNotCheckOut(planId, wayId, storeId, targetid, targetType,
						Submit.STATE_NO_SUBMIT);
				submitItem.setParamName(String.valueOf(func.getFuncId()));
				submitItem.setType(func.getType());
				submitItem.setParamValue(currentComponent.value);
				submitItem.setSubmitId(currentsubmitId);
				submitItem.setOrderId(func.getId());
				submitItem.setIsCacheFun(func.getIsCacheFun());
				if (func.getOrgOption() != null && func.getOrgOption() == Func.ORG_STORE) {
					submitItem.setNote(SubmitItem.NOTE_STORE);
				}
				if (isLink) {
					linkSubmitItemTempDB.insertSubmitItem(submitItem);
				} else {
					submitItemDB.insertSubmitItem(submitItem,isChangeActivity);
				}
			}

		}
		if (!isTableHistory && !isReport) {// 不是表格历史信息查询 不是报表查询
			if (TextUtils.isEmpty(currentComponent.value) && absFuncActivity != null) {
				absFuncActivity.inputUnOperatedMap(func);
				absFuncActivity.setShowHook(false, absFuncActivity.hookView);
				absFuncActivity.setValue(currentComponent.selectedValue, absFuncActivity.TView);
			} else {
				absFuncActivity.inputOperatedMap(func);
				absFuncActivity.setShowHook(false, absFuncActivity.hookView);
				if (currentComponent.selectedValue != null && !currentComponent.selectedValue.equals("")) {
					absFuncActivity.setValue(currentComponent.selectedValue, absFuncActivity.TView);
				} else {
                    if (func.getDefaultType() != null && func.getDefaultType() == Func.DEFAULT_TYPE_SQL&&func.getType()==Func.TYPE_TEXTCOMP) {
                    	if (currentComponent.value.endsWith(".jpg")||currentComponent.value.endsWith(".jpeg")||currentComponent.value.endsWith("png")) {
                    		if (v!=null) {
                    			try{
                    				ImageView ivs = (ImageView) v.findViewById(R.id.iv_func_photo);
                    				
                    				ivs.setVisibility(View.VISIBLE);
        							ImageLoader.getInstance().displayImage(currentComponent.value, ivs);
        							absFuncActivity.TView.setVisibility(View.GONE);
                    			}catch(Exception e){
                    				e.printStackTrace();
                    			}
    						
    						}
						}else{
							absFuncActivity.setValue(!TextUtils.isEmpty(currentComponent.dataName) ? currentComponent.dataName
									: currentComponent.value, absFuncActivity.TView);	
						}
						
					}else{
						absFuncActivity.setValue(!TextUtils.isEmpty(currentComponent.dataName) ? currentComponent.dataName
								: currentComponent.value, absFuncActivity.TView);	
					}


				}

			}
		}
	}

	/**
	 * 存储用户的值 存在submit里
	 */
	private void saveUser() {

		int submitId = submitDB.selectSubmitIdNotCheckOut(planId, wayId, storeId, targetid, targetType,
				Submit.STATE_NO_SUBMIT);

		if (submitId != Constants.DEFAULTINT) {
			if (!TextUtils.isEmpty(currentComponent.value)) {
				Submit item = submitDB.findSubmitById(submitId);
				item.setSendUserId(currentComponent.value);
				submitDB.updateSubmit(item);
			} else {
				submitDB.deleteSubmitById(submitId);
			}

		} else {
			if (!TextUtils.isEmpty(currentComponent.value)) {
				Submit submit = new Submit();
				submit.setPlanId(planId);
				submit.setState(Submit.STATE_NO_SUBMIT);
				submit.setStoreId(storeId);
				submit.setStoreName(storeName);
				submit.setWayName(wayName);
				submit.setWayId(wayId);
				submit.setTargetid(targetid);
				submit.setTargetType(targetType);
				submit.setSendUserId(currentComponent.value);
				if (module != null) {
					submit.setModType(module.getType());
				}
				submit.setMenuId(menuId);
				submit.setMenuType(menuType);
				submit.setMenuName(menuName);
				submitDB.insertSubmit(submit);
			}
		}
		if (!isTableHistory && !isReport) {// 不是表格历史查询 不是报表查询
			if (TextUtils.isEmpty(currentComponent.value) && absFuncActivity != null) {
				absFuncActivity.inputUnOperatedMap(func);
				absFuncActivity.setShowHook(false, absFuncActivity.hookView);
				absFuncActivity.title_finish_iv.setVisibility(View.GONE);
			} else {
				absFuncActivity.inputOperatedMap(func);
				absFuncActivity.setShowHook(true, absFuncActivity.hookView);
				absFuncActivity.title_finish_iv.setVisibility(View.VISIBLE);
			}
		}
	}

	/**
	 * 存储其他类型的控件
	 */
	private void saveOther() {
		int submitId = submitDB.selectSubmitIdNotCheckOut(planId, wayId, storeId, targetid, targetType,
				Submit.STATE_NO_SUBMIT);

		SubmitItem spinnerItem = new SubmitItem();
		SubmitItem editItem = new SubmitItem();
		spinnerItem.setTargetId(targetid + "");
		editItem.setTargetId(targetid + "");
		editItem.setParamStr(currentComponent.dataName);
		spinnerItem.setParamStr(currentComponent.dataName);
		spinnerItem.setIsCacheFun(func.getIsCacheFun());
		editItem.setIsCacheFun(func.getIsCacheFun());

		if (checkOther(currentComponent.value)) {
			boolean flag = false;
			if (isLink) {// 如果是超链接的话查询临时表
				flag = linkSubmitItemTempDB.findIsHaveParamName(func.getFuncId() + "_other", submitId);// 插入前先检测有没有操作该项，如果有的话先删除，适用于先选其他后改正常
			} else {
				flag = submitItemDB.findIsHaveParamName(func.getFuncId() + "_other", submitId);// 插入前先检测有没有操作该项，如果有的话先删除，适用于先选其他后改正常
			}
			if (flag) {
				spinnerItem.setSubmitId(submitId);
				spinnerItem.setParamName(func.getFuncId() + "_other");
				if (isLink) {// 如果是超链的话就删除临时表中的数据
					linkSubmitItemTempDB.deleteSubmitItem(spinnerItem);
				} else {
					submitItemDB.deleteSubmitItem(spinnerItem);
				}
			}
			save();
		} else {

			if (submitId != Constants.DEFAULTINT) {
				spinnerItem.setParamName(String.valueOf(func.getFuncId()));
				spinnerItem.setParamValue("-1");
				spinnerItem.setSubmitId(submitId);
				spinnerItem.setType(func.getType());

				editItem.setParamName(func.getFuncId() + "_other");
				editItem.setParamValue(currentComponent.value);
				editItem.setSubmitId(submitId);
				editItem.setType(func.getType());
				editItem.setOrderId(func.getId());
				boolean isHas = false;
				boolean flag = false;
				if (isLink) {// 如果是超链接的话就查询临时表中的数据
					isHas = linkSubmitItemTempDB.findIsHaveParamName(editItem.getParamName(), submitId);
					flag = linkSubmitItemTempDB.findIsHaveParamName(spinnerItem.getParamName(), submitId);// 插入前先检测有没有操作该项，如果有的话先删除，适用于先选正常后改其他
				} else {
					isHas = submitItemDB.findIsHaveParamName(editItem.getParamName(), submitId);
					flag = submitItemDB.findIsHaveParamName(spinnerItem.getParamName(), submitId);// 插入前先检测有没有操作该项，如果有的话先删除，适用于先选正常后改其他
				}
				if (isHas && flag) {// true表示已经操作过，此时更新数据库
					if (TextUtils.isEmpty(currentComponent.value)) {// 如果值是空就删除该项
						// jishen 2013、02、25修改 超链接情况保存临时表中
						List<SubmitItem> list = null;
						if (isLink) {// 超链接的情况操作临时表
							linkSubmitItemTempDB.deleteSubmitItem(spinnerItem);
							linkSubmitItemTempDB.deleteSubmitItem(editItem);
							list = linkSubmitItemTempDB.findSubmitItemBySubmitId(submitId);
						} else {
							submitItemDB.deleteSubmitItem(spinnerItem);
							submitItemDB.deleteSubmitItem(editItem);
							list = submitItemDB.findSubmitItemBySubmitId(submitId);
						}
						if (list != null && !list.isEmpty()) {
							submitDB.deleteSubmitById(submitId);
						}

					} else {
						if (isLink) {
							linkSubmitItemTempDB.updateSubmitItem(spinnerItem);
							linkSubmitItemTempDB.updateSubmitItem(editItem);
						} else {
							submitItemDB.updateSubmitItem(spinnerItem,isChangeActivity);
							submitItemDB.updateSubmitItem(editItem,isChangeActivity);
						}
					}
				} else {
					if (flag) {
						if (isLink) {
							linkSubmitItemTempDB.deleteSubmitItem(spinnerItem);
						} else {
							submitItemDB.deleteSubmitItem(spinnerItem);
						}
					}
					if (!TextUtils.isEmpty(currentComponent.value)) {// 数据库中没有该项，并且值不为空就插入

						if (isLink) {
							linkSubmitItemTempDB.insertSubmitItem(spinnerItem);
							linkSubmitItemTempDB.insertSubmitItem(editItem);
						} else {
							submitItemDB.insertSubmitItem(spinnerItem,isChangeActivity);
							submitItemDB.insertSubmitItem(editItem,isChangeActivity);
						}
					}
				}

			} else {
				if (!TextUtils.isEmpty(currentComponent.value)) {
					Submit submit = new Submit();
					submit.setPlanId(planId);
					submit.setState(Submit.STATE_NO_SUBMIT);
					submit.setStoreId(storeId);
					submit.setStoreName(storeName);
					submit.setWayName(wayName);
					submit.setWayId(wayId);
					submit.setTargetid(targetid);
					submit.setTargetType(targetType);
					if (module != null) {
						submit.setModType(module.getType());
					}
					submit.setMenuId(menuId);
					submit.setMenuType(menuType);
					submit.setMenuName(menuName);
					submitDB.insertSubmit(submit);
					int currentsubmitId = submitDB.selectSubmitIdNotCheckOut(planId, wayId, storeId, targetid,
							targetType, Submit.STATE_NO_SUBMIT);
					spinnerItem.setParamName(String.valueOf(func.getFuncId()));
					spinnerItem.setParamValue("-1");
					spinnerItem.setSubmitId(currentsubmitId);
					spinnerItem.setType(func.getType());
					spinnerItem.setOrderId(func.getId());
					editItem.setParamName(func.getFuncId() + "_other");
					editItem.setParamValue(currentComponent.value);
					editItem.setSubmitId(currentsubmitId);
					editItem.setType(func.getType());
					editItem.setOrderId(func.getId());
					if (isLink) {// 超链接的情况操作临时表中的数据
						linkSubmitItemTempDB.insertSubmitItem(spinnerItem);
						linkSubmitItemTempDB.insertSubmitItem(editItem);
					} else {
						submitItemDB.insertSubmitItem(spinnerItem,isChangeActivity);
						submitItemDB.insertSubmitItem(editItem,isChangeActivity);
					}
				}

			}
			if (!isTableHistory && !isReport) {// 不是表格历史信息查询 不是报表查询
				if (TextUtils.isEmpty(currentComponent.value) && absFuncActivity != null) {
					absFuncActivity.inputUnOperatedMap(func);
					absFuncActivity.setShowHook(false, absFuncActivity.hookView);
					if (absFuncActivity.title_finish_iv != null) {
						absFuncActivity.title_finish_iv.setVisibility(View.GONE);
					}
				} else {
					absFuncActivity.inputOperatedMap(func);
					absFuncActivity.setShowHook(true, absFuncActivity.hookView);
					absFuncActivity.setValue(currentComponent.value, absFuncActivity.TView);
					if (absFuncActivity.title_finish_iv != null) {
						absFuncActivity.title_finish_iv.setVisibility(View.VISIBLE);
					}

				}
			}

		}

	}

	/**
	 * 操作上级的时候把下级的值清除,一定要在设置值之前调用
	 * 
	 * @param curNextFuncId
	 */
	public void cleanNextChoose(int curNextFuncId) {

		Func func = null;
		if (targetType == Menu.TYPE_VISIT) {
			func = new VisitFuncDB(context).findFuncByFuncId(curNextFuncId);
		} else {
			func = new FuncDB(context).findFuncByFuncId(curNextFuncId);
		}
		if (func != null) {
			getChooseMap().remove(func.getFuncId());
			if (absFuncActivity != null) {
				absFuncActivity.inputUnOperatedMap(func);
				if (isLink) {
					SubmitItemTempDB submitItemTempDB = new SubmitItemTempDB(context);
					// submitItemTempDB.deleteSubmitItemByParamName(func.getFuncId()+"");
					submitItemTempDB.updateSubmitItemValue(String.valueOf(func.getFuncId()), "");
					submitItemTempDB.deleteSubmitItemByParamName(func.getFuncId() + "_other");
				} else {
					// submitItemDB.deleteSubmitItemByParamName(func.getFuncId()+"");
					submitItemDB.updateSubmitItemValue(String.valueOf(func.getFuncId()), "");
					submitItemDB.deleteSubmitItemByParamName(func.getFuncId() + "_other");
				}
			}
			if (isReplenish) {
				QueryFuncActivity queryFuncActivity = (QueryFuncActivity) context;
				queryFuncActivity.inputUnOperatedMap(func);
				replenish.remove(func.getFuncId() + "");
				if (replenish.containsKey(func.getFuncId() + "_other")) {
					replenish.remove(func.getFuncId() + "_other");
				}
			}
			if (func.getNextDropdown() != null && func.getNextDropdown() != 0) {
				cleanNextChoose(func.getNextDropdown());
			}
		}
	}

	/**
	 * 级联存map数据
	 * 
	 * @param curNextFuncId
	 * @param value
	 */
	public void saveChooseMap(Integer curNextFuncId, String value) {
		setChooseMap(curNextFuncId, value);
		Func funcNext = null;
		if (targetType == Menu.TYPE_VISIT) {
			funcNext = new VisitFuncDB(context).findFuncByFuncId(curNextFuncId);
		} else {
			funcNext = new FuncDB(context).findFuncByFuncId(curNextFuncId);
		}
		if (funcNext != null && funcNext.getNextDropdown() != null && funcNext.getNextDropdown() != 0) {
			saveChooseMap(funcNext.getNextDropdown(), value);
		}
	}

	/**
	 * 
	 * @param currentLevel
	 *            当前操作的层级 操作上级的时候把下级的值清除
	 */
	public void cleanOrgNext(int currentLevel) {
		Map<Integer, String> orgValue = getOrgMap();
		for (Iterator<Entry<Integer, String>> iterator = orgValue.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry<Integer, String> entry = (Map.Entry<Integer, String>) iterator.next();
			int key = entry.getKey();
			if (key > currentLevel) {
				orgValue.put(key, null);
			}

		}
		if (absFuncActivity != null) {
			absFuncActivity.orgSetHook(isReplenish, replenish);
		}
	}

	/**
	 * 设置是查询中的控件标识
	 * 
	 * @param flag
	 *            true是报表 false 不是报表
	 */
	public void setRepleanishFlag(boolean flag) {
		isReplenish = flag;
	}

	/**
	 * 设置是报表中的控件标识
	 * 
	 * @param flag
	 *            true是报表 false 不是报表
	 */
	public void setReportFlag(boolean flag) {
		isReport = flag;
		reportActivity = (ReportActivity) context;
	}

	/**
	 * 设置报表当前控件的实例
	 * 
	 * @param reportWhere
	 *            当前控件的实例
	 */
	public void setReportWhere(ReportWhere reportWhere) {
		this.reportWhere = reportWhere;
	}

	/**
	 * @param bundle
	 *            查询条件值的携带者
	 */
	public void setReplenishBundle(Bundle bundle) {
		this.replenish = bundle;
	}

	/**
	 * 验证提交的次数 一定时间内 一个控件不能反复提交同一个值
	 * 
	 * @param func
	 *            当前控件
	 * @param value
	 *            当前控件的值
	 * @return 是否可以提交
	 */
	private boolean checkSubmit(Func func, String value) {
		boolean flag = true;
		if (func.getDayNumber() != null && func.getOperateNumber() != null) {
			FuncControlDB funcControlDB = new FuncControlDB(context);
			funcControlDB.removeFuncControlByDate(func.getDayNumber(), func.getTargetid(), func.getFuncId());// 删除范围以外的
			flag = funcControlDB.isCanSubmit(func.getTargetid(), func.getFuncId(), func.getOperateNumber(), value);
			if (flag) {
				if (func.getType() != Func.TYPE_BUTTON && func.getType() != Func.TYPE_CAMERA
						&& func.getType() != Func.TYPE_CAMERA_HEIGHT && func.getType() != Func.TYPE_CAMERA_MIDDLE
						&& func.getType() != Func.TYPE_CAMERA_CUSTOM && func.getType() != Func.TYPE_CHECKIN
						&& func.getType() != Func.TYPE_LINK && func.getType() != Func.TYPE_LOCATION
						&& func.getType() != Func.TYPE_TABLECOMP
						&& func.getType() != Func.TYPE_SINGLE_CHOICE_FUZZY_QUERY_COMP
						&& func.getType() != Func.TYPE_MULTI_CHOICE_FUZZY_QUERY_COMP
						&& func.getType() != Func.TYPE_RANGEDATE && func.getType() != Func.TYPE_RANGEEDIT
						&& func.getType() != Func.TYPE_HIDDEN) {

					FuncControl control = new FuncControl();
					control.setFuncId(func.getFuncId());
					control.setTargetId(func.getTargetid());
					control.setUpdateDate(DateUtil.getCurDate());
					control.setValues(value);
					control.setSubmitState(0);// 0表示未提交
					if (funcControlDB.isHas(control)) {// 如果已有
						if (TextUtils.isEmpty(value)) {
							funcControlDB.removeFuncControlByFuncId(func.getFuncId());
						} else {
							funcControlDB.updateFuncControlValue(control.getFuncId(), control.getTargetId(), value);
						}
					} else {
						funcControlDB.insertFuncControl(control);
					}
				}

			} else {
				ToastOrder.makeText(context, context.getResources().getString(R.string.not_promite_commit), ToastOrder.LENGTH_LONG).show();
			}
		}
		return flag;
	}

	/*
	 * 验证其他下拉框 如果是数字返回true否则返回false
	 */
	public boolean checkOther(String value) {
		if (TextUtils.isEmpty(value)) {
			return false;
		}
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
	}

	/**
	 * 如果是sql查询的控件设置sql值
	 * 
	 * @param defaultSql
	 *            查询到的值
	 */
	public void setDefaultSql(String defaultSql) {
		this.defaultSql = defaultSql;
	}

	/**
	 * 
	 * @return 机构层级关系的map 联动时候使用
	 */
	public Map<Integer, String> getOrgMap() {
		if (!isTableHistory && !isReport) {
			return absFuncActivity.orgMap;
		} else {
			return new HashMap<Integer, String>();
		}
	}

	/**
	 * 
	 * @return 非机构的联动控件的层级关系map
	 */
	public Map<Integer, String> getChooseMap() {
		if (!isTableHistory && !isReport) {
			return absFuncActivity.chooseMap;
		} else {
			return new HashMap<Integer, String>();
		}
	}

	/**
	 * 保存非机构联动级联控件的关系
	 * 
	 * @param key
	 *            下级的控件ID
	 * @param value
	 *            当前层级的值
	 */
	private void setChooseMap(Integer key, String value) {
		if (!isTableHistory && !isReport) {
			absFuncActivity.saveChooseMap(key, value);
		}
	}

	/**
	 * 更新对勾显示
	 */
	private void updateChooseHook() {
		if (!isTableHistory && !isReport) {
			absFuncActivity.setShowHook();
		}
		if (isReplenish) {
			QueryFuncActivity queryFuncActivity = (QueryFuncActivity) context;
			queryFuncActivity.setShowHook();
		}
	}

	/**
	 * 
	 * @return 将非机构级联控件的层级关系存到bundle里
	 */
	private Bundle getChooseMapBundle() {
		Bundle bundle = new Bundle();
		for (Map.Entry<Integer, String> m : getChooseMap().entrySet()) {
			bundle.putString(m.getKey() + "", m.getValue());
		}
		return bundle;
	}

	/**
	 * 
	 * @return 将机构级联控件的层级关系存到bundle里
	 */
	private Bundle getOrgMapBundle() {
		Bundle bundle = new Bundle();
		for (Map.Entry<Integer, String> m : getOrgMap().entrySet()) {
			bundle.putString(m.getKey() + "", m.getValue());
		}
		return bundle;
	}

	public ProductCode getProductCode() {
		return productCode;
	}

	public ScanInputCode getScanProductCode() {
		return scanInputCode;
	}

	public void setProductCodeListener(ProductCodeListener productCodeListener) {
		this.productCodeListener = productCodeListener;
	}
	private void setContrlViewFunc(String value){//

		if(controlEnterList!=null){
			if(controlEnterList.contains(String.valueOf(func.getFuncId()))){
				TypeValue typeValue = null;
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

	public void completeConfrim() {
		if (func != null && currentComponent != null) {
				func.setvalue(currentComponent.value);


			// currentComponent.
			if (func.getType() == Func.TYPE_PRODUCT_CODE) {
				saveProductCode();
			} else if (func.getType() == Func.TYPE_SCAN_INPUT) {
				saveScanInputCode();
			} else {
				if (checkType() && checkSubmit(func, currentComponent.value)) {// 先验证类型
					if ((func.getType() == Func.TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY_COMP
							|| func.getType() == Func.TYPE_SELECT_OTHER) && !isReplenish) {// 存储其他类型的控件值并且不是查询的时候
						saveOther();// 保存其他类型的控件的值
					} else if (func.getType() == Func.TYPE_MULTI_CHOICE_SPINNER_COMP && func.getFuncId() == -8968
							&& !isReplenish) {// 存储用户
						saveUser();// 存储用户
					} else if (isReport && func.getFuncId() == -2907) {// 报表查询条件保存
						reportActivity.saveReport(reportWhere, currentComponent.value);
					} else {
						if (!isReplenish) {
							save();// 将值存进数据库
							setContrlViewFunc(currentComponent.selectedValue);
						} else {
							saveReplenish(func, currentComponent.value);// 数据查询的时候保存查询不保存数据库
						}
					}
					controlOrgAndNext();// 控制联动
				}
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
			}
//			if (func != null && func.getIsCacheFun() == 1) {
//				FuncCacheDB fcDb = new FuncCacheDB(context);
//				fcDb.updateValueToCache(func, dataName);
//
//			}
		}

	}

	private void clearNextDropDown(int nextFunId){
		HashMap<Integer, View> allFuncLayout = absFuncActivity.getAllFuncLayout();
		View view1 = allFuncLayout.get(nextFunId);
		if (view1!=null){
			TextView tvContent = (TextView) view1.findViewById(R.id.tv_func_content);
			if (tvContent!=null){
				tvContent.setText("");
			}
		}
		Func byFuncId = new FuncDB(context).findFuncByFuncId(nextFunId);
		Integer nextDropdown = byFuncId.getNextDropdown();
		if (nextDropdown!=null&&nextDropdown!=0){
			clearNextDropDown(nextDropdown);
		}
	}

}
