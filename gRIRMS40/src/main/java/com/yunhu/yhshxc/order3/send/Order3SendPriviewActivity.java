package com.yunhu.yhshxc.order3.send;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.order3.TakePhotoPopupWindow;
import com.yunhu.yhshxc.order3.bo.Order3;
import com.yunhu.yhshxc.order3.bo.Order3ProductData;
import com.yunhu.yhshxc.order3.db.Order3DB;
import com.yunhu.yhshxc.order3.db.Order3ProductDataDB;
import com.yunhu.yhshxc.order3.print.Order3PrintForSend;
import com.yunhu.yhshxc.order3.util.Order3Util;
import com.yunhu.yhshxc.order3.util.SharedPreferencesForOrder3Util;
import com.yunhu.yhshxc.print.templet.Element;
import com.yunhu.yhshxc.print.templet.Templet.DataSource;
import com.yunhu.yhshxc.submitManager.bo.PendingRequestVO;
import com.yunhu.yhshxc.submitManager.bo.TablePending;
import com.yunhu.yhshxc.submitManager.core.SubmitWorkManager;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.FileHelper;
import com.yunhu.yhshxc.utility.MD5Helper;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;

public class Order3SendPriviewActivity extends FragmentActivity implements DataSource{
	private Order3SendPriviewProductListFragment listFragment = null;
	private TextView tv_order3_send_p_number;//订单编号
	private TextView tv_order3_send_p_kh;//客户
	private TextView tv_order3_send_p_name;//订单联系人
	private TextView tv_order3_send_p_msg;//送货留言
	private LinearLayout btn_order3_send_p_dy;//打印
	private LinearLayout btn_order3_send_p_photo;//拍照
	private LinearLayout btn_order3_send_submit;//提交
	private TextView tv_order3_send_p_ddje;//送货订单金额
	private TextView tv_order3_send_p_lssk;//送货历史收款
	private TextView tv_order3_send_p_amount;//订单金额
	private TextView tv_order3_send_p_wfje;//未付金额

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order3_send_priview_avtivity);
		initWidget();
		initIntentData();
		addCombineFragment();
	}
	
	private void initWidget(){
		tv_order3_send_p_number = (TextView)findViewById(R.id.tv_order3_send_p_number);
		tv_order3_send_p_kh = (TextView)findViewById(R.id.tv_order3_send_p_kh);
		tv_order3_send_p_name = (TextView)findViewById(R.id.tv_order3_send_p_name);
		tv_order3_send_p_amount = (TextView)findViewById(R.id.tv_order3_send_p_amount);
		tv_order3_send_p_msg = (TextView)findViewById(R.id.tv_order3_send_p_msg);
		btn_order3_send_p_dy = (LinearLayout)findViewById(R.id.btn_order3_send_p_dy);
		btn_order3_send_p_dy.setOnClickListener(listener);
		btn_order3_send_p_photo = (LinearLayout)findViewById(R.id.btn_order3_send_p_photo);
		btn_order3_send_p_photo.setOnClickListener(listener);
		btn_order3_send_submit = (LinearLayout)findViewById(R.id.btn_order3_send_submit);
		btn_order3_send_submit.setOnClickListener(listener);
		tv_order3_send_p_ddje = (TextView)findViewById(R.id.tv_order3_send_p_ddje);
		tv_order3_send_p_lssk = (TextView)findViewById(R.id.tv_order3_send_p_lssk);
		tv_order3_send_p_wfje = (TextView)findViewById(R.id.tv_order3_send_p_wfje);
		controlPrint();
	}
	//是否需要手机打印（1、不要；2、要；默认：1）
	private void controlPrint(){
		int isPrint = SharedPreferencesForOrder3Util.getInstance(this).getIsPhonePrint();
		if (isPrint == 1) {
			btn_order3_send_p_dy.setVisibility(View.GONE);
		}else{
			btn_order3_send_p_dy.setVisibility(View.VISIBLE);
		}
	}

	public String orderAmount;//收款
	public String amountHistory;//历史收款
	public String amount;//订单总额
	public String unPayAmount;//未付金额
	public String orderNo;
	public String khmc;
	public String contact;
	public String sendMsg;
	private void initIntentData(){
		Intent intent = getIntent();
		orderNo = intent.getStringExtra("orderNo");
		tv_order3_send_p_number.setText(orderNo);
		khmc = intent.getStringExtra("khmc");
		tv_order3_send_p_kh.setText(khmc);
		contact = intent.getStringExtra("contact");
		tv_order3_send_p_name.setText(contact);
		
		//本次收款
		orderAmount = intent.getStringExtra("orderAmount");
		if (!TextUtils.isEmpty(orderAmount)) {
			tv_order3_send_p_amount.setText(orderAmount+" 元");
		}else{
			tv_order3_send_p_amount.setText("0 元");
		}
		
		//历史收款
		amountHistory = intent.getStringExtra("amountHistory");
		if (!TextUtils.isEmpty(amountHistory)) {
			tv_order3_send_p_lssk.setText(amountHistory+"元");
		}else{
			tv_order3_send_p_lssk.setText("0 元");
		}
		
		//未付金额
		unPayAmount = intent.getStringExtra("unPayAmount");
		if (!TextUtils.isEmpty(unPayAmount)) {
			tv_order3_send_p_wfje.setText(unPayAmount+"元");
		}else{
			tv_order3_send_p_wfje.setText("0 元");
		}
		
		//订单总额
		amount = intent.getStringExtra("amount");
		if (!TextUtils.isEmpty(amount)) {
			tv_order3_send_p_ddje.setText(amount+"元");
		}else{
			tv_order3_send_p_ddje.setText("0 元");
		}
		
		sendMsg = intent.getStringExtra("sendMsg");
		if (!TextUtils.isEmpty(sendMsg)) {
			tv_order3_send_p_msg.setText(sendMsg);
		}
	}
	
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_order3_send_p_dy:
				print();
				break;
			case R.id.btn_order3_send_p_photo:
				showPhotoPopuWindow();
				break;
			case R.id.btn_order3_send_submit:
				submit();
				break;

			default:
				break;
			}
		}
	};
	
	/**
	 * 打印订单
	 */
	private Order3PrintForSend orderPrint;
	private void print() {
		FileHelper help = new FileHelper();
		String json = help.readJsonString(this, "order3_print_send.txt");
		try {
			orderPrint = new Order3PrintForSend(this);
			
			orderPrint.print(this, new JSONArray(json), this);
			int sendPrintCount = SharedPreferencesForOrder3Util.getInstance(this).getOrderSendPrintCount();
			SharedPreferencesForOrder3Util.getInstance(this).setOrderSendPrintCount(sendPrintCount+1);
		}catch (Exception e) {
			e.printStackTrace();
			ToastOrder.makeText(this, "打印数据异常", ToastOrder.LENGTH_SHORT).show();
		}
		
	}
	
	@Override
	public void printLoop(List<Element> columns) {
		orderPrint.printLoop(columns);
	}
	
	@Override
	public void printRow(List<Element> columns) {
		orderPrint.printRow(columns);
	}
	
	@Override
	public void printPromotion(List<Element> columns) {
		orderPrint.printPromotion(columns);
	}

	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (orderPrint!=null) {
			orderPrint.release();
		}
	}

	
	/**
	 * 提交
	 */
	private void submit(){
		HashMap<String, String> params = sendParams();
		if (params!=null) {
			submitDataBackground(params);
		}
		if (!photoList.isEmpty()) {
			for (int i = 0; i < photoList.size(); i++) {
				submitPhotoBackground(photoList.get(i));
			}
		}
		SharedPreferencesForOrder3Util.getInstance(this).setOrderSendPrintCount(0);
		SubmitWorkManager.getInstance(this).commit();
		sendSuccessReceiver();
		Order3SendPriviewActivity.this.finish();
	}
	
	/**
	 * 提交订单数据
	 * @param params
	 */
	private void submitDataBackground(HashMap<String, String> params){
		PendingRequestVO vo = new PendingRequestVO();
    	vo.setContent("订单送货数据");
    	vo.setTitle("订单");
    	vo.setMethodType(SubmitWorkManager.HTTP_METHOD_POST);
    	vo.setType(TablePending.TYPE_DATA);
    	vo.setUrl(UrlInfo.doThreeOrderInfo(this));
    	vo.setParams(params);
		SubmitWorkManager.getInstance(this).performJustSubmit(vo);
	}
	
	/**
	 * 提交订单图片
	 * @param name
	 */
	private void submitPhotoBackground(String name){
		PendingRequestVO vo = new PendingRequestVO();
		vo.setContent("订单送货图片");
		vo.setTitle("订单");
		vo.setMethodType(SubmitWorkManager.HTTP_METHOD_POST);
		vo.setType(TablePending.TYPE_IMAGE);
		vo.setUrl(UrlInfo.getUrlPhoto(this));
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("name", name);
		params.put("companyid", String.valueOf(SharedPreferencesUtil.getInstance(this).getCompanyId()));
		params.put("md5Code", MD5Helper.getMD5Checksum2(Constants.ORDER3_PATH + name));
		vo.setParams(params);
		vo.setImagePath(Constants.ORDER3_PATH + name);
		SubmitWorkManager.getInstance(this).performImageUpload(vo);
	}
	
	
	private void sendSuccessReceiver(){
		Intent intent = new Intent(Constants.BROADCAST_ORDER3_SEND_SUCCESS);
		this.sendBroadcast(intent);
	}
	
	private List<String> photoList = new ArrayList<String>();//图片集合
	private HashMap<String, String> sendParams(){
		HashMap<String, String> params = new HashMap<String, String>();
		try {
			List<Order3> orderList = new Order3DB(this).findCombineOrder();
			if (!orderList.isEmpty()) {
				double payAmount = 0;
				double subAmount = 0;
				Order3ProductDataDB dataDB = new Order3ProductDataDB(this);
				if (!TextUtils.isEmpty(orderAmount)) {
					payAmount = Double.parseDouble(orderAmount);
					subAmount = payAmount;
					for (int i = 0; i < orderList.size(); i++) {
						Order3 order = orderList.get(i);
						double unPay = order.getUnPayAmount();//订单未支付金额
						if (i == orderList.size()-1) {//如果是最后一个 无论剩余收款是多少都给最后一个订单
							order.setPayAmount(order.getPayAmount() + subAmount);
							order.setUnPayAmount(order.getActualAmount() - order.getPayAmount());
						}else{
							if (unPay >0 && subAmount >= unPay) {//如果订单有未支付金额并且收款大于等于订单未支付款额 说明全部付款
								order.setPayAmount(order.getActualAmount());//全部付款
								order.setUnPayAmount(0);
								subAmount = payAmount - unPay;
							}else if(unPay > 0 && subAmount < unPay){//订单有未支付的款项 但是剩余的收款不够付款
								order.setPayAmount(order.getPayAmount()+subAmount);
								order.setUnPayAmount(order.getActualAmount() - order.getPayAmount());
								subAmount = 0;
							}
						}
						setSendOrderImage(order, i);
						List<Order3ProductData> dataList = dataDB.findOrder3ProductDataByOrderNo(order.getOrderNo());
						order.setProductList(dataList);
					}
				}else{
					for (int i = 0; i < orderList.size(); i++) {
						Order3 order = orderList.get(i);
						setSendOrderImage(order, i);
						List<Order3ProductData> dataList = dataDB.findOrder3ProductDataByOrderNo(order.getOrderNo());
						order.setProductList(dataList);
					}
				}
				String data = new Order3Util(this).submitJson(orderList);
				params.put("storeId", orderList.get(0).getStoreId());
				params.put("order", data);
				params.put("phoneno", PublicUtils.receivePhoneNO(this));
				params.put("sendMsg", sendMsg);
				params.put("sendTime", String.valueOf(System.currentTimeMillis()));
			}
		} catch (Exception e) {
			ToastOrder.makeText(this, R.string.ERROR_DATA, ToastOrder.LENGTH_SHORT).show();
		}
		return params;
	}
	
	private void setSendOrderImage(Order3 order,int index){
		String image1 = SharedPreferencesForOrder3Util.getInstance(this).getPhotoNameOne();
		if (!TextUtils.isEmpty(image1)) {
			order.setSendImage1(image1);
			if (index==0) {
				photoList.add(image1);
			}
		}
		String image2 = SharedPreferencesForOrder3Util.getInstance(this).getPhotoNameTwo();
		if (!TextUtils.isEmpty(image2)) {
			order.setSendImage2(image2);
			if (index==0) {
				photoList.add(image2);
			}
		}
		order.setSendPrintCount(SharedPreferencesForOrder3Util.getInstance(this).getOrderSendPrintCount());
	}
	/**
	 * 添加订单合并列表Fragment
	 */
	public List<Order3SendPriviewListData> order3SendPriviewListData = null;
	private void addCombineFragment(){
		listFragment = new Order3SendPriviewProductListFragment();
		getSupportFragmentManager().beginTransaction().add(R.id.ll_order3_send_p_list, listFragment).commit();
		order3SendPriviewListData = order3SendPriviewDataList();
		listFragment.setDataSrc(order3SendPriviewListData);
	}
	
	private Map<String, Order3SendPriviewListData> sendDataMap = new HashMap<String, Order3SendPriviewListData>();
	private List<Order3SendPriviewListData> order3SendPriviewDataList(){
		List<Order3SendPriviewListData> list = new ArrayList<Order3SendPriviewListData>();
		List<Order3> orderList = new Order3DB(this).findCombineOrder();
		Order3ProductDataDB db = new Order3ProductDataDB(this);
		for (int i = 0; i < orderList.size(); i++) {
			Order3 order = orderList.get(i);
			List<Order3ProductData> dataList = db.findOrder3ProductDataByOrderNo(order.getOrderNo());
			for (int j = 0; j < dataList.size(); j++) {
				Order3ProductData data = dataList.get(j);
				String productId = String.valueOf(data.getProductId());
				String unitId = String.valueOf(data.getUnitId());
				String key = new StringBuffer(productId).append(";").append(unitId).toString();
				double sendCount = data.getCurrentSendCount();
				if (!sendDataMap.containsKey(key)) {
					Order3SendPriviewListData listData = new Order3SendPriviewListData();
					String productName = data.getProductName();
					String unit = data.getProductUnit();
					listData.setName(productName);
					listData.setUnit(unit);
					listData.setNumber(sendCount);
					sendDataMap.put(key, listData);
				}else{
					Order3SendPriviewListData listData = sendDataMap.get(key);
					double number = listData.getNumber();
					listData.setNumber(number+sendCount);
				}
			}
		}
		for (Map.Entry<String, Order3SendPriviewListData> entry : sendDataMap.entrySet()) {
			list.add(entry.getValue());
		}
		return list;
	}
	
	private TakePhotoPopupWindow takePhotoPopupWindow = null;
	private void showPhotoPopuWindow() {
		takePhotoPopupWindow = new TakePhotoPopupWindow(this);
		takePhotoPopupWindow.show(null,SharedPreferencesForOrder3Util.getInstance(this).getPhotoNameOne(),SharedPreferencesForOrder3Util.getInstance(this).getPhotoNameTwo());
	}
	
	public String getString(EditText s) {
		return s.getText().toString();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (takePhotoPopupWindow!=null) {
			takePhotoPopupWindow.onActivityResult(requestCode, resultCode, data);
		}
			
	}

	@Override
	public void printZhekou(List<Element> columns) {
		
	}

	@Override
	public void printDingdanbianhao(List<Element> columns) {
		orderPrint.printDingdanbianhao(columns);
		
	}
}
