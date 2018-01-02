package com.yunhu.yhshxc.activity.questionnaire.bo;

/**
 * 调查结果
 * @author jishen
 *
 */
public class Findings {
	private int id;
	private int resultId;//结果Id

	private int questionnaireId;//问卷ID
	private int investigatorId;//调查员用户ID
	private String investigatorName;//调查员姓名
	private String investigatorPhoneno;//调查员手机号
	private String investigatorOrgPath;
	private String startDate;//调查开始时间
	private String endDate;//调查结束时间
	private String adressId;//调查地点ID
	private String adress;//地点
	private String lonLat;//经纬度 经度$纬度
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
	public int getResultId() {
		return resultId;
	}
	public void setResultId(int resultId) {
		this.resultId = resultId;
	}
	public int getQuestionnaireId() {
		return questionnaireId;
	}
	public void setQuestionnaireId(int questionnaireId) {
		this.questionnaireId = questionnaireId;
	}
	public int getInvestigatorId() {
		return investigatorId;
	}
	public void setInvestigatorId(int investigatorId) {
		this.investigatorId = investigatorId;
	}
	public String getInvestigatorName() {
		return investigatorName;
	}
	public void setInvestigatorName(String investigatorName) {
		this.investigatorName = investigatorName;
	}
	public String getInvestigatorPhoneno() {
		return investigatorPhoneno;
	}
	public void setInvestigatorPhoneno(String investigatorPhoneno) {
		this.investigatorPhoneno = investigatorPhoneno;
	}
	public String getInvestigatorOrgPath() {
		return investigatorOrgPath;
	}
	public void setInvestigatorOrgPath(String investigatorOrgPath) {
		this.investigatorOrgPath = investigatorOrgPath;
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
	public String getAdressId() {
		return adressId;
	}
	public void setAdressId(String adressId) {
		this.adressId = adressId;
	}
	public String getAdress() {
		return adress;
	}
	public void setAdress(String adress) {
		this.adress = adress;
	}
	public String getLonLat() {
		return lonLat;
	}
	public void setLonLat(String lonLat) {
		this.lonLat = lonLat;
	}
	
	
}
