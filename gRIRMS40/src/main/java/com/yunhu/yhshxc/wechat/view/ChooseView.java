package com.yunhu.yhshxc.wechat.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.OrgUser;
import com.yunhu.yhshxc.database.OrgUserDB;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.widget.pulltorefreshview.MyListView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class ChooseView {
	int flag = 0;
	private View view;
	private Context context;
	private TextView tv_bumen;
	private TextView tv_zhiwei;
	private TextView tv_tianjiaren;
	private LinearLayout ll_bumen;
	private LinearLayout ll_zhiwei;
	private LinearLayout ll_people;

	private CheckBox cb_ben;
	private CheckBox cb_xiaji;
	private CheckBox cb_all;

	private MyListView lv_zhiwei;
	private ZWAdapter adapter;
	private PeoAdapter peoAdapter;
	private TextView tv_choose_name;

	private Spinner dropdow_group;
	private EditText et_an_name;
	private MyListView lv_people;
	private Map<Integer, Boolean> checkStatusMapJS = new HashMap<Integer, Boolean>();
	private Map<Integer, Boolean> checkStatusMapPer = new HashMap<Integer, Boolean>();

	private OrgUserDB orgUserDB;
	private List<OrgUser> orgUsersAll = new ArrayList<OrgUser>();
	private List<OrgUser> orgUsersZW = new ArrayList<OrgUser>();
	private List<OrgUser> orgUsersPer = new ArrayList<OrgUser>();
	private List<OrgUser> orgUsersZWS = new ArrayList<OrgUser>();

	private MyAdapter myAdapter;
	private Button btn_wechat_chaxun;
	

	public ChooseView(final Context context) {
		this.context = context;
		orgUserDB = new OrgUserDB(context);
		view = View.inflate(context, R.layout.wechat_choose_view, null);
		tv_bumen = (TextView) view.findViewById(R.id.tv_bumen);
		tv_zhiwei = (TextView) view.findViewById(R.id.tv_zhiwei);
		tv_choose_name = (TextView) view.findViewById(R.id.tv_choose_name);
		tv_tianjiaren = (TextView) view.findViewById(R.id.tv_tianjiaren);
		btn_wechat_chaxun = (Button) view.findViewById(R.id.btn_wechat_chaxun);
		tv_bumen.setOnClickListener(listener);
		tv_zhiwei.setOnClickListener(listener);
		tv_tianjiaren.setOnClickListener(listener);
		btn_wechat_chaxun.setOnClickListener(listener);
		ll_bumen = (LinearLayout) view.findViewById(R.id.ll_bumen);
		ll_zhiwei = (LinearLayout) view.findViewById(R.id.ll_zhiwei);
		ll_people = (LinearLayout) view.findViewById(R.id.ll_people);
		cb_ben = (CheckBox) view.findViewById(R.id.cb_ben);
		cb_xiaji = (CheckBox) view.findViewById(R.id.cb_xiaji);
		cb_all = (CheckBox) view.findViewById(R.id.cb_all);
		cb_ben.setOnCheckedChangeListener(changeListener);
		cb_xiaji.setOnCheckedChangeListener(changeListener);
		cb_all.setOnCheckedChangeListener(changeListener);
		lv_zhiwei = (MyListView) view.findViewById(R.id.lv_zhiwei);
		lv_people = (MyListView) view.findViewById(R.id.lv_people);
		dropdow_group = (Spinner) view.findViewById(R.id.dropdow_group);
		et_an_name = (EditText) view.findViewById(R.id.et_an_name);
//		et_an_name.addTextChangedListener(watcher);
		orgUsersZW = orgUserDB.findAllRoleList();
		orgUsersPer = orgUserDB.findAllUserList();
		OrgUser user = new OrgUser();
		user.setRoleName(PublicUtils.getResourceString(context,R.string.toast_one3));
		orgUsersZWS.add(user);
		orgUsersZWS.addAll(orgUsersZW);
		adapter = new ZWAdapter(context, orgUsersZW);
		peoAdapter = new PeoAdapter(context, orgUsersPer);
		lv_zhiwei.setAdapter(adapter);
		lv_people.setAdapter(peoAdapter);
		myAdapter = new MyAdapter(context, orgUsersZWS);

		dropdow_group.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// String str=parent.getItemAtPosition(position).toString();
				// Toast.makeText(context, "你点击的是:"+str, 2000).show();
				if(position!=0){
					isSelectUser = orgUsersZW.get(position);
				}else{
					isSelectUser = null;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		dropdow_group.setAdapter(myAdapter);

	}

	private OrgUser isSelectUser;
	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_bumen:// 按部门
				buMen();
				break;
			case R.id.tv_zhiwei:// 按职位
				zhiWei();
				break;
			case R.id.tv_tianjiaren:// 按添加人
				tianJianRen();
				break;
			case R.id.btn_wechat_chaxun:// 查询
				search();
				break;
			default:
				break;
			}
		}

	};


	private void search() {
		String name = et_an_name.getText().toString();
		if (!TextUtils.isEmpty(name)) {
			if (isSelectUser != null) {
				orgUsersPer = orgUserDB
						.findDictMultiChoiceValueStrByRoleId(
								String.valueOf(isSelectUser.getRoleId()),
								name);
				
			}else{
				orgUsersPer = orgUserDB
						.findAllOrgUserList(name);
			}
			peoAdapter.refresh(orgUsersPer);
		} else {
			if (isSelectUser != null) {
				orgUsersPer = orgUserDB.findUserByRoleId(isSelectUser.getRoleId());
				
			}else{
				orgUsersPer = orgUserDB.findAllUserList();
			}
			peoAdapter.refresh(orgUsersPer);
		}
	}

	/**
	 * 按添加人
	 */
	@SuppressLint("NewApi")
	private void tianJianRen() {
		flag = 2;
		ll_bumen.setVisibility(View.GONE);
		ll_zhiwei.setVisibility(View.GONE);
		ll_people.setVisibility(View.VISIBLE);
		if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
			tv_bumen.setBackground(context.getResources()
					.getDrawable(R.color.white));
			tv_zhiwei.setBackground(context.getResources().getDrawable(
					R.color.white));
			tv_tianjiaren.setBackground(context.getResources().getDrawable(
					R.color.near_by_search_btn));
		}else{
			tv_bumen.setBackgroundDrawable(context.getResources()
					.getDrawable(R.color.white));
			tv_zhiwei.setBackgroundDrawable(context.getResources().getDrawable(
					R.color.white));
			tv_tianjiaren.setBackgroundDrawable(context.getResources().getDrawable(
					R.color.near_by_search_btn));
		}
		
		Resources resource = context.getResources();
		ColorStateList csl = (ColorStateList) resource
				.getColorStateList(R.color.white);
		if (csl != null) {
			tv_tianjiaren.setTextColor(csl);
		}
		ColorStateList csl1 = (ColorStateList) resource
				.getColorStateList(R.color.wechat_chat_item);
		if (csl1 != null) {
			tv_bumen.setTextColor(csl1);
			tv_zhiwei.setTextColor(csl1);
		}
	}

	/**
	 * 按职位添加
	 */
	@SuppressLint("NewApi")
	private void zhiWei() {
		flag = 1;
		ll_bumen.setVisibility(View.GONE);
		ll_zhiwei.setVisibility(View.VISIBLE);
		ll_people.setVisibility(View.GONE);
		adapter.refresh(orgUsersZW);
		if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
			tv_bumen.setBackground(context.getResources()
					.getDrawable(R.color.white));
			tv_zhiwei.setBackground(context.getResources().getDrawable(
					R.color.near_by_search_btn));
			tv_tianjiaren.setBackground(context.getResources().getDrawable(
					R.color.white));
		}else{
			tv_bumen.setBackgroundDrawable(context.getResources()
					.getDrawable(R.color.white));
			tv_zhiwei.setBackgroundDrawable(context.getResources().getDrawable(
					R.color.near_by_search_btn));
			tv_tianjiaren.setBackgroundDrawable(context.getResources().getDrawable(
					R.color.white));
		}
		
		Resources resource = context.getResources();
		ColorStateList csl = (ColorStateList) resource
				.getColorStateList(R.color.white);
		if (csl != null) {
			tv_zhiwei.setTextColor(csl);
		}
		ColorStateList csl1 = (ColorStateList) resource
				.getColorStateList(R.color.wechat_chat_item);
		if (csl1 != null) {
			tv_bumen.setTextColor(csl1);
			tv_tianjiaren.setTextColor(csl1);
		}
	}

	/**
	 * 按部门添加
	 */
	@SuppressLint("NewApi")
	private void buMen() {
		flag = 0;
		ll_bumen.setVisibility(View.VISIBLE);
		ll_zhiwei.setVisibility(View.GONE);
		ll_people.setVisibility(View.GONE);
		peoAdapter.refresh(orgUsersPer);
		if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
			tv_bumen.setBackground(context.getResources().getDrawable(
					R.color.near_by_search_btn));
			tv_zhiwei.setBackground(context.getResources().getDrawable(
					R.color.white));
			tv_tianjiaren.setBackground(context.getResources().getDrawable(
					R.color.white));
		}else{
			tv_bumen.setBackgroundDrawable(context.getResources().getDrawable(
					R.color.near_by_search_btn));
			tv_zhiwei.setBackgroundDrawable(context.getResources().getDrawable(
					R.color.white));
			tv_tianjiaren.setBackgroundDrawable(context.getResources().getDrawable(
					R.color.white));
		}
		
		Resources resource = context.getResources();
		ColorStateList csl = (ColorStateList) resource
				.getColorStateList(R.color.white);
		if (csl != null) {
			tv_bumen.setTextColor(csl);
		}
		ColorStateList csl1 = (ColorStateList) resource
				.getColorStateList(R.color.wechat_chat_item);
		if (csl1 != null) {
			tv_zhiwei.setTextColor(csl1);
			tv_tianjiaren.setTextColor(csl1);
		}

	}

	int type ;
	private OnCheckedChangeListener changeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			switch (buttonView.getId()) {
			case R.id.cb_ben:// 按部门
				setBuMen(isChecked);
				break;
			case R.id.cb_xiaji:// 按角色
				setJueSe(isChecked);
				break;
			case R.id.cb_all:// 所有人
				setAll(isChecked);
				break;

			default:
				break;
			}
		}
	};

	private void setBuMen(boolean isChecked) {
		// 人员集合先clear , 然后重新查询 赋值
		if (isChecked) {
			type = 3;
			cb_xiaji.setChecked(false);
			cb_all.setChecked(false);
		}
	}

	private void setAll(boolean isChecked) {
		if (isChecked) {
			type = 1;
			cb_xiaji.setChecked(false);
			cb_ben.setChecked(false);
		}
	}

	private void setJueSe(boolean isChecked) {
		if (isChecked) {
			type = 2;
			cb_ben.setChecked(false);
			cb_all.setChecked(false);
		}
	}

	class ZWAdapter extends BaseAdapter implements OnCheckedChangeListener {
		private Context context;
		private List<OrgUser> orgUsers;

		public ZWAdapter(Context context, List<OrgUser> orgUsers) {
			this.context = context;
			this.orgUsers = orgUsers;
			int position = 0;
			for (int i = 0; i < orgUsers.size(); i++) {
				checkStatusMapJS.put(position++, false);
			}
		}

		public void refresh(List<OrgUser> orgUsers2) {
			this.orgUsers = orgUsers2;
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return orgUsers.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			GroupView view = null;
			if (convertView == null) {
				view = new GroupView(context);
				convertView = view.getView();
				convertView.setTag(view);
			} else {
				view = (GroupView) convertView.getTag();
			}
			view.initZWData(orgUsers.get(position));
			view.getCheckBox().setOnCheckedChangeListener(this);
			view.getCheckBox().setTag(position);
			view.getCheckBox().setChecked(checkStatusMapJS.get(position));
			return convertView;
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			Integer position = (Integer) buttonView.getTag();
			if (isChecked) {
				checkStatusMapJS.put(position, true);
			} else {
				checkStatusMapJS.put(position, false);
			}
			
		}

	}
	
	public void setSelectName(){
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i<orgUsersAll.size(); i++){
			sb.append(orgUsersAll.get(i).getUserName()).append("、");
		}
