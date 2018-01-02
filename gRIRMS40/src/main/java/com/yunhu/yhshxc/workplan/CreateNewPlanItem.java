package com.yunhu.yhshxc.workplan;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.workplan.bo.WorkPlanModle;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * 新建工作计划的封装view 包含一个可编辑框和新提交框,控制其显示隐藏
 *
 */
public class CreateNewPlanItem implements TextWatcher {
	private long item_id;// 该view的标识
	private Context mContext;
	private View view;
	private LinearLayout ll_content_item;// 可编辑框布局
	private LinearLayout ll_edit_item;// 新建编辑框布局
	private TextView workpan_item_name;// 计划名称
	private TextView workpan_item_edit;// 编辑按钮

	private TextView workpan_item_title;// 计划内容
	private TextView workpan_item_beizhu;// 备注
	private RatingBar rb_important;// 重要等级
	private RatingBar rb_jinji;// 紧急等级
	private EditText workpan_item_name_et;// 新建计划名称
	private TextView workpan_item_number;

	private EditText workpan_item_content_et;// 新建计划内容
	private EditText workpan_item_beizhu_et;// 新建计划备注
	private RatingBar rb_impor_edt;// 新建计划重要等级
	private RatingBar rb_jinji_edt;// 新建计划紧急等级
	private Button btn_delete_workplan;// 删除按钮
	private Button btn_save_workplan;// 保存安按钮
	private static int whichViewShown = 0;// 当前view哪个布局正在显示 1:编辑框2:保存框

	private boolean isInputContent = false;

	public CreateNewPlanItem(Context context) {
		mContext = context;
		view = LayoutInflater.from(mContext).inflate(R.layout.activity_workplan_create_new_item, null);
		ll_content_item = (LinearLayout) view.findViewById(R.id.ll_content_item);
		ll_edit_item = (LinearLayout) view.findViewById(R.id.ll_edit_item);
		btn_delete_workplan = (Button) view.findViewById(R.id.btn_delete_workplan);
		btn_save_workplan = (Button) view.findViewById(R.id.btn_save_workplan);

		workpan_item_name = (TextView) view.findViewById(R.id.workpan_item_name);
		workpan_item_edit = (TextView) view.findViewById(R.id.workpan_item_edit);
		workpan_item_title = (TextView) view.findViewById(R.id.workpan_item_title);
		workpan_item_beizhu = (TextView) view.findViewById(R.id.workpan_item_beizhu);
		workpan_item_number = (TextView) view.findViewById(R.id.workpan_item_number);
		rb_important = (RatingBar) view.findViewById(R.id.rb_important);
		rb_jinji = (RatingBar) view.findViewById(R.id.rb_jinji);
		rb_jinji_edt = (RatingBar) view.findViewById(R.id.rb_jinji_edt);
		rb_impor_edt = (RatingBar) view.findViewById(R.id.rb_impor_edt);
		rb_impor_edt.setStar(0);
		rb_jinji_edt.setStar(0);
		rb_important.setStar(0);
		rb_jinji.setStar(0);
		workpan_item_name_et = (EditText) view.findViewById(R.id.workpan_item_name_et);
		workpan_item_content_et = (EditText) view.findViewById(R.id.workpan_item_content_et);
		workpan_item_beizhu_et = (EditText) view.findViewById(R.id.workpan_item_beizhu_et);
		workpan_item_name_et.addTextChangedListener(this);
		workpan_item_content_et.addTextChangedListener(this);
		workpan_item_beizhu_et.addTextChangedListener(this);
		// 初始化编辑框按钮隐藏,只有设置监听的时候才会显示
		workpan_item_edit.setVisibility(View.INVISIBLE);

	}
	public TextView getTextNumber(){
		return workpan_item_number;
	}

	/**
	 * 监听计划名称,计划内容,计划备注,是否有输入内容
	 */
	public boolean isInputContent() {

		return isInputContent;
	}

	/**
	 * 获取view视图
	 * 
	 * @return
	 */
	public View getView() {
		return view;
	}

	public long getItemId() {
		return item_id;
	}

	public void setItemId(long item_id) {
		this.item_id = item_id;
	}

	/**
	 * 控制保存框布局的view显示或者隐藏 编辑布局隐藏,则保存布局显示,反之亦然
	 */
	public void setSaveViewHidden(boolean flag) {
		if (flag) {
			ll_content_item.setVisibility(View.GONE);
			ll_edit_item.setVisibility(View.VISIBLE);
			whichViewShown = 1;

		} else {
			ll_content_item.setVisibility(View.VISIBLE);
			ll_edit_item.setVisibility(View.GONE);
			whichViewShown = 2;
			rb_important.setClickable(false);
			rb_jinji.setClickable(false);
		}
	}

	/**
	 * 控制隐藏所有布局
	 */
	public void hiddenAllView(boolean flag) {
		if (flag) {
			ll_content_item.setVisibility(View.GONE);
			ll_edit_item.setVisibility(View.GONE);
		}
	}

