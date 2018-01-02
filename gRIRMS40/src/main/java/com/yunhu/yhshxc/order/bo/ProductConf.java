package com.yunhu.yhshxc.order.bo;

/**
 * 产品配置
 * @author jishen
 *
 */
public class ProductConf {
	private Integer id;
	private String dictTable; //字典表
	private String dictDataId;//字典表中需要操作的字段
	private String dictCols; //所有字段
	private String next;//产品的下一层
	private String name;//产品名称
	private String dictOrderBy;//字典表排序列
	private String dictIsAsc;//字典表排序是否是生序
	
	public String getDictOrderBy() {
		return dictOrderBy;
	}
	public void setDictOrderBy(String dictOrderBy) {
		this.dictOrderBy = dictOrderBy;
	}

	public String getDictIsAsc() {
		return dictIsAsc;
	}
	public void setDictIsAsc(String dictIsAsc) {
		this.dictIsAsc = dictIsAsc;
	}
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
	public String getNext() {
		return next;
	}
	public String getDictCols() {
		return dictCols;
	}
	public void setDictCols(String dictCols) {
		this.dictCols = dictCols;
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
	
}
