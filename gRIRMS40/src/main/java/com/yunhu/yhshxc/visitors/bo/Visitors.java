package com.yunhu.yhshxc.visitors.bo;

/**
 * Created by xuelinlin on 2017/12/5.
 */

public class Visitors {
    private String taskid;
    private String patchid;
    private String status;
    private String status_name;
    private String authuser;
    private String lfdata;//来访日期
    private String lftime;//来访时间
    private String lfdw;//来访人单位
    private String lfsy;//来访事由
    private String lfr;//来访人
    private String openID;//openID

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getPatchid() {
        return patchid;
    }

    public void setPatchid(String patchid) {
        this.patchid = patchid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    public String getAuthuser() {
        return authuser;
    }

    public void setAuthuser(String authuser) {
        this.authuser = authuser;
    }

    public String getLfdata() {
        return lfdata;
    }

    public void setLfdata(String lfdata) {
        this.lfdata = lfdata;
    }

    public String getLftime() {
        return lftime;
    }

    public void setLftime(String lftime) {
        this.lftime = lftime;
    }

    public String getLfdw() {
        return lfdw;
    }

    public void setLfdw(String lfdw) {
        this.lfdw = lfdw;
    }

    public String getLfsy() {
        return lfsy;
    }

    public void setLfsy(String lfsy) {
        this.lfsy = lfsy;
    }

    public String getLfr() {
        return lfr;
    }

    public void setLfr(String lfr) {
        this.lfr = lfr;
    }

    public String getOpenID() {
        return openID;
    }

    public void setOpenID(String openID) {
        this.openID = openID;
    }
}
