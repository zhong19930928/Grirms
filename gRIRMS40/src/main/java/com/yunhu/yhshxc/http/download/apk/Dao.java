package com.yunhu.yhshxc.http.download.apk;
 
 import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yunhu.yhshxc.database.DatabaseHelper;
 
 /**
  * 
  * 一个业务类
  * 处理下载的数据信息
  */
 public class Dao {
     private DatabaseHelper dbHelper;
 
     public Dao(Context context) {
         dbHelper = DatabaseHelper.getInstance(context);
     }
     /**
      * 查看数据库中是否有数据
      */
     public boolean isHasInfors(String md5) {
         SQLiteDatabase database = dbHelper.getReadableDatabase();
         String sql = "select count(*)  from download_info where md5=?";
         Cursor cursor = database.rawQuery(sql, new String[] { md5 });
         cursor.moveToFirst();
         int count = cursor.getInt(0);
         cursor.close();
         return count == 0;
     }
     
 
     /**
      * 保存 下载的具体信息
      */
     public void saveInfos(List<DownloadInfo> infos) {
         SQLiteDatabase database = dbHelper.getWritableDatabase();
         for (DownloadInfo info : infos) {
             String sql = "insert into download_info(thread_id,start_pos, end_pos,compelete_size,url,md5) values (?,?,?,?,?,?)";
             Object[] bindArgs = { info.getThreadId(), info.getStartPos(),
                     info.getEndPos(), info.getCompeleteSize(), info.getUrl(),info.getMd5() };
             database.execSQL(sql, bindArgs);
         }
     }
     /**
      * 保存 下载的具体信息
      */
     public void saveInfo(DownloadInfo info) {
    	 SQLiteDatabase database = dbHelper.getWritableDatabase();
		 String sql = "insert into download_info(thread_id,start_pos, end_pos,compelete_size,url,md5) values (?,?,?,?,?,?)";
		 Object[] bindArgs = { info.getThreadId(), info.getStartPos(),info.getEndPos(), info.getCompeleteSize(), info.getUrl(),info.getMd5()};
		 database.execSQL(sql, bindArgs);
     }
 
     /**
      * 得到下载具体信息
      */
     public List<DownloadInfo> getInfos(String MD5) {
         List<DownloadInfo> list = new ArrayList<DownloadInfo>();
         SQLiteDatabase database = dbHelper.getReadableDatabase();
         String sql = "select thread_id, start_pos, end_pos,compelete_size,url,md5 from download_info where md5=?";
         Cursor cursor = database.rawQuery(sql, new String[] { MD5 });
         while (cursor.moveToNext()) {
             DownloadInfo info = new DownloadInfo(cursor.getInt(0),
                     cursor.getInt(1), cursor.getInt(2), cursor.getInt(3),
                     cursor.getString(4),cursor.getString(5));
             list.add(info);
         }
         cursor.close();
         return list;
     }
     /**
      * 得到下载具体信息
      */
     public DownloadInfo getInfo(String md5) {
    	 SQLiteDatabase database = dbHelper.getReadableDatabase();
    	 String sql = "select thread_id, start_pos, end_pos,compelete_size,url,md5 from download_info where md5=?";
    	 Cursor cursor = database.rawQuery(sql, new String[] { md5 });
    	 DownloadInfo info=null;
    	 while (cursor.moveToNext()) {
    		 info = new DownloadInfo(cursor.getInt(0),
    				 cursor.getInt(1), cursor.getInt(2), cursor.getInt(3),
    				 cursor.getString(4),cursor.getString(5));
    	 }
    	 cursor.close();
    	 return info;
     }
 
     /**
      * 更新数据库中的下载信息
      */
     public void updataInfos(int threadId, int compeleteSize, String md5) {
         SQLiteDatabase database = dbHelper.getReadableDatabase();
         String sql = "update download_info set compelete_size=? where thread_id=? and md5=?";
         Object[] bindArgs = { compeleteSize, threadId, md5 };
         database.execSQL(sql, bindArgs);
     }
//     /**
//      * 关闭数据库
//      */
//     public void closeDb() {
//         dbHelper.close();
//     }
 
     /**
      * 下载完成后删除数据库中的数据
      */
     public void delete(String md5) {
         SQLiteDatabase database = dbHelper.getReadableDatabase();
         database.delete("download_info", "md5=?", new String[] { md5 });
//         database.close();
     }
     /**
      * 下载完成后删除数据库中的数据
      */
     public void delete() {
    	 SQLiteDatabase database = dbHelper.getReadableDatabase();
    	 database.delete("download_info", null,null);
//    	 database.close();
     }
 }