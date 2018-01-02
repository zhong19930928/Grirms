package com.yunhu.yhshxc.activity.carSales.util;

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

import com.yunhu.yhshxc.activity.carSales.bo.Arrears;
import com.yunhu.yhshxc.activity.carSales.bo.CarSales;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesContact;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesProduct;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesProductConf;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesProductCtrl;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesProductData;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesProductUnti;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesPromotion;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesShoppingCart;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesStock;
import com.yunhu.yhshxc.activity.carSales.db.CarSalesProductConfDB;
import com.yunhu.yhshxc.activity.carSales.db.CarSalesProductCtrlDB;
import com.yunhu.yhshxc.activity.carSales.db.CarSalesPromotionDB;
import com.yunhu.yhshxc.activity.carSales.db.CarSalesStockDB;
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
import com.yunhu.yhshxc.utility.PublicUtils;

public class CarSalesUtil {
	private Context context;
	private String orgId;
	private CarSalesPromotionDB promotionDB;

	public CarSalesUtil(Context mContext) {
		this.context = mContext;
		orgId = SharedPreferencesForCarSalesUtil.getInstance(mContext)
				.getCarSalesOrgId();
		promotionDB = new CarSalesPromotionDB(mContext);
	}

	public List<CarSalesProductCtrl> allProductCtrlList() {
		return new CarSalesProductCtrlDB(context).findAllProductCtrl();
	}

	public List<CarSalesProductCtrl> commonProductCtrlList() {
		return new CarSalesProductCtrlDB(context).findCommonProductCtrl();
	}

	public List<CarSalesProductCtrl> returnProductCtrlList() {
		return new CarSalesProductCtrlDB(context).findReturnProductCtrl();
	}

	public List<CarSalesProductCtrl> loadingProductCtrlList() {
		return new CarSalesProductCtrlDB(context).findLoadingProductCtrl();
	}

	public List<CarSalesProductCtrl> unLoadingProductCtrlList() {
		return new CarSalesProductCtrlDB(context).findUnLoadingProductCtrl();
	}

	public List<CarSalesProductCtrl> rplenishmentProductCtrlList() {
		return new CarSalesProductCtrlDB(context).findRplenishmentProductCtrl();
	}

	public List<CarSalesProductCtrl> outProductCtrlList() {
		return new CarSalesProductCtrlDB(context).findOutProductCtrl();
	}

	public List<CarSalesProductCtrl> invertyProductCtrlList() {
		return new CarSalesProductCtrlDB(context).findInvertyProductCtrl();
	}

	private List<CarSalesProductCtrl> ctrlList;
	private List<CarSalesStock> stockList;

	public void initCarSalesProductCtrl() {
		ctrlList = new ArrayList<CarSalesProductCtrl>();
		stockList = new ArrayList<CarSalesStock>();
		CarSalesProductCtrlDB ctrlDB = new CarSalesProductCtrlDB(context);
		ctrlDB.delete();
		CarSalesProductConfDB db = new CarSalesProductConfDB(context);
		List<CarSalesProductConf> confList = db
				.findListByType(CarSalesProductConf.TYPE_PRODUCT);
		String queryWhere = null;
		CarSalesProductConf conf = confList.get(0);// 第一层
		List<Dictionary> list = new DictDB(context).findDictList(
				conf.getDictTable(), conf.getDictCols(), conf.getDictDataId(),
				null, null, queryWhere, null, null);
		if (list != null && !list.isEmpty()) {
			for (int j = 0; j < list.size(); j++) {// 字典表数据
				Dictionary dic = list.get(j);
				CarSalesProductCtrl ctrl = new CarSalesProductCtrl();
				ctrl.setLabel(dic.getCtrlCol());
				ctrl.setLevelLable(ctrl.getLabel());
				ctrl.setcId(dic.getDid());
				ctrl.setpId("0");
				queryWhere = dic.getDid();
				if (!TextUtils.isEmpty(conf.getNext())) {
					// ctrlDB.insert(ctrl);
					ctrlList.add(ctrl);
					next(conf.getNext(), queryWhere, ctrl, ctrlDB);
				} else {
					List<CarSalesProduct> productList = productUnitList(dic);
					for (int i = 0; i < productList.size(); i++) {
						CarSalesProduct product = productList.get(i);
						CarSalesProductCtrl cTrl = new CarSalesProductCtrl();
						cTrl.setLabel(ctrl.getLabel());
						cTrl.setLevelLable(ctrl.getLevelLable());
						cTrl.setcId(ctrl.getcId());
						cTrl.setpId(ctrl.getpId());
						cTrl.setProductLevel(true);
						cTrl.setProductId(product.getProductId());
						cTrl.setUnitId(product.getUnitId());
						cTrl.setUnit(product.getUnit());
						// ctrlDB.insert(cTrl);
						ctrlList.add(cTrl);
						stockList.add(stockByCtrl(cTrl));
					}
				}
			}
		}

		insertProductCtrl(ctrlDB);
		updateStock();
	}

	private void insertProductCtrl(CarSalesProductCtrlDB ctrlDB) {
		if (!ctrlList.isEmpty()) {
			DatabaseHelper.getInstance(context).beginTransaction();
			for (int i = 0; i < ctrlList.size(); i++) {
				CarSalesProductCtrl ctrl = ctrlList.get(i);
				ctrlDB.insert(ctrl);
			}
			DatabaseHelper.getInstance(context).endTransaction();
		}
	}

	private void updateStock() {
		if (!stockList.isEmpty()) {
			CarSalesStockDB db = new CarSalesStockDB(context);
			// DatabaseHelper.getInstance(context).beginTransaction();
			for (int i = 0; i < stockList.size(); i++) {
				CarSalesStock stock = stockList.get(i);
				CarSalesStock dbStock = db
						.findCarSalesStockByProductIdAndUnitId(
								stock.getProductId(), stock.getUnitId());
				if (dbStock != null) {
					db.updateCarSalesStockLable(stock);
				} else {
					db.insert(stock);
				}
			}
			// DatabaseHelper.getInstance(context).endTransaction();
		}
	}

