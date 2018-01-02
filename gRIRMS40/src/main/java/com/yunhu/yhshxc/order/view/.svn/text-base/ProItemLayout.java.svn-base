package com.yunhu.yhshxc.order.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.database.DictDB;
import com.yunhu.yhshxc.order.bo.ProductConf;

/**
 * 订单产品布局中，每一级的布局
 * 此布局中包括：textview spinner
 * 
 * @author houyu
 *
 */
public class ProItemLayout extends LinearLayout{
	
	private Spinner spinner=null;
	private TextView textView=null;
	private Context context=null;
	private ProductLayout parent= null; //父布局
	protected String queryWhere = null; //缓存级联下拉列表选中的值 例如：一级值@二级值@三级值
	private ProductConf productConf = null;//字典表结构
	private List<Dictionary> srcList = null;//数据源
	protected int lableColor = Color.BLACK;
	
	private Dictionary seleced;

	/**
	 * 构造函数
	 * @param context
	 * @param parent 父布局
	 * @param productConf 获取字典表的结构
	 */
	public ProItemLayout(Context context,ProductLayout parent,ProductConf productConf,int lableColor) {
		super(context);
		this.context = context;
		this.parent = parent;
		this.productConf = productConf;
		this.lableColor = lableColor;
		init();
	}
	
	/**
	 *初始化子控件 
	 */
	private void init(){
		this.setOrientation(LinearLayout.HORIZONTAL);
		this.setGravity(Gravity.CENTER_VERTICAL);
		spinner = new Spinner(context);
		textView = new TextView(context);
		
		//将控件添加到本布局中
		//需要权重分配所占空间
		this.addView(textView,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 2.0f));
		this.addView(spinner,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1.0f));
		
		//设置标签
		textView.setText(productConf.getName());
		textView.setTextColor(lableColor);
		textView.setTextSize(16);
		textView.setLines(2);
		//设置下拉列表
		spinner.setOnItemSelectedListener(listener);
		initSrcList();
		//setAdapter();
	}
	
	/**
	 * 初始化数据源，为数据源添加一个“请选择”的元素
	 * 
	 */
	private void initSrcList(){
		Dictionary dict = new Dictionary();
		dict.setCtrlCol("--请选择--");
		dict.setDid("-1");
		srcList = new ArrayList<Dictionary>();
		srcList.add(dict);
	}
	
	/**
	 * 选中后回调
	 */
	private OnItemSelectedListener listener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> _parent, View view, int position, long id) {
			/**当前下拉列表的值 */
			ArrayAdapter<Dictionary> adapter = (ArrayAdapter<Dictionary>)_parent.getAdapter();
			Dictionary dict = adapter.getItem(position);
			
			//将选中的结果返回给
			//-1”是选中了“请选择”，等于对下拉框没有选择，没有选择返回null
			if("-1".equals(dict.getDid())){
				parent.onItemSelected(null, productConf, queryWhere);
			}else{
				parent.onItemSelected(dict, productConf, queryWhere);
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			
		}
	};
	
	/**
	 * 刷新下拉列表
	 */
	public void refresh(){
		initSrcList(); //初始化数据源
		//根据条件queryWhere获取数据源数据
		List<Dictionary> list = new DictDB(context).findDictList(productConf.getDictTable(), productConf.getDictCols(), productConf.getDictDataId(),productConf.getDictOrderBy(),productConf.getDictIsAsc(), queryWhere, null, null);
		if(list!=null && srcList != null && !srcList.isEmpty()){
			srcList.addAll(list);
		}
		setAdapter();
		if(this.seleced != null){
			setSelected();
		}
	}
	
	public Dictionary getSeleced() {
		return seleced;
	}

	public void setSelected(Dictionary seleced){
		this.seleced = seleced;
	}
	
	public void setSelected(){
		int i = 0;
		for(Dictionary dict : srcList){
			if(dict.getDid().equals(seleced.getDid())){
				spinner.setSelection(i);
				break;
			}
			i++;
		}
	}
	
	/**
	 *为下拉适配器重新赋值 
	 */
	private void setAdapter(){
		ArrayAdapter<Dictionary> adapter= new ArrayAdapter<Dictionary>(getContext(), android.R.layout.simple_spinner_item,srcList);
		adapter.setDropDownViewResource(R.layout.sprinner_check_item2);//自定义的布局
		spinner.setAdapter(adapter);
	}

	/**
	 * 返回当前字典表结构信息
	 * @return
	 */
	public ProductConf getProductConf() {
		return productConf;
	}
}
