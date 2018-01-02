package com.yunhu.yhshxc.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.yunhu.yhshxc.bo.Task;

/**
 * 
 * @author jishen 数据库操作
 */
public class TaskDB {

	private String TAG = "TaskDB";
	private DatabaseHelper openHelper;

	public TaskDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}

	/**
	 * @param notify
	 * @return
	 */
	public Long insertTaskData(Task task) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("createUser", task.getCreateUser());
		cv.put("taskTitle", task.getTaskTitle());
		cv.put("detailTask", task.getDetailTask());
		cv.put("createTime", task.getCreateTime());
		cv.put("moduleid", task.getModuleid());
		cv.put("isread", task.getIsread());
		cv.put("taskId", task.getTaskId());
		Long id = db.insert(openHelper.TASK_TABLE, null, cv);
		
		List<Task> taskList = findTaskListByModuleid(task.getModuleid());
		int len = taskList.size()-80;
		if (len>0) {
				Task t = findTaskLastDate();
				deleteTaskById(t.getId());
		}
		Log.d(TAG + "===保存任务==>", "id = " + id);
		
		return id;

	}
	
	public List<Task> findTaskListByModuleid(int moduleid) {
		List<Task> taskList = new ArrayList<Task>();
//		String findSql = "select * from "+ openHelper.TASK_TABLE;
//		String findSql = "select * from " + openHelper.TASK_TABLE +" where moduleid="+moduleid+ " order by createTime desc";
		StringBuffer findSql=new StringBuffer();
		findSql.append("select * from ").append(openHelper.TASK_TABLE).append(" where moduleid=").append(moduleid).append(" order by createTime desc");
		
		SQLiteDatabase db = openHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(findSql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {	
				taskList.add(putTask(cursor));
			}
		}
		cursor.close();
		
		return taskList;
	}
	
	/**
	 * 更新任务阅读状态
	 * @param id DB中自动生成的ID
	 * @param isread 是否阅读
	 */
	public void updateTaskReadStateById(int id,int isread) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		StringBuffer sql = new StringBuffer();
		sql.append("update ").append(openHelper.TASK_TABLE).append(" set isread =").append(isread).append(" where id=").append(id);
		db.execSQL(sql.toString());
		Log.d(TAG + "===修改公任务已阅读==>", "id = " + id);
		
	}

	/**
	 * 
	 * @return
	 */
	public List<Task> findAllTaskByModuleid(int moduleid) {
		List<Task> taskList = new ArrayList<Task>();
//		String findSql = "select * from "+ openHelper.TASK_TABLE;
//		String findSql = "select * from " + openHelper.TASK_TABLE +" where moduleid="+moduleid+ " order by createTime desc limit 20";
		
		StringBuffer findSql=new StringBuffer();
		findSql.append("select * from ").append(openHelper.TASK_TABLE).append(" where moduleid=").append(moduleid).append(" order by createTime desc limit 20");
		SQLiteDatabase db = openHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(findSql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {	
				taskList.add(putTask(cursor));
			}
		}
		cursor.close();
		
		return taskList;
	}
	/**
	 * 
	 * @return
	 */
	public List<Task> findAllTask() {
		List<Task> taskList = new ArrayList<Task>();
//		String findSql = "select * from "+ openHelper.TASK_TABLE;
//		String findSql = "select * from " + openHelper.TASK_TABLE + " order by createTime desc";
		StringBuffer findSql=new StringBuffer();
		findSql.append("select * from ").append(openHelper.TASK_TABLE).append(" order by createTime desc");
		SQLiteDatabase db = openHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(findSql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {	
				taskList.add(putTask(cursor));
			}
		}
		cursor.close();
		
		return taskList;
	}
	
	/**
	 * 
	 * @return查找最新20条公告
	 */
	public List<Task> findTaskByDateAndModuleid(String date,int moduleid) {
//		String findSql = "select * from " + openHelper.TASK_TABLE + " where createTime < '"+date+"' and moduleid="+moduleid+" order by createTime desc limit 20";
	
		StringBuffer findSql=new StringBuffer();
		findSql.append("select * from ").append(openHelper.TASK_TABLE).append(" where createTime < '").append(date);
		findSql.append("' and moduleid=").append(moduleid).append(" order by createTime desc limit 20");
		SQLiteDatabase db = openHelper.getReadableDatabase();
		List<Task> list=new ArrayList<Task>();
		Cursor cursor = db.rawQuery(findSql.toString(), null);
		Task task = null;
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				task = putTask(cursor);
				list.add(task);
			}
		}
		cursor.close();
		
		return list;
	}
	
	public Task findTaskLastDate() {
//		String findSql = "select * from "+ openHelper.TASK_TABLE + " order by createTime desc limit 1";
		StringBuffer findSql=new StringBuffer();
		findSql.append("select * from ").append(openHelper.TASK_TABLE).append(" order by createTime desc limit 1");
		
		Task task = null;
		SQLiteDatabase db = openHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(findSql.toString(), null);
		if (cursor.getCount() > 0) {
			if (cursor.moveToNext()) {	
				task = putTask(cursor);
			}
		}
		cursor.close();
		
		return task;
	}
	
	private Task putTask(Cursor cursor){
		int i = 0;
		Task task = new Task();
		task.setId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		task.setTaskTitle(cursor.getString(i++));
		task.setDetailTask(cursor.getString(i++));
		task.setCreateUser(cursor.getString(i++));
		task.setCreateTime(cursor.getString(i++));
		task.setIsread(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		task.setModuleid(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		task.setTaskId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		return task;
	}

	/**
	 * 
	 * @return
	 */
	public int findAllUnreadTaskNumber(int moduleid) {
//		String findSql = "select count(*) from " + openHelper.TASK_TABLE
//				+ " where isread = 1"+" and moduleid="+moduleid;// 1表示未读
		StringBuffer findSql=new StringBuffer();
		findSql.append("select count(*) from ").append(openHelper.TASK_TABLE).append(" where isread = 1").append(" and moduleid=").append(moduleid);
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

	/**
	 * 通过Id删除信息
	 */
	public void deleteTaskById(int id) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
//		String deleteSql = "delete from " + openHelper.TASK_TABLE
//				+ " where id = " + id;
		
		StringBuffer deleteSql=new StringBuffer();
		deleteSql.append("delete from ").append( openHelper.TASK_TABLE).append(" where id = ").append(id);
		db.execSQL(deleteSql.toString());
		Log.d(TAG+"==delete Task success==>", String.valueOf(id));
		
	}

	public void removeTask(String moduleid,String taskId) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		StringBuffer deleteSql=new StringBuffer();
		deleteSql.append("delete from ").append( openHelper.TASK_TABLE).append(" where moduleid = ").append(moduleid);
		deleteSql.append(" and taskId =").append(taskId);
		db.execSQL(deleteSql.toString());
		
	}
}
