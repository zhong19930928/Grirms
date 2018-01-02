package com.yunhu.yhshxc.report;

import gcg.org.debug.JLog;

import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.bo.Report;
import com.yunhu.yhshxc.bo.Report.ReportContent;
import com.yunhu.yhshxc.list.activity.ShowImageActivity;
import com.yunhu.yhshxc.list.activity.TableListActivity;
import com.yunhu.yhshxc.parser.ReportParse;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.UrlInfo;

/**
 * 报表列表页面
 * 以列表的形式展示报表数据
 * 分页显示，每次分页请求获取数目的多少受服务端控制
 * @author jishen
 */
public class ReportListActivity extends TableListActivity {
	
	private  static final String TAG = "ReportListActivity";
	/**
	 * 标题LinearLayout
	 */
	private LinearLayout ll_title;
	/**
	 * 列宽
	 */
	private int unitViewWidth;
	/**
	 * 表格标题内容的集合
	 */
	private List<ReportContent> titleList;
	
	@Override
	protected void init() {
		super.init();
		queryIconContainer.setVisibility(View.GONE);
		queryIconContainer.setOnClickListener(null);
	}

	/*
	 * viewCoum 所有标题func集合 
	 * ll_title 标题布局
	 * (non-Javadoc)
	 * @see com.gcg.grirms.list.activity.TableListActivity#initTitle(java.util.List, android.widget.LinearLayout)
	 */
	@Override
	protected void initTitle(List<Func> viewCoum, LinearLayout ll_title) {
		this.ll_title = ll_title;
		((View) ll_title.getParent()).findViewById(R.id.iv_table_list_title_isExpanded).setVisibility(View.GONE);
	}
	
	/*
	 * 解析获取的报表数据
	 * @see com.gcg.grirms.list.activity.TableListActivity#getDataListOnThread(java.lang.String)
	 */
	@Override
	protected List getDataListOnThread(String json) throws Exception {
		Report report = new ReportParse().parseReportResult(json);
		if(report != null && report.getTitleList() !=null && !report.getTitleList().isEmpty()){
			this.settingPage(report.getCacherows(), report.getTotal());
			titleList = report.getTitleList();//标题的名称的集合
		}
		return report.getDataList();
	}
	/*
	 * (non-Javadoc)
	 * @see com.gcg.grirms.list.activity.TableListActivity#getSearchParams()
	 */
	@Override
	protected HashMap<String, String> getSearchParams() {
		//存放查询条件的map
		HashMap<String, String> searchParams =	new HashMap<String, String>();
		searchParams.put("reportid", targetId+"");
		JLog.d(TAG, "reportid ======> -->"+targetId);
		if(menuType ==  Menu.TYPE_REPORT_NEW){//新报表
			searchParams.put("reportstatus", 3+"");
		}else if(menuType ==  Menu.TYPE_REPORT){//老报表
			searchParams.put("reportstatus", 2+"");
		}
		JLog.d(TAG,"reportstatus===>"+searchParams.get("reportstatus"));
		// 从SharedPreferences获取 查询条件 的字符串
		String searchReplenishStr = SharedPreferencesUtil.getInstance(this).getReplenish();
		JLog.d(TAG, "查询条件Str ======> -->"+searchReplenishStr);
		if(!TextUtils.isEmpty(searchReplenishStr)){//判断searchReplenishStr非空
			// 根据"$"分割查询条件 的字符串 
			if(menuType ==  Menu.TYPE_REPORT_NEW){//新报表条件
				String[] searchReplenishList = TextUtils.split(searchReplenishStr, "\\$@@");
				for(String searchParamsStr : searchReplenishList){//循环添加查询条件到map中
					if(searchParamsStr.contains(",")){
						String[] searchParamsStrSplit = searchParamsStr.split(",",2);
						if(searchParamsStrSplit[1].contains("~@@")){
							String[] fromToStr = searchParamsStrSplit[1].split("~@@");
							searchParams.put(searchParamsStrSplit[0], fromToStr[0]);
							searchParams.put("To_"+searchParamsStrSplit[0], fromToStr[1]);
							JLog.d(TAG, searchParamsStrSplit[0]+" ======> -->"+searchParams.get(searchParamsStrSplit[0]));
							JLog.d(TAG, "To_"+searchParamsStrSplit[0]+" ======> -->"+searchParams.get("To_"+searchParamsStrSplit[0]));
						}else{
							searchParams.put(searchParamsStrSplit[0], searchParamsStrSplit[1]);
							JLog.d(TAG, searchParamsStrSplit[0]+" ======> -->"+searchParams.get(searchParamsStrSplit[0]));
						}
					}
				}
			}else if(menuType ==  Menu.TYPE_REPORT){//老报表条件
				// 根据"$"分割查询条件 的字符串 
				StringBuilder sb = new StringBuilder("");
				String[] searchReplenishList = TextUtils.split(searchReplenishStr, "\\$@@");
				for(String searchParamsStr : searchReplenishList){//循环添加查询条件到map中
					if(searchParamsStr.contains(",")){
						String[] searchParamsStrSplit = searchParamsStr.split(",",2);
						sb.append(";");
						if(searchParamsStrSplit[1].contains("~@@")){
							String[] fromToStr = searchParamsStrSplit[1].split("~@@");
							sb.append("from_").append(searchParamsStrSplit[0]).append("$").append(fromToStr[0])
								.append(";")
								.append("to_").append(searchParamsStrSplit[0]).append("$").append(fromToStr[1]);
						}else{
							sb.append(searchParamsStrSplit[0]).append("$").append(searchParamsStrSplit[1]);
						}
					}
				}
				searchParams.put("params", sb.deleteCharAt(0).toString());
				JLog.d(TAG, "params ======> -->"+searchParams.get("params"));
			}
		}
		return searchParams;
	}
	
