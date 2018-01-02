package com.yunhu.yhshxc.activity.onlineExamination;

import org.json.JSONException;
import org.json.JSONObject;

import gcg.org.debug.JLog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.activity.onlineExamination.adapter.OnLineExamAllAdapter;
import com.yunhu.yhshxc.activity.onlineExamination.util.ExamUtil;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.android.pulltorefresh.library.PullToRefreshBase;
import com.yunhu.android.pulltorefresh.library.PullToRefreshListView;
import com.yunhu.android.pulltorefresh.library.PullToRefreshBase.Mode;
import com.yunhu.android.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.loopj.android.http.RequestParams;

public class OnlineExaminationActivity extends AbsBaseActivity {
	private PullToRefreshListView lv_online_exam;
	private OnLineExamAllAdapter adapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.online_exam_activity);
		initView();
		initData();
	}
	
	private void initView() {
		adapter = new OnLineExamAllAdapter(this);
		lv_online_exam = (PullToRefreshListView) this.findViewById(R.id.lv_online_exam);
		lv_online_exam.setAdapter(adapter);
		String label = DateUtils.formatDateTime(this.getApplicationContext(),
				System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
						| DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_ABBREV_ALL);
		lv_online_exam.getLoadingLayoutProxy().setLastUpdatedLabel(label);
		lv_online_exam.setMode(Mode.DISABLED);
		lv_online_exam
				.setOnRefreshListener(new OnRefreshListener<ListView>() {
					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// 向下滑动
						if (refreshView.getCurrentMode() == Mode.PULL_FROM_END) {
							lv_online_exam.onRefreshComplete();
						}
						// 向上加载
						if (refreshView.getCurrentMode() == Mode.PULL_FROM_START) { 
							lv_online_exam.onRefreshComplete();
						}

					}
				});
		
		lv_online_exam.setOnItemClickListener(itemClickListener);
		
	}
	private void initData() {
		String url = UrlInfo.queryOnlineInfo(this);
		RequestParams param = searchParams();
		GcgHttpClient.getInstance(this).post(url, param, new HttpResponseListener() {
			
			@Override
			public void onSuccess(int statusCode, String content) {
				JLog.d(TAG, "onSuccess:" + content);
				JSONObject obj;
				try {
					obj = new JSONObject(content);
					String resultCode = obj.getString("resultcode");
					if ("0000".equals(resultCode)) {
						ExamUtil uti = new ExamUtil(OnlineExaminationActivity.this);
						uti.parseExamnation(obj);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
			
			@Override
			public void onStart() {
				
			}
			
			@Override
			public void onFinish() {
				
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				
			}
		});
	}
	private RequestParams searchParams() {
		RequestParams param = new RequestParams();
//		param.put("phoneno",PublicUtils.receivePhoneNO(getApplicationContext()));
		param.put("phoneno","18910901862");
		JLog.d(TAG, param.toString());
		return param;
	}
	private OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = new Intent(OnlineExaminationActivity.this,OnlineExamQuestionActivity.class);
			startActivity(intent);
		}
	};
}
