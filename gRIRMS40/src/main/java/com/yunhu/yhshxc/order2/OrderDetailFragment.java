package com.yunhu.yhshxc.order2;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.order.bo.ProductConf;
import com.yunhu.yhshxc.order2.bo.Order2;
import com.yunhu.yhshxc.order2.bo.OrderDetail;
import com.yunhu.yhshxc.order2.bo.OrderSpec;
import com.yunhu.yhshxc.order2.view.OrderItemView;
import com.yunhu.yhshxc.utility.PublicUtils;

public class OrderDetailFragment extends Fragment {

	private LinearLayout ll_order2_add;
	private ListView orderListView;
	private List<String> titleList;
	private List<OrderDetail> orderList;
	private OrderItemAdapter adapter;
	private Order2 order;
	private TextView tv_order_number;
	private OrderCreateActivity orderCreateActivity;

    public OrderDetailFragment() {
        super();
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	super.onCreateView(inflater, container, savedInstanceState);
    	View view = View.inflate(this.getActivity(), R.layout.order_detail_fragment, null);
    	tv_order_number = (TextView)view.findViewById(R.id.tv_order_number);
    	ll_order2_add = (LinearLayout)view.findViewById(R.id.ll_order2_add);
    	ll_order2_add.setOnClickListener(listener);
    	orderListView = (ListView)view.findViewById(R.id.lv_order2_list);
    	try {
        	initData();
		} catch (Exception e) {
			e.printStackTrace();
		}
        return view;
    }

   /**
    * 初始化订单详细
    * @throws JSONException
    */
    public void initData() throws JSONException{
    	orderCreateActivity = (OrderCreateActivity)this.getActivity();
		tv_order_number.setText("订单编号\n"+orderCreateActivity.orderNumber);
		if (orderCreateActivity.newOrder!=null && orderCreateActivity.newOrder.getOrder()!=null) {
			order = orderCreateActivity.newOrder.getOrder();
		}
		if (order==null || order.getOrderDetailList()==null) {
			orderList = new ArrayList<OrderDetail>();
		}else{
			orderList = order.getOrderDetailList();
		}
    	initTitleData();
    	adapter = new OrderItemAdapter();
    	orderListView.setAdapter(adapter);
    }
    
    
    /**
     * 设置订单明细标题
     */
    private void initTitleData(){
    	titleList = new ArrayList<String>();
    	List<ProductConf> productCtrl = SharedPreferencesForOrder2Util.getInstance(getActivity()).getProductCtrl();
    	if (productCtrl!=null && !productCtrl.isEmpty()) {
        	for (int i = 0; i < productCtrl.size(); i++) {
        		ProductConf conf = productCtrl.get(i);
        		if (conf!=null) {
    				titleList.add(conf.getName());
    			}
    		}
		}
    	ProductConf unit = SharedPreferencesForOrder2Util.getInstance(getActivity()).getUnitCtrl();
    	if (unit !=null) {
        	titleList.add(unit.getName());
		}
    	int is_phone_price = SharedPreferencesForOrder2Util.getInstance(this.getActivity()).getIsPhonePrice();
    	if (is_phone_price!=3) {
        	titleList.add("价格");
		}
    	titleList.add("数量");
//    	titleList.add("备注");
//    	title = titleList.toArray(new String[titleList.size()]);
    }
    
    /**
     * 初始化一条订单明细
     * @param orderDetail
     * @return
     */
    private List<String> orderData(OrderDetail orderDetail){
    	List<String> list = new ArrayList<String>();
    	Dictionary product = orderDetail.getProduct();
    	List<Dictionary> productList = OrderDetail.getProductRelatedList(product!=null?product.getDid() : "", getActivity());
    	for (int i = 0; i < productList.size(); i++) {
			Dictionary dic = productList.get(i);
			if (dic!=null) {
				list.add(dic.getCtrlCol());
			}else{
				list.add("");
			}
		}
    	if (orderDetail.getProductUnit() !=null) {
        	list.add(String.valueOf(orderDetail.getProductUnit().getCtrlCol()));
		}
    	int is_phone_price = SharedPreferencesForOrder2Util.getInstance(this.getActivity()).getIsPhonePrice();
    	if (is_phone_price!=3) {
    		list.add(PublicUtils.formatDouble(orderDetail.getPrice()));
		}
    	list.add(PublicUtils.formatDouble(orderDetail.getQuantity()));
    	list.add(orderDetail.getRemark());
    	return list;
    }
    
