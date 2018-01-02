package com.yunhu.yhshxc.webReport;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.dragimage.PhotoPriviewActivity;


public class WebViewUtilActivity extends Activity{
	public WebView webview;
	public String url;
	public String pm;//手机端横竖屏显示
	private ProgressBar progressBar1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_view_util_activity);
		webview = (WebView)findViewById(R.id.webview);
		progressBar1 = (ProgressBar)findViewById(R.id.progressBar1);
		url = getIntent().getStringExtra("url");
		pm = getIntent().getStringExtra("pm");
		setSrceenOrientation();
		initWebView();
		load();
	}
	
	private void setSrceenOrientation(){
		if (!TextUtils.isEmpty(pm)) {
			if ("1".equals(pm)) {//1横屏 2竖屏
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			}else{
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			}
		}
	}
	
	public void load(){
		webview.setWebViewClient(new WebViewClient(){

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				progressBar1.setVisibility(View.VISIBLE);
			}
			
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if(url.endsWith(".jpg") || url.endsWith(".JPG") || url.endsWith(".png") || url.endsWith(".PNG")){
					Intent intent = new Intent(WebViewUtilActivity.this,PhotoPriviewActivity.class);
					intent.putExtra("url", url);
					startActivity(intent);
					return true;
				}else if(url.contains("http://goback")){
					WebViewUtilActivity.this.finish();
//					return true;
				}else{
					view.loadUrl(url);
				}
				return super.shouldOverrideUrlLoading(view, url);
			}
			
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				progressBar1.setVisibility(View.GONE);
			}
			
		});
		if (!TextUtils.isEmpty(url)) {
			webview.loadUrl(url);
		}
	}
	
	protected void initWebView() {
		WebSettings webSetting = webview.getSettings();
		webSetting.setJavaScriptEnabled(true);
//		webSetting.setUseWideViewPort(false); // 将图片调整到适合webview的大小
		webSetting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);// 关闭webview中缓存
		webSetting.setLoadsImagesAutomatically(true); // 支持自动加载图片
		webSetting.setUseWideViewPort(true); 
		webSetting.setLoadWithOverviewMode(true);
		
		webSetting.setSupportZoom(true);
		webSetting.setBuiltInZoomControls(true);
		
		//webSetting.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		
//		webSetting.setDefaultZoom(ZoomDensity.CLOSE);
//		webSetting.setUseWideViewPort(true); 
//		webSetting.setLoadWithOverviewMode(true); 
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()){
			webview.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		load();
		super.onConfigurationChanged(newConfig);
	}

}
