package com.yunhu.yhshxc.activity.synchrodata;

import gcg.org.debug.JLog;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.HomePageActivity;
import com.yunhu.yhshxc.application.SoftApplication;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.database.DatabaseHelper;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.parser.GetOrg;
import com.yunhu.yhshxc.parser.OrgParser;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil2;
import com.yunhu.yhshxc.widget.ToastOrder;

public class SyncInitData {
	private Context context;
	private final String TAG = "SyncInitData";
//	private AlertDialog alertDialog;
	private HomePageActivity homeMenuActivity;
	public SyncInitData(Context mContext) {
		this.context = mContext;

		if (context instanceof HomePageActivity){
			homeMenuActivity = (HomePageActivity)context;
		}else {
			//获得HomePageActivity对象
			SoftApplication app = (SoftApplication) context.getApplicationContext();
			homeMenuActivity = app.getHomePageActivity();
		}


		
	}
	/**
	 * 更新全部数据
	 */
	private Dialog syncDialog = null;
	public void syncAll() {
		syncDialog = new MyProgressDialog(context,R.style.CustomProgressDialog,PublicUtils.getResourceString(context,R.string.visit_data2)+"...");
//		View loView = View.inflate(context, R.layout.loading_view_layout, null);
//		ImageView close_dialog = (ImageView) loView.findViewById(R.id.close_dialog);
//		close_dialog.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				if (alertDialog.isShowing()) {
//					alertDialog.dismiss();
//				}
//
//			}
//		});
//		
//		alertDialog = new AlertDialog.Builder(context).setView(loView).create();
//		alertDialog.setCancelable(false);
		String url = new SyncDataUrl(context).allInfo();
		GcgHttpClient.getInstance(context).get(url, null, new HttpResponseListener() {
			
			@Override
			public void onSuccess(int statusCode, String content) {
				new SyncTask().execute(content);
			}
			
			@Override
			public void onStart() {
				if (syncDialog!=null && !syncDialog.isShowing()) {
					syncDialog.show();
				}
//				if (alertDialog!=null&&!alertDialog.isShowing()) {
//					alertDialog.show();
//				}
			}
			
			@Override
			public void onFinish() {
			
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				if (syncDialog!=null && syncDialog.isShowing()) {
					syncDialog.dismiss();;
				}
//				if (alertDialog!=null&&alertDialog.isShowing()) {
//					alertDialog.dismiss();;
//				}
				ToastOrder.makeText(context, R.string.visit_data3, ToastOrder.LENGTH_SHORT).show();
			}
		});
	}
	
	/**
	 * 解析allinfo数据
	 *
	 */
	private class SyncTask extends AsyncTask<String,Integer, Boolean>{

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			clearCache("全部更新数据"); //清除缓存数据，以便重新初始化
		}
		
