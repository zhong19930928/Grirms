package com.yunhu.yhshxc.wechat.exchange;

import gcg.org.debug.JLog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import android.view.inputmethod.InputMethodManager;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;
import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.CoreHttpHelper;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.submitManager.bo.PendingRequestVO;
import com.yunhu.yhshxc.submitManager.bo.TablePending;
import com.yunhu.yhshxc.submitManager.core.SubmitWorkManager;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.FileHelper;
import com.yunhu.yhshxc.utility.MD5Helper;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.wechat.Util.SharedPrefrencesForWechatUtil;
import com.yunhu.yhshxc.wechat.Util.WechatUtil;
import com.yunhu.yhshxc.wechat.approval.ApprovalDialog;
import com.yunhu.yhshxc.wechat.approval.ApprovalDialog.ApprovalGetUser;
import com.yunhu.yhshxc.wechat.bo.Comment;
import com.yunhu.yhshxc.wechat.bo.Reply;
import com.yunhu.yhshxc.wechat.bo.Topic;
import com.yunhu.yhshxc.wechat.bo.TopicNotify;
import com.yunhu.yhshxc.wechat.content.ContentFragments;
import com.yunhu.yhshxc.wechat.content.ContentFragments.ContentFragmentSelectCheck;
import com.yunhu.yhshxc.wechat.content.ContentPhotoView;
import com.yunhu.yhshxc.wechat.content.FileView;
import com.yunhu.yhshxc.wechat.content.VideoView;
import com.yunhu.yhshxc.wechat.content.VoiceView;
import com.yunhu.yhshxc.wechat.db.ReplyDB;
import com.yunhu.yhshxc.wechat.db.TopicDB;
import com.yunhu.yhshxc.wechat.pic.PhotoAlbumActivity;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.yunhu.android.pulltorefresh.library.PullToRefreshBase;
import com.yunhu.android.pulltorefresh.library.PullToRefreshBase.Mode;
import com.yunhu.android.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.yunhu.android.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.RequestParams;

public class ExchangeActivity extends AbsBaseActivity implements KeyboardControllListener, CommentSelectListener,ApprovalGetUser,ContentFragmentSelectCheck {

	final int RESULT_CODE = 101;
	final int REQUEST_CODE = 1;
	final static int GROUPUSER_CODE = 123;// 发送审核文件@选择审核人的请求码
	final static int GETUSER_FLAG = 124;//发送审核文件选择审核人的返回码
    private EditText et;//来自approvaldialog的输入框
	private Button btn_exchange_notice;// 公告
	public Button btn_exchange_property;// 设置
	private MyViewFilpper viewFlipper;
	private PopupWindow window;
	public TextView tv_topic_name;// 话题名称
	private List<TopicNotify> notifyList = new ArrayList<TopicNotify>();
	private TopicNotify topicNotify = new TopicNotify();
	private String notifyContent;
	private Button btn_exchange_submit;// 发送按钮
	private Button btn_exchange_sound;
	public PullToRefreshListView lv_exchange_chat;
	public List<Reply> replyListAll = new ArrayList<Reply>();
	public ExchangeAdapter exchangeAdapter;
	public EditText ed_exchange_comment;// 消息内容输入框
	private int topicId;
	public int isHistory = 0;
	public Button btn_exchange_add;// 添加按钮
	public int replyId;
	private int replyLastId;
	public String loadType = "on";// 默认获取最新消息
	public Topic topic;// 话题
	// private String commentStr;
	public TopicDB topicDB;// 话题数据库
	public ReplyDB replyDB;// 回复内容数据库
	public WechatView currentWechatView;
	private Button btn_topic_back;// 后退
	private LinearLayout ll_btn_container;
	private LinearLayout more;
	private ImageView btn_take_picture, btn_file, btn_picture, btn_voice;
	public boolean isApproval;// 是否是审批类
	public int groupId = 0;
	public LinearLayout ll_send;
	// private KeyboardLayout keyboardLayout;
	private LinearLayout ll_all_send;

	private LinearLayout ll_send_2;// 评价布局
	private Button btn_exchange_cancel_2, btn_exchange_submit_2;
	public EditText ed_exchange_comment_2;
	public LinearLayout ll_b;

	public List<String> photoPathList = new ArrayList<String>();// 照片路径集合
	public List<String> voicePathList = new ArrayList<String>();// 录音路径集合
	public List<String> filePathList = new ArrayList<String>();// 附件路径集合
	public List<String> videoPathList = new ArrayList<String>();// 视频路径集合

	public List<ContentPhotoView> photoViewList = new ArrayList<ContentPhotoView>();
	public List<VoiceView> voiceViewList = new ArrayList<VoiceView>();
	public List<FileView> fileViewList = new ArrayList<FileView>();
	public List<VideoView> videoViewList = new ArrayList<VideoView>();
	public boolean isPersonalWechat;// 是否是私聊
	public boolean isCloseTopic = false;// 是否是关闭话题 true 是 false 否
	public int authUserId; // 审核人员信息id
	public String authUserName;// 审核人员信息name
    private EditText contentFragmentEdit;
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_exchange_wechat);
		registSubmitSuccessReceiver();
		initWidget();
		initData();
		
