package com.yunhu.yhshxc.pay;

import gcg.org.debug.ELog;
import gcg.org.debug.JLog;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.ReplenishSearchResult;
import com.yunhu.yhshxc.database.FuncDB;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.list.activity.AbsSearchListActivity;
import com.yunhu.yhshxc.list.activity.ShowImageActivity;
import com.yunhu.yhshxc.list.activity.ShowTableActivity;
import com.yunhu.yhshxc.list.activity.TableListActivity;
import com.yunhu.yhshxc.list.activity.TableListActivityNew;
import com.yunhu.yhshxc.parser.ReplenishParse;
import com.yunhu.yhshxc.utility.AmountUtils;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.HttpHelper;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.unionpay.UPPayAssist.UPPayAssist;
import com.unionpay.uppay.PayActivity;

/**
 * 订单listActivity
 * @author 王建雨
 */
public class OrderListActivity extends AbsSearchListActivity {
	private final String TAG = "OrderListActivity";

	public static final String R_FAIL = "fail";
	public static final String R_SUCCESS = "success";
	public static final String R_CANCEL = "cancel";
	
	private List<Map<String,String>> selectList = new ArrayList<Map<String,String>>();
	
	private int pay_type;
	
	private Dialog confirmOrderDialog;
	private Dialog payBackDialog;
	private Dialog loadDialog;
	
	//需要在列表中显示的Func
	private List<Func> viewCoum;

	// 需要在下面显示的Func
	private List<Func> unViewCoum;
	
	private int unitViewWidth;
	
	String spId = "0001";
	String sysprovider = "00000001";
	
	private String orderNo;
	private String amount;
	
	/**
	 * 支付列表页面布局
	 */
	@Override
	protected int getLayoutId() {
		pay_type = OrderListActivity.this.getIntent().getIntExtra("pay_type", 0);
		return R.layout.pay_order_list;
	}

	/**
	 * 初始化列表表头（列名）
	 */
	@Override
	protected void initTitle(List<Func> viewCoum, LinearLayout ll_title) {
		if (pay_type == Constants.PAY_TYPE_SEARCH) { //未用到
			findViewById(R.id.ll_func_show_data).setVisibility(View.GONE);
			findViewById(R.id.iv_order_head_isSelected).setVisibility(View.GONE);
		}
		
		// 列为空，一般是由配置人员忘记配置显示列的原因
		if(viewCoum == null || viewCoum.isEmpty()){
			ToastOrder.makeText(OrderListActivity.this, setString(R.string.pay_order_02), ToastOrder.LENGTH_LONG).show();
			this.finish();
		}
		this.viewCoum = viewCoum;
		FuncDB funcDB = new FuncDB(this);
		unViewCoum = funcDB.findFuncListReplenish(targetId, 0, null,null); //查询要显示列的控件
		
		unitViewWidth = computeViewWidth(viewCoum.size()); //动态计算每一列的宽度
		
		/*固定添加一列 “状态” 显示当前的data_status*/
		TextView tv_title_1 = (TextView) View.inflate(this,R.layout.table_list_item_unit, null);
		tv_title_1.setLayoutParams(new LayoutParams(PublicUtils.convertDIP2PX(OrderListActivity.this, 62), LayoutParams.WRAP_CONTENT));
		tv_title_1.setText(setString(R.string.pay_order_state));
		tv_title_1.setTextColor(Color.rgb(255, 255, 255));
		tv_title_1.setTextSize(18);
		ll_title.addView(tv_title_1);
		
		for (Func func : viewCoum) {//循环添加要显示func的列名
				TextView tv_title = (TextView) View.inflate(this,R.layout.table_list_item_unit, null);
				tv_title.setLayoutParams(new LayoutParams(unitViewWidth, LayoutParams.WRAP_CONTENT));

				tv_title.setText(func.getName());
				tv_title.setTextColor(Color.rgb(255, 255, 255));
				tv_title.setTextSize(18);
				ll_title.addView(tv_title);
		}
		
//		if(viewCoum!=null &&!viewCoum.isEmpty()){
//			//去掉第一个竖线
//			if(viewCoum!=null && !viewCoum.isEmpty()){
//				ll_title.removeViewAt(0);
//			}
//		}
		
	}

