package com.yunhu.yhshxc.activity.onlineExamination;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.activity.onlineExamination.bo.ExamQuestion;
import com.yunhu.yhshxc.activity.onlineExamination.view.OnlinePreviewView;
/**
 *	预览
 * @author xuelinlin
 *
 */
public class OnlineExamPreviewActivity extends AbsBaseActivity {
	
	private Button btn_online_preview_p;
	private Button btn_online_submit_p;
	private LinearLayout ll_online_add_preview_item_title;
	private LinearLayout ll_online_preview_explain;
	private LinearLayout ll_online_preview_back;
	private List<ExamQuestion> questionList = new ArrayList<ExamQuestion>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.online_exam_content_preview);
		init();
	}

	private void init() {
		btn_online_preview_p = (Button) findViewById(R.id.btn_online_preview_p);
		btn_online_submit_p = (Button) findViewById(R.id.btn_online_submit_p);
		ll_online_add_preview_item_title = (LinearLayout) findViewById(R.id.ll_online_add_preview_item_title);
		ll_online_preview_explain = (LinearLayout) findViewById(R.id.ll_online_preview_explain);
		ll_online_preview_back = (LinearLayout) findViewById(R.id.ll_online_preview_back);
		btn_online_preview_p.setOnClickListener(listener);
		btn_online_submit_p.setOnClickListener(listener);
		ll_online_preview_explain.setOnClickListener(listener);
		ll_online_preview_back.setOnClickListener(listener);
		addView();
	}
	
	private void addView() {
		questionList = getQuestionList();
		for(int i = 0; i<5;i++){
			OnlinePreviewView view = new OnlinePreviewView(this);
			view.setPreviewView(questionList);
			ll_online_add_preview_item_title.addView(view.getView());
		}
	}
	private List<ExamQuestion> getQuestionList() {
		List<ExamQuestion> questions = new ArrayList<ExamQuestion>();
    	for(int i = 0;i < 12;i ++){
    		ExamQuestion question = new ExamQuestion();
    		question.setTopic("1+1等于几");
    		question.setQuestionsDif(3);//问题区分 1单选 2单选其他 3多选 4多选其他 5填空
    		questions.add(question);
    	}
		return questions;
	}
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_online_preview_p:
				
				break;
			case R.id.btn_online_submit_p:
				
				break;
			case R.id.ll_online_preview_explain:
				
				break;
			case R.id.ll_online_preview_back:
				finish();
				break;

			default:
				break;
			}
		}
	};
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