	private void next(String next, String queryWhere,
			CarSalesProductCtrl pCtrl, CarSalesProductCtrlDB ctrlDB) {
		CarSalesProductConfDB db = new CarSalesProductConfDB(context);
		CarSalesProductConf nextConf = db.findProductConfByNext(next);
		List<Dictionary> nextList = new DictDB(context).findDictList(
				nextConf.getDictTable(), nextConf.getDictCols(),
				nextConf.getDictDataId(), null, null, queryWhere, null, null);
		if (nextList != null && !nextList.isEmpty()) {
			for (int i = 0; i < nextList.size(); i++) {
				Dictionary dic = nextList.get(i);
				CarSalesProductCtrl nCtrl = new CarSalesProductCtrl();
				nCtrl.setLabel(dic.getCtrlCol());
				nCtrl.setLevelLable(pCtrl.getLevelLable() + "@$"
						+ nCtrl.getLabel());
				String str = String.valueOf(pCtrl.getcId()) + dic.getDid();
				nCtrl.setcId(str);
				nCtrl.setpId(pCtrl.getcId());
				if (!TextUtils.isEmpty(nextConf.getNext())) {
					String where = queryWhere + "@" + dic.getDid();
					// ctrlDB.insert(nCtrl);
					ctrlList.add(nCtrl);
					next(nextConf.getNext(), where, nCtrl, ctrlDB);
				} else {
					List<CarSalesProduct> productList = productUnitList(dic);
					for (int j = 0; j < productList.size(); j++) {
						CarSalesProduct product = productList.get(j);
						CarSalesProductCtrl cTrl = new CarSalesProductCtrl();
						cTrl.setLabel(nCtrl.getLabel());
						cTrl.setLevelLable(nCtrl.getLevelLable());
						cTrl.setcId(nCtrl.getcId());
						cTrl.setpId(nCtrl.getpId());
						cTrl.setProductLevel(true);
						cTrl.setProductId(product.getProductId());
						cTrl.setUnitId(product.getUnitId());
						cTrl.setUnit(product.getUnit());
						// ctrlDB.insert(cTrl);
						ctrlList.add(cTrl);
						stockList.add(stockByCtrl(cTrl));
					}
				}
			}
		}

	}

	private CarSalesStock stockByCtrl(CarSalesProductCtrl ctrl) {
		CarSalesStock stock = new CarSalesStock();
		stock.setProductId(ctrl.getProductId());
		stock.setUnitId(ctrl.getUnitId());
		stock.setProductName(ctrl.getLevelLable());
		stock.setReplenishmentNum(0);
		stock.setStockNum(0);
		stock.setStockoutNum(0);
		stock.setUnit(ctrl.getUnit());
		return stock;
	}

	/**
	 * 产品
	 * 
	 * @param dic
	 *            产品对应的字典表实例
	 * @return 同样的产品，不同的单位的产品集合
	 */
	private List<CarSalesProduct> productUnitList(Dictionary dic) {
		List<CarSalesProduct> list = new ArrayList<CarSalesProduct>();
		List<CarSalesProductUnti> unitList = getCarSalesProductUnti(dic
				.getDid());
		CarSalesStockDB stockDB = new CarSalesStockDB(context);
		if (!unitList.isEmpty()) {
			for (int i = 0; i < unitList.size(); i++) {
				CarSalesProduct product = new CarSalesProduct();
				product.setProductId(Integer.parseInt(dic.getDid()));
				product.setName(dic.getCtrlCol());
				product.setCode(productCode(dic.getDid()));
				CarSalesProductUnti d = unitList.get(i);
				product.setUnit(d.getUnit());
				product.setUnitId(d.getUnitId());
				product.setPrice(d.getPrice());
				product.setPromotion(isPromotion(product.getProductId(),
						product.getUnitId()));
				CarSalesStock stock = stockDB
						.findCarSalesStockByProductIdAndUnitId(
								product.getProductId(), product.getUnitId());
				if (stock != null) {
					product.setInventory(stock.getStockNum());
				}
				list.add(product);
			}
		} else {
			CarSalesProduct product = new CarSalesProduct();
			product.setProductId(Integer.parseInt(dic.getDid()));
			product.setName(dic.getCtrlCol());
			product.setCode(productCode(dic.getDid()));
			product.setPrice(0);
			product.setUnit(" ");
			product.setUnitId(-1);
			product.setPromotion(isPromotion(product.getProductId(),
					product.getUnitId()));
			CarSalesStock stock = stockDB
					.findCarSalesStockByProductIdAndUnitId(
							product.getProductId(), product.getUnitId());
			if (stock != null) {
				product.setInventory(stock.getStockNum());
			}
			list.add(product);
		}

		return list;
	}

	/**
	 * 查询产品是否有促销信息
	 * 
	 * @param pId
	 * @param uId
	 * @return
	 */
	private boolean isPromotion(int pId, int uId) {
		boolean flag = false;
		int pormotion = SharedPreferencesForCarSalesUtil.getInstance(context)
				.getIsPromotion();// 是否需要促销模块 1不要 2要 默认2
		if (pormotion == 2) {
			int level = SharedPreferencesForCarSalesUtil.getInstance(context)
					.getCarSalesStoreLevel();
			flag = promotionDB.isPromotion(pId, uId, orgId, level);
		}
		return flag;
	}

	/**
	 * 一个产品上级所有层级对应的Dictionary
	 * 
	 * @param productId
	 * @return
	 */
	private List<Dictionary> getProductRelatedList(String productId) {
		DictDB dictDB = new DictDB(context);
		List<CarSalesProductConf> productConfList = new CarSalesProductConfDB(
				context).findListByType(CarSalesProductConf.TYPE_PRODUCT);// 所有层级
		int size = productConfList.size();// 所有层级数
		// CarSalesProductConf product = new
		// CarSalesProductConfDB(context).findLastProductConf();//最后层级
		CarSalesProductConf product = productConfList.get(size - 1);// 最后层级
		String[] relatedColumnDid = dictDB.findRelatedColumnDid(
				product.getDictTable(), product.getDictDataId(),
				product.getDictCols(), productId);
		if (relatedColumnDid == null || relatedColumnDid.length != size) {
			throw new NullPointerException("FUNC Exception");
		}
		List<Dictionary> list = new ArrayList<Dictionary>(size);
		Dictionary dict = null;
		for (int i = 0; i < size; i++) {
			dict = dictDB
					.findDictByDid(productConfList.get(i).getDictTable(),
							productConfList.get(i).getDictDataId(),
							relatedColumnDid[i]);
			list.add(dict);
		}
		return list;
	}

