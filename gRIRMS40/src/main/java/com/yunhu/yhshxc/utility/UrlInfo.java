package com.yunhu.yhshxc.utility;

import android.content.Context;

import com.yunhu.yhshxc.R;

public class UrlInfo {

    public static String baseUrl(Context context) {
        return PublicUtils.getBaseUrl(context);
    }


    /**
     *    获取固定资产数据查询接口
     * @param context
     * @return
     */
    public static String baseAssetsUrl(Context context){
    return baseUrl(context)+"queryDynamicListTextData.do";
}


    /**
     * 返回会议日程url
     * @param context
     * @return
     */
    public static String baseMeetingUrl(Context context) {
        return PublicUtils.getMeetingBaseUrl(context);
    }

    public static String getVersionInfo(Context context) {
        String baseUrl = baseUrl(context);
        String versionInfo = "";
        if (baseUrl != null) {
            if (baseUrl.equals(R.string.TEST_URL)) {
                versionInfo = "Test";
            } else if (baseUrl.equals(R.string.SIMULATE_URL)) {
                versionInfo = "Simulate";
            }
        }

        return versionInfo;
    }
    /**
     * 110101 =1*16
     */

    /**
     * 上传图片
     *
     * @param context 企业ID 除以2求余，余数0时存到服务器1，余数1时存到服务器2上。
     * @return
     */
    public static String urlUploadPhoto(Context context) {
        int companyId = SharedPreferencesUtil.getInstance(context).getCompanyId();

        if (PublicUtils.getBaseUrl(context).equals(context.getApplicationContext().getResources().getString(R.string.JieSiRui_URL))) {
            return context.getApplicationContext().getResources().getString(R.string.URL_UPLOAD_PHOTO_JieSiRui);
        }
        if (companyId % 2 == 0) {
            return context.getApplicationContext().getResources()
                    .getString(R.string.URL_UPLOAD_PHOTO);
        } else {
            return context.getApplicationContext().getResources()
                    .getString(R.string.URL_UPLOAD_PHOTO_2);
        }
    }

    // 上传log
    public static String urlUploadLog(Context context) {
        return context.getApplicationContext().getResources()
                .getString(R.string.URL_UPLOAD_LOG);
    }

    // 帮助文档
    public static String urlDownloadHelp(Context context) {
        return context.getApplicationContext().getResources()
                .getString(R.string.URL_DOWNLOAD_HELP);
    }

    // 初始化接口/全部信息
    public static String getUrlInit(Context context) {
        return baseUrl(context.getApplicationContext()) + "allInfo.do";
    }

    // 初始化接口-获取机构和店
    public static String getUrlInitOperation(Context context) {
        return baseUrl(context.getApplicationContext())
                + "operationOrStoreInfo.do";
    }

    // 初始化接口_获取评论、回复接口信息
    public static String getUrlRepliesInfo(Context context) {
        return baseUrl(context.getApplicationContext())
                + "doWeChatTopicInfo.do";
    }

    // 删除执行
    public static String getUrlDeleteExecute(Context context) {
        return baseUrl(context.getApplicationContext()) + "doCancelInfo.do";
    }

    // 查询考勤信息
    public static String getUrlSearcheAttendance(Context context) {
        return baseUrl(context.getApplicationContext()) + "workAttendInfo.do";
    }

    // 新查询考勤信息
    public static String getUrlSearcheNewAttendance(Context context) {
        return baseUrl(context.getApplicationContext())
                + "overWorkAttendData.do";
    }

    // 提交数据
    public static String getUrlSubmit(Context context) {
        return baseUrl(context.getApplicationContext()) + "doSaveInfo.do";
    }

