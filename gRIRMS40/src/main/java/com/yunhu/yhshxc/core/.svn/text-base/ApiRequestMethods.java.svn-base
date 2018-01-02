package com.yunhu.yhshxc.core;

import android.content.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by suhu on 2017/4/8.
 */

public class ApiRequestMethods {

    /**
     *@method 根据公司id查询出楼和楼层
     *@author suhu
     *@time 2017/8/31 16:23
     *@param id 公司id
     *
    */
    public static void getCompanyList(Context context, String url, int id  , final ApiRequestFactory.HttpCallBackListener httpCallBackListener, boolean isShowDialog){
        Map<String ,String> map = new HashMap<>();
        map.put("company_id",id+"");
        ApiRequestFactory.postJson(context,url,map,httpCallBackListener,isShowDialog);
    }

    /**
     *@method 会议日程列表
     *@author lou
     *@time 2017/9/5 16:23
     *@param
     *
     */
    public static void getMeetingschedulelist(Context context, String url, int company_id  ,int uid  , int page  ,  final ApiRequestFactory.HttpCallBackListener httpCallBackListener, boolean isShowDialog){
        Map<String ,String> map = new HashMap<>();
        map.put("company_id",company_id+"");
        map.put("uid",uid+"");
        map.put("page",page+"");
        ApiRequestFactory.postJson(context,url,map,httpCallBackListener,isShowDialog);
    }


    /**
     *@method 用户确定是否参加会议
     *@author lou
     *@time 2017/9/7
     *@param
     *
     */
    public static void confirmmeeting(Context context, int company_id,int uid,String uname,int huiyiid,int huiyitype,int typeid,String url,  final ApiRequestFactory.HttpCallBackListener httpCallBackListener, boolean isShowDialog){
        Map<String ,String> map = new HashMap<>();
        map.put("company_id",company_id+"");
        map.put("uid",uid+"");
        map.put("uname",uname);
        map.put("huiyiid",huiyiid+"");
        map.put("huiyitype",huiyitype+"");
        map.put("typeid",typeid+"");
        ApiRequestFactory.postJson(context,url,map,httpCallBackListener,isShowDialog);
    }


    /**
     *@method 用户确定是否参加会议
     *@author suhu
     *@time 2017/10/23
     *@param
     *
     */
    public static void checkList(Context context, int company_id,int uid,int page,final ApiRequestFactory.HttpCallBackListener httpCallBackListener, boolean isShowDialog){
        Map<String ,String> map = new HashMap<>(4);
        map.put("company_id",company_id+"");
        map.put("uid",uid+"");
        map.put("page",page+"");
        ApiRequestFactory.postJson(context,ApiUrl.CHECKLIST,map,httpCallBackListener,isShowDialog);
    }


    /**
     *@method 提交盘点单
     *@author suhu
     *@time 2017/10/24
     *@param id 盘点单id
     *
     */
    public static void submitCheck(Context context, int id,final ApiRequestFactory.HttpCallBackListener httpCallBackListener, boolean isShowDialog){
        Map<String ,String> map = new HashMap<>(2);
        map.put("id",id+"");
        ApiRequestFactory.postJson(context,ApiUrl.SUBMIT_CHECK,map,httpCallBackListener,isShowDialog);
    }


    /**
     *@method 删除盘点单
     *@author suhu
     *@time 2017/10/24
     *@param id 盘点单id
     *
     */
    public static void deleteCheck(Context context, int id,final ApiRequestFactory.HttpCallBackListener httpCallBackListener, boolean isShowDialog){
        Map<String ,String> map = new HashMap<>(2);
        map.put("id",id+"");
        ApiRequestFactory.postJson(context,ApiUrl.DELETE_CHECK,map,httpCallBackListener,isShowDialog);
    }

    /**
     * @method 获取预定工位列表
     * @author lou
     * @time 2017/11/06
     */
    public static void getStationList(Context context, int id, int company_id, int bmid, int page,final ApiRequestFactory.HttpCallBackListener httpCallBackListener, boolean isShowDialog) {
        Map<String, String> map = new HashMap<>();
        if(id>=0){
            map.put("uid", id + "");
        }
        map.put("company_id", company_id + "");
        map.put("page", page + "");
        if (bmid >=0) {
            map.put("bmid", bmid + "");
        }
        ApiRequestFactory.postJson(context, ApiUrl.MYGONGWEILIST, map, httpCallBackListener, isShowDialog);
    }


