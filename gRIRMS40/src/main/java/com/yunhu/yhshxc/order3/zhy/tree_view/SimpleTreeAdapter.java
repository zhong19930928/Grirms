package com.yunhu.yhshxc.order3.zhy.tree_view;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.yunhu.yhshxc.order3.view.Order3ProductItem;
import com.yunhu.yhshxc.order3.view.Order3ProductItem.OnShoppingCarListner;
import com.yunhu.yhshxc.order3.zhy.tree.bean.Node;
import com.yunhu.yhshxc.order3.zhy.tree.bean.TreeListViewAdapter;

public class SimpleTreeAdapter<T> extends TreeListViewAdapter<T>
{
//private List<T> datas;
	private OnShoppingCarListner listner;
	public SimpleTreeAdapter(ListView mTree, Context context, List<T> datas,
			int defaultExpandLevel,OnShoppingCarListner listner) throws IllegalArgumentException,
			IllegalAccessException
	{
		super(mTree, context, datas, defaultExpandLevel);
//		this.datas = datas;
		this.listner = listner;
	}

	@Override
	public View getConvertView(final Node node , int position, View convertView, ViewGroup parent)
	{
//		JLog.d("aaa", "=======   "+node.getId()+"  "+node.getpId()+"   "+node.getLevel()+"  "+node.getProduct().getLabel()+"   "+node.isLeaf());
		Order3ProductItem view = null;
		
		view = new Order3ProductItem(mContext, node,listner);
		
//		view.initData((Order3ProductCtrl)datas.get(position));
		view.initData(node.getProduct());
//		view.initData2(node);
		convertView = view.getView();
		return convertView;
		
//		Order3ProductCtrl productCtrl = node.getProduct();
//		Order3ProductItem item = null;
//		if (convertView == null) {
//			item = new Order3ProductItem(mContext, node);
//			convertView = item.getView();
//			convertView.setTag(item);
//		}else{
//			item = (Order3ProductItem) convertView.getTag();
//		}
//		
//		item.initData(productCtrl);
//		return convertView;
	}

	

}
