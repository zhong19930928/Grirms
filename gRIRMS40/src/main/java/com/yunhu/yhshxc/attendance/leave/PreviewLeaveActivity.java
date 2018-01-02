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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * 请假 查看，只能展示，不能修改
 * @author xuelinlin
 *
 */
public class PreviewLeaveActivity extends AbsBaseActivity {
	private LinearLayout ll_order3_layout;
	private TextView tv_attend_leave;
	
//	private LinearLayout ll_btn_layout;
//	private TextView tv_yulan;
//	private Button ib_picture;
//	private Button ib_photo;
	private LinearLayout ll_btn_layout; 
	private EditText tv_leave_time;
	private EditText et_leave_data;
	private EditText et_leave_reason;
	private EditText et_leave_shenpi;
	private Button btn_starttime;
	private Button btn_endtime;
	private Spinner sp_leave;
	private String startDate,endDate;
	private ImageView iv_picture;
	private LeaveInfoDB leaveDB;
	private List<LeaveInfo> list = new ArrayList<LeaveInfo>();
	private Intent intent;
	private AskForLeaveInfo leaveInfo;
	private AskForLeaveInfoDB leaveInfoDB;
	private int pages;
//	private int type;
	private int id;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;
	
	private Button btn_name;
	private Button btn_org;
	private Button btn_create_time;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.attend_for_leave_do);
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ic_empty)
		.showImageForEmptyUri(R.drawable.ic_empty)
		.showImageOnFail(R.drawable.ic_empty)
		.cacheOnDisk(true)
		.cacheInMemory(true).displayer(new RoundedBitmapDisplayer(0))
		.build();
		init();
		
	}
	private void init() {
		leaveInfoDB = new AskForLeaveInfoDB(this);
		intent = getIntent();
		pages = intent.getIntExtra("pages", 1);
		id = intent.getIntExtra("id",0);
		leaveInfo = leaveInfoDB.findLeavesById(id);
		
	leaveDB = new LeaveInfoDB(this);
	
	ll_order3_layout = (LinearLayout) this.findViewById(R.id.ll_order3_layout);
	tv_attend_leave = (TextView) findViewById(R.id.tv_attend_leave);
	tv_attend_leave.setText(PublicUtils.getResourceString(this,R.string.leave_m14));
	File file = ImageViewUtils.createFile(ImageViewUtils.TEMP_IMAGE_DIR,
			FileType.DIRECTORY);
	File file1 = ImageViewUtils.createFile(Constants.LEAVE_PAHT_TEMP,
			FileType.DIRECTORY);
	initLayout();
	findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			intent.putExtra("pages", pages);
			setResult(10, intent);
			finish();
		}
	});
	}
	private int position ;
	private void initLayout() {
		btn_org = (Button) findViewById(R.id.btn_org);
		btn_name = (Button) findViewById(R.id.btn_name);
		btn_create_time = (Button) findViewById(R.id.btn_create_time);
		ll_btn_layout = (LinearLayout) findViewById(R.id.ll_btn_layout);
		ll_btn_layout.setVisibility(View.GONE);
//		tv_yulan = (TextView) findViewById(R.id.tv_yulan);
//		tv_yulan.setText("修改");
//		ib_picture = (Button) findViewById(R.id.btn_picture);
//		ib_photo = (Button) findViewById(R.id.btn_photo);
		tv_leave_time = (EditText) findViewById(R.id.tv_leave_time);
		et_leave_data = (EditText) findViewById(R.id.et_leave_data);
		et_leave_reason = (EditText) findViewById(R.id.et_leave_reason);
		et_leave_shenpi = (EditText) findViewById(R.id.et_leave_shenpi);
		btn_starttime = (Button) findViewById(R.id.btn_starttime);
		btn_endtime = (Button) findViewById(R.id.btn_endtime);
		sp_leave = (Spinner) findViewById(R.id.sp_leave);
//		ll_btn_layout.setOnClickListener(listener);
//		ib_picture.setOnClickListener(listener);
//		ib_photo.setOnClickListener(listener);
//		btn_starttime.setOnClickListener(listener);
//		btn_endtime.setOnClickListener(listener);
		iv_picture = (ImageView) findViewById(R.id.iv_picture);
		iv_picture.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(leaveInfo!=null&&!TextUtils.isEmpty(leaveInfo.getImageUrl())){
					String tag = leaveInfo.getImageUrl();
							if (!TextUtils.isEmpty(tag)) {
								Intent intent = new Intent(PreviewLeaveActivity.this, PhotoPriviewActivity.class);
								if (tag.startsWith("http")) {
									intent.putExtra("url", tag);
								}else{
									intent.putExtra("url", tag);
									intent.putExtra("isLocal", true);
								}
								startActivity(intent);
							}
				}
				
			}
		});
