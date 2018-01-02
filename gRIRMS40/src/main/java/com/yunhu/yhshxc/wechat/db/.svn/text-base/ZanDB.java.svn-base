package com.yunhu.yhshxc.wechat.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yunhu.yhshxc.database.DatabaseHelper;
import com.yunhu.yhshxc.wechat.bo.Zan;

public class ZanDB {
	private DatabaseHelper openHelper;

	public ZanDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}

	public void insert(Zan zan) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues cv = putContentValues(zan);
		db.insert(openHelper.ZAN, null, cv);
	}

	public List<Zan> findZanList() {
		List<Zan> list = new ArrayList<Zan>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.ZAN)
				.append(" where 1=1");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				Zan a = putZan(cursor);
				list.add(a);
			}
			cursor.close();
		}
		return list;
	}

	// 查询某话题某回帖下点赞

	public List<Zan> findZanListByReply(int topicId, int replayId) {
		List<Zan> list = new ArrayList<Zan>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.ZAN)
				.append(" where topicId = ").append(topicId)
				.append(" and replayId = ").append(replayId).append(" order by date desc limit 10");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				Zan a = putZan(cursor);
				list.add(a);
			}

		}
		cursor.close();
		return list;
	}
	
	public Zan findZan(int topicId, int replayId,int zanId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.ZAN)
				.append(" where topicId = ").append(topicId)
				.append(" and replayId = ").append(replayId)
				.append(" and zanId = ").append(zanId);
		Zan a = null;
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				 a = putZan(cursor);
			}

		}
		cursor.close();
		return a;
	}
	
	public Zan findZanByUserId(int topicId, int replayId,int userId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.ZAN)
				.append(" where topicId = ").append(topicId)
				.append(" and replayId = ").append(replayId)
				.append(" and userId = ").append(userId);
		Zan a = null;
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				 a = putZan(cursor);
			}

		}
		cursor.close();
		return a;
	}
	
	public void deleteAll(){
		String sql = "delete from "+openHelper.ZAN;
		openHelper.execSQL(sql);
	}

	private ContentValues putContentValues(Zan zan) {
		ContentValues cv = new ContentValues();
		cv.put("zanId", zan.getZanId());
		cv.put("topicId", zan.getTopicId());
		cv.put("replayId", zan.getReplayId());
		cv.put("userId", zan.getUserId());
		cv.put("date", zan.getDate());
		cv.put("isSend", zan.getIsSend());
		cv.put("userName", zan.getUserName());
		
		return cv;
	}

	private Zan putZan(Cursor cursor) {
		int i = 0;
		Zan module = new Zan();
		module.setId(cursor.isNull(i) ? null : cursor.getInt(i));
		i++;
		module.setZanId(cursor.getInt(i++));
		module.setTopicId(cursor.getInt(i++));
		module.setReplayId(cursor.getInt(i++));
		module.setUserId(cursor.getInt(i++));
		module.setDate(cursor.getString(i++));
		module.setIsSend(cursor.getInt(i++));
		module.setUserName(cursor.getString(i++));
		return module;
	}

}
