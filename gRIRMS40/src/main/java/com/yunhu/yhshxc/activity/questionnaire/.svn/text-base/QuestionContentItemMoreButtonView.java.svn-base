package com.yunhu.yhshxc.activity.questionnaire;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.questionnaire.bo.AnswerOptions;
import com.yunhu.yhshxc.activity.questionnaire.bo.FindIngDetail;
import com.yunhu.yhshxc.activity.questionnaire.db.FindIngDetailDB;

public class QuestionContentItemMoreButtonView implements OnClickListener {

	private View view;
	private Context context;
	private AnswerOptions answerOptions;
	private LinearLayout ll_pic_question_more;
	private ImageView iv_pic_question_more;
	private Boolean ifSelected = false;
	private TextView tv_option_content;
	private LinearLayout ll_option_content;
	private EditText ed_option_content;
	private FindIngDetailDB findIngDetailDB;
	private QuestionnaireContentActivity questionnaireContentActivity;
	private Boolean ifEdit;
	private boolean isPreview;

	public QuestionContentItemMoreButtonView(Context context, boolean isPreview) {
		this.context = context;
		findIngDetailDB = new FindIngDetailDB(context);
		this.isPreview = isPreview;
		view = View.inflate(context,
				R.layout.activity_qusetion_content_item_more_button, null);
		iv_pic_question_more = (ImageView) view
				.findViewById(R.id.iv_pic_question_more);
		tv_option_content = (TextView) view
				.findViewById(R.id.tv_option_content);
		ll_pic_question_more = (LinearLayout) view
				.findViewById(R.id.ll_pic_question_more);
		ll_option_content = (LinearLayout) view
				.findViewById(R.id.ll_option_content);
		ed_option_content = (EditText) view
				.findViewById(R.id.ed_option_content);
		ll_pic_question_more.setEnabled(isPreview);
		ed_option_content.setEnabled(isPreview);
		ll_pic_question_more.setOnClickListener(this);

	}
    public void setOptionsContent(String s){
    	ed_option_content.setText(s);
    }
	public void setQuestionContentItemRadioButtonView(AnswerOptions answerOptions , Boolean ifEdit) {
		this.answerOptions = answerOptions;
		this.ifEdit = ifEdit;
		if(ifEdit){
			tv_option_content.setVisibility(View.GONE);
			ll_option_content.setVisibility(View.VISIBLE);
			ed_option_content.setHint(answerOptions.getOptions());
		}else{
			ll_option_content.setVisibility(View.GONE);
			tv_option_content.setVisibility(View.VISIBLE);
			tv_option_content.setText(answerOptions.getOptions());		
			
		}
		
		//FindIngDetail f = findIngDetailDB.findFindIngDetailById(getAId());
		FindIngDetail f = findIngDetailDB.findFindIngDetailByQId(getQId());
		if(f != null){
			if(!TextUtils.isEmpty(f.getChoiceOptions())){
				String[] fStr = f.getChoiceOptions().split(",");
try{
				if(fStr.length>0){
					for(int i = 0 ; i < fStr.length ; i++){
						if(!TextUtils.isEmpty(fStr[i])&&!fStr[i].equals("null")){
							if(Integer.parseInt(fStr[i]) == getAId() && questionnaireContentActivity == null){//数据库中存在并且为预览界面时
								ifSelected = true;
								iv_pic_question_more.setBackgroundResource(R.drawable.pic_question_more_selected);
								
								if(ifEdit){
									ed_option_content.setText(f.getFillOptions());
								}
									ed_option_content.setEnabled(isPreview);
							}

						}
					}
		
				}
			}catch(Exception e){
			e.printStackTrace();
			if(f.getChoiceOptions().equals(answerOptions.getOptions())){
				ifSelected = true;
				iv_pic_question_more.setBackgroundResource(R.drawable.pic_question_more_selected);
				if(ifEdit){
					ed_option_content.setText(f.getFillOptions());
				}
			}
			
			
			}
		
			}
		}
	}
	
	public View getView() {
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ll_pic_question_more:
			if (ifSelected) {
				ifSelected = false;
				iv_pic_question_more
						.setBackgroundResource(R.drawable.pic_question_more);
			} else {
				ifSelected = true;
				iv_pic_question_more
						.setBackgroundResource(R.drawable.pic_question_more_selected);
			}
			break;

		default:
			break;
		}
	}

	public void setSelect(boolean isSelcted) {
		ifSelected = isSelcted;
		if (isSelcted) {
			iv_pic_question_more
					.setBackgroundResource(R.drawable.pic_question_more_selected);
		} else {
			iv_pic_question_more
					.setBackgroundResource(R.drawable.pic_question_more);
		}

	}

	public void setQActivity4(Context context) {
		questionnaireContentActivity = (QuestionnaireContentActivity) context;
	}

	public Boolean getSelected() {
		return ifSelected;
	}

	/**
	 * 获得问题Id
	 * 
	 * @return
	 */
	public int getQId() {
		return answerOptions.getProblemId();
	}

	/**
	 * 获得选项id
	 * 
	 * @return
	 */
	public int getAId() {
		return answerOptions.getOptionsId();
	}

	/**
	 * 获取是否为其他 true为是，false为否
	 * 
	 * @return
	 */
	public Boolean getIfEdit() {
		return ifEdit;
	}

	/**
	 * 获得单选其他
	 */
	public String getContent() {
		return ed_option_content.getText().toString();
	}

}
