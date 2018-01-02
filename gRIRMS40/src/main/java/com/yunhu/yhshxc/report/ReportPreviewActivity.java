package com.yunhu.yhshxc.report;

import gcg.org.debug.JLog;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.bo.ReportWhere;
import com.yunhu.yhshxc.comp.menu.PreviewDataComp;
import com.yunhu.yhshxc.parser.ReportParse;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;

/**
 * 报表查询条件预览页面
 * 预览查询条件，点击查询跳转到查询列表页面
 * @author jishen
 */
public class ReportPreviewActivity extends AbsBaseActivity {
	private String TAG = "ReportPreviewActivity";
	/**
	 * 预览所有控件的父view
	 */
	private LinearLayout showDataLayout;
	/**
	 * 查询按钮
	 */
	private LinearLayout submitDataLayout;
	/**
	 * 输入的查询条件传给后台
	 */
	private String reportValue;
	/**
	 * 如果没有查询条件显示
	 */
	private TextView noReplenish;
	/**
	 * 查询条件外层的ScrollView
	 */
	private ScrollView repleish_sv;
	/**
	 * menuType模块类型targetId数据ID
	 */
	private int menuType,targetId;
	/**
	 *传递数据的bundle
	 */
	private Bundle bundle;
	/**
	 * 传过来的查询条件的值的bundle
	 */
	private Bundle report;
	/**
	 * 传过来下拉框选项名称的bundle
	 */
	private Bundle valueBundle;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report_detail);
		initBase();//父类方法，目前不做处理
		bundle=getIntent().getBundleExtra("bundle");
		valueBundle=getIntent().getBundleExtra("valueBundle");
		menuType=bundle.getInt("menuType", 0);
		targetId=bundle.getInt("targetId", 0);
		
		init();//初始化数据
		try {
			addData();//添加数据
		} catch (Exception e) {

			e.printStackTrace();
		}
		if (showDataLayout.getChildCount() == 0) {//如果没有查询条件设置页面显示没有查询条件
			noReplenish.setVisibility(View.VISIBLE);
			repleish_sv.setVisibility(View.GONE);
			JLog.d(TAG,"showDataLayout.getChildCount()==>"+ showDataLayout.getChildCount());
		}
	}

	/*
	 * 初始化数据
	 */
	private void init() {
		report=getIntent().getBundleExtra("reportValue");
		showDataLayout = (LinearLayout) findViewById(R.id.ll_report_detail_add_data);
		submitDataLayout = (LinearLayout) findViewById(R.id.ll_report_show_detail_data);
		noReplenish=(TextView)findViewById(R.id.tv_noReport);
		repleish_sv=(ScrollView)findViewById(R.id.report_sv);
		submitDataLayout.setOnClickListener(this);
	}

	/**
	 * 根据查询条件初始化页面
	 * @throws Exception
	 */
	private void addData() throws Exception {
		PreviewDataComp previewDataComp;//定义一个预览view
		StringBuffer buffer=new StringBuffer("");
		List<ReportWhere> funcList = null;
		if(menuType==Menu.TYPE_REPORT){//报表
			funcList = new ReportParse().parseReportWhere(this,targetId);
		}else{//新报表
			funcList = new ReportParse().parseReportWhere2(this,targetId);
		}
			
			for (int i = 0; i < funcList.size(); i++) { // 循环添加操作项
				ReportWhere func=funcList.get(i);//获取实例
				String key=func.getColumnNumber();//控件ID
				String value=null;
				if(report.containsKey(key) && !TextUtils.isEmpty(report.getString(key))){
					previewDataComp = new PreviewDataComp(this);
					value=report.getString(key);
					previewDataComp.setShowDataTitle(func.getColumnName());
					if(menuType==Menu.TYPE_REPORT_NEW){//新报表中
						if(Integer.parseInt(func.getCtrlType())==Constants.FUNC_TYPE_MULTI_SELECT||Integer.parseInt(func.getCtrlType())==Constants.FUNC_TYPE_SELECT){
							previewDataComp.setShowDataContent(valueBundle.getString(key));//多选下拉框，和下拉框的情况从valueBundle中获取值
						}else{
							previewDataComp.setShowDataContent(value.replace("~@@", "~"));
						}
					}else{
						previewDataComp.setShowDataContent(value.replace("~@@", "~"));
					}
//					previewDataComp.setShowDataContent(value.replace("~@@", "~"));
					buffer.append("$@@").append(key).append(",").append(value);
					showDataLayout.addView(previewDataComp.getView());
				}
				
			}
			if(buffer.length()>0){
				reportValue = buffer.substring(3).toString();//获取查询条件去除前面的“~@@”
			}
	}

	
	
	

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ll_report_show_detail_data: // 报表
			intentAttend();
			break;
		default:
			break;
		}
	}

	/**
	 * 跳转到报表查询列表页面 将查询条件存储到SharedPreferencesUtil中
	 */
	private void intentAttend(){
		SharedPreferencesUtil.getInstance(this).setReplenish(reportValue);
		Intent intent=new Intent(this,ReportListActivity.class);
		intent.putExtra("bundle", bundle);
		startActivity(intent);
		this.finish();
	}

}
