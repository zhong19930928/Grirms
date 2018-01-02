package com.yunhu.yhshxc.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yunhu.yhshxc.bo.Module;
import com.yunhu.yhshxc.utility.PublicUtils;

public class ModuleDB {
	private String TAG = "ModuleDB";

	private DatabaseHelper openHelper;

	public ModuleDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}
	
	
	/**
	 * 插入
	 * @param module
	 */
	public void insertModule(Module module){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues cv = putContentValues(module);
		db.insert(openHelper.MODULE_TABLE, null, cv);
//		JLog.d(TAG, "insertModule==>"+module.getName());
		
	}
	
	/**
	 * 根据menuid查找按类型排序
	 * @param menuId 
	 * @return
	 */
	public List<Module> findModuleByMenuId(int menuId){
		SQLiteDatabase db = openHelper.getReadableDatabase();
		StringBuffer sql = new StringBuffer();
		List<Module> list=new ArrayList<Module>();
		sql.append("select * from ").append(openHelper.MODULE_TABLE);
		sql.append(" where menuId=").append(menuId).append(" order by type");
		Cursor cursor = db.rawQuery(sql.toString(), null);
//		JLog.d(TAG, "findModuleByMenuId==>" + sql.toString());
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putModule(cursor));
			}
		}
		cursor.close();
		
		return list;
	}
	/**
	 * 根据类型和menuid查找
	 * @param menuId 
	 * @param type
	 * @return
	 */
	public Module findModuleByTargetId(int menuId,int type){
		SQLiteDatabase db = openHelper.getReadableDatabase();
		StringBuffer sql = new StringBuffer();
		Module module=null;
		sql.append("select * from ").append(openHelper.MODULE_TABLE);
		sql.append(" where menuId=").append(menuId).append(" and type= ").append(type);
		Cursor cursor = db.rawQuery(sql.toString(), null);
//		JLog.d(TAG, "findModuleByTargetId==>" + sql.toString());
		if (cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				module=putModule(cursor);
			}
		}
		cursor.close();
		
		return module;
	}
	
	
	private ContentValues putContentValues(Module module) {
		ContentValues cv = new ContentValues();
		cv.put("menuId", module.getMenuId());
		cv.put("name", module.getName());
		cv.put("type", module.getType());
		cv.put("auth", module.getAuth());
		cv.put("auth_org_id", module.getAuthOrgId());
		cv.put("org_code", module.getOrgCode());
		cv.put("is_cancel", module.getIsCancel());
		cv.put("phoneTaskFuns", module.getPhoneTaskFuns());
		cv.put("dynamicStatus", module.getDynamicStatus());
		cv.put("is_report_task", module.getIsReportTask());
		return cv;
	}
	
	private Module putModule(Cursor cursor) {
		int i = 0;
		Module module = new Module();
		module.setId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		module.setMenuId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		module.setType(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		module.setName(cursor.getString(i++));
		module.setAuth(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		module.setAuthOrgId(cursor.getString(i++));
		module.setOrgCode(cursor.getString(i++));
		module.setIsCancel(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		module.setPhoneTaskFuns(cursor.getString(i++));
		module.setDynamicStatus(cursor.getString(i++));
		module.setIsReportTask(cursor.getString(i++));
		return module;
	}
	
	/**
	 * 删除多个 menuid下的模块
	 * @param menuIdStr
	 */
	public void removeModule(String menuIdStr) {
		if(PublicUtils.isIntegerArray(menuIdStr)){
			SQLiteDatabase db = openHelper.getWritableDatabase();
			String sql = "delete from "+openHelper.MODULE_TABLE+" where menuId in ("+menuIdStr+")";
			db.execSQL(sql);
		}
		
	}
	
	/**
	 * 删除menuid下的所有模块
	 * @param menuId
	 */
	public void removeModule(Integer menuId) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		db.delete(openHelper.MODULE_TABLE, "menuId=?", new String[]{menuId+""});
		
	}
}
