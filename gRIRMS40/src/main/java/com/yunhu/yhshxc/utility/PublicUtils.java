package com.yunhu.yhshxc.utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.Environment;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.provider.Settings;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.application.SoftApplication;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.database.DictDB;
import com.yunhu.yhshxc.exception.DataComputeException;
import com.yunhu.yhshxc.http.CoreHttpHelper;
import com.yunhu.yhshxc.http.download.apk.DownApkBackstageService;
import com.yunhu.yhshxc.location.backstage.SharedPrefsBackstageLocation;
import com.yunhu.yhshxc.submitManager.bo.PendingRequestVO;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gcg.org.debug.JLog;
import im.fir.sdk.FIR;

//import im.fir.sdk.FIR;

public class PublicUtils {

    public static final String PREFERENCE_NAME = "MOBILE_MDN_PREF";
    public static final String PREFERENCE_NAME_PHONE = "MOBILE_MDN_NO_PREF";
    public static final String RANDOM_CODE = "RANDOM_CODE";// 随机码
    public static boolean ISDEMO = false;// isDeomoApp true false
    public static final boolean ISCOMBINE = false;// 是否为合并版本 false 正常版本   true 合并版本(体验版登录Web后台,密码使用MD5加密,参数追加gf="",以便直接登录)

    public static final String COMBINE_TYPE = "xc"; // 公司区分，xs：销售版、pg：派工版、sh：售后版、xj：巡检版丶 xc:云狐石化现场丶 aj:云狐安监

    public static final String phone_xj = "17788880001";//云狐巡检 登录名：巡检员 密码：grms123   17788880001 英文版  17700000033 中文版
    public static final String phone_sh = "18600000011";//// 云狐售后 登录名：售后工程师 密码：22222@gcg 
    public static final String phone_pg = "17700000052";//// 云狐派工 登录名：派工员 密码：grms@gcgcloud
    public static final String phone_xc = "17700000027";//// 云狐现场 登录名：巡检工程师 密码：yfxj2017     17700000027 13511112222 总经理
    public static final String phone_xs = "17333333333";//// 云狐销售 登录名：销售业务员 密码：123456@gcg 
    public static final String[] phoneArr = new String[]{phone_xj, phone_sh, phone_pg, phone_xc, phone_xs};


    public static String receivePhoneNO(Context context) {
        String phoneNo = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
                .getString(PREFERENCE_NAME_PHONE, "");
        if (ISDEMO) {
            if (COMBINE_TYPE.equals("xj")) {
                phoneNo = phone_xj;
            } else if (COMBINE_TYPE.equals("sh")) {
                phoneNo = phone_sh;
            } else if (COMBINE_TYPE.equals("pg")) {
                phoneNo = phone_pg;
            } else if (COMBINE_TYPE.equals("xc")) {
                phoneNo = phone_xc;
            } else if (COMBINE_TYPE.equals("xs")) {
                phoneNo = phone_xs;
            }
        } else {
			 phoneNo = "15210718321";
//			 phoneNo = "15555555551";
//			 phoneNo = "15210718323";
//			phoneNo = "15555555551";
//			 phoneNo = "18511588782";//Brook
//			 phoneNo = "18822222222";
// 			 phoneNo = "13361939955";
//			 phoneNo = "13581721301";//赵玉飞
// 			 phoneNo = "13269273580";
//			 phoneNo = "13522188758";//王泽信
//			 phoneNo = "18513024040";//许庆利
//			 phoneNo = "13911111111";
//			 phoneNo = "18910528811";//王建伟
//		     phoneNo = "13811377814";
//			 phoneNo = "13811377814";//robin
//			 phoneNo = "17701050109";
//			 phoneNo = "18912345679";//王建伟
//			 phoneNo = "15910259764";//娄军港
//			 phoneNo = "15201162812";//苏虎
//			 phoneNo = "15201163703";//钟辉斌
//			 phoneNo = "17701345301";//张健
//			 phoneNo = "13305315338";//唐文生
//			 phoneNo = "17788880001";//巡检员A
//           phoneNo = "13305315338";

        }
        FIR.addCustomizeValue("phone", phoneNo);
        FIR.addCustomizeValue("company", phoneNo);
        return phoneNo;
    }

    public static String getBaseUrl(Context context) {
        return context.getApplicationContext().getResources().getString(R.string.BASE_URL);//BASE_URL TEST_URL SIMULATE_URL JieSiRui_URL
    }

    /**
     * 返回会议日程url
     * @param context
     * @return
     */
    public static String getMeetingBaseUrl(Context context) {
        return context.getApplicationContext().getResources().getString(R.string.MEETAGENDA_BASE_URL);//BASE_URL TEST_URL SIMULATE_URL JieSiRui_URL
    }


    public static String getIMSI(Context context) {

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imsi=null;
        try{
             imsi = tm.getSubscriberId();
            if (TextUtils.isEmpty(imsi)) {
                String model = android.os.Build.MODEL;
                if (model.contains("ViewSonic")) {
                    imsi = a8Imis(context);
                }
            }
        }catch (SecurityException e){

        }

        return imsi;
    }

    public static String getIMEI(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String IMEI = tm.getDeviceId();
        if (TextUtils.isEmpty(IMEI) || "null".equalsIgnoreCase(IMEI)) {
            IMEI = getUUID(context);
        }
        return IMEI;
    }

    public static String getPhoneNumber(Context context) {
        try{
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getLine1Number();
        }catch (SecurityException e){
            return null;
        }


    }

    public static String getSubscriberId(Context context) {// 返回IMSI
//		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//		String imsi = tm.getSubscriberId();
//		if (TextUtils.isEmpty(imsi)) {
//			String model = android.os.Build.MODEL;
//			if (model.contains("ViewSonic")) {
//				imsi = a8Imis(context);
//			}
//		}
//		 return imsi;

        return getAnroidID(context);
        // return "460036101443176";
    }

