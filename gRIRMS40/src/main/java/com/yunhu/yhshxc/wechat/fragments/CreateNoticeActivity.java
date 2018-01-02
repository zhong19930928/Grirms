package com.yunhu.yhshxc.wechat.fragments;

import gcg.org.debug.JLog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.bo.OrgUser;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.CoreHttpHelper;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.submitManager.bo.PendingRequestVO;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.wechat.Util.WechatUtil;
import com.yunhu.yhshxc.wechat.bo.Notification;
import com.yunhu.yhshxc.wechat.db.NotificationDB;
import com.yunhu.yhshxc.wechat.view.ChooseView;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.loopj.android.http.RequestParams;

public class CreateNoticeActivity extends AbsBaseActivity {
	private EditText et_group_name;
	private EditText et_group_infomation;
	private LinearLayout ll_create_notice;
//	private LinearLayout ll_fujian;
	private TextView tv_create_tongzhi;
	private TextView tv_create_tongzhi_return;
//	private TextView tv_fujian;
	private ImageView iv_fujian_de;
	private ChooseView chooseView;
	private Button btn_starttime;
	private Button btn_endtime;
	private String startDate,endDate;
	private WechatUtil util;
	private NotificationDB notificationDB ;
	private MyProgressDialog progressDialog;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wechat_create_notice);
		util = new WechatUtil(this);
		notificationDB = new NotificationDB(this);
		initView(); 
		initSearchDate();
	}
	private void initView() {
		et_group_name = (EditText) findViewById(R.id.et_group_name);
		et_group_infomation = (EditText) findViewById(R.id.et_group_infomation);
		ll_create_notice= (LinearLayout) findViewById(R.id.ll_create_notice);
		tv_create_tongzhi = (TextView) findViewById(R.id.tv_create_tongzhi);
		tv_create_tongzhi_return = (TextView) findViewById(R.id.tv_create_tongzhi_return);
		iv_fujian_de =  (ImageView) findViewById(R.id.iv_fujian_de);
		btn_starttime= (Button) findViewById(R.id.btn_starttime);
		btn_endtime= (Button) findViewById(R.id.btn_endtime);
		iv_fujian_de.setOnClickListener(listener);
		btn_endtime.setOnClickListener(listener);
		btn_starttime.setOnClickListener(listener);
		tv_create_tongzhi.setOnClickListener(listener);
		tv_create_tongzhi_return.setOnClickListener(listener);
		chooseView= new ChooseView(CreateNoticeActivity.this);
		ll_create_notice.addView(chooseView.getView());
	}
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_starttime:
				searchData(TYPE_START,startDate);
				break;
			case R.id.btn_endtime:
				searchData(TYPE_END,endDate);
				break;
			case R.id.tv_create_tongzhi:
				creatSubmit();
				break;
			case R.id.iv_fujian_de:
				selectFileFromLocal();
				break;
			case R.id.tv_create_tongzhi_return:
				finish();
				break;
			default:
				break;
			}
		}

	};
	/**
     * 选择文件
     */
    public static final int REQUEST_CODE_SELECT_FILE = 24;
    private String atachmentPath;
    private void selectFileFromLocal() {
    	atachmentPath = "";
        Intent intent = null;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        } else {
            intent = new Intent( Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
    }
	private String picturePath;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == REQUEST_CODE_SELECT_FILE){
			 if (data != null) {
                Uri uri = data.getData();
                if (uri != null) {
                    sendFile(uri);
                }
            }
		}
