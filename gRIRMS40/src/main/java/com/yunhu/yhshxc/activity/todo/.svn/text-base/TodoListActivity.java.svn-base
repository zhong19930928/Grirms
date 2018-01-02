package com.yunhu.yhshxc.activity.todo;

import gcg.org.debug.JLog;

import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.database.TodoDB;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.parser.CacheData;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.yunhu.android.pulltorefresh.library.PullToRefreshBase;
import com.yunhu.android.pulltorefresh.library.PullToRefreshBase.Mode;
import com.yunhu.android.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.yunhu.android.pulltorefresh.library.PullToRefreshListView;

public class TodoListActivity extends AbsBaseActivity {
	private PullToRefreshListView mPullRefreshListView;
	private List<Todo> toDoList = null;
	private ToDoAdapter toDoAdapter = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.todo_list);
		findViewById(R.id.todo_list_back).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TodoListActivity.this.finish();
				
			}
		});
		toDoList = new TodoDB(this).findAllTodo();
		toDoAdapter = new ToDoAdapter();
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.lv_todo_list);
		mPullRefreshListView.setAdapter(toDoAdapter);
		String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
				   DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
		mPullRefreshListView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
		mPullRefreshListView.setMode(Mode.PULL_FROM_START);
		mPullRefreshListView.setEmptyView(View.inflate(this, R.layout.pull_refresh_empty, null));
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				refresh();
			}
		});
		mPullRefreshListView.setRefreshing(false);
	}
	
	
	private void refresh(){
		String url = UrlInfo.queryTodoUserInfo(this);
		JLog.d(TAG, "url:"+url);
		GcgHttpClient.getInstance(this).get(url, null, new HttpResponseListener(){
			
			@Override
			public void onSuccess(int statusCode, String content) {
				JLog.d(TAG, "ToDo:onSuccess:"+content);
				try {
					JSONObject obj = new JSONObject(content);
					if (obj!=null && obj.has("resultcode") && "0000".equals(obj.getString("resultcode"))) {
						if (obj.has("todo")) {
							new CacheData(TodoListActivity.this).parserToDo(obj.getString("todo"));
						}else{
							new TodoDB(TodoListActivity.this).deleteAllToDo();
						}
						toDoList = new TodoDB(TodoListActivity.this).findAllTodo();
						toDoAdapter.notifyDataSetChanged();
						int tNum = toDoNumbers(toDoList);
						Intent intent = new Intent();
						intent.setAction(Constants.BROADCAST_ACTION_WAITING_PROCESS);
						if (tNum > 0) {
							intent.putExtra(Constants.BROADCAST_ACTION_WAITING_PROCESS, PublicUtils.getResourceString(TodoListActivity.this,R.string.you_have)+tNum+PublicUtils.getResourceString(TodoListActivity.this,R.string.you_have1));
						}else{
							intent.putExtra(Constants.BROADCAST_ACTION_WAITING_PROCESS, PublicUtils.getResourceString(TodoListActivity.this,R.string.you_have2));
						}
						TodoListActivity.this.sendBroadcast(intent);
					}else{
						throw new Exception();
					}
				} catch (Exception e) {
					e.printStackTrace();
					ToastOrder.makeText(TodoListActivity.this, R.string.ERROR_DATA, ToastOrder.LENGTH_SHORT).show();
				}
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				ToastOrder.makeText(TodoListActivity.this, R.string.ERROR_NETWORK, ToastOrder.LENGTH_SHORT).show();
			}
			
			@Override
			public void onFinish() {
				mPullRefreshListView.onRefreshComplete();
			}

			@Override
			public void onStart() {
				
			}
		});
	}
	
	private class ToDoAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return toDoList.size();
		}

		@Override
		public Object getItem(int position) {
			return toDoList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Todo todo = toDoList.get(position);
			ToDoListItem item = null;
			if (convertView == null) {
				item = new ToDoListItem(TodoListActivity.this);
				convertView = item.getView();
				convertView.setTag(item);
			}else{
				item = (ToDoListItem) convertView.getTag();
			}
			item.setData(todo);
			return convertView;
		}
		
	}
	
	/**
	 * 待办事项条数
	 */
	private int toDoNumbers(List<Todo> list){
		int number = 0;
		for (int i = 0; i < list.size(); i++) {
			Todo todo = list.get(i);
			number += todo.getTodoNum();
		}
		return number;
	}
}
