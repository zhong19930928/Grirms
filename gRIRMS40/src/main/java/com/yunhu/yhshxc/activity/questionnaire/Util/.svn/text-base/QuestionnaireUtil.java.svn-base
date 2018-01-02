package com.yunhu.yhshxc.activity.questionnaire.Util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.yunhu.yhshxc.activity.questionnaire.bo.AnswerOptions;
import com.yunhu.yhshxc.activity.questionnaire.bo.FindIngDetail;
import com.yunhu.yhshxc.activity.questionnaire.bo.Findings;
import com.yunhu.yhshxc.activity.questionnaire.bo.Question;
import com.yunhu.yhshxc.activity.questionnaire.bo.Questionnaire;
import com.yunhu.yhshxc.activity.questionnaire.bo.SurveyAdress;
import com.yunhu.yhshxc.activity.questionnaire.db.AnswerOptionsDB;
import com.yunhu.yhshxc.activity.questionnaire.db.FindIngDetailDB;
import com.yunhu.yhshxc.activity.questionnaire.db.FindingsDB;
import com.yunhu.yhshxc.activity.questionnaire.db.QuestionDB;
import com.yunhu.yhshxc.activity.questionnaire.db.QuestionnaireDB;
import com.yunhu.yhshxc.activity.questionnaire.db.SurveyAdressDB;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.PublicUtils;

public class QuestionnaireUtil {
	private Context context;
	private QuestionnaireDB questionnaireDB;
	private QuestionDB questionDB;
	private AnswerOptionsDB answerOptionsDB;
	private SurveyAdressDB surveyAdressDB;
	private FindingsDB findingsDB;
	private FindIngDetailDB findIngDetailDB;
	
	
	public QuestionnaireUtil(Context mContext) {
		this.context = mContext;
		questionnaireDB = new QuestionnaireDB(context);
		questionDB = new QuestionDB(context);
		answerOptionsDB = new AnswerOptionsDB(context);
		surveyAdressDB = new SurveyAdressDB(context);
		findingsDB = new FindingsDB(context);
		findIngDetailDB = new FindIngDetailDB(context);
	}
	
	
	// Questionnaire解析
		public List<Questionnaire> parserSearchListItem(JSONArray array)
				throws JSONException {
			List<Questionnaire> itemList = new ArrayList<Questionnaire>();
			if (array != null && array.length() > 0) {
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj = array.getJSONObject(i);
					Questionnaire questionnaire = new Questionnaire();

					if (PublicUtils.isValid(obj, "qId")) {
						questionnaire.setQuestionId(obj.getInt("qId"));
					}
					
					if (PublicUtils.isValid(obj, "qTitle")) {
						questionnaire.setName(obj.getString("qTitle"));
					}
					
					if (PublicUtils.isValid(obj, "qDesc")) {
						questionnaire.setExplain(obj.getString("qDesc"));
					}
					
					if (PublicUtils.isValid(obj, "qStart")) {
						questionnaire.setStartDate(obj.getString("qStart"));
					}
					
					if(PublicUtils.isValid(obj, "qEnd")){
						questionnaire.setEndDate(obj.getString("qEnd"));
					}
					
					if(PublicUtils.isValid(obj, "qId")){
						Questionnaire q = questionnaireDB.findQuestionnaireById(questionnaire.getQuestionId());
						if(q != null){
							questionnaireDB.updateQuestionnaireResult(questionnaire);
						}else{
							questionnaireDB.insertQuestionnaire(questionnaire);
						}
					}

					itemList.add(questionnaire);
				}
			}

