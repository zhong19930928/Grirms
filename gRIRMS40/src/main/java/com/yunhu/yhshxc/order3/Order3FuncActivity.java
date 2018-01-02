package com.yunhu.yhshxc.order3;

import gcg.org.debug.JLog;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.LinkFuncActivity;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.bo.Submit;
import com.yunhu.yhshxc.bo.SubmitItem;
import com.yunhu.yhshxc.database.FuncDB;
import com.yunhu.yhshxc.database.SubmitDB;
import com.yunhu.yhshxc.database.SubmitItemDB;
import com.yunhu.yhshxc.exception.DataComputeException;
import com.yunhu.yhshxc.func.AbsFuncActivity;
import com.yunhu.yhshxc.func.FuncTree;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.widget.ToastOrder;

public class Order3FuncActivity extends AbsFuncActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		init();
		super.onCreate(savedInstanceState);
		isLinkActivity=false;//不是超链接
		TextView title=(TextView)findViewById(R.id.tv_titleName);
		title.setText("上报");

	
	}
	
	
	/**
	 * 获取传过来的值
	 */
	private void init(){
		bundle = getIntent().getBundleExtra("bundle");
		targetId = bundle.getInt("targetId");//数据ID
		bundle.putInt("targetType", Menu.TYPE_ORDER3);
	}

	@Override
	protected void intoLink(Bundle linkBundle) {
		Intent linkIntent=new Intent(this,LinkFuncActivity.class);
		linkIntent.putExtra("bundle", linkBundle);
		startActivity(linkIntent);
	}

	@Override
	public List<Func> getFuncList() {
		FuncDB funcDB=new FuncDB(this);
		List<Func> funcList = funcDB.findFuncListByTargetidReplash(targetId,"0",false);
		return funcList;
	}

	@Override
	public List<Func> getButtonFuncList() {
		List<Func> buttonFuncList=new FuncDB(this).findButtonFuncListReplenish(targetId,"0");
		return buttonFuncList;
	}

	@Override
	public Func getFuncByFuncId(int funcId) {
		Func func =new FuncDB(this).findFuncByFuncId(funcId);
		return func;
	}

	@Override
	public Integer[] findFuncListByInputOrder() {
		Integer[] inputOrderArr =new FuncDB(this).findFuncListByInputOrder(targetId);
		return inputOrderArr;
	}

	@Override
	public ArrayList<Func> getOrderFuncList(Integer funcId, int inputOrder) {
		ArrayList<Func> orderList = new FuncDB(this).findFuncListByInputOrder(targetId, funcId, inputOrder);
		return orderList;
	}

	@Override
	public void showPreview() {

		SubmitDB submitDB=new SubmitDB(this);
		int submitId = submitDB.selectSubmitId(planId, wayId, storeId,targetId, menuType);
		Submit submit=submitDB.findSubmitById(submitId);
		if(submit!=null && modType==Constants.MODULE_TYPE_REASSIGN && TextUtils.isEmpty(submit.getSendUserId())){//改派的时候如果改派的用户ID为空说明没选择要改派给谁，此时提示用户
			ToastOrder.makeText(this, getResources().getString(R.string.reassign_user),ToastOrder.LENGTH_LONG).show();
		}else{
			if (submit==null) {//等于空的时候要插入一条submit
				submit = new Submit();
				submit.setPlanId(planId);
				submit.setState(Submit.STATE_NO_SUBMIT);
				submit.setStoreId(storeId);
				submit.setWayId(wayId);
				submit.setTargetid(targetId);
				submit.setTargetType(menuType);
				if(modType!=0){
					submit.setModType(modType);
				}
				submit.setMenuId(menuId);
				submit.setMenuType(menuType);
				submit.setMenuName(menuName);
				submitDB.insertSubmit(submit);
				submitId = submitDB.selectSubmitIdNotCheckOut(planId, wayId,storeId, targetId, menuType,Submit.STATE_NO_SUBMIT);
			}
			
			intentToDetail(submitId);
		}
		
	
	}
	
	/**
	 * 跳转到详细预览页面
	 * @param submitId 提交表单的表单ID
	 */
	private void intentToDetail(int submitId){
		Intent intent = new Intent(this, Order3FuncDetailActivity.class);
		intent.putExtra("submitId", submitId);
		intent.putExtra("linkId", linkId);// 超链接的时候把上级SubimtID传过去
		intent.putExtra("taskId", taskId);// 双向模块的时候要传到服务端
		intent.putExtra("targetId", targetId);//数据ID
		intent.putExtra("modType", modType);//模块类型
		intent.putExtra("menuType", menuType);
		intent.putExtra("priviewBundle", bundle);
		intent.putExtra("codeSubmitControl", codeSubmitControl);//串码提交控制
		intent.putExtra("isNoWait", isNoWait);//是否提交无等待
		intent.putExtra("bundle", bundle);
		try {
			controlHidden(submitId);//存储隐藏域
			startActivityForResult(intent,R.id.submit_succeeded);
		} catch (NumberFormatException e) {//下拉框其他计算格式出错异常
			ToastOrder.makeText(this,R.string.toast_one1, ToastOrder.LENGTH_SHORT).show();
			JLog.e(e);
		} catch (DataComputeException e) {
			ToastOrder.makeText(getApplicationContext(), e.getMessage(), ToastOrder.LENGTH_LONG).show();
			JLog.e(e);
		}
	}
	
	/**
	 * 控制隐藏域
	 * @throws DataComputeException 
	 * @throws NumberFormatException 
	 */
	private void controlHidden(int submitId) throws NumberFormatException, DataComputeException{
		// 存储隐藏类型的
		List<Func> hiddenList = new FuncDB(this).findFuncListByType(targetId,Func.TYPE_HIDDEN,null);
		List<Func> hiddenSql=new ArrayList<Func>();
		if (hiddenList != null) {
			for (int i = 0; i < hiddenList.size(); i++) {
				Func func = hiddenList.get(i);
				if(func.getDefaultType()!=null && func.getDefaultType()==Func.DEFAULT_TYPE_COMPUTER){//计算类型的隐藏域先存储 最后在计算值 否则计算的值可能不正确
					hiddenSql.add(func);
				}else{//保存隐藏域的值
					saveHideFunc(func,submitId);
				}
			}
		}
		
		if(!hiddenSql.isEmpty()){
			for (int i = 0; i < hiddenSql.size(); i++) {//存储计算隐藏域
				Func func = hiddenSql.get(i);
				saveHideFunc(func,submitId);
			}
		}
		
		SubmitItemDB sItemDb = new SubmitItemDB(this);
		List<Func> list=getFuncList();
		for (int i = 0; i < list.size(); i++) {
			Func textFunc=list.get(i);
			if(textFunc.getType()==Func.TYPE_TEXTCOMP){
				SubmitItem item=sItemDb.findSubmitItemBySubmitIdAndFuncId(submitId, textFunc.getFuncId());
				if(item!=null){
					String value=new FuncTree(this,textFunc,submitId).result;
					item.setParamValue(value);
					sItemDb.updateSubmitItem(item,false);
				}
			}
		}
		
	}	

	@Override
	protected void onClickBackBtn() {
		cleanCurrentNoSubmit(targetId);//清除未提交的数据
		this.finish();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	/**
	 * 读取数据-->非正常状态下使用
	 */
	@Override
	protected void restoreConstants(Bundle savedInstanceState) {
		super.restoreConstants(savedInstanceState);
	}
}
