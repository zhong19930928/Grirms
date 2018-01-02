package com.yunhu.yhshxc.parser;

import gcg.org.debug.JLog;
import android.content.Context;

import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.database.MainMenuDB;
import com.yunhu.yhshxc.database.OrgDB;
import com.yunhu.yhshxc.database.OrgStoreDB;
import com.yunhu.yhshxc.database.OrgUserDB;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.utility.URLWrapper;
import com.yunhu.yhshxc.utility.UrlInfo;

/**
 * 获取组织机构、用户、店面
 * 
 * @author jishen
 * 
 */
public class GetOrg {

	private final String TAG = "GetOrg";
	private Context context;
	private ResponseListener listener;

	public GetOrg(Context context, ResponseListener listener) {
		this.context = context;
		this.listener = listener;
	}
	
	/**
	 * 网络获取机构\用户\店面
	 */
	public void getAll() {

		URLWrapper wrapper = new URLWrapper(
				UrlInfo.getUrlInitOperation(context));
		wrapper.addParameter("operation", Func.ORG_OPTION+","+Func.ORG_USER+","+Func.ORG_STORE);
		String url = wrapper.getRequestURL();
		JLog.d(TAG, url);
		GcgHttpClient.getInstance(context.getApplicationContext()).get(url,null, new HttpResponseListener() {

					@Override
					public void onSuccess(int statusCode, String content) {
						listener.onSuccess(0, content);
					}

					@Override
					public void onFailure(Throwable error, String content) {
						listener.onFailure(0);
					}

					@Override
					public void onStart() {
						
					}

					@Override
					public void onFinish() {
						
					}

				});

	}

	/**
	 * 网络获取机构
	 */
	public void getOrg() {

		URLWrapper wrapper = new URLWrapper(
				UrlInfo.getUrlInitOperation(context));
		wrapper.addParameter("operation", Func.ORG_OPTION);
		String url = wrapper.getRequestURL();
		JLog.d(TAG, url);
		GcgHttpClient.getInstance(context.getApplicationContext()).get(url,null, new HttpResponseListener() {

					@Override
					public void onSuccess(int statusCode, String content) {
						listener.onSuccess(Func.ORG_OPTION, content);
					}

					@Override
					public void onFailure(Throwable error, String content) {
						listener.onFailure(Func.ORG_OPTION);
					}

					@Override
					public void onStart() {
						
					}

					@Override
					public void onFinish() {
						
					}

				});

	}

	/**
	 * 网络获取机构用户
	 */
	public void getUser() {

		URLWrapper wrapper = new URLWrapper(
				UrlInfo.getUrlInitOperation(context));
		wrapper.addParameter("operation", Func.ORG_USER);
		String url = wrapper.getRequestURL();
		JLog.d(TAG, url);
		GcgHttpClient.getInstance(context.getApplicationContext()).get(url,null, new HttpResponseListener() {

					@Override
					public void onSuccess(int statusCode, String content) {
						listener.onSuccess(Func.ORG_USER, content);
					}

					@Override
					public void onFailure(Throwable error, String content) {
						listener.onFailure(Func.ORG_USER);
					}

					@Override
					public void onStart() {
						
					}

					@Override
					public void onFinish() {
						
					}

				});

	}

	/**
	 * 网络获取机构店面
	 */
	public void getStore() {

		URLWrapper wrapper = new URLWrapper(
				UrlInfo.getUrlInitOperation(context));
		wrapper.addParameter("operation", Func.ORG_STORE);
		String url = wrapper.getRequestURL();
		JLog.d(TAG, url);
		GcgHttpClient.getInstance(context.getApplicationContext()).get(url,
				null, new HttpResponseListener() {

					@Override
					public void onSuccess(int statusCode, String content) {
						listener.onSuccess(Func.ORG_STORE, content);
					}

					@Override
					public void onFailure(Throwable error, String content) {
						listener.onFailure(Func.ORG_STORE);
					}

					@Override
					public void onStart() {
						
					}

					@Override
					public void onFinish() {
						
					}

				});
	}

	/**
	 * 用来判断是否需要下载机构
	 * 
	 * @return
	 */
	public boolean getOrgOperation() {
		OrgDB db = new OrgDB(context);
		boolean ishas = db.isHasOrg();
		if (ishas || isHasStoreExpand()) {
			return db.isEmpty();
		} else {
			return false;
		}
		//return false;
	}

	/**
	 * 是否有新店上报模块
	 * @return
	 */
	private boolean isHasStoreExpand(){
		boolean flag = false;
		Menu menu  = new MainMenuDB(context).findMenuListByMenuType(Menu.IS_STORE_ADD_MOD);
		if (menu!=null) {
			flag = true;
		}
		return flag;
	}
	/**
	 * 用来判断是否需要下载用户
	 * 
	 * @return
	 */
	public boolean getUserOperation() {

		OrgUserDB db = new OrgUserDB(context);
		boolean ishas1 = db.isHasUserByFunc();
		boolean ishas2 = db.isHasUserByMenu();
		boolean ishas3 = db.isHasUserByModule();
		if (ishas1 || ishas2 || ishas3 || isHasStoreExpand()) {
			return db.isEmpty();
		} else {
			return false;
		}
		//return false;
	}

	/**
	 * 用来判断是否需要下载店面
	 * 
	 * @return
	 */
	public boolean getStoreOperation() {
		OrgStoreDB db = new OrgStoreDB(context);
		boolean ishas = db.isHasOrgStore();
		if (ishas || isHasOrderMenu() ) {
			return db.isEmpty();
		} else {
			return false;
		}
		//return false;
	}
	
	public boolean isHasOrderMenu(){
		return new MainMenuDB(context).isHasOrderMenu();
	}

	/**
	 * 网络返回值回调
	 * 
	 * @author david.hou
	 * 
	 */
	public interface ResponseListener {
		public void onSuccess(int type, String result);

		public void onFailure(int type);
	}

}
