package com.yunhu.yhshxc.activity.questionnaire.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yunhu.yhshxc.activity.questionnaire.bo.SurveyAdress;
import com.yunhu.yhshxc.database.DatabaseHelper;

public class SurveyAdressDB {

	private DatabaseHelper openHelper;

	public SurveyAdressDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}

	public void insertSurveyAdress(SurveyAdress data) {
		ContentValues cv = putContentValues(data);
		openHelper.insert(openHelper.SURVEY_ADRESS, cv);
	}
	
	public List<SurveyAdress> findAllSurveyAdress(){
		List<SurveyAdress> list = new ArrayList<SurveyAdress>();
		StringBuffer findSql=new StringBuffer();
		findSql.append("select * from ").append(openHelper.SURVEY_ADRESS);
		Cursor cursor = openHelper.query(findSql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putSurveyAdress(cursor));
			}
		}
		cursor.close();
		return list;
	}
	
	/**
	 * 根据Id查找SurveyAdress
	 * @param sId
	 * @return
	 */
	public SurveyAdress findSurveyAdressById(int sId) {
		SurveyAdress surveyAdress = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.SURVEY_ADRESS)
				.append(" where surveyAdressId = ").append(sId);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				surveyAdress = putSurveyAdress(cursor);
			}
		}
		cursor.close();
		return surveyAdress;
	}
	
	/**
	 * 根据Id查找SurveyAdress
	 * @param qId
	 * @return
	 */
	public List<SurveyAdress> findSurveyAdressByqId(int qId) {
		List<SurveyAdress> surveyAdress = new ArrayList<>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.SURVEY_ADRESS)
				.append(" where questionnaireId = ").append(qId);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				surveyAdress.add(putSurveyAdress(cursor));
			}
		}
		cursor.close();
		return surveyAdress;
	}
	
	/**
	 * 更新SurveyAdress
	 * @param surveyAdress
	 */
	public void updateSurveyAdress(SurveyAdress surveyAdress) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues cv = putContentValues(surveyAdress);
		db.update(openHelper.SURVEY_ADRESS, cv, " surveyAdressId=? ",
				new String[] { surveyAdress.getSurveyAdressId() + "" });

	}
	
	public void clearSurveyAdress(){
		StringBuffer sql = new StringBuffer();
		sql.append("delete from ").append(openHelper.SURVEY_ADRESS);
		openHelper.execSQL(sql.toString());
	}

	private SurveyAdress putSurveyAdress(Cursor cursor) {
		int i = 0;
		SurveyAdress data = new SurveyAdress();
		data.setId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		data.setSurveyAdressId(cursor.getInt(i++));
		data.setQuestionnaireId(cursor.getInt(i++));
		data.setAdress(cursor.getString(i++));
		return data;
	}

	private ContentValues putContentValues(SurveyAdress data) {
		ContentValues cv = new ContentValues();
		cv.put("surveyAdressId", data.getSurveyAdressId());
		cv.put("questionnaireId", data.getQuestionnaireId());
		cv.put("adress", data.getAdress());
		return cv;
	}

}
