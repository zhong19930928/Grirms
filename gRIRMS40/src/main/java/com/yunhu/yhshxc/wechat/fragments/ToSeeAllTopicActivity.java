package com.yunhu.yhshxc.wechat.fragments;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.wechat.approval.ApprovalActivity;
import com.yunhu.yhshxc.wechat.bo.Topic;
import com.yunhu.yhshxc.wechat.db.TopicDB;
import com.yunhu.yhshxc.wechat.exchange.ExchangeActivity;
import com.yunhu.yhshxc.wechat.survey.SurveyActivity;
import com.yunhu.yhshxc.wechat.view.TopictView;

public class ToSeeAllTopicActivity extends AbsBaseActivity {
	private ListView lv_to_see_topic;
	private AllTopicAdapter adapter;
	private List<Topic> allList;
	private TopicDB topicDB;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wechat_to_see_all_topic);
		topicDB = new TopicDB(this);
		allList = topicDB.findTopicListByIsClosed(DateUtil.getCurDate(),0);
		lv_to_see_topic = (ListView) findViewById(R.id.lv_to_see_topic);
		lv_to_see_topic.setOnItemClickListener(listener);
		adapter = new AllTopicAdapter(this, allList);
		lv_to_see_topic.setAdapter(adapter);
	}
	private OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Topic topic = allList.get(position);
			if(topic!=null){
				int isHistory = 0;
				if(topic.getType().equals(Topic.TYPE_1)){
					Intent intent = new Intent(ToSeeAllTopicActivity.this,ExchangeActivity.class);
					intent.putExtra("topicId", topic.getTopicId());
					intent.putExtra("isHistory", isHistory);
					startActivity(intent);
				}else if(topic.getType().equals(Topic.TYPE_2)){
					Intent intent = new Intent(ToSeeAllTopicActivity.this,SurveyActivity.class);
					intent.putExtra("topicId", topic.getTopicId());
					intent.putExtra("isHistory", isHistory);
					startActivity(intent);
				}else if(topic.getType().equals(Topic.TYPE_3)){
					Intent intent = new Intent(ToSeeAllTopicActivity.this,ApprovalActivity.class);
					intent.putExtra("topicId", topic.getTopicId());
					intent.putExtra("isHistory", isHistory);
					startActivity(intent);
				}
			}
		}
	};
	class AllTopicAdapter extends BaseAdapter{
		private Context context;
		private List<Topic> topList;
		
		public AllTopicAdapter(Context context,List<Topic> topList){
			this.context = context;
			this.topList = topList;
		}
		
		@Override
		public int getCount() {
			return topList.size();
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
			if(convertView == null){
				view = new TopictView(context);
				convertView = view.getView();
				convertView.setTag(view);
			}else{
				view = (TopictView) convertView.getTag();
			}
			return convertView;
		}
		
	}
}
