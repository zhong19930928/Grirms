package com.yunhu.yhshxc.wechat.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yunhu.yhshxc.database.DatabaseHelper;
import com.yunhu.yhshxc.wechat.bo.TopicNotify;

public class TopicNotifyDB {
	private DatabaseHelper openHelper;
	
	public TopicNotifyDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}
	
	
	public void insert(TopicNotify topic){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues cv = putContentValues(topic);
		db.insert(openHelper.TOPIC_NOTIFY, null, cv);
	}
	
	public List<TopicNotify> findTopicNotifyList(){
		List<TopicNotify> list = new ArrayList<TopicNotify>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.TOPIC_NOTIFY).append(" where 1=1");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor!=null && cursor.getCount()>0) {
			while (cursor.moveToNext()) {
				TopicNotify a = putTopicNotify(cursor);
				list.add(a);
			}
			cursor.close();
		}
		return list;
	}
	
	public void deleteAll(){
		String sql = "delete from "+openHelper.TOPIC_NOTIFY;
		openHelper.execSQL(sql);
	}
	
	
	
	private ContentValues putContentValues(TopicNotify topic) {
		ContentValues cv = new ContentValues();
		cv.put("topicId", topic.getTopicId());
		cv.put("content",topic.getContent());
		cv.put("date", topic.getDate());
		return cv;
	}
	
	private TopicNotify putTopicNotify(Cursor cursor) {
		int i = 0;
		TopicNotify module = new TopicNotify();
		module.setId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		module.setTopicId(cursor.getInt(i++));
		module.setContent(cursor.getString(i++));
		module.setDate(cursor.getString(i++));
		return module;
	}
	
}
