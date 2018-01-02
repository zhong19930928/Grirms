package com.yunhu.yhshxc.visitors;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zhy.http.okhttp.OkHttpUtils.delete;

public class TXRListActivity extends AppCompatActivity implements View.OnClickListener{
    public final static String NAME = "name";
    public final static String MOBILE = "mobile";
    public final static String ID = "id";
    public final static String IS_SELECT = "isSelect";

    private ListView lv_txl_contract;
    private Button btn_add_txr;
    private List<Map<String,Object>> mp = new ArrayList<>();
    private ContractAdapter adapter;
    private TextView tv_bj;
    private ImageView tv_cancel;
    private boolean isDelete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_txr_list);
        initView();
    }

    private void initView() {
        lv_txl_contract = (ListView) findViewById(R.id.lv_txl_contract);
        btn_add_txr= (Button) findViewById(R.id.btn_add_txr);
        btn_add_txr.setOnClickListener(this);
        tv_bj= (TextView) findViewById(R.id.tv_bj);
        tv_cancel= (ImageView) findViewById(R.id.tv_cancel);
        if(isDelete){
            tv_bj.setText("删 除");
        }else {
            tv_bj.setText("编 辑");
        }
        tv_bj.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
        String str = SharedPreferencesUtil.getInstance(this).getContracInfo();
        if(TextUtils.isEmpty(str)){
            Intent intent = new Intent(TXRListActivity.this,TXRActivity.class);
            startActivity(intent);
        }
        mp = changeObject(str);
        adapter = new ContractAdapter();
        lv_txl_contract.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add_txr:
                isDelete = false;
                tv_bj.setText("编 辑");
                Intent intent = new Intent(TXRListActivity.this,TXRActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_bj://编辑 选择框显示
                if(isDelete){
                    isDelete = false;
                    tv_bj.setText("编 辑");
                    deleteList();
                }else{
                    isDelete = true;
                    tv_bj.setText("删 除");
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.tv_cancel:
                insertSubmit();
                break;
        }
    }

    private void insertSubmit() {
//        String value = SharedPreferencesUtil.getInstance(this).getContracInfo();
//        if(!TextUtils.isEmpty(value)){
//
//        }
        finish();
    }

    private void deleteList() {
        StringBuffer sb =new StringBuffer();
        for (Map<String,Object> c :mp){
            if(c.containsKey(TXRListActivity.IS_SELECT)){
                boolean isSel = (boolean) c.get(TXRListActivity.IS_SELECT);
                if(!isSel){
                    sb.append(c.get(TXRListActivity.NAME)).append(":").
                            append(c.get(TXRListActivity.MOBILE)).append(":").
                            append(c.get(TXRListActivity.ID)).append(";");
                }
            }else {
                sb.append(c.get(TXRListActivity.NAME)).append(":").
                        append(c.get(TXRListActivity.MOBILE)).append(":").
                        append(c.get(TXRListActivity.ID)).append(";");
            }

        }

        SharedPreferencesUtil.getInstance(this).setContracInfo(sb.toString());
        mp.clear();
        mp = changeObject(sb.toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        //查询有误数据，有数据刷新界面，没有的话就退出
        String str = SharedPreferencesUtil.getInstance(this).getContracInfo();
        if(TextUtils.isEmpty(str)){
//           finish();
        }else{
            mp.clear();
            mp = changeObject(str);
            adapter.notifyDataSetChanged();
        }
    }

    public class ContractAdapter extends BaseAdapter {

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
                convertView = LayoutInflater.from(TXRListActivity.this).inflate(R.layout.txr_item_contract_list,null);
                holder = new ViewHolder();
                holder.tv_contract_name = (TextView) convertView.findViewById(R.id.tv_contract_name);
                holder.tv_number_list = (TextView) convertView.findViewById(R.id.tv_number_list);
                holder.cb_contract_item = (CheckBox) convertView.findViewById(R.id.cb_contract_item);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            String name = (String) mp.get(position).get(TXRListActivity.NAME);
            String number = (String) mp.get(position).get(TXRListActivity.MOBILE);
            holder.tv_contract_name.setText(name);
            holder.tv_number_list.setText(number);

            final CheckBox cb= holder.cb_contract_item;
            final Map<String, Object> contarc = mp.get(position);
            if(isDelete){
                cb.setVisibility(View.VISIBLE);
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
            }else {
                cb.setVisibility(View.GONE);
            }
            holder.cb_contract_item.setOnClickListener(new View.OnClickListener() {
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
        public TextView tv_number_list;
        public CheckBox cb_contract_item;

    }

    public List<Map<String,Object>> changeObject(String str){

        List<Map<String,Object>> list = new ArrayList<>();
        if(!TextUtils.isEmpty(str)){
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
        }
        return list;
    }
}
