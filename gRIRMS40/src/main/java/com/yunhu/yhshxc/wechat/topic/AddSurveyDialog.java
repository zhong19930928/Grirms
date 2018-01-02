package com.yunhu.yhshxc.wechat.topic;

import java.util.ArrayList;
import java.util.List;

import gcg.org.debug.JLog;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.wechat.bo.Survey;
import com.yunhu.yhshxc.widget.ToastOrder;

public class AddSurveyDialog extends Dialog implements android.view.View.OnClickListener {

	private Context context;
	private Button btn_survey_submit;
	private Button btn_add_survey_content;
	private LinearLayout ll_add_survey;
	private ScrollView sv_survey;
	private RadioGroup rd_if_double;
	private RadioButton rd_one;
	private RadioButton rd_double;
	private int type = Survey.TYPE_1;// 默认为单选
	
	private CreateTopicActivity createTopicActivity;
	public List<AddSurveySub> surveySubList = new ArrayList<AddSurveySub>();
	private List<AddSurveySubView> surveySubViewList = new ArrayList<AddSurveySubView>();

	public AddSurveyDialog(Context context) {
		super(context);
		this.context = context;
		createTopicActivity = (CreateTopicActivity)context;
		// 加载布局文件
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		setTitle(R.string.wechat_content11);
		View view = inflater.inflate(R.layout.dialog_add_survey, null);
		btn_survey_submit = (Button) view.findViewById(R.id.btn_survey_submit);
		btn_add_survey_content = (Button) view.findViewById(R.id.btn_add_survey_content);
		ll_add_survey = (LinearLayout) view.findViewById(R.id.ll_add_survey);
		sv_survey = (ScrollView)view.findViewById(R.id.sv_survey);
		rd_if_double = (RadioGroup) view.findViewById(R.id.rd_if_double);
		rd_one = (RadioButton) view.findViewById(R.id.rd_one);
		rd_double = (RadioButton) view.findViewById(R.id.rd_double);
//		ed_add_content = (EditText) view.findViewById(R.id.ed_add_content);

		btn_survey_submit.setOnClickListener(this);
		btn_add_survey_content.setOnClickListener(this);

		rd_if_double.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId != rd_one.getId()) {
					rd_one.setChecked(false);
					if (checkedId == rd_double.getId()) {
						type = Survey.TYPE_2;
					}
				}else{
					rd_double.setChecked(false);
					type = Survey.TYPE_1;
				}
			}
		});

		// dialog添加视图
		setContentView(view);
		initData();

	}

	private void initData() {
//		try {
//			JSONObject jsonObject = new JSONObject();
//			jsonObject.put("1", "优秀");
//			jsonArray.put(jsonObject);
//			JSONObject jsonObject2 = new JSONObject();
//			jsonObject2.put("2", "良好");
//			jsonArray.put(jsonObject2);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}

		AddSurveySub s1 = new AddSurveySub();
		s1.setNumber("1");
		s1.setContent(PublicUtils.getResourceString(context,R.string.excellent));
		surveySubList.add(s1);
		
		AddSurveySubView sv1 = new AddSurveySubView(context,this);
		sv1.setData(s1);
		surveySubViewList.add(sv1);
		ll_add_survey.addView(sv1.getView());

		AddSurveySub s2 = new AddSurveySub();
		s2.setNumber("2");
		s2.setContent(PublicUtils.getResourceString(context,R.string.good));
		surveySubList.add(s2);
		
		AddSurveySubView sv2 = new AddSurveySubView(context,this);
		sv2.setData(s2);
		surveySubViewList.add(sv2);
		ll_add_survey.addView(sv2.getView());
	}
	

	private void addSub(){
		AddSurveySub sub = new AddSurveySub();
		sub.setNumber(String.valueOf(surveySubList.size()+1));
		surveySubList.add(sub);
		AddSurveySubView subView = new AddSurveySubView(context,this);
		subView.setList(surveySubList);
		subView.setData(sub);
		surveySubViewList.add(subView);
		ll_add_survey.addView(subView.getView());
		sv_survey.fullScroll(ScrollView.FOCUS_DOWN);
	}

	public void setMsg(String msg) {
	}

	public void setMsg(int msgId) {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_survey_submit:
			
			submit();
//			if (!TextUtils.isEmpty(ed_add_content.getText().toString())) {
//				try {
//					JSONObject jsonObject = new JSONObject();
//					jsonObject.put("3", ed_add_content.getText().toString());
//					jsonArray.put(jsonObject);
//
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//			}
//
//			Survey survey = new Survey();
//			survey.setSurveyType(Survey.SURVEYTYPE_1);// 调查类
//			survey.setType(type);
//			survey.setOptions(jsonArray.toString());
//			JLog.d("abby", "调查:options" + jsonArray.toString());
//
//			createTopicActivity.setSurvey(survey);
			break;
		case R.id.btn_add_survey_content:
//			ll_add_survey_content.setVisibility(View.VISIBLE);
			addSub();

			break;

		default:
			break;
		}
	}
	 
	private void submit(){
		try {
			JSONArray array = new JSONArray();
			int index = 1;
			Boolean isError = false;
			for (int i = 0; i < surveySubViewList.size(); i++) {
				JSONObject obj = new JSONObject();
				AddSurveySubView view = surveySubViewList.get(i);
				if(view.isError()){
					ToastOrder.makeText(context, R.string.wechat_content10, ToastOrder.LENGTH_SHORT).show();
					isError = true;
					break;
				}else{
					AddSurveySub sub = view.getAddSurveySub();
					if (!TextUtils.isEmpty(sub.getContent().toString().trim())) {
						obj.put(String.valueOf(index), sub.getContent());
						array.put(obj);
						index++;
					}
				}
				
			}
			if(!isError){
				Survey survey = new Survey();
				survey.setSurveyType(Survey.SURVEYTYPE_1);// 调查类
				survey.setType(type);
				survey.setOptions(array.toString());
				JLog.d("abby", "调查:options" + array.toString());
				createTopicActivity.setSurvey(survey);
				dismiss();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
