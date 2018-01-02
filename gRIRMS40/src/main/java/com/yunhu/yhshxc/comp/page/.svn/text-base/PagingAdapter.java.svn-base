package com.yunhu.yhshxc.comp.page;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yunhu.yhshxc.R;

/**
 * AbsSearchListActivity中翻页栏页面跳转ListView的adapter
 * 
 */
public class PagingAdapter extends BaseAdapter{
	/**
	 * 页码数组
	 */
	private String []dataSrc = null;
	private Context context = null;
	
	/**
	 * 保存页码TextView的List
	 */
	private List<View> itemViewList = null;
	
	public PagingAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return dataSrc != null ? dataSrc.length : 0;
	}

	@Override
	public Object getItem(int position) {
		return dataSrc != null ? dataSrc[position] : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	/**
	 * 设置选项的背景色，选中项突出显示
	 * @param position 选中项在adapter中的位置
	 */
	public void setItemViewSelected(int position) {
		if(itemViewList != null){
			int i = 0;
			for(View view : itemViewList){
				if(i == position){
					view.setBackgroundColor(context.getResources().getColor(R.color.deep_bule));
				}else{
					view.setBackgroundColor(android.graphics.Color.WHITE);
				}
				i++;
			}
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView pagingItem = (TextView)itemViewList.get(position);
		pagingItem.setText(dataSrc[position]);
		return pagingItem;
	}

	/**
	 * 获取页码数组
	 * @return 返回页码数组
	 */
	public String[] getDataSrc() {
		return dataSrc;
	}

	/**
	 * 设置页码数据
	 * @param dataSrc 页码数组
	 */
	public void setDataSrc(String[] dataSrc) {
		if(dataSrc != null){
			itemViewList = new ArrayList<View>(dataSrc.length);
			for(int i=0; i<dataSrc.length; i++){
				itemViewList.add(View.inflate(context, R.layout.paging_list_item, null));
			}
		}
		this.dataSrc = dataSrc;
	}
}