	/*
	 * 解析完成以后
	 * @see com.gcg.grirms.list.activity.AbsSearchListActivity#afterSuccessParse()
	 */
	@Override
	protected void afterSuccessParse() {
		initTitleAfterData();
		super.afterSuccessParse();
	}
	
	/**
	 * 数据返回后,初始化标题
	 * @param titleList 标题名称的集合
	 */
	private void initTitleAfterData() {
		unitViewWidth = computeViewWidth(titleList.size(),null);
		ll_title.removeAllViews();
		for(ReportContent title : titleList){
			TextView tv_title = (TextView) View.inflate(this,R.layout.replenish_list_item_unit, null);
			//添加列宽，如果列宽配置（title.width）为0，则现实默认列宽
			tv_title.setLayoutParams(new LayoutParams((title.width == 0 ? unitViewWidth : title.width), LayoutParams.WRAP_CONTENT));
			//tv_title.setGravity(getGravity(title.align));
			tv_title.setTextColor(Color.rgb(255, 255, 255));
			tv_title.setTextSize(18);
			tv_title.setText(title.content);
			ll_title.addView(tv_title);
		}
	}
	
	/**
	 * 根据align返回Gravity值
	 * 用来设置单元格的位置
	 * 
	 * @param align
	 * @return
	 */
	private int getGravity(String align){
		if(TextUtils.isEmpty(align)){
			return Gravity.LEFT;
		}
		if(Constants.ALIGN_CENTER.equalsIgnoreCase(align)){
			return Gravity.CENTER;
		}else if(Constants.ALIGN_LEFT.equalsIgnoreCase(align)){
			return Gravity.LEFT;
		}else if(Constants.ALIGN_RIGHT.equalsIgnoreCase(align)){
			return Gravity.RIGHT;
		}else{
			return Gravity.LEFT;
		}
	}

	/*
	 * 获取报表查询结果的接口
	 * @see com.gcg.grirms.list.activity.TableListActivity#getSearchUrl()
	 */
	@Override
	protected String getSearchUrl() {
		return UrlInfo.getUrlReportResult(ReportListActivity.this);
	}

	/*
	 * (non-Javadoc)
	 * @see com.gcg.grirms.list.activity.TableListActivity#getCurChildrenCount(int)
	 */
	@Override
	protected int getCurChildrenCount(int groupPosition) {
		return 0;
	}

