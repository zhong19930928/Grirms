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

public class NearbyCompMultipleSpinner extends AbsNearbyComp implements OnResultListener{

	private DropDown spinner;
	private List<Dictionary> srcDictList;
	private String value;
	private OnMultiListner multiListner;
	public NearbyCompMultipleSpinner(Context mContext,OnMultiListner multiListner) {
		super(mContext);
		srcDictList = new ArrayList<Dictionary>();
		this.multiListner = multiListner;
	}

	@Override
	protected View contentView() {
		View view = View.inflate(context, R.layout.nearby_comp_multiple_spinner, null);
		spinner = (DropDown)view.findViewById(R.id.sp_nearby_comp_multi);
		spinner.setOnResultListener(this);
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
			setSelectDate();
		}
	}
	
	private void setSelectDate(){
		if (!TextUtils.isEmpty(value)) {
			List<Dictionary> selectList = new ArrayList<Dictionary>();
			String[] select = value.split(",");
			for (int i = 0; i < select.length; i++) {
				for (int j = 0; j < srcDictList.size(); j++) {
					Dictionary dic = srcDictList.get(j);
					if (select[i].equals(dic.getDid())) {
						selectList.add(dic);
						break;
					}
				}
			}
			spinner.setSelected(selectList);
		}else{
			spinner.setSelected(new ArrayList<Dictionary>());
		}
	}
	
	/**
	 * 
	 * @return绑定adapter数据源
	 */
	public String[] getDateSrc(){
		if (srcDictList == null) return null;
		String[] contcol = new String[srcDictList.size()];
		int i = 0;
		for (Dictionary dict : srcDictList) {
			contcol[i] = dict.getCtrlCol()==null?"":dict.getCtrlCol();//获取资源列所有数据
			i++;
		}
		return contcol;
	}
	
	
	@Override
	public String getValue() {
		return value;
	}
	
	public void setValueSelected(String did){
//		if(srcDictList!=null){
//			for(int i = 0; i<srcDictList.size(); i++){
//				selectDic= srcDictList.get(i);
//				if(selectDic.getDid().equals(did)){
//					spinner.setSelected(selectDic);
//				}
//			}
//		}
		
	}
	
	@Override
	public void onResult(List<Dictionary> result) {
		if (result!=null && !result.isEmpty()) {
			StringBuffer str = new StringBuffer();
			for (int i = 0; i < result.size(); i++) {
				str.append(",").append(result.get(i).getDid());
			}
			value = str.substring(1);
			multiListner.onMultiSearch();
		}
	}
	
	public interface OnMultiListner{
		public void onMultiSearch();
	}
}
