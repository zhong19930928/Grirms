package com.yunhu.yhshxc.parser;

import gcg.org.debug.JLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

import android.text.TextUtils;

/**
 * 解析表格数据
 * @author jishen
 *
 */
public class TableParse {

	private final String TAG = "TableParse";
	/**
	 * 将表格中的控件选择的值转化为json数据
	 * @param tableList
	 * @return
	 */
	public String toJason(List<HashMap<String,String>> tableList) {
		String json = null;
		JSONArray jsonArray = new JSONArray();
		if (tableList != null) {
			try {
				JSONObject jsonObject = null;
				for (HashMap<String,String> columnMap : tableList) {
					jsonObject = new JSONObject();
					if(columnMap != null){
						for(Entry<String, String> entry : columnMap.entrySet()){
							jsonObject.put(entry.getKey(),entry.getValue());
						}
					}
					jsonArray.put(jsonObject);
				}
				json = jsonArray.toString();
				JLog.d(TAG, "json=>" + json);
			} catch (Exception e) {
				JLog.d(TAG, "toJason=>" + e.getMessage());
			}
		}

		return json;
	}

	/**
	 * 将表格的json数据转化为map的形式 key是控件ID value是控件的值
	 * @param json
	 * @return
	 */
	public List<HashMap<String,String>> parseJason(String json) {
		List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		if (!TextUtils.isEmpty(json)) {
			try {
				JSONArray jsonArray = new JSONArray(json);
				JSONObject jsonObject = null;
				int length = jsonArray.length();
				HashMap<String,String> columnMap = null;
				for (int i = 0; i < length; i++) {
					columnMap = new HashMap<String,String>();
					jsonObject = jsonArray.getJSONObject(i);
					for (Iterator iterator = jsonObject.keys(); iterator.hasNext();) {
						String key = (String)iterator.next();
						columnMap.put(key, !jsonObject.isNull(key)?jsonObject.getString(key):"");
					}
					list.add(columnMap);
				}
			} catch (Exception e) {
				JLog.d(TAG, "parseJason=>" + e.getMessage());
				list = null;
			}

		}
		return list;

	}
	
	/**
	 * 将一行表格的json数据转化为map的形式 key是控件ID value是控件的值
	 * @return 表格中的一行数据
	 * 串号属性平铺
	 */
	public HashMap<String,String> parseRowJason(String json) {
		HashMap<String,String> columnMap = new HashMap<String,String>();
		if (!TextUtils.isEmpty(json)) {
			try {
				JSONArray jsonArray = new JSONArray(json);
				JSONObject jsonObject = null;
				int length = jsonArray.length();
				for (int i = 0; i < length; i++) {
					jsonObject = jsonArray.getJSONObject(i);
					for (Iterator iterator = jsonObject.keys(); iterator.hasNext();) {
						String key = (String)iterator.next();
						columnMap.put(key, !jsonObject.isNull(key)?jsonObject.getString(key):"");
					}
				}
			} catch (Exception e) {
				JLog.d(TAG, "parseJason=>" + e.getMessage());
			}

		}
		return columnMap;

	}
	
	/**
	 * 获取表格中图片的值的集合
	 * @param json 表格的值
	 * @return
	 */
	public List<String> getImageList(String json){
		List<String> imageList=new ArrayList<String>();
		if (!TextUtils.isEmpty(json)) {
			try {
				JSONArray jsonArray = new JSONArray(json);
				JSONObject jsonObject = null;
				int length = jsonArray.length();
				for (int i = 0; i < length; i++) {
					jsonObject = jsonArray.getJSONObject(i);
					for (Iterator iterator = jsonObject.keys(); iterator.hasNext();) {
						String key = (String)iterator.next();
						String image=!jsonObject.isNull(key)?jsonObject.getString(key):"";
						if(!TextUtils.isEmpty(image) && image.startsWith("grirms") && image.endsWith(".jpg")){
							imageList.add(image);
						}
					}
				}
			} catch (Exception e) {
				JLog.d(TAG, "parseJason=>" + e.getMessage());
			}

		}
		return imageList;
	}
}
