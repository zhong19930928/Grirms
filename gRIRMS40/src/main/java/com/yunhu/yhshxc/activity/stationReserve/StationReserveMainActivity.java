package com.yunhu.yhshxc.activity.stationReserve;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.Gravity;
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
import com.yunhu.yhshxc.MeetingAgenda.BookMroomDetails;
import com.yunhu.yhshxc.MeetingAgenda.MeetingUserActivity;
import com.yunhu.yhshxc.MeetingAgenda.view.AddPopWindow;
import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.stationReserve.adapter.StationReserveListAdapter;
import com.yunhu.yhshxc.application.SoftApplication;
import com.yunhu.yhshxc.bo.StationBean;
import com.yunhu.yhshxc.bo.SureStationBean;
import com.yunhu.yhshxc.core.ApiRequestFactory;
import com.yunhu.yhshxc.core.ApiRequestMethods;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.widget.ToastOrder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


/**
 * 预定工位列表页面（main页面）
 */
public class StationReserveMainActivity extends Activity implements View.OnClickListener, StationReserveListAdapter.OnClickItemListener {
    private TextView stationreserve_title;
    private LinearLayout stationreserve_menu;
    private PullToRefreshListView stationreserve_listview, station_listview_other;
    private AddPopWindow addPopWindow;
    private View popView;
    private Context context;
    private StationReserveListAdapter stationReserveListAdapter;//普通列表adapter
    private StationReserveListAdapter manageStationListAdapter;//管理列表adapter
    private int company_id;//公司id
    private int uid;
    private int bmid = 0;//部门id
    private String uName;

