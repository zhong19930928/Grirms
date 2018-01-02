package com.yunhu.yhshxc.wechat.Util;

import gcg.org.debug.JLog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.database.MainMenuDB;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.parser.CacheData;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.wechat.bo.Comment;
import com.yunhu.yhshxc.wechat.bo.Group;
import com.yunhu.yhshxc.wechat.bo.Notification;
import com.yunhu.yhshxc.wechat.bo.Reply;
import com.yunhu.yhshxc.wechat.bo.Survey;
import com.yunhu.yhshxc.wechat.bo.Topic;
import com.yunhu.yhshxc.wechat.bo.UserPerson;
import com.yunhu.yhshxc.wechat.bo.Zan;
import com.yunhu.yhshxc.wechat.db.AttentionDB;
import com.yunhu.yhshxc.wechat.db.CommentDB;
import com.yunhu.yhshxc.wechat.db.NotificationDB;
import com.yunhu.yhshxc.wechat.db.PersonalWechatDB;
import com.yunhu.yhshxc.wechat.db.ReplyDB;
import com.yunhu.yhshxc.wechat.db.SurveyDB;
import com.yunhu.yhshxc.wechat.db.TopicDB;
import com.yunhu.yhshxc.wechat.db.ZanDB;

public class WechatUtil {
	private Context context;
	private NotificationDB notificationDB;

	public WechatUtil(Context mContext) {
		this.context = mContext;
		notificationDB= new NotificationDB(mContext);
	}

	/**
	 * 创建群
	 * 
	 * @param data
	 * @return
	 * @throws JSONException
	 */
	public String submitGroupJson(Group data) throws JSONException {
		String json = "";
		if (data != null) {
			JSONObject dObj = new JSONObject();
			dObj.put("name", data.getGroupName());
			dObj.put("orgId", data.getOrgId());
			JSONArray arrayStr = new JSONArray();
			if (!TextUtils.isEmpty(data.getGroupUser())) {
				String[] userIds = data.getGroupUser().split(",");
				for (int i = 0; i < userIds.length; i++) {
					arrayStr.put(Integer.parseInt(userIds[i]));
				}
			}
			dObj.put("user", arrayStr);
			JSONArray arrayRole = new JSONArray();
			if (!TextUtils.isEmpty(data.getGroupRole())) {
				String[] roleIds = data.getGroupRole().split(",");
				for (int i = 0; i < roleIds.length; i++) {
					arrayRole.put(Integer.parseInt(roleIds[i]));
				}
			}
			dObj.put("role", arrayRole);
			dObj.put("type", String.valueOf(data.getType()));
			dObj.put("logo", data.getLogo());
			dObj.put("subDesc", data.getExplain());
			json = dObj.toString();
		}

		return json;
	}

	/**
	 * 个人资料
	 * 
	 * @throws JSONException
	 */
	public String submitPersonal(UserPerson data) throws JSONException {
		String json = "";
		if (data != null) {
			JSONObject dObj = new JSONObject();
			dObj.put("nickName", data.getNicheng());
			dObj.put("signature", data.getQianming());
			dObj.put("headImg", data.getTouxiang());
			json = dObj.toString();
		}
		return json;
	}

