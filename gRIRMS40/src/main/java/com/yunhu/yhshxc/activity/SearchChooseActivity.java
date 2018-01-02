package com.yunhu.yhshxc.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.yunhu.android.pulltorefresh.library.PullToRefreshBase;
import com.yunhu.android.pulltorefresh.library.PullToRefreshListView;
import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.adapter.SearchChooseAdapter;
import com.yunhu.yhshxc.bo.AssetsBean;
import com.yunhu.yhshxc.core.ApiRequestFactory;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil2;
import com.yunhu.yhshxc.utility.UrlInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.attr.id;


/**
 * 数据查询选择界面
 */
public class SearchChooseActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ImageView search_back;//返回
    private SearchView search_mSearchview;//查询
    private TextView search_searchtip;
    private PullToRefreshListView search_mListView;//数据列表
    private Button search_search_confir;//确定按钮
    private SearchChooseAdapter adapter;//适配器
    private List<AssetsBean> data;//查询出的data
   private  int  lastItemId = 0;
    private List<AssetsBean> comeData;
    //查询参数
    private String moduleId;//模块id
    private String dyType;//类型
    private int status = -1;
    //返回参数key
    private static final String DYNAMIC_DATA = "dynamic_data";//列表数据数组
    private static final String DYNAMIC_TITLE = "dynamic_title";//列表数据标题

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }
        setContentView(R.layout.activity_search_choose);
        initComeData();
        initView();
        initType();
        loadData();
    }
    //获取传递过来的数据
    private void initComeData() {
        comeData = (List<AssetsBean>) getIntent().getSerializableExtra("select_data");

    }


    private void initView() {
        search_back = (ImageView) findViewById(R.id.search_back);
        search_mSearchview = (SearchView) findViewById(R.id.search_mSearchview);
        search_searchtip = (TextView) findViewById(R.id.search_searchtip);
        search_back.setOnClickListener(this);
        search_mSearchview.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_searchtip.setVisibility(View.GONE);
            }
        });
        search_mSearchview.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                search_searchtip.setVisibility(View.VISIBLE);
                return false;
            }
        });
        search_mListView = (PullToRefreshListView) findViewById(R.id.search_mListView);
        search_search_confir = (Button) findViewById(R.id.search_search_confir);
        search_search_confir.setOnClickListener(this);
        search_mSearchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                 if (!TextUtils.isEmpty(s)){
                     //有内容则进行筛选
                     filterData(s);
                     search_mListView.setMode(PullToRefreshBase.Mode.DISABLED);

                  }else{
                     loadData();
                     search_mListView.setMode(PullToRefreshBase.Mode.BOTH);
                 }


                return false;
            }
        });

        data = new ArrayList<>();
        adapter = new SearchChooseAdapter(this, data);
        adapter.setComeData(comeData);
        search_mListView.setAdapter(adapter);
        search_mListView.setOnItemClickListener(this);
        search_mListView.setMode(PullToRefreshBase.Mode.BOTH);
        String label = DateUtils.formatDateTime(this.getApplicationContext(), System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
        search_mListView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
        search_mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                lastItemId = 0;
                loadData();

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (data.size()>0){
                    AssetsBean assetsBean = data.get(data.size() - 1);
                    lastItemId = Integer.parseInt(assetsBean.getId());
                }

                loadData();
            }




        });

    }
    //根据搜索进行内容过滤显示
    private void filterData(String filterStr) {
        for (int i = 0; i < data.size(); i++) {
            AssetsBean assetsBean = data.get(i);
            if (!assetsBean.getTitle().contains(filterStr)){
                data.remove(assetsBean);
                i--;
            }
        }
        adapter.notifyDataSetChanged();
    }

    //获取传递过来的参数类型
    private void initType() {
        Intent intent = getIntent();
        if (intent == null)
            return;
        moduleId = intent.getStringExtra("moduleId");
        dyType = intent.getStringExtra("dyType");
        status = getIntent().getIntExtra("status",-1);


    }

    //根据传递来的参数类型进行数据的获取展示
    private void loadData() {
        if (lastItemId==0){//当下拉刷新时清空当前所有数据,上拉加载则继续添加总数据
            data.clear();
        }
        //调试数据
        moduleId = "top";
        dyType = "3";
        String url = UrlInfo.baseAssetsUrl(this);
       String phoneno = PublicUtils.receivePhoneNO(this);
//        String phoneno = "15555555551";
        RequestParams params = new RequestParams();
        params.add("phoneno", phoneno);
        params.add("test", "gcg");
        params.add("moduleId", moduleId);
        params.add("dyType", dyType);
        String stat = "\""+status+"\"";
        String stat1 = "{\"scan_status\""+":"+stat+"}";
        params.add("dynamic_param",stat1);
        if (lastItemId!=0){
            params.add("5",lastItemId+"");
        }
        GcgHttpClient.getInstance(this).get(url, params, new HttpResponseListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {
                //取消加载框,通知适配器更新数据
                search_mListView.onRefreshComplete();

            }

            @Override
            public void onSuccess(int statusCode, String content) {
                //根据类型进行相应的解析
                if (dyType.equals("2")) {
                    toParsePerson(content);
                } else if (dyType.equals("3")) {
                    toParseIdle(content);
                }
                if (data.size() < 1) {
                    Toast.makeText(SearchChooseActivity.this, "未查询到数据", Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Throwable error, String content) {
                Toast.makeText(SearchChooseActivity.this, "获取数据失败!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //解析个人数据
    private void toParsePerson(String content) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(content);
            if (!jsonObject.getString("resultcode").equals("0000")) {
                return;
            }
            if (jsonObject.has(DYNAMIC_DATA) && jsonObject.has(DYNAMIC_TITLE)) {
                JSONArray arrayData = jsonObject.getJSONArray(DYNAMIC_DATA);
                JSONArray arrayTitle = jsonObject.getJSONArray(DYNAMIC_TITLE);
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
                    data.add(assetsBean);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

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
            if (jsonObject.has(DYNAMIC_DATA) && jsonObject.has(DYNAMIC_TITLE)) {
                JSONArray arrayData = jsonObject.getJSONArray(DYNAMIC_DATA);
                JSONArray arrayTitle = jsonObject.getJSONArray(DYNAMIC_TITLE);
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
                    data.add(assetsBean);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_back:
                this.finish();
                break;

            case R.id.search_search_confir:
                  //返回选择的数据
                 returnSelectData();

                break;
        }
    }

    private void returnSelectData() {
        Intent intent = new Intent();
         //合并传过来的数据以及刚选择的数据,返回总数据
        List<AssetsBean> selectData = adapter.getSelectData();
//        if (comeData.size()>0){
//            for (int i = 0; i < selectData.size(); i++) {
//                AssetsBean as1 = selectData.get(i);
//                for (int k = 0; k < comeData.size(); k++) {
//                    AssetsBean as2 = comeData.get(k);
//                    if (as1.getId().equals(as2.getId())){
//                        comeData.remove(as2);
//                        k--;
//                    }
//                }
//            }
//            if (comeData.size()>0){
//
//                selectData.addAll(comeData);
//            }
//
//        }
        intent.putExtra("select_data", (Serializable) selectData);
        setResult(RESULT_OK,intent);
        this.finish();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


    }
}
