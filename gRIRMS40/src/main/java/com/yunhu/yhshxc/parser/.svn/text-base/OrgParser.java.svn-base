package com.yunhu.yhshxc.parser;

import gcg.org.debug.JLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.Org;
import com.yunhu.yhshxc.bo.OrgStore;
import com.yunhu.yhshxc.bo.OrgUser;
import com.yunhu.yhshxc.database.DatabaseHelper;
import com.yunhu.yhshxc.database.OrgDB;
import com.yunhu.yhshxc.database.OrgStoreDB;
import com.yunhu.yhshxc.database.OrgUserDB;

/**
 * 解析组织机构
 * @author jishen
 *
 */
public class OrgParser {

	private final String OPERATION = "operation";
	private final String STORE = "store";
	private final String USER = "user";
	
    private final String STORE_ID = "storeid"; // 店面ID
    private final String ORG_ID = "orgid"; // 机构ID
    private final String ORG_NAME = "orgname"; // 机构名称
    private final String PARENT_ID = "parentid"; // 上级机构id
    private final String ORG_LEVEL = "orglevel"; // 机构层级
    private final String STORE_NAME = "storename"; // 店面名称
    private final String USER_NAME = "username"; // 店面名称
    private final String USER_ID = "userid"; // 店面名称
    private final String PID = "pid";
    private final String CODE = "code";
    private final String ROLE_ID = "roleId";//角色ID
    private final String ROLE_NAME = "rolename";//角色名称
    
    
    private final String AUTH_SEARCH = "authsearch"; 
    private final String AUTH_ORG_ID = "authorgid";
    
    private final String ORG_CODE="org_code";
    private final String LEVEL="level_id";
    
    private final String STORE_LON = "storelon";
    private final String STORE_LAT = "storelat";
    
    private Context context = null;
	private OrgDB orgDB;
	private OrgStoreDB orgStoreDB;
	private OrgUserDB orgUserDB;
	
	public OrgParser(Context context) {
		this.context = context;
		orgDB = new OrgDB(context);
		orgStoreDB = new OrgStoreDB(context);
		orgUserDB = new OrgUserDB(context);
	}
	
	/**
	 * 解析所有
	 * @param json
	 * @throws Exception
	 */
	public void syncParseAll(String json) throws Exception{
		long l = System.currentTimeMillis();
		JSONObject jsonObject = new JSONObject(json);
		if(jsonObject.has(OPERATION)){
			String jsonForOrg = jsonObject.getString(OPERATION);
			parseOrg(jsonForOrg);
		}
		if(jsonObject.has(STORE)){
			String jsonForStore = jsonObject.getString(STORE);
			parseStore(jsonForStore);
		}
		if(jsonObject.has(USER)){
			String jsonForUser = jsonObject.getString(USER);
			parseUser(jsonForUser);
		}
		JLog.d("syncParseAll", "initParseAll =>所用时间："+(System.currentTimeMillis()-l));
	}
	

	/**
	 * 解析所有
	 * @param json
	 * @throws Exception
	 */
	public void batchParseAll(String json) throws Exception{
		long l = System.currentTimeMillis();
		JSONObject jsonObject = new JSONObject(json);
		if(jsonObject.has(OPERATION)){
			String jsonForOrg = jsonObject.getString(OPERATION);
			parseBatchOrg(jsonForOrg);
		}
		if(jsonObject.has(STORE)){
			String jsonForStore = jsonObject.getString(STORE);
			parseBatchStore(jsonForStore);
		}
		if(jsonObject.has(USER)){
			String jsonForUser = jsonObject.getString(USER);
			parseBatchUser(jsonForUser);
		}
		JLog.d("initParseAll", "initParseAll =>所用时间："+(System.currentTimeMillis()-l));
	}
	
	public void batchParseByType(String json,int type) throws Exception{
		long l = System.currentTimeMillis();
		JSONObject jsonObject = new JSONObject(json);
		if(type == Func.ORG_OPTION){
			if(jsonObject.has(OPERATION)){
				String jsonForOrg = jsonObject.getString(OPERATION);
				parseBatchOrg(jsonForOrg);
			}
			insertOrgForEmpty();
		}else if(type == Func.ORG_STORE){
			if(jsonObject.has(STORE)){
				String jsonForStore = jsonObject.getString(STORE);
				parseBatchStore(jsonForStore);
			}
			insertStoreForEmpty();
		}else if(type == Func.ORG_USER){
			if(jsonObject.has(USER)){
				String jsonForUser = jsonObject.getString(USER);
				parseBatchUser(jsonForUser);
			}
			insertUserForEmpty();
		}else{
			
		}
		JLog.d("initParseAll", "initParseAll =>所用时间："+(System.currentTimeMillis()-l));
	}
	
	/**
	 * 如果机构为空，测添加一条-999的数据
	 * （因为提示用户更新时返回空，导致提示框一直提示）
	 * 店面扩展属性所至
	 */
	private void insertOrgForEmpty(){
		if(orgDB.isEmpty()){
			Org org = new Org();
			org.setId(-999);
			org.setOrgId(-999);
			org.setOrgName(" ");
			orgDB.insertOrg(org);
		}
	}
	
