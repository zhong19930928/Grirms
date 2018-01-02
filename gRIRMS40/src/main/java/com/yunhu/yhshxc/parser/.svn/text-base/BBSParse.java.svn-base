package com.yunhu.yhshxc.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.yunhu.yhshxc.bo.BbsCommentItem;
import com.yunhu.yhshxc.bo.BbsInformationDetail;
import com.yunhu.yhshxc.bo.BbsInformationItem;
import com.yunhu.yhshxc.bo.BbsUserInfo;
import com.yunhu.yhshxc.bo.PhotoInfo;
import com.yunhu.yhshxc.utility.Constants;

public class BBSParse {
	
	private final String USER_FRIENDS = "friends";
	private final String USER_NAME = "name";
	private final String USER_ID = "userid";
	private final String USER_SCORE = "score";
			
	private final String COMMENT = "post";
	private final String COMMENT_ID = "postid";
	private final String COMMENT_CONTENT = "postcontent";
	
	private final String CREATE_TIME = "createtime";
	private final String CREATE_USER = "createuser";
	
	private final String FORUMS = "forums";
	private final String PARTAKE_CONTENT = "partakecontent";
	private final String ADDRESS = "address";
	private final String COMMENT_COUNT = "count";
	private final String INFORMATION_ID = "msgid";
	
	private final String PHOTO_NAME = "photoname";
	private final String PHOTO_TITLE = "phototitle";
	private final String PHOTO_ID = "photoid";
	private final String PHOTO_URL = "photourl";

