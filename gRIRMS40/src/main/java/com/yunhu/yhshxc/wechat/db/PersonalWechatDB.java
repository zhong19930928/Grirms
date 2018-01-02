package com.yunhu.yhshxc.wechat.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yunhu.yhshxc.database.DatabaseHelper;
import com.yunhu.yhshxc.wechat.bo.PersonalWechat;

public class PersonalWechatDB {
	private DatabaseHelper openHelper;

	public PersonalWechatDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}

	public void insert(PersonalWechat p) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues cv = putContentValues(p);
		db.insert(openHelper.PERSONAL_WECHAT, null, cv);
	}

	public List<PersonalWechat> findPersonalWechatList(int fromId,int toId) {
		List<PersonalWechat> list = new ArrayList<PersonalWechat>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.PERSONAL_WECHAT)
				.append(" where sUserId=").append(fromId).append(" and dUserId = ").append(toId);
		sql.append(" or (sUserId = ").append(toId).append(" and dUserId = ").append(fromId).append(")");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				PersonalWechat a = putPersonalWechat(cursor);
				list.add(a);
			}
		}
		cursor.close();
		return list;
	}
	
	
	/**
	 * 查询一个列表的未读数
	 * @param fromId
	 * @param toId
	 * @return
	 */
	public int  findPersonalWechatCount(int fromId,int toId) {
		int count = 0;
		StringBuffer sql = new StringBuffer(" select count(*) from ").append(openHelper.PERSONAL_WECHAT)
				.append(" where   ((sUserId=").append(fromId).append(" and dUserId = ").append(toId);
		sql.append(") or (sUserId = ").append(toId).append(" and dUserId = ").append(fromId).append(")");
		sql.append(") and isRead = 0");
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
	 * 查询所有私聊未读数
	 * @return
	 */
	public int  findAllPersonalWechatCount() {
		int count = 0;
		StringBuffer sql = new StringBuffer(" select count(*) from ").append(openHelper.PERSONAL_WECHAT);
		sql.append(" where isRead = 0");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				count = cursor.getInt(0);
			}
		}
		cursor.close();
		return count;
	}
	
	
	
	public List<PersonalWechat> findPersonalWechat() {
		List<PersonalWechat> list = new ArrayList<PersonalWechat>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.PERSONAL_WECHAT).append(" group by  groupKey order by date desc");
		
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				PersonalWechat a = putPersonalWechat(cursor);
				list.add(a);
			}
		}
		cursor.close();
		return list;
	}
	
	public void updateAllPersonalWechatToIsRead(int fromId,int toId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" update  ").append(openHelper.PERSONAL_WECHAT).append(" set isRead = 1");
		sql.append(" where sUserId=").append(fromId).append(" and dUserId = ").append(toId);
		sql.append(" or (sUserId = ").append(toId).append(" and dUserId = ").append(fromId).append(")");
		openHelper.execSQL(sql.toString());
	}

	
	
	public void deleteAll(){
		String sql = "delete from "+openHelper.PERSONAL_WECHAT;
		openHelper.execSQL(sql);
	}

	private ContentValues putContentValues(PersonalWechat p) {
		ContentValues cv = new ContentValues();
		cv.put("dataId", p.getDataId());
		cv.put("sUserId", p.getsUserId());
		cv.put("sUserName", p.getsUserName());
		cv.put("dUserId", p.getdUserId());
		cv.put("dUserName", p.getdUserName());
		cv.put("cUserHeadImg", p.getcUserHeadImg());
		cv.put("dUserHedaImg", p.getdUserHedaImg());
		cv.put("attachment", p.getAttachment());
		cv.put("content", p.getContent());
		cv.put("date", p.getDate());
		cv.put("msgKey", p.getMsgKey());
		cv.put("groupKey", p.getGroupKey());
		cv.put("photo", p.getPhoto());
		cv.put("isRead", p.getIsRead());
		return cv;
	}

	private PersonalWechat putPersonalWechat(Cursor cursor) {
		int i = 0;
		PersonalWechat module = new PersonalWechat();
		module.setId(cursor.isNull(i) ? null : cursor.getInt(i));
		i++;
		module.setDataId(cursor.getInt(i++));
		module.setsUserId(cursor.getInt(i++));
		module.setsUserName(cursor.getString(i++));
		module.setdUserId(cursor.getInt(i++));
		module.setdUserName(cursor.getString(i++));
		module.setcUserHeadImg(cursor.getString(i++));
		module.setdUserHedaImg(cursor.getString(i++));
		module.setAttachment(cursor.getString(i++));
		module.setContent(cursor.getString(i++));
		module.setDate(cursor.getString(i++));
		module.setMsgKey(cursor.getString(i++));
		module.setGroupKey(cursor.getString(i++));
		module.setPhoto(cursor.getString(i++));
		module.setIsRead(cursor.getInt(i++));
		return module;
	}

}