    // 提交被动定位信息
    public static String getUrlLocationInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "doLocationInfo.do";
    }

    // 多点定位同时提交
    public static String getUrlLocationInfo2(Context context) {
        return baseUrl(context.getApplicationContext()) + "doLocationInfo04.do";
    }

    // 守店定位
    public static String getUrlAttendanceLocationInfo(Context context) {
        return baseUrl(context.getApplicationContext())
                + "doAttendGisDateInfo.do";
    }

    // 考勤信息上传
    public static String getUrlAttendance(Context context) {
        return baseUrl(context.getApplicationContext()) + "doWorkAttendInfo.do";
    }

    // 新考勤信息上传
    public static String getUrlNewAttendance(Context context) {
        return baseUrl(context.getApplicationContext())
                + "doOverWorkAttendInfo.do";
    }

    // 新考勤报岗信息上传
    public static String getUrlNewAttendanceGang(Context context) {
        return baseUrl(context.getApplicationContext())
                + "doOverWorkGangInfo.do";
    }

    // 新双向查询/修改/改派/审核信息
    public static String getUrlReplenish(Context context) {
        return baseUrl(context.getApplicationContext()) + "searchInfo.do";
    }

    // 删除拜访
    public static String getUrlDeleteStore(Context context) {
        return baseUrl(context.getApplicationContext()) + "doSaveVisitInfo.do";
    }

    // bbs圈子友人信息
    public static String getUrlBbsUserInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "forumsUserInfo.do";
    }

    // 获取回复信息
    public static String getUrlBbsCommentInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "forumsPostInfo.do";
    }

    // 信息流
    public static String getUrlBbsInformation(Context context) {
        return baseUrl(context.getApplicationContext()) + "forumsInfo.do";
    }

    // 发帖
    public static String getUrlBbsSubmitInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "doForumsInfo.do";
    }

    // 保存回复信息
    public static String getUrlBbsSubmitComment(Context context) {
        return baseUrl(context.getApplicationContext()) + "doPostInfo.do";
    }

    // 告诉服务端公告已读
    public static String getUrlIsReadNotify(Context context) {
        return baseUrl(context.getApplicationContext())
                + "doNotifiyIssueInfo.do";
    }

    // 报表
    public static String getUrlReportResult(Context context) {
        return baseUrl(context.getApplicationContext()) + "reportResultInfo.do";
    }

    // 提交当前版本，返回最新版本，有效期
    public static String getUrlApkVersionInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "versionInfo.do";
    }

    // 拜访表格历史数据查询
    public static String getUrlTableOldDataInfo(Context context) {
        return baseUrl(context.getApplicationContext())
                + "searchTableOldDataInfo.do";
    }

    // 获取帮助信息中的问题和答案
    public static String getUrlHelpQuestion(Context context) {
        return baseUrl(context.getApplicationContext()) + "questionInfo.do";
    }

    // 生成订单
    public static String getUrlPayInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "upPayNotice.do";
    }

    // 上传图片
    public static String getUrlPhoto(Context context) {
        return urlUploadPhoto(context.getApplicationContext())
                + "uploadInfo.do";
    }

    // 上传企业微信图片
    public static String getUrlLogoPhoto(Context context) {
        return urlUploadPhoto(context.getApplicationContext())
                + "uploadLogoInfo.do";
    }

    // 上传录音
    public static String getUrlRecord(Context context) {
        return urlUploadPhoto(context.getApplicationContext())
                + "uploadVfInfo.do";
    }

    // 考勤图片上传
    public static String getUrlAttendandceImage(Context context) {
        return urlUploadPhoto(context.getApplicationContext())
                + "uploadWorkAttendInfo.do";
    }

    // 上传圈子图片
    public static String getUrlBbsSubmitImg(Context context) {
        return urlUploadPhoto(context.getApplicationContext())
                + "uploadForumsInfo.do";
    }

    // 获取串码信息
    public static String getUrlCodeInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "codeInfo.do";
    }

    // GGMM验证url
    public static String getUrlCheckGGMM(Context context) {
        return baseUrl(context.getApplicationContext())
                + "queryCheckMobileByGGMM.do";
    }

    // 双向获取双向任务
    public static String getUrlDoubleTask(Context context) {
        return baseUrl(context.getApplicationContext()) + "doubleInfo.do";
    }

    /**
     * 创建订单时，列出上次提交的订单
     *
     * @param context Context对象
     * @return 返回URL
     */
    public static String getUrlPSSCreateOrderSearchHistory(Context context,
                                                           String storeid) {
        return baseUrl(context.getApplicationContext())
                + "orderHistoryInfo.do?storeid=" + storeid;
    }

    /**
     * 创建订单，选择产品时查出的信息
     *
     * @param context   Context对象
     * @param storeId   店面ID
     * @param productId 产品ID
     * @return 返回URL
     */
    public static String getUrlPSSCreateOrderProductInfo(Context context,
                                                         String storeId, String productId) {
        return baseUrl(context.getApplicationContext())
                + "orderProductInfo.do?proid=" + productId + "&storeid="
                + storeId;
    }

    /**
     * 查询订单
     *
     * @param context Context对象
     * @return 返回URL
     */
    public static String UrlPSSSearchOrder(Context context) {
        return baseUrl(context.getApplicationContext()) + "orderSearchInfo.do";
    }

    /**
     * 进货上报中，查询进货信息
     *
     * @param context Context对象
     * @return 返回URL
     */
    public static String UrlPSSStockInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "orderStockInfo.do";
    }

    /**
     * 退货管理中，退货查询
     *
     * @param context Context对象
     * @return 返回URL
     */
    public static String UrlPSSReturnSearch(Context context) {
        return baseUrl(context.getApplicationContext()) + "orderReturnInfo.do";
    }

    /**
     * 查询订单时，取消未生效的订单
     *
     * @param context Context对象
     * @return 返回URL
     */
    public static String UrlPSSOrderCancel(Context context) {
        return baseUrl(context.getApplicationContext())
                + "doOrderCancelInfo.do";
    }

    /**
     * 库存管理中，库存列表信息
     *
     * @param context Context对象
     * @param storeId 店面ID
     * @return 返回URL
     */
    public static String UrlPSSInventory(Context context, String storeId) {
        return baseUrl(context.getApplicationContext())
                + "orderInventoryInfo.do?storeid=" + storeId;
    }

    /**
     * 创建订单，订单提交
     *
     * @param context Context对象
     * @return 返回URL
     */
    public static String UrlPSSOrderSave(Context context) {
        return baseUrl(context.getApplicationContext()) + "doOrderSaveInfo.do";
    }

    /**
     * 进货上报中，进货情况提交
     *
     * @param context Context对象
     * @return 返回URL
     */
    public static String UrlPSSOrderStockSave(Context context) {
        return baseUrl(context.getApplicationContext()) + "doOrderStockInfo.do";
    }

    /**
     * 销量上报中，销量提交
     *
     * @param context Context对象
     * @return 返回URL
     */
    public static String UrlPSSOrderSalesSave(Context context) {
        return baseUrl(context.getApplicationContext()) + "doOrderSalesInfo.do";
    }

    /**
     * 退货管理，退货申请提交
     *
     * @param context Context对象
     * @return 返回URL
     */
    public static String UrlPSSOrderReturnedSave(Context context) {
        return baseUrl(context.getApplicationContext())
                + "doOrderReturnedInfo.do";
    }

    /**
     * 退货管理，退货申请取消
     *
     * @param context Context对象
     * @return 返回URL
     */
    public static String UrlPSSOrderReturnedCancel(Context context) {
        return baseUrl(context.getApplicationContext())
                + "doOrderReturnCancelInfo.do";
    }

    /**
     * 库存管理中，盘库提交
     *
     * @param context Context对象
     * @return 返回URL
     */
    public static String UrlPSSOrderInventorySave(Context context) {
        return baseUrl(context.getApplicationContext())
                + "doOrderInventoryInfo.do";
    }

    /**
     * 双向、拜访任务模块中接到PUSH的任务时，回调服务器
     *
     * @param targetId 模块ID
     * @param datatype 1：双向 2: 拜访 3: 初始化
     * @param did      双向任务id
     * @return URL
     */
    public static String getUrlDoDataStatusInfo(Context context,
                                                String targetId, int datatype, String did) {
        return baseUrl(context.getApplicationContext())
                + "doDataStatusInfo.do?id=" + targetId + "&datatype="
                + datatype + "&dataid=" + did;
    }

    /**
     * 查询新订单
     *
     * @param context
     * @return
     */
    public static String queryOrderNewInfo(Context context, int page) {
        return baseUrl(context.getApplicationContext())
                + "queryOrderNewInfo.do?phoneno="
                + PublicUtils.receivePhoneNO(context) + "&page=" + page;
    }

    /**
     * 查询新订单联系人
     *
     * @param context
     * @return
     */
    public static String queryOrderNewUserInfo(Context context, String storeid) {
        return baseUrl(context.getApplicationContext())
                + "queryOrderNewUserInfo.do?phoneno="
                + PublicUtils.receivePhoneNO(context) + "&storeId=" + storeid;
    }

    /**
     * 保存订单
     *
     * @param context
     * @return
     */
    public static String doOrderNewInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "doOrderNewInfo.do";
    }

    /**
     * 保存订单
     *
     * @param context
     * @return
     */
    public static String doThreeOrderInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "doThreeOrderInfo.do";
    }

    /**
     * 串码请求
     *
     * @param context
     * @return
     */
    public static String queryOrderProductInfo(Context context) {
        return baseUrl(context.getApplicationContext())
                + "queryOrderProductInfo.do";
    }

    /**
     * 促销信息
     *
     * @param context
     * @return
     */
    public static String queryOrder3ProductPromotion(Context context) {
        return baseUrl(context.getApplicationContext())
                + "queryThreeOrderPromotionInfo.do";
    }

    /**
     * 产品信息
     *
     * @param context
     * @return
     */
    public static String queryThreeOrderConfInfo(Context context) {
        return baseUrl(context.getApplicationContext())
                + "queryThreeOrderConfInfo.do";
    }

    /**
     * 产品分销信息
     *
     * @param context
     * @return
     */
    public static String queryThreeDistribution(Context context) {
        return baseUrl(context.getApplicationContext())
                + "queryThreeDistribution.do";
    }

    /**
     * 产查询订单
     *
     * @param context
     * @return
     */
    public static String queryThreeOrderInfo(Context context) {
        return baseUrl(context.getApplicationContext())
                + "queryThreeOrderInfo.do";
    }

    /**
     * 待办事项
     *
     * @param context
     * @return
     */
    public static String queryTodoUserInfo(Context context) {
        return baseUrl(context.getApplicationContext())
                + "queryTodoUserInfo.do?phoneno="
                + PublicUtils.receivePhoneNO(context);
    }

    /**
     * FR报表
     *
     * @param context
     * @return
     */
    public static String getInfoByIdForPhone(Context context) {
        return baseUrl(context.getApplicationContext())
                + "getInfoByIdForPhone.do";
    }

    /**
     * FR报表
     *
     * @param context
     * @return
     */
    public static String queryUdbPhoneInfo(Context context) {
        return baseUrl(context.getApplicationContext())
                + "queryUdbPhoneInfo.do";
    }

    /**
     * 就近拜访
     *
     * @param context
     * @return
     */
    public static String queryNearbyVisitDataInfo(Context context) {
        return baseUrl(context.getApplicationContext())
                + "queryNearbyVisitDataInfo.do";
    }

    /**
     * 就近拜访
     *
     * @param context
     * @return
     */
    public static String queryFreeCheckInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "freeCheckInfo.do";
    }

    /**
     * 初始化店面地址
     *
     * @param context
     * @return
     */
    public static String doStoreAddressInfo(Context context) {
        return baseUrl(context.getApplicationContext())
                + "doStoreAddressInfo.do";
    }

    /**
     * 车销配置信息
     *
     * @param context
     * @return
     */
    public static String carConfInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "carConfInfo.do";
    }

    /**
     * 车销促销信息
     *
     * @param context
     * @return
     */
    public static String carPromotionInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "carPromotionInfo.do";
    }

    /**
     * 销售记录信息查询
     *
     * @param context
     * @return
     */
    public static String carSalesInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "carSalesInfo.do";
    }

    /**
     * 保存销售记录
     *
     * @param context
     * @return
     */
    public static String doSalesInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "doSalesInfo.do";
    }

    /**
     * 保存退货记录
     *
     * @param context
     * @return
     */
    public static String doReturnInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "doReturnInfo.do";
    }

    /**
     * 保存装车记录
     *
     * @param context
     * @return
     */
    public static String doLodingInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "doLodingInfo.do";
    }

    /**
     * 保存补货申请记录
     *
     * @param context
     * @return
     */
    public static String doFillInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "doFillInfo.do";
    }

    /**
     * 保存卸车记录
     *
     * @param context
     * @return
     */
    public static String doTruckInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "doTruckInfo.do";
    }

    /**
     * 保存库存记录
     *
     * @param context
     * @return
     */
    public static String doStockInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "doStockInfo.do";
    }

    /**
     * 保存费用报销记录
     *
     * @param context
     * @return
     */
    public static String doCostInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "doCostInfo.do";
    }

    /**
     * 欠款记录查询
     *
     * @param context
     * @return
     */
    public static String carBalanceInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "carBalanceInfo.do";
    }

    /**
     * 查询装车记录
     */
    public static String queryLoadingInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "carLodingInfo.do";
    }

    /**
     * 查询卸车记录
     */
    public static String queryUnLoadingInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "carTruckInfo.do";
    }

    /**
     * 提交收账信息
     */
    public static String doCarBalanceInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "doCarBalanceInfo.do";
    }

    /**
     * 提交自助排班信息
     */
    public static String doAttendPaiInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "doAttendPaiInfo.do";
    }

    /**
     * 查询排班信息
     */
    public static String attendPaiInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "attendPaiInfo.do";
    }

    /**
     * 店面拓展固定模块新店上报
     */
    public static String doStoreExpInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "doStoreExpInfo.do";
    }

    /**
     * wechat保存话题信息
     */
    public static String doWebChatTopicInfo(Context context) {
        return baseUrl(context.getApplicationContext())
                + "doWeChatTopicInfo.do";
    }

    /**
     * wechat保存评论及回帖信息
     */
    public static String doWebRepliesInfo(Context context) {
        return baseUrl(context.getApplicationContext())
                + "doWeChatRepliesInfo.do";
    }

    /**
     * 话题信息查询
     */
    public static String weChatTopicInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "weChatTopicInfo.do";
    }

    /**
     * wechat点赞信息
     */
    public static String doWeChatPointInfo(Context context) {
        return baseUrl(context.getApplicationContext())
                + "doWeChatPointInfo.do";
    }

    /**
     * 企业微信 保存群信息
     */
    public static String doWebChatGroupInfo(Context context) {
        return baseUrl(context.getApplicationContext())
                + "doWeChatGroupInfo.do";
    }

    /**
     * 话题回帖信息查询
     */
    public static String weChatRepliesInfo(Context context) {
        return baseUrl(context.getApplicationContext())
                + "weChatRepliesInfo.do";
    }

    /**
     * 评论信息查询
     */
    public static String weChatReviewInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "weChatReviewInfo.do";
    }

    /**
     * 发布通知
     */
    public static String doWeChatNoticeInfo(Context context) {
        return baseUrl(context.getApplicationContext())
                + "doWeChatNoticeInfo.do";
    }

    /**
     * 查询调查类话题信息
     */

    public static String weChatAuditInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "weChatAuditInfo.do";
    }

    /**
     * 保存个人资料
     */
    public static String doWeChatUserInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "doWeChatUserInfo.do";
    }

    /**
     * 查询个人信息
     */
    public static String weChatUserInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "weChatUserInfo.do";
    }

    /**
     * 公告查询
     */
    public static String WeChatNoticeInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "weChatNoticeInfo.do";
    }

    /**
     * 保存关注信息
     */
    public static String doWeChatFollowInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "doWeChatFollowInfo.do";
    }

    /**
     * 保存历史话题删除记录信息
     */
    public static String doWeChatTopicHistoryInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "doWeChatTopicHistoryInfo.do";
    }

    /**
     *
     */
    public static String doWeChatNoticePersonInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "doWeChatNoticePersonInfo.do";
    }

    /**
     * 微信配置接口
     *
     * @param context
     * @return
     */
    public static String weChatConfInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "weChatConfInfo.do";
    }

    /**
     * 私聊信息提交接口
     *
     * @param context
     * @return
     */
    public static String doWeChatPrivateInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "doWeChatPrivateInfo.do";
    }

    /**
     * 私聊信息查询接口
     *
     * @param context
     * @return
     */
    public static String weChatPrivateInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "weChatPrivateInfo.do";
    }

    /**
     * 群信息查询接口
     *
     * @param context
     * @return
     */
    public static String weChatGroupInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "weChatGroupInfo.do";
    }

    /**
     * 帖子撤销接口
     *
     * @param context
     * @return
     */
    public static String doWeChatDelRepliesInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "doWeChatDelRepliesInfo.do";
    }

    /**
     * 通讯录获取接口
     *
     * @param context
     * @return
     */
    public static String mailListInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "mailListInfo.do";
    }

    public static String uploadHelpFileInfo(Context context) {
        return context.getApplicationContext().getResources()
                .getString(R.string.URL_WECHAT) + "uploadHelpFileInfo.do";
    }

    public static String netCheck(Context context) {
        return baseUrl(context);
    }


    /**
     * 调查问卷结果信息查询
     *
     * @param context
     * @return
     */
    public static String doQuestionResultInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "questionResultInfo.do";
    }

    /**
     * 调查问卷信息查询
     *
     * @param context
     * @return
     */
    public static String doQueryQuestionInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "queryQuestionInfo.do";
    }

    /**
     * 调查问卷结果列表信息查询
     *
     * @param context
     * @return
     */
    public static String doQuestionListInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "questionListInfo.do";
    }

    /**
     * 调查问卷信息查询
     *
     * @param context
     * @return
     */
    public static String doQuestionInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "doQuestionInfo.do";
    }

    /**
     * 试卷配置查询
     */
    public static String queryOnlineInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "queryOnlineInfo.do";
    }

    /**
     * 考勤当月报表
     *
     * @param context
     * @return
     */
    public static String queryWorkReportMonthInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "queryWorkReportMonthInfo.do";

    }

    /**
     * 今日拜访报表
     *
     * @param context
     * @return
     */
    public static String queryVisitReportTodayInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "queryVisitReportTodayInfo.do";

    }

    /**
     * 其他报表
     *
     * @param context
     * @return
     */
    public static String queryOtherReportInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "queryPhoneReport.do";
    }


    /**
     * 获取公司资料
     *
     * @param context
     * @return
     */
    public static String getCompanyFiles(Context context) {
        return baseUrl(context.getApplicationContext()) + "queryCompanyFiles.do";

    }

    /**
     * 考勤日历查询
     */
    public static String queryAttendListInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "queryAttendListInfo.do";
    }

    /**
     * 请假信息上报、修改、审核
     */
    public static String doLeaveInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "doLeaveInfo.do";
    }

    /**
     * 请假信息查询
     */
    public static String queryLeaveInfo(Context context) {
        return baseUrl(context.getApplicationContext()) + "queryLeaveInfo.do";
    }

    /**
     * 工作计划保存接口
     */
    public static String saveWorkPlan(Context context) {
        return baseUrl(context.getApplicationContext()) + "doWorkPlanInfo.do";

    }

    /**
     * 工作计划评价保存接口
     */
    public static String saveWorkPlanAssess(Context context) {
        return baseUrl(context.getApplicationContext()) + "doWorkPlanAssessInfo.do";

    }


    /**
     * 工作计划查询接口
     */
    public static String queryWorkPlan(Context context) {
        return baseUrl(context.getApplicationContext()) + "queryWorkPlanInfo.do";

    }

    /**
     * 工作总结查询接口
     */
    public static String queryWorkSum(Context context) {
        return baseUrl(context.getApplicationContext()) + "queryWorkSumInfo.do";

    }

    /**
     * 工作总结保存接口
     */
    public static String saveWorkSum(Context context) {
        return baseUrl(context.getApplicationContext()) + "doWorkSumInfo.do";

    }

    /**
     * 工作总结审核保存接口
     */
    public static String saveWorkSumAudit(Context context) {
        return baseUrl(context.getApplicationContext()) + "doWorkSumAuditInfo.do";

    }

    /**
     * 企业注册接口
     */
    public static String phoneRegister(Context context) {
        return "http://gnet.grirms.com/me86/phoneRegister.do";
    }

    /**
     * 根据手机的IMSI来查询手机号码,只针对电信
     *
     * @return
     */
    public static String getPhoneNumberByImsiUrl() {
        return "http://phone.gcgcloud.com/com.grandison.grirms.phone.web-1.0.0/queryUdbPhoneInfo.do";
    }

    /**
     * 获取服务器时间
     */
    public static String queryGcgServiceTime(Context context) {
        return baseUrl(context.getApplicationContext()) + "queryGcgServiceTime.do";
    }



    /**
     * 查询会议日程列表
     */
    public static String queryMeetingAgendaList(Context context) {
        return baseMeetingUrl(context.getApplicationContext()) + "m=Androidstation&a=Meetingschedulelist";
    }

}
