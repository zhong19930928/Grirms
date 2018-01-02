package com.yunhu.yhshxc.doubletask2;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.activity.ModuleFuncActivity;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.bo.Module;
import com.yunhu.yhshxc.comp.DoubleDetailItem;
import com.yunhu.yhshxc.database.DoubleDB;
import com.yunhu.yhshxc.database.FuncDB;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.parser.CacheData;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gcg.org.debug.JLog;

/**
 * 双向的详细信息页面
 * 展示双向任务的详细信息
 * 有执行和取消双向任务按钮 其中取消是否显示是可配置的
 * 点“执行”的时候直接跳转到ModuleFuncActivity 点击“取消”弹出对话框输入取消原因，然后发送给服务器
 * @author jishen
 * @version 2013-05-29
 *
 */
public class NewDoubleDetailActivity extends AbsBaseActivity {
	private String TAG="NewDoubleDetailActivity";
	
	public static final String DOUBLE_BTN_TYPE = "double_Btn_Type";
	public static final String DOUBLE_MASTER_TASK_NO = "double_Master_Task_No";
	
	
	/**
	 * 双向任务view
	 */
	private LinearLayout doubleDetailLayout;
	/**
	 * 任务ID
	 */
	private int taskId;
	/**
	 * 数据ID
	 */
	private int targetId;
	/**
	 * 双向任务的创建时间
	 */
	private TextView createDate;
	/**
	 * 双向任务第一项的内容
	 */
	private TextView firstContent;
	/**
	 * 取消双向任务的时候输入取消原因的输入框
	 */
	private EditText etContent;
	/**
	 * 顶部title
	 */
//	private TextView headText;
	/**
	 * 获取传递数据的bundle
	 */
	private Bundle bundle;
	/**
	 * 双向的自定义模块的实例
	 */
	private Module module;
	private boolean isNoWait;
	
