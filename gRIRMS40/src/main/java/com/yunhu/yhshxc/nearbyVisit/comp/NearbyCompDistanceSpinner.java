package com.yunhu.yhshxc.nearbyVisit.comp;

import android.content.Context;
import android.view.View;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.widget.DropDown;
import com.yunhu.yhshxc.widget.DropDown.OnResultListener;

import java.util.ArrayList;
import java.util.List;

public class NearbyCompDistanceSpinner extends AbsNearbyComp implements OnResultListener{
	
	private DropDown spinner;
	
	private String dictance;
	private String lonLat;
	private OnLocation location;
	private Context context;
	
	public NearbyCompDistanceSpinner(Context mContext,OnLocation location) {
		super(mContext);
		this.location = location;
		this.context = mContext;
	}


	public String getLonLat() {
		return lonLat;
	}


	public void setLonLat(String lonLat) {
		this.lonLat = lonLat;
	}


	@Override
	public View getView() {
		View view = View.inflate(context, R.layout.nearby_comp_distance_spinner, null);
		spinner = (DropDown)view.findViewById(R.id.sp_nearby_comp_distance);
		spinner.setOnResultListener(this);
		
		initDistanceData();
		return view;
	}
	
	@Override
	public String getValue() {
		return dictance;
	}

	public void initDistanceData(){
		List<Dictionary> dic = new ArrayList<Dictionary>();
		Dictionary d_1 = new Dictionary();
		d_1.setDid("0");
		d_1.setCtrlCol(setString(R.string.nearby_visit_24));
		Dictionary d_2 = new Dictionary();
		d_2.setDid("500");
		d_2.setCtrlCol("500"+setString(R.string.nearby_visit_meter));
		Dictionary d_3 = new Dictionary();
		d_3.setDid("1000");
		d_3.setCtrlCol("1000"+setString(R.string.nearby_visit_meter));
		Dictionary d_4 = new Dictionary();
		d_4.setDid("1500");
		d_4.setCtrlCol("1500"+setString(R.string.nearby_visit_meter));
		Dictionary d_5 = new Dictionary();
		d_5.setDid("2000");
		d_5.setCtrlCol("2000"+setString(R.string.nearby_visit_meter));
		Dictionary d_6 = new Dictionary();
		d_6.setDid("3000");
		d_6.setCtrlCol("3000"+setString(R.string.nearby_visit_meter));
		Dictionary d_7 = new Dictionary();
		d_7.setDid("5000");
		d_7.setCtrlCol("5000"+setString(R.string.nearby_visit_meter));
		dic.add(d_1);
		dic.add(d_2);
		dic.add(d_3);
		dic.add(d_4);
		dic.add(d_5);
		dic.add(d_6);
		dic.add(d_7);
		spinner.setSrcDictList(dic);
		spinner.setSelected(d_1);
	}


	@Override
	public void onResult(List<Dictionary> result) {
		if (result!=null && !result.isEmpty()) {
			Dictionary dic = result.get(0);
			if("500".equals(dic.getDid())){
				dictance = "500";
			}else if("1000".equals(dic.getDid())){
				dictance = "1000";
			}else if("1500".equals(dic.getDid())){
				dictance = "1500";
			}else if("2000".equals(dic.getDid())){
				dictance = "2000";
			}else if("3000".equals(dic.getDid())){
				dictance = "3000";
			}else if("5000".equals(dic.getDid())){
				dictance = "5000";
			}else{
				dictance = null;
				setLonLat(null);
			}
			location.startToLocation();
		}
		
	}
	
	public interface OnLocation{
		public void startToLocation();
	}
	private String setString(int stringId){
		return context.getResources().getString(stringId);
	}
	
}
