package com.yunhu.yhshxc.activity;

import gcg.org.debug.JLog;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.LocationResult;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.bo.Module;
import com.yunhu.yhshxc.bo.Org;
import com.yunhu.yhshxc.bo.OrgStore;
import com.yunhu.yhshxc.bo.OrgUser;
import com.yunhu.yhshxc.bo.Submit;
import com.yunhu.yhshxc.bo.SubmitItem;
import com.yunhu.yhshxc.bo.VisitStore;
import com.yunhu.yhshxc.comp.PreviewTable;
import com.yunhu.yhshxc.comp.location.LocationControlListener;
import com.yunhu.yhshxc.comp.location.LocationForCheckOut;
import com.yunhu.yhshxc.comp.menu.AttachmentPreviewDataComp;
import com.yunhu.yhshxc.comp.menu.LinkPreviewDataComp;
import com.yunhu.yhshxc.comp.menu.PreviewDataComp;
import com.yunhu.yhshxc.comp.menu.RecordPreviewDataComp;
import com.yunhu.yhshxc.comp.menu.VideoPreviewDataComp;
import com.yunhu.yhshxc.database.DictDB;
import com.yunhu.yhshxc.database.DoubleDB;
import com.yunhu.yhshxc.database.FuncControlDB;
import com.yunhu.yhshxc.database.FuncDB;
import com.yunhu.yhshxc.database.MainMenuDB;
import com.yunhu.yhshxc.database.ModuleDB;
import com.yunhu.yhshxc.database.OrgDB;
import com.yunhu.yhshxc.database.OrgStoreDB;
import com.yunhu.yhshxc.database.OrgUserDB;
import com.yunhu.yhshxc.database.SubmitDB;
import com.yunhu.yhshxc.database.SubmitItemDB;
import com.yunhu.yhshxc.database.SubmitItemTempDB;
import com.yunhu.yhshxc.database.VisitFuncDB;
import com.yunhu.yhshxc.database.VisitStoreDB;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.doubletask.DoubleTaskManager;
import com.yunhu.yhshxc.doubletask.DoubleTaskManagerDB;
import com.yunhu.yhshxc.doubletask2.NewDoubleDetailActivity;
import com.yunhu.yhshxc.exception.DataComputeException;
import com.yunhu.yhshxc.func.HistorySearchPopupWindow;
import com.yunhu.yhshxc.http.submit.SubmitManager;
import com.yunhu.yhshxc.listener.SubmitDataListener;
import com.yunhu.yhshxc.location.LocationMaster;
import com.yunhu.yhshxc.order3.Order3FuncDetailActivity;
import com.yunhu.yhshxc.parser.TableParse;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.widget.ToastOrder;

/**
 * 预览页面 预览已经保存的信息 预览照片的时候可以点击编辑进行删除表格 预览超链接的时候以级联的形式展示点击可以编辑每一个模块
 * 预览表格的时候如果表格中有拍照点击可以查看照片
 * 
 * @author jishen
 * @version 2013-05-28
 * 
 */
public class FuncDetailActivity extends AbsBaseActivity implements LocationControlListener, SubmitDataListener {
	private LinearLayout showDataLayout;// 添加预览的每一项view
	public LinearLayout submitDataLayout;// 提交按钮
	private Button editPhoto;// 照片编辑按钮
	public int submitId;// 要预览数据的表单ID
	private int linkId, linkKey;// linkId超链接控件本身所在的表单ID linkKey超链接的控件ID
	private int taskId;// 双向时的数据ID传给后台使用
	public SubmitDB submitDB;// 提交表单数据库
	public SubmitItemDB submitItemDB;// 提交表单的值数据库
	private VisitFuncDB visitFuncDB;// 访店控件数据库
	private FuncDB funcDB;// 自定义模块控件数据库
	private LinearLayout ll_photoPreview;// 预览图片
	public Submit submit;// 提交表单实例
	private Dialog initUiDialog;// 初始化ui进度条
	private DictDB dicDB;// 字典数据库
	private int isCheckout;// 是否离店定位 0不要离店定位 1需要离店定位
	public int attState;// 考勤类型
	private int targetId;// 数据Id
	public String ableStatus;// 结点数据状态
	public int menuType;// 菜单类型（访店 自定义模块 考勤...）
	private Integer modType;// 自定义模块的类型 （上报 下发 考勤）
	private FuncControlDB funcControlDB;// 模块在规定的时间内控件同一个值提交的次数控制
	public TextView tv_submit;// 提交按钮显示的文字
	private boolean isFromLinkPriview;// 判断是否是超链接预览跳转过来的 true是超链接预览过来 false
										// 不是超链接预览过来
	private ArrayList<String> orgMapValueList;// 超链接预览编辑跳转到上报页面的时候把机构关系传过去
	private String sqlLinkJson;// 是否是超链接外部数据
	private boolean codeSubmitControl;// 是否要提交true要提交false（根据串码控制）
	private Func codeFunc;// 串码控件
	public boolean isOnPause;// 应用是否在后台 true是在后台 false不是在后台
	private int infoType; // 是否是店面信息 0 不是
	private StringBuffer upCtrlMainBuffer, upCtrlTableBuffer;//
	private StringBuffer codeUpdateBuffer, codeUpdateTabBuffer;
	public boolean isNoWait;// 是否是无等待提交 true是无等待提交 false不是无等待提交
	private String doubleMasterTaskNo;// 新双向主任务数据id
	private String doubleButtonType;// 新双向button类型
	private String menuType2 = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		submitDB = new SubmitDB(this);
		submitItemDB = new SubmitItemDB(this);
		visitFuncDB = new VisitFuncDB(this);
		funcControlDB = new FuncControlDB(this);
		funcDB = new FuncDB(this);
		dicDB = new DictDB(this);
		initUiDialog = new MyProgressDialog(this, R.style.CustomProgressDialog,
				getResources().getString(R.string.initTable));

