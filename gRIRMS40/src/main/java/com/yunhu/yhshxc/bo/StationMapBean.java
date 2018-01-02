package com.yunhu.yhshxc.bo;

/**
 * Created by YH on 2017/10/16.
 * 工位详细信息
 */

public class StationMapBean {
    private int id;//座位id
    private String gwsn;//工位码
    private int company_id;//公司id
    private String ygid;//员工id
    private String ygname;//员工名字
    private int louid;//楼id
    private int cengid;//层id
    private String bmid;//部门id
    private String glyid;//管理员id
    private int gwstye;//工位类型是1移动还是2固定
    private int fx;//方向北4东5南6西7
    private int fpstate;//分配状态
    private int systate;//使用状态
    private int xNum;
    private  int Ynum;
    private int gwxh;//工位型号 正常工位 l工位  1标准工位 2 l工位
    private int gwdwd;//表示的定位点
    private String login_id;//登陆号   员工号
    private String bmname;//部门名称

    public String getLogin_id() {
        return login_id;
    }

    public void setLogin_id(String login_id) {
        this.login_id = login_id;
    }

    public String getBmname() {
        return bmname;
    }

    public void setBmname(String bmname) {
        this.bmname = bmname;
    }

    public int getGwdwd() {
        return gwdwd;
    }

    public void setGwdwd(int gwdwd) {
        this.gwdwd = gwdwd;
    }

    public int getGwxh() {
        return gwxh;
    }

    public void setGwxh(int gwxh) {
        this.gwxh = gwxh;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGwsn() {
        return gwsn;
    }

    public void setGwsn(String gwsn) {
        this.gwsn = gwsn;
    }

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public String getYgid() {
        return ygid;
    }

    public void setYgid(String ygid) {
        this.ygid = ygid;
    }

    public String getYgname() {
        return ygname;
    }

    public void setYgname(String ygname) {
        this.ygname = ygname;
    }

    public int getLouid() {
        return louid;
    }

    public void setLouid(int louid) {
        this.louid = louid;
    }

    public int getCengid() {
        return cengid;
    }

    public void setCengid(int cengid) {
        this.cengid = cengid;
    }

    public String getBmid() {
        return bmid;
    }

    public void setBmid(String bmid) {
        this.bmid = bmid;
    }

    public String getGlyid() {
        return glyid;
    }

    public void setGlyid(String glyid) {
        this.glyid = glyid;
    }

    public int getGwstye() {
        return gwstye;
    }

    public void setGwstye(int gwstye) {
        this.gwstye = gwstye;
    }

    public int getFx() {
        return fx;
    }

    public void setFx(int fx) {
        this.fx = fx;
    }

    public int getFpstate() {
        return fpstate;
    }

    public void setFpstate(int fpstate) {
        this.fpstate = fpstate;
    }

    public int getSystate() {
        return systate;
    }

    public void setSystate(int systate) {
        this.systate = systate;
    }

    public int getxNum() {
        return xNum;
    }

    public void setxNum(int xNum) {
        this.xNum = xNum;
    }

    public int getYnum() {
        return Ynum;
    }

    public void setYnum(int ynum) {
        Ynum = ynum;
    }
}