	/**
	 * 设置保存框的计划名称
	 */
	public void setSavePlanItemName(String itemName) {
		workpan_item_name.setText(itemName);
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
	public void setSavePlanImportant(int level) {
		rb_important.setStartCount(level);
		rb_important.setStar(level);
		
	}

	/**
	 * 设置保存框的紧急等级
	 */
	public void setSavePlanUrgent(int level) {
		rb_jinji.setStartCount(level);
		rb_jinji.setStar(level);
	}

	/**
	 * 设置编辑框的计划名称
	 */
	public void setEditPlanItemName(String itemName) {
		workpan_item_name_et.setText(itemName);
	}

	/**
	 * 设置编辑框的计划内容
	 */
	public void setEditPlanContent(String content) {
		workpan_item_content_et.setText(content);
	}

	/**
	 * 设置编辑框的计划备注
	 */
	public void setEditPlanBeiZhu(String beizhu) {
		workpan_item_beizhu_et.setText(beizhu);
	}

	/**
	 * 设置编辑框的计划重要等级
	 */
	public void setEditPlanImportsnt(int important) {
			rb_impor_edt.setStar(important);
	}

	/**
	 * 设置编辑框的计划紧急等级
	 */
	public void setEditPlanUrgent(int urgent) {
		rb_jinji_edt.setStar(urgent);
	}

	/**
	 * 设置保存框的编辑
	 */
	public void setEditClickListener(OnClickListener listener) {
		workpan_item_edit.setVisibility(View.VISIBLE);
		workpan_item_edit.setOnClickListener(listener);
	}

	/**
	 * 设置编辑框的计划删除按钮
	 */
	public void setDeleteClickListener(OnClickListener listener) {
		btn_delete_workplan.setOnClickListener(listener);
	}

	/**
	 * 设置编辑框的计划保存按钮
	 */
	public void setSaveClickListener(OnClickListener listener) {
		btn_save_workplan.setOnClickListener(listener);
	}
  
	
	/**
	 * 保存框设置所有内容
	 */
	public void setAllSaveInfo(WorkPlanModle modle) {
		if (modle.getPlanSort()==0) {			
		}else{
			workpan_item_number.setText(PublicUtils.getResourceString(mContext,R.string.work_plan_c27)+modle.getPlanSort()+":");
			
		}
		workpan_item_name.setText(modle.getPlanTitle());
		workpan_item_title.setText(modle.getPlanContent());
		workpan_item_beizhu.setText(modle.getPlanMark());
		rb_important.setStartCount(modle.getImportantLevel());
		rb_important.setStar(modle.getImportantLevel());
		rb_jinji.setStartCount(modle.getRushLevel());
		rb_jinji.setStar(modle.getRushLevel());
		rb_important.setClickable(false);
		rb_jinji.setClickable(false);
		// rb_important.setStartCount(modle.getImportantLevel());
		// rb_jinji.setStartCount(modle.getRushLevel());
		//

	}

	/**
	 * 新建设置所有内容
	 * 
	 */
	public void setAllEditInfo(WorkPlanModle modle) {

		workpan_item_name_et.setText(modle.getPlanTitle());

		workpan_item_content_et.setText(modle.getPlanContent());
		workpan_item_beizhu_et.setText(modle.getPlanMark());
		rb_impor_edt.setStar(modle.getImportantLevel());
		rb_jinji_edt.setStar(modle.getRushLevel());

	}

	/**
	 * 把编辑控件所有数据获取出来 返回一个数据对象
	 */
	public WorkPlanModle getAllEditData() {
		WorkPlanModle modle = new WorkPlanModle();
		modle.setPlanTitle(workpan_item_name_et.getText().toString().trim());

		modle.setPlanContent(workpan_item_content_et.getText().toString().trim());
		modle.setPlanMark(workpan_item_beizhu_et.getText().toString().trim());
		modle.setImportantLevel(rb_impor_edt.getSelectRatingCount());
		modle.setRushLevel(rb_jinji_edt.getSelectRatingCount());
		return modle;
	}

	/**
	 * 把保存框控件所有数据获取出来 返回一个数据对象
	 */
	public WorkPlanModle getAllSaveData() {

		WorkPlanModle modle = new WorkPlanModle();
		modle.setPlanTitle(workpan_item_name.getText().toString().trim());
		modle.setPlanContent(workpan_item_title.getText().toString().trim());
		modle.setPlanMark(workpan_item_beizhu.getText().toString().trim());
		modle.setImportantLevel(rb_important.getSelectRatingCount());
		modle.setRushLevel(rb_jinji.getSelectRatingCount());
		return modle;
	}

	/**
	 * 判断此View的是否有数据, 如果没有填写标题则返回true
	 */
	public boolean isDataEmpty() {
		String str1 = workpan_item_name_et.getText().toString().trim();
		if (TextUtils.isEmpty(str1)) {
			return true;
		}
		return false;

	}

	/**
	 * 控制保存框的编辑按钮是否隐藏
	 * 
	 * @param flag
	 */
	public void hiddenEditButton(boolean flag) {
		if (flag) {
			workpan_item_edit.setVisibility(View.INVISIBLE);
		} else {
			workpan_item_edit.setVisibility(View.VISIBLE);

		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (TextUtils.isEmpty(s)) {// 如果变为空,则为清除已输入,判断三个输入控件是否还有内容

			if (TextUtils.isEmpty(workpan_item_name_et.getText().toString().trim())
					&& TextUtils.isEmpty(workpan_item_content_et.getText().toString().trim())
					&& TextUtils.isEmpty(workpan_item_beizhu_et.getText().toString().trim())) {
				// 如果三个输入框都为空说明没有数据
				isInputContent = false;
			} else {
				isInputContent = true;
			}

		} else {
			isInputContent = true;
		}
	}

	@Override
	public void afterTextChanged(Editable s) {

	}

}
