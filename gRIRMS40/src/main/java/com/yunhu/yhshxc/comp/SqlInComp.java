package com.yunhu.yhshxc.comp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.bo.SubmitItem;
import com.yunhu.yhshxc.database.SubmitDB;
import com.yunhu.yhshxc.database.SubmitItemDB;
import com.yunhu.yhshxc.database.SubmitItemTempDB;
import com.yunhu.yhshxc.func.AbsFuncActivity;
import com.yunhu.yhshxc.func.DialogInFuncManager;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.list.activity.TableListActivity;
import com.yunhu.yhshxc.list.activity.TableListActivityNew;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gcg.org.debug.ELog;
import gcg.org.debug.JLog;

/**
 * sql类型的控件处理
 * @author jishen
 *
 */
public class SqlInComp {
	
	private final String TAG = "SqlInComp";
	private Context context = null;
	private boolean isLink=false;//是否是超链接里的控件
	/**
	 * sql分销标准
	 */
	private String sqlparam = "";
	/**
	 * 模块类型
	 */
	private int modType;
	private AbsFuncActivity act = null;
	
	private int menuId;
	private String menuName;
	private View v;
	public SqlInComp(Context context,int modType,boolean isLink,int menuId,String menuName) {
		this.isLink=isLink;
		this.context = context;
		this.modType = modType;
		this.menuId = menuId;
		this.menuName = menuName;
		act = (AbsFuncActivity)context;
	}
	public SqlInComp(Context context,int modType,boolean isLink,int menuId,String menuName,View view) {
		this.isLink=isLink;
		this.context = context;
		this.modType = modType;
		this.menuId = menuId;
		this.menuName = menuName;
		act = (AbsFuncActivity)context;
		this.v = view;
	}
	List<String> contrlEnterList = new ArrayList<String>();// 控制开关的集合
	public SqlInComp(Context context,int modType,boolean isLink,int menuId,String menuName,View view,List<String> contrlEnterList) {
		this.isLink=isLink;
		this.context = context;
		this.modType = modType;
		this.menuId = menuId;
		this.menuName = menuName;
		act = (AbsFuncActivity)context;
		this.v = view;
		this.contrlEnterList = contrlEnterList;

	}
	
	public void getSearchRequirement2(final Func func){
		String phoneNo = PublicUtils.receivePhoneNO(context);
		String url=UrlInfo.baseUrl(context)+"distributionSqlParamInfo.do?defaultData="+func.getDefaultValue()+"&phoneno="+phoneNo;
		JLog.d(TAG, "取得分销标准 SQL参数 URL:"+url);
		GcgHttpClient.getInstance(context).get(url, null, new HttpResponseListener(){
			@Override
			public void onStart() {
				new DialogInFuncManager(act).loadingDialog(context.getResources().getString(R.string.initTable));//加载dialog
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				AbsFuncActivity activity = (AbsFuncActivity)context;
				if(activity.dialog!=null){
					activity.dialog.dismiss();
				}
				act.isCanClick=true;
				ToastOrder.makeText(context, context.getResources().getString(R.string.network_isfalse_please_tryagain), ToastOrder.LENGTH_LONG).show();
				if(func.getType()==Func.TYPE_TABLECOMP){
					act.bundle.putBoolean("isNetCanUse", false);//网络异常仍然跳转到表格false表示网络异常
					CompDialog comp=new CompDialog(context, func, act.bundle);
					comp.getObject();
				}
			}
			
			@Override
			public void onFinish() {
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				JLog.d(TAG, "sql:"+content);
				AbsFuncActivity activity = (AbsFuncActivity)context;
				if(activity.dialog!=null){
					activity.dialog.dismiss();
				}
				act.isCanClick=true;
				JSONObject jsonObject = null;
				String resultcode = null;
				try {
					jsonObject = new JSONObject(content);
					resultcode = jsonObject.getString(Constants.RESULT_CODE);
				} catch (JSONException e) {
					act.isCanClick=true;
					ToastOrder.makeText(context, context.getResources().getString(R.string.network_isfalse_please_tryagain), ToastOrder.LENGTH_LONG).show();
					if(func.getType()==Func.TYPE_TABLECOMP){
						act.bundle.putBoolean("isNetCanUse", false);//网络异常仍然跳转到表格false表示网络异常
						CompDialog comp=new CompDialog(context, func, act.bundle);
						comp.getObject();
					}
					JLog.d(TAG, "获取分销标准SQL："+e.getMessage());
				}
				if(resultcode!=null && resultcode.equals(Constants.RESULT_CODE_SUCCESS)){
					String funcSql=null;
					try {
						JSONObject obj=new JSONObject(content);
						funcSql=obj.getString("sqlparam");
					} catch (JSONException e) {
						JLog.d(TAG, e.getMessage());
					}
					result2(func,funcSql);
				}else{
					act.isCanClick=true;
					ToastOrder.makeText(context, context.getResources().getString(R.string.network_isfalse_please_tryagain), ToastOrder.LENGTH_LONG).show();
					if(func.getType()==Func.TYPE_TABLECOMP){
						act.bundle.putBoolean("isNetCanUse", false);//网络异常仍然跳转到表格false表示网络异常
						CompDialog comp=new CompDialog(context, func, act.bundle);
						comp.getObject();
					}
				}
				
			}
		});
	};
	
