package com.yunhu.yhshxc.database;


import gcg.org.debug.JLog;

import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yunhu.yhshxc.bo.FuncControl;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.PublicUtils;

/**
 * 
 * @author jishen 数据库操作
 */
public class FuncControlDB {
	private String TAG = "FuncControlDB";
	private DatabaseHelper openHelper;

	public FuncControlDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}
	
	/**
	 * 插入一条控件控制记录
	 * @param funcControl
	 * @return
	 */
	public Long insertFuncControl(FuncControl funcControl) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues cv = putContentValues(funcControl);
		Long id = db.insert(openHelper.FUNC_CONTROL_TABLE, null, cv);
		
		return id;
	}

	/**
	 * 查找是否有该控件的记录
	 * @param funcControl
	 * @return true表示有 false 表示没有
	 */
	public boolean isHas(FuncControl funcControl){
		boolean flag=false;
		SQLiteDatabase db = openHelper.getWritableDatabase();
		StringBuffer buffer=new StringBuffer();
		buffer.append(" select * from ").append(openHelper.FUNC_CONTROL_TABLE).append(" where funcId= ").append(funcControl.getFuncId());
		buffer.append(" and targetId= ").append(funcControl.getTargetId());
		buffer.append(" and submitState= ").append(0);
		Cursor cursor = db.rawQuery(buffer.toString(), null);
		if(cursor.getCount()>0){
			flag=true;
		}
		
		cursor.close();
		return flag;
	}
	
	/**
	 * 根据控件ID 和数据ID 更改控件控制记录
	 * @param funcId 控件ID 
	 * @param targetid 数据ID
	 */
	public void updateFuncControlSubmitState(String funcId, int targetid) {
		if(PublicUtils.isInteger(funcId)){
			SQLiteDatabase db = openHelper.getWritableDatabase();
			StringBuffer buffer=new StringBuffer();
			
			buffer.append("update ").append(openHelper.FUNC_CONTROL_TABLE).append(" set submitState= ").append(1).append(" where submitState = ");
			buffer.append(0).append(" and funcId= '").append(funcId).append("'");
			buffer.append(" and targetId= ").append(targetid);
			JLog.d(TAG, "updateFuncControlByFuncIdAndTargetid==>"+buffer.toString());
			db.execSQL(buffer.toString());
		}
	}
	
	/**
	 * 更改控件记录的值
	 * @param funcId 控件ID
	 * @param targetid 数据ID
	 * @param newValue 值
	 */
	public void updateFuncControlValue(int funcId, int targetid,String newValue) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		StringBuffer buffer=new StringBuffer();
		
		buffer.append("update ").append(openHelper.FUNC_CONTROL_TABLE).append(" set value_= '").append(newValue).append("'");
		buffer.append(" where funcId= ").append(funcId);
		buffer.append(" and targetId= ").append(targetid);
		buffer.append(" and submitState= ").append(0);
		JLog.d(TAG, "updateFuncControlValue==>"+buffer.toString());
		db.execSQL(buffer.toString());
		
	}
	
	/**
	 * 根据状态删除控件记录
	 * @param state 状态
	 */
	public void removeFuncControlByState(int state){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		StringBuffer sql=new StringBuffer();
		sql.append("delete from ").append(openHelper.FUNC_CONTROL_TABLE).append(" where submitState = ").append(state);
		db.execSQL(sql.toString());
		
	}
	
	/**
	 * 根据控件ID移除控件控制记录
	 * @param funcId 控件ID
	 */
	public void removeFuncControlByFuncId(int funcId){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		StringBuffer sql=new StringBuffer();
		sql.append("delete from ").append(openHelper.FUNC_CONTROL_TABLE).append(" where funcId = ").append(funcId);
		sql.append(" and submitState= ").append(0);
		db.execSQL(sql.toString());
		
	}
	
	
	
	/**
	 * 
	 * @param targetId 数据ID
	 * @param funcId 控件ID
	 * @param dayNumber 天数
	 */
	public void removeFuncControlByDate(int dayNumber,int targetId,int funcId){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		//获取今天之前dayNumber天的日期
		Calendar c = Calendar.getInstance(); 
        c.add(Calendar.DAY_OF_MONTH, -dayNumber); 
        String updateDate=DateUtil.getStringDate(c);
		db.delete(openHelper.FUNC_CONTROL_TABLE,
						"updateDate<? and targetId=? and funcId=?",
						new String[] {updateDate,targetId+"",funcId+""});
		
	}

	private ContentValues putContentValues(FuncControl func) {
		ContentValues cv = new ContentValues();
		cv.put("funcId", func.getFuncId());
		cv.put("targetId", func.getTargetId());
		cv.put("value_", func.getValues());
		cv.put("updateDate", func.getUpdateDate());
		cv.put("submitState", func.getSubmitState());
		
		return cv;
	}
	
	/**
	 * 
	 * @param targetId 数据ID
	 * @param funcId 控件ID
	 * @param number 次数
	 * @param values 值
	 * @return
	 */
	public boolean isCanSubmit(int targetId,int funcId,int number,String values){//int submitState提交状态1已表示提交0表示未提交
		boolean flag=true;
		SQLiteDatabase db=openHelper.getReadableDatabase();
		StringBuffer buffer=new StringBuffer();
		buffer.append(" select * from ").append(openHelper.FUNC_CONTROL_TABLE).append(" where funcId= ").append(funcId);
		buffer.append(" and targetId= ").append(targetId);
		buffer.append(" and value_= '").append(values).append("'");
		buffer.append(" and submitState = ").append(1);
		Cursor cursor = db.rawQuery(buffer.toString(), null);
		int num=cursor.getCount();
		if(num>=number){
			flag=false;
		}
		cursor.close();
		JLog.d(TAG, "isCanSubmitSql==>"+buffer.toString());
		return flag;
	}
	
	private FuncControl putFuncControl(Cursor cursor) {
		int i = 0;
		FuncControl func = new FuncControl();
		func.setId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		func.setFuncId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		func.setTargetId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		func.setValues(cursor.getString(i++));
		func.setUpdateDate(cursor.getString(i++));
		func.setSubmitState(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		return func;
	}
	
}
