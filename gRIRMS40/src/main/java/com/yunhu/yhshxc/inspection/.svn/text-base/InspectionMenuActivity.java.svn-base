package com.yunhu.yhshxc.inspection;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.database.MainMenuDB;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *@author suhu
 *@time 2017/6/30 15:40
 *
*/

public class InspectionMenuActivity extends AbsBaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.menu_gridView)
    GridView menuGridView;
    @BindView(R.id.title)
    TextView title;

    private GridViewAdapter menuAdapter;
    private List<Menu> menuList;
    private List<String> menuIdList;
    private MainMenuDB db;
    private MenuItemClick menuItemClick;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection_menu);
        ButterKnife.bind(this);

        initView();
        setData();

    }

    private void initView() {
        if (getIntent() != null) {
            Menu menu = (Menu) getIntent().getSerializableExtra("menuIdList");
            if (menu != null) {
                menuIdList = menu.getMenuIdList();
                title.setText(menu.getName()+"");
            }
        }
        db = new MainMenuDB(this);
        menuAdapter = new GridViewAdapter(this);
        menuGridView.setAdapter(menuAdapter);
        menuGridView.setOnItemClickListener(this);
    }


    private void setData() {
        if (menuIdList == null) {
            return;
        }
        menuList = new ArrayList<>();
        for (String id : menuIdList) {
            try {
                Menu menu = db.findMenuListByMenuId(Integer.parseInt(id));
                if (menu != null) {
                    menuList.add(menu);
                }
            } catch (Exception e) {
            }
        }
        menuAdapter.setList(menuList);
    }

    @OnClick(R.id.person_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        menuItemClick = new MenuItemClick(menuList.get(position), this);
        menuItemClick.setOrder3Time();
    }
}
