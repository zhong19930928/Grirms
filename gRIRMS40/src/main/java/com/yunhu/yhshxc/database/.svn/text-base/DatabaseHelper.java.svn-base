package com.yunhu.yhshxc.database;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	private final String TAG = "DatabaseHelper";

	private Context context;
	private final static String GrirmsDBName = "GrirmsDB";// 数据库名
	private final static int VERSION = 31;

	public final String MAIN_MENU_TABLE = "MAIN_MENU";// 主页内容
	public final String VISIT_WAY_TABLE = "VISIT_WAY";// 拜访线路
	public final String VISIT_STORE_TABLE = "VISIT_STORE";// 拜访店面
	public final String VISIT_FUNC_TABLE = "VISIT_FUNC";// 访店功能项
	public final String FUNC_TABLE = "FUNC";// 功能项
	public final String SUBMIT_TABLE = "SUBMIT";// 提交数据
	public final String SUBMIT_ITEM_TABLE = "SUBMIT_ITEM";// 提交数据项
	public final String NOTICE_TABLE = "NOTICE";// 公告列表
	public final String TASK_TABLE = "TASK";// 自定义查询列表
	public final String DICT_TABLE = "DICT_";// 字典表
	public final String PUSH_TABLE = "PUSH";
	public final String PUSH_ITEM_TABLE = "PUSH_ITEM";
	public final String ORG_TABLE = "ORG";
	public final String FUNC_CACHE_TABLE="FUNC_CACHE";
	public final String ORG_STORE_TABLE = "ORG_STORE";
	public final String ORG_USER_TABLE = "ORG_USER";
	public final String DOUBLE_TABLE = "DOUBLE_";// 双向表
	public final String MODULE_TABLE = "MODULE";
	public final String FUNC_CONTROL_TABLE = "FUNC_CONTROL";
	public final String ROLE_TABLE = "ROLE";
	public final String QA_TABLE = "QA";
	public final String DOWNLOAD_INFO = "DOWNLOAD_INFO";
	public final String SUBMITITEM_TEMP = "SUBMITITEM_TEMP";// 超链接操作数据临时表
	public final String PSS_CONF_TABLE = "PSS_CONF";
	public final String PSS_PRODUCT_CONF_TABLE = "PSS_PRODUCT_CONF";
	public final String ORDER_CACHE = "ORDER_CACHE";// 订单缓存表
	public final String ORDER_SALE = "ORDER_SALE";// 订单上报表
	public final String TABLE_STENCIL = "TABLE_STENCIL";// 表格模版
	public final String QUERY = "QUERY";// 查询条件存储表
	public final String ORDER_CONTACTS = "ORDER_CONTACTS";// 订单联系人
	public final String NEW_ORDER = "NEW_ORDER";//新订单
	public final String TABLE_PENDING = "TABLE_PENDING";//未提交数据列表
	public final String TO_DO = "TODO";//待办事项
	public final String LOCATION_PENDING = "LOCATION_PENDING";//定位信息
	public final String STYLE = "STYLE";//UI样式
	public final String ADV_PICTURE = "ADV_PICTURE";//轮播图数据表

	public final String ORDER3_PRODUCT_CONF = "ORDER3_PRODUCT_CONF";//产品配置信息
	public final String ORDER3_PROMOTION = "ORDER3_PROMOTION";//促销信息配置
	public final String ORDER3_SHOPPING_CART = "ORDER3_SHOPPING_CART";//购物车
	public final String ORDER3_CONTACTS = "ORDER3_CONTACTS";//订单联系人
	public final String ORDER3_PRODUCT_CTRL = "ORDER3_PRODUCT_CTRL";//产品
	public final String ORDER3_PRODUCT_DATA = "ORDER3_PRODUCT_DATA";//订单数据
	public final String ORDER3 = "ORDER3";//订单数据
	public final String ORDER3_DIS = "ORDER3_DIS";//分销
	
	
	public final String CAR_SALES_CONTACTS = "CAR_SALES_CONTACTS";//车销联系人
	public final String CAR_SALES_PRODUCT_CONF = "CAR_SALES_PRODUCT_CONF";//产品配置信息
	public final String CAR_SALES_PRODUCT_CTRL = "CAR_SALES_PRODUCT_CTRL";//产品控制
	public final String CAR_SALES_PROMOTION = "CAR_SALES_PROMOTION";//促销信息配置
	public final String CAR_SALES_SHOPPING_CART = "CAR_SALES_SHOPPING_CART";//车销购物车
	public final String CAR_SALES_PRODUCT_DATA = "CAR_SALES_PRODUCT_DATA";//订单数据
	public final String CAR_SALES = "CAR_SALES";//订单数据
	public final String CAR_SALES_STOCK = "CAR_SALES_STOCK";//库存
	
	public final String ATTENTION = "ATTENTION";//关注
	public final String REPLY = "REPLY";//回帖
	public final String GROUP = "GROUP_TABLE";//群
	public final String GROUP_ROLE = "GROUP_ROLE";//群中的角色
	public final String GROUP_USER = "GROUP_USER";//群中的用户
	public final String SURVEY = "SURVEY";// 调查和评审
	public final String TOPIC = "TOPIC";// 话题
	public final String TOPIC_NOTIFY = "TOPIC_NOTIFY";// 话题通知
	public final String ZAN = "ZAN";// 点赞
	public final String COMMENT = "COMMENT";// 评论
	public final String NOTIFICATION = "NOTIFICATION";// 通知公告
	public final String PERSONAL_WECHAT = "PERSONAL_WECHAT";//私聊
	public final String ADRESS_BOOK_USER = "ADRESS_BOOK_USER";//通讯录用户
	
	
	public final String ANSWER_OPTIONS = "ANSWER_OPTIONS";//答题选项
	public final String FINDING_DETAIL = "FINDING_DETAIL";//调查结果详细
	public final String FINDINGS = "FINDINGS";//调查结果
	public final String QUESTION = "QUESTION";//问题
	public final String QUESTIONNAIRE = "QUESTIONNAIRE";//调查问卷的整体定义
	public final String SURVEY_ADRESS = "SURVEY_ADRESS";//调查地点
	
	public final String EXAM_ANSWER_OPTIONS = "EXAM_ANSWER_OPTIONS";//在线考试答题选项
	public final String EXAMINATION = "EXAMINATION";//试卷的整体定义
	public final String EXAM_QUESTION = "EXAM_QUESTION";//考试问题
	public final String EXAM_RESULT = "EXAM_RESULT";//考试结果
	public final String EXAM_RESULT_DETAIL = "EXAM_RESULT_DETAIL";//考试结果详细
	
	public final String DOUBLE_TASK_MANAGER = "DOUBLE_TASK_MANAGER";//双向任务控制ID

	public final String IS_LEAVE = "IS_LEAVE";//请假信息表
	public final String AF_LEAVE_INFO = "AF_LEAVE_INFO";//请假信息表
	
	
	public final String PLAN_ASSESS = "PLAN_ASSESS";//工作计划评价
	public final String WORK_PLAN_MODLE = "WORK_PLAN_MODLE";//工作计划详情
	public final String PLAN_DATA = "PLAN_DATA";//工作计划Data

	public final String MAIN_SHIHUA_MENU_TABLE = "MAIN_SHIHUA_MENU";// 石化现场menu



	private static DatabaseHelper mInstance;

	private DatabaseHelper(Context context) {
		super(context, GrirmsDBName, null, VERSION);
		this.context = context;
	}

	public synchronized static DatabaseHelper getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new DatabaseHelper(context);
		}
		return mInstance;
	}

	public synchronized static void destoryInstance() {
		if (mInstance != null) {
			mInstance.close();
		}
	}

	/**
	 * 读取SQL文件，获取SQL语句
	 * 
	 * @param in
	 * @return
	 * @throws Exception
	 */
	private List<String> loadSql() throws Exception {
		List<String> sqlList = new ArrayList<String>();
		try {
			InputStream in = context.getAssets().open("sqlite.sql");
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String s = "";
			StringBuffer sb = new StringBuffer();
			while ((s = br.readLine()) != null) {
				if (TextUtils.isEmpty(s) || s.startsWith("--") || s.startsWith("/*")) {
					continue;
				} else if (s.endsWith(";")) {
					sb.append(s);
					sqlList.add(sb.toString());
					sb.delete(0, sb.length());
				} else {
					sb.append(s);
				}

			}
			in.close();
			br.close();
			for (String sql : sqlList) {
				System.out.println("sql:" + sql);
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return sqlList;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			List<String> sqlList = loadSql();
			for (String sql : sqlList) {
				db.execSQL(sql);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onCreate(db);

	}

	/**
	 * 删除所有表
	 */
	public void deteleAllTable() {
		SQLiteDatabase db = this.getWritableDatabase();
		onCreate(db);
	}

	public void removeAllData(){
		List<String> removeSql = new ArrayList<String>();
		removeSql.add("delete from "+MAIN_MENU_TABLE);
		removeSql.add("delete from "+VISIT_WAY_TABLE);
		removeSql.add("delete from "+VISIT_STORE_TABLE);
		removeSql.add("delete from "+VISIT_FUNC_TABLE);
		removeSql.add("delete from "+FUNC_TABLE);
		removeSql.add("delete from "+NOTICE_TABLE);
		removeSql.add("delete from "+TASK_TABLE);
		removeSql.add("delete from "+ORG_TABLE);
		removeSql.add("delete from "+ORG_STORE_TABLE);
		removeSql.add("delete from "+ORG_USER_TABLE);
		removeSql.add("delete from "+MODULE_TABLE);
		removeSql.add("delete from "+FUNC_CONTROL_TABLE);
		removeSql.add("delete from "+ROLE_TABLE);
		removeSql.add("delete from "+QA_TABLE);
		removeSql.add("delete from "+PSS_CONF_TABLE);
		removeSql.add("delete from "+PSS_PRODUCT_CONF_TABLE);
		removeSql.add("delete from "+ORDER_CACHE);
		removeSql.add("delete from "+ORDER_SALE);
		removeSql.add("delete from "+QUERY);
		removeSql.add("delete from "+ORDER_CONTACTS);
		removeSql.add("delete from "+NEW_ORDER);
		removeSql.add("delete from "+STYLE);

		removeSql.add("delete from "+MAIN_SHIHUA_MENU_TABLE);
		//插入考勤控件
		removeSql.add("insert into FUNC (func_id,targetid,name,type,is_edit)values(-1,-1,'定位',2,1)");
		removeSql.add("insert into FUNC (func_id,targetid,name,type,is_edit,photoTimeType)values(-2,-1,'拍照',1,1,2)");
		removeSql.add("insert into FUNC (func_id,targetid,name,type,length,dataType,is_edit)values(-3,-1,'说明',3,500,5,'1')");
		removeSql.add("insert into FUNC (func_id,targetid,name,type,isSearch,is_edit)values(-5,-2,'用户',19,1,1)");
		removeSql.add("insert into FUNC (func_id,targetid,name,type,isSearch,is_edit)values(-6,-2,'时间',11,1,1)");
		
		SQLiteDatabase db = this.getWritableDatabase();
		for (int i = 0; i < removeSql.size(); i++) {
			String sql = removeSql.get(i);
			db.execSQL(sql);
		}
	}
	// 删除访店
	public void deteleVisit() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(new StringBuffer().append("delete from ")
				.append(VISIT_WAY_TABLE).toString());
		db.execSQL(new StringBuffer().append("delete from ")
				.append(VISIT_STORE_TABLE).toString());
		db.execSQL(new StringBuffer().append("delete from ")
				.append(VISIT_FUNC_TABLE).toString());
	}

	public synchronized int getCount(String Table) {
		Log.d(TAG, "getCount");
		int count = -1;
		Cursor c = getReadableDatabase().query(Table, null, null, null, null,
				null, null);
		if (c.moveToFirst()) {
			count = c.getCount();
		}
		c.close();
		c = null;
		return count;
	}

	/**
	 * 执行一段sql
	 * 
	 * @param sql
	 */
	public synchronized void execSQL(String sql) {
		getWritableDatabase().execSQL(sql);
	}

	/**
	 * 修改
	 * 
	 * @param table
	 *            表格
	 * @param values
	 *            值
	 * @param whereClause
	 *            占位符
	 * @param whereArgs
	 *            条件
	 * @return
	 */
	public synchronized int update(String table, ContentValues values,
			String whereClause, String[] whereArgs) {
		return getWritableDatabase().update(table, values, whereClause,
				whereArgs);
	}

	/**
	 * 插入
	 * 
	 * @param Table
	 * @param values
	 * @return
	 */
	public synchronized long insert(String Table, ContentValues values) {
		return getWritableDatabase().insert(Table, null, values);
	}

	/**
	 * 删除
	 * 
	 * @param table
	 *            表名
	 * @param whereClause
	 *            占位符
	 * @param whereArgs
	 *            条件
	 * @return
	 */
	public synchronized int delete(String table, String whereClause,
			String[] whereArgs) {
		return getWritableDatabase().delete(table, whereClause, whereArgs);
	}

	/**
	 * 查询
	 * 
	 * @param sql
	 * @param selectionArgs
	 * @return
	 */
	public synchronized Cursor query(String sql, String[] selectionArgs) {
		Cursor cursor = getReadableDatabase().rawQuery(sql, selectionArgs);
		return cursor;
	}

	public void beginTransaction() {
		getWritableDatabase().beginTransaction();
	}

	public void endTransaction() {
		getWritableDatabase().setTransactionSuccessful();
		getWritableDatabase().endTransaction();
	}

	public synchronized Cursor query(String table, String[] columns,
			String selection, String[] selectionArgs, String groupBy,
			String having, String orderBy) {
		Cursor cursor = getReadableDatabase().query(table, columns, selection,
				selectionArgs, groupBy, having, orderBy);
		return cursor;
	}
	
	/**
	 * 判断某张表是否存在
	 * 
	 * @param tableName
	 *            表名
	 * @return
	 */
	public  boolean tabbleIsExist(String tableName) {
		boolean result = false;
		if (TextUtils.isEmpty(tableName)) {
			return false;
		}
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = this.getReadableDatabase();
			// 这里表名可以是Sqlite_master
			String sql = "select count(*) as c from "
					+ "sqlite_sequence"
					+ " where name ='" + tableName.trim()
					+ "' ";
			cursor = db.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					result = true;
				}
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	

	
}
