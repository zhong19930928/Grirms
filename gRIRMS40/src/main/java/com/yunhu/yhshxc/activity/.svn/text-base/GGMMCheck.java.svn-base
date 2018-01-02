package com.yunhu.yhshxc.activity;

import gcg.org.debug.JLog;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.loopj.android.http.RequestParams;

public class GGMMCheck {
	private Context context;
	private Dialog loadDialog;
	private GGMMCheckListener checkListener;
	public GGMMCheck(Context context,GGMMCheckListener listener) {
		this.context = context;
		this.checkListener = listener;
		loadDialog = new MyProgressDialog(context,R.style.CustomProgressDialog,PublicUtils.getResourceString(context,R.string.reloading_));

	}
	
	/**
	 * 是否要验证GGMM表格
	 * @return true要验证 false不验证
	 */
	public boolean isCheckGGMM(int checkId){
		boolean flag = false;
		if (checkId == 2002863 || checkId == 2002864 || checkId == 1023760) {
			flag = true;
		}
		return flag;
	}
	
	public void checkGGMM(int checkId,String value){
		try {
			String url = UrlInfo.getUrlCheckGGMM(context);
			RequestParams params = new RequestParams();
			params.put("moduleId", String.valueOf(checkId));
			params.put("info", value);
			GcgHttpClient.getInstance(context).post(url, params, new HttpResponseListener(){
				@Override
				public void onStart() {
					if (loadDialog !=null && !loadDialog.isShowing()) {
						loadDialog.show();
					}
				}
				
				@Override
				public void onSuccess(int statusCode, String content) {
					JLog.d("jishen", content);
					try {
						JSONObject obj = new JSONObject(content);
						String code = obj.getString("resultcode");
						String contentStr = null;
						if (obj.has("content")) {
							contentStr = obj.getString("content");
						}
						if ("0000".equals(code)) {
							if (TextUtils.isEmpty(contentStr)) {
								checkListener.GGMMCheckResult(true, contentStr);
							}else{
								checkListener.GGMMCheckResult(false, contentStr);
							}
						}else{
							checkListener.GGMMCheckResult(false, PublicUtils.getResourceString(context,R.string.data_ex));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				@Override
				public void onFailure(Throwable error, String content) {
					checkListener.GGMMCheckResult(false,PublicUtils.getResourceString(context,R.string.retry_net_exception));

				}
				
				@Override
				public void onFinish() {
					if (loadDialog!=null && loadDialog.isShowing()) {
						loadDialog.dismiss();
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	interface GGMMCheckListener{
		public void GGMMCheckResult(boolean isPass,String tipMessage);
	}
}
