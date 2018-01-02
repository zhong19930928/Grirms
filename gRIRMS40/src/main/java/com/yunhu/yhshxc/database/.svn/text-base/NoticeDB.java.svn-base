package com.yunhu.yhshxc.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yunhu.yhshxc.bo.Notice;
import com.yunhu.yhshxc.utility.PublicUtils;

/**
 * 
 * @author jishen 数据库操作
 */
public class NoticeDB {

	private String TAG = "NoticeDB";

	private DatabaseHelper openHelper;

	public NoticeDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}

	/**
	 * @param notify
	 * @return保存公告信息
	 */
	public Long insertNotice(Notice notice) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("createUser", notice.getCreateUser());
		cv.put("noticeTitle", notice.getNoticeTitle());
		cv.put("detailNotice", notice.getDetailNotice());
		cv.put("createTime", notice.getCreateTime());
		cv.put("isread", notice.getIsread());
		cv.put("notifyId", notice.getNotifyId());
		cv.put("createOrg", notice.getCreateOrg());
		cv.put("dataType", notice.getNotifyType());
		cv.put("attachment", notice.getAttachment());
		Long id = db.insert(openHelper.NOTICE_TABLE, null, cv);
		List<Notice> noticeList = findAllNoticeList();
		int len = noticeList.size()-80;
		if (len >0) {
				Notice n = findNoticeLastDate();//获取最后一个
				deleteNoticeById(n.getId()+"");
		}
		return id;

	}
	
	/**
	 * 
	 * @return查找公告记录返回一个List集合
	 */
	public List<Notice> findAllNoticeList() {
		List<Notice> noticeList = new ArrayList<Notice>();
		StringBuffer findSql=new StringBuffer();
		findSql.append("select * from ").append(openHelper.NOTICE_TABLE).append(" order by createTime desc");
		
		SQLiteDatabase db = openHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(findSql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				noticeList.add(putNotice(cursor));
			}
		}
		cursor.close();
		
		return noticeList;
	}
	
	/**
	 * 更新公告阅读状态
	 * @param id DB中自动生成的ID
	 * @param isread 是否阅读
	 */
	public void updateNoticeReadStateById(int notifyId,int isread) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		StringBuffer sql = new StringBuffer();
		sql.append("update ").append(openHelper.NOTICE_TABLE).append(" set isread =").append(isread).append(" where notifyId=").append(notifyId);
		db.execSQL(sql.toString());
//		Log.d(TAG + "===修改公告已阅读==>", "notifyId = " + notifyId);
		
	}

	/**
	 * 
	 * @return查找前20条公告记录返回一个List集合
	 */
	public List<Notice> findAllNotice() {
		List<Notice> noticeList = new ArrayList<Notice>();
//		String findSql = "select * from "+ openHelper.NOTICE_TABLE;
//		String findSql = "select * from " + openHelper.NOTICE_TABLE + " order by createTime desc limit 20";
		StringBuffer findSql=new StringBuffer();
		findSql.append("select * from ").append(openHelper.NOTICE_TABLE).append(" order by createTime desc limit 20");
		
		SQLiteDatabase db = openHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(findSql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				noticeList.add(putNotice(cursor));
			}
		}
		cursor.close();
		
		return noticeList;
	}
	
	/**
	 * 
	 * @return查找最新一条公告
	 */
	public Notice findNoticeLastDate() {
//		String findSql = "select * from " + openHelper.NOTICE_TABLE + " order by createTime desc limit 1";
		StringBuffer findSql=new StringBuffer();
		findSql.append("select * from ").append(openHelper.NOTICE_TABLE).append(" order by createTime desc limit 1");
		
		SQLiteDatabase db = openHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(findSql.toString(), null);
		Notice notice = null;
		if (cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				notice = putNotice(cursor);
			}
		}
		cursor.close();
		
		return notice;
	}
	/**
	 * 
	 * @return查找最新20条公告
	 */
	public List<Notice> findNoticeByDate(String date) {
		StringBuffer findSql=new StringBuffer();
		findSql.append("select * from ").append(openHelper.NOTICE_TABLE).append(" where createTime < '").append(date).append("' order by createTime desc limit 20");
		
		
		SQLiteDatabase db = openHelper.getReadableDatabase();
		List<Notice> list=new ArrayList<Notice>();
		Cursor cursor = db.rawQuery(findSql.toString(), null);
		Notice notice = null;
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				notice = putNotice(cursor);
				list.add(notice);
			}
		}
		cursor.close();
		
		return list;
	}
	
	private Notice putNotice(Cursor cursor){
		int i = 0;
		Notice notice = new Notice();
		notice.setId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		notice.setNoticeTitle(cursor.getString(i++));
		notice.setDetailNotice(cursor.getString(i++));
		notice.setCreateUser(cursor.getString(i++));
		notice.setCreateTime(cursor.getString(i++));
		notice.setIsread(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		notice.setNotifyId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		notice.setCreateOrg(cursor.getString(i++));
		notice.setNotifyType(cursor.getString(i++));
		notice.setAttachment(cursor.getString(i++));
		return notice;
	}

	/**
	 * 
	 * @return查找所有的未读公告记录返回一个List集合
	 */
	public int findAllUnreadNoticeNumber() {
		StringBuffer findSql=new StringBuffer();
		findSql.append("select count(*) from ").append(openHelper.NOTICE_TABLE).append(" where isread = ").append(Notice.ISREAD_N);
		
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
	
	/*
	 * 通过Id删除公告信息
	 */
	public void deleteNoticeById(String id) {
		if(PublicUtils.isIntegerArray(id)){
			SQLiteDatabase db = openHelper.getWritableDatabase();
			StringBuffer deleteSql=new StringBuffer();
			deleteSql.append("delete from ").append(openHelper.NOTICE_TABLE).append(" where id in(").append(id).append(")");
			db.execSQL(deleteSql.toString());
		}
	}
	
	/**
	 * 根据公告ID删除公告
	 * @param id 要删除公告的公告id
	 */
	public void deleteNoticeByNotifyId(int id) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		StringBuffer deleteSql=new StringBuffer();
		deleteSql.append("delete from ").append(openHelper.NOTICE_TABLE).append(" where notifyId = ").append(id);
		db.execSQL(deleteSql.toString());
	}

}
