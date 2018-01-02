package com.yunhu.yhshxc.inspection;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.CompanyWebActivity;
import com.yunhu.yhshxc.activity.MenuUsableControl;
import com.yunhu.yhshxc.activity.ModuleActivity;
import com.yunhu.yhshxc.activity.TaskListActivity;
import com.yunhu.yhshxc.activity.addressBook.AddressBookActivity;
import com.yunhu.yhshxc.activity.carSales.CarSalesMainActivity;
import com.yunhu.yhshxc.activity.carSales.util.SharedPreferencesForCarSalesUtil;
import com.yunhu.yhshxc.activity.questionnaire.QuestionnaireActivity;
import com.yunhu.yhshxc.activity.todo.TodoListActivity;
import com.yunhu.yhshxc.attendance.AttendanceActivity;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.bo.Task;
import com.yunhu.yhshxc.database.TaskDB;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.expand.StoreExpandActivity;
import com.yunhu.yhshxc.help.HelpPopupWindow;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.module.bbs.BBSMainActivity;
import com.yunhu.yhshxc.nearbyVisit.NearbyVisitActivity;
import com.yunhu.yhshxc.notify.NotifyListActivity;
import com.yunhu.yhshxc.order2.Order2MainActivity;
import com.yunhu.yhshxc.order3.Order3MainActivity;
import com.yunhu.yhshxc.order3.send.Order3SendActivity;
import com.yunhu.yhshxc.report.ReportActivity;
import com.yunhu.yhshxc.submitManager.SubmitManagerActivity;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil2;
import com.yunhu.yhshxc.utility.URLWrapper;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.visit.VisitWayActivity;
import com.yunhu.yhshxc.webReport.WebViewUtilActivity;
import com.yunhu.yhshxc.wechat.WechatActivity;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.yunhu.yhshxc.workSummary.WorkSummaryMainActivity;
import com.yunhu.yhshxc.workplan.WorkPlanActivity;

import org.json.JSONObject;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import encoding.Base64;
import view.TipDialog;

/**
 * Created by YH on 2017/6/30.
 */

public class MenuItemClick {
    private Menu menu;
    private Context context;

    public MenuItemClick(Menu menu,Context context){
        this.menu=menu;
        this.context=context;

    }

    public void setOrder3Time() {
        flagOrder3Time = true;
        timer = new Timer(true);
        if (task.cancel()) {
            timer.schedule(task, 3000, 100000000); // 延时3000ms后执行，1000ms执行一次
        }
        new Thread(new Runnable() {

            @Override
            public void run() {
                Date date = PublicUtils.getNetDate();
                Message msg = Message.obtain();
                msg.obj = date;
                mHanlder.sendMessage(msg);
            }
        }).start();
    }


    private boolean flagOrder3Time = true;
    Timer timer;
    TimerTask task = new TimerTask() {
        public void run() {
            mHanlder.sendEmptyMessage(1);
        }
    };


    Handler mHanlder = new Handler() {
        public void handleMessage(Message msg) {
            if (flagOrder3Time) {
                flagOrder3Time = false;
                if (task != null) {
                    task.cancel();
                }
                Date date = (Date) msg.obj;
                onItemclickMenu(menu,  date);
            }
        }
    };


