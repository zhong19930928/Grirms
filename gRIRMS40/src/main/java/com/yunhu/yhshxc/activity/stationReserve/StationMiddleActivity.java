package com.yunhu.yhshxc.activity.stationReserve;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yunhu.android.pulltorefresh.library.PullToRefreshBase;
import com.yunhu.android.pulltorefresh.library.PullToRefreshListView;
import com.yunhu.android.view.loopview.LoopView;
import com.yunhu.android.view.loopview.OnItemSelectedListener;
import com.yunhu.yhshxc.MeetingAgenda.BookingMeetingRoom;
import com.yunhu.yhshxc.MeetingAgenda.bo.BuildingAndFloor;
import com.yunhu.yhshxc.MeetingAgenda.view.calendars.CaledarAdapter;
import com.yunhu.yhshxc.MeetingAgenda.view.calendars.CalendarBean;
import com.yunhu.yhshxc.MeetingAgenda.view.calendars.CalendarDateView;
import com.yunhu.yhshxc.MeetingAgenda.view.calendars.CalendarLayout;
import com.yunhu.yhshxc.MeetingAgenda.view.calendars.CalendarUtil;
import com.yunhu.yhshxc.MeetingAgenda.view.calendars.CalendarView;
import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.stationReserve.adapter.SureStationListAdapter;
import com.yunhu.yhshxc.bo.OrgUser;
import com.yunhu.yhshxc.bo.StationMapBean;
import com.yunhu.yhshxc.bo.SureStationBean;
import com.yunhu.yhshxc.core.ApiRequestFactory;
import com.yunhu.yhshxc.core.ApiRequestMethods;
import com.yunhu.yhshxc.core.ApiUrl;
import com.yunhu.yhshxc.database.OrgUserDB;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import gcg.org.debug.JLog;
import okhttp3.Call;

import static com.yunhu.yhshxc.MeetingAgenda.view.calendars.NormalUtils.px;

/**
 * 工位预约选择时间和楼层页面
 */
