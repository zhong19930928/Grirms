package com.yunhu.yhshxc.visit;

import gcg.org.debug.JLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.Org;
import com.yunhu.yhshxc.bo.OrgStore;
import com.yunhu.yhshxc.bo.OrgUser;
import com.yunhu.yhshxc.bo.ReplenishSearchResult;
import com.yunhu.yhshxc.bo.Submit;
import com.yunhu.yhshxc.bo.SubmitItem;
import com.yunhu.yhshxc.bo.VisitStore;
import com.yunhu.yhshxc.comp.PreviewTable;
import com.yunhu.yhshxc.database.DictDB;
import com.yunhu.yhshxc.database.FuncDB;
import com.yunhu.yhshxc.database.OrgDB;
import com.yunhu.yhshxc.database.OrgStoreDB;
import com.yunhu.yhshxc.database.OrgUserDB;
import com.yunhu.yhshxc.database.SubmitDB;
import com.yunhu.yhshxc.database.SubmitItemDB;
import com.yunhu.yhshxc.database.VisitStoreDB;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.parser.ReplenishParse;
import com.yunhu.yhshxc.parser.TableParse;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil2;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.loopj.android.http.RequestParams;

/**
 * @author 王建雨
 *	店面信息查看
 *	店面信息修改
 */
public class InfoDetailActivity extends AbsBaseActivity{
	
	protected static final int PARSE_OK = 98;//解析通过
	protected static final int PARSE_FAIL = 97;//解析异常不通过
	
	/**
	 * 添加查看项view
	 */
	private LinearLayout showDataLayout;
	/**
	 * 加载dialog
	 */
	private Dialog loadDialog;
	/**
	 * 控件名称
	 */
	private String funcName;
	/**
	 * 菜单type
	 */
	private int targetType = 3;
	/**
	 * 店面ID
	 */
	private int storeId;
	
	// 店面信息的TargerId
	private int infoId;
	
	/**
	 * 店面信息类型
	 */
	private int storeType;
	/**
	 * 所有的控件的集合
	 */
	private List<Func> funcList;
	
	// 店面数据
	private Map<String, String> infoData;
	
	//请求网络的参数
	private HashMap<String, String> params;
	/**
	 * 当前店
	 */
	private VisitStore store;
	private int menuId;
	private String menuName;
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case PARSE_OK:
				laodUI();
				// 关闭加载对话框
				dismissLoadDialog();
				break;
			case PARSE_FAIL:
				// 关闭加载对话框
				dismissLoadDialog();
				ToastOrder.makeText(InfoDetailActivity.this, R.string.zhifu_no_search_data, ToastOrder.LENGTH_LONG).show();
				JLog.d(TAG, "Json 解析异常");
				break;
			default:
				// 关闭加载对话框
				dismissLoadDialog();
				ToastOrder.makeText(InfoDetailActivity.this, R.string.visit_store14, ToastOrder.LENGTH_LONG).show();
				break;
			}
		}

	
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_detail);
		initBase();
		init();
		postSearch();
		TextView title=(TextView)findViewById(R.id.tv_title);
		title.setText(titleName());
	}
	
	/**
	 * 返回title名称
	 * @return
	 */
	public String titleName(){
		return TextUtils.isEmpty(funcName)?PublicUtils.getResourceString(this,R.string.store_info):funcName;
	}

	/**
	 * 获取传过来的值 并根据storeType判断是店面信息修改还是店面信息查看
	 * 1：是店面信息查看
	 * 2：是店面信息修改
	 * 显示初始化dialog
	 */
	private void init() {
		storeType = setStoreType();
		if(storeType == Func.IS_STORE_UPDATE){//店面信息修改
			LinearLayout  update_visit=(LinearLayout)findViewById(R.id.ll_change_info_detail_data);
			update_visit.setVisibility(View.VISIBLE);//设置修改按钮可见
			update_visit.setOnClickListener(this);
			final ImageView iv_update=(ImageView)findViewById(R.id.visit_view_update_iv);
			final TextView tv_update=(TextView)findViewById(R.id.visit_view_update_tv);
			
			update_visit.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if(event.getAction()==MotionEvent.ACTION_UP){
						iv_update.setBackgroundResource(R.drawable.visit_update1);
						tv_update.setTextColor(InfoDetailActivity.this.getResources().getColor(R.color.visit_view_update_text));
					}else{
						iv_update.setBackgroundResource(R.drawable.visit_update2);
						tv_update.setTextColor(InfoDetailActivity.this.getResources().getColor(R.color.visit_view_update_text_click));
					}
					return false;
				}
			});
			
		}else if (storeType == Func.IS_STORE_VIEW){//店面信息查看
			findViewById(R.id.ll_change_info_detail_data).setVisibility(View.GONE);//设置修改按钮不可见
		}else{
			ToastOrder.makeText(InfoDetailActivity.this, R.string.visit_store13, ToastOrder.LENGTH_LONG).show();
			this.finish();
		}
		infoId = SharedPreferencesUtil2.getInstance(InfoDetailActivity.this).getStoreInfoId();//获取数据ID
		
		showDataLayout = (LinearLayout) findViewById(R.id.ll_info_detail_add_data);
		
		Bundle bundle = getIntent().getBundleExtra("bundle");
		int wayId = bundle.getInt("wayId", 0);
		int planId = bundle.getInt("planId", 0);
		storeId = bundle.getInt("storeId");
		menuId = bundle.getInt("menuId");
		menuName = bundle.getString("menuName");
		int targetId = bundle.getInt("targetId");
		store = visitStore(wayId, targetId, storeId, planId);
		
		funcName = getIntent().getStringExtra("funcName");
		
		funcList = new FuncDB(InfoDetailActivity.this).findFuncListContainHiddenByTargetid(infoId);
