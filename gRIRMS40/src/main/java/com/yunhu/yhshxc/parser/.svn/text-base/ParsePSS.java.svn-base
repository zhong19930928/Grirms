package com.yunhu.yhshxc.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.database.DictDB;
import com.yunhu.yhshxc.database.OrderCacheDB;
import com.yunhu.yhshxc.database.PSSConfDB;
import com.yunhu.yhshxc.database.PSSProductConfDB;
import com.yunhu.yhshxc.order.bo.Inventory;
import com.yunhu.yhshxc.order.bo.Order;
import com.yunhu.yhshxc.order.bo.OrderCache;
import com.yunhu.yhshxc.order.bo.OrderItem;
import com.yunhu.yhshxc.order.bo.PSSConf;
import com.yunhu.yhshxc.order.bo.ProductConf;
import com.yunhu.yhshxc.order.bo.Returned;
import com.yunhu.yhshxc.utility.PublicUtils;

public class ParsePSS {
	
	private final String CONF = "conf";
	private final String PHONE_FUN = "phone_fun";
	private final String CREATE_ORDER_START_TIME = "order_time_begin";
	private final String CREATE_ORDER_END_TIME = "order_time_end";
	private final String SALES_START_TIME = "sales_time_begin";
	private final String SALES_END_TIME = "sales_time_end";
	private final String RETURNED_START_TIME = "returned_time_begin";
	private final String RETURNED_END_TIME = "returned_time_end";
	private final String RETURNED_REASON_DICT_TABLE = "returned_reason_module_id";
	private final String RETURNED_REASON_DICT_DATA_ID = "returned_reason_ctrl_id";
	private final String STOCKTAKE_DIFFER_DICT_TABLE = "stocktaking_dis_module_id";
	private final String STOCKTAKE_DIFFER_DICT_DATA_ID = "stocktaking_dis_ctrl_id";
	private final String ORDER_TIME_CONF = "order_time_conf";
	private final String ORDER_TIME_WEEKLY = "order_time_weekly";
	private final String SALES_TIME_CONF = "sales_time_conf";
	private final String SALES_TIME_WEEKLY = "sales_time_weekly";
	private final String RETURNED_TIME_CONF = "returned_time_conf";
	private final String RETURNED_TIME_WEEKLY = "returned_time_weekly";
	private final String IS_PRICE_EDIT = "is_price_edit";
	private final String ORDER_PRINT_STYLE = "order_print_style";//订单样式
	private final String STOCK_PRINT_STYLE = "stock_print_style";//进货样式
	
	
	private final String PRO = "pro";
	private final String DICT_TABLE = "dict_table";
	private final String DICT_DATA_ID = "dict_data_id";
	private final String DICT_COLS = "cols";
	private final String NEXT = "next";
	private final String NAME = "name";
	
	//----------字典表排序-----------
	public final String DICT_SORTS = "dict_sorts"; // 字典排序
	public final String DICT_SORTS_CLOS = "dict_sorts_clos"; // 字典排序列
	/**
	 * 订单缓存
	 */
	private final String ORDER_PRODUCT_ID="pro_id";//订单产品ID
	private final String ORDER_PRODUCT_NAME="pro_name";//订单产品名称
	private final String UNIT_PRICE="unit_price";//单价
	private final String AVAILABLE_ORDER_QUANTITY="available_order_quantity";//可订货数量
	private final String PERIOD_DATE="period_date";//数据时间段
	private final String TOTAL_SALES="total_sales";//总计销售量
	private final String PROJECTED_INVENTORY_QUANTITY="projected_inventory_quantity";//预计库存量
	private final String ORDER_QUANTITY="pro_order_count";//订货数量
	private final String PRO_INFO="pro_info";//产品信息
	
	private final String SEARCH_PRO_INFO = "search_pro_info"; //查出订单
	private final String ORDER_NO = "pro_order_num";//订单号
	private final String ORDER_CREATE_DATE = "pro_order_time";//订单创建日期
	
