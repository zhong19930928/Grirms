package com.yunhu.yhshxc.utility;

import com.yunhu.yhshxc.R;

/**
 * 用于标识App版本的枚举常量，包括普通版本、4.5版本、和伊利定制版。
 * 这里还封装了Splash屏需要用到的一些字符串常量和默认Logo
 * 
 * @version: 2013.5.20
 * @author wangchao
 *
 */
public enum Version {
	VER_NORAML {
		@Override
		public String getTitle1() {
			return "GRIRMS4";
		}

		@Override
		public String getTitle2() {
			return " 外 勤 ";
		}

		@Override
		public String getTitle3() {
			return "云 韬 略，更 长 远";
		}

		@Override
		public int getLogoResId() {
			return R.drawable.icon_main;
		}
	},
	
	VER_45 {
		@Override
		public String getTitle1() {
			return "GRIRMS4";
		}

		@Override
		public String getTitle2() {
			return " 外 勤 ";
		}

		@Override
		public String getTitle3() {
			return "云 韬 略，更 长 远";
		}

		@Override
		public int getLogoResId() {
			return R.drawable.icon_main;
		}
	},
	
	VER_YILI {
		@Override
		public String getTitle1() {
			return "";
		}

		@Override
		public String getTitle2() {
			return "伊 利 集 团";
		}

		@Override
		public String getTitle3() {
			return "销售终端管理系统";
		}

		@Override
		public int getLogoResId() {
			return R.drawable.login_logo_yili;
		}
	};
	
	/**
	 * @return Splash屏三行文字中第一行的默认显示文字
	 */
	abstract public String getTitle1();
	
	/**
	 * @return Splash屏三行文字中第二行的默认显示文字
	 */
	abstract public String getTitle2();
	
	/**
	 * @return Splash屏三行文字中第三行的默认显示文字
	 */
	abstract public String getTitle3();
	
	/**
	 * @return Splash屏默认的Logo
	 */
	abstract public int getLogoResId();
}