	/**
	 * 计量单位
	 * 
	 * @param productId
	 *            产品ID
	 * @return 该产品对应的所有计量单位
	 */
	public List<CarSalesProductUnti> getCarSalesProductUnti(String productId) {
		List<Dictionary> list = getProductRelatedList(productId);
		DictDB dictDB = new DictDB(context);
		CarSalesProductConf conf = new CarSalesProductConfDB(context)
				.findUnitProductConf();
		String[] relatedCols = dictDB.findRelatedForeignkey(conf.getDictCols(),
				conf.getDictDataId());
		String[] values = new String[list.size()];
		int i = 0;
		for (Dictionary dict : list) {
			values[i] = dict.getDid();
			i++;
		}
		String price_ctrl = SharedPreferencesForCarSalesUtil.getInstance(
				context).getPriceCtrl();
		String[] str = price_ctrl.split("\\|");
		String priceId = "data_" + str[1];
		List<CarSalesProductUnti> dict = dictDB
				.findCarSalesProductUntiByRelatedColumn(conf.getDictTable(),
						conf.getDictDataId(), priceId, relatedCols, values);
		return dict;
	}

	/**
	 * 产品二维码
	 * 
	 * @param productId
	 * @return
	 */
	public String productCode(String productId) {
		String code = "";
		String code_ctrl = SharedPreferencesForCarSalesUtil
				.getInstance(context).getCodeCtrl();
		String[] str = code_ctrl.split("\\|");
		String table = "t_m_" + str[0];
		String dictDataId = "data_" + str[1];
		Dictionary codeDic = new DictDB(context).findDictByDid(table,
				dictDataId, productId);
		if (codeDic != null) {
			code = codeDic.getCtrlCol();
		}
		return code;
	}

	/**
	 * 通过code找产品
	 * 
	 * @param code
	 * @return
	 */
	public CarSalesProduct productForCode(String code) {
		CarSalesProduct product = null;
		String code_ctrl = SharedPreferencesForCarSalesUtil
				.getInstance(context).getCodeCtrl();
		String[] str = code_ctrl.split("\\|");
		String codeTable = "t_m_" + str[0];
		String codeDictDataId = "data_" + str[1];
		Dictionary codeDic = new DictDB(context).findDictByDataValue(codeTable,
				codeDictDataId, code);
		if (codeDic != null) {
			String productId = codeDic.getDid();
			List<CarSalesProductUnti> untiList = getCarSalesProductUnti(productId);
			product = product(Integer.parseInt(productId),
					untiList.size() > 0 ? untiList.get(0).getUnitId() : -1);
		}
		return product;
	}

	/**
	 * 根据产品id
	 * 
	 * @param productId
	 *            产品ID
	 * @param unitId
	 *            计量单位ID
	 * @return
	 */
	public CarSalesProduct product(int productId, int unitId) {
		CarSalesProduct product = null;
		CarSalesProductConf productConf = new CarSalesProductConfDB(context)
				.findLastProductConf();// 最后层级
		if (productConf != null) {
			List<Dictionary> list = new DictDB(context).findDictList(
					productConf.getDictTable(), productConf.getDictCols(),
					productConf.getDictDataId(), null, null, null, null,
					String.valueOf(productId));
			if (list != null && list.size() > 0) {
				Dictionary dic = list.get(0);
				List<CarSalesProduct> productUnitList = productUnitList(dic);
				for (int i = 0; i < productUnitList.size(); i++) {
					CarSalesProduct p = productUnitList.get(i);
					if (unitId == p.getUnitId()) {
						product = p;
						break;
					}
				}
			}
		}
		return product;
	}

	/**
	 * 订单编号
	 * 
	 * @param isCreate
	 *            是否是创建订单编号
	 * @return
	 */
	public String carSalesNumber(boolean isCreate) {

		return String.valueOf(System.currentTimeMillis());
		// if (isCreate) {//创建订单号
		// StringBuffer number = new StringBuffer();
		// String d = DateUtil.getCurYearMonthDay();
		// String userId =
		// String.valueOf(SharedPreferencesUtil.getInstance(context).getUserId()+10000000).substring(1);
		// number.append(d).append(userId);
		//
		// String str =
		// SharedPreferencesForCarSalesUtil.getInstance(context).getCarSalesCount();
		// if (!TextUtils.isEmpty(str)) {
		// String[] s = str.split("_");
		// String date = s[0];
		// String num = s[1];
		// String today = DateUtil.getCurDate();
		// if (today.equals(date)) {
		// number.append(String.valueOf((Integer.parseInt(num)+1)+1000).substring(1));
		// }else{
		// number.append("001");
		// }
		// }else{
		// number.append("001");
		// }
		// return number.toString();
		// }else{//提交的时候维护订单号
		// String str =
		// SharedPreferencesForCarSalesUtil.getInstance(context).getCarSalesCount();
		// if (!TextUtils.isEmpty(str)) {
		// String[] s = str.split("_");
		// String date = s[0];
		// String num = s[1];
		// SharedPreferencesForCarSalesUtil.getInstance(context).setCarSalesCount(date+"_"+(Integer.parseInt(num)+1));
		// }else{
		// String today = DateUtil.getCurDate();
		// SharedPreferencesForCarSalesUtil.getInstance(context).setCarSalesCount(today+"_"+1);
		//
		// }
		// return "";
		// }
		//
	}

