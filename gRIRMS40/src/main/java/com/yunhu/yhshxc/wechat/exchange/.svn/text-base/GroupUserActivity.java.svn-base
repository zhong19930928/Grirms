package com.yunhu.yhshxc.wechat.exchange;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import gcg.org.debug.JLog;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.wechat.Util.WechatUtil;
import com.yunhu.yhshxc.wechat.approval.ApprovalDialog;
import com.yunhu.yhshxc.wechat.bo.Comment;
import com.yunhu.yhshxc.wechat.bo.GroupUser;
import com.yunhu.yhshxc.wechat.bo.Reply;
import com.yunhu.yhshxc.wechat.content.ContentFragments;
import com.yunhu.yhshxc.wechat.db.GroupUserDB;
import com.yunhu.yhshxc.wechat.db.ReplyDB;
import com.loopj.android.http.RequestParams;

public class GroupUserActivity  extends AbsBaseActivity implements OnClickListener, OnItemClickListener{

	private ListView lv_group_user;
	private int groupId;
	private List<GroupUser> groupUsers;
	private GroupUserDB groupUserDB;
	private GroupUserAdapter groupUserAdapter;
	private Button btn_group_user_back;
	boolean isGetUser=false;
    boolean isFromApprovalDialog=false;
    boolean isFromExchangeAdapter=false;
    boolean isFromContentFragments=false;
    private int replyId=0;//发帖人id
    private Reply reply;
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_group_user);
	
		lv_group_user = (ListView) findViewById(R.id.lv_group_user);
		btn_group_user_back = (Button) findViewById(R.id.btn_group_user_back);
		btn_group_user_back.setOnClickListener(this);
		init();
	}
	
	private void init(){
		Intent comIntent =this.getIntent();
		if (comIntent.getIntExtra("getUser", 0)==ExchangeActivity.GETUSER_FLAG) {
			//说明这是来自ExchangeActivity@审核成员的请求
			isGetUser=true;
		}else if (comIntent.getIntExtra("getUser", 0)==ApprovalDialog.APPROVALDIALOG_FLAG) {
			//来自ApprovalDialog
			isFromApprovalDialog=true;
			replyId=comIntent.getIntExtra("replyId", 0);
		}else if (comIntent.getIntExtra("getUser", 0)==ExchangeAdapter.EXCHANGEADAPTER_FLAG) {//来自ExchangeAdapter
			//来自ExchangeAdapter的activity
			isFromExchangeAdapter=true;
			ReplyDB replyDB = new ReplyDB(this);
		     reply = replyDB.findReplyByReplyId(comIntent.getIntExtra("replyId", 0));
		}else if (comIntent.getIntExtra("getUser", 0)==ContentFragments.CONTENTFRAGMENTS_FLAG) {
			//这是来自于ContentFragments对话框的请求
			isFromContentFragments=true;
		}
		groupUserDB = new GroupUserDB(this);
		Intent intent = getIntent();
		groupId = intent.getIntExtra("groupId", groupId);
		
		groupUsers = groupUserDB.findGroupUserByGroupId(groupId);
		for (int i = 0; i <groupUsers.size(); i++) {
			if (groupUsers.get(i).getUserId()==SharedPreferencesUtil.getInstance(this).getUserId()) {
				groupUsers.remove(i);
			}
		}
		groupUserAdapter = new GroupUserAdapter(getApplicationContext(), groupUsers);
		lv_group_user.setAdapter(groupUserAdapter);
		lv_group_user.setOnItemClickListener(this);
	}
	
	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_group_user_back:
			this.finish();
			break;
	

		default:
			break;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		GroupUser selectUser=(GroupUser) groupUserAdapter.getItem(position);
		// 如果是来自选择审核人员,就响应点击事件,将点击的成员信息反馈回去,否则不做操作
		if (isGetUser) {
        if (selectUser.getUserId()!=0) {
			
				
			Intent intent = new Intent();
			intent.putExtra("userId", selectUser.getUserId());
			intent.putExtra("userName", selectUser.getUserName());
			setResult(ExchangeActivity.GROUPUSER_CODE,intent);//把获取的值返回给ExchangeActivity
			GroupUserActivity.this.finish();
		}else{
			Toast.makeText(this, R.string.wechat_content60, Toast.LENGTH_SHORT).show();
		}
		
		}
		if (isFromApprovalDialog) {
			if (replyId!=0&&selectUser.getUserId()!=replyId) {
			Intent intent = new Intent();
			intent.putExtra("userId", selectUser.getUserId());
			intent.putExtra("userName", selectUser.getUserName());
			setResult(ApprovalDialog.APPROVALDIALOG_FLAG,intent);//把获取的值返回给ExchangeActivity,来自ApprovalDialog 
			GroupUserActivity.this.finish();
			}else{
				Toast.makeText(this, R.string.wechat_content59, Toast.LENGTH_SHORT).show();
			}
		}
		if (isFromExchangeAdapter) {//选择审核人的数据提交
			if (reply!=null) {		
				 String url = UrlInfo.doWebRepliesInfo(GroupUserActivity.this);
				 Comment comment = new Comment();
				 comment.setcUserId(SharedPreferencesUtil.getInstance(GroupUserActivity.this).getUserId());
				 comment.setcUserName(SharedPreferencesUtil.getInstance(GroupUserActivity.this).getUserName());
				 if (reply.getUserId()==selectUser.getUserId()) {
					Toast.makeText(this, R.string.wechat_content59, Toast.LENGTH_SHORT).show();
					return;
				}
			     comment.setAuthUserId(selectUser.getUserId());
			     comment.setAuthUserName(selectUser.getUserName());
				 comment.setMsgKey(PublicUtils.chatMsgKey(this));			
				 comment.setDate(DateUtil.getCurDateTime());
				 comment.setReplyId(reply.getReplyId());
				 comment.setComment(PublicUtils.getResourceString(this,R.string.wechat_content58)+selectUser.getUserName());
			     GcgHttpClient.getInstance(this).post(url, paramsComment(comment),new HttpResponseListener() {
					
					@Override
					public void onSuccess(int statusCode, String content) {
						// TODO Auto-generated method stub
						Toast.makeText(GroupUserActivity.this, R.string.wechat_content57, Toast.LENGTH_SHORT).show();
					}
					
					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onFailure(Throwable error, String content) {
						// TODO Auto-generated method stub
						Toast.makeText(GroupUserActivity.this, R.string.toast_one7, Toast.LENGTH_SHORT).show();
						
					}
				});
			 	GroupUserActivity.this.finish();
			     
			
			}else{
//				Toast.makeText(this, "网速较慢,数据正在提交,请稍后选择!", Toast.LENGTH_SHORT).show();
//				this.finish();
				
			}
		}
		if (isFromContentFragments) {//如果是来自ContentFragments的请求
			Intent intent = new Intent();
			intent.putExtra("userId", selectUser.getUserId());
			intent.putExtra("userName", selectUser.getUserName());
			setResult(ContentFragments.CONTENTFRAGMENTS_FLAG,intent);//把获取的值返回给ExchangeActivity
			GroupUserActivity.this.finish();
		}
		
	}

	/**
	 * 提交评论的参数
	 * @param comment
	 * @return
	 */
	private RequestParams paramsComment(Comment comment) {
		RequestParams params = new RequestParams();
		try {
			params.put("phoneno", PublicUtils.receivePhoneNO(this));
			WechatUtil wechatUtil = new WechatUtil(this);
		
				params.put("data", wechatUtil.submitCommentJson(reply,comment.getMsgKey(),reply.getMsgKey(),comment));
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JLog.d(TAG, "params:" + params.toString());
		return params;
	}
	// 提交评论信息
	public String submitCommentJson(Reply data, String msgKey,String upKeyMsg,Comment comment)
			throws JSONException {
		String json = "";
		if (data != null) {
			JSONObject dObj = new JSONObject();
			dObj.put("topicId", data.getTopicId());// 话题Id
			dObj.put("msgKey", msgKey);
			dObj.put("upMsgKey", upKeyMsg);// 回帖path
			dObj.put("pathLevel", data.getLevel());// 回帖层数
			dObj.put("repliesUserId", data.getUserId());// 回帖人Id
			dObj.put("repliesOptions", "," + comment.getComment());// 回帖调查选项
			dObj.put("repliesContent", comment.getComment());// 评论内容
			dObj.put("repliesTime", data.getDate());// 回帖时间
			dObj.put("repliesLon", "");// 经度
			dObj.put("repliesLat", "");// 纬度
			dObj.put("repliesAddress", "");// 地址
			dObj.put("repliesGisType", "");// 类型
			dObj.put("repliesParams", "");// 参数
			dObj.put("annexFile", "");// 附件
			dObj.put("photo1", "");// 照片
			dObj.put("photo2", "");// 照片
			dObj.put("photo3", "");// 照片
			dObj.put("photo4", "");// 照片
			dObj.put("photo5", "");// 照片
			dObj.put("photo6", "");// 照片
			dObj.put("photo7", "");// 照片
			dObj.put("photo8", "");// 照片
			dObj.put("photo9", "");// 照片
			dObj.put("authUserId", comment.getAuthUserId());//审批人Id
			dObj.put("authUserName", comment.getAuthUserName());//审批人姓名

			json = dObj.toString();
		}

		return json;
	}

	
}
