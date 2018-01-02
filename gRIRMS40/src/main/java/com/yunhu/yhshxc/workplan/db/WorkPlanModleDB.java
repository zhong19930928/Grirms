package com.yunhu.yhshxc.workplan.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import com.yunhu.yhshxc.database.DatabaseHelper;
import com.yunhu.yhshxc.workplan.bo.WorkPlanModle;

public class WorkPlanModleDB {

	private DatabaseHelper openHelper;

	public WorkPlanModleDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}

	public void insertWorkPlan(WorkPlanModle contacts) {
		ContentValues cv = putContentValues(contacts);
		if (contacts!=null && contacts.getPlanId()>0) {
			openHelper.insert(openHelper.WORK_PLAN_MODLE, cv);
		}
	}
	
	
	
	public void clearWorkPlan(){
		StringBuffer sql = new StringBuffer();
		sql.append("delete from ").append(openHelper.WORK_PLAN_MODLE);
		openHelper.execSQL(sql.toString());
	}

	private WorkPlanModle putWorkPlanModle(Cursor cursor) {
		int i = 1;
		WorkPlanModle contacts = new WorkPlanModle();
		contacts.setPlanId(cursor.getInt(i++));
		contacts.setPlanTitle(cursor.getString(i++));
		contacts.setPlanContent(cursor.getString(i++));
		contacts.setPlanMark(cursor.getString(i++));
		contacts.setImportantLevel(cursor.getInt(i++));
		contacts.setRushLevel(cursor.getInt(i++));
		return contacts;
	}

	private ContentValues putContentValues(WorkPlanModle contact) {
		ContentValues cv = new ContentValues();
		cv.put("plan_id", contact.getPlanId());
		cv.put("plan_title", contact.getPlanTitle());
		cv.put("plan_content", contact.getPlanContent());
		cv.put("plan_note", contact.getPlanMark());
		cv.put("important_level", contact.getImportantLevel());
		cv.put("rush_level", contact.getRushLevel());
		return cv;
	}
	
	/**
	 * 查询表中所有数据
	 * @return WorkPlanModle的集合
	 */
	public List<WorkPlanModle> checkAllPlanModle(){
		List<WorkPlanModle> dataList = new ArrayList<WorkPlanModle>();
	SQLiteDatabase db	= openHelper.getReadableDatabase();
	Cursor cursor =db.rawQuery("select * from "+openHelper.WORK_PLAN_MODLE, null);
	while(cursor.moveToNext()){
		WorkPlanModle modle = new WorkPlanModle();
		modle.setPlanId(cursor.getInt(cursor.getColumnIndex("plan_id")));
		modle.setPlanTitle(cursor.getString(cursor.getColumnIndex("plan_title")));
		modle.setPlanContent(cursor.getString(cursor.getColumnIndex("plan_content")));
		modle.setPlanMark(cursor.getString(cursor.getColumnIndex("plan_note")));
		modle.setImportantLevel(cursor.getInt(cursor.getColumnIndex("important_level")));
		modle.setRushLevel(cursor.getInt(cursor.getColumnIndex("rush_level")));
		dataList.add(modle);
		
	}
		
		cursor.close();
		return dataList ;
		
	}
	
	/**
	 * 根据planid查询对应的数据
	 */
	
	public List<WorkPlanModle> checkPlansByPlanId(int planId){
		
		List<WorkPlanModle> dataList = new ArrayList<WorkPlanModle>();
		SQLiteDatabase db	= openHelper.getReadableDatabase();
		Cursor cursor =db.rawQuery("select * from "+openHelper.WORK_PLAN_MODLE+" where plan_id = ? order by rush_level desc", new String[]{planId+""});
		while(cursor.moveToNext()){
			WorkPlanModle modle = new WorkPlanModle();
			modle.setPlanId(cursor.getInt(cursor.getColumnIndex("plan_id")));
			modle.setPlanTitle(cursor.getString(cursor.getColumnIndex("plan_title")));
			modle.setPlanContent(cursor.getString(cursor.getColumnIndex("plan_content")));
			modle.setPlanMark(cursor.getString(cursor.getColumnIndex("plan_note")));
			modle.setImportantLevel(cursor.getInt(cursor.getColumnIndex("important_level")));
			modle.setRushLevel(cursor.getInt(cursor.getColumnIndex("rush_level")));
			dataList.add(modle);
			
		}
			
			cursor.close();
			return dataList ;
	}
	public List<WorkPlanModle> findWorkPlanModleListById(int planId){
		List<WorkPlanModle> list = new ArrayList<WorkPlanModle>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.WORK_PLAN_MODLE)
		.append(" where plan_id = ").append(planId);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				WorkPlanModle a = putWorkPlanModle(cursor);
				list.add(a);
			}
		}
		cursor.close();
		return list;
	}
	

}
