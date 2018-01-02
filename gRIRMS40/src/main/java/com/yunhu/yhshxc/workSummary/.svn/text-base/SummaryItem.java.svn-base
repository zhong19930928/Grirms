package com.yunhu.yhshxc.workSummary;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.workplan.RatingBar;
import com.yunhu.yhshxc.workplan.bo.WorkPlanModle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * 
 *
 */
public class SummaryItem {
	private Context mContext;
	private View view;
	private LinearLayout ll_content_item;// 可编辑框布局
	private LinearLayout ll_edit_item;// 新建编辑框布局
	private TextView workpan_item_name;//计划名称
	private TextView workpan_item_edit;//编辑按钮
	private TextView workpan_item_type;//计划类型
	private TextView workpan_item_title;//计划内容
	private TextView workpan_item_beizhu;//备注
	private RatingBar rb_important;//重要等级
	private RatingBar rb_jinji;//紧急等级
	

	public SummaryItem(Context context) {
		mContext = context;
		view = LayoutInflater.from(mContext).inflate(R.layout.activity_workplan_create_new_item, null);
		ll_content_item = (LinearLayout) view.findViewById(R.id.ll_content_item);
		ll_edit_item = (LinearLayout) view.findViewById(R.id.ll_edit_item);

		workpan_item_name = (TextView) view.findViewById(R.id.workpan_item_name);
		workpan_item_edit = (TextView) view.findViewById(R.id.workpan_item_edit);
		workpan_item_type = (TextView) view.findViewById(R.id.workpan_item_type);
		workpan_item_title = (TextView) view.findViewById(R.id.workpan_item_title);
		workpan_item_beizhu = (TextView) view.findViewById(R.id.workpan_item_beizhu);
		rb_important = (RatingBar) view.findViewById(R.id.rb_important);
		rb_jinji = (RatingBar) view.findViewById(R.id.rb_jinji);

	}
	/**
	 * 获取view视图
	 * @return
	 */
    public View getView(){
    	return view;
    } 
	
	
	
	/**
	 * 控制编辑布局的view显示或者隐藏
	 * 编辑布局隐藏,则保存布局显示,反之亦然
	 */
	public void setSaveViewHidden(boolean flag) {
		if (flag) {
			ll_content_item.setVisibility(View.GONE);
			ll_edit_item.setVisibility(View.VISIBLE);

		} else {
			ll_content_item.setVisibility(View.VISIBLE);
			ll_edit_item.setVisibility(View.GONE);
			rb_important.setClickable(false);
			rb_jinji.setClickable(false);
		}
	}


	/**
	 * 设置保存框的计划名称
	 */
	public void setSavePlanItemName(String itemName) {
		workpan_item_name.setText(itemName);
	}
	/**
	 * 设置保存框计划类型
	 */
	public void setSavePlanType(String typeName) {
		workpan_item_type.setText(typeName);
	}
	/**
	 * 设置保存框的计划内容
	 */
	public void setSavePlanContent(String content) {
		workpan_item_title.setText(content);
	}
	/**
	 * 设置保存框的计划备注
	 */
	public void setSavePlanBeiZhu(String content) {
		workpan_item_beizhu.setText(content);
	}
	/**
	 * 设置保存框的重要等级
	 */
	public void setSavePlanImportant(int  level) {
		rb_important.setStar(level);
	}
	/**
	 * 设置保存框的紧急等级
	 */
	public void setSavePlanUrgent(int  level) {
		rb_jinji.setStar(level);
	}

	/**
	 * 设置保存框的编辑
	 */
	public void setEditClickListener(OnClickListener listener) {
		workpan_item_edit.setOnClickListener(listener);
	}
	

   /**
    * 保存框设置所有内容
    */
   public void setAllSaveInfo(WorkPlanModle modle){
	   workpan_item_name.setText(modle.getPlanTitle());
	   workpan_item_title.setText(modle.getPlanContent());
	   workpan_item_beizhu.setText(modle.getPlanMark());
	   rb_important.setStar(modle.getImportantLevel());
	   rb_jinji.setStar(modle.getRushLevel());
	   
	   
   }
  
   
   /**
    * 把保存框控件所有数据获取出来
    * 返回一个数据对象
    */
   public WorkPlanModle getAllSaveData(){

	   WorkPlanModle modle = new WorkPlanModle();
	   modle.setPlanTitle(workpan_item_name.getText().toString().trim());
	   modle.setPlanContent(workpan_item_title.getText().toString().trim());
	   modle.setPlanMark(workpan_item_beizhu.getText().toString().trim());
	   modle.setImportantLevel(rb_important.getSelectRatingCount());
	   modle.setRushLevel(rb_jinji.getSelectRatingCount());
	   return modle;
   }
   
	
}
