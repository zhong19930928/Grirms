package com.yunhu.yhshxc.workplan.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import com.yunhu.yhshxc.database.DatabaseHelper;
import com.yunhu.yhshxc.workplan.bo.Assess;

public class AssessDB {

	private DatabaseHelper openHelper;

	public AssessDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}

	public void insertAssess(Assess contacts) {
		ContentValues cv = putContentValues(contacts);
		if (contacts!=null && contacts.getPlan_id()>0) {
			openHelper.insert(openHelper.PLAN_ASSESS, cv);
		}
	}
	
	
	
	public void clearAssess(){
		StringBuffer sql = new StringBuffer();
		sql.append("delete from ").append(openHelper.PLAN_ASSESS);
		openHelper.execSQL(sql.toString());
	}

	private Assess putCarSalesContact(Cursor cursor) {
		int i = 1;
		Assess contacts = new Assess();
		contacts.setPlan_id(cursor.getInt(i++));
		contacts.setUser_id(cursor.getString(i++));
		contacts.setUser_name(cursor.getString(i++));
		contacts.setMarks(cursor.getString(i++));
		return contacts;
	}

	private ContentValues putContentValues(Assess contact) {
		ContentValues cv = new ContentValues();
		cv.put("plan_id", contact.getPlan_id());
		cv.put("user_id", contact.getUser_id());
		cv.put("user_name", contact.getUser_name());
		cv.put("marks", contact.getMarks());
		return cv;
	}
	/**
	 * 根据plan_id查询所有对应的评论数据
	 * @return
	 */
	public List<Assess> checkAllAssessByPlanId(int planId){
		SQLiteDatabase db = openHelper.getReadableDatabase();
		List<Assess> lists = new ArrayList<Assess>();
		Cursor cursor=db.rawQuery("select * from "+openHelper.PLAN_ASSESS+" where plan_id = ?", new String[]{planId+""} );
		while(cursor.moveToNext()){
			Assess as = new Assess();
			as.setPlan_id(cursor.getInt(cursor.getColumnIndex("plan_id")));
			as.setUser_id(cursor.getString(cursor.getColumnIndex("user_id")));
			as.setUser_name(cursor.getString(cursor.getColumnIndex("user_name")));
			as.setMarks(cursor.getString(cursor.getColumnIndex("marks")));
			lists.add(as);
		}
		
		cursor.close();
		
		return lists;
	}
	

}
