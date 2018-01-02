package com.yunhu.yhshxc.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.RelayActivity;
import com.yunhu.yhshxc.activity.todo.Todo;
import com.yunhu.yhshxc.activity.todo.TodoListActivity;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.bo.Push;
import com.yunhu.yhshxc.bo.PushItem;
import com.yunhu.yhshxc.database.DictDB;
import com.yunhu.yhshxc.database.DoubleDB;
import com.yunhu.yhshxc.database.FuncDB;
import com.yunhu.yhshxc.database.MainMenuDB;
import com.yunhu.yhshxc.database.OrgDB;
import com.yunhu.yhshxc.database.OrgStoreDB;
import com.yunhu.yhshxc.database.OrgUserDB;
import com.yunhu.yhshxc.database.PushDB;
import com.yunhu.yhshxc.database.PushItemDB;
import com.yunhu.yhshxc.database.TaskDB;
import com.yunhu.yhshxc.database.TodoDB;
import com.yunhu.yhshxc.database.VisitFuncDB;
import com.yunhu.yhshxc.database.VisitWayDB;
import com.yunhu.yhshxc.dialog.DialogNoticeActivity;
import com.yunhu.yhshxc.http.CoreHttpHelper;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.http.download.apk.DownApkBackstageService;
import com.yunhu.yhshxc.location.backstage.SharedPrefsBackstageLocation;
import com.yunhu.yhshxc.parser.CacheData;
import com.yunhu.yhshxc.parser.GetOrg;
import com.yunhu.yhshxc.parser.OrgParser;
import com.yunhu.yhshxc.submitManager.bo.PendingRequestVO;
import com.yunhu.yhshxc.submitManager.bo.TablePending;
import com.yunhu.yhshxc.submitManager.core.SubmitWorkManager;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.wechat.Util.WechatUtil;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gcg.org.debug.JLog;

/**
 * 用来处理PUSH下来的消息
 * 1.所以接收下来的PUSH存放在push表和PushItem表中
 * 2.根据规则对每条消息进行分类处理
 * 3.处理完成以后，提交完成消息的msgid给消息服务器
 *
 * @author david.hou
 * @version 2013-05-30
 */
public class PushBroadcastReceiver extends BroadcastReceiver {

    private final String TAG = "PushBroadcastReceiver";
    private Context mContext;

