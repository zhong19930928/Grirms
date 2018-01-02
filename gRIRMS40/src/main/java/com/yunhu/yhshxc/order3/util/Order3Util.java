package com.yunhu.yhshxc.order3.util;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.Org;
import com.yunhu.yhshxc.bo.OrgStore;
import com.yunhu.yhshxc.bo.OrgUser;
import com.yunhu.yhshxc.database.DatabaseHelper;
import com.yunhu.yhshxc.database.DictDB;
import com.yunhu.yhshxc.database.FuncDB;
import com.yunhu.yhshxc.database.OrgDB;
import com.yunhu.yhshxc.database.OrgStoreDB;
import com.yunhu.yhshxc.database.OrgUserDB;
import com.yunhu.yhshxc.order3.bo.Order3;
import com.yunhu.yhshxc.order3.bo.Order3Contact;
import com.yunhu.yhshxc.order3.bo.Order3Product;
import com.yunhu.yhshxc.order3.bo.Order3ProductConf;
import com.yunhu.yhshxc.order3.bo.Order3ProductCtrl;
import com.yunhu.yhshxc.order3.bo.Order3ProductData;
import com.yunhu.yhshxc.order3.bo.Order3ProductUnti;
import com.yunhu.yhshxc.order3.db.Order3ContactsDB;
import com.yunhu.yhshxc.order3.db.Order3DB;
import com.yunhu.yhshxc.order3.db.Order3ProductConfDB;
import com.yunhu.yhshxc.order3.db.Order3ProductCtrlDB;
import com.yunhu.yhshxc.order3.db.Order3ProductDataDB;
import com.yunhu.yhshxc.order3.db.Order3PromotionDB;
import com.yunhu.yhshxc.parser.Order3Parse;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;

public class Order3Util {
	private Context context;
	private String  orgId;
	private Order3PromotionDB promotionDB;

	public Order3Util(Context mContext) {
		this.context = mContext;
		orgId = SharedPreferencesForOrder3Util.getInstance(mContext).getOrder3OrgId();
		promotionDB = new Order3PromotionDB(mContext);
	}

	public List<Order3ProductCtrl> allProductCtrlList(){
		return new Order3ProductCtrlDB(context).findAllProductCtrl();
	}
	
	public List<Order3ProductCtrl> commonProductCtrlList(){
		return new Order3ProductCtrlDB(context).findCommonProductCtrl();
	}
		
