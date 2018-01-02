package com.yunhu.yhshxc.activity.onlineExamination.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.yunhu.yhshxc.activity.onlineExamination.bo.EaxmAnswerOptions;
import com.yunhu.yhshxc.database.DatabaseHelper;

public class EaxmAnswerOptionsDB {

	private DatabaseHelper openHelper;

	public EaxmAnswerOptionsDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}

	public void insertAnswerOptions(EaxmAnswerOptions data) {
		ContentValues cv = putContentValues(data);
		openHelper.insert(openHelper.EXAM_ANSWER_OPTIONS, cv);
	}
	
	public List<EaxmAnswerOptions> findAllEaxmAnswerOptions(){
		List<EaxmAnswerOptions> list = new ArrayList<EaxmAnswerOptions>();
		StringBuffer findSql=new StringBuffer();
		findSql.append("select * from ").append(openHelper.EXAM_ANSWER_OPTIONS);
		Cursor cursor = openHelper.query(findSql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putEaxmAnswerOptions(cursor));
			}
		}
		cursor.close();
		return list;
	}
	
	public void clearAnswerOptions(){
		StringBuffer sql = new StringBuffer();
		sql.append("delete from ").append(openHelper.EXAM_ANSWER_OPTIONS);
		openHelper.execSQL(sql.toString());
	}

	private EaxmAnswerOptions putEaxmAnswerOptions(Cursor cursor) {
		int i = 0;
		EaxmAnswerOptions data = new EaxmAnswerOptions();
		data.setId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		data.setOptionsId(cursor.getInt(i++));
		data.setQuestionId(cursor.getInt(i++));
		data.setOptions(cursor.getString(i++));
		data.setIsRight(cursor.getString(i++));
		data.setScores(cursor.getInt(i++));
		return data;
	}

	private ContentValues putContentValues(EaxmAnswerOptions data) {
		ContentValues cv = new ContentValues();
		cv.put("optionsId", data.getOptionsId());
		cv.put("questionId", data.getQuestionId());
		cv.put("options", data.getOptions());
		cv.put("isRight", data.getIsRight());
		cv.put("scores", data.getScores());
		return cv;
	}

}
