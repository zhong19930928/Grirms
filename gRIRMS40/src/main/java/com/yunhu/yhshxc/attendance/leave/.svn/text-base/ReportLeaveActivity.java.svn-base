package com.yunhu.yhshxc.attendance.leave;

import gcg.org.debug.JLog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.attendance.leave.ImageViewUtils.FileType;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.dragimage.PhotoPriviewActivity;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.submitManager.bo.PendingRequestVO;
import com.yunhu.yhshxc.submitManager.bo.TablePending;
import com.yunhu.yhshxc.submitManager.core.SubmitWorkManager;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.FileHelper;
import com.yunhu.yhshxc.utility.MD5Helper;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.yunhu.yhshxc.widget.wheeltime.TimeView;
import com.loopj.android.http.RequestParams;

/**
 * 请假上报
 * @author xuelinlin
 *
 */
public class ReportLeaveActivity extends AbsBaseActivity {
	private LinearLayout ll_order3_layout;
	private TextView tv_attend_leave;
	
	private LinearLayout ll_btn_layout;
	private TextView tv_yulan;
	private Button ib_picture;
	private Button ib_photo;
	private EditText tv_leave_time;
	private EditText et_leave_data;
	private EditText et_leave_reason;
	private Button btn_starttime;
	private Button btn_endtime;
	private Spinner sp_leave;
	private String startDate,endDate;
	private ImageView iv_picture;
	private LeaveInfoDB leaveDB;
	private List<LeaveInfo> list = new ArrayList<LeaveInfo>();
//	private AskForLeaveInfo leaveInfo;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.attend_for_leave_report);
		leaveDB = new LeaveInfoDB(this);
		
