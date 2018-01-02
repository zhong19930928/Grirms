package com.yunhu.yhshxc.order3;

import gcg.org.debug.JLog;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.MenuUsableControl;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.bo.Submit;
import com.yunhu.yhshxc.database.MainMenuDB;
import com.yunhu.yhshxc.database.SubmitDB;
import com.yunhu.yhshxc.http.submit.SubmitManager;
import com.yunhu.yhshxc.listener.SubmitDataListener;
import com.yunhu.yhshxc.order3.bo.Order3;
import com.yunhu.yhshxc.order3.bo.Order3Contact;
import com.yunhu.yhshxc.order3.bo.Order3Product;
import com.yunhu.yhshxc.order3.bo.Order3ProductCtrl;
import com.yunhu.yhshxc.order3.bo.Order3ProductData;
import com.yunhu.yhshxc.order3.bo.Order3Promotion;
import com.yunhu.yhshxc.order3.bo.Order3PromotionInfo;
import com.yunhu.yhshxc.order3.bo.Order3ShoppingCart;
import com.yunhu.yhshxc.order3.db.Order3ContactsDB;
import com.yunhu.yhshxc.order3.db.Order3ProductCtrlDB;
import com.yunhu.yhshxc.order3.db.Order3PromotionDB;
import com.yunhu.yhshxc.order3.db.Order3ShoppingCartDB;
import com.yunhu.yhshxc.order3.print.Order3PrintForCreate;
import com.yunhu.yhshxc.order3.util.Order3Util;
import com.yunhu.yhshxc.order3.util.SharedPreferencesForOrder3Util;
import com.yunhu.yhshxc.print.templet.Element;
import com.yunhu.yhshxc.print.templet.Templet.DataSource;
import com.yunhu.yhshxc.submitManager.bo.PendingRequestVO;
import com.yunhu.yhshxc.submitManager.bo.TablePending;
import com.yunhu.yhshxc.submitManager.core.SubmitWorkManager;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.FileHelper;
import com.yunhu.yhshxc.utility.MD5Helper;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;

@SuppressLint("NewApi")
public class ShoppingCarActivity extends FragmentActivity implements DataSource,SubmitDataListener{
	
	private final String TAG = "ShoppingCarActivity";
//	private LinearLayout ll_home;
	private LinearLayout ll_prince;
	private LinearLayout ll_take_photo;
	private LinearLayout ll_submit;
	private TextView tv_discount;
	private TextView tv_collection;
	private TextView tv_yulan;
	private LinearLayout ll_location_bar;
	private LinearLayout ll_yulan_btns;
	private LinearLayout layout_order;
	private LinearLayout ll_home_yulan;
	private LinearLayout layout_discount;
	private LinearLayout layout_collection;
	// 帧布局对象,就是用来存放Fragment的容器
	private FrameLayout flayout;
	// 定义4个Fragment的对象

	private ShoppingCarFragment shoppingCarFragment;
	private ShouKuanFragment shouKuanFragment;
	private ZengpinFragment zenpinFragment;
	private YulanFragment yuLanFragment;
	private List<Order3PromotionInfo> infos = new ArrayList<Order3PromotionInfo>();
	// 定义FragmentManager对象
	FragmentManager fManager;
	private Order3PromotionDB promotionDB;
	private Order3ProductCtrlDB ctrlDb;
	public Order3Util order3Util;
	public String storeId;
	public Order3Contact order3Contact;
	private int step = 0;
	private boolean isSubmit = false;
	
	private int menuType;
	private int targetId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		menuType = getIntent().getExtras().getInt("menuType", 0);
		targetId = getIntent().getExtras().getInt("targetId", 0);

