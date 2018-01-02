package com.yunhu.yhshxc.bo;
/**
 * 手机端报表管理者统计的个人拜访信息
 * @author qingli
 *
 */
public class PhoneReportVistTopTen {
	/**
	 * 用户名称
	 */
	private String name;
	/**
	 * 访店人数
	 */
	private String vsCnt;
	/**
	 * 下达访店总数
	 */
	private String xdvpCnt;
	/**
	 * 已访店数
	 */
	private String vpCnt;
	
	/**
	 * 
	 * 个人报表中的完成度
	 */
	private String average;
	
	public String getAverage() {
		return average;
	}
	public void setAverage(String average) {
		this.average = average;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVsCnt() {
		return vsCnt;
	}
	public void setVsCnt(String vsCnt) {
		this.vsCnt = vsCnt;
	}
	public String getXdvpCnt() {
		return xdvpCnt;
	}
	public void setXdvpCnt(String xdvpCnt) {
		this.xdvpCnt = xdvpCnt;
	}
	public String getVpCnt() {
		return vpCnt;
	}
	public void setVpCnt(String vpCnt) {
		this.vpCnt = vpCnt;
	}
	
	
}
