package com.yunhu.yhshxc.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.zxing.activity.CaptureActivity;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yunhu.android.view.VerticalMarqueeLayout;
import com.yunhu.yhshxc.MeetingAgenda.FirstEvent;
import com.yunhu.yhshxc.MeetingAgenda.MeetingagendaListActivity;
import com.yunhu.yhshxc.MeetingAgenda.bo.MeetingAgenda;
import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.addressBook.AddressBookActivity;
import com.yunhu.yhshxc.activity.carSales.CarSalesMainActivity;
import com.yunhu.yhshxc.activity.carSales.util.SharedPreferencesForCarSalesUtil;
import com.yunhu.yhshxc.activity.questionnaire.QuestionnaireActivity;
import com.yunhu.yhshxc.activity.stationReserve.StationReserveMainActivity;
import com.yunhu.yhshxc.activity.todo.TodoListActivity;
import com.yunhu.yhshxc.activity.zrmenu.ZRModuleActivity;
import com.yunhu.yhshxc.adapter.HomeMenuAdapter;
import com.yunhu.yhshxc.adapter.Home_GridViewAdapter;
import com.yunhu.yhshxc.adapter.ShiHuaAdapter;
import com.yunhu.yhshxc.application.SoftApplication;
import com.yunhu.yhshxc.attendance.AttendanceActivity;
import com.yunhu.yhshxc.attendance.AttendanceUtil;
import com.yunhu.yhshxc.bo.Fristpage_bean;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.HomeBean;
import com.yunhu.yhshxc.bo.HomeImageBean;
import com.yunhu.yhshxc.bo.HomeScrollImage;
import com.yunhu.yhshxc.bo.Home_beam;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.bo.Module;
import com.yunhu.yhshxc.bo.ReplenishSearchResult;
import com.yunhu.yhshxc.bo.ShiHuaMenu;
import com.yunhu.yhshxc.bo.Submit;
import com.yunhu.yhshxc.bo.SubmitItem;
import com.yunhu.yhshxc.bo.Task;
import com.yunhu.yhshxc.core.ApiRequestFactory;
import com.yunhu.yhshxc.core.ApiRequestMethods;
import com.yunhu.yhshxc.core.ApiUrl;
import com.yunhu.yhshxc.database.MainMenuDB;
import com.yunhu.yhshxc.database.MenuShiHuaDB;
import com.yunhu.yhshxc.database.ModuleDB;
import com.yunhu.yhshxc.database.SlidePictureDB;
import com.yunhu.yhshxc.database.SubmitDB;
import com.yunhu.yhshxc.database.SubmitItemDB;
import com.yunhu.yhshxc.database.SubmitItemTempDB;
import com.yunhu.yhshxc.database.TaskDB;
import com.yunhu.yhshxc.dialog.DialogGetOrgActivity;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.expand.StoreExpandActivity;
import com.yunhu.yhshxc.help.HelpPopupWindow;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.http.download.apk.DownApkBackstageService;
import com.yunhu.yhshxc.location.backstage.BackstageLocationService;
import com.yunhu.yhshxc.module.bbs.BBSMainActivity;
import com.yunhu.yhshxc.nearbyVisit.NearbyVisitActivity;
import com.yunhu.yhshxc.notify.NotifyListActivity;
import com.yunhu.yhshxc.order2.Order2MainActivity;
import com.yunhu.yhshxc.order3.Order3MainActivity;
import com.yunhu.yhshxc.order3.send.Order3SendActivity;
import com.yunhu.yhshxc.parser.GetOrg;
import com.yunhu.yhshxc.parser.ReplenishParse;
import com.yunhu.yhshxc.push.GetPushService;
import com.yunhu.yhshxc.report.ReportActivity;
import com.yunhu.yhshxc.service.DownSlidePicService;
import com.yunhu.yhshxc.service.StyleDownLoadService;
import com.yunhu.yhshxc.style.SlidePicture;
import com.yunhu.yhshxc.style.StyleUtil;
import com.yunhu.yhshxc.submitManager.SubmitManagerActivity;
import com.yunhu.yhshxc.submitManager.core.SubmitWorkManager;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.FileHelper;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil2;
import com.yunhu.yhshxc.utility.SharedPreferencesUtilForNearby;
import com.yunhu.yhshxc.utility.URLWrapper;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.visit.VisitWayActivity;
import com.yunhu.yhshxc.visitors.VisitorListActivity;
import com.yunhu.yhshxc.webReport.WebViewUtilActivity;
import com.yunhu.yhshxc.wechat.WechatActivity;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.yunhu.yhshxc.workSummary.WorkSummaryMainActivity;
import com.yunhu.yhshxc.workplan.WorkPlanActivity;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import encoding.Base64;
import gcg.org.debug.JLog;
import okhttp3.Call;
import pl.droidsonroids.gif.GifImageView;
import view.CustomGridView;
import view.TipDialog;

import static com.yunhu.yhshxc.func.AbsFuncActivity.LOAD_MORE_FINISH;
import static com.yunhu.yhshxc.func.AbsFuncActivity.PARSE_FAIL;
import static com.yunhu.yhshxc.func.AbsFuncActivity.PARSE_OK;