	/**
	 * 如果用户为空，测添加一条-999的数据
	 * （因为提示用户更新时返回空，导致提示框一直提示）
	 * 店面扩展属性所至
	 */
	private void insertStoreForEmpty(){
		if(orgStoreDB.isEmpty()){
			OrgStore orgStore = new OrgStore();
			orgStore.setId(-999);
			orgStore.setStoreId(-999);
			orgStore.setStoreName(" ");
			orgStoreDB.insertOrgStore(orgStore);
		}
	}
	
	/**
	 * 如果用户为空，测添加一条-999的数据
	 * （因为提示用户更新时返回空，导致提示框一直提示）
	 * 店面扩展属性所至
	 */
	private void insertUserForEmpty(){
		if(orgUserDB.isEmpty()){
			OrgUser orgUser = new OrgUser();
			orgUser.setId(-999);
			orgUser.setUserId(-999);
			orgUser.setUserName(" ");
			orgUserDB.insertOrgUser(orgUser);
		}
	}
	
	/**
	 * 解析数据
	 * @param json
	 * @throws JSONException
	 */
	public void parseAll(String json) throws JSONException{
		if(!TextUtils.isEmpty(json)){
			JSONObject jsonObject = new JSONObject(json);
			if(jsonObject.has(OPERATION)){
				String jsonForOrg = jsonObject.getString(OPERATION);
				parseOrg(jsonForOrg);
			}
			if(jsonObject.has(STORE)){
				String jsonForStore = jsonObject.getString(STORE);
				parseStore(jsonForStore);
			}
			if(jsonObject.has(USER)){
				String jsonForUser = jsonObject.getString(USER);
				parseUser(jsonForUser);
			}
		}
		
	}
	
	private void parseOrg(String json) throws JSONException{
		long l = System.currentTimeMillis();
		JSONArray jsonArr = new JSONArray(json);
		int size = jsonArr.length();
		Org org = null;
		orgDB.removeByOrgId("-999");
		for(int i = 0; i < size; i++){
			org = putOrg(jsonArr.getJSONObject(i));
			orgDB.removeByOrgId(org.getOrgId()+"");
			orgDB.insertOrg(org);
		}
		JLog.d("OrgParser", "parseOrg =>所用时间："+(System.currentTimeMillis()-l));
	}
	
	/**
	 * 解析机构
	 * @param json
	 * @throws JSONException
	 */
	private void parseBatchOrg(String json) throws JSONException{
		long l = System.currentTimeMillis();
		JSONArray jsonArr = new JSONArray(json);
		int size = jsonArr.length();
		Org org = null;
		DatabaseHelper.getInstance(context).beginTransaction();
		orgDB.removeByOrgId("-999");
		for(int i = 0; i < size; i++){
			org = putOrg(jsonArr.getJSONObject(i));
			orgDB.insertOrg(org);
		}
		DatabaseHelper.getInstance(context).endTransaction();
		JLog.d("OrgParser", "parseBatchOrg =>所用时间："+(System.currentTimeMillis()-l));
	}
	
	/**
	 * 解析机构
	 * @param jsonObject
	 * @return
	 * @throws JSONException
	 */
	public Org putOrg(JSONObject jsonObject) throws JSONException{
		Org org = new Org();
		if(isValid(jsonObject,ORG_ID)){
			org.setOrgId(jsonObject.getInt(ORG_ID));
		}
		if(isValid(jsonObject,ORG_NAME)){
			org.setOrgName(jsonObject.getString(ORG_NAME));
		}
		if(isValid(jsonObject,ORG_LEVEL)){
			org.setOrgLevel(jsonObject.getString(ORG_LEVEL));
		}
		if(isValid(jsonObject,PARENT_ID)){
			org.setParentId(jsonObject.getString(PARENT_ID));
		}
		if(isValid(jsonObject,CODE)){
			org.setCode(jsonObject.getString(CODE));
		}
		return org;
	}
	
	/**
	 * 解析店面
	 * @param json
	 * @throws Exception
	 */
	private void parseBatchStore(String json) throws Exception{
		long l = System.currentTimeMillis();
		JSONArray jsonArr = new JSONArray(json);
		int size = jsonArr.length();
		OrgStore store = null;
		DatabaseHelper.getInstance(context).beginTransaction();
		orgStoreDB.removeByStoreId("-999");
		for(int i = 0; i < size; i++){
			store = putStore(jsonArr.getJSONObject(i));
			orgStoreDB.insertOrgStore(store);
		}
		DatabaseHelper.getInstance(context).endTransaction();
		JLog.d("OrgParser", "parseBatchStore =>所用时间："+(System.currentTimeMillis()-l));
	}
	
