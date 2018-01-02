package com.yunhu.yhshxc.nearbyVisit;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.ChangeModuleFuncActivity;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.bo.Submit;
import com.yunhu.yhshxc.bo.SubmitItem;
import com.yunhu.yhshxc.database.FuncDB;
import com.yunhu.yhshxc.database.SubmitDB;
import com.yunhu.yhshxc.database.SubmitItemDB;
import com.yunhu.yhshxc.list.activity.TableListActivity;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.SharedPreferencesUtilForNearby;
import com.yunhu.yhshxc.widget.ToastOrder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gcg.org.debug.JLog;

public class NearbyVisitHistoryActivity extends TableListActivity{

	private int storeId;
	private String patchId;
	private int type;

	@Override
	protected void init() {
		super.init();
		queryIconContainer.setVisibility(View.GONE);//不显示查询按钮
	}
	@Override
	public void initIntentData() {
		bundle = getIntent().getBundleExtra("bundle");
		storeId = bundle.getInt("storeId");
		targetId = bundle.getInt("targetId");
		type = bundle.getInt("type");
	}

	@Override
	protected String titleName() {
		if (type == Constants.MODULE_TYPE_UPDATE) {//修改
			return setString(R.string.nearby_visit_alter);
		}else if (type == Constants.MODULE_TYPE_VERIFY) {//审核
			return setString(R.string.nearby_visit_audit);
		}
		return super.titleName();
	}


	@Override
	protected HashMap<String, String> getSearchParams() {
		HashMap<String, String> searchParams = new HashMap<String, String>();
		searchParams.put("taskid", String.valueOf(targetId));
		if ("0".equals(SharedPreferencesUtilForNearby.getInstance(this).getNearbyDataStatus(String.valueOf(targetId)))) {
			searchParams.put("storeid", String.valueOf(storeId));
		}else{
			searchParams.put("patchid", patchId);
		}
		searchParams.put("type", String.valueOf(type));
		searchParams.put("nearby", "1");
		JLog.d(TAG, "searchParams:"+searchParams.toString());
		return searchParams;
	}


	@Override
	protected void initTitle(List<Func> viewCoum, LinearLayout ll_title) {
		unViewCoum = new FuncDB(this).findFuncListReplenish(targetId, 0, null, null);
		if (viewCoum == null || viewCoum.isEmpty()) {
			ToastOrder.makeText(this,setString(R.string.nearby_visit_04), ToastOrder.LENGTH_SHORT).show();
			this.finish();
		}
		this.viewCoum = viewCoum;
		unitViewWidth = computeViewWidth(viewCoum.size(), null);
		TextView tv_title_1 = (TextView) View.inflate(this, R.layout.table_list_item_unit, null);
		tv_title_1.setGravity(Gravity.CENTER);//标题设置居中
		tv_title_1.setLayoutParams(new LayoutParams(PublicUtils.convertDIP2PX(this, 62), LayoutParams.WRAP_CONTENT));
		tv_title_1.setText(setString(R.string.nearby_visit_state));
		tv_title_1.setTextColor(Color.rgb(255, 255, 255));
		tv_title_1.setTextSize(18);
		ll_title.addView(tv_title_1);

		for (Func func : viewCoum) {// 循环添加要显示func的列名
			TextView tv_title = (TextView) View.inflate(this, R.layout.table_list_item_unit, null);
			int width = func.getColWidth() == null || func.getColWidth() == 0 ? unitViewWidth : func.getColWidth();
			tv_title.setGravity(Gravity.CENTER);//标题设置居中
			tv_title.setLayoutParams(new LayoutParams(width, LayoutParams.WRAP_CONTENT));
			tv_title.setText(func.getName());
			tv_title.setTextColor(Color.rgb(255, 255, 255));
			tv_title.setTextSize(18);
			ll_title.addView(tv_title);
		}
		btName = getButtonName();
	}

	@Override
	public String getButtonName() {
		if (type == Constants.MODULE_TYPE_UPDATE) {//修改
			return setString(R.string.nearby_visit_alter);
		}else if (type == Constants.MODULE_TYPE_VERIFY) {//审核
			return setString(R.string.nearby_visit_audit);
		}
		return setString(R.string.nearby_visit_report);
	}


