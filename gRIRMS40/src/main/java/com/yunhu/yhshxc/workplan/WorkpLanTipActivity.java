package com.yunhu.yhshxc.workplan;

import gcg.org.debug.JLog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil2;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.workplan.bo.Assess;
import com.yunhu.yhshxc.workplan.bo.PlanData;
import com.yunhu.yhshxc.workplan.bo.WorkPlanModle;
import com.yunhu.yhshxc.workplan.db.AssessDB;
import com.yunhu.yhshxc.workplan.db.PlanDataDB;
import com.yunhu.yhshxc.workplan.db.WorkPlanModleDB;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WorkpLanTipActivity extends Activity {
	private LinearLayout planItemContainer;// 计划内容
	private LinearLayout planAssessContainer;// 计划评价
	private ImageView back;
	private TextView title;// 标题
	private int planId;
	private int planType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_workplan_tip);
		planItemContainer = (LinearLayout) findViewById(R.id.workplan_tip_container);
		planAssessContainer = (LinearLayout) findViewById(R.id.workplan_tipassess_container);
		back = (ImageView) findViewById(R.id.workplan_tip_back);
		title = (TextView) findViewById(R.id.workplan_tip_title);
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				clearDatabaseData();
				WorkpLanTipActivity.this.finish();
				
			}
		});
//		search();
		
		
		//把发起提醒时间记录下来
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SharedPreferencesUtil2.getInstance(this).saveWorkPlanTipTime(sdf.format(new Date(System.currentTimeMillis())));

	}

	/**
	 * 根据传值来查询对应计划
	 */
	private void initData() {
		Intent intent = getIntent();
		if (intent != null) {
			planId = intent.getIntExtra("plan_id", 0);
			planType = intent.getIntExtra("planType", 0);
		}
		if (planType == 1) {
			title.setText(R.string.work_plan_c1);
		} else if (planType == 2) {
			title.setText(R.string.work_plan_c2);
		} else if (planType == 3) {
			title.setText(R.string.work_plan_c3);
		}
		if (planId!=0) {
			
			WorkPlanModleDB woDb = new WorkPlanModleDB(this);
			PlanDataDB plDb = new PlanDataDB(this);
			List<WorkPlanModle> list = woDb.checkPlansByPlanId(planId);
			// 遍历数据添加到容器中
			for (int i = 0; i < list.size(); i++) {
				WorkPlanModle modle = list.get(i);
				 CreateNewPlanItem item = new CreateNewPlanItem(this);
				item.setSaveViewHidden(false);// 只显示内容框
				item.setAllSaveInfo(modle);
				planItemContainer.addView(item.getView());
			}
			
	
			// 查询评价表获取评价内容添加进去,只能展示
			AssessDB asDb = new AssessDB(this);
			List<Assess> asList = asDb.checkAllAssessByPlanId(planId);
			for (int i = 0; i < asList.size(); i++) {
				AssessItemView asView = new AssessItemView(this);
				asView.setPlanId(planId);
				asView.initData(asList.get(i));
				planAssessContainer.addView(asView.getView());
			}

			
			
		}
		

	}
	
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		clearDatabaseData();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		clearDatabaseData();
		super.onDestroy();
	}
	 
	@Override
	protected void onResume() {
		clearDatabaseData();
		search();
		super.onResume();
	}
	
	/**
	 * 清空工作计划的存储数据库
	 */
	private void clearDatabaseData() {
		WorkPlanModleDB modleDb = new WorkPlanModleDB(this);
		modleDb.clearWorkPlan();
		PlanDataDB planDb = new PlanDataDB(this);
		planDb.clearPlanDataList();
		AssessDB aDb = new AssessDB(this);
		aDb.clearAssess();
		// 清空加载数据的容器
		planItemContainer.removeAllViews();
		planAssessContainer.removeAllViews();

	}
	
	private void search(){

		String url = UrlInfo.queryWorkPlan(this);
		RequestParams params = getRequestParams();
		// 开始访问网络
		GcgHttpClient.getInstance(this).get(url, params, new HttpResponseListener() {

			@Override
			public void onSuccess(int statusCode, String content) {
//				Log.e("view", "onSuccess:" + content);
				// 解析加载下来的数据
				try {
					JSONObject obj = new JSONObject(content);
					String resultCode = obj.getString("resultcode");
					if ("0000".equals(resultCode)) {
						int planId = 0;
						int planType = 0;
						WorkPlanUtils util = new WorkPlanUtils(WorkpLanTipActivity.this);
						if (PublicUtils.isValid(obj, "planData")) {
							JSONArray arrayPlan = obj.optJSONArray("planData");
							List<PlanData> planList = util.parsePlanDataList(arrayPlan);
							planId = planList.get(0).getPlanId();
							planType = Integer.parseInt(planList.get(0).getPlanType());
						}
						if (PublicUtils.isValid(obj, "detail")) {
							JSONArray arrayDetail = obj.optJSONArray("detail");
							List<WorkPlanModle> moleList = util.parsePlanModleList(arrayDetail);
						}
						if (PublicUtils.isValid(obj, "assess")) {
							JSONArray arrayAssess = obj.optJSONArray("assess");
							List<Assess> assessList = util.parseAssessList(arrayAssess);
						}
						
					} else {
						JLog.d("WorkPlanCheckService", "返回码不为0000");
					}
				} catch (Exception e) {
					e.printStackTrace();
					JLog.d("WorkPlanCheckService", "解析数据异常");
				}

			}

			@Override
			public void onStart() {

			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				initData();
			}

			@Override
			public void onFailure(Throwable error, String content) {
				// TODO Auto-generated method stub

			}
		});

	
	}
	private RequestParams getRequestParams() {
		RequestParams param = new RequestParams();
		param.put("phoneno", PublicUtils.receivePhoneNO(getApplicationContext()));
		param.put("userId", SharedPreferencesUtil.getInstance(this).getUserId() + "");
		param.put("type", 4);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		param.put("fromTime", sdf.format(new Date(System.currentTimeMillis())));

		return param;
	}

}
