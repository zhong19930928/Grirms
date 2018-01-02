package com.yunhu.yhshxc.MeetingAgenda;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yunhu.yhshxc.MeetingAgenda.adapter.MeetingName_GridviewAdapter;
import com.yunhu.yhshxc.MeetingAgenda.bo.AgendaDetsilabean;
import com.yunhu.yhshxc.MeetingAgenda.bo.MeetingAgenda;
import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.addressBook.db.AdressBookUserDB;
import com.yunhu.yhshxc.core.ApiRequestFactory;
import com.yunhu.yhshxc.core.ApiRequestMethods;
import com.yunhu.yhshxc.core.ApiUrl;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 *@date2017/9/1
 *@author zhonghuibin
 *@description
 */
public class AgendaDetailsActivity extends Activity implements View.OnClickListener{
    private TextView tv_topic;
    private TextView tv_time_newagenda;
    private TextView tv_address;
    private TextView tv_notic;
    private TextView tv_renotice;
    private ImageView iv_askpeople;
    private MeetingAgenda meetingAgenda;
    private String newAgendaTime,noitce_way;
    private TextView tv_noticeway_email,tv_noticeway_message,tv_noticeway_outnet;
    public static final int REQUSET_NOTICE = 1;//标记会议提醒时间返回页面
    public static final int REQUSET_meettime = 2;//标记会议时间返回页面
    public static final int REQUSET_NEWAGENDA_PEOPLE = 6;//标记参与日程人员返回页面
    public static final int REQUSET_BOOKROOM_NOTICE = 5;//标记参与日程人员返回页面
    private int condation_email=0,condation_message=0,condation_outnet=0;
    private boolean emailselected = false,messageselected = false,outnetselected = false;//用来标示邮件和短信通知发送方式是否被选取
    private GridView gridview_people_agenda;
    private TextView et_beizhu_meetdetails;
    private String peopleName,peopleId;
    private String[] listName,listId;
    private String org_cede;
    private SharedPreferencesUtil sh;
    private int noticeType=2,notice_recycle=0;
    private int meetingRoomId,typeid;
    private AgendaDetsilabean.DataBeanX.DataBean data;
    private AgendaDetsilabean agendabean;
    private TextView tv_ground;
    private RelativeLayout rl_newagenda_submit;
    private String starttime,endtime;
    private TextView tv_beizhu_meetdetails,tv_commit;
    private RelativeLayout rl_agenda_condition;
    private TextView tv_acccess,tv_prieve,tv_refuse;
    private int itemIsFinishFlag;
    private boolean ishow_cancle = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendadetails);
        sh = SharedPreferencesUtil.getInstance(AgendaDetailsActivity.this);
        if (getIntent()!=null){
            meetingAgenda = (MeetingAgenda) getIntent().getSerializableExtra("meetingagenda");
            meetingRoomId = meetingAgenda.getHuiYiId();
            itemIsFinishFlag = getIntent().getIntExtra("itemIsFinishFlag",0);
            typeid = meetingAgenda.getType();
        }
        //避免进入页面直接弹出软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initView();
    }

    private void initView() {
        findViewById(R.id.iv_back_newagenda).setOnClickListener(this);
        rl_newagenda_submit = (RelativeLayout) findViewById(R.id.rl_newagenda_submit);
        findViewById(R.id.tv_submit).setOnClickListener(this);
        findViewById(R.id.tv_cancle).setOnClickListener(this);
        tv_ground = (TextView) findViewById(R.id.tv_ground);
        tv_ground.setOnClickListener(this);
        gridview_people_agenda = (GridView) findViewById(R.id.gridview_people_agenda);
        et_beizhu_meetdetails = (TextView) findViewById(R.id.et_beizhu_meetdetails);
        tv_topic = (TextView) findViewById(R.id.tv_topic);
        tv_time_newagenda = (TextView) findViewById(R.id.tv_time_newagenda);
        tv_time_newagenda.setOnClickListener(this);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_notic = (TextView) findViewById(R.id.tv_notic);
        tv_commit = (TextView) findViewById(R.id.tv_commit);
        tv_renotice = (TextView) findViewById(R.id.tv_renotice);
        rl_agenda_condition = (RelativeLayout) findViewById(R.id.rl_agenda_condition);
        tv_beizhu_meetdetails = (TextView) findViewById(R.id.tv_beizhu_meetdetails);
        tv_renotice.setOnClickListener(this);
        iv_askpeople = (ImageView) findViewById(R.id.iv_askpeople);
        iv_askpeople.setOnClickListener(this);
        tv_noticeway_email = (TextView) findViewById(R.id.tv_noticeway_email);
        tv_noticeway_email.setOnClickListener(this);
        tv_noticeway_message = (TextView) findViewById(R.id.tv_noticeway_message);
        tv_noticeway_message.setOnClickListener(this);
        tv_noticeway_outnet = (TextView) findViewById(R.id.tv_noticeway_outnet);
        tv_noticeway_outnet.setOnClickListener(this);
        tv_acccess = (TextView) findViewById(R.id.tv_acccess);
        tv_acccess.setOnClickListener(this);
        tv_refuse = (TextView) findViewById(R.id.tv_refuse);
        tv_refuse.setOnClickListener(this);
        tv_prieve = (TextView) findViewById(R.id.tv_prieve);
        tv_prieve.setOnClickListener(this);
        tv_notic.setOnClickListener(this);
        if (meetingAgenda != null){
            setType(meetingAgenda.getHuiYiType());
        }
        settext();
    }

    private void setType(int type) {
        if (meetingAgenda != null && itemIsFinishFlag == 2){
            rl_agenda_condition.setVisibility(View.GONE);
            ishow_cancle = false;
        }else{
            switch (type){
                case 1:
                    rl_agenda_condition.setVisibility(View.GONE);
                    break;
                case 2:
                    rl_agenda_condition.setVisibility(View.VISIBLE);
                    tv_acccess.setTextColor(getResources().getColor(R.color.bbs_menu_blue));
                    break;
                case 3:
                    rl_agenda_condition.setVisibility(View.VISIBLE);
                    tv_prieve.setTextColor(getResources().getColor(R.color.bbs_menu_blue));
                    break;
                case 4:
                    rl_agenda_condition.setVisibility(View.VISIBLE);
                    tv_acccess.setTextColor(getResources().getColor(R.color.gray_sub));
                    tv_refuse.setTextColor(getResources().getColor(R.color.gray_sub));
                    tv_prieve.setTextColor(getResources().getColor(R.color.gray_sub));
                    break;
                case 5:
                    rl_agenda_condition.setVisibility(View.GONE);
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
        ApiRequestMethods.confirmmeeting(this, sh.getCompanyId(), sh.getUserId(), sh.getUserName(), meetingRoomId, huiyitype,typeid, ApiUrl.CONFIRMEETTING, new ApiRequestFactory.HttpCallBackListener() {
            @Override
            public void onSuccess(String response, String url, int id) {
                JSONObject object = null;
                try {
                    object = new JSONObject(response).getJSONObject("data");
                    if (object == null){
                        return;
                    }
                    if (object.has("flag") && object.getInt("flag") == 0) {
                        Toast.makeText(AgendaDetailsActivity.this,"操作成功",Toast.LENGTH_LONG).show();
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

    private void settext() {
        getAgendaDetails(AgendaDetailsActivity.this, "m=Androidstation&a=getIDscheduledetails", meetingRoomId, new ApiRequestFactory.HttpCallBackListener() {
            @Override
            public void onSuccess(String response, String url, int id) {
                String aa = response;
                try {
                    agendabean = new Gson().fromJson(response,AgendaDetsilabean.class);
                    data = agendabean.getData().getData();
                    setbean(data);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void failure(Call call, Exception e, int id) {

            }
        },true);
    }

    private void setbean(AgendaDetsilabean.DataBeanX.DataBean data){
        if (Integer.parseInt(data.getUid())==sh.getUserId() && ishow_cancle){
            tv_ground.setVisibility(View.VISIBLE);
            rl_newagenda_submit.setVisibility(View.VISIBLE);
        }else {
            tv_ground.setVisibility(View.VISIBLE);
            rl_newagenda_submit.setVisibility(View.GONE);
        }
        tv_topic.setText(data.getName());
        tv_commit.setText(data.getUname());
        tv_time_newagenda.setText(data.getDataday().substring(0,16)+"-"+data.getDataday().substring(20,36));
        tv_address.setText(data.getAddres());
        starttime = data.getStarttime();
        endtime = data.getEndtime();
        setRemindid(Integer.parseInt(data.getRemindid()));
        setRepeatid(Integer.parseInt(data.getRepeatid()));
        peopleName = data.getPeopname();
        peopleId = data.getPeopuid();
        newAgendaTime = data.getDataday();
        if (peopleName!=null&&!peopleName.equals("")){
            String []listName2 = peopleName.split(",");
            String []listId2 = peopleId.split(",");
            setListViewHeightBasedOnChildren(gridview_people_agenda,listName2,this);
            gridview_people_agenda.setAdapter(new MeetingName_GridviewAdapter(AgendaDetailsActivity.this,listName2,listId2));
        }
        sendType(data.getSend_type());
        tv_beizhu_meetdetails.setText("备注： "+data.getRemarks());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_acccess:
                onChiese(tv_acccess,2);
                break;
            case R.id.tv_prieve:
                onChiese(tv_prieve,3);
                break;
            case R.id.tv_refuse:
                onChiese(tv_refuse,4);
                break;
            case R.id.tv_notic:
//                Intent intent = new Intent(this,NoticeMeetingTime.class);
//                startActivityForResult(intent,REQUSET_NOTICE);
                break;
//            case R.id.tv_noticeway_email:
//                if (condation_email % 2 == 1){
//                    tv_noticeway_email.setBackgroundResource(R.drawable.circle_shape_bg_bbs_shadow);
//                    tv_noticeway_email.setTextColor(this.getResources().getColor(R.color.black));
//                    emailselected = false;
//                }else{
//                    tv_noticeway_email.setBackgroundResource(R.drawable.circle_shape_bg_bbs_green);
//                    tv_noticeway_email.setTextColor(this.getResources().getColor(R.color.white));
//                    emailselected = true;
//                }
//                condation_email++;
//                break;
//            case R.id.tv_noticeway_message:
//                if (condation_message % 2 == 1){
//                    tv_noticeway_message.setBackgroundResource(R.drawable.circle_shape_bg_bbs_shadow);
//                    tv_noticeway_message.setTextColor(this.getResources().getColor(R.color.black));
//                    messageselected = false;
//                }else{
//                    tv_noticeway_message.setBackgroundResource(R.drawable.circle_shape_bg_bbs_green);
//                    tv_noticeway_message.setTextColor(this.getResources().getColor(R.color.white));
//                    messageselected = true;
//                }
//                condation_message++;
//                break;
//            case R.id.tv_noticeway_outnet:
//                if (condation_outnet % 2 == 1){
//                    tv_noticeway_outnet.setBackgroundResource(R.drawable.circle_shape_bg_bbs_shadow);
//                    tv_noticeway_outnet.setTextColor(this.getResources().getColor(R.color.black));
//                    outnetselected = false;
//                }else{
//                    tv_noticeway_outnet.setBackgroundResource(R.drawable.circle_shape_bg_bbs_green);
//                    tv_noticeway_outnet.setTextColor(this.getResources().getColor(R.color.white));
//                    outnetselected = true;
//                }
//                condation_outnet++;
//                break;
            case R.id.tv_renotice:
//                Intent intent_recycle = new Intent(this,RecycleTimesActivity.class);
//                startActivityForResult(intent_recycle,REQUSET_BOOKROOM_NOTICE);
                break;
            case R.id.iv_back_newagenda:
                finish();
                break;
            case R.id.tv_submit:
                org_cede = new AdressBookUserDB(AgendaDetailsActivity.this).findUserById(sh.getUserId()).getOc();
                subto_buildnewAgenda();
                break;
            case R.id.tv_time_newagenda:
//                Intent intent_settime = new Intent(AgendaDetailsActivity.this, MeeTingDateActivity.class);
//                startActivityForResult(intent_settime,REQUSET_meettime);
                break;
            case R.id.iv_askpeople://跳转至邀请人
//                Intent intent_user = new Intent(AgendaDetailsActivity.this,MeetingUserActivity.class);
//                startActivityForResult(intent_user,REQUSET_NEWAGENDA_PEOPLE);
                break;
            case R.id.tv_ground:
                break;
            case R.id.tv_cancle:
                cancleMeeting_(sh.getCompanyId(),meetingRoomId,1);
                break;
            default:
                break;
        }
    }

    /**
     *@date2017/10/24
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
     *@date 2017/8/31
     *@author 钟辉斌
     *@description 新建日程提交
     */
    private void subto_buildnewAgenda() {
        sub_NewAgenda2(AgendaDetailsActivity.this, "m=Androidstation&a=addschedule",Integer.parseInt(data.getId()),sh.getCompanyId(), sh.getUserId(), org_cede,
                sh.getOrgId()+"", sh.getRoleId()+"", sh.getUserId(),sh.getUserRoleName(), tv_topic.getText().toString(),
                peopleId,peopleName,noticeType+"",notice_recycle+"",et_beizhu_meetdetails.getText().toString(),starttime,endtime,
                getNoticeWay(emailselected,messageselected,outnetselected), newAgendaTime, tv_address.getText().toString(),
                new ApiRequestFactory.HttpCallBackListener() {
                    @Override
                    public void onSuccess(String response, String url, int id) {
                        Toast.makeText(AgendaDetailsActivity.this,"提交成功",Toast.LENGTH_LONG).show();
                        finish();
                    }

                    @Override
                    public void failure(Call call, Exception e, int id) {
                        Toast.makeText(AgendaDetailsActivity.this,"提交失败",Toast.LENGTH_LONG).show();
                    }
                },false);
    }

    private void setRepeatid(int repeatid) {//重复
        switch (repeatid){
            case 0:
                tv_renotice.setText("永不");
                notice_recycle = 0;
                break;
            case 1:
                tv_renotice.setText("每天");
                notice_recycle = 1;
                break;
            case 2:
                tv_renotice.setText("每周");
                notice_recycle = 2;
                break;
            case 3:
                tv_renotice.setText("每月");
                notice_recycle = 3;
                break;
            default:
                break;
        }
    }

    private void sendType(String sendType) {//发送方式tv_noticeway_email   tv_noticeway_message   tv_noticeway_outnet
        if(sendType.length()>1){
            String sendlist[] = sendType.split(",");
            for (int i=0;i<sendlist.length;i++){
                int sendtype = Integer.parseInt(sendlist[i]);
                if (i == 0){
                    setnotice(tv_noticeway_email,sendtype);
                }else if(i==1){
                    setnotice(tv_noticeway_message,sendtype);
                }else if(i==2){
                    setnotice(tv_noticeway_outnet,sendtype);
                }
            }
        }else{
            int sendtype = Integer.parseInt(sendType);
            setnotice(tv_noticeway_outnet,sendtype);
        }
    }


    /**
     *@date2017/11/23
     *@author zhonghuibin
     *@description 发送方式
     */
    private void setnotice(TextView textView,int type){
        textView.setVisibility(View.VISIBLE);
        switch (type){
            case 1:
                textView.setText("邮件");
                emailselected = true;
                break;
            case 2:
                textView.setText("短信");
                messageselected = true;
                break;
            case 3:
                textView.setText("站内");
                outnetselected = true;
                break;
            default:
                break;
        }
    }


    private void setRemindid(int remindid) {//提醒方式
        switch (remindid){
            case 0:
                tv_notic.setText("无");
                noticeType = 0;
                break;
            case 1:
                tv_notic.setText("会议开始前");
                noticeType = 1;
                break;
            case 2:
                tv_notic.setText("提前5分钟");
                noticeType = 2;
                break;
            case 3:
                tv_notic.setText("提前15分钟");
                noticeType = 3;
                break;
            case 4:
                tv_notic.setText("提前30分钟");
                noticeType = 4;
                break;
            case 5:
                tv_notic.setText("提前1个小时");
                noticeType = 5;
                break;
            case 6:
                tv_notic.setText("提前1天");
                noticeType = 6;
                break;
            default:
                break;
        }
    }

    /**
     *@date2017/10/31
     *@author zhonghuibin
     *@description 取消会议
     */
    private void cancleMeeting_(int company_id,int meetingId,int type){
        cancleMeeting(AgendaDetailsActivity.this,"m=Androidstation&a=deleteinfo",company_id,meetingId,type,
                new ApiRequestFactory.HttpCallBackListener() {
                    @Override
                    public void onSuccess(String response, String url, int id) {
                        Toast.makeText(AgendaDetailsActivity.this,"取消成功",Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void failure(Call call, Exception e, int id) {
                        Toast.makeText(AgendaDetailsActivity.this,"取消失败",Toast.LENGTH_LONG).show();
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
     *@date2017/9/5
     *@author zhonghuibin
     *@description 会议前提醒时间返回的值和会议时间页面返回的值
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUSET_NOTICE:
                    tv_notic.setText(data.getStringExtra("select_type"));
                    noticeType = data.getIntExtra("select_type_int", 0);
                    break;
                case REQUSET_meettime:
                    newAgendaTime = data.getStringExtra("time");
                    tv_time_newagenda.setText(data.getStringExtra("time").substring(0,16)+"~"+data.getStringExtra("time").substring(31,36));
                    starttime = data.getStringExtra("time").substring(0,19);
                    endtime = data.getStringExtra("time").substring(20,39);
                    break;
                case REQUSET_NEWAGENDA_PEOPLE:
                    if (data != null){
                        peopleName = data.getStringExtra("usName");
                        peopleId = data.getStringExtra("usId");
                        listName = data.getStringExtra("usName").split(",");
                        listId = data.getStringExtra("usId").split(",");
                        setListViewHeightBasedOnChildren(gridview_people_agenda,listName,this);
                        gridview_people_agenda.setAdapter(new MeetingName_GridviewAdapter(AgendaDetailsActivity.this,listName,listId));
                    }
                    break;
                case REQUSET_BOOKROOM_NOTICE:
                    tv_renotice.setText(data.getStringExtra("select_type"));
                    notice_recycle = data.getIntExtra("select_type_int", 0);
                    break;
            }
        }
    }

    /**
     *@date2017/9/7
     *@author zhonghuibin
     *@description 新建日程提交参数：公司id 权限用户id 机构code串 机构id 角色id 预定人id 日程内容 日程日期 地址
     */
    public void sub_NewAgenda2(Context context, String url, int id,int company_id, int data_auth_user, String data_org, String data_org_new,
                               String data_rele, int uid,String uname,String name,String peopuid,String peopname,String remindid,
                               String repeatid ,String remarks,String send_type,String dataday, String address, String starttime,String endtime,
                               final ApiRequestFactory.HttpCallBackListener httpCallBackListener,boolean isShowDialog){
        Map<String ,String> map = new HashMap<>();
        map.put("company_id",company_id+"");
        map.put("data_auth_user",data_auth_user+"");
        map.put("data_org",data_org);
        map.put("data_org_new",data_org_new);
        map.put("data_rele",data_rele);
        map.put("uid",uid+"");
        map.put("uname",uname);
        map.put("id",id+"");
        map.put("name",name);
        map.put("peopuid",peopuid);
        map.put("peopname",peopname);
        map.put("remindid",remindid);
        map.put("repeatid",repeatid);
        map.put("remarks",remarks);
        map.put("send_type",send_type);
        map.put("dataday",dataday);
        map.put("addres",address);
        ApiRequestFactory.postJson(context,url,map,httpCallBackListener,isShowDialog);
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
     *@date2017/10/31
     *@author zhonghuibin
     *@description 获取会议室详情
     */
    private void getAgendaDetails(Context context,String url,int meetingId,final ApiRequestFactory.HttpCallBackListener httpCallBackListener,
                                   boolean isShowDialog){
        Map<String,String> map = new HashMap<>();
        map.put("id",meetingRoomId+"");
        ApiRequestFactory.postJson(context,url,map,httpCallBackListener,isShowDialog);
    }
}