		@Override
		protected Boolean doInBackground(String... params) {
			String param = params[0];
			try {
				JSONObject jsonObject = new JSONObject(param);
				String resultcode = jsonObject.getString(Constants.RESULT_CODE);
				//"0000"表示返回成功"0001"表示返回失败"0002"表示后台没检测到用户
				if(resultcode.equalsIgnoreCase(Constants.RESULT_CODE_SUCCESS)){
					new SyncParse(context).paserJson(param);
					isContinue(true);
					return true;
				}else{
					isContinue(false);
					return false;
				}
			} catch (Exception e) {
				isContinue(false);
				return false;
			}
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result) {
				syncOperation();
//				getOrg(orgType);
			}else{
				if (syncDialog!=null && syncDialog.isShowing()) {
					syncDialog.dismiss();;
				}
//				if (alertDialog!=null&&alertDialog.isShowing()) {
//					alertDialog.dismiss();;
//				}
				ToastOrder.makeText(context, R.string.ERROR_DATA, ToastOrder.LENGTH_SHORT).show();
			}
		}
		private void isContinue(Boolean result){
			//4.1版本以上
			if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN){
				
			}else{
					Message message = mHandler.obtainMessage();
					message.obj = result;
					message.sendToTarget();
			}
		}
		
		private Handler mHandler = new  Handler(){
			public void handleMessage(Message msg) {
				boolean flag = (Boolean) msg.obj;
				if (flag) {
					syncOperation();
//					getOrg(orgType);
				}else{
					if (syncDialog!=null && syncDialog.isShowing()) {
						syncDialog.dismiss();;
					}
//					if (alertDialog!=null&&alertDialog.isShowing()) {
//						alertDialog.dismiss();;
//					}
					ToastOrder.makeText(context, R.string.ERROR_DATA, ToastOrder.LENGTH_SHORT).show();
				}
			};
		};
		
	}
	
	/**
	 * 更新组织机构
	 */
	private void syncOperation() {
		String url = new SyncDataUrl(context).allOperation();
		GcgHttpClient.getInstance(context).get(url, null, new HttpResponseListener() {
			
			@Override
			public void onSuccess(int statusCode, String content) {
				new SyncOrgAnsyncTask().execute(content);
			}
			
			@Override
			public void onStart() {
			}
			
			@Override
			public void onFinish() {
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				if (syncDialog!=null && syncDialog.isShowing()) {
					syncDialog.dismiss();;
				}
//				if (alertDialog!=null&&alertDialog.isShowing()) {
//					alertDialog.dismiss();;
//				}
				ToastOrder.makeText(context,R.string.visit_data3, ToastOrder.LENGTH_SHORT).show();
			}
		});
		
	}
	
	/**
	 * 清除SharedPreferencesUtil2 SharedPrefsBackstageLocation 删除数据库中的表
	 * @param temp 标识为什么要清楚存LOG使用
	 */
	private void clearCache(String temp){
		JLog.writeErrorLog(Thread.currentThread().getName()+"("+temp+")=>删除全部缓存", "SyncAllData");
		SharedPreferencesUtil2.getInstance(context.getApplicationContext()).clearAll();
//		SharedPrefsBackstageLocation.getInstance(context.getApplicationContext()).clearAll();
		DatabaseHelper.getInstance(context).removeAllData();
//		new Dao(context).delete();
	}
	
	
	
	/**
	 * 解析机构、店面、用户
	 */
	private class SyncOrgAnsyncTask extends AsyncTask<String,Integer, Boolean> {
		
		@Override
		protected Boolean doInBackground(String... params) {
			try {
				String json = params[0];//获取要解析的json数据
				JSONObject jsonObject = new JSONObject(json);
				String resultcode = jsonObject.getString(Constants.RESULT_CODE);
				//"0000"表示返回成功"0001"表示返回失败"0002"表示后台没检测到用户
				if(resultcode.equalsIgnoreCase(Constants.RESULT_CODE_SUCCESS)){
					new OrgParser(context).syncParseAll(json);
					isContinue(true);
					return true;
				}else{
					isContinue(false);
					return false;
				}
			} catch (Exception e) {
				isContinue(false);
				return false;
			}
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (syncDialog!=null && syncDialog.isShowing()) {
				syncDialog.dismiss();;
			}
//			if (alertDialog!=null&&alertDialog.isShowing()) {
//				alertDialog.dismiss();;
//			}
			if (result) {
				ToastOrder.makeText(context,R.string.visit_data4, ToastOrder.LENGTH_SHORT).show();
				//尝试删除多余的push
				clearPush(Constants.URL_PUSH_CLEAN_ALL);
				//发送广播更新数据
				if (homeMenuActivity!=null) {
					homeMenuActivity.refreshUI();
					if(homeMenuActivity.homeFragment!=null)
					homeMenuActivity.homeFragment.refreshUI();
				}
				
			}else{
				ToastOrder.makeText(context, R.string.ERROR_DATA, ToastOrder.LENGTH_SHORT).show();
			}
		
		}
		
		private void isContinue(Boolean result){
			//4.1版本以上
			if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN){
				
			}else{
					Message message = mHandler.obtainMessage();
					message.obj = result;
					message.sendToTarget();
			}
		}
		
		private Handler mHandler = new  Handler(){
			public void handleMessage(Message msg) {
				boolean flag = (Boolean) msg.obj;
				if (syncDialog!=null && syncDialog.isShowing()) {
					syncDialog.dismiss();;
				}
//				if (alertDialog!=null&&alertDialog.isShowing()) {
//					alertDialog.dismiss();;
//				}
				if (flag) {
					ToastOrder.makeText(context, R.string.visit_data4, ToastOrder.LENGTH_SHORT).show();
					//尝试删除多余的push
					clearPush(Constants.URL_PUSH_CLEAN_ALL);
					//发送广播更新数据
					if (homeMenuActivity!=null) {
//						homeMenuActivity.refreshUI();
						if(homeMenuActivity.homeFragment!=null)
						homeMenuActivity.homeFragment.refreshUI();
					}
					
				}else{
					ToastOrder.makeText(context, R.string.ERROR_DATA, ToastOrder.LENGTH_SHORT).show();
				}
			};
		};
	}
