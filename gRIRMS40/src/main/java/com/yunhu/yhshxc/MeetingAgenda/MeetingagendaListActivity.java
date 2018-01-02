package com.yunhu.yhshxc.MeetingAgenda;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.activity.CaptureActivity;
import com.yunhu.android.pulltorefresh.library.PullToRefreshBase;
import com.yunhu.android.pulltorefresh.library.PullToRefreshListView;
import com.yunhu.yhshxc.MeetingAgenda.adapter.MeetingAgendaListAdapter;
import com.yunhu.yhshxc.MeetingAgenda.bo.MeetingAgenda;
import com.yunhu.yhshxc.MeetingAgenda.notification.Event;
import com.yunhu.yhshxc.MeetingAgenda.notification.NotificationUtils;
import com.yunhu.yhshxc.MeetingAgenda.view.AddPopWindow;
import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.application.SoftApplication;
import com.yunhu.yhshxc.core.ApiRequestFactory;
import com.yunhu.yhshxc.core.ApiRequestMethods;
import com.yunhu.yhshxc.core.ApiUrl;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.widget.ToastOrder;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gcg.org.debug.JLog;
import okhttp3.Call;

/**
 * 会议日程列表页面
 */
public class MeetingagendaListActivity extends Activity implements View.OnClickListener {

    private AddPopWindow addPopWindow;

    private RelativeLayout meeting_back, add_layout;

