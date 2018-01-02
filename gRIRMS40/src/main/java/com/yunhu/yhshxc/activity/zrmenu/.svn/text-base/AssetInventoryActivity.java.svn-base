package com.yunhu.yhshxc.activity.zrmenu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.zxing.activity.CaptureActivity;
import com.loopj.android.http.RequestParams;
import com.yunhu.android.pulltorefresh.library.PullToRefreshBase;
import com.yunhu.android.pulltorefresh.library.PullToRefreshListView;
import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.zrmenu.adapter.InventoryAdapter;
import com.yunhu.yhshxc.activity.zrmenu.adapter.MenuAdapter;
import com.yunhu.yhshxc.activity.zrmenu.module.AssetBean;
import com.yunhu.yhshxc.activity.zrmenu.module.Surplus;
import com.yunhu.yhshxc.bo.AssetsBean;
import com.yunhu.yhshxc.core.ApiRequestFactory;
import com.yunhu.yhshxc.core.ApiRequestMethods;
import com.yunhu.yhshxc.core.ToastUtils;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.UrlInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * @author suhu
 * @data 2017/10/23.
 * @description 资产盘点详情
 */
public class AssetInventoryActivity extends AppCompatActivity {

    @BindView(R.id.zrmodule_back)
    ImageView zrmoduleBack;
    @BindView(R.id.zrmodule_title)
    TextView zrmoduleTitle;
    @BindView(R.id.zrmodule_menu)
    LinearLayout zrmoduleMenu;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.number)
    TextView number;
    @BindView(R.id.wait)
    TextView wait;
    @BindView(R.id.complete)
    TextView complete;
    @BindView(R.id.over)
    TextView over;
    @BindView(R.id.wait_list_view)
    PullToRefreshListView waitListView;
    @BindView(R.id.already_list_view)
    PullToRefreshListView alreadyListView;
    @BindView(R.id.over_list_view)
    PullToRefreshListView overListView;


    private static final int SCAN = 111;
    private static final int ALTER = 222;

    private AssetBean.DataBean dataBean;
    private InventoryAdapter waitAdapter, alreadyAdapter, overAdapter;
    private List<String> listMenu;

    private String moduleId;
    private String dyType;

    private List<AssetsBean> waitAssetList, alreadyAssetList, overAssetList;


    private int pageWait = 0, pageAlready = 0;

    private String code;
    private String pdid;
    private SharedPreferencesUtil share;

    /**
     * 待盘资产id数组
     */
    private String[] dpzichanid;

    /**
     * 已盘资产id数组
     */
    private String[] ypzichanid;

    /**
     * 盘盈资产id数组
     */
    private String[] pyzichanid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_inventory);
        ButterKnife.bind(this);
        initData();
        initView();
        requestData();

    }

    private void initData() {
        Intent intern = getIntent();
        if (intern != null) {
            Bundle bundle = intern.getExtras();
            dataBean = (AssetBean.DataBean) bundle.getSerializable("date");
        }
        if (dataBean != null) {
            content.setText(dataBean.getName());
            name.setText(dataBean.getAdminname());
            number.setText(dataBean.getZichannum());
            wait.setText(dataBean.getDpnum());
            complete.setText(dataBean.getYpnum());
            over.setText(dataBean.getPynum());

            String dpId = dataBean.getDpzichanid();
            if (dpId != null&&!dpId.equals("")) {
                dpzichanid = dpId.split(",");
            }

            String ypId = dataBean.getYpzichanid();
            if (ypId != null&&!ypId.equals("")) {
                ypzichanid = ypId.split(",");
            }

            String pyId = dataBean.getPyzichanid();
            if (pyId != null&&!pyId.equals("")) {
                pyzichanid = pyId.split(",");
            }

            pdid = dataBean.getId()+"";

        }

        share = SharedPreferencesUtil.getInstance(this);

        listMenu = new ArrayList<>();
        listMenu.add("扫描");
        listMenu.add("盘盈");
    }

    private void initView() {
        initWait();
        initAlready();
        initOver();
    }

    private void requestData() {

        waitAssetList = new ArrayList<>();
        alreadyAssetList = new ArrayList<>();
        overAssetList = new ArrayList<>();

        //待盘点
        if (dpzichanid != null&&(!"".equals(dpzichanid))) {
            if (dpzichanid.length <= pageWait + 10) {
                List<String> list = new ArrayList<>();
                for (int i = pageWait; i < dpzichanid.length; i++) {
                    list.add(dpzichanid[i]);
                }
                requestData(list, 0);
            } else {
                List<String> list = new ArrayList<>();
                for (int i = pageWait; i < pageWait + 10; i++) {
                    list.add(dpzichanid[i]);
                }
                requestData(list, 0);
            }
        }


        //已盘点

        if (ypzichanid != null&&!"".equals(ypzichanid)) {
            if (ypzichanid.length <= pageAlready + 10) {
                List<String> list = new ArrayList<>();
                for (int i = pageAlready; i < ypzichanid.length; i++) {
                    list.add(ypzichanid[i]);
                }

                requestData(list, 1);
            } else {
                List<String> list = new ArrayList<>();
                for (int i = pageAlready; i < pageAlready + 10; i++) {
                    list.add(ypzichanid[i]);
                }
                requestData(list, 1);
            }
        }
        getPandYingData();

    }


    private void initOver() {
        //盘盈
        overAdapter = new InventoryAdapter(this, 2);
        overListView.setAdapter(overAdapter);
        overAdapter.setOnItemClick(new InventoryAdapter.OnItemClick() {
            @Override
            public void click(int position, View view) {
                boolean check = overAssetList.get(position).isCheck();
                if (check) {
                    check = false;
                } else {
                    check = true;
                }
                overAssetList.get(position).setCheck(check);
                overAdapter.notifyDataSetChanged();
            }
        });

    }

    private void initAlready() {
        //已盘点
        alreadyAdapter = new InventoryAdapter(this, 1);
        String label = DateUtils.formatDateTime(this.getApplicationContext(), System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
        alreadyListView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
        alreadyListView.setMode(PullToRefreshBase.Mode.BOTH);
        alreadyListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageAlready = 0;
                if (ypzichanid != null&&!ypzichanid.equals("")) {
                    if (ypzichanid.length <= pageAlready + 10) {
                        List<String> list = new ArrayList<>();
                        for (int i = pageAlready; i < ypzichanid.length; i++) {
                            list.add(ypzichanid[i]);
                        }
                        if (list.size() == 0) {
                            alreadyListView.onRefreshComplete();
                        } else {
                            requestData(list, 1);
                        }
                    } else {
                        List<String> list = new ArrayList<>();
                        for (int i = pageAlready; i < pageAlready + 10; i++) {
                            list.add(ypzichanid[i]);
                        }
                        if (list.size() == 0) {
                            alreadyListView.onRefreshComplete();
                        } else {
                            requestData(list, 1);
                        }
                    }
                } else {
                    alreadyListView.onRefreshComplete();
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (ypzichanid != null&&!ypzichanid.equals("")) {
                    if (ypzichanid.length <= pageAlready + 10) {
                        List<String> list = new ArrayList<>();
                        for (int i = pageAlready; i < ypzichanid.length; i++) {
                            list.add(ypzichanid[i]);
                        }
                        if (list.size() == 0) {
                            alreadyListView.onRefreshComplete();
                        } else {
                            requestData(list, 1);
                        }
                    } else {
                        List<String> list = new ArrayList<>();
                        for (int i = pageAlready; i < pageAlready + 10; i++) {
                            list.add(ypzichanid[i]);
                        }
                        if (list.size() == 0) {
                            alreadyListView.onRefreshComplete();
                        } else {
                            requestData(list, 1);
                        }
                    }
                } else {
                    alreadyListView.onRefreshComplete();
                }
            }
        });

        alreadyListView.setAdapter(alreadyAdapter);
        alreadyAdapter.setOnItemClick(new InventoryAdapter.OnItemClick() {
            @Override
            public void click(int position, View view) {
                boolean check = alreadyAssetList.get(position).isCheck();
                if (check) {
                    check = false;
                } else {
                    check = true;
                }
                alreadyAssetList.get(position).setCheck(check);
                alreadyAdapter.notifyDataSetChanged();
            }
        });

    }

    private void initWait() {
        //待盘点
        waitAdapter = new InventoryAdapter(this, 0);
        String label = DateUtils.formatDateTime(this.getApplicationContext(), System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
        waitListView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
        waitListView.setMode(PullToRefreshBase.Mode.BOTH);
        waitListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageWait = 0;
                if (dpzichanid != null&&!dpzichanid.equals("")) {
                    if (dpzichanid.length <= pageWait + 10) {
                        List<String> list = new ArrayList<>();
                        for (int i = pageWait; i < dpzichanid.length; i++) {
                            list.add(dpzichanid[i]);
                        }
                        if (list.size() == 0) {
                            waitListView.onRefreshComplete();
                        } else {
                            requestData(list, 0);
                        }
                    } else {
                        List<String> list = new ArrayList<>();
                        for (int i = pageWait; i < pageWait + 10; i++) {
                            list.add(dpzichanid[i]);
                        }
                        requestData(list, 0);
                    }
                } else {
                    waitListView.onRefreshComplete();
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (dpzichanid != null&&!dpzichanid.equals("")) {
                    if (dpzichanid.length <= pageWait + 10) {
                        List<String> list = new ArrayList<>();
                        for (int i = pageWait; i < dpzichanid.length; i++) {
                            list.add(dpzichanid[i]);
                        }
                        if (list.size() == 0) {
                            waitListView.onRefreshComplete();
                        } else {
                            requestData(list, 0);
                        }

                    } else {
                        List<String> list = new ArrayList<>();
                        for (int i = pageWait; i < pageWait + 10; i++) {
                            list.add(dpzichanid[i]);
                        }
                        if (list.size() == 0) {
                            waitListView.onRefreshComplete();
                        } else {
                            requestData(list, 0);
                        }
                    }
                } else {
                    waitListView.onRefreshComplete();
                }
            }
        });
        waitListView.setAdapter(waitAdapter);
        waitAdapter.setOnItemClick(new InventoryAdapter.OnItemClick() {
            @Override
            public void click(int position, View view) {
                boolean check = waitAssetList.get(position).isCheck();
                if (check) {
                    check = false;
                } else {
                    check = true;
                }
                waitAssetList.get(position).setCheck(check);
                waitAdapter.notifyDataSetChanged();
            }
        });
        waitListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AssetInventoryActivity.this, AssetAlterActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", waitAssetList.get(position-1));
                bundle.putSerializable("bean",dataBean);
                bundle.putString("id",dpzichanid[position-1]);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


    }


    @OnClick({R.id.zrmodule_back, R.id.zrmodule_menu, R.id.wait_ll, R.id.already_ll, R.id.over_ll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.zrmodule_back:
                finish();
                break;
            case R.id.zrmodule_menu:
                showDialog();
                break;
            case R.id.wait_ll:
                alreadyListView.setVisibility(View.GONE);
                overListView.setVisibility(View.GONE);
                waitListView.setVisibility(View.VISIBLE);
                break;
            case R.id.already_ll:
                waitListView.setVisibility(View.GONE);
                overListView.setVisibility(View.GONE);
                alreadyListView.setVisibility(View.VISIBLE);
                break;
            case R.id.over_ll:
                waitListView.setVisibility(View.GONE);
                alreadyListView.setVisibility(View.GONE);
                overListView.setVisibility(View.VISIBLE);
                break;
            default:
        }
    }

    private void showDialog() {
        MenuAdapter adapter = new MenuAdapter(this, listMenu);
        View view = LayoutInflater.from(this).inflate(R.layout.popmenu_list_menu, null);
        ListView menuListView = (ListView) view.findViewById(R.id.popwindow_listview);
        menuListView.setAdapter(adapter);
        int px = PublicUtils.convertDIP2PX(this, 130);
        int py = PublicUtils.convertDIP2PX(this, (listMenu.size()) * 57);
        final PopupWindow popupWindow = new PopupWindow(view, px, py + 40);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.asset_showwindows_bg));

        backgroundAlpha(0.5f);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(zrmoduleTitle, 300, 50);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });
        menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                switch (listMenu.get(position)) {
                    case "扫描":
                        scan();
                        break;
                    case "盘盈":
                        surplus(null);
                        break;
                    default:
                }

            }


        });

    }


    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    /**
     * 扫一扫
     */
    private void scan() {
        Intent i = new Intent(this, CaptureActivity.class);
        startActivityForResult(i, SCAN);
    }

    private void surplus(String code) {
        Intent intent = new Intent(this, AssetSurplusActivity.class);
        if (code != null) {
            intent.putExtra("code", code);
        }
        intent.putExtra("id",pdid);
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK == resultCode || resultCode == R.id.scan_succeeded) {
            //扫描
            if (requestCode == SCAN) {
                String code = data.getStringExtra("SCAN_RESULT");
                //用rs去查，如果没有则跳到盘盈
                requestCode(code);
            }
            if (requestCode == ALTER){
                getPandYingData();
            }
        }

    }


    /**
     * @param ids
     * @param typ 状态
     *            0：待盘点
     *            1：已盘点
     *            2：盘盈
     * @method 请求数据
     * @author suhu
     * @time 2017/10/30 9:24
     */
    private void requestData(List<String> ids, final int typ) {
        moduleId = "top";
        dyType = "9";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ids.size(); i++) {
            if (i == ids.size() - 1) {
                sb.append(ids.get(i));
            } else {
                sb.append(ids.get(i)).append(",");
            }
        }

        String url = UrlInfo.baseAssetsUrl(this);
        String phoneno = PublicUtils.receivePhoneNO(this);
        RequestParams params = new RequestParams();
        params.add("phoneno", phoneno);
        params.add("test", "gcg");
        params.add("moduleId", moduleId);
        params.add("dyType", dyType);
        params.add("5", sb.toString());
        GcgHttpClient.getInstance(this).get(url, params, new HttpResponseListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onSuccess(int statusCode, String content) {
                List<AssetsBean> list = toParseIdle4(content);
                if (list == null) {
                    return;
                }
                switch (typ) {
                    case 0:
                        waitListView.onRefreshComplete();
                        if (pageWait == 0) {
                            waitAssetList.clear();
                        }
                        waitAssetList.addAll(list);
                        waitAdapter.setAssetList(waitAssetList);
                        pageWait = pageWait + 10;
                        break;
                    case 1:
                        alreadyListView.onRefreshComplete();
                        if (pageAlready == 0) {
                            alreadyAssetList.clear();
                        }
                        alreadyAssetList.addAll(list);
                        alreadyAdapter.setAssetList(alreadyAssetList);
                        pageAlready = pageAlready + 10;
                        break;
                    default:
                }

            }

            @Override
            public void onFailure(Throwable error, String content) {

            }
        });
    }

    private void requestCode(String code) {
        moduleId = "top";
        dyType = "3";
        String url = UrlInfo.baseAssetsUrl(this);
        String phoneno = PublicUtils.receivePhoneNO(this);
        RequestParams params = new RequestParams();
        params.add("phoneno", phoneno);
        params.add("test", "gcg");
        params.add("moduleId", moduleId);
        params.add("dyType", dyType);
        StringBuilder sb = new StringBuilder();
        JSONObject localJSONObject = new JSONObject();
        try {
            localJSONObject.put("scan_cols", code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.add("dynamic_param", localJSONObject.toString());
        GcgHttpClient.getInstance(this).get(url, params, new HttpResponseListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onSuccess(int statusCode, String content) {
                toParseIdle3(content);
            }

            @Override
            public void onFailure(Throwable error, String content) {

            }
        });


    }


    private List<AssetsBean> toParseIdle4(String content) {
        List<AssetsBean> list = null;
        if (TextUtils.isEmpty(content)) {
            return null;
        }
        try {
            JSONObject jsonObject = new JSONObject(content);
            if (!jsonObject.getString("resultcode").equals("0000")) {
                return null;
            }
            if (jsonObject.has("dynamic_data") && jsonObject.has("dynamic_title")) {
                JSONArray arrayData = jsonObject.getJSONArray("dynamic_data");
                JSONArray arrayTitle = jsonObject.getJSONArray("dynamic_title");
                if (arrayData.length() < 1 || arrayTitle.length() < 1) {
                    return null;
                }
                list = new ArrayList<>();
                for (int i = 0; i < arrayData.length(); i++) {
                    AssetsBean assetsBean = new AssetsBean();
                    JSONArray jsonArray = arrayData.getJSONArray(i);
                    assetsBean.setId(jsonArray.getString(0));
                    assetsBean.setState(jsonArray.getString(1));
                    assetsBean.setUrl(jsonArray.getString(2));
                    assetsBean.setTitle(jsonArray.getString(3));
                    assetsBean.setCode(jsonArray.getString(4));
                    assetsBean.setSn(jsonArray.getString(5));
                    assetsBean.setArea(jsonArray.getString(6));
                    assetsBean.setPutAddress(jsonArray.getString(7));
                    assetsBean.setUseCompany(jsonArray.getString(8));
                    assetsBean.setUsePart(jsonArray.getString(9));
                    assetsBean.setKind(jsonArray.getString(10));
                    assetsBean.setFineKind(jsonArray.getString(11));
                    assetsBean.setProKind(jsonArray.getString(12));
                    assetsBean.setBelongKind(jsonArray.getString(13));

                    assetsBean.setIdTip(arrayTitle.getString(0));
                    assetsBean.setStateTip(arrayTitle.getString(1));
                    assetsBean.setUrlTip(arrayTitle.getString(2));
                    assetsBean.setTitleTip(arrayTitle.getString(3));
                    assetsBean.setCodeTip(arrayTitle.getString(4));
                    assetsBean.setSnTip(arrayTitle.getString(5));
                    assetsBean.setAreaTip(arrayTitle.getString(6));
                    assetsBean.setPutAddressTip(arrayTitle.getString(7));
                    assetsBean.setUseCompanyTip(arrayTitle.getString(8));
                    assetsBean.setUsePartTip(arrayTitle.getString(9));
                    assetsBean.setKindTip(arrayTitle.getString(10));
                    assetsBean.setFineKindTip(arrayTitle.getString(11));
                    assetsBean.setProKindTip(arrayTitle.getString(12));
                    assetsBean.setBelongKindTip(arrayTitle.getString(13));

                    list.add(assetsBean);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }


    private void toParseIdle3(String content) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(content);
            if (!jsonObject.getString("resultcode").equals("0000")) {
                return;
            }
            if (jsonObject.has("dynamic_data") && jsonObject.has("dynamic_title")) {
                JSONArray arrayData = jsonObject.getJSONArray("dynamic_data");
                JSONArray arrayTitle = jsonObject.getJSONArray("dynamic_title");
                if (arrayData.length() < 1 || arrayTitle.length() < 1) {

                    return;
                }
                AssetsBean assetsBean = new AssetsBean();
                JSONArray jsonArray = arrayData.getJSONArray(0);
                assetsBean.setId(jsonArray.getString(0));
                assetsBean.setState(jsonArray.getString(1));
                assetsBean.setUrl(jsonArray.getString(2));
                assetsBean.setTitle(jsonArray.getString(3));
                assetsBean.setCode(jsonArray.getString(4));
                assetsBean.setSn(jsonArray.getString(5));
                assetsBean.setArea(jsonArray.getString(6));
                assetsBean.setPutAddress(jsonArray.getString(7));
                assetsBean.setUseCompany(jsonArray.getString(8));
                assetsBean.setUsePart(jsonArray.getString(9));
                assetsBean.setKind(jsonArray.getString(10));
                assetsBean.setFineKind(jsonArray.getString(11));
                assetsBean.setProKind(jsonArray.getString(12));
                assetsBean.setBelongKind(jsonArray.getString(13));

                assetsBean.setIdTip(arrayTitle.getString(0));
                assetsBean.setStateTip(arrayTitle.getString(1));
                assetsBean.setUrlTip(arrayTitle.getString(2));
                assetsBean.setTitleTip(arrayTitle.getString(3));
                assetsBean.setCodeTip(arrayTitle.getString(4));
                assetsBean.setSnTip(arrayTitle.getString(5));
                assetsBean.setAreaTip(arrayTitle.getString(6));
                assetsBean.setPutAddressTip(arrayTitle.getString(7));
                assetsBean.setUseCompanyTip(arrayTitle.getString(8));
                assetsBean.setUsePartTip(arrayTitle.getString(9));
                assetsBean.setKindTip(arrayTitle.getString(10));
                assetsBean.setFineKindTip(arrayTitle.getString(11));
                assetsBean.setProKindTip(arrayTitle.getString(12));
                assetsBean.setBelongKindTip(arrayTitle.getString(13));

                Intent intent = new Intent(AssetInventoryActivity.this, AssetAlterActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", assetsBean);
                bundle.putSerializable("bean", dataBean);
                intent.putExtras(bundle);
                startActivityForResult(intent,ALTER);
            }else {

                Intent intent = new Intent(AssetInventoryActivity.this,AssetSurplusActivity.class);
                intent.putExtra("code",code);
                intent.putExtra("id",pdid);
                startActivity(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getPandYingData(){
        ApiRequestMethods.CheckPanYing(this, pdid, share.getCompanyId(), share.getUserId(), new ApiRequestFactory.HttpCallBackListener() {
            @Override
            public void onSuccess(String response, String url, int id) {
                JSONObject object = null;
                try {
                    object = new JSONObject(response).getJSONObject("data");
                    int flag = object.getInt("flag");
                    switch (flag) {
                        case -3:
                            ToastUtils.disPlayShort(AssetInventoryActivity.this, "参数为空");
                            return;
                        case -1:
                            ToastUtils.disPlayShort(AssetInventoryActivity.this, "提交失败");
                            return;
                        case 0:
                            if (!object.has("data")) {
                                return;
                            }
                        default:
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Gson gson = new Gson();
               Surplus surplus = gson.fromJson(object.toString(), Surplus.class);
               if (surplus!=null){
                  List<Surplus.DataBean> list = surplus.getData();
                   for (Surplus.DataBean bean : list) {
                       AssetsBean data = toConversions(bean);
                       overAssetList.add(data);
                   }
                   overAdapter.setAssetList(overAssetList);
               }

            }

            @Override
            public void failure(Call call, Exception e, int id) {

            }
        }, true);

    }

    private AssetsBean toConversions(Surplus.DataBean bean){
        AssetsBean assetsBean = new AssetsBean();
        assetsBean.setUrl(bean.getThumb());
        assetsBean.setTitle(bean.getZcname());
        assetsBean.setUseCompany(bean.getUnit());
        assetsBean.setUsePart(bean.getBumen_id());
        return assetsBean;
    }

}
