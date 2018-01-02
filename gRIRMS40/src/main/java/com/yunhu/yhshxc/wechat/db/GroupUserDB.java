package com.yunhu.yhshxc.wechat.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yunhu.yhshxc.database.DatabaseHelper;
import com.yunhu.yhshxc.wechat.bo.GroupUser;

public class GroupUserDB {
	private DatabaseHelper openHelper;
	
	public GroupUserDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}
	
	
	public void insert(GroupUser group){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues cv = putContentValues(group);
		db.insert(openHelper.GROUP_USER, null, cv);
	}
	
	public List<GroupUser> findCommentList(){
		List<GroupUser> list = new ArrayList<GroupUser>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.GROUP_USER).append(" where 1=1");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor!=null && cursor.getCount()>0) {
			while (cursor.moveToNext()) {
				GroupUser a = putGroupUser(cursor);
				list.add(a);
			}
			cursor.close();
		}
		return list;
	}
	
	public List<GroupUser> findGroupUserByGroupId(int groupId){
		List<GroupUser> list = new ArrayList<GroupUser>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.GROUP_USER).append(" where groupId = ").append(groupId);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor!=null && cursor.getCount()>0) {
			while (cursor.moveToNext()) {
				GroupUser a = putGroupUser(cursor);
				list.add(a);
			}
			cursor.close();
		}
		return list;
	}
	
//	public GroupUser findGroupUserByGroupId(int groupId,int userId){
//		List<GroupUser> list = new ArrayList<GroupUser>();
//		StringBuffer sql = new StringBuffer();
//		sql.append(" select * from ").append(openHelper.GROUP_USER).append(" where groupId = ").append(groupId);
//		Cursor cursor = openHelper.query(sql.toString(), null);
//		if (cursor!=null && cursor.getCount()>0) {
//			while (cursor.moveToNext()) {
//				GroupUser a = putGroupUser(cursor);
//				list.add(a);
//			}
//			cursor.close();
//		}
//		return list;
//	}
	
	public void deleteAll(){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		StringBuffer sql=new StringBuffer();
		sql.append("delete from ").append(openHelper.GROUP_USER);
		db.execSQL(sql.toString());
		
	}
	
	private ContentValues putContentValues(GroupUser group) {
		ContentValues cv = new ContentValues();
		cv.put("groupId", group.getGroupId());
		cv.put("userId",group.getUserId());
		cv.put("userName",group.getUserName());
		cv.put("photo",group.getPhoto());
		return cv;
	}
	
	private GroupUser putGroupUser(Cursor cursor) {
		int i = 0;
		GroupUser  module = new GroupUser();
		module.setId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		module.setGroupId(cursor.getInt(i++));
		module.setUserId(cursor.getInt(i++));
		module.setUserName(cursor.getString(i++));
		module.setPhoto(cursor.getString(i++));
		return module;
	}
	
}
