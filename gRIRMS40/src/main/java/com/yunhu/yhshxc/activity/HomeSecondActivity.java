package com.yunhu.yhshxc.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yunhu.android.pulltorefresh.library.PullToRefreshBase;
import com.yunhu.android.pulltorefresh.library.PullToRefreshListView;
import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.adapter.HomeSecondAdapter;
import com.yunhu.yhshxc.bo.HomeBean;
import com.yunhu.yhshxc.core.ApiRequestFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * ＠author zhonghuibin
 * create at 2017/12/13.
 * describe 首页点击GridView进入的二级页面
 */
public class HomeSecondActivity extends Activity {
    private PullToRefreshListView lv_fk_contract;
    private ImageView tv_cancel;
    private int page = 0;
    private int erjicatid = 0;
    private List<HomeBean.DataBeanX.DataBean.ListBean> list = new ArrayList<>();
    private HomeSecondAdapter adapter;
    private int position;
    private int catid;
    private TextView tv_title_address,tv_top_right;
    private LinearLayout ll_secondpage,ll_secondpage_line;
    private TextView tv1_type,tv2_type,tv3_type,tv4_type,tv5_type,tv6_type;
    private TextView tv1_type_line,tv2_type_line,tv3_type_line,tv4_type_line,tv5_type_line,tv6_type_line;
    private List<HomeBean.DataBeanX.DataBean.TabBean> tab;
    private int pagetype = 1,a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homesecond);
        catid = getIntent().getIntExtra("catid",1);
        initView();
    }

    private void initView() {
        tv_title_address = (TextView) findViewById(R.id.tv_title_address);
        switch (catid){
            case 0:
                tv_title_address.setText("今日美食");
                break;
            case 1:
                tv_title_address.setText("热点资讯");
                break;
            case 2:
                tv_title_address.setText("健身课程");
                break;
            case 3:
                tv_title_address.setText("康体讲座");
                break;
            case 4:
                tv_title_address.setText("自助缴费");
                break;
            case 5:
                tv_title_address.setText("发票抬头");
                break;
            case 6:
                tv_title_address.setText("活动号集");
                break;
            case 7:
                tv_title_address.setText("拼车信息");
                break;
            case 8:
                tv_title_address.setText("用印登记");
                break;
            case 9:
                tv_title_address.setText("证照登记");
                break;
            case 10:
                tv_title_address.setText("中融党建");
                break;
            case 11:
                tv_title_address.setText("调查问卷");
                break;
            case 12:
                tv_title_address.setText("公司活动");
                break;
            case 13:
                tv_title_address.setText("我的考勤");
                break;
            case 14:
                tv_title_address.setText("中融公益");
                break;
            case 15:
                tv_title_address.setText("培训学院");
                break;
            default:
                break;
        }
        lv_fk_contract= (PullToRefreshListView) findViewById(R.id.lv_fk_contract);
        tv_cancel = (ImageView) findViewById(R.id.tv_cancel);
        ll_secondpage = (LinearLayout) findViewById(R.id.ll_secondpage);
        tv_top_right = (TextView) findViewById(R.id.tv_top_right);
        ll_secondpage_line = (LinearLayout) findViewById(R.id.ll_secondpage_line);
        tv1_type = (TextView) findViewById(R.id.tv1_type);
        tv2_type = (TextView) findViewById(R.id.tv2_type);
        tv3_type = (TextView) findViewById(R.id.tv3_type);
        tv4_type = (TextView) findViewById(R.id.tv4_type);
        tv5_type = (TextView) findViewById(R.id.tv5_type);
        tv6_type = (TextView) findViewById(R.id.tv6_type);
        tv1_type_line = (TextView) findViewById(R.id.tv1_type_line);
        tv2_type_line = (TextView) findViewById(R.id.tv2_type_line);
        tv3_type_line = (TextView) findViewById(R.id.tv3_type_line);
        tv4_type_line = (TextView) findViewById(R.id.tv4_type_line);
        tv5_type_line = (TextView) findViewById(R.id.tv5_type_line);
        tv6_type_line = (TextView) findViewById(R.id.tv6_type_line);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String label = DateUtils.formatDateTime(this.getApplicationContext(), System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
        lv_fk_contract.getLoadingLayoutProxy().setLastUpdatedLabel(label);
        lv_fk_contract.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);//只能下拉不能上拉
        lv_fk_contract.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //加载更多
                position = lv_fk_contract.getRefreshableView().getLastVisiblePosition();
                page++;
                pagetype = 2;
                getNetworkData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //刷新
            }
        });
        lv_fk_contract.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(HomeSecondActivity.this,HomepageWebViewActivity.class);
                intent.putExtra("url",list.get(i-1).getJumpurl());
                startActivity(intent);
            }
        });
        getNetworkData();
    }

    /**
     *@date2017/10/30
     *@author zhonghuibin
     *@description 获取列表
     */
    private void getNetworkData() {
        getDetails(HomeSecondActivity.this,"m=Androidnews&a=getnewslist",catid,erjicatid,page,new ApiRequestFactory.HttpCallBackListener() {
                    @Override
                    public void onSuccess(String response, String url, int id) {
                        try{
                            HomeBean homeBean = new Gson().fromJson(response,HomeBean.class);
                            List<HomeBean.DataBeanX.DataBean.ListBean> homeList;
                            a = homeBean.getData().getData().getList().size();
                            if (list.size() == 0){
                                for (int i=homeBean.getData().getData().getList().size();i>0;i--){
                                    list.add(homeBean.getData().getData().getList().get(i-1));
                                }
                            }else{
                                for (int i=homeBean.getData().getData().getList().size();i>0;i--){
                                    list.add(0,homeBean.getData().getData().getList().get(i-1));
                                }
                            }
                            tab = homeBean.getData().getData().getTab();
                            setTextView(tab);
                            if (homeBean.getData().getFlag()==0){
                                if (adapter != null && pagetype !=1){
                                    adapter.refresh(list);
                                    lv_fk_contract.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            lv_fk_contract.onRefreshComplete();
                                            if (a == 0){
                                                lv_fk_contract.getRefreshableView().setSelection(1);
                                            }else{
                                                lv_fk_contract.getRefreshableView().setSelection(a+1);
                                            }
                                        }
                                    }, 0);
                                }else{
                                    adapter = new HomeSecondAdapter(HomeSecondActivity.this,list);
                                    lv_fk_contract.setAdapter(adapter);
                                    lv_fk_contract.getRefreshableView().setSelection(adapter.getCount());
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void failure(Call call, Exception e, int id) {
                    }
                },true);
    }

    private void setTextView(List<HomeBean.DataBeanX.DataBean.TabBean> tab) {
        if (tab != null && tab.size()>1){
            switch (tab.size()){
                case 2:
                    tv1_type_line.setVisibility(View.VISIBLE);
                    tv2_type_line.setVisibility(View.VISIBLE);
                    tv3_type_line.setVisibility(View.GONE);
                    tv4_type_line.setVisibility(View.GONE);
                    tv5_type_line.setVisibility(View.GONE);
                    tv6_type_line.setVisibility(View.GONE);
                    break;
                case 3:
                    tv1_type_line.setVisibility(View.VISIBLE);
                    tv2_type_line.setVisibility(View.VISIBLE);
                    tv3_type_line.setVisibility(View.VISIBLE);
                    tv4_type_line.setVisibility(View.GONE);
                    tv5_type_line.setVisibility(View.GONE);
                    tv6_type_line.setVisibility(View.GONE);
                    break;
                case 4:
                    tv1_type_line.setVisibility(View.VISIBLE);
                    tv2_type_line.setVisibility(View.VISIBLE);
                    tv3_type_line.setVisibility(View.VISIBLE);
                    tv4_type_line.setVisibility(View.VISIBLE);
                    tv5_type_line.setVisibility(View.GONE);
                    tv6_type_line.setVisibility(View.GONE);
                    break;
                case 5:
                    tv1_type_line.setVisibility(View.VISIBLE);
                    tv2_type_line.setVisibility(View.VISIBLE);
                    tv3_type_line.setVisibility(View.VISIBLE);
                    tv4_type_line.setVisibility(View.VISIBLE);
                    tv5_type_line.setVisibility(View.VISIBLE);
                    tv6_type_line.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
            for (int i=0;i<tab.size();i++){
                String service = tab.get(i).getCat_name();
                String id = tab.get(i).getId();
                switch (i){
                    case 0:
                        setText(tv1_type,service,id);
                        break;
                    case 1:
                        setText(tv2_type,service,id);
                        break;
                    case 2:
                        setText(tv3_type,service,id);
                        break;
                    case 3:
                        setText(tv4_type,service,id);
                        break;
                    case 4:
                        setText(tv5_type,service,id);
                        break;
                    case 5:
                        setText(tv6_type,service,id);
                        break;
                    default:
                        break;
                }
            }
        }else {
            ll_secondpage.setVisibility(View.GONE);
            ll_secondpage_line.setVisibility(View.GONE);
        }
    }

    private void serType(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clasecolor();
                page = 0;
                pagetype = 1;
                if (list != null){
                    list.clear();
                }
                switch (view.getId()) {
                    case R.id.tv1_type:
                        erjicatid = Integer.parseInt(tab.get(0).getId());
                        tv1_type_line.setBackgroundResource(R.color.black);
                        getNetworkData();
                        break;
                    case R.id.tv2_type:
                        erjicatid = Integer.parseInt(tab.get(1).getId());
                        tv2_type_line.setBackgroundResource(R.color.black);
                        getNetworkData();
                        break;
                    case R.id.tv3_type:
                        erjicatid = Integer.parseInt(tab.get(2).getId());
                        tv3_type_line.setBackgroundResource(R.color.black);
                        getNetworkData();
                        break;
                    case R.id.tv4_type:
                        erjicatid = Integer.parseInt(tab.get(3).getId());
                        tv4_type_line.setBackgroundResource(R.color.black);
                        getNetworkData();
                        break;
                    case R.id.tv5_type:
                        erjicatid = Integer.parseInt(tab.get(4).getId());
                        tv5_type_line.setBackgroundResource(R.color.black);
                        getNetworkData();
                        break;
                    case R.id.tv6_type:
                        erjicatid = Integer.parseInt(tab.get(5).getId());
                        tv6_type_line.setBackgroundResource(R.color.black);
                        getNetworkData();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void clasecolor() {
        tv1_type_line.setBackgroundResource(R.color.white);
        tv2_type_line.setBackgroundResource(R.color.white);
        tv3_type_line.setBackgroundResource(R.color.white);
        tv4_type_line.setBackgroundResource(R.color.white);
        tv5_type_line.setBackgroundResource(R.color.white);
        tv6_type_line.setBackgroundResource(R.color.white);
    }

    private void setText(TextView textview,String string,String id){
        textview.setText(string);
        textview.setVisibility(View.VISIBLE);
        serType(textview);
    }

    public static void getDetails(Context context, String url, int catid, int erjicatid, int page,
                                  final ApiRequestFactory.HttpCallBackListener httpCallBackListener,
                                   boolean isShowDialog){
        Map<String ,String> map = new HashMap<>();
        map.put("catid",catid+"");
        map.put("erjicatid",erjicatid+"");
        map.put("page",page+"");
        ApiRequestFactory.postJson2(context,url,map,httpCallBackListener,isShowDialog);
    }
}