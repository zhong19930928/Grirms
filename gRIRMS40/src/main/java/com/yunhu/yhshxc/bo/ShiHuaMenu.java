package com.yunhu.yhshxc.bo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by YH on 2017/6/29.
 * 石化现场menu
 */

public class ShiHuaMenu implements Serializable{
    private String folderName;//menu名称
    private String icon;//menu的icon
    private int isemptymodle;//是否是空模块 1：是 0 ：不是
    private ArrayList<Menu> menuList;//menu集合

    public int getIsemptymodle() {
        return isemptymodle;
    }

    public void setIsemptymodle(int isemptymodle) {
        this.isemptymodle = isemptymodle;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public ArrayList<Menu> getMenuList() {
        return menuList;
    }

    public void setMenuList(ArrayList<Menu> menuList) {
        this.menuList = menuList;
    }
}
