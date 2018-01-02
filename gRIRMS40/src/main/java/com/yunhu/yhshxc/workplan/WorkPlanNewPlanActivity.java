package com.yunhu.yhshxc.workplan;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.ToastUtil;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.yunhu.yhshxc.workplan.bo.Assess;
import com.yunhu.yhshxc.workplan.bo.PlanData;
import com.yunhu.yhshxc.workplan.bo.WorkPlanModle;
import com.yunhu.yhshxc.workplan.db.AssessDB;
import com.yunhu.yhshxc.workplan.db.PlanDataDB;
import com.yunhu.yhshxc.workplan.db.WorkPlanModleDB;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gcg.org.debug.JLog;
import view.DateSelectorView;
import view.DateSelectorView.OnDateClickListener;

import static com.yunhu.yhshxc.application.SoftApplication.context;

/**
 * 
 * 新建工作计划
 * 
 */
public class WorkPlanNewPlanActivity extends Activity implements OnClickListener {
	private ImageView newplan_back;// 返回
	private Button newplan_addplan;// 添加
	private Button newplan_submit;// 提交
	private DateSelectorView dateView;// 日期选择控件
	private LinearLayout newplan_result_container;// 添加展示消息的容器
	private List<CreateNewPlanItem> itemList = null;// 存储保存和编辑条目的容器
	private int planType = 0;// 计划类型,传过来的值
	private String dType = "";
	private Map<Long, WorkPlanModle> planModleData = null;// 存储保存的计划实体类
	private String createDate = "";// 创建的日期
	private String startDate = "";// 计划开始日期
	private String endDate = "";// 计划结束日期
	private int mYear = 0;
	private int mMonth = 0;
	private boolean isEditState = false;//是否有正在处于编辑状态的内容框

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_workplan_newplan);
		// 初始化视图控件
		initView();
		// 初始化数据页面
		// initData();
		ToastUtil.showText(this, PublicUtils.getResourceString(context,R.string.work_plan_c8));

	}

	private void initView() {
		newplan_back = (ImageView) findViewById(R.id.newplan_back);
		newplan_back.setOnClickListener(this);
		newplan_addplan = (Button) findViewById(R.id.newplan_addplan);
		newplan_addplan.setOnClickListener(this);
		newplan_submit = (Button) findViewById(R.id.newplan_submit);
		newplan_submit.setOnClickListener(this);
		dateView = (DateSelectorView) findViewById(R.id.newplan_dateselectview);
		Intent intent = getIntent();
		dType = intent.getStringExtra("currentType");// 获取日期控件要展示的类型
		dateView.setSpecifiedDate(DateUtil.getCurDate(), dType);
		dateView.setDateType(dType);
		dateView.setDateCompare(true);
		dateView.clearAllSelected();// 初始化清空选中状态
		dateView.isUseTextSelected(false);// 是否使用初始化选中判断
		if (dType.equals(DateSelectorView.DATE_TYPE_DAY)) {// 日计划
			planType = 1;
		} else if (dType.equals(DateSelectorView.DATE_TYPE_WEEK)) {// 周计划
			planType = 2;
		} else if (dType.equals(DateSelectorView.DATE_TYPE_MONTH)) {// 月计划
			planType = 3;
		}
		newplan_result_container = (LinearLayout) findViewById(R.id.newplan_result_container);
		itemList = new ArrayList<CreateNewPlanItem>();// 布局view的集合
		planModleData = new HashMap<Long, WorkPlanModle>();// 每个view填充的数据对象集合
		dateView.setOnDateClickListener(new OnDateClickListener() {// 选择日期的结果回调,普通日期是

			@Override
			public void onDateSelected(String date) {

				if (!TextUtils.isEmpty(date)) {
					Toast.makeText(WorkPlanNewPlanActivity.this, PublicUtils.getResourceString(context,R.string.work_plan_c9) + date, Toast.LENGTH_SHORT).show();
					if (planType == 1) {// 日计划类型
						String[] strs = date.split("-");
						mYear = Integer.parseInt(strs[0]);
						mMonth = Integer.parseInt(strs[1]);
						createDate = date;// 开始的日期
						startDate = date;// 计划开始为选择时间
						endDate = date;// 计划结束也为选择的时间
						// 根据选择的日期查询接口是否已经写过计划,写过则跳转到详情界面,否则创建一个新的计划view
						search();

					} else if (planType == 2) {// 周计划类型
						String[] dates = date.split("/");
						String[] strs = dates[0].split("-");
						mYear = Integer.parseInt(strs[0]);
						mMonth = Integer.parseInt(strs[1]);
						createDate = dates[0];// 开始的日期
						startDate = dates[0];
						endDate = dates[1];
						// 根据选择的日期查询接口是否已经写过计划,写过则跳转到详情界面,否则创建一个新的计划view
						search();

					} else if (planType == 3) {// 月计划类型
						String[] strs = date.split("-");
						mYear = Integer.parseInt(strs[0]);
						createDate = date + "-" + "01";// 开始的日期
						startDate = date + "-" + "01";
						endDate = date + "-" + DateUtil.getMonthDays(date);
						// 根据选择的日期查询接口是否已经写过计划,写过则跳转到详情界面,否则创建一个新的计划view
						search();
					}

				} else {// 如果为空的话,则将上次记录时间清空,使其不可提交
					createDate = "";// 开始的日期
					startDate = "";
					endDate = "";

				}

			}
		});

	}

	private void initData() {
		createNewPlan();

	}

	/**
	 * 创建一个新的计划条目,添加到容器,存储到集合中
	 */
	private void createNewPlan() {
		// 首先构建一个可编辑的提交框
		final CreateNewPlanItem item = new CreateNewPlanItem(this);
		item.setSaveViewHidden(true);// 隐藏保存框
		item.setItemId(System.currentTimeMillis());// 以当前时间戳其唯一标识记录
		item.setDeleteClickListener(new OnClickListener() {// 设置删除按钮监听

			@Override
			public void onClick(View v) {
				AlertDialog dialog = new AlertDialog.Builder(WorkPlanNewPlanActivity.this, R.style.NewAlertDialogStyle)
						.setTitle(R.string.tip).setMessage(R.string.work_plan_c11)
						.setPositiveButton(R.string.Confirm, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 先删除集合中的数据
						if (planModleData.containsKey(item.getItemId())) {
							planModleData.remove(item.getItemId());
						}
						// 删除view条目
						newplan_result_container.removeView(item.getView());
						itemList.remove(item);
					}
				}).setNegativeButton(R.string.Cancle, null).create();
				dialog.show();

			}
		});

		item.setSaveClickListener(new OnClickListener() {// 保存按钮的监听

			@Override
			public void onClick(View v) {
				if (item.isDataEmpty()) {
					Toast.makeText(WorkPlanNewPlanActivity.this, R.string.work_plan_c10, Toast.LENGTH_SHORT).show();
				} else {
					WorkPlanModle modle = item.getAllEditData();
					planModleData.put(item.getItemId(), modle);// key唯一,如果再次编辑保存,直接替换掉原来的
					item.setSaveViewHidden(false);
					item.setAllSaveInfo(modle);
					isEditState=false;
				}

			}
		});
		item.setEditClickListener(new OnClickListener() {// 编辑按钮的监听

			@Override
			public void onClick(View v) {
				isEditState=true;
				WorkPlanModle modle = item.getAllSaveData();
				item.setSaveViewHidden(true);
				item.setAllEditInfo(modle);

			}
		});

		itemList.add(item);// 添加到view集合,用于操作
		newplan_result_container.addView(item.getView());

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.newplan_back:// 返回
			boolean isHasData = false;
			for (int i = 0; i < itemList.size(); i++) {
				if (itemList.get(i).isInputContent()) {// 已经有输入内容
					isHasData = true;
				}
			}
			if (isHasData) {
				showTipDialog();
			} else {
				if (planModleData.size() > 0) {
					showCloseDialog();
				} else {
					this.finish();
				}
			}

			break;
		case R.id.newplan_addplan:// 添加一条计划
			if (!TextUtils.isEmpty(startDate)) {
				createNewPlan();
			} else {
				ToastUtil.showText(this, PublicUtils.getResourceString(context,R.string.work_plan_c8));
			}

			break;
		case R.id.newplan_submit:// 提交计划
			beginToSubmitData();
			break;
		default:
			break;
		}

	}

	/**
	 * 关闭提示对话框
	 */
	private void showCloseDialog() {
		AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.NewAlertDialogStyle).setTitle(R.string.tip)
				.setMessage(R.string.work_summary_c5).setPositiveButton(R.string.Confirm, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						WorkPlanNewPlanActivity.this.finish();

					}
				}).setNegativeButton(R.string.Cancle, null).setCancelable(false).create();
		alertDialog.show();

	}

	/**
	 * 提交数据
	 */
	private void beginToSubmitData() {
		// 先判断集合是否有要提交的数据
		if (planModleData.size() == 0) {// 没有数据可提交
			Toast.makeText(this, R.string.work_plan_c16, Toast.LENGTH_SHORT).show();
			return;
		}
		if (isEditState) {
			Toast.makeText(this, R.string.work_plan_c15, Toast.LENGTH_SHORT).show();
			return;
		}
		// 遍历集合,把每条数据转换成json数据保存到集合
		JSONObject jsObject = new JSONObject();
		if (TextUtils.isEmpty(createDate) || TextUtils.isEmpty(startDate) || TextUtils.isEmpty(endDate)) {
			Toast.makeText(this, R.string.work_plan_c8, Toast.LENGTH_SHORT).show();
			return;
		}

		try {
			// 拼接参数手机号+时间戳
			String msg_key = PublicUtils.receivePhoneNO(this) + System.currentTimeMillis();
			jsObject.put("msg_key", msg_key);
			// 计划类型
			jsObject.put("plan_type", planType + "");
			// 计划创建日期
			jsObject.put("plan_date", createDate);
			// 计划开始日期
			jsObject.put("start_date", startDate);
			// 计划结束日期
			jsObject.put("end_date", endDate);
			// 计划标题,创建时间+day or week or month +"plan"
			String typeStr = planType == 1 ? "day" : planType == 2 ? "week" : planType == 3 ? "month" : "";
			if (planType==3) {
				String[] strs= createDate.split("-");
				String result1 = strs[0]+"-"+strs[1];
				jsObject.put("plan_title", result1 + " " + "的计划");
			}else{
				
				jsObject.put("plan_title", createDate + " " + "的计划");
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//

		JSONArray arr = new JSONArray();
		for (Map.Entry<Long, WorkPlanModle> entry : planModleData.entrySet()) {// 遍历出每个计划添加到json数组中
			WorkPlanModle modle = entry.getValue();
			JSONObject job = new JSONObject();
			try {
				job.put("plan_title", modle.getPlanTitle());
				job.put("plan_detail", modle.getPlanContent());
				job.put("plan_marks", modle.getPlanMark());
				job.put("important_level", modle.getImportantLevel() + "");
				job.put("urgency_level", modle.getRushLevel() + "");
				arr.put(job);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		try {
			jsObject.put("detail", arr);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.e("test2", jsObject.toString());

		// 参数拼接完成,连接网络提交数据
		String postUrl = UrlInfo.saveWorkPlan(this);
		RequestParams params = new RequestParams();
		params.put("work", jsObject.toString());
		GcgHttpClient.getInstance(this).post(postUrl, params, new HttpResponseListener() {

			@Override
			public void onSuccess(int statusCode, String content) {
				Log.e("test", content);
				// {"resultcode":"0000"}//成功
				try {
					JSONObject js = new JSONObject(content);
					if (js.has("resultcode") && js.getString("resultcode").equals("0000")) {
						// 提交成功
						Toast.makeText(WorkPlanNewPlanActivity.this, R.string.submit_ok, Toast.LENGTH_SHORT).show();
						planModleData.clear();
						WorkPlanNewPlanActivity.this.finish();
					} else {
						Toast.makeText(WorkPlanNewPlanActivity.this,R.string.work_plan_c14, Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {

					e.printStackTrace();
				}

			}

			@Override
			public void onStart() {

			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFailure(Throwable error, String content) {
				Toast.makeText(WorkPlanNewPlanActivity.this, R.string.work_plan_c13, Toast.LENGTH_SHORT).show();

			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			boolean isHasData = false;
			for (int i = 0; i < itemList.size(); i++) {
				if (itemList.get(i).isInputContent()) {// 已经有输入内容
					isHasData = true;
				}
			}
			if (isHasData) {
				showTipDialog();
			} else {
				if (planModleData.size() > 0) {
					showCloseDialog();
				} else {
					this.finish();
				}
			}

		}
		return super.onKeyDown(keyCode, event);
	}

	private void showTipDialog() {
		AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.NewAlertDialogStyle).setTitle(R.string.tip)
				.setMessage(R.string.work_plan_c4).setPositiveButton(R.string.Confirm, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						WorkPlanNewPlanActivity.this.finish();

					}
				}).setNegativeButton(R.string.Cancle, null).setCancelable(false).create();

		alertDialog.show();

	}

	/**
	 * -------------------------------------------------------------------------
	 */
	private Dialog searchDialog;

	private void search() {

		// 先清空数据存储的上次查询数据
		clearDatabaseData();

		searchDialog = new MyProgressDialog(this, R.style.CustomProgressDialog, PublicUtils.getResourceString(context,R.string.init));
		String url = UrlInfo.queryWorkPlan(this);
		RequestParams param = searchParams();
		GcgHttpClient.getInstance(this.getApplicationContext()).post(url, param, new HttpResponseListener() {
			@Override
			public void onStart() {
				searchDialog.show();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				// Log.e("view", "onSuccess:" + content);
				// 解析加载下来的数据
				try {
					JSONObject obj = new JSONObject(content);
					String resultCode = obj.getString("resultcode");
					if ("0000".equals(resultCode)) {
						WorkPlanUtils util = new WorkPlanUtils(WorkPlanNewPlanActivity.this);
						if (PublicUtils.isValid(obj, "planData")) {
							JSONArray arrayPlan = obj.optJSONArray("planData");
							List<PlanData> planList = util.parsePlanDataList(arrayPlan);
						}
						if (PublicUtils.isValid(obj, "detail")) {
							JSONArray arrayDetail = obj.optJSONArray("detail");
							List<WorkPlanModle> moleList = util.parsePlanModleList(arrayDetail);
						}
						if (PublicUtils.isValid(obj, "assess")) {
							JSONArray arrayAssess = obj.optJSONArray("assess");
							List<Assess> assessList = util.parseAssessList(arrayAssess);
						}
//						if (planModleData.size()>0) {//保存过数据,提示
//							showTabDateTip();
//						}
						// 访问成功后,检查数据库的数据,是否有匹配所选日期的数据,有的话提示跳转至计划详情
						checkData();
					} else {
						throw new Exception();
					}
				} catch (Exception e) {
					e.printStackTrace();
					ToastOrder.makeText(getApplicationContext(), R.string.ERROR_DATA, ToastOrder.LENGTH_SHORT).show();
					// 访问失败,要清除日期信息
					createDate = "";
					startDate = "";
					endDate = "";
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				JLog.d("alin", "onFailure:" + content);
				ToastOrder.makeText(getApplicationContext(), R.string.retry_net_exception, ToastOrder.LENGTH_SHORT).show();
				// 访问失败,要清除日期信息
				createDate = "";
				startDate = "";
				endDate = "";
			}

			@Override
			public void onFinish() {
				if (searchDialog != null && searchDialog.isShowing()) {
					searchDialog.dismiss();
				}

			}

		});

	}
   /**
    * 未使用
    */
	private void showTabDateTip() {
		AlertDialog alDialog = new AlertDialog.Builder(this, R.style.NewAlertDialogStyle)
				.setTitle(R.string.tip)
				.setMessage(R.string.work_plan_c5)
				.setPositiveButton(R.string.Confirm, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {

						
						
					}
				})
				.setNegativeButton(R.string.Cancle,  new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						//点击取消,则不清楚数据,停止继续
						return;
						
					}} )
				.create();
		alDialog.setCancelable(false);
		alDialog.show();
		
	}

	private void checkData() {
		PlanDataDB planDb = new PlanDataDB(this);
		List<PlanData> planList = planDb.findPlanDataListByDate();
		int planId = 0;
		if (planList.size() > 0) {
			boolean hasThisData = false;
			for (int i = 0; i < planList.size(); i++) {
				if (startDate.equals(planList.get(i).getStartDate())) {// 有匹配的,已经创建过
					hasThisData = true;
					planId = planList.get(i).getPlanId();
				}
			}
			if (hasThisData) {
				// 提示弹出对话框,可跳转至详情页面,并清除日期记录数据
				ToastUtil.showText(WorkPlanNewPlanActivity.this, PublicUtils.getResourceString(context,R.string.work_plan_c12));
				startDate = "";
				createDate = "";
				endDate = "";
				showIntoDetailDialog(planId);
			} else {
				if (newplan_result_container.getChildCount()<=0) {				
					createNewPlan();
				}
			}
		} else {// 没有数据,创建新的计划
			if (newplan_result_container.getChildCount()<=0) {				
				createNewPlan();
			}

		}

	}

	private void showIntoDetailDialog(final int planId) {
		String messs = PublicUtils.getResourceString(context,R.string.work_plan_c7);
		if (planModleData.size()>0) {
			messs= PublicUtils.getResourceString(context,R.string.work_plan_c6);
		}
		AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.NewAlertDialogStyle).setTitle(R.string.tip)
				.setMessage(messs).setPositiveButton(R.string.Confirm, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 清空已创建的计划布局
						newplan_result_container.removeAllViews();
						// 清空添加计划的view缓存
						itemList.clear();
						Intent intent = new Intent(WorkPlanNewPlanActivity.this, WorkPlanDetailActivity.class);
						intent.putExtra("dateType", dType);
						intent.putExtra("planId", planId);
						int userId = SharedPreferencesUtil.getInstance(WorkPlanNewPlanActivity.this).getUserId();
						intent.putExtra("userId", userId + "");
						WorkPlanNewPlanActivity.this.startActivity(intent);

					}
				}).setNegativeButton(R.string.Cancle, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						createDate="";
						startDate ="";
						endDate="";
						
					}
				}).setCancelable(false).create();

		alertDialog.show();
	}

	private RequestParams searchParams() {
		String userId = "";
		if (TextUtils.isEmpty(userId)) {
			userId = String.valueOf(SharedPreferencesUtil.getInstance(this).getUserId());
		}
		RequestParams param = new RequestParams();
		param.put("phoneno", PublicUtils.receivePhoneNO(getApplicationContext()));
		param.put("userId", userId);
		param.put("type", planType);
		if (planType == 3) {
			param.put("fromTime", mYear);
		} else {
			int month = mMonth;

			if (month < 10) {
				param.put("fromTime", mYear + "-0" + month);
			} else {
				param.put("fromTime", mYear + "-" + month);
			}

		}

		return param;
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

	}

}
