package com.yunhu.yhshxc.activity.questionnaire.bo;

/**
 * 答题选项
 * @author jishen
 *
 */
public class AnswerOptions {
	private int id;
	private int optionsId;//选项ID
	private int questionId;//调查问卷Id
	private String options;//选项内容
	private String optionsRemarks;//选项备注
	private int problemId;//问题ID
	private int isSave;//0 正常状态 1 保存
	
	
	public int getIsSave() {
		return isSave;
	}
	public void setIsSave(int isSave) {
		this.isSave = isSave;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getProblemId() {
		return problemId;
	}
	public void setProblemId(int problemId) {
		this.problemId = problemId;
	}
	public int getOptionsId() {
		return optionsId;
	}
	public void setOptionsId(int optionsId) {
		this.optionsId = optionsId;
	}
	public int getQuestionId() {
		return questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	public String getOptions() {
		return options;
	}
	public void setOptions(String options) {
		this.options = options;
	}
	public String getOptionsRemarks() {
		return optionsRemarks;
	}
	public void setOptionsRemarks(String optionsRemarks) {
		this.optionsRemarks = optionsRemarks;
	}
	
	
	
}
