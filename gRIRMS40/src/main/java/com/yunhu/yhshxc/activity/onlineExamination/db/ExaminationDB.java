package com.yunhu.yhshxc.activity.onlineExamination.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.yunhu.yhshxc.activity.onlineExamination.bo.Examination;
import com.yunhu.yhshxc.database.DatabaseHelper;

public class ExaminationDB {

	private DatabaseHelper openHelper;

	public ExaminationDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}

	public void insertExamination(Examination data) {
		ContentValues cv = putContentValues(data);
		openHelper.insert(openHelper.EXAMINATION, cv);
	}
	
	public List<Examination> findAllExamination(){
		List<Examination> list = new ArrayList<Examination>();
		StringBuffer findSql=new StringBuffer();
		findSql.append("select * from ").append(openHelper.EXAMINATION);
		Cursor cursor = openHelper.query(findSql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putExamination(cursor));
			}
		}
		cursor.close();
		return list;
	}
	
	public void clearAnswerOptions(){
		StringBuffer sql = new StringBuffer();
		sql.append("delete from ").append(openHelper.EXAMINATION);
		openHelper.execSQL(sql.toString());
	}

	private Examination putExamination(Cursor cursor) {
		int i = 0;
		Examination data = new Examination();
		data.setId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		data.setExaminationId(cursor.getInt(i++));
		data.setTitle(cursor.getString(i++));
		data.setExplain(cursor.getString(i++));
		data.setStartDate(cursor.getString(i++));
		data.setEndDate(cursor.getString(i++));
		data.setReleaseDate(cursor.getString(i++));
		data.setState(cursor.getInt(i++));
		data.setRecord(cursor.getString(i++));
		return data;
	}

	private ContentValues putContentValues(Examination data) {
		ContentValues cv = new ContentValues();
		cv.put("examinationId", data.getExaminationId());
		cv.put("title", data.getTitle());
		cv.put("explain", data.getExplain());
		cv.put("startDate", data.getStartDate());
		cv.put("endDate", data.getEndDate());
		cv.put("releaseDate", data.getReleaseDate());
		cv.put("state", data.getState());
		cv.put("record", data.getRecord());
		return cv;
	}

}
