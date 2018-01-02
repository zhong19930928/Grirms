package com.yunhu.yhshxc.wechat.view;

import java.text.ParseException;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.wechat.Util.SharedPrefrencesForWechatUtil;
import com.yunhu.yhshxc.wechat.bo.PersonalWechat;
import com.yunhu.yhshxc.wechat.bo.Topic;
import com.yunhu.yhshxc.wechat.db.PersonalWechatDB;
import com.yunhu.yhshxc.wechat.db.ReplyDB;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TopictView {
	private LinearLayout view;
	private Context context;
	private TextView tv_number;
	private TextView tv_topic_item;
	private TextView tv_time_item;
	private TextView tv_content_item;
	private ImageView iv_header;
	private RelativeLayout re_parent;
	private TextView tv_parent;
	private PersonalWechatDB personalWechatDB;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;

	public TopictView(Context context) {
		this.context = context;
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.people_65)
		.showImageForEmptyUri(R.drawable.people_65)
		.showImageOnFail(R.drawable.people_65)
		.cacheInMemory()
		.cacheOnDisc().displayer(new RoundedBitmapDisplayer(10))
		.build();
		view = (LinearLayout) View.inflate(context, R.layout.wechat_chat_topic,
				null);
		personalWechatDB = new PersonalWechatDB(context);
		tv_number = (TextView) view.findViewById(R.id.tv_number);
		tv_topic_item = (TextView) view.findViewById(R.id.tv_topic_item);
		tv_time_item = (TextView) view.findViewById(R.id.tv_time_item);
		tv_content_item = (TextView) view.findViewById(R.id.tv_content_item);
		iv_header = (ImageView) view.findViewById(R.id.iv_header);
		tv_parent = (TextView) view.findViewById(R.id.tv_parent);
		re_parent = (RelativeLayout) view.findViewById(R.id.re_parent);
	}

	public View getView() {
		return view;
	}

	public void initData(Topic topic, ReplyDB replyDb) throws ParseException {
//		if(isLast){
//			tv_parent.setVisibility(View.VISIBLE);
//			re_parent.setVisibility(View.GONE);
//		}else{
			tv_parent.setVisibility(View.GONE);
			re_parent.setVisibility(View.VISIBLE);
		if (topic != null) {
			tv_topic_item.setText(topic.getTitle());// 话题名称
			String time = "";
			String content = "";
			int count = 0;
			count = replyDb.findTopicReplyNum(topic.getTopicId());
			if (count > 0) {
				tv_number.setVisibility(View.VISIBLE);
				tv_number.setText(String.valueOf(count));
			} else {
				tv_number.setVisibility(View.GONE);
			}
			// Reply replys = replyDb.findNewReply(topic.getTopicId());
			// if (replys != null) {
			// content = replys.getContent();
			// time = PublicUtils.wechatCompareDate(replys.getDate());
			// name = replys.getReplyName();
			// }
			if(!TextUtils.isEmpty(topic.getRecentTime())){
				time = PublicUtils.compareDate(topic.getRecentTime());
			}
			content = topic.getRecentContent();
			tv_content_item.setText(content);// 最后一条发言人及内容

			tv_time_item.setText(time);
//		}
		}

	}
	public void initDataHistory(Topic topic, ReplyDB replyDb) throws ParseException {
			tv_parent.setVisibility(View.GONE);
			re_parent.setVisibility(View.VISIBLE);
			if (topic != null) {
				tv_topic_item.setText(topic.getTitle());// 话题名称
				String time = "";
				String content = "";
				tv_number.setVisibility(View.GONE);
				// Reply replys = replyDb.findNewReply(topic.getTopicId());
				// if (replys != null) {
				// content = replys.getContent();
				// time = PublicUtils.wechatCompareDate(replys.getDate());
				// name = replys.getReplyName();
				// }
				if(!TextUtils.isEmpty(topic.getRecentTime())){
					time = PublicUtils.compareDate(topic.getRecentTime());
				}
				content = topic.getRecentContent();
				tv_content_item.setText(content);// 最后一条发言人及内容
				
				tv_time_item.setText(time);
		}
		
	}
	
	
	public void initPerson(PersonalWechat person) throws ParseException{
		
			if(person !=null){
				int sUserId = person.getsUserId();
				if (sUserId == SharedPreferencesUtil.getInstance(context).getUserId()) {
					tv_topic_item.setText(person.getdUserName());
				}else{
					tv_topic_item.setText(person.getsUserName());
				}
				String time = "";
				String content = "";
				if(!TextUtils.isEmpty(person.getDate())){
					time = PublicUtils.compareDate(person.getDate());
					tv_time_item.setText(time);
				}else{
					tv_content_item.setText("");
				}
				content = person.getContent();
				int count = 0;
				count = personalWechatDB.findPersonalWechatCount(person.getsUserId(), person.getdUserId());
				if (count > 0) {
					tv_number.setVisibility(View.VISIBLE);
					tv_number.setText(String.valueOf(count));
				} else {
					tv_number.setVisibility(View.GONE);
				}
				tv_content_item.setText(content);
				int suserId = person.getsUserId();
				if (suserId == SharedPreferencesUtil.getInstance(context).getUserId()) {
					String headUrl = SharedPrefrencesForWechatUtil.getInstance(context).getUserHeadImg(String.valueOf(person.getdUserId()));
					if(!TextUtils.isEmpty(headUrl)){
						imageLoader.displayImage(headUrl, iv_header, options, null);
					}else{
						iv_header.setImageResource(R.drawable.people_65);
					}
				}else{
					String headUrl = SharedPrefrencesForWechatUtil.getInstance(context).getUserHeadImg(String.valueOf(person.getsUserId()));
					if(!TextUtils.isEmpty(headUrl)){
						imageLoader.displayImage(headUrl, iv_header, options, null);
					}else{
						iv_header.setImageResource(R.drawable.people_65);
					}
				}
		}
		
	}

	public ImageView getImageView() {
		return iv_header;
	}

	public TextView getTextView() {
		return tv_number;
	}
}
