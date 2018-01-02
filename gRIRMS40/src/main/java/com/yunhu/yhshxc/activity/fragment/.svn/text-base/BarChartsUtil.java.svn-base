package com.yunhu.yhshxc.activity.fragment;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.application.SoftApplication;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import android.graphics.Color;
import android.graphics.Typeface;

/**
 * 主页面报表模块柱形图属性管理工具类
 * @author qingli
 *
 */
public class BarChartsUtil {

	  
//	  private String[] color = {"#34CFBE","#34CFBE","#34CFBE","#34CFBE","#34CFBE","#34CFBE","#34CFBE","#34CFBE","#34CFBE","#34CFBE","#34CFBE","#6BF3AD","#C4FF8E","#FFF88D","#FFD38C","#8CEBFF","#FF8F9D","#6BF3AD","#C4FF8E","#FFF88D","#FFD38C","#8CEBFF","#FF8F9D","#6BF3AD","#C4FF8E","#FFF88D","#FFD38C","#8CEBFF","#FF8F9D","#6BF3AD","#C4FF8E","#FFF88D","#FFD38C","#8CEBFF","#FF8F9D","#6BF3AD","#C4FF8E","#FFF88D","#FFD38C","#8CEBFF","#FF8F9D","#6BF3AD","#C4FF8E","#FFF88D","#FFD38C","#8CEBFF","#FF8F9D","#6BF3AD","#C4FF8E","#FFF88D","#FFD38C","#8CEBFF","#FF8F9D","#6BF3AD","#C4FF8E","#FFF88D","#FFD38C","#8CEBFF","#FF8F9D","#6BF3AD","#C4FF8E","#FFF88D","#FFD38C","#8CEBFF","#FF8F9D","#6BF3AD"};	
		/**
		 * 
		 * @param barChart 柱状图表
		 * @param barData  要填充的数据
		 * @param XDescription X轴标签描述,默认为null
		 * @param isXAxislean X轴标签倾斜角度 0 为不倾斜
		 */
	public static void showBarChart(BarChart barChart, BarData barData,String XDescription,float angle) {
			// 数据描述      
	        barChart.setDescription(XDescription);
	        // 如果没有数据的时候，会显示这个，类似ListView的EmptyView
	        barChart.setNoDataTextDescription("You need to provide data for the chart.");
	        // 是否显示表格颜色  
	        barChart.setDrawGridBackground(false);  
	        // 设置是否可以触摸
	        barChart.setTouchEnabled(true); 
	        // 是否可以拖拽
	        barChart.setDragEnabled(false);     
	        // 是否可以缩放
	        barChart.setScaleEnabled(false);
	        // 集双指缩放
	        barChart.setPinchZoom(false);
	        // 设置背景
	        barChart.setBackgroundColor(Color.parseColor("#01000000"));
	        // 如果打开，背景矩形将出现在已经画好的绘图区域的后边。
	        barChart.setDrawGridBackground(false);
	        // 集拉杆阴影
	        barChart.setDrawBarShadow(false);
	        // 图例
	        barChart.getLegend().setEnabled(true);
	        // 设置数据
//	        barChart.setData(barData); 
	        
	        // 隐藏右边的坐标轴 (就是右边的0 - 100 - 200 - 300 ... 和图表中横线)
	        barChart.getAxisRight().setEnabled(false);
	 		// 隐藏左边的左边轴 (同上)
//	        barChart.getAxisLeft().setEnabled(false);
	        
	        // 网格背景颜色
	        barChart.setGridBackgroundColor(Color.parseColor("#00000000"));
	        // 是否显示表格颜色
	        barChart.setDrawGridBackground(false);
	        // 设置边框颜色
//	        barChart.setBorderColor(Color.parseColor("#00000000"));
	        barChart.setBorderColor(Color.parseColor("#FFFFFF"));
	        // 说明颜色
	        barChart.setDescriptionColor(Color.parseColor("#FFFF0000"));
//	        barChart.setDescriptionColor(Color.parseColor("#ffffff"));
	        // 拉杆阴影
	        barChart.setDrawBarShadow(false);
	        // 打开或关闭绘制的图表边框。（环绕图表的线）
	        barChart.setDrawBorders(false);
	    
	        barChart.setExtraOffsets(10,10,10, 20);	        //设置左上右下边界,防止标签遮盖
	        Legend mLegend = barChart.getLegend(); // 设置比例图标示
	        mLegend.setEnabled(false);
//	        // 设置窗体样式
//	        mLegend.setForm(LegendForm.CIRCLE);
//	        // 字体
//	        mLegend.setFormSize(4f);
	        // 字体颜色
//	        mLegend.setTextColor(Color.parseColor("#00000000"));
//		     Legend mLegend=  barChart.getLegend();
//		      mLegend.setPosition(LegendPosition.BELOW_CHART_CENTER);
//		      mLegend.setTextSize(10f);
//		      mLegend.setFormSize(10f); // set the size of the legend forms/shapes
//		      mLegend.setForm(LegendForm.SQUARE);//设置图例形状， SQUARE(方格) CIRCLE（圆形）
//		      List<String> string = new ArrayList<String>();
//		      string.add("考勤");
//		      mLegend.setComputedLabels(string);
	        
	        
	        XAxis xAxis = barChart.getXAxis();
			xAxis.setPosition(XAxisPosition.BOTTOM);
			xAxis.setDrawGridLines(false);
			xAxis.setSpaceBetweenLabels(2);//设置标签之间的间距
			xAxis.setTextSize(10f);
			xAxis.setTextColor(Color.GRAY);//设置X轴标签字体颜色
			xAxis.setLabelRotationAngle(angle);//设置X轴标签字体角度
			xAxis.setTypeface(Typeface.SANS_SERIF);
			xAxis.setLabelsToSkip(0);//当X轴标签数多的时候跳过显示数,设为0全部显示
			
			  //格式化柱形图的值,去掉小数点后面的0
		       final DecimalFormat  mFormat = new DecimalFormat("#");
		        barData.setValueFormatter(new ValueFormatter() {
					
					@Override
					public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
						// TODO Auto-generated method stub
						return mFormat.format(value);
					}
				});
			barChart.setData(barData); 
	        barChart.animateY(1000); // 立即执行的动画,Y轴
	    }
	
	
	
	   /**
	    * 模板数据,无用
	    * @param count
	    * @param range
	    * @return
	    */
	    public BarData getBarData(int count, float range) {
	        ArrayList<String> xValues = new ArrayList<String>();
	        for (int i = 0; i < count; i++) {
	            xValues.add(""+(i+1)+ PublicUtils.getResourceString(SoftApplication.context, R.string.attendance_shedul_week));// 设置每个壮图的文字描述
	        }
	        ArrayList<BarEntry> yValues = new ArrayList<BarEntry>();
	        for (int i = 0; i < count; i++) {
	            float value = (float) (Math.random() * range/*100以内的随机数*/) + 3;
	            yValues.add(new BarEntry(value, i));
	        }
	        // y轴的数据集合      
	        BarDataSet barDataSet = new BarDataSet(yValues,"");
	  
	        ArrayList<Integer> colors = new ArrayList<Integer>();
	        for(int i = 0;i < count ;i++){
	        	colors.add(Color.parseColor("#FFFFFF"));//柱形图的填充色
	        }
	        barDataSet.setColors(colors);
	        // 设置栏阴影颜色
	        barDataSet.setBarShadowColor(Color.parseColor("#01000000"));
	        ArrayList<BarDataSet> barDataSets = new ArrayList<BarDataSet>();
	        barDataSets.add(barDataSet);
	        barDataSet.setValueTextColor(Color.parseColor("#FF8F9D"));
	        barDataSet.setBarSpacePercent(20f);//设置柱子之间的间距
	        // 绘制值
	        barDataSet.setDrawValues(true);
//	        barDataSet.set
	        BarData barData = new BarData(xValues, barDataSet);
	        barData.setValueTextSize(20f);//设置Y轴值得字体大小,也可设置其颜色,格式(整数);
	        
	        return barData;
	    }
		

}