	/**
	 * 发布公告
	 * 
	 * @param data
	 * @return
	 * @throws JSONException
	 */
	public String submitNoticeJson(Notification data) throws JSONException {
		String json = "";
		if (data != null) {
			JSONObject dObj = new JSONObject();
			dObj.put("type", data.getPeoples());
			json = dObj.toString();
			JSONArray arrayRole = new JSONArray();
			if (!TextUtils.isEmpty(data.getRole())) {
				String[] roleIds = data.getRole().split(",");
				for (int i = 0; i < roleIds.length; i++) {
					arrayRole.put(Integer.parseInt(roleIds[i]));
				}
			}
			dObj.put("role", arrayRole);
			JSONArray arrayStr = new JSONArray();
			if (!TextUtils.isEmpty(data.getUsers())) {
				String[] userIds = data.getUsers().split(",");
				for (int i = 0; i < userIds.length; i++) {
					arrayStr.put(Integer.parseInt(userIds[i]));
				}
			}
			dObj.put("user", arrayStr);
			dObj.put("title", data.getTitle());
			dObj.put("content", data.getContent());
			dObj.put("dateFrom", data.getFrom());
			dObj.put("dateTo", data.getTo());
//			if (!TextUtils.isEmpty(data.getAttachment())) {
//				String[] attachments = data.getAttachment().split(",");// 附件 1 2
//																		// 3
//				JSONArray array = new JSONArray();
//				for (int i = 0; i < attachments.length; i++) {
//					JSONObject dj = new JSONObject();
//					dj.put("showName", "");
//					dj.put("size", "");
//					dj.put("name", "");
//					array.put(dj);
//				}
//				dObj.put("file", array);
//			}
			String attachment = data.getAttachment();
			if (!TextUtils.isEmpty(attachment)) {
				JLog.d("alin", "attachment = "+attachment);
				JSONObject attObj = new JSONObject(attachment);
				JSONArray attArr = new JSONArray();
				Iterator<String> iterator = attObj.keys();
				while (iterator.hasNext()) {
					String key = iterator.next();
					String url = attObj.getString(key);
					JLog.d("alin", "url = "+url);
					JSONObject aObj = new JSONObject();
					if (url.endsWith(".3gp")) {
						int index = url.lastIndexOf("/");
						String fileName = url.substring(index + 1);
						aObj.put("name", fileName);
					}else{
						aObj.put("name", key);
					}
					aObj.put("showName", key);
					File file = new File(attachment);
					if (file.exists()) {
						aObj.put("size", file.length());
					}
					attArr.put(aObj);
				}
				dObj.put("annexFile", attArr);
			}
			json = dObj.toString();
		}
		return json;
	}

	public String submitTopicJson(Topic data,Survey survey) throws JSONException {
		String json = "";
		if (data != null) {
			JSONObject dObj = new JSONObject();
			dObj.put("title", data.getTitle());
			dObj.put("groupId", data.getGroupId());
			dObj.put("explain", data.getExplain());
			dObj.put("type", data.getType());
			if(data.getType().equals(Topic.TYPE_2) || data.getType().equals(Topic.TYPE_3)){
				String option = data.getOptions();
				if (!TextUtils.isEmpty(option)) {
					try {
						JSONArray array = new JSONArray(option);
						dObj.put("option", array);
						dObj.put("isSelect",String.valueOf(survey.getType()));
					} catch (Exception e) {
					}
				}
			}
			dObj.put("startDate", data.getFrom() + " 00:00:00");
			dObj.put("endDate", data.getTo() + " 00:00:00");
			dObj.put("speakNum", data.getSpeakNum());
			dObj.put("isReplies", data.getIsReply());
			dObj.put("reviewAuth", data.getComment());
			dObj.put("repliesAuth", data.getReplyReview());
			dObj.put("classify", data.getClassify());
			dObj.put("msgKey", data.getMsgKey());
			

			json = dObj.toString();
		}

		return json;
	}

