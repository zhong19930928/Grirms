package com.yunhu.yhshxc.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.order3.util.SharedPreferencesForOrder3Util;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.PublicUtils;

import static com.yunhu.yhshxc.application.SoftApplication.context;

public class MenuUsableControl{
	/**
	 * 判断模块是否可用
	 * time格式为”09:00|4.0,21:00|3.0”，即”开始时间|可用时长,开始时间|可用时长“。
	 * 可能多个时间段，可能夸天
	 * @param time
	 * @return true 可以用 false 不可以使用
	 */
	public boolean isCanUse(String time){
		boolean flag = false;
		if (TextUtils.isEmpty(time)) {//时间是空的话可以操作
			flag = true;
		}else{
			List<Config> configList = configs(time);
			if (!configList.isEmpty()) {
				for (int i = 0; i < configList.size(); i++) {
					 Config config = configList.get(i);
					 String configStart = config.getStartDate();
					 String configEnd = config.getEndDate();
					 String nowDateStr = DateUtil.getDateTime()+":00";
					 String startHour = configStart.split(" ")[1];
					 String endHour = configEnd.split(" ")[1];
					 String nowHour = nowDateStr.split(" ")[1];
					 if (startHour.compareTo(endHour)<0) {//没夸天
						 if (nowHour.compareTo(startHour)>=0 && nowHour.compareTo(endHour)<=0) {
							 flag = true;
							 break;
						 }
					}else{//夸天
						if (nowHour.compareTo(startHour) >=0 || nowHour.compareTo(endHour)<=0) {
							 flag = true;
							 break;
						 }
					}
				}
			}else{
				flag = true;
			}
		}
		return flag;
	}
	/**
	 * 判断模块是否可用
	 * time格式为”09:00|4.0,21:00|3.0”，即”开始时间|可用时长,开始时间|可用时长“。
	 * 可能多个时间段，可能夸天
	 * @param time
	 * @return true 可以用 false 不可以使用
	 */
	public boolean isCanUse(String time,Date date){
		boolean flag = false;
		if (TextUtils.isEmpty(time)) {//时间是空的话可以操作
			flag = true;
		}else{
			List<Config> configList = configs(time);
			if (!configList.isEmpty()) {
				for (int i = 0; i < configList.size(); i++) {
					Config config = configList.get(i);
					String configStart = config.getStartDate();
					String configEnd = config.getEndDate();
					String nowDateStr ;
					if(null==date){
						 nowDateStr = DateUtil.getDateTime()+":00";
					}else{
						nowDateStr = DateUtil.dateToDateString(date, DateUtil.YYYY_MM_DD_HH_MM)+":00";
					}
					String startHour = configStart.split(" ")[1];
					String endHour = configEnd.split(" ")[1];
					String nowHour = nowDateStr.split(" ")[1];
					if (startHour.compareTo(endHour)<0) {//没夸天
						if (nowHour.compareTo(startHour)>=0 && nowHour.compareTo(endHour)<=0) {
							flag = true;
							break;
						}
					}else{//夸天
						if (nowHour.compareTo(startHour) >=0 || nowHour.compareTo(endHour)<=0) {
							flag = true;
							break;
						}
					}
				}
			}else{
				flag = true;
			}
		}
		return flag;
	}
	
	/**
	 * 返回一个模块可用时间段的集合
	 * @param time
	 * @return
	 */
	private List<Config> configs(String time){
		List<Config> list = new ArrayList<Config>();
		if (!TextUtils.isEmpty(time)) {
			String[] mConfig = time.split(",");
			Config config = null;
			for (int i = 0; i < mConfig.length; i++) {
				String[] configTime = mConfig[i].split("\\|");
				if (configTime.length==2) {
					config = new Config(configTime[0], Float.parseFloat(configTime[1]));
					list.add(config);
				}
			}
		}
		return list;
	}
	
	/**
	 * 不能操作提示信息
	 * @param time
	 * @return
	 */
	public String tipInfo(String time){
		StringBuffer info = new StringBuffer(PublicUtils.getResourceString(context, R.string.date_can)+"\n");
		if (!TextUtils.isEmpty(time)) {
			String[] mConfig = time.split(",");
			for (int i = 0; i < mConfig.length; i++) {
				String[] configTime = mConfig[i].split("\\|");
				if (configTime.length==2) {
					Config config = new Config(configTime[0], Float.parseFloat(configTime[1]));
					String[] start = config.getStartDate().split(" ");
					String[] end = config.getEndDate().split(" ");
					String startDay = start[0];
					String endDay = end[0];
					String startTime = start[1].split(":")[0];
					if (startTime.startsWith("0")) {
						startTime = startTime.substring(1);
					}
					if (startDay.equals(endDay)) {
						info.append(startTime +":"+start[1].split(":")[1]).append(" - ");
						info.append(end[1].split(":")[0]+":"+end[1].split(":")[1]).append("\n");
					}else{
						info.append(startTime +":"+start[1].split(":")[1]).append(" - ");
						info.append(PublicUtils.getResourceString(context,R.string.afterday)).append(end[1].split(":")[0]+":"+end[1].split(":")[1]).append("\n");
					}
				}
			}
		}
		return info.substring(0, info.length()-1);
	}
	
	
	/**
	 * 模块可使用的开始时间和结束时间
	 * @author gcg_jishen
	 *
	 */
	class Config{
		private String startDate;//开始时间
		private String endDate;//结束时间
		
