package com.yunhu.yhshxc.func;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.LocationManager;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.activity.CaptureActivity;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.activity.ChangeModuleFuncActivity;
import com.yunhu.yhshxc.activity.NfcFuncActivity;
import com.yunhu.yhshxc.activity.PhotoPreActivity;
import com.yunhu.yhshxc.activity.SearchChooseActivity;
import com.yunhu.yhshxc.activity.SqlBigDataActivity;
import com.yunhu.yhshxc.activity.TableCompView;
import com.yunhu.yhshxc.activity.popup.PhotoPopupWindow;
import com.yunhu.yhshxc.bo.AssetsBean;
import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.FuncCache;
import com.yunhu.yhshxc.bo.LocationResult;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.bo.Module;
import com.yunhu.yhshxc.bo.Org;
import com.yunhu.yhshxc.bo.OrgStore;
import com.yunhu.yhshxc.bo.OrgUser;
import com.yunhu.yhshxc.bo.ReplenishSearchResult;
import com.yunhu.yhshxc.bo.Submit;
import com.yunhu.yhshxc.bo.SubmitItem;
import com.yunhu.yhshxc.comp.AbsCameraComp;
import com.yunhu.yhshxc.comp.CameraComp;
import com.yunhu.yhshxc.comp.CameraCompForLink;
import com.yunhu.yhshxc.comp.CompDialog;
import com.yunhu.yhshxc.comp.EditCompFunc;
import com.yunhu.yhshxc.comp.MediaManager;
import com.yunhu.yhshxc.comp.ProductCode;
import com.yunhu.yhshxc.comp.ScanInputCode;
import com.yunhu.yhshxc.comp.SqlInComp;
import com.yunhu.yhshxc.comp.location.LocationControlListener;
import com.yunhu.yhshxc.comp.location.LocationForFunc;
import com.yunhu.yhshxc.comp.menu.FuncBtnLayoutMenu;
import com.yunhu.yhshxc.comp.menu.FuncLayoutMenu;
import com.yunhu.yhshxc.comp.menu.RecordPopupWindow;
import com.yunhu.yhshxc.comp.menu.RecordPopupWindow.OnRefreshListner;
import com.yunhu.yhshxc.database.DictDB;
import com.yunhu.yhshxc.database.FuncCacheDB;
import com.yunhu.yhshxc.database.FuncDB;
import com.yunhu.yhshxc.database.OrgDB;
import com.yunhu.yhshxc.database.OrgStoreDB;
import com.yunhu.yhshxc.database.OrgUserDB;
import com.yunhu.yhshxc.database.SubmitDB;
import com.yunhu.yhshxc.database.SubmitItemDB;
import com.yunhu.yhshxc.database.SubmitItemTempDB;
import com.yunhu.yhshxc.database.VisitFuncDB;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.exception.DataComputeException;
import com.yunhu.yhshxc.expand.StoreExpandActivity;
import com.yunhu.yhshxc.expand.StoreModuleExpandUtil;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.list.activity.ShowImageActivity;
import com.yunhu.yhshxc.listener.ProductCodeListener;
import com.yunhu.yhshxc.location.LocationFactory;
import com.yunhu.yhshxc.location.LocationMaster;
import com.yunhu.yhshxc.location2.RemindDialog;
import com.yunhu.yhshxc.module.replenish.QueryFuncActivity;
import com.yunhu.yhshxc.order.MenuOrderActivity;
import com.yunhu.yhshxc.order3.Order3FuncActivity;
import com.yunhu.yhshxc.parser.ReplenishParse;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.FileHelper;
import com.yunhu.yhshxc.utility.PhoneModelManager;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.PublicUtils.TypeValue;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil2;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.visit.InfoDetailActivity;
import com.yunhu.yhshxc.visit.VisitFuncActivity;
import com.yunhu.yhshxc.visitors.TXRActivity;
import com.yunhu.yhshxc.visitors.TXRListActivity;
import com.yunhu.yhshxc.wechat.Util.SharedPrefrencesForWechatUtil;
import com.yunhu.yhshxc.wechat.content.ImageGridActivity;
import com.yunhu.yhshxc.widget.ToastOrder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gcg.org.debug.JLog;

import static com.yunhu.yhshxc.bo.Func.TYPE_MULTI_CHOICE_SPINNER_COMP;

/**
 * AbsFuncActivity是所有上报修改页面的父类子类有以下： ChangeModuleFuncActivity 自定义模块的 "审核" "修改 "
 * "改派" LinkFuncActivity 超链接 ModuleFuncActivity 自定义模块的 "上报" "下发" "执行"
 * QueryFuncActivity 自定义的查询 和考勤的查询 AttendanceFuncActivity 考勤的上报、
 *
 * @author jishen
 * @version 2013-05-28
 */
public abstract class AbsFuncActivity extends AbsBaseActivity implements LocationControlListener, ProductCodeListener {
    private ScrollView mFuncScrollView;//用于滑动到指定位置

    // private TableCompView tableCompView;
    private HashMap<Integer, TableCompView> tableListMap = new HashMap<Integer, TableCompView>();// 存储该页面创建的表格对象,用于获取相应对象进行操作>
    private HashMap<Integer, View> layouts = new HashMap<>();//存储添加Func的师徒布局,用于计算其位置做相应的滑动
    protected LinearLayout funcLayout = null; // 屏幕滚动部分
    protected LinearLayout showDataLinearLayout = null;// 预览按钮
    public View hookView;// 对勾view
    public TextView TView;// 对勾view
    public List<Func> funcList = null; // 整个布局中所用到的控件
    public List<Func> buttonFuncList = null; // 所定义的button控lo
    protected Map<Integer, View> unOperateMap = null;// 记录未处理的控件 key:funcId
    // Value:对应的View
    protected Map<Integer, View> operatedMap = null;// 记录已处理的控件 key funcId
    // Value：对应的值
    protected Map<Integer, String> selectStringMap = null;
    public Map<Integer, String> orgMap;// 机构联动使用 key存orgLevel value存操作的值orgIDO
    public Map<Integer, String> chooseMap;// 普通外部控件级联

    private ArrayList<String> orgMapValueList = null;// 查询修改的时候传过来的组织机构的值

    private int clickFuncIndex = 0; // 当前点击的控件的下标
    private Integer[] inputOrderArr = null; // func 中的input_order distinct
    private String startTime = null; // 开始操作func的开始时间o

    public boolean isCheckIn = false; // 是否CheckIn true表示点击过checkin
    // false表示没有点击过chickin
    public boolean isCanClick = true; // 防止重复点击 true 表示可以点击 false表示不可点击
    public boolean isSqlLink = false;// 是否是修改sql超链接 true表示是超链接sql false不是sql超链接

    public Dialog dialog = null;
    public AlertDialog alDialog = null;
    public Bundle bundle = null;// 传递数据的bundle
    public Integer planId, wayId, storeId, targetId, menuType;// 计划ID 线路ID 店面ID
    // 数据ID
    // 菜单类型（访店自定义模块
    // 考勤）
    public Integer taskId = null;// 双向中double表中的id
    public Integer linkId = 0; // 是否为超链接 0 不是超链接 1是超链接
    public Integer linkFuncId = null; // 超链接控件ID 用来提交patchId
    public int modType;// 自定义模块的类型 上报 下发 审核 执行 查询...
    public int infoType;// 判断店面信息修改 0不是 1 是

    public int sqlStoreId;// sql查询的时候如果是在超链接模块里key=1的时候需要传storeId
    /**
     * storeName 店面名称 wayName 线路名称
     */
    public String storeName, wayName;

    public ImageView title_finish_iv;// 访店完成checkin完成图标

    /*
     * 拍照
	 */
    private String paramName = null;// 照片对应的paramName
    private String fileName = null; // 照片的名字
    private LocationFactory locationFactory = null;

    private HistorySearchPopupWindow historySearchPopupWindow;// 历史信息查询
    // 立即上报PopupWindow
    private RecordPopupWindow recordPopupWindow = null;// 录音PopupWindow

    public boolean isLinkActivity = false;// 当前页面是否是超链接 true是超链接 false 不是超链接

    private ProductCode productCode = null;// 串码
    private ScanInputCode scanInputProductCode = null;// 可输入类型的扫描控件

    public Func currentFunc = new Func();
    public boolean codeSubmitControl = true;// 有串码的时候控件是否提交 true提交，false不提交

    public boolean isNoWait;

    public String attenTime = null;// 考勤时间

    public StoreModuleExpandUtil storeModuleExpandUtil;
    public Func codeFunc;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    public LinearLayout submitDataLayout;

    private List<ListFuncViewT1> listFuncViewT1s = null; //固定资产条目容器
    private ListFuncViewT1 currentListFunt1;//记录当前正在操作的

    private LinearLayout ll_addsacn;//单独添加固定资产扫描控件
    private LinearLayout ll_scan_add;//标题栏扫描按钮

    private int txrId = 2045909;

    /**
     * 解析完成常量
     */
    public static final int PARSE_OK = 98;

    /**
     * 解析失败常量
     */
    public static final int PARSE_FAIL = 97;

    /**
     * 加载更多常量
     */
    public static final int LOAD_MORE_FINISH = 99;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // int newSize = 4 * 1024 * 1024 ; //设置最小堆内存大小为4MB
        // VMRuntime.getRuntime().setMinimumHeapSize(newSize);
        // VMRuntime.getRuntime().setTargetHeapUtilization(0.75); //
        // 设置堆内存的利用率为75%

