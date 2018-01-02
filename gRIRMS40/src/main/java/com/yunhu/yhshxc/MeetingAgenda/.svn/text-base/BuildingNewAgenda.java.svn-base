package com.yunhu.yhshxc.MeetingAgenda;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yunhu.yhshxc.MeetingAgenda.adapter.MeetingName_GridviewAdapter;
import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.addressBook.db.AdressBookUserDB;
import com.yunhu.yhshxc.application.SoftApplication;
import com.yunhu.yhshxc.core.ApiRequestFactory;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 *@date2017/9/1
 *@author zhonghuibin
 *@description 日程预订页面
 */
public class BuildingNewAgenda extends Activity implements View.OnClickListener{
    private EditText tv_topic;
    private TextView tv_time_newagenda;
    private EditText tv_address;
    private TextView tv_notic;
    private TextView tv_renotice;
    private ImageView iv_askpeople;
    private String newAgendaTime,noitce_way;
    private TextView tv_noticeway_email,tv_noticeway_message,tv_noticeway_outnet;
    public static final int REQUSET_NOTICE = 1;//标记会议提醒时间返回页面
    public static final int REQUSET_meettime = 2;//标记会议时间返回页面
    public static final int REQUSET_NEWAGENDA_PEOPLE = 6;//标记参与日程人员返回页面
    public static final int REQUSET_BOOKROOM_NOTICE = 5;//标记参与日程人员返回页面
    private int condation_email=0,condation_message=0,condation_outnet=0;
    private boolean emailselected = false,messageselected = false,outnetselected = false;//用来标示邮件和短信通知发送方式是否被选取
    private GridView gridview_people_agenda;
    private EditText et_beizhu_meetdetails;
    private String peopleName,peopleId;
    private String[] listName,listId;
    private String org_cede;
    private SharedPreferencesUtil sh;
    private int noticeType=2,notice_recycle=0;
    private String starttime,endtime;
    private Date curDate,meetingDate;
    private SimpleDateFormat formatter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newagenda);
        //避免进入页面直接弹出软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initView();
    }

    private void initView() {
        findViewById(R.id.iv_back_newagenda).setOnClickListener(this);
        findViewById(R.id.rl_newagenda_submit).setOnClickListener(this);
        gridview_people_agenda = (GridView) findViewById(R.id.gridview_people_agenda);
        et_beizhu_meetdetails = (EditText) findViewById(R.id.et_beizhu_meetdetails);
        tv_topic = (EditText) findViewById(R.id.tv_topic);
        tv_time_newagenda = (TextView) findViewById(R.id.tv_time_newagenda);
        tv_time_newagenda.setOnClickListener(this);
        tv_address = (EditText) findViewById(R.id.tv_address);
        tv_notic = (TextView) findViewById(R.id.tv_notic);
        tv_renotice = (TextView) findViewById(R.id.tv_renotice);
        tv_renotice.setOnClickListener(this);
        iv_askpeople = (ImageView) findViewById(R.id.iv_askpeople);
        iv_askpeople.setOnClickListener(this);
        tv_noticeway_email = (TextView) findViewById(R.id.tv_noticeway_email);
        tv_noticeway_email.setOnClickListener(this);
        tv_noticeway_message = (TextView) findViewById(R.id.tv_noticeway_message);
        tv_noticeway_message.setOnClickListener(this);
        tv_noticeway_outnet = (TextView) findViewById(R.id.tv_noticeway_outnet);
        tv_noticeway_outnet.setOnClickListener(this);
        tv_notic.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_notic:
                Intent intent = new Intent(this,NoticeMeetingTime.class);
                startActivityForResult(intent,REQUSET_NOTICE);
                break;
            case R.id.tv_noticeway_email:
                if (condation_email % 2 == 1){
                    tv_noticeway_email.setBackgroundResource(R.drawable.circle_shape_bg_bbs_shadow);
                    tv_noticeway_email.setTextColor(this.getResources().getColor(R.color.black));
                    emailselected = false;
                }else{
                    tv_noticeway_email.setBackgroundResource(R.drawable.circle_shape_bg_bbs_green);
                    tv_noticeway_email.setTextColor(this.getResources().getColor(R.color.white));
                    emailselected = true;
                }
                condation_email++;
                break;
            case R.id.tv_noticeway_message:
                if (condation_message % 2 == 1){
                    tv_noticeway_message.setBackgroundResource(R.drawable.circle_shape_bg_bbs_shadow);
                    tv_noticeway_message.setTextColor(this.getResources().getColor(R.color.black));
                    messageselected = false;
                }else{
                    tv_noticeway_message.setBackgroundResource(R.drawable.circle_shape_bg_bbs_green);
                    tv_noticeway_message.setTextColor(this.getResources().getColor(R.color.white));
                    messageselected = true;
                }
                condation_message++;
                break;
            case R.id.tv_noticeway_outnet:
                if (condation_outnet % 2 == 1){
                    tv_noticeway_outnet.setBackgroundResource(R.drawable.circle_shape_bg_bbs_shadow);
                    tv_noticeway_outnet.setTextColor(this.getResources().getColor(R.color.black));
                    outnetselected = false;
                }else{
                    tv_noticeway_outnet.setBackgroundResource(R.drawable.circle_shape_bg_bbs_green);
                    tv_noticeway_outnet.setTextColor(this.getResources().getColor(R.color.white));
                    outnetselected = true;
                }
                condation_outnet++;
                break;
            case R.id.tv_renotice:
                Intent intent_recycle = new Intent(this,RecycleTimesActivity.class);
                startActivityForResult(intent_recycle,REQUSET_BOOKROOM_NOTICE);
                break;
            case R.id.iv_back_newagenda:
                finish();
                break;
            case R.id.rl_newagenda_submit:
                sh = SharedPreferencesUtil.getInstance(BuildingNewAgenda.this);
                org_cede = new AdressBookUserDB(BuildingNewAgenda.this).findUserById(sh.getUserId()).getOc();
                subto_buildnewAgenda();
                break;
            case R.id.tv_time_newagenda:
                Intent intent_settime = new Intent(BuildingNewAgenda.this, MeeTingDateActivity.class);
                startActivityForResult(intent_settime,REQUSET_meettime);
                break;
            case R.id.iv_askpeople://跳转至邀请人
                Intent intent_user = new Intent(BuildingNewAgenda.this,MeetingUserActivity.class);
                startActivityForResult(intent_user,REQUSET_NEWAGENDA_PEOPLE);
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
        if(tv_topic.getText().toString().equals("")){
            Toast.makeText(BuildingNewAgenda.this,"请填写日程主题",Toast.LENGTH_SHORT).show();
        }else if(starttime==null ||starttime.equals("")  ){
            Toast.makeText(BuildingNewAgenda.this,"请选择日程时间",Toast.LENGTH_SHORT).show();
        }else if(curDate.after(meetingDate)){
            Toast.makeText(BuildingNewAgenda.this,"日程时间已过期",Toast.LENGTH_SHORT).show();
        }else if(tv_address.getText().toString().equals("")){
            Toast.makeText(BuildingNewAgenda.this,"",Toast.LENGTH_SHORT).show();
        }else if(peopleName==null||peopleName.equals("")){
            Toast.makeText(BuildingNewAgenda.this,"请选择参与人",Toast.LENGTH_SHORT).show();
        }else{
            sub_NewAgenda2(BuildingNewAgenda.this, "m=Androidstation&a=addschedule", sh.getCompanyId(), sh.getUserId(), org_cede,
                    sh.getOrgId()+"", sh.getRoleId()+"", sh.getUserId(),sh.getUserName(), tv_topic.getText().toString(),
                    peopleId,peopleName,noticeType+"",notice_recycle+"",et_beizhu_meetdetails.getText().toString(),
                    getNoticeWay(emailselected,messageselected,outnetselected), newAgendaTime, tv_address.getText().toString(),starttime,endtime,
                    new ApiRequestFactory.HttpCallBackListener() {
                        @Override
                        public void onSuccess(String response, String url, int id) {
                            Toast.makeText(BuildingNewAgenda.this,"提交成功",Toast.LENGTH_LONG).show();
                            if(SoftApplication.getInstance().getMeetHandler()!=null){
                                SoftApplication.getInstance().getMeetHandler().sendEmptyMessage(MeetingagendaListActivity.UPDATENUM);
                            }
                            finish();
                        }

                        @Override
                        public void failure(Call call, Exception e, int id) {
                            Toast.makeText(BuildingNewAgenda.this,"提交失败",Toast.LENGTH_LONG).show();
                        }
                    },true);
        }
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
                    tv_time_newagenda.setText(data.getStringExtra("time").substring(0,16)+"-"+data.getStringExtra("time").substring(31,36));
                    starttime = data.getStringExtra("time").substring(0,19);
                    endtime = data.getStringExtra("time").substring(20,39);
                    formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    curDate=new Date(System.currentTimeMillis());//获取当前时间
                    try {
                        meetingDate = formatter.parse(endtime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case REQUSET_NEWAGENDA_PEOPLE:
                    if (data != null){
                        peopleName = data.getStringExtra("usName");
                        peopleId = data.getStringExtra("usId");
                        listName = data.getStringExtra("usName").split(",");
                        listId = data.getStringExtra("usId").split(",");
                        setListViewHeightBasedOnChildren(gridview_people_agenda,listName,this);
                        gridview_people_agenda.setAdapter(new MeetingName_GridviewAdapter(BuildingNewAgenda.this,listName,listId));
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
    public void sub_NewAgenda2(Context context, String url, int company_id, int data_auth_user, String data_org, String data_org_new,
                              String data_rele, int uid,String uname,String name,String peopuid,String peopname,String remindid, String repeatid
                              ,String remarks,String send_type,String dataday, String address, String starttime,String endtime,
                              final ApiRequestFactory.HttpCallBackListener httpCallBackListener,boolean isShowDialog){
        Map<String ,String> map = new HashMap<>();
        map.put("company_id",company_id+"");
        map.put("data_auth_user",data_auth_user+"");
        map.put("data_org",data_org);
        map.put("data_org_new",data_org_new);
        map.put("data_rele",data_rele);
        map.put("uid",uid+"");
        map.put("uname",uname);
        map.put("name",name);
        map.put("peopuid",peopuid);
        map.put("peopname",peopname);
        map.put("remindid",remindid);
        map.put("repeatid",repeatid);
        map.put("remarks",remarks);
        map.put("send_type",send_type);
        map.put("dataday",dataday);
        map.put("addres",address);
        map.put("starttime",starttime);
        map.put("endtime",endtime);
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

}
