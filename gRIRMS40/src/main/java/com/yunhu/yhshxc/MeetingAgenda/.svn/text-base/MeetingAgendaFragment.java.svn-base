package com.yunhu.yhshxc.MeetingAgenda;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;

/**
 * ＠author zhonghuibin
 * create at 2017/11/15.
 * describe 会议日程页面
 */
public class MeetingAgendaFragment extends Fragment implements View.OnClickListener {
    private AddPopWindow addPopWindow;
    private RelativeLayout meeting_back, add_layout;
    private PullToRefreshListView meetingag_list;
    private View popView;
    private int company_id, uid;
    private int page = 0;
    private List<MeetingAgenda> dataList;
    private List<MeetingAgenda> completeDataList=new ArrayList<>();//已完成列表
    private List<MeetingAgenda> nocompleteDataList=new ArrayList<>();//未// 完成列表
    private MeetingAgendaListAdapter adapter;
    public static final int UPDATENUM = 100;
    private LinearLayout empty_layout;
    private TextView title;
    private View rootView;
    private Context context;
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
    private static final  int CAMERAIDBOOK=7;
    private static final  int CAMERAIDDETAIL=8;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        SharedPreferencesUtil s = SharedPreferencesUtil.getInstance(context);
        company_id = s.getCompanyId();
        uid = s.getUserId();
        SoftApplication.getInstance().setMeetHandler(mHandler);
        if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.activity_meetingagenda_list, container, false);
            initView(rootView);
        }

        dataList = new ArrayList<>();
        loading(true);

        meetingag_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 0;
                loading(true);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loading(true);
            }
        });
        meetingag_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (dataList.get(i-1).getType()==2){
                    Intent intent = new Intent(context, MeetingRoomDetails.class);
                    intent.putExtra("meetingId",dataList.get(i-1).getHuiYiId());
                    startActivity(intent);
                }else{
                    Intent intent2 = new Intent(context, AgendaDetailsActivity.class);
                    intent2.putExtra("meetingId",dataList.get(i-1).getHuiYiId());
                    startActivity(intent2);
                }
            }
        });
        return rootView;
    }

    private void initView(View rootView) {
        title= (TextView) rootView.findViewById(R.id.title);
        empty_layout = (LinearLayout) rootView.findViewById(R.id.empty_layout);
        meeting_back = (RelativeLayout) rootView.findViewById(R.id.meeting_back);
        meeting_back.setOnClickListener(this);
        add_layout = (RelativeLayout) rootView.findViewById(R.id.add_layout);
        add_layout.setOnClickListener(this);
        meetingag_list = (PullToRefreshListView) rootView.findViewById(R.id.meetingag_list);
        String label = DateUtils.formatDateTime(context.getApplicationContext(), System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
        meetingag_list.getLoadingLayoutProxy().setLastUpdatedLabel(label);
        meetingag_list.setMode(PullToRefreshBase.Mode.BOTH);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.meeting_back:
                break;
            case R.id.add_layout:
                showPop();
                break;
        }
    }

    private void showPop() {
        if (addPopWindow == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            popView = inflater.inflate(R.layout.addpopup_window, null);
//            addPopWindow = new AddPopWindow(getActivity(), popView,LinearLayout.LayoutParams.WRAP_CONTENT);
            addPopWindow = new AddPopWindow(getActivity(), popView, PublicUtils.convertDIP2PX(context,151));
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
                Intent intent=new Intent(context, MeeTingDateActivity.class);
                startActivityForResult(intent,REMINDIDO);
                addPopWindow.dismiss();
            }
        });
        TextView order_meeting = (TextView) popView.findViewById(R.id.order_meeting);
        order_meeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, BookingMeetingRoom.class));
                addPopWindow.dismiss();
            }
        });
        TextView order_ag = (TextView) popView.findViewById(R.id.order_ag);
        order_ag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, BuildingNewAgenda.class));
                addPopWindow.dismiss();
            }
        });
        TextView order_room = (TextView) popView.findViewById(R.id.order_room);
        order_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, CaptureActivity.class);
                startActivityForResult(i, CAMERAIDBOOK);
                addPopWindow.dismiss();
            }
        });
        TextView order_detail_room = (TextView) popView.findViewById(R.id.order_detail_room);
        order_detail_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, CaptureActivity.class);
                startActivityForResult(i, CAMERAIDDETAIL);
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
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }

    private void loading(boolean isShow) {
        ApiRequestMethods.getMeetingschedulelist(context, ApiUrl.MEETINGSCHEDULELIST, company_id, uid, page, new ApiRequestFactory.HttpCallBackListener() {
            @Override
            public void onSuccess(final String response, String url, int id) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        paseData(response);
                    }
                });

            }

            @Override
            public void failure(Call call, Exception e, int id) {
                meetingag_list.onRefreshComplete();
                Toast.makeText(context, "查询失败", Toast.LENGTH_SHORT).show();
            }
        }, isShow);
    }



    private void paseData(String response) {
        try {
            meetingag_list.onRefreshComplete();
            JSONObject object = new JSONObject(response).getJSONObject("data");
            if (object == null) {
                meetingag_list.setVisibility(View.GONE);
                empty_layout.setVisibility(View.VISIBLE);
                return;
            }
            if (object.has("flag") && object.getInt("flag") == 0) {
                JSONArray data = object.getJSONArray("data");
                if (data != null && data.length() > 0) {
                    if (page == 0) {
                        dataList.clear();
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
                        dataList.add(meetagenda);
                    }
                } else {
                    ToastOrder.makeText(context, R.string.search_no_data, ToastOrder.LENGTH_SHORT).show();
                }

            } else {
                ToastOrder.makeText(context, R.string.search_no_data, ToastOrder.LENGTH_SHORT).show();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (adapter == null) {
            adapter = new MeetingAgendaListAdapter(context, dataList,uid,false);
            meetingag_list.setAdapter(adapter);
        } else {
            adapter.refresh(dataList);
        }
        if (dataList.size() > 0) {
            meetingag_list.setVisibility(View.VISIBLE);
            empty_layout.setVisibility(View.GONE);
        } else {
            meetingag_list.setVisibility(View.GONE);
            empty_layout.setVisibility(View.VISIBLE);
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
            ToastOrder.makeText(context, "第" + (1) + "条数据===" + event.getContent() + "点击了" + (type == 4 ? "拒绝" : "接受和暂缓"), ToastOrder.LENGTH_SHORT).show();
            if (dataList != null && dataList.size() > 0 && adapter != null) {
                for (int i = 0; i < dataList.size(); i++) {
                    if (dataList.get(i).getHuiYiId() == event.getMeetingID()) {
                        dataList.get(i).setHuiYiType(type);
                        adapter.refresh(dataList);
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
        context.registerReceiver(updateTypeReceiver, filter);
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

    @Override
    public void onResume() {
        super.onResume();
        registerTypeReceiver();
    }

    @Override
    public void onPause() {
        super.onPause();
        page = 0;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (updateTypeReceiver != null) {
            getActivity().unregisterReceiver(updateTypeReceiver);
        }
        mHandler.removeMessages(UPDATENUM);
        SoftApplication.getInstance().setMeetHandler(null);
        if(dataList!=null){
            dataList.clear();
        }
        if(completeDataList!=null){
            completeDataList.clear();
        }
        if(nocompleteDataList!=null){
            nocompleteDataList.clear();
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATENUM:
                    page = 0;
                    loading(false);
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REMINDIDO:
                if(data!=null){
                    Intent intent=new Intent(context,BookingMeetingRoom.class);
                    intent.putExtra("time",data.getStringExtra("time"));
                    startActivity(intent);
                }
                break;
            case CAMERAIDBOOK:
                String scanId = data.getStringExtra("SCAN_RESULT");
                if (!TextUtils.isEmpty(scanId)) {
                    Toast.makeText(context, "扫描" + scanId, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "扫描失败", Toast.LENGTH_SHORT).show();
                }
                Intent intent=new Intent(context, MeeTingDateActivity.class);
                startActivityForResult(intent,REMINDIDO);
                break;
            case CAMERAIDDETAIL:

                break;
            default:
                break;
        }
    }
}