	// 提交回帖信息
	public String submitRepliesJson(Topic topic,Reply data) throws JSONException {
		String json = "";
		if (data != null) {
			JSONObject dObj = new JSONObject();
			dObj.put("topicId", data.getTopicId());// 话题Id
			dObj.put("pathCode", "");// 回帖path
			dObj.put("pathLevel", "");// 回帖层数
			dObj.put("repliesUserId", data.getUserId());// 回帖人Id
			dObj.put("repliesOptions", "");// 回帖调查选项
			dObj.put("repliesContent", data.getContent());
			dObj.put("repliesTime", data.getDate());// 回帖时间
			dObj.put("repliesLon", "");// 经度
			dObj.put("repliesLat", "");// 纬
			dObj.put("repliesAddress", "");// 地址
			dObj.put("repliesGisType", "");// 类型
			dObj.put("repliesParams", "");// 参数
			dObj.put("msgKey",data.getMsgKey());// 参数
			dObj.put("upMsgKey",topic.getMsgKey());// 上级参数
			dObj.put("authUserId", data.getAuthUserId());//审批人Id
			dObj.put("authUserName", data.getAuthUserName());//审批人姓名
			dObj.put("delStatus", data.getDelStatus());//审批状态
			
			String attachment = data.getAttachment();
			if (!TextUtils.isEmpty(attachment)) {
				JSONObject attObj = new JSONObject(attachment);
				JSONArray attArr = new JSONArray();
				Iterator<String> iterator = attObj.keys();
				while (iterator.hasNext()) {
					String key = iterator.next();
					String url = attObj.getString(key);
					JSONObject aObj = new JSONObject();
					if (url.endsWith(".3gp")) {
						int index = url.lastIndexOf("/");
						String fileName = url.substring(index + 1);
						aObj.put("name", fileName);
						aObj.put("showName", fileName);
					}else{
						aObj.put("name", key);
						String showName = SharedPrefrencesForWechatUtil.getInstance(context).getGetFileName(key);
						aObj.put("showName", showName);
					}
					File file = new File(attachment);
					if (file.exists()) {
						aObj.put("size", file.length());
					}
					attArr.put(aObj);
				}
				dObj.put("annexFile", attArr);
			}
			String photo = data.getPhoto();
			if (!TextUtils.isEmpty(photo)) {
				String[] img = photo.split(",");
				for (int i = 0; i < img.length; i++) {
					dObj.put("photo"+(i+1),img[i]);// 照片
				}
			}
			json = dObj.toString();
		}

		return json;
	}
	
	
	// 提交调查类回帖信息
		public String submitSurveyJson(Reply data) throws JSONException {
			String json = "";
			if (data != null) {
				JSONObject dObj = new JSONObject();
				dObj.put("topicId", data.getTopicId());// 话题Id
				dObj.put("pathCode", "");// 回帖path
				dObj.put("pathLevel", "");// 回帖层数
				dObj.put("repliesUserId", data.getUserId());// 回帖人Id
				dObj.put("repliesOptions", data.getSurvey());// 回帖调查选项
				dObj.put("repliesContent", data.getContent());
				dObj.put("repliesTime", data.getDate());// 回帖时间
				dObj.put("repliesLon", "");// 经度
				dObj.put("repliesLat", "");// 纬度
				dObj.put("repliesAddress", "");// 地址
				dObj.put("repliesGisType", "");// 类型
				dObj.put("repliesParams", "");// 参数
				dObj.put("annexFile", "");// 附件
				String photo = data.getPhoto();
				if (!TextUtils.isEmpty(photo)) {
					String[] img = photo.split(",");
					for (int i = 0; i < img.length; i++) {
						dObj.put("photo"+(i+1),img[i]);// 照片
					}
				}
				json = dObj.toString();
			}

			return json;
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

	public String submitZanJson(Zan data) throws JSONException {
		String json = "";
		if (data != null) {
			JSONObject dObj = new JSONObject();
			dObj.put("topicId", data.getTopicId());
			dObj.put("repliesId", data.getReplayId());
			dObj.put("pointUserId", data.getUserId());
			dObj.put("pointTime", data.getDate());
			
			json = dObj.toString();
		}

		return json;
	}
	/**
	 * 保存关注信息
	 * @throws JSONException 
	 */
	public String submitGuanZhu(int i) throws JSONException{
		String json = "";
		JSONObject dObject = new JSONObject();
		dObject.put("followId", "");
		dObject.put("type", "");
		json = dObject.toString();
		return null;
	}

	/***
	 * 群聊标签
	 * 
	 * @param add
	 *            true 表示添加 false 表示删除
	 * @param tag
	 *            要添加或者要删除的标签
	 * @return 返回新的标签
	 */
	public Set<String> wetCharTags(boolean add, String tag) {
		JLog.d("Jpush", "add:" + add + "  tag:" + tag);
		Set<String> tags = new HashSet<String>();
		String lTags = SharedPreferencesUtil.getInstance(context)
				.getWetChatTags();
		if (!TextUtils.isEmpty(lTags)) {
			String[] str = lTags.split(",");
			if (str != null) {
				for (int i = 0; i < str.length; i++) {
					tags.add(str[i]);
				}
			}
		}
		JLog.d("Jpush", "lTags:" + lTags);

		if (add) {// 添加标签
			if (TextUtils.isEmpty(lTags)) {
				lTags = new StringBuffer(tag).toString();
			} else {
				lTags = new StringBuffer(lTags).append(",").append(tag)
						.toString();
			}
			SharedPreferencesUtil.getInstance(context).setWetChatTags(lTags);
			tags.add(tag);
			JLog.d("Jpush",
					"lTagsNew:"
							+ SharedPreferencesUtil.getInstance(context)
									.getWetChatTags());

		} else {// 移除标签
			if (tags.contains(tag)) {
				tags.remove(tag);
				StringBuffer sb = new StringBuffer();
				Iterator<String> iterator = tags.iterator();
				while (iterator.hasNext()) {
					sb.append(",");
					sb.append(iterator.next());
				}
				if (sb.length() > 0) {
					SharedPreferencesUtil.getInstance(context).setWetChatTags(
							sb.substring(1));
				}

				JLog.d("Jpush", "lTagsDel:"
						+ SharedPreferencesUtil.getInstance(context)
								.getWetChatTags());

			}
		}
		return tags;
	}
	
	/**
	 * 解析调查类问题数据
	 * @return
	 */
	public List<Survey> parserSurveyList(JSONArray array,String surveyType)
	         throws JSONException{
		List<Survey> itemList = new ArrayList<Survey>();
		if (array != null && array.length() > 0) {
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				Survey survey = new Survey();

				if (!TextUtils.isEmpty(surveyType)) {
					survey.setSurveyType(Integer.parseInt(surveyType));
				}
				
				if (PublicUtils.isValid(obj, "topicId")) {
					survey.setTopicId(Integer.parseInt(obj.getString("topicId")));
				}
				
				if (PublicUtils.isValid(obj, "optionOrder")) {
					survey.setOptionOrder(obj.getInt("optionOrder"));
				}
				
				if (PublicUtils.isValid(obj, "optionLabel")) {
					survey.setOptions(obj.getString("optionLabel"));
				}
				
				if (PublicUtils.isValid(obj, "isSelect")) {
					survey.setType(Integer.parseInt(obj.getString("isSelect")));
				}

				
				itemList.add(survey);
			}
		}
		
		return itemList;
		
	}
	

