package com.yunhu.yhshxc.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.yunhu.yhshxc.submitManager.bo.CoreHttpPendingRequest;
import com.yunhu.yhshxc.submitManager.bo.TablePending;

public class TablePendingDB {

	private DatabaseHelper openHelper;

	public TablePendingDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}

	public void insertNewOrder(TablePending tablePending){
		ContentValues cv = putContentValues(tablePending);
		openHelper.insert(openHelper.TABLE_PENDING, cv);
	}
	
	public void updateNewOrder(TablePending tablePending){
		ContentValues cv = putContentValues(tablePending);
		openHelper.update(openHelper.TABLE_PENDING, cv, "id", new String[]{tablePending.getId()+""});
	}
	
	public List<TablePending> findAllTablePending(){
		List<TablePending> list = new ArrayList<TablePending>();
		StringBuffer findSql=new StringBuffer();
		findSql.append("select * from ").append(openHelper.TABLE_PENDING).append(" where type = ").append(TablePending.TYPE_DATA);
		findSql.append(" or type = ").append(TablePending.TYPE_IMAGE);
		findSql.append(" or type = ").append(TablePending.TYPE_AUDIO);
		findSql.append(" or type = ").append(TablePending.TYPE_LARGE_IMAGE);
		findSql.append(" or (type = ").append(TablePending.TYPE_LOATION);
		findSql.append(" and status in(").append(TablePending.STATUS_ERROR_SERVER).append(",").append(TablePending.STATUS_ERROR_USER).append(")");
		findSql.append(")");
		findSql.append(" order by status ");
		Cursor cursor = openHelper.query(findSql.toString(), null); 
		if (cursor.getCount() > 0) {
			TablePending tablePending = null;
			while (cursor.moveToNext()) {
				tablePending = putTablePending(cursor);
				list.add(tablePending);
			}
		}
		cursor.close();
		return list;
	}
	
	/**
	 * 未上报车销数据
	 * @return
	 */
	public List<TablePending> findAllCarSalesTablePending(){
		return findAllTablePending();
	}

	
	public int tablePendingCount(){
		int count = 0;
		StringBuffer findSql=new StringBuffer();
		findSql.append("select count(*) from ").append(openHelper.TABLE_PENDING).append(" where type = ").append(TablePending.TYPE_DATA);
		findSql.append(" or type = ").append(TablePending.TYPE_IMAGE);
		findSql.append(" or type = ").append(TablePending.TYPE_AUDIO);
		findSql.append(" or type = ").append(TablePending.TYPE_LARGE_IMAGE);
		findSql.append(" or (type = ").append(TablePending.TYPE_LOATION);
		findSql.append(" and status in(").append(TablePending.STATUS_ERROR_SERVER).append(",").append(TablePending.STATUS_ERROR_USER).append(")");
		findSql.append(")");
		Cursor cursor = openHelper.query(findSql.toString(), null); 
		if (cursor.getCount() > 0) {
			cursor.moveToNext();
			count = (int) cursor.getLong(0);
		}
		cursor.close();
		return count;
	}
	
	public int tablePendingCountNotErrorServer(){
		int count = 0;
		StringBuffer findSql=new StringBuffer();
		findSql.append("select count(*) from ").append(openHelper.TABLE_PENDING).append(" where (type = ").append(TablePending.TYPE_DATA);
		findSql.append(" or type = ").append(TablePending.TYPE_IMAGE);
		findSql.append(" or type = ").append(TablePending.TYPE_AUDIO);
		findSql.append(" or type = ").append(TablePending.TYPE_LARGE_IMAGE);
		findSql.append(" or (type = ").append(TablePending.TYPE_LOATION);
		findSql.append(" and status in(").append(TablePending.STATUS_ERROR_SERVER).append(",").append(TablePending.STATUS_ERROR_USER).append(")");
		findSql.append("))");
		findSql.append(" and status <> ").append(TablePending.STATUS_ERROR_SERVER);
		findSql.append(" and status <> ").append(TablePending.STATUS_ERROR_USER);
		Cursor cursor = openHelper.query(findSql.toString(), null); 
		if (cursor.getCount() > 0) {
			cursor.moveToNext();
			count = (int) cursor.getLong(0);
		}
		cursor.close();
		return count;
	}
	
	public List<TablePending> findAllFailTablePending(){
		List<TablePending> list = new ArrayList<TablePending>();
		StringBuffer findSql=new StringBuffer();
		findSql.append("select * from ").append(openHelper.TABLE_PENDING);
		findSql.append(" where status <> ").append(TablePending.STATUS_SUBIMTTING);
		findSql.append(" and status <> ").append(TablePending.STATUS_READY);
		Cursor cursor = openHelper.query(findSql.toString(), null); 
		if (cursor.getCount() > 0) {
			TablePending tablePending = null;
			while (cursor.moveToNext()) {
				tablePending = putTablePending(cursor);
				list.add(tablePending);
			}
		}
		cursor.close();
		return list;
	}
	
	
	public TablePending findSubmitingTablePending(){
		StringBuffer findSql=new StringBuffer();
		findSql.append("select * from ").append(openHelper.TABLE_PENDING).append(" where status=").append(TablePending.STATUS_SUBIMTTING);
		Cursor cursor = openHelper.query(findSql.toString(), null);
		TablePending tablePending = null;
		if (cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				tablePending = putTablePending(cursor);
			}
		}
		cursor.close();
		return tablePending;
	}
	
	public TablePending findReadyTablePending(){
		StringBuffer findSql=new StringBuffer();
		findSql.append("select * from ").append(openHelper.TABLE_PENDING).append(" where status=").append(TablePending.STATUS_READY).append(" and numberOfTimes<=3").append(" order by id limit 1");
		Cursor cursor = openHelper.query(findSql.toString(), null);
		TablePending tablePending = null;
		if (cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				tablePending = putTablePending(cursor);
			}
		}
		cursor.close();
		return tablePending;
	}
	
	public TablePending findReadyTablePending(int type){
		StringBuffer findSql=new StringBuffer();
//		findSql.append("select * from ").append(openHelper.TABLE_PENDING).append(" where type=").append(type);
		findSql.append("select * from ").append(openHelper.TABLE_PENDING).append(" where type=").append(type).append(" and status <> ").append(TablePending.STATUS_ERROR_SERVER).append(" and status <> ").append(TablePending.STATUS_ERROR_USER);
		Cursor cursor = openHelper.query(findSql.toString(), null);
		TablePending tablePending = null;
		if (cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				tablePending = putTablePending(cursor);
			}
		}
		cursor.close();
		return tablePending;
	}
	
	public void updateTablePendingStatusById(int id,int status){
		StringBuffer sql = new StringBuffer();
		sql.append("update ").append(openHelper.TABLE_PENDING).append(" set status=").append(status).append(" where id=").append(id);
		openHelper.execSQL(sql.toString());
	}
	
	public void updateTablePendingErrorStatusToReadyById(int id){
		StringBuffer sql = new StringBuffer();
		sql.append("update ").append(openHelper.TABLE_PENDING).append(" set status=").append(TablePending.STATUS_READY).append(",numberOfTimes=0").append(" where id=").append(id);
		openHelper.execSQL(sql.toString());
	}
	
	public void updateTablePendingNumberOfTimesById(int id,int times){
		StringBuffer sql = new StringBuffer();
		sql.append("update ").append(openHelper.TABLE_PENDING).append(" set numberOfTimes=").append(times).append(" where id=").append(id);
		openHelper.execSQL(sql.toString());
	}
	
	public void updateAllTablePendingToReady(){
		StringBuffer sql = new StringBuffer();
		sql.append("update ").append(openHelper.TABLE_PENDING).append(" set status=").append(TablePending.STATUS_READY).append(" where status<>").append(TablePending.STATUS_READY).append(" and status<>").append(TablePending.STATUS_SUBIMTTING);
		openHelper.execSQL(sql.toString());
	}
	
	public void updateAllReadyToNetWorkError(){
		StringBuffer sql = new StringBuffer();
		sql.append("update ").append(openHelper.TABLE_PENDING).append(" set status=").append(TablePending.STATUS_ERROR_NETWORK).append(" where status=").append(TablePending.STATUS_READY).append(" or status=").append(TablePending.STATUS_SUBIMTTING);
		openHelper.execSQL(sql.toString());
	}
	
	public int updateAllNetWorkErrorToReady(){
		ContentValues cv = new ContentValues();
		cv.put("status", TablePending.STATUS_READY);
		cv.put("numberOfTimes", 0);
		return openHelper.update(openHelper.TABLE_PENDING, cv, "status=?", new String[]{TablePending.STATUS_ERROR_NETWORK+""});
	}
	
	public void updateStatusToReady(String status){
		StringBuffer sql = new StringBuffer();
		sql.append("update ").append(openHelper.TABLE_PENDING).append(" set status=").append(TablePending.STATUS_READY).append(" where status in (").append(status).append(")");
		openHelper.execSQL(sql.toString());
	}
	
	public TablePending findTablePendingId(int id){
		StringBuffer findSql=new StringBuffer();
		findSql.append("select * from ").append(openHelper.TABLE_PENDING).append(" where id = ").append(id);
		Cursor cursor = openHelper.query(findSql.toString(), null);
		TablePending tablePending = null;
		if (cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				tablePending = putTablePending(cursor);
			}
		}
		cursor.close();
		return tablePending;
	}
	
	public void removeTablePendingById(int id){
		StringBuffer sql = new StringBuffer();
		sql.append("delete from ").append(openHelper.TABLE_PENDING).append(" where id = ").append(id);
		openHelper.execSQL(sql.toString());
	}
	
	public void removeFailTablePending(){
		StringBuffer sql = new StringBuffer();
		sql.append("delete from ").append(openHelper.TABLE_PENDING).append(" where status = ").append(TablePending.STATUS_ERROR_SERVER).append(" or status = ").append(TablePending.STATUS_ERROR_USER);
		openHelper.execSQL(sql.toString());
	}

	private TablePending putTablePending(Cursor cursor){
		int i=0;
		TablePending tablePending = new TablePending();
		tablePending.setId(cursor.getInt(i++));
		tablePending.setType(cursor.getInt(i++));
		tablePending.setStatus(cursor.getInt(i++));
		tablePending.setTitle(cursor.getString(i++));
		tablePending.setContent(cursor.getString(i++));
		tablePending.setCreateDate(cursor.getString(i++));
		tablePending.setNumberOfTimes(cursor.getInt(i++));
		tablePending.setRequest(new CoreHttpPendingRequest(cursor.getString(i++)));
//		tablePending.setNote(cursor.getString(i++));
		return tablePending;
	}

	private ContentValues putContentValues(TablePending tablePending){
		ContentValues cv = new ContentValues();
		cv.put("type", tablePending.getType());
		cv.put("status", tablePending.getStatus());
		cv.put("title", tablePending.getTitle());
		cv.put("content", tablePending.getContent());
		cv.put("createDate", tablePending.getCreateDate());
		cv.put("numberOfTimes", tablePending.getNumberOfTimes());
		cv.put("package", tablePending.getRequest().toJsonString());
//		cv.put("note", tablePending.getNote());
		return cv;
	}

}
