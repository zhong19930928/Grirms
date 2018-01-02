package com.yunhu.yhshxc.activity.questionnaire.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yunhu.yhshxc.activity.questionnaire.bo.Questionnaire;
import com.yunhu.yhshxc.database.DatabaseHelper;

public class QuestionnaireDB {

	private DatabaseHelper openHelper;

	public QuestionnaireDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}

	public void insertQuestionnaire(Questionnaire data) {
		ContentValues cv = putContentValues(data);
		openHelper.insert(openHelper.QUESTIONNAIRE, cv);
	}
	/**
	 * 根据Id查找Questionnaire
	 * @param qId
	 * @return
	 */
	public Questionnaire findQuestionnaireById(int qId) {
		Questionnaire questionnaire = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.QUESTIONNAIRE)
				.append(" where questionid = ").append(qId);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				questionnaire = putQuestionnaire(cursor);
			}
		}
		cursor.close();
		return questionnaire;
	}
	
	/**
	 * 更新Questionnaire
	 * @param questionnaire
	 */
	public void updateQuestionnaire(Questionnaire questionnaire) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues cv = putContentValues(questionnaire);
		db.update(openHelper.QUESTIONNAIRE, cv, " questionid=? ",
				new String[] { questionnaire.getQuestionId() + "" });

	}
	/**
	 * 调查结果解析接口数据不全,使用我的问卷部分数据()
	 * @param data
	 * @return
	 */
	public void updateQuestionnaireResult(Questionnaire questionnaire) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues cv = putContentValuesResult(questionnaire);
		db.update(openHelper.QUESTIONNAIRE, cv, " questionid=? ",
				new String[] { questionnaire.getQuestionId() + "" });

	}
	
	public List<Questionnaire> findAllQuestionnaire(){
		List<Questionnaire> list = new ArrayList<Questionnaire>();
		StringBuffer findSql=new StringBuffer();
		findSql.append("select * from ").append(openHelper.QUESTIONNAIRE);
		Cursor cursor = openHelper.query(findSql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putQuestionnaire(cursor));
			}
		}
		cursor.close();
		return list;
	}
	
	public void clearQuestionnaire(){
		StringBuffer sql = new StringBuffer();
		sql.append("delete from ").append(openHelper.QUESTIONNAIRE);
		openHelper.execSQL(sql.toString());
	}

	private Questionnaire putQuestionnaire(Cursor cursor) {
		int i = 0;
		Questionnaire data = new Questionnaire();
		data.setId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		data.setQuestionId(cursor.getInt(i++));
		data.setName(cursor.getString(i++));
		data.setExplain(cursor.getString(i++));
		data.setStartDate(cursor.getString(i++));
		data.setEndDate(cursor.getString(i++));
		data.setNumbers(cursor.getInt(i++));
		data.setQuestionnaireState(cursor.getInt(i++));
		data.setFindingState(cursor.getInt(i++));
		data.setCycle(cursor.getInt(i++));
		data.setUpCopies(cursor.getInt(i++));
		return data;
	}

	private ContentValues putContentValues(Questionnaire data) {
		ContentValues cv = new ContentValues();
		cv.put("name", data.getName());
		cv.put("explain", data.getExplain());
		cv.put("startDate", data.getStartDate());
		cv.put("endDate", data.getEndDate());
		cv.put("numbers", data.getNumbers());
		cv.put("questionnaireState", data.getQuestionnaireState());
		cv.put("findingState", data.getFindingState());
		cv.put("questionid", data.getQuestionId());
		cv.put("cycle", data.getCycle());
		cv.put("upCopies", data.getUpCopies());
		return cv;
	}
	/**
	 * 调查结果解析接口数据不全,使用我的问卷部分数据
	 * @param data
	 * @return
	 */
	private ContentValues putContentValuesResult(Questionnaire data) {
		ContentValues cv = new ContentValues();
	
		cv.put("name", data.getName());
		cv.put("explain", data.getExplain());
		cv.put("startDate", data.getStartDate());
		cv.put("endDate", data.getEndDate());		
		cv.put("questionid", data.getQuestionId());
		return cv;
	}

}
