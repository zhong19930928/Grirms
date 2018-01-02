package com.yunhu.yhshxc.wechat.exchange;

import gcg.org.debug.JLog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.wechat.Util.SharedPrefrencesForWechatUtil;
import com.yunhu.yhshxc.wechat.bo.PersonalWechat;
import com.yunhu.yhshxc.wechat.bo.Reply;
import com.yunhu.yhshxc.wechat.db.PersonalWechatDB;
import com.yunhu.yhshxc.wechat.db.ReplyDB;
import com.yunhu.yhshxc.wechat.db.TopicDB;
import com.yunhu.android.pulltorefresh.library.PullToRefreshBase;
import com.yunhu.android.pulltorefresh.library.PullToRefreshBase.Mode;
import com.yunhu.android.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.loopj.android.http.RequestParams;

public class PersonalWechatActivity   extends ExchangeActivity{
	
	private int userId;
	private String userName;
	private List<PersonalWechat> personalWechatList;
	private PersonalWechatDB personalWechatDB;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		intentData();
		super.onCreate(savedInstanceState);
		isPersonalWechat = true;
		btn_exchange_property.setVisibility(View.INVISIBLE);
		personalWechatList = new ArrayList<PersonalWechat>();
		personalWechatDB = new PersonalWechatDB(this);
		registSubmitSuccessReceiver();
		getData();
		personalWechatDB.updateAllPersonalWechatToIsRead(userId, SharedPreferencesUtil.getInstance(this).getUserId());
	}
	
	@Override
	public void registSubmitSuccessReceiver() {
		IntentFilter filter = new IntentFilter(Constants.BROADCAST_WECHAT_PRIVATE);
		registerReceiver(wechat_broadcast_receiver, filter);
	}
	
	private BroadcastReceiver wechat_broadcast_receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent != null && Constants.BROADCAST_WECHAT_PRIVATE.equals(intent.getAction())) {
				
				List<PersonalWechat> pList = personalWechatDB.findPersonalWechatList(userId, SharedPreferencesUtil.getInstance(PersonalWechatActivity.this).getUserId());
				replyListAll.clear();
				for (int i = 0; i < pList.size(); i++) {
					PersonalWechat pw = pList.get(i);
					Reply reply = personalWechatToReply(pw);
					replyListAll.add(reply);
					JLog.d(TAG, "PW:"+pw.getContent());
				}
				exchangeAdapter.setReplyList(replyListAll);
				exchangeAdapter.refresh(replyListAll);
				scrollToBottom();
			}
		}

	};
	
	
	@Override
	@SuppressLint("NewApi")
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_exchange_submit:
			submitReply();
			break;
		case R.id.btn_exchange_add:
			add();
			break;
		case R.id.btn_topic_back:
			this.finish();
			break;
		default:
			break;
		}

	}
	
	private PersonalWechat submitPW;
	public void submitReply(){
		String content = ed_exchange_comment.getText().toString();
		if (!(TextUtils.isEmpty(content) && TextUtils.isEmpty(replyPhoto) && TextUtils.isEmpty(atachmentPath))) {
			submitPW = new PersonalWechat();
			submitPW.setAttachment(atachmentPath);
			submitPW.setPhoto(replyPhoto);
			submitPW.setContent(content);
			submitPW.setdUserId(userId);
			submitPW.setsUserId(SharedPreferencesUtil.getInstance(this).getUserId());
			submitPW.setDate(DateUtil.getCurDateTime());
			submitPW.setMsgKey(String.valueOf(System.currentTimeMillis())+PublicUtils.receivePhoneNO(this));
			String groupKey = String.valueOf(submitPW.getsUserId())+String.valueOf(submitPW.getdUserId());
			submitPW.setGroupKey(groupKey);
			submitPW.setIsRead(1);
			submitPW.setsUserName(SharedPreferencesUtil.getInstance(this).getUserName());
			submitPW.setdUserName(userName);
			
			Reply r = personalWechatToReply(submitPW);
			submitAtachment(r);
			submitReplyPhoto(r);
			
			String url = UrlInfo.doWeChatPrivateInfo(this);
			GcgHttpClient.getInstance(this).post(url, submitParams(),
					new HttpResponseListener() {
						@Override
						public void onSuccess(int statusCode, String content) {
							JLog.d(TAG, "content:" + content);
							try {
								JSONObject obj = new JSONObject(content);
								String resultcode = obj.getString("resultcode");
								if ("0000".equals(resultcode)) {
									new PersonalWechatDB(PersonalWechatActivity.this).insert(submitPW);
								} else {
									throw new Exception();
								}
							} catch (Exception e) {
								
							}
							sendBroadcast(new Intent(Constants.BROADCAST_WECHAT_PERSON));
						}

						@Override
						public void onStart() {
							if (ed_exchange_comment!=null) {
								ed_exchange_comment.setText("");
								ed_exchange_comment.setHint("");
								PublicUtils.hideKeyboard(PersonalWechatActivity.this);
								
							}
							replyListAll.add(personalWechatToReply(submitPW));
							exchangeAdapter.setReplyList(replyListAll);
							exchangeAdapter.refresh(replyListAll);
							scrollToBottom();
						}

						@Override
						public void onFinish() {
						}

						@Override
						public void onFailure(Throwable error, String content) {
						}
					});
			atachmentPath = "";
			videoPathList.clear();
			videoViewList.clear();
			voicePathList.clear();
			voiceViewList.clear();
			photoPathList.clear();
			photoViewList.clear();
			filePathList.clear();
			fileViewList.clear();
			if (contentFragments!=null) {
				contentFragments.setWechatMsg("");
			}
			
//			atachmentPath = "";
//			videoPathList.clear();
//			voicePathList.clear();
//			photoPathList.clear();
//			filePathList.clear();
//			videoViewList.clear();
//			voiceViewList.clear();
//			photoViewList.clear();
//			fileViewList.clear();
		}else{
			Toast.makeText(this, R.string.wechat_content56, Toast.LENGTH_LONG).show();
		}
//		Toast.makeText(this, "发送:"+content, Toast.LENGTH_SHORT).show();
	}
	
	
	private String msgJson(PersonalWechat pw){
		String json = "";
		try {
			JSONObject dObj = new JSONObject();
			dObj.put("toId", pw.getdUserId());// 回帖人Id
			dObj.put("fromId", pw.getsUserId());// 发送人Id
			dObj.put("content", pw.getContent());
			dObj.put("time", pw.getDate());// 回帖时间
			dObj.put("lon", "");// 经度
			dObj.put("lat", "");// 纬度
			dObj.put("address", "");// 地址
			dObj.put("gisType", "");// 类型
			dObj.put("params", "");// 参数
			dObj.put("msgKey",System.currentTimeMillis()+PublicUtils.receivePhoneNO(this));// 参数
			if (!TextUtils.isEmpty(pw.getAttachment())) {
				JSONObject attObj = new JSONObject(pw.getAttachment());
				JSONArray attArr = new JSONArray();
				Iterator<String> iterator = attObj.keys();
				while (iterator.hasNext()) {
					String key = iterator.next();
					String url = attObj.getString(key);
					JSONObject aObj = new JSONObject();
					if (url.endsWith(".3gp")) {
						int index = url.lastIndexOf("/");
						String fileName = url.substring(index + 1);
						aObj.put("name", fileName);
						aObj.put("showName", fileName);
					}else{
						aObj.put("name", key);
						String showName = SharedPrefrencesForWechatUtil.getInstance(this).getGetFileName(key);
						aObj.put("showName", showName);
					}
					attArr.put(aObj);
				}
				dObj.put("annexFile", attArr);
			}
			if (!TextUtils.isEmpty(pw.getPhoto())) {
				String[] img = pw.getPhoto().split(",");
				for (int i = 0; i < img.length; i++) {
					dObj.put("photo"+(i+1),img[i]);// 照片
				}
			}
			json = dObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		JLog.d(TAG, "json:"+json);
		return json;
	}
	
	private void intentData(){
		Intent intent = getIntent();
		userId = intent.getIntExtra("userId", 0);
		userName = intent.getStringExtra("userName");
	}
	
	
	@Override
	public void initData() {
		replyDB = new ReplyDB(this);
		topicDB = new TopicDB(this);
		if (!TextUtils.isEmpty(userName)) {
			tv_topic_name.setText(userName);
		}
		String label = DateUtils.formatDateTime(this.getApplicationContext(),
				System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
						| DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_ABBREV_ALL);
		lv_exchange_chat.getLoadingLayoutProxy().setLastUpdatedLabel(label);
		lv_exchange_chat.setMode(Mode.BOTH);
		lv_exchange_chat.setOnRefreshListener(new OnRefreshListener<ListView>() {
					@Override
					public void onRefresh(PullToRefreshBase<ListView> refreshView) {
						// 向下滑动
						if (refreshView.getCurrentMode() == Mode.PULL_FROM_END) {
							loadType = "on";
						}
						// 向上加载
						if (refreshView.getCurrentMode() == Mode.PULL_FROM_START) {
							loadType = "off";
						}
						search();
					}
			});
	}
	

	private void getData() {
		personalWechatList = personalWechatDB.findPersonalWechatList(userId, SharedPreferencesUtil.getInstance(this).getUserId());
		pListToRlist(personalWechatList);
		exchangeAdapter = new ExchangeAdapter(this, replyListAll);
		exchangeAdapter.setListener(this);
		lv_exchange_chat.setAdapter(exchangeAdapter);
		scrollToBottom();
		search();
	}
	
	private void search() {
		String url = UrlInfo.weChatPrivateInfo(this);// 回帖信息查询,向下刷新
		GcgHttpClient.getInstance(this).post(url, paramsSearch(),
				new HttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						JLog.d(TAG, "content:" + content);
						try {
							JSONObject obj = new JSONObject(content);
							String resultcode = obj.getString("resultcode");
//							String replies = obj.getString("replies");
							if ("0000".equals(resultcode)) {
								
							}else{
								throw new Exception();
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onStart() {
						
					}

					@Override
					public void onFinish() {
						lv_exchange_chat.onRefreshComplete();
					}

					@Override
					public void onFailure(Throwable error, String content) {
					}
				});

	};
	
	private RequestParams paramsSearch() {
		RequestParams params = new RequestParams();
		params.put("phoneno", PublicUtils.receivePhoneNO(this));
//		params.put("repliesId", replyLastId);
//		params.put("topicId", topicId);
		params.put("loadType", loadType);// 取得之后的五条数据
//		JLog.d(TAG, "params:" + params.toString());
		return params;
	}
	
	
	private RequestParams submitParams() {
		RequestParams params = new RequestParams();
		params.put("phoneno", PublicUtils.receivePhoneNO(this));
		params.put("data",msgJson(submitPW) );
		return params;
	}
	
	
	/**
	 * 私聊信息转化为回帖信息
	 * @param pw
	 * @return
	 */
	private Reply personalWechatToReply(PersonalWechat pw){
		Reply reply = new Reply();
		reply.setPrivate(true);
		reply.setAttachment(pw.getAttachment());
		reply.setContent(pw.getContent());
		reply.setDate(pw.getDate());
		reply.setUserId(pw.getsUserId());
		reply.setPhoto(pw.getPhoto());
		return reply;
	}
	
	private List<Reply> pListToRlist(List<PersonalWechat> pList){
		if (!pList.isEmpty()) {
			for (int i = 0; i < pList.size(); i++) {
				PersonalWechat pw = pList.get(i);
				Reply r  =personalWechatToReply(pw);
				replyListAll.add(r);
			}
		}
		return replyListAll;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			unregisterReceiver(wechat_broadcast_receiver);
		} catch (Exception e) {
		
		}
	}
}
