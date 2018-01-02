package com.yunhu.yhshxc.database;

import gcg.org.debug.JLog;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.yunhu.yhshxc.bo.Push;

/**
 * 
 * @author jishen 数据库操作
 */
public class PushDB {
	private String TAG = "PushDB";
	private DatabaseHelper openHelper;

	public PushDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}

	/*
	 * 查询所有Push内容
	 */
	public List<Push> findAllPush() {
		
		List<Push> list = new ArrayList<Push>();
//		String sql = "select * from " + openHelper.PUSH_TABLE;
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.PUSH_TABLE).append(" where isFinish=0 order by id");
		
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putPush(cursor));
			}
		}
		cursor.close();
		
		JLog.d(TAG, "findAllPushSql==>" + sql.toString());
		return list;
	}
	
	/*
	 * 查询所有Push内容
	 */
	public List<Push> findAllPushIsFinish() {
		
		List<Push> list = new ArrayList<Push>();
//		String sql = "select * from " + openHelper.PUSH_TABLE;
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.PUSH_TABLE);
		sql.append(" where isFinish = 1 order by id");
		
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putPush(cursor));
			}
		}
		cursor.close();
		
		JLog.d(TAG, "findAllPushSql==>" + sql.toString());
		return list;
	}
	
	/*
	 * 查询所有Push内容
	 */
	public Push findPush() {
		
		Push push = null;
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.PUSH_TABLE).append(" order by id desc limit 1");
		
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor.getCount() > 0 && cursor.moveToNext()) {
			push = putPush(cursor);
		}
		cursor.close();
		
		JLog.d(TAG, "findPush==>" + sql.toString());
		return push;
	}
	/*
	 * 查询所有Push内容
	 */
	public Push findPushByMsgId(String msgID) {
		
		Push push = null;
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.PUSH_TABLE).append(" where msgId = '").append(msgID).append("'");
		
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor.getCount() > 0 && cursor.moveToNext()) {
			push = putPush(cursor);
		}
		cursor.close();
		
		JLog.d(TAG, "findPushByMsgId==>" + sql.toString());
		return push;
	}
	
	public void updatePush(Push push){
		ContentValues cv=putContentValues(push);
		openHelper.update(openHelper.PUSH_TABLE, cv, "msgId=?", new String[] {push.getMsgId()});
	}
	
	/*
	 * 插入PUSH
	 */
	public void insertPush(Push push) {
		
		ContentValues cv = putContentValues(push);
		openHelper.insert(openHelper.PUSH_TABLE, cv);
		
		JLog.d(TAG, "插入Push");
	}
	
//	public void deletePushById(int id){
//		
//		openHelper.delete(openHelper.PUSH_TABLE, "id=?", new String[]{id+""});
//		
//	}
//	
//	public void deleteIsFinishPush(){
//		StringBuffer sql=new StringBuffer();
//		sql.append(" delete from ").append(openHelper.PUSH_TABLE).append(" where isFinish = 1");
//		openHelper.execSQL(sql.toString());
//		
//	}
//	public void deletePushByMsgId(String msgId){
//		
//		openHelper.delete(openHelper.PUSH_TABLE, "msgId=?", new String[]{msgId});
//		JLog.d(TAG, "删除Push msgID="+msgId);
//	}
	
	private Push putPush(Cursor cursor) {
		int i = 0;
		Push push = new Push();
		push.setId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		push.setCreateDate(cursor.getString(i++));
		push.setMsgId(cursor.getString(i++));
		push.setQueueId(cursor.getString(i++));
		push.setIsFinish(cursor.getInt(i++));
		return push;
	}

	private ContentValues putContentValues(Push push) {
		ContentValues cv = new ContentValues();
		cv.put("createDate", push.getCreateDate());
		cv.put("msgId", push.getMsgId());
		cv.put("queueId", push.getQueueId());
		cv.put("isFinish", push.getIsFinish());
		return cv;
	}

}
