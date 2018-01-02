package com.yunhu.yhshxc.wechat.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yunhu.yhshxc.database.DatabaseHelper;
import com.yunhu.yhshxc.wechat.bo.Topic;

public class TopicDB {
	private DatabaseHelper openHelper;
	private ReplyDB replyDB;

	public TopicDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
		replyDB = new ReplyDB(context);
	}
	
	/**
	 * 插入话题
	 * @param topic
	 */
	public void insert(Topic topic) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues cv = putContentValues(topic);
		db.insert(openHelper.TOPIC, null, cv);
	}

	/**
	 * 查询所有话题列表
	 * @return
	 */
	public List<Topic> findTopicList() {
		List<Topic> list = new ArrayList<Topic>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.TOPIC)
				.append(" where 1=1");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				Topic a = putTopic(cursor);
				list.add(a);
			}
		}
		cursor.close();
		return list;
	}
	
	/**
	 * 查询所有话题列表
	 * @return
	 */
	public List<Topic> findTopicListByIds(String ids) {
		List<Topic> list = new ArrayList<Topic>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.TOPIC)
				.append(" where id in (").append(ids).append(")");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				Topic a = putTopic(cursor);
				list.add(a);
			}
		}
		cursor.close();
		return list;
	}
	
	
	/**
	 * 查询关注的话题
	 * @param noticeId
	 */
	public List<Topic> findNotifacationListByIsTopic(int isAttention){
		List<Topic> list = new ArrayList<Topic>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.TOPIC).append(" where isAttention = ").append(isAttention);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor!=null && cursor.getCount()>0) {
			while (cursor.moveToNext()) {
				Topic a = putTopic(cursor);
				list.add(a);
			}
		}
		cursor.close();
		return list;
	}
	/**
	 * 更新话题
	 * @param topic
	 */
	public void updateTopic(Topic topic) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		db.update(openHelper.TOPIC, putContentValues(topic),
				"topicId=" + topic.getTopicId(), null);
	}

	/**
	 * 根据topicId查询单个话题
	 * @param topicId
	 */
	public void deleteTopic(int topicId) {
		openHelper.delete(openHelper.TOPIC, "topicId=?",
				new String[] { String.valueOf(topicId) });
	}
	/**
	 * 删除所有话题
	 */
	public void deleteAll(){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		StringBuffer sql=new StringBuffer();
		sql.append("delete from ").append(openHelper.TOPIC);
		db.execSQL(sql.toString());
		
	}
	
	/**
	 * 根据topicId查询话题
	 * @param topicId
	 * @return
	 */

	public Topic findTopicById(int topicId) {
		Topic topic = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.TOPIC)
				.append(" where topicId = ").append(topicId);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null) {
			if (cursor.moveToNext()) {
				topic = putTopic(cursor);
			}
			cursor.close();
		}
		return topic;
	}
	/**
	 * 更新话题是否关闭状态
	 * @param topicId
	 * @param colseState
	 */
	public void updateTopicCloseState(int topicId,int colseState) {
		StringBuffer sql = new StringBuffer();
		sql.append(" update ").append(openHelper.TOPIC);
		sql.append(" set isClose = ").append(colseState).append(" where topicId = ").append(topicId);
		openHelper.execSQL(sql.toString());
		
	}
	
	/**
	 * 更新最新回帖时间、内容
	 * @param topicId
	 * @param colseState
	 */
	public void updateTopicRecent(int topicId,String recentTime) {
		StringBuffer sql = new StringBuffer();
		sql.append(" update ").append(openHelper.TOPIC);
		sql.append(" set recentTime = ").append(recentTime).append(" where topicId = ").append(topicId);
		openHelper.execSQL(sql.toString());
		
	}
	
	/**
	 * 根据classify 查询公开话题列表
	 */

