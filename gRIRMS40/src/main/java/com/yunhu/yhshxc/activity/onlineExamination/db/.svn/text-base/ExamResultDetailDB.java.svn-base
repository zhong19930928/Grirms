package com.yunhu.yhshxc.activity.onlineExamination.db;


import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.yunhu.yhshxc.activity.onlineExamination.bo.ExamResultDetail;
import com.yunhu.yhshxc.database.DatabaseHelper;

public class ExamResultDetailDB {

	private DatabaseHelper openHelper;

	public ExamResultDetailDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}

	public void insertExamResultDetail(ExamResultDetail data) {
		ContentValues cv = putContentValues(data);
		openHelper.insert(openHelper.EXAM_RESULT_DETAIL, cv);
	}
	
	public List<ExamResultDetail> findAllExamResultDetail(){
		List<ExamResultDetail> list = new ArrayList<ExamResultDetail>();
		StringBuffer findSql=new StringBuffer();
		findSql.append("select * from ").append(openHelper.EXAM_RESULT_DETAIL);
		Cursor cursor = openHelper.query(findSql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putExamResultDetail(cursor));
			}
		}
		cursor.close();
		return list;
	}
	
	public void clearAnswerOptions(){
		StringBuffer sql = new StringBuffer();
		sql.append("delete from ").append(openHelper.EXAM_RESULT_DETAIL);
		openHelper.execSQL(sql.toString());
	}

	private ExamResultDetail putExamResultDetail(Cursor cursor) {
		int i = 0;
		ExamResultDetail data = new ExamResultDetail();
		data.setId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		data.setResultId(cursor.getInt(i++));
		data.setQuestionId(cursor.getInt(i++));
		data.setOptionIds(cursor.getString(i++));
		data.setFillContent(cursor.getString(i++));
		data.setAttachmentUrl(cursor.getString(i++));
		return data;
	}

	private ContentValues putContentValues(ExamResultDetail data) {
		ContentValues cv = new ContentValues();
		cv.put("resultId", data.getResultId());
		cv.put("questionId", data.getQuestionId());
		cv.put("optionIds", data.getOptionIds());
		cv.put("fillContent", data.getFillContent());
		cv.put("attachmentUrl", data.getAttachmentUrl());
		return cv;
	}

}
