package com.yunhu.yhshxc.wechat.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yunhu.yhshxc.database.DatabaseHelper;
import com.yunhu.yhshxc.wechat.bo.Attention;

public class AttentionDB {
	private DatabaseHelper openHelper;
	
	public AttentionDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}
	
	
	public void insert(Attention attention){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues cv = putContentValues(attention);
		db.insert(openHelper.ATTENTION, null, cv);
	}
	
	public List<Attention> findAttentionList(){
		List<Attention> list = new ArrayList<Attention>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.ATTENTION).append(" where 1=1");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor!=null && cursor.getCount()>0) {
			while (cursor.moveToNext()) {
				Attention a = putAttention(cursor);
				list.add(a);
			}
			cursor.close();
		}
		return list;
	}
	
	public List<Attention> findAttentionListByType(int type){
		List<Attention> list = new ArrayList<Attention>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.ATTENTION).append(" where type = ").append(type);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor!=null && cursor.getCount()>0) {
			while (cursor.moveToNext()) {
				Attention a = putAttention(cursor);
				list.add(a);
			}
			cursor.close();
		}
		return list;
	}
	
	public void deleteAll(){
		String sql = "delete from "+openHelper.ATTENTION;
		openHelper.execSQL(sql);
	}
	
	
	private ContentValues putContentValues(Attention attention) {
		ContentValues cv = new ContentValues();
		cv.put("attentionId", attention.getAttentionId());
		cv.put("userId",attention.getUserId());
		cv.put("type", attention.getType());
		cv.put("topicId",attention.getTopicId());
		cv.put("noticeId", attention.getNotifyId());
		cv.put("date", attention.getDate());
		return cv;
	}
	
	private Attention putAttention(Cursor cursor) {
		int i = 0;
		Attention module = new Attention();
		module.setId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		module.setAttentionId(cursor.getInt(i++));
		module.setType(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		module.setTopicId(cursor.getInt(i++));
		module.setNotifyId(cursor.getInt(i++));
		module.setDate(cursor.getString(i++));
		return module;
	}
	
}
