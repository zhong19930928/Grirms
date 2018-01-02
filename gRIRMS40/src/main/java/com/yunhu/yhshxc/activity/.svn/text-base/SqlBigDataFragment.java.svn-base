package com.yunhu.yhshxc.activity;

import java.util.HashMap;
import java.util.List;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.android.pulltorefresh.extras.listfragment.PullToRefreshListFragment;
import com.yunhu.android.pulltorefresh.library.PullToRefreshBase;
import com.yunhu.android.pulltorefresh.library.PullToRefreshBase.Mode;
import com.yunhu.android.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.yunhu.android.pulltorefresh.library.PullToRefreshListView;

/**
 * 大数据类型的控件的选项列表
 * @author gcg_jishen
 *
 */
public class SqlBigDataFragment extends PullToRefreshListFragment implements OnRefreshListener<ListView> {
	
	private DictAdapter adapter;
	private SqlBigDataActivity activity;
	private PullToRefreshListView mPullRefreshListView;//分页刷新的listview

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = (SqlBigDataActivity)SqlBigDataFragment.this.getActivity();
	}

	@Override
	protected PullToRefreshListView onCreatePullToRefreshListView(
			LayoutInflater inflater, Bundle savedInstanceState) {
		return (PullToRefreshListView)inflater.inflate(R.layout.ptr_list, null);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mPullRefreshListView = this.getPullToRefreshListView();
		mPullRefreshListView.setOnRefreshListener(this);
		mPullRefreshListView.setMode(Mode.PULL_FROM_END);
		mPullRefreshListView.getLoadingLayoutProxy().setPullLabel(PublicUtils.getResourceString(getActivity(),R.string.up_refresh));
		mPullRefreshListView.getLoadingLayoutProxy().setReleaseLabel(PublicUtils.getResourceString(getActivity(),R.string.up_refresh1));
		mPullRefreshListView.getLoadingLayoutProxy().setRefreshingLabel(PublicUtils.getResourceString(getActivity(),R.string.loading_));
		adapter = new DictAdapter();
		mPullRefreshListView.setAdapter(adapter);
		this.setListShown(true);
	}
	
	/**
	 * listview添加值
	 * @param list 数据源
	 * @param isClear 是否清除已有的值
	 */
	protected void addItemAndRefresh(List<Dictionary> list,boolean isClear) {
		if(list != null && !list.isEmpty()){
			if(isClear){
				adapter.clear();
			}
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
				for(Dictionary dict : list){
					adapter.add(dict);
				}
			}else{
				adapter.addAll(list);
			}
			adapter.notifyDataSetChanged();
		}
		mPullRefreshListView.onRefreshComplete();
	}
	
	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		activity.result();
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		HashMap<String,String> selectedMap = activity.getSelectedMap();
		ViewHolder vh = ((ViewHolder)v.getTag());
		if(selectedMap != null && selectedMap.containsKey(vh.dicId)){ //点击的同一个，清除数据
			vh.img.setImageResource(R.drawable.sql_big_data_search_unchoice);
			activity.selected(adapter.getItem(position-1),false);
			
		}else{
		
			//第一次操作
			vh.img.setImageResource(R.drawable.sql_big_data_search_choice);
			activity.selected(adapter.getItem(position-1),true);
		}
		
		
	}
	
	/**
	 * ListView Adapter
	 * @author gcg_jishen
	 *
	 */
	private final class DictAdapter extends ArrayAdapter<Dictionary>{
		
		public DictAdapter() {
			super(SqlBigDataFragment.this.getActivity(),0);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder viewHolder;
	        if (convertView == null) {
	        	convertView = View.inflate(this.getContext(),R.layout.sql_big_data_item, null);
	            viewHolder = new ViewHolder();
	            viewHolder.text = (TextView)convertView.findViewById(R.id.textView1);
	            viewHolder.img = (ImageView)convertView.findViewById(R.id.imageView1);
	            convertView.setTag(viewHolder);
	        } else {
	        	viewHolder = (ViewHolder)convertView.getTag();
	        }
	        
			Dictionary dict = this.getItem(position);
			
			viewHolder.text.setText(dict.getCtrlCol());
			HashMap<String,String> selectedMap = activity.getSelectedMap();
			if(selectedMap != null && selectedMap.containsKey(dict.getDid())){
				viewHolder.img.setImageResource(R.drawable.sql_big_data_search_choice);
				
			}else{
				viewHolder.img.setImageResource(R.drawable.sql_big_data_search_unchoice);
			}
			viewHolder.dicId = dict.getDid();
			return convertView;
		}
		
	}
	
	@Override
	public void onDestroyView() {
		adapter.clear();
		super.onDestroyView();
	}

	private final class ViewHolder{
		public TextView text;
		public ImageView img;
		public String dicId;
		
	}
}
