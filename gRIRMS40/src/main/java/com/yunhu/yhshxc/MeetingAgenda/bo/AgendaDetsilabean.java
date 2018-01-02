package com.yunhu.yhshxc.MeetingAgenda.bo;

/**
 * ＠author zhonghuibin
 * create at 2017/11/1.
 * describe
 */
public class AgendaDetsilabean {

    /**
     * data : {"flag":0,"data":{"id":"66","uid":"6123100","uname":"系统管理员","name":"测试测试","dataday":"2017-11-20 02:17:00~2017-11-20 06:37:00","peopuid":"6123100,6127636,6123528,6123657,","peopname":"资产管理员,roy,011561卓晴怡(集团),侯娟,","refpeopuid":null,"refpeopname":null,"accpeopuid":null,"accpeopname":null,"deferpeopuid":null,"deferpeopname":null,"addres":"访问白宫","remindid":"4","repeatid":"2","remarks":"123456789","send_type":"1","starttime":"2017-11-20 02:17:00","endtime":"2017-11-20 06:37:00"}}
     * info : null
     * status : null
     */

    private DataBeanX data;
    private Object info;
    private Object status;

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public Object getInfo() {
        return info;
    }

    public void setInfo(Object info) {
        this.info = info;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    public static class DataBeanX {
        /**
         * flag : 0
         * data : {"id":"66","uid":"6123100","uname":"系统管理员","name":"测试测试","dataday":"2017-11-20 02:17:00~2017-11-20 06:37:00","peopuid":"6123100,6127636,6123528,6123657,","peopname":"资产管理员,roy,011561卓晴怡(集团),侯娟,","refpeopuid":null,"refpeopname":null,"accpeopuid":null,"accpeopname":null,"deferpeopuid":null,"deferpeopname":null,"addres":"访问白宫","remindid":"4","repeatid":"2","remarks":"123456789","send_type":"1","starttime":"2017-11-20 02:17:00","endtime":"2017-11-20 06:37:00"}
         */

        private int flag;
        private DataBean data;

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * id : 66
             * uid : 6123100
             * uname : 系统管理员
             * name : 测试测试
             * dataday : 2017-11-20 02:17:00~2017-11-20 06:37:00
             * peopuid : 6123100,6127636,6123528,6123657,
             * peopname : 资产管理员,roy,011561卓晴怡(集团),侯娟,
             * refpeopuid : null
             * refpeopname : null
             * accpeopuid : null
             * accpeopname : null
             * deferpeopuid : null
             * deferpeopname : null
             * addres : 访问白宫
             * remindid : 4
             * repeatid : 2
             * remarks : 123456789
             * send_type : 1
             * starttime : 2017-11-20 02:17:00
             * endtime : 2017-11-20 06:37:00
             */

            private String id;
            private String uid;
            private String uname;
            private String name;
            private String dataday;
            private String peopuid;
            private String peopname;
            private Object refpeopuid;
            private Object refpeopname;
            private Object accpeopuid;
            private Object accpeopname;
            private Object deferpeopuid;
            private Object deferpeopname;
            private String addres;
            private String remindid;
            private String repeatid;
            private String remarks;
            private String send_type;
            private String starttime;
            private String endtime;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getUname() {
                return uname;
            }

            public void setUname(String uname) {
                this.uname = uname;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getDataday() {
                return dataday;
            }

            public void setDataday(String dataday) {
                this.dataday = dataday;
            }

            public String getPeopuid() {
                return peopuid;
            }

            public void setPeopuid(String peopuid) {
                this.peopuid = peopuid;
            }

            public String getPeopname() {
                return peopname;
            }

            public void setPeopname(String peopname) {
                this.peopname = peopname;
            }

            public Object getRefpeopuid() {
                return refpeopuid;
            }

            public void setRefpeopuid(Object refpeopuid) {
                this.refpeopuid = refpeopuid;
            }

            public Object getRefpeopname() {
                return refpeopname;
            }

            public void setRefpeopname(Object refpeopname) {
                this.refpeopname = refpeopname;
            }

            public Object getAccpeopuid() {
                return accpeopuid;
            }

            public void setAccpeopuid(Object accpeopuid) {
                this.accpeopuid = accpeopuid;
            }

            public Object getAccpeopname() {
                return accpeopname;
            }

            public void setAccpeopname(Object accpeopname) {
                this.accpeopname = accpeopname;
            }

            public Object getDeferpeopuid() {
                return deferpeopuid;
            }

            public void setDeferpeopuid(Object deferpeopuid) {
                this.deferpeopuid = deferpeopuid;
            }

            public Object getDeferpeopname() {
                return deferpeopname;
            }

            public void setDeferpeopname(Object deferpeopname) {
                this.deferpeopname = deferpeopname;
            }

            public String getAddres() {
                return addres;
            }

            public void setAddres(String addres) {
                this.addres = addres;
            }

            public String getRemindid() {
                return remindid;
            }

            public void setRemindid(String remindid) {
                this.remindid = remindid;
            }

            public String getRepeatid() {
                return repeatid;
            }

            public void setRepeatid(String repeatid) {
                this.repeatid = repeatid;
            }

            public String getRemarks() {
                return remarks;
            }

            public void setRemarks(String remarks) {
                this.remarks = remarks;
            }

            public String getSend_type() {
                return send_type;
            }

            public void setSend_type(String send_type) {
                this.send_type = send_type;
            }

            public String getStarttime() {
                return starttime;
            }

            public void setStarttime(String starttime) {
                this.starttime = starttime;
            }

            public String getEndtime() {
                return endtime;
            }

            public void setEndtime(String endtime) {
                this.endtime = endtime;
            }
        }
    }
}
