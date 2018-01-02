package com.yunhu.yhshxc.notify;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.adapter.NotifyListAdapter;
import com.yunhu.yhshxc.bo.Notice;
import com.yunhu.yhshxc.database.NoticeDB;

import java.util.List;

import gcg.org.debug.JLog;


/**
 * 公告列表
 */
public class NotifyListActivity extends AbsBaseActivity implements OnScrollListener {
	/**
	 * 显示公告的listview
	 */
	private ListView notifyListView;
	/**
	 * 分页加载显示的正在加载View
	 */
	private View loadingView;
	/**
	 * 所有公告集合
	 */
	private List<Notice> noticeList;
	/**
	 * 公告listview的适配器
	 */
	public NotifyListAdapter noticeListAdapter;
	/**
	 *  listView最后一个item的下标
	 */
	private int lastItem = 0;
	/**
	 * 公告数据库
	 */
	private NoticeDB noticeDB;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notify_list);
		initBase();//父类方法，目前没做处理
	}

	@Override
	protected void onStart() {
		super.onStart();
		init();//初始化数据
	}
	
	/**
	 * 初始化数据 如果公告是空提示没有公告
	 */
	private void init() {
		noticeDB=new NoticeDB(this);
		loadingView = LayoutInflater.from(this).inflate(
				R.layout.notify_list_loading, null);
		notifyListView = (ListView) findViewById(R.id.notify_list_listView);
		noticeList = noticeDB.findAllNotice();
		if(noticeList.isEmpty()){
			Toast.makeText(this,getResources().getString(R.string.noNotice),Toast.LENGTH_SHORT).show();
		}else{
			if (notifyListView.getFooterViewsCount() == 0 && noticeList.size()>=20){
				notifyListView.addFooterView(loadingView);// 添加加载视图
			}
			notifyListView.setOnScrollListener(this);
//			httpHelper=new CoreHttpHelper(this,resultHandler);
		}
		noticeListAdapter = new NotifyListAdapter(this, noticeList);
		notifyListView.setAdapter(noticeListAdapter);
	}

	/**
	 * 分页
	 * 滑动到最后一个的时候加载另外的20条
	 */

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		List<Notice> tempList = noticeListAdapter.getNoticeList();
		if (lastItem == tempList.size()&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {//判断滑动到最后
			try {
				Notice item = tempList.get(tempList.size() - 1);
				String date=item.getCreateTime();
				getNotify(date);
				JLog.d(TAG, "date===>"+date);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 分页
	 */
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		lastItem = firstVisibleItem + visibleItemCount - 1;
		if (totalItemCount < 21 && totalItemCount > 1) {
			notifyListView.removeFooterView(loadingView);
		}

	}

	/**
	 * 根据传过来的日期获取日期前面的二十条公告
	 * @param date 日期
	 */
	private void getNotify(final String date) {

		List<Notice> newList = noticeDB.findNoticeByDate(date);// 解析拿到的公告
		if (!newList.isEmpty()) {//查询到数据就添加到listview上
			List<Notice> itemList = noticeListAdapter.getNoticeList();
			itemList.addAll(newList);
			noticeListAdapter.setNoticeList(itemList);
			if (itemList.size() < 20) {
				notifyListView.removeFooterView(loadingView);
			}
			noticeListAdapter.notifyDataSetChanged();
		} else {
			notifyListView.removeFooterView(loadingView);
			Toast.makeText(NotifyListActivity.this, getResources().getString(R.string.notify_activity_10), Toast.LENGTH_LONG).show();
//			noticeListAdapter.notifyDataSetChanged();
		}
		JLog.d(TAG, "getNotifyThreadStart");
	}
	
	/**
	 * 弹出删除公告对话框
	 * @param pos 要删除的公告的位置 
	 * @param notice 要删除的公告的实例
	 */
	public void showDeleteDialog(final int pos,final Notice notice){
		JLog.d(TAG, "DELETEPOS==> "+pos);
		View contentView = View.inflate(this, R.layout.delete_prompt, null);
		final PopupWindow popupWindow = new PopupWindow(contentView,WindowManager.LayoutParams.FILL_PARENT,WindowManager.LayoutParams.FILL_PARENT,true);
		popupWindow.setAnimationStyle(R.style.popupwindow);
		int high=getWindowManager().getDefaultDisplay().getHeight();
		popupWindow.showAsDropDown(notifyListView,0,-(high/4+15));
//		popupWindow.showAsDropDown(notifyListView,0,-(Constants.SCREEN_HEIGHT/5+15));
		contentView.findViewById(R.id.btn_confirm).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
				noticeList.remove(pos);
				noticeDB.deleteNoticeById(notice.getId()+"");
				noticeListAdapter.notifyDataSetChanged();
			}
		});
		contentView.findViewById(R.id.btn_canceled).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});
	}
	
	
	@Override
	protected void onStop() {
		super.onStop();
		if (notifyListView.getFooterViewsCount() == 1) {
			notifyListView.removeFooterView(loadingView);
		}
	}
}
