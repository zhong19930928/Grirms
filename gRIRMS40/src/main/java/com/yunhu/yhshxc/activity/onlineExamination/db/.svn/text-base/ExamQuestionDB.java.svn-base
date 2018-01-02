package com.yunhu.yhshxc.activity.onlineExamination.db;


import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.yunhu.yhshxc.activity.onlineExamination.bo.ExamQuestion;
import com.yunhu.yhshxc.database.DatabaseHelper;

public class ExamQuestionDB {

	private DatabaseHelper openHelper;

	public ExamQuestionDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}

	public void insertExamQuestion(ExamQuestion data) {
		ContentValues cv = putContentValues(data);
		openHelper.insert(openHelper.EXAM_QUESTION, cv);
	}
	
	public List<ExamQuestion> findAllExamQuestion(){
		List<ExamQuestion> list = new ArrayList<ExamQuestion>();
		StringBuffer findSql=new StringBuffer();
		findSql.append("select * from ").append(openHelper.EXAM_QUESTION);
		Cursor cursor = openHelper.query(findSql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putExamQuestion(cursor));
			}
		}
		cursor.close();
		return list;
	}
	
	public void clearAnswerOptions(){
		StringBuffer sql = new StringBuffer();
		sql.append("delete from ").append(openHelper.EXAM_QUESTION);
		openHelper.execSQL(sql.toString());
	}

	private ExamQuestion putExamQuestion(Cursor cursor) {
		int i = 0;
		ExamQuestion data = new ExamQuestion();
		data.setId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		data.setQuestionId(cursor.getInt(i++));
		data.setPaperId(cursor.getInt(i++));
		data.setPaperNum(cursor.getInt(i++));
		data.setQuestionLevel(cursor.getInt(i++));
		data.setQuestionsDif(cursor.getInt(i++));
		data.setTopic(cursor.getString(i++));
		data.setRemarks(cursor.getString(i++));
		data.setMulChoiceStandard(cursor.getInt(i++));
		data.setAnswer(cursor.getString(i++));
		data.setScore(cursor.getInt(i++));
		return data;
	}

	private ContentValues putContentValues(ExamQuestion data) {
		ContentValues cv = new ContentValues();
		cv.put("questionId", data.getQuestionId());
		cv.put("paperId", data.getPaperId());
		cv.put("paperNum", data.getPaperNum());
		cv.put("questionLevel", data.getQuestionLevel());
		cv.put("questionsDif", data.getQuestionsDif());
		cv.put("topic", data.getTopic());
		cv.put("remarks", data.getRemarks());
		cv.put("mulChoiceStandard", data.getMulChoiceStandard());
		cv.put("answer", data.getAnswer());
		cv.put("score", data.getScore());
		return cv;
	}

}
