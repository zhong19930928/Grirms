package com.yunhu.yhshxc.wechat.exchange;

import gcg.org.debug.JLog;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.wechat.Util.WechatUtil;
import com.yunhu.yhshxc.wechat.bo.GroupUser;
import com.yunhu.yhshxc.wechat.bo.Topic;
import com.yunhu.yhshxc.wechat.db.GroupUserDB;
import com.yunhu.yhshxc.wechat.db.TopicDB;
import com.yunhu.yhshxc.wechat.survey.MyGridView;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.loopj.android.http.RequestParams;

public class PropertyActivity extends AbsBaseActivity {

	private int RESULT_CODE = 101;
	
	private Button btn_property_goback;
	private MyGridView property_gridview;
	private Button btn_exchange_close_topic;// 关闭话题
	private Button btn_exchange_attention;// 关注或取消关注
	private int topicId;
	private int groupId;
	private Topic topic;
	private TextView activity_property_title;
	private TextView activity_property_explain;
	private Button activity_property_start_time;
	private Button activity_property_end_time;
	private RadioButton activity_property_exchange;
	private RadioButton activity_property_survey;
	private RadioButton activity_property_approval;
	private TextView activity_property_speakNum;
	private RadioButton activity_property_no_must;
	private RadioButton activity_property_must;
	private RadioButton activity_property_everybody;
	private RadioButton activity_property_owner;
	private RadioButton activity_property_to_everybody;
	private RadioButton activity_property_to_owner;
	private TextView tv_exchange_title;
	private TextView activity_property_name;//发布人
	private TextView activity_property_department;//发布部门
	private TextView activity_property_date;
	private TopicDB topicDB;
	private String action = "c";//c 为关注 d 取消关注
	private int isHistory = 0;
	private List<GroupUser> groupUserList = new ArrayList<GroupUser>();
	private List<GroupUser> groupUsers = new ArrayList<GroupUser>();
	private GroupUserDB groupUserDB;
	private LinearLayout ll_show_all_groupusers;
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_property);
		groupUserDB = new GroupUserDB(this);
		// getData();
		init();
	}

	private void init() {
		topicDB = new TopicDB(this);
		Intent intent = getIntent();
		// 获取数据
		topicId = intent.getIntExtra("topicId", 0);
		groupId = intent.getIntExtra("groupId", 0);
		isHistory = intent.getIntExtra("isHistory", 0);

		btn_property_goback = (Button) findViewById(R.id.btn_property_goback);
		property_gridview = (MyGridView) findViewById(R.id.property_gridview);
		btn_exchange_close_topic = (Button) findViewById(R.id.btn_exchange_close_topic);
		btn_exchange_attention = (Button) findViewById(R.id.btn_exchange_attention);
		activity_property_title = (TextView) findViewById(R.id.activity_property_title);
		activity_property_explain = (TextView) findViewById(R.id.activity_property_explain);
		activity_property_start_time = (Button) findViewById(R.id.activity_property_start_time);
		activity_property_end_time = (Button) findViewById(R.id.activity_property_end_time);
		activity_property_exchange = (RadioButton) findViewById(R.id.activity_property_exchange);
		activity_property_survey = (RadioButton) findViewById(R.id.activity_property_survey);
		activity_property_approval = (RadioButton) findViewById(R.id.activity_property_approval);
		activity_property_speakNum = (TextView) findViewById(R.id.activity_property_speakNum);
		activity_property_no_must = (RadioButton) findViewById(R.id.activity_property_no_must);
		activity_property_must = (RadioButton) findViewById(R.id.activity_property_must);
		activity_property_everybody = (RadioButton) findViewById(R.id.activity_property_everybody);
		activity_property_owner = (RadioButton) findViewById(R.id.activity_property_owner);
		activity_property_to_everybody = (RadioButton) findViewById(R.id.activity_property_to_everybody);
		activity_property_to_owner = (RadioButton) findViewById(R.id.activity_property_to_owner);
		tv_exchange_title = (TextView) findViewById(R.id.tv_exchange_title);
		activity_property_name = (TextView) findViewById(R.id.activity_property_name);
		activity_property_department = (TextView) findViewById(R.id.activity_property_department);
		activity_property_date = (TextView) findViewById(R.id.activity_property_date);
		ll_show_all_groupusers = (LinearLayout) findViewById(R.id.ll_show_all_groupusers);
		
		setGridViewData();
		btn_property_goback.setOnClickListener(this);
		btn_exchange_close_topic.setOnClickListener(this);
		btn_exchange_attention.setOnClickListener(this);
		ll_show_all_groupusers.setOnClickListener(this);
		getData();
		activity_property_title.setFocusable(true);
		activity_property_title.setFocusableInTouchMode(true);
		activity_property_title.requestFocus();
		
	}

	// 给页面赋值 topic
	private void setData() {
		
		if(!TextUtils.isEmpty(String.valueOf(topic.getCreateUserId()))){
			if(topic.getCreateUserId() == SharedPreferencesUtil.getInstance(this).getUserId()){
				if(0 == topic.getIsClose()){
					btn_exchange_close_topic.setVisibility(View.VISIBLE);
				}
				
			}
		}
		
		if(!TextUtils.isEmpty(topic.getCreateUserName())){
			activity_property_name.setText(topic.getCreateUserName());
		}
		
		if(!TextUtils.isEmpty(topic.getCreateOrgName())){
			activity_property_department.setText(topic.getCreateOrgName());
		}
		
		
		if(1 == isHistory){
			btn_exchange_attention.setVisibility(View.GONE);
		}
		
		if(!TextUtils.isEmpty(topic.getCreateTime())){
			activity_property_date.setText(topic.getCreateTime().length() > 10?topic.getCreateTime().substring(0, 10):topic.getCreateTime());
		}
		
		if(TextUtils.isEmpty(topic.getTitle())){
			activity_property_title.setText(R.string.wechat_null);
			tv_exchange_title.setText(R.string.wechat_null);
		}else{
			activity_property_title.setText(topic.getTitle());
			tv_exchange_title.setText(topic.getTitle());
		}
		if(TextUtils.isEmpty(topic.getExplain())){
			activity_property_explain.setText(R.string.wechat_null);
		}else{
			activity_property_explain.setText(topic.getExplain());
		}
		if(!TextUtils.isEmpty(topic.getFrom())){
			activity_property_start_time.setText(topic.getFrom());
		}
		if(!TextUtils.isEmpty(topic.getTo())){
			activity_property_end_time.setText(topic.getTo());
		}
		
		if(!TextUtils.isEmpty(String.valueOf(topic.getIsAttention()))){
			if(topic.getIsAttention() == 0){
				btn_exchange_attention.setBackgroundResource(R.drawable.wechat_focus_m);
				action = "c";
			}else{
				btn_exchange_attention.setBackgroundResource(R.drawable.wechat_yishoucang);
				action = "d";
			}
		}
		
	}

	// 获取topic的值
	private void getData() {
		topic = topicDB.findTopicById(topicId);
		setData();
//		search();

	}

	private RequestParams params() {

		RequestParams params = new RequestParams();
		params.put("phoneno", PublicUtils.receivePhoneNO(this));
		params.put("topicId", topicId);
		params.put("groupId", groupId);

		JLog.d(TAG, "params:" + params.toString());
		return params;

	}

	private void search() {

		String url = UrlInfo.weChatTopicInfo(this);
		GcgHttpClient.getInstance(this).post(url, params(),
				new HttpResponseListener() {

					@Override
					public void onSuccess(int statusCode, String content) {
						JLog.d(TAG, "content:" + content);
						try {
							JSONObject obj = new JSONObject(content);
							String resultcode = obj.getString("resultcode");
							String topicStr = obj.getString("topic");
							JLog.d("abby", resultcode);
							if ("0000".equals(resultcode)){

								if (TextUtils.isEmpty(topicStr)) {
									ToastOrder.makeText(getApplicationContext(),
											R.string.wechat_content50, ToastOrder.LENGTH_SHORT)
											.show();
								} else {
									JSONObject topObj = obj.getJSONObject("topic");
									WechatUtil wechatUtil = new WechatUtil(getApplicationContext());
									topic = wechatUtil.parserSearchTopicListItem(topObj);
									setData();

								}
								ToastOrder.makeText(getApplicationContext(),
										R.string.wechat_content49, ToastOrder.LENGTH_LONG).show();

							} else {
								throw new Exception();
							}
						} catch (Exception e) {
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

	private void setGridViewData() {
		groupUserList = groupUserDB.findGroupUserByGroupId(groupId);
		if(groupUserList.size() > 12){
			for(int i = 0;i < 12;i++){
				groupUsers.add(groupUserList.get(i));
			}
			ll_show_all_groupusers.setVisibility(View.VISIBLE);
		}else{
			for(int i = 0;i < groupUserList.size();i++){
				groupUsers.add(groupUserList.get(i));
			}
		}
		
		
		WechatGrridViewAdapter wechatGrridViewAdapter = new WechatGrridViewAdapter(this, groupUsers);
		property_gridview.setAdapter(wechatGrridViewAdapter);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_property_goback:
		    setResult(isHistory);
			this.finish();
			break;
		case R.id.btn_exchange_close_topic:// 关闭话题
			close();
			break;
		case R.id.btn_exchange_attention:// 关注或取消关注
			guanzhu();
			break;
			
		case R.id.ll_show_all_groupusers:
			Intent intent = new Intent(getApplicationContext(),GroupUserActivity.class);
			intent.putExtra("groupId", groupId);
			startActivity(intent);
			break;

		default:
			break;
		}

	}

	private void close(){

		dialog = new MyProgressDialog(this,R.style.CustomProgressDialog,PublicUtils.getResourceString(this,R.string.wechat_content55));

		String url = UrlInfo.doWebChatTopicInfo(this);

		
		String data = "";
		try {
			JSONObject obj = new JSONObject();
			obj.put("topicId", topicId);
			obj.put("status", 0);
			obj.put("groupId", groupId);
			obj.put("title", topic.getTitle());
			obj.put("classify", topic.getClassify());
			data = obj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestParams params = new RequestParams();
		params.put("data", data);
		
	
		
		JLog.d("abby", params.toString());
		GcgHttpClient.getInstance(this).get(url, params, new HttpResponseListener() {
			
			@Override
			public void onSuccess(int statusCode, String content) {
				JLog.d("abby", "content" + content);
				try {
					JSONObject obj = new JSONObject(content);
					String resultCode = obj.getString("resultcode");
					
					if ("0000".equals(resultCode)) {
						ToastOrder.makeText(PropertyActivity.this, R.string.wechat_content54, ToastOrder.LENGTH_SHORT).show();
						btn_exchange_close_topic.setVisibility(View.GONE);
						btn_exchange_attention.setVisibility(View.GONE);
						isHistory = 1;
					}
				} catch (Exception e) {
					ToastOrder.makeText(PropertyActivity.this, R.string.wechat_content53, ToastOrder.LENGTH_SHORT).show();
				}
			}
			
			@Override
			public void onStart() {
				dialog.show();
			}
			
			@Override
			public void onFinish() {
				dialog.dismiss();
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				
			}
		});
	
		
	}
	
	 @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) { 
	        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { 
	            setResult(isHistory);
			    this.finish();
	        } 
	        return true; 
	    }
  
	
	private Dialog dialog = null;
	private void guanzhu(){
		dialog = new MyProgressDialog(this,R.style.CustomProgressDialog,PublicUtils.getResourceString(this,R.string.submint_loding));
		String url = UrlInfo.doWeChatFollowInfo(this);
		String data = "";
		try {
			JSONObject obj = new JSONObject();
			obj.put("followId", topicId);
			obj.put("type", 1);
			obj.put("action", action);
			data = obj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestParams params = new RequestParams();
		params.put("data", data);
		
		GcgHttpClient.getInstance(this).get(url, params, new HttpResponseListener() {
			
			@Override
			public void onSuccess(int statusCode, String content) {
				try {
					JSONObject obj = new JSONObject(content);
					String resultCode = obj.getString("resultcode");
					if ("0000".equals(resultCode)) {
						if("c".equals(action)){
							topic.setIsAttention(1);
							topicDB.updateTopic(topic);
							btn_exchange_attention.setBackgroundResource(R.drawable.wechat_yishoucang);
							action = "d";
						}else if("d".equals(action)){
							topic.setIsAttention(0);
							topicDB.updateTopic(topic);
							ToastOrder.makeText(PropertyActivity.this, R.string.wechat_content52, ToastOrder.LENGTH_SHORT).show();
							btn_exchange_attention.setBackgroundResource(R.drawable.wechat_focus_m);
							action = "c";
						}
						sendBroadcast(new Intent(Constants.BROADCAST_WECHAT_FOCUS));
					}
				} catch (Exception e) {
					ToastOrder.makeText(PropertyActivity.this, R.string.wechat_content51, ToastOrder.LENGTH_SHORT).show();
				}
			}
			
			@Override
			public void onStart() {
				dialog.show();
			}
			
			@Override
			public void onFinish() {
				dialog.dismiss();
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				
			}
		});
	}
}