    public static String getDeviceId(Context context) {
//		 TelephonyManager tm = (TelephonyManager) context
//		 .getSystemService(Context.TELEPHONY_SERVICE);
//		 String IMEI = tm.getDeviceId();
//		 if (TextUtils.isEmpty(IMEI) || "null".equalsIgnoreCase(IMEI)) {
//		 IMEI = getUUID(context);
//		 }
//		 return IMEI;

        return getAnroidID(context);
        // return "a0000033016a5a";
        // return "99000550233078";
    }

    public static String getAnroidID(Context context) {

        String id = Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);

        if (id == null || id.equals("")) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            id = tm.getDeviceId();
        }
        return id;
    }

    /**
     * 随机码
     *
     * @return
     */
    public static String getRandomCode(Context context) {
        SharedPreferences spf = context.getApplicationContext().getSharedPreferences(PublicUtils.PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        String randomCode = spf.getString(RANDOM_CODE, "");
        if (TextUtils.isEmpty(randomCode)) {
            UUID uuid = UUID.randomUUID();
            randomCode = uuid.toString().replace("-", "");
            spf.edit().putString(PublicUtils.RANDOM_CODE, randomCode).commit();
            SharedPreferencesUtil.getInstance(context).setGrirmsUserIsCodeOne("1");
        }
        // else{
        // SharedPreferencesUtil.getInstance(context).setGrirmsUserIsCodeOne("2");
        // }
        return randomCode;
    }

    private static String a8Imis(Context context) {
        int simId_1 = 0;// 卡1
        int simId_2 = 1;// 卡2
        try {
            Class<?> cx = Class.forName("android.telephony.MSimTelephonyManager");
            Object obj = context.getSystemService("phone_msim");
            Method ms = cx.getMethod("getSubscriberId", int.class);
            String imsi_1 = (String) ms.invoke(obj, simId_1);
            return imsi_1;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void updateApp(Context context) {
        Intent intent = new Intent(context, DownApkBackstageService.class);
        intent.putExtra("isFromHome", true);
        context.startService(intent);
    }

    /**
     * SD卡是否挂起
     *
     * @return
     */
    public static boolean isMountSDCard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    public static int align2Gravity(int align) {
        switch (align) {
            case 1:
                return Gravity.LEFT;
            case 2:
                return Gravity.CENTER;
            case 3:
                return Gravity.RIGHT;
            default:
                return Gravity.LEFT;
        }
    }

    /**
     * 获取资源字符串
     *
     * @param context
     * @param resId
     * @return
     */
    public static String getResourceString(Context context, int resId) {

        return context.getResources().getString(resId);
    }

    public static String readJason(String path) {
        StringBuffer buffer = new StringBuffer();
        FileInputStream fis = null;
        InputStreamReader isr = null;
        try {
            fis = new FileInputStream(new File(path));
            isr = new InputStreamReader(fis, "GBK");
            char[] chars = new char[1024];
            int i = 0;
            while ((i = isr.read(chars)) != -1) {
                buffer.append(chars);
            }
        } catch (Exception e) {
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (isr != null) {
                    isr.close();
                }
            } catch (IOException e) {
            }
        }

        return buffer.toString();
    }

    /**
     * 转动进度条
     *
     * @param context
     * @param message
     * @return
     */
    public static ProgressDialog getProgressBar(Context context, String message) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setTitle(R.string.tip);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        return progressDialog;

    }

    /**
     * 王建雨 2012.6.12 插入一条联系人
     *
     * @param context     上下文
     * @param displayName 联系人显示名称
     * @param number      联系人号码
     * @param email       联系人EMail
     * @throws RemoteException
     * @throws OperationApplicationException
     */
    public static void insertContact(Context context, String displayName, String number, String email) {

        // 执行SQL操作的ArrayList
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        // 插入的空值返回的contact_id的执行顺序
        int rawContactInsertIndex = 0;

        // 插入空值
        ops.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI).withValue(RawContacts.ACCOUNT_NAME, null)// 插入空值
                .build());// 构建

        // 联系人的显示名称
        ops.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)
                .withValueBackReference(Data.RAW_CONTACT_ID, rawContactInsertIndex)// 插入rawContactInsertIndex次数操作返回的值
                .withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)// 显示名称MiMeType
                .withValue(StructuredName.GIVEN_NAME, displayName)// 显示名称数据
                .build());// 构建

        // 联系人的电话号码
        ops.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)
                .withValueBackReference(Data.RAW_CONTACT_ID, rawContactInsertIndex)// 插入rawContactInsertIndex次数操作返回的值
                .withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)// 电话MiMeType
                .withValue(Phone.NUMBER, number)// 电话数据
                .withValue(Phone.TYPE, Phone.TYPE_WORK)// 电话类型
                .build());// 构建k

        if (!TextUtils.isEmpty(email)) {
            // 联系人的EMail
            ops.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)
                    .withValueBackReference(Data.RAW_CONTACT_ID, rawContactInsertIndex)// 插入rawContactInsertIndex次数操作返回的值
                    .withValue(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE)// EMail的MiMeType
                    .withValue(Email.DATA, email)// EMail数据
                    .withValue(Email.TYPE, Email.TYPE_WORK)// EMail类型
                    .build());// 构建
        }


        try {
            // 批量执行(相当于事务操作)
            context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 王建雨 2012.7.2 通过号码查询联系人显示名称
     *
     * @param context 上下文
     * @param number  联系人号码
     */
    public static String getNameByNumber(Context context, String number) {
        // 显示名称
        String displayName = null;
        // 通过ContentResolver查询Contacts 的内容提供者 查询
        Cursor cursorOriginal = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},
                ContactsContract.CommonDataKinds.Phone.NUMBER + "='" + number + "'", null, null);
        // 查询到信息
        if (cursorOriginal != null && cursorOriginal.getCount() > 0) {
            // 光标可以移动到第一行
            if (cursorOriginal.moveToFirst()) {
                // 赋值
                displayName = cursorOriginal
                        .getString(cursorOriginal.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            }
            // 关闭光标
            cursorOriginal.close();
        }
        return displayName;
    }

    /**
     * 王建雨2012.6.14 得到当前版本号
     *
     * @param context 上下文
     * @return 当前版本号
     */
    public static String getCurVersion(Context context) {
        // 得到PackageManager
        PackageManager pm = context.getPackageManager();
        // 初始化版本号为0
        int versionCode = 0;
        try {
            // 从PackageInfo中获取version_code
            versionCode = pm.getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            // 找不到包名时,提示
            Toast.makeText(context, R.string.utility_string15, Toast.LENGTH_SHORT).show();
        }
        return String.valueOf(versionCode);
    }

    // 转换dip为px
    public static int convertDIP2PX(Context context, int dip) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
    }

    // 转换px为dip
    public static int convertPX2DIP(Context context, int px) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f * (px >= 0 ? 1 : -1));
    }

    // 转换px为sp
    public static int px2sp(Context context, int px) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f * (px >= 0 ? 1 : -1));
    }

    // 转换sp为px
    public static int sp2px(Context context, int sp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (sp * scale + 0.5f * (sp >= 0 ? 1 : -1));
    }

    public static void clearCoreHttpHelperCache(Context context, String keys) {
        String[] keyArr = keys.split(",");
        if (keyArr != null && keyArr.length > 0) {
            SharedPreferences.Editor editeor = context.getSharedPreferences("COREHTTP_BACKG", Context.MODE_PRIVATE)
                    .edit();
            for (String key : keyArr) {
                editeor.remove(key);
                JLog.d("999", "remove COREHTTP_BACKG =>" + key);
            }
            editeor.commit();
        }
    }

    public static String compareDate(String oldDate) throws ParseException {
        String str = PublicUtils.getResourceString(SoftApplication.context, R.string.utility_string17);
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date begin = dfs.parse(oldDate);
        Date end = dfs.parse(DateUtil.getCurDateTime());
        long between = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了转换成秒

        long day = between / (24 * 3600);
        long hour = between % (24 * 3600) / 3600;
        long minute = between % 3600 / 60;
        long second = between % 60 / 60;

        if (day != 0) {

            if (day >= 60) {
                return PublicUtils.getResourceString(SoftApplication.context, R.string.utility_string21);
            } else if (day > 30 && day < 60) {
                return PublicUtils.getResourceString(SoftApplication.context, R.string.utility_string20);
            } else if (day >= 1 && day < 30) {
                return day + PublicUtils.getResourceString(SoftApplication.context, R.string.utility_string7);
            }
        }
        if (hour != 0) {
            return hour + PublicUtils.getResourceString(SoftApplication.context, R.string.utility_string19);
        }

        if (minute != 0) {
            if (minute >= 0 && minute <= 3) {
                return PublicUtils.getResourceString(SoftApplication.context, R.string.utility_string17);
            } else {
                return minute + PublicUtils.getResourceString(SoftApplication.context, R.string.utility_string18);
            }
        }

        return str;
    }

    public static String compareDateForWechat(String oldDate) throws ParseException {
        String str = PublicUtils.getResourceString(SoftApplication.context, R.string.utility_string17);
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date begin = dfs.parse(oldDate);
        Date end = dfs.parse(DateUtil.getCurDateTime());
        long between = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了转换成秒

        long day = between / (24 * 3600);
        long hour = between % (24 * 3600) / 3600;
        long minute = between % 3600 / 60;
        long second = between % 60 / 60;

        if (day != 0) {
            if (day >= 3) {
                return PublicUtils.getResourceString(SoftApplication.context, R.string.utility_string16);
            } else {
                if (day == 1) {
                    return PublicUtils.getResourceString(SoftApplication.context, R.string.utility_string6);
                } else {
                    if (day == 2) {
                        return PublicUtils.getResourceString(SoftApplication.context, R.string.utility_string5);
                    }
                }
            }
        } else {
            return DateUtil.getTime();
        }

        return str;
    }

    /**
     * 企业微信时间转换
     *
     * @param oldDate
     * @return
     * @throws ParseException
     */
    public static String wechatCompareDate(String oldDate) throws ParseException {
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date begin = dfs.parse(oldDate);
        Date end = dfs.parse(DateUtil.getCurDateTime());
        long between = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了转换成秒

        long day = between / (24 * 3600);
        long hour = between % (24 * 3600) / 3600;
        long minute = between % 3600 / 60;
        long second = between % 60 / 60;

        if (day != 0) {
            if (day > 10) {
                return DateUtil.getData();
            } else if (day >= 3 && day <= 10) {
                return day + PublicUtils.getResourceString(SoftApplication.context, R.string.utility_string7);
            } else if (day >= 2 && day < 3) {
                return PublicUtils.getResourceString(SoftApplication.context, R.string.utility_string6);
            } else if (day >= 1 && day < 2) {
                return PublicUtils.getResourceString(SoftApplication.context, R.string.utility_string5);
            } else {
                return DateUtil.getTimeString(oldDate);
            }
        } else {
            return DateUtil.getTimeString(oldDate);
        }
    }

    /**
     * 去掉字符串后面的电话号码,如果没有,则直接返回
     *
     * @param userName 后面带或不带电话号码的字符串,格式 :str<18911112222>
     * @return 不带电话号码的字符串, 格式 :str
     */
    public static String clearNumber(String userName) {
        if (userName.length() <= 13) {
            return userName;
        }
        int index = userName.length() - 13;
        String numberStr = userName.substring(index);
        String numberMatch = "^<1\\d{10}>$";
        if (numberStr.matches(numberMatch)) {
            return userName.substring(0, index);
        } else {
            return userName;
        }
    }

    /**
     * BBS等级区分-根据积分,返回相应图标
     *
     * @param score 积分
     * @return 图标资源
     */
    public static int getBBSLeveIcon(String score) {
        if (!TextUtils.isEmpty(score)) {
            int leve = getBBSLeve(score);
            switch (leve) {
                case 0:
                    return R.drawable.bbs_leve_0;
                case 1:
                    return R.drawable.bbs_leve_1;
                case 2:
                    return R.drawable.bbs_leve_2;
                case 3:
                    return R.drawable.bbs_leve_3;
                case 4:
                    return R.drawable.bbs_leve_4;
                case 5:
                    return R.drawable.bbs_leve_5;
                case 6:
                    return R.drawable.bbs_leve_6;
                case 7:
                    return R.drawable.bbs_leve_7;
                case 8:
                    return R.drawable.bbs_leve_8;
                case 9:
                    return R.drawable.bbs_leve_9;
                case 10:
                    return R.drawable.bbs_leve_10;
            }
        }
        return R.drawable.bbs_leve_0;
    }

    /**
     * BBS等级区分-根据积分,返回相应小图标
     *
     * @param score 积分
     * @return 图标资源
     */
    public static int getBBSLeveIconSmall(String score) {
        if (!TextUtils.isEmpty(score)) {
            int leve = getBBSLeve(score);
            switch (leve) {
                case 0:
                    return R.drawable.bbs_leve_0_s;
                case 1:
                    return R.drawable.bbs_leve_1_s;
                case 2:
                    return R.drawable.bbs_leve_2_s;
                case 3:
                    return R.drawable.bbs_leve_3_s;
                case 4:
                    return R.drawable.bbs_leve_4_s;
                case 5:
                    return R.drawable.bbs_leve_5_s;
                case 6:
                    return R.drawable.bbs_leve_6_s;
                case 7:
                    return R.drawable.bbs_leve_7_s;
                case 8:
                    return R.drawable.bbs_leve_8_s;
                case 9:
                    return R.drawable.bbs_leve_9_s;
                case 10:
                    return R.drawable.bbs_leve_10_s;
            }
        }
        return R.drawable.bbs_leve_0_s;
    }

    /**
     * BBS等级区分-根据积分,返回相应等级
     *
     * @param score 积分
     * @return bbs等级
     */
    public static int getBBSLeve(String score) {
        int leveScore = Integer.valueOf(score);
        int leve = -1;
        if (leveScore >= 200 && leveScore <= 500) {
            leve = 1;
        } else if (leveScore > 500 && leveScore <= 1000) {
            leve = 2;
        } else if (leveScore > 1000 && leveScore <= 1500) {
            leve = 3;
        } else if (leveScore > 1500 && leveScore <= 2000) {
            leve = 4;
        } else if (leveScore > 2000 && leveScore <= 4000) {
            leve = 5;
        } else if (leveScore > 4000 && leveScore <= 6000) {
            leve = 6;
        } else if (leveScore > 6000 && leveScore <= 8000) {
            leve = 7;
        } else if (leveScore > 8000 && leveScore <= 10000) {
            leve = 8;
        } else if (leveScore > 10000 && leveScore <= 15000) {
            leve = 9;
        } else if (leveScore > 15000 && leveScore <= 20000) {
            leve = 10;
        } else if (leveScore > 20000) {
            leve = 0;
        }
        return leve;
    }

    public static String receiveLocationTypeByType(String type) {
        String temp = PublicUtils.getResourceString(SoftApplication.context, R.string.utility_string8);
        String gte = PublicUtils.getResourceString(SoftApplication.context, R.string.utility_string10);
        if (!TextUtils.isEmpty(type)) {
            String tmp1 = PublicUtils.getResourceString(SoftApplication.context, R.string.utility_string11);
            String tmp2 = PublicUtils.getResourceString(SoftApplication.context, R.string.utility_string12);
            String tmp3 = PublicUtils.getResourceString(SoftApplication.context, R.string.utility_string13);
            if (type.contains(gte)) {
                temp = gte;
            } else if (type.contains(tmp1) || type.contains(tmp2) || type.contains("GPSOne") || type.contains(tmp3)) {
                temp = PublicUtils.getResourceString(SoftApplication.context, R.string.utility_string9);
            } else {
                temp = PublicUtils.getResourceString(SoftApplication.context, R.string.utility_string8);
            }
        }

        return temp;
    }

    public static final int MODE_CHINA_MOBILE = 10086;
    public static final int MODE_CHINA_UNICOM = 10010;
    public static final int MODE_CHINA_TELECOM = 10000;
    public static final int MODE_CHINA_TELECOM_4G = 100004;

    /**
     * IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
     *
     * @author houyu
     */
    public static int receiveOperator(Context context) {
        int mode = MODE_CHINA_TELECOM;
//		String IMSI = getSubscriberId(context);
        String IMSI = getIMSI(context);
        if (!TextUtils.isEmpty(IMSI)) {
            if (IMSI.startsWith("46000") || IMSI.startsWith("46002") || IMSI.startsWith("46007")) {
                mode = MODE_CHINA_MOBILE;
            } else if (IMSI.startsWith("46001")) {
                mode = MODE_CHINA_UNICOM;
            } else if (IMSI.startsWith("46003")) {
                mode = MODE_CHINA_TELECOM;
            } else if (IMSI.startsWith("46011")) {
                mode = MODE_CHINA_TELECOM_4G;
            }
        }
        return mode;
    }

    /**
     * 根据序列号IMSI判断是否是电信的手机卡
     *
     * @param context
     * @return
     */
    public static boolean isChinaTelecom(Context context) {
        String IMSI = getIMSI(context);
        if (null != IMSI) {
            if (IMSI.startsWith("46003") || IMSI.startsWith("46011") || IMSI.startsWith("20404")) {

                return true;
            }
        }

        return false;
    }

    /**
     * 判断是否采用短信验证身份 目前仅电信3G号用用CTWAP，其他都用短信验证
     *
     * @param context
     * @return
     */
    public static boolean isUseSMSCheckIdentity(Context context) {
        if (receiveOperator(context) == MODE_CHINA_TELECOM) {
            return false;
        } else {
            return true;
        }
    }

    private static String getUUID(Context context) {
        String uuid = Installation.getID(context);
        return uuid;
    }

    public static long totalFlow() {
        if (Integer.valueOf(android.os.Build.VERSION.SDK_INT) >= 8) {
            int id = android.os.Process.myUid();
            long rx = TrafficStats.getUidRxBytes(id); // 下行
            long tx = TrafficStats.getUidTxBytes(id); // 上行
            return (rx == TrafficStats.UNSUPPORTED ? 0 : rx) + (tx == TrafficStats.UNSUPPORTED ? 0 : tx);
        } else {
            return 0;
        }
    }

    public static void writeErrorLog(Context context, String content, String tag) {
        if (context != null && TextUtils.isEmpty(Constants.CURRENT_VERSION)) {
            Constants.CURRENT_VERSION = ApplicationHelper.getApplicationName(context).appVersionName;
        }
        File file = new File(Constants.SDCARD_PATH + "log");
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append(DateUtil.getCurDateTime()).append(":\t");
        sb.append(tag).append("(").append(Constants.CURRENT_VERSION).append("\\");
        sb.append(android.os.Build.MODEL).append("\\").append(android.os.Build.VERSION.RELEASE).append(")")
                .append("\t：");
        sb.append(content);
        PrintStream printStream = null;
        try {
            if (file.exists()) {
                // 清空之前的记录
                if (file.length() > 500000) {
                    file.delete();
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }
            } else {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            printStream = new PrintStream(new BufferedOutputStream(new FileOutputStream(file, true)));
            printStream.print(sb.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (printStream != null) {
                    printStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String getPTUid() {
        StringBuilder sb = new StringBuilder();
        sb.append("p:").append(android.os.Process.myPid()).append(";");
        sb.append("u:").append(android.os.Process.myUid()).append(";");
        sb.append("t:").append(android.os.Process.myTid()).append(";");
        return sb.toString();
    }

    /**
     * 通过代理地址判断是否是WAP
     *
     * @return
     */
    public static String checkIsWapForProxy(Context mContext) {
        // String TYPE_CT_WAP="10.0.0.200";
        Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");
        Cursor c = mContext.getContentResolver().query(PREFERRED_APN_URI, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
            // String user = c.getString(c.getColumnIndex("user"));
            String proxy = c.getString(c.getColumnIndex("proxy"));
            return proxy;
        } else {
            return null;
        }
    }

    public static void intentPhone(final Context mContext, final String number, View view) {
        if (!TextUtils.isEmpty(number) && view != null) {
            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    String action = Intent.ACTION_DIAL;
                    Uri data = Uri.parse("tel:" + number);
                    intent.setAction(action);
                    intent.setData(data);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    /**
     * 上传LOG
     *
     * @param mContext
     * @param fileName
     */
    public static void uploadLog(Context mContext, String fileName) {
        JLog.d("uploadLog", "开始上传LOG");
        PendingRequestVO vo = new PendingRequestVO();
        vo.setTitle("Log");
        vo.setContent("应用Log");
        vo.addParams("phoneno", receivePhoneNO(mContext));
        vo.addParams("companyid", String.valueOf(SharedPreferencesUtil.getInstance(mContext).getCompanyId()));
        vo.addFiles("phonelog", Constants.SDCARD_PATH + fileName);
        new CoreHttpHelper().performLogUpload(mContext, vo);
    }

    /**
     * 清空所有SharedPreferences数据
     */
    public static void clearAllSP(Context mContext) {
        SharedPreferencesUtil.getInstance(mContext).clearAll();
        SharedPreferencesUtil2.getInstance(mContext).clearAll();
        SharedPrefsBackstageLocation.getInstance(mContext).clearAll();
        mContext.getSharedPreferences("MOBILE_MDN_PREF", Context.MODE_PRIVATE).edit().clear().commit();
        mContext.getSharedPreferences("COREHTTP_PREF", Context.MODE_PRIVATE).edit().clear().commit();
        mContext.getSharedPreferences("COREHTTP_BACKG", Context.MODE_PRIVATE).edit().clear().commit();

    }

    public static boolean isValid(JSONObject jsonObject, String key) throws JSONException {
        boolean flag = false;
        if (jsonObject.has(key) && !jsonObject.isNull(key)) {
            flag = !"".equals(jsonObject.getString(key));
        }
        return flag;
    }

    public static String verifyReturnValue(String result) {
        if (result == null || result.contains("<html>") || result.contains("<!DOCTYPE")
                || result.contains("<result>")) {
            return null;
        } else {
            return result;
        }
    }

    /**
     * 是否迟到
     *
     * @return 迟到的时间 秒为单位
     */
    public static int isBeLate(Context mContext, String date) {
        int differ = 0;
        try {
            if (TextUtils.isEmpty(date)) {
                date = DateUtil.getCurDateTime();
            }
            // 要求上班时间
            String workTime = SharedPreferencesUtil.getInstance(mContext).getNewAttendanceWorkDay();
            String[] str = workTime.split(":");
            if (str != null && str.length == 2) {
                int workHour = Integer.parseInt(str[0]);
                int currentHour = DateUtil.getHour(DateUtil.getDate(date));
                String workStart = new StringBuffer(date).replace(11, 19, workTime + ":00").toString();
                if (currentHour > workHour) {
                    differ = differBetweenDate(DateUtil.getDate(date), DateUtil.getDate(workStart));// 迟到
                } else if (currentHour == workHour) {
                    int workMin = Integer.parseInt(str[1]);
                    int currentMin = DateUtil.getMin(DateUtil.getDate(date));
                    if (currentMin > workMin) {
                        differ = differBetweenDate(DateUtil.getDate(date), DateUtil.getDate(workStart));// 迟到
                    }
                }
                JLog.d("jishen", "上班时间：" + workTime);
            }
        } catch (Exception e) {
            JLog.d("jishen", e.getMessage());
        }
        return differ;
    }

    /**
     * 判断是否早退
     *
     * @param mContext
     * @return 早退的时间 秒为单位
     */
    public static int isLeaveEarly(Context mContext, String date) {
        int differ = 0;
        try {
            if (TextUtils.isEmpty(date)) {
                date = DateUtil.getCurDateTime();
            }
            // 要求上班时间
            String workStart = SharedPreferencesUtil.getInstance(mContext).getNewAttendanceWorkDay();
            String workTime = SharedPreferencesUtil.getInstance(mContext).getNewAttendanceWorkTime();
            if (!TextUtils.isEmpty(workTime) && !TextUtils.isEmpty(workTime)) {
                String start = new StringBuffer(date).replace(11, 19, workStart + ":00").toString();
                Date endDate = DateUtil.getInternalDateByMin(DateUtil.getDate(start),
                        (int) (Float.parseFloat(workTime) * 60));
                Date currentDate = DateUtil.getDate(date);
                if (currentDate.before(endDate)) {
                    differ = differBetweenDate(currentDate, endDate);
                }
                JLog.d("jishen", "上班：" + start + "\n" + "下班：" + DateUtil.dateToDateString(endDate));
            }
        } catch (Exception e) {
            JLog.d("jishen", e.getMessage());
        }
        return differ;
    }

    /**
     * 是否迟到
     *
     * @return 迟到的时间 秒为单位
     */
    public static int isBeLateNet(Context mContext, String date, String nowTime) {
        int differ = 0;
        try {
            if (TextUtils.isEmpty(date)) {
                date = nowTime;
            }
            // 要求上班时间
            String workTime = SharedPreferencesUtil.getInstance(mContext).getNewAttendanceWorkDay();
            String[] str = workTime.split(":");
            if (str != null && str.length == 2) {
                int workHour = Integer.parseInt(str[0]);
                int currentHour = DateUtil.getHour(DateUtil.getDate(date));
                String workStart = new StringBuffer(nowTime).replace(11, 19, workTime + ":00").toString();
                if (currentHour > workHour) {
                    differ = differBetweenDate(DateUtil.getDate(date), DateUtil.getDate(workStart));// 迟到
                } else if (currentHour == workHour) {
                    int workMin = Integer.parseInt(str[1]);
                    int currentMin = DateUtil.getMin(DateUtil.getDate(date));
                    if (currentMin > workMin) {
                        differ = differBetweenDate(DateUtil.getDate(date), DateUtil.getDate(workStart));// 迟到
                    }
                }
                JLog.d("jishen", "上班时间：" + workTime);
            }
        } catch (Exception e) {
            JLog.d("jishen", e.getMessage());
        }
        return differ;
    }

    /**
     * 判断是否早退
     *
     * @param mContext
     * @return 早退的时间 秒为单位
     */
    public static int isLeaveEarlyNet(Context mContext, String date, String nowTime) {
        int differ = 0;
        try {
            if (TextUtils.isEmpty(date)) {
                date = nowTime;
            }
            // 要求上班时间
            String workStart = SharedPreferencesUtil.getInstance(mContext).getNewAttendanceWorkDay();
            String workTime = SharedPreferencesUtil.getInstance(mContext).getNewAttendanceWorkTime();
            if (!TextUtils.isEmpty(workTime) && !TextUtils.isEmpty(workTime)) {
                String start = new StringBuffer(nowTime).replace(11, 19, workStart + ":00").toString();
                Date endDate = DateUtil.getInternalDateByMin(DateUtil.getDate(start),
                        (int) (Float.parseFloat(workTime) * 60));
                Date currentDate = DateUtil.getDate(date);
                if (currentDate.before(endDate)) {
                    differ = differBetweenDate(currentDate, endDate);
                }
                JLog.d("jishen", "上班：" + start + "\n" + "下班：" + DateUtil.dateToDateString(endDate));
            }
        } catch (Exception e) {
            JLog.d("jishen", e.getMessage());
        }
        return differ;
    }

    /**
     * 比较两个日期之间相差的毫秒数
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return
     */
    private static int differBetweenDate(Date start, Date end) {
        double dif = Math.abs(start.getTime() - end.getTime());
        return (int) Math.ceil((dif / 1000 / 60));
    }

    // public static boolean isUseNewYearUI(){
    // boolean flag = false;
    // String today = DateUtil.getCurDate();
    // // today = "2014-02-15";
    // String start = "2014-01-23";
    // String end = "2014-02-14";
    // if (today.compareTo(start)>=0 && today.compareTo(end)<=0) {
    // flag = true;
    // }
    // return flag;
    // }

    /**
     * 保留两位小数
     *
     * @param d
     * @return
     */
    public static String formatDouble(double d) {
        // DecimalFormat df = new DecimalFormat("0.00");
        // String db = df.format(d);
        // return db;
        String result = "";
        BigDecimal b = new BigDecimal(d); // 计算相减
        Double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();// 保留两位进1)new
        // BigDecimal(.doubleValue()-
        result = new DecimalFormat("#.##").format(f1);// 如果小数点后是0舍弃
        return result;
    }

    /*
     * 验证手机电话号
     */
    public static boolean isMobileTelephone(String value) {
        Pattern pattern = Pattern.compile("^[1][3,4,5,7,8]\\d{9}");
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    /*
     * 验证数字
     */
    public static boolean isNumeric(String value) {
        // Pattern pattern = Pattern.compile("\\d+");
        Pattern pattern = Pattern.compile("[0-9]+(\\.[0-9]+)?");
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    public static String percent(int y, int z) {
        String baifenbi = "";// 接受百分比的值
        double baiy = y * 1.0;
        double baiz = z * 1.0;
        double fen = baiy / baiz;
        // NumberFormat nf = NumberFormat.getPercentInstance(); 注释掉的也是一种方法
        // nf.setMinimumFractionDigits( 2 ); 保留到小数点后几位
        DecimalFormat df1 = new DecimalFormat("##"); // ##.00%
        // 百分比格式，后面不足2位的用0补齐
        // baifenbi=nf.format(fen);
        baifenbi = df1.format(fen);
        return baifenbi;
    }

    /**
     * 根据listview的子view设置高度
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    /**
     * 读取assets下文件中的json数据
     *
     * @param context
     * @param fileName 文件名称
     * @return
     */
    public String readJsonString(Context context, String fileName) {
        InputStream is = null;
        String json = null;
        try {
            is = context.getAssets().open(fileName);
            byte[] buffer = new FileHelper().readImageByte(is);
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return json;
    }

    public static void setEditTextSelection(EditText et) {
        if (et != null) {
            String text = et.getText().toString();
            if (!TextUtils.isEmpty(text)) {
                et.setSelection(text.length());
            }
        }
    }

    /**
     * 隐藏键盘
     *
     * @param context
     */
    public static void hideKeyboard(Context context) {
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager.isActive() && ((Activity) context).getCurrentFocus() != null) {
            // 如果开启
            // imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
            // InputMethodManager.HIDE_NOT_ALWAYS);
            // 关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
            manager.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);

        }
    }

    /**
     * 企业聊天的时候每条消息的消息key
     *
     * @param context
     * @return
     */
    public static String chatMsgKey(Context context) {
        String key = System.currentTimeMillis() + receivePhoneNO(context);
        return key;
    }

    /**
     * 判断字符串是否为数字组成
     *
     * @param value
     * @return
     */
    public static boolean isInteger(String value) {
        if (!TextUtils.isEmpty(value)) {
            Pattern pattern = Pattern.compile("-?[0-9]*");
            Matcher isNum = pattern.matcher(value);
            if (!isNum.matches()) {
                return false;
            }
            return true;
        }
        return false;
    }

    public static boolean isIntegerArray(String value) {
        if (!TextUtils.isEmpty(value)) {
            String[] str = value.split(",");
            for (int i = 0; i < str.length; i++) {
                boolean isTrue = isInteger(str[i]);
                if (isTrue) {
                    continue;
                } else {
                    return false;
                }

            }
            return true;
        }
        return false;
    }

    /**
     * 判断一个月有多少天
     */
    public static int getNumberByMonth(String month) {
        String[] str = month.split("-");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.parseInt(str[0]));
        cal.set(Calendar.MONTH, Integer.parseInt(str[1]) - 1);// 当前月份减1

        return cal.getActualMaximum(Calendar.DATE);
    }

    public static Date getNetDate() {
        Date date = null;
        URL url = null;
        try {
            url = new URL("http://www.baidu.com");
            URLConnection uc = url.openConnection();// 生成连接对象
            uc.setConnectTimeout(3000);
            uc.connect(); // 发出连接

            long ld = uc.getDate(); // 取得网站日期时间
            JLog.d("百度时间 : " + ld);
            if (ld == 0) {
                url = new URL("http://phone.gcgcloud.com/com.grandison.grirms.phone.web-1.0.0/");
                URLConnection uc1 = url.openConnection();// 生成连接对象
                uc1.setConnectTimeout(3000);
                uc1.connect(); // 发出连接

                long ld1 = uc1.getDate(); // 取得网站日期时间
                JLog.d("服务器时间 : " + ld1);
                if (ld1 == 0) {
                    return null;
                } else {
                    date = new Date(ld1); // 转换为标准时间对象
                }
            } else {
                date = new Date(ld); // 转换为标准时间对象
            }

        } // 取得资源对象
        catch (IOException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static boolean checkAddress(String addr) {
        boolean flag = false;
        String str1 = PublicUtils.getResourceString(SoftApplication.context, R.string.utility_string22);
        String str2 = PublicUtils.getResourceString(SoftApplication.context, R.string.utility_string23);
        String str3 = PublicUtils.getResourceString(SoftApplication.context, R.string.utility_string24);
        String str4 = PublicUtils.getResourceString(SoftApplication.context, R.string.utility_string25);
        String str5 = PublicUtils.getResourceString(SoftApplication.context, R.string.utility_string26);
        String str6 = PublicUtils.getResourceString(SoftApplication.context, R.string.utility_string27);
        String str7 = PublicUtils.getResourceString(SoftApplication.context, R.string.utility_string28);
        String str8 = PublicUtils.getResourceString(SoftApplication.context, R.string.utility_string29);
        String str9 = PublicUtils.getResourceString(SoftApplication.context, R.string.utility_string30);

        if (addr.trim().equals(str1) || addr.trim().equals(str2) || addr.trim().equals(str3)
                || addr.trim().equals(str4) || addr.trim().equals(str5)
                || addr.trim().equals(str6) || addr.trim().equals("[]")
                || addr.trim().equals(str7) || addr.trim().equals(str8)
                || addr.trim().equals(str9)) {
            flag = true;
        }
        return flag;
    }

    public static boolean checkConnection(Context c) {
        ConnectivityManager connectivityManager = (ConnectivityManager) c
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            return networkInfo.isAvailable();
        }
        return false;
    }

    public static class TypeValue {
        static final int TYPE_STING = 0;
        static final int TYPE_NUM = 1;
        static final int TYPE_TIME = 2;
        static final int TYPE_DATE = 3;

        Integer type = null;
        String value = null;
    }

    public static boolean compare(TypeValue v1, String v2, int opt) {

        boolean flag = false;

        if (v1.type == TypeValue.TYPE_NUM) {
            double num1 = TextUtils.isEmpty(v1.value) ? 0 : Double.valueOf(v1.value);
            double num2 = Double.valueOf(v2);
            switch (opt) { //(1：等于、2：不等于、3：大于、4：大于等于、5：小于、6：小于等于)
                case 1:
                    flag = (num1 == num2);
                    break;
                case 2:
                    flag = (num1 != num2);
                    break;
                case 3:
                    flag = (num1 > num2);
                    break;
                case 4:
                    flag = (num1 >= num2);
                    break;
                case 5:
                    flag = (num1 < num2);
                    break;
                case 6:
                    flag = (num1 <= num2);
                    break;
            }
        } else if (v1.type == TypeValue.TYPE_STING) {
            switch (opt) { //(1：等于、2：不等于、3：大于、4：大于等于、5：小于、6：小于等于)
                case 1:
                    flag = v1.value.compareTo(v2) == 0 ? true : false;
                    break;
                case 2:
                    flag = v1.value.compareTo(v2) != 0 ? true : false;
                    break;
                case 3:
                    flag = v1.value.compareTo(v2) == 1 ? true : false;
                    break;
                case 4:
                    flag = v1.value.compareTo(v2) > -1 ? true : false;
                    break;
                case 5:
                    flag = v1.value.compareTo(v2) == -1 ? true : false;
                    break;
                case 6:
                    flag = v1.value.compareTo(v2) < 1 ? true : false;
                    break;
            }
        } else if (v1.type == TypeValue.TYPE_DATE) {
            Date date1 = DateUtil.getDate(v1.value, DateUtil.DATAFORMAT_STR);
            Date date2 = DateUtil.getDate(v2, DateUtil.DATAFORMAT_STR);
            switch (opt) { //(1：等于、2：不等于、3：大于、4：大于等于、5：小于、6：小于等于)
                case 1:
                    flag = (date1.getTime() == date2.getTime());
                    break;
                case 2:
                    flag = (date1.getTime() != date2.getTime());
                    break;
                case 3:
                    flag = (date1.getTime() > date2.getTime());
                    break;
                case 4:
                    flag = (date1.getTime() >= date2.getTime());
                    break;
                case 5:
                    flag = (date1.getTime() < date2.getTime());
                    break;
                case 6:
                    flag = (date1.getTime() <= date2.getTime());
                    break;
            }
        } else if (v1.type == TypeValue.TYPE_TIME) {
            Date date1 = DateUtil.getDate(v1.value);
            Date date2 = DateUtil.getDate(v2);
            switch (opt) { //(1：等于、2：不等于、3：大于、4：大于等于、5：小于、6：小于等于)
                case 1:
                    flag = (date1.getTime() == date2.getTime());
                    break;
                case 2:
                    flag = (date1.getTime() != date2.getTime());
                    break;
                case 3:
                    flag = (date1.getTime() > date2.getTime());
                    break;
                case 4:
                    flag = (date1.getTime() >= date2.getTime());
                    break;
                case 5:
                    flag = (date1.getTime() < date2.getTime());
                    break;
                case 6:
                    flag = (date1.getTime() <= date2.getTime());
                    break;
            }
        }
        return flag;
    }

    public static TypeValue getValueBytype(Context context, Func func, String value, int useType) throws DataComputeException {

        TypeValue typeValue = new TypeValue();
        if (func.getType() == Func.TYPE_DATEPICKERCOMP) {
            typeValue.type = TypeValue.TYPE_DATE;
            typeValue.value = value;
        } else if (func.getType() == Func.TYPE_EDITCOMP && (func.getCheckType() != null && func.getCheckType() == Func.CHECK_TYPE_NUMERIC)) {

            typeValue.type = TypeValue.TYPE_NUM;
            typeValue.value = value;

        } else if (func.getType() == Func.TYPE_EDITCOMP || func.getType() == Func.TYPE_TEXTCOMP) {

            typeValue.type = TypeValue.TYPE_STING;
            typeValue.value = value;

        } else if (func.getType() == Func.TYPE_SELECTCOMP) {

            typeValue.value = new DictDB(context).findDictMultiChoiceValueStr(value, func.getDictDataId(), func.getDictTable());
            switch (useType) {
                case 1: //计算
                    typeValue.type = TypeValue.TYPE_NUM;
                    break;
                case 2: //条件必填
                    typeValue.type = TypeValue.TYPE_STING;
                    break;
            }
        } else if (func.getType() == Func.TYPE_SELECT_OTHER) {

            if (!RegExpvalidateUtils.isInteger(value)) {
                typeValue.value = value;
            } else if (!"-1".equals(value)) {
                typeValue.value = new DictDB(context).findDictMultiChoiceValueStr(value, func.getDictDataId(), func.getDictTable());
            } else {
                typeValue.value = "";
            }

            switch (useType) {
                case 1: //计算
                    if (!RegExpvalidateUtils.isNumeric(typeValue.value.trim())) {
                        throw new DataComputeException("\"" + func.getName() + "\"" + PublicUtils.getResourceString(SoftApplication.context, R.string.utility_string14)); //由于输入错误，导致计算错误，抛出异常
                    }
                    typeValue.type = TypeValue.TYPE_NUM;
                    break;
                case 2: //条件必填
                    typeValue.type = TypeValue.TYPE_STING;
                    break;
            }

        } else if (func.getType() == Func.TYPE_HIDDEN) {

            if (func.getDefaultType() == Func.DEFAULT_TYPE_OTHER) {
                switch (useType) {
                    case 1: //计算
                        typeValue.type = TypeValue.TYPE_NUM;
                        break;
                    case 2: //条件必填
                        typeValue.type = TypeValue.TYPE_STING;
                        break;
                }
                typeValue.value = value;
            } else if (func.getDefaultType() == Func.DEFAULT_TYPE_DATE_NO_TIME) {
                typeValue.type = TypeValue.TYPE_DATE;
                typeValue.value = value;
            } else if (func.getDefaultType() == Func.DEFAULT_TYPE_DATE || func.getDefaultType() == Func.DEFAULT_TYPE_STARTDATE) {
                typeValue.type = TypeValue.TYPE_TIME;
                typeValue.value = value;
            }

        }
        typeValue.value = value;

        return typeValue;
    }
    /**
     * 判断手机号是否符合规范
     * @param phoneNo 输入的手机号
     * @return
     */
    public static boolean isPhoneNumber(String phoneNo) {
        if (TextUtils.isEmpty(phoneNo)) {
            return false;
        }
        if (phoneNo.length() == 11) {
            for (int i = 0; i < 11; i++) {
                if (!PhoneNumberUtils.isISODigit(phoneNo.charAt(i))) {
                    return false;
                }
            }
            Pattern p = Pattern.compile("^((13[^4,\\D])" + "|(134[^9,\\D])" +
                    "|(14[5,7])" +
                    "|(15[^4,\\D])" +
                    "|(17[3,6-8])" +
                    "|(18[0-9]))\\d{8}$");
            Matcher m = p.matcher(phoneNo);
            return m.matches();
        }
        return false;
    }



}
