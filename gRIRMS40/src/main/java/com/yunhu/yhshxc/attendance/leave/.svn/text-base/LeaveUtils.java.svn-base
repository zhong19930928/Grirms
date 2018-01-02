package com.yunhu.yhshxc.attendance.leave;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.yunhu.yhshxc.utility.PublicUtils;

public class LeaveUtils {
	private Context context;
	private AskForLeaveInfoDB infoDB;
	public LeaveUtils(Context context){
		this.context = context;
		infoDB = new AskForLeaveInfoDB(context);
	}
	/**
	 * 解析请假数据
	 * @throws JSONException 
	 */
	public List<AskForLeaveInfo> paseLeaveInfos(JSONArray array,boolean isFirst) throws JSONException{
		List<AskForLeaveInfo> list = new ArrayList<AskForLeaveInfo>();
		if (array != null && array.length() > 0) {
			if(isFirst){
				infoDB.delete();
			}
			for (int i = 0; i < array.length(); i++) {
				JSONObject searchObj = array.getJSONObject(i);
				AskForLeaveInfo info = new AskForLeaveInfo();

				if (PublicUtils.isValid(searchObj, "userId")) {
					info.setUserId(searchObj.getString("userId"));
				} 
				if (PublicUtils.isValid(searchObj, "name")) {
					info.setName(searchObj.getString("name"));
				} 
				if (PublicUtils.isValid(searchObj, "status")) {
					info.setStatus(searchObj.getString("status"));
				} 
				if (PublicUtils.isValid(searchObj, "statusName")) {
					info.setStatusName(searchObj.getString("statusName"));
				} 
				if (PublicUtils.isValid(searchObj, "marks")) {
					info.setMarks(searchObj.getString("marks"));
				} 
				if (PublicUtils.isValid(searchObj, "userName")) {
					info.setUserName(searchObj.getString("userName"));
				} 
				if (PublicUtils.isValid(searchObj, "startTime")) {
					info.setStartTime(searchObj.getString("startTime"));
				} 
				if (PublicUtils.isValid(searchObj, "endTime")) {
					info.setEndTime(searchObj.getString("endTime"));
				} 
				if (PublicUtils.isValid(searchObj, "imageUrl")) {
					info.setImageUrl(searchObj.getString("imageUrl"));
				} 
				if (PublicUtils.isValid(searchObj, "days")) {
					info.setDays(searchObj.getString("days"));
				} 
				if (PublicUtils.isValid(searchObj, "hours")) {
					info.setHours(searchObj.getString("hours"));
				} 
				if(PublicUtils.isValid(searchObj, "leaveId")){
					info.setType(searchObj.getString("leaveId"));
				}
				if(PublicUtils.isValid(searchObj, "leaveName")){
					info.setLeaveName(searchObj.getString("leaveName"));
				}
				if(PublicUtils.isValid(searchObj, "msgKey")){
					info.setMsgKey(searchObj.getString("msgKey"));
				}
				if(PublicUtils.isValid(searchObj, "auditComment")){
					info.setAuditComment(searchObj.getString("auditComment"));
				}
				if(PublicUtils.isValid(searchObj, "leaveDay")){
					info.setLeaveDay(searchObj.getString("leaveDay"));
				}
				if(PublicUtils.isValid(searchObj, "orgName")){
					info.setOrgName(searchObj.getString("orgName"));
				}
				list.add(info);
				infoDB.insert(info);
				
			}

		}
		return list;
	}
}
