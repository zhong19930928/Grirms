package com.yunhu.yhshxc.activity.synchrodata;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.database.FuncDB;
import com.yunhu.yhshxc.database.MainMenuDB;
import com.yunhu.yhshxc.database.VisitFuncDB;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;

public class SyncDataUtil {
	private Context context;
	public SyncDataUtil(Context mContext) {
		this.context = mContext;
	}
	
	/**
	 * 模块定义
	 * @return
	 */
	public List<SyncItem> menuConfigSyncDataItem(){
		List<SyncItem> list = new ArrayList<SyncItem>();
		List<Menu> menuList = new MainMenuDB(context).findAllSyncMenuList();
		if (!menuList.isEmpty()) {
			SyncDataUrl synchroadataUrl = new SyncDataUrl(context);
			for (int i = 0; i < menuList.size(); i++) {
				Menu menu = menuList.get(i);
				SyncItem item = new SyncItem();
				item.setName(menu.getName());
				if (menu.getType() == Menu.TYPE_ORDER2) {//新订单
					item.setSyncUrl(synchroadataUrl.orderInfo());
				}else if(menu.getType() == Menu.TYPE_NEW_ATTENDANCE){//新考勤
					item.setSyncUrl(synchroadataUrl.overWorkAttendConf());
					item.setName(SharedPreferencesUtil.getInstance(context).getFixedName().containsKey(3)?SharedPreferencesUtil.getInstance(context).getFixedName().get(3):menu.getName());
				}else if(menu.getType() == Menu.TYPE_NOTICE){//公告
					item.setSyncUrl(synchroadataUrl.notifyInfo());
					item.setName(SharedPreferencesUtil.getInstance(context).getFixedName().containsKey(4)?SharedPreferencesUtil.getInstance(context).getFixedName().get(4):menu.getName());
				}else if(menu.getType() == Menu.TYPE_NEARBY){//就近拜访
					item.setSyncUrl(synchroadataUrl.nearbySync());
				}else{//自定义模块
					item.setSyncUrl(synchroadataUrl.moduleInfo(menu.getMenuId()));
				}
				list.add(item);
			}
		}
		return list;
	}
	
	/**
	 * 数据
	 * @return
	 */
	public List<SyncItem> menuDataSyncDataItem(){
		SyncDataUrl synchroadataUrl = new SyncDataUrl(context);
		List<SyncItem> list = new ArrayList<SyncItem>();
		List<Func> syncDataList = new ArrayList<Func>();
		syncDataList.addAll(new FuncDB(context).findSyncDataFuncList());
		syncDataList.addAll(new VisitFuncDB(context).findSyncDataFuncList());
		SyncItem orgItem = new SyncItem();
		orgItem.setName(PublicUtils.getResourceString(context, R.string.sync_org));
		orgItem.setSyncUrl(synchroadataUrl.orgInfo());
		orgItem.setType(orgItem.TYPE_ORG);
		list.add(orgItem);

		SyncItem orgStoreItem = new SyncItem();
		orgStoreItem.setName(PublicUtils.getResourceString(context,R.string.sync_store));
		orgStoreItem.setSyncUrl(synchroadataUrl.orgStoreInfo());
		orgItem.setType(orgItem.TYPE_STORE);
		list.add(orgStoreItem);

		SyncItem orgUserItem = new SyncItem();
		orgUserItem.setName(PublicUtils.getResourceString(context,R.string.func_name));
		orgItem.setType(orgItem.TYPE_USER);
		orgUserItem.setSyncUrl(synchroadataUrl.orgUserInfo());
		list.add(orgUserItem);
		if (!syncDataList.isEmpty()) {
			for (int i = 0; i < syncDataList.size(); i++) {
				Func func = syncDataList.get(i);
				String dict_table = func.getDictTable();
				if (!TextUtils.isEmpty(dict_table)) {
					SyncItem item = new SyncItem();
					item.setName(func.getName());
					orgItem.setType(orgItem.TYPE_SPINNER);
					String ids = dict_table.split("_")[2];
					item.setSyncUrl(synchroadataUrl.dictInfo(Integer.parseInt(ids)));
					list.add(item);
				}
			}
		}
		
		return list;
	}
}
