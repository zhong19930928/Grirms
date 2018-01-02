package com.yunhu.yhshxc.style;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;

import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.database.MainMenuDB;
import com.yunhu.yhshxc.database.StyleDB;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.FileHelper;

public class StyleUtil {

	public static List<Style> findImageUrlForStyle(Context context){
		
		ArrayList<Style> noDownloadUrlList = new ArrayList<Style>();
		
		MainMenuDB menuDB = new MainMenuDB(context);
		StyleDB styleDB = new StyleDB(context);
		Style style = null;
		//背景
		style = styleDB.findStyle(Style.STYLE_TYPE_BG, 0);
		if(isNeedDownloadImage(style)){
			noDownloadUrlList.add(style);
		}
		//模块
		List<Menu> menuList = menuDB.findAllMenuList();
		for(Menu m : menuList){
			style = styleDB.findStyle(styleType(m.getType()), m.getMenuId());
			if(isNeedDownloadImage(style)){
				noDownloadUrlList.add(style);
			}
		}
		//删除SD卡下无用的图片
		removeUselessRes(context);
		return noDownloadUrlList;
	}
	
	private static boolean isNeedDownloadImage(Style style){
		if(style == null || TextUtils.isEmpty(style.getImgName())){
			return false;
		}
		File file = new File(Constants.COMPANY_STYLE_PATH+style.getImgName());
		if(file.exists()){
			return false;
		}else{
			return true;
		}
	}
	
	public static int styleType(int menuType){
		
		int type = 0;
		switch(menuType){
		
		case Menu.TYPE_NOTICE:
			type = Style.STYLE_TYPE_NOTICE;
			break;
		case Menu.TYPE_VISIT:
			type = Style.STYLE_TYPE_VISIT;
			break;
		case Menu.TYPE_NEW_TARGET:
		case Menu.TYPE_NEARBY:
		case Menu.TYPE_MODULE:
			type = Style.STYLE_TYPE_MODULE;
			break;
		case Menu.TYPE_ATTENDANCE:
			type = Style.STYLE_TYPE_ATTENDANCE;
			break;
		case Menu.TYPE_NEW_ATTENDANCE:
			type = Style.STYLE_TYPE_NEW_ATTENDANCE;
			break;
		case Menu.TYPE_REPORT:
			type = Style.STYLE_TYPE_REPORT;
			break;
		case Menu.TYPE_REPORT_NEW:
			type = Style.STYLE_TYPE_REPORT_NEW;
			break;
		case Menu.TYPE_WEB_REPORT:
			type = Style.STYLE_TYPE_WEB_REPORT;
			break;
		case Menu.TYPE_HELP:
			type = Style.STYLE_TYPE_HELP;
			break;
		case Menu.TYPE_ORDER2:
			type = Style.STYLE_TYPE_ORDER2;
			break;
		case Menu.TYPE_ORDER3:
			type = Style.STYLE_TYPE_ORDER3;
			break;
		case Menu.TYPE_ORDER3_SEND:
			type = Style.STYLE_TYPE_ORDER3_SEND;
			break;
		case Menu.TYPE_MANAGER:
			type = Style.STYLE_TYPE_NO_SUBMIT_MANAGER;
			break;
		case Menu.TYPE_TODO:
			type = Style.STYLE_TYPE_TODO;
			break;
		case Menu.IS_STORE_ADD_MOD://新店上报
			type = Style.STYLE_TYPE_NEW_STORE_REPORT;
			break;
		case Menu.TYPE_CAR_SALES://车销
			type = Style.STYLE_TYPE_CAR_SALE;
			break;
		case Menu.WEI_CHAT://企业微信
			type = Style.STYLE_TYPE_COMPANY_WECHAT;
			break;
		case Menu.QUESTIONNAIRE://调查问卷
			type = Style.STYLE_TYPE_RESEARCH_QUESTION;
			break;
		case Menu.WORK_PLAN://工作计划
			type = Style.STYLE_TYPE_WORK_PLAN;
			break;
		case Menu.WORK_SUM://工作总结
			type = Style.STYLE_TYPE_WORK_SUMMARY;
			break;
		case Menu.MAIN_LIST://通讯录
			type = Style.STYLE_TYPE_ADDRESS_BOOK;
			break;
			
		}
		return type;
	}
	
	private static void removeUselessRes(Context context){
		List<File> fileList = FileHelper.readAllFilesInDir(new File(Constants.COMPANY_STYLE_PATH));
		StyleDB styleDB = new StyleDB(context);
		for(File f : fileList){
			if(f.isFile() && styleDB.findStyleByImageName(f.getName()).isEmpty()){
				f.delete();
			}
		}
	}
	
	/**
	 * 背景图片
	 * @param context
	 * @return
	 */
	public static String homeStyleImg(Context context){
		Style style = new StyleDB(context).findBGStyle();
		if (style!=null && !TextUtils.isEmpty(style.getImgName())) {
			File file = new File(Constants.COMPANY_STYLE_PATH+style.getImgName());
			if (file.exists()) {
				return style.getImgName();
			}
		}
		return null;
	}
}