/**
 * 主菜单Acitivity的工作台Fragment页面（所有模块Activity的入口）
 * <p>
 * 负责根据数据库中的数据动态创建当前用户可以使用的模块，所有模块均以菜单项的方式显示，用户单击某个菜单项 就会启动对应的Activity
 *
 * @author wangchao
 * @version 2013.5.21
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class HomeMenuFragment extends Fragment implements OnItemClickListener, OnClickListener {
    private static final String HEAD_PATH = Constants.SDCARD_PATH + "head.jpg";
    /**
     * 时钟等相关可视组件的容器
     */
    private LinearLayout homeView;

    /**
     * 菜单项可视组件的容器 日常工作功能
     */
    private CustomGridView gridView = null;

    /**
     * 菜单项可视组件的容器 基础功能
     */
    private CustomGridView basic_gridview = null;

    /**
     * 日常功能菜单容器GridView的适配器
     */
    public HomeMenuAdapter adapter = null;

    /**
     * 石化功能菜单容器GridView的适配器
     */
    public ShiHuaAdapter shiHuaAdapter = null;

    /**
     * 所有菜单项的数据，用于填充GridView
     */
    private List<Menu> menuList;


    private ArrayList<String> urlList;

    private ArrayList<View> list;

    Context context;

    private DisplayImageOptions options;
    protected ImageLoader imageLoader = ImageLoader.getInstance();

    private LayoutParams params_rb;

    private Drawable drawable;
    int current = 0;// 临时缓存角标


    /**
     * 本Fragment页面所展示的的View
     */
    private View rootView;




    /**
     * 数据库得到上方Gridview的集合
     */
    private List<ShiHuaMenu> shihuamenuList;
    private List<ShiHuaMenu> mshihuamenuList;

    private LinearLayout scan_l;
    private LinearLayout take_pl;

    private View home_rc,home_rc2,home_vp;//会议日程
    private int company_id, uid;
    private MeetingAgenda meetingdata,meetingdata2;
    private RelativeLayout title_tx_ll;

    private static final  int REMINDIDZ=0;
    private static final  int REMINDIDO=1;
    private static final  int REMINDIDT=2;
    private static final  int REMINDIDTE=3;
    private static final  int REMINDIDF=4;
    private static final  int REMINDIDFI=5;
    private static final  int REMINDIDS=6;
    private static final  int CAMERAIDBOOK=7;
    private TextView tv_home_gr,tv_home_gr2,tv_meeting_data,tv_visit_data;
    private GridView home_gridview1,home_gridview2;
    private List<Home_beam> list_home_beam;
    private Home_beam home_beam;
    private ViewPager viewpager_home;
    private int meeting,visitor;
    private List<HomeBean.DataBeanX.DataBean.ListBean>  list_top;

    private ImageView showMenu, addOne, scan;//展示菜单按钮,拍照按钮,扫一扫按钮
    private List<Fristpage_bean> fristpage_been_list = new ArrayList<>();
    private List<Fristpage_bean> fristpage_been_list2 = new ArrayList<>();
    private VerticalMarqueeLayout<Fristpage_bean> tv_top,tv_top2;
    private Intent intext;
    private List<HomeImageBean> homeImageBeenList;
    private ImageView iv_home_scrollview1;
    private ImageView iv_home_scrollview2;
    private ImageView iv_home_scrollview3;
    private ImageView iv_home_scrollview4;
    private ImageView iv_home_scrollview5;
    private ImageView iv_home_scrollview6;
    private ImageView iv_home_scrollview7;
    private ImageView iv_home_scrollview8;
    private ImageView iv_home_scrollview9;
    private ImageView iv_home_scrollview10;
    private ImageView iv_home_scrollview11;
    private ImageView iv_home_scrollview12;
    private ImageView iv_home_scrollview13;
    private ImageView iv_home_scrollview14;
    private ImageView iv_home_scrollview15;
    private ImageView iv_home_scrollview16;
    private ImageView iv_home_scrollview17;
    private ImageView iv_home_scrollview18;

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        SharedPreferencesUtil s = SharedPreferencesUtil.getInstance(context);
        company_id = s.getCompanyId();
        uid = s.getUserId();

        //假数据,记得删除
        list_home_beam = new ArrayList<>();
        for ( int i=0;i<8;i++){
            home_beam = new Home_beam();
            home_beam.setName("今日美食");
            home_beam.setContext("吃货的福利");
            home_beam.setImageUrl("");
            list_home_beam.add(home_beam);
        }

        SoftApplication.getInstance().setHomeMenuFragment(this);
        if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.home, container, false);
            initAll(rootView);
        }

        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.wechat_moren_header)
                .showImageForEmptyUri(R.drawable.wechat_moren_header)
                .showImageOnFail(R.drawable.wechat_moren_header)
                .cacheInMemory(true)
                .build();
        return rootView;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initAll();

    }

    RelativeLayout person_info_rl;
    ImageButton person_info_btn;

    /**
     * 初始化该页面要展示的数据
     *
     * @param view
     */
    void initAll(View view) {
        home_rc  = view.findViewById(R.id.home_rc);
        home_rc2  = view.findViewById(R.id.home_rc2);
        home_vp  = view.findViewById(R.id.home_vp);
        iv_home_scrollview1 = (ImageView) home_vp.findViewById(R.id.iv_home_scrollview1);
        iv_home_scrollview2 = (ImageView) home_vp.findViewById(R.id.iv_home_scrollview2);
        iv_home_scrollview3 = (ImageView) home_vp.findViewById(R.id.iv_home_scrollview3);
        iv_home_scrollview4 = (ImageView) home_vp.findViewById(R.id.iv_home_scrollview4);
        iv_home_scrollview5 = (ImageView) home_vp.findViewById(R.id.iv_home_scrollview5);
        iv_home_scrollview6 = (ImageView) home_vp.findViewById(R.id.iv_home_scrollview6);
        iv_home_scrollview7 = (ImageView) home_vp.findViewById(R.id.iv_home_scrollview7);
        iv_home_scrollview8 = (ImageView) home_vp.findViewById(R.id.iv_home_scrollview8);
        iv_home_scrollview9 = (ImageView) home_vp.findViewById(R.id.iv_home_scrollview9);
        iv_home_scrollview10 = (ImageView) home_vp.findViewById(R.id.iv_home_scrollview10);
        iv_home_scrollview11 = (ImageView) home_vp.findViewById(R.id.iv_home_scrollview11);
        iv_home_scrollview12 = (ImageView) home_vp.findViewById(R.id.iv_home_scrollview12);
        iv_home_scrollview13 = (ImageView) home_vp.findViewById(R.id.iv_home_scrollview13);
        iv_home_scrollview14 = (ImageView) home_vp.findViewById(R.id.iv_home_scrollview14);
        iv_home_scrollview15 = (ImageView) home_vp.findViewById(R.id.iv_home_scrollview15);
        iv_home_scrollview16 = (ImageView) home_vp.findViewById(R.id.iv_home_scrollview16);
        iv_home_scrollview17 = (ImageView) home_vp.findViewById(R.id.iv_home_scrollview17);
        iv_home_scrollview18 = (ImageView) home_vp.findViewById(R.id.iv_home_scrollview18);
        tv_top = (VerticalMarqueeLayout<Fristpage_bean>) view.findViewById(R.id.tv_top);
        tv_top2 = (VerticalMarqueeLayout<Fristpage_bean>) view.findViewById(R.id.tv_top2);
        view.findViewById(R.id.tv_visit_content).setOnClickListener(this);
        tv_visit_data = (TextView) view.findViewById(R.id.tv_visit_data);
        tv_visit_data.setOnClickListener(this);
        view.findViewById(R.id.tv_meeting_content).setOnClickListener(this);
        tv_meeting_data = (TextView) view.findViewById(R.id.tv_meeting_data);
        tv_meeting_data.setOnClickListener(this);
        tv_home_gr = (TextView) home_rc.findViewById(R.id.tv_home_gr);
        tv_home_gr2 = (TextView) home_rc2.findViewById(R.id.tv_home_gr);
        home_gridview1 = (GridView) home_rc.findViewById(R.id.home_gridview1);
        home_gridview2 = (GridView) home_rc2.findViewById(R.id.home_gridview1);
        if (list_home_beam !=null && list_home_beam.size()>0){
            home_gridview1.setAdapter(new Home_GridViewAdapter(context,list_home_beam,1));
            home_gridview1.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(context,HomeSecondActivity.class);
                    intent.putExtra("catid",i);
                    startActivity(intent);
                }
            });
            home_gridview2.setAdapter(new Home_GridViewAdapter(context,list_home_beam,2));
            home_gridview2.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent2 = new Intent(context,HomeSecondActivity.class);
                    intent2.putExtra("catid",i+8);
                    startActivity(intent2);
                }
            });
        }
        scan_l = (LinearLayout) view.findViewById(R.id.scan_l);
        title_tx_ll = (RelativeLayout) view.findViewById(R.id.title_tx_ll);
        take_pl = (LinearLayout) view.findViewById(R.id.take_pl);
        scan_l.setOnClickListener(listener);
        take_pl.setOnClickListener(listener);
        // initBase();//父类AbsBaseActivity的方法，目前该方法内是空的，不做任何事。
        // titlebar();//将TitleBar添加到当前Activity中
        // clock();//将时钟、用户角色等信息添加到当前Activity中
        gridView = (CustomGridView) view.findViewById(R.id.gridview_home);
        basic_gridview = (CustomGridView) view.findViewById(R.id.basicfuncation_grid);

        person_info_rl = (RelativeLayout) view.findViewById(R.id.person_info_rl);
        person_info_btn = (ImageButton) view.findViewById(R.id.person_info_btn);
        person_info_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), PersonalActivity.class));
            }
        });
        person_info_rl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), PersonalActivity.class));
            }
        });
        gridView.setOnItemClickListener(this);
        basic_gridview.setOnItemClickListener(this);
        shihuamenuList = new ArrayList<>();
        mshihuamenuList = new ArrayList<>();
        adapter = new HomeMenuAdapter(context);

        getDataSrc();// 从数据库获取所有菜单项数据列表
        if (menuList.size() > 0) {
            gridView.setAdapter(adapter);// 将数据填充到GridView组件中
        } else {
            Toast.makeText(getActivity(), PublicUtils.getResourceString(context, R.string.un_menu), Toast.LENGTH_SHORT).show();
        }
        initStartService();// 初始化开启的服务

        initStyle();

        updateOperation();

        float density = getResources().getDisplayMetrics().density;
        params_rb = new LayoutParams((int) (8 * density), (int) (8 * density));
        int margin = (int) (6 * density);
        params_rb.setMargins(margin, margin, margin, margin);
        drawable = getResources().getDrawable(R.color.ttt);


        list = new ArrayList<View>();
        //先判断标识,是否存在下载的轮播图
        if (SharedPreferencesUtil2.getInstance(context).getIsSlideImageDown()) {//已经下载完成
            try {
                setSlideImageList();
            } catch (Exception c) {
                c.printStackTrace();
            }
        } else {//使用默认的图片,等待下载完成的通知进行更新轮播图
            //有可能图片还在下载过程中设置下载完成监听
            IntentFilter filter = new IntentFilter();
            filter.addAction(HomeMenuFragment.SLIDEIMAGE_ACTION);
            context.registerReceiver(slideImageReceiver, filter);
        }
        showMenu = (ImageView) view.findViewById(R.id.iv_show_menu);
        addOne = (ImageView) view.findViewById(R.id.iv_add_one);
        scan = (ImageView) view.findViewById(R.id.iv_scan);
        showMenu.setOnClickListener(listener);
        addOne.setOnClickListener(listener);
        scan.setOnClickListener(listener);

        Fristpage_bean fristpage_bean0 = new Fristpage_bean();
        fristpage_bean0.setTitle("概况");
        fristpage_bean0.setContext("追寻梦想生活，中融新职场，等你来。");
        fristpage_bean0.setWebViewUrl("https://s.eqxiu.cn/s/J3vDh53Y");
        fristpage_bean0.setImageid(R.drawable.image_homevp1);

        Fristpage_bean fristpage_bean1 = new Fristpage_bean();
        fristpage_bean1.setTitle("功能化社区");
        fristpage_bean1.setContext("功能化社区，提供餐厅、咖啡厅、健身房等");
        fristpage_bean1.setWebViewUrl("https://v.eqxiu.cn/s/tDoWZHk9");
        fristpage_bean1.setImageid(R.drawable.image_homevp1);

        Fristpage_bean fristpage_bean2 = new Fristpage_bean();
        fristpage_bean2.setTitle("环保化社区");
        fristpage_bean2.setWebViewUrl("https://s.eqxiu.cn/s/OfLOu0YY");
        fristpage_bean2.setContext("乘风破浪，弄潮追日。");
        fristpage_bean2.setImageid(R.drawable.image_homevp1);

        Fristpage_bean fristpage_bean3 = new Fristpage_bean();
        fristpage_bean3.setTitle("智能篇");
        fristpage_bean3.setWebViewUrl("https://v.eqxiu.cn/s/RicKH0sL");
        fristpage_bean3.setContext("四位一体，打造环保。");
        fristpage_bean3.setImageid(R.drawable.image_homevp1);

        Fristpage_bean fristpage_bean4 = new Fristpage_bean();
        fristpage_bean4.setContext("智赢未来，助力智慧园区。");
        fristpage_bean4.setWebViewUrl("https://v.eqxiu.cn/s/RicKH0sL");
        fristpage_bean4.setTitle("软装篇");
        fristpage_bean4.setImageid(R.drawable.image_homevp1);

        Fristpage_bean fristpage_bean5 = new Fristpage_bean();
        fristpage_bean5.setTitle("项目历程");
        fristpage_bean5.setContext("新职场数据分析");
        fristpage_bean5.setWebViewUrl("https://s.eqxiu.cn/s/NIaNMDpJ");
        fristpage_bean5.setImageid(R.drawable.image_homevp1);

        fristpage_been_list.add(fristpage_bean0);
        fristpage_been_list.add(fristpage_bean1);
        fristpage_been_list.add(fristpage_bean2);
        fristpage_been_list.add(fristpage_bean3);
        fristpage_been_list.add(fristpage_bean4);
        fristpage_been_list.add(fristpage_bean5);

        tv_top2.datas(fristpage_been_list,R.layout.homepage_notice_item).builder(tv_top2.new OnItemBuilder() {
            @Override
            public void assemble(View view, Fristpage_bean fristpage_bean) {
                TextView tv1 = (TextView) view.findViewById(R.id.tv_startmetting);
                TextView tv2 = (TextView) view.findViewById(R.id.tv_startactivity);
                TextView tv3 = (TextView) view.findViewById(R.id.tv_startactivity);
                tv1.setText(fristpage_bean.getTitle());
                tv2.setText(fristpage_bean.getContext());
                tv3.setText(fristpage_bean.getContext());
            }
        }).listener(new VerticalMarqueeLayout.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(context,"暂无内容跳转",Toast.LENGTH_LONG).show();
            }
        }).commit();
        tv_top2.startScroll();
        //获取未完成会议和访客数量
        getMeetingcount();
        getVisitorcount();
        BookMeetingByBuild();
        BookMeetingByBuild2();
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()){
            case R.id.tv_meeting_content:
            case R.id.tv_meeting_data:
              Intent intent = new Intent(context, MeetingagendaListActivity.class);
//                Intent intent = new Intent(context, MeetingBoomJumpActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_visit_content:
            case R.id.tv_visit_data:
                Intent intent2 = new Intent(context, VisitorListActivity.class);
//              Intent intent2 = new Intent(context, AgendaPlanActivty.class);
                startActivity(intent2);
                break;
            default:
                break;
        }
    }
    /**
     * 设置下载的轮播图
     */
    private void setSlideImageList() {
        //查询出数据库图片集合
        List<SlidePicture> imgList = new SlidePictureDB(context).queryAll();
        if (imgList != null && imgList.size() >0){
            title_tx_ll.setBackground(new BitmapDrawable(BitmapFactory.decodeFile(imgList.get(0).getSdPath())+imgList.get(0).getPictureName()));
        }
        //接着要检查数据库中的图片是否都存在文件夹下,不存在的话要使用默认图片,然后开启服务进行下载,下载完成再发出广播进行通知设置
        int picCount = 0;
        for (int i = 0; i < imgList.size(); i++) {
            SlidePicture picture = imgList.get(i);
            String imagePath = picture.getSdPath() + picture.getPictureName();
            File file = new File(imagePath);
            if (file.exists()) {
                picCount += 1;
            }
        }
        if (imgList.size() > 0 && picCount <= 0) {//说明图片未被下载,先使用默认,再开启下载
            Intent intent = new Intent(context, DownSlidePicService.class);//开启后台下载图片
            intent.putExtra("download", true);
            context.startService(intent);
        } else {

            if (imgList.size() > 0) {//清空原有view
                list.clear();
            }
            for (int i = 0; i < imgList.size(); i++) {
                SlidePicture sp = imgList.get(i);
                String imagePath = sp.getSdPath() + sp.getPictureName();
                File file = new File(imagePath);
                if (!file.exists() || !isImage(imagePath)) {//如果该文件不存在,或者不是图片,就跳过
                    continue;
                }
                GifImageView imageView = new GifImageView(context);
                imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                if (imagePath.endsWith(".gif")) {
                    imageView.setImageURI(Uri.parse("file://" + imagePath));
                } else {

                    ImageLoader.getInstance().displayImage("file://" + imagePath, imageView);//利用ImageLoader进行加载
                }
                list.add(imageView);
            }
        }
    }

    private boolean isImage(String imagePath) {
        if (imagePath.endsWith(".jpg") || imagePath.endsWith(".jpeg") || imagePath.endsWith(".png") || imagePath.endsWith(".bmp") || imagePath.endsWith(".gif")) {
            return true;
        } else {
            return false;
        }
    }



    class MyPageAdapter extends PagerAdapter {
        ArrayList<View> list;
        ArrayList<String> urlList;

        MyPageAdapter(ArrayList<View> list, ArrayList<String> urlList) {
            this.list = list;
            this.urlList = urlList;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            container.removeView(list.get(position));
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // list.get(position).setOnClickListener(new
            // MyPagerOnclick(position));
            container.addView(list.get(position));
            return list.get(position);
        }
        public void setdata(ArrayList<String> urlList) {
            this.urlList = urlList;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        isEnable = true;
        refreshUI();
        loading(false);
        getNetworkData();
        //显示图片

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(tv_top != null){
            tv_top.stopScroll();
        }
        if(tv_top2 != null){
            tv_top2.stopScroll();
        }
    }

    // @Override
    public void refreshUI() {
        getDataSrc();
        if (menuList != null && menuList.size() > 0) {
            this.adapter.notifyDataSetChanged();
        }
        if (!PublicUtils.ISCOMBINE) {
//            updateApp();// 检测升级应用
        }
    }


    private void updateApp() {
        Intent intent = new Intent(context, DownApkBackstageService.class);
        intent.putExtra("isFromHome", true);
        context.startService(intent);
    }

    /**
     * update组织机构，机构用户，机构店面
     */
    private void updateOperation() {
        GetOrg getOrg = new GetOrg(context, null);
        if (getOrg.getOrgOperation() || getOrg.getStoreOperation() || getOrg.getUserOperation()) {
            Intent intent = new Intent(context, DialogGetOrgActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 初始化样式
     */
    private void initStyle() {
        if (StyleUtil.findImageUrlForStyle(context) != null && !StyleUtil.findImageUrlForStyle(context).isEmpty()) {
            registerStyleReceiver();
            // Intent intent = new Intent(context,
            // DialogGetStyleActivity.class);
            // startActivity(intent);
            Intent intent1 = new Intent(context, StyleDownLoadService.class);
            getActivity().startService(intent1);

            return;
        }
    }

    /**
     * 从数据库获取当前用户可以使用的所有菜单项数据，并将数据保存到menuList中
     */
    private void getDataSrc() {
        menuList = new MainMenuDB(context).findAllMenuList();
        shihuamenuList = new MenuShiHuaDB(context).findAllMenuList();
        removeMenu();
        if (shihuamenuList.size() <= 4) {
            mshihuamenuList.clear();
            for (int i = 0; i < shihuamenuList.size(); i++) {
                mshihuamenuList.add(shihuamenuList.get(i));
                Menu menu = new Menu();
                menu.setName(shihuamenuList.get(i).getFolderName());
                menu.setIcon(shihuamenuList.get(i).getIcon());
                menu.setMenuList(shihuamenuList.get(i).getMenuList());
                if("资产管理".equals(shihuamenuList.get(i).getFolderName())){
                    if(menuList.size()>4){
                        menuList.add(4,menu);
                    }else{
                        menuList.add(menu);
                    }

                }

            }
//            if (shihuamenuList.size() == 1) {
//                ShiHuaMenu menu = new ShiHuaMenu();
//                menu.setIsemptymodle(1);
//                mshihuamenuList.add(menu);
//                mshihuamenuList.add(menu);
//                mshihuamenuList.add(menu);
//            } else if (shihuamenuList.size() == 2) {
//                ShiHuaMenu menu = new ShiHuaMenu();
//                menu.setIsemptymodle(1);
//                mshihuamenuList.add(menu);
//                mshihuamenuList.add(menu);
//            } else if (shihuamenuList.size() == 3) {
//                ShiHuaMenu menu = new ShiHuaMenu();
//                menu.setIsemptymodle(1);
//                mshihuamenuList.add(menu);
//            }
//            if (mshihuamenuList.size() != 0) {
//                shiHuaAdapter = new ShiHuaAdapter(getActivity(), mshihuamenuList);
//                basic_gridview.setAdapter(shiHuaAdapter);
//            }
        }
        //如果资产管里面没有值，那么删除资产管理这个menu
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        int index = 0;
        list1.add(2031723);
        list1.add(2031726);
        list1.add(2031724);
        list1.add(2031752);
        list1.add(2031720);
        list1.add(2031727);
        for (int i=0;i<menuList.size();i++){
            if (menuList.get(i).getName().equals("资产管理")){
                index = i;
            }
            list2.add(menuList.get(i).getMenuId());
        }
        list1.retainAll(list2);
        if (list1.size() ==0 && index != 0){
            menuList.remove(index);
        }

        if (menuList != null && menuList.size() > 0) {
                while (menuList.size()>8){
                    menuList.remove(8);
                }
            adapter.setDataSrc(menuList);

        }

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


    /**
     * 启动后台服务，包括Push、定位和后台提交
     */
    private void initStartService() {
        // 开启Push服务
        context.startService(new Intent(context, GetPushService.class));

        // 开启被动定位service
        context.startService(new Intent(context, BackstageLocationService.class));

        // 启动后台提交服务
        SubmitWorkManager.getInstance(context).commit();

        // 启动守店定位服务
        AttendanceUtil.startAttendanceLocationService(context, 0);

        // 开启天气服务
        // this.startService(new Intent(this,WeatherService.class));
        // startService(new Intent(this, UploadService.class));
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            context.unregisterReceiver(slideImageReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    private void setOrder3Time() {
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

    Handler mHanlder = new Handler() {
        public void handleMessage(Message msg) {
            if (flagOrder3Time) {
                flagOrder3Time = false;
                if (task != null) {
                    task.cancel();
                }
                Date date = (Date) msg.obj;
                onItemclickMenu(menu, viewItem, date);
            }
        }

        ;
    };
    private boolean flagOrder3Time = true;
    Timer timer;
    TimerTask task = new TimerTask() {
        public void run() {
            mHanlder.sendEmptyMessage(1);
        }
    };
    private Menu menu;
    private View viewItem;
    public boolean isEnable = true;// 点击menu之后，置成false,是下边fragment不能切换，避免闪退

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        switch (parent.getId()) {
//            case R.id.basicfuncation_grid:
//                //基础功能
//                if (mshihuamenuList.get(position).getIsemptymodle() != 1) {
//                    if (mshihuamenuList.get(position).getMenuList().size() == 1 && mshihuamenuList.get(position).getMenuList().get(0).getMenuIdList().size() == 1) {
//                        isEnable = false;
//                        menu = new Menu();
//                        menu.setMenuId(Integer.parseInt(mshihuamenuList.get(position).getMenuList().get(0).getMenuIdList().get(0)));
//                        menu.setName(mshihuamenuList.get(position).getMenuList().get(0).getName());
//                        menu.setType(mshihuamenuList.get(position).getMenuList().get(0).getType());
//                        this.viewItem = view;
//                        setOrder3Time();
//                    } else {
//                        ShiHuaMenu shiHuaMenu = mshihuamenuList.get(position);
//                        Bundle bundle = new Bundle();
//                        bundle.putSerializable("shiHuaMenu", shiHuaMenu);
//                        Intent intent = new Intent(getActivity(), SmartInspectionActivity.class);
//                        intent.putExtras(bundle);
//                        startActivity(intent);
//                    }
//                }
//
//                break;
//            case R.id.gridview_home:
//                //日常功能
//                isEnable = false;
//                menu = menuList.get(position);// 获取被点击的菜单项数据
//                this.viewItem = view;
//                setOrder3Time();
//                break;
//        }
//        if(position!=7){
            Menu mu = menuList.get(position);
            if(mu.getMenuList()==null){
                isEnable = false;
                menu = menuList.get(position);// 获取被点击的菜单项数据
                this.viewItem = view;
                setOrder3Time();
            }else{
//            ShiHuaMenu shiHuaMenu = mshihuamenuList.get(position);
                ShiHuaMenu menu = new ShiHuaMenu();
                menu.setFolderName(mu.getName());
                menu.setMenuList(mu.getMenuList());
                menu.setIcon(mu.getIcon());
                Bundle bundle = new Bundle();
                bundle.putSerializable("shiHuaMenu", menu);
                Intent intent = new Intent(getActivity(), ZRModuleActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
//        }



    }

    public void onItemclickMenu(Menu menu, View view, Date date) {
        if (usableControl(menu, view, date)) {
            if (menu.getType() == Menu.WORK_PLAN) { // 打开工作计划
                Intent intent = new Intent(context, WorkPlanActivity.class);
                startActivity(intent);
                return;
            }
            if (menu.getType() == Menu.WORK_SUM) {// 打开工作总结
                Intent intent = new Intent(context, WorkSummaryMainActivity.class);
                startActivity(intent);
                return;
            }

            if (menu.getType() == Menu.TYPE_NOTICE) {// 打开公告模块
                Intent intent = new Intent(context, NotifyListActivity.class);
                startActivity(intent);
            } else if (menu.getType() == Menu.TYPE_VISIT) {// 打开访店模块 拜访
                Intent intent = new Intent(context, VisitWayActivity.class);
                intent.putExtra("menuId", menu.getMenuId());
                intent.putExtra("menuName", menu.getName());
                intent.putExtra("menuType", menu.getType());
                startActivity(intent);
            } else if (menu.getType() == Menu.TYPE_ATTENDANCE) {// 打开考勤模块
                Intent intent = new Intent(context, AttendanceActivity.class);
                startActivity(intent);
            } else if (menu.getType() == Menu.TYPE_NEW_ATTENDANCE) {// 打开考勤模块
                ToastOrder.makeText(context, "请点击底栏考勤按钮", ToastOrder.LENGTH_LONG).show();
                // Intent intent = new Intent(context,
                // AttendanceFragment.class);
                // startActivity(intent);
            } else if (menu.getType() == Menu.TYPE_BBS) {// 打开BBS模块
                Intent intent = new Intent(context, BBSMainActivity.class);
                startActivity(intent);
            } else if (menu.getType() == Menu.TYPE_TARGET) {// 打开任务模块（如果数据库中有任务的话）
                List<Task> taskList = new TaskDB(context).findAllTaskByModuleid(menu.getMenuId());
                if (taskList.isEmpty()) {
                    // 如果没有任务就不打开TaskListActivity，而是用一个Toast提示用户
                    ToastOrder.makeText(context, getResources().getString(R.string.noTask), ToastOrder.LENGTH_SHORT)
                            .show();
                } else {
                    // 如果有任务就打开TaskListActivity
                    Intent intent = new Intent(context, TaskListActivity.class);
                    intent.putExtra("moduleId", menu.getMenuId());
                    // intent.putExtra("taskName", menu.getName());
                    startActivity(intent);
                }
            } else if (menu.getType() == Menu.TYPE_REPORT) {// 打开报表模块
                Intent intent = new Intent(context, ReportActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("targetId", menu.getMenuId());
                bundle.putInt("menuType", Menu.TYPE_REPORT);
                bundle.putString("menuName", menu.getName());
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            } else if (menu.getType() == Menu.TYPE_REPORT_NEW) {// 打开新报表模块
                Intent intent = new Intent(context, ReportActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("targetId", menu.getMenuId());
                bundle.putInt("menuType", Menu.TYPE_REPORT_NEW);
                bundle.putString("menuName", menu.getName());
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            } else if (menu.getType() == Menu.TYPE_HELP) {// 打开帮助模块
                new HelpPopupWindow(context).show(null);// 基于home页的GridView组件显示弹出窗口
            } else if (menu.getType() == Menu.TYPE_ORDER2) {

                Intent intent = new Intent(context, Order2MainActivity.class);
                startActivity(intent);
            } else if (menu.getType() == Menu.TYPE_ORDER3) {// 订单
                Intent intent = new Intent(context, Order3MainActivity.class);
                intent.putExtra("menuType", menu.getType());
                intent.putExtra("targetId", menu.getMenuId());
                startActivity(intent);
            } else if (menu.getType() == Menu.TYPE_ORDER3_SEND) {// 送货
                Intent intent = new Intent(context, Order3SendActivity.class);
                startActivity(intent);
            } else if (menu.getType() == Menu.TYPE_CAR_SALES) {// 车销
                int carID = SharedPreferencesForCarSalesUtil.getInstance(context).getCarId();
                if (carID != 0) {
                    Intent intent = new Intent(context, CarSalesMainActivity.class);
                    startActivity(intent);
                } else {
                    ToastOrder.makeText(context, "该用户没配置车辆", ToastOrder.LENGTH_SHORT).show();
                }

            } else if (menu.getType() == Menu.TYPE_MANAGER) {

                Intent intent = new Intent(context, SubmitManagerActivity.class);
                startActivity(intent);
            } else if (menu.getType() == Menu.TYPE_TODO) {
                Intent intent = new Intent(context, TodoListActivity.class);
                startActivity(intent);
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
                startActivity(new Intent(context, WorkSummaryMainActivity.class));
            } else if(menu.getType()==Menu.HUI_YI){//会议日程
                Intent intent = new Intent(context, MeetingagendaListActivity.class);
                startActivity(intent);
            } else{// 打开默认模块
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
                    if (menu.getName().equals("气体采集")) {
                        TipDialog tipDialog = new TipDialog(context, R.style.CustomProgressDialogTip);
                        tipDialog.show();
                    } else {
                        if("耗材仓库管理".equals(menu.getName())){
                            Intent intent = new Intent(context, ConfirmSupLibrayActivity.class);
                            intent.putExtra("menuId", menu.getMenuId());
                            intent.putExtra("menuName", menu.getName());
                            intent.putExtra("menuType", menu.getType());
                            intent.putExtra("isNoWait", menu.getIsNoWait() == 1 ? true : false);
                            if (menu.getModuleType() != null && menu.getModuleType() == Menu.MENU_MODULE_TYPE_4) {// 表示是店面拓展模块
                                intent.putExtra("is_store_expand", 1);
                            }
                            startActivity(intent);
                        }else if("工位管理".equals(menu.getName())){
                            Intent intent = new Intent(context, StationReserveMainActivity.class);
                            startActivity(intent);
                        } else if(menuId==2032200){//访客预约
                            Intent intent = new Intent(context, VisitorListActivity.class);
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(context, ModuleActivity.class);
                            intent.putExtra("menuId", menu.getMenuId());
                            intent.putExtra("menuName", menu.getName());
                            intent.putExtra("menuType", menu.getType());
                            intent.putExtra("isNoWait", menu.getIsNoWait() == 1 ? true : false);
                            if (menu.getModuleType() != null && menu.getModuleType() == Menu.MENU_MODULE_TYPE_4) {// 表示是店面拓展模块
                                intent.putExtra("is_store_expand", 1);
                            }
                            startActivity(intent);
                        }

                    }
                }
            }

        } else {
            isEnable = true;
        }
    }

    public android.support.v7.app.AlertDialog alDialog = null;
    int menuId;
    int menuType;
    List<Submit> notSubmit;
    String menuName;
    private Submit submitCache;

    public void resetData() {
        notSubmit = new SubmitDB(context).findAllSubmitDataByState(Submit.STATE_NO_SUBMIT);// 获取未提交的数据

        if (!notSubmit.isEmpty()) {
            for (int i = 0; i < notSubmit.size(); i++) {
                if (SharedPreferencesUtil2.getInstance(context).getMenuId() == notSubmit.get(i).getMenuId()) {
                    submitCache = notSubmit.get(i);
                    break;
                }
            }
            if (submitCache != null) {
                menuId = submitCache.getMenuId();
                menuType = submitCache.getTargetType();
                menuName = submitCache.getMenuName();
                android.support.v7.app.AlertDialog.Builder alBuilder = new android.support.v7.app.AlertDialog.Builder(
                        context, R.style.NewAlertDialogStyle);
                alBuilder.setTitle(getResources().getString(R.string.WXTIP));
                alBuilder.setMessage(PublicUtils.getResourceString(context, R.string.discovry) + menuName + PublicUtils.getResourceString(context, R.string.discovry1));

                alBuilder.setPositiveButton(getResources().getString(R.string.confirm),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                alDialog.dismiss();
                                isEnable = true;

                                // int menuType =
                                // notSubmit.get(0).getMenuType();

                                Menu menu = new MainMenuDB(context).findMenuListByMenuIdAndType(menuId, menuType);
                                if (menu != null) {
                                    if (usableControl(menu, null, DateUtil.getCurrentDate())) {
                                        if (menu.getType() == Menu.TYPE_VISIT) {// 打开访店模块
                                            // 拜访
                                            Intent intent = new Intent(context, VisitWayActivity.class);
                                            intent.putExtra("menuId", menuId);
                                            intent.putExtra("menuType", menuType);
                                            intent.putExtra("menuName", menuName);
                                            startActivity(intent);
                                        } else if (menu.getType() == Menu.TYPE_TARGET) {// 打开任务模块（如果数据库中有任务的话）
                                            List<Task> taskList = new TaskDB(context)
                                                    .findAllTaskByModuleid(menu.getMenuId());
                                            if (taskList.isEmpty()) {
                                                // 如果没有任务就不打开TaskListActivity，而是用一个Toast提示用户
                                                ToastOrder.makeText(context, getResources().getString(R.string.noTask),
                                                        ToastOrder.LENGTH_SHORT).show();
                                            } else {
                                                // 如果有任务就打开TaskListActivity
                                                Intent intent = new Intent(context, TaskListActivity.class);
                                                intent.putExtra("moduleId", menu.getMenuId());
                                                intent.putExtra("menuId", menuId);
                                                intent.putExtra("menuType", menuType);
                                                intent.putExtra("menuName", menuName);
                                                // intent.putExtra("taskName",
                                                // menu.getName());
                                                startActivity(intent);
                                            }
                                        } else if (menu.getType() == Menu.TYPE_NEARBY) {// 就近拜访
                                            nearbyVisit(menu);
                                        } else if (menu.getType() == Menu.IS_STORE_ADD_MOD) {// 新店上报
                                            newStoreReport(menu);
                                        } else {// 打开默认模块
                                            if (menu.getType() == Menu.TYPE_MODULE) {// 自定义模块

                                                Intent intent = new Intent(context, ModuleActivity.class);
                                                intent.putExtra("menuId", menu.getMenuId());
                                                intent.putExtra("menuName", menu.getName());
                                                intent.putExtra("menuType", menu.getType());
                                                intent.putExtra("isCatchData", true);
                                                intent.putExtra("isNoWait", menu.getIsNoWait() == 1 ? true : false);
                                                if (menu.getModuleType() != null
                                                        && menu.getModuleType() == Menu.MENU_MODULE_TYPE_4) {// 表示是店面拓展模块
                                                    intent.putExtra("is_store_expand", 1);
                                                }

                                                startActivity(intent);

                                            }
                                        }
                                    }

                                }
                            }
                        });
                alBuilder.setNegativeButton(getResources().getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                alDialog.dismiss();
                                isEnable = true;
                                cleanDataMethodByNoSubmit();
                            }
                        });
                alDialog = alBuilder.create();
                alDialog.setCancelable(false);
                if (!TextUtils.isEmpty(menuName) && submitCache.getModType() != null && submitCache.getModType() == 1
                        || submitCache.getModType() != null && submitCache.getModType() == 2) {
                    if (SharedPreferencesUtil2.getInstance(context).getIsAnomaly()) {// 如果是异常退出
                        alDialog.show();
                    }
                } else if (!TextUtils.isEmpty(menuName) && menuType == 2) {
                    if (SharedPreferencesUtil2.getInstance(context).getIsAnomaly()) {// 如果是异常退出
                        alDialog.show();
                    }
                } else if (!TextUtils.isEmpty(menuName) && menuType == 21) {
                    if (SharedPreferencesUtil2.getInstance(context).getIsAnomaly()) {// 如果是异常退出
                        alDialog.show();
                    }
                } else {
                    cleanDataMethodByNoSubmit();
                }
            }

        } else if (!TextUtils.isEmpty(SharedPreferencesUtilForNearby.getInstance(context).getStoreOrgId())) {
            menuId = SharedPreferencesUtilForNearby.getInstance(context).getStoreMenuId();
            menuType = SharedPreferencesUtilForNearby.getInstance(context).getStoreMenuType();
            menuName = SharedPreferencesUtilForNearby.getInstance(context).getStoreMenuName();
            Menu menu = new MainMenuDB(context).findMenuListByMenuIdAndType(menuId, menuType);
            if (menu != null) {
                android.support.v7.app.AlertDialog.Builder alBuilder = new android.support.v7.app.AlertDialog.Builder(
                        context, R.style.NewAlertDialogStyle);
                alBuilder.setTitle(getResources().getString(R.string.WXTIP));
                alBuilder.setMessage(PublicUtils.getResourceString(context, R.string.discovry) + menuName + PublicUtils.getResourceString(context, R.string.discovry1));

                alBuilder.setPositiveButton(getResources().getString(R.string.confirm),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                alDialog.dismiss();
                                isEnable = true;
                                Menu menu = new MainMenuDB(context).findMenuListByMenuIdAndType(menuId, menuType);
                                if (menu != null) {
                                    if (usableControl(menu, null, DateUtil.getCurrentDate())) {
                                        if (menu.getType() == Menu.TYPE_VISIT) {// 打开访店模块
                                            // 拜访
                                            Intent intent = new Intent(context, VisitWayActivity.class);
                                            intent.putExtra("menuId", menuId);
                                            intent.putExtra("menuType", menuType);
                                            intent.putExtra("menuName", menuName);
                                            startActivity(intent);
                                        } else if (menu.getType() == Menu.TYPE_TARGET) {// 打开任务模块（如果数据库中有任务的话）
                                            List<Task> taskList = new TaskDB(context)
                                                    .findAllTaskByModuleid(menu.getMenuId());
                                            if (taskList.isEmpty()) {
                                                // 如果没有任务就不打开TaskListActivity，而是用一个Toast提示用户
                                                ToastOrder.makeText(context, getResources().getString(R.string.noTask),
                                                        ToastOrder.LENGTH_SHORT).show();
                                            } else {
                                                // 如果有任务就打开TaskListActivity
                                                Intent intent = new Intent(context, TaskListActivity.class);
                                                intent.putExtra("moduleId", menu.getMenuId());
                                                intent.putExtra("menuId", menuId);
                                                intent.putExtra("menuType", menuType);
                                                intent.putExtra("menuName", menuName);
                                                // intent.putExtra("taskName",
                                                // menu.getName());
                                                startActivity(intent);
                                            }
                                        } else if (menu.getType() == Menu.TYPE_NEARBY) {// 就近拜访
                                            nearbyVisit(menu);
                                        } else if (menu.getType() == Menu.IS_STORE_ADD_MOD) {// 新店上报
                                            newStoreReport(menu);
                                        } else {// 打开默认模块
                                            if (menu.getType() == Menu.TYPE_MODULE) {// 自定义模块

                                                Intent intent = new Intent(context, ModuleActivity.class);
                                                intent.putExtra("menuId", menu.getMenuId());
                                                intent.putExtra("menuName", menu.getName());
                                                intent.putExtra("menuType", menu.getType());
                                                intent.putExtra("isCatchData", true);
                                                intent.putExtra("isNoWait", menu.getIsNoWait() == 1 ? true : false);
                                                if (menu.getModuleType() != null
                                                        && menu.getModuleType() == Menu.MENU_MODULE_TYPE_4) {// 表示是店面拓展模块
                                                    intent.putExtra("is_store_expand", 1);
                                                }
                                                startActivity(intent);

                                            }
                                        }
                                    }

                                }
                            }
                        });
                alBuilder.setNegativeButton(getResources().getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                alDialog.dismiss();
                                isEnable = true;
                                cleanDataMethodByNoSubmit();
                            }
                        });
                alDialog = alBuilder.create();
                alDialog.setCancelable(false);
                alDialog.show();
            }

        }
    }

    /**
     * 删除数据库中未提交的数据及其相关图片文件
     */
    private void cleanDataMethodByNoSubmit() {
        SubmitItemDB submitItemDB = new SubmitItemDB(context);
        List<Submit> notSubmit = new SubmitDB(context).findAllSubmitDataByState(Submit.STATE_NO_SUBMIT);// 获取未提交的数据

        if (!notSubmit.isEmpty()) {
            for (int i = 0; i < notSubmit.size(); i++) {
                int id = notSubmit.get(i).getId();
                new SubmitDB(context).deleteSubmitById(id);// 清除未提交的数据

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
            SharedPreferencesUtilForNearby.getInstance(context).saveStoreInfoClear();
        }

        new SubmitItemTempDB(context).deleteAllSubmitItem();// 清除临时表中的数据

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
        startActivity(intent);
    }

    /**
     * 通讯录
     */
    private void addressBook() {
        Intent intent = new Intent(context, AddressBookActivity.class);
        startActivity(intent);
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
        startActivity(intent);

    }

    /**
     * 企业微信
     */
    private void weChat() {
        Intent intent = new Intent(context, WechatActivity.class);
        startActivity(intent);
    }

    /**
     * 调查问卷
     */
    private void question() {
        Intent intent = new Intent(context, QuestionnaireActivity.class);
        startActivity(intent);
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
        startActivity(intent);
    }

    /**
     * FR报表
     *
     * @param menu
     */
    private void frReport(Menu menu) {
        final Dialog dilog = new MyProgressDialog(context, R.style.CustomProgressDialog, PublicUtils.getResourceString(context, R.string.init));
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
                            startActivity(intent);
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
        startActivity(intent);
    }

    /**
     * 手机可用时间段控制
     *
     * @param menu
     * @return
     */
    private boolean usableControl(Menu menu, View view, Date date) {
        boolean flag = true;
        String time = menu.getPhoneUsableTime();
        MenuUsableControl control = new MenuUsableControl();
        // time = "12:00|0.5,14:55|0.5";
        flag = control.isCanUse(time, date);
        if (menu.getType() == Menu.TYPE_ORDER3) {
            if (flag) {// 时间过了判断天
                if (null == date) {
                    flag = false;
                    Toast.makeText(context, PublicUtils.getResourceString(context, R.string.check_net), Toast.LENGTH_SHORT).show();
                } else {
                    flag = control.isOrder3CanUse(context, date);
                    if (!flag) {
                        showTip(PublicUtils.getResourceString(context, R.string.toast_one8));
                    }
                }

            } else {
                showTip(control.tipInfo(time));
            }
        } else {
            if (!flag) {
                showTip(control.tipInfo(time));
            }
        }
        return flag;
    }

    /**
     * 手机段可是用时间段提示
     */
    private LinearLayout ll_tip;

    private void showTip(String info) {
        Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
        ll_tip = (LinearLayout) rootView.findViewById(R.id.ll_menu_usable_tip);
        TextView tv_Tip = (TextView) rootView.findViewById(R.id.tv_menu_usable_tip);
        ll_tip.setVisibility(View.VISIBLE);
        tv_Tip.setText(info);
        ll_tip.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ll_tip.setVisibility(View.GONE);
            }
        });
    }

    /***
     * 更新style广播注册
     */
    private void registerStyleReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.BROADCAST_ACTION_STYLE);
        context.registerReceiver(receiverStyle, filter);
    }

    /**
     * 更新style广播
     */
    private BroadcastReceiver receiverStyle = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
           adapter.notifyDataSetChanged();
