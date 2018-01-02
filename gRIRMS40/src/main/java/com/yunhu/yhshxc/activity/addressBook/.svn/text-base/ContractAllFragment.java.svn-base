package com.yunhu.yhshxc.activity.addressBook;

import gcg.org.debug.JLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.addressBook.AddressBFragmentPagerAdapter.FragmentListener;
import com.yunhu.yhshxc.activity.addressBook.bo.AdressBookUser;
import com.yunhu.yhshxc.activity.addressBook.db.AdressBookUserDB;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.parser.CacheData;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.wechat.view.sortListView.CharacterParser;
import com.yunhu.yhshxc.wechat.view.sortListView.ClearEditText;
import com.yunhu.yhshxc.wechat.view.sortListView.PinyinComparator;
import com.yunhu.yhshxc.wechat.view.sortListView.SideBar;
import com.yunhu.yhshxc.wechat.view.sortListView.SideBar.OnTouchingLetterChangedListener;
import com.yunhu.yhshxc.wechat.view.sortListView.SortModel;
import com.yunhu.yhshxc.widget.pulltorefreshview.HeaderPullToRefreshView;
import com.yunhu.yhshxc.widget.pulltorefreshview.HeaderPullToRefreshView.OnHeaderRefreshListener;
import com.loopj.android.http.RequestParams;

