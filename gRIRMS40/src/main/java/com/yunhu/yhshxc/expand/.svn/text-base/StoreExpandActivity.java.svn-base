package com.yunhu.yhshxc.expand;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.Org;
import com.yunhu.yhshxc.bo.Submit;
import com.yunhu.yhshxc.bo.SubmitItem;
import com.yunhu.yhshxc.database.FuncDB;
import com.yunhu.yhshxc.database.OrgDB;
import com.yunhu.yhshxc.database.SubmitDB;
import com.yunhu.yhshxc.database.SubmitItemDB;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.exception.DataComputeException;
import com.yunhu.yhshxc.func.AbsFuncActivity;
import com.yunhu.yhshxc.func.FuncTree;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.nearbyVisit.view.NearbyVisitStoreDetailItem;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil2;
import com.yunhu.yhshxc.utility.SharedPreferencesUtilForNearby;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.DropDown;
import com.yunhu.yhshxc.widget.DropDown.OnFuzzyQueryListener;
import com.yunhu.yhshxc.widget.DropDown.OnResultListener;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StoreExpandActivity extends AbsFuncActivity implements OnFuzzyQueryListener{

	private DropDown sp_store_expand;
	private FrameLayout ll_title;
	private TextView tv_titleName;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		storeModuleExpandUtil = new StoreModuleExpandUtil(this);
		super.onCreate(savedInstanceState);
		super.expandShow();
		ll_title=(FrameLayout) findViewById(R.id.ll_title);
		ll_title.setVisibility(View.VISIBLE);
		tv_titleName = (TextView) findViewById(R.id.tv_titleName);

		if(menuName!=null){
			tv_titleName.setText(menuName);
		}else{
			tv_titleName.setText(getResources().getString(R.string.is_store_add_mod));
		}


		sp_store_expand = (DropDown)findViewById(R.id.sp_store_expand);
		sp_store_expand.setOnFuzzyQueryListener(this);
		sp_store_expand.setOnResultListener(resultListener);
		initOrgData(null);
	}

	@Override
	protected void intoLink(Bundle linkBundle) {
	
	}
	
	private String orgId;
	private OnResultListener resultListener = new OnResultListener() {
		
		@Override
		public void onResult(List<Dictionary> result) {
			if (result!=null && result.size()>0) {
				Dictionary dic = result.get(0);
				orgId = dic.getDid();
				SharedPreferencesUtilForNearby.getInstance(StoreExpandActivity.this).saveStoreOrgId(orgId);
				SharedPreferencesUtilForNearby.getInstance(StoreExpandActivity.this).saveStoreMenuId(menuId);
				SharedPreferencesUtilForNearby.getInstance(StoreExpandActivity.this).saveStoreMenuType(menuType);
				SharedPreferencesUtilForNearby.getInstance(StoreExpandActivity.this).saveStoreMenuName(menuName);
//				orgName = dic.getCtrlCol();
			}
		}
	};
     /**
      * 获取新店上报activity的显示控件
      */
	@Override
	public List<Func> getFuncList() {
		List<Func> list = new ArrayList<Func>();
		list.addAll(storeModuleExpandUtil.getFuncs(targetId));
		FuncDB funcDB=new FuncDB(this);
		List<Func> funcList = funcDB.findFuncListByTargetid(SharedPreferencesUtil2.getInstance(this).getStoreInfoId(),true);//查询可修改的控件
		list.addAll(funcList);
		return list;
	}

	@Override
	public List<Func> getButtonFuncList() {
		return null;
	}

	@Override
	public Func getFuncByFuncId(int funcId) {
		return null;
	}

	@Override
	public Integer[] findFuncListByInputOrder() {
		return null;
	}

	@Override
	public ArrayList<Func> getOrderFuncList(Integer funcId, int inputOrder) {
		return null;
	}

	@Override
	public void showPreview() {
		if (!TextUtils.isEmpty(orgId)) {
			SubmitDB submitDB=new SubmitDB(this);
			int submitId = submitDB.selectSubmitId(planId, wayId, storeId,targetId, menuType);
			Submit submit=submitDB.findSubmitById(submitId);
			
			if(submit!=null &&modType==Constants.MODULE_TYPE_ISSUED && TextUtils.isEmpty(submit.getSendUserId())){
				ToastOrder.makeText(this, getResources().getString(R.string.issued_user),ToastOrder.LENGTH_LONG).show();
			}else{
				if (submit==null) {//等于空的时候要插入一条submit
					submit = new Submit();
					submit.setPlanId(planId);
					submit.setState(Submit.STATE_NO_SUBMIT);
					submit.setStoreId(storeId);
					submit.setWayId(wayId);
					submit.setTargetid(targetId);
					submit.setTargetType(menuType);
					if(modType!=0){
						submit.setModType(modType);
					}
					submit.setMenuId(menuId);
					submit.setMenuType(menuType);
					submit.setMenuName(menuName);
					submitDB.insertSubmit(submit);
					submitId = submitDB.selectSubmitIdNotCheckOut(planId, wayId,
							storeId, targetId, menuType,Submit.STATE_NO_SUBMIT);
				}
//在此进行判断用户是否输入店名,否则不应该跳转到预览
//				if (TextUtils.isEmpty(submit.getStoreName())) {
//					ToastOrder.makeText(this, "请输入店面名称", ToastOrder.LENGTH_SHORT).show();
//					return;
//				}
				intentToDetail(submitId);
				
			}
		}else{
			ToastOrder.makeText(this, getResources().getString(R.string.please_select_institution), ToastOrder.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * 跳转到预览页面
	 * @param submitId 提交表单的表单ID
 	 */
	private JSONObject storeExpandJson = new JSONObject();//提交的数据json格式
	private RequestParams paramsMap = new RequestParams();//提交的参数
	private void intentToDetail(int submitId){
//		Intent intent = new Intent(this, StoreExpandPriviewActivity.class);
//		intent.putExtra("submitId", submitId);
//		intent.putExtra("targetId", targetId);//数据ID
//		intent.putExtra("menuType", menuType);
//		intent.putExtra("orgId", orgId);
//		intent.putExtra("orgName", orgName);
//		try {
//			controlHidden(submitId);//存储隐藏域
//			startActivityForResult(intent,R.id.submit_succeeded);
//		} catch (NumberFormatException e) {//下拉框其他计算格式出错异常
//			ToastOrder.makeText(this,"格式错误", ToastOrder.LENGTH_SHORT).show();
//			e.printStackTrace();
//		} catch (DataComputeException e) {
//			ToastOrder.makeText(this,e.getMessage(), ToastOrder.LENGTH_SHORT).show();
//			JLog.e(e);
//		}
		//改为直接提交数据
		

		List<SubmitItem> submitList = new SubmitItemDB(this).findSubmitItemBySubmitId(submitId);
		try {
			storeExpandJson.put("orgId", orgId);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
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

				}
	
			}else{
				String name = item.getParamName();
				String value = item.getParamValue();
				paramsMap.put(name, value);
					
			}
		}
		

		try {
			if (TextUtils.isEmpty(storeExpandJson.getString("name"))) {
				ToastOrder.makeText(StoreExpandActivity.this, getResources().getString(R.string.StoreExpandActivity_message05), ToastOrder.LENGTH_SHORT).show();
			    return;
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			ToastOrder.makeText(StoreExpandActivity.this, getResources().getString(R.string.StoreExpandActivity_message05), ToastOrder.LENGTH_SHORT).show();
		
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
						ToastOrder.makeText(StoreExpandActivity.this, getResources().getString(R.string.StoreExpandActivity_message07), ToastOrder.LENGTH_SHORT).show();
						setResult(R.id.submit_succeeded);
						SharedPreferencesUtil2.getInstance(StoreExpandActivity.this).saveIsAnomaly(false);
						SharedPreferencesUtil2.getInstance(StoreExpandActivity.this).saveMenuId(-1);
						cleanAllNoSubmit();
						SharedPreferencesUtilForNearby.getInstance(StoreExpandActivity.this).saveStoreInfoClear();
						StoreExpandActivity.this.finish();
					}else if("0005".equals(resultCode)){//提交数据重复
						ToastOrder.makeText(StoreExpandActivity.this, getResources().getString(R.string.StoreExpandActivity_message08), ToastOrder.LENGTH_SHORT).show();
					}else{
						ToastOrder.makeText(StoreExpandActivity.this, getResources().getString(R.string.StoreExpandActivity_message09), ToastOrder.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					ToastOrder.makeText(StoreExpandActivity.this, getResources().getString(R.string.StoreExpandActivity_message010), ToastOrder.LENGTH_SHORT).show();
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
				ToastOrder.makeText(StoreExpandActivity.this, getResources().getString(R.string.StoreExpandActivity_message011), ToastOrder.LENGTH_SHORT).show();
			}
		});
	
	}
	
	/**
	 * 控制隐藏域
	 * @throws DataComputeException  //计算错误
	 * @throws NumberFormatException  
	 */
	private void controlHidden(int submitId) throws NumberFormatException, DataComputeException{
		// 存储隐藏类型的
		List<Func> hiddenList=null;
		hiddenList = new FuncDB(this).findFuncListByType(targetId,Func.TYPE_HIDDEN,null);
		List<Func> hiddenSql=new ArrayList<Func>();
		if (hiddenList != null) {
			for (int i = 0; i < hiddenList.size(); i++) {
				Func func = hiddenList.get(i);
				if(func.getDefaultType()!=null && func.getDefaultType()==Func.DEFAULT_TYPE_COMPUTER){//计算类型的隐藏域先不存储
					hiddenSql.add(func);
				}else{
					saveHideFunc(func,submitId);
				}
			}
		}
		//等其他隐藏域类型都存储完以后再存储计算类型的隐藏域
		if(!hiddenSql.isEmpty()){
			for (int i = 0; i < hiddenSql.size(); i++) {//存储计算隐藏域
				Func func = hiddenSql.get(i);
				saveHideFunc(func,submitId);
			}
		}
		
		/**
		 * 存储文本标签的计算值
		 */
		SubmitItemDB sItemDb = new SubmitItemDB(this);
		List<Func> list=getFuncList();
		for (int i = 0; i < list.size(); i++) {
			Func textFunc=list.get(i);
			if(textFunc.getType()==Func.TYPE_TEXTCOMP){//文本标签类型
				SubmitItem item=sItemDb.findSubmitItemBySubmitIdAndFuncId(submitId, textFunc.getFuncId());
				if(item!=null){
					String value=new FuncTree(this,textFunc,submitId).result;
					item.setParamValue(value);
					sItemDb.updateSubmitItem(item,true);
				}
			}
		}
		
	}	
	

	@Override
	protected void onClickBackBtn() {
		this.finish();
	}
	
	private void initOrgData(String fuuzy){
		List<Org> orgList = new OrgDB(this).storeExpandOrg(this,fuuzy);
		List<Dictionary> srcList = new ArrayList<Dictionary>();
		for (int i = 0; i < orgList.size(); i++) {
			Org org  = orgList.get(i);
			Dictionary dic = new Dictionary();
			dic.setDid(String.valueOf(org.getOrgId()));
			dic.setCtrlCol(org.getOrgName());
			srcList.add(dic);
		}
		sp_store_expand.setSrcDictList(srcList);
		orgId = SharedPreferencesUtilForNearby.getInstance(StoreExpandActivity.this).getStoreOrgId();
		if(!TextUtils.isEmpty(orgId)){
			for (int i = 0; i < orgList.size(); i++) {
				Org org = orgList.get(i);
				if(org.getOrgId()==Integer.parseInt(orgId)){
					Dictionary dic = new Dictionary();
					dic.setDid(String.valueOf(org.getOrgId()));
					dic.setCtrlCol(org.getOrgName());
					sp_store_expand.setSelected(dic);
					break;
				}
			}
		}
	}

	@Override
	public void onTextChanged(CharSequence s) {
		initOrgData(s.toString());
	}


	
	
}
