package com.yunhu.yhshxc.order3;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.order3.bo.Order3Contact;
import com.yunhu.yhshxc.order3.db.Order3ContactsDB;
import com.yunhu.yhshxc.order3.util.SharedPreferencesForOrder3Util;
import com.yunhu.yhshxc.parser.Order3Parse;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.DropDown;
import com.yunhu.yhshxc.widget.DropDown.OnResultListener;

public class ShouKuanFragment extends Fragment implements OnResultListener{
	private final String TAG = "ShouKuanFragment";
	private EditText et_shoukuan;
	private EditText et_sub;
	private EditText et_leave_message;
	private DropDown dropdown_order3_contracts;
	private Order3ContactsDB orderContactsDB = null;
	private List<Order3Contact> orderContactsList = null;
	private ShoppingCarActivity carActivity = null;
	private Order3Contact order3Contact;
	private Button btn_syn_user;
	private LinearLayout ll_order3_contact;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.order3_gathering_message, null);
		carActivity = (ShoppingCarActivity) getActivity();
		orderContactsDB = new Order3ContactsDB(carActivity);
		initView(v);
		initContracts();
		return v;
		
	}
	
	private void initView(View view) {
		et_shoukuan = (EditText) view.findViewById(R.id.et_shoukuan);
		et_shoukuan.addTextChangedListener(skWatcher);
		et_sub = (EditText) view.findViewById(R.id.et_sub);
		et_sub.addTextChangedListener(jmWatcher);
		et_leave_message = (EditText) view.findViewById(R.id.et_leave_message);
		et_leave_message.addTextChangedListener(msgWatcher);
		
		dropdown_order3_contracts = (DropDown) view.findViewById(R.id.dropdown_order3_contracts);
		dropdown_order3_contracts.setOnResultListener(this);
		btn_syn_user = (Button)view.findViewById(R.id.btn_syn_user);
		btn_syn_user.setOnClickListener(listener);
		ll_order3_contact = (LinearLayout)view.findViewById(R.id.ll_order3_contact);
		//是否需要订货联系人 1不要 2要 默认是2
		int isOrderUser = SharedPreferencesForOrder3Util.getInstance(carActivity).getIsOrderUser();
		if (isOrderUser == 1) {
			ll_order3_contact.setVisibility(View.GONE);
		}else{
			ll_order3_contact.setVisibility(View.VISIBLE);

		}
		getMessage();
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		//设置本界面内容
		getMessage();
		
	}
	
	private void getMessage() {
		String sk = SharedPreferencesForOrder3Util.getInstance(carActivity).getShouKuan();
		et_shoukuan.setText("0".equals(sk)?"":sk);
		et_leave_message.setText(SharedPreferencesForOrder3Util.getInstance(carActivity).getLeaveMessage());
		String jm = SharedPreferencesForOrder3Util.getInstance(carActivity).getJianMian();
		et_sub.setText("0".equals(jm)?"":jm);

	}
	
	/**
	 * 初始化联系人
	 */
	private void initContracts(){
		orderContactsList = orderContactsDB.findAllOrderContactsByStoreId(carActivity.storeId);
		List<Dictionary> contactsList = new ArrayList<Dictionary>();
		Order3Contact orderContacts = null;
    	for (int i = 0; i < orderContactsList.size(); i++) {
    		orderContacts = orderContactsList.get(i);
    		Dictionary dic = new Dictionary();
    		dic.setDid(String.valueOf(orderContacts.getOrderContactsId()));
    		dic.setCtrlCol(orderContacts.getUserName());
    		contactsList.add(dic);
		}
    	dropdown_order3_contracts.setSrcDictList(contactsList);
    	initContractSelect(contactsList);
	}
	
	/**
	 * 设置选中的店面
	 * 
	 * @param srcList
	 *            所有店面
	 */
	private void initContractSelect(List<Dictionary> srcList) {
		int contactId  = SharedPreferencesForOrder3Util.getInstance(carActivity).getOrder3ContactId();
		if (contactId!=0 && srcList.size() > 0) {
			for (int i = 0; i < srcList.size(); i++) {
				Dictionary dic = srcList.get(i);
				if (contactId == Integer.parseInt(dic.getDid())) {
					dropdown_order3_contracts.setSelected(dic);
					break;
				}
			}
		}
	}


	@Override
	public void onResult(List<Dictionary> result) {
		Dictionary dic = result.get(0);
		if (dic!=null) {
			order3Contact = orderContactsDB.findOrderContactsByContactsIdAndStoreId(Integer.parseInt(dic.getDid()), carActivity.storeId);
			carActivity.order3Contact = order3Contact;
			SharedPreferencesForOrder3Util.getInstance(carActivity).setOrder3ContactId(order3Contact.getOrderContactsId());
		}
	}

	public Order3Contact getOrder3Contact() {
		return order3Contact;
	}
	
	/**
	 * 同步联系人
	 */
	private Dialog synDialog = null;
	private void sycLXR(){
		synDialog =  new MyProgressDialog(carActivity,R.style.CustomProgressDialog,"正在同步联系人...");
		String storeId = SharedPreferencesForOrder3Util.getInstance(carActivity).getStoreId();
		if (!TextUtils.isEmpty(storeId)) {
			String url = UrlInfo.queryOrderNewUserInfo(carActivity, storeId);
			GcgHttpClient.getInstance(carActivity).post(url, null,new HttpResponseListener() {
				@Override
				public void onStart() {
					synDialog.show();
				}


				@Override
				public void onFailure(Throwable error, String content) {
					Toast.makeText(carActivity, "联系人同步失败", Toast.LENGTH_SHORT).show();

				}

				@Override
				public void onFinish() {
					synDialog.dismiss();
				}


				@Override
				public void onSuccess(int statusCode, String content) {
//					JLog.d(TAG, content);
					try {
						JSONObject obj = new JSONObject(content);
						String resultcode = obj.getString("resultcode");
						if ("0000".equals(resultcode)) {
							if (obj.has("orderUser") && !TextUtils.isEmpty(obj.getString("orderUser"))) {
								JSONArray arr = obj.getJSONArray("orderUser");
								JSONObject userObj = null;
								Order3Contact orderContacts = null;
								orderContactsDB.clearOrderContacts();
								Order3Parse parser = new Order3Parse(carActivity);
								for (int i = 0; i < arr.length(); i++) {
									userObj = arr.getJSONObject(i);
									orderContacts = parser.parserContact(userObj);
									orderContactsDB.insertOrderContants(orderContacts);
								}
								initContracts();
							}else{
								Toast.makeText(carActivity, "没有联系人信息", Toast.LENGTH_SHORT).show();
							}
						}else{
							Toast.makeText(carActivity, "联系人同步失败", Toast.LENGTH_SHORT).show();
						}
						
					} catch (Exception e) {
						e.printStackTrace();
						Toast.makeText(carActivity, "联系人同步失败", Toast.LENGTH_SHORT).show();
					}
				}

			});
		}else{
			Toast.makeText(carActivity, "请先选择客户", Toast.LENGTH_SHORT).show();
		}
	}
	private TextWatcher skWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			String sk = s.toString();
			if (!TextUtils.isEmpty(sk)) {
				if(sk.equals(".")){
					sk = "0";
				}else if (sk.startsWith(".")) {
					sk = "0"+sk;
				}else if(sk.endsWith(".")){
					sk = sk+"0";
				}
				SharedPreferencesForOrder3Util.getInstance(carActivity).setShouKuan(sk);
			}else{
				SharedPreferencesForOrder3Util.getInstance(carActivity).setShouKuan("0");
			}
		}
	};
	private TextWatcher jmWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			String jm = s.toString();
			if (!TextUtils.isEmpty(jm)) {
				if(jm.equals(".")){
					jm = "0";
				}else if (jm.startsWith(".")) {
					jm = "0"+jm;
				}else if(jm.endsWith(".")){
					jm = jm+"0";
				}
				SharedPreferencesForOrder3Util.getInstance(carActivity).setJianMian(jm);
			}else{
				SharedPreferencesForOrder3Util.getInstance(carActivity).setJianMian("0");
			}
		}
	};
	private TextWatcher msgWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			String msg = s.toString();
			if (!TextUtils.isEmpty(msg)) {
				SharedPreferencesForOrder3Util.getInstance(carActivity).setLeaveMessage(msg);
			}else{
				SharedPreferencesForOrder3Util.getInstance(carActivity).setLeaveMessage("");
			}

		}
	};
	
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_syn_user:
				sycLXR();
				break;

			default:
				break;
			}
		}
	};
}
