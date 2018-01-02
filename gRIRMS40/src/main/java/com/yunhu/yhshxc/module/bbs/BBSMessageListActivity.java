package com.yunhu.yhshxc.module.bbs;



import gcg.org.debug.JLog;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.bo.BbsInformationItem;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.UrlInfo;

public class BBSMessageListActivity extends AbsBaseActivity implements OnScrollListener{
	private String TAG="BBSMessageListActivity";
	public ListView msgList;
	public ImageView editIv, refalseIv;
	public List<BbsInformationItem> informationList; 
	public BBSMessageAdapter messageAdapter;
	public ProgressBar reflastPb;
	public View footView;
	public LinearLayout cliclLayout,loadLayout;
	public boolean isLoadind=false;
	private int lastItem = 0;// listView分页
	private String userid;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bbs_message_list);
		initBase();
		init();
	}

	private void init() {
		File userFile=new File(Constants.USERIV_PATH);
		File photoFile=new File(Constants.CONTENTIV_PATH);
		if(!userFile.exists()){
			userFile.mkdirs();
		}
		if(!photoFile.exists()){
			photoFile.mkdirs();
		}
		userid=getIntent().getStringExtra("userid");
		msgList = (ListView) findViewById(R.id.bbs_msg_listView);
		editIv = (ImageView) findViewById(R.id.bbs_msg_edit_iv);
		refalseIv = (ImageView) findViewById(R.id.bbs_msg_reflash_iv);
		reflastPb=(ProgressBar)findViewById(R.id.bbs_msg_reflash_pb);
		footView=View.inflate(this, R.layout.bbs_loadview, null);
		cliclLayout=(LinearLayout) footView.findViewById(R.id.bbs_startload_ll);
		loadLayout=(LinearLayout) footView.findViewById(R.id.bbs_loading_ll);
		footView.setOnClickListener(footViewClick);
		refalseIv.setOnClickListener(this);
		editIv.setOnClickListener(this);
		msgList.setOnScrollListener(this);

		BBSGetMessage message=new BBSGetMessage(this);
		HashMap<String, String> map=new HashMap<String, String>();
		map.put("userid", userid);
		map.put("phoneno",PublicUtils.receivePhoneNO(this));
		message.setParamMap(map);
		message.execute(UrlInfo.getUrlBbsInformation(BBSMessageListActivity.this));
		
		
		messageAdapter=new BBSMessageAdapter(this);
		messageAdapter.setListView(msgList);
		msgList.setAdapter(messageAdapter);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.bbs_msg_edit_iv:
			editBbs();
			break;
		case R.id.bbs_msg_reflash_iv:
			reflash();
			break;
		default:
			break;
		}

	}

	/**
	 * 点击加载按钮
	 */
	private OnClickListener footViewClick=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(!isLoadind){//如果没有点击加载
				isLoadind=true;
				cliclLayout.setVisibility(View.GONE);
				loadLayout.setVisibility(View.VISIBLE);
				loadData();
			}
			
		}
	};
	
	/**
	 * 加载新数据
	 */
	private void loadData(){
		List<BbsInformationItem> tempList = messageAdapter.getInformationList();
		if(tempList==null||tempList.isEmpty()){
			reflash();
		}else{
			BbsInformationItem item=tempList.get(tempList.size()-1);
			String date=item.getBbsInfoDetail().getCreateTime();
			HashMap<String, String> map=new HashMap<String, String>();
			BBSGetMessage getMessage=new BBSGetMessage(this);
			map.put("userid", userid);
			map.put("phoneno",PublicUtils.receivePhoneNO(this));
//			map.put("phoneno","18910901892");
			map.put("createtime",date);
			getMessage.setParamMap(map);
			getMessage.setIsReflash(false);
			JLog.d(TAG, "userid==>"+userid);
			JLog.d(TAG, "phoneno==>"+PublicUtils.receivePhoneNO(this));
			JLog.d(TAG, "createtime==>"+date);
			getMessage.execute(UrlInfo.getUrlBbsInformation(BBSMessageListActivity.this));
		}
	}
	
	/**
	 * 刷新
	 */
	private void reflash(){
		refalseIv.setVisibility(View.GONE);
		reflastPb.setVisibility(View.VISIBLE);
		HashMap<String, String> map=new HashMap<String, String>();
		map.put("userid", userid);
		map.put("phoneno",PublicUtils.receivePhoneNO(this));
//		map.put("phoneno","18910901892");
		BBSGetMessage getMessage=new BBSGetMessage(this);
		getMessage.setIsReflash(true);
		getMessage.setParamMap(map);
		getMessage.execute(UrlInfo.getUrlBbsInformation(BBSMessageListActivity.this));
	}
	/**
	 * 发帖
	 */
	private void editBbs(){
		Intent intent=new Intent(this,BBSPublishActivity.class);
		startActivity(intent);
		Constants.isNeadReflash=true;
	}
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		List<BbsInformationItem> tempList = messageAdapter.getInformationList();
		if(tempList!=null){
			JLog.d(TAG, "tempList==>"+tempList.size());
			if (lastItem == tempList.size()&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
				if(msgList.getFooterViewsCount()==0 && tempList.size()>=20){
					msgList.addFooterView(footView);
				}
				cliclLayout.setVisibility(View.VISIBLE);
				loadLayout.setVisibility(View.GONE);
			}
		}
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		lastItem = firstVisibleItem + visibleItemCount;
		JLog.d(TAG, "lastItem==>"+lastItem);
		JLog.d(TAG, "firstVisibleItem==>"+firstVisibleItem);
		JLog.d(TAG, "visibleItemCount==>"+visibleItemCount);
		JLog.d(TAG, "totalItemCount==>"+totalItemCount);
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		if(Constants.isNeadReflash){
			Constants.isNeadReflash=false;
			userid=null;
		}
		reflash();
	}
	
}
