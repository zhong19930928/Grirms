package com.yunhu.yhshxc.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.yunhu.yhshxc.bo.PushItem;

import java.util.ArrayList;

import gcg.org.debug.JLog;

/**
 * 
 * @author jishen 数据库操作
 */
public class PushItemDB {
	private String TAG = "PushItemDB";
	private DatabaseHelper openHelper;

	public PushItemDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}
	
	public void insert(PushItem pushItem){
		openHelper.insert(openHelper.PUSH_ITEM_TABLE, putContentValues(pushItem));
	}

	public ArrayList<PushItem> findPushItemByMsgId(String msgId){
		ArrayList<PushItem> list = new ArrayList<PushItem>();
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.PUSH_ITEM_TABLE).append(" where msgId='").append(msgId).append("' order by id");
		
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putPushItem(cursor));
			}
		}
		cursor.close();
		
		JLog.d(TAG, "findAllPushSql==>" + sql.toString());
		return list;
	}
	/**
	 * 从消息条目数据库中删除指定消息
	 * @param msgId
	 */
	public void delete(String msgId){
	
		openHelper.execSQL("delete from "+openHelper.PUSH_ITEM_TABLE+" where msgId= '"+msgId+"'");
	}
	public ArrayList<PushItem> findAllPushItem(){
		ArrayList<PushItem> list = new ArrayList<PushItem>();
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.PUSH_ITEM_TABLE)
//		.append(" where msgId='").append(msgId)
		.append(" order by id");
		
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putPushItem(cursor));
			}
		}
		cursor.close();
		
		JLog.d(TAG, "findAllPushSql==>" + sql.toString());
		return list;
	}
	
	public ArrayList<PushItem> findAllPushItemByType(String str){
		ArrayList<PushItem> list = new ArrayList<PushItem>();
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.PUSH_ITEM_TABLE).append(" where type in (").append(str).append(")")
//		.append(" where msgId='").append(msgId)
		.append(" order by id");
		
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putPushItem(cursor));
			}
		}
		cursor.close();
		
		JLog.d(TAG, "findAllPushSql==>" + sql.toString());
		return list;
	}
	
//	public void deletePushItemByMsgId(String msgId){
//		
//		openHelper.delete(openHelper.PUSH_ITEM_TABLE, "msgId=?", new String[]{msgId});
//		JLog.d(TAG, "删除Push PushItem="+msgId);
//	}
//	
//	public void deletePushItemById(int id){
//		
//		openHelper.delete(openHelper.PUSH_ITEM_TABLE, "id=?", new String[]{String.valueOf(id)});
//		JLog.d(TAG, "删除PushItem id="+id);
//	}

	private PushItem putPushItem(Cursor cursor) {
		int i = 0;
		PushItem push = new PushItem();
		push.setId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		push.setType(cursor.getInt(i++));
		push.setContent(cursor.getString(i++));
		push.setStatus(cursor.getString(i++));
		push.setMsgId(cursor.getString(i++));
		return push;
	}

	private ContentValues putContentValues(PushItem item) {
		ContentValues cv = new ContentValues();
		cv.put("type", item.getType());
		cv.put("msgId", item.getMsgId());
		cv.put("content", item.getContent());
		cv.put("status", item.getStatus());
		return cv;
	}

}
