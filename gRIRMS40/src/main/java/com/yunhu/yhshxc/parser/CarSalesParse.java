package com.yunhu.yhshxc.parser;

import gcg.org.debug.JLog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.carSales.bo.Arrears;
import com.yunhu.yhshxc.activity.carSales.bo.CarSaleSalesVolume;
import com.yunhu.yhshxc.activity.carSales.bo.CarSales;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesContact;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesDebtDetail;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesDebtListItem;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesProduct;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesProductConf;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesProductData;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesPromotion;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesStock;
import com.yunhu.yhshxc.activity.carSales.db.CarSalesDB;
import com.yunhu.yhshxc.activity.carSales.db.CarSalesProductConfDB;
import com.yunhu.yhshxc.activity.carSales.db.CarSalesProductDataDB;
import com.yunhu.yhshxc.activity.carSales.db.CarSalesPromotionDB;
import com.yunhu.yhshxc.activity.carSales.db.CarSalesStockDB;
import com.yunhu.yhshxc.activity.carSales.util.CarSalesUtil;
import com.yunhu.yhshxc.activity.carSales.util.SharedPreferencesForCarSalesUtil;
import com.yunhu.yhshxc.attendance.Scheduling;
import com.yunhu.yhshxc.attendance.attendCalendar.AttendanceInfo;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.database.DatabaseHelper;
import com.yunhu.yhshxc.database.MainMenuDB;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.PublicUtils;

public class CarSalesParse {
    private final String CAR_CONF = "car_conf";
    private final String PRODUCT_CTRL = "product_ctrl";
    private final String TAB = "tab";
    private final String CTRL = "ctrl";
    private final String NAME = "name";
    private final String COLS = "cols";
    private final String NEXT = "next";
    private final String CODE_CTRL = "code_ctrl";// 二维码控件ID
    private final String UNIT_CTRL = "unit_ctrl";// 计量单位控件ID
    private final String PRICE_CTRL = "price_ctrl";// 价格控件ID
    private final String IS_PHONE_PRICE = "is_phone_price";// 手机端价格的显示控制(1:不显示,2:不可修改,3:任意修改,4:只能高,5:只能低
    // 默认是 3)
    private final String IS_PROMOTION = "is_promotion";// 是否需要促销模块 1不要 2要 默认2
    private final String IS_CUSTOMER = "is_customer";// 是否需要订货联系人 1不要 2要 默认是2
    private final String IS_SALES = "is_sales";// 是否需要打印销售单，1不需要 2需要 默认是2
    private final String IS_LODING = "is_loding";// 是否需要打印装车单，1不需要 2需要 默认是2
    private final String IS_TRUCK = "is_truck";// 是否需要打印卸车单，1不需要 2需要 默认是2
    private final String IS_RECEIVABLES = "is_receivables";// 是否需要打印收款单，1不需要 2需要
    // 默认是2
    private final String IS_RETURN = "is_return";// 是否需要打印退货单，1不需要 2需要 默认是2
    private final String IS_SALES_PHOTO = "is_sales_photo";// 是否需要销售拍照，1不需要 2需要
    // 默认是2
    private final String IS_LODING_PHOTO = "is_loding_photo";// 是否需要装车拍照，1不需要
    // 2需要 默认是2
    private final String IS_TRUCK_PHOTO = "is_truck_photo";// 是否需要卸车拍照，1不需要 2需要
    // 默认是2
    private final String IS_RECEIVABLES_PHOTO = "is_receivables_photo";// 是否需要收款拍照，1不需要
    // 2需要
    // 默认是2
    private final String IS_RETURN_PHOTO = "is_return_photo";// 是否需要退货拍照，1不需要
    // 2需要 默认是2

    private final String SALES_COUNT = "sales_count";// 当天提交的订单个数
    private final String BASE_FUNC = "basefunc";
    public final String DICT = "dict";
    public final String LINK_MOD = "link_mod";// 拓展模块数据Id
    public final String LINK_NAME = "link_name";// 拓展模块名称
    private final String ENABLE_TIME = "enable_time"; // 下订单可用时间,
    public final String CAR_PROMOTION = "car_promotion";
    public final String STOCK_DETAIL = "stockDetail";

    private final String CAR_ID = "car_id";
    private final String CAR_NO = "car_no";// 车牌号
    private final String CAR_DPM = "car_dpm";// 车辆所属机构

    private final String IS_HAS_COST = "is_has_cost";// 是否需要费用报销，0不需要 1需要 默认是0
    private final String IS_HAS_STOCK = "is_has_stock";// 是否需要库存盘点，0不需要 1需要 默认是0
    private final String IS_HAS_SALES = "is_has_sales";// 是否需要卖货和收取欠款，0不需要 1需要
    // 默认是0
    private final String IS_HAS_FILL = "is_has_fill";// 是否需要补货申请，0不需要 1需要 默认是0
    private final String IS_HAS_LODING = "is_has_loding";// 是否需要装车和装车记录，0不需要 1需要
    // 默认是0
    private final String IS_HAS_TRUCK = "is_has_truck";// 是否需要卸车和卸车记录，0不需要 1需要
    // 默认是0
    private final String IS_HAS_RETURN = "is_has_return";// 是否需要退货，0不需要 1需要 默认是0

    private final String MIN_NUM = "price_min";// 下单每个单品数量的最小值
    private final String MAX_NUM = "price_max";// 下单每个单品数量的最大值

    private Context context;
    private CacheData cacheData = null;

    private CarSalesUtil carSalesUtil;

