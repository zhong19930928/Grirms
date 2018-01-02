package com.yunhu.yhshxc.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.submitManager.bo.PendingRequestVO;
import com.yunhu.yhshxc.submitManager.bo.TablePending;
import com.yunhu.yhshxc.submitManager.core.SubmitWorkManager;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.MD5Helper;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static com.yunhu.yhshxc.utility.PublicUtils.receivePhoneNO;


/**
 * Created by suhu on 2017/5/10.
 */

public class ImageShowActivity extends AbsBaseActivity implements View.OnClickListener,PopupWindow.OnDismissListener{
    private static final String IMAGE_FILE_PATH = Constants.SDCARD_PATH;
    private static final long IMAGE_TIME = System.currentTimeMillis();
    private static final String IMAGE_FILE_NAME = IMAGE_TIME+".jpg";
    private static final String FACE_PATH = IMAGE_FILE_PATH+IMAGE_FILE_NAME;
    private static final String HEAD_PATH = IMAGE_FILE_PATH+"head.jpg";

    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;

    private RelativeLayout back;
    private TextView setting;
    private ImageView imageView;
    private PopupWindow photoPopWindow;
    private RelativeLayout pop_menu_background;
    private BitmapDrawable drawable;

    private MyProgressDialog progressDialog;
    private DisplayImageOptions options;
    protected ImageLoader imageLoader = ImageLoader.getInstance();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_show);

        initView();
        setListener();
        setData();
    }




    private void initView() {

        back = (RelativeLayout) findViewById(R.id.person_back);
        setting = (TextView) findViewById(R.id.person_setting);
        imageView = (ImageView) findViewById(R.id.show_image);

        pop_menu_background = (RelativeLayout) findViewById(R.id.send_background);

        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.wechat_moren_header)
                .showImageForEmptyUri(R.drawable.wechat_moren_header)
                .showImageOnFail(R.drawable.wechat_moren_header)
                .cacheInMemory(true)
                .build();
    }

    private void setListener() {
        back.setOnClickListener(this);
        setting.setOnClickListener(this);
    }
    private void setData() {
        showImage(imageView,HEAD_PATH);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.person_back:
                finish();
                break;
            case R.id.person_setting:
                shotSelectImages();
                break;
            case R.id.btn_paizhao:
                openCamera();
                break;
            case R.id.btn_select_pic:
                openPhoto();
                break;
        }
    }

    /**
     *@method 显示本地图片
     *@param imageView
     *@param file_path
     *
     */
    private void showImage(ImageView imageView, String file_path){
        Bitmap bm = BitmapFactory.decodeFile(file_path);
        if (bm!=null){
            imageView.setImageBitmap(bm);
        }else {
            imageLoader.displayImage(SharedPreferencesUtil.getInstance(this).getHeadImage(), imageView, options, null);
        }
    }


    /**
     *@method 弹出框
     *@author suhu
     *@time 2017/4/25 15:51
     *
     */
    public void shotSelectImages() {
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout myView = (LinearLayout) inflater.inflate(
                R.layout.select_images_from_local1, null);
        myView.findViewById(R.id.btn_paizhao).setOnClickListener(this);
        myView.findViewById(R.id.btn_select_pic).setOnClickListener(this);
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        photoPopWindow = new PopupWindow(myView, width - dip2px(40),
                RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        photoPopWindow.setOutsideTouchable(true);
        photoPopWindow.setBackgroundDrawable(new BitmapDrawable());
        photoPopWindow.showAtLocation(imageView, Gravity.CENTER, 0, 0);
        photoPopWindow.setOnDismissListener(this);
        pop_menu_background.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDismiss() {
        pop_menu_background.setVisibility(View.GONE);
    }

    /**
     *@method 打开相机
     *@author suhu
     *@time 2017/4/25 15:50
     *
     */
    private void openCamera(){
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (hasSdcard()) {
            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
        }
        startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
        pop_menu_background.setVisibility(View.GONE);
        photoPopWindow.dismiss();
    }
    /**
     *@method 打开相册
     *@author suhu
     *@time 2017/4/25 15:51
     *
     */
    private void openPhoto(){
        Intent intentFromGallery = new Intent(Intent.ACTION_PICK, null);
        intentFromGallery.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
        pop_menu_background.setVisibility(View.GONE);
        photoPopWindow.dismiss();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    startPhotoZoom(data.getData());
                    break;
                case CAMERA_REQUEST_CODE:
                    if (hasSdcard()) {
                        File tempFile = new File(Environment.getExternalStorageDirectory() + File.separator + IMAGE_FILE_NAME);
                        startPhotoZoom(Uri.fromFile(tempFile));
                    } else {
                        Toast.makeText(ImageShowActivity.this, "未找到存储卡，无法存储照片！", Toast.LENGTH_LONG).show();
                    }
                    break;
                case RESULT_REQUEST_CODE:
                    if (data != null) {
                        getImageToView(FACE_PATH);
                        upLoadImage();
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /********************************************工具方法**************************************************/

    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    public int dip2px(float dpValue) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     *@method 裁剪图片方法实现
     *@param uri
     *
     */
    public void startPhotoZoom(Uri uri){
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = display.getWidth();
        if (screenWidth>800){
            intent.putExtra("outputX", 800);
            intent.putExtra("outputY", 800);
        }else {
            intent.putExtra("outputX", 600);
            intent.putExtra("outputY", 600);
        }
        intent.putExtra("return-data", false);
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(FACE_PATH)));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, RESULT_REQUEST_CODE);
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
            flag = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.flush();
                    os.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
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
            imageView.setImageBitmap(bm);
            SaveBitmapAsFile(HEAD_PATH,bm);
        }
    }

    /**
     * 更新数据
     *
    */
    private void upLoadImage(){

        progressDialog = new MyProgressDialog(ImageShowActivity.this,R.style.CustomProgressDialog,
                getResources().getString(R.string.str_wechat_person_in));
        progressDialog.show();
        String url = UrlInfo.doWeChatUserInfo(this);
        JSONObject dObj = new JSONObject();
        File file = new File(IMAGE_FILE_NAME);
        String path = file.toString();
        String json = "";
        try {
            dObj.put("nickName","aa" );
            dObj.put("signature","111" );
            dObj.put("headImg",path);
            json = dObj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestParams params = new RequestParams();
        params.put("phoneno", receivePhoneNO(this));
        params.put("data", json);

        GcgHttpClient.getInstance(this).post(url, params, new HttpResponseListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onSuccess(int statusCode, String content) {
                String resultCode = null;
                try {
                    JSONObject obj = new JSONObject(content);
                    resultCode = obj.getString("resultcode");
                    if ("0000".equals(resultCode)) {
                        submitPhotoBackground();
                        SubmitWorkManager.getInstance(ImageShowActivity.this).commit();
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        String time = formatter.format(new Date(IMAGE_TIME));
                        String phone = PublicUtils.receivePhoneNO(ImageShowActivity.this.getApplicationContext());
                        String phone_1 = phone.substring(0,3);
                        String phone_2 = phone.substring(phone.length()-4,phone.length());

                        String oldUrl = SharedPreferencesUtil.getInstance(ImageShowActivity.this).getHeadImage();
                        String newUrl;
                        if (oldUrl!=null&&oldUrl.length()>0){
                            String str [] = oldUrl.split("upload/");
                            newUrl = str[0]+"upload/"+time+"/"+phone_1+phone_2+"@"+IMAGE_FILE_NAME;
                        }else {
                            newUrl = "http://image2.gcgcloud.com/9/upload/"+time+"/"+phone_1+phone_2+"@"+IMAGE_FILE_NAME;
                        }
                        SharedPreferencesUtil.getInstance(ImageShowActivity.this).setHeadImage(newUrl);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Toast.makeText(ImageShowActivity.this, "修改成功", Toast.LENGTH_LONG).show();
            }
            @Override
            public void onFailure(Throwable error, String content) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Toast.makeText(ImageShowActivity.this, "修改失败", Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     * 提交图片
     */
    private void submitPhotoBackground(){
        PendingRequestVO vo = new PendingRequestVO();
        vo.setContent("用户信息:");
        vo.setTitle("个人头像");
        vo.setMethodType(SubmitWorkManager.HTTP_METHOD_POST);
        vo.setType(TablePending.TYPE_IMAGE);
        vo.setUrl(UrlInfo.getUrlPhoto(ImageShowActivity.this));
        HashMap<String, String> params = new HashMap<>();
        params.put("name", IMAGE_FILE_NAME);
        params.put("companyid", String.valueOf(SharedPreferencesUtil.getInstance(ImageShowActivity.this).getCompanyId()));
        params.put("md5Code", MD5Helper.getMD5Checksum2(FACE_PATH));
        vo.setParams(params);
        vo.setImagePath(FACE_PATH);
        SubmitWorkManager.getInstance(ImageShowActivity.this).performImageUpload(vo);
    }

}
