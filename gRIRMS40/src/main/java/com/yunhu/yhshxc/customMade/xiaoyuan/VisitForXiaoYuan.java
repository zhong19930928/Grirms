package com.yunhu.yhshxc.customMade.xiaoyuan;

import gcg.org.debug.JLog;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;

import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.loopj.android.http.RequestParams;

public class VisitForXiaoYuan {

	private  final String TAG = "VisitForXiaoYuan";
	private static Context mContext;
	private static VisitForXiaoYuan VXY = null;

	public static VisitForXiaoYuan getInstance(Context context) {
		if (VXY == null) {
			VXY = new VisitForXiaoYuan();
			mContext = context.getApplicationContext();
		}
		return VXY;
	}
	
	public void search(final Dialog dialog,int storeId,final VisitForXiaoYuanListener mListener){
		String phoneNo = PublicUtils.receivePhoneNO(mContext);
		JSONObject obj = new JSONObject();
		try {
			obj.put("1", String.valueOf(storeId));
		} catch (Exception e) {
			JLog.e(e);
		}
		String sqlUrl = UrlInfo.baseUrl(mContext)+"distributionSqlDataInfo.do?ctrlType=1"+"&defaultData=1001165"+"&phoneno="+phoneNo+"&sqlparam="+obj.toString();
		JLog.d(TAG, "url:"+sqlUrl);
		
		RequestParams params = new RequestParams();
		params.put("ctrlType", "1");
		params.put("defaultData", "1001165");
		params.put("phoneno", phoneNo);
		params.put("sqlparam", obj.toString());
		
		GcgHttpClient.getInstance(mContext).post(UrlInfo.baseUrl(mContext)+"distributionSqlDataInfo.do", params, new HttpResponseListener(){
			@Override
			public void onStart() {
				if (dialog!=null && !dialog.isShowing()) {
					dialog.show();
				}
			}
			
			
			@Override
			public void onFinish() {
				if (dialog!=null && dialog.isShowing()) {
					dialog.dismiss();
				}
				if (mListener!=null) {
					mListener.searchResult();
				}
			}


			@Override
			public void onSuccess(int statusCode, String content) {
				JLog.d(TAG, "sqldataContent:"+content);
				String defaultSql="";//sql查询到的值
				try {
					JSONObject jsonObject = new JSONObject(content);
					String resultcode = jsonObject.getString(Constants.RESULT_CODE);
					if(resultcode!=null && resultcode.equals(Constants.RESULT_CODE_SUCCESS)){
						defaultSql=jsonObject.getString("sqldata");
					}
				} catch (JSONException e) {
					JLog.e(e);
				}
				SharedPreferencesUtilForXiaoYuan.getInstance(mContext).saveXYStoreProperty(defaultSql);
			}


			@Override
			public void onFailure(Throwable error, String content) {
				
			}
		});
	
	}
	
	public boolean isUse(){
		if(SharedPreferencesUtil.getInstance(mContext).getCompanyId() == 10244){
			return true;
		}else{
			return false;
		}
	}
	
	public List<Func> getFuncList(List<Func> list){
		String storeProperty = SharedPreferencesUtilForXiaoYuan.getInstance(mContext).getXYStoreProperty();
		if ("3".equals(storeProperty)) {
			List<Func> funcList = new ArrayList<Func>();
			for (int i = 0; i < list.size(); i++) {
				Func func = list.get(i);
				if (2000959 == func.getFuncId()) {
					continue;
				}else{
					funcList.add(func);
				}
			}
			return funcList;
		}else if("2".equals(storeProperty)){
			List<Func> funcList = new ArrayList<Func>();
			for (int i = 0; i < list.size(); i++) {
				Func func = list.get(i);
				if (2000957 == func.getFuncId()) {
					continue;
				}else{
					funcList.add(func);
				}
			}
			return funcList;
		}else{
			return list;
		}
	}

}
