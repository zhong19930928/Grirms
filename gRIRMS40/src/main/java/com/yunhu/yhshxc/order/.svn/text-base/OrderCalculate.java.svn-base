package com.yunhu.yhshxc.order;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Random;

import com.yunhu.yhshxc.utility.DateUtil;

/**
 * 订单计算类
 * @author jishen
 *
 */
public class OrderCalculate {
	
	/**
	 * 返回订单编号
	 */
	public static String getOrderNumber() {
		String currentMsec = DateUtil.getCurMsecDate();
		Random i = new Random();
		int j = i.nextInt(10);
		return currentMsec + j;
	}
	
	/**
	 * 乘法计算 first*second
	 * @param first
	 * @param second
	 * @return
	 */
	public static String computeMultiply(String first,String second){
		String result="";
		BigDecimal b =new BigDecimal(first).multiply(new BigDecimal(second));  //计算相乘
		Double f1= b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();//保留两位进1
		result=new DecimalFormat("#.##").format(f1);//如果小数点后是0舍弃
		return result;
	}
	
	/**
	 * 加法计算 first+second
	 * @param first
	 * @param second
	 * @return
	 */
	public static String computeAdd(String first,String second){
		String result="";
		BigDecimal b =new BigDecimal(first).add(new BigDecimal(second));  //计算相加
		Double f1= b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();//保留两位进1
		result=new DecimalFormat("#.##").format(f1);//如果小数点后是0舍弃
		return result;
	}
	
	/**
	 * 减法计算  first-second
	 * @param first
	 * @param second
	 * @return
	 */
	public static String computeSubduct(String first,String second){
		String result="";
		BigDecimal b =new BigDecimal(first).subtract(new BigDecimal(second));  //计算相减
		Double f1= b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();//保留两位进1)new BigDecimal(.doubleValue()-
		result=new DecimalFormat("#.##").format(f1);//如果小数点后是0舍弃
		return result;
	}
}
