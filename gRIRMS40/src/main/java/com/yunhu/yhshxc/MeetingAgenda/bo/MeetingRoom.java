package com.yunhu.yhshxc.MeetingAgenda.bo;

/**
 * Created by xuelinlin on 2017/8/30.
 * 会议室
 */

public class MeetingRoom {
    private int id;//会议室id
    private int galleryful;//可容纳人数
    private String name;//会议室名字
    private int buildingId;//楼ID
    private String buildingName;//楼名
    private int floorId;//层id
    private String floorName;//第几次
    private String infoId;//用品id字符串
    private String recommend;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGalleryful() {
        return galleryful;
    }

    public void setGalleryful(int galleryful) {
        this.galleryful = galleryful;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(int buildingId) {
        this.buildingId = buildingId;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public int getFloorId() {
        return floorId;
    }

    public void setFloorId(int floorId) {
        this.floorId = floorId;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public String getInfoId() {
        return infoId;
    }

    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }
}
