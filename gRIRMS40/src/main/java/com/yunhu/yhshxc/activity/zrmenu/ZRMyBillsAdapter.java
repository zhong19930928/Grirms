package com.yunhu.yhshxc.activity.zrmenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.AssetsBean;

import java.util.List;

/**
 *@author zhonghuibin
 *@time 2017/10/24 15:40
 *
*/

public class ZRMyBillsAdapter extends BaseAdapter{
    private Context context;
    private List<AssetsBean> list;

    public ZRMyBillsAdapter(Context context,List<AssetsBean> list){
        super();
        this.context = context;
        this.list = list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHoder viewHoder;
        if (view == null) {
            viewHoder = new ViewHoder();
            view = LayoutInflater.from(context).inflate(
                    R.layout.item_mybills_listview, viewGroup, false);//item布局
            viewHoder.tv1 = (TextView) view.findViewById(R.id.bills_number);
            viewHoder.tv2 = (TextView) view.findViewById(R.id.bills_type);
            viewHoder.tv3 = (TextView) view.findViewById(R.id.bills_date);
            viewHoder.tv4 = (TextView) view.findViewById(R.id.bills_condition);
            view.setTag(viewHoder);
        } else {
            viewHoder = (ViewHoder) view.getTag();
        }
        viewHoder.tv1.setText(list.get(i).getTitleTip()+" "+list.get(i).getTitle());
        viewHoder.tv2.setText(list.get(i).getNumTip()+":"+list.get(i).getNum());
        viewHoder.tv3.setText(list.get(i).getSnTip()+":"+list.get(i).getSn());
        viewHoder.tv4.setText(list.get(i).getCodeTip()+" "+list.get(i).getCode());
        return view;
    }

    class ViewHoder {
        TextView tv1;
        TextView tv2;
        TextView tv3;
        TextView tv4;
    }//item布局里面所需要的元素
}
