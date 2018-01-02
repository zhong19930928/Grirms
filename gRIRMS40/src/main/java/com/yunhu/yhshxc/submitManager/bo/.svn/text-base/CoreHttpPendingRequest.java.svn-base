package com.yunhu.yhshxc.submitManager.bo;

import gcg.org.debug.JLog;

import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONObject;

/**
 * 此类是缓存数据PendingRequest类
 * 用来存储提交数据的
 * @author jishen
 *
 */
public class CoreHttpPendingRequest {

	private int reqID = 0;//请求ID
	private int execType = 0;//请求类型
	private boolean imageType;//是否是照片类型 true 是 false 不是
	private String url = null;//请求URL
	private HashMap<String, String> params = null; //要提交的参数
	private HashMap<String, String> files = null;//要提交的图片

	public CoreHttpPendingRequest(int reqID, int execType, boolean imageType, String url,HashMap<String, String> params, HashMap<String, String> files) {
		this.reqID = reqID;
		this.execType = execType;
		this.url = url;
		this.params = params;
		this.files = files;
		this.imageType = imageType;
	}
	
	/**
	 * 构造函数根据json 生成PendingRequest对象
	 * @param jsonString
	 */
	public CoreHttpPendingRequest(String jsonString) {
		JSONObject data;
		try {
			data = new JSONObject(jsonString);
			this.reqID = data.getInt("REQID");
			this.url = data.getString("URL");
			this.imageType = data.getBoolean("REQTYPE");
			this.execType = data.getInt("ACTION");
			if (data.has("DATA")) {//提交的数据
				this.params = new HashMap<String, String>();
				JSONObject content = data.getJSONObject("DATA");
				Iterator<String> contentIt = content.keys();
				while (contentIt.hasNext()) {
					String currKey = contentIt.next();
					String currVal = content.getString(currKey);
					this.params.put(currKey, currVal);
				}
			}
			if (data.has("files")) {//提交的文件
				this.files = new HashMap<String, String>();
				JSONObject content = data.getJSONObject("files");
				Iterator<String> contentIt = content.keys();
				while (contentIt.hasNext()) {
					String currKey = contentIt.next();
					String currVal = content.getString(currKey);
					this.files.put(currKey, currVal);
				}
			}
		} catch (Exception e) {
			JLog.e(e);
		}
	}

	/**
	 * 将要提交的缓存数据转化为json格式的数据
	 * @return
	 */
	public String toJsonString() {
		String result = "";

		JSONObject data = new JSONObject();
		try {
			data.put("URL", this.url);//提交url
			data.put("REQID", this.reqID);//请求ID
			data.put("ACTION", this.execType);//请求类型
			data.put("REQTYPE", this.imageType);//是否是照片类型
			if (this.params != null) {//要提交的数据
				JSONObject content = new JSONObject();
				Iterator<String> paramIt = this.params.keySet().iterator();
				while (paramIt.hasNext()) {
					String currKey = paramIt.next();
					String currVal = this.params.get(currKey);
					content.put(currKey, currVal);
				}
				data.put("DATA", content);
			}
			if (this.files != null) {//要提交的文件
				JSONObject content = new JSONObject();
				Iterator<String> paramIt = this.files.keySet().iterator();
				while (paramIt.hasNext()) {
					String currKey = paramIt.next();
					String currVal = this.files.get(currKey);
					content.put(currKey, currVal);
				}
				data.put("files", content);
			}
		} catch (Exception e) {
			JLog.e(e);
		}
		result = data.toString();
		return result;
	}

	public int getRequestID() {
		return this.reqID;
	}

	public String getUrl() {
		return url;
	}

	public HashMap<String, String> getParams() {
		return params;
	}

	public int getExecType() {
		return execType;
	}

	public boolean isImageType() {
		return imageType;
	}

	public HashMap<String, String> getFiles() {
		return files;
	}
	
	
}
