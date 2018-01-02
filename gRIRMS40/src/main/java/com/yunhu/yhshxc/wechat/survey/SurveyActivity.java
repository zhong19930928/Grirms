package com.yunhu.yhshxc.wechat.survey;

import gcg.org.debug.JLog;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.wechat.Util.WechatUtil;
import com.yunhu.yhshxc.wechat.bo.Reply;
import com.yunhu.yhshxc.wechat.bo.Topic;
import com.yunhu.yhshxc.wechat.bo.TopicNotify;
import com.yunhu.yhshxc.wechat.db.ReplyDB;
import com.yunhu.yhshxc.wechat.db.TopicDB;
import com.yunhu.yhshxc.wechat.exchange.MyViewFilpper;
import com.yunhu.yhshxc.wechat.exchange.PropertyActivity;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.yunhu.android.pulltorefresh.library.PullToRefreshBase;
import com.yunhu.android.pulltorefresh.library.PullToRefreshBase.Mode;
import com.yunhu.android.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.yunhu.android.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.RequestParams;

public class SurveyActivity extends AbsBaseActivity {

    final int RESULT_CODE=101;
    final int REQUEST_CODE=1;
	
	private Button btn_survey_anwer;
	private Button btn_survey_back;// 点击返回按钮
	private TopicDB topicDB;
	private int topicId;
	private int replyLastId;
	private String loadType = "on";
	private Topic topic;
	private TextView tv_survey_name;
	private PullToRefreshListView lv_survey_chat;
	private PopupWindow window;
	private MyViewFilpper viewFlipper;

	private List<Reply> replyListAll = new ArrayList<Reply>();
	private ReplyDB replyDB = new ReplyDB(this);
	private SurveyAdapter surveyAdapter;
	private Button btn_survey_notice;
	private TopicNotify topicNotify = new TopicNotify();
	private List<TopicNotify> notifyList = new ArrayList<TopicNotify>();
	private Button btn_survey_property2;
	public int isHistory = 0;
	private int groupId = 0;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_survey);
		registSubmitSuccessReceiver();
		init();
