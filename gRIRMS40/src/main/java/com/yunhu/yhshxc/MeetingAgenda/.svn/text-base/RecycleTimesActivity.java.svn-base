package com.yunhu.yhshxc.MeetingAgenda;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.yunhu.yhshxc.R;

/**
 *@date2017/9/1
 *@author zhonghuibin
 *@description 提醒时间选择页面
 */
public class RecycleTimesActivity extends Activity implements View.OnClickListener{
    private ImageView iv_back_noticemeeting;
    private String select_notice;
    private int select_type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycletimes);
        findViewById(R.id.noticetime1).setOnClickListener(this);
        findViewById(R.id.noticetime2).setOnClickListener(this);
        findViewById(R.id.noticetime3).setOnClickListener(this);
        findViewById(R.id.noticetime4).setOnClickListener(this);
        iv_back_noticemeeting = (ImageView) findViewById(R.id.iv_back_noticemeeting);
        iv_back_noticemeeting.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.noticetime1:
                select_notice = "永不重复";
                select_type = 0;
                break;
            case R.id.noticetime2:
                select_notice = "每天";
                select_type = 1;
                break;
            case R.id.noticetime3:
                select_notice = "每周";
                select_type = 2;
                break;
            case R.id.noticetime4:
                select_notice = "每月";
                select_type = 3;
                break;
            case R.id.iv_back_noticemeeting:
                finish();
            default:
                break;
        }
        Intent intent = new Intent();
        intent.putExtra("select_type",select_notice);
        intent.putExtra("select_type_int",select_type);
        setResult(RESULT_OK,intent);
        finish();
    }
}
