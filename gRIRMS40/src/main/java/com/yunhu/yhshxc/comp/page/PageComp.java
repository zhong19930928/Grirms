package com.yunhu.yhshxc.comp.page;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.widget.ToastOrder;

public class PageComp{

	/**
	 * 底部的翻页栏的容器（包括TIP组件），如果需要翻页，就显示，否则隐藏
	 */
	private View ll_paging = null;

	private View ll_paging2 = null;

	/**
	 * 底部的翻页栏的容器（不包括TIP组件），如果需要翻页，就显示，否则隐藏
	 */
	private View ll_paging_cotrol = null;

	/**
	 * 翻页页数列表的容器(翻页栏中间按钮)
	 */
	private View ll_page_mid_list = null;

	/**
	 * 显示翻页的按钮
	 */
	private View ll_page_tip = null;
	private LinearLayout ll_page_left,ll_page_mid,ll_page_right;

	/**
	 * 翻页页数列表
	 */
	private ListView pageMidListView = null;

	/**
	 * 显示页数的组件
	 */
	private TextView tv_paging_count = null;

	/**
	 * 总页数
	 */
	protected int pageCount = 0;

	/**
	 * 当前页数
	 */
	public int currentPage = 0;

	/**
	 * 上一页
	 */
	public int lastPage = 0;

	/**
	 * 每页数量
	 */
	protected int EachPageNum = 20;
	
	
	private Context context;
	private View view =null;
	private PageCompListener pageCompListener;
	
	public PageComp(Context context,PageCompListener listener) {
		this.context = context;
		this.pageCompListener = listener;
		view = View.inflate(context, R.layout.page_comp, null);
		addPaging();
	}
	
	public boolean isShowPageComp(){
		return pageCount>1?true:false;
	}
	
