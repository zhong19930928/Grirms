package com.yunhu.yhshxc.visitors;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;

public class VisitorDetailActivity extends AppCompatActivity {
    private TextView tv_visitor_date,tv_visitor_time,tv_visitor_dw,tv_visitor_sy,tv_visitor_person;
    private ImageView tv_cancel;
    private LinearLayout ll_content_all;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_detail);
        initView();
        initData();
    }

    private void initView() {
        tv_visitor_date = (TextView) findViewById(R.id.tv_visitor_date);
        tv_visitor_time = (TextView) findViewById(R.id.tv_visitor_time);
        tv_visitor_dw = (TextView) findViewById(R.id.tv_visitor_dw);
        tv_visitor_sy = (TextView) findViewById(R.id.tv_visitor_sy);
        tv_visitor_person = (TextView) findViewById(R.id.tv_visitor_person);
        tv_cancel= (ImageView) findViewById(R.id.tv_cancel);
        ll_content_all = (LinearLayout) findViewById(R.id.ll_content_all);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void initData() {
        Intent intent = getIntent();
        String date = intent.getStringExtra("date");
        String time = intent.getStringExtra("time");
        String dw = intent.getStringExtra("dw");
        String sy = intent.getStringExtra("sy");
        String person = intent.getStringExtra("person");
        tv_visitor_date.setText(date);
        tv_visitor_time.setText(time);
        tv_visitor_dw.setText(dw);
        tv_visitor_sy.setText(sy);
//        tv_visitor_person.setText(person);
        String name = "";
        String phone = "";
        if(!TextUtils.isEmpty(person)){
            String[] str = person.split(";");
            for (int i = 0; i<str.length;i++){
                String[] ns = str[i].split(":");
                if(ns.length>1){
                    name = ns[0];
                    phone = ns[1];
                    VisitorView view = new VisitorView(this);
                    view.initData(name,phone);
                    ll_content_all.addView(view.getView());
                }
            }

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
