package com.yunhu.yhshxc.activity.addressBook.db;

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

import com.yunhu.yhshxc.activity.addressBook.bo.AdressBookUser;
import com.yunhu.yhshxc.bo.OrgUser;
import com.yunhu.yhshxc.database.DatabaseHelper;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;

public class AdressBookUserDB {
	private DatabaseHelper openHelper;
	private Context context;
	
	public AdressBookUserDB(Context context) {
		this.context = context;
		openHelper = DatabaseHelper.getInstance(context);
	}
	
	
	public void insert(AdressBookUser attention){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues cv = putContentValues(attention);
		db.insert(openHelper.ADRESS_BOOK_USER, null, cv);
	}
	
	/**
	 * 查找最高层
	 * @return
	 */
	public AdressBookUser findFirstLevelAdressBook(){
		StringBuffer sql = new StringBuffer();
		AdressBookUser user = null;
//		sql.append(" select * from ").append(openHelper.ADRESS_BOOK_USER).append(" order by ol limit 1");
		sql.append(" select * from ").append(openHelper.ADRESS_BOOK_USER).
				append(" where oc like '%").append("5736403").append("-%'").
				append(" order by substr(oc,1,8) limit 1");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				user = putAdressBookUser(cursor);
			}
		}
		cursor.close();
		return user;
	}
	/**
	 * 查找机构层 的人
	 * @param oId
	 * @return
	 */
	public List<AdressBookUser> findOrgBookUser(int oId){
		List<AdressBookUser> list = new ArrayList<AdressBookUser>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.ADRESS_BOOK_USER).append(" where oId = ").append(oId).append(" order by olevel+0 asc");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				AdressBookUser user = putAdressBookUser(cursor);
				list.add(user);
			}
		}
		cursor.close();
//		Collections.sort(list,comparatorRn);
		return list;
	}
	
	public int orgBookUserCount(int oId,int level){
		SQLiteDatabase db = openHelper.getReadableDatabase();
		int count = 0;
		StringBuffer sql = new StringBuffer("select count(*) from ").append(openHelper.ADRESS_BOOK_USER);
		sql.append(" where ol >= ").append(level);
		sql.append(" and oc like '%").append(oId).append("%'");
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor!=null) {
			if (cursor.moveToNext()) {
				count = cursor.getInt(0);
			}
		}
		cursor.close();
		return count;
	}

	public List<AdressBookUser> findListUserByName(String name){
		List<AdressBookUser> list = new ArrayList<AdressBookUser>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.ADRESS_BOOK_USER)
		.append(" where un like '%").append(name).append("%'");
		Cursor cursor = openHelper.query(sql.toString(),null);
		if(cursor!=null){
			while (cursor.moveToNext()){
				list.add(putAdressBookUser(cursor));
			}
		}
		cursor.close();
		return list;
	}
	
	/**
	 * 查找机构层下有机构的人
	 * @param level
	 * @param oc
	 * @return
	 */
	
	public List<AdressBookUser> findAllOrgBook(int level,String oc){
		List<AdressBookUser> list = new ArrayList<AdressBookUser>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.ADRESS_BOOK_USER);
		if (!TextUtils.isEmpty(oc)) {
			sql.append(" where oc like '%").append(oc).append("-%'");
			sql.append(" group by substr(oc,1,").append(oc.length()+8).append(")");
		}
		sql.append(" order by oId desc");

		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				AdressBookUser user = putAdressBookUser(cursor);
				String op= user.getOp();
				String[] ops = op.split("-");
				String[] ocs = oc.split("-");
				String[] uoc = user.getOc().split("-");
				user.setOn(ops[ocs.length]);
				user.setOc(oc+"-"+uoc[ocs.length]);
				user.setOrg(true);
				list.add(user);
