package com.yunhu.yhshxc.bo;

import java.util.List;
import java.util.Map;

public class ReplenishSearchResult {

	private String targetId = null;
	private List<Map<String,String>> resultList = null;
	private String searchcols = null;
	private String searchredate = null;
	private int total = 0;
	private int cacherows = 0;
	
	public String getTargetId() {
		return targetId;
	}
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
	public List<Map<String, String>> getResultList() {
		return resultList;
	}
	public void setResultList(List<Map<String, String>> resultList) {
		this.resultList = resultList;
	}
	public String getSearchcols() {
		return searchcols;
	}
	public void setSearchcols(String searchcols) {
		this.searchcols = searchcols;
	}
	public String getSearchredate() {
		return searchredate;
	}
	public void setSearchredate(String searchredate) {
		this.searchredate = searchredate;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getCacherows() {
		return cacherows;
	}
	public void setCacherows(int cacherows) {
		this.cacherows = cacherows;
	}
	
	
}
