package com.yunhu.yhshxc.activity.questionnaire.bo;
/**
 * 调查结果详细
 * @author jishen
 *
 */
public class FindIngDetail {
	private int id;//每份结果详细ID
	private int findIngId;//每份结果ID
	private int questionId;//问题ID
	private String choiceOptions;//选择的选项ID 多选时的格式 101,102,103
	private String fillOptions;//开放填写内容 （填空题 单选(其他)，多选(其他) 时使用）
	private String attachment;//附件URL (开放回答时可能有多个图片语音附件 格式: url1,url2,url3)
	
	
	
	private int optionsId;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getFindIngId() {
		return findIngId;
	}
	public void setFindIngId(int findIngId) {
		this.findIngId = findIngId;
	}
	public int getQuestionId() {
		return questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	public String getChoiceOptions() {
		return choiceOptions;
	}
	public void setChoiceOptions(String choiceOptions) {
		this.choiceOptions = choiceOptions;
	}
	public String getFillOptions() {
		return fillOptions;
	}
	public void setFillOptions(String fillOptions) {
		this.fillOptions = fillOptions;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	public int getOptionsId() {
		return optionsId;
	}
	public void setOptionsId(int optionsId) {
		this.optionsId = optionsId;
	}
	
	
}
