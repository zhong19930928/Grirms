package com.yunhu.yhshxc.wechat.fragments;

import gcg.org.debug.JLog;

import java.text.ParseException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.wechat.approval.ApprovalActivity;
import com.yunhu.yhshxc.wechat.bo.Group;
import com.yunhu.yhshxc.wechat.bo.Notification;
import com.yunhu.yhshxc.wechat.bo.Topic;
import com.yunhu.yhshxc.wechat.db.GroupDB;
import com.yunhu.yhshxc.wechat.db.NotificationDB;
import com.yunhu.yhshxc.wechat.db.ReplyDB;
import com.yunhu.yhshxc.wechat.db.TopicDB;
import com.yunhu.yhshxc.wechat.exchange.ExchangeActivity;
import com.yunhu.yhshxc.wechat.survey.SurveyActivity;
import com.yunhu.yhshxc.wechat.view.NoticeView;
import com.yunhu.yhshxc.wechat.view.SlideView;
import com.yunhu.yhshxc.wechat.view.TopictView;
import com.yunhu.yhshxc.wechat.view.WeChatListView;
import com.yunhu.yhshxc.wechat.view.SlideView.ExpendListViewOnlick;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class FocusActivity extends AbsBaseActivity {
	
	private TopAdapter topictAdapter;
	private NotiAdapter notiAdapter;
	private WeChatListView lv_topic;
	private WeChatListView lv_notice;
	private TopicDB topicDB;
	private List<Topic> topicList ;
	private List<Notification> noticeList ;
	private NotificationDB notificationDB;
	private TextView tv_topic;
	private TextView tv_notice;
	private TextView tv_focus_return;
	private GroupDB groupDB;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wechat_focus);
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.wechat_moren_group_header)
		.showImageForEmptyUri(R.drawable.wechat_moren_group_header)
		.showImageOnFail(R.drawable.wechat_moren_group_header)
		.cacheInMemory()
		.cacheOnDisc().displayer(new RoundedBitmapDisplayer(10))
		.build();
		topicDB= new TopicDB(this);
		groupDB = new GroupDB(this);
		notificationDB= new NotificationDB(this);
		noticeList = notificationDB.findNotifacationListByIsNoticed();
		topicList = topicDB.findNotifacationListByIsTopic(1);
		initView();
	}

	private void initView() {
		lv_topic = (WeChatListView) findViewById(R.id.lv_topic);		
		lv_notice = (WeChatListView)findViewById(R.id.lv_notice);	
		tv_topic = (TextView) findViewById(R.id.tv_topic);
		tv_notice = (TextView) findViewById(R.id.tv_notice);
		tv_focus_return = (TextView) findViewById(R.id.tv_focus_return);
		tv_topic.setOnClickListener(listener);
		tv_notice.setOnClickListener(listener);
		tv_focus_return.setOnClickListener(listener);
		lv_notice.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Notification no = noticeList.get(position);
				if(no!=null){
					Intent intent = new Intent(FocusActivity.this,WechatNoticeActivity.class);
					intent.putExtra("noticeId", no.getNoticeId());
					startActivity(intent);
				}
				
			}
		});
		lv_topic.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Topic topic = topicList.get(position);
				int isHistory = 0;
				if(topic!=null){
					if(topic.getType().equals(Topic.TYPE_1)){
						Intent intent = new Intent(FocusActivity.this,ExchangeActivity.class);
						intent.putExtra("topicId", topic.getTopicId());
						intent.putExtra("isHistory", isHistory);
						startActivity(intent);
					}else if(topic.getType().equals(Topic.TYPE_2)){
						Intent intent = new Intent(FocusActivity.this,SurveyActivity.class);
						intent.putExtra("topicId", topic.getTopicId());
						intent.putExtra("isHistory", isHistory);
						startActivity(intent);
					}else if(topic.getType().equals(Topic.TYPE_3)){
						Intent intent = new Intent(FocusActivity.this,ApprovalActivity.class);
						intent.putExtra("topicId", topic.getTopicId());
						intent.putExtra("isHistory", isHistory);
						startActivity(intent);
					}
				}
			}
		});
		topictAdapter = new TopAdapter(FocusActivity.this,topicList,new ExpendListViewOnlick() {
			
			@Override
			public void expendOnclick(int groupPosition, int childPosition) {
				//取消关注话题
				Topic topic = topicList.get(childPosition);
				if(topic!=null){
					deleteTopic(topic);
				}
			}
		});
		notiAdapter = new NotiAdapter(FocusActivity.this,noticeList,new ExpendListViewOnlick() {
			
			@Override
			public void expendOnclick(int groupPosition, int childPosition) {
				//取消收藏公告
				Notification no = noticeList.get(childPosition);
				if(no!=null){
					deleteNoticed(no);
				}
				
			}

		});
		lv_topic.setAdapter(topictAdapter);
		lv_notice.setAdapter(notiAdapter);
	}
	private void deleteTopic(final Topic topic) {
		String url = UrlInfo.doWeChatFollowInfo(FocusActivity.this);
		GcgHttpClient.getInstance(FocusActivity.this).post(url, params(topic),
				new HttpResponseListener() {

					@Override
					public void onSuccess(int statusCode, String content) {
						JLog.d("alin", "content:" + content);
						try {
							JSONObject obj = new JSONObject(content);
							String resultcode = obj.getString("resultcode");
							if ("0000".equals(resultcode)) {
								topic.setIsAttention(0);
								topicDB.updateTopic(topic);
								topicList = topicDB.findNotifacationListByIsTopic(1);
								refreshTopicList(topicList);
							} else {
								throw new Exception();
							}
						} catch (Exception e) {
							// searchHandler.sendEmptyMessage(3);
							ToastOrder.makeText(FocusActivity.this, R.string.wechat_content20,
									ToastOrder.LENGTH_LONG).show();
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
						JLog.d("alin", "failure");
						ToastOrder.makeText(FocusActivity.this, R.string.wechat_content20,
								ToastOrder.LENGTH_LONG).show();
					}
				});
	}

	private RequestParams params(Topic topic)  {
		RequestParams params = new RequestParams();
		String json = "";
		JSONObject dObject = new JSONObject();
		try {
			dObject.put("followId", topic.getTopicId());
			dObject.put("type", "1");
			dObject.put("action", "d");//取消关注
		} catch (JSONException e) {
			e.printStackTrace();
		}
		json = dObject.toString();
		params.put("data", json);
		JLog.d("alin", "params:" + params.toString());
		return params;
	}

	private void deleteNoticed(final Notification no) {
		String url = UrlInfo.doWeChatFollowInfo(FocusActivity.this);
		GcgHttpClient.getInstance(FocusActivity.this).post(url, params(no),
				new HttpResponseListener() {

					@Override
					public void onSuccess(int statusCode, String content) {
						JLog.d("alin", "content:" + content);
						try {
							JSONObject obj = new JSONObject(content);
							String resultcode = obj.getString("resultcode");
							if ("0000".equals(resultcode)) {
								no.setIsNoticed("0");
								notificationDB.updateNotification(no);
								noticeList = notificationDB.findNotifacationListByIsNoticed();
								refreshNoticeList(noticeList);
							} else {
								throw new Exception();
							}
						} catch (Exception e) {
							// searchHandler.sendEmptyMessage(3);
							ToastOrder.makeText(FocusActivity.this, R.string.wechat_content20,
									ToastOrder.LENGTH_LONG).show();
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
						JLog.d("alin", "failure");
						ToastOrder.makeText(FocusActivity.this, R.string.wechat_content20,
								ToastOrder.LENGTH_LONG).show();
					}
				});
	}

	private RequestParams params(Notification notification)  {
		RequestParams params = new RequestParams();
		String json = "";
		JSONObject dObject = new JSONObject();
		try {
			dObject.put("followId", notification.getNoticeId());
			dObject.put("type", "2");
			dObject.put("action", "d");//取消关注
		} catch (JSONException e) {
			e.printStackTrace();
		}
		json = dObject.toString();
		params.put("data", json);
		JLog.d("alin", "params:" + params.toString());
		return params;
	}

	class TopAdapter extends BaseAdapter{
		private Context context;
		private List<Topic> topList;
		 private ReplyDB replyDb;
		 private ExpendListViewOnlick mExpendListViewOnlick;
			
		public TopAdapter(Context context,List<Topic> topList,ExpendListViewOnlick mExpendListViewOnlick){
			this.context = context;
			this.topList = topList;
			this.mExpendListViewOnlick = mExpendListViewOnlick;
			replyDb = new ReplyDB(context);
		}
		@Override
		public int getCount() {
			return topList.size();
//			return 10;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TopictView view = null;
			if (convertView == null) {
				view = new TopictView(context);
				convertView = new SlideView(context,
						context.getResources(), view.getView(), true,
						mExpendListViewOnlick);
				convertView.setTag(view);
			} else {
				view = (TopictView) convertView.getTag();
			}
			((SlideView) convertView).getBack().setText(R.string.wechat_content32);
			((SlideView) convertView).setPostion(0,position);
			Topic topic = topicDB.findTopicById(topList.get(position).getTopicId());
			if(topic!=null){
				try {
					view.initData(topic, replyDb);
					Group group = groupDB.findGroupByGroupId(topic.getGroupId());
					if(group!=null){
						if(!TextUtils.isEmpty(group.getLogo())){
							JLog.d("aaa", "url = "+group.getLogo());
								imageLoader.displayImage(group.getLogo(), view.getImageView(), options, null);
						}
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
			}
			return convertView;
		}
		public void refresh(List<Topic> topic){
			this.topList = topic;
			notifyDataSetChanged();
		}
		
	}
	class NotiAdapter extends BaseAdapter{
		private Context context;
		private List<Notification> notList;
		 private ExpendListViewOnlick mExpendListViewOnlick;
		public NotiAdapter(Context context,List<Notification> notList,  ExpendListViewOnlick mExpendListViewOnlick){
			this.context = context;
			this.notList = notList;
			this.mExpendListViewOnlick = mExpendListViewOnlick;
		}
		@Override
		public int getCount() {
			return notList.size();
//			return 20;
		}
		
		@Override
		public Object getItem(int position) {
			return null;
		}
		
		@Override
		public long getItemId(int position) {
			return 0;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			NoticeView view = null;
			if (convertView == null) {
				view = new NoticeView(context);
				convertView = new SlideView(context,
						context.getResources(), view.getView(), true,
						mExpendListViewOnlick);
				convertView.setTag(view);
			} else {
				view = (NoticeView) convertView.getTag();
			}
			((SlideView) convertView).getBack().setText(R.string.wechat_content32);
			((SlideView) convertView).setPostion(0,position);
			try {
				view.initData(notList.get(position));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return convertView;
		}
		public void refresh(List<Notification> topic){
			this.notList = topic;
			notifyDataSetChanged();
		}
		
	}
	private OnClickListener listener = new OnClickListener() {
		
		@SuppressLint({ "NewApi", "ResourceAsColor" })
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_topic:
				lv_notice.setVisibility(View.GONE);
				lv_topic.setVisibility(View.VISIBLE);
				if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
					tv_topic.setBackground(FocusActivity.this.getResources().getDrawable(R.color.near_by_search_btn));
					tv_notice.setBackground(FocusActivity.this.getResources().getDrawable(R.color.white));
				}else{
					tv_topic.setBackgroundDrawable(FocusActivity.this.getResources().getDrawable(R.color.near_by_search_btn));
					tv_notice.setBackgroundDrawable(FocusActivity.this.getResources().getDrawable(R.color.white));
				}
				
				Resources resource = (Resources) FocusActivity.this.getBaseContext().getResources();  
				ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.white);  
				if (csl != null) {  
					tv_topic.setTextColor(csl);  
				}  
				ColorStateList csl1 = (ColorStateList) resource.getColorStateList(R.color.wechat_chat_item);  
				if (csl1 != null) {  
					tv_notice.setTextColor(csl1);  
				}  
				break;
			case R.id.tv_notice:
				lv_notice.setVisibility(View.VISIBLE);
				lv_topic.setVisibility(View.GONE);
				if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
					tv_notice.setBackground(FocusActivity.this.getResources().getDrawable(R.color.near_by_search_btn));
					tv_topic.setBackground(FocusActivity.this.getResources().getDrawable(R.color.white));
				}else{
					tv_notice.setBackgroundDrawable(FocusActivity.this.getResources().getDrawable(R.color.near_by_search_btn));
					tv_topic.setBackgroundDrawable(FocusActivity.this.getResources().getDrawable(R.color.white));
				}
				
				Resources resource1 = (Resources) FocusActivity.this.getBaseContext().getResources(); 
				ColorStateList csl13 = (ColorStateList) resource1.getColorStateList(R.color.white);  
				if (csl13 != null) {  
					tv_notice.setTextColor(csl13);  
				}  
				ColorStateList csl12 = (ColorStateList) resource1.getColorStateList(R.color.wechat_chat_item);  
				if (csl12 != null) {  
					tv_topic.setTextColor(csl12);  
				}  
				break;
			case R.id.tv_focus_return:
				FocusActivity.this.finish();
				break;
			default:
				break;
			}
		}
	};
	/**
	 * 刷新通知
	 * @param list
	 */
	public void refreshNoticeList(List<Notification> list){
		this.noticeList = list;
		notiAdapter.refresh(noticeList);
	}
	/**
	 * 刷新话题
	 * @param list
	 */
	public void refreshTopicList(List<Topic> list){
		this.topicList = list;
		topictAdapter.refresh(topicList);
	}
	 @Override 
	    public boolean onKeyDown(int keyCode, KeyEvent event) { 
	        if ((keyCode == KeyEvent.KEYCODE_BACK)) { 
	             FocusActivity.this.finish();
	        } 
	        return super.onKeyDown(keyCode, event); 
	           
	    } 
	 @Override
	protected void onStart() {
		super.onStart();
		noticeList = notificationDB.findNotifacationListByIsNoticed();
		topicList = topicDB.findNotifacationListByIsTopic(1);
		refreshNoticeList(noticeList);
		refreshTopicList(topicList);
	}
}
