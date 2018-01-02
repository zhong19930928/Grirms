package com.yunhu.yhshxc.database;

import gcg.org.debug.JLog;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.utility.PublicUtils;

/**
 * 访店中的控件
 * @author jishen 数据库操作
 */
public class VisitFuncDB {

	private String TAG = "VisitFuncDB";
	private DatabaseHelper openHelper;

	public VisitFuncDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}
	
	/**
	 * 查找是是否必须输入的控件
	 * @param isEmpty 是否必须输入 1 必须输入 0不必须输入
	 * @param targetid 数据ID
	 * @return
	 */
	public List<Func> findFuncListByIsEmpty(int isEmpty,int targetid) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		List<Func> list=new ArrayList<Func>();
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.VISIT_FUNC_TABLE).append(" where isEmpty=").append(isEmpty);
		sql.append(" and targetid=").append(targetid);
		Cursor cursor = db.rawQuery(sql.toString(), null);
		JLog.d(TAG, "findFuncListByIsEmpty==>" + sql.toString());
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
	 * 有顺序的输入的顺序集合
	 * @param targetid 数据ID
	 * @return
	 */
	public Integer[] findFuncListByInputOrder(int targetid) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		Integer[] arr=null;
		StringBuffer sql=new StringBuffer();
		sql.append("select distinct input_order from ").append(openHelper.VISIT_FUNC_TABLE);
		sql.append(" where targetid=").append(targetid).append(" and input_order is  not null").append(" order by input_order");
		
		Cursor cursor = db.rawQuery(sql.toString(), null);
		JLog.d(TAG, "findFuncListByInputOrder==>" + sql.toString());
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
	 * 通过控件ID 数据ID 和输入排序查找控件
	 * @param targetid 数据ID
	 * @param funcId 控件ID
	 * @param inputOrder 输入顺序
	 * @return 查找到的func集合
	 */
	
	public ArrayList<Func> findFuncListByInputOrder(int targetid,Integer funcId,int inputOrder) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.VISIT_FUNC_TABLE);
		sql.append(" where targetid=").append(targetid).append(" and input_order =").append(inputOrder);
		if(funcId != null){
			sql.append(" and func_id =").append(funcId);
		}
		
		Cursor cursor = db.rawQuery(sql.toString(), null);
		JLog.d(TAG, "findFuncListByInputOrder==>" + sql.toString());
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
	 * 根据数据ID查找控件 不包含隐藏域
	 * @param targetid 数据ID
	 * @return
	 */
	public List<Func> findFuncListByTargetid(Integer targetid) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		List<Func> list = new ArrayList<Func>();
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.VISIT_FUNC_TABLE).append(" where targetid=").append(targetid);
		sql.append(" and type <>").append(Func.TYPE_HIDDEN).append(" order by id");
		Cursor cursor = db.rawQuery(sql.toString(), null);
		JLog.d(TAG, "findFuncListByTargetid==>" + sql.toString());
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
		sql.append("select * from ").append(openHelper.VISIT_FUNC_TABLE).append(" where targetid=").append(targetid);
		sql.append(" order by id");
		Cursor cursor = db.rawQuery(sql.toString(), null);
		JLog.d(TAG, "findFuncListByTargetid==>" + sql.toString());
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putFunc(cursor));
			}
		}
		cursor.close();
		
		return list;
	}
	
	/**
	 * 查找隐藏域类型的控件
	 * @param targetid 数据ID
	 * @param type
	 * @return
	 */
	public List<Func> findFuncListByType(Integer targetid,int type){
		SQLiteDatabase db = openHelper.getReadableDatabase();
		List<Func> list = new ArrayList<Func>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(openHelper.VISIT_FUNC_TABLE);
		sql.append(" where targetid=").append(targetid);
		sql.append(" and type =").append(Func.TYPE_HIDDEN);
		Cursor cursor = db.rawQuery(sql.toString(), null);
		JLog.d(TAG, "findVisitFuncListByTargetid==>" + sql.toString());
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putFunc(cursor));
			}
		}
		cursor.close();
		
		return list;
	}

	/**
	 * 根据数据ID和控件ID查找控件
	 * @param func_id 控件ID
	 * @param targetId 数据ID
	 * @return
	 */
	public Func findFuncListByFuncIdAndTargetId(String func_id,Integer targetId) {
		Func func = null;
		if(PublicUtils.isInteger(func_id)){
			SQLiteDatabase db = openHelper.getReadableDatabase();
			StringBuffer sql=new StringBuffer();
			sql.append("select * from ").append(openHelper.VISIT_FUNC_TABLE).append(" where func_id=").append(func_id);
			sql.append(" and targetid=").append(targetId);
			
			
			Cursor cursor = db.rawQuery(sql.toString(), null);
			JLog.d(TAG, "findFuncListByFuncIdAndTargetId==>" + sql.toString());
			
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
	 * 根据parentid查找控件
	 * @param parentid
	 * @return
	 */
	public List<Func> findFuncListByParentid(Integer parentid) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		List<Func> list = new ArrayList<Func>();
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.VISIT_FUNC_TABLE).append(" where parentid=").append(parentid);
		
		Cursor cursor = db.rawQuery(sql.toString(), null);
		JLog.d(TAG, "findFuncListByParentid==>" + sql.toString());
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putFunc(cursor));
			}
		}
		cursor.close();
		
		return list;
	}

	/**
	 * 插入
	 * @param func
	 * @return
	 */
	public Long insertFunc(Func func) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues cv = putContentValues(func);
		Long id = db.insert(openHelper.VISIT_FUNC_TABLE, null, cv);
		
		if (func.getDataList() != null) {
			for (Func data : func.getDataList()) {
				data.setParentid(Integer.valueOf(String.valueOf(id)));
				insertFunc(data);
			}
		}
		return id;
	}

	/**
	 * 修改
	 * @param func
	 * @return
	 */
	public int updateFuncById(Func func) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues cv = putContentValues(func);
		int id = db.update(openHelper.VISIT_FUNC_TABLE, cv, "id=?",
				new String[] { func.getId() + "" });
		
		return id;
	}

	/**
	 * 修改
	 * @param func
	 * @return
	 */
	public int updateFuncByFuncIdAndTargetid(Func func) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues cv = putContentValues(func);
		int id = db.update(
				openHelper.VISIT_FUNC_TABLE,
				cv,
				"func_id=? and targetid=?",
				new String[] { String.valueOf(func.getFuncId()),
						String.valueOf(func.getTargetid()) });
		
		return id;
	}

	/**
	 * 删除
	 * @param funcId 控件ID
	 * @param targetid 数据ID
	 * @return
	 */
	public int removeFuncByFuncIdAndTargetid(int funcId, int targetid) {

		int id = openHelper.delete(openHelper.VISIT_FUNC_TABLE,
						"func_id=? and targetid=?",
						new String[] { String.valueOf(funcId),
								String.valueOf(targetid) });
		
		return id;
	}
	
	/**
	 * 根据数据ID伤处控件
	 * @param targetid 数据ID
	 * @return
	 */
	public int removeFuncByTargetid(int targetid) {
		int id = openHelper.delete(openHelper.VISIT_FUNC_TABLE,"targetid=?",new String[] {String.valueOf(targetid)});
		return id;
	}
	/**
	 * 根据数据ID伤处控件
	 * @param targetid 数据ID
	 * @return
	 */
	public void removeFuncByTargetid(String targetidStr) {
		if(PublicUtils.isIntegerArray(targetidStr)){
			String sql = "delete from "+openHelper.VISIT_FUNC_TABLE+" where targetid in ("+targetidStr+")";
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
		cv.put("defaultType", func.getDefaultType());
		cv.put("defaultValue", func.getDefaultValue());
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
		func.setLength(cursor.isNull(i) ? null : cursor.getInt(i));i++;;
		func.setIsEmpty(cursor.isNull(i) ? null : cursor.getInt(i));i++;
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
		func.setWebOrder(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		func.setIsShowColumn(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		func.setIsSearch(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		func.setStatus(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		func.setReplenish_able_status(cursor.getString(i++));
		func.setDayNumber(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		func.setOperateNumber(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		func.setTableId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		func.setIsSearchModify(cursor.isNull(i) ? null : cursor.getInt(i));i++;
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
		func.setIsCacheFun(cursor.isNull(i) ?0:cursor.getInt(i++));
		
		func.setRestrictType(cursor.getInt(cursor.getColumnIndex("restrict_type")));
		func.setRestrictRule(cursor.getString(cursor.getColumnIndex("restrict_rule")));
		return func;
		
		
	}

	/**
	 * 查找
	 * @param funcId 控件ID
	 * @return
	 */
	public Func findFuncByFuncId(int funcId) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.VISIT_FUNC_TABLE);
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
	 * 查找可同步数据的下拉框
	 * @param targetid
	 * @return
	 */
	public List<Func> findSyncDataFuncList(){
		List<Func> list = new ArrayList<Func>();
		SQLiteDatabase db = openHelper.getReadableDatabase();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(openHelper.VISIT_FUNC_TABLE);
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
