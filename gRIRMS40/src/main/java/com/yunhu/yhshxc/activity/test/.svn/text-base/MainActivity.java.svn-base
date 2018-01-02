package com.yunhu.yhshxc.activity.test;


import gcg.org.debug.JLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.bo.LocationResult;
import com.yunhu.yhshxc.comp.page.PageComp;
import com.yunhu.yhshxc.comp.page.PageCompListener;
import com.yunhu.yhshxc.database.DatabaseHelper;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.location.ReceiveLocationListener;
import com.yunhu.yhshxc.order2.OrderSearchListActivity;
import com.yunhu.yhshxc.service.LocationService;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.android.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.RequestParams;

public class MainActivity extends Activity implements ReceiveLocationListener,PageCompListener{

	@Override
	public void onLowMemory() {
		System.gc();
		super.onLowMemory();
	}

	private static TextView resultView;
	private static TextView counterView;
    private static Button actionBtn,button3;

    private static long testSucCount=0;
	private static long testFalCount=0;
	
	private LinearLayout ll_page;
	private Button createOrder,searchOrder;
	
	private PullToRefreshListView lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.test);
		createOrder = (Button)findViewById(R.id.button1);
		searchOrder = (Button)findViewById(R.id.button2);
		createOrder.setOnClickListener(listener);
		searchOrder.setOnClickListener(listener);
		ll_page = (LinearLayout)findViewById(R.id.ll_page);
		button3 = (Button)findViewById(R.id.button3);
		button3.setOnClickListener(listener);
//		resultView=(TextView)this.findViewById(R.id.result);
//		Date date = DateUtil.getDate("2014-12-15 10:11:10");
//		String dateStr = DateUtil.getFirstDateByWeek(date);
//		resultView.setText(dateStr);
//		
//		Calendar now = Calendar.getInstance();
//		now.setTime(date);
//		counterView=(TextView) this.findViewById(R.id.counter);
//		counterView.setText(now.get(Calendar.WEEK_OF_YEAR)+"");
		
		//DropDown dropDown = new DropDown(this,2);
//		DropDown dropDown = (DropDown)findViewById(R.id.dropdown);
//		List<Dictionary> list = new DictDB(this).findDictList("t_m_98", "data_121",null, null);
//		dropDown.setSrcDictList(list);
		//((LinearLayout)this.findViewById(R.id.ll_test)).addView(dropDown);
		 //test00();
		 //test01();
		 
//		 String imsi = PublicUtils.getImsi(this);
//		 String imei = PublicUtils.getDeviceId(this);
//		 System.out.println(imsi+"/"+imei);
		
