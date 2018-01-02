package com.yunhu.yhshxc.wechat.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yunhu.yhshxc.database.DatabaseHelper;
import com.yunhu.yhshxc.wechat.bo.Survey;

public class SurveyDB {
	private DatabaseHelper openHelper;

	public SurveyDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}

	public void insert(Survey survey) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues cv = putContentValues(survey);
		db.insert(openHelper.SURVEY, null, cv);
	}

	public void updateSurvey(Survey survey) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues cv = putContentValues(survey);
		db.update(openHelper.SURVEY, cv, " surveyId=? ",
				new String[] { survey.getSurveyId() + "" });

	}

	/**
	 * 根据topicId查询调查选项
	 * 
	 * @return
	 */

	public List<Survey> findSurveyListByTopicId(int topicId) {
		List<Survey> list = new ArrayList<Survey>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.SURVEY)
				.append(" where topicId = ").append(topicId);

		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				Survey a = putSurvey(cursor);
				list.add(a);
			}
		}
		cursor.close();
		return list;
	}

	public List<Survey> findSurveyList() {
		List<Survey> list = new ArrayList<Survey>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.SURVEY)
				.append(" where 1=1");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				Survey a = putSurvey(cursor);
				list.add(a);
			}
			cursor.close();
		}
		return list;
	}

	public Survey findReplyBySurveyId(int surveyId) {
		Survey survey = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.SURVEY)
				.append(" where surveyId = ").append(surveyId);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				survey = putSurvey(cursor);
			}
		}
		cursor.close();
		return survey;
	}
	
	public void deleteAll(){
		String sql = "delete from "+openHelper.SURVEY;
		openHelper.execSQL(sql);
	}
	

	private ContentValues putContentValues(Survey comment) {
		ContentValues cv = new ContentValues();
		cv.put("surveyId", comment.getSurveyId());
		cv.put("topicId", comment.getTopicId());
		cv.put("explain", comment.getExplain());
		cv.put("title", comment.getTitle());
		cv.put("type", comment.getType());
		cv.put("topicId", comment.getTopicId());
		cv.put("surveyType", comment.getSurveyType());
		cv.put("options", comment.getOptions());
		cv.put("optionOrder", comment.getOptionOrder());
		return cv;
	}

	private Survey putSurvey(Cursor cursor) {
		int i = 0;
		Survey module = new Survey();
		module.setId(cursor.isNull(i) ? null : cursor.getInt(i));
		i++;
		module.setSurveyId(cursor.getInt(i++));
		module.setTopicId(cursor.getInt(i++));
		module.setExplain(cursor.getString(i++));
		module.setTitle(cursor.getString(i++));
		module.setType(cursor.getInt(i++));
		module.setSurveyType(cursor.getInt(i++));
		module.setOptions(cursor.getString(i++));
		module.setOptionOrder(cursor.getInt(i++));
		return module;
	}

}
