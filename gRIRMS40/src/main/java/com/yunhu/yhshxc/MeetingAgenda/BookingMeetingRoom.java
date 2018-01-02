package com.yunhu.yhshxc.MeetingAgenda;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yunhu.android.view.loopview.LoopView;
import com.yunhu.android.view.loopview.OnItemSelectedListener;
import com.yunhu.yhshxc.MeetingAgenda.adapter.MeetingListAdapter;
import com.yunhu.yhshxc.MeetingAgenda.bo.BuildingAndFloor;
import com.yunhu.yhshxc.MeetingAgenda.bo.MeetingRoomDetail;
import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.application.SoftApplication;
import com.yunhu.yhshxc.core.ApiRequestFactory;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 *＠author zhonghuibin
 *create at 2017/9/1.
 *describe 会议室预定页面
 */
public class BookingMeetingRoom extends Activity implements View.OnClickListener{
    private TextView tv_select_meettime;
    private RelativeLayout rl_select_meettime;
    private LoopView tv_select_address;
    private ListView lv_list_room;
    private String meeting_begin = "",meeting_end ="";
    private int mBuildId,mFloorId;
    private List<String> list_address = new ArrayList<String>();
    private List<Integer> list_build_id = new ArrayList<Integer>();
    private List<Integer> list_floorid = new ArrayList<Integer>();
    private static final int REQUSET = 5;
    private SharedPreferencesUtil share;
    private Dialog mLoadProgressDialog;
    private TextView message;
    private MeetingListAdapter meetingListAdapter;
    private List<MeetingRoomDetail.DataBeanX.DataBean> list;
    private static int MEETINGDETAILS = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_meetroom);
        tv_select_meettime = (TextView) findViewById(R.id.tv_select_meettime);
        tv_select_meettime.setOnClickListener(this);
        rl_select_meettime = (RelativeLayout) findViewById(R.id.rl_select_meettime);
        share = SharedPreferencesUtil.getInstance(BookingMeetingRoom.this);
        if (getIntent()!=null && getIntent().getStringExtra("time") !=null){
            rl_select_meettime.setVisibility(View.GONE);
            getTime(getIntent());
        }else{
            rl_select_meettime.setVisibility(View.VISIBLE);
        }
        initView();
        findViewById(R.id.iv_back_bookmeetroom).setOnClickListener(this);
        getBuildList();

        tv_select_address.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if (list_address.size() > 0){
                    list_address.get(index).toString();
                    for (int i=0;i<list_address.size();i++){
                        if (list_address.get(i).equals(list_address.get(index).toString())){
                            mBuildId = list_build_id.get(i);
                            mFloorId = list_floorid.get(i);
                        }
                    }
                }
                BookMeetingByBuild();
            }
        });
    }
    private void initView() {
        tv_select_address = (LoopView) findViewById(R.id.tv_select_address);
        lv_list_room = (ListView) findViewById(R.id.lv_list_room);
        list_address.add("");
        tv_select_address.setItems(list_address);
        tv_select_address.setOutTextSize(20F,16F);
        lv_list_room.setVerticalScrollBarEnabled(false);
        lv_list_room.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(BookingMeetingRoom.this,BookMroomDetails.class);
                MeetingRoomDetail.DataBeanX.DataBean databean = list.get(i);
                intent.putExtra("DataBean", databean);
                intent.putExtra("meeting_begin",meeting_begin);
                intent.putExtra("meeting_end",meeting_end);
                startActivityForResult(intent,MEETINGDETAILS);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back_bookmeetroom:
                finish();
                break;
            case R.id.tv_select_meettime:
                Intent intent = new Intent(BookingMeetingRoom.this,MeeTingDateActivity.class);
                startActivityForResult(intent,REQUSET);
                break;
            default:
                break;
        }
    }
    /**
     *@date2017/9/13
     *@author zhonghuibin
     *@description 根据公司ID查询的楼和楼层
     */
    public static void getCompanyList(Context context, String url, int id  ,
                                      final ApiRequestFactory.HttpCallBackListener httpCallBackListener,
                                      boolean isShowDialog){
        Map<String ,String> map = new HashMap<>();
        map.put("company_id",id+"");
        ApiRequestFactory.postJson(context,url,map,httpCallBackListener,isShowDialog);
    }

    /**
     *@date2017/9/13
     *@author zhonghuibin
     *@description 根据楼id层id和开始结束时间筛选会议室信息列表
     */
    public static void getMeetRoom(Context context,String url,int company_id,int buildingid,int floorid,
                                   String starttime,String endtime ,final ApiRequestFactory.HttpCallBackListener httpCallBackListener,
                                   boolean isShowDialog){
        Map<String ,String> map = new HashMap<>();
        map.put("company_id",company_id+"");
        if (buildingid == 0){
            map.put("starttime",starttime);
            map.put("endtime",endtime);
        }else{
            map.put("buildingid",buildingid+"");
            map.put("floorid",floorid+"");
            map.put("starttime",starttime);
            map.put("endtime",endtime);
        }
        ApiRequestFactory.postJson(context,url,map,httpCallBackListener,isShowDialog);
    }

    /**
     *@date2017/9/13
     *@author zhonghuibin
     *@description 根据公司ID获取推荐的会议室列表
     */