    private class OrderItemAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return orderList.size()+1;
		}

		@Override
		public Object getItem(int position) {
			return orderList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			OrderItemView itemView = null;
			if (position == 0) {
				itemView = new OrderItemView(getActivity(), true);
//				String[] title = new String[]{"产品大类","产品名称","产品规格","数量"};
				itemView.initTitleData(titleList);
			}else{
				itemView = new OrderItemView(getActivity(), false);
//				String[] data = new String[]{"产品大类"+position,"产品名称"+position,"产品规格"+position,"数量"+position,"备注:"+position};
				OrderDetail orderDetail = orderList.get(position-1);
				List<String> detail = orderData(orderDetail);
				itemView.initContentData(detail);
				itemView.setOrderDetailFragment(OrderDetailFragment.this);
				itemView.getMain().setOnCreateContextMenuListener(onCreateContextMenuListener);
				itemView.getMain().setTag(String.valueOf(position-1));
			}
			convertView = itemView.getView();
			convertView.setTag(String.valueOf(position-1));
			itemView.getView().setTag(String.valueOf(position-1));
			
			return convertView;
		}
    	
    	
    }
    
    /**
     * 给订单明细添加长按“删除”和“修改”事件
     */
	private int currentIndex;
	private OnCreateContextMenuListener onCreateContextMenuListener = new OnCreateContextMenuListener() {
		
		@Override
		public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
			menu.add(0, 0, 0, "删除");
			menu.add(0, 1, 0, "修改");
			currentIndex = Integer.parseInt((String) v.getTag());
		}
	};
	
	/**
	 * 修改订单明细
	 * @param index 要修改的下标
	 */
	public void update(int index){
		try {
			Intent intent = new Intent(this.getActivity(), OrderCreateEditActivity.class);
			intent.putExtra("updateIndex", String.valueOf(index));
			intent.putExtra("updateDetailJson", Order2Data.orderDetail2Json(orderCreateActivity.orderDetailList.get(index)));
			this.getActivity().startActivityForResult(intent, 100);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除订单明细
	 * @param index 要删除的明细的下标
	 * @throws JSONException
	 */
	public void delete(int index) throws JSONException{
		OrderSpec  spec = order.getOrderSpec();
		Double amount = Double.parseDouble(order.getAmount());
		amount = amount - orderList.get(index).getAmount();
		order.setAmount(String.valueOf(amount));
		double unPay = Double.parseDouble(TextUtils.isEmpty(order.getAmount())?"0":order.getAmount()) - spec.getPay() - spec.getDiscount();
		spec.setUnPay(unPay);
		order.setOrderSpec(spec);
		orderList.remove(index);
		adapter.notifyDataSetChanged();
		order.setOrderDetailList(orderList);
		orderCreateActivity.newOrder.setOrder(order);
		orderCreateActivity.newOrderDB.updateNewOrder(orderCreateActivity.newOrder);
		orderCreateActivity.initFragmen();
	}
	
	public boolean onContextItemSelected(android.view.MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			try {
				delete(currentIndex);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case 1:
			update(currentIndex);
		default:
			break;
		}
		
		return super.onContextItemSelected(item);
		
	};
    
    private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ll_order2_add:
				add();
				break;

			default:
				break;
			}			
		}
	};
	
	/**
	 * 添加订单明细
	 */
	private void add(){
		if (order==null || TextUtils.isEmpty(order.getStoreId())) {
			Toast.makeText(getActivity(), "请选择客户", Toast.LENGTH_SHORT).show();
			return;
		}
		Intent intent = new Intent(getActivity(), OrderCreateEditActivity.class);
		this.getActivity().startActivityForResult(intent, 100);
	}
}
