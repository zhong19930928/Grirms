package com.yunhu.yhshxc.wechat;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.wechat.bo.Notification;
import com.yunhu.yhshxc.wechat.bo.PersonalWechat;
import com.yunhu.yhshxc.wechat.bo.Topic;
import com.yunhu.yhshxc.wechat.db.NotificationDB;
import com.yunhu.yhshxc.wechat.db.PersonalWechatDB;
import com.yunhu.yhshxc.wechat.db.ReplyDB;
import com.yunhu.yhshxc.wechat.db.TopicDB;
import com.yunhu.yhshxc.wechat.fragments.ChatFragment;
import com.yunhu.yhshxc.wechat.fragments.FocusFragment;
import com.yunhu.yhshxc.wechat.fragments.NoticeFragment;
import com.yunhu.yhshxc.wechat.fragments.PersonalFragment;

@SuppressLint("NewApi")
public class WechatActivity extends FragmentActivity {
	
	private int current = 0;
	// 定义FragmentManager对象
	private FragmentManager fManager;
	private ChatFragment chatFragment;
	private FocusFragment focusFragment;
	private PersonalFragment personalFragment;
	private NoticeFragment noticeFragment;
//	private LinearLayout layout_chat;
//	private LinearLayout layout_notice;
//	private LinearLayout layout_focus;
//	private LinearLayout layout_personal;
	private ImageView img_chat;
	private ImageView img_notice;
	private ImageView img_focus;
	private ImageView img_personal;
	private TextView tv_chat;
	private TextView tv_notice;
	private TextView tv_focus;
	private TextView tv_personal;
	private TextView tv_shopping_car_ll_number;
	private TextView tv_notice_ll_number;
	
