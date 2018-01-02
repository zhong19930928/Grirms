package com.yunhu.yhshxc.MeetingAgenda.bo;

import java.io.Serializable;
import java.util.List;

/**
 * ＠author zhonghuibin
 * create at 2017/9/13.
 * describe 会议室信息bean类
 */
public class MeetingRoomDetail implements Serializable{

    /**
     * data : {"flag":0,"data":[{"id":"18","galleryful":"1","name":"1","buildingid":"21","floorid":"1","infoid":"8,9,10","buildingname":"D","floorname":null,"articles":[{"goodsname":"投影仪","thumb":"http://dev-dddw.mobifox.cn/Public/goodsdata/goods/2017-11-03/59fc0a9fb4220.png"},{"goodsname":"电话会议","thumb":"http://dev-dddw.mobifox.cn/Public/goodsdata/goods/2017-11-03/59fc0a8aedd8e.png"},{"goodsname":"白板","thumb":"http://dev-dddw.mobifox.cn/Public/goodsdata/goods/2017-11-03/59fc0a6fda46a.png"}]},{"id":"19","galleryful":"1","name":"2","buildingid":"21","floorid":"1","infoid":"8,9,10","buildingname":"D","floorname":null,"articles":[{"goodsname":"投影仪","thumb":"http://dev-dddw.mobifox.cn/Public/goodsdata/goods/2017-11-03/59fc0a9fb4220.png"},{"goodsname":"电话会议","thumb":"http://dev-dddw.mobifox.cn/Public/goodsdata/goods/2017-11-03/59fc0a8aedd8e.png"},{"goodsname":"白板","thumb":"http://dev-dddw.mobifox.cn/Public/goodsdata/goods/2017-11-03/59fc0a6fda46a.png"}]}]}
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

    public static class DataBeanX implements Serializable {
        /**
         * flag : 0
         * data : [{"id":"18","galleryful":"1","name":"1","buildingid":"21","floorid":"1","infoid":"8,9,10","buildingname":"D","floorname":null,"articles":[{"goodsname":"投影仪","thumb":"http://dev-dddw.mobifox.cn/Public/goodsdata/goods/2017-11-03/59fc0a9fb4220.png"},{"goodsname":"电话会议","thumb":"http://dev-dddw.mobifox.cn/Public/goodsdata/goods/2017-11-03/59fc0a8aedd8e.png"},{"goodsname":"白板","thumb":"http://dev-dddw.mobifox.cn/Public/goodsdata/goods/2017-11-03/59fc0a6fda46a.png"}]},{"id":"19","galleryful":"1","name":"2","buildingid":"21","floorid":"1","infoid":"8,9,10","buildingname":"D","floorname":null,"articles":[{"goodsname":"投影仪","thumb":"http://dev-dddw.mobifox.cn/Public/goodsdata/goods/2017-11-03/59fc0a9fb4220.png"},{"goodsname":"电话会议","thumb":"http://dev-dddw.mobifox.cn/Public/goodsdata/goods/2017-11-03/59fc0a8aedd8e.png"},{"goodsname":"白板","thumb":"http://dev-dddw.mobifox.cn/Public/goodsdata/goods/2017-11-03/59fc0a6fda46a.png"}]}]
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
             * id : 18
             * galleryful : 1
             * name : 1
             * buildingid : 21
             * floorid : 1
             * infoid : 8,9,10
             * buildingname : D
             * floorname : null
             * articles : [{"goodsname":"投影仪","thumb":"http://dev-dddw.mobifox.cn/Public/goodsdata/goods/2017-11-03/59fc0a9fb4220.png"},{"goodsname":"电话会议","thumb":"http://dev-dddw.mobifox.cn/Public/goodsdata/goods/2017-11-03/59fc0a8aedd8e.png"},{"goodsname":"白板","thumb":"http://dev-dddw.mobifox.cn/Public/goodsdata/goods/2017-11-03/59fc0a6fda46a.png"}]
             */

            private String id;
            private String galleryful;
            private String name;
            private String buildingid;
            private String floorid;
            private String infoid;
            private String buildingname;
            private Object floorname;
            private List<ArticlesBean> articles;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getGalleryful() {
                return galleryful;
            }

            public void setGalleryful(String galleryful) {
                this.galleryful = galleryful;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getBuildingid() {
                return buildingid;
            }

            public void setBuildingid(String buildingid) {
                this.buildingid = buildingid;
            }

            public String getFloorid() {
                return floorid;
            }

            public void setFloorid(String floorid) {
                this.floorid = floorid;
            }

            public String getInfoid() {
                return infoid;
            }

            public void setInfoid(String infoid) {
                this.infoid = infoid;
            }

            public String getBuildingname() {
                return buildingname;
            }

            public void setBuildingname(String buildingname) {
                this.buildingname = buildingname;
            }

            public Object getFloorname() {
                return floorname;
            }

            public void setFloorname(Object floorname) {
                this.floorname = floorname;
            }

            public List<ArticlesBean> getArticles() {
                return articles;
            }

            public void setArticles(List<ArticlesBean> articles) {
                this.articles = articles;
            }

            public static class ArticlesBean implements Serializable {
                /**
                 * goodsname : 投影仪
                 * thumb : http://dev-dddw.mobifox.cn/Public/goodsdata/goods/2017-11-03/59fc0a9fb4220.png
                 */

                private String goodsname;
                private String thumb;

                public String getGoodsname() {
                    return goodsname;
                }

                public void setGoodsname(String goodsname) {
                    this.goodsname = goodsname;
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
