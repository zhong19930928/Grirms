package com.yunhu.yhshxc.wechat.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yunhu.yhshxc.database.DatabaseHelper;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.wechat.bo.Group;

public class GroupDB {
	private DatabaseHelper openHelper;
	private Context context;
	public GroupDB(Context context) {
		this.context = context;
		openHelper = DatabaseHelper.getInstance(context);
	}
	
	
	public void insert(Group group){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues cv = putContentValues(group);
		db.insert(openHelper.GROUP, null, cv);
	}
	
	public List<Group> findGroupList(){
		List<Group> list = new ArrayList<Group>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.GROUP).append(" where createUserId = ").append(SharedPreferencesUtil.getInstance(context).getUserId());
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor!=null && cursor.getCount()>0) {
			while (cursor.moveToNext()) {
				Group a = putGroup(cursor);
				list.add(a);
			}
		}
		cursor.close();
		return list;
	}
	
	public Group findGroupByGroupId(int groupId){
		Group group = null;
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(openHelper.GROUP).append(" where groupId = ").append(groupId);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor!=null) {
			if (cursor.moveToNext()) {
				group = putGroup(cursor);
			}
			cursor.close();
		}
		return group;
	}
	public void delete(int groupId){
		openHelper.delete(openHelper.GROUP, "groupId=?",new String[]{String.valueOf(groupId)});
	}
	
	public void deleteAll(){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		StringBuffer sql=new StringBuffer();
		sql.append("delete from ").append(openHelper.GROUP);
		db.execSQL(sql.toString());
		
	}
	
	
	
	public void updateGroup(Group group){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		db.update(openHelper.GROUP, putContentValues(group), "groupId="+group.getGroupId(), null);
	}
	private ContentValues putContentValues(Group group) {
		ContentValues cv = new ContentValues();
		cv.put("groupId", group.getGroupId());
		cv.put("groupName",group.getGroupName());
		cv.put("explain", group.getExplain());
		cv.put("groupUser",group.getGroupUser());
		cv.put("orgId",group.getOrgId());
		cv.put("orgCode",group.getOrgCode());
		cv.put("logo",group.getLogo());
		cv.put("type", group.getType());
		cv.put("groupRole", group.getGroupRole());
		cv.put("createUserId", group.getCreateUserId());
		return cv;
	}
	
	private Group putGroup(Cursor cursor) {
		int i = 0;
		Group  module = new Group();
		module.setId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		module.setGroupId(cursor.getInt(i++));
		module.setGroupName(cursor.getString(i++));
		module.setExplain(cursor.getString(i++));
		module.setGroupUser(cursor.getString(i++));
		module.setOrgId(cursor.getString(i++));
		module.setOrgCode(cursor.getString(i++));
		module.setLogo(cursor.getString(i++));
		module.setType(cursor.getInt(i++));
		module.setGroupRole(cursor.getString(i++));
		module.setCreateUserId(cursor.getInt(i++));
		
		return module;
	}
	
}
