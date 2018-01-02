package com.yunhu.yhshxc.activity.fragment.reportbean;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.application.SoftApplication;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 本月新增客户封装类,用于报表模块
 *
 */
public class ReportNewClient implements OnClickListener {
	private Context mContext;
	private LinearLayout monthTargetLayout;// 新增客户数整个条目布局
	private LinearLayout monthTargetChart;// 新增客户折线图布局
	private static boolean isHiddenChart = true;// 是否隐藏图表
	private TextView targetMoney;// 昨日新增
	private TextView predictMoney;// 本月新增总数
	private TextView returnMoney;// 本月日均新增
	private TextView monthtargetTitle;// 条目标题
	private TextView dayaddTitle;// 标题
	private TextView monthaddTitle;
	private TextView alladdTitle;
	private TextView lineChartYtip;//折线图Y轴标签

	private ImageView monthaddImag;// 本月新增箭头
	private ImageView dayaddImag;// 昨日新增箭头
	private ImageView alladdImag;// 总新增箭头
	private LineChart mLineChart;// 折线图表
	private View mView;

	public ReportNewClient(Context context) {
		mContext = context;
		mView = LayoutInflater.from(context).inflate(R.layout.report_linechart_layout, null);
		dayaddTitle = (TextView) mView.findViewById(R.id.report_dayadd_title);
		monthaddTitle = (TextView) mView.findViewById(R.id.report_monthadd_title);
		alladdTitle = (TextView) mView.findViewById(R.id.report_alladd_title);
		monthtargetTitle = (TextView) mView.findViewById(R.id.report_monthtarget_title);
		monthTargetLayout = (LinearLayout) mView.findViewById(R.id.report_monthtarget_layout);
		monthTargetChart = (LinearLayout) mView.findViewById(R.id.report_month_targetchart);
		targetMoney = (TextView) mView.findViewById(R.id.report_target_money);
		predictMoney = (TextView) mView.findViewById(R.id.report_predict_money);
		returnMoney = (TextView) mView.findViewById(R.id.report_return_money);
		lineChartYtip = (TextView) mView.findViewById(R.id.report_line_chart_ytip);
		dayaddImag = (ImageView) mView.findViewById(R.id.report_dayadd_imag);
		monthaddImag = (ImageView) mView.findViewById(R.id.report_monthadd_imag);
		alladdImag = (ImageView) mView.findViewById(R.id.report_alladd_imag);
		mLineChart = (LineChart) mView.findViewById(R.id.report_target_columchart);
		monthTargetChart.setVisibility(View.GONE);
		monthTargetLayout.setOnClickListener(this);

	}
  private ArrayList<String> xValues = new ArrayList<String>();//X数据集合
  private ArrayList<Entry> yValues = new ArrayList<Entry>();
  private String yTip="";
  private String xTip="";
  private String descTitle="";