//    public static void getRecommendMeetinglist(Context context, String url, int id  ,
//                                      final ApiRequestFactory.HttpCallBackListener httpCallBackListener,
//                                      boolean isShowDialog){
//        Map<String ,String> map = new HashMap<>();
//        map.put("company_id",id+"");
//        ApiRequestFactory.postJson(context,url,map,httpCallBackListener,isShowDialog);
//    }
    private void showDialog(String messages) {
        if (mLoadProgressDialog == null) {
            mLoadProgressDialog = new Dialog(this,
                    R.style.loading_dialog);
            View v1 = LayoutInflater.from(this).inflate(R.layout.loading_progressdialog, null);
            message = (TextView) v1.findViewById(R.id.message);
            message.setText(messages);
            v1.setMinimumWidth(PublicUtils.convertDIP2PX(this, 300));
            mLoadProgressDialog.setContentView(v1);
            mLoadProgressDialog.setCanceledOnTouchOutside(false);
        }
        message.setText(messages);
        mLoadProgressDialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUSET && resultCode == RESULT_OK){
            getTime(data);
            BookMeetingByBuild();
        }else if (requestCode == MEETINGDETAILS && resultCode == RESULT_OK){
//            EventBus.getDefault().post(new FirstEvent("新建成功"));
            if(SoftApplication.getInstance().getMeetHandler()!=null){
                SoftApplication.getInstance().getMeetHandler().sendEmptyMessage(MeetingagendaListActivity.UPDATENUM);
            }

            finish();
        }
    }

    /**
     *@date2017/10/20
     *@author zhonghuibin
     *@description 按照时间预定会议室时候，页面得到的开始时间和结束时间
     */
    private void getTime(Intent intent){
        meeting_begin = intent.getStringExtra("time").substring(0,19);
        meeting_end = intent.getStringExtra("time").substring(20,intent.getStringExtra("time").length());
        tv_select_meettime.setText(intent.getStringExtra("time").substring(5,16)+"~"+intent.getStringExtra("time").substring(31,36));
//        meetingListAdapter.notifyDataSetChanged();
    }

    /**
     *@date2017/10/30
     *@author zhonghuibin
     *@description 获取滑动控件楼层列表
     */
    public void getBuildList() {
        getCompanyList(BookingMeetingRoom.this, "m=Androidstation&a=getbuildinglist",
                share.getCompanyId(),
                new ApiRequestFactory.HttpCallBackListener() {
                    @Override
                    public void onSuccess(String response, String url, int id) {
                        try{
                            BuildingAndFloor build = new Gson().fromJson(response,BuildingAndFloor.class);
                            //存储楼ID和层ID
                            share.setBuildingId(Integer.parseInt(build.getData().getData().get(0).getId()));
                            if (build.getData().getData().get(0).getFloor() != null){
                            share.setFloorId(Integer.parseInt(build.getData().getData().get(0).getFloor().get(0).getId()));
                            }
                            list_address.clear();
                            list_address.add("全部会议室");
                            list_build_id.add(0);
                            list_floorid.add(0);
                            for (int i=0;i<build.getData().getData().size();i++){
                                if (build.getData().getData().get(i).getFloor() != null){
                                    for (int j=0;j<build.getData().getData().get(i).getFloor().size();j++){
                                        list_address.add(build.getData().getData().get(i).getBuilding()+"栋"+
                                                build.getData().getData().get(i).getFloor().get(j).getFloor()+"层");
                                        list_build_id.add(Integer.parseInt(build.getData().getData().get(i).getId()));
                                        list_floorid.add(Integer.parseInt(build.getData().getData().get(i).getFloor().get(j).getId()));
                                    }
                                }else{
                                    list_address.add(build.getData().getData().get(i).getBuilding()+"栋");
                                    list_build_id.add(Integer.parseInt(build.getData().getData().get(i).getId()));
                                    list_floorid.add(0);
                                }
                            }
                                tv_select_address.setItems(list_address);
                                tv_select_address.setCurrentPosition(0);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        if (mLoadProgressDialog != null && mLoadProgressDialog.isShowing()){
                            mLoadProgressDialog.dismiss();
                        }
                        BookMeetingByBuild();
                    }
                    @Override
                    public void failure(Call call, Exception e, int id) {
                        tv_select_address.setVisibility(View.GONE);
                        if (mLoadProgressDialog != null && mLoadProgressDialog.isShowing()){
                            mLoadProgressDialog.dismiss();
                        }
                    }
                },true);
    }
    /**
     *@date2017/10/30
     *@author zhonghuibin
     *@description 按照楼层预定会议室
     */
    private void BookMeetingByBuild() {
        getMeetRoom(BookingMeetingRoom.this,"m=Androidstation&a=Meetingroominfo",
                share.getCompanyId(),mBuildId,mFloorId,meeting_begin,meeting_end, new ApiRequestFactory.HttpCallBackListener() {
                    @Override
                    public void onSuccess(String response, String url, int id) {
                        MeetingRoomDetail meetingroom = new Gson().fromJson(response,MeetingRoomDetail.class);
                        if (meetingroom.getData().getFlag()==0){
                            list = meetingroom.getData().getData();
                            meetingListAdapter = new MeetingListAdapter(list,BookingMeetingRoom.this,meeting_begin,meeting_end);
                            if (meetingroom.getData().getData() != null){
                                lv_list_room.setAdapter(meetingListAdapter);
                            }
                        }else if(meetingroom.getData().getFlag()== -5){
                            list.clear();
                            meetingListAdapter.notifyDataSetChanged();
                        }
                    }
                    @Override
                    public void failure(Call call, Exception e, int id) {
                        if (mLoadProgressDialog != null && mLoadProgressDialog.isShowing()){
                            mLoadProgressDialog.dismiss();
                        }
                    }
                },true);
    }
}
