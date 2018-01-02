package com.yunhu.yhshxc.activity.carSales.bo;

public class CarSalesProductConf {
	public static final int TYPE_PRODUCT = 1;//产品
	public static final int TYPE_UNIT = 2;//计量单位
	
	private Integer id;
	private String dictTable; //字典表
	private String dictDataId;//字典表中需要操作的字段
	private String dictCols; //所有字段
	private String next;//产品的下一层
	private String name;//产品名称
	private int type;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDictTable() {
		return dictTable;
	}
	public void setDictTable(String dictTable) {
		this.dictTable = dictTable;
	}
	public String getDictDataId() {
		return dictDataId;
	}
	public void setDictDataId(String dictDataId) {
		this.dictDataId = dictDataId;
	}
	public String getDictCols() {
		return dictCols;
	}
	public void setDictCols(String dictCols) {
		this.dictCols = dictCols;
	}
	public String getNext() {
		return next;
	}
	public void setNext(String next) {
		this.next = next;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	
}
