package com.yunhu.yhshxc.activity.carSales.scene.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.carSales.bo.Arrears;
import com.yunhu.yhshxc.activity.carSales.bo.CarSales;
import com.yunhu.yhshxc.activity.carSales.scene.view.ChargeArrearsItemView.OnCheckedIdOver;
import com.yunhu.yhshxc.utility.PublicUtils;

public class ChargeArrearsView implements OnCheckedIdOver{
	private View view;
	private Context context;
	private LinearLayout ll_item;
	private TextView tv_qiankuan;
	private OnCheckedIdOver onCheck;
	private List<ChargeArrearsItemView> list = new ArrayList<ChargeArrearsItemView>();
	private double subInput;//剩余收款
	private boolean isLast;
	public double getSubInput() {
		return subInput;
	}
	public void setSubInput(double subInput,boolean isLast) {
		this.subInput = subInput;
		this.isLast = isLast;
	}
	public ChargeArrearsView(Context context,OnCheckedIdOver onCheck){
		this.context = context;
		this.onCheck = onCheck;
		view = View.inflate(context, R.layout.car_sales_charge_arrears_list, null);
		tv_qiankuan = (TextView) view.findViewById(R.id.tv_qiankuan);
		ll_item = (LinearLayout) view.findViewById(R.id.ll_item);
	}
	public View getView(){
		view.setTag(this);
		return view;
	}
	Map<Integer,String> map = new HashMap<Integer,String>();
	public void initData(CarSales data){
		map.clear();
		List<Arrears> arrears = data.getArrears();
		if(!arrears.isEmpty()){
			ll_item.removeAllViews();
			list = new ArrayList<ChargeArrearsItemView>();
			for(int i = 0; i<arrears.size(); i++){
				ChargeArrearsItemView item = new ChargeArrearsItemView(context, onCheck);
				if(TextUtils.isEmpty(map.get(i))||map.get(i).equalsIgnoreCase("0")||map.get(i).equalsIgnoreCase("0.0")){
					if(isLast){
						if(i == arrears.size() -1){
							item.setSubInput(subInput,true);
						}else{
							item.setSubInput(subInput,false);
						}
					}else{
						item.setSubInput(subInput,false);
					}
					carTotal(i,arrears);
				}else{
					double sub = Double.parseDouble(map.get(i));
					if(isLast){
						if(i == arrears.size() -1){
							item.setSubInput(sub,true);
						}else{
							item.setSubInput(sub,false);
						}
					}else{
						item.setSubInput(sub,false);
					}
				}
				item.initData(arrears.get(i),data.getStoreName());
				list.add(item);
				ll_item.addView(item.getView());
			}
		}
		tv_qiankuan.setText("总金额:"+PublicUtils.formatDouble(data.getUnPayAmount()));
	}
	private void carTotal(int position,List<Arrears> arrears) {
		if(TextUtils.isEmpty(map.get(position))||map.get(position).equalsIgnoreCase("0")||map.get(position).equalsIgnoreCase("0.0")){
			map.put(position, String.valueOf(subInput));
			Arrears car = arrears.get(position);
			double carTotal = car.getArrearsAmount();
			double tempInput = subInput-carTotal;
			subInput = tempInput<=0 ? 0:tempInput;
		}
			
	}
	@Override
	public void onChecked(boolean isChecked, Arrears data) {
		onCheck.onChecked(isChecked, data);
		
	}
	@Override
	public void onSkChanged(Editable s, Arrears data) {
		onCheck.onSkChanged(s, data);
		
	}
	@Override
	public void onSingleChanged(boolean isSingle, CharSequence s) {
		onCheck.onSingleChanged(isSingle, s);
	}

}
