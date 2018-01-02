package com.yunhu.yhshxc.wechat.fragments;

import gcg.org.debug.JLog;

import java.text.ParseException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.wechat.approval.ApprovalActivity;
import com.yunhu.yhshxc.wechat.bo.Group;
import com.yunhu.yhshxc.wechat.bo.Topic;
import com.yunhu.yhshxc.wechat.db.GroupDB;
import com.yunhu.yhshxc.wechat.db.ReplyDB;
import com.yunhu.yhshxc.wechat.db.TopicDB;
import com.yunhu.yhshxc.wechat.exchange.ExchangeActivity;
import com.yunhu.yhshxc.wechat.survey.SurveyActivity;
import com.yunhu.yhshxc.wechat.view.SlideView;
import com.yunhu.yhshxc.wechat.view.TopictView;
import com.yunhu.yhshxc.wechat.view.WeChatListView;
import com.yunhu.yhshxc.wechat.view.SlideView.ExpendListViewOnlick;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class HistoryTopicActivity extends AbsBaseActivity {
	
	private TopAdapter topictAdapter;
	private WeChatListView lv_topic_history;
	private TopicDB topicDB;
	private List<Topic> topicList ;
	private TextView tv_focus_return;
	private GroupDB groupDB;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wechat_topic_history);
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.wechat_moren_group_header)
		.showImageForEmptyUri(R.drawable.wechat_moren_group_header)
		.showImageOnFail(R.drawable.wechat_moren_group_header)
		.cacheInMemory()
		.cacheOnDisc().displayer(new RoundedBitmapDisplayer(10))
		.build();
		topicDB= new TopicDB(this);
		groupDB = new GroupDB(this);
		topicList = topicDB.findHistoryTopicList(DateUtil.getCurDate(), 1);
		initView();
	}

	private void initView() {
		lv_topic_history = (WeChatListView) findViewById(R.id.lv_topic_history);		
		tv_focus_return = (TextView) findViewById(R.id.tv_focus_return);
		tv_focus_return.setOnClickListener(listener);
		lv_topic_history.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Topic topic = topicList.get(position);
				int isHistory = 1;
				if(topic!=null){
					if(topic.getType().equals(Topic.TYPE_1)){
						Intent intent = new Intent(HistoryTopicActivity.this,ExchangeActivity.class);
						intent.putExtra("topicId", topic.getTopicId());
						intent.putExtra("isHistory", isHistory);
						startActivity(intent);
					}else if(topic.getType().equals(Topic.TYPE_2)){
						Intent intent = new Intent(HistoryTopicActivity.this,SurveyActivity.class);
						intent.putExtra("topicId", topic.getTopicId());
						intent.putExtra("isHistory", isHistory);
						startActivity(intent);
					}else if(topic.getType().equals(Topic.TYPE_3)){
						Intent intent = new Intent(HistoryTopicActivity.this,ApprovalActivity.class);
						intent.putExtra("topicId", topic.getTopicId());
						intent.putExtra("isHistory", isHistory);
						startActivity(intent);
					}
				}
			}
		});
		topictAdapter = new TopAdapter(HistoryTopicActivity.this,topicList,new ExpendListViewOnlick() {
			
			@Override
			public void expendOnclick(int groupPosition, int childPosition) {
				//取消关注话题
				Topic topic = topicList.get(childPosition);
				if(topic!=null){
					submitDelete(topic);
				}
			}
		});
		lv_topic_history.setAdapter(topictAdapter);
	}
	private void submitDelete(final Topic topic) {
		String url = UrlInfo.doWeChatTopicHistoryInfo(HistoryTopicActivity.this);
		GcgHttpClient.getInstance(HistoryTopicActivity.this).post(url, params(topic),
				new HttpResponseListener() {

					@Override
					public void onSuccess(int statusCode, String content) {
						JLog.d("alin", "content:" + content);
						try {
							JSONObject obj = new JSONObject(content);
							String resultcode = obj.getString("resultcode");
							if ("0000".equals(resultcode)) {
								topicDB.deleteTopic(topic.getTopicId());
								topicList = topicDB.findHistoryTopicList(DateUtil.getCurDate(), 1);
								refreshTopicList(topicList);
								ToastOrder.makeText(HistoryTopicActivity.this, R.string.delete_sucsess,
										ToastOrder.LENGTH_LONG).show();
								
							} else {
								throw new Exception();
							}
						} catch (Exception e) {
							// searchHandler.sendEmptyMessage(3);
							ToastOrder.makeText(HistoryTopicActivity.this, R.string.delete_fail,
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
						ToastOrder.makeText(HistoryTopicActivity.this, R.string.delete_fail,
								ToastOrder.LENGTH_LONG).show();
					}
				});
	}

	private RequestParams params(Topic topic)  {
		RequestParams params = new RequestParams();
		String json = "";
		JSONObject dObject = new JSONObject();
		try {
			dObject.put("groupId", topic.getGroupId());
			dObject.put("topicId", topic.getTopicId());
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
			((SlideView) convertView).getBack().setText(R.string.delete);
			((SlideView) convertView).setPostion(0,position);
			Topic topic = topicDB.findTopicById(topList.get(position).getTopicId());
			if(topic!=null){
				try {
					view.initDataHistory(topic, replyDb);
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
	private OnClickListener listener = new OnClickListener() {
		
		@SuppressLint({ "NewApi", "ResourceAsColor" })
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_focus_return:
				HistoryTopicActivity.this.finish();
				break;
			default:
				break;
			}
		}
	};
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
	             HistoryTopicActivity.this.finish();
	        } 
	        return super.onKeyDown(keyCode, event); 
	           
	    } 
}
