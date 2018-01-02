package com.yunhu.yhshxc.bo;

import java.util.List;

/**
 * ＠author zhonghuibin
 * create at 2017/12/13.
 * describe
 */
public class HomeBean {

    /**
     * data : {"flag":0,"data":{"tab":[{"id":"1","cat_name":"可乐鸡翅"},{"id":"2","cat_name":"可乐"},{"id":"13","cat_name":"娱乐资讯"},{"id":"12","cat_name":"八卦新闻"}],"list":[{"id":"4","title":"八卦来了","cover_thumb":"http://dev-dddw.mobifox.cn/","description":"伊能静","add_time":"1513215193","input_time":"2017-12-14 09:33:13","input_histime":"09:33:13","jumpurl":"http://dev-dddw.mobifox.cn/kupao.php?m=Androidnews&a=getnewsinfo&id=4"}]}}
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
         * data : {"tab":[{"id":"1","cat_name":"可乐鸡翅"},{"id":"2","cat_name":"可乐"},{"id":"13","cat_name":"娱乐资讯"},{"id":"12","cat_name":"八卦新闻"}],"list":[{"id":"4","title":"八卦来了","cover_thumb":"http://dev-dddw.mobifox.cn/","description":"伊能静","add_time":"1513215193","input_time":"2017-12-14 09:33:13","input_histime":"09:33:13","jumpurl":"http://dev-dddw.mobifox.cn/kupao.php?m=Androidnews&a=getnewsinfo&id=4"}]}
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
            private List<TabBean> tab;
            private List<ListBean> list;

            public List<TabBean> getTab() {
                return tab;
            }

            public void setTab(List<TabBean> tab) {
                this.tab = tab;
            }

            public List<ListBean> getList() {
                return list;
            }

            public void setList(List<ListBean> list) {
                this.list = list;
            }

            public static class TabBean {
                /**
                 * id : 1
                 * cat_name : 可乐鸡翅
                 */

                private String id;
                private String cat_name;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getCat_name() {
                    return cat_name;
                }

                public void setCat_name(String cat_name) {
                    this.cat_name = cat_name;
                }
            }

            public static class ListBean {
                /**
                 * id : 4
                 * title : 八卦来了
                 * cover_thumb : http://dev-dddw.mobifox.cn/
                 * description : 伊能静
                 * add_time : 1513215193
                 * input_time : 2017-12-14 09:33:13
                 * input_histime : 09:33:13
                 * jumpurl : http://dev-dddw.mobifox.cn/kupao.php?m=Androidnews&a=getnewsinfo&id=4
                 */

                private String id;
                private String title;
                private String cover_thumb;
                private String description;
                private String add_time;
                private String input_time;
                private String input_histime;
                private String jumpurl;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getCover_thumb() {
                    return cover_thumb;
                }

                public void setCover_thumb(String cover_thumb) {
                    this.cover_thumb = cover_thumb;
                }

                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public String getAdd_time() {
                    return add_time;
                }

                public void setAdd_time(String add_time) {
                    this.add_time = add_time;
                }

                public String getInput_time() {
                    return input_time;
                }

                public void setInput_time(String input_time) {
                    this.input_time = input_time;
                }

                public String getInput_histime() {
                    return input_histime;
                }

                public void setInput_histime(String input_histime) {
                    this.input_histime = input_histime;
                }

                public String getJumpurl() {
                    return jumpurl;
                }

                public void setJumpurl(String jumpurl) {
                    this.jumpurl = jumpurl;
                }
            }
        }
    }
}
