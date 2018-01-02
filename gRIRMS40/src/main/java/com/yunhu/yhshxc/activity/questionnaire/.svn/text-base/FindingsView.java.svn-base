package com.yunhu.yhshxc.activity.questionnaire;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.questionnaire.bo.Findings;
import com.yunhu.yhshxc.activity.questionnaire.bo.Questionnaire;
import com.yunhu.yhshxc.activity.questionnaire.db.FindingsDB;
import com.yunhu.yhshxc.activity.questionnaire.db.QuestionnaireDB;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;

public class FindingsView {
	
	private View view;
	private Context context;
	private Findings findings = new Findings();
	
	private TextView tv_question_item_title;
	private TextView tv_question_item_time;
	private TextView tv_question_item_explain;
	private QuestionnaireDB questionnaireDB;
	private FindingsDB findingsDB;
	private TextView tv_question_item_nums;
	
	public FindingsView(Context context) {
		this.context = context;
		view = View.inflate(context, R.layout.actiity_question_item, null);
		
		questionnaireDB = new QuestionnaireDB(context);
		findingsDB = new FindingsDB(context);
		tv_question_item_title = (TextView) view.findViewById(R.id.tv_question_item_title);
		tv_question_item_time = (TextView) view.findViewById(R.id.tv_question_item_time);
		tv_question_item_explain = (TextView) view.findViewById(R.id.tv_question_item_explain);
		tv_question_item_nums = (TextView) view.findViewById(R.id.tv_question_item_nums);
	}
	
	/**
	 * 设置调差问卷信息
	 */
//	public void setFindings(Findings findings){
//		this.findings = findings;
//		List<Findings> findingsList = new ArrayList<Findings>();
//		
//		Questionnaire questionnaire = questionnaireDB.findQuestionnaireById(findings.getQuestionnaireId());
//		if(questionnaire != null){
//			if(!TextUtils.isEmpty(questionnaire.getName())){
//				tv_question_item_title.setText(questionnaire.getName());
//			}
//			
//			findingsList = findingsDB.findFindingsByQId(questionnaire.getQuestionId());
//			tv_question_item_time.setText(findingsList.size() + "");
//			SharedPreferencesUtil.getInstance(context).setFindingNums(questionnaire.getQuestionId() + "nums",  findingsList.size() + "");
//			if(!TextUtils.isEmpty(questionnaire.getExplain())){
//				tv_question_item_explain.setText(questionnaire.getExplain());
//			}else{
//				tv_question_item_explain.setText("");
//			}
//			tv_question_item_nums.setText("份");
//			
//			DateUtil.getCurDate();
//			
//		}
//		
//	}
	/**
	 * 设置调差问卷信息
	 */
	public void setFindingsNew(Questionnaire questionnaire ){
		
		
		
		
		if(questionnaire != null){
			if(!TextUtils.isEmpty(questionnaire.getName())){
				tv_question_item_title.setText(questionnaire.getName());
			}
			
			
			tv_question_item_time.setText(questionnaire.getCount() + "");
			SharedPreferencesUtil.getInstance(context).setFindingNums(questionnaire.getQuestionId() + "nums",  questionnaire.getCount()+ "");
			if(!TextUtils.isEmpty(questionnaire.getExplain())){
				tv_question_item_explain.setText(questionnaire.getExplain());
			}else{
				tv_question_item_explain.setText("");
			}
			tv_question_item_nums.setText(PublicUtils.getResourceString(context,R.string.question_fen));
			
			DateUtil.getCurDate();
			
		}
		
	}
	
	public View getView() {
		return view;
	}

}
