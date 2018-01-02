package com.yunhu.yhshxc.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.database.SubmitItemDB;
import com.yunhu.yhshxc.database.SubmitItemTempDB;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.FileHelper;

/**
 * 图片预览Dialog模块
 * 此Activity以Dialog方式显示，主要提供预览和删除功能
 * 
 * @version 2013.5.23
 * @author wangchao
 *
 */
public class PhotoPreActivity extends Activity {
	/**
	 * 图片对应的SubmitItemId
	 */
	private int submitItemId;
	private int submitId;
	private Integer funcId;
	
	/**
	 * 图片文件名
	 */
	private String fileName;
	
	/**
	 * 是否是超链接
	 */
	private boolean isLink;
	
	/**
	 * 是否是从预览界面超链接跳转到下层超链接预览
	 */
	private boolean isFromLinkPriview;
	
	private int screenW,screenH;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.photo);
		DisplayMetrics  metric = new  DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		screenW = metric.widthPixels;
		screenH = metric.heightPixels;
		// 从Intent取出相关参数
		String title = this.getIntent().getStringExtra("title");
		fileName = this.getIntent().getStringExtra("fileName");
		funcId =   (Integer) this.getIntent().getSerializableExtra("funcId");
		submitItemId = this.getIntent().getIntExtra("submitItemId", 0);
		submitId = this.getIntent().getIntExtra("submitId", 0);
		isLink = this.getIntent().getBooleanExtra("isLink", false);
		isFromLinkPriview = this.getIntent().getBooleanExtra("isFromLinkPriview", false);
		
		TextView txtTitle = (TextView)findViewById(R.id.txt_title_bar_title);
		txtTitle.setText(title);

		String path = Constants.SDCARD_PATH + fileName;// 将文件名转换成路径
		ImageView photo = (ImageView)findViewById(R.id.photo);

//		Bitmap bitmap = BitmapFactory.decodeFile(path);// 根据路径获取图片
//		photo.setImageBitmap(resizeImage(bitmap));//压缩图片

		Bitmap bitmap = FileHelper.compressImagePriview(path, screenW, screenH);;// 根据路径获取图片
		if (bitmap!=null) {			
			photo.setImageBitmap(resizeImage(bitmap));//压缩图片
		}

		//删除按钮
		Button btnDelete = (Button)findViewById(R.id.btn_delete);
		btnDelete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				if (isLink && !isFromLinkPriview) {
//					new SubmitItemTempDB(getApplicationContext()).deleteSubmitItemById(submitItemId);
//				}
//				else {
////					new SubmitItemDB(getApplicationContext()).deleteSubmitItemById(submitItemId);
//					new SubmitItemDB(getApplicationContext()).deleteSubmitItemByParamName(funcId+"");
//					
//					new FileHelper().deleteFile(Constants.SDCARD_PATH + fileName);
//				}
				if (isLink ) {
					new SubmitItemTempDB(getApplicationContext()).deleteSubmitItemBySubmitIdAndParamName(submitId, funcId+"");
				}
				else {
//					new SubmitItemDB(getApplicationContext()).deleteSubmitItemById(submitItemId);
					new SubmitItemDB(getApplicationContext()).deleteSubmitItemByParamName(funcId+"");
					
//					new FileHelper().deleteFile(Constants.SDCARD_PATH + fileName);
				}
				 new FileHelper().deleteFile(Constants.SDCARD_PATH+fileName);
//				setResult(Activity.RESULT_OK);
				finish();
			}
		});
	}

	@Override
	public void onBackPressed() {
		setResult(Activity.RESULT_CANCELED);
		finish();
	}
	
	/**
     * 以最省内存的方式读取本地资源的图片
     * 
     * @param context
     * @param resId
     * @return
	 * @throws FileNotFoundException 
     */
    public Bitmap readBitMap(String path) throws FileNotFoundException {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        FileInputStream is = new FileInputStream(new File(path));
        return BitmapFactory.decodeStream(is, null, opt);
    }
    

	/**
	 * 按窗口尺寸缩放图片
	 * 
	 * @param bitmap 原图片
	 * @return 缩放后的图片
	 */
	private Bitmap resizeImage(Bitmap bitmap) {

		Bitmap tmp = bitmap;
		if (bitmap.getWidth() > bitmap.getHeight()) {
			Matrix matrix = new Matrix();
			matrix.setRotate(90, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
			tmp = Bitmap.createBitmap(tmp, 0, 0, tmp.getWidth(), tmp.getHeight(), matrix, true);
		}
		// 获取原图片的长宽值
		int width = tmp.getWidth();
		int height = tmp.getHeight();

		// 获取窗口的长宽值
		int newWidth = getWindowManager().getDefaultDisplay().getWidth();
		int newHeight = getWindowManager().getDefaultDisplay().getHeight();

		// 计算缩放比例
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		float s = scaleWidth;
		if (scaleWidth < scaleHeight) {
			s = scaleHeight;
		}
		// 创建一个用于缩放的Matrix对象
		Matrix matrix = new Matrix();
		// 设置缩放比例
		matrix.postScale(s, s);
		// 根据缩放比例生成新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(tmp, 0, 0, width, height, matrix, true);
		// BitmapOrg.recycle();
		// make a Drawable from Bitmap to allow to set the Bitmap
		// to the ImageView, ImageButton or what ever
		return resizedBitmap;
	}

}
