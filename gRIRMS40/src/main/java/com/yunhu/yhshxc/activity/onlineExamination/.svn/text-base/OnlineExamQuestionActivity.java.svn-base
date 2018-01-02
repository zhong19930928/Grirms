package com.yunhu.yhshxc.activity.onlineExamination;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.activity.onlineExamination.bo.ExamQuestion;
import com.yunhu.yhshxc.activity.onlineExamination.view.ExamContentView;

public class OnlineExamQuestionActivity extends AbsBaseActivity {

	private LinearLayout ll_online_content_back;
	private LinearLayout ll_online_content_explain;
	private LinearLayout ll_online_add_content_item_title;
	private Button btn_online_preview;
	private Button btn_online_submit;
	private TextView tv_online_numer;
	private TextView tv_online_time;
	private List<ExamQuestion> exams = new ArrayList<ExamQuestion>();
	private List<ExamContentView> views = new ArrayList<ExamContentView>();
	private MyCountDownTimer timer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.online_exam_content);
		timer = new MyCountDownTimer(120*60*1000, 60000);
		timer.start();
		initView();
	}

	private void initView() {
		ll_online_content_back = (LinearLayout) this
				.findViewById(R.id.ll_online_content_back);
		ll_online_content_explain = (LinearLayout) this
				.findViewById(R.id.ll_online_content_explain);
		ll_online_add_content_item_title = (LinearLayout) this
				.findViewById(R.id.ll_online_add_content_item_title);
		btn_online_preview = (Button) this
				.findViewById(R.id.btn_online_preview);
		btn_online_submit = (Button) this.findViewById(R.id.btn_online_submit);
		tv_online_time = (TextView) this.findViewById(R.id.tv_online_time);
		tv_online_numer = (TextView) this.findViewById(R.id.tv_online_numer);
		ll_online_content_explain.setOnClickListener(listener);
		ll_online_content_back.setOnClickListener(listener);
		btn_online_preview.setOnClickListener(listener);
		btn_online_submit.setOnClickListener(listener);
		addView();
	}

	private void addView() {
		views.clear();
		for (int i = 0; i < 12; i++) {
			ExamContentView questionnaireContentView = new ExamContentView(this);
			exams = getQuestionList();
			questionnaireContentView.setQuestionContent(exams);
			ll_online_add_content_item_title.addView(questionnaireContentView
					.getView());
			views.add(questionnaireContentView);
		}
	}

	private List<ExamQuestion> getQuestionList() {
		List<ExamQuestion> questions = new ArrayList<ExamQuestion>();
		for (int i = 0; i < 12; i++) {
			ExamQuestion question = new ExamQuestion();
			question.setTopic("1+1等于几");
			question.setQuestionsDif(1);// 问题区分 1单选 2单选其他 3多选 4多选其他 5填空
			questions.add(question);
		}
		return questions;
	}

	class MyCountDownTimer extends CountDownTimer {
		//     /**
		//      *
		//      * @param millisInFuture
		//      *      表示以毫秒为单位 倒计时的总数
		//      *
		//      *      例如 millisInFuture=1000 表示1秒
		//      *
		//      * @param countDownInterval
		//      *      表示 间隔 多少微秒 调用一次 onTick 方法
		//      *
		//      *      例如: countDownInterval =1000 ; 表示每1000毫秒调用一次onTick()
		//      *
		//      */
		public MyCountDownTimer(long millisInFuture, long countDownInterval) { 
			super(millisInFuture, countDownInterval); 
		}

		@Override
		public void onTick(long millisUntilFinished) {
			tv_online_time.setText("剩余时间" + millisUntilFinished / 1000/60 + "/120分钟");
		}

		@Override
		public void onFinish() {
			Toast.makeText(OnlineExamQuestionActivity.this, "考试结束",Toast.LENGTH_LONG).show();
		}

	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ll_online_content_back:
				OnlineExamQuestionActivity.this.finish();
				break;
			case R.id.ll_online_content_explain:
				explain();
				break;
			case R.id.btn_online_preview:
				preview();
				break;
			case R.id.btn_online_submit:

				break;

			default:
				break;
			}
		}

	};

	private void preview() {
		Intent intent = new Intent(this, OnlineExamPreviewActivity.class);
		startActivity(intent);
	}

	private void explain() {
		Intent intent = new Intent(this, OnlineExamExplainationActivity.class);
		startActivity(intent);
	}
}
