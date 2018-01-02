package com.yunhu.yhshxc.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 
 * @author Jarvis
 * 90%的验证都调用了Regular方法 但是本类也可删除大部分方法 涉及到正则的判断都直接穿参数和正则表达式
 * 但是为了方便业务类调用和有更直观的含义 建议不要这么做
 * Pattern的matcher已经被同步synchronized 所以 此类的任何使用正则验证的方法都不需要同步
 *
 */
public class RegExpvalidateUtils {

	//------------------常量定义
	/**
	 * 验证数字[0-9]+(\\.[0-9]+)?
	 */
	public static final String NUMERIC = "-?[0-9]+(\\.[0-9]+)?";
	/**
	 * Integer正则表达式 -?\\d+
	 */
	public static final String INTEGER = "-?\\d+";

	/**
	 * 判断字段是否为INTEGER  符合返回ture
	 * @param str
	 * @return boolean
	 */
	public static boolean isInteger(String str) {
		return Regular(str,INTEGER);
	}
	
	/**
	 * 判断字段是否为INTEGER  符合返回ture
	 * @param str
	 * @return boolean
	 */
	public static boolean isNumeric(String str) {
		return Regular(str,NUMERIC);
	}
		
 	/**
 	 * 匹配是否符合正则表达式pattern 匹配返回true
 	 * @param str 匹配的字符串
 	 * @param pattern 匹配模式
 	 * @return boolean
 	 */
 	private static boolean Regular(String str,String pattern){
 		System.out.println("pattern="+pattern);
		if(null == str || str.trim().length()<=0)
			return false;         
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(str);
		return m.matches();
 	}
}
