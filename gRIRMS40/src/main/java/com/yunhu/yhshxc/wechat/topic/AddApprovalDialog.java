package com.yunhu.yhshxc.wechat.topic;

import gcg.org.debug.JLog;

import java.util.ArrayList;
import java.util.List;

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

public class AddApprovalDialog extends Dialog implements android.view.View.OnClickListener {

	private Context context;
	private RadioGroup rb_approval_if_double;
	private RadioButton rb_approval_one;
	private int type = Survey.TYPE_1;
	
	private Button btn_approval_submit;
	private Button btn_add_approval_content;
	private LinearLayout ll_add_approval;
	private ScrollView sv_approval;
	
	private CreateTopicActivity createTopicActivity;
	private List<AddApprovalSub> approvalSubList = new ArrayList<AddApprovalSub>();
	private List<AddApprovalSubView> approvalSubViewList = new ArrayList<AddApprovalSubView>();

	public AddApprovalDialog(Context context) {
		super(context);
		this.context = context;
		createTopicActivity = (CreateTopicActivity)context;
		// 加载布局文件
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		setTitle(R.string.wechat_content14);
		View view = inflater.inflate(R.layout.dialog_add_approval, null);
		sv_approval = (ScrollView)view.findViewById(R.id.sv_approval);
		ll_add_approval = (LinearLayout)view.findViewById(R.id.ll_add_approval);
		
		
		btn_approval_submit = (Button) view.findViewById(R.id.btn_approval_submit);
		btn_add_approval_content = (Button) view.findViewById(R.id.btn_add_approval_content);
		rb_approval_if_double = (RadioGroup) view.findViewById(R.id.rb_approval_if_double);
		rb_approval_one = (RadioButton) view.findViewById(R.id.rb_approval_one);

		btn_approval_submit.setOnClickListener(this);
		btn_add_approval_content.setOnClickListener(this);

		rb_approval_if_double
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if (checkedId != rb_approval_one.getId()) {
							rb_approval_one.setChecked(false);
							type = Survey.TYPE_2;
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

		AddApprovalSub s1 = new AddApprovalSub();
		s1.setNumber("1");
		s1.setContent(PublicUtils.getResourceString(context,R.string.wechat_content13));
		approvalSubList.add(s1);
		
		AddApprovalSubView sv1 = new AddApprovalSubView(context);
		sv1.setData(s1);
		approvalSubViewList.add(sv1);
		ll_add_approval.addView(sv1.getView());

		AddApprovalSub s2 = new AddApprovalSub();
		s2.setNumber("2");
		s2.setContent(PublicUtils.getResourceString(context,R.string.wechat_content12));
		approvalSubList.add(s2);
		
		AddApprovalSubView sv2 = new AddApprovalSubView(context);
		sv2.setData(s2);
		approvalSubViewList.add(sv2);
		ll_add_approval.addView(sv2.getView());
	}
	

	private void addSub(){
		AddApprovalSub sub = new AddApprovalSub();
		sub.setNumber(String.valueOf(approvalSubList.size()+1));
		approvalSubList.add(sub);
		AddApprovalSubView subView = new AddApprovalSubView(context);
		subView.setData(sub);
		approvalSubViewList.add(subView);
		ll_add_approval.addView(subView.getView());
		sv_approval.fullScroll(ScrollView.FOCUS_DOWN);
	}

	public void setMsg(String msg) {
	}

	public void setMsg(int msgId) {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_approval_submit:
			submit();
			break;
		case R.id.btn_add_approval_content:
//			ll_add_addproval_content.setVisibility(View.VISIBLE);
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
			for (int i = 0; i < approvalSubViewList.size(); i++) {
				JSONObject obj = new JSONObject();
				AddApprovalSubView view = approvalSubViewList.get(i);
				AddApprovalSub sub = view.getAddapprovalSub();
				if (!TextUtils.isEmpty(sub.getContent().toString().trim())) {
				obj.put(String.valueOf(index), sub.getContent());
				array.put(obj);
				index++;
				}
			}
			Survey  approval = new Survey();
			approval.setSurveyType(Survey.SURVEYTYPE__2);// 调查类
			approval.setType(type);
			approval.setOptions(array.toString());
			JLog.d("abby", "审批:options" + array.toString());
			createTopicActivity.setSurvey(approval);
		} catch (Exception e) {
			e.printStackTrace();
		}
		dismiss();
	}

}
