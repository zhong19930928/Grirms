package com.yunhu.yhshxc.report;


import gcg.org.debug.JLog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.bo.ReportSelectData;
import com.yunhu.yhshxc.bo.ReportWhere;
import com.yunhu.yhshxc.comp.CompDialog;
import com.yunhu.yhshxc.comp.menu.FuncBtnLayoutMenu;
import com.yunhu.yhshxc.comp.menu.FuncLayoutMenu;
import com.yunhu.yhshxc.parser.ReportParse;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.widget.wheeltime.TimeView;
/**
 * 报表查询条件页面
 * 显示报表的查询条件
 * @author jishen
 *
 */
public class ReportActivity extends AbsBaseActivity {
	private String TAG = "ReportActivity";
	/**
	 * 所有控件的父view
	 */
	private LinearLayout funcLayout; 
	/**
	 * 传递数据的bundle
	 */
	private Bundle bundle;
	/**
	 * 报表查询条件控件的集合
	 */
	private List<ReportWhere> reportList;
	/**
	 * 存储当前操作页面添加的View
	 */
	private List<View> viewList;
	/**
	 * 预览按钮
	 */
	private LinearLayout showDataLinearLayout;
	/**
	 * 数据ID
	 */
	private Integer targetId;
	/**
	 * 对勾Mark View
	 */
	public View hookView;
	/**
	 * 模块类型
	 */
	public int menuType;
	/**
	 * 是否能点击 true 能点击 false 不能点击
	 */
	public boolean isCanClick = true;
	/**
	 * 当前dialog
	 */
	private Dialog currentDialog=null;
	/**
	 * 存放查询条件
	 */
	public Bundle reportValue;
	/**
	 * 当前控件的查询条件的值
	 */
	private String currentValue;
	
	
	private int year;//年
	private int month;//月
	private int day;//日
	private int hour;//小时
	private int minutes;//分钟
	private int selectType;//有范围的时间控件选中类型 0是开始时间 1是结束时间
	
	/**
	 * 有范围的日期的 年 月 日 小时 分钟
	 */
	private int year_first,year_second;
	private int month_first,month_second;
	private int day_first,day_second;
	private int hour_first,hour_second;
	private int minutes_first,minutes_second;
	