	// reply解析
	public List<Reply> parserSearchListItem(JSONArray array)
			throws JSONException {
		List<Reply> itemList = new ArrayList<Reply>();
		if (array != null && array.length() > 0) {
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				Reply reply = new Reply();

				if (PublicUtils.isValid(obj, "repliesId")) {
					reply.setReplyId(obj.getInt("repliesId"));
				}

				if (PublicUtils.isValid(obj, "topicId")) {
					reply.setTopicId(obj.getInt("topicId"));
				}
				if (PublicUtils.isValid(obj, "msgKey")) {
					reply.setMsgKey(obj.getString("msgKey"));
				}

				if (PublicUtils.isValid(obj, "repliesUserId")) {
					reply.setUserId(obj.getInt("repliesUserId"));
				}
				
				if (PublicUtils.isValid(obj, "pathLevel")) {
					reply.setLevel(obj.getInt("pathLevel"));
				} 

				if (PublicUtils.isValid(obj, "repliesTime")) {
					reply.setDate(obj.getString("repliesTime"));
				} 

				if (PublicUtils.isValid(obj, "repliesContent")) {
					reply.setContent(obj.getString("repliesContent"));
				}

				if (PublicUtils.isValid(obj, "repliesUser")) {
					reply.setReplyName(obj.getString("repliesUser"));
				} 

				if (PublicUtils.isValid(obj, "pathCode")) {
					reply.setPathCode(obj.getString("pathCode"));
				} 
				
				if (PublicUtils.isValid(obj, "headImg")) {
					reply.setUrl(obj.getString("headImg"));
					SharedPrefrencesForWechatUtil.getInstance(context).setUserHedaImg(String.valueOf(reply.getUserId()), reply.getUrl());
				} 
				if (PublicUtils.isValid(obj, "type")) {
					reply.setTopicType(obj.getString("type"));
				} 
				
				if(PublicUtils.isValid(obj, "isPublic")){
					reply.setIsPublic(obj.getString("isPublic"));
				}
				
				StringBuffer sb = new StringBuffer();
				if (PublicUtils.isValid(obj, "photo1")) {
					String p = obj.getString("photo1");
					sb.append(",").append(p);
				}
				if (PublicUtils.isValid(obj, "photo2")) {
					String p = obj.getString("photo2");
					sb.append(",").append(p);
				}
				if (PublicUtils.isValid(obj, "photo3")) {
					String p = obj.getString("photo3");
					sb.append(",").append(p);
				}
				if (PublicUtils.isValid(obj, "photo4")) {
					String p = obj.getString("photo4");
					sb.append(",").append(p);
				}
				if (PublicUtils.isValid(obj, "photo5")) {
					String p = obj.getString("photo5");
					sb.append(",").append(p);
				}
				if (PublicUtils.isValid(obj, "photo6")) {
					String p = obj.getString("photo6");
					sb.append(",").append(p);
				}
				if (PublicUtils.isValid(obj, "photo7")) {
					String p = obj.getString("photo7");
					sb.append(",").append(p);
				}
				if (PublicUtils.isValid(obj, "photo8")) {
					String p = obj.getString("photo8");
					sb.append(",").append(p);
				}
				if (PublicUtils.isValid(obj, "photo9")) {
					String p = obj.getString("photo9");
					sb.append(",").append(p);
				}

				if (sb.length()>0) {
					String photo = sb.substring(1);
					reply.setPhoto(photo);
				}
				if (PublicUtils.isValid(obj, "attachment")) {
					String attachment = obj.getString("attachment");
					reply.setAttachment(attachment);
				}
				
				
				if(PublicUtils.isValid(obj, "authUserId")){
					
					
					
					int authUserId = obj.getInt("authUserId");
										
					reply.setAuthUserId(authUserId);
				}
				
				if(PublicUtils.isValid(obj, "authUserName")){
					String authUserName = obj.getString("authUserName");
					reply.setAuthUserName(authUserName);
				}
				
				if(PublicUtils.isValid(obj, "delStatus")){
					String delStatus = obj.getString("delStatus");
					reply.setDelStatus(delStatus);
				}
				
				
//				if(PublicUtils.isValid(obj, "review")){
//					
//					
//					JSONArray reArr = 	obj.getJSONArray("review");
//					
//					for (int j = 0; j < reArr.length(); j++) {
//						
//						
//						JSONObject re = reArr.getJSONObject(j);
//						
//						if(PublicUtils.isValid(re, "authUserId")){
//							
//							
//							
//							int authUserId = re.getInt("authUserId");
//							if(authUserId==0){
//								continue;						}
//							reply.setAuthUserId(authUserId);
//						}
//						
//						if(PublicUtils.isValid(re, "authUserName")){
//							String authUserName = re.getString("authUserName");
//							reply.setAuthUserName(authUserName);
//						}
//						
//						if(PublicUtils.isValid(re, "delStatus")){
//							String delStatus = re.getString("delStatus");
//							reply.setDelStatus(delStatus);
//						}
//						
//						
//						
//					}
//					}
					
				
				
				itemList.add(reply);
			}
		}

