package com.yunhu.yhshxc.activity.carSales.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.submitManager.bo.PendingRequestVO;
import com.yunhu.yhshxc.submitManager.bo.TablePending;
import com.yunhu.yhshxc.submitManager.core.SubmitWorkManager;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.DropDown;
import com.yunhu.yhshxc.widget.DropDown.OnResultListener;
import com.yunhu.yhshxc.widget.wheeltime.TimeView;

public class ApplyForReimbursementActivity  extends AbsBaseActivity {
	
	private final String TYPE_TC = "1";//停车
	private final String TYPE_GL = "2";//过路
	private final String TYPE_JY = "3";//加油
	private final String TYPE_XL = "4";//修理
	private final String TYPE_QT = "5";//其他
	
	private DropDown dropDown;//报销类型
	private EditText et_amount;//报销金额
	private Button btn_date;//报销日期
	private EditText et_explain;//说明
	private String seleteId;//报销类别选中的ID
	private String selectName;//报销类别选中的名称;
	private String bxDate = null;//报销日期
	private LinearLayout ll_apply_submit;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.apply_for_reimbursement_activity);
		initWidget();
		initDropDownData(null);
	}
	
	private void initWidget(){
		dropDown = (DropDown) findViewById(R.id.sp_lb);
		dropDown.setOnResultListener(resultListener);
		et_amount = (EditText)findViewById(R.id.et_amount);
		btn_date = (Button)findViewById(R.id.btn_date);
		btn_date.setOnClickListener(listener);
		et_explain = (EditText)findViewById(R.id.et_explain);
		ll_apply_submit = (LinearLayout)findViewById(R.id.ll_apply_submit);
		ll_apply_submit.setOnClickListener(listener);
	}

	/**
	 * 初始化所有客户数据
	 */
	private List<Dictionary> srcList;
	private void initDropDownData(String fuzzy) {
		srcList = new ArrayList<Dictionary>();
		
		Dictionary dicTC = new Dictionary();
		dicTC.setDid(TYPE_TC);
		dicTC.setCtrlCol("停车费");
		srcList.add(dicTC);
		
		Dictionary dicGL = new Dictionary();
		dicGL.setDid(TYPE_GL);
		dicGL.setCtrlCol("过路费");
		srcList.add(dicGL);
		
		Dictionary dicJY = new Dictionary();
		dicJY.setDid(TYPE_JY);
		dicJY.setCtrlCol("加油费");
		srcList.add(dicJY);
		
		Dictionary dicXL = new Dictionary();
		dicXL.setDid(TYPE_XL);
		dicXL.setCtrlCol("修理费");
		srcList.add(dicXL);
		
		Dictionary dicQT = new Dictionary();
		dicQT.setDid(TYPE_QT);
		dicQT.setCtrlCol("其他");
		srcList.add(dicQT);
		
		dropDown.setSrcDictList(srcList);
		initDropDownSelect(srcList);
	}

	
	/**
	 * 设置选中的店面
	 * 
	 * @param srcList
	 *            所有店面
	 */
	
	private void initDropDownSelect(List<Dictionary> srcList) {
		if (!TextUtils.isEmpty(seleteId) && srcList.size() > 0) {
			for (int i = 0; i < srcList.size(); i++) {
				Dictionary dic = srcList.get(i);
				if (seleteId.equals(dic.getDid())) {
					dropDown.setSelected(dic);
					break;
				}
			}
		}
	}

	private OnResultListener resultListener = new OnResultListener() {

		@Override
		public void onResult(List<Dictionary> result) {
			if (result != null && !result.isEmpty()) {
				Dictionary dic = result.get(0);
				seleteId = dic.getDid();
				selectName = dic.getCtrlCol();
			}

		}
	};
	
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_date:
				selectDate();
				break;
			case R.id.ll_apply_submit:
				submit();
				break;
			}
		}
	};
	
	private void submit(){
		if (checkComplete()) {
			String url = UrlInfo.doCostInfo(this);
			PendingRequestVO vo = new PendingRequestVO();
			vo.setContent("类别:"+selectName);
			vo.setTitle("费用报销");
			vo.setMethodType(SubmitWorkManager.HTTP_METHOD_POST);
			vo.setType(TablePending.TYPE_DATA);
			vo.setUrl(url);
			vo.setParams(submitParams());
			SubmitWorkManager.getInstance(this).performJustSubmit(vo);
			SubmitWorkManager.getInstance(this).commit();
			finish();
		}
	}
	
	private HashMap<String, String> submitParams(){
		HashMap<String, String> params = new HashMap<String, String>();
		JSONArray arry = new JSONArray();
		try {
			JSONObject obj = new JSONObject();
			obj.put("apply_date", bxDate+" 00:00:00");
			obj.put("cost_num", String.valueOf(System.currentTimeMillis()));
			obj.put("remarks", et_explain.getText().toString());
			obj.put("amount", et_amount.getText().toString());
			obj.put("cost_type", seleteId);
			arry.put(obj);
			params.put("cost", arry.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return params;
	}
	
	/**
	 * 完整性验证
	 * @return
	 */
	private boolean checkComplete(){
		boolean flag = true;
		if (TextUtils.isEmpty(seleteId)) {
			flag = false;
			Toast.makeText(this, "请选择报销类别", Toast.LENGTH_SHORT).show();
		}else if(TextUtils.isEmpty(et_amount.getText().toString())){
			flag = false;
			Toast.makeText(this, "请填写费用金额", Toast.LENGTH_SHORT).show();

		}else if(TextUtils.isEmpty(bxDate)){
			flag = false;
			Toast.makeText(this, "请选择日期", Toast.LENGTH_SHORT).show();
		}
		return flag;
	}
	
	/**
	 * 选择日期
	 */
	private int year,month,day;
	private String currentDate;
	private void selectDate(){
		currentDate = bxDate;
		final Dialog dialog = new Dialog(this, R.style.transparentDialog);
		View view=null;
		view=View.inflate(this, R.layout.report_date_dialog, null);
		if(!TextUtils.isEmpty(currentDate)){
			String[] date = currentDate.split("-");
			year = Integer.parseInt(date[0]);
			month = Integer.parseInt(date[1]) - 1;
			day = Integer.parseInt(date[2]);
		}else{
			Calendar _c = Calendar.getInstance();// 获取系统默认是日期
			year = _c.get(Calendar.YEAR);
			month = _c.get(Calendar.MONTH);
			day = _c.get(Calendar.DAY_OF_MONTH);
		}
		LinearLayout ll_date = (LinearLayout) view.findViewById(R.id.ll_compDialog);
		TimeView dateView = new TimeView(this,TimeView.TYPE_DATE, new TimeView.WheelTimeListener() {
			
			@Override
			public void onResult(String wheelTime) {
				currentDate = wheelTime;
			}
		});
		dateView.setOriDate(year, month+1, day);
		ll_date.addView(dateView);
		Button confirmBtn=(Button)view.findViewById(R.id.report_dialog_confirmBtn);
		Button cancelBtn=(Button)view.findViewById(R.id.report_dialog_cancelBtn);
		confirmBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				bxDate = currentDate;
				btn_date.setText(currentDate);
			}
		});
		cancelBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.setContentView(view);
		dialog.setCancelable(false);
		dialog.show();
	}
	
}