    public void onItemclickMenu(Menu menu,  Date date) {
        if (usableControl(menu, date)) {
            if (menu.getType() == Menu.WORK_PLAN) { // 打开工作计划
                Intent intent = new Intent(context, WorkPlanActivity.class);
                context.startActivity(intent);
                return;
            }
            if (menu.getType() == Menu.WORK_SUM) {// 打开工作总结
                Intent intent = new Intent(context, WorkSummaryMainActivity.class);
                context.startActivity(intent);
                return;
            }

            if (menu.getType() == Menu.TYPE_NOTICE) {// 打开公告模块
                Intent intent = new Intent(context, NotifyListActivity.class);
                context.startActivity(intent);
            } else if (menu.getType() == Menu.TYPE_VISIT) {// 打开访店模块 拜访
                Intent intent = new Intent(context, VisitWayActivity.class);
                intent.putExtra("menuId", menu.getMenuId());
                intent.putExtra("menuName", menu.getName());
                intent.putExtra("menuType", menu.getType());
                context.startActivity(intent);
            } else if (menu.getType() == Menu.TYPE_ATTENDANCE) {// 打开考勤模块
                Intent intent = new Intent(context, AttendanceActivity.class);
                context.startActivity(intent);
            } else if (menu.getType() == Menu.TYPE_NEW_ATTENDANCE) {// 打开考勤模块
                ToastOrder.makeText(context, "请点击底栏考勤按钮", ToastOrder.LENGTH_LONG).show();
                // Intent intent = new Intent(context,
                // AttendanceFragment.class);
                // startActivity(intent);
            } else if (menu.getType() == Menu.TYPE_BBS) {// 打开BBS模块
                Intent intent = new Intent(context, BBSMainActivity.class);
                context.startActivity(intent);
            } else if (menu.getType() == Menu.TYPE_TARGET) {// 打开任务模块（如果数据库中有任务的话）
                List<Task> taskList = new TaskDB(context).findAllTaskByModuleid(menu.getMenuId());
                if (taskList.isEmpty()) {
                    // 如果没有任务就不打开TaskListActivity，而是用一个Toast提示用户
                    ToastOrder.makeText(context, context.getResources().getString(R.string.noTask), ToastOrder.LENGTH_SHORT)
                            .show();
                } else {
                    // 如果有任务就打开TaskListActivity
                    Intent intent = new Intent(context, TaskListActivity.class);
                    intent.putExtra("moduleId", menu.getMenuId());
                    // intent.putExtra("taskName", menu.getName());
                    context.startActivity(intent);
                }
            } else if (menu.getType() == Menu.TYPE_REPORT) {// 打开报表模块
                Intent intent = new Intent(context, ReportActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("targetId", menu.getMenuId());
                bundle.putInt("menuType", Menu.TYPE_REPORT);
                bundle.putString("menuName", menu.getName());
                intent.putExtra("bundle", bundle);
                context.startActivity(intent);
            } else if (menu.getType() == Menu.TYPE_REPORT_NEW) {// 打开新报表模块
                Intent intent = new Intent(context, ReportActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("targetId", menu.getMenuId());
                bundle.putInt("menuType", Menu.TYPE_REPORT_NEW);
                bundle.putString("menuName", menu.getName());
                intent.putExtra("bundle", bundle);
                context.startActivity(intent);
            } else if (menu.getType() == Menu.TYPE_HELP) {// 打开帮助模块
                new HelpPopupWindow(context).show(null);// 基于home页的GridView组件显示弹出窗口
            } else if (menu.getType() == Menu.TYPE_ORDER2) {

                Intent intent = new Intent(context, Order2MainActivity.class);
                context.startActivity(intent);
            } else if (menu.getType() == Menu.TYPE_ORDER3) {// 订单
                Intent intent = new Intent(context, Order3MainActivity.class);
                intent.putExtra("menuType", menu.getType());
                intent.putExtra("targetId", menu.getMenuId());
                context.startActivity(intent);
            } else if (menu.getType() == Menu.TYPE_ORDER3_SEND) {// 送货
                Intent intent = new Intent(context, Order3SendActivity.class);
                context.startActivity(intent);
            } else if (menu.getType() == Menu.TYPE_CAR_SALES) {// 车销
                int carID = SharedPreferencesForCarSalesUtil.getInstance(context).getCarId();
                if (carID != 0) {
                    Intent intent = new Intent(context, CarSalesMainActivity.class);
                    context.startActivity(intent);
                } else {
                    ToastOrder.makeText(context, "该用户没配置车辆", ToastOrder.LENGTH_SHORT).show();
                }

            } else if (menu.getType() == Menu.TYPE_MANAGER) {

                Intent intent = new Intent(context, SubmitManagerActivity.class);
                context.startActivity(intent);
            } else if (menu.getType() == Menu.TYPE_TODO) {
                Intent intent = new Intent(context, TodoListActivity.class);
                context.startActivity(intent);
            } else if (menu.getType() == Menu.TYPE_WEB_REPORT) {// FR报表
                frReport(menu);
            } else if (menu.getType() == Menu.TYPE_NEARBY) {// 就近拜访
                nearbyVisit(menu);
            } else if (menu.getType() == Menu.IS_STORE_ADD_MOD) {// 新店上报
                newStoreReport(menu);
            } else if (menu.getType() == Menu.WEI_CHAT) {// 企业微信
                weChat();
            } else if (menu.getType() == Menu.QUESTIONNAIRE) {// 调查问卷
                question();
            } else if (menu.getType() == Menu.MAIN_LIST) {// 通讯录
                addressBook();
            } else if (menu.getType() == Menu.COMPANY_TYPE1) {// 公司指定的webview展示模块
                loadCompanyWeb(menu);
            } else if (menu.getType() == 28) {// 临时测试,工作总结
                context.startActivity(new Intent(context, WorkSummaryMainActivity.class));
            } else {// 打开默认模块
                int menuId = menu.getMenuId();
                if (menuId == 2010286 || menuId == 2010287 || menuId == 2010557 || menuId == 2010558
                        || menuId == 2010559 || menuId == 2010560 || menuId == 2010561 || menuId == 2010562
                        || menuId == 2010563 || menuId == 2010564 || menuId == 2010565 || menuId == 2010566
                        || menuId == 2010567 || menuId == 2010568 || menuId == 2010569 || menuId == 2010570
                        || menuId == 2010571 || menuId == 2010572 || menuId == 2010573 || menuId == 2010574
                        || menuId == 2010676 || menuId == 2010677 || menuId == 2010678 || menuId == 2010679
                        || menuId == 2010680 || menuId == 2010681 || menuId == 2010682 || menuId == 2010683
                        || menuId == 2010684 || menuId == 2010685 || menuId == 2010686 || menuId == 2010687) {
                    webReport(menuId);
                } else {// 自由访店等
                    if(menu.getName().equals("气体采集")){
                        TipDialog tipDialog = new TipDialog(context,R.style.CustomProgressDialogTip);
                        tipDialog.show();
                    }else{
                        Intent intent = new Intent(context, ModuleActivity.class);
                        intent.putExtra("menuId", menu.getMenuId());
                        intent.putExtra("menuName", menu.getName());
                        intent.putExtra("menuType", menu.getType());
                        intent.putExtra("isNoWait", menu.getIsNoWait() == 1 ? true : false);
                        if (menu.getModuleType() != null && menu.getModuleType() == Menu.MENU_MODULE_TYPE_4) {// 表示是店面拓展模块
                            intent.putExtra("is_store_expand", 1);
                        }
                        context.startActivity(intent);
                    }

                }
            }

        } else {
        }
    }


    private boolean usableControl(Menu menu, Date date) {
        boolean flag = true;
        String time = menu.getPhoneUsableTime();
        MenuUsableControl control = new MenuUsableControl();
        // time = "12:00|0.5,14:55|0.5";
        flag = control.isCanUse(time, date);
        if (menu.getType() == Menu.TYPE_ORDER3) {
            if (flag) {// 时间过了判断天
                if (null == date) {
                    flag = false;
                    Toast.makeText(context, PublicUtils.getResourceString(context,R.string.check_net), Toast.LENGTH_SHORT).show();
                } else {
                    flag = control.isOrder3CanUse(context, date);
                    if (!flag) {
//                        showTip(PublicUtils.getResourceString(context,R.string.toast_one8));
                        Toast.makeText(context,PublicUtils.getResourceString(context,R.string.toast_one8),Toast.LENGTH_SHORT).show();
                    }
                }

            } else {
//                showTip(control.tipInfo(time));
                Toast.makeText(context,control.tipInfo(time),Toast.LENGTH_SHORT).show();
            }
        } else {
            if (!flag) {
//                showTip(control.tipInfo(time));
                Toast.makeText(context,control.tipInfo(time),Toast.LENGTH_SHORT).show();
            }
        }
        return flag;
    }


    /**
     * FR报表
     *
     * @param menu
     */
    private void frReport(Menu menu) {
        final Dialog dilog = new MyProgressDialog(context, R.style.CustomProgressDialog, PublicUtils.getResourceString(context,R.string.init));
        URLWrapper wrapper = new URLWrapper(UrlInfo.getInfoByIdForPhone(context));
        wrapper.addParameter("phoneno", PublicUtils.receivePhoneNO(context));
        wrapper.addParameter("moduleId", menu.getMenuId());
        String url = wrapper.getRequestURL();
        GcgHttpClient.getInstance(context).get(url, null, new HttpResponseListener() {
            @Override
            public void onStart() {
                if (dilog != null && !dilog.isShowing()) {
                    dilog.show();
                }
            }

            @Override
            public void onSuccess(int statusCode, String content) {
                // JLog.d(TAG, content);
                try {
                    JSONObject obj = new JSONObject(content);
                    String resultCode = obj.getString("resultcode");
                    if ("0000".equals(resultCode)) {
                        String url = obj.getString("url");
                        String pm = obj.getString("pm");// 手机端横竖屏显示
                        if (!TextUtils.isEmpty(url)) {
                            Intent intent = new Intent(context, WebViewUtilActivity.class);
                            intent.putExtra("url", url);
                            intent.putExtra("pm", pm);
                            context.startActivity(intent);
                        } else {
                            throw new Exception();
                        }
                    } else {
                        throw new Exception();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastOrder.makeText(context, R.string.ERROR_DATA, ToastOrder.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable error, String content) {
                ToastOrder.makeText(context, R.string.reload_failure, ToastOrder.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                if (dilog != null && dilog.isShowing()) {
                    dilog.dismiss();
                }
            }
        });
    }


    /**
     * 就近拜访
     *
     * @param menu
     */
    private void nearbyVisit(Menu menu) {
        Intent intent = new Intent(context, NearbyVisitActivity.class);
        intent.putExtra("title", menu.getName());
        intent.putExtra("menuId", String.valueOf(menu.getMenuId()));
        intent.putExtra("menuType", menu.getType());
        context.startActivity(intent);
    }


    /**
     * 新店上报
     */
    private void newStoreReport(Menu menu) {
        Bundle bundle = new Bundle();
        bundle.putInt("menuId", menu.getMenuId());
        bundle.putString("menuName", menu.getName());
        bundle.putInt("menuType", menu.getType());
        bundle.putInt("planId", Constants.DEFAULTINT);// 计划ID
        bundle.putInt("awokeType", Constants.DEFAULTINT);// 提醒类型
        bundle.putInt("wayId", Constants.DEFAULTINT);// 线路ID
        bundle.putInt("storeId", Constants.DEFAULTINT);// 店面ID
        bundle.putInt("targetId", SharedPreferencesUtil2.getInstance(context).getStoreInfoId());// 数据ID
        bundle.putString("storeName", null);// 店面名称
        bundle.putString("wayName", null);// 线路名称
        bundle.putInt("isCheckin", Constants.DEFAULTINT);// 是否要进店定位1 是 0和null否
        bundle.putInt("isCheckout", Constants.DEFAULTINT);// 是否要离店定位1 是 0和null否
        bundle.putInt("menuType", Menu.IS_STORE_ADD_MOD);// 菜单类型
        bundle.putInt("targetType", Menu.IS_STORE_ADD_MOD);// 菜单类型
        Intent intent = new Intent(context, StoreExpandActivity.class);// 跳转到上报页面
        intent.putExtra("bundle", bundle);
        context.startActivity(intent);

    }

    /**
     * 企业微信
     */
    private void weChat() {
        Intent intent = new Intent(context, WechatActivity.class);
        context.startActivity(intent);
    }

    /**
     * 调查问卷
     */
    private void question() {
        Intent intent = new Intent(context, QuestionnaireActivity.class);
        context.startActivity(intent);
    }


    /**
     * 通讯录
     */
    private void addressBook() {
        Intent intent = new Intent(context, AddressBookActivity.class);
        context.startActivity(intent);
    }

    /**
     * 公司指定的webview展示页面
     */
    private void loadCompanyWeb(Menu menu) {
        Intent intent = new Intent(context, CompanyWebActivity.class);
        intent.putExtra("company_url", "http://mail.himin.com");
        intent.putExtra("company_name", menu.getName());
        // Intent intent = new Intent();
        // intent.setAction("android.intent.action.VIEW");
        // Uri content_url = Uri.parse("http://mail.himin.com");
        // intent.setData(content_url);
        context.startActivity(intent);
    }


    private void webReport(int menuId) {
        String userId = new String(
                Base64.encodeBytes(String.valueOf(SharedPreferencesUtil.getInstance(context).getUserId()).getBytes()));
        String url = null;
        if (menuId == 2010286) {// 数据汇总
            url = "http://report.gcgcloud.com/gcgreport/ReportServer?reportlet=xiaoyuan%2FUserMobile.cpt";
        } else if (menuId == 2010287) {// 巡店照片
            url = "http://report.gcgcloud.com/gcgreport/ReportServer?reportlet=xiaoyuan%2FSchoolPhoto.cpt";
        } else if (menuId == 2010557) {// 数据汇总(省)
            url = "http://report.gcgcloud.com/gcgreport/ReportServer?reportlet=xiaoyuan%2FUserMobileByProvince.cpt&userId="
                    + userId;
        } else if (menuId == 2010558) {// 巡店照片(省)
            url = "http://report.gcgcloud.com/gcgreport/ReportServer?reportlet=xiaoyuan%2FSchoolPhotoByProvince.cpt&userId="
                    + userId;
        } else if (menuId == 2010559) {// 数据汇总(市)
            url = "http://report.gcgcloud.com/gcgreport/ReportServer?reportlet=xiaoyuan%2FUserMobileByCity.cpt&userId="
                    + userId;
        } else if (menuId == 2010560) {// 巡店照片(市)
            url = "http://report.gcgcloud.com/gcgreport/ReportServer?reportlet=xiaoyuan%2FSchoolPhotoByCity.cpt&userId="
                    + userId;
        } else if (menuId == 2010561) {// 数据汇总(县)
            url = "http://report.gcgcloud.com/gcgreport/ReportServer?reportlet=xiaoyuan%2FUserMobileByCounty.cpt&userId="
                    + userId;
        } else if (menuId == 2010562) {// 巡店照片(县)
            url = "http://report.gcgcloud.com/gcgreport/ReportServer?reportlet=xiaoyuan%2FSchoolPhotoByCounty.cpt&userId="
                    + userId;
        } else if (menuId == 2010563) {
            url = "http://report.gcgcloud.com/gcgreport/ReportServer?reportlet=xiaoyuan%2Ftemp2010563.cpt&userId="
                    + userId;
        } else if (menuId == 2010564) {
            url = "http://report.gcgcloud.com/gcgreport/ReportServer?reportlet=xiaoyuan%2Ftemp2010564.cpt&userId="
                    + userId;
        } else if (menuId == 2010565) {
            url = "http://report.gcgcloud.com/gcgreport/ReportServer?reportlet=xiaoyuan%2Ftemp2010565.cpt&userId="
                    + userId;
        } else if (menuId == 2010566) {
            url = "http://report.gcgcloud.com/gcgreport/ReportServer?reportlet=xiaoyuan%2Ftemp2010566.cpt&userId="
                    + userId;
        } else if (menuId == 2010567) {
            url = "http://report.gcgcloud.com/gcgreport/ReportServer?reportlet=xiaoyuan%2Ftemp2010567.cpt&userId="
                    + userId;
        } else if (menuId == 2010568) {
            url = "http://report.gcgcloud.com/gcgreport/ReportServer?reportlet=xiaoyuan%2Ftemp2010568.cpt&userId="
                    + userId;
        } else if (menuId == 2010569) {
            url = "http://report.gcgcloud.com/gcgreport/ReportServer?reportlet=xiaoyuan%2Ftemp2010569.cpt&userId="
                    + userId;
        } else if (menuId == 2010570) {
            url = "http://report.gcgcloud.com/gcgreport/ReportServer?reportlet=xiaoyuan%2Ftemp2010570.cpt&userId="
                    + userId;
        } else if (menuId == 2010571) {
            url = "http://report.gcgcloud.com/gcgreport/ReportServer?reportlet=xiaoyuan%2Ftemp2010571.cpt&userId="
                    + userId;
        } else if (menuId == 2010572) {
            url = "http://report.gcgcloud.com/gcgreport/ReportServer?reportlet=xiaoyuan%2Ftemp2010572.cpt&userId="
                    + userId;
        } else if (menuId == 2010573) {
            url = "http://report.gcgcloud.com/gcgreport/ReportServer?reportlet=xiaoyuan%2Ftemp2010573.cpt&userId="
                    + userId;
        } else if (menuId == 2010574) {
            url = "http://report.gcgcloud.com/gcgreport/ReportServer?reportlet=xiaoyuan%2Ftemp2010574.cpt&userId="
                    + userId;
        } else if (menuId == 2010676) {
            url = "http://report.gcgcloud.com/gcgreport/ReportServer?reportlet=xiaoyuan%2Ftemp2010676.cpt&userId="
                    + userId;
        } else if (menuId == 2010677) {
            url = "http://report.gcgcloud.com/gcgreport/ReportServer?reportlet=xiaoyuan%2Ftemp2010677.cpt&userId="
                    + userId;
        } else if (menuId == 2010678) {
            url = "http://report.gcgcloud.com/gcgreport/ReportServer?reportlet=xiaoyuan%2Ftemp2010678.cpt&userId="
                    + userId;
        } else if (menuId == 2010679) {
            url = "http://report.gcgcloud.com/gcgreport/ReportServer?reportlet=xiaoyuan%2Ftemp2010679.cpt&userId="
                    + userId;
        } else if (menuId == 2010680) {
            url = "http://report.gcgcloud.com/gcgreport/ReportServer?reportlet=xiaoyuan%2Ftemp2010680.cpt&userId="
                    + userId;
        } else if (menuId == 2010681) {
            url = "http://report.gcgcloud.com/gcgreport/ReportServer?reportlet=xiaoyuan%2Ftemp2010681.cpt&userId="
                    + userId;
        } else if (menuId == 2010682) {
            url = "http://report.gcgcloud.com/gcgreport/ReportServer?reportlet=xiaoyuan%2Ftemp2010682.cpt&userId="
                    + userId;
        } else if (menuId == 2010683) {
            url = "http://report.gcgcloud.com/gcgreport/ReportServer?reportlet=xiaoyuan%2Ftemp2010683.cpt&userId="
                    + userId;
        } else if (menuId == 2010684) {
            url = "http://report.gcgcloud.com/gcgreport/ReportServer?reportlet=xiaoyuan%2Ftemp2010684.cpt&userId="
                    + userId;
        } else if (menuId == 2010685) {
            url = "http://report.gcgcloud.com/gcgreport/ReportServer?reportlet=xiaoyuan%2Ftemp2010685.cpt&userId="
                    + userId;
        } else if (menuId == 2010686) {
            url = "http://report.gcgcloud.com/gcgreport/ReportServer?reportlet=xiaoyuan%2Ftemp2010686.cpt&userId="
                    + userId;
        } else if (menuId == 2010687) {
            url = "http://report.gcgcloud.com/gcgreport/ReportServer?reportlet=xiaoyuan%2Ftemp2010687.cpt&userId="
                    + userId;
        }

        Intent intent = new Intent(context, WebViewUtilActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

}
