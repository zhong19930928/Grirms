package com.yunhu.yhshxc.activity.questionnaire;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.questionnaire.bo.Questionnaire;
import com.yunhu.yhshxc.activity.questionnaire.db.FindingsDB;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;

public class QuestionView {
	
	private View view;
	private Context context;
	private Questionnaire questionnaire = new Questionnaire();
	
	private TextView tv_question_item_title;
	private TextView tv_question_item_time;
	private TextView tv_question_item_explain;
	private TextView tv_question_item_nums;
	private FindingsDB findingsDB;
	
	public QuestionView(Context context) {
		this.context = context;
		view = View.inflate(context, R.layout.actiity_question_item, null);
		
		findingsDB = new FindingsDB(context);
		tv_question_item_title = (TextView) view.findViewById(R.id.tv_question_item_title);
		tv_question_item_time = (TextView) view.findViewById(R.id.tv_question_item_time);
		tv_question_item_explain = (TextView) view.findViewById(R.id.tv_question_item_explain);
		tv_question_item_nums = (TextView) view.findViewById(R.id.tv_question_item_nums);
		tv_question_item_title.setText("");
		tv_question_item_time.setText("");
		tv_question_item_explain.setText("");
		tv_question_item_nums.setText("");
	}
	
	/**
	 * 设置调差问卷信息
	 */
	public void setQuestion(Questionnaire questionnaire){
		this.questionnaire = questionnaire;
		
		
		if(!TextUtils.isEmpty(questionnaire.getName())){
			tv_question_item_title.setText(questionnaire.getName());
		}
		
		if(!TextUtils.isEmpty(questionnaire.getExplain())){
			tv_question_item_explain.setText(questionnaire.getExplain());
		}
		
		if(questionnaire.getNumbers()!= 0){
//			tv_question_item_nums.setText(SharedPreferencesUtil.getInstance(context).getFindingNums(questionnaire.getQuestionId() + "nums") + "/"+questionnaire.getNumbers()+"份");
			tv_question_item_nums.setText(questionnaire.getUpCopies()+"/"+questionnaire.getNumbers());
		}else{
//			tv_question_item_nums.setText("已有"+SharedPreferencesUtil.getInstance(context).getFindingNums(questionnaire.getQuestionId() + "nums")+"份");
			tv_question_item_nums.setText(PublicUtils.getResourceString(context,R.string.question_ti1)+questionnaire.getUpCopies()+PublicUtils.getResourceString(context,R.string.question_fen));
		}
		
		
//		if(findings.size() > 0){
//			Findings f = findings.get(findings.size() - 1);
//			if(f != null && f.getEndDate().length() > 10){
//				try {
//					int date = DateUtil.daysBetween(f.getEndDate(), DateUtil.getCurDate());
//					tv_question_item_time.setText(date + "天前");
//				} catch (ParseException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			
//		}
		
		
//		try {
//			int date = DateUtil.daysBetween(SharedPreferencesUtil.getInstance(context).getFinding( questionnaire.getQuestionId() + "time"), DateUtil.getCurDate());
//			String dateTime=PublicUtils.compareDate(SharedPreferencesUtil.getInstance(context).getFinding(questionnaire.getQuestionId() + "time"));
//			tv_question_item_time.setText(date + "天前");			
			tv_question_item_time.setText(SharedPreferencesUtil.getInstance(context).getFinding( questionnaire.getQuestionId() + "time"));
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
		
//		try {
//			
//			tv_question_item_time.setText(DateUtil.daysBetween(f.getEndDate().substring(0, 10), DateUtil.getCurDate()) + "天前");
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	public View getView() {
		return view;
	}

}