	/**
	 * 计算每列显示的宽度
	 * @param showCount 显示列的个数
	 * @return 单位为px
	 */
	protected int computeViewWidth(int showCount){
		if(showCount==0){
			return 0;
		}
		int realPX = getWindowManager().getDefaultDisplay().getWidth();
		int realDIP = PublicUtils.convertPX2DIP(OrderListActivity.this, realPX)-102;
		int viewWidth = 100;
		int tempWidth = realDIP/showCount;
//		switch (showCount) {
//		case 0:
//			viewWidth = 314;
//			break;
//		case 1:
//			viewWidth = 157;
//			break;
//		case 2:
//			viewWidth = 105;
//			break;
//		}
		return PublicUtils.convertDIP2PX(OrderListActivity.this,viewWidth >= tempWidth ? viewWidth : tempWidth);
	}
	
	@Override
	protected List getDataListOnThread(String json) throws Exception {
		// 解析JSON
		ReplenishSearchResult searchResult = new ReplenishParse().parseSearchResult(json);
		// 补报数据
		  List<Map<String, String>> resultList = searchResult.getResultList();
		 return resultList;
	}

	@Override
	protected String getSearchUrl() {
		return UrlInfo.getUrlReplenish(this);
	}

	@Override
	protected HashMap<String, String> getSearchParams() {
		HashMap<String, String> searchParams =	new HashMap<String, String>();
		searchParams.put(Constants.TASK_ID, targetId+"");
		searchParams.put("type", modType+"");
		//TODO查询条件
		return searchParams;
	}

	@Override
	protected int getCurChildrenCount(int groupPosition) {
		return 1;
	}

	@Override
	protected View getCurGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		
		final Map<String, String> itemData = (Map<String, String>) dataList.get(groupPosition);
		// 优化...
		GroupViewHodler hodler;
		// 显示的item view
		LinearLayout view;
		
		if(convertView==null){//没有缓存view时
			// 加载item view
			view = (LinearLayout) View.inflate(OrderListActivity.this, R.layout.pay_order_list_item, null);
			// 初始化ViewHodler
			hodler = new GroupViewHodler();
			// 是否展开
			hodler.iv_isExpanded = (ImageView) view.findViewById(R.id.iv_pay_isExpanded);
			hodler.iv_isSelected = (ImageView) view.findViewById(R.id.iv_pay_isSelected);
			// 设定hodler.tvs的大小
			hodler.tvs = new TextView[viewCoum.size()+1];
			
			hodler.tvs[0] = (TextView)View.inflate(OrderListActivity.this, R.layout.replenish_list_item_unit, null);
			hodler.tvs[0].setLayoutParams(new LayoutParams(PublicUtils.convertDIP2PX(OrderListActivity.this, 62), LayoutParams.WRAP_CONTENT));
			// 添加textview到item view
			view.addView(hodler.tvs[0]);

			for (int i = 1;i<hodler.tvs.length;i++) {
				// 加载textview
				hodler.tvs[i] = (TextView)View.inflate(OrderListActivity.this, R.layout.replenish_list_item_unit, null);
				// 设置属性
				hodler.tvs[i].setLayoutParams(new LayoutParams(unitViewWidth+PublicUtils.convertDIP2PX(OrderListActivity.this, 2), LayoutParams.WRAP_CONTENT));
				// 添加textview到item view
				view.addView(hodler.tvs[i]);
			}
			
			// 将hodler全部添加到item view 的tag中
			view.setTag(hodler);
			
		}else{
			// 将缓存view赋给item view
			view = (LinearLayout) convertView;
			// 获取item view中的hodler
			hodler = (GroupViewHodler) view.getTag();
		}
		