	/**
	 * 存放机构联动条件
	 */
	public Map<String,String> orgMap;
	/**
	 * 存放下拉框中的选项的名称
	 */
	public Bundle valueBundle;
	/**
	 * 有范围的输入框的值
	 */
	private String firstValue,secondValue;
	/**
	 * 有范围的时间控件的值
	 */
	private String firstTimeValue,secondTimeValue;
	/**
	 * 有范围的日期控件的年月日的拼接结果
	 */
	private String currentBufferFirst,currentBufferSecond;
	/**
	 * 下拉框的值
	 */
	public List<ReportSelectData> reportSelectDatalist;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report);
		initBase();//父类方法，目前没做处理
		bundle = getIntent().getBundleExtra("bundle");
		targetId=bundle.getInt("targetId");//获取数据ID
		menuType=bundle.getInt("menuType");//获取模块类型
		reportValue=new Bundle();
		orgMap=new HashMap<String, String>();
		valueBundle=new Bundle();
		init();
		TextView title=(TextView)findViewById(R.id.tv_report_titleName);
		title.setText(bundle.getString("menuName"));//设置标题
	}

	/**
	 * 初始化对象
	 * 获取数据源
	 */
	private void init() {
		viewList = new ArrayList<View>();
		showDataLinearLayout = (LinearLayout) findViewById(R.id.ll_report_btn__layout);
		showDataLinearLayout.setOnClickListener(this);
		try {
			if(menuType==Menu.TYPE_REPORT){//旧报表
				reportList = new ReportParse().parseReportWhere(this,targetId);
			}else{//新报表
				reportList = new ReportParse().parseReportWhere2(this,targetId);
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		if(reportList == null || reportList.isEmpty()){//如果没有查询条件，直接跳转到查询列表页面
			Intent intent=new Intent(this,ReportListActivity.class);
			intent.putExtra("bundle", bundle);
			startActivity(intent);
			this.finish();
		}else{
			bundle.putInt("targetType", Menu.TYPE_REPORT);
			initLayout(reportList);//有查询条件，初始化页面
		}
	}

	/**
	 * 初始化功能项视图
	 * @param reportList 查询条件集合
	 */
	private void initLayout(List<ReportWhere> reportList) {
		funcLayout = (LinearLayout) findViewById(R.id.ll_report);
		
		if (reportList != null) {
			int len = reportList.size();
			LinearLayout ll_two = null;
			if (len == 1) { // 表示就一行添加大View
				ll_two = new LinearLayout(this);
				ll_two.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				ll_two.addView(getFuncView(0,reportList.get(0)));
				ll_two.setOrientation(LinearLayout.VERTICAL);
				ll_two.addView(getFuncView(-1,null));
				funcLayout.addView(ll_two);
			} else {
				
				for (int i = 1; i < len; i += 2) { // 表示要显示两个小View
					ll_two = new LinearLayout(this);
					ll_two.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
					ll_two.setOrientation(LinearLayout.VERTICAL);
					ll_two.addView(getFuncView(i-1,reportList.get(i-1)));
					ll_two.addView(getFuncView(i,reportList.get(i)));
					funcLayout.addView(ll_two);
				}
				if (len % 2 != 0) { // 表示还有一行大View要显示
					ll_two = new LinearLayout(this);
					ll_two.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
					ll_two.setOrientation(LinearLayout.VERTICAL);
					ll_two.addView(getFuncView(len-1,reportList.get(len-1)));
					ll_two.addView(getFuncView(-1,null));
					funcLayout.addView(ll_two);
				}
			}
		}
		LinearLayout ll_btnLayout=(LinearLayout)findViewById(R.id.ll_report_btn__layout);
		View view = new FuncBtnLayoutMenu(this, this).getView(null);
		TextView textView = (TextView) view.findViewById(R.id.tv_btn_five);
		textView.setText(R.string.priview_);
		ll_btnLayout.addView(view);
	}
	
	/**
	 * 创建一个FUNC VIEW
	 * @param index 控件的下标
	 * @param reportWhere 控件的实例
 	 * @return 控件的视图
	 */
	
	private View getFuncView(int index,ReportWhere reportWhere){
		if(index==-1){
			View view = new FuncLayoutMenu(this,null,true).getView();
			view.setVisibility(View.INVISIBLE);
			return view;
		}else{
			Func tFunc=new Func();
			tFunc.setType(Integer.parseInt(reportWhere.getCtrlType()));
			FuncLayoutMenu funcLayoutMenu = new FuncLayoutMenu(this,tFunc,true);
			funcLayoutMenu.setBackgroundResource(R.drawable.func_menu_onclick);
			funcLayoutMenu.setIcon(setIconForTypeNew(Integer.parseInt(reportWhere.getCtrlType()))); // 设置显示的图标
			funcLayoutMenu.setFuncName(reportWhere.getColumnName()); // 设置名字
			funcLayoutMenu.setVisibilityForCheckMark(false); // 初始设置标记不显示
			View view=funcLayoutMenu.getView().findViewById(R.id.ll_func_menu);
			view.setId(Integer.parseInt(reportWhere.getCtrlType()));// 给VIEW设置ID
			view.setOnClickListener(this);
			view.setTag(reportWhere);
			return funcLayoutMenu.getView();
		}
	}

	/*
	 * 跳转到显示采集信息将选择的查询条件和关联关系传过去
	 */

	private void showPreview() {
		Intent intent=new Intent(this,ReportPreviewActivity.class);
		intent.putExtra("bundle", bundle);
		intent.putExtra("valueBundle", valueBundle);
		intent.putExtra("reportValue", reportValue);
		startActivity(intent);
	}

	
	/**
	 * 获取体表背景图片
	 * @param type 控件类型
	 * @return 图片的ID
	 */
	private int setIconForTypeNew(int type) {
		switch (type) {
		case Constants.FUNC_TYPE_DATE:
		case Constants.FUNC_TYPE_TIME:
		case Constants.FUNC_TYPE_RANGE_TIME:
		case Constants.FUNC_TYPE_RANGE_DATE:
			return R.drawable.f_date;
		case Constants.FUNC_TYPE_TEXT:
			return R.drawable.f_default;
		default:
			break;
		}
		return R.drawable.f_default;
	}


	/**
	 * 获取体表背景图片
	 * @param type 控件类型
	 * @return 图片的ID
	 */
	private int setIconForType(int type) {
		switch (type) {
		case Constants.FUNC_TYPE_DATE:
		case Constants.FUNC_TYPE_TIME:
		case Constants.FUNC_TYPE_RANGE_TIME:
		case Constants.FUNC_TYPE_RANGE_DATE:
			return R.drawable.func_riqi1;
		case Constants.FUNC_TYPE_TEXT:
			return R.drawable.func_shujushangbao1;
		default:
			break;
		}
		return R.drawable.func_shujushangbao1;
	}

	
	
	/*
	 * 设置对勾图片是否显示
	 */
	public void setShowHook(boolean isShowHook) {
		if (isShowHook) {
			this.hookView.setVisibility(View.VISIBLE);
		} else {
			this.hookView.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		setShowHook();
	}

	/*
	 * 返回的时候重新查询数据库看是否显示对勾
	 * 如果已经操作过查询条件就显示对勾
	 */
	private void setShowHook() {
		for (int i = 0; i < viewList.size(); i++) {
			View view = viewList.get(i);
			ReportWhere reportWhere = (ReportWhere) view.getTag();
			hookView = view.findViewById(R.id.iv_func_check_mark);
			if(reportValue.containsKey(reportWhere.getColumnNumber())){
				if(!TextUtils.isEmpty(reportValue.getString(reportWhere.getColumnNumber()))){
					setShowHook(true);
				}else{
					reportValue.remove(reportWhere.getColumnNumber());
					setShowHook(false);
				}
			}else{
				setShowHook(false);
			}

		}
	}

	private ReportWhere currentReport = null;
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		currentReport = (ReportWhere) v.getTag();
		switch (v.getId()) {
		case FuncBtnLayoutMenu.SHOWPRIVIEW_ID:// 预览显示数据  预览ID设置的是-100
			showPreview();
			break;
		default:
			isCanClick = false;
			hookView = v.findViewById(R.id.iv_func_check_mark);
			try {
				if(Integer.parseInt(currentReport.getCtrlType())==Constants.FUNC_TYPE_MULTI_SELECT){//多选下拉框
					Func func=new Func();
					func.setFuncId(-2907);//设置一步重复Id作为区分
					func.setTargetid(targetId);
					func.setType(Func.TYPE_MULTI_CHOICE_SPINNER_COMP);
					bundle.putBoolean("isReport", true);//设置是报表
					CompDialog comp =new CompDialog(ReportActivity.this, func, bundle);
					comp.setReportFlag(true);
					comp.setReportWhere(currentReport);
					currentDialog=comp.createDialog();//弹出对话框
				}else{//不是多选下拉框的情况
					currentDialog=reportDialog(currentReport,reportValue.getString(currentReport.getColumnNumber()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(currentDialog != null){
				currentDialog.show();	
			}
			break;
		}
	}
	
	/**
	 * 退出的时候提示
	 */
	public void onClickBack() {
		Dialog backDialog = backDialog(ReportActivity.this
				.getResources().getString(R.string.BACKDIALOG));
		backDialog.show();
	}

	/**
	 * 
	 * @return 当前正操作的查询条件的实例
	 */
	public ReportWhere getCurrentReportWhere(){
		return currentReport;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
//			onClickBack();
			back();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onPause() {

		super.onPause();
		if(currentDialog!=null && currentDialog.isShowing()){
			currentDialog.dismiss();
			isCanClick = true;
		}
	}
	/*
	 * 退出的时候删除没有提交的数据
	 */
	private void back() {
		reportValue.clear();
		ReportActivity.this.finish();
	}

	/**
	 * 退出提示dialog
	 * @param bContent 提示的内容
	 * @return dialog
	 */
	private Dialog backDialog(String bContent) {
		final Dialog backDialog = new Dialog(this, R.style.transparentDialog);
		View view = View.inflate(this, R.layout.back_dialog, null);
		TextView backContent = (TextView) view
				.findViewById(R.id.tv_back_content);
		Button confirm = (Button) view.findViewById(R.id.btn_back_confirm);
		Button cancel = (Button) view.findViewById(R.id.btn_back_cancel);
		backContent.setText(bContent);
		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				backDialog.dismiss();
				back();
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				backDialog.dismiss();

			}
		});
		backDialog.setContentView(view);
		backDialog.setCancelable(false);
		return backDialog;

	}

	/**
	 * 保存查询条件
	 * @param reportWhere 当前控件实例
	 * @param value 当前控件的值
	 */
	public void saveReport(ReportWhere reportWhere,String value){
		if(!TextUtils.isEmpty(value)){
			JLog.d(TAG, "saveReport==>"+value);
			reportValue.putString(reportWhere.getColumnNumber(), value);
			setShowHook(true);
		}else{
			if(reportValue.containsKey(reportWhere.getColumnNumber())){
				reportValue.remove(reportWhere.getColumnNumber());
			}
			setShowHook(false);
		}
	}
	
	/**
	 * 不是多选下拉框 根据类型填出不同的dialog
	 * @param reportWhere 控件实例
	 * @param value 控件的值
	 * @return dialog
	 * @throws Exception
	 */
	private Dialog reportDialog(final ReportWhere reportWhere,String value) throws Exception{
		final Dialog dialog = new Dialog(this, R.style.transparentDialog);
		View view=null;
		currentValue=null;
		switch (Integer.parseInt(reportWhere.getCtrlType())) {
		case Constants.FUNC_TYPE_TEXT:// 文本
			view=View.inflate(ReportActivity.this, R.layout.report_edittext_dialog, null);
			EditText tv=(EditText)view.findViewById(R.id.report_edittext);
			if(!TextUtils.isEmpty(value)){
				tv.setText(value);
			}
			tv.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					currentValue=s.toString();
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {

					
				}
				
				@Override
				public void afterTextChanged(Editable s) {

					
				}
			});
			break;
		case Constants.FUNC_TYPE_RANGE_TEXT:// 有范围的输入框
			view=View.inflate(ReportActivity.this, R.layout.report_edittext_range_dialog, null);
			EditText et_first=(EditText)view.findViewById(R.id.report_edittext_first);
			EditText et_second=(EditText)view.findViewById(R.id.report_edittext_second);
			
			
			if(!TextUtils.isEmpty(value)){
				
				et_first.setText(value.split("~@@")[0]);
				et_second.setText(value.split("~@@")[1]);
			}
			et_first.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					firstValue=s.toString();
					currentValue=firstValue+"~@@"+secondValue;
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					
				}
				
				@Override
				public void afterTextChanged(Editable s) {
					
					
				}
			});
			et_second.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					secondValue=s.toString();
					currentValue=firstValue+"~@@"+secondValue;
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					
					
				}
				
				@Override
				public void afterTextChanged(Editable s) {
					
					
				}
			});
			break;
		case Constants.FUNC_TYPE_SELECT:// 下拉选择
			view=View.inflate(ReportActivity.this, R.layout.report_spinner_dialog, null);
			Spinner spinner=(Spinner)view.findViewById(R.id.report_spinner);
			if(menuType==Menu.TYPE_REPORT){//旧报表
				reportSelectDatalist = new ReportParse().parseReportWhereForDict(ReportActivity.this,targetId, reportWhere.getColumnNumber(), orgMap.get(reportWhere.getColumnNumber()));
			} else {//新报表
				reportSelectDatalist = new ReportParse().parseReportWhereForDict2(ReportActivity.this,targetId, reportWhere.getColumnNumber(), orgMap.get(reportWhere.getColumnNumber()));
			}
			
			final String[] srcString=new String[reportSelectDatalist.size()];
			
			
			for (int i = 0; i < reportSelectDatalist.size(); i++) {
				srcString[i]=reportSelectDatalist.get(i).getName();
			}
			ArrayAdapter<String> adapter=new ArrayAdapter<String>(ReportActivity.this,
					R.layout.single_choice_view, srcString);
			adapter.setDropDownViewResource(R.layout.sprinner_check_item);
			spinner.setAdapter(adapter);
			spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					if(menuType==Menu.TYPE_REPORT_NEW){//新报表存id
						currentValue=reportSelectDatalist.get(position).getCode();
						valueBundle.putString(reportWhere.getColumnNumber(), reportSelectDatalist.get(position).getName());
					}else{//存name
						currentValue=srcString[position];
					}
					if(!TextUtils.isEmpty(reportWhere.getNextColumnNumber())){
						orgMap.put("seldata_"+reportWhere.getNextColumnNumber(), reportSelectDatalist.get(position).getCode());
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
			
					
				}
			});
			//控制级联筛选
			if(reportSelectDatalist!=null && !reportSelectDatalist.isEmpty() && !TextUtils.isEmpty(value)){
				for (int j = 0; j < reportSelectDatalist.size(); j++) {
					if(reportSelectDatalist.get(j).getCode().equals(value)){
						spinner.setSelection(j);
						break;
					}
				}
			}
			break;
		case Constants.FUNC_TYPE_TIME:// 时间
			if(!TextUtils.isEmpty(value)){
				String[] time = value.split(":");
				hour=Integer.parseInt(time[0]);
				minutes=Integer.parseInt(time[1]);
				currentValue=value;
			}else{
				Calendar c=Calendar.getInstance();
				hour=c.get(Calendar.HOUR_OF_DAY);
				minutes=c.get(Calendar.MINUTE);
			}
			view=View.inflate(ReportActivity.this, R.layout.report_time_dialog, null);
			LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll_compDialog);
			TimeView timeView = new TimeView(ReportActivity.this,TimeView.TYPE_TIME, new TimeView.WheelTimeListener() {
				
				@Override
				public void onResult(String wheelTime) {
					currentValue = wheelTime;
				}
			});
			timeView.setOriTime(hour, minutes);
			ll.addView(timeView);
			break;
		case Constants.FUNC_TYPE_RANGE_TIME:// 有范围的时间
			selectType = 0;
			if(!TextUtils.isEmpty(value)){
				String[] time=value.split("~@@");
				hour_first=Integer.parseInt(time[0].split(":")[0]);
				minutes_first=Integer.parseInt(time[0].split(":")[1]);
				hour_second=Integer.parseInt(time[1].split(":")[0]);
				minutes_second=Integer.parseInt(time[1].split(":")[1]);
				currentValue=value;
			}else{
				Calendar c_t=Calendar.getInstance();
				hour_first=c_t.get(Calendar.HOUR_OF_DAY);
				minutes_first=c_t.get(Calendar.MINUTE);
				hour_second=c_t.get(Calendar.HOUR_OF_DAY);
				minutes_second=c_t.get(Calendar.MINUTE);
			}
			firstTimeValue=hour_first+":"+minutes_first;
			secondTimeValue=hour_second+":"+minutes_second;
			currentValue=firstTimeValue+"~@@"+secondTimeValue;
			view = View.inflate(this, R.layout.report_date_range_comp, null);
			LinearLayout ll_second_time = (LinearLayout)view.findViewById(R.id.datePickerComp_second);
			final LinearLayout ll_range_start = (LinearLayout)view.findViewById(R.id.ll_range_time_start);
			final LinearLayout ll_range_end = (LinearLayout)view.findViewById(R.id.ll_range_time_end);
			final TextView tv_range_start = (TextView)view.findViewById(R.id.tv_range_time_start);
			final TextView tv_range_end = (TextView)view.findViewById(R.id.tv_range_time_end);
			ll_range_start.setBackgroundResource(R.color.bbs_menu_blue);
			
			final TimeView t_second = new TimeView(this, TimeView.TYPE_TIME, new TimeView.WheelTimeListener() {
				
				@Override
				public void onResult(String wheelTime) {
					if (selectType == 0) {//选中开始时间
						firstTimeValue = wheelTime;
					}else{
						secondTimeValue = wheelTime;
					}
					tv_range_start.setText(firstTimeValue);
					tv_range_end.setText(secondTimeValue);
					currentValue=firstTimeValue+"~@@"+secondTimeValue;

				}
			});

			ll_range_start.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					selectType = 0;	
					ll_range_start.setBackgroundResource(R.color.bbs_menu_blue);
					ll_range_end.setBackgroundResource(R.color.transparent);
					String[] first = firstTimeValue.split(":");
					t_second.setOriTime(Integer.parseInt(first[0]), Integer.parseInt(first[1]));
				}
			});
			ll_range_end.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					selectType = 1;	
					ll_range_end.setBackgroundResource(R.color.bbs_menu_blue);
					ll_range_start.setBackgroundResource(R.color.transparent);
					String[] second = secondTimeValue.split(":");
					t_second.setOriTime(Integer.parseInt(second[0]), Integer.parseInt(second[1]));
				}
			});
			
			tv_range_start.setText(firstTimeValue);
			tv_range_end.setText(secondTimeValue);
			t_second.setOriTime(hour_first, minutes_first);
			ll_second_time.addView(t_second);
			break;
		case Constants.FUNC_TYPE_DATE:// 日期
			view=View.inflate(ReportActivity.this, R.layout.report_date_dialog, null);
			if(!TextUtils.isEmpty(value)){
				String[] date = value.split("-");
				year = Integer.parseInt(date[0]);
				month = Integer.parseInt(date[1]) - 1;
				day = Integer.parseInt(date[2]);
				currentValue=value;
			}else{
				Calendar _c = Calendar.getInstance();// 获取系统默认是日期
				year = _c.get(Calendar.YEAR);
				month = _c.get(Calendar.MONTH);
				day = _c.get(Calendar.DAY_OF_MONTH);
			}
			LinearLayout ll_date = (LinearLayout) view.findViewById(R.id.ll_compDialog);
			TimeView dateView = new TimeView(ReportActivity.this,TimeView.TYPE_DATE, new TimeView.WheelTimeListener() {
				
				@Override
				public void onResult(String wheelTime) {
					currentValue = wheelTime;
				}
			});
			dateView.setOriDate(year, month+1, day);
			ll_date.addView(dateView);
			break;
		case Constants.FUNC_TYPE_RANGE_DATE:// 有范围的日期
			selectType = 0;
			if(!TextUtils.isEmpty(value)){
				String[] date = value.split("~@@");
				String[] firstDate=date[0].split("-");
				String[] secondDate=date[1].split("-");
				year_first = Integer.parseInt(firstDate[0]);
				month_first = Integer.parseInt(firstDate[1]) - 1;
				day_first = Integer.parseInt(firstDate[2]);
				year_second = Integer.parseInt(secondDate[0]);
				month_second = Integer.parseInt(secondDate[1]) - 1;
				day_second = Integer.parseInt(secondDate[2]);
			}else{
				Calendar __c = Calendar.getInstance();// 获取系统默认是日期
				year_first = __c.get(Calendar.YEAR);
				month_first = __c.get(Calendar.MONTH);
				day_first = __c.get(Calendar.DAY_OF_MONTH);
				year_second = __c.get(Calendar.YEAR);
				month_second = __c.get(Calendar.MONTH);
				day_second = __c.get(Calendar.DAY_OF_MONTH);
			}
			currentBufferFirst=year_first+"-"+String.valueOf(month_first+ 1+100).substring(1)+"-"+String.valueOf(day_first+100).substring(1);
			currentBufferSecond=year_second+"-"+String.valueOf(month_second+ 1+100).substring(1)+"-"+String.valueOf(day_second+100).substring(1);
			currentValue=currentBufferFirst+"~@@"+currentBufferSecond;
			view = View.inflate(this, R.layout.report_date_range_comp, null);
			LinearLayout ll_second_date = (LinearLayout)view.findViewById(R.id.datePickerComp_second);
			final LinearLayout ll_range_start_date = (LinearLayout)view.findViewById(R.id.ll_range_time_start);
			final LinearLayout ll_range_end_date = (LinearLayout)view.findViewById(R.id.ll_range_time_end);
			final TextView tv_range_start_date = (TextView)view.findViewById(R.id.tv_range_time_start);
			final TextView tv_range_end_date = (TextView)view.findViewById(R.id.tv_range_time_end);
			final TimeView d_second = new TimeView(this, TimeView.TYPE_DATE, new TimeView.WheelTimeListener() {
				
				@Override
				public void onResult(String wheelTime) {
					if (selectType == 0) {//选中开始时间
						currentBufferFirst = wheelTime;
					}else{
						currentBufferSecond = wheelTime;
					}
					tv_range_start_date.setText(currentBufferFirst);
					tv_range_end_date.setText(currentBufferSecond);
					currentValue=currentBufferFirst+"~@@"+currentBufferSecond;
				}
			});

			ll_range_start_date.setBackgroundResource(R.color.bbs_menu_blue);
			ll_range_start_date.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					selectType = 0;	
					ll_range_start_date.setBackgroundResource(R.color.bbs_menu_blue);
					ll_range_end_date.setBackgroundResource(R.color.transparent);
					String[] first = currentBufferFirst.split("-");
					d_second.setOriDate(Integer.parseInt(first[0]), Integer.parseInt(first[1]), Integer.parseInt(first[2]));
				}
			});
			ll_range_end_date.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					selectType = 1;	
					ll_range_end_date.setBackgroundResource(R.color.bbs_menu_blue);
					ll_range_start_date.setBackgroundResource(R.color.transparent);
					String[] second = currentBufferSecond.split("-");
					d_second.setOriDate(Integer.parseInt(second[0]), Integer.parseInt(second[1]), Integer.parseInt(second[2]));
				}
			});
			
			tv_range_start_date.setText(currentBufferFirst);
			tv_range_end_date.setText(currentBufferSecond);
			
			d_second.setOriDate(year_first, month_first+1, day_first);
			ll_second_date.addView(d_second);
			break;
		default:
			break;
		}
		Button confirmBtn=(Button)view.findViewById(R.id.report_dialog_confirmBtn);
		Button cancelBtn=(Button)view.findViewById(R.id.report_dialog_cancelBtn);
		confirmBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(Integer.parseInt(reportWhere.getCtrlType())==Constants.FUNC_TYPE_RANGE_TEXT){//有范围的
					String[] s=currentValue.split("~@@");
					if(s.length<2){//完整型控制数据两个都要输入
						Toast.makeText(ReportActivity.this, R.string.toast_string7, Toast.LENGTH_SHORT).show();
					}else{
						dialog.dismiss();
						saveReport(reportWhere, currentValue);
					}
				}else{
					dialog.dismiss();
					saveReport(reportWhere, currentValue);
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
		return dialog;
	}
	
	
}
