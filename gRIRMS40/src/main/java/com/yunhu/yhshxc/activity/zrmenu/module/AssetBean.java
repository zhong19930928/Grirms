package com.yunhu.yhshxc.activity.zrmenu.module;

import java.io.Serializable;
import java.util.List;

/**
 * @author suhu
 * @data 2017/10/23.
 * @description
 */

public class AssetBean implements Serializable{


    /**
     * flag : 0
     * data : [{"id":"1","name":"1","inserttime":"1","adminid":"1","adminname":"1","zichannum":"1","ypnum":"1","ypzichanid":"\t11","dpnum":"1","dpzichanid":"11","pyzichanid":"1","pynum":"1"}]
     */

    private int flag;
    private List<DataBean> data;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * id : 1
         * name : 1
         * inserttime : 1
         * adminid : 1
         * adminname : 1
         * zichannum : 1
         * ypnum : 1
         * ypzichanid : 	11
         * dpnum : 1
         * dpzichanid : 11
         * pyzichanid : 1
         * pynum : 1
         */

        private int id;
        private String name;
        private String inserttime;
        private String adminid;
        private String adminname;
        private String zichannum;
        private String ypnum;
        private String ypzichanid;
        private String dpnum;
        private String dpzichanid;
        private String pyzichanid;
        private String pynum;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getInserttime() {
            return inserttime;
        }

        public void setInserttime(String inserttime) {
            this.inserttime = inserttime;
        }

        public String getAdminid() {
            return adminid;
        }

        public void setAdminid(String adminid) {
            this.adminid = adminid;
        }

        public String getAdminname() {
            return adminname;
        }

        public void setAdminname(String adminname) {
            this.adminname = adminname;
        }

        public String getZichannum() {
            return zichannum;
        }

        public void setZichannum(String zichannum) {
            this.zichannum = zichannum;
        }

        public String getYpnum() {
            return ypnum;
        }

        public void setYpnum(String ypnum) {
            this.ypnum = ypnum;
        }

        public String getYpzichanid() {
            return ypzichanid;
        }

        public void setYpzichanid(String ypzichanid) {
            this.ypzichanid = ypzichanid;
        }

        public String getDpnum() {
            return dpnum;
        }

        public void setDpnum(String dpnum) {
            this.dpnum = dpnum;
        }

        public String getDpzichanid() {
            return dpzichanid;
        }

        public void setDpzichanid(String dpzichanid) {
            this.dpzichanid = dpzichanid;
        }

        public String getPyzichanid() {
            return pyzichanid;
        }

        public void setPyzichanid(String pyzichanid) {
            this.pyzichanid = pyzichanid;
        }

        public String getPynum() {
            return pynum;
        }

        public void setPynum(String pynum) {
            this.pynum = pynum;
        }
    }
}
