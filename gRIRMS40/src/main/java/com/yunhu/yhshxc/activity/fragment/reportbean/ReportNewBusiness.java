package com.yunhu.yhshxc.activity.fragment.reportbean;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 本月新增商机封装类,用于报表模块
 *
 */
public class ReportNewBusiness implements OnClickListener {
	private Context mContext;
	private LinearLayout monthTargetLayout;// 新增商机数整个条目布局
	private LinearLayout monthTargetChart;// 新增商机饼状图图布局
	private static boolean isHiddenChart = true;// 是否隐藏图表
	private TextView targetMoney;// 昨日新增
	private TextView predictMoney;// 本月新增总数
	private TextView returnMoney;// 本月总商机数
	private TextView newbusinessTitle;// 条目标题
	private TextView dayaddTitle;
	private TextView monthaddTitle;
	private TextView alladdTitle;

	private ImageView monthaddImag;// 本月新增箭头
	private ImageView dayaddImag;// 昨日新增箭头
	private ImageView alladdImag;// 总新增箭头
	private PieChart pieChart;// 柱状图表
	private View mView;

	public ReportNewBusiness(Context context) {
		mContext = context;
		mView = LayoutInflater.from(context).inflate(R.layout.report_piechart_layout, null);
		;
		monthTargetLayout = (LinearLayout) mView.findViewById(R.id.report_monthtarget_layout);
		monthTargetChart = (LinearLayout) mView.findViewById(R.id.report_month_targetchart);
		targetMoney = (TextView) mView.findViewById(R.id.report_target_money);
		predictMoney = (TextView) mView.findViewById(R.id.report_predict_money);
		newbusinessTitle = (TextView) mView.findViewById(R.id.report_newbusiness_title);
		dayaddTitle = (TextView) mView.findViewById(R.id.report_dayadd_title);
		monthaddTitle = (TextView) mView.findViewById(R.id.report_monthadd_title);
		alladdTitle = (TextView) mView.findViewById(R.id.report_alladd_title);
		returnMoney = (TextView) mView.findViewById(R.id.report_return_money);
		dayaddImag = (ImageView) mView.findViewById(R.id.report_dayadd_imag);
		monthaddImag = (ImageView) mView.findViewById(R.id.report_monthadd_imag);
		alladdImag = (ImageView) mView.findViewById(R.id.report_alladd_imag);
		pieChart = (PieChart) mView.findViewById(R.id.report_target_columchart);
		monthTargetChart.setVisibility(View.GONE);
		monthTargetLayout.setOnClickListener(this);

	}