	/*
	 * 展开以后子view设置
	 * @see com.gcg.grirms.list.activity.TableListActivity#getCurGroupView(int, boolean, android.view.View, android.view.ViewGroup)
	 */
	@Override
	protected View getCurGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		
		ReportContent[] itemDataArr = (ReportContent[]) dataList.get(groupPosition);
		// 优化...
		GroupViewHodler hodler;
		// 显示的item view
		LinearLayout view;
		
		if(convertView==null){//没有缓存view时
			// 加载item view
			view = (LinearLayout) View.inflate(ReportListActivity.this, R.layout.table_list_item, null);
			// 初始化ViewHodler
			hodler = new GroupViewHodler();
			// 是否展开
			hodler.iv_isExpanded = (ImageView) view.findViewById(R.id.iv_table_list_item_isExpanded);
			hodler.iv_isExpanded.setVisibility(View.GONE);
			// 设定hodler.tvs的大小
			hodler.tvs = new TextView[itemDataArr.length];
			
			for (int i = 0;i<itemDataArr.length;i++) {
				// 加载textview
				hodler.tvs[i] = (TextView)View.inflate(ReportListActivity.this, R.layout.replenish_list_item_unit, null);
				//添加列宽，如果列宽配置（itemDataArr[i].width）为0，则现实默认列宽
				hodler.tvs[i].setLayoutParams(new LayoutParams((itemDataArr[i].width == 0 ? unitViewWidth : itemDataArr[i].width), LayoutParams.WRAP_CONTENT));
				//添加位置对齐
				hodler.tvs[i].setGravity(getGravity(itemDataArr[i].align));
				hodler.tvs[i].setPadding(2, 0, 2, 0);
				// 添加textview到item view
				view.addView(hodler.tvs[i]);
			}
			
			// 将hodler全部添加到item view 的tag中
			view.setTag(hodler);
			
		}else{
			// 将缓存view赋给item view
			view = (LinearLayout) convertView;
			// 获取item view中的hodler
			hodler = (GroupViewHodler) view.getTag();
		}
		
		// 从第几列开始赋值
		int j = 0;
		// 循环可被显示的func\
		for (ReportContent item : itemDataArr) {
			if(item.content.startsWith("http:")&&item.content.endsWith(".jpg")){
				hodler.tvs[j].setText(R.string.attendance_date3);
				hodler.tvs[j].setClickable(true);
				hodler.tvs[j].setTextSize(16);
				hodler.tvs[j].setTextColor(Color.rgb(0, 0, 255));
				hodler.tvs[j].getPaint().setUnderlineText(true);
				final String imageUrl = item.content;
				hodler.tvs[j].setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {//图片的话点击“查看图片”跳转到ShowImageActivity页面
						Intent intent = new Intent(ReportListActivity.this,ShowImageActivity.class);
						intent.putExtra("imageUrl",imageUrl);
						intent.putExtra("imageName", PublicUtils.getResourceString(ReportListActivity.this,R.string.attendance_date3));
						startActivity(intent);
					}
				});
			}else{
				hodler.tvs[j].setText(item.content);
				hodler.tvs[j].setClickable(false);
				hodler.tvs[j].setTextSize(14);
				hodler.tvs[j].setTextColor(Color.rgb(0, 0,0));
				hodler.tvs[j].getPaint().setUnderlineText(false);
			}
			// 下一条对应的textview的索引
			j++;
		}
		
		//展开和关闭设置不同的背景显示
		if(isExpanded){
			view.setBackgroundResource(R.color.att_bg_color_blue);
		}else{
			view.setBackgroundResource(R.color.att_bg_color_normal);
		}
		return view;
	}

	/*
	 * (non-Javadoc)
	 * @see com.gcg.grirms.list.activity.TableListActivity#getCurChildView(int, int, boolean, android.view.View, android.view.ViewGroup)
	 */
	@Override
	protected View getCurChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		return null;
	}

}
