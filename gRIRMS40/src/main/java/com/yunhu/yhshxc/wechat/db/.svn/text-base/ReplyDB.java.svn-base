package com.yunhu.yhshxc.wechat.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yunhu.yhshxc.database.DatabaseHelper;
import com.yunhu.yhshxc.wechat.bo.Reply;

public class ReplyDB {
	private DatabaseHelper openHelper;

	public ReplyDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}

	public void insert(Reply comment) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues cv = putContentValues(comment);
		db.insert(openHelper.REPLY, null, cv);
	}

	/**
	 * 根据话题ID查找回帖 查找最新的最新的10条
	 * 
	 * @param topicId
	 * @return
	 */
	public List<Reply> findReplyListByTopicId(int topicId) {
		List<Reply> list = new ArrayList<Reply>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from (");
		sql.append(" select * from ").append(openHelper.REPLY)
				.append(" where topicId = ").append(topicId);
		sql.append("  order by date desc  limit 10 ").append(
				") order by date asc");

		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				Reply a = putComment(cursor);
				list.add(a);
			}
		}
		cursor.close();
		return list;
	}
	
	/**
	 * 更新话题下所有回帖状态
	 * @param topicId
	 * @param colseState
	 */
	public void updateReplyCloseState(int topicId,int colseState) {
		StringBuffer sql = new StringBuffer();
		sql.append(" update ").append(openHelper.REPLY);
		sql.append(" set isClose = ").append(colseState).append(" where topicId = ").append(topicId);
		openHelper.execSQL(sql.toString());
		
	}
	
	
	public void deleteAll(){
		String sql = "delete from "+openHelper.REPLY;
		openHelper.execSQL(sql);
	}
	
	/**
	 * 根据话题ID查找回帖 查找最新的最新的10条
	 * 
	 * @param topicId
	 * @return
	 */
	public List<Reply> findAllReplyListByTopicId(int topicId) {
		List<Reply> list = new ArrayList<Reply>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.REPLY)
		.append(" where topicId = ").append(topicId);
		sql.append("  order by date,replyId");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				Reply a = putComment(cursor);
				list.add(a);
			}
		}
		cursor.close();
		return list;
	}

	/**
	 * 查找最新的一条回帖内容
	 * 
	 * @param topicId
	 * @return
	 */
	public Reply findNewReply(int topicId) {
		StringBuffer sql = new StringBuffer();
		Reply reply = null;
		sql.append(" select * from ").append(openHelper.REPLY)
				.append(" where topicId = ").append(topicId);
		sql.append("  order by date desc  limit 1 ");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				reply = putComment(cursor);
			}
		}
		cursor.close();
		return reply;
	}

	/**
	 * 查找每个话题未读的消息个数
	 * 
	 * @param replyId
	 * @param pathCode
	 */
	public int findTopicReplyNum(int topicId){
		SQLiteDatabase db = openHelper.getReadableDatabase();
		int count = 0;
		StringBuffer sql = new StringBuffer(" select count(*) from ").append(openHelper.REPLY)
				.append(" where topicId = ").append(topicId);
		sql.append(" and isRead = 0 ");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				count = cursor.getInt(0);
			}
		}
		cursor.close();
		return count;
	}