	private FrameLayout fl_huiliao;
	private FrameLayout fl_notice;
	private TopicDB topicDb;
	private NotificationDB notificationDB;
	private ReplyDB replyDB;
	private PersonalWechatDB personalWechatDB ;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		topicDb = new TopicDB(this);
		topicDb.findTopicListForOverDue(DateUtil.getCurDate(), 0);
		notificationDB= new NotificationDB(this);
		replyDB = new ReplyDB(this);
		personalWechatDB = new PersonalWechatDB(this);
		setContentView(R.layout.wechat_main);
		registSubmitSuccessReceiver();
		registSubmitNoticeSuccessReceiver();
//		registSubmitFocusSuccessReceiver();
		registSilisaoReceiver();
		initAllViews();
	}

	private void initAllViews() {
		fManager = getSupportFragmentManager();
//		layout_chat = (LinearLayout) findViewById(R.id.layout_chat);
//		layout_notice = (LinearLayout) findViewById(R.id.layout_notice);
//		layout_focus = (LinearLayout) findViewById(R.id.layout_focus);
//		layout_personal = (LinearLayout) findViewById(R.id.layout_personal);
		img_chat = (ImageView) findViewById(R.id.img_chat);
		img_notice = (ImageView) findViewById(R.id.img_notice);
		img_focus = (ImageView) findViewById(R.id.img_focus);
		img_personal = (ImageView) findViewById(R.id.img_personal);
		tv_chat = (TextView) findViewById(R.id.tv_chat);
		tv_notice = (TextView) findViewById(R.id.tv_notice);
		tv_focus = (TextView) findViewById(R.id.tv_focus);
		tv_personal = (TextView) findViewById(R.id.tv_personal);
		tv_shopping_car_ll_number = (TextView) findViewById(R.id.tv_shopping_car_ll_number);
		tv_notice_ll_number = (TextView) findViewById(R.id.tv_notice_ll_number);
		fl_huiliao = (FrameLayout) findViewById(R.id.fl_huiliao);
		fl_notice = (FrameLayout) findViewById(R.id.fl_notice);
		initFragments();
	}
	private void initFragments() {
		setChioceItem(0);
		initData();
	}

	private void initData() {
		
	}

	private void setChioceItem(int i) {
		FragmentTransaction transaction = fManager.beginTransaction();
		clearChoice();
		hideFragments(transaction);
		switch (i) {
		case 0:
			if (chatFragment == null) {
				chatFragment = new ChatFragment();
				transaction.add(R.id.framelayout, chatFragment);
			} else {
				// 如果MessageFragment不为空，则直接将它显示出来
				transaction.show(chatFragment);
			}
			current = 0;
			transaction.commit();
			img_chat.setImageDrawable(getResources().getDrawable(R.drawable.wechat_huiliao_press));
			img_notice.setImageDrawable(getResources().getDrawable(R.drawable.wechat_notice));
			img_focus.setImageDrawable(getResources().getDrawable(R.drawable.wechat_contact));
			img_personal.setImageDrawable(getResources().getDrawable(R.drawable.wechat_my));
			tv_chat.setTextColor(getResources().getColor(R.color.wechat_location_bar_press));
			tv_notice.setTextColor(getResources().getColor(R.color.wechat_location_bar));
			tv_focus.setTextColor(getResources().getColor(R.color.wechat_location_bar));
			tv_personal.setTextColor(getResources().getColor(R.color.wechat_location_bar));
			break;
		case 1:
			if (noticeFragment == null) {
				noticeFragment = new NoticeFragment();
				transaction.add(R.id.framelayout, noticeFragment);
			} else {
				transaction.show(noticeFragment);
			}
			current = 1;
			transaction.commit();
			initData();
//			layout_notice.setBackgroundResource(R.color.order3_locationbar_pressed);
			img_notice.setImageDrawable(getResources().getDrawable(R.drawable.wechat_notice_press));
			img_focus.setImageDrawable(getResources().getDrawable(R.drawable.wechat_contact));
			img_personal.setImageDrawable(getResources().getDrawable(R.drawable.wechat_my));
			img_chat.setImageDrawable(getResources().getDrawable(R.drawable.wechat_huiliao));
			tv_chat.setTextColor(getResources().getColor(R.color.wechat_location_bar));
			tv_notice.setTextColor(getResources().getColor(R.color.wechat_location_bar_press));
			tv_focus.setTextColor(getResources().getColor(R.color.wechat_location_bar));
			tv_personal.setTextColor(getResources().getColor(R.color.wechat_location_bar));
			break;
		case 2:
			if (focusFragment == null) {
				focusFragment = new FocusFragment();
				transaction.add(R.id.framelayout, focusFragment);
			} else {
//				focusFragment.initData();
				transaction.show(focusFragment);
			}
			current = 2;
			transaction.commit();
			initData();
//			layout_focus.setBackgroundResource(R.color.order3_locationbar_pressed);
			img_focus.setImageDrawable(getResources().getDrawable(R.drawable.wechat_contracted));
			img_notice.setImageDrawable(getResources().getDrawable(R.drawable.wechat_notice));
			img_personal.setImageDrawable(getResources().getDrawable(R.drawable.wechat_my));
			img_chat.setImageDrawable(getResources().getDrawable(R.drawable.wechat_huiliao));
			tv_chat.setTextColor(getResources().getColor(R.color.wechat_location_bar));
			tv_notice.setTextColor(getResources().getColor(R.color.wechat_location_bar));
			tv_focus.setTextColor(getResources().getColor(R.color.wechat_location_bar_press));
			tv_personal.setTextColor(getResources().getColor(R.color.wechat_location_bar));
			break;
		case 3:
			if (personalFragment == null) {
				personalFragment = new PersonalFragment();
				transaction.add(R.id.framelayout, personalFragment);
			} else {
				transaction.show(personalFragment);
//				yuLanFragment.initData();
			}
			current = 3;
			transaction.commit();
			initData();
//			layout_personal.setBackgroundResource(R.color.order3_locationbar_pressed);
			img_personal.setImageDrawable(getResources().getDrawable(R.drawable.wechat_my_press));
			img_focus.setImageDrawable(getResources().getDrawable(R.drawable.wechat_contact));
			img_notice.setImageDrawable(getResources().getDrawable(R.drawable.wechat_notice));
			img_chat.setImageDrawable(getResources().getDrawable(R.drawable.wechat_huiliao));
			tv_chat.setTextColor(getResources().getColor(R.color.wechat_location_bar));
			tv_notice.setTextColor(getResources().getColor(R.color.wechat_location_bar));
			tv_focus.setTextColor(getResources().getColor(R.color.wechat_location_bar));
			tv_personal.setTextColor(getResources().getColor(R.color.wechat_location_bar_press));
			break;
		default:
			break;
		}
	}
	private void clearChoice() {
		
	}

	private void hideFragments(FragmentTransaction transaction) {
		if (chatFragment != null) {
			transaction.hide(chatFragment);
		}
		if (noticeFragment != null) {
			transaction.hide(noticeFragment);
		}
		if (focusFragment != null) {
			transaction.hide(focusFragment);
		}
		if (personalFragment != null) {
			transaction.hide(personalFragment);
		}
	}
	public void LayoutOnclickMethod(View layout) {
		switch (layout.getId()) {
		case R.id.fl_huiliao://汇聊
			setChioceItem(0);
			break;
		case R.id.fl_notice://通知
			setChioceItem(1);
			break;
		case R.id.layout_focus://关注
			setChioceItem(2);
			break;
		case R.id.layout_personal://我的
			setChioceItem(3);
			break;
		default:
			break;
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(wechat_broadcast_receiver);
		unregisterReceiver(wechat_notice_receiver);
//		unregisterReceiver(wechat_focus_receiver);
		unregisterReceiver(wechat_siliao_receriver);
	}
	private void registSubmitSuccessReceiver(){
		IntentFilter filter = new IntentFilter(Constants.BROADCAST_WECHAT_HUILIAO);
		registerReceiver(wechat_broadcast_receiver, filter);
	}
	private void registSubmitNoticeSuccessReceiver(){
		IntentFilter filter = new IntentFilter(Constants.BROADCAST_WECHAT_NOTICE);
		registerReceiver(wechat_notice_receiver, filter);
	}
//	private void registSubmitFocusSuccessReceiver(){
//		IntentFilter filter = new IntentFilter(Constants.BROADCAST_WECHAT_FOCUS);
//		registerReceiver(wechat_focus_receiver, filter);
//	}
	private void registSilisaoReceiver(){
		IntentFilter filter = new IntentFilter(Constants.BROADCAST_WECHAT_PERSON);
		registerReceiver(wechat_siliao_receriver, filter);
	}
	/**
	 * 刷新汇聊界面广播
	 */
	private BroadcastReceiver wechat_broadcast_receiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent!=null && Constants.BROADCAST_WECHAT_HUILIAO.equals(intent.getAction())) {
				refreshHuiLiao();
			}
		}
		
	};
	/**
	 * 刷新通知界面广播
	 */
	private BroadcastReceiver wechat_notice_receiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent != null&&Constants.BROADCAST_WECHAT_NOTICE.equals(intent.getAction())){
				refreshNoticeFragment();
				
			}
		}

	};
	/**
	 * 刷新汇聊私聊
	 */
	private BroadcastReceiver wechat_siliao_receriver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent!=null && Constants.BROADCAST_WECHAT_PERSON.equals(intent.getAction())) {
				refreshSiliao();
			}
		}

	};


	private void refreshSiliao() {
		List<PersonalWechat> list = personalWechatDB.findPersonalWechat();
		if(chatFragment!=null){
			chatFragment.refreshSL(list);
			int count = replyDB.findAllRepayNum(0)+personalWechatDB.findAllPersonalWechatCount();
			 if(count>0){
				 tv_shopping_car_ll_number.setVisibility(View.VISIBLE);
				 tv_shopping_car_ll_number.setText(String.valueOf(count));
			 }else{
				 tv_shopping_car_ll_number.setVisibility(View.GONE);
			 }
		}
	}
	
	private void refreshNoticeFragment() {
		List<Notification> notifications = new ArrayList<Notification>();
		notifications = notificationDB.findNotificationList();
		if(noticeFragment!=null){
			noticeFragment.refresh(notifications);
			
		}
		int count = notificationDB.findAllNoticeNum();
		 if(count>0){
			 tv_notice_ll_number.setVisibility(View.VISIBLE);
			 tv_notice_ll_number.setText(String.valueOf(count));
		 }else{
			 tv_notice_ll_number.setVisibility(View.GONE);
		 }
	}
