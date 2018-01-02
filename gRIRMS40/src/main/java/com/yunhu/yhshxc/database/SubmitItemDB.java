package com.yunhu.yhshxc.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.bo.SubmitItem;
import com.yunhu.yhshxc.exception.DataComputeException;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.RegExpvalidateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gcg.org.debug.JLog;

/**
 * 提交数据数据库
 * @author jishen 数据库操作
 */
public class SubmitItemDB {
	private String TAG = "SubmitItemDB";
    private FuncCacheDB funcCacheDB ;
	private DatabaseHelper openHelper = null;;
	private Context context = null;

	public SubmitItemDB(Context context) {
		this.context = context;
		openHelper = DatabaseHelper.getInstance(context);
		funcCacheDB = new FuncCacheDB(context);
	}

	
	/**
	 * 
	 * @param submitItem
	 * @return
	 */
	public Long insertSubmitItem(SubmitItem submitItem,boolean flag) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues cv = putContentValues(submitItem);
		Long id = db.insert(openHelper.SUBMIT_ITEM_TABLE, null, cv);
		if (submitItem.getIsCacheFun()==1&&flag) {
			funcCacheDB.insertSubmitToCache(submitItem);
		}
		JLog.d(TAG, "插入SubmitItem");
		return id;
	}
	
	/**
	 * 
	 * @param submitItem 插入,如果存在,先删除原有的
	 * @return
	 */
	public Long insertSubmitItemOrDelete(SubmitItem submitItem) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		db.delete(openHelper.SUBMIT_ITEM_TABLE, "param_name = ?", new String[]{submitItem.getParamName()});
		ContentValues cv = putContentValues(submitItem);
		Long id = db.insert(openHelper.SUBMIT_ITEM_TABLE, null, cv);	
		return id;
	}
	
	/**
	 * 根据主键删除
	 * @param id
	 */
	public void deleteSubmitItemByFuncId(Integer id) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		StringBuffer sql=new StringBuffer();
		sql.append("delete from ").append(openHelper.SUBMIT_ITEM_TABLE).append(" where param_name = ").append(id);
