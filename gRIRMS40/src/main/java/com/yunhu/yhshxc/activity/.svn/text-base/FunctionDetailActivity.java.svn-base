package com.yunhu.yhshxc.activity;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.utility.PublicUtils;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class FunctionDetailActivity extends AbsBaseActivity{
  private ImageView imageBack;
  private LinearLayout photoContainer;
  private WebView webView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_function_detail);
	    imageBack = (ImageView) findViewById(R.id.function_detail_back);
	    photoContainer = (LinearLayout) findViewById(R.id.function_detail_container);
	    initView();
	}
	private void initView() {
//		PhotoView photoView1 = new PhotoView(this);
//		photoView1.setImageResource(R.drawable.function_detail);
//		photoView1.setLayoutParams( new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
//		photoView1.setScaleType(ScaleType.FIT_XY);
//		PhotoView photoView2 = new PhotoView(this);
//		photoView2.setImageResource(R.drawable.function_detail2);
//		photoView2.setLayoutParams( new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
//		PhotoView photoView3 = new PhotoView(this);
//		photoView3.setImageResource(R.drawable.function_detail3);
//		photoView3.setLayoutParams( new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
//		photoContainer.addView(photoView1);
//		photoContainer.addView(photoView2);
//		photoContainer.addView(photoView3);
		
		
		webView = new WebView(this);
		 WebSettings settings = webView.getSettings();
		 settings.setSupportZoom(true);//支持缩放
		 settings.setBuiltInZoomControls(true);//支持缩放控制
		 settings.setJavaScriptEnabled(true);//支持javascript
		 WebChromeClient chromeClient = new WebChromeClient(){
		     @Override
		     public boolean onConsoleMessage(ConsoleMessage consoleMessage) {//打印来自javascript中的的内容信息 console.log("你要打印的内容");
		  
		         return true;
		     }
		 };
		 webView.setWebChromeClient(chromeClient);
		 if(PublicUtils.ISCOMBINE){
			 webView.loadUrl("file:///android_asset/html/fuction_intro.html");
		 }else{//通用版
			 webView.loadUrl("file:///android_asset/html/waiqinjieshao.html");
		 }
		 
		 photoContainer.addView(webView);
		
		imageBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FunctionDetailActivity.this.finish();
				
			}
		});
		
	}
	
	
}