	@Override
	public void btnControl(View parentView, int groupPosition, BtnBo bo) {
		Button bt_do = (Button) parentView.findViewById(R.id.bt_table_list_item_children_do);
		Button bt_copy = (Button) parentView.findViewById(R.id.bt_table_list_item_children_copy);
		bt_do.setText(getButtonName());
		bt_copy.setText(setString(R.string.nearby_visit_copy));
		bt_do.setTag(bo);
		bt_copy.setTag(bo);
		bt_do.setOnClickListener(this);
		bt_copy.setOnClickListener(this);
		if (type == Constants.MODULE_TYPE_UPDATE) {//修改的时候显示“修改”和“复制”按钮 查询的时候只显示“复制”按钮
			bt_do.setVisibility(View.VISIBLE);
			if (isShowCopyBtn()) {
				bt_copy.setVisibility(View.VISIBLE);
			}else{
				bt_copy.setVisibility(View.GONE);
			}
		}else if(type == Constants.MODULE_TYPE_VERIFY){//审核的时候显示"审核","复制"按钮 
			bt_do.setVisibility(View.VISIBLE);
			if (isShowCopyBtn()) {
				bt_copy.setVisibility(View.VISIBLE);
			}else{
				bt_copy.setVisibility(View.GONE);
			}
		}else{//查询
			bt_do.setVisibility(View.GONE);
			if (isShowCopyBtn()) {
				bt_copy.setVisibility(View.VISIBLE);
			}else{
				bt_copy.setVisibility(View.GONE);
			}
		}
	}

