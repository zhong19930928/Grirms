package com.yunhu.yhshxc.activity.synchrodata;

import android.content.Context;

import com.yunhu.yhshxc.utility.UrlInfo;

public class SyncDataUrl {
	
	private String baseUrl = null;
	public SyncDataUrl(Context context) {
//		baseUrl = context.getResources().getString(R.string.BASE_URL);
		baseUrl = UrlInfo.baseUrl(context);
	}
	
	/**
	 * 更新全部
	 * @return
	 */
	public String allInfo(){
		String url = baseUrl+"allInfo.do";
		return url;
	}
	/**
	 * 拜访任务
	 * @return
	 */
	public String visitInfo(){
		String url = baseUrl+"visitSync.do";
		return url;
	}
	
	/**
	 * 模块定义
	 * @return
	 */
	public String moduleInfo(){
		String url = baseUrl+"moduleSync.do";
		return url;
	}
	
	/**
	 * 单个模块定义
	 * @param menuId 模块id
	 * @return
	 */
	public String moduleInfo(int menuId){
		String url = baseUrl+"moduelParamInfo.do?ids="+menuId;
		return url;
	}
	
	/**
	 * 订单信息
	 * @return
	 */
	public String orderInfo(){
		String url = baseUrl+"orderParamInfo.do";
		return url;
	}
	
	/**
	 * 机构数据
	 * @return
	 */
	public String orgInfo(){
		String url = baseUrl+"operationOrStoreInfo.do?operation=1";
		return url;
	}
	/**
	 * 用户数据
	 * @return
	 */
	public String orgUserInfo(){
		String url = baseUrl+"operationOrStoreInfo.do?operation=2";
		return url;
	}
	/**
	 * 店面数据
	 * @return
	 */
	public String orgStoreInfo(){
		String url = baseUrl+"operationOrStoreInfo.do?operation=3";
		return url;
	}
	
	/**
	 * 单个字典表数据
	 * @param dataId 字典表控件id
	 * @return
	 */
	public String dictInfo(int dataId){
		String url = baseUrl + "dictInfo.do?ids="+dataId;
		return url;
	}
	
	/**
	 * 同步所有数据接口 包含 机构，店面，用户，所有字典表
	 * @return
	 */
	public String allData(){
		String url = baseUrl+"operationOrStoreInfo.do?operation=1,2,3";
		return url;
	}
	
	/**
	 * 同步所有数据接口 包含 机构，店面，用户
	 * @return
	 */
	public String allOperation(){
		String url = baseUrl+"operationOrStoreInfo.do?operation=1,2,3";
		return url;
	}
	
	/**
	 * 新考勤配置信息
	 * @return
	 */
	public String overWorkAttendConf(){
		String url = baseUrl+"overWorkAttendConf.do";
		return url;
	}
	/**
	 * 公告信息
	 * @return
	 */
	public String notifyInfo(){
		String url = baseUrl+"notifyInfo.do";
		return url;
	}
	
	/**
	 * 就近拜访
	 * @return
	 */
	public String nearbySync(){
		String url = baseUrl+"nearbySync.do";
		return url;
	}
	
}
