package com.yunhu.yhshxc.wechat.exchange;

import gcg.org.debug.JLog;
import android.annotation.SuppressLint;
import android.content.Context;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.wechat.bo.Comment;
import com.yunhu.yhshxc.wechat.view.TextViewFixTouchConsume;

public class CommentViewItem {
	private final String TAG = "CommentViewItem";
	private Context context;
	private Comment comment;
	private ExchangeActivity exchangeActivity;
//	private EditText inputEditText;//输入框
	private WechatView wechatView;
	
	public WechatView getWechatView() {
		return wechatView;
	}


	public void setWechatView(WechatView wechatView) {
		this.wechatView = wechatView;
	}


//	public EditText getInputEditText() {
//		return inputEditText;
//	}
//
//
//	public void setInputEditText(EditText inputEditText) {
//		this.inputEditText = inputEditText;
//	}


	public CommentViewItem(Context mContext) {
		this.context = mContext;
	}
	
	
	public ExchangeActivity getExchangeActivity() {
		return exchangeActivity;
	}


	public void setExchangeActivity(ExchangeActivity exchangeActivity) {
		this.exchangeActivity = exchangeActivity;
	}

	public View getCommentView(final Comment comment){
		this.comment = comment;
		LinearLayout ll = new LinearLayout(context);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		if (comment!=null) {
			boolean isRootComment = false;
			TextView tv_name = new TextView(context);
			final String text1 = comment.getcUserName();//评论人名称
			final String text2 = comment.getdUserName();//被评论人名称
			final String text3 = comment.getComment();//评论内容
			SpannableStringBuilder ssb = new SpannableStringBuilder();
			SpannableString s1 = null;	
			
			String pathCode = comment.getPathCode();
			if (!TextUtils.isEmpty(pathCode)) {
				String[] str = pathCode.split("-");
				if (str.length == 3) {
					isRootComment = true;
				}
			}
			
			if(isRootComment){
				s1 = new SpannableString(text1 + " : ");
			}else if(!TextUtils.isEmpty(text2) && !text1.equals(text2)){
				s1 = new SpannableString(text1+PublicUtils.getResourceString(context,R.string.bbs_replay));
			}else{
				s1 = new SpannableString(text1 + " : ");
			}
			
			s1.setSpan(new ClickableSpan() {

				@Override
				public void onClick(View widget) {
					if (comment!=null && comment.getcUserId() == SharedPreferencesUtil.getInstance(context).getUserId()) {
						Toast.makeText(context, R.string.wechat_content80, Toast.LENGTH_SHORT).show();
					}else{
						wechatView.bComment = comment;
						wechatView.isComment = true;
//						exchangeActivity.currentWechatView = wechatView;
//						if (inputEditText != null) {
//							inputEditText.setFocusable(true);
//							inputEditText.setFocusableInTouchMode(true);
//							inputEditText.requestFocus();
//							InputMethodManager inputManager = (InputMethodManager) inputEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//							inputManager.showSoftInput(inputEditText, 0);
//							wechatView.keyboardControllListener.keyboardControll(false,wechatView);//弹出键盘
//							wechatView.commentSelectListener.commentSelect("我" + " 回复 " + text1,wechatView);
//							inputEditText.setHint("我" + " 回复 " + text1);
//						}
						wechatView.keyboardControllListener.keyboardControll(false,wechatView);//弹出键盘
						wechatView.commentSelectListener.commentSelect(PublicUtils.getResourceString(context,R.string.wechat_content46) + text1,wechatView);

					}
				}
				/*
				 * 设置文本为超链接 同时去掉下划线
				 */
				@SuppressLint("ResourceAsColor")
				@Override
				public void updateDrawState(TextPaint ds) {
					ds.setColor(context.getResources().getColor(R.color.wechat_chat_text)); // 设置链接的文本颜色
					ds.setUnderlineText(false); // 去掉下划线
				}

			}, 0, text1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			ssb.append(s1);
			
			if (!TextUtils.isEmpty(text2)   &&  !text2.equals(text1) && !isRootComment) {
				SpannableString s2 = new SpannableString(text2);
				s2.setSpan(new ClickableSpan() {
					@Override
					public void onClick(View arg0) {
						JLog.d(TAG, "-------------s2------------");
					}

					@SuppressLint("ResourceAsColor")
					@Override
					public void updateDrawState(TextPaint ds) {
						ds.setColor(context.getResources().getColor(R.color.wechat_chat_text)); // 设置链接的文本颜色
						ds.setUnderlineText(false); // 去掉下划线
					}

				}, 0, text2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				ssb.append(s2);
			}
			
			if (!TextUtils.isEmpty(text3)) {
				SpannableString s3 = new SpannableString(" "+text3);
				ssb.append(s3);
			}
			
//			tv_name.setHighlightColor(Color.TRANSPARENT);
			tv_name.setText(ssb);
			tv_name.setMovementMethod(TextViewFixTouchConsume.LocalLinkMovementMethod.getInstance());
			TextView tv_content = new TextView(context);
			tv_content.setText(text3);
			ll.addView(tv_name);
		
		}
		return ll;
	}
	
	public Comment getComment() {
		return comment;
	}


	public void setComment(Comment comment) {
		this.comment = comment;
	}
}
