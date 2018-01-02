package com.yunhu.yhshxc.module.bbs;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yunhu.yhshxc.R;

public class BBSMessageItem {
	private Context context;
	private TextView mainUserName,reviewUserNameFirst,reviewUserNameSecond;
	private TextView mainUserTime,reviewUserTimeFirst,reviewUserTimeSecond;
	private TextView mainUserContent,reviewUserContentFirst,reviewUserContentSecond;
	private TextView userLocation,userDate;
	private TextView reviewNumber;
	private View view;
	private LinearLayout firstReviewLayout,secondReviewLayout;
	private LinearLayout mainMsgLayout;
	private ImageView mainUserIv;
//	private ImageView reviewUserFirstIv,reviewUserSecondIv;
	private ImageView mainContentIv;
	private LinearLayout contentIvLayout;
	private ImageView locationIv;
	private ProgressBar downPb;
	private ImageView reviewLeveFirst;
	private ImageView reviewLeveSecond;
	private ImageView mainUserLeve;
	private TextView mainUserScore;
	

	public TextView getMainUserScore() {
		return mainUserScore;
	}

	public ImageView getReviewLeveFirst() {
		return reviewLeveFirst;
	}

	public ImageView getReviewLeveSecond() {
		return reviewLeveSecond;
	}

	public ImageView getMainUserLeve() {
		return mainUserLeve;
	}
	
	public BBSMessageItem(Context context) {
		this.context=context;
		view=View.inflate(context, R.layout.bbs_msg_list_item, null);
		mainUserName=(TextView) view.findViewById(R.id.bbs_msg_userName_tv);
		reviewUserNameFirst=(TextView) view.findViewById(R.id.bbs_msg_reviewNameFirst_tv);
		reviewUserNameSecond=(TextView) view.findViewById(R.id.bbs_msg_reviewNameSecond_tv);
		mainUserTime=(TextView) view.findViewById(R.id.bbs_msg_afterTime_tv);
		reviewUserTimeFirst=(TextView) view.findViewById(R.id.bbs_msg_reviewNameFirstAfterTime_tv);
		reviewUserTimeSecond=(TextView) view.findViewById(R.id.bbs_msg_reviewNameSecondAfterTime_tv);
		mainUserContent=(TextView) view.findViewById(R.id.bbs_msg_content_tv);
		reviewUserContentFirst=(TextView) view.findViewById(R.id.bbs_msg_reviewFirstContent_tv);
		reviewUserContentSecond=(TextView) view.findViewById(R.id.bbs_msg_reviewContentSecond_tv);
		userLocation=(TextView) view.findViewById(R.id.bbs_msg_location_tv);
		userDate=(TextView) view.findViewById(R.id.bbs_msg_date_tv);
		reviewNumber=(TextView) view.findViewById(R.id.bbs_msg_reviewNumber_tv);
		mainMsgLayout=(LinearLayout)view.findViewById(R.id.bbs_msg_content_ll);
		firstReviewLayout=(LinearLayout)view.findViewById(R.id.bbs_msg_reviewFirst_ll);
		secondReviewLayout=(LinearLayout)view.findViewById(R.id.bbs_msg_reviewSecond_ll);
		mainUserIv=(ImageView)view.findViewById(R.id.bbs_msg_user_iv);
//		reviewUserFirstIv=(ImageView)view.findViewById(R.id.bbs_msg_reviewFirst_iv);
//		reviewUserSecondIv=(ImageView)view.findViewById(R.id.bbs_msg_reviewSecond_iv);
		mainContentIv=(ImageView)view.findViewById(R.id.bbs_msg_content_iv);
		contentIvLayout=(LinearLayout)view.findViewById(R.id.bbs_contentIv_ll);
		locationIv=(ImageView)view.findViewById(R.id.bbs_location_iv);
		downPb=(ProgressBar)view.findViewById(R.id.bbs_msg_content_iv_pb);
	
		mainUserLeve = (ImageView) view.findViewById(R.id.bbs_msg_userLeve_iv);
		reviewLeveFirst = (ImageView) view.findViewById(R.id.bbs_msg_reviewLeveFirst_iv);
		reviewLeveSecond = (ImageView) view.findViewById(R.id.bbs_msg_reviewLeveSecond_iv);
		mainUserScore = (TextView) view.findViewById(R.id.bbs_msg_user_score_tv);
	}

	public ProgressBar getDownPb() {
		return downPb;
	}




	public ImageView getLocationIv() {
		return locationIv;
	}




	public void setMainUserIv(Drawable drawable) {
		mainUserIv.setBackgroundDrawable(drawable);
	}


//	public void setReviewUserFirstIv(Drawable drawable) {
//		reviewUserFirstIv.setBackgroundDrawable(drawable);
//	}
//
//
//	public void setReviewUserSecondIv(Drawable drawable) {
//		reviewUserSecondIv.setBackgroundDrawable(drawable);
//	}


	public void setMainContentIv(Drawable drawable) {
		mainContentIv.setBackgroundDrawable(drawable);
	}


	public void setMainUserName(String name) {
		mainUserName.setText(name);
	}


	public void setReviewUserNameFirst(String name) {
		reviewUserNameFirst.setText(name);
	}


	public void setReviewUserNameSecond(String name) {
		reviewUserNameSecond.setText(name);
	}


	public void setMainUserTime(String time) {
		mainUserTime.setText(time);
	}


	public void setReviewUserTimeFirst(String time) {
		reviewUserTimeFirst.setText(time);
	}


	public void setReviewUserTimeSecond(String time) {
		reviewUserTimeSecond.setText(time);
	}


	public void setMainUserContent(String content) {
		mainUserContent.setText(content);
	}


	public TextView getMainUserContent() {
		return mainUserContent;
	}




	public void setMainUserContent(TextView mainUserContent) {
		this.mainUserContent = mainUserContent;
	}




	public void setReviewUserContentFirst(String content) {
		reviewUserContentFirst.setText(content);
	}


	public void setReviewUserContentSecond(String content) {
		reviewUserContentSecond.setText(content);
	}


	public void setUserLocation(String location) {
		userLocation.setText(location);
	}


	public void setUserDate(String date) {
		userDate.setText(date);
	}


	public void setReviewNumber(String number) {
		reviewNumber.setText(number);
	}




	public void setFirstReviewLayoutShow(boolean flag) {
		if(flag){
			firstReviewLayout.setVisibility(View.VISIBLE);
		}else{
			firstReviewLayout.setVisibility(View.GONE);
		}
	}

	public void setSecondReviewLayoutShow(boolean flag) {
		if(flag){
			secondReviewLayout.setVisibility(View.VISIBLE);
		}else{
			secondReviewLayout.setVisibility(View.GONE);
		}
	}

	
	public ImageView getMainUserIv() {
		return mainUserIv;
	}


	public ImageView getMainContentIv() {
		return mainContentIv;
	}

	public View getView() {
		return view;
	}

	public LinearLayout getContentIvLayout() {
		return contentIvLayout;
	}
	
	
}
