package com.yunhu.yhshxc.activity.zrmenu;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.loopj.android.http.RequestParams;
import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.AssetsBean;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.UrlInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * ＠author zhonghuibin
 * create at 2017/10/25.
 * describe
 */
public class ZRMyBillsDetailsActivity extends Activity {
    private String moduleId;
    private String dyType;
    private String dyType_detail;
    private AssetsBean assetsBean;
    private TextView tv_11,tv_21,tv_31,tv_41,tv_51,tv_61,tv_71,tv_81,tv_91,tv_101,tv_121,tv_131;
    private ImageView iv_121;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null){
            dyType = String.valueOf(getIntent().getIntExtra("dytpye",0));
            dyType_detail = String.valueOf(getIntent().getIntExtra("ids",0));
        }
        setContentView(R.layout.activity_mybills_details);
        initView();
        loadData();
//        switch (getIntent().getIntExtra("tpye",0)){
//            case 1:
//                setContentView(R.layout.activity_mybills_details);
//                break;
//            case 2:
//                setContentView(R.layout.activity_mybills_details);
//                break;
//            case 3:
//                setContentView(R.layout.activity_mybills_details);
//                break;
//            case 4:
//                setContentView(R.layout.activity_mybills_details);
//                break;
//            case 5:
//                setContentView(R.layout.activity_mybills_details);
//                break;
//        }

    }

    private void initView() {
        tv_11 = (TextView) findViewById(R.id.tv_11);
        tv_21 = (TextView) findViewById(R.id.tv_21);
        tv_31 = (TextView) findViewById(R.id.tv_31);
        tv_41 = (TextView) findViewById(R.id.tv_41);
        tv_51 = (TextView) findViewById(R.id.tv_51);
        tv_61 = (TextView) findViewById(R.id.tv_61);
        tv_71 = (TextView) findViewById(R.id.tv_71);
        tv_81 = (TextView) findViewById(R.id.tv_81);
        tv_91 = (TextView) findViewById(R.id.tv_91);
        tv_101 = (TextView) findViewById(R.id.tv_101);
        tv_121 = (TextView) findViewById(R.id.tv_121);
        tv_131 = (TextView) findViewById(R.id.tv_131);
        iv_121 = (ImageView) findViewById(R.id.iv_121);
    }


    //根据传递来的参数类型进行数据的获取展示
    private void loadData() {
        moduleId = "top";
        dyType = "5";
        dyType_detail = "61";
        String url = UrlInfo.baseAssetsUrl(this);
        String phoneno = PublicUtils.receivePhoneNO(this);
        RequestParams params = new RequestParams();
        params.add("phoneno", phoneno);
        params.add("test", "gcg");
        params.add("moduleId", moduleId);
        params.add("dyType", dyType);
        params.add("5", dyType_detail);

        GcgHttpClient.getInstance(this).get(url, params, new HttpResponseListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {
                //取消加载框,通知适配器更新数据

            }

            @Override
            public void onSuccess(int statusCode, String content) {
                Log.d("views", "content: " + content);
                //根据类型进行相应的解析
                toParseIdle4(content);
                if (assetsBean != null){
                    setdate(assetsBean);
                }
            }
            @Override
            public void onFailure(Throwable error, String content) {
                Toast.makeText(ZRMyBillsDetailsActivity.this, "获取数据失败!", Toast.LENGTH_SHORT).show();
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
                    assetsBean = new AssetsBean();
                    JSONArray jsonArray = arrayData.getJSONArray(i);
                    assetsBean.setId(jsonArray.getString(0));
                    assetsBean.setIdTip(arrayTitle.getString(0));
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
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void setdate(AssetsBean assetsBean){
        tv_11.setText(assetsBean.getId());
        tv_21.setText(assetsBean.getState());
        tv_31.setText(assetsBean.getUrl());
        tv_41.setText(assetsBean.getTitle());
        tv_51.setText(assetsBean.getCode());
        tv_61.setText(assetsBean.getSn());
        tv_71.setText(assetsBean.getArea());
        tv_81.setText(assetsBean.getPutAddress());
        tv_91.setText(assetsBean.getUseCompany());
        tv_101.setText(assetsBean.getProKind());
        tv_121.setText(assetsBean.getKind());
        tv_131.setText(assetsBean.getFineKind());
        Glide.with(ZRMyBillsDetailsActivity.this).load(assetsBean.getUsePart()).into(iv_121);
    }
}
