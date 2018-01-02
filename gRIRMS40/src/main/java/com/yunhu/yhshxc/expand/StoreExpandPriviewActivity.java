package com.yunhu.yhshxc.expand;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.Org;
import com.yunhu.yhshxc.bo.OrgStore;
import com.yunhu.yhshxc.bo.OrgUser;
import com.yunhu.yhshxc.bo.SubmitItem;
import com.yunhu.yhshxc.database.DictDB;
import com.yunhu.yhshxc.database.FuncDB;
import com.yunhu.yhshxc.database.OrgDB;
import com.yunhu.yhshxc.database.OrgStoreDB;
import com.yunhu.yhshxc.database.OrgUserDB;
import com.yunhu.yhshxc.database.SubmitItemDB;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.nearbyVisit.view.NearbyVisitStoreDetailItem;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil2;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.loopj.android.http.RequestParams;

public class StoreExpandPriviewActivity  extends AbsBaseActivity{
	
	private LinearLayout ll_store_expand_add_data,ll_store_expand_submit_data;
	private int submitId;
	private String orgId,orgName;
	private RequestParams paramsMap = new RequestParams();

	private JSONObject storeExpandJson = new JSONObject();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_expand_priview);
		initIntentData();
		initWidget();
		addData();
	}
	
	private void initIntentData(){
		Intent intent = getIntent();
		submitId = intent.getIntExtra("submitId", 0);
		orgId = intent.getStringExtra("orgId");
		orgName = intent.getStringExtra("orgName");
	}
	
	/**
	 * 添加数据页面
	 */
	private void addData(){
		List<SubmitItem> submitList = new SubmitItemDB(this).findSubmitItemBySubmitId(submitId);
		NearbyVisitStoreDetailItem orgItemView = new NearbyVisitStoreDetailItem(this);
		orgItemView.setData(getResources().getString(R.string.has_selected_institution), orgName);
		try {
			storeExpandJson.put("orgId", orgId);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		ll_store_expand_add_data.addView(orgItemView.getView());
		
		for (int i = 0; i <submitList.size(); i++) {
			SubmitItem item = submitList.get(i);
			if (item.getType() != Func.TYPE_HIDDEN) {//隐藏域不显示
				String name = item.getParamName();
				String value = item.getParamValue();
				
				String showValue = value;
				NearbyVisitStoreDetailItem itemView = new NearbyVisitStoreDetailItem(this);
				if (String.valueOf(StoreModuleExpandUtil.TYPE_STOREMODULE_NAME).equals(name)) {
					itemView.setData(getResources().getString(R.string.StoreExpandActivity_message01), value);
					try {
						storeExpandJson.put("name", value);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}else if(String.valueOf(StoreModuleExpandUtil.TYPE_STOREMODULE_NUM).equals(name)){
					itemView.setData(getResources().getString(R.string.StoreExpandActivity_message02), value);
					try {
						storeExpandJson.put("no", value);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}else if(String.valueOf(StoreModuleExpandUtil.TYPE_STOREMODULE_LOCATION).equals(name)){//定位不显示
//					itemView.setData("定位", value);
					continue;
				}else if(String.valueOf(StoreModuleExpandUtil.TYPE_STOREMODULE_ADRESS).equals(name)){
					itemView.setData(getResources().getString(R.string.StoreExpandActivity_message03), value);
					try {
						storeExpandJson.put("address", value);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}else if(String.valueOf(StoreModuleExpandUtil.TYPE_STOREMODULE_LON).equals(name)){
					try {
						storeExpandJson.put("lon", value);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					continue;
				}else if(String.valueOf(StoreModuleExpandUtil.TYPE_STOREMODULE_LAT).equals(name)){
					try {
						storeExpandJson.put("lat", value);
						
						storeExpandJson.put("acc", "1000");
						storeExpandJson.put("action", "getaddr");
						storeExpandJson.put("status", "302");
						storeExpandJson.put("version", "3");

					} catch (JSONException e) {
						e.printStackTrace();
					}
					continue;
				}else{
					paramsMap.put(name, value);
					if (!PublicUtils.isNumeric(name)) {
						continue;
					}
					Func func = new FuncDB(this).findFuncByFuncId(Integer.parseInt(name));
					if (func!=null) {
						if (func.getType() == Func.TYPE_SELECTCOMP || func.getType() == Func.TYPE_SINGLE_CHOICE_FUZZY_QUERY_COMP) { // 下拉框

							if (func.getOrgOption() != null) {//组织结构 机构店面 机构用户
								showValue = orgOptionValue(func, value);
							} else {
								showValue = selectValue(func,value);//普通下拉框
							}

						} else if (func.getType() == Func.TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY_COMP || func.getType() == Func.TYPE_SELECT_OTHER) {
							//其他类型的下拉框
							if (value.equals("-1")) {//选了其他
								for (SubmitItem otherItem : submitList) {//遍历，查找其他的值
									if (otherItem.getParamName().equals(func.getFuncId() + "_other")) {
										showValue = otherItem.getParamValue();
										break;
									}
								}

							} else {//没选其他
								if (func.getOrgOption() != null) {//如果是机构
									showValue = orgOptionValue(func, value);
								} else {
									showValue = selectValue(func,value);//不是机构的话就当普通下拉框处理
								}
							}

						} else if ((func.getType() == Func.TYPE_MULTI_CHOICE_SPINNER_COMP || func.getType() == Func.TYPE_MULTI_CHOICE_FUZZY_QUERY_COMP) && !TextUtils.isEmpty(value)) {// 多选下拉框
							// 多选下拉框
							if (func.getOrgOption()!=null) {//机构
								showValue = orgOptionValue(func, value);
							} else {
								//查找多选下拉框的值
								showValue= new DictDB(this).findDictMultiChoiceValueStr(value, func.getDictDataId(),func.getDictTable());
							}
						} 
						itemView.setData(func.getName(), showValue);
					}
				}
				ll_store_expand_add_data.addView(itemView.getView());
			}else{
				String name = item.getParamName();
				String value = item.getParamValue();
				paramsMap.put(name, value);
					
			}
		}
		}
		
		/**
		 * 设置机构店面
		 * 
		 * @param func 下拉框func
		 * @param showDataLayout 添加预览的每一项view
		 * @param previewDataComp 该项预览view
		 * @param currentValue 当前值
		 */
		private String orgOptionValue(Func func, String currentValue) {
			if (func.getOrgOption() != null && func.getOrgOption() == Func.ORG_STORE) {
				return getOrgStore(currentValue);
			} else if (func.getOrgOption() != null && func.getOrgOption() == Func.ORG_USER) {
				return getOrgUser(currentValue);
			} else if (func.getOrgOption() != null && func.getOrgOption() == Func.ORG_OPTION) {
				return getOrg(currentValue);
			}else{
				return currentValue;
			}
		}

		/**
		 * 下拉框类型
		 * @param func 下拉框func
		 * @param previewDataComp 当前下拉框view
		 * @param currentValue当前下拉框的值
		 */
		private String selectValue(Func func, String currentValue){
			String tableName = func.getDictTable(); // 查询的表
			String dataId = func.getDictDataId(); // 查询表中的列
			Dictionary dic = new DictDB(this).findDictListByTable(tableName, dataId, currentValue);
			if (dic == null) {//没有找到该下拉框对应的字典表
				return currentValue;
			} else {
				return dic.getCtrlCol();
			}
		}
		
		
	
		/**
		 * 返回机构数据
		 * @param value 机构的did
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
		 * @param value 店面的did
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
		 * @param value 用户的did
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

		
	
	private void initWidget(){
		ll_store_expand_add_data = (LinearLayout)findViewById(R.id.ll_store_expand_add_data);
		ll_store_expand_submit_data = (LinearLayout)findViewById(R.id.ll_store_expand_submit_data);
		ll_store_expand_submit_data.setOnClickListener(listener);
	}
	
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ll_store_expand_submit_data:
				submit();
				break;

			default:
				break;
			}
		}
	};
	
	
	private Dialog dialog = null;
	private void submit(){
		try {
			if (TextUtils.isEmpty(storeExpandJson.getString("name"))) {
				ToastOrder.makeText(StoreExpandPriviewActivity.this, getResources().getString(R.string.StoreExpandActivity_message05), ToastOrder.LENGTH_SHORT).show();
			    return;
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			ToastOrder.makeText(StoreExpandPriviewActivity.this, getResources().getString(R.string.StoreExpandActivity_message05), ToastOrder.LENGTH_SHORT).show();
			StoreExpandPriviewActivity.this.finish();
			return;
		}
		String url = UrlInfo.doStoreExpInfo(this);
		dialog =  new MyProgressDialog(this,R.style.CustomProgressDialog,getResources().getString(R.string.StoreExpandActivity_message06));
		paramsMap.put("dataExp", storeExpandJson.toString());
		paramsMap.put("tableId", SharedPreferencesUtil2.getInstance(this).getStoreInfoId());
		paramsMap.put("patchId",System.currentTimeMillis());
//		JLog.d("alin", "param  =  "+paramsMap.toString());
		GcgHttpClient.getInstance(this).post(url, paramsMap, new HttpResponseListener() {
			
			@Override
			public void onSuccess(int statusCode, String content) {
				try {
					JSONObject obj = new JSONObject(content);
					String resultCode = obj.getString("resultcode");
					if ("0000".equals(resultCode)) {//提交成功
						ToastOrder.makeText(StoreExpandPriviewActivity.this, getResources().getString(R.string.StoreExpandActivity_message07), ToastOrder.LENGTH_SHORT).show();
						setResult(R.id.submit_succeeded);
						StoreExpandPriviewActivity.this.finish();
					}else if("0005".equals(resultCode)){//提交数据重复
						ToastOrder.makeText(StoreExpandPriviewActivity.this, getResources().getString(R.string.StoreExpandActivity_message08), ToastOrder.LENGTH_SHORT).show();
					}else{
						ToastOrder.makeText(StoreExpandPriviewActivity.this, getResources().getString(R.string.StoreExpandActivity_message09), ToastOrder.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					ToastOrder.makeText(StoreExpandPriviewActivity.this, getResources().getString(R.string.StoreExpandActivity_message010), ToastOrder.LENGTH_SHORT).show();
				}
			}
			
			@Override
			public void onStart() {
				dialog.show();
			}
			
			@Override
			public void onFinish() {
				dialog.dismiss();
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				ToastOrder.makeText(StoreExpandPriviewActivity.this, getResources().getString(R.string.StoreExpandActivity_message011), ToastOrder.LENGTH_SHORT).show();
			}
		});
	}
	
}
