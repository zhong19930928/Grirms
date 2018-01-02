package com.yunhu.yhshxc.comp.spinner;

import gcg.org.debug.JLog;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.Submit;
import com.yunhu.yhshxc.bo.SubmitItem;
import com.yunhu.yhshxc.comp.CompDialog;
import com.yunhu.yhshxc.database.SubmitItemDB;
import com.yunhu.yhshxc.database.SubmitItemTempDB;
import com.yunhu.yhshxc.listener.OnSingleChoiceConfirmListener;
import com.yunhu.yhshxc.widget.SingleChoiceFuzzyQuerySpinner;

/**
 * 模糊搜索下拉框
 * 
 * @author jishen
 */
public class FuzzySingleChoiceSpinnerComp extends AbsSelectComp implements OnSingleChoiceConfirmListener {
	private String TAG = "FuzzySingleChoiceSpinnerComp";
	public SingleChoiceFuzzyQuerySpinner singleChoiceFuzzyQuerySpinner;
	private Context context;
	private Func func;
	private Bundle replenishBundle = null;// 携带查询条件的bundle
	private OnMuktiChiceSelecteLister lisner;

	/**
	 * 控件不在表格中使用
	 * 
	 * @param context
	 * @param func
	 * @param queryWhere
	 *            上级级联关系
	 * @param orgBundle
	 *            机构级联关系
	 * @param compDialog
	 */
	public FuzzySingleChoiceSpinnerComp(Context context, Func func, String queryWhere, Bundle orgBundle,
			CompDialog compDialog, OnMuktiChiceSelecteLister lisner) {
		super(context, func, queryWhere, orgBundle);
		this.context = context;
		this.func = func;
		this.lisner = lisner;
		this.compFunc = func;
		this.putData(compDialog.type, compDialog.wayId, compDialog.planId, compDialog.storeId, compDialog.targetid,
				compDialog.targetType);
		this.setReplenishBundle(compDialog.replenish);
		this.setOrgLevelMap(compDialog.getOrgMap());
		init();
	}

	/**
	 * 控件在表格中
	 * 
	 * @param context
	 * @param func
	 * @param queryWhere
	 *            上级级联关系
	 * @param orgBundle
	 *            机构级联关系
	 */
	public FuzzySingleChoiceSpinnerComp(Context context, Func func, String queryWhere, Bundle orgBundle) {
		super(context, func, queryWhere, orgBundle);
		this.context = context;
		this.func = func;
		this.compFunc = func;
		init();
	}

	/**
	 * 创建下拉列表
	 */
	public void create() {
		if (func.getType() == Func.TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY_COMP) {// 其他类型的添加其他项
			Dictionary dictionary = new Dictionary();
			dictionary.setCtrlCol(context.getResources().getString(R.string.other));
			dictionary.setDid("-1");
			if (dataSrcList == null) {
				dataSrcList = new ArrayList<Dictionary>();
			}
			dataSrcList.add(dictionary);
		}
		singleChoiceFuzzyQuerySpinner.setSrcDictList(dataSrcList);// 设置数据源
	}

	/**
	 * 初始化实例
	 */
	private void init() {
		view = View.inflate(context, R.layout.comp_fuzzy_single_choice_spinner, null);
		singleChoiceFuzzyQuerySpinner = (SingleChoiceFuzzyQuerySpinner) view
				.findViewById(R.id.singleChoiceFuzzyQuerySpinner);
		singleChoiceFuzzyQuerySpinner.setOnSingleChoiceConfirmListener(this);
	}