	/**
	 * 添加分页功能
	 */
	private void addPaging() {
		// 获取页面组件对象
		this.ll_paging_cotrol = view.findViewById(R.id.ll_paging_cotrol_comp);
		this.ll_paging = view.findViewById(R.id.ll_paging_comp);
		this.ll_paging2 = view.findViewById(R.id.ll_paging2_comp);
		this.ll_page_tip = view.findViewById(R.id.ll_page_tip_comp);
		this.ll_page_tip.setOnClickListener(listener);
		this.ll_page_left = (LinearLayout) view.findViewById(R.id.ll_page_left_comp);
		this.ll_page_left.setOnClickListener(listener);
		this.ll_page_mid = (LinearLayout) view.findViewById(R.id.ll_page_mid_comp);
		this.ll_page_mid.setOnClickListener(listener);
		this.ll_page_right = (LinearLayout) view.findViewById(R.id.ll_page_right_comp);
		this.ll_page_right.setOnClickListener(listener);
		
		this.ll_page_mid_list = view.findViewById(R.id.ll_page_mid_list_comp);
		this.tv_paging_count = (TextView) view.findViewById(R.id.tv_paging_count_comp);
		this.pageMidListView = (ListView) view.findViewById(R.id.lv_page_mid_list_view_comp);
		this.pageMidListView.setDivider(null);// 取消分割线
		this.pageMidListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				if (currentPage == position)// 如果用户跳转的目标就是当前页，则什么都不做
//					return;
				lastPage = currentPage;
				currentPage = position;
				pagingOnItemClick();
			}

		});
		this.pageMidListView.setAdapter(new PagingAdapter(context));
	}

	/**
	 * 页面跳转项的事件
	 */
	private void pagingOnItemClick() {
		PagingAdapter adapter = (PagingAdapter) this.pageMidListView.getAdapter();
		adapter.setItemViewSelected(this.currentPage);// 设置选中项的背景颜色
		if (pageCompListener !=null) {
			pageCompListener.pageSelect(currentPage);
			this.pageMidListView.setSelection(this.currentPage - 1);
			tv_paging_count.setText((this.currentPage + 1) + "/"+ this.pageCount);
		}
	}

	/**
	 * 刷新翻页栏
	 */
	private void refreshPage() {
		if (pageCount <= 1) {// 如何只有一页的话，就隐藏翻页
			ll_paging.setVisibility(View.GONE);
		} else {
			if (this.ll_paging != null) {
				this.ll_paging.setVisibility(View.VISIBLE);
//				View footView = View.inflate(context, R.layout.foot_listview, null);
//				if (mListView.getFooterViewsCount() == 0) {
//					mListView.addFooterView(footView);
//				}
//				mListView.setAdapter(searchAdapter);
			}
		}
		if (this.pageMidListView != null) {
			this.pageMidListView.setSelection(this.currentPage - 1);
			PagingAdapter adapter = (PagingAdapter) this.pageMidListView.getAdapter();
			adapter.setDataSrc(getPageListData());// 更新跳转项List
			adapter.setItemViewSelected(this.currentPage);// 将当前页突出显示
			adapter.notifyDataSetChanged();
			tv_paging_count.setText((this.currentPage + 1) + "/"+ this.pageCount);
		}
	}

	/**
	 * 获取页码数组
	 * 
	 * @return 返回页码数组
	 */
	private String[] getPageListData() {
		String[] pageArr = new String[this.pageCount];
		for (int i = 0; i < this.pageCount; i++) {
			pageArr[i] = "第" + (i + 1) + "页";
		}
		return pageArr;
	}

	/**
	 * 设置翻页栏的总页数和每页数量
	 * 
	 * @param cacherows
	 *            每页数据量
	 * @param total
	 *            数据总量
	 */
	public void settingPage(Integer cacherows, Integer total) {
		if (cacherows != null && cacherows != 0 && total != null) {
			this.pageCount = (int) Math.ceil((double) total / cacherows);
			this.EachPageNum = cacherows;
			refreshPage();
		}
	}

	
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ll_page_tip_comp:// 显示/隐藏翻页栏
				settingShowPagingNoneAnimation();
				break;
			case R.id.ll_page_left_comp:// 上一页
				lastPage = currentPage;
				int temp1 = currentPage;
				temp1--;
				if (temp1 < 0) {
					ToastOrder.makeText(context, context.getResources().getString(R.string.is_on_firstpage), ToastOrder.LENGTH_LONG).show();
				} else {
					currentPage = temp1;
					pagingOnItemClick();
				}
				break;
			case R.id.ll_page_mid_comp:// 按页码跳转
				settingShowSearchPagingList();
				break;
			case R.id.ll_page_right_comp:// 下一页
				lastPage = currentPage;
				int temp2 = currentPage;
				temp2++;
				if (temp2 >= pageCount) {
					ToastOrder.makeText(context, context.getResources().getString(R.string.is_on_lastpage), ToastOrder.LENGTH_LONG).show();
				} else {
					currentPage = temp2;
					pagingOnItemClick();
				}
				break;
			}
		}
	};

	/**
	 * 显示/隐藏翻页栏
	 */
	private void settingShowPaging() {

		if (ll_paging_cotrol.isShown()) {
			float to = (float) ll_paging.getHeight();
			TranslateAnimation animation = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF,
					0, Animation.RELATIVE_TO_SELF, 0f,
					Animation.RELATIVE_TO_SELF, (float) ll_paging2.getHeight()/ to);
			animation.setDuration(500);
			// animation.setFillAfter(true);
			animation.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					ll_paging2.setVisibility(View.GONE);

				}
			});
			ll_paging.setAnimation(animation);
			ll_paging.setTag(to);
			ll_page_tip.setBackgroundResource(R.drawable.paging_tip_up);

		} else {
			float from = ll_paging.getTag() == null ? 0 : (Float) ll_paging
					.getTag();
			TranslateAnimation animation = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF,
					0, Animation.RELATIVE_TO_SELF,
					(float) ll_paging2.getHeight() / from,
					Animation.RELATIVE_TO_SELF, 0);
			animation.setDuration(500);
			// animation.setFillAfter(true);
			ll_paging2.setVisibility(View.VISIBLE);
			ll_paging.setAnimation(animation);
			ll_page_tip.setBackgroundResource(R.drawable.paging_tip_down);
		}

	}
	
	/**
	 * 显示/隐藏翻页栏
	 */
	private void settingShowPagingNoneAnimation() {
		
		if (ll_paging_cotrol.isShown()) {
			ll_paging2.setVisibility(View.GONE);
			ll_page_tip.setBackgroundResource(R.drawable.paging_tip_up);
			
		}else {
			ll_paging2.setVisibility(View.VISIBLE);
			ll_page_tip.setBackgroundResource(R.drawable.paging_tip_down);
		}		
		
	}
	
	

	/**
	 * 用动画显示/隐藏页码跳转的List
	 */
	private void settingShowSearchPagingList() {
		if (this.ll_page_mid_list.isShown()) {
			this.ll_page_mid_list.setVisibility(View.GONE);
		} else {
			this.ll_page_mid_list.setVisibility(View.VISIBLE);
		}
	}
	

	public View getView(){
		return view;
	}
	
	public void setCurrentPage(int page){
		this.currentPage = page;
	}

}
