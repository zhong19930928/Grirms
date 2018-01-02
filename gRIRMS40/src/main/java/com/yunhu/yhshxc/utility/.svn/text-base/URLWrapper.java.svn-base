package com.yunhu.yhshxc.utility;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.text.TextUtils;
import android.util.Log;

public class URLWrapper {
	private String baseURL;
	private Map<String, String> params;

	public URLWrapper(String url) {
		this.baseURL = url;
		this.params = new HashMap<String, String>();
	}

	public void addParameter(String name, String value) {
		this.params.put(name, value);
	}
	
	public void addParameter(String name, int value) {
		this.params.put(name, String.valueOf(value));
	}

	public String getRequestURL() {
//		params.put("test", "gcg");
//		params.put("phoneno", "18910901892");
		if (params.isEmpty())
			return this.baseURL;
		else {
			StringBuffer result = new StringBuffer(baseURL);
			if(this.baseURL.contains("?")){
				if(this.baseURL.lastIndexOf("?") == -1){
					result.append("&");
				}
			}else{
				result.append("?");
			}
			for(Entry<String,String> entry : params.entrySet()){
				String key = entry.getKey();
				String value = entry.getValue();
				if (!TextUtils.isEmpty(key)){
					result.append(key.trim()).append("=").append(value).append("&");
				}
			}
			Log.i("HTTP", "baseUrl==>" + result);
			return result.substring(0, result.length() - 1);
		}
	}

}