		hodler.tvs[0].setText( itemData.get("status_name"));// 状态
		// 从第几列开始赋值
		int j = 1;
		// 循环可被显示的func
		for (Func func : viewCoum) {
			
			if(func.getType() == Func.TYPE_BUTTON ){// 按钮 不显示的 进行下一次循环
				continue;
			}
			//textview显示的内容
			String tvText = itemData.get(func.getFuncId().toString()) != null ? itemData.get(func.getFuncId().toString()) : "";
			tvText = getShowValue(itemData, func, tvText);
		
			if(func.getType() == Func.TYPE_CAMERA || func.getType() == Func.TYPE_CAMERA_HEIGHT || func.getType()==Func.TYPE_CAMERA_CUSTOM || func.getType() == Func.TYPE_CAMERA_MIDDLE || func.getType() == Func.TYPE_TABLECOMP||func.getType() == Func.TYPE_LINK){
				hodler.tvs[j].setClickable(true);
				hodler.tvs[j].setClickable(true);
				hodler.tvs[j].setTextSize(16);
				hodler.tvs[j].setTextColor(Color.rgb(0, 0, 255));
				hodler.tvs[j].getPaint().setUnderlineText(true);
				hodler.tvs[j].setOnClickListener(this);
				hodler.tvs[j].setId(groupPosition);
				hodler.tvs[j].setTag(func);
			}else{
				hodler.tvs[j].setTextSize(14);
				hodler.tvs[j].setTextColor(Color.rgb(0, 0, 0));
				hodler.tvs[j].setClickable(false);
				hodler.tvs[j].setClickable(false);
				hodler.tvs[j].getPaint().setUnderlineText(false);
			}
			// 设置textview显示的内容
			hodler.tvs[j].setText(tvText);
			// 下一条func对应的textview的索引
			j++;
		}
		if (pay_type == Constants.PAY_TYPE_SEARCH) {
			hodler.iv_isSelected.setVisibility(View.GONE);
		}else {
			hodler.iv_isSelected.setVisibility(View.VISIBLE);
			if ((!selectList.isEmpty()) && selectList.contains(itemData)) {// 已经有了,显示对勾
				hodler.iv_isSelected.setImageResource(R.drawable.pay_y);
			} else {// 还没有
				hodler.iv_isSelected.setImageResource(R.drawable.pay_n);
			}
			hodler.iv_isSelected.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 未支付
					if ((!selectList.isEmpty()) && selectList.contains(itemData)) {// 已经有了,删除
						selectList.remove(itemData);
						((ImageView) v).setImageResource(R.drawable.pay_n);
					} else {// 还没有,添加
						selectList.add(itemData);
						((ImageView) v).setImageResource(R.drawable.pay_y);
					}
				}
			});
		}
		
		if(isExpanded){
			hodler.iv_isExpanded.setImageResource(R.drawable.icon_reduce);
		}else{
			hodler.iv_isExpanded.setImageResource(R.drawable.icon_plus);
		}
		
		return view;
		

	}

	@Override
	protected View getCurChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		Map<String, String> itemData = (Map<String, String>) dataList.get(groupPosition);
		// 显示的item view
		RelativeLayout view = (RelativeLayout) View.inflate(OrderListActivity.this, R.layout.table_list_item_children, null);
		LinearLayout ll_child = (LinearLayout) view.findViewById(R.id.ll_table_list_item_children);
		// 屏蔽点击操作
		view.findViewById(R.id.bt_table_list_item_children_do).setVisibility(View.GONE);
		boolean isShowCount = false;
		for(Func func :unViewCoum){
			String tvValue = itemData.get(func.getFuncId().toString()) != null ? itemData.get(func.getFuncId().toString()) : "";
			if(!TextUtils.isEmpty(tvValue)){

				if(func.getType() == Func.TYPE_BUTTON){// 按钮 连接 不显示的 进行下一次循环
					continue;
				}else{
					tvValue = getShowValue(itemData, func, tvValue);
					if(!TextUtils.isEmpty(tvValue)){
						LinearLayout ll_unit = (LinearLayout) View.inflate(OrderListActivity.this, R.layout.table_list_item_children_unit, null);
						TextView tv_name = (TextView) ll_unit.findViewById(R.id.tv_table_item_children_unit_name);
						TextView tv_value = (TextView) ll_unit.findViewById(R.id.tv_table_item_children_unit_value);
						tv_name.setText(func.getName());
						tv_value.setText(tvValue);
						if(func.getType() == Func.TYPE_CAMERA || func.getType() == Func.TYPE_CAMERA_HEIGHT || func.getType() == Func.TYPE_CAMERA_CUSTOM || func.getType() == Func.TYPE_CAMERA_MIDDLE || func.getType() == Func.TYPE_TABLECOMP||func.getType() == Func.TYPE_LINK){
							tv_value.setTextSize(16);
							tv_value.setTextColor(Color.rgb(0, 0, 255));
							tv_value.getPaint().setUnderlineText(true);
							tv_value.setOnClickListener(this);
							tv_value.setId(groupPosition);
							tv_value.setTag(func);
							
						}
						ll_child.addView(ll_unit);
						isShowCount = true;
					}
				}
			}
		}
		if(isShowCount){
			ll_child.setVisibility(View.VISIBLE);
			return view;
		}else{
			ll_child.setVisibility(View.GONE);
			return new LinearLayout(OrderListActivity.this);
		}
	
	
	}

	@Override
	public void onClick(View v) {
		// 获取点击条目的map
				Map<String, String> clickItem = (Map<String, String>) dataList.get(v.getId());
				
				if(v.getTag() != null ){
					if(TextUtils.isEmpty(clickItem.get( ((Func)v.getTag()).getFuncId().toString()))){
						ToastOrder.makeText(OrderListActivity.this,setString(R.string.pay_order_06), ToastOrder.LENGTH_LONG).show();
						return;
					}
					Intent intent = null;
					int type = ((Func)v.getTag()).getType();
					if(type == Func.TYPE_CAMERA || type == Func.TYPE_CAMERA_HEIGHT || type == Func.TYPE_CAMERA_MIDDLE || type == Func.TYPE_CAMERA_CUSTOM){
						intent = new Intent(OrderListActivity.this,ShowImageActivity.class);
						intent.putExtra("imageUrl",clickItem.get( ((Func)v.getTag()).getFuncId().toString()));
						intent.putExtra("imageName", ((Func)v.getTag()).getName());
					}else if(type == Func.TYPE_TABLECOMP){
						intent = new Intent(OrderListActivity.this,ShowTableActivity.class);
						intent.putExtra("menuType", menuType);
						intent.putExtra("tableId", ((Func)v.getTag()).getTableId());
						intent.putExtra("funcName", ((Func)v.getTag()).getName());
						intent.putExtra("tableJson", clickItem.get( ((Func)v.getTag()).getFuncId().toString()));
					}else if(type == Func.TYPE_LINK){
						ELog.d("Open TableListActivity");
						intent = new Intent(OrderListActivity.this,TableListActivityNew.class);
						bundle.putInt("targetId", ((Func)v.getTag()).getMenuId());
						bundle.putInt("menuType", menuType);
						bundle.putInt("modType", modType);
						intent.putExtra("bundle", bundle);
						intent.putExtra("linkJson", clickItem.get( ((Func)v.getTag()).getFuncId().toString()));
					}
					startActivity(intent);
				}
		super.onClick(v);
	}
	
	
	/**
	 *缓存listview中item view-->listview的优化
	 */
	private class GroupViewHodler{
		public ImageView iv_isExpanded;
		public ImageView iv_isSelected;
		public TextView [] tvs;
	}
	
	/**
	 * 生产订单Hander
	 */
	public Handler orderHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if (loadDialog != null&& loadDialog.isShowing()) {
				loadDialog.dismiss();
			}
			if(TextUtils.isEmpty((String) msg.obj)){
				ToastOrder.makeText(OrderListActivity.this, setString(R.string.pay_order_11), ToastOrder.LENGTH_LONG).show();
				return;
			}
			goToPayOrder((String) msg.obj);
		};
	};

	private String r_state;
	
	/**
	 * 点击支付按钮事件
	 * @param v
	 */
	public void payOrder(View v) {
		if (selectList.isEmpty()) {
			ToastOrder.makeText(this, setString(R.string.pay_order_09), ToastOrder.LENGTH_LONG).show();
			return;
		}
	
//		if (checkApkExist(UPPayUtils.PACKAGE_NAME)) {
			loadDialog = new MyProgressDialog(this,R.style.CustomProgressDialog,setString(R.string.pay_order_10));
			loadDialog.show();
			getUrlData(orderHandler);
		
//		} else {
//			File file = new File(Constants.SDCARD_PATH+ UPPayUtils.APK_FILE_NAME);
//			if (!file.exists() || !file.isFile()) {
//				getApk(file);
//			} else {
//				showDialogToInstallApk(file);
//			}
//			JLog.d(TAG, "插件未安装");
//		}
	}
	
	/**
	 * 检测WIFI状态
	 * @return
	 */
	private boolean isWIFIOpen(){
		WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		return wifiManager.isWifiEnabled();
	}
	
	/**
	 * 设置无线网路
	 */
   private void openNetworkSettings() {      
        AlertDialog dialog = new AlertDialog.Builder(this)  
                .setTitle(setString(R.string.pay_order_07))
                .setMessage(setString(R.string.pay_order_08))
                .setPositiveButton(setString(R.string.pay_order_open), new DialogInterface.OnClickListener() {
                      
                    @Override  
                    public void onClick(DialogInterface dialog, int which) {  
                    	
//                    	if(android.os.Build.VERSION.SDK_INT <= 10){
	                        Intent intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
//	                        Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
//	                        ComponentName cName = new ComponentName("com.android.phone","com.android.phone.Settings");
//	                        intent.setComponent(cName);
	                        startActivity(intent);
//                    	}else{
//                    		Intent intent = new Intent(Settings.ACTION_SETTINGS);
//                            startActivity(intent);
//                    	}
                    }  
                })  
                .setNegativeButton(setString(R.string.pay_order_cancel),null)
                .show();          
    }
	    
   public boolean isWifiConnect() {  
	        ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);  
	         NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);  
	         return mWifi.isConnected();  
	 }  
	
	/**
	 * 从服务器获取订单数据
	 * @param handler
	 */
	public void getUrlData(final Handler handler) {
		new Thread(){
		
			@Override
			public void run() {
				
			  String payData = null;
			  String payInfo = getOrderInfo();
			  //String url = Constants.BASE_URL+"upPayNotice.do?phoneno="+PublicUtils.receivePhoneNO(getApplicationContext())+"&payInfo="+payInfo;
			  //JLog.d(TAG,"生产订单url====>>>"+url);
			  Map<String,String> params = new HashMap<String,String>();
			  params.put("phoneno", PublicUtils.receivePhoneNO(getApplicationContext()));
			  params.put("payInfo",payInfo);
			  payData = new HttpHelper(OrderListActivity.this).connectPost(UrlInfo.getUrlPayInfo(OrderListActivity.this), params);
			  
			  try {
				  parseOrderInfo(new ByteArrayInputStream(payData.getBytes("UTF-8")));
			  } catch (Exception e) {
				e.printStackTrace();
			  }
			  JLog.d(TAG, "payData==>"+payData);
		            
	           
		        Message msg =new Message();
		        msg.what = 1;
		        msg.obj = payData;
		        handler.sendMessage(msg);
		        
			}
			
		}.start();
    }
	
	/**
	 * 从服务器获取到xml中解析订单号和总金额
	 * @param is
	 * @throws Exception
	 */
	protected void parseOrderInfo(InputStream is) throws Exception {
		
		XmlPullParser xpParser = Xml.newPullParser();
		xpParser.setInput(is, "UTF-8");
		 int event = xpParser.getEventType();
//		 while (event != XmlPullParser.END_DOCUMENT) {
		 while ((TextUtils.isEmpty(orderNo) || TextUtils.isEmpty(amount)) && event != XmlPullParser.END_DOCUMENT) {
			 if(event==XmlPullParser.START_TAG){
				 if("order".equals(xpParser.getName())){
					 orderNo = xpParser.getAttributeValue(null, "id");
				 }else  if("transAmount".equals(xpParser.getName())){
					 amount =AmountUtils.changeF2Y(xpParser.nextText());
//					 convertAmount();
				 }
			 }
		
			event = xpParser.next();
		}
		
	}

	/**
	 * 将12位的总金额转换为123.45形式
	 */
	private void convertAmount() {
		JLog.d(TAG, "amount 之前==>"+amount);
		if(amount.startsWith("0")){
			amount=amount.substring(1);
			convertAmount();
		}else{
			amount=(TextUtils.isEmpty(amount.substring(0, amount.length()-2)) ? "0" : amount.substring(0, amount.length()-2))
							+"."
							+(((amount.substring( amount.length()-2, amount.length())).length()==1)?("0"+(amount.substring( amount.length()-2, amount.length()))):(amount.substring( amount.length()-2, amount.length())));
		}
		JLog.d(TAG, "amount 之后==>"+amount);
	}

	/**
	 * 生成查询订单需要的josnarray
	 * @return
	 */
	protected String getOrderInfo() {
		StringBuilder sb = new StringBuilder("[");
		for (Map<String, String> map :selectList) {
			sb.append("{")
				.append("moduleId:").append(targetId)
				.append(",")
				.append("patchId:").append(map.get(Constants.PATCH_ID))
				.append("},");
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.append("]").toString();
	}

	/**
	 * 判断连接网络是否需要代理
	 * @param context
	 * @return
	 */
	private boolean needSetProxy(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobNetInfo = connectivityManager.getActiveNetworkInfo();
        if (mobNetInfo == null || "WIFI".equals(mobNetInfo.getTypeName())) {
            return false;
        }
        if (mobNetInfo.getSubtypeName().toLowerCase().contains("cdma")) {
            // 电信网络
            if (android.net.Proxy.getDefaultHost() != null
                    && android.net.Proxy.getDefaultPort() != -1) {
                return true;
            }
        } else if (mobNetInfo.getExtraInfo().contains("wap")) {
            // 移动或联通网络
            return true;
        }
        return false;
    }
	
	/**
	 * 订单确认对话框,确定按钮会调用银联安全支付控件
	 * @param orderInfo
	 */
	private void goToPayOrder(final String orderInfo) {

		LinearLayout ll_confirm = (LinearLayout) View.inflate(OrderListActivity.this, R.layout.confirm_order, null);
		((TextView) ll_confirm.findViewById(R.id.tv_confirm_order_no)).setText(orderNo);
		((TextView) ll_confirm.findViewById(R.id.tv_confirm_order_sum)).setText(amount + " "+setString(R.string.pay_order_yuan));
//		LinearLayout ll_confirm_content = (LinearLayout) ll_confirm.findViewById(R.id.ll_order_content);
//		for (OrderEntry o : (List<OrderEntry>) dataList) {
//			// OrderEntry o = (OrderEntry) dataList.get(i);
//			if (selectList.contains(o)) {
//				LinearLayout ll_order_detail = (LinearLayout) View.inflate(OrderListActivity.this, R.layout.confirm_order_unit,null);
//				((TextView) ll_order_detail.findViewById(R.id.tv_confirm_order_unit_price)).setText(o.price + "");
//				((TextView) ll_order_detail.findViewById(R.id.tv_confirm_order_unit_p_name)).setText(o.p_name);
//				((TextView) ll_order_detail.findViewById(R.id.tv_confirm_order_unit_amount)).setText(o.amount + "");
//				ll_confirm_content.addView(ll_order_detail);
//			}
//		}
		ll_confirm.findViewById(R.id.bt_confirm_order_ok).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						
						if(isWIFIOpen()){
							if(!isWifiConnect()){
								ToastOrder.makeText(OrderListActivity.this, setString(R.string.pay_order_01), ToastOrder.LENGTH_LONG).show();
								return;
							}
							if (confirmOrderDialog != null&& confirmOrderDialog.isShowing()) {
								confirmOrderDialog.dismiss();
							}
							// 支付
							JLog.d(TAG, "start pay");
	//						UPPayAssist.test(OrderListActivity.this,spId,sysprovider,orderInfo);
							UPPayAssist.startPayByJAR(OrderListActivity.this, PayActivity.class, spId, sysprovider, orderInfo, true);
						}else{
							openNetworkSettings();
						}
					}
				});
		ll_confirm.findViewById(R.id.bt_confirm_order_cancel)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (confirmOrderDialog != null&& confirmOrderDialog.isShowing()) {
							confirmOrderDialog.dismiss();
							orderNo ="";
							amount ="";
						}
					}
				});
		confirmOrderDialog = new AlertDialog.Builder(OrderListActivity.this)
				.setInverseBackgroundForced(true)
				.setView(ll_confirm)
				.setCustomTitle(View.inflate(OrderListActivity.this,R.layout.confirm_order_head, null))
				.create();
	
		confirmOrderDialog.show();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data !=null){
			r_state = data.getExtras().getString("pay_result");
			if(!TextUtils.isEmpty(r_state)){
				JLog.d(TAG, "支付结果 ==== >>>>"+r_state);
				if( r_state.equalsIgnoreCase(R_SUCCESS) ){
					afterResult(R.drawable.pay_ok,setString(R.string.pay_order_03));
					selectList.clear();
				}else if( r_state.equalsIgnoreCase(R_FAIL) ){
					afterResult(R.drawable.pay_fail,setString(R.string.pay_order_04));
				}else if( r_state.equalsIgnoreCase(R_CANCEL) ){
					afterResult(R.drawable.pay_fail, setString(R.string.pay_order_05));
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * 弹出支付结果对话框
	 * @param resResultIcon	支付状态图标
	 * @param result				支付状态文本
	 */
	private void afterResult(int resResultIcon,String result) {

		payBackDialog = new AlertDialog.Builder(OrderListActivity.this)
		.setInverseBackgroundForced(true)
		.setCustomTitle(View.inflate(OrderListActivity.this,R.layout.confirm_order_back_head, null))
		.setView(getPayBackView(resResultIcon, result)).create();
		payBackDialog.show();

	}
	
	/**
	 * 支付结果对话框的布局
	 * @param icon_res
	 * @param showMsg
	 * @return
	 */
	private LinearLayout getPayBackView(int icon_res, String showMsg) {
		LinearLayout ll_confirm = (LinearLayout) View.inflate(OrderListActivity.this, R.layout.confirm_order_back, null);
		LinearLayout ll_realult = (LinearLayout)ll_confirm.findViewById(R.id.ll_pay_reasult);
		if(icon_res==R.drawable.pay_succeed){
			ll_realult.setBackgroundResource(R.color.pay_success);
		}else{
			ll_realult.setBackgroundResource(R.color.pay_file);
		}
		((TextView) ll_confirm.findViewById(R.id.tv_order_pay_state)).setText(showMsg);
		((ImageView) ll_confirm.findViewById(R.id.iv_order_pay_state)).setImageResource(icon_res);
		((TextView) ll_confirm.findViewById(R.id.tv_confirm_order_no)).setText(orderNo);
		((TextView) ll_confirm.findViewById(R.id.tv_confirm_order_sum)).setText(amount + " "+setString(R.string.pay_order_yuan));
		LinearLayout ll_confirm_content = (LinearLayout) ll_confirm.findViewById(R.id.ll_order_content);
		// for(int i = dataList.size();i>=0;i--){
//		for (OrderEntry o : (List<OrderEntry>) dataList) {
//			// OrderEntry o = (OrderEntry) dataList.get(i);
//			if (selectList.contains(o)) {
//				LinearLayout ll_order_detail = (LinearLayout) View.inflate(OrderListActivity.this, R.layout.confirm_order_unit,null);
//				((TextView) ll_order_detail.findViewById(R.id.tv_confirm_order_unit_price)).setText(o.price + "");
//				((TextView) ll_order_detail.findViewById(R.id.tv_confirm_order_unit_p_name)).setText(o.p_name);
//				((TextView) ll_order_detail.findViewById(R.id.tv_confirm_order_unit_amount)).setText(o.amount + "");
//				// ((TextView)
//				// ll_order_detail.findViewById(R.id.tv_confirm_order_unit_sum)).setText(o.sum+
//				// " 元");
//				ll_confirm_content.addView(ll_order_detail);
//			}
//		}
		
		((Button) ll_confirm.findViewById(R.id.bt_order_back_ok))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (payBackDialog != null && payBackDialog.isShowing()) {
							payBackDialog.dismiss();
							orderNo ="";
							amount ="";
							if( r_state.equalsIgnoreCase(R_SUCCESS) ){
								//防止重复支付,重新加载数据
								Bundle payBundle = new Bundle();
								payBundle.putInt("targetId", module.getMenuId());
								payBundle.putSerializable("module", module);
								payBundle.putInt("menuType", menuType);
								Intent payIntent = new Intent(OrderListActivity.this, OrderListActivity.class);
								payIntent.putExtra("bundle", payBundle);
								startActivity(payIntent);
								OrderListActivity.this.finish();
							}
						}
					}
				});
		return ll_confirm;
	}
	
