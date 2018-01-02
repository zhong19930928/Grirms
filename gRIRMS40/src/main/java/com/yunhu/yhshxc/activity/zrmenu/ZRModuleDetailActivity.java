package com.yunhu.yhshxc.activity.zrmenu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.yunhu.android.pulltorefresh.library.PullToRefreshBase;
import com.yunhu.android.pulltorefresh.library.PullToRefreshListView;
import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.AssetsBean;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.UrlInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.yunhu.yhshxc.R.id.zrmodule_menu;


public class ZRModuleDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView zrmodule_back;//返回键
    private TextView zrmodule_title;//标题
    private PullToRefreshListView zemodule_listview_mine;//我的资产数据
    private int lastItemIdMine = 0; //我的资产最后一条数据id
    private String id;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zrmodule_detail);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        title = intent.getStringExtra("title");
        initData();
        initView();
    }

    private void initView() {

        zrmodule_back = (ImageView) findViewById(R.id.zrmodule_back);
        zrmodule_title = (TextView) findViewById(R.id.zrmodule_title);
        zrmodule_title.setText(title);
        zemodule_listview_mine = (PullToRefreshListView) findViewById(R.id.zemodule_listview_mine);
        zrmodule_back.setOnClickListener(this);
        //我的资产
        zemodule_listview_mine.setMode(PullToRefreshBase.Mode.BOTH);
        String label = DateUtils.formatDateTime(this.getApplicationContext(), System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
        zemodule_listview_mine.getLoadingLayoutProxy().setLastUpdatedLabel(label);
        zemodule_listview_mine.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                lastItemIdMine = 0;
                loadData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (dataMine.size()>=1){
                    AssetsBean assetsBean = dataMine.get(dataMine.size() - 1);
                    lastItemIdMine = Integer.parseInt(assetsBean.getId());
                }

                loadData();
            }
        });
        zemodule_listview_mine.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AssetsBean bean = dataMine.get(position-1);
                ZRDialog dialog = new ZRDialog(ZRModuleDetailActivity.this);
                dialog.show();
                dialog.initData(bean);
            }
        });
        zemodule_listview_mine.setAdapter(adapterMine);
        //获取传递来的数据
        list = new ArrayList<>();
        //初始化数据
        loadData();
    }


    private void initData() {
        dataMine = new ArrayList<>();
        adapterMine = new ZRModuleAdapter(this, dataMine,1);
    }

    private String moduleId;
    private String dyType;
    private List<AssetsBean> dataMine;//查询出的data
    private ZRModuleAdapter adapterMine;//适配器,我的资产

    //根据传递来的参数类型进行数据的获取展示
    private void loadData() {
        if (lastItemIdMine==0){
            dataMine.clear();
        }
        moduleId = "top";
        dyType = "3";
        String url = UrlInfo.baseAssetsUrl(this);
        String phoneno = PublicUtils.receivePhoneNO(this);
        RequestParams params = new RequestParams();
        params.add("phoneno", phoneno);
        params.add("test", "gcg");
        params.add("moduleId", moduleId);
        params.add("dyType", dyType);

        if (lastItemIdMine!=0){
            params.add("5",lastItemIdMine+"");
        }
        JSONObject object = new JSONObject();
        try {
            object.put("scan_type",id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.add("dynamic_param",object.toString());
        GcgHttpClient.getInstance(this).get(url, params, new HttpResponseListener() {
            @Override
            public void onStart() {

            }
            @Override
            public void onFinish() {
                //取消加载框,通知适配器更新数据
                zemodule_listview_mine.onRefreshComplete();

            }
            @Override
            public void onSuccess(int statusCode, String content) {
                Log.d("views", "content: " + content);
                //根据类型进行相应的解析
                toParseIdle(content);
                if (dataMine.size() < 1) {
                    Toast.makeText(ZRModuleDetailActivity.this, "未查询到数据", Toast.LENGTH_SHORT).show();
                }
                adapterMine.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Throwable error, String content) {
                Toast.makeText(ZRModuleDetailActivity.this, "获取数据失败!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //请求其他资产信息



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zrmodule_back:
                this.finish();
                break;
            case zrmodule_menu:
                break;
        }


    }

    private List<Menu> list;



    //解析闲置数据
    private void toParseIdle(String content) {
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
                for (int i = 0; i < arrayData.length(); i++) {
                    AssetsBean assetsBean = new AssetsBean();
                    JSONArray jsonArray = arrayData.getJSONArray(i);
                    assetsBean.setId(String.valueOf(jsonArray.getInt(0)));
                    assetsBean.setIdTip(String.valueOf(arrayTitle.get(0)));
                    assetsBean.setState(jsonArray.getString(1));
                    assetsBean.setStateTip(arrayTitle.getString(1));
                    assetsBean.setUrl(jsonArray.getString(2));
                    assetsBean.setUrlTip(arrayTitle.getString(2));
                    assetsBean.setTitle(jsonArray.getString(3));
                    assetsBean.setTitleTip(arrayTitle.getString(3));
                    assetsBean.setCode(jsonArray.getString(4));
                    assetsBean.setCodeTip(arrayTitle.getString(4));
                    assetsBean.setSn(jsonArray.getString(5));
                    assetsBean.setSnTip(arrayTitle.getString(5));
                    assetsBean.setArea(jsonArray.getString(6));
                    assetsBean.setAreaTip(arrayTitle.getString(6));
                    assetsBean.setPutAddress(jsonArray.getString(7));
                    assetsBean.setPutAddressTip(arrayTitle.getString(7));
                    assetsBean.setUseCompany(jsonArray.getString(8));
                    assetsBean.setUseCompanyTip(arrayTitle.getString(8));
                    assetsBean.setUsePart(jsonArray.getString(9));
                    assetsBean.setUsePartTip(arrayTitle.getString(9));
                    assetsBean.setKind(jsonArray.getString(10));
                    assetsBean.setKindTip(arrayTitle.getString(10));
                    assetsBean.setFineKind(jsonArray.getString(11));
                    assetsBean.setFineKindTip(arrayTitle.getString(11));
                    assetsBean.setProKind(jsonArray.getString(12));
                    assetsBean.setProKindTip(arrayTitle.getString(12));
                    assetsBean.setBelongKind(jsonArray.getString(13));
                    assetsBean.setBelongKindTip(arrayTitle.getString(13));
                    dataMine.add(assetsBean);

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void toParseIdle4(String content){
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
                for (int i = 0; i < arrayData.length(); i++) {
                    AssetsBean assetsBean = new AssetsBean();
                    JSONArray jsonArray = arrayData.getJSONArray(i);
                    assetsBean.setId(String.valueOf(jsonArray.getInt(0)));
                    assetsBean.setUrl(jsonArray.getString(1));
                    assetsBean.setTitle(jsonArray.getString(2));
                    assetsBean.setNum(String.valueOf(jsonArray.getInt(3)));
                    assetsBean.setIdTip(arrayTitle.getString(0));
                    assetsBean.setUrlTip(arrayTitle.getString(1));
                    assetsBean.setTitleTip(arrayTitle.getString(2));
                    assetsBean.setNumTip(arrayTitle.getString(3));
//                    dataOhter.add(assetsBean);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


}
