package com.yunhu.yhshxc.activity.addressBook;

import java.util.List;

import com.yunhu.yhshxc.activity.addressBook.bo.AdressBookUser;
import com.yunhu.yhshxc.wechat.view.sortListView.SortModel;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;

public class ContractAdapter extends BaseAdapter implements SectionIndexer{
	private List<SortModel> list = null;
	private Context context;
	public ContractAdapter(Context context,List<SortModel> list){
		this.context = context;
		this.list = list;
	}
	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * @param list
	 */
	public void updateListView(List<SortModel> list){
		this.list = list;
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return this.list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AddressView view = null;
		if (convertView == null) {
			view = new AddressView(context);
			convertView = view.getView();
			convertView.setTag(view);
		} else {
			view = (AddressView) convertView.getTag();
		}
		final SortModel mContent = list.get(position);
		AdressBookUser user = mContent.getAdressBookUser();
		view.init(user);
		//根据position获取分类的首字母的Char ascii值
		int section = getSectionForPosition(position);
		
		//如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if(position == getPositionForSection(section)){
			view.getTextView().setVisibility(View.VISIBLE);
			view.getTextView().setText(mContent.getSortLetters());
		}else{
			view.getTextView().setVisibility(View.GONE);
		}
		return convertView;

	}
	

	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		return list.get(position).getSortLetters().charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		
		return -1;
	}
	
	/**
	 * 提取英文的首字母，非英文字母用#代替。
	 * 
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		String  sortStr = str.trim().substring(0, 1).toUpperCase();
		// 正则表达式，判断首字母是否是英文字母
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	@Override
	public Object[] getSections() {
		return null;
	}

}
