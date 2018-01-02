package com.yunhu.yhshxc.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.utility.PublicUtils;

/**
 * 
 * @author jishen 数据库操作
 */
public class FuncDB {
	private String TAG = "FuncDB";
	private DatabaseHelper openHelper;

	public FuncDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}
	
	
	/**
	 * 查找必须输入的控件
	 * @param isEmpty 是否为必须输入
	 * @param targetid 数据ID
	 * @param ableStatus 数据状态
	 * @return
	 */
	public List<Func> findFuncListByIsEmpty(int isEmpty,int targetid,String ableStatus) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		List<Func> list=new ArrayList<Func>();
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.FUNC_TABLE).append(" where isEmpty=").append(isEmpty);
		sql.append(" and targetid=").append(targetid);
		sql.append(" and is_edit <> ").append(Func.IS_N);
		sql.append(" and (").append("replenish_able_status is null").append(" or ");
		sql.append(" replenish_able_status like").append("'%").append(",").append(ableStatus).append(",").append("%'").append(")");
		
		Cursor cursor = db.rawQuery(sql.toString(), null);
//		JLog.d(TAG, "findFuncListByIsEmpty==>" + sql.toString());
		Func func = null;
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				func = putFunc(cursor);
				list.add(func);
			}
		}
		cursor.close();
		return list;
	}
	
	/**
	 * 按顺序输入的控件的顺序
	 * @param targetid 数据id
	 * @return 顺序的数组
	 */
	public Integer[] findFuncListByInputOrder(int targetid) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		Integer[] arr=null;
		StringBuffer sql=new StringBuffer();
		sql.append("select distinct input_order from ").append(openHelper.FUNC_TABLE);
		sql.append(" where targetid=").append(targetid).append(" and input_order is  not null").append(" order by input_order");
		
		Cursor cursor = db.rawQuery(sql.toString(), null);
//		JLog.d(TAG, "findFuncListByInputOrder==>" + sql.toString());
		if (cursor.getCount() > 0) {
			arr=new Integer[cursor.getCount()];
			int i=0;
			while (cursor.moveToNext()) {
				arr[i]=cursor.getInt(0);
				i++;
			}
		}
		cursor.close();
		
		return arr;
	}
	
	/**
	 * 查找执行顺序下的控件
	 * @param targetid 数据ID
 	 * @param funcId 控件ID
	 * @param inputOrder 顺序
	 * @return
	 */
	public ArrayList<Func> findFuncListByInputOrder(int targetid,Integer funcId,int inputOrder) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.FUNC_TABLE);
		sql.append(" where targetid=").append(targetid).append(" and input_order =").append(inputOrder);
		if(funcId != null){
			sql.append(" and func_id =").append(funcId);
		}
		
		Cursor cursor = db.rawQuery(sql.toString(), null);
//		JLog.d(TAG, "findFuncListByInputOrder==>" + sql.toString());
		ArrayList<Func> list = new ArrayList<Func>();
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putFunc(cursor));
			}
		}
		cursor.close();
		
		return list;
	}
	
	/**
	 * 根据控件iD和数据ID查找控件
	 * @param func_id 控件id
	 * @param targetId 数据ID
	 * @return
	 */
	public Func findFuncListByFuncIdAndTargetId(String func_id,Integer targetId) {
		Func func = null;
		if (PublicUtils.isInteger(func_id)) {
			SQLiteDatabase db = openHelper.getReadableDatabase();
			StringBuffer sql=new StringBuffer();
			sql.append("select * from ").append(openHelper.FUNC_TABLE).append(" where func_id=").append(func_id);
			sql.append(" and targetid=").append(targetId);
			
			
			Cursor cursor = db.rawQuery(sql.toString(), null);
//			JLog.d(TAG, "findFuncListByFuncIdAndTargetId==>" + sql.toString());
			
			if (cursor.getCount() > 0) {
				if (cursor.moveToNext()) {
					func = putFunc(cursor);
				}
			}
			cursor.close();
		}
		return func;
	}
	
	
	/**
	 * 根据数据ID和默认类型查找控件
	 * @param targetId 数据ID
	 * @param defaultType 默认类型
	 * @return
	 */
	public Func findFuncListByTargetIdAndDefaultType(int targetId,int defaultType) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.FUNC_TABLE);
		sql.append(" where targetid=").append(targetId);
		sql.append(" and type = ").append(Func.TYPE_HIDDEN);
		sql.append(" and defaultType = ").append(defaultType);
		
		
		Cursor cursor = db.rawQuery(sql.toString(), null);
