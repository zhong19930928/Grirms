package com.yunhu.yhshxc.activity;

import gcg.org.debug.JLog;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.jpush.android.api.JPushInterface;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.login.LoginActivity;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.parser.CacheData;
import com.yunhu.yhshxc.service.LocationService;
import com.yunhu.yhshxc.utility.ApplicationHelper;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.HttpHelper;
import com.yunhu.yhshxc.utility.NetSettingUtil;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.URLWrapper;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.utility.Version;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.loopj.android.http.RequestParams;


/**
 * @author jishen
 *         1.加载页面，验证SD卡剩余空间，如果小于5M提示用户然后程序自动关闭，如果大于5M小于10M提示用户，但是程序继续运行。
 *         2.设置企业名称，第一次登陆的时候是默认的企业名称和企业LOGO，如果已经从后台获取到企业名称和企业LOGO则用获取的企业名称和企业LOGO。
 *         3.验证当前手机网络，如果没有获取手机号，则要求必须是WAP网络（为了获取手机号），否则只判断手机网络是否开启,移动用户则不必，移动用户是让用户输入手机号。
 *         4.获取服务端最新版本的信息，包括：（1）最新版本号，（2）最新版本的下载URL，（3）最新版本的安装包的大小，（4）最新版本安装包的MD5（验证下载后下载的包和实际的包是否一样）。
 *         5.获取该用户的使用的开始日期和结束日期，并验证是否在有效期内，如果不在有效期内则提示用户过期
 *         6.鉴权验证用户是否更换手机号，如果更换则提示用户会初始化
 *         7.开启一次定位，获取当前用户的位置信息
 */
public class SplashActivity2 extends AbsBaseActivity {