//		sql.append(" and param_name = ").append(funcid);
		
		db.execSQL(sql.toString());
	}

	/**
	 * 删除
	 * @param submitItem
	 * @return
	 */
	public int deleteSubmitItem(SubmitItem submitItem) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		int id = db.delete(
				openHelper.SUBMIT_ITEM_TABLE,
				"param_name=? and submit_id=?",
				new String[] { submitItem.getParamName() + "",
						submitItem.getSubmitId() + "" });
		
		JLog.d(TAG, "deleteSubmitItem" + submitItem.getParamName());
		return id;
	}

	/**
	 * 修改
	 * @param submitItem
	 * @return
	 */
	public int updateSubmitItem(SubmitItem submitItem,boolean flag) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues cv = putContentValues(submitItem);
		int id = db.update(openHelper.SUBMIT_ITEM_TABLE,cv,"param_name=? and submit_id=?",
				new String[] {String.valueOf(submitItem.getParamName()),String.valueOf(submitItem.getSubmitId())});
		JLog.d(TAG, "updateSubmitItem" + submitItem.getParamName());
		if (submitItem.getIsCacheFun()==1&&flag) {
			//执行更新
			funcCacheDB.insertSubmitToCache(submitItem);
		}
		return id;
	}


	private ContentValues putContentValues(SubmitItem submitItem) {
		ContentValues cv = new ContentValues();
		cv.put("submit_id", submitItem.getSubmitId());
		cv.put("param_name", submitItem.getParamName());
		cv.put("param_value", submitItem.getParamValue());
		cv.put("type", submitItem.getType());
		cv.put("orderId", submitItem.getOrderId());
		cv.put("note", submitItem.getNote());
//		cv.put("menuId", submitItem.getMenuId());
		cv.put("isCacheFun", submitItem.getIsCacheFun());
		return cv;
	}
	
	private SubmitItem putSubmitItem(Cursor cursor) {
		int i = 0;
		SubmitItem submitItem = new SubmitItem();
		submitItem.setId(cursor.getInt(i++));
		submitItem.setSubmitId(cursor.getInt(i++));
		submitItem.setParamName(cursor.getString(i++));
		submitItem.setParamValue(cursor.getString(i++));
		submitItem.setType(cursor.getInt(i++));
		submitItem.setOrderId(cursor.getInt(i++));
		submitItem.setNote(cursor.getString(i++));
//		submitItem.setMenuId(cursor.getInt(i++));
		submitItem.setIsCacheFun(cursor.getInt(i++));
		return submitItem;
	}
	
	

	/**
	 * 
	 * @return所有的SubmitItem集合
	 */
	public List<SubmitItem> findAllSubmitItem() {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		List<SubmitItem> list = new ArrayList<SubmitItem>();
		SubmitItem submitItem = null;
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.SUBMIT_ITEM_TABLE).append(" order by orderId");
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				submitItem = putSubmitItem(cursor);
				list.add(submitItem);
			}
		}
		cursor.close();
		
		return list;
	}

	/**
	 * 根据表单ID查找
	 * @return
	 */
	public List<SubmitItem> findSubmitItemBySubmitId(int submit_id) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		List<SubmitItem> list = new ArrayList<SubmitItem>();
		SubmitItem submitItem = null;
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.SUBMIT_ITEM_TABLE).append(" where submit_id = ").append(submit_id).append(" order by orderId");
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				submitItem = putSubmitItem(cursor);
				list.add(submitItem);
			}
		}
		cursor.close();
		
		return list;
	}
	/**
	 * 根据表单ID查找超链接类型的数据
	 */
	public List<SubmitItem> findLinkSubmitItemBySubmitId(int submit_id) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		List<SubmitItem> list = new ArrayList<SubmitItem>();
		SubmitItem submitItem = null;
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.SUBMIT_ITEM_TABLE).append(" where submit_id = ").append(submit_id).append(" and type =").append(Func.TYPE_LINK).append(" order by orderId");
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				submitItem = putSubmitItem(cursor);
				list.add(submitItem);
			}
		}
		cursor.close();
		
		return list;
	}
	/**
	 * 查找
	 * @param submit_id 表单ID
	 * @param funcId 控件ID
	 * @return
	 */
	public SubmitItem findSubmitItemBySubmitIdAndFuncId(int submit_id,int funcId) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		SubmitItem submitItem = null;
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.SUBMIT_ITEM_TABLE).append(" where submit_id = ").append(submit_id).append(" and param_name= ").append(funcId);
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				submitItem = putSubmitItem(cursor);
			}
		}
		cursor.close();
		
		return submitItem;
	}
	
	/**
	 * 查找
	 * @param id 主键ID
	 * @return
	 */
	public SubmitItem findSubmitItemBySubmitItemId(int id) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		SubmitItem submitItem = null;
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.SUBMIT_ITEM_TABLE).append(" where id = ").append(id);
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				submitItem = putSubmitItem(cursor);
			}
		}
		cursor.close();
		
		return submitItem;
	}
	
	public int getItemId(int submitid,int paramName){
		SQLiteDatabase db = openHelper.getReadableDatabase();
	  int resultId = 0;
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.SUBMIT_ITEM_TABLE).append(" where submit_id = ").append(submitid).append(" and param_name= ").append(paramName);
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				resultId=cursor.getInt(cursor.getColumnIndex("id"));
			}
		}
		cursor.close();
		return resultId;
	}
	
	/**
	 * 查找
	 * @param funcId 控件ID
	 * @return
	 */
	public SubmitItem findSubmitItemByFuncId(int funcId) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		SubmitItem submitItem = null;
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.SUBMIT_ITEM_TABLE).append(" where param_name = ").append(funcId);
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				submitItem = putSubmitItem(cursor);
			}
		}
		cursor.close();
		
		return submitItem;
	}
	/**
	 * 查找
	 * @param submit_id 表单ID
	 * @param type 控件类型
	 * @return
	 */
	public List<SubmitItem> findSubmitItemBySubmitIdAndType(int submit_id,int type) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		List<SubmitItem> list = new ArrayList<SubmitItem>();
		SubmitItem submitItem = null;
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.SUBMIT_ITEM_TABLE).append(" where submit_id = ").append(submit_id);
		sql.append(" and type = ").append(type).append(" order by orderId");
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				submitItem = putSubmitItem(cursor);
				list.add(submitItem);
			}
		}
		cursor.close();
		
		return list;
	}
	/**
	 * 查找
	 * @param type 控件类型
	 * @return
	 */
	public List<SubmitItem> findSubmitItemByType(int type) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		List<SubmitItem> list = new ArrayList<SubmitItem>();
		SubmitItem submitItem = null;
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.SUBMIT_ITEM_TABLE);
		sql.append(" where type = ").append(type).append(" order by orderId");
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				submitItem = putSubmitItem(cursor);
				list.add(submitItem);
			}
		}
		cursor.close();
		
		return list;
	}
	
	/**
	 * 查找
	 * @return
	 */
	public List<SubmitItem> findPhotoSubmitItem() {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		List<SubmitItem> list = new ArrayList<SubmitItem>();
		SubmitItem submitItem = null;
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.SUBMIT_ITEM_TABLE);
		sql.append(" where (type = ").append(Func.TYPE_CAMERA_HEIGHT).append(" or type = ").append(Func.TYPE_CAMERA_CUSTOM).append(" or type = ").append(Func.TYPE_CAMERA_MIDDLE).append(" or type = ").append(Func.TYPE_CAMERA).append(")");
		sql.append(" order by orderId");
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				submitItem = putSubmitItem(cursor);
				list.add(submitItem);
			}
		}
		cursor.close();
		
		return list;
	}
	/**
	 * 查找
	 * @return
	 */
	public List<SubmitItem> findPhotoSubmitItemBySubmitId(int submitId) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		List<SubmitItem> list = new ArrayList<SubmitItem>();
		SubmitItem submitItem = null;
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.SUBMIT_ITEM_TABLE).append(" where submit_id = ").append(submitId);
		sql.append(" and (type = ").append(Func.TYPE_CAMERA_HEIGHT).append(" or type = ").append(Func.TYPE_CAMERA_CUSTOM).append(" or type = ").append(Func.TYPE_CAMERA_MIDDLE).append(" or type = ").append(Func.TYPE_CAMERA).append(")");
		sql.append(" order by orderId");
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				submitItem = putSubmitItem(cursor);
				list.add(submitItem);
			}
		}
		cursor.close();
		
		return list;
	}
	
	/**
	 * 查找
	 * @param paramName 控件id
	 * @param type 控件类型
	 * @return
	 */
	public SubmitItem findSubmitItemByParamNameAndType(String paramName,int type) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		SubmitItem submitItem = null;
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ");
		sql.append(openHelper.SUBMIT_ITEM_TABLE).append(" where param_name = ").append(paramName).append(" and type=").append(type);
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				submitItem = putSubmitItem(cursor);
			}
		}
		cursor.close();
		
		return submitItem;
	}
	
	/**
	 * 查找
	 * @param paramName 控件id
	 * @return
	 */
	public SubmitItem findSubmitItemByParamName(String paramName) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		SubmitItem submitItem = null;
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ");
		sql.append(openHelper.SUBMIT_ITEM_TABLE).append(" where param_name = ").append(paramName);
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				submitItem = putSubmitItem(cursor);
			}
		}
		cursor.close();
		
		return submitItem;
	}
	/**
	 * 查找
	 * @param paramName 控件ID
	 * @param submitID 表单ID
	 * @return
	 */
	public SubmitItem findSubmitItemByParamNameAndSubmitId(String paramName,int submitID) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		SubmitItem submitItem = null;
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ");
		sql.append(openHelper.SUBMIT_ITEM_TABLE).append(" where param_name = ").append(paramName).append(" and submit_id=").append(submitID);
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				submitItem = putSubmitItem(cursor);
			}
		}
		cursor.close();
		
		return submitItem;
	}

	/**
	 * 
	 * @param submit_id
	 * @param type 控件类型
	 * @return
	 */
	public List<SubmitItem> findSubmitItemList(int submit_id, int type) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		List<SubmitItem> list = new ArrayList<SubmitItem>();
		SubmitItem submitItem = null;
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(openHelper.SUBMIT_ITEM_TABLE);
		sql.append(" where submit_id = ").append(submit_id).append(" and type = ").append(type);
		sql.append(" order by orderId");
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				submitItem = putSubmitItem(cursor);
				list.add(submitItem);
			}
		}
		cursor.close();
		
		return list;
	}
	/* 
	* @param submit_id
	* @return
			*/
			public List<SubmitItem> findSubmitItemListById(int submit_id) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		List<SubmitItem> list = new ArrayList<SubmitItem>();
		SubmitItem submitItem = null;
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(openHelper.SUBMIT_ITEM_TABLE);
		sql.append(" where submit_id = ").append(submit_id);
		sql.append(" order by orderId");
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				submitItem = putSubmitItem(cursor);
				list.add(submitItem);
			}
		}
		cursor.close();
		
		return list;
	}

	/**
	 * 是否有该控件的值
	 * @param paramName 控件ID
	 * @param submitId 表单ID
	 * @return
	 */
	public boolean findIsHaveParamName(String paramName, Integer submitId) {
		boolean flag;
		SQLiteDatabase db = openHelper.getReadableDatabase();
		StringBuffer sql=new StringBuffer();
		sql.append("select param_name from ").append(openHelper.SUBMIT_ITEM_TABLE).append(" where param_name = '");
		sql.append(paramName).append("' and submit_id=").append(submitId);
		Cursor cursor = db.rawQuery(sql.toString(), null);
		JLog.d(TAG, "findIsHaveParamName" + sql.toString());
		if (cursor.getCount() > 0) {
			flag = true;
		} else {
			flag = false;
		}
		cursor.close();
		
		return flag;
	}

	/**
	 * 根据条件查出指定控件数据
	 * 
	 * @param submitId 表单ID
	 * @param paramName 控件ID
	 * @return
	 */
	public SubmitItem findSubmitItem(Integer submitId, String paramName) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		SubmitItem submitItem = null;
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(openHelper.SUBMIT_ITEM_TABLE)
				.append(" where submit_id =").append(submitId);
		if (!TextUtils.isEmpty(paramName)) {
			sql.append(" and param_name ='").append(paramName).append("'");
		}
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				submitItem = putSubmitItem(cursor);
			}
		}
		cursor.close();
		
		return submitItem;
	}
	
	public SubmitItem findSubmitItemByNote(Integer submitId, String note) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		SubmitItem submitItem = null;
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(openHelper.SUBMIT_ITEM_TABLE).append(" where submit_id =").append(submitId);
		sql.append(" and note = '").append(note).append("'");
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				submitItem = putSubmitItem(cursor);
			}
		}
		cursor.close();
		
		return submitItem;
	}

	public void updateSubmitBySubmitIdAndNote(int submitId,String note){
		StringBuffer sql = new StringBuffer();
		sql.append(" update ").append(openHelper.SUBMIT_ITEM_TABLE).append(" set param_value = null").append(",note = null");
		sql.append(" where submit_id = ").append(submitId);
		sql.append(" and note = '").append(note).append("'");
		openHelper.execSQL(sql.toString());
	}
	
	/**
	 * 根据条件查出指定控件数据
	 * 
	 * @param baseUrl
	 * @param state
	 * @param planId
	 * @param wayId
	 * @param storeId
	 * @param targetid
	 * @param paramName
	 * @return
	 */
	public SubmitItem findSubmitItem(String baseUrl, Integer state,
			Integer planId, Integer wayId, Integer storeId, Integer targetid,Integer targetType,
			String paramName) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		SubmitItem submitItem = null;
		StringBuffer sql = new StringBuffer();
		sql.append("select b.* from ")
				.append(openHelper.SUBMIT_TABLE).append(" a,");
		sql.append(openHelper.SUBMIT_ITEM_TABLE).append(" b ")
				.append(" where a.id = b.submit_id ");
		if (state != null) {
			sql.append(" and a.state =").append(state);
		}
		if (planId != null) {
			sql.append(" and a.plan_id =").append(planId);
		}
		if (wayId != null) {
			sql.append(" and a.way_id =").append(wayId);
		}
		if (storeId != null) {
			sql.append(" and a.store_id =").append(storeId);
		}
		if (targetid != null) {
			sql.append(" and a.targetid =").append(targetid);
		}
		if (targetid != null) {
			sql.append(" and a.targetType =").append(targetType);
		}
		if (!TextUtils.isEmpty(paramName)) {
			sql.append(" and b.param_name ='").append(paramName).append("'");
		}
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				submitItem = putSubmitItem(cursor);
			}
		}
		cursor.close();
		
		JLog.d(TAG, "findSubmitItemSql"+sql.toString());
		return submitItem;
	}

	/**
	 * 根据条件删除指定提交数据
	 */
	public void deleteSubmitItemBySubmitId(Integer submitId) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		
		StringBuffer sql=new StringBuffer();
		sql.append("delete from ").append(openHelper.SUBMIT_ITEM_TABLE).append(" where submit_id = ").append(submitId);
		db.execSQL(sql.toString());
		JLog.d(TAG,"SubmitItemDB" + "deleteSubmitItemBySubmitId success==>");
		
	}
	/**
	 * 根据条件删除指定提交数据
	 */
	public void deleteSubmitItemBySubmitIdAndCacheFunc(Integer submitId) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		
		StringBuffer sql=new StringBuffer();
		sql.append("delete from ").append(openHelper.SUBMIT_ITEM_TABLE).append(" where submit_id = ").append(submitId);
		db.execSQL(sql.toString());
		JLog.d(TAG,"SubmitItemDB" + "deleteSubmitItemBySubmitId success==>");
		
	}
	
	/**
	 * 提交后删除数据
	 */
	public void deleteSubmitData(Integer submitId){
		deleteSubmitItemBySubmitId(submitId);
		new SubmitDB(context).deleteSubmitById(submitId);
		JLog.d(TAG,"SubmitItemDB" + "delete Submit and SubmitItem success==>");
	}
	
	/**
	 * 根据条件删除指定提交数据
	 */
	public void deleteAllPhotoSubmitItemBySubmitId(Integer submitId) {
		StringBuffer sql=new StringBuffer();
		sql.append("delete from ").append(openHelper.SUBMIT_ITEM_TABLE).append(" where submit_id = ").append(submitId);
		sql.append(" and (type = ").append(Func.TYPE_CAMERA_HEIGHT).append(" or type = ").append(Func.TYPE_CAMERA_CUSTOM).append(" or type = ").append(Func.TYPE_CAMERA_MIDDLE).append(" or type = ").append(Func.TYPE_CAMERA).append(")");
		openHelper.execSQL(sql.toString());
	}

	/**
	 * 根据主键删除
	 * @param id
	 */
	public void deleteSubmitItemById(Integer id) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		StringBuffer sql=new StringBuffer();
		sql.append("delete from ").append(openHelper.SUBMIT_ITEM_TABLE).append(" where id = ").append(id);
		db.execSQL(sql.toString());
	}
	/**
	 * 根据类型删除
	 * @param type
	 */
	public void deleteSubmitItemByType(int type) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		StringBuffer sql=new StringBuffer();
		sql.append("delete from ").append(openHelper.SUBMIT_ITEM_TABLE).append(" where type = ").append(type);
		
		db.execSQL(sql.toString());
		JLog.d(TAG,"SubmitItemDB" + "deleteSubmitItemByType success==>");
		
	}
	/**
	 * 根据控件ID删除
	 * @param funcId
	 */
	public void deleteSubmitItemByParamName(String funcId) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		StringBuffer sql=new StringBuffer();
		sql.append("delete from ").append(openHelper.SUBMIT_ITEM_TABLE).append(" where param_name = '").append(funcId).append("'");
		
		db.execSQL(sql.toString());
		JLog.d(TAG,"SubmitItemDB" + "deleteSubmitItemByParamName success==>");
		
	}
	
	public void updateSubmitItemValue(String funcId,String value){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		StringBuffer buffer=new StringBuffer();
		buffer.append("update ").append(openHelper.SUBMIT_ITEM_TABLE).append(" set param_value= '").append(value).append("'");
		buffer.append(" where param_name ='").append(funcId).append("'");
		db.execSQL(buffer.toString());
	}
	
	/**
	 * 根据控件ID删除
	 * @param funcId
	 */
	public void deleteSubmitItemBySubmitIdAndParamName(Integer sumitId,String funcId) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		StringBuffer sql=new StringBuffer();
		sql.append("delete from ").append(openHelper.SUBMIT_ITEM_TABLE).append(" where param_name = '").append(funcId).append("'").append(" and submit_id = ").append(sumitId);
		db.execSQL(sql.toString());
	}
	
	/**
	 * 根据条件查出指定控件数据
	 * 
	 * @param submitId 表单ID
	 * @param paramNames (用逗号分割)
	 * @return
	 */
	public List<SubmitItem> findSubmitItemListBySubmitIdAndParamNames(Integer submitId, String paramNames) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		List<SubmitItem> list = new ArrayList<SubmitItem>();
		SubmitItem submitItem = null;
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(openHelper.SUBMIT_ITEM_TABLE);
		sql.append(" where submit_id =").append(submitId);
		sql.append(" and param_name in").append(" (").append(paramNames).append(")");
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				submitItem = putSubmitItem(cursor);
				list.add(submitItem);
			}
		}
		cursor.close();
		
		return list;
	}
	

	public TypeValue getValueBytype(int funcId,String value,Integer submitId,int menuType,int useType) throws DataComputeException{
		Func func = null;
				
		if(menuType == Menu.TYPE_VISIT){
			func = new VisitFuncDB(this.context).findFuncByFuncId(funcId);
		}else{
			func = new FuncDB(this.context).findFuncByFuncId(funcId);
		}
		TypeValue typeValue = new TypeValue();
		if(func.getType() == Func.TYPE_DATEPICKERCOMP ){
			typeValue.type = TypeValue.TYPE_DATE;
			typeValue.value = value;
		}else if (func.getType() == Func.TYPE_EDITCOMP && (func.getCheckType() != null && func.getCheckType() == Func.CHECK_TYPE_NUMERIC)) {

			typeValue.type = TypeValue.TYPE_NUM;
			typeValue.value = value;
		
		} else if(func.getType() == Func.TYPE_EDITCOMP||func.getType() == Func.TYPE_TEXTCOMP){
			
			typeValue.type = TypeValue.TYPE_STING;
			typeValue.value = value;
			
		}else if(func.getType() == Func.TYPE_SELECTCOMP){
			
			typeValue.value = new DictDB(context).findDictMultiChoiceValueStr(value, func.getDictDataId(), func.getDictTable());
			switch(useType){
			case 1: //计算
				typeValue.type = TypeValue.TYPE_NUM;
				break;
			case 2: //条件必填
				typeValue.type = TypeValue.TYPE_STING;
				break;
			}
			
			
		}else if(func.getType() == Func.TYPE_SELECT_OTHER){
			
			if(!RegExpvalidateUtils.isInteger(value)){
				typeValue.value = value;
			}else if(!"-1".equals(value)){
				typeValue.value = new DictDB(context).findDictMultiChoiceValueStr(value, func.getDictDataId(), func.getDictTable());
			}else if(submitId != null){
				SubmitItem item = findSubmitItem(submitId,funcId+"_other");
				typeValue.value = item.getParamValue();
			}else{
				typeValue.value = "";
			}
			
			switch(useType){
			case 1: //计算
				if(!RegExpvalidateUtils.isNumeric(typeValue.value.trim())){
					throw new DataComputeException("\""+func.getName()+"\""+context.getResources().getString(R.string.only_support_figure_type)); //由于输入错误，导致计算错误，抛出异常
				}
				typeValue.type = TypeValue.TYPE_NUM;
				break;
			case 2: //条件必填
				typeValue.type = TypeValue.TYPE_STING;
				break;
			}
			
		}else if(func.getType() == Func.TYPE_HIDDEN){
			
			if(func.getDefaultType() == Func.DEFAULT_TYPE_OTHER){
				switch(useType){
				case 1: //计算
					typeValue.type = TypeValue.TYPE_NUM;
					break;
				case 2: //条件必填
					typeValue.type = TypeValue.TYPE_STING;
					break;
				}
				typeValue.value = value;
			}else if(func.getDefaultType() == Func.DEFAULT_TYPE_DATE_NO_TIME){
				typeValue.type = TypeValue.TYPE_DATE;
				typeValue.value = value;
			}else if(func.getDefaultType() == Func.DEFAULT_TYPE_DATE || func.getDefaultType() == Func.DEFAULT_TYPE_STARTDATE){
				typeValue.type = TypeValue.TYPE_TIME;
				typeValue.value = value;
			}
			
		}
		 
		return typeValue;
	}
	
	public class TypeValue{
		static final int TYPE_STING = 0;
		static final int TYPE_NUM = 1;
		static final int TYPE_TIME = 2;
		static final int TYPE_DATE = 3;
		
		Integer type = null;
		String value = null;
	}
	
	public boolean isEmptyByEnterMustList(Integer submitId,String rule,int menuType) throws NumberFormatException, DataComputeException{
		boolean flag = false;
		String arr[] = rule.split(",");
		if(arr.length>0){
			SubmitItem item = findSubmitItem(submitId,arr[0]);
			if(item != null && !TextUtils.isEmpty(item.getParamValue()) && !TextUtils.isEmpty(arr[2])){
				TypeValue v1 = getValueBytype(Integer.valueOf(item.getParamName()),item.getParamValue(),item.getSubmitId(),menuType,2);
				flag = compare(v1,arr[2],Integer.valueOf(arr[1]));
			}
		}
		return flag;
	}
	
	private boolean compare(TypeValue v1,String v2,int opt){
		
		boolean flag = false;
		
		if(v1.type == TypeValue.TYPE_NUM){
			double num1 = TextUtils.isEmpty(v1.value)?0:Double.valueOf(v1.value);
			double num2 = Double.valueOf(v2);
			switch(opt){ //(1：等于、2：不等于、3：大于、4：大于等于、5：小于、6：小于等于)
			case 1:
				flag = (num1 == num2);
				break;
			case 2:
				flag = (num1 != num2);
				break;
			case 3:
				flag = (num1 > num2);
				break;
			case 4:
				flag = (num1 >= num2);
				break;
			case 5:
				flag = (num1 < num2);
				break;
			case 6:
				flag = (num1 <= num2);
				break;
			}
		}else if(v1.type == TypeValue.TYPE_STING){
			switch(opt){ //(1：等于、2：不等于、3：大于、4：大于等于、5：小于、6：小于等于)
			case 1:
				flag = v1.value.compareTo(v2) == 0 ? true : false;
				break;
			case 2:
				flag = v1.value.compareTo(v2) != 0 ? true : false;
				break;
			case 3:
				flag = v1.value.compareTo(v2) == 1 ? true : false;
				break;
			case 4:
				flag = v1.value.compareTo(v2) > -1 ? true : false;
				break;
			case 5:
				flag = v1.value.compareTo(v2) == -1 ? true : false;
				break;
			case 6:
				flag = v1.value.compareTo(v2) < 1 ? true : false;
				break;
			}
		}else if(v1.type == TypeValue.TYPE_DATE){
			Date date1 = DateUtil.getDate(v1.value,DateUtil.DATAFORMAT_STR);
			Date date2 = DateUtil.getDate(v2,DateUtil.DATAFORMAT_STR);
			switch(opt){ //(1：等于、2：不等于、3：大于、4：大于等于、5：小于、6：小于等于)
			case 1:
				flag = (date1.getTime() == date2.getTime());
				break;
			case 2:
				flag = (date1.getTime() != date2.getTime());
				break;
			case 3:
				flag = (date1.getTime() > date2.getTime());
				break;
			case 4:
				flag = (date1.getTime() >= date2.getTime());
				break;
			case 5:
				flag = (date1.getTime() < date2.getTime());
				break;
			case 6:
				flag = (date1.getTime() <= date2.getTime());
				break;
			}
		}else if(v1.type == TypeValue.TYPE_TIME){
			Date date1 = DateUtil.getDate(v1.value);
			Date date2 = DateUtil.getDate(v2);
			switch(opt){ //(1：等于、2：不等于、3：大于、4：大于等于、5：小于、6：小于等于)
			case 1:
				flag = (date1.getTime() == date2.getTime());
				break;
			case 2:
				flag = (date1.getTime() != date2.getTime());
				break;
			case 3:
				flag = (date1.getTime() > date2.getTime());
				break;
			case 4:
				flag = (date1.getTime() >= date2.getTime());
				break;
			case 5:
				flag = (date1.getTime() < date2.getTime());
				break;
			case 6:
				flag = (date1.getTime() <= date2.getTime());
				break;
			}
		}
		return flag;
	}
}
