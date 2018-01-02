package com.yunhu.yhshxc.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.carSales.util.SharedPreferencesForCarSalesUtil;
import com.yunhu.yhshxc.activity.synchrodata.SyncInitData;
import com.yunhu.yhshxc.activity.todo.Todo;
import com.yunhu.yhshxc.activity.todo.TodoListActivity;
import com.yunhu.yhshxc.attendance.SharedPrefsAttendanceUtil;
import com.yunhu.yhshxc.attendance.util.SharedPreferencesForLeaveUtil;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.customMade.xiaoyuan.SharedPreferencesUtilForXiaoYuan;
import com.yunhu.yhshxc.database.DatabaseHelper;
import com.yunhu.yhshxc.database.MainMenuDB;
import com.yunhu.yhshxc.database.TablePendingDB;
import com.yunhu.yhshxc.database.TodoDB;
import com.yunhu.yhshxc.help.HelpPopupWindow;
import com.yunhu.yhshxc.order2.SharedPreferencesForOrder2Util;
import com.yunhu.yhshxc.order3.util.SharedPreferencesForOrder3Util;
import com.yunhu.yhshxc.submitManager.SubmitManagerActivity;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.FileHelper;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil2;
import com.yunhu.yhshxc.utility.SharedPreferencesUtilForNearby;
import com.yunhu.yhshxc.utility.SharedPreferencesUtilForTable;
import com.yunhu.yhshxc.wechat.Util.SharedPrefrencesForWechatUtil;
import com.yunhu.yhshxc.widget.ToastOrder;

import java.util.List;


/**
 * Created by suhu on 2017/5/6.
 */

public class PersonalSettingActivity extends AbsBaseActivity {