			return itemList;
		}
		
		
		// 解析结果外部列表 2016-Aug－11
				public List<Questionnaire> parserQuestionItem(JSONArray array)
						throws JSONException {
					List<Questionnaire> itemList = new ArrayList<Questionnaire>();
					if (array != null && array.length() > 0) {
						for (int i = 0; i < array.length(); i++) {
							JSONObject obj = array.getJSONObject(i);
							Questionnaire questionnaire = new Questionnaire();

							if (PublicUtils.isValid(obj, "qId")) {
								questionnaire.setQuestionId(obj.getInt("qId"));
							}
							
							if (PublicUtils.isValid(obj, "qTitle")) {
								questionnaire.setName(obj.getString("qTitle"));
							}
							
							if (PublicUtils.isValid(obj, "qDesc")) {
								questionnaire.setExplain(obj.getString("qDesc"));
							}
							if (PublicUtils.isValid(obj, "qCnt")) {
								questionnaire.setCount(obj.getInt("qCnt"));
							}
							itemList.add(questionnaire);
						}
					}

					return itemList;
				}
		
		
				
		//提交问卷调查
		public String submitQuestionJson(Questionnaire questionnaire ,SurveyAdress selectAddress,List<FindIngDetail> findIngDetails ,String startTime,Double Longitude ,Double Latitude,String address) throws JSONException{
			String json = "";
			JSONObject qObj = new JSONObject();
			
			qObj.put("questionId", questionnaire.getQuestionId());
			qObj.put("msgKey", PublicUtils.chatMsgKey(context));//唯一标识
			qObj.put("startTime", startTime);
			qObj.put("endTime", DateUtil.getCurDateTime());
			if(selectAddress!=null){
				
				qObj.put("addressId",selectAddress.getSurveyAdressId() );
				qObj.put("address", selectAddress.getAdress());
			}
//			qObj.put("address", selectAddress);
			if(!TextUtils.isEmpty(Longitude + "")){
				qObj.put("locLon", Longitude + "");//经度
			}
			if(!TextUtils.isEmpty(Latitude + "")){
				qObj.put("locLat", Latitude + "");//纬度
			}
			if(!TextUtils.isEmpty(address)){
				qObj.put("locAddress", address);//定位地址
			}
			
			qObj.put("locParams", "");//定位参数
			
			
			JSONArray detailArr = new JSONArray();
			
			String msgId = PublicUtils.chatMsgKey(context);
			for(int i= 0 ;i < findIngDetails.size();i++){
				FindIngDetail findIngDetail = findIngDetails.get(i);
				JSONObject detailObj = new JSONObject();
//				String[] oStr = findIngDetail.getChoiceOptions().split(",");
//				for(int j = 0;j < oStr.length;j++){
//					detailObj.put("optionId",oStr[j] );//选项Id
//					Question question = questionDB.findQuestionById(findIngDetail.getQuestionId());
//					detailObj.put("chapterId", question.getChapterId());//章节Id
//					detailObj.put("problemId", findIngDetail.getQuestionId());//问题Id
//					detailObj.put("content", "");//单选、多选其他等填写内容
//					detailObj.put("msgId",  msgId);//唯一标识，对应附件
//					detailArr.put(detailObj);
//				}
				

				detailObj.put("optionId",findIngDetail.getChoiceOptions() );//选项Id
				Question question = questionDB.findQuestionById(findIngDetail.getQuestionId());
				detailObj.put("chapterId", question.getChapterId());//章节Id
				detailObj.put("problemId", findIngDetail.getQuestionId());//问题Id
				detailObj.put("content", findIngDetail.getFillOptions());//单选、多选其他等填写内容
				detailObj.put("msgId",  msgId);//唯一标识，对应附件
				detailArr.put(detailObj);
			}
			
			
			qObj.put("detail", detailArr);
			
			JSONArray fileArr = new JSONArray();
			JSONObject fileObj = new JSONObject();
			for(int i = 0;i < 0;i++){
				fileObj.put("msgId", msgId);
				fileObj.put("type", 1);//附件类型 1：附件 2：视频 3：图片 4：音频      
				fileObj.put("showName", 1);
				fileObj.put("size", "");
				fileObj.put("name", "");
				fileArr.put(fileObj);
			}
			qObj.put("annexFile", fileArr);
			
			json = qObj.toString();
			return json;
		}
		
		
		
		// Findings解析
		public List<Findings>  parserFindingsListItem(JSONArray array)
				throws JSONException {
			List<Findings> itemList = new ArrayList<Findings>();
			if (array != null && array.length() > 0) {
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj = array.getJSONObject(i);
					Findings findings = new Findings();
					
					if (PublicUtils.isValid(obj, "rId")) {
						findings.setResultId(obj.getInt("rId"));
					}
					
					if (PublicUtils.isValid(obj, "qId")) {
						findings.setQuestionnaireId(obj.getInt("qId"));
					}
					
					if (PublicUtils.isValid(obj, "uId")) {
						findings.setInvestigatorId(obj.getInt("uId"));
					}
					
					if (PublicUtils.isValid(obj, "uName")) {
						findings.setInvestigatorName(obj.getString("uName"));
					}
					
					if (PublicUtils.isValid(obj, "phone_no")) {
						findings.setInvestigatorPhoneno(obj.getString("phone_no"));
					}
					
					if (PublicUtils.isValid(obj, "address")) {
						findings.setAdress(obj.getString("address"));
					}
					
					
					if (PublicUtils.isValid(obj, "locAddress")) {
						findings.setLonLat(obj.getString("locAddress"));
					}
					
					
					if (PublicUtils.isValid(obj, "rStart")) {
						findings.setStartDate(obj.getString("rStart"));
					}
					
					if (PublicUtils.isValid(obj, "rEnd")) {
						findings.setEndDate(obj.getString("rEnd"));
					}
					
					if(PublicUtils.isValid(obj, "rId")){
						Findings f = findingsDB.findFindingsById(findings.getResultId());
						if(f != null){
							findingsDB.updateFindings(findings);
						}else{
							findingsDB.insertFindIngs(findings);
						}
					}
					
					itemList.add(findings);
					
				}
			}
			
			return itemList;
		}
		
		//Questionnaire解析
		public List<Questionnaire>  parserQuestionListItem(JSONArray array)
				throws JSONException {
			List<Questionnaire> itemList = new ArrayList<Questionnaire>();
			if (array != null && array.length() > 0) {
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj = array.getJSONObject(i);
					Questionnaire questionnaire = new Questionnaire();

					
					if (PublicUtils.isValid(obj, "id")) {
						questionnaire.setQuestionId(obj.getInt("id"));
					}
					
					if (PublicUtils.isValid(obj, "title")) {
						questionnaire.setName(obj.getString("title"));
					}
					
					if(PublicUtils.isValid(obj, "desc")){
						questionnaire.setExplain(obj.getString("desc"));
					}
					
					if(PublicUtils.isValid(obj, "startDate")){
						questionnaire.setStartDate(obj.getString("startDate"));
					}
					
					
					if(PublicUtils.isValid(obj, "endDate")){
						questionnaire.setEndDate(obj.getString("endDate"));
					}
					

					if(PublicUtils.isValid(obj, "numCopies")){
						questionnaire.setNumbers(obj.getInt("numCopies"));
					}
					
					if(PublicUtils.isValid(obj, "cycle")){
						questionnaire.setCycle(obj.getInt("cycle"));
					}
					
					if(PublicUtils.isValid(obj, "upCopies")){
						questionnaire.setUpCopies(obj.getInt("upCopies"));
					}

					if(PublicUtils.isValid(obj, "id")){
						Questionnaire q = questionnaireDB.findQuestionnaireById(questionnaire.getQuestionId());
						if(q != null){
							questionnaireDB.updateQuestionnaire(questionnaire);
						}else{
							questionnaireDB.insertQuestionnaire(questionnaire);
						}
					}
					
					itemList.add(questionnaire);
				}
			}

			return itemList;
		}
		
		
		
		//FindIngDetail解析
		public List<FindIngDetail>  parserFindIngDetailListItem(JSONArray array,boolean isClearDb)
				throws JSONException {
			if (isClearDb) {
				findIngDetailDB.clearFindIngDetail();
			}
		
			List<FindIngDetail> itemList = new ArrayList<FindIngDetail>();
			if (array != null && array.length() > 0) {
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj = array.getJSONObject(i);
					FindIngDetail findIngDetail = new FindIngDetail();

					
					if (PublicUtils.isValid(obj, "pId")) {
						findIngDetail.setQuestionId(obj.getInt("pId"));
					}
					
					if (PublicUtils.isValid(obj, "rId")) {
						findIngDetail.setFindIngId(obj.getInt("rId"));
					}
					
//					if (PublicUtils.isValid(obj, "oTitle")) {
//						findIngDetail.setChoiceOptions(obj.getString("oTitle"));
//					}
					
					if (PublicUtils.isValid(obj, "oId")) {
						findIngDetail.setChoiceOptions(obj.getString("oId"));
					}
					
					
					if(PublicUtils.isValid(obj, "rContent")){
						findIngDetail.setFillOptions(obj.getString("rContent"));
					}
					

					if(PublicUtils.isValid(obj, "pId") || PublicUtils.isValid(obj, "rId")){
//						FindIngDetail f = findIngDetailDB.findFindIngDetailByQId(findIngDetail.getQuestionId(),findIngDetail.getFindIngId());
//						if(f != null&&f.getChoiceOptions().equals(findIngDetail.getChoiceOptions())){
//							findIngDetailDB.updateFindIngDetail(findIngDetail);
//						}else{
							findIngDetailDB.insertFindIngDetail(findIngDetail);
//						}
					}
					
					itemList.add(findIngDetail);
				}
			}

			return itemList;
		}

				
		//SurveyAdress解析
		public List<SurveyAdress>  parserAddressListItem(JSONArray array)
				throws JSONException {
			List<SurveyAdress> itemList = new ArrayList<SurveyAdress>();
			if (array != null && array.length() > 0) {
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj = array.getJSONObject(i);
					SurveyAdress surveyAdress = new SurveyAdress();

					
					if (PublicUtils.isValid(obj, "id")) {
						surveyAdress.setSurveyAdressId(obj.getInt("id"));
					}
					
					if (PublicUtils.isValid(obj, "questionId")) {
						surveyAdress.setQuestionnaireId(obj.getInt("questionId"));
					}
					
					if (PublicUtils.isValid(obj, "address")) {
						surveyAdress.setAdress(obj.getString("address"));
					}
					
					if(PublicUtils.isValid(obj, "id")){
						SurveyAdress s = surveyAdressDB.findSurveyAdressById(surveyAdress.getSurveyAdressId());
						if(s != null){
							surveyAdressDB.updateSurveyAdress(surveyAdress);
						}else{
							surveyAdressDB.insertSurveyAdress(surveyAdress);
						}
					}
					
					itemList.add(surveyAdress);
				}
			}
			return itemList;
		}
		
		//Problem解析
		public List<Question>  parserProblemListItem(JSONArray array)
				throws JSONException {
			List<Question> itemList = new ArrayList<Question>();
			if (array != null && array.length() > 0) {
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj = array.getJSONObject(i);
					Question question = new Question();

					
					if (PublicUtils.isValid(obj, "id")) {
						question.setQuestionId(obj.getInt("id"));
					}
					
					if (PublicUtils.isValid(obj, "questionId")) {
						question.setQuestionnaireId(obj.getInt("questionId"));
					}
					
					if(PublicUtils.isValid(obj, "chapterId")){
						question.setChapterId(obj.getInt("chapterId"));
					}
					
					if (PublicUtils.isValid(obj, "problemNo")) {
						question.setQuestionNum(obj.getString("problemNo"));
					}
					
					if (PublicUtils.isValid(obj, "level")) {
						question.setLevel(obj.getInt("level"));
					}
					
					if (PublicUtils.isValid(obj, "type")) {
						question.setQuestionDiscriminate(obj.getInt("type"));
					}
					
					if (PublicUtils.isValid(obj, "title")) {
						question.setTopic(obj.getString("title"));
					}
					
					if (PublicUtils.isValid(obj, "desc")) {
						question.setRemarks(obj.getString("desc"));
					}
				  if (PublicUtils.isValid(obj, "isAnswer")) {//是否是必答题
						question.setIsAnswer(Integer.parseInt(obj.getString("isAnswer")));
					}
					
					if(PublicUtils.isValid(obj, "id")){
						Question q = questionDB.findQuestionById(question.getQuestionId());
						if(q != null){
							questionDB.updateQuestion(question);
						}else{
							questionDB.insertQuestion(question);
						}
					}
					
					
					itemList.add(question);
				}
			}
			return itemList;
		}
		
		//Option解析
		public List<AnswerOptions>  parserOptionListItem(JSONArray array)
				throws JSONException {
			List<AnswerOptions> itemList = new ArrayList<AnswerOptions>();
			if (array != null && array.length() > 0) {
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj = array.getJSONObject(i);
					AnswerOptions answerOptions = new AnswerOptions();

					if (PublicUtils.isValid(obj, "id")) {
						answerOptions.setOptionsId(obj.getInt("id"));
					}
					
					if (PublicUtils.isValid(obj, "questionId")) {
						answerOptions.setQuestionId(obj.getInt("questionId"));
					}
					
					if (PublicUtils.isValid(obj, "problemId")) {
						answerOptions.setProblemId(obj.getInt("problemId"));
					}
					
					if (PublicUtils.isValid(obj, "content")) {
						answerOptions.setOptions(obj.getString("content"));
					}
					
					if (PublicUtils.isValid(obj, "desc")) {
						answerOptions.setOptionsRemarks(obj.getString("desc"));
					}
					
					if(PublicUtils.isValid(obj, "id")){
						AnswerOptions a = answerOptionsDB.findAnswerOptionsById(answerOptions.getOptionsId());
						if(a != null){
							answerOptions.setIsSave(a.getIsSave());
							answerOptionsDB.updateOption(answerOptions);
						}else{
							answerOptionsDB.insertAnswerOptions(answerOptions);
						}
					}

				
					itemList.add(answerOptions);
				}
			}
			return itemList;
		}
				
								
				
				
}