		// android.nfc.NdefMessage;
	}

	/**
	 * 初始化实例初始化页面布局
	 */
	@Override
	protected void onStart() {
		super.onStart();
		setContentView(R.layout.func_detail);
		initBase();
		initIntentData();
		init();// 初始化数据
		initUiDialog.show();// 显示初始化UI dialog
		new Thread() {// 开启一个线程添加要预览的项
			public void run() {
				Looper.prepare();
				addData();// 添加数据
				initUiHandle.sendEmptyMessage(1);
			};
		}.start();
	}

	/**
	 * 初始化传递过来的数据
	 */
	private void initIntentData() {
		if (isNormal) {
			menuType2 = getIntent().getStringExtra("menuType2");
			isCheckout = getIntent().getIntExtra("isCheckout", Constants.DEFAULTINT);
			submitId = getIntent().getIntExtra("submitId", Constants.DEFAULTINT);
			linkId = getIntent().getIntExtra("linkId", Constants.DEFAULTINT);
			taskId = getIntent().getIntExtra("taskId", Constants.DEFAULTINT);
			attState = getIntent().getIntExtra("attState", Constants.DEFAULTINT);
			linkKey = getIntent().getIntExtra("linkKey", Constants.DEFAULTINT);
			sqlLinkJson = getIntent().getStringExtra("sqlLinkJson");
			isFromLinkPriview = getIntent().getBooleanExtra("isFromLinkPriview", false);
			orgMapValueList = getIntent().getStringArrayListExtra("map");
			menuType = getIntent().getIntExtra("menuType", 0);
			modType = getIntent().getIntExtra("modType", 0);
			targetId = getIntent().getIntExtra("targetId", Constants.DEFAULTINT);
			ableStatus = getIntent().getStringExtra("ableStatus");
			infoType = getIntent().getIntExtra("infoType", Constants.DEFAULTINT);
			bundle = getIntent().getBundleExtra("bundle");
			codeSubmitControl = getIntent().getBooleanExtra("codeSubmitControl", true);
			isNoWait = getIntent().getBooleanExtra("isNoWait", true);
			if (bundle != null) {
				doubleMasterTaskNo = bundle.getString(NewDoubleDetailActivity.DOUBLE_MASTER_TASK_NO);
				doubleButtonType = bundle.getString(NewDoubleDetailActivity.DOUBLE_BTN_TYPE);
			}
		}
	}

	/**
	 * 初始化数据 获取传过来的值
	 */
	private Bundle bundle;

	private void init() {
		tv_submit = (TextView) findViewById(R.id.tv_submit);

		editPhoto = (Button) findViewById(R.id.editPhoto_btn);
		editPhoto.setOnClickListener(this);
		showDataLayout = (LinearLayout) findViewById(R.id.ll_func_detail_add_data);
		submitDataLayout = (LinearLayout) findViewById(R.id.ll_func_show_detail_data);
		submitDataLayout.setOnClickListener(this);
		ll_photoPreview = (LinearLayout) findViewById(R.id.ll_priviewPhoto);
	}

	/**
	 * 添加要预览的项，根据不同类型的控件添加值 初始化UI
	 */
	private void addData() {
		PreviewDataComp previewDataComp;// 定义一个预览view
		PreviewDataComp linkPreviewDataComp = null;// 超链接预览view
		LinearLayout linkLayout = new LinearLayout(this);// 添加超链接预览view的layout
		linkLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
		linkLayout.setOrientation(LinearLayout.VERTICAL);
		// submit =
		// submitDB.findSubmitByIdandState(submitId,Submit.STATE_NO_SUBMIT);//
		// 首先查询Submit
		submit = submitDB.findSubmitById(submitId);// 首先查询Submit

		if (submit != null) { // submit不等于空表示有数据
			int currentSubmitId = submit.getId();// 提交数据的表单ID
			menuType = submit.getTargetType();// 菜单类型
			currentTargetId = submit.getTargetid();// 当前数据ID
			if (modType == Constants.MODULE_TYPE_EXECUTE) {// 双向执行
				submit.setDoubleId(taskId);// 双向的时候要把taskid传给服务端
				submitDB.updateSubmit(submit);// 更新submit
			}

			if (modType == Constants.MODULE_TYPE_ISSUED || modType == Constants.MODULE_TYPE_REASSIGN) {// 下发和改派的情况添加预览显示要下发和显示的用户
				if (!TextUtils.isEmpty(submit.getSendUserId())) {// 要下发或改派的用户的用户ID不为空则显示用户
					previewDataComp = new PreviewDataComp(this);
					String rs = new OrgUserDB(this).findDictMultiChoiceValueStr(submit.getSendUserId(), null);// 查找数据库
					previewDataComp.setShowDataContent(rs);// 设置内容
					previewDataComp.setShowDataTitle(PublicUtils.getResourceString(FuncDetailActivity.this,R.string.func_name));
					addView(previewDataComp.getView());// 添加view
				}
			}
			// jishen 2012-02-25修改超链接读取临时表里的数据
			List<SubmitItem> submitItemList = null;
			if (linkId != 0 && !isFromLinkPriview) {// 超链接的情况并且不是从超链接预览跳转过来的查询临时表中的数据
				submitItemList = new SubmitItemTempDB(this).findSubmitItemBySubmitId(currentSubmitId);// 根据Submitid找到该ID对应的一组数据
			} else {
				submitItemList = submitItemDB.findSubmitItemBySubmitId(currentSubmitId);// 根据Submitid找到该ID对应的一组数据
			}

			for (int i = 0; i < submitItemList.size(); i++) { // 如果有照片就显示照片
				int phtopType = submitItemList.get(i).getType();
				if (phtopType == Func.TYPE_CAMERA || phtopType == Func.TYPE_CAMERA_MIDDLE
						|| phtopType == Func.TYPE_CAMERA_HEIGHT || phtopType == Func.TYPE_CAMERA_CUSTOM) {
					initUiHandle.sendEmptyMessage(4);// 显示照片预览VIEW
					break;
				}
			}
			for (int j = 0; j < submitItemList.size(); j++) { // 循环添加操作项
				SubmitItem item = submitItemList.get(j);
				String paramName = item.getParamName(); // 对应FUNC_ID
				String currentValue = item.getParamValue();
				int currentType = item.getType();

				if (TextUtils.isEmpty(currentValue)) {// 如果当前值为空则不显示
					continue;
				}
				// 其他类型如果不是数字（说明输入了‘其他’值）此时就跳出该循环
				if (currentType == Func.TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY_COMP
						|| currentType == Func.TYPE_SELECT_OTHER) {
					if (!isInteger(paramName)) {
						continue;
					}
				}
				if (currentType != Func.TYPE_CAMERA && currentType != Func.TYPE_CAMERA_MIDDLE
						&& currentType != Func.TYPE_CAMERA_HEIGHT && currentType != Func.TYPE_CAMERA_CUSTOM
						&& currentType != Func.TYPE_HIDDEN && currentType != Func.TYPE_BUTTON) { // 只添加不是照片的类型并且不是隐藏类型的也不是BUTTON类型的

					if (currentType == Func.TYPE_TABLECOMP) { // 表格类型
						previewDataComp = new PreviewDataComp(this);
						previewDataComp.setShowDataIcon(true, R.drawable.p_table_icon);
						previewDataComp.setBackgroundResource(R.drawable.func_detail_table_click);
						previewDataComp.setIsShowUp(true);
						Func func = getFuncByParamName(paramName);
						// 隐藏域类型的控件不显示在预览里
						if (func == null || func.getType() == Func.TYPE_HIDDEN) {
							continue;
						}
						List<Func> funcTableList = funcDB.findFuncListByTargetidForPriview(func.getTableId()); // 查找table中的所有组件
						tableProductCodeControl(func, funcTableList);// 处理表格中的串码控件
						String name = func.getName(); // 组件名称
						previewDataComp.setShowDataTitle(name);
						List<HashMap<String, String>> tableList = getTableList(currentValue); // 获取表格的列数
						List<String> contentList = getTableContent(tableList, funcTableList);// 获取表格中的内容
						PreviewTable table = new PreviewTable(FuncDetailActivity.this, tableList.size(), contentList,
								funcTableList);// 获取表格预览view
						table.setTableSubmitItem(item);
						if (linkId != 0 && !isFromLinkPriview) {// 是否是超链接中的表格
							table.setIsInLink(true);
						} else {
							table.setIsInLink(false);
						}
						View tableView = table.getObject(); // 获取表格视图View
						previewDataComp.addShowDataContent(tableView);
						addView(previewDataComp.getView());// 添加表格view

						String imageList = getTableImageList(tableList, funcTableList);// 获取表格中图片的集合
						if (!TextUtils.isEmpty(imageList)) {// 如果表格中有照片并且拍照了
							item.setNote(imageList);// 将照片集合存到数据库
							if (linkId != 0 && !isFromLinkPriview) {// 超链接存到临时表中
								new SubmitItemTempDB(this).updateSubmitItem(item);
							} else {
								submitItemDB.updateSubmitItem(item, false);
							}
						}

					} else if (currentType == Func.TYPE_LOCATION) { // 定位类型
						previewDataComp = new PreviewDataComp(this);
						previewDataComp.setShowDataIcon(true, R.drawable.p_location_icon);
						previewDataComp.setBackgroundResource(R.drawable.func_detail_table_click);
						previewDataComp.setIsShowUp(true);// 控制显示箭头
						Func func = getFuncByParamName(paramName);
						if (func == null || func.getType() == Func.TYPE_HIDDEN) {
							continue;
						}
						// 如果是不显示地址的定位控件就不添加到预览页面里
						if (!(func.getOrgOption() != null && Func.OPTION_LOCATION == func.getOrgOption()
								&& "0".equals(func.getIsAddress()))) {
							String name = func.getName();
							previewDataComp.setShowDataTitle(name);
							StringBuffer buffer = new StringBuffer();// 拼接定位内容
							String adress = currentValue.split("\\$")[0];

							if (TextUtils.isEmpty(adress) || adress.equals("null")) {// 没有定位地址的情况
								buffer.append(this.getResources().getString(R.string.location_success_no_addr)
										+ buffer.toString());
							} else {
								buffer.append(adress);
							}
							if (currentValue.split("\\$").length > 4) {
								buffer.append("\n").append(PublicUtils.getResourceString(FuncDetailActivity.this,R.string.func_name1)).append(currentValue.split("\\$")[4]);
							}
							previewDataComp.setShowDataContent(buffer.toString());
							addView(previewDataComp.getView());
						}
					} else if (currentType == Func.TYPE_LINK) {// 超链接情况
						linkPreviewDataComp = new PreviewDataComp(this);
						linkPreviewDataComp.setShowDataIcon(true, R.drawable.p_mokuai_icon);
						linkPreviewDataComp.setBackgroundResource(R.drawable.func_detail_table_click);
						linkPreviewDataComp.setIsShowUp(true);
						Func func = getFuncByParamName(paramName);
						if (func == null || func.getType() == Func.TYPE_HIDDEN) {
							continue;
						}
						linkPreviewDataComp.setShowDataTitle(PublicUtils.getResourceString(FuncDetailActivity.this,R.string.func_name2));
						LinkPreviewDataComp comp = new LinkPreviewDataComp(this, func);// 初始化超链接view
						comp.initUI(func.getFuncId());// 如果有多层超链接的话控制层级显示
						linkLayout.addView(comp.getView());
					} else if (currentType == Func.TYPE_RECORD) {
						RecordPreviewDataComp recordPreviewDataComp = new RecordPreviewDataComp(FuncDetailActivity.this,
								currentValue);
						Func func = getFuncByParamName(paramName);
						if (func == null || func.getType() == Func.TYPE_HIDDEN) {
							continue;
						}
						recordPreviewDataComp.setShowDataTitle(func.getName());
						addView(recordPreviewDataComp.getView());
					} else if (currentType == Func.TYPE_VIDEO) {
						VideoPreviewDataComp recordPreviewDataComp = new VideoPreviewDataComp(FuncDetailActivity.this,
								currentValue);
						Func func = getFuncByParamName(paramName);
						if (func == null || func.getType() == Func.TYPE_HIDDEN) {
							continue;
						}
						recordPreviewDataComp.setShowDataTitle(func.getName());
						addView(recordPreviewDataComp.getView());

					} else if (currentType == Func.TYPE_ATTACHMENT) {
						AttachmentPreviewDataComp recordPreviewDataComp = new AttachmentPreviewDataComp(
								FuncDetailActivity.this, currentValue);
						Func func = getFuncByParamName(paramName);
						if (func == null || func.getType() == Func.TYPE_HIDDEN) {
							continue;
						}
						recordPreviewDataComp.setShowDataTitle(func.getName());
						addView(recordPreviewDataComp.getView());

					} else { // 其他类型 也分访店和自定义
						Func func = getFuncByParamName(paramName);
						if (func == null || func.getType() == Func.TYPE_HIDDEN) {
							continue;
						}
						previewDataComp = new PreviewDataComp(this);
						String name = func.getName();
						previewDataComp.setShowDataTitle(name);

						if (func.getType() == Func.TYPE_CHECKBOXCOMP) { // 是checkbox
							if (currentValue.equals(Func.CHECKBOX_CHECKED + "")) {// 选中的状态下添加该VIEW
								previewDataComp.setShowDataContent(PublicUtils.getResourceString(FuncDetailActivity.this,R.string.func_name3));
							} else {
								previewDataComp.setShowDataContent(PublicUtils.getResourceString(FuncDetailActivity.this,R.string.func_name4));
							}
							addView(previewDataComp.getView());
						} else if (func.getType() == Func.TYPE_SQL_BIG_DATA) {// 大数据sql类型的控件
							String note = item.getNote();
							if (!TextUtils.isEmpty(note)) {
								previewDataComp.setShowDataContent(note);
								addView(previewDataComp.getView());
							}
						} else if (func.getType() == Func.TYPE_PRODUCT_CODE) {// 串码
							codeFunc = func;
							previewDataComp.setShowDataContent(currentValue);
							addView(previewDataComp.getView());
							String updateCode = func.getCodeUpdate();
							if (codeUpdateBuffer == null && !TextUtils.isEmpty(updateCode)) {
								codeUpdateBuffer = new StringBuffer(updateCode);
								submit.setCodeUpdate(codeUpdateBuffer.toString());
								submitDB.updateSubmit(submit);
							}
							String codeControl = func.getCodeControl();
							String codeType = func.getCodeType();
							if (upCtrlMainBuffer == null) {// 不显示不控制的串码
								upCtrlMainBuffer = new StringBuffer();
								upCtrlMainBuffer.append(String.valueOf(func.getFuncId()));
								upCtrlMainBuffer.append("_").append(codeControl);
								submit.setUpCtrlMain(upCtrlMainBuffer.toString());
								submitDB.updateSubmit(submit);
							}

						} else if (func.getType() == Func.TYPE_SELECTCOMP
								|| func.getType() == Func.TYPE_SINGLE_CHOICE_FUZZY_QUERY_COMP) { // 下拉框

							if (func.getOrgOption() != null) {// 组织结构 机构店面 机构用户
								setOrgOption(func, showDataLayout, previewDataComp, currentValue);
							} else {
								setSelect(func, previewDataComp, currentValue);// 普通下拉框
							}

						} else if (func.getType() == Func.TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY_COMP
								|| func.getType() == Func.TYPE_SELECT_OTHER) {
							// 其他类型的下拉框
							if (currentValue.equals("-1")) {// 选了其他
								for (SubmitItem otherItem : submitItemList) {// 遍历，查找其他的值
									if (otherItem.getParamName().equals(func.getFuncId() + "_other")) {
										previewDataComp.setShowDataContent(otherItem.getParamValue());
										addView(previewDataComp.getView());
										break;
									}
								}

							} else {// 没选其他
								if (func.getOrgOption() != null) {// 如果是机构
									setOrgOption(func, showDataLayout, previewDataComp, currentValue);
								} else {
									setSelect(func, previewDataComp, currentValue);// 不是机构的话就当普通下拉框处理
								}
							}

						} else if ((func.getType() == Func.TYPE_MULTI_CHOICE_SPINNER_COMP
								|| func.getType() == Func.TYPE_MULTI_CHOICE_FUZZY_QUERY_COMP)
								&& !TextUtils.isEmpty(currentValue)) {// 多选下拉框
							// 多选下拉框
							if (func.getOrgOption() != null) {// 机构
								setOrgOption(func, showDataLayout, previewDataComp, currentValue);
							} else {
								// 查找多选下拉框的值
								String rs = dicDB.findDictMultiChoiceValueStr(currentValue, func.getDictDataId(),
										func.getDictTable());
								previewDataComp.setShowDataContent(rs);
								addView(previewDataComp.getView());
							}
						} else {// 其他类型的，直接显示值
							previewDataComp.setShowDataContent(currentValue);
							addView(previewDataComp.getView());
						}
					}
				}
			}
			if (linkPreviewDataComp != null) {// 不等于空说明预览里面有超链接
												// 那么就添加超链接linkLayout
				linkPreviewDataComp.addShowDataContent(linkLayout);
				Message msg = initUiHandle.obtainMessage(3);
				msg.obj = linkPreviewDataComp.getView();
				initUiHandle.sendMessage(msg);
			}
		} else {// 表示没有可预览的数据
			ToastOrder.makeText(FuncDetailActivity.this, getResources().getString(R.string.priview),
					ToastOrder.LENGTH_LONG).show();
			thisFinish(R.id.submit_no_succeeded, null);
		}
	}

	/**
	 * 返回机构数据
	 * 
	 * @param value
	 *            机构的did
	 * @return 机构的名称
	 */
	private String getOrg(String value) {
		List<Org> orgList = new OrgDB(this).findOrgByOrgId(value);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < orgList.size(); i++) {
			Org org = orgList.get(i);
			sb.append(",").append(org.getOrgName());
		}
		return sb.length() > 0 ? sb.substring(1).toString() : value;
	}

	/**
	 * 返回店面数据
	 * 
	 * @param value
	 *            店面的did
	 * @return 店面的名称
	 */
	private String getOrgStore(String value) {
		List<OrgStore> orgStoreList = new OrgStoreDB(this).findOrgListByStoreId(value);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < orgStoreList.size(); i++) {
			OrgStore orgStore = orgStoreList.get(i);
			sb.append(",").append(orgStore.getStoreName());
		}
		return sb.length() > 0 ? sb.substring(1).toString() : value;
	}

	/**
	 * 返回用户数据
	 * 
	 * @param value
	 *            用户的did
	 * @return 用户名
	 */
	private String getOrgUser(String value) {
		List<OrgUser> orgUserList = new OrgUserDB(this).findOrgUserByUserId(value);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < orgUserList.size(); i++) {
			OrgUser orgUser = orgUserList.get(i);
			sb.append(",").append(orgUser.getUserName());
		}
		return sb.length() > 0 ? sb.substring(1).toString() : value;
	}

	/**
	 * 设置机构店面
	 * 
	 * @param func
	 *            下拉框func
	 * @param showDataLayout
	 *            添加预览的每一项view
	 * @param previewDataComp
	 *            该项预览view
	 * @param currentValue
	 *            当前值
	 */
	private void setOrgOption(Func func, LinearLayout showDataLayout, PreviewDataComp previewDataComp,
			String currentValue) {
		if (func.getOrgOption() != null && func.getOrgOption() == Func.ORG_STORE) {
			previewDataComp.setShowDataContent(getOrgStore(currentValue));
			addView(previewDataComp.getView());
		} else if (func.getOrgOption() != null && func.getOrgOption() == Func.ORG_USER) {
			previewDataComp.setShowDataContent(getOrgUser(currentValue));
			addView(previewDataComp.getView());
		} else if (func.getOrgOption() != null && func.getOrgOption() == Func.ORG_OPTION) {

			previewDataComp.setShowDataContent(getOrg(currentValue));
			addView(previewDataComp.getView());
		}
	}

	/**
	 * 下拉框类型
	 * 
	 * @param func
	 *            下拉框func
	 * @param previewDataComp
	 *            当前下拉框view
	 * @param
	 */
	private void setSelect(Func func, PreviewDataComp previewDataComp, String currentValue) {
		String tableName = func.getDictTable(); // 查询的表
		String dataId = func.getDictDataId(); // 查询表中的列
		Dictionary dic = dicDB.findDictListByTable(tableName, dataId, currentValue);
		if (dic == null) {// 没有找到该下拉框对应的字典表
			previewDataComp.setShowDataContent(currentValue);
		} else {
			previewDataComp.setShowDataContent(dic.getCtrlCol());
		}
		addView(previewDataComp.getView());
	}

	/**
	 * 编辑照片 跳转到照片预览activity
	 */
	public void editPhoto() {
		Intent intent = new Intent(this, PhotoPreviewActivity.class);
		intent.putExtra("submitId", submitId);// 提交表单ID
		if (linkId != 0) {// 超链接情况
			intent.putExtra("isLink", true); // true表示是超链接
			intent.putExtra("isFromLinkPriview", isFromLinkPriview);
		}
		startActivity(intent);
	}

	/**
	 * 根据表格的json获取Table列集合
	 * 
	 * @param json
	 *            表格保存的数据
	 * @return 每行的值的集合
	 */
	private List<HashMap<String, String>> getTableList(String json) {
		List<HashMap<String, String>> tableList = new TableParse().parseJason(json);
		return tableList;
	}

	/**
	 * 表格中串码控制
	 * 
	 * @param tableFunc
	 *            表格本身控件
	 * @param funcTableList
	 *            表格关联的控件
	 */
	private void tableProductCodeControl(Func tableFunc, List<Func> funcTableList) {
		for (int i = 0; i < funcTableList.size(); i++) {
			Func func = funcTableList.get(i);
			if (func.getType() == Func.TYPE_PRODUCT_CODE) {
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
	 * 获取表格内容
	 * 
	 * @param tablelist
	 *            每行的集合
	 * @param funcTableList
	 *            表格中所有控件的集合
	 * @return 表格所有控件值的集合
	 */
	private List<String> getTableContent(List<HashMap<String, String>> tablelist, List<Func> funcTableList) {
		List<String> sList = new ArrayList<String>();
		for (int i = 0; i < tablelist.size(); i++) { // 循环行数
			HashMap<String, String> table = tablelist.get(i);
			for (int j = 0; j < funcTableList.size(); j++) {// 循环列数
				Func func = funcTableList.get(j); // 得到该列的Func
				String did = table.get(func.getFuncId() + "");
				if (func.getType() == Func.TYPE_EDITCOMP || func.getType() == Func.TYPE_EDITCOMP_NUMERIC
						|| func.getType() == Func.TYPE_DATEPICKERCOMP || func.getType() == Func.TYPE_TIMEPICKERCOMP
						|| func.getType() == Func.TYPE_CAMERA || func.getType() == Func.TYPE_CAMERA_MIDDLE
						|| func.getType() == Func.TYPE_CAMERA_CUSTOM || func.getType() == Func.TYPE_CAMERA_HEIGHT
						|| func.getType() == Func.TYPE_SCAN || func.getType() == Func.TYPE_SCAN_INPUT
						|| func.getType() == Func.TYPE_PRODUCT_CODE) {// 这几种类型直接添加
					sList.add(did);
				} else if (func.getType() == Func.TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY_COMP
						|| func.getType() == Func.TYPE_SELECT_OTHER) {// 其他类型的下拉框
					if (table.get(func.getFuncId() + "").equals("-1")) {
						sList.add(table.get(func.getFuncId() + "_other")); // 将内容添加到List
					} else {
						if (TextUtils.isEmpty(did)) {// 空值
							sList.add(""); // 将内容添加到List
						} else {
							String tableName = func.getDictTable(); // 查询的表
							String dataId = func.getDictDataId(); // 查询表中的列
							Dictionary dic = dicDB.findDictListByTable(tableName, dataId, did);
							if (dic == null) {
								sList.add(did); // 将内容添加到List
							} else {
								sList.add(dic.getCtrlCol()); // 将内容添加到List
							}
						}
					}
				} else {
					if (func.getOrgOption() != null && func.getOrgOption() != Func.OPTION_LOCATION) {// 表格里如果有店或者user
						if (TextUtils.isEmpty(did)) {
							sList.add("");
						} else {
							if (func.getOrgOption() == Func.ORG_STORE) {// 店
								sList.add(getOrgStore(did));
							} else if (func.getOrgOption() == Func.ORG_USER) {// user
								sList.add(getOrgUser(did));
							}
						}
					} else {
						String tableName = func.getDictTable(); // 查询的表
						String dataId = func.getDictDataId(); // 查询表中的列
						if (!TextUtils.isEmpty(did)) {
							String rc = dicDB.findDictMultiChoiceValueStr(did, dataId, tableName);
							sList.add(rc);
						} else {
							sList.add("");
						}
					}

				}
			}

		}

		return sList;
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
				if (func.getType() == Func.TYPE_CAMERA || func.getType() == Func.TYPE_CAMERA_MIDDLE
						|| func.getType() == Func.TYPE_CAMERA_HEIGHT || func.getType() == Func.TYPE_CAMERA_CUSTOM) {// 拍照类型
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
	 * 确认checkOUT
	 */
	private void sureCheckOut(LocationResult locationResult) {
		if (isCheckout == 1) {// 插入离店定位内容
			if (locationResult != null) {
				if (this.getResources().getString(R.string.location_success_no_addr).equals(locationResult.getAddress())
						|| PublicUtils.checkAddress(locationResult.getAddress())) {
					locationResult.setAddress("");
				}
				String location = new StringBuffer().append(locationResult.getLongitude()).append("$")
						.append(locationResult.getLatitude()).append("$").append(locationResult.getAddress())
						.append("$").append(locationResult.getLocType()).append("$").append(locationResult.getRadius())
						.append("$").append(locationResult.getPosType()).append("$").append(LocationMaster.ACTION)
						.append("$").append(LocationMaster.VERSION).toString();
				submit.setCheckoutGis(location);
			}
			submit.setCheckoutTime(DateUtil.getCurDateTime());
		}
		submit.setState(Submit.STATE_SUBMITED);// 改变数据状态为提交状态
		Constants.ISCHECKOUT = true;// 离店定位以后修改访店的访问时间和访店状态
		VisitStoreDB visitStoreDB = new VisitStoreDB(this);
		VisitStore visitStore = visitStoreDB.findVisitStore(submit.getWayId(), submit.getTargetid(),
				submit.getStoreId(), submit.getPlanId());
		if (visitStore != null) {
			submit.setVisitNumbers(visitStore.getSubmitNum() + 1);
		}
		if (TextUtils.isEmpty(submit.getTimestamp())) {
			submit.setTimestamp(String.valueOf(System.currentTimeMillis()));
		}
		submitDB.updateSubmit(submit);
		startSubmitService();// 开始提交
	}

	/**
	 * 用串码的时候控制是否提交
	 * 
	 * @return
	 */
	private boolean codeSubmitControl() {
		boolean flag = true;
		if (codeFunc != null) {
			String codeControl = codeFunc.getCodeControl();
			if ("4".equals(codeControl) || "3".equals(codeControl)) {// 控制
				flag = codeSubmitControl;
			}
			if (!flag) {
				ToastOrder.makeText(this, PublicUtils.getResourceString(this,R.string.toast_one), ToastOrder.LENGTH_SHORT).show();
				submitDataLayout.setEnabled(true);
			}
		}
		return flag;
	}

	/**
	 * 手机可用时间段控制
	 * 
	 * @param
	 * @return
	 */
	public boolean usableControl() {
		boolean flag = true;
		String time = null;
		Menu menu = null;
		if (menuType == Menu.TYPE_VISIT || menuType == Menu.TYPE_ATTENDANCE || menuType == Menu.TYPE_NEW_ATTENDANCE) {
			menu = new MainMenuDB(this).findMenuListByMenuType(menuType);
		} else {
			menu = new MainMenuDB(this).findMenuListByMenuId(targetId);
		}
		// time = "12:00|0.5,14:55|0.5";
		time = menu != null ? menu.getPhoneUsableTime() : "";
		MenuUsableControl control = new MenuUsableControl();
		flag = control.isCanUse(time);
		if (menuType == Menu.TYPE_ORDER3) {
			if (flag) {// 时间过了判断天
				flag = control.isOrder3CanUse(this);
				if (!flag) {
					showTip(PublicUtils.getResourceString(this,R.string.toast_one8));
					submitDataLayout.setEnabled(true);
				}
			} else {
				showTip(control.tipInfo(time));
				submitDataLayout.setEnabled(true);
			}
		} else {
			if (!flag) {
				showTip(control.tipInfo(time));
				submitDataLayout.setEnabled(true);
			}
		}
		return flag;
	}

	/**
	 * 手机可用时间段控制,新考勤模块
	 * 
	 * @param
	 * @return
	 */
	public boolean usableControl(Date date) {
		boolean flag = true;
		String time = null;
		Menu menu = null;
		if (menuType == Menu.TYPE_VISIT || menuType == Menu.TYPE_ATTENDANCE || menuType == Menu.TYPE_NEW_ATTENDANCE) {
			menu = new MainMenuDB(this).findMenuListByMenuType(menuType);
		} else {
			menu = new MainMenuDB(this).findMenuListByMenuId(targetId);
		}
		// time = "12:00|0.5,14:55|0.5";
		time = menu != null ? menu.getPhoneUsableTime() : "";
		MenuUsableControl control = new MenuUsableControl();
		flag = control.isCanUse(time, date);
		if (menuType == Menu.TYPE_ORDER3) {
			if (flag) {// 时间过了判断天
				flag = control.isOrder3CanUse(this);
				if (!flag) {
					showTip(PublicUtils.getResourceString(this,R.string.toast_one8));
					submitDataLayout.setEnabled(true);
				}
			} else {
				showTip(control.tipInfo(time));
				submitDataLayout.setEnabled(true);
			}
		} else {
			if (!flag) {
				showTip(control.tipInfo(time));
				submitDataLayout.setEnabled(true);
			}
		}
		return flag;
	}

	/**
	 * 手机可用时间段控制,新订单模块
	 * 
	 * @param
	 * @return
	 */
	public boolean usableControlOrder3(Date date) {
		boolean flag = true;
		String time = null;
		Menu menu = null;
		if (menuType == Menu.TYPE_VISIT || menuType == Menu.TYPE_ATTENDANCE || menuType == Menu.TYPE_NEW_ATTENDANCE) {
			menu = new MainMenuDB(this).findMenuListByMenuType(menuType);
		} else {
			menu = new MainMenuDB(this).findMenuListByMenuId(targetId);
		}
		// time = "12:00|0.5,14:55|0.5";
		time = menu != null ? menu.getPhoneUsableTime() : "";
		MenuUsableControl control = new MenuUsableControl();
		flag = control.isCanUse(time, date);
		if (menuType == Menu.TYPE_ORDER3) {
			if (flag) {// 时间过了判断天
				flag = control.isOrder3CanUse(this, date);
				if (!flag) {
					showTip(PublicUtils.getResourceString(this,R.string.toast_one8));
					submitDataLayout.setEnabled(true);
				}
			} else {
				showTip(control.tipInfo(time));
				submitDataLayout.setEnabled(true);
			}
		} else {
			if (!flag) {
				showTip(control.tipInfo(time));
				submitDataLayout.setEnabled(true);
			}
		}
		return flag;
	}

	/**
	 * 手机可使用时间段控制提醒
	 */
	private LinearLayout ll_tip;

	private void showTip(String info) {
		ll_tip = (LinearLayout) findViewById(R.id.ll_menu_usable_tip);
		TextView tv_tip = (TextView) findViewById(R.id.tv_menu_usable_tip);
		ll_tip.setVisibility(View.VISIBLE);
		tv_tip.setText(info);
		ll_tip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ll_tip.setVisibility(View.GONE);
			}
		});

	}

	/*
	 * 完整性控制
	 */
	public boolean isCompletion() {
		boolean isCompletion = true; // 默认操作完整
		Submit isCompletionSubmit = submitDB.findSubmitById(submitId); // 首先查询要提交的Submit
		if (isCompletionSubmit != null) {
			int currentSubmitId = isCompletionSubmit.getId();
			int currentTargetId = isCompletionSubmit.getTargetid();
			// 根据Submitid找到该ID对应的应经保存的一组数据
			List<Func> isEmptyList = new ArrayList<Func>(); // 查找所有必须操作的List集合
			if (menuType == Menu.TYPE_VISIT) { // 访店
				isEmptyList = visitFuncDB.findFuncListByIsEmpty(1, currentTargetId);// 查找所有不能为空的选项
			} else if (menuType == Menu.TYPE_VISIT) {
				isEmptyList = funcDB.findNearbyFuncListIsEmpty(1, currentTargetId);// 查找所有不能为空的选项
			} else {// 自定义模块
				switch (modType == null ? 0 : modType) {
				case Constants.MODULE_TYPE_EXECUTE:
					isEmptyList = funcDB.findFuncListByIsEmpty(1, currentTargetId, "1"); // 查找双向所有不能为空的选项
					break;
				case Constants.MODULE_TYPE_QUERY:// 查询
				case Constants.MODULE_TYPE_VERIFY:// 审核
				case Constants.MODULE_TYPE_REASSIGN:// 改派
					isEmptyList = funcDB.findFuncListByIsEmpty(1, currentTargetId, ableStatus);
					break;
				case Constants.MODULE_TYPE_ISSUED:// 下发
				case Constants.MODULE_TYPE_REPORT:// 上报
					isEmptyList = funcDB.findFuncListByIsEmpty(1, currentTargetId, "0"); // 上报和下发的情况
					break;
				default:
					isEmptyList = funcDB.findFuncListByIsEmpty(1, currentTargetId, ableStatus);
					break;
				}
			}
			int isEmptyListLen = isEmptyList.size();
			if (isEmptyListLen != 0) { // 说明有选项必须要操作
				for (int i = 0; i < isEmptyListLen; i++) { // 2012-10-29 houyu修改
															// for EnterMustList
					Func isEmptyFunc = isEmptyList.get(i); // 获取必须要操作的Func
					SubmitItem item = null;
					if (linkId != 0 && !isFromLinkPriview) {// 超链接查找临时表
						item = new SubmitItemTempDB(this).findSubmitItemByParamNameAndSubmitId(
								String.valueOf(isEmptyFunc.getFuncId()), currentSubmitId);
					} else {
						item = submitItemDB.findSubmitItemByParamNameAndSubmitId(
								String.valueOf(isEmptyFunc.getFuncId()), currentSubmitId);
					}

					if (item == null || TextUtils.isEmpty(item.getParamValue())) {
						if (!TextUtils.isEmpty(isEmptyFunc.getEnterMustList())) { // 如果此处有值，要判断条件是否满足，满足时，才是必填
							boolean isEmptyByEnterMustList = false;
							try {
								if (linkId != 0 && !isFromLinkPriview) {
									isEmptyByEnterMustList = new SubmitItemTempDB(this).isEmptyByEnterMustList(
											currentSubmitId, isEmptyFunc.getEnterMustList(), menuType);
								} else {
									isEmptyByEnterMustList = submitItemDB.isEmptyByEnterMustList(currentSubmitId,
											isEmptyFunc.getEnterMustList(), menuType);
								}
							} catch (NumberFormatException e) {
								ToastOrder.makeText(this, PublicUtils.getResourceString(this,R.string.toast_one1), ToastOrder.LENGTH_SHORT).show();
								JLog.e(e);
								break;
							} catch (DataComputeException e) {
								ToastOrder.makeText(FuncDetailActivity.this, e.getMessage(), ToastOrder.LENGTH_SHORT)
										.show();
								JLog.e(e);
								break;
							}
							if (isEmptyByEnterMustList) { // 如果不满足，为不必填
								isCompletion = false;
								ToastOrder.makeText(FuncDetailActivity.this,
										PublicUtils.getResourceString(FuncDetailActivity.this,R.string.toast_one2)+"\n\"" + isEmptyFunc.getName() + "\"", ToastOrder.LENGTH_SHORT).show();
								break;
							} else {
								continue;
							}
						} else { // 此处无值为必填栏位
							isCompletion = false;
							ToastOrder.makeText(FuncDetailActivity.this, PublicUtils.getResourceString(FuncDetailActivity.this,R.string.toast_one2)+"\"" + isEmptyFunc.getName() + "\"",
									ToastOrder.LENGTH_SHORT).show();
							break;
						}
					}
				}
			}
		}
		if (!isCompletion) {
			submitDataLayout.setEnabled(true);
		}
		return isCompletion;
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		submitDataLayout.setEnabled(false);
		switch (v.getId()) {
		case R.id.ll_func_show_detail_data: // 提交数据
			if (linkId != Constants.DEFAULTINT) {// 超链接预览
				if (codeSubmitControl() && isCompletion()) {
					controlLink();
				}
			} else {
				if (menuType == Menu.TYPE_VISIT) {// 首先检测完整性控制 访店提交
					if (usableControl() && codeSubmitControl() && isCompletion()) {
						submitVisit();
					}
				} else if (menuType == Menu.TYPE_NEW_TARGET) {// 新双向
					if (usableControl() && codeSubmitControl() && submitForBaseTime() && isCompletion()) {
						if (modType == Constants.MODULE_TYPE_EXECUTE_NEW && infoType == 0) {
							submitNewDouble();
						}
					}
				} else if (menuType == Menu.TYPE_NEARBY && isCompletion()) {// 就近拜访
					submitNearbyVisit();
				} else if (menuType == Menu.TYPE_MODULE) {// 自定义情况下只改变提交状态和存入时间戳
					if (usableControl() && codeSubmitControl() && submitForBaseTime() && isCompletion()) {
						// 自定义 执行 上报 店面信息修改
						if ((modType == Constants.MODULE_TYPE_EXECUTE || modType == Constants.MODULE_TYPE_REPORT
								|| modType == Constants.DEFAULTINT) && infoType == 0) {
							submitModuleOrDouble();
						} else {
							submitReplenish();// 补报提交
						}
					}
				}
			}
			break;
		case R.id.editPhoto_btn:
			editPhoto();// 照片编辑
			break;
		default:
			break;
		}
	}

	/**
	 * 访店数据提交
	 */
	private void submitVisit() {
		if (isCheckout == 1) {
			checkOutLocation();
		} else {
			sureCheckOut(null);
		}
	}

	/**
	 * checkout定位
	 */
	private LocationForCheckOut locationForCheckOut;

	private void checkOutLocation() {
		Bundle lBundle = new Bundle();
		lBundle.putString("storelon", bundle.getString("storelon"));
		lBundle.putString("storelat", bundle.getString("storelat"));
		lBundle.putInt("isCheck", bundle.getInt("isCheck"));
		lBundle.putInt("storeDistance", bundle.getInt("storeDistance"));
		lBundle.putString("is_address", bundle.getString("is_address"));// 默认1显示
		lBundle.putString("is_anew_loc", bundle.getString("is_anew_loc"));// 默认0不重新定位
		lBundle.putString("loc_type", bundle.getString("loc_type"));// 默认2选择所有定位方式
		Dialog dialog = new MyProgressDialog(this, R.style.CustomProgressDialog,
				getResources().getString(R.string.waitForAMoment));
		locationForCheckOut = new LocationForCheckOut(this, this);
		locationForCheckOut.setLoadDialog(dialog);
		locationForCheckOut.setBundle(lBundle);
		locationForCheckOut.startLocation();
	}

	/**
	 * 补报数据提交
	 */
	private void submitReplenish() {
		submit.setState(Submit.STATE_SUBMITED);
		if ((TextUtils.isEmpty(submit.getTimestamp()))) {
			submit.setTimestamp(String.valueOf(System.currentTimeMillis()));
		}
		submitDB.updateSubmit(submit);
		Constants.ISCHECKOUTMODUL = true;
		startSubmitService();
	}

	/**
	 * 双向或者自定义提交
	 */
	private void submitNewDouble() {
		submit.setState(Submit.STATE_SUBMITED);
		if ("1".equals(doubleButtonType) || "-99".equals(doubleButtonType)) {// 接受
																				// 和动态按钮
																				// 的时候是修改主数据
																				// 传doubleMasterTaskNo
			submit.setTimestamp(doubleMasterTaskNo);
		} else if ("2".equals(doubleButtonType)) {// 上报的时候自己生成时间戳
			submit.setTimestamp(String.valueOf(System.currentTimeMillis()));
		} else {// 修改数据的时候时间戳是服务器传过来的值
			if ((TextUtils.isEmpty(submit.getTimestamp()))) {
				submit.setTimestamp(String.valueOf(System.currentTimeMillis()));
			}
		}
		// 新双向
		if (modType == Constants.MODULE_TYPE_EXECUTE_NEW) {
			submit.setDoubleBtnType(doubleButtonType);
			submit.setDoubleMasterTaskNo(doubleMasterTaskNo);
		}
		submitDB.updateSubmit(submit);
		// Constants.ISDOUBLERETURN = true;
		Constants.ISCHECKOUTMODUL = true;
		startSubmitService();
	}

	/**
	 * 双向或者自定义提交
	 */
	private void submitModuleOrDouble() {
		submit.setState(Submit.STATE_SUBMITED);
		if ((TextUtils.isEmpty(submit.getTimestamp()))) {
			submit.setTimestamp(String.valueOf(System.currentTimeMillis()));
		}
		// 旧双向如果是双向提交完以后删除该条记录
		if (modType == Constants.MODULE_TYPE_EXECUTE) {
			if (linkId == Constants.DEFAULTINT && TextUtils.isEmpty(sqlLinkJson)) {// 双向如果是超链接过来就不删除
																					// 超链接外部数据不删除
				removeDoubleTask(taskId, currentTargetId);

				DoubleTaskManagerDB dbs = new DoubleTaskManagerDB(this);
				boolean isHas = dbs.isHasDoubleTaskManager(currentTargetId, taskId);
				if (!isHas) {
					DoubleTaskManager m = new DoubleTaskManager();
					m.setDataId(taskId);
					m.setMenuId(currentTargetId);
					dbs.insert(m);
					dbs.controlDoubleTaskManagerNum();
				}
			}

		}
		submitDB.updateSubmit(submit);
		Constants.ISCHECKOUTMODUL = true;
		startSubmitService();
	}

	/**
	 * 就近拜访
	 */
	private void submitNearbyVisit() {
		submit.setState(Submit.STATE_SUBMITED);
		if ((TextUtils.isEmpty(submit.getTimestamp()))) {
			submit.setTimestamp(String.valueOf(System.currentTimeMillis()));
		}
		submitDB.updateSubmit(submit);
		Constants.ISCHECKOUTMODUL = true;
		startSubmitService();
	}

	/**
	 * 开启提交服务
	 */
	private void startSubmitService() {
		List<SubmitItem> submitItemList = submitItemDB.findSubmitItemBySubmitId(submitId);// 根据Submitid找到该ID对应的一组数据
		for (int i = 0; i < submitItemList.size(); i++) {
			funcControlDB.updateFuncControlSubmitState(submitItemList.get(i).getParamName(), targetId);
		}

		funcControlDB.removeFuncControlByState(0);// 删除没提交的
		updateLinkSubmitState(submitId);// 修改超链接提交表单数据状态
		submit();
	}

	@Override
	protected void onResume() {
		isOnPause = false;
		if (locationForCheckOut != null) {
			locationForCheckOut.onAcivityResume();
		}
		super.onResume();
	}

	@Override
	protected void onPause() {
		isOnPause = true;
		super.onPause();
	}

	/**
	 * 更改超链接提交状态
	 * 
	 * @param parentId
	 *            上层的表单ID
	 */
	private void updateLinkSubmitState(int parentId) {
		// 超链接的情况更改子链接的submit的提交状态
		List<Submit> linkSubmitList = submitDB.findSubmitByParentId(parentId);
		if (!linkSubmitList.isEmpty()) {
			submitDB.updateSubmitStateByParentId(parentId);
			for (int i = 0; i < linkSubmitList.size(); i++) {
				Submit linkSubmit = linkSubmitList.get(i);
				updateLinkSubmitState(linkSubmit.getId());
			}
		}
	}

	private int currentTargetId;

	/**
	 * ƒ 双向模块提交以后删除该条记录
	 * 
	 * @param taskId
	 *            任务ID
	 * @param targetId
	 *            数据ID
	 */
	private void removeDoubleTask(int taskId, int targetId) {
		DoubleDB doubleDB = new DoubleDB(this);
		doubleDB.removeDoubleTask(String.valueOf(taskId), String.valueOf(targetId));
	}

	/*
	 * 验证整数
	 */
	public boolean isInteger(String value) {
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
	}

	/**
	 * 根据funcid获取func
	 * 
	 * @param paramName
	 *            funcID
	 * @return
	 */
	private Func getFuncByParamName(String paramName) {
		Func func = null;
		if (menuType == Menu.TYPE_VISIT) {// 访店查询visitFuncDB
			func = visitFuncDB.findFuncListByFuncIdAndTargetId(paramName, currentTargetId);
		} else { // 自定义查询funcDB
			func = funcDB.findFuncListByFuncIdAndTargetId(paramName, currentTargetId);
		}
		return func;
	}

	/**
	 * 关闭预览页面
	 * 
	 * @param resultCode
	 */
	public void thisFinish(Integer resultCode, Intent intent) {
		this.setResult(resultCode, intent);
		this.finish();
	}

	/**
	 * 自定义上报判断基于时间的提交
	 * 
	 * @return true允许提交 false 不允许提交
	 */
	private boolean submitForBaseTime() {
		boolean flag = true;

		if (linkId != Constants.DEFAULTINT) {// 超链接直接通过
			return flag;
		}
		Menu currentMenu = new MainMenuDB(this).findMenuListByMenuId(targetId);
		Integer moduleType = null;
		if (currentMenu != null) {
			moduleType = currentMenu.getModuleType();
		} else {
			return flag;
		}
		Date date = new Date();
		int currentTime = Integer.parseInt(
				(DateUtil.getHour(date) + 100 + "").substring(1) + (DateUtil.getMin(date) + 100 + "").substring(1));
		int baseTime = Integer.parseInt(TextUtils.isEmpty(currentMenu.getBaseTime()) ? currentTime + ""
				: currentMenu.getBaseTime().replace(":", ""));// 超过时间不让提交或者让提交但是日期改为明天的
		if (moduleType != null) {
			if (moduleType == Menu.MENU_MODULE_TYPE_2) {// 超时后不能再提交
				if (currentTime > baseTime) {
					flag = false;
					ToastOrder.makeText(this, PublicUtils.getResourceString(this,R.string.toast_one5), ToastOrder.LENGTH_LONG).show();
				}
			} else if (moduleType == Menu.MENU_MODULE_TYPE_3) {// 把提交时间改成明天
				if (currentTime > baseTime) {
					Func hiddenFunc = new FuncDB(this).findFuncListByTargetIdAndDefaultType(targetId,
							Func.DEFAULT_TYPE_BASE_TIEM);// 基于时间的隐藏域
					if (hiddenFunc != null) {
						SubmitItemDB submitItemDB = new SubmitItemDB(this);
						SubmitItem item = submitItemDB.findSubmitItemBySubmitIdAndFuncId(submitId,
								hiddenFunc.getFuncId());
						if (item != null) {
							item.setParamValue(DateUtil.getTomorrowDate());
							submitItemDB.updateSubmitItem(item, false);
						}
					}
				}
			}
		}
		return flag;
	}

	/**
	 * 超链接点击保存
	 */
	private void controlLink() {
		if (isFromLinkPriview) {
			Intent linkIntent = new Intent(this, LinkFuncActivity.class);
			Bundle priviewBundle = new Bundle();
			priviewBundle.putInt("modType", modType);
			priviewBundle.putInt("targetId", targetId);
			priviewBundle.putInt("linkId", linkId); // 下级连接用来查找上级submitid,以便保存patchId
			priviewBundle.putInt("taskId", taskId);// 双向模块的时候要传到服务端
			priviewBundle.putInt("linkKey", linkKey);// 当前func的id提交的时候存储超链接的值使用
			priviewBundle.putInt("storeId", submit.getStoreId());
			priviewBundle.putInt("menuType", Menu.TYPE_MODULE);
			if (orgMapValueList != null && !orgMapValueList.isEmpty()) {
				priviewBundle.putStringArrayList("map", orgMapValueList);
			}
			linkIntent.putExtra("bundle", priviewBundle);
			startActivity(linkIntent);
			this.finish();
		} else {
			SubmitItemTempDB submitItemTempDB = new SubmitItemTempDB(this);
			SubmitItem item = new SubmitItem(); // 上一级item
			item.setTargetId(targetId + "");
			item.setSubmitId(linkId);
			item.setParamName(linkKey + "");
			if ((TextUtils.isEmpty(submit.getTimestamp()))) {
				submit.setTimestamp(String.valueOf(System.currentTimeMillis()));
			}
			item.setParamValue(submit.getTimestamp());
			item.setType(Func.TYPE_LINK);
			boolean isHas = submitItemDB.findIsHaveParamName(item.getParamName(), linkId);
			if (isHas) {
				submitItemDB.updateSubmitItem(item, false);
			} else {
				submitItemDB.insertSubmitItem(item, false);
			}

			/**
			 * 临时表中也添加数据添加，否则保存外层预览的时候查询不到该链接
			 */
			boolean isHasTemp = submitItemTempDB.findIsHaveParamName(item.getParamName(), linkId);
			if (isHasTemp) {
				submitItemTempDB.updateSubmitItem(item);
			} else {
				submitItemTempDB.insertSubmitItem(item);
			}

			Func linkFunc = new VisitFuncDB(this).findFuncByFuncId(linkKey);
			if (linkFunc == null) {// 无法区分超链接Func是拜访中的还是自定义中的所以两个表都查
				linkFunc = new FuncDB(this).findFuncByFuncId(linkKey);
			}
			if (linkFunc != null) {
				submit.setContentDescription(linkFunc.getName());
			}

			submit.setParentId(linkId);
			submitDB.updateSubmit(submit);

			// jishen2013-02-25保存要提交的临时表中的数据
			List<SubmitItem> submitItemTempList = new SubmitItemTempDB(this).findSubmitItemBySubmitId(submitId);// 根据Submitid找到该ID对应的一组数据
			new SubmitItemDB(this).deleteSubmitItemBySubmitId(submitId);// 删除原来表中的所有数据
			for (int i = 0; i < submitItemTempList.size(); i++) {// 将临时表中的数据添加到提交表中
				SubmitItem tempItem = submitItemTempList.get(i);
				// boolean flag =
				// submitItemDB.findIsHaveParamName(tempItem.getParamName(),
				// submitId);
				// if(flag){
				// submitItemDB.updateSubmitItem(tempItem);
				// }else{
				// submitItemDB.insertSubmitItem(tempItem);
				// }
				tempItem.setTargetId(targetId + "");
				submitItemDB.insertSubmitItem(tempItem, false);
				new SubmitItemTempDB(this).deleteSubmitItem(tempItem);
			}
			if (!TextUtils.isEmpty(menuType2) && menuType2.equals(Menu.TYPE_VISIT + "")) {// 如果是来自拜访中的超链接保存
				Intent closeIntent = new Intent();
				closeIntent.setAction(HistorySearchPopupWindow.CLOSE_ACTION);
				sendBroadcast(closeIntent);
			}
			thisFinish(R.id.submit_succeeded, null);
			JLog.d(TAG, "超链接添加Timestamp ===>" + submit.getTimestamp());
		}
	}

	/**
	 * 上报数据的时候更改提交时间
	 */
	private void updateMenuSubmitTime() {
		MainMenuDB mainMenuDB = new MainMenuDB(this);
		Menu currentMenu = mainMenuDB.findMenuListByMenuId(targetId);
		if (currentMenu != null) {
			currentMenu.setSubmitTime(DateUtil.getCurDate());
			mainMenuDB.updateMenuByMenuId(currentMenu);
		}

	}

	private Handler initUiHandle = new Handler() {
		public void handleMessage(Message msg) {
			int what = msg.what;
			if (what == 1) {
				if (initUiDialog.isShowing()) {
					initUiDialog.dismiss();
				}

				if (linkId != Constants.DEFAULTINT) {// 如果是超链的话显示确定否则显示提交
					if (isFromLinkPriview) {
						tv_submit.setText(PublicUtils.getResourceString(FuncDetailActivity.this,R.string.editor));
					} else {
						tv_submit.setText(PublicUtils.getResourceString(FuncDetailActivity.this,R.string.save1));
					}
				}

				if (FuncDetailActivity.this instanceof Order3FuncDetailActivity) {
					tv_submit.setText(PublicUtils.getResourceString(FuncDetailActivity.this,R.string.save1));
				}

			} else if (what == 2) {
				View view = (View) msg.obj;
				showDataLayout.addView(view);
			} else if (what == 3) {// 添加超链接
				View view = (View) msg.obj;
				showDataLayout.addView(view, 0);
			} else if (what == 4) {
				ll_photoPreview.setVisibility(View.VISIBLE);
			}
		};
	};

	private void addView(View view) {
		Message msg = initUiHandle.obtainMessage(2);
		msg.obj = view;
		initUiHandle.sendMessage(msg);
	}

	@Override
	public void confirmLocation(LocationResult result) {
		sureCheckOut(result);// 确认提交

	}

	@Override
	public void areaSearchLocation(LocationResult result) {

	}

	/**
	 * 提交数据
	 * 
	 */
	public void submit() {
		// isNoWait = false;
		if (submit != null) {
			submit.setContentDescription(contentDescription());
			submitDB.updateSubmit(submit);
		}
		SubmitManager.getInstance(this).submitData(isNoWait, this);
	}

	@Override
	public void submitReceive(boolean isSuccess) {
		submitDataLayout.setEnabled(true);
		if (isSuccess) {
			if (menuType == Menu.TYPE_VISIT) {// 拜访店面提交成功的时候店面提交个数要加1
				VisitStoreDB visitStoreDB = new VisitStoreDB(this);
				VisitStore visitStore = visitStoreDB.findVisitStore(submit.getWayId(), submit.getTargetid(),
						submit.getStoreId(), submit.getPlanId());
				if (visitStore != null) {
					visitStore.setSubmitNum(visitStore.getSubmitNum() + 1);
					visitStore.setSubmitDate(DateUtil.getCurDate());
					visitStoreDB.updateVisitStoreById(visitStore);
				}
			}
			Intent intent = null;
			if (menuType == Menu.TYPE_MODULE) {// 自定义情况
				if (modType == Constants.MODULE_TYPE_EXECUTE) {// 双向提交
					intent = new Intent();
					intent.putExtra("doubleSubmit", true);
				}
			}
			thisFinish(R.id.submit_succeeded, intent);
		} else {
			if (submit != null) {
				submit.setState(Submit.STATE_NO_SUBMIT);
				submitDB.updateSubmit(submit);
			}
		}
	}

	public String contentDescription() {
		String contentDescription = "";
		if (menuType == Menu.TYPE_VISIT) {
			contentDescription = submit.getWayName() + "-" + submit.getStoreName();
		} else if (menuType == Menu.TYPE_NEW_TARGET) {// 新双向
			int targetid = submit.getTargetid();
			Module doubleModule = new ModuleDB(this).findModuleByTargetId(targetid, Constants.MODULE_TYPE_EXECUTE_NEW);
			if (doubleModule != null) {
				contentDescription = doubleModule.getName();
			}
		} else if (menuType == Menu.TYPE_NEARBY) {
			int targetid = submit.getTargetid();
			Menu menu = new MainMenuDB(this).findMenuListByMenuId(targetid);
			if (menu != null) {
				contentDescription = menu.getName();
			}
		} else if (menuType == Menu.TYPE_MODULE) {// 自定义
			if (submit.getModType() != null) {
				int targetid = submit.getTargetid();
				Module module = new ModuleDB(this).findModuleByTargetId(targetid, submit.getModType());
				if (module != null) {
					contentDescription = module.getName();
				}
			}
		}
		return contentDescription;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt("submitId", submitId);
		outState.putInt("linkId", linkId);
		outState.putInt("taskId", taskId);
		outState.putInt("attState", attState);
		outState.putInt("linkKey", linkKey);
		outState.putString("sqlLinkJson", sqlLinkJson);
		outState.putBoolean("isFromLinkPriview", isFromLinkPriview);
		outState.putInt("menuType", menuType);
		outState.putInt("targetId", targetId);
		outState.putString("ableStatus", ableStatus);
		outState.putInt("infoType", infoType);
		outState.putBundle("bundle", bundle);
		outState.putBoolean("codeSubmitControl", codeSubmitControl);
		outState.putBoolean("isNoWait", isNoWait);
		outState.putInt("isCheckout", isCheckout);
		outState.putStringArrayList("map", orgMapValueList);
		super.onSaveInstanceState(outState);
	}

	/**
	 * 读取数据-->非正常状态下使用
	 */
	@Override
	protected void restoreConstants(Bundle savedInstanceState) {
		super.restoreConstants(savedInstanceState);
		isCheckout = savedInstanceState.getInt("isCheckout");
		submitId = savedInstanceState.getInt("submitId");
		linkId = savedInstanceState.getInt("linkId");
		taskId = savedInstanceState.getInt("taskId");
		attState = savedInstanceState.getInt("attState");
		linkKey = savedInstanceState.getInt("linkKey");
		sqlLinkJson = savedInstanceState.getString("sqlLinkJson");
		isFromLinkPriview = savedInstanceState.getBoolean("isFromLinkPriview");
		menuType = savedInstanceState.getInt("menuType");
		modType = savedInstanceState.getInt("modType");
		targetId = savedInstanceState.getInt("targetId");
		ableStatus = savedInstanceState.getString("ableStatus");
		infoType = savedInstanceState.getInt("infoType");
		bundle = savedInstanceState.getBundle("bundle");
		codeSubmitControl = savedInstanceState.getBoolean("codeSubmitControl");
		isNoWait = savedInstanceState.getBoolean("isNoWait");
		orgMapValueList = savedInstanceState.getStringArrayList("map");
		if (bundle != null) {
			doubleMasterTaskNo = bundle.getString(NewDoubleDetailActivity.DOUBLE_MASTER_TASK_NO);
			doubleButtonType = bundle.getString(NewDoubleDetailActivity.DOUBLE_BTN_TYPE);
		}
	}

}