    private TextView companyPhone;
    private TextView tvClearData;
    private Context context;
    private LinearLayout linearLayout;
    private View rootView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_setting);
        init();
    }


    private void init() {
        linearLayout = (LinearLayout) findViewById(R.id.linear_layout);
        rootView = linearLayout;
        context = this;
        companyPhone = (TextView) findViewById(R.id.person_info_company_phone);
        final String phoneNumber = SharedPreferencesUtil.getInstance(context).getHelpTel();
        if (!TextUtils.isEmpty(phoneNumber)) {
            if (PublicUtils.ISDEMO) {
                companyPhone.setText(PublicUtils.getResourceString(this,R.string.company_number)+"010-11111111");
            } else {
                companyPhone.setText(PublicUtils.getResourceString(this,R.string.company_number) + phoneNumber);

            }
        }
        View web = findViewById(R.id.btn_web_backstage);
        if (PublicUtils.ISDEMO) {
            web.setVisibility(View.GONE);
        } else {
            web.setVisibility(View.GONE);
        }
        web.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("http://wqshxc.grirms.com/com.grandison.grirms.manager.web-1.0.0/login.do?companyNameJc=巡检演示&userName=巡检员&userPassWord=grms123&companyName=wqshxc");
                intent.setData(content_url);
                startActivity(intent);
            }
        });
        tvClearData = (TextView) findViewById(R.id.tv_cleat_data);
        if (PublicUtils.ISCOMBINE && PublicUtils.ISDEMO) {
            tvClearData.setText(PublicUtils.getResourceString(this,R.string.change_version));
        } else if (PublicUtils.ISCOMBINE && !PublicUtils.ISDEMO) {
            tvClearData.setText(PublicUtils.getResourceString(this,R.string.change_version1));
        } else if (!PublicUtils.ISCOMBINE) {
            tvClearData.setText(PublicUtils.getResourceString(this,R.string.person_info_cleardata));
        }
        findViewById(R.id.clear_all_data).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {//清除配置,清除数据库表
                AlertDialog dialog = new AlertDialog.Builder(context, R.style.NewAlertDialogStyle)
                        .setTitle(PublicUtils.getResourceString(context,R.string.dialog_one)).setMessage(PublicUtils.getResourceString(context,R.string.change_version2))
                        .setIcon(R.drawable.warning_icon)
                        .setPositiveButton(PublicUtils.getResourceString(context,R.string.Confirm), new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    getSharedPreferences(PublicUtils.PREFERENCE_NAME, Context.MODE_PRIVATE).edit().clear().commit();
                                    SharedPreferencesUtil.getInstance(context).clearAll();
                                    SharedPreferencesUtil2.getInstance(context).clearAll();
                                    SharedPreferencesUtilForNearby.getInstance(context).clearAll();
                                    SharedPreferencesUtilForTable.getInstance(context).clearAll();
                                    SharedPreferencesUtilForXiaoYuan.getInstance(context).clearAll();
                                    SharedPreferencesForCarSalesUtil.getInstance(context).clearAll();
                                    SharedPreferencesForLeaveUtil.getInstance(context).clearAll();
                                    SharedPrefrencesForWechatUtil.getInstance(context).clearAll();
                                    SharedPreferencesForOrder2Util.getInstance(context).clearAll();
                                    SharedPreferencesForOrder3Util.getInstance(context).clearAll();
                                    SharedPrefsAttendanceUtil.getInstance(context).clearAll();
                                    DatabaseHelper.getInstance(context).deteleAllTable();

//				           					startActivity(new Intent(context,LoginForFreeActivity.class));
                                    String cachePath = getApplicationContext().getCacheDir().toString();

                                    String path = cachePath.substring(0, cachePath.lastIndexOf("/"));
                                    FileHelper.deleteAllFile(path + "/cache");//清除缓存文件
                                    FileHelper.deleteAllFile(path + "/shared_prefs");//清除配置文件
//				                        	FileHelper.deleteAllFile(path+"/databases");//清除数据库文件
                                    FileHelper.deleteAllFile(Constants.SDCARD_PATH);//清除外部存储数据

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (PublicUtils.ISDEMO) {
                                    startActivity(new Intent(context, SplashNewActivity.class));
                                } else {
                                    if (PublicUtils.ISCOMBINE) {
                                        startActivity(new Intent(context, SplashNewActivity.class));
                                    } else {
                                        startActivity(new Intent(context, SplashActivity2.class));
                                    }

                                }
                                finish();

                            }
                        })
                        .setNegativeButton(PublicUtils.getResourceString(context,R.string.Cancle), null)
                        .setCancelable(false)
                        .create();
                dialog.show();


            }
        });

        //设置apk版本信息展示
        RelativeLayout btn_version = (RelativeLayout) findViewById(R.id.btn_version);
        btn_version.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //进入版本展示页面
                startActivity(new Intent(PersonalSettingActivity.this, ApkVersionActivity.class));

            }
        });

        //拨打客服电话
        findViewById(R.id.btn_open_telephone).setOnClickListener(new View.OnClickListener() {//未提交数据

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String phoneUri = "tel:" + phoneNumber;
                if (PublicUtils.ISDEMO) {
                    phoneUri = "tel:01011111111";
                }
                try {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(phoneUri));
                    startActivity(intent);
                } catch (Exception e) {

                }
            }
        });

        findViewById(R.id.person_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //未提交数据
        findViewById(R.id.btn_unupload).setOnClickListener(new View.OnClickListener() {//未提交数据

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent1 = new Intent(context, SubmitManagerActivity.class);
                context.startActivity(intent1);
            }
        });
        //资料库
       findViewById(R.id.btn_com_files).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent1 = new Intent(context, CompanyFilesActivity.class);
                context.startActivity(intent1);
            }
        });

        //同步
        findViewById(R.id.btn_synchronization).setOnClickListener(new View.OnClickListener() {//同步

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                syncAll();

            }
        });
        if (isHasMenu(Menu.TYPE_TODO)) {


            //待办事项
            RelativeLayout toDO = (RelativeLayout) findViewById(R.id.btn_todo);
            toDO.setVisibility(View.VISIBLE);
            //待办事项条数
            TextView toDoCount = (TextView) findViewById(R.id.todo_numbers);

            int toDoNumbers = toDoNumbers();
            if (toDoNumbers > 0) {
                //待办事项数
                toDoCount.setVisibility(View.VISIBLE);
                toDoCount.setText(String.valueOf(toDoNumbers));

            } else {
                toDoCount.setVisibility(View.GONE);
                toDoCount.setText("0");

            }
            toDO.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent2 = new Intent(context, TodoListActivity.class);
                    context.startActivity(intent2);

                }
            });

        }

        if (isHasMenu(Menu.TYPE_HELP)) {


            RelativeLayout help = (RelativeLayout) findViewById(R.id.btn_help);
            help.setVisibility(View.VISIBLE);
            //帮助
            help.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    new HelpPopupWindow(context).show(rootView);//基于home页的GridView组件显示弹出窗口
                }
            });
        }
    }

    private void syncAll() {
        int unSumitCount = new TablePendingDB(context).tablePendingCountNotErrorServer();
        if (unSumitCount > 0) {
            ToastOrder.makeText(context, R.string.un_submit_data, ToastOrder.LENGTH_LONG).show();
        } else {
            new SyncInitData(context).syncAll();
        }
    }

    /**
     * 判断是否有type类型的menu
     *
     * @return none
     */
    private boolean isHasMenu(int type) {
        Menu menu = new MainMenuDB(context).findMenuListByMenuType(type);
        boolean flag = menu == null ? false : true;
        return flag;
    }

    /**
     * 待办事项条数
     */
    private int toDoNumbers() {
        int number = 0;
        List<Todo> list = new TodoDB(context).findAllTodo();
        for (int i = 0; i < list.size(); i++) {
            Todo todo = list.get(i);
            number += todo.getTodoNum();
        }
        return number;
    }
}
