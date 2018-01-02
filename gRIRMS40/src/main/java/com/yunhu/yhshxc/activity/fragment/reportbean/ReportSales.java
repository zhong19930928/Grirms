package com.yunhu.yhshxc.activity.fragment.reportbean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.utility.PublicUtils;

public class ReportSales implements OnClickListener{
	private View view;
	private Context context;
	private TextView personreport_normal_duty;//总金额
	private TextView personreport_normal_duty_title;
	private TextView personreport_late;//赢单个数
	private TextView personreport_late_title;
	private TextView personal_attendance_exception_days;//赢单金额
	private TextView personal_attendance_exception_days_title;
	private TextView report_monthtitle;//条目标题
	private TextView ll_one,ll_two,ll_three,ll_four,ll_five;
	
	private TextView tv_chuqi_money,tv_chuqi_count;
	private TextView tv_gz_m,tv_gz_count;
	private TextView tv_tb_m,tv_tb_count;
	private TextView tv_tp_m,tv_tp_count;
	private TextView tv_yd_m,tv_yd_count;
	
	private LinearLayout personreport_attendance_statistics,ll_content;
	
	public ReportSales(Context context){
		this.context = context;
		view = View.inflate(context, R.layout.reprot_item_loudou, null);
		personreport_normal_duty = (TextView) view.findViewById(R.id.personreport_normal_duty);
		personreport_late = (TextView) view.findViewById(R.id.personreport_late);
		personal_attendance_exception_days = (TextView) view.findViewById(R.id.personal_attendance_exception_days);
		tv_chuqi_money = (TextView) view.findViewById(R.id.tv_chuqi_money);		
		personreport_normal_duty_title = (TextView) view.findViewById(R.id.personreport_normal_duty_title);
		personreport_late_title = (TextView) view.findViewById(R.id.personreport_late_title);
		personal_attendance_exception_days_title = (TextView) view.findViewById(R.id.personal_attendance_exception_days_title);
		report_monthtitle = (TextView) view.findViewById(R.id.report_monthtitle);
		
		tv_chuqi_count = (TextView) view.findViewById(R.id.tv_chuqi_count);
		tv_gz_m = (TextView) view.findViewById(R.id.tv_gz_m);
		tv_gz_count = (TextView) view.findViewById(R.id.tv_gz_count);
		tv_tb_m = (TextView) view.findViewById(R.id.tv_tb_m);
		tv_tb_count = (TextView) view.findViewById(R.id.tv_tb_count);
		tv_tp_m = (TextView) view.findViewById(R.id.tv_tp_m);
		tv_tp_count = (TextView) view.findViewById(R.id.tv_tp_count);
		tv_yd_m = (TextView) view.findViewById(R.id.tv_yd_m);
		tv_yd_count = (TextView) view.findViewById(R.id.tv_yd_count);
		ll_one = (TextView) view.findViewById(R.id.ll_one);
		ll_two = (TextView) view.findViewById(R.id.ll_two);
		ll_three = (TextView) view.findViewById(R.id.ll_three);
		ll_four = (TextView) view.findViewById(R.id.ll_four);
		ll_five = (TextView) view.findViewById(R.id.ll_five);
		personreport_attendance_statistics = (LinearLayout) view.findViewById(R.id.personreport_attendance_statistics);
		ll_content = (LinearLayout) view.findViewById(R.id.ll_content);
		personreport_attendance_statistics.setOnClickListener(this);
		
	}
	private int first,tewth,third,forth,fifth;
	private String tString="";
	/**
	 *根据数据进行初始化
	 *
	 */
	public void initData(JSONObject jsobOne){
		TextView[] titles = { personreport_normal_duty_title, personreport_late_title, personal_attendance_exception_days_title };
		TextView[] showValues = { personreport_normal_duty, personreport_late, personal_attendance_exception_days };
		try {
			if (PublicUtils.isValid(jsobOne, "title")) {//条目标题
				tString=jsobOne.getString("title");
				report_monthtitle.setText(tString);
				
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

			TextView[] mames = {ll_one,ll_two,ll_three,ll_four,ll_five};
			TextView[] moneys = {tv_chuqi_money,tv_gz_m,tv_tb_m,tv_tp_m,tv_yd_m};
			TextView[] counts = {tv_chuqi_count,tv_gz_count,tv_tb_count,tv_tp_count,tv_yd_count};
			if ("1".equals(jsobOne.getString("isDetail"))) {
				JSONArray jsArr1 = jsobOne.getJSONArray("detail");
				JSONObject jsOb1 = jsArr1.getJSONObject(0);
				// JSONArray jsXYTip = jsOb1.getJSONArray("cols");
				JSONArray jsArr2 = jsOb1.getJSONArray("data");
				// 遍历
				for (int i = 0; i < jsArr2.length(); i++) {
					JSONArray jsArr3 = jsArr2.getJSONArray(i);
					mames[i].setText(jsArr3.getString(0));
					moneys[i].setText(jsArr3.getString(1)+PublicUtils.getResourceString(context,R.string.yuan));
					counts[i].setText("|"+jsArr3.getString(2)+PublicUtils.getResourceString(context,R.string.ge));
				}
				first = Integer.parseInt(jsArr2.getJSONArray(0).getString(2));
				tewth = Integer.parseInt(jsArr2.getJSONArray(1).getString(2));
				third = Integer.parseInt(jsArr2.getJSONArray(2).getString(2));
				forth = Integer.parseInt(jsArr2.getJSONArray(3).getString(2));
				fifth = Integer.parseInt(jsArr2.getJSONArray(4).getString(2));
			}
			sortCount(32, 7,first,tewth,third,forth,fifth);
		}catch(Exception e){
			
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * 
	 * @param middleSize  中间值 高度
	 * @param ladderValue  每次递减或递增的值 
	 */
	public void sortCount(int middleSize,int ladderValue,int first,int tweth,int third,int forth,int fifth){ 
		//声明  
				Map<String,Integer> hashMap = new HashMap<String,Integer>();  
				//向Map中添加数据  
				//.....  
				//转换  
				hashMap.put("one", first);
				hashMap.put("two", tweth);
				hashMap.put("three", third);
				hashMap.put("four", forth);
				hashMap.put("five", fifth);
				ArrayList<Entry<String, Integer>> arrayList = new ArrayList<Entry<String, Integer>>(  
				            hashMap.entrySet());  
				//排序  
				Collections.sort(arrayList, new Comparator<Map.Entry<String, Integer>>() {  
				    public int compare(Map.Entry<String, Integer> map1,  
				                Map.Entry<String, Integer> map2) {  
				        return (map2.getValue() - map1.getValue());  
				    }  
				});  
				//输出  
//				for (Entry<String, Integer> entry : arrayList) {  
//				    System.out.println(entry.getKey() + "\t" + entry.getValue());  
//				    
//				} 
			int a1 = 	arrayList.get(0).getValue();
			int a2 = 	arrayList.get(1).getValue();
			int a3 = 	arrayList.get(2).getValue();
			int a4 = 	arrayList.get(3).getValue();
			int a5 = 	arrayList.get(4).getValue();
			setHeight(arrayList.get(2).getKey(), middleSize);
			if(a3==a2){
				setHeight(arrayList.get(1).getKey(), middleSize);
				if(a2==a1){
					setHeight(arrayList.get(0).getKey(), middleSize);
				}else if(a2<a1){
					setHeight(arrayList.get(0).getKey(), middleSize+ladderValue);
				}
			}else if(a3<a2){
				setHeight(arrayList.get(1).getKey(), middleSize+ladderValue);
				if(a2==a1){
					setHeight(arrayList.get(0).getKey(), middleSize+ladderValue);
				}else if(a2<a1){
					setHeight(arrayList.get(0).getKey(), middleSize+ladderValue*2);
				}
			}
			if(a3==a4){
				setHeight(arrayList.get(3).getKey(), middleSize);
				if(a4==a5){
					setHeight(arrayList.get(4).getKey(), middleSize);
				}else if(a4>a5){
					setHeight(arrayList.get(4).getKey(), middleSize-ladderValue);
				}
			}else if(a3>a4){
				setHeight(arrayList.get(3).getKey(), middleSize-ladderValue);
				if(a4==a5){
					setHeight(arrayList.get(4).getKey(), middleSize-ladderValue);
				}else if(a4>a5){
					setHeight(arrayList.get(4).getKey(), middleSize-ladderValue*2);
				}
			}
	}
	public  void setHeight(String key,int value){
		if(key.equals("one")){
			LayoutParams param = new LayoutParams(LayoutParams.MATCH_PARENT, PublicUtils.convertDIP2PX(context, value));
			ll_one.setLayoutParams(param);
		}else if(key.equals("two")){
			LayoutParams param = new LayoutParams(LayoutParams.MATCH_PARENT, PublicUtils.convertDIP2PX(context, value));
			param.setMargins(PublicUtils.convertDIP2PX(context, 20), 0, PublicUtils.convertDIP2PX(context, 20), 0);
			ll_two.setLayoutParams(param);
		}else if(key.equals("three")){
			LayoutParams param = new LayoutParams(LayoutParams.MATCH_PARENT, PublicUtils.convertDIP2PX(context, value));
			param.setMargins(PublicUtils.convertDIP2PX(context, 40), 0, PublicUtils.convertDIP2PX(context, 40), 0);
			ll_three.setLayoutParams(param);
		}else if(key.equals("four")){
			LayoutParams param = new LayoutParams(LayoutParams.MATCH_PARENT, PublicUtils.convertDIP2PX(context, value));
			param.setMargins(PublicUtils.convertDIP2PX(context, 60), 0, PublicUtils.convertDIP2PX(context, 60), 0);
			ll_four.setLayoutParams(param);
		}else if(key.equals("five")){
			LayoutParams param = new LayoutParams(LayoutParams.MATCH_PARENT, PublicUtils.convertDIP2PX(context, value));
			param.setMargins(PublicUtils.convertDIP2PX(context, 80), 0, PublicUtils.convertDIP2PX(context, 80), 0);
			ll_five.setLayoutParams(param);
		}
		
	}
	
	public View getView(){
		return view;
	}
	private boolean isVisible;
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.personreport_attendance_statistics:
			if(isVisible){
				isVisible = false;
				ll_content.setVisibility(View.GONE);
			}else{
				isVisible = true;
				ll_content.setVisibility(View.VISIBLE);
			}
			break;

		default:
			break;
		}
		
	}
	
	
}