	/**
	 * 初始化数据
	 */
	public void initData(JSONObject jsobOne) {
		TextView[] titles = { dayaddTitle, monthaddTitle, alladdTitle };
		TextView[] showValues = { targetMoney, predictMoney, returnMoney };
		// 解析数据
		try {
			if (PublicUtils.isValid(jsobOne, "title")) {
				
				descTitle=jsobOne.getString("title");
				monthtargetTitle.setText(descTitle);
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
				JSONArray jsXYTip = jsOb1.getJSONArray("cols");
				xTip = jsXYTip.getString(0);//X
				yTip = jsXYTip.getString(1);//Y轴标签
				lineChartYtip.setText(yTip);
				JSONArray jsArr2 = jsOb1.getJSONArray("data");
				//遍历
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

	// 设置显示的样式
	private void showChart(LineChart lineChart, int color) {
		LineDataSet lineDataSet = new LineDataSet(yValues,"");
		// 用y轴的集合来设置参数
		// 线宽
		lineDataSet.setLineWidth(2.0f);
		// 显示的圆形大小
		lineDataSet.setCircleSize(4f);
		// 显示颜色
		lineDataSet.setColor(Color.parseColor("#4BB8C2"));
		// 圆形的颜色
		lineDataSet.setCircleColor(Color.parseColor("#4BB8C2"));
		// 高亮的线的颜色
		lineDataSet.setHighLightColor(Color.RED);
		// 设置圆点的颜色
		lineDataSet.setFillColor(Color.parseColor("#4BB8C2"));
		lineDataSet.setDrawCircleHole(false);
		// lineDataSet.setValueTextSize(9f);
		lineDataSet.setFillAlpha(65);
		// y轴的数据 1======================================end

		// create a data object with the datasets
		LineData lineData = new LineData(xValues, lineDataSet);
		
		
		// 是否在折线图上添加边框
		lineChart.setDrawBorders(false);

		// 数据描述
		lineChart.setDescription("");
		// 如果没有数据的时候，会显示这个，类似listview的emtiew
		lineChart.setNoDataTextDescription(PublicUtils.getResourceString(SoftApplication.context,R.string.take_rest1));

		// 是否显示表格颜色
		lineChart.setDrawGridBackground(false);
		// 表格的的颜色，在这里是是给颜色设置一个透明度
		// lineChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF);

		// 设置是否可以触摸
		lineChart.setTouchEnabled(true);
		// 是否可以拖拽
		lineChart.setDragEnabled(true);
		// 是否可以缩放
		lineChart.setScaleEnabled(true);

		// if disabled, scaling can be done on x- and y-axis separately
		lineChart.setPinchZoom(false);

		// lineChart.setBackgroundColor(Color.rgb(Integer.parseInt("7e", 16),
		// Integer.parseInt("ce", 16), Integer.parseInt("f4", 16)));// 设置背景
		// 设置数据
		lineChart.setData(lineData);

		// 设置比例图标示，就是那个一组y的value的
		Legend mLegend = lineChart.getLegend();
		// mLegend.setPosition(LegendPosition.BELOW_CHART_CENTER);
		// 样式
		mLegend.setForm(LegendForm.CIRCLE);
		// 字体
		mLegend.setFormSize(0f);
		// 颜色
		mLegend.setTextColor(Color.RED);
		// 字体
		// mLegend.setTypeface(mTf);

		// 设置Y轴右边不显示数字
		lineChart.getAxisRight().setEnabled(false);

		XAxis xAxis = lineChart.getXAxis();
		// 设置X轴的数据显示在报表的下方
		xAxis.setPosition(XAxisPosition.BOTTOM);
		// xAxis.setDrawAxisLine(false);
		// 设置不从X轴发出纵向直线
		xAxis.setDrawGridLines(false);
		xAxis.setLabelRotationAngle(90);
		// 立即执行的动画,x轴
		lineChart.animateX(2500);

	}

//	/**
//	 * 生成一个数据
//	 * 
//	 * @param count
//	 *            表示图表中有多少个坐标点
//	 * @param range
//	 *            用来生成range以内的随机数
//	 * @return
//	 */
//	private LineData getLineData(int count, float range) {
//		ArrayList<String> xValues = new ArrayList<String>();
//		for (int i = 0; i < count; i++) {
//			// x轴显示的数据，这里默认使用数字下标显示
//			xValues.add("" + i);
//		}
//
//		ArrayList<LineDataSet> lineDataSets = new ArrayList<LineDataSet>();
//		// y轴的数据 1======================================start
//		ArrayList<Entry> yValues = new ArrayList<Entry>();
//		for (int i = 0; i < count; i++) {
//			float value = (float) (Math.random() * range) + 3;
//			yValues.add(new Entry(value, i));
//		}
//
//		// create a dataset and give it a type
//		// y轴的数据集合
//		LineDataSet lineDataSet = new LineDataSet(yValues, "" /* 测试折线图 */);
//		// mLineDataSet.setFillAlpha(110);
//		// mLineDataSet.setFillColor(Color.RED);
//
//		// 用y轴的集合来设置参数
//		// 线宽
//		lineDataSet.setLineWidth(2.0f);
//		// 显示的圆形大小
//		lineDataSet.setCircleSize(4f);
//		// 显示颜色
//		lineDataSet.setColor(Color.parseColor("#4BB8C2"));
//		// 圆形的颜色
//		lineDataSet.setCircleColor(Color.parseColor("#4BB8C2"));
//		// 高亮的线的颜色
//		lineDataSet.setHighLightColor(Color.RED);
//		// 设置圆点的颜色
//		lineDataSet.setFillColor(Color.parseColor("#4BB8C2"));
//		lineDataSet.setDrawCircleHole(false);
//		// lineDataSet.setValueTextSize(9f);
//		lineDataSet.setFillAlpha(65);
//
//		lineDataSets.add(lineDataSet);
//		// y轴的数据 1======================================end
//
//		// create a data object with the datasets
//		LineData lineData = new LineData(xValues, lineDataSet);
//
//		return lineData;
//	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.report_monthtarget_layout:// 点击此出展示或者隐藏图表
			if (isHiddenChart) {
				monthTargetChart.setVisibility(View.VISIBLE);
				// 重新计算数据,展示动画效果
				if (xValues.size()>0||yValues.size()>0) {			
					showChart(mLineChart, Color.rgb(114, 188, 223));
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

	/**
	 * 返回整个view
	 * 
	 * @return
	 */
	public View getView() {
		return mView;
	}

}
