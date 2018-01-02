package com.yunhu.yhshxc.order.view;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.database.PSSConfDB;
import com.yunhu.yhshxc.order.CreateOrderActivity;
import com.yunhu.yhshxc.order.OrderPurchaseActivity;
import com.yunhu.yhshxc.order.OrderQueryActivity;
import com.yunhu.yhshxc.order.OrderReturnedListActivity;
import com.yunhu.yhshxc.order.OrderSalePreviewActivity;
import com.yunhu.yhshxc.order.OrderStockList;
import com.yunhu.yhshxc.order.bo.OrderMenu;
import com.yunhu.yhshxc.order.bo.PSSConf;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.DateUtil;

public class OrderMenuItem {
	private View currentView;//当前view
	private LinearLayout ll_first;//布局1
	private LinearLayout ll_second;//布局2
	private TextView tv_name_first;//名称1
	private TextView tv_name_second;//名称2
	private TextView tv_date_first;//日期1
	private TextView tv_date_second;//日期2
	private TextView tv_number_first;//标注1
	private TextView tv_number_second;//标注2
	private LinearLayout ll_order_menu_number_first;//
	private LinearLayout ll_order_menu_number_second;//
	private ImageView iv_icon_first;//菜单图标1
	private ImageView iv_icon_second;//菜单图标2
	private Context context;
	private final int INDEX_1=0;
	private final int INDEX_2=1;
	
	private final int TYPE_WORKDAY=1;//工作日
	private final int TYPE_EVERYDAY=2;//每日
	private final int TYPE_WEEKLY=3;//每周
	private int intervalType;//操作类型 有“工作日” “每日” “每周”
	private String weekly;//每周类型的店面的都是哪些天可以访问 以0和1表示 0表示当天不能访问1表示可以访问 从周一开始
	
	private String storeId;String storeName;//店面Id 店面名称

	public OrderMenuItem(Context mContext,OrderMenu firstMenu,OrderMenu secondMenu) {
		this.context=mContext;
		currentView=View.inflate(mContext, R.layout.order_menu_item, null);
		ll_first=(LinearLayout)currentView.findViewById(R.id.ll_order_menu_first);
		ll_second=(LinearLayout)currentView.findViewById(R.id.ll_order_menu_second);
		ll_order_menu_number_first=(LinearLayout)currentView.findViewById(R.id.ll_order_menu_number_first);
		ll_order_menu_number_second=(LinearLayout)currentView.findViewById(R.id.ll_order_menu_number_second);
		tv_name_first=(TextView)currentView.findViewById(R.id.tv_order_menu_name_first);
		tv_name_second=(TextView)currentView.findViewById(R.id.tv_order_menu_name_second);
		tv_date_first=(TextView)currentView.findViewById(R.id.tv_order_menu_date_first);
		tv_date_second=(TextView)currentView.findViewById(R.id.tv_order_menu_date_second);
		tv_number_first=(TextView)currentView.findViewById(R.id.tv_order_number_first);
		tv_number_second=(TextView)currentView.findViewById(R.id.tv_order_number_second);
		iv_icon_first=(ImageView)currentView.findViewById(R.id.iv_order_menu_icon_first);
		iv_icon_second=(ImageView)currentView.findViewById(R.id.iv_order_menu_icon_second);
		if(secondMenu==null){//只有一个布局的时候
			ll_second.setVisibility(View.INVISIBLE);
		}else{//有连个布局页面
			setIconByMenuType(secondMenu.getType(), iv_icon_second);
			ll_second.setTag(secondMenu);
			ll_second.setOnClickListener(onClickListener);
			tv_name_second.setText(secondMenu.getOrderMenuName());//设置名称
			tv_number_second.setText(secondMenu.getNumber()+"");
			controlMenu(secondMenu.getType(),INDEX_2,secondMenu);
		}
		tv_name_first.setText(firstMenu.getOrderMenuName());
		tv_number_first.setText(firstMenu.getNumber()+"");
		ll_first.setTag(firstMenu);
		ll_first.setOnClickListener(onClickListener);
		setIconByMenuType(firstMenu.getType(), iv_icon_first);
		controlMenu(firstMenu.getType(),INDEX_1,firstMenu);
	}
	
