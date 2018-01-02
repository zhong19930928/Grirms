package com.yunhu.yhshxc.wechat.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.yunhu.yhshxc.database.DatabaseHelper;
import com.yunhu.yhshxc.wechat.bo.Notification;

public class NotificationDB {
	private DatabaseHelper openHelper;
	
	public NotificationDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}
	
	
	public void insert(Notification topic){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues cv = putContentValues(topic);
		db.insert(openHelper.NOTIFICATION, null, cv);
	}
	/**
	 * 查找所有通知的未读消息个数
	 * @param replyId
	 * @param pathCode
	 */
	public int findAllNoticeNum(){
		SQLiteDatabase db = openHelper.getReadableDatabase();
		int count = 0;
		StringBuffer sql = new StringBuffer(" select count(*) from ").append(openHelper.NOTIFICATION);
		sql.append(" where isRead = 0 ");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				count = cursor.getInt(0);
			}
		}
		cursor.close();
		return count;
	}
	public List<Notification> findNotificationList(){
		List<Notification> list = new ArrayList<Notification>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.NOTIFICATION).append(" where 1=1 order by createDate desc");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor!=null && cursor.getCount()>0) {
			while (cursor.moveToNext()) {
				Notification a = putNotification(cursor);
				list.add(a);
			}
		}
		cursor.close();
		return list;
	}
	public Notification findNotifacationById(int noticeId){
		Notification notification = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.NOTIFICATION).append(" where noticeId = ").append(noticeId);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor!=null) {
			if (cursor.moveToNext()) {
				notification = putNotification(cursor);
			}
			cursor.close();
		}
		return notification;
	}
	/**
	 * 查询关注的通知
	 * @param noticeId
	 */
	public List<Notification> findNotifacationListByIsNoticed(){
		List<Notification> list = new ArrayList<Notification>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.NOTIFICATION).append(" where isNoticed = 1");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor!=null && cursor.getCount()>0) {
			while (cursor.moveToNext()) {
				Notification a = putNotification(cursor);
				list.add(a);
			}
		}
		cursor.close();
		return list;
	}
	public void deleteNotification(int noticeId) {
		openHelper.delete(openHelper.NOTIFICATION, "noticeId=?",
				new String[] { String.valueOf(noticeId) });
	}
	public void updateNotification(Notification topic) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		db.update(openHelper.NOTIFICATION, putContentValues(topic),
				"noticeId=" + topic.getNoticeId(), null);
	}
	
	public void deleteAll(){
		String sql = "delete from "+openHelper.NOTIFICATION;
		openHelper.execSQL(sql);
	}
	
	
	private ContentValues putContentValues(Notification topic) {
		ContentValues cv = new ContentValues();
		cv.put("noticeId", topic.getNoticeId());
		cv.put("title", topic.getTitle());
		cv.put("content", topic.getContent());
		cv.put("from_",topic.getFrom());
		cv.put("to_", topic.getTo());
		cv.put("attachment", topic.getAttachment());
		cv.put("peoples", topic.getPeoples());
		cv.put("orgId", topic.getOrgId());
		cv.put("orgCode", topic.getOrgCode());
		cv.put("creater", topic.getCreater());
		cv.put("createOrg", topic.getCreateOrg());
		cv.put("createDate", topic.getCreateDate());
		cv.put("role", topic.getRole());
		cv.put("users", topic.getUsers());
		cv.put("isAttach",topic.getIsAttach());
		cv.put("isNoticed",topic.getIsNoticed());
		cv.put("isRead",topic.getIsRead());
		return cv;
	}
	
	private Notification putNotification(Cursor cursor) {
		int i = 0;
		Notification module = new Notification();
		module.setId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		module.setNoticeId(cursor.getInt(i++));
		module.setTitle(cursor.getString(i++));
		module.setContent(cursor.getString(i++));
		module.setFrom(cursor.getString(i++));
		module.setTo(cursor.getString(i++));
		module.setAttachment(cursor.getString(i++));
		module.setPeoples(cursor.getString(i++));
		module.setOrgId(cursor.getInt(i++));
		module.setOrgCode(cursor.getString(i++));
		module.setCreater(cursor.getString(i++));
		module.setCreateOrg(cursor.getString(i++));
		module.setCreateDate(cursor.getString(i++));
		module.setRole(cursor.getString(i++));
		module.setUsers(cursor.getString(i++));
		module.setIsAttach(cursor.getString(i++));
		
		String isNoticed = cursor.getString(i++);
		if(TextUtils.isEmpty(isNoticed)){
			isNoticed = "0";
		}else{
			if(isNoticed.equals("1")){
				isNoticed = "1";
			}else{
				isNoticed = "0";
			}
		}
		module.setIsNoticed(isNoticed);
		module.setIsRead(cursor.getString(i++));
		
		return module;
	}
	
}
