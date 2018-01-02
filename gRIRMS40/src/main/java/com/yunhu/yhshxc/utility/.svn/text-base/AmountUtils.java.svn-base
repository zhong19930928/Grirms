package com.yunhu.yhshxc.utility;

import java.math.BigDecimal;


public class AmountUtils {
	
	/**金额为分的格式 */
	public static final String CURRENCY_FEN_REGEX = "\\-?[0-9]+";

	/**
	 * 将分为单位的转换为元 （除100）
	 * 
	 * @param amount
	 * @return
	 * @throws Exception 
	 */
	public static String changeF2Y(String amount) throws Exception{
		if(!amount.matches(CURRENCY_FEN_REGEX)) {
			throw new Exception("金额格式有误");
		}
		return BigDecimal.valueOf(Long.valueOf(amount)).divide(new BigDecimal(100)).toString();
	}
	
	/** 
	 * 将元为单位的转换为分 （乘100）
	 * 
	 * @param amount
	 * @return
	 */
	public static String changeY2F(Long amount){
		return BigDecimal.valueOf(amount).multiply(new BigDecimal(100)).toString();
	}
	

}

