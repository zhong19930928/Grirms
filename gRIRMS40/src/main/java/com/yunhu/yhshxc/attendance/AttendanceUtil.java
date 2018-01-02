package com.yunhu.yhshxc.attendance;

import gcg.org.debug.JLog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.yunhu.yhshxc.attendance.backstage.AttendanceAutoLocationService;
import com.yunhu.yhshxc.attendance.backstage.AttendanceLocationService;
import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.bo.OrgUser;
import com.yunhu.yhshxc.database.MainMenuDB;
import com.yunhu.yhshxc.database.OrgUserDB;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;

/**
 * 只处理新考勤
 * @author shenji
 *
 */
public class AttendanceUtil {

	private static Context context;

	public static void resetAttendance(Context mContext,Date date){
		context = mContext;
		String startDate;
		String stopDate;
		boolean start=false;
		boolean end =false;
		if (!PublicUtils.ISDEMO) {
			// 考勤的上班日期
			 startDate = SharedPreferencesUtil.getInstance(mContext).getNewAttendanceStart();
			// 考勤的下班日期
			 stopDate = SharedPreferencesUtil.getInstance(mContext).getNewAttendanceEnd();
				JLog.d("考勤endtrue：上打:"+startDate+" 下打:"+stopDate);
				if (!TextUtils.isEmpty(startDate)) {//上班时间有值的话说明上班已经打过卡
					SharedPreferencesUtil.getInstance(mContext).setNewAttendanceStartDoCard(true);
				}else{
					SharedPreferencesUtil.getInstance(mContext).setNewAttendanceStartDoCard(false);
				}
				if (!TextUtils.isEmpty(stopDate)) {//下班时间有值说明下班已经打过卡
					SharedPreferencesUtil.getInstance(mContext).setNewAttendanceEndDoCard(true);
				}else{
					SharedPreferencesUtil.getInstance(mContext).setNewAttendanceEndDoCard(false);
				}
				 start = SharedPreferencesUtil.getInstance(mContext).getNewAttendanceStartDoCard();
				 end	= SharedPreferencesUtil.getInstance(mContext).getNewAttendanceEndDoCard();	
		}else{
			// 考勤的上班日期
			 startDate = SharedPreferencesUtil.getInstance(mContext).getNativeAttendanceStart();
			// 考勤的下班日期
			 stopDate = SharedPreferencesUtil.getInstance(mContext).getNativeAttendanceEnd();
				if (!TextUtils.isEmpty(startDate)) {//上班时间有值的话说明上班已经打过卡
					SharedPreferencesUtil.getInstance(mContext).setNativeStartCard(true);
				}else{
					SharedPreferencesUtil.getInstance(mContext).setNativeStartCard(false);
				}
				if (!TextUtils.isEmpty(stopDate)) {//下班时间有值说明下班已经打过卡
					SharedPreferencesUtil.getInstance(mContext).setNativeEndCard(true);
				}else{
					SharedPreferencesUtil.getInstance(mContext).setNativeEndCard(false);
				}
				 start = SharedPreferencesUtil.getInstance(mContext).getNativeStartCard();
				 end	= SharedPreferencesUtil.getInstance(mContext).getNativeEndCard();
		}




		String resetType = SharedPreferencesUtil.getInstance(mContext).getNewAttendanceResetType();
		boolean isReset = "2".equals(resetType)?false:true;//1：按天自动复位、2：都完成后复位
		if (end || (isReset && start)) {// 下班打过卡 或者上班打过卡并且跨天要初始化
			// 当前日期是否大于上班日期
			if(null!=date){
				Date sDate = DateUtil.getDate(startDate.split(" ")[0],DateUtil.DATAFORMAT_STR);// 上班日期
				Date cDate = DateUtil.getDate(DateUtil.dateToDateString(date,DateUtil.DATAFORMAT_STR),DateUtil.DATAFORMAT_STR);// 当前日期
				JLog.d("考勤endtrue：上打:" + DateUtil.dateToDateString(sDate) + " 当前:"+ DateUtil.dateToDateString(cDate));
				if (sDate.before(cDate)) {// 说明跨天了要初始化
					start = false;
					end = false;
					if (!PublicUtils.ISDEMO) {
						SharedPreferencesUtil.getInstance(mContext).setNewAttendanceStartDoCard(start);
						SharedPreferencesUtil.getInstance(mContext).setNewAttendanceEndDoCard(end);
						SharedPreferencesUtil.getInstance(mContext).setNewAttendanceStart("");
						SharedPreferencesUtil.getInstance(mContext).setNewAttendanceEnd("");
					}else{
						SharedPreferencesUtil.getInstance(mContext).setNativeStartCard(start);
						SharedPreferencesUtil.getInstance(mContext).setNativeEndCard(end);
                        SharedPreferencesUtil.getInstance(mContext).setNativeAttendanceStart("");
                        SharedPreferencesUtil.getInstance(mContext).setNativeAttendanceEnd("");
                        
					}
					SharedPreferencesUtil.getInstance(context).setNewAttendTime(DateUtil.dateToDateString(date, DateUtil.DATATIMEF_STR));//初始化的时候设置考勤时间	
					
				}
			}
			
		}
		
		
		if(!start && !end){
			if(null!=date){
				Date cDate = DateUtil.getDate(DateUtil.dateToDateString(date,DateUtil.DATAFORMAT_STR),DateUtil.DATAFORMAT_STR);// 当前日期
				String time = SharedPreferencesUtil.getInstance(mContext).getNewAttendTime();
				if(!TextUtils.isEmpty(time)){
					Date initData = DateUtil.getDate(time,DateUtil.DATAFORMAT_STR);//
					if(initData.before(cDate)){
						SharedPreferencesUtil.getInstance(context).setNewAttendTime(DateUtil.dateToDateString(date, DateUtil.DATATIMEF_STR));;//初始化的时候设置考勤时间
					}
				}else{
					SharedPreferencesUtil.getInstance(context).setNewAttendTime(DateUtil.dateToDateString(date, DateUtil.DATATIMEF_STR));//初始化的时候设置考勤时间
				}
			}
			
			
		}
		
	}

