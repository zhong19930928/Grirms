package com.yunhu.yhshxc.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.listener.OnMultiChoiceConfirmListener;
import com.yunhu.yhshxc.utility.PublicUtils;

/**
 * 多选下拉框
 * @author jishen
 *
 */
public class MultiChoiceSpinner extends Spinner {
	
	public String text = "";//多选下拉框展示的文本
	private boolean[] chooseStatus; //选中状态数组
	private String[] dataSrc;//数据源\
	String [] currentArr ;
	private OnMultiChoiceConfirmListener onMultiChoiceConfirmListener = null;//按钮监听
	private Context context;
	private MultiAdapter multiAdapter;
	private AlertDialog multiDialog;
	
	public MultiChoiceSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
	}
	
	/**
	 * 创建下拉框
	 */
	public void create(){
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,R.layout.single_choice_view);
		if(!TextUtils.isEmpty(text)){//有值说明已经选择过，就显示选择过的值
			adapter.add(text);
		}else{//没选择过显示请选择
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
		if (onMultiChoiceConfirmListener != null) {
    		onMultiChoiceConfirmListener.fuzzySearch(null);
		}
		final StringBuilder sb = new StringBuilder();
		DialogInterface.OnMultiChoiceClickListener listener = new DialogInterface.OnMultiChoiceClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which,
					boolean isChecked) {
				if(isChecked){//如果选中就把状态置为true
					chooseStatus[which] = true;
				} else {
					chooseStatus[which] = false;
				}
			}		
			
		};
		
		View view = LayoutInflater.from(context)
		.inflate(R.layout.multi_choice_search_dialog, null);		
		EditText multi_search_et = (EditText)view.findViewById(R.id.multi_search_et);
		Button btn_back_confirm = (Button)view.findViewById(R.id.btn_back_confirm);
		Button btn_back_cancel = (Button)view.findViewById(R.id.btn_back_cancel);
		ListView multi_list = (ListView)view.findViewById(R.id.multi_list);
		
		multi_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				if(currentArr!=null){
					if(chooseStatus[checkIndex(currentArr[position])]){
						chooseStatus[checkIndex(currentArr[position])] = false;
					}else{
						chooseStatus[checkIndex(currentArr[position])] = true;
					}
					multiAdapter.notifyDataSetChanged();
				}
			}
		});
		btn_back_confirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				if(chooseStatus!=null){
					if(onMultiChoiceConfirmListener != null){//点击确定 回调
						onMultiChoiceConfirmListener.OnConfirm(chooseStatus);
					}
					for(int i=0;i<chooseStatus.length;i++){
						if(chooseStatus[i]){//拼接选择的值
							sb.append(",").append(dataSrc[i]);
						}
					}
					if(sb.length() > 0){
						text = "";
						text = sb.substring(1);//设置选中的值
					}else{
						text = "";
					}
					ArrayAdapter<String> adapter = ((ArrayAdapter)MultiChoiceSpinner.this.getAdapter());
					adapter.clear();
					adapter.add(MultiChoiceSpinner.this.text);
					adapter.notifyDataSetChanged();
				}
				
				multiDialog.cancel();
			}
		});
		btn_back_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				multiDialog.cancel();
			}
		});
		currentArr = dataSrc;
		multiAdapter  = new MultiAdapter(currentArr);
		multi_list.setAdapter(multiAdapter);
		multi_search_et.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				String str = s.toString().trim();
				if(str.equals("")){
					currentArr = dataSrc;
					multiAdapter.setData(currentArr);
					multiAdapter.notifyDataSetChanged();
				}else{
					currentArr = updateArr(str);
					multiAdapter.setData(currentArr);
					multiAdapter.notifyDataSetChanged();
					
					
					}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		
		
		multiDialog = new AlertDialog.Builder(this.getContext(),R.style.NewMulityDialog)
//		.setMultiChoiceItems(dataSrc, chooseStatus, listener)
//		.setPositiveButton("确定", new DialogInterface.OnClickListener(){
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				if(chooseStatus!=null){
//					if(onMultiChoiceConfirmListener != null){//点击确定 回调
//						onMultiChoiceConfirmListener.OnConfirm(chooseStatus);
//					}
//					for(int i=0;i<chooseStatus.length;i++){
//						if(chooseStatus[i]){//拼接选择的值
//							sb.append(",").append(dataSrc[i]);
//						}
//					}
//					if(sb.length() > 0){
//						text = "";
//						text = sb.substring(1);//设置选中的值
//					}else{
//						text = "";
//					}
//					ArrayAdapter<String> adapter = ((ArrayAdapter)MultiChoiceSpinner.this.getAdapter());
//					adapter.clear();
//					adapter.add(MultiChoiceSpinner.this.text);
//					adapter.notifyDataSetChanged();
//				}
//			}
//			
//		})
//		.setNegativeButton("取消", new DialogInterface.OnClickListener(){
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {		
//				
//			}
//			
//		})
		.setView(view)
		.setCancelable(false)
		.show();
		return true;
	}

	public String[] getDataSrc() {
		return dataSrc;
	}

	/**
	 * 多选下拉框设置数据源
	 * @param dataSrc 数据源
	 */
	public void setDataSrc(String[] dataSrc) {
		this.dataSrc = dataSrc;
	}

	public String getText() {
		return text;
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
	
	class MultiAdapter extends BaseAdapter{
		
		String[] arr;
		
		MultiAdapter(String[] arr){this.arr = arr;}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return arr.length;
		}
		
		public void setData(String[] arr){
			this.arr = arr;
		}

		@Override
		public String getItem(int position) {
			// TODO Auto-generated method stub
			return arr[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return arr[position].hashCode();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHold  viewHold;
			if(convertView==null){
				convertView = LayoutInflater.from(context).inflate(R.layout.multi_choice_search_itme, null);
			    viewHold = new ViewHold();
				viewHold.content = (TextView)convertView.findViewById(R.id.content);
				convertView.setTag(viewHold);
				
			}else{
				viewHold = (ViewHold)convertView.getTag();
			}
			if(chooseStatus[checkIndex(arr[position])]){
				viewHold.content.setBackgroundDrawable(getResources().getDrawable(R.drawable.spinner_multiy_bg));
			}else{
				viewHold.content.setBackgroundColor(getResources().getColor(R.color.white));
			}
			viewHold.content.setText(arr[position]);
			
			
			
			
			return convertView;
		}
		
	}
	class ViewHold{
		TextView content;
	}
	
	int checkIndex(String content){
		int i = 0;
		for (; i < dataSrc.length; i++) {
			if(content.equals(dataSrc[i]))
				break;
		}
		return i ;
	}
	
	String[] updateArr(String content){
		
		StringBuffer arrStr = new StringBuffer();
		
		for (int i = 0; i < dataSrc.length; i++) {
			if(dataSrc[i].indexOf(content)>-1){
				arrStr.append(",").append(dataSrc[i]);	
			}				
		}
		
		
		
		if(arrStr.length()>0){
			
		return arrStr.toString().substring(1).split(",") ;
		}else{
			return new String[0];
		}
	}
	
	
}
