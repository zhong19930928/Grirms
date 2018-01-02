package com.yunhu.yhshxc.wechat.survey;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.wechat.Util.SharedPrefrencesForWechatUtil;
import com.yunhu.yhshxc.wechat.bo.Reply;
import com.yunhu.yhshxc.wechat.fragments.UserInfomationActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class SurveyOthersView implements OnClickListener{

	private View view;
	private Context context;
	private TextView tv_survey_myself_name;
	private TextView tv_survey_myself_content;
	private TextView tv_reply_date;
	private ImageView iv_chat;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;
	private Reply reply;
	
	public SurveyOthersView(Context context) {
		this.context = context;
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.people_65)
		.showImageForEmptyUri(R.drawable.people_65)
		.showImageOnFail(R.drawable.people_65)
		.cacheOnDisk(true)
		.cacheInMemory(true).displayer(new RoundedBitmapDisplayer(10))
		.build();
		view = View.inflate(context, R.layout.activity_survey_others_item, null);

		tv_survey_myself_name = (TextView) view.findViewById(R.id.tv_survey_others_name);
		tv_survey_myself_content = (TextView) view.findViewById(R.id.tv_survey_others_content);
		tv_reply_date = (TextView)view.findViewById(R.id.tv_reply_date);
		iv_chat = (ImageView) view.findViewById(R.id.iv_chat);
		
		iv_chat.setOnClickListener(this);
	}

	/**
	 * 设置调查类评审评论信息
	 */
	public void setSurveyReplyView(Reply reply) {
		this.reply = reply;
		if (!TextUtils.isEmpty(reply.getReplyName())) {
			tv_survey_myself_name.setText(reply.getReplyName());
		}
		if(!TextUtils.isEmpty(reply.getContent())){
			tv_survey_myself_content.setText(reply.getContent());
		}
		
		if(!TextUtils.isEmpty(reply.getDate())){
			tv_reply_date.setText(reply.getDate());
		}
		
		String headUrl = SharedPrefrencesForWechatUtil.getInstance(context).getUserHeadImg(String.valueOf(reply.getUserId()));
		if(!TextUtils.isEmpty(headUrl)){
			imageLoader.displayImage(headUrl, iv_chat, options, null);
		}

	}

	public View getView() {
		return view;
	}
	
	/**
	 * 查看用户信息
	 * @param userId
	 */
	private void userInfo(String userId){
		Intent intent = new Intent(context, UserInfomationActivity.class);
		intent.putExtra("userId", userId);
		context.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_chat:
			int userId = reply.getUserId();
			userInfo(String.valueOf(userId));
			break;

		default:
			break;
		}
	}

}
