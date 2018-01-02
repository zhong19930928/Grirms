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
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.bo.OrgUser;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;

/**
 * 用户
 * @author jishen
 *
 */
public class OrgUserDB {
	private String TAG = "OrgUserDB";
	private DatabaseHelper openHelper;
	private Context context=null;
	public OrgUserDB(Context context) {
		this.context = context;
		openHelper = DatabaseHelper.getInstance(context);
	}
	
	/**
	 * 插入用户
	 * @param orgUser
	 */
	public void insertOrgUser(OrgUser orgUser){
		openHelper.insert(openHelper.ORG_USER_TABLE, putContentValues(orgUser));
		JLog.d(TAG, "insertOrgUser Success==>"+orgUser.getUserName());
		
	}
	
	/**
	 * 根据用户ID查找用户
	 * @param userId 要查找的用户ID
	 * @return
	 */
	public List<OrgUser> findOrgUserByUserId(String userId){
		List<OrgUser> orgUser=new ArrayList<OrgUser>();
		if(PublicUtils.isIntegerArray(userId)){
			StringBuffer sql = new StringBuffer();
			sql.append("select * from ").append(openHelper.ORG_USER_TABLE);
			sql.append(" where userId in (").append(userId).append(")");
			Cursor cursor = openHelper.query(sql.toString(), null);
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					orgUser.add(putOrgUser(cursor));
				}
			}
			cursor.close();
			Collections.sort(orgUser,comparator);	
		}
		
		return orgUser;
	}
	
	/**
	 * 根据用户ID查找用户
	 * @param userId 要查找的用户ID
	 * @return
	 */
	public OrgUser findUserByUserId(int userId){
		StringBuffer sql = new StringBuffer();
		OrgUser orgUser= null;
		sql.append("select * from ").append(openHelper.ORG_USER_TABLE);
		sql.append(" where userId =").append(userId);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				orgUser = putOrgUser(cursor);
			}
		}
		cursor.close();
		return orgUser;
	}
	/**
	 * 根据角色ID查找用户
	 * @param roleId 要查找的角色ID
	 * @return
	 */
	public List<OrgUser> findUserByRoleId(int roleId){
		StringBuffer sql = new StringBuffer();
		List<OrgUser> orgUser=new ArrayList<OrgUser>();
		sql.append("select * from ").append(openHelper.ORG_USER_TABLE);
		sql.append(" where roleId in (").append(roleId).append(")");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				orgUser.add(putOrgUser(cursor));
			}
		}
		cursor.close();
		Collections.sort(orgUser,comparator);
		return orgUser;
	}
	/**
	 * 根据用户名称模糊查找用户
	 * @param fuzzySearch 用户名字
 	 * @return
	 */
	public List<OrgUser> findAllOrgUserList(String fuzzySearch){
		List<OrgUser> list=new ArrayList<OrgUser>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(openHelper.ORG_USER_TABLE);
		if(!TextUtils.isEmpty(fuzzySearch)){
			sql.append(" where userName ").append(" like ").append("'%").append(fuzzySearch).append("%'");
		}
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putOrgUser(cursor));
			}
		}
		cursor.close();
		Collections.sort(list,comparator);
		return list;
	}
	
	
	/**
	 * 根据用户ID 和用户名称模糊搜索查找用户的名称的拼接集合
	 * @param idStr 用户id
	 * @param fuzzySearch模糊搜索条件
	 * @return
	 */
	public String findDictMultiChoiceValueStr(String idStr,String fuzzySearch){
		String result = "";
		if(PublicUtils.isIntegerArray(idStr)){
			StringBuffer sql = new StringBuffer();
			sql.append("select ").append(" userName ").append(" from ").append(openHelper.ORG_USER_TABLE);
			sql.append(" where ").append(" userId ").append(" in (").append(idStr).append(")");
			if(!TextUtils.isEmpty(fuzzySearch)){
				sql.append(" and userName ").append(" like ").append("'%").append(fuzzySearch).append("%'");
			}
			Cursor cursor = openHelper.query(sql.toString(), null);
			StringBuffer sb = new StringBuffer();
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					sb.append(",").append(cursor.getString(0));
				}
			}
			cursor.close();
			
			if(sb.length() > 0){
				result = sb.substring(1);
			}
		}
		
		return result;
	}
	
	
	
	/**
	 * 根据用户角色roleId 和用户名称模糊搜索查找用户的名称的拼接集合
	 * @param idStr 用户id
	 * @param fuzzySearch模糊搜索条件
	 * @return
	 */
	public List<OrgUser> findDictMultiChoiceValueStrByRoleId(String roleId,String fuzzySearch){
		List<OrgUser> list=new ArrayList<OrgUser>();
		if(PublicUtils.isIntegerArray(roleId)){
			StringBuffer sql = new StringBuffer();
			sql.append("select * from ").append(openHelper.ORG_USER_TABLE);
			sql.append(" where ").append(" roleId ").append(" in (").append(roleId).append(")");
			if(!TextUtils.isEmpty(fuzzySearch)){
				sql.append(" and userName ").append(" like ").append("'%").append(fuzzySearch).append("%'");
			}
			JLog.d("alin", "sql = "+sql.toString());
			Cursor cursor = openHelper.query(sql.toString(), null);
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					list.add(putOrgUser(cursor));
				}
			}
			cursor.close();
			Collections.sort(list,comparator);
		}
		
		return list;
	}
	
	
	
	/**
	 * 查找用户
	 * @param menuId 菜单ID
	 * @param auth 穿透规则
	 * @param authOrgId 机构ID
	 * @param fuzzySearch 模糊搜索条件
	 * @param queryWhereForDid 用户ID
	 * @return
	 */
	public List<OrgUser> findUserList(int menuId,Integer auth,String authOrgId,String fuzzySearch,String queryWhereForDid){
		
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(openHelper.ORG_USER_TABLE).append(" where purview like '%,").append(menuId).append(",%' ");
		if(!TextUtils.isEmpty(fuzzySearch)){
			sql.append(" and userName ").append(" like ").append("'%").append(fuzzySearch).append("%'");
		}
		
		if(!TextUtils.isEmpty(queryWhereForDid)&&PublicUtils.isIntegerArray(queryWhereForDid)){
			sql.append(" and ").append("userId in (").append(queryWhereForDid).append(")");
		}
		int userId = SharedPreferencesUtil.getInstance(context.getApplicationContext()).getUserId();
		
		return exeSqlByAuth(sql,userId,auth,authOrgId);
	}
	
	/**
	 * 超找用户
	 * @param orgIdStr 机构ID
	 * @param fuzzyContent 名字模糊搜索条件
	 * @param queryWhereForDid 用户ID
	 * @return
	 */
	public List<OrgUser> findOrgUserListByAuthSearch(String orgIdStr,String fuzzyContent,String queryWhereForDid){
		List<OrgUser> list=new ArrayList<OrgUser>();
		int userId = SharedPreferencesUtil.getInstance(context.getApplicationContext()).getUserId();
		List<OrgUser> currentUserList = this.findOrgUserByUserId(userId+"");
		if(currentUserList.isEmpty()){
			return list;
		}
		
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(openHelper.ORG_USER_TABLE).append(" where 1=1 ");
		if(!TextUtils.isEmpty(orgIdStr)&&PublicUtils.isIntegerArray(orgIdStr)){
			sql.append("  and orgId in (").append(orgIdStr).append(")");
		}
		if(!TextUtils.isEmpty(fuzzyContent)){
			sql.append(" and userName ").append(" like ").append("'%").append(fuzzyContent).append("%'");
		}
		if(!TextUtils.isEmpty(queryWhereForDid)&&PublicUtils.isIntegerArray(queryWhereForDid)){
			sql.append(" and ").append("userId in (").append(queryWhereForDid).append(")");
		}
		
		OrgUser currentUser = currentUserList.get(0);
		Integer auth = null;
		if (currentUser.getAuthSearch() !=null){
			auth = Integer.valueOf(currentUser.getAuthSearch());
		}
		return exeSqlByAuth(sql,userId,auth,currentUser.getAuthOrgId());
	}
	
	/**
	 * 查找用户
	 * @param auth 穿透条件
	 * @param authOrgId 机构ID
	 * @param fuzzyContent 名称模糊搜索条件
	 * @param queryWhereForDid 用户ID
	 * @return
	 */
	public List<OrgUser> findOrgUserListByAuth(Integer auth,String authOrgId,String fuzzyContent,String queryWhereForDid){
		
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(openHelper.ORG_USER_TABLE).append(" where 1=1 ");
		if(!TextUtils.isEmpty(fuzzyContent)){
			sql.append(" and userName ").append(" like ").append("'%").append(fuzzyContent).append("%'");
		}
		if(!TextUtils.isEmpty(queryWhereForDid)&&PublicUtils.isIntegerArray(queryWhereForDid)){
			sql.append(" and ").append("userId in (").append(queryWhereForDid).append(")");
		}
		int userId = SharedPreferencesUtil.getInstance(context.getApplicationContext()).getUserId();
		return exeSqlByAuth(sql,userId,auth,authOrgId);
	}
	
	/**
	 * 穿透查询
	 * @param sql 拼接查询sql
	 * @param userId 用户ID
	 * @param auth 穿透规则 
	 * @param authOrgId 穿透机构ID
	 * @return
	 */
	private List<OrgUser> exeSqlByAuth(StringBuffer sql,int userId,Integer auth,String authOrgId){
		List<OrgUser> list=new ArrayList<OrgUser>();
		boolean flag = false; //是否直接,也就是通透
		//（1：自己、2：本区域直接、3：本区域所有、4：同上级平行部门、5：直接下属、6：所有下属、7：自己及直接下属、8：自己及所有下属、9：所有、10：指定区域 11：指定多个区域（authOrgId）)
		if(auth != null){
			OrgUser user = null;
			switch(auth){
				case 1:
					sql.append(" and userId = ").append(userId);
					break;
				case 2:
					sql.append(" and orgId in (select orgId from ").append(openHelper.ORG_USER_TABLE).append(" where userId = ").append(userId).append(")");
					break;
				case 3:
					user = this.findOrgUserByUserId(userId+"").get(0);
					sql.append(" and (userId = ").append(userId);
					sql.append(" or orgId in (select orgId from ").append(openHelper.ORG_USER_TABLE).append(" where org_code like '%").append(user.getOrgCode()).append("%'))");
					break;
				case 4:
					user = this.findOrgUserByUserId(userId+"").get(0);
					sql.append(" and (userId = ").append(userId);                 //user.getOrgCode() 更改为上级的usercode
					sql.append(" or orgId in (select orgId from ").append(openHelper.ORG_USER_TABLE).append(" where org_code like '%").append(user.getOrgCode().replaceAll(user.getOrgId()+"", "")).append("%'))");
					break;
				case 5:
					sql.append(" and pid =").append(userId);
					break;
				case 6:
					list = findUserListByPid(userId,null);
					flag = true;
					break;
				case 7:
					sql.append(" and (userId = ").append(userId).append(" or pid =").append(userId).append(")");
					break;
				case 8:
					list = findUserListByPid(userId);
					flag = true;
					break;
				case 9:
					break;
				case 10:
					if(!TextUtils.isEmpty(authOrgId)){
						user = this.findOrgUserList(authOrgId).get(0);
						sql.append(" and orgId in (select orgId from ").append(openHelper.ORG_USER_TABLE).append(" where org_code like '%").append(user.getOrgCode()).append("%')");
					}
					break;
				case 11:
					if(!TextUtils.isEmpty(authOrgId)){
						List<OrgUser> orgUserList = this.findOrgUserList(authOrgId);
						sql.append(" and orgId in (select orgId from ").append(openHelper.ORG_USER_TABLE).append(" where ((1<>1) ");
						for(OrgUser u : orgUserList){
							sql.append(" or (org_code like '%").append(u.getOrgCode()).append("%')");
						}
						sql.append("))");
					}
			}
		}
		
		Cursor cursor = openHelper.query(sql.toString(), null);
		JLog.d(TAG, "findAllOrgUserList==>" + sql.toString());
		if(flag){
			List<OrgUser> newlist=new ArrayList<OrgUser>();
			if(cursor.getCount() > 0){
				while (cursor.moveToNext()) {
					OrgUser user = putOrgUser(cursor);
					for(OrgUser u : list){
						if(u.getUserId().intValue() == user.getUserId()){
							newlist.add(user);
						}
					}
				}
			}
			list = newlist;
		}else{
			if(cursor.getCount() > 0){
				
				while (cursor.moveToNext()) {
					list.add(putOrgUser(cursor));
				}
			}
		}
		cursor.close();
		Collections.sort(list,comparator);
		return list;
	}
	
	
	Comparator<OrgUser> comparator =new Comparator<OrgUser>() {
		
		private Collator collator = Collator.getInstance(java.util.Locale.CHINA);
		@Override
		public int compare(OrgUser lhs, OrgUser rhs) {
			if (lhs == null || rhs == null ||  TextUtils.isEmpty(lhs.getUserName()) || TextUtils.isEmpty(rhs.getUserName())) {
				return 1;
			}
			return collator.compare(lhs.getUserName(), rhs.getUserName());
		}
	};
	
	/**
	 * 根据pid超找
	 * @param pid
	 * @return
	 */
	public List<OrgUser> findUserListByPid(int pid){
		List<OrgUser> list = findUserListByPid(pid,null);
		List<OrgUser> user = this.findOrgUserByUserId(pid+"");
		list.addAll(user);
		Collections.sort(list,comparator);
		return list;
	}
	
	/**
	 * 根据pid和用户ID查找
	 * @param pid
	 * @param userIdStr
	 * @return
	 */
	public List<OrgUser> findUserListByPid(int pid,String userIdStr){
	    List<OrgUser> list = new ArrayList<OrgUser>();
		StringBuffer sql = new StringBuffer();
		if(TextUtils.isEmpty(userIdStr)){
			sql.append("select * from ");
			sql.append(openHelper.ORG_USER_TABLE);
			sql.append(" where pid =").append(pid);
		}else if(PublicUtils.isIntegerArray(userIdStr)){
			sql.append("select a.* from (select * from ").append(openHelper.ORG_USER_TABLE).append(" where userId in (").append(userIdStr).append(")) a");
			sql.append(" where a.pid =").append(pid);
		}
		Cursor cursor = openHelper.query(sql.toString(), null);
		while (cursor.moveToNext()) {
			list.add(putOrgUser(cursor));
		}
		cursor.close();
		
		
		List<OrgUser> reList = new ArrayList<OrgUser>();
		for(OrgUser user : list){
			List<OrgUser> userList = findUserListByPid(user.getUserId(),userIdStr);
			if(!userList.isEmpty()){
				reList.addAll(userList);
			}
		}
		reList.addAll(list);
		Collections.sort(reList,comparator);
		return reList;
	}
	
	/**
	 * 根据机构ID查找
	 * @param orgIdStr 机构ID
	 * @return
	 */
	public List<OrgUser> findOrgUserList(String orgIdStr){
		List<OrgUser> list=new ArrayList<OrgUser>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(openHelper.ORG_USER_TABLE);
		if(!TextUtils.isEmpty(orgIdStr)&&PublicUtils.isIntegerArray(orgIdStr)){
			sql.append(" where orgId in (").append(orgIdStr).append(")");
		}
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putOrgUser(cursor));
			}
		}
		cursor.close();
		Collections.sort(list,comparator);
		return list;
	}
	/**
	 * 查找所有
	 * @return
	 */
	public List<OrgUser> findAllUserList(){
		List<OrgUser> list=new ArrayList<OrgUser>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(openHelper.ORG_USER_TABLE);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				OrgUser user = putOrgUser(cursor);
				if(user.getUserId() != SharedPreferencesUtil.getInstance(context).getUserId()){
					list.add(putOrgUser(cursor));
				}
			}
		}
		cursor.close();
		Collections.sort(list,comparator);
		return list;
	}
	
	/**
	 * 查找所有角色
	 * @return
	 */
	public List<OrgUser> findAllRoleList(){
		List<OrgUser> list=new ArrayList<OrgUser>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(openHelper.ORG_USER_TABLE).append(" group by roleId");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putOrgUser(cursor));
			}
		}
		cursor.close();
		Collections.sort(list,comparator);
		return list;
	}
	
	
	/**
	 * 表格中的数据是否为空
	 * @return true不为空 false是空
	 */
	public boolean isEmpty(){
		String sql = "select count(*) from "+openHelper.ORG_USER_TABLE;
		Cursor cursor = openHelper.query(sql, null);
		boolean flag = false;
		if(cursor.moveToNext()){
			flag = cursor.getInt(0) >0 ? false : true;
		}
		cursor.close();
		
		return flag;
	}
	
	private ContentValues putContentValues(OrgUser orgUser) {
		ContentValues cv = new ContentValues();
		cv.put("userId", orgUser.getUserId());
		cv.put("userName", orgUser.getUserName());
		cv.put("orgId", orgUser.getOrgId());
		cv.put("pid", orgUser.getPid());
		cv.put("purview", orgUser.getPurview());
		cv.put("authsearch", orgUser.getAuthSearch());
		cv.put("authorgid", orgUser.getAuthOrgId());
		cv.put("org_code", orgUser.getOrgCode());
		cv.put("roleId", orgUser.getRoleId());
		cv.put("roleName", orgUser.getRoleName());
		return cv;
	}
	
	private OrgUser putOrgUser(Cursor cursor) {
		int i = 0;
		OrgUser user = new OrgUser();
		user.setId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		user.setUserId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		user.setOrgId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		user.setUserName(cursor.getString(i++));
		user.setPid(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		user.setPurview(cursor.getString(i++));
		user.setAuthSearch(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		user.setAuthOrgId(cursor.getString(i++));
		user.setOrgCode(cursor.getString(i++));
		user.setRoleId(cursor.getInt(i++));
		user.setRoleName(cursor.getString(i++));
		return user;
	}
	
	/**
	 * 根据用户ID删除用户
	 * @param userId
	 */
	public void removeByUserId(String userId){
		if(PublicUtils.isIntegerArray(userId)){
			StringBuffer sql=new StringBuffer();
			sql.append("delete from ").append(openHelper.ORG_USER_TABLE).append(" where userId in(").append(userId).append(")");
			openHelper.execSQL(sql.toString());
		}
	}
	/**
	 * 删除所有数据
	 */
	public void removeAll(){
		openHelper.delete(openHelper.ORG_USER_TABLE, null, null);
		
	}
	
	/**
	 * 根据用户名称查找
	 * @param storeName
	 * @return
	 */
	public OrgUser findOrgByUserName(String userName){
		OrgUser orgUser = null;
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(openHelper.ORG_USER_TABLE);
		if(!TextUtils.isEmpty(userName)){
			sql.append(" where userName ").append(" = ").append("'").append(userName).append("'");
			Cursor cursor = openHelper.query(sql.toString(), null);
			if (cursor.getCount() > 0) {
				if (cursor.moveToNext()) {
					orgUser = putOrgUser(cursor);
				}
			}
			cursor.close();

		}
		return orgUser;
	}

	/**
	 * 判断是否有用户控件，并且不是大数据sql控件 
	 * @param context
	 * @return true 有 false 无
	 */
	public boolean isHasUserByFunc(){
		boolean flag = false;
		SQLiteDatabase db = openHelper.getReadableDatabase();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.FUNC_TABLE).append(" where type <> ").append(Func.TYPE_SQL_BIG_DATA).append(" and orgOption = ").append(Func.ORG_USER);
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor.getCount() > 0) {
			flag = true;
		}else{
			sql = new StringBuffer();
			sql.append(" select * from ").append(openHelper.VISIT_FUNC_TABLE).append(" where type <> ").append(Func.TYPE_SQL_BIG_DATA).append(" and orgOption = ").append(Func.ORG_USER);;
			Cursor viositCursor = db.rawQuery(sql.toString(), null);
			if (viositCursor.getCount() > 0) {
				flag = true;
			}
			viositCursor.close();
		}
		cursor.close();
		return flag;
	}
	
	public boolean isHasUserByMenu(){
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.MAIN_MENU_TABLE)
		.append(" where type in(").append(Menu.TYPE_ATTENDANCE)
		.append(",").append(Menu.TYPE_NEW_ATTENDANCE).append(")");
		Cursor cursor = openHelper.query(sql.toString(), null);
		boolean flag = cursor == null || cursor.getCount() == 0 ? false : true; 
		cursor.close();
		return flag;
	}
	
	public boolean isHasUserByModule(){
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(openHelper.MODULE_TABLE);
		sql.append(" where type in(").append(Constants.MODULE_TYPE_ISSUED).append(",").append(Constants.MODULE_TYPE_REASSIGN).append(")");
		Cursor cursor = openHelper.query(sql.toString(), null);
		boolean flag = cursor == null || cursor.getCount() == 0 ? false : true; 
		cursor.close();
		return flag;
	}
	
}
