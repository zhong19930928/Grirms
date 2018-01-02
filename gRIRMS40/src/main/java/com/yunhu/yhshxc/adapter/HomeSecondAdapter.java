package com.yunhu.yhshxc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.HomeBean;

import java.util.List;

/**
 * ＠author zhonghuibin
 * create at 2017/12/13.
 * describe 二级页面适配器
 */
public class HomeSecondAdapter extends BaseAdapter {
    private Context mContext;
    private List<HomeBean.DataBeanX.DataBean.ListBean> mList;

    public HomeSecondAdapter(Context context, List<HomeBean.DataBeanX.DataBean.ListBean> list){
        mContext = context;
        mList = list;
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHoder viewHoder;
        if (view == null) {
            viewHoder = new ViewHoder();
            view = LayoutInflater.from(mContext).inflate(
                    R.layout.home_listview_item, viewGroup, false);//item布局
            viewHoder.tv1 = (TextView) view.findViewById(R.id.tv_item1);
            viewHoder.tv2 = (TextView) view.findViewById(R.id.tv_item2);
            viewHoder.tv3 = (TextView) view.findViewById(R.id.tv_item3);
            viewHoder.tv4 = (TextView) view.findViewById(R.id.tv_item4);
            viewHoder.iv1 = (ImageView) view.findViewById(R.id.iv_item1);
            view.setTag(viewHoder);
        } else {
            viewHoder = (ViewHoder) view.getTag();
        }
        viewHoder.tv1.setText(mList.get(i).getTitle());
        viewHoder.tv2.setText(mList.get(i).getInput_time());
        viewHoder.tv3.setText(mList.get(i).getDescription());
        viewHoder.tv4.setText(mList.get(i).getInput_histime());
        Glide.with(mContext).load(mList.get(i).getCover_thumb()).into(viewHoder.iv1);
        return view;
    }

    class ViewHoder {
        TextView tv1;
        TextView tv2;
        TextView tv3;
        TextView tv4;
        ImageView iv1;
    }

    public void refresh(List<HomeBean.DataBeanX.DataBean.ListBean> list){
        this.mList = list;
        notifyDataSetChanged();
    }
}

