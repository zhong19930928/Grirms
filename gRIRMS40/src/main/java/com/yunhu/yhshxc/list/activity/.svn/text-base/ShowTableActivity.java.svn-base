package com.yunhu.yhshxc.list.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.OrgStore;
import com.yunhu.yhshxc.bo.OrgUser;
import com.yunhu.yhshxc.comp.PreviewTable;
import com.yunhu.yhshxc.database.DictDB;
import com.yunhu.yhshxc.database.FuncDB;
import com.yunhu.yhshxc.database.OrgStoreDB;
import com.yunhu.yhshxc.database.OrgUserDB;
import com.yunhu.yhshxc.parser.TableParse;

public class ShowTableActivity extends AbsBaseActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_table);
		initBase();
		Integer menuType = getIntent().getIntExtra("menuType", 0);
		Integer tableId = getIntent().getIntExtra("tableId", 0);//func的id,不是funcid
		String funcName = getIntent().getStringExtra("funcName");
		String tableJson = getIntent().getStringExtra("tableJson");
		LinearLayout ll_table_view = (LinearLayout) findViewById(R.id.ll_show_table);
		TextView tv_name = (TextView) findViewById(R.id.tv_show_table_name);
		tv_name.setText(funcName);
		getTableView(ll_table_view, funcName, tableId,menuType,tableJson);
	}

	/**
	 * 获取表格view
	 * @param ll_child
	 * @param func
	 * @param jsonValue
	 */
	protected void getTableView(LinearLayout ll_table_view, String funcName,Integer tableId, Integer menuType,String jsonValue){
		 // 表格类型
		// 存储表格中的Func
		List<Func> funcTableList = new FuncDB(ShowTableActivity.this).findFuncListContainHiddenByTargetid(tableId); // 查找table中的所有组件
		List<HashMap<String, String>> tableList = getTableList(jsonValue); // 获取表格的列数
		List<String> contentList = getTableContent(tableList,funcTableList);// 获取表格中的内容
		View tableView = new PreviewTable(ShowTableActivity.this, tableList.size(),
		contentList, funcTableList).getObject(); // 获取表格视图View
		ll_table_view.addView(tableView);
	}

	/**
	 * 获取表格内容
	 */
	private List<String> getTableContent(List<HashMap<String, String>> tablelist,
			List<Func> funcTableList) {
		List<String> sList = new ArrayList<String>();
		for (int i = 0; i < tablelist.size(); i++) { // 循环行数
			HashMap<String, String> table = tablelist.get(i);
			for (int j = 0; j < funcTableList.size(); j++) {// 循环列数
				Func func = funcTableList.get(j); // 得到该列的Func
				String did = table.get(func.getFuncId()+"");
				if(TextUtils.isEmpty(did)){
					sList.add("");
				}else if (func.getType() == Func.TYPE_PRODUCT_CODE || 
						  func.getType() == Func.TYPE_SCAN || 
						  func.getType() == Func.TYPE_SCAN_INPUT || 
						  func.getType() == Func.TYPE_CAMERA || 
						  func.getType() == Func.TYPE_CAMERA_HEIGHT || 
						  func.getType() == Func.TYPE_CAMERA_MIDDLE || 
						  func.getType() == Func.TYPE_CAMERA_CUSTOM ||
						  func.getType() == Func.TYPE_EDITCOMP || 
						  func.getType() == Func.TYPE_DATEPICKERCOMP ||
						  func.getType() == Func.TYPE_TIMEPICKERCOMP ||
						  ( func.getType() == Func.TYPE_HIDDEN && func.getDefaultType() !=null && func.getDefaultType() != Func.DEFAULT_TYPE_SELECT)) {
					sList.add(did);
				}else if(func.getType() == Func.TYPE_SELECT_OTHER || func.getType() == Func.TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY_COMP){
					if(table.get(func.getFuncId()+"").equals("-1")){
						sList.add(table.get(func.getFuncId()+"_other")); // 将内容添加到List
					}else{
						if(TextUtils.isEmpty(did)){
							sList.add(""); // 将内容添加到List
						}else{
							String tableName = func.getDictTable(); // 查询的表
							String dataId = func.getDictDataId(); // 查询表中的列
							Dictionary dic = new DictDB(ShowTableActivity.this).findDictListByTable(
									tableName, dataId, did);
							sList.add(dic.getCtrlCol()); // 将内容添加到List
						}
					}
				}else {
					if (func.getOrgOption() != null && func.getOrgOption()!=Func.OPTION_LOCATION) {// 表格里如果有店或者user
						if (func.getOrgOption() == Func.ORG_STORE) {// 店
							List<OrgStore> orgStoreList = new OrgStoreDB(this).findOrgListByStoreId(did);
							StringBuffer sb=new StringBuffer();
							for (int k = 0; k < orgStoreList.size(); k++) {
								sb.append(",").append(orgStoreList.get(k).getStoreName());
							}
							sList.add(sb.length()>0 ? sb.substring(1).toString():did);
							
						} else if (func.getOrgOption() == Func.ORG_USER) {// user
							List<OrgUser> orgUserList = new OrgUserDB(this).findOrgUserByUserId(did);
							StringBuffer sb=new StringBuffer();
							for (int k = 0; k < orgUserList.size(); k++) {
								sb.append(",").append(orgUserList.get(k).getUserName());
							}
							sList.add(sb.length()>0 ? sb.substring(1).toString():did);
						}
					} else {
						String tableName = func.getDictTable(); // 查询的表
						String dataId = func.getDictDataId(); // 查询表中的列
						if (!TextUtils.isEmpty(did)) {
							String rc=new DictDB(ShowTableActivity.this).findDictMultiChoiceValueStr(did,dataId,tableName);
							sList.add(rc);
						} else {
							sList.add("");
						}
					}
				}
			}
		}
		return sList;
	}
	/**
	* 获取Table列集合
	*/
	private List<HashMap<String, String>> getTableList(String json) {
		List<HashMap<String, String>> tableList = new TableParse().parseJason(json);
		return tableList;
	}

}
