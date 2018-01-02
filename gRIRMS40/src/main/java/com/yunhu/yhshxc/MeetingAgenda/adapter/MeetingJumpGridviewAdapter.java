package com.yunhu.yhshxc.MeetingAgenda.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunhu.yhshxc.MeetingAgenda.bo.Meetingarticle;
import com.yunhu.yhshxc.R;

import java.util.List;

/**
 * @author zhonghuibin
 * create at 2017/12/13.
 * describe 二级页面适配器
 */
public class MeetingJumpGridviewAdapter extends BaseAdapter {
    private Context mContext;
    private List<Meetingarticle> mList;

    public MeetingJumpGridviewAdapter(Context context, List<Meetingarticle> list){
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
                    R.layout.items_jumpgridview, viewGroup, false);//item布局
            viewHoder.tv1 = view.findViewById(R.id.tv1);
            viewHoder.iv1 = view.findViewById(R.id.iv1);
            view.setTag(viewHoder);
        } else {
            viewHoder = (ViewHoder) view.getTag();
        }
        viewHoder.tv1.setText(mList.get(i).getName());
        return view;
    }

    class ViewHoder {
        TextView tv1;
        ImageView iv1;
    }
}

