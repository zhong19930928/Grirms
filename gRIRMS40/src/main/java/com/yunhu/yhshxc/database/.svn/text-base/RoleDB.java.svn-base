package com.yunhu.yhshxc.database;

import gcg.org.debug.JLog;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.yunhu.yhshxc.bo.Role;
import com.yunhu.yhshxc.utility.PublicUtils;

public class RoleDB {
	private String TAG = "RoleDB";

	private DatabaseHelper openHelper;

	public RoleDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}
	
	
	public void insertRole(Role role){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues cv = putContentValues(role);
		db.insert(openHelper.ROLE_TABLE, null, cv);
		
	}
	
	public List<Role> findAllRole(String fuzzySearch,String queryWhereForDid){
		List<Role> roleList = new ArrayList<Role>();
		SQLiteDatabase db = openHelper.getReadableDatabase();
		StringBuffer sql=new StringBuffer();
		sql.append("select * from "+openHelper.ROLE_TABLE).append(" where 1=1 ");
		if(!TextUtils.isEmpty(fuzzySearch)){
			sql.append(" and name ").append(" like ").append("'%").append(fuzzySearch).append("%'");
		}
		
		if(!TextUtils.isEmpty(queryWhereForDid)){
			sql.append(" and ").append("did in (").append(queryWhereForDid).append(")");
		}
		
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				roleList.add(putRole(cursor));
			}
		}
		cursor.close();
		Collections.sort(roleList,comparator);
		return roleList;
	}
	Comparator<Role> comparator =new Comparator<Role>() {
		
		private Collator collator = Collator.getInstance(java.util.Locale.CHINA);
		@Override
		public int compare(Role lhs, Role rhs) {
			if (lhs == null || rhs == null ||  TextUtils.isEmpty(lhs.getName()) || TextUtils.isEmpty(rhs.getName())) {
				return 1;
			}
			return collator.compare(lhs.getName(), rhs.getName());
		}
	};
	private ContentValues putContentValues(Role role) {
		ContentValues cv = new ContentValues();
		cv.put("roleId", role.getRoleId());
		cv.put("name", role.getName());
		cv.put("level", role.getLevel());
		return cv;
	}
	
	private Role putRole(Cursor cursor) {
		int i = 0;
		Role role = new Role();
		role.setId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		role.setRoleId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		role.setName(cursor.getString(i++));
		role.setLevel(cursor.getString(i++));
		return role;
	}
	
	
	public String findDictMultiChoiceValueStr(String idStr,String fuzzySearch){
		String result = "";
		if(PublicUtils.isIntegerArray(idStr)){
			SQLiteDatabase db = openHelper.getReadableDatabase();
			StringBuffer sql = new StringBuffer();
			sql.append("select ").append(" name ").append(" from ").append(openHelper.ROLE_TABLE);
			sql.append(" where ").append(" roleId ").append(" in (").append(idStr).append(")");
			if(!TextUtils.isEmpty(fuzzySearch)){
				sql.append(" and name ").append(" like ").append("'%").append(fuzzySearch).append("%'");
			}
			Cursor cursor = db.rawQuery(sql.toString(), null);
			StringBuffer sb = new StringBuffer();
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					sb.append(",").append(cursor.getString(0));
				}
			}
			cursor.close();
			
			if(sb.length() > 0){
				result = sb.substring(1);
			}
			JLog.d(TAG, "findDictMultiChoiceValueStr==>"+sql.toString());
		}
			
		return result;
	}
	
	public void removeByRoleId(String roleId){
		if(PublicUtils.isInteger(roleId)){
			SQLiteDatabase db = openHelper.getWritableDatabase();
			db.delete(openHelper.ROLE_TABLE, "roleId=?", new String[]{roleId});
		}
	}
}
