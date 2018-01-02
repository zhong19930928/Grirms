package com.yunhu.yhshxc.wechat.fragments;

import java.util.Iterator;

import gcg.org.debug.JLog;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.wechat.Util.WechatUtil;
import com.yunhu.yhshxc.wechat.bo.Notification;
import com.yunhu.yhshxc.wechat.db.NotificationDB;
import com.yunhu.yhshxc.wechat.view.NoticeFujianView;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.loopj.android.http.RequestParams;

public class WechatNoticeActivity extends AbsBaseActivity {
    private TextView ib_return_notice;
    private TextView ib_shoucang;
    private TextView tv_item_name;
    private TextView tv_item_context;
    private TextView tv_name;
    private TextView tv_time;
    private LinearLayout ll_fujian;
    private NoticeFujianView view;// 通知附件有一个view
    private Notification notification;
    private NotificationDB notificationDB;
    private WechatUtil util;
    private MyProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wechat_notice_item);
        notificationDB = new NotificationDB(this);
        util = new WechatUtil(this);
        Intent intent = getIntent();
        int noticeId = intent.getIntExtra("noticeId", 0);
        notification = notificationDB.findNotifacationById(noticeId);
        initWidget();
        if (notification != null) {
            if ("1".equals(notification.getIsRead())) {

            } else {
                readingInfo(noticeId);
            }
            notificationDB.updateNotification(notification);
            initNotice(noticeId);
        }