    /**
     * @method 释放工位
     * @author lou
     * @time 2017/11/06
     */
    public static void deleteGongwei(Context context, int id, int company_id, final ApiRequestFactory.HttpCallBackListener httpCallBackListener, boolean isShowDialog) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id + "");
        map.put("company_id", company_id + "");
        ApiRequestFactory.postJson(context, ApiUrl.DELETEGONGWEI, map, httpCallBackListener, isShowDialog);
    }


    /**
     * @method 预定工位
     * @author lou
     * @time 2017/11/06
     */
    public static void reserveGongwei(Context context, String gwsn, String starttime, String endtime, String ydpeo, String ygid, String bmid, String louid, String cengid, String ydpeoname, String yghaoname
            , String data_org, String data_org_new, String data_role, String data_auth_user, int company_id, final ApiRequestFactory.HttpCallBackListener httpCallBackListener, boolean isShowDialog) {
        Map<String, String> map = new HashMap<>();
        map.put("company_id", company_id + "");
        map.put("gwsn", gwsn);
        map.put("starttime", starttime);
        map.put("endtime", endtime);
        map.put("ydpeo", ydpeo);
        map.put("ygid", ygid);
        map.put("bmid", bmid);
        map.put("louid", louid);
        map.put("cengid", cengid);
        map.put("ydpeoname", ydpeoname);
        map.put("yghaoname", yghaoname);
        map.put("data_org", data_org);
        map.put("data_org_new", data_org_new);
        map.put("data_role", data_role);
        map.put("data_auth_user", data_auth_user);
        ApiRequestFactory.postJson(context, ApiUrl.RESERVEGONGWEI, map, httpCallBackListener, isShowDialog);
    }


    /**
     * @method 根据扫描回的工位编码和时间段查询当前工位使用状态
     * @author lou
     * @time 2017/11/06
     * time 2017-01-01 14：14：14
     */
    public static void getCodeTimeState(Context context, String gwsn, String starttime, String endtime, int company_id, final ApiRequestFactory.HttpCallBackListener httpCallBackListener, boolean isShowDialog) {
        Map<String, String> map = new HashMap<>();
        map.put("company_id", company_id + "");
        map.put("gwsn", gwsn);
        map.put("starttime", starttime);
        map.put("endtime", endtime);
        ApiRequestFactory.postJson(context, ApiUrl.GETCODETIMESTATE, map, httpCallBackListener, isShowDialog);
    }

    /**
     * @method 根据时间段和楼号楼层查询该楼层所有座位信息及状态
     * @author lou
     * @time 2017/11/06
     * time 2017-01-01 14：14：14
     */
    public static void gongWeiInfoState(Context context, String louid, String cengid, String starttime, String endtime, int company_id, final ApiRequestFactory.HttpCallBackListener httpCallBackListener, boolean isShowDialog) {
        Map<String, String> map = new HashMap<>();
        map.put("company_id", company_id + "");
        map.put("louid", louid);
        map.put("cengid", cengid);
        map.put("starttime", starttime);
        map.put("endtime", endtime);
        ApiRequestFactory.postJson(context, ApiUrl.GONGWEIYINFOSTATE, map, httpCallBackListener, isShowDialog);
    }


    /**
     *@method 上报盘盈界面
     *@author suhu
     *@time 2017/11/1 9:28
     *
    */
    public static void checkInfoList(Context context, List<SubmitFile> submitFiles, Map<String,String> params, final ApiRequestFactory.HttpCallBackListener httpCallBackListener, boolean isShowDialog){
        ApiRequestFactory.postMixture(context,ApiUrl.CHECK_INFOLIST,submitFiles,params,httpCallBackListener,isShowDialog);
    }

    /**
     *@method 盘点的盘盈
     *@author suhu
     *@time 2017/11/1 16:33
     *
     */
    public static void CheckPanYing(Context context, String pdId,int company_id,int uid,final ApiRequestFactory.HttpCallBackListener httpCallBackListener, boolean isShowDialog){
        Map<String ,String> map = new HashMap<>(8);
        map.put("pdid",pdId);
        map.put("company_id",company_id+"");
        map.put("uid",uid+"");
        ApiRequestFactory.postJson(context,ApiUrl.CHECK_PANYING,map,httpCallBackListener,isShowDialog);
    }

  /**
     * @method 根据时间段和楼信息查询各层的剩余未预定座位
     * @author lou
     * @time 2017/11/06
     * time 2017-01-01 14：14：14
     */
    public static void selectGongwei(Context context, String louid, String starttime, String endtime, int company_id, final ApiRequestFactory.HttpCallBackListener httpCallBackListener, boolean isShowDialog) {
        Map<String, String> map = new HashMap<>();
        map.put("company_id", company_id + "");
        map.put("louid", louid);
        map.put("starttime", starttime);
        map.put("endtime", endtime);
        ApiRequestFactory.postJson(context, ApiUrl.SELECTGONGWEI, map, httpCallBackListener, isShowDialog);
    }

    /**
     * @method 修改工位使用人
     * @author lou
     * @time 2017/11/06
     * time 2017-01-01 14：14：14
     */
    public static void updateUserInfo(Context context, String ygid, String yghaoname, String update_user, int company_id, int id,String bmid ,final ApiRequestFactory.HttpCallBackListener httpCallBackListener, boolean isShowDialog) {
        Map<String, String> map = new HashMap<>();
        map.put("company_id", company_id + "");
        map.put("id", id + "");
        map.put("yghaoname", yghaoname);
        map.put("ygid", ygid);
        map.put("update_user", update_user);
        map.put("bmid", bmid);
        ApiRequestFactory.postJson(context, ApiUrl.UPDATEUSERINFO, map, httpCallBackListener, isShowDialog);
    }

    /**
     *@date2017/11/30
     *@author zhonghuibin
     *@description 扫码打开会议室
     */
    public static void openMeetingRoom(Context context, int company_id,int uid,String macaddress,String time_stamp,final ApiRequestFactory.HttpCallBackListener httpCallBackListener, boolean isShowDialog) {
        Map<String, String> map = new HashMap<>();
        map.put("company_id", company_id + "");
        map.put("uid", uid + "");
        map.put("macaddress", macaddress);
        map.put("time_stamp", time_stamp);
        ApiRequestFactory.postJson(context, ApiUrl.OPENMEETINGROOM, map, httpCallBackListener, isShowDialog);
    }

    /**
     * 楼层大小获取
     * @param context
     * @param louId
     * @param cengId
     * @param httpCallBackListener
     * @param isShowDialog
     */
    public static void getCengXY(Context context, String louId,String cengId,final ApiRequestFactory.HttpCallBackListener httpCallBackListener, boolean isShowDialog) {
        Map<String, String> map = new HashMap<>();
        map.put("louid", louId);
        map.put("cengid", cengId);
        ApiRequestFactory.postJson(context, ApiUrl.GETCENGXY, map, httpCallBackListener, isShowDialog);
    }
}