	private void result2(final Func func,String funcSql){
		boolean isPassSql = true;//要求操作的项是否都操作 true表示都操作 false表示有选项没操作
		Map<String, String> param=new HashMap<String, String>();
		AbsFuncActivity activity = (AbsFuncActivity)context;
		if(!TextUtils.isEmpty(funcSql)){
			String[] str=funcSql.split(",");
			int submitId = new SubmitDB(context).selectSubmitId(activity.planId, activity.wayId, activity.storeId,activity.targetId, activity.menuType);
			for (int i = 0; i < str.length; i++) {
				String sqlKey=str[i];
				if(sqlKey.equals("1")){//访店中的超链接要把店面ID传给服务器
					param.put(str[i], activity.sqlStoreId==0 ? activity.storeId+"":activity.sqlStoreId+"");
				}else if(sqlKey.equals("2")){//传用户ID
					param.put(str[i], SharedPreferencesUtil.getInstance(context).getUserId()+"");
				}else if(sqlKey.equals("3")){//传双向任务的任务ID
					param.put(str[i], activity.taskId+"");
				}else{
					Integer id = Integer.parseInt(str[i].split("\\.")[1].split("_")[1]);
					SubmitItem item=null;
					if(isLink){
						item=new SubmitItemTempDB(context).findSubmitItemBySubmitIdAndFuncId(submitId,id);
					}else{
						item=new SubmitItemDB(context).findSubmitItemBySubmitIdAndFuncId(submitId,id);
					}
					if(item!=null && !TextUtils.isEmpty(item.getParamValue())){
						param.put(str[i], item.getParamValue());
					}else{
						if(func.getType()==Func.TYPE_LINK){//超链接sql查询并且查询条件是空的情况下
							param.put("did", activity.taskId+"");
							param.put(str[i],"@@");
						}else{
							isPassSql = false;
							Func findFunc = activity.getFuncByFuncId(id);
							if(findFunc != null){
								ToastOrder.makeText(context,findFunc.getName()+context.getResources().getString(R.string.no_input), ToastOrder.LENGTH_LONG).show();
							}else{
								ToastOrder.makeText(context, context.getResources().getString(R.string.configure_false_nothis_view), ToastOrder.LENGTH_LONG).show();
							}
							break;
						}
					}
				}
			}
			if(!param.isEmpty()){
				this.sqlparam = new JSONObject(param).toString();
			}
		
		}
		if(isPassSql){//如果必须输入的值已经输入就发起网络请求获取sql查询到的值
			final String phoneNo = PublicUtils.receivePhoneNO(context);
			String t = getSql_T(func);
			String sqlUrl = UrlInfo.baseUrl(context)+"distributionSqlDataInfo.do?ctrlType="+t+"&defaultData="+func.getDefaultValue()+"&phoneno="+phoneNo+"&tabId="+func.getMenuId();
			JLog.d(TAG, "取得分销标准 SQL数据URL:"+sqlUrl);
			RequestParams params = new RequestParams();
			params.put("sqlparam", sqlparam);
			JLog.d(TAG, "sqlparam:"+sqlparam);
			GcgHttpClient.getInstance(context).post(sqlUrl, params, new HttpResponseListener(){
				@Override
				public void onStart() {
					new DialogInFuncManager(act).loadingDialog(context.getResources().getString(R.string.initTable));
				}
				
			
				@Override
				public void onFailure(Throwable error, String content) {
					AbsFuncActivity activity = (AbsFuncActivity)context;
					if(activity.dialog!=null){
						activity.dialog.dismiss();
					}
					act.isCanClick=true;
					ToastOrder.makeText(context, context.getResources().getString(R.string.network_isfalse_please_tryagain), ToastOrder.LENGTH_LONG).show();
					return;
				}
				
				@Override
				public void onFinish() {
					
				}

				@Override
				public void onSuccess(int statusCode, String content) {

					AbsFuncActivity activity = (AbsFuncActivity)context;
					if(activity!=null&&activity.dialog!=null&&activity.dialog.isShowing()){
						activity.dialog.dismiss();
					}
					act.isCanClick=true;
					JSONObject jsonObject = null;
					String resultcode = null;
					try {
						jsonObject = new JSONObject(content);
						resultcode = jsonObject.getString(Constants.RESULT_CODE);
					} catch (JSONException e) {
						ToastOrder.makeText(context, context.getResources().getString(R.string.no_has_select_data), ToastOrder.LENGTH_LONG).show();
						return;
					}
					if(resultcode!=null && resultcode.equals(Constants.RESULT_CODE_SUCCESS)){
						JLog.d(TAG, "getSql:"+content);
						
						String defaultSql="";//sql查询到的值
						try {
							JSONObject obj=new JSONObject(content);
							defaultSql=obj.getString("sqldata");
						} catch (JSONException e) {
							ToastOrder.makeText(context, context.getResources().getString(R.string.no_has_select_data), ToastOrder.LENGTH_LONG).show();
							return;
						}
						JLog.d(TAG, "defaultSql:"+defaultSql);
						
						switch (func.getType()) {
						
						case Func.TYPE_EDITCOMP://输入框
//							act.isCanClick = true;
//							RelativeLayout iv_func_photo_container = (RelativeLayout) v
//									.findViewById(R.id.iv_func_photo_container);
//							EditText textEditTextComp = (EditText) iv_func_photo_container.findViewById(R.id.textEditTextComp);
//							textEditTextComp.setText(defaultSql);
//							textEditTextComp.setFocusable(true);
//							textEditTextComp.setFocusableInTouchMode(true);
//							textEditTextComp.requestFocus();
//							textEditTextComp.findFocus();
//							InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//							imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//							break;
						case Func.TYPE_SELECTCOMP://下拉框
						case Func.TYPE_SINGLE_CHOICE_FUZZY_QUERY_COMP://单选模糊搜索
						case Func.TYPE_SELECT_OTHER://单选其他
						case Func.TYPE_MULTI_CHOICE_SPINNER_COMP://多选
						case Func.TYPE_MULTI_CHOICE_FUZZY_QUERY_COMP://多选模糊搜索
						case Func.TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY_COMP://单选模糊搜索其他
							CompDialog c=new CompDialog(context, func, act.bundle);
							c.setDefaultSql(defaultSql);
							c.setMenuId(menuId);
							c.setMenuName(menuName);
							c.setMenuType(modType);
							act.dialog = c.createDialog();
							act.dialog.show();
							break;
						case Func.TYPE_TEXTCOMP://文本标签
							CompDialog c1=new CompDialog(context, func,v, act.bundle,contrlEnterList);
							c1.setDefaultSql(defaultSql);
							c1.setMenuId(menuId);
							c1.setMenuName(menuName);
							c1.setMenuType(modType);
							act.dialog = c1.createDialog();
							act.dialog.show();
//							act.isCanClick = true;
//							TextView tv_func_content = (TextView) v
//									.findViewById(R.id.tv_func_content);
//							tv_func_content.setVisibility(View.VISIBLE);
//							tv_func_content.setText(defaultSql);
							break;
						case Func.TYPE_TABLECOMP://表格
							CompDialog comp=new CompDialog(context, func, act.bundle);
							comp.setDefaultSql(defaultSql);
							comp.getObject();
							break;
						case Func.TYPE_LINK://超链接
							if(!TextUtils.isEmpty(defaultSql)){
								ELog.d("Open TableListActivity");
								Intent intent = new Intent(context,TableListActivityNew.class);
								Bundle sqlbundle=new Bundle();
								sqlbundle.putInt("menuType", Menu.TYPE_MODULE);
								sqlbundle.putInt("targetId",func.getMenuId());
								sqlbundle.putInt("modType",modType);
								sqlbundle.putBoolean(TableListActivity.TAG_IS_LINK, true);
								sqlbundle.putInt("menuId", menuId);
								sqlbundle.putString("menuName", menuName);
								intent.putExtra("bundle", sqlbundle);
								intent.putExtra("sqlLinkJson", defaultSql);
								
								context.startActivity(intent);
							}else{
								ToastOrder.makeText(context, context.getResources().getString(R.string.no_has_select_data), ToastOrder.LENGTH_LONG).show();
							}
							break;

						default:
							ToastOrder.makeText(context, context.getResources().getString(R.string.type_is_not_supported), ToastOrder.LENGTH_LONG).show();
							break;
						}
					}else{
						ToastOrder.makeText(context, context.getResources().getString(R.string.no_has_select_data), ToastOrder.LENGTH_LONG).show();
					}

				
					
				}
			});
		}
		
	}
	
	private String getSql_T(Func func){
		String t = null;
		switch (func.getType()) {
		case Func.TYPE_EDITCOMP://输入框
			t="1";
			break;
		case Func.TYPE_SELECTCOMP://下拉框
			t="3";
			break;
		case Func.TYPE_SINGLE_CHOICE_FUZZY_QUERY_COMP:
			t="4";  //模糊搜索单选下拉框
			break;
		case Func.TYPE_SELECT_OTHER:
			t="21"; //模糊搜索其他
			break;
		case Func.TYPE_MULTI_CHOICE_SPINNER_COMP:
			t="5"; // 多选下拉框
			break;
		case Func.TYPE_MULTI_CHOICE_FUZZY_QUERY_COMP:
			t="28"; //模糊多选下拉框
			break;
		case Func.TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY_COMP:
			t="29"; //模糊单选下拉框其他
			break;
		case Func.TYPE_TABLECOMP://表格
			t="18";
			break;
		case Func.TYPE_LINK://超链接
			t="30";
			break;
		case Func.TYPE_TEXTCOMP://文本标签
			t="9";
			break;
		}
		return t;
	}
	
}
