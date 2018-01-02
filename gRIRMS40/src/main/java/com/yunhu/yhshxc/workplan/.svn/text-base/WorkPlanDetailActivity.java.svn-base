package com.yunhu.yhshxc.workplan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.yunhu.yhshxc.workplan.AssessItemView.AssessSubmitSucessListener;
import com.yunhu.yhshxc.workplan.bo.Assess;
import com.yunhu.yhshxc.workplan.bo.PlanData;
import com.yunhu.yhshxc.workplan.bo.WorkPlanModle;
import com.yunhu.yhshxc.workplan.db.AssessDB;
import com.yunhu.yhshxc.workplan.db.PlanDataDB;
import com.yunhu.yhshxc.workplan.db.WorkPlanModleDB;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import gcg.org.debug.JLog;
import view.DateSelectorView;
import view.DateSelectorView.OnDateClickListener;
import view.NumberTextView;

import static com.yunhu.yhshxc.application.SoftApplication.context;

public class WorkPlanDetailActivity extends Activity {
	private ListView mListView;// 计划详情列表
	private WorkPlanDetailAdapter detailAdapter;// 计划详情适配器
	private LinearLayout workplan_detail_container;// 计划数据容器
	private LinearLayout workplan_Assess_container;// 评价数据容器
	private Button workplan_detail_addplan;// 添加工作计划
	private Button workplan_detail_submit;// 提交数据按钮
	private ImageView back;// 返回
	private TextView title;// 标题
	private String dateType;// 日期类型
	private static int planType = 0;// 提交需要的类型标识1:日报 2:周报3:月报
	private String userId;// 用户的id
	private int planId;// 计划的id
	private DateSelectorView dateView;// 日期选择控件
	private Map<Long, WorkPlanModle> subMitPlans;// 存储当前所选时段所有数据,只在编辑更改的情况下调用
	private static boolean isCanSubmit = false;// 修改数据后才能提交数据
	private String createDate;// 计划时间开始日期(默认为显示内容的时间段)
	private String startDate;// 计划开始日期
	private String endDate;// 计划结束日期
	// 点击日期控件查询参数
	private int mMonth;
	private int mYear;
	private int mDay;

