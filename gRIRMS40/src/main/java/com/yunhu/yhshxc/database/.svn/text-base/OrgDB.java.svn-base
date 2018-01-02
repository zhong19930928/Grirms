package com.yunhu.yhshxc.database;

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
import com.yunhu.yhshxc.bo.Org;
import com.yunhu.yhshxc.bo.OrgUser;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;

public class OrgDB {
	private String TAG = "orgDB";

	private DatabaseHelper openHelper;

	public OrgDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}
	
	/**
	 * 插入
	 * @param org
	 */
	public void insertOrg(Org org){
		ContentValues cv = putContentValues(org);
		openHelper.insert(openHelper.ORG_TABLE,cv);
//		JLog.d(TAG, "insertOrgSuccess==>"+org.getOrgName());
		
	}
	
	/**
	 * 根据机构ID查找
	 * @param orgId
	 * @return
	 */
	public List<Org> findOrgByOrgId(String orgId){
		List<Org> org=new ArrayList<Org>();
		if(PublicUtils.isIntegerArray(orgId)){
			StringBuffer sql = new StringBuffer();
			
			sql.append("select * from ").append(openHelper.ORG_TABLE);
			sql.append(" where orgId in(").append(orgId).append(")");
			Cursor cursor = openHelper.query(sql.toString(), null);
//			JLog.d(TAG, "findOrgByOrgId==>" + sql.toString());
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					org.add(putOrg(cursor));
				}
			}
			cursor.close();
		}
		return org;
	}
	
	/**
	 * 超找多个机构id下的机构
	 * @param orgIdStr
	 * @return
	 */
	public ArrayList<Org> findOrgListByOrgIds(String orgIdStr){
		ArrayList<Org> list = new ArrayList<Org>();
		if(PublicUtils.isIntegerArray(orgIdStr)){
			StringBuffer sql = new StringBuffer();
			sql.append("select * from ").append(openHelper.ORG_TABLE);
			sql.append(" where orgId in (").append(orgIdStr).append(")");
			Cursor cursor = openHelper.query(sql.toString(), null);
//			JLog.d(TAG, "findOrgListByOrgId==>" + sql.toString());
			
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					list.add(putOrg(cursor));
				}
			}
			cursor.close();
		}
		return list;
	}
	
	/**
	 * 根据用户ID查找上级的机构
	 * @param userId 用户ID
	 * @return
	 */
	public Org findParentOrgByUserId(int userId){
		StringBuffer sql = new StringBuffer();
		Org org=null;
		sql.append(" select * from ").append(openHelper.ORG_TABLE);
		sql.append(" where orgId = (select parentId from ").append(openHelper.ORG_TABLE);
		sql.append(" where orgId=").append("(select orgId from ").append(openHelper.ORG_USER_TABLE);
		sql.append(" where userId=").append(userId).append("))");
		Cursor cursor = openHelper.query(sql.toString(), null);
//		JLog.d(TAG, "findOrgByOrgId==>" + sql.toString());
		if (cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				org=putOrg(cursor);
			}
		}
		cursor.close();
		
		return org;
	}
	
	
	public  Org  findOrgByOrgId(int orgId){
		StringBuffer sql = new StringBuffer();
		Org org=null;
		sql.append("select * from ").append(openHelper.ORG_TABLE);
		sql.append(" where orgId=").append(orgId);
		Cursor cursor = openHelper.query(sql.toString(), null);
//		JLog.d(TAG, "findOrgByOrgId==>" + sql.toString());
		if (cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				org=putOrg(cursor);
			}
		}
		cursor.close();
		
		return org;	
		
	}
	/**
	 * 根据用户ID查找
	 * @param userId 用户ID
	 * @return
	 */
	public Org findOrgByUserId(int userId){
		StringBuffer sql = new StringBuffer();
		Org org=null;
		sql.append("select * from ").append(openHelper.ORG_TABLE);
		sql.append(" where orgId=").append("(select orgId from ").append(openHelper.ORG_USER_TABLE);
		sql.append(" where userId=").append(userId).append(")");
		Cursor cursor = openHelper.query(sql.toString(), null);
//		JLog.d(TAG, "findOrgByOrgId==>" + sql.toString());
		if (cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				org=putOrg(cursor);
			}
		}
		cursor.close();
		
		return org;
	}
	
	/**
	 * 根据层级 搜索条件 和上级条件查找
	 * @param orgLevel 层级
	 * @param fuzzySearch 搜索条件
	 * @param queryWhereForDid 上级条件
	 * @return
	 */
	public List<Org> findOrgListByOrgLevel(String orgLevel,String fuzzySearch,String queryWhereForDid){
		List<Org> list=new ArrayList<Org>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(openHelper.ORG_TABLE);
		sql.append(" where orgLevel=").append(orgLevel);
		if(!TextUtils.isEmpty(fuzzySearch)){
			sql.append(" and orgName ").append(" like ").append("'%").append(fuzzySearch).append("%'");
		}
		
		if(!TextUtils.isEmpty(queryWhereForDid)&&PublicUtils.isIntegerArray(queryWhereForDid)){
			sql.append(" and ").append("orgId in (").append(queryWhereForDid).append(")");
		}
		
		Cursor cursor = openHelper.query(sql.toString(), null);
//		JLog.d(TAG, "findOrgListByOrgLevel==>" + sql.toString());
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
	 * 根据上级关系 和机构名称 和机构id查找
	 * @param orgRelation 上级关系
	 * @param fuzzySearch 机构名称
	 * @param queryWhereForDid 机构ID
	 * @return
	 */
	public List<Org> findOrgListByOrgParentId(String orgRelation,String fuzzySearch,String queryWhereForDid){
		List<Org> list=new ArrayList<Org>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(openHelper.ORG_TABLE);
		sql.append(" where parentId in (").append(orgRelation).append(" )");
		if(!TextUtils.isEmpty(fuzzySearch)){
			sql.append(" and orgName ").append(" like ").append("'%").append(fuzzySearch).append("%'");
		}
		
		if(!TextUtils.isEmpty(queryWhereForDid)&&PublicUtils.isIntegerArray(queryWhereForDid)){
			sql.append(" and ").append("orgId in (").append(queryWhereForDid).append(")");
		}
		
		Cursor cursor = openHelper.query(sql.toString(), null);
//		JLog.d(TAG, "findOrgListByOrgParentId==>" + sql.toString());
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putOrg(cursor));
			}
		}
		cursor.close();
		Collections.sort(list,comparator);
		return list;
	}
	
	Comparator<Org> comparator =new Comparator<Org>() {
		
		private Collator collator = Collator.getInstance(java.util.Locale.CHINA);
		@Override
		public int compare(Org lhs, Org rhs) {
			if (lhs == null || rhs == null ||  TextUtils.isEmpty(lhs.getOrgName()) || TextUtils.isEmpty(rhs.getOrgName())) {
				return 1;
			}
			return collator.compare(lhs.getOrgName(), rhs.getOrgName());
		}
	};
	
	/**
	 * 根据上级ID 和机构名称查找
	 * @param parentIds 上级id
	 * @param fuzzySearch 机构名称
	 * @return
	 */
	public List<Org> findOrgListByParentIds(String parentIds,String fuzzySearch){
		List<Org> list=new ArrayList<Org>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(openHelper.ORG_TABLE).append(" where 1=1");
		if(!TextUtils.isEmpty(parentIds)){
			sql.append(" and parentId in (").append(parentIds).append(")");
		}
		
		if(!TextUtils.isEmpty(fuzzySearch)){
			sql.append(" and orgName ").append(" like ").append("'%").append(fuzzySearch).append("%'");
		}
		Cursor cursor = openHelper.query(sql.toString(), null);
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
	 * 查找机构的最大层级
	 * @return
	 */
	public int findMaxLevel(){
		StringBuffer sql = new StringBuffer();
		sql.append("select orgLevel from ").append(openHelper.ORG_TABLE).append(" order by length(orgLevel) desc,orglevel desc limit 1");
		Cursor cursor = openHelper.query(sql.toString(), null);
		int max = 0;
		if (cursor.moveToNext()) {
			max=cursor.getInt(0);
		}
		cursor.close();
		
		return max;
	}
	
	/**
	 * 判断机构表中是否为空
	 * @return
	 */
	public boolean isEmpty(){
		String sql = "select count(*) from "+openHelper.ORG_TABLE;
		Cursor cursor = openHelper.query(sql, null);
		boolean flag = false;
		if(cursor.moveToNext()){
			flag = cursor.getInt(0) >0 ? false : true;
		}
		cursor.close();
		
		return flag;
	}
	
	private ContentValues putContentValues(Org org) {
		ContentValues cv = new ContentValues();
		cv.put("orgId", org.getOrgId());
		cv.put("orgName", org.getOrgName());
		cv.put("orgLevel", org.getOrgLevel());
		cv.put("parentId", org.getParentId());
		cv.put("code", org.getCode());
		return cv;
	}
	
	private Org putOrg(Cursor cursor) {
		int i = 0;
		Org org = new Org();
		org.setId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		org.setOrgId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		org.setOrgName(cursor.getString(i++));
		org.setOrgLevel(cursor.getString(i++));
		org.setParentId(cursor.getString(i++));
		org.setCode(cursor.getString(i++));
		return org;
	}
	
	/**
	 * 根据机构id删除机构
	 * @param orgId
	 */
	public void removeByOrgId(String orgId){
		if(PublicUtils.isIntegerArray(orgId)){
			StringBuffer sql=new StringBuffer();
			sql.append("delete from ").append(openHelper.ORG_TABLE).append(" where orgId in(").append(orgId).append(")");
			openHelper.execSQL(sql.toString());
		}
		
	}
	/**
	 * 删除所有机构
	 */
	public void removeAll(){
		openHelper.delete(openHelper.ORG_TABLE, null , null);
		
	}
	
	/**
	 * 根据组织机构名称查找
	 * @param orgName
	 * @return
	 */
	public Org findOrgByOrgName(String orgName){
		Org org = null;
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(openHelper.ORG_TABLE);
		if(!TextUtils.isEmpty(orgName)){
			sql.append(" where orgName ").append(" = ").append("'").append(orgName).append("'");
			Cursor cursor = openHelper.query(sql.toString(), null);
			if (cursor.getCount() > 0) {
				if (cursor.moveToNext()) {
					org = putOrg(cursor);
				}
			}
			cursor.close();

		}
		return org;
	}
	
	/**
	 * 判断是否有机构，并且不是大数据sql控件 
	 * @param context
	 * @return true 有 false 无
	 */
	public boolean isHasOrg(){
		boolean flag = false;
		SQLiteDatabase db = openHelper.getReadableDatabase();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.FUNC_TABLE).append(" where type <> ").append(Func.TYPE_SQL_BIG_DATA).append(" and orgOption = ").append(Func.ORG_OPTION);
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor.getCount() > 0) {
			flag = true;
		}else{
			sql = new StringBuffer();
			sql.append(" select * from ").append(openHelper.VISIT_FUNC_TABLE).append(" where type <> ").append(Func.TYPE_SQL_BIG_DATA).append(" and orgOption = ").append(Func.ORG_OPTION);;
			Cursor viositCursor = db.rawQuery(sql.toString(), null);
			if (viositCursor.getCount() > 0) {
				flag = true;
			}
			viositCursor.close();
		}
		cursor.close();
		return flag;
	}

	/**
	 * 新店上报，机构查询
	 * @param context
	 * @return
	 */
	public List<Org> storeExpandOrg(Context context,String fuuzy){
		List<Org> list = new ArrayList<Org>();
		OrgUser user = new OrgUserDB(context).findUserByUserId(SharedPreferencesUtil.getInstance(context).getUserId());
		if (user!=null) {
			StringBuffer sql = new StringBuffer();
			sql.append(" select * from ").append(openHelper.ORG_TABLE).append(" where code like '%").append(user.getOrgCode()).append("%'");//默认机构不是店面
			if (!TextUtils.isEmpty(fuuzy)) {
				sql.append(" and orgName like '%").append(fuuzy).append("%'");
			}
			Cursor cursor = openHelper.query(sql.toString(), null);
			Org org = null;
			if (cursor!=null) {
				if (cursor.getCount()>0) {
					while (cursor.moveToNext()) {
						org = putOrg(cursor);
						list.add(org);
					}
				}
				cursor.close();
			}
			if (list.isEmpty()) {
				String userPCode = user.getOrgCode().replace("-"+user.getOrgId(), "");
				StringBuffer sql_2 = new StringBuffer();
				sql_2.append(" select * from ").append(openHelper.ORG_TABLE).append(" where code like '%").append(userPCode).append("%'");//最后一级，机构是店面默认是上级机构
				if (!TextUtils.isEmpty(fuuzy)) {
					sql_2.append(" and orgName like '%").append(fuuzy).append("%'");
				}
				Cursor cursor2 = openHelper.query(sql_2.toString(), null);
				if (cursor2!=null) {
					if (cursor2.getCount() > 0) {
						while (cursor2.moveToNext()) {
							org = putOrg(cursor2);
							list.add(org);
						}
					}
					cursor2.close();
				}
			}
		}
		return list;
	}
}
