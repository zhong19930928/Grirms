package com.yunhu.yhshxc.activity.zrmenu;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.addressBook.db.AdressBookUserDB;
import com.yunhu.yhshxc.activity.zrmenu.module.AssetBean;
import com.yunhu.yhshxc.bo.AssetsBean;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.comp.spinner.AbsSelectComp;
import com.yunhu.yhshxc.core.ApiRequestFactory;
import com.yunhu.yhshxc.core.ApiRequestMethods;
import com.yunhu.yhshxc.core.SubmitFile;
import com.yunhu.yhshxc.core.ToastUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static com.yunhu.yhshxc.R.id.user_company;
import static com.yunhu.yhshxc.R.id.user_use;

/**
 * @author suhu
 * @data 2017/10/23.
 * @description 资产盘点修改界面
 */
public class AssetAlterActivity extends AppCompatActivity {

    @BindView(R.id.zrmodule_back)
    ImageView zrmoduleBack;
    @BindView(R.id.zrmodule_title)
    TextView zrmoduleTitle;
    @BindView(R.id.z_name)
    TextView zName;
    @BindView(R.id.item_state)
    TextView itemState;
    @BindView(R.id.z_size)
    TextView zSize;
    @BindView(R.id.alter_time)
    TextView alterTime;
    @BindView(R.id.barcode)
    TextView barcode;
    @BindView(R.id.classification)
    TextView classification;
    @BindView(R.id.company)
    TextView company;
    @BindView(R.id.use_company)
    TextView useCompany;
    @BindView(R.id.use_name)
    TextView useName;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.is_show)
    LinearLayout isShow;
    @BindView(R.id.show_text)
    TextView showText;
    @BindView(R.id.show_im)
    ImageView showIm;
    @BindView(R.id.on_click)
    LinearLayout onClick;
    @BindView(R.id.area)
    TextView area;
    @BindView(R.id.address)
    EditText address;
    @BindView(user_company)
    TextView userCompany;
    @BindView(R.id.technology)
    TextView technology;
    @BindView(user_use)
    TextView userUse;
    @BindView(R.id.submit)
    ImageView submit;

    private boolean show = true;
    private List<String> areaList;
    private List<String> userCompanyList;
    private List<String> technologyList;
    private List<String> userList;

    private AssetsBean bean;
    private SharedPreferencesUtil share;
    private String zichanid = "";
    private AssetBean.DataBean dataBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_alter);
        ButterKnife.bind(this);
        initData();
        initList();
        
    }



    private void initData() {
        Intent intent  = getIntent();
        if (intent!=null){
            Bundle bundle = intent.getExtras();
            zichanid = (String) bundle.get("id");
            bean = (AssetsBean) bundle.getSerializable("data");
            if (bean!=null){
                zName.setText(bean.getTitle());
                barcode.setText(bean.getCode());
                classification.setText(bean.getFineKind());
                company.setText("后台无配置属性");
                useCompany.setText(bean.getUseCompany());
                useName.setText("后台无配置属性");
                time.setText("后台无配置属性");
            }
            dataBean = (AssetBean.DataBean) bundle.getSerializable("bean");

        }
        share = SharedPreferencesUtil.getInstance(this);

    }

    private void initList() {
        Func func = new Func();
        func.setDictCols("did,data_2044332,data_2044333");
        func.setDictDataId("data_2044333");
        func.setDictTable("t_m_2031753");
        func.setOrgOption(0);
        areaList = queryData(func);

        Func func1 = new Func();
        func1.setDictCols("did,data_2044317,data_2045013,data_2045014,data_2045016,data_2045709,data_2045759");
        func1.setDictDataId("data_2045709");
        func1.setDictTable("t_m_2031751");
        func1.setOrgOption(0);
        userCompanyList = queryData(func1);

        Func func2 = new Func();
        func2.setDictCols("did,data_2045760,data_2045761,data_2045762,data_2045763,data_2045764,data_2045765");
        func2.setDictDataId("data_2045764");
        func2.setDictTable("t_m_2032178");
        func2.setOrgOption(0);
        technologyList = queryData(func2);

        Func func3 = new Func();
        func3.setOrgOption(2);
        userList = queryData(func3);



    }


    @OnClick({R.id.zrmodule_back, R.id.on_click, R.id.area, R.id.address, user_company, R.id.technology, user_use, R.id.submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.zrmodule_back:
                finish();
                break;
            case R.id.on_click:
                if (show){
                    isShow.setVisibility(View.VISIBLE);
                    showText.setText("收起");
                    showIm.setImageResource(R.drawable.ivb_close);
                    show = false;
                }else {
                    isShow.setVisibility(View.GONE);
                    showText.setText("规格型号，使用人");
                    showIm.setImageResource(R.drawable.ivb_open);
                    show = true;
                }
                break;
            case R.id.area:
                areaData();
                break;
            case R.id.address:
                break;
            case user_company:
                userCompany();
                break;
            case R.id.technology:
                technology();
                break;
            case user_use:
                userUse();
                break;
            case R.id.submit:
                submit();
                break;
            default:
        }
    }




    private void areaData() {
        SelectDialog dialog = new SelectDialog(this,areaList);
        dialog.show();
        dialog.setOnSelectData(new SelectDialog.OnSelectData() {
            @Override
            public void selectData(String data, Dialog dialog) {
                area.setText(data);
                dialog.dismiss();
            }
        });
    }

    private void userCompany() {
        SelectDialog dialog = new SelectDialog(this,userCompanyList);
        dialog.show();
        dialog.setOnSelectData(new SelectDialog.OnSelectData() {
            @Override
            public void selectData(String data, Dialog dialog) {
                userCompany.setText(data);
                dialog.dismiss();
            }
        });
    }

    private void technology() {
        SelectDialog dialog = new SelectDialog(this,technologyList);
        dialog.show();
        dialog.setOnSelectData(new SelectDialog.OnSelectData() {
            @Override
            public void selectData(String data, Dialog dialog) {
                technology.setText(data);
                dialog.dismiss();
            }
        });
    }

    private void userUse() {
        SelectDialog dialog = new SelectDialog(this,userList);
        dialog.show();
        dialog.setOnSelectData(new SelectDialog.OnSelectData() {
            @Override
            public void selectData(String data, Dialog dialog) {
                userUse.setText(data);
                dialog.dismiss();
            }
        });

    }

    private void submit() {
        String areaS = area.getText().toString();
        if ("可选择".equals(areaS)){
            areaS = "";
        }

        String addressS = address.getText().toString().trim();


        String userCompanyS = userCompany.getText().toString();
        if ("可选择".equals(userCompanyS)){
            userCompanyS = "";
        }

        String technologyS = technology.getText().toString();
        if ("可选择".equals(technologyS)){
            technologyS = "";
        }

        String userUses = userUse.getText().toString();
        if ("可选择".equals(userUses)){
            userUses = "";
        }

        SharedPreferencesUtil sh = SharedPreferencesUtil.getInstance(this);
        String org_cede = "";
        String data_org_new ="";
        String roleId = "";
        String uid = "";
        try {
            roleId = sh.getRoleId()+"";
            data_org_new =sh.getOrgId()+"";
            org_cede = new AdressBookUserDB(this).findUserById(sh.getUserId()).getOc();
            uid = new AdressBookUserDB(this).findUserById(sh.getUserId()).getuId()+"";
        }catch (Exception e){
        }


        Map<String,String> map = new HashMap<>();
        map.put("company_id",share.getCompanyId()+"");
        map.put("data_auth_user",sh.getUserId()+"");
        map.put("data_org",org_cede);
        map.put("data_org_new",data_org_new);
        map.put("data_role",roleId);
        map.put("uid",sh.getUserId()+"");
        map.put("uname",sh.getUserRoleName());
        map.put("pdid",dataBean.getId()+"");
        map.put("zcid",zichanid+"");
        map.put("zcname",bean.getTitle()+"");
        map.put("zccompany_id","");
        map.put("quyuid",areaS);
        map.put("bumen_id",technologyS);
        map.put("model","");
        map.put("zccat_id","");
        map.put("unit",userCompanyS);
        map.put("syname",userUses);
        map.put("remarks","");
        map.put("place",addressS);
        map.put("state","1");
        List<SubmitFile> fileList = new ArrayList<>();
        ApiRequestMethods.checkInfoList(this, fileList, map, new ApiRequestFactory.HttpCallBackListener() {
            @Override
            public void onSuccess(String response, String url, int id) {
                JSONObject object = null;
                try {
                    object = new JSONObject(response).getJSONObject("data");
                    int flag = object.getInt("flag");
                    switch (flag) {
                        case -3:
                            ToastUtils.disPlayShort(AssetAlterActivity.this, "参数为空");
                            return;
                        case -1:
                            ToastUtils.disPlayShort(AssetAlterActivity.this, "后台没有资产目录");
                            return;
                        case -5:
                            ToastUtils.disPlayShort(AssetAlterActivity.this, "添加失败");
                            return;
                        case 0:
                            ToastUtils.disPlayShort(AssetAlterActivity.this, "提交成功");
                            break;
                        default:
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(Call call, Exception e, int id) {
                Toast.makeText(AssetAlterActivity.this,"上传数据失败",Toast.LENGTH_SHORT).show();
            }
        }, true);


    }

    private List<String> queryData(Func func){
        List<String> list = new ArrayList<>();

        Bundle bundle = new Bundle();

        AbsSelectComp absSelectComp = new AbsSelectComp(this,func,null,bundle) {
            @Override
            public void notifyDataSetChanged() {
            }

            @Override
            public void setSelected(String did) {
            }
        };
        absSelectComp.getDataSrcList(null,null);
        String[] dataS = absSelectComp.getDateSrc();
        for (String data : dataS) {
            list.add(data);
        }
        return list;
    }


}
