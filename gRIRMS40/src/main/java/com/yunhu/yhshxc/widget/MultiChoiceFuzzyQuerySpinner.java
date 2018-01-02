package com.yunhu.yhshxc.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.listener.OnMultiChoiceConfirmListener;
import com.yunhu.yhshxc.utility.PublicUtils;

/**
 * 模糊搜索多选下拉框
 * @author gcg_jishen
 *
 */
public class MultiChoiceFuzzyQuerySpinner extends Spinner {
	
	private Context context = null;
	private String text = null;
	private boolean[] chooseStatus = null;
	private String[] dataSrc = null;
	private OnMultiChoiceConfirmListener onMultiChoiceConfirmListener = null;

	public MultiChoiceFuzzyQuerySpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}
	
	public void create(){
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,R.layout.single_choice_view);
		if(!TextUtils.isEmpty(text)){
			adapter.add(text);
		}else{
			adapter.add(PublicUtils.getResourceString(context,R.string.toast_one3));
		}
		this.setAdapter(adapter);
	}

	
    //如果视图定义了OnClickListener监听器，调用此方法来执行
	@Override
	public boolean performClick() {

		showFuzzyQueryDialog();
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
            	if (onMultiChoiceConfirmListener != null) {
            		String fuzzy = editText.getText().toString();
            		onMultiChoiceConfirmListener.fuzzySearch(fuzzy);
				}
            	showListView();
            }  
        })  
        .setNegativeButton(R.string.Cancle, new DialogInterface.OnClickListener() {
              
            @Override  
            public void onClick(DialogInterface dialog, int which) {  
            	
            }  
        }).show();  
	}
	
	private void showListView(){
		final StringBuilder sb = new StringBuilder();
		DialogInterface.OnMultiChoiceClickListener listener = new DialogInterface.OnMultiChoiceClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which,
					boolean isChecked) {
				if(isChecked){
					chooseStatus[which] = true;
				} else {
					chooseStatus[which] = false;
				}
			}		
			
		};
		new AlertDialog.Builder(this.getContext())
		.setMultiChoiceItems(dataSrc, chooseStatus, listener)
		.setPositiveButton("确定", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				if(chooseStatus!=null){
					if(onMultiChoiceConfirmListener != null){
						onMultiChoiceConfirmListener.OnConfirm(chooseStatus);
					}
					for(int i=0;i<chooseStatus.length;i++){
						if(chooseStatus[i]){
							sb.append(",").append(dataSrc[i]);
						}
					}
					if(sb.length() > 0){
						text = "";
						text = sb.substring(1);
					}else{
						text = "";
					}
					ArrayAdapter<String> adapter = ((ArrayAdapter)MultiChoiceFuzzyQuerySpinner.this.getAdapter());
					adapter.clear();
					adapter.add(MultiChoiceFuzzyQuerySpinner.this.text);
					adapter.notifyDataSetChanged();
				}
			}
			
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {		
				
			}
			
		})
		.setCancelable(false)
		.show();
	}

	public String[] getDataSrc() {
		return dataSrc;
	}

	public void setDataSrc(String[] dataSrc) {
		this.dataSrc = dataSrc;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean[] getChooseStatus() {
		return chooseStatus;
	}

	public void setChooseStatus(boolean[] chooseStatus) {
		this.chooseStatus = chooseStatus;
	}

	public void setOnMultiChoiceConfirmListener(
			OnMultiChoiceConfirmListener onMultiChoiceConfirmListener) {
		this.onMultiChoiceConfirmListener = onMultiChoiceConfirmListener;
	} 
}
