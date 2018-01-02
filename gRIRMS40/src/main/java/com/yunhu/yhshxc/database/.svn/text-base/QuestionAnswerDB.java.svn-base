package com.yunhu.yhshxc.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yunhu.yhshxc.bo.QA;

public class QuestionAnswerDB {

	private DatabaseHelper openHelper;

	public QuestionAnswerDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}
	
	public void insertQA(QA qa){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues cv = putContentValues(qa);
		db.insert(openHelper.QA_TABLE, null, cv);
		
	}
	
	public ArrayList<QA> findAllList(){
		SQLiteDatabase db = openHelper.getReadableDatabase();
		ArrayList<QA> list = new ArrayList<QA>();
		StringBuffer  sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.QA_TABLE).append(" order by id");
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putQA(cursor));
			}
		}
		cursor.close();
		
		return list;
	}
	
	private QA putQA(Cursor cursor) {
		int i = 0;
		QA qa = new QA();
		qa.setId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		qa.setQuesion(cursor.getString(i++));
		qa.setAnswer(cursor.getString(i++));
		return qa;
	}
	
	private ContentValues putContentValues(QA qa){
		ContentValues cv = new ContentValues();
		cv.put("question", qa.getQuesion());
		cv.put("answer", qa.getAnswer());
		return cv;
	}
	
}
