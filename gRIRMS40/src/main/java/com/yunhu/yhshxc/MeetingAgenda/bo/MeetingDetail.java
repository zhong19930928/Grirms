package com.yunhu.yhshxc.MeetingAgenda.bo;

/**
 * Created by xuelinlin on 2017/8/31.
 * 会议详情
 */

public class MeetingDetail {
    private int id;//会议室id
    private int uid;//发起会议人id
    private String uname;//发起会议人名字
    private String meetingName;//会议主题
    private String startTime;//会议开始时间
    private String endTime;//会议结束时间
    private String peopuId;//邀请参加会议人的id
    private String peopName;//邀请参加会议人的用户名
    private String refpeopuId;//拒绝参加会议人的id
    private String refpeopName;//拒绝参加会议人的用户名
    private String accpeopuId;//接受参加会议人的id
    private String accpeopName;//接受参加会议人的用户名
    private String deferpeop;//暂缓参加会议人id

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
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

    public String getPeopuId() {
        return peopuId;
    }

    public void setPeopuId(String peopuId) {
        this.peopuId = peopuId;
    }

    public String getPeopName() {
        return peopName;
    }

    public void setPeopName(String peopName) {
        this.peopName = peopName;
    }

    public String getRefpeopuId() {
        return refpeopuId;
    }

    public void setRefpeopuId(String refpeopuId) {
        this.refpeopuId = refpeopuId;
    }

    public String getRefpeopName() {
        return refpeopName;
    }

    public void setRefpeopName(String refpeopName) {
        this.refpeopName = refpeopName;
    }

    public String getAccpeopuId() {
        return accpeopuId;
    }

    public void setAccpeopuId(String accpeopuId) {
        this.accpeopuId = accpeopuId;
    }

    public String getAccpeopName() {
        return accpeopName;
    }

    public void setAccpeopName(String accpeopName) {
        this.accpeopName = accpeopName;
    }

    public String getDeferpeop() {
        return deferpeop;
    }

    public void setDeferpeop(String deferpeop) {
        this.deferpeop = deferpeop;
    }
}