		order3ShoppingCartDB = new Order3ShoppingCartDB(this);
		promotionDB = new Order3PromotionDB(this);
		ctrlDb = new Order3ProductCtrlDB(this);
		order3Util = new Order3Util(this);
		storeId = SharedPreferencesForOrder3Util.getInstance(this).getStoreId();
		setContentView(R.layout.order3_activity_main_shopping_car);
		initAllViews();
	}

	private void initAllViews() {
		fManager = getSupportFragmentManager();
		flayout = (FrameLayout) this.findViewById(R.id.framelayout);
		ll_location_bar = (LinearLayout) this.findViewById(R.id.ll_location_bar);
		ll_yulan_btns = (LinearLayout) this.findViewById(R.id.ll_yulan_btns);
		layout_discount = (LinearLayout) this.findViewById(R.id.layout_discount);
		layout_collection = (LinearLayout) this.findViewById(R.id.layout_collection);
		ll_prince = (LinearLayout) this.findViewById(R.id.ll_prince);
		ll_take_photo = (LinearLayout) this.findViewById(R.id.ll_take_photo);
		ll_submit = (LinearLayout) this.findViewById(R.id.ll_submit);
		tv_discount = (TextView) this.findViewById(R.id.tv_discount);
		tv_collection = (TextView) this.findViewById(R.id.tv_collection);
		tv_yulan = (TextView) this.findViewById(R.id.tv_shopping_car);
		layout_order = (LinearLayout) this.findViewById(R.id.layout_order);
		ll_home_yulan = (LinearLayout) this.findViewById(R.id.ll_home_yulan);
		controlPrint();
		initFragments();
	}
	//是否需要手机打印（1、不要；2、要；默认：1）
	private void controlPrint(){
		int isPrint = SharedPreferencesForOrder3Util.getInstance(this).getIsPhonePrint();
		if (isPrint == 1) {
			ll_prince.setVisibility(View.GONE);
		}else{
			ll_prince.setVisibility(View.VISIBLE);
		}
	}
		
	private void initFragments() {
		setChioceItem(0);
		initData();
	}

	public void LayoutOnclickMethod(View layout) {
		switch (layout.getId()) {
		case R.id.layout_discount://收款留言
			setChioceItem(1);
			break;
		case R.id.layout_collection://本次赠品
			setChioceItem(2);
			break;
		case R.id.layout_shopping_car://订单预览
			setChioceItem(3);
			break;
//		case R.id.ll_home://首页
//			goHome();
//			break;
		case R.id.layout_order:
			goHome();
			break;
		case R.id.ll_home_yulan:
			goHome();
			break;
		case R.id.ll_prince://打印
			print();
			break;
		case R.id.ll_take_photo://拍照
			showPopuWindow();
			break;
		case R.id.ll_submit://提交
			if(!isSubmit){
				String isInput = SharedPreferencesForOrder3Util.getInstance(ShoppingCarActivity.this).getIsInput();
				if ("1".equals(isInput)) {//必须提交扩展模块
					String tm = SharedPreferencesForOrder3Util.getInstance(ShoppingCarActivity.this).getOrderTimestamp();
					if (!TextUtils.isEmpty(tm)) {
						setOrder3Time();
					}else{
						String linkName = SharedPreferencesForOrder3Util.getInstance(ShoppingCarActivity.this).getLinkName();
						ToastOrder.makeText(ShoppingCarActivity.this, linkName+"的数据必须填报", ToastOrder.LENGTH_SHORT).show();
					}
				}else{
					setOrder3Time();
				}
			}
			
			break;
		default:
			break;
		}
	}
	private  void setOrder3Time(){
		flagOrder3Time = true;
		timer = new Timer(true);
		if(task.cancel()){
			timer.schedule(task,3000, 100000000); //延时3000ms后执行，1000ms执行一次
		}
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Date date = PublicUtils.getNetDate();;
				  Message msg = Message.obtain();
				  msg.obj = date;
				  mHanlder.sendMessage(msg);
			}
		}).start();
	}
	 Handler mHanlder = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(flagOrder3Time){
				flagOrder3Time = false;
				if (task != null){
					 task.cancel();
				 }
				Date date = (Date) msg.obj;
				if(null==date){
					Toast.makeText(ShoppingCarActivity.this, "请检查当前网络是否可用", Toast.LENGTH_SHORT).show();
				}else{
					if (usableControlOrder3(date)) {
						submit(date);
						isSubmit = true;
					}
					
				}
			}
		};
	};
	/**
	 * 手机可用时间段控制,新订单模块
	 * @param menu
	 * @return
	 */
	public  boolean usableControlOrder3(Date date){
		boolean flag = true;
		String time = null;
		Menu menu = null;
		
			menu = new MainMenuDB(this).findMenuListByMenuType(menuType);
		
//		time = "12:00|0.5,14:55|0.5";
		time = menu!=null?menu.getPhoneUsableTime():"";
		MenuUsableControl  control = new MenuUsableControl();
		flag = control.isCanUse(time,date);
		if (menuType == Menu.TYPE_ORDER3) {
			if (flag) {//时间过了判断天
				flag = control.isOrder3CanUse(this,date);
				if (!flag) {
					Toast.makeText(ShoppingCarActivity.this, "该模块今天不能使用", Toast.LENGTH_SHORT).show();
					isSubmit = false;
				}
			}else{
				Toast.makeText(ShoppingCarActivity.this, control.tipInfo(time), Toast.LENGTH_SHORT).show();
				isSubmit = false;
			}
		}else{
			if (!flag) {
				Toast.makeText(ShoppingCarActivity.this, control.tipInfo(time), Toast.LENGTH_SHORT).show();
				isSubmit = false;
			}
		}
		return flag;
	}
	private  boolean flagOrder3Time = true;
	 Timer timer;
	 TimerTask task = new TimerTask(){  
        public void run() {  
        	mHanlder.sendEmptyMessage(1);   
        }  
	};
	/**
	 * 提交的购物信息,成为常用列表
	 */
	public void setCommonList(){
		for(int i = 0; i<order3Shoppingcars.size(); i++){
			if(order3Shoppingcars.get(i).getPitcOn()==1){
				Order3ProductCtrl produCtrl  = ctrlDb.findProductCtrlByProductIdAndUnitId(order3Shoppingcars.get(i).getProductId(), order3Shoppingcars.get(i).getUnitId());
				if(produCtrl!=null){
					ctrlDb.updateProductCtrlCount(produCtrl);
				}
			}
		}
	}
	/**
	 * 提交订单
	 */
	private void submit(Date date) {
		order3Shoppingcars = order3ShoppingCartDB.findAllList();
		double min = Double.parseDouble(SharedPreferencesForOrder3Util.getInstance(ShoppingCarActivity.this).getMinNum());
		double max = Double.parseDouble(SharedPreferencesForOrder3Util.getInstance(ShoppingCarActivity.this).getMaxNum());
		if(min>0){
			
		}else{
			min = 1;
		}
		if(max <= 0){
			max = Double.MAX_VALUE;
		}
		for(int i = 0; i<order3Shoppingcars.size(); i++){
			Order3ShoppingCart cart = order3Shoppingcars.get(i);
			if(cart.getNumber()<min){
				Order3Product product = order3Util.product(cart.getProductId(), cart.getUnitId());
				ToastOrder.makeText(ShoppingCarActivity.this, product.getName()+"至少购买"+PublicUtils.formatDouble(min)+product.getUnit(), ToastOrder.LENGTH_LONG).show();
				return ;
			}else if(cart.getNumber()>max){
				Order3Product product = order3Util.product(cart.getProductId(), cart.getUnitId());
				ToastOrder.makeText(ShoppingCarActivity.this, product.getName()+"最多购买"+PublicUtils.formatDouble(max)+product.getUnit(), ToastOrder.LENGTH_LONG).show();
				return ;
			}
		}
		HashMap<String, String> parms = submitParams(date);
		if (parms == null) {
			ToastOrder.makeText(this, "请选择产品", ToastOrder.LENGTH_SHORT).show();
		}else{
			submitDataBackground(parms);
		}
		if (!photoList.isEmpty()) {
			for (int i = 0; i < photoList.size(); i++) {
				String name = photoList.get(i);
				submitPhotoBackground(name);
			}
		}
		setCommonList();
		controlOrderExpand();
		sendBroadcast(new Intent(Constants.BROADCAST_ORDER3_CREATE_SUCCESS));
		order3Util.orderNumber(false);
		SubmitWorkManager.getInstance(this).commit();
		SharedPreferencesForOrder3Util.getInstance(this).clearSubmitMeassage();
		new Order3ShoppingCartDB(ShoppingCarActivity.this).deleteSub();
		ShoppingCarActivity.this.finish();
	}
	

	/**
	 * 控制订单拓展模块数据
	 */
	private void controlOrderExpand(){
		String timestamp = SharedPreferencesForOrder3Util.getInstance(this).getOrderTimestamp();
		if (!TextUtils.isEmpty(timestamp)) {
			SubmitDB submitDB  = new SubmitDB(this);
			Submit submit = submitDB.findSubmitByTimestamp(timestamp);
			if (submit!=null) {
				submit.setState(Submit.STATE_SUBMITED);
				submitDB.updateSubmit(submit);
				updateLinkSubmitState(submit.getId());
				SubmitManager.getInstance(this).submitData(true, this);
			}
		}
	}
	
	/**
	 * 更改超链接提交状态
	 * @param parentId 上层的表单ID
	 */
	public void updateLinkSubmitState(int parentId){
		SubmitDB submitDB = new SubmitDB(this);
		//超链接的情况更改子链接的submit的提交状态
		List<Submit> linkSubmitList=submitDB.findSubmitByParentId(parentId);
		if(!linkSubmitList.isEmpty()){
			submitDB.updateSubmitStateByParentId(parentId);
			for (int i = 0; i < linkSubmitList.size(); i++) {
				Submit linkSubmit=linkSubmitList.get(i);
				updateLinkSubmitState(linkSubmit.getId());
			}
		}
	}
	
	
	private List<String> photoList = new ArrayList<String>();//图片集合
	private HashMap<String, String> submitParams(Date date){
		HashMap<String, String> params = new HashMap<String, String>();
		try {
			Order3 order3 = getOrder3(date);
			String image1 = order3.getImage1();
			String image2 = order3.getImage2();
			if (!TextUtils.isEmpty(image1)) {
				photoList.add(image1);
			}
			if (!TextUtils.isEmpty(image2)) {
				photoList.add(image2);
			}
			List<Order3ProductData> listData = order3.getProductList();
			if (listData == null || listData.isEmpty()) {
				return null;
			}
			List<Order3> orderList = new ArrayList<Order3>();
			orderList.add(order3);
			String data = order3Util.submitJson(orderList);
			params.put("phoneno", PublicUtils.receivePhoneNO(this));
			params.put("storeId", storeId);
			params.put("order", data);
			JLog.d(TAG, "storeId:"+storeId+" order:"+data);
//			JLog.d(TAG, params.toString());
//			JLog.d("aaa", "storeId:"+storeId+" order:"+data);
		} catch (Exception e) {
			e.printStackTrace();
			ToastOrder.makeText(this, "订单数据异常", ToastOrder.LENGTH_SHORT).show();
		}
		return params;
	}

	List<Order3ShoppingCart> order3Shoppingcars;
	Order3ShoppingCartDB order3ShoppingCartDB;
	
	private void goHome() {
		this.finish();
	}


	Order3Product allNumProductGift;
	Order3Product allPriceProGift;
	private void initData() {
		infos.clear();
		order3Shoppingcars = order3ShoppingCartDB.findAllList();
		shoppingCarFragment.initdb();
		infos.removeAll(infos);
		if(SharedPreferencesForOrder3Util.getInstance(ShoppingCarActivity.this).getIsPromotion()==1){
			
		}else{
			danPinInfo();
			zongHeInfo();
		}
		
	}
	/**
	 * 总和赠品折扣信息
	 */
	private void zongHeInfo() {
		Map<String,String> mapNumDis = zongHeFullNumDis();//买满数量打折
		Map<String,String> mapNumJM = zongHeFullNumJM();//买满数量减免
		Map<String,String> mapPriceDis = zongHeFullNumDis6();//买满金额打折
		Map<String,String> mapPriceJM = zongHeFullNumJM6();//买满金额减免
		Map<String,Double> mapNum  = zongHeFullNum5() ;//送赠品
		Map<String,Double> mapNum6  = zongHeFullNum6() ;//送赠品
		double allNumAmount = Math.abs(Double.parseDouble(mapNumDis.get("pre")));
		double allNumAmount1 = Math.abs(Double.parseDouble(mapNumJM.get("pre")));
		double allNumGift = mapNum.get("num");
		double allPriceAmount = Math.abs(Double.parseDouble(mapPriceDis.get("pre")));
		double allPriceAmount1 = Math.abs(Double.parseDouble(mapPriceJM.get("pre")));
		double allPriceGift = mapNum6.get("num");
		Order3PromotionInfo info = new Order3PromotionInfo();
//		JLog.d("aaa", allNumAmount+"   "+allNumAmount1+"   "+allPriceAmount+"   "+allPriceAmount1);
		double[] a = {allNumAmount,allNumAmount1,allPriceAmount,allPriceAmount1};
		double allM = 0;
		double s = 0;
		for(int i = 0; i<4;i++){
			allM = Math.max(allM, a[i]);
		}
		if(allM!=0){
			if(allM==allNumAmount){
				info.setTitle(mapNumDis.get("name"));
				info.setDetails("减免"+PublicUtils.formatDouble(allNumAmount)+"元");
				info.setPromotionId((int) Double.parseDouble(mapNumDis.get("proid")));
			}else if(allM==allNumAmount1){
				info.setTitle(mapNumJM.get("name"));
				info.setDetails("减免"+PublicUtils.formatDouble(allNumAmount1)+"元");
				info.setPromotionId((int) Double.parseDouble(mapNumJM.get("proid")));
			}else if(allM==allPriceAmount){
				info.setTitle(mapPriceDis.get("name"));
				info.setDetails("减免"+PublicUtils.formatDouble(allPriceAmount)+"元");
				info.setPromotionId((int) Double.parseDouble(mapPriceDis.get("proid")));
			}else if(allM==allPriceAmount1){
				info.setTitle(mapPriceJM.get("name"));
				info.setDetails("减免"+PublicUtils.formatDouble(allPriceAmount1)+"元");
				info.setPromotionId((int) Double.parseDouble(mapPriceJM.get("proid")));
			}
			info.setActualAmount(allM);
			info.setOrderPrice(allM);
		}else if(allNumGift>=allPriceGift&&allNumGift!=0){
			int level1 = SharedPreferencesForOrder3Util.getInstance(ShoppingCarActivity.this).getOrder3StoreLevel();
			List<Order3Promotion> pos = promotionDB.findPromotionByPreType(SharedPreferencesForOrder3Util.getInstance(ShoppingCarActivity.this).getOrder3OrgId(), 5,1,level1);
			Double b = mapNum.get("index");
			int v = b.intValue();
			info.setTitle(pos.get(v).getName());
			info.setPromotionId(mapNum.get("proid").intValue());
			Order3Product product = order3Util.product(Integer.parseInt(!TextUtils.isEmpty(pos.get(v).getsId())?pos.get(v).getsId():"0"),Integer.parseInt(!TextUtils.isEmpty(pos.get(v).getsUid())?pos.get(v).getsUid():"0"));
			if(product!=null){
				info.setDetails("赠送"+product.getName()+" "+PublicUtils.formatDouble(allNumGift)+" "+product.getUnit());
				info.setGiftId(product.getProductId());
				info.setGiftUnit(product.getUnitId());
			}
			info.setGiftNum(allNumGift);
			info.setActualAmount(0);
			info.setOrderPrice(0);
		}else if(allNumGift<=allPriceGift&&allPriceGift!=0){
			int level1 = SharedPreferencesForOrder3Util.getInstance(ShoppingCarActivity.this).getOrder3StoreLevel();
			List<Order3Promotion> pos = promotionDB.findPromotionByPreType(SharedPreferencesForOrder3Util.getInstance(ShoppingCarActivity.this).getOrder3OrgId(), 5,1,level1);
			Double b = mapNum6.get("index");
			int v = b.intValue();
			info.setTitle(pos.get(v).getName());
			info.setPromotionId(mapNum6.get("proid").intValue());
			Order3Product product = order3Util.product(Integer.parseInt(pos.get(v).getsId()),Integer.parseInt(pos.get(v).getsUid()));
			if(product!=null){
				info.setDetails("赠送"+product.getName()+" "+PublicUtils.formatDouble(allNumGift)+" "+product.getUnit());
				info.setGiftId(product.getProductId());
				info.setGiftUnit(product.getUnitId());
			}
			info.setGiftNum(allNumGift);
			info.setActualAmount(0);
			info.setOrderPrice(0);
		}
//		info.setOrderPrice(shoppingCarFragment.allMoney());
		if(!TextUtils.isEmpty(info.getTitle())){
			infos.add(info);
		}
	}
	/**
	 * 单品赠品折扣信息
	 */
	private void danPinInfo() {
		if(order3Shoppingcars.size()>0){
			for(int i = 0; i<order3Shoppingcars.size();i++){
				Order3ShoppingCart cart = order3Shoppingcars.get(i);
				String ids = cart.getPromotionIds();
				if(cart.getPitcOn()==1 && !TextUtils.isEmpty(ids)&&!ids.equals("0")){

					
					Order3Promotion pro = promotionDB.findPromotionByPromotionId(Integer.parseInt(ids));
					if(pro!=null){
						if(pro.getPreType() == 1){//买满数量
							if(cart.getNumber()>=pro.getmCnt()){
								Order3PromotionInfo info = new Order3PromotionInfo();
								info.setTitle(pro.getName());
								if(pro.getDisType()==3){//打折
									info.setDetails("减免"+PublicUtils.formatDouble(cart.getPrePrice())+"元");
									info.setActualAmount(cart.getPrePrice());//减免值
									info.setOrderPrice(cart.getDiscountPrice());//实际价格
								}else if(pro.getDisType()==1){
									Order3Product p  = order3Util.product(cart.getGiftId(), cart.getGiftUnitId());
									info.setDetails("赠送"+p.getName()+" "+PublicUtils.formatDouble(cart.getDisNumber())+" "+p.getUnit());
									info.setGiftId(cart.getGiftId());
									info.setGiftUnit(cart.getGiftUnitId());
									info.setGiftNum(cart.getDisNumber());
								}else if(pro.getDisType()==2){
									info.setDetails("减免"+PublicUtils.formatDouble(cart.getPreAmount())+"元");
									info.setActualAmount(cart.getPreAmount());//减免值
									info.setOrderPrice(cart.getDisAmount());//实际价格
								}
								info.setPromotionId(pro.getPromotionId());
								info.setProductId(cart.getProductId());
								info.setUnitId(cart.getUnitId());
								infos.add(info);
							}
							
						}else if(pro.getPreType() == 2){//买满金额
							if(cart.getSubtotal()>=pro.getAmount()){
								Order3PromotionInfo info = new Order3PromotionInfo();
								info.setTitle(pro.getName());
								if(pro.getDisType()==3){//打折
									info.setDetails("减免"+PublicUtils.formatDouble(cart.getPrePrice())+"元");
									info.setActualAmount(cart.getPrePrice());//减免值
									info.setOrderPrice(cart.getDiscountPrice());//实际价格
								}else if(pro.getDisType()==1){//赠送产品
									Order3Product p  = order3Util.product(cart.getGiftId(), cart.getGiftUnitId());
									info.setDetails("赠送"+p.getName()+" "+PublicUtils.formatDouble(cart.getDisNumber())+" "+p.getUnit());
									info.setGiftId(cart.getGiftId());
									info.setGiftUnit(cart.getGiftUnitId());
									info.setGiftNum(cart.getDisNumber());
									
								}else if(pro.getDisType()==2){//减免金额
									info.setDetails("减免"+PublicUtils.formatDouble(cart.getPreAmount())+"元");
									info.setActualAmount(cart.getPreAmount());//减免值
									info.setOrderPrice(cart.getDisAmount());//实际价格
								}
								info.setPromotionId(pro.getPromotionId());
								info.setProductId(cart.getProductId());
								info.setUnitId(cart.getUnitId());
								infos.add(info);
							}
							
						}
						
					}
				}
			}
		}
		
	}

	/**
	 * 总和买满数量送打折
	 * @return
	 */
	public Map<String,String> zongHeFullNumDis(){
//		double finalNum = 0;
		double prePriPrice = 0;
		double allPriceAmount = 0;
		String name = null;
		double pid = 0;
		Map<String,String> map = new HashMap<String,String>();
		int level = SharedPreferencesForOrder3Util.getInstance(ShoppingCarActivity.this).getOrder3StoreLevel();
		List<Order3Promotion> pos5 = promotionDB.findPromotionByPreType(SharedPreferencesForOrder3Util.getInstance(ShoppingCarActivity.this).getOrder3OrgId(), 5,3,level);
		if(pos5.size()>0){
			
			for(int i = 0; i<pos5.size(); i++){
				if(shoppingCarFragment.allNum()>=pos5.get(i).getmCnt()){
					double a = shoppingCarFragment.prePrice(pos5.get(i));
					if(prePriPrice<a){
						prePriPrice = a;
						allPriceAmount = shoppingCarFragment.discountZonghe(pos5.get(i));
						pid = pos5.get(i).getPromotionId();
						name = pos5.get(i).getName();
					}
				}
			}
		}
		map.put("name", name);
		map.put("price", String.valueOf(allPriceAmount));
		map.put("pre", String.valueOf(prePriPrice));
		map.put("proid", String.valueOf(pid));
		return map;
	}
	/**
	 * 总和买满数量送减免金额
	 * @return
	 */
	public Map<String,String> zongHeFullNumJM(){
		double prePriPrice = 0;
		double allPriceAmount = 0;
		String name = null;
		double pid = 0;
		Map<String,String> map = new HashMap<String,String>();
		int level = SharedPreferencesForOrder3Util.getInstance(ShoppingCarActivity.this).getOrder3StoreLevel();
		List<Order3Promotion> pos5 = promotionDB.findPromotionByPreType(SharedPreferencesForOrder3Util.getInstance(ShoppingCarActivity.this).getOrder3OrgId(), 5,2,level);
		if(pos5.size()>0){
			
			for(int i = 0; i<pos5.size(); i++){
				if(shoppingCarFragment.allNum()>=pos5.get(i).getmCnt()){
					int b = (int) (shoppingCarFragment.allNum()/pos5.get(i).getmCnt());
					double a = shoppingCarFragment.preAmount(pos5.get(i),b);
					if(prePriPrice<a){
						prePriPrice = a;
						allPriceAmount = shoppingCarFragment.subMoneyZonHe(pos5.get(i));
						name = pos5.get(i).getName();
						pid = pos5.get(i).getPromotionId();
					}
				}
			}
		}
		map.put("name", name);
		map.put("price", String.valueOf(allPriceAmount));
		map.put("pre", String.valueOf(prePriPrice));
		map.put("proid", String.valueOf(pid));
		return map;
	}
	/**
	 * 总和买满数量送赠品
	 * @return
	 */
