package com.yunhu.yhshxc.inspection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Menu;

import java.util.List;

/**
 *@author suhu
 *@time 2017/6/30 15:41
 *
*/

public class ListViewAdapter extends BaseAdapter{
    private List<Menu> list;
    private Context context;
    private LayoutInflater inflater;
    private ViewHolder viewHolder;

    public ListViewAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setList(List<Menu> list) {
        this.list = list;
        notifyDataSetChanged();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView = inflater.inflate(R.layout.item_listview_inspection,null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.item_title);
            viewHolder.begin = (TextView) convertView.findViewById(R.id.item_begin);
            viewHolder.message_1 = (TextView) convertView.findViewById(R.id.item_message_1);
            viewHolder.message_2 = (TextView) convertView.findViewById(R.id.item_message_2);
            viewHolder.message_3 = (TextView) convertView.findViewById(R.id.item_message_3);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(list.get(position).getName());
        return convertView;
    }

    class ViewHolder{
        private TextView title,message_1,message_2,message_3,begin;
    }


}