        super.onCreate(savedInstanceState);
        setContentView(R.layout.func_);
        ll_addsacn = (LinearLayout) findViewById(R.id.ll_addsacn);
        submitDataLayout = (LinearLayout) findViewById(R.id.ll_btn_layout);
        options = new DisplayImageOptions.Builder().showStubImage(R.drawable.ic_empty)
                .showImageForEmptyUri(R.drawable.ic_empty).showImageOnFail(R.drawable.ic_empty).cacheInMemory()
                .cacheOnDisc().displayer(new RoundedBitmapDisplayer(0)).build();
        if (savedInstanceState != null) {// 非正常关闭
            JLog.d(TAG, "<--Activity非正常关闭过!!-->");
            restoreConstants(savedInstanceState);
            bundle = new Bundle(savedInstanceState);
            SharedPreferencesUtil2.getInstance(this).saveIsAnomaly(false);
            SharedPreferencesUtil2.getInstance(this).saveMenuId(-1);
        } else {
            bundle = getIntent().getBundleExtra("bundle");
        }
        initBase();
        init();
        initLayout();
        initButtnTypeLayout();

        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                theOperatedFinish();
                onClickKeyCodeBack();
                // finish();
            }
        });
    }

    public void expandShow() {
        FrameLayout ll_title = (FrameLayout) findViewById(R.id.ll_title);
        LinearLayout ll_expand = (LinearLayout) findViewById(R.id.ll_expand);

        ll_title.setVisibility(View.GONE);
        ll_expand.setVisibility(View.VISIBLE);
    }

    /**
     * 初始化数据以及获取传过来的数据
     */
    public int menuId;
    public String menuName;

    @Override
    public File getFileStreamPath(String name) {
        return super.getFileStreamPath(name);
    }

    private void init() {
        mFuncScrollView = (ScrollView) findViewById(R.id.func_scrollview);
        funcLayout = (LinearLayout) findViewById(R.id.ll_func);
        showDataLinearLayout = (LinearLayout) findViewById(R.id.ll_btn_layout);
        if (TextUtils.isEmpty(startTime)) {
            startTime = DateUtil.getCurDateTime();
        }
        locationFactory = new LocationFactory(this);
        if (isNormal) {// 适配异常退出机型
            orgMap = new HashMap<Integer, String>();
            chooseMap = new HashMap<Integer, String>();
        }
        listFuncViewT1s = new ArrayList<>();
        unOperateMap = new HashMap<Integer, View>();
        operatedMap = new HashMap<Integer, View>();
        selectStringMap = new HashMap<Integer, String>();
        planId = bundle.getInt("planId");
        targetId = bundle.getInt("targetId");
        wayId = bundle.getInt("wayId");
        storeId = bundle.getInt("storeId");
        menuType = bundle.getInt("menuType", 0);
        menuName = bundle.getString("menuName");
        taskId = bundle.getInt("taskId", 0);
        linkFuncId = bundle.getInt("funcId", 0);
        isSqlLink = bundle.getBoolean("isSqlLink");
        is_store_expand = bundle.getInt("is_store_expand");
        menuId = bundle.getInt("menuId", 0);
        isNoWait = getIntent().getBooleanExtra("isNoWait", true);
        funcList = getFuncList();
        forCheck();//控件按后台配置的时间段来判断显示与否    2017.04.01
        buttonFuncList = getButtonFuncList();
        inputOrderArr = findFuncListByInputOrder();

        orgMapValueList = bundle.getStringArrayList("map");// 查询和审核的时候结构的值
        bundle.putBoolean("isNoWait", isNoWait);// 是否无等待提交
    }

    /**
     * 根据要显示的数据初初始化页面
     */

    private StoreExpandUtil getlist;
    private int is_store_expand = 0;

    public void initLayout() {

        ll_scan_add = (LinearLayout) findViewById(R.id.ll_scan_add);
        ll_scan_add.setOnClickListener(this);
        if(targetId==2031720){
            ll_scan_add.setVisibility(View.VISIBLE);
        }else{
            ll_scan_add.setVisibility(View.GONE);
        }
        /**
         * 是否是店面拓展，is_store_expand = 1 是，is_store_expand = 0 否
         */
        if (is_store_expand == 1) {
            getlist = new StoreExpandUtil(this);
            funcList = getlist.getFuncs(funcList);
        }
        if (funcList == null || funcList.isEmpty())
            return;
        int len = funcList.size();
        LinearLayout ll_two = null;
        if (len == 1) { // 添加一个view
            ll_two = new LinearLayout(this);
            ll_two.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            ll_two.addView(getFuncView(0));
            // ll_two.addView(getFuncView(-1));
            funcLayout.addView(ll_two);
        } else {

            for (int i = 0; i < len; i++) { // 表示要显示两个小View

//				boolean b = checkFuncTimeOptions(funcList.get(i).getRestrictType(),funcList.get(i).getRestrictRule());
//				if(b){
                ll_two = new LinearLayout(this);
                ll_two.setLayoutParams(
                        new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                // ll_two.setOrientation(LinearLayout.HORIZONTAL);
                // ll_two.addView(getFuncView(i - 1));
                Func cFunc = this.funcList.get(i);
//                listNewFunc = cFunc;
                if (cFunc.getIsScan()==1&&cFunc.getType()==TYPE_MULTI_CHOICE_SPINNER_COMP){
                    //新功能控件的处理
                  final ListFuncViewT1   listFuncViewT1 = new ListFuncViewT1(this);
                    listFuncViewT1.setFunc(cFunc);
                    listFuncViewT1s.add(listFuncViewT1);
                    View listFuncView = listFuncViewT1.getView();
                    listFuncView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    funcLayout.addView(listFuncView, funcLayout.getChildCount());
//                    ll_addsacn.addView(listFuncView);
                    //添加功能点击事件
                    listFuncViewT1.setOnListFuncListener(new ListFuncViewT1.OnListFuncListener() {
                        @Override
                        public void onAdd(Func func) {
                             currentListFunt1 = listFuncViewT1;
                            //点击了添加控件,调出新界面进行数据获取,返回后添加
                            Intent intent = new Intent(AbsFuncActivity.this, SearchChooseActivity.class);
                            //查询出已选择数据.传递给选择界面使其呈选中状态
                            intent.putExtra("status",func.getScanStatus());
                            List<AssetsBean> seLectata = listFuncViewT1.getData();
                            intent.putExtra("select_data", (Serializable) seLectata);
                            AbsFuncActivity.this.startActivityForResult(intent, 211);
                        }

                        @Override
                        public void onScan(Func func) {
                            currentListFunt1 = listFuncViewT1;
                            //点击了扫描控件,调出扫描框,扫描结果返回进行处理
                            Intent intent = new Intent(AbsFuncActivity.this, CaptureActivity.class);
                            startActivityForResult(intent, 212);

                        }

                        @Override
                        public void onDelete(String assId) {
                            for (int j = 0; j < allAssetsBeenScan.size(); j++) {
                                AssetsBean assetsBean = allAssetsBeenScan.get(j);
                                if (assetsBean.getId().equals(assId)){
                                    allAssetsBeenScan.remove(assetsBean);
                                    j--;
                                }
                            }
                        }
                    });


                }else{
                    ll_two.addView(getFuncView(i));
                    funcLayout.addView(ll_two);
                    layouts.put(funcList.get(i).getFuncId(), ll_two);
                }


//				}else{
//					funcList.remove(i);
//					len = len-1;
//				}
            }

            // if (len % 2 != 0) { // 最后一行添加一个view
            // ll_two = new LinearLayout(this);
            // ll_two.setLayoutParams(new LinearLayout.LayoutParams(
            // LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            // ll_two.addView(getFuncView(len - 1));
            //// ll_two.addView(getFuncView(-1));
            // funcLayout.addView(ll_two);
            // }
        }
//        //新功能控件的处理
//        listFuncViewT1 = new ListFuncViewT1(this);
//        View listFuncView = listFuncViewT1.getView();
//        listFuncView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//        funcLayout.addView(listFuncView, funcLayout.getChildCount());
//        //添加功能点击事件
//        listFuncViewT1.setOnListFuncListener(new ListFuncViewT1.OnListFuncListener() {
//            @Override
//            public void onAdd() {
//                //点击了添加控件,调出新界面进行数据获取,返回后添加
//                Intent intent = new Intent(AbsFuncActivity.this, SearchChooseActivity.class);
//                //查询出已选择数据.传递给选择界面使其呈选中状态
//                List<AssetsBean> seLectata = listFuncViewT1.getData();
//                intent.putExtra("select_data", (Serializable) seLectata);
//                AbsFuncActivity.this.startActivityForResult(intent, 211);
//            }
//
//            @Override
//            public void onScan() {
//                //点击了扫描控件,调出扫描框,扫描结果返回进行处理
//                Intent intent = new Intent(AbsFuncActivity.this, CaptureActivity.class);
//                startActivityForResult(intent, 212);
//
//            }
//        });


        LinearLayout nullView = new LinearLayout(this);
        nullView.setBackgroundColor(Color.TRANSPARENT);
        if (len == 6) {
            nullView.setLayoutParams(
                    new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, PublicUtils.convertDIP2PX(this, 250)));
        } else if (len == 7) {
            nullView.setLayoutParams(
                    new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, PublicUtils.convertDIP2PX(this, 300)));
        } else if (len == 8) {
            nullView.setLayoutParams(
                    new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, PublicUtils.convertDIP2PX(this, 350)));
        } else {
            nullView.setLayoutParams(
                    new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, PublicUtils.convertDIP2PX(this, 100)));
        }

        funcLayout.addView(nullView);
        controlOrgMap();
        initContrlView();


    }

    private void forCheck() {
        if (null != funcList) {
            Iterator<Func> it = funcList.iterator();
            while (it.hasNext()) {
                Func f = it.next();
                if (checkFuncTimeOptions(f.getRestrictType(), f.getRestrictRule())) {
                    it.remove();
                }
            }
        }
    }


    private boolean checkFuncTimeOptions(int restrict_type, String restrict_rule) {

        ArrayList<Integer> strList = new ArrayList<Integer>();
        boolean flag = true;
        if (null == restrict_rule || restrict_rule.equals("")) {
            return false;
        } else {
            String[] arr = restrict_rule.split(",");
            for (String s : arr) {

                if (s.contains("-")) {
                    String[] arr2 = s.split("-");

                    int num1 = Integer.parseInt(arr2[0]);
                    int num2 = Integer.parseInt(arr2[1]);

                    for (int i = num1; i <= num2; i++) {
                        strList.add(i);
                    }
                } else {
                    strList.add(Integer.parseInt(s));
                }
            }
        }

        Calendar c = Calendar.getInstance();
        int checkflag = 0;
        if (restrict_type == 1) {
            checkflag = c.get(Calendar.DAY_OF_MONTH);
        } else if (restrict_type == 2) {
            checkflag = c.get(Calendar.MONTH) + 1;
        }
        for (int i : strList) {
            if (checkflag == i) {
                flag = false;
            }
        }
        return flag;
    }


    List<String> contrlEnterList = new ArrayList<String>();// 控制开关的集合
    Map<String, List<String>> contrlmap = new HashMap<String, List<String>>();// 所有被控制控件的集合
    SubmitItemDB submitItemDB = new SubmitItemDB(this);
    SubmitItemTempDB submitItemTempDB = new SubmitItemTempDB(this);

    public void initContrlView() {
        for (int i = 0; i < funcList.size(); i++) {

            String enterMustList = funcList.get(i).getEnterMustList();
            if (enterMustList != null && !enterMustList.equals("")) {
                String[] arr = enterMustList.split(",");
                if (arr[0] != null && !arr[0].equals("")) {
                    int funcId = funcList.get(i).getFuncId();
                    View view = unOperateMap.get(funcId);
                    SubmitItem subItem1 = submitItemDB.findSubmitItemByFuncId(funcId);
                    changeGrag1(view);
                    //如果提交表里
                    if (subItem1 != null) {
                        changeBlue1(view);
                    }
                    if (!contrlEnterList.contains(arr[0])) {
                        contrlEnterList.add(arr[0]);
                    }
                }
            }
        }

        for (String string : contrlEnterList) {
            List<String> contrlList = new ArrayList<String>();// 单个开关被控制控件的集合
            for (int i = 0; i < funcList.size(); i++) {
                String enterMustList = funcList.get(i).getEnterMustList();
                if (enterMustList != null && !enterMustList.equals("")) {
                    String[] arr = enterMustList.split(",");
                    if (arr[0] != null && !arr[0].equals("")) {
                        if (arr[0].equals(string)) {
                            int funcId = funcList.get(i).getFuncId();
                            contrlList.add(funcId + "," + enterMustList);
                            contrlmap.put(arr[0], contrlList);
                        }
                    }
                }
            }
        }
        Log.e("contrlEnterList", contrlEnterList.toString());
        Log.e("contrlmap", contrlmap.toString());

    }

    public void setContrlView(TypeValue typeValue, String funcId) {
        List<String> contrlList = contrlmap.get(funcId);
        for (String contrlInfo : contrlList) {
            String[] arr = contrlInfo.split(",");
            Integer funcId1 = Integer.parseInt(arr[0]);
            boolean flag1 = PublicUtils.compare(typeValue, arr[3], Integer.parseInt(arr[2]));

            View view = unOperateMap.get(funcId1);
            if (flag1) {
                if (view != null) {
                    changeBlue1(view);
                } else {
                    View view2 = operatedMap.get(funcId1);
                    if (view2 != null) {
                        changeBlue1(view2);
                    }
                }
            } else {
                if (view != null) {
                    changeGrag1(view);
                } else {
                    View view1 = operatedMap.get(funcId1);
                    if (view1 != null) {
                        changeGrag1(view1);
                    }
                }


                SubmitItem s = submitItemDB.findSubmitItemByFuncId(funcId1);
                if (s != null) {
                    List<Submit> listS = new SubmitDB(this).findSubmitByParentId(s.getSubmitId());
                    for (Submit submit : listS) {
                        submitItemDB.deleteSubmitItemBySubmitId(submit.getId());
                    }
                }
                submitItemDB.deleteSubmitItemByFuncId(funcId1);
            }
        }
    }

    /*
     * 初始化Button类型的控件项视图
	 */
    public void initButtnTypeLayout() {
        LinearLayout ll_btnLayout = (LinearLayout) findViewById(R.id.ll_btn_layout);
        // ll_btnLayout.setOnClickListener(this);

        View view = new FuncBtnLayoutMenu(this, this).getView(buttonFuncList);
        if (view != null && isLinkActivity) {
            TextView tv_btn_first = (TextView) view.findViewById(R.id.tv_btn_first);
            tv_btn_first.setText(getResources().getString(R.string.preview));
        } else if (view != null && menuType == Menu.TYPE_ORDER3) {
            TextView tv_btn_first = (TextView) view.findViewById(R.id.tv_btn_five);
            tv_btn_first.setText(getResources().getString(R.string.preview));
        }
        ll_btnLayout.addView(view);

    }

    public boolean isShowDialog = false;

    @Override
    protected void onResume() {
        super.onResume();
        setShowHook();
        isOnPause = false;
        if (locationForFunc != null) {
            locationForFunc.onAcivityResume();
        }
    }

    /**
     * 查询数据库，如果数据库中有该控件的值说明已经操作过
     * 将该view放到operatedMap里并且设置该view对勾显示否则放到unOperateMap
     */
    private void initAllFunc() {
        int submitId = new SubmitDB(this).selectSubmitIdNotCheckOut(planId, wayId, storeId, targetId, menuType,
                Submit.STATE_NO_SUBMIT);
        if (funcList != null) {
            for (Func func : funcList) {// 遍历该页面的所有控件

                View view = null;
                if (unOperateMap.containsKey(func.getFuncId())) {// 如果没操作的map里有这个控件
                    view = this.unOperateMap.get(func.getFuncId());
                } else {
                    view = this.operatedMap.get(func.getFuncId());
                }
                if (view!=null){
                    View hookView = view.findViewById(R.id.iv_func_check_mark);// 初始化对勾view
                    TView = (TextView) view.findViewById(R.id.tv_func_content);// 初始化对勾view
                    TView.setText("");
                    String str = SharedPreferencesUtil.getInstance(this).getContracInfo();
                    if(txrId== func.getFuncId()&&!TextUtils.isEmpty(str)){
                        TView.setText("已完成");
                        TView.setVisibility(View.VISIBLE);
                    }
                    if (!isAssetEdit&&func.getIsEdit()==0){
                        view.setClickable(false);
                        view.setEnabled(false);
                        if (func.getType()==Func.TYPE_EDITCOMP){
                          try{
                              RelativeLayout iv_func_photo_container = (RelativeLayout) view
                                      .findViewById(R.id.iv_func_photo_container);
                              EditText textEditTextComp = (EditText) iv_func_photo_container.findViewById(R.id.textEditTextComp);

                              textEditTextComp.setEnabled(false);

                          }catch (Exception e){

                          }
                        }


                    }else{
                        view.setClickable(true);
                        view.setEnabled(true);

                        if (func.getType()==Func.TYPE_EDITCOMP){
                            try{
                                RelativeLayout iv_func_photo_container = (RelativeLayout) view
                                        .findViewById(R.id.iv_func_photo_container);
                                EditText textEditTextComp = (EditText) iv_func_photo_container.findViewById(R.id.textEditTextComp);

                                textEditTextComp.setEnabled(true);

                            }catch (Exception e){

                            }
                        }

                    }

                }else{
                    TView = new TextView(this);
                }

                if (func.getType() == Func.TYPE_CAMERA || func.getType() == Func.TYPE_CAMERA_MIDDLE
                        || func.getType() == Func.TYPE_CAMERA_HEIGHT || func.getType() == Func.TYPE_CAMERA_CUSTOM) {
                    TView.setText("");
                    ImageView iv = (ImageView) view.findViewById(R.id.iv_func_photo);
                    iv.setVisibility(View.GONE);
                }
                setShowHook(false, hookView);// 默认设置不显示

                if (func.getFuncId() == Func.ISSUE_USER) {// 如果是下发的用户
                    if (submitId != 0) {
                        inputOperatedMap(func);// 说明有下发的用户
                    } else {
                        inputUnOperatedMap(func);// 没有下发的用户
                    }
                } else {
                    SubmitItem item = null;
                    if (submitId != 0) {// 数据库中有submit的情况
                        if (linkId != 0 && func.getType() != Func.TYPE_LINK) {// 超链接的时间戳值都是存在SubmitItemDB中，否则有多层的时候就无法查询到数据
                            item = new SubmitItemTempDB(this)
                                    .findSubmitItemByParamNameAndSubmitId(String.valueOf(func.getFuncId()), submitId);
                        } else {
                            item = new SubmitItemDB(this)
                                    .findSubmitItemByParamNameAndSubmitId(String.valueOf(func.getFuncId()), submitId);
                        }
                    }
                    if (item != null && !TextUtils.isEmpty(item.getParamValue())) {// 如果数据库中有值并且值不是空的情况下认为该view已经操作
                        if (func.getOrgOption() != null && func.getOrgOption() == Func.ORG_OPTION) {// 维护机构级联关系（串码平铺属性使用）
                            orgMap.put(func.getOrgLevel(), item.getParamValue());
                        }
                        inputOperatedMap(func);
                        initInputChooseMap(func, item.getParamValue());// 修改的时候外部级联Map条件
                        // 维护
                    } else {
                        inputUnOperatedMap(func);
                        if(func.getType() == Func.TYPE_EDITCOMP&&func.getFuncId()!=txrId){
                            EditText et = (EditText) view.findViewById(R.id.textEditTextComp);
                            et.setText("");
                        }
                    }
                }

            }
        }
    }

    /**
     * 返回的时候重新查询数据库看是否显示对勾 如果控件是有顺序的输入
     */
    public void setShowHook() {
        initAllFunc();// 初始化所有控件
        if (!operatedMap.isEmpty()) {// 遍历已经操作的控件 设置对勾显示
            // 填完数据再次进去进行查表设置值
            SubmitItemDB submitDb = new SubmitItemDB(this);
            SubmitItemTempDB submitTempDb = new SubmitItemTempDB(this);
            DictDB dictDb = new DictDB(this);
            for (Map.Entry<Integer, View> m : operatedMap.entrySet()) {
                View view = m.getValue();
                Integer index = (Integer) view.getTag();
                final Func func = funcList.get(index);
                Func linkFuns = funcList.get(index);// 超链接查询填充值专用
                // Log.e("brook", func.toString());

                if (func != null && func.getType() != Func.TYPE_BUTTON) {
                    View hookView = view.findViewById(R.id.iv_func_check_mark);
                    TextView tv_func_content = (TextView) view.findViewById(R.id.tv_func_content);
                    if (func.getType() == Func.TYPE_CAMERA || func.getType() == Func.TYPE_CAMERA_MIDDLE
                            || func.getType() == Func.TYPE_CAMERA_HEIGHT || func.getType() == Func.TYPE_CAMERA_CUSTOM
                            || func.getType() == Func.TYPE_SIGNATURE) {
                        tv_func_content.setText("");
                        tv_func_content.setVisibility(View.GONE);
                        ImageView iv = (ImageView) view.findViewById(R.id.iv_func_photo);
                        SubmitItem submitItem = null;
                        final int submitId = new SubmitDB(this).selectSubmitIdNotCheckOut(planId, wayId, storeId,
                                targetId, menuType, Submit.STATE_NO_SUBMIT);
                        boolean isLink = false;
                        if (linkId != 0 && func.getType() != Func.TYPE_LINK) {
                            isLink = true;
                            submitItem = new SubmitItemTempDB(this).findSubmitItemBySubmitIdAndFuncId(submitId,
                                    func.getFuncId());
                        } else {
                            isLink = false;
                            submitItem = new SubmitItemDB(AbsFuncActivity.this)
                                    .findSubmitItemByFuncId(func.getFuncId());
                        }
                        final String file_name = submitItem.getParamValue();
                        final boolean is_link = isLink;
                        if (submitItem != null) {
                            iv.setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {

                                    if (file_name.startsWith("http")) {
                                        Intent intent = new Intent(AbsFuncActivity.this, ShowImageActivity.class);
                                        intent.putExtra("imageUrl", file_name);
                                        intent.putExtra("imageName", "");
                                        startActivity(intent);
                                    } else {
                                        Intent i = new Intent(AbsFuncActivity.this, PhotoPreActivity.class);
                                        i.putExtra("title", func.getName());
                                        i.putExtra("fileName", file_name);
                                        i.putExtra("funcId", func.getFuncId());
                                        i.putExtra("submitId", submitId);
                                        // i.putExtra("submitItemId", );
                                        i.putExtra("isLink", is_link);
                                        // i.putExtra("isFromLinkPriview",
                                        // isFromLinkPriview);
                                        // startActivityForResult(i, 1);
                                        startActivity(i);
                                    }
                                }
                            });

                            iv.setVisibility(View.VISIBLE);
                            if (submitItem.getParamValue().startsWith("http")) {
                                imageLoader.displayImage(submitItem.getParamValue(),
                                        iv, options, null);
                            } else {
                                File f = new File(Constants.SDCARD_PATH + submitItem.getParamValue());
                                if (f.exists()) {
                                    imageLoader.displayImage("file://" + Constants.SDCARD_PATH + submitItem.getParamValue(),
                                            iv, options, null);
                                }
                            }
                        }

                    } else if (func.getType() == Func.TYPE_VIDEO) {
                        View hookView1 = view.findViewById(R.id.iv_func_check_mark);
                        TextView tv_func_content1 = (TextView) view.findViewById(R.id.tv_func_content);

                        tv_func_content1.setText("");
                        tv_func_content1.setVisibility(View.GONE);
                        SubmitItem submitItem = null;
                        final int submitId = new SubmitDB(this).selectSubmitIdNotCheckOut(planId, wayId, storeId,
                                targetId, menuType, Submit.STATE_NO_SUBMIT);
                        boolean isLink = false;
                        if (linkId != 0 && func.getType() != Func.TYPE_LINK) {
                            isLink = true;
                            submitItem = new SubmitItemTempDB(this).findSubmitItemBySubmitIdAndFuncId(submitId,
                                    func.getFuncId());
                        } else {
                            isLink = false;
                            submitItem = new SubmitItemDB(AbsFuncActivity.this)
                                    .findSubmitItemByFuncId(func.getFuncId());
                        }
                        final String file_name = submitItem.getParamValue();
                        final boolean is_link = isLink;
                        if (submitItem != null) {
                            File audioFile = null;
                            if (!TextUtils.isEmpty(file_name)) {
                                audioFile = new File(Constants.VIDEO_PATH + file_name);
                            }
                            final File aFile = audioFile;
                            ImageView iv = (ImageView) view.findViewById(R.id.iv_func_photo);

                            iv.setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // Toast.makeText(AbsFuncActivity.this,
                                    // "dhsl", 0).show();
                                    if (aFile.exists()) {
                                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.putExtra("oneshot", 0);
                                        intent.putExtra("configchange", 0);
                                        Uri uri = Uri.fromFile(aFile);
                                        intent.setDataAndType(uri, "video/*");
                                        startActivity(intent);
                                    }
                                }
                            });

                            iv.setVisibility(View.VISIBLE);
                            // iv.setImageBitmap(getVideoThumbnail(Constants.VIDEO_PATH+file_name));
                            iv.setImageDrawable(getResources().getDrawable(R.drawable.func_vedio));
                        }

                    } else if (func.getType() == Func.TYPE_ATTACHMENT) {
                        TextView tv_func_content1 = (TextView) view.findViewById(R.id.tv_func_content);

                        tv_func_content1.setText("");
                        tv_func_content1.setVisibility(View.GONE);
                        SubmitItem submitItem = null;
                        final int submitId = new SubmitDB(this).selectSubmitIdNotCheckOut(planId, wayId, storeId,
                                targetId, menuType, Submit.STATE_NO_SUBMIT);
                        boolean isLink = false;
                        if (linkId != 0 && func.getType() != Func.TYPE_LINK) {
                            isLink = true;
                            submitItem = new SubmitItemTempDB(this).findSubmitItemBySubmitIdAndFuncId(submitId,
                                    func.getFuncId());
                        } else {
                            isLink = false;
                            submitItem = new SubmitItemDB(AbsFuncActivity.this)
                                    .findSubmitItemByFuncId(func.getFuncId());
                        }
                        final String file_name = submitItem.getParamValue();
                        final boolean is_link = isLink;
                        if (submitItem != null) {
                            File audioFile = null;
                            if (!TextUtils.isEmpty(file_name)) {
                                audioFile = new File(Constants.FUNC_ATTACHMENT_PATH + file_name);
                            }
                            final File aFile = audioFile;
                            ImageView iv = (ImageView) view.findViewById(R.id.iv_func_photo);

                            iv.setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // Toast.makeText(AbsFuncActivity.this,
                                    // "dhsl", 0).show();
                                    if (aFile == null || !aFile.exists()) {
                                        Toast.makeText(AbsFuncActivity.this, getResources().getString(R.string.no_file), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Intent intent = new Intent();
                                        Bundle bundle = new Bundle();
                                        // bundle.putString(OPEN_MODE,
                                        // READ_ONLY);
                                        // //打开模式
                                        // bundle.putBoolean(SEND_CLOSE_BROAD,
                                        // true);
                                        // //关闭时是否发送广播
                                        // bundle.putString(THIRD_PACKAGE,
                                        // selfPackageName);
                                        // //第三方应用的包名，用于对改应用合法性的验证
                                        // 清除打开记录
                                        bundle.putBoolean("ClearTrace", true);
                                        // 关闭后删除打开文件
                                        intent.putExtras(bundle);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.setAction(android.content.Intent.ACTION_VIEW);
                                        intent.setClassName("cn.wps.moffice_eng",
                                                "cn.wps.moffice.documentmanager.PreStartActivity2");
                                        Uri uri = Uri.fromFile(aFile);
                                        intent.setData(uri);
                                        try {
                                            startActivity(intent);
                                        } catch (ActivityNotFoundException e) {
                                            e.printStackTrace();
                                            Toast.makeText(AbsFuncActivity.this, getResources().getString(R.string.install_wpsoffice), Toast.LENGTH_SHORT)
                                                    .show();
                                        }
                                    }
                                }
                            });

                            iv.setVisibility(View.VISIBLE);
                            iv.setImageDrawable(getResources().getDrawable(R.drawable.func_fujian));
                        }
                    } else if (func.getType() == Func.TYPE_RECORD) {
                        TextView tv_func_content1 = (TextView) view.findViewById(R.id.tv_func_content);
                        tv_func_content1.setText("");
                        tv_func_content1.setVisibility(View.GONE);
                        SubmitItem submitItem = null;
                        int submitId = new SubmitDB(this).selectSubmitIdNotCheckOut(planId, wayId, storeId, targetId,
                                menuType, Submit.STATE_NO_SUBMIT);
                        boolean isLink = false;
                        if (linkId != 0 && func.getType() != Func.TYPE_LINK) {
                            isLink = true;
                            submitItem = new SubmitItemTempDB(this).findSubmitItemBySubmitIdAndFuncId(submitId,
                                    func.getFuncId());
                        } else {
                            isLink = false;
                            submitItem = new SubmitItemDB(AbsFuncActivity.this)
                                    .findSubmitItemByFuncId(func.getFuncId());
                        }
                        String file_name = submitItem.getParamValue();
                        if (submitItem != null) {
                            File audioFile = null;
                            if (!TextUtils.isEmpty(file_name)) {
                                audioFile = new File(Constants.RECORD_PATH + file_name);
                            }
                            File aFile = audioFile;
                            ImageView iv = (ImageView) view.findViewById(R.id.iv_func_photo);
                            RelativeLayout recordLayout = (RelativeLayout) view
                                    .findViewById(R.id.iv_func_photo_container);
                            recordLayout.removeAllViews();
                            MediaManager comp = new MediaManager(AbsFuncActivity.this, aFile.getName());
                            View recordButton = comp.getView();
                            // if (recordLayout.getChildCount()==0) {
                            recordLayout.addView(recordButton);
                            // }
                            recordLayout.setVisibility(View.VISIBLE);
                            // iv.setImageDrawable(getResources().getDrawable(R.drawable.func_pause));
                        }
                    } else if (func.getType() == Func.TYPE_EDITCOMP) {
                        try {
                            TextView tv_func_content1 = (TextView) view.findViewById(R.id.tv_func_content);
                            tv_func_content1.setText("");
                            tv_func_content1.setVisibility(View.GONE);
                            int submitId = new SubmitDB(this).selectSubmitIdNotCheckOut(planId, wayId, storeId, targetId,
                                    menuType, Submit.STATE_NO_SUBMIT);
                            SubmitItem submitItem = null;
                            if (linkId != 0 && func.getType() != Func.TYPE_LINK) {
                                submitItem = new SubmitItemTempDB(this).findSubmitItemBySubmitIdAndFuncId(submitId,
                                        func.getFuncId());
                            } else {
                                submitItem = new SubmitItemDB(AbsFuncActivity.this)
                                        .findSubmitItemByFuncId(func.getFuncId());
                            }
                            String file_name = submitItem.getParamValue();
                            if (submitItem != null) {
                                RelativeLayout iv_func_photo_container = (RelativeLayout) view
                                        .findViewById(R.id.iv_func_photo_container);
                                EditText textEditTextComp = (EditText) iv_func_photo_container
                                        .findViewById(R.id.textEditTextComp);
                                textEditTextComp.setText(file_name);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (func.getType() == Func.TYPE_SQL_BIG_DATA) {
                        TextView tv_func_content1 = (TextView) view.findViewById(R.id.tv_func_content);
                        tv_func_content1.setText("");
                        int submitId = new SubmitDB(this).selectSubmitIdNotCheckOut(planId, wayId, storeId, targetId,
                                menuType, Submit.STATE_NO_SUBMIT);
                        SubmitItem submitItem = null;
                        if (linkId != 0 && func.getType() != Func.TYPE_LINK) {
                            submitItem = new SubmitItemTempDB(this).findSubmitItemBySubmitIdAndFuncId(submitId,
                                    func.getFuncId());
                        } else {
                            submitItem = new SubmitItemDB(AbsFuncActivity.this)
                                    .findSubmitItemByFuncId(func.getFuncId());
                        }
                        String file_name = submitItem.getNote();
                        if (submitItem != null) {
                            tv_func_content1.setText(file_name);
                        }
                    } else {
                        ImageView iv = (ImageView) view.findViewById(R.id.iv_func_photo);
                        iv.setVisibility(View.GONE);
                        tv_func_content.setVisibility(View.VISIBLE);
                        String valueStr = tv_func_content.getText().toString().trim();
                        if (valueStr == null || valueStr.equals("")) {
                            try {
                                StringBuffer stBuffer = new StringBuffer();
                                if (linkFuns.getType() == TYPE_MULTI_CHOICE_SPINNER_COMP) {// 多选
                                    SubmitItem subItem = submitDb.findSubmitItemByParamNameAndType(
                                            linkFuns.getFuncId() + "", TYPE_MULTI_CHOICE_SPINNER_COMP);
                                    if (subItem == null) {
                                        subItem = submitTempDb.findSubmitItemByParamNameAndType(
                                                linkFuns.getFuncId() + "", TYPE_MULTI_CHOICE_SPINNER_COMP);
                                    }
                                    String resultStr = subItem.getParamValue();
                                    if (!TextUtils.isEmpty(resultStr)) {
                                        String[] strs = resultStr.split(",");
                                        for (int i = 0; i < strs.length; i++) {
                                            String selectStr = null;
                                            if (linkFuns.getOrgOption() != null) {// 说明是其他类型的单选
                                                if (linkFuns.getOrgOption() == 3) {// 机构店面
                                                    OrgStoreDB orgStoreDB = new OrgStoreDB(this);
                                                    OrgStore orgStore = orgStoreDB.findOrgStoreByStoreId(strs[i]);
                                                    selectStr = orgStore.getStoreName();
                                                } else if (linkFuns.getOrgOption() == 2) {// 用户
                                                    OrgUserDB orgUserDB = new OrgUserDB(this);
                                                    OrgUser orgUser = orgUserDB
                                                            .findUserByUserId(Integer.parseInt(strs[i]));
                                                    selectStr = orgUser.getUserName();
                                                } else if (linkFuns.getOrgOption() == 1) {// 机构
                                                    OrgDB orgDb = new OrgDB(this);
                                                    Org org = orgDb.findOrgByOrgId(Integer.parseInt(strs[i]));
                                                    selectStr = org.getOrgName();
                                                }
                                            } else {
                                                selectStr = dictDb.findDictByDid(linkFuns.getDictTable(),
                                                        linkFuns.getDictDataId(), strs[i]).getCtrlCol();
                                            }

                                            if (i < strs.length - 1) {
                                                stBuffer.append(selectStr).append(",");
                                            } else {
                                                stBuffer.append(selectStr);
                                            }
                                        }
                                        setValue(stBuffer.toString(), tv_func_content);

                                    }

                                } else if (linkFuns.getType() == Func.TYPE_SELECT_OTHER) {
                                    SubmitItem subItem = submitDb.findSubmitItemByParamNameAndType(
                                            linkFuns.getFuncId() + "", Func.TYPE_SELECT_OTHER);
                                    if (subItem == null) {
                                        subItem = submitTempDb.findSubmitItemByParamNameAndType(
                                                linkFuns.getFuncId() + "", Func.TYPE_SELECT_OTHER);
                                    }
                                    String resultStr = subItem.getParamValue();
                                    if (resultStr.equals("-1")) {
                                        subItem = submitDb.findSubmitItemByParamNameAndType(
                                                "'" + linkFuns.getFuncId() + "_other'", Func.TYPE_SELECT_OTHER);
                                        if (subItem == null) {
                                            subItem = submitTempDb.findSubmitItemByParamNameAndType(
                                                    "'" + linkFuns.getFuncId() + "_other'", Func.TYPE_SELECT_OTHER);

                                        }
                                        resultStr = subItem.getParamValue();
                                        setValue(resultStr, tv_func_content);
                                    } else {

                                        if (linkFuns.getOrgOption() != null) {
                                            // setValue(resultStr, tv_func_content);
                                            if (linkFuns.getOrgOption() == 3) {// 机构店面
                                                OrgStoreDB orgStoreDB = new OrgStoreDB(this);
                                                OrgStore orgStore = orgStoreDB.findOrgStoreByStoreId(resultStr);
                                                setValue(orgStore.getStoreName(), tv_func_content);
                                            } else if (linkFuns.getOrgOption() == 2) {// 用户
                                                OrgUserDB orgUserDB = new OrgUserDB(this);
                                                OrgUser orgUser = orgUserDB.findUserByUserId(Integer.parseInt(resultStr));
                                                setValue(orgUser.getUserName(), tv_func_content);
                                            } else if (linkFuns.getOrgOption() == 1) {// 机构
                                                OrgDB orgDb = new OrgDB(this);
                                                Org org = orgDb.findOrgByOrgId(Integer.parseInt(resultStr));
                                                setValue(org.getOrgName(), tv_func_content);
                                            }

                                        } else {
                                            String selectSingleStr = dictDb.findDictByDid(linkFuns.getDictTable(),
                                                    linkFuns.getDictDataId(), resultStr).getCtrlCol();
                                            setValue(selectSingleStr, tv_func_content);
                                        }
                                    }
                                } else if (linkFuns.getType() == Func.TYPE_SELECTCOMP) {// 单选类型
                                    SubmitItem subItem = submitDb.findSubmitItemByParamNameAndType(
                                            linkFuns.getFuncId() + "", Func.TYPE_SELECTCOMP);
                                    if (subItem == null) {
                                        subItem = submitTempDb.findSubmitItemByParamNameAndType(
                                                linkFuns.getFuncId() + "", Func.TYPE_SELECTCOMP);
                                    }
                                    String resultStr = subItem.getParamValue();
                                    if (linkFuns.getOrgOption() != null) {// 说明是其他类型的单选
                                        if (linkFuns.getOrgOption() == 3) {// 机构店面
                                            OrgStoreDB orgStoreDB = new OrgStoreDB(this);
                                            OrgStore orgStore = orgStoreDB.findOrgStoreByStoreId(resultStr);
                                            setValue(orgStore.getStoreName(), tv_func_content);
                                        } else if (linkFuns.getOrgOption() == 2) {// 用户
                                            OrgUserDB orgUserDB = new OrgUserDB(this);
                                            OrgUser orgUser = orgUserDB.findUserByUserId(Integer.parseInt(resultStr));
                                            setValue(orgUser.getUserName(), tv_func_content);
                                        } else if (linkFuns.getOrgOption() == 1) {// 机构
                                            OrgDB orgDb = new OrgDB(this);
                                            Org org = orgDb.findOrgByOrgId(Integer.parseInt(resultStr));
                                            setValue(org.getOrgName(), tv_func_content);
                                        }
                                    } else {
                                        String selectSingleStr = dictDb.findDictByDid(linkFuns.getDictTable(),
                                                linkFuns.getDictDataId(), resultStr).getCtrlCol();
                                        setValue(selectSingleStr, tv_func_content);
                                    }

                                } else if (linkFuns.getType() == Func.TYPE_SINGLE_CHOICE_FUZZY_QUERY_COMP) {// 单选类型
                                    SubmitItem subItem = submitDb.findSubmitItemByParamNameAndType(
                                            linkFuns.getFuncId() + "", Func.TYPE_SINGLE_CHOICE_FUZZY_QUERY_COMP);
                                    if (subItem == null) {
                                        subItem = submitTempDb.findSubmitItemByParamNameAndType(
                                                linkFuns.getFuncId() + "", Func.TYPE_SINGLE_CHOICE_FUZZY_QUERY_COMP);
                                    }
                                    String resultStr = subItem.getParamValue();
                                    if (linkFuns.getOrgOption() != null) {// 说明是其他类型的单选
                                        if (linkFuns.getOrgOption() == 3) {// 机构店面
                                            OrgStoreDB orgStoreDB = new OrgStoreDB(this);
                                            OrgStore orgStore = orgStoreDB.findOrgStoreByStoreId(resultStr);
                                            setValue(orgStore.getStoreName(), tv_func_content);
                                        } else if (linkFuns.getOrgOption() == 2) {// 用户
                                            OrgUserDB orgUserDB = new OrgUserDB(this);
                                            OrgUser orgUser = orgUserDB.findUserByUserId(Integer.parseInt(resultStr));
                                            setValue(orgUser.getUserName(), tv_func_content);
                                        } else if (linkFuns.getOrgOption() == 1) {// 机构
                                            OrgDB orgDb = new OrgDB(this);
                                            Org org = orgDb.findOrgByOrgId(Integer.parseInt(resultStr));
                                            setValue(org.getOrgName(), tv_func_content);
                                        }
                                    } else {
                                        String selectSingleStr = dictDb.findDictByDid(linkFuns.getDictTable(),
                                                linkFuns.getDictDataId(), resultStr).getCtrlCol();
                                        setValue(selectSingleStr, tv_func_content);
                                    }

                                } else if (linkFuns.getType() == Func.TYPE_LOCATION) {// 定位类型
                                    SubmitItem subItem = submitDb.findSubmitItemByParamNameAndType(
                                            linkFuns.getFuncId() + "", Func.TYPE_LOCATION);
                                    if (subItem == null) {
                                        subItem = submitTempDb.findSubmitItemByParamNameAndType(
                                                linkFuns.getFuncId() + "", Func.TYPE_LOCATION);
                                    }
                                    String locationStr = subItem.getParamValue();
                                    String subStr = locationStr.substring(0, locationStr.indexOf("$"));

                                    setValue(subStr, tv_func_content);

                                } else if (linkFuns.getType() == Func.TYPE_DATEPICKERCOMP) {// 日期类型
                                    SubmitItem subItem = submitDb.findSubmitItemByParamNameAndType(
                                            linkFuns.getFuncId() + "", Func.TYPE_DATEPICKERCOMP);
                                    if (subItem == null) {
                                        subItem = submitTempDb.findSubmitItemByParamNameAndType(
                                                linkFuns.getFuncId() + "", Func.TYPE_DATEPICKERCOMP);
                                    }
                                    String dateStr = subItem.getParamValue();
                                    setValue(dateStr, tv_func_content);

                                } else if (linkFuns.getType() == Func.TYPE_TIMEPICKERCOMP) {// 时间类型
                                    SubmitItem subItem = submitDb.findSubmitItemByParamNameAndType(
                                            linkFuns.getFuncId() + "", Func.TYPE_TIMEPICKERCOMP);
                                    if (subItem == null) {
                                        subItem = submitTempDb.findSubmitItemByParamNameAndType(
                                                linkFuns.getFuncId() + "", Func.TYPE_TIMEPICKERCOMP);
                                    }
                                    String timeStr = subItem.getParamValue();
                                    setValue(timeStr, tv_func_content);

                                } else if (linkFuns.getType() == Func.TYPE_TEXTCOMP) {// 文本标签类型
                                    SubmitItem subItem = submitDb.findSubmitItemByParamNameAndType(
                                            linkFuns.getFuncId() + "", Func.TYPE_TEXTCOMP);
                                    if (subItem == null) {
                                        subItem = submitTempDb.findSubmitItemByParamNameAndType(
                                                linkFuns.getFuncId() + "", Func.TYPE_TEXTCOMP);
                                    }
                                    String textcompStr = subItem.getParamValue();
                                    if (textcompStr.endsWith(".jpg") || textcompStr.endsWith(".jpeg") || textcompStr.endsWith(".png")) {
                                        tv_func_content.setVisibility(View.GONE);
                                        iv.setVisibility(View.VISIBLE);
                                        imageLoader.displayImage(textcompStr, iv);

                                    } else {

                                        setValue(textcompStr, tv_func_content);
                                    }

                                } else if (linkFuns.getType() == Func.TYPE_PRODUCT_CODE) {// 串码
                                    SubmitItem subItem = submitDb.findSubmitItemByParamNameAndType(
                                            linkFuns.getFuncId() + "", Func.TYPE_PRODUCT_CODE);
                                    if (subItem == null) {
                                        subItem = submitTempDb.findSubmitItemByParamNameAndType(
                                                linkFuns.getFuncId() + "", Func.TYPE_PRODUCT_CODE);
                                    }
                                    String textcompStr = subItem.getParamValue();
                                    setValue(textcompStr, tv_func_content);
                                } else if (linkFuns.getType() == Func.TYPE_SELECTCOMP) {// 机构
                                    SubmitItem subItem = submitDb.findSubmitItemByParamNameAndType(
                                            linkFuns.getFuncId() + "", Func.TYPE_SELECTCOMP);
                                    if (subItem == null) {
                                        subItem = submitTempDb.findSubmitItemByParamNameAndType(
                                                linkFuns.getFuncId() + "", Func.TYPE_SELECTCOMP);
                                    }
                                    String textcompStr = subItem.getParamValue();

                                    setValue(textcompStr, tv_func_content);
                                } else if (linkFuns.getType() == Func.TYPE_SCAN) {// 扫描
                                    SubmitItem subItem = submitDb.findSubmitItemByParamNameAndType(
                                            linkFuns.getFuncId() + "", Func.TYPE_SCAN);
                                    if (subItem == null) {
                                        subItem = submitTempDb.findSubmitItemByParamNameAndType(
                                                linkFuns.getFuncId() + "", Func.TYPE_SCAN);
                                    }
                                    String textcompStr = subItem.getParamValue();
                                    setValue(textcompStr, tv_func_content);
                                }

                                else {
                                    setValue(getResources().getString(R.string.have_finished), tv_func_content);
                                }

                            } catch (Exception e) {
                                setValue(valueStr, tv_func_content);
                            }
                        }

                        setShowHook(false, hookView);

                    }

                    // Toast.makeText(AbsFuncActivity.this, ""+func.getvalue(),
                    // Toast.LENGTH_SHORT).show();
                    if (func.getInputOrder() != null) {// 修改的时候 异常退出的时候
                        // 顺序输入如果有值就变为蓝色
                        changeBlue(view, func);
                        view.setEnabled(true);
                    }
                }
            }
        }
    }

    /**
     * 设置对勾图片是否显示
     *
     * @param isShowHook true对勾显示 false 对勾不显示
     * @param hookView   要操作的对勾view
     */
    public void setShowHook(boolean isShowHook, View hookView) {
        if (hookView != null) {
            if (isShowHook) {
                hookView.setVisibility(View.VISIBLE);
            } else {
                hookView.setVisibility(View.GONE);
            }
        }
    }

    public void setValue(String value, TextView TView) {
        if (TView != null) {

            TView.setText(value);

        }
    }

    private RemindDialog redialog;

    @Override
    public void onClick(View v) {
        Log.e(TAG, "----------------");
        super.onClick(v);
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        if (!isCanClick) {
            return;
        } else if (menuType == com.yunhu.yhshxc.bo.Menu.TYPE_VISIT && !isCheckIn) {// 如果没有点击过checkin提示用户
            ToastOrder.makeText(this, getResources().getString(R.string.checkin), ToastOrder.LENGTH_SHORT).show();
        } else {
            isCanClick = false;
            if (v.getTag() != null) {
                this.clickFuncIndex = (Integer) v.getTag(); // 取出之前存在View中的funcIndex
            }
            switch (v.getId()) {
                case -100:// 预览显示数据
                    //如果存在新添控件ListFcunViewT1,先写入提交表
                    insertNewData();
                    insertTXR();
                    showPreview();
                    isCanClick = true;
                    break;
                case Func.TYPE_CAMERA:// 拍照
                case Func.TYPE_CAMERA_HEIGHT:
                case Func.TYPE_CAMERA_MIDDLE:
                case Func.TYPE_CAMERA_CUSTOM:
                    currentFunc = funcList.get(clickFuncIndex);
                    hookView = v.findViewById(R.id.iv_func_check_mark);
                    TView = (TextView) v.findViewById(R.id.tv_func_content);// 初始化对勾view

                    try {
                        if (currentFunc.getPhotoFlg() == 2) {
                            new PhotoPopupWindow(this).show(null);
                        } else {
                            takePhoto(1);
                        }
                    } catch (Exception e) {
                        JLog.e(e);
                    }
                    break;
                case Func.TYPE_SCAN_INPUT:
                    currentFunc = funcList.get(clickFuncIndex);
                    if (this instanceof QueryFuncActivity) {// 扫码作为查询条件的时候转为输入框
                        currentFunc.setType(Func.TYPE_EDITCOMP);
                        openCompDialog(currentFunc, v, 0);
                    } else {
                        hookView = v.findViewById(R.id.iv_func_check_mark);
                        TView = (TextView) v.findViewById(R.id.tv_func_content);// 初始化对勾view
                        openCompDialog(currentFunc, v, 0);
                    }
                    break;
                case Func.TYPE_SCAN:
                    currentFunc = funcList.get(clickFuncIndex);
                    if (this instanceof QueryFuncActivity) {// 扫码作为查询条件的时候转为输入框
                        currentFunc.setType(Func.TYPE_EDITCOMP);
                        openCompDialog(currentFunc, v, 0);
                    } else {
                        hookView = v.findViewById(R.id.iv_func_check_mark);
                        TView = (TextView) v.findViewById(R.id.tv_func_content);// 初始化对勾view
                        clickScan();
                    }
                    break;
                case Func.TYPE_LOCATION:// 定位
                    try {
                        currentFunc = funcList.get(clickFuncIndex);
                        hookView = v.findViewById(R.id.iv_func_check_mark);
                        TView = (TextView) v.findViewById(R.id.tv_func_content);// 初始化对勾view

                        if (is_store_expand == 1 && currentFunc.getFuncId() == StoreExpandUtil.STORE_EXPAND_LOCATION_ID) {// 如果是店面拓展，就执行自己的定位规则
                            getlist.initLocation(false);
                        } else if (this instanceof StoreExpandActivity
                                && currentFunc.getFuncId() == StoreModuleExpandUtil.TYPE_STOREMODULE_LOCATION) {// 新店上报中的定位
                            storeModuleExpandUtil.initLocation(false);
                        } else {
                            // location(false);
                            String level = TextUtils.isEmpty(currentFunc.getLocLevel()) ? "" : currentFunc.getLocLevel();
                            if (level.equalsIgnoreCase("1")) {// GPS优先
                                if (gPSIsOPen(AbsFuncActivity.this)) {
                                    location(false);
                                } else {
                                    redialog = new RemindDialog(AbsFuncActivity.this, getResources().getString(R.string.absfuncactivity_message03));
                                    redialog.show();
                                    isCanClick = true;
                                }
                            } else if (level.equalsIgnoreCase("2")) {// WIFI优先
                                if (isWifiActive(AbsFuncActivity.this)) {
                                    location(false);
                                } else {
                                    redialog = new RemindDialog(AbsFuncActivity.this, getResources().getString(R.string.absfuncactivity_message04));
                                    redialog.show();
                                    isCanClick = true;
                                }
                            } else {// 混合定位
                                location(false);
                            }
                        }
                    } catch (Exception e) {
                        isCanClick = true;
                        tryCatch();
                        // Toast.makeText(AbsFuncActivity.this,
                        // "定位无法使用",Toast.LENGTH_LONG).show();
                        JLog.e(e);
                    }

                    break;
                case Func.TYPE_TABLECOMP:// 表格
                    currentFunc = funcList.get(clickFuncIndex);
                    hookView = v.findViewById(R.id.iv_func_check_mark);
                    TView = (TextView) v.findViewById(R.id.tv_func_content);// 初始化对勾view
                    boolean isExcuteSql = isExcuteSql(currentFunc);
                    if (currentFunc.getDefaultType() != null && currentFunc.getDefaultType() == Func.DEFAULT_TYPE_SQL
                            && isExcuteSql) {
                        new SqlInComp(this, modType, isLinkActivity, menuId, menuName).getSearchRequirement2(currentFunc);
                    } else {
                        if (currentFunc.getIsSearchModify() != null && currentFunc.getIsSearchModify() != 0) {
                            historySearchPopupWindow = new HistorySearchPopupWindow(this, currentFunc, isLinkActivity);
                            historySearchPopupWindow.setMenuId(menuId);
                            historySearchPopupWindow.setMenuName(menuName);
                            historySearchPopupWindow.showAsDropDown(this.findViewById(R.id.ll_btn_layout));
                            isCanClick = true;
                        } else {
                            Intent mIntetn = new Intent();

                            if (bundle.getBoolean("isTableHistory")) {
                                bundle.putBoolean("isTableHistory", true);
                            } else {

                                bundle.putBoolean("isTableHistory", false);
                                if (currentFunc.getDefaultType() != null
                                        && currentFunc.getDefaultType() == Func.DEFAULT_TYPE_SQL) {// 表格sql获取
                                    bundle.putString("defaultSql", "");
                                } else {
                                    bundle.putString("defaultSql", "");
                                }

                            }
                            Module mModel = (Module) bundle.getSerializable("module");
                            bundle.putSerializable("func", currentFunc);
                            if (mModel != null) {
                                bundle.putInt("modType", mModel.getType());
                            }
                            mIntetn.putExtra("tableBundle", bundle);
                            mIntetn.putExtra("chooseMapBundle", getChooseMapBundle());
                            mIntetn.putExtra("orgMapBundle", getOrgMapBundle());
                            mIntetn.putExtra("isNoWait", bundle.getBoolean("bundle"));
                            // CompDialog comp = new CompDialog(this, currentFunc,
                            // bundle);
                            // comp.getObject();
                            // Toast.makeText(this,
                            // "clickFuncIndex--"+clickFuncIndex,
                            // Toast.LENGTH_SHORT).show();
                            //先去集合查询是否存在该表格,不存在则添加

                            if (tableListMap.containsKey(currentFunc.getFuncId())) {
                                TableCompView tableV = tableListMap.get(currentFunc.getFuncId());
                                //判断值如果是0则进行隐藏,如果是1则进行展示
                                if (tableV.getShowTag() == 0) {
                                    tableV.setVisibility(View.GONE);
                                    tableV.setShowTag(1);
                                } else {
                                    tableV.setVisibility(View.VISIBLE);
                                    tableV.setShowTag(0);
                                }

                            } else {
                                LinearLayout tableContainer = (LinearLayout) v.findViewById(R.id.ll_menu_table_container);
                                final TableCompView tableCompView = new TableCompView(this, mIntetn); // 创建表格布局,设置布局参数
                                tableCompView.setMenuId(menuId);
                                tableCompView.setMenuType(menuType);
                                tableCompView.setMenuName(menuName);
                                LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                                tableCompView.setTitleName(currentFunc.getName());// 设置表格名称
                                tableCompView.setLayoutParams(params);
                                tableCompView.setPositionId(clickFuncIndex);// 给表格对象设置打开它的控件位置
                                tableCompView.setSaveTableClickListener(new TableCompView.SaveTableClickListener() {

                                    @Override
                                    public void clickPosition(int index) {
                                        tableCompView.setVisibility(View.GONE);
                                        tableCompView.setShowTag(1);
                                        setShowHook();//更新界面
                                        View sv = layouts.get(currentFunc.getFuncId());
                                        mFuncScrollView.scrollTo(0, sv.getTop());//滑动到添加表VIEW处
//									Log.e("view",currentFunc.getFuncId()+"---"+sv.getTop());
                                    }
                                });
                                // 表格内控件的按钮监听,用于明确是哪个表格对象的操作
                                tableCompView.setStartPhotoClickListener(new TableCompView.StartPhotoClickListener() {

                                    @Override
                                    public void clickPhotoPosition(int index) {
                                        AbsFuncActivity.this.whichTablePhoto = currentFunc.getFuncId();

                                    }
                                });

                                tableContainer.addView(tableCompView);
                                tableListMap.put(currentFunc.getFuncId(), tableCompView);

                            }


                            isCanClick = true;
                        }
                    }
                    break;
                case Func.TYPE_EDITCOMP:// EditText
                    currentFunc = funcList.get(clickFuncIndex);
                    if(currentFunc.getFuncId()==txrId){
                        Intent intent = new Intent(this, TXRListActivity.class);
                        startActivity(intent);
                    }else{
                        openCompDialog(currentFunc, v, 0);
                    }
                    isCanClick = true;
                    // if (AbsFuncActivity.this instanceof QueryFuncActivity
                    // && (currentFunc.getDataType() == Func.DATATYPE_BIG_INTEGER
                    // || currentFunc.getDataType() == Func.DATATYPE_DECIMAL
                    // || currentFunc.getDataType() == Func.DATATYPE_SMALL_INTEGER))
                    // {// 如果是查询条件就弹出有范围的输入框
                    // openCompDialog(currentFunc, v);
                    // } else {
                    // isCanClick = true;
                    // RelativeLayout iv_func_photo_container = (RelativeLayout) v
                    // .findViewById(R.id.iv_func_photo_container);
                    // EditText textEditTextComp = (EditText)
                    // iv_func_photo_container.findViewById(R.id.textEditTextComp);
                    // textEditTextComp.setFocusable(true);
                    // textEditTextComp.setFocusableInTouchMode(true);
                    // textEditTextComp.requestFocus();
                    // textEditTextComp.findFocus();
                    // InputMethodManager imm = (InputMethodManager)
                    // getSystemService(Context.INPUT_METHOD_SERVICE);
                    // imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    // }
                    break;
                case Func.TYPE_DATEPICKERCOMP:// 日期组件
                    currentFunc = funcList.get(clickFuncIndex);
                    // openTimePicker();
                    // openDatePicker(currentFunc, v);
                    if (this instanceof QueryFuncActivity) {
                        openCompDialog(currentFunc, v, 0);
                    } else {
                        openDatePicker(currentFunc, v);
                    }
                    break;
                case Func.TYPE_TIMEPICKERCOMP:// 日期组件
                    currentFunc = funcList.get(clickFuncIndex);
                    openCompDialog(currentFunc, v, 0);

                    break;
                case Func.TYPE_SELECTCOMP:// 下拉选择
                    currentFunc = funcList.get(clickFuncIndex);
                    if (this instanceof QueryFuncActivity) {// 如果是查询要改变状态
                        currentFunc.setType(changeTypeForIsSearchMulAndIsFuzzy(currentFunc));
                    }
                    openCompDialog(currentFunc, v, 0);
                    break;
                case Func.TYPE_TEXTCOMP:// 标签
                    currentFunc = funcList.get(clickFuncIndex);
                    openCompDialog(currentFunc, v, 0);
                    break;
                case TYPE_MULTI_CHOICE_SPINNER_COMP:// 多选下拉框
                    currentFunc = funcList.get(clickFuncIndex);
                    if (this instanceof QueryFuncActivity && currentFunc.getTargetid() != -2
                            && currentFunc.getFuncId() != Func.REPORT_MULTI) {// 考勤查询的时候func.getTargetid()==-2//func.getFuncId()==-2907表示是报表中的多选//如果是查询要改变状态
                        currentFunc.setType(changeTypeForIsSearchMulAndIsFuzzy(currentFunc));
                    }
                    openCompDialog(currentFunc, v, 0);
                    break;
                case Func.TYPE_CHECKBOXCOMP:// 选择
                    currentFunc = funcList.get(clickFuncIndex);
                    openCompDialog(currentFunc, v, 0);
                    break;
                case Func.TYPE_RADIOBTNCOMP:// 多选
                    currentFunc = funcList.get(clickFuncIndex);
                    openCompDialog(currentFunc, v, 0);
                    break;
                case Func.TYPE_SINGLE_CHOICE_FUZZY_QUERY_COMP:// 搜索
                    currentFunc = funcList.get(clickFuncIndex);
                    if (this instanceof QueryFuncActivity) {// 如果是查询要改变状态
                        currentFunc.setType(changeTypeForIsSearchMulAndIsFuzzy(currentFunc));
                    }
                    openCompDialog(currentFunc, v, 0);
                    break;
                case Func.TYPE_MULTI_CHOICE_FUZZY_QUERY_COMP:// 搜索
                    currentFunc = funcList.get(clickFuncIndex);
                    if (this instanceof QueryFuncActivity) {// 如果是查询要改变状态
                        currentFunc.setType(changeTypeForIsSearchMulAndIsFuzzy(currentFunc));
                    }
                    openCompDialog(currentFunc, v, 0);
                    break;
                case Func.TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY_COMP:// 模糊搜索其他
                    currentFunc = funcList.get(clickFuncIndex);
                    if (this instanceof QueryFuncActivity) {// 如果是查询要改变状态
                        currentFunc.setType(changeTypeForIsSearchMulAndIsFuzzy(currentFunc));
                    }
                    openCompDialog(currentFunc, v, 0);
                    break;
                case Func.TYPE_SELECT_OTHER:// 单选其他
                    currentFunc = funcList.get(clickFuncIndex);
                    if (this instanceof QueryFuncActivity) {// 如果是查询要改变状态
                        currentFunc.setType(changeTypeForIsSearchMulAndIsFuzzy(currentFunc));
                    }
                    openCompDialog(currentFunc, v, 0);
                    break;

                case Func.TYPE_HIDDEN:// 下拉选择
                    currentFunc = funcList.get(clickFuncIndex);
                    isCanClick = false;
                    hookView = v.findViewById(R.id.iv_func_check_mark);
                    TView = (TextView) v.findViewById(R.id.tv_func_content);// 初始化对勾view
                    // 0,1,5,6,7,8,9,10改为输入类型，2,3,4,12改为日期类型
                    Integer defaultType = currentFunc.getDefaultType();
                    if (defaultType != null) {
                        switch (defaultType) {
                            case Func.DEFAULT_TYPE_OTHER:
                            case Func.DEFAULT_TYPE_USER:
                            case Func.DEFAULT_TYPE_YEAR:
                            case Func.DEFAULT_TYPE_MONTH:
                            case Func.DEFAULT_TYPE_DAY:
                            case Func.DEFAULT_TYPE_HOURS:
                            case Func.DEFAULT_TYPE_MINUTE:
                            case Func.DEFAULT_TYPE_JOB_NUMBER:
                                currentFunc.setType(Func.TYPE_EDITCOMP);
                                break;
                            case Func.DEFAULT_TYPE_TIME:
                            case Func.DEFAULT_TYPE_DATE_NO_TIME:
                            case Func.DEFAULT_TYPE_DATE:
                            case Func.DEFAULT_TYPE_STARTDATE:
                                currentFunc.setType(Func.TYPE_DATEPICKERCOMP);
                                break;

                            default:
                                break;
                        }
                    }

                    currentFunc.setDataType(Func.DATATYPE_TEXT);
                    int type1 = 1;
                    openCompDialog(currentFunc, v, type1);
                    break;

                case Func.TYPE_LINK:
                    currentFunc = funcList.get(clickFuncIndex);
                    hookView = v.findViewById(R.id.iv_func_check_mark);
                    TView = (TextView) v.findViewById(R.id.tv_func_content);// 初始化对勾view
                    boolean isExcuteSqlLink = isExcuteSql(currentFunc);
                    if (currentFunc.getDefaultType() != null && currentFunc.getDefaultType() == Func.DEFAULT_TYPE_SQL
                            && isExcuteSqlLink) {
                        new SqlInComp(this, modType, isLinkActivity, menuId, menuName).getSearchRequirement2(currentFunc);
                    } else {
                        if (currentFunc.getIsSearchModify() != null && currentFunc.getIsSearchModify() != 0) {
                            historySearchPopupWindow = new HistorySearchPopupWindow(this, currentFunc, isLinkActivity);
                            historySearchPopupWindow.setMenuId(menuId);
                            historySearchPopupWindow.setMenuName(menuName);
                            historySearchPopupWindow.showAsDropDown(this.findViewById(R.id.ll_btn_layout));
                        } else {
                            linkFunc(currentFunc);
                        }
                        isCanClick = true;
                    }
                    break;
                case Func.TYPE_BUTTON:
                    currentFunc = buttonFuncList.get(clickFuncIndex);
                    insertNewData();
                    insertTXR();
                    clickButton(currentFunc);
                    isCanClick = true;
                    break;
                case Func.TYPE_STORE_VIEW:
                    currentFunc = funcList.get(clickFuncIndex);
                    hookView = v.findViewById(R.id.iv_func_check_mark);
                    TView = (TextView) v.findViewById(R.id.tv_func_content);// 初始化对勾view
                    storeView(currentFunc);
                    isCanClick = true;
                    break;
                case Func.TYPE_STORE_UPDATA:
                    currentFunc = funcList.get(clickFuncIndex);
                    hookView = v.findViewById(R.id.iv_func_check_mark);
                    TView = (TextView) v.findViewById(R.id.tv_func_content);// 初始化对勾view
                    storeUpdata(currentFunc);
                    isCanClick = true;
                    break;
                case Func.TYPE_RECORD:// 录音
                    currentFunc = funcList.get(clickFuncIndex);
                    hookView = v.findViewById(R.id.iv_func_check_mark);
                    TView = (TextView) v.findViewById(R.id.tv_func_content);// 初始化对勾view
                    record(currentFunc);
                    isCanClick = true;
                    break;
                case Func.TYPE_ORDER:// 订单
                    currentFunc = funcList.get(clickFuncIndex);
                    hookView = v.findViewById(R.id.iv_func_check_mark);
                    TView = (TextView) v.findViewById(R.id.tv_func_content);// 初始化对勾view
                    order();
                    isCanClick = true;
                    break;
                case Func.TYPE_PRODUCT_CODE:// 串码
                    currentFunc = funcList.get(clickFuncIndex);
                    codeFunc = funcList.get(clickFuncIndex);
                    hookView = v.findViewById(R.id.iv_func_check_mark);
                    TView = (TextView) v.findViewById(R.id.tv_func_content);// 初始化对勾view
                    openCompDialog(currentFunc, v, 0);
                    isCanClick = true;
                    break;
                case Func.TYPE_SQL_BIG_DATA:// sql大数据查询
                    currentFunc = funcList.get(clickFuncIndex);
                    hookView = v.findViewById(R.id.iv_func_check_mark);
                    TView = (TextView) v.findViewById(R.id.tv_func_content);// 初始化对勾view
                    sqlBigData(currentFunc);
                    isCanClick = true;
                    break;
                case Func.TYPE_ORDER2:// 新订单
                    currentFunc = funcList.get(clickFuncIndex);
                    hookView = v.findViewById(R.id.iv_func_check_mark);
                    TView = (TextView) v.findViewById(R.id.tv_func_content);// 初始化对勾view
                    newOrder(currentFunc);
                    isCanClick = true;
                    break;
                case Func.TYPE_ORDER3:// 订单3
                    currentFunc = funcList.get(clickFuncIndex);
                    hookView = v.findViewById(R.id.iv_func_check_mark);
                    TView = (TextView) v.findViewById(R.id.tv_func_content);// 初始化对勾view
                    threeOrder(currentFunc);
                    isCanClick = true;
                    break;
                case Func.TYPE_ORDER3_SEND:// 订单3送货
                    currentFunc = funcList.get(clickFuncIndex);
                    hookView = v.findViewById(R.id.iv_func_check_mark);
                    TView = (TextView) v.findViewById(R.id.tv_func_content);// 初始化对勾view
                    threeOrderSend(currentFunc);
                    isCanClick = true;
                    break;
                case Func.TYPE_VIDEO:// 视频
                    currentFunc = funcList.get(clickFuncIndex);
                    hookView = v.findViewById(R.id.iv_func_check_mark);
                    TView = (TextView) v.findViewById(R.id.tv_func_content);// 初始化对勾view
                    video(currentFunc);
                    isCanClick = true;
                    break;
                case Func.TYPE_ATTACHMENT:// 附件
                    currentFunc = funcList.get(clickFuncIndex);
                    hookView = v.findViewById(R.id.iv_func_check_mark);
                    TView = (TextView) v.findViewById(R.id.tv_func_content);// 初始化对勾view
                    attachment(currentFunc);
                    isCanClick = true;
                    break;
                case Func.TYPE_SIGNATURE://签字
                    into_SIGNATURE();
                    isCanClick = true;
                    break;
                case R.id.ll_scan_add:
                    addSacn();
                    isCanClick = true;
                    break;
                default:
                    break;
            }
        }
    }

    private void addSacn() {
        Intent intent = new Intent(AbsFuncActivity.this, CaptureActivity.class);
        startActivityForResult(intent, 412);
//        requestScanCode("0");
    }

    //    private Func listNewFunc;
    //把ListFuncViewT1的数据写入提交表中
    private void insertNewData() {
         if (listFuncViewT1s.size()<1){
             return;
         }

        for (int i = 0; i < listFuncViewT1s.size(); i++) {
            ListFuncViewT1 listFuncViewT1 = listFuncViewT1s.get(i);
            Func getmFunc = listFuncViewT1.getmFunc();
            if (listFuncViewT1!=null&&getmFunc!=null){
                List<AssetsBean> data = listFuncViewT1.getData();
                if (data.size()<1){
                    return;
                }
                StringBuilder stringBuilder = new StringBuilder();
                for (int k = 0; k < data.size(); k++) {
                    String id = data.get(k).getId();
                    stringBuilder.append(id+",");
                }
                String resultV = stringBuilder.substring(0,stringBuilder.length()-1);
                save(getmFunc,null,resultV,null);
            }
        }



    }

    /**
     * 插入同行人控件数据
     */
    private void insertTXR(){
        String value =SharedPreferencesUtil.getInstance(this).getContracInfo();
        if(!TextUtils.isEmpty(value)){
            int submitId = new SubmitDB(this).selectSubmitIdNotCheckOut(planId, wayId, storeId, targetId,
                    menuType, Submit.STATE_NO_SUBMIT);
            SubmitItem submitItem =  new SubmitItemDB(AbsFuncActivity.this)
                        .findSubmitItemByFuncId(txrId);
            String str = value.substring(0,value.length()-1);
            if(submitItem==null){
                submitItem = new SubmitItem();
                submitItem.setParamName(String.valueOf(txrId));
                submitItem.setParamValue(str);
                submitItem.setSubmitId(submitId);
                new SubmitItemDB(this).insertSubmitItem(submitItem,false);
            }else {
                submitItem.setParamValue(str);
                new SubmitItemDB(this).updateSubmitItem(submitItem,false);
            }

        }
    }

    /**
     * 打开只有日期的选择对话框
     *
     * @param currentFunc2
     * @param v
     */
    DatePickerDialog pcikDialog = null;

    private void openDatePicker(final Func currentFunc2, final View v) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        int nowYear = calendar.get(Calendar.YEAR);
        int nowMonth = calendar.get(Calendar.MONTH);
        int nowDay = calendar.get(Calendar.DAY_OF_MONTH);
        if (Integer.parseInt(Build.VERSION.SDK) >= 21) {
            pcikDialog = new DatePickerDialog(this, R.style.NewDatePickerDialog, null, nowYear, nowMonth, nowDay);
        } else {
            pcikDialog = new DatePickerDialog(this, R.style.NewDatePickerDialogOld, null, nowYear, nowMonth, nowDay);
        }

        final DatePicker datePicker = pcikDialog.getDatePicker();
        pcikDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.Cancle), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                isCanClick = true;
                if (pcikDialog != null && pcikDialog.isShowing()) {
                    pcikDialog.dismiss();
                }

            }
        });
        pcikDialog.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.Confirm), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                isCanClick = true;
                int month1 = datePicker.getMonth() + 1;
                int day1 = datePicker.getDayOfMonth();
                String month2 = month1 >= 10 ? month1 + "" : "0" + month1;
                String day2 = day1 >= 10 ? day1 + "" : "0" + day1;
                String dateStr = datePicker.getYear() + "-" + month2 + "-" + day2;
                save(currentFunc2, v, dateStr, null);
            }
        });
        pcikDialog.setCanceledOnTouchOutside(false);
        pcikDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                isCanClick = true;

            }
        });
        pcikDialog.show();

    }

    protected void save(Func currentFunc2, View v, String dateStr, FuncCache funcCache) {
        TextView funcText = null;
        if (v!=null){
            funcText = (TextView) v.findViewById(R.id.tv_func_content);
        }
        SubmitItem submitItem = new SubmitItem();
        submitItem.setTargetId(currentFunc2.getTargetid() + "");
        SubmitItemDB submitItemDB = new SubmitItemDB(this);
        SubmitDB subDb = new SubmitDB(this);
        SubmitItemTempDB linkSubmitItemTempDB = new SubmitItemTempDB(this);
        int targetId2 = currentFunc2.getTargetid();
        int targetType2 = bundle.getInt("targetType");
        Module model2 = (Module) bundle.getSerializable("module");
        int submitId = subDb.selectSubmitIdNotCheckOut(planId, wayId, storeId, targetId2, targetType2,
                Submit.STATE_NO_SUBMIT);

        boolean isTableHistory = bundle.getBoolean("isTableHistory");// 是否是表格历史数据
        boolean isReport = bundle.getBoolean("isReport");// 是否是报表 true为是
        // false为不是
        if (submitId != Constants.DEFAULTINT) {
            submitItem.setParamName(String.valueOf(currentFunc2.getFuncId()));
            submitItem.setParamValue(dateStr);
            if (funcCache != null) {
                submitItem.setParamValue(funcCache.getParamId());
                submitItem.setNote(funcCache.getParamValue());
            }
            submitItem.setSubmitId(submitId);
            submitItem.setType(currentFunc2.getType());
            submitItem.setOrderId(currentFunc2.getId());
            submitItem.setIsCacheFun(currentFunc2.getIsCacheFun());
            if (currentFunc2.getOrgOption() != null && currentFunc2.getOrgOption() == Func.ORG_STORE) {
                submitItem.setNote(SubmitItem.NOTE_STORE);
            }
            boolean isHas = false;
            if (isLinkActivity) {// 超链接的时候操作临时表中的数据
                isHas = linkSubmitItemTempDB.findIsHaveParamName(submitItem.getParamName(), submitId);
            } else {
                isHas = submitItemDB.findIsHaveParamName(submitItem.getParamName(), submitId);
            }
            if (isHas) {// true表示已经操作过，此时更新数据库

                if (TextUtils.isEmpty(dateStr)) {// 如果值是空就修改为空
                    // 修改的时候如果值取消就传给服务器一个空值，否则服务器不处理
                    submitItem.setParamValue("");
                }

                if (isLinkActivity) {// 超链接
                    linkSubmitItemTempDB.updateSubmitItem(submitItem);
                } else {
                    if (this instanceof ChangeModuleFuncActivity) {

                        submitItemDB.updateSubmitItem(submitItem, false);
                    } else {
                        submitItemDB.updateSubmitItem(submitItem, true);
                    }
                }
            } else {
                if (!TextUtils.isEmpty(dateStr)) {// 没有该项，并且值不为空就插入
                    if (isLinkActivity) {
                        linkSubmitItemTempDB.insertSubmitItemOrDelete(submitItem);
                    } else {
                        // submitItem.setMenuId(menuId);
                        submitItemDB.insertSubmitItemOrDelete(submitItem);
                    }
                }
            }

        } else {
            if (!TextUtils.isEmpty(dateStr)) {
                Submit submit = new Submit();
                submit.setPlanId(planId);
                submit.setState(Submit.STATE_NO_SUBMIT);
                submit.setStoreId(storeId);
                submit.setStoreName(storeName);
                submit.setWayName(wayName);
                submit.setWayId(wayId);
                submit.setTargetid(targetId2);
                submit.setTargetType(targetType2);
                if (model2 != null) {
                    submit.setModType(model2.getType());
                }
                submit.setMenuId(menuId);
                submit.setMenuType(menuType);
                submit.setMenuName(menuName);
                subDb.insertSubmit(submit);
                int currentsubmitId = subDb.selectSubmitIdNotCheckOut(planId, wayId, storeId, targetId2, targetType2,
                        Submit.STATE_NO_SUBMIT);
                submitItem.setParamName(String.valueOf(currentFunc2.getFuncId()));
                submitItem.setType(currentFunc2.getType());
                submitItem.setParamValue(dateStr);
                if (funcCache != null) {
                    submitItem.setParamValue(funcCache.getParamId());
                    submitItem.setNote(funcCache.getParamValue());
                }
                submitItem.setSubmitId(currentsubmitId);
                submitItem.setOrderId(currentFunc2.getId());
                submitItem.setIsCacheFun(currentFunc2.getIsCacheFun());
                if (currentFunc2.getOrgOption() != null && currentFunc2.getOrgOption() == Func.ORG_STORE) {
                    submitItem.setNote(SubmitItem.NOTE_STORE);
                }
                if (isLinkActivity) {
                    linkSubmitItemTempDB.insertSubmitItemOrDelete(submitItem);
                } else {
                    // submitItem.setMenuId(menuId);
                    submitItemDB.insertSubmitItemOrDelete(submitItem);
                }
            }

        }
        if (!isTableHistory && !isReport) {// 不是表格历史信息查询 不是报表查询
            if (TextUtils.isEmpty(dateStr)) {
                inputUnOperatedMap(currentFunc2);
            } else {
                inputOperatedMap(currentFunc2);

                if (dateStr != null && !dateStr.equals("")&&funcText!=null) {
                    setValue(dateStr, funcText);
                }

            }
        }

    }

    private void tryCatch() {
        final Dialog dialog = new Dialog(this, R.style.transparentDialog);
        View view = null;
        view = View.inflate(this, R.layout.location_checkin_dialog, null);
        TextView location_title = (TextView) view.findViewById(R.id.location_title);
        location_title.setText(getResources().getString(R.string.tip));
        TextView ll_date = (TextView) view.findViewById(R.id.tv_needcheckin_dialog_content);
        ll_date.setText(getResources().getString(R.string.locations_exception_try_again));
        LinearLayout confirmBtn = (LinearLayout) view.findViewById(R.id.ll_needcheckin_dialog_confirm);
        LinearLayout cancelBtn = (LinearLayout) view.findViewById(R.id.ll_needcheckin_dialog_newlocation);
        cancelBtn.setVisibility(View.GONE);
        confirmBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        LinearLayout iv_needcheckin_dialog_cancle = (LinearLayout) view.findViewById(R.id.iv_needcheckin_dialog_cancle);
        iv_needcheckin_dialog_cancle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.show();
    }

    /**
     * 判断是否开启GPS
     *
     * @param context
     * @return
     */
    public static final boolean gPSIsOPen(final Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }

    /**
     * 检测wifi或网络是否开启
     *
     * @param
     * @return
     */
    // public static boolean checkNet(Context context) {//
    // 获取手机所有连接管理对象（包括对wi-fi,
    // // net等连接的管理）
    // try {
    // ConnectivityManager connectivity = (ConnectivityManager) context
    // .getSystemService(Context.CONNECTIVITY_SERVICE);
    // if (connectivity != null) {
    // // 获取网络连接管理的对象
    // NetworkInfo info = connectivity.getActiveNetworkInfo();
    // // System.out.println(">>>>>>>>>>>>Net:"+info);
    // if (info == null || !info.isAvailable()) {
    // return false;
    // } else {
    // return true;
    // }
    // }
    //
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // return false;
    // }
    public static boolean isWifiActive(Context icontext) {

        Context context = icontext.getApplicationContext();
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info;
        if (connectivity != null) {
            info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getTypeName().equals("WIFI") && info[i].isConnected()) {
                        return true;
                    }
                }
            }
        }
        return false;

    }

    /**
     * 附件
     *
     * @param func
     */
    private final int REQUEST_CODE_SELECT_FILE = 201;


    public void attachment(Func func) {
        Intent intent = null;
        intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // if (Build.VERSION.SDK_INT < 20) {
        // intent = new Intent(Intent.ACTION_GET_CONTENT);
        // intent.setType("*/*");
        // intent.addCategory(Intent.CATEGORY_OPENABLE);
        // } else {
        // intent = new Intent( Intent.ACTION_PICK,
        // android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // }
        startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
    }

