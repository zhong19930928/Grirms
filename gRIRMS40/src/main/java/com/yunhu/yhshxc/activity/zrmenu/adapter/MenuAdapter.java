package com.yunhu.yhshxc.activity.zrmenu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yunhu.yhshxc.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author suhu
 * @data 2017/10/27.
 * @description
 */

public class MenuAdapter extends BaseAdapter {
    private List<String> list;
    private Context context;
    private LayoutInflater inflater;
    private ViewHolder viewHolder;

    public MenuAdapter(Context context ,List<String> list) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return list != null ? list.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_zrmodule_menu, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.zrmoduleMenuName.setText(list.get(position));
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.zrmodule_menu_name)
        TextView zrmoduleMenuName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
