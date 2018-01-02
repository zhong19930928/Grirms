package com.yunhu.yhshxc.MeetingAgenda;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yunhu.android.view.TimeAxisView;
import com.yunhu.yhshxc.MeetingAgenda.adapter.MeetingJumpAdapter;
import com.yunhu.yhshxc.MeetingAgenda.adapter.MeetingJumpGridviewAdapter;
import com.yunhu.yhshxc.MeetingAgenda.bo.Meetingarticle;
import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.BeanMeetingroom;
import com.yunhu.yhshxc.bo.TimeBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhonghuibin
 * @data 2017/12/20 9:17
 * @description 预定会议室中转页面
 */

public class MeetingBoomJumpActivity extends Activity implements View.OnClickListener{
    private ListView lv_main;
    private MeetingJumpAdapter homeSecondAdapter;
    private List<BeanMeetingroom> list = new ArrayList<>();
    private List<Meetingarticle> articleList = new ArrayList<>();
    private List<TimeBean> list2;
    private BeanMeetingroom bean;
    private Meetingarticle article;
    private TimeAxisView tav_main;
    private GridView gv_meetingjump;
    private ImageView iv_top;
    private TextView tv_below1,tv_below2,tv_meetingroomname,tv_lastmeetingtime;
    private ImageView tv_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetingbookshow);
        initView();
        setData();
    }

    private void initView() {
        lv_main = findViewById(R.id.lv_main);
        tav_main = findViewById(R.id.tav_main);
        gv_meetingjump = findViewById(R.id.gv_meetingjump);
        iv_top = findViewById(R.id.iv_top);
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(this);
        tv_below1 = findViewById(R.id.tv_below1);
        tv_below2 = findViewById(R.id.tv_below2);
        tv_meetingroomname = findViewById(R.id.tv_meetingroomname);
        tv_lastmeetingtime = findViewById(R.id.tv_lastmeetingtime);
    }

    private void setData() {
        for (int i=0;i<3;i++){
            bean = new BeanMeetingroom();
            bean.setTv1("12-8");
            bean.setTv2("08:00~11:30");
            bean.setTv3("质量产品部");
            bean.setTv4("产品部");
            list.add(bean);
        }
        for (int i=0;i<2;i++){
            bean = new BeanMeetingroom();
            bean.setTv1("12-9");
            bean.setTv2("06:30~28:30");
            bean.setTv3("新项目启动");
            bean.setTv4("技术部");
            list.add(bean);
        }
        for (int i=0;i<3;i++){
            bean = new BeanMeetingroom();
            bean.setTv1("12-10");
            bean.setTv2("09:00~11:60");
            bean.setTv3("需求修改部");
            bean.setTv4("研发");
            list.add(bean);
        }
        for (int i=0;i<7;i++){
            article = new Meetingarticle();
            article.setName("电子白板");
            article.setImageurl("");
            articleList.add(article);
        }
        if (list != null && list.size() >0){
            homeSecondAdapter = new MeetingJumpAdapter(this,list);
            lv_main.setAdapter(homeSecondAdapter);
        }
        if (articleList != null && articleList.size() >0){
            MeetingJumpGridviewAdapter  adapter= new MeetingJumpGridviewAdapter(this,articleList);
            gv_meetingjump.setAdapter(adapter);
        }

        list2 = new ArrayList<>();
        list2.add(new TimeBean("12-18","0,5,6,15"));
//        list2.add(new TimeBean("12-19","0,1,2,3,4,5,6,8"));
//        list2.add(new TimeBean("12-20","15,16"));
//        list2.add(new TimeBean("12-21","7,8,9"));
//        list2.add(new TimeBean("12-22","2,3,4,8,9,10,22,23"));
//        list2.add(new TimeBean("12-23","3,4,5,17,18,19,20,21"));
//        list2.add(new TimeBean("12-24","3,4,5,6,7,8,9,10,18,19,20,21,22,23"));
        tav_main.setList(list2);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_cancel:
                finish();
                break;
            default:
                break;
        }
    }
}