//	/**
//	 * 刷新关注界面广播
//	 */
//	private BroadcastReceiver wechat_focus_receiver = new BroadcastReceiver() {
//		
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			if(intent !=null &&Constants.BROADCAST_WECHAT_FOCUS.equals(intent.getAction())){
//				 List<Topic> topicList = topicDb.findNotifacationListByIsTopic(1);
//				 List<Notification> noticeList = notificationDB.findNotifacationListByIsNoticed();
//				 if(focusFragment!=null){
//					 focusFragment.refreshNoticeList(noticeList);
//					 focusFragment.refreshTopicList(topicList);
//				 }
//			}
//		}
//	};
	private void refreshHuiLiao(){
		 List<Topic> pubTopics  = topicDb.findTopicListForPublic(DateUtil.getCurDate(),0);//公开话题
		 List<Topic> bumenTopics =topicDb.findTopicListForBuM(DateUtil.getCurDate(),0);//部门交流
		 List<List<Topic>> childrenData  = new ArrayList<List<Topic>>();
		 childrenData.add(pubTopics);
	     childrenData.add(bumenTopics);
		 if(chatFragment!=null){
			 chatFragment.refresh(childrenData,pubTopics,bumenTopics);
		 }
		 int count = replyDB.findAllRepayNum(0)+personalWechatDB.findAllPersonalWechatCount();
		 if(count>0){
			 tv_shopping_car_ll_number.setVisibility(View.VISIBLE);
			 tv_shopping_car_ll_number.setText(String.valueOf(count));
		 }else{
			 tv_shopping_car_ll_number.setVisibility(View.GONE);
		 }
	}
	@Override
	protected void onStart() {
		super.onStart();
		refreshHuiLiao();
		refreshSiliao();
		refreshNoticeFragment();
	}
}