//	public List<Topic> findTopicListByClassify(int classify,String data,int isClose,String lastData,int id) {
////		List<Topic> list = new ArrayList<Topic>();
////		StringBuffer sql = new StringBuffer();
////		sql.append(" select * from ").append(openHelper.TOPIC);
////		sql.append(" where classify = ").append(classify);
////		sql.append(" and to_ >= '").append(data).append("'").append(" and to_ <> ''").append(" and isClose = ").append(isClose);
////		if (!TextUtils.isEmpty(lastData)) {//如果最后日期一条数据的日期是空的话
////			if (id == 0) {//说明是第一次加载
////				sql.append(" order by recentTime desc limit 5");
////			}else{
////				sql.append(" and id > ").append(id);
////				sql.append(" limit 5");
////			}
////		
////		}else{
////			sql.append(" order by recentTime desc limit 5");
////		}
////		JLog.d("alin", "sql  -----  "+sql.toString());
////		Cursor cursor = openHelper.query(sql.toString(), null);
////		if (cursor != null && cursor.getCount() > 0) {
////			while (cursor.moveToNext()) {
////				Topic a = putTopic(cursor);
////				list.add(a);
////			}
////		}
////		cursor.close();
////		
//		
//		List<Topic> list = new ArrayList<Topic>();
//		StringBuffer sql = new StringBuffer();
//		sql.append(" select * from ").append(openHelper.TOPIC);
//		sql.append(" where classify = ").append(classify);
//		sql.append(" and to_ >= '").append(data).append("'").append(" and isClose = ").append(isClose);
//		if (!TextUtils.isEmpty(lastData) || id == 0) {//如果最后日期一条数据的日期是空的话
//			sql.append(" and recentTime <> ''").append(" and recentTime is not null ");
//			if (!TextUtils.isEmpty(lastData)) {
//				sql.append(" and recentTime < '").append(lastData).append("'");
//			}
//			sql.append(" order by recentTime desc limit 5");
//			Cursor cursor = openHelper.query(sql.toString(), null);
//			if (cursor != null && cursor.getCount() > 0) {
//				while (cursor.moveToNext()) {
//					Topic a = putTopic(cursor);
//					list.add(a);
//				}
//			}
//			cursor.close();
//			
//			if (list.isEmpty()) {//空的话说明有时间的已经查完，那就查没有时间的
//				StringBuffer sql2 = new StringBuffer();
//				sql2.append(" select * from ").append(openHelper.TOPIC);
//				sql2.append(" where classify = ").append(classify);
//				sql2.append(" and to_ >= '").append(data).append("'").append(" and to_ <> ''").append(" and isClose = ").append(isClose);
//				sql2.append(" and recentTime is null");
//				sql2.append(" and id > ").append(id);
//				sql2.append(" order by recentTime desc limit 5");
//				Cursor cursor2 = openHelper.query(sql2.toString(), null);
//				if (cursor2 != null && cursor2.getCount() > 0) {
//					while (cursor2.moveToNext()) {
//						Topic a = putTopic(cursor2);
//						list.add(a);
//					}
//				}
//				cursor2.close();
//			}
//		}else{
//			StringBuffer sql2 = new StringBuffer();
//			sql2.append(" select * from ").append(openHelper.TOPIC);
//			sql2.append(" where classify = ").append(classify);
//			sql2.append(" and to_ >= '").append(data).append("'").append(" and to_ <> ''").append(" and isClose = ").append(isClose);
//			sql2.append(" and (recentTime is null or recentTime = '')");
//			sql2.append(" and id > ").append(id);
//			sql2.append(" order by recentTime desc limit 5");
//			Cursor cursor2 = openHelper.query(sql2.toString(), null);
//			if (cursor2 != null && cursor2.getCount() > 0) {
//				while (cursor2.moveToNext()) {
//					Topic a = putTopic(cursor2);
//					list.add(a);
//				}
//			}
//			cursor2.close();
//		}
//		JLog.d("alin", "sql  -----  "+sql.toString());
//		return list;
//	}
	public List<Topic> findTopicListForPublic(String data,int isClose){
		List<Topic> list = new ArrayList<Topic>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.TOPIC)
		.append(" where classify = ").append(Topic.CLASSIFY_TYPE_GK).append(" and to_ >= '").append(data).append("'").append(" and to_ <> ''").append(" and isClose = ").append(isClose).append(" order by recentTime desc");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				Topic a = putTopic(cursor);
				list.add(a);
			}
		}
		cursor.close();
		return list;
	}
	/**
	 * 查询所有的过期未关闭话题
	 * isClosed = 0;
	 */
	public void findTopicListForOverDue(String data,int isClose){
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.TOPIC)
		.append(" where  to_ < '").append(data).append("'").append(" and to_ <> ''").append(" and isClose = ").append(isClose);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				Topic a = putTopic(cursor);
				if(a!=null){
					replyDB.updateAllReplyToIsRead(a.getTopicId());
				}
			}
		}
		cursor.close();
	}
	/**
	 * 查询部门话题
	 */