public class ContractAllFragment extends Fragment implements
		OnHeaderRefreshListener {
	private ListView lv_contact;
	private TextView tv_xialashuaxin;
	private HeaderPullToRefreshView pulltorefresh_all;
	private ClearEditText filter_edit;
	private TextView dialog;
	private SideBar sideBar;
	private ContractAdapter adapter;
	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList = new ArrayList<SortModel>();
	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;

	private List<AdressBookUser> userList;
	private AdressBookUserDB userDB;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.address_book_all, container,
				false);
		userDB = new AdressBookUserDB(getActivity());
		initView(view);
		return view;
	}

	private void initView(View view) {
		tv_xialashuaxin = (TextView) view.findViewById(R.id.tv_xialashuaxin);
		pulltorefresh_all = (HeaderPullToRefreshView) view
				.findViewById(R.id.pulltorefresh_all);
		pulltorefresh_all.setOnHeaderRefreshListener(this);
		pulltorefresh_all.setLastUpdated(new Date().toLocaleString());
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();
		sideBar = (SideBar) view.findViewById(R.id.sidrbar);
		dialog = (TextView) view.findViewById(R.id.dialog);
		sideBar.setTextView(dialog);
		lv_contact = (ListView) view.findViewById(R.id.lv_all);
		filter_edit = (ClearEditText) view.findViewById(R.id.filter_edit);
		filter_edit.addTextChangedListener(watcher);
		initUserData();
		
	}
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			initSortByName();
		};
	};
	private void initUserData(){
		new Thread(){
			public void run() {
				userList = userDB.findAllUserList();
				SourceDateList = filledData2(userList);
				// 根据a-z进行排序源数据
				Collections.sort(SourceDateList, pinyinComparator);

				List<SortModel> temp = new ArrayList<SortModel>();

				Iterator<SortModel> iterator = SourceDateList.iterator();
				while (iterator.hasNext()) {
					SortModel dd = iterator.next();
					if ("#".equals(dd.getSortLetters())) {
						temp.add(dd);
						iterator.remove();
					}
				}
				if (temp.size() != 0) {
					Collections.reverse(temp);
					SourceDateList.addAll(temp);
				}
				mHandler.sendEmptyMessage(1);
			};
		}.start();
	}
	private void initSortByName() {

		adapter = new ContractAdapter(getActivity(), SourceDateList);
		lv_contact.setAdapter(adapter);
		lv_contact.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 这里要利用adapter.getItem(position)来获取当前position所对应的对象
				SortModel sort = (SortModel) adapter.getItem(position);
				AdressBookUser user = sort.getAdressBookUser();
				if (user != null) {
					Intent intent = new Intent(getActivity(),
							AddressBookDetailActivity.class);
					intent.putExtra("userId", user.getuId());
					startActivity(intent);
				}
			}
		});
		lv_contact.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch(scrollState){  
		        case OnScrollListener.SCROLL_STATE_IDLE://空闲状态  
		                      
		       break;  
		        case OnScrollListener.SCROLL_STATE_FLING://滚动状态  
		            break;  
		        case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL://触摸后滚动  
		            break;  
		        }  
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if(firstVisibleItem ==0&&!isFirst){
					
					tv_xialashuaxin.setVisibility(View.VISIBLE);
				}else{
					tv_xialashuaxin.setVisibility(View.INVISIBLE);
				}
			}
		});
		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					lv_contact.setSelection(position);
				}

			}
		});
	}
	boolean isFirst = false;
	private List<SortModel> filledData2(List<AdressBookUser> date) {
		List<SortModel> mSortList = new ArrayList<SortModel>();
		for (int i = 0; i < date.size(); i++) {
			SortModel sortModel = new SortModel();
			sortModel.setName(date.get(i).getUn());
			sortModel.setAdressBookUser(date.get(i));
			// 汉字转换成拼音
			String pinyin = characterParser.getSelling(date.get(i).getUn());
			String sortString = pinyin.substring(0, 1).toUpperCase();

			// 正则表达式，判断首字母是否是英文字母
			if (sortString.matches("[A-Z]")) {
				sortModel.setSortLetters(sortString.toUpperCase());
			} else {
				sortModel.setSortLetters("#");
			}

			mSortList.add(sortModel);
		}
		return mSortList;
	}

	private TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			filterData(s.toString());
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {

		}
	};

	public void setV() {
		if (sideBar != null) {
			sideBar.setVis();
		}
	}

	private void filterData(String filterStr) {
		List<SortModel> filterDateList = new ArrayList<SortModel>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = SourceDateList;
		} else {
			filterDateList.clear();
			for (SortModel sortModel : SourceDateList) {
				String name = sortModel.getName();
				if (name.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(name).startsWith(
								filterStr.toString())) {
					filterDateList.add(sortModel);
				}
			}
		}

		// 根据a-z进行排序
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}


	private void syncData() {
		isFirst = true;
		tv_xialashuaxin.setVisibility(View.INVISIBLE);
		String url = UrlInfo.mailListInfo(getActivity());
		GcgHttpClient.getInstance(getActivity()).post(url, params(),
				new HttpResponseListener() {

					@Override
					public void onSuccess(int statusCode, String content) {
						JLog.d("aaa", "content:" + content);
						try {
							JSONObject obj = new JSONObject(content);
							String resultcode = obj.getString("resultcode");
							if ("0000".equals(resultcode)) {
								isFirst = false;
								new CacheData(getActivity()).parserIsMailList(obj);
								userList = userDB.findAllUserList();
								SourceDateList = filledData2(userList);
								// 根据a-z进行排序源数据
								Collections.sort(SourceDateList, pinyinComparator);

								List<SortModel> temp = new ArrayList<SortModel>();

								Iterator<SortModel> iterator = SourceDateList.iterator();
								while (iterator.hasNext()) {
									SortModel dd = iterator.next();
									if ("#".equals(dd.getSortLetters())) {
										temp.add(dd);
										iterator.remove();
									}
								}
								if (temp.size() != 0) {
									Collections.reverse(temp);
									SourceDateList.addAll(temp);
								}
								adapter.updateListView(SourceDateList);
								tv_xialashuaxin.setVisibility(View.VISIBLE);
								pulltorefresh_all.onHeaderRefreshComplete();
								if (listener != null) {
						              listener.onFragmentClickListener(1);
						              listener.onFragmentClickListener(2);
						            }
							} else {
								throw new Exception();
							}
						} catch (Exception e) {
//							searchHandler.sendEmptyMessage(3);
						}
					}

					@Override
					public void onStart() {
						
					}

					@Override
					public void onFinish() {
						isFirst = false;
						tv_xialashuaxin.setVisibility(View.VISIBLE);
					}

					@Override
					public void onFailure(Throwable error, String content) {
						isFirst = false;
						tv_xialashuaxin.setVisibility(View.VISIBLE);
						pulltorefresh_all.onHeaderRefreshComplete();
					}
				});
	}
	private RequestParams params() {
		RequestParams params = new RequestParams();
		params.put("phoneno", PublicUtils.receivePhoneNO(getActivity()));
		
//		JLog.d("alin", "params:"+params.toString());
		return params;
	}

	@Override
	public void onHeaderRefresh(HeaderPullToRefreshView view) {
		syncData();
	}
	private FragmentListener listener;
	public void onAttach(Activity activity) {  
		  super.onAttach(activity);  
		  try {  
		      listener = (FragmentListener)activity;  
		  } catch (Exception e) {  
		      e.printStackTrace();  
		     
		  }  
		    }
}
