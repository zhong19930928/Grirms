package com.yunhu.yhshxc.visitors;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContractActivity extends AppCompatActivity implements View.OnClickListener{
    private ListView lv_txl_contract;
    private List<Map<String,Object>> mp=new ArrayList<>();
    private List<Map<String,Object>> mpOns=new ArrayList<>();
    private ContractAdapter adapter;
    private TextView tv_confirm;
    private ImageView iv_cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_txr_contract);
        initView();
    }

    private void initView() {
        lv_txl_contract = (ListView) findViewById(R.id.lv_txl_contract);
        iv_cancel = (ImageView) findViewById(R.id.iv_cancel);
        tv_confirm = (TextView) findViewById(R.id.tv_confirm);
        iv_cancel.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);

        mp = readContact();
        //实例一个简易适配器
        adapter = new ContractAdapter();
        //给listView设置适配器
        lv_txl_contract.setAdapter(adapter);
    }



    /**
     *得到联系人
     **/
    private List<Map<String,Object>> readContact() {
        String s = SharedPreferencesUtil.getInstance(this).getContracInfo();
        List<Map<String,Object>> temp = changeObject(s);
        List<Map<String,Object>> list= new ArrayList<>();
        //获取联系人
        Uri uri=ContactsContract.Contacts.CONTENT_URI;
        ContentResolver  cr = getContentResolver();
         Cursor cursor =cr.query(uri,null,null,null,null);
        for (int i = 0; i<temp.size();i++){
            if(temp.get(i).containsKey(TXRListActivity.ID)){
                String id = (String) temp.get(i).get(TXRListActivity.ID);
                if(id.equals("0")){
                    mpOns.add(temp.get(i));
                    continue;
                }
            }
        }
        while(cursor.moveToNext()){
            Map<String,Object> map = new HashMap<String,Object>();
            StringBuilder sb = new StringBuilder();
            //获取联系人的ID
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            //获取联系人的姓名
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            //构造联系人信息
            sb.append("contactId=").append(contactId).append(",Name=").append(name);
            map.put(TXRListActivity.NAME, name);
            map.put(TXRListActivity.ID,contactId);
            for (int i = 0; i<temp.size();i++){

                if(temp.get(i).containsKey(TXRListActivity.ID)){
                    String id = (String) temp.get(i).get(TXRListActivity.ID);
                    if(contactId.equals(id)){
                        map.put(TXRListActivity.IS_SELECT,true);
                        break;
                    }
                }
            }

            //查询电话类型的数据操作
            Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,
                    null, null);
            while(phones.moveToNext())
            {
                String phoneNumber = phones.getString(phones.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.NUMBER));
                //添加Phone的信息
                sb.append(",Phone=").append(phoneNumber);
                map.put(TXRListActivity.MOBILE, phoneNumber);
            }

            list.add(map);

        }

        return list;
    }
    public class ContractAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mp.size();
        }

        @Override
        public Object getItem(int position) {
            return mp.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView == null){
                convertView = LayoutInflater.from(ContractActivity.this).inflate(R.layout.txr_item_contract,null);
                holder = new ViewHolder();
                holder.cb_contract = (CheckBox) convertView.findViewById(R.id.cb_contract);
                holder.tv_contract_name = (TextView) convertView.findViewById(R.id.tv_contract_name);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_contract_name.setText((String) mp.get(position).get(TXRListActivity.NAME));

            final CheckBox cb= holder.cb_contract;
            final Map<String, Object> contarc = mp.get(position);
            if(contarc.containsKey(TXRListActivity.IS_SELECT)){
                boolean isSle = (boolean) contarc.get(TXRListActivity.IS_SELECT);
                if (isSle) {
                    cb.setChecked(true);
                } else {
                    cb.setChecked(false);
                }
            }else {
                cb.setChecked(false);
            }

            holder.cb_contract.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(cb.isChecked()){
                        contarc.put(TXRListActivity.IS_SELECT,true);
                    }else {
                        contarc.put(TXRListActivity.IS_SELECT,false);
                    }
                }
            });
            return convertView;
        }
    }
    class ViewHolder{
        public TextView tv_contract_name;
        public CheckBox cb_contract;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_confirm:
                save();
                break;
            case R.id.iv_cancel:
                finish();
                break;
        }
    }

    private void save() {
        StringBuffer sb =new StringBuffer();
        for (Map<String,Object> c :mp){
            if(c.containsKey(TXRListActivity.IS_SELECT)){
                boolean isSel = (boolean) c.get(TXRListActivity.IS_SELECT);
                if(isSel){
                    sb.append(c.get(TXRListActivity.NAME)).append(":").
                            append(c.get(TXRListActivity.MOBILE)).append(":").
                            append(c.get(TXRListActivity.ID)).append(";");
                }
            }

        }
        for (Map<String,Object> c :mpOns){
            sb.append(c.get(TXRListActivity.NAME)).append(":").
                    append(c.get(TXRListActivity.MOBILE)).append(":").
                    append(c.get(TXRListActivity.ID)).append(";");
        }

        SharedPreferencesUtil.getInstance(this).setContracInfo(sb.toString());
        setResult(200);
        finish();
    }

    public List<Map<String,Object>> changeObject(String str){
        List<Map<String,Object>> list = new ArrayList<>();
        String[] strs = str.split(";");
        for (int i = 0; i<strs.length;i++){
            String[] valuse = strs[i].split(":");
            Map<String,Object> map = new HashMap<>();
            if(valuse.length>0){
                map.put(TXRListActivity.NAME,valuse[0]);
            }
            if(valuse.length>1){
                map.put(TXRListActivity.MOBILE,valuse[1]);
            }
            if(valuse.length>2){
                map.put(TXRListActivity.ID,valuse[2]);
            }
            list.add(map);
        }
        return list;
    }
}