//		JLog.d(TAG, "findFuncListByTargetIdAndDefaultType==>" + sql.toString());
		Func func = null;
		if (cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				func = putFunc(cursor);
			}
		}
		cursor.close();
		
		return func;
	}

	/**
	 * 根据上级ID查找控件
	 * @param parentid 上级ID
	 * @return
	 */
	public List<Func> findFuncListByParentid(Integer parentid) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		List<Func> list = new ArrayList<Func>();
//		String sql = "select * from " + openHelper.FUNC_TABLE
//				+ " where parentid=" + parentid;
		
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.FUNC_TABLE).append(" where parentid=").append(parentid);
		
		Cursor cursor = db.rawQuery(sql.toString(), null);
//		JLog.d(TAG, "findFuncListByParentid==>" + sql.toString());
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putFunc(cursor));
			}
		}
		cursor.close();
		
		return list;
	}

	/**
	 * 查找不可编辑的控件
	 * @param targetid 数据ID
	 * @param ableStatus 数据状态
	 * @param isSearchCanEdit 是否不可编辑
	 * @return
	 */
	public List<Func> findFuncListByTargetidReplash(Integer targetid,String ableStatus,boolean isSearchCanEdit) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		List<Func> list = new ArrayList<Func>();
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.FUNC_TABLE).append(" where targetid=").append(targetid);
		sql.append(" and type <>").append(Func.TYPE_HIDDEN).append(" and type <>").append(Func.TYPE_BUTTON);
		if(isSearchCanEdit){
			sql.append(" and is_edit <>").append(Func.IS_N);
		}
		sql.append(" and (").append("replenish_able_status is null").append(" or ");
		sql.append(" replenish_able_status like").append("'%").append(",").append(ableStatus).append(",").append("%'").append(")");
		sql.append(" order by id");
		
		Cursor cursor = db.rawQuery(sql.toString(), null);
//		JLog.d(TAG, "findFuncListByTargetidReplash==>" + sql.toString());
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putFunc(cursor));
			}
		}
		cursor.close();
		
		return list;
	}
	
	/**
	 * 查询就近拜访的上报控件 不包含店面
	 * @param targetid 数据ID
	 * @param ableStatus 数据状态
	 * @return
	 */
	public List<Func> findNearbyFuncList(Integer targetid,String ableStatus) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		List<Func> list = new ArrayList<Func>();
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.FUNC_TABLE).append(" where targetid=").append(targetid);
		sql.append(" and type <>").append(Func.TYPE_HIDDEN);
		sql.append(" and type <>").append(Func.TYPE_BUTTON);
		sql.append(" and (").append("orgOption is null or orgOption <> ").append(Func.ORG_STORE).append(")");
		sql.append(" and (").append("replenish_able_status is null or replenish_able_status like ").append("'%,").append(ableStatus).append(",%')");
		sql.append(" order by id");
		
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putFunc(cursor));
			}
		}
		cursor.close();
		
		return list;
	}
	
	/**
	 * 查询就近拜访的上报控件 不包含店面
	 * @param targetid 数据ID
	 * @param
	 * @return
	 */
	public List<Func> findNearbyFuncListIsEmpty(int isEmpty,Integer targetid) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		List<Func> list = new ArrayList<Func>();
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.FUNC_TABLE).append(" where targetid=").append(targetid).append(" and isEmpty = ").append(isEmpty );
		sql.append(" order by id");
		
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putFunc(cursor));
			}
		}
		cursor.close();
		
		return list;
	}
	
	/**
	 * 查询就近拜访的店面控件
	 * @param targetid 数据ID
	 * @param ableStatus 数据状态
	 * @return
	 */
	public 	Func findNearbyStoreFunc(Integer targetid,String ableStatus) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		Func func = null;
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.FUNC_TABLE).append(" where targetid=").append(targetid);
		sql.append(" and orgOption = ").append(Func.ORG_STORE);
		sql.append(" and (").append("replenish_able_status is null or replenish_able_status like ").append("'%,").append(ableStatus).append(",%')");
		sql.append(" order by id");
		
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				func = putFunc(cursor);
			}
		}
		cursor.close();
		return func;
	}
	
	/**
	 * 查找不可编辑的控件
	 * @param targetid 数据iD
	 * @param isSearchCanEdit  是否不可编辑
	 * @return
	 */
	public List<Func> findFuncListByTargetid(Integer targetid,boolean isSearchCanEdit) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		List<Func> list = new ArrayList<Func>();
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.FUNC_TABLE).append(" where targetid=").append(targetid);
		if(isSearchCanEdit){
			sql.append(" and is_edit <>").append(Func.IS_N);
		}
		sql.append(" and type <>").append(Func.TYPE_HIDDEN).append(" and type <>").append(Func.TYPE_BUTTON).append(" order by id");
		
		
		Cursor cursor = db.rawQuery(sql.toString(), null);