    public CarSalesParse(Context mContext) {
        this.context = mContext;
        cacheData = new CacheData(mContext);
        carSalesUtil = new CarSalesUtil(mContext);
    }

    public void parserAll(JSONObject obj) throws JSONException {
        if (PublicUtils.isValid(obj, CAR_CONF)) {
            JSONObject confObj = obj.getJSONObject(CAR_CONF);
            parserCarSales(confObj);
        }

        if (PublicUtils.isValid(obj, CAR_PROMOTION)) {
            JSONArray promotionArray = obj.getJSONArray(CAR_PROMOTION);
            parserProductPromotion(promotionArray);
        }

        if (PublicUtils.isValid(obj, STOCK_DETAIL)) {
            JSONArray array = obj.getJSONArray(STOCK_DETAIL);
            parserStockDetail(array);
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

    public void parserCarSales(JSONObject obj) throws JSONException {
        if (obj != null) {
            if (PublicUtils.isValid(obj, CODE_CTRL)) {
                String codeCtrl = obj.getString(CODE_CTRL);
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setCodeCtrl(codeCtrl);
            } else {
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setCodeCtrl("");
            }
            if (PublicUtils.isValid(obj, PRICE_CTRL)) {
                String priceCtrl = obj.getString(PRICE_CTRL);
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setPriceCtrl(priceCtrl);
            } else {
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setPriceCtrl("");
            }

            if (PublicUtils.isValid(obj, IS_SALES)) {
                String isSales = obj.getString(IS_SALES);
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setIsSales(isSales);
            } else {
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setIsSales("2");
            }

            if (PublicUtils.isValid(obj, IS_LODING)) {
                String isLoding = obj.getString(IS_LODING);
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setIsLoding(isLoding);
            } else {
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setIsLoding("2");
            }

            if (PublicUtils.isValid(obj, IS_TRUCK)) {
                String isTruck = obj.getString(IS_TRUCK);
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setIsTruck(isTruck);
            } else {
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setIsTruck("2");
            }

            if (PublicUtils.isValid(obj, IS_RECEIVABLES)) {
                String isReceivables = obj.getString(IS_RECEIVABLES);
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setIsReceivables(isReceivables);
            } else {
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setIsReceivables("2");
            }

            if (PublicUtils.isValid(obj, IS_RETURN)) {
                String isReturn = obj.getString(IS_RETURN);
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setIsReturn(isReturn);
            } else {
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setIsReturn("2");
            }

            if (PublicUtils.isValid(obj, IS_SALES_PHOTO)) {
                String isSalesPhoto = obj.getString(IS_SALES_PHOTO);
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setIsSalesPhoto(isSalesPhoto);
            } else {
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setIsSalesPhoto("2");
            }

            if (PublicUtils.isValid(obj, IS_LODING_PHOTO)) {
                String isLodingPhoto = obj.getString(IS_LODING_PHOTO);
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setIsLodingPhoto(isLodingPhoto);
            } else {
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setIsLodingPhoto("2");
            }

            if (PublicUtils.isValid(obj, IS_TRUCK_PHOTO)) {
                String isTruckPhoto = obj.getString(IS_TRUCK_PHOTO);
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setIsTruckPhoto(isTruckPhoto);
            } else {
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setIsTruckPhoto("2");
            }

            if (PublicUtils.isValid(obj, IS_RECEIVABLES_PHOTO)) {
                String isReceivablesPhoto = obj.getString(IS_RECEIVABLES_PHOTO);
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setIsReceivablesPhoto(isReceivablesPhoto);
            } else {
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setIsReceivablesPhoto("2");
            }

            if (PublicUtils.isValid(obj, IS_RETURN_PHOTO)) {
                String isReturnPhoto = obj.getString(IS_RETURN_PHOTO);
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setIsReturnPhoto(isReturnPhoto);
            } else {
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setIsReturnPhoto("2");
            }

            if (PublicUtils.isValid(obj, IS_PHONE_PRICE)) {
                String value = obj.getString(IS_PHONE_PRICE);
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setIsPhonePrice(Integer.parseInt(value));
            }
            if (PublicUtils.isValid(obj, IS_CUSTOMER)) {
                String value = obj.getString(IS_CUSTOMER);
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setIsCarSalesUser(Integer.parseInt(value));
            }
            if (PublicUtils.isValid(obj, IS_PROMOTION)) {
                String value = obj.getString(IS_PROMOTION);
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setIsPromotion(Integer.parseInt(value));
            }

            if (PublicUtils.isValid(obj, MIN_NUM)) {
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setMinNum(String.valueOf(obj.getInt(MIN_NUM)));
            } else {
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setMinNum("0");
            }
            if (PublicUtils.isValid(obj, MAX_NUM)) {
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setMaxNum(String.valueOf(obj.getInt(MAX_NUM)));
            } else {
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setMaxNum("0");
            }

            if (PublicUtils.isValid(obj, LINK_MOD)) {
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setLinkMod(obj.getInt(LINK_MOD));
            } else {
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setLinkMod(0);
            }

            if (PublicUtils.isValid(obj, LINK_NAME)) {
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setLinkName(obj.getString(LINK_NAME));
            } else {
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setLinkName(context.getResources().getString(R.string.parser_cache_07));
            }

            if (PublicUtils.isValid(obj, ENABLE_TIME)) {
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setEnableTime(obj.getString(ENABLE_TIME));
            } else {
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setEnableTime("");
            }

            if (PublicUtils.isValid(obj, CAR_ID)) {
                SharedPreferencesForCarSalesUtil.getInstance(context).setCarId(
                        obj.getInt(CAR_ID));
            } else {
                SharedPreferencesForCarSalesUtil.getInstance(context).setCarId(
                        0);
            }

            if (PublicUtils.isValid(obj, CAR_NO)) {
                SharedPreferencesForCarSalesUtil.getInstance(context).setCarNo(
                        obj.getString(CAR_NO));
            } else {
                SharedPreferencesForCarSalesUtil.getInstance(context).setCarNo(
                        "");
            }

            if (PublicUtils.isValid(obj, CAR_DPM)) {
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setCarDpm(obj.getString(CAR_DPM));
            } else {
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setCarDpm("");
            }

            if (PublicUtils.isValid(obj, IS_HAS_COST)) {
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setIsHasCost(obj.getString(IS_HAS_COST));
            } else {
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setIsHasCost("0");
            }

            if (PublicUtils.isValid(obj, IS_HAS_STOCK)) {
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setIsHasStock(obj.getString(IS_HAS_STOCK));
            } else {
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setIsHasStock("0");
            }
            if (PublicUtils.isValid(obj, IS_HAS_SALES)) {
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setIsHasSales(obj.getString(IS_HAS_SALES));
            } else {
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setIsHasSales("0");
            }
            if (PublicUtils.isValid(obj, IS_HAS_FILL)) {
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setIsHasFill(obj.getString(IS_HAS_FILL));
            } else {
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setIsHasFill("0");
            }
            if (PublicUtils.isValid(obj, IS_HAS_LODING)) {
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setIsHasLoding(obj.getString(IS_HAS_LODING));
            } else {
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setIsHasLoding("0");
            }
            if (PublicUtils.isValid(obj, IS_HAS_TRUCK)) {
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setIsHasTruck(obj.getString(IS_HAS_TRUCK));
            } else {
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setIsHasTruck("0");
            }
            if (PublicUtils.isValid(obj, IS_HAS_RETURN)) {
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setIsHasReturn(obj.getString(IS_HAS_RETURN));
            } else {
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setIsHasReturn("0");
            }

            if (PublicUtils.isValid(obj, STOCK_DETAIL)) {
                JSONArray array = obj.getJSONArray(STOCK_DETAIL);
                parserStockDetail(array);
            }

            CarSalesProductConfDB confDB = new CarSalesProductConfDB(context);
            confDB.delete();
            if (PublicUtils.isValid(obj, PRODUCT_CTRL)) {
                JSONArray array = obj.getJSONArray(PRODUCT_CTRL);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject cObj = array.getJSONObject(i);
                    CarSalesProductConf conf = parserProductConf(cObj,
                            CarSalesProductConf.TYPE_PRODUCT);
                    confDB.insert(conf);
                }
            }
            if (PublicUtils.isValid(obj, UNIT_CTRL)) {
                JSONObject cObj = obj.getJSONObject(UNIT_CTRL);
                CarSalesProductConf conf = parserProductConf(cObj,
                        CarSalesProductConf.TYPE_UNIT);
                confDB.insert(conf);
            }
            if (PublicUtils.isValid(obj, SALES_COUNT)) {
                int count = obj.getInt(SALES_COUNT);
                SharedPreferencesForCarSalesUtil.getInstance(context)
                        .setCarSalesCount(DateUtil.getCurDate() + "_" + count);
            }

            MainMenuDB db = new MainMenuDB(context);
            Menu menu = db.findMenuListByMenuType(Menu.TYPE_CAR_SALES);
            if (menu != null) {
                menu.setPhoneUsableTime(SharedPreferencesForCarSalesUtil
                        .getInstance(context).getEnableTime());
                db.updateMenuById(menu);
            }
        }
    }

    private CarSalesProductConf parserProductConf(JSONObject cObj, int type)
            throws JSONException {
        CarSalesProductConf conf = new CarSalesProductConf();
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

    public void parserProductPromotion(JSONArray array) throws JSONException {
        if (array != null) {
            CarSalesPromotionDB db = new CarSalesPromotionDB(context);
            db.delete();
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                CarSalesPromotion conf = new CarSalesPromotion();
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
                    conf.setIsDouble(Integer.parseInt(obj
                            .getString("is_double")));
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
                    conf.setDisAmount(Double.parseDouble(obj
                            .getString("dis_amount")));
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
    }

    /**
     * 解析销售统计列表数据
     *
     * @param json
     * @return
     * @throws JSONException
     */
    public List<CarSaleSalesVolume> parserSalesListItem(JSONArray array)
            throws JSONException {
        List<CarSaleSalesVolume> itemList = new ArrayList<CarSaleSalesVolume>();
        CarSalesUtil carSalesUtil = new CarSalesUtil(context);
        List<String> list_name = carSalesUtil.carSalesStatisticsTitle();
        CarSaleSalesVolume carSaleSalesVolume_name = new CarSaleSalesVolume();
        carSaleSalesVolume_name.setProductClass(list_name);
        itemList.add(carSaleSalesVolume_name);
        if (array != null && array.length() > 0) {
            for (int i = 0; i < array.length(); i++) {
                JSONArray itemArray = array.getJSONArray(i);
                CarSaleSalesVolume carSaleSalesVolume = new CarSaleSalesVolume();
                List<String> list = new ArrayList<String>();
                for (int j = 0; j < itemArray.length(); j++) {
                    String item = "";
                    if (j == (itemArray.length() - 2)
                            || j == (itemArray.length() - 1)) {
                        float m = Float.parseFloat(itemArray.getString(j));
                        item = PublicUtils.formatDouble(m);
                    } else {
                        item = itemArray.getString(j);
                    }
                    if ("null".equals(item)) {
                        list.add("");
                    } else {
                        list.add(item);
                    }
                }
                carSaleSalesVolume.setProductClass(list);
                itemList.add(carSaleSalesVolume);

            }

        }
        return itemList;
    }

    /**
     * 解析排班查询数据
     */

    public List<Scheduling> parserSearchListItem(JSONArray array)
            throws JSONException {
        List<Scheduling> itemList = new ArrayList<Scheduling>();
        if (array != null && array.length() > 0) {
            for (int i = 0; i < array.length(); i++) {
                JSONObject searchObj = array.getJSONObject(i);
                Scheduling scheduling = new Scheduling();

                if (PublicUtils.isValid(searchObj, "userName")) {
                    scheduling.setName(searchObj.getString("userName"));
                } else {
                    continue;
                }
                if (PublicUtils.isValid(searchObj, "startTime")) {
                    scheduling.setStartTime(searchObj.getString("startTime"));
                } else {
                    continue;
                }
                if (PublicUtils.isValid(searchObj, "endTime")) {
                    scheduling.setEndTimel(searchObj.getString("endTime"));
                } else {
                    continue;
                }
                if (PublicUtils.isValid(searchObj, "detail")) {
                    JSONArray searchArray = searchObj.getJSONArray("detail");
                    scheduling.setArray(searchArray);
                } else {
                    continue;
                }
                itemList.add(scheduling);
            }

        }
        for (int i = 0; i < itemList.size(); i++) {
            JLog.d("abby", "itemList" + itemList.get(i).getName());
        }

        return itemList;
    }

    /**
     * 解析考勤查询记录信息 ，一个月的
     */
    public List<AttendanceInfo> parserSearchAttendInfoItem(JSONObject obj)
            throws JSONException {
        List<AttendanceInfo> itemList = new ArrayList<AttendanceInfo>();
        Iterator<String> keys = obj.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            AttendanceInfo attendance = new AttendanceInfo();
            attendance.setTime(key);
            String result = obj.getString(key);
            if (!TextUtils.isEmpty(result)) {
                JSONObject attObject = new JSONObject(result);
                if (PublicUtils.isValid(attObject, "in_time")) {
                    attendance.setInTime(attObject.getString("in_time"));
                } else {
                    attendance.setInTime("");
                }
                if (PublicUtils.isValid(attObject, "out_time")) {
                    attendance.setOutTime(attObject.getString("out_time"));
                } else {
                    attendance.setOutTime("");
                }
                if (PublicUtils.isValid(attObject, "isExp")) {
                    attendance.setIsExp(attObject.getString("isExp"));
                } else {
                    attendance.setIsExp("");
                }
                if (PublicUtils.isValid(attObject, "in_addr")) {
                    attendance.setInAddr(attObject.getString("in_addr"));
                } else {
                    attendance.setInAddr("");
                }
                if (PublicUtils.isValid(attObject, "out_addr")) {
                    attendance.setOutAddr(attObject.getString("out_addr"));
                } else {
                    attendance.setOutAddr("");
                }
                if (PublicUtils.isValid(attObject, "in_comment")) {
                    attendance.setInComment(attObject.getString("in_comment"));
                } else {
                    attendance.setInComment("");
                }
                if (PublicUtils.isValid(attObject, "out_comment")) {
                    attendance.setOutComment(attObject.getString("out_comment"));
                } else {
                    attendance.setOutComment("");
                }
                if (PublicUtils.isValid(attObject, "in_time_j")) {
                    attendance.setInTimeJ(attObject.getString("in_time_j"));
                } else {
                    attendance.setInTimeJ("");
                }
                if (PublicUtils.isValid(attObject, "out_time_j")) {
                    attendance.setOutTimeJ(attObject.getString("out_time_j"));
                } else {
                    attendance.setOutTimeJ("");
                }
                if (PublicUtils.isValid(attObject, "in_comment_j")) {
                    attendance.setInCommentJ(attObject.getString("in_comment_j"));
                } else {
                    attendance.setInCommentJ("");
                }
                if (PublicUtils.isValid(attObject, "out_comment_j")) {
                    attendance.setOutCommentJ(attObject.getString("out_comment_j"));
                } else {
                    attendance.setOutCommentJ("");
                }
            }
            itemList.add(attendance);
        }
        return itemList;
    }

    /**
     * 解析欠款统计列表数据
     *
     * @param json
     * @return
     * @throws JSONException
     */
    public List<CarSalesDebtListItem> parserDebtListItem(JSONArray array)
            throws JSONException {
        List<CarSalesDebtListItem> itemList = new ArrayList<CarSalesDebtListItem>();
        if (array != null && array.length() > 0) {
            for (int i = 0; i < array.length(); i++) {
                JSONObject carSalesDebtObj = array.getJSONObject(i);

                CarSalesDebtListItem item = new CarSalesDebtListItem();
                item.setResultName(carSalesDebtObj.getString("store_name"));
                item.setResultTotalPrice(carSalesDebtObj
                        .getString("unpaid_amount"));

                // 获取detail
                JSONArray dArray = carSalesDebtObj.getJSONArray("detail");
                List<CarSalesDebtDetail> list = new ArrayList<CarSalesDebtDetail>();
                for (int j = 0; j < dArray.length(); j++) {
                    JSONArray cArray = dArray.getJSONArray(j);

                    String time = cArray.getString(1);
                    String price = cArray.getString(2);
                    CarSalesDebtDetail carSalesDebtDetail = new CarSalesDebtDetail();
                    carSalesDebtDetail.setTime(time);
                    carSalesDebtDetail.setPrice(price);
                    list.add(carSalesDebtDetail);

                    item.setData(list);

                }

                itemList.add(item);
            }

        }

        return itemList;
    }

    /**
     * 解析欠款统计列表数据
     *
     * @param json
     * @return
     * @throws JSONException
     */
    public List<String> parserStockListItem(CarSalesStock carSalesStock)
            throws JSONException {

        List<String> itemList = new ArrayList<String>();

        List<CarSalesProductConf> confList = new CarSalesProductConfDB(context)
                .findListByType(CarSalesProductConf.TYPE_PRODUCT);
        for (int i = 0; i < confList.size(); i++) {
            String name = carSalesStock.getProductName();
            if (null == name) {
                break;
            }
            String str = context.getResources().getString(R.string.parser_cache_no);
            switch (i) {
                case 0:
                    str = name.substring(0, name.indexOf("@$"));
                    break;
                case 1:
                    str = name.substring(name.indexOf("@$") + 2,
                            name.lastIndexOf("@$"));
                    break;
                case 2:
                    str = name.substring(name.lastIndexOf("@$") + 2, name.length());
                default:
                    break;
            }

            itemList.add(str);
        }

        String Num = PublicUtils.formatDouble(carSalesStock.getStockNum());
        String Outnum = PublicUtils
                .formatDouble(carSalesStock.getStockoutNum());
        String ReplenishmentNum = PublicUtils.formatDouble(carSalesStock
                .getReplenishmentNum());
        String unit = carSalesStock.getUnit();
        itemList.add(unit);
        itemList.add(Num);
        itemList.add(Outnum);
        itemList.add(ReplenishmentNum);

        return itemList;
    }

    /**
     * 解析联系人
     *
     * @param userObj
     * @return
     * @throws JSONException
     */
    public CarSalesContact parserContact(JSONObject userObj)
            throws JSONException {
        CarSalesContact orderContacts = new CarSalesContact();
        if (userObj.has("id")) {
            orderContacts.setContactsId(userObj.getInt("id"));
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

    public List<CarSales> parserCarSales(String json, boolean clear)
            throws JSONException {
        List<CarSales> list = new ArrayList<CarSales>();
        if (!TextUtils.isEmpty(json)) {
            JSONObject obj = new JSONObject(json);
            if (PublicUtils.isValid(obj, "salesData")) {
                JSONArray array = obj.getJSONArray("salesData");
                CarSalesDB carSalesDB = new CarSalesDB(context);
                CarSalesProductDataDB dataDB = new CarSalesProductDataDB(
                        context);
                if (clear) {
                    carSalesDB.clearCarSales();
                    dataDB.clearCarSalesProductData();
                }
                for (int i = 0; i < array.length(); i++) {
                    JSONObject carSalesObj = array.getJSONObject(i);
                    CarSales carSales = new CarSales();
                    if (PublicUtils.isValid(carSalesObj, "id")) {
                        carSales.setCarSalesId(carSalesObj.getInt("id"));
                    }
                    if (PublicUtils.isValid(carSalesObj, "store_id")) {
                        carSales.setStoreId(String.valueOf(carSalesObj
                                .getInt("store_id")));
                    }
                    if (PublicUtils.isValid(carSalesObj, "store_name")) {
                        carSales.setStoreName(carSalesObj
                                .getString("store_name"));
                    }
                    if (PublicUtils.isValid(carSalesObj, "carSales_time")) {
                        carSales.setCarSalesTime(carSalesObj
                                .getString("carSales_time"));
                    }
                    if (PublicUtils.isValid(carSalesObj, "carSales_num")) {
                        carSales.setCarSalesNo(carSalesObj
                                .getString("carSales_num"));
                    }
                    if (PublicUtils.isValid(carSalesObj, "audit_status")) {
                        carSales.setCarSalesState(carSalesObj
                                .getString("audit_status"));
                    }
                    if (PublicUtils.isValid(carSalesObj, "pay_amount")) {
                        carSales.setPayAmount(Double.parseDouble(carSalesObj
                                .getString("pay_amount")));
                    }
                    if (PublicUtils.isValid(carSalesObj, "carSales_amount")) {
                        carSales.setCarSalesAmount(Double
                                .parseDouble(carSalesObj
                                        .getString("carSales_amount")));
                    }
                    if (PublicUtils.isValid(carSalesObj, "actual_amount")) {
                        carSales.setActualAmount(Double.parseDouble(carSalesObj
                                .getString("actual_amount")));
                        if (carSales.getActualAmount() == 0) {
                            carSales.setActualAmount(carSales
                                    .getCarSalesAmount());
                        }
                    }
                    if (PublicUtils.isValid(carSalesObj, "carSales_discount")) {
                        carSales.setCarSalesDiscount(Double
                                .parseDouble(carSalesObj
                                        .getString("carSales_discount")));
                    }
                    if (PublicUtils.isValid(carSalesObj, "detail")) {
                        JSONArray dArray = carSalesObj.getJSONArray("detail");
                        List<CarSalesProductData> dataList = new ArrayList<CarSalesProductData>();
                        for (int j = 0; j < dArray.length(); j++) {
                            JSONObject dObj = dArray.getJSONObject(j);
                            CarSalesProductData data = parserCarSalesProductData(dObj);
                            if (data == null) {
                                continue;
                            }
                            data.setCarSalesNo(carSales.getCarSalesNo());
                            dataList.add(data);
                            dataDB.insertCarSalesProductData(data);
                        }
                        carSales.setProductList(dataList);
                    }
                    list.add(carSales);
                    carSalesDB.insertCarSales(carSales);
                }
            }
        }
        return list;
    }

    // private CarSalesDebtItemDetail parserCarSalesDebtItemDetail(JSONObject
    // obj)
    // throws JSONException {
    // CarSalesDebtItemDetail carSalesDebtItemDetail = new
    // CarSalesDebtItemDetail();
    // carSalesDebtItemDetail.setPrice(obj.get);
    // return carSalesDebtItemDetail;
    //
    // }

    private CarSalesProductData parserCarSalesProductData(JSONObject obj)
            throws JSONException {
        CarSalesProductData data = new CarSalesProductData();
        if (PublicUtils.isValid(obj, "id")) {
            data.setDataId(obj.getInt("id"));
        }
        if (PublicUtils.isValid(obj, "sales_num")) {
            data.setCarSalesNo(obj.getString("sales_num"));
        }

        if (PublicUtils.isValid(obj, "product_id")) {
            data.setProductId(obj.getInt("product_id"));
        }
        if (PublicUtils.isValid(obj, "sales_count")) {
            data.setCarSalesCount(Double.parseDouble(obj
                    .getString("sales_count")));
        }
        if (PublicUtils.isValid(obj, "actual_count")) {
            data.setActualCount(Double.parseDouble(obj
                    .getString("actual_count")));
        }
        if (PublicUtils.isValid(obj, "product_standard")) {
            data.setProductUnit(obj.getString("product_standard"));
        }
        if (PublicUtils.isValid(obj, "product_standard_id")) {
            data.setUnitId(obj.getInt("product_standard_id"));
        }
        if (PublicUtils.isValid(obj, "sales_price")) {
            data.setCarSalesPrice(Double.parseDouble(obj
                    .getString("sales_price")));
        }
        if (PublicUtils.isValid(obj, "sales_amount")) {
            data.setCarSalesAmount(Double.parseDouble(obj
                    .getString("sales_amount")));
        }
        if (PublicUtils.isValid(obj, "actual_amount")) {
            data.setActualAmount(Double.parseDouble(obj
                    .getString("actual_amount")));
        }
        if (PublicUtils.isValid(obj, "carSales_status")) {
            data.setStatus(obj.getString("carSales_status"));
        }
        if (PublicUtils.isValid(obj, "is_sales_main")) {
            data.setIsCarSalesMain(Integer.parseInt(obj
                    .getString("is_sales_main")));
        }
        if (PublicUtils.isValid(obj, "promotion_id")) {
            data.setPromotionId(Integer.parseInt(obj.getString("promotion_id")));
        }
        if (PublicUtils.isValid(obj, "main_product_id")) {
            data.setMainProductId(Integer.parseInt(obj
                    .getString("main_product_id")));
        }
        if (PublicUtils.isValid(obj, "carSales_remark")) {
            data.setCarSalesRemark(obj.getString("carSales_remark"));
        }
        if (PublicUtils.isValid(obj, "send_count")) {
            String s = obj.getString("send_count");
            data.setSendCount(TextUtils.isEmpty(s) ? 0 : Double.parseDouble(s));
        }
        CarSalesProduct product = carSalesUtil.product(data.getProductId(),
                data.getUnitId());
        if (product != null) {
            data.setProductName(product.getName());
        } else {
            data.setProductName(String.valueOf(data.getProductId()));
        }

        return data;
    }

    /**
     * 装车记录数据解析
     *
     * @param json
     * @param clear
     * @return
     * @throws JSONException
     */
    public List<CarSales> parserLodingCarSales(String json, boolean clear)
            throws JSONException {
        List<CarSales> list = new ArrayList<CarSales>();
        if (!TextUtils.isEmpty(json)) {
            JSONObject obj = new JSONObject(json);
            if (PublicUtils.isValid(obj, "lodingData")) {
                JSONArray array = obj.getJSONArray("lodingData");
                CarSalesDB carSalesDB = new CarSalesDB(context);
                CarSalesProductDataDB dataDB = new CarSalesProductDataDB(
                        context);
                if (clear) {
                    carSalesDB.clearCarSales();
                    dataDB.clearCarSalesProductData();
                }
                for (int i = 0; i < array.length(); i++) {
                    JSONObject carSalesObj = array.getJSONObject(i);
                    CarSales carSales = new CarSales();
                    if (PublicUtils.isValid(carSalesObj, "id")) {
                        carSales.setCarSalesId(carSalesObj.getInt("id"));
                    }
                    if (PublicUtils.isValid(carSalesObj, "car_id")) {
                        carSales.setCarId(String.valueOf(carSalesObj
                                .getInt("car_id")));
                    }
                    if (PublicUtils.isValid(carSalesObj, "loding_num")) {
                        carSales.setCarSalesNo(carSalesObj
                                .getString("loding_num"));
                    }
                    if (PublicUtils.isValid(carSalesObj, "loding_date")) {
                        carSales.setCarSalesTime(carSalesObj
                                .getString("loding_date"));
                    }
                    if (PublicUtils.isValid(carSalesObj, "audit_status")) {
                        carSales.setCarSalesState(carSalesObj
                                .getString("audit_status"));
                    }

                    if (PublicUtils.isValid(carSalesObj, "image1")) {
                        carSales.setImage1(carSalesObj.getString("image1"));
                    }
                    if (PublicUtils.isValid(carSalesObj, "image2")) {
                        carSales.setImage2(carSalesObj.getString("image2"));
                    }

                    if (PublicUtils.isValid(carSalesObj, "remarks")) {
                        carSales.setNote(carSalesObj.getString("remarks"));
                    }
                    if (PublicUtils.isValid(carSalesObj, "detail")) {
                        JSONArray dArray = carSalesObj.getJSONArray("detail");
                        List<CarSalesProductData> dataList = new ArrayList<CarSalesProductData>();
                        for (int j = 0; j < dArray.length(); j++) {
                            JSONObject dObj = dArray.getJSONObject(j);
                            CarSalesProductData data = parserLoadingProductData(dObj);
                            if (data == null) {
                                continue;
                            }
                            data.setCarSalesNo(carSales.getCarSalesNo());
                            dataList.add(data);
                            dataDB.insertCarSalesProductData(data);
                        }
                        carSales.setProductList(dataList);
                    }
                    list.add(carSales);
                    carSalesDB.insertCarSales(carSales);
                }
            }
        }
        return list;
    }

    private CarSalesProductData parserLoadingProductData(JSONObject obj)
            throws JSONException {
        CarSalesProductData data = new CarSalesProductData();
        if (PublicUtils.isValid(obj, "id")) {
            data.setDataId(obj.getInt("id"));
        }
        if (PublicUtils.isValid(obj, "loding_num")) {
            data.setCarSalesNo(obj.getString("loding_num"));
        }
        if (PublicUtils.isValid(obj, "product_id")) {
            data.setProductId(Integer.parseInt(obj.getString("product_id")));
        }
        if (PublicUtils.isValid(obj, "pro_standard_id")) {
            data.setUnitId(Integer.parseInt(obj.getString("pro_standard_id")));
        }
        if (PublicUtils.isValid(obj, "loding_count")) {
            data.setCarSalesCount(Double.parseDouble(obj
                    .getString("loding_count")));
        }
        if (PublicUtils.isValid(obj, "product_standard")) {
            data.setProductUnit(obj.getString("product_standard"));
        }
        CarSalesProduct product = carSalesUtil.product(data.getProductId(),
                data.getUnitId());
        if (product != null) {
            data.setProductName(product.getName());
        } else {
            data.setProductName(String.valueOf(data.getProductId()));
        }
        return data;
    }

    /**
     * 卸车记录数据解析
     *
     * @param json
     * @param clear
     * @return
     * @throws JSONException
     */
    public List<CarSales> parserUnLodingCarSales(String json, boolean clear)
            throws JSONException {
        List<CarSales> list = new ArrayList<CarSales>();
        if (!TextUtils.isEmpty(json)) {
            JSONObject obj = new JSONObject(json);
            if (PublicUtils.isValid(obj, "truckData")) {
                JSONArray array = obj.getJSONArray("truckData");
                CarSalesDB carSalesDB = new CarSalesDB(context);
                CarSalesProductDataDB dataDB = new CarSalesProductDataDB(
                        context);
                if (clear) {
                    carSalesDB.clearCarSales();
                    dataDB.clearCarSalesProductData();
                }
                for (int i = 0; i < array.length(); i++) {
                    JSONObject carSalesObj = array.getJSONObject(i);
                    CarSales carSales = new CarSales();
                    if (PublicUtils.isValid(carSalesObj, "id")) {
                        carSales.setCarSalesId(carSalesObj.getInt("id"));
                    }
                    if (PublicUtils.isValid(carSalesObj, "car_id")) {
                        carSales.setCarId(String.valueOf(carSalesObj
                                .getInt("car_id")));
                    }
                    if (PublicUtils.isValid(carSalesObj, "truck_num")) {
                        carSales.setCarSalesNo(carSalesObj
                                .getString("truck_num"));
                    }
                    if (PublicUtils.isValid(carSalesObj, "truck_date")) {
                        carSales.setCarSalesTime(carSalesObj
                                .getString("truck_date"));
                    }
                    if (PublicUtils.isValid(carSalesObj, "audit_status")) {
                        carSales.setCarSalesState(carSalesObj
                                .getString("audit_status"));
                    }
                    if (PublicUtils.isValid(carSalesObj, "image1")) {
                        carSales.setImage1(carSalesObj.getString("image1"));
                    }
                    if (PublicUtils.isValid(carSalesObj, "image2")) {
                        carSales.setImage2(carSalesObj.getString("image2"));
                    }
                    if (PublicUtils.isValid(carSalesObj, "remarks")) {
                        carSales.setNote(carSalesObj.getString("remarks"));
                    }
                    if (PublicUtils.isValid(carSalesObj, "detail")) {
                        JSONArray dArray = carSalesObj.getJSONArray("detail");
                        List<CarSalesProductData> dataList = new ArrayList<CarSalesProductData>();
                        for (int j = 0; j < dArray.length(); j++) {
                            JSONObject dObj = dArray.getJSONObject(j);
                            CarSalesProductData data = parserUnLoadingProductData(dObj);
                            if (data == null) {
                                continue;
                            }
                            data.setCarSalesNo(carSales.getCarSalesNo());
                            dataList.add(data);
                            dataDB.insertCarSalesProductData(data);
                        }
                        carSales.setProductList(dataList);
                    }
                    list.add(carSales);
                    carSalesDB.insertCarSales(carSales);
                }
            }
        }
        return list;
    }

    private CarSalesProductData parserUnLoadingProductData(JSONObject obj)
            throws JSONException {
        CarSalesProductData data = new CarSalesProductData();
        if (PublicUtils.isValid(obj, "id")) {
            data.setDataId(obj.getInt("id"));
        }
        if (PublicUtils.isValid(obj, "truck_num")) {
            data.setCarSalesNo(obj.getString("truck_num"));
        }
        if (PublicUtils.isValid(obj, "product_id")) {
            data.setProductId(Integer.parseInt(obj.getString("product_id")));
        }

        if (PublicUtils.isValid(obj, "pro_standard_id")) {
            data.setUnitId(Integer.parseInt(obj.getString("pro_standard_id")));
        }
        if (PublicUtils.isValid(obj, "truck_count")) {
            data.setCarSalesCount(Double.parseDouble(obj
                    .getString("truck_count")));

        }
        if (PublicUtils.isValid(obj, "product_standard")) {
            data.setProductUnit(obj.getString("product_standard"));
        }
        CarSalesProduct product = carSalesUtil.product(data.getProductId(),
                data.getUnitId());
        if (product != null) {
            data.setProductName(product.getName());
        } else {
            data.setProductName(String.valueOf(data.getProductId()));
        }
        return data;
    }

    /**
     * 解析收取欠款记录
     */
    public List<CarSales> parserChargeArrearsCarSales(String json, boolean clear)
            throws JSONException {
        List<CarSales> list = new ArrayList<CarSales>();
        if (!TextUtils.isEmpty(json)) {
            JSONObject obj = new JSONObject(json);
            if (PublicUtils.isValid(obj, "balanceData")) {
                JSONArray array = obj.getJSONArray("balanceData");
                CarSalesDB carSalesDB = new CarSalesDB(context);
                CarSalesProductDataDB dataDB = new CarSalesProductDataDB(
                        context);
                if (clear) {
                    carSalesDB.clearCarSales();
                    dataDB.clearCarSalesProductData();
                }
                for (int i = 0; i < array.length(); i++) {
                    JSONObject carSalesObj = array.getJSONObject(i);
                    CarSales carSales = new CarSales();
                    if (PublicUtils.isValid(carSalesObj, "car_id")) {
                        carSales.setCarId(String.valueOf(carSalesObj
                                .getInt("car_id")));
                    }
                    if (PublicUtils.isValid(carSalesObj, "car_no")) {
                        carSales.setCarNo(carSalesObj.getString("car_no"));
                    }
                    if (PublicUtils.isValid(carSalesObj, "unpaid_amount")) {
                        carSales.setUnPayAmount(Math.abs(Double
                                .parseDouble(carSalesObj
                                        .getString("unpaid_amount"))));
                    }
                    if (PublicUtils.isValid(carSalesObj, "store_id")) {
                        carSales.setStoreId(carSalesObj.getString("store_id"));
                    }
                    String storeName = null;
                    if (PublicUtils.isValid(carSalesObj, "store_name")) {
                        carSales.setStoreName(carSalesObj
                                .getString("store_name"));
                        storeName = carSales.getStoreName();
                    }
                    if (PublicUtils.isValid(carSalesObj, "detail")) {
                        JSONArray dArray = carSalesObj.getJSONArray("detail");
                        List<Arrears> dataList = new ArrayList<Arrears>();
                        for (int j = 0; j < dArray.length(); j++) {
                            JSONArray arra = dArray.getJSONArray(j);
                            Arrears data = parserChargeArrearsProductData(arra,
                                    storeName);
                            if (data == null) {
                                continue;
                            }
                            // 只加载未结清的数据
                            if (data.getIsOver() == 0) {
                                dataList.add(data);
                            }
                        }
                        carSales.setArrears(dataList);
                    }
                    list.add(carSales);
                    carSalesDB.insertCarSales(carSales);
                }
            }
        }
        return list;
    }

    private Arrears parserChargeArrearsProductData(JSONArray arra, String name)
            throws JSONException {
        Arrears data = new Arrears();
        data.setName(name);
        for (int i = 0; i < arra.length(); i++) {
            switch (i) {
                case 0:
                    data.setId(Integer.parseInt(arra.getString(i)));
                    break;
                case 1:
                    data.setTime(arra.getString(i));
                    break;
                case 2:
                    data.setArrearsAmount(Math.abs(Double.parseDouble(arra
                            .getString(i))));
                    break;
                case 3:
                    data.setIsOver(Integer.parseInt(arra.getString(i)));
                    break;
                case 4:
                    data.setHisAmount(Double.parseDouble(arra.getString(i)));
                default:
                    break;
            }
        }
        return data;
    }

    /**
     * 解析库存
     *
     * @throws JSONException
     */
    private void parserStockDetail(JSONArray array) throws JSONException {
        if (array != null && array.length() > 0) {
            CarSalesStockDB db = new CarSalesStockDB(context);
            db.delete();
            DatabaseHelper.getInstance(context).beginTransaction();
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                CarSalesStock stock = new CarSalesStock();
                if (PublicUtils.isValid(obj, "product_id")) {
                    stock.setProductId(obj.getInt("product_id"));
                }
                if (PublicUtils.isValid(obj, "pro_standard")) {
                    stock.setUnit(obj.getString("pro_standard"));
                }
                if (PublicUtils.isValid(obj, "pro_standard_id")) {
                    stock.setUnitId(obj.getInt("pro_standard_id"));
                }
                if (PublicUtils.isValid(obj, "stock_count")) {
                    stock.setStockNum(obj.getDouble("stock_count"));
                }
                if (PublicUtils.isValid(obj, "stock_out_count")) {
                    stock.setStockoutNum(obj.getDouble("stock_out_count"));
                }
                if (PublicUtils.isValid(obj, "fill_count")) {
                    stock.setReplenishmentNum(obj.getDouble("fill_count"));
                }
                db.insert(stock);
            }
            DatabaseHelper.getInstance(context).endTransaction();
        }
    }
}
