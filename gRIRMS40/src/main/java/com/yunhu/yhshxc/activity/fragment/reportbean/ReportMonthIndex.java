package com.yunhu.yhshxc.activity.fragment.reportbean;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.fragment.BarChartsUtil;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 本月指标封装类,用于报表模块
 *
 */
public class ReportMonthIndex implements OnClickListener {
	private Context mContext;
    private LinearLayout monthTargetLayout;//本月指标整个条目布局
    private LinearLayout monthTargetChart;//本月指标柱状图布局
    private static boolean isHiddenChart=true;
    private TextView reportTitle;//条目标题
    private TextView targetMoney;//目标
    private TextView targetMoneyTitle;//目标 标题
    private TextView predictMoney;//预测
    private TextView predictMoneyTitle;//预测标题
    private TextView saleMoney;//销售订单
    private TextView saleMoneyTitle;//销售订单标题
    private TextView returnMoney;//回款
    private TextView returnMoneyTitle;//回款标题
    private BarChart targetMolumchart;//柱状图表
    private View mView;
    

	public ReportMonthIndex(Context context) {
		mContext = context;
		mView = LayoutInflater.from(context).inflate(R.layout.report_barchart_layout, null);;
		monthTargetLayout = (LinearLayout) mView.findViewById(R.id.report_monthtarget_layout);
		monthTargetChart = (LinearLayout) mView.findViewById(R.id.report_month_targetchart);
		targetMoney = (TextView) mView.findViewById(R.id.report_target_money);
		predictMoney = (TextView) mView.findViewById(R.id.report_predict_money);
		saleMoney = (TextView) mView.findViewById(R.id.report_sale_money);
		returnMoney = (TextView) mView.findViewById(R.id.report_return_money);
		targetMolumchart = (BarChart) mView.findViewById(R.id.report_target_columchart);
		reportTitle = (TextView) mView.findViewById(R.id.report_target_title);
		targetMoneyTitle = (TextView) mView.findViewById(R.id.report_frist_title);
		predictMoneyTitle = (TextView) mView.findViewById(R.id.report_second_title);
		saleMoneyTitle = (TextView) mView.findViewById(R.id.report_third_title);
		returnMoneyTitle = (TextView) mView.findViewById(R.id.report_fourth_title);
		monthTargetChart.setVisibility(View.GONE);//初始隐藏图表,随着点击进行展示
		monthTargetLayout.setOnClickListener(this);
		
	}
	
	private ArrayList<String> xValues;
	private ArrayList<BarEntry> yValues;
	
   /**
    * 初始化数据
    */
	public void initData(JSONObject json){
		TextView[] titles = {targetMoneyTitle,predictMoneyTitle,saleMoneyTitle,returnMoneyTitle};
		TextView[] showValues = {targetMoney,predictMoney,saleMoney,returnMoney};
		
		//解析数据,设置对应值
		 xValues = new ArrayList<String>();//X轴的标签值
		// 添加Y轴的值
		 yValues = new ArrayList<BarEntry>();
		try {
			if (PublicUtils.isValid(json, "title")) {
				reportTitle.setText(json.getString("title"));
			}
			if (PublicUtils.isValid(json, "cols")) {
				 JSONArray jsArr = json.getJSONArray("cols");
				 for (int i = 0; i < jsArr.length(); i++) {
					 String xv = jsArr.getString(i);
					 xValues.add(xv);
					 if (i<titles.length) {					
						 titles[i].setText(xv);
					}
				}
			}
			if (PublicUtils.isValid(json, "data")) {
				 JSONArray jsArr1 = json.getJSONArray("data");
				 JSONArray jsArr2 = jsArr1.getJSONArray(0);
				 for (int i = 0; i < jsArr2.length(); i++) {
					 float value = Float.parseFloat(jsArr2.getString(i));					 
					 yValues.add(new BarEntry(value, i));
					 if (i<showValues.length) {
						 showValues[i].setText(value+"");
					}
					
				}
				 
			}	
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		
//		yValues.add(new BarEntry(18, 0));
//		yValues.add(new BarEntry(28, 1));
//		yValues.add(new BarEntry(86, 2));
//		yValues.add(new BarEntry(7, 3));


	}
	
	private void showChart(){

		// y轴的数据集合
		BarDataSet barDataSet = new BarDataSet(yValues, "");
		// ArrayList<Integer> colors = new ArrayList<Integer>();
		// for(int i = 0;i < lables.length ;i++){
		// colors.add(Color.parseColor("#4BB8C2"));//柱形图的填充色
		// }
		// barDataSet.setColors(colors);
		barDataSet.setColor(Color.parseColor("#4BB8C2"));
		// 设置栏阴影颜色
		barDataSet.setBarShadowColor(Color.parseColor("#01000000"));
		barDataSet.setValueTextColor(Color.parseColor("#FF8F9D"));
		barDataSet.setBarSpacePercent(50f);// 设置柱子间距
		// 绘制值
		barDataSet.setDrawValues(true);
		ArrayList<IBarDataSet> barDataSets = new ArrayList<IBarDataSet>();
		barDataSets.add(barDataSet);
		// barDataSet.set
	     BarData  barData = new BarData(xValues, barDataSets);
		targetMolumchart.setVisibleXRangeMaximum(6);// 设置最大显示数,超出可滑动查看
		BarChartsUtil.showBarChart(targetMolumchart, barData, "", 0);
	}
	
	/**
	 * 返回整个view
	 * @return
	 */
	public View getView(){
		return mView;
	}
	@Override
	public void onClick(View v) {
	 switch (v.getId()) {
	case R.id.report_monthtarget_layout://点击此出展示或者隐藏图表
           if (isHiddenChart) {
        	   monthTargetChart.setVisibility(View.VISIBLE);
        	   //重新计算数据,展示动画效果
        	   showChart();
        	   isHiddenChart=!isHiddenChart;
		}else{
			
	     	   monthTargetChart.setVisibility(View.GONE);
        	   isHiddenChart=!isHiddenChart;
		}
		break;

	default:
		break;
	}
		
	}

}
