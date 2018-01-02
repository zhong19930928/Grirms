package com.yunhu.yhshxc.comp.spinner;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.bo.Module;
import com.yunhu.yhshxc.bo.Org;
import com.yunhu.yhshxc.bo.OrgStore;
import com.yunhu.yhshxc.bo.OrgUser;
import com.yunhu.yhshxc.bo.ReportSelectData;
import com.yunhu.yhshxc.bo.ReportWhere;
import com.yunhu.yhshxc.bo.Role;
import com.yunhu.yhshxc.comp.Component;
import com.yunhu.yhshxc.database.DatabaseHelper;
import com.yunhu.yhshxc.database.DictDB;
import com.yunhu.yhshxc.database.OrgDB;
import com.yunhu.yhshxc.database.OrgStoreDB;
import com.yunhu.yhshxc.database.OrgUserDB;
import com.yunhu.yhshxc.database.RoleDB;
import com.yunhu.yhshxc.parser.ReportParse;
import com.yunhu.yhshxc.report.ReportActivity;
import com.yunhu.yhshxc.utility.MapDistance;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.widget.ToastOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gcg.org.debug.JLog;

import static com.unionpay.uppay.util.k.n;

/**
 * 所有下拉框的父类
 * 获取数据
 * @author jishen
 *
 */
public abstract class AbsSelectComp extends Component{

	protected View view = null;
	private String TAG = "AbsSelectComp";
	protected Context context = null;
	protected Func func = null;//当前控件func
	protected boolean isInTable=false;//控件是否在表格里
	protected boolean isMultichoiceUser=false;//控件是否是用户或角色多选的时候使用
	protected Map<String, String> currentMap;//如果是在表格里，当前控件所在的行
	protected String defaultSql;//sql查询值
	
	protected List<Dictionary> dataSrcList = null; //本下拉类表数据源
	private int dataSrcType = 0; // 数据源从哪张表中取数据 
	
	//级联
	public String queryWhere; //级联时，上级选中的值，用来关联取出本空件的内容数据
	protected String queryWhereForDid; //sql查询的时候查询条件
	protected AbsSelectComp nextComp = null; //仅表格控件所需
	protected Bundle orgBundle;//表格中的店面和用户和外部的机构关联使用
	private Map<Integer,String> orgLevelMap = null;
	private Module module = null;//控件所在的模块
	private int menuType;
	public int compRow;

	public Double currentLon,currentLat;//店面距离搜索使用
	public AbsSelectComp(Context context,Func func,String queryWhere,Bundle orgBundle) {
		this.context = context;
		this.func = func;
		this.queryWhere = queryWhere;
		this.orgBundle = orgBundle;
		dataSrcList=new ArrayList<Dictionary>();
		if(func.getOrgOption() != null && func.getOrgOption()!=Func.OPTION_LOCATION){ // 数据源从哪张表中取数据
			this.dataSrcType = func.getOrgOption();
		}
	}
	

	/**
	 * 获取数据级联源
	 */
	public void getDataSrcList(String fuzzy,String queryWhereForDid){
		if(isMultichoiceUser){
			this.dataSrcType =Func.MULTICHOICE_USER;
		}
		if(dataSrcList!=null && !dataSrcList.isEmpty()){
			dataSrcList.clear();//调用前先清空List里数据，否则模糊搜索的时候数值转变的时候数据会有多余
		}
		switch(dataSrcType){
			case Func.ORG_OPTION://机构
				setOrgSelect(fuzzy,this.queryWhereForDid);
				break;
			case Func.ORG_USER://用户
				setOrgUserSelect(fuzzy,this.queryWhereForDid);
				break;
			case Func.ORG_STORE://店面
				if (func.getIsAreaSearch() == 1 && currentLat!=null && currentLon!=null && func.getAreaSearchValue()!=null) {
					setOrgStoreSelect(fuzzy,this.queryWhereForDid,func.getAreaSearchValue());
				}else{
					setOrgStoreSelect(fuzzy,this.queryWhereForDid);
				}
				break;
			case Func.MULTICHOICE_USER://多选下拉框
				mUltiChoiceUser(fuzzy,this.queryWhereForDid);
				break;
			default:
				if(!TextUtils.isEmpty(func.getDictTable()) && DatabaseHelper.getInstance(context).tabbleIsExist("DICT_"+func.getDictTable())){
					dataSrcList = new DictDB(context).findDictList(func.getDictTable(),func.getDictCols(),func.getDictDataId(),func.getDictOrderBy(),func.getDictIsAsc(),queryWhere,fuzzy,queryWhereForDid);
				}else{
					ToastOrder.makeText(context, func.getName()+context.getResources().getString(R.string.configure_false), ToastOrder.LENGTH_SHORT).show();
				}
		}
		tableSelectControll();
	}
	
