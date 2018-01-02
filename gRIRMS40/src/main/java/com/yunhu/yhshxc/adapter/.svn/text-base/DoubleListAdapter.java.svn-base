package com.yunhu.yhshxc.adapter;


import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yunhu.yhshxc.bo.DoubleTask;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.comp.DoubleListItem;

/**
 * 
 * @author jishen 公告列表适配器
 */
public class DoubleListAdapter extends BaseAdapter {
	private Context context;
	private List<DoubleTask> doubleList;
	private int currentPos;
	private Integer targetId;
	private Func func;
	
	public DoubleListAdapter(Context context,Func func, List<DoubleTask> doubleList,Integer targetId) {
		this.context = context;
		this.doubleList = doubleList;
		this.targetId=targetId;
		this.func = func;
	}

	/**
	 * 返回所有任务的总数
	 */
	public int getCount() {
		return doubleList.size();
	}

	public Object getItem(int pos) {
		return 0;
	}

	public long getItemId(int pos) {
		return pos;
	}

	public View getView(final int pos, View v, ViewGroup p) {
		DoubleTask currentDoubleTask = doubleList.get(pos);
		DoubleListItem item=new DoubleListItem(context);
		View currentView=item.getView();
		if(!TextUtils.isEmpty(currentDoubleTask.getFirstColumn())&&currentDoubleTask.getFirstColumn().length()>20){
			item.setContent(currentDoubleTask.getFirstColumn().substring(0,20)+"...");
		}else{
			item.setContent(currentDoubleTask.getFirstColumn());
		}
		item.setDate(currentDoubleTask.getCreateTime());
		item.setCreater(currentDoubleTask.getCreateUser());
		return currentView;
	}

	public List<DoubleTask> getDoubleTaskList() {
		return doubleList;
	}

	public void setDoubleTaskList(List<DoubleTask> doubleList) {
		this.doubleList = doubleList;
	}

	
	
	public int getPos(){
		return currentPos;
	}
}
