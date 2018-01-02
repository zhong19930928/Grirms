package com.yunhu.yhshxc.nearbyVisit.comp;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.database.DictDB;
import com.yunhu.yhshxc.widget.DropDown;
import com.yunhu.yhshxc.widget.DropDown.OnResultListener;

public class NearbyCompSpinner extends AbsNearbyComp implements OnResultListener{

	private DropDown spinner;
	private List<Dictionary> srcDictList;
	private Dictionary selectDic;
	private OnSearchListner searchListner;
	public NearbyCompSpinner(Context mContext,OnSearchListner searchListner) {
		super(mContext);
		srcDictList = new ArrayList<Dictionary>();
		this.searchListner = searchListner;
	}

	@Override
	protected View contentView() {
		View view = View.inflate(context, R.layout.nearby_comp_spinner, null);
		spinner = (DropDown)view.findViewById(R.id.sp_nearby_comp);
		spinner.setOnResultListener(this);
		String name = getNearbyStyle().getName();
		if (!TextUtils.isEmpty(name)){
			spinner.refreshComp(name);
		}

//		spinner.setSelection(position);
		initData();
		return view;
	}
	
	/**
	 * 初始化数据
	 */
	private void initData(){
		Func func = this.getNearbyStyle().getFunc();
		if (func !=null) {
			srcDictList = new DictDB(context).findDictList(func.getDictTable(),func.getDictCols(),func.getDictDataId(),func.getDictOrderBy(),func.getDictIsAsc(),null,null,null);
			spinner.setSrcDictList(srcDictList);
		}
	}
	
	@Override
	public String getValue() {
		if (selectDic!=null) {
			return selectDic.getDid();
		}else{
			return super.getValue();
		}
	}
	
	public void setValueSelected(String did){
		if(srcDictList!=null){
			for(int i = 0; i<srcDictList.size(); i++){
				selectDic= srcDictList.get(i);
				if(selectDic.getDid().equals(did)){
					spinner.setSelected(selectDic);
					spinner.refreshComp(selectDic.getCtrlCol());
					break;
				}
			}
		}
		
	}

	@Override
	public void onResult(List<Dictionary> result) {
		if (result!=null && !result.isEmpty()) {
			selectDic = result.get(0);
			searchListner.startToSearch();
		}
	}
	
	public interface OnSearchListner{
		public void startToSearch();
	}
}
