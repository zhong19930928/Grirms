package com.yunhu.yhshxc.activity.carSales.bo;
/**
 * 收取欠款
 * @author xuelinlin
 *
 */
public class Arrears {
	private int id;
	private double arrearsAmount;//欠款
	private double skAmount;//收款
	private int isOver;//是否结清
	private double hisAmount;//历史收款
	private String time;
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getArrearsAmount() {
		return arrearsAmount;
	}
	public void setArrearsAmount(double arrearsAmount) {
		this.arrearsAmount = arrearsAmount;
	}
	public double getSkAmount() {
		return skAmount;
	}
	public void setSkAmount(double skAmount) {
		this.skAmount = skAmount;
	}
	public int getIsOver() {
		return isOver;
	}
	public void setIsOver(int isOver) {
		this.isOver = isOver;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public double getHisAmount() {
		return hisAmount;
	}
	public void setHisAmount(double hisAmount) {
		this.hisAmount = hisAmount;
	}
	@Override
	public String toString() {
		return "Arrears [id=" + id + ", arrearsAmount=" + arrearsAmount
				+ ", skAmount=" + skAmount + ", isOver=" + isOver
				+ ", hisAmount=" + hisAmount + ", time=" + time + ", name="
				+ name + "]";
	}
	
}