	/**
	 * 对表格中已经选择的项标识（已选）
	 */
	 private void tableSelectControll(){
		 if (isInTable) {
			 for (int i = 0; i <dataSrcList.size(); i++) {
					Dictionary dic = dataSrcList.get(i);
					if (dic.getSelectCount()>0) {
						dic.setCtrlCol(context.getResources().getString(R.string.has_selected)+dic.getCtrlCol());
					}
			}
		}
	 }
	
	/**
	 * 
	 * @return绑定adapter数据源
	 */
	public String[] getDateSrc(){
		if (dataSrcList == null) return null;
		String[] contcol = new String[dataSrcList.size()];
		int i = 0;
		for (Dictionary dict : dataSrcList) {
			contcol[i] = dict.getCtrlCol()==null?"":dict.getCtrlCol();//获取资源列所有数据
			i++;
		}
		return contcol;
	}
	
	/**
	 * 刷新下拉列表中的内容
	 */
	public abstract void notifyDataSetChanged();
	
	/**
	 * 
	 * @param 二次取数据时，让下拉框显示选中的那条数据
	 */
	public abstract void setSelected(String did);

	@Override
	public View getObject() {
		return view;
	}

	@Override
	public void setIsEnable(boolean isEnable) {
		view.setEnabled(isEnable);
	}
	
	/**
	 * 级联下一级
	 */
	public void oneToMany(){
		nextComp.notifyDataSetChanged();
		if(nextComp.func.getType()==Func.TYPE_SELECTCOMP){
			if(TextUtils.isEmpty(value)){
				nextComp.value="";
			}
			nextComp.setSelected(nextComp.value);
		}else{
			nextComp.setSelected("");//多选和模糊搜索的时候下级显示请选择
		}
	}
	
	/**
	 * 
	 * @return 当前控件
	 */
	public Func getFunc() {
		return func;
	}
	
	/**
	 * 设置当前控件
	 * @param func
	 */
	public void setFunc(Func func) {
		this.func = func;
	}
	
	/**
	 * 设置下级控件
	 * @param nextComp
	 */
	public void setNextComp(AbsSelectComp nextComp) {
		this.nextComp = nextComp;
	}
	
	/**
	 * 设置机构的值
	 * @param fuzzyContent 机构名字
	 * @param queryWhereForDid 上级机构did
	 */
	private void setOrgSelect(String fuzzyContent,String queryWhereForDid){
		List<Org> orgList=null;
		Integer orgLevel=func.getOrgLevel();  //当前func的层级
		if(isTop(orgLevel)){ //如果当前层级是顶层
			orgList=new OrgDB(context).findOrgListByOrgLevel(String.valueOf(orgLevel),fuzzyContent,queryWhereForDid);
		}else{
			String relation = getCurrentParentId(orgLevel);//获取机构关系
			if(relation == null){
				orgList=new ArrayList<Org>();
			}else{
				orgList = new OrgDB(context).findOrgListByOrgParentId(relation+"",fuzzyContent,queryWhereForDid);
			}
		}
		
		for (int i = 0; i < orgList.size(); i++) {//将数据转为字典实例的形式
			Org org=orgList.get(i);
			Dictionary dic=new Dictionary();
			dic.setCtrlCol(org.getOrgName());
			dic.setDid(org.getOrgId()+"");
			dataSrcList.add(dic);
		}
	}
	

	/**
	 * 设置店面
	 * @param fuzzyContent 店面名称
	 * @param queryWhereForDid 上级did
	 */
	private void setOrgStoreSelect(String fuzzyContent,String queryWhereForDid){
		String relation = getCurrentOrgByStore();
		List<OrgStore> orgStoreList = new OrgStoreDB(context).findOrgList(relation,fuzzyContent,queryWhereForDid);
		for (int i = 0; i < orgStoreList.size(); i++) {
			OrgStore orgStore=orgStoreList.get(i);
			Dictionary dic=new Dictionary();
			dic.setCtrlCol(orgStore.getStoreName());
			dic.setDid(orgStore.getStoreId()+"");
			dataSrcList.add(dic);
		}
	}
	
	/**
	 * 设置店面需要添加店面距离验证
	 * @param fuzzyContent 店面名称
	 * @param queryWhereForDid 上级did
	 */
	private void setOrgStoreSelect(String fuzzyContent,String queryWhereForDid,float storeDistance){
		String relation = getCurrentOrgByStore();
		List<OrgStore> orgStoreList = new OrgStoreDB(context).findOrgList(relation,fuzzyContent,queryWhereForDid);
		for (int i = 0; i < orgStoreList.size(); i++) {
			OrgStore orgStore=orgStoreList.get(i);
			if (orgStore.getStoreLat() != null && orgStore.getStoreLon() != null) {
				Double distance = MapDistance.getDistance(orgStore.getStoreLat(), orgStore.getStoreLon(), currentLat, currentLon);
				JLog.d("jishen", "distance:"+distance+" SysDistance:"+storeDistance);
				if (distance>=0 && distance <=storeDistance) {
					Dictionary dic=new Dictionary();
					dic.setCtrlCol(orgStore.getStoreName());
					dic.setDid(orgStore.getStoreId()+"");
					dataSrcList.add(dic);
				}
			}
		}
	}
	
