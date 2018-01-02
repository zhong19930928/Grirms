package com.yunhu.yhshxc.widget;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.utility.PublicUtils;

public class DropDown extends Spinner{
	
	public static final int SINGLE_CHOICE = 1;
	public static final int SINGLE_CHOICE_FUZZY_QUERY = 2;
	public static final int MULTI_CHOICE = 3;
	
	private List<Dictionary> srcDictList  = null; //本下拉类表数据源
	private int currentPosition = -1;
	private int mode = SINGLE_CHOICE;
	private SelectDialog selectDialog;
	
	private OnResultListener onResultListener;
	
	public interface OnResultListener{
		public void onResult(List<Dictionary> result);
	}
	
	public DropDown(Context context,int mode) {
		super(context);
		this.mode = mode;
		init();
	}
	
	 public DropDown(Context context, AttributeSet attrs) {
	    super(context, attrs);
	    TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.Dropdown);
	    this.mode = a.getInteger(R.styleable.Dropdown_ddMode, SINGLE_CHOICE);
		init();
	 }
	 
	private void init() {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				this.getContext(), R.layout.single_choice_view);
		if(srcDictList != null && currentPosition != -1){
			//String did = srcDictList.get(currentPosition).getDid();
			String value = srcDictList.get(currentPosition).getCtrlCol();
			adapter.add(value);
		}else{
			adapter.add(PublicUtils.getResourceString(getContext(),R.string.toast_one3));
		}
		this.setAdapter(adapter);

		selectDialog = new SelectDialog(this.getContext());
	}
	
	/**
	 * 设置字体颜色为白色字体
	 */
	public void initWhite(){
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				this.getContext(), R.layout.single_choice_view_white);
		if(srcDictList != null && currentPosition != -1){
			//String did = srcDictList.get(currentPosition).getDid();
			String value = srcDictList.get(currentPosition).getCtrlCol();
			adapter.add(value);
		}else{
			adapter.add(PublicUtils.getResourceString(getContext(),R.string.toast_one3));
		}
		this.setAdapter(adapter);

		selectDialog = new SelectDialog(this.getContext());
	}
	
	/**
	 * 刷新Spinner控件 
	 */
	public void refreshComp(String strin){
		//String str = srcDictList.get(currentPosition).getCtrlCol();
		ArrayAdapter<String> adapter = ((ArrayAdapter<String>)this.getAdapter());
		adapter.clear();
		if(!TextUtils.isEmpty(strin)){
			adapter.add(strin);
		}else{
			adapter.add(PublicUtils.getResourceString(getContext(),R.string.toast_one3));
		}
		adapter.notifyDataSetChanged();
	}
	
	public void setOnFuzzyQueryListener(OnFuzzyQueryListener listener){
		selectDialog.setOnFuzzyQueryListener(listener);
	}
	
	public void setOnResultListener(OnResultListener listener) {
		this.onResultListener = listener;
	}

	public List<Dictionary> getSrcDictList() {
		return srcDictList;
	}
	
	/**
	 * 重选(删除选中)
	 * @param
	 */
	public void resetChoose(){
		currentPosition = -1;
		init();
	}
	
	/**
	 * 设置选中
	 * @param seleced
	 */
	public void setSelected(Dictionary seleced){
		selectDialog.setSelected(seleced);
		refreshComp(seleced.getCtrlCol());
		
	}
	
	/**
	 * 设置选中（用于多选）
	 * @param selecedList
	 */
	public void setSelected(List<Dictionary> selecedList){
		selectDialog.setSrcSelected(selecedList);
		selectDialog.setSelected(selecedList);
		//refreshComp(seleced.get);
	}
	
	/**
	 * 设置数据源
	 * @param srcDictList
	 */
	public void setSrcDictList(List<Dictionary> srcDictList) {		
		this.srcDictList = srcDictList;
		notifyDataSetChanged();
	}
	
	/**
	 * 刷新弹出数据源列表
	 */
	public void notifyDataSetChanged(){
		selectDialog.notifyDataSetChanged();
	}
	
	@Override
	public boolean performClick() {
		if(mode == SINGLE_CHOICE || mode == SINGLE_CHOICE_FUZZY_QUERY){
			selectDialog.setCancelable(true);
		}else{
			selectDialog.setCancelable(false);
		}
		selectDialog.show();
		return true;
	}
	
	/**
	 * 下拉框弹出的ListView
	 * 其中数据源采用全局的srcDictList；
	 * 
	 * 选中的结果在 selecedItemList中
	 * 
	 * @author david.hou
	 *
	 */
	private ListView listView = null;
	class SelectDialog extends Dialog implements OnClickListener,OnItemClickListener,TextWatcher{

//		private ListView listView;
		private EditText editText;
		
		private LinearLayout ll_fuzzyQuery;
		private LinearLayout ll_list;
		private LinearLayout ll_btn;
		private SelectDialogAdapter adapter;
		
		private OnFuzzyQueryListener onFuzzyQueryListener;
		
		private List<Dictionary> selecedItemList = new ArrayList<Dictionary>();
		private List<Dictionary> srcSelectItemList = new ArrayList<Dictionary>();

		protected SelectDialog(Context context) {
			super(context, R.style.CustomeDropDownListDialog);
			adapter = new SelectDialogAdapter();
			listView = new ListView(this.getContext());
			listView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			listView.setVerticalScrollBarEnabled(false);
			listView.setOnItemClickListener(this);
			listView.setAdapter(adapter);
			
		}
		
		
		/**
		 * 设置已选择
		 * @param dict
		 */
		public void setSelected(Dictionary dict){
			selecedItemList = new ArrayList<Dictionary>();
			selecedItemList.add(dict);

		}
		
		/**
		 * 设置已选择（用于多选）
		 * @param
		 */
		public void setSelected(List<Dictionary> selecedItemList){
			this.selecedItemList.clear();
			this.selecedItemList.addAll(selecedItemList);
		}
		
		/**
		 * 设置已选择（用于多选） 用于维护选中源数据，选中后点取消时需要恢复源数据，这时候会用到
		 * @param
		 */
		public void setSrcSelected(List<Dictionary> selecedItemList){
			this.srcSelectItemList.clear();
			this.srcSelectItemList.addAll(selecedItemList);
		}
		
		/**
		 * 输入框注册监听 
		 * @param listener
		 */
		protected void setOnFuzzyQueryListener(OnFuzzyQueryListener listener){
			this.onFuzzyQueryListener = listener;
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			this.setContentView(R.layout.custom_drop_down_list);
			
			ll_fuzzyQuery = (LinearLayout)this.findViewById(R.id.ll_fuzzyQuery);
			ll_list = (LinearLayout)this.findViewById(R.id.ll_list);
			ll_btn = (LinearLayout)this.findViewById(R.id.ll_btn);
			
			editText = (EditText)this.findViewById(R.id.editText);
			editText.addTextChangedListener(this);
			
			Button confirmBtn = (Button)findViewById(R.id.btn_confirm);
			Button cancelBtn = (Button)findViewById(R.id.btn_cancel);
			confirmBtn.setOnClickListener(this);
			cancelBtn.setOnClickListener(this);
			
			if(mode == SINGLE_CHOICE){
				ll_fuzzyQuery.setVisibility(View.GONE);
				ll_btn.setVisibility(View.GONE);
				listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			}
			if(mode == SINGLE_CHOICE_FUZZY_QUERY){
				ll_btn.setVisibility(View.GONE);
				listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			}
			if(mode == MULTI_CHOICE){
				ll_fuzzyQuery.setVisibility(View.GONE);
				ll_btn.setVisibility(View.VISIBLE);
				listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			}
		}

		@Override
		protected void onStart() {
			super.onStart();
			ll_list.removeAllViews();
			if(adapter.getCount() > 0){
				ll_list.addView(listView);
			}else{
				TextView text = new TextView(this.getContext());
				text.setText(R.string.work_plan_c26);
				text.setTextSize(20);
				text.setTextColor(android.graphics.Color.GRAY);
				ll_list.addView(text);
			}
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			currentPosition = position;
			Dictionary dict = (Dictionary)view.getTag();
			
			if(mode == SINGLE_CHOICE || mode == SINGLE_CHOICE_FUZZY_QUERY){
				selecedItemList = new ArrayList<Dictionary>();
				selecedItemList.add(dict);
				confirm();
			}else{
				if(selecedItemList == null){
					selecedItemList = new ArrayList<Dictionary>();
				}
				if (selecedItemList.contains(dict)) {//如果有了说明已经添加过，此次是取消选中 否则是选中
					selecedItemList.remove(dict);
				}else{
					selecedItemList.add(dict);
				}
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if(onFuzzyQueryListener != null){
				onFuzzyQueryListener.onTextChanged(s);
			}
		}

		@Override
		public void afterTextChanged(Editable s) {
			
		}

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.btn_confirm:
				confirm();
				break;
			case R.id.btn_cancel:
				cancelBtn();
				break;
			}
			
		}
		
		/**
		 *确认选择完成 
		 */
		private void confirm(){
			if(onResultListener != null){
				onResultListener.onResult(selecedItemList);
			}
			String str = "";
			for(Dictionary d:selecedItemList){
				str = str+","+d.getCtrlCol();
			}
			if(str.length()>0){
				refreshComp(str.substring(1));
			}
//			selecedItemList = null;
			setSrcSelected(selecedItemList);
			this.dismiss();
		}
		
		/**
		 * 选取消按钮
		 */
		private void cancelBtn(){
			setSelected(srcSelectItemList);
			this.notifyDataSetChanged();
			this.dismiss();
		}
		
		/**
		 * 刷新listView
		 */
		public void notifyDataSetChanged(){
			((SelectDialogAdapter)listView.getAdapter()).notifyDataSetChanged();
		}

		public List<Dictionary> getSelecedItemList() {
			return selecedItemList;
		}
	}
	
	public interface OnFuzzyQueryListener{
		public void onTextChanged(CharSequence s);
	}
	
	/**
	 * 下拉列表的Adapter
	 * 
	 * @author david.hou
	 *
	 */
	class SelectDialogAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return srcDictList != null ? srcDictList.size() : 0;
		}

		@Override
		public Object getItem(int position) {
			return srcDictList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Dictionary dict = srcDictList.get(position);
			CheckedTextView textView;
			if(convertView == null){
				convertView = View.inflate(getContext(), R.layout.custom_drop_down_list_item, null);
			}
			textView = (CheckedTextView)convertView;
			textView.setText(dict.getCtrlCol());
			textView.setTag(dict);
			List<Dictionary> selectedList = selectDialog.getSelecedItemList();
			if(selectedList != null){
				if (selectedList.contains(dict) && !"其他...".equals(dict.getCtrlCol())) {
					listView.setItemChecked(position, true);
				}else{
					listView.setItemChecked(position, false);
				}
				
//				for(Dictionary d : selectedList){
//					if(dict.equals(d)){
//						listView.setItemChecked(position, true);
//					}
//				}
			}
//			listView.setItemChecked(3, true);

			return convertView;
		}
		
	}
	
}