	List<Order3ProductCtrl> ctrlList;
	public void initOrder3ProductCtrl(){
		ctrlList = new ArrayList<Order3ProductCtrl>();
		Order3ProductCtrlDB ctrlDB = new Order3ProductCtrlDB(context);
		ctrlDB.delete();
		Order3ProductConfDB db = new Order3ProductConfDB(context);
		List<Order3ProductConf> confList = db.findListByType(Order3ProductConf.TYPE_PRODUCT);
		String queryWhere = null;
		Order3ProductConf conf = confList.get(0);//第一层
		List<Dictionary> list = new DictDB(context).findDictList(conf.getDictTable(), conf.getDictCols(), conf.getDictDataId(),null,null, queryWhere, null, null);
		if (list!=null && !list.isEmpty()) {
			for (int j = 0; j < list.size(); j++) {//字典表数据
				Dictionary dic = list.get(j);
				Order3ProductCtrl ctrl = new Order3ProductCtrl();
				ctrl.setLabel(dic.getCtrlCol());
//				ctrl.setcId(Integer.parseInt(dic.getDid()));
				ctrl.setcId(dic.getDid());
				ctrl.setpId("0");
				queryWhere = dic.getDid();
				if (!TextUtils.isEmpty(conf.getNext())) {
//					ctrlDB.insert(ctrl);
					ctrlList.add(ctrl);
					next(conf.getNext(), queryWhere, ctrl, ctrlDB);
				}else{
					List<Order3Product> productList = productUnitList(dic);
					for (int i = 0; i < productList.size(); i++) {
						Order3Product product = productList.get(i);
						Order3ProductCtrl cTrl = new Order3ProductCtrl();
						cTrl.setLabel(ctrl.getLabel());
						cTrl.setcId(ctrl.getcId());
						cTrl.setpId(ctrl.getpId());
						cTrl.setProductId(product.getProductId());
						cTrl.setUnitId(product.getUnitId());
//						ctrlDB.insert(cTrl);
						ctrlList.add(cTrl);

//						cTrl = productDisControl(cTrl);
//						if (cTrl!=null) {
//							ctrlDB.insert(cTrl);
//						}
					}
				}
			}
		}
		
		if (!ctrlList.isEmpty()) {
			DatabaseHelper.getInstance(context).beginTransaction();
			for (int i = 0; i < ctrlList.size(); i++) {
				Order3ProductCtrl ctrl = ctrlList.get(i);
				ctrlDB.insert(ctrl);
			}
			DatabaseHelper.getInstance(context).endTransaction();

		}
	}
	
//	private Order3ProductCtrl productDisControl(Order3ProductCtrl ctrl){
//		int is_dist_sales  = SharedPreferencesForOrder3Util.getInstance(context).getIsdistSales();//是否需要分销管理 1不要 2要 默认2
//		if (is_dist_sales == 2) {//需要分销管理的情况查看是否在销售区域内
//			Order3Dis dis = new Order3DisDB(context).findOrder3DisByProductId(ctrl.getProductId());
//			if (ctrl.getProductId() == 31 || ctrl.getProductId() == 32 || ctrl.getProductId() == 33) {
//				JLog.d("jishen", "aa");
//			}
//			String storeOrgCode = SharedPreferencesForOrder3Util.getInstance(context).getOrder3OrgId();
//			if (dis!=null && !storeOrgCode.contains(dis.getOrgCode())) {//选中的店面不包含分销机构code的话说明不在此区域销售
//				return null;
//			}
//		}
//		return ctrl;
//	}
	
	private void next(String next,String queryWhere,Order3ProductCtrl pCtrl,Order3ProductCtrlDB ctrlDB){
		Order3ProductConfDB db = new Order3ProductConfDB(context);
		Order3ProductConf nextConf = db.findProductConfByNext(next);
		List<Dictionary> nextList = new DictDB(context).findDictList(nextConf.getDictTable(), nextConf.getDictCols(), nextConf.getDictDataId(),null,null, queryWhere, null, null);
		if (nextList!=null && !nextList.isEmpty()) {
			for (int i = 0; i < nextList.size(); i++) {
				Dictionary dic = nextList.get(i);
				Order3ProductCtrl nCtrl = new Order3ProductCtrl();
				nCtrl.setLabel(dic.getCtrlCol());
				String str = String.valueOf(pCtrl.getcId())+dic.getDid();
//				nCtrl.setcId(Integer.parseInt(str));
				nCtrl.setcId(str);
				nCtrl.setpId(pCtrl.getcId());
				if (!TextUtils.isEmpty(nextConf.getNext())) {
					String where = queryWhere+"@"+dic.getDid();
//					ctrlDB.insert(nCtrl);
					ctrlList.add(nCtrl);
					next(nextConf.getNext(), where, nCtrl, ctrlDB);
				}else{
					List<Order3Product> productList = productUnitList(dic);
					for (int j = 0; j < productList.size(); j++) {
						Order3Product product = productList.get(j);
						Order3ProductCtrl cTrl = new Order3ProductCtrl();
						cTrl.setLabel(nCtrl.getLabel());
						cTrl.setcId(nCtrl.getcId());
						cTrl.setpId(nCtrl.getpId());
//						cTrl.setProduct(product);
						cTrl.setProductLevel(true);
						cTrl.setProductId(product.getProductId());
						cTrl.setUnitId(product.getUnitId());
//						ctrlDB.insert(cTrl);
						ctrlList.add(cTrl);
					}
				}
			}
		}
		
	}
	
	
	/**
	 * 产品
	 * @param dic 产品对应的字典表实例
	 * @return 同样的产品，不同的单位的产品集合
	 */
	private List<Order3Product> productUnitList(Dictionary dic){
		List<Order3Product> list = new ArrayList<Order3Product>();
		List<Order3ProductUnti> unitList = getOrder3ProductUnti(dic.getDid());
		if (!unitList.isEmpty()) {
			for (int i = 0; i < unitList.size(); i++) {
				Order3Product product = new Order3Product();
				product.setProductId(Integer.parseInt(dic.getDid()));
				product.setName(dic.getCtrlCol());
				product.setCode(productCode(dic.getDid()));
				Order3ProductUnti d = unitList.get(i);
				product.setUnit(d.getUnit());
				product.setUnitId(d.getUnitId());
				product.setPrice(d.getPrice());
				product.setPromotion(isPromotion(product.getProductId(), product.getUnitId()));
				list.add(product);
			}
		}else{
			Order3Product product = new Order3Product();
			product.setProductId(Integer.parseInt(dic.getDid()));
			product.setName(dic.getCtrlCol());
			product.setCode(productCode(dic.getDid()));
			product.setPrice(0);
			product.setUnit(" ");
			product.setUnitId(-1);
			product.setPromotion(isPromotion(product.getProductId(), product.getUnitId()));
			list.add(product);
		}
		
		return list;
	}
	
