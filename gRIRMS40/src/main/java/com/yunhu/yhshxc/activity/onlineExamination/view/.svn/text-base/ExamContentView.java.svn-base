package com.yunhu.yhshxc.activity.onlineExamination.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.onlineExamination.adapter.OnlineExamContentAdapter;
import com.yunhu.yhshxc.activity.onlineExamination.bo.ExamQuestion;

public class ExamContentView implements OnClickListener{

	private View view;
	private Context context;
	private ExamQuestion questionnaire;
	private List<ExamQuestion> questionList = new ArrayList<ExamQuestion>();
	private OnlineExamContentAdapter questionContentItemAdapter;
	private ListView lv_online_content_add_item_view;
	private LinearLayout ll_online_content_item_title;
	private TextView tv_online_content_title;
	private Boolean isShow = false;//判断listview的显示 true 隐藏 false

	public ExamContentView(Context context) {
		this.context = context;
		view = View.inflate(context, R.layout.online_exam_content_view, null);
		
		lv_online_content_add_item_view = (ListView) view.findViewById(R.id.lv_online_content_add_item_view);
		ll_online_content_item_title = (LinearLayout) view.findViewById(R.id.ll_online_content_item_title);
		tv_online_content_title = (TextView) view.findViewById(R.id.tv_online_content_title);
		
//		String label = DateUtils.formatDateTime(context,
//				System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
//						| DateUtils.FORMAT_SHOW_DATE
//						| DateUtils.FORMAT_ABBREV_ALL);
//		lv_content_add_item_view.getLoadingLayoutProxy().setLastUpdatedLabel(label);
//		lv_content_add_item_view.setMode(Mode.BOTH);
//		lv_content_add_item_view
//				.setOnRefreshListener(new OnRefreshListener<ListView>() {
//					@Override
//					public void onRefresh(
//							PullToRefreshBase<ListView> refreshView) {
//						// 向下滑动
//						if (refreshView.getCurrentMode() == Mode.PULL_FROM_END) {
//							lv_content_add_item_view.onRefreshComplete();
//						}
//						// 向上加载
//						if (refreshView.getCurrentMode() == Mode.PULL_FROM_START) {
//							lv_content_add_item_view.onRefreshComplete();
//						}
//
//					}
//				});
		
		ll_online_content_item_title.setOnClickListener(this);
		
	}

	
	
	/**
	 * 设置考题信息
	 */
	public void setQuestionContent(List<ExamQuestion> questionList) {
		this.questionList = questionList;
		
		questionContentItemAdapter = new OnlineExamContentAdapter(context, questionList);
		lv_online_content_add_item_view.setAdapter(questionContentItemAdapter);
		setListViewHeightBasedOnChildren(lv_online_content_add_item_view);
	
		lv_online_content_add_item_view.setVisibility(View.GONE);
		
		
		
		
	}
	
	public void setListViewHeightBasedOnChildren(ListView listView) {
		 

		  // 获取ListView对应的Adapter

		  ListAdapter listAdapter = listView.getAdapter();

		  if (listAdapter == null) {

		   return;

		  }

		  int totalHeight = 0;

		  for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目

		   View listItem = listAdapter.getView(i, null, listView);

		   listItem.measure(0, 0); // 计算子项View 的宽高

		   totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度

		  }

		  ViewGroup.LayoutParams params = listView.getLayoutParams();

		  params.height = totalHeight
		    + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

		  // listView.getDividerHeight()获取子项间分隔符占用的高度

		  // params.height最后得到整个ListView完整显示需要的高度

		  listView.setLayoutParams(params);

		 }

	public View getView() {
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ll_online_content_item_title:
			if(isShow){
				lv_online_content_add_item_view.setVisibility(View.GONE);
				isShow = false;
			}else{
				lv_online_content_add_item_view.setVisibility(View.VISIBLE);
				isShow = true;
			}
			
			break;

		default:
			break;
		}
	}
}
