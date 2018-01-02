package com.yunhu.yhshxc.database;

import gcg.org.debug.JLog;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.bo.Submit;
import com.yunhu.yhshxc.utility.Constants;

/**
 * 提交表单数据库
 * @author jishen 数据库操作
 */
public class SubmitDB {
	private String TAG = "SubmitDB";
	private DatabaseHelper openHelper;

	public SubmitDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}

	/**
	 * 
	 * @param submit插入一条submit记录
	 */
	public Long insertSubmit(Submit submit) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues contentValues = putContentValues(submit);
		Long id = db.insert(openHelper.SUBMIT_TABLE, null, contentValues);
		
		JLog.d(TAG, "插入Submit:"+id);
		return id;
	}

	/**
	 * 
	 * @return表格中所有submit集合
	 */
	public List<Submit> findAllSubmit() {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		List<Submit> list = new ArrayList<Submit>();
//		String sql = "select * from "+ openHelper.SUBMIT_TABLE;
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.SUBMIT_TABLE);
		
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putSubmit(cursor));
			}
		}
		cursor.close();
		
		return list;
	}
	/**
	 * 根据parentId查找
	 * 
	 */
	public List<Submit> findSubmitByParentId(int parentId) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		List<Submit> list = new ArrayList<Submit>();
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.SUBMIT_TABLE).append(" where parentId = ").append(parentId);
		
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putSubmit(cursor));
			}
		}
		cursor.close();
		
		return list;
	}

	/**
	 * 
	 * @param id查询是否有该条记录
	 * @return true表示有该条记录，fasle表示没有该条记录
	 */
	public int selectSubmitId(Integer plan_id, Integer way_id,
			Integer store_id, Integer targetid, Integer targetType) {
		int id = Constants.DEFAULTINT;
		StringBuffer sql=new StringBuffer(); 
		SQLiteDatabase db = openHelper.getReadableDatabase();
		if (targetType == Menu.TYPE_VISIT) {
			sql.append("select id from ").append( openHelper.SUBMIT_TABLE).append(" where plan_id= ").append(plan_id);
			sql.append(" and way_id=").append(way_id).append(" and store_id=").append(store_id).append(" and targetid=");
			sql.append(targetid).append(" and targetType=").append(targetType);
		} else if (targetType == Menu.IS_STORE_ADD_MOD || targetType == Menu.TYPE_NEW_TARGET || targetType == Menu.TYPE_MODULE||targetType == Menu.TYPE_ATTENDANCE || targetType == Menu.TYPE_NEW_ATTENDANCE || targetType == Menu.TYPE_NEARBY  || targetType == Menu.TYPE_ORDER3) {
			sql.append("select id from ").append(openHelper.SUBMIT_TABLE).append(" where targetid= ");
			sql.append(targetid).append(" and targetType=").append(targetType);
		} else {
			JLog.d(TAG, "targetType==>" + targetType);
		}
		JLog.d(TAG, "selectSubmitIdSql==>" + sql);
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				id = cursor.getInt(cursor.getColumnIndex("id"));
			}
		}
		cursor.close();
		return id;
	}
	/**
	 * 
	 * @param id查询是否有该条记录
	 * @return true表示有该条记录，fasle表示没有该条记录
	 */
	public int selectSubmitIdNotCheckOut(Integer plan_id, Integer way_id,
			Integer store_id, Integer targetid, Integer targetType,int state) {
		int id = Constants.DEFAULTINT;
		StringBuffer sql=new StringBuffer();
		SQLiteDatabase db = openHelper.getReadableDatabase();
		if (targetType == Menu.TYPE_VISIT) {
			
			sql.append("select id from ").append(openHelper.SUBMIT_TABLE).append(" where plan_id= ").append(plan_id);
			sql.append(" and way_id=").append(way_id).append(" and store_id=").append(store_id);
			sql.append(" and targetid=").append(targetid).append(" and targetType=").append(targetType).append(" and state=").append(state);
		} else if (targetType == Menu.IS_STORE_ADD_MOD || targetType == Menu.TYPE_NEW_TARGET || targetType == Menu.TYPE_MODULE||targetType == Menu.TYPE_ATTENDANCE||targetType == Menu.TYPE_NEW_ATTENDANCE || targetType == Menu.TYPE_NEARBY  || targetType == Menu.TYPE_ORDER3) {
			sql.append("select id from ").append(openHelper.SUBMIT_TABLE).append(" where targetid= ").append(targetid).append(" and targetType=");
			sql.append(targetType).append(" and state=").append(state);
		} else {
			return id;
		}
		JLog.d(TAG, "selectSubmitIdSql==>" + sql.toString());
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				id = cursor.getInt(cursor.getColumnIndex("id"));
			}
		}
		cursor.close();
		
		JLog.d(TAG, "selectSubmitIdNotCheckOutID==>" + id);
		return id;
	}

	/**
	 * 根据数据ID和提交状态查询
	 * @param state 提交状态
	 * @param targetId 数据ID
	 * @return
	 */
	public List<Submit> findAllSubmitDataByStateAndTargetId(Integer state,int targetId) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		List<Submit> list = new ArrayList<Submit>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(openHelper.SUBMIT_TABLE)
				.append(" where state = ").append(state).append(" and targetid=").append(targetId+"");
		JLog.d(TAG,"SubmitDB" + "findAllSubmitData====>" + sql);
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putSubmit(cursor));
			}
		}
		cursor.close();
		
		return list;
	}
	
	/**
	 * 根据提交状态查找
	 * @param state 提交状态
	 * @return
	 */
	public List<Submit> findAllSubmitDataByState(Integer state) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		List<Submit> list = new ArrayList<Submit>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(openHelper.SUBMIT_TABLE)
		.append(" where state = ").append(state);
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putSubmit(cursor));
			}
		}
		cursor.close();
		
		return list;
	}

	/**
	 * 根据条件查出指定提交数据
	 * 
	 * @param baseUrl
	 * @param state
	 * @param planId
	 * @param wayId
	 * @param storeId
	 * @param targetid
	 * @return
	 */
	public Submit findSubmit(Integer state, Integer planId,
			Integer wayId, Integer storeId, Integer targetid, Integer targetType,String timestamp,
			String checkinGis,String checkinTime,String checkoutTime,String checkoutGis) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(openHelper.SUBMIT_TABLE).append(" where 1=1 ");
		if (state != null) {
			sql.append(" and state =").append(state);
		}
		if (planId != null) {
			sql.append(" and plan_id =").append(planId);
		}
		if (wayId != null) {
			sql.append(" and way_id =").append(wayId);
		}
		if (storeId != null) {
			sql.append(" and store_id =").append(storeId);
		}
		if (targetid != null) {
			sql.append(" and targetid =").append(targetid);
		}
		if (timestamp != null) {
			sql.append(" and timestamp =").append(timestamp);
		}
		if (targetType != null) {
			sql.append(" and targetType =").append(targetType);
		}
		if (checkinGis != null) {
			sql.append(" and checkinGis =").append(checkinGis);
		}
		if (checkinTime != null) {
			sql.append(" and checkinTime =").append(checkinTime);
		}
		if (checkoutTime != null) {
			sql.append(" and checkoutTime =").append(checkoutTime);
		}
		if (checkoutGis != null) {
			sql.append(" and checkoutGis =").append(checkoutGis);
		}
		Cursor cursor = db.rawQuery(sql.toString(), null);
		Submit submit = null;
		if (cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				submit = putSubmit(cursor);
			}
		}
		JLog.d(TAG, "findSubmitSql==>"+sql.toString());
		cursor.close();
		
		return submit;
	}

	/**
	 * 根据主键和状态查找
	 * @param id 主键
	 * @param state 状态
	 * @return
	 */
	public Submit findSubmitByIdandState(int id ,int state) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from "
				+ openHelper.SUBMIT_TABLE + " where id = " + id+" and state="+state, null);
		Submit submit = null;
		if (cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				submit = putSubmit(cursor);
			}
		}
		cursor.close();
		
		return submit;
	}
	/**
	 * 根据targetid 和state来查询submit
	 * @param targetId
	 * @param state
	 * @return
	 */
	public Submit findSubmitByTargetId(int targetId,int state) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from "
				+ openHelper.SUBMIT_TABLE + " where targetid = " + targetId+" and state="+state, null);
		Submit submit = null;
		if (cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				submit = putSubmit(cursor);
			}
		}
		cursor.close();
		
		return submit;
	}
	
	/**
	 * 根据时间戳查找submit
	 * @param timestamp
	 * @return
	 */
	public Submit findSubmitByTimestamp(String timestamp) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from "+ openHelper.SUBMIT_TABLE + " where timestamp = '" + timestamp+"'", null);
		Submit submit = null;
		if (cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				submit = putSubmit(cursor);
			}
		}
		cursor.close();
		return submit;
	}
	
	/**
	 * 根据主键查找
	 * @param id 主键
	 * @return
	 */
	public Submit findSubmitById(int id) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from "
				+ openHelper.SUBMIT_TABLE + " where id = " + id, null);
		Submit submit = null;
		if (cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				submit = putSubmit(cursor);
			}
		}
		cursor.close();
		
		return submit;
	}
	

	private Submit putSubmit(Cursor cursor) {
		int i = 0;
		Submit submit = new Submit();
		submit.setId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		submit.setParentId(cursor.getInt(i++));
		submit.setState(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		submit.setPlanId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		submit.setWayId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		submit.setWayName(cursor.getString(i++));
		submit.setStoreId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		submit.setStoreName(cursor.getString(i++));
		submit.setTargetid(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		submit.setTimestamp(cursor.getString(i++));
		submit.setTargetType(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		submit.setCheckinGis(cursor.getString(i++));
		submit.setCheckinTime(cursor.getString(i++));
		submit.setCheckoutTime(cursor.getString(i++));
		submit.setCheckoutGis(cursor.getString(i++));
		submit.setDoubleId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		submit.setModType(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		submit.setSendUserId(cursor.getString(i++));
		submit.setUpCtrlMain(cursor.getString(i++));
		submit.setUpCtrlTable(cursor.getString(i++));
		submit.setCodeUpdate(cursor.getString(i++));
		submit.setCodeUpdateTab(cursor.getString(i++));
		submit.setVisitNumbers(cursor.getInt(i++));
		submit.setDoubleMasterTaskNo(cursor.getString(i++));
		submit.setDoubleBtnType(cursor.getString(i++));
		submit.setContentDescription(cursor.getString(i++));
		submit.setMenuId(cursor.getInt(i++));
		submit.setMenuType(cursor.getInt(i++));
		submit.setMenuName(cursor.getString(i++));
		submit.setIsCacheFun(cursor.getInt(i++));
		return submit;
	}

	/**
	 * 根据条件删除指定提交数据
	 */
	public void deleteSubmitById(Integer id) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		StringBuffer sql=new StringBuffer();
		sql.append("delete from ").append(openHelper.SUBMIT_TABLE).append(" where id = ").append(id);
		db.execSQL(sql.toString());
		JLog.d(TAG,"deleteSubmitById success==>");
		
	}
	/**
	 * 根据条件删除指定提交数据
	 */
	public void deleteSubmitByState(int state,int targetId) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		
		StringBuffer sql=new StringBuffer();
		sql.append("delete from ").append(openHelper.SUBMIT_TABLE).append(" where state = ").append(state);
		sql.append(" and targetid= ").append(targetId);
		db.execSQL(sql.toString());
		JLog.d(TAG, "deleteSubmitByState success==>");
		
	}

	/*
	 * 更改CHECKOUT状态
	 */
	public void updateState(Submit submit) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		StringBuffer sql=new StringBuffer();
		sql.append("update ").append(openHelper.SUBMIT_TABLE).append(" set state= ").append(submit.getState()).append(" where state = ");
		sql.append(Submit.STATE_NO_SUBMIT).append(" and id=").append(submit.getId());
		db.execSQL(sql.toString());
		
		JLog.d(TAG, "update时间戳" + submit.getTimestamp());
	}
	/*
	 * 更改CHECKOUT时间
	 */
	public void updateTimestamp(Submit submit) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		String sql = "update " + openHelper.SUBMIT_TABLE + " set timestamp= "
				+ submit.getTimestamp() + " where timestamp is "
				+ null + " and id=" + submit.getId();
		db.execSQL(sql);
		
		JLog.d(TAG, "updateState" + submit.getState());
	}

	public void updateTargetType(int targetType,int submitId){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		String sql = "update "+openHelper.SUBMIT_TABLE+" set targetType= "+targetType+" where id="+submitId;
		db.execSQL(sql);
	}

	
	/**
	 * 修改submit
	 * @param submit
	 */
	public void updateSubmit(Submit submit) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues cv = putContentValues(submit);
		db.update( openHelper.SUBMIT_TABLE, cv, " id=? ", new String[] { submit.getId() + "" });

		JLog.d(TAG, "update更新后时间戳==>" + submit.getTimestamp());
	}
	
	/**
	 * 根据parentId更爱submit状态
	 * @param parentId
	 */
	public void updateSubmitStateByParentId(int parentId) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		StringBuffer sql=new StringBuffer();
		sql.append(" update ").append(openHelper.SUBMIT_TABLE).append(" set state = ").append(Submit.STATE_SUBMITED);
		sql.append(" where parentId = ").append(parentId);
		db.execSQL(sql.toString());
	}
	/**
	 * 根据parentId更爱submit状态
	 * @param parentId
	 */
	public void updateSubmitStateByParentIdNo(int parentId) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		StringBuffer sql=new StringBuffer();
		sql.append(" update ").append(openHelper.SUBMIT_TABLE).append(" set state = ").append(Submit.STATE_NO_SUBMIT);
		sql.append(" where parentId = ").append(parentId);
		db.execSQL(sql.toString());
	}

	
	private ContentValues putContentValues(Submit submit) {
		ContentValues cv = new ContentValues();
		cv.put("id", submit.getId());
		cv.put("parentId", submit.getParentId());
		cv.put("state", submit.getState());
		cv.put("plan_id", submit.getPlanId()==null?0:submit.getPlanId());
		cv.put("way_id", submit.getWayId()==null?0:submit.getWayId());
		cv.put("way_name", submit.getWayName());
		cv.put("store_id", submit.getStoreId()==null?0:submit.getStoreId());
		cv.put("store_name", submit.getStoreName());
		cv.put("targetid", submit.getTargetid());
		cv.put("timestamp", submit.getTimestamp());
		cv.put("targetType", submit.getTargetType());
		cv.put("checkinGis", submit.getCheckinGis());
		cv.put("checkinTime", submit.getCheckinTime());
		cv.put("checkoutTime", submit.getCheckoutTime());
		cv.put("checkoutGis", submit.getCheckoutGis());
		cv.put("doubleId", submit.getDoubleId());
		cv.put("modType", submit.getModType());
		cv.put("sendUserId", submit.getSendUserId());
		cv.put("upCtrlMain", submit.getUpCtrlMain());
		cv.put("upCtrlTable", submit.getUpCtrlTable());
		cv.put("codeUpdate", submit.getCodeUpdate());
		cv.put("codeUpdateTab", submit.getCodeUpdateTab());
		cv.put("storeVisitNumbers", submit.getVisitNumbers());
		cv.put("doubleMasterTaskNo", submit.getDoubleMasterTaskNo());
		cv.put("doubleBtnType", submit.getDoubleBtnType());
		cv.put("contentDescription", submit.getContentDescription());
		cv.put("menuId", submit.getMenuId());
		cv.put("menuType", submit.getModType());
		cv.put("menuName", submit.getMenuName());
		cv.put("isCacheFun",submit.getIsCacheFun());
		return cv;
	}

}
