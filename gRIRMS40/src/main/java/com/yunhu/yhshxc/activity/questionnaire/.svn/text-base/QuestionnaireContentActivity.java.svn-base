package com.yunhu.yhshxc.activity.questionnaire;

import gcg.org.debug.JLog;
import view.SearchDialogView;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.activity.questionnaire.Util.QuestionnaireUtil;
import com.yunhu.yhshxc.activity.questionnaire.bo.AnswerOptions;
import com.yunhu.yhshxc.activity.questionnaire.bo.FindIngDetail;
import com.yunhu.yhshxc.activity.questionnaire.bo.Question;
import com.yunhu.yhshxc.activity.questionnaire.bo.Questionnaire;
import com.yunhu.yhshxc.activity.questionnaire.bo.SurveyAdress;
import com.yunhu.yhshxc.activity.questionnaire.db.AnswerOptionsDB;
import com.yunhu.yhshxc.activity.questionnaire.db.FindIngDetailDB;
import com.yunhu.yhshxc.activity.questionnaire.db.QuestionDB;
import com.yunhu.yhshxc.activity.questionnaire.db.QuestionnaireDB;
import com.yunhu.yhshxc.activity.questionnaire.db.SurveyAdressDB;
import com.yunhu.yhshxc.bo.LocationResult;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.location.ReceiveLocationListener;
import com.yunhu.yhshxc.location2.LocationFactoy;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.loopj.android.http.RequestParams;

public class QuestionnaireContentActivity extends AbsBaseActivity {