    public Handler handler2 = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 100) { //对于双向、拜访任务提交收到通知
                String url = (String) msg.obj;
                PendingRequestVO vo = new PendingRequestVO();
                vo.setContent(PublicUtils.getResourceString(mContext, R.string.toast_string8));
                vo.setTitle(PublicUtils.getResourceString(mContext, R.string.toast_string9));
                vo.setMethodType(SubmitWorkManager.HTTP_METHOD_GET);
                vo.setType(TablePending.TYPE_DATA);
                vo.setUrl(url);
                new CoreHttpHelper().performJustSubmit(mContext, vo);
            } else if (msg.what == 200) {
                try {
                    callbackStatus(); //提交完成消息的msgid给消息服务器
                } catch (Exception e) {
                    JLog.e(e);
                    PublicUtils.uploadLog(mContext, "log");
                }
            } else if (msg.what == 300) {
                getOrg(); //联网捞取机构店面用户的数据
            } else if (msg.what == 400) {
                String msgId = (String) msg.obj;
                new WechatUtil(mContext).initWechat(msgId);
                try {
                    callbackStatus(); //提交完成消息的msgid给消息服务器
                } catch (Exception e) {
                    JLog.e(e);
                    PublicUtils.uploadLog(mContext, "log");
                }
            } else if (msg.what == 500) {//通讯录push

            } else {
                PublicUtils.uploadLog(mContext, (String) msg.obj); //上传log
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 提交完成消息的msgid给消息服务器
     *
     * @throws Exception
     */
    private void callbackStatus() throws Exception {
        List<Push> isFinishPush = new PushDB(mContext).findAllPushIsFinish(); //所有完成的消息
        if (!isFinishPush.isEmpty()) {
            JSONArray array = new JSONArray();
            for (int i = 0; i < isFinishPush.size(); i++) {
                array.put(i, isFinishPush.get(i).getMsgId());
            }
            RequestParams params = new RequestParams();
            String msgidStr = array.toString(); //msgId的字符串
            params.put("cl", msgidStr);
            params.put("mdn", PublicUtils.receivePhoneNO(mContext));
            params.put("test", Constants.PUSH_TEST + "");
            JLog.d(TAG, "callbackStatus msgidStr= " + msgidStr);
            submit(Constants.URL_PUSH_CALLBACK_STATUS, params);
        }

        Constants.PUSH_IS_RUNNING = false;//执行结束
    }

    /**
     * push收到以后通知服务器
     *
     * @param url
     * @param params
     */
    private void submit(final String url, final RequestParams params) {
        JLog.d(TAG, "callbackStatus submit= " + url);
        GcgHttpClient.getInstance(mContext).post(url, params, new HttpResponseListener() {

            @Override
            public void onFailure(Throwable error, String content) {
                //提交失败
                JLog.d(TAG, "callbackStatus error= " + error.getMessage());
                if (url.contains(Constants.URL_PUSH)) { //提交失败后使用备用提交
                    submit(Constants.URL_PUSH_CALLBACK_STATUS_BAK, params);
                }
            }

            @Override
            public void onSuccess(int statusCode, String content) {
                //提交成功，（如果返回是为空，说明并未成功，使用备用接口）
                String res = PublicUtils.verifyReturnValue(content);
                if (!TextUtils.isEmpty(res)) {
//					new PushDB(mContext).deleteIsFinishPush();//删除本地的处理过的消息
                    JLog.d(TAG, "callbackStatus delete= " + res);
                } else { //使用备用接口
                    if (url.contains(Constants.URL_PUSH)) {
                        submit(Constants.URL_PUSH_CALLBACK_STATUS_BAK, params);
                    }
                }

            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

            }
        });

    }

    /**
     * 接收com.gcg.android.grirms.PNARRIVE的广播
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getExtras() != null) {
            this.mContext = context;
            boolean result = intent.getBooleanExtra(Constants.BROADCAST_KEY_PUSH, false);//是否接到可信任的广播
            if (result && !Constants.PUSH_IS_RUNNING) {
                Constants.PUSH_IS_RUNNING = true; //正在处理
                thread().start(); //处理消息的线程
            } else {
                JLog.d(TAG, "be running");
            }
        }
    }

    /**
     * 处理消息的线程
     *
     * @return
     */
    private Thread thread() {
        return new Thread() {
            @Override
            public void run() {
                PushDB pushDB = new PushDB(mContext);
                List<Push> list = pushDB.findAllPush(); //从DB中取到未处理的PUSN
                String threadName = this.currentThread().getName();
                Log.d(TAG, "=====开始执行======" + threadName);
                if (!list.isEmpty()) {
                    int i = 0;
                    for (Push push : list) {
                        JLog.d(TAG, (i++) + threadName + "msgId=" + push.getMsgId());
                        //处理消息
                        exePush(push);
                        //处理完成后，将消息设置成完成状态
                        push.setIsFinish(1);
                        pushDB.updatePush(push);
                    }
                    list = null;
                    handler2.sendEmptyMessage(200); //调用callbackStatus
                } else {
                    Constants.PUSH_IS_RUNNING = false;//执行结束
                }
                Log.d(TAG, "=====结果执行======" + threadName);
            }
        };
    }

    /**
     * 处理pushItem表中的数据
     *
     * @param currentPush
     */
    private void exePush(Push currentPush) {
        PushItemDB db = new PushItemDB(mContext);
        ArrayList<PushItem> list = db.findPushItemByMsgId(currentPush.getMsgId()); //根据msgId获取本地PushItem存在的所有数据
        for (PushItem pushItem : list) {
            try {
                exePushItem(pushItem);
            } catch (Exception e) {
                JLog.e(e);
                JLog.d("push解析异常 msgid(" + currentPush.getMsgId() + ")" + e.getMessage());
            }

            int type = pushItem.getType(); //消息类型
            String content = pushItem.getContent(); //消息内容
//			Message msg = Message.obtain();
//			msg.what=type;
//			msg.obj=content;
//			handler.sendMessage(msg);
            if (type == Constants.WECHAT_PUSH && "1".equals(content)) {
                JLog.d(TAG, "企业微信");
            } else {
//				db.deletePushItemById(pushItem.getId());
            }
            JLog.d(TAG, pushItem.getContent());
        }
    }

    /**
     * pushItem表中的每一条数据
     *
     * @param pushItem
     * @throws Exception
     */
    private void exePushItem(PushItem pushItem) throws Exception {

        if (pushItem != null) {
            int type = pushItem.getType(); //消息类型
            String status = pushItem.getStatus();//消息操作符
            String content = pushItem.getContent(); //消息内容
            if (type == 0 || TextUtils.isEmpty(status) || TextUtils.isEmpty(content)) {
                throw new Exception("push内容错误!");
            } else {
                if (status.equals("a")) { //下载APK
                    String[] msgArr = content.split("\\|");
                    if (msgArr.length >= 4) { //a|1.14.01|7ae553ffedccd45045a199e89fc85d41|http://219.148.162.101/GRIRMS4.0_1.14.01.apk|4391581
                        SharedPreferencesUtil.getInstance(mContext.getApplicationContext()).saveNewVersion(msgArr[0]);
                        SharedPreferencesUtil.getInstance(mContext.getApplicationContext()).saveMD5Code(msgArr[1]);
                        SharedPreferencesUtil.getInstance(mContext.getApplicationContext()).saveDownUrl(msgArr[2]);
                        SharedPreferencesUtil.getInstance(mContext.getApplicationContext()).saveApkSize(msgArr[3]);
                        Intent downService = new Intent(mContext, DownApkBackstageService.class);
                        mContext.startService(downService);
                    }
                } else {
                    getDataByType(type, content, status, pushItem.getMsgId()); //根据消息类型解析
                }
            }
        }
    }


    /**
     * 根据PUSH来的类型，条消息进行分类处理。
     *
     * @param type    消息类型
     * @param content 消息内容
     * @param status  消息操作符
     * @throws Exception
     */
    private void getDataByType(int type, String content, String status, String msgId) throws Exception {
        switch (type) {
            case Constants.PUSH_LOCATION_RULE: //获取定位规则
                if (CacheData.CUD_D.equals(status)) {
                    SharedPrefsBackstageLocation.getInstance(mContext.getApplicationContext()).setLocationIsAvailable(false);
                    JLog.d(TAG, "PUSH_LOCATION_RULE==>无效");
                } else {
                    new CacheData(mContext).parseAll(content);
                }
                break;
            case Constants.PUSH_PLAN: //获取访店计划
                if (CacheData.CUD_D.equals(status)) {
                    new VisitWayDB(mContext).removeVisitWayByPlanId(Integer.valueOf(content));
                } else if (CacheData.CUD_DT.equals(status)) {
                    new CacheData(mContext).parseAll(content, true); //删除字典表
                } else if (CacheData.CUD_C.equals(status)) {
                    Constants.changed_visit_task_notice = null;//修改的计划线路名
                    new CacheData(mContext).parseAll(content);
                }
                break;
            case Constants.PUSH_VISIT_FUNC:  //获取访店控件
                if (CacheData.CUD_D.equals(status)) {
                    new VisitFuncDB(mContext).removeFuncByTargetid(Integer.valueOf(content));
                } else if (CacheData.CUD_DT.equals(status)) {
                    new CacheData(mContext).parseAll(content, true); //删除字典表
                } else if (CacheData.CUD_C.equals(status)) {
                    new CacheData(mContext).parseAll(content);
                }
                break;
            case Constants.PUSH_DICT: //获取字典表
                if (CacheData.CUD_D.equals(status)) {
                    String[] str = content.split("@");
                    if (str.length == 2) {
                        new DictDB(mContext).deleteTableByidStr("t_m_" + str[0], str[1]);
                    }
                } else if (CacheData.CUD_DT.equals(status)) {
                    new CacheData(mContext).parseAll(content, true);
                } else if (CacheData.CUD_C.equals(status)) {
                    new CacheData(mContext).parseAll(content);
                }
                break;
            case Constants.PUSH_MENU: //获取模块
                if (CacheData.CUD_ALL.equals(status)) {
                    new MainMenuDB(mContext).removeAllMenu();
                    SharedPreferencesUtil.getInstance(mContext.getApplicationContext()).setIsInit(false); //重新初始化
                } else if (CacheData.CUD_D.equals(status)) {
                    new MainMenuDB(mContext).removeMenu(content);
                } else if (CacheData.CUD_DT.equals(status)) {
                    new CacheData(mContext).parseAll(content, true); //删除字典表
                } else if (CacheData.CUD_C.equals(status)) {
                    new CacheData(mContext).parseAll(content);
                }
                break;
            case Constants.PUSH_BASE_FUNC: //获取模块控件
                if (CacheData.CUD_D.equals(status)) {
                    new FuncDB(mContext).removeFuncByTargetid(Integer.valueOf(content));
                } else if (CacheData.CUD_DT.equals(status)) {
                    new CacheData(mContext).parseAll(content, true); //删除字典表
                } else if (CacheData.CUD_C.equals(status)) {
                    new CacheData(mContext).parseAll(content);
                }
                break;
            case Constants.PUSH_BASE_FIXED:
                new CacheData(mContext).parseAll(content);
                break;
            case Constants.PUSH_NEW_ATTENDANCE: //获取新考勤
                new CacheData(mContext).parseAll(content);
                break;
            case Constants.PUSH_ALL: //删除所有
                SharedPreferencesUtil.getInstance(mContext.getApplicationContext()).setIsInit(false); //重新初始化
                break;
            case Constants.PUSH_TASK:
                if (CacheData.CUD_D.equals(status)) {
                    String[] arr = content.split("@");
                    new TaskDB(mContext).removeTask(arr[1], arr[0]);
                } else {
                    new CacheData(mContext).parseAll(content);
                }
                break;
            case Constants.PUSH_NOTIFY: //公告
                new CacheData(mContext).parseAll(content);
                break;
            case Constants.PUSH_OPERATION: //机构
                if (CacheData.CUD_D.equals(status)) { //删除一条机构
                    new OrgDB(mContext).removeByOrgId(content);
                } else if (CacheData.CUD_DT.equals(status)) { //删除table后添加
                    new OrgDB(mContext).removeAll();
                    new OrgParser(mContext).parseAll(content);
                } else if (CacheData.CUD_C.equals(status)) {
                    if (TextUtils.isEmpty(content)) {
                        new OrgDB(mContext).removeAll();
                    } else {
                        new OrgParser(mContext).parseAll(content);
                    }
                }
                break;
            case Constants.PUSH_STORE:
                if (CacheData.CUD_D.equals(status)) { //删除一条店面
                    new OrgStoreDB(mContext).removeByStoreId(content);
                } else if (CacheData.CUD_DT.equals(status)) { //删除table后添加
                    new OrgStoreDB(mContext).removeAll();
                    new OrgParser(mContext).parseAll(content);
                } else if (CacheData.CUD_C.equals(status)) {
                    if (TextUtils.isEmpty(content)) {
                        new OrgStoreDB(mContext).removeAll();
                    } else {
                        new OrgParser(mContext).parseAll(content);
                    }
                }
                break;
            case Constants.PUSH_USER:
                if (CacheData.CUD_D.equals(status)) { //删除一条用户
                    new OrgUserDB(mContext).removeByUserId(content);
                } else if (CacheData.CUD_DT.equals(status)) { //删除table后添加
                    new OrgUserDB(mContext).removeAll();
                    new OrgParser(mContext).parseAll(content);
                } else if (CacheData.CUD_C.equals(status)) {
                    if (TextUtils.isEmpty(content)) {
                        new OrgUserDB(mContext).removeAll();
                    } else {
                        new OrgParser(mContext).parseAll(content);
                    }
                }
                break;
            case Constants.PUSH_DOUBLE: //双向任务
                if (CacheData.CUD_D.equals(status)) {
                    String[] arr = content.split("@");
                    new DoubleDB(mContext).removeDoubleTask(arr[1], arr[0]);
                } else {
//    			DoubleTaskManagerDB db = new DoubleTaskManagerDB(mContext);
//    			String [] arr = content.split("@");
//    			boolean isHas = db.isHasDoubleTaskManager(Integer.parseInt(arr[1]), Integer.parseInt(arr[0]));
//    			if (!isHas) {
//    				DoubleTaskManager m = new DoubleTaskManager();
//        			m.setDataId(Integer.parseInt(arr[0]));
//        			m.setMenuId(Integer.parseInt(arr[1]));
//        			db.insert(m);
//        			db.controlDoubleTaskManagerNum();
//        			new CacheData(mContext,handler2).parseAll(content);	
//			}

                    new CacheData(mContext, handler2).parseAll(content);
                }
                break;
            case Constants.PUSH_DOUBLE_STRUCT_CHANGE: //改变双向任务结构
                CacheData cacheDate = new CacheData(mContext);
                JSONObject jsonObject = new JSONObject(content);
                String doubleJson = jsonObject.getString(cacheDate.DOUBLE);
                cacheDate.parseDouble(doubleJson, true);
                break;
            case Constants.PUSH_ROLE: //角色
                new CacheData(mContext).parseAll(content);
                break;
            case Constants.PUSH_REPORT: //报表
                if (CacheData.CUD_D.equals(status)) {
                    new MainMenuDB(mContext).removeMenu(content);
                } else {
                    new CacheData(mContext).parseAll(content);
                }
                break;
            case Constants.PUSH_UPLOAD_LOG: //上传log
                if (!TextUtils.isEmpty(content)) {
                    handler2.obtainMessage(0, content).sendToTarget();
                }
                break;
            case Constants.PUSH_AVAIL_DATE: //有效期
                JLog.d(TAG, "有效期==》" + content);
                if (!TextUtils.isEmpty(content)) {
                    SharedPreferencesUtil.getInstance(mContext.getApplicationContext()).setUsefulStartDate(content.split(";")[0]);
                    SharedPreferencesUtil.getInstance(mContext.getApplicationContext()).setUsefulEndDate(content.split(";")[1]);
                }
                break;
            case Constants.PUSH_REPORT_NEW: //新报表
                if (CacheData.CUD_D.equals(status)) {
                    new MainMenuDB(mContext).removeMenu(content);
                } else {
                    new CacheData(mContext).parseAll(content);
                }
                break;

            case Constants.PUSH_INFO://解析所有
                new CacheData(mContext).parseAll(content);
                new OrgParser(mContext).parseAll(content);
                break;
            case Constants.PUSH_BASE_PARAM: //
                new CacheData(mContext).parseAll(content);
                break;
            case Constants.PUSH_VISIT_PARAM: //
                new CacheData(mContext).parseAll(content);
                break;
            case Constants.PUSH_PSS: //进销存
                new CacheData(mContext).parseAll(content);
                break;
            case Constants.PUSH_ORDER2: //新订单
                new CacheData(mContext).parseAll(content);
                break;
            case Constants.PUSH_ORDER3: //新订单
            case Constants.PUSH_ORDER3_SEND: //新订单
            case Constants.PUSH_ORDER3_FENXIAO: //分销
            case Constants.PUSH_ORDER3_CUXIAO: //促销
                new CacheData(mContext).parseAll(content);
                break;
            case Constants.IS_ADD_MODULE_STORE: //新店上报
                new CacheData(mContext).parseAll(content);
                break;
            case Constants.PUSH_CAR_SALES_CONF: //车销配置
            case Constants.PUSH_CAR_SALES_P: //车销促销
                new CacheData(mContext).parseAll(content);
                sendInitDataBroadcastReceiver();
                break;
            case Constants.PUSH_SUBMIT: //后台提交数据中返回0001的，现在开始提交
                if (CacheData.CUD_D.equals(status)) {
                    SubmitWorkManager.getInstance(mContext).removeByFail();
                } else {
                    SubmitWorkManager.getInstance(mContext).updatePendingInfoByFail();
                    SubmitWorkManager.getInstance(mContext).commit();
                }
                break;
            case Constants.PUSH_ORG_ALL: //获取机构、用户、店面
                handler2.sendEmptyMessage(300);
                break;
            case Constants.PUSH_TODO://待办事项
                pushToDo(content);
                break;
            case Constants.PUSH_WEB_REPORT://FR图表
                if (CacheData.CUD_D.equals(status)) {
                    new MainMenuDB(mContext).removeMenu(content);
                } else {
                    pushWebReport(content);
                }
                break;
            case Constants.WECHAT_PUSH://企业微信
                if ("1".equals(content)) {//1：有企信模块，需要手机端访问weChatConfInfo
                    Message msg = handler2.obtainMessage();
                    msg.what = 400;
                    msg.obj = msgId;
                    handler2.sendMessage(msg);
                } else if ("0".equals(content)) {// 0：无企信模块
                    new WechatUtil(mContext).removeWechat();
                }
                break;
            case Constants.MAIL_LIST://通讯录
                if ("1".equals(content)) {//1：有通讯录
                    Message msg = handler2.obtainMessage();
                    msg.what = 500;
                    msg.obj = msgId;
                    handler2.sendMessage(msg);
                } else if ("0".equals(content)) {// 0：无通讯录

                }
                break;
            default:
                break;
        }
        if (type == Constants.PUSH_PLAN || type == Constants.PUSH_DOUBLE || type == Constants.PUSH_NOTIFY || type == Constants.PUSH_TASK || type == Constants.PUSH_TODO) {
            if (CacheData.CUD_C.equals(status) && !TextUtils.isEmpty(content)) {
                JSONObject jsonObject = new JSONObject(content);
                if (type == Constants.PUSH_TODO || type == Constants.PUSH_DOUBLE || jsonObject.has(Constants.AWOKE_TYPE)) {
                    handler.obtainMessage(type, content).sendToTarget();
                }
            }
        }
    }

    private void pushWebReport(String content) {
        JLog.d(TAG, "FR图表:" + content);
        try {
            JSONObject obj = new JSONObject(content);
            CacheData cacheData = new CacheData(mContext);
            String jsonWebReport = obj.getString(cacheData.WEB_REPORT);
            cacheData.parseWebReport(jsonWebReport);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pushToDo(String content) {
        JLog.d(TAG, "待办事项:" + content);
        try {
            JSONObject obj = new JSONObject(content);
            if (obj != null && obj.has("resultcode") && "0000".equals(obj.getString("resultcode"))) {
                if (obj.has("todo")) {
                    new CacheData(mContext).parserToDo(obj.getString("todo"));
                    List<Todo> toDoList = new TodoDB(mContext).findAllTodo();
                    int tNum = toDoNumbers(toDoList);
                    Intent intent = new Intent();
                    intent.setAction(Constants.BROADCAST_ACTION_WAITING_PROCESS);
                    if (tNum > 0) {
                        String title1 = PublicUtils.getResourceString(mContext,R.string.toast_string12);
                        String title2 = PublicUtils.getResourceString(mContext,R.string.toast_string22);
                        intent.putExtra(Constants.BROADCAST_ACTION_WAITING_PROCESS, title1 + tNum + title2);
                        mContext.sendBroadcast(intent);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 待办事项条数
     */
    private int toDoNumbers(List<Todo> list) {
        int number = 0;
        for (int i = 0; i < list.size(); i++) {
            Todo todo = list.get(i);
            number += todo.getTodoNum();
        }
        return number;
    }

    private void getOrg() {
        new GetOrg(mContext, new GetOrg.ResponseListener() {

            @Override
            public void onSuccess(int type, String result) {
                new OrgParserTask().execute(result);
            }

            @Override
            public void onFailure(int type) {
                JLog.d(TAG, "PUSH_ORG_ALL=>联网失败...");
                JLog.d("PUSH_ORG_ALL=>联网失败...");
            }
        }).getAll();
    }

    /**
     * 解析机构、店面、用户
     *
     * @author houyu
     */
    private class OrgParserTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            String json = params[0];//获取要解析的json数据
            try {
                new OrgDB(mContext).removeAll();
                new OrgUserDB(mContext).removeAll();
                new OrgStoreDB(mContext).removeAll();
                //开始解析
                new OrgParser(mContext).parseAll(json);
            } catch (Exception e) {
                JLog.e(e);
            }
            return true;
        }
    }


    /**
     * 准备发送Notification
     */
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            try {
                String result = (String) msg.obj;
                JSONObject jsonObject = new JSONObject(result);
                switch (msg.what) {

                    case Constants.PUSH_PLAN: //获取访店计划
                        String title1 = PublicUtils.getResourceString(mContext, R.string.toast_string10);//计划
                        String title2 = PublicUtils.getResourceString(mContext, R.string.toast_string11);//一杯修改
                        String title4 = PublicUtils.getResourceString(mContext, R.string.toast_string12);//您有
                        String title5 = PublicUtils.getResourceString(mContext, R.string.toast_string13);//条新计划

                        if (!TextUtils.isEmpty(Constants.changed_visit_task_notice)) { //如果不为空，则说明此线路是被修改过的
                            noti(title1, "\"" + Constants.changed_visit_task_notice + "\"" + title2, 4, jsonObject.getInt(Constants.AWOKE_TYPE), 0, Constants.PUSH_PLAN);
                        } else {
                            noti(title1, title4 + (++Constants.PLANNUMBER) + title5, 3, jsonObject.getInt(Constants.AWOKE_TYPE), 0, Constants.PUSH_PLAN);
                        }
                        break;
                    case Constants.PUSH_DOUBLE://双向任务
                        String title6 = PublicUtils.getResourceString(mContext, R.string.toast_string14);//临时任务
                        String title7 = PublicUtils.getResourceString(mContext, R.string.toast_string15);//模块有
                        String title8 = PublicUtils.getResourceString(mContext, R.string.toast_string16);//条临时任务下达

                        Menu menu = new MainMenuDB(mContext).findMenuListByMenuId(CacheData.currentTargetId);
                        if (menu == null) {
                            break;
                        }
                        int number = Integer.parseInt(SharedPreferencesUtil.getInstance(mContext).getKey(menu.getMenuId() + ""));
                        SharedPreferencesUtil.getInstance(mContext).setKey(menu.getMenuId() + "", ++number + "");
                        noti(title6, menu.getName() + title7 + (number) + title8, menu.getMenuId(), 0, CacheData.currentTargetId, Constants.PUSH_DOUBLE);
                        break;
                    case Constants.PUSH_NOTIFY://公告
                        String title15 = PublicUtils.getResourceString(mContext, R.string.toast_string12);//您有
                        String title9 = PublicUtils.getResourceString(mContext, R.string.toast_string17);//公告

                        String title14 = PublicUtils.getResourceString(mContext, R.string.toast_string18);//条新公告
                        noti(title9, title15 + (++Constants.NOTICENUMBER) + title14, 1, jsonObject.getInt(Constants.AWOKE_TYPE), 0, Constants.PUSH_NOTIFY);
                        if (jsonObject.has(Constants.NOTIFY_IS_MUST)) {
                            if (jsonObject.getInt(Constants.NOTIFY_IS_MUST) == 1) {
                                Intent intent = new Intent(mContext, DialogNoticeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("push_notice", result);
                                mContext.startActivity(intent);
                            }
                        }
                        break;
                    case Constants.PUSH_TASK://任务
                        String title16 = PublicUtils.getResourceString(mContext, R.string.toast_string12);//您有
                        String title10 = PublicUtils.getResourceString(mContext, R.string.toast_string19);//任务
                        String title11 = PublicUtils.getResourceString(mContext, R.string.toast_string20);//条新任务

                        if (CacheData.currentTask != null) {
                            noti(title10, title16 + (++Constants.TASKNUMBER) + title11, 2, 0, 0, Constants.PUSH_TASK);
                        }
                        break;

                    case Constants.PUSH_TODO://待办事项
                        String title17 = PublicUtils.getResourceString(mContext, R.string.toast_string12);//您有
                        String title12 = PublicUtils.getResourceString(mContext, R.string.toast_string21);//待审批事项提醒
                        String title13 = PublicUtils.getResourceString(mContext, R.string.toast_string12);//条待审批事项
                        int tNum = toDoNumbers(new TodoDB(mContext).findAllTodo());
                        if (tNum > 0) {
                            noti(title12, title17 + tNum + title13, 0, 0, 0, Constants.PUSH_TODO);
                        }
                        break;

                }


            } catch (Exception e) {
                JLog.e(e);
            }

        }
    };


    /**
     * 发送Notification
     *
     * @param title     标题
     * @param content   内容
     * @param id        notification id
     * @param awoketype 1：震动   2：声音提示
     * @param targetId  任务ID
     * @param type      push类型
     */
    private void noti(String title, String content, int id, int awoketype, int targetId, int type) {
        NotificationManager nm = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(mContext);
        builder.setContentTitle(title);
        builder.setContentText(content);


        Notification n = builder.getNotification();
        if (PublicUtils.ISDEMO) {
            n.icon = R.drawable.icon_main;
        } else {
            n.icon = R.drawable.icon_main;
        }
        n.tickerText = title;
        if (awoketype == 2) {// isSound表示需要声音提示
            n.defaults |= Notification.DEFAULT_SOUND;// 调用默认声音
        } else if (awoketype == 1) {// 震动
            n.defaults |= Notification.DEFAULT_VIBRATE; // 调用系统默认震动，需要权限
            long[] vibrate = {0, 100, 200, 300}; // 自定义震动
            n.vibrate = vibrate;
        } else {//铃声加震动
            n.defaults |= Notification.DEFAULT_SOUND;// 调用默认声音
            n.defaults |= Notification.DEFAULT_VIBRATE; // 调用系统默认震动，需要权限
            long[] vibrate = {0, 100, 200, 300}; // 自定义震动
            n.vibrate = vibrate;
        }

        // n.sound = Uri.parse("file:///sdcard/test.mp3");//调用自定义声音

        n.defaults |= Notification.DEFAULT_LIGHTS; // 调用系统默认的灯光

        n.flags |= Notification.FLAG_AUTO_CANCEL; // 通知被点击后自动消除
        n.flags |= Notification.FLAG_NO_CLEAR; // 点击'Clear'时，不清除该通知
        //n.flags |= Notification.FLAG_; //让声音、振动无限循环，直到用户响应
        n.when = System.currentTimeMillis();
        Intent intent = new Intent(mContext, RelayActivity.class);

        switch (type) {
            case Constants.RELAY_NOTICE://公告
                intent.putExtra(Constants.RELAY_STATE, Constants.RELAY_NOTICE);
                break;
            case Constants.PUSH_TASK://任务
                intent.putExtra("moduleId", CacheData.currentTask.getModuleid());
                intent.putExtra(Constants.RELAY_STATE, Constants.RELAY_TASK);
                break;
            case Constants.PUSH_PLAN://计划
                intent.putExtra(Constants.RELAY_STATE, Constants.RELAY_PLAN);
                break;
            case Constants.PUSH_DOUBLE://双向
                intent.putExtra("targetId", targetId);
                intent.putExtra(Constants.RELAY_STATE, Constants.RELAY_DOUBLE);
                break;
            case Constants.PUSH_TODO://待审批事项提醒
                intent = new Intent(mContext, TodoListActivity.class);
                break;
            default:
                break;
        }

        PendingIntent pi = PendingIntent.getActivity(mContext, id, intent, 0);
        builder.setContentIntent(pi);
        nm.notify(id, n);

    }

    /**
     * 刷新产品通知
     */
    private void sendInitDataBroadcastReceiver() {
        Intent intent = new Intent(Constants.BROADCAST_CAR_SALES_REFRASH_PRODUCT);
        mContext.sendBroadcast(intent);
    }

}