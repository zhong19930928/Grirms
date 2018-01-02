package com.yunhu.yhshxc.activity.zrmenu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.yunhu.android.pulltorefresh.library.PullToRefreshBase;
import com.yunhu.android.pulltorefresh.library.PullToRefreshListView;
import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.AssetsBean;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.UrlInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * ＠author zhonghuibin
 * create at 2017/10/24.
 * describe
 */
public class ZRMyBillsActivity extends Activity implements View.OnClickListener{
    private ImageView mybills_back;
    private PullToRefreshListView listview_mybills;
    private List<String> list = new ArrayList<>();
    private String moduleId;
    private String dyType;
    private List<AssetsBean> dataMine = new ArrayList<>();
    private ZRMyBillsAdapter armbillsadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybills);
        initView();
    }

    private void initView() {
        mybills_back = (ImageView) findViewById(R.id.mybills_back);
        mybills_back.setOnClickListener(this);
        listview_mybills = (PullToRefreshListView) findViewById(R.id.listview_mybills);
        listview_mybills.setMode(PullToRefreshBase.Mode.BOTH);
        String label = DateUtils.formatDateTime(this.getApplicationContext(), System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
        listview_mybills.getLoadingLayoutProxy().setLastUpdatedLabel(label);
        listview_mybills.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData();
            }
        });

        listview_mybills.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent_details = new Intent(ZRMyBillsActivity.this,ZRMyBillsDetailsActivity_new.class);
                intent_details.putExtra("dytpye",dataMine.get(i-1).getUrl());
                intent_details.putExtra("ids",dataMine.get(i-1).getId());
                startActivity(intent_details);
            }
        });
        loadData();
        armbillsadapter = new ZRMyBillsAdapter(ZRMyBillsActivity.this,dataMine);
        listview_mybills.setAdapter(armbillsadapter);
    }


    //根据传递来的参数类型进行数据的获取展示
    private void loadData() {
        dataMine.clear();
        moduleId = "top";
        dyType = "4";
        String url = UrlInfo.baseAssetsUrl(this);
        String phoneno = PublicUtils.receivePhoneNO(this);
        RequestParams params = new RequestParams();
        params.add("phoneno", phoneno);
        params.add("test", "gcg");
        params.add("moduleId", moduleId);
        params.add("dyType", dyType);

        GcgHttpClient.getInstance(this).get(url, params, new HttpResponseListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {
                //取消加载框,通知适配器更新数据
                listview_mybills.onRefreshComplete();
            }

            @Override
            public void onSuccess(int statusCode, String content) {
                Log.d("views", "content: " + content);
                //根据类型进行相应的解析
                toParseIdle4(content);
                if (dataMine.size() < 1) {
                    Toast.makeText(ZRMyBillsActivity.this, "未查询到数据", Toast.LENGTH_SHORT).show();
                }else{
                    armbillsadapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Throwable error, String content) {
                Toast.makeText(ZRMyBillsActivity.this, "获取数据失败!", Toast.LENGTH_SHORT).show();
            }
        });
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
                    assetsBean.setUrl(String.valueOf(jsonArray.getInt(1)));
                    assetsBean.setTitle(jsonArray.getString(2));
                    assetsBean.setNum(jsonArray.getString(3));
                    assetsBean.setCode(jsonArray.getString(4));
                    assetsBean.setSn(jsonArray.getString(5));
                    assetsBean.setIdTip(arrayTitle.getString(0));
                    assetsBean.setUrlTip(arrayTitle.getString(1));
                    assetsBean.setTitleTip(arrayTitle.getString(2));
                    assetsBean.setNumTip(arrayTitle.getString(3));
                    assetsBean.setCodeTip(arrayTitle.getString(4));
                    assetsBean.setSnTip(arrayTitle.getString(5));
                    dataMine.add(assetsBean);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mybills_back:
                this.finish();
                break;
        }
    }

}