//				String op = user.getOc();
//				if (!TextUtils.isEmpty(op)) {
//					String[] ops = op.split("-");
//					level = findOrgBookLevel(level, oc);
//					if (ops.length-level == 1) {
//						user.setOrg(true);
//						list.add(user);
//					}
//				}
			}
		}
		cursor.close();
		return list;
	}
	
	public int findOrgBookLevel(int level,String oc){
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.ADRESS_BOOK_USER).append(" where 1=1");
		if (!TextUtils.isEmpty(oc)) {
			sql.append(" and oc like '%").append(oc).append("%'");
		}
		sql.append(" and ol > ").append(level);
		sql.append(" order by ol limit 1");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				AdressBookUser user = putAdressBookUser(cursor);
				level = user.getOl()-1;
			}
		}
		cursor.close();
		return level;
	}
	
	/**
	 * 查找所有
	 * @return
	 */
	public List<AdressBookUser> findAllUserList(){
		List<AdressBookUser> list=new ArrayList<AdressBookUser>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(openHelper.ADRESS_BOOK_USER);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				AdressBookUser user = putAdressBookUser(cursor);
				if(SharedPreferencesUtil.getInstance(context).getUserId() != user.getuId()){
					list.add(user);
				}
			}
		}//美
		cursor.close();
		Collections.sort(list,comparator);
		return list;
	}
	/**
	 * 根据Id查找用户
	 * @return
	 */
	public AdressBookUser findUserById(int uId){
		AdressBookUser list= null;
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(openHelper.ADRESS_BOOK_USER).append(" where uId = ").append(uId);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list = putAdressBookUser(cursor);
			}
		}
		cursor.close();
		return list;
	}
	Comparator<AdressBookUser> comparator =new Comparator<AdressBookUser>() {
		
		private Collator collator = Collator.getInstance(java.util.Locale.CHINA);
		@Override
		public int compare(AdressBookUser lhs, AdressBookUser rhs) {
			if (lhs == null || rhs == null ||  TextUtils.isEmpty(lhs.getUn()) || TextUtils.isEmpty(rhs.getUn())) {
				return 1;
			}
			return collator.compare(lhs.getUn(), rhs.getUn());
		}
	};
	Comparator<AdressBookUser> comparatorRn =new Comparator<AdressBookUser>() {

		private Collator collator = Collator.getInstance(java.util.Locale.CHINA);
		@Override
		public int compare(AdressBookUser lhs, AdressBookUser rhs) {
			if (lhs == null || rhs == null ||  TextUtils.isEmpty(lhs.getRn()) || TextUtils.isEmpty(rhs.getRn())) {
				return 1;
			}
			return collator.compare(lhs.getRn(), rhs.getRn());
		}
	};
	/**
	 * 查找所有角色
	 * @return
	 */
	public List<AdressBookUser> findAllRoleList(){
		List<AdressBookUser> list=new ArrayList<AdressBookUser>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(openHelper.ADRESS_BOOK_USER).append(" group by rId");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putAdressBookUser(cursor));
			}
		}
		cursor.close();
		Collections.sort(list,comparator);
		return list;
	}
	/**
	 * 查找所有部门
	 */
	public List<AdressBookUser> findAllOrgList(){
		List<AdressBookUser> list = new ArrayList<>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.ADRESS_BOOK_USER).append(" group by oId");
		Cursor cursor = openHelper.query(sql.toString(),null);
		if(cursor.getCount()>0){
			while ((cursor.moveToNext())){
				list.add(putAdressBookUser(cursor));
			}
		}
		cursor.close();
		Collections.sort(list,comparator);
		return list;
	}
	/**
	 * 根据角色ID查找用户
	 * @param roleId 要查找的角色ID
	 * @return
	 */
	public List<AdressBookUser> findUserByRoleId(int roleId){
		StringBuffer sql = new StringBuffer();
		List<AdressBookUser> orgUser=new ArrayList<AdressBookUser>();
		sql.append("select * from ").append(openHelper.ADRESS_BOOK_USER);
		sql.append(" where rId in (").append(roleId).append(")");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				AdressBookUser user = putAdressBookUser(cursor);
				if(SharedPreferencesUtil.getInstance(context).getUserId() != user.getuId()){
					orgUser.add(user);
				}
			}
		}
		cursor.close();
		Collections.sort(orgUser,comparator);
		return orgUser;
	}
	/**
	 * 根据角色ID查找用户
	 * @param roleName 要查找的角色ID
	 * @return
	 */
	public List<AdressBookUser> findUserByRoleName(String roleName){
		StringBuffer sql = new StringBuffer();
		List<AdressBookUser> orgUser=new ArrayList<AdressBookUser>();
		sql.append("select * from ").append(openHelper.ADRESS_BOOK_USER);
		sql.append(" where rn in ('").append(roleName).append("')");
//				.append(" limit 1");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				AdressBookUser user = putAdressBookUser(cursor);
				if(SharedPreferencesUtil.getInstance(context).getUserId() != user.getuId()){
					orgUser.add(user);
				}
			}
		}
		cursor.close();
		Collections.sort(orgUser,comparator);
		return orgUser;
	}
	public List<AdressBookUser> findUserByOId(int oId){
		StringBuffer sql = new StringBuffer();
		List<AdressBookUser> orgUser=new ArrayList<AdressBookUser>();
		sql.append("select * from ").append(openHelper.ADRESS_BOOK_USER);
		sql.append(" where oId in (").append(oId).append(")");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				AdressBookUser user = putAdressBookUser(cursor);
				if(SharedPreferencesUtil.getInstance(context).getUserId() != user.getuId()){
					orgUser.add(user);
				}
			}
		}
		cursor.close();
		Collections.sort(orgUser,comparator);
		return orgUser;
	}
	public boolean isVisitAllByOId(int oId){
		boolean isVistAll= true;
		StringBuffer sql = new StringBuffer();
//		List<AdressBookUser> orgUser=new ArrayList<AdressBookUser>();
		sql.append("select * from ").append(openHelper.ADRESS_BOOK_USER);
		sql.append(" where oId in (").append(oId).append(")");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				AdressBookUser user = putAdressBookUser(cursor);
				if(SharedPreferencesUtil.getInstance(context).getUserId() != user.getuId()){
					if(user.getIsVisit()==0){
						isVistAll = false;
					}
//					orgUser.add(user);
				}
			}
		}
		cursor.close();
		return isVistAll;
	}

	/**
	 * 查找所有参与的人员
	 * @return
	 */
	public List<AdressBookUser> findVisitedUsers(){
		List<AdressBookUser> list = new ArrayList<>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.ADRESS_BOOK_USER);
		sql.append(" where isVisit = 1");
		Cursor cursor = openHelper.query(sql.toString(),null);
		if(cursor.getCount()>0){
			while (cursor.moveToNext()){
				list.add(putAdressBookUser(cursor));
			}
		}
		cursor.close();
		return list;
	}
	/**
	 * 查找每个角色ID的人数
	 * 
	 * @param roleId
	 * @param uId
	 */
	public int findUserNumberByRoleId(int roleId,int uId){
		int count = 0;
		StringBuffer sql = new StringBuffer(" select count(*) from ").append(openHelper.ADRESS_BOOK_USER)
				.append(" where rId = ").append(roleId).append(" and uId <> ").append(uId);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				count = cursor.getInt(0);
			}
		}
		cursor.close();
		return count;
	}
	public int findUserNumberByOId(int oId,int uId){
		int count = 0;
		StringBuffer sql = new StringBuffer(" select count(*) from ").append(openHelper.ADRESS_BOOK_USER)
				.append(" where oId = ").append(oId).append(" and uId <> ").append(uId);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				count = cursor.getInt(0);
			}
		}
		cursor.close();
		return count;
	}
	public void deleteAll(){
		String sql = "delete from "+openHelper.ADRESS_BOOK_USER;
		openHelper.execSQL(sql);
	}

	/**
	 * 如果删除会议参与人，设置user的isVisit=0，unclickable=0,再调用此方法更新数据库
	 * @param user
	 */
	public void updateAdressUser(AdressBookUser user){
		StringBuffer sql = new StringBuffer();
		sql.append(" update ").append(openHelper.ADRESS_BOOK_USER).append(" set isVisit = ").append(user.getIsVisit()).append(" , unClickble = ").append(user.getUnClickble());
		sql.append(" where id = ").append(user.getId());
		openHelper.execSQL(sql.toString());
	}
	public void updateAdressUser(int isVisit,int isUnclickale,int uId){
		StringBuffer sql = new StringBuffer();
		sql.append(" update ").append(openHelper.ADRESS_BOOK_USER).append(" set isVisit = ").append(isVisit).append(" , unClickble = ").append(isUnclickale);
		sql.append(" where uId = ").append(uId);
		openHelper.execSQL(sql.toString());
	}
	
	private ContentValues putContentValues(AdressBookUser attention) {
		ContentValues cv = new ContentValues();
		cv.put("uId", attention.getuId());
		cv.put("rId", attention.getrId());
		cv.put("un", attention.getUn());
		cv.put("pn", attention.getPn());
		cv.put("rn", attention.getRn());
		cv.put("rl", attention.getRl());
		cv.put("ons", attention.getOn());
		cv.put("oc", attention.getOc());
		cv.put("ol", attention.getOl());
		cv.put("oId", attention.getoId());
		cv.put("op", attention.getOp());
		cv.put("isVisit",attention.getIsVisit());
		cv.put("unClickble",attention.getUnClickble());
		cv.put("mailAddr",attention.getMailAddr());
		cv.put("photo",attention.getPhoto());
		cv.put("patch_id",attention.getPatch_id());
		cv.put("ids",attention.getIds());
		cv.put("olevel",attention.getOlevel());
		return cv;
	}
	
	private AdressBookUser putAdressBookUser(Cursor cursor) {
		int i = 0;
		AdressBookUser module = new AdressBookUser();
		module.setId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		module.setuId(cursor.getInt(i++));
		module.setrId(cursor.getInt(i++));
		module.setUn(cursor.getString(i++));
		module.setPn(cursor.getString(i++));
		module.setRn(cursor.getString(i++));
		module.setRl(cursor.getInt(i++));
		module.setOn(cursor.getString(i++));
		module.setOc(cursor.getString(i++));
		module.setOl(cursor.getInt(i++));
		module.setoId(cursor.getInt(i++));
		module.setOp(cursor.getString(i++));
		module.setIsVisit(cursor.getInt(i++));
		module.setUnClickble(cursor.getInt(i++));
		module.setMailAddr(cursor.getString(i++));
		module.setPhoto(cursor.getString(i++));
		module.setPatch_id(cursor.getString(i++));
		module.setIds(cursor.getInt(i++));
		module.setOlevel(cursor.getInt(i++));
		return module;
	}
	
}