	/**
	 * 解析店面
	 * @param json
	 * @throws JSONException
	 */
	private void parseStore(String json) throws JSONException{
		long l = System.currentTimeMillis();
		JSONArray jsonArr = new JSONArray(json);
		int size = jsonArr.length();
		OrgStore orgStore = null;
		orgStoreDB.removeByStoreId("-999");
		for(int i = 0; i < size; i++){
			orgStore = putStore(jsonArr.getJSONObject(i));
			orgStoreDB.removeByStoreId(orgStore.getStoreId()+"");
			orgStoreDB.insertOrgStore(orgStore);
		}
		JLog.d("OrgParser", "parseStore =>所用时间："+(System.currentTimeMillis()-l));
	}
	
	/**
	 * 解析获取店面对象
	 * @param jsonObject
	 * @return
	 * @throws JSONException
	 */
	public OrgStore putStore(JSONObject jsonObject) throws JSONException{
		OrgStore orgStore = new OrgStore();
		if(isValid(jsonObject,STORE_ID)){
			orgStore.setStoreId(jsonObject.getInt(STORE_ID));
		}
		if(isValid(jsonObject,ORG_ID)){
			orgStore.setOrgId(jsonObject.getInt(ORG_ID));
		}
		if(isValid(jsonObject,STORE_NAME)){
			orgStore.setStoreName(jsonObject.getString(STORE_NAME));
		}
		if (isValid(jsonObject, STORE_LON)) {
			orgStore.setStoreLon(jsonObject.getDouble(STORE_LON));
		}
		if (isValid(jsonObject, STORE_LAT)) {
			orgStore.setStoreLat(jsonObject.getDouble(STORE_LAT));
		}
		if (isValid(jsonObject, CODE)) {
			orgStore.setOrgCode(jsonObject.getString(CODE));
		}
		if (isValid(jsonObject, LEVEL)) {
			orgStore.setLevel(jsonObject.getInt(LEVEL));
		}
		return orgStore;
	}
	
	/**
	 * 解析用户
	 * @param json
	 * @throws Exception
	 */
	private void parseBatchUser(String json) throws Exception{
		long l = System.currentTimeMillis();
		JSONArray jsonArr = new JSONArray(json);
		int size = jsonArr.length();
		OrgUser orgUser = null;
		DatabaseHelper.getInstance(context).beginTransaction();
		orgUserDB.removeByUserId("-999");
		for(int i = 0; i < size; i++){
			orgUser = putUser(jsonArr.getJSONObject(i));
			orgUserDB.insertOrgUser(orgUser);
		}
		DatabaseHelper.getInstance(context).endTransaction();
		JLog.d("OrgParser", "parseBatchUser =>所用时间："+(System.currentTimeMillis()-l));
	}
	
	/**
	 * 解析用户
	 * @param json
	 * @throws JSONException
	 */
	private void parseUser(String json) throws JSONException{
		long l = System.currentTimeMillis();
		JSONArray jsonArr = new JSONArray(json);
		int size = jsonArr.length();
		OrgUser orgUser = null;
		orgUserDB.removeByUserId("-999");
		for(int i = 0; i < size; i++){
			orgUser = putUser(jsonArr.getJSONObject(i));
			orgUserDB.removeByUserId(orgUser.getUserId()+"");
			orgUserDB.insertOrgUser(orgUser);
		}
		JLog.d("OrgParser", "parseBatchUser =>所用时间："+(System.currentTimeMillis()-l));
	}
	
	/**
	 * 解析获取用户对象
	 * @param jsonObject
	 * @return
	 * @throws JSONException
	 */
	public OrgUser putUser(JSONObject jsonObject) throws JSONException{
		OrgUser orgUser = new OrgUser();
		if(isValid(jsonObject,USER_ID)){
			orgUser.setUserId(jsonObject.getInt(USER_ID));
		}
		if(isValid(jsonObject,ORG_ID)){
			orgUser.setOrgId(jsonObject.getInt(ORG_ID));
		}
		if(isValid(jsonObject,USER_NAME)){
			orgUser.setUserName(jsonObject.getString(USER_NAME));
		}
		if(isValid(jsonObject,PID)){
			orgUser.setPid(jsonObject.getInt(PID));
		}
		if(isValid(jsonObject,OPERATION)){
			orgUser.setPurview(jsonObject.getString(OPERATION));
		}
		if(isValid(jsonObject,AUTH_SEARCH)){
			orgUser.setAuthSearch(jsonObject.getInt(AUTH_SEARCH));
		}
		if(isValid(jsonObject,AUTH_ORG_ID)){
			orgUser.setAuthOrgId(jsonObject.getString(AUTH_ORG_ID));
		}
		if(isValid(jsonObject,ORG_CODE)){
			orgUser.setOrgCode(jsonObject.getString(ORG_CODE));
		}
		if (isValid(jsonObject, ROLE_ID)) {
			orgUser.setRoleId(jsonObject.getInt(ROLE_ID));
		}
		if (isValid(jsonObject, ROLE_NAME)) {
			orgUser.setRoleName(jsonObject.getString(ROLE_NAME));
		}
		return orgUser;
	}
	
	private boolean isValid(JSONObject jsonObject,String key) throws JSONException{
		boolean flag = false;
		if(jsonObject.has(key) && !jsonObject.isNull(key)){
			flag = !"".equals(jsonObject.getString(key));
		}
		return flag;
	}
}