	/**
	 * 根据菜单类型设置图标
	 * @param type 类型
	 * @param iv 图标
	 */
	private void setIconByMenuType(int type,ImageView iv){
		switch (type) {
		case 1://创建订单
			iv.setBackgroundResource(R.drawable.order_create);
			break;
		case 2://查询订单
			iv.setBackgroundResource(R.drawable.order_search);
			break;
		case 3://进货上报
			iv.setBackgroundResource(R.drawable.order_stock_report);
			break;
		case 4://销量上报
			iv.setBackgroundResource(R.drawable.order_sales_report);
			break;
		case 5://退货管理
			iv.setBackgroundResource(R.drawable.order_return_management);
			break;
		case 6://库存管理
			iv.setBackgroundResource(R.drawable.order_stock_control);
			break;
		default:
			iv.setBackgroundResource(R.drawable.order_create);
			break;
		}
	}
	
	/**
	 * 设置第一个menu是否可用
	 * @param flag 标识 true 表示可用 false 表示不可用
	 */
	private void setFirstMenuEnable(boolean flag){
		if(flag){
			ll_first.setBackgroundResource(R.drawable.order_menu_click);
		}else{
			ll_first.setBackgroundResource(R.color.order_menu_gary);
		}
		ll_first.setEnabled(flag);
		//设置背景透明度
		ll_first.getBackground().setAlpha(240);
	}
	
	/**
	 * 设置第二个menu是否可用
	 * @param flag 标识 true 表示可用 false 表示不可用
	 */
	private void setSecondMenuEnable(boolean flag){
		if(flag){
			ll_second.setBackgroundResource(R.drawable.order_menu_click);
		}else{
			ll_second.setBackgroundResource(R.color.order_menu_gary);
		}
		ll_second.setEnabled(flag);
		ll_second.getBackground().setAlpha(240);
	}
	
