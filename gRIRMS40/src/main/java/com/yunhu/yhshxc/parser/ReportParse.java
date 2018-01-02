package com.yunhu.yhshxc.parser;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.yunhu.yhshxc.bo.Report;
import com.yunhu.yhshxc.bo.Report.ReportContent;
import com.yunhu.yhshxc.bo.ReportSelectData;
import com.yunhu.yhshxc.bo.ReportWhere;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil2;

public class ReportParse {
	private final String REPORT_RESULT = "reportresult";
	private final String CTRL_TYPE = "ctrlType";
	private final String NEXT_COLUMN_NUMBER = "nextColumnNumber";
	private final String column_Name = "columnName";
	private final String column_Number = "columnNumber";
	private final String SELECT_LIST = "selectList";
	private final String CACHETOTAL = "cachetotal"; //总页数
    private final String CACHEROWS = "cacherows"; //一页显示几行
    private final String PHONECOLWIDTH = "phoneColWidth";//列宽和对其方式

	public Report parseReportResult(String json) throws Exception{
		Report report = null;
		if(!TextUtils.isEmpty(json)){
			JSONObject  res = new JSONObject(json);
			if(!res.has(REPORT_RESULT)){
				return null;
			}
			report = new Report();
			if(isValid(res,CACHETOTAL)){
				report.setTotal(res.getInt(CACHETOTAL));
			}
			if(isValid(res,CACHEROWS)){
				report.setCacherows(res.getInt(CACHEROWS));
			}
			List<ReportContent> titleList = new ArrayList<ReportContent>();//列表标题
			List<ReportContent[]> dataList = new ArrayList<ReportContent[]>();//列表内容
			
			JSONArray resultJsonArr = res.getJSONArray(REPORT_RESULT);//查询内容JSON包含标题和查询内容
			JSONArray phoneColWidthJsonArr = res.getJSONArray(PHONECOLWIDTH);//列宽和对齐方式
			int len = resultJsonArr.length();
			JSONArray reprtArr = null;
			ReportContent reportContent = null;
			for(int i=0; i<len; i++){
				reprtArr = resultJsonArr.getJSONArray(i);
				ReportContent[] dataArr = new ReportContent[reprtArr.length()];
				for(int j=0;j<reprtArr.length();j++){
					String phoneColWidth = phoneColWidthJsonArr.getString(j);
					reportContent = report.new ReportContent();
					reportContent.width = Integer.parseInt(phoneColWidth.split(",")[0]);//列宽
					reportContent.align = phoneColWidth.split(",")[1];//对齐方式
					reportContent.content = reprtArr.getString(j);
					if(i == 0) { //title
						titleList.add(reportContent);
					}else { //data
						dataArr[j] = reportContent;
					}
				}
				if (i!=0) {
					dataList.add(dataArr);
				}
			}
			report.setDataList(dataList);
			report.setTitleList(titleList);
		}
		return report;
	}
	
	
	
	public List<ReportWhere> parseReportWhere(Context context,Integer targetId) throws Exception{
		String json = SharedPreferencesUtil2.getInstance(context).getReportWhere();
		return parseReportWhere(json,targetId);
	}
	
	public List<ReportWhere> parseReportWhere2(Context context,Integer targetId) throws Exception{
		String json = SharedPreferencesUtil2.getInstance(context).getReportWhere2();
		return parseReportWhere(json,targetId);
	}
	
	public List<ReportSelectData> parseReportWhereForDict(Context context,Integer targetId,String columnNumber,String parentCode) throws Exception{
		String json = SharedPreferencesUtil2.getInstance(context).getReportWhere();
		return parseReportWhereForDict(json,targetId,columnNumber,parentCode);
	}
	
	public List<ReportSelectData> parseReportWhereForDict2(Context context,Integer targetId,String columnNumber,String parentCode) throws Exception{
		String json = SharedPreferencesUtil2.getInstance(context).getReportWhere2();
		return parseReportWhereForDict(json,targetId,columnNumber,parentCode);
	}
	
