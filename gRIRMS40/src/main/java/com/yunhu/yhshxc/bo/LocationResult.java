package com.yunhu.yhshxc.bo;

public class LocationResult {
	
	private Double Longitude;//经度
	private Double Latitude;//纬度
	private String address;//地址
	private String lcationTime;//定位时间
	private String locType;//定位方式
	private int posType; //500是图吧
	private float radius;//精确度
	private boolean status;//定位是否成功 true成功 false不成功
	private boolean isInAcc;//是否在精度范围内
	
	private int type;
	private int startType;
	private boolean isZhudong;//是否是主动定位，true 是，false 被动定位
	
	
	
	
	public boolean isZhudong() {
		return isZhudong;
	}
	public void setZhudong(boolean isZhudong) {
		this.isZhudong = isZhudong;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getStartType() {
		return startType;
	}
	public void setStartType(int startType) {
		this.startType = startType;
	}
	public boolean isInAcc() {
		return isInAcc;
	}
	public void setInAcc(boolean isInAcc) {
		this.isInAcc = isInAcc;
	}
	public Double getLongitude() {
		return Longitude;
	}
	public void setLongitude(Double longitude) {
		Longitude = longitude;
	}
	public Double getLatitude() {
		return Latitude;
	}
	public void setLatitude(Double latitude) {
		Latitude = latitude;
	}
	
	public String getLocType() {
		return locType;
	}
	public void setLocType(String locType) {
		this.locType = locType;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getLcationTime() {
		return lcationTime;
	}
	public void setLcationTime(String lcationTime) {
		this.lcationTime = lcationTime;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public float getRadius() {
		return radius;
	}
	public void setRadius(float radius) {
		this.radius = radius;
	}
	public int getPosType() {
		return posType;
	}
	public void setPosType(int posType) {
		this.posType = posType;
	}
	
	
}
