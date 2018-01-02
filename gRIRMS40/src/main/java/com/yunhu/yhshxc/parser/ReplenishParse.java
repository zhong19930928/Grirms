package com.yunhu.yhshxc.parser;

import gcg.org.debug.JLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.yunhu.yhshxc.bo.ReplenishSearchResult;
import com.yunhu.yhshxc.utility.Constants;

public class ReplenishParse {

	private final String TAG = "ReplenishParse";
    //private final String SEARCH_TAB = "searchtab"; // 表格数据
    private final String SEARCH_DATA = "searchdata"; // 控件数据
    private final String SEARCH_TABLE = "searchtable"; // 表名
    private final String SEARCH_COLS = "searchcols"; // 表列
    private final String SEARCH = "search"; // 数据
    private final String TABLEOLD = "tableold"; // 表格数据
    private final String DATE = "date"; // 表格日期数据
    private final String TAB = "tab"; // 表格json数据
    private final String CACHETOTAL = "cachetotal"; //总页数
    private final String CACHEROWS = "cacherows"; //一页显示几行

	private boolean isValid(JSONObject jsonObject,String key) throws JSONException{
		boolean flag = false;
		if(jsonObject.has(key) && !jsonObject.isNull(key)){
			flag = !"".equals(jsonObject.getString(key));
		}
		return flag;
	}
	
	public ReplenishSearchResult parseSearchResult(String json) throws Exception{
		if(TextUtils.isEmpty(json)) return null;
		ReplenishSearchResult rsr = null;
		JSONObject jsonObject = new JSONObject(json);
		if(jsonObject.has(SEARCH)){
			rsr = new ReplenishSearchResult();
			JSONObject searchObject = jsonObject.getJSONObject(SEARCH);
			
			if(isValid(searchObject,Constants.SEARCH_RE_DATE)){
				rsr.setSearchredate(searchObject.getString(Constants.SEARCH_RE_DATE));
			}
			
			if(isValid(searchObject,SEARCH_TABLE)){
				rsr.setTargetId(searchObject.getString(SEARCH_TABLE));
			}
			
			if(isValid(searchObject,CACHETOTAL)){
				rsr.setTotal(searchObject.getInt(CACHETOTAL));
			}
			
			if(isValid(searchObject,CACHEROWS)){
				rsr.setCacherows(searchObject.getInt(CACHEROWS));
			}
			
			if(isValid(searchObject,SEARCH_COLS)){
				String cols = searchObject.getString(SEARCH_COLS);
				rsr.setSearchcols(cols);
				String colArr[] = cols.split(",");
				if(isValid(searchObject,SEARCH_DATA)){
	
					JSONArray dataArray = searchObject.getJSONArray(SEARCH_DATA);
					List<Map<String,String>> resultList = new ArrayList<Map<String,String>>();
					Map<String,String> map = null;
					int len = dataArray.length();
					int colsLen = colArr.length;
					JSONArray valueArr = null;
					for(int i=0; i<len; i++){
						valueArr = dataArray.getJSONArray(i);
						map = new HashMap<String,String>();
						
						for(int j=0;j<colsLen;j++){
							
							int index = colArr[j].indexOf(CacheData.DATA_N);
							
							String key = index != -1 ? colArr[j].substring(CacheData.DATA_N.length()) : colArr[j];
							
							map.put(key, valueArr.isNull(j)?"":valueArr.getString(j));
							
							JLog.d(TAG, key+"=>"+map.get(key));
						}
						
						resultList.add(map);
					}
					JLog.d(TAG, "resultList =>"+resultList.size());
					rsr.setResultList(resultList);
				}
			}
		}
		return rsr;
	}
	
	/**
	 *解析店面历史数据-表格
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public ReplenishSearchResult parseStoreTableResult(String json) throws Exception{
		if(TextUtils.isEmpty(json)){
			return null;
		}
		ReplenishSearchResult rsr = null;
		JSONObject jsonObject = new JSONObject(json);
		if(jsonObject.has(TABLEOLD)){
			rsr = new ReplenishSearchResult();
			JSONArray dataArray = jsonObject.getJSONArray(TABLEOLD);
			List<Map<String,String>> resultList = new ArrayList<Map<String,String>>();
			Map<String,String> map = null;
			int len = dataArray.length();
			JSONObject valueObj = null;
			for(int i=0; i<len; i++){
				valueObj = dataArray.getJSONObject(i);
				map = new HashMap<String,String>();
				map.put(TAB, valueObj.getString(TAB));
				map.put(DATE, valueObj.getString(DATE));
				map.put(Constants.PATCH_ID, valueObj.getString(Constants.PATCH_ID));
				resultList.add(map);
			}
			JLog.d(TAG, "resultList =>"+resultList.size());
			rsr.setResultList(resultList);
		}
		return rsr;
	}
}
