package com.yunhu.yhshxc.activity.questionnaire.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yunhu.yhshxc.activity.questionnaire.bo.Findings;
import com.yunhu.yhshxc.database.DatabaseHelper;

public class FindingsDB {

	private DatabaseHelper openHelper;

	public FindingsDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}

	public void insertFindIngs(Findings data) {
		ContentValues cv = putContentValues(data);
		openHelper.insert(openHelper.FINDINGS, cv);
	}
	
	/**
	 * 根据Id查找Findings
	 * @param qId
	 * @return
	 */
	public Findings findFindingsById(int rId) {
		Findings findings = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.FINDINGS)
				.append(" where resultId = ").append(rId);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				findings = putFindIngs(cursor);
			}
		}
		cursor.close();
		return findings;
	}
	
	
	/**
	 * 根据Id查找Findings
	 * @param qId
	 * @return
	 */
	public List<Findings> findFindingsByQId(int qId) {
		List<Findings> list = new ArrayList<Findings>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.FINDINGS)
				.append(" where questionnaireId = ").append(qId);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putFindIngs(cursor));
			}
		}
		cursor.close();
		return list;
	}
	
	
	
	/**
	 * 更新Findings
	 * @param findings
	 */
	public void updateFindings(Findings findings) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues cv = putContentValues(findings);
		db.update(openHelper.FINDINGS, cv, " resultId=? ",
				new String[] { findings.getResultId() + "" });

	}
	
	
	public List<Findings> findAllFindIng(){
		List<Findings> list = new ArrayList<Findings>();
		StringBuffer findSql=new StringBuffer();
		findSql.append("select * from ").append(openHelper.FINDINGS);
		Cursor cursor = openHelper.query(findSql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putFindIngs(cursor));
			}
		}
		cursor.close();
		return list;
	}
	
	public void clearFindIngDetail(){
		StringBuffer sql = new StringBuffer();
		sql.append("delete from ").append(openHelper.FINDINGS);
		openHelper.execSQL(sql.toString());
	}
	

	private Findings putFindIngs(Cursor cursor) {
		int i = 0;
		Findings data = new Findings();
		data.setId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		data.setQuestionnaireId(cursor.getInt(i++));
		data.setResultId(cursor.getInt(i++));
		data.setInvestigatorId(cursor.getInt(i++));
		data.setInvestigatorName(cursor.getString(i++));
		data.setInvestigatorPhoneno(cursor.getString(i++));
		data.setInvestigatorOrgPath(cursor.getString(i++));
		data.setStartDate(cursor.getString(i++));
		data.setEndDate(cursor.getString(i++));
		data.setAdressId(cursor.getString(i++));
		data.setAdress(cursor.getString(i++));
		data.setLonLat(cursor.getString(i++));
		return data;
	}

	private ContentValues putContentValues(Findings data) {
		ContentValues cv = new ContentValues();
		cv.put("questionnaireId", data.getQuestionnaireId());
		cv.put("resultId", data.getResultId());
		cv.put("investigatorId", data.getInvestigatorId());
		cv.put("investigatorName", data.getInvestigatorName());
		cv.put("investigatorPhoneno", data.getInvestigatorPhoneno());
		cv.put("investigatorOrgPath", data.getInvestigatorOrgPath());
		cv.put("startDate", data.getStartDate());
		cv.put("endDate", data.getEndDate());
		cv.put("adressId", data.getAdressId());
		cv.put("adress", data.getAdress());
		cv.put("lonLat", data.getLonLat());
		return cv;
	}

}
