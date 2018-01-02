package com.yunhu.yhshxc.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.yunhu.yhshxc.activity.HomeMenuFragment;
import com.yunhu.yhshxc.database.SlidePictureDB;
import com.yunhu.yhshxc.style.SlidePicture;
import com.yunhu.yhshxc.utility.HttpHelper;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil2;

import java.util.List;


public class DownSlidePicService extends IntentService {


    public DownSlidePicService() {
        super("DownSlidePicService");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            boolean download = intent.getBooleanExtra("download",false);
            if (download){//执行下载工作
                try{
                    //读取数据库,查询出要下载的图片集合
                    List<SlidePicture> imgList = new SlidePictureDB(this).queryAll();
                    HttpHelper httpHelper = new HttpHelper(this);
                    for (int i = 0; i < imgList.size(); i++) {
                        SlidePicture sp = imgList.get(i);
                        int ok = httpHelper.downloadStyleFile(sp.getUrl(),sp.getSdPath(),sp.getPictureName());
                    }
                    SharedPreferencesUtil2.getInstance(this).setIsSlideImageDown(true);
                    Intent intentBro = new Intent();
                    intentBro.setAction(HomeMenuFragment.SLIDEIMAGE_ACTION);
                    DownSlidePicService.this.sendBroadcast(intentBro);
                }catch (Exception e){
                    e.printStackTrace();
                }


            }


         }
    }


}