	private boolean  isShowCopyBtn(){
		boolean flag = false;
		String isCopy = SharedPreferencesUtilForNearby.getInstance(this).getNearbyIsDataCpy(String.valueOf(targetId));
		String datastus = SharedPreferencesUtilForNearby.getInstance(this).getNearbyDataStatus(String.valueOf(targetId));
		flag = "1".equals(isCopy) && "0".equals(datastus);
		return flag;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.bt_table_list_item_children_do) {//修改或审核
			update((BtnBo)v.getTag());
		}else if(v.getId() == R.id.bt_table_list_item_children_copy){
			copy((BtnBo)v.getTag());
		}else{
			super.onClick(v);
		}
	}

	@Override
	public void onLocalVoiceInteractionStarted() {
		super.onLocalVoiceInteractionStarted();
	}

	private void update(BtnBo bo){
		// 跳转的Activity
		Intent intent = new Intent(this, ChangeModuleFuncActivity.class);
		intent.putExtra("isNoWait", isNoWait);
		bundle.putInt("menuType", Menu.TYPE_NEARBY);
		bundle.putInt("targetId", targetId);
		bundle.putInt("taskId", Integer.parseInt(bo.getTaskid()));
		bundle.putString("status", bo.getStatus());
		bundle.putString("buttonName", getButtonName());
		// 保存数据到数据库
		SubmitDB submitDB = new SubmitDB(this);
		SubmitItemDB submitItemDB = new SubmitItemDB(this);
		ArrayList<String> orgString = new ArrayList<String>();
		saveData(targetId, Menu.TYPE_NEARBY, bo.getItemData(), submitDB, submitItemDB, orgString, false, 0, null);
		if (orgString != null && !orgString.isEmpty()) {
			bundle.putStringArrayList("map", orgString);
		}
		intent.putExtra("bundle", bundle);
		startActivity(intent);
		this.sendBroadcast(new Intent(Constants.BROADCAST_ACTION_NEARBY_COPY));
		finish();
	}

	boolean isCopy = false;
	private void copy(BtnBo bo){
		isCopy = true;
		// 跳转的Activity
//		Intent intent = new Intent(this, ChangeModuleFuncActivity.class);
		Intent intent = new Intent(this, NearbyVisitFuncActivity.class);
		intent.putExtra("isNoWait", isNoWait);
		bundle.putInt("menuType", Menu.TYPE_NEARBY);
		bundle.putInt("targetId", targetId);
		bundle.putInt("taskId", Integer.parseInt(bo.getTaskid()));
		bundle.putString("status", "0");
		bundle.putString("buttonName", getButtonName());
		bundle.putInt("targetType",Menu.TYPE_NEARBY);
		// 保存数据到数据库
		SubmitDB submitDB = new SubmitDB(this);
		SubmitItemDB submitItemDB = new SubmitItemDB(this);
		ArrayList<String> orgString = new ArrayList<String>();
		saveData(targetId, Menu.TYPE_NEARBY, bo.getItemData(), submitDB, submitItemDB, orgString, false, 0, null);
		if (orgString != null && !orgString.isEmpty()) {
			bundle.putStringArrayList("map", orgString);
		}
		intent.putExtra("bundle", bundle);
		startActivity(intent);
		this.sendBroadcast(new Intent(Constants.BROADCAST_ACTION_NEARBY_COPY));
		finish();
	}

	@Override
	public void saveData(int targetId, int menuType,Map<String, String> saveItem, SubmitDB submitDB,SubmitItemDB submitItemDB, ArrayList<String> orgString,boolean isLink, int submitParentId, Func linkItemFunc) {
		cleanCurrentNoSubmit(targetId);
		if (saveItem != null && !saveItem.isEmpty()) {
			Submit submit = new Submit();
			submit.setTargetid(targetId);
			submit.setTargetType(menuType);
			String patchId = saveItem.get(Constants.PATCH_ID);
			if (!TextUtils.isEmpty(patchId)) {
				if (isCopy) {
					submit.setTimestamp(String.valueOf(System.currentTimeMillis()));
				}else{
					submit.setTimestamp(patchId);
				}
			}
			String taskId = saveItem.get(Constants.TASK_ID);
			if (!TextUtils.isEmpty(taskId)) {
				submit.setDoubleId(Integer.valueOf(taskId));
			}
			submit.setStoreId(storeId);
			submit.setState(Submit.STATE_NO_SUBMIT);
			if (isLink) {
				submit.setParentId(submitParentId);
				Submit parentSubmit = submitDB.findSubmitById(submitParentId);
				SubmitItem submitItem = new SubmitItem();
				submitItem.setSubmitId(parentSubmit.getId());
				submitItem.setType(Func.TYPE_LINK);
				submitItem.setParamName(linkItemFunc.getFuncId().toString());
				submitItem.setParamValue(submit.getTimestamp());
				submitItem.setIsCacheFun(linkItemFunc.getIsCacheFun());
				submitItem.setTargetId(linkItemFunc.getTargetid()+"");
				submitItemDB.insertSubmitItem(submitItem,false);
			}
			submit.setMenuId(menuId);
			submit.setMenuType(menuType);
			submit.setMenuName(menuName);
			submitDB.insertSubmit(submit);
			// 通过targetId/targetType/Submit.State查询submitId
			int submitId = submitDB.selectSubmitIdNotCheckOut(null, null, null, targetId, menuType, Submit.STATE_NO_SUBMIT);

//			List<Func> 	funcs = new FuncDB(this).findFuncListByTargetidReplash(targetId, saveItem.get(Constants.DATA_STATUS), isSqlLink);

			List<Func> 	funcs = null;
			if (isCopy) {
				funcs = new FuncDB(this).findNearbyFuncList(targetId,"0");
			}else{
				funcs = new FuncDB(this).findFuncListByTargetidReplash(targetId, saveItem.get(Constants.DATA_STATUS), isSqlLink);
			}

			StringBuilder sb = new StringBuilder();
			for (Func func : funcs) {// 遍历
				if (func.getType() == Func.TYPE_CAMERA || func.getType() == Func.TYPE_CAMERA_HEIGHT || func.getType() == Func.TYPE_CAMERA_CUSTOM || func.getType() == Func.TYPE_CAMERA_MIDDLE || func.getType() == Func.TYPE_BUTTON) {
					continue;
				}

				if (isCopy && func.getType() == Func.TYPE_LOCATION) {//就近拜访如果是复制数据的话不复制定位和拍照信息
					continue;
				}

				if (!TextUtils.isEmpty(saveItem.get(func.getFuncId().toString()))) {
					if (func.getType() == Func.TYPE_LINK) {
						saveData(func.getMenuId(), menuType, parseLinkJson(saveItem.get(func.getFuncId().toString())), submitDB, submitItemDB, orgString, true, submitId, func);
					}else {
						SubmitItem submitItem = new SubmitItem();
						submitItem.setSubmitId(submitId);
						submitItem.setType(func.getType());
						submitItem.setIsCacheFun(func.getIsCacheFun());
						submitItem.setTargetId(func.getTargetid()+"");
						if (func.getOrgOption()!=null && func.getOrgOption()== Func.ORG_STORE) {
							submitItem.setNote(SubmitItem.NOTE_STORE);
						}
						if (func.getOrgOption()!=null && func.getOrgOption() == Func.OPTION_LOCATION) {
							SubmitItem storeItem = new SubmitItemDB(this).findSubmitItemByNote(submitId, SubmitItem.NOTE_STORE);
							if (storeItem!=null && !TextUtils.isEmpty(storeItem.getParamValue())) {
								submitItem.setNote(storeItem.getParamName());
							}
						}
						submitItem.setParamName(func.getFuncId().toString());
						if (func.getType() == Func.TYPE_SQL_BIG_DATA) {
							String json = saveItem.get(func.getFuncId().toString());
							try {
								JSONArray array = new JSONArray(json);
								submitItem.setParamValue(array.getString(0));
								submitItem.setNote(array.getString(1));
							}
							catch (JSONException e) {
								e.printStackTrace();
							}
						}else {
							submitItem.setParamValue(saveItem.get(func.getFuncId().toString()));
						}
						submitItemDB.insertSubmitItem(submitItem,false);
						if ((func.getType() == Func.TYPE_SELECT_OTHER || func.getType() == Func.TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY_COMP) && saveItem.get(func.getFuncId().toString()).equals("-1")) {
							SubmitItem submitItem_other = new SubmitItem();
							submitItem_other.setSubmitId(submitId);
							submitItem_other.setType(func.getType());
							submitItem_other.setTargetId(func.getTargetid()+"");
							submitItem_other.setParamName(func.getFuncId().toString() + "_other");
							submitItem_other.setParamValue(saveItem.get(func.getFuncId().toString() + "_other"));
							submitItemDB.insertSubmitItem(submitItem_other,false);
						}
						if (func.getOrgOption() != null && func.getOrgOption() == Func.ORG_OPTION) {
							sb.append(";").append(func.getOrgLevel()).append(":").append(saveItem.get(func.getFuncId().toString()));
						}
						if (func.getType() == Func.TYPE_PRODUCT_CODE && !TextUtils.isEmpty(saveItem.get("compare_content"))) {
							SharedPreferencesUtil.getInstance(this).setCompareContent("content_"+func.getTargetid(),saveItem.get("compare_content"));
						}
					}
				}
			}
			if(isLink){
				submitDB.updateTargetType(Menu.TYPE_MODULE,submitId);
			}
			if (sb != null && sb.length() > 1) {
				orgString.add(targetId + sb.toString());
			}
		}
	}

	public void saveDataCopy(int targetId, int menuType,Map<String, String> saveItem, SubmitDB submitDB,SubmitItemDB submitItemDB, ArrayList<String> orgString,boolean isLink, int submitParentId, Func linkItemFunc) {
		cleanCurrentNoSubmit(targetId);
		if (saveItem != null && !saveItem.isEmpty()) {
			Submit submit = new Submit();
			submit.setTargetid(targetId);
			submit.setTargetType(menuType);
			String patchId = saveItem.get(Constants.PATCH_ID);
			if (!TextUtils.isEmpty(patchId)) {
				if (isCopy) {
					submit.setTimestamp(String.valueOf(System.currentTimeMillis()));
				}else{
					submit.setTimestamp(patchId);
				}
			}
			String taskId = saveItem.get(Constants.TASK_ID);
			if (!TextUtils.isEmpty(taskId)) {
				submit.setDoubleId(Integer.valueOf(taskId));
			}
			submit.setStoreId(storeId);
			submit.setState(Submit.STATE_NO_SUBMIT);
			if (isLink) {
				submit.setParentId(submitParentId);
				Submit parentSubmit = submitDB.findSubmitById(submitParentId);
				SubmitItem submitItem = new SubmitItem();
				submitItem.setSubmitId(parentSubmit.getId());
				submitItem.setType(Func.TYPE_LINK);
				submitItem.setParamName(linkItemFunc.getFuncId().toString());
				submitItem.setParamValue(submit.getTimestamp());
				submitItem.setIsCacheFun(linkItemFunc.getIsCacheFun());
				submitItem.setTargetId(linkItemFunc.getTargetid()+"");
				submitItemDB.insertSubmitItem(submitItem,false);
			}
			submit.setMenuId(menuId);
			submit.setMenuType(menuType);
			submit.setMenuName(menuName);
			submitDB.insertSubmit(submit);
			// 通过targetId/targetType/Submit.State查询submitId
			int submitId = submitDB.selectSubmitIdNotCheckOut(null, null, null, targetId, menuType, Submit.STATE_NO_SUBMIT);
//       List<Func>     funcs = new FuncDB(this).findFuncListByTargetidReplash(targetId, saveItem.get(Constants.DATA_STATUS), isSqlLink);

			List<Func>     funcs = null;
			if (isCopy) {
				funcs = new FuncDB(this).findNearbyFuncList(targetId,"0");
			}else{
				funcs = new FuncDB(this).findFuncListByTargetidReplash(targetId, saveItem.get(Constants.DATA_STATUS), isSqlLink);
			}

			StringBuilder sb = new StringBuilder();
			for (Func func : funcs) {// 遍历
				if (func.getType() == Func.TYPE_CAMERA || func.getType() == Func.TYPE_CAMERA_HEIGHT || func.getType() == Func.TYPE_CAMERA_CUSTOM || func.getType() == Func.TYPE_CAMERA_MIDDLE || func.getType() == Func.TYPE_BUTTON) {
					continue;
				}

				if (isCopy && func.getType() == Func.TYPE_LOCATION) {//就近拜访如果是复制数据的话不复制定位和拍照信息
					continue;
				}

				if (!TextUtils.isEmpty(saveItem.get(func.getFuncId().toString()))) {
					if (func.getType() == Func.TYPE_LINK) {
						saveDataCopy(func.getMenuId(), menuType, parseLinkJson(saveItem.get(func.getFuncId().toString())), submitDB, submitItemDB, orgString, true, submitId, func);
					}else {
						SubmitItem submitItem = new SubmitItem();
						submitItem.setSubmitId(submitId);
						submitItem.setType(func.getType());
						submitItem.setIsCacheFun(func.getIsCacheFun());
						submitItem.setTargetId(func.getTargetid()+"");
						if (func.getOrgOption()!=null && func.getOrgOption()== Func.ORG_STORE) {
							submitItem.setNote(SubmitItem.NOTE_STORE);
						}
						if (func.getOrgOption()!=null && func.getOrgOption() == Func.OPTION_LOCATION) {
							SubmitItem storeItem = new SubmitItemDB(this).findSubmitItemByNote(submitId, SubmitItem.NOTE_STORE);
							if (storeItem!=null && !TextUtils.isEmpty(storeItem.getParamValue())) {
								submitItem.setNote(storeItem.getParamName());
							}
						}
						submitItem.setParamName(func.getFuncId().toString());
						if (func.getType() == Func.TYPE_SQL_BIG_DATA) {
							String json = saveItem.get(func.getFuncId().toString());
							try {
								JSONArray array = new JSONArray(json);
								submitItem.setParamValue(array.getString(0));
								submitItem.setNote(array.getString(1));
							}
							catch (JSONException e) {
								e.printStackTrace();
							}
						}else {
							submitItem.setParamValue(saveItem.get(func.getFuncId().toString()));
						}
						submitItemDB.insertSubmitItem(submitItem,false);
						if ((func.getType() == Func.TYPE_SELECT_OTHER || func.getType() == Func.TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY_COMP) && saveItem.get(func.getFuncId().toString()).equals("-1")) {
							SubmitItem submitItem_other = new SubmitItem();
							submitItem_other.setSubmitId(submitId);
							submitItem_other.setType(func.getType());
							submitItem_other.setTargetId(func.getTargetid()+"");
							submitItem_other.setParamName(func.getFuncId().toString() + "_other");
							submitItem_other.setParamValue(saveItem.get(func.getFuncId().toString() + "_other"));
							submitItemDB.insertSubmitItem(submitItem_other,false);
						}
						if (func.getOrgOption() != null && func.getOrgOption() == Func.ORG_OPTION) {
							sb.append(";").append(func.getOrgLevel()).append(":").append(saveItem.get(func.getFuncId().toString()));
						}
						if (func.getType() == Func.TYPE_PRODUCT_CODE && !TextUtils.isEmpty(saveItem.get("compare_content"))) {
							SharedPreferencesUtil.getInstance(this).setCompareContent("content_"+func.getTargetid(),saveItem.get("compare_content"));
						}
					}
				}
			}
			if(isLink){
//				submitDB.updateTargetType(Menu.TYPE_MODULE,submitId);
			}
			if (sb != null && sb.length() > 1) {
				orgString.add(targetId + sb.toString());
			}
		}
	}


	@Override
	public void onClickPage(View v) {
		super.onClickPage(v);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("storeId", storeId);
		outState.putInt("type", type);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		storeId = savedInstanceState.getInt("storeId");
		type = savedInstanceState.getInt("type");
	}

	private String setString(int stringId){
		return getResources().getString(stringId);
	}
}
