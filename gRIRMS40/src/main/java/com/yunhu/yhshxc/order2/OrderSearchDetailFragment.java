package com.yunhu.yhshxc.order2;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.order.bo.ProductConf;
import com.yunhu.yhshxc.order2.bo.Order2;
import com.yunhu.yhshxc.order2.bo.OrderDetail;
import com.yunhu.yhshxc.order2.view.OrderItemView;
import com.yunhu.yhshxc.utility.PublicUtils;

public class OrderSearchDetailFragment extends Fragment {

	private final String TAG = "OrderSearchDetailFragment";
	private ListView orderListView;
	private List<String> titleList;
	private List<OrderDetail> orderList;
	private Order2 order;
	
	private OrderSearchDetailActivity orderSearchDetailActivity;
	private OrderItemAdapter adapter;
    public OrderSearchDetailFragment() {
        super();
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	super.onCreateView(inflater, container, savedInstanceState);
    	orderSearchDetailActivity = (OrderSearchDetailActivity) this.getActivity();
    	order = orderSearchDetailActivity.order;
    	if (order!=null && order.getOrderDetailList()!=null) {
    		orderList = order.getOrderDetailList();
		}else{
	    	orderList = new ArrayList<OrderDetail>();
		}
    	View view = View.inflate(this.getActivity(), R.layout.order_search_detail_fragment, null);
    	orderListView = (ListView)view.findViewById(R.id.lv_order2_list);
    	adapter = new OrderItemAdapter();
    	orderListView.setAdapter(adapter);
    	initData();
        return view;
    }

    /**
     * 初始化数据
     */
   private void initData(){
	   initTitleData();
   }
    
   /**
    * 初始化标题数据
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
   	if (unit !=null) {//如果有单位的话显示单位
       	titleList.add(unit.getName());
	}
   	int is_phone_price = SharedPreferencesForOrder2Util.getInstance(this.getActivity()).getIsPhonePrice();
   	if (is_phone_price!=3) {//如果配置价格显示的话标题添加价格
       	titleList.add("价格");
	}
   	titleList.add("数量");
//   	titleList.add("备注");
//   	title = titleList.toArray(new String[titleList.size()]);
   }
    
   /**
    * 生成一条订单明细数据
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
   	if (orderDetail.getProductUnit() !=null) {//单位
       	list.add(String.valueOf(orderDetail.getProductUnit().getCtrlCol()));
	}
   	int is_phone_price = SharedPreferencesForOrder2Util.getInstance(this.getActivity()).getIsPhonePrice();
   	if (is_phone_price!=3) {//价格
   		list.add(PublicUtils.formatDouble(orderDetail.getPrice()));
	}
   	list.add(PublicUtils.formatDouble(orderDetail.getQuantity()));//数量
   	list.add(orderDetail.getRemark());//备注
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
			}
			itemView.setIsExamine(true);
			convertView = itemView.getView();
			convertView.setTag(String.valueOf(position-1));
			itemView.getView().setTag(String.valueOf(position-1));
			return convertView;
		}
    	
    	
    }
}
