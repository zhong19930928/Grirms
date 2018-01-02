package com.yunhu.yhshxc.order.view;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.database.PSSProductConfDB;
import com.yunhu.yhshxc.order.bo.ProductConf;
import com.yunhu.yhshxc.order.listener.OnProductChoosedListener;

/**
 *订单产品布局，插入此布局，所有级联便会显示。
 *通过监听返回它最后一级列表的值 
 * @author houyu 
 *
 */
public class ProductLayout extends LinearLayout{

	private OnProductChoosedListener onProductChoosedListener=null; //监听结果的返回
	private HashMap<String,ProItemLayout> itemLayoutMap = null;  //缓存级联下拉列表控件
	private int lableColor= Color.BLACK;
	private List<ProductConf> productConfList;
	
	/**
	 * 返回选中产品的监听事件
	 * @return
	 */
	public void setOnProductChoosedListener(
			OnProductChoosedListener onProductChoosedListener) {
		this.onProductChoosedListener = onProductChoosedListener;
	}

	/**
	 * xml配置是所调用
	 * @param context
	 * @param attrs
	 */
	public ProductLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	/**
	 * 代码实例时调用
	 * @param context
	 */
	public ProductLayout(Context context) {
		super(context);
		init();
	}
	
	public ProductLayout(Context context,int lableColor) {
		super(context);
		this.lableColor = lableColor;
		init();
	}
	
	/**
	 * 初始化
	 * 1.设置LinearLayout属性
	 * 2.创建级联下拉列表
	 * 
	 * @param context
	 */
	private void init(){
		
		//设置此布局
		this.setOrientation(LinearLayout.VERTICAL); 
		//查询下拉列表配置信息，确定几级级联
		productConfList = getAllSpinnerDataConfInfo();
		itemLayoutMap = new HashMap<String,ProItemLayout>();
		int i=0;
		if(productConfList!=null&&productConfList.size()>0){
			for(ProductConf pc : productConfList){
				//初始化添加下拉列表子布局
				ProItemLayout layout = new ProItemLayout(getContext(),this,pc,lableColor);
				this.addView(layout);
				itemLayoutMap.put(pc.getDictTable(), layout);
				if(i==0){ //只有第一级下拉列表显示数据
					layout.refresh();
				}
				i++;
			}
		}
		
	}
	
	/**
	 * 设置productId后，级联下拉列表分别赋值
	 * @param productId
	 */
	public void setProductId(String productId){
		
	}
	
	/**
	 * 获取级联中所以下拉列表的机构集合
	 * @return
	 */
	public List<ProductConf> getAllSpinnerDataConfInfo(){
		List<ProductConf> productConfList = new PSSProductConfDB(getContext()).findList();
		return productConfList;
	}
	
	/**
	 * 有下拉列表返回，更改下级列表数据
	 * @param selected
	 * @param productConf
	 * @param queryWhere
	 */
	protected void onItemSelected(Dictionary selected,ProductConf productConf,String queryWhere) {
		//Toast.makeText(context, productConf.getName()+"被选中"+(selected==null?"null":selected.getCtrlCol()), Toast.LENGTH_SHORT).show();
		if(TextUtils.isEmpty(productConf.getNext())){
			//返回产品ID
			if(selected == null || TextUtils.isEmpty(selected.getDid())){
				onProductChoosedListener.onOptionsItemChoosed(null);
			}else{
				onProductChoosedListener.onOptionsItemChoosed(selected);
			}
			
		}else{
			/** 改变下级列表*/
			ProductConf nextPc = itemLayoutMap.get(productConf.getNext()).getProductConf(); //找到下级列表的字典表对象
			String nextQueryWhere = null;
			String did = (selected == null || TextUtils.isEmpty(selected.getDid()))?"-1":selected.getDid();
			if(!TextUtils.isEmpty(queryWhere)){ //更改下级列表的查询条件
				nextQueryWhere = queryWhere+"@"+did;
			}else{
				nextQueryWhere = did;
			}
			changeNextSpinner(nextPc,nextQueryWhere);
		}
	}
	
	/**
	 * 改变下级下拉列表的值
	 * @param pc
	 */
	private void changeNextSpinner(ProductConf pc, String queryWhere){
		ProItemLayout itemLayout = itemLayoutMap.get(pc.getDictTable());
		itemLayout.queryWhere = queryWhere; //更改查询条件
		itemLayout.refresh();
	}

	public HashMap<String, ProItemLayout> getItemLayoutMap() {
		return itemLayoutMap;
	}

	public List<ProductConf> getProductConfList() {
		return productConfList;
	}
}
