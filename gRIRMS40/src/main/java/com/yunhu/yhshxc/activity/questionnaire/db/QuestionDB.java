package com.yunhu.yhshxc.activity.questionnaire.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yunhu.yhshxc.activity.questionnaire.bo.Question;
import com.yunhu.yhshxc.database.DatabaseHelper;

public class QuestionDB {

	private DatabaseHelper openHelper;

	public QuestionDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}

	public void insertQuestion(Question data) {
		ContentValues cv = putContentValues(data);
		openHelper.insert(openHelper.QUESTION, cv);
	}
	
	//查询qId下所有question
	public List<Question> findAllQuestion(int qId){
		List<Question> list = new ArrayList<Question>();
		StringBuffer findSql=new StringBuffer();
		findSql.append("select * from ").append(openHelper.QUESTION)
		             .append(" where questionnaireId = ").append(qId);
		Cursor cursor = openHelper.query(findSql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putQuestion(cursor));
			}
		}
		cursor.close();
		return list;
	}
	
	
	
	
	
	/**
	 * 根据Id查找Question
	 * @param qId
	 * @return
	 */
	public Question findQuestionById(int questionid) {
		Question question = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.QUESTION)
				.append(" where questionid = ").append(questionid);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				question = putQuestion(cursor);
			}
		}
		cursor.close();
		return question;
	}
	
	

	/**
	 * 根据questionnaireId查询questionList
	 * @param qId
	 * @return
	 */
	public List<Question> findQuestionListById(int qId,int level) {
		List<Question> list = new ArrayList<Question>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.QUESTION)
				.append(" where questionnaireid = ").append(qId)
				.append(" and level = ").append(level);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				Question question = putQuestion(cursor);
				list.add(question);
			}
			cursor.close();
		}else{
			cursor.close();
		}
		
		return list;
	}
	/**
	 * 根据questionnaireId查询questionList
	 * @param qId
	 * @return
	 */
	public Question findQuestionById(int qId,int level,String number) {
		Question list = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.QUESTION)
		.append(" where questionnaireid = ").append(qId)
		.append(" and level = ").append(level).append(" and questionNum = '").append(number).append("'");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list = putQuestion(cursor);
			}
			cursor.close();
		}else{
			cursor.close();
		}
		
		return list;
	}
	
	

	/**
	 * 根据questionnaireId查询questionList
	 * @param qId
	 * @return
	 */
	public List<Question> findQuestionListByCId(int qId,int level,int cId) {
		List<Question> list = new ArrayList<Question>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.QUESTION)
				.append(" where questionnaireid = ").append(qId)
				.append(" and level = ").append(level)
				.append(" and chapterId = ").append(cId);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				Question question = putQuestion(cursor);
				list.add(question);
			}
			cursor.close();
		}
		
		return list;
	}
	
	
	
	
	
	
	/**
	 * 更新Questionnaire
	 * @param question
	 */
	public void updateQuestion(Question question) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues cv = putContentValues(question);
		db.update(openHelper.QUESTION, cv, " questionid=? ",
				new String[] { question.getQuestionId() + "" });

	}
	
	public void clearQuestion(){
		StringBuffer sql = new StringBuffer();
		sql.append("delete from ").append(openHelper.QUESTION);
		openHelper.execSQL(sql.toString());
	}

	private Question putQuestion(Cursor cursor) {
		int i = 0;
		Question data = new Question();
		data.setId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		data.setQuestionId(cursor.getInt(i++));
		data.setQuestionnaireId(cursor.getInt(i++));
		data.setQuestionNum(cursor.getString(i++));
		data.setLevel(cursor.getInt(i++));
		data.setQuestionDiscriminate(cursor.getInt(i++));
		data.setTopic(cursor.getString(i++));
		data.setRemarks(cursor.getString(i++));
		data.setChapterId(cursor.getInt(i++));
		data.setIsAnswer(cursor.getInt(i));
		return data;
	}

	private ContentValues putContentValues(Question data) {
		ContentValues cv = new ContentValues();
		cv.put("questionId", data.getQuestionId());
		cv.put("questionnaireId", data.getQuestionnaireId());
		cv.put("questionNum", data.getQuestionNum());
		cv.put("level", data.getLevel());
		cv.put("questionDiscriminate", data.getQuestionDiscriminate());
		cv.put("topic", data.getTopic());
		cv.put("remarks", data.getRemarks());
		cv.put("chapterId", data.getChapterId());
		cv.put("isAnswer",data.getIsAnswer());
		return cv;
	}

}
