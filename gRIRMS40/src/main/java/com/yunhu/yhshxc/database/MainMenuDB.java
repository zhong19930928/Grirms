package com.yunhu.yhshxc.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.utility.PublicUtils;

/**
 * 
 * @author jishen
 *	数据库操作
 */
public class MainMenuDB {

	private DatabaseHelper openHelper = null;
	
	public MainMenuDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}

	/**
	 * 查找所有menu
	 * @return
	 */
	public List<Menu> findAllMenuList(){
		List<Menu> list = new ArrayList<Menu>();
		StringBuffer  sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.MAIN_MENU_TABLE);
		sql.append(" where type <> ").append(Menu.TYPE_HELP).append(" and type <> ").append(Menu.TYPE_MANAGER).append(" and type <> ").append(Menu.TYPE_TODO)
		
		.append(" and type <> ").append(Menu.TYPE_NEW_ATTENDANCE).append(" and type <> ").append(Menu.TYPE_ATTENDANCE);//2016-May-26 brook   替换新旧考勤到底栏
		sql.append(" order by phoneSerialNo,menu_id");
					
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putMenu(cursor));
			}
		}
		cursor.close();
		return list;
 	}
	
	/**
	 * 查找所有要同步信息的menu
	 * @return
	 */
	public List<Menu> findAllSyncMenuList(){
		List<Menu> list = new ArrayList<Menu>();
		StringBuffer  sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.MAIN_MENU_TABLE);
		sql.append(" where type = ").append(Menu.TYPE_MODULE);
		sql.append(" or type = ").append(Menu.TYPE_NEARBY);
		sql.append(" or type = ").append(Menu.IS_STORE_ADD_MOD);
		sql.append(" or type = ").append(Menu.TYPE_NEW_ATTENDANCE);
		sql.append(" or type = ").append(Menu.TYPE_REPORT);
		sql.append(" or type = ").append(Menu.TYPE_REPORT_NEW);
		sql.append(" or type = ").append(Menu.TYPE_ORDER2);
		sql.append(" or type = ").append(Menu.TYPE_ORDER3);
		sql.append(" or type = ").append(Menu.TYPE_ORDER3_SEND);
		sql.append(" or type = ").append(Menu.TYPE_NOTICE);
		sql.append(" order by phoneSerialNo,menu_id");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putMenu(cursor));
			}
		}
		cursor.close();
		return list;
 	}
	
	public boolean isHasOrderMenu(){
		boolean flag = false;
		StringBuffer  sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.MAIN_MENU_TABLE);
		sql.append(" where type = ").append(Menu.TYPE_ORDER2);
		sql.append(" or type = ").append(Menu.TYPE_ORDER3);
		sql.append(" or type = ").append(Menu.TYPE_ORDER3_SEND);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor.getCount() > 0) {
			flag = true;
		}
		cursor.close();
		return flag;
	}
	
	/**
	 * 根据menuID查找menu
	 * @param menuId
	 * @return
	 */
	public Menu findMenuListByMenuId(int menuId){
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.MAIN_MENU_TABLE).append(" where menu_id=").append(menuId);
		Menu menu = null;
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				menu = putMenu(cursor);
			}
		}
		cursor.close();
		return menu;
 	}

	/**
	 * 根据menuName和menuType查找menu
	 * @param menuId
	 * @return
	 */
	public Menu findMenuListByMenuIdAndType(int menuId,int menuType){
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.MAIN_MENU_TABLE).append(" where menu_id=").append(menuId).append(" and type=").append(menuType);
		Menu menu = null;
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				menu = putMenu(cursor);
			}
		}
		cursor.close();
		return menu;
 	}
	/**
	 * 根据menuID查找menu
	 * @param menuId
	 * @return
	 */
	public Menu findMenuListByMenuType(int type){
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.MAIN_MENU_TABLE).append(" where type=").append(type);
		Menu menu = null;
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				menu = putMenu(cursor);
			}
		}
		cursor.close();
		return menu;
 	}
	
	/**
	 * 插入menu
	 * @param menu
	 * @return
	 */
	public Long insertMenu(Menu menu) {
		ContentValues cv = putContentValues(menu);
		Long id = openHelper.insert(openHelper.MAIN_MENU_TABLE, cv);
		return id;
	}
	
	/**
	 * 更新 根据主键
	 * @param menu
	 * @return
	 */
	public int updateMenuById(Menu menu) {
		ContentValues cv = putContentValues(menu);
		int id = openHelper.update(openHelper.MAIN_MENU_TABLE, cv, "id=?", new String[]{menu.getId()+""});
		return id;
	}
	/**
	 * 更新 根据 menuid
	 * @param menu
	 * @return
	 */
	public int updateMenuByMenuId(Menu menu) {
		ContentValues cv = putContentValues(menu);
		int id = openHelper.update(openHelper.MAIN_MENU_TABLE, cv, "menu_id=?", new String[]{menu.getMenuId()+""});
		return id;
	}
	/**
	 * 根据数据库一个cursor结果,从0开始查询列值,并把值(null或者非null)付给Menu对象返回
	 * @param cursor 一个存有数据的cursor记录
	 * @return
	 */
	private Menu putMenu(Cursor cursor) {
		int i = 0;
		Menu menu = new Menu();
		menu.setId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		menu.setMenuId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		menu.setName(cursor.getString(i++));
		menu.setType(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		menu.setModuleType(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		menu.setBaseTime(cursor.getString(i++));
		menu.setSubmitTime(cursor.getString(i++));
		menu.setPhoneSerialNo(cursor.getInt(i++));
		menu.setIsNoWait(cursor.getInt(i++));
		menu.setPhoneUsableTime(cursor.getString(i++));
		return menu;
	}
	
	private ContentValues putContentValues(Menu menu){
		ContentValues cv = new ContentValues();
		cv.put("menu_id", menu.getMenuId());
		cv.put("name", menu.getName());
		cv.put("type", menu.getType());
		cv.put("module_type", menu.getModuleType());
		cv.put("base_time", menu.getBaseTime());
		cv.put("submitTime", menu.getSubmitTime());
		cv.put("phoneSerialNo", menu.getPhoneSerialNo());
		cv.put("is_no_wait", menu.getIsNoWait());
		cv.put("phoneUsableTime", menu.getPhoneUsableTime());
		return cv;
	}
	
	/**
	 * 删除
	 * @param menuId
	 * @return
	 */
	public int removeMenuByMenuId(int menuId) {
		return openHelper.delete(openHelper.MAIN_MENU_TABLE,"menu_id=?", new String[]{menuId+""});
	}
	
	
	/**
	 * 删除多个
	 * @param menuIdStr
	 */
	public void removeMenu(String menuIdStr) {
		if(PublicUtils.isIntegerArray(menuIdStr)){
			String sql = "delete from "+openHelper.MAIN_MENU_TABLE+" where menu_id in ("+menuIdStr+")";
			openHelper.execSQL(sql);
		}
	}
	
	/**
	 * 删除特定类型的menu 特定的menu
	 * @param menuId 
	 * @param type
	 */
	public void removeMenuByMenuIdAndType(int menuId,int type) {
		openHelper.delete(openHelper.MODULE_TABLE, "menuId=?", new String[]{menuId+""});
		openHelper.delete(openHelper.MAIN_MENU_TABLE,"menu_id=? and type=?", new String[]{menuId+"",type+""});
		
	}
	/**
	 * 根据类型删除menu
	 * @param type 类型
	 * @return
	 */
	public int removeMenuByType(int type) {
		return openHelper.delete(openHelper.MAIN_MENU_TABLE,"type=?", new String[]{type+""});
	}
	/**
	 * 删除所有
	 */
	public void removeAllMenu() {
		String sql = "delete from "+openHelper.MAIN_MENU_TABLE;
		openHelper.execSQL(sql);
	}
	
}