    private PullToRefreshListView shecMeetingag_list;//未完成列表
    private PullToRefreshListView finishMeetingag_list;//已完成列表
    private View popView;
    private int company_id, uid;
    private int page = 0,fPage=0;
    private List<MeetingAgenda> completeDataList=new ArrayList<>();//已完成列表
    private List<MeetingAgenda> nocompleteDataList=new ArrayList<>();//未完成列表
    private MeetingAgendaListAdapter nocompleteAdapter;
    private MeetingAgendaListAdapter completeAdapter;
    public static final int UPDATENUM = 100;
    private LinearLayout empty_layout;
    private TextView title;
    /**
     * 会议日程提醒时间标示   0=>"无",1=>"会议开始时",2=>"提前5分钟",3=>"提前15分钟",4=>"提前30分钟",5=>"提前一小时",6=>"提前一天"
     */
    private static final  int REMINDIDZ=0;
    private static final  int REMINDIDO=1;
    private static final  int REMINDIDT=2;
    private static final  int REMINDIDTE=3;
    private static final  int REMINDIDF=4;
    private static final  int REMINDIDFI=5;
    private static final  int REMINDIDS=6;
    private int itemPosition,itemIsFinishFlag;
    private LinearLayout over_layout;
    private TextView tv_unfinish,tv_finish,tv_unfinish_line,tv_finish_line;
    private LinearLayout title_layout,ll_line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetingagenda_list);
        SharedPreferencesUtil s = SharedPreferencesUtil.getInstance(this);
        company_id = s.getCompanyId();
        uid = s.getUserId();
        SoftApplication.getInstance().setMeetHandler(mHandler);
        initView();

        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                scheduleListloading(true,true);
            }
        });

        shecMeetingag_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 0;
                scheduleListloading(true,false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                scheduleListloading(true,false);
            }
        });
        finishMeetingag_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                fPage = 0;
                finishListloading(true,false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                finishListloading(true,false);
            }
        });
        shecMeetingag_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemIsFinishFlag=1;
                if (nocompleteDataList.get(i-1).getType()==2){
                    itemPosition=i-1;
                    Intent intent = new Intent(MeetingagendaListActivity.this, MeetingRoomDetails.class);
                    intent.putExtra("meetingagenda",nocompleteDataList.get(i-1));
                    intent.putExtra("itemIsFinishFlag",itemIsFinishFlag);
                    startActivityForResult(intent,100);
                }else{
                    itemPosition=i-1;
                    Intent intent2 = new Intent(MeetingagendaListActivity.this, AgendaDetailsActivity.class);
                    intent2.putExtra("meetingagenda",nocompleteDataList.get(i-1));
                    intent2.putExtra("itemIsFinishFlag",itemIsFinishFlag);
                    startActivityForResult(intent2,100);
                }
            }
        });
        finishMeetingag_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemIsFinishFlag=2;
                if (completeDataList.get(i-1).getType()==2){
                    itemPosition=i-1;
                    Intent intent = new Intent(MeetingagendaListActivity.this, MeetingRoomDetails.class);
                    intent.putExtra("meetingagenda",completeDataList.get(i-1));
                    intent.putExtra("itemIsFinishFlag",itemIsFinishFlag);
                    startActivityForResult(intent,100);
                }else{
                    itemPosition=i-1;
                    Intent intent2 = new Intent(MeetingagendaListActivity.this, AgendaDetailsActivity.class);
                    intent2.putExtra("meetingagenda",completeDataList.get(i-1));
                    intent2.putExtra("itemIsFinishFlag",itemIsFinishFlag);
                    startActivityForResult(intent2,100);
                }
            }
        });
    }



    private void initView() {
        title= (TextView) findViewById(R.id.title);
        empty_layout = (LinearLayout) findViewById(R.id.empty_layout);
        over_layout = (LinearLayout) findViewById(R.id.over_layout);
        meeting_back = (RelativeLayout) findViewById(R.id.meeting_back);
        tv_unfinish = (TextView) findViewById(R.id.tv_unfinish);
        tv_unfinish_line = (TextView) findViewById(R.id.tv_unfinish_line);
        tv_finish_line = (TextView) findViewById(R.id.tv_finish_line);
        tv_unfinish.setOnClickListener(this);
        tv_finish = (TextView) findViewById(R.id.tv_finish);
        tv_finish.setOnClickListener(this);
        meeting_back.setOnClickListener(this);
        add_layout = (RelativeLayout) findViewById(R.id.add_layout);
        title_layout = (LinearLayout) findViewById(R.id.title_layout);
        ll_line = (LinearLayout) findViewById(R.id.ll_line);
        add_layout.setOnClickListener(this);
        shecMeetingag_list = (PullToRefreshListView) findViewById(R.id.meetingag_list);
        finishMeetingag_list = (PullToRefreshListView) findViewById(R.id.over_listview);
        String label = DateUtils.formatDateTime(this.getApplicationContext(), System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
        shecMeetingag_list.getLoadingLayoutProxy().setLastUpdatedLabel(label);
        shecMeetingag_list.setMode(PullToRefreshBase.Mode.BOTH);
        finishMeetingag_list.getLoadingLayoutProxy().setLastUpdatedLabel(label);
        finishMeetingag_list.setMode(PullToRefreshBase.Mode.BOTH);
        registerTypeReceiver();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.meeting_back:
                finish();
                break;
            case R.id.add_layout:
                showPop();
                break;
            case R.id.tv_unfinish:
                tv_unfinish_line.setBackgroundResource(R.color.app_color);
                tv_finish_line.setBackgroundResource(R.color.white);
                shecMeetingag_list.setVisibility(View.VISIBLE);
                over_layout.setVisibility(View.GONE);
                break;
            case R.id.tv_finish:
                tv_unfinish_line.setBackgroundResource(R.color.white);
                tv_finish_line.setBackgroundResource(R.color.app_color);
                shecMeetingag_list.setVisibility(View.GONE);
                over_layout.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void showPop() {
        if (addPopWindow == null) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            popView = inflater.inflate(R.layout.addpopup_window, null);
            addPopWindow = new AddPopWindow(this, popView, PublicUtils.convertDIP2PX(this,151));
            addPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    backgroundAlpha(1.0f);
                }
            });
        }
        TextView order_time_meeting = (TextView) popView.findViewById(R.id.order_time_meeting);
        order_time_meeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MeetingagendaListActivity.this, MeeTingDateActivity.class);
                startActivityForResult(intent,REMINDIDO);
                addPopWindow.dismiss();
            }
        });
        TextView order_meeting = (TextView) popView.findViewById(R.id.order_meeting);
        order_meeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MeetingagendaListActivity.this, BookingMeetingRoom.class));
                addPopWindow.dismiss();
            }
        });
        TextView order_ag = (TextView) popView.findViewById(R.id.order_ag);
        order_ag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MeetingagendaListActivity.this, BuildingNewAgenda.class));
                addPopWindow.dismiss();
            }
        });
        TextView order_room = (TextView) popView.findViewById(R.id.order_room);
        order_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPopWindow.dismiss();
            }
        });
        TextView order_detail_room = (TextView) popView.findViewById(R.id.order_detail_room);
        order_detail_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MeetingagendaListActivity.this, CaptureActivity.class);
                startActivityForResult(i, 101);
                addPopWindow.dismiss();
            }
        });
        if (!addPopWindow.isShowing()) {
            addPopWindow.showPopupWindow(title);
            backgroundAlpha(0.5f);
        }
    }

    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }


    /**
     * 未完成列表
     * @param isShow
     */
    private void scheduleListloading(boolean isShow,final  boolean isLoadingFinish) {
        ApiRequestMethods.getMeetingschedulelist(this, ApiUrl.MEETINGSCHEDULELIST, company_id, uid, page, new ApiRequestFactory.HttpCallBackListener() {
            @Override
            public void onSuccess(final String response, String url, int id) {
                JLog.d("meetListParams", "response:" + response.toString());
                MeetingagendaListActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        schPaseData(response,isLoadingFinish);
                    }
                });

            }

            @Override
            public void failure(Call call, Exception e, int id) {
                JLog.d("meetListParams", "response:" + e.toString());
                shecMeetingag_list.onRefreshComplete();
                Toast.makeText(MeetingagendaListActivity.this, "未完成列表查询失败", Toast.LENGTH_SHORT).show();
                if(isLoadingFinish){
                    finishListloading(true,isLoadingFinish);
                }
            }
        }, isShow);
    }

    /**
     * 已完成列表
     * @param isShow
     */
    private void finishListloading(boolean isShow,final  boolean isLoadingFinish) {
        ApiRequestMethods.getMeetingschedulelist(this, ApiUrl.MEETINGFINISHELIST, company_id, uid, fPage, new ApiRequestFactory.HttpCallBackListener() {
            @Override
            public void onSuccess(final String response, String url, int id) {
                JLog.d("meetListParams", "response:" + response.toString());
                MeetingagendaListActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finishPaseData(response,isLoadingFinish);
                    }
                });

            }

            @Override
            public void failure(Call call, Exception e, int id) {
                JLog.d("meetListParams", "response:" + e.toString());
                finishMeetingag_list.onRefreshComplete();
                Toast.makeText(MeetingagendaListActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
                if(isLoadingFinish){
                    if(completeDataList!=null&&nocompleteDataList!=null&&completeDataList.size()==0&&nocompleteDataList.size()==0){
                        title_layout.setVisibility(View.GONE);
                        ll_line.setVisibility(View.GONE);
                        over_layout.setVisibility(View.GONE);
                        shecMeetingag_list.setVisibility(View.GONE);
                        empty_layout.setVisibility(View.VISIBLE);
                    }else{
                        title_layout.setVisibility(View.VISIBLE);
                        ll_line.setVisibility(View.VISIBLE);
                    }
                }
            }
        }, isShow);
    }


    /**
     * 未完成解析
     * @param response
     */
    private void schPaseData(String response,boolean isLoadingFinish) {
        try {
            shecMeetingag_list.onRefreshComplete();
            JSONObject object = new JSONObject(response).getJSONObject("data");
            if (object == null) {
                if(isLoadingFinish){
                    finishListloading(true,isLoadingFinish);
                }
                return;
            }
            if (object.has("flag") && object.getInt("flag") == 0) {
                JSONArray data = object.getJSONArray("data");
                if (data != null && data.length() > 0) {
                    if (page == 0&&nocompleteDataList!=null) {
                        nocompleteDataList.clear();
                    }
                    page++;
                    MeetingAgenda meetagenda;
                    for (int i = 0; i < data.length(); i++) {
                        meetagenda = new MeetingAgenda();
                        JSONObject jsonObject = data.getJSONObject(i);
                        if (jsonObject.has("id")) {
                            meetagenda.setId(jsonObject.getInt("id"));
                        }
                        if (jsonObject.has("uid")) {
                            meetagenda.setUid(jsonObject.getInt("uid"));
                        }
                        if (jsonObject.has("uname")) {
                            meetagenda.setUname(jsonObject.getString("uname"));
                        }
                        if (jsonObject.has("info")) {
                            meetagenda.setInfo(jsonObject.getString("info"));
                        }
                        if (jsonObject.has("dataday")) {
                            meetagenda.setDataDay(jsonObject.getString("dataday"));
                        }
                        if (jsonObject.has("starttime")) {
                            meetagenda.setStartTime(jsonObject.getString("starttime"));
                        }
                        if (jsonObject.has("endtime")) {
                            meetagenda.setEndTime(jsonObject.getString("endtime"));
                        }
                        if (jsonObject.has("type")) {
                            meetagenda.setType(jsonObject.getInt("type"));
                        }

                        if (jsonObject.has("fquid")) {
                            meetagenda.setFquid(jsonObject.getInt("fquid"));
                        }
                        if (jsonObject.has("fquname")) {
                            meetagenda.setFquname(jsonObject.getString("fquname"));
                        }
                        if (jsonObject.has("addres")) {
                            meetagenda.setAddres(jsonObject.getString("addres"));
                        }
                        if (jsonObject.has("huiyi_type")) {
                            meetagenda.setHuiYiType(jsonObject.getInt("huiyi_type"));
                        }
                        if (jsonObject.has("remindid") && jsonObject.getString("remindid") != null && !"null".equals(jsonObject.getString("remindid"))) {
                            meetagenda.setRemindid(jsonObject.getInt("remindid"));
                            meetagenda.setRemindingTime(getRemindTime(jsonObject.getInt("remindid"), meetagenda.getStartTime()));
                        } else {
                            meetagenda.setRemindid(0);
                        }
                        if (jsonObject.has("huiyiid") && jsonObject.getString("huiyiid") != null && !"null".equals(jsonObject.getString("huiyiid"))) {
                            meetagenda.setHuiYiId(jsonObject.getInt("huiyiid"));
                        }
                        nocompleteDataList.add(meetagenda);
                    }
                } else {
                    ToastOrder.makeText(MeetingagendaListActivity.this, R.string.search_no_data, ToastOrder.LENGTH_SHORT).show();
                }

            } else {
                ToastOrder.makeText(MeetingagendaListActivity.this, R.string.search_no_data, ToastOrder.LENGTH_SHORT).show();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (nocompleteAdapter == null) {
            nocompleteAdapter = new MeetingAgendaListAdapter(this, nocompleteDataList,uid,false);
            shecMeetingag_list.setAdapter(nocompleteAdapter);
        } else {
            nocompleteAdapter.refresh(nocompleteDataList);
        }
        if(isLoadingFinish){
            if(nocompleteDataList.size()>0){
                empty_layout.setVisibility(View.GONE);
                title_layout.setVisibility(View.VISIBLE);
                ll_line.setVisibility(View.VISIBLE);
                shecMeetingag_list.setVisibility(View.VISIBLE);
            }
            finishListloading(true,isLoadingFinish);
        }else{
            if(nocompleteDataList.size()>0){
                empty_layout.setVisibility(View.GONE);
                title_layout.setVisibility(View.VISIBLE);
                ll_line.setVisibility(View.VISIBLE);
                shecMeetingag_list.setVisibility(View.VISIBLE);
            }
        }
    }
    /**
     * 已完成解析
     * @param response
     */
    private void finishPaseData(String response,boolean isLoadingFinish) {
        try {
            finishMeetingag_list.onRefreshComplete();
            JSONObject object = new JSONObject(response).getJSONObject("data");
            if (object == null) {
                if(isLoadingFinish){
                    if(nocompleteDataList.size()==0){
                        title_layout.setVisibility(View.GONE);
                        ll_line.setVisibility(View.GONE);
                        over_layout.setVisibility(View.GONE);
                        shecMeetingag_list.setVisibility(View.GONE);
                        empty_layout.setVisibility(View.VISIBLE);
                    }
                }
                return;
            }
            if (object.has("flag") && object.getInt("flag") == 0) {
                JSONArray data = object.getJSONArray("data");
                if (data != null && data.length() > 0) {
                    if (fPage == 0&&completeDataList!=null) {
                        completeDataList.clear();
                    }
                    fPage++;
                    MeetingAgenda meetagenda;
                    for (int i = 0; i < data.length(); i++) {
                        meetagenda = new MeetingAgenda();
                        JSONObject jsonObject = data.getJSONObject(i);
                        if (jsonObject.has("id")) {
                            meetagenda.setId(jsonObject.getInt("id"));
                        }
                        if (jsonObject.has("uid")) {
                            meetagenda.setUid(jsonObject.getInt("uid"));
                        }
                        if (jsonObject.has("uname")) {
                            meetagenda.setUname(jsonObject.getString("uname"));
                        }
                        if (jsonObject.has("info")) {
                            meetagenda.setInfo(jsonObject.getString("info"));
                        }
                        if (jsonObject.has("dataday")) {
                            meetagenda.setDataDay(jsonObject.getString("dataday"));
                        }
                        if (jsonObject.has("starttime")) {
                            meetagenda.setStartTime(jsonObject.getString("starttime"));
                        }
                        if (jsonObject.has("endtime")) {
                            meetagenda.setEndTime(jsonObject.getString("endtime"));
                        }
                        if (jsonObject.has("type")) {
                            meetagenda.setType(jsonObject.getInt("type"));
                        }

                        if (jsonObject.has("fquid")) {
                            meetagenda.setFquid(jsonObject.getInt("fquid"));
                        }
                        if (jsonObject.has("fquname")) {
                            meetagenda.setFquname(jsonObject.getString("fquname"));
                        }
                        if (jsonObject.has("addres")) {
                            meetagenda.setAddres(jsonObject.getString("addres"));
                        }
                        if (jsonObject.has("huiyi_type")) {
                            meetagenda.setHuiYiType(jsonObject.getInt("huiyi_type"));
                        }
                        if (jsonObject.has("remindid") && jsonObject.getString("remindid") != null && !"null".equals(jsonObject.getString("remindid"))) {
                            meetagenda.setRemindid(jsonObject.getInt("remindid"));
                            meetagenda.setRemindingTime(getRemindTime(jsonObject.getInt("remindid"), meetagenda.getStartTime()));
                        } else {
                            meetagenda.setRemindid(0);
                        }
                        if (jsonObject.has("huiyiid") && jsonObject.getString("huiyiid") != null && !"null".equals(jsonObject.getString("huiyiid"))) {
                            meetagenda.setHuiYiId(jsonObject.getInt("huiyiid"));
                        }
                        completeDataList.add(meetagenda);
                    }
                } else {
                    ToastOrder.makeText(MeetingagendaListActivity.this, R.string.search_no_data, ToastOrder.LENGTH_SHORT).show();
                }

            } else {
                if (fPage==0&&completeDataList!=null&&completeDataList.size()>0){
                    completeDataList.clear();
                    if (completeAdapter == null) {
                        completeAdapter = new MeetingAgendaListAdapter(this, completeDataList,uid,true);
                        finishMeetingag_list.setAdapter(completeAdapter);
                    } else {
                        completeAdapter.refresh(completeDataList);
                    }
                }
                ToastOrder.makeText(MeetingagendaListActivity.this, R.string.search_no_data, ToastOrder.LENGTH_SHORT).show();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (completeAdapter == null) {
            completeAdapter = new MeetingAgendaListAdapter(this, completeDataList,uid,true);
            finishMeetingag_list.setAdapter(completeAdapter);
        } else {
            completeAdapter.refresh(completeDataList);
        }
        if(isLoadingFinish){
            if(completeDataList.size()==0&&nocompleteDataList.size()==0){
                title_layout.setVisibility(View.GONE);
                ll_line.setVisibility(View.GONE);
                over_layout.setVisibility(View.GONE);
                shecMeetingag_list.setVisibility(View.GONE);
                empty_layout.setVisibility(View.VISIBLE);
            }else{
                title_layout.setVisibility(View.VISIBLE);
                ll_line.setVisibility(View.VISIBLE);
            }
        }else{
            if(completeDataList.size()>0){
                empty_layout.setVisibility(View.GONE);
                title_layout.setVisibility(View.VISIBLE);
                ll_line.setVisibility(View.VISIBLE);
                over_layout.setVisibility(View.VISIBLE);
            }
        }
    }


    private String getRemindTime(int remindId, String startTime) {
        String remindTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long tt = 0;
        if (remindId == REMINDIDZ) {
            return remindTime;
        } else if (remindId == REMINDIDO) {
            return startTime;
        } else if (remindId == REMINDIDT) {
            tt = 5 * 60 * 1000;
        } else if (remindId == REMINDIDTE) {
            tt = 15 * 60 * 1000;
        } else if (remindId == REMINDIDF) {
            tt = 30 * 60 * 1000;
        } else if (remindId == REMINDIDFI) {
            tt = 60 * 60 * 1000;
        } else if (remindId == REMINDIDS) {
            tt = 24 * 60 * 60 * 1000;
        }
        try {
            long time = sdf.parse(startTime).getTime() - tt;
            Date d = new Date(time);
            remindTime = sdf.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return remindTime;
    }


    /**
     * 接受通知栏更改状态的广播
     */
    private BroadcastReceiver updateTypeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int type = intent.getIntExtra("type", -1);
            Event event = (Event) intent.getSerializableExtra("event");
            if (nocompleteDataList != null && nocompleteDataList.size() > 0 && nocompleteAdapter != null) {
                for (int i = 0; i < nocompleteDataList.size(); i++) {
                    if (nocompleteDataList.get(i).getHuiYiId() == event.getMeetingID()&&type==5) {
                        nocompleteDataList.remove(i);
                        nocompleteAdapter.refresh(nocompleteDataList);
                    }else if(nocompleteDataList.get(i).getHuiYiId() == event.getMeetingID()&&(type==2||type==3)&&nocompleteDataList.get(i).getFquid()!=uid){
                        nocompleteDataList.get(i).setHuiYiType(type);
                        nocompleteAdapter.refresh(nocompleteDataList);
                    }
                }
            }

        }
    };

    /***
     * type广播注册
     */
    private void registerTypeReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(NotificationUtils.DELAY);
        filter.addAction(NotificationUtils.OK);
        filter.addAction(NotificationUtils.CANCEL);
        registerReceiver(updateTypeReceiver, filter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (updateTypeReceiver != null) {
            unregisterReceiver(updateTypeReceiver);
        }
        mHandler.removeMessages(UPDATENUM);
        SoftApplication.getInstance().setMeetHandler(null);
        if(completeDataList!=null){
            completeDataList.clear();
            completeDataList=null;
        }
        if(nocompleteDataList!=null){
            nocompleteDataList.clear();
            nocompleteDataList=null;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATENUM:
                    page = 0;
                    scheduleListloading(false,false);
                    break;
            }
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REMINDIDO:
                if(data!=null){
                    Intent intent=new Intent(MeetingagendaListActivity.this,BookingMeetingRoom.class);
                    intent.putExtra("time",data.getStringExtra("time"));
                    startActivity(intent);
                }
                break;
            case 100:
                if(resultCode==Activity.RESULT_OK&&data!=null){
                    if(itemIsFinishFlag==1){
                        int type=data.getIntExtra("type",-1);
                        if(type==2||type==3){
                            nocompleteDataList.get(itemPosition).setHuiYiType(type);
                            nocompleteAdapter.refresh(nocompleteDataList);
                        }else if(type==5){
                            nocompleteDataList.get(itemPosition).setHuiYiType(type);
                            completeDataList.add(nocompleteDataList.get(itemPosition));
                            if(completeAdapter==null){
                                completeAdapter = new MeetingAgendaListAdapter(this, completeDataList,uid,true);
                                finishMeetingag_list.setAdapter(completeAdapter);
                            }else{
                                completeAdapter.refresh(completeDataList);
                            }
                            nocompleteDataList.remove(itemPosition);
                            nocompleteAdapter.refresh(nocompleteDataList);
                        }

                    }
                }
                break;
            case 101:
                if(data!=null&&resultCode==Activity.RESULT_OK){
                    String scanId = data.getStringExtra("SCAN_RESULT");
                    if (!TextUtils.isEmpty(scanId)) {
                        Toast.makeText(MeetingagendaListActivity.this, "扫描成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MeetingagendaListActivity.this, "扫描失败", Toast.LENGTH_SHORT).show();
                    }
                }

                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void first(FirstEvent event){
        page = 0;
        scheduleListloading(false,false);
    }
}
