package com.yunhu.yhshxc.attendance.leave;

import gcg.org.debug.JLog;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.yunhu.android.pulltorefresh.library.PullToRefreshBase;
import com.yunhu.android.pulltorefresh.library.PullToRefreshListView;
import com.yunhu.android.pulltorefresh.library.PullToRefreshBase.Mode;
import com.yunhu.android.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.loopj.android.http.RequestParams;

/**
 * 待审批
 * @author xuelinlin
 *
 */
public class WaitForApproLeaveActivity extends AbsBaseActivity implements OnItemClickListener{
	private LinearLayout ll_order3_layout;
	private TextView tv_attend_leave;
	private PullToRefreshListView leave_list;
	private List<AskForLeaveInfo> list  = new ArrayList<AskForLeaveInfo>();
	private List<AskForLeaveInfo> newItems  = new ArrayList<AskForLeaveInfo>();
	private LeaveAdapter adapter;
	private AskForLeaveInfoDB leaveDB;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.attend_for_leave);
		leaveDB = new AskForLeaveInfoDB(this);
		ll_order3_layout = (LinearLayout) this.findViewById(R.id.ll_order3_layout);
		ll_order3_layout.setVisibility(View.GONE);
		tv_attend_leave = (TextView) findViewById(R.id.tv_attend_leave);
		tv_attend_leave.setText(PublicUtils.getResourceString(this,R.string.leave_m1));
		init();
		findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				leaveDB.delete();
				finish();
			}
		});
	}
	private void init() {
		
		leave_list = (PullToRefreshListView) findViewById(R.id.leave_list);
		leave_list.setVisibility(View.VISIBLE);
		adapter = new LeaveAdapter(this, list);
		leave_list.setAdapter(adapter);
		search();
		String label = DateUtils.formatDateTime(this.getApplicationContext(),
				System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
						| DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_ABBREV_ALL);
		leave_list.getLoadingLayoutProxy().setLastUpdatedLabel(label);
		leave_list.setMode(Mode.PULL_FROM_END);
		leave_list.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (refreshView.getCurrentMode() == Mode.PULL_FROM_END) {
					search();
				}
			}

			
		});
		leave_list.setOnItemClickListener(this);
		
	}
	
	private Dialog searchDialog;

	private void search() {
		searchDialog = new MyProgressDialog(this, R.style.CustomProgressDialog,
				PublicUtils.getResourceString(this,R.string.init));
		String url = UrlInfo.queryLeaveInfo(this);
		RequestParams param = searchParams();
		GcgHttpClient.getInstance(this.getApplicationContext()).post(url,
				param, new HttpResponseListener() {
					@Override
					public void onStart() {
						if (pages == 1) {
							searchDialog.show();
						}
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						JLog.d(TAG, "onSuccess:" + content);
						try {
							JSONObject obj = new JSONObject(content);
							String resultCode = obj.getString("resultcode");
							if ("0000".equals(resultCode)) {
								String arrayStr = "";
								if (PublicUtils.isValid(obj, "data")) {
									arrayStr = obj.getString("data");
								}else{
									if(pages == 1){
										leaveDB.delete();
									}
								}
								if ("[]".endsWith(arrayStr)) {
									ToastOrder.makeText(getApplicationContext(),
											R.string.un_search_data4, ToastOrder.LENGTH_SHORT)
											.show();
								} else {
									
									JSONArray array = obj.optJSONArray("data");
									newItems.clear();
									if (pages == 1) {// 说明是刷新
										list.clear();
										newItems = new LeaveUtils(WaitForApproLeaveActivity.this).paseLeaveInfos(array,true);
									}else{
										newItems = new LeaveUtils(WaitForApproLeaveActivity.this).paseLeaveInfos(array,false);
									}
									
//									list.addAll(newItems);
									list= leaveDB.findAllLeaves();
									adapter.refresh(list);
									pages++;
								}
							} else {
								throw new Exception();
							}
						} catch (Exception e) {
							e.printStackTrace();
							ToastOrder.makeText(getApplicationContext(), R.string.ERROR_DATA,
									ToastOrder.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						JLog.d(TAG, "onFailure:" + content);
						ToastOrder.makeText(getApplicationContext(), R.string.retry_net_exception,
								ToastOrder.LENGTH_SHORT).show();
					}

					@Override
					public void onFinish() {
						if (searchDialog != null && searchDialog.isShowing()) {
							searchDialog.dismiss();
						}
						leave_list.onRefreshComplete();
					}

				});
	}
	public int pages = 1;

	private RequestParams searchParams() {

		RequestParams param = new RequestParams();
		param.put("phoneno",
				PublicUtils.receivePhoneNO(getApplicationContext()));
		param.put("userId", SharedPreferencesUtil.getInstance(this).getUserId());
		param.put("authDiv", "2");
		param.put("page", pages);
		
		JLog.d("alin", param.toString());
		return param;
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(list.size()>0){
			AskForLeaveInfo info = list.get(position-1);
			Intent intent = new Intent(WaitForApproLeaveActivity.this,CheckLeaveActivity.class);
			intent.putExtra("page", pages);
			intent.putExtra("id", info.getId());
			startActivityForResult(intent, 1);
		}
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		list = leaveDB.findAllLeaves();
		adapter.refresh(list);
	}
	@Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			leaveDB.delete();
			finish();
		}
		return super.onKeyDown(keyCode, event);
    }
	
}
