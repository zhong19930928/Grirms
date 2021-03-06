package com.yunhu.yhshxc.activity.carSales.zhy.tree_view;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.yunhu.yhshxc.activity.carSales.management.LoadingView;
import com.yunhu.yhshxc.activity.carSales.scene.view.ReturnGoodsView.OnList;
import com.yunhu.yhshxc.activity.carSales.zhy.tree.bean.Node;
import com.yunhu.yhshxc.activity.carSales.zhy.tree.bean.TreeListViewAdapter;

public class LoadingTreeAdapter<T> extends TreeListViewAdapter<T>
{
	private OnList onList;
	boolean isEnable;
	public LoadingTreeAdapter(ListView mTree, Context context, List<T> datas,
			int defaultExpandLevel,OnList onList,boolean isEnable) throws IllegalArgumentException,IllegalAccessException
	{
		super(mTree, context, datas, defaultExpandLevel);
		this.onList = onList;
		this.isEnable = isEnable;
	}

	@Override
	public View getConvertView(Node node , int position, View convertView, ViewGroup parent)
	{
		LoadingView view = null;
		view = new LoadingView(mContext, node,onList);
		view.initData(node.getProductCtrl(),isEnable);
		convertView = view.getView();
		return convertView;
	}

}
