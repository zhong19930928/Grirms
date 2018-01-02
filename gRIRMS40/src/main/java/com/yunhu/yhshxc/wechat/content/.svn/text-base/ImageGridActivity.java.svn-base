package com.yunhu.yhshxc.wechat.content;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class ImageGridActivity extends FragmentActivity {

	private static final String TAG = "ImageGridActivity";
	private String ifTakePhoto = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        if (BuildConfig.DEBUG) {
//            Utils.enableStrictMode();
//        }
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
       
        if(intent.getExtras() != null && !"".equals(intent.getExtras())){
        	 ifTakePhoto = intent.getExtras().getString("isSelect");
        }
        if(ifTakePhoto.equals("1")){
			Intent intent_video=new Intent();
			intent_video.setClass(this, RecorderVideoActivity.class);
			startActivityForResult(intent_video, 100);
        }else{
        	if (getSupportFragmentManager().findFragmentByTag(TAG) == null) {
                final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(android.R.id.content, new ImageGridFragment(), TAG);
                ft.commit();
            }
        }
        

        
    }
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==Activity.RESULT_OK){
		if(requestCode==100)
		{
			Uri uri=data.getParcelableExtra("uri");
			String[] projects = new String[] { MediaStore.Video.Media.DATA,
					MediaStore.Video.Media.DURATION };
			Cursor cursor = getApplicationContext().getContentResolver().query(
					uri, projects, null,
					null, null);
			int duration=0;
			String filePath=null;
			
			if (cursor.moveToFirst()) {
				// 路径：MediaStore.Audio.Media.DATA
				filePath = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
				// 总播放时长：MediaStore.Audio.Media.DURATION
				duration = cursor
						.getInt(cursor
								.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
				System.out.println("duration:"+duration);
			}
			if(cursor!=null)
            {
            	cursor.close();
            	cursor=null;
            }
			 
			this.setResult(Activity.RESULT_OK, this.getIntent().putExtra("path", filePath).putExtra("dur", duration));
			this.finish();
			
		}
		}else{
			if(ifTakePhoto.equals("1")){
				finish();
			}
			
		}
		
		
		
	}
	
	
}
