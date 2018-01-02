package com.yunhu.yhshxc.wechat.exchange;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import gcg.org.debug.JLog;
import android.widget.Toast;

import android.widget.TextView;


import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.wechat.Util.WechatUtil;
import com.yunhu.yhshxc.wechat.bo.Comment;
import com.yunhu.yhshxc.wechat.bo.Reply;
import com.yunhu.yhshxc.wechat.bo.Topic;
import com.yunhu.yhshxc.wechat.bo.Zan;
import com.yunhu.yhshxc.wechat.db.CommentDB;
import com.yunhu.yhshxc.wechat.db.ReplyDB;
import com.yunhu.yhshxc.wechat.db.ZanDB;
import com.loopj.android.http.RequestParams;

public class ExchangeAdapter extends BaseAdapter {
	private static final String TAG = "ExchangeAdapter";
	private Context mContext = null;
	private HashMap<Integer, String> fixedName;
	private LayoutInflater inflater;
	/**
	 * 所有菜单项数据的引用，这个List是从HomeMenuActivity传进来的
	 */
	private List<Reply> replyList = new ArrayList<Reply>();
	private List<Comment> commentList = new ArrayList<Comment>();
	private List<Zan> zanList = new ArrayList<Zan>();
	private LinearLayout ll_exchange_chat;
//	private EditText ed_exchange_comment;
	public ExchangeActivity exchangeActivity;
	public KeyboardControllListener listener;
	public CommentSelectListener commentSelectListener;
	private ZanDB zanDB;
	private CommentDB commentDB;
	private Topic topic;
	
    public final static int  EXCHANGEADAPTER_FLAG=120;
	
	public void setCommentSelectListener(CommentSelectListener commentSelectListener) {
		this.commentSelectListener = commentSelectListener;
	}


//	public void setExchangeComment(EditText et) {
//		this.ed_exchange_comment = et;
//	}

	
	public void setListener(KeyboardControllListener listener) {
		this.listener = listener;
	}


	public List<Reply> getReplyList() {
		return replyList;
	}

	public void setReplyList(List<Reply> replyList) {
		this.replyList = replyList;
	}

	public ExchangeAdapter(Context mContext, List<Reply> replyList) {
		this.mContext = mContext;
		this.replyList = replyList;
		exchangeActivity = (ExchangeActivity) mContext;
		fixedName = SharedPreferencesUtil.getInstance(mContext).getFixedName();
		zanDB = new ZanDB(mContext);
		commentDB = new CommentDB(mContext);
	}

	/**
	 * 获取菜单项总数，即菜单项List的总数
	 * 
	 * @return 返回菜单项总数
	 */
	@Override
	public int getCount() {
		return replyList.size();
	}

	/**
	 * 获取指定位置的菜单项数据
	 * 
	 * @param position
	 *            菜单项在List中的位置
	 * @return 返回指定位置的菜单项数据
	 */
	@Override
	public Object getItem(int position) {
		return replyList.get(position);
	}

