package com.yunhu.yhshxc.visitors;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;

import java.util.HashMap;
import java.util.Map;

public class TXRActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView iv_txl_lxr,iv_cancel;
    private Button btn_cancel_txr,btn_confirm_txr;
    private EditText et_txr_name,et_txr_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_txr);
        initView();
    }

    private void initView() {
        iv_txl_lxr = (ImageView) findViewById(R.id.iv_txl_lxr);
        btn_cancel_txr = (Button) findViewById(R.id.btn_cancel_txr);
        btn_confirm_txr = (Button) findViewById(R.id.btn_confirm_txr);
        et_txr_name = (EditText) findViewById(R.id.et_txr_name);
        et_txr_number = (EditText) findViewById(R.id.et_txr_number);
        iv_cancel = (ImageView) findViewById(R.id.iv_cancel);
        //        et_txr_number.addTextChangedListener(watcher);
        iv_txl_lxr.setOnClickListener(this);
        btn_cancel_txr.setOnClickListener(this);
        btn_confirm_txr.setOnClickListener(this);
        iv_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_txl_lxr:
                goTXL();
                break;
            case R.id.btn_cancel_txr:
                finish();
                break;
            case R.id.iv_cancel:
                finish();
                break;
            case R.id.btn_confirm_txr:
                save();
                break;
        }
    }

    /**
     *保存
     */
    private void save() {
        String name = et_txr_name.getText().toString();
        String number = et_txr_number.getText().toString();
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this,"请输入姓名",Toast.LENGTH_SHORT).show();
            return;
        }
        if((TextUtils.isEmpty(number))){
            Toast.makeText(this,"请输入手机号",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!PublicUtils.isPhoneNumber(number)){
            Toast.makeText(this,"手机号格式不正确，请重新输入",Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String,Object> map = new HashMap<>();
        map.put(TXRListActivity.NAME,name);
        map.put(TXRListActivity.ID,0);
        map.put(TXRListActivity.MOBILE,number);
        StringBuffer sb = new StringBuffer();
        String str = SharedPreferencesUtil.getInstance(this).getContracInfo();
        sb.append(str).append(name).append(":").append(number).append(":").append("0").append(";");
        SharedPreferencesUtil.getInstance(this).setContracInfo(sb.toString());
        finish();
    }

    /**
     * 跳转到手机联系人列表
     */
    private void goTXL() {
        Intent intent= new Intent(this,ContractActivity.class);
        startActivityForResult(intent,100);
//        Uri uri = Uri.parse("content://contacts/people");
//        Intent intent = new Intent(Intent.ACTION_PICK, uri);
//        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==100&&resultCode==200){
           finish();
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (count == 1){
                int length = s.toString().length();
                if (length == 3 || length == 8){
                    et_txr_number.setText(s + " ");
                    et_txr_number.setSelection(et_txr_number.getText().toString().length());
                }
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
