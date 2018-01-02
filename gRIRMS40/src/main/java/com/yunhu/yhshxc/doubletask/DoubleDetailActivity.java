package com.yunhu.yhshxc.doubletask;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.yunhu.yhshxc.database.MainMenuDB;
import com.yunhu.yhshxc.http.CoreHttpHelper;
import com.yunhu.yhshxc.parser.CacheData;
import com.yunhu.yhshxc.submitManager.bo.PendingRequestVO;
import com.yunhu.yhshxc.submitManager.bo.TablePending;
import com.yunhu.yhshxc.submitManager.core.SubmitWorkManager;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;

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
public class DoubleDetailActivity extends AbsBaseActivity {
	private String TAG="DoubleDetailActivity";
	/**
	 * operateLayout执行按钮
	 * cancelLayout取消按钮
	 */
	private LinearLayout operateLayout,cancelLayout;
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
	private TextView headText;
	/**
	 * operateIv执行按钮的图标
	 * cancelIv取消按钮的图标
	 */
	private ImageView operateIv,cancelIv;
	/**
	 * operateTv执行按钮的文字内容
	 * cancelTv取消按钮的文字内容
	 */
	private TextView operateTv,cancelTv;
	/**
	 * 获取传递数据的bundle
	 */
	private Bundle bundle;
	/**
	 * 双向的自定义模块的实例
	 */
	private Module module;
	private boolean isNoWait;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.double_detail);
		initBase();//实现父类的方法，目前没做处理
		//初始化实例
		operateLayout = (LinearLayout) findViewById(R.id.ll_double_detail_data);
		cancelLayout = (LinearLayout) findViewById(R.id.ll_double_detail_data_cancle);
		doubleDetailLayout = (LinearLayout) findViewById(R.id.ll_double_detail_content);
		createDate = (TextView) findViewById(R.id.tv_doubleDetail_Time);
		firstContent = (TextView) findViewById(R.id.excute_detail_first_item);
		
		operateIv=(ImageView)findViewById(R.id.excute_detail_iv);
		cancelIv=(ImageView)findViewById(R.id.excute_delete_iv);
		operateTv=(TextView)findViewById(R.id.excute_tv);
		cancelTv=(TextView)findViewById(R.id.excute_delete_tv);
		
		headText=(TextView)findViewById(R.id.tv_doubleHade);
		operateLayout.setOnClickListener(this);
		cancelLayout.setOnClickListener(this);
		//执行按钮设置触摸事件 改变背景颜色和图片
		operateLayout.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction()==MotionEvent.ACTION_UP){
					operateIv.setBackgroundResource(R.drawable.excute_btn1);
					operateTv.setTextColor(DoubleDetailActivity.this.getResources().getColor(R.color.excute_list_title));
				}else{
					operateIv.setBackgroundResource(R.drawable.excute_btn2);
					operateTv.setTextColor(DoubleDetailActivity.this.getResources().getColor(R.color.excute_text_click));
				}
				return false;
			}
		});
		//取消按钮设置触摸事件 改变背景颜色和图片
		cancelLayout.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction()==MotionEvent.ACTION_UP){
					cancelIv.setBackgroundResource(R.drawable.excute_cancel1);
					cancelTv.setTextColor(DoubleDetailActivity.this.getResources().getColor(R.color.excute_list_title));
				}else{
					cancelIv.setBackgroundResource(R.drawable.excute_cancel2);
					cancelTv.setTextColor(DoubleDetailActivity.this.getResources().getColor(R.color.excute_text_click));
				}
				return false;
			}
		});
		
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
		if(!TextUtils.isEmpty(bundle.getString("headContent"))){//设置标题
			headText.setText(bundle.getString("headContent"));
		}
		//1要隐藏 0和null显示(改成1和null显示)
		if(module!=null && module.getIsCancel()!=null && module.getIsCancel()==0){
			cancelLayout.setVisibility(View.GONE);
		}
		taskId = bundle.getInt("taskId");//获取任务ID
		targetId = bundle.getInt("targetId");//获取数据ID
		createDate.setText(bundle.getString("createTime"));//获取创建日期
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
			if(i==0){//第一项
				if(funcItem.getCheckType()!=null && (funcItem.getCheckType()==Func.CHECK_TYPE_MOBILE_TELEPHONE || funcItem.getCheckType()==Func.CHECK_TYPE_FIXED_TELEPHONE)){
					firstContent.setAutoLinkMask(Linkify.PHONE_NUMBERS);//自动转手机号码点击它可进入系统拨号界面
				}//如果是手机号码，设置点击直接可拨打
				firstContent.setText(funcItem.getName()+":"+map.get(CacheData.DATA_N+funcItem.getFuncId()));
			} else {
				DoubleDetailItem item=new DoubleDetailItem(this);//添加一个双向内容view
				item.setTitle(funcItem.getName());//设置标题
				item.setContent(map.get(CacheData.DATA_N+funcItem.getFuncId()),funcItem);//设置内容
				doubleDetailLayout.addView(item.getView());//将view添加到父view中
			}
		}
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
		case R.id.ll_double_detail_data:
			executeDouble();//执行
			break;
		case R.id.ll_double_detail_data_cancle:
			cancelDouble();//取消
			break;
		default:
			break;
		}

	}

	/**
	 * 点击执行按钮跳转到ModuleFuncActivity
	 */
	private void executeDouble(){
		Intent intent=new Intent(DoubleDetailActivity.this,ModuleFuncActivity.class);
		Bundle bundle_ = new Bundle();
		bundle_.putInt("targetId",targetId);//数据ID
		bundle_.putInt("taskId", taskId);//任务ID
		bundle_.putSerializable("module", module);//自定义双向实例
		bundle_.putInt("menuType", Menu.TYPE_MODULE);//模块类型自定义模块
		intent.putExtra("bundle", bundle_);
		intent.putExtra("isNoWait", isNoWait);
		startActivityForResult(intent,104);
	}
	/**
	 * 点击删除按钮弹出删除原因对话框
	 */
	private void cancelDouble(){
		deleteTempStore().show();
	}
	
	/**
	 * 删除双向任务的时候弹出删除原因对话框
	 * @return
	 */
	private Dialog deleteTempStore(){
		final Dialog dialog=new Dialog(this, R.style.transparentDialog);
		View view=View.inflate(this, R.layout.dialog_delte_tempstore, null);
		
		//删除提示内容TextView
		TextView  tvContent = (TextView) view.findViewById(R.id.tv_back_content);
		//设置删除提示内容TextView不显示
		tvContent.setVisibility(View.GONE);
		//店面取消的理由EditText 
		etContent = (EditText) view.findViewById(R.id.et_delete_reason);
		etContent.setHint(getResources().getString(R.string.please_input_reason_for_cancel));
		//设置店面取消的理由EditText显示 
		etContent.setVisibility(View.VISIBLE);

		Button confirm=(Button)view.findViewById(R.id.btn_delete_tempstore_confirm);
		Button cancel=(Button)view.findViewById(R.id.btn_delete_tempstore_cancel);
		confirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String deleteReason = etContent.getText().toString();
				if(TextUtils.isEmpty(deleteReason)){//删除理由为空 
					//提示用户输入删除理由
					 ToastOrder.makeText(DoubleDetailActivity.this,getResources().getString(R.string.please_input_reason_for_cancel), ToastOrder.LENGTH_LONG).show();
					 //结束本次点击事件
					 return;
				}
				
				HashMap<String, String> params=new HashMap<String, String>();
				params.put("cancel", deleteReason);//删除原因
				params.put("tableId", targetId+"");//数据ID
				params.put("dataId", taskId+"");//任务ID
				JLog.d(TAG, "cancel==>"+deleteReason);
				JLog.d(TAG, "tableId==>"+targetId);
				JLog.d(TAG, "dataId==>"+taskId);
				
				
				PendingRequestVO vo = new PendingRequestVO();
	        	vo.setContent(module.getName());
	        	Menu menu = new MainMenuDB(DoubleDetailActivity.this).findMenuListByMenuId(targetId);
	        	vo.setTitle(menu!=null?menu.getName():getResources().getString(R.string.cancel_task));
	        	vo.setMethodType(SubmitWorkManager.HTTP_METHOD_POST);
	        	vo.setParams(params);
	        	vo.setType(TablePending.TYPE_DATA);
	        	vo.setUrl(UrlInfo.getUrlDeleteExecute(DoubleDetailActivity.this));
	    		new CoreHttpHelper().performJustSubmit(DoubleDetailActivity.this,vo);
				new DoubleDB(DoubleDetailActivity.this).removeDoubleTask(taskId+"", targetId+"");//提交后删除该双向任务
				dialog.dismiss();
				DoubleDetailActivity.this.finish();
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
	
//	@Override
//	protected void onStart() {
//		super.onStart();
//		JLog.d("jishen", "isDouble:onStart:"+Constants.ISDOUBLERETURN);
//		if(Constants.ISDOUBLERETURN){//true表示是双向提交返回 false 不是双向提交返回
//			this.finish();//双向提交返回的直接关闭本页面
//			Constants.ISDOUBLERETURN=false;
//		}
//	}
//	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == R.id.submit_succeeded) {
			this.finish();
		}
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