public class StationMiddleActivity extends Activity implements View.OnClickListener {
    private CalendarDateView mCalendarDateView;//日历选择器
    private LinearLayout stationreserve_menu;
    private TextView station_reservetime;
    private LoopView tv_select_floor, tv_select_layer;
    private ListView stationreserve_listview;
    private List<String> list_floor = new ArrayList<String>();
    private List<String> list_layer = new ArrayList<String>();
    private TextView metting_date_title;//时间显示
    private List<String> dataTimeList = new ArrayList<>();//时间选择集合
    private TextView sure_tx;
    private SureStationListAdapter listAdapter;
    private List<SureStationBean> dataList;
    private CalendarLayout calendarlayout;
    private String strFloor;//选择楼
    private String strLayer;//选择楼层
    private int floorIndex,layerIndex;
    private int company_id;//公司id
    private int uid;
    private int bId;
    private  int roleId;
    private String org_code,userName;
    private BuildingAndFloor build;
    private String startTime,endTime;
    private String gswn="A-2-006";//工位码
    private int flag;//1代表按照预定工位正常流程 2代表是从扫码页面进来的
    private TextView tx_floor_layer,confirm_btn;
    private RelativeLayout content_layout,time_layout;
    private TextView address_tx,station_type,station_num,station_bulid,station_per,per_num,per_jibie,starttime_tx;
    private ImageView sure_icon,person_icon;
    private StationMapBean stationMapBean;
    private int num=0;//点击次数
    private ImageView layer_icon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_middle);
        SharedPreferencesUtil s = SharedPreferencesUtil.getInstance(this);
        company_id = s.getCompanyId();
        uid = s.getUserId();
        userName=s.getUserName();
        bId=s.getOrgId();
        roleId=s.getRoleId();
        OrgUserDB orgUserDB=new OrgUserDB(this);
        OrgUser u = orgUserDB.findUserByUserId(uid);
        if(u!=null){
            org_code=u.getOrgCode();
        }
        initView();
        if(getIntent()!=null){
            flag=getIntent().getIntExtra("flag",-1);
            if(flag==1){
                initDateView();
                initData();
                confirm_btn.setVisibility(View.VISIBLE);
                confirm_btn.setText("下一步");
                tx_floor_layer.setVisibility(View.GONE);
                content_layout.setVisibility(View.GONE);
                sure_tx.setVisibility(View.GONE);
                sure_icon.setVisibility(View.GONE);
            }else if(flag==2){
                tv_select_floor.setVisibility(View.GONE);
                tv_select_layer.setVisibility(View.GONE);
                stationreserve_listview.setVisibility(View.GONE);
                calendarlayout.setVisibility(View.VISIBLE);
                tx_floor_layer.setVisibility(View.VISIBLE);
                confirm_btn.setVisibility(View.VISIBLE);
                confirm_btn.setText("确认预约");
                content_layout.setVisibility(View.VISIBLE);
                sure_tx.setVisibility(View.GONE);
                sure_icon.setVisibility(View.VISIBLE);
                time_layout.getLayoutParams().height=PublicUtils.convertDIP2PX(this,40);
                tx_floor_layer.setText("A座2层");
                initDateView();
            }else if(flag==3){
                initData();
                confirm_btn.setVisibility(View.VISIBLE);
                confirm_btn.setText("下一步");
                tx_floor_layer.setVisibility(View.GONE);
                content_layout.setVisibility(View.GONE);
                sure_tx.setVisibility(View.GONE);
                sure_icon.setVisibility(View.GONE);
                stationreserve_listview.setVisibility(View.GONE);
                calendarlayout.setVisibility(View.GONE);
                station_reservetime.setVisibility(View.GONE);
                layer_icon.setVisibility(View.VISIBLE);
            }
        }

    }

    private void initDateView(){
        setDateTime();
        mCalendarDateView.setAdapter(new CaledarAdapter() {
            @Override
            public View getView(View convertView, ViewGroup parentView, final CalendarBean bean) {

                if (convertView == null) {
                    convertView = LayoutInflater.from(parentView.getContext()).inflate(R.layout.item_calendar, null);
                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(px(38), px(38));
                    convertView.setLayoutParams(params);
                }

                TextView view = (TextView) convertView.findViewById(R.id.text_day_item);

                view.setText("" + bean.day);
                if (bean.mothFlag != 0) {
                    //如果非当前月显示为灰色
                    view.setTextColor(Color.GRAY);
                } else {
                    //如果为当前月的日期显示为黑色
                    view.setTextColor(Color.BLACK);
                }

                return convertView;
            }
        });
        mCalendarDateView.setIsStation(true);
        mCalendarDateView.setOnItemClickListener(new CalendarView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, CalendarBean bean) {
                String time = new StringBuffer().append(bean.year).append("年").append(getDisPlayNumber(bean.moth)).append("月").append(getDisPlayNumber(bean.day)).append("日").toString();
                if(num>=2){
                    dataTimeList.clear();
                    station_reservetime.setText("");
                    num=0;
                }
                if (dataTimeList != null && dataTimeList.size() == 0 && !comTime(time)) {
                    num++;
                    dataTimeList.add(time);
                    station_reservetime.setText(time+"-");
                } else if (dataTimeList != null && dataTimeList.size() == 1 && !comTime(time)) {
                    num++;
                    dataTimeList.add(time);
                    station_reservetime.setText(beforeTime(dataTimeList.get(0), dataTimeList.get(1)));
                }
//                metting_date_title.setText(time);
            }
        });

        mCalendarDateView.setOnGetMonthListener(new CalendarView.OnGetMonthListener() {
            @Override
            public void onMonth(CalendarBean bean) {
                String time = new StringBuffer().append(bean.year).append("年").append(getDisPlayNumber(bean.moth)).append("月").toString();
                metting_date_title.setText(time);
            }
        });


    }

    private void initData() {
        dataList=new ArrayList<>();
        list_floor.add("");
        tv_select_floor.setItems(list_floor);
        list_layer.add("");
        tv_select_layer.setItems(list_layer);
        BookingMeetingRoom.getCompanyList(this, ApiUrl.GETBUILDINGLIST, company_id, new ApiRequestFactory.HttpCallBackListener() {
            @Override
            public void onSuccess(String response, String url, int id) {
                try{
                    build = new Gson().fromJson(response,BuildingAndFloor.class);
                    if(build!=null&&build.getData().getFlag()==0&&build.getData().getData()!=null&&build.getData().getData().size()>0){
                        list_floor.clear();
                        for (int i=0;i<build.getData().getData().size();i++){
                            list_floor.add(build.getData().getData().get(i).getBuilding()+"座");
                        }
                        tv_select_floor.setItems(list_floor);
                        tv_select_floor.setCurrentPosition(0);
                        floorIndex=0;
                        setLayer();
                    }else{
                        Toast.makeText(StationMiddleActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }catch (Exception e){
                    Toast.makeText(StationMiddleActivity.this, "解析失败", Toast.LENGTH_SHORT).show();
                    finish();
                }


            }

            @Override
            public void failure(Call call, Exception e, int id) {
                Toast.makeText(StationMiddleActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
                finish();
            }
        }, true);

        tv_select_floor.setTextSize(16);
        tv_select_layer.setTextSize(16);
        tv_select_floor.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                strFloor=list_floor.get(index);
                floorIndex=index;
                mHandler.sendEmptyMessage(0);

            }
        });
        tv_select_layer.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                strLayer=list_layer.get(index);
                layerIndex=index;
            }
        });

    }

    private void initView() {
        mCalendarDateView = (CalendarDateView) findViewById(R.id.station_data_calendarDateView);
        findViewById(R.id.stationreserve_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        stationreserve_menu = (LinearLayout) findViewById(R.id.stationreserve_menu);
        station_reservetime = (TextView) findViewById(R.id.station_reservetime);
        station_reservetime.setOnClickListener(this);
        tv_select_floor = (LoopView) findViewById(R.id.tv_select_floor);
        tv_select_layer = (LoopView) findViewById(R.id.tv_select_layer);
        stationreserve_listview = (ListView) findViewById(R.id.stationreserve_listview);
//        String label = DateUtils.formatDateTime(this.getApplicationContext(), System.currentTimeMillis(),
//                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
//        stationreserve_listview.getLoadingLayoutProxy().setLastUpdatedLabel(label);
//        stationreserve_listview.setMode(PullToRefreshBase.Mode.DISABLED);
        metting_date_title = (TextView) findViewById(R.id.metting_date_title);
        sure_tx = (TextView) findViewById(R.id.sure_tx);
        sure_tx.setOnClickListener(this);
        calendarlayout= (CalendarLayout) findViewById(R.id.calendarlayout);
        tx_floor_layer= (TextView) findViewById(R.id.tx_floor_layer);
        confirm_btn= (TextView) findViewById(R.id.confirm_btn);
        confirm_btn.setOnClickListener(this);
        content_layout= (RelativeLayout) findViewById(R.id.content_layout);
        time_layout= (RelativeLayout) findViewById(R.id.time_layout);
        sure_icon= (ImageView) findViewById(R.id.sure_icon);
        sure_icon.setOnClickListener(this);
        person_icon= (ImageView) findViewById(R.id.person_icon);
        address_tx= (TextView) findViewById(R.id.address_tx);
        station_type= (TextView) findViewById(R.id.station_type);
        station_num= (TextView) findViewById(R.id.station_num);
        station_bulid= (TextView) findViewById(R.id.station_bulid);
        station_per= (TextView) findViewById(R.id.station_per);
        per_num= (TextView) findViewById(R.id.per_num);
        per_jibie= (TextView) findViewById(R.id.per_jibie);
        starttime_tx= (TextView) findViewById(R.id.starttime_tx);
        layer_icon= (ImageView) findViewById(R.id.layer_icon);
    }


    private String getDisPlayNumber(int num) {
        return num < 10 ? "0" + num : "" + num;
    }

    /**
     * 比较两个时间
     *
     * @return
     */
    private boolean comTime(String dataTime) {
        boolean flag = false;
        long time = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        String ss = sdf.format(time);
        if (sdf.format(time).equals(dataTime)) {
            flag = false;
            return flag;
        }
        try {
            flag = sdf.parse(dataTime).before(new Date(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return flag;

    }

    private String beforeTime(String time1, String time2) {
        StringBuffer buffer = new StringBuffer();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

        try {
            if (sdf.parse(time1).before(sdf.parse(time2))) {
                buffer.append(time1).append("-").append(time2);
                startTime=sdf1.format(sdf.parse(time1));
                endTime=sdf1.format(sdf.parse(time2));
            } else {
                buffer.append(time2).append("-").append(time1);
                startTime=sdf1.format(sdf.parse(time2));
                endTime=sdf1.format(sdf.parse(time1));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sure_tx:
                break;
            case R.id.station_reservetime:
                num=0;
                if(flag==1){
                    if(calendarlayout.getVisibility()==View.GONE){
                        calendarlayout.setVisibility(View.VISIBLE);
                        metting_date_title.setVisibility(View.VISIBLE);
                        stationreserve_listview.setVisibility(View.GONE);
                        dataTimeList.clear();
                        station_reservetime.setText("");
                    }else{
                        dataTimeList.clear();
                        station_reservetime.setText("");
                    }
                }else if(flag==2){
                    dataTimeList.clear();
                    station_reservetime.setText("");
                }

                break;
            case R.id.confirm_btn:
                if(flag==1) {
                    if (dataTimeList != null && dataTimeList.size() == 2) {
                        if (strLayer != null && !"".equals(strLayer)) {
                            calendarlayout.setVisibility(View.GONE);
                            metting_date_title.setVisibility(View.GONE);
                            stationreserve_listview.setVisibility(View.VISIBLE);
                            Intent intent = new Intent(this, ReserveStationActivity.class);
                            SureStationBean sureStationBean = new SureStationBean();
                            sureStationBean.setStartTime(startTime);
                            sureStationBean.setEndTime(endTime);
                            sureStationBean.setLouName(build.getData().getData().get(floorIndex).getBuilding());
                            sureStationBean.setLouId(build.getData().getData().get(floorIndex).getId());
                            sureStationBean.setCengName(build.getData().getData().get(floorIndex).getFloor().get(layerIndex - 1).getFloor());
                            sureStationBean.setCengId(build.getData().getData().get(floorIndex).getFloor().get(layerIndex - 1).getId());
                            intent.putExtra("SureStationBean", sureStationBean);
                            startActivityForResult(intent, 100);
                        } else {
                            stationreserve_listview.setVisibility(View.VISIBLE);
                            calendarlayout.setVisibility(View.GONE);
                            metting_date_title.setVisibility(View.GONE);
                            dataList.clear();
                            selectGongwei(startTime, endTime, build.getData().getData().get(floorIndex).getId());
                        }

                    } else {
                        Toast.makeText(StationMiddleActivity.this, "请选择时间", Toast.LENGTH_SHORT).show();
                    }
                }else if(flag==2){
                    if(stationMapBean!=null&&stationMapBean.getGwstye()==1&&stationMapBean.getSystate()==2){
                        submitDate(gswn,startTime,endTime,uid+"",uid+"",bId+""
                                ,stationMapBean.getLouid()+"",stationMapBean.getCengid()+"",userName,userName,org_code,bId+"",roleId+"",uid+"",company_id);
                    }
                }else if(flag==3){
                    if (strLayer != null && !"".equals(strLayer)) {
                        Intent intent = new Intent(this, ReserveStationActivity.class);
                        SureStationBean sureStationBean = new SureStationBean();
                        sureStationBean.setLouName(build.getData().getData().get(floorIndex).getBuilding());
                        sureStationBean.setLouId(build.getData().getData().get(floorIndex).getId());
                        sureStationBean.setCengName(build.getData().getData().get(floorIndex).getFloor().get(layerIndex - 1).getFloor());
                        sureStationBean.setCengId(build.getData().getData().get(floorIndex).getFloor().get(layerIndex - 1).getId());
                        intent.putExtra("SureStationBean", sureStationBean);
                        intent.putExtra("flag",3);
                        startActivityForResult(intent, 100);
                    }else{
                        Toast.makeText(StationMiddleActivity.this, "请选择楼层", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.sure_icon:
                if(dataTimeList!=null&&dataTimeList.size()==2){
                    getCodeTimeState(gswn);
                }else{
                    Toast.makeText(StationMiddleActivity.this, "请选择时间", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }


    private void setDateTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
//        Calendar calendar = new GregorianCalendar();
//        calendar.setTime(date);
//        calendar.add(calendar.DATE, 1);//把日期往后增加一天.整数往后推,负数往前移动
//        date = calendar.getTime(); //这个时间就是日期往后推一天的结果
//        String endTime=sdf.format(date);
        metting_date_title.setText(sdf.format(new Date()));

    }

    /**
     * 设置楼层值
     */
    private void setLayer(){
        if(list_layer!=null&&build.getData().getData().get(floorIndex).getFloor()!=null){
            list_layer.clear();
            for (int i=0;i<build.getData().getData().get(floorIndex).getFloor().size()+1;i++){
                if(i==0){
                    list_layer.add("");
                }else {
                    list_layer.add(build.getData().getData().get(floorIndex).getFloor().get(i-1).getFloor()+"层");
                }
            }
                if(list_layer.size()>0){
                    tv_select_layer.setItemsVisibleCount(list_layer.size());
                    tv_select_layer.setItems(list_layer);
                    tv_select_layer.setCurrentPosition(0);
                    layerIndex=0;
                }

        }else{
            list_layer.clear();
            list_layer.add("");
            tv_select_layer.setItems(list_layer);
        }

    }

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            setLayer();
        }
    };



    /**
     * 根据时间段和楼信息查询各层的剩余未预定座位
     * @param starttime
     * @param endtime
     * @param louId
     */
    private void selectGongwei(final String starttime, final String endtime, String louId){
        ApiRequestMethods.selectGongwei(this, louId,starttime,endtime, company_id, new ApiRequestFactory.HttpCallBackListener() {
            @Override
            public void onSuccess(final String response, String url, int id) {
                try {
                    JSONObject object = new JSONObject(response).getJSONObject("data");
                    if (object == null) {
                        Toast.makeText(StationMiddleActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (object.has("flag") && object.getInt("flag") == 0) {
                        JSONArray data = object.getJSONArray("data");
                        if (data != null && data.length() > 0) {
                            SureStationBean sureStationBean;
                            JSONObject jsonObject;
                            for (int i = 0; i < data.length(); i++) {
                                sureStationBean=new SureStationBean();
                                 jsonObject = data.getJSONObject(i);
                                if(jsonObject.has("num")){
                                    sureStationBean.setNums(jsonObject.getInt("num")+"");
                                }
                                if(jsonObject.has("louid")){
                                    sureStationBean.setLouId(jsonObject.getInt("louid")+"");
                                }
                                if(jsonObject.has("cengid")){
                                    sureStationBean.setCengId(jsonObject.getInt("cengid")+"");
                                }

                                if (jsonObject.has("louname")) {
                                    sureStationBean.setLouName(jsonObject.getString("louname"));
                                }
                                if (jsonObject.has("cengname")) {
                                    sureStationBean.setCengName(jsonObject.getString("cengname"));
                                }
                                sureStationBean.setStartTime(starttime);
                                sureStationBean.setEndTime(endtime);

                                dataList.add(sureStationBean);

                            }
                                    if(listAdapter==null){
                                        listAdapter=new SureStationListAdapter(StationMiddleActivity.this,dataList);
                                        stationreserve_listview.setAdapter(listAdapter);
                                    }else{
                                        listAdapter.refresh(dataList);
                                    }


                        }else{
                            if(dataList!=null){
                                dataList.clear();
                            }
                            if(listAdapter==null){
                                listAdapter=new SureStationListAdapter(StationMiddleActivity.this,dataList);
                                stationreserve_listview.setAdapter(listAdapter);
                            }else{
                                listAdapter.refresh(dataList);
                            }

                            Toast.makeText(StationMiddleActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        if(dataList!=null){
                            dataList.clear();
                        }
                        if(listAdapter==null){
                            listAdapter=new SureStationListAdapter(StationMiddleActivity.this,dataList);
                            stationreserve_listview.setAdapter(listAdapter);
                        }else{
                            listAdapter.refresh(dataList);
                        }
                        Toast.makeText(StationMiddleActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void failure(Call call, Exception e, int id) {
                Toast.makeText(StationMiddleActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
            }
        }, true);

    }


    /**
     * 预约工位
     * @param gwsn
     * @param starttime
     * @param endtime
     * @param ydpeo
     * @param ygid
     * @param bmid
     * @param louid
     * @param cengid
     * @param ydpeoname
     * @param yghaoname
     * @param data_org
     * @param data_org_new
     * @param data_role
     * @param data_auth_user
     * @param company_id
     */
    private void submitDate(String gwsn, String starttime, String endtime, String ydpeo, String ygid, String bmid, String louid, String cengid, String ydpeoname, String yghaoname
            , String data_org, String data_org_new, String data_role, String data_auth_user, int company_id) {
        ApiRequestMethods.reserveGongwei(this,  gwsn,  starttime,  endtime,  ydpeo,  ygid,  bmid,  louid,  cengid,  ydpeoname,  yghaoname
                ,  data_org,  data_org_new,  data_role,  data_auth_user,  company_id, new ApiRequestFactory.HttpCallBackListener() {
                    @Override
                    public void onSuccess(final String response, String url, int id) {
                        try {
                            JSONObject object = new JSONObject(response).getJSONObject("data");
                            if (object == null) {
                                Toast.makeText(StationMiddleActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (object.has("flag") && object.getInt("flag") == 0) {
                                Toast.makeText(StationMiddleActivity.this, "预约成功", Toast.LENGTH_SHORT).show();
                                finish();

                            }else {
                                Toast.makeText(StationMiddleActivity.this, "预约失败", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void failure(Call call, Exception e, int id) {
                        Toast.makeText(StationMiddleActivity.this, "预约失败", Toast.LENGTH_SHORT).show();
                    }
                }, true);
    }


    /**
     * 根据扫描回的工位编码和时间段查询当前工位使用状态
     * @param gswn
     */
    private void getCodeTimeState(final String gswn){
        ApiRequestMethods.getCodeTimeState(this, gswn, startTime, endTime, company_id, new ApiRequestFactory.HttpCallBackListener() {
            @Override
            public void onSuccess(String response, String url, int id) {
                try {
                    JSONObject object = new JSONObject(response).getJSONObject("data");
                    if (object == null) {
                        Toast.makeText(StationMiddleActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (object.has("flag") && object.getInt("flag") == 0) {
                        JSONArray data = object.getJSONArray("data");
                        if (data != null && data.length() > 0) {
                            JSONObject jsonObject;

                            for (int i = 0; i < data.length(); i++) {
                                stationMapBean = new StationMapBean();
                                jsonObject = data.getJSONObject(i);
                                if (jsonObject.has("id")) {
                                    stationMapBean.setId(jsonObject.getInt("id"));
                                }

                                if (jsonObject.has("gwsn")) {
                                    stationMapBean.setGwsn(jsonObject.getString("gwsn"));
                                }

                                if (jsonObject.has("company_id")) {
                                    stationMapBean.setCompany_id(jsonObject.getInt("company_id"));
                                }
                                if (jsonObject.has("ygid")) {
                                    stationMapBean.setYgid(jsonObject.getString("ygid"));
                                }

                                if (jsonObject.has("ygname")) {
                                    stationMapBean.setYgname(jsonObject.getString("ygname"));
                                }

                                if (jsonObject.has("louid")) {
                                    stationMapBean.setLouid(jsonObject.getInt("louid"));
                                }

                                if (jsonObject.has("cengid")) {
                                    stationMapBean.setCengid(jsonObject.getInt("cengid"));
                                }

                                if (jsonObject.has("bmid")) {
                                    stationMapBean.setBmid(jsonObject.getString("bmid"));
                                }

                                if (jsonObject.has("glyid")) {
                                    stationMapBean.setGlyid(jsonObject.getString("glyid"));
                                }
                                if (jsonObject.has("gwstye")) {
                                    stationMapBean.setGwstye(jsonObject.getInt("gwstye"));
                                }

                                if (jsonObject.has("fx") && !"null".equals(jsonObject.getString("fx"))) {
                                    stationMapBean.setFx(jsonObject.getInt("fx"));
                                }

                                if (jsonObject.has("fpstate")&& !"null".equals(jsonObject.getString("fpstate"))) {
                                    stationMapBean.setFpstate(jsonObject.getInt("fpstate"));
                                }
                                if (jsonObject.has("systate") && !"null".equals(jsonObject.getString("systate"))) {
                                    stationMapBean.setSystate(jsonObject.getInt("systate"));
                                }

                                if (jsonObject.has("gwxh") && !"null".equals(jsonObject.getString("gwxh"))) {
                                    stationMapBean.setGwxh(jsonObject.getInt("gwxh"));
                                }

                                if (jsonObject.has("zsx")) {
                                    stationMapBean.setxNum(jsonObject.getInt("zsx"));
                                }

                                if (jsonObject.has("zsy")) {
                                    stationMapBean.setYnum(jsonObject.getInt("zsy"));
                                }

                                address_tx.setText("北京A座2层");
                                station_type.setText(stationMapBean.getGwstye()==1?"移动工位":"固定工位");
                                station_num.setText(gswn);
                                station_bulid.setText("");
                                station_per.setText(stationMapBean.getYgname());
                                per_num.setText(stationMapBean.getYgid());
                                per_jibie.setText("");
                                starttime_tx.setText("");
                                if(stationMapBean.getGwstye()!=1){
                                    confirm_btn.setClickable(false);
                                    confirm_btn.setPressed(true);
                                }else{
                                    if(stationMapBean.getSystate()!=2){
                                        confirm_btn.setClickable(false);
                                        confirm_btn.setPressed(true);
                                    }
                                }

                            }
                        }

                    }else {
                        Toast.makeText(StationMiddleActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(Call call, Exception e, int id) {
                Toast.makeText(StationMiddleActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
            }
        }, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==Activity.RESULT_OK){
            finish();
        }
    }
}
