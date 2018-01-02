package com.yunhu.yhshxc.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yunhu.yhshxc.bo.VisitStore;
import com.yunhu.yhshxc.bo.VisitWay;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.PublicUtils;

/**
 * 拜访线路
 * @author jishen
 *	数据库操作
 */
public class VisitWayDB {

	private DatabaseHelper openHelper;
	private Context context = null;
	public VisitWayDB(Context context) {
		this.context = context;
		openHelper = DatabaseHelper.getInstance(context);
	}
	
	/**
	 * 查找日期小于开始日期的线路
	 * @param startDate 开始日期
	 * @return 
	 */
	public List<VisitWay> findWayListByStartdate(String startDate){
		SQLiteDatabase db = openHelper.getReadableDatabase();
		List<VisitWay> list = new ArrayList<VisitWay>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(openHelper.VISIT_WAY_TABLE).append(" where startdate <= '").append(startDate).append("'").append(" order by ").append("plan_id");
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putVisitWay(cursor));
			}
		}
		cursor.close();
		
		return list;
	}
	/**
	 * 根据计划ID查找线路
	 * @param planid 计划ID
	 * @return 该计划id下的所有线路
	 */
	public List<VisitWay> findWayListByPlanId(int planid){
		SQLiteDatabase db = openHelper.getReadableDatabase();
		List<VisitWay> list = new ArrayList<VisitWay>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(openHelper.VISIT_WAY_TABLE).append(" where plan_id = ").append(planid);
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putVisitWay(cursor));
			}
		}
		cursor.close();
		
		return list;
	}
	
	/**
	 * 插入一条线路
	 * @param visitWay 要插入的线路的实例
	 * @return 插入的数据的id
	 */
	public Long insertVisitWay(VisitWay visitWay) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues cv=putContentValues(visitWay);
		Long id = db.insert(openHelper.VISIT_WAY_TABLE, null, cv);
		
		return id;
	}
	
	/**
	 * 根据id更改一条线路的数据
	 * @param visitWay 要更改的线路的实例
	 * @return 被修改的数据的ID
	 */
	public int updateVisitWayById(VisitWay visitWay) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues cv=putContentValues(visitWay);
		int id = db.update(openHelper.VISIT_WAY_TABLE, cv, "id=?", new String[]{visitWay.getId()+""});
		
		return id;
	}
	/**
	 * 根据线路id删除线路
	 * @param wayId 要删除的线路的线路ID
	 * @return 被删除的线路的数据的ID
	 */
	public int removeVisitWayByWayId(int wayId) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		int id = db.delete(openHelper.VISIT_WAY_TABLE,
						"way_id=?",new String[] { String.valueOf(wayId)});
		
		return id;
	}
	
	/**
	 * 删除一个计划
	 * @param planId 要删除的计划的计划ID
	 */
	public void removeVisitWayByPlanId(int planId) {
		/* 因为是一个计划下只能有一个线路，所以此处无错，
		 * 若需求改为一个计划可以有多条线路，则需要修改此处
		 * */
		List<VisitWay> wayList = this.findWayListByPlanId(planId);
		if(!wayList.isEmpty()){
			StringBuffer sb = new StringBuffer();
			for(VisitWay way:wayList){
				sb.append(",").append(way.getWayId());
				Constants.changed_visit_task_notice = way.getName();
			}
			String wayIdStr = sb.length() > 0 ? sb.substring(1) : "0";//总计出需要删除的线路，用来删除店面
			VisitStoreDB storeDB = new VisitStoreDB(context);
			List<VisitStore> storeList = storeDB.findAllVisitStoreList(wayIdStr, planId);
			sb = new StringBuffer();
			for(VisitStore store:storeList){
				if(countPlanIdByTargetid(store.getTargetid()) == 1){
					sb.append(",").append(store.getTargetid());
				}
			}
			String targetidStr = sb.length() > 0 ? sb.substring(1) : "0"; //总计出需要删除的控件
			//开始删除
			new FuncDB(context).removeFuncByTargetid(targetidStr);
			storeDB.removeVisitStore(planId,wayIdStr);
			openHelper.delete(openHelper.VISIT_WAY_TABLE,"plan_id="+planId,null);
		}
		
	}
	
	/**
	 * 根据计划Id删除线路
	 * @param planId 计划id
	 */
	public void removeVisitWay(String planId) {
		if(PublicUtils.isIntegerArray(planId)){
			SQLiteDatabase db = openHelper.getWritableDatabase();
			StringBuffer sql=new StringBuffer();
			sql.append("delete from ").append(openHelper.VISIT_WAY_TABLE).append(" where plan_id in(").append(planId).append(")");
			db.execSQL(sql.toString());
		}
		
	}
	
	
	public int countPlanIdByTargetid(int targetId){
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct way.plan_id from ").append(openHelper.VISIT_WAY_TABLE).append(" way,").append(openHelper.VISIT_STORE_TABLE).append(" store where");
		sql.append(" way.way_id = store.way_id and targetid = ").append(targetId);
		Cursor cursor = openHelper.query(sql.toString(), null);
		int count = cursor.getCount();
		cursor.close();
		return count;
	}
	
	private ContentValues putContentValues(VisitWay visitWay){
		ContentValues cv = new ContentValues();
		cv.put("way_id", visitWay.getWayId());
		cv.put("name", visitWay.getName());
		cv.put("is_order", visitWay.getIsOrder());
		cv.put("plan_id", visitWay.getPlanId());
		cv.put("awokeType", visitWay.getAwokeType());
		cv.put("intervalType", visitWay.getIntervalType());
		cv.put("weekly", visitWay.getWeekly());
		cv.put("fromDate", visitWay.getFromDate());
		cv.put("toDate", visitWay.getToDate());
		cv.put("startdate", visitWay.getStartdate());
		cv.put("cycle_count", visitWay.getCycleCount());
		cv.put("visit_count", visitWay.getVisitCount());
		return cv;
	}
	
	private VisitWay putVisitWay(Cursor cursor){
		int i = 0;
		VisitWay visitWay = new VisitWay();
		visitWay.setId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		visitWay.setWayId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		visitWay.setName(cursor.getString(i++));
		visitWay.setIsOrder(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		visitWay.setPlanId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		visitWay.setAwokeType(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		visitWay.setIntervalType(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		visitWay.setWeekly(cursor.getString(i++));
		visitWay.setFromDate(cursor.getString(i++));
		visitWay.setToDate(cursor.getString(i++));
		visitWay.setStartdate(cursor.getString(i++));
		visitWay.setCycleCount(cursor.getInt(i++));
		visitWay.setVisitCount(cursor.getInt(i++));
		return visitWay;
	}
}
