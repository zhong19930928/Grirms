package com.yunhu.yhshxc.activity.carSales.scene.sellingGoods;

import gcg.org.debug.JLog;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesContact;
import com.yunhu.yhshxc.activity.carSales.db.CarSalesContactsDB;
import com.yunhu.yhshxc.activity.carSales.util.SharedPreferencesForCarSalesUtil;
import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.parser.CarSalesParse;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.DropDown;
import com.yunhu.yhshxc.widget.DropDown.OnResultListener;

public class CarSaleShouKuanFragment extends Fragment implements OnResultListener{
	
	private final String TAG = "CarSaleShouKuanFragment";
	private EditText et_shoukuan;
	private EditText et_sub;
	private EditText et_leave_message;
	private DropDown dropdown_order3_contracts;
	private CarSalesContactsDB contactsDB = null;
	private List<CarSalesContact> carSalesContactsList = null;
	private CarSaleShoppingCartActivity carActivity = null;
	private CarSalesContact order3Contact;
	private Button btn_syn_user;
	private LinearLayout ll_order3_contact;
	private CheckBox car_scale_clear;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
//		View view = inflater.inflate(R.layout.car_scale_gethering_message, null);
		View view = inflater.inflate(R.layout.car_scale_gethering_message, container, false);
		carActivity = (CarSaleShoppingCartActivity) getActivity();
		contactsDB = new CarSalesContactsDB(carActivity);
		initView(view);
		initContracts();
		return view;
	}

	private void initView(View view) {
		car_scale_clear = (CheckBox) view.findViewById(R.id.car_scale_clear);
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
		int isOrderUser = SharedPreferencesForCarSalesUtil.getInstance(carActivity).getIsCarSalesUser();
		if (isOrderUser == 1) {
			ll_order3_contact.setVisibility(View.GONE);
		}else{
			ll_order3_contact.setVisibility(View.VISIBLE);

		}
		getMessage();
		car_scale_clear.setOnCheckedChangeListener(changeListner);
	}
	
	private OnCheckedChangeListener changeListner = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if(isChecked){
				SharedPreferencesForCarSalesUtil.getInstance(carActivity).setIsStatus("1");
			}else{
				SharedPreferencesForCarSalesUtil.getInstance(carActivity).setIsStatus("0");
			}
		}
	};
	
	@Override
	public void onResume() {
		super.onResume();
		//设置本界面内容
		getMessage();
		
	}
	
	private void getMessage() {
		String sk = SharedPreferencesForCarSalesUtil.getInstance(carActivity).getShouKuan();
		et_shoukuan.setText("0".equals(sk)?"":sk);
		et_leave_message.setText(SharedPreferencesForCarSalesUtil.getInstance(carActivity).getLeaveMessage());
		String jm = SharedPreferencesForCarSalesUtil.getInstance(carActivity).getJianMian();
		et_sub.setText("0".equals(jm)?"":jm);

	}
	
	/**
	 * 初始化联系人
	 */
	private void initContracts(){
		carSalesContactsList = contactsDB.findAllCarSalesContactsByStoreId(carActivity.storeId);
		List<Dictionary> contactsList = new ArrayList<Dictionary>();
		CarSalesContact orderContacts = null;
    	for (int i = 0; i < carSalesContactsList.size(); i++) {
    		orderContacts = carSalesContactsList.get(i);
    		Dictionary dic = new Dictionary();
    		dic.setDid(String.valueOf(orderContacts.getContactsId()));
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
		int contactId  = SharedPreferencesForCarSalesUtil.getInstance(carActivity).getCarSalesContactId();
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
			order3Contact = contactsDB.findCarSalesContactByContactsIdAndStoreId(Integer.parseInt(dic.getDid()), carActivity.storeId);
			carActivity.carSalesContact = order3Contact;
			SharedPreferencesForCarSalesUtil.getInstance(carActivity).setCarSalesContactId(order3Contact.getContactsId());
		}
	}

	public CarSalesContact getCarSalesContact() {
		return order3Contact;
	}
	
	/**
	 * 同步联系人
	 */
	private Dialog synDialog = null;
	private void sycLXR(){
		synDialog =  new MyProgressDialog(carActivity,R.style.CustomProgressDialog,"正在同步联系人...");
		String storeId = SharedPreferencesForCarSalesUtil.getInstance(carActivity).getStoreId();
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
					JLog.d(TAG, content);
					try {
						JSONObject obj = new JSONObject(content);
						String resultcode = obj.getString("resultcode");
						if ("0000".equals(resultcode)) {
							if (obj.has("orderUser") && !TextUtils.isEmpty(obj.getString("orderUser"))) {
								JSONArray arr = obj.getJSONArray("orderUser");
								JSONObject userObj = null;
								CarSalesContact orderContacts = null;
								contactsDB.clearCarSalesContacts();
								CarSalesParse parser = new CarSalesParse(carActivity);
								for (int i = 0; i < arr.length(); i++) {
									userObj = arr.getJSONObject(i);
									orderContacts = parser.parserContact(userObj);
									contactsDB.insertCarSalesContact(orderContacts);;
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
				SharedPreferencesForCarSalesUtil.getInstance(carActivity).setShouKuan(sk);
			}else{
				SharedPreferencesForCarSalesUtil.getInstance(carActivity).setShouKuan("0");
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
				SharedPreferencesForCarSalesUtil.getInstance(carActivity).setJianMian(jm);
			}else{
				SharedPreferencesForCarSalesUtil.getInstance(carActivity).setJianMian("0");
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
				SharedPreferencesForCarSalesUtil.getInstance(carActivity).setLeaveMessage(msg);
			}else{
				SharedPreferencesForCarSalesUtil.getInstance(carActivity).setLeaveMessage("");
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
