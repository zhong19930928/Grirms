package com.yunhu.yhshxc.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.GoodsStorage;
import com.yunhu.yhshxc.bo.Org;
import com.yunhu.yhshxc.bo.OrgStore;
import com.yunhu.yhshxc.bo.OrgUser;
import com.yunhu.yhshxc.bo.ReplenishSearchResult;
import com.yunhu.yhshxc.database.DictDB;
import com.yunhu.yhshxc.database.FuncDB;
import com.yunhu.yhshxc.database.OrgDB;
import com.yunhu.yhshxc.database.OrgStoreDB;
import com.yunhu.yhshxc.database.OrgUserDB;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.exception.HTTPResponseResultException;
import com.yunhu.yhshxc.func.PaintActivity;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.list.activity.AbsSearchListActivity;
import com.yunhu.yhshxc.parser.ReplenishParse;
import com.yunhu.yhshxc.submitManager.core.SubmitWorkManager;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.HttpHelper;
import com.yunhu.yhshxc.utility.MD5Helper;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gcg.org.debug.JDebugOptions;
import gcg.org.debug.JLog;

/**
 * @author YH
 *         耗材入库确认页面
 */
public class ConfirmSupLibrayActivity extends Activity implements View.OnClickListener {
    private RelativeLayout meeting_back;
    private TextView confirm_btn;
    private ListView ruku_listview;
    private RelativeLayout ruku_num_layout, ruku_adds, ruku_time, ruku_supply, ruku_per, ruku_datetime, ruku_remarks;
    private TextView ruku_num_tx, ruku_num_content;
    private TextView ruku_adds_tx, ruku_adds_content;
    private TextView ruku_time_tx, ruku_time_content;
    private TextView ruku_supply_tx, ruku_supply_content;
    private TextView ruku_per_tx, ruku_per_content;
    private TextView ruku_datetime_tx, ruku_datetime_content;
    private TextView ruku_remarks_tx, ruku_remarks_content;
    private ArrayList<GoodsStorage> dList1;
    private GoodsStorage goodsStorage;
    private ConFirmRukuAdapter confirmAdapter;
    private static final int REQUESTCODE = 100;
    public final String TAG = this.getClass().getSimpleName();
    /**
     * 解析完成常量
     */
    protected final int PARSE_OK = 98;
    /**
     * 解析失败常量
     */
    protected final int PARSE_FAIL = 97;

    protected final int UPLOAD_OK = 99;
    protected final int UPLOAD_FAIL = 96;

