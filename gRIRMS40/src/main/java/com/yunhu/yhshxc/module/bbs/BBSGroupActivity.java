package com.yunhu.yhshxc.module.bbs;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.bo.BbsUserInfo;
import com.yunhu.yhshxc.parser.BBSParse;
import com.yunhu.yhshxc.utility.HttpHelper;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;

import java.util.ArrayList;
import java.util.List;

import gcg.org.debug.JLog;

/**
 * 
 * @author 石宗银 12.6.26 圈子列表
 */
public class BBSGroupActivity extends AbsBaseActivity {
	private static final String TAG = "BBSGroupActivity";
	private ListView lv;
	private BBSGroupAdapter adapter;
//	private View footerView;
	private List<BbsUserInfo> onceQueryList = new ArrayList<BbsUserInfo>();
	private List<BbsUserInfo> allList = new ArrayList<BbsUserInfo>();
	private RelativeLayout backLayout;
	private BBSParse bbsParse;
//	private final int PAGE_SIZE = 20;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bbs_group);
		initBase();
		init();
		new AllGroupTask().execute();
	}
	private void init() {
//		backLayout = (RelativeLayout) findViewById(R.id.ll_bbs_group_bottom);
//		backLayout.setOnClickListener(this);
		
		adapter = new BBSGroupAdapter(this, onceQueryList);
		lv = (ListView) findViewById(R.id.lv_group);
		
//		footerView = LayoutInflater.from(this).inflate(R.layout.bbs_loadview, null);
//		footerView.findViewById(R.id.bbs_loading_ll).setVisibility(View.VISIBLE);
//		footerView.findViewById(R.id.bbs_startload_ll).setVisibility(View.GONE);
//		footerView.findViewById(R.id.bbs_startload_ll).setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				new AllGroupTask().execute(null);
//			}
//		});
		
//		lv.addFooterView(footerView);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(adapter.getList() == null){
					return;
				}
				if (position == adapter.getList().size()) {
					return;
				}
				Intent intent = new Intent(BBSGroupActivity.this, BBSMessageListActivity.class);
				intent.putExtra("userid", adapter.getList().get(position).getId());
				startActivity(intent);
				BBSGroupActivity.this.finish();
			}
		});
		bbsParse = new BBSParse();
	}
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.ll_bbs_group_bottom:
//			finish();
//			break;
//		default:
//			break;
//		}
//	}

	class AllGroupTask extends AsyncTask<String, Integer, String> {
		

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			String phoneno = getSharedPreferences("MOBILE_MDN_PREF", Context.MODE_PRIVATE).getString("MOBILE_MDN_NO_PREF", "");
			String userInfoUrl = UrlInfo.getUrlBbsUserInfo(BBSGroupActivity.this)+"?phoneno="+phoneno;
			JLog.d(TAG, "userInfoUrl ==> "+userInfoUrl);
			HttpHelper helper= new HttpHelper(BBSGroupActivity.this);
			isContinue(helper.connectGet(userInfoUrl));
			return helper.connectGet(userInfoUrl);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			//JLog.d("UserInfoList-JSON=====>" + result);
			try {
				
				onceQueryList = bbsParse.parseUserInfo(result);
				
				if(onceQueryList==null)
					return;
				
				JLog.d(TAG, "onceQueryList.size()==>"+onceQueryList.size());
				
				
				
				allList = adapter.getList();
				if (!onceQueryList.isEmpty()) {
					if(allList == null){
						allList = new ArrayList<BbsUserInfo>();
					}
					allList.addAll(onceQueryList);
					adapter.setList(allList);
//					footerView.findViewById(R.id.bbs_loading_ll).setVisibility(View.GONE);
////					footerView.findViewById(R.id.bbs_startload_ll).setVisibility(View.VISIBLE);
//					if (onceQueryList.size() < PAGE_SIZE) {
//						lv.removeFooterView(footerView);
//					}
					adapter.notifyDataSetChanged();
				} else {
//					lv.removeFooterView(footerView);
					adapter.notifyDataSetChanged();
					if (allList.size()>0 && onceQueryList.size() == 0) {
	//					ToastOrder.makeText(GroupActivity.this, "下面没有圈子好友啦！", 1).show();
					}
				}
				
				
				
			} catch (Exception e) {
				JLog.e(e);
				ToastOrder.makeText(BBSGroupActivity.this, getResources().getString(R.string.bbs_info_10), ToastOrder.LENGTH_LONG).show();
//				footerView.findViewById(R.id.bbs_loading_ll).setVisibility(View.GONE);
//				footerView.findViewById(R.id.bbs_startload_ll).setVisibility(View.VISIBLE);
			}
		}
		private void isContinue(String result){
			//4.1版本以上
			if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN){
			}else{
					Message message = mHandler.obtainMessage();
					message.obj = result;
					message.sendToTarget();
			}
		}
		
		private Handler mHandler = new  Handler(){
			public void handleMessage(Message msg) {
				String result =  (String) msg.obj;
				try {
					
					onceQueryList = bbsParse.parseUserInfo(result);
					JLog.d(TAG, "onceQueryList.size()==>"+onceQueryList.size());
					allList = adapter.getList();
					if (!onceQueryList.isEmpty()) {
						if(allList == null){
							allList = new ArrayList<BbsUserInfo>();
						}
						allList.addAll(onceQueryList);
						adapter.setList(allList);
//						footerView.findViewById(R.id.bbs_loading_ll).setVisibility(View.GONE);
////						footerView.findViewById(R.id.bbs_startload_ll).setVisibility(View.VISIBLE);
//						if (onceQueryList.size() < PAGE_SIZE) {
//							lv.removeFooterView(footerView);
//						}
						adapter.notifyDataSetChanged();
					} else {
//						lv.removeFooterView(footerView);
						adapter.notifyDataSetChanged();
						if (allList.size()>0 && onceQueryList.size() == 0) {
		//					ToastOrder.makeText(GroupActivity.this, "下面没有圈子好友啦！", 1).show();
						}
					}
				} catch (Exception e) {
					JLog.e(e);
					ToastOrder.makeText(BBSGroupActivity.this, getResources().getString(R.string.bbs_info_10), ToastOrder.LENGTH_LONG).show();
//					footerView.findViewById(R.id.bbs_loading_ll).setVisibility(View.GONE);
//					footerView.findViewById(R.id.bbs_startload_ll).setVisibility(View.VISIBLE);
				}
			};
		};
				
	}

}