//    private final int REQUEST_CODE_SIGNATURE = 202;
    public void into_SIGNATURE() {
        Intent intent = null;
        intent = new Intent();
        intent.setClass(AbsFuncActivity.this,PaintActivity.class);
        startActivityForResult(intent, 400);
    }

    /**
     * 视频
     *
     * @param func
     */
    private final int REQUEST_CODE_SELECT_VIDEO = 200;

    private void video(Func func) {
        Intent intent_video = new Intent(this, ImageGridActivity.class);
        int flag = func.getPhotoFlg();// 1：只能现场拍摄、2 可拍摄可选择
        intent_video.putExtra("isSelect", flag == 1 ? "1" : "0");// 是否只能拍摄
        // 1是只能拍摄
        startActivityForResult(intent_video, REQUEST_CODE_SELECT_VIDEO);
    }

    /**
     * 选择视频返回
     */
    private Dialog videoDialog;

    private void resultForVideo(Intent data) {
        if (videoDialog == null) {
            videoDialog = new MyProgressDialog(this, R.style.CustomProgressDialog, getResources().getString(R.string.absfuncactivity_message07));
        }
        if (videoDialog != null && !videoDialog.isShowing()) {
            videoDialog.show();
        }
        if (data != null) {
            final String path = data.getStringExtra("path");
            if (!TextUtils.isEmpty(path)) {
                new Thread() {
                    public void run() {
                        try {
                            if (!TextUtils.isEmpty(path)) {
                                int index = path.lastIndexOf("/");
                                String name = path.substring(index + 1);
                                FileHelper helper = new FileHelper();
                                SharedPrefrencesForWechatUtil.getInstance(AbsFuncActivity.this).setFileName(name, name);
                                File file = new File(Constants.VIDEO_PATH);
                                if (!file.exists()) {
                                    file.mkdirs();
                                }
                                helper.copyFile(path, Constants.VIDEO_PATH + name);

                                Message msg = videoHander.obtainMessage();
                                msg.what = 1;
                                msg.obj = name;
                                videoHander.sendMessage(msg);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            videoHander.sendEmptyMessage(2);
                        }
                    }

                    ;
                }.start();

            }
        }
    }

    private Handler videoHander = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int what = msg.what;
            String fileName = (String) msg.obj;
            if (videoDialog != null && videoDialog.isShowing()) {
                videoDialog.dismiss();
            }
            switch (what) {
                case 1:
                    saveVideo(fileName, currentFunc);
                    setShowHook(false, hookView);
                    setShowHook();
                    // ToastOrder.makeText(AbsFuncActivity.this, "test5",
                    // ToastOrder.LENGTH_SHORT).show();

                    break;
                case 2:
                    ToastOrder.makeText(AbsFuncActivity.this, getResources().getString(R.string.absfuncactivity_message08), ToastOrder.LENGTH_SHORT).show();
                    break;
                case 3:
                    ToastOrder.makeText(AbsFuncActivity.this, getResources().getString(R.string.absfuncactivity_message09), ToastOrder.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        }

        ;
    };

    /**
     * Android得到视频缩略图
     *
     * @param filePath
     * @return
     */
    public Bitmap getVideoThumbnail(String filePath) {
        Bitmap bitmap = null;
        // 利用ThumnailUtils
        // bitmap = ThumbnailUtils.createVideoThumbnail(infor.srcPath,
        // Images.Thumbnails.MINI_KIND);
        MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
        try {
            if (android.os.Build.VERSION.SDK_INT >= 14) {
                metadataRetriever.setDataSource(filePath, new HashMap<String, String>());
                ;
            } else {
                metadataRetriever.setDataSource(filePath);
            }
            bitmap = metadataRetriever.getFrameAtTime();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            metadataRetriever.release();
        }
        return bitmap;
    }

    /**
     * 附件选择返回
     */
    private void resultForFile(Intent data) {
        Uri uri = data.getData();
        String filePath = getPath(this, uri);
        File file = new File(filePath);
        if (file == null || !file.exists()) {
            ToastOrder.makeText(this, getResources().getString(R.string.no_file), ToastOrder.LENGTH_SHORT).show();
            return;
        }
        if (file.length() > 10 * 1024 * 1024) {
            ToastOrder.makeText(this, getResources().getString(R.string.file_donot_more10M), ToastOrder.LENGTH_SHORT).show();
            return;
        }
        if (filePath.endsWith(".doc") || filePath.endsWith(".docx") || filePath.endsWith(".xls")
                || filePath.endsWith(".xlsx") || filePath.endsWith(".ppt") || filePath.endsWith(".pptx")
                || filePath.endsWith(".pdf") || filePath.endsWith(".txt")) {
            fileResult(file);
        } else {
            fileHander.sendEmptyMessage(3);
            return;
        }
    }

    private Handler fileHander = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int what = msg.what;
            if (fileDialog != null && fileDialog.isShowing()) {
                fileDialog.dismiss();
            }
            switch (what) {
                case 1:
                    String name = (String) msg.obj;
                    saveVideo(name, currentFunc);
                    setShowHook(false, hookView);
                    setShowHook();
                    // ToastOrder.makeText(AbsFuncActivity.this, "test1",
                    // ToastOrder.LENGTH_SHORT).show();

                    break;
                case 2:
                    ToastOrder.makeText(AbsFuncActivity.this, getResources().getString(R.string.absfuncactivity_message08), ToastOrder.LENGTH_SHORT).show();
                    break;
                case 3:
                    ToastOrder.makeText(AbsFuncActivity.this, getResources().getString(R.string.absfuncactivity_message09), ToastOrder.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        }

        ;
    };

    private Dialog fileDialog;

    private void fileResult(final File srcFile) {
        if (fileDialog == null) {
            fileDialog = new MyProgressDialog(this, R.style.CustomProgressDialog, getResources().getString(R.string.absfuncactivity_message02));
        }
        if (fileDialog != null && !fileDialog.isShowing()) {
            fileDialog.show();
        }
        new Thread() {
            public void run() {
                try {
                    String filePath = srcFile.getAbsolutePath();
                    if (!TextUtils.isEmpty(filePath)) {
                        int index = filePath.lastIndexOf("/");
                        String name = filePath.substring(index + 1);
                        FileHelper helper = new FileHelper();
                        String[] fileStr = name.split("\\.");
                        String newName = String.valueOf(System.currentTimeMillis()) + "." + fileStr[fileStr.length - 1];
                        SharedPrefrencesForWechatUtil.getInstance(AbsFuncActivity.this).setFileName(newName, name);
                        File fDir = new File(Constants.FUNC_ATTACHMENT_PATH);
                        if (!fDir.exists()) {
                            fDir.mkdirs();
                        }
                        helper.copyFile(filePath, Constants.FUNC_ATTACHMENT_PATH + newName);

                        Message msg = fileHander.obtainMessage();
                        msg.what = 1;
                        msg.obj = newName;
                        fileHander.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    fileHander.sendEmptyMessage(2);
                }
            }

            ;
        }.start();
    }

    /**
     * 新订单
     */
    protected void newOrder(Func func) {
    }

    ;

    /**
     * 订单3
     */
    protected void threeOrder(Func func) {
        // Intent intent = new Intent(this,Order3MainActivity.class);
        // startActivity(intent);
    }

    ;

    /**
     * 订单3送货
     */
    protected void threeOrderSend(Func func) {
        // Intent intent = new Intent(this,Order3SendActivity.class);
        // startActivity(intent);
    }

    ;

    /**
     * 查询submitId
     *
     * @return
     */
    protected int submitId() {
        SubmitDB submitDB = new SubmitDB(this);
        int submitId = submitDB.selectSubmitId(planId, wayId, storeId, targetId, menuType);
        if (submitId == Constants.DEFAULTINT) {// 等于0表示没有操作任何选项
            Submit submit = new Submit();
            submit.setPlanId(planId);
            submit.setState(Submit.STATE_NO_SUBMIT);
            submit.setStoreId(storeId);
            submit.setStoreName(storeName);
            submit.setWayName(wayName);
            submit.setWayId(wayId);
            submit.setTargetid(targetId);
            submit.setTargetType(menuType);
            submit.setMenuId(menuId);
            submit.setMenuType(menuType);
            submit.setMenuName(menuName);
            submitDB.insertSubmit(submit);
            submitId = submitDB.selectSubmitIdNotCheckOut(planId, wayId, storeId, targetId, menuType,
                    Submit.STATE_NO_SUBMIT);
        }
        return submitId;
    }

    /**
     * @return 新订单的店面控件
     */
    protected Func newOrderStoreFunc() {
        Func storeFunc = null;
        for (int i = 0; i < funcList.size(); i++) {// 查找店面控件
            Func func = funcList.get(i);
            if (func.getOrgOption() != null && func.getOrgOption() == Func.ORG_STORE) {
                storeFunc = func;
                break;
            }
        }
        return storeFunc;
    }

    /**
     * 新订单选择的店面
     *
     * @param storeFunc
     * @return
     */
    protected OrgStore chosedNewOrderStore(Func storeFunc) {
        OrgStore store = null;
        if (storeFunc != null) {
            SubmitItem item = new SubmitItemDB(this).findSubmitItemByParamName(String.valueOf(storeFunc.getFuncId()));
            if (item != null && !TextUtils.isEmpty(item.getParamValue())) {// 没输入店面信息
                store = new OrgStoreDB(this).findOrgStoreByStoreId(item.getParamValue());// 根据店面ID查找店面
            }
        }
        return store;
    }

    /**
     * 弹出操作dialog
     *
     * @param currentFunc 当前操作的控件
     * @param v           当前操作的view
     */
    public void openCompDialog(Func currentFunc, View v, int type1) {
        hookView = v.findViewById(R.id.iv_func_check_mark);
        TView = (TextView) v.findViewById(R.id.tv_func_content);// 初始化对勾view
        if (currentFunc.getDefaultType() != null && currentFunc.getDefaultType() == Func.DEFAULT_TYPE_SQL) {// sql类型的执行SQL查询
            new SqlInComp(this, modType, isLinkActivity, menuId, menuName, v, contrlEnterList).getSearchRequirement2(currentFunc);
            isCanClick = true;
        } else {
            if (currentFunc.getIsFuzzy() != null && currentFunc.getIsFuzzy() == Func.IS_Y) {// 单选模糊搜索其他
                if (currentFunc.getType() == TYPE_MULTI_CHOICE_SPINNER_COMP) {// 多选
                    currentFunc.setType(Func.TYPE_MULTI_CHOICE_FUZZY_QUERY_COMP);// 设置成多选模糊搜索
                } else if (currentFunc.getType() == Func.TYPE_SELECTCOMP) {
                    currentFunc.setType(Func.TYPE_SINGLE_CHOICE_FUZZY_QUERY_COMP);// 单选
                } else if (currentFunc.getType() == Func.TYPE_SELECT_OTHER) {// 单选其他类型的设置成单选其他模糊搜索
                    currentFunc.setType(Func.TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY_COMP);
                }
            }
            if (currentFunc.getOrgOption() != null && currentFunc.getOrgOption() == Func.ORG_STORE
                    && currentFunc.getIsAreaSearch() == 1 && currentFunc.getAreaSearchValue() != null
                    && currentFunc.getAreaSearchValue() > 0) {
                location(true);
            } else if (currentFunc.getType() == Func.TYPE_EDITCOMP && type1 != 1) {
                isCanClick = true;
                RelativeLayout iv_func_photo_container = (RelativeLayout) v.findViewById(R.id.iv_func_photo_container);
                EditText textEditTextComp = (EditText) iv_func_photo_container.findViewById(R.id.textEditTextComp);
                textEditTextComp.setCursorVisible(true);
                textEditTextComp.setFocusable(true);
                textEditTextComp.setFocusableInTouchMode(true);
                textEditTextComp.requestFocus();
                textEditTextComp.findFocus();
                textEditTextComp.setSelection(textEditTextComp.getText().toString().length());
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            } else {
                CompDialog c = new CompDialog(this, currentFunc, bundle, contrlEnterList);// 弹出dialog
                c.setMenuId(menuId);
                c.setMenuType(menuType);
                c.setMenuName(menuName);
                c.setProductCodeListener(this);
                dialog = c.createDialog();
                if (currentFunc.getType() == Func.TYPE_SELECTCOMP
                        || currentFunc.getType() == TYPE_MULTI_CHOICE_SPINNER_COMP
                        || currentFunc.getType() == Func.TYPE_SINGLE_CHOICE_FUZZY_QUERY_COMP
                        || currentFunc.getType() == Func.TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY_COMP
                        || currentFunc.getType() == Func.TYPE_MULTI_CHOICE_FUZZY_QUERY_COMP
                        || currentFunc.getType() == Func.TYPE_SELECT_OTHER
                        || currentFunc.getType() == Func.TYPE_HIDDEN) {

                } else {
                    dialog.show();
                }
                //
                if (currentFunc.getType() == Func.TYPE_PRODUCT_CODE) {
                    productCode = c.getProductCode();
                }
                if (currentFunc.getType() == Func.TYPE_SCAN_INPUT) {
                    scanInputProductCode = c.getScanProductCode();
                }

                isCanClick = true;
            }
        }
    }

    /**
     * 点击BUTTON类型
     *
     * @param func
     */
    private void clickButton(Func func) {

        new SubmitItemDB(this).deleteSubmitItemByType(Func.TYPE_BUTTON);// 回退的时候删除BUTTON类型的数据库记录

        SubmitDB submitDB = new SubmitDB(this);
        SubmitItemDB submitItemDB = new SubmitItemDB(this);
        SubmitItem submitItem = new SubmitItem();
        submitItem.setTargetId(func.getTargetid() + "");
        int submitId = submitDB.selectSubmitIdNotCheckOut(planId, wayId, storeId, targetId, menuType,
                Submit.STATE_NO_SUBMIT);
        if (submitId != Constants.DEFAULTINT) {// 首先查找有没有该条数据对应的Submit
            submitItem.setParamName(String.valueOf(func.getFuncId()));
            submitItem.setParamValue(String.valueOf(func.getStatus()));
            submitItem.setSubmitId(submitId);
            submitItem.setType(Func.TYPE_BUTTON);
            submitItem.setIsCacheFun(currentFunc.getIsCacheFun());
            boolean isHas = submitItemDB.findIsHaveParamName(submitItem.getParamName(), submitId);
            if (isHas) {// 如果有就更新没有就插入
                if (this instanceof ChangeModuleFuncActivity) {

                    submitItemDB.updateSubmitItem(submitItem, false);
                } else {
                    submitItemDB.updateSubmitItem(submitItem, true);
                }

            } else {
                if (this instanceof ChangeModuleFuncActivity) {

                    submitItemDB.insertSubmitItem(submitItem, false);
                } else {
                    submitItemDB.insertSubmitItem(submitItem, true);
                }

            }

        } else {// 没有该条数据对应的Submit 在submitDB中先插入一条数据，与该条操作对应。
            Submit submit = new Submit();
            submit.setPlanId(planId);
            submit.setState(Submit.STATE_NO_SUBMIT);
            submit.setStoreId(storeId);
            submit.setWayId(wayId);
            submit.setTargetid(targetId);
            submit.setTargetType(menuType);
            if (modType != 0) {
                submit.setModType(modType);
            }
            submit.setMenuId(menuId);
            submit.setMenuType(menuType);
            submit.setMenuName(menuName);
            long id = submitDB.insertSubmit(submit);
            submitItem.setParamName(String.valueOf(func.getFuncId()));
            submitItem.setType(Func.TYPE_BUTTON);
            submitItem.setParamValue(String.valueOf(func.getStatus()));
            submitItem.setSubmitId(Integer.valueOf(String.valueOf(id)));
            // submitItem.setMenuId(menuId);
            submitItem.setIsCacheFun(currentFunc.getIsCacheFun());
            if (this instanceof ChangeModuleFuncActivity) {

                submitItemDB.insertSubmitItem(submitItem, false);// 插入该submitItem
            } else {
                submitItemDB.insertSubmitItem(submitItem, true);// 插入该submitItem
            }

        }

        showPreview();
        theOperatedFinish();
    }

    /**
     * 店面修改
     */
    private void storeUpdata(Func func) {
        Intent intent = new Intent(this, InfoDetailActivity.class);
        bundle.putInt("menuId", menuId);
        bundle.putString("menuName", menuName);
        intent.putExtra("bundle", bundle);
        intent.putExtra("storeType", Func.IS_STORE_UPDATE);
        intent.putExtra("funcName", func.getName());
        startActivity(intent);

    }

    /**
     * 店面信息查看
     */
    private void storeView(Func func) {
        Intent intent = new Intent(this, InfoDetailActivity.class);
        bundle.putInt("menuId", menuId);
        bundle.putString("menuName", menuName);
        intent.putExtra("bundle", bundle);
        intent.putExtra("storeType", Func.IS_STORE_VIEW);
        intent.putExtra("funcName", func.getName());
        startActivity(intent);
    }

    private AbsCameraComp cameraComp = null;

    public void takePhoto(int photoFlg) {
        Func func = this.funcList.get(this.clickFuncIndex);
        fileName = System.currentTimeMillis() + ".jpg";
        if (linkId != 0) {
            cameraComp = new CameraCompForLink(this, func);
        } else {
            cameraComp = new CameraComp(this, func);

        }
        cameraComp.setMenuId(menuId);
        cameraComp.setMenuType(menuType);
        cameraComp.setMenuName(menuName);
        if (photoFlg == 2) {// 从相册中选择
            cameraComp.startPhotoAlbum();
        } else {
            cameraComp.startPhoto(fileName);
        }
    }

    /*
	 * 拍二维码
	 */
    private void clickScan() {
        Func func = this.funcList.get(this.clickFuncIndex);
        // Intent i = new Intent(this, CaptureActivity.class);
        if (PublicUtils.ISDEMO && func.getTargetid() == 2028934) {// 暂时配置nfc
            NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
            if (nfcAdapter != null) {
                Intent nfcIntent = new Intent(this, NfcFuncActivity.class);
                nfcIntent.putExtra("nfctag", 11);
                paramName = String.valueOf(func.getFuncId());
                if (nfcIntent != null) {
                    startActivityForResult(nfcIntent, 101);
                }
            } else {
                Toast.makeText(this, getResources().getString(R.string.absfuncactivity_message01), Toast.LENGTH_SHORT).show();
            }

        } else {// 正常模块
            Intent i = PhoneModelManager.getIntent(this, true);
            paramName = String.valueOf(func.getFuncId());
            if (i != null) {
                startActivityForResult(i, 101);
            }
        }

    }

    public void subimitResultToDo(int requestCode, Intent data) {
        theOperatedFinish();
        subimitResult(requestCode, data);
    }

    public void onActivityResultAbs(int requestCode, int resultCode, Intent data) {
        isCanClick = true;
        if (resultCode == R.id.submit_succeeded) {
            theOperatedFinish();
            subimitResult(requestCode, data);
        }
        application.isSubmitActive = true;
    }

    private MyProgressDialog mDialog = null;

    /**
     * 拍照 扫描返回
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        isCanClick = true;
        if (resultCode == R.id.submit_succeeded) {
            theOperatedFinish();
            subimitResult(requestCode, data);
        } else if (Activity.RESULT_OK == resultCode || resultCode == R.id.scan_succeeded
                || resultCode == R.id.sql_big_data) {
            // if (data == null||"".equals(data)) {
            //
            // Log.e("takePhoto", "data is null");
            //// Toast.makeText(this, "data is null",
            // Toast.LENGTH_SHORT).show();
            // return;}
            if (requestCode == 102) {// 串码扫描返回
                String code = data.getStringExtra("SCAN_RESULT");
                resultForProductCode(code);
            } else if (requestCode == 105) {// 可输入类型的扫描控件返回
                String code = data.getStringExtra("SCAN_RESULT");
                resultForScanInputCode(code);
            } else if (requestCode == 103) {// sql数据跳转
                String sqlValue = data.getStringExtra("SQL_BIG_DATA");
                boolean clear = data.getBooleanExtra("SQL_BIG_DATA_CLEAR", false);
                resultForSqlBigData(sqlValue, clear);
            } else if (requestCode == 101) {// 扫描
                String code = data.getStringExtra("SCAN_RESULT");
                resultForScanf(code);
                if (currentFunc != null && currentFunc.getType() == Func.TYPE_SCAN) {
                    TView.setText(code);
                }
            } else if (requestCode == 100) {// 拍照
                JLog.d("拍照返回**************************");//fileName
                try {
                    mDialog = new MyProgressDialog(AbsFuncActivity.this, R.style.CustomProgressDialog, getResources().getString(R.string.loading));
                    mDialog.show();

                    setPhotoSYTime(data, requestCode);
                } catch (Exception e) {
                    JLog.d("拍照保存异常");
                    JLog.e(e);
                }
            }else if(requestCode == 400){
                try {
                    mDialog = new MyProgressDialog(AbsFuncActivity.this, R.style.CustomProgressDialog, getResources().getString(R.string.loading));
                    mDialog.show();

                    fileName = data.getStringExtra("path");
                    setPhotoSYTime(data, requestCode);
                } catch (Exception e) {
                    JLog.d("拍照保存异常");
                    JLog.e(e);
                }
            } else if (requestCode == 104) {// 从相册中选取的图片
                mDialog = new MyProgressDialog(AbsFuncActivity.this, R.style.CustomProgressDialog, getResources().getString(R.string.loading));
                mDialog.show();
                setPhotoSYTime(data, requestCode);
            } else if (requestCode == REQUEST_CODE_SELECT_VIDEO) {
                resultForVideo(data);
            } else if (requestCode == REQUEST_CODE_SELECT_FILE) {
                resultForFile(data);
            } else if (requestCode == 109) {// 表格里控件的拍照
                tableListMap.get(whichTablePhoto).setCameraPhoto(data, requestCode);
            } else if (requestCode == 110) {// 表格里控件从相册选取图片
                tableListMap.get(whichTablePhoto).setCameraPhoto(data, requestCode);
            } else if (requestCode == 111) {// 表格里控件的扫描
                tableListMap.get(whichTablePhoto).setScanCode(data, requestCode);
            } else if (requestCode == 112) {// 表格里控件的可输入扫描
                tableListMap.get(whichTablePhoto).setScanCodeInput(data, requestCode);
            } else if (requestCode == 113) {// 表格里控件的串码
                tableListMap.get(whichTablePhoto).setProductCode(data, requestCode);
            } else if (requestCode == 211) {//固定资产选择返回
                List<AssetsBean> selectAsset = (List<AssetsBean>) data.getSerializableExtra("select_data");
                toAddlistFuncView(selectAsset,true);
            } else if (requestCode == 212) {//固定资产扫描返回
                //根据商品id 去查详细信息
                String scanId = data.getStringExtra("SCAN_RESULT");
                if (!TextUtils.isEmpty(scanId)) {
                    requestDetailAsset(scanId);
                } else {
                    Toast.makeText(AbsFuncActivity.this, "扫描失败", Toast.LENGTH_SHORT).show();
                }


            }else if(requestCode == 412){//标题栏扫描按钮返回
                String scanId = data.getStringExtra("SCAN_RESULT");
                if(!TextUtils.isEmpty(scanId)){
                    requestScanCode(scanId);
                }else{
                    Toast.makeText(AbsFuncActivity.this,"扫描失败",Toast.LENGTH_SHORT).show();
                }
            }
        }
        application.isSubmitActive = true;
    }

    //根据资产编码去查询资产的详细信息
    private void requestDetailAsset(String scanId) {
        if (TextUtils.isEmpty(scanId)){
            return;
        }
        String url = UrlInfo.baseAssetsUrl(AbsFuncActivity.this);
        String phone = PublicUtils.receivePhoneNO(AbsFuncActivity.this);
        int dyType = 3;
        String status = "\""+currentListFunt1.getmFunc().getScanStatus()+"\"";
//        String status = "\"0\"";
        String scId = "\""+scanId+"\"";
        if (!TextUtils.isEmpty(status)){
            status = "{\"scan_status\""+":"+status+","+"\"scan_cols\""+":"+scId+"}";
        }
        Log.d("views","status: "+status);
        RequestParams params = new RequestParams();
//        phoneno=15555555551&test=gcg&moduleId=top&dyType=3&dynamic_param={"scan_status":"2"}
        params.put("phoneno",phone);
        params.put("test","gcg");
        params.put("moduleId","top");
        params.put("dyType",dyType);
        if (!TextUtils.isEmpty(status)){
            params.put("dynamic_param",status);
        }
        GcgHttpClient.getInstance(AbsFuncActivity.this).get(url, params, new HttpResponseListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onSuccess(int statusCode, String content) {
                Log.d("views","content: "+content);
                List<AssetsBean> assetsBeenScan = toParseIdle(content);
                if (assetsBeenScan.size()<=0){
                    Toast.makeText(application, "未查到此编码的资产", Toast.LENGTH_SHORT).show();
                }else{
                    toAddlistFuncView(assetsBeenScan,false);
                }

            }

            @Override
            public void onFailure(Throwable error, String content) {
                Toast.makeText(application, "查询失败", Toast.LENGTH_SHORT).show();
            }
        });



    }
    //扫码回来的总数据
   private List<AssetsBean>  allAssetsBeenScan = new ArrayList<>();
    //解析闲置数据
    private  List<AssetsBean> toParseIdle(String content) {
        List<AssetsBean> assetsList = new ArrayList<>();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(application, "未查询到匹配数据", Toast.LENGTH_SHORT).show();
            return assetsList;
        }


        try {
            JSONObject jsonObject = new JSONObject(content);
            if (!jsonObject.getString("resultcode").equals("0000")) {
                Toast.makeText(application, "返回数据异常", Toast.LENGTH_SHORT).show();
                return assetsList;
            }
            if (jsonObject.has("dynamic_data") && jsonObject.has("dynamic_title")) {
                JSONArray arrayData = jsonObject.getJSONArray("dynamic_data");
                JSONArray arrayTitle = jsonObject.getJSONArray("dynamic_title");
                if (arrayData.length() < 1 || arrayTitle.length() < 1) {
                    return assetsList;
                }
                for (int i = 0; i < arrayData.length(); i++) {
                    AssetsBean assetsBean = new AssetsBean();
                    JSONArray jsonArray = arrayData.getJSONArray(i);
                    assetsBean.setId(String.valueOf(jsonArray.getInt(0)));
                    assetsBean.setIdTip(String.valueOf(arrayTitle.get(0)));
                    assetsBean.setState(jsonArray.getString(1));
                    assetsBean.setStateTip(arrayTitle.getString(1));
                    assetsBean.setUrl(jsonArray.getString(2));
                    assetsBean.setUrlTip(arrayTitle.getString(2));
                    assetsBean.setTitle(jsonArray.getString(3));
                    assetsBean.setTitleTip(arrayTitle.getString(3));
                    assetsBean.setCode(jsonArray.getString(4));
                    assetsBean.setCodeTip(arrayTitle.getString(4));
                    assetsBean.setSn(jsonArray.getString(5));
                    assetsBean.setSnTip(arrayTitle.getString(5));
                    assetsBean.setArea(jsonArray.getString(6));
                    assetsBean.setAreaTip(arrayTitle.getString(6));
                    assetsBean.setPutAddress(jsonArray.getString(7));
                    assetsBean.setPutAddressTip(arrayTitle.getString(7));
                    assetsBean.setUseCompany(jsonArray.getString(8));
                    assetsBean.setUseCompanyTip(arrayTitle.getString(8));
                    assetsBean.setUsePart(jsonArray.getString(9));
                    assetsBean.setUsePartTip(arrayTitle.getString(9));
                    assetsBean.setKind(jsonArray.getString(10));
                    assetsBean.setKindTip(arrayTitle.getString(10));
                    assetsBean.setFineKind(jsonArray.getString(11));
                    assetsBean.setFineKindTip(arrayTitle.getString(11));
                    assetsBean.setProKind(jsonArray.getString(12));
                    assetsBean.setProKindTip(arrayTitle.getString(12));
                    assetsBean.setBelongKind(jsonArray.getString(13));
                    assetsBean.setBelongKindTip(arrayTitle.getString(13));
                    assetsList.add(assetsBean);
                }
            }

        } catch (JSONException e) {
            Toast.makeText(application, "返回数据解析异常", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        if (assetsList.size()>0){
            allAssetsBeenScan.addAll(assetsList);
        }
       return assetsList;

    }

    //添加选项,是否先清空
    private void toAddlistFuncView(List<AssetsBean> assetsBeanList,boolean isClear) {
        if (isClear){
            currentListFunt1.celarAll();
            for (int i = 0; i < allAssetsBeenScan.size(); i++) {
                AssetsBean assetsBean = allAssetsBeenScan.get(i);
                currentListFunt1.addOneItem(assetsBean);
            }
        }
        for (int i = 0; i < assetsBeanList.size(); i++) {
            AssetsBean assetsBean = assetsBeanList.get(i);
            currentListFunt1.addOneItem(assetsBean);
        }

    }


    /**
     * 从相册选择照片后返回
     *
     * @param data
     */
    private void resultForPhotoAlbum(Intent data, Date date) {
        try {
            if (data != null) {
                Uri originalUri = data.getData(); // 获得图片的uri
                if (originalUri != null) {
                    File file = new File(Constants.PATH_TEMP);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    String tempPath = Constants.PATH_TEMP + fileName;
                    String path = getPath(AbsFuncActivity.this, originalUri);
                    // Bitmap bitmap =
                    // MediaStore.Images.Media.getBitmap(getContentResolver(),
                    // originalUri); // 显得到bitmap图片
                    Bitmap bitmap = FileHelper.lessenUriImage(path); // 显得到bitmap图片
                    FileHelper.saveBitmapToFile(bitmap, tempPath);
                    resultForPhoto(date);
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }

                } else {
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    ToastOrder.makeText(this, getResources().getString(R.string.absfuncactivity_message06), ToastOrder.LENGTH_SHORT).show();

                }
            } else {
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                }
                ToastOrder.makeText(this, getResources().getString(R.string.absfuncactivity_message06), ToastOrder.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
            ToastOrder.makeText(this, getResources().getString(R.string.absfuncactivity_message06), ToastOrder.LENGTH_SHORT).show();
        }
    }

    /**
     * 串码扫描返回
     */
    private void resultForProductCode(String code) {
        if (productCode == null) {// 处理扫描串码异常返回情况
            currentFunc = funcList.get(clickFuncIndex);
            CompDialog c = new CompDialog(this, currentFunc, bundle);// 弹出dialog
            c.setMenuId(menuId);
            c.setMenuType(menuType);
            c.setMenuName(menuName);
            c.setProductCodeListener(this);
            dialog = c.createDialog();
            dialog.show();
            if (currentFunc.getType() == Func.TYPE_PRODUCT_CODE) {
                productCode = c.getProductCode();
            }
        } else {
            if (dialog != null && !dialog.isShowing()) {
                dialog.show();
            }
        }
        if (productCode != null) {
            productCode.setProductCode(code);
        }
    }

    /**
     * 可输入类型的扫描返回
     */
    private void resultForScanInputCode(String code) {
        if (scanInputProductCode == null) {// 处理扫描串码异常返回情况
            currentFunc = funcList.get(clickFuncIndex);
            CompDialog c = new CompDialog(this, currentFunc, bundle);// 弹出dialog
            c.setMenuId(menuId);
            c.setMenuType(menuType);
            c.setMenuName(menuName);
            c.setProductCodeListener(this);
            dialog = c.createDialog();
            dialog.show();
            if (currentFunc.getType() == Func.TYPE_SCAN_INPUT) {
                scanInputProductCode = c.getScanProductCode();
            }
        } else {
            if (dialog != null && !dialog.isShowing()) {
                dialog.show();
            }
        }
        if (scanInputProductCode != null) {
            scanInputProductCode.setProductCode(code);
        }
    }

    /**
     * sql大数据作为查询条件返回
     *
     * @param sqlValue
     * @param clear
     */
    private void resultForSqlBigData(String sqlValue, boolean clear) {
        if (this instanceof QueryFuncActivity) {// 扫码作为查询条件的时候转为输入框
            QueryFuncActivity queryFuncActivity = (QueryFuncActivity) this;
            if (clear) {
                queryFuncActivity.replenish.remove(String.valueOf(currentFunc.getFuncId()));
                queryFuncActivity.replenish.remove(currentFunc.getFuncId() + "_value");
                queryFuncActivity.setShowHook(false, hookView);
            } else {
                String[] str = sqlValue.split("@");
                if (str.length == 2) {
                    queryFuncActivity.replenish.putString(currentFunc.getFuncId() + "", str[0]);
                    queryFuncActivity.replenish.putString(currentFunc.getFuncId() + "_value", str[1]);
                    queryFuncActivity.setShowHook(false, hookView);
                    // ToastOrder.makeText(AbsFuncActivity.this, "test2",
                    // ToastOrder.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * 扫描返回
     *
     * @param value
     */
    private void resultForScanf(String value) {
        theOperatedFinish();
        SubmitDB submitDB = new SubmitDB(this);
        SubmitItemDB submitItemDB = new SubmitItemDB(this);
        SubmitItemTempDB linkSubmitItemTempDB = new SubmitItemTempDB(this);

        int submitId = submitDB.selectSubmitIdNotCheckOut(planId, wayId, storeId, targetId, menuType,
                Submit.STATE_NO_SUBMIT);
        if (submitId != Constants.DEFAULTINT) {// 首先查找有没有该条数据对应的Submit
            SubmitItem submitItem = new SubmitItem();
            submitItem.setTargetId(currentFunc.getTargetid() + "");
            submitItem.setSubmitId(submitId);
            submitItem.setParamName(paramName);
            submitItem.setParamValue(value);
            submitItem.setType(Func.TYPE_SCAN);
            submitItem.setIsCacheFun(currentFunc.getIsCacheFun());
            boolean isHas = false;
            if (linkId != 0) {
                isHas = linkSubmitItemTempDB.findIsHaveParamName(submitItem.getParamName(), submitId);// 首先判断数据库中有没有该项操作
            } else {
                isHas = submitItemDB.findIsHaveParamName(submitItem.getParamName(), submitId);// 首先判断数据库中有没有该项操作
            }
            if (isHas) {// 如果有就更新没有就插入 此时不能删除原来的图片，因为用户可能不保存
                if (linkId != 0) {
                    linkSubmitItemTempDB.updateSubmitItem(submitItem);
                } else {
                    if (this instanceof ChangeModuleFuncActivity) {

                        submitItemDB.updateSubmitItem(submitItem, false);
                    } else {
                        submitItemDB.updateSubmitItem(submitItem, true);

                    }
                }
            } else {
                if (linkId != 0) {
                    linkSubmitItemTempDB.insertSubmitItem(submitItem);
                } else {
                    // submitItem.setMenuId(menuId);
                    if (this instanceof ChangeModuleFuncActivity) {
                        submitItemDB.insertSubmitItem(submitItem, false);
                    } else {
                        submitItemDB.insertSubmitItem(submitItem, true);
                    }
                }
            }
        } else {// 没有该条数据对应的Submit 在submitDB中先插入一条数据，与该条操作对应。
            Submit submit = new Submit();
            submit.setPlanId(planId);
            submit.setState(Submit.STATE_NO_SUBMIT);
            submit.setStoreId(storeId);
            submit.setWayId(wayId);
            submit.setTargetid(targetId);
            submit.setTargetType(menuType);
            if (modType != 0) {
                submit.setModType(modType);
            }
            submit.setMenuId(menuId);
            submit.setMenuType(menuType);
            submit.setMenuName(menuName);
            long submitid = submitDB.insertSubmit(submit);
            SubmitItem submitItem = new SubmitItem();
            submitItem.setSubmitId(Integer.valueOf(submitid + ""));
            submitItem.setParamName(paramName);
            submitItem.setParamValue(value);
            submitItem.setType(Func.TYPE_SCAN);
            submitItem.setIsCacheFun(currentFunc.getIsCacheFun());
            // 插入该submitItem
            if (linkId != 0) {
                linkSubmitItemTempDB.insertSubmitItem(submitItem);// 插入该submitItem
            } else {
                // submitItem.setMenuId(menuId);
                if (this instanceof ChangeModuleFuncActivity) {

                    submitItemDB.insertSubmitItem(submitItem, false);// 插入该submitItem
                } else {

                    submitItemDB.insertSubmitItem(submitItem, true);// 插入该submitItem
                }
            }
        }
        if (!isNormal) {// 非正常关闭
            setShowHook();
            isNormal = true;
        }
    }

    /**
     * 拍照返回
     */
    private void resultForPhoto(Date date) {
        theOperatedFinish();
        setAttenTime();
        if (cameraComp == null) {
            currentFunc = this.funcList.get(this.clickFuncIndex);
            if (linkId != 0) {
                cameraComp = new CameraCompForLink(this, currentFunc);
            } else {
                cameraComp = new CameraComp(this, currentFunc);
            }
            cameraComp.setMenuId(menuId);
            cameraComp.setMenuType(menuType);
            cameraComp.setMenuName(menuName);
        }
        File file = new File(Constants.PATH_TEMP + fileName);
        if (!file.exists()) {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
            ToastOrder.makeText(AbsFuncActivity.this, getResources().getString(R.string.absfuncactivity_message05), ToastOrder.LENGTH_LONG).show();
        } else {
            int waterMarkCustom = currentFunc.getIsImgCustom();
            if (waterMarkCustom == 1) {// 控件需要添加自定义水印内容
                waterMarkDialog(cameraComp, date);
            } else {
                cameraComp.resultPhoto(fileName, date);
                setShowHook();
                if (!isNormal) {// 非正常关闭
                    setShowHook();
                    isNormal = true;
                }
            }
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
        }

    }

    /**
     * 表格拍照返回
     */
    private void resultTableForPhoto(Date date) {
        theOperatedFinish();
        setAttenTime();
        if (cameraComp == null) {
            currentFunc = this.funcList.get(this.clickFuncIndex);
            if (linkId != 0) {
                cameraComp = new CameraCompForLink(this, currentFunc);
            } else {
                cameraComp = new CameraComp(this, currentFunc);
            }
            cameraComp.setMenuId(menuId);
            cameraComp.setMenuType(menuType);
            cameraComp.setMenuName(menuName);
        }
        File file = new File(Constants.PATH_TEMP + TableCompView.fileName);
        if (!file.exists()) {
            ToastOrder.makeText(AbsFuncActivity.this, getResources().getString(R.string.absfuncactivity_message05), ToastOrder.LENGTH_LONG).show();
        } else {
            int waterMarkCustom = currentFunc.getIsImgCustom();
            if (waterMarkCustom == 1) {// 控件需要添加自定义水印内容
                waterMarkDialog(cameraComp, date);
            } else {
                cameraComp.resultPhoto(TableCompView.fileName, date);
                setShowHook();
                if (!isNormal) {// 非正常关闭
                    setShowHook();
                    isNormal = true;
                }
            }
        }

    }

    private void waterMarkDialog(final AbsCameraComp cameraComp, final Date date) {
        final Dialog dialog = new Dialog(this, R.style.transparentDialog);
        View view = View.inflate(this, R.layout.water_mark, null);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        final EditText et = (EditText) view.findViewById(R.id.et_water_mark);
        Button confirmBtn = (Button) view.findViewById(R.id.btn_water_mark_confirmBtn);
        Button cancelBtn = (Button) view.findViewById(R.id.btn_water_mark_cancelBtn);
        confirmBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String waterMark = et.getText().toString();
                cameraComp.setWaterMark(waterMark);
                cameraComp.resultPhoto(fileName, date);
                if (!isNormal) {// 非正常关闭
                    isNormal = true;
                }
                setShowHook();
                dialog.dismiss();
            }
        });
        cancelBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                cameraComp.resultPhoto(fileName, date);
                if (!isNormal) {// 非正常关闭
                    isNormal = true;
                }
                setShowHook();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void subimitResult(int reqId, Intent data) {
        switch (reqId) {
            case R.id.submit_succeeded_superlink:
                this.finish();
                break;
            default:
                if (data != null) {// 双向提交返回
                    boolean isDoubleSubmit = data.getBooleanExtra("doubleSubmit", false);
                    if (isDoubleSubmit) {
                        this.setResult(R.id.submit_succeeded);
                    }
                }
                this.finish();
        }
    }

	/* ****************开始定位******************** */
    /**
     * 定位 和 重新定位
     */
    private LocationForFunc locationForFunc;

    protected void location(boolean isAreaSearchLocation) {
        Bundle lBundle = null;
        if (!isAreaSearchLocation && currentFunc != null && currentFunc.getOrgOption() != null
                && Func.OPTION_LOCATION == currentFunc.getOrgOption()
                && !TextUtils.isEmpty(currentFunc.getLocCheckDistance())
                && Double.parseDouble(currentFunc.getLocCheckDistance()) > 0) {
            lBundle = locationBundle();
        }
        dialog = new MyProgressDialog(this, R.style.CustomProgressDialog,
                getResources().getString(R.string.waitForAMoment));
        locationForFunc = new LocationForFunc(this, this);
        locationForFunc.setLoadDialog(dialog);
        locationForFunc.setAreaSearchLocation(isAreaSearchLocation);// 设置是否是店面距离搜索定位
        locationForFunc.setBundle(lBundle);
        Bundle type = locatType();
        locationForFunc.setBunType(type);
        locationForFunc.startLocation();
    }

    private Bundle locatType() {
        Bundle lBundle = new Bundle();
        lBundle.putString("loc_lev", TextUtils.isEmpty(currentFunc.getLocLevel()) ? "" : currentFunc.getLocLevel());
        return lBundle;
    }

    private Bundle locationBundle() {
        Bundle lBundle = new Bundle();
        SubmitItem storeItem = findStoreSubmitItem();// 店面控件 通过店面控件获取选择的店面
        if (storeItem != null && !TextUtils.isEmpty(storeItem.getParamValue())) {
            OrgStore store = new OrgStoreDB(this).findOrgStoreByStoreId(storeItem.getParamValue());// 根据店面控件选择的值获取选择店面的经纬度
            if (store != null) {
                Double storelon = store.getStoreLon();
                Double storelat = store.getStoreLat();
                lBundle.putString("storelon", storelon == null ? null : String.valueOf(storelon));
                lBundle.putString("storelat", storelat == null ? null : String.valueOf(storelat));
                lBundle.putInt("storeDistance", TextUtils.isEmpty(currentFunc.getLocCheckDistance()) ? 0
                        : Integer.parseInt(currentFunc.getLocCheckDistance()));
            }
        }
        lBundle.putString("is_address",
                TextUtils.isEmpty(currentFunc.getIsAddress()) ? "1" : currentFunc.getIsAddress());// 默认1显示
        lBundle.putString("is_anew_loc",
                TextUtils.isEmpty(currentFunc.getIsNewLoc()) ? "0" : currentFunc.getIsNewLoc());// 默认0不重新定位
        lBundle.putString("loc_type", TextUtils.isEmpty(currentFunc.getLocType()) ? "2" : currentFunc.getLocType());// 默认2选择所有定位方式
        lBundle.putInt("isCheck", 1);

        return lBundle;
    }

    /**
     * 查找当前页面的店面控件选择的值
     *
     * @return
     */
    private SubmitItem findStoreSubmitItem() {
        SubmitItem storeItem = null;
        int submitId = new SubmitDB(this).selectSubmitId(planId, wayId, storeId, targetId, menuType);
        if (linkId != 0) {// 超链接
            storeItem = new SubmitItemTempDB(this).findSubmitItemByNote(submitId, SubmitItem.NOTE_STORE);
        } else {// 非超链接
            storeItem = new SubmitItemDB(this).findSubmitItemByNote(submitId, SubmitItem.NOTE_STORE);
        }
        return storeItem;
    }

    /**
     * 超链接Func
     *
     * @param linkFunc
     */
    public void linkFunc(Func linkFunc) {
        int tempLinkId = 0;
        // 点击超链接的时候设置上级ID
        SubmitDB submitDB = new SubmitDB(this);
        int submitId = new SubmitDB(this).selectSubmitId(planId, wayId, storeId, targetId, menuType);
        if (submitId == Constants.DEFAULTINT) {
            Submit submit = new Submit();
            submit.setPlanId(planId);
            submit.setWayId(wayId);
            submit.setStoreId(storeId);
            submit.setTargetid(targetId);
            submit.setTargetType(menuType);
            submit.setState(Submit.STATE_NO_SUBMIT);
            if (modType != 0) {
                submit.setModType(modType);
            }
            submit.setMenuId(menuId);
            submit.setMenuType(menuType);
            submit.setMenuName(menuName);
            long id = submitDB.insertSubmit(submit);
            tempLinkId = Integer.valueOf(id + "");
        } else {
            tempLinkId = submitId;
        }
        Bundle linkBundle = new Bundle();
        if (linkFuncId != Constants.DEFAULTINT) {// 历史数据时,多层link时用到,否则修改时没有数据
            linkBundle.putInt("funcId", linkFuncId);
            linkBundle.putInt("storeId", storeId);// 历史数据 修改要把storeId传给后台
        }
        // 如果是访店里的超链接把店面ID和是访店标识传过去
        if (menuType == com.yunhu.yhshxc.bo.Menu.TYPE_VISIT) {
            linkBundle.putInt("sqlStoreId", storeId);
            linkBundle.putBoolean("isVisit", true);
        }

        linkBundle.putInt("targetId", linkFunc.getMenuId());
        linkBundle.putInt("linkId", tempLinkId); // 下级连接用来查找上级submitid,以便保存patchId
        linkBundle.putInt("taskId", taskId);// 双向模块的时候要传到服务端
        linkBundle.putInt("linkKey", linkFunc.getFuncId());// 当前func的id提交的时候存储超链接的值使用
        linkBundle.putInt("menuType", com.yunhu.yhshxc.bo.Menu.TYPE_MODULE);
        linkBundle.putStringArrayList("map", orgMapValueList);// 组织机构关系
        linkBundle.putBoolean("isSqlLink", isSqlLink);// sql超链接

        // 超链接预览编辑跳转到上报页面的时候把机构关系传过去
        setOrgRelation(submitId, linkFunc, linkBundle);

        intoLink(linkBundle);
    }

    /**
     * 跳转到超链接页面
     *
     * @param linkBundle 携带数据的bundle
     */
    protected abstract void intoLink(Bundle linkBundle);

    /**
     * 创建一个FUNC VIEW
     */


    private void setName(FuncLayoutMenu funcLayoutMenu, Func func) {
        if (null != func) {
            switch (func.getFuncId()) {
                case -1:
                    funcLayoutMenu.setFuncName(getResources().getString(R.string.location_title));
                    break;
                case -2:
                    funcLayoutMenu.setFuncName(getResources().getString(R.string.take_photo));
                    break;
                case -3:
                    funcLayoutMenu.setFuncName(getResources().getString(R.string.description));
                    break;
                case -5:
                    funcLayoutMenu.setFuncName(getResources().getString(R.string.func_name));
                    break;
                case -6:
                    funcLayoutMenu.setFuncName(getResources().getString(R.string.question_info9));
                    break;

                default:
                    funcLayoutMenu.setFuncName(func.getName());
            }
        }
    }

    private View getFuncView(int funcIndex) {
        if (funcIndex == -1) {
            View view = new FuncLayoutMenu(this, null, false).getView();
            view.setVisibility(View.INVISIBLE);
            return view;
        } else {
            Func func = this.funcList.get(funcIndex);
            FuncLayoutMenu funcLayoutMenu = new FuncLayoutMenu(this, func, false);
            funcLayoutMenu.setBackgroundResource(R.drawable.func_menu_onclick);
            funcLayoutMenu.setIcon(setIconForTypeNew(func.getType())); // 设置显示的图标
            if (setIconForName(func.getName()) != 0){
                funcLayoutMenu.setIcon(setIconForName(func.getName())); // 设置显示的图标
            }
            setName(funcLayoutMenu, func);// 设置名字
            funcLayoutMenu.setVisibilityForCheckMark(false); // 初始设置标记不显示
            View view = funcLayoutMenu.getView().findViewById(R.id.ll_func_menu);
            if (func.getType() == Func.TYPE_EDITCOMP) {
                if (AbsFuncActivity.this instanceof QueryFuncActivity
                        && (func.getDataType() == Func.DATATYPE_BIG_INTEGER
                        || func.getDataType() == Func.DATATYPE_DECIMAL
                        || func.getDataType() == Func.DATATYPE_SMALL_INTEGER)) {// 如果是查询条件就弹出有范围的输入框
                } else {
                    boolean isReplenish = false;// 是否是查询 true是 false不是
                    Bundle replenish = null;// 查询条件

                    isReplenish = AbsFuncActivity.this instanceof QueryFuncActivity;// 是否是查询
                    if (isReplenish) {// 如果是查询的情况
                        QueryFuncActivity queryFuncActivity = (QueryFuncActivity) this;
                        replenish = queryFuncActivity.replenish;
                    }
                    RelativeLayout iv_func_photo_container = (RelativeLayout) view
                            .findViewById(R.id.iv_func_photo_container);
                    iv_func_photo_container.setVisibility(View.VISIBLE);
                    TextView tv_func_content = (TextView) view.findViewById(R.id.tv_func_content);
                    tv_func_content.setVisibility(View.INVISIBLE);
                    EditCompFunc editComp = new EditCompFunc(AbsFuncActivity.this, func, bundle, contrlEnterList);
                    editComp.setMenuId(menuId);
                    editComp.setMenuType(menuType);
                    editComp.setMenuName(menuName);
                    editComp.isLink = isLinkActivity;
                    editComp.setReplenishBundle(replenish);
                    editComp.setTargetType(menuType);
                    // editComp.setDefaultSql(defaultSql);
//					final EditText et = editComp.getEditText();
//					et.setOnClickListener(new OnClickListener() {
//
//						@Override
//						public void onClick(View v) {
//							// TODO Auto-generated method stub
//
//							et.setSelection(et.getText().toString().length());\
//						}
//					});
                    View view1 = editComp.getObject();
                    if(func.getFuncId()==txrId){
                        EditText et = (EditText) view1.findViewById(R.id.textEditTextComp);
                        et.setVisibility(View.GONE);
                    }
                    iv_func_photo_container.addView(view1);

                }
            }

            view.setId(func.getType());// 给VIEW设置ID
            view.setOnClickListener(this);
            view.setTag(funcIndex);
            // if (func.getIsCacheFun() == 1) {// 缓存控件
            // FuncCacheDB fcDb = new FuncCacheDB(this);
            // FuncCache fcCache = fcDb.findFunCacheByFuncId(func.getFuncId() +
            // "", func.getTargetid() + "");
            // String textV = fcCache.getParamValue();
            // if (TextUtils.isEmpty(textV)) {
            // textV = fcCache.getParamId();
            // }
            // // view
            // if (!TextUtils.isEmpty(fcCache.getFuncId())) {
            // save(func, view, textV, fcCache);
            // }
            //
            // }
            // View.xu 在非修改页面下进行缓存数据的填充
            if (!(this instanceof ChangeModuleFuncActivity) && isNormal) {

                if (func.getIsCacheFun() == 1) {// 缓存控件
                    try {
                        FuncCacheDB fcDb = new FuncCacheDB(this);
                        FuncCache fcCache = fcDb.findFunCacheByFuncId(func.getFuncId() + "", func.getTargetid() + "");
                        if (fcCache != null) {
                            String textV = fcCache.getParamValue();
                            if (TextUtils.isEmpty(textV)) {
                                textV = fcCache.getParamId();
                            }
                            // view 如果funcid不为0并且不是单选其他类型
                            int funtype = func.getType();
                            if (!TextUtils.isEmpty(fcCache.getFuncId()) && funtype != Func.TYPE_SELECT_OTHER) {
                                save(func, view, textV, fcCache);
                            } else {
                                if (fcCache.getParamValue().equals(fcCache.getParamId())) {// 说明是自己输入的,而非选择已有id的列表
                                    // 创建两条submititem插入提交表
                                    SubmitDB submitDb1 = new SubmitDB(this);
                                    SubmitItemDB itemDb1 = new SubmitItemDB(this);
                                    int submitId1 = submitDb1.selectSubmitIdNotCheckOut(planId, wayId, storeId,
                                            func.getTargetid(), menuType, Submit.STATE_NO_SUBMIT);
                                    // 创建第一条
                                    SubmitItem item1 = new SubmitItem();
                                    item1.setSubmitId(submitId1);
                                    item1.setParamName(fcCache.getFuncId());
                                    item1.setParamValue("-1");
                                    item1.setType(func.getType());

                                    // 创建第二条
                                    SubmitItem item2 = new SubmitItem();
                                    item2.setSubmitId(submitId1);
                                    item2.setParamName(fcCache.getFuncId() + "_other");
                                    item2.setParamValue(fcCache.getParamId());
                                    item2.setType(func.getType());
                                    item2.setOrderId(func.getId());
                                    if (isLinkActivity) {
                                        SubmitItemTempDB tempDb = new SubmitItemTempDB(this);
                                        tempDb.insertSubmitItemOrDelete(item1);
                                        tempDb.insertSubmitItemOrDelete(item2);
                                    } else {
                                        itemDb1.insertSubmitItemOrDelete(item1);
                                        itemDb1.insertSubmitItemOrDelete(item2);
                                    }
                                    // 除了插入到提交数据库中还需要在条目中进行展示
                                    TextView tv1 = (TextView) view.findViewById(R.id.tv_func_content);
                                    tv1.setText(fcCache.getParamValue());
                                } else {
                                    save(func, view, textV, fcCache);
                                }
                            }
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

            }
            unOperateMap.put(func.getFuncId(), view);
            initFuncChangeDisable(func, funcLayoutMenu);
            initInputOrgMap(func);
            return funcLayoutMenu.getView();
        }
    }

    /**
     * 根据类型获取小图标背景图片
     *
     * @param type 类型
     * @return 图片的ID
     */
    private int setIconForTypeNew(int type) {
        switch (type) {
            case Func.TYPE_CAMERA:// 拍照
            case Func.TYPE_CAMERA_HEIGHT:// 拍照
            case Func.TYPE_CAMERA_MIDDLE:// 拍照
            case Func.TYPE_CAMERA_CUSTOM:
                return R.drawable.icon_take_photo;
            case Func.TYPE_LOCATION:// 定位
                return R.drawable.icon_unupload;
            case Func.TYPE_EDITCOMP:// 输入框
                return R.drawable.f_default;
            case Func.TYPE_DATEPICKERCOMP:// 日期
                return R.drawable.f_date;
            case Func.TYPE_TABLECOMP:// 表格
                return R.drawable.icon_order;
            case Func.TYPE_LINK:// 超链接
                return R.drawable.f_link;
            case Func.TYPE_SCAN:// 扫描
            case Func.TYPE_SCAN_INPUT:// 可输入类型的扫描
                return R.drawable.icon_scan;
            default:
                break;
        }
        return R.drawable.icon_upload;
    }

    /**
     * 根据funcName获取小图标背景图片
     * @return 图片的ID
     */
    private int setIconForName(String funcName) {
        if (funcName.equals("申请人")){
            return R.drawable.asset_shenqingren;
        }else if(funcName.equals("使用人")||funcName.equals("借用人")||funcName.equals("借用处理人")||funcName.equals("归还处理人")||funcName.equals("归还人")||funcName.equals("调出管理员")||funcName.equals("调出处理人")){
            return R.drawable.asset_shiyongren;
        }else if(funcName.equals("领用时间")||funcName.equals("退库时间")||funcName.equals("借用日期")||funcName.equals("预计归还时间")||funcName.equals("申请归还时间")||funcName.equals("调出日期")){
            return R.drawable.asset_shijian;
        }else if(funcName.equals("领用后使用公司")||funcName.equals("退库所属公司")){
            return R.drawable.asset_shiyonggongsi;
        }else if(funcName.equals("领用后使用部门")){
            return R.drawable.asset_shiyongbumen;
        }else if(funcName.equals("领用后管理人")){
            return R.drawable.asset_shiyongren;
        }else if(funcName.equals("领用后使用区域")||funcName.equals("退库后区域")){
            return R.drawable.asset_shiyongquyu;
        }else if(funcName.equals("领用后存放地点")){
            return R.drawable.asset_shiyongdidian;
        }else if(funcName.equals("领用处理人")||funcName.equals("退库处理人")){
            return R.drawable.asset_shiyongren;
        }else if(funcName.equals("备注")){
            return R.drawable.asset_beizhu;
        }else if(funcName.equals("资产条码")||funcName.equals("选择调拨资产条码")){
            return R.drawable.icon_scan;
        }else if(funcName.equals("预计退库时间")){
            return R.drawable.asset_shijian;
        }else if(funcName.equals("调拨说明")){
            return R.drawable.f_default;
        }
        return 0;
    }
    // /**
    // * 根据类型获取小图标背景图片
    // *
    // * @param type
    // * 类型
    // * @return 图片的ID
    // */
    // private int setIconForTypeNew(int type) {
    // switch (type) {
    // case Func.TYPE_CAMERA:// 拍照
    // case Func.TYPE_CAMERA_HEIGHT:// 拍照
    // case Func.TYPE_CAMERA_MIDDLE:// 拍照
    // case Func.TYPE_CAMERA_CUSTOM:
    // return R.drawable.f_photo;
    // case Func.TYPE_LOCATION:// 定位
    // return R.drawable.f_location;
    // case Func.TYPE_EDITCOMP:// 输入框
    // return R.drawable.f_default;
    // case Func.TYPE_DATEPICKERCOMP:// 日期
    // return R.drawable.f_date;
    // case Func.TYPE_TABLECOMP:// 表格
    // return R.drawable.f_table;
    // case Func.TYPE_LINK:// 超链接
    // return R.drawable.f_link;
    // case Func.TYPE_SCAN:// 扫描
    // case Func.TYPE_SCAN_INPUT:// 可输入类型的扫描
    // return R.drawable.f_scanf;
    // default:
    // break;
    // }
    // return R.drawable.f_default;
    // }

    /**
     * 根据类型获取小图标背景图片
     *
     * @param type 类型
     * @return 图片的ID
     */
    private int setIconForTypeNewUnable(int type) {
        switch (type) {
            case Func.TYPE_CAMERA:// 拍照
            case Func.TYPE_CAMERA_HEIGHT:// 拍照
            case Func.TYPE_CAMERA_MIDDLE:// 拍照
            case Func.TYPE_CAMERA_CUSTOM:
                return R.drawable.f_photo_unable;
            case Func.TYPE_LOCATION:// 定位
                return R.drawable.f_location_unable;
            case Func.TYPE_EDITCOMP:// 输入框
                return R.drawable.f_default_unable;
            case Func.TYPE_DATEPICKERCOMP:// 日期
                return R.drawable.f_date_unable;
            case Func.TYPE_TABLECOMP:// 表格
                return R.drawable.f_table_unable;
            case Func.TYPE_LINK:// 超链接
                return R.drawable.f_link_unable;
            case Func.TYPE_SCAN:// 扫描
            case Func.TYPE_SCAN_INPUT:// 扫描
                return R.drawable.f_scanf_unable;
            default:
                break;
        }
        return R.drawable.f_default_unable;
    }


    /**
     * 初始化按输入顺序控件
     *
     * @param func
     */
    public void initFuncChangeDisable(Func func, FuncLayoutMenu funcLayoutMenu) {
        View view = unOperateMap.get(func.getFuncId());
        if (!isCheckIn && this instanceof VisitFuncActivity) {
            changeGrag(view, func);
            // showDataLinearLayout
            // .setBackgroundResource(R.color.func_menu_unenable);
        } else {
            if (inputOrderArr != null && func.getInputOrder() != null
                    && func.getInputOrder().intValue() != inputOrderArr[0]) {
                changeGrag(view, func);
                view.setEnabled(false);

            }
        }
    }

    /**
     * 将view设置为灰色
     *
     * @param view 要操作的view
     */
    private void changeGrag1(View view) {
        view.setBackgroundResource(R.color.func_menu_unenable);
        view.setEnabled(false);


        RelativeLayout iv_func_photo_container = (RelativeLayout) view.findViewById(R.id.iv_func_photo_container);
        if (null != iv_func_photo_container) {
            EditText textEditTextComp = (EditText) iv_func_photo_container.findViewById(R.id.textEditTextComp);
            if (null != textEditTextComp)
                textEditTextComp.setEnabled(false);
        }


    }

    /**
     * 将该view设置为可用
     *
     * @param view 要操作的view
     * @param
     */
    private void changeBlue1(View view) {
        view.setBackgroundResource(R.drawable.func_menu_onclick);
        view.setEnabled(true);

        RelativeLayout iv_func_photo_container = (RelativeLayout) view.findViewById(R.id.iv_func_photo_container);
        if (null != iv_func_photo_container) {
            EditText textEditTextComp = (EditText) iv_func_photo_container.findViewById(R.id.textEditTextComp);
            if (null != textEditTextComp)
                textEditTextComp.setEnabled(true);
        }
    }

    /**
     * 将view设置为灰色
     *
     * @param view 要操作的view
     */
    private void changeGrag(View view, Func func) {
        view.setBackgroundResource(R.color.func_menu_unenable);
        ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_func_icon);
        iv_icon.setBackgroundResource(setIconForTypeNewUnable(func.getType()));
        if (setIconForName(func.getName()) != 0){
            iv_icon.setBackgroundResource(setIconForName(func.getName())); // 设置显示的图标
        }
        if (func.getType() == Func.TYPE_EDITCOMP) {
            if (AbsFuncActivity.this instanceof QueryFuncActivity
                    && (func.getDataType() == Func.DATATYPE_BIG_INTEGER || func.getDataType() == Func.DATATYPE_DECIMAL
                    || func.getDataType() == Func.DATATYPE_SMALL_INTEGER)) {// 如果是查询条件就弹出有范围的输入框
            } else {
                RelativeLayout iv_func_photo_container = (RelativeLayout) view
                        .findViewById(R.id.iv_func_photo_container);
                EditText textEditTextComp = (EditText) iv_func_photo_container.findViewById(R.id.textEditTextComp);

                textEditTextComp.setEnabled(false);
            }
        }
    }

    /**
     * 将该view设置为可用
     *
     * @param view 要操作的view
     * @param func 要操作view所在的func
     */
    private void changeBlue(View view, Func func) {
        view.setBackgroundResource(R.drawable.func_menu_onclick);
        view.setEnabled(true);
        ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_func_icon);
        iv_icon.setBackgroundResource(setIconForTypeNew(func.getType()));
        if (setIconForName(func.getName()) != 0){
            iv_icon.setBackgroundResource(setIconForName(func.getName())); // 设置显示的图标
        }
        if (func.getType() == Func.TYPE_EDITCOMP) {
            if (AbsFuncActivity.this instanceof QueryFuncActivity
                    && (func.getDataType() == Func.DATATYPE_BIG_INTEGER || func.getDataType() == Func.DATATYPE_DECIMAL
                    || func.getDataType() == Func.DATATYPE_SMALL_INTEGER)) {// 如果是查询条件就弹出有范围的输入框
            } else {
                RelativeLayout iv_func_photo_container = (RelativeLayout) view

                        .findViewById(R.id.iv_func_photo_container);
                EditText textEditTextComp = (EditText) iv_func_photo_container.findViewById(R.id.textEditTextComp);
                textEditTextComp.setEnabled(true);
            }
        }
    }

    /**
     * 有顺序的输入，控制下级可输入
     *
     * @param func 当前操作的控件
     */
    public void nextFuncChangeDisableByInputOrder(Func func) {
        if (inputOrderArr != null && func.getInputOrder() != null
                && func.getInputOrder().intValue() != inputOrderArr[inputOrderArr.length - 1]) {
            Integer nextOrder = null;
            for (int i = 0; i < inputOrderArr.length; i++) {// 获取当前层级的值，计算下级层级的值
                if (func.getInputOrder().intValue() == inputOrderArr[i]) {
                    nextOrder = inputOrderArr[i + 1];
                    break;
                }
            }
            // 查找下级层级的所有控件
            ArrayList<Func> nextFuncList = getOrderFuncList(null,
                    nextOrder == null ? (func.getInputOrder() + 1) : nextOrder);
            for (Func f : nextFuncList) {// 设置控件可操作
                if (f.getFuncId() != null && unOperateMap.containsKey(f.getFuncId())) {
                    changeBlue(unOperateMap.get(f.getFuncId()), f);
                }
            }

        }
    }

    /**
     * 将全部控件置为可点击
     */
    protected void funcChangeEnable() {
        for (Map.Entry<Integer, View> m : unOperateMap.entrySet()) {
            View view = m.getValue();
            Integer index = (Integer) view.getTag();
            Func func = this.funcList.get(index);
            if (func.getInputOrder() == null || (func.getInputOrder() != null && inputOrderArr != null
                    && (func.getInputOrder().intValue() == inputOrderArr[0]))) {
                // View funcView = view.findViewById(R.id.ll_func_menu);
                // funcView.setBackgroundResource(R.color.Cambridgeblue);
                view.setBackgroundResource(R.drawable.func_menu_onclick);

                changeBlue(view, func);
            } else {
                view.setEnabled(false);
                if (func.getType() == Func.TYPE_EDITCOMP) {
                    if (AbsFuncActivity.this instanceof QueryFuncActivity
                            && (func.getDataType() == Func.DATATYPE_BIG_INTEGER
                            || func.getDataType() == Func.DATATYPE_DECIMAL
                            || func.getDataType() == Func.DATATYPE_SMALL_INTEGER)) {// 如果是查询条件就弹出有范围的输入框
                    } else {
                        RelativeLayout iv_func_photo_container = (RelativeLayout) view
                                .findViewById(R.id.iv_func_photo_container);
                        EditText textEditTextComp = (EditText) iv_func_photo_container
                                .findViewById(R.id.textEditTextComp);
                        textEditTextComp.setEnabled(false);
                    }
                }
            }

        }

        for (Map.Entry<Integer, View> m : operatedMap.entrySet()) {
            View view = m.getValue();
            Integer index = (Integer) view.getTag();
            Func func = this.funcList.get(index);
            if (func.getInputOrder() == null || (func.getInputOrder() != null && inputOrderArr != null
                    && (func.getInputOrder().intValue() == inputOrderArr[0]))) {
                // View funcView = view.findViewById(R.id.ll_func_menu);
                // funcView.setBackgroundResource(R.color.Cambridgeblue);
                view.setBackgroundResource(R.drawable.func_menu_onclick);

                changeBlue(view, func);
            } else {
                view.setEnabled(false);
                if (func.getType() == Func.TYPE_EDITCOMP) {
                    if (AbsFuncActivity.this instanceof QueryFuncActivity
                            && (func.getDataType() == Func.DATATYPE_BIG_INTEGER
                            || func.getDataType() == Func.DATATYPE_DECIMAL
                            || func.getDataType() == Func.DATATYPE_SMALL_INTEGER)) {// 如果是查询条件就弹出有范围的输入框
                    } else {
                        RelativeLayout iv_func_photo_container = (RelativeLayout) view
                                .findViewById(R.id.iv_func_photo_container);
                        EditText textEditTextComp = (EditText) iv_func_photo_container
                                .findViewById(R.id.textEditTextComp);
                        textEditTextComp.setEnabled(false);
                    }
                }
            }

        }
    }

    /**
     * 初始化组织机构层级
     *
     * @param func 当前要操作的控件
     */
    private void initInputOrgMap(Func func) {
        if (isNormal) {
            if (func.getOrgOption() != null && func.getOrgOption() == Func.ORG_OPTION) {
                orgMap.put(func.getOrgLevel(), null);// 层级作为key值
                JLog.d(TAG, "Level==>" + func.getOrgLevel());
                if (!orgMap.containsKey(-10000)) {// 用-10000作为记录最高层级的值
                    orgMap.put(-10000, String.valueOf(func.getOrgLevel()));
                } else {
                    if (Integer.valueOf(orgMap.get(-10000)) < func.getOrgLevel()) {
                        orgMap.put(-10000, func.getOrgLevel() + "");
                    }
                }
            }
        }
    }

    /**
     * 修改的时候外部级联Map 条件 维护
     *
     * @param func 当前操作控件
     * @param did  当前操作控件所选择的值
     */
    public void initInputChooseMap(Func func, String did) {

        if (func.getNextDropdown() != null && func.getNextDropdown() != 0) {// 有下级级联的时候
            // 当此控件有下一级的时候，为下级控件级联做准备
            // 下级控件id（getNextDropdown）做key，本控件的级联值+本控件的所选值做value
            if (chooseMap.get(func.getFuncId()) != null) {
                saveChooseMap(func.getNextDropdown(), chooseMap.get(func.getFuncId()) + "@" + did);// 如果选择为其他，则加上的为-1
            } else {
                saveChooseMap(func.getNextDropdown(), did);
            }
        }
    }

    /**
     * 保存级联关系
     *
     * @param curNextFuncId 下级控件的ID
     * @param value         当前层级的值
     */
    public void saveChooseMap(Integer curNextFuncId, String value) {
        chooseMap.put(curNextFuncId, value);
        Func funcNext = getFuncByFuncId(curNextFuncId);
        if (funcNext != null && funcNext.getNextDropdown() != null && funcNext.getNextDropdown() != 0) {
            saveChooseMap(funcNext.getNextDropdown(), value);
        }
    }

    /**
     * 添加操作的VIEW删除未操作的VIEW
     *
     * @param func 要操作的view所在的func
     */
    public void inputOperatedMap(Func func) {
        if (unOperateMap.containsKey(func.getFuncId())) {
            operatedMap.put(func.getFuncId(), unOperateMap.get(func.getFuncId()));
            unOperateMap.remove(func.getFuncId());
            nextFuncChangeDisableByInputOrder(func);
        }
    }

    /**
     * 添加未操作的VIEW删除操作的VIEW
     *
     * @param
     */
    public void inputUnOperatedMap(Func func) {
        if (operatedMap.containsKey(func.getFuncId())) {
            unOperateMap.put(func.getFuncId(), operatedMap.get(func.getFuncId()));
            operatedMap.remove(func.getFuncId());
            nextFuncChangeDisableByInputOrder(func);
        }
    }

    /**
     * 整个布局中所用到的控件
     *
     * @return
     */
    public abstract List<Func> getFuncList();

    /**
     * 布局中所定义的button的控件
     *
     * @return
     */
    public abstract List<Func> getButtonFuncList();

    /**
     * 获取当前funcId的对象
     *
     * @param funcId 要查找的控件的Id
     * @return
     */
    public abstract Func getFuncByFuncId(int funcId);

    /**
     * 获取当前输入顺序
     *
     * @return 输入顺序的数组
     */
    public abstract Integer[] findFuncListByInputOrder();

    /**
     * 按输入顺序获取对象
     *
     * @param funcId     控件ID
     * @param inputOrder 输入顺序
     * @return 查找到的控件集合
     */
    public abstract ArrayList<Func> getOrderFuncList(Integer funcId, int inputOrder);

    /**
     * 显示采集信息
     */
    public abstract void showPreview();

    /**
     * 点击返回对话框的确定按钮时，所做的处理
     */
    protected abstract void onClickBackBtn();

    /**
     * @return 组织机构机构关系map
     */
    public Map<Integer, String> getOrgMap() {
        return orgMap;
    }

    /**
     * 设置组织结构关系map
     *
     * @param orgMap
     */
    public void setOrgMap(Map<Integer, String> orgMap) {
        this.orgMap = orgMap;
    }

    /**
     * 保存位置信息
     *
     * @param func 当前控件
     */
    protected void saveLocationContent(LocationResult locationResult, Func func) {
        setAttenTime();
        StringBuffer sb = new StringBuffer();
        if (locationResult != null) {
            String address = locationResult.getAddress();
            if (this.getResources().getString(R.string.location_success_no_addr).equals(address)
                    || PublicUtils.checkAddress(address)) {
                locationResult.setAddress("");
            }
            // 拼接定位内容
            sb.append(locationResult.getAddress()).append("$").append(locationResult.getLongitude()).append("$")
                    .append(locationResult.getLatitude()).append("$").append(DateUtil.getCurDateTime()).append("$")
                    .append(locationResult.getLocType()).append("$").append(locationResult.getRadius()).append("$")
                    .append(locationResult.getPosType()).append("$").append(LocationMaster.ACTION).append("$")
                    .append(LocationMaster.VERSION);
        }

        SubmitDB submitDB = new SubmitDB(this);
        SubmitItemDB submitItemDB = new SubmitItemDB(this);
        SubmitItem submitItem = new SubmitItem();
        submitItem.setTargetId(func.getTargetid() + "");
        int submitId = submitDB.selectSubmitIdNotCheckOut(planId, wayId, storeId, targetId, menuType,
                Submit.STATE_NO_SUBMIT);
        if (submitId != Constants.DEFAULTINT) {// 存入数据库
            submitItem.setParamName(String.valueOf(func.getFuncId()));
            submitItem.setParamValue(sb.toString());
            submitItem.setSubmitId(submitId);
            submitItem.setType(Func.TYPE_LOCATION);
            submitItem.setIsCacheFun(currentFunc.getIsCacheFun());
            if (func.getOrgOption() != null && func.getOrgOption() == Func.OPTION_LOCATION) {
                SubmitItem storeItem = findStoreSubmitItem();
                if (storeItem != null && !TextUtils.isEmpty(storeItem.getParamValue())) {
                    submitItem.setNote(storeItem.getParamName());
                }
            }
            SubmitItemTempDB submitItemTempDB = new SubmitItemTempDB(this);
            boolean isHas = false;
            if (linkId != 0) {// 超链接模块里
                isHas = submitItemTempDB.findIsHaveParamName(submitItem.getParamName(), submitId);
            } else {
                isHas = submitItemDB.findIsHaveParamName(submitItem.getParamName(), submitId);
            }

            if (isHas) {// 如果有就更新
                if (linkId != 0) {// 超链接更新临时表
                    submitItemTempDB.updateSubmitItem(submitItem);
                } else {// 非超链接更新提交表
                    if (this instanceof ChangeModuleFuncActivity) {

                        submitItemDB.updateSubmitItem(submitItem, false);
                    } else {
                        submitItemDB.updateSubmitItem(submitItem, true);

                    }
                }
            } else {// 没有就插入
                if (linkId != 0) {// 超链接插入临时表
                    submitItemTempDB.insertSubmitItem(submitItem);
                } else {// 非超链接插入提交表
                    // submitItem.setMenuId(menuId);
                    if (this instanceof ChangeModuleFuncActivity) {

                        submitItemDB.insertSubmitItem(submitItem, false);
                    } else {
                        submitItemDB.insertSubmitItem(submitItem, true);

                    }
                }
            }

        } else {
            Submit submit = new Submit();
            submit.setPlanId(planId);
            submit.setState(Submit.STATE_NO_SUBMIT);
            submit.setStoreId(storeId);
            submit.setWayId(wayId);
            submit.setTargetid(targetId);
            submit.setTargetType(menuType);
            if (modType != 0) {
                submit.setModType(modType);
            }
            submit.setMenuId(menuId);
            submit.setMenuType(menuType);
            submit.setMenuName(menuName);
            long submit_Id = submitDB.insertSubmit(submit);
            submitItem.setParamName(String.valueOf(func.getFuncId()));
            submitItem.setType(Func.TYPE_LOCATION);
            submitItem.setParamValue(sb.toString());
            submitItem.setSubmitId(Integer.valueOf(submit_Id + ""));
            submitItem.setIsCacheFun(currentFunc.getIsCacheFun());
            if (func.getOrgOption() != null && func.getOrgOption() == Func.OPTION_LOCATION) {
                SubmitItem storeItem = findStoreSubmitItem();
                if (storeItem != null && !TextUtils.isEmpty(storeItem.getParamValue())) {
                    submitItem.setNote(storeItem.getParamName());
                }
            }
            if (linkId != 0) {
                new SubmitItemTempDB(this).insertSubmitItem(submitItem);
            } else {
                // submitItem.setMenuId(menuId);
                if (this instanceof ChangeModuleFuncActivity) {
                    submitItemDB.insertSubmitItem(submitItem, false);

                } else {

                    submitItemDB.insertSubmitItem(submitItem, true);
                }
            }
        }

        inputOperatedMap(func);
        setShowHook(false, hookView);

        // TView.setText(sb.toString());
        if (locationResult != null) {
            TView.setText(locationResult.getAddress());
        }
        // Toast.makeText(AbsFuncActivity.this, "test0"+sb.toString(),
        // Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK: // 点击返回按钮
                if (this instanceof QueryFuncActivity) {// 查询条件的时候直接返回
                    onClickBackBtn();
                } else {
                    theOperatedFinish();
                    onClickKeyCodeBack();
                }
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 单击返回按钮弹出提示对话框
     */
    public void onClickKeyCodeBack() {
        if (linkId != 0 || this instanceof Order3FuncActivity) {// 超链接和订单3
            new DialogInFuncManager(AbsFuncActivity.this).backDialog(true);
        } else {
            new DialogInFuncManager(AbsFuncActivity.this).backDialog(false);
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        theOperatedFinish();
        locationFactory.stopLocation();
        if (bundle != null && bundle.containsKey("isNetCanUse")) {
            bundle.remove("isNetCanUse");
        }
    }

    /**
     * 关闭dialog
     */
    private void theOperatedFinish() {
        isCanClick = true; // 恢复点击
        if (historySearchPopupWindow != null) {
            historySearchPopupWindow.close();
        }
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    /*
	 * 保存隐藏FUNC数据
	 */
    protected void saveHideFunc(Func func, int submitId) throws NumberFormatException, DataComputeException {

        SubmitDB submitDB = new SubmitDB(this);
        Submit submit = new Submit();
        SubmitItemDB submitItemDB = new SubmitItemDB(this);
        SubmitItemTempDB submitItemTempDB = new SubmitItemTempDB(this);
        SubmitItem submitItem = new SubmitItem();
        submitItem.setTargetId(func.getTargetid() + "");
        if (submitId == Constants.DEFAULTINT) {// 如果没有submit就先插入一条
            submit.setPlanId(planId);
            submit.setState(Submit.STATE_NO_SUBMIT);
            submit.setStoreId(storeId);
            submit.setWayId(wayId);
            submit.setTargetid(targetId);
            submit.setTargetType(menuType);
            if (modType != 0) {
                submit.setModType(modType);
            }
            submit.setMenuId(menuId);
            submit.setMenuType(menuType);
            submit.setMenuName(menuName);
            submitId = submitDB.insertSubmit(submit).intValue();
        }

        if (func.getDefaultType() != null && func.getDefaultType() != Func.DEFAULT_TYPE_SELECT) {// 不是隐藏域下拉框
            String defauleValue = null;
            if (func.getDefaultType() == Func.DEFAULT_TYPE_COMPUTER) { // 计算类型的隐藏域
                if (linkId != 0) {// 超链接从临时表中查找数据
                    defauleValue = new FuncTreeForLink(this, func, submitId).result;
                } else if (menuType == com.yunhu.yhshxc.bo.Menu.TYPE_VISIT) {
                    defauleValue = new FuncTreeForVisit(this, func, submitId).result;
                } else {
                    defauleValue = new FuncTree(this, func, submitId).result;
                }
            } else {
                defauleValue = getDefaultVaiueByType(func);// 获取隐藏域默认值类型
            }

            if (!TextUtils.isEmpty(defauleValue)) {
                submitItem.setSubmitId(submitId);
                submitItem.setParamName(String.valueOf(func.getFuncId()));
                submitItem.setType(func.getType());
                submitItem.setParamValue(defauleValue);
                if (currentFunc != null) {
                    submitItem.setIsCacheFun(currentFunc.getIsCacheFun());
                }
                boolean isHas = false;
                if (linkId != 0) {
                    isHas = submitItemTempDB.findIsHaveParamName(submitItem.getParamName(), submitId);
                } else {
                    isHas = submitItemDB.findIsHaveParamName(submitItem.getParamName(), submitId);
                }
                if (isHas) {// true表示已经操作过，此时更新数据库
                    if (linkId != 0) {
                        submitItemTempDB.updateSubmitItem(submitItem);
                    } else {
                        if (this instanceof ChangeModuleFuncActivity) {

                            submitItemDB.updateSubmitItem(submitItem, false);
                        } else {
                            submitItemDB.updateSubmitItem(submitItem, true);

                        }
                    }
                } else {
                    if (linkId != 0) {
                        submitItemTempDB.insertSubmitItem(submitItem);
                    } else {
                        // submitItem.setMenuId(menuId);
                        if (this instanceof ChangeModuleFuncActivity) {
                            submitItemDB.insertSubmitItem(submitItem, false);

                        } else {

                            submitItemDB.insertSubmitItem(submitItem, true);
                        }
                    }
                }
            }
        }

    }

    /**
     * 根据传入的默认类型设置默认值
     */
    public String getDefaultVaiueByType(Func func) {
        if (func.getDefaultType() != null) {
            Calendar c = Calendar.getInstance();
            switch (func.getDefaultType()) {
                case Func.DEFAULT_TYPE_OTHER:// 取默认值
                    return func.getDefaultValue();
                case Func.DEFAULT_TYPE_YEAR:// 返回当前年
                    return String.valueOf(c.get(Calendar.YEAR));
                case Func.DEFAULT_TYPE_MONTH:// 返回当前月
                    return String.valueOf((c.get(Calendar.MONTH) + 1) + 100).substring(1);
                case Func.DEFAULT_TYPE_DAY:// 返回当前日
                    return String.valueOf(c.get(Calendar.DAY_OF_MONTH) + 100).substring(1);
                case Func.DEFAULT_TYPE_DATE:// 返回当前日期包含时间
                    return DateUtil.getCurDateTime();
                case Func.DEFAULT_TYPE_DATE_NO_TIME:// 返回当前日期不包含时间
                    return DateUtil.getCurDate();
                case Func.DEFAULT_TYPE_TIME:// 返回当前时间
                    return String.valueOf(c.get(Calendar.HOUR_OF_DAY) + 100).substring(1) + ":"
                            + String.valueOf(c.get(Calendar.MINUTE) + 100).substring(1) + ":"
                            + String.valueOf(c.get(Calendar.SECOND) + 100).substring(1);
                case Func.DEFAULT_TYPE_HOURS:// 返回当前小时
                    return String.valueOf(c.get(Calendar.HOUR_OF_DAY) + 100).substring(1);
                case Func.DEFAULT_TYPE_MINUTE:// 返回当前分钟
                    return String.valueOf(c.get(Calendar.MINUTE) + 100).substring(1);
                case Func.DEFAULT_TYPE_USER:// 返回当前用户
                    return SharedPreferencesUtil.getInstance(this).getUserName();// name修改成ID
                // 代要求09_21号
                case Func.DEFAULT_TYPE_JOB_NUMBER:// 唯一工号
                    return String.valueOf(System.currentTimeMillis());// 返回时间戳
                case Func.DEFAULT_TYPE_STARTDATE:// 开始时间
                    return startTime;// 返回开始时间
                case Func.DEFAULT_TYPE_BASE_TIEM:// 基于时间的隐藏域 核算日期
                    return DateUtil.getCurDate();// 基于时间的隐藏域 核算日期
            }
        }
        return "";
    }

    /**
     * 删除未提交的数据
     *
     * @param targetId 当前的数据ID
     */
    protected void cleanCurrentNoSubmit(Integer targetId) {
        if (!isNormal) {// 异常退出的情况不删除数据 适配多种机型使用
            return;
        }
        SubmitItemDB submitItemDB = new SubmitItemDB(this);
        SubmitItemTempDB submitItemTempDB = new SubmitItemTempDB(this);
        SubmitDB submitDB = new SubmitDB(this);
        List<Submit> notSubmit = new SubmitDB(this).findAllSubmitDataByStateAndTargetId(Submit.STATE_NO_SUBMIT,
                targetId);
        if (!notSubmit.isEmpty()) {
            for (int i = 0; i < notSubmit.size(); i++) {
                int id = notSubmit.get(i).getId();
                submitDB.deleteSubmitById(id);
                submitItemTempDB.deleteSubmitItemBySubmitId(id);// 清除临时表中的数据
                List<SubmitItem> itemList = submitItemDB.findPhotoSubmitItemBySubmitId(id);
                if (!itemList.isEmpty()) {// 如果有照片的话删除照片
                    for (int k = 0; k < itemList.size(); k++) {
                        String tempPath = Constants.SDCARD_PATH + itemList.get(k).getParamValue();
                        File tempFile = new File(tempPath);
                        if (tempFile.exists()) {
                            tempFile.delete();
                        }
                    }
                }
                // 清除录音文件
                List<SubmitItem> recordItemList = new SubmitItemDB(AbsFuncActivity.this)
                        .findSubmitItemByType(Func.TYPE_RECORD);
                if (!recordItemList.isEmpty()) {
                    for (int k = 0; k < recordItemList.size(); k++) {
                        String tempPath = Constants.RECORD_PATH + recordItemList.get(k).getParamValue();
                        File tempFile = new File(tempPath);
                        if (tempFile.exists()) {
                            tempFile.delete();
                        }
                    }
                }
                submitItemDB.deleteSubmitItemBySubmitId(id);
                // 清楚所关联的超链接数据
                List<Submit> linkSubmit = new SubmitDB(this).findSubmitByParentId(id);
                if (!linkSubmit.isEmpty()) {
                    for (int j = 0; j < linkSubmit.size(); j++) {
                        cleanCurrentNoSubmit(linkSubmit.get(j).getTargetid());
                    }
                }
            }
        }
    }

    /**
     * 删除数据库中未提交的数据及其相关图片文件
     */
    protected void cleanAllNoSubmit() {
        if (!isNormal) {
            return;
        }
        SharedPreferencesUtil.getInstance(this).setContracInfo("");
        SubmitItemDB submitItemDB = new SubmitItemDB(this);
        List<Submit> notSubmit = new SubmitDB(this).findAllSubmitDataByState(Submit.STATE_NO_SUBMIT);// 获取未提交的数据

        if (!notSubmit.isEmpty()) {
            for (int i = 0; i < notSubmit.size(); i++) {
                int id = notSubmit.get(i).getId();
                new SubmitDB(this).deleteSubmitById(id);// 清除未提交的数据

                // 先删除所有图片文件，在删除数据库中相关数据
                List<SubmitItem> itemList = submitItemDB.findPhotoSubmitItemBySubmitId(id);// 获取未提交的图片类型数据
                if (!itemList.isEmpty()) {
                    for (int k = 0; k < itemList.size(); k++) {
                        String tempPath = Constants.SDCARD_PATH + itemList.get(k).getParamValue();// 获取图片地址
                        File tempFile = new File(tempPath);
                        if (tempFile.exists()) {
                            tempFile.delete();// 删除图片文件
                        }
                    }
                }

                submitItemDB.deleteSubmitItemBySubmitId(id);// 删除数据库中相关数据
            }
        }

        new SubmitItemTempDB(getApplicationContext()).deleteAllSubmitItem();// 清除临时表中的数据
    }

    /**
     * 控制机构店面联动显示
     */
    public void orgSetHook(boolean isReplenish, Bundle replenish) {
        SubmitItemDB submitItemDB = new SubmitItemDB(this);
        SubmitItemTempDB submitItemTempDB = new SubmitItemTempDB(this);
        for (Map.Entry<Integer, View> m : operatedMap.entrySet()) {
            View view = m.getValue();
            Integer index = (Integer) view.getTag();
            Func func = funcList.get(index);
            if (func != null && func.getOrgOption() != null) {
                hookView = view.findViewById(R.id.iv_func_check_mark);
                TView = (TextView) view.findViewById(R.id.tv_func_content);// 初始化对勾view
                if (func.getOrgOption() == Func.ORG_OPTION) {// 备注：此处不能把定位联动给去掉，如果是重新选择机构的话有可能把机构下的店面数据也清除掉，所以此时就需要也清除定位联动的值
                    String value = getOrgMap().get(func.getOrgLevel());// 机构去机构map里找值
                    if (!TextUtils.isEmpty(value)) {
                        setShowHook(false, hookView);
                        // ToastOrder.makeText(AbsFuncActivity.this, "test3",
                        // ToastOrder.LENGTH_SHORT).show();
                    } else {// value等于空的情况把以存储的ItEM都删除
                        if (isReplenish) {// 如果是查询的话 清楚掉replenish中的值
                            if (replenish.containsKey(String.valueOf(func.getFuncId()))) {
                                replenish.remove(String.valueOf(func.getFuncId()));
                                if (replenish.containsKey(func.getFuncId() + "_other")) {
                                    replenish.remove(func.getFuncId() + "_other");
                                }
                            }
                        } else {
                            SubmitItem item = null;
                            if (linkId != 0) {
                                item = submitItemTempDB.findSubmitItem(null, Submit.STATE_NO_SUBMIT, null, null, null,
                                        targetId, menuType, String.valueOf(func.getFuncId()));
                            } else {
                                item = submitItemDB.findSubmitItem(null, Submit.STATE_NO_SUBMIT, null, null, null,
                                        targetId, menuType, String.valueOf(func.getFuncId()));
                            }
                            if (item != null) {
                                item.setParamValue("");
                                if (linkId != 0) {
                                    submitItemTempDB.updateSubmitItem(item);
                                } else {
                                    if (this instanceof ChangeModuleFuncActivity) {

                                        submitItemDB.updateSubmitItem(item, false);
                                    } else {

                                        submitItemDB.updateSubmitItem(item, true);
                                    }
                                }
                                // submitItemDB.deleteSubmitItemById(item.getId());
                            }
                        }
                        setShowHook(false, hookView);
                    }
                } else {// 掉用此方法说明是选择的机构，选择机构的时候店面的数据肯定是要清除的，因为店面在最低层
                    // 如果清除店面的话和之相关的定位联动也要清除
                    if (isReplenish) {
                        if (replenish.containsKey(String.valueOf(func.getFuncId()))) {
                            replenish.remove(String.valueOf(func.getFuncId()));
                            if (replenish.containsKey(func.getFuncId() + "_other")) {
                                replenish.remove(func.getFuncId() + "_other");
                            }
                        }
                    } else {
                        SubmitItem item = null;
                        if (linkId != 0) {
                            item = submitItemTempDB.findSubmitItem(null, Submit.STATE_NO_SUBMIT, null, null, null,
                                    targetId, menuType, String.valueOf(func.getFuncId()));
                        } else {
                            item = submitItemDB.findSubmitItem(null, Submit.STATE_NO_SUBMIT, null, null, null, targetId,
                                    menuType, String.valueOf(func.getFuncId()));
                        }
                        if (item != null) {
                            item.setParamValue("");
                            if (linkId != 0) {
                                submitItemTempDB.updateSubmitItem(item);
                            } else {
                                if (this instanceof ChangeModuleFuncActivity) {

                                    submitItemDB.updateSubmitItem(item, false);
                                } else {
                                    submitItemDB.updateSubmitItem(item, true);

                                }
                                try {
                                    // View.xu 清空内容的数据,要从数据库删除,并重新加载页面
                                    Integer reKey = Integer.parseInt(item.getParamName());
                                    // submitItemDB.deleteSubmitItemByParamName(reKey+"");
                                    if (operatedMap.containsKey(reKey)) {
                                        // 将显示的内容置空
                                        View mView = operatedMap.get(reKey);
                                        TextView tvx = (TextView) mView.findViewById(R.id.tv_func_content);
                                        if (tvx != null) {
                                            tvx.setText("");
                                        }

                                    }
                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    setShowHook(false, hookView);
                }
            }
        }
    }

    /**
     * 隐藏域下拉框查询条件的时候要根据IsSearchMul和IsFuzzy的值进行判断该下拉框的类型
     *
     * @param func
     * @return
     */
    private Integer changeTypeForIsSearchMulAndIsFuzzy(Func func) {
        // 将类型改变为多选查询条件 如果是模糊搜索的话设置为模糊多选搜索
        if (func.getIsSearchMul() != null && func.getIsSearchMul() == Func.IS_Y) {
            if (func.getIsFuzzy() != null && func.getIsFuzzy() == Func.IS_Y) {
                func.setType(Func.TYPE_MULTI_CHOICE_FUZZY_QUERY_COMP);
            } else {
                func.setType(TYPE_MULTI_CHOICE_SPINNER_COMP);
            }
        } else {
            if (func.getIsFuzzy() != null && func.getIsFuzzy() == Func.IS_Y) {
                func.setType(Func.TYPE_SINGLE_CHOICE_FUZZY_QUERY_COMP);
            } else {
                func.setType(Func.TYPE_SELECTCOMP);
            }
        }
        return func.getType();
    }

    private String map2String(Map<Integer, String> map) {
        String mapStr = null;
        if (map != null && !map.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<Integer, String> entry : map.entrySet()) {
                sb.append(";").append(entry.getKey() != null ? entry.getKey() : "").append(",")
                        .append(entry.getValue() != null ? entry.getValue() : "");
            }
            mapStr = sb.substring(1).toString();
        }
        return mapStr;
    }

    private Map<Integer, String> string2Map(String value) {
        Map<Integer, String> map = new HashMap<Integer, String>();
        if (!TextUtils.isEmpty(value)) {
            String[] mapList = value.split(";");
            for (int i = 0; i < mapList.length; i++) {
                String[] str = mapList[i].split(",");
                if (str.length == 1 && !TextUtils.isEmpty(str[0])) {
                    map.put(Integer.parseInt(str[0]), null);
                } else if (!TextUtils.isEmpty(str[0]) && !TextUtils.isEmpty(str[1])) {
                    map.put(Integer.parseInt(str[0]), str[1]);
                }
            }
        }
        return map;
    }

    /**
     * 审核和上报的时候如果有机构的话要先存到MAP里联动时使用
     */
    private void controlOrgMap() {
        // 布局以后再调用，否则存在MAP里的值就被清空了
        if (isNormal && orgMapValueList != null && !orgMapValueList.isEmpty()) {
            for (String map : orgMapValueList) {
                String[] mapList = map.split(";");
                if (String.valueOf(targetId).equals(mapList[0])) {
                    for (int i = 1; i < mapList.length; i++) {
                        String[] str = mapList[i].split(":");
                        if (!TextUtils.isEmpty(str[0]) && !TextUtils.isEmpty(str[1])) {
                            orgMap.put(Integer.parseInt(str[0]), str[1]);
                        }
                    }
                }
            }
        }
    }

    /**
     * SQL查询的时候如果数据库里有值则不再进行查询
     *
     * @param func 当前控件
     * @return true 执行sql false 不执行sql
     */
    private boolean isExcuteSql(Func func) {
        boolean flag = true;// 默认执行SQL查询
        SubmitItem item = new SubmitItemDB(this).findSubmitItemByParamNameAndType(String.valueOf(func.getFuncId()),
                func.getType());
        if (item != null) {
            flag = false;
        }
        return flag;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        SharedPreferencesUtil2.getInstance(this).saveIsAnomaly(true);
        if (!isLinkActivity) {
            SharedPreferencesUtil2.getInstance(this).saveMenuId(menuId);
        }
        outState.putAll(bundle);
        outState.putString("startTime", startTime);
        outState.putString("orgMap_String", map2String(orgMap));
        outState.putString("chooseMap_String", map2String(chooseMap));
        outState.putString("paramName", paramName);
        outState.putString("fileName", fileName);
        outState.putBoolean("isCheckIn", isCheckIn);
        outState.putBoolean("isCanClick", isCanClick);
        outState.putInt("planId", planId);
        outState.putInt("targetId", targetId);
        outState.putInt("wayId", wayId);
        outState.putInt("storeId", storeId);
        outState.putInt("menuType", menuType);
        outState.putInt("taskId", taskId);
        outState.putInt("linkFuncId", linkFuncId);
        outState.putInt("clickFuncIndex", clickFuncIndex);
        outState.putBoolean("isSqlLink", isSqlLink);
        outState.putStringArrayList("map", orgMapValueList);
        outState.putBoolean("codeSubmitControl", codeSubmitControl);
        outState.putBoolean("isNoWait", isNoWait);
        outState.putString("attenTime", attenTime);
        outState.putInt("is_store_expand", is_store_expand);
        super.onSaveInstanceState(outState);
    }

    /**
     * 读取数据-->非正常状态下使用
     */
    @Override
    protected void restoreConstants(Bundle savedInstanceState) {
        super.restoreConstants(savedInstanceState);
        startTime = savedInstanceState.getString("startTime");
        paramName = savedInstanceState.getString("paramName");
        fileName = savedInstanceState.getString("fileName");
        isCheckIn = savedInstanceState.getBoolean("isCheckIn", false);
        isCanClick = savedInstanceState.getBoolean("isCanClick", true);
        orgMap = string2Map(savedInstanceState.getString("orgMap_String"));
        chooseMap = string2Map(savedInstanceState.getString("chooseMap_String"));

        planId = savedInstanceState.getInt("planId");
        targetId = savedInstanceState.getInt("targetId");
        wayId = savedInstanceState.getInt("wayId");
        storeId = savedInstanceState.getInt("storeId");
        menuType = savedInstanceState.getInt("menuType", 0);
        taskId = savedInstanceState.getInt("taskId", 0);
        linkFuncId = savedInstanceState.getInt("funcId", 0);
        clickFuncIndex = savedInstanceState.getInt("clickFuncIndex", 0);
        infoType = savedInstanceState.getInt("infoType", 0);
        isSqlLink = savedInstanceState.getBoolean("isSqlLink");
        orgMapValueList = savedInstanceState.getStringArrayList("map");// 查询和审核的时候结构的值
        codeSubmitControl = savedInstanceState.getBoolean("codeSubmitControl");
        isNoWait = savedInstanceState.getBoolean("isNoWait");
        attenTime = savedInstanceState.getString("attenTime");
        is_store_expand = savedInstanceState.getInt("is_store_expand");
    }

    /**
     * 设置机构关系
     *
     * @param submitId 当前的提交表单ID
     * @param linkFunc 当前控件
     * @param bundle   携带数据的bundle
     */
    private void setOrgRelation(int submitId, Func linkFunc, Bundle bundle) {
        StringBuffer orgBuffer = new StringBuffer();
        SubmitDB sDB = new SubmitDB(this);
        Submit submit = sDB.findSubmitById(submitId);
        if (submit != null) {
            int linkSubmitId = sDB.selectSubmitIdNotCheckOut(submit.getPlanId(), submit.getWayId(), submit.getStoreId(),
                    linkFunc.getMenuId(), 3, Submit.STATE_NO_SUBMIT);
            List<SubmitItem> itemList = new SubmitItemDB(this).findSubmitItemBySubmitId(linkSubmitId);
            for (int i = 0; i < itemList.size(); i++) {
                SubmitItem item = itemList.get(i);
                if (!isInteger(item.getParamName())) {// 机构里面不包含其他
                    continue;
                }
                Func func = new FuncDB(this).findFuncByFuncId(Integer.parseInt(item.getParamName()));
                if (func.getOrgOption() != null && func.getOrgOption() == Func.ORG_OPTION) {
                    orgBuffer.append(";").append(func.getOrgLevel()).append(":").append(item.getParamValue());
                }
            }
            if (orgBuffer.length() > 1) {
                orgMapValueList = new ArrayList<String>();
                orgMapValueList.add(linkFunc.getMenuId() + orgBuffer.toString());
                bundle.putStringArrayList("map", orgMapValueList);
            }
        }
    }

    /*
	 * 验证整数
	 */
    public boolean isInteger(String value) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    public boolean isOnPause = false;

    @Override
    protected void onPause() {
        super.onPause();
        isOnPause = true;
        if (recordPopupWindow != null) {// 如果正在录音停止
            recordPopupWindow.pauseRecord();
        }
    }

    /**
     * 录音
     */
    private void record(Func func) {
        recordPopupWindow = new RecordPopupWindow(this, bundle, func, new OnRefreshListner() {

            @Override
            public void onRefreshRecore() {
                setShowHook();
            }
        });
        recordPopupWindow.setMenuId(menuId);
        recordPopupWindow.setMenuType(menuType);
        recordPopupWindow.setMenuName(menuName);
        recordPopupWindow.show(null);
    }

    /**
     * 订单
     */
    private void order() {
        String orderStoreId = null;// 订单店面ID
        String orderStoreName = null;// 订单店面名称
        Intent intent = new Intent(this, MenuOrderActivity.class);
        if (menuType == com.yunhu.yhshxc.bo.Menu.TYPE_VISIT) {// 访店模块
            orderStoreId = String.valueOf(storeId);
            orderStoreName = storeName;
        } else {
            Func storeFunc = null;
            for (int i = 0; i < funcList.size(); i++) {// 查找店面控件
                Func func = funcList.get(i);
                if (func.getOrgOption() != null && func.getOrgOption() == Func.ORG_STORE) {
                    storeFunc = func;
                    break;
                }
            }
            if (storeFunc != null) {
                SubmitItem item = null;
                if (isLinkActivity) {// 超链接
                    item = new SubmitItemTempDB(this).findSubmitItemByParamName(String.valueOf(storeFunc.getFuncId()));
                } else {// 非超链接
                    item = new SubmitItemDB(this).findSubmitItemByParamName(String.valueOf(storeFunc.getFuncId()));
                }
                if (item == null || TextUtils.isEmpty(item.getParamValue())) {// 没输入店面信息
                    ToastOrder.makeText(this, getResources().getString(R.string.please_input) + storeFunc.getName(), ToastOrder.LENGTH_SHORT).show();
                    return;
                } else {
                    orderStoreId = item.getParamValue();// 店面ID
                    OrgStore store = new OrgStoreDB(this).findOrgStoreByStoreId(orderStoreId);// 根据店面ID查找店面
                    if (store != null) {
                        orderStoreName = store.getStoreName();// 店面名称
                    }
                }
            }
        }
        intent.putExtra("storeId", orderStoreId);// 店面ID
        intent.putExtra("storeName", orderStoreName);// 店面名称
        startActivity(intent);
    }

    @Override
    public void productCodeInfo(String info, int tableRow) {
        JLog.d(TAG, "ProductCodeInfo:" + info);
        try {
            JSONArray array = new JSONArray(info);
            if (array != null && array.length() > 0) {
                SubmitItemDB submitItemDB = new SubmitItemDB(this);
                SubmitItemTempDB submitItemTempDB = new SubmitItemTempDB(this);
                SubmitDB submitDB = new SubmitDB(this);
                int submitId = submitDB.selectSubmitId(planId, wayId, storeId, targetId, menuType);
                Submit submit = submitDB.findSubmitById(submitId);
                if (submit == null) {// 等于空的时候要插入一条submit
                    submit = new Submit();
                    submit.setPlanId(planId);
                    submit.setState(Submit.STATE_NO_SUBMIT);
                    submit.setStoreId(storeId);
                    submit.setWayId(wayId);
                    submit.setTargetid(targetId);
                    submit.setTargetType(menuType);
                    if (modType != 0) {
                        submit.setModType(modType);
                    }
                    submit.setMenuId(menuId);
                    submit.setMenuType(menuType);
                    submit.setMenuName(menuName);
                    submitDB.insertSubmit(submit);
                    submitId = submitDB.selectSubmitIdNotCheckOut(planId, wayId, storeId, targetId, menuType,
                            Submit.STATE_NO_SUBMIT);
                }

                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    Iterator<String> iterator = obj.keys();
                    if (!iterator.hasNext()) {
                        continue;
                    }
                    String key = iterator.next();
                    String value = obj.getString(key);
                    SubmitItem submitItem = new SubmitItem();
                    submitItem.setTargetId(currentFunc.getTargetid() + "");
                    submitItem.setSubmitId(submitId);
                    submitItem.setParamName(key);
                    submitItem.setParamValue(value);
                    submitItem.setIsCacheFun(currentFunc.getIsCacheFun());
                    boolean isHas = false;
                    if (linkId != 0) {
                        isHas = submitItemTempDB.findIsHaveParamName(submitItem.getParamName(), submitId);
                    } else {
                        isHas = submitItemDB.findIsHaveParamName(submitItem.getParamName(), submitId);
                    }
                    if (isHas) {// true表示已经操作过，此时更新数据库
                        if (linkId != 0) {
                            submitItemTempDB.updateSubmitItem(submitItem);
                        } else {
                            if (this instanceof ChangeModuleFuncActivity) {

                                submitItemDB.updateSubmitItem(submitItem, false);
                            } else {
                                submitItemDB.updateSubmitItem(submitItem, true);

                            }
                        }
                    } else {
                        if (linkId != 0) {
                            submitItemTempDB.insertSubmitItem(submitItem);
                        } else {
                            // submitItem.setMenuId(menuId);
                            if (this instanceof ChangeModuleFuncActivity) {
                                submitItemDB.insertSubmitItem(submitItem, false);

                            } else {
                                submitItemDB.insertSubmitItem(submitItem, true);

                            }
                        }
                    }
                }
                setShowHook();
            }
        } catch (Exception e) {
            JLog.d(TAG, "ProductCodeInfo:" + e.getMessage());
        }
    }

    /**
     * 串码平铺的时候先根据funcid查询下func
     *
     * @param funcId
     * @return
     */
    private Func productCodeInfoFuncByFuncId(String funcId) {
        Func func = null;
        if (menuType == Menu.TYPE_VISIT) {
            func = new VisitFuncDB(this).findFuncListByFuncIdAndTargetId(funcId, targetId);
        } else {
            func = new FuncDB(this).findFuncListByFuncIdAndTargetId(funcId, targetId);
        }
        return func;
    }

    /**
     * 根据值查找下拉框的id
     *
     * @param value
     * @param func
     * @return
     */
    private String spinnerDidByValue(String value, Func func) {
        String did = null;
        if (!TextUtils.isEmpty(value) && func != null) {
            Integer optionType = func.getOrgOption();
            if (optionType == null) {
                Dictionary dict = new DictDB(this).findDictByValue(func.getDictTable(), func.getDictDataId(), value);
                if (dict != null) {
                    did = dict.getDid();
                }
            } else if (optionType == Func.ORG_OPTION) {
                Org org = new OrgDB(this).findOrgByOrgName(value);
                if (org != null) {
                    did = String.valueOf(org.getOrgId());
                }
            } else if (optionType == Func.ORG_STORE) {
                OrgStore store = new OrgStoreDB(this).findOrgByStoreName(value);
                if (store != null) {
                    did = String.valueOf(store.getStoreId());
                }
            } else if (optionType == Func.ORG_USER) {
                OrgUser user = new OrgUserDB(this).findOrgByUserName(value);
                if (user != null) {
                    did = String.valueOf(user.getUserId());
                }
            }
        }
        return did;
    }

    /**
     * sql大数据查询
     *
     * @param func
     */
    private void sqlBigData(Func func) {
        boolean isReplenish = false;// 是否是查询条件 true是 false不是
        Bundle sqlBundle = new Bundle();
        sqlBundle.putInt("planId", planId);
        sqlBundle.putInt("wayId", wayId);
        sqlBundle.putInt("storeId", storeId);
        sqlBundle.putInt("targetId", targetId);
        sqlBundle.putInt("menuType", menuType);
        sqlBundle.putInt("taskId", taskId);
        sqlBundle.putInt("sqlStoreId", sqlStoreId);
        sqlBundle.putInt("modType", modType);
        sqlBundle.putString("funcId", String.valueOf(func.getFuncId()));
        sqlBundle.putBoolean("isLink", isLinkActivity);
        if (this instanceof QueryFuncActivity) {// 扫码作为查询条件的时候转为输入框
            QueryFuncActivity queryFuncActivity = (QueryFuncActivity) this;
            isReplenish = true;
            sqlBundle.putBundle("replenishBundle", queryFuncActivity.replenish);
        }
        sqlBundle.putBoolean("isReplenish", isReplenish);
        sqlBundle.putInt("menuId", menuId);
        sqlBundle.putString("menuName", menuName);
        Intent intent = new Intent(this, SqlBigDataActivity.class);
        intent.putExtra("bundle", sqlBundle);
        if (isReplenish) {
            startActivityForResult(intent, 103);
        } else {
            startActivity(intent);
        }
    }

    private void areaSearchLocationReceive(LocationResult result) {
        try {
            theOperatedFinish();
            CompDialog c = new CompDialog(this, currentFunc, bundle);// 弹出dialog
            c.setMenuId(menuId);
            c.setMenuType(menuType);
            c.setMenuName(menuName);
            if (result == null || !result.isStatus()) {
                ToastOrder.makeText(this, getResources().getString(R.string.org_store_distance_location_fail),
                        ToastOrder.LENGTH_SHORT).show();
            } else {
                c.c_lat = result.getLatitude();
                c.c_lon = result.getLongitude();
            }
            c.setProductCodeListener(this);
            dialog = c.createDialog();
            dialog.show();
            if (currentFunc.getType() == Func.TYPE_PRODUCT_CODE) {
                productCode = c.getProductCode();
            }
            isCanClick = true;
        } catch (Exception e) {
            JLog.e(e);
            ToastOrder.makeText(AbsFuncActivity.this, getResources().getString(R.string.locations_exception_try_again), ToastOrder.LENGTH_LONG).show();
            isCanClick = true;
        }
    }

    @Override
    public void areaSearchLocation(LocationResult result) {
        areaSearchLocationReceive(result);
    }

    @Override
    public void confirmLocation(LocationResult result) {
        theOperatedFinish();
        currentFunc = funcList.get(clickFuncIndex);
        saveLocationContent(result, currentFunc);// 保存该定位信息
    }

    /**
     * 设置考勤时间
     */
    public void setAttenTime() {
        attenTime = DateUtil.getCurDateTime();
    }

    /**
     * 存储视频信息
     *
     * @param fileName 照片名称
     * @return
     */
    protected int saveVideo(String fileName, Func compFunc) {
        SubmitDB submitDB = new SubmitDB(this);
        SubmitItemDB submitItemDB = new SubmitItemDB(this);
        int submitId = submitDB.selectSubmitIdNotCheckOut(planId, wayId, storeId, targetId, menuType,
                Submit.STATE_NO_SUBMIT);
        if (submitId != Constants.DEFAULTINT) {// 首先查找有没有该条数据对应的Submit
            SubmitItem submitItem = new SubmitItem();
            submitItem.setTargetId(currentFunc.getTargetid() + "");
            submitItem.setSubmitId(submitId);
            submitItem.setParamName(String.valueOf(compFunc.getFuncId()));
            submitItem.setParamValue(fileName);
            submitItem.setType(compFunc.getType());
            submitItem.setIsCacheFun(currentFunc.getIsCacheFun());
            boolean isHas = submitItemDB.findIsHaveParamName(submitItem.getParamName(), submitId);// 首先判断数据库中有没有该项操作
            if (isHas) {// 如果有就更新没有就插入 此时不能删除原来的图片，因为用户可能不保存
                if (this instanceof ChangeModuleFuncActivity) {

                    submitItemDB.updateSubmitItem(submitItem, false);
                } else {
                    submitItemDB.updateSubmitItem(submitItem, true);

                }
            } else {
                // submitItem.setMenuId(menuId);
                if (this instanceof ChangeModuleFuncActivity) {

                    submitItemDB.insertSubmitItem(submitItem, false);
                } else {
                    submitItemDB.insertSubmitItem(submitItem, true);

                }
            }
        } else {// 没有该条数据对应的Submit 在submitDB中先插入一条数据，与该条操作对应。
            Submit submit = new Submit();
            submit.setPlanId(planId);
            submit.setState(Submit.STATE_NO_SUBMIT);
            submit.setStoreId(storeId);
            submit.setWayId(wayId);
            submit.setTargetid(targetId);
            submit.setTargetType(menuType);
            if (modType != 0) {
                submit.setModType(modType);
            }
            submit.setMenuId(menuId);
            submit.setMenuType(menuType);
            submit.setMenuName(menuName);
            long submitid = submitDB.insertSubmit(submit);
            SubmitItem submitItem = new SubmitItem();
            submitItem.setTargetId(currentFunc.getTargetid() + "");
            submitItem.setSubmitId(Integer.valueOf(submitid + ""));
            submitItem.setParamName(String.valueOf(compFunc.getFuncId()));
            submitItem.setParamValue(fileName);
            submitItem.setType(compFunc.getType());
            // submitItem.setMenuId(menuId);
            submitItem.setIsCacheFun(currentFunc.getIsCacheFun());
            if (this instanceof ChangeModuleFuncActivity) {

                submitItemDB.insertSubmitItem(submitItem, false);// 插入该submitItem
            } else {
                submitItemDB.insertSubmitItem(submitItem, true);// 插入该submitItem

            }
        }

        return submitId;
    }

    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    class PhotoContent {
        Date date;
        Intent intentData;
        int requeCode;

        public int getRequeCode() {
            return requeCode;
        }

        public void setRequeCode(int requeCode) {
            this.requeCode = requeCode;
        }

        public PhotoContent() {
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public Intent getIntentData() {
            return intentData;
        }

        public void setIntentData(Intent intentData) {
            this.intentData = intentData;
        }

    }

    private void setPhotoSYTime(final Intent data, final int requestCode) {
        flag = true;
        timer = new Timer(true);
        if (task == null) {
            task = new MyTimerTask(requestCode);
        }
        // if(task.cancel()){
        timer.schedule(task, 3000, 100000000); // 延时3000ms后执行，1000ms执行一次
        // }
        new Thread(new Runnable() {

            @Override
            public void run() {
                Date date = PublicUtils.getNetDate();
                ;
                Message msg = Message.obtain();
                PhotoContent pc = new PhotoContent();
                pc.setDate(date);
                pc.setIntentData(data);
                pc.setRequeCode(requestCode);
                msg.obj = pc;
                mHanlder.sendMessage(msg);
            }
        }).start();
    }

    Handler mHanlder = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (flag) {
                flag = false;
                if (task != null) {
                    task.cancel();
                    task = null;
                }
                PhotoContent pc = (PhotoContent) msg.obj;
                if (null != pc) {
                    int requsetCode = pc.getRequeCode();
                    if (requsetCode == 100||requsetCode==400) {//拍照返回
                        resultForPhoto(pc.getDate());
                    } else if (requsetCode == 104) {//相册返回
                        resultForPhotoAlbum(pc.getIntentData(), pc.getDate());
                    }
                }
            }
        }

        ;
    };
    private boolean flag = true;
    Timer timer;
    // TimerTask task = new TimerTask(){
    // public void run() {
    // mHanlder.sendEmptyMessage(1);
    // }
    // };
    MyTimerTask task;

    class MyTimerTask extends TimerTask {
        int requ;

        public MyTimerTask(int requ) {
            this.requ = requ;
        }

        @Override
        public void run() {
            Message msg = Message.obtain();
            PhotoContent pc = new PhotoContent();
            pc.setRequeCode(requ);
            msg.obj = pc;
            mHanlder.sendMessage(msg);
        }

    }

    /**
     * @return 将非机构级联控件的层级关系存到bundle里
     */
    private Bundle getChooseMapBundle() {
        Bundle bundle = new Bundle();
        for (Map.Entry<Integer, String> m : getChooseMap().entrySet()) {
            bundle.putString(m.getKey() + "", m.getValue());
        }
        return bundle;
    }

    /**
     * @return 非机构的联动控件的层级关系map
     */
    public Map<Integer, String> getChooseMap() {
        if (!bundle.getBoolean("isTableHistory") && !bundle.getBoolean("isReport")) {
            return this.chooseMap;
        } else {
            return new HashMap<Integer, String>();
        }
    }

    /**
     * @return 将机构级联控件的层级关系存到bundle里
     */
    private Bundle getOrgMapBundle() {
        Bundle bundle = new Bundle();
        for (Map.Entry<Integer, String> m : getOrgMap().entrySet()) {
            bundle.putString(m.getKey() + "", m.getValue());
        }
        return bundle;
    }

    private int whichTablePhoto;

    public HashMap<Integer, View> getAllFuncLayout() {
        return layouts;
    }

    //标题栏扫描按钮返回
    private void requestScanCode(String scanId) {
        if (TextUtils.isEmpty(scanId)){
            return;
        }
        inserItem(scanId);



    }


    /**
     * 加载对话框
     */
    private Dialog dialogSc = null;
    /**
     * 在一个线程中用http获取数据
     */
    private void inserItem(String scanId) {
// 初始化加载对话框
        dialogSc = new MyProgressDialog(this, R.style.CustomProgressDialog,getResources().getString(R.string.activity_after_04));
        // 显示加载对话框
        dialogSc.show();
        HashMap<String ,String> map = setPageParams(scanId);

        RequestParams requestParams = new RequestParams();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            requestParams.put(entry.getKey(), entry.getValue());
            JLog.d(TAG, entry.getKey() + ":" + entry.getValue());
        }
        String baseUrlSearch = UrlInfo.getUrlReplenish(this);
        GcgHttpClient.getInstance(this).post(baseUrlSearch, requestParams,new HttpResponseListener() {
            @Override
            public void onStart() {
            }


            @Override
            public void onFailure(Throwable error, String content) {
//						currentPage = lastPage;
                fail(getResources().getString(R.string.activity_after_02));
            }

            @Override
            public void onFinish() {
//                elv_search.onRefreshComplete();
            }

            @Override
            public void onSuccess(int statusCode, String content) {
                // 获取JSON
                // 判断是否返回 "resultcode":"0001"
                if ("{\"resultcode\":\"0001\"}".equals(content)) {
                    JLog.d(TAG, "msg.obj   ====json===>>>>>" + content);

                    fail("没有查询到相关数据!");
                    return;
                }
                JLog.d(TAG, "JSON   =======>>>>>" + content);
                doInbackGround(content);

            }

        });
    }



    private HashMap<String ,String> setPageParams(String scanId) {
        HashMap<String ,String> map = new HashMap<>();
        map.put("phoneno",PublicUtils.receivePhoneNO(this));
        map.put("type",String.valueOf(Constants.MODULE_TYPE_UPDATE));
        map.put("page","1");
        map.put("taskid",String.valueOf(targetId));
        map.put("2044258",scanId);//scanId  0107020300001   0107020300002
        map.put("isScan","1");//此key不为null,后台用此字段区分权限问题
        return  map;
    }
    private void doInbackGround(final String json) {
        new Thread() {
            public void run() {
                try {
                    doInbackGroundInThread(json);
                } catch (Exception e) {
                    e.printStackTrace();
                    mHandler.sendEmptyMessage(PARSE_FAIL);
                }
            };
        }.start();
    }

    private void doInbackGroundInThread(String json) throws Exception {

            dataList = getDataListOnThread(json);
            mHandler.sendEmptyMessage(PARSE_OK);

    }

    private List<Map<String, String>> getDataListOnThread(String json) throws Exception {
        return parseJson(json);
    }

    private List<Map<String, String>> parseJson(String json) throws Exception {
        // 解析JSON
        ReplenishSearchResult searchResult = new ReplenishParse().parseSearchResult(json);
//        this.settingPage(searchResult.getCacherows(), searchResult.getTotal());
        // 补报数据
        List<Map<String, String>> resultList = searchResult.getResultList();
        // 判断没有数据时,隐藏窗体
        return resultList;
    }
    /**
     * 接受网络返回数据的Handler
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {

                case PARSE_OK:
                    afterSuccessParse();
                    break;
                case PARSE_FAIL:
                    fail(getResources().getString(R.string.activity_after_01));
                    break;
                case LOAD_MORE_FINISH:
                    fail(getResources().getString(R.string.activity_after_03));
                    break;
                default:
                    fail(getResources().getString(R.string.activity_after_02));
                    break;
            }
        }
    };

    /**
     * 提示错误信息
     *
     * @param message
     *            错误信息
     */
    private void fail(String message) {
        dismissLoadDialog();// 关闭加载对话框
        ToastOrder.makeText(this, message, ToastOrder.LENGTH_LONG).show();
    }
    /**
     * 关闭载入对话框
     */
    public void dismissLoadDialog() {
        if (dialogSc != null && dialogSc.isShowing()) {
            dialogSc.dismiss();
        }
    }
    /**
     * 服务器返回json,解析后的数据 map<funcId,value>
     */
    public List<Map<String, String>> dataList = new ArrayList<>();
    /**
     * 解析完在主线程中操作 可继承
     */
    private void afterSuccessParse() {
        if (dataList == null || dataList.isEmpty()) {
            fail(getResources().getString(R.string.activity_after_01));

        } else {
            dismissLoadDialog();// 关闭加载对话框
            //保存数据刷新列表
            saveInsetSubmitItem();
        }
    }

    /**
     * 扫描完获取数据插入到数据库，并刷新UI
     */
    public void saveInsetSubmitItem() {
    // 保存数据到数据库
        SubmitDB submitDB = new SubmitDB(this);
        SubmitItemDB submitItemDB = new SubmitItemDB(this);
        ArrayList<String> orgString = new ArrayList<String>();

        saveData(targetId, menuType, dataList.get(0), submitDB, submitItemDB, orgString, false, 0, null);

    }

    /**
     * 保存到数据库
     *
     * @param targetId
     * @param saveItem
     * @param submitDB
     * @param submitItemDB
     * @param orgString
     */
    public void saveData(int targetId, int menuType, Map<String, String> saveItem, SubmitDB submitDB, SubmitItemDB submitItemDB, ArrayList<String> orgString, boolean isLink, int submitParentId, Func linkItemFunc) {
        cleanCurrentNoSubmit(targetId);
        if (saveItem == null || saveItem.isEmpty()) {
            return;
        }
        Submit submit = null;
        int submitIdTemp = submitDB.selectSubmitIdNotCheckOut(planId, wayId, storeId, targetId, menuType,
                Submit.STATE_NO_SUBMIT);
        if(submitIdTemp!=0){
            submit = submitDB.findSubmitById(submitIdTemp);
        }else{
             submit = new Submit();
        }


        submit.setTargetid(targetId);
        submit.setTargetType(menuType);
        if (modType!=0) {
            submit.setModType(modType);
        }
        if (!TextUtils.isEmpty(saveItem.get(Constants.PATCH_ID))) {
            submit.setTimestamp(saveItem.get(Constants.PATCH_ID));
            JLog.d(TAG, "PATCH_ID=========>" + saveItem.get(Constants.PATCH_ID));
        }
        if (!TextUtils.isEmpty(saveItem.get(Constants.TASK_ID))) {
            submit.setDoubleId(Integer.valueOf(saveItem.get(Constants.TASK_ID)));
        }
//        if (linkFunc != null && linkFunc.getIsSearchModify() != null && linkFunc.getIsSearchModify() == 2) {  //超链接接暂不考虑
//            submit.setStoreId(storeId);
//        }
        JLog.d(TAG, "clickItem.get(\"taskid\")=========>" + saveItem.get(Constants.TASK_ID));
        submit.setState(Submit.STATE_NO_SUBMIT);
        if (isLink) {
            submit.setParentId(submitParentId);
            Submit parentSubmit = submitDB.findSubmitById(submitParentId);
            SubmitItem submitItem = new SubmitItem();
            submitItem.setSubmitId(parentSubmit.getId());
            submitItem.setType(Func.TYPE_LINK);
            submitItem.setParamName(linkItemFunc.getFuncId().toString());
            submitItem.setParamValue(submit.getTimestamp());
            submitItem.setIsCacheFun(linkItemFunc.getIsCacheFun());
            submitItem.setTargetId(linkItemFunc.getTargetid()+"");
            submitItemDB.insertSubmitItem(submitItem,false);
        }
        submit.setMenuId(menuId);
        submit.setMenuType(menuType);
        submit.setMenuName(menuName);
        submitDB.insertSubmit(submit);

        // 通过targetId/targetType/Submit.State查询submitId
        int submitId = submitDB.selectSubmitIdNotCheckOut(null, null, null, targetId, menuType, Submit.STATE_NO_SUBMIT);

        List<Func> funcs = null;
        if (modType == Constants.MODULE_TYPE_REASSIGN) {// 改派
            funcs = new FuncDB(AbsFuncActivity.this).findFuncListByDoubleReadOnly(targetId);
        }
//        else if (isLink || funcId != Constants.DEFAULTINT) {//是超链接接暂不考虑
//            funcs = new FuncDB(AbsFuncActivity.this).findFuncListByTargetid(targetId, isSqlLink);
//        }
        else {
            funcs = new FuncDB(AbsFuncActivity.this).findFuncListByTargetidReplash(targetId, saveItem.get(Constants.DATA_STATUS), isSqlLink);
        }
        StringBuilder sb = new StringBuilder();
        for (Func func : funcs) {// 遍历
//			if (func.getType() == Func.TYPE_VIDEO || func.getType()==Func.TYPE_ATTACHMENT || func.getType() == Func.TYPE_CAMERA || func.getType() == Func.TYPE_CAMERA_HEIGHT || func.getType() == Func.TYPE_CAMERA_CUSTOM || func.getType() == Func.TYPE_CAMERA_MIDDLE || func.getType() == Func.TYPE_BUTTON) {
//				continue;
//			}

            if (!TextUtils.isEmpty(saveItem.get(func.getFuncId().toString()))) {
                if (func.getType() == Func.TYPE_LINK) {//超链接暂不考虑
//                    saveData(func.getMenuId(), menuType, parseLinkJson(saveItem.get(func.getFuncId().toString())), submitDB, submitItemDB, orgString, true, submitId, func);
                }else {
                    SubmitItem submitItem = new SubmitItem();
                    submitItem.setSubmitId(submitId);
                    submitItem.setType(func.getType());
                    submitItem.setIsCacheFun(func.getIsCacheFun());
                    submitItem.setTargetId(func.getTargetid()+"");
                    if (func.getOrgOption()!=null && func.getOrgOption()== Func.ORG_STORE) {
                        submitItem.setNote(SubmitItem.NOTE_STORE);
                    }
                    if (func.getOrgOption()!=null && func.getOrgOption() == Func.OPTION_LOCATION) {
                        SubmitItem storeItem = new SubmitItemDB(this).findSubmitItemByNote(submitId, SubmitItem.NOTE_STORE);
                        if (storeItem!=null && !TextUtils.isEmpty(storeItem.getParamValue())) {
                            submitItem.setNote(storeItem.getParamName());
                        }
                    }
                    submitItem.setParamName(func.getFuncId().toString());
                    if (func.getType() == Func.TYPE_SQL_BIG_DATA) {
                        String json = saveItem.get(func.getFuncId().toString());
                        try {
                            JSONArray array = new JSONArray(json);
                            submitItem.setParamValue(array.getString(0));
                            submitItem.setNote(array.getString(1));
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else {
                        submitItem.setParamValue(saveItem.get(func.getFuncId().toString()));
                    }
                    submitItemDB.insertSubmitItem(submitItem,false);
                    JLog.d(TAG, "submitItem=====ParamName-->" + submitItem.getParamName() + "<=====>ParamValue-->" + submitItem.getParamValue());
                    if ((func.getType() == Func.TYPE_SELECT_OTHER || func.getType() == Func.TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY_COMP) && saveItem.get(func.getFuncId().toString()).equals("-1")) {
                        SubmitItem submitItem_other = new SubmitItem();
                        submitItem_other.setSubmitId(submitId);
                        submitItem_other.setType(func.getType());

                        submitItem_other.setParamName(func.getFuncId().toString() + "_other");
                        submitItem_other.setParamValue(saveItem.get(func.getFuncId().toString() + "_other"));
                        submitItemDB.insertSubmitItem(submitItem_other,false);
                    }
                    if (func.getOrgOption() != null && func.getOrgOption() == Func.ORG_OPTION) {
                        sb.append(";").append(func.getOrgLevel()).append(":").append(saveItem.get(func.getFuncId().toString()));
                    }
                    if (func.getType() == Func.TYPE_PRODUCT_CODE && !TextUtils.isEmpty(saveItem.get("compare_content"))) {
                        SharedPreferencesUtil.getInstance(this).setCompareContent("content_"+func.getTargetid(),saveItem.get("compare_content"));
                    }
                }
            }
        }
        if (sb != null && sb.length() > 1) {
            orgString.add(targetId + sb.toString());
        }
        isAssetEdit = false;
        setShowHook();
    }
    //固定资产扫描返回数据后的控件是否可以编辑
    private boolean isAssetEdit = true;


}