	private final String PRO_NAME = "pro_name";//产品实际进货数量
	private final String PRO_ARRIVAL_QUANTITY = "pro_stock_count";//产品实际进货数量
	private final String PRO_ORDER_TOTAL = "pro_order_count"; //订货数量
	private final String PRO_ORDER_PRICE = "pro_order_price"; //单价
	private final String PRO_ORDER_STATUS = "pro_order_status"; //产品状态
	private final String PRO_ID = "pro_id"; //产品ID
	private final String ID = "id"; //唯一ID
	
	private final String RETURNED_INFO = "returned_info";
	private final String RETURNED_NO = "pro_returned_num";//订单号
	private final String RETURNED_CREATE_DATE = "pro_returned_time";//产品实际进货数量
	
	private final String PRO_RETURNED_INFO = "pro_returned_info";
	private final String PRO_RETURNED_QUANTITY = "pro_returned_count"; //退货数量
	//private final String PRO_RETURNED_PRICE = "pro_returned_price";
	private final String PRO_RETURNED_STATUS = "status";
	private final String PRO_RETURNED_REASON = "pro_returned_reason";
	
	/**
	 * 库存管理
	 */
	private final String INVENTORY_INFO="inventory_info";
	private final String INVENTORY_STOCK_COUNT="stock_count";//有效进货
	private final String INVENTORY_RETURNED_COUNT="returned_count";//期间退货
	private final String INVENTORY_REPORT_TIME="report_time";//时间
	private final String INVENTORY_PRO_ID="pro_id";//产品ID
	private final String INVENTORY_COUNT="inventory_count";//上次盘点
	private final String INVENTORY_TOTAL_SALES="total_sales";//总销量
	private final String INVENTORY_PRO_NAME="pro_name";//产品 名称
	
	private Context context=null;

	public ParsePSS(Context context) {
		this.context = context;
	}

