package com.yunhu.yhshxc.visitors;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.yunhu.yhshxc.R;

/**
 * Created by xuelinlin on 2017/12/6.
 */

public class VisitorView {
    private Context context;
    private View view;
    private TextView tv_visitor_name;
    private TextView tv_visitor_phone;
    public VisitorView(Context context){
        this.context = context;
        view = View.inflate(context, R.layout.item_list_visitor,null);
        tv_visitor_phone = (TextView) view.findViewById(R.id.tv_visitor_phone);
        tv_visitor_name = (TextView) view.findViewById(R.id.tv_visitor_name);
    }
    public void initData(String name,String phone){
        tv_visitor_name.setText(name);
        tv_visitor_phone.setText(phone);
    }
    public View getView(){
        return view;
    }
}
