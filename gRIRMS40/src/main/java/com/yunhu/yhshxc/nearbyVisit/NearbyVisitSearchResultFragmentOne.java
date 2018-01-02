package com.yunhu.yhshxc.nearbyVisit;

import gcg.org.debug.JLog;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.nearbyVisit.bo.NearbyListItem;
import com.yunhu.yhshxc.nearbyVisit.view.NearbyVisitSearchResultItem;
import com.yunhu.yhshxc.parser.CacheData;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.SharedPreferencesUtilForNearby;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.yunhu.android.pulltorefresh.library.PullToRefreshBase;
import com.yunhu.android.pulltorefresh.library.PullToRefreshBase.Mode;
import com.yunhu.android.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.yunhu.android.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.RequestParams;


public class NearbyVisitSearchResultFragmentOne extends Fragment implements OnItemClickListener{
	
	private final String TAG = "NearbyVisitSearchResultFragment";
	private PullToRefreshListView mPullRefreshListView;
	private SearchResultAdapter searchResultAdapter;
	private NearbyVisitActivity nearbyVisitActivity;
	private List<NearbyListItem> nearbyListItemList;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		nearbyListItemList = new ArrayList<NearbyListItem>();
		nearbyVisitActivity = (NearbyVisitActivity)this.getActivity();
		View view = inflater.inflate(R.layout.nearby_visit_search_result_fragment, null);
		searchResultAdapter = new SearchResultAdapter();
		mPullRefreshListView = (PullToRefreshListView)view.findViewById(R.id.pull_refresh_list);
		mPullRefreshListView.setAdapter(searchResultAdapter);
		mPullRefreshListView.setOnItemClickListener(this);
		String label = DateUtils.formatDateTime(this.getActivity().getApplicationContext(), System.currentTimeMillis(),
				   DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
		mPullRefreshListView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
		mPullRefreshListView.setMode(Mode.PULL_FROM_END);
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (refreshView.getCurrentMode() == Mode.PULL_FROM_END) {
					search(searchParams);
				}
			}
		});
		return view;
	}
	
    public int pages;//查询列表页数
    
    private String pagesValue(){
    	String page = ((pages*20)+","+"20");
    	return page;
    }

    /**
     * 刷新
     * @param params
     */
    private String searchParams;
    public void refresh(String params){
    	pages = 0;
    	search(params);
    }
    
    private RequestParams searchParams(String params){
    	RequestParams param = new RequestParams();
		param.put("nearby_param", params);
		param.put("moduleId", nearbyVisitActivity.menuId);
		param.put("dataStaus", SharedPreferencesUtilForNearby.getInstance(this.getActivity()).getNearbyDataStatus(String.valueOf(nearbyVisitActivity.menuId)));
		param.put("sqlId", SharedPreferencesUtilForNearby.getInstance(getActivity()).getNearbyStoreListSql(nearbyVisitActivity.menuId));
		param.put("2", String.valueOf(SharedPreferencesUtil.getInstance(getActivity()).getUserId()));
		param.put("page", pagesValue());
		if (null!=nearbyVisitActivity.nearbyVisitLeftMenu.distanceSpinner&&!TextUtils.isEmpty(nearbyVisitActivity.nearbyVisitLeftMenu.distanceSpinner.getLonLat())) {
			param.put("dstn", nearbyVisitActivity.nearbyVisitLeftMenu.distanceSpinner.getValue());//距离
			param.put("lonlat", nearbyVisitActivity.nearbyVisitLeftMenu.distanceSpinner.getLonLat());//经纬度

		}
		JLog.d(TAG, param.toString());
		return param;
    }
    
    private Dialog searchDialog;
	private void search(String params){
		this.searchParams = params;
		String url = UrlInfo.queryNearbyVisitDataInfo(getActivity());
		RequestParams param = searchParams(params);
		GcgHttpClient.getInstance(this.getActivity()).post(url, param, new HttpResponseListener(){
			@Override
			public void onStart() {
				if (pages==0) {
					searchDialog  = new MyProgressDialog(NearbyVisitSearchResultFragmentOne.this.getActivity(),R.style.CustomProgressDialog,setString(R.string.nearby_visit_05));
					searchDialog.show();
				}
			}
			
			@Override
			public void onSuccess(int statusCode, String content) {
				JLog.d(TAG, "onSuccess:"+content);
				try {
					JSONObject obj = new JSONObject(content);
					String resultCode = obj.getString("resultcode");
					if ("0000".equals(resultCode)) {
						String arrayStr = obj.getString("nearby_data");
						if (TextUtils.isEmpty(arrayStr)) {
							ToastOrder.makeText(getActivity(), setString(R.string.nearby_visit_09), ToastOrder.LENGTH_SHORT).show();
						}else{
							JSONArray array = obj.getJSONArray("nearby_data");
							List<NearbyListItem> newListItem = new CacheData(getActivity()).parserNearbyListItem(array);
							if (pages == 0) {//说明是刷新
								nearbyListItemList.clear();
							}
							nearbyListItemList.addAll(newListItem);
							searchResultAdapter.notifyDataSetChanged();
							pages++;
						}
					}else{
						throw new Exception();
					}
				} catch (Exception e) {
					e.printStackTrace();
					if(getActivity()!=null){
						ToastOrder.makeText(getActivity(), R.string.ERROR_DATA, ToastOrder.LENGTH_SHORT).show();
					}
				}
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				JLog.d(TAG, "onFailure:"+content);
				if(getActivity()!=null){
					ToastOrder.makeText(getActivity(), R.string.retry_net_exception, ToastOrder.LENGTH_SHORT).show();
				}
			}
			
			@Override
			public void onFinish() {
				if (searchDialog!=null && searchDialog.isShowing()) {
					searchDialog.dismiss();
				}
				mPullRefreshListView.onRefreshComplete();
			}
			
		});
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		NearbyListItem item = nearbyListItemList.get(position-1);
		searchStoreDetail(item);
	}
	
	/**
	 * 查询店面详细信息参数
	 * @param status等于0的时候传storeid status大于0的时候传patchid
	 * @return
	 */
	private RequestParams searchStoreDetailParams(NearbyListItem item){
		RequestParams param = new RequestParams();
		param.put("moduleId", nearbyVisitActivity.menuId);
		param.put("dataStaus", SharedPreferencesUtilForNearby.getInstance(this.getActivity()).getNearbyDataStatus(String.valueOf(nearbyVisitActivity.menuId)));
		param.put("sqlId", SharedPreferencesUtilForNearby.getInstance(getActivity()).getNearbyStoreDetailSql(nearbyVisitActivity.menuId));
		param.put("nearby_param", searchParams);
		if ("0".equals(SharedPreferencesUtilForNearby.getInstance(getActivity()).getNearbyDataStatus(nearbyVisitActivity.menuId))) {
			param.put("1", item.getStoreID());
		}else{
			param.put("patchId", item.getPatchID());
		}
		param.put("2", String.valueOf(SharedPreferencesUtil.getInstance(getActivity()).getUserId()));
		JLog.d(TAG, param.toString());
		return param;
	}
	
	/**
	 * 查询店面详细信息
	 * @param item
	 */
	private void searchStoreDetail(NearbyListItem item){
		final Dialog dialog = new MyProgressDialog(NearbyVisitSearchResultFragmentOne.this.getActivity(),R.style.CustomProgressDialog,setString(R.string.nearby_visit_05));
		String url = UrlInfo.queryNearbyVisitDataInfo(getActivity());
		RequestParams param = searchStoreDetailParams(item);
		GcgHttpClient.getInstance(this.getActivity()).post(url, param, new HttpResponseListener(){
			@Override
			public void onStart() {
				dialog.show();
			}
			
			@Override
			public void onSuccess(int statusCode, String content) {
				JLog.d(TAG, "onSuccess:"+content);
				try {
					JSONObject obj = new JSONObject(content);
					String resultCode = obj.getString("resultcode");
					if ("0000".equals(resultCode)) {
						String data = obj.getString("nearby_data");
						if (!TextUtils.isEmpty(data)) {
							Intent intent = new Intent(getActivity(), NearbyVisitStoreDetailActivity.class);
							intent.putExtra("storeInfoData", data);
							intent.putExtra("menuId", nearbyVisitActivity.menuId);
							getActivity().startActivity(intent);
						}else{
							ToastOrder.makeText(getActivity(), setString(R.string.nearby_visit_11), ToastOrder.LENGTH_SHORT).show();
						}
					}else{
						throw new Exception();
					}
				} catch (Exception e) {
					e.printStackTrace();
					ToastOrder.makeText(getActivity(), R.string.ERROR_DATA, ToastOrder.LENGTH_SHORT).show();
				}
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				JLog.d(TAG, "onFailure:"+content);
				ToastOrder.makeText(getActivity(), setString(R.string.nearby_visit_12), ToastOrder.LENGTH_SHORT).show();
			}
			
			@Override
			public void onFinish() {
				if (dialog!=null && dialog.isShowing()) {
					dialog.dismiss();
				}
			}
			
		});
	
	}
	
	
	private class SearchResultAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return nearbyListItemList.size();
		}

		@Override
		public Object getItem(int position) {
			return nearbyListItemList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			NearbyVisitSearchResultItem searchResultItem = null;
			NearbyListItem item = nearbyListItemList.get(position);
			if (convertView == null) {
				searchResultItem = new NearbyVisitSearchResultItem(NearbyVisitSearchResultFragmentOne.this.getActivity());
				convertView = searchResultItem.getView();
				convertView.setTag(searchResultItem);
			}else{
				searchResultItem = (NearbyVisitSearchResultItem) convertView.getTag();
			}
			searchResultItem.setData(item);
			return convertView;
		}
		
	}
	private String setString(int stringId){
		return getActivity().getResources().getString(stringId);
	}
}
