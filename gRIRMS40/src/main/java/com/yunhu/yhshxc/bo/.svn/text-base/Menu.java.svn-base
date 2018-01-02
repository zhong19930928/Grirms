package com.yunhu.yhshxc.bo;

import java.io.Serializable;
import java.util.ArrayList;

public class Menu implements Serializable{
  
	public static final int TYPE_NOTICE = 1; // 公告
	public static final int TYPE_VISIT = 2; // 访店
	public static final int TYPE_MODULE = 3; // 自定义模块//   new 初始库存登记／ 要货计划／
	public static final int TYPE_TARGET = 4; // 任务
	public static final int TYPE_NEW_TARGET = 5; // 新双向任务
	public static final int TYPE_ATTENDANCE = 6; // 考勤
	public static final int TYPE_NEARBY = 7; // 就近拜访
	public static final int TYPE_BBS = 8; // bbs
	public static final int TYPE_PASSIVE_LOCATION = 9; // 被动定位
	public static final int TYPE_REPORT = 10; // 报表
	public static final int TYPE_REPORT_NEW = 11; // 新报表
	public static final int TYPE_HELP = 12; // 帮助
	public static final int TYPE_NEW_ATTENDANCE = 13; // 新考勤
	public static final int TYPE_ORDER2 = 14; //订单
	public static final int TYPE_MANAGER = 15;//管理模块
	public static final int TYPE_TODO = 16;//待办事项
	public static final int TYPE_WEB_REPORT = 17;//findReport
	public static final int TYPE_ORDER3 = 18; //订单3
	public static final int TYPE_ORDER3_SEND = 19; //订单3 送货
	public static final int TYPE_CAR_SALES = 20; //车销
	public static final int IS_STORE_ADD_MOD = 21;//新店上报
	public static final int WEI_CHAT = 22;//企业微信
	public static final int MAIN_LIST = 23;//通讯录
	public static final int QUESTIONNAIRE = 24;//调查问卷
	public static final int WORK_PLAN = 25;//工作计划
	public static final int WORK_SUM = 26;//工作总结
	public static final int HUI_YI= 27;//会议

	public static final int COMPANY_TYPE1 = 10031;//皇明太阳能
	public static final int MENU_MODULE_TYPE_2 = 2;// 今天只能提交一次
	public static final int MENU_MODULE_TYPE_3 = 3;// 今天可以提交多次，第二次以后提交时间改成明天的时间
	public static final int MENU_MODULE_TYPE_4 = 4;// 表示是店面拓展模块

	private int id;// 主键
	private int menuId;// 模块ID
	private int type;// 模块类型
	private String name;// 模块名称
	private Integer moduleType; // 模块区分
	private String baseTime;// 基准时间
	private String submitTime;// 最后提交时间
	private int phoneSerialNo;// 手机端menu排序
	private int isNoWait;// 是否无等待提交 0不需要无等待(前端提交)  1 需要无等待（后台提交）
	private String phoneUsableTime;//手机段可用时间段

	private int isEmptyModel;//是否是空模板 1表示是空的占位

	private String icon;//menu的icon
	private ArrayList<String> menuIdList;//menuid的列表
	private ArrayList<Menu> menuList;//menu集合

	public ArrayList<Menu> getMenuList() {
		return menuList;
	}

	public void setMenuList(ArrayList<Menu> menuList) {
		this.menuList = menuList;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public ArrayList<String> getMenuIdList() {
		return menuIdList;
	}

	public void setMenuIdList(ArrayList<String> menuIdList) {
		this.menuIdList = menuIdList;
	}

	public int getIsEmptyModel() {
		return isEmptyModel;
	}

	public void setIsEmptyModel(int isEmptyModel) {
		this.isEmptyModel = isEmptyModel;
	}

	public String getPhoneUsableTime() {
		return phoneUsableTime;
	}

	public void setPhoneUsableTime(String phoneUsableTime) {
		this.phoneUsableTime = phoneUsableTime;
	}

	public int getIsNoWait() {
		return isNoWait;
	}

	public void setIsNoWait(int isNoWait) {
		this.isNoWait = isNoWait;
	}

	public int getPhoneSerialNo() {
		return phoneSerialNo;
	}

	public void setPhoneSerialNo(int phoneSerialNo) {
		this.phoneSerialNo = phoneSerialNo;
	}

	public String getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMenuId() {
		return menuId;
	}

	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getModuleType() {
		return moduleType;
	}

	public void setModuleType(Integer moduleType) {
		this.moduleType = moduleType;
	}

	public String getBaseTime() {
		return baseTime;
	}

	public void setBaseTime(String baseTime) {
		this.baseTime = baseTime;
	}
}