    //企业信息文字标识
    private TextView title1, title2, title3;
    //企业LOGO
    private ImageView logo;
    //中国电信图标
    private ImageView im_ct_logo;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        try {
            setStrictMode();
            setAppInfoByVersion();
        } catch (Exception e) {
            JLog.e(e);
        }
        //开启一次定位
        //startLocationInBackground();
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            //设置SD卡下GRIRMS目录
            setSdcardPath();
            netCheck();
        } catch (Exception e) {
            JLog.e(e);
        }
    }

    /**
     * 验证网络
     */
    private void netCheck() {
        //验证当前网络是否开启，以及网络的类型
        int netCode = NetSettingUtil.isPassByNetwork(SplashActivity2.this);
        if (netCode == 0) {
            netPass();
        } else {//提示用户打开网络
            openNetworkSettings();
        }
    }


    //如果是当前手机的系统版本大于10则需要添加以下信息，否则联网会报异常
    @SuppressLint("NewApi")
    private void setStrictMode() {
        if (Integer.valueOf(android.os.Build.VERSION.SDK_INT) >= 10) {
            StrictMode.setThreadPolicy(new StrictMode
                    .ThreadPolicy
                    .Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork() // or .detectAll() for all detectable problems
                    .penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode
                    .VmPolicy
                    .Builder()
                    .detectLeakedSqlLiteObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }
    }

    private void setAppInfoByVersion() {
        //获取当前设置的app类型
        String projectVersion = this.getResources().getString(R.string.PROJECT_VERSIONS);
        //根据获取的类型来设置默认的显示的标识分别有"YILI" "4.5" "4.0"
        if (projectVersion.equalsIgnoreCase("YILI")) {
            initialize(Version.VER_YILI);
        } else if (projectVersion.equalsIgnoreCase(Constants.APP_VERSION_4_5)) {
            initialize(Version.VER_45);
        } else {
            initialize(Version.VER_NORAML);
        }
    }

    /**
     * 网络处于开启状态
     */
    private void netPass() {

        String locVersion = SharedPreferencesUtil.getInstance(this).getLocationVersion();
        if (!TextUtils.isEmpty(locVersion) && (Integer.valueOf(this.getString(R.string.IS_NEED_INIT_VERSION)) > Integer.valueOf(locVersion))) {
            // 如果本地版本小于等于在需要重新初始化的历史本版时
            SharedPreferencesUtil.getInstance(this).setIsInit(false);
        }

        //等于0表示网络验证通过
        //imsi2为已经存储的手机卡的IMSI号   当用户第一次使用的时候会存储
        //imsi1为当前的手机卡的IMSI号
        String imsi1 = PublicUtils.getSubscriberId(this);
        String imsi2 = SharedPreferencesUtil.getInstance(this).getSubscriberId();
        if (TextUtils.isEmpty(imsi1)) {
            if (TextUtils.isEmpty(PublicUtils.receivePhoneNO(this))) {
                intentFreeLogin(null);
            } else {
//				getVersionInfo();
                checkPhoneNumber();
            }
        } else {
            if (!TextUtils.isEmpty(imsi2) && !imsi2.equals(imsi1)) {//不同说明更换了手机卡
                changeIMSI();
            } else {
//				String projectVersion = this.getResources().getString(R.string.PROJECT_VERSIONS);
//				if(Constants.APP_VERSION_4_5.equals(projectVersion) || Constants.APP_VERSION_4_7.equals(projectVersion)){//免费版本和通用版鉴权使用密码登陆鉴权
//					intentFreeLogin(null);
//				}else{//正式版本
//					getVersionInfo();
//				}

//				getVersionInfo();
                checkPhoneNumber();

            }
        }
    }

    /**
     * 检测是否是电信手机号
     */
    private void checkPhoneNumber() {
        //如果有手机号,就不在验证这些
        if (!TextUtils.isEmpty(PublicUtils.receivePhoneNO(this))) {
            getVersionInfo();

        } else {
            //先判断手机号码是否属于电信
            if (PublicUtils.isChinaTelecom(this)) {
                //先根据IMSI去查手机号,查到手机号
                RequestParams pa = new RequestParams();
                String imsiStr = PublicUtils.getIMSI(this);
                pa.put("grirms_user_imsi", imsiStr);
//			pa.put("grirms_user_imsi", "460030900246895");//测试数据
                GcgHttpClient.getInstance(this).getNoHeader(UrlInfo.getPhoneNumberByImsiUrl(), pa, new HttpResponseListener() {

                    @Override
                    public void onSuccess(int statusCode, String content) {
                        JSONObject jsob;
                        try {
                            jsob = new JSONObject(content);
                            if (jsob.has("phone_no")) {
                                String phoneN = jsob.getString("phone_no");
                                if (!TextUtils.isEmpty(phoneN)) {
                                    SplashActivity2.this.getSharedPreferences(PublicUtils.PREFERENCE_NAME, Context.MODE_PRIVATE)
                                            .edit().putString(PublicUtils.PREFERENCE_NAME_PHONE, phoneN).commit();
                                    getVersionInfo();
                                } else {
                                    //如果查询不到此IMSI对应的手机号码,或者获取不到Imsi信息,则获取手机号码
                                    netCheckPhone();

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            netCheckPhone();
                        }

                    }

                    @Override
                    public void onStart() {


                    }

                    @Override
                    public void onFinish() {
//					getVersionInfo();

                    }

                    @Override
                    public void onFailure(Throwable error, String content) {


                    }
                });

            } else {
                //如果不是电信手机号,则进行获取手机号,如果能获取到则检查是否为有效号码,有效号码就存入免登陆,否则继续走登录页面
                netCheckPhone();

            }
        }


    }

    /**
     * 获取手机号码,进行验证
     */
    private void netCheckPhone() {
        String phoneNumber = PublicUtils.getPhoneNumber(this);

        if (!TextUtils.isEmpty(phoneNumber)) {
            phoneNumber = phoneNumber.substring(3);
            //请求网络,验证当前手机号是否有效
            SplashActivity2.this.getSharedPreferences(PublicUtils.PREFERENCE_NAME, Context.MODE_PRIVATE)
                    .edit().putString(PublicUtils.PREFERENCE_NAME_PHONE, phoneNumber).commit();

            GcgHttpClient.getInstance(this).get(UrlInfo.getUrlApkVersionInfo(this), versionInfoParms(), new HttpResponseListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onFinish() {
                    getVersionInfo();
                }

                @Override
                public void onSuccess(int statusCode, String content) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(content);
                        String resultcode = jsonObject.getString("resultcode");
                        if ("0003".equals(resultcode)) {//说明是无效用户,清除已手机号存储
                            SplashActivity2.this.getSharedPreferences(PublicUtils.PREFERENCE_NAME, Context.MODE_PRIVATE)
                                    .edit().remove(PublicUtils.PREFERENCE_NAME_PHONE).commit();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        SplashActivity2.this.getSharedPreferences(PublicUtils.PREFERENCE_NAME, Context.MODE_PRIVATE)
                                .edit().remove(PublicUtils.PREFERENCE_NAME_PHONE).commit();

                    }
                }

                @Override
                public void onFailure(Throwable error, String content) {

                }
            });

        } else {
            getVersionInfo();
        }

    }


//	/**
//	 * 没有正常获取UBD信息
//	 * @param code -1 后台代码异常,7 没传IMSI,8 没有取到手机号,9 UBD接口异常,1 UBD返回json异常
//	 */
//	private Dialog udbTipDialog = null;
//	private void getUdbTipInfo(int resCode){
//		StringBuffer message = new StringBuffer();
//		if (resCode == 8) {
//			message.append("本手机可能不是合法用户,是否继续验证?");
//		}else {
//			message.append("手机终端身份验证出错(").append(resCode).append("),是否继续验证?");
//		}
//		message.append("\n技术支持 400-818-8809");
//		if (udbTipDialog == null) {
//			udbTipDialog = new Dialog(this, R.style.transparentDialog);
//		}
//		View view = View.inflate(this, R.layout.udb_tip_dialog, null);
//		TextView backContent = (TextView) view.findViewById(R.id.tv_udb_tip_content);
//		Button confirm = (Button) view.findViewById(R.id.btn_udb_confirm);
//		Button cancel = (Button) view.findViewById(R.id.btn_udb_cancel);
//		backContent.setText(message);
//		confirm.setOnClickListener(new OnClickListener() {
//
//			/**
//			 * 继续
//			 */
//			@Override
//			public void onClick(View v) {
//				udbTipDialog.dismiss();
//				intentFreeLogin();
//			}
//		});
//		cancel.setOnClickListener(new OnClickListener() {
//			/**
//			 * 取消
//			 */
//			@Override
//			public void onClick(View v) {
//				udbTipDialog.dismiss();
//				SplashActivity2.this.finish();
//			}
//		});
//		udbTipDialog.setContentView(view);
//		udbTipDialog.setCancelable(false);// 将对话框设置为模态
//		if (udbTipDialog!=null && !udbTipDialog.isShowing()) {
//			udbTipDialog.show();
//		}
//	}

    /**
     * 没有获取UDB信息
     */
    private void getUdbFailure() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity2.this);
        builder.setTitle(PublicUtils.getResourceString(SplashActivity2.this, R.string.tip));
        builder.setMessage(PublicUtils.getResourceString(SplashActivity2.this, R.string.welcom_info4));
        builder.setPositiveButton(PublicUtils.getResourceString(SplashActivity2.this, R.string.submit_btn_again), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //getVersionInfo();
                checkPhoneNumber();
            }
        });
        builder.setNegativeButton(PublicUtils.getResourceString(SplashActivity2.this, R.string.Cancle), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                SplashActivity2.this.finish();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }

    /**
     * 无法获取到IMSI
     */
    private void unKnownIMSI() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(PublicUtils.getResourceString(this, R.string.tip));
        builder.setMessage(PublicUtils.getResourceString(this, R.string.open_net_service4));
        builder.setPositiveButton(PublicUtils.getResourceString(this, R.string.open_net_service5), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                SplashActivity2.this.finish();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }

    /**
     * IMSI发生了变化 说明用户换了手机卡
     */
    private void changeIMSI() {
        // 如果用户换卡，则提示用户重新登录如果用户选择重新登录则进行初始化
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(PublicUtils.getResourceString(this, R.string.welcom_info2));
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //清除所有存储的缓存信息并初始化
                PublicUtils.clearAllSP(SplashActivity2.this);
                //getVersionInfo();
                checkPhoneNumber();
            }
        });
        builder.create().show();
    }

    /**
     * 判断SD卡空间
     *
     * @return sizeMb SD卡的剩余空间大小已Mb为单位
     */
    private int getSdcardFreeSpace() {
        int free = 0;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) { // sd卡可用
            String sdcard = Environment.getExternalStorageDirectory().getPath();
            StatFs statFs = new StatFs(sdcard);
            long blockSize = statFs.getBlockSize(); // 以字节为单位，一个文件系统
            long blocks = statFs.getAvailableBlocks();// 获取当前可用的存储空间
            free = (int) (blocks * blockSize / (1024 * 1024)) + 1;
        }
        return free;
    }

    /**
     * 设置SD卡路径
     * 如果路径不存在则新建一个
     */
    private void setSdcardPath() {
        //获取SD卡的剩余空间返回值以M为单位
        int free = getSdcardFreeSpace();
        if (free < 20) {//如果小于20M提示用户并退出程序
            ToastOrder.makeText(getApplicationContext(), R.string.open_net_service2, ToastOrder.LENGTH_SHORT).show();
            finish();
        } else if (free < 50) {//如果大于20小于50则提示用户，程序继续运行
            ToastOrder.makeText(getApplicationContext(), R.string.open_net_service3, ToastOrder.LENGTH_SHORT).show();
        }
        File dir = new File(Constants.SDCARD_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * 卸载APP
     *
     * @param packageName 要卸载的APP的包名
     */
    public void uninstallAPK(String packageName) {
        Uri uri = Uri.parse("package:" + packageName);
        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
        this.startActivity(intent);
    }

    /**
     * 获取版本信息
     */
    private void getVersionInfo() {

//		String verUrl = new StringBuffer().append(UrlInfo.getUrlApkVersionInfo(SplashActivity.this)).append("?cv=").append(Constants.CURRENT_VERSION).toString();

        URLWrapper urlWrapper = new URLWrapper(UrlInfo.getUrlApkVersionInfo(SplashActivity2.this));
        urlWrapper.addParameter("cv", Constants.CURRENT_VERSION);
        String verUrl = urlWrapper.getRequestURL();
        int timeOut = TextUtils.isEmpty(PublicUtils.receivePhoneNO(this)) ? GcgHttpClient.TIME_OUT_LONG : GcgHttpClient.TIME_OUT_SHORT;
        GcgHttpClient.getInstance(this, timeOut).get(verUrl, versionInfoParms(), new HttpResponseListener() {

            @Override
            public void onFailure(Throwable error, String content) {
                String phoneno = PublicUtils.receivePhoneNO(SplashActivity2.this);
                if (TextUtils.isEmpty(phoneno)) {//网络异常的时候如果手机号是空就提示用户不允许通过，如果手机号不是空就通过
                    getUdbFailure();
                } else {
                    intentFreeLogin(null);
                }
            }

            @Override
            public void onFinish() {
                //无论请求成功失败都开始下载logo
                downloadLogo();
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(int statusCode, String content) {
                resetVersionInfo(content);
            }

        });
    }


    private RequestParams versionInfoParms() {
        RequestParams params = new RequestParams();
        params.add("userid", String.valueOf(SharedPreferencesUtil.getInstance(this).getUserId()));
        params.add("username", SharedPreferencesUtil.getInstance(this).getUserName());
        params.add("roleid", String.valueOf(SharedPreferencesUtil.getInstance(this).getRoleId()));
        params.add("orgid", String.valueOf(SharedPreferencesUtil.getInstance(this).getOrgId()));
        return params;
    }

    /**
     * 免费版本鉴权
     */

    private void intentFreeLogin(final String code) {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                //获取版本信息执行完直接跳转到登陆页面
//				Intent intent = null;
//				if (TextUtils.isEmpty(PublicUtils.receivePhoneNO(getApplicationContext()))) {
//					intent = new Intent(SplashActivity2.this, LoginForFreeActivity.class);
//				}else{
//					intent = new Intent(SplashActivity2.this, LoginActivity.class);
//				}
//				SplashActivity2.this.startActivity(intent);
//				SplashActivity2.this.finish();

                if ("0003".equals(code)) {
                    Intent intent = null;
                    if (TextUtils.isEmpty(PublicUtils.receivePhoneNO(getApplicationContext()))) {
                        intent = new Intent(SplashActivity2.this, ZRLoginActivity.class);
                        SplashActivity2.this.startActivity(intent);
                        SplashActivity2.this.finish();
                    } else {//有手机号说明以前初始化成功过此时不用再次让用户输入手机号
                        invalidUser();
                    }
                } else {
                    Intent intent = null;
                    if (TextUtils.isEmpty(PublicUtils.receivePhoneNO(getApplicationContext()))) {
                        intent = new Intent(SplashActivity2.this, ZRLoginActivity.class);
                    } else {
                        if (PublicUtils.ISDEMO) {
                            //如果已经初始化，则跳转到主界面，否则跳转到初始化界面
                            SharedPreferencesUtil prefs = SharedPreferencesUtil.getInstance(getApplicationContext());
                            intent = new Intent(getApplicationContext(), prefs.getIsInit() ? HomePageActivity.class : InitActivity.class);

                        } else {
//							intent = new Intent(SplashActivity2.this, LoginActivity.class);
                            SharedPreferencesUtil prefs = SharedPreferencesUtil.getInstance(getApplicationContext());

                            String locVersion = SharedPreferencesUtil.getInstance(SplashActivity2.this).getLocationVersion();
                            if (!TextUtils.isEmpty(locVersion) && (Integer.valueOf(SplashActivity2.this.getString(R.string.IS_NEED_INIT_VERSION)) > Integer.valueOf(locVersion))) {
                                // 如果本地版本小于等于在需要重新初始化的历史本版时
                                SharedPreferencesUtil.getInstance(SplashActivity2.this).setIsInit(false);
                            }
                            // 获取当前版本号
                            String curVersion = PublicUtils.getCurVersion(SplashActivity2.this);
                            // 存放当前版本号到本地
                            SharedPreferencesUtil.getInstance(SplashActivity2.this).setLocationVersion(curVersion);

                            //如果已经初始化，则跳转到主界面，否则跳转到初始化界面
                            intent = new Intent(getApplicationContext(), prefs.getIsInit() ? HomePageActivity.class : InitActivity.class);

                        }

                    }
                    SplashActivity2.this.startActivity(intent);
                    SplashActivity2.this.finish();
                }

            }
        }, 1000);
        downloadLogo();
    }

    /**
     * 无效用户
     */
    private void invalidUser() {
        if (!SplashActivity2.this.isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(PublicUtils.getResourceString(this, R.string.tip));
            builder.setMessage(PublicUtils.getResourceString(this, R.string.un_search_data1));
            builder.setPositiveButton(PublicUtils.getResourceString(this, R.string.Confirm), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    SplashActivity2.this.finish();
                }
            });
            builder.setCancelable(false);
            builder.create().show();
        }
    }

    /**
     * 重置最新version信息
     *
     * @param json
     */
    private void resetVersionInfo(String json) {
        JLog.d(TAG, "version==>" + json);
        try {
            JSONObject jsonObject = new JSONObject(json);
            String resultcode = jsonObject.getString("resultcode");
            if ("0000".equals(resultcode)) {
                if (jsonObject.has("version")) {
                    JSONObject versionObj = jsonObject.getJSONObject("version");
                    //服务端最新APK安装包的MD5
                    if (versionObj.has("md5code")) {
                        String md5code = versionObj.getString("md5code");
                        SharedPreferencesUtil.getInstance(getApplicationContext()).saveMD5Code(md5code);
                    }
                    //服务端最新APK安装包的下载URL
                    if (versionObj.has("url")) {
                        String url = versionObj.getString("url");
                        SharedPreferencesUtil.getInstance(getApplicationContext()).saveDownUrl(url);
                    }
                    //服务端最新APK安装包的版本
                    if (versionObj.has("version")) {
                        String version = versionObj.getString("version");
                        SharedPreferencesUtil.getInstance(getApplicationContext()).saveNewVersion(version);
                    }
                    //服务端最新APK安装包的大小 KB为单位
                    if (versionObj.has("size")) {
                        String size = versionObj.getString("size");
                        SharedPreferencesUtil.getInstance(getApplicationContext()).saveApkSize(size);
                    }
                    //用户的有效期的开始日期
                    if (versionObj.has(CacheData.AVAIL_START)) {
                        SharedPreferencesUtil.getInstance(getApplicationContext()).setUsefulStartDate(versionObj.getString(CacheData.AVAIL_START));
                    }
                    //用户的有效期的结束日期
                    if (versionObj.has(CacheData.AVAIL_END)) {
                        SharedPreferencesUtil.getInstance(getApplicationContext()).setUsefulEndDate(versionObj.getString(CacheData.AVAIL_END));
                    }
                    //用户手机号码
                    if (PublicUtils.isValid(versionObj, "resCode")) {
                        int resCode = versionObj.getInt("resCode");
                        if (resCode == 0) {//取手机号成功
                            String phoneno = versionObj.getString("phone_no");
                            getApplicationContext().getSharedPreferences(PublicUtils.PREFERENCE_NAME, Context.MODE_PRIVATE).edit().putString(PublicUtils.PREFERENCE_NAME_PHONE, phoneno).commit();
                            if (PublicUtils.isValid(versionObj, "isInit")) {
                                String isInit = versionObj.getString("isInit");
                                initTipUser(isInit);
                            } else {
                                intentFreeLogin("0000");
                            }
                        } else {//后台代码异常 udb没有获取到手机号码
//							getUdbTipInfo(resCode);
                            intentFreeLogin("0000");
                        }
                    } else {//如果没有手机号信息就直接跳转到登陆
                        intentFreeLogin("0000");
                    }
                } else {
                    intentFreeLogin("0000");
                }
            } else if ("0002".equals(resultcode)) {//三码验证不通过
                codeNotPass();
            } else {//0003 表示用户手机号是无效用户 //清除本地缓存的用户手机号
                intentFreeLogin("0003");
            }
        } catch (Exception e) {
            JLog.d(TAG, e.toString());
//			getUdbTipInfo(1);
            intentFreeLogin(null);
        }
    }

    /**
     * 提示用户初始化
     * 1,0,1,0格式显示按userid,roleid,orgid,username,的顺序排列 1代表变化了要提示用户初始化，0代表没变化
     */
    private void initTipUser(String code) {
        boolean isInit = SharedPreferencesUtil.getInstance(this).getIsInit();
        String[] init = code.split(",");
        if (isInit && init.length == 4 && code.contains("1")) {
            StringBuffer msg = new StringBuffer();
            if ("1".equals(init[0])) {
                msg.append(",").append(PublicUtils.getResourceString(SplashActivity2.this, R.string.user_id_changed));
            }
            if ("1".equals(init[1])) {
                msg.append(",").append(PublicUtils.getResourceString(SplashActivity2.this, R.string.user_id_changed1));
            }
            if ("1".equals(init[2])) {
                msg.append(",").append(PublicUtils.getResourceString(SplashActivity2.this, R.string.user_id_changed2));
            }
            if ("1".equals(init[3])) {
                msg.append(",").append(PublicUtils.getResourceString(SplashActivity2.this, R.string.user_id_changed3));
            }
            msg.append(",").append(PublicUtils.getResourceString(SplashActivity2.this, R.string.user_id_changed4));
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(PublicUtils.getResourceString(SplashActivity2.this, R.string.tip));
            builder.setMessage(msg.substring(1));
            builder.setPositiveButton(PublicUtils.getResourceString(SplashActivity2.this, R.string.confirm), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferencesUtil.getInstance(SplashActivity2.this).setIsInit(false);
                    intentFreeLogin(null);
                }
            });
            builder.setCancelable(false);
            builder.create().show();
        } else {
            intentFreeLogin(null);
        }

    }

    /**
     * 用户三码鉴权不通过
     */
    private void codeNotPass() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(PublicUtils.getResourceString(this, R.string.tip));
        builder.setMessage(PublicUtils.getResourceString(this, R.string.welcom_info3));
        builder.setPositiveButton(PublicUtils.getResourceString(this, R.string.Confirm), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                SplashActivity2.this.finish();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }


    /**
     * 开启一次定位，获取上一次定位的时间，如果时间是空则说明是第一次定位，则直接开启定位，否则就比较上次定位和当前的时间
     * 如果时间差大于1分钟则开启一次定位
     */
    private void startLocationInBackground() {
        //获取存在SharedPreferencesUtil里的上次手机定位的时间
        String time = SharedPreferencesUtil.getInstance(getApplicationContext()).getLocationPhoneTime();
        if (TextUtils.isEmpty(time)) {//如果时间是空则开始定位
            this.startService(new Intent(this, LocationService.class));
        } else {//比较上次定位和当前的时间差，如果大于1分钟则开始定位
            long l = DateUtil.compareCurrentDateStr(time);
            if (l > 1000 * 60) {
                this.startService(new Intent(this, LocationService.class));
            }
        }

    }

    /**
     * 打开网络服务，如果手机系统版本小于等于10就通过代码直打开网络，否则通过代码无法直接打开网络，则需要跳转到页面让用户手动打开
     */
    private void openNetworkSettings() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(PublicUtils.getResourceString(this, R.string.open_net_service)).setMessage(PublicUtils.getResourceString(this, R.string.open_net_service1));
        builder.setCancelable(false).setPositiveButton(PublicUtils.getResourceString(this, R.string.set_center), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Go to the activity of settings of wireless
                if (android.os.Build.VERSION.SDK_INT <= 10) {
                    Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
                    ComponentName cName = new ComponentName("com.android.phone", "com.android.phone.Settings");
                    intent.setComponent(cName);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
                    startActivity(intent);
                }
            }
        });
        builder.setNegativeButton(PublicUtils.getResourceString(this, R.string.Cancle), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                SplashActivity2.this.finish();
            }
        });
        builder.create().show();
    }

    /**
     * 下载企业LOGO图片
     * 第一次登陆的时候企业LOGO下载URL会存储在SharedPreferencesUtil中，获取到SharedPreferencesUtil存的路径以后、
     * 判断如果logo图片不存在则需要下载图片，将下载的图片存储在/sdcard/grirms/logo/目录下
     */
    private void downloadLogo() {
        new Thread() {
            public void run() {
                String logoUrl = SharedPreferencesUtil.getInstance(SplashActivity2.this).getPhoneLogoPath();
                if (!TextUtils.isEmpty(logoUrl)) {
                    File logoFile = new File(Constants.COMPANY_LOGO_PATH);
                    HttpHelper httpHelper = new HttpHelper(SplashActivity2.this);
                    if (logoFile.exists()) {
                        String[] str = logoFile.list();
                        if (str == null || str.length == 0) {
                            httpHelper.downloadFile(logoUrl, Constants.COMPANY_LOGO_PATH);
                        } else {
                            String fileName = str[0];
                            int index = logoUrl.lastIndexOf("/");
                            String urlFile = logoUrl.substring(index + 1);
                            if (!fileName.equals(urlFile)) {
                                httpHelper.downloadFile(logoUrl, Constants.COMPANY_LOGO_PATH);
                            }
                        }

                    } else {//如果文件不存在就下载
                        httpHelper.downloadFile(logoUrl, Constants.COMPANY_LOGO_PATH);
                    }
                }
            }

            ;
        }.start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_HOME:
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * @param type 当前app类型
     *             根据传入的类型来设置企业标识信息
     */
    private void initialize(Version type) {
        SharedPreferencesUtil prefs = SharedPreferencesUtil.getInstance(getApplicationContext());
//		String value1 = "";
//		String value2 = "GRIRMS";
//		String value3 = "云韬略";
        String value1 = prefs.getPhoneName1();
        String value2 = prefs.getPhoneName2();
        String value3 = prefs.getPhoneName3();
        String value4 = prefs.getPhoneName1Size();
        String value5 = prefs.getPhoneName2Size();
        String value6 = prefs.getPhoneName3Size();
        String value7 = prefs.getPhoneName1AlignTAype();
        String value8 = prefs.getPhoneName2AlignTAype();
        String value9 = prefs.getPhoneName3AlignTAype();
        setContentView(R.layout.splash);
        TextView tv = (TextView) findViewById(R.id.splash_icontitle);


        tv.setText(getResources().getString(R.string.app_normalname));

        //企业信息标识
        title1 = (TextView) findViewById(R.id.title1);
        title2 = (TextView) findViewById(R.id.title2);
        title3 = (TextView) findViewById(R.id.title3);
        //企业LOGO
        logo = (ImageView) findViewById(R.id.logo);
        im_ct_logo = (ImageView) findViewById(R.id.im_ct_logo);
//		if (PublicUtils.ISDEMO) {
        logo.setImageResource(R.drawable.icon_main);    //icon_main
        title1.setText("");
        title2.setText(PublicUtils.getResourceString(this, R.string.app_normalname));
        title3.setText(type.getTitle3());
//		}else{
//			logo.setImageResource(type.getLogoResId());
//			title1.setText(type.getTitle1());
//			title2.setText(type.getTitle2());
//			title3.setText(type.getTitle3());
//		}


        switch (type) {
            case VER_NORAML:
            case VER_45:
                if (!TextUtils.isEmpty(value1) || !TextUtils.isEmpty(value2) || !TextUtils.isEmpty(value3)) {//如果已经获取了企业信息则用获取的企业信息更改掉默认的信息
                    title1.setText(value1);
                    title2.setText(value2);
                    title3.setText(value3);
                    //根据获取的信息设置字体的大小
                    try {
                        int size = Integer.parseInt(prefs.getPhoneName1Size());
                        title1.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
                    } catch (Exception e) {
                    }

                    try {
                        int size = Integer.parseInt(prefs.getPhoneName2Size());
                        title2.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
                    } catch (Exception e) {
                    }

                    try {
                        int size = Integer.parseInt(prefs.getPhoneName3Size());
                        title3.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
                    } catch (Exception e) {
                    }

                    //设置企业企业信息显示的位置
                    if (value7.matches("[123]")) {
                        title1.setGravity(PublicUtils.align2Gravity(Integer.parseInt(value7)));
                    }

                    if (value8.matches("[123]")) {
                        title2.setGravity(PublicUtils.align2Gravity(Integer.parseInt(value8)));
                    }

                    if (value9.matches("[123]")) {
                        title3.setGravity(PublicUtils.align2Gravity(Integer.parseInt(value9)));
                    }

                    //如果有的值为空，则不显示该标识
                    if (TextUtils.isEmpty(value1))
                        title1.setVisibility(View.GONE);
                    if (TextUtils.isEmpty(value2))
                        title2.setVisibility(View.GONE);
                    if (TextUtils.isEmpty(value3))
                        title3.setVisibility(View.GONE);

                }
                if (im_ct_logo != null) {//4.5的时候隐藏中国电信图标
                    im_ct_logo.setVisibility(View.INVISIBLE);
                }
                //从企业LOGO下载的URL中获取企业LOGO的名字，并根据名字从SD卡中找到已经下载的图片
                String logoUrl = SharedPreferencesUtil.getInstance(SplashActivity2.this).getPhoneLogoPath();
                if (!TextUtils.isEmpty(logoUrl)) {
                    //从URL中截取最后“/”后最后一位的内容就是图片的名字信息
                    int index = logoUrl.lastIndexOf("/");
                    String fileName = logoUrl.substring(index + 1);
                    File f = new File(Constants.COMPANY_LOGO_PATH + fileName);
                    if (f.exists()) {//如果图片存在就给企业LOGO设置该图片
                        logo.setImageBitmap(BitmapFactory.decodeFile(f.getAbsolutePath()));
                    }
                }
                break;

            case VER_YILI:
                title1.setVisibility(View.GONE);//如果是yili的情况下title1要隐藏不显示
        }

        //读取当前手机屏幕的宽和高，做适配使用
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);

        //当前手机屏幕的宽
        Constants.SCREEN_WIDTH = dm.widthPixels;
        //当前手机屏幕的高
        Constants.SCREEN_HEIGHT = dm.heightPixels;
        //当前应用的名字
        Constants.CURRENT_VERSION = ApplicationHelper.getApplicationName(this).appVersionName;
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(SplashActivity2.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(SplashActivity2.this);
    }
}