//	
	/**
	 * //请求服务器删除多余的push数据
	 * @param url
	 */
	private int clearPushNum = 0;
	private void clearPush(final String url){
		GcgHttpClient.getInstance(context).get(url+"&mdn="+PublicUtils.receivePhoneNO(context), null, new HttpResponseListener(){

			@Override
			public void onFailure(Throwable error, String content) {
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				String r = PublicUtils.verifyReturnValue(content);
				if (TextUtils.isEmpty(r) && clearPushNum < 3) {
					clearPush(url);
					clearPushNum++;
				}
				
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
	 * 初始化机构、用户、店面
	 * 此方法会根据type来进行递归遍历机构、用户、店面是否需要初始化下载，
	 * 如果type+1>3,代表已遍历完成，则进入initFinsh()
	 * 
	 * @param type
	 */
	private GetOrg getOrg = null;
	private int orgType = 1;
	private void getOrg(int type){
		getOrg = new GetOrg(context,getOrgResponseListener);
		if (type == Func.ORG_OPTION) {
			getOrg.getOrg();
		} else if (type == Func.ORG_USER) {
			getOrg.getUser();

		} else if (type == Func.ORG_STORE) {
			getOrg.getStore();
		} else if(type > Func.ORG_STORE) {
			initFinsh();
		}else{
			orgType = type;
			getOrg(orgType+1);//递归
		}
		
	}
	
	private void initFinsh(){
		if (syncDialog!=null && syncDialog.isShowing()) {
			syncDialog.dismiss();
		}
//		if (alertDialog!=null&&alertDialog.isShowing()) {
//			alertDialog.dismiss();;
//		}
		ToastOrder.makeText(context, R.string.visit_data4, ToastOrder.LENGTH_SHORT).show();
		//尝试删除多余的push
		clearPush(Constants.URL_PUSH_CLEAN_ALL);
		
		//发送广播更新数据
		Intent intent = new Intent();
		intent.setAction(Constants.BROADCAST_ACTION_STYLE);
		context.sendBroadcast(intent);
	}
	
	private GetOrg.ResponseListener getOrgResponseListener = new GetOrg.ResponseListener(){

		@Override
		public void onSuccess(int type, String result) {
			orgType = type;
			try {
				receiveOrgReq(result);
			} catch (JSONException e) {			
				e.printStackTrace();
				getOrg(orgType+1);//递归
			}
		}

		@Override
		public void onFailure(int type) {
			orgType = type;
			getOrg(orgType+1);//递归
			
		}
		
	};
	
	/**
	 * 获取组织机构和机构店面信息 并开始解析信息
	 * @param result json格式的文本信息
	 * @throws JSONException 
	 * @exception 捕获解析异常，提示用户初始化失败
	 */
	public void receiveOrgReq(String result) throws JSONException{
		JSONObject jsonObject = new JSONObject(result);
		String resultcode = jsonObject.getString(Constants.RESULT_CODE);
		if(resultcode.equalsIgnoreCase(Constants.RESULT_CODE_SUCCESS)){
			JLog.d(TAG, result);
			//解析返回的数据
			new initORGAnsyncTask().execute(result);
		}else{//如果不是返回"0000"表示初始化失败
			getOrg(orgType+1);//递归
		}
	}
	
	/**
	 * 解析机构、店面、用户
	 *
	 */
	private class initORGAnsyncTask extends AsyncTask<String,Integer, Boolean> {
		
		@Override
		protected Boolean doInBackground(String... params) {
			boolean flag = true;
			String json = params[0];//获取要解析的json数据
			try {
				//开始解析
				new OrgParser(context).batchParseByType(json,orgType);
			} catch (Exception e) {
				//如果有异常该变标识状态
				flag = false;
				e.printStackTrace();
			}
			isContinue(flag);
			return flag;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			getOrg(orgType+1); //递归
		}
		
		private void isContinue(Boolean result){
			//4.1版本以上
			if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN){
				
			}else{
					Message message = mHandler.obtainMessage();
					message.obj = result;
					message.sendToTarget();
			}
		}
		
		private Handler mHandler = new  Handler(){
			public void handleMessage(Message msg) {
				boolean flag = (Boolean) msg.obj;
				getOrg(orgType+1); //递归			
				};
		};
	}
	
	
}
