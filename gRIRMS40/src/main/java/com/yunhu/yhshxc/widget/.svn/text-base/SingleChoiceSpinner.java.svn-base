package com.yunhu.yhshxc.widget;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.listener.OnSingleChoiceConfirmListener;
import com.yunhu.yhshxc.utility.PublicUtils;

/**
 * 单选下拉框
 * @author gcg_jishen
 */
public class SingleChoiceSpinner extends AppCompatSpinner {

	private EditText etOther  = null;
	public String otherVaule = null;
	private Context context  = null;
	private int currentPosition = -1;

	private List<Dictionary> srcDictList  = null; //本下拉类表数据源

	public int getCurrentPosition() {
		return currentPosition;
	}

	/**
	 * 设置当前选中的位置
	 * @param currentPosition
	 */
	public void setCurrentPosition(int currentPosition) {
		this.currentPosition = currentPosition;
	}


	private OnSingleChoiceConfirmListener onSingleChoiceConfirmListener = null;

	public SingleChoiceSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	
	public void create(){
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,R.layout.single_choice_view);
		if(srcDictList != null && currentPosition != -1){
			String did = srcDictList.get(currentPosition).getDid();
			String value = srcDictList.get(currentPosition).getCtrlCol();
			if(did.equals("-1")){
				value = otherVaule;
			}
			adapter.add(value);
		}else{
			adapter.add(PublicUtils.getResourceString(context,R.string.toast_one3));
		}
		this.setAdapter(adapter);
		
	}
	
	
	// 如果视图定义了OnClickListener监听器，调用此方法来执行
	@Override
	public boolean performClick() {
		showView(currentPosition);
		return true;
	}

	private String[] getDataSrc(){
		int size = srcDictList==null ? 0 : srcDictList.size();
		String []dataSrc = new String[size];
		for(int i=0; i<size; i++){
			dataSrc[i] = srcDictList.get(i).getCtrlCol();
		}
		return dataSrc;
	}
	
	/**
	 * 显示选项列表
	 * @param positon
	 */
	private void showView(int positon){
		final String dataSrc[] = getDataSrc();
		final ArrayAdapter<String> adapter = ((ArrayAdapter<String>)this.getAdapter());
		
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setSingleChoiceItems(dataSrc, currentPosition,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						
						currentPosition=which;
						String did = srcDictList.get(which).getDid();

						if (did.equals("-1")) {
							otherVaule = etOther.getText().toString();
							etOther.setVisibility(View.VISIBLE);
						}else{
							etOther.setVisibility(View.GONE);
						}
					}
				});

		builder.setPositiveButton(R.string.Confirm, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				if(currentPosition!=-1){
					String did = srcDictList.get(currentPosition).getDid();
					String str = srcDictList.get(currentPosition).getCtrlCol();
					String restr = srcDictList.get(currentPosition).getCtrlCol();
					if (did.equals("-1")) {
						did = etOther.getText().toString();
						str = did;
						otherVaule=did;
					}
					if (onSingleChoiceConfirmListener != null) {
						onSingleChoiceConfirmListener.OnConfirm(did,restr,etOther.isShown());
					}
					
					adapter.clear();
					adapter.add(str);
					adapter.notifyDataSetChanged();
				}
			}
		});
		etOther = new EditText(context);
		etOther.setVisibility(View.GONE);
		builder.setView(etOther);
		if(currentPosition != -1 && srcDictList!=null && !srcDictList.isEmpty() && srcDictList.size()>=currentPosition){
			String did = srcDictList.get(currentPosition).getDid();
			if(did.equals("-1")){
				etOther.setVisibility(View.VISIBLE);
				etOther.setText(otherVaule);
			}else{
				etOther.setVisibility(View.GONE);
			}	
		}
		builder.setNegativeButton(R.string.Cancle, null);
		builder.create().show();
	}
	public void setOtherVaule(String otherVaule) {
		this.otherVaule = otherVaule;
	}

	public void setSrcDictList(List<Dictionary> srcDictList) {
		this.srcDictList = srcDictList;
	}


	public void setOnSingleChoiceConfirmListener(
			OnSingleChoiceConfirmListener onSingleChoiceConfirmListener) {
		this.onSingleChoiceConfirmListener = onSingleChoiceConfirmListener;
	}
	
}