	/**
	 * 设置用户数据
	 * @param fuzzyContent 用户名称
	 * @param queryWhereForDid 上级did
	 */
	private void setOrgUserSelect(String fuzzyContent,String queryWhereForDid) {
//		String relation = etCurrentOrgByStore();
		String relation = null;
		List<OrgUser> orgUserList = new OrgUserDB(context).findOrgUserListByAuthSearch(relation,fuzzyContent,queryWhereForDid);
		for (int i = 0; i < orgUserList.size(); i++) {
			OrgUser orgUser=orgUserList.get(i);
			Dictionary dic=new Dictionary();
			dic.setCtrlCol(orgUser.getUserName());
			dic.setDid(orgUser.getUserId()+"");
			dataSrcList.add(dic);
		}
	}
	
	/**
 	 * @return	上级选择关系
	 */
	private String getCurrentOrgByStore(){
		if(orgBundle == null || orgBundle.size() == 0) return null;
		Integer bestLevel = Integer.parseInt(orgBundle.getString(-10000+""));
		Integer selecedLevel = 0;
		//根据最大层，获取已选中的最大层，并将所有选择的层的orgid拼成查询条件
		for(int i = bestLevel; i > 0; i--){
			if(orgBundle.getString(i+"") != null){
				selecedLevel = i;
				break;
			}
		}
		StringBuffer orgStr=new StringBuffer();
		if(orgBundle.getString(selecedLevel+"") != null){
			String temp = ","+orgBundle.getString(selecedLevel+"");
			orgStr.append(temp);
			OrgDB db = new OrgDB(context);
			int max = db.findMaxLevel();
			for(int i = selecedLevel; i < max; i++){
				List<Org> list = db.findOrgListByParentIds(temp.substring(1),null);
				for (Org org : list) {
					temp = temp + ","+org.getOrgId();
				}
				orgStr.append(temp);
			}
		}
		if(orgStr.length() > 0){
			return orgStr.substring(1);
		}else{
			return null;
		}
	}
	
	/**
	 * 
	 * @param orgLevel 传入当前层级
	 * @return true表示当前层级是最顶层 false表示当前层级不是最顶层
	 */
	private boolean isTop(int orgLevel){

		boolean isTop=true;
		if(orgBundle.containsKey((orgLevel-1)+"")){
			isTop=false;
			return isTop;
		}
		return  isTop;
	}
	
	/**
	 * 得到当前的parentID
	 * @param currentLevel 传入当前等级
 	 * @return	上级选择关系
	 */
	private String getCurrentParentId(int currentLevel){
		String orgid= orgBundle.getString((currentLevel-1)+"");//下级的parentID等于上级的orgID
		return orgid;
	}
	
	public ReportActivity getReportActivity(){
		ReportActivity reportActivity = null;
		if(context instanceof ReportActivity){
			reportActivity = (ReportActivity)context;
		}
		return reportActivity;
	}
	
