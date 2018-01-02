package com.yunhu.yhshxc.comp;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.SubmitItem;
import com.yunhu.yhshxc.list.activity.ShowImageActivity;
import com.yunhu.yhshxc.utility.ScreenUtil;

/**
 * 预览表格数据
 * @author jishen
 */
public class PreviewTable {
	private Context context;
	private int row, col;// 表格的行和列
	private List<String> contentList;//表格中的所有数据的集合
	private List<Func> funcList;//所有控件的集合
	private SubmitItem item;//当前item
	private boolean isLink;//是否是超链接中的表格 true 是 false 不是
	public PreviewTable(Context context, int row, List<String> contentList,List<Func> funcList) {
		this.context = context;
		this.contentList = contentList;
		this.funcList=funcList;
		this.row = row;//表格中的行数
		col = contentList.size() / row;//计算列数
	}

	/**
	 * 添加title
	 * @return
	 */
	private View addTableTitleRow() {
		TableCell[] tableCell = new TableCell[col];// 列数
		TextView tv_title;
		int width=getWidth(70);//设置宽
		if(funcList.size()==1){
			width=getWidth(200);
		}
		for (int j = 0; j < tableCell.length; j++) {
			Func func=funcList.get(j);
			tv_title = new TextView(this.context);
			tv_title.setText(func.getName());
			tv_title.setTextSize(20);
			tv_title.setGravity(Gravity.CENTER);
			tv_title.setTextColor(Color.WHITE);
			tableCell[j] = new TableCell(tv_title, width, getHeigh(50));
		}
		View titleView = getTableComp(new TableRow(tableCell));
		return titleView;
	}

	/**
	 * 返回生成的表格
	 */
	public View getObject() {
		ScrollView sv = new ScrollView(context);
		HorizontalScrollView hs = new HorizontalScrollView(context);
		hs.setHorizontalScrollBarEnabled(false);
		LinearLayout rootLinearLayout = new LinearLayout(context);
		rootLinearLayout.setGravity(Gravity.CENTER);
		LinearLayout linearLayout = new LinearLayout(context);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		TableCell[] tableCell = new TableCell[col];// 列数
		TextView tv_title;
		linearLayout.addView(addTableTitleRow());//添加标题
		int p = 0;
		int width=getWidth(70);
		if(funcList.size()==1){
			width=getWidth(200);
		}
		for (int j = 0; j < row; j++) { // 行数
			for (int i = 0; i < tableCell.length; i++) {
				final String content = contentList.get(p++);
				final Func func=funcList.get(i);
				tv_title = new TextView(this.context);
				tv_title.setTextSize(20);
				tv_title.setGravity(Gravity.CENTER);
				if((func.getType()==Func.TYPE_CAMERA || func.getType()==Func.TYPE_CAMERA_MIDDLE || func.getType()==Func.TYPE_CAMERA_HEIGHT || func.getType()==Func.TYPE_CAMERA_CUSTOM) && !TextUtils.isEmpty(content)){//拍照的话设置单击事件
					if(content.endsWith(".jpg")){
						tv_title.setTextColor(context.getResources().getColor(R.color.blue2));
						tv_title.setText(context.getResources().getString(R.string.query));
						tv_title.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								Intent intent =new Intent(context, ShowImageActivity.class);
								intent.putExtra("imageUrl",content);
								intent.putExtra("imageName", func.getName());
								if(item!=null){
									intent.putExtra("submitItemId", item.getId());
									intent.putExtra("PreviewTable", true);//是表格里的图片
								}
								if(isLink){
									intent.putExtra("isLink", true);
								}
								context.startActivity(intent);
							}
						});
					}
				}else{
					tv_title.setTextColor(Color.WHITE);
					tv_title.setText(content);
				}
					
				tableCell[i] = new TableCell(tv_title, width, getHeigh(50));
			}

			View view = getTableComp(new TableRow(tableCell));
			linearLayout.addView(view);
		}
		hs.addView(linearLayout);
		rootLinearLayout.addView(hs);
		sv.addView(rootLinearLayout);
		sv.setPadding(5, 5, 5, 5);
		return sv;
	}

	/**
	 * 
	 * @param tableRow表的每一行
	 * @return一个表格样式的view
	 */
	private View getTableComp(TableRow tableRow) {
		LinearLayout rootLinearLayout = new LinearLayout(context);
		rootLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
		for (int i = 0; i < tableRow.getSize(); i++) {// 逐个格单位添加到行
			TableCell tableCell = tableRow.getCellValue(i);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					tableCell.width, tableCell.height);// 指定的大小设置空间
			layoutParams.setMargins(1, 1, 1, 1);// 预留空地建造边框
			LinearLayout l = new LinearLayout(context);
			l.setGravity(Gravity.CENTER_VERTICAL);
			l.setBackgroundColor(context.getResources().getColor(R.color.notice_detail_title));
			View viewCell = new View(context);
			viewCell = (View) tableCell.value;
			viewCell.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT,
					LinearLayout.LayoutParams.FILL_PARENT));
			l.addView(viewCell);
			rootLinearLayout.addView(l, layoutParams);
		}
		rootLinearLayout.setBackgroundColor(Color.WHITE);
		return rootLinearLayout;
	}

	/**
	 * TableRow 实现表格的行
	 */
	static public class TableRow {
		private TableCell[] cell;

		public TableRow(TableCell[] cell) {
			this.cell = cell;
		}

		public int getSize() {
			return cell.length;
		}

		public TableCell getCellValue(int index) {
			if (index >= cell.length)
				return null;
			return cell[index];
		}
	}

	/**
	 * TableCell 实现表格的格单位
	 */
	static public class TableCell {
		public Object value;
		public int width;
		public int height;

		public TableCell(Object value, int width, int height) {
			this.value = value;
			this.width = width;
			this.height = height;
		}
	}
	
	/*
	 * 不同分辨率适配宽
	 */
	private int getWidth(int width) {
		ScreenUtil screenUtil = new ScreenUtil(context);
		float ratio = (float) width / 240;
		int currentScreenWidth = screenUtil.getScreenWidth();
		int changeWidth = screenUtil.getWidth(ratio, currentScreenWidth);
		return changeWidth;
	}

	/*
	 * 不同分辨率适配高
	 */
	private int getHeigh(int heigh) {
		ScreenUtil screenUtil = new ScreenUtil(context);
		float ratio = (float) heigh / 320;
		int currentScreenHeigh = screenUtil.getScreenHeight();
		int changeHeigh = screenUtil.getWidth(ratio, currentScreenHeigh);
		return changeHeigh;
	}
	
	public void setTableSubmitItem(SubmitItem subItem){
		this.item=subItem;
	}
	
	public void setIsInLink(boolean isLink){
		this.isLink=isLink;
	}
}
