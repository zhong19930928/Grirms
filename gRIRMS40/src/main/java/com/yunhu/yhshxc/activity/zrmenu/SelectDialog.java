package com.yunhu.yhshxc.activity.zrmenu;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.zrmenu.adapter.DialogAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author suhu
 * @data 2017/10/25.
 * @description
 */

public class SelectDialog extends Dialog {
    private List<String> list = new ArrayList<>();
    private ListView listView;
    private DialogAdapter adapter;
    private Context context;
    private OnSelectData  onSelectData;
    private SelectDialog dialog;
    private RelativeLayout rl;


    public SelectDialog(@NonNull Context context, List<String> list) {
        super(context, R.style.MyDialog);
        this.context = context;
        this.list = list;
        dialog = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_selet);
        initView();
    }

    private void initView() {
        rl = (RelativeLayout) findViewById(R.id.rl);
        if (list.size()<4){
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(300*3,list.size()*50*3);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            rl.setLayoutParams(params);
        }


        listView = (ListView) findViewById(R.id.list_view);
        adapter = new DialogAdapter(context,list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onSelectData.selectData(list.get(position),dialog);
            }
        });
    }

    public void setOnSelectData(OnSelectData onSelectData) {
        this.onSelectData = onSelectData;
    }

    public interface OnSelectData{
        void selectData(String data,Dialog dialog);
    }

    @Override
    public void show() {
        super.show();
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity= Gravity.CENTER;
        layoutParams.width= WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height= WindowManager.LayoutParams.MATCH_PARENT;

        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);

    }
}
