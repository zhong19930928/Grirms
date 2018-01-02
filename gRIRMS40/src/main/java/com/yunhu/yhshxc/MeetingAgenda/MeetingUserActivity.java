package com.yunhu.yhshxc.MeetingAgenda;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.addressBook.PinnedHeaderExpandableListView;
import com.yunhu.yhshxc.activity.addressBook.bo.AdressBookUser;
import com.yunhu.yhshxc.activity.addressBook.db.AdressBookUserDB;
import com.yunhu.yhshxc.adapter.AddressBookAdapter;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.wechat.view.sortListView.ClearEditText;

import java.util.ArrayList;
import java.util.List;

import view.BarView;
import view.SearchEditText;
import view.TestBarView;


public class MeetingUserActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,View.OnClickListener{
    private TextView tv_cancel;
    private TextView tv_confirm;
    private SearchEditText activity_main_input_edittext;
    private ListView address_list;
    private List<AdressBookUser> listUser =new ArrayList<>();//所有的
    private AdressBookUserDB userDB;
    private AddressBookAdapter adapter;
    private TestBarView barView;
    private int position=-1;//标示是只能选择一个联系人
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_address_book);
        initDate();
        init();
    }

    private void initDate() {
        userDB = new AdressBookUserDB(this);
        String str = getIntent().getStringExtra("uIds");
        if(getIntent()!=null&&getIntent().hasExtra("position")){
            position=getIntent().getIntExtra("position",0);
        }
        if(!TextUtils.isEmpty(str)){
            String[] ids = str.split(",");
            for (int i = 0; i<ids.length;i++){
                userDB.updateAdressUser(1,1,Integer.parseInt(ids[i]));
            }
        }

    }

    private void init() {
        activity_main_input_edittext = (SearchEditText)findViewById(R.id.activity_main_input_edittext);
        address_list = (ListView) findViewById(R.id.address_list);
        barView = (TestBarView) findViewById(R.id.hs_book);
        activity_main_input_edittext.addTextChangedListener(watcher);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_confirm = (TextView) findViewById(R.id.tv_confirm);
        tv_cancel.setVisibility(View.VISIBLE);
        tv_confirm.setVisibility(View.VISIBLE);
        tv_confirm.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
        setData();
    }

    private void setData() {
        AdressBookUser user = userDB.findFirstLevelAdressBook();
        listUser.clear();
        if (user != null) {
            String[] ocs = user.getOc().split("-");
            String[] ops = user.getOp().split("-");
            if(ops.length>1&&ocs.length>1){
                List<AdressBookUser> listPer = userDB.findOrgBookUser(Integer.parseInt(ocs[1]));
                user.setOn(ops[1]);

                user.setOc(ocs[0]+"-"+ocs[1]);

                List<AdressBookUser> listOrg = userDB.findAllOrgBook(user.getOl(),user.getOc());
                List<AdressBookUser> listO = new ArrayList<>();
                for(AdressBookUser u :listOrg) {
                    if ("高管".equals(u.getOn())) {
                        listO.add(u);
                    }
                }
                for(AdressBookUser u :listOrg) {
                    if ("前台".equals(u.getOn())) {
                        listO.add(u);
                    }
                }
                for(AdressBookUser u :listOrg) {
                    if ("中后台".equals(u.getOn())) {
                        listO.add(u);
                    }
                }
                for(AdressBookUser u :listOrg) {
                    if ("发行".equals(u.getOn())) {
                        listO.add(u);
                    }
                }
                for(AdressBookUser u :listOrg) {
                    if ("北京中融鼎新投资管理有限公司".equals(u.getOn())) {
                        listO.add(u);
                    }
                }
                if(listO.size()>0){
                    listUser.addAll(listO);
                }else{
                    listUser.addAll(listOrg);
                }
                listUser.addAll(listPer);
            }else{
                user.setOn(ops[0]);
                user.setOc(ocs[0]);
                List<AdressBookUser> listPer = userDB.findOrgBookUser(Integer.parseInt(ocs[0]));
                List<AdressBookUser> listOrg = userDB.findAllOrgBook(user.getOl(),user.getOc());
                listUser.addAll(listOrg);
                listUser.addAll(listPer);
            }
            barView.addBt(user);
        }
        adapter = new AddressBookAdapter(this, listUser,true);
        address_list.setAdapter(adapter);
        address_list.setOnItemClickListener(this);
    }

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            fliter(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void fliter(String s) {

        if(TextUtils.isEmpty(s)){
            AdressBookUser user = userDB.findFirstLevelAdressBook();
            listUser.clear();
            if (user != null) {
//                List<AdressBookUser> listPer = userDB.findOrgBookUser(user.getoId());
                String[] ocs = user.getOc().split("-");

                String[] ops = user.getOp().split("-");
                if(ops.length>1&&ocs.length>1){
                    List<AdressBookUser> listPer = userDB.findOrgBookUser(Integer.parseInt(ocs[1]));
                    user.setOn(ops[1]);
                    user.setOc(ocs[0]+"-"+ocs[1]);

                    List<AdressBookUser> listOrg = userDB.findAllOrgBook(user.getOl(),user.getOc());
                    List<AdressBookUser> listO = new ArrayList<>();
                    for(AdressBookUser u :listOrg) {
                        if ("高管".equals(u.getOn())) {
                            listO.add(u);
                        }
                    }
                    for(AdressBookUser u :listOrg) {
                        if ("前台".equals(u.getOn())) {
                            listO.add(u);
                        }
                    }
                    for(AdressBookUser u :listOrg) {
                        if ("中后台".equals(u.getOn())) {
                            listO.add(u);
                        }
                    }
                    for(AdressBookUser u :listOrg) {
                        if ("发行".equals(u.getOn())) {
                            listO.add(u);
                        }
                    }
                    for(AdressBookUser u :listOrg) {
                        if ("北京中融鼎新投资管理有限公司".equals(u.getOn())) {
                            listO.add(u);
                        }
                    }
                    if(listO.size()>0){
                        listUser.addAll(listO);
                    }else{
                        listUser.addAll(listOrg);
                    }
                    barView.addBt(user);
                    listUser.addAll(listPer);
                    adapter.refresh(listUser);
                }else{
                    user.setOn(ops[0]);
                    user.setOc(ocs[0]);
                    List<AdressBookUser> listPer = userDB.findOrgBookUser(Integer.parseInt(ocs[0]));
                    List<AdressBookUser> listOrg = userDB.findAllOrgBook(user.getOl(),user.getOc());
                    barView.addBt(user);
                    listUser.addAll(listOrg);
                    listUser.addAll(listPer);
                    adapter.refresh(listUser);
                }

            }
        }else{
            barView.addBt(null);
            List<AdressBookUser> list = userDB.findListUserByName(s);
            listUser.clear();
            listUser.addAll(list);
            adapter.refresh(listUser);
        }
    }




    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_confirm:
                    setInte();
                    break;
                case R.id.tv_cancel:

                    cancelOp();
                    break;
            }
        }
    };

    private void setInte() {
        StringBuffer sbId = new StringBuffer();
        StringBuffer sbNa = new StringBuffer();
        List<AdressBookUser> us = userDB.findVisitedUsers();
        for(int i = 0; i<us.size();i++){
            sbId.append(us.get(i).getuId()).append(",");
            sbNa.append(us.get(i).getUn()).append(",");
            us.get(i).setIsVisit(0);
            userDB.updateAdressUser(us.get(i));

        }

        if(position!=-1){
            if(sbId.toString()!=null&&!"".equals(sbId.toString())){
                if(sbId.toString().split(",").length>1){
                    Toast.makeText(this,"您最多只能选择一个联系人",Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    Intent intent = new Intent();
                    intent.putExtra("usId",sbId.toString());
                    intent.putExtra("usName",sbNa.toString());
                    intent.putExtra("position",position);
                    setResult(RESULT_OK,intent);
                    finish();
                }

            }else{
                Toast.makeText(this,"您还没有选择联系人",Toast.LENGTH_SHORT).show();
                return;
            }

        }else{
            Intent intent = new Intent();
            intent.putExtra("usId",sbId.toString());
            intent.putExtra("usName",sbNa.toString());
            setResult(RESULT_OK,intent);
            finish();
        }
    }

    public void cancelOp(){
        List<AdressBookUser> childrenData = userDB.findVisitedUsers();
        for (AdressBookUser us:childrenData){
            us.setIsVisit(0);
            userDB.updateAdressUser(us);
        }
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            cancelOp();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        AdressBookUser user = listUser.get(position);
        if (user != null) {
            if (user.isOrg()) {
                barView.addBt(user);
                barView.setBarListener(new BarView.BarViewListener() {
                    @Override
                    public void open(View v, AdressBookUser o, int postion) {
                    }

                    @Override
                    public void close(View v, AdressBookUser o, int postion) {
                        refreshData(o);
                    }
                });
                refreshData(user);
            }
        }
    }
    private void refreshData(AdressBookUser user) {
//        List<AdressBookUser> listPer = userDB
//                .findOrgBookUser(user.getoId());
        String[] ocs = user.getOc().split("-");
        List<AdressBookUser> listPer = userDB.findOrgBookUser(Integer.parseInt(ocs[ocs.length-1]));
        List<AdressBookUser> listOrg = userDB.findAllOrgBook(user.getOl(),
                user.getOc());
        List<AdressBookUser> listO = new ArrayList<>();
        listUser.clear();
        if("中融国际信托有限公司".equals(user.getOn())){

            for(AdressBookUser u :listOrg) {
                if ("高管".equals(u.getOn())) {
                    listO.add(u);
                }
            }
            for(AdressBookUser u :listOrg) {
                if ("前台".equals(u.getOn())) {
                    listO.add(u);
                }
            }
            for(AdressBookUser u :listOrg) {
                if ("中后台".equals(u.getOn())) {
                    listO.add(u);
                }
            }
            for(AdressBookUser u :listOrg) {
                if ("发行".equals(u.getOn())) {
                    listO.add(u);
                }
            }
            for(AdressBookUser u :listOrg) {
                if ("北京中融鼎新投资管理有限公司".equals(u.getOn())) {
                    listO.add(u);
                }
            }
            listUser.addAll(listO);
        }else {
            listUser.addAll(listOrg);
        }
        listUser.addAll(listPer);
        adapter.refresh(listUser);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_confirm:
                setInte();
                break;
            case R.id.tv_cancel:
                cancelOp();
                break;
        }
    }
}