//		if (requestCode == RESULT_LOAD_FILE && resultCode == RESULT_LOAD_FILE
//				&& data != null) {
//			picturePath = data.getStringExtra("path");
//			String fileName = data.getStringExtra("fileName");
//			TextView tv = new TextView(CreateNoticeActivity.this);
//			tv.setText(fileName);
//			ll_fujian.addView(tv);
//		}
	}
	private void sendFile(Uri uri) {
		String filePath = null;
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = getContentResolver().query(uri, projection, null,
                        null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(column_index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            filePath = uri.getPath();
        }
        File file = new File(filePath);
        if (file == null || !file.exists()) {
            ToastOrder.makeText(getApplicationContext(), R.string.work_summary_c7, ToastOrder.LENGTH_SHORT).show();
            return;
        }
        if (file.length() > 10 * 1024 * 1024) {
            ToastOrder.makeText(getApplicationContext(), R.string.wechat_content39, ToastOrder.LENGTH_SHORT).show();
            return;
        }
        
        if(filePath.endsWith(".doc") || filePath.endsWith(".docx") || 
        		filePath.endsWith(".xls") || filePath.endsWith(".xlsx")|| 
        		filePath.endsWith(".ppt") || filePath.endsWith(".pptx")|| 
        		filePath.endsWith(".pdf") || filePath.endsWith(".txt")|| 
        		filePath.endsWith(".png") || filePath.endsWith(".PNG")|| 
        		filePath.endsWith(".jpg") || filePath.endsWith(".JPG")|| 
        		filePath.endsWith(".GIF") || filePath.endsWith(".gif")|| 
        		filePath.endsWith(".JPEG") || filePath.endsWith(".jpeg")|| 
        		filePath.endsWith(".BMP") || filePath.endsWith(".bmp")|| 
        		filePath.endsWith(".3gp") || filePath.endsWith(".3GP")){
        	   try {
	        		if (!TextUtils.isEmpty(filePath)) {
					int index = filePath.lastIndexOf("/");
					String name = filePath.substring(index + 1);
					JSONObject aObj = null;
					if (TextUtils.isEmpty(atachmentPath)) {
						aObj = new JSONObject();
					}else{
						aObj = new JSONObject(atachmentPath);
					}
					aObj.put(name, filePath);
			        atachmentPath = aObj.toString();
				}
			   JLog.d(TAG, "附件:"+atachmentPath);
			   iv_fujian_de.setImageDrawable(getResources().getDrawable(R.drawable.wechat_fujian_you));
			} catch (Exception e) {
				e.printStackTrace();
			}
        }else{
        	  ToastOrder.makeText(getApplicationContext(), R.string.un_support_type, ToastOrder.LENGTH_SHORT).show();
	         return;
        }
	}
	private void creatSubmit() {
		Notification notification = new Notification();
		String title = et_group_name.getText().toString().trim();
		if(TextUtils.isEmpty(title)){
			ToastOrder.makeText(CreateNoticeActivity.this, R.string.wechat_content43, ToastOrder.LENGTH_LONG).show();
			return;
		}else{
			notification.setTitle(title);
		}
		String content = et_group_infomation.getText().toString().trim();
		if(TextUtils.isEmpty(content)){
			ToastOrder.makeText(CreateNoticeActivity.this, R.string.wechat_content42, ToastOrder.LENGTH_LONG).show();
			return;
		}else{
			notification.setContent(content);
		}
		
		if (!TextUtils.isEmpty(endDate)&&endDate.compareTo(startDate)<0) {
			ToastOrder.makeText(CreateNoticeActivity.this, R.string.wechat_content41, ToastOrder.LENGTH_LONG).show();
			return;
		}
		String startTime = startDate+" 00:00:00";
		if(TextUtils.isEmpty(endDate)){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR)+10);
			endDate = format.format(calendar.getTime());
		}
		String endTime = endDate+" 00:00:00";
		
		notification.setFrom(startTime);
		notification.setTo(endTime);
		int flag = chooseView.getFlag();
		if(flag == 0){
			int type = chooseView.getType();
			if(type == 0){
				ToastOrder.makeText(CreateNoticeActivity.this, R.string.wechat_content40, ToastOrder.LENGTH_LONG).show();
				return;
			}else{
				notification.setPeoples(String.valueOf(type));
			}
		}else if(flag == 1){
			List<OrgUser> orgList = chooseView.getOrgUsersZW();
			if(orgList.size() == 0){
				ToastOrder.makeText(CreateNoticeActivity.this, R.string.wechat_content40, ToastOrder.LENGTH_LONG).show();
				return;
			}
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < orgList.size(); i++) {
				OrgUser o = orgList.get(i);
				sb.append(o.getRoleId()).append(",");
			}
			int roleIds =SharedPreferencesUtil.getInstance(this).getRoleId();
			sb.append(roleIds);
			notification.setRole(sb.toString());
		}else{
			List<OrgUser> orgList = chooseView.getOrgUsers();
			if(orgList.size() == 0){
				ToastOrder.makeText(CreateNoticeActivity.this, R.string.wechat_content40, ToastOrder.LENGTH_LONG).show();
				return;
			}
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < orgList.size(); i++) {
				OrgUser o = orgList.get(i);
				sb.append(o.getUserId()).append(",");
			}
			int userId =SharedPreferencesUtil.getInstance(this).getUserId();
			sb.append(userId);
			notification.setUsers(sb.toString());
			
		}
		notification.setAttachment(atachmentPath);
		submit(notification);
		submitAttachment(notification);
	}
	/**
	 * 提交公告附件
	 * @param notification
	 */
	private void submitAttachment(Notification notification) {
		try {
			if (notification!=null) {
				String atachment = notification.getAttachment();
				if (!TextUtils.isEmpty(atachment)) {
					JSONObject attObj = new JSONObject(atachment);
					Iterator<String> iterator = attObj.keys();
					while (iterator.hasNext()) {
						String key = iterator.next();
						String url = attObj.getString(key);
						PendingRequestVO vo = new PendingRequestVO();
						vo.setTitle(PublicUtils.getResourceString(this,R.string.wechat_content36));
						vo.setContent(PublicUtils.getResourceString(this,R.string.wechat_content35));
						vo.addFiles("name", url);	
						HashMap<String, String> params = new HashMap<String, String>();
						params.put("cid", String.valueOf(SharedPreferencesUtil.getInstance(this).getCompanyId()));
						params.put("name", key);
						params.put("fip", "help.gcgcloud.com");
//						params.put("md5Code", MD5Helper.getMD5Checksum2(url));
						vo.setParams(params);
						new CoreHttpHelper().performWehatUpload(this, vo);
					}
				}
			}
		} catch (Exception e) {
		}
	}
	private void submit(final Notification notification) {
		progressDialog = new MyProgressDialog(CreateNoticeActivity.this,R.style.CustomProgressDialog,
				getResources().getString(R.string.str_wechat_fabu));
		progressDialog.show();
		String url = UrlInfo.doWeChatNoticeInfo(this);
		GcgHttpClient.getInstance(this).post(url, params(notification),
				new HttpResponseListener() {

					@Override
					public void onSuccess(int statusCode, String content) {
						JLog.d("alin", "content:" + content);
						try {
							JSONObject obj = new JSONObject(content);
							String resultcode = obj.getString("resultcode");
							if ("0000".equals(resultcode)) {
								if (progressDialog != null && progressDialog.isShowing()) {
									progressDialog.dismiss();
								}
								ToastOrder.makeText(CreateNoticeActivity.this, R.string.wechat_content38, ToastOrder.LENGTH_LONG).show();
								finish();
							} else {
								throw new Exception();
							}
						} catch (Exception e) {
//							searchHandler.sendEmptyMessage(3);
							if (progressDialog != null && progressDialog.isShowing()) {
								progressDialog.dismiss();
							}
							ToastOrder.makeText(CreateNoticeActivity.this,R.string.wechat_content37, ToastOrder.LENGTH_LONG).show();
						}
					}

					@Override
					public void onStart() {
					}

					@Override
					public void onFinish() {
						if (progressDialog != null && progressDialog.isShowing()) {
							progressDialog.dismiss();
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						JLog.d("alin", "failure");
						if (progressDialog != null && progressDialog.isShowing()) {
							progressDialog.dismiss();
						}
						ToastOrder.makeText(CreateNoticeActivity.this, R.string.wechat_content37, ToastOrder.LENGTH_LONG).show();
					}
				});
	}
	private RequestParams params(Notification notification) {
		RequestParams params = new RequestParams();
		params.put("phoneno", PublicUtils.receivePhoneNO(this));
		try {
			String data = util.submitNoticeJson(notification);
			params.put("data", data);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JLog.d("alin", "params:"+params.toString());
		return params;
	}
	
	/**
	 *初始化查询日期
	 */
	private void initSearchDate(){
//		startDate = DateUtil.getDateByDate(DateUtil.getInternalDateByDay(new Date(), -1));
		startDate = DateUtil.getCurDate();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR)+10);
//		endDate = format.format(calendar.getTime());
//		btn_starttime.setText(startDate);
//		btn_endtime.setText(endDate);
//		btn_starttime.setHint("有效开始日期");
//		btn_endtime.setHint("有效结束日期");
	}
	private final int TYPE_START = 1;//开始时间
	private final int TYPE_END = 2;//结束时间
	private int year,month,day;
	private String currentValue = null;
	private void searchData(final int type,String value){
		currentValue = value;
//		final Dialog dialog = new Dialog(this, R.style.transparentDialog);
//		View view=null;
//		view=View.inflate(this, R.layout.report_date_dialog, null);
		if(!TextUtils.isEmpty(value)){
			String[] date = value.split("-");
			year = Integer.parseInt(date[0]);
			month = Integer.parseInt(date[1]) - 1;
			day = Integer.parseInt(date[2]);
		}else{
			Calendar _c = Calendar.getInstance();// 获取系统默认是日期
			year = _c.get(Calendar.YEAR);
			month = _c.get(Calendar.MONTH);
			day = _c.get(Calendar.DAY_OF_MONTH);
		}
		final DatePickerDialog pcikDialog = new DatePickerDialog(this,R.style.NewDatePickerDialog,null, year, month, day);
		 final DatePicker datePicker = pcikDialog.getDatePicker();
			pcikDialog.setButton(DialogInterface.BUTTON_NEGATIVE,PublicUtils.getResourceString(CreateNoticeActivity.this,R.string.Cancle),new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {	
			
					if (pcikDialog!=null&&pcikDialog.isShowing()) {
						pcikDialog.dismiss();
					}
					
				}
			});
			pcikDialog.setButton(DialogInterface.BUTTON_POSITIVE,PublicUtils.getResourceString(CreateNoticeActivity.this,R.string.Confirm),new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
			
					int month1=datePicker.getMonth() + 1;
					int day1 = datePicker.getDayOfMonth();
					String month2=month1>=10?month1+"":"0"+month1;
					String day2 = day1>=10?day1+"":"0"+day1;
					String dateStr = datePicker.getYear() + "-" + month2 + "-"
							+ day2;
					  currentValue=dateStr;
					if (type == TYPE_START) {
						startDate = currentValue;
						btn_starttime.setText(startDate);
					}else{
						endDate = currentValue;
						btn_endtime.setText(endDate);
					}
				
				}
			});
			pcikDialog.setCanceledOnTouchOutside(false);
			pcikDialog.show();
//		LinearLayout ll_date = (LinearLayout) view.findViewById(R.id.ll_compDialog);
//		TimeView dateView = new TimeView(this,TimeView.TYPE_DATE, new TimeView.WheelTimeListener() {
//			
//			@Override
//			public void onResult(String wheelTime) {
//				currentValue = wheelTime;
//			}
//		});
//		dateView.setOriDate(year, month+1, day);
//		ll_date.addView(dateView);
//		Button confirmBtn=(Button)view.findViewById(R.id.report_dialog_confirmBtn);
//		Button cancelBtn=(Button)view.findViewById(R.id.report_dialog_cancelBtn);
//		confirmBtn.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//				if (type == TYPE_START) {
//					startDate = currentValue;
//					btn_starttime.setText(startDate);
//				}else{
//					endDate = currentValue;
//					btn_endtime.setText(endDate);
//				}
//				
//			}
//		});
//		cancelBtn.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//			}
//		});
//		dialog.setContentView(view);
//		dialog.setCancelable(false);
//		dialog.show();
	}
}
