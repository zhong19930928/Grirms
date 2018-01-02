package com.yunhu.yhshxc.activity.onlineExamination.bo;

/**
 * 答题选项
 * @author jishen
 *
 */
public class EaxmAnswerOptions {
	private int id;
	private int optionsId;//选项ID
	private int questionId;//试题ID
	private String options;//选项内容
	private String isRight;//答案是否正确 1错误 2正确
	private int scores;//分值
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public String getIsRight() {
		return isRight;
	}
	public void setIsRight(String isRight) {
		this.isRight = isRight;
	}
	public int getScores() {
		return scores;
	}
	public void setScores(int scores) {
		this.scores = scores;
	}
	
	
	
}
