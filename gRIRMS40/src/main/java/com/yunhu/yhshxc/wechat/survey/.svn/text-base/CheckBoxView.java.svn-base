package com.yunhu.yhshxc.wechat.survey;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.wechat.bo.Survey;

public class CheckBoxView {


	private View view;
	private Context context;
	private Survey survey = new Survey();

	private CheckBox cb_wechat_survey;
	private TextView tv_wechat_content;
	private String options = "";
	private SurveyDialiog surveyDialiog;

	

	public CheckBoxView(Context context) {
		this.context = context;
		view = View.inflate(context, R.layout.activity_wechat_survey_checkbox, null);
		
		cb_wechat_survey = (CheckBox) view.findViewById(R.id.cb_wechat_survey);
		tv_wechat_content = (TextView) view.findViewById(R.id.tv_wechat_content);
		
		cb_wechat_survey.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					
					if(survey.getType() == Survey.TYPE_1){
						for(int i= 0;i < surveyDialiog.checkBoxViewList.size();i++){
							CheckBoxView checkBoxView = surveyDialiog.checkBoxViewList.get(i);
							checkBoxView.setOptions();
						}
						
					}
						cb_wechat_survey.setChecked(true);
						options = tv_wechat_content.getText().toString();
				}else{
					options = "";
				}
			}
		});

	}

	/**
	 * 设置调查类评审评论信息
	 */
	public void setSurvey(Survey survey) {		
		this.survey = survey;
		tv_wechat_content.setText(survey.getOptions());

	}

	public void setChecked(){
		cb_wechat_survey.setChecked(true);
	}

	public View getView() {
		return view;
	}
	public String getOptions(){
		return options;
	}
	
	public void setOptions(){
		cb_wechat_survey.setChecked(false);
		options = "";
	}

	public void setSurDialog(SurveyDialiog surveyDialiog){
		this.surveyDialiog = surveyDialiog;
	}


}