	/**
	 * 
	 * @param 二次取数据时，让下拉框显示选中的那条数据
	 */
	public void setSelected(String did) {
		this.value = did;
		if (TextUtils.isEmpty(did)) {
			singleChoiceFuzzyQuerySpinner.setCurrentPosition(-1);
			singleChoiceFuzzyQuerySpinner.create();
			if (nextComp != null) {// 拼接下级级联关系
				nextComp.queryWhere = queryWhere + "@" + "-2";
			}
		} else {
			if (!TextUtils.isEmpty(did) && did.equals("-1")) {// 选择了其他
				if (!isInTable) {// 没有在表格里
					SubmitItem item_ = null;
					if (isLink) {// 超链接
						item_ = new SubmitItemTempDB(context).findSubmitItem(null, Submit.STATE_NO_SUBMIT, planId,
								wayId, storeId, targetid, targetType, func.getFuncId() + "_other");
					} else {
						item_ = new SubmitItemDB(context).findSubmitItem(null, Submit.STATE_NO_SUBMIT, planId, wayId,
								storeId, targetid, targetType, func.getFuncId() + "_other");
					}
					if (item_ != null && replenishBundle == null) {
						value = item_.getParamValue();
					} else {// 查询条件的情况
						if (replenishBundle != null && replenishBundle.containsKey(func.getFuncId() + "_other")) {
							value = replenishBundle.getString(func.getFuncId() + "_other");
						}
					}
					singleChoiceFuzzyQuerySpinner.setCurrentPosition(dataSrcList.size() - 1);
					singleChoiceFuzzyQuerySpinner.setOtherVaule(value);
				} else {// 表格中
					if (currentMap != null) {
						singleChoiceFuzzyQuerySpinner.setCurrentPosition(dataSrcList.size() - 1);
						singleChoiceFuzzyQuerySpinner.setOtherVaule(currentMap.get(func.getFuncId() + "_other"));// 获取填写的其他值
						value = currentMap.get(func.getFuncId() + "_other");
					}
				}
				singleChoiceFuzzyQuerySpinner.create();// 创建下拉框
			} else if (dataSrcList != null && !TextUtils.isEmpty(did)) {// 没选其他
				int i = 0;
				for (Dictionary dict : dataSrcList) {
					JLog.d(TAG, dataSrcList.get(i).getCtrlCol());
					if (dict.getDid().equals(did)) {// 设置当前选择的值
						JLog.d(TAG, func.getName() + "=>seleced:" + i);
						singleChoiceFuzzyQuerySpinner.setCurrentPosition(i);
						value = did;
						singleChoiceFuzzyQuerySpinner.create();
						break;
					}
					i++;
				}
			}
		}
		if (nextComp != null && isInTable) {// 多选的时候清空下级
			oneToMany();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gcg.grirms.comp.spinner.AbsSelectComp#setIsEnable(boolean)
	 */
	@Override
	public void setIsEnable(boolean isEnable) {
		singleChoiceFuzzyQuerySpinner.setEnabled(isEnable);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gcg.grirms.listener.OnSingleChoiceConfirmListener#OnConfirm(
	 * java.lang.String, boolean)
	 */
	@Override
	public void OnConfirm(String chooseString, String restr, boolean isShowOther) {

		if (TextUtils.isEmpty(chooseString)) {// 没选择值
			singleChoiceFuzzyQuerySpinner.setCurrentPosition(-1);
			singleChoiceFuzzyQuerySpinner.create();
			value = chooseString;
		} else {
			if (isShowOther && isOther(chooseString)) {// 选择了其他 并且其他输入框了输入的是数字
				value = chooseString.trim() + " ";// 添加一个空格 以后验证其他的时候要使用
			} else {
				value = chooseString;
			}
			if (nextComp != null && isInTable) {// 下级不为空并且是在表格里
				if (isShowOther && !isOther(chooseString)) {// 如果输入的是其他值输入框显示，并且选中的值不是数字
					if (queryWhere == null) {
						nextComp.queryWhere = "-1";
					} else {
						nextComp.queryWhere = queryWhere + "@" + "-1";
					}
				} else {
					if (queryWhere == null) {
						nextComp.queryWhere = value;
					} else {
						nextComp.queryWhere = queryWhere + "@" + value;
					}
				}

				oneToMany();
			}
			if (!TextUtils.isEmpty(restr)) {
				dataName = restr;
			} else {
				dataName = value;
			}
			JLog.d(TAG, "value==>" + value);
			if (lisner != null) {
				lisner.onMuktiConfirm();
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gcg.grirms.listener.OnSingleChoiceConfirmListener#fuzzySearch
	 * (java.lang.String)
	 */
	@Override
	public void fuzzySearch(String fuzzyContent) {
		getDataSrcList(fuzzyContent, queryWhereForDid);
		create();
	}

	/*
	 * 验证其他下拉框
	 */
	public boolean isOther(String value) {
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gcg.grirms.comp.spinner.AbsSelectComp#notifyDataSetChanged()
	 */
	@Override
	public void notifyDataSetChanged() {
		getDataSrcList(null, queryWhereForDid);
		create();
	}

	/**
	 * 设置初始化数据
	 */
	public void setInitData() {

		if (func.getDefaultType() != null && func.getDefaultType() == Func.DEFAULT_TYPE_SQL) {// sql类型
			String itemValue = getDefaultSql();// 查询数据库中当前对应的值
			if (!TextUtils.isEmpty(itemValue)) {
				queryWhereForDid = itemValue;
			} else {
				queryWhereForDid = "-99";
			}
			notifyDataSetChanged();
			// sql类型的下拉框 sql查询到的结果不一定是最终的value
			// value = itemValue;
			// setSelected(itemValue);//设置选择的值
			JLog.d(TAG, "itemValue==>" + itemValue);
		} else {
			notifyDataSetChanged();
			if (isInTable && currentMap != null) {// 在表格中
				this.value = currentMap.get(func.getFuncId() + "");
				setSelected(value);
			} else {
				SubmitItem item = null;
				if (isLink) {// 超链接情况
					item = new SubmitItemTempDB(context).findSubmitItem(null, Submit.STATE_NO_SUBMIT, planId, wayId,
							storeId, targetid, targetType, String.valueOf(func.getFuncId()));
				} else {
					item = new SubmitItemDB(context).findSubmitItem(null, Submit.STATE_NO_SUBMIT, planId, wayId,
							storeId, targetid, targetType, String.valueOf(func.getFuncId()));
				}
				if (item != null && replenishBundle == null) {// 不是查询条件
					this.value = item.getParamValue();
					setSelected(value);
					JLog.d(TAG, "value==>" + value);
				} else {
					if (replenishBundle != null && replenishBundle.containsKey(func.getFuncId() + "")) {// 查询条件
						value = replenishBundle.getString(func.getFuncId() + "");
						setSelected(replenishBundle.getString(func.getFuncId() + ""));
						JLog.d(TAG, "itemValue==>" + value);
					}
				}
			}
			// setSelected(value);
		}
		singleChoiceFuzzyQuerySpinner.create();// 创建下拉列表
	}

	public void setReplenishBundle(Bundle bundle) {
		this.replenishBundle = bundle;
	}

	public Bundle getReplenishBundle() {
		return replenishBundle;
	}

}