	private Button btn_question_content_back;
	private LinearLayout ll_add_content_item_title;
	private List<Question> questionList = new ArrayList<Question>();// 章节
	private List<Question> problemList = new ArrayList<Question>();// 问题
	private List<QuestionnaireContentView> questionnaireContentViews = new ArrayList<QuestionnaireContentView>();
	// private Button btn_question_content_save;
	// private Button btn_question_content_show;
	// private Button btn_question_content_submit;
	private Button sp_question_title_show;
	private Button btn_question_preview;
	private Button btn_question_content_explain;
	private List<SurveyAdress> list = new ArrayList<SurveyAdress>();
	private ArrayAdapter<String> adapter;
	private QuestionDB questionDB;
	private SurveyAdressDB surveyAdressDB;
	private List<SurveyAdress> surveyAdressList = new ArrayList<>();
	private Questionnaire questionnaire = new Questionnaire();
	private QuestionnaireDB questionnaireDB;
//	private SurveyAdress sAdress = new SurveyAdress();
	private int qId;
	private FindIngDetailDB findIngDetailDB;
	public List<QuestionContentItemRadioButtonView> questionContentItemRadioButtonViews = new ArrayList<QuestionContentItemRadioButtonView>();// 存放单选view的list
	public List<QuestionContentItemMoreButtonView> questionContentItemMoreButtonViews = new ArrayList<QuestionContentItemMoreButtonView>();// 存放多选view的list
	public List<QuestionContentItemEditTextView> questionContentItemEditTextViews = new ArrayList<QuestionContentItemEditTextView>();// 存放输入框view的list
	private Button btn_question_submit;
	private Button btn_question_save;
	private AnswerOptionsDB answerOptionsDB;
	private String startTime;
	private String endTime;
	private Double Longitude = null;
	private Double Latitude = null;
	private String address = "";
	private SurveyAdress addressSelected =null;
	private ArrayList<Question> allProblemList = new ArrayList<Question>();// 所有问题的集合

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_question_content);
		questionDB = new QuestionDB(this);
		surveyAdressDB = new SurveyAdressDB(this);
		findIngDetailDB = new FindIngDetailDB(this);
		questionnaireDB = new QuestionnaireDB(this);
		answerOptionsDB = new AnswerOptionsDB(this);

		init();
	}

	private void init() {
		Intent intent = getIntent();
		qId = Integer.parseInt(intent.getStringExtra("qId"));
		startTime = DateUtil.getCurDateTime();

		btn_question_content_back = (Button) findViewById(R.id.btn_question_content_back);
		ll_add_content_item_title = (LinearLayout) findViewById(R.id.ll_add_content_item_title);
		// btn_question_content_save = (Button)
		// findViewById(R.id.btn_question_content_save);
		// btn_question_content_show = (Button)
		// findViewById(R.id.btn_question_content_show);
		// btn_question_content_submit = (Button)
		// findViewById(R.id.btn_question_content_submit);
		sp_question_title_show = (Button) findViewById(R.id.sp_question_title_show);
		btn_question_preview = (Button) findViewById(R.id.btn_question_preview);
		btn_question_content_explain = (Button) findViewById(R.id.btn_question_content_explain);
		btn_question_submit = (Button) findViewById(R.id.btn_question_submit);
		btn_question_save = (Button) findViewById(R.id.btn_question_save);
		btn_question_save.setVisibility(View.INVISIBLE);

		btn_question_content_back.setOnClickListener(this);
		// btn_question_content_save.setOnClickListener(this);
		// btn_question_content_show.setOnClickListener(this);
		// btn_question_content_submit.setOnClickListener(this);
		btn_question_preview.setOnClickListener(this);
		btn_question_content_explain.setOnClickListener(this);
		btn_question_submit.setOnClickListener(this);
		btn_question_save.setOnClickListener(this);
		sp_question_title_show.setOnClickListener(this);
//		sp_question_title_show.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//				// TODO Auto-generated method stub
//				addressSelected = list.get(position);
//				TextView tv = (TextView) view;
//				tv.setTextSize(14);
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> parent) {
//				// TODO Auto-generated method stub
//
//			}
//		});

		addView();
		initData();
	}

	private void initData() {
		surveyAdressList = surveyAdressDB.findSurveyAdressByqId(qId);
		if (surveyAdressList != null) {
					list.addAll(surveyAdressList);

		}
		//默认为第一个地点
	if(list!=null&&list.size()>0){
		
		sp_question_title_show.setText(list.get(0).getAdress());
		addressSelected=list.get(0);
	}
//		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
		// 第三步：为适配器设置下拉列表下拉时的菜单样式。
//		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 第四步：将适配器添加到下拉列表上
//		sp_question_title_show.setAdapter(adapter);
	}

	public void addView() {
		questionList = questionDB.findQuestionListById(qId, 1);// 查询章节

		for (int i = 0; i < questionList.size(); i++) {
			QuestionnaireContentView questionnaireContentView = new QuestionnaireContentView(this, true);
			Question question = questionList.get(i);
			problemList = questionDB.findQuestionListByCId(qId, 2, question.getQuestionId());// 查询问题
			allProblemList.addAll(problemList);
			questionnaireContentView.setQActivity(this);
			questionnaireContentView.setQuestion(question);
			questionnaireContentView.setQuestionContent(problemList, i);
			ll_add_content_item_title.addView(questionnaireContentView.getView());
			questionnaireContentViews.add(questionnaireContentView);
		}

	}

	private RequestParams searchParams() throws JSONException {

		RequestParams param = new RequestParams();
		param.put("phoneno", PublicUtils.receivePhoneNO(getApplicationContext()));
		QuestionnaireUtil questionnaireUtil = new QuestionnaireUtil(this);
		questionnaire = questionnaireDB.findQuestionnaireById(qId);
//		sAdress = surveyAdressDB.findSurveyAdressByqId(qId);
		List<Question> questionList = questionDB.findAllQuestion(qId);
		List<FindIngDetail> findIngDetails = new ArrayList<FindIngDetail>();
		for (int i = 0; i < questionList.size(); i++) {
			Question question = questionList.get(i);
			int id = question.getQuestionId();
			FindIngDetail findIngDetail = findIngDetailDB.findFindIngDetailByQId(id);
			if (findIngDetail != null) {
				findIngDetails.add(findIngDetail);
			}
		}

		param.put("data", questionnaireUtil.submitQuestionJson(questionnaire,addressSelected, findIngDetails, startTime,
				Longitude, Latitude, address));
		JLog.d(TAG, param.toString());
		return param;
	}

	// 保存调查问卷内容
	private Dialog dialog = null;

	private void saveContent() throws JSONException {
		dialog = new MyProgressDialog(this, R.style.CustomProgressDialog, PublicUtils.getResourceString(this,R.string.submint_loding));
		String url = UrlInfo.doQuestionInfo(this);
		RequestParams param = searchParams();
		GcgHttpClient.getInstance(this.getApplicationContext()).post(url, param, new HttpResponseListener() {
			@Override
			public void onStart() {
				dialog.show();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				JLog.d(TAG, "onSuccess:" + content);
				try {
					JSONObject obj = new JSONObject(content);
					String resultCode = obj.getString("resultcode");
					if ("0000".equals(resultCode)) {
						ToastOrder.makeText(getApplicationContext(), R.string.submit_ok, ToastOrder.LENGTH_SHORT).show();
						SharedPreferencesUtil.getInstance(getApplicationContext())
								.setIfSave(questionnaire.getId() + "save", PublicUtils.getResourceString(getApplicationContext(),R.string.qustion_no));
						SharedPreferencesUtil.getInstance(getApplicationContext())
								.setFinding(questionnaire.getQuestionId() + "time", DateUtil.getCurDate());

						int nums = Integer.parseInt(SharedPreferencesUtil.getInstance(getApplicationContext())
								.getFindingNums(questionnaire.getQuestionId() + "nums")) + 1;
						SharedPreferencesUtil.getInstance(getApplicationContext())
								.setFindingNums(questionnaire.getQuestionId() + "nums", nums + "");
						List<AnswerOptions> answerOptions = answerOptionsDB.findAnswerOptionsByQId(qId);
						for (int i = 0; i < answerOptions.size(); i++) {
							AnswerOptions answerOption = answerOptions.get(i);
							answerOption.setIsSave(0);
							answerOptionsDB.updateOption(answerOption);
						}

//						Intent intent = new Intent(getApplicationContext(), QuestionnaireActivity.class);
//						startActivity(intent);
						finish();
					} else {
						throw new Exception();
					}
				} catch (Exception e) {
					e.printStackTrace();
					ToastOrder.makeText(getApplicationContext(), R.string.ERROR_DATA, ToastOrder.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				JLog.d(TAG, "onFailure:" + content);
				ToastOrder.makeText(getApplicationContext(), R.string.retry_net_exception, ToastOrder.LENGTH_SHORT).show();
			}

			@Override
			public void onFinish() {
				dialog.dismiss();
			}

		});

	}

	private Boolean ifSubmit = true;

	// 展示调查问卷内容
	private void showContent(Boolean ifS, Boolean isSave) {
		// 查询单选及单选其他结果
		for (int j = 0; j < questionContentItemRadioButtonViews.size(); j++) {
			QuestionContentItemRadioButtonView questionContentItemRadioButtonView = questionContentItemRadioButtonViews
					.get(j);
			if (questionContentItemRadioButtonView.getSelected()) {
				FindIngDetail findIngDetail = new FindIngDetail();
				findIngDetail.setQuestionId(questionContentItemRadioButtonView.getQId());
				findIngDetail.setChoiceOptions(questionContentItemRadioButtonView.getAId() + "");
				if (!TextUtils.isEmpty(findIngDetail.getChoiceOptions())) {
					if (findIngDetail.getChoiceOptions().equals(PublicUtils.getResourceString(getApplicationContext(),R.string.others))) {
						findIngDetail.setFillOptions(questionContentItemRadioButtonView.getContent());
					}
				}
				if (questionContentItemRadioButtonView.getIfEdit()) {
					findIngDetail.setFillOptions(questionContentItemRadioButtonView.getContent());
				}
				FindIngDetail f = findIngDetailDB.findFindIngDetailByQId(questionContentItemRadioButtonView.getQId());
				if (f != null) {
					findIngDetailDB.updateFindIngDetail(findIngDetail);
				} else {
					findIngDetailDB.insertFindIngDetail(findIngDetail);
				}

			}else{
				findIngDetailDB.deleteFindIngDetailByOptionId(questionContentItemRadioButtonView.getAId() + "");
			}

		}
		// 查询多选及多选其他结果
		for (int i = 0; i < questionContentItemMoreButtonViews.size(); i++) {
			QuestionContentItemMoreButtonView questionContentItemMoreButtonView = questionContentItemMoreButtonViews
					.get(i);
			if (questionContentItemMoreButtonView.getSelected()) {
				FindIngDetail findIngDetail = new FindIngDetail();
				findIngDetail.setQuestionId(questionContentItemMoreButtonView.getQId());
				if (questionContentItemMoreButtonView.getIfEdit()) {
					findIngDetail.setFillOptions(questionContentItemMoreButtonView.getContent());
				}
				FindIngDetail f = findIngDetailDB.findFindIngDetailByQId(questionContentItemMoreButtonView.getQId());
				// FindIngDetail f =
				// findIngDetailDB.findFindIngDetailById(questionContentItemMoreButtonView.getAId());
				if (f != null) {
					if (f.getChoiceOptions() != null) {
						String[] fStr = f.getChoiceOptions().split(",");
						Boolean ifHave = false;
						if (fStr.length > 0) {
							for (int p = 0; p < fStr.length; p++) {
								if (!TextUtils.isEmpty(fStr[p]) && !fStr[p].equals("null")) {
									if (questionContentItemMoreButtonView.getAId() == Integer.parseInt(fStr[p])) {
										ifHave = true;
									}
								}

							}
						}
						if (ifHave) {
							findIngDetail.setChoiceOptions(f.getChoiceOptions());
						} else {
							findIngDetail.setChoiceOptions(
									f.getChoiceOptions() + "," + questionContentItemMoreButtonView.getAId());
						}
					} else {
						findIngDetail.setChoiceOptions(questionContentItemMoreButtonView.getAId() + "");
					}
					findIngDetailDB.updateFindIngDetail(findIngDetail);
				} else {
					findIngDetail.setChoiceOptions(questionContentItemMoreButtonView.getAId() + "");
					findIngDetailDB.insertFindIngDetail(findIngDetail);
				}

			} else {
				FindIngDetail f = findIngDetailDB.findFindIngDetailByQId(questionContentItemMoreButtonView.getQId());
				if (f != null) {
					if (!TextUtils.isEmpty(f.getChoiceOptions())) {
						String[] arr = f.getChoiceOptions().split(",");
						arr = remove(arr, questionContentItemMoreButtonView.getAId() + "");
						StringBuffer sb = new StringBuffer();
						for (int j = 0; j < arr.length; j++) {
							if (!TextUtils.isEmpty(arr[j]) && !arr[j].equals("null")) {
								sb.append(arr[j]).append(",");
							}
						}
						if (sb.length() > 0) {
							f.setChoiceOptions(sb.substring(0, sb.length() - 1));
						}else{
							f.setChoiceOptions("");
						}
					}
					findIngDetailDB.updateFindIngDetail(f);
				}
			}

		}

		// 填空
		for (int i = 0; i < questionContentItemEditTextViews.size(); i++) {
			QuestionContentItemEditTextView questionContentItemEditTextView = questionContentItemEditTextViews.get(i);
			if (questionContentItemEditTextView.ifEdit()) {
				FindIngDetail findIngDetail = new FindIngDetail();
				findIngDetail.setQuestionId(questionContentItemEditTextView.getQId());
				findIngDetail.setFillOptions(questionContentItemEditTextView.getContent());

				FindIngDetail f = findIngDetailDB.findFindIngDetailByQId(questionContentItemEditTextView.getQId());
				if (f != null) {
					findIngDetailDB.updateFindIngDetail(findIngDetail);
				} else {
					findIngDetailDB.insertFindIngDetail(findIngDetail);
				}

			}
		}

		if (ifS) {

			int completeCount = 0;
			// 至少
			for (int i = 0; i < allProblemList.size(); i++) {
				FindIngDetail f = findIngDetailDB.findFindIngDetailByQId(allProblemList.get(i).getQuestionId());
				if (f != null && (TextUtils.isEmpty(f.getChoiceOptions()) || TextUtils.isEmpty(f.getFillOptions()))) {
					completeCount++;
				}

			}
			if (completeCount == 0) {

				ToastOrder.makeText(getApplicationContext(), R.string.question_exit2, ToastOrder.LENGTH_SHORT).show();
				ifSubmit = true;
				isSubmitOnce = false;
				return;
			}

			for (int i = 0; i < allProblemList.size(); i++) {
				if (2 == allProblemList.get(i).getIsAnswer()) {

					FindIngDetail f = findIngDetailDB.findFindIngDetailByQId(allProblemList.get(i).getQuestionId());
					if (f == null) {
						ifSubmit = false;
					} else if (TextUtils.isEmpty(f.getChoiceOptions()) && TextUtils.isEmpty(f.getFillOptions())) {
						ifSubmit = false;
					}

				}
			}

			if (ifSubmit) {
				try {

					saveContent();

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				ToastOrder.makeText(getApplicationContext(), R.string.question_exit3, ToastOrder.LENGTH_SHORT).show();
				ifSubmit = true;
				isSubmitOnce = false;
			}
		}

	}

	// 一次只能删除一个元素
	private String[] remove(String[] arr, String num) {

		String[] tmp = new String[arr.length];

		int idx = 0;
		boolean hasRemove = false;
		for (int i = 0; i < arr.length; i++) {

			if (!hasRemove && arr[i].equals(num)) {
				hasRemove = true;
				continue;
			}
			tmp[idx++] = arr[i];

		}

		return tmp;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			clickBackButton();

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void clickBackButton() {
		// TODO Auto-generated method stub
		if (!QuestionnaireContentActivity.this.isFinishing()) {
			Dialog dialog = null;
			dialog = backDialog(PublicUtils.getResourceString(this,R.string.question_exit));
			dialog.show();
		}
	}

	/**
	 * 创建提示用户是否退出的模态对话框，如果用户选择退出，则清除本地临时缓存的图片
	 * 
	 * @param bContent
	 *            询问用户的文字
	 * @return 返回一个构造好的Dialog对象
	 */
	private Dialog backDialog(String bContent) {
		final Dialog backDialog = new Dialog(this, R.style.transparentDialog);
		View view = View.inflate(this, R.layout.back_dialog, null);
		TextView backContent = (TextView) view.findViewById(R.id.tv_back_content);
		Button confirm = (Button) view.findViewById(R.id.btn_back_confirm);
		Button cancel = (Button) view.findViewById(R.id.btn_back_cancel);
		backContent.setText(bContent);
		confirm.setOnClickListener(new OnClickListener() {

			/**
			 * 用户退出
			 */
			@Override
			public void onClick(View v) {
				backDialog.dismiss();
				findIngDetailDB.clearFindIngDetail();
				QuestionnaireContentActivity.this.finish();
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			/**
			 * 取消退出
			 */
			@Override
			public void onClick(View v) {
				backDialog.dismiss();
			}
		});
		backDialog.setContentView(view);
		backDialog.setCancelable(false);// 将对话框设置为模态
		return backDialog;
	}

	/**
	 * 定位回调监听
	 */
	ReceiveLocationListener receiveLocationListener = new ReceiveLocationListener() {

		@Override
		public void onReceiveResult(LocationResult result) {
			if (result != null) {
				Longitude = result.getLongitude();
				Latitude = result.getLatitude();
				address = result.getAddress();
				showContent(true, false);
			} else {
				ToastOrder.makeText(QuestionnaireContentActivity.this, R.string.question_location, ToastOrder.LENGTH_SHORT).show();
				showContent(true, false);
			}

		}
	};

	/**
	 * 开始定位
	 * 
	 * @param listener
	 */
	public void startLocation(ReceiveLocationListener listener) {
		new LocationFactoy(this, listener).startLocationHH();
	}

	private boolean isSubmitOnce = false;

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sp_question_title_show://选择地点
			SearchDialogView sv = new SearchDialogView(this);
			sv.setInitList(list);
			sv.setOnItemClickListener(new SearchDialogView.OnSelectDataListener() {
				
				@Override
				public void onSelectData(SurveyAdress text) {
					addressSelected = text;
					if (text!=null) {
						
						sp_question_title_show.setText(text.getAdress());
					}
					
				}
			});
			sv.showDialog();
			
			
			break;
		case R.id.btn_question_content_back:
			findIngDetailDB.clearFindIngDetail();
			this.finish();
			break;

		case R.id.btn_question_preview:
			showContent(false, false);
			// 跳转到预览界面
			Intent intent_content = new Intent(getApplicationContext(), QuestionPreviewActivity.class);
			intent_content.putExtra("sId", addressSelected!=null?addressSelected.getSurveyAdressId()+"":"");
			intent_content.putExtra("addressSelected", addressSelected!=null?addressSelected.getAdress():"");
			intent_content.putExtra("qId", qId + "");
			startActivity(intent_content);
			break;
		case R.id.btn_question_content_explain:
			Intent intent_explain = new Intent(getApplicationContext(), QuestionExplainActivity.class);
			intent_explain.putExtra("qId", qId);
			startActivity(intent_explain);
			break;
		case R.id.btn_question_submit:
			if (!isSubmitOnce) {// 用于限定快速多次点击提交,只能点击一次
				// 提交按钮
				if (!QuestionnaireContentActivity.this.isFinishing()) {
					Dialog dialog = null;
					dialog = submitDialog(PublicUtils.getResourceString(QuestionnaireContentActivity.this,R.string.question_exit1));
					dialog.show();
				}
			}

			break;
		case R.id.btn_question_save:
			// 保存按钮
			showContent(false, true);
			SharedPreferencesUtil.getInstance(this).setIfSave(qId + "save", PublicUtils.getResourceString(this,R.string.yes));
			finish();
			break;
		default:
			break;
		}

	}

	private Dialog submitDialog(String bContent) {
		final Dialog backDialog = new Dialog(this, R.style.transparentDialog);
		View view = View.inflate(this, R.layout.back_dialog, null);
		TextView backContent = (TextView) view.findViewById(R.id.tv_back_content);
		Button confirm = (Button) view.findViewById(R.id.btn_back_confirm);
		Button cancel = (Button) view.findViewById(R.id.btn_back_cancel);
		backContent.setText(bContent);
		confirm.setText(PublicUtils.getResourceString(this,R.string.Confirm));
		confirm.setOnClickListener(new OnClickListener() {

			/**
			 * 用户确定
			 */
			@Override
			public void onClick(View v) {
				backDialog.dismiss();
				startLocation(receiveLocationListener);
				isSubmitOnce = true;
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			/**
			 * 取消退出
			 */
			@Override
			public void onClick(View v) {
				backDialog.dismiss();
			}
		});
		backDialog.setContentView(view);
		backDialog.setCancelable(false);// 将对话框设置为模态
		return backDialog;
	}
}