	private ProgressBar progressBar;//任务完成进度条
	private TextView tv_bar;//进度条完成进度
	private TextView tv_complete;//完成进度
	private String total,cut;
	private String dataStatus;
	private String doubleMasterTaskNo;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_double_detail);
		initBase();//实现父类的方法，目前没做处理
		
		progressBar = (ProgressBar)findViewById(R.id.new_double_detail_bar);
		tv_bar = (TextView)findViewById(R.id.new_double_detail_bar_tv);
		tv_complete = (TextView)findViewById(R.id.new_double_detail_complete_tv);
		doubleDetailLayout = (LinearLayout) findViewById(R.id.ll_double_detail_content);
		createDate = (TextView) findViewById(R.id.tv_doubleDetail_Time);
		firstContent = (TextView) findViewById(R.id.excute_detail_first_item);
		
		bindData();//绑定数据
	}

	/**
	 * 给详细信息组件绑定数据
	 */
	private void bindData() {
		if (isNormal) {
			bundle = getIntent().getBundleExtra("currentDoubleTask");
			isNoWait = getIntent().getBooleanExtra("isNoWait", true);
		}
		module=(Module) bundle.getSerializable("module");
		taskId = bundle.getInt("taskId");//获取任务ID
		targetId = bundle.getInt("targetId");//获取数据ID
		createDate.setText(bundle.getString("createTime"));//获取创建日期
		firstContent.setText(getResources().getString(R.string.time));
		total = bundle.getString("total");
		cut = bundle.getString("cut");
		dataStatus = bundle.getString("dataStatus");
		doubleMasterTaskNo = bundle.getString("doubleMasterTaskNo");
		
//		cut = "0";
//		total = "3";
		if (!TextUtils.isEmpty(cut) && !TextUtils.isEmpty(total)) {
			tv_complete.setText(cut+"/"+total);
			if ("0".equals(total)) {
				tv_bar.setText("100%");
			}else{
				tv_bar.setText(percent(Integer.parseInt(cut), Integer.parseInt(total)));
			}
			progressBar.setMax(Integer.parseInt(total));
			progressBar.setProgress(Integer.parseInt(cut));
		}
		
		Map<String, String> map=new DoubleDB(this).findDoubleTask(String.valueOf(taskId), targetId);//根据数据ID和任务ID查找双向内容返回map key是data_+控件ID value是值
		List<Func> doubleFuncList=new FuncDB(this).findFuncListByDoubleReadOnly(targetId);//双向中获取只读的func
		Func funcItem = null;
		for (int i = 0; i < doubleFuncList.size(); i++) {
			funcItem = doubleFuncList.get(i);
			if(funcItem.getType()==Func.TYPE_BUTTON || 
			   funcItem.getType()==Func.TYPE_HIDDEN || 
			   funcItem.getType()==Func.TYPE_CAMERA || 
			   funcItem.getType()==Func.TYPE_LINK ||
			   funcItem.getType()==Func.TYPE_CAMERA_MIDDLE || 
			   funcItem.getType()==Func.TYPE_CAMERA_CUSTOM ||
			   funcItem.getType()==Func.TYPE_CAMERA_HEIGHT){
				continue;//button类型 隐藏域类型 拍照类型 超链接类型不处理
			}
			DoubleDetailItem item=new DoubleDetailItem(this);//添加一个双向内容view
			item.setTitle(funcItem.getName());//设置标题
			item.setContent(map.get(CacheData.DATA_N+funcItem.getFuncId()),funcItem);//设置内容
			doubleDetailLayout.addView(item.getView());//将view添加到父view中
		}
		buttonControl();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 * 点击删除按钮删除该条信息
	 */

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case 1://接受
			accept();
			break;
		case 2://上报
			report();
			break;
		case 3://修改
			update();
			break;
		case 4://取消
			cancel();
			break;
		case 5://完成上报
			complete();
			break;
		case -99://动态按钮
			dynamicBtn();
			break;
		default:
			break;
		}

	}
	
	/**
	 * 接受
	 */
	private void accept(){
		Intent intent=new Intent(NewDoubleDetailActivity.this,ModuleFuncActivity.class);
		Bundle bundle_ = new Bundle();
		bundle_.putInt("targetId",targetId);//数据ID
		bundle_.putInt("taskId", taskId);//任务ID
		bundle_.putSerializable("module", module);//自定义双向实例
		bundle_.putInt("menuType", Menu.TYPE_NEW_TARGET);//模块类型新双向
		bundle_.putString("status", dataStatus);
		bundle_.putString(DOUBLE_BTN_TYPE, "1");
		bundle_.putString(DOUBLE_MASTER_TASK_NO, doubleMasterTaskNo);
		intent.putExtra("bundle", bundle_);
		intent.putExtra("isNoWait", isNoWait);
		startActivity(intent);
		this.finish();
	}
	
	/**
	 * 上报
	 */
	private void report(){
		String isReportTask = module.getIsReportTask();//超出任务是否可以继续上报 1 可以 0 不可以
		if (Integer.parseInt(cut) >= Integer.parseInt(total) && !"1".equals(isReportTask)) {
			ToastOrder.makeText(this, getResources().getString(R.string.not_reporting_data_continue), ToastOrder.LENGTH_SHORT).show();
			return;
		}
		Intent intent=new Intent(NewDoubleDetailActivity.this,ModuleFuncActivity.class);
		Bundle bundle_ = new Bundle();
		bundle_.putInt("targetId",targetId);//数据ID
		bundle_.putInt("taskId", taskId);//任务ID
		bundle_.putSerializable("module", module);//自定义双向实例
		bundle_.putInt("menuType", Menu.TYPE_NEW_TARGET);//模块类型新双向
		bundle_.putString("status", dataStatus);
		bundle_.putString(DOUBLE_BTN_TYPE, "2");
		bundle_.putString(DOUBLE_MASTER_TASK_NO, doubleMasterTaskNo);
		intent.putExtra("bundle", bundle_);
		intent.putExtra("isNoWait", isNoWait);
		startActivity(intent);
		this.finish();
	}
	
	/**
	 * 修改
	 */
	private void update(){
		Intent intent = new Intent(this, ChildrenListActivity.class);
		Bundle replenishBundle = new Bundle();
		replenishBundle.putInt("targetId", module.getMenuId());//数据ID
		replenishBundle.putSerializable("module", module);//自定义模块实例
		replenishBundle.putInt("menuType", Menu.TYPE_NEW_TARGET);//菜单类型
		replenishBundle.putString(DOUBLE_BTN_TYPE, "3");
		replenishBundle.putString(DOUBLE_MASTER_TASK_NO, doubleMasterTaskNo);
		intent.putExtra("bundle", replenishBundle);
		intent.putExtra("isNoWait", isNoWait);//是否无等待提交数据
		startActivity(intent);
	}
	
	/**
	 * 取消
	 * doubleBtnType = 4
	 */
	private void cancel(){
		deleteTempStore("4").show();
	}
	/**
	 * 动态按钮
	 */
	private void dynamicBtn(){
		Intent intent=new Intent(NewDoubleDetailActivity.this,ModuleFuncActivity.class);
		Bundle bundle_ = new Bundle();
		bundle_.putInt("targetId",targetId);//数据ID
		bundle_.putInt("taskId", taskId);//任务ID
		bundle_.putSerializable("module", module);//自定义双向实例
		bundle_.putInt("menuType", Menu.TYPE_NEW_TARGET);//模块类型新双向
		bundle_.putString("status", dataStatus);
		bundle_.putString(DOUBLE_BTN_TYPE, "-99");
		bundle_.putString(DOUBLE_MASTER_TASK_NO, doubleMasterTaskNo);
		intent.putExtra("bundle", bundle_);
		intent.putExtra("isNoWait", isNoWait);
		startActivity(intent);
		this.finish();
	}
	
	
	/**
	 * 完成上报 中止
	 * doubleBtnType = 5
	 */
	private void complete(){
		deleteTempStore("5").show();
	}
	
	/**
	 * 删除双向任务的时候弹出删除原因对话框
	 * type = cancel 取消
	 * type = stop 中止
	 * @return
	 */
	private Dialog deleteTempStore(final String type){
		final Dialog dialog=new Dialog(this, R.style.transparentDialog);
		View view=View.inflate(this, R.layout.dialog_delte_tempstore, null);
		
		//删除提示内容TextView
		TextView  tvContent = (TextView) view.findViewById(R.id.tv_back_content);
		//设置删除提示内容TextView不显示
		tvContent.setVisibility(View.GONE);
		//店面取消的理由EditText 
		etContent = (EditText) view.findViewById(R.id.et_delete_reason);
		etContent.setHint(getResources().getString(R.string.please_input_reasons));
		//设置店面取消的理由EditText显示 
		etContent.setVisibility(View.VISIBLE);

		Button confirm=(Button)view.findViewById(R.id.btn_delete_tempstore_confirm);
		Button cancel=(Button)view.findViewById(R.id.btn_delete_tempstore_cancel);
		confirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String deleteReason = etContent.getText().toString();
				if(TextUtils.isEmpty(deleteReason) && "4".equals(type)){//删除理由为空 //中止理由非必须输入
					//提示用户输入删除理由
					 ToastOrder.makeText(NewDoubleDetailActivity.this, getResources().getString(R.string.please_input_reasons), ToastOrder.LENGTH_LONG).show();
					 //结束本次点击事件
					 return;
				}
				
				RequestParams params =new RequestParams();
				params.put("cancel", deleteReason);//删除原因
				params.put("tableId", String.valueOf(targetId));//数据ID
				params.put("dataId", String.valueOf(taskId));//任务ID
				params.put("doubleBtnType",type);//button类型
				params.put("doubleMasterTaskNo", doubleMasterTaskNo);//双向主任务数据id
				params.put("phoneno", PublicUtils.receivePhoneNO(NewDoubleDetailActivity.this));//电话号码
				JLog.d(TAG, "phoneno==>"+PublicUtils.receivePhoneNO(NewDoubleDetailActivity.this)+"\ncancel==>"+deleteReason + "\ntableId==>"+targetId + "\ndataId==>"+taskId + "\ndoubleBtnType==>"+type + "\ndoubleMasterTaskNo==>"+doubleMasterTaskNo);
				submit(params,type);
				dialog.dismiss();
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.setContentView(view);
		dialog.setCancelable(false);
		return dialog;
	}
	
	
	private Dialog submitLoadDialog;
	private void submit(RequestParams params,final String btnType){
		submitLoadDialog = new MyProgressDialog(this,R.style.CustomProgressDialog,"正在提交...");
		String url = UrlInfo.getUrlDeleteExecute(NewDoubleDetailActivity.this);
		GcgHttpClient.getInstance(NewDoubleDetailActivity.this).post(url, params, new HttpResponseListener(){
			@Override
			public void onStart() {
				if (submitLoadDialog!=null && !submitLoadDialog.isShowing()) {
					submitLoadDialog.show();
				}
			}
			
			@Override
			public void onSuccess(int statusCode, String content) {
				JLog.d(TAG, "取消/中止："+content);
				try {
					JSONObject obj = new JSONObject(content);
					if (obj.has("resultcode") && "0000".equals(obj.get("resultcode"))) {
						if ("4".equals(btnType)) {//删除双向任务数据  如果是完成上报的话不删除双向任务数据
							new DoubleDB(NewDoubleDetailActivity.this).removeDoubleTask(String.valueOf(taskId), String.valueOf(targetId));//提交后删除该双向任务
						}
						NewDoubleDetailActivity.this.finish();
					}else{
						ToastOrder.makeText(NewDoubleDetailActivity.this, getResources().getString(R.string.submit_fali), ToastOrder.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				JLog.d(TAG, "提交失败:"+content+"  : "+error.getMessage());
				ToastOrder.makeText(NewDoubleDetailActivity.this, getResources().getString(R.string.submit_fali), ToastOrder.LENGTH_SHORT).show();
			}
			
			@Override
			public void onFinish() {
				if (submitLoadDialog!=null && submitLoadDialog.isShowing()) {
					submitLoadDialog.dismiss();
				}
			}
			
		});
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == R.id.submit_succeeded) {
			this.finish();
		}
	}
	
	
	/**
	 * 按钮控制
	 */
	
	private LinearLayout ll_new_double_detail_btn;
	private void buttonControl(){
		ll_new_double_detail_btn = (LinearLayout)findViewById(R.id.ll_new_double_detail_btn);
		NewDoubleButtonView btnView = new NewDoubleButtonView(this, this);
		//固定按钮
		String phoneTaskFuns = module.getPhoneTaskFuns();
		Map<String, String> mainBtnMap = new HashMap<String, String>();
		if (!TextUtils.isEmpty(phoneTaskFuns)) {
			String[] taskFuns = phoneTaskFuns.split(",");
			for (int i = 0; i < taskFuns.length; i++) {
				String[] signalBtn = taskFuns[i].split(":");
				mainBtnMap.put(signalBtn[0], signalBtn[1]);
			}
		}
		btnView.setMainBtnMap(mainBtnMap);
		
		//动态按钮
		String dynamicStatus = module.getDynamicStatus();
		Map<String, String> duynamicBtnMap = new HashMap<String, String>();
		if (!TextUtils.isEmpty(dynamicStatus)) {
			String[] taskFuns = dynamicStatus.split(",");
			for (int i = 0; i < taskFuns.length; i++) {
				String[] signalBtn = taskFuns[i].split(":");
				duynamicBtnMap.put(signalBtn[0], signalBtn[1]);
			}
		}
		btnView.setDynamicBtnMap(duynamicBtnMap);
		btnView.initButtonData(dataStatus);
		if (btnView.isShowBtn()) {
			ll_new_double_detail_btn.addView(btnView.getView());
		}
	}
	
	public String percent(int y,int z){
		   String baifenbi="";//接受百分比的值
		   double baiy=y*1.0;
		   double baiz=z*1.0;
		   double fen=baiy/baiz;
		   //NumberFormat nf   =   NumberFormat.getPercentInstance();     注释掉的也是一种方法
		   //nf.setMinimumFractionDigits( 2 );        保留到小数点后几位
		   DecimalFormat df1 = new DecimalFormat("##%");    //##.00%   百分比格式，后面不足2位的用0补齐
		    //baifenbi=nf.format(fen);   
		   baifenbi= df1.format(fen);  
		    return baifenbi;
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBundle("bundle",bundle);
		outState.putBoolean("isNoWait", isNoWait);
	}

	/**
	 * 读取数据-->非正常状态下使用
	 */
	@Override
	protected void restoreConstants(Bundle savedInstanceState) {
		super.restoreConstants(savedInstanceState);
		bundle = savedInstanceState.getBundle("bundle");
		isNoWait = savedInstanceState.getBoolean("isNoWait");
	}
}
