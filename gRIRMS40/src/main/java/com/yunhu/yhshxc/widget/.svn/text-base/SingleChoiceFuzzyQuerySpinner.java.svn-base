package com.yunhu.yhshxc.widget;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.listener.OnSingleChoiceConfirmListener;
import com.yunhu.yhshxc.utility.PublicUtils;

/**
 * 模糊搜索单选下拉框
 * @author jishen
 */
public class SingleChoiceFuzzyQuerySpinner extends Spinner {

	private EditText etOther  = null;//其他输入框
	public String otherVaule = null;//其他值
	private Context context  = null;
	private int currentPosition = -1;//当前选择的位置

	private List<Dictionary> srcDictList  = null; //本下拉类表数据源

	private OnSingleChoiceConfirmListener onSingleChoiceConfirmListener = null;

	public SingleChoiceFuzzyQuerySpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}
	
	/**
	 * 创建下拉框
	 */
	public void create(){
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,R.layout.single_choice_view);
		if(srcDictList != null && currentPosition != -1){//显示已经选择的值
			String did = srcDictList.get(currentPosition).getDid();
			String value = srcDictList.get(currentPosition).getCtrlCol();
			if(did.equals("-1")){//其他
				value = otherVaule;
			}
			adapter.add(value);
		}else{//没选择值就显示请选择
			adapter.add(PublicUtils.getResourceString(context,R.string.toast_one3));
		}
		this.setAdapter(adapter);
		
	}

	/*
	 * 如果视图定义了OnClickListener监听器，调用此方法来执行
	 * @see android.widget.Spinner#performClick()
	 */
	@Override
	public boolean performClick() {
		showFuzzyQueryDialog();
		//showView(currentPosition);
		return true;
	}
	
	/**
	 * 弹出模糊查询窗口
	 */
	private void showFuzzyQueryDialog(){
		final EditText editText = new EditText(context);
		new AlertDialog.Builder(context)
		.setTitle(R.string.input_search_content).setView(editText)
        .setPositiveButton(R.string.Confirm, new DialogInterface.OnClickListener() {
              
            @Override  
            public void onClick(DialogInterface dialog, int which) {
            	if (onSingleChoiceConfirmListener != null) {
            		String fuzzy = editText.getText().toString();
					onSingleChoiceConfirmListener.fuzzySearch(fuzzy);
				}
            	
            	if(currentPosition != -1 && (srcDictList==null || srcDictList.isEmpty() || srcDictList.size()<currentPosition)){
            		currentPosition=-1;
        		}
            	showListView(currentPosition);
            	
            }  
        })  
        .setNegativeButton(R.string.Cancle, new DialogInterface.OnClickListener() {
              
            @Override  
            public void onClick(DialogInterface dialog, int which) {  
            	
            }  
        }).show();  
	}
	
	/**
	 * @return 数据源
	 */
	private String[] getDataSrc(){
		int size = srcDictList==null ? 0 : srcDictList.size();
		String []dataSrc = new String[size];
		for(int i=0; i<size; i++){
			dataSrc[i] = srcDictList.get(i).getCtrlCol();
		}
		return dataSrc;
	}

	/**
	 * 显示下拉数据列表
	 * @param positon 当前选择的位置
	 */
	private void showListView(int positon){
		final String dataSrc[] = getDataSrc();
		final ArrayAdapter<String> adapter = ((ArrayAdapter<String>)this.getAdapter());
		
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setSingleChoiceItems(dataSrc, currentPosition,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						
						currentPosition=which;
						String did = srcDictList.get(which).getDid();

						if (did.equals("-1")) {//选择其他
							otherVaule = etOther.getText().toString();
							etOther.setVisibility(View.VISIBLE);//其他输入框设置可见
						}else{
							etOther.setVisibility(View.GONE);//没选择其他就隐藏其他输入框
						}
					}
				});

		builder.setPositiveButton(R.string.Confirm, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				if(currentPosition!=-1){//如果有选择
					String did = srcDictList.get(currentPosition).getDid();//获取选择的did
					String str = srcDictList.get(currentPosition).getCtrlCol();//获取选择的值
					String restr = srcDictList.get(currentPosition).getCtrlCol();//获取选择的值
					if (did.equals("-1")) {//选择其他
						did = etOther.getText().toString();
						str = did;
						otherVaule=did;
					}
					if (onSingleChoiceConfirmListener != null) {
						onSingleChoiceConfirmListener.OnConfirm(did,restr,etOther.isShown());//回调
					}
					
					adapter.clear();
					adapter.add(str);
					adapter.notifyDataSetChanged();
				}
			}
		});
		etOther = new EditText(context);
		etOther.setVisibility(View.GONE);//默认设置其他输入框不可见
		builder.setView(etOther);
		if(currentPosition != -1 && srcDictList!=null && !srcDictList.isEmpty() && srcDictList.size() - 1 >=currentPosition){
			String did = srcDictList.get(currentPosition).getDid();
			if(did.equals("-1")){//如果选择了其他就显示其他数据框
				etOther.setVisibility(View.VISIBLE);
				etOther.setText(otherVaule);
			}else{
				etOther.setVisibility(View.GONE);
			}	
		}
		builder.setNegativeButton(R.string.Cancle, null);
		builder.create().show();
	}
	

	/**
	 * 设置其他值
	 * @param otherVaule 值
	 */
	public void setOtherVaule(String otherVaule) {
		this.otherVaule = otherVaule;
	}

	/**
	 * 设置当前选中的位置
	 * @param currentPosition 位置
	 */
	public void setCurrentPosition(int currentPosition) {
		this.currentPosition = currentPosition;
	}

	public void setOnSingleChoiceConfirmListener(
			OnSingleChoiceConfirmListener onSingleChoiceConfirmListener) {
		this.onSingleChoiceConfirmListener = onSingleChoiceConfirmListener;
	}

	/**
	 * 设置数据源
	 * @param srcDictList数据源
	 */
	public void setSrcDictList(List<Dictionary> srcDictList) {
		this.srcDictList = srcDictList;
	}
}
