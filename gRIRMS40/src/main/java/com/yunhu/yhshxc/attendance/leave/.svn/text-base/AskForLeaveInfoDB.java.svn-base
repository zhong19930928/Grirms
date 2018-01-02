package com.yunhu.yhshxc.attendance.leave;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yunhu.yhshxc.database.DatabaseHelper;

public class AskForLeaveInfoDB {

	private DatabaseHelper openHelper;

	public AskForLeaveInfoDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}

	public Long insert(AskForLeaveInfo productConf) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		Long id = db.insert(openHelper.AF_LEAVE_INFO, null,putContentValues(productConf));

		return id;

	}
	
	
	public List<AskForLeaveInfo> findAllLeaves(){
		List<AskForLeaveInfo> list = new ArrayList<AskForLeaveInfo>();
		StringBuffer findSql=new StringBuffer();
		findSql.append("select * from ").append(openHelper.AF_LEAVE_INFO).append(" where 1=1");
		Cursor cursor = openHelper.query(findSql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putProductConf(cursor));
			}
		}
		cursor.close();
		return list;
	}
	public AskForLeaveInfo findLeavesById(int id){
		AskForLeaveInfo list = new AskForLeaveInfo();
		StringBuffer findSql=new StringBuffer();
		findSql.append("select * from ").append(openHelper.AF_LEAVE_INFO).append(" where id=").append(id);
		Cursor cursor = openHelper.query(findSql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list = putProductConf(cursor);
			}
		}
		cursor.close();
		return list;
	}
	
	public void updateLeave(AskForLeaveInfo info){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		db.update(openHelper.AF_LEAVE_INFO, putContentValues(info), "id="+info.getId(), null);
	}
	
	/**
	 * 根据msgKey删除单个
	 * @param msgKey
	 */
	public void deleteLeave(String msgKey) {
		openHelper.delete(openHelper.AF_LEAVE_INFO, "msgKey=?",
				new String[] {msgKey });
	}
	public void delete() {
		openHelper.delete(openHelper.AF_LEAVE_INFO, null, null);
	}

	private AskForLeaveInfo putProductConf(Cursor cursor) {
		int i = 0;
		AskForLeaveInfo product = new AskForLeaveInfo();
		product.setId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		product.setUserId(cursor.getString(i++));
		product.setName(cursor.getString(i++));
		product.setStatus(cursor.getString(i++));
		product.setStatusName(cursor.getString(i++));
		product.setMarks(cursor.getString(i++));
		product.setUserName(cursor.getString(i++));
		product.setStartTime(cursor.getString(i++));
		product.setEndTime(cursor.getString(i++));
		product.setImageUrl(cursor.getString(i++));
		product.setDays(cursor.getString(i++));
		product.setHours(cursor.getString(i++));
		product.setLeaveName(cursor.getString(i++));
		product.setType(cursor.getString(i++));
		product.setImageName(cursor.getString(i++));
		product.setMsgKey(cursor.getString(i++));
		product.setAuditComment(cursor.getString(i++));
		product.setLeaveDay(cursor.getString(i++));
		product.setOrgName(cursor.getString(i++));
		return product;
	}

	private ContentValues putContentValues(AskForLeaveInfo productConf) {
		ContentValues cv = new ContentValues();
		cv.put("userId", productConf.getUserId());
		cv.put("name", productConf.getName());
		cv.put("status", productConf.getStatus());
		cv.put("statusName", productConf.getStatusName());
		cv.put("marks", productConf.getMarks());
		cv.put("userName", productConf.getUserName());
		cv.put("startTime", productConf.getStartTime());
		cv.put("endTime", productConf.getEndTime());
		cv.put("imageUrl", productConf.getImageUrl());
		cv.put("days", productConf.getDays());
		cv.put("hours", productConf.getHours());
		cv.put("leaveName", productConf.getLeaveName());
		cv.put("type", productConf.getType());
		cv.put("imageName", productConf.getImageName());
		cv.put("msgKey", productConf.getMsgKey());
		cv.put("auditComment", productConf.getAuditComment());
		cv.put("leaveDay", productConf.getLeaveDay());
		cv.put("orgName", productConf.getOrgName());
		return cv;
	}

}
