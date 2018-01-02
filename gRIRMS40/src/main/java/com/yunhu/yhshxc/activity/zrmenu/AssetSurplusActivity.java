package com.yunhu.yhshxc.activity.zrmenu;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.addressBook.db.AdressBookUserDB;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.comp.spinner.AbsSelectComp;
import com.yunhu.yhshxc.core.ApiRequestFactory;
import com.yunhu.yhshxc.core.ApiRequestMethods;
import com.yunhu.yhshxc.core.SubmitFile;
import com.yunhu.yhshxc.core.ToastUtils;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * @author suhu
 * @data 2017/10/23.
 * @description 资产盘盈
 */
public class AssetSurplusActivity extends AppCompatActivity {

    @BindView(R.id.zrmodule_back)
    ImageView zrmoduleBack;
    @BindView(R.id.zrmodule_title)
    TextView zrmoduleTitle;
    @BindView(R.id.surplus_name)
    EditText surplusName;
    @BindView(R.id.surplus_code)
    EditText surplusCode;
    @BindView(R.id.surplus_measurement)
    EditText surplusMeasurement;
    @BindView(R.id.equipment)
    TextView equipment;
    @BindView(R.id.company)
    TextView company;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.area)
    TextView area;
    @BindView(R.id.surplus_address)
    EditText surplusAddress;
    @BindView(R.id.user_company)
    TextView userCompany;
    @BindView(R.id.user_department)
    TextView userDepartment;
    @BindView(R.id.user_name)
    EditText userName;
    @BindView(R.id.remark)
    EditText remark;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.submit)
    ImageView submit;

    private static final int CAMERA_REQUEST_CODE = 1;
    private static final String IMAGE_FILE_PATH = Constants.SDCARD_PATH;
    private static final long IMAGE_TIME = System.currentTimeMillis();
    private static final String IMAGE_FILE_NAME = IMAGE_TIME+".jpg";
    private static final String FACE_PATH = IMAGE_FILE_PATH+IMAGE_FILE_NAME;

    private List<String> equipmentList,companyList,areaList,userCompanyList,departmentList;

    private SharedPreferencesUtil share;
    private boolean isOk = false;
    private String pandid ="null";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_surplus);
        ButterKnife.bind(this);
        initData();
        initList();
        initView();
    }

    private void initData() {
        Intent intent  = getIntent();
        if (intent!=null){
           String codeS =  intent.getStringExtra("code");
           pandid = intent.getStringExtra("id");
           surplusCode.setText(codeS);
        }
    }

    private void initView() {
        share = SharedPreferencesUtil.getInstance(this);
    }

    private void initList() {
        equipmentList = new ArrayList<>();
        equipmentList.add("电子产品");
        equipmentList.add("化学物品");
        equipmentList.add("生物产品");

        Func func0 = new Func();
        func0.setDictCols("did,data_2044317,data_2045013,data_2045014,data_2045016,data_2045709,data_2045759");
        func0.setDictDataId("data_2045709");
        func0.setDictTable("t_m_2031751");
        func0.setOrgOption(0);


        companyList = queryData(func0);

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
        departmentList = queryData(func2);

    }

    @OnClick({R.id.zrmodule_back, R.id.equipment, R.id.company, R.id.area, R.id.user_company, R.id.user_department, R.id.image, R.id.submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.zrmodule_back:
                finish();
                break;
            case R.id.equipment:
                dialogShow(equipmentList,equipment);
                break;
            case R.id.company:
                dialogShow(companyList,company);
                break;
            case R.id.area:
                dialogShow(areaList,area);
                break;
            case R.id.user_company:
                dialogShow(userCompanyList,userCompany);
                break;
            case R.id.user_department:
                dialogShow(departmentList,userDepartment);
                break;
            case R.id.image:
                openCamera();
                break;
            case R.id.submit:
                submit();
                break;
            default:
        }
    }

    private void submit() {
        String areaName = area.getText().toString().trim();
        String code = surplusCode.getText().toString().trim();
        String sMeasurement = surplusMeasurement.getText().toString().trim();

        String equi = equipment.getText().toString().trim();
        if ("点子产品及通讯设备".equals(equi)){
            equi = "";
        }

        String companyS = company.getText().toString().trim();
        if ("可选择".equals(equi)){
            companyS = "";
        }

        String areaS = area.getText().toString().trim();
        if ("可选择".equals(equi)){
            areaS = "";
        }

        String addressS = surplusAddress.getText().toString().trim();

        String userCompanyS = userCompany.getText().toString().trim();
        if ("可选择".equals(equi)){
            userCompanyS = "";
        }

        String department = userDepartment.getText().toString().trim();
        if ("可选择".equals(equi)){
            department = "";
        }

        String user_name = userName.getText().toString().trim();

        String remarkS = remark.getText().toString().trim();


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
        map.put("pdid",pandid);
        map.put("zcid","");
        map.put("zcname",areaName);
        map.put("zccompany_id",companyS);
        map.put("quyuid",areaS);
        map.put("bumen_id",department);
        map.put("model","");
        map.put("zccat_id",equi);
        map.put("unit",department);
        map.put("syname",user_name);
        map.put("remarks",remarkS);
        map.put("place",addressS);
        map.put("state","2");
        List<SubmitFile> fileList = new ArrayList<>();
        if (isOk){
            SubmitFile file = new SubmitFile();
            file.setFilename(IMAGE_FILE_NAME);
            file.setName("thumb");
            file.setFile(new File(FACE_PATH));
            fileList.add(file);
        }
        ApiRequestMethods.checkInfoList(this, fileList, map, new ApiRequestFactory.HttpCallBackListener() {
            @Override
            public void onSuccess(String response, String url, int id) {
                JSONObject object = null;
                try {
                    object = new JSONObject(response).getJSONObject("data");
                    int flag = object.getInt("flag");
                    switch (flag) {
                        case -3:
                            ToastUtils.disPlayShort(AssetSurplusActivity.this, "参数为空");
                            return;
                        case -1:
                            ToastUtils.disPlayShort(AssetSurplusActivity.this, "后台没有资产目录");
                            return;
                        case -5:
                            ToastUtils.disPlayShort(AssetSurplusActivity.this, "添加失败");
                            return;
                        case 0:
                            ToastUtils.disPlayShort(AssetSurplusActivity.this, "提交成功");
                            break;
                        default:
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(Call call, Exception e, int id) {
                Toast.makeText(AssetSurplusActivity.this,"上传数据失败",Toast.LENGTH_SHORT).show();
            }
        }, true);

    }

    /**
     *@method 弹框
     *@author suhu
     *@time 2017/10/27 14:45
     *@param list
     *@param textView
     *
    */
    private void dialogShow(List<String> list,final TextView textView) {
        if (list==null&&list.size()==0) {
            Toast.makeText(this,"无数据",Toast.LENGTH_SHORT).show();
            return;
        }
        SelectDialog dialog = new SelectDialog(this,list);
        dialog.show();
        dialog.setOnSelectData(new SelectDialog.OnSelectData() {
            @Override
            public void selectData(String data, Dialog dialog) {
                textView.setText(data);
                dialog.dismiss();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case CAMERA_REQUEST_CODE:
                    if (hasSdcard()) {
                        File tempFile = new File(Environment.getExternalStorageDirectory() + File.separator + IMAGE_FILE_NAME);
                        getImageToView(tempFile.toString());
                    } else {
                        Toast.makeText(AssetSurplusActivity.this, "未找到存储卡，无法存储照片！", Toast.LENGTH_LONG).show();
                    }
                    break;
                default:
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }





    /**
     *@method 查询数据
     *@author suhu
     *@time 2017/10/30 16:38
     *@param func
     *
    */
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




    /**
     *@method 打开相机
     *@author suhu
     *@time 2017/10/27 15:50
     *
     */
    private void openCamera(){
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (hasSdcard()) {
            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
        }
        startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
    }


    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     *@method 保存图片到内存卡
     *@param filepath
     *@param bitmap
     *
     */
    public boolean SaveBitmapAsFile(String filepath, Bitmap bitmap) {
        boolean flag = false;
        String dir = filepath.substring(0, filepath.lastIndexOf("/"));
        Log.e("", filepath);
        Log.e("", dir);
        File mDownloadDir = new File(dir);
        if (!mDownloadDir.exists()) {
            mDownloadDir.mkdirs();
        }
        File newFile = new File(filepath);
        FileOutputStream os = null;
        if (newFile.exists()) {
            newFile.delete();
        }
        try {
            newFile.createNewFile();
            os = new FileOutputStream(newFile);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight());
            flag = bitmap.compress(Bitmap.CompressFormat.JPEG, 40, os);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.flush();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }

    /**
     *@method 保存裁剪之后的图片数据
     *@param srcFile
     *
     */
    private void getImageToView(String srcFile){
        Bitmap bm = BitmapFactory.decodeFile(srcFile.toString());
        if (bm!=null){
            image.setImageBitmap(bm);
            isOk = SaveBitmapAsFile(FACE_PATH,bm);
        }
    }


}
