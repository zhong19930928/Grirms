package com.yunhu.yhshxc.activity.carSales.zhy.tree_view;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.yunhu.yhshxc.activity.carSales.scene.view.SellingGoodsView;
import com.yunhu.yhshxc.activity.carSales.scene.view.SellingGoodsView.OnShoppingCarListner;
import com.yunhu.yhshxc.activity.carSales.zhy.tree.bean.Node;
import com.yunhu.yhshxc.activity.carSales.zhy.tree.bean.TreeListViewAdapter;

public class SellingGoodsTreeAdapter<T> extends TreeListViewAdapter<T>
{
	private OnShoppingCarListner onShoppingCarListner;
	public SellingGoodsTreeAdapter(ListView mTree, Context context, List<T> datas,
			int defaultExpandLevel,OnShoppingCarListner onShoppingCarListner) throws IllegalArgumentException,IllegalAccessException
	{
		super(mTree, context, datas, defaultExpandLevel);
		this.onShoppingCarListner = onShoppingCarListner;
	}

	@Override
	public View getConvertView(Node node , int position, View convertView, ViewGroup parent)
	{
		SellingGoodsView view = null;
		view = new SellingGoodsView(mContext, node,onShoppingCarListner);
		view.initData(node.getProductCtrl());
		convertView = view.getView();
		return convertView;
	}

}
