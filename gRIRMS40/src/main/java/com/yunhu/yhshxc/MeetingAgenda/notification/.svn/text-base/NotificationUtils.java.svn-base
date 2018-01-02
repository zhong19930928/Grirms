package com.yunhu.yhshxc.MeetingAgenda.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.yunhu.yhshxc.R;

import static android.content.Context.NOTIFICATION_SERVICE;


/**
 * @author suhu
 * @data 2017/8/31
 * @description 这是一个通知工具类
 */
public class NotificationUtils {

    public static final String DELAY = "action.TIME_DELAY";
    public static final String CANCEL = "action.TIME_CANCEL";
    public static final String OK = "action.TIME_OK";

    private static NotificationManager notificationManager;
    private static Notification.Builder builder;


    /**
     *@method 根据用户选择进行不同的操作
     *@author suhu
     *@time 2017/9/1 15:28
     *@param
     *
    */
    public static void showNotification(Context context, Event event, int id) {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            builder = new Notification.Builder(context);
        }
        event.setNotificationID(id);

        RemoteViews contentViews = new RemoteViews(context.getPackageName(), R.layout.notifi_layout);
        contentViews.setImageViewResource(R.id.image, R.drawable.icon_main);
        contentViews.setTextViewText(R.id.title, event.getType()==1?"日程："+event.getContent() :"会议："+event.getContent());
        contentViews.setTextViewText(R.id.message, event.getType()==1?"日程时间：" + event.getMeetingTime():"会议时间：" + event.getMeetingTime());


        Bundle bundle = new Bundle();
        bundle.putSerializable("event",event);


        Intent receiver_ok = new Intent(OK);
        receiver_ok.putExtras(bundle);
        PendingIntent pendingIntent_ok = PendingIntent.getService(context, 0, receiver_ok, PendingIntent.FLAG_UPDATE_CURRENT);
        contentViews.setOnClickPendingIntent(R.id.ok, pendingIntent_ok);


        Intent receiver_delay = new Intent(DELAY);
        receiver_delay.putExtras(bundle);
        PendingIntent pendingIntent_delay = PendingIntent.getService(context, 0, receiver_delay, PendingIntent.FLAG_UPDATE_CURRENT);
        contentViews.setOnClickPendingIntent(R.id.delay, pendingIntent_delay);

        Intent receiver_cancel = new Intent(CANCEL);
        receiver_cancel.putExtras(bundle);
        PendingIntent pendingIntent_cancel = PendingIntent.getService(context, 0, receiver_cancel, PendingIntent.FLAG_UPDATE_CURRENT);
        contentViews.setOnClickPendingIntent(R.id.cancel, pendingIntent_cancel);



        builder.setContent(contentViews)
                .setSmallIcon(R.drawable.icon_main)
                .setTicker(event.getContent())
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_VIBRATE);

        Notification notification = builder.build();
        notification.defaults = Notification.DEFAULT_ALL;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(id, notification);
    }


    /**
     *@method 直接显示，不用用户交互
     *@author suhu
     *@time 2017/9/1 15:27
     *@param context
     *@param event
     *@param id
     *
    */
    public static void directShowNotification(Context context, Event event, int id) {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            builder = new Notification.Builder(context);
        }
        event.setNotificationID(id);

        RemoteViews contentViews = new RemoteViews(context.getPackageName(), R.layout.notifi_message_layout);
        contentViews.setImageViewResource(R.id.image, R.drawable.icon_main);
        contentViews.setTextViewText(R.id.title, event.getType()==1?"日程："+event.getContent() :"会议："+event.getContent());
        contentViews.setTextViewText(R.id.message, event.getType()==1?"日程时间：" + event.getMeetingTime():"会议时间：" + event.getMeetingTime());

        builder.setContent(contentViews)
                .setSmallIcon(R.drawable.icon_main)
                .setTicker(event.getContent())
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_VIBRATE);

        Notification notification = builder.build();
        notification.defaults = Notification.DEFAULT_ALL;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(id, notification);
    }



}