	/**
	 * 查询产品是否有促销信息
	 * @param pId
	 * @param uId
	 * @return
	 */
	private boolean isPromotion(int pId,int uId){
		int level = SharedPreferencesForOrder3Util.getInstance(context).getOrder3StoreLevel();
		boolean flag = promotionDB.isPromotion(pId, uId, orgId,level);
		return flag;
	}
	
	/**
	 * 一个产品上级所有层级对应的Dictionary
	 * @param productId
	 * @return
	 */
	private  List<Dictionary> getProductRelatedList(String productId) {
		DictDB dictDB = new DictDB(context);
		List<Order3ProductConf> productConfList = new Order3ProductConfDB(context).findListByType(Order3ProductConf.TYPE_PRODUCT);//所有层级
		int size = productConfList.size();//所有层级数
//		Order3ProductConf product = new Order3ProductConfDB(context).findLastProductConf();//最后层级
		Order3ProductConf product = productConfList.get(size-1);//最后层级
		String[] relatedColumnDid = dictDB.findRelatedColumnDid(product.getDictTable(),product.getDictDataId(),product.getDictCols(), productId);
		if(relatedColumnDid == null || relatedColumnDid.length != size){
			throw new NullPointerException("FUNC Exception");
		}
		List<Dictionary> list = new ArrayList<Dictionary>(size);
		Dictionary dict = null;
		for(int i= 0; i<size; i++){
			dict = dictDB.findDictByDid(productConfList.get(i).getDictTable(), productConfList.get(i).getDictDataId(), relatedColumnDid[i]);
			list.add(dict);
		}
		return list;
	}
	
	/**
	 * 计量单位
	 * @param productId 产品ID
	 * @return 该产品对应的所有计量单位
	 */
	public  List<Order3ProductUnti> getOrder3ProductUnti(String productId) {
		List<Dictionary> list = getProductRelatedList(productId);
		DictDB dictDB = new DictDB(context);
		Order3ProductConf conf = new Order3ProductConfDB(context).findUnitProductConf();
		String[] relatedCols = dictDB.findRelatedForeignkey(conf.getDictCols(), conf.getDictDataId());
		String[] values = new String[list.size()];
		int i = 0;
		for(Dictionary dict : list){
			values[i] = dict.getDid();
			i++;
		}
		String price_ctrl = SharedPreferencesForOrder3Util.getInstance(context).getPriceCtrl();
		String[] str = price_ctrl.split("\\|");
		String priceId = "data_"+str[1];
		List<Order3ProductUnti> dict = dictDB.findDictByRelatedColumn(conf.getDictTable(), conf.getDictDataId(),priceId,relatedCols, values);
		return dict;
	}
		