//	public List<Topic> findTopicListForBuM(String data,int isClose,String lastData,int id){
////		List<Topic> list = new ArrayList<Topic>();
////		StringBuffer sql = new StringBuffer();
////		sql.append(" select * from ").append(openHelper.TOPIC);
////		sql.append(" where classify <> ").append(Topic.CLASSIFY_TYPE_GK);
////		sql.append(" and to_ >= '").append(data).append("'").append(" and to_ <> ''").append(" and isClose = ").append(isClose);
////		if(TextUtils.isEmpty(lastData)){//如果最后日期一条数据的日期是空的话
////			if(id == 0){
////				sql.append(" order by recentTime desc limit 5");
////			}else {
////				sql.append(" and id > ").append(id);
////				sql.append(" limit 5");
////			}
////		}else{
////			sql.append(" order by recentTime desc limit 5");	
////			}
////				
////			Cursor cursor = openHelper.query(sql.toString(), null);
////			if (cursor != null && cursor.getCount() > 0) {
////				while (cursor.moveToNext()) {
////					Topic a = putTopic(cursor);
////					list.add(a);
////				}
////			}
////		cursor.close();
//		
//		List<Topic> list = new ArrayList<Topic>();
//		StringBuffer sql = new StringBuffer();
//		sql.append(" select * from ").append(openHelper.TOPIC);
//		sql.append(" where classify <> ").append(Topic.CLASSIFY_TYPE_GK);
//		sql.append(" and to_ >= '").append(data).append("'").append(" and isClose = ").append(isClose);
//		if (!TextUtils.isEmpty(lastData) || id == 0) {//如果最后日期一条数据的日期是空的话
//			sql.append(" and recentTime <> ''").append(" and recentTime is not null ");
//			if (!TextUtils.isEmpty(lastData)) {
//				sql.append(" and recentTime < '").append(lastData).append("'");
//			}
//			sql.append(" order by recentTime desc limit 5");
//			Cursor cursor = openHelper.query(sql.toString(), null);
//			if (cursor != null && cursor.getCount() > 0) {
//				while (cursor.moveToNext()) {
//					Topic a = putTopic(cursor);
//					list.add(a);
//				}
//			}
//			cursor.close();
//			
//			if (list.isEmpty()) {//空的话说明有时间的已经查完，那就查没有时间的
//				StringBuffer sql2 = new StringBuffer();
//				sql2.append(" select * from ").append(openHelper.TOPIC);
//				sql2.append(" where classify <> ").append(Topic.CLASSIFY_TYPE_GK);
//				sql2.append(" and to_ >= '").append(data).append("'").append(" and to_ <> ''").append(" and isClose = ").append(isClose);
//				sql2.append(" and recentTime is null");
//				sql2.append(" and id > ").append(id);
//				sql2.append(" order by recentTime desc limit 5");
//				Cursor cursor2 = openHelper.query(sql2.toString(), null);
//				if (cursor2 != null && cursor2.getCount() > 0) {
//					while (cursor2.moveToNext()) {
//						Topic a = putTopic(cursor2);
//						list.add(a);
//					}
//				}
//				cursor2.close();
//			}
//		}else{
//			StringBuffer sql2 = new StringBuffer();
//			sql2.append(" select * from ").append(openHelper.TOPIC);
//			sql2.append(" where classify <> ").append(Topic.CLASSIFY_TYPE_GK);
//			sql2.append(" and to_ >= '").append(data).append("'").append(" and to_ <> ''").append(" and isClose = ").append(isClose);
//			sql2.append(" and (recentTime is null or recentTime = '')");
//			sql2.append(" and id > ").append(id);
//			sql2.append(" order by recentTime desc limit 5");
//			Cursor cursor2 = openHelper.query(sql2.toString(), null);
//			if (cursor2 != null && cursor2.getCount() > 0) {
//				while (cursor2.moveToNext()) {
//					Topic a = putTopic(cursor2);
//					list.add(a);
//				}
//			}
//			cursor2.close();
//		}
//		JLog.d("alin", "sql  -----  "+sql.toString());
//		
//		return list;
//	}
	public List<Topic> findTopicListForBuM(String data,int isClose){
		List<Topic> list = new ArrayList<Topic>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.TOPIC)
		.append(" where classify <> ").append(Topic.CLASSIFY_TYPE_GK).append(" and to_ >= '").append(data).append("'").append(" and to_ <> ''").append(" and isClose = ").append(isClose).append(" order by recentTime desc");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				Topic a = putTopic(cursor);
				list.add(a);
			}
		}
		cursor.close();
		return list;
	}
	
	/**
	 * 查询所有有效期内未关闭的话题
	 */
	public List<Topic> findTopicListByIsClosed(String data,int isClose){
		List<Topic> list = new ArrayList<Topic>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.TOPIC)
				.append(" where to_ >= '").append(data).append("'").append(" and to_ <> ''").append(" and isClose = ").append(isClose).append(" order by recentTime desc");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				Topic a = putTopic(cursor);
				list.add(a);
			}
		}
		cursor.close();
		return list;
	}

	/**
	 * 查询历史话题，话题结束日期小于传入日期
	 * @param data
	 * @return
	 */