//		 initData(noticeId);
    }

    /**
     * 保存接收状态
     *
     * @param noticeId
     */
    private void readingInfo(int noticeId) {
        String url = UrlInfo.doWeChatNoticePersonInfo(this);
        GcgHttpClient.getInstance(this).post(url, paramsInfo(noticeId),
                new HttpResponseListener() {

                    @Override
                    public void onSuccess(int statusCode, String content) {
                        JLog.d("alin", "content:" + content);
                        try {
                            JSONObject obj = new JSONObject(content);
                            String resultcode = obj.getString("resultcode");
                            if ("0000".equals(resultcode)) {
                                notification.setIsRead("1");
                                notificationDB.updateNotification(notification);
                            } else {
                                throw new Exception();
                            }
                        } catch (Exception e) {
                        }
                    }

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onFailure(Throwable error, String content) {
                    }
                });
    }

    private RequestParams paramsInfo(int noticeId) {
        RequestParams params = new RequestParams();
        // params.put("phoneno", PublicUtils.receivePhoneNO(this));
        params.put("noticeId", noticeId);
        JLog.d("alin", "params:" + params.toString());
        return params;
    }

    private void initNotice(int noticeId) {
        String url = UrlInfo.WeChatNoticeInfo(this);
        GcgHttpClient.getInstance(this).post(url, params(noticeId),
                new HttpResponseListener() {

                    @Override
                    public void onSuccess(int statusCode, String content) {
                        JLog.d("alin", "content:" + content);
                        try {
                            JSONObject obj = new JSONObject(content);
                            String resultcode = obj.getString("resultcode");
                            if ("0000".equals(resultcode)) {
                                notification = util.parserSearchNotification(
                                        content, notification);
                                initData(notification);
                            } else {
                                throw new Exception();
                            }
                        } catch (Exception e) {
                            // searchHandler.sendEmptyMessage(3);
                            ToastOrder.makeText(WechatNoticeActivity.this, R.string.reload_failure,
                                    ToastOrder.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onFailure(Throwable error, String content) {
                        JLog.d("alin", "failure");
                        ToastOrder.makeText(WechatNoticeActivity.this, R.string.reload_failure,
                                ToastOrder.LENGTH_LONG).show();
                    }
                });
    }

    private RequestParams params(int noticeId) {
        RequestParams params = new RequestParams();
        // params.put("phoneno", PublicUtils.receivePhoneNO(this));
        params.put("noticeId", noticeId);
        JLog.d("alin", "params:" + params.toString());
        return params;
    }

    private void initWidget() {
        ib_return_notice = (TextView) findViewById(R.id.ib_return_notice);
        ib_shoucang = (TextView) findViewById(R.id.ib_shoucang);
        tv_item_name = (TextView) findViewById(R.id.tv_item_name);
        tv_item_context = (TextView) findViewById(R.id.tv_item_context);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_time = (TextView) findViewById(R.id.tv_item_time);
        ll_fujian = (LinearLayout) findViewById(R.id.ll_fujian);
        ib_return_notice.setOnClickListener(listner);
        ib_shoucang.setOnClickListener(listner);
    }

    @SuppressLint("NewApi")
    private void initData(Notification notification) {
        // notification = notificationDB.findNotifacationById(noticeId);
        if (notification != null) {

            tv_item_name.setText(notification.getTitle());
            if (!TextUtils.isEmpty(notification.getContent())) {
                tv_item_context.setText("\u3000\u3000" + notification.getContent());
            } else {
                tv_item_context.setText("");
            }

            tv_name.setText(notification.getCreateOrg() + "("
                    + notification.getCreater() + ")");
            String time = notification.getCreateDate();
            if (!TextUtils.isEmpty(time)) {
                String[] ts = time.split(" ");
                tv_time.setText(ts[0]);
            } else {
                tv_time.setText("");
            }

            setFujian(notification);
            if (notification.getIsNoticed().equals("1")) {
                if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
                    ib_shoucang.setBackground(getResources().getDrawable(R.drawable.wechat_yishoucang));
                } else {
                    ib_shoucang.setBackgroundDrawable(getResources().getDrawable(R.drawable.wechat_yishoucang));
                }

            } else {
                if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
                    ib_shoucang.setBackground(getResources().getDrawable(R.drawable.wechat_focus_m));
                } else {
                    ib_shoucang.setBackgroundDrawable(getResources().getDrawable(R.drawable.wechat_focus_m));
                }

            }
        }
    }

    private void setFujian(Notification notification) {
        ll_fujian.removeAllViews();
//		String fujian = notification.getAttachment();
        try {
            if (notification != null) {
                String attachment = notification.getAttachment();
                if (!TextUtils.isEmpty(attachment)) {
                    JSONObject attObj = new JSONObject(attachment);
                    Iterator<String> iterator = attObj.keys();
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        String url = attObj.getString(key);
                        if (!(url.endsWith(".zip") || url.endsWith(".rar"))) {//压缩文件格式在手机端不显示
                            NoticeFujianView attView = new NoticeFujianView(this);
                            attView.setAttachmentData(key, url);
                            ll_fujian.addView(attView.getView());
                        }
//						WechatAttachment a = new WechatAttachment();
//						a.setName(key);
//						a.setUrl(url);
//						WechatAttachmentView view = new WechatAttachmentView(WechatNoticeActivity.this);
//						view.setData(a);
////						view.setShowVoiceReferView(WechatNoticeActivity.this.ll_send);
//						ll_fujian.addView(view.getView());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private OnClickListener listner = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ib_return_notice:// 返回通知
                    WechatNoticeActivity.this.finish();
                    break;
                case R.id.ib_shoucang:// 收藏
                    if (notification.getIsNoticed().equals("1")) {
                        canselSC();
                    } else {
                        shouCang();
                    }
                    break;

                default:
                    break;
            }
        }

    };

    private void canselSC() {
        String url = UrlInfo.doWeChatFollowInfo(this);
        GcgHttpClient.getInstance(this).post(url, paramsCancel(),
                new HttpResponseListener() {

                    @SuppressLint("NewApi")
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        JLog.d("alin", "content:" + content);
                        try {
                            JSONObject obj = new JSONObject(content);
                            String resultcode = obj.getString("resultcode");
                            if ("0000".equals(resultcode)) {
                                notification.setIsNoticed("0");
                                notificationDB.updateNotification(notification);
                                WechatNoticeActivity.this.sendBroadcast(new Intent(Constants.BROADCAST_WECHAT_FOCUS));
                                if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
                                    ib_shoucang.setBackground(getResources().getDrawable(R.drawable.wechat_focus_m));
                                } else {
                                    ib_shoucang.setBackgroundDrawable(getResources().getDrawable(R.drawable.wechat_focus_m));
                                }
//								

                                ToastOrder.makeText(WechatNoticeActivity.this,
                                        R.string.wechat_content21, ToastOrder.LENGTH_LONG).show();
                            } else {
                                throw new Exception();
                            }
                        } catch (Exception e) {
                            // searchHandler.sendEmptyMessage(3);
                            ToastOrder.makeText(WechatNoticeActivity.this, R.string.wechat_content20,
                                    ToastOrder.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onFailure(Throwable error, String content) {
                        JLog.d("alin", "failure");
                        ToastOrder.makeText(WechatNoticeActivity.this, R.string.wechat_content20,
                                ToastOrder.LENGTH_LONG).show();
                    }
                });
    }

    private RequestParams paramsCancel() {
        RequestParams params = new RequestParams();
        String json = "";
        JSONObject dObject = new JSONObject();
        try {
            dObject.put("followId", notification.getNoticeId());
            dObject.put("type", "2");
            dObject.put("action", "d");//添加关注
        } catch (JSONException e) {
            e.printStackTrace();
        }
        json = dObject.toString();
        params.put("data", json);
        JLog.d("alin", "params:" + params.toString());
        return params;
    }

    private void shouCang() {
        String url = UrlInfo.doWeChatFollowInfo(this);
        GcgHttpClient.getInstance(this).post(url, params(),
                new HttpResponseListener() {

                    @SuppressLint("NewApi")
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        JLog.d("alin", "content:" + content);
                        try {
                            JSONObject obj = new JSONObject(content);
                            String resultcode = obj.getString("resultcode");
                            if ("0000".equals(resultcode)) {
                                notification.setIsNoticed("1");
                                notificationDB.updateNotification(notification);
                                WechatNoticeActivity.this.sendBroadcast(new Intent(Constants.BROADCAST_WECHAT_FOCUS));
                                if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
                                    ib_shoucang.setBackground(getResources().getDrawable(R.drawable.wechat_yishoucang));
                                } else {
                                    ib_shoucang.setBackgroundDrawable(getResources().getDrawable(R.drawable.wechat_yishoucang));
                                }
//								

                                ToastOrder.makeText(WechatNoticeActivity.this,
                                        R.string.wechat_content19, ToastOrder.LENGTH_LONG).show();
                            } else {
                                throw new Exception();
                            }
                        } catch (Exception e) {
                            // searchHandler.sendEmptyMessage(3);
                            ToastOrder.makeText(WechatNoticeActivity.this, R.string.wechat_content18,
                                    ToastOrder.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onFailure(Throwable error, String content) {
                        JLog.d("alin", "failure");
                        ToastOrder.makeText(WechatNoticeActivity.this, R.string.wechat_content18,
                                ToastOrder.LENGTH_LONG).show();
                    }
                });
    }

    private RequestParams params() {
        RequestParams params = new RequestParams();
        String json = "";
        JSONObject dObject = new JSONObject();
        try {
            dObject.put("followId", notification.getNoticeId());
            dObject.put("type", "2");
            dObject.put("action", "c");//添加关注
        } catch (JSONException e) {
            e.printStackTrace();
        }
        json = dObject.toString();
        params.put("data", json);
        JLog.d("alin", "params:" + params.toString());
        return params;
    }


}