	/**
	 * 获取指定位置菜单项的id，通常情况下这个ID就是指定位置本身
	 * 
	 * @param position
	 *            在菜单项List中的位置
	 * @return 直接返回position值
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 系统接口，系统会自己调用
	 */
	@Override
	public View getView( int position, View convertView, ViewGroup parent) {
		 Reply reply = replyList.get(position);
			
		
		if(!TextUtils.isEmpty(reply.getDelStatus())&&reply.getDelStatus().equals("1")){//撤回
			convertView = View.inflate(mContext, R.layout.activity_survey_others_item_chenhui, null);
			TextView tv = (TextView) convertView.findViewById(R.id.tv_chexiao);
			tv.setText("\""+reply.getReplyName()+"\""+PublicUtils.getResourceString(mContext,R.string.wechat_content69));
		}else{//正常
			commentList = commentDB.findCommentListByReply(reply);
			zanList = zanDB.findZanListByReply(reply.getTopicId(), reply.getReplyId());
			WechatView wechatView = new WechatView(mContext);
			wechatView.setRepliesAuth(topic);
			wechatView.setExchangeView(exchangeActivity);
//			wechatView.setEditText(ed_exchange_comment);
			wechatView.setKeyboardControllListener(listener);
			wechatView.setCommentSelectListener(commentSelectListener);
			try {
				wechatView.setExchangeWechat(reply, commentList, zanList);
				wechatView.setImageGridView();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			convertView = wechatView.getView();
		}
		String[] strs = {PublicUtils.getResourceString(mContext,R.string.wechat_content67),PublicUtils.getResourceString(mContext,R.string.wechat_content68)};
//	   View dialogView = View.inflate(mContext, R.layout.wechat_msg_dialog, null);//点击消息弹出的选择框
	   AlertDialog alDialog = new AlertDialog.Builder(mContext,R.style.WeChatAlertDialogStyle)
			   .setItems(strs,new myDialogClick(reply))
			   .create();
	   alDialog.setCanceledOnTouchOutside(true);
//	   Button select_audit=(Button) dialogView.findViewById(R.id.select_audit);
//	   Button recall_msg=(Button) dialogView.findViewById(R.id.recall_msg);
//	   select_audit.setOnClickListener(new myDialogClick(reply,alDialog));
//	   recall_msg.setOnClickListener(new myDialogClick(reply,alDialog));
      
	   
//	   PopupMenu pMenu = new PopupMenu(mContext, convertView);
//       pMenu.getMenuInflater().inflate(R.menu.wechat_item_menu, pMenu.getMenu());
//       pMenu.setOnMenuItemClickListener(new myMenuItemClickListener(reply));
       convertView.setOnClickListener(new myOnclick(reply,alDialog));
		return convertView;
	}
	class myOnclick implements OnClickListener{
		private Reply reply;
		AlertDialog alDialog;
		myOnclick( Reply reply,AlertDialog alDialog){
			this.reply = reply;
			this.alDialog = alDialog;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			if(exchangeActivity.isApproval) {//审批
				Comment comment = commentDB.findRecentCommentByRplayId(reply.getReplyId());
				if (TextUtils.isEmpty(reply.getDelStatus())||reply.getDelStatus().equals("0")) {
				
				if(comment!=null){
					if (reply.getUserId()==SharedPreferencesUtil.getInstance(mContext).getUserId()||reply.getAuthUserId()==SharedPreferencesUtil.getInstance(mContext).getUserId()||comment.getAuthUserId()==SharedPreferencesUtil.getInstance(mContext).getUserId()) {
						alDialog.show();
					}
				}else{
					if (reply.getUserId()==SharedPreferencesUtil.getInstance(mContext).getUserId()||reply.getAuthUserId()==SharedPreferencesUtil.getInstance(mContext).getUserId()) {
						alDialog.show();
					}
				}
				
			
			}
			
		}
		
		}
		
	}
	
	 class myDialogClick implements DialogInterface.OnClickListener{//dialog点击功能监听
		 private Reply reply;
		 public myDialogClick(Reply reply) {
			// TODO Auto-generated constructor stub
			 this.reply=reply;
	
		}
		@Override
		public void onClick(DialogInterface dialog, int which) {


			switch (which) {
			case 0:
				
				//进行指定信息选择评审人的操作
				if(exchangeActivity.isApproval) {//审批
					Comment comment = commentDB.findRecentCommentByRplayId(reply.getReplyId());
					if(comment==null&&reply.getUserId()==SharedPreferencesUtil.getInstance(mContext).getUserId()){
						//如果消息发送成功就会写入数据库,才能在小组成员界面获取该reply,否则要等待消息发送成功才能跳转到选人界面
						ReplyDB replyDB1 = new ReplyDB(mContext);
					    Reply  reply1 = replyDB1.findReplyByReplyId(reply.getReplyId());
					    if (reply1==null) {
					    	Toast.makeText(mContext, R.string.wechat_content66, Toast.LENGTH_SHORT).show();
						}else{
							  Intent intent = new Intent(mContext,GroupUserActivity.class);
							  intent.putExtra("getUser",EXCHANGEADAPTER_FLAG);
							  intent.putExtra("groupId",exchangeActivity.groupId);								  
							  intent.putExtra("replyId", reply.getReplyId());
							  mContext.startActivity(intent);
						}
						    //如果发帖人指定的审核人未审批,可以更换审批人   
					}else if (comment!=null&&comment.getcUserId()==SharedPreferencesUtil.getInstance(mContext).getUserId()&&comment.getcUserId()==reply.getUserId()) {
						//进入小组成员选择界面
						  Intent intent = new Intent(mContext,GroupUserActivity.class);
						  intent.putExtra("getUser",EXCHANGEADAPTER_FLAG);
						  intent.putExtra("groupId",exchangeActivity.groupId);
						  intent.putExtra("replyId", reply.getReplyId());
						  mContext.startActivity(intent);
						  //如果审核人没有审核,可以再指定审核人
					}else if (comment!=null&&comment.getAuthUserId()==SharedPreferencesUtil.getInstance(mContext).getUserId()){
						//进入小组成员选择界面
						  Intent intent = new Intent(mContext,GroupUserActivity.class);
						  intent.putExtra("getUser",EXCHANGEADAPTER_FLAG);
						  intent.putExtra("groupId",exchangeActivity.groupId);
						  intent.putExtra("replyId", reply.getReplyId());
						  mContext.startActivity(intent);
					}else if (comment==null&&reply.getAuthUserId()==SharedPreferencesUtil.getInstance(mContext).getUserId()){
						//进入小组成员选择界面
						  Intent intent = new Intent(mContext,GroupUserActivity.class);
						  intent.putExtra("getUser",EXCHANGEADAPTER_FLAG);
						  intent.putExtra("groupId",exchangeActivity.groupId);
						  intent.putExtra("replyId", reply.getReplyId());
						  mContext.startActivity(intent);
					}else{
						
						Toast.makeText(mContext, R.string.wechat_content65, Toast.LENGTH_SHORT).show();
					}
					
					
				}
				
				
				
				break;
			case 1:
				 
				
				Comment comment = commentDB.findRecentCommentByRplayId(reply.getReplyId());

				if ((comment==null&&reply.getUserId()==SharedPreferencesUtil.getInstance(mContext).getUserId())||comment!=null&&comment.getcUserId()==SharedPreferencesUtil.getInstance(mContext).getUserId()) {//发帖本人
			
				GcgHttpClient.getInstance(mContext).post(UrlInfo.doWeChatDelRepliesInfo(mContext), replyParams(topic,reply), new HttpResponseListener() {
					
					@Override
					public void onSuccess(int statusCode, String content) {
	
								Toast.makeText(mContext, R.string.wechat_content64, Toast.LENGTH_SHORT).show();
						
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
						Toast.makeText(mContext, R.string.wechat_content63, Toast.LENGTH_SHORT).show();
					}
				});
				
				}else if (comment!=null&&reply.getUserId()==SharedPreferencesUtil.getInstance(mContext).getUserId()){
					Toast.makeText(mContext, R.string.wechat_content62, Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(mContext, R.string.wechat_content61, Toast.LENGTH_SHORT).show();
				}
				
			
				break;

			default:
				break;
			} 	
		
			
		}

		 
	 }
//	class myMenuItemClickListener implements OnMenuItemClickListener{
//		
//		private Reply reply;
//		myMenuItemClickListener( Reply reply){
//			this.reply = reply;
//		}
//
//		@Override
//		public boolean onMenuItemClick(MenuItem item) {
//			// TODO Auto-generated method stub
//
//			// TODO Auto-generated method stub
//
//			return true;
//		
//		}
//		
//	}
	
	/**
	 * 回帖参数
	 * 
	 * @return
	 */
	private RequestParams replyParams(Topic topic,Reply reply) {
		RequestParams params = new RequestParams();
		try {
			params.put("phoneno", PublicUtils.receivePhoneNO(mContext));
			WechatUtil wechatUtil = new WechatUtil(mContext);
			JLog.d("abby", "data" + wechatUtil.submitRepliesJson(topic, reply));
			params.put("repliesId", reply.getReplyId());

		} catch (JSONException e) {
			e.printStackTrace();
		}
		JLog.d(TAG, "params:" + params.toString());
		return params;
	}
	public void setRepliesAuth(Topic topic){
		this.topic = topic;
	}
	
	public void refresh(List<Reply> replyList) {
		this.replyList = replyList;
		notifyDataSetChanged();
		
	}
}
