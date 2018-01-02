package com.yunhu.yhshxc.wechat.topic;

import gcg.org.debug.JLog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.attendance.calendar.CalendarCardPopupWindowCreateTopic;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.wechat.Util.WechatUtil;
import com.yunhu.yhshxc.wechat.bo.Group;
import com.yunhu.yhshxc.wechat.bo.Survey;
import com.yunhu.yhshxc.wechat.bo.Topic;
import com.yunhu.yhshxc.wechat.db.GroupDB;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.loopj.android.http.RequestParams;

public class CreateTopicActivity extends AbsBaseActivity {
	private RadioButton rd_exchange;
	private RadioButton rd_survey;
	private RadioButton rd_approval;
	private Button btn_wechat_create_topic_start_time;//话题开始时间
	private Button btn_wechat_create_topic_end_time;//话题结束时间
	private TextView btn_wechat_create_topic_create;
	private EditText tv_wechat_create_topic_title;
	private EditText tv_wechat_create_topic_explain;
	private RadioGroup btn_wechat_create_topic_radio_group;
	private CalendarCardPopupWindowCreateTopic pw;// 日期选择控件
	private RadioGroup rg_if_must;
	private RadioButton rd_no_must;
	private RadioButton rd_must;
	private RadioGroup rg_if_everybody;
	private RadioButton rb_everybody;
	private RadioButton rb_owner;
	private RadioGroup rg_to_if_everybody;
	private RadioButton rb_to_everybody;
	private RadioButton rb_to_owner;
	private Date date;
	private Date dateSelect;
	private Topic topic = new Topic();
	private String type = Topic.TYPE_1;
	private String startDate;
	private String endDate;
	private EditText ed_speak_num;
	private int isReplies = Topic.ISREPLY_1;// 默认为不必须回帖；1为不必须 2为必须
	private int comment = Topic.COMMENT_1;// 1为所有人 2为本人 默认为所有人
	private int reply = Topic.REPLY_1;// 1为所有人 2为本人 默认为所有人
	private List<Group> listGroup = new ArrayList<Group>();
	private List<String> listGroupName = new ArrayList<String>();
	private Spinner sp_wechat_group;
	private ArrayAdapter<String> adapter;
	private int groupId = -1;
	private Group curGroup;
	private Survey survey = new Survey();// 调查类
	private MyProgressDialog progressDialog;
	private Button btn_create_topic_back;
	
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		this.setContentView(R.layout.activity_create_topic);
		init();
	}

	private void init() {
		rg_to_if_everybody = (RadioGroup) findViewById(R.id.rg_to_if_everybody);
		rb_to_everybody = (RadioButton) findViewById(R.id.rb_to_everybody);
		rb_everybody = (RadioButton) findViewById(R.id.rb_everybody);
		rg_if_everybody = (RadioGroup) findViewById(R.id.rg_if_everybody);
		rd_no_must = (RadioButton) findViewById(R.id.rd_no_must);
		rd_must = (RadioButton) findViewById(R.id.rd_must);
		rg_if_must = (RadioGroup) findViewById(R.id.rg_if_must);
		rd_exchange = (RadioButton) findViewById(R.id.rd_exchange);
		rd_survey = (RadioButton) findViewById(R.id.rd_survey);
		rd_approval = (RadioButton) findViewById(R.id.rd_approval);
		btn_wechat_create_topic_start_time = (Button) findViewById(R.id.btn_wechat_create_topic_start_time);
		btn_wechat_create_topic_end_time = (Button) findViewById(R.id.btn_wechat_create_topic_end_time);
		btn_wechat_create_topic_create = (TextView) findViewById(R.id.btn_wechat_create_topic_create);
		tv_wechat_create_topic_title = (EditText) findViewById(R.id.tv_wechat_create_topic_title);
		tv_wechat_create_topic_explain = (EditText) findViewById(R.id.tv_wechat_create_topic_explain);
		btn_wechat_create_topic_radio_group = (RadioGroup) findViewById(R.id.btn_wechat_create_topic_radio_group);
		ed_speak_num = (EditText) findViewById(R.id.ed_speak_num);
		rb_owner = (RadioButton) findViewById(R.id.rb_owner);
		rb_to_owner = (RadioButton) findViewById(R.id.rb_to_owner);
		sp_wechat_group = (Spinner) findViewById(R.id.sp_wechat_group);
		btn_create_topic_back = (Button) findViewById(R.id.btn_create_topic_back);
		
		ed_speak_num.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(ed_speak_num.getText().length() > 1 && ed_speak_num.getText().toString().startsWith("0")){
					ed_speak_num.setError(PublicUtils.getResourceString(CreateTopicActivity.this,R.string.wechat_content7));
				}
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});

		GroupDB groupDB = new GroupDB(this);
		listGroup = groupDB.findGroupList();
		listGroupName.add(PublicUtils.getResourceString(this,R.string.wechat_content6));
		for (int i = 0; i < listGroup.size(); i++) {
			listGroupName.add(listGroup.get(i).getGroupName());
		}
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, listGroupName);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_wechat_group.setAdapter(adapter);

		sp_wechat_group.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				JLog.d("abby", "position" + position);
				if (position >= 1) {
					curGroup = listGroup.get(position - 1);
					if (curGroup != null) {
						groupId = curGroup.getGroupId();
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		btn_create_topic_back.setOnClickListener(this);
		rd_survey.setOnClickListener(this);
		rd_approval.setOnClickListener(this);
		btn_wechat_create_topic_start_time.setOnClickListener(this);
		btn_wechat_create_topic_end_time.setOnClickListener(this);
		btn_wechat_create_topic_create.setOnClickListener(this);

		btn_wechat_create_topic_radio_group
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						JLog.d("abby", "checkedId" + checkedId);
						if (checkedId == rd_exchange.getId()) {
							type = Topic.TYPE_1;
						}
						if (checkedId == rd_survey.getId()) {
							type = Topic.TYPE_2;
						}
						if (checkedId == rd_approval.getId()) {
							type = Topic.TYPE_3;
						}
						if (checkedId != rd_exchange.getId()) {
							rd_exchange.setChecked(false);
						}

					}
				});
		rg_if_must.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == rd_no_must.getId()) {
					isReplies = Topic.ISREPLY_1;
				}
				if (checkedId == rd_must.getId()) {
					isReplies = Topic.ISREPLY_2;
				}
				if (checkedId != rd_no_must.getId()) {
					rd_no_must.setChecked(false);
				}
			}
		});

		/**
		 * 评论发言权限
		 */
		rg_if_everybody
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if (checkedId == rb_everybody.getId()) {
							comment = Topic.COMMENT_1;// 所有人
						}
						if (checkedId == rb_owner.getId()) {
							comment = Topic.COMMENT_2;// 所有人
						}

						if (checkedId != rb_everybody.getId()) {
							rb_everybody.setChecked(false);
						}
					}
				});
		/**
		 * 回帖查看权限
		 */

		rg_to_if_everybody
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {

						if (checkedId == rb_to_everybody.getId()) {
							reply = Topic.REPLY_1;
						}

						if (checkedId == rb_to_owner.getId()) {
							reply = Topic.REPLY_2;
						}

						if (checkedId != rb_to_everybody.getId()) {
							rb_to_everybody.setChecked(false);
						}
					}
				});

		pw = new CalendarCardPopupWindowCreateTopic(this);
		List<String> selectDateList = new ArrayList<String>();
		selectDateList.add(pw.getSelectDate());
		pw.setSelected(selectDateList);

	}

	private RequestParams params() {
		RequestParams params = new RequestParams();
		params.put("phoneno", PublicUtils.receivePhoneNO(this));
		WechatUtil wechatUtil = new WechatUtil(this);
		try {
			params.put("data", wechatUtil.submitTopicJson(topic,survey));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JLog.d(TAG, "params:" + params.toString());
		return params;
	}

	public void setDate(String time) {
		time = time.substring(0, 10);
		btn_wechat_create_topic_start_time.setText(time);
		btn_wechat_create_topic_end_time.setText(time);
	}

	public void setUpdateDate(String time, String timeStr) {
		time = time.substring(0, 10);
		if (timeStr.endsWith("start")) {
			compareStartTime(time,btn_wechat_create_topic_end_time.getText().toString());
			
		}
		if (timeStr.endsWith("end")) {
			compareEndTime(btn_wechat_create_topic_start_time.getText().toString(), time);
		}
	}

	private void submit() {
		progressDialog = new MyProgressDialog(CreateTopicActivity.this,R.style.CustomProgressDialog,getResources().getString(R.string.str_wechat_submit));
		String url = UrlInfo.doWebChatTopicInfo(this);
		GcgHttpClient.getInstance(this).post(url, params(),
				new HttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						JLog.d(TAG, "content:" + content);
						try {
							JSONObject obj = new JSONObject(content);
							String resultcode = obj.getString("resultcode");
							JLog.d("abby", resultcode);
							if ("0000".equals(resultcode)) {
								ToastOrder.makeText(getApplicationContext(),R.string.create_topic_ok, ToastOrder.LENGTH_SHORT).show();
								finish();
							} else if("0005".equals(resultcode)){
								ToastOrder.makeText(getApplicationContext(),R.string.recreate_topic, ToastOrder.LENGTH_SHORT).show();
							}else{
								throw new Exception();
							}
						} catch (Exception e) {
							ToastOrder.makeText(getApplicationContext(),R.string.ERROR_DATA, ToastOrder.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onStart() {
						progressDialog.show();
					}

					@Override
					public void onFinish() {
						if (progressDialog != null && progressDialog.isShowing()) {
							progressDialog.dismiss();
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						if (progressDialog != null && progressDialog.isShowing()) {
							progressDialog.dismiss();
						}
						ToastOrder.makeText(getApplicationContext(),R.string.ERROR_NETWORK, ToastOrder.LENGTH_SHORT).show();
					}
				});

	}

	// 设置调查类问题参数
	public void setSurvey(Survey survey) {
		this.survey = survey;
		JLog.d("abby", "options" + this.survey.getOptions());

	}

	/**
	 * 判断时间选择按钮开始时间不能小于结束时间
	 * @param startTime
	 * @param endTime
	 */
	private void compareStartTime(String startTime,String endTime ){
		String startTimeStr = startTime.replaceAll("/", "");
		String endTimeStr = endTime.replaceAll("/", "");
		if(Integer.parseInt(startTimeStr) > Integer.parseInt(endTimeStr)){
			ToastOrder.makeText(this, R.string.wechat_content8, ToastOrder.LENGTH_SHORT).show();
		}else{
			btn_wechat_create_topic_start_time.setText(startTime);
		}
		
	}
	
	private void compareEndTime(String startTime,String endTime ){
		String startTimeStr = startTime.replaceAll("/", "");
		String endTimeStr = endTime.replaceAll("/", "");
		if(Integer.parseInt(startTimeStr) > Integer.parseInt(endTimeStr)){
			ToastOrder.makeText(this, R.string.wechat_content9, ToastOrder.LENGTH_SHORT).show();
		}else{
			btn_wechat_create_topic_end_time.setText(endTime);
		}
		
	}
	
	private AddSurveyDialog addSurveyDialog = null;
	private AddApprovalDialog addApprovalDialog = null;
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.rd_survey:
			if (addSurveyDialog == null) {
				addSurveyDialog = new AddSurveyDialog(this);
			}
			addSurveyDialog.show();

			break;
		case R.id.rd_approval:
			if(addApprovalDialog == null){
				addApprovalDialog = new AddApprovalDialog(this);
			}
			addApprovalDialog.show();
			break;
		case R.id.btn_wechat_create_topic_start_time:
			pw.setButtonTime("start");
			pw.show();
			break;
		case R.id.btn_wechat_create_topic_end_time:
			pw.setButtonTime("end");
			pw.show();
			break;
		 
		case R.id.btn_create_topic_back:
			finish();
			break;

		case R.id.btn_wechat_create_topic_create:
			String title = tv_wechat_create_topic_title.getText().toString().trim();// 话题名称
			String explain = tv_wechat_create_topic_explain.getText().toString().trim();// 话题说明
			startDate = btn_wechat_create_topic_start_time.getText().toString();
			endDate = btn_wechat_create_topic_end_time.getText().toString();

			if (TextUtils.isEmpty(title)) {
				ToastOrder.makeText(this, R.string.wechat_content5, ToastOrder.LENGTH_SHORT).show();
			} else {
				if (TextUtils.isEmpty(explain)) {
					ToastOrder.makeText(this, R.string.wechat_content4, ToastOrder.LENGTH_SHORT).show();
				} else {
					if (groupId == -1) {
						ToastOrder.makeText(this, R.string.wechat_content3, ToastOrder.LENGTH_SHORT).show();
					} else {
						
						int speakNum = 0;//默认为0
						if (!TextUtils.isEmpty(ed_speak_num.getText().toString())) {
							speakNum = Integer.parseInt(ed_speak_num.getText().toString());
						} 
							topic.setTitle(title);
							topic.setExplain(explain);
							topic.setType(type);
							String startDateStr = startDate.replaceAll("/", "-");
							topic.setFrom(startDateStr);
							topic.setGroupId(groupId);
							topic.setIsClose(0);
							String endDateStr = endDate.replaceAll("/", "-");
							topic.setTo(endDateStr);
							
							
							topic.setSpeakNum(speakNum);
							topic.setIsReply(isReplies);
							topic.setComment(comment);
							topic.setReplyReview(reply);
							topic.setClassify(curGroup != null ? curGroup.getType() : Topic.CLASSIFY_TYPE_BM);
							
							/**
							 * 如果为调查/审批类话题,判断是否添加选项,区分没有点击确定dialog消失的情况
							 */
							if(type.equals(Topic.TYPE_2) || type.equals(Topic.TYPE_3)){
								if(TextUtils.isEmpty(survey.getOptions())){
									if(type.equals(Topic.TYPE_2)){
										ToastOrder.makeText(this, R.string.wechat_content2, ToastOrder.LENGTH_SHORT).show();
									}else if(type.equals(Topic.TYPE_3)){
										ToastOrder.makeText(this, R.string.wechat_content1, ToastOrder.LENGTH_SHORT).show();
									}
									
								}else{
									topic.setOptions(survey.getOptions());
									submit();
								}
							}else{
								submit();
							}
							
							
						

						
					}

					// }

				}
			}

			break;

		default:
			break;
		}
	}
}
