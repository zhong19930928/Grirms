package com.yunhu.yhshxc.activity.carSales.zhy.tree_view;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.yunhu.yhshxc.activity.carSales.management.StockTakingView;
import com.yunhu.yhshxc.activity.carSales.scene.view.ReturnGoodsView.OnList;
import com.yunhu.yhshxc.activity.carSales.zhy.tree.bean.Node;
import com.yunhu.yhshxc.activity.carSales.zhy.tree.bean.TreeListViewAdapter;

public class StockTakingTreeAdapter<T> extends TreeListViewAdapter<T>
{
	private OnList onList;
	public StockTakingTreeAdapter(ListView mTree, Context context, List<T> datas,
			int defaultExpandLevel,OnList onList) throws IllegalArgumentException,IllegalAccessException
	{
		super(mTree, context, datas, defaultExpandLevel);
		this.onList = onList;
	}

	@Override
	public View getConvertView(Node node , int position, View convertView, ViewGroup parent)
	{
		StockTakingView view = null;
		view = new StockTakingView(mContext, node,onList);
		view.initData(node.getProductCtrl());
		convertView = view.getView();
		return convertView;
	}

}