	private ArrayList<String> xValues = new ArrayList<String>();
	private ArrayList<Entry> yValues = new ArrayList<Entry>();// 每个饼块实际数据
    private String tString="";
	/**
	 * 初始化数据
	 */
	public void initData(JSONObject jsobOne) {
		TextView[] titles = { dayaddTitle, monthaddTitle, alladdTitle };
		TextView[] showValues = { targetMoney, predictMoney, returnMoney };
		try {
			if (PublicUtils.isValid(jsobOne, "title")) {
				tString=jsobOne.getString("title");
				newbusinessTitle.setText(tString);
				
			}

			if (PublicUtils.isValid(jsobOne, "cols")) {// 标题
				JSONArray jsCols = jsobOne.getJSONArray("cols");
				for (int i = 0; i < jsCols.length(); i++) {
					if (i < titles.length) {
						titles[i].setText(jsCols.getString(i));
					}
				}

			}

			if (PublicUtils.isValid(jsobOne, "data")) {// 数值
				JSONArray jsArr1 = jsobOne.getJSONArray("data");
				JSONArray jsArr2 = jsArr1.getJSONArray(0);
				for (int i = 0; i < jsArr2.length(); i++) {
					float value = Float.parseFloat(jsArr2.getString(i));
					if (i < showValues.length) {
						showValues[i].setText(value + "");
					}

				}
			}

			if ("1".equals(jsobOne.getString("isDetail"))) {// 有折线图的数据
				JSONArray jsArr1 = jsobOne.getJSONArray("detail");
				JSONObject jsOb1 = jsArr1.getJSONObject(0);
				// JSONArray jsXYTip = jsOb1.getJSONArray("cols");
				JSONArray jsArr2 = jsOb1.getJSONArray("data");
				// 遍历
				for (int i = 0; i < jsArr2.length(); i++) {
					JSONArray jsArr3 = jsArr2.getJSONArray(i);
					String xVa = jsArr3.getString(0);
					float yVa = Float.parseFloat(jsArr3.getString(1));
					xValues.add(xVa);
					yValues.add(new Entry(yVa, i));

				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
	
	//#4bbbc2   #F79646   #4BACC6   #92D050   #C0504D   #4F81BD    #FFCB25   
	private String[] allColors = {"#4bbbc2","#F79646","#4BACC6","#aad3ad","#C0504D","#4F81BD","#FFCB25"};
	private void showPieChart(){

		// 数据设置
		// y轴集合
		PieDataSet pieDataSet = new PieDataSet(yValues, "");
		pieDataSet.setSliceSpace(0f);// 设置饼状图之间的间距
		// 饼状图块颜色集合
		ArrayList<Integer> colors = new ArrayList<Integer>();
		// 饼图颜色
         for (int i = 0; i < xValues.size(); i++) {
        	 colors.add(Color.parseColor(allColors[i]));
		}

		pieDataSet.setColors(colors);
		DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
		float px = 5 * (metrics.densityDpi / 160f);
		pieDataSet.setSelectionShift(px); // 选中态多出的长度

		PieData pieData = new PieData(xValues, pieDataSet);

		// pieChart.setHoleColor(Color.TRANSPARENT);
		pieChart.setHoleRadius(40f);// 设置圆心半径
		pieChart.setTransparentCircleRadius(30f); // 半透明圈
		pieChart.setDescription(tString);
		pieChart.setDrawCenterText(true); // 饼状图中间可以添加文字
		pieChart.setTouchEnabled(true);
		pieChart.setDrawHoleEnabled(true);
		pieChart.setRotationAngle(0); // 初始旋转角度
		pieChart.setRotationEnabled(true); // 可以手动旋转
		pieChart.setUsePercentValues(true); // 显示成百分比
		pieChart.setCenterText(""); // 饼状图中间的文字
		pieChart.setHighlightPerTapEnabled(true);
		pieChart.setTransparentCircleColor(Color.WHITE);
		pieChart.setTransparentCircleAlpha(110);
		pieChart.setDragDecelerationFrictionCoef(0.95f);
		// 设置数据
		pieChart.setData(pieData);
		Legend mLegend = pieChart.getLegend(); // 设置比例图
		mLegend.setPosition(LegendPosition.RIGHT_OF_CHART); // 最右边显示
		mLegend.setXOffset(10);
		// mLegend.setForm(LegendForm.LINE); //设置比例图的形状，默认是方形
		mLegend.setXEntrySpace(7f);
		mLegend.setYEntrySpace(5f);
		pieChart.animateXY(1000, 1000); // 设置动画
	}

	// /**
	// *
	// * @param count
	// * 展示块数
	// * @param totalNum
	// * 总量数
	// * @return
	// */
	// private PieData getPieData(int count, int totalNum) {
	// // 每个饼块代表的内容
	// ArrayList<String> xValues = new ArrayList<String>();
	//
	// for (int k = 0; k < count; k++) {
	// xValues.add("第" + k + "部分");
	// }
	// // 每个饼块实际数据
	// ArrayList<Entry> yValues = new ArrayList<Entry>();
	// yValues.add(new Entry(20, 0));// 0位置
	// yValues.add(new Entry(35, 0));// 1位置
	// yValues.add(new Entry(15, 0));// 2位置
	// yValues.add(new Entry(30, 0));// 3位置
	// // y轴集合
	// PieDataSet pieDataSet = new PieDataSet(yValues, "2017年度数据");
	// pieDataSet.setSliceSpace(0f);// 设置饼状图之间的间距
	// // 饼状图块颜色集合
	// ArrayList<Integer> colors = new ArrayList<Integer>();
	// // 饼图颜色
	// colors.add(Color.parseColor("#4BBBC2"));
	// colors.add(Color.rgb(114, 188, 223));
	// colors.add(Color.rgb(255, 123, 124));
	// colors.add(Color.rgb(57, 135, 200));
	// pieDataSet.setColors(colors);
	// DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
	// float px = 5 * (metrics.densityDpi / 160f);
	// pieDataSet.setSelectionShift(px); // 选中态多出的长度
	//
	// PieData pieData = new PieData(xValues, pieDataSet);
	//
	// return pieData;
	// }

	/**
	 * 返回整个view
	 * 
	 * @return
	 */
	public View getView() {
		return mView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.report_monthtarget_layout:// 根据此条目的点击来展示或者隐藏图表
			if (isHiddenChart) {
				monthTargetChart.setVisibility(View.VISIBLE);
				// 重新计算数据展示动画效果

				// 初始化数据
				if (yValues.size()>0||xValues.size()>0) {
					showPieChart();
				}
				isHiddenChart = !isHiddenChart;
			} else {
				monthTargetChart.setVisibility(View.GONE);
				isHiddenChart = !isHiddenChart;
			}
			break;

		default:
			break;
		}

	}

}
