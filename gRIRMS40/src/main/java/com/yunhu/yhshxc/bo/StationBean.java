package com.yunhu.yhshxc.bo;

/**
 * Created by YH on 2017/11/1.
 * 工位预定信息
 */

public class StationBean {
    private int id;//预定结果id
    private int gwsnId;//工位id
    private String gwname;//工位名字
    private String startTime;//开始时间2017-11-07 04:02:30
    private String endTime;//结束时间2017-11-07 04:02:30
    private int ydpeId;//预定人id
    private int ygId;//使用人工号
    private int bmId;//部门
    private int louId;//楼id
    private String louName;//楼名字
    private int cengId;//层
    private String cengName;//层名字
    private String ygHaoName;//使用人

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGwsnId() {
        return gwsnId;
    }

    public void setGwsnId(int gwsnId) {
        this.gwsnId = gwsnId;
    }

    public String getGwname() {
        return gwname;
    }

    public void setGwname(String gwname) {
        this.gwname = gwname;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getYdpeId() {
        return ydpeId;
    }

    public void setYdpeId(int ydpeId) {
        this.ydpeId = ydpeId;
    }

    public int getYgId() {
        return ygId;
    }

    public void setYgId(int ygId) {
        this.ygId = ygId;
    }

    public int getBmId() {
        return bmId;
    }

    public void setBmId(int bmId) {
        this.bmId = bmId;
    }

    public int getLouId() {
        return louId;
    }

    public void setLouId(int louId) {
        this.louId = louId;
    }

    public String getLouName() {
        return louName;
    }

    public void setLouName(String louName) {
        this.louName = louName;
    }

    public int getCengId() {
        return cengId;
    }

    public void setCengId(int cengId) {
        this.cengId = cengId;
    }

    public String getCengName() {
        return cengName;
    }

    public void setCengName(String cengName) {
        this.cengName = cengName;
    }

    public String getYgHaoName() {
        return ygHaoName;
    }

    public void setYgHaoName(String ygHaoName) {
        this.ygHaoName = ygHaoName;
    }
}
