package com.yunhu.yhshxc.activity.addressBook;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.activity.addressBook.bo.AdressBookUser;
import com.yunhu.yhshxc.activity.addressBook.db.AdressBookUserDB;
import com.yunhu.yhshxc.widget.ToastOrder;

public class AddressBookDetailActivity extends AbsBaseActivity {
	private TextView tv_address_return;
	private TextView tv_tel_name;
	private TextView tv_address_zhiwei;
	private TextView tv_address_detail;
	private TextView tv_tel_num;
	private LinearLayout ll_message;
	private LinearLayout ll_siliao_address;
	private LinearLayout ll_address_save;
	private LinearLayout ll_tel;
	private AdressBookUserDB userDB;
	AdressBookUser user;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.address_xiangxi);
		userDB = new AdressBookUserDB(this);
		Intent intent = getIntent();
		int uId = intent.getIntExtra("userId", 0);
		 user = userDB.findUserById(uId);
		initView();
		initData(user);
	}
	
	private void initView() {
		tv_address_return = (TextView) findViewById(R.id.tv_address_return);
		tv_address_return.setOnClickListener(listener);
		tv_tel_name = (TextView) findViewById(R.id.tv_tel_name);
		tv_address_zhiwei = (TextView) findViewById(R.id.tv_address_zhiwei);
		tv_address_detail = (TextView) findViewById(R.id.tv_address_detail);
		tv_tel_num = (TextView) findViewById(R.id.tv_tel_nummber);
		ll_tel =  (LinearLayout) findViewById(R.id.ll_tel);
		ll_tel.setOnClickListener(listener);
		ll_message = (LinearLayout) findViewById(R.id.ll_message);
		ll_message.setOnClickListener(listener);
		ll_siliao_address = (LinearLayout) findViewById(R.id.ll_siliao_address);
		ll_siliao_address.setOnClickListener(listener);
		ll_address_save = (LinearLayout) findViewById(R.id.ll_address_save);
		ll_address_save.setOnClickListener(listener);
	}
	private void initData(AdressBookUser user) {
		if(user!=null){
			tv_tel_name.setText(user.getUn());
			tv_tel_num.setText(user.getPn());
			tv_address_zhiwei.setText(user.getRn());
			tv_address_detail.setText(user.getOn());
		}
	}
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_address_return:
				finish();
				break;
			case R.id.ll_message:
				sendMessage();
				break;
			case R.id.ll_siliao_address:
				
				break;
			case R.id.ll_address_save:
				save();
				break;
			case R.id.ll_tel:
				telephone();
				break;
			default:
				break;
			}
		}

		

		
	};
	private void save() {
		if(user!=null){
			String name = user.getUn();
			String number = user.getPn();
			addContact(name, number);
			ToastOrder.makeText(this, R.string.save_sucessful3, ToastOrder.LENGTH_LONG).show();;
		}else{
			ToastOrder.makeText(this, R.string.save_failed, ToastOrder.LENGTH_LONG).show();;
		}
		
	}
	public void addContact(String name, String phoneNum) {  
        ContentValues values = new ContentValues();  
        Uri rawContactUri = getContentResolver().insert(  
                RawContacts.CONTENT_URI, values);  
        long rawContactId = ContentUris.parseId(rawContactUri);  
        // 向data表插入数据  
        if (name != "") {  
            values.clear();  
            values.put(Data.RAW_CONTACT_ID, rawContactId);  
            values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);  
            values.put(StructuredName.GIVEN_NAME, name);  
            getContentResolver().insert(ContactsContract.Data.CONTENT_URI,  
                    values);  
        }  
        // 向data表插入电话号码  
        if (phoneNum != "") {  
            values.clear();  
            values.put(Data.RAW_CONTACT_ID, rawContactId);  
            values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);  
            values.put(Phone.NUMBER, phoneNum);  
            values.put(Phone.TYPE, Phone.TYPE_MOBILE);  
            getContentResolver().insert(ContactsContract.Data.CONTENT_URI,  
                    values);  
        }  
    }  
	private void telephone() {
		Intent intent = new Intent();

		//系统默认的action，用来打开默认的电话界面
		intent.setAction(Intent.ACTION_CALL);

		//需要拨打的号码
		intent.setData(Uri.parse("tel:"+user.getPn()));
		startActivity(intent);
	}

	private void sendMessage() {
		Intent intent = new Intent();

		//系统默认的action，用来打开默认的短信界面
		intent.setAction(Intent.ACTION_SENDTO);

		//需要发短息的号码
		intent.setData(Uri.parse("smsto:"+user.getPn()));
		startActivity(intent);
	}
}
