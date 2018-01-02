package com.yunhu.yhshxc.inspection;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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
public class InspectionTaskActivity extends AbsBaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.task_listView)
    ListView taskListView;
    @BindView(R.id.title)
    TextView title;

    private List<Menu> list;
    private ListViewAdapter listViewAdapter;
    private List<String> menuIdList;
    private MainMenuDB db;
    private MenuItemClick menuItemClick;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection_task);
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
        listViewAdapter = new ListViewAdapter(this);
        taskListView.setAdapter(listViewAdapter);
        taskListView.setOnItemClickListener(this);
    }

    private void setData() {
        if (menuIdList == null) {
            return;
        }
        list = new ArrayList<>();
        for (String id : menuIdList) {
            try {
                Menu menu = db.findMenuListByMenuId(Integer.parseInt(id));
                if (menu != null) {
                    list.add(menu);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        listViewAdapter.setList(list);
    }

    @OnClick(R.id.person_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        menuItemClick = new MenuItemClick(list.get(position), this);
        menuItemClick.setOrder3Time();
    }
}