//		JLog.d(TAG, "findFuncListByTargetid==>" + sql.toString());
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putFunc(cursor));
			}
		}
		cursor.close();
		return list;
	}
	
	/**
	 * 根据数据ID查找控件 不包含隐藏域
	 * @param targetid 数据ID
	 * @return
	 */
	public List<Func> findFuncListByTargetidForPriview(Integer targetid) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		List<Func> list = new ArrayList<Func>();
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.FUNC_TABLE).append(" where targetid=").append(targetid);
		sql.append(" and type <>").append(Func.TYPE_HIDDEN).append(" and type <>").append(Func.TYPE_BUTTON).append(" order by id");
		
		
		Cursor cursor = db.rawQuery(sql.toString(), null);
//		JLog.d(TAG, "findFuncListByTargetid==>" + sql.toString());
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putFunc(cursor));
			}
		}
		cursor.close();
		
		return list;
	}
	
	/**
	 * 根据数据ID查找控件 包含隐藏域
	 * @param targetid 数据ID
	 * @return
	 */
	public List<Func> findFuncListContainHiddenByTargetid(Integer targetid) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		List<Func> list = new ArrayList<Func>();
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.FUNC_TABLE).append(" where targetid=").append(targetid);
		sql.append(" and type <>").append(Func.TYPE_BUTTON).append(" order by id");
		
		
		Cursor cursor = db.rawQuery(sql.toString(), null);
//		JLog.d(TAG, "findFuncListByTargetid==>" + sql.toString());
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putFunc(cursor));
			}
		}
		cursor.close();
		
		return list;
	}
	
	/**
	 * 根据控件类型 数据ID 数据状态查找控件
	 * @param targetid 数据ID
	 * @param type 控件类型
	 * @param status 数据状态
	 * @return
	 */
	public List<Func> findFuncListByType(Integer targetid,int type,String status){
		SQLiteDatabase db = openHelper.getReadableDatabase();
		List<Func> list = new ArrayList<Func>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(openHelper.FUNC_TABLE);
		sql.append(" where targetid=").append(targetid);
		sql.append(" and type =").append(type);
		
		if(TextUtils.isEmpty(status)){//目前等于空的只有上报和下发
			status="0";
		}
		sql.append(" and (").append("replenish_able_status is null");
		sql.append(" or replenish_able_status like").append("'%").append(",").append(status).append(",").append("%')");
		Cursor cursor = db.rawQuery(sql.toString(), null);
//		JLog.d(TAG, "findFuncListByType==>" + sql.toString());
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putFunc(cursor));
			}
		}
		cursor.close();
		
		return list;
	}
	/**
	 * 根据数据状态和数据ID查找控件
	 * @param targetid 数据ID
	 * @param ableStatus 控件状态
	 * @return
	 */
	public List<Func> findButtonFuncListReplenish(Integer targetid,String ableStatus){
		SQLiteDatabase db = openHelper.getReadableDatabase();
		List<Func> list = new ArrayList<Func>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(openHelper.FUNC_TABLE);
		sql.append(" where targetid=").append(targetid);
		sql.append(" and type =").append(Func.TYPE_BUTTON);
		sql.append(" and (").append("replenish_able_status is null").append(" or ");
		sql.append(" replenish_able_status like").append("'%").append(",").append(ableStatus).append(",").append("%'").append(")");
		Cursor cursor = db.rawQuery(sql.toString(), null);
//		JLog.d(TAG, "findButtonFuncListReplenish==>" + sql.toString());
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putFunc(cursor));
			}
		}
		cursor.close();
		
		return list;
	}
	
	/**
	 * 查找下发的控件
	 * @param targetid 数据ID
	 * @param ableStatus 控件状态
	 * @return
	 */
	public List<Func> findFuncListIssude(Integer targetid,String ableStatus){//0
		
		SQLiteDatabase db = openHelper.getReadableDatabase();
		List<Func> list = new ArrayList<Func>();
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.FUNC_TABLE).append(" where targetid=").append(targetid);
		sql.append(" and type <>").append(Func.TYPE_HIDDEN).append(" and type <>").append(Func.TYPE_BUTTON);
		sql.append(" and (").append("replenish_able_status is null").append(" or ");
		sql.append(" replenish_able_status like").append("'%").append(",").append(ableStatus).append(",").append("%'").append(")");
		sql.append(" order by id");
		
		Cursor cursor = db.rawQuery(sql.toString(), null);
