package com.yunhu.yhshxc.activity;

import gcg.org.debug.JLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.bo.Submit;
import com.yunhu.yhshxc.bo.SubmitItem;
import com.yunhu.yhshxc.database.FuncDB;
import com.yunhu.yhshxc.database.SubmitDB;
import com.yunhu.yhshxc.database.SubmitItemDB;
import com.yunhu.yhshxc.database.SubmitItemTempDB;
import com.yunhu.yhshxc.database.VisitFuncDB;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.listener.SqlBigDataListener;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.loopj.android.http.RequestParams;

/**
 * 大数据类型的控件
 * @author gcg_jishen
 *
 */
public class SqlBigDataActivity extends FragmentActivity implements SqlBigDataListener{
	private final String  TAG = "SqlBigDataActivity";
	private SqlBigDataFragment listFragment;
	private LinearLayout searchBtn;//搜索图标
	private EditText etSearchBar;//搜索内容输入框
	private Func func;
	private String sqlStandard;//sql标准
	private Integer planId;
	private Integer wayId;
	private Integer storeId;
	private Integer targetId;
	private Integer menuType;
	private Integer taskId;
	private Integer sqlStoreId;
	private Integer modType;
	private String funcId;
	private boolean isLink;//是否是超链接中的控件 true是 false不是
	private boolean isReplenish;//是否是查询条件 true是 false不是
	private String fuzzySearch;
	private Bundle bundle;
	private Dialog dialog;
	private int menuId;
	private String menuName;
	private int page;//当前查询是第几页
	private HashMap<String, String> selectedMap;//选中的值 key是did value是文本信息
	private List<Dictionary> dictionaryList;//sql查询到的值
	private boolean searchKey = true;//键盘的搜索按键是否可用 true表示可用，false表示不可用
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sql_big_data_activity);
		dialog = new MyProgressDialog(this,R.style.CustomProgressDialog,getResources().getString(R.string.loading));
		listFragment = (SqlBigDataFragment)getSupportFragmentManager().findFragmentById(R.id.listview);
		searchBtn = (LinearLayout)findViewById(R.id.iv_sql_big_data_search_btn);
		etSearchBar = (EditText)findViewById(R.id.et_sql_big_data_search);
		etSearchBar.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode==KeyEvent.KEYCODE_ENTER){
					if (searchKey) {
						searchKey = false;
						((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(SqlBigDataActivity.this
						.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
						fuzzySearch = etSearchBar.getText().toString().trim();
						getSearchRequirement(func);
					}
				}//修改回车键功能
				return false;
			}
		});
		
		etSearchBar.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					searchKey = true;
				}				
			}
		});
		selectedMap = new HashMap<String, String>();
		searchBtn.setOnClickListener(listener);
		bundle = getIntent().getBundleExtra("bundle");
		planId = bundle.getInt("planId");
		wayId = bundle.getInt("wayId");
		storeId = bundle.getInt("storeId");
		targetId = bundle.getInt("targetId");
		menuType = bundle.getInt("menuType");
		taskId = bundle.getInt("taskId");
		sqlStoreId = bundle.getInt("sqlStoreId");
		modType = bundle.getInt("modType");
		isLink = bundle.getBoolean("isLink");
		isReplenish = bundle.getBoolean("isReplenish");
		funcId = bundle.getString("funcId");
		menuId = bundle.getInt("menuId");
		menuName = bundle.getString("menuName");
		if (Menu.TYPE_VISIT == menuType) {
			func = new VisitFuncDB(this).findFuncListByFuncIdAndTargetId(funcId, targetId);
		}else{
			func = new FuncDB(this).findFuncListByFuncIdAndTargetId(funcId, targetId);
		}
		initSelectedData();
	}
	
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.iv_sql_big_data_search_btn:
				fuzzySearch = etSearchBar.getText().toString().trim();
				getSearchRequirement(func);
				break;

			default:
				break;
			}
			
		}
	};
	
	/**
	 * 获取sql查询条件
	 * @param func
	 */
	private void getSearchRequirement(final Func func){
		if (func == null) {
			ToastOrder.makeText(this, R.string.ERROR_DATA, ToastOrder.LENGTH_SHORT).show();
		}else{
			page = 0;
			String phoneNo = PublicUtils.receivePhoneNO(this);
			String url=UrlInfo.baseUrl(this)+"radioDpdwSqlParamInfo.do?defaultData="+func.getDefaultValue()+"&phoneno="+phoneNo;
			JLog.d(TAG, "取得分销标准 SQL参数 URL:"+url);
			GcgHttpClient.getInstance(this).get(url, null, new HttpResponseListener(){
				@Override
				public void onStart() {
					if (dialog!=null && !dialog.isShowing()) {
						dialog.show();
					}
				}
				@Override
				public void onFailure(Throwable error, String content) {
					ToastOrder.makeText(SqlBigDataActivity.this, R.string.retry_net_exception, ToastOrder.LENGTH_LONG).show();
				}
				
				@Override
				public void onFinish() {
					if (dialog!=null && dialog.isShowing()) {
						dialog.dismiss();
					}
				}
				@Override
				public void onSuccess(int statusCode, String content) {
					JLog.d(TAG, "sql:"+content);
					JSONObject jsonObject = null;
					String resultcode = null;
					try {
						jsonObject = new JSONObject(content);
						resultcode = jsonObject.getString(Constants.RESULT_CODE);
					} catch (JSONException e) {
						ToastOrder.makeText(SqlBigDataActivity.this, R.string.un_search_data, ToastOrder.LENGTH_LONG).show();
						JLog.d(TAG, "获取分销标准SQL："+e.getMessage());
					}
					if(resultcode!=null && resultcode.equals(Constants.RESULT_CODE_SUCCESS)){
						try {
							JSONObject obj=new JSONObject(content);
							sqlStandard=obj.getString("sqlparam");
							result();
						} catch (JSONException e) {
							ToastOrder.makeText(SqlBigDataActivity.this, R.string.un_search_data, ToastOrder.LENGTH_LONG).show();
							JLog.d(TAG, e.getMessage());
						}
					}else{
						ToastOrder.makeText(SqlBigDataActivity.this,R.string.un_search_data, ToastOrder.LENGTH_LONG).show();
					}
				
					
				}
			});
		}
	}
	
	/**
	 * 
	 * @param  sql查询规则
	 * @param  页数
	 * @param  模糊搜索条件
	 */
	public void result(){
		boolean isPassSql = true;//要求操作的项是否都操作 true表示都操作 false表示有选项没操作
		String sqlparam = "";
		Map<String, String> param=new HashMap<String, String>();
		if(!TextUtils.isEmpty(sqlStandard)){
			String[] str=sqlStandard.split(",");
			int submitId = new SubmitDB(this).selectSubmitId(planId, wayId, storeId,targetId,menuType);
			for (int i = 0; i < str.length; i++) {
				String sqlKey=str[i];
				if(sqlKey.equals("1")){//访店中的超链接要把店面ID传给服务器
					param.put(str[i], sqlStoreId==0 ? storeId+"":sqlStoreId+"");
				}else if(sqlKey.equals("2")){//传用户ID
					param.put(str[i], SharedPreferencesUtil.getInstance(SqlBigDataActivity.this).getUserId()+"");
				}else if(sqlKey.equals("3")){//传双向任务的任务ID
					param.put(str[i], taskId+"");
				}else if (sqlKey.equals("5")) {//传递查询条件
					param.put(str[i], fuzzySearch == null?"":fuzzySearch);
				}else{
					Integer id = Integer.parseInt(str[i].split("_")[1]);
					SubmitItem item=null;
					String replenishValue = null;
					if (isReplenish) {//控件是查询条件
						Bundle replenishBundle = bundle.getBundle("replenishBundle");
						if (replenishBundle!=null) {
							replenishValue = replenishBundle.getString(String.valueOf(id));
						}
					}else{
						if(isLink){//控件是在超链接模块中
							item=new SubmitItemTempDB(SqlBigDataActivity.this).findSubmitItemBySubmitIdAndFuncId(submitId,id);
						}else{
							item=new SubmitItemDB(SqlBigDataActivity.this).findSubmitItemBySubmitIdAndFuncId(submitId,id);
						}
					}
					if(item!=null && !TextUtils.isEmpty(item.getParamValue())){
						param.put(str[i], item.getParamValue());
					}else if (!TextUtils.isEmpty(replenishValue)) {
						param.put(str[i], replenishValue);
					}else{
						isPassSql = false;
						Func findFunc = null;
						if (Menu.TYPE_VISIT == menuType) {
							findFunc = new VisitFuncDB(this).findFuncListByFuncIdAndTargetId(String.valueOf(id), targetId);
						}else{
							findFunc = new FuncDB(this).findFuncListByFuncIdAndTargetId(String.valueOf(id), targetId);
						}
						if(findFunc != null){
							ToastOrder.makeText(SqlBigDataActivity.this,findFunc.getName()+PublicUtils.getResourceString(SqlBigDataActivity.this,R.string.un_search_data2)+"!", ToastOrder.LENGTH_LONG).show();
						}else{
							ToastOrder.makeText(SqlBigDataActivity.this, PublicUtils.getResourceString(SqlBigDataActivity.this,R.string.un_search_data3)+"！", ToastOrder.LENGTH_LONG).show();
						}
						break;
					}
				}
			}
			if(!param.isEmpty()){
				sqlparam = new JSONObject(param).toString();
			}
		
		}
		if(isPassSql){//如果必须输入的值已经输入就发起网络请求获取sql查询到的值
			final String phoneNo = PublicUtils.receivePhoneNO(SqlBigDataActivity.this);
			String sqlUrl = UrlInfo.baseUrl(SqlBigDataActivity.this)+"radioDpdwSqlDataInfo.do?ctrlType="+35+"&defaultData="+func.getDefaultValue()+"&phoneno="+phoneNo+"&tabId="+func.getMenuId();
			JLog.d(TAG, "取得分销标准 SQL数据URL:"+sqlUrl);
			RequestParams params = new RequestParams();
			params.put("sqlparam", sqlparam);
			params.put("page", String.valueOf(page));
			JLog.d(TAG, "sqlparam:"+sqlparam+"\npage:"+page);
			GcgHttpClient.getInstance(SqlBigDataActivity.this).post(sqlUrl, params, new HttpResponseListener(){
				@Override
				public void onStart() {
					if (dialog!=null && !dialog.isShowing()) {
						dialog.show();
					}
				}
				
				
				@Override
				public void onFailure(Throwable error, String content) {
					listFragment.addItemAndRefresh(dictionaryList,page==1?true:false);							
					ToastOrder.makeText(SqlBigDataActivity.this, R.string.retry_net_exception, ToastOrder.LENGTH_LONG).show();
					return;
				}
				
				@Override
				public void onFinish() {
					if (dialog!=null && dialog.isShowing()) {
						dialog.dismiss();
					}
				}

				@Override
				public void onSuccess(int statusCode, String content) {
					JLog.d(TAG, "getSql:"+content);
					try {
						JSONObject jsonObject = new JSONObject(content);
						String resultcode = jsonObject.getString(Constants.RESULT_CODE);
						if(resultcode!=null && resultcode.equals(Constants.RESULT_CODE_SUCCESS)){
							JSONObject obj=new JSONObject(content);
							String	defaultSql=obj.getString("sqldata");
							dictionaryList = parserDictionary(defaultSql);
							if (dictionaryList.isEmpty()) {
								ToastOrder.makeText(SqlBigDataActivity.this, R.string.un_search_data4, ToastOrder.LENGTH_LONG).show();
							}else{
								page++;
							}
							listFragment.addItemAndRefresh(dictionaryList,page==1?true:false);							
						}else{
							listFragment.addItemAndRefresh(dictionaryList,page==1?true:false);							
							ToastOrder.makeText(SqlBigDataActivity.this, R.string.search_no_data, ToastOrder.LENGTH_LONG).show();
						}
					} catch (Exception e) {
						listFragment.addItemAndRefresh(dictionaryList,page==1?true:false);							
						ToastOrder.makeText(SqlBigDataActivity.this, R.string.search_no_data, ToastOrder.LENGTH_LONG).show();
					}
					
				}
			});
		}
		
	}
	
	/**
	 * 解析查询到的数据
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	private List<Dictionary> parserDictionary(String json) throws JSONException{
		List<Dictionary> list = new ArrayList<Dictionary>();
		if (!TextUtils.isEmpty(json)) {
			JSONArray array = new JSONArray(json);
			if (array !=null && array.length()>0) {
				JSONObject dicObj = null;
				for (int i = 0; i < array.length(); i++) {
					Dictionary dic = new Dictionary();
					dicObj = (JSONObject) array.get(i);
					if (PublicUtils.isValid(dicObj, "key")) {
						dic.setDid(dicObj.getString("key"));
					}
					if (PublicUtils.isValid(dicObj, "value")) {
						dic.setCtrlCol(dicObj.getString("value"));
					}
					list.add(dic);
				}
			}
		}
		return list;
	}
	
	/**
	 * 选中值以后回调
	 */
	@Override
	public void selected(Dictionary dictionary,boolean isChoice) {
		if (isChoice) {//如果选择了值
			if (dictionary!=null) {
				selectedMap.put(dictionary.getDid(), dictionary.getCtrlCol());
				if (isReplenish) {//控件作为查询条件
					Intent intent = new Intent();
					intent.putExtra("SQL_BIG_DATA", dictionary.getDid()+"@"+dictionary.getCtrlCol());
					setResult(R.id.sql_big_data, intent);
					this.finish();
				}else{//存储数据库
					save(dictionary.getDid(), dictionary.getCtrlCol());
				}
			}
		}else{
			this.selectedMap.remove(dictionary.getDid());
			if (isReplenish) {//控件作为查询条件
				Intent intent = new Intent();
				intent.putExtra("SQL_BIG_DATA_CLEAR", true);
				setResult(R.id.sql_big_data, intent);
				this.finish();
			}else{
				save(null, null);//修改的时候如果值取消就传给服务器一个空值，否则服务器不处理
			}
		}
	}
	
	protected HashMap<String,String> getSelectedMap() {
		return selectedMap;
	}
	
	/**
	 * 存储数据库
	 * @param did 选中的id
	 * @param value 选中的文本信息 预览的时候要展示
	 */
	private void save(String did,String value){
		SubmitDB submitDB = new SubmitDB(this);
		SubmitItemDB submitItemDB = new SubmitItemDB(this);
		SubmitItemTempDB submitItemTempDB = new SubmitItemTempDB(this);
		SubmitItem submitItem = new SubmitItem();
		submitItem.setTargetId(targetId+"");
		int submitId = submitDB.selectSubmitIdNotCheckOut(planId, wayId, storeId,targetId, menuType,Submit.STATE_NO_SUBMIT);
		if (submitId != Constants.DEFAULTINT) {
			submitItem.setParamName(String.valueOf(func.getFuncId()));
			submitItem.setParamValue(did);
			submitItem.setSubmitId(submitId);
			submitItem.setType(func.getType());
			submitItem.setOrderId(func.getId());
			submitItem.setNote(value);
			submitItem.setIsCacheFun(func.getIsCacheFun());
			boolean isHas=false;
			if(isLink){//超链接的时候操作临时表中的数据
				isHas = submitItemTempDB.findIsHaveParamName(submitItem.getParamName(), submitId);
			}else{
				isHas = submitItemDB.findIsHaveParamName(submitItem.getParamName(), submitId);
			}
			if (isHas) {// true表示已经操作过，此时更新数据库
				if (TextUtils.isEmpty(did)) {// 如果值是空就修改为空
					submitItem.setParamValue("");
				}
				if(isLink){//超链接
					submitItemTempDB.updateSubmitItem(submitItem);
				}else{
					submitItemDB.updateSubmitItem(submitItem,true);
				}
			} else {
				if (!TextUtils.isEmpty(did)) {// 没有该项，并且值不为空就插入
					if(isLink){
						submitItemTempDB.insertSubmitItem(submitItem);
					}else{
						submitItemDB.insertSubmitItem(submitItem,true);
					}
				}
			}

		} else {
			if(!TextUtils.isEmpty(did)){
				Submit submit = new Submit();
				submit.setPlanId(planId);
				submit.setState(Submit.STATE_NO_SUBMIT);
				submit.setStoreId(storeId);
				submit.setWayId(wayId);
				submit.setTargetid(targetId);
				submit.setTargetType(menuType);
				if (modType!=null&&modType!=0) {
					submit.setModType(modType);
				}
				submit.setMenuId(menuId);
				submit.setMenuType(menuType);
				submit.setMenuName(menuName);
				submitDB.insertSubmit(submit);
				int currentsubmitId = submitDB.selectSubmitIdNotCheckOut(planId, wayId,storeId, targetId, menuType,Submit.STATE_NO_SUBMIT);
				submitItem.setParamName(String.valueOf(func.getFuncId()));
				submitItem.setType(func.getType());
				submitItem.setParamValue(did);
				submitItem.setSubmitId(currentsubmitId);
				submitItem.setOrderId(func.getId());
				submitItem.setNote(value);
				submitItem.setIsCacheFun(func.getIsCacheFun());
				if(isLink){
					submitItemTempDB.insertSubmitItem(submitItem);
				}else{
					submitItemDB.insertSubmitItem(submitItem,true);
				}
			}
			
		}
		this.finish();
	}

	/**
	 * 初始化已经选中的值
	 */
	private void initSelectedData(){
		if (isReplenish) {//控件是查询条件
			Bundle replenishBundle = bundle.getBundle("replenishBundle");
			if (replenishBundle!=null) {
				String did = replenishBundle.getString(String.valueOf(func.getFuncId()));
				String value = replenishBundle.getString(func.getFuncId()+"_value");
				if (!TextUtils.isEmpty(did) && !TextUtils.isEmpty(value)) {
					selectedMap.put(did, value);
				}
			}
		}else{//控件不是查询条件就查数据库
			int submitId = new SubmitDB(this).selectSubmitIdNotCheckOut(planId, wayId, storeId,targetId, menuType,Submit.STATE_NO_SUBMIT);
			if (submitId != Constants.DEFAULTINT) {
				SubmitItem item = null;
				if(isLink){//超链接 查询临时表
					item=new SubmitItemTempDB(this).findSubmitItem(null, Submit.STATE_NO_SUBMIT, planId, wayId, storeId, targetId, menuType, String.valueOf(func.getFuncId()));
				}else{
					item=new SubmitItemDB(this).findSubmitItem(null, Submit.STATE_NO_SUBMIT, planId,wayId, storeId, targetId, menuType, String.valueOf(func.getFuncId()));
				}
				if (item!=null && !TextUtils.isEmpty(item.getParamValue())&&!TextUtils.isEmpty(item.getNote())) {
					selectedMap.put(item.getParamValue(), item.getNote());
				}
			}
		}
		if (selectedMap !=null && selectedMap.size() > 0 ) {
			Dictionary dic = null;
			List<Dictionary> list = new ArrayList<Dictionary>();
			for (Map.Entry<String, String> entry : selectedMap.entrySet()) {
				dic = new Dictionary();
				dic.setDid(entry.getKey());
				dic.setCtrlCol(entry.getValue());
				list.add(dic);
			}
			listFragment.addItemAndRefresh(list,false);
		}
	}
}
