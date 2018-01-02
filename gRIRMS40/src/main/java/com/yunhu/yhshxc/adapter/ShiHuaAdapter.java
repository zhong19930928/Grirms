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
import com.yunhu.yhshxc.bo.ShiHuaMenu;

import java.util.List;

/**
 * Created by yh
 * on 2017/6/29.
 */
public class ShiHuaAdapter extends BaseAdapter {
    private Context mContent;
    private List<ShiHuaMenu> mMenuList;

    public ShiHuaAdapter(Context context, List<ShiHuaMenu> menuList){
        mContent = context;
        mMenuList = menuList;
    }
    @Override
    public int getCount() {
        return mMenuList.size();
    }

    @Override
    public Object getItem(int i) {
        return mMenuList.get(i);
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
            view = LayoutInflater.from(mContent).inflate(
                    R.layout.home_menu_item_topgridview, viewGroup, false);//item布局
            viewHoder.tv1 = (TextView) view.findViewById(R.id.tv_cnName);
            viewHoder.iv1 = (ImageView) view.findViewById(R.id.iv_icon);
            view.setTag(viewHoder);
        } else {
            viewHoder = (ViewHoder) view.getTag();
        }
        viewHoder.tv1.setText(mMenuList.get(i).getFolderName());
        Glide.with(mContent).load(mMenuList.get(i).getIcon()).into(viewHoder.iv1);
        return view;
    }

    class ViewHoder {
        TextView tv1;
        ImageView iv1;
    }
}