//	/**
//	 * 检测插件apk是否已经安装
//	 * @param packageName
//	 * @return
//	 */
//	private  boolean checkApkExist(String packageName) {
//		return new File("/data/data/" + packageName).exists();
//	}
//	
//	/**
//	 * 获取apk资源
//	 * @param file
//	 */
//	private void getApk(File file) {
//		if (file.exists()) {
//			file.delete();
//		}
//		if (retrieveApkFromAssets(OrderListActivity.this,UPPayUtils.APK_FILE_NAME, file)) {
//			showDialogToInstallApk(file);
//		} else {
//			getApk(file);
//		}
//	}
//	
//	/**
//	 * 从资产目录里拷贝出apk
//	 * @param context
//	 * @param srcfileName
//	 * @param file
//	 * @return
//	 */
//	public static boolean retrieveApkFromAssets(Context context,
//		String srcfileName, File file) {
//		boolean bRet = false;
//		try {
//			InputStream is = context.getAssets().open(srcfileName);
//			FileOutputStream fos = new FileOutputStream(file);
//			byte[] temp = new byte[1024];
//			int i = 0;
//			while ((i = is.read(temp)) > 0) {
//				fos.write(temp, 0, i);
//			}
//			fos.close();
//			is.close();
//			bRet = true;
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return bRet;
//	}
//	
//	/**
//	 * 显示安装apk对话框
//	 * @param file
//	 */
//	private void showDialogToInstallApk(final File file) {
//		if (file.exists()) {
//			new AlertDialog.Builder(OrderListActivity.this)
//					.setIcon(R.drawable.icon)
//					.setTitle("插件安装提示")
//					.setMessage("欢迎使用GCG支付系统,由于您是第一次使用,请先安装银联插件,是否安装?")
//					.setPositiveButton(" 安  装 ",
//							new DialogInterface.OnClickListener() {
//								@Override
//								public void onClick(DialogInterface dialog,
//										int which) {
//									dialog.dismiss();
//									Intent intent = new Intent(Intent.ACTION_VIEW);
//									intent.setDataAndType(Uri.parse("file://"+ file.getAbsolutePath()),"application/vnd.android.package-archive");
//									startActivity(intent);
//								}
//							}).setNegativeButton(" 稍  后 ", null).create()
//					.show();
//		}
//	}
	
	@Override
	protected void onDestroy() {
		if (loadDialog != null && loadDialog.isShowing()) {
			loadDialog.dismiss();
		}
		if (payBackDialog != null && payBackDialog.isShowing()) {
			payBackDialog.dismiss();
		}

		if (confirmOrderDialog != null && confirmOrderDialog.isShowing()) {
			confirmOrderDialog.dismiss();
		}
		super.onDestroy();
	}
	private String setString(int stringId){
		return getResources().getString(stringId);
	}
}
