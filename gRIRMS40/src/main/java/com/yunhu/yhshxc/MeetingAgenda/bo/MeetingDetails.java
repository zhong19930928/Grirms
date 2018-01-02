package com.yunhu.yhshxc.MeetingAgenda.bo;

import java.util.ArrayList;
import java.util.List;

/**
 * ＠author zhonghuibin
 * create at 2017/10/31.
 * describe 会议室详情界面数据
 */
public class MeetingDetails {

    /**
     * data : {"flag":0,"data":{"id":"102","uid":"6123278","uname":"部门管理员1","meetingname":"The only ","starttime":"09:00","endtime":"10:00","peopuid":"101,102,103","peopname":"张三,李四,王五","refpeopuid":null,"refpeopname":null,"accpeopuid":null,"accpeopname":null,"deferpeopuid":null,"deferpeopname":null,"serveid":"9,8,12,10","roomname":"第三会议室","conferenceid":"20","floorid":"29","buildingid":"22","floorname":"1","buildingname":"H","remindid":"2","repeatid":"2","remarks":"Jdjjdjd","send_type":"1","goodsid":null,"service":[{"servicename":"茶杯"},{"servicename":"开水"},{"servicename":"桌盘"},{"servicename":"标语"}],"goods":[],"galleryful":"15"}}
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
         * data : {"id":"102","uid":"6123278","uname":"部门管理员1","meetingname":"The only ","starttime":"09:00","endtime":"10:00","peopuid":"101,102,103","peopname":"张三,李四,王五","refpeopuid":null,"refpeopname":null,"accpeopuid":null,"accpeopname":null,"deferpeopuid":null,"deferpeopname":null,"serveid":"9,8,12,10","roomname":"第三会议室","conferenceid":"20","floorid":"29","buildingid":"22","floorname":"1","buildingname":"H","remindid":"2","repeatid":"2","remarks":"Jdjjdjd","send_type":"1","goodsid":null,"service":[{"servicename":"茶杯"},{"servicename":"开水"},{"servicename":"桌盘"},{"servicename":"标语"}],"goods":[],"galleryful":"15"}
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
             * id : 102
             * uid : 6123278
             * uname : 部门管理员1
             * meetingname : The only
             * starttime : 09:00
             * endtime : 10:00
             * peopuid : 101,102,103
             * peopname : 张三,李四,王五
             * refpeopuid : null
             * refpeopname : null
             * accpeopuid : null
             * accpeopname : null
             * deferpeopuid : null
             * deferpeopname : null
             * serveid : 9,8,12,10
             * roomname : 第三会议室
             * conferenceid : 20
             * floorid : 29
             * buildingid : 22
             * floorname : 1
             * buildingname : H
             * remindid : 2
             * repeatid : 2
             * remarks : Jdjjdjd
             * send_type : 1
             * goodsid : null
             * service : [{"servicename":"茶杯"},{"servicename":"开水"},{"servicename":"桌盘"},{"servicename":"标语"}]
             * goods : []
             * galleryful : 15
             */

            private String id;
            private String uid;
            private String uname;
            private String meetingname;
            private String starttime;
            private String endtime;
            private String peopuid = "";
            private String peopname = "";
            private Object refpeopuid;
            private Object refpeopname;
            private Object accpeopuid;
            private Object accpeopname;
            private Object deferpeopuid;
            private Object deferpeopname;
            private String serveid;
            private String roomname;
            private String conferenceid;
            private String floorid;
            private String buildingid;
            private String floorname;
            private String buildingname;
            private String remindid;
            private String repeatid;
            private String remarks;
            private String send_type;
            private Object goodsid;
            private String galleryful;
            private List<ServiceBean> service = new ArrayList<>();
            private List<?> goods;

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

            public String getMeetingname() {
                return meetingname;
            }

            public void setMeetingname(String meetingname) {
                this.meetingname = meetingname;
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

            public String getServeid() {
                return serveid;
            }

            public void setServeid(String serveid) {
                this.serveid = serveid;
            }

            public String getRoomname() {
                return roomname;
            }

            public void setRoomname(String roomname) {
                this.roomname = roomname;
            }

            public String getConferenceid() {
                return conferenceid;
            }

            public void setConferenceid(String conferenceid) {
                this.conferenceid = conferenceid;
            }

            public String getFloorid() {
                return floorid;
            }

            public void setFloorid(String floorid) {
                this.floorid = floorid;
            }

            public String getBuildingid() {
                return buildingid;
            }

            public void setBuildingid(String buildingid) {
                this.buildingid = buildingid;
            }

            public String getFloorname() {
                return floorname;
            }

            public void setFloorname(String floorname) {
                this.floorname = floorname;
            }

            public String getBuildingname() {
                return buildingname;
            }

            public void setBuildingname(String buildingname) {
                this.buildingname = buildingname;
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

            public Object getGoodsid() {
                return goodsid;
            }

            public void setGoodsid(Object goodsid) {
                this.goodsid = goodsid;
            }

            public String getGalleryful() {
                return galleryful;
            }

            public void setGalleryful(String galleryful) {
                this.galleryful = galleryful;
            }

            public List<ServiceBean> getService() {
                return service;
            }

            public void setService(List<ServiceBean> service) {
                this.service = service;
            }

            public List<?> getGoods() {
                return goods;
            }

            public void setGoods(List<?> goods) {
                this.goods = goods;
            }

            public static class ServiceBean {
                /**
                 * servicename : 茶杯
                 */

                private String servicename;

                public String getServicename() {
                    return servicename;
                }

                public void setServicename(String servicename) {
                    this.servicename = servicename;
                }
            }
        }
    }
}
