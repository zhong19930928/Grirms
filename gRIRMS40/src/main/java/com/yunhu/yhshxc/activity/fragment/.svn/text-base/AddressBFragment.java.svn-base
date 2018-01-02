package com.yunhu.yhshxc.activity.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.addressBook.bo.AdressBookUser;
import com.yunhu.yhshxc.activity.addressBook.db.AdressBookUserDB;
import com.yunhu.yhshxc.adapter.AddressBookAdapter;

import java.util.ArrayList;
import java.util.List;

import view.BarView;
import view.SearchEditText;
import view.TestBarView;

import static com.yunhu.yhshxc.R.anim.refresh;


public class AddressBFragment extends Fragment implements AdapterView.OnItemClickListener {

    private Context context;
    private View rootView;
    private SearchEditText activity_main_input_edittext;
    private ListView address_list;
    private List<AdressBookUser> listUser =new ArrayList<>();//所有的
    private AdressBookUserDB userDB;
    private AddressBookAdapter adapter;
    private TestBarView barView;
    private TextView tv_title_address;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        rootView = inflater.inflate(R.layout.fragment_address_book, container, false);
        userDB = new AdressBookUserDB(getActivity());
        init(rootView);
        initData();
        return rootView;
    }


    private void init(View view) {
        activity_main_input_edittext = (SearchEditText) view.findViewById(R.id.activity_main_input_edittext);
        address_list = (ListView) view.findViewById(R.id.address_list);
        barView = (TestBarView) view.findViewById(R.id.hs_book);
        activity_main_input_edittext.addTextChangedListener(watcher);
        tv_title_address = (TextView) view.findViewById(R.id.tv_title_address);
        tv_title_address.setText("通讯录");
    }

    private void initData(){
        AdressBookUser user = userDB.findFirstLevelAdressBook();
        listUser.clear();
        /*****************************************/

        /******************************************/
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
                List<AdressBookUser> listPer = userDB.findOrgBookUser(Integer.parseInt(ocs[0]));
                user.setOn(ops[0]);
                user.setOc(ocs[0]);

                List<AdressBookUser> listOrg = userDB.findAllOrgBook(user.getOl(),user.getOc());
                listUser.addAll(listOrg);
                listUser.addAll(listPer);
            }
            barView.addBt(user);
        }
        adapter = new AddressBookAdapter(getActivity(), listUser,false);
        address_list.setAdapter(adapter);
        address_list.setOnItemClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
            } else {
                Intent intent = new Intent(getActivity(),AddressDetailActivity.class);
                intent.putExtra("userId",user.getuId());
                startActivity(intent);
            }
        }
    }

    private void refreshData(AdressBookUser user) {

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
        barView.addBt(null);
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
                    List<AdressBookUser> listPer = userDB.findOrgBookUser(Integer.parseInt(ocs[0]));
                    user.setOn(ops[0]);
                    user.setOc(ocs[0]);

                    List<AdressBookUser> listOrg = userDB.findAllOrgBook(user.getOl(),user.getOc());
                    barView.addBt(user);
                    listUser.addAll(listOrg);
                    listUser.addAll(listPer);
                    adapter.refresh(listUser);
                }

            }
        }else{
            List<AdressBookUser> list = userDB.findListUserByName(s);
            listUser.clear();
            listUser.addAll(list);
            adapter.refresh(listUser);
        }
    }

    private List<AdressBookUser> addZc(){
        List<AdressBookUser> list= new ArrayList<>();
        List<AdressBookUser> dszList = userDB.findUserByRoleName("董事长");
        if(dszList.size()>0){
            AdressBookUser adressBookUser = dszList.get(0);
            adressBookUser.setOrg(true);
            adressBookUser.setOn("董事长");
            list.add(adressBookUser);
        }
        List<AdressBookUser> dwsjList = userDB.findUserByRoleName("党委书记");
        if(dwsjList.size()>0){
            AdressBookUser adressBookUser = dwsjList.get(0);
            adressBookUser.setOrg(true);
            adressBookUser.setOn("党委书记");
            list.add(adressBookUser);
        }
        List<AdressBookUser> zcList = userDB.findUserByRoleName("总裁");
        if(zcList.size()>0){
            AdressBookUser adressBookUser = zcList.get(0);
            adressBookUser.setOrg(true);
            adressBookUser.setOn("总裁");
            list.add(adressBookUser);
        }
        List<AdressBookUser> jszList = userDB.findUserByRoleName("监事长");
        if(jszList.size()>0){
            AdressBookUser adressBookUser = jszList.get(0);
            adressBookUser.setOrg(true);
            adressBookUser.setOn("监事长");
            list.add(adressBookUser);
        }
        List<AdressBookUser> cwfzcList = userDB.findUserByRoleName("常务副总裁");
        if(cwfzcList.size()>0){
            AdressBookUser adressBookUser = cwfzcList.get(0);
            adressBookUser.setOrg(true);
            adressBookUser.setOn("常务副总裁");
            list.add(adressBookUser);
        }
        List<AdressBookUser> fzcList = userDB.findUserByRoleName("副董事长");
        if(fzcList.size()>0){
            AdressBookUser adressBookUser = fzcList.get(0);
            adressBookUser.setOrg(true);
            adressBookUser.setOn("副董事长");
            list.add(adressBookUser);
        }
        List<AdressBookUser> jwList = userDB.findUserByRoleName("党委副书记、纪委书记");
        if(jwList.size()>0){
            AdressBookUser adressBookUser = jwList.get(0);
            adressBookUser.setOrg(true);
            adressBookUser.setOn("党委副书记、纪委书记");
            list.add(adressBookUser);
        }
        List<AdressBookUser> fList = userDB.findUserByRoleName("副总裁");
        if(fList.size()>0){
            AdressBookUser adressBookUser = fList.get(0);
            adressBookUser.setOrg(true);
            adressBookUser.setOn("副总裁");
            list.add(adressBookUser);
        }
        List<AdressBookUser> cwzjList = userDB.findUserByRoleName("财务总监");
        if(cwzjList.size()>0){
            AdressBookUser adressBookUser = cwzjList.get(0);
            adressBookUser.setOrg(true);
            adressBookUser.setOn("财务总监");
            list.add(adressBookUser);
        }
        List<AdressBookUser> xzList = userDB.findUserByRoleName("行政总监");
        if(xzList.size()>0){
            AdressBookUser adressBookUser = xzList.get(0);
            adressBookUser.setOrg(true);
            adressBookUser.setOn("行政总监");
            list.add(adressBookUser);
        }
        List<AdressBookUser> hgList = userDB.findUserByRoleName("合规总监");
        if(hgList.size()>0){
            AdressBookUser adressBookUser = hgList.get(0);
            adressBookUser.setOrg(true);
            adressBookUser.setOn("合规总监");
            list.add(adressBookUser);
        }
        return list;
    }

}