	/**
	 * 开启守店定位服务
	 * 
	 * @param mContext
	 * @param state
	 */
	public static void startAttendanceLocationService(Context mContext, int state) {
		int serverFlg = SharedPrefsAttendanceUtil.getInstance(mContext).getAttendanceBackstageLocationServiceFlg();
		if (serverFlg == 2) {
			Intent intent = new Intent(mContext, AttendanceAutoLocationService.class);
			mContext.startService(intent);
		} else {
			int i = SharedPrefsAttendanceUtil.getInstance(mContext).getAttendanceBackstageLocationIntervalTime();
			Menu attendanceMenu = new MainMenuDB(mContext).findMenuListByMenuType(Menu.TYPE_NEW_ATTENDANCE);
			if (i > 0 && attendanceMenu != null) {
				Intent intent = new Intent(mContext, AttendanceLocationService.class);
				if (state != 0) {
					intent.putExtra(AttendanceLocationService.WORK_STATUS, state);
				}
				mContext.startService(intent);
			}
		}
	}

	/**
	 * 考勤排班数据
	 * 
	 * @param context
	 * @return
	 */
	public List<Dictionary> attendanceScheduingData(Context context, String fuuy) {
		String attendAuth = SharedPreferencesUtil.getInstance(context.getApplicationContext()).getAttendAuth(); // 考勤加入权限
		if (TextUtils.isEmpty(attendAuth)) {
			attendAuth = SharedPreferencesUtil.getInstance(context.getApplicationContext()).getNewAttendAuth(); // 新考勤加入权限
		}
		Integer auth = null;
		String authOrgId = null;
		if (!TextUtils.isEmpty(attendAuth)) {
			String arr[] = attendAuth.split("\\|");
			auth = Integer.valueOf(arr[0]);
			if (arr.length == 2) {
				authOrgId = arr[1];
			}
		}
		List<OrgUser> list = new OrgUserDB(context).findOrgUserListByAuth(auth, authOrgId, fuuy, null);
		return setDataSrcListForOrgUser(list);
	}

	/**
	 * 将用户实例转化为字典的形式
	 * 
	 * @param orgUserList
	 *            用户实例集合
	 */
	private List<Dictionary> setDataSrcListForOrgUser(List<OrgUser> orgUserList) {
		List<Dictionary> dataSrcList = new ArrayList<Dictionary>();
		if (orgUserList != null && !orgUserList.isEmpty()) {
			for (int i = 0; i < orgUserList.size(); i++) {
				OrgUser orgUser = orgUserList.get(i);
				Dictionary dic = new Dictionary();
				dic.setCtrlCol(orgUser.getUserName());
				dic.setDid(orgUser.getUserId() + "");
				dataSrcList.add(dic);
			}
		}
		return dataSrcList;
	}
}
