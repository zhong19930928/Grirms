package com.yunhu.yhshxc.activity;

/**
 * brook
 */

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.MeetingAgenda.FirstEvent;
import com.yunhu.yhshxc.MeetingAgenda.MeetingAgendaFragment;
import com.yunhu.yhshxc.MeetingAgenda.MeetingagendaListActivity;
import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.fragment.AddressBFragment;
import com.yunhu.yhshxc.activity.fragment.Connectivities;
import com.yunhu.yhshxc.activity.fragment.Connectivities.ConnectivityChangeReceiver;
import com.yunhu.yhshxc.activity.fragment.MessageFragment;
import com.yunhu.yhshxc.activity.fragment.PersonalFragment;
import com.yunhu.yhshxc.activity.fragment.ReportFragment;
import com.yunhu.yhshxc.application.SoftApplication;
import com.yunhu.yhshxc.attendance.AttendanceFragment;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.bo.SubmitItem;
import com.yunhu.yhshxc.database.MainMenuDB;
import com.yunhu.yhshxc.database.SubmitItemDB;
import com.yunhu.yhshxc.style.StatusBarUtil;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.FileHelper;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil2;
import com.yunhu.yhshxc.workplan.WorkPlanCheckService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


/**
 * 主Activity,包含:工作台,考勤,消息,我,四大fragment.
 */
public class HomePageActivity extends Activity {

    private AlertDialog dialog = null;
    private Context context;
    private PersonalFragment personalFragment;
    private FragmentManager fm;
    /**
     * 工作台
     */
    private ImageView image_recovery;
    /**
     * 考勤
     */
    private ImageView image_promotion;
    /**
     * 报表
     */
    private ImageView image_phonereport;
    private ReportFragment reportFragment;
    /**
     * 消息
     */
    private ImageView image_others;
    /**
     * 我
     */
    private ImageView image_personal,image_rcp,image_txl;
    public HomeMenuFragment homeFragment;

    private AddressBFragment addressBFragment;

    private AttendanceFragment attendanceNewActivity;

    private MessageFragment meetingAgendaFragment;
    private ImageView image_p;
    private MyBroadcast myBroadcast;
    private MainMenuDB mainMenuDB;
    private Fragment currentFragment;
    private String attendanceName;
    private static Menu menu;

