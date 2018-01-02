package com.yunhu.yhshxc.MeetingAgenda.notification;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.yunhu.yhshxc.core.ApiRequestFactory;
import com.yunhu.yhshxc.core.ApiRequestMethods;
import com.yunhu.yhshxc.core.ApiUrl;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

import static com.yunhu.yhshxc.application.SoftApplication.context;

/**
 * @author suhu
 * @data 2017/9/7.
 * @description
 */

public class NotificationSubmit extends Service{

    private  NotificationManager notificationManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        String action = intent.getAction();
        Event event =(Event) intent.getExtras().getSerializable("event");
        if (event ==null) return;
        if (notificationManager==null){
            notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        }
//        notificationManager.cancel(event.getNotificationID());

        switch (action){
            case NotificationUtils.DELAY:
                postNet(event,3,NotificationUtils.DELAY);
                break;
            case NotificationUtils.CANCEL:
                postNet(event,4,NotificationUtils.CANCEL);
                break;
            case NotificationUtils.OK:
                postNet(event,2,NotificationUtils.OK);
                break;
        }

    }

    private void postNet(final Event event, final int type, final String action){
        ApiRequestMethods.confirmmeeting(this, event.getCompanyID(), event.getuID(), event.getuName(), event.getMeetingID(), type, event.getType(),ApiUrl.CONFIRMEETTING, new ApiRequestFactory.HttpCallBackListener() {
            @Override
            public void onSuccess(String response, String url, int id) {
                JSONObject object = null;
                try {
                    object = new JSONObject(response).getJSONObject("data");
                    if (object==null) return;
                    if(object.has("flag")&&object.getInt("flag")==0){
                        Intent intent=new Intent();
                        intent.putExtra("type", type==4?5:type);
                        intent.putExtra("event",event);
                        intent.setAction(action);
                        sendBroadcast(intent);
                    }else if(object.has("flag")&&object.getInt("flag")==-5){
                        Intent intent=new Intent();
                        intent.putExtra("type", type==4?5:type);
                        intent.putExtra("event",event);
                        intent.setAction(action);
                        sendBroadcast(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void failure(Call call, Exception e, int id) {

            }
        }, false);
    }
}