		public Config(String startHour,float useHour) {
			String[] str = startHour.split(":");
			String s_hour = String.valueOf(Integer.parseInt(str[0])+100).substring(1);
			String s_min = String.valueOf(Integer.parseInt(str[1])+100).substring(1);
			int useMin = (int) (useHour*60);
			startDate =new StringBuffer(DateUtil.getCurDate()).append(" ").append(s_hour+":"+s_min).append(":00").toString();
			endDate = DateUtil.addDate(DateUtil.getDate(startDate), Calendar.MINUTE, useMin);
		}

		public String getStartDate() {
			return startDate;
		}

		public String getEndDate() {
			return endDate;
		}
		
		
	}
	
	/**
	 * 订单3是否能用
	 * 分为每周类型和每月类型
	 * 每周 1010111  1表示能用0表示不能用
	 * 每月类型  10~15|20~30  表示日期范围段可用
	 * @return true 表示可用 false 不可用
	 */
	public boolean isOrder3CanUse(Context context){
		boolean flag = false;
		String week = SharedPreferencesForOrder3Util.getInstance(context).getUnableWeek();
		if (!TextUtils.isEmpty(week)) {//每周类型的
			Calendar c = Calendar.getInstance();
			int day = c.get(Calendar.DAY_OF_WEEK) - 1;// 获取今天是本周的第几天 从星期一开始
			day = day == 0 ? 7 : day;
			int n = Integer.valueOf(week, 2) >> (7 - day);
			if (n % 2 == 1) { // 判断今天执行不 1 的话就表示今天执行，0 表示今天不执行
				flag = true;
			}else {
				flag = false;
			}
		}else{//每月类型的
			String date = SharedPreferencesForOrder3Util.getInstance(context).getUnableDate();
			if (!TextUtils.isEmpty(date)) {
				String[] s1 = date.split("\\|");
				for (int i = 0; i < s1.length; i++) {
					String s = s1[i];
					String[] s2 = s.split("~");
					if (s2!=null && s2.length == 2) {
						String d1 = s2[0];//开始日期
						String d2 = s2[1];//结束日期
						if (PublicUtils.isNumeric(d1) && PublicUtils.isNumeric(d2)) {
							int today = DateUtil.getDay(new Date());//今天
							int start = Integer.parseInt(d1);
							int end = Integer.parseInt(d2);
							if (today == start || today == end || (today>start && today<end)) {
								flag = true;
								break;
							}
						}
					}
				}
			}else{//表示没设置每周和每月的几号可操作控制
				flag = true;
			}
		}
		return flag;
	}
	/**
	 * 订单3是否能用
	 * 分为每周类型和每月类型
	 * 每周 1010111  1表示能用0表示不能用
	 * 每月类型  10~15|20~30  表示日期范围段可用
	 * @return true 表示可用 false 不可用
	 */
	public boolean isOrder3CanUse(Context context,Date dateOrder3){
		boolean flag = false;
		String week = SharedPreferencesForOrder3Util.getInstance(context).getUnableWeek();
		if (!TextUtils.isEmpty(week)) {//每周类型的
			Calendar c = Calendar.getInstance();
			c.setTime(dateOrder3);  
			int day = c.get(Calendar.DAY_OF_WEEK) - 1;// 获取今天是本周的第几天 从星期一开始
			day = day == 0 ? 7 : day;
			int n = Integer.valueOf(week, 2) >> (7 - day);
							if (n % 2 == 1) { // 判断今天执行不 1 的话就表示今天执行，0 表示今天不执行
								flag = true;
							}else {
								flag = false;
							}
		}else{//每月类型的
			String date = SharedPreferencesForOrder3Util.getInstance(context).getUnableDate();
			if (!TextUtils.isEmpty(date)) {
				String[] s1 = date.split("\\|");
				for (int i = 0; i < s1.length; i++) {
					String s = s1[i];
					String[] s2 = s.split("~");
					if (s2!=null && s2.length == 2) {
						String d1 = s2[0];//开始日期
						String d2 = s2[1];//结束日期
						if (PublicUtils.isNumeric(d1) && PublicUtils.isNumeric(d2)) {
							int today = DateUtil.getDay(dateOrder3);//今天
							int start = Integer.parseInt(d1);
							int end = Integer.parseInt(d2);
							if (today == start || today == end || (today>start && today<end)) {
								flag = true;
								break;
							}
						}
					}
				}
			}else{//表示没设置每周和每月的几号可操作控制
				flag = true;
			}
		}
		return flag;
	}
}
