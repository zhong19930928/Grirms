package com.yunhu.yhshxc.MeetingAgenda.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.BeanMeetingroom;

import java.util.List;

/**
 * ＠author zhonghuibin
 * create at 2017/12/13.
 * describe
 */
public class MeetingJumpAdapter extends BaseAdapter {
    private Context mContext;
    private List<BeanMeetingroom> mList;

    public MeetingJumpAdapter(Context context, List<BeanMeetingroom> list){
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
                    R.layout.item_demo, viewGroup, false);//item布局
            viewHoder.tv1 = view.findViewById(R.id.tv1);
            viewHoder.tv2 = view.findViewById(R.id.tv2);
            viewHoder.tv3 = view.findViewById(R.id.tv3);
            viewHoder.tv4 = view.findViewById(R.id.tv4);
            viewHoder.iv1 = view.findViewById(R.id.iv1);
            view.setTag(viewHoder);
        } else {
            viewHoder = (ViewHoder) view.getTag();
        }
        if (i>0){
            if (mList.get(i).getTv1().equals(mList.get(i-1).getTv1())){
                viewHoder.tv1.setVisibility(View.INVISIBLE);
            }else{
                viewHoder.tv1.setVisibility(View.VISIBLE);
            }
        }else {
            viewHoder.tv1.setVisibility(View.VISIBLE);
        }
        if (i==0){
            viewHoder.iv1.setBackgroundResource(R.drawable.item_line1);
        }else if ((i+1)==mList.size()){
            viewHoder.iv1.setBackgroundResource(R.drawable.item_line2);
        }else{
            viewHoder.iv1.setBackgroundResource(R.drawable.item_line);
        }
        viewHoder.tv1.setText(mList.get(i).getTv1());
        viewHoder.tv2.setText(mList.get(i).getTv2());
        viewHoder.tv3.setText(mList.get(i).getTv3());
        viewHoder.tv4.setText(mList.get(i).getTv4());
        return view;
    }

    class ViewHoder {
        TextView tv1;
        TextView tv2;
        TextView tv3;
        TextView tv4;
        ImageView iv1;
    }
}

