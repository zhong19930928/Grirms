package com.yunhu.yhshxc.activity.carSales.management;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesProduct;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesProductCtrl;
import com.yunhu.yhshxc.activity.carSales.scene.view.ReturnGoodsView.OnList;
import com.yunhu.yhshxc.activity.carSales.util.CarSalesUtil;
import com.yunhu.yhshxc.activity.carSales.zhy.tree.bean.Node;
import com.yunhu.yhshxc.utility.PublicUtils;


public class ReplenishmentView {
	private View view;
	private Context context;
	private Node node;
	private TextView tv_return_goods_name;
	private Button add_to_shopping_car;
	private ImageView id_treenode_icon;
	private TextView id_treenode_label;
	private LinearLayout return_goods_item;
	private LinearLayout ll_return_goods;
	private EditText et_return_goods_num;
	private OnList onList;
	private CarSalesUtil util;
	public ReplenishmentView(Context context,Node node,OnList onList) {
		this.context = context;
		this.node = node;
		this.onList = onList;
		util= new CarSalesUtil(context);
		view = View.inflate(context, R.layout.car_sales_return_goods_item, null);
		tv_return_goods_name = (TextView) view.findViewById(R.id.tv_return_goods_name);
		add_to_shopping_car = (Button) view.findViewById(R.id.add_to_shopping_car);
		id_treenode_icon = (ImageView) view.findViewById(R.id.id_treenode_icon);
		id_treenode_label = (TextView) view.findViewById(R.id.id_treenode_label);
		return_goods_item = (LinearLayout) view.findViewById(R.id.return_goods_item);
		ll_return_goods = (LinearLayout) view.findViewById(R.id.ll_return_goods);
		et_return_goods_num = (EditText) view.findViewById(R.id.et_return_goods_num);
		et_return_goods_num.setHint("补货数量");
	}

	public void initData(CarSalesProductCtrl data) {
		if(node.getIcon() == -1){
			id_treenode_icon.setVisibility(View.GONE);
		}else{
			id_treenode_icon.setVisibility(View.VISIBLE);
			id_treenode_icon.setImageResource(node.getIcon());
		}
		if(node.isLeaf()){  //添加具体的产品信息
			CarSalesProduct product = util.product(data.getProductId(), data.getUnitId());
			if(product !=null){
				ll_return_goods.setVisibility(View.GONE);
				return_goods_item.setVisibility(View.VISIBLE);
				tv_return_goods_name.setText(product.getName());
			}else{
				return_goods_item.setVisibility(View.GONE);
				ll_return_goods.setVisibility(View.VISIBLE);
	     		id_treenode_label.setText(data.getLabel());
			}
		}else{  //添加产品类别
			return_goods_item.setVisibility(View.GONE);
			ll_return_goods.setVisibility(View.VISIBLE);
     		id_treenode_label.setText(data.getLabel());
		}
		if(!TextUtils.isEmpty(data.getReplenishmentCount())&&!data.getReplenishmentCount().equals("0")&&!data.getReplenishmentCount().equals("0.0")){
			et_return_goods_num.setText(PublicUtils.formatDouble(Double.parseDouble(data.getReplenishmentCount())));
		}
		et_return_goods_num.addTextChangedListener(watcher);
	}

	public View getView() {
		return view;
	}
	TextWatcher watcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			onList.onListNum(s, node,et_return_goods_num);
		}
	};
}