	/**
	 * 产品二维码
	 * @param productId
	 * @return
	 */
	public String productCode(String productId){
		String code = "";
		String code_ctrl = SharedPreferencesForOrder3Util.getInstance(context).getCodeCtrl();
		String[] str = code_ctrl.split("\\|");
		String table = "t_m_"+str[0];
		String dictDataId = "data_"+str[1];
		Dictionary codeDic = new DictDB(context).findDictByDid(table, dictDataId, productId);
		if (codeDic!=null) {
			code = codeDic.getCtrlCol();
		}
		return code;
	}
	
	/**
	 * 通过code找产品
	 * @param code
	 * @return
	 */
	public Order3Product productForCode(String code){
		Order3Product product = null;
		String code_ctrl = SharedPreferencesForOrder3Util.getInstance(context).getCodeCtrl();
		String[] str = code_ctrl.split("\\|");
		String codeTable = "t_m_"+str[0];
		String codeDictDataId = "data_"+str[1];
		Dictionary codeDic = new DictDB(context).findDictByDataValue(codeTable, codeDictDataId, code);
		if (codeDic!=null) {
			String productId = codeDic.getDid();
			List<Order3ProductUnti> untiList = getOrder3ProductUnti(productId);
			product = product(Integer.parseInt(productId),untiList.size()>0?untiList.get(0).getUnitId():-1);
		}
		return product;
	}
	
	
	/**
	 * 根据产品id
	 * @param productId 产品ID
	 * @param unitId 计量单位ID
	 * @return
	 */
	public Order3Product product(int productId,int unitId){
		Order3Product product = null;
		Order3ProductConf productConf = new Order3ProductConfDB(context).findLastProductConf();//最后层级
		if (productConf!=null) {
			List<Dictionary> list = new DictDB(context).findDictList(productConf.getDictTable(), productConf.getDictCols(), productConf.getDictDataId(),null,null, null, null, String.valueOf(productId));
			if (list!=null && list.size()>0) {
				Dictionary dic = list.get(0);
				List<Order3Product> productUnitList = productUnitList(dic);
				for (int i = 0; i < productUnitList.size(); i++) {
					Order3Product p = productUnitList.get(i);
					if (unitId == p.getUnitId()) {
						product = p;
						break;
					}
				}
			}
		}
		return product;
	}
	
//	/**
//	 * 订单编号
//	 * @param isCreate 是否是创建订单编号
//	 * @return
//	 */
//	public String orderNumber(boolean isCreate){
////		return String.valueOf(System.currentTimeMillis());
//		if (isCreate) {//创建订单号
//			StringBuffer number = new StringBuffer();
//			String d = DateUtil.getCurYearMonthDay();
//			String userId = String.valueOf(SharedPreferencesUtil.getInstance(context).getUserId()+10000000).substring(1);
//			number.append(d).append(userId);
//
//			String str = SharedPreferencesForOrder3Util.getInstance(context).getOrder3Count();
//			if (!TextUtils.isEmpty(str)) {
//				String[] s = str.split("_");
//				String date = s[0];
//				String num = s[1];
//				String today = DateUtil.getCurDate();
//				if (today.equals(date)) {
//					number.append(String.valueOf((Integer.parseInt(num)+1)+1000).substring(1));
//				}else{
//					number.append("001");
//				}
//			}else{
//				number.append("001");
//			}
//			return number.toString();
//		}else{//提交的时候维护订单号
//			String str = SharedPreferencesForOrder3Util.getInstance(context).getOrder3Count();
//			if (!TextUtils.isEmpty(str)) {
//				String[] s = str.split("_");
//				String date = s[0];
//				String num = s[1];
//				SharedPreferencesForOrder3Util.getInstance(context).setOrder3Count(date+"_"+(Integer.parseInt(num)+1));
//			}else{
//				String today = DateUtil.getCurDate();
//				SharedPreferencesForOrder3Util.getInstance(context).setOrder3Count(today+"_"+1);
//
//			}
//			return "";
//		}
//	}
	
