package com.yunhu.yhshxc.wechat.approval;

import gcg.org.debug.JLog;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.wechat.Util.WechatUtil;
import com.yunhu.yhshxc.wechat.bo.Comment;
import com.yunhu.yhshxc.wechat.bo.Topic;
import com.yunhu.yhshxc.wechat.db.CommentDB;
import com.yunhu.yhshxc.wechat.exchange.WechatView;
import com.yunhu.yhshxc.wechat.survey.CheckBoxView;
import com.yunhu.yhshxc.wechat.survey.SurveyDialiog;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.loopj.android.http.RequestParams;

public class ApprovalDialog extends SurveyDialiog implements TextWatcher{
	private static int replyId=0;//发帖人id
	private final String TAG = "ApprovalDialog";
	private WechatView wechatView;
	private CommentDB commentDB;
	 public ApprovalGetUser approvalGetUser;
	public final static int APPROVALDIALOG_FLAG=110;
	public static String resultName;
	private EditText approvalText;
//	public EditText ed_survey_remarks;
	public ApprovalDialog(Context context, Topic topic) {
		super(context, topic);
		this.approvalGetUser=(ApprovalGetUser) context;
		commentDB = new CommentDB(context);
		ed_survey_remarks.addTextChangedListener(this);
		approvalText=ed_survey_remarks;
	
		approvalGetUser.setResultShowName(approvalText);
	}
	
	
	
	public WechatView getWechatView() {
		return wechatView;
	}

     public void setReplyId(int replyId){
    	 this.replyId=replyId;
     }

	public void setWechatView(WechatView wechatView) {
		this.wechatView = wechatView;
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
					}else{
						options = options  +  (i + 1) + "," + checkBoxView.getOptions();
					}
					
				}
				
			}
						
			//必须选中一项，才能提交
			if(TextUtils.isEmpty(options)){
				ToastOrder.makeText(context, R.string.wechat_content16, ToastOrder.LENGTH_SHORT).show();
			}else{
				if(!TextUtils.isEmpty(ed_survey_remarks.getText().toString())){
					options = options  + PublicUtils.getResourceString(context,R.string.approval_note) + ed_survey_remarks.getText().toString();
				}
				String approvalStr = options;
				submitApproval(approvalStr);
				dismiss();
			}
			
			break;

		default:
			break;
		}
	
	}
	

	/**
	 * 提交审批
	 */
	public void submitApproval(final String commentStr) {
		String url = UrlInfo.doWebRepliesInfo(context);
		final Comment comment = new Comment();
		comment.setcUserId(SharedPreferencesUtil.getInstance(context).getUserId());
		comment.setcUserName(SharedPreferencesUtil.getInstance(context).getUserName());
		approvalGetUser.setResultUserId(comment);//接口回调设置id
		approvalGetUser.setResultUserName(comment);//接口回调设置name
		if (wechatView.bComment!=null) {
			comment.setdUserId(wechatView.bComment.getcUserId());
			comment.setdUserName(wechatView.bComment.getcUserName());
		}
		comment.setMsgKey(PublicUtils.chatMsgKey(context));
		comment.setComment(commentStr);
		comment.setDate(DateUtil.getCurDateTime());
		comment.setReplyId(wechatView.reply.getReplyId());
		GcgHttpClient.getInstance(context).post(url, paramsComment(comment),
				new HttpResponseListener() {

					@Override
					public void onSuccess(int statusCode, String content) {
						JLog.d(TAG, "content:" + content);
						wechatView.isComment = false;
						try {
							JSONObject obj = new JSONObject(content);
							String resultcode = obj.getString("resultcode");
							String pathCode = obj.getString("pathCode");
							String repliesId = obj.getString("repliesId");
							String topId = obj.getString("topId");
							if ("0000".equals(resultcode)) {
								ToastOrder.makeText(context,R.string.submit_ok, ToastOrder.LENGTH_SHORT).show();
								comment.setTopicId(topId);
								comment.setPathCode(pathCode);
								comment.setcUserId(SharedPreferencesUtil.getInstance(context).getUserId());
								comment.setcUserName(SharedPreferencesUtil.getInstance(context).getUserName());
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
						try {
							wechatView.commentList.add(comment);
							wechatView.setExchangeWechat(wechatView.reply, wechatView.commentList, wechatView.zanList);
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
			if (wechatView.bComment!=null) {//说明是评论其他评论
				params.put("data", wechatUtil.submitCommentJson(wechatView.reply,comment.getMsgKey(),wechatView.bComment.getMsgKey(),comment));
			}else{//说明直接评论回帖
				params.put("data", wechatUtil.submitCommentJson(wechatView.reply, comment.getMsgKey(),wechatView.reply.getMsgKey(),comment));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JLog.d(TAG, "params:" + params.toString());
		return params;
	}

  //定义接口,让其基于的activity开启GroupUserActivity获取指定二次审核人
	public interface ApprovalGetUser{
	   void startGroupUserActivity(int replyId);//
	   void setResultUserId(Comment id);
	   void setResultUserName(Comment  name);
	   void setResultShowName(EditText et);
	}


	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// TODO Auto-generated method stub
		 
	}



	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		if (count != 0) {// 输入框有内容
		
			String result = s.toString();
			char[] array = result.toCharArray();
			String re = array[0] + "";
			if (re.equals("@") && array.length < 2 ) {
				approvalGetUser.startGroupUserActivity(replyId);
			}
		}

	}



	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		if (resultName!=null) {
			ed_survey_remarks.setText("@"+resultName+"  ");
			ed_survey_remarks.setSelection((s.toString()+"  ").length());
		}
		
	}
}
