package com.yunhu.yhshxc.activity.zrmenu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.zrmenu.module.AssetBean;

import java.util.List;

/**
 * @author suhu
 * @data 2017/10/23.
 * @description
 */

public class AssetAdapter extends BaseAdapter{

    private Context context;
    private List<AssetBean.DataBean> list;
    private LayoutInflater layoutInflater;
    private ViewHolder viewHolder;
    private OnItemClick onItemClick;

    public AssetAdapter(Context context, List<AssetBean.DataBean> list) {
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list!=null?list.size():0;
    }

    @Override
    public Object getItem(int position) {
        return list!=null?list.get(position):null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView = layoutInflater.inflate(R.layout.item_asset,null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.item_title);
            viewHolder.time = (TextView) convertView.findViewById(R.id.item_title);
            viewHolder.name = (TextView) convertView.findViewById(R.id.item_name);
            viewHolder.sum = (TextView) convertView.findViewById(R.id.item_sum);
            viewHolder.have = (TextView) convertView.findViewById(R.id.item_have);
            viewHolder.start = (TextView) convertView.findViewById(R.id.item_start);
            viewHolder.upload = (TextView) convertView.findViewById(R.id.item_upload);
            viewHolder.delete = (TextView) convertView.findViewById(R.id.item_delete);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        AssetBean.DataBean dataBean = list.get(position);
        viewHolder.title.setText(dataBean.getName());
        viewHolder.time.setText(dataBean.getInserttime());
        viewHolder.name.setText(dataBean.getAdminname());
        viewHolder.sum.setText(dataBean.getZichannum());
        viewHolder.have.setText(dataBean.getYpnum());

        viewHolder.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClick!=null){
                    onItemClick.click(position,v);
                }
            }
        });
        viewHolder.upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClick!=null){
                    onItemClick.click(position,v);
                }
            }
        });
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClick!=null){
                    onItemClick.click(position,v);
                }
            }
        });

        return convertView;
    }

    public void setList(List<AssetBean.DataBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }


    private static class ViewHolder{
        public TextView title,time,name,sum,have,start,upload,delete;
    }

    public interface OnItemClick{

        /**
         * @param position
         * @param view
         *item上的控件的点击事件
         *
        */
        void click(int position,View view);
    }




}
