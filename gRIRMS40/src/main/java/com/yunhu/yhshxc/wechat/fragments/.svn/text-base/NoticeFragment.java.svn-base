package com.yunhu.yhshxc.wechat.fragments;

import java.util.ArrayList;
import java.util.List;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.wechat.Util.SharedPrefrencesForWechatUtil;
import com.yunhu.yhshxc.wechat.bo.Notification;
import com.yunhu.yhshxc.wechat.db.NotificationDB;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
/**
 * 通知
 * @author xuelinlin
 *
 */
public class NoticeFragment extends Fragment{
	private ListView lv_notice;
	private NoticeAdapter adapter;
	private TextView tv_notice_tianjia;
	private List<Notification> notifications = new ArrayList<Notification>();
	private NotificationDB notificationDB;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.wechat_notice, null);
		notificationDB= new NotificationDB(getActivity());
		initView(v);
		return v;

	}
	private void initView(View v) {
		tv_notice_tianjia = (TextView) v.findViewById(R.id.tv_notice_tianjia);
		tv_notice_tianjia.setOnClickListener(listener);
		if(SharedPrefrencesForWechatUtil.getInstance(getActivity()).getIsNotice().equals("0")){
			tv_notice_tianjia.setVisibility(View.INVISIBLE);
		}else{
			tv_notice_tianjia.setVisibility(View.VISIBLE);
		}
		lv_notice = (ListView) v.findViewById(R.id.lv_notice);		
		lv_notice.setOnItemClickListener(itemClickListener);
		notifications = notificationDB.findNotificationList();
		adapter = new NoticeAdapter(getActivity(),notifications);
		lv_notice.setAdapter(adapter);
		lv_notice.setOnItemClickListener(itemClickListener);
	}
	private OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
				Notification no = notifications.get(position);
				if(no!=null){
					Intent intent = new Intent(getActivity(),WechatNoticeActivity.class);
					intent.putExtra("noticeId", no.getNoticeId());
					startActivity(intent);
				}
				
		}
	};
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
				createNotice();
			
		}

	};

	private void createNotice() {
		Intent intent = new Intent(getActivity(),CreateNoticeActivity.class);
		startActivity(intent);
	}
	public void refresh(List<Notification> notification){
		this.notifications = notification;
		adapter.refresh(notifications);
	}
	@Override
	public void onResume() {
		super.onResume();
		notifications = notificationDB.findNotificationList();
		adapter.refresh(notifications);
	}
}
