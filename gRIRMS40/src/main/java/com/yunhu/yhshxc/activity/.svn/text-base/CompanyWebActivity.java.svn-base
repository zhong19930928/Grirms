package com.yunhu.yhshxc.activity;

import com.yunhu.yhshxc.R;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

public class CompanyWebActivity extends AbsBaseActivity {
	private WebView mWebview;
	private ImageView companyweb_back;
	private TextView companyweb_title;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_companyweb);

		initView();
	}

	private void initView() {
		Intent intent = getIntent();
		String url = intent.getStringExtra("company_url");
		String company_name = intent.getStringExtra("company_name");
		companyweb_back = (ImageView) findViewById(R.id.companyweb_back);
		companyweb_back.setOnClickListener(this);
		companyweb_title = (TextView) findViewById(R.id.companyweb_title);
		
		mWebview = (WebView) findViewById(R.id.company_webview);
		// mTextView = (TextView) findViewById(R.id.mTextView);
		WebSettings webSettings = mWebview.getSettings();// 设置
		webSettings.setBuiltInZoomControls(true);// 可进行缩放
		webSettings.setSupportZoom(true);// 支持缩放
		webSettings.setJavaScriptEnabled(true);// 支持jsp
		webSettings.setUseWideViewPort(true);//
		webSettings.setLoadWithOverviewMode(true);
		if (!TextUtils.isEmpty(url)) {
			mWebview.loadUrl(url);
		}
		if (!TextUtils.isEmpty(company_name)) {
			companyweb_title.setText(company_name);
		}
		// 设置不调用系统浏览器
		mWebview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		// 可获取加载进度
		mWebview.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);

			}

			@Override
			public void onReceivedTitle(WebView view, String title) {

				super.onReceivedTitle(view, title);
			}

		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.companyweb_back:
        this.finish();
			break;

		default:
			break;
		}
	}

}
