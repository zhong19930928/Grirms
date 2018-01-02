package com.yunhu.yhshxc.database;

import gcg.org.debug.JLog;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yunhu.yhshxc.bo.VisitStore;
import com.yunhu.yhshxc.utility.PublicUtils;

/**
 * 拜访线路
 * @author jishen 数据库操作
 */
public class VisitStoreDB {
	private String TAG="VisitStoreDB";

	private DatabaseHelper openHelper;

	public VisitStoreDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}
	
	public List<VisitStore> findAll() {
		List<VisitStore> list = new ArrayList<VisitStore>();
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.VISIT_STORE_TABLE);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor.getCount() > 0) {
			VisitStore visitStore;
			while (cursor.moveToNext()) {
				visitStore =putVisitStore(cursor);
				list.add(visitStore);
			}
		}
		cursor.close();
		
		return list;
	}

	/**
	 * 查找
	 * @param way_id 线路ID
	 * @param planId 计划ID
	 * @return
	 */
	public List<VisitStore> findAllVisitStoreList(int way_id,int planId) {
		List<VisitStore> list = new ArrayList<VisitStore>();
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.VISIT_STORE_TABLE).append(" where way_id=").append(way_id);
		sql.append(" and planId=").append(planId);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor.getCount() > 0) {
			VisitStore visitStore;
			while (cursor.moveToNext()) {
				visitStore =putVisitStore(cursor);
				list.add(visitStore);
			}
		}
		JLog.d(TAG, "findAllVisitStoreList==>"+sql.toString());
		cursor.close();
		
		return list;
	}
	
	/**
	 * 查找
	 * @param wayIdStr 线路ID
	 * @param planId 计划ID
	 * @return
	 */
	public List<VisitStore> findAllVisitStoreList(String wayIdStr,int planId) {
		List<VisitStore> list = new ArrayList<VisitStore>();
		if(PublicUtils.isIntegerArray(wayIdStr)){
			StringBuffer sql=new StringBuffer();
			sql.append("select * from ").append(openHelper.VISIT_STORE_TABLE).append(" where way_id in (").append(wayIdStr).append(")");
			sql.append(" and planId=").append(planId);
			Cursor cursor = openHelper.query(sql.toString(),null);
			if (cursor.getCount() > 0) {
				VisitStore visitStore;
				while (cursor.moveToNext()) {
					visitStore =putVisitStore(cursor);
					list.add(visitStore);
				}
			}
			JLog.d(TAG, "findAllVisitStoreList==>"+sql.toString());
			cursor.close();
		}
		
		return list;
	}

	/**
	 * 查找
	 * @param way_id 线路ID
	 * @param targetid 数据ID
	 * @param store_id 店面ID
	 * @param planId 计划ID
	 * @return
	 */
	public VisitStore findVisitStore(int way_id, int targetid, int store_id,int planId) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.VISIT_STORE_TABLE).append(" where way_id=").append(way_id);
		sql.append(" and targetid=").append(targetid).append(" and store_id=").append(store_id).append(" and planId=").append(planId);
		Cursor cursor = db.rawQuery(sql.toString(), null);
		VisitStore visitStore = null;
		if (cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				visitStore =putVisitStore(cursor);
			}
		}
		JLog.d(TAG, "findVisitStore==>"+sql.toString());
		cursor.close();
		
		return visitStore;
	}
	
	/**
	 * 查找
	 * @param targetId 数据ID
	 * @return
	 */
	public List<VisitStore> findVisitStoreListByTargetId(int targetId) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		List<VisitStore> list = new ArrayList<VisitStore>();
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.VISIT_STORE_TABLE).append(" where targetid=").append(targetId);
		
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor.getCount() > 0) {
			VisitStore visitStore;
			while (cursor.moveToNext()) {
				visitStore =putVisitStore(cursor);
				list.add(visitStore);
			}
		}
		cursor.close();
		
		return list;
	}
	
	/**
	 * 查找
	 * @param targetId 数据ID
	 * @return
	 */
	public VisitStore findVisitStoreListByStoreId(int storeid) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		StringBuffer sql=new StringBuffer();
		VisitStore visitStore = null;
		sql.append("select * from ").append(openHelper.VISIT_STORE_TABLE).append(" where store_id=").append(storeid);
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				visitStore =putVisitStore(cursor);
			}
		}
		cursor.close();
		return visitStore;
	}
	
	/**
	 * 插入
	 * @param visitStore
	 * @return
	 */
	public Long insertVisitStore(VisitStore visitStore) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues cv = putContentValues(visitStore);
		Long id = db.insert(openHelper.VISIT_STORE_TABLE, null, cv);
		
		return id;
	}

	/**
	 * 修改
	 * @param visitStore
	 * @return
	 */
	public int updateVisitStoreById(VisitStore visitStore) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues cv = putContentValues(visitStore);
		int id = db.update(openHelper.VISIT_STORE_TABLE, cv, "id=?",
				new String[] { visitStore.getId() + "" });
		
		return id;
	}
	
	/**
	 * 修改checkin和checkout状态
	 * @param targetId 数据ID
	 * @param checkin 进店状态
	 * @param checkout 离店状态
	 */
	public void updateVisitStore(int targetId,int checkin,int checkout,int isCheck,int storeDistance,String locType,String isAddress,String isNewLoc,int isNoWait) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		StringBuffer sql = new StringBuffer();
		sql.append("update ").append(openHelper.VISIT_STORE_TABLE);
		sql.append(" set isCheckin =").append(checkin);
		sql.append(",isCheckout=").append(checkout);
		sql.append(",isCheck=").append(isCheck);
		sql.append(",storeDistance=").append(storeDistance);
		sql.append(",loc_type= '").append(locType).append("'");
		sql.append(",is_address= '").append(isAddress).append("'");
		sql.append(",is_anew_loc= '").append(isNewLoc).append("'");
		sql.append(",is_no_wait= ").append(isNoWait);
		sql.append(" where targetid =").append(targetId);
		db.execSQL(sql.toString());
		JLog.d(TAG, "updateVisitStore="+targetId+"=>checkin:"+checkin+" checkout:"+checkout);
		
	}

	/**
	 * 修改
	 * @param visitStore
	 * @return
	 */
	public int updateVisitStoreByStoreIdAndWayId(VisitStore visitStore) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues cv = putContentValues(visitStore);
		int id = db.update(
				openHelper.VISIT_STORE_TABLE,
				cv,
				"store_id=? and way_id=? and planId=?",
				new String[] { String.valueOf(visitStore.getStoreId()),
						String.valueOf(visitStore.getWayId()),String.valueOf(visitStore.getPlanId()) });
		
		return id;
	}

	/**
	 * 根据线路ID 店面ID 计划ID删除
	 * @param wayId 线路ID
	 * @param storeId 店面ID
	 * @param planId 计划ID
	 * @return
	 */
	public int removeVisitStore(int wayId, int storeId,int planId) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		int id = db.delete(openHelper.VISIT_STORE_TABLE,
				"way_id=? and store_id=? and planId=?", new String[] {
						String.valueOf(wayId), String.valueOf(storeId),String.valueOf(planId) });
		
		return id;
	}
	
	/**
	 * 根据线路ID删除
	 * @param wayIdStr
	 */
	public void removeVisitStore(int planId,String wayIdStr) {
		if(PublicUtils.isIntegerArray(wayIdStr)){
			String sql = "delete from "+openHelper.VISIT_STORE_TABLE+" where planId="+planId+" and way_id in ("+wayIdStr+")";
			openHelper.execSQL(sql);
		}
	}
	
	/**
	 * 根据线路ID 和计划ID删除店面
	 * @param wayId 线路ID
	 * @param planId 计划ID
	 * @return
	 */
	public int removeVisitStore(int wayId,int planId) {
		int id = openHelper.delete(openHelper.VISIT_STORE_TABLE,
				"way_id=? and planId=?", new String[] {String.valueOf(wayId),String.valueOf(planId)});
		
		return id;
	}
	
	private ContentValues putContentValues(VisitStore visitStore){
		ContentValues cv = new ContentValues();
		cv.put("store_id", visitStore.getStoreId());
		cv.put("name", visitStore.getName());
		cv.put("way_id", visitStore.getWayId());
		cv.put("targetid", visitStore.getTargetid());
		cv.put("submitDate", visitStore.getSubmitDate());
		cv.put("isCheckout", visitStore.getIsCheckout()+"");
		cv.put("isCheckin", visitStore.getIsCheckin()+"");
		cv.put("submitDate", visitStore.getSubmitDate());
		cv.put("planId", visitStore.getPlanId()+"");
		cv.put("deleteDate", visitStore.getDeleteDate());
		cv.put("lon", visitStore.getLon());
		cv.put("lat", visitStore.getLat());
		cv.put("isCheck", visitStore.getIsCheck()+"");
		cv.put("storeDistance", visitStore.getStoreDistance()+"");
		cv.put("loc_type", visitStore.getLocType());
		cv.put("is_address", visitStore.getIsAddress());
		cv.put("is_anew_loc", visitStore.getIsNewLoc());
		cv.put("is_no_wait", visitStore.getIsNoWait());
		cv.put("submit_num", visitStore.getSubmitNum());
		return cv;
	}
	
	private VisitStore putVisitStore(Cursor cursor){
		int i = 0;
		VisitStore visitStore = new VisitStore();
		visitStore.setId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		visitStore.setStoreId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		visitStore.setName(cursor.getString(i++));
		visitStore.setWayId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		visitStore.setTargetid(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		visitStore.setIsCheckin(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		visitStore.setIsCheckout(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		visitStore.setSubmitDate(cursor.getString(i++));
		visitStore.setPlanId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		visitStore.setDeleteDate(cursor.getString(i++));
		visitStore.setLon(cursor.getString(i++));
		visitStore.setLat(cursor.getString(i++));
		visitStore.setIsCheck(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		visitStore.setStoreDistance(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		visitStore.setLocType(cursor.getString(i++));
		visitStore.setIsAddress(cursor.getString(i++));
		visitStore.setIsNewLoc(cursor.getString(i++));
		visitStore.setIsNoWait(cursor.isNull(i) ? 0 : cursor.getInt(i));i++;
		visitStore.setSubmitNum(cursor.getInt(i++));
		return visitStore;
	}
}
