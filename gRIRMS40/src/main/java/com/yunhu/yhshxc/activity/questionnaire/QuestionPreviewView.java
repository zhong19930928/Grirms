package com.yunhu.yhshxc.activity.questionnaire;

import java.util.ArrayList;
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
import com.yunhu.yhshxc.activity.questionnaire.db.QuestionDB;

public class QuestionPreviewView implements OnClickListener{
	
	/**
	 * 预览View
	 */
	
	private View view;
	private Context context;
	private ListViewForScrollView lv_question_preview_view;
	private Question questionList ;
	private List<Question> radioButtonList = new ArrayList<Question>();
	private LinearLayout ll_question_preview_show;
	private Boolean isShow = false;//是否显示，默认为false 不显示,true为显示
	private TextView tv_question_preview_title;
	private int qId;
	private int rId;
	private boolean isPreview;
	private QuestionDB qEB;
	private ImageView iv;
	
	public QuestionPreviewView(Context context,boolean isPreview) {
		this.context = context;
		this.isPreview = isPreview;
		qEB = new QuestionDB(context);
		view = View.inflate(context, R.layout.activity_question_preview_view, null);
		lv_question_preview_view = (ListViewForScrollView) view.findViewById(R.id.lv_question_preview_view);
		ll_question_preview_show = (LinearLayout) view.findViewById(R.id.ll_question_preview_show);
		tv_question_preview_title = (TextView) view.findViewById(R.id.tv_question_preview_title);
		iv = (ImageView) view.findViewById(R.id.iv);
		ll_question_preview_show.setOnClickListener(this);
		
	}
	
	public void setID( int qId,int rId){
		this.qId = qId;
		this.rId = rId;
	}
	
	
	public void setPreviewView(Question questionList ,int dis){
		this.questionList = questionList;
		if(questionList!=null){
			List<Question> problemList = qEB.findQuestionListByCId(qId, 2, questionList.getQuestionId());// 查询问题
			ll_question_preview_show.setVisibility(View.VISIBLE);
			tv_question_preview_title.setText(questionList.getTopic());
			isShow = true;
			for (int i = 0; i < problemList.size(); i++) {
				radioButtonList.add(problemList.get(i));
			}
			
		}
		if(dis ==0){
			lv_question_preview_view.setVisibility(View.VISIBLE);
			iv.setImageResource(R.drawable.address_up);
		}else{
			lv_question_preview_view.setVisibility(View.GONE);
		}
//		for(int i= 0;i < questionList.size();i++){
//			Question question = questionList.get(i);
//			Question qList = qEB.findQuestionById(qId, 1,String.valueOf(Integer.parseInt(question.getQuestionNum())-1));
//			if(dis == 1){
//				//单选
//				
////				if(question.getQuestionDiscriminate() == 1 || question.getQuestionDiscriminate() == 2){
//					if(qList!=null){
//						ll_question_preview_show.setVisibility(View.VISIBLE);
//						tv_question_preview_title.setText(qList.getTopic());
//						isShow = true;
//					}else{
//						ll_question_preview_show.setVisibility(View.GONE);
//					}
//					lv_question_preview_view.setVisibility(View.VISIBLE);
//					
//					radioButtonList.add(question);
////				}else{
////					lv_question_preview_view.setVisibility(View.GONE);
////					tv_question_preview_title.setVisibility(View.GONE);
////					isShow = false;
////				}
//			}else if(dis == 2){
//				//多选
//				
//				if(question.getQuestionDiscriminate() == 3 || question.getQuestionDiscriminate() == 4){
//					if(qList!=null){
////						tv_question_preview_title.setText(qList.get(0).getTopic());
//					}
//					lv_question_preview_view.setVisibility(View.VISIBLE);
//					isShow = true;
////					tv_question_preview_title.setText("多选题");
//					radioButtonList.add(question);
//				}else{
//					lv_question_preview_view.setVisibility(View.GONE);
//					tv_question_preview_title.setVisibility(View.GONE);
//					isShow = false;
//				}
//			}else if(dis == 3){
//				//问答题
//				if(question.getQuestionDiscriminate() == 5){
//					if(qList!=null){
////						tv_question_preview_title.setText(qList.get(0).getTopic());
//					}
//					lv_question_preview_view.setVisibility(View.VISIBLE);
//					isShow = true;
////					tv_question_preview_title.setText("问答题");
//					radioButtonList.add(question);
//				}else{
//					lv_question_preview_view.setVisibility(View.GONE);
//					tv_question_preview_title.setVisibility(View.GONE);
//					isShow = false;
//				}
//			}
//			
//		}
		
		QuestionContentItemAdapter questionContentItemAdapter = new QuestionContentItemAdapter(context, radioButtonList,isPreview,true);
		questionContentItemAdapter.setId(qId, rId);
		lv_question_preview_view.setAdapter(questionContentItemAdapter);
//		setListViewHeightBasedOnChildren(lv_question_preview_view);	
		
//		lv_question_preview_view.setVisibility(View.GONE);
//		QuestionPreviewListViewAdapter questionPreviewListViewAdapter = new QuestionPreviewListViewAdapter(context, radioButtonList,isPreview);
//		lv_question_preview_view.setAdapter(questionPreviewListViewAdapter);
//		setListViewHeightBasedOnChildren(lv_question_preview_view);
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


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ll_question_preview_show:
			//listView的显示隐藏 true显示，false隐藏
			if(isShow){
				lv_question_preview_view.setVisibility(View.GONE);
				iv.setImageResource(R.drawable.address_down);
				isShow = false;
			}else{
				lv_question_preview_view.setVisibility(View.VISIBLE);
				iv.setImageResource(R.drawable.address_up);
				isShow = true;
			}
			break;

		default:
			break;
		}
		
	}

	

}
