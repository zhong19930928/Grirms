package com.yunhu.yhshxc.database;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yunhu.yhshxc.bo.DoubleTask;
import com.yunhu.yhshxc.parser.CacheData;

public class NewDoubleDB {

	private DatabaseHelper openHelper;

	private String TAG = "NewDoubleDB";

	public NewDoubleDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}
	
	/**
	 * 查询找双向任务
	 * @param firstColumn  查找列
	 * @param targetId 数据ID
	 * @return
	 */
	public List<DoubleTask> findNewDoubleTaskList(String firstColumn,Integer targetId){
		List<DoubleTask> list = new ArrayList<DoubleTask>();
		SQLiteDatabase db = openHelper.getReadableDatabase();
		StringBuffer sql = new StringBuffer();
		sql.append("select ").append(CacheData.DATA_N).append(firstColumn).append(",").append(CacheData.DOUBLE_COLUMN_TASKID).append(",").append(CacheData.DOUBLE_COLUMN_TIMESTAMP).append(",").append(CacheData.DOUBLE_COLUMN_CREATERUSER);
		sql.append(",").append(CacheData.DOUBLE_COLUMN_TASK_NO).append(",").append(CacheData.DOUBLE_COLUMN_DATA_STATUS).append(",").append(CacheData.DOUBLE_COLUMN_TATOL).append(",").append(CacheData.DOUBLE_COLUMN_CUT);
		sql.append(" from ").append(openHelper.DOUBLE_TABLE).append(targetId);
		sql.append(" order by ").append(CacheData.DOUBLE_COLUMN_TIMESTAMP).append(" desc");
		DoubleTask doubleTask = null;
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				int i=0;
				doubleTask = new DoubleTask();
				doubleTask.setFirstColumn(cursor.getString(i++));
				doubleTask.setTaskId(cursor.getInt(i++));
				doubleTask.setCreateTime(cursor.getString(i++));
				doubleTask.setCreateUser(cursor.getString(i++));
				doubleTask.setTaskNo(cursor.getString(i++));
				doubleTask.setDataStatus(cursor.getString(i++));
				doubleTask.setTatol(cursor.getString(i++));
				doubleTask.setCut(cursor.getString(i++));
				list.add(doubleTask);
			}
		}
//		JLog.d(TAG, "findNewDoubleTaskList==>"+sql.toString());
		cursor.close();
		
		return list;
	}
	
}
