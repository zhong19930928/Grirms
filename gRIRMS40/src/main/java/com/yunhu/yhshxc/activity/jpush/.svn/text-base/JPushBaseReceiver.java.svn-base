package com.yunhu.yhshxc.activity.jpush;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.wechat.Util.SharedPrefrencesForWechatUtil;
import com.yunhu.yhshxc.wechat.Util.WechatUtil;
import com.yunhu.yhshxc.wechat.WechatActivity;
import com.yunhu.yhshxc.wechat.bo.Comment;
import com.yunhu.yhshxc.wechat.bo.Group;
import com.yunhu.yhshxc.wechat.bo.GroupUser;
import com.yunhu.yhshxc.wechat.bo.Notification;
import com.yunhu.yhshxc.wechat.bo.PersonalWechat;
import com.yunhu.yhshxc.wechat.bo.Reply;
import com.yunhu.yhshxc.wechat.bo.Survey;
import com.yunhu.yhshxc.wechat.bo.Topic;
import com.yunhu.yhshxc.wechat.bo.Zan;
import com.yunhu.yhshxc.wechat.db.CommentDB;
import com.yunhu.yhshxc.wechat.db.GroupDB;
import com.yunhu.yhshxc.wechat.db.GroupUserDB;
import com.yunhu.yhshxc.wechat.db.NotificationDB;
import com.yunhu.yhshxc.wechat.db.PersonalWechatDB;
import com.yunhu.yhshxc.wechat.db.ReplyDB;
import com.yunhu.yhshxc.wechat.db.SurveyDB;
import com.yunhu.yhshxc.wechat.db.TopicDB;
import com.yunhu.yhshxc.wechat.db.ZanDB;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import gcg.org.debug.JLog;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class JPushBaseReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";
	private GroupDB groupDB;
	private TopicDB topicDB;
	private WechatUtil util;
	private Context context;
	private NotificationDB notificationDB;
	private String repliesId;
	private String topicId;
	private ReplyDB replyDB = null;

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		this.context = context;
		groupDB = new GroupDB(context);
		topicDB = new TopicDB(context);
		util = new WechatUtil(context);
		notificationDB = new NotificationDB(context);
		replyDB = new ReplyDB(context);
		Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction()
				+ ", extras: " + printBundle(bundle));
		// processCustomMessage(context, bundle);
		String regIds = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
		Log.d(TAG, " 接收Registration Id : " + regIds);
		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle
					.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Log.d(TAG, " 接收Registration Id : " + regId);
			// send the Registration Id to your server...

		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {
			Log.d(TAG,
					"推送下来的自定义消息: "
							+ bundle.getString(JPushInterface.EXTRA_MESSAGE));
			processCustomMessage(context, bundle);
			
			
//			context.sendStickyBroadcast(new Intent("new_message"));
//			context.sendStickyBroadcast(new Intent("new_wechat_message"));
			
			
			
			// notificationText();

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {
			Log.d(TAG, "推送下来的通知");
			int notifactionId = bundle
					.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			Log.d(TAG, "推送下来的通知的ID: " + notifactionId);

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
			Log.d(TAG, "用户点击打开了通知");

			// 打开自定义的Activity
			// Intent i = new Intent(context, TestActivity.class);
			// i.putExtras(bundle);
			// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
			// Intent.FLAG_ACTIVITY_CLEAR_TOP );
			// context.startActivity(i);
			Intent i = new Intent(context, WechatActivity.class);
			i.putExtras(bundle);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			context.startActivity(i);

		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
				.getAction())) {
			Log.d(TAG,
					"用户收到到RICH PUSH CALLBACK: "
							+ bundle.getString(JPushInterface.EXTRA_EXTRA));
			// 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
			// 打开一个网页等..

		} else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent
				.getAction())) {
			boolean connected = intent.getBooleanExtra(
					JPushInterface.EXTRA_CONNECTION_CHANGE, false);
			Log.w(TAG, "[MyReceiver]" + intent.getAction()
					+ " connected state change to " + connected);
		} else {
			Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
		}
	}

	/**
	 *@method
	 *@author suhu
	 *@time 2017/4/18 12:30
	 *@param
	 *
	*/
	private void notificationText(CharSequence contentTitle,
			CharSequence contentText) {

		// 消息通知栏

		// 定义NotificationManager

		String ns = Context.NOTIFICATION_SERVICE;

		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(ns);
		android.app.Notification.Builder builder = new android.app.Notification.Builder(context);
		// 定义通知栏展现的内容信息


		int icon = R.drawable.icon_main;

		CharSequence tickerText = PublicUtils.getResourceString(context,R.string.new_notice6);

		long when = System.currentTimeMillis();
		builder.setSmallIcon(icon);
		builder.setContentTitle(contentTitle);
		builder.setContentText(contentText);
		android.app.Notification notification = builder.getNotification();

		notification.tickerText=tickerText;


		notification.flags |= android.app.Notification.FLAG_AUTO_CANCEL;// 点击后就会自动消失了
		notification.defaults |= android.app.Notification.DEFAULT_SOUND; // 提示声音
		notification.defaults |= android.app.Notification.DEFAULT_VIBRATE; // 手机震动
		// 定义下拉通知栏时要展现的内容信息

		// CharSequence contentTitle = "我的通知栏标展开标题";
		//
		// CharSequence contentText = "我的通知栏展开详细内容";

		Intent notificationIntent = new Intent(context, WechatActivity.class);
		// notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);

		builder.setContentIntent(contentIntent);

		// 用mNotificationManager的notify方法通知用户生成标题栏消息通知
		// int requestCode = (int) System.currentTimeMillis();
		if (SharedPreferencesUtil.getInstance(context).getIsInit()) {
			mNotificationManager.notify(1, notification);
		}
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

	// send msg to MainActivity
	private void processCustomMessage(Context context, Bundle bundle) {

		String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
		String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);

		// Toast.makeText(context, "消息内容:" + message + "extras:" + extras,
		// Toast.LENGTH_SHORT).show();
		JLog.d("alin", "消息内容:" + message + "extras:" + extras);
		if (!TextUtils.isEmpty(extras)) {
			try {
				JSONObject obj = new JSONObject(extras);
				if (PublicUtils.isValid(obj, "action")) {
					String action = obj.getString("action");
					if (action.equalsIgnoreCase("group")) {// 群
						pushGroup(obj);
					} else if (action.equalsIgnoreCase("topic")) {// 话题
						pushTopic(obj);
					} else if (action.equalsIgnoreCase("replies")) {// 回帖和评论
						pushReplies(obj);
					} else if (action.equalsIgnoreCase("notice")) {// 公告
						pushNotice(obj);
					} else if (action.equalsIgnoreCase("point")) {// 点赞
						pushZan(obj);
					} else if (action.equalsIgnoreCase("private")) {// 私聊
						pushPrivate(obj);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
				// Toast.makeText(context, "收到异常数据PUSH",
				// Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void pushPrivate(JSONObject obj) throws JSONException {
		String privateId = obj.getString("privateId");
		if (!TextUtils.isEmpty(privateId)) {
			String url = UrlInfo.weChatPrivateInfo(context);
			RequestParams params = new RequestParams();
			params.put("privateId", privateId);
			GcgHttpClient.getInstance(context).get(url, params,
					new HttpResponseListener() {

						@Override
						public void onSuccess(int statusCode, String content) {
							PersonalWechat per = null;
							try {

								JSONObject obj = new JSONObject(content);
								String resultcode = obj.getString("resultcode");
								if ("0000".equals(resultcode)) {
									JSONArray array = obj
											.getJSONArray("privateInfo");
									if (array != null && array.length() > 0) {
										for (int i = 0; i < array.length(); i++) {
											JSONObject pObj = array
													.getJSONObject(i);
											PersonalWechat pw = new PersonalWechat();
											if (PublicUtils.isValid(pObj,
													"privateId")) {
												int value = pObj
														.getInt("privateId");
												pw.setDataId(value);
											}
											if (PublicUtils.isValid(pObj,
													"fromId")) {
												int value = pObj
														.getInt("fromId");
												pw.setsUserId(value);

												if (PublicUtils.isValid(pObj,
														"fromHeadImg")) {
													String url = pObj
															.getString("fromHeadImg");
													SharedPrefrencesForWechatUtil
															.getInstance(
																	context)
															.setUserHedaImg(
																	String.valueOf(value),
																	url);
												}

											}

											if (PublicUtils.isValid(pObj,
													"toId")) {
												int value = pObj.getInt("toId");
												pw.setdUserId(value);
												if (PublicUtils.isValid(pObj,
														"toHeadImg")) {
													String url = pObj
															.getString("toHeadImg");
													SharedPrefrencesForWechatUtil
															.getInstance(
																	context)
															.setUserHedaImg(
																	String.valueOf(value),
																	url);
												}
											}
											if (PublicUtils.isValid(pObj,
													"fromName")) {
												String value = pObj
														.getString("fromName");
												pw.setsUserName(value);
											}
											if (PublicUtils.isValid(pObj,
													"toName")) {
												String value = pObj
														.getString("toName");
												pw.setdUserName(value);
											}
											if (PublicUtils.isValid(pObj,
													"content")) {
												String value = pObj
														.getString("content");
												pw.setContent(value);
											}
											StringBuffer sb = new StringBuffer();
											if (PublicUtils.isValid(pObj,
													"photo1")) {
												String p = pObj
														.getString("photo1");
												sb.append(",").append(p);
											}
											if (PublicUtils.isValid(pObj,
													"photo2")) {
												String p = pObj
														.getString("photo2");
												sb.append(",").append(p);
											}
											if (PublicUtils.isValid(pObj,
													"photo3")) {
												String p = pObj
														.getString("photo3");
												sb.append(",").append(p);
											}
											if (PublicUtils.isValid(pObj,
													"photo4")) {
												String p = pObj
														.getString("photo4");
												sb.append(",").append(p);
											}
											if (PublicUtils.isValid(pObj,
													"photo5")) {
												String p = pObj
														.getString("photo5");
												sb.append(",").append(p);
											}
											if (PublicUtils.isValid(pObj,
													"photo6")) {
												String p = pObj
														.getString("photo6");
												sb.append(",").append(p);
											}
											if (PublicUtils.isValid(pObj,
													"photo7")) {
												String p = pObj
														.getString("photo7");
												sb.append(",").append(p);
											}
											if (PublicUtils.isValid(pObj,
													"photo8")) {
												String p = pObj
														.getString("photo8");
												sb.append(",").append(p);
											}
											if (PublicUtils.isValid(pObj,
													"photo9")) {
												String p = pObj
														.getString("photo9");
												sb.append(",").append(p);
											}

											if (sb.length() > 0) {
												String photo = sb.substring(1);
												pw.setPhoto(photo);
											}
											if (PublicUtils.isValid(pObj,
													"attachment")) {
												String value = pObj
														.getString("attachment");
												pw.setAttachment(value);
											}

											if (PublicUtils.isValid(pObj,
													"tTime")) {
												String value = pObj
														.getString("tTime");
												pw.setDate(value);
											}

											String groupKey = "";
											if (SharedPreferencesUtil
													.getInstance(context)
													.getUserId() == pw
													.getsUserId()) {
												groupKey = String.valueOf(pw
														.getsUserId())
														+ String.valueOf(pw
																.getdUserId());
											} else {
												groupKey = String.valueOf(pw
														.getdUserId())
														+ String.valueOf(pw
																.getsUserId());
											}
											pw.setGroupKey(groupKey);
											if (pw.getsUserId() == SharedPreferencesUtil
													.getInstance(context)
													.getUserId()) {
												pw.setIsRead(1);
											}
											new PersonalWechatDB(context)
													.insert(pw);
											per = pw;
										}
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							if (per != null) {
								CharSequence title = PublicUtils.getResourceString(context,R.string.new_notice6);
								CharSequence contenttext = "";
								if (TextUtils.isEmpty(per.getContent())) {
									contenttext = per.getsUserName() + ":";
								} else {
									contenttext = per.getsUserName() + ":"
											+ per.getContent();
								}

								notificationText(title, contenttext);
								context.sendStickyBroadcast(new Intent("new_wechat_message"));
								context.sendStickyBroadcast(new Intent("new_message"));
							}

							context.sendBroadcast(new Intent(
									Constants.BROADCAST_WECHAT_PRIVATE));
							context.sendBroadcast(new Intent(
									Constants.BROADCAST_WECHAT_PERSON));

						}

						@Override
						public void onStart() {

						}

						@Override
						public void onFinish() {

						}

						@Override
						public void onFailure(Throwable error, String content) {

						}
					});
		}

	}

	/**
	 * 处理加入群PUSH
	 * 
	 * @param obj
	 * @throws JSONException
	 */
	private void pushGroup(JSONObject obj) throws JSONException {
		String operation = obj.getString("operation");
		if (PublicUtils.isValid(obj, "groupId")) {
			String groupId = obj.getString("groupId");

			Group group = groupDB.findGroupByGroupId(Integer.parseInt(groupId));
			if (operation.equalsIgnoreCase("c")) {// 增加或更新
				if (group != null) {
					if (PublicUtils.isValid(obj, "logo")) {
						String logoUrl = obj.getString("logo");
						group.setLogo(logoUrl);
					}
					if (PublicUtils.isValid(obj, "groupName")) {
						String groupName = obj.getString("groupName");
						group.setGroupName(groupName);
					}

					if (PublicUtils.isValid(obj, "userId")) {
						String userId = obj.getString("userId");
						group.setCreateUserId(Integer.parseInt(userId));
					}

					if (PublicUtils.isValid(obj, "type")) {
						String type = obj.getString("type");
						if (!TextUtils.isEmpty(type)) {
							group.setType(Integer.parseInt(type));
						}
					}
					groupDB.updateGroup(group);
				} else {
					group = new Group();
					if (PublicUtils.isValid(obj, "logo")) {
						String logoUrl = obj.getString("logo");
						group.setLogo(logoUrl);
					}
					if (PublicUtils.isValid(obj, "groupName")) {
						String groupName = obj.getString("groupName");
						group.setGroupName(groupName);
					}

					if (PublicUtils.isValid(obj, "userId")) {
						String userId = obj.getString("userId");
						group.setCreateUserId(Integer.parseInt(userId));
					}

					if (PublicUtils.isValid(obj, "type")) {
						String type = obj.getString("type");
						if (!TextUtils.isEmpty(type)) {
							group.setType(Integer.parseInt(type));
						}
					}
					group.setGroupId(Integer.parseInt(groupId));
					groupDB.insert(group);
					Set<String> tags = util.wetCharTags(true, groupId);
					setTags(tags);
					searchGroup(group.getGroupId());
				}
			} else if (operation.equalsIgnoreCase("d")) {// 删除
				if (group != null) {
					groupDB.delete(Integer.parseInt(groupId));
					Set<String> tags = util.wetCharTags(false, groupId);
					setTags(tags);
				}
			}
		}
	}

	/**
	 * 查询群信息
	 * 
	 * @param groupId
	 */
	private void searchGroup(int groupId) {
		String url = UrlInfo.weChatGroupInfo(context);
		RequestParams params = new RequestParams();
		params.put("phoneno", PublicUtils.receivePhoneNO(context));
		params.put("groupId", groupId);
		GcgHttpClient.getInstance(context).post(url, params,
				new HttpResponseListener() {

					@Override
					public void onSuccess(int statusCode, String content) {
						try {
							JSONObject obj = new JSONObject(content);
							if (PublicUtils.isValid(obj, "groups")) {
								JSONArray gArray = obj.getJSONArray("groups");
								if (gArray != null && gArray.length() > 0) {
									for (int i = 0; i < gArray.length(); i++) {
										JSONObject gObj = gArray
												.getJSONObject(i);
										Group group = new Group();
										if (PublicUtils.isValid(gObj, "id")) {
											int value = gObj.getInt("id");
											group.setGroupId(value);
										}
										if (PublicUtils.isValid(gObj, "logo")) {
											String value = gObj
													.getString("logo");
											group.setLogo(value);
										}

										if (PublicUtils.isValid(gObj, "name")) {
											String value = gObj
													.getString("name");
											group.setGroupName(value);
										}

										if (PublicUtils.isValid(gObj, "orgId")) {
											int value = gObj.getInt("orgId");
											group.setOrgId(String
													.valueOf(value));
										}

										if (PublicUtils
												.isValid(gObj, "orgCode")) {
											String value = gObj
													.getString("orgCode");
											group.setOrgCode(value);
										}

										if (PublicUtils
												.isValid(gObj, "subDesc")) {
											String value = gObj
													.getString("subDesc");

										}

										if (PublicUtils.isValid(gObj, "userId")) {
											int userId = gObj.getInt("userId");
											group.setCreateUserId(userId);
										}

										if (PublicUtils.isValid(gObj, "user")) {
											GroupUserDB userDB = new GroupUserDB(
													context);
											JSONArray uArray = gObj
													.getJSONArray("user");
											if (uArray != null
													&& uArray.length() > 0) {
												for (int j = 0; j < uArray
														.length(); j++) {
													JSONObject uObj = uArray
															.getJSONObject(j);
													GroupUser user = new GroupUser();
													if (PublicUtils.isValid(
															uObj, "userId")) {
														int value = uObj
																.getInt("userId");
														user.setUserId(value);
													}
													if (PublicUtils.isValid(
															uObj, "headImg")) {
														String value = uObj
																.getString("headImg");
														user.setPhoto(value);
														SharedPrefrencesForWechatUtil
																.getInstance(
																		context)
																.setUserHedaImg(
																		String.valueOf(user
																				.getUserId()),
																		user.getPhoto());
													}

													if (PublicUtils.isValid(
															uObj, "nickName")) {
														String value = uObj
																.getString("nickName");
													}

													if (PublicUtils.isValid(
															uObj, "userName")) {
														String value = uObj
																.getString("userName");
														user.setUserName(value);
													}
													user.setGroupId(group
															.getGroupId());
													userDB.insert(user);
												}
											}
										}

									}
								}
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

					}

					@Override
					public void onFailure(Throwable error, String content) {
						JLog.d("abby", content);
					}
				});
	}

	/**
	 * 设置标签，用群ID
	 * 
	 * @param tags
	 */
	private void setTags(Set<String> tags) {
		JPushInterface.setTags(context, tags, new TagAliasCallback() {// 设置标签

					@Override
					public void gotResult(int arg0, String arg1,
							Set<String> arg2) {
						Log.i("JPush", "setTags-----arg0:" + arg0);// 0表示成功
						Log.i("jishen", "setTags-----arg1:" + arg1);
					}
				});

	}

	/**
	 * 处理话题消息
	 * 
	 * @param obj
	 * @throws JSONException
	 */
	public void pushTopic(JSONObject obj) throws JSONException {

		String operation = obj.getString("operation");
		if (PublicUtils.isValid(obj, "topicId")) {
			String topicId = obj.getString("topicId");
			int groupId = 0;

			Topic topic = topicDB.findTopicById(Integer.parseInt(topicId));
			if (operation.equalsIgnoreCase("c")) {// 增加或更新
				if (topic != null) {
					if (PublicUtils.isValid(obj, "groupId")) {
						groupId = obj.getInt("groupId");
						topic.setGroupId(groupId);
					}
					if (PublicUtils.isValid(obj, "msgKey")) {
						String msgKey = obj.getString("msgKey");
						topic.setMsgKey(msgKey);
					}
					if (PublicUtils.isValid(obj, "groupId")) {
						groupId = obj.getInt("groupId");
						topic.setGroupId(groupId);
					}

					if (PublicUtils.isValid(obj, "from")) {
						String from = obj.getString("from");
						topic.setFrom(from);
					}

					if (PublicUtils.isValid(obj, "to")) {
						String to = obj.getString("to");
						topic.setTo(to);
					}

					if (PublicUtils.isValid(obj, "type")) {
						String type = obj.getString("type");
						topic.setType(type);
					}

					if (PublicUtils.isValid(obj, "title")) {
						String title = obj.getString("title");
						topic.setTitle(title);
					}
					if (PublicUtils.isValid(obj, "content")) {
						String explain = obj.getString("content");
						topic.setExplain(explain);
					}

					if (PublicUtils.isValid(obj, "num")) {
						String num = obj.getString("num");
						topic.setSpeakNum(Integer.parseInt(num));
					}

					if (PublicUtils.isValid(obj, "userId")) {
						String userId = obj.getString("userId");
						topic.setCreateUserId(Integer.parseInt(userId));
					}

					if (PublicUtils.isValid(obj, "time")) {
						String time = obj.getString("time");
						topic.setCreateTime(time);
					}

					if (PublicUtils.isValid(obj, "classify")) {
						String type = obj.getString("classify");
						topic.setClassify(TextUtils.isEmpty(type) ? Topic.CLASSIFY_TYPE_BM
								: Integer.parseInt(type));
					}

					if (PublicUtils.isValid(obj, "userName")) {
						String userName = obj.getString("userName");
						topic.setCreateUserName(userName);
					}

					if (PublicUtils.isValid(obj, "orgName")) {
						String orgName = obj.getString("orgName");
						topic.setCreateOrgName(orgName);
					}

					if (PublicUtils.isValid(obj, "isReplies")) {
						int value = obj.getInt("isReplies");
						topic.setIsReply(value);
					}

					if (PublicUtils.isValid(obj, "reviewAuth")) {
						String value = obj.getString("reviewAuth");
						topic.setComment(Integer.parseInt(value));
					}
					if (PublicUtils.isValid(obj, "repliesAuth")) {
						String value = obj.getString("repliesAuth");
						topic.setReplyReview(Integer.parseInt(value));

					}

					topic.setTopicId(Integer.parseInt(topicId));

					topicDB.updateTopic(topic);
				} else {
					topic = new Topic();
					if (PublicUtils.isValid(obj, "groupId")) {
						groupId = obj.getInt("groupId");
						topic.setGroupId(groupId);
					}
					if (PublicUtils.isValid(obj, "title")) {
						String title = obj.getString("title");
						topic.setTitle(title);
					}
					if (PublicUtils.isValid(obj, "from")) {
						String from = obj.getString("from");
						topic.setFrom(from);
					}

					if (PublicUtils.isValid(obj, "type")) {
						String type = obj.getString("type");
						topic.setType(type);
					}

					if (PublicUtils.isValid(obj, "to")) {
						String to = obj.getString("to");
						topic.setTo(to);
					}

//					if (PublicUtils.isValid(obj, "content")) {
//						String explain = obj.getString("content");
//						topic.setExplain(explain);
//					}
//                    
					if (PublicUtils.isValid(obj, "explain")) {
						String explain = obj.getString("explain");
						topic.setExplain(explain);
					}
					if (PublicUtils.isValid(obj, "num")) {
						String num = obj.getString("num");
						topic.setSpeakNum(Integer.parseInt(num));
					}

					if (PublicUtils.isValid(obj, "userId")) {
						String userId = obj.getString("userId");
						topic.setCreateUserId(Integer.parseInt(userId));
					}

					if (PublicUtils.isValid(obj, "userName")) {
						String userName = obj.getString("userName");
						topic.setCreateUserName(userName);
					}

					if (PublicUtils.isValid(obj, "orgName")) {
						String orgName = obj.getString("orgName");
						topic.setCreateOrgName(orgName);
					}

					if (PublicUtils.isValid(obj, "time")) {
						String time = obj.getString("time");
						topic.setCreateTime(time);
					}

					if (PublicUtils.isValid(obj, "isReplies")) {
						int value = obj.getInt("isReplies");
						topic.setIsReply(value);
					}

					/**
					 * 评论发言权限
					 */
					if (PublicUtils.isValid(obj, "reviewAuth")) {
						String value = obj.getString("reviewAuth");
						topic.setComment(Integer.parseInt(value));

					}

					/**
					 * 回帖查看权限
					 */
					if (PublicUtils.isValid(obj, "repliesAuth")) {
						String value = obj.getString("repliesAuth");
						topic.setReplyReview(Integer.parseInt(value));
					}

					if (PublicUtils.isValid(obj, "classify")) {
						String type = obj.getString("classify");
						topic.setClassify(TextUtils.isEmpty(type) ? Topic.CLASSIFY_TYPE_BM
								: Integer.parseInt(type));
					}

					topic.setTopicId(Integer.parseInt(topicId));
					topic.setIsClose(0);
					topicDB.insert(topic);
				}

				if (topic != null
						&& (Topic.TYPE_2.equals(topic.getType()) || Topic.TYPE_3
								.equals(topic.getType()))) {
					searchSurvey(topicId, groupId, topic.getType());
				}

			} else if (operation.equalsIgnoreCase("d")) {// 删除
				if (topic != null) {
					topicDB.deleteTopic(Integer.parseInt(topicId));
				}
			} else if (operation.equalsIgnoreCase("g")) {// 关闭话题
				topicDB.updateTopicCloseState(Integer.parseInt(topicId), 1);
				replyDB.updateReplyCloseState(Integer.parseInt(topicId), 1);
			}
			context.sendBroadcast(new Intent(Constants.BROADCAST_WECHAT_HUILIAO));
		}
	}

	/**
	 * 查询调查类信息接口参数
	 */
	private RequestParams paramsSearchSurvey(String toicId, int groupId) {
		RequestParams params = new RequestParams();
		params.put("phoneno", PublicUtils.receivePhoneNO(context));
		params.put("groupId", groupId);
		params.put("topicId", toicId);

		JLog.d(TAG, "params:" + params.toString());
		return params;
	}

	/**
	 * 查询调查类信息
	 */

	private void searchSurvey(String toicId, int groupId,
			final String surveryType) {
		final SurveyDB surveyDB = new SurveyDB(context);
		String url = UrlInfo.weChatAuditInfo(context);
		GcgHttpClient.getInstance(context).post(url,
				paramsSearchSurvey(toicId, groupId),
				new HttpResponseListener() {

					@Override
					public void onSuccess(int statusCode, String content) {
						JLog.d(TAG, "content:" + content);
						try {
							JSONObject obj = new JSONObject(content);
							String resultcode = obj.getString("resultcode");
							String audit = obj.getString("audit");
							if ("0000".equals(resultcode)) {
								if (TextUtils.isEmpty(audit)) {
									// Toast.makeText(context,
									// "数据加载完毕",Toast.LENGTH_SHORT).show();
								} else {
									JSONArray array = obj.getJSONArray("audit");
									List<Survey> surveyList = new ArrayList<Survey>();
									WechatUtil wechatUtil = new WechatUtil(
											context);
									surveyList = wechatUtil.parserSurveyList(
											array, surveryType);
									for (int i = 0; i < surveyList.size(); i++) {
										Survey survey = surveyList.get(i);
										surveyDB.insert(survey);
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

					}

					@Override
					public void onFailure(Throwable error, String content) {
					}
				});

	}

	/**
	 * 处理回帖和评论消息
	 * 
	 * @param obj
	 * @throws JSONException
	 */
	public void pushReplies(JSONObject obj) throws JSONException {
		String operation = obj.getString("operation");
		
			
		if (PublicUtils.isValid(obj, "repliesUserId")) {
			String userId = obj.getString("repliesUserId");
			// if (SharedPreferencesUtil.getInstance(context).getUserId() ==
			// Integer.parseInt(userId)) {
			// JLog.d("abby", "user为本人，不执行操作！");
			// } else {
			
			if (PublicUtils.isValid(obj, "topicId")) {
				if (operation.equalsIgnoreCase("d")) {// 撤销回帖
					if (PublicUtils.isValid(obj, "repliesId")) {
						repliesId = obj.getString("repliesId");
						
						Reply rep = replyDB.findReplyByReplyId(Integer.parseInt(repliesId));
						if(rep!=null){
							rep.setDelStatus("1");
							String repliesUserName = "";
							if(PublicUtils.isValid(obj, "repliesUserName")){
								repliesUserName = obj.getString("repliesUserName");
								rep.setAuthUserName(repliesUserName);
							}
							replyDB.updateReply(rep);
							CharSequence title = "\""+repliesUserName+"\""+"撤回了一条信息";
							CharSequence contenttext = "";
							notificationText(title,contenttext);
							context.sendBroadcast(new Intent(
									Constants.BROADCAST_WECHAT_REPLY));
							context.sendBroadcast(new Intent(
									Constants.BROADCAST_WECHAT_HUILIAO));
						}
					}
				}else{
					topicId = obj.getString("topicId");
					Boolean ifHave = false;//验证本地数据库是否存在该话题 true为存在 false为不存在
					TopicDB topicDB = new TopicDB(context);
					List<Topic> topicList = topicDB.findTopicList();
					for (int i = 0; i < topicList.size(); i++) {
						if (Integer.parseInt(topicId) == topicList.get(i).getTopicId()) {
							ifHave = true;
							break;
						}
					}

					if (ifHave) {
						if (PublicUtils.isValid(obj, "pathLevel")) {
							String pathLevel = obj.getString("pathLevel");
							if (Integer.parseInt(pathLevel) > 2) {// 判断为回帖或评论
																	// 评论：>2
								// 查询出comment信息,存入数据库
								if (PublicUtils.isValid(obj, "repliesId")) {
									repliesId = obj.getString("repliesId");
									searchComment();
								}

							} else {
								// 查询出reply信息，存入数据库
								if (PublicUtils.isValid(obj, "repliesId")) {
									repliesId = obj.getString("repliesId");
									String content = "";
									if(PublicUtils.isValid(obj, "content")){
										content = obj.getString("content");
									}
									search(userId,content);
								}
							}
						}

					} else {
						JLog.d("abby", "不存在topic！");
					}
				}
				
			}
			
		}
		// }

	}

	private RequestParams paramsSearch() {
		RequestParams params = new RequestParams();
		params.put("phoneno", PublicUtils.receivePhoneNO(context));
		params.put("repliesId", repliesId);
		JLog.d(TAG, "params:" + params.toString());
		return params;
	}

	private void search(final String userID, final String contentTe) {

		String url = UrlInfo.weChatRepliesInfo(context);
		GcgHttpClient.getInstance(context).post(url, paramsSearch(),
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
									// Toast.makeText(context,
									// "数据加载完毕",Toast.LENGTH_SHORT).show();
								} else {
									JSONArray array = obj
											.getJSONArray("replies");
									List<Reply> replyList = new ArrayList<Reply>();
									WechatUtil wechatUtil = new WechatUtil(
											context);
									JLog.d("alin", array.toString());
									replyList = wechatUtil
											.parserSearchListItem(array);
									JLog.d("abby", replyList.size() + "size");

									for (int i = 0; i < replyList.size(); i++) {
										Reply reply = replyList.get(i);
										Topic topic = topicDB
												.findTopicById(reply
														.getTopicId());
										if (SharedPreferencesUtil.getInstance(
												context).getUserId() == Integer
												.parseInt(userID)) {
											reply.setIsRead(1);

										} else {
											reply.setIsRead(0);
										}
										Reply r = replyDB
												.findReplyByReplyId(reply
														.getReplyId());
										if (r != null) {
											replyDB.updateReply(reply);
										} else {
											if (topic != null) {
												/**
												 * 
												 * 回帖查看权限
												 */
												String replyName = "";
												if (!TextUtils.isEmpty(reply
														.getReplyName())) {
													replyName = reply
															.getReplyName();
												}
												int reviewAuth = topic
														.getReplyReview();
												if (reviewAuth == Topic.ISREPLY_2) {// 话题查看权限只有创建者和发送本人的话
													if (reply.getUserId() == SharedPreferencesUtil
															.getInstance(
																	context)
															.getUserId()
															|| topic.getCreateUserId() == SharedPreferencesUtil
																	.getInstance(
																			context)
																	.getUserId()) {
														replyDB.insert(reply);
														topic.setRecentTime(reply
																.getDate());
														if (TextUtils.isEmpty(reply
																.getContent())) {
															if (!TextUtils
																	.isEmpty(reply
																			.getPhoto())) {
																topic.setRecentContent(reply
																		.getReplyName()
																		+ ":"
																		+ PublicUtils.getResourceString(context,R.string.new_notice10));
															} else if (!TextUtils
																	.isEmpty(reply
																			.getAttachment())) {
																topic.setRecentContent(reply
																		.getReplyName()
																		+ ":"
																		+ PublicUtils.getResourceString(context,R.string.new_notice111));
															}
														} else {
															topic.setRecentContent(reply
																	.getReplyName()
																	+ ":"
																	+ reply.getContent());
														}

														topicDB.updateTopic(topic);
														// 发送通知

														if (!(SharedPreferencesUtil
																.getInstance(
																		context)
																.getUserId() == reply
																.getUserId())) {
															CharSequence title = PublicUtils.getResourceString(context,R.string.new_notice6);
															CharSequence contenttext = replyName
																	+ ":"
																	+ contentTe;
															notificationText(
																	title,
																	contenttext);
														}

													}
												} else {
													replyDB.insert(reply);
													topic.setRecentTime(reply
															.getDate());
													topic.setRecentContent(reply
															.getReplyName()
															+ ":"
															+ reply.getContent());
													topicDB.updateTopic(topic);

													// 发送通知
													if (!(SharedPreferencesUtil
															.getInstance(
																	context)
															.getUserId() == reply
															.getUserId())) {
														CharSequence title = PublicUtils.getResourceString(context,R.string.new_notice6);
														if (SharedPreferencesUtil
																.getInstance(
																		context)
																.getUserId() == reply
																.getAuthUserId()) {
															title = replyName
																	+ "@"+PublicUtils.getResourceString(context,R.string.new_notice112);
														}

														CharSequence contenttext = replyName
																+ ":"
																+ contentTe;
														notificationText(title,
																contenttext);
													}
												}
											}

										}

										context.sendBroadcast(new Intent(
												Constants.BROADCAST_WECHAT_REPLY));
										context.sendBroadcast(new Intent(
												Constants.BROADCAST_WECHAT_HUILIAO));
										context.sendStickyBroadcast(new Intent("new_wechat_message"));
										context.sendStickyBroadcast(new Intent("new_message"));
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

					}

					@Override
					public void onFailure(Throwable error, String content) {
					}
				});

	}

	private RequestParams paramsSearchComment() {
		RequestParams params = new RequestParams();
		params.put("phoneno", PublicUtils.receivePhoneNO(context));
		params.put("repliesId", repliesId);
		params.put("topicId", topicId);

		JLog.d(TAG, "params:" + params.toString());
		return params;
	}

	private void searchComment() {

		String url = UrlInfo.weChatReviewInfo(context);
		GcgHttpClient.getInstance(context).post(url, paramsSearchComment(),
				new HttpResponseListener() {

					@Override
					public void onSuccess(int statusCode, String content) {
						JLog.d(TAG, "content:" + content);
						try {
							JSONObject obj = new JSONObject(content);
							String resultcode = obj.getString("resultcode");
							String review = obj.getString("review");
							if ("0000".equals(resultcode)) {
								if (TextUtils.isEmpty(review)) {
									JLog.d("abby", "评论查询未成功！");
								} else {
									JSONArray array = obj
											.getJSONArray("review");
									List<Comment> commentList = new ArrayList<Comment>();
									WechatUtil wechatUtil = new WechatUtil(
											context);
									JLog.d("abby", array.toString());
									commentList = wechatUtil
											.parserSearchCommentList(array);
									JLog.d("abby",
											"commentList" + commentList.size());
									CommentDB commentDB = new CommentDB(context);
									for (int i = 0; i < commentList.size(); i++) {
										Comment comment = commentList.get(i);
										comment.setCommentId(comment
												.getReplyId());
										String pathCode = comment.getPathCode();
										String[] strs = pathCode.split("-");
										comment.setReplyId(Integer
												.parseInt(strs[1]));

										JLog.d("abby", comment.getComment());
										Comment c = commentDB
												.findCommentByCommentId(comment
														.getCommentId());
										if (c != null) {
											commentDB.updateComment(comment);
										} else {
											commentDB.insert(comment);
										}
										/**
										 * 如果不是评论人自己，发送通知_评论
										 */
										int dUserId = comment.getdUserId();// 被评论人Id
										int authorId = comment.getAuthUserId();// 审批人Id
										if (SharedPreferencesUtil.getInstance(
												context).getUserId() == dUserId) {// 评论人是自己

											String contextStr = comment
													.getComment();
											String commentCName = comment
													.getcUserName();
											CharSequence cTitle = commentCName
													+ PublicUtils.getResourceString(context,R.string.new_notice113);
											CharSequence conText = contextStr;
											notificationText(cTitle, conText);
										}
										if (SharedPreferencesUtil.getInstance(
												context).getUserId() == authorId) {
											String contextStr = comment
													.getComment();
											String commentCName = comment
													.getcUserName();
											CharSequence cTitle = commentCName
													+ "@"+PublicUtils.getResourceString(context,R.string.ge1);
											CharSequence conText = contextStr;
											notificationText(cTitle, conText);
										}
										context.sendBroadcast(new Intent(
												Constants.BROADCAST_WECHAT_REPLY));
									}

									JLog.d("abby", "评论查询成功！" + array.toString());

								}

								// Toast.makeText(context,
								// "评论Push成功！",Toast.LENGTH_LONG).show();
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

					}

					@Override
					public void onFailure(Throwable error, String content) {
					}
				});

	}

	/**
	 * 解析zan信息
	 * 
	 * @throws JSONException
	 */
	private void pushZan(JSONObject obj) throws JSONException {
		ZanDB db = new ZanDB(context);
		Zan zan = new Zan();
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
		Zan z = db.findZan(zan.getTopicId(), zan.getReplayId(), zan.getZanId());
		if (z == null) {
			db.insert(zan);
		}
		/**
		 * 如果不是点赞人,发送通知
		 */
		if (!(SharedPreferencesUtil.getInstance(context).getUserId() == zan
				.getUserId())) {
			String zanName = zan.getUserName();
			CharSequence cTitle = "你收到一条新消息";
			CharSequence conText = zanName + "赞了我";
			notificationText(cTitle, conText);
		}

		context.sendBroadcast(new Intent(Constants.BROADCAST_WECHAT_REPLY));
	}

	/**
	 * 处理公告消息
	 * 
	 * @param obj
	 * @throws JSONException
	 */
	public void pushNotice(JSONObject obj) throws JSONException {
		String operation = obj.getString("operation");
		if (PublicUtils.isValid(obj, "noticeId")) {
			String noticeId = obj.getString("noticeId");
			Notification notification = notificationDB
					.findNotifacationById(Integer.parseInt(noticeId));
			if (operation.equalsIgnoreCase("c")) {// 增加或更新

				if (notification != null) {
					if (PublicUtils.isValid(obj, "title")) {
						String title = obj.getString("title");
						notification.setTitle(title);
					}
					if (PublicUtils.isValid(obj, "content")) {
						String content = obj.getString("content");
						notification.setContent(content);
					}
					if (PublicUtils.isValid(obj, "count")) {
						String count = obj.getString("count");
						if (Integer.parseInt(count) > 0) {// 有附件
							notification.setIsAttach("1");
						} else {// 没有附件
							notification.setIsAttach("0");
						}
					}
					if (PublicUtils.isValid(obj, "time")) {// 发布时间
						String time = obj.getString("time");
						notification.setCreateDate(time);

					}
					if (PublicUtils.isValid(obj, "orgName")) {// 部门
						String name = obj.getString("orgName");
						notification.setCreateOrg(name);
					}
					if (PublicUtils.isValid(obj, "userName")) {
						String userName = obj.getString("userName");
						notification.setCreater(userName);
					}

					notification.setNoticeId(Integer.parseInt(noticeId));
					notificationDB.updateNotification(notification);
				} else {
					notification = new Notification();
					if (PublicUtils.isValid(obj, "title")) {
						String title = obj.getString("title");
						notification.setTitle(title);
					}
					if (PublicUtils.isValid(obj, "content")) {
						String content = obj.getString("content");
						notification.setContent(content);
					}
					if (PublicUtils.isValid(obj, "count")) {
						String count = obj.getString("count");
						if (Integer.parseInt(count) > 0) {// 有附件
							notification.setIsAttach("1");
						} else {// 没有附件
							notification.setIsAttach("0");
						}
					}
					if (PublicUtils.isValid(obj, "time")) {// 发布时间
						String time = obj.getString("time");
						notification.setCreateDate(time);
					}
					if (PublicUtils.isValid(obj, "orgName")) {// 部门
						String name = obj.getString("orgName");
						notification.setCreateOrg(name);
					}
					if (PublicUtils.isValid(obj, "userName")) {
						String userName = obj.getString("userName");
						notification.setCreater(userName);
					}
					notification.setIsRead("0");
					notification.setNoticeId(Integer.parseInt(noticeId));
					notificationDB.insert(notification);
				}
				if (PublicUtils.isValid(obj, "userId")) {
					String userId = obj.getString("userId");
					if (SharedPreferencesUtil.getInstance(context).getUserId() == Integer
							.parseInt(userId)) {

					} else {
						CharSequence cTitle = notification.getCreater()
								+ PublicUtils.getResourceString(context,R.string.new_notice7);
						CharSequence conText = notification.getContent();
						notificationText(cTitle, conText);
					}
				}
			} else if (operation.equalsIgnoreCase("d")) {// 删除
				if (notification != null) {
					notificationDB.deleteNotification(Integer
							.parseInt(noticeId));
				}
			}
		}
		context.sendBroadcast(new Intent(Constants.BROADCAST_WECHAT_NOTICE));
		context.sendStickyBroadcast(new Intent("new_wechat_message"));
		context.sendStickyBroadcast(new Intent("new_message"));
	}

}
