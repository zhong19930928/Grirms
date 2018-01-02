package com.yunhu.yhshxc.activity.questionnaire;

import java.util.ArrayList;
import java.util.List;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.questionnaire.bo.AnswerOptions;
import com.yunhu.yhshxc.activity.questionnaire.bo.FindIngDetail;
import com.yunhu.yhshxc.activity.questionnaire.bo.Question;
import com.yunhu.yhshxc.activity.questionnaire.db.AnswerOptionsDB;
import com.yunhu.yhshxc.activity.questionnaire.db.FindIngDetailDB;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class QuestionContentItemView {

	private View view;
	private Context context;
	private Question question;
	private LinearLayout ll_add_item_view;
	private TextView tv_title;
	public List<QuestionContentItemRadioButtonView> questionContentItemRadioButtonViews = new ArrayList<QuestionContentItemRadioButtonView>();//存放单选view的list
	private List<QuestionContentItemMoreButtonView> questionContentItemMoreButtonViews = new ArrayList<QuestionContentItemMoreButtonView>();//存放多选view的list
	private List<QuestionContentItemEditTextView> questionContentItemEditTextViews = new ArrayList<QuestionContentItemEditTextView>();//存放输入框view的list
	private AnswerOptionsDB answerOptionsDB;
	private List<AnswerOptions> answerOptionList = new ArrayList<AnswerOptions>();
	private QuestionnaireContentActivity questionnaireContentActivity;
	private int pId = -1;
	private int rId;
	private FindIngDetailDB findIngDetailDB;
	private List<FindIngDetail> f = new ArrayList<FindIngDetail>();
	private boolean isPreview;
	private TextView tv_desc;
	
	public QuestionContentItemView(Context context,boolean isPreview) {
		this.context = context;
		this.isPreview = isPreview;
		view = View.inflate(context, R.layout.activity_question_content_item_view_one, null);
		
		findIngDetailDB = new FindIngDetailDB(context);
		answerOptionsDB = new AnswerOptionsDB(context);
		ll_add_item_view = (LinearLayout)view.findViewById(R.id.ll_add_item_view);
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		tv_desc = (TextView) view.findViewById(R.id.tv_desc);

	}
	
	public void setId(int pId, int rId){
		this.pId = pId;
		this.rId = rId;
	    f = findIngDetailDB.findFindIngDetailListByQId(pId, rId);
	}
	
	public void setQActivity3(Context context){
		questionnaireContentActivity = (QuestionnaireContentActivity)context;
	}

	public void setQuestionContentItemView(Question question,boolean isClearOptions) {
		this.question = question;
		
		//问题区分 1单选 2单选其他 3多选 4多选其他 5填空
		tv_title.setText(question.getTopic());
		tv_desc.setText(question.getRemarks());
		answerOptionList = answerOptionsDB.findAnswerOptionById(question.getQuestionnaireId(), question.getQuestionId());
		
		if(question.getQuestionDiscriminate() ==1){
	    //1单选
			for(int i= 0;i < answerOptionList.size();i ++){
				AnswerOptions answerOptions = answerOptionList.get(i);
				
				QuestionContentItemRadioButtonView questionContentItemRadioButtonView = new QuestionContentItemRadioButtonView(context, this,isPreview);
				if(questionnaireContentActivity != null){
					questionContentItemRadioButtonView.setQActivity4(questionnaireContentActivity);
				}
				questionContentItemRadioButtonView.setQuestionContentItemRadioButtonView(answerOptions,false);
				if (isClearOptions) {
					questionContentItemRadioButtonView.setSelected(false);
				}
				if(answerOptions.getProblemId() == pId){
					if(f.size()>0){
						if(String.valueOf(answerOptions.getOptionsId()).equals(f.get(0).getChoiceOptions())){
							questionContentItemRadioButtonView.setSelected(true);
						}else{
							questionContentItemRadioButtonView.setSelected(false);
						}
					}
				}
				if(isPreview){
					questionContentItemRadioButtonView.setSelected(false);
				}
	
				
				ll_add_item_view.addView(questionContentItemRadioButtonView.getView());
				
				questionContentItemRadioButtonViews.add(questionContentItemRadioButtonView);
				
					if(answerOptions.getIsSave() == 1){
						if(SharedPreferencesUtil.getInstance(context).getIfSave(question.getQuestionnaireId() + "save").equals(PublicUtils.getResourceString(context,R.string.yes))){
							questionContentItemRadioButtonView.setSelected(true);
						}
						
					}
					if(questionnaireContentActivity != null){
					questionnaireContentActivity.questionContentItemRadioButtonViews.add(questionContentItemRadioButtonView);
					
				}
			
			}
		}
		else if(question.getQuestionDiscriminate() == 2){
		//2单选其他
			for(int i= 0;i < answerOptionList.size();i ++){
				
				AnswerOptions answerOptions = answerOptionList.get(i);
					QuestionContentItemRadioButtonView questionContentItemRadioButtonView = new QuestionContentItemRadioButtonView(context, this,isPreview);
	                if(pId != -1){
	                	questionContentItemRadioButtonView.setIsResult();
					}
					if(i == answerOptionList.size() - 1){
						questionContentItemRadioButtonView.setQuestionContentItemRadioButtonView(answerOptions,true);
					}else{
						questionContentItemRadioButtonView.setQuestionContentItemRadioButtonView(answerOptions,false);
					}
					
					if(questionnaireContentActivity == null){
						
					}
					if (isClearOptions) {
						questionContentItemRadioButtonView.setSelected(false);
					}
				
					if(answerOptions.getProblemId() == pId){
						questionContentItemRadioButtonView.setText("");
						if(f.size()>0){
							if(String.valueOf(answerOptions.getOptionsId()).equals(f.get(0).getChoiceOptions())){
								questionContentItemRadioButtonView.setSelected(true);
								questionContentItemRadioButtonView.setText(f.get(0).getFillOptions());
							}
						}
						
					}
					
		
					ll_add_item_view.addView(questionContentItemRadioButtonView.getView());
					questionContentItemRadioButtonViews.add(questionContentItemRadioButtonView);
					
					
						if(answerOptions.getIsSave() == 1){
							if(SharedPreferencesUtil.getInstance(context).getIfSave(question.getQuestionnaireId() + "save").equals(PublicUtils.getResourceString(context,R.string.yes))){
							questionContentItemRadioButtonView.setSelected(true);
							if(answerOptions.getOptions().equals(PublicUtils.getResourceString(context,R.string.others))){
								questionContentItemRadioButtonView.setText(answerOptions.getOptionsRemarks());
							}
						}
							
						}
						if(isPreview){
							questionContentItemRadioButtonView.setText("");
							questionContentItemRadioButtonView.setSelected(false);
						}
						if(questionnaireContentActivity != null){
						questionnaireContentActivity.questionContentItemRadioButtonViews.add(questionContentItemRadioButtonView);
					}
		
			}
			
		}
		else if(question.getQuestionDiscriminate() == 3){
		//3多选
			if(questionnaireContentActivity != null){
//				questionnaireContentActivity.questionContentItemMoreButtonViews.clear();
				
			}
			for(int i= 0;i < answerOptionList.size();i ++){
				AnswerOptions answerOptions = answerOptionList.get(i);
				QuestionContentItemMoreButtonView questionContentItemMoreButtonView = new QuestionContentItemMoreButtonView(context,isPreview);
				if(questionnaireContentActivity != null){
					questionContentItemMoreButtonView.setQActivity4(questionnaireContentActivity);
				}
				questionContentItemMoreButtonView.setQuestionContentItemRadioButtonView(answerOptions,false);
			
				if(answerOptions.getProblemId() == pId){
					if (isClearOptions) {
						questionContentItemMoreButtonView.setSelect(false);
					}
				
					for (int j = 0; j < f.size(); j++) {
						if(String.valueOf(answerOptions.getOptionsId()).equals(f.get(j).getChoiceOptions())){
							questionContentItemMoreButtonView.setSelect(true);
						}
					}
						
				}
				if(isPreview){
					questionContentItemMoreButtonView.setSelect(false);
				}
				ll_add_item_view.addView(questionContentItemMoreButtonView.getView());
				questionContentItemMoreButtonViews.add(questionContentItemMoreButtonView);
				if(questionnaireContentActivity != null){
					questionnaireContentActivity.questionContentItemMoreButtonViews.add(questionContentItemMoreButtonView);
					
				}
			}
		}else if(question.getQuestionDiscriminate() == 4){
		//4多选其他
//			if(questionnaireContentActivity != null){
//				questionnaireContentActivity.questionContentItemMoreButtonViews.clear();
//				
//			}
			for(int i= 0;i < answerOptionList.size();i ++){
				AnswerOptions answerOptions = answerOptionList.get(i);
				QuestionContentItemMoreButtonView questionContentItemMoreButtonView = new QuestionContentItemMoreButtonView(context,isPreview);
				if(i == answerOptionList.size() - 1){
					questionContentItemMoreButtonView.setQuestionContentItemRadioButtonView(answerOptions,true);
				}else{
					questionContentItemMoreButtonView.setQuestionContentItemRadioButtonView(answerOptions,false);
				}
				if (isClearOptions) {
					questionContentItemMoreButtonView.setSelect(false);
					questionContentItemMoreButtonView.setOptionsContent("");
				}
				if(answerOptions.getProblemId() == pId){
	
			
					for (int j = 0; j < f.size(); j++) {
						if(String.valueOf(answerOptions.getOptionsId()).equals(f.get(j).getChoiceOptions())){
							questionContentItemMoreButtonView.setSelect(true);
							if(i == answerOptionList.size() - 1){
								questionContentItemMoreButtonView.setOptionsContent(f.get(j).getFillOptions());
							}
						}
					}
					
				}
				if(isPreview){
					questionContentItemMoreButtonView.setSelect(false);
				}
				ll_add_item_view.addView(questionContentItemMoreButtonView.getView());
				questionContentItemMoreButtonViews.add(questionContentItemMoreButtonView);
				if(questionnaireContentActivity != null){
					questionnaireContentActivity.questionContentItemMoreButtonViews.add(questionContentItemMoreButtonView);
					
				}
			}
			
		}else if(question.getQuestionDiscriminate() == 5){
		//5填空
			QuestionContentItemEditTextView questionContentItemEditTextView = new QuestionContentItemEditTextView(context,isPreview,rId);
			questionContentItemEditTextView.setQuestionContentItemRadioButtonView(question);
			ll_add_item_view.addView(questionContentItemEditTextView.getView());
			questionContentItemEditTextViews.add(questionContentItemEditTextView);
			if(questionnaireContentActivity != null){
				questionnaireContentActivity.questionContentItemEditTextViews.add(questionContentItemEditTextView);
			}
			
		}

		
	}
	
	public void getDanxuanList(){
		
	}

	public View getView() {
		return view;
	}

}