//		loadDialog = initDialog("正在加载数据,请稍候...");
		loadDialog = new MyProgressDialog(this,R.style.CustomProgressDialog,getResources().getString(R.string.initTable));;
		loadDialog.show();
		
	}
	
	/**
	 * 设置storeType
	 */
	public int setStoreType(){
		storeType = getIntent().getIntExtra("storeType", 0);
		return storeType;
	};
	
	/**
	 * 设置店面
	 * @return
	 */
	public VisitStore visitStore(int wayId,int targetId,int storeId,int planId){
		VisitStore store = new VisitStoreDB(InfoDetailActivity.this).findVisitStore(wayId, targetId, storeId, planId);
		return store;
	}
	
	/**
	 * 返回店面名称
	 * @return
	 */
	public String storeName(){
		if (store!=null) {
			return store.getName();
		}else{
			return "";
		}
	}
	/**
	 * 初始化UI
	 */
	protected void laodUI() {
		InfoDetailItem showDataViewName = new InfoDetailItem(this,null);
		//设置店名
		showDataViewName.setName(PublicUtils.getResourceString(this,R.string.store_name));
		showDataViewName.setValue(storeName());
		showDataLayout.addView(showDataViewName.getView());
		for(Func func : funcList){//添加所有控件的值
			String value = getShowValue(func);
			InfoDetailItem showDataView;
			if(!TextUtils.isEmpty(value)){
				if(func.getType() == Func.TYPE_TABLECOMP){ // 表格类型
					showDataView = new InfoDetailItem(this,func);
					List<Func> funcTableList =  new FuncDB(InfoDetailActivity.this).findFuncListByTargetid(func.getTableId(),false); // 查找table中的所有组件
					String name = func.getName(); // 组件名称
					showDataView.setName(name);
					List<HashMap<String,String>> tableList = getTableList(value); // 获取表格的列数
					List<String> contentList = getTableContent(tableList,funcTableList);// 获取表格中的内容
					View tableView = new PreviewTable(InfoDetailActivity.this, tableList.size(),contentList, funcTableList).getObject(); // 获取表格视图View
					showDataLayout.addView(showDataView.getView());
					showDataLayout.addView(tableView);
				}else{//非表格情况
					showDataView = new InfoDetailItem(InfoDetailActivity.this,func);
					showDataView.setName(func.getName());
					showDataView.setValue(value);
					showDataLayout.addView(showDataView.getView());
				}
			}
		}
		
	}

	/**
	 *每个view的实例 
	 *
	 */
	private class InfoDetailItem{
		private LinearLayout ll_info_detail_item;
		private Func func;
		public InfoDetailItem(Context context,Func func) {
			super();
			this.func=func;
			ll_info_detail_item = (LinearLayout) View.inflate(context, R.layout.info_detail_item, null);
		}
		public void setName(String name){//控件名称
			((TextView)ll_info_detail_item.findViewById(R.id.tv_info_detail_item_name)).setText(name);
		}
		public void setValue(String value){//控件的值
			if(func!=null && func.getCheckType()!=null && (func.getCheckType()==Func.CHECK_TYPE_MOBILE_TELEPHONE || func.getCheckType()==Func.CHECK_TYPE_FIXED_TELEPHONE)){
				((TextView)ll_info_detail_item.findViewById(R.id.tv_info_detail_item_value)).setAutoLinkMask(Linkify.PHONE_NUMBERS);//自动转手机号码点击它可进入系统拨号界面
			}
			((TextView)ll_info_detail_item.findViewById(R.id.tv_info_detail_item_value)).setText(value);
		}
		public View getView(){//返回view
			return ll_info_detail_item;
		}
	}
	
	/**
	 * 普通/其他/多选下拉框的值
	 * @param func 当前控件
	 * @return 当前控件的值
	 */
	private String getShowValue(Func func) {
		String value= infoData.get(func.getFuncId()+"");
		switch (func.getType()) {
		case Func.TYPE_SINGLE_CHOICE_FUZZY_QUERY_COMP://单选查询和单选
		case Func.TYPE_SELECTCOMP:
			value = getSelectCompValue(func,value);
			break;
		case Func.TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY_COMP://模糊搜索其他
		case Func.TYPE_SELECT_OTHER://单选其他
			value = getSelectOtherCompValue(func,infoData,value);
			break;
		case Func.TYPE_MULTI_CHOICE_SPINNER_COMP://多选
		case Func.TYPE_MULTI_CHOICE_FUZZY_QUERY_COMP://模糊多选
			value = getMultiSelectCompValue(func,value);
			break;
//		case Func.TYPE_HIDDEN:
////			if(func.getDefaultType() == Func.DEFAULT_TYPE_SELECT){//隐藏下拉框
////				value = getSelectCompValue(func,value);
////			}
//			
//			if(func.getDefaultValue()!=null){//隐藏域  只显示 DefaultValue的值
//				value=func.getDefaultValue();
//			}
//			
//			break;
			
		}
		return value;
	}

	/**
	 * 其他-下拉框
	 * @param
	 * @param itemData key 控件的控件ID value 当前控件的值
	 * @param key 控件的ID
	 * @return 当前控件的值
	 */
	private String getSelectOtherCompValue(Func func, Map<String, String> itemData, String key) {
		String tvValue ="" ;
		 if(!TextUtils.isEmpty(key)){
			 if("-1".equals(key)){//其他类型
				 tvValue = itemData.get(func.getFuncId()+"_other");
			 }else{//非其他类型
				 tvValue = getSelectCompValue(func, key);
			 }
		 }
		return tvValue;
	}
	
	/**
	 * 如果是多选下来选框时调用该方法,获取下拉选框的值
	 * @param mIds 多选的值 did
	 * @return 所有选择的值的拼接的字符串
	 */
	protected String getMultiSelectCompValue(Func func ,String mIds){
		
		String tvValue ="" ;
		if( !TextUtils.isEmpty(mIds)){//判断非空
			if (func.getOrgOption() != null && func.getOrgOption() == Func.ORG_STORE) {//店面
				
				List<OrgStore> orgStoreList = new OrgStoreDB(InfoDetailActivity.this).findOrgListByStoreId(mIds);
				StringBuffer sb=new StringBuffer();
				for (int j = 0; j < orgStoreList.size(); j++) {
					OrgStore orgStore = orgStoreList.get(j);
					sb.append(",").append(orgStore.getStoreName());
				}
				tvValue = sb.length()>0 ? sb.substring(1).toString():mIds;
				
			} else if (func.getOrgOption() != null && func.getOrgOption() == Func.ORG_USER) {//用户
				
				List<OrgUser> orgUserList = new OrgUserDB(InfoDetailActivity.this).findOrgUserByUserId(mIds);
				StringBuffer sb=new StringBuffer();
				for (int j = 0; j < orgUserList.size(); j++) {
					OrgUser orgUser = orgUserList.get(j);
					sb.append(",").append(orgUser.getUserName());
				}
				tvValue = sb.length()>0 ? sb.substring(1).toString():mIds;
				
			} else if (func.getOrgOption() != null && func.getOrgOption() == Func.ORG_OPTION) {//机构
				
				List<Org> orgList = new OrgDB(InfoDetailActivity.this).findOrgByOrgId(mIds);
				StringBuffer sb=new StringBuffer();
				for (int j = 0; j < orgList.size(); j++) {
					Org org = orgList.get(j);
					sb.append(",").append(org.getOrgName());
				}
				tvValue = sb.length()>0 ? sb.substring(1).toString():mIds;
				
			} else {//其他非机构店面机构用户的下拉框
				tvValue = new  DictDB(InfoDetailActivity.this).findDictMultiChoiceValueStr(
						mIds, func.getDictDataId(),func.getDictTable());
			}
		}
		
		return  tvValue;//返回值
	}
	/**
	 * 如果是下拉选框时调用该方法,获取下拉选框的值
	 * @param func 当前控件
	 * @param key 控件ID
	 * @return 当前控件的值
	 */
	protected String getSelectCompValue(Func func,String key){
		String tvValue ="" ;
		if( !TextUtils.isEmpty(key)){//判断非空
//			Integer selectcompKey =  Integer.parseInt(key);
			if (func.getOrgOption()!=null && func.getOrgOption() == Func.ORG_STORE) {//店面
//				OrgStore orgStore = new OrgStoreDB(InfoDetailActivity.this).findOrgListByStoreId(key);
//				if(orgStore != null){
//					tvValue = orgStore.getStoreName();
//				}
				
				List<OrgStore> orgStoreList = new OrgStoreDB(InfoDetailActivity.this).findOrgListByStoreId(key);
				StringBuffer sb=new StringBuffer();
				for (int j = 0; j < orgStoreList.size(); j++) {
					OrgStore orgStore = orgStoreList.get(j);
					sb.append(",").append(orgStore.getStoreName());
				}
				tvValue = sb.length()>0 ? sb.substring(1).toString():key;
				
			} else if (func.getOrgOption()!=null && func.getOrgOption() == Func.ORG_USER) {//用户
//				OrgUser orgUser = new OrgUserDB(InfoDetailActivity.this).findOrgUserByUserId(key);
//				if(orgUser != null){
//					tvValue = orgUser.getUserName();
//				}
				
				
				List<OrgUser> orgUserList = new OrgUserDB(InfoDetailActivity.this).findOrgUserByUserId(key);
				StringBuffer sb=new StringBuffer();
				for (int j = 0; j < orgUserList.size(); j++) {
					OrgUser orgUser = orgUserList.get(j);
					sb.append(",").append(orgUser.getUserName());
				}
				tvValue = sb.length()>0 ? sb.substring(1).toString():key;
				
			} else if (func.getOrgOption()!=null && func.getOrgOption() == Func.ORG_OPTION) {//机构
				// 机构情况
//				Org org = new OrgDB(InfoDetailActivity.this).findOrgByOrgId(key);
//				if(org != null){
//					tvValue = org.getOrgName();
//				}
				List<Org> orgList = new OrgDB(InfoDetailActivity.this).findOrgByOrgId(key);
				StringBuffer sb=new StringBuffer();
				for (int j = 0; j < orgList.size(); j++) {
					Org org = orgList.get(j);
					sb.append(",").append(org.getOrgName());
				}
				tvValue = sb.length()>0 ? sb.substring(1).toString():key;
			} else {//非机构店面用户的情况
				// 查询的表
				String tableName = func.getDictTable();				
				// 查询表中的列
				String dataId = func.getDictDataId();				
				// 获取下拉内容Dictionary
				Dictionary dic = new DictDB(InfoDetailActivity.this).findDictListByTable(tableName,dataId,key);
				if(dic != null){
					// 复制下拉选框的内容
					tvValue =dic.getCtrlCol();
				}
			}
		}
		return tvValue;
	}	


	/**
	 * 获取表格中内容的集合
	 * @param tablelist 表格中每一行数据的集合
	 * @param funcTableList 表格中所有控件的集合
	 * @return 所有控件的值的集合
	 */
	private List<String> getTableContent(List<HashMap<String, String>> tablelist,List<Func> funcTableList) {
		List<String> sList = new ArrayList<String>();
		for (int i = 0; i < tablelist.size(); i++) { // 循环行数
			HashMap<String, String> table = tablelist.get(i);
			for (int j = 0; j < funcTableList.size(); j++) {// 循环列数
				Func func = funcTableList.get(j); // 得到该列的Func
				String did = table.get(func.getFuncId()+"");
				if (func.getType() == Func.TYPE_EDITCOMP
						|| func.getType() == Func.TYPE_EDITCOMP_NUMERIC || func.getType() == Func.TYPE_DATEPICKERCOMP) {
					sList.add(did);
				}else if(func.getType() == Func.TYPE_SELECT_OTHER){
					if(table.get(func.getFuncId()+"").equals("-1")){
						sList.add(table.get(func.getFuncId()+"_other")); // 将内容添加到List
					}else{
						if(TextUtils.isEmpty(did)){
							sList.add(""); // 将内容添加到List
						}else{
							String tableName = func.getDictTable(); // 查询的表
							String dataId = func.getDictDataId(); // 查询表中的列
							Dictionary dic = new DictDB(InfoDetailActivity.this).findDictListByTable(tableName, dataId, did);
							sList.add(dic.getCtrlCol()); // 将内容添加到List
						}
					}
				} else {
					if (func.getOrgOption() != null && func.getOrgOption()!=Func.OPTION_LOCATION) {// 表格里如果有店或者user
						if (func.getOrgOption() == Func.ORG_STORE) {// 店
//							OrgStore orgStore = new OrgStoreDB(this).findOrgListByStoreId(did);
//							sList.add(orgStore.getStoreName());
							
							List<OrgStore> orgStoreList = new OrgStoreDB(this).findOrgListByStoreId(did);
							StringBuffer sb=new StringBuffer();
							for (int k = 0; k < orgStoreList.size(); k++) {
								sb.append(",").append(orgStoreList.get(k).getStoreName());
							}
							sList.add(sb.length()>0 ? sb.substring(1).toString():did);
							
						} else if (func.getOrgOption() == Func.ORG_USER) {// user
//							OrgUser orgUser = new OrgUserDB(this).findOrgUserByUserId(did);
//							sList.add(orgUser.getUserName());
							
							List<OrgUser> orgUserList = new OrgUserDB(this).findOrgUserByUserId(did);
							StringBuffer sb=new StringBuffer();
							for (int k = 0; k < orgUserList.size(); k++) {
								sb.append(",").append(orgUserList.get(k).getUserName());
							}
							sList.add(sb.length()>0 ? sb.substring(1).toString():did);
							
						}
					} else {
						String tableName = func.getDictTable(); // 查询的表
						String dataId = func.getDictDataId(); // 查询表中的列
						if (!TextUtils.isEmpty(did)) {
							Dictionary dic = new DictDB(InfoDetailActivity.this).findDictListByTable(tableName, dataId, did);
							sList.add(dic.getCtrlCol()); // 将内容添加到List
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
	* 获取Table列集合
	*/
	private List<HashMap<String, String>> getTableList(String json) {
		List<HashMap<String, String>> tableList = new TableParse().parseJason(json);
		return tableList;
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ll_change_info_detail_data: // 修改信息数据
			editInfo();
			break;
	
		}
	}

	/**
	 * 跳转至UpdateStoreFuncActivity便捷店面信息
	 */
	private void editInfo() {
		if(storeType == Func.IS_STORE_UPDATE){
			Intent intent = new Intent(InfoDetailActivity.this,UpdateStoreFuncActivity.class);
			
			Bundle bundle = new Bundle();
			 //存放targetId
			bundle.putInt("targetId", infoId );
			 //存放infoType
			bundle.putInt("infoType", Constants.STORE_INFO_TYPE);
			 //存放storeId
			bundle.putInt("storeId", storeId);
			 //存放targetType
			bundle.putInt("targetType", targetType);
			bundle.putInt("menuType",targetType);
			bundle.putString("funcName", getIntent().getStringExtra("funcName"));
			 //存放storeID
	//		bundle.putInt("storeIdOfInfo", store.getStoreId());
	//		bundle.putString(Constants.DATA_STATUS, clickItem.get(Constants.DATA_STATUS));
		
		
			// 保存数据到数据库
			SubmitDB submitDB = new SubmitDB(InfoDetailActivity.this);
			SubmitItemDB submitItemDB = new SubmitItemDB(InfoDetailActivity.this);
			ArrayList<String> orgString = new ArrayList<String>();
			saveData(infoId,infoData, submitDB, submitItemDB,orgString,false);
			if(orgString != null && !orgString.isEmpty()){
				bundle.putStringArrayList("map",orgString);
			}
			// 在intent中放入bundle
			intent.putExtra("bundle", bundle);
			startActivity(intent);
			InfoDetailActivity.this.finish();
		}else{
			ToastOrder.makeText(InfoDetailActivity.this, R.string.visit_store9, ToastOrder.LENGTH_LONG).show();
		}
	}

	/**
	 * 将联网获取的值保存到数据库
	 * @param targetId 数据ID
	 * @param saveItem key 控件的iD value 控件的值
	 * @param submitDB 提交表单数据库
	 * @param submitItemDB 提交表单的值的数据库
	 * @param orgString  组织机构关系的拼接
	 * @param isLink 是否是超链接 true 是超链接 false 不是超链接
  	 */
	private void saveData(int targetId, Map<String, String> saveItem, SubmitDB submitDB,
			SubmitItemDB submitItemDB, ArrayList<String> orgString,boolean isLink) {
		if(saveItem==null||saveItem.isEmpty()){
			return;
		}
		Submit submit = new Submit();
		
		submit.setTargetid(targetId);
		if(!TextUtils.isEmpty(saveItem.get(Constants.PATCH_ID))){//给提交表单设置时间戳 服务端使用
			submit.setTimestamp(saveItem.get(Constants.PATCH_ID));
			JLog.d(TAG, "PATCH_ID=========>"+saveItem.get(Constants.PATCH_ID));
		}
		if(!TextUtils.isEmpty(saveItem.get(Constants.TASK_ID))){//双向执行的时候的数据ID
			submit.setDoubleId(Integer.valueOf(saveItem.get(Constants.TASK_ID)));
		}
		JLog.d(TAG, "clickItem.get(\"taskid\")=========>"+saveItem.get(Constants.TASK_ID));
		submit.setStoreId(storeId);//设置店面ID
		submit.setTargetType(targetType);//设置菜单type
		submit.setState(Submit.STATE_NO_SUBMIT);//设置提交状态
		submit.setMenuId(menuId);
		submit.setMenuType(targetType);
		submit.setMenuName(menuName);
		submitDB.insertSubmit(submit);
		
		// 通过targetId/targetType/Submit.State查询submitId
		int submitId = submitDB.selectSubmitIdNotCheckOut(null, null, null,targetId, targetType,Submit.STATE_NO_SUBMIT);

		
	
		StringBuilder sb = new StringBuilder();
		for (Func func : funcList) {// 遍历
			//拍照 定位 按钮类型的不处理
			if(func.getType() == Func.TYPE_CAMERA || func.getType() == Func.TYPE_CAMERA_HEIGHT || func.getType() == Func.TYPE_CAMERA_CUSTOM || func.getType() == Func.TYPE_CAMERA_MIDDLE || func.getType() == Func.TYPE_LOCATION || func.getType() == Func.TYPE_BUTTON){
				continue;
			}
			//隐藏域下拉框不处理
			if(func.getDefaultType()!=null && (func.getDefaultType()==Func.DEFAULT_TYPE_SELECT ||func.getDefaultType()==Func.DEFAULT_TYPE_MULTI_SELECT)){
				continue;
			}
			
			//如果值不为空就存储到SubmitItemDB
			if(!TextUtils.isEmpty(saveItem.get(func.getFuncId().toString()))){
					SubmitItem submitItem = new SubmitItem();
					submitItem.setSubmitId(submitId);
					submitItem.setType(func.getType());
					submitItem.setTargetId(func.getTargetid()+"");
					submitItem.setParamName(func.getFuncId().toString());
					submitItem.setParamValue(saveItem.get(func.getFuncId().toString()));
					submitItem.setIsCacheFun(func.getIsCacheFun());
					submitItemDB.insertSubmitItem(submitItem,false);
					JLog.d(TAG, "submitItem=====ParamName-->"+submitItem.getParamName()+"<=====>ParamValue-->"+submitItem.getParamValue());
					if((func.getType() == Func.TYPE_SELECT_OTHER || func.getType() == Func.TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY_COMP) && saveItem.get(func.getFuncId().toString()).equals("-1")){
						SubmitItem submitItem_other = new SubmitItem();
						submitItem_other.setSubmitId(submitId);
						submitItem_other.setType(func.getType());
						submitItem_other.setTargetId(func.getTargetid()+"");
						//其他类型的下拉框如果是选择的其他值则从func.getFuncId().toString()+"_other"中取值
						submitItem_other.setParamName(func.getFuncId().toString()+"_other");
						submitItem_other.setParamValue(saveItem.get(func.getFuncId().toString()+"_other"));
						submitItemDB.insertSubmitItem(submitItem_other,false);
					}
					//如果有组织结构则把机构关系拼接成串 
					if(func.getOrgOption() != null && func.getOrgOption() == Func.ORG_OPTION){
						sb.append(";").append(func.getOrgLevel()).append(":").append(saveItem.get(func.getFuncId().toString()));
					}
			}
		}
		if(sb != null && sb.length() > 1){
			orgString.add(targetId+sb.toString()) ;
		}
	}
	
	/**
	 * 联网查询数据
	 */
	private void postSearch() {
	
		// 获得存放电话SharedPreferences
		params = new HashMap<String, String>();
		//加入targertID(taskid是传给服务器的用的key)
		params.put("taskid", String.valueOf(infoId));
		//加入店面ID
		params.put("storeid", String.valueOf(storeId));
		//加入电话号码
		params.put("phoneno", PublicUtils.receivePhoneNO(this));
		
		RequestParams requestParams = new RequestParams();
		
		for(Map.Entry<String,String> e : params.entrySet()){
			JLog.d(TAG, "params  <<< "+e.getKey()+"==>"+e.getValue());
			requestParams.put(e.getKey(), e.getValue());
		}
		
		GcgHttpClient.getInstance(InfoDetailActivity.this).post(UrlInfo.getUrlReplenish(InfoDetailActivity.this), requestParams, new HttpResponseListener(){
			@Override
			public void onStart() {
			}
			
			
			@Override
			public void onFailure(Throwable error, String content) {
				// 关闭加载对话框
				dismissLoadDialog();
				ToastOrder.makeText(InfoDetailActivity.this, R.string.visit_store14, ToastOrder.LENGTH_LONG).show();
				JLog.d(TAG, "<<<<<<<<<<<msg.obj"+content);
			}
			
			@Override
			public void onFinish() {
			}


			@Override
			public void onSuccess(int statusCode, String content) {
				// 获取JSON
				// 判断是否返回  "resultcode":"0001"
				if("{\"resultcode\":\"0001\"}".equals(content)){
					ToastOrder.makeText(InfoDetailActivity.this, R.string.zhifu_no_search_data, ToastOrder.LENGTH_LONG).show();
					// 关闭加载对话框
					dismissLoadDialog();
					return;
				}
				JLog.d(TAG, "JSON   =======>>>>>" + content);
				parseInfoJsonOnThread(content);
			}
		});
	}

	/**
	 * 开启一个线程解析JSON
	 * @param json 要解析的值
	 */
	private void parseInfoJsonOnThread(final String json) {
		new Thread(){
			public void run() {
				// 解析JSON
				Message msg = new Message();
				msg.what = PARSE_FAIL;
				try {
					ReplenishSearchResult searchResult = new ReplenishParse().parseSearchResult(json);
					List<Map<String, String>> resultList = searchResult.getResultList();
					if(resultList!=null &&resultList.size()>0){
						infoData = resultList.get(0);
						msg.what = PARSE_OK;//
					}
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					mHandler.sendMessage(msg);
				}
			};
		}.start();
		
	}
	
	/**
	 * 关闭加载的dialog
	 */
	private void dismissLoadDialog() {
		if(loadDialog != null &&  loadDialog.isShowing()){
			loadDialog.dismiss();
		}
	}
	
	@Override
	protected void onDestroy() {
		dismissLoadDialog();
		super.onDestroy();
	}
}
