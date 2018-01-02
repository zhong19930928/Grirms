package com.yunhu.yhshxc.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.bo.ShiHuaMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yh on 2017/6/29.
 * 数据库石化现场menu操作
 */

public class MenuShiHuaDB {
    private DatabaseHelper openHelper = null;

    public MenuShiHuaDB(Context context){
        openHelper = DatabaseHelper.getInstance(context);
    }

    /**
     * 查找所有menu
     * @return
     */
    public List<ShiHuaMenu> findAllMenuList(){
        List<ShiHuaMenu> list = new ArrayList<ShiHuaMenu>();
        StringBuffer  sql=new StringBuffer();
        sql.append("select * from ").append(openHelper.MAIN_SHIHUA_MENU_TABLE);
        Cursor cursor = openHelper.query(sql.toString(), null);
        if (cursor!=null&&cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    ShiHuaMenu shiHuaMenu=new ShiHuaMenu();
                    shiHuaMenu.setFolderName(cursor.getString(
                            cursor.getColumnIndex("folderName")));
                    shiHuaMenu.setIcon(cursor.getString(
                            cursor.getColumnIndex("icon")));
                    shiHuaMenu.setMenuList(listMenu(cursor.getString(
                            cursor.getColumnIndex("list"))));
                    list.add(shiHuaMenu);
                } while (cursor.moveToNext());
            }
        }
        if(cursor!=null){
            cursor.close();
        }
        return list;
    }

    private ArrayList<Menu> listMenu(String jsonContent){
        ArrayList<Menu> list=new ArrayList<>();
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(jsonContent);
            if(jsonArray!=null){
                for (int i=0;i<jsonArray.length();i++){
                    Menu menu=new Menu();
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    if(jsonObject.has("menuName")){
                        menu.setName(jsonObject.getString("menuName"));
                    }
                    if(jsonObject.has("icon")){
                        menu.setIcon(jsonObject.getString("icon"));
                    }
                    if(jsonObject.has("type")){
                        menu.setType(jsonObject.getInt("type"));
                    }
                    if(jsonObject.has("modIds")){
                        ArrayList<String> menuIdList=new ArrayList<>();
                        String modIds=jsonObject.getString("modIds");
                        if(modIds!=null&&!"".equals(modIds)){
                            if(modIds.contains(",")){
                                String[] ss=  modIds.split(",");
                                if(ss!=null&&ss.length>0){
                                    for(int j=0;j<ss.length;j++){
                                        menuIdList.add(ss[j]);
                                    }
                                }
                            }else{
                                menuIdList.add(modIds);
                            }
                        }
                        menu.setMenuIdList(menuIdList);

                    }
                    list.add(menu);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * 插入menu
     *
     * @return
     */
    public Long insertMenu(ContentValues cv) {
        Long id = openHelper.insert(openHelper.MAIN_SHIHUA_MENU_TABLE, cv);
        return id;
    }

}
