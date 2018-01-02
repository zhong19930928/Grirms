package com.yunhu.yhshxc.activity.zrmenu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ListView;

import com.google.gson.Gson;
import com.yunhu.android.pulltorefresh.library.PullToRefreshBase;
import com.yunhu.android.pulltorefresh.library.PullToRefreshListView;
import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.zrmenu.adapter.AssetAdapter;
import com.yunhu.yhshxc.activity.zrmenu.module.AssetBean;
import com.yunhu.yhshxc.core.ApiRequestFactory;
import com.yunhu.yhshxc.core.ApiRequestMethods;
import com.yunhu.yhshxc.core.ToastUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;

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
 * @description 资产盘点列表
 */

public class AssetDiscoveryActivity extends AppCompatActivity {

    @BindView(R.id.list_view)
    PullToRefreshListView freshView;


    private SharedPreferencesUtil share;
    private AssetBean dean;
    private List<AssetBean.DataBean> list;
    private AssetAdapter adapter;
    private int page = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_discovery);
        ButterKnife.bind(this);
        initView();
        getData();

    }

    private void initView() {
        share = SharedPreferencesUtil.getInstance(this);
        String label = DateUtils.formatDateTime(this.getApplicationContext(), System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
        freshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
        freshView.setMode(PullToRefreshBase.Mode.BOTH);
        freshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 0;
                getData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getData();
            }
        });

        list = new ArrayList<>();
        //list = jiashuju();
        adapter = new AssetAdapter(this,list);
        freshView.setAdapter(adapter);
        adapter.setOnItemClick(new AssetAdapter.OnItemClick() {
            @Override
            public void click(int position, View view) {
                AssetBean.DataBean dataBean = list.get(position);
                switch (view.getId()){
                    case R.id.item_start:
                        Intent intent = new Intent(AssetDiscoveryActivity.this,AssetInventoryActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("date",dataBean);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    case R.id.item_upload:
                        submit(dataBean.getId());
                        break;
                    case R.id.item_delete:
                        delete(dataBean.getId());
                        break;
                    default:
                }
            }
        });
    }



    @OnClick(R.id.zrmodule_back)
    public void onViewClicked() {
        finish();
    }

    private void getData(){
        int cid = share.getCompanyId();
        int id = share.getUserId();
        // 10358 , 6123100
        ApiRequestMethods.checkList(this, cid, id,page, new ApiRequestFactory.HttpCallBackListener() {
            @Override
            public void onSuccess(String response, String url, int id) {
                freshView.onRefreshComplete();
                JSONObject object = null;
                try {
                    object = new JSONObject(response).getJSONObject("data");
                    int flag = object.getInt("flag");
                    switch (flag) {
                        case -3:
                            ToastUtils.disPlayShort(AssetDiscoveryActivity.this, "参数为空");
                            return;
                        case -5:
                            ToastUtils.disPlayShort(AssetDiscoveryActivity.this, "无数据");
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
                dean = new Gson().fromJson(object.toString(),AssetBean.class);
                if (page==0){
                    list.clear();
                }
                list.addAll(dean.getData());
                adapter.setList(list);
                page++;
            }

            @Override
            public void failure(Call call, Exception e, int id) {

            }
        },true);
    }

    private void delete(int id){
        ApiRequestMethods.deleteCheck(this, id, new ApiRequestFactory.HttpCallBackListener() {
            @Override
            public void onSuccess(String response, String url, int id) {
                JSONObject object = null;
                try {
                    object = new JSONObject(response).getJSONObject("data");
                    int flag = object.getInt("flag");
                    switch (flag) {
                        case -3:
                            ToastUtils.disPlayShort(AssetDiscoveryActivity.this, "参数为空");
                            return;
                        case -2:
                            ToastUtils.disPlayShort(AssetDiscoveryActivity.this, "该盘点单未完成");
                            return;
                        case -1:
                            ToastUtils.disPlayShort(AssetDiscoveryActivity.this, "提交失败");
                            return;
                        case 0:
                            ToastUtils.disPlayShort(AssetDiscoveryActivity.this, "删除成功");
                            page = 0;
                            getData();
                            break;
                        default:
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(Call call, Exception e, int id) {

            }
        },true);
    }

    private void submit(int id){
        ApiRequestMethods.submitCheck(this, id, new ApiRequestFactory.HttpCallBackListener() {
            @Override
            public void onSuccess(String response, String url, int id) {
                JSONObject object = null;
                try {
                    object = new JSONObject(response).getJSONObject("data");
                    int flag = object.getInt("flag");
                    switch (flag) {
                        case -3:
                            ToastUtils.disPlayShort(AssetDiscoveryActivity.this, "参数为空");
                            return;
                        case -1:
                            ToastUtils.disPlayShort(AssetDiscoveryActivity.this, "提交失败");
                            return;
                        case 0:
                            ToastUtils.disPlayShort(AssetDiscoveryActivity.this, "提交成功");
                            page = 0;
                            getData();
                            break;
                        default:
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(Call call, Exception e, int id) {

            }
        },true);
    }

    private List<AssetBean.DataBean> jiashuju(){
        List<AssetBean.DataBean> list = new ArrayList<>();
        AssetBean.DataBean dataBean = new AssetBean.DataBean();

        for (int i = 0; i < 2; i++) {
            dataBean.setId(123);
            dataBean.setName("test");
            dataBean.setAdminid("asdsad");
            dataBean.setInserttime("2017-10-25");
            dataBean.setAdminname("suhu");
            dataBean.setZichannum("7");
            dataBean.setYpnum("8");
            dataBean.setYpzichanid("11");
            dataBean.setDpnum("4");
            dataBean.setDpzichanid("44");
            dataBean.setPyzichanid("ss");
            dataBean.setPynum("1");
            list.add(dataBean);
        }
        return list;
    }
}