	private List<ReportWhere> parseReportWhere(String json,Integer targetId) throws Exception{
		List<ReportWhere> list = null;
		if(!TextUtils.isEmpty(json)){
			list = new ArrayList<ReportWhere>();
			JSONArray jsonArr = new JSONArray(json);
			int len = jsonArr.length();
			JSONObject jsonObj = null;
			ReportWhere reportWhere = null;
			
			
			for(int i=0; i<len; i++){
				reportWhere = new ReportWhere();
				jsonObj = jsonArr.getJSONObject(i);
				
				if(targetId!=null && targetId==jsonObj.getInt("taskId")){
					if(isValid(jsonObj,CTRL_TYPE)){
						reportWhere.setCtrlType(jsonObj.getString(CTRL_TYPE));
					}
					if(isValid(jsonObj,NEXT_COLUMN_NUMBER)){
						reportWhere.setNextColumnNumber(jsonObj.getString(NEXT_COLUMN_NUMBER));			
					}
					if(isValid(jsonObj,column_Name)){
						reportWhere.setColumnName(jsonObj.getString(column_Name));
					}
					if(isValid(jsonObj,column_Number)){
						reportWhere.setColumnNumber(jsonObj.getString(column_Number));
					}
					list.add(reportWhere);
				}
				
			}
		}
		return list;
	}
	 
	
	private List<ReportSelectData> parseReportWhereForDict(String json,Integer targetId,String columnNumber,String parentCode) throws Exception{
		List<ReportSelectData> list = null;
		if(!TextUtils.isEmpty(json)){
			JSONArray jsonArr = new JSONArray(json);
			int len = jsonArr.length();
			JSONObject jsonObj = null;
			for(int i=0; i<len; i++){
				jsonObj = jsonArr.getJSONObject(i);
				String columnNumberTemp = "";
				
				if(targetId!=null && targetId==jsonObj.getInt("taskId")){
					if(isValid(jsonObj,column_Number)){
						columnNumberTemp = jsonObj.getString(column_Number);
					}
					if(columnNumberTemp.equals(columnNumber)){
						if(isValid(jsonObj,SELECT_LIST)){
							list = getSelectListData(jsonObj.getJSONArray(SELECT_LIST),parentCode);
							break;
						}
					}
				}
				
			}
		}
		Collections.sort(list,comparator);
		return list;
	}
	
	Comparator<ReportSelectData> comparator =new Comparator<ReportSelectData>() {
		
		private Collator collator = Collator.getInstance(java.util.Locale.CHINA);
		@Override
		public int compare(ReportSelectData lhs, ReportSelectData rhs) {
			if (lhs == null || rhs == null ||  TextUtils.isEmpty(lhs.getName()) || TextUtils.isEmpty(rhs.getName())) {
				return 1;
			}
			return collator.compare(lhs.getName(), rhs.getName());
		}
	};
	
	private List<ReportSelectData> getSelectListData(JSONArray jsonArray,String parentCode) throws Exception{
		List<ReportSelectData> list = new ArrayList<ReportSelectData>();
		int len = jsonArray.length();
		JSONObject jsonObj = null;
		ReportSelectData data = null;
		for(int i=0; i < len; i++){
			jsonObj = jsonArray.getJSONObject(i);
			data = new ReportSelectData();
			if(TextUtils.isEmpty(parentCode)){
				if(isValid(jsonObj,"parentCode")){
					data.setParentCode(jsonObj.getString("parentCode"));
				}
				if(isValid(jsonObj,"name")){
					data.setName(jsonObj.getString("name"));
				}
				if(isValid(jsonObj,"code")){
					data.setCode(jsonObj.getString("code"));
				}
				list.add(data);
				
			}else{
				if(isValid(jsonObj,"parentCode")){
					data.setParentCode(jsonObj.getString("parentCode"));
				}
				if(!TextUtils.isEmpty(data.getParentCode()) && parentCode.equals(data.getParentCode())){
					if(isValid(jsonObj,"name")){
						data.setName(jsonObj.getString("name"));
					}
					if(isValid(jsonObj,"code")){
						data.setCode(jsonObj.getString("code"));
					}
					list.add(data);
				}
			}
		}
		Collections.sort(list,comparator);
		return list;
	}
	
	private boolean isValid(JSONObject jsonObject,String key) throws JSONException{
		boolean flag = false;
		if(jsonObject.has(key) && !jsonObject.isNull(key)){
			flag = !"".equals(jsonObject.getString(key));
		}
		return flag;
	}
}
