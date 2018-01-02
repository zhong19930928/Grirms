package com.yunhu.yhshxc.doubletask2;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.adapter.DoubleListAdapter;
import com.yunhu.yhshxc.bo.DoubleTask;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.Module;
import com.yunhu.yhshxc.database.DoubleDB;
import com.yunhu.yhshxc.database.FuncDB;
import com.yunhu.yhshxc.database.NewDoubleDB;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.parser.CacheData;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.yunhu.android.pulltorefresh.library.PullToRefreshBase;
import com.yunhu.android.pulltorefresh.library.PullToRefreshBase.Mode;
import com.yunhu.android.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.yunhu.android.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONObject;

import java.util.List;

import gcg.org.debug.JLog;

/**
 * 双向任务的列表activity
 * 以列表的形式展示出可以执行的双向任务
 * @author jishen
 * @version 2012-02-29
 *
 */
public class NewDoubleListActivity extends AbsBaseActivity implements OnItemClickListener{

	private String TAG="NewDoubleListActivity";
	/**
	 * 双向任务列表listview
	 */
	private PullToRefreshListView mPullRefreshListView;
	/**
	 * 可执行的双向任务集合
	 */
	private List<DoubleTask> doubleList;
	/**
	 *  双向任务列表listview 的adapter
	 */
	public DoubleListAdapter doubleListAdapter;
	/**
	 * 双向任务表
	 */
	private NewDoubleDB newDoubleDB;
	/**
	 * 数据ID
	 */
	private int targetId;
	/**
	 * title名称
	 */
	private TextView doubleHead;
	
	private boolean isNoWait;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_double_list);
		initBase();//父类的方法，目前没做处理
	}

	/**
	 * 初始化实例，获取传过来的数据
	 */
	private void init() {
		newDoubleDB=new NewDoubleDB(this);
		targetId=getIntent().getIntExtra("targetId", 0);//获取数据ID
		isNoWait = getIntent().getBooleanExtra("isNoWait", true);
		doubleHead=(TextView)findViewById(R.id.doubleHead);
		doubleHead.setText(getIntent().getStringExtra("doubleHead"));//获取title名称
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
//		mPullRefreshListView.setAdapter(doubleListAdapter);
		mPullRefreshListView.setOnItemClickListener(this);
		String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
				   			 DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
		mPullRefreshListView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
		mPullRefreshListView.setMode(Mode.PULL_FROM_START);
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				refresh();
			}
		});