	public void pssconfig(String json) throws JSONException{
		JSONObject jsonObject = new JSONObject(json);
		if(PublicUtils.isValid(jsonObject, CONF)){
			JSONObject confObject = jsonObject.getJSONObject(CONF);
			PSSConf conf = new PSSConf();
			if(PublicUtils.isValid(confObject, PHONE_FUN)){
				conf.setPhoneFun(confObject.getString(PHONE_FUN));
			}
			if(PublicUtils.isValid(confObject, CREATE_ORDER_START_TIME)){
				conf.setCreateOrderStartTime(confObject.getString(CREATE_ORDER_START_TIME));
			}
			if(PublicUtils.isValid(confObject, CREATE_ORDER_END_TIME)){
				conf.setCreateOrderEndTime(confObject.getString(CREATE_ORDER_END_TIME));
			}
			if(PublicUtils.isValid(confObject, SALES_START_TIME)){
				conf.setSalesStartTime(confObject.getString(SALES_START_TIME));
			}
			if(PublicUtils.isValid(confObject, SALES_END_TIME)){
				conf.setSalesEndTime(confObject.getString(SALES_END_TIME));
			}
			if(PublicUtils.isValid(confObject, RETURNED_START_TIME)){
				conf.setReturnedStartTime(confObject.getString(RETURNED_START_TIME));
			}
			if(PublicUtils.isValid(confObject, RETURNED_END_TIME)){
				conf.setReturnedEndTime(confObject.getString(RETURNED_END_TIME));
			}
			if(PublicUtils.isValid(confObject, RETURNED_REASON_DICT_TABLE)){
				conf.setReturnedReasonDictTable(confObject.getString(RETURNED_REASON_DICT_TABLE));
			}
			if(PublicUtils.isValid(confObject, RETURNED_REASON_DICT_DATA_ID)){
				conf.setReturnedReasonDictDataId(confObject.getString(RETURNED_REASON_DICT_DATA_ID));
			}
			if(PublicUtils.isValid(confObject, STOCKTAKE_DIFFER_DICT_TABLE)){
				conf.setStocktakeDifferDictTable(confObject.getString(STOCKTAKE_DIFFER_DICT_TABLE));
			}
			if(PublicUtils.isValid(confObject, STOCKTAKE_DIFFER_DICT_DATA_ID)){
				conf.setStocktakeDifferDictDataId(confObject.getString(STOCKTAKE_DIFFER_DICT_DATA_ID));
			}
			if(PublicUtils.isValid(confObject, ORDER_TIME_CONF)){
				conf.setCreateOrderTimeConf(confObject.getString(ORDER_TIME_CONF));
			}
			if(PublicUtils.isValid(confObject, ORDER_TIME_WEEKLY)){
				conf.setCreateOrderTimeWeekly(confObject.getString(ORDER_TIME_WEEKLY));
			}
			if(PublicUtils.isValid(confObject, SALES_TIME_CONF)){
				conf.setSalesTimeConf(confObject.getString(SALES_TIME_CONF));
			}
			if(PublicUtils.isValid(confObject, SALES_TIME_WEEKLY)){
				conf.setSalesTimeWeekly(confObject.getString(SALES_TIME_WEEKLY));
			}
			if(PublicUtils.isValid(confObject, RETURNED_TIME_CONF)){
				conf.setReturnedTimeConf(confObject.getString(RETURNED_TIME_CONF));
			}
			if(PublicUtils.isValid(confObject, RETURNED_TIME_WEEKLY)){
				conf.setReturnedTimeWeekly(confObject.getString(RETURNED_TIME_WEEKLY));
			}
			if(PublicUtils.isValid(confObject, IS_PRICE_EDIT)){
				conf.setIsPriceEdit(confObject.getString(IS_PRICE_EDIT));
			}
			
			if(PublicUtils.isValid(confObject, ORDER_PRINT_STYLE)){
				conf.setOrderPrintStyle(confObject.getString(ORDER_PRINT_STYLE));
			}
			
			if(PublicUtils.isValid(confObject, STOCK_PRINT_STYLE)){
				conf.setStockPrintStyle(confObject.getString(STOCK_PRINT_STYLE));
			}
			if(PublicUtils.isValid(confObject, DICT_SORTS)){
				conf.setDictIsAsc(confObject.getString(DICT_SORTS));
			}
			if(PublicUtils.isValid(confObject, DICT_SORTS_CLOS)){
				conf.setDictOrderBy(confObject.getString(DICT_SORTS_CLOS));
			}
			
			PSSConfDB db = new PSSConfDB(context);
			db.delete();
			db.insert(conf);
		}
		if(PublicUtils.isValid(jsonObject, PRO)){
			JSONArray confArr = jsonObject.getJSONArray(PRO);
			int len = confArr.length();
			ProductConf conf = null;
			JSONObject proObject = null;
			PSSProductConfDB db = new PSSProductConfDB(context);
			db.delete();
			for(int i=0;i<len;i++){
				proObject = confArr.getJSONObject(i);
				conf = new ProductConf();
				if(PublicUtils.isValid(proObject, DICT_TABLE)){
					conf.setDictTable(proObject.getString(DICT_TABLE));
				}
				if(PublicUtils.isValid(proObject, DICT_DATA_ID)){
					conf.setDictDataId(proObject.getString(DICT_DATA_ID));
				}
				if(PublicUtils.isValid(proObject, DICT_COLS)){
					conf.setDictCols(proObject.getString(DICT_COLS));
				}
				if(PublicUtils.isValid(proObject, NEXT)){
					conf.setNext(proObject.getString(NEXT));
				}
				if(PublicUtils.isValid(proObject, NAME)){
					conf.setName(proObject.getString(NAME));
				}
				if(PublicUtils.isValid(proObject, DICT_SORTS)){
					conf.setDictIsAsc(proObject.getString(DICT_SORTS));
				}
				if(PublicUtils.isValid(proObject, DICT_SORTS_CLOS)){
					conf.setDictOrderBy(proObject.getString(DICT_SORTS_CLOS));
				}
				db.insert(conf);
			}
		}
	}

