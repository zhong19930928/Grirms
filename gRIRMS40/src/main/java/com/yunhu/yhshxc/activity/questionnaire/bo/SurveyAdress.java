package com.yunhu.yhshxc.activity.questionnaire.bo;

/**
 * 调查地点
 * 每次问卷单独设定本次调查要求的地方
 * @author jishen
 *
 */
public class SurveyAdress {
	private int id;
	private int surveyAdressId;//调查地点ID
	private int questionnaireId;//问卷ID
	private String adress;//地点
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSurveyAdressId() {
		return surveyAdressId;
	}
	public void setSurveyAdressId(int surveyAdressId) {
		this.surveyAdressId = surveyAdressId;
	}
	public int getQuestionnaireId() {
		return questionnaireId;
	}
	public void setQuestionnaireId(int questionnaireId) {
		this.questionnaireId = questionnaireId;
	}
	public String getAdress() {
		return adress;
	}
	public void setAdress(String adress) {
		this.adress = adress;
	}
	
	
}