//			this.registerForContextMenu(lv_exchange_chat);
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (1 == resultCode) {
			ll_all_send.setVisibility(View.GONE);
			isCloseTopic = true;
		}
		if (resultCode == GROUPUSER_CODE) {
			//处理本身发起的@审核人请求结果
			authUserId = data.getIntExtra("userId", 0);
			authUserName = data.getStringExtra("userName");
			ed_exchange_comment.setText("@" + authUserName+"  ");
			ed_exchange_comment.setSelection(ed_exchange_comment.getText().length());
		}
		if (resultCode==ApprovalDialog.APPROVALDIALOG_FLAG) {
			//处理ApprovalDialog发起的@审核人请求结果
			authUserId = data.getIntExtra("userId", 0);
			authUserName = data.getStringExtra("userName");
			if (authUserName!=null) {
				
				et.setText("@"+authUserName+"  ");
			}
		}
		if (resultCode==ContentFragments.CONTENTFRAGMENTS_FLAG) {
			//处理ContentFragments发起的@审核人请求结果
			authUserId = data.getIntExtra("userId", 0);
			authUserName = data.getStringExtra("userName");
			if (contentFragmentEdit!=null) {
				contentFragmentEdit.setText("@"+authUserName+"  ");
				contentFragmentEdit.setSelection(contentFragmentEdit.getText().length());
			}
			
		}
	}

	private void initCommentSendView() {
		ll_send_2 = (LinearLayout) findViewById(R.id.ll_send_2);
		ed_exchange_comment_2 = (EditText) findViewById(R.id.ed_exchange_comment_2);
		btn_exchange_cancel_2 = (Button) findViewById(R.id.btn_exchange_cancel_2);
		btn_exchange_submit_2 = (Button) findViewById(R.id.btn_exchange_submit_2);
		btn_exchange_submit_2.setOnClickListener(this);
		btn_exchange_cancel_2.setOnClickListener(this);
		ll_b = (LinearLayout) findViewById(R.id.ll_b);

		ed_exchange_comment.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {// 回帖输入框只要获取过焦点就认为是回帖
					currentWechatView = null;
				}
			}
		});
		// 监听输入框文本变化
		ed_exchange_comment.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// 如果属于审批会话并且输入的首个字母是@,那么就调出该群组的成员列表进行选择可审批的人
				if (count != 0) {// 输入框有内容
					
					String result = s.toString();
					char[] array = result.toCharArray();
					String re = array[0] + "";
					if (re.equals("@") && array.length < 2 && isApproval) {
						Intent intent = new Intent(ExchangeActivity.this, GroupUserActivity.class);
					
						intent.putExtra("groupId", groupId);
						intent.putExtra("getUser", GETUSER_FLAG);
						startActivityForResult(intent, GROUPUSER_CODE);
					}
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

	}

	public void initWidget() {
		btn_exchange_notice = (Button) findViewById(R.id.btn_exchange_notice);
		btn_exchange_property = (Button) findViewById(R.id.btn_exchange_property2);
		tv_topic_name = (TextView) findViewById(R.id.tv_topic_name);
		btn_exchange_submit = (Button) findViewById(R.id.btn_exchange_submit);
		btn_exchange_sound = (Button) findViewById(R.id.btn_exchange_sound);
		lv_exchange_chat = (PullToRefreshListView) findViewById(R.id.lv_exchange_chat);
		ed_exchange_comment = (EditText) findViewById(R.id.ed_exchange_comment);
		btn_exchange_add = (Button) findViewById(R.id.btn_exchange_add);
		btn_topic_back = (Button) findViewById(R.id.btn_topic_back);
		ll_btn_container = (LinearLayout) findViewById(R.id.ll_btn_container);
		more = (LinearLayout) findViewById(R.id.more);
		btn_take_picture = (ImageView) findViewById(R.id.btn_take_picture);
		btn_file = (ImageView) findViewById(R.id.btn_file);
		btn_picture = (ImageView) findViewById(R.id.btn_picture);
		btn_voice = (ImageView) findViewById(R.id.btn_voice);
		ll_send = (LinearLayout) findViewById(R.id.ll_send);
		ll_all_send = (LinearLayout) findViewById(R.id.ll_all_send);
		initCommentSendView();
		btn_picture.setOnClickListener(this);
		btn_voice.setOnClickListener(this);
		btn_file.setOnClickListener(this);
		btn_take_picture.setOnClickListener(this);
		ed_exchange_comment.setOnClickListener(this);
		btn_topic_back.setOnClickListener(this);
		btn_exchange_notice.setOnClickListener(this);
		btn_exchange_property.setOnClickListener(this);
		btn_exchange_submit.setOnClickListener(this);
		btn_exchange_sound.setOnClickListener(this);
		btn_exchange_add.setOnClickListener(this);
		keyBoardState();
	}

	public void initData() {
		// keyboardLayout = (KeyboardLayout)
		// findViewById(R.id.ll_exchange_root);
		Intent intent = getIntent();
		replyDB = new ReplyDB(this);
		topicDB = new TopicDB(this);
		topicId = intent.getIntExtra("topicId", 0);
		replyDB.updateAllReplyToIsRead(topicId);
		isHistory = intent.getIntExtra("isHistory", 0);
		topic = topicDB.findTopicById(topicId);
		groupId = topic.getGroupId();
		String label = DateUtils.formatDateTime(this.getApplicationContext(), System.currentTimeMillis(),
				DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
		lv_exchange_chat.getLoadingLayoutProxy().setLastUpdatedLabel(label);
		lv_exchange_chat.setMode(Mode.BOTH);
		lv_exchange_chat.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				isBottom=true;
				// 上拉加载
				if (refreshView.getCurrentMode() == Mode.PULL_FROM_END) {
					if (replyListAll.size() > 0) {
						replyLastId = replyListAll.get(replyListAll.size() - 1).getReplyId();
					}
					loadType = "on";
					replyListAll.clear();
					replyLastId = 0;
//				    scrollToBottom();
					isBottom=true;
					if (cListView!=null) {
						cListView.setSelection(msgPosition);
					}
					search();
		     
           
                   

	
				}
				// 下拉刷新
				if (refreshView.getCurrentMode() == Mode.PULL_FROM_START) {
					if (replyListAll.size() > 0) {
						replyLastId = replyListAll.get(0).getReplyId();
					}
					isBottom=false;
					loadType = "off";
				   
					search();
//					if (replyListAll!=null) {					
//						cListView.setSelection(0);
//					}
			    
				}

			}
		});

		if (topic == null) {
			ToastOrder.makeText(this, R.string.wechat_content17, ToastOrder.LENGTH_SHORT).show();
			this.finish();
		} else {
			tv_topic_name.setText(topic.getTitle());
		}
		// 如果是历史话题将下面输入框隐藏
		if (1 == isHistory) {
			ll_all_send.setVisibility(View.GONE);
		}
		if (!TextUtils.isEmpty(String.valueOf(topic.getIsClose()))) {
			if (1 == topic.getIsClose()) {
				ll_all_send.setVisibility(View.GONE);
			}
		}
		getData();
	}

	private RequestParams paramsSearch() {
		RequestParams params = new RequestParams();
		params.put("phoneno", PublicUtils.receivePhoneNO(this));
		params.put("repliesId", replyLastId);
		params.put("topicId", topicId);
		params.put("loadType", loadType);// 取得之后的五条数据
		JLog.d(TAG, "params:" + params.toString());
		return params;
	}

	private void search() {
		String url = UrlInfo.weChatRepliesInfo(this);// 回帖信息查询,向下刷新
		GcgHttpClient.getInstance(this).post(url, paramsSearch(), new HttpResponseListener() {
			@Override
			public void onSuccess(int statusCode, String content) {
				JLog.d(TAG, "content:" + content);
				try {
					JSONObject obj = new JSONObject(content);
					String resultcode = obj.getString("resultcode");
					// String replies = obj.getString("replies");
					JLog.d("abby", resultcode);
					if ("0000".equals(resultcode)) {
						JSONArray array = obj.getJSONArray("replies");
						WechatUtil wechatUtil = new WechatUtil(getApplicationContext());
						List<Reply> newReply = wechatUtil.parserSearchListItem(array);
						if (newReply != null && !newReply.isEmpty()) {
							for (int i = 0; i < newReply.size(); i++) {
								Reply reply = newReply.get(i);
								Reply r = replyDB.findReplyByReplyId(reply.getReplyId());
								reply.setIsRead(1);
								/**
								 * 回帖查看权限
								 */
								int reviewAuth = topic.getReplyReview();

								if (r == null) {
									if (reviewAuth == Topic.ISREPLY_2) {// 话题查看权限只有创建者和发送本人的话
										if (reply.getUserId() == SharedPreferencesUtil
												.getInstance(getApplicationContext()).getUserId()
												|| topic.getCreateUserId() == SharedPreferencesUtil
														.getInstance(getApplicationContext()).getUserId()) {
											replyDB.insert(reply);
										}
									} else {
										replyDB.insert(reply);
									}
								} else {
									if (reviewAuth == Topic.ISREPLY_2) {// 话题查看权限只有创建者和发送本人的话
										if (reply.getUserId() == SharedPreferencesUtil
												.getInstance(getApplicationContext()).getUserId()
												|| topic.getCreateUserId() == SharedPreferencesUtil
														.getInstance(getApplicationContext()).getUserId()) {
											replyDB.updateReply(reply);
										}
									} else {
										replyDB.updateReply(reply);
									}

								}

								// 将查询出最新一条数据存在topic数据库
								if (i == (newReply.size() - 1)) {
									if ("on".equals(loadType)) {
										if (reviewAuth == Topic.ISREPLY_2) {// 话题查看权限只有创建者和发送本人的话
											if (reply.getUserId() == SharedPreferencesUtil
													.getInstance(getApplicationContext()).getUserId()
													|| topic.getCreateUserId() == SharedPreferencesUtil
															.getInstance(getApplicationContext()).getUserId()) {
												Reply replyLast = newReply.get(i);
												topic.setRecentTime(replyLast.getDate());
												if (TextUtils.isEmpty(replyLast.getContent())) {
													if (!TextUtils.isEmpty(replyLast.getPhoto())) {
														topic.setRecentContent(
																replyLast.getReplyName() + ":" + PublicUtils.getResourceString(ExchangeActivity.this,R.string.wechat_content75));
													} else if (!TextUtils.isEmpty(replyLast.getAttachment())) {
														topic.setRecentContent(
																replyLast.getReplyName() + ":" + PublicUtils.getResourceString(ExchangeActivity.this,R.string.wechat_content74));
													}
												} else {
													topic.setRecentContent(
															replyLast.getReplyName() + ":" + replyLast.getContent());
												}
												topicDB.updateTopic(topic);
											}
										} else {
											Reply replyLast = newReply.get(i);
											topic.setRecentTime(replyLast.getDate());
											if (TextUtils.isEmpty(replyLast.getContent())) {
												if (!TextUtils.isEmpty(replyLast.getPhoto())) {
													topic.setRecentContent(replyLast.getReplyName() + ":" + PublicUtils.getResourceString(ExchangeActivity.this,R.string.wechat_content75));
												} else if (!TextUtils.isEmpty(replyLast.getAttachment())) {
													topic.setRecentContent(replyLast.getReplyName() + ":" + PublicUtils.getResourceString(ExchangeActivity.this,R.string.wechat_content74));
												}
											} else {
												topic.setRecentContent(
														replyLast.getReplyName() + ":" + replyLast.getContent());
											}
											topicDB.updateTopic(topic);

										}
									}
								}

							}
							replyListAll = replyDB.findAllReplyListByTopicId(topicId);
							exchangeAdapter.setReplyList(replyListAll);
//							scrollToBottom();
				
						}

					} else {
						throw new Exception();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onStart() {

			}

			@Override
			public void onFinish() {
				lv_exchange_chat.onRefreshComplete();
				if (isBottom&&replyListAll!=null) {					
					cListView.setSelection(replyListAll.size()-1);
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
			}
		});

	};

	private void getData() {
		replyListAll = replyDB.findReplyListByTopicId(topicId);
		if (replyListAll.size() > 0) {
			Reply reply = replyListAll.get(replyListAll.size() - 1);
			topic.setRecentTime(reply.getDate());
			if (TextUtils.isEmpty(reply.getContent())) {
				if (!TextUtils.isEmpty(reply.getPhoto())) {
					topic.setRecentContent(reply.getReplyName() + ":" + PublicUtils.getResourceString(ExchangeActivity.this,R.string.wechat_content75));
				} else if (!TextUtils.isEmpty(reply.getAttachment())) {
					topic.setRecentContent(reply.getReplyName() + ":" + PublicUtils.getResourceString(ExchangeActivity.this,R.string.wechat_content74));
				}
			} else {
				topic.setRecentContent(reply.getReplyName() + ":" + reply.getContent());
			}
			topicDB.updateTopic(topic);
			replyLastId = reply.getReplyId();
		}
		exchangeAdapter = new ExchangeAdapter(this, replyListAll);
		exchangeAdapter.setRepliesAuth(topic);
		// exchangeAdapter.setExchangeComment(ed_exchange_comment);
		exchangeAdapter.setListener(this);
		exchangeAdapter.setCommentSelectListener(this);
		lv_exchange_chat.setAdapter(exchangeAdapter);
	
	   scrollToBottom();
		search();

	}


	/**
	 * 提交评论
	 */
	private void submitComment() {
		if (currentWechatView != null) {
			String commentStr = ed_exchange_comment_2.getText().toString().trim();
			if (!TextUtils.isEmpty(commentStr)) {
				currentWechatView.submitComment(commentStr);
				cancelSendComment();
			} else {
				ToastOrder.makeText(this, R.string.wechat_content77, ToastOrder.LENGTH_LONG).show();
			}
		}
	}

	/**
	 * 回帖参数
	 * 
	 * @return
	 */
	private RequestParams replyParams(Topic topic, Reply reply) {
		RequestParams params = new RequestParams();
		try {
			params.put("phoneno", PublicUtils.receivePhoneNO(this));
			WechatUtil wechatUtil = new WechatUtil(this);
			JLog.d("abby", "data" + wechatUtil.submitRepliesJson(topic, reply));
			params.put("data", wechatUtil.submitRepliesJson(topic, reply));

		} catch (JSONException e) {
			e.printStackTrace();
		}
		JLog.d(TAG, "params:" + params.toString());
		return params;
	}

	/**
	 * 提交回帖
	 */
	public void submitReply() {
		String replyContent = ed_exchange_comment.getText().toString().trim();
		if (!(TextUtils.isEmpty(replyContent) && TextUtils.isEmpty(replyPhoto) && TextUtils.isEmpty(atachmentPath))) {
			String date = DateUtil.getCurDateTime();// yyyy-MM-dd
			// 储存回帖信息
			final Reply reply = new Reply();
			reply.setMsgKey(PublicUtils.chatMsgKey(this));
			reply.setDate(date);
			reply.setTopicId(topicId);
			reply.setContent(replyContent);
			reply.setReplyName(SharedPreferencesUtil.getInstance(this).getUserName());
			reply.setIsSend(0);
			reply.setIsRead(1);
			reply.setUserId(SharedPreferencesUtil.getInstance(this).getUserId());
			reply.setPhoto(replyPhoto);
			reply.setAttachment(atachmentPath);
			// 只针对审核话题的值
			if (isApproval && authUserName != null && authUserId != 0) {
				reply.setAuthUserId(authUserId);
				reply.setAuthUserName(authUserName);
				
				
				
			}
			submitReplyData(reply);
			submitReplyPhoto(reply);
			submitAtachment(reply);
			atachmentPath = "";
			if (contentFragments != null) {
				contentFragments.setWechatMsg("");
			}
			videoPathList.clear();
			videoViewList.clear();
			voicePathList.clear();
			voiceViewList.clear();
			photoPathList.clear();
			photoViewList.clear();
			filePathList.clear();
			fileViewList.clear();

		} else {
			ToastOrder.makeText(this, R.string.wechat_content76, ToastOrder.LENGTH_LONG).show();
		}

	}

	private void submitReplyData(final Reply reply) {
		String url = UrlInfo.doWebRepliesInfo(this);
		GcgHttpClient.getInstance(this).post(url, replyParams(topic, reply), new HttpResponseListener() {
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
						Reply r = replyDB.findReplyByReplyId(reply.getReplyId());
						if (r != null) {
							replyDB.updateReply(reply);
						} else {
							replyDB.insert(reply);
						}
						topic.setRecentTime(reply.getDate());
						if (TextUtils.isEmpty(reply.getContent())) {
							if (!TextUtils.isEmpty(reply.getPhoto())) {
								topic.setRecentContent(reply.getReplyName() + ":" + PublicUtils.getResourceString(ExchangeActivity.this,R.string.wechat_content75));
							} else if (!TextUtils.isEmpty(reply.getAttachment())) {
								topic.setRecentContent(reply.getReplyName() + ":" + PublicUtils.getResourceString(ExchangeActivity.this,R.string.wechat_content74));
							}
						} else {
							topic.setRecentContent(reply.getReplyName() + ":" + reply.getContent());
						}
						topicDB.updateTopic(topic);
						// Toast.makeText(getApplicationContext(),"提交成功 ！",
						// Toast.LENGTH_LONG).show();
					} else {
						throw new Exception();
					}
				} catch (Exception e) {

				}
			}

			@Override
			public void onStart() {
				if (ed_exchange_comment != null) {
					ed_exchange_comment.setText("");
					ed_exchange_comment.setHint("");
					PublicUtils.hideKeyboard(ExchangeActivity.this);

				}
				replyListAll.add(reply);
				exchangeAdapter.setReplyList(replyListAll);
				exchangeAdapter.refresh(replyListAll);
				
				
				
				 authUserId = 0; // 审核人员信息id
				 authUserName = "";// 审核人员信息name
				scrollToBottom();
				
				
				
				
				
			}

			@Override
			public void onFinish() {
			}

			@Override
			public void onFailure(Throwable error, String content) {
				Toast.makeText(ExchangeActivity.this, R.string.wechat_content73, Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * 提交回帖图片
	 * 
	 * @param reply
	 */
	public void submitReplyPhoto(Reply reply) {
		if (reply != null) {
			String photos = reply.getPhoto();
			if (!TextUtils.isEmpty(photos)) {
				String[] s = photos.split(",");
				for (int i = 0; i < s.length; i++) {
					String name = s[i];
					PendingRequestVO vo = new PendingRequestVO();
					vo.setContent(PublicUtils.getResourceString(this,R.string.wechat_content72));
					vo.setTitle(PublicUtils.getResourceString(this,R.string.wechat_content71));
					vo.setMethodType(SubmitWorkManager.HTTP_METHOD_POST);
					vo.setType(TablePending.TYPE_IMAGE);
					vo.setUrl(UrlInfo.getUrlPhoto(this));
					HashMap<String, String> params = new HashMap<String, String>();
					params.put("name", name);
					params.put("companyid", String.valueOf(SharedPreferencesUtil.getInstance(this).getCompanyId()));
					params.put("md5Code", MD5Helper.getMD5Checksum2(Constants.WECHAT_PATH + name));
					params.put("weImg", MD5Helper.getMD5Checksum2(Constants.WECHAT_PATH + name));
					vo.setParams(params);
					vo.setImagePath(Constants.WECHAT_PATH + name);
					SubmitWorkManager.getInstance(this).performImageUpload(vo);
					SubmitWorkManager.getInstance(this).commit();
				}
			}
		}
		replyPhoto = "";
	}

	/**
	 * 提交回帖附件
	 * 
	 * @param reply
	 */
	public void submitAtachment(Reply reply) {
		try {
			if (reply != null) {
				String atachment = reply.getAttachment();
				if (!TextUtils.isEmpty(atachment)) {
					JSONObject attObj = new JSONObject(atachment);
					Iterator<String> iterator = attObj.keys();
					while (iterator.hasNext()) {
						String key = iterator.next();
						String url = attObj.getString(key);

						PendingRequestVO vo = new PendingRequestVO();
						vo.setTitle(PublicUtils.getResourceString(ExchangeActivity.this,R.string.im_chat));
						vo.setContent(PublicUtils.getResourceString(ExchangeActivity.this,R.string.wechat_content78));
						vo.addFiles("name", url);
						HashMap<String, String> params = new HashMap<String, String>();
						params.put("cid", String.valueOf(SharedPreferencesUtil.getInstance(this).getCompanyId()));
						params.put("name", key);
						params.put("fip", "help.gcgcloud.com");
						params.put("md5Code", MD5Helper.getMD5Checksum2(url));
						vo.setParams(params);
						new CoreHttpHelper().performWehatUpload(this, vo);
					}
				}
			}
		} catch (Exception e) {
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_exchange_notice:
			showPopWindow();
			break;
		case R.id.btn_exchange_property2:
			toProperty();
			break;
		case R.id.btn_exchange_submit:
			submitReply();
			break;
		case R.id.btn_exchange_add:
			add();
			break;
		case R.id.btn_topic_back:
			this.finish();
			break;
		case R.id.ed_exchange_comment:
			more.setVisibility(View.GONE);
			break;

		case R.id.btn_take_picture:
			Intent photo = new Intent(ExchangeActivity.this, WechatPhotoSelectActivity.class);
			photo.putExtra("isFirst", true);
			startActivityForResult(photo, 500);
			more.setVisibility(View.GONE);
			break;
		case R.id.btn_file:
			// selectFileFromLocal();
			// more.setVisibility(View.GONE);
			break;

		case R.id.btn_picture:// 相册选择图片
			Intent intent = new Intent(getApplicationContext(), PhotoAlbumActivity.class);
			startActivityForResult(intent, 100);
			more.setVisibility(View.GONE);
			break;
		case R.id.btn_voice:
			voice();
			more.setVisibility(View.GONE);
			break;
		case R.id.btn_exchange_cancel_2:
			cancelSendComment();
			break;
		case R.id.btn_exchange_submit_2:
			submitComment();
			break;

		default:
			break;
		}

	}

	public void add() {
		/**
		 * 点击加号事件
		 */
		if (contentFragments == null) {
			contentFragments = new ContentFragments();
			contentFragments.setExchangeActivity(ExchangeActivity.this);
			contentFragments.setContentFragmentSelectCheck(ExchangeActivity.this);
		}
		if (!contentFragments.isAdded()) {
			contentFragments.setVideoViewList(videoViewList);
			contentFragments.setVideoPathList(videoPathList);
			contentFragments.setVoiceViewList(voiceViewList);
			contentFragments.setVoicePathList(voicePathList);
			contentFragments.setFileViewList(fileViewList);
			contentFragments.setFilePathList(filePathList);
			contentFragments.setPhotoViewList(photoViewList);
			contentFragments.setPhotoPathList(photoPathList);
			contentFragments.setWechatMsg(ed_exchange_comment.getText().toString());
			contentFragments.show(getFragmentManager(), TAG);
		}
	}

	/**
	 * 取消评论
	 */
	private void cancelSendComment() {
		PublicUtils.hideKeyboard(this);
		// ll_send_2.setVisibility(View.GONE);
		// ed_exchange_comment_2.setText("");
		// ed_exchange_comment_2.setHint("");
		// ll_send.setVisibility(View.VISIBLE);
	}

	public ContentFragments contentFragments;

	public void dismisContentFragment() {
		if (contentFragments != null) {
			contentFragments.dismiss();
		}
		contentFragments = null;
	}

	/**
	 * 保存录音文件
	 */

	private String voiceFileName;

	public void saveVoice(String name) {
		voiceResult(name);
	}

	public void backVoice() {
		if (contentFragments == null) {
			contentFragments = new ContentFragments();
			contentFragments.setExchangeActivity(ExchangeActivity.this);
		}
		if (!contentFragments.isAdded()) {
			contentFragments.setVideoViewList(videoViewList);
			contentFragments.setVideoPathList(videoPathList);
			contentFragments.setVoiceViewList(voiceViewList);
			contentFragments.setVoicePathList(voicePathList);
			contentFragments.setFileViewList(fileViewList);
			contentFragments.setFilePathList(filePathList);
			contentFragments.setPhotoViewList(photoViewList);
			contentFragments.setPhotoPathList(photoPathList);
			contentFragments.setWechatMsg(ed_exchange_comment.getText().toString());
			contentFragments.show(getFragmentManager(), TAG);
		}
	}

	public void voiceResult(String name) {
		if (contentFragments == null) {
			contentFragments = new ContentFragments();
			contentFragments.setExchangeActivity(ExchangeActivity.this);
		}
		if (!contentFragments.isAdded()) {
			contentFragments.setVideoViewList(videoViewList);
			contentFragments.setVideoPathList(videoPathList);
			contentFragments.setVoiceViewList(voiceViewList);
			contentFragments.setVoicePathList(voicePathList);
			contentFragments.setFileViewList(fileViewList);
			contentFragments.setFilePathList(filePathList);
			contentFragments.setPhotoViewList(photoViewList);
			contentFragments.setPhotoPathList(photoPathList);
			contentFragments.setWechatMsg(ed_exchange_comment.getText().toString());
			contentFragments.show(getFragmentManager(), TAG);
			contentFragments.voiceForResult(Constants.RECORD_PATH + name);
		}
	}

	/**
	 * 录音
	 */
	private WechatVoicePopupWindow recordPopupWindow;

	public void voice() {
		dismisContentFragment();
		recordPopupWindow = new WechatVoicePopupWindow(this);
		recordPopupWindow.setFileName(voiceFileName);
		recordPopupWindow.show(ll_send);
	}

	private void toProperty() {
		Intent intent = new Intent();
		intent.putExtra("topicId", topicId);
		intent.putExtra("groupId", groupId);
		intent.putExtra("isHistory", isHistory);
		intent.setClass(ExchangeActivity.this, PropertyActivity.class);
		startActivityForResult(intent, RESULT_CODE);
	}

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

		window.showAsDropDown(btn_exchange_notice, 0, 0);

		viewFlipper = (MyViewFilpper) view.findViewById(R.id.viewFlipper);
		// 往viewFlipper添加View

		topicNotify.setContent("");

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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			unregisterReceiver(wechat_broadcast_receiver);
		} catch (Exception e) {

		}
	}

	public void registSubmitSuccessReceiver() {
		IntentFilter filter = new IntentFilter(Constants.BROADCAST_WECHAT_REPLY);
		registerReceiver(wechat_broadcast_receiver, filter);
	}

	private BroadcastReceiver wechat_broadcast_receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent != null && Constants.BROADCAST_WECHAT_REPLY.equals(intent.getAction())) {
				replyListAll.clear();
				replyListAll = replyDB.findReplyListByTopicId(topicId);
				for (int i = 0; i < replyListAll.size(); i++) {
					Reply reply = replyListAll.get(i);
					if (reply != null) {
						reply.setIsRead(1);
						replyDB.updateReply(reply);
					}

				}

				exchangeAdapter.setReplyList(replyListAll);
				exchangeAdapter.refresh(replyListAll);
				scrollToBottom();
			}
		}

	};

	/**
	 * 滑动到最低端
	 */
	private ListView cListView;
	private boolean isBottom=false;
	private static int msgPosition;
	public void scrollToBottom() {
		cListView = lv_exchange_chat.getRefreshableView();
		if (cListView != null) {
			cListView.setSelection(cListView.getCount() - 1);// 发送一条消息时，ListView显示选择最后一项
		   msgPosition=cListView.getCount()-1;
		}
	}
	public void scrollToTop() {
		cListView = lv_exchange_chat.getRefreshableView();
		if (cListView != null) {
			cListView.setSelection(0);// 发送一条消息时，ListView显示选择最后一项
		}
	}
	@Override
	public void keyboardControll(boolean isClose, WechatView wechatView) {
		if (isClose) {// 关闭键盘
			if (wechatView == null) {
				ed_exchange_comment.setText("");
				ed_exchange_comment.setHint("");
				PublicUtils.hideKeyboard(this);
			} else {
				ed_exchange_comment_2.setText("");
				ed_exchange_comment_2.setHint("");
			}

		} else {// 弹出键盘
			if (wechatView == null) {
				ed_exchange_comment.setFocusable(true);
				ed_exchange_comment.setFocusableInTouchMode(true);
				ed_exchange_comment.requestFocus();
				InputMethodManager inputManager = (InputMethodManager) ed_exchange_comment.getContext()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(ed_exchange_comment, 0);
			} else {
				ed_exchange_comment_2.setFocusable(true);
				ed_exchange_comment_2.setFocusableInTouchMode(true);
				ed_exchange_comment_2.requestFocus();
				InputMethodManager inputManager = (InputMethodManager) ed_exchange_comment_2.getContext()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(ed_exchange_comment_2, 0);
			}
		}
	}

	private void keyBoardState() {
		ed_exchange_comment.getViewTreeObserver()
				.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						if (!isKeyboardShown(ed_exchange_comment.getRootView())) {// 隐藏键盘
							ed_exchange_comment.setHint("");
							ed_exchange_comment_2.setHint("");
							ll_send_2.setVisibility(View.GONE);
							ll_send.setVisibility(View.VISIBLE);
						} else {
							if (currentWechatView != null) {// 评论弹出键盘
								ll_send_2.setVisibility(View.VISIBLE);
								ll_send.setVisibility(View.GONE);
								ed_exchange_comment_2.setFocusable(true);
								ed_exchange_comment_2.setFocusableInTouchMode(true);
								ed_exchange_comment_2.requestFocus();
							} else {// 回帖弹出键盘
								ll_send_2.setVisibility(View.GONE);
								ll_send.setVisibility(View.VISIBLE);
								ed_exchange_comment.setFocusable(true);
								ed_exchange_comment.setFocusableInTouchMode(true);
								ed_exchange_comment.requestFocus();
							}
						}

					}
				});
	}

	private boolean isKeyboardShown(View rootView) {
		/*
		 * 128dp = 32dp * 4, minimum button height 32dp and generic 4 rows soft
		 * keyboard
		 */
		final int SOFT_KEYBOARD_HEIGHT_DP_THRESHOLD = 128;

		Rect r = new Rect();
		rootView.getWindowVisibleDisplayFrame(r);
		DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
		/*
		 * heightDiff = rootView height - status bar height (r.top) - visible
		 * frame height (r.bottom - r.top)
		 */
		int heightDiff = rootView.getBottom() - r.bottom;
		/* Threshold size: dp to pixels, multiply with display density */
		boolean isKeyboardShown = heightDiff > SOFT_KEYBOARD_HEIGHT_DP_THRESHOLD * dm.density;

		return isKeyboardShown;
	}

	@Override
	public void commentSelect(String hint, WechatView wechatView) {
		currentWechatView = wechatView;
		ll_send.setVisibility(View.GONE);
		ll_send_2.setVisibility(View.VISIBLE);
		ed_exchange_comment_2.setFocusable(true);
		ed_exchange_comment_2.setFocusableInTouchMode(true);
		ed_exchange_comment_2.requestFocus();
		ed_exchange_comment_2.setHint(hint);
		InputMethodManager inputManager = (InputMethodManager) ed_exchange_comment_2.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.showSoftInput(ed_exchange_comment_2, 0);
	}

	/**
	 * 附件选择完毕
	 */

	private Dialog fileDialog;

	private void fileResult(final File srcFile) {
		if (fileDialog == null) {
			fileDialog = new MyProgressDialog(this, R.style.CustomProgressDialog, PublicUtils.getResourceString(this,R.string.wechat_content79));
		}
		if (fileDialog != null && !fileDialog.isShowing()) {
			fileDialog.show();
		}
		new Thread() {
			public void run() {
				try {
					String filePath = srcFile.getAbsolutePath();
					if (!TextUtils.isEmpty(filePath)) {
						int index = filePath.lastIndexOf("/");
						String name = filePath.substring(index + 1);
						FileHelper helper = new FileHelper();
						// byte[] b = helper.readImageByte(filePath);
						// byte[] c = helper.readImageByte(filePath);
						// helper.saveWeReply(b,
						// name,Constants.WECHAT_PATH_LOAD);//查看用
						// helper.saveWeReply(c,
						// name,Constants.WECHAT_PATH);//提交用

						String[] fileStr = name.split("\\.");
						String newName = String.valueOf(System.currentTimeMillis()) + "." + fileStr[1];
						SharedPrefrencesForWechatUtil.getInstance(ExchangeActivity.this).setFileName(newName, name);

						helper.copyFile(filePath, Constants.WECHAT_PATH_LOAD + newName);
						helper.copyFile(filePath, Constants.WECHAT_PATH + newName);

						Message msg = fileHander.obtainMessage();
						msg.what = 1;
						msg.obj = newName;
						fileHander.sendMessage(msg);

						// JSONObject aObj = null;
						// if (TextUtils.isEmpty(atachmentPath)) {
						// aObj = new JSONObject();
						// }else{
						// aObj = new JSONObject(atachmentPath);
						// }
						// aObj.put(name, filePath);
						// atachmentPath = aObj.toString();
					}
					JLog.d(TAG, "附件:" + atachmentPath);
				} catch (Exception e) {
					e.printStackTrace();
					fileHander.sendEmptyMessage(2);
				}
			};
		}.start();
	}

	private Handler fileHander = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
			if (fileDialog != null && fileDialog.isShowing()) {
				fileDialog.dismiss();
			}
			switch (what) {
			case 1:
				try {
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 2:
				ToastOrder.makeText(ExchangeActivity.this, R.string.wechat_content70, ToastOrder.LENGTH_SHORT).show();
				break;
			case 3:
				ToastOrder.makeText(getApplicationContext(), R.string.un_support_type, ToastOrder.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 确定选中的消息
	 */
	public String replyPhoto;
	public String atachmentPath;

	public void confrimMessage(List<String> photoList, List<String> voiceList, List<String> fileList,
			List<String> videoList, String wechatMsg) {
		replyPhoto = "";
		atachmentPath = "";
		this.photoPathList = photoList;
		this.voicePathList = voiceList;
		this.filePathList = fileList;
		this.videoPathList = videoList;
		if (!photoPathList.isEmpty()) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < photoPathList.size(); i++) {
				String path = photoPathList.get(i);
				int index = path.lastIndexOf("/");
				String fileName = path.substring(index + 1);
				sb.append(",").append(fileName);
			}
			if (sb.length() > 0) {
				replyPhoto = sb.substring(1);
			} else {
				replyPhoto = "";
			}
		} else {
			replyPhoto = "";
		}

		if (!voicePathList.isEmpty()) {
			try {
				JSONObject aObj = null;
				if (TextUtils.isEmpty(atachmentPath)) {
					aObj = new JSONObject();
				} else {
					aObj = new JSONObject(atachmentPath);
				}

				for (int i = 0; i < voicePathList.size(); i++) {
					String path = voicePathList.get(i);
					int index = path.lastIndexOf("/");
					String fileName = path.substring(index + 1);
					aObj.put(fileName, Constants.RECORD_PATH + fileName);
				}
				atachmentPath = aObj.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (!filePathList.isEmpty()) {
			try {
				JSONObject aObj = null;
				if (TextUtils.isEmpty(atachmentPath)) {
					aObj = new JSONObject();
				} else {
					aObj = new JSONObject(atachmentPath);
				}

				for (int i = 0; i < filePathList.size(); i++) {
					String path = filePathList.get(i);
					int index = path.lastIndexOf("/");
					String fileName = path.substring(index + 1);
					aObj.put(fileName, Constants.WECHAT_PATH + fileName);
				}
				atachmentPath = aObj.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (!videoPathList.isEmpty()) {
			try {
				JSONObject aObj = null;
				if (TextUtils.isEmpty(atachmentPath)) {
					aObj = new JSONObject();
				} else {
					aObj = new JSONObject(atachmentPath);
				}

				for (int i = 0; i < videoPathList.size(); i++) {
					String path = videoPathList.get(i);
					int index = path.lastIndexOf("/");
					String fileName = path.substring(index + 1);
					aObj.put(fileName, Constants.WECHAT_PATH + fileName);
				}
				atachmentPath = aObj.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (!TextUtils.isEmpty(wechatMsg)) {
			ed_exchange_comment.setText(wechatMsg);
		}

		videoViewList = contentFragments.videoViewList;
		voiceViewList = contentFragments.voiceViewList;
		photoViewList = contentFragments.photoViewList;
		fileViewList = contentFragments.fileViewList;
	}

	@Override
	public void startGroupUserActivity(int replyId) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this,GroupUserActivity.class);
		intent.putExtra("groupId", groupId);//所在话题小组成员的Id
		intent.putExtra("replyId", replyId);
		intent.putExtra("getUser", ApprovalDialog.APPROVALDIALOG_FLAG);
		startActivityForResult(intent, 111);
	}
//	public interface ExchangeSetUser{
//		void setResultUserId(int id);//设置选择审核人id
//		void setResultUserName(String name);//设置选择审核人name
//	}

	@Override
	public void setResultUserId(Comment id) {
		// TODO Auto-generated method stub
		if (authUserId!=0) {
			id.setAuthUserId(authUserId);
		}
		
	}

	@Override
	public void setResultUserName(Comment name) {
		// TODO Auto-generated method stub
		if (authUserName!=null) {
			name.setAuthUserName(authUserName);
		}
	}

	@Override
	public void setResultShowName(EditText et) {
		// TODO Auto-generated method stub
//		if (authUserName!=null) {
//			et.setText(authUserName);
//		}
		this.et=et;
	}

	@Override
	public void toGroupUserActivity() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this,GroupUserActivity.class);
		intent.putExtra("groupId", groupId);
		intent.putExtra("getUser", ContentFragments.CONTENTFRAGMENTS_FLAG);
		startActivityForResult(intent, ContentFragments.CONTENTFRAGMENTS_FLAG);
	}

	@Override
	public void setEditTextContent(EditText et) {
		// TODO Auto-generated method stub
		this.contentFragmentEdit=et;
	}


}
