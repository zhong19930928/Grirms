package com.yunhu.yhshxc.MeetingAgenda;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.yunhu.yhshxc.MeetingAgenda.adapter.MeetingName_GridviewAdapter;
import com.yunhu.yhshxc.MeetingAgenda.bo.MeetingRoomDetail;
import com.yunhu.yhshxc.MeetingAgenda.bo.MeetingServiceid;
import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.addressBook.db.AdressBookUserDB;
import com.yunhu.yhshxc.core.ApiRequestFactory;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * ＠author zhonghuibin
 * create at 2017/9/1.
 *
 * describe 会议预定提交页面
 */
public class BookMroomDetails extends Activity implements View.OnClickListener{
    public static final int REQUSET_BOOKROOM_NOTICE = 4;
    public static final int REQUSET_BOOKROOM_RECYCLE = 5;
    public static final int REQUSET_BOOKROOM_PEOPLE = 6;
    public static final int REQUSET_BOOKROOM_TIME = 7;
    private RelativeLayout rl_bookmeetdetails_submit,rl_select_meettime_detail;
    private TextView meetingroom_name_meetdetails,meetingroom_bumber_meetdetails;
    private EditText et_meetsubject_meetdetails;
    private RelativeLayout rl_top_bookroom;
    private TextView tv_service_chaye,tv_service_chabei,tv_service_kaishui,
            tv_service_zhuopan,tv_service_guopan,tv_service_biaoyu,tv_service7,tv_service8;
    private TextView tv_notic_bookdetails,tv_select_meettime_detail,tv_renumber_bookdetails;
    private ImageView iv_askname_bookdetails,iv_back_bookdetails;
    private TextView tv_noticeway_email_bookdetails,tv_noticeway_message_bookdetail,
            tv_noticeway_netout_bookdetail;
    private EditText et_beizhu_meetdetails;
    //会议需要的服务标识
    private int has_select_chaye,has_select_chabei,has_select_kaishui,
            has_select_zhuopan,has_select_guopan,has_select_biaoyu,int7,int8;
    //会议的三种发送方式标识：站内、邮件、短信
    private boolean netout=false,message=false,email=false;
    private int noticetime = 2,notice_recycle=0;
    private String noitce_way,meetingTime_begin,meetingTime_end,peopleName,peopleId;
    private MeetingRoomDetail.DataBeanX.DataBean dataBean;
    private String[] listName,listId;
    private GridView gridview_people_meeting;
    private PopupWindow popupWindow;
    private ImageView iv_article1,iv_article2,iv_article3,iv_article4,iv_article5,iv_article6,iv_article7,
    iv_article8,iv_article9,iv_article10,iv_article11;
    private LinearLayout ll_meetroom_article;
    private SharedPreferencesUtil sh;
    private MeetingServiceid meetingServiceid;
    private List<MeetingServiceid.DataBeanX.DataBean> data;
    private Date curDate,meetingDate;
    private SimpleDateFormat formatter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmroomdetails);
        //避免进入页面直接弹出软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dataBean = (MeetingRoomDetail.DataBeanX.DataBean) getIntent().getSerializableExtra("DataBean");
        meetingTime_begin = getIntent().getStringExtra("meeting_begin");
        meetingTime_end = getIntent().getStringExtra("meeting_end");
        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        curDate=new Date(System.currentTimeMillis());//获取当前时间
        try {
            meetingDate = formatter.parse(meetingTime_end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sh = SharedPreferencesUtil.getInstance(BookMroomDetails.this);
        initView();
    }



    private void setService( List<MeetingServiceid.DataBeanX.DataBean> data) {
        for (int i=0;i<data.size();i++){
            String service = data.get(i).getName();
            String id = data.get(i).getId();
            switch (i){
                case 0:
                    setText(tv_service_chaye,service,id);
                    break;
                case 1:
                    setText(tv_service_chabei,service,id);
                    break;
                case 2:
                    setText(tv_service_kaishui,service,id);
                    break;
                case 3:
                    setText(tv_service_zhuopan,service,id);
                    break;
                case 4:
                    setText(tv_service_guopan,service,id);
                    break;
                case 5:
                    setText(tv_service_biaoyu,service,id);
                    break;
                case 6:
                    setText(tv_service7,service,id);
                    break;
                case 7:
                    setText(tv_service8,service,id);
                    break;
                default:
                    break;
            }
        }
    }
    private void setText(TextView textview,String string,String id){
        textview.setText(string);
        textview.setVisibility(View.VISIBLE);
        serviceFormeeting(textview);
    }
    private void setIvarticle() {
        if (dataBean.getArticles() != null && dataBean.getArticles().size()>0){
            if (dataBean.getArticles().size()>3){
                ll_meetroom_article.setVisibility(View.VISIBLE);
            }else{
                ll_meetroom_article.setVisibility(View.GONE);
            }
            int aa = dataBean.getArticles().size();
           switch (dataBean.getArticles().size()){
               case 1:
                   Glide.with(this).load(dataBean.getArticles().get(0).getThumb()).into(iv_article1);
                   break;
               case 2:
                   Glide.with(this).load(dataBean.getArticles().get(0).getThumb()).into(iv_article1);
                   Glide.with(this).load(dataBean.getArticles().get(1).getThumb()).into(iv_article2);
                   break;
               case 3:
                   Glide.with(this).load(dataBean.getArticles().get(0).getThumb()).into(iv_article1);
                   Glide.with(this).load(dataBean.getArticles().get(1).getThumb()).into(iv_article2);
                   Glide.with(this).load(dataBean.getArticles().get(2).getThumb()).into(iv_article3);
                   break;
               case 4:
                   Glide.with(this).load(dataBean.getArticles().get(0).getThumb()).into(iv_article4);
                   Glide.with(this).load(dataBean.getArticles().get(1).getThumb()).into(iv_article5);
                   Glide.with(this).load(dataBean.getArticles().get(2).getThumb()).into(iv_article6);
                   Glide.with(this).load(dataBean.getArticles().get(3).getThumb()).into(iv_article7);
                   break;
               case 5:
                   Glide.with(this).load(dataBean.getArticles().get(0).getThumb()).into(iv_article4);
                   Glide.with(this).load(dataBean.getArticles().get(1).getThumb()).into(iv_article5);
                   Glide.with(this).load(dataBean.getArticles().get(2).getThumb()).into(iv_article6);
                   Glide.with(this).load(dataBean.getArticles().get(3).getThumb()).into(iv_article7);
                   Glide.with(this).load(dataBean.getArticles().get(4).getThumb()).into(iv_article8);
                   break;
               case 6:
                   Glide.with(this).load(dataBean.getArticles().get(0).getThumb()).into(iv_article4);
                   Glide.with(this).load(dataBean.getArticles().get(1).getThumb()).into(iv_article5);
                   Glide.with(this).load(dataBean.getArticles().get(2).getThumb()).into(iv_article6);
                   Glide.with(this).load(dataBean.getArticles().get(3).getThumb()).into(iv_article7);
                   Glide.with(this).load(dataBean.getArticles().get(4).getThumb()).into(iv_article8);
                   Glide.with(this).load(dataBean.getArticles().get(5).getThumb()).into(iv_article9);
                   break;
               case 7:
                   Glide.with(this).load(dataBean.getArticles().get(0).getThumb()).into(iv_article4);
                   Glide.with(this).load(dataBean.getArticles().get(1).getThumb()).into(iv_article5);
                   Glide.with(this).load(dataBean.getArticles().get(2).getThumb()).into(iv_article6);
                   Glide.with(this).load(dataBean.getArticles().get(3).getThumb()).into(iv_article7);
                   Glide.with(this).load(dataBean.getArticles().get(4).getThumb()).into(iv_article8);
                   Glide.with(this).load(dataBean.getArticles().get(5).getThumb()).into(iv_article9);
                   Glide.with(this).load(dataBean.getArticles().get(6).getThumb()).into(iv_article10);
                   break;
               case 8:
                   Glide.with(this).load(dataBean.getArticles().get(0).getThumb()).into(iv_article4);
                   Glide.with(this).load(dataBean.getArticles().get(1).getThumb()).into(iv_article5);
                   Glide.with(this).load(dataBean.getArticles().get(2).getThumb()).into(iv_article6);
                   Glide.with(this).load(dataBean.getArticles().get(3).getThumb()).into(iv_article7);
                   Glide.with(this).load(dataBean.getArticles().get(4).getThumb()).into(iv_article8);
                   Glide.with(this).load(dataBean.getArticles().get(5).getThumb()).into(iv_article9);
                   Glide.with(this).load(dataBean.getArticles().get(6).getThumb()).into(iv_article10);
                   Glide.with(this).load(dataBean.getArticles().get(7).getThumb()).into(iv_article11);
                   break;
               case 9:
                   Glide.with(this).load(dataBean.getArticles().get(0).getThumb()).into(iv_article4);
                   Glide.with(this).load(dataBean.getArticles().get(1).getThumb()).into(iv_article5);
                   Glide.with(this).load(dataBean.getArticles().get(2).getThumb()).into(iv_article6);
                   Glide.with(this).load(dataBean.getArticles().get(3).getThumb()).into(iv_article7);
                   Glide.with(this).load(dataBean.getArticles().get(4).getThumb()).into(iv_article8);
                   Glide.with(this).load(dataBean.getArticles().get(5).getThumb()).into(iv_article9);
                   Glide.with(this).load(dataBean.getArticles().get(6).getThumb()).into(iv_article10);
                   Glide.with(this).load(dataBean.getArticles().get(7).getThumb()).into(iv_article11);
                   break;
               default:
                   break;
           }
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
                        if (has_select_chaye == Integer.parseInt(data.get(0).getId())) {
                            tv_service_chaye.setBackgroundResource(R.drawable.circle_shape_bg_bbs_shadow);
                            tv_service_chaye.setTextColor(getResources().getColor(R.color.gray_sub));
                            has_select_chaye = 0;
                        } else {
                            tv_service_chaye.setBackgroundResource(R.drawable.circle_shape_bg_bbs_green);
                            tv_service_chaye.setTextColor(getResources().getColor(R.color.white));
                            has_select_chaye = Integer.parseInt(data.get(0).getId());
                        }
                        break;
                    case R.id.tv_service_chabei:
                        if (has_select_chabei == Integer.parseInt(data.get(1).getId())) {
                            tv_service_chabei.setBackgroundResource(R.drawable.circle_shape_bg_bbs_shadow);
                            tv_service_chabei.setTextColor(getResources().getColor(R.color.gray_sub));
                            has_select_chabei = 0;
                        } else {
                            tv_service_chabei.setBackgroundResource(R.drawable.circle_shape_bg_bbs_green);
                            tv_service_chabei.setTextColor(getResources().getColor(R.color.white));
                            has_select_chabei = Integer.parseInt(data.get(1).getId());
                        }
                        break;
                    case R.id.tv_service_kaishui:
                        if (has_select_kaishui == Integer.parseInt(data.get(2).getId())) {
                            tv_service_kaishui.setBackgroundResource(R.drawable.circle_shape_bg_bbs_shadow);
                            tv_service_kaishui.setTextColor(getResources().getColor(R.color.gray_sub));
                            has_select_kaishui = 0;
                        } else {
                            tv_service_kaishui.setBackgroundResource(R.drawable.circle_shape_bg_bbs_green);
                            tv_service_kaishui.setTextColor(getResources().getColor(R.color.white));
                            has_select_kaishui = Integer.parseInt(data.get(2).getId());
                        }
                        break;
                    case R.id.tv_service_zhuopan:
                        if (has_select_zhuopan == Integer.parseInt(data.get(3).getId())) {
                            tv_service_zhuopan.setBackgroundResource(R.drawable.circle_shape_bg_bbs_shadow);
                            tv_service_zhuopan.setTextColor(getResources().getColor(R.color.gray_sub));
                            has_select_zhuopan = 0;
                        } else {
                            tv_service_zhuopan.setBackgroundResource(R.drawable.circle_shape_bg_bbs_green);
                            tv_service_zhuopan.setTextColor(getResources().getColor(R.color.white));
                            has_select_zhuopan = Integer.parseInt(data.get(3).getId());
                        }
                        break;
                    case R.id.tv_service_guopan:
                        if (has_select_guopan == Integer.parseInt(data.get(4).getId())) {
                            tv_service_guopan.setBackgroundResource(R.drawable.circle_shape_bg_bbs_shadow);
                            tv_service_guopan.setTextColor(getResources().getColor(R.color.gray_sub));
                            has_select_guopan = 0;
                        } else {
                            tv_service_guopan.setBackgroundResource(R.drawable.circle_shape_bg_bbs_green);
                            tv_service_guopan.setTextColor(getResources().getColor(R.color.white));
                            has_select_guopan = Integer.parseInt(data.get(4).getId());
                        }
                        break;
                    case R.id.tv_service_biaoyu:
                        if (has_select_biaoyu == Integer.parseInt(data.get(5).getId())) {
                            tv_service_biaoyu.setBackgroundResource(R.drawable.circle_shape_bg_bbs_shadow);
                            tv_service_biaoyu.setTextColor(getResources().getColor(R.color.gray_sub));
                            has_select_biaoyu = 0;
                        } else {
                            tv_service_biaoyu.setBackgroundResource(R.drawable.circle_shape_bg_bbs_green);
                            tv_service_biaoyu.setTextColor(getResources().getColor(R.color.white));
                            has_select_biaoyu = Integer.parseInt(data.get(5).getId());
                        }
                        break;
                    case R.id.tv_service7:
                        if (int7 == Integer.parseInt(data.get(5).getId())) {
                            tv_service7.setBackgroundResource(R.drawable.circle_shape_bg_bbs_shadow);
                            tv_service7.setTextColor(getResources().getColor(R.color.gray_sub));
                            int7 = 0;
                        } else {
                            tv_service7.setBackgroundResource(R.drawable.circle_shape_bg_bbs_green);
                            tv_service7.setTextColor(getResources().getColor(R.color.white));
                            int7 = Integer.parseInt(data.get(6).getId());
                        }
                        break;
                    case R.id.tv_service8:
                        if (int8 == Integer.parseInt(data.get(7).getId())) {
                            tv_service8.setBackgroundResource(R.drawable.circle_shape_bg_bbs_shadow);
                            tv_service8.setTextColor(getResources().getColor(R.color.gray_sub));
                            int8 = 0;
                        } else {
                            tv_service8.setBackgroundResource(R.drawable.circle_shape_bg_bbs_green);
                            tv_service8.setTextColor(getResources().getColor(R.color.white));
                            int8 = Integer.parseInt(data.get(7).getId());
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
                gobackPopwindows();
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
        int py = PublicUtils.convertDIP2PX(this, 150);
        popupWindow = new PopupWindow(view, px, py+40);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_shape_bg_bbs_while2));
        backgroundAlpha(0.5f);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(rl_top_bookroom, Gravity.CENTER,0,0);
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
                gobackPopwindows();
                break;
            case R.id.tv_notic_bookdetails:
                final Intent intent = new Intent(this,NoticeMeetingTime.class);
                startActivityForResult(intent,REQUSET_BOOKROOM_NOTICE);
                break;
            case R.id.tv_renumber_bookdetails:
                Intent intent_recycle = new Intent(this,RecycleTimesActivity.class);
                startActivityForResult(intent_recycle,REQUSET_BOOKROOM_RECYCLE);
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
                Intent intent_people = new Intent(BookMroomDetails.this,MeetingUserActivity.class);
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
                Intent intent_meetingtime = new Intent(BookMroomDetails.this,MeeTingDateActivity.class);
                startActivityForResult(intent_meetingtime,REQUSET_BOOKROOM_TIME);
                break;
            case R.id.rl_bookmeetdetails_submit:
                String org_cede = new AdressBookUserDB(this).findUserById(sh.getUserId()).getOc();
                String meetservice =getMeetingService(has_select_chaye,has_select_chabei,has_select_kaishui,
                        has_select_zhuopan,has_select_guopan,has_select_biaoyu,int7,int8);
                if (meetingTime_begin==null ||meetingTime_begin.equals("")){
                    Toast.makeText(BookMroomDetails.this,"请选择会议时间",Toast.LENGTH_SHORT).show();
                }else if(et_meetsubject_meetdetails.getText().toString().equals("")){
                    Toast.makeText(BookMroomDetails.this,"请填写会议主题",Toast.LENGTH_SHORT).show();
                }else if(curDate.after(meetingDate)){
                    Toast.makeText(BookMroomDetails.this,"会议时间已过期",Toast.LENGTH_SHORT).show();
                }else if(peopleName==null||peopleName.equals("")){
                    Toast.makeText(BookMroomDetails.this,"请选择参会人",Toast.LENGTH_SHORT).show();
                }else{
                    sub_MeetingDetail(BookMroomDetails.this, "m=Androidstation&a=addmeetingdetails", sh.getCompanyId(), sh.getUserId(),
                            org_cede,sh.getOrgId()+"", sh.getRoleId()+"", sh.getUserId(), sh.getUserName(),
                            et_meetsubject_meetdetails.getText().toString(), meetingTime_begin, meetingTime_end,peopleId,
                            peopleName, Integer.parseInt(dataBean.getId()), dataBean.getName(),Integer.parseInt(dataBean.getFloorid()),
                            Integer.parseInt(dataBean.getBuildingid()),dataBean.getBuildingname(), dataBean.getFloorname()+"",
                            meetservice,noticetime,notice_recycle,et_beizhu_meetdetails.getText().toString(),
                            getNoticeWay(email,message,netout),new ApiRequestFactory.HttpCallBackListener() {
                                @Override
                                public void onSuccess(String response, String url, int id) {
                                    Toast.makeText(BookMroomDetails.this,"提交成功",Toast.LENGTH_LONG).show();
                                    Intent intent1 = new Intent();
                                    intent1.putExtra("success",1);
                                    setResult(RESULT_OK,intent1);
                                    finish();
                                }
                                @Override
                                public void failure(Call call, Exception e, int id) {
                                    Toast.makeText(BookMroomDetails.this,"提交失败",Toast.LENGTH_LONG).show();
                                }
                    },true);
                }
                break;
            default:
                break;
        }
    }

    private void initView() {
        iv_back_bookdetails = (ImageView) findViewById(R.id.iv_back_bookdetails);
        iv_askname_bookdetails = (ImageView) findViewById(R.id.iv_askname_bookdetails);
        tv_select_meettime_detail = (TextView) findViewById(R.id.tv_select_meettime_detail);
        gridview_people_meeting = (GridView) findViewById(R.id.gridview_people_meeting);
        ll_meetroom_article = (LinearLayout) findViewById(R.id.ll_meetroom_article);
        meetingroom_name_meetdetails = (TextView) findViewById(R.id.meetingroom_name_meetdetails);
        meetingroom_bumber_meetdetails = (TextView) findViewById(R.id.meetingroom_bumber_meetdetails);
        rl_top_bookroom = (RelativeLayout) findViewById(R.id.rl_top_bookroom);
        tv_service_chaye = (TextView) findViewById(R.id.tv_service_chaye);
        tv_service_chabei = (TextView) findViewById(R.id.tv_service_chabei);
        tv_service_kaishui = (TextView) findViewById(R.id.tv_service_kaishui);
        tv_service_zhuopan = (TextView) findViewById(R.id.tv_service_zhuopan);
        tv_service_guopan = (TextView) findViewById(R.id.tv_service_guopan);
        tv_service_biaoyu = (TextView) findViewById(R.id.tv_service_biaoyu);
        tv_service7 = (TextView) findViewById(R.id.tv_service7);
        tv_service8 = (TextView) findViewById(R.id.tv_service8);
        tv_notic_bookdetails = (TextView) findViewById(R.id.tv_notic_bookdetails);
        rl_bookmeetdetails_submit = (RelativeLayout) findViewById(R.id.rl_bookmeetdetails_submit);
        rl_select_meettime_detail = (RelativeLayout) findViewById(R.id.rl_select_meettime_detail);
        tv_renumber_bookdetails = (TextView) findViewById(R.id.tv_renumber_bookdetails);
        tv_noticeway_email_bookdetails = (TextView) findViewById(R.id.tv_noticeway_email_bookdetails);
        tv_noticeway_message_bookdetail = (TextView) findViewById(R.id.tv_noticeway_message_bookdetail);
        tv_noticeway_netout_bookdetail = (TextView) findViewById(R.id.tv_noticeway_netout_bookdetail);
        et_meetsubject_meetdetails = (EditText) findViewById(R.id.et_meetsubject_meetdetails);
        et_beizhu_meetdetails = (EditText) findViewById(R.id.et_beizhu_meetdetails);
        iv_article1 = (ImageView) findViewById(R.id.iv_article1);
        iv_article2 = (ImageView) findViewById(R.id.iv_article2);
        iv_article3 = (ImageView) findViewById(R.id.iv_article3);
        iv_article4 = (ImageView) findViewById(R.id.iv_article4);
        iv_article5 = (ImageView) findViewById(R.id.iv_article5);
        iv_article6 = (ImageView) findViewById(R.id.iv_article6);
        iv_article7 = (ImageView) findViewById(R.id.iv_article7);
        iv_article8 = (ImageView) findViewById(R.id.iv_article8);
        iv_article9 = (ImageView) findViewById(R.id.iv_article9);
        iv_article10 = (ImageView) findViewById(R.id.iv_article10);
        iv_article11 = (ImageView) findViewById(R.id.iv_article11);
        setIvarticle();
        tv_noticeway_netout_bookdetail.setOnClickListener(this);
        tv_noticeway_message_bookdetail.setOnClickListener(this);
        tv_noticeway_email_bookdetails.setOnClickListener(this);
        tv_renumber_bookdetails.setOnClickListener(this);
        iv_back_bookdetails.setOnClickListener(this);
        tv_select_meettime_detail.setOnClickListener(this);
        iv_askname_bookdetails.setOnClickListener(this);
        rl_bookmeetdetails_submit.setOnClickListener(this);
        tv_notic_bookdetails.setOnClickListener(this);
        serviceFormeeting(tv_service_chaye);
        serviceFormeeting(tv_service_chabei);
        serviceFormeeting(tv_service_kaishui);
        serviceFormeeting(tv_service_zhuopan);
        serviceFormeeting(tv_service_guopan);
        serviceFormeeting(tv_service_biaoyu);
        serviceFormeeting(tv_service7);
        serviceFormeeting(tv_service8);
        meetingroom_name_meetdetails.setText(dataBean.getBuildingname()+"座"+dataBean.getFloorname()+"层"+dataBean.getName()+"室");
        meetingroom_bumber_meetdetails.setText("规格："+dataBean.getGalleryful()+"人");
        if (listName!=null && listName.length ==0){
            for (int i=0;i<listName.length;i++){
                peopleName = peopleName+","+listName[i];
                peopleId = peopleId+","+listId[i];
            }
        }

        getService(BookMroomDetails.this, "m=Androidstation&a=ConferenceService", sh.getCompanyId(), new ApiRequestFactory.HttpCallBackListener() {
            @Override
            public void onSuccess(String response, String url, int id) {
                try {
                    meetingServiceid = new Gson().fromJson(response,MeetingServiceid.class);
                    data = meetingServiceid.getData().getData();
                    setService(data);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void failure(Call call, Exception e, int id) {

            }
        },false);
    }

    /**
     *@date2017/9/7
     *@author zhonghuibin
     *@description 会议室预定提交页面
     * 参数依次是：公司id 用户id 机构cede串 机构id 角色id 预定会议人id 预定会议人用户名 会议室名字 开始时间 结束时间
     * 参加会议人id 参加会议人名字 会议室id 会议室名字 层id 楼id 楼名字 第几层 会议服务id 提醒id 是否重复id 备注 发送方式
     */
    private void sub_MeetingDetail(Context context,String url,int company_id,int data_auth_user,String data_rog,
                                   String data_org_new,String data_role,int uid,String uname,String meetingname,String starttime,
                                   String endtime,String peopuid,String peopname,int conferenceid,String roomname,int floorid,
                                   int buildingid,String bulidingname,String floorname,String serveid,int remindid,int repeatid,
                                   String remarks,String send_type, final ApiRequestFactory.HttpCallBackListener httpCallBackListener,
                                   boolean isShowDialog) {
        Map<String ,String> map = new HashMap<>();
        map.put("company_id",company_id+"");
        map.put("data_auth_user",data_auth_user+"");
        map.put("data_rog",data_rog+"");
        map.put("data_org_new",data_org_new);
        map.put("data_role",data_role);
        map.put("uid",uid+"");
        map.put("uname",uname);
        map.put("meetingname",meetingname);
        map.put("starttime",starttime);
        map.put("endtime",endtime);
        map.put("peopuid",peopuid);
        map.put("peopname",peopname);
        map.put("conferenceid",conferenceid+"");
        map.put("roomname",roomname);
        map.put("floorid",floorid+"");
        map.put("buildingid",buildingid+"");
        map.put("buildingname",bulidingname);
        map.put("floorname",floorname);
        map.put("serveid",serveid+"");
        map.put("remindid",remindid+"");
        map.put("repeatid",repeatid+"");
        map.put("remarks",remarks);
        map.put("send_type",send_type+"");
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
                        tv_select_meettime_detail.setText(data.getStringExtra("time").substring(0, 16)+ "~"+ data.getStringExtra("time").substring(31, 36));
                    }
                    break;
                case REQUSET_BOOKROOM_PEOPLE:
                    if (data != null){
                        peopleName = data.getStringExtra("usName");
                        peopleId = data.getStringExtra("usId");
                        listName = data.getStringExtra("usName").split(",");
                        listId = data.getStringExtra("usId").split(",");
                        setListViewHeightBasedOnChildren(gridview_people_meeting,listName,this);
                        gridview_people_meeting.setAdapter(new MeetingName_GridviewAdapter(BookMroomDetails.this,listName,listId));
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
     *@date2017/12/4
     *@author zhonghuibin
     *@description 根据公司id获取会议室服务的选项
     */
    private void getService(Context context,String url,int company_id, final ApiRequestFactory.HttpCallBackListener httpCallBackListener,
                                   boolean isShowDialog) {
        Map<String ,String> map = new HashMap<>();
        map.put("company_id",company_id+"");
        ApiRequestFactory.postJson(context,url,map,httpCallBackListener,isShowDialog);
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
                               int has_select_zhuopan,int has_select_guopan,int has_select_biaoyu,int int7,int int8){
        String MeetingService ="";
        List<Integer> list = new ArrayList<>();
        list.add(has_select_chaye);
        list.add(has_select_chabei);
        list.add(has_select_kaishui);
        list.add(has_select_zhuopan);
        list.add(has_select_guopan);
        list.add(has_select_biaoyu);
        list.add(int7);
        list.add(int8);
        for (int i=0;i<list.size();i++){
            if (list.get(i)!=0){
                MeetingService = MeetingService+","+list.get(i);
            }
        }
        return MeetingService.length() == 0?"":MeetingService.substring(1,MeetingService.length());
    }
}
