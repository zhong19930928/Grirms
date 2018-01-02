package com.yunhu.yhshxc.parser;

import android.content.Context;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.database.MainMenuDB;
import com.yunhu.yhshxc.order3.bo.Order3Contact;
import com.yunhu.yhshxc.order3.bo.Order3Dis;
import com.yunhu.yhshxc.order3.bo.Order3ProductConf;
import com.yunhu.yhshxc.order3.bo.Order3Promotion;
import com.yunhu.yhshxc.order3.db.Order3DisDB;
import com.yunhu.yhshxc.order3.db.Order3ProductConfDB;
import com.yunhu.yhshxc.order3.db.Order3PromotionDB;
import com.yunhu.yhshxc.order3.util.Order3Util;
import com.yunhu.yhshxc.order3.util.SharedPreferencesForOrder3Util;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.PublicUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Order3Parse {
	private final String THREE_ORDER = "three_order";
	private final String IS_DATE_TYPE = "is_date_type";//下订单可用日期（1.固定时间(周一 ~ 周日 可选) 2.自定义 默认是1）
	private final String PRODUCT_CTRL = "product_ctrl";
	private final String TAB = "tab";
	private final String CTRL = "ctrl";
	private final String NAME = "name";
	private final String COLS = "cols";
	private final String NEXT = "next";
	private final String CODE_CTRL = "code_ctrl";//二维码控件ID
	private final String UNIT_CTRL = "unit_ctrl";//计量单位控件ID
	private final String PRICE_CTRL = "price_ctrl";//价格控件ID
	private final String IS_PHONE_PRICE = "is_phone_price";//手机端价格的显示控制(1:不显示,2:不可修改,3:任意修改,4:只能高,5:只能低 默认是 3)
	private final String IS_STOCKS = "is_stocks";//下单时库存参考 （1:不需要,2:需要 默认 1）
	private final String IS_PHONE_PRINT = "is_phone_print";//手机端是否要打印  1:不要 2：要 默认
	private final String IS_ORDER_USER = "is_order_user";//是否需要订货联系人 1不要 2要 默认是2
	private final String IS_AMOUNT = "is_amount";//配送单上是否显示余额 1不要 2要 默认是2
	private final String IS_AUDIT_UPDATE = "is_audit_update";//审批后是否可修改 1不可 2 可 默认是2
	private final String IS_PROMOTION = "is_promotion";//是否需要促销模块 1不要 2要 默认2
	private final String IS_DIST_SALES = "is_dist_sales";//是否需要分销管理 1不要 2要 默认2
	private final String THREE_COUNT = "three_count";//当天提交的订单个数
	private final String MAX_NUM = "max_num";//下单数量的最大值
	private final String MIN_NUM = "min_num";//下单数量的最小值
	private final String BASE_FUNC = "basefunc";
	public final String DICT = "dict";
	public final String LINK_MOD = "link_mod";//拓展模块数据Id
	public final String LINK_NAME = "link_name";//拓展模块名称
	private final String ENABLE_TIME = "unable_time"; //下订单可用时间, 

	public final String THREE_PROMOTION =  "three_promotion";
	public final String ORDERDIS =  "orderDis";
	
	private final String UNABLE_WEEK = "unable_week";//每周可用 1可用0不可用
	private final String UNABLE_DATE = "unable_date";//每月可用日期
	
	private Context context;
	private CacheData cacheData = null;
	private Order3Util order3Util;
	public Order3Parse(Context mContext) {
		this.context = mContext;
		cacheData = new CacheData(mContext);
		order3Util = new Order3Util(mContext);
	}
	
	public void parserAll(JSONObject obj) throws JSONException{
		if (PublicUtils.isValid(obj, THREE_ORDER)) {
			JSONObject threeObj = obj.getJSONObject(THREE_ORDER);
			parserOrder3(threeObj);
		}
		if (PublicUtils.isValid(obj, THREE_PROMOTION)) {
			JSONArray promotionArray = obj.getJSONArray(THREE_PROMOTION);
			parserProductPromotion(promotionArray);
		}
		if (PublicUtils.isValid(obj, ORDERDIS)) {
			JSONArray disArray = obj.getJSONArray(ORDERDIS);
			parserOrder3Dis(disArray);
		}
		
		if (PublicUtils.isValid(obj, BASE_FUNC)) {
			String jsonForBasefunc = obj.getString(BASE_FUNC);
			cacheData.parseBaseFunc(jsonForBasefunc);
		}
		if (PublicUtils.isValid(obj, DICT)) { // 要在解析控件之后
			String jsonForDict = obj.getString(DICT);
			cacheData.parseDictionary(jsonForDict, true);
		}
	}
	
	public void parserOrder3(JSONObject obj) throws JSONException{
		if (obj!=null) {
			if (PublicUtils.isValid(obj, IS_DATE_TYPE)) {
				String dateType = obj.getString(IS_DATE_TYPE);
				SharedPreferencesForOrder3Util.getInstance(context).setIsDateType(Integer.parseInt(dateType));
			}
			if (PublicUtils.isValid(obj, CODE_CTRL)) {
				String codeCtrl = obj.getString(CODE_CTRL);
				SharedPreferencesForOrder3Util.getInstance(context).setCodeCtrl(codeCtrl);
			}else{
				SharedPreferencesForOrder3Util.getInstance(context).setCodeCtrl("");
			}
			if (PublicUtils.isValid(obj, PRICE_CTRL)) {
				String priceCtrl = obj.getString(PRICE_CTRL);
				SharedPreferencesForOrder3Util.getInstance(context).setPriceCtrl(priceCtrl);
			}else{
				SharedPreferencesForOrder3Util.getInstance(context).setPriceCtrl("");
			}
			if (PublicUtils.isValid(obj, IS_PHONE_PRICE)) {
				String value = obj.getString(IS_PHONE_PRICE);
				SharedPreferencesForOrder3Util.getInstance(context).setIsPhonePrice(Integer.parseInt(value));
			}
			if (PublicUtils.isValid(obj, IS_STOCKS)) {
				String value = obj.getString(IS_STOCKS);
				SharedPreferencesForOrder3Util.getInstance(context).setIsStocks(Integer.parseInt(value));
			}
			if (PublicUtils.isValid(obj, IS_PHONE_PRINT)) {
				String value = obj.getString(IS_PHONE_PRINT);
				SharedPreferencesForOrder3Util.getInstance(context).setIsPhonePrint(Integer.parseInt(value));
			}
			if (PublicUtils.isValid(obj, IS_ORDER_USER)) {
				String value = obj.getString(IS_ORDER_USER);
				SharedPreferencesForOrder3Util.getInstance(context).setIsOrderUser(Integer.parseInt(value));
			}
			if (PublicUtils.isValid(obj, IS_AMOUNT)) {
				String value = obj.getString(IS_AMOUNT);
				SharedPreferencesForOrder3Util.getInstance(context).setIsAmount(Integer.parseInt(value));
			}
			if (PublicUtils.isValid(obj, IS_AUDIT_UPDATE)) {
				String value = obj.getString(IS_AUDIT_UPDATE);
				SharedPreferencesForOrder3Util.getInstance(context).setIsAuditUpdate(Integer.parseInt(value));
			}
			if (PublicUtils.isValid(obj, IS_PROMOTION)) {
				String value = obj.getString(IS_PROMOTION);
				SharedPreferencesForOrder3Util.getInstance(context).setIsPromotion(Integer.parseInt(value));
			}
			if (PublicUtils.isValid(obj, IS_DIST_SALES)) {
				String value = obj.getString(IS_DIST_SALES);
				SharedPreferencesForOrder3Util.getInstance(context).setIsdistSales(Integer.parseInt(value));
			}
			
			if (PublicUtils.isValid(obj, MIN_NUM)) {
				SharedPreferencesForOrder3Util.getInstance(context).setMinNum(String.valueOf(obj.getInt(MIN_NUM)));
			}else{
				SharedPreferencesForOrder3Util.getInstance(context).setMinNum("0");
			}
			if (PublicUtils.isValid(obj, MAX_NUM)) {
				SharedPreferencesForOrder3Util.getInstance(context).setMaxNum(String.valueOf(obj.getInt(MAX_NUM)));
			}else{
				SharedPreferencesForOrder3Util.getInstance(context).setMaxNum("0");
			}
			
			if (PublicUtils.isValid(obj, LINK_MOD)) {
				SharedPreferencesForOrder3Util.getInstance(context).setLinkMod(obj.getInt(LINK_MOD));
			}else{
				SharedPreferencesForOrder3Util.getInstance(context).setLinkMod(0);
			}
			
			if (PublicUtils.isValid(obj, LINK_NAME)) {
				SharedPreferencesForOrder3Util.getInstance(context).setLinkName(obj.getString(LINK_NAME));
			}else{
				SharedPreferencesForOrder3Util.getInstance(context).setLinkName(context.getResources().getString(R.string.parser_cache_07));
			}
			
			if (PublicUtils.isValid(obj, ENABLE_TIME)) {
				SharedPreferencesForOrder3Util.getInstance(context).setEnableTime(obj.getString(ENABLE_TIME));
			}else{
				SharedPreferencesForOrder3Util.getInstance(context).setEnableTime("");
			}
			
			if (PublicUtils.isValid(obj, UNABLE_WEEK)) {
				SharedPreferencesForOrder3Util.getInstance(context).setUnableWeek(obj.getString(UNABLE_WEEK));
			}else{
				SharedPreferencesForOrder3Util.getInstance(context).setUnableWeek("");
			}
			
			if (PublicUtils.isValid(obj, UNABLE_DATE)) {
				SharedPreferencesForOrder3Util.getInstance(context).setUnablDate(obj.getString(UNABLE_DATE));
			}else{
				SharedPreferencesForOrder3Util.getInstance(context).setUnablDate("");
			}
			
			if (PublicUtils.isValid(obj, "is_input")) {
				SharedPreferencesForOrder3Util.getInstance(context).setIsInput(obj.getString("is_input"));
			}else{
				SharedPreferencesForOrder3Util.getInstance(context).setIsInput("0");
			}
			
			Order3ProductConfDB confDB = new Order3ProductConfDB(context);
			confDB.delete();
			if (PublicUtils.isValid(obj, PRODUCT_CTRL)) {
				JSONArray array = obj.getJSONArray(PRODUCT_CTRL);
				for (int i = 0; i < array.length(); i++) {
					JSONObject cObj = array.getJSONObject(i);
					Order3ProductConf conf = parserProductConf(cObj,Order3ProductConf.TYPE_PRODUCT);
					confDB.insert(conf);
				}
			}
			if (PublicUtils.isValid(obj, UNIT_CTRL)) {
				JSONObject cObj = obj.getJSONObject(UNIT_CTRL);
				Order3ProductConf conf = parserProductConf(cObj,Order3ProductConf.TYPE_UNIT);
				confDB.insert(conf);
			}
			if (PublicUtils.isValid(obj,THREE_COUNT)) {
				int count = obj.getInt(THREE_COUNT);
//				SharedPreferencesForOrder3Util.getInstance(context).setOrder3Count(DateUtil.getCurDate()+"_"+count);
				Map<String,String> map = new HashMap<String,String>();
				map.put(DateUtil.getCurDate(), String.valueOf(count));
				order3Util.saveOrder3NumberCount(map);
			}
			
			MainMenuDB db = new MainMenuDB(context);
			Menu menu = db.findMenuListByMenuType(Menu.TYPE_ORDER3);
			if (menu!= null) {
				menu.setPhoneUsableTime(SharedPreferencesForOrder3Util.getInstance(context).getEnableTime());
				db.updateMenuById(menu);
			}
		}
	
	}
	
	private Order3ProductConf parserProductConf(JSONObject cObj,int type) throws JSONException{
		Order3ProductConf conf = new Order3ProductConf();
		if (PublicUtils.isValid(cObj, TAB)) {
			String tab = cObj.getString(TAB);
			conf.setDictTable(tab);
		}
		if (PublicUtils.isValid(cObj, CTRL)) {
			String ctrl = cObj.getString(CTRL);
			conf.setDictDataId(ctrl);
		}
		if (PublicUtils.isValid(cObj, NAME)) {
			String name = cObj.getString(NAME);
			conf.setName(name);
		}
		if (PublicUtils.isValid(cObj, COLS)) {
			String cols = cObj.getString(COLS);
			conf.setDictCols(cols);
		}
		if (PublicUtils.isValid(cObj, NEXT)) {
			String next = cObj.getString(NEXT);
			conf.setNext(next);
		}
		conf.setType(type);		
		return conf;
	}
	
	public void parserProductPromotion(JSONArray array) throws JSONException{
		Order3PromotionDB db = new Order3PromotionDB(context);
		db.delete();
		if (array!=null) {
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				Order3Promotion conf = new Order3Promotion();
				if (PublicUtils.isValid(obj, "id")) {
					conf.setPromotionId(obj.getInt("id"));
				}
				if (PublicUtils.isValid(obj, "name")) {
					conf.setName(obj.getString("name"));
				}
				if (PublicUtils.isValid(obj, "m_cnt")) {
					conf.setmCnt(Double.parseDouble(obj.getString("m_cnt")));
				}
				if (PublicUtils.isValid(obj, "s_cnt")) {
					conf.setsCnt(Double.parseDouble(obj.getString("s_cnt")));
				}
				if (PublicUtils.isValid(obj, "amount")) {
					conf.setAmount(Double.parseDouble(obj.getString("amount")));
				}
				if (PublicUtils.isValid(obj, "pre_type")) {
					conf.setPreType(Integer.parseInt(obj.getString("pre_type")));
				}
				if (PublicUtils.isValid(obj, "dis_type")) {
					conf.setDisType(Integer.parseInt(obj.getString("dis_type")));
				}
				if (PublicUtils.isValid(obj, "is_double")) {
					conf.setIsDouble(Integer.parseInt(obj.getString("is_double")));
				}
				if (PublicUtils.isValid(obj, "f_time")) {
					conf.setfTime(obj.getString("f_time"));
				}
				if (PublicUtils.isValid(obj, "t_time")) {
					conf.settTime(obj.getString("t_time"));
				}
				if (PublicUtils.isValid(obj, "dis_rate")) {
					conf.setDisRate(obj.getString("dis_rate"));
				}
				if (PublicUtils.isValid(obj, "dis_amount")) {
					conf.setDisAmount(Double.parseDouble(obj.getString("dis_amount")));
				}
				if (PublicUtils.isValid(obj, "level")) {
					conf.setLevel(obj.getString("level"));
				}
				if (PublicUtils.isValid(obj, "mark")) {
					conf.setMark(obj.getString("mark"));
				}
				if (PublicUtils.isValid(obj, "org_id")) {
					conf.setOrgId(String.valueOf(obj.getInt("org_id")));
				}
				if (PublicUtils.isValid(obj, "s_tab")) {
					conf.setsTab(obj.getString("s_tab"));
				}
				if (PublicUtils.isValid(obj, "s_id")) {
					conf.setsId(obj.getString("s_id"));
				}
				if (PublicUtils.isValid(obj, "s_uid")) {
					conf.setsUid(obj.getString("s_uid"));
				}
				if (PublicUtils.isValid(obj, "m_tab")) {
					conf.setmTab(obj.getString("m_tab"));
				}
				if (PublicUtils.isValid(obj, "m_id")) {
					conf.setmId(obj.getString("m_id"));
				}
				if (PublicUtils.isValid(obj, "m_uid")) {
					conf.setmUid(obj.getString("m_uid"));
				}
				db.insert(conf);
			}
		}
			
//			if (PublicUtils.isValid(obj, "m_tab")) {//主产品表名
//				String sTab = obj.getString("m_tab");
//				conf.setsTab(sTab);
//				Order3ProductConf lastConf = confDB.findLastProductConf();
//				if (sTab.equals(lastConf.getDictTable())) {//如果主产品的表名和最后一级产品配置表名一样，说明不是分类 即主产品就是单品
//					if (PublicUtils.isValid(obj, "m_id")) {
//						conf.setsId(obj.getString("m_id"));
//					}
//					if (PublicUtils.isValid(obj, "s_uid")) {
//						conf.setsUid(obj.getString("s_uid"));
//					}
//					db.insert(conf);
//				}else{//说明主产品促销是选择了分类产品那么该分类下的所有产品都享有该促销
//					Order3ProductConf cConf = confDB.findProductConfByTableName(sTab);//该分类下的所有产品都享有促销
//					if (cConf!=null) {
//						if (PublicUtils.isValid(obj, "m_id")) {
//							String sId = obj.getString("m_id");
//							List<Order3ProductCtrl> ctrlList = ctrlDB.findAllLastProductCtrlByCconfDid(Integer.parseInt(sId));
//							for (int j = 0; j < ctrlList.size(); j++) {
//								Order3ProductCtrl ctrl = ctrlList.get(j);
//								int productId = ctrl.getProductId();
//								int unitId = ctrl.getUnitId();
//								conf.setsId(String.valueOf(productId));
//								conf.setsUid(String.valueOf(unitId));
//								db.insert(conf);
//							}
//						}
//					}
//				}
//			}else{
//				db.insert(conf);
//			}
	}
	
	/**
	 * 解析联系人
	 * @param userObj
	 * @return
	 * @throws JSONException
	 */
	public Order3Contact parserContact(JSONObject userObj) throws JSONException{
		Order3Contact orderContacts = new Order3Contact();
		if (userObj.has("id")) {
			orderContacts.setOrderContactsId(userObj.getInt("id"));
		}
		if (userObj.has("user_address")) {
			orderContacts.setUserAddress(userObj.getString("user_address"));
		}
		if (userObj.has("user_name")) {
			orderContacts.setUserName(userObj.getString("user_name"));
		}
		if (userObj.has("user_phone1")) {
			orderContacts.setUserPhone1(userObj.getString("user_phone1"));
		}
		if (userObj.has("user_phone2")) {
			orderContacts.setUserPhone2(userObj.getString("user_phone2"));
		}
		if (userObj.has("user_phone3")) {
			orderContacts.setUserPhone3(userObj.getString("user_phone3"));
		}
		if (userObj.has("store_id")) {
			orderContacts.setStoreId(userObj.getString("store_id"));
		}
		return orderContacts;
	}
	
	/**
	 * 解析分销
	 * @param obj
	 * @throws JSONException
	 */
	public void parserOrder3Dis(JSONArray array) throws JSONException{
		Order3DisDB db = new Order3DisDB(context);
		db.clearOrder3Dis();
		if (array!=null) {
			for (int i = 0; i < array.length(); i++) {
				JSONObject disObj = array.getJSONObject(i);
				Order3Dis dis = new Order3Dis();
				if (PublicUtils.isValid(disObj, "id")) {
					dis.setDisId(disObj.getInt("id"));
				}
				if (PublicUtils.isValid(disObj, "product_id")) {
					dis.setProductId(disObj.getInt("product_id"));
				}
				if (PublicUtils.isValid(disObj, "org_id")) {
					dis.setOrgId(disObj.getInt("org_id"));
				}
				if (PublicUtils.isValid(disObj, "org_code")) {
					dis.setOrgCode(disObj.getString("org_code"));
				}
				if (PublicUtils.isValid(disObj, "org_level")) {
					dis.setOrgLevel(disObj.getInt("org_level"));
				}
				db.insertOrderContants(dis);
			}
		}
	}
	
}