    /**
     * 服务器返回json,解析后的数据 map<funcId,value>
     */
    private List<Map<String, String>> dataList = new ArrayList<>();
    private List<Func> viewCoum;
    private String imagename;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_sup_libray);
        context=ConfirmSupLibrayActivity.this;
        init();
        dList1 = new ArrayList<>();
        goodsStorage=new GoodsStorage();
        dList1.add(goodsStorage);
        FuncDB funcDB = new FuncDB(this);
        // 通过targetID和是否在列表中显示的标记查询需要在列表中显示的Func
        viewCoum = funcDB.findFuncListReplenish(2031736, 1, null,
                null);
        inserItem();

    }

    private void init() {
        meeting_back = (RelativeLayout) findViewById(R.id.meeting_back);
        meeting_back.setOnClickListener(this);
        confirm_btn = (TextView) findViewById(R.id.confirm_btn);
        confirm_btn.setOnClickListener(this);
        ruku_listview = (ListView) findViewById(R.id.ruku_listview);
        ruku_num_layout = (RelativeLayout) findViewById(R.id.ruku_num_layout);
        ruku_num_tx = (TextView) ruku_num_layout.findViewById(R.id.ruku_tx);
        ruku_num_tx.setText("入库单号");
        ruku_num_content = (TextView) ruku_num_layout.findViewById(R.id.ruku_tx_content);

        ruku_adds = (RelativeLayout) findViewById(R.id.ruku_adds);
        ruku_adds_tx = (TextView) ruku_adds.findViewById(R.id.ruku_tx);
        ruku_adds_tx.setText("入库仓库");
        ruku_adds_content = (TextView) ruku_adds.findViewById(R.id.ruku_tx_content);

        ruku_time = (RelativeLayout) findViewById(R.id.ruku_time);
        ruku_time_tx = (TextView) ruku_time.findViewById(R.id.ruku_tx);
        ruku_time_tx.setText("业务日期");
        ruku_time_content = (TextView) ruku_time.findViewById(R.id.ruku_tx_content);

        ruku_supply = (RelativeLayout) findViewById(R.id.ruku_supply);
        ruku_supply_tx = (TextView) ruku_supply.findViewById(R.id.ruku_tx);
        ruku_supply_tx.setText("供应商");
        ruku_supply_content = (TextView) ruku_supply.findViewById(R.id.ruku_tx_content);

        ruku_per = (RelativeLayout) findViewById(R.id.ruku_per);
        ruku_per_tx = (TextView) ruku_per.findViewById(R.id.ruku_tx);
        ruku_per_tx.setText("经办人");
        ruku_per_content = (TextView) ruku_per.findViewById(R.id.ruku_tx_content);

        ruku_datetime = (RelativeLayout) findViewById(R.id.ruku_datetime);
        ruku_datetime_tx = (TextView) ruku_datetime.findViewById(R.id.ruku_tx);
        ruku_datetime_tx.setText("经办日期");
        ruku_datetime_content = (TextView) ruku_datetime.findViewById(R.id.ruku_tx_content);

        ruku_remarks = (RelativeLayout) findViewById(R.id.ruku_remarks);
        ruku_remarks_tx = (TextView) ruku_remarks.findViewById(R.id.ruku_tx);
        ruku_remarks_tx.setText("入库备注");
        ruku_remarks_content = (TextView) ruku_remarks.findViewById(R.id.ruku_tx_content);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.confirm_btn:
                Intent intent = new Intent(ConfirmSupLibrayActivity.this, PaintActivity.class);
                startActivityForResult(intent, REQUESTCODE);
                break;
            case R.id.meeting_back:
                finish();
                break;
            default:
                break;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUESTCODE:
                if (data != null) {
                    imagename = data.getStringExtra("path");
                    submitData();
                } else {
                    imagename=null;
                    Toast.makeText(ConfirmSupLibrayActivity.this, "您还没有电子签名", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;

        }
    }

    public class ConFirmRukuAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return dList1.size();
        }

        @Override
        public Object getItem(int i) {
            return dList1.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ViewHolder vh = null;
            if (vh == null) {
                vh = new ViewHolder();
                LayoutInflater mInflater = LayoutInflater.from(ConfirmSupLibrayActivity.this);
                convertView = mInflater.inflate(R.layout.confirmsup_listitem, null);
                vh.goods_name_content = (TextView) convertView
                        .findViewById(R.id.goods_name_content);
                vh.ruku_goodsnum_content = (TextView) convertView
                        .findViewById(R.id.ruku_goodsnum_content);
                vh.ruku_goodsprice_content = (TextView) convertView
                        .findViewById(R.id.ruku_goodsprice_content);
                vh.ruku_goodsmeney_content = (TextView) convertView
                        .findViewById(R.id.ruku_goodsmeney_content);
                vh.goods_icon = (ImageView) convertView.findViewById(R.id.goods_icon);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            vh.goods_name_content.setText(dList1.get(i).getStorageName());
            vh.ruku_goodsnum_content.setText(dList1.get(i).getStorageNums());
            if(dList1.get(i).getStoragePrice()!=null&&!"".equals(dList1.get(i).getStoragePrice())){
                vh.ruku_goodsprice_content.setText("￥"+dList1.get(i).getStoragePrice());
            }
            if(dList1.get(i).getStorageMeney()!=null&&!"".equals(dList1.get(i).getStorageMeney())){

                vh.ruku_goodsmeney_content.setText("￥"+dList1.get(i).getStorageMeney());
            }

            return convertView;
        }
    }

    class ViewHolder {
        public TextView goods_name_content;
        public TextView ruku_goodsnum_content;
        public TextView ruku_goodsprice_content;
        public TextView ruku_goodsmeney_content;
        public ImageView goods_icon;

    }


    /**
     * 加载对话框
     */
    private Dialog dialogSc = null;

    /**
     * 在一个线程中用http获取数据
     */
    private void inserItem() {
        // 初始化加载对话框
        dialogSc = new MyProgressDialog(this, R.style.CustomProgressDialog, getResources().getString(R.string.activity_after_04));
        // 显示加载对话框
        dialogSc.show();
        HashMap<String, String> map = setPageParams();

        RequestParams requestParams = new RequestParams();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            requestParams.put(entry.getKey(), entry.getValue());
            JLog.d(TAG, entry.getKey() + ":" + entry.getValue());
        }
        String baseUrlSearch = UrlInfo.getUrlReplenish(this);
        GcgHttpClient.getInstance(this).post(baseUrlSearch, requestParams, new HttpResponseListener() {
            @Override
            public void onStart() {
            }


            @Override
            public void onFailure(Throwable error, String content) {
              //						currentPage = lastPage;
                fail(getResources().getString(R.string.activity_after_02));
            }

            @Override
            public void onFinish() {
           //                elv_search.onRefreshComplete();
            }

            @Override
            public void onSuccess(int statusCode, String content) {
                // 获取JSON
                // 判断是否返回 "resultcode":"0001"
                if ("{\"resultcode\":\"0001\"}".equals(content)) {
                    JLog.d(TAG, "msg.obj   ====json===>>>>>" + content);

                    fail("没有查询到相关数据!");
                    return;
                }
                JLog.d(TAG, "JSON   =======>>>>>" + content);
                doInbackGround(content);

            }

        });
    }


    private HashMap<String, String> setPageParams() {
        HashMap<String, String> map = new HashMap<>();
        map.put("phoneno", PublicUtils.getPhoneNumber(this));
        map.put("type", "4");
        map.put("page", "1");
        map.put("taskid", "2031736");
        return map;
    }

    private HashMap<String, String> setPutParams() {
        Map<String, String> itemData = (Map<String, String>) dataList.get(0);
        List<Func> buttonFuncList=new FuncDB(this).findButtonFuncListReplenish(2031736,itemData.get("status"));
        HashMap<String, String> map = new HashMap<>();
        map.put("tableId", "2031736");
        map.put("2044955", buttonFuncList.size()!=0?buttonFuncList.get(0).getStatus()+"":"0");
        map.put("2044954", imagename);
        map.put("doubleId", itemData.get("taskid"));
        map.put("2044121",  itemData.get("2044121"));
        map.put("routeId", "0");
        map.put("patchId", itemData.get("patchid"));
        map.put("planId", "0");
        map.put("type", "4");
        map.put("submit_count", "0");
        map.put("storeId", "0");
        return map;
    }

    /**
     * 提示错误信息
     *
     * @param message 错误信息
     */
    private void fail(String message) {
        dismissLoadDialog();// 关闭加载对话框
        ToastOrder.makeText(this, message, ToastOrder.LENGTH_LONG).show();
        finish();
    }

    /**
     * 关闭载入对话框
     */
    public void dismissLoadDialog() {
        if (dialogSc != null && dialogSc.isShowing()) {
            dialogSc.dismiss();
        }
    }

    private void doInbackGround(final String json) {
        new Thread() {
            @Override
            public void run() {
                try {
                    doInbackGroundInThread(json);
                } catch (Exception e) {
                    e.printStackTrace();
                    mHandler.sendEmptyMessage(PARSE_FAIL);
                }
            }

            ;
        }.start();
    }

    private void doInbackGroundInThread(String json) throws Exception {
        dataList = getDataListOnThread(json);
        mHandler.sendEmptyMessage(PARSE_OK);

    }

    /**
     * 接受网络返回数据的Handler
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {

                case PARSE_OK:
                    afterSuccessParse();
                    break;
                case PARSE_FAIL:
                    fail(getResources().getString(R.string.activity_after_01));
                    break;
                case UPLOAD_OK:
                    dismissLoadDialog();// 关闭加载对话框
                    ToastOrder.makeText(context, "提交成功", ToastOrder.LENGTH_LONG).show();
                    finish();
                    break;
                case UPLOAD_FAIL:
                    dismissLoadDialog();// 关闭加载对话框
                    ToastOrder.makeText(context, "提交失败", ToastOrder.LENGTH_LONG).show();
                    finish();
                    break;
                default:
                    fail(getResources().getString(R.string.activity_after_02));
                    break;
            }
        }
    };

    /**
     * 解析完在主线程中操作 可继承
     */
    private void afterSuccessParse() {
        if (dataList == null || dataList.isEmpty()) {
            fail(getResources().getString(R.string.activity_after_01));

        } else {
            dismissLoadDialog();// 关闭加载对话框
            //保存数据刷新列表
//            saveInsetSubmitItem();
            getData();
            if(confirmAdapter==null){
                confirmAdapter = new ConFirmRukuAdapter();
                ruku_listview.setAdapter(confirmAdapter);
            }else{
                confirmAdapter.notifyDataSetChanged();
            }
        }
    }


    private List<Map<String, String>> getDataListOnThread(String json) throws Exception {
        return parseJson(json);
    }

    private List<Map<String, String>> parseJson(String json) throws Exception {
        // 解析JSON
        ReplenishSearchResult searchResult = new ReplenishParse().parseSearchResult(json);
//        this.settingPage(searchResult.getCacherows(), searchResult.getTotal());
        // 补报数据
        List<Map<String, String>> resultList = searchResult.getResultList();
        // 判断没有数据时,隐藏窗体
        return resultList;
    }


    /**
     * 其他-下拉框
     *
     * @param func
     *            Func对象
     * @param itemData
     *            数据map<funcId,value>
     * @param key
     *            数据ID
     * @return 返回下拉框的文字
     */
    protected String getSelectOtherCompValue(Func func,
                                             Map<String, String> itemData, String key) {
        String tvValue = "";
        if (!TextUtils.isEmpty(key)) {
            if ("-1".equals(key)) {
                tvValue = itemData.get(func.getFuncId() + "_other");
            } else {
                tvValue = getSelectCompValue(func, key);
            }
        }
        return tvValue;
    }

    /**
     * 获取多选下拉框的值
     *
     * @param mIds
     *            用“,"分割的选项id集合
     * @return 返回用“,"分割的选项文字
     */
    protected String getMultiSelectCompValue(Func func, String mIds) {
        String tvValue = "";
        if (!TextUtils.isEmpty(mIds)) {// 判断非空
            if (func.getOrgOption() != null
                    && func.getOrgOption() == Func.ORG_STORE) {

                List<OrgStore> orgStoreList = new OrgStoreDB(
                        ConfirmSupLibrayActivity.this).findOrgListByStoreId(mIds);
                StringBuffer sb = new StringBuffer();
                for (int j = 0; j < orgStoreList.size(); j++) {
                    OrgStore orgStore = orgStoreList.get(j);
                    sb.append(",").append(orgStore.getStoreName());
                }
                tvValue = sb.length() > 0 ? sb.substring(1).toString() : mIds;

            } else if (func.getOrgOption() != null
                    && func.getOrgOption() == Func.ORG_USER) {

                List<OrgUser> orgUserList = new OrgUserDB(
                        ConfirmSupLibrayActivity.this).findOrgUserByUserId(mIds);
                StringBuffer sb = new StringBuffer();
                for (int j = 0; j < orgUserList.size(); j++) {
                    OrgUser orgUser = orgUserList.get(j);
                    sb.append(",").append(orgUser.getUserName());
                }
                tvValue = sb.length() > 0 ? sb.substring(1).toString() : mIds;

            } else if (func.getOrgOption() != null
                    && func.getOrgOption() == Func.ORG_OPTION) {

                List<Org> orgList = new OrgDB(ConfirmSupLibrayActivity.this)
                        .findOrgByOrgId(mIds);
                StringBuffer sb = new StringBuffer();
                for (int j = 0; j < orgList.size(); j++) {
                    Org org = orgList.get(j);
                    sb.append(",").append(org.getOrgName());
                }
                tvValue = sb.length() > 0 ? sb.substring(1).toString() : mIds;

            } else {
                if(!",".equals(mIds)){
                    tvValue = new DictDB(ConfirmSupLibrayActivity.this)
                            .findDictMultiChoiceValueStr(mIds,
                                    func.getDictDataId(), func.getDictTable());
                }

            }
        }

        return tvValue;
    }

    /**
     * 如果是下来选框时调用该方法,获取下拉选框的值
     *
     * @param func
     *            Func对象
     * @param key
     *            数据ID
     * @return 返回需要显示的文字，如果是多个值，则用“,”分割
     */
    protected String getSelectCompValue(Func func, String key) {
        String tvValue = "";
        if (!TextUtils.isEmpty(key)) {// 判断非空
            if (func.getOrgOption() != null
                    && func.getOrgOption() == Func.ORG_STORE) {// 机构店面联动
                // 根据storeId查询机构店面，然后取出所有店面名并作为结果返回
                // 如果找不到相关店面，则将storeId作为结果返回
                List<OrgStore> orgStoreList = new OrgStoreDB(
                        ConfirmSupLibrayActivity.this).findOrgListByStoreId(key);
                StringBuffer sb = new StringBuffer();
                for (int j = 0; j < orgStoreList.size(); j++) {
                    OrgStore orgStore = orgStoreList.get(j);
                    sb.append(",").append(orgStore.getStoreName());
                }
                tvValue = sb.length() > 0 ? sb.substring(1).toString() : key;
            } else if (func.getOrgOption() != null
                    && func.getOrgOption() == Func.ORG_USER) {// 用户联动
                // 根据userId查询机构用户，然后取出所有用户名并作为结果返回
                // 如果找不到相关用户，则将userId作为结果返回
                List<OrgUser> orgUserList = new OrgUserDB(
                        ConfirmSupLibrayActivity.this).findOrgUserByUserId(key);
                StringBuffer sb = new StringBuffer();
                for (int j = 0; j < orgUserList.size(); j++) {
                    OrgUser orgUser = orgUserList.get(j);
                    sb.append(",").append(orgUser.getUserName());
                }
                tvValue = sb.length() > 0 ? sb.substring(1).toString() : key;
            } else if (func.getOrgOption() != null
                    && func.getOrgOption() == Func.ORG_OPTION) {// 机构联动
                // 根据orgId查询机构，然后取出所有机构名并作为结果返回
                // 如果找不到相关机构，则将orgId作为结果返回
                List<Org> orgList = new OrgDB(ConfirmSupLibrayActivity.this)
                        .findOrgByOrgId(key);
                StringBuffer sb = new StringBuffer();
                for (int j = 0; j < orgList.size(); j++) {
                    Org org = orgList.get(j);
                    sb.append(",").append(org.getOrgName());
                }
                tvValue = sb.length() > 0 ? sb.substring(1).toString() : key;
            } else {
                // 查询的表
                String tableName = func.getDictTable();
                // 查询表中的列
                String dataId = func.getDictDataId();
                // 获取下拉内容Dictionary
                Dictionary dic = new DictDB(ConfirmSupLibrayActivity.this)
                        .findDictListByTable(tableName, dataId, key);
                if (dic != null) {
                    // 复制下拉选框的内容
                    tvValue = dic.getCtrlCol();
                }
            }
        }
        return tvValue;
    }

    private void getData(){
        Map<String, String> itemData = (Map<String, String>) dataList.get(0);
        ruku_num_content.setText(itemData.get("2044114"));
        for (Func func : viewCoum) {
            if (func.getType() == Func.TYPE_BUTTON) {// 按钮 不显示的 进行下一次循环
                continue;
            }
            String tvText = itemData.get(func.getFuncId().toString()) != null ? itemData.get(func.getFuncId().toString()) : "";
            tvText = getShowValue(itemData, func, tvText);
            if(ruku_adds_tx.getText().toString().equals(func.getName())){
                ruku_adds_content.setText(tvText);
            }
            if("入库日期".equals(func.getName())){
                ruku_time_content.setText(tvText);
            }
            if(ruku_supply_tx.getText().toString().equals(func.getName())){
                ruku_supply_content.setText(tvText);
            }
            if(ruku_per_tx.getText().toString().equals(func.getName())){
                ruku_per_content.setText(tvText);
            }
            if("经办时间".equals(func.getName())){
                ruku_datetime_content.setText(tvText);
            }
            if(ruku_remarks_tx.getText().toString().equals(func.getName())){
                ruku_remarks_content.setText(tvText);
            }
            if("物品名称".equals(func.getName())){
                goodsStorage.setStorageName(tvText);
            }
            if("入库数量".equals(func.getName())){
                goodsStorage.setStorageNums(tvText);
            }
            if("入库单价".equals(func.getName())){
                goodsStorage.setStoragePrice(tvText);
            }
            if("入库金额".equals(func.getName())){
                goodsStorage.setStorageMeney(tvText);
            }
        }
    }


    /**
     * 获取超链接显示的文字
     *
     * @param itemData
     *            数据map<funcId,value>
     * @param func
     *            Func对象
     * @param value
     *            根据funcId从itemData中取出的值
     * @return 返回超链接显示的文字
     */
    protected String getShowValue(Map<String, String> itemData, Func func,
                                  String value) {
        switch (func.getType()) {
            case Func.TYPE_SINGLE_CHOICE_FUZZY_QUERY_COMP:
            case Func.TYPE_SELECTCOMP:
                value = getSelectCompValue(func, value);
                break;
            case Func.TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY_COMP:// 模糊搜索其他
            case Func.TYPE_SELECT_OTHER:
                value = getSelectOtherCompValue(func, itemData, value);
                break;
            case Func.TYPE_MULTI_CHOICE_SPINNER_COMP:
            case Func.TYPE_MULTI_CHOICE_FUZZY_QUERY_COMP:// 模糊多选
                value = getMultiSelectCompValue(func, value);
                break;
            case Func.TYPE_CAMERA:
            case Func.TYPE_CAMERA_HEIGHT:
            case Func.TYPE_CAMERA_CUSTOM:
            case Func.TYPE_CAMERA_MIDDLE:
            case Func.TYPE_VIDEO:
            case Func.TYPE_ATTACHMENT:
            case Func.TYPE_TABLECOMP:
            case Func.TYPE_LINK:
                value = getResources().getString(R.string.activity_after_look);
                break;
            case Func.TYPE_LOCATION:
                value = TextUtils.isEmpty(value) ? value : value.split("\\$")[0];
                break;
            case Func.TYPE_RECORD:
                    value = getResources().getString(R.string.activity_after_play);
                break;
            case Func.TYPE_SQL_BIG_DATA:
                value = getResources().getString(R.string.activity_after_play);
                break;
        }

        return value;
    }




    /**
     * 在一个线程中用http获取数据
     */
    private void submitData() {
        // 初始化加载对话框
        dialogSc = new MyProgressDialog(this, R.style.CustomProgressDialog, getResources().getString(R.string.activity_after_04));
        // 显示加载对话框
        dialogSc.show();
        HashMap<String, String> map = setPutParams();

        RequestParams requestParams = new RequestParams();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            requestParams.put(entry.getKey(), entry.getValue());
            JLog.d(TAG, entry.getKey() + ":" + entry.getValue());
        }
        String baseUrlSearch = UrlInfo.getUrlSubmit(this);
        GcgHttpClient.getInstance(this).post(baseUrlSearch, requestParams, new HttpResponseListener() {
            @Override
            public void onStart() {
            }


            @Override
            public void onFailure(Throwable error, String content) {
//						currentPage = lastPage;
                fail(getResources().getString(R.string.activity_after_02));
            }

            @Override
            public void onFinish() {
//                elv_search.onRefreshComplete();
            }

            @Override
            public void onSuccess(int statusCode, String content) {
                // 获取JSON
                // 判断是否返回 "resultcode":"0001"
                if ("{\"resultcode\":\"0001\"}".equals(content)) {
                    JLog.d(TAG, "msg.obj   ====json===>>>>>" + content);

                    fail("没有查询到相关数据!");
                    return;
                }
                if ("{\"resultcode\":\"0000\"}".equals(content)) {
                    JLog.d(TAG, "msg.obj   ====json===>>>>>" + content);
                    File file = new File(Constants.PATH_TEMP + imagename);

                    if (file.exists()) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    upLoadImage();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    mHandler.sendEmptyMessage(UPLOAD_FAIL);
                                }
                            }
                        }).start();
                    }else{
                        dismissLoadDialog();// 关闭加载对话框
                        ToastOrder.makeText(context, "电子签名文件不存在", ToastOrder.LENGTH_LONG).show();
                    }

                }else{
                    fail("提交失败!");
                    return;
                }
                JLog.d(TAG, "JSON   =======>>>>>" + content);

            }

        });
    }

    private void upLoadImage() throws HTTPResponseResultException, JSONException,Exception {
        HashMap<String, String> param = new HashMap<>();

        param.put("name", imagename);
        param.put("targetid", "2031736");
        param.put("md5Code", MD5Helper
                .getMD5Checksum2(Constants.PATH_TEMP
                        + imagename));

        param.put("companyid", String
                .valueOf(SharedPreferencesUtil.getInstance(
                        ConfirmSupLibrayActivity.this).getCompanyId()));
        // 直接提交
        String url = UrlInfo.getUrlPhoto(context);
        String path=Constants.PATH_TEMP + imagename;

        HashMap<String, String> files = new HashMap<String, String>();
        files.put(SubmitWorkManager.FILE_KEY, path);
        String result = new HttpHelper(context).uploadFile(url, param,files);
        returned(result);
    }


    /**
     * 处理直接提交数据的返回值
     *
     * @param result
     * @throws HTTPResponseResultException
     * @throws JSONException
     */
    private void returned(String result) throws HTTPResponseResultException,
            JSONException {
        if (TextUtils.isEmpty(result)) {
            mHandler.sendEmptyMessage(UPLOAD_FAIL);
            throw new HTTPResponseResultException(context.getResources().getString(R.string.submitmanager_message01));

        }
        JLog.d(JDebugOptions.TAG_SUBMIT,context.getResources().getString(R.string.submitmanager_message02)+result);
        JSONObject json = new JSONObject(result);
        String code = json.getString(Constants.RESULT_CODE);
        if (code.equals(Constants.RESULT_CODE_SUCCESS)) {
            mHandler.sendEmptyMessage(UPLOAD_OK);
        } else if (code.equals(Constants.RESULT_CODE_FAILURE)) {
            mHandler.sendEmptyMessage(UPLOAD_FAIL);
            throw new HTTPResponseResultException(context.getResources().getString(R.string.submitmanager_message03), code);
        } else if (code.equals(Constants.RESULT_CODE_NO_REGISTER)) {
            mHandler.sendEmptyMessage(UPLOAD_FAIL);
            throw new HTTPResponseResultException(context.getResources().getString(R.string.submitmanager_message04), code);
        } else {
            mHandler.sendEmptyMessage(UPLOAD_FAIL);
            throw new HTTPResponseResultException(context.getResources().getString(R.string.submitmanager_message05));
        }
    }
}