/**
 * 公共话题未读消息数量
 */
	public int findClassfyReplyNum(int isPublic,int isRead){
		SQLiteDatabase db = openHelper.getReadableDatabase();
		int count = 0;
		StringBuffer sql = new StringBuffer(" select count(*) from ").append(openHelper.REPLY)
				.append(" where isPublic = ").append(isPublic).append(" and isClose = 0");
		sql.append(" and isRead = ").append(isRead);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				count = cursor.getInt(0);
			}
		}
		cursor.close();
		return count;
	}
	/**
	 * 部门交流话题未读消息
	 */
	public int findBumenReplayNum(int isPublic,int isRead){
		SQLiteDatabase db = openHelper.getReadableDatabase();
		int count = 0;
		StringBuffer sql = new StringBuffer(" select count(*) from ").append(openHelper.REPLY)
				.append(" where isPublic <> ").append(isPublic).append(" and isClose = 0");
		sql.append(" and isRead = ").append(isRead);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				count = cursor.getInt(0);
			}
		}
		cursor.close();
		return count;
	}
	/**
	 * 查找所有话题的未读消息个数
	 * @param replyId
	 * @param pathCode
	 */
	public int findAllRepayNum(int isRead){
		SQLiteDatabase db = openHelper.getReadableDatabase();
		int count = 0;
		StringBuffer sql = new StringBuffer(" select count(*) from ").append(openHelper.REPLY);
		sql.append(" where isRead = ").append(isRead).append(" and isClose = 0");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				count = cursor.getInt(0);
			}
		}
		cursor.close();
		return count;
	}
	
	public void updateAllReplyToIsRead(int topicId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" update  ").append(openHelper.REPLY)
				.append(" set isRead = 1")
				.append(" where topicId=").append(topicId);
		openHelper.execSQL(sql.toString());
	}

	public void updateReply(Reply reply) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues cv = putContentValues(reply);
		db.update(openHelper.REPLY, cv, " replyId=? ",
				new String[] { reply.getReplyId() + "" });

	}

	public List<Reply> findCurrentList1(int topicId) {
		List<Reply> list = new ArrayList<Reply>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.REPLY)
				.append(" where topicId = ").append(topicId);
		// .append(" by date asc");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				Reply a = putComment(cursor);
				list.add(a);
			}
			cursor.close();
		}
		return list;
	}

	public Reply findReplyByReplyId(int replyId) {
		Reply reply = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.REPLY)
				.append(" where replyId = ").append(replyId);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				reply = putComment(cursor);
			}
		}
		cursor.close();
		return reply;
	}

	public void deleteReply(int topicId) {
		openHelper.delete(openHelper.REPLY, "topicId=?",
				new String[] { String.valueOf(topicId) });
	}
	
	private ContentValues putContentValues(Reply comment) {
		ContentValues cv = new ContentValues();
		cv.put("replyId", comment.getReplyId());
		cv.put("topicId", comment.getTopicId());
		cv.put("level", comment.getLevel());
		cv.put("userId", comment.getUserId());
		cv.put("survey", comment.getSurvey());
		cv.put("content", comment.getContent());
		cv.put("date", comment.getDate());
		cv.put("adress", comment.getAdress());
		cv.put("attachment", comment.getAttachment());
		cv.put("photo", comment.getPhoto());
		cv.put("pathCode", comment.getPathCode());
		cv.put("isSend", comment.getIsSend());
		cv.put("replyName", comment.getReplyName());
		cv.put("isRead", comment.getIsRead());
		cv.put("headImg", comment.getUrl());
		cv.put("topicType", comment.getTopicType());
		cv.put("msgKey", comment.getMsgKey());
		cv.put("isPublic", comment.getIsPublic());
		cv.put("isClose", comment.getIsClose());
		cv.put("authUserId", comment.getAuthUserId());
		cv.put("authUserName", comment.getAuthUserName());
		cv.put("delStatus", comment.getDelStatus());
		return cv;
	}

	private Reply putComment(Cursor cursor) {
		int i = 0;
		Reply module = new Reply();
		module.setId(cursor.isNull(i) ? null : cursor.getInt(i));
		i++;
		module.setReplyId(cursor.getInt(i++));
		module.setTopicId(cursor.getInt(i++));
		module.setLevel(cursor.getInt(i++));
		module.setUserId(cursor.getInt(i++));
		module.setSurvey(cursor.getString(i++));
		module.setContent(cursor.getString(i++));
		module.setDate(cursor.getString(i++));
		module.setAdress(cursor.getString(i++));
		module.setAttachment(cursor.getString(i++));
		module.setPhoto(cursor.getString(i++));
		module.setPathCode(cursor.getString(i++));
		module.setIsSend(cursor.getInt(i++));
		module.setReplyName(cursor.getString(i++));
		module.setIsRead(cursor.getInt(i++));
		module.setUrl(cursor.getString(i++));
		module.setTopicType(cursor.getString(i++));
		module.setMsgKey(cursor.getString(i++));
		module.setIsPublic(cursor.getString(i++));
		module.setIsClose(cursor.getInt(i++));
		module.setAuthUserId(cursor.getInt(i++));
		module.setAuthUserName(cursor.getString(i++));
		module.setDelStatus(cursor.getString(i++));
		return module;
	}

}
