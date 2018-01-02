package com.yunhu.yhshxc.visitors;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.yunhu.android.pulltorefresh.library.PullToRefreshBase;
import com.yunhu.android.pulltorefresh.library.PullToRefreshListView;
import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.ModuleFuncActivity;
import com.yunhu.yhshxc.bo.Module;
import com.yunhu.yhshxc.database.ModuleDB;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.visitors.bo.Visitors;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VisitorListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private PullToRefreshListView lv_fk_contract;
    private int page = 1;
    private int taskId = 2032200;
    private List<Visitors> entitieList = new ArrayList<>();
    private VisitAdapter adapter;
    private ImageView tv_cancel;
    private TextView tv_bj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_list);
        initView();
    }

    private void initView() {
        lv_fk_contract= (PullToRefreshListView) findViewById(R.id.lv_fk_contract);
        tv_cancel = (ImageView) findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_bj= (TextView) findViewById(R.id.tv_bj);
        tv_bj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentReport();
            }
        });
        adapter = new VisitAdapter(this,entitieList);
        lv_fk_contract.setAdapter(adapter);
        lv_fk_contract.setOnItemClickListener(this);
        String label = DateUtils.formatDateTime(this.getApplicationContext(), System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
        lv_fk_contract.getLoadingLayoutProxy().setLastUpdatedLabel(label);
        lv_fk_contract.setMode(PullToRefreshBase.Mode.BOTH);
        lv_fk_contract.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //刷新
                page = 1;
                getNetworkData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //加载更多
                page++;
                getNetworkData();
            }
        });
        getNetworkData();

    }

    private RequestParams getRequestParams(){
        RequestParams params = new RequestParams();
        String phone = PublicUtils.receivePhoneNO(this);
        params.put("type",3);
        params.put("page",page);
        params.put("phoneno",phone);
        params.put("taskid",taskId);
        params.put("isDoubleMain","2");
        params.put("doubleBtnType",3);
        return params;
    }
    //  网络请求数据
    private void getNetworkData() {

        String reqUrl = PublicUtils.getBaseUrl(this) + "searchInfo.do?" ;

        GcgHttpClient.getInstance(this).get(reqUrl, getRequestParams(), new HttpResponseListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {
                lv_fk_contract.onRefreshComplete();
            }

            @Override
            public void onSuccess(int statusCode, String content) {
                if (!TextUtils.isEmpty(content)) {
                    try {
                        JSONObject jsonObject = new JSONObject(content);
                        if (jsonObject.has("resultcode")) {
                            if ("0000".equals(jsonObject.getString("resultcode"))) {
                                parseData(jsonObject);
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(VisitorListActivity.this, "解析数据异常", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(VisitorListActivity.this, "未查询到数据", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable error, String content) {
                Toast.makeText(VisitorListActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
            }
        });


    }
    /**
     * 解析数据
     */
    private void parseData(JSONObject jsonObject) throws Exception {
        if(page ==1)
            entitieList.clear();
        if (jsonObject.has("search")) {
            JSONObject searchObj = jsonObject.getJSONObject("search");
            if (searchObj.has("searchdata")) {
                JSONArray searchArray = searchObj.getJSONArray("searchdata");
                for (int i = 0; i < searchArray.length(); i++) {
                    JSONArray dataArray = searchArray.getJSONArray(i);
                    Visitors entity = new Visitors();
                    String taskid = dataArray.getString(0);
                    entity.setTaskid(taskid);
                    entity.setPatchid(dataArray.getString(1));
                    entity.setStatus(dataArray.getString(2));
                    entity.setStatus_name(dataArray.getString(3));
                    entity.setAuthuser(dataArray.getString(4));
                    entity.setLfdata(dataArray.getString(5));
                    entity.setLftime(dataArray.getString(6));
                    entity.setLfdw(dataArray.getString(7));
                    entity.setLfsy(dataArray.getString(8));
                    entity.setLfr(dataArray.getString(9));
                    entity.setOpenID(dataArray.getString(10));
                    entitieList.add(entity);
                }
            }
        }
        adapter.refresh(entitieList);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Visitors visitors = entitieList.get(position-1);
        Intent intent = new Intent(VisitorListActivity.this,VisitorDetailActivity.class);
        intent.putExtra("date",visitors.getLfdata());
        intent.putExtra("time",visitors.getLftime());
        intent.putExtra("dw",visitors.getLfdw());
        intent.putExtra("sy",visitors.getLfsy());
        intent.putExtra("person",visitors.getLfr());
        startActivity(intent);
    }

    class VisitAdapter extends BaseAdapter{
        private Context context;
        private List<Visitors> list ;
        private LayoutInflater mLayoutInflater;
        public VisitAdapter(Context context,List<Visitors> list){
            mLayoutInflater = LayoutInflater.from(context);
            this.context  = context;
            this.list = list;

        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.item_list_message, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.tv_visitor_name = (TextView) convertView.findViewById(R.id.tv_visitor_name);
                viewHolder.tv_visitor_time = (TextView) convertView.findViewById(R.id.tv_visitor_time);
                viewHolder.tv_visitor_number = (TextView) convertView.findViewById(R.id.tv_visitor_number);
                viewHolder.tv_visitor_bumen = (TextView) convertView.findViewById(R.id.tv_visitor_bumen);
                viewHolder.tv_visitor_status = (TextView) convertView.findViewById(R.id.tv_visitor_status);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Visitors entity = list.get(position);
            String value = entity.getLfr();
            String name = "";
            String phone = "";
            if(!TextUtils.isEmpty(value)){
                String[] str = value.split(";");
                if(str.length>0){
                    String[] ns = str[0].split(":");
                    if(ns.length>1){
                        name = ns[0];
                        phone = ns[1];
                    }
                }

            }
            viewHolder.tv_visitor_name.setText(name);
            viewHolder.tv_visitor_number.setText(phone);
            viewHolder.tv_visitor_time.setText(entity.getLfdata()+"  "+entity.getLftime());
            viewHolder.tv_visitor_bumen.setText(entity.getLfdw());
            viewHolder.tv_visitor_status.setText(entity.getStatus_name());
            return convertView;
        }
        private class ViewHolder{
            public TextView tv_visitor_name;
            public TextView tv_visitor_time;
            public TextView tv_visitor_number;
            public TextView tv_visitor_bumen;
            public TextView tv_visitor_status;
        }

        public void refresh(List<Visitors> list){
            this.list = list;
            notifyDataSetChanged();
        }
    }

    /**
     * 上报
     *
     */
    private void intentReport() {
        Module module = new ModuleDB(this).findModuleByTargetId(2032200,1);
        Bundle bundle = new Bundle();
        bundle.putInt("planId", Constants.DEFAULTINT);// 计划ID
        bundle.putInt("awokeType", Constants.DEFAULTINT);// 提醒类型
        bundle.putInt("wayId", Constants.DEFAULTINT);// 线路ID
        bundle.putInt("storeId", Constants.DEFAULTINT);// 店面ID
        bundle.putInt("targetId", 2032200);// 数据ID
        bundle.putInt("taskId", 2032200);// 超链接sql查询的时候使用
        bundle.putString("storeName", null);// 店面名称
        bundle.putString("wayName", null);// 线路名称
        bundle.putInt("isCheckin", Constants.DEFAULTINT);// 是否要进店定位1 是 0和null否
        bundle.putInt("isCheckout", Constants.DEFAULTINT);// 是否要离店定位1 是 0和null否
        bundle.putInt("menuType", 3);// 菜单类型
        bundle.putSerializable("module", module);// 自定义模块实例
        bundle.putInt("is_store_expand", 0);
        Intent intent = new Intent(this, ModuleFuncActivity.class);// 跳转到上报页面
        intent.putExtra("isNoWait", true);
        bundle.putInt("menuId", 2032200);
        bundle.putString("menuName", "访客预约");
        intent.putExtra("bundle", bundle);
        startActivity(intent);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