		ll_order3_layout = (LinearLayout) this.findViewById(R.id.ll_order3_layout);
		tv_attend_leave = (TextView) findViewById(R.id.tv_attend_leave);
		tv_attend_leave.setText(PublicUtils.getResourceString(this,R.string.attendance_title));
		File file = ImageViewUtils.createFile(ImageViewUtils.TEMP_IMAGE_DIR,
				FileType.DIRECTORY);
		
		
		initLayout();
		findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	private int position ;
	private void initLayout() {
		ll_btn_layout = (LinearLayout) findViewById(R.id.ll_btn_layout);
		tv_yulan = (TextView) findViewById(R.id.tv_yulan);
		ib_picture = (Button) findViewById(R.id.btn_picture);
		ib_photo = (Button) findViewById(R.id.btn_photo);
		tv_leave_time = (EditText) findViewById(R.id.tv_leave_time);
		et_leave_data = (EditText) findViewById(R.id.et_leave_data);
		et_leave_reason = (EditText) findViewById(R.id.et_leave_reason);
		btn_starttime = (Button) findViewById(R.id.btn_starttime);
		btn_endtime = (Button) findViewById(R.id.btn_endtime);
		sp_leave = (Spinner) findViewById(R.id.sp_leave);
		ll_btn_layout.setOnClickListener(listener);
		ib_picture.setOnClickListener(listener);
		ib_photo.setOnClickListener(listener);
		btn_starttime.setOnClickListener(listener);
		btn_endtime.setOnClickListener(listener);
		iv_picture = (ImageView) findViewById(R.id.iv_picture);
		iv_picture.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String tag = (String) iv_picture.getTag();
				if (!TextUtils.isEmpty(tag)) {
					Intent intent = new Intent(ReportLeaveActivity.this, PhotoPriviewActivity.class);
					if (tag.startsWith("http")) {
						intent.putExtra("url", tag);
					}else{
						intent.putExtra("url", tag);
						intent.putExtra("isLocal", true);
					}
					startActivity(intent);
				}
			}
		});
		list = leaveDB.findAllLeaves();
		final List<String>  names  = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			names.add(list.get(i).getName());
		}
		ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, names);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//绑定 Adapter到控件
		sp_leave .setAdapter(adapter);
		sp_leave.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parent, View view, 
		            int pos, long id) {
		    	position = pos;
		    }
		    @Override
		    public void onNothingSelected(AdapterView<?> parent) {
		    }
		});
		
		
		initSearchDate();
		
		
	}
	/**
	 *初始化查询日期
	 */
	private void initSearchDate(){
		endDate = DateUtil.getDateByDateAndTime(DateUtil.getInternalDateByDay(new Date(), 1));
			startDate = DateUtil.getDateTime();
			try {
				startDate =replace(startDate, 15);
				endDate = replace(endDate,15);
			} catch (Throwable e) {
				e.printStackTrace();
			}
			
		btn_starttime.setText(startDate);
		btn_endtime.setText(endDate);
	}
	public  String replace(String str, int n)
			throws Throwable {
		try {
			str = str.substring(0, n - 1);
			
		} catch (Exception ex) {
			throw new Throwable(PublicUtils.getResourceString(this,R.string.leave_m6));
		}
		return str = str+"00";
	}
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ll_btn_layout:
				if(!TextUtils.isEmpty(et_leave_data.getText().toString())&&et_leave_data.getText().toString().contains(".")){
					Toast.makeText(ReportLeaveActivity.this, R.string.leave_m7, Toast.LENGTH_LONG).show();;
					return;
				}else if(!TextUtils.isEmpty(tv_leave_time.getText().toString())&&tv_leave_time.getText().toString().contains(".")){
					Toast.makeText(ReportLeaveActivity.this, R.string.leave_m7, Toast.LENGTH_LONG).show();;
					return;
				}else if(btn_starttime.getText().toString().equals(btn_endtime.getText().toString())){
					Toast.makeText(ReportLeaveActivity.this,R.string.leave_m8,Toast.LENGTH_LONG).show();
					return ;
				}else if(DateUtil.compareDateStr(btn_starttime.getText().toString()+":00", btn_endtime.getText().toString()+":00")<0){
					Toast.makeText(ReportLeaveActivity.this, R.string.leave_m9,Toast.LENGTH_LONG).show();
					return ;
				}else if(TextUtils.isEmpty(et_leave_data.getText().toString())&&TextUtils.isEmpty(tv_leave_time.getText().toString())){
					Toast.makeText(ReportLeaveActivity.this, R.string.leave_m11, Toast.LENGTH_LONG).show();;
					return;
				}else if(TextUtils.isEmpty(et_leave_data.getText().toString())&&"0".equals(tv_leave_time.getText().toString())){
					Toast.makeText(ReportLeaveActivity.this, R.string.leave_m12, Toast.LENGTH_LONG).show();;
					return;
				}else if(TextUtils.isEmpty(tv_leave_time.getText().toString())&&"0".equals(et_leave_data.getText().toString())){
					Toast.makeText(ReportLeaveActivity.this, R.string.leave_m12, Toast.LENGTH_LONG).show();;
					return;
				}else if("0".equals(tv_leave_time.getText().toString())&&"0".equals(et_leave_data.getText().toString())){
					Toast.makeText(ReportLeaveActivity.this, R.string.leave_m12, Toast.LENGTH_LONG).show();;
					return;
				}
				
				submit();
				if(!TextUtils.isEmpty(photoFileName1)){
					submitPhotoBackground(photoFileName1);
				}
				break;
			case R.id.btn_picture://相册
				gallery();
				break;
			case R.id.btn_photo://相机
				camera();
				break;
			case R.id.btn_starttime:
				searchData(TYPE_START,startDate);
				break;
			case R.id.btn_endtime:
				searchData(TYPE_END,endDate);
				break;

			default:
				break;
			}
		}
	};
	/**
	 * 从相册获取
	 */
	public void gallery() {
		photoFileName1 = System.currentTimeMillis() + ".jpg";
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
		startActivityForResult(intent, 1004);
	}
	private String photoFileName1="";
	/**
	 * 从相机获取
	 */
	public void camera() {
		Intent intentPhote = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intentPhote.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		String pathUrl = Constants.SDCARD_PATH + "/temp/";
		File filePreview = new File(pathUrl);
		if (!filePreview.exists()) {
			filePreview.mkdir();
		}
		File file = null;
			photoFileName1 = System.currentTimeMillis() + ".jpg";
			file = new File(filePreview, photoFileName1);
		
		Uri uri = Uri.fromFile(file);
		// 获取拍照后未压缩的原图片，并保存在uri路径中
		intentPhote.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		startActivityForResult(intentPhote, 2001);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (Activity.RESULT_OK == resultCode && requestCode == 2001) {
			String tempPath = null;
				tempPath = Constants.SDCARD_PATH + "/temp/"+ photoFileName1;
			
			File tempFile = new File(tempPath);
			if (!tempFile.exists()) {
				ToastOrder.makeText(ReportLeaveActivity.this, R.string.leave_m5, ToastOrder.LENGTH_SHORT).show();
			}else{
				Bitmap bitmap = null;
				bitmap = setPhoto(photoFileName1);
				iv_picture.setImageBitmap(bitmap);
			}
			iv_picture.setTag(Constants.LEAVE_PAHT + photoFileName1);
		}else if(Activity.RESULT_OK == resultCode && requestCode == 1004){
			resultForPhotoAlbum(data);
			iv_picture.setTag(Constants.LEAVE_PAHT + photoFileName1);
		}
		super.onActivityResult(requestCode, resultCode, data);
		
	}
	private void resultForPhotoAlbum(Intent data) {
		try {
			if (data != null) {
				Uri originalUri = data.getData();        //获得图片的uri 
				if (originalUri != null) {
					String tempPath = Constants.PATH_TEMP + photoFileName1;
					Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), originalUri); //显得到bitmap图片
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
					saveFile(baos.toByteArray(), photoFileName1);
					File tempFile = new File(tempPath);
					if (tempFile.exists()) {
						tempFile.delete();
					}
					iv_picture.setImageBitmap(bitmap);
				}else{
					ToastOrder.makeText(this,R.string.work_summary_c6, ToastOrder.LENGTH_SHORT).show();
				}
			}else{
				ToastOrder.makeText(this,R.string.work_summary_c6, ToastOrder.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
			ToastOrder.makeText(this,R.string.work_summary_c6, ToastOrder.LENGTH_SHORT).show();
		}
	}
	public Bitmap setPhoto(String fileName){
		String tempPath = Constants.PATH_TEMP + fileName;
		Bitmap bitmap = FileHelper.compressImage(tempPath,100/50);
//		Bitmap bitmapAddWater = settingWatermark(bitmap);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
		saveFile(baos.toByteArray(), fileName);
		File tempFile = new File(tempPath);
		if (tempFile.exists()) {
			tempFile.delete();
		}
		return bitmap;
	}
	private void saveFile(byte[] data, String fileName) {
		OutputStream os = null;
		try {
			File dir = new File(Constants.LEAVE_PAHT);
			if(!dir.exists()){
				dir.mkdirs();
			}
			File file = new File(Constants.LEAVE_PAHT + fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			os = new FileOutputStream(file);
			os.write(data, 0, data.length);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

	}
	private final int TYPE_START = 1;//开始时间
	private final int TYPE_END = 2;//结束时间
	private int year,month,day,hour;
	private String currentValue = null;
	private void searchData(final int type,String value){
		currentValue = value;
		final Dialog dialog = new Dialog(this, R.style.transparentDialog);
		View view=null;
		view=View.inflate(this, R.layout.report_date_dialog, null);
		if(!TextUtils.isEmpty(value)){
			String[] time = value.split(" ");
			String[] date = time[0].split("-");
			year = Integer.parseInt(date[0]);
			month = Integer.parseInt(date[1]) - 1;
			day = Integer.parseInt(date[2]);
			if(time.length==2&&!TextUtils.isEmpty(time[1])){
				String[] t = time[1].split(":");
				hour =Integer.parseInt(t[0]) ;
			}else{
				hour = 0;
			}
			
		}else{
			Calendar _c = Calendar.getInstance();// 获取系统默认是日期
			year = _c.get(Calendar.YEAR);
			month = _c.get(Calendar.MONTH);
			day = _c.get(Calendar.DAY_OF_MONTH);
			hour = _c.get(Calendar.HOUR_OF_DAY);
		}
		LinearLayout ll_date = (LinearLayout) view.findViewById(R.id.ll_compDialog);
		TimeView dateView = new TimeView(this,TimeView.TYPE_DATE_HOUR, new TimeView.WheelTimeListener() {
			
			@Override
			public void onResult(String wheelTime) {
				currentValue = wheelTime;
			}
		});
		dateView.setOriDateByHour(year, month+1, day,hour);
		ll_date.addView(dateView);
		Button confirmBtn=(Button)view.findViewById(R.id.report_dialog_confirmBtn);
		Button cancelBtn=(Button)view.findViewById(R.id.report_dialog_cancelBtn);
		confirmBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if (type == TYPE_START) {
					startDate = currentValue;
					try {
						startDate=	replace(startDate,15);
					} catch (Throwable e) {
						e.printStackTrace();
					}
					btn_starttime.setText(startDate);
				}else{
					endDate = currentValue;
					try {
						endDate=replace(endDate,15);
					} catch (Throwable e) {
						e.printStackTrace();
					}
					btn_endtime.setText(endDate);
				}
				
			}
		});
		cancelBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.setContentView(view);
		dialog.setCancelable(false);
		dialog.show();
	}
	private Dialog searchDialog;
	private void submit(){
		searchDialog = new MyProgressDialog(this, R.style.CustomProgressDialog,
				PublicUtils.getResourceString(this,R.string.submint_loding));
		String url = UrlInfo.doLeaveInfo(this);
		GcgHttpClient.getInstance(this).post(url, params(), new HttpResponseListener() {
			
			@Override
			public void onSuccess(int statusCode, String content) {
				JLog.d("aaa", "content = "+content);
				JSONObject obj;
				try {
					obj = new JSONObject(content);
					String resultcode = obj.getString("resultcode");
					if ("0000".equals(resultcode)) {
						Toast.makeText(ReportLeaveActivity.this, R.string.submit_ok, Toast.LENGTH_SHORT).show();
						finish();
					}else{
						Toast.makeText(ReportLeaveActivity.this, R.string.toast_one7, Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(ReportLeaveActivity.this, R.string.ERROR_DATA, Toast.LENGTH_SHORT).show();
				}
				
			}
			
			@Override
			public void onStart() {
				searchDialog.show();
			}
			
			@Override
			public void onFinish() {
				if (searchDialog != null && searchDialog.isShowing()) {
					searchDialog.dismiss();
				}
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				Toast.makeText(ReportLeaveActivity.this, R.string.toast_one7, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	private RequestParams params() {
		RequestParams params = new RequestParams();
		try {
			params.put("phoneno", PublicUtils.receivePhoneNO(this));
			String json = "";
				JSONObject dObj = new JSONObject();
			
				dObj.put("msgKey", PublicUtils.chatMsgKey(ReportLeaveActivity.this));// 
				dObj.put("startTime", startDate+":00");//
				dObj.put("endTime", endDate+":00");//
				dObj.put("leaveTime", DateUtil.getCurDateTime());//
				if(list.size()>0){
					dObj.put("type", list.get(position).getType());//
				}else{
					dObj.put("type", "");//
				}
				dObj.put("marks", et_leave_reason.getText().toString()+"");//
				dObj.put("days", et_leave_data.getText().toString()+"");//
				dObj.put("hours",tv_leave_time.getText().toString()+ "");//
				dObj.put("stauts", "0");//
				dObj.put("imageName", photoFileName1);//
				dObj.put("auditComment", "");//
				json = dObj.toString();
			
			params.put("data",json );

		} catch (JSONException e) {
			e.printStackTrace();
		}
		JLog.d(TAG, "params:" + params.toString());
		return params;
	}
	
	/**
	 * 提交订单图片
	 * @param name
	 */
	private void submitPhotoBackground(String name){
		PendingRequestVO vo = new PendingRequestVO();
		vo.setTitle("请假图片");
		vo.setMethodType(SubmitWorkManager.HTTP_METHOD_POST);
		vo.setType(TablePending.TYPE_IMAGE);
		vo.setUrl(UrlInfo.getUrlPhoto(this));
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("name", name);
		params.put("companyid", String.valueOf(SharedPreferencesUtil.getInstance(this).getCompanyId()));
		params.put("md5Code", MD5Helper.getMD5Checksum2(Constants.LEAVE_PAHT + name));
		vo.setParams(params);
		vo.setImagePath(Constants.LEAVE_PAHT + name);
		SubmitWorkManager.getInstance(this).performImageUpload(vo);
		SubmitWorkManager.getInstance(this).commit();
	}
	@Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
    }
}
