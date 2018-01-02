package com.yunhu.yhshxc.activity.questionnaire.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yunhu.yhshxc.activity.questionnaire.bo.FindIngDetail;
import com.yunhu.yhshxc.database.DatabaseHelper;

public class FindIngDetailDB {

	private DatabaseHelper openHelper;

	public FindIngDetailDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}

	public void insertFindIngDetail(FindIngDetail FindIngDetail) {
		ContentValues cv = putContentValues(FindIngDetail);
		openHelper.insert(openHelper.FINDING_DETAIL, cv);
	}
	
	/**
	 * 根据Id查找FindIngDetail
	 * @param cId
	 * @return
	 */
	public FindIngDetail findFindIngDetailById(int cId) {
		FindIngDetail findIngDetail = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.FINDING_DETAIL)
				.append(" where choiceOptions = ").append(cId);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				findIngDetail = putFindIngDetail(cursor);
			}
		}
		cursor.close();
		return findIngDetail;
	}
	
	
	
	
	
	/**
	 * 根据Id查找FindIngDetail
	 * @param qId
	 * @return
	 */
	public FindIngDetail findFindIngDetailByQId(int qId) {
		FindIngDetail findIngDetail = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.FINDING_DETAIL)
				.append(" where questionId = ").append(qId);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				findIngDetail = putFindIngDetail(cursor);
			}
		}
		cursor.close();
		return findIngDetail;
	}
	
	
	
	/**
	 * 根据Id查找FindIngDetail
	 * @param qId,rId
	 * @return
	 */
	public FindIngDetail findFindIngDetailByQId(int qId,int rId) {
		FindIngDetail findIngDetail = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.FINDING_DETAIL)
				.append(" where questionId = ").append(qId)
				.append(" and findIngId = ").append(rId);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				findIngDetail = putFindIngDetail(cursor);
			}
		}
		cursor.close();
		return findIngDetail;
	}
	/**
	 * 根据Id查找FindIngDetail
	 * @param qId,rId
	 * @return
	 */
	public List<FindIngDetail> findFindIngDetailListByQId(int qId,int rId) {
		List<FindIngDetail> findIngDetail = new ArrayList<FindIngDetail>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.FINDING_DETAIL)
		.append(" where questionId = ").append(qId)
		.append(" and findIngId = ").append(rId);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
				FindIngDetail f = putFindIngDetail(cursor);
				findIngDetail.add(f);
			}
		}
		cursor.close();
		return findIngDetail;
	}

	
	
	
	/**
	 * 更新FindIngDetail
	 * @param findIngDetail
	 */
	public void updateFindIngDetail(FindIngDetail findIngDetail) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues cv = putContentValues(findIngDetail);
		db.update(openHelper.FINDING_DETAIL, cv, " questionId=? and findIngId=? ",
				new String[] { findIngDetail.getQuestionId()+"", findIngDetail.getFindIngId()+""});

	}
	

	

	
	public List<FindIngDetail> findAllFindIngDetail(){
		List<FindIngDetail> list = new ArrayList<FindIngDetail>();
		StringBuffer findSql=new StringBuffer();
		findSql.append("select * from ").append(openHelper.FINDING_DETAIL);
		Cursor cursor = openHelper.query(findSql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putFindIngDetail(cursor));
			}
		}
		cursor.close();
		return list;
	}
	
	public void clearFindIngDetail111(){
		StringBuffer sql = new StringBuffer();
		sql.append("delete from ").append(openHelper.FINDING_DETAIL);
		openHelper.execSQL(sql.toString());
	}
	
	public void clearFindIngDetail(){
		StringBuffer sql = new StringBuffer();
		sql.append("delete from ").append(openHelper.FINDING_DETAIL);
		openHelper.execSQL(sql.toString());
	}

	/**
	 * 根据Id删除FindIngDetail
	 * @param qId
	 * @return
	 */
	public void deleteFindIngDetailByQId(int qId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" delete from ").append(openHelper.FINDING_DETAIL)
		.append(" where questionId = ").append(qId);
		openHelper.execSQL(sql.toString());
	}
	/**
	 * 根据optionId 删除FindIngDetail
	 * @param optionId
	 * @return
	 */
	public void deleteFindIngDetailByOptionId(String choiceOptions) {
		StringBuffer sql = new StringBuffer();
		sql.append(" delete from ").append(openHelper.FINDING_DETAIL)
		.append(" where choiceOptions = ").append(choiceOptions);
		openHelper.execSQL(sql.toString());
	}
	
	private FindIngDetail putFindIngDetail(Cursor cursor) {
		int i = 0;
		FindIngDetail data = new FindIngDetail();
		data.setId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		data.setFindIngId(cursor.getInt(i++));
		data.setQuestionId(cursor.getInt(i++));
		data.setChoiceOptions(cursor.getString(i++));
		data.setFillOptions(cursor.getString(i++));
		data.setAttachment(cursor.getString(i++));
		return data;
	}

	private ContentValues putContentValues(FindIngDetail data) {
		ContentValues cv = new ContentValues();
		cv.put("findIngId", data.getFindIngId());
		cv.put("questionId", data.getQuestionId());
		cv.put("choiceOptions", data.getChoiceOptions());
		cv.put("fillOptions", data.getFillOptions());
		cv.put("attachment", data.getAttachment());
		return cv;
	}

}
