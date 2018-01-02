package com.yunhu.yhshxc.MeetingAgenda.bo;

/**
 * Created by xuelinlin on 2017/8/30.
 * 楼层
 */

public class BuildingFloors {
    private int floor;//楼层
    private int id;//层id
    private String thumb;//层的log图片地址

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
