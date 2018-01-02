package com.yunhu.yhshxc.workplan;

import gcg.org.debug.JLog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import view.DatePickerYMDialog;
import view.DateSelectorView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.bo.OrgUser;
import com.yunhu.yhshxc.database.OrgUserDB;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.DropDown;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.yunhu.yhshxc.widget.DropDown.OnFuzzyQueryListener;
import com.yunhu.yhshxc.widget.DropDown.OnResultListener;
import com.yunhu.yhshxc.workplan.bo.Assess;
import com.yunhu.yhshxc.workplan.bo.PlanData;
import com.yunhu.yhshxc.workplan.bo.WorkPlanModle;
import com.yunhu.yhshxc.workplan.db.AssessDB;
import com.yunhu.yhshxc.workplan.db.PlanDataDB;
import com.yunhu.yhshxc.workplan.db.WorkPlanModleDB;
import com.loopj.android.http.RequestParams;

/**
 * 工作计划模块首页面 默认为月计划,月计划时,中间时间段为年 周计划是中间时间段为年和月(使用重写的日期控件来选择,控制其显示年或者月) 日计划时也是年和月
 * 我的计划点击弹出搜索框搜索其他人
 *
 */
public class WorkPlanActivity extends Activity implements OnClickListener, OnFuzzyQueryListener {
	private static final int DATE_TYPE_MONTH = 1;
	private static final int DATE_TYPE_WEEK = 2;
	private static final int DATE_TYPE_DAY = 3;
	private static String currentType = DateSelectorView.DATE_TYPE_MONTH;// 当前页面展示的类型
	private ImageView workplan_back;// 返回
	private TextView workplan_add;// 添加计划
	private Button workplan_type;// 选择月计划,周计划,日计划
	private Button workplan_date;// 对应日期
	private DropDown workplan_searchplan;// 用户选择控件
	private SimpleAdapter typeAdapter;

	private int[] typeArr = new int[] { R.string.month_plan, R.string.week_plan, R.string.day_plan };
	private List<String> typeList;// 类型数据集合

	private List<OrgUser> allUser;// 所有用户
	public String userId;
	private ListView lvPlanItem;
	private List<PlanData> planList = new ArrayList<PlanData>();// 工作计划展示列表
	private MonthListAdapter adapter;

