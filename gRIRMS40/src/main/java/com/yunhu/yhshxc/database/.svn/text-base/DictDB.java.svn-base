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

import com.yunhu.yhshxc.activity.carSales.bo.CarSalesProductUnti;
import com.yunhu.yhshxc.bo.Content;
import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.order3.bo.Order3Product;
import com.yunhu.yhshxc.order3.bo.Order3ProductUnti;
import com.yunhu.yhshxc.parser.CacheData;
import com.yunhu.yhshxc.utility.PublicUtils;

/**
 * 
 * @author jishen 数据库操作
 */
public class DictDB {

	private DatabaseHelper openHelper;

	private String TAG = "DictDB";

	public DictDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}

	/**
	 * 动态创建Table
	 * 
	 * @param table
	 * @param columns
	 *            字段数组
	 */
	public void createDictTable(String table, String[] columns) {
		if(!TextUtils.isEmpty(table)){
			SQLiteDatabase db = openHelper.getWritableDatabase();
			StringBuffer sql = new StringBuffer();
			// 删除已存在的table
			sql.append("DROP TABLE IF EXISTS ").append(openHelper.DICT_TABLE)
					.append(table);
			db.execSQL(sql.toString());
			// 创建新的Table
			sql = new StringBuffer();
			sql.append("CREATE TABLE IF NOT EXISTS ")
					.append(openHelper.DICT_TABLE).append(table);
			sql.append("(id INTEGER primary key autoincrement not null");
			for (String col : columns) {
				sql.append(",").append(col).append(" VARCHAR");
			}
//			sql.append(",selectCount").append(" INTEGER");

			sql.append(")");
			db.execSQL(sql.toString());
			
		}
	}

	/**
	 * 为动态table插入动态数据
	 */
	public Long insertDict(List<Content> list, String table) {
		if(!TextUtils.isEmpty(table)){
			SQLiteDatabase db = openHelper.getWritableDatabase();
			ContentValues cv = new ContentValues();
			if (list != null) {
				for (Content content : list) {
					cv.put(content.getKey(), content.getValue());
				}
				Long id = db.insert(openHelper.DICT_TABLE + table, null, cv);
				
				return id;
			} else {
				
			}
		}
		return null;
	}
	
	/**
	 * 为动态table插入动态数据
	 * @return
	 */
	public Long insertDict2(List<Content> list, String table) {
		if(!TextUtils.isEmpty(table)){
			ContentValues cv = new ContentValues();
			if (list != null) {
				for (Content content : list) {
					cv.put(content.getKey(), content.getValue());
				}
				openHelper.delete(openHelper.DICT_TABLE + table, "did =?", new String[] {cv.getAsString("did")});
				Long id = openHelper.insert(openHelper.DICT_TABLE + table, cv);
				
				return id;
			} else {
				
			}
		}
		return null;
	}
	
	/**
	 * 判断表中的数据是否为空
	 * @param table
	 * @return
	 */
	public boolean isEmptyTable(String table){
		String sql = "SELECT * FROM sqlite_master WHERE type=? AND name=?";
		Cursor cursor = openHelper.query(sql, new String[]{"table",openHelper.DICT_TABLE + table});
		int size = cursor.getCount();
		cursor.close();
		if(size > 0){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 删除动态Table
	 * 
	 * @param table
	 */
	public void deleteTable(String table) {
		if(!TextUtils.isEmpty(table)){
			SQLiteDatabase db = openHelper.getWritableDatabase();
			String sql = "DROP TABLE IF EXISTS " + openHelper.DICT_TABLE + table;
//			JLog.d(TAG, sql);
			db.execSQL(sql);
			
		}
	}
	
	/**
	 * 删除动态Table
	 * 
	 * @param table
	 */
	public void deleteTableByidStr(String table,String idstr) {
		if(!TextUtils.isEmpty(idstr) && isEmptyTable(table)){
			openHelper.delete(openHelper.DICT_TABLE + table, "did in ("+idstr+")",null);
		}
	}

	/**
	 * 查询表
	 * @param table 表名
	 * @param dataId 查询列
	 * @param content 值
	 * @return
	 */
	public Dictionary findDictListByTable(String table, String dataId,String content) {
		Dictionary dict = null;
		if(!TextUtils.isEmpty(table)){
			SQLiteDatabase db = openHelper.getReadableDatabase();
			StringBuffer sql=new StringBuffer();
			
			sql.append(" select ").append(dataId).append(" from ").append(openHelper.DICT_TABLE).append(table);
			sql.append(" where did in (").append(content).append(")");
			Cursor cursor=null;
			try{
				 cursor = db.rawQuery(sql.toString(), null);
				}catch(Exception e){
					e.printStackTrace();
				//数据配置异常,返回空结果
					return dict;
				}
	
//			Log.d(TAG, "findAllDictListSqlbyTableName==》" + sql.toString());
	
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					dict = new Dictionary();
					dict.setCtrlCol(cursor.getString(cursor.getColumnIndex(dataId)));
//					dict.setSelectCount(cursor.getInt(cursor.getColumnIndex("selectCount")));

				}
			}
			cursor.close();
			
		}
		return dict;
	}


	/**
	 * 查询级联中相关联的字段
	 * 
	 * @param columns
	 *            table中所有的字段
	 * @param ctrlColumn
	 *            操作字段
	 */
	public String[] findRelatedForeignkey(String columns, String ctrlColumn) {
		List<String> colList = new ArrayList<String>();
		int index = getColumnIndex(ctrlColumn);
		String[] colArr = columns.split(",");
		for (String col : colArr) {
			if (col.contains(CacheData.DATA_N) && index > getColumnIndex(col)) {
				colList.add(col);
			}
		}
		String[] arr = colList.toArray(new String[colList.size()]);
		return arr;
	}

	public int getColumnIndex(String column) {
		String s = column.substring(CacheData.DATA_N.length());
		return Integer.valueOf(s);
	}

	/**
	 * 
	 * @param dictTable 表名
	 * @param dictCtrls 列名
	 * @param dictDataId 关键列
	 * @param queryWhere
	 * @return
	 */
	public List<Dictionary> findDictList(String dictTable,String dictCtrls,String dictDataId,String dictOrderBy,String dictIsAsc, String queryWhere,String fuzzy,String queryWhereForDid) {

		if(TextUtils.isEmpty(dictTable) || TextUtils.isEmpty(dictCtrls) || TextUtils.isEmpty(dictDataId)){
			return null;
		}else{
			String []relatedArr = findRelatedForeignkey(dictCtrls,dictDataId);
			StringBuffer sql = new StringBuffer();
//			sql.append("select ").append(CacheData.DICT_ID).append(",").append(dictDataId).append(" from ").append(openHelper.DICT_TABLE + dictTable).append(" where 1=1");
			sql.append("select * from ").append(openHelper.DICT_TABLE + dictTable).append(" where 1=1");

			if(!TextUtils.isEmpty(queryWhere)){
				String []query = queryWhere.split("@");
				int len = query.length;
				if(query.length == 0 || len > relatedArr.length){
					return null;
				}
				for(int i=1;i<=len;i++){
					sql.append(" and ").append(relatedArr[relatedArr.length-i]).append(" in ('").append(query[len-i]).append("')");
				}
			}
			
			if(!TextUtils.isEmpty(queryWhereForDid)){
				sql.append(" and ").append("did in (").append(queryWhereForDid).append(")");
			}
			
			if(!TextUtils.isEmpty(fuzzy)){
				sql.append(" and ").append(dictDataId).append(" like '%").append(fuzzy).append("%'");
			}
			
			String orderString = dictOrder(dictOrderBy, dictIsAsc);
			if (!TextUtils.isEmpty(orderString)) {//如果有排序列
				sql.append(orderString);
			}
			
			List<Dictionary> list = new ArrayList<Dictionary>();
			List<Dictionary> orderList = new ArrayList<Dictionary>();//没排序的
			List<Dictionary> noOrderList = new ArrayList<Dictionary>();//没排序的
			
			SQLiteDatabase db = openHelper.getReadableDatabase();
			String s = sql.toString();
//			Log.d(TAG, s);	
			
			try {	
				Cursor cursor = db.rawQuery(s, null);
				if(cursor!=null){
					if (cursor.getCount() > 0) {
						Dictionary dict = null;
						while (cursor.moveToNext()) {
							dict = new Dictionary();
							dict.setDid(cursor.getString(cursor.getColumnIndex("did")));
							dict.setCtrlCol(cursor.getString(cursor.getColumnIndex(dictDataId)));
//							dict.setSelectCount(cursor.getInt(cursor.getColumnIndex("selectCount")));
							if (isOrderDic(dictOrderBy, dictIsAsc,cursor)) {
								orderList.add(dict);
							}else{
								noOrderList.add(dict);
							}
						}
					}
					cursor.close();
				}	
								
			} catch (Exception e) {
				
			}
			
			if (!orderList.isEmpty()) {
				list.addAll(orderList);
			}
			if (!noOrderList.isEmpty()) {
				Collections.sort(noOrderList,comparator);
				list.addAll(noOrderList);
			}
//			JLog.d(TAG, "DictionaryList==>"+list.toString());
			
			return list;
		}
	}
	
	private String dictOrder(String dictOrderBy,String dictIsAsc){
		if (TextUtils.isEmpty(dictOrderBy) || TextUtils.isEmpty(dictIsAsc)) {
			return null;
		}
		StringBuffer sql = new StringBuffer();
		String[] order = dictOrderBy.split(",");
		String[] asc = dictIsAsc.split(",");
		if (order.length == asc.length) {
			for (int i = 0; i < order.length; i++) {
				String orderCol = order[i];
				String isAsc = asc[i];
				if (i==0) {
					sql.append(" order by -").append(orderCol+"+0" );
				}else{
					sql.append(" ,-").append(orderCol);
				}
				if (!"2".equals(isAsc)) {
					sql.append(" desc ");
				}else{
					sql.append(" asc ");
				}
			}
		}
		return sql.toString();
	}
	
	private boolean isOrderDic(String dictOrderBy,String dictIsAsc,Cursor cursor){
		boolean flag = false;
		if (TextUtils.isEmpty(dictOrderBy) || TextUtils.isEmpty(dictIsAsc)) {
			return flag;
		}else{
			String[] order = dictOrderBy.split(",");
			String[] asc = dictIsAsc.split(",");
			if (order.length != asc.length) {
				return flag;
			}else{
				for (int i = 0; i < order.length; i++) {
					String orderCol = order[i];
					String orderStr = cursor.getString(cursor.getColumnIndex(orderCol));
					if (!TextUtils.isEmpty(orderStr)) {
						flag = true;
						break;
					}
				}
			}
		}
		return flag;
	}
	
	Comparator<Dictionary> comparator =new Comparator<Dictionary>() {
		
		private Collator collator = Collator.getInstance(java.util.Locale.CHINA);
		@Override
		public int compare(Dictionary lhs, Dictionary rhs) {
			if (lhs == null || rhs == null ||  TextUtils.isEmpty(lhs.getCtrlCol()) || TextUtils.isEmpty(rhs.getCtrlCol())) {
				return 1;
			}
			return collator.compare(lhs.getCtrlCol(), rhs.getCtrlCol());
		}
	};

	
	/**
	 * 
	 * @param dictTable 表名
	 * @param dictCtrls 列名
	 * @param dictDataId 关键列
	 * @param queryWhere
	 * @return
	 */
	public List<Dictionary> findDictList(String dictTable,String dictDataId,String dictOrderBy,String dictIsAsc) {

		if(TextUtils.isEmpty(dictTable) || TextUtils.isEmpty(dictDataId)){
			return null;
		}else{
//			String s = new StringBuffer().append("select ").append(CacheData.DICT_ID).append(",").append(dictDataId).append(" from ").append(openHelper.DICT_TABLE + dictTable).toString();
//			List<Dictionary> list = new ArrayList<Dictionary>();
//			SQLiteDatabase db = openHelper.getReadableDatabase();
//			Log.d(TAG, s);	 
//			Cursor cursor = db.rawQuery(s, null);
//			if(cursor!=null){
//				if (cursor.getCount() > 0) {
//					Dictionary dict = null;
//					while (cursor.moveToNext()) {
//						dict = new Dictionary();
//						dict.setDid(cursor.getString(cursor.getColumnIndex("did")));
//						dict.setCtrlCol(cursor.getString(cursor.getColumnIndex(dictDataId)));
//						list.add(dict);
//					}
//				}
//				cursor.close();
//			}
//			Collections.sort(list,comparator);
//			JLog.d(TAG, "DictionaryList==>"+list.toString());
			
			StringBuffer sql = new StringBuffer();
			sql.append("select ").append(CacheData.DICT_ID).append(",").append(dictDataId).append(" from ").append(openHelper.DICT_TABLE + dictTable);
			String orderString = dictOrder(dictOrderBy, dictIsAsc);
			if (!TextUtils.isEmpty(orderString)) {//如果有排序列
				sql.append(orderString);
			}
			List<Dictionary> list = new ArrayList<Dictionary>();
			List<Dictionary> orderList = new ArrayList<Dictionary>();//没排序的
			List<Dictionary> noOrderList = new ArrayList<Dictionary>();//没排序的
			
			SQLiteDatabase db = openHelper.getReadableDatabase();
//			Log.d(TAG, sql.toString());	 
			Cursor cursor = db.rawQuery(sql.toString(), null);
			if(cursor!=null){
				if (cursor.getCount() > 0) {
					Dictionary dict = null;
					while (cursor.moveToNext()) {
						dict = new Dictionary();
						dict.setDid(cursor.getString(cursor.getColumnIndex("did")));
						dict.setCtrlCol(cursor.getString(cursor.getColumnIndex(dictDataId)));
//						dict.setSelectCount(cursor.getInt(cursor.getColumnIndex("selectCount")));

						if (isOrderDic(dictOrderBy, dictIsAsc,cursor)) {
							orderList.add(dict);
						}else{
							noOrderList.add(dict);
						}
					}
				}
				cursor.close();
			}
			if (!orderList.isEmpty()) {
				list.addAll(orderList);
			}
			if (!noOrderList.isEmpty()) {
				Collections.sort(noOrderList,comparator);
				list.addAll(noOrderList);
			}
//			JLog.d(TAG, "DictionaryList==>"+list.toString());
			return list;
		}
	}
	
	/**
	 * 查找多选下拉框的值
	 * @param idStr did的值
	 * @param ctrlCol 查找列
	 * @param table 表格，名称
	 * @return 查找到的所有的值
	 */
	public String findDictMultiChoiceValueStr(String idStr,String ctrlCol,String table){
		String result = "";
		if(PublicUtils.isIntegerArray(idStr)){
			if(!TextUtils.isEmpty(table)){
				SQLiteDatabase db = openHelper.getReadableDatabase();
				StringBuffer sql = new StringBuffer();
				sql.append("select ").append(ctrlCol).append(" from ").append(openHelper.DICT_TABLE).append(table);
				sql.append(" where ").append(CacheData.DICT_ID).append(" in (").append(idStr).append(")");
				Cursor cursor = db.rawQuery(sql.toString(), null);
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
//				JLog.d(TAG, "findDictMultiChoiceValueStrSQL==>"+sql.toString());
			}
		}
		
//		JLog.d(TAG, "findDictMultiChoiceValueStr=>"+ctrlCol+"/"+result);
		return result;
	}
	
	
	/**
	 * 
	 * @param dictTable 表名
	 * @param dictCtrls 列名
	 * @param dictDataId 关键列
	 * @return
	 */
	public Dictionary findDictByValue(String dictTable,String dictDataId,String fuzzy) {
		Dictionary dict = null;
		StringBuffer sql = new StringBuffer();
		if(!TextUtils.isEmpty(fuzzy)){
			sql.append("select * from ").append(openHelper.DICT_TABLE + dictTable).append(" where 1=1");
			sql.append(" and ").append(dictDataId).append(" = '").append(fuzzy).append("'");
			SQLiteDatabase db = openHelper.getReadableDatabase();
			String s = sql.toString();
//			Log.d(TAG, s);	 
			Cursor cursor = db.rawQuery(s, null);
			if(cursor!=null){
				if (cursor.getCount() > 0) {
					if (cursor.moveToNext()) {
						dict = new Dictionary();
						dict.setDid(cursor.getString(cursor.getColumnIndex("did")));
						dict.setCtrlCol(cursor.getString(cursor.getColumnIndex(dictDataId)));
//						dict.setSelectCount(cursor.getInt(cursor.getColumnIndex("selectCount")));

					}
				}
				cursor.close();
			}
		}
		return dict;
	}
	
	public Dictionary findDictByDid(String dictTable,String dictDataId,String did) {
		Dictionary dict = null;
		StringBuffer sql = new StringBuffer();
		if(!TextUtils.isEmpty(did)){
			sql.append("select * from ").append(openHelper.DICT_TABLE + dictTable);
			sql.append(" where  did = '").append(did).append("'");
			String s = sql.toString();
//			Log.d(TAG, s);	 
			Cursor cursor = openHelper.getReadableDatabase().rawQuery(s, null);
			if(cursor!=null){
				if (cursor.getCount() > 0) {
					if (cursor.moveToNext()) {
						dict = new Dictionary();
						dict.setDid(cursor.getString(cursor.getColumnIndex("did")));
						dict.setCtrlCol(cursor.getString(cursor.getColumnIndex(dictDataId)));
//						dict.setSelectCount(cursor.getInt(cursor.getColumnIndex("selectCount")));

					}
				}
				cursor.close();
			}
		}
		return dict;
	}
	
	public Dictionary findDictByDataValue(String dictTable,String dictDataId,String value) {
		Dictionary dict = null;
		StringBuffer sql = new StringBuffer();
		if(!TextUtils.isEmpty(dictDataId)){
			sql.append("select * from ").append(openHelper.DICT_TABLE + dictTable);
			sql.append(" where ").append(dictDataId).append(" = '").append(value).append("'");
			String s = sql.toString();
//			Log.d(TAG, s);	 
			Cursor cursor = openHelper.getReadableDatabase().rawQuery(s, null);
			if(cursor!=null){
				if (cursor.getCount() > 0) {
					if (cursor.moveToNext()) {
						dict = new Dictionary();
						dict.setDid(cursor.getString(cursor.getColumnIndex("did")));
						dict.setCtrlCol(cursor.getString(cursor.getColumnIndex(dictDataId)));
//						dict.setSelectCount(cursor.getInt(cursor.getColumnIndex("selectCount")));

					}
				}
				cursor.close();
			}
		}
		return dict;
	}
	
	/**
	 * 根据级联中最后一级的did,查出所有级联表的did
	 * @param func
	 * @param did
	 * @return
	 */
	public String[] findRelatedColumnDid(String dictTable,String dictDataId,String columns,String did) {

		if(TextUtils.isEmpty(dictTable) || TextUtils.isEmpty(columns) || TextUtils.isEmpty(dictDataId)){
			return null;
		}
		String[] relatedArr = findRelatedForeignkey(columns, dictDataId);
		StringBuffer sql = new StringBuffer();

		sql.append("select ");
		if(relatedArr != null){
			for (String s : relatedArr) {
				sql.append(s).append(",");
			}
		}
		sql.append("did").append(" from ").append(openHelper.DICT_TABLE + dictTable).append(" where 1=1");
		sql.append(" and ").append("did").append(" = '").append(did).append("'");
		String s = sql.toString();
//		Log.d(TAG, s);
		Cursor cursor = openHelper.getReadableDatabase().rawQuery(s, null);
		String []resArr = null;
		if (cursor != null) {
			if (cursor.moveToNext()) {
				int size = cursor.getColumnCount();
				resArr = new String[size];
				for(int i=0;i<size;i++){
					resArr[i] = cursor.getString(i);
				}
			}
		}
		cursor.close();
		return resArr;
	}
	
	public List<Dictionary> findDictByRelatedColumn(String dictTable,String dictDataId,String[] relatedColums,String []relatedValue) {
		List<Dictionary> list = new ArrayList<Dictionary>();
		Dictionary dict = null;
		StringBuffer sql = new StringBuffer();
		if(relatedColums.length == relatedValue.length){
			sql.append("select * from ").append(openHelper.DICT_TABLE + dictTable).append(" where 1=1");
			int i=0;
			for(String col : relatedColums){
				sql.append(" and ").append(col).append(" = '").append(relatedValue[i]).append("'");
				i++;
			}
			String s = sql.toString();
//			Log.d(TAG, s);	 
			Cursor cursor = openHelper.getReadableDatabase().rawQuery(s, null);
			if(cursor!=null){
				if (cursor.getCount() > 0) {
					while (cursor.moveToNext()) {
						dict = new Dictionary();
						dict.setDid(cursor.getString(cursor.getColumnIndex("did")));
						dict.setCtrlCol(cursor.getString(cursor.getColumnIndex(dictDataId)));
//						dict.setSelectCount(cursor.getInt(cursor.getColumnIndex("selectCount")));

						list.add(dict);
					}
				}
				cursor.close();
			}
		}
		return list;
	}
	
	public void updateDictSelectCount(String table,String did,int count){
		StringBuffer sql = new StringBuffer();
		sql.append(" update ").append(openHelper.DICT_TABLE).append(table).append(" set selectCount = ").append(count);
		sql.append(" where did = '").append(did).append("'");
		openHelper.execSQL(sql.toString());
	}
	
	public List<Order3ProductUnti> findDictByRelatedColumn(String dictTable,String dictUnitId,String priceId,String[] relatedColums,String []relatedValue) {
		List<Order3ProductUnti> list = new ArrayList<Order3ProductUnti>();
		Order3ProductUnti dict = null;
		StringBuffer sql = new StringBuffer();
		if(relatedColums.length == relatedValue.length){
			sql.append("select * from ").append(openHelper.DICT_TABLE + dictTable).append(" where 1=1");
			int i=0;
			for(String col : relatedColums){
				sql.append(" and ").append(col).append(" = '").append(relatedValue[i]).append("'");
				i++;
			}
			String s = sql.toString();
//			Log.d(TAG, s);	 
			Cursor cursor = openHelper.getReadableDatabase().rawQuery(s, null);
			if(cursor!=null){
				if (cursor.getCount() > 0) {
					while (cursor.moveToNext()) {
						dict = new Order3ProductUnti();
						dict.setUnitId(Integer.parseInt(cursor.getString(cursor.getColumnIndex("did"))));
						dict.setUnit(cursor.getString(cursor.getColumnIndex(dictUnitId)));
						dict.setPrice(Double.parseDouble(cursor.getString(cursor.getColumnIndex(priceId))));
						list.add(dict);
					}
				}
				cursor.close();
			}
		}
		return list;
	}
	
	public List<CarSalesProductUnti> findCarSalesProductUntiByRelatedColumn(String dictTable,String dictUnitId,String priceId,String[] relatedColums,String []relatedValue) {
		List<CarSalesProductUnti> list = new ArrayList<CarSalesProductUnti>();
		CarSalesProductUnti dict = null;
		StringBuffer sql = new StringBuffer();
		if(relatedColums.length == relatedValue.length){
			sql.append("select * from ").append(openHelper.DICT_TABLE + dictTable).append(" where 1=1");
			int i=0;
			for(String col : relatedColums){
				sql.append(" and ").append(col).append(" = '").append(relatedValue[i]).append("'");
				i++;
			}
			String s = sql.toString();
//			Log.d(TAG, s);	 
			Cursor cursor = openHelper.getReadableDatabase().rawQuery(s, null);
			if(cursor!=null){
				if (cursor.getCount() > 0) {
					while (cursor.moveToNext()) {
						dict = new CarSalesProductUnti();
						dict.setUnitId(Integer.parseInt(cursor.getString(cursor.getColumnIndex("did"))));
						dict.setUnit(cursor.getString(cursor.getColumnIndex(dictUnitId)));
						dict.setPrice(Double.parseDouble(cursor.getString(cursor.getColumnIndex(priceId))));
						list.add(dict);
					}
				}
				cursor.close();
			}
		}
		return list;
	}
	
	
	/**
	 * 
	 * @param dictTable 表名
	 * @param dictCtrls 列名
	 * @param dictDataId 编码列
	 * @param inOrOut 1是内机 2是外机
	 * @return
	 */
	public Dictionary findDictByValueCheckForGL(String inOrOut,String value) {
		Dictionary dict = null;
		StringBuffer sql = new StringBuffer();
		if(!TextUtils.isEmpty(value)){
			sql.append("select * from ").append(openHelper.DICT_TABLE + "t_m_2008387").append(" where 1=1");
			sql.append(" and data_2018711='").append(value).append("'");
			sql.append(" and data_2018722='").append(inOrOut).append("'");
			SQLiteDatabase db = openHelper.getReadableDatabase();
			String s = sql.toString();
//			Log.d(TAG, s);	 
			Cursor cursor = db.rawQuery(s, null);
			if(cursor!=null){
				if (cursor.getCount() > 0) {
					if (cursor.moveToNext()) {
						dict = new Dictionary();
						dict.setDid(cursor.getString(cursor.getColumnIndex("did")));
						dict.setCtrlCol(cursor.getString(cursor.getColumnIndex("data_2018711")));
					}
				}
				cursor.close();
			}
		}
		return dict;
	}
	
	
	/**
	 * 机型描述
	 * @param value
	 * @return
	 */
	public Dictionary findDictDescriptionCheckForGL(String value){
		Dictionary dict = null;
		StringBuffer sql = new StringBuffer();
		if (!TextUtils.isEmpty(value)) {
			sql.append("select * from ").append(openHelper.DICT_TABLE + "t_m_2008387").append(" where data_2018711 = '").append(value).append("'");
			SQLiteDatabase db = openHelper.getReadableDatabase();
			String s = sql.toString();
//			Log.d(TAG, s);	 
			Cursor cursor = db.rawQuery(s, null);
			if(cursor!=null){
				if (cursor.getCount() > 0) {
					if (cursor.moveToNext()) {
						dict = new Dictionary();
						dict.setDid(cursor.getString(cursor.getColumnIndex("did")));
						dict.setCtrlCol(cursor.getString(cursor.getColumnIndex("data_2018710")));
					}
				}
				cursor.close();
			}
		}
		return dict;
	}
	
	
	/**
	 * 
	 * @param dictTable 表名
	 * @param dictCtrls 列名
	 * @param dictDataId 关键列
	 * @param queryWhere 级联关系
	 * @return
	 */
	public List<Order3Product> findProductDictList(String pTable,String pCtrls,String pDataId,String queryWhere,String fuzzy,String productId) {
		List<Order3Product> list = new ArrayList<Order3Product>();//没排序的
		if(!TextUtils.isEmpty(pTable) && !TextUtils.isEmpty(pCtrls) && !TextUtils.isEmpty(pDataId)){
			String []relatedArr = findRelatedForeignkey(pCtrls,pDataId);
			StringBuffer sql = new StringBuffer();
			sql.append("select * from ").append(openHelper.DICT_TABLE + pTable).append(" where 1=1");

			if(!TextUtils.isEmpty(queryWhere)){
				String []query = queryWhere.split("@");
				int len = query.length;
				if(query.length == 0 || len > relatedArr.length){
					return null;
				}
				for(int i=1;i<=len;i++){
					sql.append(" and ").append(relatedArr[relatedArr.length-i]).append(" in (").append(query[len-i]).append(")");
				}
			}
			
			if(!TextUtils.isEmpty(productId)){
				sql.append(" and ").append("did in (").append(productId).append(")");
			}
			
			if(!TextUtils.isEmpty(fuzzy)){
				sql.append(" and ").append(pDataId).append(" like '%").append(fuzzy).append("%'");
			}
			SQLiteDatabase db = openHelper.getReadableDatabase();
			String s = sql.toString();
//			JLog.d(TAG, s);	 
			Cursor cursor = db.rawQuery(s, null);
			if(cursor!=null){
				if (cursor.getCount() > 0) {
					Order3Product product = null;
					while (cursor.moveToNext()) {
						product = new Order3Product();
						product.setProductId(cursor.getColumnIndex("did"));
						product.setName(cursor.getString(cursor.getColumnIndex(pDataId)));
						list.add(product);
					}
				}
				cursor.close();
			}
		}
		return list;

	}
	
}
