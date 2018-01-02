package com.yunhu.yhshxc.MeetingAgenda.notification;

import java.io.Serializable;

/**
 * Created by suhu on 2017/8/31.
 */

public class Event implements Serializable{


    private String content;

    /**
     * 会议时间
     * 时间格式：yyyy-MM-dd HH:mm:ss
     *
     * */
    private String meetingTime;

    /**
     * 提醒时间
     * 时间格式：yyyy-MM-dd HH:mm:ss
     *
     * */
    private String remindingTime;

    /**
     * 通知ID:管理通知
     *
     * */
    private int notificationID;

    /**
     * 公司id
     * */
    private int companyID;

    /**
     * 用户id
     * */
    private int uID;

    /**
     * 会议id
     * */
    private int meetingID;

    /**
     * 会议类型
     * 2：同意  直接通知（用户不能操作）
     * 3：暂缓  （用户可选择，同意、暂缓、不再通知）
     * 4：拒绝
     * */
    private int meetingType;

    /**
     * 用户名
     * */
    private String uName;

    private int type;//会议日程类型 1会议2日程

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Event() {
    }

    public Event(String content, String meetingTime, String remindingTime , int meetingType) {
        this.content = content;
        this.meetingTime = meetingTime;
        this.remindingTime = remindingTime;
        this.meetingType = meetingType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(String meetingTime) {
        this.meetingTime = meetingTime;
    }

    public String getRemindingTime() {
        return remindingTime;
    }

    public void setRemindingTime(String remindingTime) {
        this.remindingTime = remindingTime;
    }

    public int getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(int notificationID) {
        this.notificationID = notificationID;
    }

    public int getCompanyID() {
        return companyID;
    }

    public void setCompanyID(int companyID) {
        this.companyID = companyID;
    }

    public int getuID() {
        return uID;
    }

    public void setuID(int uID) {
        this.uID = uID;
    }

    public int getMeetingID() {
        return meetingID;
    }

    public void setMeetingID(int meetingID) {
        this.meetingID = meetingID;
    }

    public int getMeetingType() {
        return meetingType;
    }

    public void setMeetingType(int meetingType) {
        this.meetingType = meetingType;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }
}
