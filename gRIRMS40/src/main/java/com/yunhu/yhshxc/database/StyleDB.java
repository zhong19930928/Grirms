package com.yunhu.yhshxc.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.yunhu.yhshxc.style.Style;

/**
 * 
 * @author jishen 数据库操作
 */
public class StyleDB {
	private DatabaseHelper openHelper;

	public StyleDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}


	/*
	 * 插入ToDo
	 */
	public void insertStyle(Style style) {
		
		ContentValues cv = putContentValues(style);
		openHelper.insert(openHelper.STYLE, cv);
	}
	
	public void deleteAllStyle(){
		StringBuffer sql = new StringBuffer();
		sql.append(" delete from ").append(openHelper.STYLE);
		openHelper.execSQL(sql.toString());
	}
	
	public List<Style> findStyleByImageName(String imageName){
		List<Style> list = new ArrayList<Style>();
		String sql = new StringBuffer().append("select * from ")
			.append(openHelper.STYLE).append(" where img_name='")
			.append(imageName).append("'").toString();
		Cursor cursor = openHelper.query(sql, null);
		if (cursor.getCount() > 0) {
			while(cursor.moveToNext()){
				list.add(putStyle(cursor));
			}
		}
		cursor.close();
		return list;
	}
	
	public Style findStyle(int styleType,int moduleId){
		
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(openHelper.STYLE).append(" where style_type=").append(styleType);
		if (moduleId > 0) {
			sql.append(" and module_id=").append(moduleId);
		}
		sql.append(" and type=").append(Style.TYPE_CUSTOM);
		Cursor cursor = openHelper.query(sql.toString(), null);
		
		Style style = null;
		if (cursor.getCount() > 0 && cursor.moveToNext()) {
			style = putStyle(cursor);
		}
		cursor.close();
		
		if(style == null){
			style = findDefaultStyle(styleType,moduleId);
		}
		
		return style;
	}
	
	public Style findDefaultStyle(int styleType,int moduleId){
		
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.STYLE);
		sql.append(" where style_type=").append(styleType);
		if (moduleId > 0) {
			sql.append(" and module_id=").append(moduleId);
		}
		sql.append(" and type=").append(Style.TYPE_DEFAULT);	
		Cursor cursor = openHelper.query(sql.toString(), null);
		
		Style style = null;
		if (cursor.getCount() > 0 && cursor.moveToNext()) {
			style = putStyle(cursor);
		}
		cursor.close();
		
		return style;
	} 
	
	public Style findBGStyle(){
		
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.STYLE);
		sql.append(" where style_type=").append(Style.STYLE_TYPE_BG);
		Cursor cursor = openHelper.query(sql.toString(), null);
		Style style = null;
		if (cursor.getCount() > 0 && cursor.moveToNext()) {
			style = putStyle(cursor);
		}
		cursor.close();
		
		return style;
	} 

	private Style putStyle(Cursor cursor) {
		int i = 0;
		Style style = new Style();
		style.setId(cursor.getInt(i++));
		style.setStyleType(cursor.getInt(i++));
		style.setModuleId(cursor.getInt(i++));
		style.setImgUrl(cursor.getString(i++));
		style.setImgName(cursor.getString(i++));
		style.setBgColor(cursor.getString(i++));
		style.setImgMd5(cursor.getString(i++));
		style.setType(cursor.getInt(i++));
		return style;
	}

	private ContentValues putContentValues(Style style) {
		ContentValues cv = new ContentValues();
		cv.put("style_type", style.getStyleType());
		cv.put("module_id", style.getModuleId());
		cv.put("img_url", style.getImgUrl());
		cv.put("img_name", style.getImgName());
		cv.put("bg_color", style.getBgColor());
		cv.put("img_md5", style.getImgMd5());
		cv.put("type", style.getType());
		return cv;
	}

}
