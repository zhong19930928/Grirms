package com.yunhu.yhshxc.help;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;

/**
 * 查看常见问题详细信息模块
 * 通过Intent传入常见问题的内容（问题和答案），并显示在此Activity中
 * 
 * @version 2013.5.23
 *
 */
public class QuestionDetailActivity extends AbsBaseActivity {
	
	private ImageView iv_back;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.help_question_detail);
		iv_back =(ImageView) findViewById(R.id.help_question_detail_back);
		iv_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				QuestionDetailActivity.this.finish();
			}
		});
		String q = null;
		String a = null;
		//获取问题和答案的字符串
		if(this.getIntent() != null){
			q = this.getIntent().getStringExtra("Q");
			a = this.getIntent().getStringExtra("A");
		}
		
		//将字符串显示在相关TextView中
		if(!TextUtils.isEmpty(q) ){
			((TextView)this.findViewById(R.id.tv_Q)).setText(q);
		}
		if(!TextUtils.isEmpty(a) ){
			((TextView)this.findViewById(R.id.tv_A)).setText(a);
		}
	}
}
