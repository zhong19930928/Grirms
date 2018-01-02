package com.yunhu.yhshxc.wechat.topic;

import java.util.ArrayList;
import java.util.List;

import com.yunhu.yhshxc.R;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class AddSurveySubView {
	private View view;
	private Context context;
	private TextView tv_survey_bh;
	private EditText et_survey_content;
	private AddSurveySub surveySub;
	private List<AddSurveySub> surveySubList = new ArrayList<AddSurveySub>();
	private AddSurveyDialog addSurveyDialog;
	
	public AddSurveySubView(Context mContext,AddSurveyDialog context) {
		this.addSurveyDialog = (AddSurveyDialog)context;
		view = View.inflate(mContext, R.layout.add_survey_sub_view, null);
		tv_survey_bh = (TextView)view.findViewById(R.id.tv_survey_bh);
		et_survey_content = (EditText)view.findViewById(R.id.et_survey_content);
	    surveySubList = addSurveyDialog.surveySubList;
		et_survey_content.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
				/**
				 * 设置调查选项不可重复
				 */
//					for(int i =0;i < surveySubList.size();i++){
//						
//						AddSurveySub addSurveySub = surveySubList.get(i);
//						if(Integer.parseInt(tv_survey_bh.getText().toString()) != (i + 1)){
//							if(!TextUtils.isEmpty(addSurveySub.getContent())){
//								if(addSurveySub.getContent().equals(et_survey_content.getText().toString())){
//									et_survey_content.setError("调查选项不能重复！");
//								}
//							}
//						}else{
//							addSurveySub.setContent(et_survey_content.getText().toString());
//							surveySubList.set(i, addSurveySub);
//						}
//						
//						
//				}
				
				
			
				}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub


				
			}
		});
	
	}
 
	public Boolean isError(){
		if(et_survey_content.getError() != null){
			return true;
		}
		return false;
	}
	
	public void setList(List<AddSurveySub> surveySubList){
		this.surveySubList = surveySubList;
	}
	
	public void setData( AddSurveySub data){
		if (data!=null) {
			this.surveySub = data;
			String number = surveySub.getNumber();
			String content = surveySub.getContent();
			if (!TextUtils.isEmpty(number)) {
				tv_survey_bh.setText(number);
			}
			if (!TextUtils.isEmpty(content)) {
				et_survey_content.setText(content);
			}
		}
	}
	
	public AddSurveySub getAddSurveySub(){
		if (surveySub == null) {
			surveySub = new AddSurveySub();
		}
		surveySub.setNumber(tv_survey_bh.getText().toString());
		surveySub.setContent(et_survey_content.getText().toString());
		return surveySub;
	}
	
	public View getView(){
		
		return view;
	}
}
