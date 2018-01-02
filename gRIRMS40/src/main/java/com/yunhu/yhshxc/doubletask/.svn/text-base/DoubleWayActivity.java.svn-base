package com.yunhu.yhshxc.doubletask;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.adapter.DoubleListAdapter;
import com.yunhu.yhshxc.bo.DoubleTask;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.Module;
import com.yunhu.yhshxc.database.DoubleDB;
import com.yunhu.yhshxc.database.FuncDB;
import com.yunhu.yhshxc.widget.ToastOrder;

import java.util.List;

/**
 * 双向任务的列表activity
 * 以列表的形式展示出可以执行的双向任务
 * @author jishen
 * @version 2012-02-29
 *
 */
public class DoubleWayActivity extends AbsBaseActivity implements OnScrollListener,OnItemClickListener{

	/**
	 * 双向任务列表listview
	 */
	private ListView doubleListView;
	/**
	 *  分页加载视图底部View
	 */
	private View loadingView;
	/**
	 * 可执行的双向任务集合
	 */
	private List<DoubleTask> doubleList;
	/**
	 *  双向任务列表listview 的adapter
	 */
	public DoubleListAdapter doubleListAdapter;
	/**
	 * 双向任务表
	 */
	private DoubleDB doubleDB;
	/**
	 * listView分页表示最后一个的下标
	 */
	private int lastItem = 0;
	/**
	 * 数据ID
	 */
	private int targetId;
	/**
	 * title名称
	 */
	private TextView doubleHead;
	
	private boolean isNoWait;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.double_way_list);
		initBase();//父类的方法，目前没做处理
	}

	/**
	 * 初始化实例，获取传过来的数据
	 */
	private void init() {
		doubleDB=new DoubleDB(this);
		targetId=getIntent().getIntExtra("targetId", 0);//获取数据ID
		isNoWait = getIntent().getBooleanExtra("isNoWait", true);
		doubleHead=(TextView)findViewById(R.id.doubleHead);
		doubleHead.setText(getIntent().getStringExtra("doubleHead"));//获取title名称
		loadingView = LayoutInflater.from(this).inflate(R.layout.notify_list_loading, null);
		doubleListView = (ListView) findViewById(R.id.double_list_listView);
		doubleListView.setOnItemClickListener(this);
		Func func=new FuncDB(this).findFuncByFirstColumn(targetId);//查找第一列显示的func
		if(func!=null){//有显示内容的情况
			doubleList=doubleDB.findDoubleTaskList(String.valueOf(func.getFuncId()), targetId);//查找所有的双向任务
			doubleListAdapter=new DoubleListAdapter(this, func,doubleList,targetId);
			if(doubleList.isEmpty()){
				ToastOrder.makeText(this, getResources().getString(R.string.no_readable_content), ToastOrder.LENGTH_SHORT).show();
				this.finish();
			}else{
				if (doubleListView.getFooterViewsCount() == 0 && doubleList.size()>=20){
					doubleListView.addFooterView(loadingView);// 添加加载视图
				}
			}
			doubleListView.setAdapter(doubleListAdapter);
			doubleListView.setOnScrollListener(this);
		}else{//没有显示内容
			ToastOrder.makeText(this, getResources().getString(R.string.no_readable_content), ToastOrder.LENGTH_SHORT).show();
			this.finish();
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		init();
	}
	
	/**
	 * 当滑动到最底层的时候分页加载
	 */
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		List<DoubleTask> tempList = doubleListAdapter.getDoubleTaskList();//获取现在已有双向任务
		if (lastItem == tempList.size()&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {//滑动到最底部的情况
			try {
				DoubleTask item = tempList.get(tempList.size() - 1);//获取最后一个
				String date=item.getCreateTime();//获取最后一个双向任务的创建日期
				getDoubleTask(date);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 根据日期查找该日期以前的20条记录
	 * @param date 日期
	 */
	private void getDoubleTask(final String date) {
		//查找数据库
		List<DoubleTask> newList = doubleDB.findDoubleTaskByDate(date,targetId+"");
		if (!newList.isEmpty()) {//如果有数据
			List<DoubleTask> itemList = doubleListAdapter.getDoubleTaskList();
			itemList.addAll(newList);//把信息查到的数据添加到页面
			doubleListAdapter.setDoubleTaskList(itemList);
			if (itemList.size() < 20) {
				doubleListView.removeFooterView(loadingView);
			}
			doubleListAdapter.notifyDataSetChanged();
		} else {//没数据提示加载完毕
			doubleListView.removeFooterView(loadingView);
			ToastOrder.makeText(DoubleWayActivity.this, getResources().getString(R.string.loading_finish1), ToastOrder.LENGTH_LONG).show();
		}
	}
	
	/**
	 * 如果双向任务总数少于20条就移掉底部view
	 */
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		lastItem = firstVisibleItem + visibleItemCount - 1;
		if (totalItemCount < 21 && totalItemCount > 1) {
			doubleListView.removeFooterView(loadingView);
		}
	}
	
	
	@Override
	protected void onStop() {
		super.onStop();
		if (doubleListView.getFooterViewsCount() == 1) {
			doubleListView.removeFooterView(loadingView);
		}
	}
	
	/**
	 * 单击双向任务item
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		if(view!=loadingView){//如果单击的不是底部view的情况跳转到DoubleDetailActivity页面
			DoubleTask doubleTask = doubleList.get(position);
			Bundle bundle=new Bundle();
			bundle.putInt("taskId", doubleTask.getTaskId());//传递任务ID
			bundle.putString("createTime", doubleTask.getCreateTime());//创建时间
			bundle.putInt("targetId", targetId);//数据ID
			bundle.putString("headContent", doubleHead.getText().toString());//title
			Module module=(Module) getIntent().getBundleExtra("bundle").getSerializable("module");//双向模块实例
			bundle.putSerializable("module",module);
			Intent intent = new Intent(this,DoubleDetailActivity.class);
			intent.putExtra("currentDoubleTask", bundle);
			intent.putExtra("isNoWait", isNoWait);
			startActivity(intent);
		}
	}
}
