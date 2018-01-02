package com.yunhu.yhshxc.wechat.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.OrgUser;
import com.yunhu.yhshxc.database.OrgUserDB;
import com.yunhu.yhshxc.wechat.exchange.PersonalWechatActivity;
import com.yunhu.yhshxc.wechat.view.sortListView.CharacterParser;
import com.yunhu.yhshxc.wechat.view.sortListView.ClearEditText;
import com.yunhu.yhshxc.wechat.view.sortListView.PinyinComparator;
import com.yunhu.yhshxc.wechat.view.sortListView.SideBar;
import com.yunhu.yhshxc.wechat.view.sortListView.SideBar.OnTouchingLetterChangedListener;
import com.yunhu.yhshxc.wechat.view.sortListView.SortAdapter;
import com.yunhu.yhshxc.wechat.view.sortListView.SortModel;
/**
 * 联系人列表
 * @author xuelinlin
 *
 */
public class FocusFragment extends Fragment {
	private ListView lv_contact;
	private ClearEditText filter_edit;
	private TextView dialog;
	
	private List<OrgUser> userList  = new ArrayList<OrgUser>();
	private OrgUserDB orgUserDB ;
	private SortAdapter adapter;
	private SideBar sideBar;
	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;
	
	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.wechat_contact, null);
		
		orgUserDB = new OrgUserDB(getActivity());
		initView(v);
		initSortByName();
		return v;
	}
	private void initSortByName() {
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
		
		adapter = new SortAdapter(getActivity(), SourceDateList);
		lv_contact.setAdapter(adapter);
		lv_contact.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//这里要利用adapter.getItem(position)来获取当前position所对应的对象
				SortModel sort = (SortModel) adapter.getItem(position);
				OrgUser user = sort.getUser();
				Intent intent = new Intent(getActivity(), PersonalWechatActivity.class);
				intent.putExtra("userId", user.getUserId());
				intent.putExtra("userName", user.getUserName());
				startActivity(intent);
			}
		});
		//设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
					
			@Override
			public void onTouchingLetterChanged(String s) {
				//该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if(position != -1){
					lv_contact.setSelection(position);
				}
				
			}
		});
	}
	private List<SortModel> filledData2(List<OrgUser> date) {
			List<SortModel> mSortList = new ArrayList<SortModel>();
			for(int i=0; i<date.size(); i++){
				SortModel sortModel = new SortModel();
				sortModel.setName(date.get(i).getUserName());
				sortModel.setUser(date.get(i));
				//汉字转换成拼音
				String pinyin = characterParser.getSelling(date.get(i).getUserName());
				String sortString = pinyin.substring(0, 1).toUpperCase();
				
				// 正则表达式，判断首字母是否是英文字母
				if(sortString.matches("[A-Z]")){
					sortModel.setSortLetters(sortString.toUpperCase());
				}else{
					sortModel.setSortLetters("#");
				}
				
				mSortList.add(sortModel);
			}
			return mSortList;
	}
	private void initView(View v) {
		userList = orgUserDB.findAllUserList();
		//实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();
		sideBar = (SideBar) v.findViewById(R.id.sidrbar);
		dialog = (TextView) v.findViewById(R.id.dialog);
		sideBar.setTextView(dialog);
		lv_contact = (ListView) v.findViewById(R.id.lv_contact);
		filter_edit = (ClearEditText) v.findViewById(R.id.filter_edit);
		
		filter_edit.addTextChangedListener(watcher);
	}
	private TextWatcher watcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
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

	private void filterData(String filterStr) {
		List<SortModel> filterDateList = new ArrayList<SortModel>();
		
		if(TextUtils.isEmpty(filterStr)){
			filterDateList = SourceDateList;
		}else{
			filterDateList.clear();
			for(SortModel sortModel : SourceDateList){
				String name = sortModel.getName();
				if(name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())){
					filterDateList.add(sortModel);
				}
			}
		}
		
		// 根据a-z进行排序
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}
}
