package com.yunhu.yhshxc.activity.zrmenu;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.yunhu.android.pulltorefresh.library.PullToRefreshBase;
import com.yunhu.android.pulltorefresh.library.PullToRefreshListView;
import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.HomeMenuFragment;
import com.yunhu.yhshxc.application.SoftApplication;
import com.yunhu.yhshxc.bo.AssetsBean;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.bo.ShiHuaMenu;
import com.yunhu.yhshxc.database.MainMenuDB;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.inspection.InspectionMenuActivity;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.UrlInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ZRModuleActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView zrmodule_back;//返回键
    private TextView zrmodule_title;//标题
    private LinearLayout zrmodule_menu;//菜单按钮
    private PullToRefreshListView zemodule_listview_mine;//我的资产数据
    private PullToRefreshListView zemodule_listview_other;//其他资产数据
    private int lastItemIdMine = 0; //我的资产最后一条数据id
    private int lastItemIdOther = 0;//其他资产最后一条数据id

    MainMenuDB db ;
    private LinearLayout guli_zichan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zrmodule);
        initData();
        initView();
    }

    private void initView() {
        db = new MainMenuDB(this);
        guli_zichan = (LinearLayout) findViewById(R.id.guli_zichan);
        zrmodule_back = (ImageView) findViewById(R.id.zrmodule_back);
        zrmodule_title = (TextView) findViewById(R.id.zrmodule_title);
        zrmodule_menu = (LinearLayout) findViewById(R.id.zrmodule_menu);
        zemodule_listview_mine = (PullToRefreshListView) findViewById(R.id.zemodule_listview_mine);
        zemodule_listview_other = (PullToRefreshListView) findViewById(R.id.zemodule_listview_other);
        zrmodule_back.setOnClickListener(this);
        zrmodule_menu.setOnClickListener(this);
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
                    ZRDialog dialog = new ZRDialog(ZRModuleActivity.this);
                dialog.show();
                dialog.initData(bean);
            }
        });
        zemodule_listview_mine.setAdapter(adapterMine);
    //其他资产
        zemodule_listview_other.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        zemodule_listview_other.getLoadingLayoutProxy().setLastUpdatedLabel(label);
        zemodule_listview_other.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                lastItemIdOther = 0;
                loadDataOther();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (dataOhter.size()>=1){
                    AssetsBean assetsBean = dataOhter.get(dataOhter.size() - 1);
                    lastItemIdOther = Integer.parseInt(assetsBean.getId());
                }

                loadDataOther();
            }
        });
        zemodule_listview_other.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AssetsBean bean = dataOhter.get(position-1);
                Intent intent = new Intent(ZRModuleActivity.this,ZRModuleDetailActivity.class);
                intent.putExtra("id",bean.getId()+"");
                intent.putExtra("title",bean.getTitle());
                startActivity(intent);
            }
        });
        zemodule_listview_other.setAdapter(adapterOther);
        //获取传递来的数据
        list = new ArrayList<>();
        if (getIntent() != null) {
            menu = (ShiHuaMenu) getIntent().getSerializableExtra("shiHuaMenu");
            if (menu != null) {
                List<Menu> listAll = menu.getMenuList();
                for (Menu menu : listAll){
                    Menu m = db.findMenuListByMenuId(Integer.parseInt(menu.getMenuIdList().get(0)));
                    if(m!=null){
                        menu.setName(m.getName());
                        list.add(menu);
                    }
                }
                Menu menu = new Menu();
                menu.setName("我的单据");
                ArrayList<String> list_mybills = new ArrayList<>();
                list_mybills.add("0");
                list_mybills.add("1");
                list_mybills.add("2");
                menu.setMenuIdList(list_mybills);
                list.add(menu);
                Menu menu1 = new Menu();
                menu1.setName("盘点");
                menu1.setMenuIdList(list_mybills);
                list.add(menu1);
//                zrmodule_title.setText(menu.getFolderName());getMenuIdList().get(0)
            }
        }
        //初始化数据
        loadData();
        loadDataOther();
    }


    private void initData() {
        dataMine = new ArrayList<>();
        dataOhter = new ArrayList<>();
        adapterMine = new ZRModuleAdapter(this, dataMine,1);
        adapterOther = new ZRModuleAdapter(this,dataOhter,2);
        adapterOther.setIsCanClickItem(true);
    }

    private String moduleId;
    private String dyType;
    private List<AssetsBean> dataMine;//查询出的data
    private List<AssetsBean> dataOhter;//查询出的data
    private ZRModuleAdapter adapterMine;//适配器,我的资产
    private ZRModuleAdapter adapterOther;//适配器,其他资产

    //根据传递来的参数类型进行数据的获取展示
    private void loadData() {
        if (lastItemIdMine==0){
            dataMine.clear();
        }
        moduleId = "top";
        dyType = "1";
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
                    Toast.makeText(ZRModuleActivity.this, "未查询到数据", Toast.LENGTH_SHORT).show();
                }
                adapterMine.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Throwable error, String content) {
                Toast.makeText(ZRModuleActivity.this, "获取数据失败!", Toast.LENGTH_SHORT).show();
            }
        });

    }
    //请求其他资产信息
    private void loadDataOther() {
        if (lastItemIdOther==0){
            dataOhter.clear();

        }
        moduleId = "top";
        dyType = "2";
        String url = UrlInfo.baseAssetsUrl(this);
        String phoneno = PublicUtils.receivePhoneNO(this);
        RequestParams params = new RequestParams();
        params.add("phoneno", phoneno);
        params.add("test", "gcg");
        params.add("moduleId", moduleId);
        params.add("dyType", dyType);
        if (lastItemIdOther!=0){
            params.add("5",lastItemIdOther+"");
        }

        GcgHttpClient.getInstance(this).get(url, params, new HttpResponseListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {
                //取消加载框,通知适配器更新数据
                zemodule_listview_other.onRefreshComplete();

            }

            @Override
            public void onSuccess(int statusCode, String content) {
                Log.d("views", "content: " + content);
                //根据类型进行相应的解析
                toParseIdle4(content);
                if (dataOhter.size() < 1) {
                        guli_zichan.setVisibility(View.GONE);
//                    Toast.makeText(ZRModuleActivity.this, "未查询到数据", Toast.LENGTH_SHORT).show();
                }else{
                    guli_zichan.setVisibility(View.VISIBLE);
                }
                adapterOther.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Throwable error, String content) {
                Toast.makeText(ZRModuleActivity.this, "获取数据失败!", Toast.LENGTH_SHORT).show();
            }
        });


    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zrmodule_back:
                this.finish();
                break;
            case R.id.zrmodule_menu:
                showDropMenu();
                break;
        }


    }

    private List<Menu> list;
     private ShiHuaMenu menu;

    private void showDropMenu() {

        ZRMduleMenuAdapter adapter = new ZRMduleMenuAdapter(this);
        if (list.size()<1){
            return;
        }

        adapter.setList(list);
        View view = LayoutInflater.from(this).inflate(R.layout.popmenu_list_menu, null);
        ListView menuListView = (ListView) view.findViewById(R.id.popwindow_listview);
        menuListView.setAdapter(adapter);
        int px = PublicUtils.convertDIP2PX(this, 130);
        int py = PublicUtils.convertDIP2PX(this, (list.size()-1)*57);
        final PopupWindow popupWindow = new PopupWindow(view, px, py+40);
//       final PopupWindow popupWindow = new PopupWindow(300,WindowManager.LayoutParams.WRAP_CONTENT);
//        popupWindow.setContentView(view);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.asset_showwindows_bg));
        backgroundAlpha(0.5f);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(zrmodule_title, 300,50);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });

        menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    if (popupWindow.isShowing()){
                        popupWindow.dismiss();
                    }
                    if (list.get(position).getMenuIdList().size() == 1) {
                        posit = position;
                        HomeMenuFragment fragment = SoftApplication.getInstance().getHomeMenuFragment();
                        Menu menu = null;
                        try {
                            menu = db.findMenuListByMenuId(Integer.parseInt(list.get(position).getMenuIdList().get(0)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (menu == null){
                            Toast.makeText(ZRModuleActivity.this,"InvalidMenu",Toast.LENGTH_LONG).show();
                            return;
                        }
//                        fragment.onItemclickMenu(menu, view, PublicUtils.getNetDate());
                        setOrder3Time(view);
                     } else if("我的单据".equals(list.get(position).getName())){
                        Intent intent_bills = new Intent(ZRModuleActivity.this, ZRMyBillsActivity.class);
                        startActivity(intent_bills);
                    } else if("盘点".equals(list.get(position).getName())){
                        startActivity(new Intent(ZRModuleActivity.this, AssetDiscoveryActivity.class));
                    }else {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("menuIdList", list.get(position));
                        Intent intent = new Intent(ZRModuleActivity.this, InspectionMenuActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
            }
        });
    }
    int posit ;
    private void setOrder3Time(final View view) {
        flagOrder3Time = true;
        timer = new Timer(true);
        if (task.cancel()) {
            timer.schedule(task, 3000, 100000000); // 延时3000ms后执行，1000ms执行一次
        }
        new Thread(new Runnable() {

            @Override
            public void run() {
                Date date = PublicUtils.getNetDate();
                Message msg = Message.obtain();
                MyData m = new MyData();
                m.setDa(date);
                m.setView(view);
                msg.obj = m;
//
                mHanlder.sendMessage(msg);
            }
        }).start();
    }

    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }


    class MyData{
        private Date da;
        private View view;
        public Date getDa() {
            return da;
        }

        public void setDa(Date da) {
            this.da = da;
        }

        public View getView() {
            return view;
        }

        public void setView(View view) {
            this.view = view;
        }
    }

    Handler mHanlder = new Handler() {
        public void handleMessage(Message msg) {
            if (flagOrder3Time) {
                flagOrder3Time = false;
                if (task != null) {
                    task.cancel();
                }
                MyData m = (MyData) msg.obj;
                if(m!=null){
                    HomeMenuFragment fragment = SoftApplication.getInstance().getHomeMenuFragment();
                    Menu menu = null;
                    try {
                        menu = db.findMenuListByMenuId(Integer.parseInt(list.get(posit).getMenuIdList().get(0)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (menu == null){
                        Toast.makeText(ZRModuleActivity.this,"InvalidMenu",Toast.LENGTH_LONG).show();
                        return;
                    }
                    fragment.onItemclickMenu(menu, m.getView(), m.getDa());
                }
            }
        }

        ;
    };
    private boolean flagOrder3Time = true;
    Timer timer;
    TimerTask task = new TimerTask() {
        public void run() {
            mHanlder.sendEmptyMessage(1);
        }
    };

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
                    dataOhter.add(assetsBean);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


}
