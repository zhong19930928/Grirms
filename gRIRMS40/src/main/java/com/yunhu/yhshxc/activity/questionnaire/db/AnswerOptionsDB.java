package com.yunhu.yhshxc.activity.questionnaire.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yunhu.yhshxc.activity.questionnaire.bo.AnswerOptions;
import com.yunhu.yhshxc.database.DatabaseHelper;

public class AnswerOptionsDB {

	private DatabaseHelper openHelper;

	public AnswerOptionsDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}

	public void insertAnswerOptions(AnswerOptions data) {
		ContentValues cv = putContentValues(data);
		openHelper.insert(openHelper.ANSWER_OPTIONS, cv);
	}
	
	public List<AnswerOptions> findAllFindIng(){
		List<AnswerOptions> list = new ArrayList<AnswerOptions>();
		StringBuffer findSql=new StringBuffer();
		findSql.append("select * from ").append(openHelper.ANSWER_OPTIONS);
		Cursor cursor = openHelper.query(findSql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putAnswerOptions(cursor));
			}
		}
		cursor.close();
		return list;
	}
	

	
	/**
	 * 根据Id查找AnswerOptions
	 * @param qId
	 * @return
	 */
	public AnswerOptions findAnswerOptionsById(int qId) {
		AnswerOptions answerOptions = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.ANSWER_OPTIONS)
				.append(" where optionsId = ").append(qId);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				answerOptions = putAnswerOptions(cursor);
			}
		}
		cursor.close();
		return answerOptions;
	}
	
	/**
	 * 根据Id查找AnswerOptions
	 * @param qId
	 * @return
	 */
	public List<AnswerOptions> findAnswerOptionsByQId(int qId) {
		List<AnswerOptions> list = new ArrayList<AnswerOptions>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.ANSWER_OPTIONS)
				.append(" where questionId = ").append(qId);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putAnswerOptions(cursor));
			}
		}
		cursor.close();
		return list;
	}
	
	
	/**
	 * 根据Id查找AnswerOptions
	 * @param qId
	 * @return
	 */
	public List<AnswerOptions> findAnswerOptionById(int qId,int pId) {
		List<AnswerOptions> aList = new ArrayList<AnswerOptions>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.ANSWER_OPTIONS)
				.append(" where questionId = ").append(qId)
				.append(" and problemId = ").append(pId);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				aList.add(putAnswerOptions(cursor));
			}
		}
		cursor.close();
		return aList;
	}
	
	
	/**
	 * 更新AnswerOptions
	 * @param question
	 */
	public void updateOption(AnswerOptions answerOptions) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues cv = putContentValues(answerOptions);
		db.update(openHelper.ANSWER_OPTIONS, cv, " optionsid=? ",
				new String[] { answerOptions.getOptionsId() + "" });

	}
	
	public void clearAnswerOptions(){
		StringBuffer sql = new StringBuffer();
		sql.append("delete from ").append(openHelper.ANSWER_OPTIONS);
		openHelper.execSQL(sql.toString());
	}

	private AnswerOptions putAnswerOptions(Cursor cursor) {
		int i = 0;
		AnswerOptions data = new AnswerOptions();
		data.setId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		data.setOptionsId(cursor.getInt(i++));
		data.setQuestionId(cursor.getInt(i++));
		data.setOptions(cursor.getString(i++));
		data.setOptionsRemarks(cursor.getString(i++));
		data.setProblemId(cursor.getInt(i++));
		data.setIsSave(cursor.getInt(i++));
		return data;
	}

	private ContentValues putContentValues(AnswerOptions data) {
		ContentValues cv = new ContentValues();
		cv.put("optionsId", data.getOptionsId());
		cv.put("questionId", data.getQuestionId());
		cv.put("options", data.getOptions());
		cv.put("optionsRemarks", data.getOptionsRemarks());
		cv.put("problemId", data.getProblemId());
		cv.put("isSave", data.getIsSave());
		return cv;
	}

}
