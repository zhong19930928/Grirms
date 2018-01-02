package com.yunhu.yhshxc.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.yunhu.yhshxc.bo.Attendance;
import com.yunhu.yhshxc.utility.Constants;

/**
 * 考勤解析
 * 
 * @version 2013.6.3
 *
 */
public class AttendanceParse {
	/**
	 * 考勤字段
	 */
	private final String ATTENDANCE = "attend";
	
	/**
	 * 考勤用户名
	 */
	private final String USER_NAME = "user_name";
	
	/**
	 * 上班考勤时间
	 */
	private final String IN_TIME = "in_time";
	
	/**
	 * 上班考勤的定位地址
	 */
	private final String IN_ADDRESS = "in_position";

	/**
	 * 上班考勤的纬度
	 */
	private final String IN_LAT = "in_gis_y";

	/**
	 * 上班考勤的经度
	 */
	private final String IN_LON = "in_gis_x";

	/**
	 * 上班考勤的定位类型
	 */
	private final String IN_GIS_TYPE = "in_gis_type";

	/**
	 * 上班考勤的照片
	 */
	private final String IN_IMAGE = "in_image";

	/**
	 * 上班考勤说明
	 */
	private final String IN_COMMENT = "in_comment";

	/**
	 * 下班考勤时间
	 */
	private final String OUT_TIME = "out_time";

	/**
	 * 下班考勤的定位地址
	 */
	private final String OUT_ADDRESS = "out_position";

	/**
	 * 下班考勤的纬度
	 */
	private final String OUT_LAT = "out_gis_y";

	/**
	 * 下班考勤的经度
	 */
	private final String OUT_LON = "out_gis_x";

	/**
	 * 下班考勤的定位类型
	 */
	private final String OUT_GIS_TYPE = "out_gis_type";

	/**
	 * 下班考勤的照片
	 */
	private final String OUT_IMAGE = "out_image";

	/**
	 * 下班考勤的说明
	 */
	private final String OUT_COMMENT = "out_comment";

	/**
	 * 考勤日期
	 */
	private final String CREATE_TIME = "create_time";
	
	/**
	 * 验证JSON数据是否为空
	 * @param jsonObject JSON数据对象
	 * @param key 数据Key
	 * @return 如果数据不为空则返回true，否则返回false
	 * @throws JSONException JSON解析异常
	 */
	private boolean isValid(JSONObject jsonObject,String key) throws JSONException{
		boolean flag = false;
		if(jsonObject.has(key) && !jsonObject.isNull(key)){
			flag = !"".equals(jsonObject.getString(key));
		}
		return flag;
	}
	
	/**
	 * 解析考勤JSON数据
	 * @param json JSON数据字符串
	 * @return 返回解析后的考勤对象列表
	 * @throws Exception 各种可能出现的异常
	 */
	public List<Attendance> parseAttendance(String json) throws Exception{
		List<Attendance> list = null;
		if(!TextUtils.isEmpty(json)){
			JSONObject jsonObject = new JSONObject(json);
			if(jsonObject.has(Constants.RESULT_CODE)){
				String resultcode = jsonObject.getString(Constants.RESULT_CODE);
				if(!resultcode.equals(Constants.RESULT_CODE_SUCCESS)){
					return null;
				}
			}
			if(jsonObject.has(ATTENDANCE)){
				
				list = new ArrayList<Attendance>();
				JSONArray attendArr = jsonObject.getJSONArray(ATTENDANCE);
				int len = attendArr.length();
				for(int i=0; i<len; i++){
					list.add(putAttend(attendArr.getJSONObject(i)));
				}
			}
			
		}
		return list;
	}
	
	/**
	 * 解析单条考勤数据
	 * @param jsonObject 单条考勤JSON数据对象
	 * @return 返回考勤对象
	 * @throws Exception 各种可能出现的异常
	 */
	private Attendance putAttend(JSONObject jsonObject) throws Exception{
		Attendance att = new Attendance();
		if(isValid(jsonObject,USER_NAME)){
			att.setUserName(jsonObject.getString(USER_NAME));
		}
		if(isValid(jsonObject,IN_TIME)){
			att.setInTime(jsonObject.getString(IN_TIME));
		}
		if(isValid(jsonObject,IN_ADDRESS)){
			att.setInAddress(jsonObject.getString(IN_ADDRESS));
		}
		if(isValid(jsonObject,IN_LAT)){
			att.setInLat(jsonObject.getString(IN_LAT));
		}
		if(isValid(jsonObject,IN_LON)){
			att.setInLon(jsonObject.getString(IN_LON));
		}
		if(isValid(jsonObject,IN_GIS_TYPE)){
			att.setInGisType(jsonObject.getString(IN_GIS_TYPE));
		}
		if(isValid(jsonObject,IN_IMAGE)){
			att.setInImage(jsonObject.getString(IN_IMAGE));
		}
		if(isValid(jsonObject,IN_COMMENT)){
			att.setInComment(jsonObject.getString(IN_COMMENT));
		}
		if(isValid(jsonObject,OUT_TIME)){
			att.setOutTime(jsonObject.getString(OUT_TIME));
		}
		if(isValid(jsonObject,OUT_ADDRESS)){
			att.setOutAddress(jsonObject.getString(OUT_ADDRESS));
		}
		if(isValid(jsonObject,OUT_LAT)){
			att.setOutLat(jsonObject.getString(OUT_LAT));
		}
		if(isValid(jsonObject,OUT_LON)){
			att.setOutLon(jsonObject.getString(OUT_LON));
		}
		if(isValid(jsonObject,OUT_GIS_TYPE)){
			att.setOutGisType(jsonObject.getString(OUT_GIS_TYPE));
		}
		if(isValid(jsonObject,OUT_IMAGE)){
			att.setOutImage(jsonObject.getString(OUT_IMAGE));
		}
		if(isValid(jsonObject,OUT_COMMENT)){
			att.setOutComment(jsonObject.getString(OUT_COMMENT));
		}
		if(isValid(jsonObject,CREATE_TIME)){
			att.setCreateTime(jsonObject.getString(CREATE_TIME));
		}
		return att;
	}
}
