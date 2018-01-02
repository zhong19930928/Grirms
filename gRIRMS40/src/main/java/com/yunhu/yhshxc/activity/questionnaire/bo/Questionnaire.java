package com.yunhu.yhshxc.activity.questionnaire.bo;

/**
 * 调查问卷的整体定义
 * @author jishen
 *
 */
public class Questionnaire {
	private int id;
	private int questionId;//问卷调查Id
	private String name;//名称
	private String explain;//说明
	private String startDate;//开始日期
	private String endDate;//结束日期
	private int numbers;//调查份数
	private int questionnaireState;//问卷状态
	private int findingState;//调查结果状态
	private int count;//2016-Aug-11 已调查份数
	private int cycle; //  1：每天、2：每周、3：每月、4：每季度、5：每半年、6：每年   （ 空白或key不存在时，不限制）
	private int upCopies; // 根据cycle类型，查询出的已上报份数。  （无限制时 key不存在）
	public int getCycle() {
		return cycle;
	}
	public void setCycle(int cycle) {
		this.cycle = cycle;
	}
	public int getUpCopies() {
		return upCopies;
	}
	public void setUpCopies(int upCopies) {
		this.upCopies = upCopies;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getExplain() {
		return explain;
	}
	public void setExplain(String explain) {
		this.explain = explain;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public int getNumbers() {
		return numbers;
	}
	public void setNumbers(int numbers) {
		this.numbers = numbers;
	}
	public int getQuestionnaireState() {
		return questionnaireState;
	}
	public void setQuestionnaireState(int questionnaireState) {
		this.questionnaireState = questionnaireState;
	}
	public int getFindingState() {
		return findingState;
	}
	public void setFindingState(int findingState) {
		this.findingState = findingState;
	}
	
	public int getQuestionId() {
		return questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	
}