//		mPullRefreshListView.setRefreshing(false);
		Func func=new FuncDB(this).findFuncByFirstColumn(targetId);//查找第一列显示的func
		if(func!=null){//有显示内容的情况
			doubleList=newDoubleDB.findNewDoubleTaskList(String.valueOf(func.getFuncId()), targetId);//查找所有的双向任务
			doubleListAdapter=new DoubleListAdapter(this, func,doubleList,targetId);
			if(doubleList.isEmpty()){
				ToastOrder.makeText(this, getResources().getString(R.string.no_readable_content), ToastOrder.LENGTH_SHORT).show();
				this.finish();
			}
		}else{//没有显示内容
			ToastOrder.makeText(this, getResources().getString(R.string.no_readable_content), ToastOrder.LENGTH_SHORT).show();
			this.finish();
		}
		mPullRefreshListView.setAdapter(doubleListAdapter);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		init();
	}
	
	
	private void refresh(){
		String url = UrlInfo.getUrlDoubleTask(this)+"?phoneno="+PublicUtils.receivePhoneNO(this)+"&"+"ids="+targetId;
		GcgHttpClient.getInstance(this).get(url, null, new HttpResponseListener(){
			@Override
			public void onFinish() {
				mPullRefreshListView.onRefreshComplete();
			}

			@Override
			public void onStart() {
				
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				JLog.d(TAG, "新双向："+content);
				try {
					if (!TextUtils.isEmpty(content)) {
						JSONObject obj = new JSONObject(content);
						if (obj.has("resultcode")) {
							String resultcode = obj.getString("resultcode");
							if ("0000".equals(resultcode)) {
								new DoubleDB(NewDoubleListActivity.this).removeAllDoubleTask(String.valueOf(targetId));
								new CacheData(NewDoubleListActivity.this).parseAll(content);
								Func func=new FuncDB(NewDoubleListActivity.this).findFuncByFirstColumn(targetId);//查找第一列显示的func
								if(func!=null){//有显示内容的情况
									doubleList=newDoubleDB.findNewDoubleTaskList(String.valueOf(func.getFuncId()), targetId);//查找所有的双向任务
									doubleListAdapter=new DoubleListAdapter(NewDoubleListActivity.this, func,doubleList,targetId);
									if(doubleList.isEmpty()){
										ToastOrder.makeText(NewDoubleListActivity.this, getResources().getString(R.string.no_readable_content), ToastOrder.LENGTH_SHORT).show();
										NewDoubleListActivity.this.finish();
									}
								}else{//没有显示内容
									ToastOrder.makeText(NewDoubleListActivity.this, getResources().getString(R.string.no_readable_content), ToastOrder.LENGTH_SHORT).show();
									NewDoubleListActivity.this.finish();
								}
								mPullRefreshListView.setAdapter(doubleListAdapter);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				
			}
			
		});
	}

	/**
	 * 单击双向任务item
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		DoubleTask doubleTask = doubleList.get(position-1);
		synchronizationData(doubleTask);
	}
	
	private Dialog synchronizationDialog;
	private void synchronizationData(final DoubleTask doubleTask){
		synchronizationDialog = new MyProgressDialog(this,R.style.CustomProgressDialog,getResources().getString(R.string.loading));
		String url = UrlInfo.getUrlDoubleTask(this)+"?phoneno="+PublicUtils.receivePhoneNO(this)+"&ids="+targetId+"@"+doubleTask.getTaskId();
		JLog.d(TAG, "新双向url："+url);
		GcgHttpClient.getInstance(this).get(url, null, new HttpResponseListener(){
			@Override
			public void onStart() {
				synchronizationDialog.show();
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				ToastOrder.makeText(NewDoubleListActivity.this, getResources().getString(R.string.BBS_LOAD_FAIL), ToastOrder.LENGTH_SHORT).show();
			}
			
			@Override
			public void onFinish() {
				synchronizationDialog.dismiss();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				JLog.d(TAG, "新双向："+content);
				try {
					JSONObject obj = new JSONObject(content);
					if (obj.has("resultcode")) {
						String resultcode = obj.getString("resultcode");
						if ("0000".equals(resultcode)) {
							new CacheData(NewDoubleListActivity.this).parseAll(content);
							Bundle bundle=new Bundle();
							bundle.putInt("taskId", doubleTask.getTaskId());//传递任务ID
							bundle.putString("createTime", doubleTask.getCreateTime());//创建时间
							bundle.putInt("targetId", targetId);//数据ID
							bundle.putString("total", doubleTask.getTatol());
							bundle.putString("cut", doubleTask.getCut());
							Module module=(Module) getIntent().getBundleExtra("bundle").getSerializable("module");//双向模块实例
							bundle.putSerializable("module",module);
							bundle.putString("dataStatus", doubleTask.getDataStatus());
							bundle.putString("doubleMasterTaskNo", doubleTask.getTaskNo());
							Intent intent = new Intent(NewDoubleListActivity.this,NewDoubleDetailActivity.class);
							intent.putExtra("currentDoubleTask", bundle);
							intent.putExtra("isNoWait", isNoWait);
							startActivity(intent);
						}
					}else{
						ToastOrder.makeText(NewDoubleListActivity.this, getResources().getString(R.string.BBS_LOAD_FAIL), ToastOrder.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
					ToastOrder.makeText(NewDoubleListActivity.this, getResources().getString(R.string.BBS_LOAD_FAIL), ToastOrder.LENGTH_SHORT).show();
				}
			}
			
		});
	}

}
