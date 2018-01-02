package com.yunhu.yhshxc.wechat.exchange;

import gcg.org.debug.JLog;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.wechat.Util.SharedPrefrencesForWechatUtil;
import com.yunhu.yhshxc.wechat.Util.WechatUtil;
import com.yunhu.yhshxc.wechat.approval.ApprovalDialog;
import com.yunhu.yhshxc.wechat.bo.Comment;
import com.yunhu.yhshxc.wechat.bo.Reply;
import com.yunhu.yhshxc.wechat.bo.Topic;
import com.yunhu.yhshxc.wechat.bo.WechatAttachment;
import com.yunhu.yhshxc.wechat.bo.Zan;
import com.yunhu.yhshxc.wechat.db.CommentDB;
import com.yunhu.yhshxc.wechat.db.ZanDB;
import com.yunhu.yhshxc.wechat.fragments.UserInfomationActivity;
import com.yunhu.yhshxc.wechat.survey.MyGridView;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class WechatView implements OnClickListener {
	private final String TAG = "WechatView";
	private View view;
	private Context context;
	public List<Zan> zanList = new ArrayList<Zan>();
	public List<Comment> commentList = new ArrayList<Comment>();
	private MyGridView gv_chat_view;
	private LinearLayout ll_more_comment;
	private Boolean if_more_comment = false;
	private Boolean isMyself = false;
	private ExchangeActivity exchangeActivity;
	public Reply reply;
	private ZanDB zanDB;
	private CommentDB  commentDB;
	public boolean isComment = false;//标识是否是评论 true表示是回帖 false 表示是评论
	public Comment bComment;//被评论的conment数据
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;
	public KeyboardControllListener keyboardControllListener;
	public CommentSelectListener commentSelectListener;
	private Topic topic = new Topic();
	public WechatView(Context context) {
		this.context = context;
		zanDB = new ZanDB(context);
		commentDB = new CommentDB(context);
		exchangeActivity = (ExchangeActivity)context;
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.people_65)
		.showImageForEmptyUri(R.drawable.people_65)
		.showImageOnFail(R.drawable.people_65)
		.cacheOnDisk(true)
		.cacheInMemory(true).displayer(new RoundedBitmapDisplayer(10))
		.build();
	}
	
	
	
	public void setCommentSelectListener(CommentSelectListener commentSelectListener) {
		this.commentSelectListener = commentSelectListener;
	}



	public void setKeyboardControllListener(
			KeyboardControllListener keyboardControllListener) {
		this.keyboardControllListener = keyboardControllListener;
	}



	public View getView() {
		return view;
	}

	public void setExchangeView(ExchangeActivity exchangeActivity) {
		this.exchangeActivity = exchangeActivity;
	}
	
	public void setRepliesAuth(Topic topic){
		this.topic = topic;
	}
	
	/**
	 * 刷新view
	 * @param exchangeWechat
	 * @param commentList
	 * @param zanList
	 * @throws ParseException
	 */
	public void setExchangeWechat(Reply exchangeWechat,
			List<Comment> commentList, List<Zan> zanList) throws ParseException {
		this.zanList = zanList;
		this.commentList = commentList;
		this.reply = exchangeWechat;
		if (SharedPreferencesUtil.getInstance(context).getUserId() == exchangeWechat.getUserId()) {
			isMyself = true;
		} else {
			isMyself = false;
		}
		setIsSelf(isMyself);
		
		tv_chat_name.setText(reply.getReplyName());
		String content = exchangeWechat.getContent();
		if (TextUtils.isEmpty(content)) {
			tv_chat_content.setVisibility(View.GONE);
		}else{
			tv_chat_content.setVisibility(View.VISIBLE);
			tv_chat_content.setText(content);
		}
		tv_zan.setText(zanName(zanList));
		String time = PublicUtils.compareDate(exchangeWechat.getDate());
	    tv_reply_date.setText(time);
		if (commentList.size() > 0) {// 判断是否有评论
			ll_reply.removeAllViews();
			for (int i = 0; i < commentList.size(); i++) {
				ll_reply.setVisibility(View.VISIBLE);
				iv_jianjiao.setVisibility(View.VISIBLE);
				if(zanList.size() > 0){
					tv_line.setVisibility(view.VISIBLE);
				}
				Comment comment  = commentList.get(i);
				CommentViewItem item = new CommentViewItem(context);
				View commentView = item.getCommentView(comment);
//				item.setInputEditText(et);
				item.setWechatView(this);
				item.setExchangeActivity(exchangeActivity);
				ll_reply.addView(commentView);
			}
			if (if_more_comment) {// 如果评论超过五条
				ll_more_comment.setVisibility(View.VISIBLE);
			}
		}
//		rl_zan_counts.setOnClickListener(this);
			
		btn_zan.setOnClickListener(this);
		btn_comment.setOnClickListener(this);
		if (zanList.size() > 0) {
//			rl_zan_counts.setVisibility(View.VISIBLE);
		}
		
		boolean isPrivate = reply.isPrivate();
		if (isPrivate) {
			ll_pinglun_zan.setVisibility(View.GONE);
		}
		if (exchangeActivity!=null && exchangeActivity.isCloseTopic) {
			ll_pinglun_zan.setVisibility(View.GONE);
		}

	}

	/**
	 * 设置是自己发言还是收到信息
	 * @param isSelf true表示是自己发言 false表示是收到信息
	 */
	
	private ImageView iv_chat;//头像
	private TextView tv_chat_name;//名称
	private TextView tv_chat_content;//评论内容
	private TextView tv_reply_date;//回帖日期
	private LinearLayout btn_comment,btn_zan;//评论和点赞按钮
	private TextView tv_zan,tv_comment;//有哪些人点了赞
	private LinearLayout ll_zan;//控制赞的显示 隐藏
	private ImageView iv_jianjiao;//控制尖角的现实隐藏
	private LinearLayout ll_reply;//添加回帖
	private ImageView iv_comment;//评论、审批
	private boolean canZan = true;//是否可以点赞 true可以false不可以
	private TextView tv_line;//点赞分割线
	private LinearLayout ll_chat_file;
	private LinearLayout ll_pinglun_zan;
	
	
	private void setIsSelf(Boolean isSelf) {
		if (view == null) {
			if (isSelf) {
				if (view == null ) {
					view = View.inflate(context, R.layout.chat_view_right, null);
				}
			}else{
				if (view == null) {
					view = View.inflate(context, R.layout.chat_view_left, null);
				}
			}
			ll_chat_file = (LinearLayout)view.findViewById(R.id.ll_chat_file);
			gv_chat_view = (MyGridView)view.findViewById(R.id.gv_chat_view);
			ll_reply = (LinearLayout) view.findViewById(R.id.ll_reply);
			iv_chat = (ImageView)view.findViewById(R.id.iv_chat);
			iv_chat.setOnClickListener(this);
			tv_chat_name = (TextView)view.findViewById(R.id.tv_chat_name);
			tv_chat_content = (TextView)view.findViewById(R.id.tv_chat_content);
			tv_reply_date = (TextView)view.findViewById(R.id.tv_reply_date);
			btn_comment = (LinearLayout)view.findViewById(R.id.btn_comment);
			btn_comment.setOnClickListener(this);
			btn_zan = (LinearLayout)view.findViewById(R.id.btn_zan);
			btn_zan.setOnClickListener(this);
			iv_comment = (ImageView) view.findViewById(R.id.iv_comment);
			tv_comment = (TextView) view.findViewById(R.id.tv_comment);
			tv_zan = (TextView) view.findViewById(R.id.tv_zan);
			ll_zan = (LinearLayout) view.findViewById(R.id.ll_zan);
			iv_jianjiao = (ImageView) view.findViewById(R.id.iv_jianjiao);
			tv_line = (TextView) view.findViewById(R.id.tv_line);
			ll_pinglun_zan = (LinearLayout) view.findViewById(R.id.ll_pinglun_zan);
			
			if (reply.isPrivate()) {
				ll_pinglun_zan.setVisibility(View.GONE);
			}else{
				if(!isSelf){
					if (Topic.REPLY_2 == topic.getComment()) {//只能创建人对话题进行评论
						if (topic.getCreateUserId() == SharedPreferencesUtil.getInstance(context).getUserId()) {
							ll_pinglun_zan.setVisibility(View.VISIBLE);
						}else{
							ll_pinglun_zan.setVisibility(View.GONE);
						}
					}
				}
			}
			String headUrl = SharedPrefrencesForWechatUtil.getInstance(context).getUserHeadImg(String.valueOf(reply.getUserId()));
			if(!TextUtils.isEmpty(headUrl)){
				imageLoader.displayImage(headUrl, iv_chat, options, null);
			}
			
			if (!reply.isPrivate()) {
				if(1 == exchangeActivity.topic.getIsClose() || 1 == exchangeActivity.isHistory ){
					ll_pinglun_zan.setVisibility(View.GONE);
				}
			}

//			if(!isSelf){
//				if (Topic.REPLY_2 == topic.getComment()) {//只能创建人对话题进行评论
//					if (topic.getCreateUserId() == SharedPreferencesUtil.getInstance(context).getUserId()) {
//						iv_comment.setBackgroundResource(R.drawable.shenhe_wechat);
//						tv_comment.setText("审批");
//						btn_comment.setVisibility(View.INVISIBLE);
//					}else{
//						iv_comment.setVisibility(View.GONE);
//						btn_comment.setVisibility(View.INVISIBLE);
//					}
//				}
//			}
		
			
			if (exchangeActivity.isApproval) {
		
				iv_comment.setBackgroundResource(R.drawable.shenhe_wechat);
				tv_comment.setText(R.string.approval_exa);
				btn_comment.setVisibility(View.INVISIBLE);

				if(!isSelf){
					if (Topic.REPLY_2 == topic.getComment()) {//只能创建人对话题进行评论
						if (!(topic.getCreateUserId() == SharedPreferencesUtil.getInstance(context).getUserId())) {
							iv_comment.setVisibility(View.GONE);
							tv_comment.setVisibility(View.GONE);
						}
					}
				}
			}
			addFile();
		}
	}
	
	/**
	 * 添加附件VIEW
	 */
	private void addFile(){
		try {
			if (reply!=null) {
				String filePath = reply.getAttachment();
				if (!TextUtils.isEmpty(filePath)) {
					JSONObject  attObj = new JSONObject(filePath);
					Iterator<String> iterator = attObj.keys();
					while (iterator.hasNext()) {
						String key = iterator.next();
						String url = attObj.getString(key);
						if (!TextUtils.isEmpty(url)) {
							String[] urlS = url.split(",");
							for (int i = 0; i < urlS.length; i++) {
								WechatAttachment a = new WechatAttachment();
								if (reply.getUserId() == SharedPreferencesUtil.getInstance(context).getUserId()) {
									String showName = SharedPrefrencesForWechatUtil.getInstance(context).getGetFileName(key);
									if (!TextUtils.isEmpty(showName)) {
										key = showName;
									}
								}
								a.setName(key);
								a.setUrl(urlS[i]);
								WechatAttachmentView view = new WechatAttachmentView(context);
								view.setData(a);
								view.setShowVoiceReferView(exchangeActivity.ll_send);
								ll_chat_file.addView(view.getView());
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 设置图片
	 */
	public  void setImageGridView(){
		if (reply!=null) {
			String images = reply.getPhoto();
			if (!TextUtils.isEmpty(images)) {
				WechatImageAdapter adapter = new WechatImageAdapter(context);
				List<String> imageList = new ArrayList<String>();
				String[] str = images.split(",");
				for (int i = 0; i < str.length; i++) {
					imageList.add(str[i]);
				}
				adapter.setImageSrcList(imageList);
				adapter.setReply(reply);
				gv_chat_view.setAdapter(adapter);
			}
		}
	}

	/**
	 * 设置点赞人名称
	 */
	private List<String> tempZan = new ArrayList<String>();
	private String zanName(List<Zan> list) {
		String str="";
		StringBuffer zanNames = new StringBuffer();
		tempZan.clear();
		for (int i = 0; i < zanList.size(); i++) {
			String zanName = zanList.get(i).getUserName();
			if (!tempZan.contains(zanName)) {
				tempZan.add(zanName);
				zanNames.append( "、" +zanName );

			}
		}
		if (list==null || list.isEmpty()) {
			ll_zan.setVisibility(View.GONE);
			iv_jianjiao.setVisibility(View.GONE);
//			tv_comment.setVisibility(View.GONE);
		}else{
			if(commentList.size() > 0){
//				tv_comment.setVisibility(View.VISIBLE);
			}
			ll_zan.setVisibility(View.VISIBLE);
			iv_jianjiao.setVisibility(View.VISIBLE);
		}
		if (zanNames.length()>0) {
			str = zanNames.substring(1);
		}
		return str;
	}

	/**
	 * 
	 * @param zan 点赞提交的时候的参数
	 * @return
	 */
	private RequestParams zanParams(Zan zan) {
		RequestParams params = new RequestParams();
		params.put("phoneno", PublicUtils.receivePhoneNO(context));
		WechatUtil wechatUtil = new WechatUtil(context);
		try {
			JLog.d("abby", "data" + wechatUtil.submitZanJson(zan));
			params.put("data", wechatUtil.submitZanJson(zan));

		} catch (JSONException e) {
			e.printStackTrace();
		}
		JLog.d("abby", "params:" + params.toString());
		return params;
	}

	/**
	 * 提交赞
	 * @param zan
	 */
	private void submitZan(final Zan zan) {
		String url = UrlInfo.doWeChatPointInfo(context);
		GcgHttpClient.getInstance(context).post(url, zanParams(zan),
				new HttpResponseListener() {

					@Override
					public void onSuccess(int statusCode, String content) {
						try {
							JSONObject obj = new JSONObject(content);
							String resultcode = obj.getString("resultcode");
							JLog.d("abby", resultcode);
							if ("0000".equals(resultcode)) {
								if (PublicUtils.isValid(obj, "repliesId")) {
									String repliesId = obj.getString("repliesId");
									zan.setReplayId(Integer.parseInt(repliesId));
								}

								if (PublicUtils.isValid(obj, "userId")) {
									String value = obj.getString("userId");
									zan.setUserId(Integer.parseInt(value));
								}
								
								if (PublicUtils.isValid(obj, "topicId")) {
									String value = obj.getString("topicId");
									zan.setTopicId(Integer.parseInt(value));
								}
								
								if (PublicUtils.isValid(obj, "userName")) {
									String value = obj.getString("userName");
									zan.setUserName(value);
								}
								
								if (PublicUtils.isValid(obj, "pointId")) {
									String value = obj.getString("pointId");
									zan.setZanId(Integer.parseInt(value));
								}
								Zan z = zanDB.findZan(zan.getTopicId(), zan.getReplayId(), zan.getZanId());
								if (z==null) {
									zanDB.insert(zan);
								}
//								Toast.makeText(context, "点赞成功！",Toast.LENGTH_LONG).show();
							} else {
								throw new Exception();
							}
						} catch (Exception e) {
						}
					}

					@Override
					public void onStart() {
						if (!zanList.contains(zan)) {
							zanList.add(zan);
							tv_zan.setText(zanName(zanList));
						}
					}

					@Override
					public void onFinish() {
						canZan = true;
					}

					@Override
					public void onFailure(Throwable error, String content) {
					}
				});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_zan_counts:
//			tv_exchange_names.setVisibility(View.GONE);
//			tv_exchange_names_all.setVisibility(View.VISIBLE);
//			tv_exchange_names_all.setText(zanName(zanList));
//			tv_zan_counts.setVisibility(View.GONE);
			break;

		case R.id.btn_zan:
			if(exchangeActivity.isApproval) {//审批
				Comment comment = commentDB.findRecentCommentByRplayId(reply.getReplyId());//取最后一个评论者
				Comment comment1 = commentDB.findRecentCommentByAuthUserId(reply.getReplyId());//取最后一个评论者指定审核人	
				
				if(reply.getAuthUserId()==0){//发帖没有指定
					
						if(comment==null){//没有评论
							approval();	
						}else{
							if(comment1==null){//评论中无审核人
								approval();		
							}else{//评论中有审核人
								if(comment1.getAuthUserId()==SharedPreferencesUtil.getInstance(context).getUserId()){//评论中审核人是自己
									approval();	
								}else{
									Toast.makeText(context, R.string.wechat_content47, 1).show();
								}
							}
	
					}
				}else{//有指定
					if(comment1==null){//评论中无审核人
						if(reply.getAuthUserId()==SharedPreferencesUtil.getInstance(context).getUserId()){//指定审核人是自己
							approval();	
						}else{
							Toast.makeText(context, R.string.wechat_content47, 1).show();
						}
					}else{//评论中有审核人
						if(comment1.getAuthUserId()==SharedPreferencesUtil.getInstance(context).getUserId()){//评论中审核人是自己
							approval();	
						}else{
							Toast.makeText(context, R.string.wechat_content47, 1).show();
						}
					}	
				}

				
			}else{
				if (canZan) {
					zan();
				}
			}
			break;
			
		case R.id.iv_chat:
			int userId = reply.getUserId();
			userInfo(String.valueOf(userId));
			break;
		case R.id.btn_comment:
			isComment = true;
//			exchangeActivity.ed_exchange_comment.setHint("我 回复" + reply.getReplyName());
//			exchangeActivity.btn_exchange_add.setVisibility(View.GONE);
//			exchangeActivity.currentWechatView = this;
//			if (et != null) {
//				et.setFocusable(true);
//				et.setFocusableInTouchMode(true);
//				et.requestFocus();
//				InputMethodManager inputManager =(InputMethodManager) et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//				inputManager.showSoftInput(et, 0);
//			}
			keyboardControllListener.keyboardControll(false,this);//弹出键盘
			commentSelectListener.commentSelect(PublicUtils.getResourceString(context,R.string.wechat_content46) + reply.getReplyName(),this);
			break;
		default:
			break;
		}
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
	
	private void zan(){
		canZan = false;
		Zan z = zanDB.findZanByUserId(reply.getTopicId(), reply.getReplyId(), SharedPreferencesUtil.getInstance(context).getUserId());
		if (z!=null) {
			ToastOrder.makeText(context, R.string.wechat_content44, ToastOrder.LENGTH_SHORT).show();
		}else{
			// 点赞
			Zan zan = new Zan();
			zan.setTopicId(reply.getTopicId());
			zan.setReplayId(reply.getReplyId());
			zan.setUserId(SharedPreferencesUtil.getInstance(context).getUserId());
			zan.setUserName(SharedPreferencesUtil.getInstance(context).getUserName());
			zan.setDate(DateUtil.getCurDateTime());
			submitZan(zan);
		}
		
	}
	
	/**
	 * 审批
	 */
	private void approval(){
		isComment = true;
//		if (et != null) {
//			et.setFocusable(true);
//			et.setFocusableInTouchMode(true);
//			et.requestFocus();
//			InputMethodManager inputManager =(InputMethodManager) et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//			inputManager.showSoftInput(et, 0);
//		}
		keyboardControllListener.keyboardControll(false,this);//弹出键盘
//		exchangeActivity.currentWechatView = this;
		ApprovalDialog dialiog = new ApprovalDialog(context,exchangeActivity.topic);
		dialiog.setTitle(R.string.str_wechat_comment_title);
		dialiog.setWechatView(this);
		dialiog.setReplyId(reply.getUserId());
		dialiog.show();
		dialiog.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				isComment = false;
			}
		});
	}
	/**
	 * 提交评论
	 */
	public void submitComment(final String commentStr) {
		String url = UrlInfo.doWebRepliesInfo(context);
		final Comment comment = new Comment();
		comment.setcUserId(SharedPreferencesUtil.getInstance(context).getUserId());
		comment.setcUserName(SharedPreferencesUtil.getInstance(context).getUserName());
		if (bComment!=null) {
			comment.setdUserId(bComment.getcUserId());
			comment.setdUserName(bComment.getcUserName());
		}
		comment.setMsgKey(PublicUtils.chatMsgKey(context));
		comment.setComment(commentStr);
		comment.setDate(DateUtil.getCurDateTime());
		comment.setReplyId(reply.getReplyId());
		
		GcgHttpClient.getInstance(context).post(url, paramsComment(comment),new HttpResponseListener() {

					@Override
					public void onSuccess(int statusCode, String content) {
						JLog.d(TAG, "content:" + content);
						isComment = false;
						try {
							JSONObject obj = new JSONObject(content);
							String resultcode = obj.getString("resultcode");
							String pathCode = obj.getString("pathCode");
							String repliesId = obj.getString("repliesId");
							String topId = obj.getString("topId");
							if ("0000".equals(resultcode)) {
								ToastOrder.makeText(context,R.string.wechat_content45, ToastOrder.LENGTH_SHORT).show();
								comment.setMsgKey(PublicUtils.chatMsgKey(context));
								comment.setTopicId(topId);
								comment.setPathCode(pathCode);
								comment.setComment(commentStr);
								comment.setDate(DateUtil.getCurDateTime());
								comment.setCommentId(Integer.parseInt(repliesId));
								comment.setIsSend(1);
								Comment c = commentDB.findCommentByCommentId(comment.getCommentId());
								if (c!=null) {
									commentDB.updateComment(c);
								}else{
									commentDB.insert(comment);
								}
							} else {
								throw new Exception();
							}
						} catch (Exception e) {
						}
					}

					@Override
					public void onStart() {
						keyboardControllListener.keyboardControll(true,WechatView.this);
						try {
							commentList.add(comment);
							setExchangeWechat(reply, commentList, zanList);
						} catch (Exception e) {
							e.printStackTrace();
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
	
	/**
	 * 提交评论的参数
	 * @param comment
	 * @return
	 */
	private RequestParams paramsComment(Comment comment) {
		RequestParams params = new RequestParams();
		try {
			params.put("phoneno", PublicUtils.receivePhoneNO(context));
			WechatUtil wechatUtil = new WechatUtil(context);
			if (bComment!=null) {//说明是评论其他评论
				params.put("data", wechatUtil.submitCommentJson(reply, comment.getMsgKey(),bComment.getMsgKey(),comment));
			}else{//说明直接评论回帖
				params.put("data", wechatUtil.submitCommentJson(reply, comment.getMsgKey(),reply.getMsgKey(),comment));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JLog.d(TAG, "params:" + params.toString());
		return params;
	}
	
	public Reply getReply() {
		return reply;
	}

	public void setReply(Reply reply) {
		this.reply = reply;
	}

	
	public List<Zan> getZanList() {
		return zanList;
	}

	public void setZanList(List<Zan> zanList) {
		this.zanList = zanList;
	}

	public List<Comment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<Comment> commentList) {
		this.commentList = commentList;
	}
	
	
//	public EditText et;
//	public void setEditText(EditText et) {
//		this.et = et;
//	}
}
