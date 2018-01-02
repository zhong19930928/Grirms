package com.yunhu.yhshxc.nearbyVisit;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.LocationResult;
import com.yunhu.yhshxc.database.FuncDB;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.location.ReceiveLocationListener;
import com.yunhu.yhshxc.location2.LocationFactoy;
import com.yunhu.yhshxc.nearbyVisit.bo.NearbyStyle;
import com.yunhu.yhshxc.nearbyVisit.comp.AbsNearbyComp;
import com.yunhu.yhshxc.nearbyVisit.comp.NearbyCompDate;
import com.yunhu.yhshxc.nearbyVisit.comp.NearbyCompDistanceSpinner;
import com.yunhu.yhshxc.nearbyVisit.comp.NearbyCompDistanceSpinner.OnLocation;
import com.yunhu.yhshxc.nearbyVisit.comp.NearbyCompEdit;
import com.yunhu.yhshxc.nearbyVisit.comp.NearbyCompEditRange;
import com.yunhu.yhshxc.nearbyVisit.comp.NearbyCompMultipleSpinner;
import com.yunhu.yhshxc.nearbyVisit.comp.NearbyCompMultipleSpinner.OnMultiListner;
import com.yunhu.yhshxc.nearbyVisit.comp.NearbyCompSpinner;
import com.yunhu.yhshxc.nearbyVisit.comp.NearbyCompSpinner.OnSearchListner;
import com.yunhu.yhshxc.utility.SharedPreferencesUtilForNearby;
import com.yunhu.yhshxc.widget.GCGScrollView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gcg.org.debug.JLog;

//import com.gcg.grirms.location.LocationFactory;

