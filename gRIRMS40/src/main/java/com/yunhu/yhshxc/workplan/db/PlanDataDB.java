package com.yunhu.yhshxc.workplan.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yunhu.yhshxc.database.DatabaseHelper;
import com.yunhu.yhshxc.workplan.bo.PlanData;

public class PlanDataDB {

	private DatabaseHelper openHelper;

	public PlanDataDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}

	public void insertPlanData(PlanData contacts) {
		ContentValues cv = putContentValues(contacts);
		if (contacts != null && contacts.getPlanId() > 0) {
			openHelper.insert(openHelper.PLAN_DATA, cv);
		}
	}

	public void clearPlanDataList() {
		StringBuffer sql = new StringBuffer();
		sql.append("delete from ").append(openHelper.PLAN_DATA);
		openHelper.execSQL(sql.toString());
	}

	private PlanData putPlanData(Cursor cursor) {
		int i = 1;
		PlanData contacts = new PlanData();
		contacts.setPlanId(cursor.getInt(i++));
		contacts.setUserName(cursor.getString(i++));
		contacts.setUserCode(cursor.getString(i++));
		contacts.setOrgName(cursor.getString(i++));
		contacts.setPlanTitle(cursor.getString(i++));
		contacts.setPlanData(cursor.getString(i++));
		contacts.setStartDate(cursor.getString(i++));
		contacts.setEndDate(cursor.getString(i++));
		return contacts;
	}

	private ContentValues putContentValues(PlanData contact) {
		ContentValues cv = new ContentValues();
		cv.put("plan_id", contact.getPlanId());
		cv.put("userName", contact.getUserName());
		cv.put("userCode", contact.getUserCode());
		cv.put("orgName", contact.getOrgName());
		cv.put("planTitle", contact.getPlanTitle());
		cv.put("planData", contact.getPlanData());
		cv.put("startDate", contact.getStartDate());
		cv.put("endDate", contact.getEndDate());
		return cv;
	}

	/**
	 * 根据plan_id查找一条对应的PlanData数据
	 */
	public PlanData checkPlanDataById(int planID) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + openHelper.PLAN_DATA + " where plan_id = ?",
				new String[] { planID + "" });
		PlanData planD = new PlanData();
		while (cursor.moveToNext()) {
			planD =putPlanData(cursor);
		}
		cursor.close();
		return planD;
	}
	
	/**
	 * 根据plan_id查询对应数据的时间
	 */
	public String getStartDate(int planId){
		SQLiteDatabase db = openHelper.getReadableDatabase();
		String dateStr = "";
		Cursor cursor = db.rawQuery("select * from " + openHelper.PLAN_DATA + " where plan_id = ?",
				new String[] { planId + "" });
		
		while (cursor.moveToNext()) {
			dateStr = cursor.getString(cursor.getColumnIndex("startDate"));
		}
		cursor.close();
	  return dateStr;
		
		
	}
	
	public List<PlanData> findPlanDataListByDate(){
		List<PlanData> list = new ArrayList<PlanData>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.PLAN_DATA)
		.append(" where 1=1 ").append(" order by startDate desc");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				PlanData a = putPlanData(cursor);
				list.add(a);
			}
		}
		cursor.close();
		return list;
	}
	
   /**
    * 根据plan_id查询出对应的user_code
    */
	
	public String getUserCodeByPlanId(int planId){
		SQLiteDatabase db = openHelper.getReadableDatabase();
		String userCode = "";
		Cursor cursor = db.rawQuery("select * from " + openHelper.PLAN_DATA + " where plan_id = ?",
				new String[] { planId + "" });
		
		while (cursor.moveToNext()) {
			userCode = cursor.getString(cursor.getColumnIndex("userCode"));
		}
		cursor.close();
	  return userCode;
		
	}
	
	
	/**
	 * 根据开始日期查询对应的planId
	 */
	public int  getWorkPlanIdByStartDate(String startDate){
		SQLiteDatabase db = openHelper.getReadableDatabase();
		int planId = -1;
		Cursor cursor = db.rawQuery("select * from " + openHelper.PLAN_DATA + " where startDate = ?",
				new String[] {startDate});
		
		while (cursor.moveToNext()) {
			planId = cursor.getInt(cursor.getColumnIndex("plan_id"));
		}
		cursor.close();
	  return planId;
		
		
	}
}
