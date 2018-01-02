package com.yunhu.yhshxc.wechat.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yunhu.yhshxc.database.DatabaseHelper;
import com.yunhu.yhshxc.wechat.bo.GroupRole;

public class GroupRoleDB {
	private DatabaseHelper openHelper;
	
	public GroupRoleDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}
	
	
	public void insert(GroupRole group){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues cv = putContentValues(group);
		db.insert(openHelper.GROUP_ROLE, null, cv);
	}
	
	public List<GroupRole> findCommentList(){
		List<GroupRole> list = new ArrayList<GroupRole>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.GROUP_ROLE).append(" where 1=1");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor!=null && cursor.getCount()>0) {
			while (cursor.moveToNext()) {
				GroupRole a = putGroupRole(cursor);
				list.add(a);
			}
			cursor.close();
		}
		return list;
	}
	
	
	private ContentValues putContentValues(GroupRole group) {
		ContentValues cv = new ContentValues();
		cv.put("groupId", group.getGroupId());
		cv.put("roleId",group.getRoleId());
		cv.put("roleName",group.getRoleName());
		return cv;
	}
	
	private GroupRole putGroupRole(Cursor cursor) {
		int i = 0;
		GroupRole  module = new GroupRole();
		module.setId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		module.setGroupId(cursor.getInt(i++));
		module.setRoleId(cursor.getInt(i++));
		module.setRoleName(cursor.getString(i++));
		return module;
	}
	
}