	/***
	 * 订单号生成
	 * @param isCreate
	 * @return
	 */
	public String orderNumber(boolean isCreate){
//		return String.valueOf(System.currentTimeMillis());
		Map<String,String> map = getOrder3NumberCount();
		String count = map.get(DateUtil.getCurDate());
		if (isCreate) {//创建订单号
			StringBuffer number = new StringBuffer();
			String d = DateUtil.getCurYearMonthDay();
			String userId = String.valueOf(SharedPreferencesUtil.getInstance(context).getUserId()+10000000).substring(1);
			number.append(d).append(userId);
			if(!TextUtils.isEmpty(count)){
				number.append(String.valueOf((Integer.parseInt(count)+1)+1000).substring(1));
			}else{
				number.append("001");
			}
			return number.toString();
		}else{//提交的时候维护订单号
			if(!TextUtils.isEmpty(count)){
				map.put(DateUtil.getCurDate(), String.valueOf(Integer.parseInt(count)+1));
			}else{
				map.put(DateUtil.getCurDate(), "1");
			}
			saveOrder3NumberCount(map);
			return "";
		}
	}
	
	public Map<String, String> getOrder3NumberCount() {
		Map<String, String> map = new HashMap<String, String>();
		try {
			String ctrl = SharedPreferencesForOrder3Util.getInstance(context).getOrder3NumberCount();
			if (!TextUtils.isEmpty(ctrl)) {
				JSONObject obj = new JSONObject(ctrl);
				Iterator it = obj.keys();
				while (it.hasNext()) {
					String key = String.valueOf(it.next());
					String value = (String) obj.get(key);
					map.put(key, value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	public void saveOrder3NumberCount(Map<String, String> map) {
		if (map != null) {
			JSONObject obj = new JSONObject(map);
			SharedPreferencesForOrder3Util.getInstance(context)
					.setOrder3NumberCount(obj.toString());
		}
	}
	
	public String submitJson(List<Order3> orderList) throws JSONException{
		String json = "";
		JSONObject submitObj = new JSONObject();
		JSONArray orderArray = new JSONArray();
		for (int i = 0; i < orderList.size(); i++) {
			Order3 order = orderList.get(i);
			if (order!=null) {
				JSONObject obj = new JSONObject();
				String orderNo = order.getOrderNo();//订单编号
				obj.put("order_num", orderNo);
				obj.put("order_time", order.getOrderTime());//订单日期
				String timestamp = SharedPreferencesForOrder3Util.getInstance(context).getOrderTimestamp();
				if (!TextUtils.isEmpty(timestamp)) {
					obj.put("link_mod", timestamp);
				}
				JSONObject spec = new JSONObject();
				spec.put("pay_amount", order.getPayAmount());//预收款
				spec.put("order_discount", order.getOrderDiscount());//特别折扣
				spec.put("unpaid_amount", order.getUnPayAmount());//未支付
				String amount = String.valueOf(order.getOrderAmount());//订单总额
				spec.put("order_amount", amount);
				spec.put("actual_amount", order.getActualAmount());
				String note = order.getNote();//留言
				spec.put("order_remark", note);
				spec.put("is_promotion", order.getIsPromotion());//是否有促销
				spec.put("promotion_amount", order.getActualAmount()+order.getOrderDiscount());//促销金额  实际金额+特别减免
				spec.put("print_send_count", order.getSendPrintCount());//送货打印次数
				spec.put("print_count", order.getPrintCount());//下单打印次数
				spec.put("image1", order.getImage1());
				spec.put("image2", order.getImage2());
				spec.put("send_image1", order.getSendImage1());
				spec.put("send_image2", order.getSendImage2());
				

				Order3Contact order3Contact = order.getContact();
				if (order3Contact!=null) {//联系人
					JSONObject contact = new JSONObject();
					contact.put("id", order3Contact.getOrderContactsId());
					if(order3Contact.getUserName() != null){
						contact.put("user_name", order3Contact.getUserName());
					}
					if(order3Contact.getUserAddress() != null){
						contact.put("user_address", order3Contact.getUserAddress());
					}
					if(order3Contact.getUserPhone1() != null){
						contact.put("user_phone_1", order3Contact.getUserPhone1());
					}
					if(order3Contact.getUserPhone2() != null){
						contact.put("user_phone_2", order3Contact.getUserPhone2());
					}
					if(order3Contact.getUserPhone3() != null){
						contact.put("user_phone_3", order3Contact.getUserPhone3());
					}
					if(order3Contact.getStoreId() != null){
						contact.put("storeId", order3Contact.getStoreId());
					}
					spec.put("place_user", contact);
				}
				obj.put("spec", spec);
				
				List<Order3ProductData> dataList = order.getProductList();
				if (dataList!=null && !dataList.isEmpty()) {
					JSONArray array = new JSONArray();
					for (int j = 0; j < dataList.size(); j++) {
						Order3ProductData data = dataList.get(j);
						JSONObject dObj = new JSONObject();
						dObj.put("product_id", data.getProductId());
						dObj.put("order_count", data.getOrderCount());
						dObj.put("actual_count", data.getActualCount());
						dObj.put("product_standard", data.getProductUnit());
						dObj.put("product_standard_id", data.getUnitId());
						dObj.put("order_price", data.getOrderPrice());
						dObj.put("order_amount", data.getOrderAmount());
						dObj.put("actual_amount", data.getActualAmount());
						dObj.put("promotion_id", data.getPromotionId());
						dObj.put("main_product_id", data.getMainProductId());
						dObj.put("order_remark", data.getOrderRemark());
						dObj.put("is_order_main", data.getIsOrderMain());
						dObj.put("id", data.getDataId());
						dObj.put("send_count", data.getCurrentSendCount());
						array.put(dObj);
					}
					obj.put("detail", array);
				}
				orderArray.put(obj);
			}
		}
		submitObj.put("data", orderArray);
		json = submitObj.toString();
		return json;
	}
	
	public List<Order3> parserOrder3(String json,boolean clear) throws JSONException{
		List<Order3> list = new ArrayList<Order3>();
		if (!TextUtils.isEmpty(json)) {
			JSONObject obj = new JSONObject(json);
			if (PublicUtils.isValid(obj, "orderData")) {
				JSONArray array = obj.getJSONArray("orderData");
				Order3DB order3DB = new Order3DB(context);
				Order3ProductDataDB dataDB = new Order3ProductDataDB(context);
				if (clear) {
					order3DB.clearOrder();
					dataDB.clearOrder3ProductData();
				}
				for (int i = 0; i < array.length(); i++) {
					JSONObject orderObj = array.getJSONObject(i);
					Order3 order = new Order3();
					if (PublicUtils.isValid(orderObj, "id")) {
						order.setOrderId(orderObj.getInt("id"));
					}
					if (PublicUtils.isValid(orderObj, "store_id")) {
						order.setStoreId(String.valueOf(orderObj.getInt("store_id")));
					}
					if (PublicUtils.isValid(orderObj, "store_name")) {
						order.setStoreName(orderObj.getString("store_name"));
					}
					if (PublicUtils.isValid(orderObj, "order_time")) {
						order.setOrderTime(orderObj.getString("order_time"));
					}
					if (PublicUtils.isValid(orderObj, "order_num")) {
						order.setOrderNo(orderObj.getString("order_num"));
					}
					if (PublicUtils.isValid(orderObj, "audit_status")) {
						order.setOrderState(orderObj.getString("audit_status"));
					}
					if (PublicUtils.isValid(orderObj, "spec")) {
						JSONObject specObj = orderObj.getJSONObject("spec");
						if (PublicUtils.isValid(specObj, "pay_amount")) {
							order.setPayAmount(Double.parseDouble(specObj.getString("pay_amount")));
						}
						if (PublicUtils.isValid(specObj, "order_amount")) {
							order.setOrderAmount(Double.parseDouble(specObj.getString("order_amount")));
						}
						if (PublicUtils.isValid(specObj, "actual_amount")) {
							order.setActualAmount(Double.parseDouble(specObj.getString("actual_amount")));
							if (order.getActualAmount()==0) {
								order.setActualAmount(order.getOrderAmount());
							}
						}
						if (PublicUtils.isValid(specObj, "order_discount")) {
							order.setOrderDiscount(Double.parseDouble(specObj.getString("order_discount")));
						}
						if (PublicUtils.isValid(specObj, "order_remark")) {
							order.setNote(specObj.getString("order_remark"));
						}
						if (PublicUtils.isValid(specObj, "place_user")) {
							JSONObject userObj = specObj.getJSONObject("place_user");
							Order3Contact contact = new Order3Parse(context).parserContact(userObj);
							if (contact!=null) {
								Order3ContactsDB db = new Order3ContactsDB(context);
								Order3Contact c = db.findOrderContactsByContactsIdAndStoreId(contact.getOrderContactsId(), contact.getStoreId());
								if (c == null) {
									db.insertOrderContants(contact);
								}
								order.setContactId(contact.getOrderContactsId());
							}
						}
					}
					if (PublicUtils.isValid(orderObj, "detail")) {
						JSONArray dArray = orderObj.getJSONArray("detail");
						List<Order3ProductData> dataList = new ArrayList<Order3ProductData>();
						for (int j = 0; j < dArray.length(); j++) {
							JSONObject dObj = dArray.getJSONObject(j);
							Order3ProductData data = parserOrder3ProductData(dObj);
							if (data == null) {
								continue;
							}
							data.setOrderNo(order.getOrderNo());
							dataList.add(data);
							dataDB.insertOrder(data);
						}
						order.setProductList(dataList);
					}
					list.add(order);
					order3DB.insertOrder(order);
				}
			}
		}
		return list;
	}
	
	private Order3ProductData parserOrder3ProductData(JSONObject obj) throws JSONException{
		Order3ProductData data = new Order3ProductData();
		if (PublicUtils.isValid(obj, "id")) {
			data.setDataId(obj.getInt("id"));
		}
		if (PublicUtils.isValid(obj, "product_id")) {
			data.setProductId(obj.getInt("product_id"));
		}
		if (PublicUtils.isValid(obj, "order_count")) {
			data.setOrderCount(Double.parseDouble(obj.getString("order_count")));
		}
		if (PublicUtils.isValid(obj, "actual_count")) {
			data.setActualCount(Double.parseDouble(obj.getString("actual_count")));
		}
		if (PublicUtils.isValid(obj, "product_standard")) {
			data.setProductUnit(obj.getString("product_standard"));
		}
		if (PublicUtils.isValid(obj, "product_standard_id")) {
			data.setUnitId(obj.getInt("product_standard_id"));
		}
		if (PublicUtils.isValid(obj, "order_price")) {
			data.setOrderPrice(Double.parseDouble(obj.getString("order_price")));
		}
		if (PublicUtils.isValid(obj, "order_amount")) {
			data.setOrderAmount(Double.parseDouble(obj.getString("order_amount")));
		}
		if (PublicUtils.isValid(obj, "actual_amount")) {
			data.setActualAmount(Double.parseDouble(obj.getString("actual_amount")));
		}
		if (PublicUtils.isValid(obj, "order_status")) {
			data.setStatus(obj.getString("order_status"));
		}
		if (PublicUtils.isValid(obj, "is_order_main")) {
			data.setIsOrderMain(Integer.parseInt(obj.getString("is_order_main")));
		}
		if (PublicUtils.isValid(obj, "promotion_id")) {
			data.setPromotionId(Integer.parseInt(obj.getString("promotion_id")));
		}
		if (PublicUtils.isValid(obj, "main_product_id")) {
			data.setMainProductId(Integer.parseInt(obj.getString("main_product_id")));
		}
		if (PublicUtils.isValid(obj, "order_remark")) {
			data.setOrderRemark(obj.getString("order_remark"));
		}
		if (PublicUtils.isValid(obj, "send_count")) {
			String s  =obj.getString("send_count");
			data.setSendCount(TextUtils.isEmpty(s)?0:Double.parseDouble(s));
		}
		Order3Product product = product(data.getProductId(), data.getUnitId());
		if (product!=null) {
			data.setProductName(product.getName());
		}else{
			data.setProductName(String.valueOf(data.getProductId()));
		}
		
		return data;
	}
	
	/**
	 * 产品属性
	 * @param productId 产品ID
	 * @param unitId 单位ID
	 */
	public Map<String, String> productInfo(int productId){
		Map<String, String> map = new HashMap<String, String>();
		Order3ProductConf conf = new Order3ProductConfDB(context).findLastProductConf();//最后层级
		String tab = conf.getDictTable();
		String ctrl =  conf.getDictDataId();
		String cols = conf.getDictCols();
		String funcInfo = cols.split(ctrl)[1];
		List<Func> funcList = new FuncDB(context).findFuncByFuncIds(funcInfo.replace("data_", "").substring(1));
		DictDB dicDB = new DictDB(context);
		for (int i = 0; i < funcList.size(); i++) {
			Func func = funcList.get(i);
			String name = func.getName();
			int funcId = func.getFuncId();
			int type = func.getType();
			Dictionary dic = dicDB.findDictByDid(tab, "data_"+funcId, String.valueOf(productId));
			if (dic!=null) {
				String value = dic.getCtrlCol();
				if (type== Func.TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY_COMP || type == Func.TYPE_SELECTCOMP
					|| type == Func.TYPE_MULTI_CHOICE_FUZZY_QUERY_COMP || type == Func.TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY_COMP
					|| type == Func.TYPE_SELECT_OTHER || type == Func.TYPE_MULTI_CHOICE_SPINNER_COMP) {//其他类型的下拉框
					if (func.getOrgOption() != null && func.getOrgOption() != Func.OPTION_LOCATION) {// 表格里如果有店或者user
						if (!TextUtils.isEmpty(value)) {
							if (func.getOrgOption() == Func.ORG_STORE) {// 店
								value = getOrgStore(value);
							} else if (func.getOrgOption() == Func.ORG_USER) {// user
								value = getOrgUser(value);
							} else if(func.getOrgOption() == Func.ORG_OPTION){
								value = getOrg(value);
							}
						} 
					} else {
						if (!TextUtils.isEmpty(value)) {//空值
							value = dicDB.findDictMultiChoiceValueStr(value, func.getDictDataId(),func.getDictTable());
						}
					}
				}
				map.put(name, value);
			}
		}
		return map;
	}
	
	/**
	 * 返回机构数据
	 * @param value 机构的did
	 * @return 机构的名称
	 */
	private String getOrg(String value) {
		List<Org> orgList = new OrgDB(context).findOrgByOrgId(value);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < orgList.size(); i++) {
			Org org = orgList.get(i);
			sb.append(",").append(org.getOrgName());
		}
		return sb.length() > 0 ? sb.substring(1).toString() : value;
	}

	/**
	 * 返回店面数据
	 * @param value 店面的did
	 * @return 店面的名称
	 */
	private String getOrgStore(String value) {
		List<OrgStore> orgStoreList = new OrgStoreDB(context).findOrgListByStoreId(value);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < orgStoreList.size(); i++) {
			OrgStore orgStore = orgStoreList.get(i);
			sb.append(",").append(orgStore.getStoreName());
		}
		return sb.length() > 0 ? sb.substring(1).toString() : value;
	}

	/**
	 * 返回用户数据
	 * @param value 用户的did
	 * @return 用户名
	 */
	private String getOrgUser(String value) {
		List<OrgUser> orgUserList = new OrgUserDB(context).findOrgUserByUserId(value);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < orgUserList.size(); i++) {
			OrgUser orgUser = orgUserList.get(i);
			sb.append(",").append(orgUser.getUserName());
		}
		return sb.length() > 0 ? sb.substring(1).toString() : value;
	}

}
