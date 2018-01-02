package com.yunhu.yhshxc.activity;

import gcg.org.debug.JLog;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.GGMMCheck.GGMMCheckListener;
import com.yunhu.yhshxc.activity.popup.PhotoPopupWindow;
import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.LocationResult;
import com.yunhu.yhshxc.bo.Org;
import com.yunhu.yhshxc.bo.OrgStore;
import com.yunhu.yhshxc.bo.OrgUser;
import com.yunhu.yhshxc.bo.Submit;
import com.yunhu.yhshxc.bo.SubmitItem;
import com.yunhu.yhshxc.bo.TableStencil;
import com.yunhu.yhshxc.comp.Component;
import com.yunhu.yhshxc.comp.DateInTableComp;
import com.yunhu.yhshxc.comp.EditComp;
import com.yunhu.yhshxc.comp.HiddenCompInTable;
import com.yunhu.yhshxc.comp.PhotoInTableComp;
import com.yunhu.yhshxc.comp.ProductCode;
import com.yunhu.yhshxc.comp.ProductCodeInTableComp;
import com.yunhu.yhshxc.comp.ProductCodeTable;
import com.yunhu.yhshxc.comp.ScanInTableComp;
import com.yunhu.yhshxc.comp.ScanInputCodeTable;
import com.yunhu.yhshxc.comp.TextComp;
import com.yunhu.yhshxc.comp.TimeInTableComp;
import com.yunhu.yhshxc.comp.menu.TableButtonComp;
import com.yunhu.yhshxc.comp.spinner.AbsSelectComp;
import com.yunhu.yhshxc.comp.spinner.FuzzyMultiChoiceSpinnerComp;
import com.yunhu.yhshxc.comp.spinner.FuzzySingleChoiceSpinnerComp;
import com.yunhu.yhshxc.comp.spinner.MultiChoiceSpinnerComp;
import com.yunhu.yhshxc.comp.spinner.SingleChoiceSpinnerComp;
import com.yunhu.yhshxc.comp.spinner.SpinnerComp;
import com.yunhu.yhshxc.database.DictDB;
import com.yunhu.yhshxc.database.FuncDB;
import com.yunhu.yhshxc.database.OrgDB;
import com.yunhu.yhshxc.database.OrgStoreDB;
import com.yunhu.yhshxc.database.OrgUserDB;
import com.yunhu.yhshxc.database.SubmitDB;
import com.yunhu.yhshxc.database.SubmitItemDB;
import com.yunhu.yhshxc.database.SubmitItemTempDB;
import com.yunhu.yhshxc.database.TableStencilDB;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.exception.DataComputeException;
import com.yunhu.yhshxc.func.AbsFuncActivity;
import com.yunhu.yhshxc.func.FuncTreeForTable;
import com.yunhu.yhshxc.http.submit.SubmitManager;
import com.yunhu.yhshxc.list.activity.ShowImageTableActivity;
import com.yunhu.yhshxc.listener.ProductCodeListener;
import com.yunhu.yhshxc.listener.SubmitDataListener;
import com.yunhu.yhshxc.location.ReceiveLocationListener;
import com.yunhu.yhshxc.location2.LocationFactoy;
import com.yunhu.yhshxc.parser.TableParse;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.FileHelper;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.SharedPreferencesUtilForTable;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.google.zxing.activity.CaptureActivity;
import com.nostra13.universalimageloader.core.ImageLoader;