//		
//		 Intent intent=new Intent(this,PropertyActivity.class);
//	     startActivityForResult(intent, REQUEST_CODE);
		
	}

	private void init() {

		
		Intent intent = getIntent();
		topicDB = new TopicDB(this);
		replyDB = new ReplyDB(this);
		topicId = intent.getIntExtra("topicId", 0);
		replyDB.updateAllReplyToIsRead(topicId);
		isHistory = intent.getIntExtra("isHistory", 0);
		topic = topicDB.findTopicById(topicId);
		groupId = topic.getGroupId();
		
		btn_survey_anwer = (Button) findViewById(R.id.btn_survey_anwer);
		btn_survey_back = (Button) findViewById(R.id.btn_survey_back);
		lv_survey_chat = (PullToRefreshListView) findViewById(R.id.lv_survey_chat);
		tv_survey_name = (TextView) findViewById(R.id.tv_survey_name);
		btn_survey_notice = (Button) findViewById(R.id.btn_survey_notice);
		btn_survey_property2 = (Button) findViewById(R.id.btn_survey_property2);

		btn_survey_back.setOnClickListener(this);
		btn_survey_anwer.setOnClickListener(this);
		btn_survey_notice.setOnClickListener(this);
		btn_survey_property2.setOnClickListener(this);
		
		//如果是历史话题将下面输入框隐藏
		if(1 == isHistory){
			btn_survey_anwer.setVisibility(View.GONE);
		}
		if(!TextUtils.isEmpty(String.valueOf(topic.getIsClose()))){
			if(1 == topic.getIsClose()){
				btn_survey_anwer.setVisibility(View.GONE);
			}
		}

		String label = DateUtils.formatDateTime(this.getApplicationContext(),
				System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
						| DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_ABBREV_ALL);
		lv_survey_chat.getLoadingLayoutProxy().setLastUpdatedLabel(label);
		lv_survey_chat.setMode(Mode.BOTH);
		lv_survey_chat.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// 向下滑动
				if (refreshView.getCurrentMode() == Mode.PULL_FROM_END) {
					if (replyListAll.size() > 0) {
						replyLastId = replyListAll.get(replyListAll.size() - 1).getReplyId();
					}
					loadType = "on";
					search();
				}
				// 向上加载
				if (refreshView.getCurrentMode() == Mode.PULL_FROM_START) {
					if (replyListAll.size() > 0) {
						replyLastId = replyListAll.get(0).getReplyId();
					}
					loadType = "off";
					search();
				}

			}
		});


		if (topic == null) {
			ToastOrder.makeText(this, R.string.wechat_content17, ToastOrder.LENGTH_SHORT).show();
			this.finish();
		} else {
			tv_survey_name.setText(topic.getTitle());
		}
		initData();
	}

	// 加载数据
	private void initData() {
		replyListAll = replyDB.findReplyListByTopicId(topicId);
	
		surveyAdapter = new SurveyAdapter(this, replyListAll);
		lv_survey_chat.setAdapter(surveyAdapter);
		scrollToBottom();
		

		for(int i = 0;i < replyListAll.size();i++){
			Reply reply = replyListAll.get(i);
			if(reply != null){
				reply.setIsRead(1);
				replyDB.updateReply(reply);
			}
			
		}
		

		
		
		if (replyListAll.size() > 0) {
			Reply reply = replyListAll.get(replyListAll.size() - 1);
			topic.setRecentTime(reply.getDate());
			topic.setRecentContent(reply.getReplyName() + ":" + reply.getContent());
			topicDB.updateTopic(topic);
			
			replyLastId = replyListAll.get(replyListAll.size() - 1).getReplyId();
		}
		loadType = "on";
		search();
	}
	
	/**
	 * 回帖信息查询接口数据
	 * @return
	 */
	private RequestParams paramsSearch() {
		RequestParams params = new RequestParams();
		params.put("phoneno", PublicUtils.receivePhoneNO(this));
		params.put("repliesId", replyLastId);
		params.put("topicId", topicId);
		params.put("loadType", loadType);// 取得之后的五条数据
		JLog.d(TAG, "params:" + params.toString());
		return params;
	}
	
	/**
	 * 回帖信息查询，向下刷新
	 */
	private void search(){

		String url = UrlInfo.weChatRepliesInfo(this);
		GcgHttpClient.getInstance(this).post(url, paramsSearch(),
				new HttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						JLog.d(TAG, "content:" + content);
						try {
							JSONObject obj = new JSONObject(content);
							String resultcode = obj.getString("resultcode");
							String replies = obj.getString("replies");
							JLog.d("abby", resultcode);
							if ("0000".equals(resultcode)) {
								if (TextUtils.isEmpty(replies)) {
//									Toast.makeText(getApplicationContext(),"数据加载完毕", Toast.LENGTH_SHORT).show();
								} else {
									JSONArray array = obj.getJSONArray("replies");
									WechatUtil wechatUtil = new WechatUtil(getApplicationContext());
									List<Reply> newReply = wechatUtil.parserSearchListItem(array);
									if (newReply != null && !newReply.isEmpty()) {

										for (int i = 0; i < newReply.size(); i++) {
											Reply reply = newReply.get(i);
											Reply r = replyDB.findReplyByReplyId(reply.getReplyId());
											reply.setIsRead(1);
											int reviewAuth = topic.getReplyReview();
											
											
											if (r==null) {
												if (reviewAuth == Topic.ISREPLY_2) {//话题查看权限只有创建者和发送本人的话
													if (reply.getUserId()==SharedPreferencesUtil.getInstance(getApplicationContext()).getUserId() || topic.getCreateUserId() == SharedPreferencesUtil.getInstance(getApplicationContext()).getUserId()) {
														replyDB.insert(reply);
													}
												}else{
													replyDB.insert(reply);
												}
											}else{
												if (reviewAuth == Topic.ISREPLY_2) {//话题查看权限只有创建者和发送本人的话
													if (reply.getUserId()==SharedPreferencesUtil.getInstance(getApplicationContext()).getUserId() || topic.getCreateUserId() == SharedPreferencesUtil.getInstance(getApplicationContext()).getUserId()) {
														replyDB.updateReply(reply);
													}
												}else{
													replyDB.updateReply(reply);
												}
												
											}
										}
										replyListAll = replyDB.findAllReplyListByTopicId(topicId);
										surveyAdapter.setReplyList(replyListAll);
										scrollToBottom();
									
										
										
									}
								}

							} else {
								throw new Exception();
							}
						} catch (Exception e) {

						}
					}

					@Override
					public void onStart() {

					}

					@Override
					public void onFinish() {
						lv_survey_chat.onRefreshComplete();
					}

					@Override
					public void onFailure(Throwable error, String content) {
					}
				});

	
	}
	

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_survey_back:
			this.finish();
			break;
		case R.id.btn_survey_anwer:
			SurveyDialiog surveyDialiog = new SurveyDialiog(this,topic);
			surveyDialiog.show();
			break;
		case R.id.btn_survey_notice:
			showPopWindow();
			break;
		case R.id.btn_survey_property2:
			toProperty();
			break;

		default:
			break;
		}

	}
	/**
	 * 广播接受PUSH消息
	 */
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(wechat_broadcast_receiver);
	}

	private void registSubmitSuccessReceiver() {
		IntentFilter filter = new IntentFilter(Constants.BROADCAST_WECHAT_REPLY);
		registerReceiver(wechat_broadcast_receiver, filter);
	}

	private BroadcastReceiver wechat_broadcast_receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent != null
					&& Constants.BROADCAST_WECHAT_REPLY.equals(intent.getAction())) {
				refreshUI();
			}
		}

	};
	
	private void showPopWindow() {
		// 利用layoutInflater获得View
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.activity_poupwindow, null);
		// 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()

		window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.WRAP_CONTENT);
		// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
		window.setFocusable(true);
		// 实例化一个ColorDrawable颜色为半透明
		// ColorDrawable dw = new ColorDrawable(0xa080808);
		ColorDrawable dw = new ColorDrawable(0xff5B5B5B);
		window.setBackgroundDrawable(dw);

		window.showAsDropDown(btn_survey_notice, 0, 0);

		viewFlipper = (MyViewFilpper) view.findViewById(R.id.viewFlipper);
		// 往viewFlipper添加View

		topicNotify.setContent("群公告群公告群公告群公告群公告群公告群公告");

		viewFlipper.addView(getTextView(topicNotify));

		for (int i = 0; i < notifyList.size(); i++) {
			viewFlipper.addView(getTextView(notifyList.get(i)));
		}

		// popWindow消失监听方法
		window.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				System.out.println("popWindow消失");
			}
		});

	}
	
	private TextView getTextView(TopicNotify topicNotify) {

		TextView textView = new TextView(this);
		textView.setText(topicNotify.getContent());
		return textView;
	}

	private void toProperty() {
		Intent intent = new Intent();
		intent.putExtra("topicId", topicId);
		intent.putExtra("groupId", groupId);
		intent.putExtra("isHistory", isHistory);
		intent.setClass(SurveyActivity.this, PropertyActivity.class);
		startActivityForResult(intent, RESULT_CODE);
	}
	
	/**
	 * 刷新页面
	 */
	public void refreshUI(){
		if (replyListAll!=null) {
			replyListAll.clear();
			replyListAll = replyDB.findReplyListByTopicId(topicId);
			for(int i = 0;i < replyListAll.size();i++){
				Reply reply = replyListAll.get(i);
				if(reply != null){
					reply.setIsRead(1);
					replyDB.updateReply(reply);
				}
				
			}
			surveyAdapter.setReplyList(replyListAll);
			surveyAdapter.refresh(replyListAll);
			scrollToBottom();
		}
	}
	
	/**
	 * 若为历史话题，则只能查看
	 * 1 为历史话题
	 */
	  @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        if(1 == resultCode){
	        	btn_survey_anwer.setVisibility(View.GONE);
	        }
	        super.onActivityResult(requestCode, resultCode, data);
	    }
	
	public void addSelfView(Reply reply){
		replyListAll.add(reply);
		surveyAdapter.setReplyList(replyListAll);
		surveyAdapter.refresh(replyListAll);
		scrollToBottom();
	}

	/**
	 * 滑动到最低端
	 */
	private ListView cListView;

	private void scrollToBottom() {
		cListView = lv_survey_chat.getRefreshableView();
		if (cListView != null) {
			cListView.setSelection(cListView.getCount() - 1);// 发送一条消息时，ListView显示选择最后一项
		}
	}
}
