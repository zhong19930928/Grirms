package com.yunhu.yhshxc.wechat.survey;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.wechat.bo.Reply;
import com.yunhu.yhshxc.wechat.bo.Survey;

public class SurveyView {

	private View view;
	private Context context;
	private Survey survey = new Survey();

	private LinearLayout ll_survery_view;

	public SurveyView(Context context) {
		this.context = context;
		view = View.inflate(context, R.layout.activity_survey_item, null);

		ll_survery_view = (LinearLayout) view.findViewById(R.id.ll_survery_view);
	}

	/**
	 * 设置调查类评审评论信息
	 */
	public void setSurveyReply(Reply reply) {

		/**
		 * 判断是自己还是群成员
		 */
		if (SharedPreferencesUtil.getInstance(context).getUserId() == reply.getUserId()) {
			SurveyMySelfView surveyMySelfView = new SurveyMySelfView(context);
			surveyMySelfView.setSurveyReplyView(reply);
			ll_survery_view.addView(surveyMySelfView.getView());

		} else {
			SurveyOthersView surveyOthersView = new SurveyOthersView(context);
			surveyOthersView.setSurveyReplyView(reply);
			ll_survery_view.addView(surveyOthersView.getView());

		}

	}

	public View getView() {
		return view;
	}

	public Survey getExchangeWechat() {
		return survey;
	}

	public void setSurveyView(Survey survey) {
		this.survey = survey;

	}
}