	/**
	 * 多选下拉框
	 * @param fuzzy 模糊搜索条件
	 * @param queryWhereForDid 上级did
	 */
	private void mUltiChoiceUser(String fuzzy,String queryWhereForDid){

		if(func.getFuncId()==Func.ISSUE_USER){ //下发用户控件
			Module module = getModule();
			List<OrgUser> list = new OrgUserDB(context).findUserList(func.getTargetid(),module.getAuth(),module.getAuthOrgId(),fuzzy,queryWhereForDid);
			setDataSrcListForOrgUser(list);
		}else if(func.getTargetid()==-2){//考勤查询的时候func.getTargetid()==-2
			if(func.getFuncId()==-4){//角色
				List<Role> roleList = new RoleDB(context).findAllRole(fuzzy,queryWhereForDid);
				setDataSrcListForRole(roleList);//转化数据
			}else if(func.getFuncId()==-5){//用户
				String attendAuth = null;
				JLog.d(TAG, "menuType:"+menuType);
				if (menuType == Menu.TYPE_ATTENDANCE) {
					attendAuth = SharedPreferencesUtil.getInstance(context.getApplicationContext()).getAttendAuth(); //考勤加入权限
				}else{
					attendAuth = SharedPreferencesUtil.getInstance(context.getApplicationContext()).getNewAttendAuth(); //新考勤加入权限
				}
				Integer auth = null;
				String authOrgId = null;
				if(!TextUtils.isEmpty(attendAuth)){
					String arr[] = attendAuth.split("\\|");
					auth = Integer.valueOf(arr[0]);
					if(arr.length == 2){
						authOrgId = arr[1];
					}
				}
				List<OrgUser> list = new OrgUserDB(context).findOrgUserListByAuth(auth, authOrgId, null, null);
				setDataSrcListForOrgUser(list);//转化数据
			}
		}else{//报表中的多选下拉框
			ReportActivity reportActivity = getReportActivity();
			
			if(func.getFuncId()==-2907 && reportActivity != null && reportActivity.reportValue!=null){//表示是报表中的多选
				try {
					ReportWhere reportWhere = reportActivity.getCurrentReportWhere();
					if(reportWhere == null){
						return;
					}
					List<ReportSelectData> reportSelectDatalist=null;
					if(reportActivity.menuType==Menu.TYPE_REPORT){//旧报表
						reportSelectDatalist = new ReportParse().parseReportWhereForDict(context,func.getTargetid(), reportWhere.getColumnNumber(), reportActivity.orgMap.get(reportWhere.getColumnName()));
					} else {//新报表
						reportSelectDatalist = new ReportParse().parseReportWhereForDict2(context,func.getTargetid(), reportWhere.getColumnNumber(),reportActivity.orgMap.get(reportWhere.getColumnName()));
					}
					setDataSrcListForReportSelectData(reportSelectDatalist);//转化数据
					
				} catch (Exception e) {
					JLog.d(TAG, "reportSelectDatalist异常");
				}
				
			}
		}
		
	}
	
	
	/**
	 * 将角色实例转化为字典的数据形式
	 * @param roleList 角色实例集合
	 */
	private void setDataSrcListForRole(List<Role> roleList){
		if(roleList!=null && !roleList.isEmpty()){
			for (int i = 0; i < roleList.size(); i++) {
				Role role=roleList.get(i);
				Dictionary dic=new Dictionary();
				dic.setCtrlCol(role.getName());
				dic.setDid(role.getRoleId()+"");
				dataSrcList.add(dic);
			}
		}
	}
	
	/**
	 * 将用户实例转化为字典的形式
	 * @param orgUserList 用户实例集合
	 */
	private void setDataSrcListForOrgUser(List<OrgUser> orgUserList){
		if(orgUserList!=null && !orgUserList.isEmpty()){
			for (int i = 0; i < orgUserList.size(); i++) {
				OrgUser orgUser=orgUserList.get(i);
				Dictionary dic=new Dictionary();
				dic.setCtrlCol(orgUser.getUserName());
				dic.setDid(orgUser.getUserId()+"");
				dataSrcList.add(dic);
			}
		}
	}
	
	/**
	 * 将数据转化为字典的形式
	 * @param reportSelectDatalist 报表控件的实例
	 */
	private void setDataSrcListForReportSelectData(List<ReportSelectData> reportSelectDatalist){
		if(reportSelectDatalist!=null && !reportSelectDatalist.isEmpty()){
			for (int i = 0; i < reportSelectDatalist.size(); i++) {
				ReportSelectData reportSelectData=reportSelectDatalist.get(i);
				Dictionary dic=new Dictionary();
				dic.setCtrlCol(reportSelectData.getName());
				dic.setDid(reportSelectData.getCode());
				dataSrcList.add(dic);
			}
		}
	}
	
	/**
	 * 设置该控件是否在表格中
	 * @param isInTable true表示是表格中的控件 false表示不是表格中的控件
	 */
	public void setInTable(boolean isInTable) {
		this.isInTable = isInTable;
	}
	
	/**
	 * 设置是否是多选下拉框用户
	 * @param isMultichoiceUser true是 false不是
	 */
	public void setMultichoiceUser(boolean isMultichoiceUser) {
		this.isMultichoiceUser = isMultichoiceUser;
	}

	/**
	 * 
	 * @return sql默认值
	 */
	public String getDefaultSql() {
		return defaultSql;
	}
	/**
	 * 设置sql默认值 
	 * @param defaultSql 值
	 */
	public void setDefaultSql(String defaultSql) {
		this.defaultSql = defaultSql;
	}


	/**
	 * 设置机构层级map
	 * @param orgLevelMap
	 */
	public void setOrgLevelMap(Map<Integer, String> orgLevelMap) {
		this.orgLevelMap = orgLevelMap;
	}


	/**
	 * 获取模块实例
	 * @return
	 */
	public Module getModule() {
		return module;
	}

	/**
	 * 设置模块实例
	 * @param module
	 */
	public void setModule(Module module) {
		this.module = module;
	}
	
	/**
	 * 表格中的一行数据的map
	 * @param currentMap
	 */
	public void setCurrentMap(HashMap<String, String> currentMap) {
		this.currentMap = currentMap;
	}


	public void setMenuType(int menuType) {
		this.menuType = menuType;
	}
	
}