	/**
	 * 解析信息流
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public List<BbsInformationItem> parseInformation(String json) throws Exception{
		List<BbsInformationItem> list = null;
		if(!TextUtils.isEmpty(json)){
			JSONObject jsonObject = new JSONObject(json);
			if(jsonObject.has(Constants.RESULT_CODE)){
				String resultcode = jsonObject.getString(Constants.RESULT_CODE);
				if(!resultcode.equals(Constants.RESULT_CODE_SUCCESS)){
					return null;
				}
			}
			if(jsonObject.has(FORUMS)){
				list = new ArrayList<BbsInformationItem>();
				JSONArray arr = jsonObject.getJSONArray(FORUMS);
				JSONObject itemObj = null;
				int len = arr.length();
				BbsInformationItem infoItem = null;
				BbsUserInfo userInfo = null;
				BbsInformationDetail detail = null;
				for(int i=0; i<len; i++){
					itemObj = arr.getJSONObject(i);
					infoItem = new BbsInformationItem();
					
					//发帖人
					userInfo = new BbsUserInfo();
					if(isValid(itemObj,USER_ID)){
						userInfo.setName(itemObj.getString(USER_ID));
					}
					if(isValid(itemObj,CREATE_USER)){
						userInfo.setName(itemObj.getString(CREATE_USER));
					}
					if(isValid(itemObj,USER_SCORE)){
						userInfo.setScore(itemObj.getString(USER_SCORE));
					}
					infoItem.setUserInfo(userInfo);
					
					//内容
					detail = compBbsInformationDetail(itemObj);
					infoItem.setBbsInfoDetail(detail);
					
					
					//2条回复
					infoItem.setBbsCommentItemList(parseComment(itemObj.toString()));
					
					list.add(infoItem);
				}
			}
		}
		
		return list;
	}
	
	private BbsInformationDetail compBbsInformationDetail(JSONObject jsonObject) throws Exception{
		BbsInformationDetail detail = new BbsInformationDetail();
		if(isValid(jsonObject,PARTAKE_CONTENT)){
			detail.setContent(jsonObject.getString(PARTAKE_CONTENT));
		}
		if(isValid(jsonObject,ADDRESS)){
			detail.setAddress(jsonObject.getString(ADDRESS));
		}
		if(isValid(jsonObject,COMMENT_COUNT)){
			detail.setCommentNum(jsonObject.getString(COMMENT_COUNT));
		}
		if(isValid(jsonObject,CREATE_TIME)){
			detail.setCreateTime(jsonObject.getString(CREATE_TIME));
		}
		if(isValid(jsonObject,INFORMATION_ID)){
			detail.setId(jsonObject.getString(INFORMATION_ID));
		}
		PhotoInfo photo = new PhotoInfo();
		if(isValid(jsonObject,PHOTO_ID)){
			photo.setId(jsonObject.getString(PHOTO_ID));
		}
		if(isValid(jsonObject,PHOTO_NAME)){
			photo.setPhotoName(jsonObject.getString(PHOTO_NAME));
		}
		if(isValid(jsonObject,PHOTO_TITLE)){
			photo.setPhotoTitle(jsonObject.getString(PHOTO_TITLE));
		}
		if(isValid(jsonObject,PHOTO_URL)){
			photo.setPhotoUrl(jsonObject.getString(PHOTO_URL));
		}
		detail.setPhotoInfo(photo);
		return detail;
	}
	
	
	/**
	 * 解析圈子中用户信息
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public List<BbsUserInfo> parseUserInfo(String json) throws Exception{
		List<BbsUserInfo> list = null;
		if(!TextUtils.isEmpty(json)){
			JSONObject jsonObject = new JSONObject(json);
			if(jsonObject.has(Constants.RESULT_CODE)){
				String resultcode = jsonObject.getString(Constants.RESULT_CODE);
				if(!resultcode.equals(Constants.RESULT_CODE_SUCCESS)){
					return null;
				}
			}
			if(jsonObject.has(USER_FRIENDS)){
				list = new ArrayList<BbsUserInfo>();
				JSONArray friendsArr = jsonObject.getJSONArray(USER_FRIENDS);
				int len = friendsArr.length();
				BbsUserInfo userInfo = null;
				for(int i=0; i<len; i++){
					userInfo = compUserInfo(friendsArr.getJSONObject(i));
					list.add(userInfo);
				}
			}
		}
		return list;
	}
	
	private BbsUserInfo compUserInfo(JSONObject jsonObject) throws Exception{
		BbsUserInfo info = new BbsUserInfo();
		if(isValid(jsonObject,USER_NAME)){
			info.setName(jsonObject.getString(USER_NAME));
		}
		if(isValid(jsonObject,USER_ID)){
			info.setId(jsonObject.getString(USER_ID));
		}
		if(isValid(jsonObject,USER_SCORE)){
			info.setScore(jsonObject.getString(USER_SCORE));
		}
		return info;
	}
	
	/**
	 * 解析回复信息
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public List<BbsCommentItem> parseComment(String json) throws Exception{
		List<BbsCommentItem> list = null;
		if(!TextUtils.isEmpty(json)){
			JSONObject jsonObject = new JSONObject(json);
			if(jsonObject.has(Constants.RESULT_CODE)){
				String resultcode = jsonObject.getString(Constants.RESULT_CODE);
				if(!resultcode.equals(Constants.RESULT_CODE_SUCCESS)){
					return null;
				}
			}
			if(jsonObject.has(COMMENT)){
				list = new ArrayList<BbsCommentItem>();
				JSONArray arr = jsonObject.getJSONArray(COMMENT);
				int len = arr.length();
				BbsCommentItem comment = null;
				for(int i=0; i<len; i++){
					comment = compComment(arr.getJSONObject(i));
					list.add(comment);
				}
			}
		}
		return list;
	}
	
	private BbsCommentItem compComment(JSONObject jsonObject) throws Exception{
		BbsCommentItem comment = new BbsCommentItem();
		if(isValid(jsonObject,COMMENT_ID)){
			comment.setId(jsonObject.getString(COMMENT_ID));
		}
		if(isValid(jsonObject,COMMENT_CONTENT)){
			comment.setContent(jsonObject.getString(COMMENT_CONTENT));
		}
		if(isValid(jsonObject,CREATE_TIME)){
			comment.setCreateTime(jsonObject.getString(CREATE_TIME));
		}
		if(isValid(jsonObject,CREATE_USER)){
			comment.setCreateuser(jsonObject.getString(CREATE_USER));
		}
		if(isValid(jsonObject,USER_SCORE)){
			comment.setScore(jsonObject.getString(USER_SCORE));
		}
		return comment;
	}
	
	private boolean isValid(JSONObject jsonObject,String key) throws JSONException{
		boolean flag = false;
		if(jsonObject.has(key) && !jsonObject.isNull(key)){
			flag = !"".equals(jsonObject.getString(key));
		}
		return flag;
	}
}
