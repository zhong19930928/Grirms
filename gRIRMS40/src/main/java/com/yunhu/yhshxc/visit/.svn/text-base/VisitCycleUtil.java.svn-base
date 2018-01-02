package com.yunhu.yhshxc.visit;

import gcg.org.debug.JLog;

import java.text.ParseException;

import android.content.Context;
import android.text.TextUtils;

import com.yunhu.yhshxc.bo.VisitStore;
import com.yunhu.yhshxc.database.VisitStoreDB;
import com.yunhu.yhshxc.utility.DateUtil;

public class VisitCycleUtil {
	private VisitStoreDB visitStoreDB;
	private int cycleCount;//周期数
	private int visitCount;//拜访次数
	private String startDate;//开始日期
	
	public VisitCycleUtil(Context context) {
		visitStoreDB = new VisitStoreDB(context);
	}
	/**
	 * 每天类型判断断面是否可操作
	 * @param visitStore
	 * @return true 表示完成上报 false 表是还可以上报
	 * @throws ParseException
	 */
	public boolean dayVisitType(VisitStore visitStore) throws ParseException{
		
		boolean isFinish = false;//默认没完成上报
		String submitDate = visitStore.getSubmitDate();//上次提交日期
		if (!TextUtils.isEmpty(submitDate)) {
			int submitNumber = visitStore.getSubmitNum();//已经提交
			String[] dataStr = submitDate.split(" ");
			if (dataStr.length==2) {
				submitDate = dataStr[0];
			}
			String nowDateStr = DateUtil.getCurDate();
			int subCycle = cycleNumber(submitDate, cycleCount);//提交日期所在的周期数
			int nowCycle = cycleNumber(nowDateStr, cycleCount);//当前日期所在的周期数
			if (subCycle == nowCycle) {//当前日期和提交日期在同一个周期内
				if (submitNumber >=visitCount) {//规定周期内报够了次数则不能再上报
					isFinish = true;
				}
			}else{//当前日期和提交日期不在同一个周期内
				visitStore.setSubmitNum(0);//清空已经提交的次数
				visitStoreDB.updateVisitStoreById(visitStore);
			}
			JLog.d("访店("+visitStore.getName()+"):"+isFinish+","+submitNumber+"/"+submitDate+","+nowDateStr+"/"+subCycle+","+nowCycle);
		}else{
			JLog.d("访店("+visitStore.getName()+"):"+isFinish+","+visitStore.getSubmitNum()+"/"+submitDate);
		}
		return isFinish;
	}
	
	/**
	 * 每周类型判断店面是否可操作
	 * @param visitStore
	 * @return true 表示完成上报 false 表是还可以上报
	 * @throws ParseException
	 */
	public boolean weekVisitType(VisitStore visitStore) throws ParseException{
		boolean isFinish = false;//默认没完成上报
		String submitDate = visitStore.getSubmitDate();//上次提交日期
		if (!TextUtils.isEmpty(submitDate)) {
			int submitNumber = visitStore.getSubmitNum();//已经提交
			String[] dataStr = submitDate.split(" ");
			if (dataStr.length==2) {
				submitDate = dataStr[0];
			}
			String nowDateStr = DateUtil.getCurDate();
			String firstDayInNowDate = DateUtil.getFirstDateByWeek(DateUtil.getDate(nowDateStr, DateUtil.DATAFORMAT_STR));
			String firstDayInSubDate = DateUtil.getFirstDateByWeek(DateUtil.getDate(submitDate, DateUtil.DATAFORMAT_STR));
			int subCycle = cycleNumber(firstDayInSubDate, cycleCount*7);//提交日期所在的周期数
			int nowCycle = cycleNumber(firstDayInNowDate, cycleCount*7);//当前日期所在的周期数
			if (subCycle == nowCycle) {//当前日期和提交日期在同一个周期内
				if (submitNumber >=visitCount) {//规定周期内报够了次数则不能再上报
					isFinish = true;
				}
			}else{//当前日期和提交日期不在同一个周期内
				visitStore.setSubmitNum(0);//清空已经提交的次数
				visitStoreDB.updateVisitStoreById(visitStore);
			}
			JLog.d("访店("+visitStore.getName()+"):"+isFinish+","+submitNumber+"/"+submitDate+","+nowDateStr+"/"+subCycle+","+nowCycle);
		}else{
			JLog.d("访店("+visitStore.getName()+"):"+isFinish+","+visitStore.getSubmitNum()+"/"+submitDate);
		}
		return isFinish;
	}
	
