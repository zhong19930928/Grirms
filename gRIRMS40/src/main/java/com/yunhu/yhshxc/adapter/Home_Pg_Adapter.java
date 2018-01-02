package com.yunhu.yhshxc.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * ＠author zhonghuibin
 * create at 2017/12/12.
 * describe
 */
public class Home_Pg_Adapter extends PagerAdapter {
    ArrayList<View> list;
    ArrayList<String> urlList;

    public Home_Pg_Adapter(ArrayList<View> list, ArrayList<String> urlList) {
        this.list = list;
        this.urlList = urlList;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView(list.get(position));
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(list.get(position));
        return list.get(position);
    }
    public void setdata(ArrayList<String> urlList) {
        this.urlList = urlList;
    }
}
