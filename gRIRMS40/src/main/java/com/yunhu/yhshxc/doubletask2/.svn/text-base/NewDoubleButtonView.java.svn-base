package com.yunhu.yhshxc.doubletask2;

import java.util.Map;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;

public class NewDoubleButtonView {
	private View view = null;
	private LinearLayout ll_btn1,ll_btn2,ll_btn3,ll_btn4;
	private TextView tv_1,tv_2,tv_3,tv_4;
	private Map<String, String> mainBtnMap,dynamicBtnMap;
	private boolean isShowBtn = false;//判断指定节点下是否有按钮显示 true 有按钮 false 没有按钮显示
	public NewDoubleButtonView(Context context,OnClickListener listener) {
		view = View.inflate(context, R.layout.new_double_btn_view, null);
		ll_btn1 = (LinearLayout)view.findViewById(R.id.ll_new_double_detail_btn1);
		ll_btn2 = (LinearLayout)view.findViewById(R.id.ll_new_double_detail_btn2);
		ll_btn3 = (LinearLayout)view.findViewById(R.id.ll_new_double_detail_btn3);
		ll_btn4 = (LinearLayout)view.findViewById(R.id.ll_new_double_detail_btn4);
		tv_1 = (TextView)view.findViewById(R.id.tv_new_double_detail_btn1);
		tv_2 = (TextView)view.findViewById(R.id.tv_new_double_detail_btn2);
		tv_3 = (TextView)view.findViewById(R.id.tv_new_double_detail_btn3);
		tv_4 = (TextView)view.findViewById(R.id.tv_new_double_detail_btn4);
		ll_btn1.setOnClickListener(listener);
		ll_btn2.setOnClickListener(listener);
		ll_btn3.setOnClickListener(listener);
		ll_btn4.setOnClickListener(listener);
	}
	
	public void initButtonData(String dataStatus){
		
		if (!TextUtils.isEmpty(dataStatus)) {
			isShowBtn = true;
			if ("1".equals(dataStatus)) {//节点是 1 显示接受和取消按钮  即 1 和 4 
				if (mainBtnMap.containsKey("1")) {
					ll_btn3.setVisibility(View.VISIBLE);
					ll_btn3.setId(1);//接受
					tv_3.setText(mainBtnMap.get("1"));
				}
				if (mainBtnMap.containsKey("4")) {
					ll_btn4.setVisibility(View.VISIBLE);
					ll_btn4.setId(4);//取消
					tv_4.setText(mainBtnMap.get("4"));
				}
			}else if("2".equals(dataStatus)){//节点是 2 的 显示 上报 修改 取消 中止 即 2 3 4 5
				
				if (mainBtnMap.containsKey("2")) {
					ll_btn1.setVisibility(View.VISIBLE);
					ll_btn1.setId(2);//上报
					tv_1.setText(mainBtnMap.get("2"));
				}
				
				if (mainBtnMap.containsKey("3")) {
					ll_btn2.setVisibility(View.VISIBLE);
					ll_btn2.setId(3);//修改
					tv_2.setText(mainBtnMap.get("3"));
				}
				
				//状态大于1就不显示取消
//				if (mainBtnMap.containsKey("4")) {
//					ll_btn3.setVisibility(View.VISIBLE);
//					ll_btn3.setId(4);//取消
//					tv_3.setText(mainBtnMap.get("4"));
//				}
				
				if (mainBtnMap.containsKey("5")) {
					ll_btn4.setVisibility(View.VISIBLE);
					ll_btn4.setId(5);//中止
					tv_4.setText(mainBtnMap.get("5"));
				}
				
			}else{//显示动态按钮
				String dynamicBtnText = dynamicBtnMap.get(dataStatus);
				if (!TextUtils.isEmpty(dynamicBtnText)) {
					ll_btn4.setVisibility(View.VISIBLE);
					ll_btn4.setId(-99);//动态按钮
					tv_4.setText(dynamicBtnText);
				}
			}
		}else{
			isShowBtn = false;
		}
	}
	
	
	
	public boolean isShowBtn() {
		return isShowBtn;
	}

	public View getView(){
		return view;
	}

	public Map<String, String> getMainBtnMap() {
		return mainBtnMap;
	}

	public void setMainBtnMap(Map<String, String> mainBtnMap) {
		this.mainBtnMap = mainBtnMap;
	}

	public Map<String, String> getDynamicBtnMap() {
		return dynamicBtnMap;
	}

	public void setDynamicBtnMap(Map<String, String> dynamicBtnMap) {
		this.dynamicBtnMap = dynamicBtnMap;
	}
	
	
	
}
