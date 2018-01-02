package com.yunhu.yhshxc.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yunhu.yhshxc.bo.TableStencil;

/**
 * 表格模板数据库
 * @author gcg_jishen
 *
 */
public class TableStencilDB {
	private DatabaseHelper openHelper;
	public TableStencilDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}
	/**
	 * 插入
	 * @param TableStencil
	 * @return
	 */
	public Long insertTableStencil(TableStencil tableStencil) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues cv = putContentValues(tableStencil);
		Long id = db.insert(openHelper.TABLE_STENCIL, null, cv);
		return id;
	}

	/**
	 * 根据数据id和控件id删除
	 * @param targetid
	 * @param funcid
	 * @return
	 */
	public int removeTableStencil(int targetid, int funcid) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		int id = db.delete(openHelper.TABLE_STENCIL,
				"targetid=? and funcid=?", new String[] {
						String.valueOf(targetid), String.valueOf(funcid)});
		
		return id;
	}
	
	/**
	 * 修改
	 * @param visitStore
	 * @return
	 */
	public int updateTableStencil(TableStencil tableStencil) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues cv = putContentValues(tableStencil);
		int id = db.update(openHelper.TABLE_STENCIL, cv, "targetid=? and funcid=?",
				new String[] { String.valueOf(tableStencil.getTargetId()), String.valueOf(tableStencil.getFuncId()) });
		
		return id;
	}
	
	/**
	 * 查找
	 * @param targetId 数据ID
	 * @return
	 */
	public TableStencil findTableStencilByTargetIdAndFuncId(int targetId,int funcId) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		TableStencil tableStencil=null;
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.TABLE_STENCIL).append(" where targetid=").append(targetId);
		sql.append(" and funcid = ").append(funcId);
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				tableStencil =putTableStencil(cursor);
			}
		}
		cursor.close();
		return tableStencil;
	}
	
	private ContentValues putContentValues(TableStencil tableStencil){
		ContentValues cv = new ContentValues();
		cv.put("funcid", tableStencil.getFuncId());
		cv.put("targetid", tableStencil.getTargetId());
		cv.put("value", tableStencil.getValue());
		return cv;
	}
	
	private TableStencil putTableStencil(Cursor cursor){
		int i = 0;
		TableStencil tableStencil = new TableStencil();
		tableStencil.setId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		tableStencil.setTargetId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		tableStencil.setFuncId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		tableStencil.setValue(cursor.getString(i++));
		return tableStencil;
	}
}
