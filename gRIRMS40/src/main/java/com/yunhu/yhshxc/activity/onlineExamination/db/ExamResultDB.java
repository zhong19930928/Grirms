package com.yunhu.yhshxc.activity.onlineExamination.db;


import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.yunhu.yhshxc.activity.onlineExamination.bo.ExamResult;
import com.yunhu.yhshxc.database.DatabaseHelper;

public class ExamResultDB {

	private DatabaseHelper openHelper;

	public ExamResultDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}

	public void insertExamResult(ExamResult data) {
		ContentValues cv = putContentValues(data);
		openHelper.insert(openHelper.EXAM_RESULT, cv);
	}
	
	public List<ExamResult> findAllExamResult(){
		List<ExamResult> list = new ArrayList<ExamResult>();
		StringBuffer findSql=new StringBuffer();
		findSql.append("select * from ").append(openHelper.EXAM_RESULT);
		Cursor cursor = openHelper.query(findSql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putExamResult(cursor));
			}
		}
		cursor.close();
		return list;
	}
	
	public void clearAnswerOptions(){
		StringBuffer sql = new StringBuffer();
		sql.append("delete from ").append(openHelper.EXAM_RESULT);
		openHelper.execSQL(sql.toString());
	}

	private ExamResult putExamResult(Cursor cursor) {
		int i = 0;
		ExamResult data = new ExamResult();
		data.setId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		data.setResultId(cursor.getInt(i++));
		data.setPaperId(cursor.getInt(i++));
		data.setPaperNum(cursor.getInt(i++));
		data.setExamUserId(cursor.getString(i++));
		data.setExamUserName(cursor.getString(i++));
		data.setExamPhoneno(cursor.getString(i++));
		data.setPaperSubmitTime(cursor.getString(i++));
		data.setExamTime(cursor.getString(i++));
		data.setExamUserOrgPath(cursor.getString(i++));
		data.setScore(cursor.getString(i++));
		return data;
	}

	private ContentValues putContentValues(ExamResult data) {
		ContentValues cv = new ContentValues();
		cv.put("resultId", data.getResultId());
		cv.put("paperId", data.getPaperId());
		cv.put("paperNum", data.getPaperNum());
		cv.put("examUserId", data.getExamUserId());
		cv.put("examUserName", data.getExamUserName());
		cv.put("examPhoneno", data.getExamPhoneno());
		cv.put("paperSubmitTime", data.getPaperSubmitTime());
		cv.put("examTime", data.getExamTime());
		cv.put("examUserOrgPath", data.getExamUserOrgPath());
		cv.put("score", data.getScore());
		return cv;
	}

}
