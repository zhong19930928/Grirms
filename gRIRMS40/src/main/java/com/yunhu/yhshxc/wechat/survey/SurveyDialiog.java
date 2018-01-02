package com.yunhu.yhshxc.wechat.survey;

import gcg.org.debug.JLog;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.wechat.Util.WechatUtil;
import com.yunhu.yhshxc.wechat.approval.ApprovalDialog;
import com.yunhu.yhshxc.wechat.bo.Reply;
import com.yunhu.yhshxc.wechat.bo.Survey;
import com.yunhu.yhshxc.wechat.bo.Topic;
import com.yunhu.yhshxc.wechat.db.ReplyDB;
import com.yunhu.yhshxc.wechat.db.SurveyDB;
import com.yunhu.yhshxc.wechat.exchange.ExchangeActivity;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.loopj.android.http.RequestParams;

public class SurveyDialiog extends Dialog implements
		android.view.View.OnClickListener {

	private String TAG = "SurveyDialiog";
	public Context context;
	private Button btn_survey_submit;
	public Topic topic;
	public String options = "";
	public String replyOptions = "";
	public EditText ed_survey_remarks;
	private SurveyDB surveyDB = new SurveyDB(context);
	private ReplyDB replyDB = new ReplyDB(getContext());
	private SurveyActivity surveyActivity;
	public ExchangeActivity exchangeActivity;
	private LinearLayout ll_survey_options;
	public List<CheckBoxView> checkBoxViewList = new ArrayList<CheckBoxView>();
	private boolean isMul = false;//是否是多选 true是单选 false是多选 默认为单选
	
	@SuppressLint("ResourceAsColor")
	public SurveyDialiog(Context context,Topic topic) {
		super(context);
		this.context = context;
		this.topic = topic;
		if (this instanceof ApprovalDialog) {
			this.exchangeActivity = (ExchangeActivity)context;
		}else{
			this.surveyActivity = (SurveyActivity) context;
		}
		// 加载布局文件
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		setTitle(R.string.wechat_content15);
		View view = inflater.inflate(R.layout.activity_survey_dialog, null);
		btn_survey_submit = (Button) view.findViewById(R.id.btn_survey_reply_submit);
		ed_survey_remarks = (EditText) view.findViewById(R.id.ed_survey_remarks);
		ll_survey_options = (LinearLayout) view.findViewById(R.id.ll_survey_options);
		
		btn_survey_submit.setOnClickListener(this);
		
		/**
		 * 测试数据
		 */
		
		List<Survey> surveyList = surveyDB.findSurveyListByTopicId(topic.getTopicId());
		
		
		for(int i = 0; i < surveyList.size();i ++){
			Survey survey = new Survey();
			if(survey.getType() == Survey.TYPE_2){
				isMul = true;
			}
			survey = surveyList.get(i);
			CheckBoxView checkBoxView = new CheckBoxView(context);
			checkBoxView.setSurvey(survey);
			checkBoxView.setSurDialog(this);
			if(0 == i){
				checkBoxView.setChecked();
			}
			ll_survey_options.addView(checkBoxView.getView());
			checkBoxViewList.add(checkBoxView);
		}
		
	
		
		

		// dialog添加视图
		setContentView(view);

	}
	

	public void setMsg(String msg) {
	}

	public void setMsg(int msgId) {
	}
	
	/**
	 * 回帖参数
	 * 
	 * @return
	 */
	private RequestParams replyParams(Reply reply) {
		RequestParams params = new RequestParams();
		try {
			params.put("phoneno", PublicUtils.receivePhoneNO(getContext()));
			WechatUtil wechatUtil = new WechatUtil(getContext());
			params.put("data", wechatUtil.submitSurveyJson(reply));

		} catch (JSONException e) {
			e.printStackTrace();
		}
		JLog.d(TAG, "params:" + params.toString());
		return params;
	}
	
	/**
	 * 提交回帖
	 */
	private void submitReply(final Reply reply) {
		String url = UrlInfo.doWebRepliesInfo(getContext());
		GcgHttpClient.getInstance(getContext()).post(url, replyParams(reply),
				new HttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						JLog.d(TAG, "content:" + content);
						try {
							JSONObject obj = new JSONObject(content);
							String resultcode = obj.getString("resultcode");
							String pathCode = obj.getString("pathCode");
							int repliesId = obj.getInt("repliesId");
							if ("0000".equals(resultcode)) {
								reply.setReplyId(repliesId);
								reply.setPathCode(pathCode);
								Reply r = replyDB.findReplyByReplyId(repliesId);
								if (r!=null) {
									replyDB.updateReply(reply);
								}else{
									replyDB.insert(reply);
								}
								ToastOrder.makeText(getContext(),R.string.submit_ok, ToastOrder.LENGTH_LONG).show();
							} else {
								throw new Exception();
							}
						} catch (Exception e) {
						}
					}

					@Override
					public void onStart() {
						if (surveyActivity!=null) {
							surveyActivity.addSelfView(reply);
						}else if(exchangeActivity!=null){
							
						}
					}

					@Override
					public void onFinish() {
					}

					@Override
					public void onFailure(Throwable error, String content) {
					}
				});

	}
	
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_survey_reply_submit:
			
			for(int i = 0;i < checkBoxViewList.size();i++){
				CheckBoxView checkBoxView = checkBoxViewList.get(i);
				if(!TextUtils.isEmpty(checkBoxView.getOptions())){
					if(TextUtils.isEmpty(options)){
						options = options  + (i + 1) + "," + checkBoxView.getOptions();
						replyOptions ="," + replyOptions + (i + 1) + "," + checkBoxView.getOptions();
					}else{
						options = options + "\n" +  (i + 1) + "," + checkBoxView.getOptions();
						replyOptions = replyOptions  + (i + 1) + "," + checkBoxView.getOptions();
					}
				}
				
			}
			
			//调查选项必须选择一项提交,否则Toast提示
			if(TextUtils.isEmpty(options)){
				ToastOrder.makeText(context, R.string.wechat_content16, ToastOrder.LENGTH_SHORT).show();
			}else{
				if(!TextUtils.isEmpty(ed_survey_remarks.getText().toString())){
					options = options + "\n" + PublicUtils.getResourceString(context,R.string.approval_note) + ed_survey_remarks.getText().toString();
					replyOptions = replyOptions  + PublicUtils.getResourceString(context,R.string.approval_note) + ed_survey_remarks.getText().toString();
				}		
				String date = DateUtil.getCurDateTime();// yyyy-MM-dd
				// 储存回帖信息
				Reply reply = new Reply();
				reply.setDate(date);
				reply.setTopicId(topic.getTopicId());
				reply.setContent(options);
				reply.setReplyName(SharedPreferencesUtil.getInstance(getContext()).getUserName());
				reply.setIsSend(0);
				reply.setIsRead(1);
				reply.setSurvey(replyOptions);
				reply.setUserId(SharedPreferencesUtil.getInstance(getContext()).getUserId());
				submitReply(reply);
				dismiss();
				
			}
			
		
			break;

		default:
			break;
		}
	
	}

}