//	List<>
	public Map<String,Double> zongHeFullNum5(){
		Map<String,Double> map = new HashMap();
		double finalNum = 0;
		double pid = 0;
		double b = 0;
		int level = SharedPreferencesForOrder3Util.getInstance(ShoppingCarActivity.this).getOrder3StoreLevel();
		List<Order3Promotion> pos5 = promotionDB.findPromotionByPreType(SharedPreferencesForOrder3Util.getInstance(ShoppingCarActivity.this).getOrder3OrgId(), 5,1,level);
		if(pos5.size()>0){
			
			for(int i = 0; i<pos5.size(); i++){
				double moneyAll = shoppingCarFragment.allNum();
				if(moneyAll>=pos5.get(i).getmCnt()){
					int bei = (int) (moneyAll/pos5.get(i).getmCnt());
					double a = shoppingCarFragment.zengpin(pos5.get(i),bei);
					if(finalNum<a){
						finalNum = a;
						b = i;
						pid = pos5.get(i).getPromotionId();
					}
				}
			}
			
		}
		map.put("num", finalNum);
		map.put("index", b);
		map.put("proid", pid);
		return map;
	}
	
	/**
	 * 总和买满金额送打折
	 * @return
	 */
	public Map<String,String> zongHeFullNumDis6(){
//		double finalNum = 0;
		double prePriPrice = 0;
		double allPriceAmount = 0;
		double pid = 0;
		String name = null;
		Map<String,String> map = new HashMap<String,String>();
		int level = SharedPreferencesForOrder3Util.getInstance(ShoppingCarActivity.this).getOrder3StoreLevel();
		List<Order3Promotion> pos5 = promotionDB.findPromotionByPreType(SharedPreferencesForOrder3Util.getInstance(ShoppingCarActivity.this).getOrder3OrgId(), 6,3,level);
		if(pos5.size()>0){
			
			for(int i = 0; i<pos5.size(); i++){
				if(shoppingCarFragment.allMoney()>=pos5.get(i).getAmount()){
					double a = shoppingCarFragment.prePrice(pos5.get(i));
					if(prePriPrice<a){
						prePriPrice = a;
						allPriceAmount = shoppingCarFragment.discountZonghe(pos5.get(i));
						pid = pos5.get(i).getPromotionId();
						name = pos5.get(i).getName();
					}
				}
			}
		}
		map.put("name", name);
		map.put("price", String.valueOf(allPriceAmount));
		map.put("pre", String.valueOf(prePriPrice));
		map.put("proid", String.valueOf(pid));
		return map;
	}
	/**
	 * 总和买满金额送减免金额
	 * @return
	 */
	public Map<String,String> zongHeFullNumJM6(){
		double prePriPrice = 0;
		double allPriceAmount = 0;
		String name = null;
		double pid = 0;
		Map<String,String> map = new HashMap<String,String>();
		int level = SharedPreferencesForOrder3Util.getInstance(ShoppingCarActivity.this).getOrder3StoreLevel();
		List<Order3Promotion> pos5 = promotionDB.findPromotionByPreType(SharedPreferencesForOrder3Util.getInstance(ShoppingCarActivity.this).getOrder3OrgId(), 6,2,level);
		if(pos5.size()>0){
			
			for(int i = 0; i<pos5.size(); i++){
				if(shoppingCarFragment.allMoney()>=pos5.get(i).getAmount()){
					int b = (int) (shoppingCarFragment.allMoney()/pos5.get(i).getAmount());
					double a = shoppingCarFragment.preAmount(pos5.get(i),b);
					if(prePriPrice<a){
						prePriPrice = a;
						allPriceAmount = shoppingCarFragment.subMoneyZonHe(pos5.get(i));
						name = pos5.get(i).getName();
						pid = pos5.get(i).getPromotionId();
					}
				}
			}
		}
		map.put("name", name);
		map.put("price", String.valueOf(allPriceAmount));
		map.put("pre", String.valueOf(prePriPrice));
		map.put("proid", String.valueOf(pid));
//		JLog.d("aaa", "  name  "+name+"  price  "+allPriceAmount+"  pre  "+prePriPrice);
		return map;
	}
	/**
	 * 总和买满金额送赠品
	 * @return
	 */
	public Map<String,Double> zongHeFullNum6(){
		Map<String,Double> map = new HashMap();
		double finalNum = 0;
		double b = 0;
		double pid = 0;
		int level = SharedPreferencesForOrder3Util.getInstance(ShoppingCarActivity.this).getOrder3StoreLevel();
		List<Order3Promotion> pos5 = promotionDB.findPromotionByPreType(SharedPreferencesForOrder3Util.getInstance(ShoppingCarActivity.this).getOrder3OrgId(), 6,1,level);
		if(pos5.size()>0){
			for(int i = 0; i<pos5.size(); i++){
				double moneyAll = shoppingCarFragment.allMoney();
				if(moneyAll>=pos5.get(i).getAmount()){
					int c = (int) (moneyAll/pos5.get(i).getAmount());
					double a = shoppingCarFragment.zengpin(pos5.get(i),c);
					if(finalNum<a){
						finalNum = a;
						b = i;
						pid = pos5.get(i).getPromotionId();
					}
				}
			}
		}
		map.put("num", finalNum);
		map.put("index", b);
		map.put("proid", pid);
		return map;
	}
	
	public List<Order3PromotionInfo> getProInfo(){
		initData();
		return infos;
	}

	private void setChioceItem(int i) {
		// 重置选项+隐藏所有Fragment
		FragmentTransaction transaction = fManager.beginTransaction();
		clearChioce();
		hideFragments(transaction);
		switch (i) {
		case 0:
			if (shoppingCarFragment == null) {
				// 如果fg1为空，则创建一个并添加到界面上
				shoppingCarFragment = new ShoppingCarFragment();
				transaction.add(R.id.framelayout, shoppingCarFragment);
			} else {
				// 如果MessageFragment不为空，则直接将它显示出来
				transaction.show(shoppingCarFragment);
			}
			step = 0;
			transaction.commit();
			break;
		case 1:
			if (shouKuanFragment == null) {
				shouKuanFragment = new ShouKuanFragment();
				transaction.add(R.id.framelayout, shouKuanFragment);
			} else {
				transaction.show(shouKuanFragment);
			}
//			ll_location_bar.setVisibility(View.GONE);
			step = 1;
			transaction.commit();
			initData();
//			layout_discount.setBackgroundResource(R.color.order3_locationbar_pressed);
			break;
		case 2:		
			if (zenpinFragment == null) {
				zenpinFragment = new ZengpinFragment();
				transaction.add(R.id.framelayout, zenpinFragment);
			} else {
				zenpinFragment.initData();
				transaction.show(zenpinFragment);
			}
//			ll_location_bar.setVisibility(View.GONE);
			step = 2;
			transaction.commit();
			initData();
//			layout_collection.setBackgroundResource(R.color.order3_locationbar_pressed);
			break;
		case 3:
			if (yuLanFragment == null) {
				yuLanFragment = new YulanFragment();
				transaction.add(R.id.framelayout, yuLanFragment);
			} else {
				transaction.show(yuLanFragment);
				yuLanFragment.initData();
			}
			ll_location_bar.setVisibility(View.GONE);
			ll_yulan_btns.setVisibility(View.VISIBLE);
			step = 3;
			transaction.commit();
			initData();
			break;
		}
	}

	private void hideFragments(FragmentTransaction transaction) {
		if (shoppingCarFragment != null) {
			transaction.hide(shoppingCarFragment);
		}
		if (shouKuanFragment != null) {
			transaction.hide(shouKuanFragment);
		}
		if (zenpinFragment != null) {
			transaction.hide(zenpinFragment);
		}
		if (yuLanFragment != null) {
			transaction.hide(yuLanFragment);
		}

	}

	private void clearChioce() {
//		layout_discount.setBackgroundResource(R.color.order3_locationbar_bg);
//		layout_collection.setBackgroundResource(R.color.order3_locationbar_bg);
	}

	private TakePhotoPopupWindow t;
	private void showPopuWindow() {
		t = new TakePhotoPopupWindow(ShoppingCarActivity.this);
		t.show(null,SharedPreferencesForOrder3Util.getInstance(ShoppingCarActivity.this).getPhotoNameOne(),SharedPreferencesForOrder3Util.getInstance(ShoppingCarActivity.this).getPhotoNameTwo());
	}

	public String getString(EditText s) {
		return s.getText().toString();
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
			if (t!=null) {
				t.onActivityResult(requestCode, resultCode, data);
			}
	}

	private int isPromotion;
	private Order3 order3;
	
	public Order3 getOrder3(Date date){
		order3 = new Order3();
		order3.setStoreId(storeId);
		order3.setStoreName(SharedPreferencesForOrder3Util.getInstance(ShoppingCarActivity.this).getOrder3StoreName());
		order3.setOrderNo(order3Util.orderNumber(true));
		if(null==date){
			order3.setOrderTime(DateUtil.getCurDateTime());
		}else{
			order3.setOrderTime(DateUtil.dateToDateString(date));
		}
		
		order3.setImage1(SharedPreferencesForOrder3Util.getInstance(this).getPhotoNameOne());
		order3.setImage2(SharedPreferencesForOrder3Util.getInstance(this).getPhotoNameTwo());
		order3.setPrintCount(SharedPreferencesForOrder3Util.getInstance(this).getOrderPrintCount());
		order3.setSendPrintCount(SharedPreferencesForOrder3Util.getInstance(this).getOrderSendPrintCount());
		if(order3Contact==null){
			int contachId = SharedPreferencesForOrder3Util.getInstance(this).getOrder3ContactId();
			if (contachId!=0) {
				order3Contact = new Order3ContactsDB(this).findOrderContactsByContactsIdAndStoreId(contachId, storeId);
			}
		}
		if (order3Contact!=null) {
			order3.setContact(order3Contact);
			order3.setContactId(order3Contact.getOrderContactsId());
		}
		List<Order3ProductData> order3Pros = new ArrayList<Order3ProductData>();
		if(order3Shoppingcars.size()>0){//购物车  主产品
			for(int i = 0; i<order3Shoppingcars.size(); i++){
				Order3ShoppingCart c = order3Shoppingcars.get(i);
				if(c.getPitcOn()==1){
					Order3Product p = order3Util.product(c.getProductId(), c.getUnitId());
					if(p!=null){
						Order3ProductData pda = new Order3ProductData();
						pda.setProductId(c.getProductId());
						pda.setOrderCount(c.getNumber());
						pda.setActualCount(c.getNumber());
						pda.setProductUnit(p.getUnit());
						pda.setUnitId(c.getUnitId());
						pda.setOrderPrice(p.getPrice());
						pda.setOrderAmount(c.getSubtotal());
						pda.setProductName(p.getName());
						if(c.getDisAmount()!=0&&c.getDisAmount()!=c.getSubtotal()){
							pda.setActualAmount(c.getDisAmount());
						}else if(c.getDiscountPrice()!=0&&c.getDiscountPrice()!=c.getSubtotal()){
							pda.setActualAmount(c.getDiscountPrice());
						}else{
							pda.setActualAmount(c.getSubtotal());
						}
						pda.setIsOrderMain(1);
						pda.setPromotionId(Integer.parseInt(c.getPromotionIds()));
						if(!TextUtils.isEmpty(c.getPromotionIds())&&(!c.getPromotionIds().equals("0"))){
							isPromotion = 1;
						}
						order3Pros.add(pda);
					}
					
				}
			}
		}
		
		if(infos.size()>0){//赠品折扣信息
			for(int i = 0; i<infos.size();i++){
				Order3PromotionInfo fo = infos.get(i);
				Order3Product gift = order3Util.product(fo.getGiftId(), fo.getGiftUnit());
				Order3ProductData pda = new Order3ProductData();
				if(gift!=null){//赠品信息
					pda.setProductId(fo.getGiftId());
					pda.setOrderCount(fo.getGiftNum());
					pda.setActualCount(fo.getGiftNum());
					pda.setProductUnit(gift.getUnit());
					pda.setUnitId(fo.getGiftUnit());
					pda.setMainProductId(fo.getProductId());
					pda.setPromotionId(fo.getPromotionId());
					pda.setProductName(gift.getName());
					pda.setIsOrderMain(2);
					order3Pros.add(pda);
				}else if(fo.getProductId()==0){//总和打折减免信息
//					double a = fo.getOrderPrice()-fo.getActualAmount();
					pda.setActualAmount(fo.getActualAmount());
					pda.setOrderAmount(fo.getActualAmount());
					pda.setPromotionId(fo.getPromotionId());
					pda.setIsOrderMain(2);
					order3Pros.add(pda);
				}else{//单品折扣减免信息
					Order3Product duct = order3Util.product(fo.getProductId(), fo.getUnitId());
					if(duct!=null){
						pda.setProductId(duct.getProductId());
//						pda.setOrderCount(c.getNumber());
//						pda.setActualCount(c.getNumber());
						pda.setProductUnit(duct.getUnit());
						pda.setUnitId(duct.getUnitId());
						pda.setOrderPrice(duct.getPrice());//商品单价
//						pda.setOrderAmount(fo.getOrderPrice());//实际价格
						pda.setOrderAmount(fo.getActualAmount());//实际价格 改成减免值
						pda.setActualAmount(fo.getActualAmount());//减免值
						pda.setProductName(duct.getName());
						pda.setIsOrderMain(2);
						pda.setPromotionId(fo.getPromotionId());
						
						order3Pros.add(pda);
					}
				}
				if(fo.getPromotionId()!=0){
					isPromotion =1;
				}
			}
		}
		order3.setIsPromotion(isPromotion);
		if(order3Pros.size()>0){
			order3.setProductList(order3Pros);
		}
		String shouKuan = SharedPreferencesForOrder3Util.getInstance(this).getShouKuan();
		order3.setPayAmount(Double.parseDouble(shouKuan));//预收款
		String jianMian = SharedPreferencesForOrder3Util.getInstance(this).getJianMian();
		order3.setOrderDiscount(Double.parseDouble(jianMian));//特别折扣
		String msg = SharedPreferencesForOrder3Util.getInstance(this).getLeaveMessage();
		order3.setNote(msg);
		order3.setOrderAmount(getToPri());
		order3.setActualAmount(getActulAmount()-order3.getOrderDiscount());
		order3.setUnPayAmount((order3.getActualAmount()-order3.getPayAmount()));
		return order3;
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
			if(step != 0){
				initFragments();
				ll_location_bar.setVisibility(View.VISIBLE);
				ll_yulan_btns.setVisibility(View.GONE);
				return true;
			}else{
				finish();
			}
			
		}
		return super.onKeyDown(keyCode, event);
	}

	public double getToPri(){//原价总价
		return shoppingCarFragment == null ? 0:shoppingCarFragment.getTotalPrice();
	}
	public double getActulAmount(){//现价 折后
		return shoppingCarFragment == null ? 0:shoppingCarFragment.getActulAmo();
	}
	
	/**
	 * 提交订单数据
	 * @param params
	 */
	private void submitDataBackground(HashMap<String, String> params){
		PendingRequestVO vo = new PendingRequestVO();
		vo.setContent("订单号:"+order3.getOrderNo());
		vo.setTitle("订单数据");
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
		vo.setContent("订单号:"+order3.getOrderNo());
		vo.setTitle("订单图片");
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
	
	/**
	 * 打印订单
	 */
	private Order3PrintForCreate orderPrint;
	private void print() {
		FileHelper help = new FileHelper();
		String json = help.readJsonString(ShoppingCarActivity.this, "order3_print_create.txt");
		try {
			orderPrint = new Order3PrintForCreate(this);
			Order3 order = getOrder3(null);
			orderPrint.setOrder(order);
			orderPrint.setOrder3PromotionInfoList(infos);
			orderPrint.print(ShoppingCarActivity.this, new JSONArray(json), ShoppingCarActivity.this);
			int printCount = SharedPreferencesForOrder3Util.getInstance(this).getOrderPrintCount();
			SharedPreferencesForOrder3Util.getInstance(this).setOrderPrintCount(printCount+1);
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
	public void printZhekou(List<Element> columns) {
		
	}

	@Override
	public void printDingdanbianhao(List<Element> columns) {
		
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (orderPrint!=null) {
			orderPrint.release();
		}
	}

	@Override
	public void submitReceive(boolean isSuccess) {
		
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt("step", step);
		outState.putInt("isPromotion", isPromotion);
		outState.putBoolean("isSubmit", isSubmit);
		outState.putString("storeId", storeId);
		outState.putStringArrayList("photoList", (ArrayList<String>) photoList);
		super.onSaveInstanceState(outState);
	}
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		step = savedInstanceState.getInt("step");
		isPromotion = savedInstanceState.getInt("isPromotion");
		storeId = savedInstanceState.getString("storeId");
		isSubmit = savedInstanceState.getBoolean("isSubmit", false);
		photoList = savedInstanceState.getStringArrayList("photoList");
	}
	
}
