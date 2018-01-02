package com.yunhu.yhshxc.inspection;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.activity.HomeMenuFragment;
import com.yunhu.yhshxc.application.SoftApplication;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.bo.ShiHuaMenu;
import com.yunhu.yhshxc.database.MainMenuDB;
import com.yunhu.yhshxc.utility.PublicUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import view.CustomGridView;


/**
 * @author suhu
 * @time 2017/6/30 15:40
 */

public class SmartInspectionActivity extends AbsBaseActivity implements AdapterView.OnItemClickListener {

    /**
     * gridView间隔
     */
    private static final int SPACING = 40;
    /**
     * gridView一屏显示Item个数
     */
    private static final int NUMBER = 4;
    /**
     * HorizontalScrollView距左右两边距离
     */
    private static final int DISTANCE = 15;


    @BindView(R.id.gridView)
    CustomGridView gridView;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.smart_ll)
    LinearLayout smartLl;
    @BindView(R.id.title)
    TextView title;


    private List<Menu> list;
    private GridViewAdapter gridViewAdapter;

    private List<Menu> xList;
    private ListViewAdapter listViewAdapter;

    private MainMenuDB db;
    private MenuItemClick menuItemClick;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_inspection);
        ButterKnife.bind(this);
        initView();
        setGridViewData();
        setListViewData();
    }


    private void initView() {
        if (getIntent() != null) {
            ShiHuaMenu menu = (ShiHuaMenu) getIntent().getSerializableExtra("shiHuaMenu");
            if (menu != null) {
                list = menu.getMenuList();
                title.setText(menu.getFolderName());
            }
        }
        db = new MainMenuDB(this);

        gridViewAdapter = new GridViewAdapter(this);
        gridView.setAdapter(gridViewAdapter);
        gridView.setOnItemClickListener(this);

        listViewAdapter = new ListViewAdapter(this);
        listView.setAdapter(listViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                menuItemClick = new MenuItemClick(xList.get(position), SmartInspectionActivity.this);
                menuItemClick.setOrder3Time();
            }
        });
    }


    private void setGridViewData() {
        if (list == null) {
            return;
        }
        gridViewAdapter.setList(list);

        int size = list.size();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int itemWidth = (dm.widthPixels - 2 * dip2px(this, DISTANCE) - (NUMBER - 1) * SPACING) / NUMBER;
        int gridViewWidth = size * itemWidth + (size - 1) * SPACING;

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gridViewWidth, LinearLayout.LayoutParams.FILL_PARENT);
        gridView.setLayoutParams(params);
        gridView.setColumnWidth(itemWidth);
        gridView.setHorizontalSpacing(SPACING);
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setNumColumns(size);

        gridViewAdapter.notifyDataSetChanged();
    }

    private void setListViewData() {
        if (list == null) {
            return;
        }
        xList = new ArrayList<>();
        for (Menu menu : list) {
            if (getResources().getString(R.string.filter).equals(menu.getName())) {
                smartLl.setVisibility(View.VISIBLE);
                ArrayList<String> menuList = menu.getMenuIdList();
                for (int i = 0; i < menuList.size(); i++) {
                    if (i < 2) {
                        Menu m = null;
                        try {
                            m = db.findMenuListByMenuId(Integer.parseInt(menuList.get(i)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (m != null) {
                            xList.add(m);
                        }
                    }
                }
            }
        }
        listViewAdapter.setList(xList);
    }

    @OnClick(R.id.person_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (getResources().getString(R.string.filter).equals(list.get(position).getName())) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("menuIdList", list.get(position));
            Intent intent = new Intent(this, InspectionTaskActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            if (list.get(position).getMenuIdList().size() == 1) {
                HomeMenuFragment fragment = SoftApplication.getInstance().getHomeMenuFragment();
                Menu menu = null;
                try {
                    menu = db.findMenuListByMenuId(Integer.parseInt(list.get(position).getMenuIdList().get(0)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (menu == null){
                    Toast.makeText(this,"InvalidMenu",Toast.LENGTH_LONG).show();
                    return;
                }
                fragment.onItemclickMenu(menu, view, PublicUtils.getNetDate());
            } else {
                Bundle bundle = new Bundle();
                bundle.putSerializable("menuIdList", list.get(position));
                Intent intent = new Intent(this, InspectionMenuActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    }


    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
