package com.yunhu.yhshxc.database;

import gcg.org.debug.JLog;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.OrgStore;
import com.yunhu.yhshxc.utility.PublicUtils;

/**
 * 店面
 * @author jishen
 *
 */
public class OrgStoreDB {
	private String TAG = "orgStoreDB";

	private DatabaseHelper openHelper;

	public OrgStoreDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}
	
	/**
	 * 插入店面
	 * @param orgStore
	 */
	public void insertOrgStore(OrgStore orgStore){
		this.removeByStoreId("-999");
		openHelper.insert(openHelper.ORG_STORE_TABLE, putContentValues(orgStore));
//		JLog.d(TAG, "insertOrgStoreSuccess==>"+orgStore.getStoreName());
		
	}
	
	/**
	 * 根据店面ID查找店面
	 * @param storeId 店面ID
	 * @return
	 */
	public List<OrgStore> findOrgListByStoreId(String storeId){
		List<OrgStore> orgStore=new ArrayList<OrgStore>();
		if(PublicUtils.isIntegerArray(storeId)){
			SQLiteDatabase db = openHelper.getReadableDatabase();
			StringBuffer sql = new StringBuffer();
			
			sql.append("select * from ").append(openHelper.ORG_STORE_TABLE);
			sql.append(" where storeId in (").append(storeId).append(")");
			Cursor cursor = db.rawQuery(sql.toString(), null);
//			JLog.d(TAG, "findOrgListByOrgId==>" + sql.toString());
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					orgStore.add(putOrg(cursor));
				}
			}
			cursor.close();
			Collections.sort(orgStore,comparator);
		}
		return orgStore;
	}
	/**
	 * 根据店面名字模糊搜索
	 * @param fuzzySearch 名字
	 * @return
	 */
	public List<OrgStore> findAllOrgList(String fuzzySearch){
		SQLiteDatabase db = openHelper.getReadableDatabase();
		List<OrgStore> list=new ArrayList<OrgStore>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(openHelper.ORG_STORE_TABLE);
		if(!TextUtils.isEmpty(fuzzySearch)){
			sql.append(" where storeName ").append(" like ").append("'%").append(fuzzySearch).append("%'");
		}
		Cursor cursor = db.rawQuery(sql.toString(), null);
//		JLog.d(TAG, "findAllOrgList==>" + sql.toString());
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putOrg(cursor));
			}
		}
		cursor.close();
		Collections.sort(list,comparator);
		return list;
	}
	
	
	/**
	 * 根据机构ID 店面名字模糊搜索店面ID查找
	 * @param orgIdStr 机构ID
	 * @param fuzzySearch 店面名字模糊搜索内容
	 * @param queryWhereForDid 店面ID
	 * @return
	 */
	public List<OrgStore> findOrgList(String orgIdStr,String fuzzySearch,String queryWhereForDid){
		SQLiteDatabase db = openHelper.getReadableDatabase();
		List<OrgStore> list=new ArrayList<OrgStore>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(openHelper.ORG_STORE_TABLE).append(" where 1=1");
		if(!TextUtils.isEmpty(orgIdStr)&&PublicUtils.isIntegerArray(orgIdStr)){
			sql.append(" and orgId in (").append(orgIdStr).append(")");
		}
		if(!TextUtils.isEmpty(fuzzySearch)){
			sql.append(" and storeName ").append(" like ").append("'%").append(fuzzySearch).append("%'");
		}
		
		if(!TextUtils.isEmpty(queryWhereForDid)){
			sql.append(" and ").append("storeId in (").append(queryWhereForDid).append(")");
		}
		
		Cursor cursor = db.rawQuery(sql.toString(), null);
//		JLog.d(TAG, "findOrgList==>" + sql.toString());
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putOrg(cursor));
			}
		}
		cursor.close();
		Collections.sort(list,comparator);
		return list;
	}
	
	Comparator<OrgStore> comparator =new Comparator<OrgStore>() {
		
		private Collator collator = Collator.getInstance(java.util.Locale.CHINA);
		@Override
		public int compare(OrgStore lhs, OrgStore rhs) {
			if (lhs == null || rhs == null ||  TextUtils.isEmpty(lhs.getStoreName()) || TextUtils.isEmpty(rhs.getStoreName())) {
				return 1;
			}
			return collator.compare(lhs.getStoreName(), rhs.getStoreName());
		}
	};
	
	public OrgStore findOrgStoreByStoreId(String storeId){
		OrgStore orgStore=null;
		if(PublicUtils.isInteger(storeId)){
			SQLiteDatabase db = openHelper.getReadableDatabase();
			
			StringBuffer sql = new StringBuffer();
			sql.append("select * from ").append(openHelper.ORG_STORE_TABLE).append(" where storeId = ").append(storeId);
			
			Cursor cursor = db.rawQuery(sql.toString(), null);
			JLog.d(TAG, "findOrgList==>" + sql.toString());
			if (cursor.getCount() > 0) {
				if (cursor.moveToNext()) {
					orgStore=putOrg(cursor);
				}
			}
			cursor.close();
		}
		
		return orgStore;
	}
	
	/**
	 * 表格中的数据是否为空
	 * @return
	 */
	public boolean isEmpty(){
		SQLiteDatabase db = openHelper.getReadableDatabase();
		String sql = "select count(*) from "+openHelper.ORG_STORE_TABLE;
		Cursor cursor = db.rawQuery(sql, null);
		boolean flag = false;
		if(cursor.moveToNext()){
			flag = cursor.getInt(0) >0 ? false : true;
		}
		cursor.close();
		
		return flag;
	}
	
	private ContentValues putContentValues(OrgStore orgStore) {
		ContentValues cv = new ContentValues();
		cv.put("storeId", orgStore.getStoreId());
		cv.put("storeName", orgStore.getStoreName());
		cv.put("orgId", orgStore.getOrgId());
		cv.put("storeLon", orgStore.getStoreLon());
		cv.put("storeLat", orgStore.getStoreLat());
		cv.put("orgCode", orgStore.getOrgCode());
		cv.put("level", orgStore.getLevel());
		return cv;
	}
	
	private OrgStore putOrg(Cursor cursor) {
		int i = 0;
		OrgStore orgStore = new OrgStore();
		orgStore.setId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		orgStore.setStoreId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		orgStore.setOrgId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		orgStore.setStoreName(cursor.getString(i++));
		orgStore.setStoreLon(cursor.isNull(i) ? null : cursor.getDouble(i));i++;
		orgStore.setStoreLat(cursor.isNull(i) ? null : cursor.getDouble(i));i++;
		orgStore.setOrgCode(cursor.getString(i++));
		orgStore.setLevel(cursor.getInt(i++));
		return orgStore;
	}
	
	/**
	 * 根据店面ID删除店面
	 * @param storeId
	 */
	public void removeByStoreId(String storeId){
		if(PublicUtils.isIntegerArray(storeId)){
			StringBuffer sql=new StringBuffer();
			sql.append("delete from ").append(openHelper.ORG_STORE_TABLE).append(" where storeId in(").append(storeId).append(")");
			openHelper.execSQL(sql.toString());
		}
		
	}
	/**
	 * 删除多有数据
	 */
	
	public void removeAll(){
		openHelper.delete(openHelper.ORG_STORE_TABLE, null , null);
		
	}
	
	/**
	 * 根据店面名称查找
	 * @param storeName
	 * @return
	 */
	public OrgStore findOrgByStoreName(String storeName){
		OrgStore orgStore = null;
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(openHelper.ORG_STORE_TABLE);
		if(!TextUtils.isEmpty(storeName)){
			sql.append(" where storeName ").append(" = ").append("'").append(storeName).append("'");
			Cursor cursor = openHelper.query(sql.toString(), null);
			if (cursor.getCount() > 0) {
				if (cursor.moveToNext()) {
					orgStore = putOrg(cursor);
				}
			}
			cursor.close();

		}
		return orgStore;
	}
	
	/**
	 * 判断是否有店面控件，并且不是大数据sql控件 
	 * @param context
	 * @return true 有 false 无
	 */
	public boolean isHasOrgStore(){
		boolean flag = false;
		SQLiteDatabase db = openHelper.getReadableDatabase();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.FUNC_TABLE).append(" where type <> ").append(Func.TYPE_SQL_BIG_DATA).append(" and orgOption = ").append(Func.ORG_STORE);
		sql.append(" or type = ").append(Func.TYPE_ORDER);
		sql.append(" or type = ").append(Func.TYPE_ORDER2);
		sql.append(" or type = ").append(Func.TYPE_ORDER3);
		sql.append(" or type = ").append(Func.TYPE_ORDER3_SEND);
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor.getCount() > 0) {
			flag = true;
		}else{
			sql = new StringBuffer();
			sql.append(" select * from ").append(openHelper.VISIT_FUNC_TABLE).append(" where type <> ").append(Func.TYPE_SQL_BIG_DATA).append(" and orgOption = ").append(Func.ORG_STORE).append(" or type = ").append(Func.TYPE_ORDER3);
			sql.append(" or type = ").append(Func.TYPE_ORDER);
			sql.append(" or type = ").append(Func.TYPE_ORDER2);
			sql.append(" or type = ").append(Func.TYPE_ORDER3);
			sql.append(" or type = ").append(Func.TYPE_ORDER3_SEND);
			Cursor viositCursor = db.rawQuery(sql.toString(), null);
			if (viositCursor.getCount() > 0) {
				flag = true;
			}
			viositCursor.close();
		}
		cursor.close();
		return flag;
	}
	
}
