package com.yunhu.yhshxc.activity.fragment;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.activity.addressBook.bo.AdressBookUser;
import com.yunhu.yhshxc.activity.addressBook.db.AdressBookUserDB;
import com.yunhu.yhshxc.widget.ToastOrder;

public class AddressDetailActivity extends AbsBaseActivity {
    private TextView tv_address_return;
    private TextView id_user_name, id_org_name;
    private TextView tv_zwmc;
    private TextView tv_bumc;
    private TextView tv_tel;
    private ImageView iv_tel;
    private ImageView iv_mail;
    private TextView tv_mail;

    private AdressBookUserDB userDB;
    AdressBookUser user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_detail);
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
        id_user_name = (TextView) findViewById(R.id.id_user_name);
        tv_zwmc = (TextView) findViewById(R.id.tv_zwmc);
        tv_bumc = (TextView) findViewById(R.id.tv_bumc);
        tv_tel = (TextView) findViewById(R.id.tv_tel);
        id_org_name = (TextView) findViewById(R.id.id_org_name);
        iv_tel = (ImageView) findViewById(R.id.iv_tel);
        iv_mail = (ImageView) findViewById(R.id.iv_mail);
        tv_mail = (TextView) findViewById(R.id.tv_mail);
        iv_mail.setOnClickListener(listener);
        iv_tel.setOnClickListener(listener);
    }

    private void initData(AdressBookUser user) {
        if (user != null) {
            id_user_name.setText(user.getUn());
            tv_tel.setText(user.getPn());
            tv_zwmc.setText(user.getRn());
            tv_bumc.setText(user.getOn());
            id_org_name.setText(user.getOp());
            tv_mail.setText(user.getMailAddr());
        }
    }

    private OnClickListener listener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_address_return:
                    finish();
                    break;

                case R.id.iv_tel:
                    telephone();
                    break;
                case R.id.iv_mail:
                    sendEmail();
                    break;
                default:
                    break;
            }
        }


    };

    private void sendEmail() {
       try {
           Intent intent = new Intent(Intent.ACTION_SENDTO);
           intent.setData(Uri.parse("mailto:"+user.getMailAddr()));
           intent.addCategory(Intent.CATEGORY_DEFAULT);
           startActivity(intent);
       }catch (Exception e){
           e.printStackTrace();
           Toast.makeText(this, "手机未安装邮箱应用", Toast.LENGTH_SHORT).show();
       }
    }

    private void telephone() {
        Intent intent = new Intent();

        //系统默认的action，用来打开默认的电话界面
        intent.setAction(Intent.ACTION_CALL);

        //需要拨打的号码
        intent.setData(Uri.parse("tel:" + user.getPn()));
        startActivity(intent);
    }

}
