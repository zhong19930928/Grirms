package com.yunhu.yhshxc.order;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.database.PSSConfDB;
import com.yunhu.yhshxc.order.bo.OrderMenu;
import com.yunhu.yhshxc.order.bo.PSSConf;
import com.yunhu.yhshxc.order.view.OrderMenuItem;
import com.yunhu.yhshxc.print.BluetoothPrinter;
import com.yunhu.yhshxc.print.PrintHelper;
import com.yunhu.yhshxc.print.templet.Element;
import com.yunhu.yhshxc.print.templet.Templet.DataSource;
import com.yunhu.yhshxc.widget.ToastOrder;

/**
 * 订单菜单和提醒 包含“创建订单” “查询订单” “进货上报” “销量上报” “退货管理” “库存管理”六项模块
 * 订单状态的通知提醒
 * @author jishen
 *	
 */
public class MenuOrderActivity extends Activity {
	protected static PrintHelper printHelper;
	
	private LinearLayout ll_order_menu;//所有页面布局
	private List<OrderMenu> orderMenuList;//进销存模块
	private String storeId,storeName;//店面ID 店面名称
	private TextView tv_order_menu_store_name;//店面名称
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_menu);
		init();
		initLayout();
	}
	/**
	 * 初始化实例
	 */
	private void init(){
		storeId=getIntent().getStringExtra("storeId");
		storeName=getIntent().getStringExtra("storeName");
		ll_order_menu=(LinearLayout)findViewById(R.id.ll_order_menu);
		tv_order_menu_store_name=(TextView)findViewById(R.id.tv_order_menu_store_name);
		tv_order_menu_store_name.setText(storeName);
		
		PSSConfDB pssConfDB=new PSSConfDB(this);
		PSSConf pssConf=pssConfDB.findPSSConf();
		
		if(pssConf==null){//没有订单配置信息
			ToastOrder.makeText(this, "配置有误,请联系管理员!", ToastOrder.LENGTH_SHORT).show();
			this.finish();
		}else{
			String phoneFun=pssConf.getPhoneFun();
			if (TextUtils.isEmpty(phoneFun)) {//没有订单模块配置信息
				ToastOrder.makeText(this, "配置有误,请联系管理员!", ToastOrder.LENGTH_SHORT).show();
				this.finish();
			}else{
				orderMenuList=new ArrayList<OrderMenu>();
				String[] menuStr=phoneFun.split(",");
				for (int i = 0; i < menuStr.length; i++) {
					OrderMenu orderMenu=new OrderMenu();
					int type=Integer.parseInt(menuStr[i]);
					orderMenu.setType(type);
					switch (type) {
					case 1:
						orderMenu.setOrderMenuName("创建订单");
						break;
					case 2:
						orderMenu.setOrderMenuName("查询订单");
						break;
					case 3:
						orderMenu.setOrderMenuName("进货上报");
						break;
					case 4:
						orderMenu.setOrderMenuName("销量上报");
						break;
					case 5:
						orderMenu.setOrderMenuName("退货管理");
						break;
					case 6:
						orderMenu.setOrderMenuName("库存管理");
						break;
					default:
						break;
					}
					orderMenuList.add(orderMenu);
				}
			}
		}
		
		printHelper = new PrintHelper(getApplicationContext(), PrintHelper.DEVICE_BM9000);
		printHelper.initDialog(this);
	}
	
	/**
	 * 根据要显示的数据初初始化页面
	 */
	public void initLayout(){
		if(orderMenuList == null || orderMenuList.isEmpty()) return;
		int len = orderMenuList.size();
		if (len == 1) { // 表示就一个View
			OrderMenuItem orderMenuItem=new OrderMenuItem(this, orderMenuList.get(0), null);
			orderMenuItem.setStoreInfo(storeId, storeName);
			ll_order_menu.addView(orderMenuItem.getView());
		} else {
			for (int i = 1; i < len; i += 2) { // 表示要显示两个View
				OrderMenuItem orderMenuItem=new OrderMenuItem(this,orderMenuList.get(i-1), orderMenuList.get(i));
				orderMenuItem.setStoreInfo(storeId, storeName);
				ll_order_menu.addView(orderMenuItem.getView());
			}
			if (len % 2 != 0) { // 表示还有个View要显示
				OrderMenuItem orderMenuItem=new OrderMenuItem(this, orderMenuList.get(len-1), null);
				orderMenuItem.setStoreInfo(storeId, storeName);
				ll_order_menu.addView(orderMenuItem.getView());
			}
		}
	}
	
	public static void print(Context dialogContext, JSONArray templetSource, DataSource dataSource) throws Exception {
		switch (printHelper.status()) {
			case BluetoothPrinter.STATUS_CONNECTED:
				printHelper.setTemplet(PrintHelper.TEMPLET_JSON, templetSource, dataSource);
				printHelper.print();
				break;
				
			case BluetoothPrinter.STATUS_DISCONNECTED:
				printHelper.initDialog(dialogContext);
				printHelper.connect();
				break;
		}
	}
	
	public static void printElement(Element element) {
		printHelper.printElement(element);
	}
	
	public static void printNewLine() {
		printHelper.printNewLine();
	}
	
	public static void printDivider() {
		printHelper.printDivider();
	}
	
	@Override
	protected void onDestroy() {
		if (printHelper != null) {
			printHelper.release();
			printHelper = null;
		}
		
		super.onDestroy();
	}

}