	private int mMonth;
	private int mYear;
	private int mDay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_workplan);
		initView();
		initData();

	}

	private void initView() {
		workplan_back = (ImageView) findViewById(R.id.workplan_back);
		workplan_add = (TextView) findViewById(R.id.workplan_add);
		workplan_type = (Button) findViewById(R.id.workplan_type);
		workplan_date = (Button) findViewById(R.id.workplan_date);
		workplan_date.setOnClickListener(this);
		workplan_searchplan = (DropDown) findViewById(R.id.workplan_searchplan);
		workplan_searchplan.setOnFuzzyQueryListener(this);
		workplan_searchplan.setOnResultListener(resultListener);
		lvPlanItem = (ListView) findViewById(R.id.lvTrace);
		lvPlanItem.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
				// Toast.makeText(WorkPlanActivity.this,
				// planList.get(position).getPlanTitle(),
				// Toast.LENGTH_SHORT).show();
				// 根据点击跳转进入相应计划的详情页面
				Intent intent = new Intent(WorkPlanActivity.this, WorkPlanDetailActivity.class);
				intent.putExtra("dateType", currentType);
				int planId = planList.get(position).getPlanId();
				intent.putExtra("userId", userId);
				intent.putExtra("planId", planId);
				WorkPlanActivity.this.startActivity(intent);

			}
		});
		workplan_back.setOnClickListener(this);
		workplan_add.setOnClickListener(this);
         
	}

	private void initData() {
		// 计划类型数据
		if (TextUtils.isEmpty(userId)) {
			userId = String.valueOf(SharedPreferencesUtil.getInstance(this).getUserId());
		}
		typeList = new ArrayList<String>();

		for (int i = 0; i < typeArr.length; i++) {
			typeList.add(PublicUtils.getResourceString(this,typeArr[i]));
		}

		// 选择为我的计划
		Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		workplan_date.setText(mYear + PublicUtils.getResourceString(WorkPlanActivity.this,R.string.year));

		// typeAdapter = new WorkPlanAdapter(this, typeList);
		// SimpleAdapter(Context context, List<? extends Map<String, ?>> data,
		// int resource, String[] from, int[] to)
		// typeAdapter = new
		// SimpleAdapter(this,typeList,R.layout.spinner_item_defined,R.id.spinner_text_show,);
		// *******************************************
		workplan_type.setText(R.string.month_plan);
		workplan_type.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				View view = LayoutInflater.from(WorkPlanActivity.this).inflate(R.layout.spinner_item_defined, null);
				Button btn_ri = (Button) view.findViewById(R.id.btn_ri);
				Button btn_zhou = (Button) view.findViewById(R.id.btn_zhou);
				Button btn_yue = (Button) view.findViewById(R.id.btn_yue);
				final android.support.v7.app.AlertDialog dia = new android.support.v7.app.AlertDialog.Builder(
						WorkPlanActivity.this, R.style.NewAlertDialogStyle).setView(view).create();
				dia.show();

				btn_ri.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Calendar c = Calendar.getInstance();
						mYear = c.get(Calendar.YEAR);
						mMonth = c.get(Calendar.MONTH);
						mDay = c.get(Calendar.DAY_OF_MONTH);
						currentType = DateSelectorView.DATE_TYPE_DAY;
						workplan_date.setText(mYear + PublicUtils.getResourceString(WorkPlanActivity.this,R.string.year) + (mMonth + 1) + PublicUtils.getResourceString(WorkPlanActivity.this,R.string.month));
						workplan_type.setText(R.string.day_plan);
						if (dia.isShowing()) {
							dia.dismiss();
						}
						search();

					}
				});
				btn_zhou.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Calendar c = Calendar.getInstance();
						mYear = c.get(Calendar.YEAR);
						mMonth = c.get(Calendar.MONTH);
						mDay = c.get(Calendar.DAY_OF_MONTH);
						currentType = DateSelectorView.DATE_TYPE_WEEK;
						workplan_date.setText(mYear + PublicUtils.getResourceString(WorkPlanActivity.this,R.string.year) + (mMonth + 1) + PublicUtils.getResourceString(WorkPlanActivity.this,R.string.month));
						workplan_type.setText(R.string.week_plan);
						if (dia.isShowing()) {
							dia.dismiss();
						}
						search();
					}
				});
				btn_yue.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Calendar c = Calendar.getInstance();
						mYear = c.get(Calendar.YEAR);
						mMonth = c.get(Calendar.MONTH);
						mDay = c.get(Calendar.DAY_OF_MONTH);
						currentType = DateSelectorView.DATE_TYPE_MONTH;
						workplan_date.setText(mYear + PublicUtils.getResourceString(WorkPlanActivity.this,R.string.year));
						workplan_type.setText(R.string.month_plan);
						if (dia.isShowing()) {
							dia.dismiss();
						}
						search();
					}
				});

			}
		});

		// *******************************************

		// 设置下拉列表的风格,simple_spinner_dropdown_item是android系统自带的样式，等会自定义修改
		// typeAdapter.setDropDownViewResource(R.layout.spinner_item_defined);
		// workplan_type.setAdapter(typeAdapter);
		// workplan_type.setSelection(0);
		// workplan_type.setBackgroundColor(getResources().getColor(R.color.white));
		// workplan_type.setOnItemSelectedListener(new OnItemSelectedListener()
		// {
		//
		// @Override
		// public void onItemSelected(AdapterView<?> parent, View view, int
		// position, long id) {
		// if (position == 0) {// 月计划
		// currentType = DateSelectorView.DATE_TYPE_MONTH;
		// workplan_date.setText(mYear + "年");
		// } else if (position == 1) {// 周计划
		// currentType = DateSelectorView.DATE_TYPE_WEEK;
		// workplan_date.setText(mYear + "年" + (mMonth + 1) + "月");
		// } else if (position == 2) {// 日计划
		// currentType = DateSelectorView.DATE_TYPE_DAY;
		// workplan_date.setText(mYear + "年" + (mMonth + 1) + "月");
		// }
		// TextView tv = (TextView) view;
		// tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
		// search();
		//
		// }
		//
		// @Override
		// public void onNothingSelected(AdapterView<?> parent) {
		// // TODO Auto-generated method stub
		//
		// }
		// });

		// 默认为月计划
		// initDateList(DATE_TYPE_MONTH);
		initUserData(null);
		search();
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
		// 清除完成以后listview需要重新加载
		planList.clear();
		adapter = new MonthListAdapter(this, planList);
		lvPlanItem.setAdapter(adapter);
	}

	List<Dictionary> srcList = new ArrayList<Dictionary>();

	private void initUserData(String fuzzy) {
		// allUser = new OrgUserDB(this).findAllOrgUserList(fuzzy);
		// List<Dictionary> srcList = new ArrayList<Dictionary>();
		// if (allUser != null && allUser.size() > 0) {
		// OrgUser user = null;
		// Dictionary dic = null;
		//
		// for (int i = 0; i < allUser.size(); i++) {
		// user = allUser.get(i);
		// dic = new Dictionary();
		// dic.setDid(String.valueOf(user.getUserId()));
		// if
		// (user.getUserId().equals(SharedPreferencesUtil.getInstance(WorkPlanActivity.this).getUserId()))
		// {
		// dic.setCtrlCol("我的计划");
		// srcList.add(0, dic);
		// } else {
		// dic.setCtrlCol(user.getUserName());
		// srcList.add(dic);
		// }
		//
		// }
		// }
		srcList = workPlanUserData(WorkPlanActivity.this, fuzzy);
		workplan_searchplan.setSrcDictList(srcList);
		initStoreSelect(srcList);
	}

	/**
	 * 设置选中的用户
	 * 
	 * @param srcList
	 *            所有用户
	 */
	private void initStoreSelect(List<Dictionary> srcList) {
		if (!TextUtils.isEmpty(userId) && srcList.size() > 0) {
			for (int i = 0; i < srcList.size(); i++) {
				Dictionary dic = srcList.get(i);
				if (userId.equals(dic.getDid())) {
					workplan_searchplan.setSelected(dic);
					break;
				}
			}
		}
	}

	/**
	 * 初始化日期显示类型
	 */
	private void initDateList(int dateType) {
		Calendar calendar = Calendar.getInstance();

		switch (dateType) {
		case DATE_TYPE_MONTH:// 月份日期
			// search();// 初始化查询数据
			break;
		case DATE_TYPE_WEEK:// 周日期

			break;
		case DATE_TYPE_DAY:// 当天日期

			break;

		}

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.workplan_back:// 返回
			this.finish();
			break;

		case R.id.workplan_add:// 新建工作计划
			// 弹出提示框,让用户明确选择要建立的计划类型
			showDialogSelectType();
			break;
		case R.id.workplan_date:
			initDateDialog();
			break;
		default:
			break;
		}

	}

	/**
	 * 用户选择建立的计划类型
	 */
	private void showDialogSelectType() {
		String[] types = new String[] { PublicUtils.getResourceString(this,R.string.month_plan), PublicUtils.getResourceString(this,R.string.week_plan), PublicUtils.getResourceString(this,R.string.day_plan) };
		// 跳转
		final Intent intent = new Intent(this, WorkPlanNewPlanActivity.class);
		AlertDialog dialog = new AlertDialog.Builder(this, R.style.AlertDialog_AppColor).setTitle(R.string.work_plan_c25)
				.setItems(types, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:// 月计划
							intent.putExtra("currentType", DateSelectorView.DATE_TYPE_MONTH);
							startActivity(intent);
							break;
						case 1:// 周计划
							intent.putExtra("currentType", DateSelectorView.DATE_TYPE_WEEK);
							startActivity(intent);
							break;
						case 2:// 日计划
							intent.putExtra("currentType", DateSelectorView.DATE_TYPE_DAY);
							startActivity(intent);
							break;

						default:
							break;
						}

					}
				}).create();

		dialog.show();

	}

	private void initDateDialog() {
		switch (currentType) {
		case DateSelectorView.DATE_TYPE_MONTH:
			new DatePickerYMDialog(WorkPlanActivity.this, new OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					workplan_date.setText(year + PublicUtils.getResourceString(WorkPlanActivity.this,R.string.year));
					mYear = year;
					search();
				}
			}, mYear, mMonth, mDay, true, true).show();
			break;
		case DateSelectorView.DATE_TYPE_DAY:
			new DatePickerYMDialog(WorkPlanActivity.this, new OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					workplan_date.setText(year + PublicUtils.getResourceString(WorkPlanActivity.this,R.string.year) + (monthOfYear + 1) + PublicUtils.getResourceString(WorkPlanActivity.this,R.string.month));
					mYear = year;
					mMonth = monthOfYear;
					search();
				}
			}, mYear, mMonth, mDay, true, false).show();
			break;
		case DateSelectorView.DATE_TYPE_WEEK:
			new DatePickerYMDialog(WorkPlanActivity.this, new OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					workplan_date.setText(+year + PublicUtils.getResourceString(WorkPlanActivity.this,R.string.year) + (monthOfYear + 1) + PublicUtils.getResourceString(WorkPlanActivity.this,R.string.month));
					mYear = year;
					mMonth = monthOfYear;
					search();
				}
			}, mYear, mMonth, mDay, true, false).show();
			break;

		default:
			break;
		}
	}

	@Override
	public void onTextChanged(CharSequence s) {
		initUserData(s.toString());
	}

	private OnResultListener resultListener = new OnResultListener() {

		@Override
		public void onResult(List<Dictionary> result) {
			if (result != null && !result.isEmpty()) {
				Dictionary dic = result.get(0);
				userId = dic.getDid();
				search();
			}

		}
	};

	private RequestParams searchParams() {
		int type = 0;
		if (currentType.equalsIgnoreCase(DateSelectorView.DATE_TYPE_MONTH)) {
			type = 3;
		} else if (currentType.equalsIgnoreCase(DateSelectorView.DATE_TYPE_WEEK)) {
			type = 2;
		} else if (currentType.equalsIgnoreCase(DateSelectorView.DATE_TYPE_DAY)) {
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
			int month = mMonth + 1;

			if (month < 10) {
				param.put("fromTime", mYear + "-0" + month);
			} else {
				param.put("fromTime", mYear + "-" + month);
			}

		}

		return param;
	}

	private Dialog searchDialog;

	private void search() {
		// 先清空数据存储的上次查询数据
		clearDatabaseData();
		searchDialog = new MyProgressDialog(this, R.style.CustomProgressDialog, PublicUtils.getResourceString(WorkPlanActivity.this,R.string.init));
		String url = UrlInfo.queryWorkPlan(this);
		RequestParams param = searchParams();
		GcgHttpClient.getInstance(this.getApplicationContext()).post(url, param, new HttpResponseListener() {
			@Override
			public void onStart() {
				searchDialog.show();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
//				Log.e("view", "onSuccess:" + content);
				// 解析加载下来的数据
				try {
					JSONObject obj = new JSONObject(content);
					String resultCode = obj.getString("resultcode");
					if ("0000".equals(resultCode)) {
						WorkPlanUtils util = new WorkPlanUtils(WorkPlanActivity.this);
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
				findAllDataFromDb();
			}

		});

	}

	/**
	 * 从数据库查出所有的数据(月计划,周计划或者日计划)填充到list列表
	 */
	private void findAllDataFromDb() {
		WorkPlanModleDB db = new WorkPlanModleDB(this);
		PlanDataDB planDb = new PlanDataDB(this);
		planList = planDb.findPlanDataListByDate();
		Map<Integer, PlanData> plans = new HashMap<Integer, PlanData>();
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		if (planList.size() > 0) {
			// 遍历一下数据,根据数据类型,设置相应的日期
			for (PlanData modle : planList) {
				// PlanData planData=
				// planDb.checkPlanDataById(modle.getPlanId());
				String startStr = modle.getPlanData();// 开始日期
				String endStr = modle.getEndDate();// 结束日期
				if (currentType.equals(DateSelectorView.DATE_TYPE_MONTH)) {// 月计划类型
					// 转为月份类型时间

					try {
						Date d = sdf.parse(startStr);
						c.setTime(d);
						modle.setPlanTime(c.get(c.MONTH) + 1 + PublicUtils.getResourceString(WorkPlanActivity.this,R.string.month));

					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else if (currentType.equals(DateSelectorView.DATE_TYPE_WEEK)) {// 周计划类型
					// 转为周类型时间

					try {
						Date ds = sdf.parse(startStr);
						Date de = sdf.parse(endStr);
						c.setTime(ds);
						StringBuilder builder = new StringBuilder();

						builder.append(DateUtil.getDateByMonthDate(ds)).append("\n").append("~").append("\n");

						// builder.append((c.get(c.MONTH) + 1) + "." +
						// c.get(c.DAY_OF_MONTH)).append("\n").append("~")
						// .append("\n");

						c.setTime(de);

						builder.append(DateUtil.getDateByMonthDate(de));

						// builder.append((c.get(c.MONTH) + 1) + "." +
						// c.get(c.DAY_OF_MONTH));

						modle.setPlanTime(builder.toString());

					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else if (currentType.equals(DateSelectorView.DATE_TYPE_DAY)) {// 日计划类型
					// 转为日类型时间 10046-51441-51520-742874

					try {
						Date ds = sdf.parse(startStr);
						c.setTime(ds);
						modle.setPlanTime((c.get(c.MONTH) + 1) + "." + c.get(c.DAY_OF_MONTH));

					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				// 添加数据
				plans.put(modle.getPlanId(), modle);

			}
			// planList.clear();
			// //去重复后的数据集合
			// for (Map.Entry<Integer, PlanData> aPlan : plans.entrySet()) {
			// planList.add(aPlan.getValue());
			//
			// }
			//
			adapter = new MonthListAdapter(this, planList);

			lvPlanItem.setAdapter(adapter);
			//
		} else {

			Toast.makeText(this, R.string.work_plan_c26, Toast.LENGTH_SHORT).show();

		}

	}

	boolean isFirst;

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (isFirst) {
			search();
			isFirst = false;
		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		isFirst = true;
	}

	/**
	 * 工作计划查看人员
	 * 
	 * @param context
	 * @return
	 */
	public List<Dictionary> workPlanUserData(Context context, String fuuy) {
		String attendAuth = SharedPreferencesUtil.getInstance(context.getApplicationContext()).getWorkPlanAuth(); // 工作计划加入权限

		Integer auth = null;
		String authOrgId = null;
		if (!TextUtils.isEmpty(attendAuth)) {
			String arr[] = attendAuth.split("\\|");
			auth = Integer.valueOf(arr[0]);
			if (arr.length == 2) {
				authOrgId = arr[1];
			}
		}
		List<OrgUser> list = new OrgUserDB(context).findOrgUserListByAuth(auth, authOrgId, fuuy, null);
		return setDataSrcListForOrgUser(list);
	}

	/**
	 * 将用户实例转化为字典的形式
	 * 
	 * @param orgUserList
	 *            用户实例集合
	 */
	private List<Dictionary> setDataSrcListForOrgUser(List<OrgUser> orgUserList) {
		List<Dictionary> dataSrcList = new ArrayList<Dictionary>();
		if (orgUserList != null && !orgUserList.isEmpty()) {
			for (int i = 0; i < orgUserList.size(); i++) {
				OrgUser orgUser = orgUserList.get(i);
				Dictionary dic = new Dictionary();
				// dic.setCtrlCol(orgUser.getUserName());
				dic.setDid(orgUser.getUserId() + "");

				if (orgUser.getUserId().equals(SharedPreferencesUtil.getInstance(WorkPlanActivity.this).getUserId())) {
					dic.setCtrlCol(PublicUtils.getResourceString(WorkPlanActivity.this,R.string.my_plan));
					dataSrcList.add(0, dic);
				} else {
					dic.setCtrlCol(orgUser.getUserName());
					dataSrcList.add(dic);
				}

			}
		}
		return dataSrcList;
	}
}
