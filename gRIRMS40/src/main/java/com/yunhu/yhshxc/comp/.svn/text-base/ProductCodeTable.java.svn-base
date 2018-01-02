package com.yunhu.yhshxc.comp;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.Submit;
import com.yunhu.yhshxc.bo.SubmitItem;

import com.yunhu.yhshxc.database.SubmitItemDB;
import com.yunhu.yhshxc.database.SubmitItemTempDB;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.func.AbsFuncActivity;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.listener.ProductCodeListener;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.PhoneModelManager;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.PaintDrawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import gcg.org.debug.JLog;
/**
 * 表格控件的串码 codeControl 1:不显示不控制 2：显示不控制 3：显示控制 4：不显示控制
 * @author qingli
 *
 */
public class ProductCodeTable extends Component{
	private final String TAG = "ProductCode";
	public Context myContext;
	public static final int TABLE_TYPE_MODULE=1;
	public static final int TABLE_TYPE_VISIT=2;
	public static final int TABLE_TYPE_TABLE=3;
	public int table_type;
	public Func currentFunc;
	private PopupWindow  popupWindow;
	private View view=null;
	private CompDialog compDialog;
	private ProductCodeInTableComp productCodeInTableComp;
	private ProductCodeListener productCodeListener;
	private int tableRow;//表格中串码控件所在的行号

	public ProductCodeTable(Context context,Func func,CompDialog compDialog) {
		this.myContext = context;
		this.currentFunc = func;
		this.compDialog = compDialog;

	}

	/**
	 * 串码
	 */
	public void setProductTableType(int tableType) {
		this.table_type=tableType;
	}

