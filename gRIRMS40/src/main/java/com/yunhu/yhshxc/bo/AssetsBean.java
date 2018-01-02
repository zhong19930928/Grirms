package com.yunhu.yhshxc.bo;

import java.io.Serializable;

/**
 * 固定资产信息实例类
 */

public class AssetsBean implements Serializable {

    private String url;//图片路径
    private String urlTip;//给定的url描述

    private String title;//资产名称
    private String titleTip;//给定的title描述

    private String id;//资产id
    private String idTip;//给定的id描述

    private String num;//资产数量
    private String numTip;//给定的num描述

    private String state;//资产状态
    private String stateTip;//状态描述

    private String code;//资产条码
    private String codeTip;//条码描述

    private String sn;//sn
    private String snTip;//sn描述

    private String area;//区域
    private String areaTip;//区域描述

    private String putAddress;//存放地点
    private String putAddressTip;//存放地点描述

    private String useCompany;//使用公司
    private String useCompanyTip;

    private String usePart;//使用部门
    private String usePartTip;

    private String kind;//类别
    private String kindTip;

    private String fineKind;//细分类别
    private String fineKindTip;

    private String proKind;//产品类别
    private String proKindTip;

    private String belongKind;//选择资产分类
    private String belongKindTip;

    private boolean isCheck;//是否已选择

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateTip() {
        return stateTip;
    }

    public void setStateTip(String stateTip) {
        this.stateTip = stateTip;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodeTip() {
        return codeTip;
    }

    public void setCodeTip(String codeTip) {
        this.codeTip = codeTip;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getSnTip() {
        return snTip;
    }

    public void setSnTip(String snTip) {
        this.snTip = snTip;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAreaTip() {
        return areaTip;
    }

    public void setAreaTip(String areaTip) {
        this.areaTip = areaTip;
    }

    public String getPutAddress() {
        return putAddress;
    }

    public void setPutAddress(String putAddress) {
        this.putAddress = putAddress;
    }

    public String getPutAddressTip() {
        return putAddressTip;
    }

    public void setPutAddressTip(String putAddressTip) {
        this.putAddressTip = putAddressTip;
    }

    public String getUseCompany() {
        return useCompany;
    }

    public void setUseCompany(String useCompany) {
        this.useCompany = useCompany;
    }

    public String getUseCompanyTip() {
        return useCompanyTip;
    }

    public void setUseCompanyTip(String useCompanyTip) {
        this.useCompanyTip = useCompanyTip;
    }

    public String getUsePart() {
        return usePart;
    }

    public void setUsePart(String usePart) {
        this.usePart = usePart;
    }

    public String getUsePartTip() {
        return usePartTip;
    }

    public void setUsePartTip(String usePartTip) {
        this.usePartTip = usePartTip;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getKindTip() {
        return kindTip;
    }

    public void setKindTip(String kindTip) {
        this.kindTip = kindTip;
    }

    public String getFineKind() {
        return fineKind;
    }

    public void setFineKind(String fineKind) {
        this.fineKind = fineKind;
    }

    public String getFineKindTip() {
        return fineKindTip;
    }

    public void setFineKindTip(String fineKindTip) {
        this.fineKindTip = fineKindTip;
    }

    public String getProKind() {
        return proKind;
    }

    public void setProKind(String proKind) {
        this.proKind = proKind;
    }

    public String getProKindTip() {
        return proKindTip;
    }

    public void setProKindTip(String proKindTip) {
        this.proKindTip = proKindTip;
    }

    public String getBelongKind() {
        return belongKind;
    }

    public void setBelongKind(String belongKind) {
        this.belongKind = belongKind;
    }

    public String getBelongKindTip() {
        return belongKindTip;
    }

    public void setBelongKindTip(String belongKindTip) {
        this.belongKindTip = belongKindTip;
    }

    public String getUrlTip() {
        return urlTip;
    }

    public void setUrlTip(String urlTip) {
        this.urlTip = urlTip;
    }

    public String getTitleTip() {
        return titleTip;
    }

    public void setTitleTip(String titleTip) {
        this.titleTip = titleTip;
    }

    public String getIdTip() {
        return idTip;
    }

    public void setIdTip(String idTip) {
        this.idTip = idTip;
    }

    public String getNumTip() {
        return numTip;
    }

    public void setNumTip(String numTip) {
        this.numTip = numTip;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
