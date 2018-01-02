package com.yunhu.yhshxc.wechat.bo;

/**
 * 调查和评审
 * 
 * @author jishen
 * 
 */
public class Survey {
	public static final int TYPE_1 = 1;// 单选
	public static final int TYPE_2 = 2;// 多选
	public static final int SURVEYTYPE_1 = 1;// 调查类
	public static final int SURVEYTYPE__2 = 2;// 评审类

	private int id;
	private int surveyId;// 选项ID
	private int topicId;// 话题ID
	private String explain;// 说明
	private String title;// 标题
	private int type;// 1单选 2多选
	private int surveyType;// 类型 1是调查 2是评审
	private String options;// 选项
	private int optionOrder;//调查类选项编号

	
	
	public int getOptionOrder() {
		return optionOrder;
	}

	public void setOptionOrder(int optionOrder) {
		this.optionOrder = optionOrder;
	}

	public int getSurveyType() {
		return surveyType;
	}

	public void setSurveyType(int surveyType) {
		this.surveyType = surveyType;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(int surveyId) {
		this.surveyId = surveyId;
	}

	public int getTopicId() {
		return topicId;
	}

	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