	/**
	 * 每月类型判断店面是否可操作
	 * @param visitStore
	 * @return true 表示完成上报 false 表是还可以上报
	 * @throws ParseException
	 */
	public boolean monthVisitType(VisitStore visitStore) throws ParseException{
		boolean isFinish = false;//默认没完成上报
//		visitCount = 3;
//		cycleCount = 3;
//		startDate = "2013-12-16";
//		submitDate = "2013-12-18";
		String submitDate = visitStore.getSubmitDate();//上次提交日期
		if (!TextUtils.isEmpty(submitDate)) {
			String[] dataStr = submitDate.split(" ");
			if (dataStr.length==2) {
				submitDate = dataStr[0];
			}
			int submitNumber = visitStore.getSubmitNum();//已经提交
			String nowDateStr = DateUtil.getCurDate();
			int subCycle = monthTypeCycleNumber(submitDate, cycleCount);
			int nowCycle = monthTypeCycleNumber(nowDateStr, cycleCount);
			if (subCycle == nowCycle) {//当前月份和提交日期的月份在同一个周期内
				if (submitNumber >=visitCount) {//规定周期内报够了次数则不能再上报
					isFinish = true;
				}
			}else{//当前日期和提交日期不在同一个周期内
				visitStore.setSubmitNum(0);//清空已经提交的次数
				visitStoreDB.updateVisitStoreById(visitStore);
			}
			JLog.d("访店("+visitStore.getName()+"):"+isFinish+","+submitNumber+"/"+submitDate+","+nowDateStr+"/"+subCycle+","+nowCycle);
		}else{
			JLog.d("访店("+visitStore.getName()+"):"+isFinish+","+visitStore.getSubmitNum()+"/"+submitDate);
		}			
		return isFinish;
	}
	
	/**
	 * 指定日期所在的周期数
	 * @param dateStr 指定日期
	 * @param interval 间隔天数
	 * @return
	 */
	public int cycleNumber(String dateStr,int interval){
		int number = 0;
		try {
			int days = DateUtil.daysBetween(startDate, dateStr);//指定日期到开始日期所差的天数
			number = days / interval;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return number;
	}
	
	/**
	 * 每月类型的判断指定日期坐在的周期数
	 * @param dateStr 指定日期 cycle 周期
	 * @return
	 */
	public int monthTypeCycleNumber(String dateStr,int cycle){
		int number = 0;
		try {
			int difderMonth = DateUtil.getMonthSpace(startDate, dateStr);//开始日期到指定日期的月数
			number = difderMonth / cycle;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return number;
	}
	/**
	 * 自定义类型的判断店面是否可操作
	 * @param visitStore
	 * @return true 表示完成上报 false 表是还可以上报
	 * @throws ParseException
	 */
	public boolean moduleVisitType(VisitStore visitStore) throws ParseException{
		boolean isFinish = false;
		String submitDate = visitStore.getSubmitDate();
		if (!TextUtils.isEmpty(submitDate)) {
			int submitNumber = visitStore.getSubmitNum();//已经提交
			if (submitNumber >=visitCount) {//规定周期内报够了次数则不能再上报
				isFinish = true;
			}
			JLog.d("访店("+visitStore.getName()+"):"+isFinish+","+submitNumber+"/"+submitDate);
		}else{
			JLog.d("访店("+visitStore.getName()+"):"+isFinish+","+visitStore.getSubmitNum()+"/"+submitDate);
		}
		return isFinish;
	}
	
	public void setCycleCount(int cycleCount) {
		this.cycleCount = cycleCount;
	}
	public void setVisitCount(int visitCount) {
		this.visitCount = visitCount;
	}
	public void setStartDate(String startDate) {
		this.startDate = DateUtil.dateToDateString(DateUtil.getDate(startDate), DateUtil.DATAFORMAT_STR);
	}
}
