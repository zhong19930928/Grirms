package com.yunhu.yhshxc.MeetingAgenda.bo;

import java.util.List;

/**
 * ＠author zhonghuibin
 * create at 2017/9/13.
 * describe 巡查出的楼id和层id bean类
 */
public class BuildingAndFloor {

    /**
     * data : {"flag":0,"data":[{"id":"2","building":"D","company_id":"10351","thumb":"","floor":[{"id":"1","floor":"2","thumb":""},{"id":"12","floor":"3","thumb":""}]},{"id":"3","building":"H","company_id":"10351","thumb":"","floor":[{"id":"13","floor":"1","thumb":""},{"id":"14","floor":"2","thumb":""},{"id":"15","floor":"3","thumb":""}]},{"id":"5","building":"A","company_id":"10351","thumb":"/Public/floorsdata/floor/2017-08-24/599e424ade3bf.png","floor":null},{"id":"6","building":"V","company_id":"10351","thumb":"/Public/floorsdata/floor/2017-08-24/599e6611ccdc7.png","floor":null},{"id":"7","building":"C","company_id":"10351","thumb":"/Public/floorsdata/floor/2017-08-24/599e65cecc69c.png","floor":null},{"id":"8","building":"F","company_id":"10351","thumb":"","floor":[{"id":"16","floor":"1","thumb":""},{"id":"17","floor":"2","thumb":""},{"id":"18","floor":"3","thumb":""}]}]}
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
         * data : [{"id":"2","building":"D","company_id":"10351","thumb":"","floor":[{"id":"1","floor":"2","thumb":""},{"id":"12","floor":"3","thumb":""}]},{"id":"3","building":"H","company_id":"10351","thumb":"","floor":[{"id":"13","floor":"1","thumb":""},{"id":"14","floor":"2","thumb":""},{"id":"15","floor":"3","thumb":""}]},{"id":"5","building":"A","company_id":"10351","thumb":"/Public/floorsdata/floor/2017-08-24/599e424ade3bf.png","floor":null},{"id":"6","building":"V","company_id":"10351","thumb":"/Public/floorsdata/floor/2017-08-24/599e6611ccdc7.png","floor":null},{"id":"7","building":"C","company_id":"10351","thumb":"/Public/floorsdata/floor/2017-08-24/599e65cecc69c.png","floor":null},{"id":"8","building":"F","company_id":"10351","thumb":"","floor":[{"id":"16","floor":"1","thumb":""},{"id":"17","floor":"2","thumb":""},{"id":"18","floor":"3","thumb":""}]}]
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

        public static class DataBean {
            /**
             * id : 2
             * building : D
             * company_id : 10351
             * thumb :
             * floor : [{"id":"1","floor":"2","thumb":""},{"id":"12","floor":"3","thumb":""}]
             */

            private String id;
            private String building;
            private String company_id;
            private String thumb;
            private List<FloorBean> floor;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getBuilding() {
                return building;
            }

            public void setBuilding(String building) {
                this.building = building;
            }

            public String getCompany_id() {
                return company_id;
            }

            public void setCompany_id(String company_id) {
                this.company_id = company_id;
            }

            public String getThumb() {
                return thumb;
            }

            public void setThumb(String thumb) {
                this.thumb = thumb;
            }

            public List<FloorBean> getFloor() {
                return floor;
            }

            public void setFloor(List<FloorBean> floor) {
                this.floor = floor;
            }

            public static class FloorBean {
                /**
                 * id : 1
                 * floor : 2
                 * thumb :
                 */

                private String id;
                private String floor;
                private String thumb;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getFloor() {
                    return floor;
                }

                public void setFloor(String floor) {
                    this.floor = floor;
                }

                public String getThumb() {
                    return thumb;
                }

                public void setThumb(String thumb) {
                    this.thumb = thumb;
                }
            }
        }
    }
}
