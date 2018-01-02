package com.yunhu.yhshxc.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yunhu.yhshxc.style.SlidePicture;

import java.util.ArrayList;
import java.util.List;

/**
 * 用来存储首页轮播图的信息
 */

public class SlidePictureDB {

    private DatabaseHelper databaseHelper;

    public SlidePictureDB(Context context) {
        this.databaseHelper = DatabaseHelper.getInstance(context);
    }

    /**
     * 插入指定轮播图信息
     * @param slidePicture
     */
    public void insert(SlidePicture slidePicture){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues cvs = putContentValues(slidePicture);
        db.insert(databaseHelper.ADV_PICTURE,null,cvs);

    }

    /**
     * 删除所有
     */
    public void deleteAll(){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete(databaseHelper.ADV_PICTURE,null,null);

    }
    /**
     * 删除指定轮播图信息
     * @param slidePicture
     */
    public void delete(SlidePicture slidePicture){

    }

    /**
     * 查询出数据库所有图片信息
     * @return
     */
    public List<SlidePicture> queryAll(){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + databaseHelper.ADV_PICTURE + " order by sortId asc", null);
        List<SlidePicture> list = new ArrayList<SlidePicture>();
         if (cursor.getCount()>0){
             while (cursor.moveToNext()){
                 list.add(putCursor(cursor));

             }

         }
        cursor.close();

        return list;


    }

    private SlidePicture putCursor(Cursor cursor) {
        SlidePicture sp  = new SlidePicture();
        sp.setSortId(cursor.getInt(cursor.getColumnIndex("sortId")));
        sp.setPictureName(cursor.getString(cursor.getColumnIndex("pictureName")));
        sp.setUrl(cursor.getString(cursor.getColumnIndex("url")));
        sp.setSdPath(cursor.getString(cursor.getColumnIndex("sdPath")));

        return sp;
    }


    /**
     * 存入数据
     * @param slidePicture
     * @return
     */
    private ContentValues putContentValues(SlidePicture slidePicture) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("sortId",slidePicture.getSortId());
        contentValues.put("pictureName",slidePicture.getPictureName());
        contentValues.put("url",slidePicture.getUrl());
        contentValues.put("sdPath",slidePicture.getSdPath());
        return contentValues;
    }


}