		return itemList;
	}

	// comment解析
	public List<Comment> parserSearchCommentList(JSONArray array)
			throws JSONException {
		List<Comment> itemList = new ArrayList<Comment>();
		if (array != null && array.length() > 0) {
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				JLog.d("abby", obj.toString());
				Comment comment = new Comment();

				if (PublicUtils.isValid(obj, "repliesId")) {
					comment.setReplyId(obj.getInt("repliesId"));
				}
				if (PublicUtils.isValid(obj, "commentId")) {
					comment.setCommentId(obj.getInt("commentId"));
				}
				if (PublicUtils.isValid(obj, "msgKey")) {
					comment.setMsgKey(obj.getString("msgKey"));
				}

				if (PublicUtils.isValid(obj, "repliesContent")) {
					comment.setComment(obj.getString("repliesContent"));
				}

				if (PublicUtils.isValid(obj, "repliesUserId")) {
					comment.setcUserId(obj.getInt("repliesUserId"));
				}

				if (PublicUtils.isValid(obj, "repliesUser")) {
					comment.setcUserName(obj.getString("repliesUser"));
				} 
				if (PublicUtils.isValid(obj, "toUserId")) {
					comment.setdUserId(obj.getInt("toUserId"));
				}

				if (PublicUtils.isValid(obj, "toUser")) {
					comment.setdUserName(obj.getString("toUser"));
				} 

				if (PublicUtils.isValid(obj, "pathCode")) {
					comment.setPathCode(obj.getString("pathCode"));
				} 

				if (PublicUtils.isValid(obj, "isPublic")) {
					comment.setIsPublic(obj.getString("isPublic"));
				} 
				
				if (PublicUtils.isValid(obj, "topicId")) {
					comment.setTopicId("" + obj.getInt("topicId"));
				} 
				
				if (PublicUtils.isValid(obj, "repliesTime")) {
					comment.setDate(obj.getString("repliesTime"));
				} 
				
				if(PublicUtils.isValid(obj, "authUserId")){
					comment.setAuthUserId(obj.getInt("authUserId"));
				}
				
				if(PublicUtils.isValid(obj, "authUserName")){
					comment.setAuthUserName(obj.getString("authUserName"));
				}

				itemList.add(comment);
				JLog.d("abby", "size" + itemList.size());
			}
		}

		return itemList;
	}

	// 获取群属性解析
	public Topic parserSearchTopicListItem(JSONObject obj) throws JSONException {
		Topic topic = new Topic();
		if (topic != null) {

			if (PublicUtils.isValid(obj, "explain")) {
				topic.setExplain(obj.getString("explain"));
			}
			if (PublicUtils.isValid(obj, "title")) {
				topic.setTitle(obj.getString("title"));
			}

			if (PublicUtils.isValid(obj, "startDate")) {
				topic.setFrom(obj.getString("startDate"));
			}
			if (PublicUtils.isValid(obj, "endDate")) {
				topic.setTo(obj.getString("endDate"));
			}

		}

		return topic;
	}
	public Notification parserSearchNotification(String json,Notification notifi) throws JSONException{
		if (!TextUtils.isEmpty(json)) {
			JSONObject objecy = new JSONObject(json);
//			JSONObject obj = null;
			if(PublicUtils.isValid(objecy, "notice")){
				JSONArray array = objecy.getJSONArray("notice");
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj = array.getJSONObject(i);
					if(PublicUtils.isValid(obj, "issuerName")){
						notifi.setCreater(obj.getString("issuerName"));//发布人
					}
					if(PublicUtils.isValid(obj, "issuerTime")){
						notifi.setCreateDate(obj.getString("issuerTime"));//创建时间
					}
					if(PublicUtils.isValid(obj, "title")){
						notifi.setTitle(obj.getString("title"));//标题
					}
					if(PublicUtils.isValid(obj, "content")){
						notifi.setContent(obj.getString("content"));//内容
					}
					if(PublicUtils.isValid(obj, "isFollow")){
						notifi.setIsNoticed(obj.getString("isFollow"));
					}
					if(PublicUtils.isValid(obj, "orgName")){
						String orgName = obj.getString("orgName");
						notifi.setCreateOrg(orgName);
					}
					if (PublicUtils.isValid(obj, "attachment")) {
						String attachment = obj.getString("attachment");
//						JSONObject  attObj = new JSONObject(attachment);
						notifi.setAttachment(attachment);
//						Iterator<String> iterator = attObj.keys();
//						while (iterator.hasNext()) {
//							String key = iterator.next();
//							String url = attObj.getString(key);
//							if (!(url.endsWith(".zip") || url.endsWith(".rar"))) {//压缩文件格式在手机端不显示
//								NotifyAttachmentView attView = new NotifyAttachmentView(this);
//								attView.setAttachmentData(key,url);
//								ll_attachment.addView(attView.getView());
//							}
//						}
					}
				}
			}
			notificationDB.updateNotification(notifi);
		}
		return notifi;
	}
	
	/**
	 * 移除企业微信
	 */
	public void removeWechat(){
		new MainMenuDB(context).removeMenuByType(Menu.WEI_CHAT);
	}
	
	/**
	 * 初始化企业微信
	 */
	public void initWechat(String msgId){
		new AttentionDB(context).deleteAll();
		new CommentDB(context).deleteAll();
		new NotificationDB(context).deleteAll();
		new PersonalWechatDB(context).deleteAll();
		new ReplyDB(context).deleteAll();
		new SurveyDB(context).deleteAll();
		new TopicDB(context).deleteAll();
		new TopicDB(context).deleteAll();
		new ZanDB(context).deleteAll();
		netCheck(msgId);
	}
	
	/**
	 * 同步企业微信配置信息
	 */
	private void syncChatConfInfo(final String msgId){
		String url = UrlInfo.weChatConfInfo(context);
		GcgHttpClient.getInstance(context).get(url, null, new  HttpResponseListener() {
			
			@Override
			public void onSuccess(int statusCode, String content) {
				try {
					JSONObject obj = new JSONObject(content);
					new CacheData(context).parserWeiChat(obj);
				} catch (Exception e) {
					e.printStackTrace();
				}
//				new PushItemDB(context).deletePushItemByMsgId(msgId);
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
	
	public void netCheck(final String msgId){
		
//		String url = UrlInfo.netCheck(context);
//		GcgHttpClient.getInstance(context).get(url, null, new  HttpResponseListener() {
//			
//			@Override
//			public void onSuccess(int statusCode, String content) {
//				syncChatConfInfo(msgId);
//			}
//			
//			@Override
//			public void onStart() {
//				
//			}
//			
//			@Override
//			public void onFinish() {
//				
//			}
//			
//			@Override
//			public void onFailure(Throwable error, String content) {
//				
//			}
//		});
		
		syncChatConfInfo(msgId);

	}
}
