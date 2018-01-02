package com.yunhu.yhshxc.MeetingAgenda.bo;

/**
 * Created by xuelinlin on 2017/8/30.
 * 大楼
 */

public class BuildingCompany {
    private int id;//楼的id
    private String building;//楼的名字
    private int company_id;//公司id
    private String thumb;//楼Log图片地址

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
