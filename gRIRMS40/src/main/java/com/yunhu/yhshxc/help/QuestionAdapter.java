package com.yunhu.yhshxc.help;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yunhu.yhshxc.R;

/**
 * 常见问题显示数据的ListView所用的Adapter
 * 继承自android.widget.BaseAdapter
 * 
 * @see BaseAdapter
 * 
 * @version 2013.5.23
 * @author wangchao
 *
 */
public class QuestionAdapter extends BaseAdapter{
	/**
	 * 存储常见问题数据的List
	 */
	private ArrayList<String> dataSrcList = null;
	private Context context = null;
	/**
	 * 构造方法
	 */
	public QuestionAdapter(Context context) {
		this.context = context;
	}

	/**
	 * 获取数据的总数
	 * 
	 * @return 返回数据总数
	 */
	@Override
	public int getCount() {
		return dataSrcList.size();
	}

	/**
	 * 获取指定位置的数据
	 * 
	 * @param position 指定位置
	 * @return 返回指定位置的数据
	 */
	@Override
	public Object getItem(int position) {
		return dataSrcList.get(position);
	}

	/**
	 * 获取数据的ID（即position值）
	 * 
	 * @param position 数据位置
	 * @return 返回position值
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * Adapter模式由系统调用的方法，用于创建和更新ListView中的Item
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
//		LinearLayout item = null;
//		if(convertView == null){
//			item = new LinearLayout(context);
//			item.setPadding(3, 3, 3, 3);
//			TextView tv = new TextView(context);
//			int height = (Double.valueOf(Math.floor(Constants.SCREEN_HEIGHT/10))).intValue();
//			tv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, height));
//			if(position % 2 == 0){
//				tv.setBackgroundColor(context.getResources().getColor(R.color.notify_list));
//			}else{
//				tv.setBackgroundColor(context.getResources().getColor(R.color.Light_Blue_bg));
//			}
//			tv.setTextColor(context.getResources().getColor(android.R.color.white));
//			tv.setTextSize(20);
//			tv.setPadding(10, 10, 10, 10);
//			item.addView(tv);
//		}else{
//			item = (LinearLayout)convertView;
//		}
//		TextView tv = (TextView)item.getChildAt(0);
//		tv.setText(dataSrcList.get(position));
//		return item;
		TextView tv=null;
		if (convertView==null) {
			convertView=View.inflate(context, R.layout.question_common_item, null);
			tv = (TextView) convertView.findViewById(R.id.question_common_item_content);
		     convertView.setTag(tv);
		}else{
			tv=(TextView) convertView.getTag();
		}
		tv.setText(dataSrcList.get(position));
		return convertView;
	}

	/**
	 * 获取常见问题数据List
	 * @return 返回常见问题数据List
	 */
	public ArrayList<String> getDataSrcList() {
		return dataSrcList;
	}

	/**
	 * 传入常见问题数据List
	 * @param dataSrcList 常见问题数据List
	 */
	public void setDataSrcList(ArrayList<String> dataSrcList) {
		this.dataSrcList = dataSrcList;
	}

}