//		tv_choose_name.setText(sb.toString());
		if(sb.length()>=1){
			tv_choose_name.setText(sb.substring(0, sb.length()-1).toString());
		}else{
			tv_choose_name.setText("");
		}
	}

	class PeoAdapter extends BaseAdapter implements OnCheckedChangeListener {
		private Context context;
		private List<OrgUser> orgUsersPer;

		public PeoAdapter(Context context, List<OrgUser> orgUsersPer) {
			this.context = context;
			this.orgUsersPer = orgUsersPer;
			for (int i = 0; i < orgUsersPer.size(); i++) {
				checkStatusMapPer.put(orgUsersPer.get(i).getUserId(), false);
			}
		}

		public void refresh(List<OrgUser> orgUsers2) {
			this.orgUsersPer = orgUsers2;
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return orgUsersPer.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			GroupView view = null;
			if (convertView == null) {
				view = new GroupView(context);
				convertView = view.getView();
				convertView.setTag(view);
			} else {
				view = (GroupView) convertView.getTag();
			}
			view.initData(orgUsersPer.get(position));
			view.getCheckBox().setOnCheckedChangeListener(this);
			view.getCheckBox().setTag(position);
			OrgUser user = orgUsersPer.get(position);
			Boolean flag = checkStatusMapPer.get(user.getUserId());
			if (flag==null) {
				flag = false;
			}
			view.getCheckBox().setChecked(flag);
			return convertView;
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			Integer position = (Integer) buttonView.getTag();
			if (isChecked) {
				checkStatusMapPer.put(orgUsersPer.get(position).getUserId(), true);
				addPerson(orgUsersPer.get(position));
				
			} else {
				checkStatusMapPer.put(orgUsersPer.get(position).getUserId(), false);
				deletePerson(orgUsersPer.get(position));
			}
			setSelectName();
		}

	}
	public void deletePerson(OrgUser user) {
		Iterator<OrgUser> it = orgUsersAll.iterator();
		while (it.hasNext()) {
			OrgUser p = it.next();
			if (p.getId()==user.getId()) {
				it.remove();
			}
		}
	}

	public void addPerson(OrgUser user) {
		boolean isHas = false;
		Iterator<OrgUser> it = orgUsersAll.iterator();
		while (it.hasNext()) {
			OrgUser p = it.next();
			if (p.getId()==user.getId()) {
				isHas = true;
			}
		}
		if (!isHas) {
			orgUsersAll.add(user);
		}

	}
	
	public View getView() {
		return view;
	}

	public int getFlag() {
		return flag;
	}

	/**
	 * flag == 2;
	 * 
	 * @return
	 */
	public List<OrgUser> getOrgUsers() {
		return orgUsersAll;
	}

	/**
	 * flag == 1;
	 * 
	 * @return
	 */
	public List<OrgUser> getOrgUsersZW() {

		List<OrgUser> finalOrgList = new ArrayList<OrgUser>();
		for (int i = 0; i < orgUsersZW.size(); i++) {
			if (checkStatusMapJS.get(i)) {
				finalOrgList.add(orgUsersZW.get(i));
			}
		}
		return finalOrgList;
	}

	/**
	 * flag == 0;
	 * 
	 * @return
	 */
	public int getType() {
		return type;
	}


	public class MyAdapter extends BaseAdapter {
		private List<OrgUser> mList;
		private Context mContext;

		public MyAdapter(Context pContext, List<OrgUser> pList) {
			this.mContext = pContext;
			this.mList = pList;
		}

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
			convertView = _LayoutInflater.inflate(R.layout.wechat_spinner_item,
					null);
			if (convertView != null) {
				TextView _TextView1 = (TextView) convertView
						.findViewById(R.id.textView1);
				_TextView1.setText(mList.get(position).getRoleName());
			}
			return convertView;
		}
	}
}
