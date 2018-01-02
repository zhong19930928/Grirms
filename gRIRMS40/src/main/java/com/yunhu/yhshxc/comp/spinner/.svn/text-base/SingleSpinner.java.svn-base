package com.yunhu.yhshxc.comp.spinner;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yunhu.yhshxc.R;

public class SingleSpinner extends AppCompatSpinner {
	public String text = "";//多选下拉框展示的文本
	private boolean[] chooseStatus; //选中状态数组
	private String[] dataSrc;//数据源\
	String [] currentArr ;
	private Context context;
	private MultiAdapter multiAdapter;
	private AlertDialog multiDialog;
	private OnSingleSpinnerSeclectLisner onMultiChoiceConfirmListener = null;//按钮监听
	//记录已经选择过的
	private String selected="";
	public SingleSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	/**
	 * 创建下拉框
	 */
	public void create(){
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,R.layout.single_choice_view);
		if(!TextUtils.isEmpty(text)){//有值说明已经选择过，就显示选择过的值
			adapter.add(text);
		}else{//没选择过显示请选择
			adapter.add(context.getResources().getString(R.string.please_select));
		}
		this.setAdapter(adapter);
		
	}
	public void setOnMultiChoiceConfirmListener(
			OnSingleSpinnerSeclectLisner onMultiChoiceConfirmListener) {
		this.onMultiChoiceConfirmListener = onMultiChoiceConfirmListener;
	} 
	
    /*
     * 如果视图定义了OnClickListener监听器，调用此方法来执行
     * @see android.widget.Spinner#performClick()
     */
	@Override
	public boolean performClick() {
		final StringBuilder sb = new StringBuilder();
		
		View view = LayoutInflater.from(context)
		.inflate(R.layout.comp_dialog_spinner, null);		
		ListView multi_list = (ListView)view.findViewById(R.id.multi_list);
		
		multi_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				if(currentArr!=null){
					if(onMultiChoiceConfirmListener!=null){
						onMultiChoiceConfirmListener.onSingleChoice(parent, view, position, id);
					}
					multiDialog.cancel();
				}
			}
		});
		
		
		currentArr = dataSrc;
		multiAdapter  = new MultiAdapter(currentArr);
		multi_list.setAdapter(multiAdapter);
		
		multiDialog = new AlertDialog.Builder(this.getContext(),R.style.NewMulityDialog)
		.setView(view)
		.setCancelable(true)
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
				viewHold.selectView = (ImageView) convertView.findViewById(R.id.choice_checkbox);
				convertView.setTag(viewHold);
				
			}else{
				viewHold = (ViewHold)convertView.getTag();
			}
//			if(chooseStatus[checkIndex(arr[position])]){
//				viewHold.content.setBackgroundColor(getResources().getColor(R.color.app_color));
//			}else{
//				viewHold.content.setBackgroundColor(getResources().getColor(R.color.white));
//			}
			viewHold.content.setText(arr[position]);
			if (!TextUtils.isEmpty(selected)&&selected.equals(arr[position])){
				viewHold.selectView.setVisibility(View.VISIBLE);
			}else{
				viewHold.selectView.setVisibility(View.INVISIBLE);
			}
			viewHold.content.setBackgroundDrawable(getResources().getDrawable(R.drawable.spinner_choice_bgselector));
			
			
			
			
			return convertView;
		}
		
	}
	class ViewHold{
		TextView content;
		ImageView selectView;
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

	/**
	 * 设置单选选择过的内容,以便展示的时候进行标识
	 * @param sstr
	 */
	public void setSelected(String sstr){
		this.selected=sstr;
	}
	public String getSelected(){
		return selected;
	}

}
