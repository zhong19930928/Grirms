package com.yunhu.yhshxc.bo;

/**
 * @author suhu
 * @data 2017/12/15.
 * @description
 */

public class TimeBean {
    private String date;
    private String point;

    public TimeBean() {
    }

    public TimeBean(String date, String point) {
        this.date = date;
        this.point = point;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPiont() {
        return point;
    }

    public void setPiont(String point) {
        this.point = point;
    }
}
