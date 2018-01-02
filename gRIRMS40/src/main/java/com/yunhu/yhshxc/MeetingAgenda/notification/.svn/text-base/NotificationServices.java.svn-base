package com.yunhu.yhshxc.MeetingAgenda.notification;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.yunhu.yhshxc.application.SoftApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by suhu on 2017/8/31.
 */

public class NotificationServices extends Service {
    /**
     * 时间间隔毫秒数：
     */
    private static final int SPACING_TIME = 5000;

    private List<Event> eventsList;
    private int id = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        sendNotification();

    }

    /**
     *@method 发送通知
     *@author suhu
     *@time 2017/9/1 15:21
     *@description 只要提醒时间小于当前时间就移除该队列里的消息
     * type值1：直接显示（移除）
     *       2：与用户交互，先删除，然后看情况添加
     * id：用不同id来区分通知
    */
    private void sendNotification() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(SPACING_TIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    long time = System.currentTimeMillis();
                    eventsList = SoftApplication.getInstance().getEventsList();
                    for (int i = 0; i < eventsList.size(); i++) {
                        Event event = eventsList.get(i);
                        long remindingTime = getTime(event.getRemindingTime());
                        if (remindingTime < time ) {
                            if (time - remindingTime < SPACING_TIME){
                                if (event.getMeetingType()==1){
                                    //直接显示
                                    NotificationUtils.directShowNotification(NotificationServices.this, event, id);
                                }else if(event.getMeetingType()==2){
                                    //与用户交互
//                                    NotificationUtils.showNotification(NotificationServices.this, event, id);
                                    NotificationUtils.directShowNotification(NotificationServices.this, event, id);
                                }
                                id++;
                            }
                            eventsList.remove(i);

                        }

                    }
                }
            }
        }).start();
    }


    private long getTime(String data) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(data).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private String getData(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
        return sdf.format(new Date(time));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
