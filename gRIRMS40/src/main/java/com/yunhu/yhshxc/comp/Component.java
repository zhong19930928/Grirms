package com.yunhu.yhshxc.comp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;
import android.view.View;

import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.utility.PublicUtils;

public abstract class Component {
	
	public Integer type;//类型

	public Integer param;//控件ID

	public String value;//控件值
	
	public String dataName;//多选内容名称
	
	public String selectedValue;//选择的值

	public Integer wayId;//线路ID

	public Integer planId;//计划ID

	public Integer storeId;//店面ID

	public Integer targetid;//数据ID

	public String storeName;//店面名称

	public String wayName;//线路名称

	public Integer targetType;//模块类型
	
	public Func compFunc;//当前控件func
	
	public boolean isLink;//是否是超链接 true是 false不是
	
	public int compRow;//表示该控件在表格中的第几行（初始化时候的行号）
	
	/**
	 * 初始化数据
	 * @param type
	 * @param wayId
	 * @param planId
	 * @param storeId
	 * @param targetid
	 * @param targetType
	 */
	public void putData(Integer type,Integer wayId,Integer planId,Integer storeId,Integer targetid,Integer targetType){
		this.type = type;
		this.wayId = wayId;
		this.planId = planId;
		this.storeId = storeId;
		this.targetid = targetid;
		this.targetType = targetType;
	}
	
	public abstract View getObject();

	/**
	 * 设置控件是否可用
	 * @param isEnable true可用 false 不可用
	 */
	public abstract void setIsEnable(boolean isEnable);
	
	/*
	 * 判断该组件的值是否为空 true表示为空，false表示非空
	 */
	public boolean isEmpty() {
		return TextUtils.isEmpty(value)?true:false;
	}
	
	/*
	 * 验证邮箱
	 */
	public boolean isMail() {
		Pattern pattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
	}
	
	/*
	 * 验证邮编
	 */
	public boolean isZipCode(){
		Pattern pattern = Pattern.compile("[1-9]\\d{5}");
		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
	}
	
	/*
	 * 验证数字
	 */
	public boolean isNumeric() {
//		Pattern pattern = Pattern.compile("\\d+");
		Pattern pattern = Pattern.compile("[0-9]+(\\.[0-9]+)?");
		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
	}
	
	/*
	 * 验证整数
	 */
	public boolean isInteger() {
		Pattern pattern = Pattern.compile("-?\\d+");
		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
	}
	
	/*
	 * 验证固定电话号
	 */
	public boolean isFixedTelephone(){
		//Pattern pattern = Pattern.compile("0\\d{2}-\\d{8}|0\\d{3}-\\d{7}");
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
	}
	
	/*
	 * 验证手机电话号
	 */
	public boolean isMobileTelephone(){
		return PublicUtils.isMobileTelephone(value);
	}
	
	/*
	 * 验证身份证号
	 */
	public boolean isIDNumber(){
		String id = "((11|12|13|14|15|21|22|23|31|32|33|34|35|36|37|41|42|43|44|45|46|50|51|52|53|54|61|62|63|64|65|71|81|82|91)\\d{4})((((19|20)(([02468][048])|([13579][26]))0229))|((20[0-9][0-9])|(19[0-9][0-9]))((((0[1-9])|(1[0-2]))((0[1-9])|(1\\d)|(2[0-8])))|((((0[1,3-9])|(1[0-2]))(29|30))|(((0[13578])|(1[02]))31))))((\\d{3}(x|X))|(\\d{4}))";  
		//String id = "[1-9][0-7]\\d{4}(19|20)\\d{2}[0-1]\\d{1}(([0-2]\\d{1})|(3(0|1)))\\d{3}(\\d|x|X)";
		Pattern pattern = Pattern.compile(id);
		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
	}
}
