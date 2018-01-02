package com.yunhu.yhshxc.activity.zrmenu;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
public class ZRMyBillsDetailsActivity_new extends Activity implements View.OnClickListener {
    private String moduleId;
    private String dyType;
    private String dyType_detail;
    private AssetsBean assetsBean;
    private LinearLayout ll_mybills_details;
    private LayoutInflater inflater;
    private TextView tv1,tv2;
    private ImageView iv1,iv2;
    private ImageView mybills_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null){
            dyType = String.valueOf(getIntent().getStringExtra("dytpye"));//7
            dyType_detail = String.valueOf(getIntent().getStringExtra("ids"));//5
        }
        setContentView(R.layout.activity_mybills_details_new);
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
        ll_mybills_details = (LinearLayout) findViewById(R.id.ll_mybills_details);
        mybills_back = (ImageView) findViewById(R.id.mybills_back);
        mybills_back.setOnClickListener(this);
    }


    //根据传递来的参数类型进行数据的获取展示
    private void loadData() {
        moduleId = "top";
//        dyType = "5";
//        dyType_detail = "61";
        String url = UrlInfo.baseAssetsUrl(this);
        String phoneno = PublicUtils.receivePhoneNO(this);
        RequestParams params = new RequestParams();
        params.add("phoneno", phoneno);
        params.add("test", "gcg");
        params.add("moduleId", moduleId);
        params.add("5", dyType_detail);
        params.add("dyType",dyType);

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
            }
            @Override
            public void onFailure(Throwable error, String content) {
                Toast.makeText(ZRMyBillsDetailsActivity_new.this, "获取数据失败!", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void toParseIdle4(String content){
        if (TextUtils.isEmpty(content)) {
            return;
        }
        inflater = LayoutInflater.from(ZRMyBillsDetailsActivity_new.this);
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
                    JSONArray jsonArray = arrayData.getJSONArray(0);
                    for (int j = 0; j < jsonArray.length(); j++){
                        View view  = inflater.inflate(R.layout.mybills_items_layout,null);
                        tv1 = (TextView) view.findViewById(R.id.tv_1);
                        tv2 = (TextView) view.findViewById(R.id.tv_2);
                        iv1 = (ImageView) view.findViewById(R.id.iv_1);
                        iv2 = (ImageView) view.findViewById(R.id.iv_2);
                        tv1.setText(jsonArray.getString(j));
                        tv2.setText(arrayTitle.getString(j));
                        if (arrayTitle.getString(j).equals("签字")){
                            iv2.setVisibility(View.VISIBLE);
                            tv1.setVisibility(View.GONE);
                            Glide.with(ZRMyBillsDetailsActivity_new.this).load(jsonArray.getString(j)).into(iv2);
                        }else{
                            iv2.setVisibility(View.GONE);
                            tv1.setVisibility(View.VISIBLE);
                        }
                        iv1.setImageResource(setIconForName(tv2.getText().toString()));
                        ll_mybills_details.addView(view);
                    }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /**
     * 根据funcName获取小图标背景图片
     * @return 图片的ID
     */
    private int setIconForName(String funcName) {
        if (funcName.equals("申请人")){
            return R.drawable.asset_shenqingren;
        }else if(funcName.equals("使用人")||funcName.equals("借用人")||funcName.equals("借用处理人")||funcName.equals("归还处理人")||funcName.equals("归还人")||funcName.equals("调出管理员")||funcName.equals("调出处理人")){
            return R.drawable.asset_shiyongren;
        }else if(funcName.equals("领用时间")||funcName.equals("退库时间")||funcName.equals("借用日期")||funcName.equals("预计归还时间")||funcName.equals("申请归还时间")||funcName.equals("调出日期")){
            return R.drawable.asset_shijian;
        }else if(funcName.equals("领用后使用公司")||funcName.equals("退库所属公司")){
            return R.drawable.asset_shiyonggongsi;
        }else if(funcName.equals("领用后使用部门")){
            return R.drawable.asset_shiyongbumen;
        }else if(funcName.equals("领用后管理人")){
            return R.drawable.asset_shiyongren;
        }else if(funcName.equals("领用后使用区域")||funcName.equals("退库后区域")){
            return R.drawable.asset_shiyongquyu;
        }else if(funcName.equals("领用后存放地点")){
            return R.drawable.asset_shiyongdidian;
        }else if(funcName.equals("领用处理人")||funcName.equals("退库处理人")){
            return R.drawable.asset_shiyongren;
        }else if(funcName.equals("备注")){
            return R.drawable.asset_beizhu;
        }else if(funcName.equals("资产条码")||funcName.equals("选择调拨资产条码")){
            return R.drawable.icon_scan;
        }else if(funcName.equals("预计退库时间")){
            return R.drawable.asset_shijian;
        }else if(funcName.equals("调拨说明")){
            return R.drawable.f_default;
        }else{
            return R.drawable.icon_scan;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.mybills_back:
                finish();
                break;
        }


    }
}