	/**
	 * 串码dialog
	 */
	private EditText pCodeET = null;
	private void productCodeDialog(final Func func) {
		view = View.inflate(myContext, R.layout.comp_product_code_dialog,null);
		pCodeET = (EditText) view.findViewById(R.id.et_pCode);
//		value="1000001";
		pCodeET.setText(value);
		ImageButton scanBtn = (ImageButton) view.findViewById(R.id.btn_pCode_scanf);
//		scanBtn.setText(scanBtnName());
		scanBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				productScan(func);
			}
		});		
		pCodeET.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				value=s.toString();
				JLog.d(TAG, "value:"+value);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
	}
	
	private Dialog dialog = null;
	public void productCodeInTable(final ProductCodeInTableComp productCodeInTableComp,String code){
		this.productCodeInTableComp = productCodeInTableComp;
		this.sCode = code;
		View tableCodeview = View.inflate(myContext, R.layout.comp_dialog, null);
		LinearLayout linearLayout = (LinearLayout) tableCodeview.findViewById(R.id.ll_compDialog);
		Button confirmBtn = (Button) tableCodeview.findViewById(R.id.comp_dialog_confirmBtn);//保存按钮
		Button cancelBtn = (Button) tableCodeview.findViewById(R.id.comp_dialog_cancelBtn);//取消按钮
		dialog = new Dialog(myContext, R.style.transparentDialog);
		linearLayout.addView(getObject());
		confirmBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if (TextUtils.isEmpty(value)) {
					ToastOrder.makeText(myContext, myContext.getResources().getString(R.string.please_imput_chuancode), ToastOrder.LENGTH_SHORT).show();
				}else{
					if (isNeedNet()) {//联网
						getProductCodeInfo(currentFunc, value);
					}else{//不联网保存
						if (table_type == TABLE_TYPE_TABLE && productCodeInTableComp!=null) {
							productCodeInTableComp.setValue(value);
							if (isCodeUpdate() && "1".equals(currentFunc.getCodeControl())) {//后台处理的时候 表格中串码修改的时候如果没有修改的话要把查询得到的标识传给后台
								productCodeInTableComp.setCompareContent("");
							}
						}
					}
				}
			}
		});
		cancelBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		if (!TextUtils.isEmpty(code)) {
			setProductCode(code);
		}
		dialog.setContentView(tableCodeview);
		dialog.show();
		dialog.setCancelable(false);
	}
	
	
	public void showProductCodeDialog(boolean isShow){
		if (isShow) {
			if (dialog!=null && !dialog.isShowing()) {
				dialog.show();
			}
		}else{
			if (dialog!=null && dialog.isShowing()) {
				dialog.dismiss();
			}
		}
	}

	/**
	 * 是否需要联网
	 * @return
	 */
	public boolean isNeedNet(){
		boolean flag = true;
		String codeType = currentFunc.getCodeType();
		String codeControl = currentFunc.getCodeControl();
		if ("3".equals(codeType) && "1".equals(codeControl)) {//类型是只有3 并且control是1即不显示不控制则不需要联网
			flag = false;
		}
		return flag;
	}
	
	/**
	 * 串码扫描
	 */
	public void productScan(Func func) {
		
		//点击扫描的时候把dialog关闭 （处理扫描异常返回）
		if (table_type == TABLE_TYPE_TABLE && productCodeInTableComp!=null) {
			if (dialog!=null && dialog.isShowing()) {
				dialog.dismiss();
			}
		}else if (compDialog!=null) {
			compDialog.dialog.dismiss();
		}
		
//		Intent i = new Intent(myContext, CaptureActivity.class);
		Intent i = PhoneModelManager.getIntent(myContext,true);
		if (i!=null) {						
			((Activity) myContext).startActivityForResult(i, requestCode());
		}
	}
	
	public int  requestCode(){
		return 113;
	}

	/**
	 * 扫描串码返回
	 */
	public void setProductCode(String code) {
		if (!TextUtils.isEmpty(code)) {
			pCodeET.setText(code);
			value = code;
		}
	}

	/**
	 * 获取串码信息
	 */
	public void getProductCodeInfo(final Func func, String code) {
		if (!TextUtils.isEmpty(func.getCodeType())) {
			String url = UrlInfo.getUrlCodeInfo(myContext)+"?code="+code+"&code_type="+func.getCodeType()+
					"&table_id="+func.getTargetid()+"&table_type="+table_type+
					"&is_code_update="+func.getCodeUpdate()+"&compare_result="+func.getCodeControl();
		JLog.d(TAG, "获取串码信息URL：" + url);
		
		RequestParams params = new RequestParams();
		params.put("code", code);
		params.put("code_type", func.getCodeType());
		params.put("table_id", String.valueOf(func.getTargetid()));
		params.put("table_type", String.valueOf(table_type));
		params.put("is_code_update", func.getCodeUpdate());
		params.put("compare_result", func.getCodeControl());
		
		GcgHttpClient.getInstance(myContext).post(UrlInfo.getUrlCodeInfo(myContext), params,new HttpResponseListener() {
				@Override
				public void onStart() {
					showloadDialog();
				}

				@Override
				public void onFailure(Throwable error, String content) {
					JLog.d(TAG, "result:" + content);
					ToastOrder.makeText(myContext, myContext.getResources().getString(R.string.network_isfalse_and_loading_fail),ToastOrder.LENGTH_SHORT).show();
				}

				@Override
				public void onFinish() {
					if (loadDialog != null && loadDialog.isShowing()) {
						loadDialog.dismiss();
					}
				}

				@Override
				public void onSuccess(int statusCode, String content) {
					JLog.d(TAG, "result:" + content);
					try {
						JSONObject jsonObject = new JSONObject(content);
						String resultcode = jsonObject.getString(Constants.RESULT_CODE);
						if (resultcode.equalsIgnoreCase(Constants.RESULT_CODE_SUCCESS)) {// 返回0000获取成功
							showCodeInfo(func, content);
						} else {// 失败
							ToastOrder.makeText(myContext, myContext.getResources().getString(R.string.network_isfalse_and_loading_fail),ToastOrder.LENGTH_SHORT).show();
						}
					} catch (Exception e) {// 数据异常，失败
						JLog.d(TAG, "异常：" + e.getMessage());
						ToastOrder.makeText(myContext, myContext.getResources().getString(R.string.network_isfalse_and_loading_fail),ToastOrder.LENGTH_SHORT).show();
					}
					
				}
			});
		}else{
			ToastOrder.makeText(myContext, myContext.getResources().getString(R.string.configure_false),ToastOrder.LENGTH_SHORT).show();
		}
	}

	/**
	 * 显示串码信息
	 * 
	 * @param func串码控件
	 * @param info串码属性信息
	 * @throws JSONException 
	 */
	private void showCodeInfo(Func func, String info) throws JSONException {
//		String codeType = func.getCodeType();
		JLog.d(TAG, "codeInfo:"+info);

		String obj_1=null;
		String obj_2=null;
		String obj_3=null;
		String obj_4=null;
		
		JSONObject object = new JSONObject(info);
		if (isValid(object, "1")) {
			obj_1=object.getString("1");
			JLog.d(TAG, "1:"+obj_1);
		}
		if (isValid(object, "2")) {
			obj_2=object.getString("2");
			JLog.d(TAG, "2:"+obj_2);
		}
		if (isValid(object, "3")) {
			obj_3=object.getString("3");
			JLog.d(TAG, "3:"+obj_3);
		}
		if (isValid(object, "4")) {
			obj_4=object.getString("4");
			JLog.d(TAG, "4:"+obj_4);
		}
		
		if ("4".equals(func.getCodeType())) {
			if (table_type == TABLE_TYPE_TABLE && productCodeInTableComp!=null) {
				productCodeInTableComp.setValue(value);
			}else if (compDialog!=null) {
				compDialog.save();
			}
			if (productCodeListener !=null) {
				productCodeListener.productCodeInfo(obj_4, tableRow);
			}
		}else{
			showCodeInfo(object,obj_1, obj_2,obj_3,obj_4,func);
		}
		
	}
	
	/**
	 * 显示串码信息
	 * @param object
	 * @param obj_1
	 * @param obj_2
	 * @param obj_3
	 * @param obj_4
	 * @param func
	 * @throws JSONException
	 */
	private void showCodeInfo(final JSONObject object,String obj_1, String obj_2,String obj_3,final String obj_4,final Func func) throws JSONException {
		View view = View.inflate(myContext,R.layout.product_code_show_info_dialog, null);
		TextView code = (TextView)view.findViewById(R.id.tv_p_code);
		TextView tip = (TextView)view.findViewById(R.id.pcode_tip);
		code.setText(value);
		popupWindow = new PopupWindow(view,WindowManager.LayoutParams.FILL_PARENT,WindowManager.LayoutParams.FILL_PARENT,true);
		popupWindow.showAtLocation(((Activity) myContext).findViewById(R.id.ll_btn_layout), LayoutParams.WRAP_CONTENT, 0, 0);
		Button confirmBtn = (Button) view.findViewById(R.id.pCode_info_confirmBtn);
		LinearLayout ll_info_1 = (LinearLayout) view.findViewById(R.id.ll_pCode_info_1);
		LinearLayout ll_info_2 = (LinearLayout) view.findViewById(R.id.ll_pCode_info_2);
		LinearLayout ll_info_3 = (LinearLayout) view.findViewById(R.id.ll_pCode_info_3);

		if (!TextUtils.isEmpty(obj_1) && obj_1.equals("0")) {//串码不存在的时候显示串码不存在
			tip.setVisibility(View.VISIBLE);
			tip.setText(myContext.getResources().getString(R.string.onhas_this_chuancode));
		}else{
			String codeType = func.getCodeType();
			if (codeType.contains("1") && obj_1!=null && obj_1.equals("1")) {
				tip.setVisibility(View.VISIBLE);
				tip.setText(myContext.getResources().getString(R.string.has_this_chuancode));
			}
			if (codeType.contains("2")) {
				ll_info_2.setVisibility(View.VISIBLE);
				JSONArray array = object.getJSONArray("2");
				int len = array.length();
				if (len == 0) {
					ll_info_1.setVisibility(View.VISIBLE);
				} else {
					for (int i = 0; i < array.length(); i++) {
						JSONObject obj=(JSONObject) array.get(i);
						if (obj !=null) {
							InfoDetailItem item =new InfoDetailItem(myContext);
							Iterator<String> iterator= obj.keys();
							if (!iterator.hasNext()) {
								continue;
							}
							String key = iterator.next();
							String value = obj.getString(key);
							item.setName(key);
							item.setValue("     "+value);
							ll_info_2.addView(item.getView());
						}
					}
				}
			}
			if (codeType.contains("3") && !TextUtils.isEmpty(obj_3)) {
				JSONObject obj = object.getJSONObject("3");
				String codeControl = currentFunc.getCodeControl();
				String compareContent = obj.has("compare_content")?obj.getString("compare_content"):"";
				if (("2".equals(codeControl) || "3".equals(codeControl)) && !TextUtils.isEmpty(compareContent)) {
					ll_info_3.setVisibility(View.VISIBLE);
					TextView tv_pCode_match = (TextView)view.findViewById(R.id.tv_pCode_match);
					tv_pCode_match.setText(compareContent);
				}
			}
		}
		view.setFocusable(true); // 这个很重要
		view.setFocusableInTouchMode(true);
		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(new PaintDrawable());
		confirmBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
				if (table_type == TABLE_TYPE_TABLE && productCodeInTableComp!=null) {
					productCodeInTableComp.setValue(value);
				}else if (compDialog!=null) {
					compDialog.save();
				}
				if (productCodeListener !=null) {
					productCodeListener.productCodeInfo(obj_4, tableRow);
				}
				setCompareResult(object,func);//点击确定的时候设置比对结果
			}
		});
		
		view.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					popupWindow.dismiss();
					return true;
				}
				return false;
			}
		});
	}
	
	/**
	 * 设置串码比对结果
	 * @param object
	 * @param func
	 */
	private void setCompareResult(JSONObject object,Func func){
		if (object.has("3")) {
			try {
				String obj_1=object.getString("1");
				if (obj_1.equals("0") && ("3".equals(func.getCodeControl())|| "4".equals(func.getCodeControl()))) {//串码不存在的情况
					//如果包含串码比对的话,并且需要控制提交的话认为比对没通过
					if (table_type != TABLE_TYPE_TABLE) {
						AbsFuncActivity absFuncActivity = (AbsFuncActivity) myContext;
						absFuncActivity.codeSubmitControl = false;
					}else{
						productCodeInTableComp.codeSubmitControl = false;
					}
				}else{//串码存在
					JSONObject obj = object.getJSONObject("3");
					compareResult(obj);
					compareContent(obj);
					compareRuleIds(obj);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 串码比对数据ids 提交数据的时候传给服务端 手机段不使用
	 * @param obj
	 * @throws JSONException
	 */
	private void compareRuleIds(JSONObject obj) throws JSONException{
		if (obj!=null && obj.has("compare_rule_ids")) {
			String compare_rule_ids = obj.getString("compare_rule_ids");
			if (table_type == TABLE_TYPE_TABLE && productCodeInTableComp!=null) {
				productCodeInTableComp.setCompareRuleIds(compare_rule_ids);
			}else{
				SharedPreferencesUtil.getInstance(myContext).setCompareRuleIds("ids_"+currentFunc.getTargetid(),compare_rule_ids);
			}
		}
	}
	
	/**
	 * 串码比对内容
	 * @param obj
	 * @return
	 * @throws JSONException
	 */
	private void compareContent(JSONObject obj) throws JSONException{
		String compareContent = null;
		if (obj!=null && obj.has("compare_content")) {//串码比对是否通过
			compareContent = obj.getString("compare_content");//比对结果
			if (table_type == TABLE_TYPE_TABLE && productCodeInTableComp!=null) {
				productCodeInTableComp.setCompareContent(compareContent);
			}else{
				SharedPreferencesUtil.getInstance(myContext).setCompareContent("content_"+currentFunc.getTargetid(),compareContent);
			}
		}
	}
	
	/**
	 * 串码比对是否通过结果
	 * @param obj
	 * @throws JSONException
	 */
	private void compareResult(JSONObject obj) throws JSONException{
		String codeControl = currentFunc.getCodeControl();
		if (("3".equals(codeControl)|| "4".equals(codeControl)) && obj!=null && obj.has("is_success")) {//串码比对是否通过
			boolean submitControl = obj.getBoolean("is_success");//true通过可以提交，false不通过
			if (table_type != TABLE_TYPE_TABLE) {
				AbsFuncActivity absFuncActivity = (AbsFuncActivity) myContext;
				absFuncActivity.codeSubmitControl = submitControl;
			}else{
				productCodeInTableComp.codeSubmitControl = submitControl;
			}
		}
	}
	/**
	 * 显示加载dialog
	 */
	private Dialog loadDialog = null;
	private void showloadDialog() {
		if (loadDialog != null && loadDialog.isShowing()) {
			loadDialog.dismiss();
			loadDialog = null;
		}
		loadDialog = new MyProgressDialog(myContext,R.style.CustomProgressDialog, myContext.getResources().getString(R.string.initTable));
		loadDialog.show();
	}
	
	public boolean isValid(JSONObject jsonObject, String key)throws JSONException {
		boolean flag = false;
		if (jsonObject.has(key) && !jsonObject.isNull(key)) {
			flag = !"".equals(jsonObject.getString(key));
		}
		return flag;
	}
	
	/**
	 *每个view的实例 
	 *
	 */
	private class InfoDetailItem{
		private LinearLayout ll_info_detail_item;
		public InfoDetailItem(Context context) {
			super();
			ll_info_detail_item = (LinearLayout) View.inflate(context, R.layout.pcode_info_item, null);
		}
		public void setName(String name){//控件名称
			((TextView)ll_info_detail_item.findViewById(R.id.pcode_item_name)).setText(name);
		}
		public void setValue(String value){//控件的值
			((TextView)ll_info_detail_item.findViewById(R.id.pcode_item_value)).setText(value);
		}
		public View getView(){//返回view
			return ll_info_detail_item;
		}
	}

	@Override
	public View getObject() {
		productCodeDialog(currentFunc);
		init();
		return view;
	}
	
	
	public String scanBtnName(){
		return myContext.getString(R.string.product_code_scan);
	}

	/**
	 * 初始化数据
	 */
	private void init(){
		if (table_type != TABLE_TYPE_TABLE) {
			SubmitItem item=null;
			if(isLink){//超链接 查询临时表
				item=new SubmitItemTempDB(myContext).findSubmitItem(null, Submit.STATE_NO_SUBMIT, planId, wayId, storeId, targetid, targetType, String.valueOf(currentFunc.getFuncId()));
			}else{
				item=new SubmitItemDB(myContext).findSubmitItem(null, Submit.STATE_NO_SUBMIT, planId,wayId, storeId, targetid, targetType, String.valueOf(currentFunc.getFuncId()));
			}
			if(item!=null){
				String itemValue = item.getParamValue();// 查询数据库中当前对应的值
				pCodeET.setText(itemValue);
				value = itemValue;
				this.sCode = value;
				JLog.d(TAG, "itemValue==>" + itemValue);
			}
		}
	}

	@Override
	public void setIsEnable(boolean isEnable) {
		
	}

	public void setProductCodeListener(ProductCodeListener productCodeListener) {
		this.productCodeListener = productCodeListener;
	}

	public void setTableRow(int tableRow) {
		this.tableRow = tableRow;
	}		
	
	/**
	 * 串码是否更换过
	 * true 表示串码更换过 false表示串码没有更换过
	 */
	private String sCode;
	public boolean isCodeUpdate(){
		sCode = TextUtils.isEmpty(sCode)?"":sCode;
		value = TextUtils.isEmpty(value)?"":value;
		if (sCode.equals(value)) {
			return false;
		}else{
			return true;
		}
	}
}
