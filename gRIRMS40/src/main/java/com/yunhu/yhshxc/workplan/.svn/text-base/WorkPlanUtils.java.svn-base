package com.yunhu.yhshxc.workplan;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.workplan.bo.Assess;
import com.yunhu.yhshxc.workplan.bo.PlanData;
import com.yunhu.yhshxc.workplan.bo.WorkPlanModle;
import com.yunhu.yhshxc.workplan.db.AssessDB;
import com.yunhu.yhshxc.workplan.db.PlanDataDB;
import com.yunhu.yhshxc.workplan.db.WorkPlanModleDB;

public class WorkPlanUtils {
	private Context context;
	PlanDataDB dataDB;
	WorkPlanModleDB modleDB;
	AssessDB assessDB ;
	public WorkPlanUtils(Context context){
		this.context = context;
		dataDB = new PlanDataDB(context);
		modleDB = new WorkPlanModleDB(context);
		assessDB = new AssessDB(context);
	}
	
	public List<PlanData> parsePlanDataList(JSONArray array) throws JSONException{
		List<PlanData> list = new ArrayList<PlanData>();
		dataDB.clearPlanDataList();
		if (array != null && array.length() > 0) {
			for (int i = 0; i < array.length(); i++) {
				JSONObject searchObj = array.getJSONObject(i);
				PlanData planData = new PlanData();

				if (PublicUtils.isValid(searchObj, "plan_id")) {
					planData.setPlanId(searchObj.getInt("plan_id"));
				} 
				if (PublicUtils.isValid(searchObj, "user_name")) {
					planData.setUserName(searchObj.getString("user_name"));
				}
				if (PublicUtils.isValid(searchObj, "user_code")) {
					planData.setUserCode(searchObj.getString("user_code"));
				}
				if (PublicUtils.isValid(searchObj, "org_name")) {
					planData.setOrgName(searchObj.getString("org_name"));
				} 
				if (PublicUtils.isValid(searchObj, "plan_title")) {
					planData.setPlanTitle(searchObj.getString("plan_title"));
				}
				if (PublicUtils.isValid(searchObj, "plan_date")) {
					planData.setPlanData(searchObj.getString("plan_date"));
				}
				if (PublicUtils.isValid(searchObj, "start_date")) {
					planData.setStartDate(searchObj.getString("start_date"));
				}
				if (PublicUtils.isValid(searchObj, "end_date")) {
					planData.setEndDate(searchObj.getString("end_date"));
				}
				if (PublicUtils.isValid(searchObj, "plan_type")) {
					planData.setPlanType(searchObj.getString("plan_type"));
				}
				list.add(planData);
				dataDB.insertPlanData(planData);
			}

		}
		
		return list;
	}
	public List<WorkPlanModle> parsePlanModleList(JSONArray array) throws JSONException{
		List<WorkPlanModle> list = new ArrayList<WorkPlanModle>();
		modleDB.clearWorkPlan();
		if (array != null && array.length() > 0) {
			for (int i = 0; i < array.length(); i++) {
				JSONObject searchObj = array.getJSONObject(i);
				WorkPlanModle planData = new WorkPlanModle();
				
				if (PublicUtils.isValid(searchObj, "plan_id")) {
					planData.setPlanId(searchObj.getInt("plan_id"));
				} 
				if (PublicUtils.isValid(searchObj, "plan_title")) {
					planData.setPlanTitle(searchObj.getString("plan_title"));
				}
				if (PublicUtils.isValid(searchObj, "plan_detail")) {
					planData.setPlanContent(searchObj.getString("plan_detail"));
				} 
				if (PublicUtils.isValid(searchObj, "plan_marks")) {
					planData.setPlanMark(searchObj.getString("plan_marks"));
				}
				if (PublicUtils.isValid(searchObj, "important_level")) {
					planData.setImportantLevel(searchObj.getInt("important_level"));
				}
				if (PublicUtils.isValid(searchObj, "urgency_level")) {
					planData.setRushLevel(searchObj.getInt("urgency_level"));
				}
				
				list.add(planData);
				modleDB.insertWorkPlan(planData);
			}
			
		}
		
		return list;
	}
	public List<Assess> parseAssessList(JSONArray array) throws JSONException{
		List<Assess> list = new ArrayList<Assess>();
		assessDB.clearAssess();
		if (array != null && array.length() > 0) {
			for (int i = 0; i < array.length(); i++) {
				JSONObject searchObj = array.getJSONObject(i);
				Assess planData = new Assess();
				
				if (PublicUtils.isValid(searchObj, "plan_id")) {
					planData.setPlan_id(searchObj.getInt("plan_id"));
				} 
				if (PublicUtils.isValid(searchObj, "user_id")) {
					planData.setUser_id(searchObj.getString("user_id"));
				}
				if (PublicUtils.isValid(searchObj, "user_name")) {
					planData.setUser_name(searchObj.getString("user_name"));
				} 
				if (PublicUtils.isValid(searchObj, "marks")) {
					planData.setMarks(searchObj.getString("marks"));
				}
				
				list.add(planData);
				assessDB.insertAssess(planData);
			}
			
		}
		
		return list;
	}
}
