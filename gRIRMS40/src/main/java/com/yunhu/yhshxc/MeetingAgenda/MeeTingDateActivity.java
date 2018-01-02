package com.yunhu.yhshxc.MeetingAgenda;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yunhu.yhshxc.MeetingAgenda.view.TimeDialView;
import com.yunhu.yhshxc.MeetingAgenda.view.calendars.CaledarAdapter;
import com.yunhu.yhshxc.MeetingAgenda.view.calendars.CalendarBean;
import com.yunhu.yhshxc.MeetingAgenda.view.calendars.CalendarDateView;
import com.yunhu.yhshxc.MeetingAgenda.view.calendars.CalendarUtil;
import com.yunhu.yhshxc.MeetingAgenda.view.calendars.CalendarView;
import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.utility.DateUtil;

import java.util.Date;

import static com.yunhu.yhshxc.MeetingAgenda.view.calendars.NormalUtils.px;

/**
 * 日期时间选择,返回字段"time",在onActivityResult中获得  getStringExtra("time","");
 * 时间格式:yyyy-MM-dd HH:mm:ss~yyyy-MM-dd HH:mm:ss  开始时间和结束时间
 */
public class MeeTingDateActivity extends AppCompatActivity implements View.OnClickListener, TimeDialView.OnTimeChangeListener {

    private ImageView meeting_data_back;//返回键
    private TextView metting_date_title;//时间显示
    private CalendarDateView mCalendarDateView;//日历选择器
    private TimeDialView meeting_data_timepicker;//时钟选择器
    private TextView startTime, endTime;//时钟选择开始时间,结束时间
    private Button finishBtn;//完成按钮

    private  String initStartDateTime,initEndDateTime;//开始时间结束时间

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mee_ting_date);
        initView();
        initData();
    }


    private void initView() {
        meeting_data_back = (ImageView) findViewById(R.id.meeting_data_back);
        meeting_data_back.setOnClickListener(this);
        metting_date_title = (TextView) findViewById(R.id.metting_date_title);
        mCalendarDateView = (CalendarDateView) findViewById(R.id.meeting_data_calendarDateView);
        meeting_data_timepicker = (TimeDialView) findViewById(R.id.meeting_data_timepicker);
        startTime = (TextView) findViewById(R.id.meeting_time_start);
        endTime = (TextView) findViewById(R.id.meeting_time_end);

        finishBtn = (Button) findViewById(R.id.meeting_finish_btn);
        finishBtn.setOnClickListener(this);
//        startTime.setOnClickListener(this);
//        endTime.setOnClickListener(this);
    }

    private void initData() {
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

        mCalendarDateView.setOnItemClickListener(new CalendarView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, CalendarBean bean) {
                metting_date_title.setText(bean.year + "年" + getDisPlayNumber(bean.moth) + "月" + getDisPlayNumber(bean.day) + "日");
                initStartDateTime = bean.year + "-" + getDisPlayNumber(bean.moth) + "-" + getDisPlayNumber(bean.day) +" ";
                initEndDateTime = bean.year + "-" + getDisPlayNumber(bean.moth) + "-" + getDisPlayNumber(bean.day) + " ";
//                Toast.makeText(MeeTingDateActivity.this, "选择了: " + bean.year + "--" + getDisPlayNumber(bean.moth) + "----" + getDisPlayNumber(bean.day), Toast.LENGTH_SHORT).show();
                isDayChange(initStartDateTime);
            }
        });

        int[] data = CalendarUtil.getYMD(new Date());
        metting_date_title.setText(data[0] + "年" + data[1] + "月" + data[2] + "日");
        initStartDateTime = data[0] + "-" + getDisPlayNumber(data[1]) + "-" + getDisPlayNumber(data[2]) + " ";//初始默认时间为当天8点
        initEndDateTime = data[0] + "-" + getDisPlayNumber(data[1]) + "-" + getDisPlayNumber(data[2]) + " ";//结束默认时间为当天8点
        //24小时时间选择器,监听时间选择
        meeting_data_timepicker.setOnTimeChangeListener(this);
        //初始化当前时间
        meeting_data_timepicker.getTime();
    }


    private String getDisPlayNumber(int num) {
        return num < 10 ? "0" + num : "" + num;
    }

    /**
     * 判断日期是否改变
     */
 private  void isDayChange(String dateStr){
     String date = DateUtil.getDateByDate(new Date());
     //如果选择的日期与当前的日期一样,则启用区域校验模式
     if (date.equals(dateStr.trim())){
         meeting_data_timepicker.isToday(true);
     }else{
         meeting_data_timepicker.isToday(false);
     }
 }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.meeting_data_back://返回
                this.finish();
                break;
            case R.id.meeting_finish_btn://完成
                completeSelect();
                break;
            case R.id.meeting_time_start://时钟开始时间
                startTime.setBackgroundResource(R.drawable.conner_stroke_bg_1);
                endTime.setBackgroundResource(R.drawable.conner_stroke_bg_2);
                break;
            case R.id.meeting_time_end://时钟结束时间
                endTime.setBackgroundResource(R.drawable.conner_stroke_bg_1);
                startTime.setBackgroundResource(R.drawable.conner_stroke_bg_2);
                break;
        }
    }
    //点击完成返回时间
    private void completeSelect() {
      if (!meeting_data_timepicker.isUserTime()){
          Toast.makeText(this, "所选时间无效", Toast.LENGTH_SHORT).show();
          return;
      }
        Intent intent = new Intent();
        String resultTime = initStartDateTime+startTime.getText().toString()+":00"+"~"+initEndDateTime+endTime.getText().toString()+":00";
        intent.putExtra("time",resultTime);
        setResult(RESULT_OK,intent);
//        Toast.makeText(this, resultTime, Toast.LENGTH_SHORT).show();
        this.finish();
    }

    @Override
    public void onTimeChanged(String time) {
       //解析时间返回
        String[] strings = time.split("-");
        startTime.setText(strings[0]);
        endTime.setText(strings[1]);

    }
}