    private List<StationBean> nomalList;//普通列表list
    private List<StationBean> manageList;//普通列表list
    public static final int UPDATEPAGE=10;
    private static final int PASEPAGE=11;
    private int nomalPage=0,managePage=0;
    private  PopupWindow releaseWindow = null;
    private TextView tv_unfinish,tv_finish,tv_unfinish_line,tv_finish_line;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_reserve_main);
        context = this;
        SharedPreferencesUtil s = SharedPreferencesUtil.getInstance(this);
        company_id = s.getCompanyId();
        uid = s.getUserId();
        bmid = s.getOrgId();
        uName = s.getUserName();
        SoftApplication.getInstance().setStationHandler(mHandler);
        initView();
        initData();
    }


    /**
     * 初始化view
     */
    private void initView() {
        findViewById(R.id.stationreserve_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        stationreserve_title = (TextView) findViewById(R.id.stationreserve_title);
        stationreserve_menu = (LinearLayout) findViewById(R.id.stationreserve_menu);
        stationreserve_menu.setOnClickListener(this);
        stationreserve_listview = (PullToRefreshListView) findViewById(R.id.stationreserve_listview);
        station_listview_other = (PullToRefreshListView) findViewById(R.id.station_listview_other);
        String label = DateUtils.formatDateTime(this.getApplicationContext(), System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
        stationreserve_listview.getLoadingLayoutProxy().setLastUpdatedLabel(label);
        stationreserve_listview.setMode(PullToRefreshBase.Mode.BOTH);
        station_listview_other.getLoadingLayoutProxy().setLastUpdatedLabel(label);
        station_listview_other.setMode(PullToRefreshBase.Mode.BOTH);
        stationreserve_listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                nomalPage = 0;
                loading(true,false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loading(true,false);
            }
        });
        station_listview_other.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                managePage = 0;
                loading(true,true);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loading(true,true);
            }
        });

        stationreserve_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(context, ReserveStationActivity.class);
                SureStationBean sureStationBean = new SureStationBean();
                sureStationBean.setStartTime(nomalList.get(i-1).getStartTime());
                sureStationBean.setEndTime(nomalList.get(i-1).getEndTime());
                sureStationBean.setLouName(nomalList.get(i-1).getLouName());
                sureStationBean.setLouId(nomalList.get(i-1).getLouId()+"");
                sureStationBean.setCengName(nomalList.get(i-1).getCengName());
                sureStationBean.setCengId(nomalList.get(i-1).getCengId()+"");
                sureStationBean.setGswn(nomalList.get(i-1).getGwsnId()+"");
                intent.putExtra("SureStationBean", sureStationBean);
                intent.putExtra("flag",2);
                startActivity(intent);
            }
        });

        tv_unfinish = (TextView) findViewById(R.id.tv_unfinish);
        tv_unfinish_line = (TextView) findViewById(R.id.tv_unfinish_line);
        tv_finish_line = (TextView) findViewById(R.id.tv_finish_line);
        tv_unfinish.setOnClickListener(this);
        tv_finish = (TextView) findViewById(R.id.tv_finish);
        tv_finish.setOnClickListener(this);

    }


    /**
     * 初始化数据
     */
    private void initData() {
        nomalList = new ArrayList<>();
        manageList=new ArrayList<>();
        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                loading(true, false);
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.stationreserve_menu:
                showPop();
                break;
            case R.id.tv_unfinish:
                tv_unfinish_line.setBackgroundResource(R.color.app_color);
                tv_finish_line.setBackgroundResource(R.color.white);
                stationreserve_listview.setVisibility(View.VISIBLE);
                station_listview_other.setVisibility(View.GONE);
                break;
            case R.id.tv_finish:
                tv_unfinish_line.setBackgroundResource(R.color.white);
                tv_finish_line.setBackgroundResource(R.color.app_color);
                stationreserve_listview.setVisibility(View.GONE);
                station_listview_other.setVisibility(View.VISIBLE);
                if(manageList!=null&&manageList.size()==0){
                    loading(true,true);
                }
                break;
            default:
                break;
        }

    }


    /**
     * 预约工位弹出框
     */
    private void showPop() {
        if (addPopWindow == null) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            popView = inflater.inflate(R.layout.addstation_popupwindow, null);
            addPopWindow = new AddPopWindow(this, popView, PublicUtils.convertDIP2PX(context, 151));
        }
        addPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });
        TextView station_reserve = (TextView) popView.findViewById(R.id.station_reserve);
        station_reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(StationReserveMainActivity.this, StationMiddleActivity.class);
                intent.putExtra("flag",1);
                startActivity(intent);
                addPopWindow.dismiss();
            }
        });
        TextView put_stationreserve = (TextView) popView.findViewById(R.id.put_stationreserve);
        put_stationreserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(context, CaptureActivity.class);