//		TestDateThread t = new TestDateThread();
//		
//		for(int i=0; i<20; i++){
//			new Thread(t).start();
//		}

		lv = (PullToRefreshListView)findViewById(R.id.lv);
		lv.setAdapter(new MyAdapter());
	}
	
	
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return 10;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout ll = new LinearLayout(getApplicationContext());
			for (int i = 0; i <15; i++) {
				TextView tv = new TextView(getApplicationContext());
				
				tv.setText(PublicUtils.getResourceString(MainActivity.this,R.string.product_type)+"+"+i);
				ll.addView(tv);
			}
			convertView = ll;
			return convertView;
		}
		
	}
	
	class TestDateThread implements Runnable{

		@Override
		public void run() {
			for(int i=0; i<100;i++){
				Log.d("888", Thread.currentThread().getName()+":"+DateUtil.getCurDateTime());
			}
		}
		
	}
	
	/**
	 * push收到以后通知服务器

	 */
	private void submit(){
		try {
			JSONArray array=new JSONArray();
			array.put(0, "38853ce19cea43d57.49186673");
			RequestParams params = new RequestParams();
			params.put("cl", array.toString());
			params.put("mdn","18910901892");
			params.put("test", Constants.PUSH_TEST+"");
			String url = Constants.URL_PUSH_CALLBACK_STATUS;
			GcgHttpClient.getInstance(this).post(url, params, new HttpResponseListener(){

				@Override
				public void onStart() {
					// TODO Auto-generated method stub
					createOrder.setText(PublicUtils.getResourceString(MainActivity.this,R.string.ask_for));
					createOrder.setEnabled(false);
				}
				
				@Override
				public void onFailure(Throwable error, String content) {
					//提交失败
					String res = PublicUtils.verifyReturnValue(content);
					Toast.makeText(MainActivity.this, PublicUtils.getResourceString(MainActivity.this,R.string.toast_failed)+"："+res, Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onSuccess(int statusCode, String content) {
					//提交成功，（如果返回是为空，说明并未成功，使用备用接口）
					String res = PublicUtils.verifyReturnValue(content);
					Toast.makeText(MainActivity.this, PublicUtils.getResourceString(MainActivity.this,R.string.toast_success)+res, Toast.LENGTH_SHORT).show();
				}
				
				@Override
				public void onFinish() {
					createOrder.setText(PublicUtils.getResourceString(MainActivity.this,R.string.request));
					createOrder.setEnabled(true);
				}
				
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.button1:
				submit();
				break;
			case R.id.button2:
				Intent intent2 = new Intent(MainActivity.this, OrderSearchListActivity.class);
				startActivity(intent2);
				break;
			case R.id.button3:
				query();
				break;

			default:
				break;
			}
			
		}
	};
	private DatabaseHelper openHelper;
	private void query(){
		openHelper = DatabaseHelper.getInstance(this);
		List<Dictionary> list = sss();
		JLog.d("jishen", list.toString());
	}
	
	private List<Dictionary> sss(){
		List<Dictionary> list = new ArrayList<Dictionary>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select DICT_t_m_21635.did,DICT_t_m_21635.data_12502,DICT_t_m_21636.did,DICT_t_m_21636.data_12503  from ").append("DICT_t_m_21635,DICT_t_m_21636 ").append("  where  1=1 and  ");
		sql.append(" DICT_t_m_21635.did = ").append(" DICT_t_m_21636.data_12503");
		Cursor cursor = openHelper.query(sql.toString(), null);
		Dictionary dict = null;
		if (cursor.getCount() > 0) {
			int i =0;
			while (cursor.moveToNext()) {
				dict = new Dictionary();
//				JLog.d("jishen", cursor.getColumnName(cursor.getPosition()));
//				dict.setDid(cursor.getString(cursor.getColumnIndex("did")));
//				dict.setCtrlCol(cursor.getString(cursor.getColumnIndex(dictDataId)));
				list.add(dict);
			}
		}
		cursor.close();
		return list;
	}
	
	
	
	
//	11-21 12:04:49.114: D/SubmitService(3378): 要上传的数据====>1
//			11-21 12:04:49.154: D/SubmitService(3378): -------------提交考勤表单------------------------
//			11-21 12:04:49.154: D/SubmitService(3378): PARAMS ---->intime  ===>2013-11-21 12:04:48
//			11-21 12:04:49.154: D/SubmitService(3378): PARAMS ---->inposition  ===>北京市朝阳区京广桥北京市朝阳区人民政府呼家楼街道东大桥，京广桥南62米，三环西4米 [精确到500米]
//			11-21 12:04:49.154: D/SubmitService(3378): PARAMS ---->ingisx  ===>116.46149
//			11-21 12:04:49.154: D/SubmitService(3378): PARAMS ---->ingisy  ===>39.91868
//			11-21 12:04:49.154: D/SubmitService(3378): PARAMS ---->ingistype  ===>混合定位
//			11-21 12:04:49.154: D/SubmitService(3378): PARAMS ---->acc  ===>500.0
//			11-21 12:04:49.154: D/SubmitService(3378): PARAMS ---->status  ===>502
//			11-21 12:04:49.154: D/SubmitService(3378): PARAMS ---->action  ===>getaddr
//			11-21 12:04:49.154: D/SubmitService(3378): PARAMS ---->version  ===>3
//			11-21 12:04:49.164: D/SubmitService(3378): PARAMS ---->inimage  ===>1385006655648.jpg
//			11-21 12:04:49.164: D/SubmitService(3378): PARAMS ---->incomment  ===>aaaaaaaaa测试提交
//			11-21 12:04:49.304: D/SubmitService(3378): baseUrlAttendanceImageResultCode==>641050
//			11-21 12:04:49.364: D/SubmitService(3378): -------------提交考勤结束------------------------
//			11-21 12:04:49.364: D/SubmitService(3378): 要上传的数据====>0
//
//			
	public void test00() {
		this.setTitle("高森明晨内部后台提交测试");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("intime", "2013-11-21 12:04:48");
		params.put("inposition", "北京市朝阳区京广桥北京市朝阳区人民政府呼家楼街道东大桥，京广桥南62米，三环西4米 [精确到500米]");
		params.put("ingisx", "116.46149");
		params.put("ingisy", "39.91868");
		params.put("ingistype", "混合定位");
		params.put("acc", "500.0");
		params.put("status", "502");
		params.put("action", "getaddr");
		params.put("version", "3");
		params.put("inimage", "1385006655648.jpg");
		params.put("incomment", "测试提交测试提交测试提交测试提交");
//		for(int i=0; i<500; i++){
//			new CoreHttpHelper(this)
//			.performJustSubmit(
//					SubmitInBackstageManager.HTTP_METHOD_POST,
//					"http://219.148.162.73:8080/com.grandison.grirms.phone.web-1.0.0/doWorkAttendInfo.do",
//					params);
//		}

	}
	
	public void test01() {
		this.setTitle("高森明晨内部后台提交测试");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("patchId", "1384943932128");
		params.put("type", "1");
		params.put("10305", "2013-11-20 18:38:45");
		params.put("tableId", "20348");
		params.put("10304", "0");
		params.put("10307", "0");
		params.put("10306", "2013-11-20 18:38:50");
		params.put("10301", "52");
		params.put("10302", "52");
		Log.d("test", "test01 exe");
//		for(int i=0; i<500; i++){
//			new CoreHttpHelper(this)
//			.performJustSubmit(
//					SubmitInBackstageManager.HTTP_METHOD_POST,
//					"http://219.148.162.73:8080/com.grandison.grirms.phone.web-1.0.0/doSaveInfo.do",
//					params);
//		}

	}
	
	public void test02(){
		this.setTitle("高森明晨内部定位测试");
		resultView=(TextView)this.findViewById(R.id.result);
		counterView=(TextView) this.findViewById(R.id.counter);
        actionBtn=(Button) this.findViewById(R.id.actionBtn);

        this.startService(new Intent(this, LocationService.class));
        actionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	for(int i=0;i<3;i++){
//            		new LocationFactory(MainActivity.this).startNewLocation(MainActivity.this,true);
            	}
            }
        });
	}

	@Override
	public void onReceiveResult(LocationResult result) {

		if(result.isStatus()){
			testSucCount++;
			StringBuffer sb = new StringBuffer();
			sb.append("addr:").append(result.getAddress());
			sb.append("\n Lon:").append(result.getLongitude());
			sb.append("\n Lat:").append(result.getLatitude());
			sb.append("\n acc:").append(result.getRadius());
			sb.append("\n type:").append(result.getPosType());
			resultView.setText(sb.toString());
			
		}else{
			testFalCount++;
			resultView.setText("Failed");
		}

		counterView.setText("Success:"+testSucCount+",Fail:"+testFalCount);
		
	}
	
	
	private void addPage(){
		PageComp comp = new PageComp(this, this);
		comp.settingPage(20, 100);
		ll_page.addView(comp.getView());
	}

	@Override
	public void pageSelect(int page) {
		Toast.makeText(this, String.valueOf(page+1), Toast.LENGTH_LONG).show();
		
	}

}
