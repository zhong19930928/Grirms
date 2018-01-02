package com.yunhu.yhshxc.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yunhu.yhshxc.bo.Content;
import com.yunhu.yhshxc.bo.DoubleTask;
import com.yunhu.yhshxc.doubletask.DoubleTaskManagerDB;
import com.yunhu.yhshxc.parser.CacheData;

public class DoubleDB {

	private DatabaseHelper openHelper;

	private String TAG = "DoubleDB";
	private Context context;

	public DoubleDB(Context context) {
		this.context = context;
		openHelper = DatabaseHelper.getInstance(context);
	}

	/**
	 * 动态创建Table(先删除table，在创建table)
	 * 
	 * @param table
	 * @param columns
	 *            字段数组
	 */
	public void createDoubleTable(String table, String[] columns) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		StringBuffer sql = new StringBuffer();
		// 删除已存在的table
		sql.append("DROP TABLE IF EXISTS ").append(openHelper.DOUBLE_TABLE)
				.append(table);
		db.execSQL(sql.toString());
		// 创建新的Table
		sql = new StringBuffer();
		sql.append("CREATE TABLE IF NOT EXISTS ")
				.append(openHelper.DOUBLE_TABLE).append(table);
		sql.append("(id INTEGER primary key autoincrement not null");
		for (String col : columns) {
			sql.append(",").append(col).append(" VARCHAR");
		}
		sql.append(")");
		db.execSQL(sql.toString());
		
	}
	
	/**
	 * 动态创建Table(如果table不存在就不创建table)
	 * 
	 * @param table
	 * @param columns 字段数组
	 */
	public void createDoubleTable2(String table, String[] columns) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		StringBuffer sql = new StringBuffer();
		// 创建新的Table
		sql = new StringBuffer();
		sql.append("CREATE TABLE IF NOT EXISTS ")
				.append(openHelper.DOUBLE_TABLE).append(table);
		sql.append("(id INTEGER primary key autoincrement not null");
		for (String col : columns) {
			sql.append(",").append(col).append(" VARCHAR");
		}
		sql.append(")");
		db.execSQL(sql.toString());
		
	}

	/**
	 * 为动态table插入动态数据
	 * 
	 * @param dict
	 * @return
	 */
	public Long insert(List<Content> list, String table) {
		int taskId = 0;
		for (int i = 0; i < list.size(); i++) {
			Content c = list.get(i);
			if ("taskid".equals(c.getKey())) {
				taskId = Integer.parseInt(c.getValue());
				break;
			}
		}
		DoubleTaskManagerDB dbs = new DoubleTaskManagerDB(context);
		boolean isHas = dbs.isHasDoubleTaskManager(Integer.parseInt(table), taskId);
		if (!isHas) {
			SQLiteDatabase db = openHelper.getWritableDatabase();
			ContentValues cv = new ContentValues();
			if (list != null) {
				for (Content content : list) {
					cv.put(content.getKey(), content.getValue() == null ? "" : content.getValue());
				}
				Long id = db.insert(openHelper.DOUBLE_TABLE + table, null, cv);
				
				return id;
			} else {
				return null;
			}
	}else{
		return null;
	}
//		SQLiteDatabase db = openHelper.getWritableDatabase();
//		ContentValues cv = new ContentValues();
//		if (list != null) {
//			for (Content content : list) {
//				cv.put(content.getKey(), content.getValue() == null ? "" : content.getValue());
//			}
//			Long id = db.insert(openHelper.DOUBLE_TABLE + table, null, cv);
//			
//			return id;
//		} else {
//			
//			return null;
//		}
	}
	
	/**
	 * 查询找双向任务
	 * @param firstColumn  查找列
	 * @param targetId 数据ID
	 * @return
	 */
	public List<DoubleTask> findDoubleTaskList(String firstColumn,Integer targetId){
		List<DoubleTask> list = new ArrayList<DoubleTask>();
		SQLiteDatabase db = openHelper.getReadableDatabase();
		StringBuffer sql = new StringBuffer();
		sql.append("select ").append(CacheData.DATA_N).append(firstColumn).append(",").append(CacheData.DOUBLE_COLUMN_TASKID).append(",").append(CacheData.DOUBLE_COLUMN_TIMESTAMP).append(",").append(CacheData.DOUBLE_COLUMN_CREATERUSER);
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
				list.add(doubleTask);
			}
		}
//		JLog.d(TAG, "findDoubleTaskList==>"+sql.toString());
		cursor.close();
		
		return list;
	}
	
	
	/**
	 * 根据数据ID和任务ID查找双向内容返回map key是data_+控件ID value是值
	 * @param taskId 任务ID
	 * @param targetId 数据ID
	 * @return
	 */
	public Map<String,String> findDoubleTask(String taskId,Integer targetId){
		SQLiteDatabase db = openHelper.getReadableDatabase();
		Map<String,String> hashMap = new HashMap<String,String>();
		StringBuffer sql = new StringBuffer();
		sql.append("select *").append(" from ").append(openHelper.DOUBLE_TABLE).append(targetId);
		sql.append(" where ").append(CacheData.DOUBLE_COLUMN_TASKID).append("=").append(taskId);
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor.getCount() > 0) {
			int size = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				for(int i=0;i<size;i++){
					hashMap.put(cursor.getColumnName(i), cursor.getString(i));
				}
			}
		}
		cursor.close();
		
		return hashMap;
	}
	
	/**
	 * 根据日期.查找日期以前的20条
	 * @param date 日期
	 * @param targetId 数据ID
	 * @return 查找到的双向任务的集合
	 */
	public List<DoubleTask> findDoubleTaskByDate(String date,String targetId) {
		StringBuffer findSql=new StringBuffer();
		findSql.append("select * from ").append(openHelper.DOUBLE_TABLE).append(targetId).append(" where createTime < '").append(date).append("' order by createTime desc limit 20");
		SQLiteDatabase db = openHelper.getReadableDatabase();
		List<DoubleTask> list=new ArrayList<DoubleTask>();
		Cursor cursor = db.rawQuery(findSql.toString(), null);
		DoubleTask doubleTask = null;
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
		cursor.close();
		
		return list;
	}
	
	/**
	 * 删除双向任务
	 * @param taskId 任务ID
	 * @param targetId 数据ID
	 * @return
	 */
	public int removeDoubleTask(String taskId,String targetId) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		int id = db.delete(openHelper.DOUBLE_TABLE+targetId,CacheData.DOUBLE_COLUMN_TASKID+"=?",new String[] {taskId});
		
		return id;
	}
	
	/**
	 * 删除双向任务
	 * @param taskId 任务ID
	 * @param targetId 数据ID
	 * @return
	 */
	public void removeAllDoubleTask(String targetId) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		StringBuffer sql = new StringBuffer();
		sql.append("delete from ").append(openHelper.DOUBLE_TABLE+targetId);
		db.execSQL(sql.toString());
	}
	
	/**
	 * 查找双向任务的数目
	 * @param targetId 数据ID
	 * @return
	 */
	public int findAllDoubleTaskNumber(int targetId) {
		StringBuffer findSql=new StringBuffer();
		findSql.append("select count(*) ");
		findSql.append(" from ").append(openHelper.DOUBLE_TABLE).append(targetId);
		SQLiteDatabase db = openHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(findSql.toString(), null);
		int count = 0;
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				count = cursor.getInt(0);
			}
		}
		cursor.close();
		
		return count;
	}
}
