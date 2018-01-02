package com.yunhu.yhshxc.activity.onlineExamination.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.yunhu.yhshxc.activity.onlineExamination.bo.EaxmAnswerOptions;
import com.yunhu.yhshxc.activity.onlineExamination.bo.ExamQuestion;
import com.yunhu.yhshxc.activity.onlineExamination.bo.Examination;
import com.yunhu.yhshxc.utility.PublicUtils;

public class ExamUtil {
	private Context context;
	public ExamUtil(Context context){
		this.context = context;
	}
	/**
	 * 解析试卷配置信息
	 * @throws JSONException 
	 */
	public void parseExamnation(JSONObject obj) throws JSONException{
		JSONArray arrayOption = obj.getJSONArray("on_option");
		parseOptionList(arrayOption);
		JSONArray arrayQuestion = obj.getJSONArray("on_problem");
		parseQuestion(arrayQuestion);
		JSONArray arrayExam = obj.getJSONArray("online");
		parseResult(arrayExam);
	}
	/**
	 * 解析答题选项 option
	 * @throws JSONException 
	 */
	public List<EaxmAnswerOptions> parseOptionList(JSONArray array) throws JSONException{
		List<EaxmAnswerOptions> options = new ArrayList<EaxmAnswerOptions>();
		if (array != null && array.length() > 0) {
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				EaxmAnswerOptions op = new EaxmAnswerOptions();
				if(PublicUtils.isValid(obj, "id")){//选项id
					op.setOptionsId(obj.getInt("id"));
				}
				if(PublicUtils.isValid(obj, "problemId")){//问题id
					op.setQuestionId(obj.getInt("problemId"));
				}
				if(PublicUtils.isValid(obj, "companyId")){//公司id
					
				}
				if(PublicUtils.isValid(obj, "onlineId")){//试卷id
					
				}
				if(PublicUtils.isValid(obj, "optionNo")){//排序
					
				}
				if(PublicUtils.isValid(obj, "content")){//选项内容
					
				}
				if(PublicUtils.isValid(obj, "desc")){//选项说明
					
				}
				if(PublicUtils.isValid(obj, "isAnswer")){//是否是标准答案  1:否；2：是
					op.setIsRight(obj.getString("isAnswer"));
				}
				if(PublicUtils.isValid(obj, "score")){// 分值
					op.setScores(obj.getInt("score"));
				}
				options.add(op);
				
			}
		}
		return options;
	}
	
	/**
	 * 解析考试问题  on_problem
	 * @throws JSONException 
	 */
	public List<ExamQuestion> parseQuestion(JSONArray array) throws JSONException{
		List<ExamQuestion> list = new ArrayList<ExamQuestion>();
		if(array !=null &&array.length()>0){
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				ExamQuestion que = new ExamQuestion();
				if(PublicUtils.isValid(obj, "id")){// 问题ID
					que.setQuestionId(obj.getInt("id"));
				}
				if(PublicUtils.isValid(obj, "companyId")){//公司ID
					
				}
				if(PublicUtils.isValid(obj, "onlineId")){//  试卷ID
					que.setPaperId(obj.getInt("onlineId"));
				}
				if(PublicUtils.isValid(obj, "problemNo")){//排序
					que.setPaperNum(obj.getInt("problemNo"));
				}
				if(PublicUtils.isValid(obj, "level")){// 问题层级（1：章节、2：问题）
					que.setQuestionLevel(obj.getInt("level"));
				}
				// 问题区分（1：单选、2：单选（其他）、3：多选、4：多选（其他）、5：填空）
				if(PublicUtils.isValid(obj, "type")){ 
					que.setQuestionsDif(obj.getInt("type"));
				}
				if(PublicUtils.isValid(obj, "title")){// 题干
					que.setTopic(obj.getString("title"));
				}
				if(PublicUtils.isValid(obj, "desc")){// 说明
					que.setRemarks(obj.getString("desc"));
				}
				/**
				 * 评分规则（1：选对记分、2：选错扣分、3：完全符合、4：部分符合、
				 * 5：错选扣分 单选题的评分规则：【选对记分、选错扣分】 两种。 
				 * 多选题的评分规则：【完全符合、部分符合、错选扣分】 三种。）
				 */
				if(PublicUtils.isValid(obj, "scoreRule")){
					que.setMulChoiceStandard(obj.getInt("scoreRule"));
				}
				if(PublicUtils.isValid(obj, "score")){// 分值
					que.setScore(obj.getInt("score"));
				}
				list.add(que);
			}
		}
		return list;
	}
	
	/**
	 * 解析试卷的整体定义 online
	 * @throws JSONException 
	 */
	public List<Examination> parseResult(JSONArray array) throws JSONException{
		List<Examination> results = new ArrayList<Examination>();
		if(array!=null&&array.length()>0){
			for(int i = 0;i <array.length(); i++){
				JSONObject obj = array.getJSONObject(i);
				Examination re = new Examination();
				if(PublicUtils.isValid(obj, "id")){// 试卷ID
					re.setExaminationId(obj.getInt("id"));
				}
				if(PublicUtils.isValid(obj, "companyId")){// 公司ID
					
				}
				if(PublicUtils.isValid(obj, "title")){// 试卷TITLE
					re.setTitle(obj.getString("title"));
				}
				if(PublicUtils.isValid(obj, "desc")){// 备注
					
				}
				if(PublicUtils.isValid(obj, "startDate")){// 试卷开始时间
					re.setStartDate(obj.getString("startDate"));
				}
				if(PublicUtils.isValid(obj, "endDate")){// 试卷结束时间
					re.setEndDate(obj.getString("endDate"));
				}
				if(PublicUtils.isValid(obj, "onlineTime")){// 试卷规定时间（分钟）
					
				}
			}
		}
		return results;
	}
}