//                startActivityForResult(i, 111);
                Toast.makeText(StationReserveMainActivity.this, "暂未开放", Toast.LENGTH_SHORT).show();
                addPopWindow.dismiss();
            }
        });
        TextView look_allsattion = (TextView) popView.findViewById(R.id.look_allsattion);
        look_allsattion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(StationReserveMainActivity.this, StationMiddleActivity.class);
                intent.putExtra("flag",3);
                startActivity(intent);
                addPopWindow.dismiss();
            }
        });
        if (!addPopWindow.isShowing()) {
            addPopWindow.showPopupWindow(stationreserve_title);
            backgroundAlpha(0.5f);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == R.id.scan_succeeded && requestCode == 111) {
            //扫描返回
            String scanId = data.getStringExtra("SCAN_RESULT");
            if (!TextUtils.isEmpty(scanId)) {
                Toast.makeText(context, "扫描成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "扫描失败", Toast.LENGTH_SHORT).show();
            }
            Intent intent=new Intent(StationReserveMainActivity.this, StationMiddleActivity.class);
            intent.putExtra("flag",2);
            startActivity(intent);
        } else if (requestCode == BookMroomDetails.REQUSET_BOOKROOM_PEOPLE) {
            if (data != null) {
                String ss = data.getStringExtra("usName");
                String ss1 = data.getStringExtra("usId");
                int position = data.getIntExtra("position", 0);
                if (!data.getStringExtra("usId").split(",")[0].equals(nomalList.get(position).getYgId() + "")) {
                    updateUserInfo(data.getStringExtra("usId").split(",")[0], data.getStringExtra("usName").split(",")[0],
                            uName, company_id, nomalList.get(position).getId(), true, position);
                }
            }
        }
    }


    /**
     * 加载列表数据
     *
     * @param isShow
     */
    private void loading(boolean isShow, final boolean isManage) {
        ApiRequestMethods.getStationList(this, isManage ? -1 :uid, company_id, isManage ? bmid : -1,isManage?managePage:nomalPage, new ApiRequestFactory.HttpCallBackListener() {
            @Override
            public void onSuccess(final String response, String url, int id) {
                Message msg=new Message();
                msg.what=PASEPAGE;
                Bundle bundle=new Bundle();
                bundle.putBoolean("isManage",isManage);
                bundle.putString("response",response);
                msg.setData(bundle);
                mHandler.sendMessage(msg);
            }

            @Override
            public void failure(Call call, Exception e, int id) {
                stationreserve_listview.onRefreshComplete();
                station_listview_other.onRefreshComplete();
                Toast.makeText(context, "查询失败", Toast.LENGTH_SHORT).show();
            }
        }, isShow);
    }


    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case PASEPAGE:
                    Bundle bundle=msg.getData();
                    paseData(bundle.getString("response"), bundle.getBoolean("isManage"));
                    break;
                case UPDATEPAGE:
                    if(nomalList!=null){
                        nomalList.clear();
                    }
                    if(manageList!=null){
                        manageList.clear();
                    }
                    nomalPage=0;
                    managePage=0;
                    loading(false, false);
                    loading(false, true);
                    break;
                default:break;
            }


        }
    };

    /**
     * 释放工位
     */
    private void releaseGongwei(int id, int company_id, boolean isShow, final int positon) {
        ApiRequestMethods.deleteGongwei(this, id, company_id, new ApiRequestFactory.HttpCallBackListener() {
            @Override
            public void onSuccess(final String response, String url, int id) {
                StationReserveMainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(response).getJSONObject("data");
                            if (object == null) {
                                ToastOrder.makeText(context, "释放工位失败", ToastOrder.LENGTH_SHORT).show();
                                return;
                            }
                            if (object.has("flag") && object.getInt("flag") == 0) {
                                reFreshManageList(nomalList.get(positon).getId());
                                nomalList.remove(positon);
                                stationReserveListAdapter.refresh(nomalList);
                                ToastOrder.makeText(context, "释放工位成功", ToastOrder.LENGTH_SHORT).show();
                            }else if(object.has("flag") && object.getInt("flag") == -5){
                                reFreshManageList(nomalList.get(positon).getId());
                                nomalList.remove(positon);
                                stationReserveListAdapter.refresh(nomalList);
                                ToastOrder.makeText(context, "释放工位成功", ToastOrder.LENGTH_SHORT).show();
                            } else {
                                ToastOrder.makeText(context, "释放工位失败", ToastOrder.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }

            @Override
            public void failure(Call call, Exception e, int id) {
                Toast.makeText(context, "释放工位失败", Toast.LENGTH_SHORT).show();
            }
        }, isShow);
    }


    /**
     * 修改工位使用人
     */
    private void updateUserInfo(final String ygid, final String yghaoname, String update_user, int company_id, int id, final boolean isShow, final int position) {
        ApiRequestMethods.updateUserInfo(this, ygid, yghaoname, update_user, company_id, id, bmid+"",new ApiRequestFactory.HttpCallBackListener() {
            @Override
            public void onSuccess(final String response, String url, int id) {
                try {
                    JSONObject object = new JSONObject(response).getJSONObject("data");
                    if (object == null) {
                        Toast.makeText(context, "修改工位失败", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (object.has("flag") && object.getInt("flag") == 0) {
                        Toast.makeText(context, "修改工位成功", Toast.LENGTH_SHORT).show();
                        nomalList.get(position).setYgId(Integer.parseInt(ygid));
                        nomalList.get(position).setYgHaoName(yghaoname);
                        stationReserveListAdapter.refresh(nomalList);
                    } else {
                        Toast.makeText(context, "修改工位失败", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(Call call, Exception e, int id) {
                Toast.makeText(context, "修改工位失败", Toast.LENGTH_SHORT).show();
            }
        }, isShow);
    }


    /**
     * 解析数据
     *
     * @param response
     * @param isManageList true表示部门列表
     */
    private void paseData(String response, boolean isManageList) {
        stationreserve_listview.onRefreshComplete();
        station_listview_other.onRefreshComplete();
        try {
            JSONObject object = new JSONObject(response).getJSONObject("data");
            if (object == null) {
                ToastOrder.makeText(context, R.string.search_no_data, ToastOrder.LENGTH_SHORT).show();
                return;
            }
            if (object.has("flag") && object.getInt("flag") == 0) {
                JSONArray data = object.getJSONArray("data");
                if (data != null && data.length() > 0) {
                    if(isManageList){
                        if (managePage == 0) {
                            manageList.clear();
                        }
                        managePage++;
                    }else{
                        if (nomalPage == 0) {
                            nomalList.clear();
                        }
                        nomalPage++;
                    }
                    StationBean stationBean;
                    for (int i = 0; i < data.length(); i++) {
                        stationBean = new StationBean();
                        JSONObject jsonObject = data.getJSONObject(i);
                        if (jsonObject.has("id")) {
                            stationBean.setId(jsonObject.getInt("id"));
                        }
                        if (jsonObject.has("gwsn")&& !"null".equals(jsonObject.getString("gwsn"))) {
                            stationBean.setGwsnId(jsonObject.getInt("gwsn"));
                        }
                        if (jsonObject.has("gwname")) {
                            stationBean.setGwname(jsonObject.getString("gwname"));
                        }
                        if (jsonObject.has("starttime")) {
                            stationBean.setStartTime(jsonObject.getString("starttime"));
                        }
                        if (jsonObject.has("endtime")) {
                            stationBean.setEndTime(jsonObject.getString("endtime"));
                        }
                        if (jsonObject.has("ydpeo")&& !"null".equals(jsonObject.getString("ydpeo"))) {
                            stationBean.setYdpeId(jsonObject.getInt("ydpeo"));
                        }
                        if (jsonObject.has("ygid")&& !"null".equals(jsonObject.getString("ygid"))) {
                            stationBean.setYgId(jsonObject.getInt("ygid"));
                        }
                        if (jsonObject.has("bmid")&& !"null".equals(jsonObject.getString("bmid"))) {
                            stationBean.setBmId(jsonObject.getInt("bmid"));
                        }
                        if (jsonObject.has("louid")&& !"null".equals(jsonObject.getString("louid"))) {
                            stationBean.setLouId(jsonObject.getInt("louid"));
                        }
                        if (jsonObject.has("cengid")&& !"null".equals(jsonObject.getString("cengid"))) {
                            stationBean.setCengId(jsonObject.getInt("cengid"));
                        }

                        if (jsonObject.has("louname")) {
                            stationBean.setLouName(jsonObject.getString("louname"));
                        }
                        if (jsonObject.has("cengname")) {
                            stationBean.setCengName(jsonObject.getString("cengname"));
                        }
                        if (jsonObject.has("yghaoname")) {
                            stationBean.setYgHaoName(jsonObject.getString("yghaoname"));
                        }

                        if (isManageList) {
                            //部门列表
                            if (manageList == null) {
                                manageList = new ArrayList<>();
                            }
                            manageList.add(stationBean);

                        } else {
                            //正常列表
                            if (nomalList == null) {
                                nomalList = new ArrayList<>();
                            }
                            nomalList.add(stationBean);
                        }

                    }
                    if (isManageList) {
                        if (manageStationListAdapter == null) {
                            manageStationListAdapter = new StationReserveListAdapter(this, manageList, this);
                            manageStationListAdapter.setManage(true);
                            station_listview_other.setAdapter(manageStationListAdapter);
                        } else {
                            manageStationListAdapter.refresh(manageList);
                            manageStationListAdapter.notifyDataSetChanged();
                        }
                    } else {
                        if (stationReserveListAdapter == null) {
                            stationReserveListAdapter = new StationReserveListAdapter(this, nomalList, this);
                            stationreserve_listview.setAdapter(stationReserveListAdapter);
                        } else {
                            stationReserveListAdapter.refresh(nomalList);
                            stationReserveListAdapter.notifyDataSetChanged();
                        }
                    }

                } else {
                    ToastOrder.makeText(context, R.string.search_no_data, ToastOrder.LENGTH_SHORT).show();
                }
            } else if(object.has("flag") && object.getInt("flag") == -5){
                if (isManageList) {
                    if (manageStationListAdapter != null&&manageList!=null&&managePage==0) {
                        manageList.clear();
                        manageStationListAdapter.refresh(manageList);
                        manageStationListAdapter.notifyDataSetChanged();
                    }
                }else{
                    if (stationReserveListAdapter != null&&nomalList!=null&&nomalPage==0) {
                        nomalList.clear();
                        stationReserveListAdapter.refresh(manageList);
                        stationReserveListAdapter.notifyDataSetChanged();
                    }
                }
                ToastOrder.makeText(context, R.string.search_no_data, ToastOrder.LENGTH_SHORT).show();
            } else {
                ToastOrder.makeText(context, "查询失败", ToastOrder.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void clickItem(int position, View view) {
        StationBean stationBean = nomalList.get(position);
        switch (view.getId()) {
            case R.id.release_station:
                //释放工位
                Toast.makeText(StationReserveMainActivity.this, "暂未开放", Toast.LENGTH_SHORT).show();
//                surePopWindow(stationBean,position);
                break;
            case R.id.pername_layout:
                //修改工位使用人
                Toast.makeText(StationReserveMainActivity.this, "暂未开放", Toast.LENGTH_SHORT).show();
//                String peopleId = stationBean.getYgId() + "";
//                if (peopleId == null) {
//                    peopleId = "";
//                }
//                Intent intent_people = new Intent(StationReserveMainActivity.this, MeetingUserActivity.class);
//                if (peopleId != null) {
//                    intent_people.putExtra("uIds", peopleId);
//                    intent_people.putExtra("position", position);
//                }
//                startActivityForResult(intent_people, BookMroomDetails.REQUSET_BOOKROOM_PEOPLE);
                break;
            default:
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(UPDATEPAGE);
        SoftApplication.getInstance().setStationHandler(null);
    }


    private void surePopWindow(final  StationBean stationBean,final  int position) {
        if (releaseWindow != null && releaseWindow.isShowing()){
            return;
        }
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout myView = (LinearLayout) inflater.inflate(R.layout.surecancle_layout, null);

        myView.findViewById(R.id.bt_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                releaseWindow.dismiss();
                releaseGongwei(stationBean.getId(), company_id, true,position);
            }
        });
        myView.findViewById(R.id.bt_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                releaseWindow.dismiss();
            }
        });
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        releaseWindow = new PopupWindow(myView, width*2 /3,
                RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        releaseWindow.setAnimationStyle(R.style.AnimationPopup);
        releaseWindow.setOutsideTouchable(true);
        releaseWindow.setBackgroundDrawable(new BitmapDrawable());
        releaseWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });
        if(!releaseWindow.isShowing()){
            releaseWindow.showAtLocation(stationreserve_title, Gravity.CENTER, 0, 0);
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
     * 刷新部门列表
     * @param id
     */
    private void reFreshManageList(int id){
        if(manageList!=null&&manageList.size()>0&&manageStationListAdapter!=null){
            for(int i=0;i<manageList.size();i++){
                if(manageList.get(i).getId()==id){
                    manageList.remove(i);
                    manageStationListAdapter.refresh(manageList);
                }
            }
        }
    }
}
