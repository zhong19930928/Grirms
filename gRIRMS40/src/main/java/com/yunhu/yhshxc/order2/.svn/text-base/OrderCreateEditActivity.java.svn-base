package com.yunhu.yhshxc.order2;

import gcg.org.debug.JLog;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.database.DictDB;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.order.bo.ProductConf;
import com.yunhu.yhshxc.order.listener.OnProductChoosedListener;
import com.yunhu.yhshxc.order2.bo.OrderDetail;
import com.yunhu.yhshxc.order2.view.ProductLayoutForOrder2;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.google.zxing.activity.CaptureActivity;
import com.loopj.android.http.RequestParams;

public class OrderCreateEditActivity extends AbsBaseActivity {
	private final String TAG = "OrderCreateEditActivity";
	private LinearLayout scan,save;
	private String scanCode;
	private LinearLayout ll_order_create_price;//价格
	private EditText et_order_create_price;//价格
	private EditText et_order_create_quantity;//数量
	private EditText et_order_create_remark;//备注
	private OrderDetail orderDetail=null;//当前要添加或者要修改的订单
	private String updateIndex;//修改订单的下标
	private String updateDetailJson;//修改订单的时候要修改的订单详细
	private Spinner sp_order_create_unit;//单位
	private LinearLayout ll_order_create_unit;
	private LinearLayout ll_order_product;
	private TextView tv_order_create_price_title;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_add_activity);
		tv_order_create_price_title = (TextView)findViewById(R.id.tv_order_create_price_title);
		updateIndex = getIntent().getStringExtra("updateIndex");
		updateDetailJson = getIntent().getStringExtra("updateDetailJson");
		scan = (LinearLayout)findViewById(R.id.ll_order2_shape_code);
		scan.setOnClickListener(listener);
		save = (LinearLayout)findViewById(R.id.ll_order2_add_save);
		save.setOnClickListener(listener);
		ll_order_create_price = (LinearLayout)findViewById(R.id.ll_order_create_price);
		et_order_create_price = (EditText)findViewById(R.id.et_order_create_price);
		et_order_create_price.addTextChangedListener(textWatcher);
		et_order_create_quantity = (EditText)findViewById(R.id.et_order_create_quantity);
		et_order_create_quantity.addTextChangedListener(textWatcher);
		et_order_create_remark = (EditText)findViewById(R.id.et_order_create_remark);
		sp_order_create_unit = (Spinner)findViewById(R.id.et_order_create_unit);
		ll_order_create_unit = (LinearLayout)findViewById(R.id.ll_order_create_unit);
		ll_order_product  = (LinearLayout)findViewById(R.id.ll_order_product);
		initData();
	}
	
	private ProductLayoutForOrder2 productLayoutForOrder2 = null;
	private void initData(){
		productLayoutForOrder2 = new ProductLayoutForOrder2(this);
		productLayoutForOrder2.setOnProductChoosedListener(productChoosedListener);
		ll_order_product.addView(productLayoutForOrder2);
		initOrderDetail();
		priceControl();
		unitControl();
		
	}
	
	/**
	 * 控制输入框小数点后只能输入两位
	 */
	private TextWatcher textWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}
		
		@Override
		public void afterTextChanged(Editable s) {
			String temp = s.toString();
			int posDot = temp.indexOf(".");
			if (posDot <= 0) {
				return;
			}
			if (temp.length() - posDot - 1 > 2) {
				s.delete(posDot+3, posDot+4);
			}
		}
	};
	
	/**
	 * 初始化订单明细
	 */
	private void initOrderDetail(){
		try {
			if (!TextUtils.isEmpty("updateIndex") && !TextUtils.isEmpty(updateDetailJson)) {
				orderDetail = Order2Data.json2OrderDetail(new JSONObject(updateDetailJson));
				et_order_create_price.setText(String.valueOf(orderDetail.getPrice()));
				et_order_create_quantity.setText(String.valueOf(orderDetail.getQuantity()));
				et_order_create_remark.setText(String.valueOf(orderDetail.getRemark()));
				Dictionary product = orderDetail.getProduct();
				if (product!=null) {
					productLayoutForOrder2.setProductId(product.getDid());
				}
			}else{
				orderDetail = new OrderDetail();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * 选择产品监听
	 */
	private OnProductChoosedListener productChoosedListener = new OnProductChoosedListener() {
		
		@Override
		public void onOptionsItemChoosed(Dictionary obj) {
			if (obj!=null) {
				orderDetail.setProduct(obj);
				unitList = OrderDetail.getUnit(obj.getDid(), OrderCreateEditActivity.this);
				unitControl();
			}else{
				cleanUnitControlAndPrice();
			}
		}
	};
	
	/**
	 * 清空单位和价格
	 */
	private void cleanUnitControlAndPrice(){
		unitList = new ArrayList<Dictionary>();
		sp_order_create_unit.setAdapter(bindData(unitList));
		et_order_create_price.setText("");
	}
	
	/**
	 * 初始化产品单位
	 */
	private List<Dictionary> unitList;
	private void unitControl(){
		ProductConf unit_ctrl = SharedPreferencesForOrder2Util.getInstance(this).getUnitCtrl();
		if (unit_ctrl !=null) {
			if (unitList!=null) {
				sp_order_create_unit.setAdapter(bindData(unitList));
				sp_order_create_unit.setOnItemSelectedListener(unitItemSelectListener);
				Dictionary unitDictionary = orderDetail.getProductUnit();
				if (unitDictionary!=null) {
					String unitDid = unitDictionary.getDid();
					for (int i = 0; i < unitList.size(); i++) {
						String did = unitList.get(i).getDid();
						if (unitDid.equals(did)) {
							sp_order_create_unit.setSelection(i);
							break;
						}
					}
				}
			}
		}else{
			ll_order_create_unit.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 
	 * @return绑定adapter数据源
	 */
	private String[] getDateSrc(){
		if (unitList == null) return null;
		String[] contcol = new String[unitList.size()];
		int i = 0;
		for (Dictionary dict : unitList) {
			contcol[i] = dict.getCtrlCol()==null?"":dict.getCtrlCol();//获取资源列所有数据
			i++;
		}
		return contcol;
	}
	
	/**
	 * 单位选择监听
	 */
	private OnItemSelectedListener unitItemSelectListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if (unitList!=null && unitList.size()>0) {
				Dictionary dic = unitList.get(arg2);
				orderDetail.setProductUnit(dic);
				String price_ctrl = SharedPreferencesForOrder2Util.getInstance(OrderCreateEditActivity.this).getPriceCtrl();
				String[] str = price_ctrl.split("\\|");
				String table = "t_m_"+str[0];
				String dictDataId = "data_"+str[1];
				Dictionary priceDic = new DictDB(OrderCreateEditActivity.this).findDictByDid(table, dictDataId, dic.getDid());
				if (priceDic!=null) {
					String price = priceDic.getCtrlCol();
					et_order_create_price.setText(price);
				}
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			
		}
	};
	
	/**
	 * @return绑定数据使用的Adapter
	 */
	
	private ArrayAdapter<String> bindData(List<Dictionary> unitList) {
		String[] str=getDateSrc();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item);
		if(str!=null && str.length>0){
			for (int i = 0; i < str.length; i++) {
				adapter.add(str[i]);
			}
		}else{
			adapter.add("--请选择--");
		}
		adapter.setDropDownViewResource(R.layout.sprinner_check_item2);
		return adapter;
	}
	
	/**
	 * 控制价格
	 * 1、显示、不可修改；2、显示、可修改；3、不显示；默认：2
	 */
	private void priceControl(){
		int is_phone_price = SharedPreferencesForOrder2Util.getInstance(this).getIsPhonePrice();
		switch (is_phone_price) {
		case 1:
			ll_order_create_price.setVisibility(View.VISIBLE);
			et_order_create_price.setEnabled(false);
			break;
		case 2:
			ll_order_create_price.setVisibility(View.VISIBLE);
			et_order_create_price.setEnabled(true);
			break;
		case 3:
			ll_order_create_price.setVisibility(View.GONE);
			break;
		}
		String prive_ctrl = SharedPreferencesForOrder2Util.getInstance(this).getPriceCtrl();
		if (!TextUtils.isEmpty(prive_ctrl)) {
			String[] str = prive_ctrl.split("\\|");
			if (str.length>0 && str[str.length-1]!=null) {
				tv_order_create_price_title.setText(str[str.length-1]);
			}
		}
	}
	
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ll_order2_shape_code:
				scan();
				break;
			case R.id.ll_order2_add_save:
				save();
				break;

			default:
				break;
			}
		}
	};
	
	
	/**
	 * 保存订单详细
	 */
	private void save(){
		try {
			if (orderDetail.getProduct()==null) {
				ToastOrder.makeText(this, "请选择产品", ToastOrder.LENGTH_SHORT).show();
				return;
			}
			String priceStr = et_order_create_price.getText().toString().trim();
			double price = Double.parseDouble(TextUtils.isEmpty(priceStr)?"0":priceStr);
			if (price<0) {
				ToastOrder.makeText(this, "请输入价格", ToastOrder.LENGTH_SHORT).show();
				return;
			}
			orderDetail.setPrice(price);
			String quantityStr = et_order_create_quantity.getText().toString().trim();
			double quantity = Double.parseDouble(TextUtils.isEmpty(quantityStr)?"0":quantityStr);
			if (quantity<=0) {
				ToastOrder.makeText(this, "请输入数量", ToastOrder.LENGTH_SHORT).show();
				return;
			}
			orderDetail.setQuantity(quantity);
			String remark = et_order_create_remark.getText().toString().trim();
			orderDetail.setRemark(remark);
			orderDetail.setAmount(price*quantity);
			Intent intent = new Intent();
			intent.putExtra("updateIndex", updateIndex);
			intent.putExtra("updateDetailJson", Order2Data.orderDetail2Json(orderDetail));
			this.setResult(100, intent);
			this.finish();
			
		} catch (Exception e) {
			e.printStackTrace();
			ToastOrder.makeText(this, R.string.ERROR_DATA, ToastOrder.LENGTH_SHORT).show();
		}
	}
	/**
	 * 扫码
	 */
	private void scan(){
		Intent i = new Intent(this, CaptureActivity.class);
		startActivityForResult(i, 100);
	}
	
	/**
	 * 拍照 扫描返回
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == R.id.scan_succeeded && requestCode == 100) {
			scanCode = data.getStringExtra("SCAN_RESULT");
			scanForResult(scanCode);
		}
	}
	
	/**
	 * 扫码成功返回
	 */
	private Dialog scanfDialog = null;
	private void scanForResult(String code){
		if (!TextUtils.isEmpty(code)) {
			try {
				scanfDialog =  new MyProgressDialog(this,R.style.CustomProgressDialog,"正在查询...");
				String url = UrlInfo.queryOrderProductInfo(this);
				RequestParams requestParams = new RequestParams();
				requestParams.put("phoneno", PublicUtils.receivePhoneNO(this));
				requestParams.put("code", code);
				
				GcgHttpClient.getInstance(this).post(url, requestParams,new HttpResponseListener() {
					@Override
					public void onStart() {
						scanfDialog.show();
					}


					@Override
					public void onFailure(Throwable error, String content) {
						JLog.d(TAG, content+"");
						ToastOrder.makeText(OrderCreateEditActivity.this, "网络异常", ToastOrder.LENGTH_SHORT).show();
					}

					@Override
					public void onFinish() {
						scanfDialog.dismiss();
					}


					@Override
					public void onSuccess(int statusCode, String content) {
						JLog.d(TAG, content);
						try {
							JSONObject obj = new JSONObject(content);
							String resultcode = obj.getString("resultcode");
							if ("0000".equals(resultcode)) {
								if (obj.has("order_product_id") && !TextUtils.isEmpty(obj.getString("order_product_id"))) {
									String order_product_id = obj.getString("order_product_id");
									productLayoutForOrder2.setProductId(order_product_id);
								}else{
									ToastOrder.makeText(OrderCreateEditActivity.this, "该条形码不存在", ToastOrder.LENGTH_SHORT).show();
								}
							}else{
								ToastOrder.makeText(OrderCreateEditActivity.this, "条形码查询失败", ToastOrder.LENGTH_SHORT).show();
							}
						} catch (Exception e) {
							e.printStackTrace();
							ToastOrder.makeText(OrderCreateEditActivity.this, R.string.ERROR_DATA, ToastOrder.LENGTH_SHORT).show();
						}						
					}

				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			ToastOrder.makeText(this, "请选择客户", ToastOrder.LENGTH_SHORT).show();
		}
		
	
		
	}
	
}