public class TableCompView extends LinearLayout implements OnClickListener, ProductCodeListener,
		ReceiveLocationListener, GGMMCheckListener, SubmitDataListener {
	private static final String TAG = "TableCompView";
	
	private int positionId;//记录表格控件位置,用于确定哪个表格控件的事件
	private int showTag=0;
	/**
	 * 表格中所有列的func的集合
	 */
	private List<Func> funcList;
	/**
	 * 控件数据库
	 */
	private FuncDB funcDB;
	/**
	 * 提交数据库
	 */
	private SubmitItemDB submitItemDB;
	/**
	 * 提交表单数据库
	 */
	private SubmitDB submitDB;
	/**
	 * 添加一行
	 */
	private LinearLayout btn_add;
	/**
	 * 保存数据
	 */
	private LinearLayout btn_save;
	/**
	 * 保存模版
	 */
	private LinearLayout btn_stencil;
	/**
	 * 放控件View的父VIEW
	 */
	private LinearLayout ll_tableContent;
	/**
	 * 级联所用
	 */
	private Map<Integer, Component> tempCompoMap;
	/**
	 * 存储组件
	 */
	private List<Component> compList;
	/**
	 * 表格控件
	 */
	private Func funcTable;
	/**
	 * 当前行的数据 key是控件ID value是值
	 */
	private HashMap<String, String> currentMap;
	/**
	 * 是否是历史信息查询
	 */
	private boolean isTableHistory = false;
	/**
	 * sql查询的值
	 */
	private String defaultSql;
	/**
	 * 表格长度
	 */
	private int tableLen;
	/**
	 * 弹出表格的时间
	 */
	private String startTime;
	/**
	 * 表格的标题
	 */
	private LinearLayout ll_tableContentTitle;
	/**
	 * 传值
	 */
	private Bundle bundle;
	/**
	 * 级联值
	 */
	private Bundle chooseMapBundle;
	/**
	 * 组织结构值
	 */
	private Bundle orgMapBundle;
	/**
	 * 初始化时候加载dialog
	 */
	// private Dialog initTableDialog;

	private ScrollView sv;

	/**
	 * 删除按钮的宽
	 */
	private int widthDeleteBtn;
	/**
	 * 删除按钮的高
	 */
	private int heigh;
	/**
	 * 表示sql查询的时候拼key要添加data_
	 */
	private boolean isNeedAddData = true;
	/**
	 * 主键控件，需要平铺
	 */
	private Func keyFunc = null;
	/**
	 * 是否能添加和删除行 true能 false 不能
	 */
	private boolean isAddDel = true;
	/**
	 * 主键控件，需要平铺 平铺的下拉框
	 */
	private List<Dictionary> keyFuncDicList;
	/**
	 * sql类型的网络是否是通的默认网路是通的
	 */
	private boolean isNetCanUse = true;
	/**
	 * 是否是超链接里的表格
	 */
	private boolean isLink;

	private ProductCodeTable productCode;// 串码控件
	private ScanInputCodeTable scanInputCode;// 可输入扫描控件

	/**
	 * 是否是使用模版
	 */
	private boolean isUseStencil = false;// 默认不是使用模版 true表示是使用模版

	/**
	 * 当前表格加载到多少行
	 */
	private int compRow;
	private Map<String, ArrayList<Component>> rowMap;// 表格中每一行控件 key是行号
														// value是该行所有控件的集合

	private boolean isNoWait;// 是否是无等待提交 true是无等待提交 false不是无等待提交

	private int planId;
	private int wayId;
	private int storeId;
	private int targetId;
	private String storeName;
	private String wayName;
	private int targetType;
	private int modType;

	private int currentCompIndex;// 当前控件所在集合中的位置（拍照，扫描，串码 异常处理使用）
	private boolean isNormal = true;// 拍照 扫描是否是正常返回 true正常返回 false非正常返回
	private String unNormalCompData;// 异常返回的时候要从这里获取已经操作的数据 json的形式

	private GGMMCheck ggmmCheck;// GGMM做特殊验证

	private Context mContext;
	/**
	 * 携带传进来的参数
	 */
	private Intent intent;
	/**
	 * 该页面整个布局
	 */
	private View layoutView;
	/**
	 * 表格主标题
	 */
	private TextView titleView;
	
	private int menuId;
	private int menuType;
	private String menuName;

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
	private HorizontalScrollView hsl_table;
	public TableCompView(Context context, Intent intent) {
		super(context);
		mContext = context;
		this.intent = intent;
       
		LayoutInflater inflater = LayoutInflater.from(context);
		layoutView = inflater.inflate(R.layout.table_activity_comp, null);
		layoutView.findViewById(R.id.back).setVisibility(View.INVISIBLE);
		titleView = (TextView) layoutView.findViewById(R.id.tv_titleName);
		addView(layoutView);
		if (TextUtils.isEmpty(startTime)) {
			startTime = DateUtil.getCurDateTime();
		}
		rowMap = new HashMap<String, ArrayList<Component>>();
		init();
		initTitleView();
		initData();
	}
    /**
     * 设置表格的标题
     * @param title
     */
	public void setTitleName(String title) {
		titleView.setText(title);
	}

	private void initTitleView() {
		if (ll_tableContentTitle != null) {
			ll_tableContentTitle.addView(addTableTitleRow());
		}
	}

	/**
	 * 初始化实例
	 */
	private void init() {
		ggmmCheck = new GGMMCheck(mContext, TableCompView.this);
		sv = (ScrollView) layoutView.findViewById(R.id.sv_table);
		// 初始化加载dialog
		// initTableDialog = new MyProgressDialog(mContext,
		// R.style.CustomProgressDialog,
		// getResources().getString(R.string.initTable));
		// initTableDialog.show();
		// setScreenRotation(false);//刚开始设置屏幕不可旋转
		// initTableDialog.setOnDismissListener(dialogDismissListener);
		// 添加按钮
		btn_add = (LinearLayout) layoutView.findViewById(R.id.btn_add);
		btn_add.setOnClickListener(TableCompView.this);
		// 保存按钮
		btn_save = (LinearLayout) layoutView.findViewById(R.id.btn_save);
		btn_save.setOnClickListener(TableCompView.this);
		// 保存模版按钮
		btn_stencil = (LinearLayout) layoutView.findViewById(R.id.btn_stencil);
		btn_stencil.setOnClickListener(TableCompView.this);
		// 表格内容
		ll_tableContent = (LinearLayout) layoutView.findViewById(R.id.ll_tableContent);
		// 表格标题
		ll_tableContentTitle = (LinearLayout) layoutView.findViewById(R.id.ll_tableContentTitle);
		compList = new ArrayList<Component>();

		funcDB = new FuncDB(mContext);
		submitItemDB = new SubmitItemDB(mContext);
		submitDB = new SubmitDB(mContext);
		// 获取传过来的值
		if (isNormal) {
			bundle = intent.getBundleExtra("tableBundle");
			// 获取级联关系
			chooseMapBundle = intent.getBundleExtra("chooseMapBundle");
			// 获取组织结构关系
			orgMapBundle = intent.getBundleExtra("orgMapBundle");
			isNoWait = intent.getBooleanExtra("isNoWait", true);// 是否无等待提交
		}
		// 获取是否是超链接
		isLink = bundle.getBoolean("isLink");
		// 获取表格控件实例
		this.funcTable = (Func) bundle.getSerializable("func");
		// 获取sql值
		this.defaultSql = bundle.getString("defaultSql");
		// 获取是否是表格历史信息查询
		this.isTableHistory = bundle.getBoolean("isTableHistory");
		// 获取sql类型网络是否通的
		if (bundle.containsKey("isNetCanUse")) {
			this.isNetCanUse = bundle.getBoolean("isNetCanUse");
		}
		tempCompoMap = new HashMap<Integer, Component>();
		funcList = funcDB.findFuncListContainHiddenByTargetid(funcTable.getTableId());// 查找table中的所有组件
		tableLen = getTableLen();
		planId = bundle.getInt("planId");
		storeId = bundle.getInt("storeId");
		storeName = bundle.getString("storeName");
		targetId = bundle.getInt("targetId");
		wayId = bundle.getInt("wayId");
		wayName = bundle.getString("wayName");
		targetType = bundle.getInt("targetType");
		modType = bundle.getInt("modType");
		initUI();// 初始化表格控件的宽和高
	}

	// private DialogInterface.OnDismissListener dialogDismissListener = new
	// DialogInterface.OnDismissListener() {
	//
	// @Override
	// public void onDismiss(DialogInterface dialog) {
	// setScreenRotation(true);//数据加载完毕设置数据可旋转
	// }
	// };
	/**
	 * 添加表格的标题
	 */
	@SuppressLint("ResourceAsColor")
	private View addTableTitleRow() {
		int i = 1;
		TableCell[] tableCell = new TableCell[tableLen + 1];// 列数
		TextView tv_title;
		TextView delTv = new TextView(mContext);
		delTv.setTextSize(14);// 设置文字大小
		delTv.setGravity(Gravity.CENTER);// 设置居中
		delTv.setTextColor(Color.WHITE);// 设置字体颜色
		delTv.setText("");
		// delTv.setBackgroundColor(R.color.blue);
		tableCell[0] = new TableCell(delTv, widthDeleteBtn, heigh);
		for (Func func : funcList) {
			// 如果控件是隐藏域则不添加
			if (func.getType() == Func.TYPE_HIDDEN) {
				continue;
			}
			// 如果有主键 就设置主键
			if (keyFunc == null && func.getIsImportKey() == 1
					&& (func.getType() == Func.TYPE_SINGLE_CHOICE_FUZZY_QUERY_COMP
							|| func.getType() == Func.TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY_COMP
							|| func.getType() == Func.TYPE_SELECTCOMP || func.getType() == Func.TYPE_SELECT_OTHER
							|| func.getType() == Func.TYPE_MULTI_CHOICE_SPINNER_COMP
							|| func.getType() == Func.TYPE_MULTI_CHOICE_FUZZY_QUERY_COMP)) {
				keyFunc = func;
				String isAD = funcTable.getAbleAD();
				if ("11".equals(isAD)) {// 设置不允许添加和删除行
					isAddDel = false;
				}
			}
			tv_title = new TextView(mContext);
			tv_title.setText(func.getName());
			tv_title.setTextSize(14);
			tv_title.setGravity(Gravity.CENTER);
			tv_title.setTextColor(Color.WHITE);
			// tv_title.setBackgroundColor(R.color.title_bg_form);
			if (func.getType() == Func.TYPE_EDITCOMP || func.getType() == Func.TYPE_DATEPICKERCOMP) {// 设置输入框和日期的类型的宽
				tableCell[i] = new TableCell(tv_title, getFuncColWidth(func), heigh);
			} else {
				tableCell[i] = new TableCell(tv_title, getFuncColWidth(func), heigh);
			}
			i++;
		}
		View rowView = getTableComp(new TableRow(tableCell), true);
		return rowView;
		// ll_tableContentTitle.addView(rowView);
	}

	/**
	 * 获取表格的长度(除去隐藏域)
	 * 
	 * @return 表格列数
	 */
	private int getTableLen() {
		// int len = 0 ;
		// for(Func func :funcList){
		//// if(func.getType() != Func.TYPE_HIDDEN){
		//// len++;
		//// }
		// len++;
		// }
		// JLog.d(TAG, "表格列数 -- TableLen == >"+len);
		// return len;
		return funcList != null ? funcList.size() : 0;
	}

	/**
	 * 
	 * @param keyFuncStr
	 *            主键平铺的时候的值
	 * @return 表格的一行
	 */
	public View addTableRow(String keyFuncStr) {
		compRow = compRow + 1;
		currentMap = null;
		int i = 1;
		TableCell[] tableCell = new TableCell[tableLen + 1];// 列数
		TableButtonComp delImg = new TableButtonComp(mContext);
		tableCell[0] = new TableCell(delImg.getView(), widthDeleteBtn, heigh);
		for (final Func func : funcList) {
			if (func.getType() == Func.TYPE_HIDDEN) {
				HiddenCompInTable hiddenCompInTable = new HiddenCompInTable();
				hiddenCompInTable.compFunc = func;
				compList.add(hiddenCompInTable);
			} else if (func.getType() == Func.TYPE_TEXTCOMP) {// 文本标签
				tableCell[i] = new TableCell(selectComp(func).getObject(), getFuncColWidth(func), heigh);
			} else if (func.getType() == Func.TYPE_EDITCOMP) {// 输入框
				EditComp ec = (EditComp) selectComp(func);
				EditText et = ec.getCurrentEditText();
				et.setGravity(Gravity.CENTER);
				View view = ec.getObject();
				tableCell[i] = new TableCell(view, getFuncColWidth(func), heigh);
				if (keyFunc != null && func.getIsEdit() == Func.IS_N) {// 如果有主键
																		// 并且设置不可用，则不能点击
					ec.setIsEnable(false);
				}
			} else if (func.getType() == Func.TYPE_DATEPICKERCOMP) {// 日期类型
				DateInTableComp dateInTableComp = new DateInTableComp(mContext, func);
				dateInTableComp.compRow = compRow;
				View view = dateInTableComp.getObject();
				tableCell[i] = new TableCell(view, getFuncColWidth(func), heigh);
				compList.add(dateInTableComp);
				if (keyFunc != null && func.getIsEdit() == Func.IS_N) {
					dateInTableComp.setIsEnable(false);
				}
			} else if (func.getType() == Func.TYPE_TIMEPICKERCOMP) {// 时间类型
				TimeInTableComp timeInTableComp = new TimeInTableComp(mContext, func);
				timeInTableComp.compRow = compRow;
				View view = timeInTableComp.getObject();
				tableCell[i] = new TableCell(view, getFuncColWidth(func), heigh);
				compList.add(timeInTableComp);
				if (keyFunc != null && func.getIsEdit() == Func.IS_N) {
					timeInTableComp.setIsEnable(false);
				}
			} else if (func.getType() == Func.TYPE_SINGLE_CHOICE_FUZZY_QUERY_COMP) {// 单选模糊搜索
				FuzzySingleChoiceSpinnerComp sc = (FuzzySingleChoiceSpinnerComp) selectComp(func);

				if (keyFunc != null && func.getFuncId() == keyFunc.getFuncId()) {
					sc.setSelected(keyFuncStr);
				}
				View view = sc.getObject();
				tableCell[i] = new TableCell(view, getFuncColWidth(func), heigh);
				if (keyFunc != null && func.getIsEdit() == Func.IS_N) {
					sc.setIsEnable(false);
				}

			} else if (func.getType() == Func.TYPE_SELECT_OTHER) {// 单选其他

				SingleChoiceSpinnerComp sc = (SingleChoiceSpinnerComp) selectComp(func);

				if (keyFunc != null && func.getFuncId() == keyFunc.getFuncId()) {
					sc.setSelected(keyFuncStr);
				}
				View view = sc.getObject();
				tableCell[i] = new TableCell(view, getFuncColWidth(func), heigh);
				if (keyFunc != null && func.getIsEdit() == Func.IS_N) {
					sc.setIsEnable(false);
				}

			} else if (func.getType() == Func.TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY_COMP) {// 单选模糊搜索其他
				FuzzySingleChoiceSpinnerComp sc = (FuzzySingleChoiceSpinnerComp) selectComp(func);

				if (keyFunc != null && func.getFuncId() == keyFunc.getFuncId()) {
					sc.setSelected(keyFuncStr);
				}
				View view = sc.getObject();
				tableCell[i] = new TableCell(view, getFuncColWidth(func), heigh);
				if (keyFunc != null && func.getIsEdit() == Func.IS_N) {
					sc.setIsEnable(false);
				}
			} else if (func.getType() == Func.TYPE_MULTI_CHOICE_FUZZY_QUERY_COMP) {// 多选模糊搜索
				FuzzyMultiChoiceSpinnerComp sc = (FuzzyMultiChoiceSpinnerComp) selectComp(func);

				if (keyFunc != null && func.getFuncId() == keyFunc.getFuncId()) {
					sc.setSelected(keyFuncStr);
				}
				View view = sc.getObject();
				tableCell[i] = new TableCell(view, getFuncColWidth(func), heigh);
				if (keyFunc != null && func.getIsEdit() == Func.IS_N) {
					sc.setIsEnable(false);
				}
			} else if (func.getType() == Func.TYPE_MULTI_CHOICE_SPINNER_COMP) {// 多选
				MultiChoiceSpinnerComp sc = (MultiChoiceSpinnerComp) selectComp(func);

				if (keyFunc != null && func.getFuncId() == keyFunc.getFuncId()) {
					sc.setSelected(keyFuncStr);
				}
				View view = sc.getObject();
				tableCell[i] = new TableCell(view, getFuncColWidth(func), heigh);
				if (keyFunc != null && func.getIsEdit() == Func.IS_N) {
					sc.setIsEnable(false);
				}
			} else if (func.getType() == Func.TYPE_SCAN) {// 扫描
				final ScanInTableComp scanInTableComp = new ScanInTableComp(mContext, func);
				scanInTableComp.compRow = compRow;
				View view = scanInTableComp.getObject();
				tableCell[i] = new TableCell(view, getFuncColWidth(func), heigh);
				compList.add(scanInTableComp);
				scanInTableComp.getCurrentButton().setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						currentCompIndex = compList.indexOf(scanInTableComp);
						currentScanInTableComp = scanInTableComp;
						clickScan(func);
						startPhotoClickListener.clickPhotoPosition(positionId);//用来确定是哪个表格控件的表格内点击事件
					}
				});
				if (keyFunc != null && func.getIsEdit() == Func.IS_N) {
					scanInTableComp.setIsEnable(false);
				}
			} else if (func.getType() == Func.TYPE_SCAN_INPUT) {// 扫描
				final ScanInTableComp scanInTableComp = new ScanInTableComp(mContext, func);
				scanInTableComp.compRow = compRow;
				View view = scanInTableComp.getObject();
				tableCell[i] = new TableCell(view, getFuncColWidth(func), heigh);
				compList.add(scanInTableComp);
				scanInTableComp.getCurrentButton().setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						currentCompIndex = compList.indexOf(scanInTableComp);
						scanInputCode = new ScanInputCodeTable(mContext, scanInTableComp.getFunc(), null);
						scanInputCode.setProductCodeListener(TableCompView.this);
						scanInputCode.setProductTableType(ProductCode.TABLE_TYPE_TABLE);
						scanInputCode.setTableRow(scanInTableComp.compRow);
						scanInputCode.scanInputCodeInTable(scanInTableComp, scanInTableComp.value);
						startPhotoClickListener.clickPhotoPosition(positionId);//用来确定是哪个表格控件的表格内点击事件
					}
				});
				if (keyFunc != null && func.getIsEdit() == Func.IS_N) {
					scanInTableComp.setIsEnable(false);
				}
			} else if (func.getType() == Func.TYPE_PRODUCT_CODE) {// 串码
				final ProductCodeInTableComp productCodeInTableComp = new ProductCodeInTableComp(mContext, func);
				View view = productCodeInTableComp.getObject();
				productCodeInTableComp.compRow = compRow;
				tableCell[i] = new TableCell(view, getFuncColWidth(func), heigh);
				compList.add(productCodeInTableComp);
				productCodeInTableComp.getCurrentButton().setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						currentCompIndex = compList.indexOf(productCodeInTableComp);
						productCode = new ProductCodeTable(mContext, func, null);
						productCode.setProductCodeListener(TableCompView.this);
						productCode.setProductTableType(ProductCode.TABLE_TYPE_TABLE);
						productCode.setTableRow(productCodeInTableComp.compRow);
						productCode.productCodeInTable(productCodeInTableComp, productCodeInTableComp.value);
						startPhotoClickListener.clickPhotoPosition(positionId);//用来确定是哪个表格控件的表格内点击事件
					}
				});
				if (keyFunc != null && func.getIsEdit() == Func.IS_N) {
					productCodeInTableComp.setIsEnable(false);
				}
			} else if (func.getType() == Func.TYPE_CAMERA || func.getType() == Func.TYPE_CAMERA_HEIGHT
					|| func.getType() == Func.TYPE_CAMERA_MIDDLE || func.getType() == Func.TYPE_CAMERA_CUSTOM) {// 拍照
				
				try {
					
					final PhotoInTableComp photoInTableComp = new PhotoInTableComp(mContext, func);
					photoInTableComp.compRow = compRow;
					View view = photoInTableComp.getObject();
					tableCell[i] = new TableCell(view, getFuncColWidth(func), heigh);
					compList.add(photoInTableComp);
					photoInTableComp.getCurrentButton().setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							currentCompIndex = compList.indexOf(photoInTableComp);
							currentPhotoInTableComp = photoInTableComp;
							fileName = System.currentTimeMillis() + ".jpg";
							int photoFlg = func.getPhotoFlg();
							if (photoFlg == 2) {
								new PhotoPopupWindow(mContext, currentPhotoInTableComp, fileName).show(null);
								startPhotoClickListener.clickPhotoPosition(positionId);//用来确定是哪个表格控件的表格内点击事件
							} else {
								currentPhotoInTableComp.startPhotoTable(fileName);
								startPhotoClickListener.clickPhotoPosition(positionId);//用来确定是哪个表格控件的表格内点击事件
								
							}
						}
					});
					if (keyFunc != null && func.getIsEdit() == Func.IS_N) {
						photoInTableComp.setIsEnable(false);
					}
				
				} catch (Exception e) {
					//如果拍照控件出现异常
//					Toast.makeText(mContext, "请重新点击拍照按钮", Toast.LENGTH_SHORT).show();
				}
			} else {// 下拉框

				SpinnerComp sc = (SpinnerComp) selectComp(func);
				if (keyFunc != null && func.getFuncId() == keyFunc.getFuncId()) {
					sc.setSelected(keyFuncStr);
				}
				sc.compRow = compRow;
				View view = sc.getObject();
				tableCell[i] = new TableCell(view, getFuncColWidth(func), heigh);
				if (keyFunc != null && func.getIsEdit() == Func.IS_N) {
					sc.setIsEnable(false);
				}
			}
			i++;
		}
		View rowView = getTableComp(new TableRow(tableCell), false);// 设置一行的view
		delImg.getView().setTag(rowView);
		ArrayList<Component> subList = new ArrayList<Component>();
		for (int j = compList.size() - tableLen; j < compList.size(); j++) {
			subList.add(compList.get(j));
		}
		rowView.setTag(subList);
		rowMap.put(String.valueOf(compRow), subList);
		delImg.getView().setOnClickListener(onTableRowClick);// 给删除按钮添加单击事件
		delImg.getView().requestFocus();
		return rowView;
		// ll_tableContent.addView(rowView);

	}

	// /**
	// * 添加表格中下拉框选择标识
	// */
	// private void addTbleRowSelect(Func spinnerFunc,int rowNum,String
	// value,String did){
	// String key =
	// String.valueOf(spinnerFunc.getFuncId())+"#"+String.valueOf(rowNum);
	// String select = did+"#"+value;
	// SharedPreferencesUtilForTable.getInstance(this).setTableSelect(key,
	// select);
	// }

	/**
	 * 移除表格中下拉框标识
	 */
	private void removeTableRowSelect(Func spinnerFunc, int rowNum) {
		String key = String.valueOf(spinnerFunc.getFuncId()) + "#" + String.valueOf(rowNum);
		SharedPreferencesUtilForTable.getInstance(mContext).clear(key);
	}

	/**
	 * 删除按钮的单击事件
	 */
	private OnClickListener onTableRowClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (isAddDel) {
				View view = (View) v.getTag();
				deleteDialog(view).show();// 弹出删除dialog
			} else {
				ToastOrder.makeText(mContext, R.string.save_sucessful2, ToastOrder.LENGTH_SHORT).show();
			}

		}
	};

	/**
	 * 删除一行dialog
	 * 
	 * @param deleteView
	 *            要删除的行
	 * @return dialog
	 */
	private Dialog deleteDialog(final View deleteView) {
		final Dialog deleteDialog = new Dialog(mContext, R.style.transparentDialog);
		View view = View.inflate(mContext, R.layout.delelte_dialog, null);
		Button confirm = (Button) view.findViewById(R.id.btn_back_confirm);
		Button cancel = (Button) view.findViewById(R.id.btn_back_cancel);
		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				deleteDialog.dismiss();
				ll_tableContent.removeView(deleteView);// 删除传过来的行
				List<Component> subList = (List<Component>) deleteView.getTag();
				int row = subList.get(0).compRow;
				for (int i = 0; i < subList.size(); i++) {// 如果改行里面有拍照，删除sd卡下的照片
					Func f = funcList.get(i);
					String value = subList.get(i).value;
					if ((f.getType() == Func.TYPE_CAMERA || f.getType() == Func.TYPE_CAMERA_HEIGHT
							|| f.getType() == Func.TYPE_CAMERA_MIDDLE)
							|| f.getType() == Func.TYPE_CAMERA_CUSTOM && !TextUtils.isEmpty(value)) {
						new FileHelper().deleteFile(Constants.SDCARD_PATH + value);
					}
					if (f.getType() == Func.TYPE_SELECTCOMP) {
						Dictionary dic = new DictDB(mContext).findDictListByTable(f.getDictTable(), f.getDictDataId(),
								value);
						if (dic != null) {
							int count = dic.getSelectCount();
							if (count >= 1) {
								count = count - 1;
								new DictDB(mContext).updateDictSelectCount(f.getDictTable(), value, count);
							}
						}

					}
				}
				compList.removeAll(subList);
				rowMap.remove(row);
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				deleteDialog.dismiss();
			}
		});
		deleteDialog.setContentView(view);
		deleteDialog.setCancelable(false);
		return deleteDialog;
	}

	/**
	 * 添加一行
	 */
	public View addTableRow(HashMap<String, String> rowData) {
		currentMap = rowData;
		compRow = compRow + 1;
		int i = 1;
		TableCell[] tableCell = new TableCell[tableLen + 1];// 列数
		TableButtonComp delImg = new TableButtonComp(mContext);
		tableCell[0] = new TableCell(delImg.getView(), widthDeleteBtn, heigh);

		for (final Func func : funcList) {
			String key = func.getFuncId() + "";
			if (funcTable.getDefaultType() != null && funcTable.getDefaultType() == Func.DEFAULT_TYPE_SQL
					&& isNeedAddData) {
				key = "data_" + func.getFuncId();
			}
			String currentTableCompValue = rowData.get(key);
			if (!TextUtils.isEmpty(currentTableCompValue) && currentTableCompValue.equals("@@")) {
				currentTableCompValue = "";
			}
			// JLog.d(TAG, "currentTableCompValue==>" + currentTableCompValue);

			if (func.getType() == Func.TYPE_HIDDEN) {
				HiddenCompInTable hiddenCompInTable = new HiddenCompInTable();
				hiddenCompInTable.value = currentTableCompValue;
				hiddenCompInTable.compFunc = func;
				compList.add(hiddenCompInTable);
			} else if (func.getType() == Func.TYPE_TEXTCOMP) {// 文本类型
				tableCell[i] = new TableCell(selectComp(func).getObject(), getFuncColWidth(func), heigh);
			} else if (func.getType() == Func.TYPE_EDITCOMP) {// 输入类型
				if (!(bundle.getInt("targetId") == 11793 && func.getFuncId() == 13580)) {// TODO临时写死
					EditComp ec = (EditComp) selectComp(func);
					View view = ec.getObject();
					ec.setValue(currentTableCompValue);// 设置值
					tableCell[i] = new TableCell(view, getFuncColWidth(func), heigh);
					if (keyFunc != null && func.getIsEdit() == Func.IS_N) {
						ec.setIsEnable(false);
					}
				}
			} else if (func.getType() == Func.TYPE_DATEPICKERCOMP) {
				DateInTableComp dateInTableComp = new DateInTableComp(mContext, func);
				dateInTableComp.compRow = compRow;
				View view = dateInTableComp.getObject();
				dateInTableComp.setValue(currentTableCompValue);
				tableCell[i] = new TableCell(view, getFuncColWidth(func), heigh);
				compList.add(dateInTableComp);
				if (keyFunc != null && func.getIsEdit() == Func.IS_N) {
					dateInTableComp.setIsEnable(false);
				}
			} else if (func.getType() == Func.TYPE_TIMEPICKERCOMP) {
				TimeInTableComp timeInTableComp = new TimeInTableComp(mContext, func);
				timeInTableComp.compRow = compRow;
				View view = timeInTableComp.getObject();
				timeInTableComp.setValue(currentTableCompValue);
				tableCell[i] = new TableCell(view, getFuncColWidth(func), heigh);
				compList.add(timeInTableComp);
				if (keyFunc != null && func.getIsEdit() == Func.IS_N) {
					timeInTableComp.setIsEnable(false);
				}
			} else if (func.getType() == Func.TYPE_SELECTCOMP) {// 下拉选项类型
				SpinnerComp sc = (SpinnerComp) selectComp(func);
				sc.setSelected(currentTableCompValue);
				View view = sc.getObject();
				tableCell[i] = new TableCell(view, getFuncColWidth(func), heigh);
				if (keyFunc != null && func.getIsEdit() == Func.IS_N) {
					sc.setIsEnable(false);
				}

			} else if (func.getType() == Func.TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY_COMP) {
				FuzzySingleChoiceSpinnerComp sc = (FuzzySingleChoiceSpinnerComp) selectComp(func);
				sc.setCurrentMap(rowData);
				sc.setSelected(currentTableCompValue);
				View view = sc.getObject();
				tableCell[i] = new TableCell(view, getFuncColWidth(func), heigh);

				if (keyFunc != null && func.getIsEdit() == Func.IS_N) {
					sc.setIsEnable(false);
				}

			} else if (func.getType() == Func.TYPE_SINGLE_CHOICE_FUZZY_QUERY_COMP) {

				FuzzySingleChoiceSpinnerComp sc = (FuzzySingleChoiceSpinnerComp) selectComp(func);
				sc.setCurrentMap(rowData);
				sc.setSelected(currentTableCompValue);
				View view = sc.getObject();
				tableCell[i] = new TableCell(view, getFuncColWidth(func), heigh);

				if (keyFunc != null && func.getIsEdit() == Func.IS_N) {
					sc.setIsEnable(false);
				}

			} else if (func.getType() == Func.TYPE_SELECT_OTHER) {

				SingleChoiceSpinnerComp sc = (SingleChoiceSpinnerComp) selectComp(func);
				sc.setCurrentMap(rowData);
				sc.setSelected(currentTableCompValue);
				View view = sc.getObject();
				tableCell[i] = new TableCell(view, getFuncColWidth(func), heigh);
				if (keyFunc != null && func.getIsEdit() == Func.IS_N) {
					sc.setIsEnable(false);
				}

			} else if (func.getType() == Func.TYPE_MULTI_CHOICE_FUZZY_QUERY_COMP) {// 多选模糊搜索
				FuzzyMultiChoiceSpinnerComp sc = (FuzzyMultiChoiceSpinnerComp) selectComp(func);
				sc.setSelected(currentTableCompValue);
				View view = sc.getObject();
				tableCell[i] = new TableCell(view, getFuncColWidth(func), heigh);
				if (keyFunc != null && func.getIsEdit() == Func.IS_N) {
					sc.setIsEnable(false);
				}
			} else if (func.getType() == Func.TYPE_MULTI_CHOICE_SPINNER_COMP) {// 多选
				MultiChoiceSpinnerComp sc = (MultiChoiceSpinnerComp) selectComp(func);
				sc.setSelected(currentTableCompValue);
				View view = sc.getObject();
				tableCell[i] = new TableCell(view, getFuncColWidth(func), heigh);
				if (keyFunc != null && func.getIsEdit() == Func.IS_N) {
					sc.setIsEnable(false);
				}
			} else if (func.getType() == Func.TYPE_SCAN) {// 扫描
				final ScanInTableComp scanInTableComp = new ScanInTableComp(mContext, func);
				scanInTableComp.compRow = compRow;
				View view = scanInTableComp.getObject();
				scanInTableComp.setValue(currentTableCompValue);
				tableCell[i] = new TableCell(view, getFuncColWidth(func), heigh);
				compList.add(scanInTableComp);
				scanInTableComp.getCurrentButton().setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						currentCompIndex = compList.indexOf(scanInTableComp);
						currentScanInTableComp = scanInTableComp;
						clickScan(func);
					}
				});
				if (keyFunc != null && func.getIsEdit() == Func.IS_N) {
					scanInTableComp.setIsEnable(false);
				}
			} else if (func.getType() == Func.TYPE_SCAN_INPUT) {// 扫描
				final ScanInTableComp scanInTableComp = new ScanInTableComp(mContext, func);
				scanInTableComp.compRow = compRow;
				View view = scanInTableComp.getObject();
				scanInTableComp.setValue(currentTableCompValue);
				tableCell[i] = new TableCell(view, getFuncColWidth(func), heigh);
				compList.add(scanInTableComp);
				scanInTableComp.getCurrentButton().setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						currentCompIndex = compList.indexOf(scanInTableComp);
						scanInputCode = new ScanInputCodeTable(mContext, scanInTableComp.getFunc(), null);
						scanInputCode.setProductCodeListener(TableCompView.this);
						scanInputCode.setProductTableType(ProductCode.TABLE_TYPE_TABLE);
						scanInputCode.setTableRow(scanInTableComp.compRow);
						scanInputCode.scanInputCodeInTable(scanInTableComp, scanInTableComp.value);
					}
				});
				if (keyFunc != null && func.getIsEdit() == Func.IS_N) {
					scanInTableComp.setIsEnable(false);
				}
			} else if (func.getType() == Func.TYPE_PRODUCT_CODE) {// 串码
				final ProductCodeInTableComp productCodeInTableComp = new ProductCodeInTableComp(mContext, func);
				productCodeInTableComp.compRow = compRow;
				View view = productCodeInTableComp.getObject();
				tableCell[i] = new TableCell(view, getFuncColWidth(func), heigh);
				productCodeInTableComp.setValue(currentTableCompValue);
				compList.add(productCodeInTableComp);
				productCodeInTableComp.getCurrentButton().setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						currentCompIndex = compList.indexOf(productCodeInTableComp);
						productCode = new ProductCodeTable(mContext, func, null);
						productCode.setProductCodeListener(TableCompView.this);
						productCode.setProductTableType(ProductCode.TABLE_TYPE_TABLE);
						productCode.setTableRow(productCodeInTableComp.compRow);
						productCode.productCodeInTable(productCodeInTableComp, productCodeInTableComp.value);
					}
				});
				if (keyFunc != null && func.getIsEdit() == Func.IS_N) {
					productCodeInTableComp.setIsEnable(false);
				}
				String compare_rule_ids = rowData.get("compare_rule_ids");
				String compare_content = rowData.get("compare_content");
				productCodeInTableComp.setCompareRuleIds(compare_rule_ids);
				productCodeInTableComp.setCompareContent(compare_content);

			} else if (func.getType() == Func.TYPE_CAMERA || func.getType() == Func.TYPE_CAMERA_HEIGHT
					|| func.getType() == Func.TYPE_CAMERA_MIDDLE || func.getType() == Func.TYPE_CAMERA_CUSTOM) {// 拍照
				final PhotoInTableComp photoInTableComp = new PhotoInTableComp(mContext, func);
				photoInTableComp.compRow = compRow;
				View view = photoInTableComp.getObject();
				if (!TextUtils.isEmpty(currentTableCompValue) && currentTableCompValue.endsWith(".jpg")
						&& !isUseStencil) {// 不是空并且不是模版的情况
					photoInTableComp.setValue(currentTableCompValue);
					photoInTableComp.setName(PublicUtils.getResourceString(getContext(),R.string.al_take_phpto));
					final String imageUri="file://"+Constants.SDCARD_PATH+currentTableCompValue;
				
					ImageView currentImageView = photoInTableComp.getCurrentImageView();
//					ImageLoader.getInstance().displayImage(imageUri,currentImageView );
					Bitmap bitmap=BitmapFactory.decodeFile(Constants.SDCARD_PATH+currentTableCompValue);
			
					currentImageView.setImageBitmap(bitmap);
					currentImageView.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
						 int submitId1 = submitDB.selectSubmitIdNotCheckOut(planId, wayId, storeId, targetId, targetType,
								Submit.STATE_NO_SUBMIT);
						 int subId=0;
					   if (isLink) {
						   subId = new SubmitItemTempDB(mContext).findSubmitItem(submitId1,funcTable.getFuncId()+"").getId();
					    }else{
						
						 subId=new SubmitItemDB(mContext).getItemId(submitId1, funcTable.getFuncId());
					    }
						Intent intenta =new Intent(mContext, ShowImageTableActivity.class);
						intenta.putExtra("imageUrl",imageUri.substring(imageUri.lastIndexOf("/")+1));
						intenta.putExtra("imageName", func.getName());
						if(func!=null){
							intenta.putExtra("submitItemId",subId);
							intenta.putExtra("PreviewTable", true);//是表格里的图片
						}
						if(isLink){
							intenta.putExtra("isLink", true);
						}
	                     mContext.startActivity(intenta);
	                     saveTableClickListener.clickPosition(positionId);
						}
					});
					
				}
				tableCell[i] = new TableCell(view, getFuncColWidth(func), heigh);
				compList.add(photoInTableComp);
				photoInTableComp.getCurrentButton().setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						currentCompIndex = compList.indexOf(photoInTableComp);
						currentPhotoInTableComp = photoInTableComp;
						fileName = System.currentTimeMillis() + ".jpg";
						int photoFlg = func.getPhotoFlg();
						if (photoFlg == 2) {
							new PhotoPopupWindow(mContext, currentPhotoInTableComp, fileName).show(null);
						} else {
							currentPhotoInTableComp.startPhotoTable(fileName);
						}
					}
				});
			
				
				if (keyFunc != null && func.getIsEdit() == Func.IS_N) {
					photoInTableComp.setIsEnable(false);
				}
				
			} else {
				JLog.d(TAG, "currentType==>" + func.getType());
			}
			i++;
		}
		View rowView = getTableComp(new TableRow(tableCell), false);
		delImg.getView().setTag(rowView);
		delImg.getView().requestFocus();
		ArrayList<Component> subList = new ArrayList<Component>();
		for (int j = compList.size() - tableLen; j < compList.size(); j++) {
			subList.add(compList.get(j));
		}
		rowView.setTag(subList);
		rowMap.put(String.valueOf(compRow), subList);
		delImg.getView().setOnClickListener(onTableRowClick);
		return rowView;
		// ll_tableContent.addView(rowView);
	}

	/**
	 * 选择组件
	 */
	private Component selectComp(Func func) {

		switch (func.getType()) {
		case Func.TYPE_SELECTCOMP:// 单选下拉
			return getSelectComp(func);// 下拉组件
		case Func.TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY_COMP:// 模糊搜索其他
			return getFuzzySingleChoiceSpinner(func);
		case Func.TYPE_SELECT_OTHER:// 单选其他
			return getOtherSelectComp(func);
		case Func.TYPE_MULTI_CHOICE_FUZZY_QUERY_COMP:// 模糊搜索多选
			return getFuzzyMultiChoiceSpinner(func);
		case Func.TYPE_MULTI_CHOICE_SPINNER_COMP:// 多选
			return getMultiChoiceSpinner(func);
		case Func.TYPE_SINGLE_CHOICE_FUZZY_QUERY_COMP:// 模糊搜索单选
			return getFuzzySingleChoiceSpinner(func);
		case Func.TYPE_TEXTCOMP:
			return getTextComp(func);// 文本组件
		case Func.TYPE_EDITCOMP:
			return getEditComp(func);// 输入组件
		default:
			return getTextComp(func);// 文本组件 ;
		}
	}

	/**
	 * 获取TextView
	 */
	private Component getEditComp(Func func) {
		EditComp editComp = new EditComp(mContext, func, null);
		editComp.compRow = compRow;
		compList.add(editComp);
		return editComp;
	}

	/**
	 * 获取TextView
	 */
	private Component getTextComp(Func func) {
		TextComp textComp = new TextComp(mContext, func, null);
		textComp.compRow = compRow;
		compList.add(textComp);
		return textComp;
	}

	/**
	 * spinner级联关系
	 */
	private Component getSelectComp(Func func) {

		SpinnerComp select = new SpinnerComp(mContext, func, chooseMapBundle.getString(func.getFuncId() + ""),
				orgMapBundle);
		select.currentLat = c_lat;
		select.currentLon = c_lon;
		controlNextAbsSelectComp(select, func);

		select.setInTable(true);
		select.setInitData();
		select.compRow = compRow;
		compList.add(select);
		// 赋值
		return select;
	}

	/**
	 * 模糊搜索其他
	 * 
	 * @param func
	 *            当前的控件
	 * @return
	 */
	private Component getOtherSelectComp(Func func) {

		SingleChoiceSpinnerComp singleChoiceSpinnerComp = new SingleChoiceSpinnerComp(mContext, func,
				chooseMapBundle.getString(func.getFuncId() + ""), orgMapBundle);
		singleChoiceSpinnerComp.currentLat = c_lat;
		singleChoiceSpinnerComp.currentLon = c_lon;
		controlNextAbsSelectComp(singleChoiceSpinnerComp, func);

		singleChoiceSpinnerComp.setInTable(true);
		singleChoiceSpinnerComp.setInitData();
		singleChoiceSpinnerComp.compRow = compRow;
		compList.add(singleChoiceSpinnerComp);
		return singleChoiceSpinnerComp;
	}

	/**
	 * 模糊多选下拉框
	 * 
	 * @param func
	 * @return
	 */
	private Component getFuzzyMultiChoiceSpinner(Func func) {
		FuzzyMultiChoiceSpinnerComp fuzzyMultiChoiceSpinnerComp = new FuzzyMultiChoiceSpinnerComp(mContext, func,
				chooseMapBundle.getString(func.getFuncId() + ""), orgMapBundle);
		fuzzyMultiChoiceSpinnerComp.currentLat = c_lat;
		fuzzyMultiChoiceSpinnerComp.currentLon = c_lon;
		controlNextAbsSelectComp(fuzzyMultiChoiceSpinnerComp, func);

		fuzzyMultiChoiceSpinnerComp.setInTable(true);
		fuzzyMultiChoiceSpinnerComp.setInitData();
		fuzzyMultiChoiceSpinnerComp.compRow = compRow;
		compList.add(fuzzyMultiChoiceSpinnerComp);
		return fuzzyMultiChoiceSpinnerComp;
	}

	/**
	 * 多选下拉框
	 * 
	 * @param func
	 * @return
	 */
	private Component getMultiChoiceSpinner(Func func) {
		MultiChoiceSpinnerComp multiChoiceSpinnerComp = new MultiChoiceSpinnerComp(mContext, func,
				chooseMapBundle.getString(func.getFuncId() + ""), orgMapBundle);
		multiChoiceSpinnerComp.currentLat = c_lat;
		multiChoiceSpinnerComp.currentLon = c_lon;
		controlNextAbsSelectComp(multiChoiceSpinnerComp, func);

		multiChoiceSpinnerComp.setInTable(true);
		multiChoiceSpinnerComp.setInitData();
		multiChoiceSpinnerComp.compRow = compRow;
		compList.add(multiChoiceSpinnerComp);
		return multiChoiceSpinnerComp;
	}

	/**
	 * 模糊单选下拉框
	 * 
	 * @param func
	 * @return
	 */
	private Component getFuzzySingleChoiceSpinner(Func func) {
		FuzzySingleChoiceSpinnerComp fuzzySingleChoiceSpinnerComp = new FuzzySingleChoiceSpinnerComp(mContext, func,
				chooseMapBundle.getString(func.getFuncId() + ""), orgMapBundle);
		fuzzySingleChoiceSpinnerComp.currentLat = c_lat;
		fuzzySingleChoiceSpinnerComp.currentLon = c_lon;
		controlNextAbsSelectComp(fuzzySingleChoiceSpinnerComp, func);
		fuzzySingleChoiceSpinnerComp.setInTable(true);// 设置在表格里
		fuzzySingleChoiceSpinnerComp.setInitData();// 设置初始值
		fuzzySingleChoiceSpinnerComp.compRow = compRow;
		compList.add(fuzzySingleChoiceSpinnerComp);
		return fuzzySingleChoiceSpinnerComp;
	}

	/**
	 * 把下级对象付给上级
	 * 
	 * @param select
	 * @param func
	 */
	private void controlNextAbsSelectComp(AbsSelectComp select, Func func) {
		if (tempCompoMap.containsKey(func.getFuncId())) { // 是不是下级
			AbsSelectComp parentSelect = (AbsSelectComp) tempCompoMap.get(func.getFuncId());
			select.queryWhere = getQueryWhere(parentSelect);
			parentSelect.setNextComp(select);// 将自己添加到父级中
		}
		if (func.getNextDropdown() != null) { // 是不是级联中的父级
			tempCompoMap.put(func.getNextDropdown(), select);
		}
	}

	/**
	 * 给下级付上级的查询条件
	 * 
	 * @param select
	 * @return
	 */
	private String getQueryWhere(AbsSelectComp select) {
		StringBuffer queryWhere = new StringBuffer();
		if (!TextUtils.isEmpty(select.value)) {
			AbsSelectComp parentSelect = (AbsSelectComp) tempCompoMap.get(select.getFunc().getFuncId());
			if (parentSelect != null) {
				Func parentSelectFunc = select.getFunc();
				String s = getQueryWhere(parentSelect);
				if (!TextUtils.isEmpty(s)) {
					if (isOther(select.value) || parentSelectFunc.getType() == Func.TYPE_MULTI_CHOICE_SPINNER_COMP
							|| parentSelectFunc.getType() == Func.TYPE_MULTI_CHOICE_FUZZY_QUERY_COMP) {// 如果是数字的话
						queryWhere.append(s).append("@");
					} else {
						queryWhere.append("-1").append("@");
					}
				}
			}
			Func func = select.getFunc();
			if (isOther(select.value) || func.getType() == Func.TYPE_MULTI_CHOICE_SPINNER_COMP
					|| func.getType() == Func.TYPE_MULTI_CHOICE_FUZZY_QUERY_COMP) {// 因为多选的时候值是多个的时候就无法验证数字
																					// 且多选不包含其他类型
				queryWhere.append(select.value);
			} else {
				queryWhere.append("-1");
			}
		}

		return queryWhere.length() > 0 ? queryWhere.toString() : null;
	}

	/**
	 * 
	 * @param tableRow表的每一行
	 * @return一个表格样式的view
	 */
	private View getTableComp(TableRow tableRow, boolean isTitle) {
		LinearLayout rootLinearLayout = new LinearLayout(mContext);
		rootLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
		int tableSize = tableRow.getSize();
		if (bundle.getInt("targetId") == 11793) {// TODO临时写死
			tableSize = tableRow.getSize() - 1;
		}
		for (int i = 0; i < tableSize; i++) {// 逐个格单位添加到行
			TableCell tableCell = tableRow.getCellValue(i);
			if (tableCell == null) {// 隐藏域的时候tableCell是空
				continue;
			}
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(tableCell.width, tableCell.height);// 指定的大小设置空间
			layoutParams.setMargins(1, 1, 1, 1);// 预留空地建造边框
			LinearLayout l = new LinearLayout(mContext);
			l.setGravity(Gravity.CENTER_VERTICAL);
			if (isTitle) {
				l.setBackgroundColor(getResources().getColor(R.color.title_bg_form));
			} else {
				l.setBackgroundColor(getResources().getColor(R.color.table_bg));
			}
			View viewCell = new View(mContext);
			viewCell = (View) tableCell.value;
			viewCell.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT));
			l.addView(viewCell);
			rootLinearLayout.addView(l, layoutParams);
		}
		// rootLinearLayout.setBackgroundColor(getResources().getColor(R.color.table_line));
		rootLinearLayout.setBackgroundColor(getResources().getColor(R.color.form_line));
		return rootLinearLayout;
	}

	/**
	 * TableRow 实现表格的行
	 */
	private class TableRow {
		private TableCell[] cell;

		public TableRow(TableCell[] cell) {
			this.cell = cell;
		}

		public int getSize() {
			return cell.length;
		}

		public TableCell getCellValue(int index) {
			if (index >= cell.length)
				return null;
			return cell[index];
		}
	}

	/**
	 * TableCell 实现表格的格单位
	 */
	private class TableCell {
		public Object value;
		public int width;
		public int height;

		public TableCell(Object value, int width, int height) {
			this.value = value;
			this.width = width;
			this.height = height;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_add:
			// addTableRow();
			if (isAddDel) {
				Message msg = handler.obtainMessage();
				msg.what = 1;
				msg.obj = addTableRow("");
				handler.sendMessage(msg);
				ToastOrder.makeText(mContext, R.string.add_row, ToastOrder.LENGTH_SHORT).show();
			} else {
				ToastOrder.makeText(mContext, R.string.un_pre_add, ToastOrder.LENGTH_SHORT).show();
			}
			break;
		case R.id.btn_save:
			if (isCompletion()) {// 首先检测是否完整操作
				if (isPass()) {// 验证类型是否通过
					if (codeControl()) {
						try {
							if (compList.isEmpty()) {// 如果是空则删除存储的该条记录
								SubmitItemDB submitItemDB = new SubmitItemDB(mContext);
								SubmitItem item = submitItemDB.findSubmitItemByParamNameAndType(
										String.valueOf(funcTable.getFuncId()), funcTable.getType());
								if (item != null) {
									submitItemDB.deleteSubmitItem(item);
								}
								// this.finish();
							} else {
								if (ggmmCheck.isCheckGGMM(funcTable.getTableId())) {// GGMM先做检测
									ggmmCheck.checkGGMM(funcTable.getTableId(), getTableValue());
								} else {
									save();// 保存
								}
							}
							// this.finish();
						} catch (DataComputeException e) {
							ToastOrder
									.makeText(mContext.getApplicationContext(), e.getMessage(), ToastOrder.LENGTH_SHORT)
									.show();
							JLog.e(e);
						} catch (NumberFormatException e) {
							ToastOrder.makeText(mContext, R.string.toast_one1, ToastOrder.LENGTH_SHORT).show();
							JLog.e(e);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			break;
		case R.id.btn_stencil:// 模板
			showStencil();
			break;

		}
	}

	/**
	 * 弹出表格模板对话框
	 */
	private void showStencil() {
		View contentView = View.inflate(mContext, R.layout.table_stencil_prompt, null);
		final PopupWindow popupWindow = new PopupWindow(contentView, WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT, true);
		popupWindow.setAnimationStyle(R.style.popupwindow);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());// 响应返回键必须的语句
		int high = ((Activity) mContext).getWindowManager().getDefaultDisplay().getHeight();
		popupWindow.showAsDropDown(btn_stencil, 0, -(high / 4 + 15));
		contentView.findViewById(R.id.table_stencil_use).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
				useStencil();
			}
		});
		contentView.findViewById(R.id.table_stencil_save).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
				try {
					saveStencil();
				} catch (DataComputeException e) {
					ToastOrder.makeText(mContext.getApplicationContext(), e.getMessage(), ToastOrder.LENGTH_SHORT)
							.show();
					JLog.e(e);
				} catch (NumberFormatException e) {
					ToastOrder.makeText(mContext, R.string.toast_one1, ToastOrder.LENGTH_SHORT).show();
					JLog.e(e);
				}
			}
		});

	}

	// 保存模版
	private void saveStencil() throws NumberFormatException, DataComputeException {
		TableStencilDB tableStencilDB = new TableStencilDB(mContext);
		int targetId = bundle.getInt("targetId");
		int funcId = funcTable.getFuncId();
		TableStencil tableStencil = tableStencilDB.findTableStencilByTargetIdAndFuncId(targetId, funcId);
		if (tableStencil == null) {
			TableStencil t = new TableStencil();
			t.setFuncId(funcId);
			t.setTargetId(targetId);
			t.setValue(getTableValue());
			tableStencilDB.insertTableStencil(t);
			JLog.d(TAG, "保存模版数据：" + t.getValue());
			ToastOrder.makeText(mContext, R.string.save_sucessful, ToastOrder.LENGTH_SHORT).show();
		} else {
			tableStencil.setValue(getTableValue());
			JLog.d(TAG, tableStencil.getValue());
			stencilDialog(tableStencil);
		}
	}

	// 使用模版
	private void useStencil() {
		TableStencilDB tableStencilDB = new TableStencilDB(mContext);
		int targetId = bundle.getInt("targetId");
		int funcId = funcTable.getFuncId();
		TableStencil tableStencil = tableStencilDB.findTableStencilByTargetIdAndFuncId(targetId, funcId);
		if (tableStencil == null) {
			ToastOrder.makeText(mContext, R.string.save_sucessful1, ToastOrder.LENGTH_SHORT).show();
		} else {
			// 添加模版数据
			isUseStencil = true;
			final String tableValue = tableStencil.getValue();
			JLog.d(TAG, "模版数据：" + tableValue);
			new Thread() {
				public void run() {
					Looper.prepare();
					// 删除已有的数据
					Message delMsg = handler.obtainMessage(4);
					handler.sendMessage(delMsg);
					compList.clear();
					List<HashMap<String, String>> tableList = new TableParse().parseJason(tableValue);
					if (!tableList.isEmpty()) {
						for (int j = 0; j < tableList.size(); j++) {
							HashMap<String, String> table = tableList.get(j);// 一个table表示一行
							Message msg = handler.obtainMessage(1);
							msg.obj = addTableRow(table);// 每循环一次添加一行
							handler.sendMessage(msg);
						}
					}
				};
			}.start();
		}
	}

	/**
	 * 删除一行dialog
	 * 
	 *
	 *            要删除的行
	 * @return dialog
	 */
	private void stencilDialog(final TableStencil tableStencil) {
		final Dialog deleteDialog = new Dialog(mContext, R.style.transparentDialog);
		View view = View.inflate(mContext, R.layout.table_stencil_dialog, null);
		Button confirm = (Button) view.findViewById(R.id.btn_stencil_confirm);
		Button cancel = (Button) view.findViewById(R.id.btn_stencil_cancel);
		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				deleteDialog.dismiss();
				TableStencilDB tableStencilDB = new TableStencilDB(mContext);
				tableStencilDB.updateTableStencil(tableStencil);
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				deleteDialog.dismiss();
			}
		});
		deleteDialog.setContentView(view);
		deleteDialog.setCancelable(false);
		deleteDialog.show();
	}

	/**
	 * 获取表格中控件的json值
	 * 
	 * @return
	 * @throws NumberFormatException
	 * @throws DataComputeException
	 */
	private List<HashMap<String, String>> tableList;

	private String getTableValue() throws NumberFormatException, DataComputeException {
		int compSize = compList.size();// 要保存的控件的个数
		if (tableList == null) {
			tableList = new ArrayList<HashMap<String, String>>();// 存放每行的值的集合
		} else {
			tableList.clear();
		}
		HashMap<String, String> table = null; // 存放一行的控件的值 key是控件的funcID
												// 值是控件要保存的值
		for (int i = 0; i < compSize; i += tableLen) {
			table = new HashMap<String, String>();
			int j = 0;// 每一行可见func的索引
			for (Func currentFunc : funcList) {
				if (currentFunc.getType() == Func.TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY_COMP
						|| currentFunc.getType() == Func.TYPE_SELECT_OTHER) {
					if (compList.get(i + j).value == null) {
						compList.get(i + j).value = "";
					}
					if (!TextUtils.isEmpty(compList.get(i + j).value) && !isInteger(compList.get(i + j))) {// 如果是其他类型的下拉框
																											// 不是数字说明选了其他
						table.put(String.valueOf(currentFunc.getFuncId()), "-1");// 先存-1
						table.put(currentFunc.getFuncId() + "_other", compList.get(i + j).value);// 再存值
																									// 此时的key
																									// 是控件的ID加_other
					} else {// 没有选其他的情况
						table.put(String.valueOf(currentFunc.getFuncId()), compList.get(i + j).value);
					}
				} else if (currentFunc.getType() == Func.TYPE_TEXTCOMP) {// 表格自动计算
					table.put(String.valueOf(currentFunc.getFuncId()),
							new FuncTreeForTable(mContext, currentFunc, compList.subList(i, i + tableLen)).result);
				} else if (currentFunc.getType() == Func.TYPE_PRODUCT_CODE) {
					ProductCodeInTableComp productCodeInTableComp = (ProductCodeInTableComp) compList.get(i + j);
					table.put(String.valueOf(currentFunc.getFuncId()), productCodeInTableComp.value);
					table.put("compare_content", productCodeInTableComp.getCompareContent());
					table.put("compare_rule_ids", productCodeInTableComp.getCompareRuleIds());

				} else if (currentFunc.getType() == Func.TYPE_HIDDEN) {// 表格中的隐藏域类型
					String hiddenValue = "";
					if (currentFunc.getDefaultType() != null
							&& (currentFunc.getDefaultType() == Func.DEFAULT_TYPE_COMPUTER)) {
						hiddenValue = new FuncTreeForTable(mContext, currentFunc,
								compList.subList(i, i + tableLen)).result;// 表格隐藏类型自动计算
					} else {
						hiddenValue = getHiddenValue(currentFunc);// 获取表格中隐藏域的值
					}
					if (TextUtils.isEmpty(hiddenValue) && !TextUtils.isEmpty(compList.get(i + j).value)) {
						hiddenValue = compList.get(i + j).value;
					}
					table.put(String.valueOf(currentFunc.getFuncId()), hiddenValue);// 将值放到map中
				} else {
					table.put(String.valueOf(currentFunc.getFuncId()), compList.get(i + j).value);
				}
				j++;
			}
			tableList.add(table);
		}
		String jason = new TableParse().toJason(tableList);
		return jason;
	}

	/**
	 * 保存表格中的数据
	 * 
	 * @throws DataComputeException
	 * @throws NumberFormatException
	 * @throws Exception
	 *             保存的时候将要提交的值转化成json的时候会抛异常
	 */

	private void save() throws NumberFormatException, DataComputeException, Exception {

		SubmitItem submitItem = new SubmitItem();
		submitItem.setTargetId(targetId+"");
		String jason = getTableValue();
		submitItem.setParamName(String.valueOf(funcTable.getFuncId()));
		submitItem.setParamValue(jason);
		submitItem.setType(funcTable.getType());
		submitItem.setOrderId(funcTable.getId());
		submitItem.setIsCacheFun(funcTable.getIsCacheFun());
		int submitId = submitDB.selectSubmitIdNotCheckOut(planId, wayId, storeId, targetId, targetType,
				Submit.STATE_NO_SUBMIT);
		if (submitId != Constants.DEFAULTINT) {// submitId！=0表示没有该条记录这时候要查询看是插入数据还是修改数据
			submitItem.setSubmitId(submitId);
			boolean isHas = false;
			if (isLink) {// 超链接查询临时表
				isHas = new SubmitItemTempDB(mContext).findIsHaveParamName(submitItem.getParamName(), submitId);
			} else {
				isHas = submitItemDB.findIsHaveParamName(submitItem.getParamName(), submitId);
			}

			if (isHas) {// isHas=true表示有该条记录，此时修改
				if (isLink) {// 超链接修改临时表
					new SubmitItemTempDB(mContext).updateSubmitItem(submitItem);
				} else {// 修改提交表
					submitItemDB.updateSubmitItem(submitItem,false);
				}
			} else {
				if (isLink) {
					new SubmitItemTempDB(mContext).insertSubmitItem(submitItem);// 没有记录，插入
				} else {
					submitItemDB.insertSubmitItem(submitItem,false);// 没有记录，插入
				}
			}
		} else {
			Submit submit = new Submit();
			if (planId != 0) {// 拜访
				submit.setPlanId(planId);
				submit.setWayId(wayId);
				submit.setStoreId(storeId);
				submit.setStoreName(storeName);
				submit.setWayName(wayName);

			}
			submit.setTargetid(targetId);
			submit.setTargetType(targetType);
			submit.setState(Submit.STATE_NO_SUBMIT);
			if (modType != 0) {
				submit.setModType(modType);
			}
			submit.setMenuId(menuId);
			submit.setMenuType(menuType);
			submit.setMenuName(menuName);
			submitDB.insertSubmit(submit);
			int currentsubmitId = submitDB.selectSubmitId(planId, wayId, storeId, targetId, targetType);
			submitItem.setSubmitId(currentsubmitId);
			if (isLink) {
				new SubmitItemTempDB(mContext).insertSubmitItem(submitItem);
			} else {
				submitItemDB.insertSubmitItem(submitItem,false);
			}
		}

		if (isTableHistory) {// 如果是表格历史信息查询的情况直接提交
			int currentsubmitId = submitDB.selectSubmitId(planId, wayId, storeId, targetId, targetType);
			Submit sb = submitDB.findSubmitById(currentsubmitId);
			tableProductCodeControl(funcTable, funcList, sb);
			sb.setState(Submit.STATE_SUBMITED);

			// 提交表格中的图片
			String imageList = getTableImageList(tableList, funcList);// 获取表格中图片的集合
			if (!TextUtils.isEmpty(imageList)) {// 如果表格中有照片并且拍照了
				submitItem.setNote(imageList);// 将照片集合存到数据库
				submitItemDB.updateSubmitItem(submitItem,false);
			}
			submitDB.updateState(sb);
			// SubmitManager.getInstance(this).submitData();
			SubmitManager.getInstance(mContext).submitData(isNoWait, TableCompView.this);
			JLog.d(TAG, "表格历史数据提交");

		} else {
			// this.finish();
			Toast.makeText(mContext, R.string.save_sucessful3, Toast.LENGTH_SHORT).show();
			saveTableClickListener.clickPosition(positionId);
		}
		JLog.d(TAG, "TableJason==>" + jason);
	}
  
	private void savePhoto() throws NumberFormatException, DataComputeException, Exception {

		SubmitItem submitItem = new SubmitItem();
		submitItem.setTargetId(targetId+"");
		String jason = getTableValue();
		submitItem.setParamName(String.valueOf(funcTable.getFuncId()));
		submitItem.setParamValue(jason);
		submitItem.setType(funcTable.getType());
		submitItem.setOrderId(funcTable.getId());
		submitItem.setIsCacheFun(funcTable.getIsCacheFun());
		int submitId = submitDB.selectSubmitIdNotCheckOut(planId, wayId, storeId, targetId, targetType,
				Submit.STATE_NO_SUBMIT);
		if (submitId != Constants.DEFAULTINT) {// submitId！=0表示没有该条记录这时候要查询看是插入数据还是修改数据
			submitItem.setSubmitId(submitId);
			boolean isHas = false;
			if (isLink) {// 超链接查询临时表
				isHas = new SubmitItemTempDB(mContext).findIsHaveParamName(submitItem.getParamName(), submitId);
			} else {
				isHas = submitItemDB.findIsHaveParamName(submitItem.getParamName(), submitId);
			}

			if (isHas) {// isHas=true表示有该条记录，此时修改
				if (isLink) {// 超链接修改临时表
					new SubmitItemTempDB(mContext).updateSubmitItem(submitItem);
				} else {// 修改提交表
					submitItemDB.updateSubmitItem(submitItem,false);
				}
			} else {
				if (isLink) {
					new SubmitItemTempDB(mContext).insertSubmitItem(submitItem);// 没有记录，插入
				} else {
					submitItemDB.insertSubmitItem(submitItem,false);// 没有记录，插入
				}
			}
		} else {
			Submit submit = new Submit();
			if (planId != 0) {// 拜访
				submit.setPlanId(planId);
				submit.setWayId(wayId);
				submit.setStoreId(storeId);
				submit.setStoreName(storeName);
				submit.setWayName(wayName);

			}
			submit.setTargetid(targetId);
			submit.setTargetType(targetType);
			submit.setState(Submit.STATE_NO_SUBMIT);
			if (modType != 0) {
				submit.setModType(modType);
			}
			submit.setMenuId(menuId);
			submit.setMenuType(menuType);
			submit.setMenuName(menuName);
			submitDB.insertSubmit(submit);
			int currentsubmitId = submitDB.selectSubmitId(planId, wayId, storeId, targetId, targetType);
			submitItem.setSubmitId(currentsubmitId);
			if (isLink) {
				new SubmitItemTempDB(mContext).insertSubmitItem(submitItem);
			} else {
				submitItemDB.insertSubmitItem(submitItem,false);
			}
		}

		if (isTableHistory) {// 如果是表格历史信息查询的情况直接提交
			int currentsubmitId = submitDB.selectSubmitId(planId, wayId, storeId, targetId, targetType);
			Submit sb = submitDB.findSubmitById(currentsubmitId);
			tableProductCodeControl(funcTable, funcList, sb);
			sb.setState(Submit.STATE_SUBMITED);

			// 提交表格中的图片
			String imageList = getTableImageList(tableList, funcList);// 获取表格中图片的集合
			if (!TextUtils.isEmpty(imageList)) {// 如果表格中有照片并且拍照了
				submitItem.setNote(imageList);// 将照片集合存到数据库
				submitItemDB.updateSubmitItem(submitItem,false);
			}
			submitDB.updateState(sb);
			// SubmitManager.getInstance(this).submitData();
			SubmitManager.getInstance(mContext).submitData(isNoWait, TableCompView.this);
			JLog.d(TAG, "表格历史数据提交");

		} else {
			// this.finish();
//			Toast.makeText(mContext, "保存成功", Toast.LENGTH_SHORT).show();
		
		}
		JLog.d(TAG, "TableJason==>" + jason);
	}
	
	
	
	/**
	 * 表格中串码控制
	 * 
	 * @param tableFunc
	 *            表格本身控件
	 * @param funcTableList
	 *            表格关联的控件
	 */
	private void tableProductCodeControl(Func tableFunc, List<Func> funcTableList, Submit submit) {
		for (int i = 0; i < funcTableList.size(); i++) {
			Func func = funcTableList.get(i);
			if (func.getType() == Func.TYPE_PRODUCT_CODE) {
				StringBuffer codeUpdateTabBuffer = null;
				StringBuffer upCtrlTableBuffer = null;
				// 串码更新
				String codeUpdate = func.getCodeUpdate();
				if (!TextUtils.isEmpty(codeUpdate)) {
					String submitUpdateCode = submit.getCodeUpdateTab();
					if (TextUtils.isEmpty(submitUpdateCode)) {
						codeUpdateTabBuffer = new StringBuffer();
						codeUpdateTabBuffer.append(",").append(tableFunc.getTableId()).append("_").append(codeUpdate)
								.append(",");
					} else {// 有可能是多个表格
						codeUpdateTabBuffer = new StringBuffer(submitUpdateCode);
						codeUpdateTabBuffer.append(tableFunc.getTableId()).append("_").append(codeUpdate).append(",");
					}
					submit.setCodeUpdateTab(codeUpdateTabBuffer.toString());
				}
				// 串码控制
				if (upCtrlTableBuffer == null) {// 不显示不控制的串码
					upCtrlTableBuffer = new StringBuffer();
					upCtrlTableBuffer.append(String.valueOf(tableFunc.getFuncId())).append("_");
					upCtrlTableBuffer.append(String.valueOf(tableFunc.getTableId())).append("_");
					upCtrlTableBuffer.append(String.valueOf(func.getFuncId())).append("_");
					upCtrlTableBuffer.append(func.getCodeControl());
					submit.setUpCtrlTable(upCtrlTableBuffer.toString());
				}
				submitDB.updateSubmit(submit);
				break;
			}
		}
	}

	/**
	 * 获取表格中图片名字的LIST
	 * 
	 * @param tablelist
	 *            表格中所有行的数据的集合
	 * @param funcTableList
	 *            表格中所有控件的集合
	 * @return 所有图片的名字的集合
	 */
	private String getTableImageList(List<HashMap<String, String>> tablelist, List<Func> funcTableList) {
		StringBuffer sList = new StringBuffer();
		for (int i = 0; i < tablelist.size(); i++) { // 循环行数
			HashMap<String, String> table = tablelist.get(i);
			for (int j = 0; j < funcTableList.size(); j++) {
				Func func = funcTableList.get(j); // 得到该列的Func
				if (func.getType() == Func.TYPE_CAMERA || func.getType() == Func.TYPE_CAMERA_HEIGHT
						|| func.getType() == Func.TYPE_CAMERA_MIDDLE || func.getType() == Func.TYPE_CAMERA_CUSTOM) {// 拍照类型
					String imageName = table.get(func.getFuncId() + "");
					if (!TextUtils.isEmpty(imageName) && imageName.endsWith(".jpg")) {
						sList.append(",").append(imageName);
					}
				}
			}

		}
		if (sList.length() > 0) {
			return sList.substring(1);
		} else {
			return "";
		}
	}

	/**
	 * 默认值
	 * 
	 * @param func
	 *            当前控件
	 * @return 当前隐藏域的默认值
	 */
	public String getHiddenValue(Func func) {
		if (func.getDefaultType() != null) {
			Calendar c = Calendar.getInstance();
			switch (func.getDefaultType()) {
			case Func.DEFAULT_TYPE_OTHER:// 取默认值
				return func.getDefaultValue();
			case Func.DEFAULT_TYPE_YEAR:// 返回当前年
				return c.get(Calendar.YEAR) + "";
			case Func.DEFAULT_TYPE_MONTH:// 返回当前月
				return (c.get(Calendar.MONTH) + 1) + "";
			case Func.DEFAULT_TYPE_DAY:// 返回当前日
				return (c.get(Calendar.DAY_OF_MONTH)) + "";
			case Func.DEFAULT_TYPE_DATE:// 返回当前日期包含时间
				return DateUtil.getCurDateTime();
			case Func.DEFAULT_TYPE_DATE_NO_TIME:// 返回当前日期不包含时间
				return DateUtil.getCurDate();
			case Func.DEFAULT_TYPE_TIME:// 返回当前时间
				return c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);
			case Func.DEFAULT_TYPE_HOURS:// 返回当前小时
				return c.get(Calendar.HOUR_OF_DAY) + "";
			case Func.DEFAULT_TYPE_MINUTE:// 返回当前分钟
				return c.get(Calendar.MINUTE) + "";
			case Func.DEFAULT_TYPE_USER:// 返回当前用户
				return SharedPreferencesUtil.getInstance(mContext).getUserName();// name修改成ID
																					// 代要求09_21号
			case Func.DEFAULT_TYPE_JOB_NUMBER:// 唯一工号
				return System.currentTimeMillis() + "";// 返回时间戳
			case Func.DEFAULT_TYPE_STARTDATE:// 开始时间
				return startTime;// 返回开始时间
			}
		}
		return "";
	}

	/**
	 * 初始化数据 1：如果数据库中有数据就根据数据库中的数据初始化页面 2：如果是sql类型的就根据sql传过来的值赋值
	 * isNetCanUse=false表示是sql网络不通的情况下跳转过来 3：
	 * keyFunc!=null说明是有主键，这时候要平铺，如果没有主键就直接添加一行，有主键的时候如果设置成不可编辑则不能点击操作
	 * 
	 */
	private Dialog locationLoadingDialog;

	private void initData() {
		if (isNeedLocation() && isNormal) {// 如果是拍照异常返回话就不需要再次定位了
			locationLoadingDialog = new MyProgressDialog(mContext, R.style.CustomProgressDialog,
					getResources().getString(R.string.waitForAMoment));
			locationLoadingDialog.show();
			// new
			// LocationFactory(TableCompActivity.this).startNewLocation(TableCompActivity.this,false);//不需要地址
			new LocationFactoy(mContext, this).startLocationHH();
		} else {
			loadUI();
		}
	}

	/**
	 * 加载表格UI
	 */
	private void loadUI() {
		new Thread() {
			public void run() {
				Looper.prepare();
				if (funcTable.getDefaultType() != null && funcTable.getDefaultType() == Func.DEFAULT_TYPE_SQL) {
					setSqlTypeTable();
				} else {
					setDefaultTypeTable();
				}
				Message msg = handler.obtainMessage(3);// 表格加载完成
				handler.sendMessage(msg);
			};
		}.start();
	}

	/**
	 * 非sql类型的表格
	 */
	private void setDefaultTypeTable() {
		if (isNormal) {
			SubmitItem item = null;
			if (isLink) {// 如果是超链接，查询临时表中的数据
				item = new SubmitItemTempDB(mContext).findSubmitItem(null, Submit.STATE_NO_SUBMIT, planId, wayId,
						storeId, targetId, targetType, String.valueOf(funcTable.getFuncId()));
			} else {// 不是超链接查询提交表中的数据
				item = new SubmitItemDB(mContext).findSubmitItem(null, Submit.STATE_NO_SUBMIT, planId, wayId, storeId,
						targetId, targetType, String.valueOf(funcTable.getFuncId()));
			}
			if (item != null) {
				String itemValue = item.getParamValue();// 将数据库中查出的值赋给当前组件
				List<HashMap<String, String>> tableList = new TableParse().parseJason(itemValue);
				for (int j = 0; j < tableList.size(); j++) {
					HashMap<String, String> table = tableList.get(j);// 一个table表示一行
					Message msg = handler.obtainMessage(1);
					msg.obj = addTableRow(table);// 每循环一次添加一行
					handler.sendMessage(msg);
				}
			} else {
				if (keyFunc != null) {// 如果有主键就平铺
					keyFuncDicList = new DictDB(mContext).findDictList(keyFunc.getDictTable(), keyFunc.getDictCols(),
							keyFunc.getDictDataId(), keyFunc.getDictOrderBy(), keyFunc.getDictIsAsc(), null, null,
							null);
					for (Dictionary d : keyFuncDicList) {
						Message msg = handler.obtainMessage(1);
						msg.obj = addTableRow(d.getDid());
						handler.sendMessage(msg);
					}
				} else {// 没有主键添加一行
					Message msg = handler.obtainMessage(1);
					msg.obj = addTableRow("");
					handler.sendMessage(msg);
				}
			}
		} else {
			String itemValue = unNormalCompData;
			List<HashMap<String, String>> tableList = new TableParse().parseJason(itemValue);
			for (int j = 0; j < tableList.size(); j++) {
				HashMap<String, String> table = tableList.get(j);// 一个table表示一行
				Message msg = handler.obtainMessage(1);
				msg.obj = addTableRow(table);// 每循环一次添加一行
				handler.sendMessage(msg);
			}
		}
	}

	/**
	 * sql查询类型的表格
	 */
	private void setSqlTypeTable() {
		String itemValue = defaultSql;// 将数据库中查出的值赋给当前组件
		if (isNormal) {
			if (TextUtils.isEmpty(itemValue)) {
				SubmitItem item = null;
				if (isLink) {// 如果表格是超链接中的控件就查询临时表中的值
					item = new SubmitItemTempDB(mContext).findSubmitItem(null, Submit.STATE_NO_SUBMIT, planId, wayId,
							storeId, targetId, targetType, String.valueOf(funcTable.getFuncId()));
				} else {
					item = new SubmitItemDB(mContext).findSubmitItem(null, Submit.STATE_NO_SUBMIT, planId, wayId,
							storeId, targetId, targetType, String.valueOf(funcTable.getFuncId()));
				}

				if (item != null) {
					itemValue = item.getParamValue();
				}
				isNeedAddData = false;// 此时拼KEY的时候不需要添加data_
			}
		} else {
			itemValue = unNormalCompData;
		}
		if (!isNetCanUse) {// 此时为空说说明是sql网络查询，但是网络不通isNetCanUse=false表示是网络不通的情况下跳转过来
			if (keyFunc != null) {// 如果有主键平铺
				keyFuncDicList = new DictDB(mContext).findDictList(keyFunc.getDictTable(), keyFunc.getDictCols(),
						keyFunc.getDictDataId(), keyFunc.getDictOrderBy(), keyFunc.getDictIsAsc(), null, null, null);
				for (Dictionary d : keyFuncDicList) {// 主键平铺
					Message msg = handler.obtainMessage(1);
					msg.obj = addTableRow(d.getDid());
					handler.sendMessage(msg);
				}
			} else {// 没有主键就只添加一行
				Message msg = handler.obtainMessage(1);
				msg.obj = addTableRow("");
				handler.sendMessage(msg);
			}
		} else {
			List<HashMap<String, String>> tableList = new TableParse().parseJason(itemValue);
			if (!tableList.isEmpty()) {
				for (int j = 0; j < tableList.size(); j++) {
					HashMap<String, String> table = tableList.get(j);// 一个table表示一行
					Message msg = handler.obtainMessage(1);
					msg.obj = addTableRow(table);// 每循环一次添加一行
					handler.sendMessage(msg);
				}
			}
		}

	}

	/**
	 * 判断是否操作完整
	 * 
	 * @return true 验证通过 false 验证不通过
	 */
	private boolean isCompletion() {
		boolean isCompletion = true;// 默认操作完整
		for (int i = 0; i < compList.size(); i++) {
			Component comp = compList.get(i);
			Func func = comp.compFunc;
			if (func.getIsEmpty() != null && func.getIsEmpty() == 1 && func.getType() != Func.TYPE_SELECTCOMP
					&& func.getType() != Func.TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY_COMP
					&& func.getType() != Func.TYPE_SELECT_OTHER
					&& func.getType() != Func.TYPE_SINGLE_CHOICE_FUZZY_QUERY_COMP
					&& func.getType() != Func.TYPE_MULTI_CHOICE_FUZZY_QUERY_COMP
					&& func.getType() != Func.TYPE_MULTI_CHOICE_SPINNER_COMP) {// 说明该项必须操作

				if (comp.isEmpty()) { // 如果该项为空
					isCompletion = false;// 有必须操作项没操作
					ToastOrder.makeText(mContext, func.getName() + PublicUtils.getResourceString(mContext,R.string.un_search_data2), ToastOrder.LENGTH_SHORT).show();
					break;
				}
			}
		}
		return isCompletion;
	}

	/**
	 * 根据验证类型检测输入的值是否正确
	 * 
	 * @param currentComponent
	 *            表格中的控件
	 * @param func
	 *            表格中控件的func
	 * @return true验证通过， false验证不通过
	 */
	public boolean checkType(Component currentComponent, Func func) {
		boolean flag = true;
		if (!TextUtils.isEmpty(currentComponent.value) && func.getCheckType() != null) {
			String name = func.getName();
			switch (func.getCheckType()) {
			case Func.CHECK_TYPE_FIXED_TELEPHONE:
				flag = currentComponent.isFixedTelephone();
				if (!flag) {
					ToastOrder.makeText(mContext, name +PublicUtils.getResourceString(mContext,R.string.input_corect_photo), ToastOrder.LENGTH_LONG).show();
				}
				break;
			case Func.CHECK_TYPE_IDNUMBER:
				flag = currentComponent.isIDNumber();
				if (!flag) {
					ToastOrder.makeText(mContext, name + PublicUtils.getResourceString(mContext,R.string.input_corect_photo1), ToastOrder.LENGTH_LONG).show();
				}
				break;
			case Func.CHECK_TYPE_MOBILE_TELEPHONE:
				flag = currentComponent.isMobileTelephone();
				if (!flag) {
					ToastOrder.makeText(mContext, name + PublicUtils.getResourceString(mContext,R.string.input_corect_photo), ToastOrder.LENGTH_LONG).show();
				}
				break;
			case Func.CHECK_TYPE_MAIL:
				flag = currentComponent.isMail();
				if (!flag) {
					ToastOrder.makeText(mContext, name + PublicUtils.getResourceString(mContext,R.string.reset_email), ToastOrder.LENGTH_LONG).show();
				}
				break;
			case Func.CHECK_TYPE_ZIPCODE:
				flag = currentComponent.isZipCode();
				if (!flag) {
					ToastOrder.makeText(mContext, name + PublicUtils.getResourceString(mContext,R.string.input_corect_photo2), ToastOrder.LENGTH_LONG).show();
				}
				break;
			case Func.CHECK_TYPE_NUMERIC:

				if (func.getDataType().intValue() == Func.DATATYPE_DECIMAL) {// 如果是小数
					flag = isNumeric(currentComponent);
					if (!flag) {
						ToastOrder.makeText(mContext, name + PublicUtils.getResourceString(mContext,R.string.input_corect_photo3), ToastOrder.LENGTH_LONG).show();
					}
				} else {
					flag = isInteger(currentComponent);
					if (!flag) {
						ToastOrder.makeText(mContext, name + PublicUtils.getResourceString(mContext,R.string.input_corect_photo4), ToastOrder.LENGTH_LONG).show();
					}
				}
				break;
			}
		}
		return flag;
	}

	/**
	 * 检测输入类型是否正确
	 * 
	 * @return true 表示验证通过，false表示验证不通过
	 */
	private boolean isPass() {
		boolean flag = true;
		for (int i = 0; i < compList.size(); i++) {
			Component comp = compList.get(i);
			Func func = comp.compFunc;
			if (func.getType() != Func.TYPE_SELECTCOMP
					&& func.getType() != Func.TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY_COMP
					&& func.getType() != Func.TYPE_SELECT_OTHER
					&& func.getType() != Func.TYPE_SINGLE_CHOICE_FUZZY_QUERY_COMP
					&& func.getType() != Func.TYPE_MULTI_CHOICE_FUZZY_QUERY_COMP
					&& func.getType() != Func.TYPE_MULTI_CHOICE_SPINNER_COMP && !TextUtils.isEmpty(comp.value)) {// 说明该项必须操作
				if (!checkType(comp, func)) {
					flag = false;
					return flag;
				}
			}
		}
		return flag;
	}

	/**
	 * 表格中串码提交比对控制
	 */
	private boolean codeControl() {
		boolean isCanSubmit = true;
		for (int i = 0; i < compList.size(); i++) {
			Component comp = compList.get(i);
			Func func = comp.compFunc;
			if (func.getType() == Func.TYPE_PRODUCT_CODE && !TextUtils.isEmpty(comp.value)) {
				String codeControl = func.getCodeControl();
				if ("3".equals(codeControl) || "4".equals(codeControl)) {
					ProductCodeInTableComp codeComp = (ProductCodeInTableComp) comp;
					isCanSubmit = codeComp.codeSubmitControl;
					if (!isCanSubmit) {
						ToastOrder.makeText(mContext, R.string.toast_one, ToastOrder.LENGTH_SHORT).show();
						break;
					}
				}
			}
		}
		return isCanSubmit;
	}

	/*
	 * 验证小数
	 */
	public boolean isNumeric(Component comp) {
		Pattern pattern = Pattern.compile("[0-9]+(\\.[0-9]+)?");
		Matcher matcher = pattern.matcher(comp.value);
		return matcher.matches();
	}

	/*
	 * 验证整数
	 */
	public boolean isInteger(Component comp) {
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(comp.value);
		return matcher.matches();
	}

	/*
	 * 验证其他下拉框
	 */
	public boolean isOther(String value) {
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int type = msg.what;
			View view = (View) msg.obj;
			switch (type) {
			case 1:// 添加一行
				ll_tableContent.addView(view);
				scrollToBottom(sv, ll_tableContent);
				break;
			// case 2://添加标题
			// ll_tableContentTitle.addView(view);
			// break;
			case 3:// 表格数据加载完成
				// if (initTableDialog != null && initTableDialog.isShowing()) {
				// initTableDialog.dismiss();
				// }
				isLoadFinish = true;// 页面加载完毕
				if (!isNormal && !isExecuteResult) {// 非正常返回并且没执行过返回结果的时候执行
					// unNormalActivityResult();
					setPhotoSYTime(null, 0);
				}
				// setScreenRotation(true);
				break;
			case 4:// 删除所有的数据
				ll_tableContent.removeAllViews();
				break;
			default:
				// if (initTableDialog != null && initTableDialog.isShowing()) {
				// initTableDialog.dismiss();
				// }
				// setScreenRotation(true);
				break;
			}
		};
	};

	/**
	 * 判断是否有需要验证距离的店面控件
	 * 
	 * @return
	 */
	private boolean isNeedLocation() {
		boolean flag = false;
		for (int i = 0; i < funcList.size(); i++) {
			Func func = funcList.get(i);
			if (func.getOrgOption() != null && func.getOrgOption() == Func.ORG_STORE && func.getIsAreaSearch() == 1
					&& func.getAreaSearchValue() != null) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	/**
	 * 根据不同的屏幕适配不同的宽和高
	 */
	private int getFuncColWidth(Func func) {

		DisplayMetrics metric = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(metric);
		int densityDpi = metric.densityDpi; // 屏幕密度DPI（120 / 160 / 240）
		int defaultValue = getDateForDPI(densityDpi, 420);
		if (func != null && func.getColWidth() != null && func.getColWidth() != 0) {// 没多加25像素多显示一个字
			// return
			// PublicUtils.convertDIP2PX(this,func.getColWidth())+defaultValue;
			return PublicUtils.convertDIP2PX(mContext, Math.abs(func.getColWidth())) + getDateForDPI(densityDpi, 160);
		} else {// 如果为空就默认返回下拉框的宽
			return defaultValue;
		}
	}

	/**
	 * 根据不同的屏幕适配不同的宽和高
	 */
	private void initUI() {
		DisplayMetrics metric = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(metric);
		// int width = metric.widthPixels; // 屏幕宽度（像素）
		// int height = metric.heightPixels; // 屏幕高度（像素）
		// float density = metric.density; // 屏幕密度（0.75 / 1.0 / 1.5）
		int densityDpi = metric.densityDpi; // 屏幕密度DPI（120 / 160 / 240）
//		widthDeleteBtn = getDelWidthDataForDPI(densityDpi, 150);// 删除button宽
		widthDeleteBtn=getDelWidthDataForDPI(75, 150);// 删除button宽
		heigh = getHeighDateForDPI(densityDpi, 120);// 高
	}

	/**
	 * 适配不同分辨率使用 适配三种分辨率 240*320 320*480 480*800
	 * 
	 * @param currentDPI
	 *            当前手机的屏幕密度
	 * @param normal
	 *            显示正常的情况的值
	 * @return 适配后返回的值
	 */
	private int getHeighDateForDPI(int currentDPI, int normal) {
		switch (currentDPI) {
		case 120:
			return PublicUtils.convertPX2DIP(mContext, (currentDPI * normal) / 240) - 40;
		case 160:
			return PublicUtils.convertPX2DIP(mContext, (currentDPI * normal) / 240) - 30;
		case 240:
			return PublicUtils.convertPX2DIP(mContext, (currentDPI * normal) / 240);
		default:
			return PublicUtils.convertPX2DIP(mContext, (currentDPI * normal) / 240)
					+ (int) getResources().getDimension(R.dimen.table_col_heigh);
		}

	}

	/**
	 * 删除键适配不同机型
	 * 
	 * @param currentDPI
	 * @param normal
	 * @return
	 */
	private int getDelWidthDataForDPI(int currentDPI, int normal) {
		switch (currentDPI) {
		case 120:
			return PublicUtils.convertPX2DIP(mContext, (currentDPI * normal) / 240) - 40;
		case 160:
			return PublicUtils.convertPX2DIP(mContext, (currentDPI * normal) / 240) - 30;
		case 240:
			return PublicUtils.convertPX2DIP(mContext, (currentDPI * normal) / 240);
		default:
			return PublicUtils.convertPX2DIP(mContext, (currentDPI * normal) / 240)
					+ (int) getResources().getDimension(R.dimen.table_del_width);
		}
	}

	/**
	 * 适配不同分辨率使用 适配三种分辨率 240*320 320*480 480*800
	 * 
	 * @param currentDPI
	 *            当前手机的屏幕密度
	 * @param normal
	 *            显示正常的情况的值
	 * @return 适配后返回的值
	 */
	private int getDateForDPI(int currentDPI, int normal) {

		switch (currentDPI) {
		case 120:
			return PublicUtils.convertPX2DIP(mContext, (currentDPI * normal) / 240) - 40;
		case 160:
			return PublicUtils.convertPX2DIP(mContext, (currentDPI * normal) / 240) - 30;
		case 240:
			return PublicUtils.convertPX2DIP(mContext, (currentDPI * normal) / 240);
		default:
			return PublicUtils.convertPX2DIP(mContext, (currentDPI * normal) / 240)
					+ (int) getResources().getDimension(R.dimen.table_col_width);
		}

	}

	/**
	 * 控制表格滑动到最底部
	 * 
	 * @param scroll
	 *            滑动的scrollView
	 * @param inner
	 *            scrollView所在的父view
	 */
	public void scrollToBottom(final View scroll, final View inner) {

		Handler mHandler = new Handler();

		mHandler.post(new Runnable() {
			public void run() {
				if (scroll == null || inner == null) {
					return;
				}

				int offset = inner.getMeasuredHeight() - scroll.getHeight();
				if (offset < 0) {
					offset = 0;
				}

				scroll.scrollTo(0, offset);
			}
		});
	}

	// @Override
	// protected void onDestroy() {
	// super.onDestroy();
	// if(bundle!=null && bundle.containsKey("isNetCanUse")){
	// bundle.remove("isNetCanUse");
	// }
	// }

	public static String fileName;// 拍照照片名字
	private PhotoInTableComp currentPhotoInTableComp;// 表格中拍照类型的控件的view
	private ScanInTableComp currentScanInTableComp;// 表格中扫描控件的view

	/*
	 * 拍二维码
	 */
	private void clickScan(Func func) {
		Intent i = new Intent(mContext, CaptureActivity.class);
		((Activity) mContext).startActivityForResult(i, 111);
	}

	/**
	 * 串码控件扫描后返回
	 * 
	 * @param code
	 */
	private void resultForProductCode(String code) {
		if (productCode == null) {
			ProductCodeInTableComp productCodeInTableComp = (ProductCodeInTableComp) compList.get(currentCompIndex);
			productCode = new ProductCodeTable(mContext, productCodeInTableComp.getFunc(), null);
			productCode.setProductCodeListener(TableCompView.this);
			productCode.setProductTableType(ProductCode.TABLE_TYPE_TABLE);
			productCode.setTableRow(productCodeInTableComp.compRow);
			productCode.productCodeInTable(productCodeInTableComp, productCodeInTableComp.value);
		} else {
			productCode.showProductCodeDialog(true);
		}
		if (productCode != null) {
			productCode.setProductCode(code);
		}
	}

	/**
	 * 可输入扫描控件扫描后返回
	 * 
	 * @param code
	 */
	private void resultForScanInputProductCode(String code) {
		if (scanInputCode == null) {
			ScanInTableComp scanInTableComp = (ScanInTableComp) compList.get(currentCompIndex);
			scanInputCode = new ScanInputCodeTable(mContext, scanInTableComp.getFunc(), null);
			scanInputCode.setProductCodeListener(TableCompView.this);
			scanInputCode.setProductTableType(ProductCode.TABLE_TYPE_TABLE);
			scanInputCode.setTableRow(scanInTableComp.compRow);
			scanInputCode.scanInputCodeInTable(scanInTableComp, scanInTableComp.value);
		} else {
			scanInputCode.showProductCodeDialog(true);
		}
		if (scanInputCode != null) {
			scanInputCode.setProductCode(code);
		}
	}

	/**
	 * 扫描控件扫描后返回
	 * 
	 * @param value
	 */
	private void resultForScanf(String value) {
		if (currentScanInTableComp == null) {
			currentScanInTableComp = (ScanInTableComp) compList.get(currentCompIndex);
		}
		if (currentScanInTableComp != null) {
			currentScanInTableComp.setValue(value);
		}
	}

	/**
	 * 拍照返回
	 */
	private void resultForPhoto(Date date) {
		if (currentPhotoInTableComp == null) {
			currentPhotoInTableComp = (PhotoInTableComp) compList.get(currentCompIndex);
		}

		// if (currentPhotoInTableComp!=null) {
		// int submitId = submitDB.selectSubmitIdNotCheckOut(planId,wayId,
		// storeId, targetId,targetType, Submit.STATE_NO_SUBMIT);
		// currentPhotoInTableComp.setSubmitId(submitId);
		// currentPhotoInTableComp.setIsLink(isLink);
		// currentPhotoInTableComp.resultPhoto(fileName);
		// currentPhotoInTableComp.value=fileName;
		// currentPhotoInTableComp.setName("已拍照");
		// }

		if (currentPhotoInTableComp != null) {
			final int submitId = submitDB.selectSubmitIdNotCheckOut(planId, wayId, storeId, targetId, targetType,
					Submit.STATE_NO_SUBMIT);
			currentPhotoInTableComp.setSubmitId(submitId);
			currentPhotoInTableComp.setIsLink(isLink);

			final Func func = currentPhotoInTableComp.getFunc();
			int waterMark = func.getIsImgCustom();
			if (waterMark == 1) {
				waterMarkDialog(currentPhotoInTableComp, date);
			} else {
				currentPhotoInTableComp.resultPhoto(fileName, date);
				currentPhotoInTableComp.value = fileName;
				currentPhotoInTableComp.setName(PublicUtils.getResourceString(mContext,R.string.al_take_phpto));
				final String imageUri="file://"+Constants.SDCARD_PATH+fileName;
		
				ImageView currentImageView = currentPhotoInTableComp.getCurrentImageView();
				ImageLoader.getInstance().displayImage(imageUri,currentImageView );
				try {
					savePhoto();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				currentImageView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
//					Toast.makeText(mContext, "点击了--"+"submitid-"+submitId+"  fun-"+func.getFuncId()+"--"+func.getId(), Toast.LENGTH_LONG).show();
                     int submitId1 = submitDB.selectSubmitIdNotCheckOut(planId, wayId, storeId, targetId, targetType,
         					Submit.STATE_NO_SUBMIT);
					if (submitId1!=0) {
						 int subId=0;
						   if (isLink) {
							   subId = new SubmitItemTempDB(mContext).findSubmitItem(submitId1,funcTable.getFuncId()+"").getId();
						    }else{
							
							 subId=new SubmitItemDB(mContext).getItemId(submitId1, funcTable.getFuncId());
						    }
						Intent intenta =new Intent(mContext, ShowImageTableActivity.class);
						intenta.putExtra("imageUrl",imageUri.substring(imageUri.lastIndexOf("/")+1));
						intenta.putExtra("imageName", func.getName());
						if(func!=null){
							intenta.putExtra("submitItemId",subId);
							intenta.putExtra("PreviewTable", true);//是表格里的图片
						}
						if(isLink){
							intenta.putExtra("isLink", true);
						}
	                     mContext.startActivity(intenta);
	                     saveTableClickListener.clickPosition(positionId);
					}else{}
	
					}
				});
				
				
			}

		}
	}

	/**
	 * 照片添加自定义水印内容
	 * 
	 * @param currentPhotoInTableComp
	 */
	private void waterMarkDialog(final PhotoInTableComp currentPhotoInTableComp, final Date date) {
		final Dialog dialog = new Dialog(mContext, R.style.transparentDialog);
		View view = View.inflate(mContext, R.layout.water_mark, null);
		dialog.setContentView(view);
		dialog.setCancelable(false);
		final EditText et = (EditText) view.findViewById(R.id.et_water_mark);
		Button confirmBtn = (Button) view.findViewById(R.id.btn_water_mark_confirmBtn);
		Button cancelBtn = (Button) view.findViewById(R.id.btn_water_mark_cancelBtn);
		confirmBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String waterMark = et.getText().toString();
				currentPhotoInTableComp.setWaterMark(waterMark);
				currentPhotoInTableComp.resultPhoto(fileName, date);
				currentPhotoInTableComp.value = fileName;
				currentPhotoInTableComp.setName(PublicUtils.getResourceString(mContext,R.string.al_take_phpto));
				dialog.dismiss();
			}
		});
		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				currentPhotoInTableComp.resultPhoto(fileName, date);
				currentPhotoInTableComp.value = fileName;
				currentPhotoInTableComp.setName(PublicUtils.getResourceString(mContext,R.string.al_take_phpto));
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	private String scanResult = null;// 扫描返回的值
	private int activityResultRequestCode;
	private boolean isLoadFinish;// 页面是否加载完毕
	private boolean isExecuteResult;// 是否执行过返回结果
	private Intent activityResultData = null;
	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// super.onActivityResult(requestCode, resultCode, data);
	//
	// if (Activity.RESULT_OK == resultCode || resultCode ==
	// R.id.scan_succeeded) {
	//
	// if (isNormal || isLoadFinish) {//正常返回或者非正常返回但是加载UI线程先执行完的时候
	// isExecuteResult = true;
	// if (requestCode == 102) {//串码
	// String code = data.getStringExtra("SCAN_RESULT");
	// resultForProductCode(code);
	// }else if(requestCode == 101){//扫描
	// String rs = data.getStringExtra("SCAN_RESULT");
	// resultForScanf(rs);
	// }else if(requestCode == 105){//可输入扫描控件
	// String rs = data.getStringExtra("SCAN_RESULT");
	// resultForScanInputProductCode(rs);
	// }else if (requestCode == 100) {//拍照
	// setPhotoSYTime(data,requestCode);
	// }else if(requestCode == 104){
	// setPhotoSYTime(data,requestCode);
	// }
	// }else{//非正常返回，并且加载UI线程还没执行完毕 此时保存返回的值和code
	// 等UI加载完毕执行unNormalActivityResult
	// activityResultRequestCode = requestCode;
	// activityResultData = data;
	// if (requestCode == 102 || requestCode == 101) {//串码
	// scanResult = data.getStringExtra("SCAN_RESULT");
	// }
	//
	// }
	// }
	// application.isSubmitActive = true;
	// }

	/**
	 * 选择照片返回
	 */
	/**
	 * 从相册选择照片后返回
	 * 
	 * @param data
	 */
	private void resultForPhotoAlbum(Intent data, Date date) {
		try {
			if (data != null) {
				Uri originalUri = data.getData(); // 获得图片的uri
				if (originalUri != null) {
					String tempPath =Constants.PATH_TEMP + fileName;
					if(!new File(Constants.PATH_TEMP).exists()){
						new File(Constants.PATH_TEMP).mkdir();
					}
					String path = AbsFuncActivity.getPath(mContext, originalUri);
//					Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), originalUri); // 显得到bitmap图片
					Bitmap bitmap = FileHelper.lessenUriImage(path); // 显得到bitmap图片
					FileHelper.saveBitmapToFile(bitmap, tempPath);
					resultForPhoto(date);
				} else {
					ToastOrder.makeText(mContext,R.string.work_summary_c6, ToastOrder.LENGTH_SHORT).show();
				}
			} else {
				ToastOrder.makeText(mContext, R.string.work_summary_c6, ToastOrder.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
			ToastOrder.makeText(mContext, R.string.work_summary_c6, ToastOrder.LENGTH_SHORT).show();
		}
	}

	/**
	 * 拍照异常返回
	 */
	private void unNormalActivityResult(Date date) {
		if (activityResultRequestCode == 102) {// 串码
			resultForProductCode(scanResult);
		} else if (activityResultRequestCode == 101) {
			resultForScanf(scanResult);
		} else if (activityResultRequestCode == 100) {
			resultForPhoto(date);
		} else if (activityResultRequestCode == 104) {
			resultForPhotoAlbum(activityResultData, date);
		}
	}

	@Override
	public void productCodeInfo(String info, int tableRow) {
		JLog.d(TAG, "ProductCodeInfo:" + info + " tableRow:" + tableRow);
		if (!TextUtils.isEmpty(info)) {
			ArrayList<Component> rowComp = rowMap.get(String.valueOf(tableRow));// 一行的数据
			HashMap<String, String> rowData = new TableParse().parseRowJason(info);// 一行的数据
			if (rowComp != null && rowComp.size() > 0) {
				for (int i = 0; i < rowComp.size(); i++) {
					Component comp = rowComp.get(i);
					if (comp != null && rowData != null) {
						// Func func = comp.compFunc;
						// if (func !=null && func.getType() ==
						// Func.TYPE_SELECT_OTHER || func.getType() ==
						// Func.TYPE_SELECTCOMP
						// || func.getType() ==
						// Func.TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY_COMP ||
						// func.getType() ==
						// Func.TYPE_SINGLE_CHOICE_FUZZY_QUERY_COMP) {
						//
						// String did = spinnerDidByValue(comp.value, func);
						// comp.value = did;
						// }
						updateTableRow(comp, rowData);
					}
				}
			}
		}
	}

	/**
	 * 更改表格中一行数据的值
	 * 
	 * @param rowComp
	 *            一行中所有的控件
	 * @param rowData
	 *            要改变的值
	 */
	private void updateTableRow(Component comp, HashMap<String, String> rowData) {

		Func func = comp.compFunc;
		if (rowData.containsKey(String.valueOf(func.getFuncId()))) {
			String value = rowData.get(String.valueOf(func.getFuncId()));
			if (comp instanceof TextComp) {// 文本组件
				TextComp textComp = (TextComp) comp;
				textComp.setText(value);
			} else if (comp instanceof EditComp) {// 输入框
				EditComp editComp = (EditComp) comp;
				editComp.setValue(value);
			} else if (comp instanceof DateInTableComp) {// 日期
				DateInTableComp dateInTableComp = (DateInTableComp) comp;
				dateInTableComp.setValue(value);
			} else if (comp instanceof SpinnerComp) {// 下拉框
				SpinnerComp spinnerComp = (SpinnerComp) comp;
				spinnerComp.setSelected(value);
			} else if (comp instanceof FuzzySingleChoiceSpinnerComp) {// 模糊搜索单选下拉框
				FuzzySingleChoiceSpinnerComp fuzzySingleChoiceSpinnerComp = (FuzzySingleChoiceSpinnerComp) comp;
				fuzzySingleChoiceSpinnerComp.setCurrentMap(rowData);
				fuzzySingleChoiceSpinnerComp.setSelected(value);
			} else if (comp instanceof SingleChoiceSpinnerComp) {// 模糊搜索单选下拉框
				SingleChoiceSpinnerComp singleChoiceSpinnerComp = (SingleChoiceSpinnerComp) comp;
				singleChoiceSpinnerComp.setCurrentMap(rowData);
				singleChoiceSpinnerComp.setSelected(value);
			} else if (comp instanceof FuzzyMultiChoiceSpinnerComp) {// 模糊多选
				FuzzyMultiChoiceSpinnerComp fuzzyMultiChoiceSpinnerComp = (FuzzyMultiChoiceSpinnerComp) comp;
				fuzzyMultiChoiceSpinnerComp.setSelected(value);
			} else if (comp instanceof MultiChoiceSpinnerComp) {// 多选
				MultiChoiceSpinnerComp multiChoiceSpinnerComp = (MultiChoiceSpinnerComp) comp;
				multiChoiceSpinnerComp.setSelected(value);
			} else if (comp instanceof ScanInTableComp) {// 扫描
				ScanInTableComp scanInTableComp = (ScanInTableComp) comp;
				scanInTableComp.setValue(value);
			} else if (comp instanceof ProductCodeInTableComp) {// 串码
				ProductCodeInTableComp productCodeInTableComp = (ProductCodeInTableComp) comp;
				productCodeInTableComp.setValue(value);
			} else if (comp instanceof HiddenCompInTable) {
				HiddenCompInTable hiddenCompInTable = (HiddenCompInTable) comp;
				hiddenCompInTable.value = value;
			}
		}
	}

	/**
	 * 根据值查找下拉框的id
	 * 
	 * @param value
	 * @param func
	 * @return
	 */
	private String spinnerDidByValue(String value, Func func) {
		String did = null;
		if (!TextUtils.isEmpty(value) && func != null) {
			Integer optionType = func.getOrgOption();
			if (optionType == null) {
				Dictionary dict = new DictDB(mContext).findDictByValue(func.getDictTable(), func.getDictDataId(),
						value);
				if (dict != null) {
					did = dict.getDid();
				}
			} else if (optionType == Func.ORG_OPTION) {
				Org org = new OrgDB(mContext).findOrgByOrgName(value);
				if (org != null) {
					did = String.valueOf(org.getOrgId());
				}
			} else if (optionType == Func.ORG_STORE) {
				OrgStore store = new OrgStoreDB(mContext).findOrgByStoreName(value);
				if (store != null) {
					did = String.valueOf(store.getStoreId());
				}
			} else if (optionType == Func.ORG_USER) {
				OrgUser user = new OrgUserDB(mContext).findOrgByUserName(value);
				if (user != null) {
					did = String.valueOf(user.getUserId());
				}
			}
		}
		return did;
	}

	private Double c_lat, c_lon;// 店面距离检测使用

	@Override
	public void onReceiveResult(LocationResult result) {
		if (locationLoadingDialog != null && locationLoadingDialog.isShowing()) {
			locationLoadingDialog.dismiss();
		}
		if (result != null && result.isStatus()) {
			c_lat = result.getLatitude();
			c_lon = result.getLongitude();
		} else {
			ToastOrder.makeText(mContext, getResources().getString(R.string.org_store_distance_location_fail),
					ToastOrder.LENGTH_SHORT).show();
		}
		loadUI();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		JLog.d(TAG, "屏幕发生旋转");
		// setScreenRotation(false);//刚开始设置屏幕不可旋转
	}

	public void setScreenRotation(boolean rotationSwitch) {
		// int flag
		// =Settings.System.getInt(getContentResolver(),Settings.System.ACCELEROMETER_ROTATION,
		// 0);
		// JLog.d(TAG, "旋转:"+flag);
		// Settings.System.putInt(getContentResolver(),Settings.System.ACCELEROMETER_ROTATION,flag==1?0:1);
		int flag = rotationSwitch ? 1 : 0;// 1是可旋转 0是不可旋转
		JLog.d(TAG, "旋转:" + rotationSwitch);
		Settings.System.putInt(mContext.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, flag);
	}

	// /**
	// * 异常退出的时候存储一些值
	// */
	// @Override
	// protected void onSaveInstanceState(Bundle outState) {
	// if(initTableDialog!=null && initTableDialog.isShowing()){
	// initTableDialog.dismiss();
	// }
	// outState.putString("fileName", fileName);
	// outState.putInt("currentCompIndex", currentCompIndex);
	// outState.putString("startTime", startTime);
	// if (c_lat!=null && c_lon!=null) {
	// outState.putDouble("c_lat", c_lat);
	// outState.putDouble("c_lon", c_lon);
	// }
	// try {
	// outState.putString("unNormalCompData", getTableValue());
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// outState.putBundle("bundle", bundle);
	// outState.putBundle("chooseMapBundle", chooseMapBundle);
	// outState.putBundle("orgMapBundle", orgMapBundle);
	// outState.putBoolean("isNoWait", isNoWait);
	// outState.putBoolean("isAddDel", isAddDel);
	// super.onSaveInstanceState(outState);
	// }

	// /**
	// * 获取存储的值
	// * @param savedInstanceState
	// */
	// protected void restoreConstants(Bundle savedInstanceState) {
	// super.restoreConstants(savedInstanceState);
	// startTime = savedInstanceState.getString("startTime");
	// fileName = savedInstanceState.getString("fileName");
	// currentCompIndex = savedInstanceState.getInt("currentCompIndex");
	// unNormalCompData = savedInstanceState.getString("unNormalCompData");
	// c_lon = savedInstanceState.getDouble("c_lon");
	// c_lat = savedInstanceState.getDouble("c_lat");
	// bundle = savedInstanceState.getBundle("bundle");
	// chooseMapBundle = savedInstanceState.getBundle("chooseMapBundle");
	// orgMapBundle = savedInstanceState.getBundle("orgMapBundle");
	// isNoWait = savedInstanceState.getBoolean("isNoWait");
	// isNormal = false;
	// isAddDel = savedInstanceState.getBoolean("isAddDel");
	// }

	/**
	 * GGMM 验证返回结果
	 */
	@Override
	public void GGMMCheckResult(boolean isPass, String tipMessage) {
		try {
			if (isPass) {// 验证通过就保存
				save();
			} else {// 验证不通过，提示原因
				checkGGMMDialog(tipMessage, getTableValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * GGMM提示
	 * 
	 * @param message
	 *            提示内容
	 * @param value
	 *            验证的时候要传给后台的值
	 */
	private void checkGGMMDialog(String message, final String value) {

		final Dialog tipDialog = new Dialog(mContext, R.style.transparentDialog);
		View view = View.inflate(mContext, R.layout.ggmm_check_dialog, null);
		TextView tv_tip_content = (TextView) view.findViewById(R.id.tv_tip_content);
		tipDialog.setContentView(view);
		tv_tip_content.setText(message);
		Button confirm = (Button) view.findViewById(R.id.btn_tip_confirm);
		Button cancel = (Button) view.findViewById(R.id.btn_tip_cancel);
		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tipDialog.dismiss();
				ggmmCheck.checkGGMM(funcTable.getTableId(), value);
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tipDialog.dismiss();
			}
		});
		tipDialog.setCancelable(false);
		tipDialog.show();
	}

	@Override
	public void submitReceive(boolean isSuccess) {
		if (isSuccess) {
			ToastOrder.makeText(mContext, R.string.submit_ok, ToastOrder.LENGTH_LONG).show();
			// this.finish();
		}
	}

	class PhotoContent {
		Date date;
		Intent intentData;
		int requeCode;

		public int getRequeCode() {
			return requeCode;
		}

		public void setRequeCode(int requeCode) {
			this.requeCode = requeCode;
		}

		public PhotoContent() {
		}

		public Date getDate() {
			return date;
		}

		public void setDate(Date date) {
			this.date = date;
		}

		public Intent getIntentData() {
			return intentData;
		}

		public void setIntentData(Intent intentData) {
			this.intentData = intentData;
		}

	}

	public void setPhotoSYTime(final Intent data, final int requestCode) {
		flag = true;
		timer = new Timer(true);
		if (task.cancel()) {
			timer.schedule(task, 3000, 100000000); // 延时3000ms后执行，1000ms执行一次
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				Date date = PublicUtils.getNetDate();
				;
				Message msg = Message.obtain();
				PhotoContent pc = new PhotoContent();
				pc.setDate(date);
				pc.setIntentData(data);
				pc.setRequeCode(requestCode);
				msg.obj = pc;
				mHanlderTable.sendMessage(msg);
			}
		}).start();
	}

	Handler mHanlderTable = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (flag) {
				flag = false;
				if (task != null) {
					task.cancel();
				}
				PhotoContent pc = (PhotoContent) msg.obj;
				if (null != pc) {
					int requsetCode = pc.getRequeCode();
					if (requsetCode == 109) {
						resultForPhoto(pc.getDate());
					} else if (requsetCode == 110) {
						resultForPhotoAlbum(pc.getIntentData(), pc.getDate());
					} else {
						unNormalActivityResult(pc.getDate());
					}
				}

			}
		};
	};
	private boolean flag = true;
	Timer timer;
	TimerTask task = new TimerTask() {
		public void run() {
			mHanlderTable.sendEmptyMessage(1);
		}
	};
   /**
    * 处理拍照
    * @param data
    */
	public void setCameraPhoto(Intent data, int requestCode) {
		setPhotoSYTime(data,requestCode);
	}
	/**
	 * 处理扫描
	 * @param data
	 * @param requestCode
	 */
    public void setScanCode(Intent data,int requestCode){
    	 String rs = data.getStringExtra("SCAN_RESULT");
    	 resultForScanf(rs);
    }
	/**
	 * 处理可输入扫描
	 * @param data
	 * @param requestCode
	 */
    public void setScanCodeInput(Intent data,int requestCode){
    	 String rs = data.getStringExtra("SCAN_RESULT");
    	 resultForScanInputProductCode(rs);
    }
	/**
	 * 处理串码
	 * @param data
	 * @param requestCode
	 */
    public void setProductCode(Intent data,int requestCode){
    	 String code = data.getStringExtra("SCAN_RESULT");
    	 resultForProductCode(code);
    }
    /**
     * 设置添加的位置,用于保存 时根据位置移除表格
     * @param index
     */
    public void setPositionId(int index){
    	this.positionId=index;
    }
    /**
     * 用于点击保存按钮传点击位置的监听
     * @author qingli
     *
     */
    public static interface SaveTableClickListener{
    	void clickPosition(int index);
    }
    private SaveTableClickListener saveTableClickListener;
    public void setSaveTableClickListener(SaveTableClickListener saveTableClickListener){
    	this.saveTableClickListener=saveTableClickListener;
    };
    /**
     * 表格内拍照控件的点击监听
     * @author qingli
     *
     */
    public static interface StartPhotoClickListener{
    	void clickPhotoPosition(int index);
    }
    private StartPhotoClickListener startPhotoClickListener;

    public void setStartPhotoClickListener(StartPhotoClickListener startPhotoClickListener){
    	this.startPhotoClickListener=startPhotoClickListener;
    }
    
    public void setShowTag(int tag){
    	this.showTag=tag;
    }
    public int getShowTag(){
    	return showTag;
    }
    
}
