package com.yunhu.yhshxc.activity.questionnaire;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.questionnaire.bo.Question;

public class QuestionnaireContentView implements OnClickListener{

	private View view;
	private Context context;
	private List<Question> questionList;
	private QuestionContentItemAdapter questionContentItemAdapter;
	private ListViewForScrollView lv_content_add_item_view;
	private LinearLayout ll_content_item_title;
	private Boolean isShow = false;//判断listview的显示 true 隐藏 false
	private Question question;
	private TextView tv_title;
	private TextView tv_desc;
	private QuestionnaireContentActivity questionnaireContentActivity;
	boolean isPreview ;
	private ImageView iv;

	public QuestionnaireContentView(Context context,boolean isPreview) {
		this.context = context;
		this.isPreview = isPreview;
		view = View.inflate(context, R.layout.activity_question_content_view, null);
		
		lv_content_add_item_view = (ListViewForScrollView) view.findViewById(R.id.lv_content_add_item_view1111);
		ll_content_item_title = (LinearLayout) view.findViewById(R.id.ll_content_item_title);
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		tv_desc = (TextView) view.findViewById(R.id.tv_desc);
		iv = (ImageView) view.findViewById(R.id.iv);
		
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
		
		ll_content_item_title.setOnClickListener(this);
		
	}
	
	public void setQuestion(Question question){
		this.question = question;
		tv_title.setText(question.getTopic());
		tv_desc.setText(question.getRemarks());
		
	}

	public void setQActivity(Context context){
		questionnaireContentActivity = (QuestionnaireContentActivity)context;
	}
	
	
	/**
	 * 设置调差问卷信息
	 */
	public void setQuestionContent(List<Question> questionList,int size) {
		this.questionList = questionList;
		
		questionContentItemAdapter = new QuestionContentItemAdapter(context, questionList,isPreview,false);
		questionContentItemAdapter.setQActivity2(questionnaireContentActivity);
		lv_content_add_item_view.setAdapter(questionContentItemAdapter);
//		setListViewHeightBasedOnChildren(lv_content_add_item_view);	
		if(size == 0){
			lv_content_add_item_view.setVisibility(View.VISIBLE);
			iv.setImageResource(R.drawable.address_up);
		}else{
			lv_content_add_item_view.setVisibility(View.GONE);
		}
		
		
	}
	
//	public QuestionContentItemView getViews(){
//		return questionContentItemAdapter.getViews();
//		
//	}
	
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
		    + (listView.getDividerHeight() * (listAdapter.getCount() - 1)) + 60;

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
		case R.id.ll_content_item_title:
			if(isShow){
				lv_content_add_item_view.setVisibility(View.GONE);
				iv.setImageResource(R.drawable.address_down);
				isShow = false;
			}else{
				lv_content_add_item_view.setVisibility(View.VISIBLE);
				iv.setImageResource(R.drawable.address_up);
				isShow = true;
			}
			
			break;

		default:
			break;
		}
	}
}