//		JLog.d(TAG, "findFuncListByTargetidReplash==>" + sql.toString());
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putFunc(cursor));
			}
		}
		cursor.close();
		
		return list;
	}
	
	/**
	 * 根据数据ID 查找双向第一列显示控件
	 * @param targetid 数据ID
	 * @return
	 */
	public Func findFuncByFirstColumn(Integer targetid){
		SQLiteDatabase db = openHelper.getReadableDatabase();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(openHelper.FUNC_TABLE);
		sql.append(" where targetid=").append(targetid);
		sql.append(" and type not in").append("(").append(Func.TYPE_BUTTON).append(",").append(Func.TYPE_CAMERA).append(",").append(Func.TYPE_CAMERA_HEIGHT).append(",").append(Func.TYPE_CAMERA_CUSTOM).append(",").append(Func.TYPE_CAMERA_MIDDLE).append(",").append(Func.TYPE_HIDDEN).append(")");
		sql.append(" and replenish_able_status like").append("'%").append(",").append(0).append(",").append("%'");
		sql.append(" limit 1");
		Cursor cursor = db.rawQuery(sql.toString(), null);
		Func func = null;
		if (cursor.moveToNext()) {
			func = putFunc(cursor);
		}
		cursor.close();
		
		return func;
	}

	/**
	 * 插入
	 * @param func 
	 * @return
	 */
	public Long insertFunc(Func func) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues cv = putContentValues(func);
		Long id = db.insert(openHelper.FUNC_TABLE, null, cv);
		
		if (func.getDataList() != null) {
			for (Func data : func.getDataList()) {
				data.setParentid(Integer.valueOf(String.valueOf(id)));// 将该行的ID当做PID
				insertFunc(data);
			}
		}
		return id;
	}

	/**
	 * 更新func
	 * @param func
	 * @return
	 */
	public int updateFuncById(Func func) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues cv = putContentValues(func);
		int id = db.update(openHelper.FUNC_TABLE, cv, "id=?",
				new String[] { func.getId() + "" });
		
		return id;
	}

	/**
	 * 更新func
	 * @param func
	 * @return
	 */
	public int updateFuncByFuncIdAndTargetid(Func func) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues cv = putContentValues(func);
		int id = db.update(
				openHelper.FUNC_TABLE,
				cv,
				"func_id=? and targetid=?",
				new String[] { String.valueOf(func.getFuncId()),
						String.valueOf(func.getTargetid()) });
		
		return id;
	}

	/**
	 * 删除func
	 * @param funcId
	 * @param targetid
	 * @return
	 */
	public int removeFuncByFuncIdAndTargetid(int funcId, int targetid) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		int id = db.delete(openHelper.FUNC_TABLE,
						"func_id=? and targetid=?",
						new String[] { String.valueOf(funcId),
								String.valueOf(targetid) });
		
		return id;
	}
	
	/**
	 * 删除数据ID下所有func
	 * @param targetid
	 * @return
	 */
	public int removeFuncByTargetid(int targetid) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		int id = db.delete(openHelper.FUNC_TABLE,"targetid=?",
						new String[] {String.valueOf(targetid) });
		
		return id;
	}
	/**
	 * 删除多个数据ID下的控件  
	 * @param targetidStr 要删除的数据ID的拼接串
	 */
	public void removeFuncByTargetid(String targetidStr) {
		if(PublicUtils.isIntegerArray(targetidStr)){
			String sql = "delete from "+openHelper.FUNC_TABLE+" where targetid in ("+targetidStr+")";
			openHelper.execSQL(sql);
		}
	}

	private ContentValues putContentValues(Func func) {
		ContentValues cv = new ContentValues();
		cv.put("func_id", func.getFuncId());
		cv.put("name", func.getName());
		cv.put("type", func.getType());
		cv.put("length", func.getLength());
		cv.put("isEmpty", func.getIsEmpty());
		cv.put("checkType", func.getCheckType());
		cv.put("nextDropdown", func.getNextDropdown());
		cv.put("defaultValue", func.getDefaultValue());
		cv.put("defaultType", func.getDefaultType());
		cv.put("targetid", func.getTargetid());
		cv.put("menuid", func.getMenuId());
		cv.put("dict_table", func.getDictTable());
		cv.put("dict_data_id", func.getDictDataId());
		cv.put("dict_cols", func.getDictCols());
		cv.put("parentid", func.getParentid());
		cv.put("dataType", func.getDataType());
		cv.put("orgOption", func.getOrgOption());
		cv.put("orgLevel", func.getOrgLevel());
		cv.put("webOrder", func.getWebOrder());
		cv.put("isShowColumn", func.getIsShowColumn());
		cv.put("isSearch", func.getIsSearch());
		cv.put("status", func.getStatus());
		cv.put("replenish_able_status", func.getReplenish_able_status());
		cv.put("dupallowdays", func.getDayNumber());
		cv.put("dupallowtimes", func.getOperateNumber());
		cv.put("tableId", func.getTableId());
		cv.put("is_search_modify", func.getIsSearchModify());
		cv.put("input_order", func.getInputOrder());
		cv.put("enter_must_list", func.getEnterMustList());
		cv.put("isFuzzy", func.getIsFuzzy());
		cv.put("isSearchMul", func.getIsSearchMul());
		cv.put("is_edit", func.getIsEdit() == null ? Func.IS_Y : func.getIsEdit());
		cv.put("is_import_key", func.getIsImportKey() == null ? Func.IS_N : func.getIsImportKey());
		cv.put("colWidth", func.getColWidth());
		cv.put("photoTimeType", func.getPhotoTimeType());
		cv.put("codeType", func.getCodeType());
		cv.put("dict_order_by", func.getDictOrderBy());
		cv.put("dict_is_asc", func.getDictIsAsc());
		cv.put("is_area_search", func.getIsAreaSearch());
		cv.put("area_search_value", func.getAreaSearchValue());
		cv.put("code_control", func.getCodeControl());
		cv.put("code_update", func.getCodeUpdate());
		cv.put("photoLocationType", func.getPhotoLocationType());
		cv.put("loc_type", func.getLocType());
		cv.put("is_address", func.getIsAddress());
		cv.put("is_anew_loc", func.getIsNewLoc());
		cv.put("locCheckDistance", func.getLocCheckDistance());
		cv.put("task_status", func.getTaskStatus());
		cv.put("print_alignment", func.getPrintAlignment());
		cv.put("photo_flg", func.getPhotoFlg());
		cv.put("is_img_custom", func.getIsImgCustom());
		cv.put("is_img_store", func.getIsImgStore());
		cv.put("is_img_user", func.getIsImgUser());
		cv.put("ableAD", func.getAbleAD());
		cv.put("showColor", func.getShowColor());
		cv.put("locLevel", func.getLocLevel());
		cv.put("is_cache", func.getIsCacheFun());
		cv.put("restrict_type", func.getRestrictType());
		cv.put("restrict_rule", func.getRestrictRule());
		cv.put("is_scan",func.getIsScan());
		cv.put("scan_status",func.getScanStatus());
		cv.put("scan_cols",func.getScanCols());
		return cv;
	}
	
	private Func putFunc(Cursor cursor) {
		int i = 0;
		Func func = new Func();
		func.setId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		func.setFuncId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		func.setTargetid(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		func.setMenuId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		func.setName(cursor.getString(i++));
		func.setType(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		func.setLength(TextUtils.isEmpty(cursor.getString(i))?null:cursor.getInt(i));i++;
		func.setIsEmpty(cursor.getInt(i++));
		func.setCheckType(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		func.setNextDropdown(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		func.setDefaultType(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		func.setDefaultValue(cursor.getString(i++));
		func.setDictTable(cursor.getString(i++));
		func.setDictDataId(cursor.getString(i++));
		func.setDictCols(cursor.getString(i++));
		func.setParentid(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		func.setDataType(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		func.setOrgOption(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		func.setOrgLevel(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		func.setWebOrder(cursor.getInt(i++));
		func.setIsShowColumn(cursor.getInt(i++));
		func.setIsSearch(cursor.getInt(i++));
		func.setStatus(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		func.setReplenish_able_status(cursor.getString(i++));
		func.setDayNumber(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		func.setOperateNumber(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		func.setTableId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		func.setIsSearch(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		func.setInputOrder(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		func.setEnterMustList(cursor.getString(i++));
		func.setIsFuzzy(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		func.setIsSearchMul(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		func.setIsEdit(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		func.setIsImportKey(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		func.setColWidth(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		func.setPhotoTimeType(cursor.getInt(i++));
		func.setCodeType(cursor.getString(i++));
		func.setDictOrderBy(cursor.getString(i++));
		func.setDictIsAsc(cursor.getString(i++));
		func.setIsAreaSearch(cursor.isNull(i) ? 0 : cursor.getInt(i));i++;
		func.setAreaSearchValue(cursor.isNull(i) ? null : cursor.getFloat(i));i++;
		func.setCodeControl(cursor.getString(i++));
		func.setCodeUpdate(cursor.getString(i++));
		func.setPhotoLocationType(cursor.getInt(i++));
		func.setLocType(cursor.getString(i++));
		func.setIsAddress(cursor.getString(i++));
		func.setIsNewLoc(cursor.getString(i++));
		func.setLocCheckDistance(cursor.getString(i++));
		func.setTaskStatus(cursor.getString(i++));
		func.setPrintAlignment(cursor.getInt(i++));
		func.setPhotoFlg(cursor.getInt(i++));
		func.setIsImgCustom(cursor.getInt(i++));
		func.setIsImgStore(cursor.getInt(i++));
		func.setIsImgUser(cursor.getInt(i++));
		func.setAbleAD(cursor.getString(i++));
		func.setShowColor(cursor.getString(i++));
		func.setLocLevel(cursor.getString(i++));
		func.setIsCacheFun(cursor.isNull(i) ?0:cursor.getInt(i));
		func.setRestrictType(cursor.getInt(cursor.getColumnIndex("restrict_type")));
		func.setRestrictRule(cursor.getString(cursor.getColumnIndex("restrict_rule")));
		func.setIsScan(cursor.getInt(cursor.getColumnIndex("is_scan")));
		func.setScanStatus(cursor.getInt(cursor.getColumnIndex("scan_status")));
		func.setScanCols(cursor.getString(cursor.getColumnIndex("scan_cols")));

		return func;
	}
	
	/**
	 * 查找只读的控件
	 * @param targetid 数据ID
	 * @return
	 */
	public List<Func> findFuncListByDoubleReadOnly(int targetid){//replenish_able_status=0
		List<Func> list = new ArrayList<Func>();
		SQLiteDatabase db = openHelper.getReadableDatabase();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(openHelper.FUNC_TABLE).append(" where targetid =").append(targetid);
		sql.append(" and replenish_able_status like").append("'%").append(",").append(0).append(",").append("%'");
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putFunc(cursor));
			}
		}
//		JLog.d(TAG, "findFuncListByDoubleReadOnlySql==>"+sql.toString());
		cursor.close();
		
		return list;
	}
	
	/**
	 * 查找可操作的控件
	 * @param targetid
	 * @return
	 */
	public List<Func> findFuncListByDoubleWritable(int targetid){
		List<Func> list = new ArrayList<Func>();
		SQLiteDatabase db = openHelper.getReadableDatabase();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(openHelper.FUNC_TABLE).append(" where targetid =").append(targetid);
		sql.append(" and replenish_able_status like").append("'%").append(",").append(1).append(",").append("%'");
		sql.append(" and type <>").append(Func.TYPE_HIDDEN).append(" and type <>").append(Func.TYPE_BUTTON).append(" order by webOrder");
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putFunc(cursor));
			}
		}
		
//		JLog.d(TAG, "findFuncListByDoubleWritable==>"+sql.toString());
		cursor.close();
		
		return list;
	}
	
	/**
	 * 查找查询条件的集合
	 * @param targetId 数据ID
	 * @param isShowColumn 修改 是否显示列 为0时,查询条件为 不等于1(即,为空时也按0算)
	 * @param isSearch 是否是查询条件
	 * @param isReplenish
	 * @return
	 */
	public List<Func> findFuncListReplenish(Integer targetId,Integer isShowColumn, Integer isSearch,Integer isReplenish){
		SQLiteDatabase db = openHelper.getReadableDatabase();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.FUNC_TABLE).append(" where 1=1");
		List<Func> list=new ArrayList<Func>();
		if(targetId!=null){
			sql.append(" and targetid=").append(targetId);
		}
		if(isShowColumn!=null ){
			if(isShowColumn == 0){//修改 是否显示列 为0时,查询条件为 不等于1(即,为空时也按0算)
				sql.append(" and (isShowColumn=").append(isShowColumn).append(" or isShowColumn is null )");
			}else{
				sql.append(" and isShowColumn=").append(isShowColumn);
			}
		}
		if(isSearch!=null){
			sql.append(" and isSearch=").append(isSearch);
		}
		if(isReplenish!=null){
			sql.append(" and isReplenish=").append(isReplenish);
		}
		Cursor cursor = db.rawQuery(sql.toString(), null);
		Func func=null;
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				func=putFunc(cursor);
				list.add(func);
			}
		}
		cursor.close();
		
//		JLog.d(TAG, "findFuncListReplenish"+sql.toString());
		return list;
	}


	//此处错误，不能只通过funcID来获取控件
	/**
	 * 根据控件ID查找控件
	 * @param funcId 控件ID
	 * @return
	 */
	public Func findFuncByFuncId(int funcId) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.FUNC_TABLE);
		sql.append(" where func_id = ").append(funcId).append(" ");
		Cursor cursor = db.rawQuery(sql.toString(), null);
		Func func = null;
		if(cursor.getCount() >0){
			
			if (cursor.moveToNext()) {
				func = putFunc(cursor);
			}
		}
		cursor.close();
		return func;
	}
	
	/**
	 * 根据控件ID查找控件
	 * @param funcId 控件ID
	 * @return
	 */
	public List<Func> findFuncByFuncIds(String funcId) {
		List<Func> list = new ArrayList<Func>();
		if(PublicUtils.isIntegerArray(funcId)){
			SQLiteDatabase db = openHelper.getReadableDatabase();
			StringBuffer sql = new StringBuffer();
			sql.append(" select * from ").append(openHelper.FUNC_TABLE);
			sql.append(" where func_id in ( ").append(funcId).append(" )");
			Cursor cursor = db.rawQuery(sql.toString(), null);
			Func func = null;
			if(cursor.getCount() >0){
				
				while (cursor.moveToNext()) {
					func = putFunc(cursor);
					list.add(func);
				}
			}
			cursor.close();
		}
		
		return list;
	}
	
	
	/**
	 * 查找可同步数据的下拉框
	 * @param
	 * @return
	 */
	public List<Func> findSyncDataFuncList(){
		List<Func> list = new ArrayList<Func>();
		SQLiteDatabase db = openHelper.getReadableDatabase();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(openHelper.FUNC_TABLE);
		sql.append(" where (type =").append(Func.TYPE_SELECTCOMP);
		sql.append(" or type =").append(Func.TYPE_SINGLE_CHOICE_FUZZY_QUERY_COMP);
		sql.append(" or type =").append(Func.TYPE_SELECT_OTHER);
		sql.append(" or type =").append(Func.TYPE_MULTI_CHOICE_SPINNER_COMP);
		sql.append(" or type =").append(Func.TYPE_MULTI_CHOICE_FUZZY_QUERY_COMP);
		sql.append(" or type =").append(Func.TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY_COMP);
		sql.append(") and func_id > 0 ").append(" group by dict_table");
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putFunc(cursor));
			}
		}
		cursor.close();
		return list;
	}
	
}