	//初始化偏移量
	private int offset = 0;
	private int scrollViewWidth = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_workplan_monthdetail);
		dateView = (DateSelectorView) findViewById(R.id.workplan_detail_dateview);
		mListView = (ListView) findViewById(R.id.lv_by_detail);
		back = (ImageView) findViewById(R.id.workplan_detail_back);
		title = (TextView) findViewById(R.id.workplan_detail_title);
		workplan_detail_container = (LinearLayout) findViewById(R.id.workplan_detail_container);
		workplan_Assess_container = (LinearLayout) findViewById(R.id.workplan_Assess_container);
		workplan_detail_submit = (Button) findViewById(R.id.workplan_detail_submit);
		workplan_detail_addplan = (Button) findViewById(R.id.workplan_detail_addplan);
		Intent intent = getIntent();
		dateType = intent.getStringExtra("dateType");
		planId = intent.getIntExtra("planId", 0);// 计划ID
		String sD = new PlanDataDB(this).getStartDate(planId);
		dateView.setSpecifiedDate(sD, dateType);
		dateView.setDateCompare(false);
		userId = intent.getStringExtra("userId");// 用户id
		subMitPlans = new HashMap<Long, WorkPlanModle>();
		// 根据类型设置标题.初始化日期
		dateView.setDateType(dateType);
		getArrayForCirclePoint();//初始化小红点
		
		
		if (dateType.equals(DateSelectorView.DATE_TYPE_MONTH)) {
			title.setText(R.string.work_plan_c3);
			planType = 3;
		} else if (dateType.equals(DateSelectorView.DATE_TYPE_WEEK)) {
			title.setText(R.string.work_plan_c2);
			planType = 2;
		} else if (dateType.equals(DateSelectorView.DATE_TYPE_DAY)) {
			title.setText(R.string.work_plan_c1);
			planType = 1;
		}
		// 根据数据的useid判断是否是自己的数据
		if (Integer.parseInt(userId) == SharedPreferencesUtil.getInstance(this).getUserId()) {// 自己的数据
			initDataSelf();
		} else {// 别人的数据
			initDataOther();
		}
		// 如果是自己的根据数据的日期和类型判断是否可进行编辑
		// 如果是别人的,则不可编辑,就判断是否可进行评价(比对权限),如果可评价就显示评价内容框,并显示提交评价的提交按钮
		// 查询添加数据到list列表 ,包括评价
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				WorkPlanDetailActivity.this.finish();

			}
		});

		dateView.setOnDateClickListener(new OnDateClickListener() {// 选择日期的结果回调,普通日期是

			@Override
			public void onDateSelected(String date) {
				Calendar calendar = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				if (!TextUtils.isEmpty(date)) {
					// Toast.makeText(WorkPlanDetailActivity.this, "选择时间:" +
					// date, Toast.LENGTH_SHORT).show();
					if (dateType.equals(DateSelectorView.DATE_TYPE_DAY)) {// 日计划类型
						createDate = date;// 开始的日期
						startDate = date;// 计划开始为选择时间
						endDate = date;// 计划结束也为选择的时间

					} else if (dateType.equals(DateSelectorView.DATE_TYPE_WEEK)) {// 周计划类型
						String[] dates = date.split("/");
						createDate = dates[0];// 开始的日期
						startDate = dates[0];
						endDate = dates[1];

					} else if (dateType.equals(DateSelectorView.DATE_TYPE_MONTH)) {// 月计划类型
						createDate = date + "-" + "01";// 开始的日期
						startDate = date + "-" + "01";
						endDate = date + "-" + DateUtil.getMonthDays(date);
					}
				}

				// 初始化查询的日期参数
				try {
					Date d = sdf.parse(startDate);
					calendar.setTime(d);
					mYear = calendar.get(Calendar.YEAR);
					mMonth = calendar.get(Calendar.MONTH) + 1;
					mDay = calendar.get(Calendar.DAY_OF_MONTH);
				} catch (ParseException e) {

					e.printStackTrace();
				}

				// 重新联网查询数据,清空数据库,重新写入,重新加载当前页面
				search();

			}
		});

	}

	/**
	 * 初始化他人数据
	 */
	private void initDataOther() {

		// 去WORK_PLAN_MODLE表里查询数据
		WorkPlanModleDB woDb = new WorkPlanModleDB(this);
		// 根据planid查询出别人的的数据
		List<WorkPlanModle> list = woDb.checkPlansByPlanId(planId);
		for (int i = 0; i < list.size(); i++) {
			CreateNewPlanItem othreItem = new CreateNewPlanItem(this);
			othreItem.setSaveViewHidden(false);
			othreItem.getTextNumber().setVisibility(View.VISIBLE);
			othreItem.setAllSaveInfo(list.get(i));
			workplan_detail_container.addView(othreItem.getView());

		}
		// 展示数据,展示已存在的评价
		boolean isCanAssess = true;
		final String useId = SharedPreferencesUtil.getInstance(WorkPlanDetailActivity.this).getUserId() + "";
		final String userName = SharedPreferencesUtil.getInstance(WorkPlanDetailActivity.this).getUserName();
		AssessDB asDb = new AssessDB(this);
		List<Assess> asList = asDb.checkAllAssessByPlanId(planId);
		for (int i = 0; i < asList.size(); i++) {
			Assess asItem = asList.get(i);
			AssessItemView asView = new AssessItemView(this);
			asView.setPlanId(planId);
			asView.initData(asItem);
			if (useId.equals(asItem.getUser_id())) {// 已经评论过了,不能再评论
				isCanAssess = false;
			}

			workplan_Assess_container.addView(asView.getView());
		}
		// 根据userCode判断自己是否有权限评价,如果有就创建一个可编辑的评价框
		PlanDataDB pDb = new PlanDataDB(this);
		String userCode = pDb.getUserCodeByPlanId(planId);
		String mCode = SharedPreferencesUtil.getInstance(this).getUserId() + "";
		if (userCode.contains(mCode) && isCanAssess) {// 有权限评价,并且未评价过
			final AssessItemView etView = new AssessItemView(this);
			etView.setPlanId(planId);
			etView.setAssessSubmitListener(new AssessSubmitSucessListener() {

				@Override
				public void onSubmitSucess() {// 提交评论成功

					// 将此评论也添加至评论列表中
					Assess ass = etView.getAssessData(planId, useId, userName);
					AssessItemView newView = new AssessItemView(WorkPlanDetailActivity.this);
					newView.setPlanId(planId);
					newView.initData(ass);
					workplan_Assess_container.removeView(etView.getView());
					workplan_Assess_container.addView(newView.getView());
				}
			});
			workplan_Assess_container.addView(etView.getView());

		}

	}

	/**
	 * 初始化个人数据
	 */
	private void initDataSelf() {
		// 去WORK_PLAN_MODLE表里查询数据
		WorkPlanModleDB woDb = new WorkPlanModleDB(this);
		PlanDataDB plDb = new PlanDataDB(this);
		List<WorkPlanModle> list = woDb.checkPlansByPlanId(planId);
		// 根据plan_id查询出一条PlanData
		PlanData plData = plDb.checkPlanDataById(planId);

		// 初始化当前时间段的日期
		createDate = plData.getPlanData();
		startDate = plData.getStartDate();
		endDate = plData.getEndDate();
		// 根据planId对应日期判断是否可以编辑
		String stDate = plData.getPlanData();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 如果计划开始时间在当前时间后(月,周,日),则为可编辑(月计划的开始时间为每个月的1号,所以只比较大小就可)
		boolean isCanEdit = false;
		try {
			Date startD = sdf.parse(stDate);
			isCanEdit = startD.after(new Date(System.currentTimeMillis()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 如果可以编辑,则显示添加一条计划和提交按钮,提交按钮数据集合为空的情况下提示
		if (isCanEdit) {

			workplan_detail_addplan.setVisibility(View.VISIBLE);// 显示添加按钮
			workplan_detail_submit.setVisibility(View.VISIBLE);// 显示提交按钮
			workplan_detail_addplan.setOnClickListener(new OnClickListener() {// 添加一条计划

				@Override
				public void onClick(View v) {
					addOneNewPlan();
				}
			});

			workplan_detail_submit.setOnClickListener(new OnClickListener() {// 提交当前计划

				@Override
				public void onClick(View v) {
				if (subMitPlans.size()<=0) {//清除了数据,弹出提示,
					//弹出提示框,是否确定删除
					AlertDialog dialog = new AlertDialog.Builder(WorkPlanDetailActivity.this, R.style.NewAlertDialogStyle)
							.setTitle(R.string.tip)
							.setMessage(R.string.work_plan_c19)
							.setPositiveButton(R.string.Confirm, new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									subMitAllDataToService();
								}
							})
							.setNegativeButton(R.string.Cancle, null)
							.create();
					dialog.show();	
					
					}else{
						
						subMitAllDataToService();
					}
				}
			});

		} else {
			workplan_detail_addplan.setVisibility(View.INVISIBLE);
			workplan_detail_submit.setVisibility(View.INVISIBLE);
		}
		// 遍历数据添加到容器中
		for (int i = 0; i < list.size(); i++) {
			WorkPlanModle modle = list.get(i);
			modle.setPlanSort(i+1);
			final CreateNewPlanItem item = new CreateNewPlanItem(this);
			item.getTextNumber().setVisibility(View.VISIBLE);
			item.setSaveViewHidden(false);// 只显示内容框
			item.setItemId(System.currentTimeMillis());
			// 先把所有加载的数据存入提交集合,配合是否可提交标识进行提交
			subMitPlans.put(item.getItemId(), modle);
			if (isCanEdit) {// 如果可编辑则显示编辑按钮
				item.setEditClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 展示编辑框,隐藏保存框,把编辑框内容赋给保存框
						item.setSaveViewHidden(true);
						item.setAllEditInfo(item.getAllSaveData());

					}
				});
			}
			item.setDeleteClickListener(new OnClickListener() {// 保存框删除按钮

				@Override
				public void onClick(View v) {
					
					//弹出提示框,是否确定删除
					AlertDialog dialog = new AlertDialog.Builder(WorkPlanDetailActivity.this, R.style.NewAlertDialogStyle)
							.setTitle(R.string.tip)
							.setMessage(R.string.work_plan_c11)
							.setPositiveButton(R.string.Confirm, new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									// 删除一条数据,从集合中删除此条
									if (subMitPlans.containsKey(item.getItemId())) {
										subMitPlans.remove(item.getItemId());
										// // 更改提交按钮状态
										 isCanSubmit = true;
									}
									// 删除后隐藏该条目
									item.hiddenAllView(true);
								}
							})
							.setNegativeButton(R.string.Cancle, null)
							.create();
					dialog.show();
					
					

				}
			});
			item.setSaveClickListener(new OnClickListener() {// 保存框保存按钮

				@Override
				public void onClick(View v) {
					// 清空原先存储的数据,isCanSubmit置为false,更换日期时
					// subMitPlans.clear();
					// 更改提交按钮状态
					isCanSubmit = true;
					// 把更改后的数据保存至集合subMitPlans,
					WorkPlanModle modle = item.getAllEditData();
					subMitPlans.put(item.getItemId(), modle);// 替换原有的数据
					// 编辑框变为保存框,内容转移
					item.setSaveViewHidden(false);
					item.setAllSaveInfo(modle);

				}
			});
			item.setAllSaveInfo(modle);
			workplan_detail_container.addView(item.getView());
		}

		// 查询评价表获取评价内容添加进去,只能展示
		AssessDB asDb = new AssessDB(this);
		List<Assess> asList = asDb.checkAllAssessByPlanId(planId);
		for (int i = 0; i < asList.size(); i++) {
			AssessItemView asView = new AssessItemView(this);
			asView.setPlanId(planId);
			asView.initData(asList.get(i));
			workplan_Assess_container.addView(asView.getView());
		}

	}

	/**
	 * 遍历数据集合,并且检查是否可提交isCanSubmit
	 */
	private void subMitAllDataToService() {
		if (!isCanSubmit) {
			Toast.makeText(this, R.string.work_plan_c17, Toast.LENGTH_SHORT).show();
			return;
		}
//		// 先判断集合是否有要提交的数据
//		if (subMitPlans.size() == 0) {// 没有数据可提交
//			Toast.makeText(this, "没有计划可提交,请确认是否保存了计划", Toast.LENGTH_SHORT).show();
//			return;
//		}
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
			//计划id
			jsObject.put("plan_id", planId);
			//userId
//			jsObject.put("userId", SharedPreferencesUtil.getInstance(this).getUserId()+"");
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
		for (Map.Entry<Long, WorkPlanModle> entry : subMitPlans.entrySet()) {// 遍历出每个计划添加到json数组中
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
//			if (arr.length()==0) {
//				jsObject.put("detail", arr.toString());
//			}else{				
				jsObject.put("detail", arr);
//			}
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
						Toast.makeText(WorkPlanDetailActivity.this, R.string.submit_ok, Toast.LENGTH_SHORT).show();
						isCanSubmit = false;// 提交过以后不需要再次提交
						//清除数据库数据,重新加载
						// 初始化查询的日期参数
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						Calendar calendar = Calendar.getInstance(Locale.CHINA);
						try {
							Date d = sdf.parse(startDate);
							calendar.setTime(d);
							mYear = calendar.get(Calendar.YEAR);
							mMonth = calendar.get(Calendar.MONTH) + 1;
							mDay = calendar.get(Calendar.DAY_OF_MONTH);
						} catch (ParseException e) {

							e.printStackTrace();
						}

						search();
						// subMitPlans.clear();
						// WorkPlanDetailActivity.this.finish();
					} else {
						Toast.makeText(WorkPlanDetailActivity.this, R.string.work_plan_c14, Toast.LENGTH_SHORT).show();
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
				Toast.makeText(WorkPlanDetailActivity.this, R.string.work_plan_c13, Toast.LENGTH_SHORT).show();

			}
		});

	}

	/**
	 * 添加一条新计划
	 */
	private void addOneNewPlan() {
		final CreateNewPlanItem item = new CreateNewPlanItem(this);
		item.setSaveViewHidden(true);// 隐藏保存框
		item.setItemId(System.currentTimeMillis());// 以当前时间戳其唯一标识记录
		item.setDeleteClickListener(new OnClickListener() {// 设置删除按钮监听

			@Override
			public void onClick(View v) {
				// 先删除集合中的数据
				if (subMitPlans.containsKey(item.getItemId())) {
					subMitPlans.remove(item.getItemId());
					isCanSubmit = true;// 可以提交数据
				}
				// 删除view条目
				workplan_detail_container.removeView(item.getView());

			}
		});

		item.setSaveClickListener(new OnClickListener() {// 保存按钮的监听

			@Override
			public void onClick(View v) {
				if (item.isDataEmpty()) {
					Toast.makeText(WorkPlanDetailActivity.this, R.string.work_plan_c10, Toast.LENGTH_SHORT).show();
				} else {
					WorkPlanModle modle = item.getAllEditData();
					
					subMitPlans.put(item.getItemId(), modle);// key唯一,如果再次编辑保存,直接替换掉原来的
					item.setSaveViewHidden(false);
					item.setAllSaveInfo(modle);
					isCanSubmit = true;// 可以提交数据
				}

			}
		});
		item.setEditClickListener(new OnClickListener() {// 编辑按钮的监听

			@Override
			public void onClick(View v) {
				WorkPlanModle modle = item.getAllSaveData();
				item.setSaveViewHidden(true);
				item.setAllEditInfo(modle);

			}
		});

		// itemList.add(item);// 添加到view集合,用于操作
		workplan_detail_container.addView(item.getView());

	}

	/**
	 * 
	 * 工作计划详情页面的适配器
	 *
	 */
	class WorkPlanDetailAdapter extends BaseAdapter {
		private Context mContext;
		private LayoutInflater inflater;
		private List<WorkPlanModle> dataList;
		private boolean isCanEdit = false;

		public WorkPlanDetailAdapter(Context context, List<WorkPlanModle> dataList, boolean flag) {
			this.mContext = context;
			this.dataList = dataList;
			this.isCanEdit = flag;
			this.inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return dataList.size();
		}

		@Override
		public WorkPlanModle getItem(int position) {
			// TODO Auto-generated method stub
			return dataList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				final CreateNewPlanItem item = new CreateNewPlanItem(mContext);
				item.setSaveViewHidden(false);// 只显示内容框
				item.setItemId(System.currentTimeMillis());
				// 先把所有加载的数据存入提交集合,配合是否可提交标识进行提交
				subMitPlans.put(item.getItemId(), dataList.get(position));
				if (isCanEdit) {// 如果可编辑则显示编辑按钮
					item.setEditClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// 展示编辑框,隐藏保存框,把编辑框内容赋给保存框
							item.setSaveViewHidden(true);
							item.setAllEditInfo(item.getAllSaveData());

						}
					});
				}
				item.setDeleteClickListener(new OnClickListener() {// 保存框删除按钮

					@Override
					public void onClick(View v) {
						// 删除一条数据,从集合中删除此条
						if (subMitPlans.containsKey(item.getItemId())) {
							subMitPlans.remove(item.getItemId());
						}
						// 删除后隐藏该条目
						item.hiddenAllView(true);
					}
				});
				item.setSaveClickListener(new OnClickListener() {// 保存框保存按钮

					@Override
					public void onClick(View v) {
						// //清空原先存储的数据,isCanSubmit置为false,更换日期时
						// subMitPlans.clear();
						// 更改提交按钮状态
						isCanSubmit = true;
						// 把更改后的数据保存至集合subMitPlans,
						WorkPlanModle modle = item.getAllEditData();
						subMitPlans.put(item.getItemId(), modle);// 替换原有的数据
						// 编辑框变为保存框,内容转移
						item.setSaveViewHidden(false);
						item.setAllSaveInfo(modle);

					}
				});
				item.setAllSaveInfo(dataList.get(position));
				convertView = item.getView();
			}

			return convertView;
		}

	}

	// -------------------------------------------------------------

	private MyProgressDialog searchDialog;

	private void search() {
		// 先清空数据存储的上次查询数据
		clearDatabaseData();
		subMitPlans.clear();
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
				Log.e("view", "onSuccess:" + content);
				// 解析加载下来的数据
				try {
					JSONObject obj = new JSONObject(content);
					String resultCode = obj.getString("resultcode");
					if ("0000".equals(resultCode)) {
						WorkPlanUtils util = new WorkPlanUtils(WorkPlanDetailActivity.this);
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
						PlanDataDB planDataDB = new PlanDataDB(WorkPlanDetailActivity.this);
						//如果点击日期后,每次查询完,planid应该发生改变,需要根据查询的开始日期去表中查找
						planId = planDataDB.getWorkPlanIdByStartDate(startDate);
						

					} else {
						throw new Exception();
					}
				} catch (Exception e) {
					e.printStackTrace();
					ToastOrder.makeText(getApplicationContext(), R.string.ERROR_DATA, ToastOrder.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				JLog.d("alin", "onFailure:" + content);
				ToastOrder.makeText(getApplicationContext(), R.string.retry_net_exception, ToastOrder.LENGTH_SHORT).show();
			}

			@Override
			public void onFinish() {
				if (searchDialog != null && searchDialog.isShowing()) {
					searchDialog.dismiss();
				}

				// 解析并放入数据库以后,从数据库中读取并展示出来
				// 根据所选日期去数据库查询对应的planId,如果没有数据,则提示
				int pl_id = new PlanDataDB(WorkPlanDetailActivity.this).getWorkPlanIdByStartDate(startDate);
				if (pl_id > 0) {// 说明数据库有数据
					planId = pl_id;
					// 根据数据的useid判断是否是自己的数据
					if (Integer.parseInt(userId) == SharedPreferencesUtil.getInstance(WorkPlanDetailActivity.this)
							.getUserId()) {// 自己的数据
						initDataSelf();
					} else {// 别人的数据
						initDataOther();
					}
					getArrayForCirclePoint();//初始化小红点
				} else {
					Toast.makeText(WorkPlanDetailActivity.this, R.string.work_plan_c18, Toast.LENGTH_SHORT).show();
				}

			}

		});

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
		workplan_detail_container.removeAllViews();
		workplan_Assess_container.removeAllViews();

	}

	private RequestParams searchParams() {
		int type = 0;
		if (dateType.equalsIgnoreCase(DateSelectorView.DATE_TYPE_MONTH)) {
			type = 3;
		} else if (dateType.equalsIgnoreCase(DateSelectorView.DATE_TYPE_WEEK)) {
			type = 2;
		} else if (dateType.equalsIgnoreCase(DateSelectorView.DATE_TYPE_DAY)) {
			type = 1;
		}
		if (TextUtils.isEmpty(userId)) {
			userId = String.valueOf(SharedPreferencesUtil.getInstance(this).getUserId());
		}
		RequestParams param = new RequestParams();
		param.put("phoneno", PublicUtils.receivePhoneNO(getApplicationContext()));
		param.put("userId", userId);
		param.put("type", type);
		if (type == 3) {
			param.put("fromTime", mYear);
		} else {
			String m = mMonth + "";
			if (mMonth < 10) {
				m = "0" + mMonth;
			}
			param.put("fromTime", mYear + "-" + m);
		}

		return param;
	}

	/**
	 * 获取判断小红点显示的数组
	 */
	public void getArrayForCirclePoint() {
		// 查询所有plandata
		List<String> poins = new ArrayList<String>();
		PlanDataDB plDb = new PlanDataDB(this);
		List<PlanData> datas = plDb.findPlanDataListByDate();
		if (dateType.equals(DateSelectorView.DATE_TYPE_DAY)) {
			for (int i = 0; i < datas.size(); i++) {
				String[] dayPlan = datas.get(i).getPlanData().split("-");
				int day = Integer.parseInt(dayPlan[2]);
				poins.add(day+"");
			}
						
		}else if (dateType.equals(DateSelectorView.DATE_TYPE_WEEK)) {
			for (int i = 0; i < datas.size(); i++) {
				String[] weekPlans = datas.get(i).getPlanData().split("-");
				String[] weekPlane = datas.get(i).getEndDate().split("-");			
				int weekS = Integer.parseInt(weekPlans[2]);
				int weekE = Integer.parseInt(weekPlane[2]);
				poins.add(weekS+"~"+weekE);
			}
			
			
			
		}else if (dateType.equals(DateSelectorView.DATE_TYPE_MONTH)) {
			for (int i = 0; i < datas.size(); i++) {
				String[] dayPlan = datas.get(i).getPlanData().split("-");
				int month = Integer.parseInt(dayPlan[1]);
				poins.add(month+"");
			}
			
			
		}
		
		LinearLayout container= dateView.getContainer();
		
		for (int i = 0; i < container.getChildCount(); i++) {
			NumberTextView nt= (NumberTextView)container.getChildAt(i);
			for (int j = 0; j < poins.size(); j++) {
				if (nt.getText().equals(poins.get(j))) {
					nt.showPointCircle(true);
				}
//				else{
//					nt.showPointCircle(false);
//				}
			}
		}
		
		

	}
}
