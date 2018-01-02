package com.yunhu.yhshxc.submitManager.bo;

import java.util.HashMap;

public class PendingRequestVO {

	private int methodType;
	private String url;
	private HashMap<String, String> params;
	private HashMap<String, String> files;
	private String imagePath;
	private String title;
	private String content;
	private int type;
	
	public int getMethodType() {
		return methodType;
	}
	public void setMethodType(int methodType) {
		this.methodType = methodType;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public HashMap<String, String> getParams() {
		return params;
	}
	public void setParams(HashMap<String, String> params) {
		this.params = params;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public HashMap<String, String> getFiles() {
		return files;
	}
	public void setFiles(HashMap<String, String> files) {
		this.files = files;
	}
	
	public void addParams(String key,String value){
		if(params == null){
			params = new HashMap<String, String>();
		}
		params.put(key, value);
	}
	
	public void addFiles(String key,String value){
		//目前只能一个一个的提交
		files = new HashMap<String, String>();
		files.put(key, value);
	}
}