public class NearbyVisitLeftMenu implements ReceiveLocationListener,OnLocation,OnSearchListner,OnMultiListner{
	private View view;
	private LinearLayout ll_nearby_left_menu;
	private Context context;
	private NearbyVisitActivity nearbyVisitActivity;
	private Button nearby_left_menu_search;
	private List<AbsNearbyComp> nearbyCompList;
	private GCGScrollView gcgScrollView;
	public NearbyVisitLeftMenu(Context mContext) {
		this.context = mContext;
		nearbyCompList = new ArrayList<AbsNearbyComp>();
		nearbyVisitActivity = (NearbyVisitActivity)mContext;
		view = View.inflate(mContext, R.layout.nearby_visit_left_menu, null);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		gcgScrollView = (GCGScrollView)view.findViewById(R.id.sv_nearby_left_menu);
		ll_nearby_left_menu = (LinearLayout)view.findViewById(R.id.ll_nearby_left_menu);
		nearby_left_menu_search = (Button)view.findViewById(R.id.nearby_left_menu_search);
		nearby_left_menu_search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				search();
			}
		});
		initLeftMenu();
		initNearbyDistance();
	}
	
	public NearbyCompDistanceSpinner distanceSpinner;
	private void initNearbyDistance(){
		LinearLayout ll = (LinearLayout)view.findViewById(R.id.ll_nearby_comp_distance);
		distanceSpinner = new NearbyCompDistanceSpinner(context,this);
		View distanceView = distanceSpinner.getView();
		distanceView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		ll.addView(distanceView);
	}
	
	public void initLeftMenu(){
		List<NearbyStyle> styleList = parderNearbyStyle();
		if (!styleList.isEmpty()) {
			ll_nearby_left_menu.removeAllViews();
			for (int i = 0; i < styleList.size(); i++) {
				AbsNearbyComp nearbyComp = null;
				NearbyStyle style = styleList.get(i);
				switch (style.getType()) {
				case NearbyStyle.NEARBY_STYLE_EDIT:
					nearbyComp = new NearbyCompEdit(context);
					break;
				case NearbyStyle.NEARBY_STYLE_EDIT_RANGE:
					nearbyComp = new NearbyCompEditRange(context);
					break;
				case NearbyStyle.NEARBY_STYLE_SPINNER:
					nearbyComp = new NearbyCompSpinner(context,this);
					break;
				case NearbyStyle.NEARBY_STYLE_MULTI_SPINNER:
					nearbyComp = new NearbyCompMultipleSpinner(context,this);
					break;
				case NearbyStyle.NEARBY_STYLE_DATE:
					nearbyComp = new NearbyCompDate(context);
					break;
				default:
					break;
				}
				if (nearbyComp!=null) {
					nearbyComp.setNearbyStyle(style,true);
					ll_nearby_left_menu.addView(nearbyComp.getView());
					nearbyCompList.add(nearbyComp);
				}
			}
		}
		gcgScrollView.setViewTouchMode(true);
	}
	
	private List<NearbyStyle> parderNearbyStyle(){
		List<NearbyStyle> list = new ArrayList<NearbyStyle>();
		try {
			String style = SharedPreferencesUtilForNearby.getInstance(context).getNearbyStyle(nearbyVisitActivity.menuId);
			if (!TextUtils.isEmpty(style)) {
				JSONArray array = new JSONArray(style);
				NearbyStyle nearbyStyle = null;
				for (int i = 0; i < array.length(); i++) {
					nearbyStyle = new NearbyStyle();
					JSONArray arr = array.getJSONArray(i);
					String mark = arr.getString(0);
					String name = arr.getString(1);
					nearbyStyle.setMark(mark);
					nearbyStyle.setName(name);
					if ("prop1".equals(mark) || "prop2".equals(mark) || "prop3".equals(mark) || "prop4".equals(mark)) {
						nearbyStyle.setType(NearbyStyle.NEARBY_STYLE_EDIT);
					}else{
//						String funcid = mark.split("_")[1];
//						Func func = new FuncDB(context).findFuncByFuncId(Integer.parseInt(funcid));
						Func func = new FuncDB(context).findFuncByFuncId(Integer.parseInt(mark.split("_")[1]));
						if (func!=null) {
							if (func.getType() == Func.TYPE_EDITCOMP) {
								if ((func.getCheckType()!=null && func.getCheckType() == Func.CHECK_TYPE_NUMERIC) || (func.getDataType()!=null &&(func.getDataType() == Func.DATATYPE_BIG_INTEGER || func.getDataType() == Func.DATATYPE_SMALL_INTEGER || func.getDataType() == Func.DATATYPE_DECIMAL))) {
									nearbyStyle.setType(NearbyStyle.NEARBY_STYLE_EDIT_RANGE);//如果是数字类型的话就是范围的
								}else{
									nearbyStyle.setType(NearbyStyle.NEARBY_STYLE_EDIT);
								}
							}else if(func.getType() == Func.TYPE_SELECTCOMP || func.getType() == Func.TYPE_SELECT_OTHER || func.getType() == Func.TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY_COMP ){//不包含组织机构和店面
								if (func.getOrgOption()!=null) {
									continue;
								}
								nearbyStyle.setType(NearbyStyle.NEARBY_STYLE_SPINNER);
							}else if(func.getType() == Func.TYPE_MULTI_CHOICE_SPINNER_COMP || func.getType() == Func.TYPE_MULTI_CHOICE_FUZZY_QUERY_COMP){
								nearbyStyle.setType(NearbyStyle.NEARBY_STYLE_MULTI_SPINNER);
							}else if(func.getType() == Func.TYPE_DATEPICKERCOMP){
								nearbyStyle.setType(NearbyStyle.NEARBY_STYLE_DATE);
							}else{
								continue;
							}
							nearbyStyle.setFunc(func);
						}else{
							continue;
						}
					}
					list.add(nearbyStyle);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public View getView(){
		return view;
	}
	
	public void search(){
		if (null!=distanceSpinner&&!TextUtils.isEmpty(distanceSpinner.getValue()) && TextUtils.isEmpty(distanceSpinner.getLonLat())) {
			location();
		}else{
			String searchParam = searchParam();
			if (!"$$".equals(searchParam)) {
				if (nearbyVisitActivity!=null) {
					nearbyVisitActivity.search(searchParam());
				}	
			}
		}
	}
	
	public String searchParam(){
		String paramJson = "";
		StringBuffer buf = new StringBuffer();
		Map<String, String> param = new HashMap<String, String>();
		if (!nearbyCompList.isEmpty()) {
			for (int i = 0; i < nearbyCompList.size(); i++) {
				AbsNearbyComp comp = nearbyCompList.get(i);
				NearbyStyle style = comp.getNearbyStyle();
				String value = comp.getValue();
				if (!TextUtils.isEmpty(value)) {
					if (comp instanceof NearbyCompDate) {//日期类型的验证日期范围
						String[] date = value.split("~@@");
						String start = date[0];
						String end = date[1];
						if (start.compareTo(end) == 0) {
							param.put(style.getMark(), start);
							buf.append(",").append(style.getMark());
						}else if(start.compareTo(end) < 0){
							param.put(style.getMark()+"_from", start);
							param.put(style.getMark()+"_to", end);
						}else if(start.compareTo(end) > 0){
							Toast.makeText(context, style.getName()+setString(R.string.nearby_visit_07), Toast.LENGTH_SHORT).show();
							return "$$";
						}
					}else if(comp instanceof NearbyCompEditRange){//范围类型的输入框要验证
						NearbyCompEditRange editComp = (NearbyCompEditRange)comp;
						String startValue = editComp.startValue();
						String endValue = editComp.endValue();
						if (!TextUtils.isEmpty(startValue) && !TextUtils.isEmpty(endValue)) {
							int start = Integer.parseInt(startValue);
							int end = Integer.parseInt(endValue);
							if(start > end){
								Toast.makeText(context, style.getName()+setString(R.string.nearby_visit_07), Toast.LENGTH_SHORT).show();
								return "$$";
							}else if(start == end){
								param.put(style.getMark(), startValue);
							}else{
								param.put(style.getMark()+"_from", startValue);
								param.put(style.getMark()+"_to", endValue);
							}
						}else{
							if (!TextUtils.isEmpty(startValue)) {
								param.put(style.getMark()+"_from", startValue);
							}
							if (!TextUtils.isEmpty(endValue)) {
								param.put(style.getMark()+"_to", endValue);
							}
						}
						
					}else{
						param.put(style.getMark(), value);
					}
				}
			}
		}
		paramJson = new JSONObject(param).toString();
		return paramJson;
	}
	
	private Dialog locationLoadingDialog;
	private void location(){
		locationLoadingDialog  = new MyProgressDialog(context,R.style.CustomProgressDialog,setString(R.string.nearby_visit_05));
		locationLoadingDialog.show();
//		new LocationFactory(context).startNewLocation(this,false);//不需要地址
		new LocationFactoy(context, this).startLocationHH();
	}

	@Override
	public void onReceiveResult(LocationResult result) {
		if (locationLoadingDialog!=null && locationLoadingDialog.isShowing()) {
			locationLoadingDialog.dismiss();
		}
		try {
			if (result!=null && result.isStatus()) {
				String lon = String.valueOf(result.getLongitude());
				String lat = String.valueOf(result.getLatitude());
				distanceSpinner.setLonLat(lon+","+lat);
				JLog.d("就近拜访经纬度"+lon+","+lat);
				search();
			}else{
				Toast.makeText(context, setString(R.string.nearby_visit_06), Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			JLog.e(e);
			Toast.makeText(context, setString(R.string.nearby_visit_06), Toast.LENGTH_SHORT).show();
		}
		
	}

	@Override
	public void startToLocation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startToSearch() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMultiSearch() {
		// TODO Auto-generated method stub
		
	}
	private String setString(int stringId){
		return context.getResources().getString(stringId);
	}
}