//		if(SharedPreferencesForLeaveUtil.getInstance(this).getIS_UPDATE().equals("0")){
			sp_leave.setEnabled(false);
//			ll_btn_layout.setEnabled(false);
//			ll_btn_layout.setVisibility(View.GONE);
//			ib_picture.setEnabled(false);
//			ib_photo.setEnabled(false);
			tv_leave_time.setEnabled(false);
			btn_starttime.setEnabled(false);
			btn_endtime.setEnabled(false);
			et_leave_data.setEnabled(false);
			et_leave_reason.setEnabled(false);
			et_leave_shenpi.setEnabled(false);
//		}
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
		if(!TextUtils.isEmpty(leaveInfo.getName())){
			btn_name.setText(leaveInfo.getName());
		}else{
			btn_name.setText("");
		}
		if(!TextUtils.isEmpty(leaveInfo.getOrgName())){
			btn_org.setText(leaveInfo.getOrgName());
		}else{
			btn_org.setText("");
		}
		if(!TextUtils.isEmpty(leaveInfo.getLeaveDay())){
			btn_create_time.setText(leaveInfo.getLeaveDay().substring(0, 16));
		}else{
			btn_create_time.setText("");
		}
		if(!TextUtils.isEmpty(leaveInfo.getStartTime())){
			startDate = leaveInfo.getStartTime().substring(0, 16);
		}else{
			startDate = DateUtil.getDateByDate(DateUtil.getInternalDateByDay(new Date(), -1));
		}
		if(!TextUtils.isEmpty(leaveInfo.getEndTime())){
			endDate = leaveInfo.getEndTime().substring(0, 16);
		}else{
			endDate = DateUtil.getCurDate();
		}
		btn_starttime.setText(startDate);
		btn_endtime.setText(endDate);
		
		for (int i = 0; i < list.size(); i++) {
			if(!TextUtils.isEmpty(leaveInfo.getType())){
				if(list.get(i).getType().equals(leaveInfo.getType())){
					sp_leave.setSelection(i);
					break;
				}
			}
		}
		if(!TextUtils.isEmpty(leaveInfo.getMarks())){
			et_leave_reason.setText(leaveInfo.getMarks());
		}
		if(!TextUtils.isEmpty(leaveInfo.getDays())){
			et_leave_data.setText(leaveInfo.getDays());
		}
		if(!TextUtils.isEmpty(leaveInfo.getHours())){
			tv_leave_time.setText(leaveInfo.getHours());
		}
		if(!TextUtils.isEmpty(leaveInfo.getImageUrl())){
			imageLoader.displayImage(leaveInfo.getImageUrl(), iv_picture, options, null);
		}else if(!TextUtils.isEmpty(leaveInfo.getImageName())){
			File f = new File(Constants.LEAVE_PAHT_TEMP+leaveInfo.getImageName());
			if (f.exists()) {
				imageLoader.displayImage("file://"+Constants.LEAVE_PAHT_TEMP+leaveInfo.getImageName(), iv_picture, options, null);
			}
		}
		if(!TextUtils.isEmpty(leaveInfo.getAuditComment())){
			et_leave_shenpi.setText(leaveInfo.getAuditComment());
		}
		
	}
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ll_btn_layout:
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
				ToastOrder.makeText(PreviewLeaveActivity.this, R.string.leave_m5, ToastOrder.LENGTH_SHORT).show();
			}else{
				Bitmap bitmap = null;
				bitmap = setPhoto(photoFileName1);
				iv_picture.setImageBitmap(bitmap);
			}
			File file = new File(Constants.LEAVE_PAHT+photoFileName1);
	        if (file == null || !file.exists()) {
	            return;
	        }
	        fileResult(file);
		}else if(Activity.RESULT_OK == resultCode && requestCode == 1004){
			resultForPhotoAlbum(data);
			File file = new File(Constants.LEAVE_PAHT+photoFileName1);
	        if (file == null || !file.exists()) {
	            return;
	        }
	        fileResult(file);
		}
		
		super.onActivityResult(requestCode, resultCode, data);
		
	}
	private void fileResult(final File srcFile){
		new Thread(){
			public void run() {
	        	   try {
	        		   String filePath = srcFile.getAbsolutePath();
		        		if (!TextUtils.isEmpty(filePath)) {
						int index = filePath.lastIndexOf("/");
						String name = filePath.substring(index + 1);
						FileHelper helper = new FileHelper();
						helper.copyFile(filePath, Constants.LEAVE_PAHT_TEMP+name);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
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
					String[] str = startDate.split(" ");
					btn_starttime.setText(str[0]);
				}else{
					endDate = currentValue;
					String[] str = endDate.split(" ");
					btn_endtime.setText(str[0]);
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
						Toast.makeText(PreviewLeaveActivity.this,R.string.wechat_content31, Toast.LENGTH_SHORT).show();
						leaveInfoDB.updateLeave(leaveInfo);
						intent.putExtra("pages", pages);
						setResult(1, intent);
						finish();
					}else{
						Toast.makeText(PreviewLeaveActivity.this, R.string.toast_one7, Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(PreviewLeaveActivity.this, R.string.ERROR_DATA, Toast.LENGTH_SHORT).show();
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
//				if (searchDialog != null && searchDialog.isShowing()) {
//					searchDialog.dismiss();
//				}
				Toast.makeText(PreviewLeaveActivity.this, R.string.toast_one7, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	private RequestParams params() {
		RequestParams params = new RequestParams();
		try {
			params.put("phoneno", PublicUtils.receivePhoneNO(this));
			String json = "";
				JSONObject dObj = new JSONObject();
			
				dObj.put("msgKey", PublicUtils.chatMsgKey(PreviewLeaveActivity.this));// 
				String[] sDt = startDate.split(" ");
				if(sDt.length == 1){
					dObj.put("startTime", startDate+" 00:00:00");//
					leaveInfo.setStartTime(startDate+" 00:00:00");
				}else{
					dObj.put("startTime", startDate+":00");//
					leaveInfo.setStartTime(startDate+":00");
				}
				String[] eDt = endDate.split(" ");
				if(eDt.length == 1){
					dObj.put("endTime", endDate+" 00:00:00");//
					leaveInfo.setEndTime(endDate+" 00:00:00");
				}else{
					dObj.put("endTime", endDate+":00");//
					leaveInfo.setEndTime(endDate+":00");
				}
				
				dObj.put("leaveTime", DateUtil.getCurDateTime());//
				
				if(list.size()>0){
					dObj.put("type", list.get(position).getType());//
					leaveInfo.setType(list.get(position).getType());
				}else{
					dObj.put("type", "");//
				}
				dObj.put("marks", et_leave_reason.getText().toString()+"");//
				dObj.put("days", et_leave_data.getText().toString()+"");//
				dObj.put("hours",tv_leave_time.getText().toString()+ "");//
				dObj.put("stauts", "0");//
				if(!TextUtils.isEmpty(photoFileName1)){
					dObj.put("imageName", photoFileName1);//
					leaveInfo.setImageUrl("");
					leaveInfo.setImageName(photoFileName1);
				}else{
					String url = leaveInfo.getImageUrl();
					if(!TextUtils.isEmpty(url)){
						int index = url.lastIndexOf("/");
			 			String fileName = url.substring(index + 1);
			 			String[] str = fileName.split("@");
			 			if (str.length>1) {
			 				fileName = str[1];
						}
					dObj.put("imageName",fileName );
					}
				}
				dObj.put("auditComment", "");//
				
				json = dObj.toString();
			
				leaveInfo.setHours(tv_leave_time.getText().toString()+ "");
				leaveInfo.setDays(et_leave_data.getText().toString()+"");
				leaveInfo.setMarks(et_leave_reason.getText().toString()+"");
				
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
}
