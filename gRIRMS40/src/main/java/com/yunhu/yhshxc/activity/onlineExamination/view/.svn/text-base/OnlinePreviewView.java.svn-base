package com.yunhu.yhshxc.activity.onlineExamination.view;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.onlineExamination.adapter.OnlineExamPreviewAdapter;
import com.yunhu.yhshxc.activity.onlineExamination.bo.ExamQuestion;

/***
 * 预览
 * @author xuelinlin
 *
 */
public class OnlinePreviewView {
	private View view;
	private Context  context;
	private LinearLayout ll_online_question_preview_show;
	private TextView tv_online_question_preview_title;
	private ListView lv_online_question_preview_view;
	private Boolean isShow = false;//是否显示，默认为false 不显示,true为显示
	private List<ExamQuestion> questionList;
	public OnlinePreviewView(Context  context){
		this.context = context;
		view = View.inflate(context, R.layout.online_question_preview_view, null);
		lv_online_question_preview_view = (ListView) view.findViewById(R.id.lv_online_question_preview_view);
		tv_online_question_preview_title = (TextView) view.findViewById(R.id.tv_online_question_preview_title);
		ll_online_question_preview_show = (LinearLayout) view.findViewById(R.id.ll_online_question_preview_show);
		ll_online_question_preview_show.setOnClickListener(listener);
	}
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(isShow){
				lv_online_question_preview_view.setVisibility(View.GONE);
				isShow = false;
			}else{
				lv_online_question_preview_view.setVisibility(View.VISIBLE);
				isShow = true;
			}
		}
	};
	
	public void setPreviewView(List<ExamQuestion> questionList){
		this.questionList = questionList;
	
		OnlineExamPreviewAdapter adapter = new OnlineExamPreviewAdapter(context, questionList);
		lv_online_question_preview_view.setAdapter(adapter);
		setListViewHeightBasedOnChildren(lv_online_question_preview_view);
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
		  listView.setLayoutParams(params);
		 }

	
	
	

	public View getView() {
		return view;
	}
}
