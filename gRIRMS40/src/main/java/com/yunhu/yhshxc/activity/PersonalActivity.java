package com.yunhu.yhshxc.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.android.view.RoundedImage;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;


/**
 * Created by suhu on 2017/5/4.
 */

public class PersonalActivity extends AbsBaseActivity implements View.OnClickListener{
    private static final String HEAD_PATH = Constants.SDCARD_PATH+"head.jpg";
    private RoundedImage imageView;
    private RelativeLayout back;
    private TextView name,position,lead,branch,setting;

    private DisplayImageOptions options;
    protected ImageLoader imageLoader = ImageLoader.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        initView();
        setListener();
        setData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.person_back:
                finish();
                break;
            case R.id.person_setting:
                startActivity(new Intent(this,PersonalSettingActivity.class));
                break;
            case R.id.imageView:
                //startActivity(new Intent(this,PersonalInformationActivity.class));
                startActivity(new Intent(this,ImageShowActivity.class));
                break;
        }
    }


    private void initView() {
        back = (RelativeLayout) findViewById(R.id.person_back);
        name = (TextView) findViewById(R.id.person_name);
        position = (TextView) findViewById(R.id.person_position);
        lead = (TextView) findViewById(R.id.person_lead_name);
        branch = (TextView) findViewById(R.id.person_branch);
        setting = (TextView) findViewById(R.id.person_setting);
        imageView = (RoundedImage) findViewById(R.id.imageView);

        options = new DisplayImageOptions
                .Builder()
                .showStubImage(R.drawable.wechat_moren_header)
                .showImageForEmptyUri(R.drawable.wechat_moren_header)
                .showImageOnFail(R.drawable.wechat_moren_header)
                .cacheInMemory(true)
                .build();
    }

    private void setListener() {
        back.setOnClickListener(this);
        setting.setOnClickListener(this);
        imageView.setOnClickListener(this);
    }



    private void setData() {
        // 用户名
        name.setText(SharedPreferencesUtil.getInstance(this).getUserName());
        // 职位
        position.setText(SharedPreferencesUtil.getInstance(this).getUserRoleName());
        // 直接领导
        lead.setText("");
        // 所属部门
        branch.setText(SharedPreferencesUtil.getInstance(this).getOrgName());

    }

    /**
     *@method 显示本地图片
     *@param imageView
     *
     */
    private void showImage(ImageView imageView, String file_path){
        Bitmap bm = BitmapFactory.decodeFile(file_path);
        if (bm!=null){
            imageView.setImageBitmap(bm);
        }else {
            imageLoader.displayImage(SharedPreferencesUtil.getInstance(this).getHeadImage(), imageView, options, null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showImage(imageView,HEAD_PATH);
    }


}