	public String submitCarBalanceInfoJson(List<Arrears> arrears)
			throws JSONException {
		String json = "";
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < arrears.size(); i++) {
			Arrears ears = arrears.get(i);
			if (ears != null) {
				JSONArray array = new JSONArray();
				array.put(String.valueOf(ears.getId()));
				array.put(String.valueOf(ears.getSkAmount()));
				array.put(String.valueOf(ears.getArrearsAmount()
						- ears.getSkAmount()));
				array.put(String.valueOf(ears.getIsOver()));
				array.put(String.valueOf(ears.getHisAmount()
						+ ears.getSkAmount()));
				jsonArray.put(array);
			}
		}
		json = jsonArray.toString();
		return json;
	}

	public String submitJson(List<CarSales> carSalesList) throws JSONException {
		String json = "";
		JSONArray carSalesArray = new JSONArray();
		for (int i = 0; i < carSalesList.size(); i++) {
			CarSales carSales = carSalesList.get(i);
			if (carSales != null) {
				JSONObject obj = new JSONObject();
				String carSalesNo = carSales.getCarSalesNo();// 订单编号
				obj.put("sales_num", carSalesNo);
				obj.put("sales_date", carSales.getCarSalesTime());// 订单日期
				String timestamp = SharedPreferencesForCarSalesUtil
						.getInstance(context).getCarSalesTimestamp();
				if (!TextUtils.isEmpty(timestamp)) {
					obj.put("link_mod", timestamp);
				}
				obj.put("pay_amount", carSales.getPayAmount());// 预收款
				obj.put("order_discount", carSales.getCarSalesDiscount());// 特别折扣
				obj.put("unpaid_amount", carSales.getUnPayAmount());// 未支付
				String amount = String.valueOf(carSales.getCarSalesAmount());// 订单总额
				obj.put("sales_amount", amount);
				obj.put("actual_amount", carSales.getActualAmount());
				String note = carSales.getNote();// 留言
				obj.put("remarks", note);
				obj.put("is_promotion", carSales.getIsPromotion());// 是否有促销
				obj.put("promotion_amount", carSales.getActualAmount()
						+ carSales.getCarSalesDiscount());// 促销金额 实际金额+特别减免
				obj.put("print_count", carSales.getPrintCount());// 下单打印次数
				obj.put("image1", carSales.getImage1());
				obj.put("image2", carSales.getImage2());
				obj.put("storeId", carSales.getStoreId());
				obj.put("storeName", carSales.getStoreName());
				obj.put("salesDate", carSales.getSalesDate());
				obj.put("carId", carSales.getCarId());
				obj.put("status", carSales.getStatus());
				obj.put("id", carSales.getCarSalesId());
				obj.put("return_amount", carSales.getReturnAmount());

				CarSalesContact CarSalesContact = carSales.getContact();
				if (CarSalesContact != null) {// 联系人
					JSONObject contact = new JSONObject();
					contact.put("id", CarSalesContact.getContactsId());
					if (CarSalesContact.getUserName() != null) {
						contact.put("user_name", CarSalesContact.getUserName());
					}
					if (CarSalesContact.getUserAddress() != null) {
						contact.put("user_address",
								CarSalesContact.getUserAddress());
					}
					if (CarSalesContact.getUserPhone1() != null) {
						contact.put("user_phone_1",
								CarSalesContact.getUserPhone1());
					}
					if (CarSalesContact.getUserPhone2() != null) {
						contact.put("user_phone_2",
								CarSalesContact.getUserPhone2());
					}
					if (CarSalesContact.getUserPhone3() != null) {
						contact.put("user_phone_3",
								CarSalesContact.getUserPhone3());
					}
					if (CarSalesContact.getStoreId() != null) {
						contact.put("storeId", CarSalesContact.getStoreId());
					}
				}

				List<CarSalesProductData> dataList = carSales.getProductList();
				if (dataList != null && !dataList.isEmpty()) {
					JSONArray array = new JSONArray();
					for (int j = 0; j < dataList.size(); j++) {
						CarSalesProductData data = dataList.get(j);
						JSONObject dObj = new JSONObject();
						dObj.put("product_id", data.getProductId());
						dObj.put("sales_count", data.getCarSalesCount());
						dObj.put("actual_count", data.getActualCount());
						dObj.put("pro_standard", data.getProductUnit());
						dObj.put("pro_standard_id", data.getUnitId());
						dObj.put("sales_price", data.getCarSalesPrice());
						dObj.put("sales_amount", data.getCarSalesAmount());
						dObj.put("actual_amount", data.getActualAmount());
						dObj.put("promotion_id", data.getPromotionId());
						dObj.put("main_product_id", data.getMainProductId());
						dObj.put("sales_remark", data.getCarSalesRemark());
						dObj.put("is_sales_main", data.getIsCarSalesMain());
						dObj.put("id", data.getDataId());
						dObj.put("send_count", data.getCurrentSendCount());
						array.put(dObj);
					}
					obj.put("detail", array);
				}

				boolean isStock = carSales.isStock();
				if (isStock) {
					JSONArray stockArray = stockData();
					obj.put("stockDetail", stockArray);
				}
				carSalesArray.put(obj);
			}
		}
		json = carSalesArray.toString();
		return json;
	}

	private JSONArray stockData() throws JSONException {
		List<CarSalesStock> stockList = new CarSalesStockDB(context)
				.findCarSalesStock();
		JSONArray array = new JSONArray();
		for (int i = 0; i < stockList.size(); i++) {
			CarSalesStock stock = stockList.get(i);
			JSONObject obj = new JSONObject();
			obj.put("product_id", stock.getProductId());
			obj.put("pro_standard", stock.getUnit());
			obj.put("pro_standard_id", stock.getUnitId());
			// obj.put("lable", stock.getProductName());
			obj.put("stock_count", stock.getStockNum());
			obj.put("stock_out_count", stock.getStockoutNum());
			obj.put("fill_count", stock.getReplenishmentNum());
			array.put(obj);
		}
		return array;
	}

	/**
	 * 产品属性
	 * 
	 * @param productId
	 *            产品ID
	 * @param unitId
	 *            单位ID
	 */
	public Map<String, String> productInfo(int productId) {
		Map<String, String> map = new HashMap<String, String>();
		CarSalesProductConf conf = new CarSalesProductConfDB(context)
				.findLastProductConf();// 最后层级
		String tab = conf.getDictTable();
		String ctrl = conf.getDictDataId();
		String cols = conf.getDictCols();
		String funcInfo = cols.split(ctrl)[1];
		List<Func> funcList = new FuncDB(context).findFuncByFuncIds(funcInfo
				.replace("data_", "").substring(1));
		DictDB dicDB = new DictDB(context);
		for (int i = 0; i < funcList.size(); i++) {
			Func func = funcList.get(i);
			String name = func.getName();
			int funcId = func.getFuncId();
			int type = func.getType();
			Dictionary dic = dicDB.findDictByDid(tab, "data_" + funcId,
					String.valueOf(productId));
			if (dic != null) {
				String value = dic.getCtrlCol();
				if (type == Func.TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY_COMP
						|| type == Func.TYPE_SELECTCOMP
						|| type == Func.TYPE_MULTI_CHOICE_FUZZY_QUERY_COMP
						|| type == Func.TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY_COMP
						|| type == Func.TYPE_SELECT_OTHER
						|| type == Func.TYPE_MULTI_CHOICE_SPINNER_COMP) {// 其他类型的下拉框
					if (func.getOrgOption() != null
							&& func.getOrgOption() != Func.OPTION_LOCATION) {// 表格里如果有店或者user
						if (!TextUtils.isEmpty(value)) {
							if (func.getOrgOption() == Func.ORG_STORE) {// 店
								value = getOrgStore(value);
							} else if (func.getOrgOption() == Func.ORG_USER) {// user
								value = getOrgUser(value);
							} else if (func.getOrgOption() == Func.ORG_OPTION) {
								value = getOrg(value);
							}
						}
					} else {
						if (!TextUtils.isEmpty(value)) {// 空值
							value = dicDB.findDictMultiChoiceValueStr(value,
									func.getDictDataId(), func.getDictTable());
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
	 * 
	 * @param value
	 *            机构的did
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
	 * 
	 * @param value
	 *            店面的did
	 * @return 店面的名称
	 */
	private String getOrgStore(String value) {
		List<OrgStore> orgStoreList = new OrgStoreDB(context)
				.findOrgListByStoreId(value);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < orgStoreList.size(); i++) {
			OrgStore orgStore = orgStoreList.get(i);
			sb.append(",").append(orgStore.getStoreName());
		}
		return sb.length() > 0 ? sb.substring(1).toString() : value;
	}

	/**
	 * 返回用户数据
	 * 
	 * @param value
	 *            用户的did
	 * @return 用户名
	 */
	private String getOrgUser(String value) {
		List<OrgUser> orgUserList = new OrgUserDB(context)
				.findOrgUserByUserId(value);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < orgUserList.size(); i++) {
			OrgUser orgUser = orgUserList.get(i);
			sb.append(",").append(orgUser.getUserName());
		}
		return sb.length() > 0 ? sb.substring(1).toString() : value;
	}

	public void initPro(CarSalesShoppingCart cart) {
		cart.setDiscountPrice(0);
		cart.setDisAmount(0);
		cart.setDisNumber(0);
		cart.setPreAmount(0);
		cart.setPrePrice(0);
		cart.setPro(null);
		cart.setGiftId(0);
		cart.setGiftUnitId(0);

	}

	/**
	 * 单品 促销分类计算
	 * 
	 * @param promotion
	 * @param product
	 * @param cart
	 * @param allNum
	 */
	public void fenLei(CarSalesPromotion promotion, CarSalesProduct product,
			CarSalesShoppingCart cart, double allNum) {
		initPro(cart);
		if (allNum != 0) {
			double sinFullPrice = allNum * cart.getNowProductPrict();
			switch (promotion.getPreType()) {
			case 1:// 单品买满数量
				if (allNum >= promotion.getmCnt()) {
					int a = (int) (allNum / promotion.getmCnt());
					switch (promotion.getDisType()) {
					case 3:// 打折
						double singleFullNumDis = discount(allNum, promotion,
								cart.getNowProductPrict(), cart);
						cart.setDiscountPrice(singleFullNumDis);
						break;
					case 1:// 送赠品
						CarSalesProduct singleFullNumProduct = product(
								Integer.parseInt(promotion.getsId()),
								Integer.parseInt(promotion.getsUid()));// 根据mTab,mId,mUid去查赠品
						double singleFullNumZeng = zengpin(a, promotion);
						cart.setDisNumber(singleFullNumZeng);
						cart.setPro(singleFullNumProduct);
						cart.setGiftId(singleFullNumProduct.getProductId());
						cart.setGiftUnitId(singleFullNumProduct.getUnitId());
						break;
					case 2:// 减免金额
						double singleFullNumPri = subMoney(a, promotion,
								allNum, cart.getNowProductPrict(), cart);
						cart.setDisAmount(singleFullNumPri);
						break;
					default:
						break;
					}
				}
				break;
			case 2:// 单品买满金额
				if (sinFullPrice >= promotion.getAmount()) {
					int b = (int) (sinFullPrice / promotion.getAmount());
					switch (promotion.getDisType()) {
					case 3:// 打折
						double singleFullPriceDis = discount(allNum, promotion,
								cart.getNowProductPrict(), cart);
						cart.setDiscountPrice(singleFullPriceDis);
						break;
					case 1:// 送赠品
						CarSalesProduct singleFullPriceProduct = product(
								Integer.parseInt(promotion.getsId()),
								Integer.parseInt(promotion.getsUid()));// 根据mTab,mId,mUid去查赠品
						double singleFullPriceZeng = zengpin(b, promotion);
						cart.setDisNumber(singleFullPriceZeng);
						cart.setPro(singleFullPriceProduct);
						cart.setGiftId(singleFullPriceProduct.getProductId());
						cart.setGiftUnitId(singleFullPriceProduct.getUnitId());
						break;
					case 2:// 减免金额
						double singleFullPricePri = subMoney(b, promotion,
								allNum, cart.getNowProductPrict(), cart);
						cart.setDisAmount(singleFullPricePri);
						break;
					default:
						break;
					}
				}
				break;
			default:
				break;
			}
		}
	}

	private double subMoney(int bei, CarSalesPromotion o, double number,
			double price, CarSalesShoppingCart cart) {
		double disAmount = o.getDisAmount() * bei;// 返还现金金额
		double prePriceJ = number * price;
		double nowPriceJ = prePriceJ - disAmount;
		cart.setPreAmount(disAmount);
		return nowPriceJ;

	}

	public double zengpin(int bei, CarSalesPromotion order3Promotion) {

		double zengpinNum = order3Promotion.getsCnt() * bei;// 赠送产品数；
		return zengpinNum;

	}

	private double discount(double number, CarSalesPromotion order3Promotion,
			double price, CarSalesShoppingCart cart) {
		double disRate = changeDouble(Integer.parseInt(order3Promotion
				.getDisRate()));// 折扣率
		double prePrice = number * price;
		double nowPrice = prePrice * disRate;
		double p = prePrice - nowPrice;
		cart.setPrePrice(Double.parseDouble(PublicUtils.formatDouble(p)));
		return nowPrice;

	}

	public double changeDouble(int s) {
		int g = s / 10;
		int y = s % 10;
		StringBuffer sb = new StringBuffer();
		if (g == 0) {
			sb.append("0.0").append(String.valueOf(y));
			return Double.parseDouble(sb.toString());
		} else {
			sb.append("0.").append(String.valueOf(g)).append(String.valueOf(y));
			return Double.parseDouble(sb.toString());
		}
	}

	public double discountZonghe(CarSalesPromotion pro, double allMoney) {
		double disRate = changeDouble(Integer
				.parseInt(pro.getDisRate() != null ? pro.getDisRate() : "0"));// 折扣率
		double prePrice = allMoney;
		double nowPrice = prePrice * disRate;
		return nowPrice;
	}

	public double prePrice(CarSalesPromotion pro, double allMoney) { // 打折后减免金额
		double disRate = changeDouble(Integer
				.parseInt(pro.getDisRate() != null ? pro.getDisRate() : "0"));// 折扣率
		double prePrice = allMoney;
		double nowPrice = prePrice * disRate;
		double p = prePrice - nowPrice;
		return p;
	}

	public double subMoneyZonHe(CarSalesPromotion pro, double allMoney) {
		double disAmount = pro.getDisAmount();// 返还现金金额
		double prePriceJ = allMoney;
		double nowPriceJ = prePriceJ - disAmount;
		// JLog.d("aaa", "prePriceJ  "+prePriceJ+"   disAmount  "+disAmount);
		return nowPriceJ;
	}

	public double preAmount(CarSalesPromotion pro, int bei) {
		double disAmount = pro.getDisAmount() * bei;// 返还现金金额
		return disAmount;
	}

	/**
	 * 初始化优惠数据
	 */
	public void allChecked(List<CarSalesShoppingCart> cartsCheckedIsDouble,
			List<CarSalesShoppingCart> cartsCheckedNoDouble,
			List<CarSalesShoppingCart> carSalesShoppingCarts) {
		if (cartsCheckedIsDouble != null) {
			cartsCheckedIsDouble.clear();
		}
		if (cartsCheckedNoDouble != null) {
			cartsCheckedNoDouble.clear();
		}
		if (carSalesShoppingCarts != null) {
			for (int i = 0; i < carSalesShoppingCarts.size(); i++) {
				CarSalesShoppingCart cart = carSalesShoppingCarts.get(i);
				if (cart.getPitcOn() == 1) {// 选中
					String ids = cart.getPromotionIds();
					if (!TextUtils.isEmpty(ids) && !ids.equals("0")) {
						CarSalesPromotion o = promotionDB
								.findPromotionByPromotionId(Integer
										.parseInt(ids));
						if (o != null) {
							if (o.getIsDouble() == 1) {// 享受多重优惠的
								cartsCheckedIsDouble.add(cart);
							} else {
								cartsCheckedNoDouble.add(cart);
							}
						} else {
							// cartsCheckedNoDouble.add(cart);
							cartsCheckedIsDouble.add(cart);
						}
					} else {
						cartsCheckedIsDouble.add(cart);
					}
				}

			}
		}

	}

	/**
	 * 总和买满数量减免打折
	 * 
	 * @return
	 */
	public double zongHeFullNumDis(double allMoeny, double allNum) {
		double finalPrice = 0;
		int level = SharedPreferencesForCarSalesUtil.getInstance(context)
				.getCarSalesStoreLevel();
		List<CarSalesPromotion> pos5 = promotionDB.findPromotionByPreType(
				SharedPreferencesForCarSalesUtil.getInstance(context)
						.getCarSalesOrgId(), 5, 3, level);
		if (pos5.size() > 0) {
			for (int i = 0; i < pos5.size(); i++) {
				if (allNum >= pos5.get(i).getmCnt()) {
					double a = prePrice(pos5.get(i), allMoeny);
					if (finalPrice < a) {
						finalPrice = a;
					}
				}
			}
		}
		return finalPrice;
	}

	/**
	 * 总和买满金额减免打折
	 * 
	 * @return
	 */
	public double zongHeFullPriceDis(double allMoeny) {
		double finalPrice = 0;
		int level = SharedPreferencesForCarSalesUtil.getInstance(context)
				.getCarSalesStoreLevel();
		List<CarSalesPromotion> pos6 = promotionDB.findPromotionByPreType(
				SharedPreferencesForCarSalesUtil.getInstance(context)
						.getCarSalesOrgId(), 6, 3, level);
		if (pos6.size() > 0) {
			for (int i = 0; i < pos6.size(); i++) {
				if (allMoeny >= pos6.get(i).getAmount()) {
					double a = prePrice(pos6.get(i), allMoeny);
					if (finalPrice < a) {
						finalPrice = a;
					}
				}
			}
		}
		return finalPrice;
	}

	/**
	 * 总和买满数量减免金额
	 * 
	 * @return
	 */
	public double zongHeFullNum(List<CarSalesShoppingCart> cartsCheckedIsDouble) {
		double finalPrice = 0;
		int level = SharedPreferencesForCarSalesUtil.getInstance(context)
				.getCarSalesStoreLevel();
		List<CarSalesPromotion> pos5 = promotionDB.findPromotionByPreType(
				SharedPreferencesForCarSalesUtil.getInstance(context)
						.getCarSalesOrgId(), 5, 2, level);
		if (pos5.size() > 0) {
			for (int i = 0; i < pos5.size(); i++) {
				if (allNum(cartsCheckedIsDouble) >= pos5.get(i).getmCnt()) {
					int b = (int) (allNum(cartsCheckedIsDouble) / pos5.get(i)
							.getmCnt());
					double a = preAmount(pos5.get(i), b);
					if (finalPrice < a) {
						finalPrice = a;
					}
				}
			}
		}
		return finalPrice;
	}

	/**
	 * 购物车中所有享受多重优惠产品的总数量
	 */
	public double allNum(List<CarSalesShoppingCart> cartsCheckedIsDouble) {
		double all = 0;
		for (int i = 0; i < cartsCheckedIsDouble.size(); i++) {
			all += cartsCheckedIsDouble.get(i).getNumber();
		}
		return all;
	}

	/**
	 * 总和买满金额减免金额
	 * 
	 * @return
	 */
	public double zongHeFullPrice(double allMoney) {
		double finalPrice = 0;
		int level = SharedPreferencesForCarSalesUtil.getInstance(context)
				.getCarSalesStoreLevel();
		List<CarSalesPromotion> pos6 = promotionDB.findPromotionByPreType(
				SharedPreferencesForCarSalesUtil.getInstance(context)
						.getCarSalesOrgId(), 6, 2, level);
		if (pos6.size() > 0) {
			for (int i = 0; i < pos6.size(); i++) {
				if (allMoney >= pos6.get(i).getAmount()) {
					int b = (int) (allMoney / pos6.get(i).getAmount());
					double a = preAmount(pos6.get(i), b);
					if (finalPrice < a) {
						finalPrice = a;
					}
				}
			}
		}
		return finalPrice;

	}

	/**
	 * 购物车中所有享受多重优惠产品的总价格
	 * 
	 * @return
	 */
	public double allMoney(List<CarSalesShoppingCart> cartsCheckedIsDouble,
			List<CarSalesShoppingCart> cartsCheckedNoDouble,
			List<CarSalesShoppingCart> carSalesShoppingCarts) {
		allChecked(cartsCheckedIsDouble, cartsCheckedNoDouble,
				carSalesShoppingCarts);
		double money = 0;
		if (cartsCheckedIsDouble != null) {
			for (int i = 0; i < cartsCheckedIsDouble.size(); i++) {
				CarSalesShoppingCart cart = cartsCheckedIsDouble.get(i);
				if ((cart.getDisAmount() + cart.getDiscountPrice()) != 0) {
					money += (cart.getDisAmount() + cart.getDiscountPrice());
				} else {
					money += cart.getSubtotal();
				}
			}
		}

		return money;
	}

	/**
	 * CarSaleShoppingCartActivity中
	 */

	/**
	 * 总和买满数量送打折
	 * 
	 * @return
	 */
	public Map<String, String> zongHeFullNumDis(
			List<CarSalesShoppingCart> cartsCheckedIsDouble, double allMoney) {
		// double finalNum = 0;
		double prePriPrice = 0;
		double allPriceAmount = 0;
		String name = null;
		double pid = 0;
		Map<String, String> map = new HashMap<String, String>();
		int level = SharedPreferencesForCarSalesUtil.getInstance(context)
				.getCarSalesStoreLevel();
		List<CarSalesPromotion> pos5 = promotionDB.findPromotionByPreType(
				SharedPreferencesForCarSalesUtil.getInstance(context)
						.getCarSalesOrgId(), 5, 3, level);
		if (pos5.size() > 0) {

			for (int i = 0; i < pos5.size(); i++) {
				if (allNum(cartsCheckedIsDouble) >= pos5.get(i).getmCnt()) {
					double a = prePrice(pos5.get(i), allMoney);
					if (prePriPrice < a) {
						prePriPrice = a;
						allPriceAmount = discountZonghe(pos5.get(i), allMoney);
						pid = pos5.get(i).getPromotionId();
						name = pos5.get(i).getName();
					}
				}
			}
		}
		map.put("name", name);
		map.put("price", String.valueOf(allPriceAmount));
		map.put("pre", String.valueOf(prePriPrice));
		map.put("proid", String.valueOf(pid));
		return map;
	}

	/**
	 * 总和买满数量送减免金额
	 * 
	 * @return
	 */
	public Map<String, String> zongHeFullNumJM(
			List<CarSalesShoppingCart> cartsCheckedIsDouble, double allMoney) {
		double prePriPrice = 0;
		double allPriceAmount = 0;
		String name = null;
		double pid = 0;
		Map<String, String> map = new HashMap<String, String>();
		int level = SharedPreferencesForCarSalesUtil.getInstance(context)
				.getCarSalesStoreLevel();
		List<CarSalesPromotion> pos5 = promotionDB.findPromotionByPreType(
				SharedPreferencesForCarSalesUtil.getInstance(context)
						.getCarSalesOrgId(), 5, 2, level);
		if (pos5.size() > 0) {

			for (int i = 0; i < pos5.size(); i++) {
				double numA = allNum(cartsCheckedIsDouble);
				if (numA >= pos5.get(i).getmCnt()) {
					int b = (int) (numA / pos5.get(i).getmCnt());
					double a = preAmount(pos5.get(i), b);
					if (prePriPrice < a) {
						prePriPrice = a;
						allPriceAmount = subMoneyZonHe(pos5.get(i), allMoney);
						name = pos5.get(i).getName();
						pid = pos5.get(i).getPromotionId();
					}
				}
			}
		}
		map.put("name", name);
		map.put("price", String.valueOf(allPriceAmount));
		map.put("pre", String.valueOf(prePriPrice));
		map.put("proid", String.valueOf(pid));
		return map;
	}

	/**
	 * 总和买满数量送赠品
	 * 
	 * @return
	 */
	// List<>
	public Map<String, Double> zongHeFullNum5(
			List<CarSalesShoppingCart> cartsCheckedIsDouble) {
		Map<String, Double> map = new HashMap();
		double finalNum = 0;
		double pid = 0;
		double b = 0;
		int level = SharedPreferencesForCarSalesUtil.getInstance(context)
				.getCarSalesStoreLevel();
		List<CarSalesPromotion> pos5 = promotionDB.findPromotionByPreType(
				SharedPreferencesForCarSalesUtil.getInstance(context)
						.getCarSalesOrgId(), 5, 1, level);
		if (pos5.size() > 0) {

			for (int i = 0; i < pos5.size(); i++) {
				double moneyAll = allNum(cartsCheckedIsDouble);
				if (moneyAll >= pos5.get(i).getmCnt()) {
					int bei = (int) (moneyAll / pos5.get(i).getmCnt());
					double a = zengpin(bei, pos5.get(i));
					if (finalNum < a) {
						finalNum = a;
						b = i;
						pid = pos5.get(i).getPromotionId();
					}
				}
			}

		}
		map.put("num", finalNum);
		map.put("index", b);
		map.put("proid", pid);
		return map;
	}

	/**
	 * 总和买满金额送打折
	 * 
	 * @return
	 */
	public Map<String, String> zongHeFullNumDis6(
			List<CarSalesShoppingCart> cartsCheckedIsDouble,
			List<CarSalesShoppingCart> cartsCheckedNoDouble,
			List<CarSalesShoppingCart> carSalesShoppingCarts, double allMoney) {
		// double finalNum = 0;
		double prePriPrice = 0;
		double allPriceAmount = 0;
		double pid = 0;
		String name = null;
		Map<String, String> map = new HashMap<String, String>();
		int level = SharedPreferencesForCarSalesUtil.getInstance(context)
				.getCarSalesStoreLevel();
		List<CarSalesPromotion> pos5 = promotionDB.findPromotionByPreType(
				SharedPreferencesForCarSalesUtil.getInstance(context)
						.getCarSalesOrgId(), 6, 3, level);
		if (pos5.size() > 0) {

			for (int i = 0; i < pos5.size(); i++) {
				if (allMoney >= pos5.get(i).getAmount()) {
					double a = prePrice(pos5.get(i), allMoney);
					if (prePriPrice < a) {
						prePriPrice = a;
						allPriceAmount = discountZonghe(pos5.get(i), allMoney);
						pid = pos5.get(i).getPromotionId();
						name = pos5.get(i).getName();
					}
				}
			}
		}
		map.put("name", name);
		map.put("price", String.valueOf(allPriceAmount));
		map.put("pre", String.valueOf(prePriPrice));
		map.put("proid", String.valueOf(pid));
		return map;
	}

	/**
	 * 总和买满金额送减免金额
	 * 
	 * @return
	 */
	public Map<String, String> zongHeFullNumJM6(
			List<CarSalesShoppingCart> cartsCheckedIsDouble,
			List<CarSalesShoppingCart> cartsCheckedNoDouble,
			List<CarSalesShoppingCart> carSalesShoppingCarts, double allMoney) {
		double prePriPrice = 0;
		double allPriceAmount = 0;
		String name = null;
		double pid = 0;
		Map<String, String> map = new HashMap<String, String>();
		int level = SharedPreferencesForCarSalesUtil.getInstance(context)
				.getCarSalesStoreLevel();
		List<CarSalesPromotion> pos5 = promotionDB.findPromotionByPreType(
				SharedPreferencesForCarSalesUtil.getInstance(context)
						.getCarSalesOrgId(), 6, 2, level);
		if (pos5.size() > 0) {

			for (int i = 0; i < pos5.size(); i++) {
				if (allMoney >= pos5.get(i).getAmount()) {
					int b = (int) (allMoney / pos5.get(i).getAmount());
					double a = preAmount(pos5.get(i), b);
					if (prePriPrice < a) {
						prePriPrice = a;
						allPriceAmount = subMoneyZonHe(pos5.get(i), allMoney);
						name = pos5.get(i).getName();
						pid = pos5.get(i).getPromotionId();
					}
				}
			}
		}
		map.put("name", name);
		map.put("price", String.valueOf(allPriceAmount));
		map.put("pre", String.valueOf(prePriPrice));
		map.put("proid", String.valueOf(pid));
		// JLog.d("aaa",
		// "  name  "+name+"  price  "+allPriceAmount+"  pre  "+prePriPrice);
		return map;
	}

	/**
	 * 总和买满金额送赠品
	 * 
	 * @return
	 */
	// List<>
	public Map<String, Double> zongHeFullNum6(
			List<CarSalesShoppingCart> cartsCheckedIsDouble,
			List<CarSalesShoppingCart> cartsCheckedNoDouble,
			List<CarSalesShoppingCart> carSalesShoppingCarts, double allMoney) {
		Map<String, Double> map = new HashMap();
		double finalNum = 0;
		double b = 0;
		double pid = 0;
		int level = SharedPreferencesForCarSalesUtil.getInstance(context)
				.getCarSalesStoreLevel();
		List<CarSalesPromotion> pos5 = promotionDB.findPromotionByPreType(
				SharedPreferencesForCarSalesUtil.getInstance(context)
						.getCarSalesOrgId(), 6, 1, level);
		if (pos5.size() > 0) {

			for (int i = 0; i < pos5.size(); i++) {

				if (allMoney >= pos5.get(i).getAmount()) {
					int c = (int) (allMoney / pos5.get(i).getAmount());
					double a = zengpin(c, pos5.get(i));
					if (finalNum < a) {
						finalNum = a;
						b = i;
						pid = pos5.get(i).getPromotionId();
					}
				}
			}

		}
		map.put("num", finalNum);
		map.put("index", b);
		map.put("proid", pid);
		return map;
	}

	/**
	 * 购物车中所有不享受多重优惠产品的总价格
	 * 
	 * @return
	 */
	public double allMoneyNoDouble(
			List<CarSalesShoppingCart> cartsCheckedNoDouble) {
		double money = 0;
		if (cartsCheckedNoDouble != null) {
			for (int i = 0; i < cartsCheckedNoDouble.size(); i++) {
				CarSalesShoppingCart cart = cartsCheckedNoDouble.get(i);
				if ((cart.getDisAmount() + cart.getDiscountPrice()) != 0) {
					money += cart.getDisAmount() + cart.getDiscountPrice();
				} else {
					money += cart.getSubtotal();
				}
			}
		}

		return money;
	}

	/**
	 * 列表库存控制 key 是CarSalesProductCtrl 的主键ID value是库存量
	 * 
	 * @return
	 */
	public Map<String, String> getStockCtrl() {
		Map<String, String> map = new HashMap<String, String>();
		try {
			String ctrl = SharedPreferencesForCarSalesUtil.getInstance(context)
					.getStorcCtrl();
			if (!TextUtils.isEmpty(ctrl)) {
				JSONObject obj = new JSONObject(ctrl);
				Iterator it = obj.keys();
				// 遍历jsonObject数据，添加到Map对象
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

	public void saveStockCtrl(Map<String, String> map) {
		if (map != null) {
			JSONObject obj = new JSONObject(map);
			SharedPreferencesForCarSalesUtil.getInstance(context).setStorcCtrl(
					obj.toString());
		}
	}

	/**
	 * 根据id存储从记录查询到详细页面时，首次的装卸车数量
	 * 
	 * @return
	 */
	public Map<String, String> getCtrlCount() {
		Map<String, String> map = new HashMap<String, String>();
		try {
			String ctrl = SharedPreferencesForCarSalesUtil.getInstance(context)
					.getListCtrlCount();
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

	public void saveCtrlCount(Map<String, String> map) {
		if (map != null) {
			JSONObject obj = new JSONObject(map);
			SharedPreferencesForCarSalesUtil.getInstance(context)
					.setListCtrlCount(obj.toString());
		}
	}

	/**
	 * 销售统计查询列表title
	 * 
	 * @return
	 */
	public List<String> carSalesStatisticsTitle() {
		List<String> list = new ArrayList<String>();
		List<CarSalesProductConf> confList = new CarSalesProductConfDB(context)
				.findListByType(CarSalesProductConf.TYPE_PRODUCT);
		for (int i = 0; i < confList.size(); i++) {
			CarSalesProductConf conf = confList.get(i);
			String name = conf.getName();
			list.add(name);
		}
		list.add("单位");
		list.add("数量");
		list.add("价格");
		return list;
	}

	/**
	 * 库存统计查询列表title
	 * 
	 * @return
	 */
	public List<String> carStockTitle() {
		List<String> list = new ArrayList<String>();
		List<CarSalesProductConf> confList = new CarSalesProductConfDB(context)
				.findListByType(CarSalesProductConf.TYPE_PRODUCT);
		for (int i = 0; i < confList.size(); i++) {
			CarSalesProductConf conf = confList.get(i);
			String name = conf.getName();
			list.add(name);
		}
		list.add("单位");
		list.add("库存");
		list.add("缺货");
		list.add("补货");

		return list;
	}

	/**
	 * 缺货统计查询列表title
	 * 
	 * @return
	 */
	public List<String> carStockOTitle() {
		List<String> list = new ArrayList<String>();
		List<CarSalesProductConf> confList = new CarSalesProductConfDB(context)
				.findListByType(CarSalesProductConf.TYPE_PRODUCT);
		for (int i = 0; i < confList.size(); i++) {
			CarSalesProductConf conf = confList.get(i);
			String name = conf.getName();
			list.add(name);
		}
		list.add("单位");
		list.add("库存");
		list.add("缺货");

		return list;
	}
	
}
