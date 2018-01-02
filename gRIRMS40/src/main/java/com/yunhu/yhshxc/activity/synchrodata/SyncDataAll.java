package com.yunhu.yhshxc.activity.synchrodata;

import gcg.org.debug.JLog;

import java.util.List;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.parser.OrgParser;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.widget.ToastOrder;

public class SyncDataAll{
	private Context context;
	private List<SyncItem> syncItemList = null;
	
	public SyncDataAll(Context mContext) {
		this.context = mContext;
	}
	
	
	
	public List<SyncItem> getSyncItemList() {
		return syncItemList;
	}

	public void setSyncItemList(List<SyncItem> syncItemList) {
		this.syncItemList = syncItemList;
	}



	private Dialog syncDialog;
	private int currentIndex;
	public void syncData(){
		currentIndex = 0;
		sync(currentIndex);
	}
	
	private void sync(int index){
		currentIndex = index;
		JLog.d("jishen", currentIndex+"");
		if (syncItemList!=null && !syncItemList.isEmpty() && index<syncItemList.size()) {
			final SyncItem item = syncItemList.get(index);
			final String url = item.getSyncUrl();
			syncDialog = new MyProgressDialog(context,R.style.CustomProgressDialog,PublicUtils.getResourceString(context,R.string.visit_data2)+item.getName()+PublicUtils.getResourceString(context,R.string.visit_data)+"...");
			GcgHttpClient.getInstance(context).get(url, null, new HttpResponseListener() {
				
				@Override
				public void onSuccess(int statusCode, String content) {
					JLog.d("jishen", "更新数据："+url+"\n"+content);
					new SyncParseTask(item).execute(content);
				}
				
				@Override
				public void onStart() {
					if (syncDialog!=null && !syncDialog.isShowing()) {
						syncDialog.show();
					}
				}
				
				@Override
				public void onFinish() {

				}
				
				@Override
				public void onFailure(Throwable error, String content) {
					if (syncDialog!=null && syncDialog.isShowing()) {
						syncDialog.dismiss();
					}
					ToastOrder.makeText(context, R.string.visit_data3, ToastOrder.LENGTH_SHORT).show();
				}
			});
		
		}else{
			ToastOrder.makeText(context,R.string.visit_data4, ToastOrder.LENGTH_SHORT).show();
		}
	}
	
	private class SyncParseTask extends AsyncTask<String,Integer, Boolean>{

		private SyncItem syncItem = null;
		public SyncParseTask(SyncItem syncItem) {
			this.syncItem = syncItem;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
//			clearCache("全部更新数据"); //清除缓存数据，以便重新初始化
		}
		
		@Override
		protected Boolean doInBackground(String... params) {
			String param = params[0];
			try {
				JSONObject jsonObject = new JSONObject(param);
				String resultcode = jsonObject.getString(Constants.RESULT_CODE);
				//"0000"表示返回成功"0001"表示返回失败"0002"表示后台没检测到用户
				if(resultcode.equalsIgnoreCase(Constants.RESULT_CODE_SUCCESS)){
					if (syncItem!=null) {
						if (syncItem.getType() == syncItem.TYPE_SPINNER) {
							new SyncParse(context).paserJson(param);
						}else{
							new OrgParser(context).parseAll(param);
						}
					}
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
			if (result) {
//				Toast.makeText(context, syncItem.getName()+"更新完毕", Toast.LENGTH_SHORT).show();
			}else{
				ToastOrder.makeText(context, syncItem.getName()+ PublicUtils.getResourceString(context,R.string.visit_data1), ToastOrder.LENGTH_SHORT).show();
			}
			currentIndex++;
			sync(currentIndex);
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
				if (flag) {
//					Toast.makeText(context, syncItem.getName()+"更新完毕", Toast.LENGTH_SHORT).show();
				}else{
					ToastOrder.makeText(context, syncItem.getName()+PublicUtils.getResourceString(context,R.string.visit_data1), ToastOrder.LENGTH_SHORT).show();
				}
				currentIndex++;
				sync(currentIndex);
			};
		};
	}
	
}
