package com.yunhu.yhshxc.MeetingAgenda;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yunhu.yhshxc.MeetingAgenda.adapter.MeetingName_GridviewAdapter;
import com.yunhu.yhshxc.MeetingAgenda.bo.MeetingAgenda;
import com.yunhu.yhshxc.MeetingAgenda.bo.MeetingDetails;
import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.core.ApiRequestFactory;
import com.yunhu.yhshxc.core.ApiRequestMethods;
import com.yunhu.yhshxc.core.ApiUrl;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * ＠author zhonghuibin
 * create at 2017/9/1.
 * describe 会议室详情页面
 */
public class MeetingRoomDetails extends Activity implements View.OnClickListener{
    public static final int REQUSET_BOOKROOM_NOTICE = 4;
    public static final int REQUSET_BOOKROOM_RECYCLE = 5;
    public static final int REQUSET_BOOKROOM_PEOPLE = 6;
    public static final int REQUSET_BOOKROOM_TIME = 7;
    private MeetingAgenda meetingAgenda;
    private RelativeLayout rl_select_meettime_detail,rl_bookmeetdetails_cancle;
    private TextView meetingroom_name_meetdetails,meetingroom_bumber_meetdetails;
    private TextView et_meetsubject_meetdetails;
    private RelativeLayout rl_top_bookroom;
    private TextView tv_service_chaye,tv_service_chabei,tv_service_kaishui,
            tv_service_zhuopan,tv_service_guopan,tv_service_biaoyu;
    private TextView tv_notic_bookdetails,tv_select_meettime_detail,tv_renumber_bookdetails;
    private ImageView iv_askname_bookdetails,iv_back_bookdetails;
    private TextView tv_noticeway_email_bookdetails,tv_noticeway_message_bookdetail,tv_beizhu_meetdetails,
            tv_noticeway_netout_bookdetail;
    private EditText et_beizhu_meetdetails;
    //会议需要的服务标识
    private int has_select_chaye,has_select_chabei,has_select_kaishui,
            has_select_zhuopan,has_select_guopan,has_select_biaoyu;
    //会议的三种发送方式标识：站内、邮件、短信
    private boolean netout=false,message=false,email=false;
    private int noticetime = 2,notice_recycle=0;
    private String noitce_way,meetingTime_begin,meetingTime_end,peopleName,peopleId;
    private String[] listName,listId;
    private String[] listName2,listId2;
    private GridView gridview_people_meeting;
    private PopupWindow popupWindow;
    private TextView rl_back,tv_commitname;
    private int meetingRoomId,typeid;
    private MeetingDetails meetdetails;
    private MeetingDetails.DataBeanX.DataBean data;
    private SharedPreferencesUtil shi;
    private TextView tv_acccess,tv_refuse,tv_prieve;
    private int itemIsFinishFlag;
    private boolean isshow_cancle=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmroomdetails2);
        if (getIntent()!=null){
            meetingAgenda = (MeetingAgenda) getIntent().getSerializableExtra("meetingagenda");
            meetingRoomId = meetingAgenda.getHuiYiId();
            itemIsFinishFlag = getIntent().getIntExtra("itemIsFinishFlag",0);
            typeid = meetingAgenda.getType();
        }
        shi = SharedPreferencesUtil.getInstance(MeetingRoomDetails.this);
        //避免进入页面直接弹出软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initView();
    }

    private void initView() {
        rl_back = (TextView) findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
        iv_back_bookdetails = (ImageView) findViewById(R.id.iv_back_bookdetails);
        iv_askname_bookdetails = (ImageView) findViewById(R.id.iv_askname_bookdetails);
        tv_select_meettime_detail = (TextView) findViewById(R.id.tv_select_meettime_detail);
        gridview_people_meeting = (GridView) findViewById(R.id.gridview_people_meeting);
        meetingroom_name_meetdetails = (TextView) findViewById(R.id.meetingroom_name_meetdetails);
        meetingroom_bumber_meetdetails = (TextView) findViewById(R.id.meetingroom_bumber_meetdetails);
        rl_top_bookroom = (RelativeLayout) findViewById(R.id.rl_top_bookroom);
        tv_service_chaye = (TextView) findViewById(R.id.tv_service_chaye);
        tv_service_chabei = (TextView) findViewById(R.id.tv_service_chabei);
        tv_service_kaishui = (TextView) findViewById(R.id.tv_service_kaishui);
        tv_service_zhuopan = (TextView) findViewById(R.id.tv_service_zhuopan);
        tv_service_guopan = (TextView) findViewById(R.id.tv_service_guopan);
        tv_acccess = (TextView) findViewById(R.id.tv_acccess);
        tv_acccess.setOnClickListener(this);
        tv_refuse = (TextView) findViewById(R.id.tv_refuse);
        tv_refuse.setOnClickListener(this);
        tv_prieve = (TextView) findViewById(R.id.tv_prieve);
        tv_prieve.setOnClickListener(this);
        tv_service_biaoyu = (TextView) findViewById(R.id.tv_service_biaoyu);
        tv_notic_bookdetails = (TextView) findViewById(R.id.tv_notic_bookdetails);
        rl_bookmeetdetails_cancle = (RelativeLayout) findViewById(R.id.rl_bookmeetdetails_cancle);
        rl_select_meettime_detail = (RelativeLayout) findViewById(R.id.rl_select_meettime_detail);
        tv_renumber_bookdetails = (TextView) findViewById(R.id.tv_renumber_bookdetails);
        tv_noticeway_email_bookdetails = (TextView) findViewById(R.id.tv_noticeway_email_bookdetails);
        tv_noticeway_message_bookdetail = (TextView) findViewById(R.id.tv_noticeway_message_bookdetail);
        tv_noticeway_netout_bookdetail = (TextView) findViewById(R.id.tv_noticeway_netout_bookdetail);
        et_meetsubject_meetdetails = (TextView) findViewById(R.id.et_meetsubject_meetdetails);
        tv_commitname = (TextView) findViewById(R.id.tv_commitname);
        et_beizhu_meetdetails = (EditText) findViewById(R.id.et_beizhu_meetdetails);
        tv_beizhu_meetdetails = (TextView) findViewById(R.id.tv_beizhu_meetdetails);
        tv_noticeway_netout_bookdetail.setOnClickListener(this);
        tv_noticeway_message_bookdetail.setOnClickListener(this);
        tv_noticeway_email_bookdetails.setOnClickListener(this);
        tv_renumber_bookdetails.setOnClickListener(this);
        iv_back_bookdetails.setOnClickListener(this);
        tv_select_meettime_detail.setOnClickListener(this);
        iv_askname_bookdetails.setOnClickListener(this);
        rl_bookmeetdetails_cancle.setOnClickListener(this);
        tv_notic_bookdetails.setOnClickListener(this);

        serviceFormeeting(tv_service_chaye);
        serviceFormeeting(tv_service_chabei);
        serviceFormeeting(tv_service_kaishui);
        serviceFormeeting(tv_service_zhuopan);
        serviceFormeeting(tv_service_guopan);
        serviceFormeeting(tv_service_biaoyu);
        if (meetingAgenda != null){
            setType(meetingAgenda.getHuiYiType());
        }
        getMeetingDetails(MeetingRoomDetails.this, "m=Androidstation&a=getIDmeetingdetails", meetingRoomId, new ApiRequestFactory.HttpCallBackListener() {
            @Override
            public void onSuccess(String response, String url, int id) {
                String aa = response;
                try {
                    meetdetails = new Gson().fromJson(response,MeetingDetails.class);
                    data = meetdetails.getData().getData();
                    setmean();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(Call call, Exception e, int id) {

            }
        },true);
    }

    private void setType(int type) {
        if (meetingAgenda != null && itemIsFinishFlag == 2){
            tv_acccess.setVisibility(View.GONE);
            tv_refuse.setVisibility(View.GONE);
            tv_prieve.setVisibility(View.GONE);
            rl_bookmeetdetails_cancle.setVisibility(View.GONE);
            isshow_cancle = false;
        }else{
            switch (type){
                case 1:
                    tv_acccess.setVisibility(View.GONE);
                    tv_refuse.setVisibility(View.GONE);
                    tv_prieve.setVisibility(View.GONE);
                    break;
                case 2:
                    tv_acccess.setVisibility(View.VISIBLE);
                    tv_refuse.setVisibility(View.VISIBLE);
                    tv_prieve.setVisibility(View.VISIBLE);
                    tv_acccess.setTextColor(getResources().getColor(R.color.bbs_menu_blue));
                    break;
                case 3:
                    tv_acccess.setVisibility(View.VISIBLE);
                    tv_refuse.setVisibility(View.VISIBLE);
                    tv_prieve.setVisibility(View.VISIBLE);
                    tv_prieve.setTextColor(getResources().getColor(R.color.bbs_menu_blue));
                    break;
                case 4:
                    tv_acccess.setTextColor(getResources().getColor(R.color.gray_sub));
                    tv_refuse.setTextColor(getResources().getColor(R.color.gray_sub));
                    tv_prieve.setTextColor(getResources().getColor(R.color.gray_sub));
                    break;
                case 5:
                    tv_acccess.setVisibility(View.GONE);
                    tv_refuse.setVisibility(View.GONE);
                    tv_prieve.setVisibility(View.GONE);
                    break;
                default:
                    tv_acccess.setTextColor(getResources().getColor(R.color.gray_sub));
                    tv_refuse.setTextColor(getResources().getColor(R.color.gray_sub));
                    tv_prieve.setTextColor(getResources().getColor(R.color.gray_sub));
                    break;
            }
        }
    }

    private void onChiese(View view,final int huiyitype){
        ApiRequestMethods.confirmmeeting(this, shi.getCompanyId(), shi.getUserId(), shi.getUserName(), meetingRoomId, huiyitype,typeid, ApiUrl.CONFIRMEETTING, new ApiRequestFactory.HttpCallBackListener() {
            @Override
            public void onSuccess(String response, String url, int id) {
                JSONObject object = null;
                try {
                    object = new JSONObject(response).getJSONObject("data");
                    if (object == null){
                        return;
                    }
                    if (object.has("flag") && object.getInt("flag") == 0) {
                        Toast.makeText(MeetingRoomDetails.this,"操作成功",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent();
                        if (huiyitype == 4){
                            intent.putExtra("type",5);
                        }else{
                            intent.putExtra("type",huiyitype);
                        }
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void failure(Call call, Exception e, int id) {

            }
        }, true);
    }
    /**
     *@date2017/10/31
     *@author zhonghuibin
     *@description 会议室详情界面赋值
     */
    private void setmean() {
        if (Integer.parseInt(data.getUid())==shi.getUserId() && isshow_cancle){
            rl_bookmeetdetails_cancle.setVisibility(View.VISIBLE);
        }else {
            rl_bookmeetdetails_cancle.setVisibility(View.GONE);
        }
        meetingTime_begin = data.getStarttime();
        meetingTime_end = data.getEndtime();
        tv_select_meettime_detail.setText(meetingTime_begin.substring(0,meetingTime_begin.length()-3)+"-"+meetingTime_end.substring(0,meetingTime_end.length()-3));
        meetingroom_name_meetdetails.setText(data.getBuildingname()+"座"+data.getFloorname()+"层"+data.getRoomname()+"室");
//        meetingroom_bumber_meetdetails.setText(data.get);  会议室容纳人数
        et_meetsubject_meetdetails.setText(data.getMeetingname());
        tv_commitname.setText(data.getUname());
        peopleName = data.getPeopname();
        peopleId = data.getPeopuid();
        if (peopleName!=null&&!peopleName.equals("")){
            listName2 = peopleName.split(",");
            listId2 = peopleId.split(",");
            setListViewHeightBasedOnChildren(gridview_people_meeting,listName2,this);
            gridview_people_meeting.setAdapter(new MeetingName_GridviewAdapter(MeetingRoomDetails.this,listName2,listId2));
        }
        sendType(data.getSend_type());
        service(data.getService());
        setRemindid(Integer.parseInt(data.getRemindid()));
        setRepeatid(Integer.parseInt(data.getRepeatid()));
        et_beizhu_meetdetails.setText(data.getRemarks());
        tv_beizhu_meetdetails.setText("备注： "+data.getRemarks());

    }

    private void setRepeatid(int repeatid) {//重复
        switch (repeatid){
            case 0:
                tv_renumber_bookdetails.setText("永不重复");
                notice_recycle = 0;
                break;
            case 1:
                tv_renumber_bookdetails.setText("每天");
                notice_recycle = 1;
                break;
            case 2:
                tv_renumber_bookdetails.setText("每周");
                notice_recycle = 2;
                break;
            case 3:
                tv_renumber_bookdetails.setText("每月");
                notice_recycle = 3;
                break;
            default:
                break;
        }
    }

    private void setRemindid(int remindid) {//提醒方式
        switch (remindid){
            case 0:
                tv_notic_bookdetails.setText("无");
                noticetime = 0;
                break;
            case 1:
                tv_notic_bookdetails.setText("会议开始前");
                noticetime = 1;
                break;
            case 2:
                tv_notic_bookdetails.setText("提前5分钟");
                noticetime = 2;
                break;
            case 3:
                tv_notic_bookdetails.setText("提前15分钟");
                noticetime = 3;
                break;
            case 4:
                tv_notic_bookdetails.setText("提前30分钟");
                noticetime = 4;
                break;
            case 5:
                tv_notic_bookdetails.setText("提前1个小时");
                noticetime = 5;
                break;
            case 6:
                tv_notic_bookdetails.setText("提前1天");
                noticetime = 6;
                break;
            default:
                break;
        }
    }

    private void sendType(String sendType) {//发送方式
        if(sendType.length()>1){
            String sendlist[] = sendType.split(",");
            for (int i=0;i<sendlist.length;i++){
                int sendtype = Integer.parseInt(sendlist[i]);
                if (i == 0){
                    setnotice(tv_noticeway_email_bookdetails,sendtype);
                }else if(i==1){
                    setnotice(tv_noticeway_message_bookdetail,sendtype);
                }else if(i==2){
                    setnotice(tv_noticeway_netout_bookdetail,sendtype);
                }
            }
        }else{
            int sendtype = Integer.parseInt(sendType);
            setnotice(tv_noticeway_email_bookdetails,sendtype);
        }
    }

    private void setnotice(TextView textView,int type){
        textView.setVisibility(View.VISIBLE);
        switch (type){
            case 1:
                textView.setText("邮件");
                email = true;
                break;
            case 2:
                textView.setText("短信");
                message = true;
                break;
            case 3:
                textView.setText("站内");
                netout = true;
                break;
            default:
                break;
        }
    }


    private void service(List<MeetingDetails.DataBeanX.DataBean.ServiceBean> aa) {//会议服务
        for (int i=0;i<aa.size();i++){
            String service = aa.get(i).getServicename();
            switch (i){
                case 0:
                    setText(tv_service_chaye,service);
                    break;
                case 1:
                    setText(tv_service_chabei,service);
                    break;
                case 2:
                    setText(tv_service_kaishui,service);
                    break;
                case 3:
                    setText(tv_service_zhuopan,service);
                    break;
                case 4:
                    setText(tv_service_guopan,service);
                    break;
                case 5:
                    setText(tv_service_biaoyu,service);
                    break;
                default:
                    break;
            }
        }
    }
    private void setText(TextView textview,String string){
        textview.setText(string);
        textview.setVisibility(View.VISIBLE);
        if (string.equals("茶叶")){
            has_select_chaye = 1;
        }else if (string.equals("茶杯")){
            has_select_chabei = 2;
        }else if (string.equals("开水")){
            has_select_kaishui = 3;
        }else if (string.equals("桌盘")){
            has_select_zhuopan = 4;
        }else if (string.equals("果盘")){
            has_select_guopan = 5;
        }else if (string.equals("标语")){
            has_select_biaoyu = 6;
        }
    }



    /**
     *@date2017/9/4
     *@author zhonghuibin
     *@description 选择会议需要的服务：茶叶、茶杯、开水、桌盘、果盘、标语
     */
    private void serviceFormeeting(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tv_service_chaye:
                        if (has_select_chaye == 1) {
                            tv_service_chaye.setBackgroundResource(R.drawable.circle_shape_bg_bbs_shadow);
                            tv_service_chaye.setTextColor(getResources().getColor(R.color.gray_sub));
                            has_select_chaye = 0;
                        } else {
                            tv_service_chaye.setBackgroundResource(R.drawable.circle_shape_bg_bbs_green);
                            tv_service_chaye.setTextColor(getResources().getColor(R.color.white));
                            has_select_chaye = 1;
                        }
                        break;
                    case R.id.tv_service_chabei:
                        if (has_select_chabei == 1) {
                            tv_service_chabei.setBackgroundResource(R.drawable.circle_shape_bg_bbs_shadow);
                            tv_service_chabei.setTextColor(getResources().getColor(R.color.gray_sub));
                            has_select_chabei = 0;
                        } else {
                            tv_service_chabei.setBackgroundResource(R.drawable.circle_shape_bg_bbs_green);
                            tv_service_chabei.setTextColor(getResources().getColor(R.color.white));
                            has_select_chabei = 1;
                        }
                        break;
                    case R.id.tv_service_kaishui:
                        if (has_select_kaishui == 1) {
                            tv_service_kaishui.setBackgroundResource(R.drawable.circle_shape_bg_bbs_shadow);
                            tv_service_kaishui.setTextColor(getResources().getColor(R.color.gray_sub));
                            has_select_kaishui = 0;
                        } else {
                            tv_service_kaishui.setBackgroundResource(R.drawable.circle_shape_bg_bbs_green);
                            tv_service_kaishui.setTextColor(getResources().getColor(R.color.white));
                            has_select_kaishui = 1;
                        }
                        break;
                    case R.id.tv_service_zhuopan:
                        if (has_select_zhuopan == 1) {
                            tv_service_zhuopan.setBackgroundResource(R.drawable.circle_shape_bg_bbs_shadow);
                            tv_service_zhuopan.setTextColor(getResources().getColor(R.color.gray_sub));
                            has_select_zhuopan = 0;
                        } else {
                            tv_service_zhuopan.setBackgroundResource(R.drawable.circle_shape_bg_bbs_green);
                            tv_service_zhuopan.setTextColor(getResources().getColor(R.color.white));
                            has_select_zhuopan = 1;
                        }
                        break;
                    case R.id.tv_service_guopan:
                        if (has_select_guopan == 1) {
                            tv_service_guopan.setBackgroundResource(R.drawable.circle_shape_bg_bbs_shadow);
                            tv_service_guopan.setTextColor(getResources().getColor(R.color.gray_sub));
                            has_select_guopan = 0;
                        } else {
                            tv_service_guopan.setBackgroundResource(R.drawable.circle_shape_bg_bbs_green);
                            tv_service_guopan.setTextColor(getResources().getColor(R.color.white));
                            has_select_guopan = 1;
                        }
                        break;
                    case R.id.tv_service_biaoyu:
                        if (has_select_biaoyu == 1) {
                            tv_service_biaoyu.setBackgroundResource(R.drawable.circle_shape_bg_bbs_shadow);
                            tv_service_biaoyu.setTextColor(getResources().getColor(R.color.gray_sub));
                            has_select_biaoyu = 0;
                        } else {
                            tv_service_biaoyu.setBackgroundResource(R.drawable.circle_shape_bg_bbs_green);
                            tv_service_biaoyu.setTextColor(getResources().getColor(R.color.white));
                            has_select_biaoyu = 1;
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                finish();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     *@date2017/10/30
     *@author zhonghuibin
     *@description 是否放弃本次添加会议室
     */
    private void gobackPopwindows() {
        View view = LayoutInflater.from(this).inflate(R.layout.popwindows_goback, null);
        TextView sure_giveup = (TextView) view.findViewById(R.id.sure_giveup);
        TextView cancle_giveup = (TextView) view.findViewById(R.id.cancle_giveup);
        sure_giveup.setOnClickListener(this);
        cancle_giveup.setOnClickListener(this);
        int px = PublicUtils.convertDIP2PX(this, 300);
        int py = PublicUtils.convertDIP2PX(this, 140);
        popupWindow = new PopupWindow(view, px, py+40);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.asset_showwindows_bg));
        backgroundAlpha(0.5f);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(rl_top_bookroom, 90,600);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });
    }
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_back:
                break;
            case R.id.sure_giveup:
                if (popupWindow.isShowing()){
                    popupWindow.dismiss();
                }
                finish();
                break;
            case R.id.cancle_giveup:
                if (popupWindow.isShowing()){
                    popupWindow.dismiss();
                }
                break;
            case R.id.iv_back_bookdetails:
                finish();
                break;
            case R.id.tv_notic_bookdetails:
//                Intent intent = new Intent(this,NoticeMeetingTime.class);
//                startActivityForResult(intent,REQUSET_BOOKROOM_NOTICE);
                break;
            case R.id.tv_renumber_bookdetails:
//                Intent intent_recycle = new Intent(this,RecycleTimesActivity.class);
//                startActivityForResult(intent_recycle,REQUSET_BOOKROOM_RECYCLE);
                break;
            case R.id.tv_noticeway_email_bookdetails:
                if (email){
                    tv_noticeway_email_bookdetails.setBackgroundResource(R.drawable.circle_shape_bg_bbs_shadow);
                    tv_noticeway_email_bookdetails.setTextColor(this.getResources().getColor(R.color.black));
                    email = false;
                }else{
                    tv_noticeway_email_bookdetails.setBackgroundResource(R.drawable.circle_shape_bg_bbs_green);
                    tv_noticeway_email_bookdetails.setTextColor(this.getResources().getColor(R.color.white));
                    email = true;
                }
                break;
            case R.id.iv_askname_bookdetails:
                Intent intent_people = new Intent(MeetingRoomDetails.this,MeetingUserActivity.class);
                if (peopleId!=null){
                    intent_people.putExtra("uIds",peopleId);
                }
                startActivityForResult(intent_people,REQUSET_BOOKROOM_PEOPLE);
                break;
            case R.id.tv_noticeway_message_bookdetail:
                if (message){
                    tv_noticeway_message_bookdetail.setBackgroundResource(R.drawable.circle_shape_bg_bbs_shadow);
                    tv_noticeway_message_bookdetail.setTextColor(this.getResources().getColor(R.color.black));
                    message = false;
                }else{
                    tv_noticeway_message_bookdetail.setBackgroundResource(R.drawable.circle_shape_bg_bbs_green);
                    tv_noticeway_message_bookdetail.setTextColor(this.getResources().getColor(R.color.white));
                    message = true;
                }
                break;
            case R.id.tv_noticeway_netout_bookdetail:
                if (netout){
                    tv_noticeway_netout_bookdetail.setBackgroundResource(R.drawable.circle_shape_bg_bbs_shadow);
                    tv_noticeway_netout_bookdetail.setTextColor(this.getResources().getColor(R.color.black));
                    netout = false;
                }else{
                    tv_noticeway_netout_bookdetail.setBackgroundResource(R.drawable.circle_shape_bg_bbs_green);
                    tv_noticeway_netout_bookdetail.setTextColor(this.getResources().getColor(R.color.white));
                    netout = true;
                }
                break;
            case R.id.tv_select_meettime_detail:
                Intent intent_meetingtime = new Intent(MeetingRoomDetails.this,MeeTingDateActivity.class);
                startActivityForResult(intent_meetingtime,REQUSET_BOOKROOM_TIME);
                break;
            case R.id.tv_acccess:
                onChiese(tv_acccess,2);
                break;
            case R.id.tv_prieve:
                onChiese(tv_prieve,3);
                break;
            case R.id.tv_refuse:
                onChiese(tv_refuse,4);
                break;
            case R.id.rl_bookmeetdetails_cancle:
                cancleMeeting_(shi.getCompanyId(),meetingRoomId,2);
                //取消会议
                break;
            default:
                break;
        }
    }

//    /**
//     *@date2017/9/7
//     *@author zhonghuibin
//     *@description 会议室预定提交页面
//     * 参数依次是：公司id 用户id 机构cede串 机构id 角色id 预定会议人id 预定会议人用户名 会议室名字 开始时间 结束时间
//     * 参加会议人id 参加会议人名字 会议室id 会议室名字 层id 楼id 楼名字 第几层 会议服务id 提醒id 是否重复id 备注 发送方式
//     */
//    private void sub_MeetingDetail(Context context,String url,int id,int company_id,int data_auth_user,String data_rog,
//                                   String data_org_new,String data_role,int uid,String uname,String meetingname,String starttime,
//                                   String endtime,String peopuid,String peopname,int conferenceid,String roomname,int floorid,
//                                   int buildingid,String bulidingname,String floorname,String serveid,int remindid,int repeatid,
//                                   String remarks,String send_type, final ApiRequestFactory.HttpCallBackListener httpCallBackListener,
//                                   boolean isShowDialog) {
//        Map<String ,String> map = new HashMap<>();
//        map.put("company_id",company_id+"");
//        map.put("data_auth_user",data_auth_user+"");
//        map.put("data_rog",data_rog+"");
//        map.put("data_org_new",data_org_new);
//        map.put("data_role",data_role);
//        map.put("uid",uid+"");
//        map.put("id",id+"");
//        map.put("uname",uname);
//        map.put("meetingname",meetingname);
//        map.put("starttime",starttime);
//        map.put("endtime",endtime);
//        map.put("peopuid",peopuid);
//        map.put("peopname",peopname);
//        map.put("conferenceid",conferenceid+"");
//        map.put("roomname",roomname);
//        map.put("floorid",floorid+"");
//        map.put("buildingid",buildingid+"");
//        map.put("bulidingname",bulidingname);
//        map.put("floorname",floorname);
//        map.put("serveid",serveid+"");
//        map.put("remindid",remindid+"");
//        map.put("repeatid",repeatid+"");
//        map.put("remarks",remarks);
//        map.put("send_type",send_type+"");
//        ApiRequestFactory.postJson(context,url,map,httpCallBackListener,isShowDialog);
//    }

    /**
     *@date2017/10/31
     *@author zhonghuibin
     *@description 获取会议室详情
     */
    private void getMeetingDetails(Context context,String url,int meetingId,final ApiRequestFactory.HttpCallBackListener httpCallBackListener,
                                   boolean isShowDialog){
        Map<String,String> map = new HashMap<>();
        map.put("id",meetingRoomId+"");
        ApiRequestFactory.postJson(context,url,map,httpCallBackListener,isShowDialog);
    }

    /**
     *@date2017/10/31
     *@author zhonghuibin
     *@description 取消会议
     */
    private void cancleMeeting_(int company_id,int meetingId,int type){
        cancleMeeting(MeetingRoomDetails.this,"m=Androidstation&a=deleteinfo",company_id,meetingId,type,
        new ApiRequestFactory.HttpCallBackListener() {
            @Override
            public void onSuccess(String response, String url, int id) {
                Toast.makeText(MeetingRoomDetails.this,"取消成功",Toast.LENGTH_LONG).show();
            }
            @Override
            public void failure(Call call, Exception e, int id) {
                Toast.makeText(MeetingRoomDetails.this,"取消失败",Toast.LENGTH_LONG).show();
            }
        },true);
        finish();
    }
    private void cancleMeeting(Context context,String url,int company_id,int meetingId,int type,
                               final ApiRequestFactory.HttpCallBackListener httpCallBackListener,
                               boolean isShowDialog){
        Map<String,String> map = new HashMap<>();
        map.put("company_id",company_id+"");
        map.put("id",meetingRoomId+"");
        map.put("type",type+"");
        ApiRequestFactory.postJson(context,url,map,httpCallBackListener,isShowDialog);
    }


    /**
     *@date2017/9/4
     *@author zhonghuibin
     *@description 选取会议提醒时间返回赋值
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUSET_BOOKROOM_NOTICE:
                    tv_notic_bookdetails.setText(data.getStringExtra("select_type"));
                    noticetime = data.getIntExtra("select_type_int", 0);
                    break;
                case REQUSET_BOOKROOM_RECYCLE:
                    tv_renumber_bookdetails.setText(data.getStringExtra("select_type"));
                    notice_recycle = data.getIntExtra("select_type_int", 0);
                    break;
                case REQUSET_BOOKROOM_TIME:
                    if(data != null){
                        meetingTime_begin = data.getStringExtra("time").substring(0, 19);
                        meetingTime_end = data.getStringExtra("time").substring(20, data.getStringExtra("time").length());
                        tv_select_meettime_detail.setText(data.getStringExtra("time").substring(0,16)+"~" + data.getStringExtra("time").substring(31, 36));
                    }
                    break;
                case REQUSET_BOOKROOM_PEOPLE:
                    if (data != null){
                        peopleName = data.getStringExtra("usName");
                        peopleId = data.getStringExtra("usId");
                        listName = data.getStringExtra("usName").split(",");
                        listId = data.getStringExtra("usId").split(",");
                        setListViewHeightBasedOnChildren(gridview_people_meeting,listName,this);
                        gridview_people_meeting.setAdapter(new MeetingName_GridviewAdapter(MeetingRoomDetails.this,listName,listId));
                    }
                    break;
            }
        }
    }


    /**
     *@date2017/10/19
     *@author zhonghuibin
     *@description 邀请的参会人员gridview在scrallview里面，需要对gridview根据item的条目进行动态设置高度
     */
    public static void setListViewHeightBasedOnChildren(GridView gridview_people_meeting,String[] listName,Context context) {
        ViewGroup.LayoutParams params = gridview_people_meeting.getLayoutParams();
        // 设置高度
        if (listName.length%5==0){
            params.height = PublicUtils.convertDIP2PX(context, (listName.length/5)*75);
        } else {
            params.height = PublicUtils.convertDIP2PX(context, (listName.length/5+1)*75);
        }
        // 设置参数
        gridview_people_meeting.setLayoutParams(params);
    }

    /**
     *@date2017/9/18
     *@author zhonghuibin
     *@description 数组删除元素工具方法
     */
    private static String[] remove(String[] arr, String num) {
        String[] tmp = new String[arr.length - 1];
        int idx = 0;
        boolean hasRemove = false;
        for (int i = 0; i < arr.length; i++) {
            if (!hasRemove && arr[i].equals(num)) {
                hasRemove = true;
                continue;
            }
            tmp[idx++] = arr[i];
        }
        return tmp;
    }

    /**
     *@date2017/9/14
     *@author zhonghuibin
     *@description 会议提醒方式，通过下面方法得到传给服务器的参数 1：邮件 2:短息 3：站内
     */
    private String getNoticeWay(boolean email,boolean message,boolean netout){
        if ((email&&!message&&!netout)||(!email&&!message&&!netout)){
            noitce_way ="1";
        }else if(!email&&message&&!netout){
            noitce_way ="2";
        }else if(!email&&!message&&netout){
            noitce_way ="3";
        }else if(email&&message&&!netout){
            noitce_way ="1,2";
        }else if(email&&!message&&netout){
            noitce_way ="1,3";
        }else if(!email&&message&&netout){
            noitce_way ="2,3";
        }else if(email&&message&&netout){
            noitce_way ="1,2,3";
        }
        return noitce_way;
    }

    /**
     *@date2017/9/14
     *@author zhonghuibin
     *@description 会议服务的返回
     */
    private String getMeetingService(int has_select_chaye,int has_select_chabei,int has_select_kaishui,
                                     int has_select_zhuopan,int has_select_guopan,int has_select_biaoyu){
        String MeetingService ="";
        List<Integer> list = new ArrayList<>();
        list.add(has_select_chaye);
        list.add(has_select_chabei);
        list.add(has_select_kaishui);
        list.add(has_select_zhuopan);
        list.add(has_select_guopan);
        list.add(has_select_biaoyu);
        for (int i=0;i<list.size();i++){
            if (list.get(i)!=0){
                MeetingService = MeetingService+","+(i+1);
            }
        }
        return MeetingService.length() == 0?"":MeetingService.substring(1,MeetingService.length());
    }
}
