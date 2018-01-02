package com.yunhu.yhshxc.wechat.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yunhu.yhshxc.database.DatabaseHelper;
import com.yunhu.yhshxc.wechat.bo.Comment;
import com.yunhu.yhshxc.wechat.bo.Reply;

public class CommentDB {
	private DatabaseHelper openHelper;

	public CommentDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}

	public void insert(Comment comment) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues cv = putContentValues(comment);
		db.insert(openHelper.COMMENT, null, cv);
	}

	public List<Comment> findCommentList() {
		List<Comment> list = new ArrayList<Comment>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.COMMENT)
				.append(" where 1=1");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				Comment a = putComment(cursor);
				list.add(a);
			}
			cursor.close();
		}
		return list;
	}
	
	public Comment findCommentByCommentId(int commentId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.COMMENT).append(" where commentId = ").append(commentId);
		Cursor cursor = openHelper.query(sql.toString(), null);
		Comment a = null;
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				a = putComment(cursor);
			}
		}
		cursor.close();
		return a;
	}

	public List<Comment> findCommentListByReply(Reply reply) {
		List<Comment> list = new ArrayList<Comment>();
		if (reply!=null) {
			StringBuffer sql = new StringBuffer();
			sql.append(" select * from ").append(openHelper.COMMENT).append(" where replyId = ").append(reply.getReplyId()).append(" order by commentId");
			Cursor cursor = openHelper.query(sql.toString(), null);
			if (cursor != null) {
				if (cursor.getCount() > 0) {
					while (cursor.moveToNext()) {
						Comment a = putComment(cursor);
						list.add(a);
					}
				}
				cursor.close();
			}
		}
		return list;
	}
	
	public void updateCommentPathCode(Comment comment,String pathCode){
		StringBuffer sql = new StringBuffer();
		sql.append(" update  ").append(openHelper.COMMENT).append(" set pathCode = '").append(pathCode).append("'").append(" where replyId=").append(comment.getReplyId());
		sql.append(" and commentId = ").append(comment.getCommentId());
	}

	public void updateComment(Comment comment) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues cv = putContentValues(comment);
		db.update( openHelper.REPLY, cv, " commentId=? ", new String[] { comment.getCommentId() + "" });
		
	}
	
	/**
	 * 排序取最后一条评论信息
	 */
	public Comment findRecentCommentByRplayId(int replyId){
		Comment comment = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from (");
		sql.append(" select * from ").append(openHelper.COMMENT)
				.append(" where replyId = ").append(replyId);
		sql.append("  order by pathCode desc  limit 1 ").append(
				") order by pathCode asc");

		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				comment = putComment(cursor);
			}
		}
		cursor.close();
		return comment;
	}
	
	/**
	 * 排序取最后一条指定审核人id
	 */
	public Comment findRecentCommentByAuthUserId(int replyId){
		Comment comment = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from (");
		sql.append(" select * from ").append(openHelper.COMMENT)
				.append(" where replyId = ").append(replyId).append(" and authUserId <> \"\" ");
		sql.append("  order by pathCode desc  limit 1 ").append(
				") order by pathCode asc");

		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				comment = putComment(cursor);
			}
		}
		cursor.close();
		return comment;
	}
	
	public void deleteAll(){
		String sql = "delete from "+openHelper.COMMENT;
		openHelper.execSQL(sql);
	}
	
	
	private ContentValues putContentValues(Comment comment) {
		ContentValues cv = new ContentValues();
		cv.put("replyId", comment.getReplyId());
		cv.put("commentId", comment.getCommentId());
		cv.put("comment", comment.getComment());
		cv.put("cUserId", comment.getcUserId());
		cv.put("cUserName", comment.getcUserName());
		cv.put("dUserId", comment.getdUserId());
		cv.put("dUserName", comment.getdUserName());
		cv.put("pathCode", comment.getPathCode());
		cv.put("date", comment.getDate());
		cv.put("topicId", comment.getTopicId());
		cv.put("isSend", comment.getIsSend());
		cv.put("msgKey", comment.getMsgKey());
		cv.put("isPublic", comment.getIsPublic());
		cv.put("authUserId", comment.getAuthUserId());
		cv.put("authUserName", comment.getAuthUserName());
		return cv;
	}

	private Comment putComment(Cursor cursor) {
		int i = 0;
		Comment module = new Comment();
		module.setId(cursor.isNull(i) ? null : cursor.getInt(i));
		i++;
		module.setReplyId(cursor.getInt(i++));
		module.setCommentId(cursor.getInt(i++));
		module.setComment(cursor.getString(i++));
		module.setcUserId(cursor.getInt(i++));
		module.setcUserName(cursor.getString(i++));
		module.setdUserId(cursor.getInt(i++));
		module.setdUserName(cursor.getString(i++));
		module.setPathCode(cursor.getString(i++));
		module.setDate(cursor.getString(i++));
		module.setTopicId(cursor.getString(i++));
		module.setIsSend(cursor.getInt(i++));
		module.setMsgKey(cursor.getString(i++));
		module.setIsPublic(cursor.getString(i++));
		module.setAuthUserId(cursor.getInt(i++));
		module.setAuthUserName(cursor.getString(i++));
		return module;
	}

}