	private SoftApplication application;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            StatusBarUtil.setColor(this, Color.parseColor("#6383AD"));
        }
		context = this;
		setContentView(R.layout.homepage);
		application = (SoftApplication)getApplication();
		registerReceiver(receiver, Connectivities.ConnectivityChangeReceiver.FILTER);
		mainMenuDB = new MainMenuDB(this);
        EventBus.getDefault().register(this);
		fm = getFragmentManager();
		setDefaultFragment();

        Intent intent = this.getIntent();
        // 强制关闭
        if (intent != null && intent.getIntExtra("CLOSE", 0) == 1) {
            this.finish();
        }

        image_recovery = (ImageView) findViewById(R.id.image_recovery);
        image_promotion = (ImageView) findViewById(R.id.image_promotion);
        image_others = (ImageView) findViewById(R.id.image_others);//
        image_personal = (ImageView) findViewById(R.id.image_personal);
        image_p = (ImageView) findViewById(R.id.image_p);
        image_phonereport = (ImageView) findViewById(R.id.image_phonereport);
        image_rcp = (ImageView)findViewById(R.id.image_rcp);
        image_txl = (ImageView)findViewById(R.id.image_txl);
        // 工作台
        findViewById(R.id.recovery).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                // image_recovery
                image_recovery.setBackgroundResource(R.drawable.ic_dashboard_grey600_36dp);
                image_promotion.setBackgroundResource(R.drawable.kaoqin1);
                image_others.setBackgroundResource(R.drawable.ic_question_answer_black_36dp);
                image_personal.setBackgroundResource(R.drawable.ic_person_black_36dp);
                image_phonereport.setBackgroundResource(R.drawable.phone_report1);
                image_txl.setBackgroundResource(R.drawable.ic_txl_gray);
                image_rcp.setBackgroundResource(R.drawable.ic_rc_gray);

                if (homeFragment == null)
                    homeFragment = new HomeMenuFragment();

                switchContent(currentFragment,homeFragment);
            }
        });
        // 考勤
        menu = mainMenuDB.findMenuListByMenuType(Menu.TYPE_NEW_ATTENDANCE);
        if (null == menu) {
            findViewById(R.id.promotion).setVisibility(View.GONE);
        } else {
            findViewById(R.id.promotion).setVisibility(View.VISIBLE);

            TextView text_promotion = (TextView) findViewById(R.id.text_promotion);
            if (menu.getName() != null && !menu.equals("")) {
                attendanceName = SharedPreferencesUtil.getInstance(context).getFixedName().containsKey(3)
                        ? SharedPreferencesUtil.getInstance(context).getFixedName().get(3) : menu.getName();
                text_promotion.setText(attendanceName);
            }

        }
        findViewById(R.id.promotion).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (homeFragment != null && homeFragment.isEnable) {
                    // image_promotion
                    image_recovery.setBackgroundResource(R.drawable.ic_dashboard_black_36dp);
                    image_promotion.setBackgroundResource(R.drawable.kaoqin2);
                    image_others.setBackgroundResource(R.drawable.ic_question_answer_black_36dp);
                    image_personal.setBackgroundResource(R.drawable.ic_person_black_36dp);
                    image_phonereport.setBackgroundResource(R.drawable.phone_report1);
                    if (attendanceNewActivity == null) {

                        attendanceNewActivity = new AttendanceFragment();

                        Bundle args = new Bundle();
                        args.putString("name", attendanceName);
                        attendanceNewActivity.setArguments(args);
                    }
                    switchContent(currentFragment,attendanceNewActivity);
                }
            }
        });
        if (SharedPreferencesUtil2.getInstance(this).getPhoneReport()) {// 如果配置了拜访或者考勤模块,就可以显示报表
            // if (true) {// 如果配置了拜访或者考勤模块,就可以显示报表
            // 报表
            findViewById(R.id.phonereport).setVisibility(View.VISIBLE);
            findViewById(R.id.phonereport).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (homeFragment != null && homeFragment.isEnable) {

                        image_recovery.setBackgroundResource(R.drawable.ic_dashboard_black_36dp);
                        image_promotion.setBackgroundResource(R.drawable.kaoqin1);
                        image_others.setBackgroundResource(R.drawable.ic_question_answer_black_36dp);
                        image_personal.setBackgroundResource(R.drawable.ic_person_black_36dp);
                        image_phonereport.setBackgroundResource(R.drawable.phone_report2);
                        if (reportFragment == null) {
                            reportFragment = new ReportFragment();
                        }
                        switchContent(currentFragment,reportFragment);
                    }
                }
            });

        }
        //通讯录
        findViewById(R.id.txl).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                image_recovery.setBackgroundResource(R.drawable.ic_dashboard_black_36dp);
                image_promotion.setBackgroundResource(R.drawable.kaoqin1);
                image_others.setBackgroundResource(R.drawable.ic_question_answer_black_36dp);
                image_personal.setBackgroundResource(R.drawable.ic_person_black_36dp);
                image_phonereport.setBackgroundResource(R.drawable.phone_report1);
                image_rcp.setBackgroundResource(R.drawable.ic_rc_gray);
                image_txl.setBackgroundResource(R.drawable.ic_txl_blue);
                if(addressBFragment==null){
                    addressBFragment = new AddressBFragment();
                }
                switchContent(currentFragment,addressBFragment);
            }
        });
        //朋友圈
        findViewById(R.id.home_rc_rl).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                image_recovery.setBackgroundResource(R.drawable.ic_dashboard_black_36dp);
                image_promotion.setBackgroundResource(R.drawable.kaoqin1);
                image_others.setBackgroundResource(R.drawable.ic_question_answer_black_36dp);
                image_personal.setBackgroundResource(R.drawable.ic_person_black_36dp);
                image_phonereport.setBackgroundResource(R.drawable.phone_report1);
                image_rcp.setBackgroundResource(R.drawable.ic_rc_blue);
                image_txl.setBackgroundResource(R.drawable.ic_txl_gray);

                if (meetingAgendaFragment ==null)
                    meetingAgendaFragment = new MessageFragment();
                switchContent(currentFragment,meetingAgendaFragment);
            }
        });

        // 消息
        findViewById(R.id.others).setOnClickListener(new View.OnClickListener() {

            private MessageFragment messageFragment;

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (homeFragment != null && homeFragment.isEnable) {

                    image_p.setVisibility(View.GONE);

                    // image_others
                    image_recovery.setBackgroundResource(R.drawable.ic_dashboard_black_36dp);
                    image_promotion.setBackgroundResource(R.drawable.kaoqin1);
                    image_others.setBackgroundResource(R.drawable.ic_question_answer_grey600_36dp);
                    image_personal.setBackgroundResource(R.drawable.ic_person_black_36dp);
                    image_phonereport.setBackgroundResource(R.drawable.phone_report1);
                    image_txl.setBackgroundResource(R.drawable.ic_txl_gray);
                    image_rcp.setBackgroundResource(R.drawable.ic_rc_gray);
                    if (messageFragment == null)
                        messageFragment = new MessageFragment();

                    switchContent(currentFragment,messageFragment);
                }
            }
        });
        // 我
        findViewById(R.id.personal_info).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (homeFragment != null && homeFragment.isEnable) {

                    // image_personal
                    image_recovery.setBackgroundResource(R.drawable.ic_dashboard_black_36dp);
                    image_promotion.setBackgroundResource(R.drawable.kaoqin1);
                    image_others.setBackgroundResource(R.drawable.ic_question_answer_black_36dp);
                    image_personal.setBackgroundResource(R.drawable.ic_person_grey600_36dp);
                    image_phonereport.setBackgroundResource(R.drawable.phone_report1);
                    image_txl.setBackgroundResource(R.drawable.ic_txl_gray);
                    image_rcp.setBackgroundResource(R.drawable.ic_rc_gray);
                    if (personalFragment == null)
                        personalFragment = new PersonalFragment();
                    switchContent(currentFragment,personalFragment);
                }
            }
        });

        myBroadcast = new MyBroadcast();
        IntentFilter inFilter = new IntentFilter();
        inFilter.addAction("new_message");
        inFilter.addAction("cancle_type_wechat_redpoint");
        context.registerReceiver(myBroadcast, inFilter);
        // context.registerReceiver(myBroadcast, new
        // IntentFilter("new_message"));

        // ToastOrder.makeText(context,
        // SharedPreferencesUtil.getInstance(context).getString("comFilesname0"),
        // ToastOrder.LENGTH_LONG).show();
        // 检测工作计划是够需要提醒
        checkWorkplanTip();

    }

    public void switchContent(Fragment from, Fragment to) {
	    if(currentFragment==null){
            FragmentTransaction transaction = fm.beginTransaction();
        }else if (currentFragment != to) {
            currentFragment = to;
            FragmentTransaction transaction = fm.beginTransaction();
            if (!to.isAdded()) {    // 先判断是否被add过
                transaction.hide(from).add(R.id.content, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }

    }

    /**
     * 检查今天是否有需要提醒的工作计划
     */
    private void checkWorkplanTip() {
        // 首先查询配置文件中是否记录上一次提醒时间,如果为空则进行联网获取
        String tipTime = SharedPreferencesUtil2.getInstance(context).getWorkPlanTipTime();
        if (!TextUtils.isEmpty(tipTime)) {
            // 不为空则比较当前时间,如果跨天则进行联网获取,在弹出提醒后重置配置提醒时间
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date lastTime = sdf.parse(tipTime);// 上次提示时间
                String nowStr = sdf.format(new Date(System.currentTimeMillis()));
                Date nowTime = sdf.parse(nowStr);

                if (nowTime.after(lastTime)) {
                    // 跨天初始化
                    checkIntenetForWork();
                } else {
                    // 未跨天不做提示
                }

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else {
            checkIntenetForWork();

        }

    }

    /**
     * 开启服务获取并进行提醒
     */
    private void checkIntenetForWork() {
        // 再去判断时间段,是否满足提醒规则
        String timeRule = SharedPreferencesUtil2.getInstance(this).getWorkPlanTipRule();
        if (!TextUtils.isEmpty(timeRule)) {
            String[] timeRs = timeRule.split("-");
            int startHour = Integer.parseInt(timeRs[0]);
            int endHour = Integer.parseInt(timeRs[1]);
            Calendar cc = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
            cc.setTime(new Date(System.currentTimeMillis()));
            int nowHour = cc.get(Calendar.HOUR_OF_DAY);
            if (nowHour > startHour && nowHour < endHour) {
                Intent intent = new Intent(context, WorkPlanCheckService.class);
                intent.setAction("start_getworkplan");
                // Log.e("view", "开启服务获取提醒信息");
                context.startService(intent);
            }

        } else {
            Intent intent = new Intent(context, WorkPlanCheckService.class);
            intent.setAction("start_getworkplan");
            // Log.e("view", "开启服务获取提醒信息");
            context.startService(intent);
        }

    }

    // 默认显示的fragment
    private void setDefaultFragment() {
        personalFragment = new PersonalFragment();
        homeFragment = new HomeMenuFragment();
        currentFragment = homeFragment;
        fm.beginTransaction().replace(R.id.content, homeFragment).commit();
        // text_recovery.setTextColor(getResources().getColor(R.color.blue3));
        // image_recovery.setBackgroundResource(R.drawable.homepage_icon_recovery_checked);
    }

	@Override
	protected void onDestroy() {
		// Activity销毁前必须调用TitleBar.unregister()
		// if (titleBar != null) {
		// titleBar.unregister();
		// }
        EventBus.getDefault().unregister(this);
		unregisterReceiver(receiver);
		cleanTempPicture();// 清除本地临时缓存的图片
		context.unregisterReceiver(myBroadcast);
		application.setHomePageActivity(null);
		super.onDestroy();
	}

    /**
     * 清除缓存图片(在线程中进行)
     */
    private void cleanTempPicture() {

        new Thread() {
            @Override
            public void run() {

                FileHelper fileHelper = new FileHelper();
                fileHelper.delAllFile(Constants.USERIV_PATH);
                fileHelper.delAllFile(Constants.CONTENTIV_PATH);
                fileHelper.delAllFile(Constants.TEMP_IMAGE_PATH);
                List<SubmitItem> photoItemList = new SubmitItemDB(context).findPhotoSubmitItem();
                List<String> needSubmitPhoto = new ArrayList<String>();
                if (!photoItemList.isEmpty()) {
                    for (int i = 0; i < photoItemList.size(); i++) {
                        needSubmitPhoto.add(photoItemList.get(i).getParamValue());
                    }
                }
                // 清除录音文件
                List<SubmitItem> recordItemList = new SubmitItemDB(context).findSubmitItemByType(Func.TYPE_RECORD);
                List<String> needSubmitRecord = new ArrayList<String>();
                if (!recordItemList.isEmpty()) {
                    for (int i = 0; i < recordItemList.size(); i++) {
                        needSubmitRecord.add(recordItemList.get(i).getParamValue());
                    }
                }
                File recordFile = new File(Constants.RECORD_PATH);
                String[] tempRecordList = recordFile.list();// 路径下的所有文件
                if (tempRecordList != null && tempRecordList.length > 0) {
                    for (int i = 0; i < tempRecordList.length; i++) {
                        File tempFile = new File(Constants.RECORD_PATH + tempRecordList[i]);
                        if (tempFile.isFile() && tempFile.getName().endsWith("3gp")) {
                            if (!needSubmitRecord.contains(tempFile.getName())) {// 说明是无用的图片要清除
                                tempFile.delete();
                            }
                        }
                    }
                }

                SystemClock.sleep(10000);
                File photoFile = new File(Constants.SDCARD_PATH);
                String[] tempList = photoFile.list();
                if (tempList.length > 0) {
                    for (int i = 0; i < tempList.length; i++) {
                        File tempFile = new File(Constants.SDCARD_PATH + tempList[i]);
                        if (tempFile.isFile() && tempFile.getName().endsWith("jpg")) {
                            if (!needSubmitPhoto.contains(tempFile.getName())) {// 说明是无用的图片要清除
                                tempFile.delete();
                            }
                        }
                    }
                }

            }

        }.start();
    }

    // /**
    // * 从数据库获取当前用户可以使用的所有菜单项数据，并将数据保存到menuList中
    // */
    // private void getDataSrc() {
    // menuList = new MainMenuDB(context).findAllMenuList();
    // adapter.setDataSrc(menuList);
    // }
    @Override
    protected void onResume() {
        super.onResume();
        // 获取有效开始日期
        String usefulStartData = SharedPreferencesUtil.getInstance(context).getUsefulStartDate();
        // 获取有效结束日期
        String usefulEndData = SharedPreferencesUtil.getInstance(context).getUsefulEndDate();
        // Log.d(TAG, "有效日期:" + usefulStartData+"==>"+usefulEndData);
        // 获取手机当前时间戳
        Date nowDate = DateUtil.getDate(DateUtil.getCurDate(), DateUtil.DATAFORMAT_STR);
        if (TextUtils.isEmpty(usefulStartData) || TextUtils.isEmpty(usefulEndData)
                || nowDate.before(DateUtil.getDate(usefulStartData.trim(), DateUtil.DATAFORMAT_STR))
                || nowDate.after(DateUtil.getDate(usefulEndData.trim(), DateUtil.DATAFORMAT_STR))) {
            outOfdateDialog();// 提示日期过期的Dialog;
        } else {
            //如果是合并版后者捷思锐版 不检查升级
            if (!PublicUtils.ISCOMBINE
                    ||!PublicUtils.getBaseUrl(context).equals(context.getApplicationContext().getResources().getString(R.string.JieSiRui_URL))) {
//                PublicUtils.updateApp(context);// 检测升级应用
            }
        }
        // cleanDataMethodByNoSubmit();// 清除未提交的数据 清除本地残留的数据和图片
        if (homeFragment != null && homeFragment.isAdded()) {
            if (homeFragment.alDialog != null && homeFragment.alDialog.isShowing()) {

            } else {
                homeFragment.resetData();
            }

        }
    }

    /**
     * 弹出用户手机号未被授权使用的模态对话框，用户只能点击确定，确定后将退出
     */
    private void outOfdateDialog() {
        AlertDialog dialog = new AlertDialog.Builder(context).setIcon(android.R.drawable.btn_star).setTitle(PublicUtils.getResourceString(HomePageActivity.this,R.string.tip))
                .setMessage(PublicUtils.getResourceString(context,R.string.unkown_pho))// 有效日期已过期.
                .setPositiveButton(PublicUtils.getResourceString(context,R.string.zhifuqueren), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).create();
        dialog.setCancelable(false);// 将对话框设置为模态
        dialog.show();
    }

    /**
     * 用户按下返回键时的事件处理（也可以通过覆盖Activity.onBackPressed()方法来响应返回键的事件）
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                onClickBack();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 手机段可是用时间段提示
     */
    private LinearLayout ll_tip;

    private void showTip(String info) {
        ll_tip = (LinearLayout) findViewById(R.id.ll_menu_usable_tip);
        TextView tv_Tip = (TextView) findViewById(R.id.tv_menu_usable_tip);
        ll_tip.setVisibility(View.VISIBLE);
        tv_Tip.setText(info);
        ll_tip.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ll_tip.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 用户按下返回键的事件处理：弹出对话框，询问用户是否退出
     */
    public void onClickBack() {

        if (ll_tip != null && ll_tip.isShown()) {
            ll_tip.setVisibility(View.GONE);
        } else {
            dialog = backDialog(PublicUtils.getResourceString(HomePageActivity.this,R.string.is_exit) + getString(R.string.app_normalname) + "?");
            dialog.show();
        }
    }

    /**
     * 创建提示用户是否退出的模态对话框，如果用户选择退出，则清除本地临时缓存的图片
     * @param bContent 询问用户的文字
     * @return 返回一个构造好的AlertDialog对象
     */
    private AlertDialog backDialog(String bContent) {
        AlertDialog.Builder etBuilder = new AlertDialog.Builder(HomePageActivity.this, R.style.NewAlertDialogStyle);
        etBuilder.setTitle(context.getResources().getString(R.string.TIP));// context.getResources().getString(R.string.tip);
        etBuilder.setMessage(bContent);
        etBuilder.setIcon(R.drawable.exit_dialog_icon);
        etBuilder.setNegativeButton(context.getResources().getString(R.string.Cancle),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        dialog.dismiss();

                    }
                });
        etBuilder.setPositiveButton(context.getResources().getString(R.string.Confirm),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                        HomePageActivity.this.finish();
                    }
                });
        AlertDialog dialog = etBuilder.create();
        dialog.setCancelable(false);

        return dialog;
    }

    class MyBroadcast extends BroadcastReceiver {

        @SuppressWarnings("deprecation")
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("new_message")) {
                image_p.setVisibility(View.VISIBLE);
                context.removeStickyBroadcast(intent);
            } else if (intent.getAction().equals("cancle_type_wechat_redpoint")) {
                image_p.setVisibility(View.GONE);
            }
        }

    }

    public void refreshUI() {
        Menu menu = mainMenuDB.findMenuListByMenuType(Menu.TYPE_NEW_ATTENDANCE);
        if (null == menu) {
            findViewById(R.id.promotion).setVisibility(View.GONE);
        } else {
            findViewById(R.id.promotion).setVisibility(View.VISIBLE);
        }
    }

    private Connectivities.ConnectivityChangeReceiver receiver = new ConnectivityChangeReceiver() {

        @Override
        protected void onDisconnected() {
            if (attendanceNewActivity != null && currentFragment == attendanceNewActivity) {
                attendanceNewActivity.isEnableNetWork = false;
                attendanceNewActivity.setNewAttendTime();
            }
        }

        @Override
        protected void onConnected() {
            if (attendanceNewActivity != null && currentFragment == attendanceNewActivity) {
                attendanceNewActivity.isEnableNetWork = false;
                attendanceNewActivity.setNewAttendTime();
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(homeFragment!=null){
            homeFragment.onActivityResult(requestCode,resultCode,data);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void first(FirstEvent event){
        Intent intent = new Intent(HomePageActivity.this, MeetingagendaListActivity.class);
        startActivity(intent);
    }
}