//	public List<Topic> findHistoryTopicList(String data,int isClose,String lastData,int id){
////		List<Topic> list = new ArrayList<Topic>();
////		StringBuffer sql = new StringBuffer();
////		sql.append(" select * from ").append(openHelper.TOPIC);
////		sql.append(" where to_ < '").append(data).append("'").append(" and to_ <> ''");
////		sql.append(" and isClose = ").append(isClose);
////		if (TextUtils.isEmpty(lastData)) {//如果最后日期一条数据的日期是空的话
////			if (id == 0) {//说明是第一次加载
////				sql.append(" order by recentTime desc limit 5");
////			}else{
////				sql.append(" and id > ").append(id);
////				sql.append(" limit 5");
////			}
////		
////		}else{
////			sql.append(" order by recentTime desc limit 5");
////		}
////		
////		Cursor cursor = openHelper.query(sql.toString(), null);
////		if (cursor != null && cursor.getCount() > 0) {
////			while (cursor.moveToNext()) {
////				Topic a = putTopic(cursor);
////				list.add(a);
////			}
////		}
////		cursor.close();
//		
//		List<Topic> list = new ArrayList<Topic>();
//		StringBuffer sql = new StringBuffer();
//		sql.append(" select * from ").append(openHelper.TOPIC);
//		sql.append(" where to_ < '").append(data).append("'").append(" and to_ <> ''").append(" and isClose = ").append(isClose);
//		if (!TextUtils.isEmpty(lastData) || id == 0) {//如果最后日期一条数据的日期是空的话
//			sql.append(" and recentTime <> ''").append(" and recentTime is not null ");
//			if (!TextUtils.isEmpty(lastData)) {
//				sql.append(" and recentTime < '").append(lastData).append("'");
//			}
//			sql.append(" order by recentTime desc limit 5");
//			Cursor cursor = openHelper.query(sql.toString(), null);
//			if (cursor != null && cursor.getCount() > 0) {
//				while (cursor.moveToNext()) {
//					Topic a = putTopic(cursor);
//					list.add(a);
//				}
//			}
//			cursor.close();
//			
//			if (list.isEmpty()) {//空的话说明有时间的已经查完，那就查没有时间的
//				StringBuffer sql2 = new StringBuffer();
//				sql2.append(" select * from ").append(openHelper.TOPIC);
//				sql2.append(" where to_ < '").append(data).append("'").append(" and to_ <> ''").append(" and isClose = ").append(isClose);
//				sql2.append(" and recentTime is null");
//				sql2.append(" and id > ").append(id);
//				sql2.append(" order by recentTime desc limit 5");
//				Cursor cursor2 = openHelper.query(sql2.toString(), null);
//				if (cursor2 != null && cursor2.getCount() > 0) {
//					while (cursor2.moveToNext()) {
//						Topic a = putTopic(cursor2);
//						list.add(a);
//					}
//				}
//				cursor2.close();
//			}
//		}else{
//			StringBuffer sql2 = new StringBuffer();
//			sql2.append(" select * from ").append(openHelper.TOPIC);
//			sql2.append(" where to_ < '").append(data).append("'").append(" and to_ <> ''").append(" and isClose = ").append(isClose);
//			sql2.append(" and (recentTime is null or recentTime = '')");
//			sql2.append(" and id > ").append(id);
//			sql2.append(" order by recentTime desc limit 5");
//			Cursor cursor2 = openHelper.query(sql2.toString(), null);
//			if (cursor2 != null && cursor2.getCount() > 0) {
//				while (cursor2.moveToNext()) {
//					Topic a = putTopic(cursor2);
//					list.add(a);
//				}
//			}
//			cursor2.close();
//		}
//		
//		
//		return list;
//	}
	public List<Topic> findHistoryTopicList(String data,int isClose){
		List<Topic> list = new ArrayList<Topic>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.TOPIC)
		.append(" where to_ < '").append(data).append("'").append(" and to_ <> ''").append(" or isClose = ").append(isClose).append(" order by recentTime desc");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				Topic a = putTopic(cursor);
				list.add(a);
			}
		}
		cursor.close();
		return list;
	}
	
	private ContentValues putContentValues(Topic topic) {
		ContentValues cv = new ContentValues();
		cv.put("topicId", topic.getTopicId());
		cv.put("groupId", topic.getGroupId());
		cv.put("title", topic.getTitle());
		cv.put("explain", topic.getExplain());
		cv.put("type", topic.getType());
		cv.put("from_", topic.getFrom());
		cv.put("to_", topic.getTo());
		cv.put("speakNum", topic.getSpeakNum());
		cv.put("isReply", topic.getIsReply());
		cv.put("comment", topic.getComment());
		cv.put("replyReview", topic.getReplyReview());
		cv.put("createUserId", topic.getCreateUserId());
		cv.put("createTime", topic.getCreateTime());
		cv.put("classify", topic.getClassify());
		cv.put("recentTime", topic.getRecentTime());
		cv.put("options", topic.getOptions());
		cv.put("selectType", topic.getSelectType());
		cv.put("isAttention", topic.getIsAttention());
		cv.put("isClose", topic.getIsClose());
		cv.put("recentContent", topic.getRecentContent());
		cv.put("msgKey", topic.getMsgKey());
		cv.put("createUserName", topic.getCreateUserName());
		cv.put("createOrgName", topic.getCreateOrgName());
		return cv;
	}

	private Topic putTopic(Cursor cursor) {
		int i = 0;
		Topic module = new Topic();
		module.setId(cursor.isNull(i) ? null : cursor.getInt(i));
		i++;
		module.setTopicId(cursor.getInt(i++));
		module.setGroupId(cursor.getInt(i++));
		module.setTitle(cursor.getString(i++));
		module.setExplain(cursor.getString(i++));
		module.setType(cursor.getString(i++));
		module.setFrom(cursor.getString(i++));
		module.setTo(cursor.getString(i++));
		module.setSpeakNum(cursor.getInt(i++));
		module.setIsReply(cursor.getInt(i++));
		module.setComment(cursor.getInt(i++));
		module.setReplyReview(cursor.getInt(i++));
		module.setCreateUserId(cursor.getInt(i++));
		module.setCreateTime(cursor.getString(i++));
		module.setClassify(cursor.getInt(i++));
		module.setRecentTime(cursor.getString(i++));
		module.setOptions(cursor.getString(i++));
		module.setSelectType(cursor.getString(i++));
		module.setIsAttention(cursor.getInt(i++));
		module.setIsClose(cursor.getInt(i++));
		module.setRecentContent(cursor.getString(i++));
		module.setMsgKey(cursor.getString(i++));
		module.setCreateUserName(cursor.getString(i++));
		module.setCreateOrgName(cursor.getString(i++));
		return module;
	}

}