	/**
	 * 单击事件 “创建订单” “查询订单” “进货上报” “销量上报” “退货管理” “库存管理”
	 */
	private OnClickListener onClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			OrderMenu orderMenu=(OrderMenu) v.getTag();
			if(orderMenu!=null){
				switch (orderMenu.getType()) {
				case 1://创建订单
					createOrder();
					break;
				case 2://查询订单
					orderSearch();
					break;
				case 3://进货上报
					orderStockReport();
					break;
				case 4://销量上报
					orderSalesReport();
					break;
				case 5://退货管理
					orderReturnManagement();
					break;
				case 6://库存管理
					stockControl();
					break;

				default:
					break;
				}
			}
		}
	};
	
	/**
	 * 创建订单
	 */
	private void createOrder(){
		Intent intent =new Intent(context, CreateOrderActivity.class);
		intent.putExtra("storeId", storeId);//store id
		intent.putExtra("storeName", storeName);//store name
		context.startActivity(intent);
	}
	/**
	 * 查询订单
	 */
	private void orderSearch(){
		Intent intent =new Intent(context, OrderQueryActivity.class);
		intent.putExtra(Constants.ORDER_BOUNDLE_STORE_ID_KEY, storeId);//store id
		intent.putExtra(Constants.ORDER_BOUNDLE_TITLE_KEY, storeName);//store name
		context.startActivity(intent);
	}
	/**
	 * 进货上报
	 */
	private void orderStockReport(){
		Intent intent =new Intent(context, OrderPurchaseActivity.class);
		intent.putExtra(Constants.ORDER_BOUNDLE_STORE_ID_KEY, storeId);
		intent.putExtra(Constants.ORDER_BOUNDLE_TITLE_KEY, storeName);
		context.startActivity(intent);
	}
	/**
	 * 销量上报
	 */
	private void orderSalesReport(){
		Intent intent =new Intent(context, OrderSalePreviewActivity.class);
		intent.putExtra(Constants.ORDER_BOUNDLE_STORE_ID_KEY, storeId);
		intent.putExtra(Constants.ORDER_BOUNDLE_TITLE_KEY, storeName);
		context.startActivity(intent);
	}
	/**
	 * 退货管理
	 */
	private void orderReturnManagement(){
		Intent intent =new Intent(context, OrderReturnedListActivity.class);
		intent.putExtra(Constants.ORDER_BOUNDLE_STORE_ID_KEY, storeId);
		intent.putExtra(Constants.ORDER_BOUNDLE_TITLE_KEY, storeName);
		context.startActivity(intent);
	}
	/**
	 * 库存管理
	 */
	private void stockControl(){
		Intent intent=new Intent(context,OrderStockList.class);
		intent.putExtra("storeId", storeId);//store id
		intent.putExtra("storeName", storeName);//store name
		context.startActivity(intent);
	}

	/**
	 * 返回当前view
	 * @return
	 */
	public View getView(){
		return currentView;
	}
	
	/**
	 * 控制menu超时不能操作  创建订单 销量上报 退货管理
	 * @param type 菜单类型 index菜单下标
	 */
	private void controlMenu(int type,int index,OrderMenu orderMenu){
		PSSConf pssConf=new PSSConfDB(context).findPSSConf();
		boolean flag = true;//是否可用
		if(pssConf!=null){
			switch (type) {
			case 1://创建订单
				flag=checkIsEnable(1,pssConf,orderMenu);
				break;
			case 4://销量上报
				flag=checkIsEnable(4,pssConf,orderMenu);
				break;
			case 5://退货管理
				flag=checkIsEnable(5,pssConf,orderMenu);
				break;

			default:
				break;
			}
			if(index==INDEX_1){
				setFirstMenuEnable(flag);
				tv_date_first.setText(orderMenu.getEffectiveDate());//设置有效期
			}else{
				setSecondMenuEnable(flag);
				tv_date_second.setText(orderMenu.getEffectiveDate());//设置有效期
			}
		}
	}
	
	/**
	 * 检测是否可用
	 * @param menuType 菜单类型 1创建订单 4销量上报 5退货管理
	 * @return true 表示可用 false 表示不可用
	 */
	private boolean checkIsEnable(int menuType,PSSConf pssConf,OrderMenu orderMenu){
		boolean flag=true;
		switch (menuType) {
		case 1:// 创建订单
			String createOrderTimeConf=pssConf.getCreateOrderTimeConf();
			String createOrderStartTime=pssConf.getCreateOrderStartTime();
			String createOrderEndTime=pssConf.getCreateOrderEndTime();
			if(!TextUtils.isEmpty(createOrderStartTime)&&!TextUtils.isEmpty(createOrderEndTime)&&!TextUtils.isEmpty(createOrderTimeConf)){
				intervalType=Integer.parseInt(createOrderTimeConf);
				weekly=pssConf.getCreateOrderTimeWeekly();
				flag=checkForType(flag,createOrderStartTime,createOrderEndTime,orderMenu);
			}
			
			break;
		case 4:// 销量上报
			String salesTimeConf=pssConf.getSalesTimeConf();
			String salesStartTime=pssConf.getSalesStartTime();
			String salesEndTime=pssConf.getSalesEndTime();
			if(!TextUtils.isEmpty(salesStartTime)&&!TextUtils.isEmpty(salesEndTime)&&!TextUtils.isEmpty(salesTimeConf)){
				intervalType=Integer.parseInt(salesTimeConf);
				weekly=pssConf.getCreateOrderTimeWeekly();
				flag=checkForType(flag,salesStartTime,salesEndTime,orderMenu);
			}

			break;
		case 5:// 退货管理
			String returnedTimeConf=pssConf.getReturnedTimeConf();
			String returnedStartTime=pssConf.getReturnedStartTime();
			String returnedEndTime=pssConf.getReturnedEndTime();
			if(!TextUtils.isEmpty(returnedStartTime)&&!TextUtils.isEmpty(returnedEndTime)&&!TextUtils.isEmpty(returnedTimeConf)){
				intervalType=Integer.parseInt(returnedTimeConf);
				weekly=pssConf.getReturnedTimeWeekly();
				flag=checkForType(flag,returnedStartTime,returnedEndTime,orderMenu);
			}
			break;
		}
		return flag;
	}
	
	/**
	 * 根据类型判断是否可用
	 * @param flag true可用 false 不可用
	 */
	private boolean checkForType(boolean flag,String start,String end,OrderMenu orderMenu){
		Date date=new Date();
		String hour=new StringBuffer(String.valueOf(DateUtil.getHour(date)+100)).substring(1).toString();
		String min=new StringBuffer(String.valueOf(DateUtil.getMin(date)+100)).substring(1).toString();
		String currentTime=hour+":"+min;
		switch (intervalType) {
		case TYPE_WORKDAY://工作日
			if(DateUtil.todayIsWorkDay()){//判断今天是否是工作日
				if((currentTime.compareTo(start)>0 && currentTime.compareTo(end)<0)){
					flag=true;
				}else{
					flag=false;
				}
			}else{
				flag=false;
			}
			orderMenu.setEffectiveDate("仅工作日 "+start+"-"+end);//设置可操作时间
			break;
		case TYPE_EVERYDAY://每日
			if((currentTime.compareTo(start)>0 && currentTime.compareTo(end)<0)){
				flag=true;
			}else{
				flag=false;
			}
			orderMenu.setEffectiveDate("仅每日 "+start+"-"+end);
			break;
		case TYPE_WEEKLY://每周
			if (TextUtils.isEmpty(weekly)) {//没配置周几显示
				flag=true;
			}else{
				Calendar c = Calendar.getInstance();
				int day = c.get(Calendar.DAY_OF_WEEK) - 1;// 获取今天是本周的第几天 从星期一开始
				day = day == 0 ? 7 : day;
				int n = Integer.valueOf(weekly, 2) >> (7 - day);
				if (n % 2 == 1) { // 判断今天执行不  1 的话就表示今天执行，0 表示今天不执行
					if((currentTime.compareTo(start)>0 && currentTime.compareTo(end)<0)){
						flag=true;
					}else{
						flag=false;
					}
				}else {
					flag = false;
				}
				String str=getShowWeekDay(weekly);
				if(str.length()>0){
					orderMenu.setEffectiveDate("仅每"+str+" "+start+"-"+end);
				}
			}
			break;
		}
		return flag;
	}
	
	/**
	 * 返回都是每周几执行
	 * @param weekly
	 * @return
	 */
	private String getShowWeekDay(String weekly){
		StringBuffer weekDay=new StringBuffer();
		if(weekly.charAt(0)=='1'){
			weekDay.append("周一");
		}
		if(weekly.charAt(1)=='1'){
			weekDay.append("周二");
		}
		if(weekly.charAt(2)=='1'){
			weekDay.append("周三");
		}
		if(weekly.charAt(3)=='1'){
			weekDay.append("周四");
		}
		if(weekly.charAt(4)=='1'){
			weekDay.append("周五");
		}
		if(weekly.charAt(5)=='1'){
			weekDay.append("周六");
		}
		if(weekly.charAt(6)=='1'){
			weekDay.append("周日");
		}
		return weekDay.toString();
	}
	
	/**
	 * 设置店面ID
	 */
	public void setStoreInfo(String storeId,String storeName){
		this.storeId=storeId;
		this.storeName=storeName;
		
	}
}