	/**
	 * 解析产品
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public List<OrderCache> getOrderCacheList(String json) throws JSONException{
		List<OrderCache> orderCacheList=new ArrayList<OrderCache>();
		if(!TextUtils.isEmpty(json)){
			OrderCacheDB orderCacheDB=new OrderCacheDB(context);
			JSONObject jsonObject=new JSONObject(json);
			if(!PublicUtils.isValid(jsonObject, PRO_INFO)){
				return orderCacheList;
			}
			JSONArray arr=jsonObject.getJSONArray(PRO_INFO);
			JSONObject obj=null;
			OrderCache orderCach=null;
			int len=arr.length();
			for (int i = 0; i < len; i++) {
				obj=arr.getJSONObject(i);
				orderCach=new OrderCache();
				if(PublicUtils.isValid(obj, ORDER_PRODUCT_ID)){
					orderCach.setOrderProductId(Integer.parseInt(obj.getString(ORDER_PRODUCT_ID)));
				}
				if(PublicUtils.isValid(obj, ORDER_PRODUCT_NAME)){
					orderCach.setOrderProductName(obj.getString(ORDER_PRODUCT_NAME));
				}
				if(PublicUtils.isValid(obj, UNIT_PRICE)){
					orderCach.setUnitPrice(Double.parseDouble(obj.getString(UNIT_PRICE)));
				}
				if(PublicUtils.isValid(obj, AVAILABLE_ORDER_QUANTITY)){
					orderCach.setAvailableOrderQuantity(Long.parseLong(obj.getString(AVAILABLE_ORDER_QUANTITY)));
				}
				if(PublicUtils.isValid(obj, AVAILABLE_ORDER_QUANTITY)){
					orderCach.setAvailableOrderQuantity(Long.parseLong(obj.getString(AVAILABLE_ORDER_QUANTITY)));
				}
				if(PublicUtils.isValid(obj, PERIOD_DATE)){
					orderCach.setPeriodDate(obj.getString(PERIOD_DATE));
				}
				if(PublicUtils.isValid(obj, TOTAL_SALES)){
					orderCach.setTotalSales(Long.parseLong(obj.getString(TOTAL_SALES)));
				}
				if(PublicUtils.isValid(obj, PROJECTED_INVENTORY_QUANTITY)){
					orderCach.setProjectedInventoryQuantity(Long.parseLong(obj.getString(PROJECTED_INVENTORY_QUANTITY)));
				}
				if(PublicUtils.isValid(obj, ORDER_QUANTITY)){
					orderCach.setOrderQuantity(Long.parseLong(obj.getString(ORDER_QUANTITY)));
				}
				orderCacheList.add(orderCach);
				orderCacheDB.insert(orderCach);
			}
		}
		return orderCacheList;
	}
	
	/**
	 * 解析产品
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public OrderCache getOrderCacheInfo(String json) throws JSONException{
		OrderCache orderCache=null;
		if(!TextUtils.isEmpty(json)){
			JSONObject jsonObject=new JSONObject(json);
			if(!PublicUtils.isValid(jsonObject, PRO_INFO)){
				return orderCache;
			}
			JSONArray arr=jsonObject.getJSONArray(PRO_INFO);
			JSONObject obj=null;
			int len=arr.length();
			if(len>0){
				obj=arr.getJSONObject(0);
				orderCache=new OrderCache();
				if(PublicUtils.isValid(obj, ORDER_PRODUCT_ID)){
					orderCache.setOrderProductId(Integer.parseInt(obj.getString(ORDER_PRODUCT_ID)));
				}
				if(PublicUtils.isValid(obj, ORDER_PRODUCT_NAME)){
					orderCache.setOrderProductName(obj.getString(ORDER_PRODUCT_NAME));
				}
				if(PublicUtils.isValid(obj, UNIT_PRICE)){
					orderCache.setUnitPrice(Double.parseDouble(obj.getString(UNIT_PRICE)));
				}
				if(PublicUtils.isValid(obj, AVAILABLE_ORDER_QUANTITY)){
					orderCache.setAvailableOrderQuantity(Long.parseLong(obj.getString(AVAILABLE_ORDER_QUANTITY)));
				}
				if(PublicUtils.isValid(obj, AVAILABLE_ORDER_QUANTITY)){
					orderCache.setAvailableOrderQuantity(Long.parseLong(obj.getString(AVAILABLE_ORDER_QUANTITY)));
				}
				if(PublicUtils.isValid(obj, PERIOD_DATE)){
					orderCache.setPeriodDate(obj.getString(PERIOD_DATE));
				}
				if(PublicUtils.isValid(obj, TOTAL_SALES)){
					orderCache.setTotalSales(Long.parseLong(obj.getString(TOTAL_SALES)));
				}
				if(PublicUtils.isValid(obj, PROJECTED_INVENTORY_QUANTITY)){
					orderCache.setProjectedInventoryQuantity(Long.parseLong(obj.getString(PROJECTED_INVENTORY_QUANTITY)));
				}
				if(PublicUtils.isValid(obj, ORDER_QUANTITY)){
					orderCache.setOrderQuantity(Long.parseLong(obj.getString(ORDER_QUANTITY)));
				}
			}
		}
		return orderCache;
	}
	
	/**
	 * 查询
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public List<Order> getOrderList(String json) throws JSONException{
		List<Order> orderList=new ArrayList<Order>();
		JSONObject jsonObject = new JSONObject(json);
		if (!jsonObject.has(SEARCH_PRO_INFO))
			return orderList;
		
		JSONArray orderArr = jsonObject.getJSONArray(SEARCH_PRO_INFO);
		int len = orderArr.length();
		JSONObject orderObject = null;
		Order order = null;
		for(int i=0;i<len;i++){
			
			orderObject = orderArr.getJSONObject(i);
			order = new Order();
			if(PublicUtils.isValid(orderObject, ORDER_NO)){
				order.setOrderNo(orderObject.getString(ORDER_NO));
			}
			if(PublicUtils.isValid(orderObject, ORDER_CREATE_DATE)){
				order.setDate(orderObject.getString(ORDER_CREATE_DATE));
			}
			if(PublicUtils.isValid(orderObject, PRO_INFO)){
				order.setItems(getProList(orderObject.getJSONArray(PRO_INFO)));
			}
			orderList.add(order);
		}
		
		return orderList;
	}
	
	private List<OrderItem> getProList(JSONArray proArr) throws JSONException{
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		int len = proArr.length();
		JSONObject object = null;
		OrderItem orderItem = null;
		for(int i=0; i<len; i++){
			object = proArr.getJSONObject(i);
			orderItem = new OrderItem();
			if(PublicUtils.isValid(object, PRO_NAME)){
				orderItem.setName(object.getString(PRO_NAME));
			}
			if(PublicUtils.isValid(object, PRO_ARRIVAL_QUANTITY)){
				orderItem.setArrivalQuantity(object.getLong(PRO_ARRIVAL_QUANTITY));
			}
			if(PublicUtils.isValid(object, PRO_ORDER_PRICE)){
				orderItem.setPrice(object.getDouble(PRO_ORDER_PRICE));
			}
			if(PublicUtils.isValid(object, PRO_ORDER_STATUS)){
				orderItem.setState(object.getInt(PRO_ORDER_STATUS));
			}
			if(PublicUtils.isValid(object, PRO_ORDER_TOTAL)){
				orderItem.setTotal(object.getLong(PRO_ORDER_TOTAL));
			}
			if(PublicUtils.isValid(object, PRO_ID)){
				orderItem.setProductId(object.getInt(PRO_ID));
			}
			if(PublicUtils.isValid(object, ID)){
				orderItem.setId(object.getInt(ID));
			}
			orderItemList.add(orderItem);
		}
		return orderItemList;
	}
	
	public List<Order> getReturnedList(String json) throws JSONException{
		List<Order> orderList=new ArrayList<Order>();
		JSONObject jsonObject = new JSONObject(json);
		if (!jsonObject.has(RETURNED_INFO))
			return orderList;
		JSONArray orderArr = jsonObject.getJSONArray(RETURNED_INFO);
		int len = orderArr.length();
		JSONObject orderObject = null;
		Order order = null;
		for(int i=0;i<len;i++){
			orderObject = orderArr.getJSONObject(i);
			order = new Order();
			if(PublicUtils.isValid(orderObject, RETURNED_NO)){
				order.setOrderNo(orderObject.getString(RETURNED_NO));
			}
			if(PublicUtils.isValid(orderObject, RETURNED_CREATE_DATE)){
				order.setDate(orderObject.getString(RETURNED_CREATE_DATE));
			}
			if(PublicUtils.isValid(orderObject, PRO_RETURNED_INFO)){
				order.setReturneds(getProReturnedList(orderObject.getJSONArray(PRO_RETURNED_INFO)));
			}
			orderList.add(order);
		}
		
		return orderList;
	}
	
	private List<Returned> getProReturnedList(JSONArray proArr) throws JSONException{
		List<Returned> returnedList = new ArrayList<Returned>();
		int len = proArr.length();
		JSONObject object = null;
		Returned returned = null;
		for(int i=0; i<len; i++){
			object = proArr.getJSONObject(i);
			returned = new Returned();
			if(PublicUtils.isValid(object, PRO_NAME)){
				returned.setName(object.getString(PRO_NAME));
			}
			if(PublicUtils.isValid(object, PRO_RETURNED_QUANTITY)){
				returned.setReturnedQuantity(object.getLong(PRO_RETURNED_QUANTITY));
			}
			if(PublicUtils.isValid(object,PRO_RETURNED_REASON)){
				String did = object.getString(PRO_RETURNED_REASON);
				PSSConf conf = new PSSConfDB(context).findPSSConf();
				Dictionary dict = new DictDB(context).findDictListByTable(conf.getReturnedReasonDictTable(), conf.getReturnedReasonDictDataId(), did);
				returned.setReason(dict);
			}
			if(PublicUtils.isValid(object, PRO_RETURNED_STATUS)){
				returned.setState(object.getInt(PRO_RETURNED_STATUS));
			}
			if(PublicUtils.isValid(object, PRO_ID)){
				returned.setProductId(object.getInt(PRO_ID));
			}
			if(PublicUtils.isValid(object, ID)){
				returned.setId(object.getInt(ID));
			}
			returnedList.add(returned);
		}
		return returnedList;
	}
	
	/**
	 * 获取库存管理列表
	 * @param json
	 * @return
	 * @throws JSONException 
	 */
	public List<Inventory> getInventoryList(String json) throws JSONException{
		List<Inventory> inventoryList=new ArrayList<Inventory>();
		JSONObject obj=new JSONObject(json);
		if(!PublicUtils.isValid(obj, INVENTORY_INFO)){
			return inventoryList;
		}
		JSONArray array=obj.getJSONArray(INVENTORY_INFO);
		JSONObject jsonObject=null;
		Inventory inventory=null;
		int len=array.length();
		if(len>0){
			for (int i = 0; i < len; i++) {
				inventory=new Inventory();
				jsonObject=array.getJSONObject(i);
				if(PublicUtils.isValid(jsonObject, INVENTORY_STOCK_COUNT)){
					inventory.setStockCount(jsonObject.getLong(INVENTORY_STOCK_COUNT));
				}
				if(PublicUtils.isValid(jsonObject, INVENTORY_RETURNED_COUNT)){
					inventory.setReturnedCount(jsonObject.getLong(INVENTORY_RETURNED_COUNT));
				}
				if(PublicUtils.isValid(jsonObject, INVENTORY_REPORT_TIME)){
					inventory.setReportTime(jsonObject.getString(INVENTORY_REPORT_TIME));
				}
				if(PublicUtils.isValid(jsonObject, INVENTORY_PRO_ID)){
					inventory.setProductId(jsonObject.getInt(INVENTORY_PRO_ID));
				}
				if(PublicUtils.isValid(jsonObject, INVENTORY_COUNT)){
					inventory.setInventoryCount(jsonObject.getLong(INVENTORY_COUNT));
				}
				if(PublicUtils.isValid(jsonObject, INVENTORY_TOTAL_SALES)){
					inventory.setTotalSales(jsonObject.getLong(INVENTORY_TOTAL_SALES));
				}
				if(PublicUtils.isValid(jsonObject, INVENTORY_PRO_NAME)){
					inventory.setProductName(jsonObject.getString(INVENTORY_PRO_NAME));
				}
				inventoryList.add(inventory);
			}
		}
		return inventoryList;
	}
	
}