//            if (!TextUtils.isEmpty(StyleUtil.homeStyleImg(context))) {
//                Drawable drable = Drawable
//                        .createFromPath(Constants.COMPANY_STYLE_PATH + StyleUtil.homeStyleImg(context));
//                if (drable != null) {
//                    // fl_home.setBackgroundDrawable(drable);
//                }
//            }
            context.unregisterReceiver(receiverStyle);
        }
    };

    public static final String SLIDEIMAGE_ACTION = "slideimage_down_ok";
    //下载轮播图片成功
    private BroadcastReceiver slideImageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (SLIDEIMAGE_ACTION.equals(intent.getAction())) {
                setSlideImageList();
            }
        }
    };


    /**
     * @param imageView
     * @method 显示本地图片
     */
    private void showImage(ImageView imageView, String file_path) {
        Bitmap bm = BitmapFactory.decodeFile(file_path);
        if (bm != null) {
            imageView.setImageBitmap(bm);
        } else {
            imageLoader.displayImage(SharedPreferencesUtil.getInstance(getActivity()).getHeadImage(), imageView, options, null);
        }
    }


    /**
     * 去掉menu中重复的menu  暂时这样 待修改
     */
    public void removeMenu() {
        try {
            if (shihuamenuList != null && shihuamenuList.size() > 0) {
                for (int j = 0; j < shihuamenuList.size(); j++) {
                    if (shihuamenuList.get(j).getMenuList() != null) {
                        for (int k = 0; k < shihuamenuList.get(j).getMenuList().size(); k++) {
                            if (shihuamenuList.get(j).getMenuList().get(k).getMenuIdList() != null) {
                                for (int l = 0; l < shihuamenuList.get(j).getMenuList().get(k).getMenuIdList().size(); l++) {
                                    for (int i = 0; i < menuList.size(); i++) {
                                        if ((menuList.get(i).getMenuId() + "").equals(shihuamenuList.get(j).getMenuList().get(k).getMenuIdList().get(l))) {
                                            menuList.remove(i);
                                            i--;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_scan:
                    scan();
                    break;
                case R.id.iv_add_one:
                    takePhoto();
                    break;
                case R.id.iv_show_menu:
                   showOrHideMenu();
                    break;


            }
        }
    };

    /**
     * 跳转到会议日程
     */
    private void startRC() {
        EventBus.getDefault().post(new FirstEvent("切换到会议日程!"));
    }


    /**
     * 拍一拍
     */

    private void takePhoto() {
        Intent intentPhote = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentPhote.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        String pathUrl = Constants.SDCARD_PATH + "/temp/";
        File filePreview = new File(pathUrl);
        if (!filePreview.exists()) {
            filePreview.mkdir();
        }
        File file = null;
          String  photoFileName1 = System.currentTimeMillis() + ".jpg";
            SharedPreferencesForCarSalesUtil.getInstance(context).setPhotoNameOne(photoFileName1);
            file = new File(filePreview, photoFileName1);

        Uri uri = Uri.fromFile(file);
        // 获取拍照后未压缩的原图片，并保存在uri路径中
        intentPhote.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        ((Activity) context).startActivityForResult(intentPhote, 2000);
    }

    /**
     * 扫一扫
     */
    private void scan() {
        Intent i = new Intent(getActivity(), CaptureActivity.class);
        getActivity().startActivityForResult(i, 111);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( resultCode == R.id.scan_succeeded&&requestCode ==111){//扫描返回
            String scanId = data.getStringExtra("SCAN_RESULT");
            if(!TextUtils.isEmpty(scanId)){
                if (scanId.length() != 13){
                    openMeetingRoom(scanId);
                }else{
                    requestScanCode(scanId);
                }

            }else{
                Toast.makeText(context,"扫描失败",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openMeetingRoom(String scanId) {
        int a = scanId.length();
        String macaddress = "",time_stamp = "";
        try{
            if (a<30){
                String macid_time[] = scanId.split(";");
                macaddress = macid_time[0];
                time_stamp = macid_time[1];
            }else{
                time_stamp = scanId.substring(a-13,a);
                macaddress = scanId.substring(a-26,a-14);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        ApiRequestMethods.openMeetingRoom(context, company_id, uid, macaddress, time_stamp, new ApiRequestFactory.HttpCallBackListener() {
            @Override
            public void onSuccess(String response, String url, int id) {
                try {
                    JSONObject object = new JSONObject(response).getJSONObject("data");
                    if (object.getInt("flag") == 0) {
                        Toast.makeText(context,"开门成功！",Toast.LENGTH_LONG).show();
                    }else if(object.getInt("flag") == -5){
                        Toast.makeText(context,"未找到该会议室",Toast.LENGTH_LONG).show();
                    }else if(object.getInt("flag") == -7 || object.getInt("flag") == -3){
                        Toast.makeText(context,"二维码有误或时间已过期",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(Call call, Exception e, int id) {
                Toast.makeText(context,"开门失败",Toast.LENGTH_LONG).show();
            }
        },false);
    }

    /**
     * 加载对话框
     */
    private Dialog dialogSc = null;
    private void requestScanCode(String scanId) {
        if (TextUtils.isEmpty(scanId)){
            return;
        }
        // 初始化加载对话框
        dialogSc = new MyProgressDialog(context, R.style.CustomProgressDialog,getResources().getString(R.string.activity_after_04));
        // 显示加载对话框
        dialogSc.show();
        HashMap<String ,String> map = setPageParams(scanId);

        RequestParams requestParams = new RequestParams();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            requestParams.put(entry.getKey(), entry.getValue());
            JLog.d("alin", entry.getKey() + ":" + entry.getValue());
        }
        String baseUrlSearch = UrlInfo.getUrlReplenish(context);
        GcgHttpClient.getInstance(context).post(baseUrlSearch, requestParams,new HttpResponseListener() {
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
                    JLog.d("alin", "msg.obj   ====json===>>>>>" + content);

                    fail("没有查询到相关数据!");
                    return;
                }
                JLog.d("alin", "JSON   =======>>>>>" + content);
                doInbackGround(content);

            }

        });
    }
    private HashMap<String ,String> setPageParams(String scanId) {
        HashMap<String ,String> map = new HashMap<>();
        map.put("phoneno",PublicUtils.receivePhoneNO(context));
        map.put("type",String.valueOf(Constants.MODULE_TYPE_UPDATE));
        map.put("page","1");
        map.put("taskid","2031720");
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
        public void handleMessage(Message msg) {
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
        ToastOrder.makeText(context, message, ToastOrder.LENGTH_LONG).show();
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
    private List<Map<String, String>> dataList = new ArrayList<>();
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

    private void saveInsetSubmitItem() {
        Module module = new ModuleDB(context).findModuleByTargetId(2031720,3);
        if(module!=null){
            Bundle bundle = new Bundle();
            bundle.putInt("planId", Constants.DEFAULTINT);// 计划ID
            bundle.putInt("awokeType", Constants.DEFAULTINT);// 提醒类型
            bundle.putInt("wayId", Constants.DEFAULTINT);// 线路ID
            bundle.putInt("storeId", Constants.DEFAULTINT);// 店面ID
            bundle.putInt("targetId", module.getMenuId());// 数据ID
            bundle.putInt("taskId", module.getMenuId());// 超链接sql查询的时候使用
            bundle.putString("storeName", null);// 店面名称
            bundle.putString("wayName", null);// 线路名称
            bundle.putInt("isCheckin", Constants.DEFAULTINT);// 是否要进店定位1 是 0和null否
            bundle.putInt("isCheckout", Constants.DEFAULTINT);// 是否要离店定位1 是 0和null否
            bundle.putInt("menuType", 3);// 菜单类型
            bundle.putSerializable("module", module);// 自定义模块实例
            bundle.putSerializable("list", (ArrayList) dataList);
            bundle.putInt("is_store_expand", 0);
            Intent intent = new Intent(context, ZCLRFuncActivity.class);// 跳转到上报页面
            intent.putExtra("isNoWait", true);
            bundle.putInt("menuId", module.getMenuId());
            bundle.putString("menuName", "资产录入");
            intent.putExtra("bundle", bundle);
            startActivity(intent);
        }

    }

//        /**
//         *@date2017/11/15
//         *@author zhonghuibin
//         *@description 拉取时间最近的会议日程通知
//         */
    private void loading(boolean isShow) {
        ApiRequestMethods.getMeetingschedulelist(context, ApiUrl.MEETINGSCHEDULELIST, company_id, uid, 0, new ApiRequestFactory.HttpCallBackListener() {
            @Override
            public void onSuccess(final String response, String url, int id) {
                JLog.d("meetListParams", "response:" + response.toString());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(response).getJSONObject("data");
                            if (object == null) {
                                return;
                            }
                            if (object.has("flag") && object.getInt("flag") == 0) {
                                JSONArray data = object.getJSONArray("data");
                                meeting = data.length();
//                                tv_meeting_data.setText(meeting+"");
                            } else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

            @Override
            public void failure(Call call, Exception e, int id) {
                JLog.d("meetListParams", "response:" + e.toString());
                
            }
        }, isShow);
    }

    /**
     * 比较两个时间
     *
     * @return
     */
    private boolean comTime(String dataTime,int bol) {
        boolean flag = false;
        long time = System.currentTimeMillis();
        SimpleDateFormat sdf=null;
        if(bol==1){
            sdf = new SimpleDateFormat("yyyy-MM-dd");
        }else{
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        }
        try {
            flag = sdf.parse(dataTime).before(new Date(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return flag;
    }


    private String getMeetTime(String startTime,String endTime){
        StringBuffer buffer=new StringBuffer();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM月dd日 HH:mm");
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
        try {

            Date sd = sdf.parse(startTime);
            Date ed = sdf.parse(endTime);
            buffer.append(sdf2.format(sd)).append("-").append(sdf1.format(ed));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }


    private boolean isShowing = false;

    private void showOrHideMenu() {
        //展示其他两个菜单,上移,渐变,缩放
        int addHeight = showMenu.getMeasuredHeight();
        int transV1 = (int) (addHeight + addHeight * 0.2 + 10);
        int transV2 = (int) (addHeight + addHeight * 0.2) * 2 + 10;

        PropertyValuesHolder transY1;
        PropertyValuesHolder transY2;
        PropertyValuesHolder scaleX;
        PropertyValuesHolder scaleY;
        PropertyValuesHolder transY1_;
        PropertyValuesHolder transY2_;
        PropertyValuesHolder scaleX_;
        PropertyValuesHolder scaleY_;
        PropertyValuesHolder rotation;
        PropertyValuesHolder rotation_;

//        if (up){
        transY1 = PropertyValuesHolder.ofFloat("translationY", -transV1);
        transY2 = PropertyValuesHolder.ofFloat("translationY", -transV2);
        scaleX = PropertyValuesHolder.ofFloat("scaleX", 0f, 1f);
        scaleY = PropertyValuesHolder.ofFloat("scaleY", 0f, 1f);
        transY1_ = PropertyValuesHolder.ofFloat("translationY", transV1);
        transY2_ = PropertyValuesHolder.ofFloat("translationY", transV2);
        scaleX_ = PropertyValuesHolder.ofFloat("scaleX", 1f, 0f);
        scaleY_ = PropertyValuesHolder.ofFloat("scaleY", 1f, 0f);
        rotation = PropertyValuesHolder.ofFloat("rotation", 45);
        rotation_ = PropertyValuesHolder.ofFloat("rotation", 0);
//            if (isShowing){
//                currentUp=false;
//            }else{
//                currentUp=true;
//            }
//
//        }else{
//        transY1 = PropertyValuesHolder.ofFloat("translationX", -transV1);
//        transY2 = PropertyValuesHolder.ofFloat("translationX", -transV2);
//        scaleX = PropertyValuesHolder.ofFloat("scaleX", 0f, 1f);
//        scaleY = PropertyValuesHolder.ofFloat("scaleY", 0f, 1f);
//        transY1_ = PropertyValuesHolder.ofFloat("translationX", transV1);
//        transY2_ = PropertyValuesHolder.ofFloat("translationX", transV2);
//        scaleX_ = PropertyValuesHolder.ofFloat("scaleX", 1f, 0f);
//        scaleY_ = PropertyValuesHolder.ofFloat("scaleY", 1f, 0f);
//        rotation = PropertyValuesHolder.ofFloat("rotation", -45);
//        rotation_ = PropertyValuesHolder.ofFloat("rotation", 0);
//            if (isShowing){
//                currentUp =  false;
//            }else{
//                currentUp = true;
//            }
//
//        }


        if (isShowing) {
            ObjectAnimator objectAnimator1 = ObjectAnimator.ofPropertyValuesHolder(addOne, transY1_, scaleX_, scaleY_);
            objectAnimator1.setDuration(200);
            objectAnimator1.start();
            ObjectAnimator objectAnimator2 = ObjectAnimator.ofPropertyValuesHolder(scan, transY2_, scaleX_, scaleY_);
            objectAnimator2.setDuration(200);
            objectAnimator2.start();
            ObjectAnimator objectAnimator3 = ObjectAnimator.ofPropertyValuesHolder(showMenu, rotation_);
            objectAnimator3.setDuration(200);
            objectAnimator3.start();

            objectAnimator2.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    addOne.setVisibility(View.INVISIBLE);
                    scan.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

            isShowing = false;

        } else {
            addOne.setVisibility(View.VISIBLE);
            scan.setVisibility(View.VISIBLE);
            ObjectAnimator objectAnimator1 = ObjectAnimator.ofPropertyValuesHolder(addOne, transY1, scaleX, scaleY);
            objectAnimator1.setDuration(200);
            objectAnimator1.start();
            ObjectAnimator objectAnimator2 = ObjectAnimator.ofPropertyValuesHolder(scan, transY2, scaleX, scaleY);
            objectAnimator2.setDuration(200);
            objectAnimator2.start();
            ObjectAnimator objectAnimator3 = ObjectAnimator.ofPropertyValuesHolder(showMenu, rotation);
            objectAnimator3.setDuration(200);
            objectAnimator3.start();
            isShowing = true;


        }

    }

    //  网络请求数据
    private void getNetworkData() {

        String reqUrl = PublicUtils.getBaseUrl(context) + "searchInfo.do?" ;

        GcgHttpClient.getInstance(context).get(reqUrl, getRequestParams(), new HttpResponseListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onFinish() {
            }

            @Override
            public void onSuccess(int statusCode, String content) {
                if (!TextUtils.isEmpty(content)) {
                    try {
                        JSONObject jsonObject = new JSONObject(content);
                        if (jsonObject.has("resultcode")) {
                            if ("0000".equals(jsonObject.getString("resultcode"))) {
                                if (jsonObject.has("search")) {
                                    JSONObject searchObj = jsonObject.getJSONObject("search");
                                    if (searchObj.has("searchdata")) {
                                        JSONArray searchArray = searchObj.getJSONArray("searchdata");
                                        visitor = searchArray.length();
//                                        tv_visit_data.setText(visitor+"");
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Throwable error, String content) {
            }
        });
    }
    private RequestParams getRequestParams(){
        RequestParams params = new RequestParams();
        String phone = PublicUtils.receivePhoneNO(context);
        params.put("type",3);
        params.put("page",1);
        params.put("phoneno",phone);
        params.put("taskid",2032200);
        params.put("isDoubleMain","2");
        params.put("doubleBtnType",3);
        return params;
    }

    /**
     *@date2017/10/30
     *@author zhonghuibin
     *@description 获取列表
     */
    private void BookMeetingByBuild() {
        getDetails(context,"m=Androidnews&a=getnewslist",17,0,0,new ApiRequestFactory.HttpCallBackListener() {
            @Override
            public void onSuccess(String response, String url, int id) {
                try{
                    Fristpage_bean b,a;
                    HomeBean homeBean = new Gson().fromJson(response,HomeBean.class);
                    for (int i=0;i<homeBean.getData().getData().getList().size();i++){
                        b = new Fristpage_bean();
                        b.setTitle(homeBean.getData().getData().getList().get(i).getTitle());
                        b.setContext(homeBean.getData().getData().getList().get(i).getDescription());
                        b.setWebViewUrl(homeBean.getData().getData().getList().get(i).getJumpurl());
                        b.setImageUrl(homeBean.getData().getData().getList().get(i).getCover_thumb());
                        if (homeBean.getData().getData().getList().size()==1){
                            a = new Fristpage_bean();
                            a.setTitle(homeBean.getData().getData().getList().get(i).getTitle());
                            a.setContext(homeBean.getData().getData().getList().get(i).getDescription());
                            a.setWebViewUrl(homeBean.getData().getData().getList().get(i).getJumpurl());
                            a.setImageUrl(homeBean.getData().getData().getList().get(i).getCover_thumb());
                            fristpage_been_list2.add(a);
                        }
                        fristpage_been_list2.add(b);
                    }
                    list_top = homeBean.getData().getData().getList();
                    tv_top.datas(fristpage_been_list2,R.layout.scrollviewitem).builder(tv_top.new OnItemBuilder() {
                        @Override
                        public void assemble(View view, Fristpage_bean fristpage_bean) {
                            TextView tv1 = (TextView) view.findViewById(R.id.tv1_items);
                            TextView tv2 = (TextView) view.findViewById(R.id.tv2_items);
                            ImageView iv1 = (ImageView) view.findViewById(R.id.iv_items);
                            tv1.setText(fristpage_bean.getTitle());
                            tv2.setText(fristpage_bean.getContext());
                            Glide.with(context).load(fristpage_bean.getImageUrl()).into(iv1);
                        }
                    }).listener(new VerticalMarqueeLayout.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            Intent intent = new Intent(context,HomepageWebViewActivity.class);
                            intent.putExtra("url",fristpage_been_list2.get(position).getWebViewUrl());
                            startActivity(intent);
                        }
                    }).commit();
                    tv_top.startScroll();
                    if (homeBean.getData().getFlag()==0){

                    }else if(homeBean.getData().getFlag()== -5){

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void failure(Call call, Exception e, int id) {
            }
        },false);
    }
    private void BookMeetingByBuild2() {
        getDetails(context,"m=Androidnews&a=getnewslist",16,0,0,new ApiRequestFactory.HttpCallBackListener() {
            @Override
            public void onSuccess(String response, String url, int id) {
                try{
                    HomeImageBean b;
                    HomeScrollImage homeScrollImage = new Gson().fromJson(response,HomeScrollImage.class);
                    List<HomeScrollImage.DataBeanX.DataBean.ListBean> aa = homeScrollImage.getData().getData().getList();
                    if (homeScrollImage.getData().getFlag()==0 && aa.size() >0){
                        homeImageBeenList = new ArrayList<HomeImageBean>();
                        for (int i=0;i<aa.size();i++){
                            b = new HomeImageBean();
                            b.setCover_thumb(aa.get(i).getCover_thumb());
                            b.setJumpurl(aa.get(i).getJumpurl());
                            b.setId(Integer.parseInt(aa.get(i).getId()));
                            homeImageBeenList.add(b);
                        }
                        setImageResuse(homeImageBeenList);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void failure(Call call, Exception e, int id) {
            }
        },false);
    }

    private void setImageResuse(List<HomeImageBean> homeImageBeenList) {
        for (int i=0;i<homeImageBeenList.size();i++){
            String imageurl = homeImageBeenList.get(i).getCover_thumb();
            int id = homeImageBeenList.get(i).getId();
            switch (i){
                case 0:
                    setText(iv_home_scrollview1,imageurl,id);
                    break;
                case 1:
                    setText(iv_home_scrollview2,imageurl,id);
                    break;
                case 2:
                    setText(iv_home_scrollview3,imageurl,id);
                    break;
                case 3:
                    setText(iv_home_scrollview4,imageurl,id);
                    break;
                case 4:
                    setText(iv_home_scrollview5,imageurl,id);
                    break;
                case 5:
                    setText(iv_home_scrollview6,imageurl,id);
                    break;
                case 6:
                    setText(iv_home_scrollview7,imageurl,id);
                    break;
                case 7:
                    setText(iv_home_scrollview8,imageurl,id);
                    break;
                case 8:
                    setText(iv_home_scrollview9,imageurl,id);
                    break;
                case 9:
                    setText(iv_home_scrollview10,imageurl,id);
                    break;
                case 10:
                    setText(iv_home_scrollview10,imageurl,id);
                    break;
                case 11:
                    setText(iv_home_scrollview10,imageurl,id);
                    break;
                case 12:
                    setText(iv_home_scrollview10,imageurl,id);
                    break;
                case 13:
                    setText(iv_home_scrollview10,imageurl,id);
                    break;
                case 14:
                    setText(iv_home_scrollview10,imageurl,id);
                    break;
                case 15:
                    setText(iv_home_scrollview10,imageurl,id);
                    break;
                case 16:
                    setText(iv_home_scrollview10,imageurl,id);
                    break;
                case 17:
                    setText(iv_home_scrollview10,imageurl,id);
                    break;
                default:
                    break;
            }
        }
    }

    private void setText(ImageView imageView,String imageUrl,int id){
        Glide.with(context).load(imageUrl).into(imageView);
        imageView.setVisibility(View.VISIBLE);
        serviceFormeeting(imageView);
    }
    private Intent intentimage;
    private void serviceFormeeting(ImageView imageView) {

        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.iv_home_scrollview1:
                        intentimage = new Intent(context,HomepageWebViewActivity.class);
                        intentimage.putExtra("url",homeImageBeenList.get(0).getJumpurl());
                        startActivity(intentimage);
                        break;
                    case R.id.iv_home_scrollview2:
                        intentimage = new Intent(context,HomepageWebViewActivity.class);
                        intentimage.putExtra("url",homeImageBeenList.get(1).getJumpurl());
                        startActivity(intentimage);
                        break;
                    case R.id.iv_home_scrollview3:
                        intentimage = new Intent(context,HomepageWebViewActivity.class);
                        intentimage.putExtra("url",homeImageBeenList.get(2).getJumpurl());
                        startActivity(intentimage);
                        break;
                    case R.id.iv_home_scrollview4:
                        intentimage = new Intent(context,HomepageWebViewActivity.class);
                        intentimage.putExtra("url",homeImageBeenList.get(3).getJumpurl());
                        startActivity(intentimage);
                        break;
                    case R.id.iv_home_scrollview5:
                        intentimage = new Intent(context,HomepageWebViewActivity.class);
                        intentimage.putExtra("url",homeImageBeenList.get(4).getJumpurl());
                        startActivity(intentimage);
                        break;
                    case R.id.iv_home_scrollview6:
                        intentimage = new Intent(context,HomepageWebViewActivity.class);
                        intentimage.putExtra("url",homeImageBeenList.get(5).getJumpurl());
                        startActivity(intentimage);
                        break;
                    case R.id.iv_home_scrollview7:
                        intentimage = new Intent(context,HomepageWebViewActivity.class);
                        intentimage.putExtra("url",homeImageBeenList.get(6).getJumpurl());
                        startActivity(intentimage);
                        break;
                    case R.id.iv_home_scrollview8:
                        intentimage = new Intent(context,HomepageWebViewActivity.class);
                        intentimage.putExtra("url",homeImageBeenList.get(7).getJumpurl());
                        startActivity(intentimage);
                        break;
                    case R.id.iv_home_scrollview9:
                        intentimage = new Intent(context,HomepageWebViewActivity.class);
                        intentimage.putExtra("url",homeImageBeenList.get(8).getJumpurl());
                        startActivity(intentimage);
                        break;
                    case R.id.iv_home_scrollview10:
                        intentimage = new Intent(context,HomepageWebViewActivity.class);
                        intentimage.putExtra("url",homeImageBeenList.get(9).getJumpurl());
                        startActivity(intentimage);
                        break;
                    case R.id.iv_home_scrollview11:
                        intentimage = new Intent(context,HomepageWebViewActivity.class);
                        intentimage.putExtra("url",homeImageBeenList.get(10).getJumpurl());
                        startActivity(intentimage);
                        break;
                    case R.id.iv_home_scrollview12:
                        intentimage = new Intent(context,HomepageWebViewActivity.class);
                        intentimage.putExtra("url",homeImageBeenList.get(11).getJumpurl());
                        startActivity(intentimage);
                        break;
                    case R.id.iv_home_scrollview13:
                        intentimage = new Intent(context,HomepageWebViewActivity.class);
                        intentimage.putExtra("url",homeImageBeenList.get(12).getJumpurl());
                        startActivity(intentimage);
                        break;
                    case R.id.iv_home_scrollview14:
                        intentimage = new Intent(context,HomepageWebViewActivity.class);
                        intentimage.putExtra("url",homeImageBeenList.get(13).getJumpurl());
                        startActivity(intentimage);
                        break;
                    case R.id.iv_home_scrollview15:
                        intentimage = new Intent(context,HomepageWebViewActivity.class);
                        intentimage.putExtra("url",homeImageBeenList.get(14).getJumpurl());
                        startActivity(intentimage);
                        break;
                    case R.id.iv_home_scrollview16:
                        intentimage = new Intent(context,HomepageWebViewActivity.class);
                        intentimage.putExtra("url",homeImageBeenList.get(15).getJumpurl());
                        startActivity(intentimage);
                        break;
                    case R.id.iv_home_scrollview17:
                        intentimage = new Intent(context,HomepageWebViewActivity.class);
                        intentimage.putExtra("url",homeImageBeenList.get(16).getJumpurl());
                        startActivity(intentimage);
                        break;
                    case R.id.iv_home_scrollview18:
                        intentimage = new Intent(context,HomepageWebViewActivity.class);
                        intentimage.putExtra("url",homeImageBeenList.get(17).getJumpurl());
                        startActivity(intentimage);
                        break;
                    default:
                        break;

                }
            }
        });
    }


    public static void getDetails(Context context, String url, int catid, int erjicatid, int page, final ApiRequestFactory.HttpCallBackListener httpCallBackListener,
                                  boolean isShowDialog){
        Map<String ,String> map = new HashMap<>();
        map.put("catid",catid+"");
        map.put("erjicatid",erjicatid+"");
        map.put("page",page+"");
        ApiRequestFactory.postJson2(context,url,map,httpCallBackListener,isShowDialog);
    }

    private void getMeetingcount() {
        getmeetingdata(context,"m=Androidnews&a=Meetingcount",uid,new ApiRequestFactory.HttpCallBackListener() {
            @Override
            public void onSuccess(String response, String url, int id) {
                try{
                    JSONObject object = new JSONObject(response).getJSONObject("data");
                    tv_meeting_data.setText(object.getString("data"));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void failure(Call call, Exception e, int id) {
            }
        },false);
    }
    private void getVisitorcount() {
        getmeetingdata(context,"m=Androidnews&a=Meetingcount",uid,new ApiRequestFactory.HttpCallBackListener() {
            @Override
            public void onSuccess(String response, String url, int id) {
                try{
                    JSONObject object = new JSONObject(response).getJSONObject("data");
                    tv_visit_data.setText(object.getString("data"));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void failure(Call call, Exception e, int id) {
            }
        },false);
    }
    public static void getmeetingdata(Context context, String url,  int uid, final ApiRequestFactory.HttpCallBackListener httpCallBackListener,
                                  boolean isShowDialog){
        Map<String ,String> map = new HashMap<>();
        map.put("uid",uid+"");
        ApiRequestFactory.postJson2(context,url,map,httpCallBackListener,isShowDialog);
    }
}
